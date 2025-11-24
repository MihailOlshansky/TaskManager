package org.molsh.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.molsh.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenProvider {
    public static final int VALIDITY_IN_MILLISECONDS = 1000 * 60 * 60 * 24; // one day
    @Value("${token.signing.key}")
    private String jwtSigningKey;

    public String generateToken(User user) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + VALIDITY_IN_MILLISECONDS);

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRoles());

        return Jwts.builder()
                .id(user.getId().toString())
                .subject(user.getUsername())
                .issuedAt(now)
                .expiration(validity)
                .claims(claims)
                .signWith(getSigningKey())
                .compact();
    }

    public boolean validateToken(String token) {
        return extractAllClaims(token).getExpiration().after(new Date());
    }

    public String getUsernameFromToken(String token) {
        return extractAllClaims(token).getSubject();
    }

    private Claims extractAllClaims(String token) {
        SecretKey secretKey = new SecretKeySpec(jwtSigningKey.getBytes(StandardCharsets.UTF_8), "BASE64");
        Jws<Claims> jws = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token);
        return jws.getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
