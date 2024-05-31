package academy.mischok.todoapp.service.impl;

import academy.mischok.todoapp.dto.LoginPasswordDto;
import academy.mischok.todoapp.model.UserEntity;
import academy.mischok.todoapp.service.AuthenticationService;
import academy.mischok.todoapp.service.JwtService;
import academy.mischok.todoapp.service.UserService;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService, UserDetailsService {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userService.findUserByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public Optional<Cookie> buildCookie(UserDetails user) {
        return this.buildToken(user)
                .map(token -> {
                    final Cookie cookie = new Cookie("token", token);
                    cookie.setSecure(true);
                    cookie.setPath("/");
                    cookie.setMaxAge((int) Duration.ofHours(24).toSeconds());
                    return cookie;
                });
    }

    @Override
    public Optional<String> buildToken(UserDetails user) {
        return Optional.ofNullable(this.jwtService.buildToken(user));
    }

    @Override
    public UserEntity authenticateUser(String login, String password) {
        final LoginPasswordDto loginPasswordDto = new LoginPasswordDto(login, password);
        final Optional<UserEntity> optional = Optional.ofNullable(loginPasswordDto)
                .filter(dto -> Objects.nonNull(dto.password()))
                .filter(dto -> Objects.nonNull(dto.login()))
                .flatMap(dto -> this.userService.findByLogin(dto.login()));

        if (optional.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }
        return optional.filter(userEntity -> this.passwordEncoder.matches(
                        loginPasswordDto.password(),
                        userEntity.getPassword())
                )
                .orElseThrow(() -> new BadCredentialsException("Wrong Password"));
    }

    @Override
    public UserEntity authenticateUser(String token) {
        return Optional.ofNullable(token)
                .map(s -> this.jwtService.extractUsername(s))
                .flatMap(s -> this.userService.findUserByName(s))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}