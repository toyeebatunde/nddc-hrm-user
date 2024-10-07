package co.payrail.attendance_srv.config;

import co.payrail.attendance_srv.auth.security.JwtRequestFilter;
import co.payrail.attendance_srv.auth.service.ApiKeyService;
import co.payrail.attendance_srv.auth.service.implementation.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final ApiKeyService apiKeyService;

    public SecurityConfig(@Lazy UserService userService, TokenProvider tokenProvider, @Lazy ApiKeyService apiKeyService) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        this.apiKeyService = apiKeyService;
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(Arrays.asList("https://nyis.nddc.gov.ng","http://localhost:3000", "https://nddc.payrail.co","https://polite-field-0c4bc3b03.5.azurestaticapps.net")); // Allow all origins with credentials
        corsConfig.setAllowedMethods(Collections.singletonList("*")); // Allow all HTTP methods (GET, POST, PUT, DELETE, etc.)
        corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type")); // Explicitly allow 'Content-Type' header
        corsConfig.setExposedHeaders(Arrays.asList("Authorization")); // Optionally expose headers like Authorization
        corsConfig.setAllowCredentials(true); // Allow credentials like cookies or authentication tokens
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return source;
    }

    @Bean
    public JwtRequestFilter jwtRequestFilter() {
        return new JwtRequestFilter(tokenProvider, userService, apiKeyService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(c -> c.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/auth/*", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        http.addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
