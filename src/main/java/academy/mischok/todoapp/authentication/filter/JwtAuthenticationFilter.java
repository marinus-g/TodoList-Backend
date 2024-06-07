package academy.mischok.todoapp.authentication.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationProvider authenticationProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException,
            IOException {
        final Optional<Cookie> tokenCookie = Stream.of(Optional
                        .ofNullable(request.getCookies())
                        .orElse(new Cookie[0]))
                .filter(cookie -> "token".equals(cookie.getName()))
                .findFirst();

        tokenCookie.ifPresent(cookie -> SecurityContextHolder
                .getContext()
                .setAuthentication(authenticationProvider.authenticate(
                        new PreAuthenticatedAuthenticationToken(cookie.getValue(), null))
                )
        );
        tokenCookie.ifPresent(response::addCookie);
        filterChain.doFilter(request, response);
    }
}
