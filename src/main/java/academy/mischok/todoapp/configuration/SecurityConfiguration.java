package academy.mischok.todoapp.configuration;

import academy.mischok.todoapp.authentication.filter.JwtAuthenticationFilter;
import academy.mischok.todoapp.authentication.filter.UsernamePasswordAuthFilter;
import academy.mischok.todoapp.authentication.provider.AuthenticationProviderImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final UsernamePasswordAuthFilter usernamePasswordAuthFilter;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProviderImpl authenticationProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .exceptionHandling(configurer
                        -> configurer
                        .authenticationEntryPoint(authenticationEntryPoint))
                .addFilterBefore(usernamePasswordAuthFilter, BasicAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .logout(configurer -> configurer.deleteCookies("token"))
                .authorizeHttpRequests(registry -> {
                    registry
                            .requestMatchers(HttpMethod.POST, "/user").permitAll()
                            .requestMatchers(HttpMethod.POST, "/user/authenticate").permitAll()
                            .anyRequest().authenticated();
                });
        return http.build();
    }
}