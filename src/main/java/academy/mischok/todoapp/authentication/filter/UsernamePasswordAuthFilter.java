package academy.mischok.todoapp.authentication.filter;

import academy.mischok.todoapp.dto.LoginPasswordDto;
import academy.mischok.todoapp.service.AuthenticationService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class UsernamePasswordAuthFilter extends OncePerRequestFilter {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private final AuthenticationProvider authenticationProvider;
    private final AuthenticationService authenticationService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (("/user/authenticate".equals(request.getServletPath())
                || "/user/authenticate".equals(request.getPathInfo()))
                && "POST".equals(request.getMethod())) {
            final LoginPasswordDto loginPasswordDto = MAPPER
                    .readValue(request.getInputStream(),
                            LoginPasswordDto.class);
            final Authentication authentication = authenticationProvider.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginPasswordDto.login(),
                            loginPasswordDto.password()
                    )
            );
            SecurityContextHolder
                    .getContext()
                    .setAuthentication(
                            authentication
                    );
            if (authentication.getPrincipal() instanceof UserDetails userDetails) {
                authenticationService.buildCookie(userDetails)
                        .stream().peek(cookie -> System.out.println("Cookie: " + cookie))
                        .findFirst()
                        .ifPresent(cookie -> response.addCookie(cookie)
                        );
            }
        }
        filterChain.doFilter(request, response);
    }
}