package co.payrail.attendance_srv.config;

import co.payrail.attendance_srv.auth.entity.User;
import co.payrail.attendance_srv.auth.entity.Permission;
import co.payrail.attendance_srv.auth.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static co.payrail.attendance_srv.auth.dto.input.Constants.AUTHORITIES_KEY;

@Component
@Slf4j
@Data
@RequiredArgsConstructor
public class TokenProvider {
    private final UserRepository userRepository;

    @Value("${jjwt.secret.key}")
    private String secret;

    private Key key;

    private Long id;

    private String email;

    private String username;

    private String firstname;

    private String lastname;

    private List<String> permissions;

    private String roles;

    private String token;

    private Long parent;

    private Key secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public void setDetails(String token) {
        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);

        Claims claims = claimsJws.getBody();

        // Extract and set values, with null checks to avoid NullPointerException
        if (claims.get("userId") != null) {
           int  intValue = (int) claims.get("userId");
            String intToString = Integer.toString(intValue);
            this.id = Long.valueOf(intToString);
        }

        if (claims.get("parent") != null) {
            int  intValue = (int) claims.get("parent");
            String intToString = Integer.toString(intValue);
            this.parent = Long.valueOf(intToString);
        }

        this.email = claims.get("email") != null ? claims.get("email").toString() : null;
        this.firstname = claims.get("firstname") != null ? claims.get("firstname").toString() : null;
        this.username = claims.get("username") != null ? claims.get("username").toString() : null;

        this.lastname = claims.get("lastname") != null ? claims.get("lastname").toString() : null;
        this.roles = claims.get("role") != null ? claims.get("role").toString() : null;
//        this.permissions = claims.get("permissions") != null ? (List<String>) claims.get("permissions") : null;
        this.token = token;

        // Optionally log or handle cases where required fields are null
        if (this.id == null || this.username == null) {
            System.err.println("Missing required claims in the token");
        }

        System.out.println("USERID__F___ " + claims.get("userId"));
    }

    // Method to get username from the "username" claim in the JWT
    public String getUsernameFromJWTToken(String token) {
        return getClaimFromJWTToken(token, claims -> claims.get("username", String.class));
    }

    public Date getExpirationDateFromJWTToken(String token) {
        return getClaimFromJWTToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromJWTToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaimsFromJWTToken(token);
        System.out.println("Claims+++++" + claims);  // To inspect claims
        return claimsResolver.apply(claims);
    }

    public Claims getAllClaimsFromJWTToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)  // Assuming secretKey is correctly initialized
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean isJWTTokenExpired(String token) {
        Date expirationDate = getExpirationDateFromJWTToken(token);
        return expirationDate.before(new Date());
    }

    public String generateJWTToken(User user) {
        String roleName = Optional.ofNullable(user.getRole()).map(role -> role.getName()).orElse("USER");
        String authorities = Optional.ofNullable(user.getRole())
                .map(role -> role.getPermissions().stream()
                        .map(Permission::getCode)
                        .collect(Collectors.joining(",")))
                .orElse("");

        return Jwts.builder()
                .setSubject(user.getUserName()) // Sets the subject
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours expiration
                .claim("username", user.getUserName())  // Add "username" claim
                .claim("userId", user.getId())  // Add "userId" claim
                .claim("firstname", user.getFirstName())
                .claim("email", user.getEmail())
                .claim("lastname", user.getLastName())
                .claim("parent", user.getParent())
                .claim("role", roleName)
                .claim(AUTHORITIES_KEY, authorities)
                .setIssuedAt(new Date(System.currentTimeMillis()))  // Issue timestamp
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }


    public String generateTokenForVerification(String id) {
        return Jwts.builder()
                .setSubject(id)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 25200000L)) // 7 hours
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean validateJWTToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromJWTToken(token);
        return (username.equals(userDetails.getUsername()) && !isJWTTokenExpired(token));
    }

    public UsernamePasswordAuthenticationToken getAuthenticationToken(String token, Authentication existingAuth, UserDetails userDetails) {
        Claims claims = getAllClaimsFromJWTToken(token);
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }
}
