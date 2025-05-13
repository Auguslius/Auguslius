package org.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 科大讯飞API配置类
 */
@Component
public class XfyunConfig {
    
    @Value("${xfyun.appId}")
    private String appId;
    
    @Value("${xfyun.secretKey}")
    private String secretKey;
    
    public String getAppId() {
        return appId;
    }
    
    public String getSecretKey() {
        return secretKey;
    }
} 