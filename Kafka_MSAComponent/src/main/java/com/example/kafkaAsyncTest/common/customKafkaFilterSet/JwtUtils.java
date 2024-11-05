package com.example.kafkaAsyncTest.common.customKafkaFilterSet;

import com.example.kafkaAsyncTest.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;

@Component
public class JwtUtils {

    private final Key hmacKey;
    private final Long expirationTime;

    public JwtUtils(Environment env) {
        this.hmacKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(env.getProperty("token.secret")));
        this.expirationTime = Long.parseLong(Objects.requireNonNull(env.getProperty("token.expiration-time")));
    }

    public String generateToken(User user) {
        Instant now = Instant.now();
        return Jwts.builder()
                .claim("name", user.getFirstname())
                .claim("email", user.getEmail())
                .subject(user.getUsername())
                .id(String.valueOf(user.getUserId()))
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(expirationTime, ChronoUnit.MILLIS)))
                .signWith(hmacKey)
                .compact();
    }

    public Claims getAllClaimsFromToken(String token){
        Jws<Claims> jwt = Jwts.parser().verifyWith((SecretKey) hmacKey).build().parseSignedClaims(token);
        return jwt.getPayload();
    }

    public String getSubjectFromToken(String token) {
        final Claims claims = getAllClaimsFromToken(token);
        return claims.getSubject();
    }

    public Date getExpirationDateFromToken(String token) {
        final Claims claims = getAllClaimsFromToken(token);
        return claims.getExpiration();
    }

    private boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public boolean validateToken(String token, User user) {
        // 토큰 유효 기간 체크
        if (isTokenExpired(token))
            return false;

        // 토큰 내용 검증
        String subject = getSubjectFromToken(token);
        String username = user.getUsername();

        return subject != null && subject.equals(username);
    }
}