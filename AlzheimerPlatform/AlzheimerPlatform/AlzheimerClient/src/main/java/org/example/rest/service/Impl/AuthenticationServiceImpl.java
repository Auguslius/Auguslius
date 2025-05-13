package org.example.rest.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.example.common.Enum.AuthenticationExceptionEnum;
import org.example.common.Enum.ResultCode;
import org.example.domain.dto.PatientAuthenticationDto;
import org.example.domain.entity.Client;
import org.example.domain.vo.PatientAuthenticationVO;
import org.example.exception.BusinessException;
import org.example.domain.entity.Patient;
import org.example.rest.mapper.AuthenticationMapper;
import org.example.rest.mapper.ClientMapper;
import org.example.rest.service.AuthenticationService;
import org.example.utils.AgeCalculatorUtil;
import org.example.utils.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 认证服务接口
 *
 * @author lyx
 */
@Service
public class AuthenticationServiceImpl extends ServiceImpl<AuthenticationMapper, Patient> implements AuthenticationService {

    @Autowired
    private AuthenticationMapper authenticationMapper;

    @Autowired
    private ClientMapper clientMapper;


    /**
     * 用户患者认证
     *
     * @param patientAuthenticationDto 患者认证DTO
     * @return 认证结果
     */
    @Override
    public Boolean authenticatePatient(PatientAuthenticationDto patientAuthenticationDto) {
        // 先检查uuid是否存在
        if (StringUtils.isBlank(patientAuthenticationDto.getUuid())) {
            throw new BusinessException(ResultCode.FAIL.getCode(), AuthenticationExceptionEnum.PATIENT_NOT_EXIST.getMessage());
        }

        // 检查客户是否存在
        Client client;
        try {
            QueryWrapper<Client> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("uuid", patientAuthenticationDto.getUuid());
            client = clientMapper.selectOne(queryWrapper);
            if (client == null) {
                throw new BusinessException(ResultCode.FAIL.getCode(), AuthenticationExceptionEnum.PATIENT_NOT_EXIST.getMessage());
            }
        } catch (Exception e) {
            throw new BusinessException(ResultCode.FAIL.getCode(),AuthenticationExceptionEnum.PATIENT_NOT_EXIST.getMessage());
        }

        try {
            // 插入患者表
            // 忽略id
            Patient patient = BeanUtil.copyProperties(patientAuthenticationDto, Patient.class, "id");
            // 使用封装好的 stringToDate 方法转换
            Date birthDate = DateUtils.stringToDate(patient.getBirthDate());
            // 计算年龄
            patient.setAge(AgeCalculatorUtil.calculateAge(birthDate));
            patient.setUpdateTime(new Date());
            patient.setCreateTime(new Date());
            authenticationMapper.insert(patient);

            // 更新客户表
            client.setEmail(patientAuthenticationDto.getEmail());
            client.setPhone(patientAuthenticationDto.getPhone());
            client.setRealName(patientAuthenticationDto.getName());
            client.setUpdateTime(new Date());

            // 使用UpdateWrapper更新指定客户
            UpdateWrapper<Client> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("uuid", client.getUuid());
            clientMapper.update(client, updateWrapper);
            return true;
        } catch (Exception e) {
            // 捕获并转换异常，提供更详细的错误信息
            System.out.println("异常信息：" + e.getMessage());
            throw new BusinessException(ResultCode.FAIL.getCode(), AuthenticationExceptionEnum.PATIENT_AUTHENTICATION_FAIL.getMessage());
        }
    }

    /**
     * 用户患者认证检测
     *
     * @param uuid uuid
     * @return 认证信息
     */
    @Override
    public PatientAuthenticationVO getPatientAuthentication(String uuid) {
        if (StringUtils.isBlank(uuid)){
            throw new BusinessException(ResultCode.FAIL.getCode(), AuthenticationExceptionEnum.PATIENT_NOT_EXIST.getMessage());
        }
        try {
            QueryWrapper<Patient> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("uuid", uuid);
            Patient patient = authenticationMapper.selectOne(queryWrapper);

            PatientAuthenticationVO vo = new PatientAuthenticationVO();
            // 仅复制 VO 中存在的字段
            BeanUtils.copyProperties(patient, vo);
            return vo;
        } catch (BeansException e) {
            throw new BusinessException(ResultCode.FAIL.getCode(), AuthenticationExceptionEnum.PATIENT_NOT_EXIST.getMessage());
        }
    }

}
