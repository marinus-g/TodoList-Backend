package academy.mischok.todoapp.service.impl;

import academy.mischok.todoapp.service.AuthenticationService;
import academy.mischok.todoapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService, UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userService.findUserByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
