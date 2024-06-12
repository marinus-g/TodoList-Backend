package academy.mischok.todoapp.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Optional;

public interface JwtService {

    String buildToken(final UserDetails user);

    boolean isTokenValid(final String token, UserDetails user);

    boolean isTokenExpired(final String token);

    Date extractExpiration(final String token);

    String extractUsername(final String token);

}