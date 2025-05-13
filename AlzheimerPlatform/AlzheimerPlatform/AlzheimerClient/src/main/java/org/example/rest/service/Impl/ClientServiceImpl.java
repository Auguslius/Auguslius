package org.example.rest.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.common.Enum.InstitutionDoctorEnum;
import org.example.domain.dto.BindDoctorDto;
import org.example.domain.entity.Client;
import org.example.exception.BusinessException;
import org.example.domain.entity.Patient;
import org.example.domain.entity.User;
import org.example.rest.mapper.ClientMapper;
import org.example.rest.service.ClientService;
import org.example.web.rest.mapper.PatientMapper;
import org.example.web.rest.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl extends ServiceImpl<ClientMapper, Client> implements ClientService {

    @Autowired
    private ClientMapper clientMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PatientMapper patientMapper;

    private static final String NUMBER = "number";

    private static final String UUID = "uuid";

    private static final int DEFAULT_DOCTOR_NUMBER = 887375;


    // 绑定医生
    @Override
    public boolean bindDoctorByDoctorNumber(BindDoctorDto bindDoctorDto){
        String clientUuid = bindDoctorDto.getUuid();
        int doctorNumber = bindDoctorDto.getDoctorNumber();

        // 1. 查询医生
        User doctor = userMapper.selectOne(
                new QueryWrapper<User>().eq(NUMBER, doctorNumber)
        );
        if (doctor == null) {
            throw new BusinessException(
                    InstitutionDoctorEnum.DOCTOR_NOT_EXIST.getCode(),
                    InstitutionDoctorEnum.DOCTOR_NOT_EXIST.getMessage());
        }

        // 2. 查询 client
        Client client = clientMapper.selectOne(
                new QueryWrapper<Client>().eq(UUID, clientUuid)
        );
        if (client == null) {
            throw new BusinessException(
                    InstitutionDoctorEnum.CLIENT_NOT_EXIST.getCode(),
                    InstitutionDoctorEnum.CLIENT_NOT_EXIST.getMessage());
        }

        // 3. 查询 patient
        Patient patient = patientMapper.selectOne(
                new QueryWrapper<Patient>().eq(UUID, clientUuid)
        );
        if (patient == null) {
            throw new BusinessException(
                    InstitutionDoctorEnum.Patient_NOT_EXIST.getCode(),
                    InstitutionDoctorEnum.Patient_NOT_EXIST.getMessage());
        }

        // 4. 校验当前医生是否可覆盖
        Integer currentDoctorNumber = patient.getDoctorNumber();
        if (currentDoctorNumber != null) {
            User currentDoctor = userMapper.selectOne(
                    new QueryWrapper<User>().eq(NUMBER, currentDoctorNumber)
            );
            if (currentDoctor != null && currentDoctor.getRole() != 1) {
                throw new BusinessException(
                        InstitutionDoctorEnum.DOCTOR_ALREADY_BOUND.getCode(),
                        InstitutionDoctorEnum.DOCTOR_ALREADY_BOUND.getMessage()
                );
            }
        }

        // 5. 更新绑定
        patient.setDoctorNumber(doctorNumber);
        int update = patientMapper.update(
                patient,
                new UpdateWrapper<Patient>().eq(UUID, patient.getUuid())
        );

        if (update <= 0) {
            throw new BusinessException(
                    InstitutionDoctorEnum.BIND_UPDATE_FAILED.getCode(),
                    InstitutionDoctorEnum.BIND_UPDATE_FAILED.getMessage()
            );
        }

        return true;
    }

    @Override
    public boolean unbindDoctorByDoctorNumber(BindDoctorDto bindDoctorDto) {
        String clientUuid = bindDoctorDto.getUuid();

        Patient patient = patientMapper.selectOne(
                new QueryWrapper<Patient>().eq(UUID, clientUuid)
        );

        if (patient == null) {
            throw new BusinessException(
                    InstitutionDoctorEnum.Patient_NOT_EXIST.getCode(),
                    InstitutionDoctorEnum.Patient_NOT_EXIST.getMessage()
            );
        }

        patient.setDoctorNumber(DEFAULT_DOCTOR_NUMBER);

        int update = patientMapper.update(
                patient,
                new UpdateWrapper<Patient>().eq(UUID, clientUuid)
        );

        if (update <= 0) {
            throw new BusinessException(
                    InstitutionDoctorEnum.BIND_UPDATE_FAILED.getCode(),
                    InstitutionDoctorEnum.BIND_UPDATE_FAILED.getMessage()
            );
        }

        return true;
    }

}
