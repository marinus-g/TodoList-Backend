package academy.mischok.todoapp.service.impl;

import academy.mischok.todoapp.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final static Duration TOKEN_EXPIRATION = Duration.ofDays(10);

    private final SecretKey signingKey;

    @Override
    public String buildToken(UserDetails user) {
        return Jwts
                .builder()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()
                        + TOKEN_EXPIRATION.toMillis())
                )
                .signWith(this.signingKey)
                .compact();
        /*
        @Bean
    public SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
         */
    }

    @Override
    public boolean isTokenValid(String token, UserDetails user) {
        final Claims claim = this.extractAllClaims(token);
        final String username = claim.getSubject();
        return username != null
                && username.equals(user.getUsername())
                && !this.isTokenExpired(token);
    }

    @Override
    public boolean isTokenExpired(String token) {
        return this.extractExpiration(token).before(new Date());
    }

    @Override
    public Date extractExpiration(String token) {
        return this.extractAllClaims(token).getExpiration();
    }

    @Override
    public String extractUsername(String token) {
        return this.extractAllClaims(token).getSubject();
    }

    private Claims extractAllClaims(final String token) {
        return Jwts
                .parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}