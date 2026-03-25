package com.app.security;

import com.app.config.JwtProperties;
import com.app.model.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
public class JwtUtils {

    private static final String CLAIM_JTI = "jti";
    private static final String CLAIM_ROLE = "role";

    private final JwtProperties jwtProperties;
    private final SecretKey key;

    public JwtUtils(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtProperties.secretKey()));
    }

    public record TokenWithJti(String jti, String token) {
    }

    public TokenWithJti generateRefreshToken(Long userId) {
        String jti = UUID.randomUUID().toString();

        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_JTI, jti);
        claims.put("tokenType", "refresh");
        String token = generateToken(userId, claims, jwtProperties.refreshTokenExpiration());
        return new TokenWithJti(jti, token);
    }

    public String generateAccessToken(UserEntity user) {
        return generateAccessTokenWithJti(user).token();
    }

    public TokenWithJti generateAccessTokenWithJti(UserEntity user) {
        Map<String, Object> claims = buildClaims(user);
        String jti = UUID.randomUUID().toString();
        claims.put(CLAIM_JTI, jti);
        String token = generateToken(user.getId(), claims, jwtProperties.accessTokenExpiration());
        return new TokenWithJti(jti, token);
    }

    public String generateToken(Long userId, Map<String, Object> extraClaims, long expiration) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(String.valueOf(userId))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    public String extractRole(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get(CLAIM_ROLE, String.class);
    }

    public boolean isTokenValid(String token, Long userId) {
        try {
            final Long extractUserId = extractUserId(token);
            return extractUserId.equals(userId) && !isTokenExpired(token);
            //CHECKSTYLE:OFF
        }
        catch (Exception e) {
            return false;
            //CHECKSTYLE:ON
        }
    }

    public String extractJti(String token) {
        return extractAllClaims(token).get(CLAIM_JTI, String.class);
    }

    public Long extractUserId(String token) {
        return Long.valueOf(extractClaim(token, Claims::getSubject));
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Map<String, Object> buildClaims(UserEntity user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_ROLE, user.getRole());
        return claims;
    }
}
