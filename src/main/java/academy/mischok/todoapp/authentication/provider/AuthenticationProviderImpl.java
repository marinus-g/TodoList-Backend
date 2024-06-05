package academy.mischok.todoapp.authentication.provider;

import academy.mischok.todoapp.model.UserEntity;
import academy.mischok.todoapp.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Component
@RequiredArgsConstructor
public class AuthenticationProviderImpl implements AuthenticationProvider {

    private final AuthenticationService authenticationService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final UserEntity user;
        final AtomicReference<String> tokenReference = new AtomicReference<>();
        if (authentication instanceof UsernamePasswordAuthenticationToken
                usernamePasswordAuthenticationToken) {
            user = Optional.ofNullable(this.authenticationService.authenticateUser(
                                    usernamePasswordAuthenticationToken.getPrincipal().toString(),
                                    usernamePasswordAuthenticationToken.getCredentials().toString()
                            )
                    )
                    .stream().peek(
                            userEntity -> tokenReference.set(
                                    this.authenticationService.buildToken(userEntity)
                                            .orElse(null)
                            )
                    )
                    .findFirst()
                    .orElse(null);
        } else if (authentication instanceof PreAuthenticatedAuthenticationToken) {
            user = this.authenticationService.authenticateUser(
                    authentication.getPrincipal().toString()
            );
            tokenReference.set(authentication.getPrincipal().toString());
        } else {
            throw new IllegalArgumentException("Unsupported authentication type");
        }

        return new UsernamePasswordAuthenticationToken(
                user,
                tokenReference.get(),
                Optional.of(user).map(UserEntity::getAuthorities).orElse(Collections.emptyList())
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
