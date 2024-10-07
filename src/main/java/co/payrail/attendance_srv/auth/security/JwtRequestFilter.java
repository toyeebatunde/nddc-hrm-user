package co.payrail.attendance_srv.auth.security;


import co.payrail.attendance_srv.auth.entity.ApiKey;
import co.payrail.attendance_srv.auth.service.UserService;
import co.payrail.attendance_srv.auth.service.ApiKeyService;
import co.payrail.attendance_srv.config.TokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final TokenProvider jwtUtil;

    private final ApiKeyService apiKeyService;



    @Autowired
    public JwtRequestFilter(TokenProvider jwtUtil, UserService userService, ApiKeyService apiKeyService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.apiKeyService = apiKeyService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");


        String apiKey = request.getHeader("api-key");

        String username = null;
        String jwt = null;

        if (!Objects.isNull(apiKey)) {

            ApiKey key = apiKeyService.findByKey(apiKey);
            if (key != null) {
                username = key.getUser().getUserName();
            }
        }else if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            System.out.println("______jwt+++++ "+jwt);
            try {
                System.out.println("______username0++++ "+username);
                username = jwtUtil.getUsernameFromJWTToken(jwt);
                System.out.println("______username++++ "+username);
            } catch (IllegalArgumentException e) {
                logger.error("An error has occurred while fetching username from token", e);
                throw new IllegalArgumentException(e.getMessage());
            } catch (ExpiredJwtException e) {
                logger.warn("The token has expired", e);
                throw new JwtException(e.getMessage());
            } catch (SignatureException e) {
                logger.error("Authentication failed. Username or password not valid");
                throw new JwtException(e.getMessage());
            }
        } else {
            logger.warn("Couldn't find bearer string header will be ignored");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService.loadUserByUsername(username);

            if (jwtUtil.validateJWTToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                setUserDetailsInTokenProvider(jwt);
            }
        }

        filterChain.doFilter(request, response);
    }
    private void setUserDetailsInTokenProvider(String authToken) {
        jwtUtil.setDetails(authToken);
    }
}
