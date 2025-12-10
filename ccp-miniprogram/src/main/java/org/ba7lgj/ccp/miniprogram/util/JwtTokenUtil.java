package org.ba7lgj.ccp.miniprogram.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.ba7lgj.ccp.miniprogram.context.UserContextHolder;

public class JwtTokenUtil {
    private static final String SECRET = "ccp-miniprogram-secret-key";
    private static final long EXPIRATION_MILLIS = 7 * 24 * 60 * 60 * 1000L;

    public static String generateToken(Long userId) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + EXPIRATION_MILLIS);
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    public static Long parseUserId(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
            String subject = claims.getSubject();
            return Long.parseLong(subject);
        } catch (Exception ex) {
            return null;
        }
    }

    public static Long getCurrentUserId() {
        return UserContextHolder.getUserId();
    }
}
