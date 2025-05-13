package org.example.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.example.utils.JwtUtil; // 添加这一行以导入 JwtUtil 类

import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {

        @Autowired
        private StringRedisTemplate stringRedisTemplate;

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

            String token = request.getHeader("Authorization");

            try {
                ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
                String redisToken = operations.get(token);
                if(redisToken == null){
                    throw new RuntimeException("token失效");
                }
                Map<String, Object> claims = JwtUtil.parseToken(token);
                // 把业务数据存储到ThreadLocal中
                ThreadLocalUtil.set(claims);
                return true;
            } catch (Exception e){
                response.setStatus(401);
                return false;
            }

        }

}
