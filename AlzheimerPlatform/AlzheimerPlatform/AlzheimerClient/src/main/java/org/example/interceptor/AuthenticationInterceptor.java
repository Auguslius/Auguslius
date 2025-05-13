package org.example.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.example.common.Enum.LoginExceptionEnum;
import org.example.exception.BusinessException;
import org.example.utils.JwtUtil;
import org.example.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisUtil redisUtil;

    private static final String TOKEN_KEY_PREFIX = "TOKEN:";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) {
        // 获取token
        String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token)) {
            throw new BusinessException(LoginExceptionEnum.NOT_LOGIN.getCode(),
                    LoginExceptionEnum.NOT_LOGIN.getMessage());
        }

        // 验证token
        if (!jwtUtil.validateToken(token)) {
            throw new BusinessException(LoginExceptionEnum.TOKEN_INVALID.getCode(),
                    LoginExceptionEnum.TOKEN_INVALID.getMessage());
        }

        // 验证Redis中的token
        String uuid = jwtUtil.getUserUuidFromToken(token);
        String redisToken = (String) redisUtil.get(TOKEN_KEY_PREFIX + uuid);
        if (redisToken == null || !redisToken.equals(token)) {
            throw new BusinessException(LoginExceptionEnum.TOKEN_EXPIRED.getCode(),
                    LoginExceptionEnum.TOKEN_EXPIRED.getMessage());
        }

        return true;
    }
}