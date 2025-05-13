package org.example.rest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.domain.entity.Client;

/**
 * 短信服务接口
 *
 * @author lyx
 */
public interface SmsService extends IService<Client> {

    /**
     * 发送短信验证码
     *
     * @param phone   手机号
     * @param smsCode 短信验证码
     */
    void sendSmsCode(String phone, String smsCode);

}
