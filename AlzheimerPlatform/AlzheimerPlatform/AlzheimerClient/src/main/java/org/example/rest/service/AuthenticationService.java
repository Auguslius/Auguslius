package org.example.rest.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.example.domain.dto.PatientAuthenticationDto;
import org.example.domain.vo.PatientAuthenticationVO;
import org.example.domain.entity.Patient;

/**
 * 认证服务接口
 *
 * @author lyx
 */
public interface AuthenticationService extends IService<Patient> {

    /**
     * 用户患者认证
     *
     * @param patientAuthenticationDto 患者认证DTO
     * @return 认证结果
     */
    Boolean authenticatePatient(PatientAuthenticationDto patientAuthenticationDto);

    /**
     * 用户患者认证检测
     *
     * @param uuid uuid
     * @return 认证信息
     */
    PatientAuthenticationVO getPatientAuthentication(String uuid);


}
