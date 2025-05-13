package org.example.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 生成token
     */
    public String generateToken(String uuid, String username) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expiration * 1000);

        return JWT.create()
                .withClaim("uuid", uuid)
                .withClaim("username", username)
                .withIssuedAt(now)
                .withExpiresAt(expireDate)
                .sign(Algorithm.HMAC256(secret));
    }

    /**
     * 验证token
     */
    public boolean validateToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            // 验证必要的claim是否存在
            String uuid = jwt.getClaim("uuid").asString();
            String username = jwt.getClaim("username").asString();
            return StringUtils.isNotBlank(uuid) && StringUtils.isNotBlank(username);
        } catch (Exception e) {
            System.out.println("token验证失败"+e);
            return false;
        }
    }

    /**
     * 从token中获取uuid
     */
    public String getUserUuidFromToken(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim("uuid").asString();
    }
    /**
     * 从token中获取用户名
     */
    public String getUsernameFromToken(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim("username").asString();
    }
}