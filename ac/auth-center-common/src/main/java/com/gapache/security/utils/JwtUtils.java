package com.gapache.security.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

/**
 * @author HuSen
 * @since 2020/7/31 3:27 下午
 */
@Slf4j
public class JwtUtils {

    private static final String CONTENT = "content";

    public static String generateToken(String content, PrivateKey privateKey, Long timeout) {
        return Jwts.builder()
                .claim(CONTENT, content)
                .setExpiration(new Date(System.currentTimeMillis() + timeout))
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }

    public static String parseToken(String token, PublicKey publicKey) {
        try {
            return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token)
                    .getBody().get(CONTENT, String.class);
        } catch (Exception e) {
            log.error("parseToken error:{}", e.getMessage());
            return null;
        }
    }
}
