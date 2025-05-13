package org.example.rest.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.domain.entity.Client;
import org.example.rest.mapper.ClientMapper;
import org.example.rest.service.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl extends ServiceImpl<ClientMapper, Client> implements SmsService {

    private static final Logger log = LoggerFactory.getLogger(SmsServiceImpl.class);

    /**
     * 发送短信验证码
     *
     * @param phone   手机号
     * @param smsCode 短信验证码
     */
    @Override
    public void sendSmsCode(String phone, String smsCode) {
        // 这里实际项目中应该调用短信服务商的API发送短信
        // 本示例仅打印日志模拟发送短信
        log.info("向手机号 {} 发送短信验证码: {}", phone, smsCode);
    }
}
