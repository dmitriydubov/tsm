package com.example.tms.jwt;

import com.example.tms.service.impl.UserDetailsImpl;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;

@Component
@Slf4j
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetime}")
    private Duration lifetime;

    public String generateToken(UserDetailsImpl userDetails) {
        return generateTokenFromEmail(userDetails.getEmail());
    }

    public String generateToken(String email) {
        return generateTokenFromEmail(email);
    }

    public String generateTokenFromEmail(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + lifetime.toMillis()))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public String getEmail(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public Boolean validate(String authToken) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            log.error("Неверная подпись токена {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.error("Невалидный токен {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.error("Токен просрочен {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("Токен не поддерживается {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("пустые строки claims {}", ex.getMessage());
        }
        return false;
    }
}
