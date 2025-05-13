package org.example.rest.controller;


import org.example.common.result.Result;
import org.example.domain.dto.PatientAuthenticationDto;
import org.example.domain.vo.PatientAuthenticationVO;
import org.example.rest.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 认证服务接口
 *
 * @author lyx
 */
@RestController
@RequestMapping("/patients")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    /**
     * 用户患者认证
     *
     * @param patientAuthenticationDto 患者认证DTO
     * @return 认证结果
     */
    @PostMapping("/authentication")
    public Result<?> PatientAuthentication(@RequestBody PatientAuthenticationDto patientAuthenticationDto) {
        return Result.success(authenticationService.authenticatePatient(patientAuthenticationDto));
    }

    /**
     * 用户患者认证检测
     *
     * @param uuid uuid
     * @return 认证信息
     */
    @GetMapping("/isAuthentication/{uuid}")
    public Result<PatientAuthenticationVO> isAuthentication(@PathVariable String uuid) {
        PatientAuthenticationVO patientAuthenticationVO = authenticationService.getPatientAuthentication(uuid);
        return Result.success(patientAuthenticationVO);
    }
}
