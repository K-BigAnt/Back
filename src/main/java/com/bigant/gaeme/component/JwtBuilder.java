package com.bigant.gaeme.component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.Duration;
import java.time.Instant;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtBuilder {

    private final SecretKey key;

    public JwtBuilder(@Value("${jwt.secret}") String jwtSecret) {
        key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String createJwt(Long id) {
        Duration expirationDuration = Duration.ofHours(1);

        return Jwts.builder()
                .id(id.toString())
                .expiration(Date.from(Instant.now().plusMillis(expirationDuration.toMillis())))
                .signWith(key)
                .compact();
    }

    public Long decryptJwt(String jwtToken) {
        try {
            String id = Jwts.parser().verifyWith(key).build().parseSignedClaims(jwtToken).getPayload().getId();

            return Long.parseLong(id);
        } catch (RuntimeException exception) {
            return null;
        }
    }

}