package org.example.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtil {

    private static final String KEY= "healthcare";

    public static String generateToken(Map<String, Object> claims) {
        return JWT.create()
                .withClaim("claim",claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 )) // 设置过期时间
                .sign(Algorithm.HMAC256(KEY));// 设置密钥
    }

    public static Map<String, Object>parseToken(String token){
        return JWT.require(Algorithm.HMAC256(KEY))
                .build()// 构建验证器
                .verify(token)
                .getClaim("claim")
                .asMap();
    }
}
