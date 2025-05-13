package org.example.web.rest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.domain.entity.UserPasswordResetRequest;
import org.example.domain.dto.PasswordResetDTO;
import org.example.domain.dto.PasswordResetRequestDTO;

public interface PasswordResetService extends IService<UserPasswordResetRequest>{
    String requestPasswordReset(PasswordResetRequestDTO passwordResetRequestDTO);

    Boolean validatePasswordResetRequest(String token);

    Boolean resetPassword(PasswordResetDTO passwordResetDTO);
}
