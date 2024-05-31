package academy.mischok.todoapp.service;

import academy.mischok.todoapp.model.UserEntity;
import jakarta.servlet.http.Cookie;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface AuthenticationService {

    Optional<Cookie> buildCookie(UserDetails user);

    Optional<String> buildToken(UserDetails user);

    UserEntity authenticateUser(String login, String password);

    UserEntity authenticateUser(String token);
}
