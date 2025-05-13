package org.example.web.rest.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.common.enums.CommonEnum;
import org.example.common.enums.PasswordResetEnum;
import org.example.common.result.Result;
import org.example.domain.dto.PasswordResetDTO;
import org.example.domain.dto.PasswordResetRequestDTO;
import org.example.domain.group.CreateGroup;
import org.example.domain.group.QueryGroup;
import org.example.web.rest.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "密码找回管理")
@RequestMapping("password-reset")
@RestController
public class PasswordResetController {
    @Autowired
    private PasswordResetService passwordResetService;
    /**
     * 请求密码重置
     *
     * @param passwordResetRequestDTO 密码重置请求实体类
     * @return
     */
    @RequestMapping(value = "/requestPwd", method = RequestMethod.POST)
    @Operation(summary = "请求密码重置")
    public Result<String> requestPasswordReset(@RequestBody @Validated(CreateGroup.class) PasswordResetRequestDTO passwordResetRequestDTO){
        String token = passwordResetService.requestPasswordReset(passwordResetRequestDTO);
        return Result.success(CommonEnum.SUCCESS.getCode(), PasswordResetEnum.PASSWORD_RESET_REQUEST_SUCCESS.getMessage(), token);
    }

    @RequestMapping(value = "/validate", method = RequestMethod.GET)
    @Operation(summary = "验证重置密码请求")
    public Result<Boolean> validatePasswordResetRequest(@Validated(QueryGroup.class) String token) {
        Boolean result = passwordResetService.validatePasswordResetRequest(token);
        return Result.success(CommonEnum.SUCCESS.getCode(), PasswordResetEnum.PASSWORD_VALIDATED_SUCCESS.getMessage(), result);
    }

    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    @Operation(summary = "重置密码")
    public Result<Boolean> resetPassword(@RequestBody @Validated(CreateGroup.class) PasswordResetDTO passwordResetDTO) {
        Boolean result = passwordResetService.resetPassword(passwordResetDTO);
        return Result.success(CommonEnum.SUCCESS.getCode(), PasswordResetEnum.PASSWORD_RESET_SUCCESS.getMessage(), result);
    }
}
