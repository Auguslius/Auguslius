package org.example.config;

import org.example.interceptor.AuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AuthenticationInterceptor authenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 登录接口和注册接口不拦截
        registry.addInterceptor(authenticationInterceptor).excludePathPatterns("/login/password", "/login/register", "/login/code","/mmse-answers/submit","/mmse-answers/score","/mmse-answers/getAnswer/{patientUuid}","/mmse-answers/getAllAnswer","/mmse-answers/scoreDistribution");
    }
}
