package org.example.web.rest.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.common.enums.CommonEnum;
import org.example.common.enums.InstitutionEnum;
import org.example.common.enums.MedicalRecordEnum;
import org.example.exception.BusinessException;
import org.example.domain.entity.Institution;
import org.example.domain.entity.Patient;
import org.example.domain.entity.PatientMedicalRecord;
import org.example.domain.entity.User;
import org.example.domain.dto.PageDTO;
import org.example.domain.query.PatientMedicalRecordQuery;
import org.example.domain.vo.PatientMedicalRecordVO;
import org.example.utils.ThreadLocalUtil;
import org.example.web.rest.mapper.InstitutionMapper;
import org.example.web.rest.mapper.PatientMedicalRecordMapper;
import org.example.web.rest.mapper.PatientMapper;
import org.example.web.rest.mapper.UserMapper;
import org.example.web.rest.service.PatientMedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
public class PatientMedicalRecordServiceImpl extends ServiceImpl<PatientMedicalRecordMapper, PatientMedicalRecord> implements PatientMedicalRecordService {

    @Autowired
    private PatientMedicalRecordMapper medicalRecordMapper;

    @Autowired
    private PatientMapper patientMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private InstitutionMapper institutionMapper;

    @Override
    @Transactional
    public Boolean addMedicalRecord(PatientMedicalRecord record) {
        // 验证数据
        if (record == null || record.getPatientUuid() == null) {
            throw new BusinessException(CommonEnum.FAIL.getCode(), MedicalRecordEnum.INCOMPLETE_INFO.getMessage());
        }

        // 获取当前登录用户信息
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        User user = userMapper.selectById(id);

        // 设置必要字段
        record.setRecordUuid(UUID.randomUUID().toString());
        QueryWrapper<Patient> queryNameWrapper = new QueryWrapper<>();
        queryNameWrapper.eq("uuid", record.getPatientUuid());
        Patient patient = patientMapper.selectOne(queryNameWrapper);
        record.setPatientName(patient.getName());

        record.setDoctorNumber(user.getNumber());
        record.setDoctorName(user.getNickname());
        record.setInstitution(user.getInstitution());

        // 根据机构名称查询机构UUID
        if (record.getInstitution() != null && !record.getInstitution().isEmpty()) {
            // 假设有一个institutionMapper用于查询机构信息
            // 查询条件：机构名称等于record.getInstitution()
            LambdaQueryWrapper<Institution> queryInstitutionWrapper = new LambdaQueryWrapper<>();
            queryInstitutionWrapper.eq(Institution::getInstitutionName, record.getInstitution());
            Institution institution = institutionMapper.selectOne(queryInstitutionWrapper);

            if (institution != null) {
                record.setInstitutionUuid(institution.getUuid());
            } else {
                 throw new BusinessException(CommonEnum.FAIL.getCode(), InstitutionEnum.Add_Category_Not_Exist.getMessage());
            }
        }
        // 插入数据库
        int insertResult = medicalRecordMapper.insert(record);
        if (insertResult <= 0) {
            throw new BusinessException(CommonEnum.FAIL.getCode(), MedicalRecordEnum.ADD_FAIL.getMessage());
        }

        return true;
    }
    @Override
    @Transactional
    public Boolean updateMedicalRecord(PatientMedicalRecord record) {
        // 验证数据
        if (record == null || record.getRecordUuid() == null) {
            throw new BusinessException(CommonEnum.FAIL.getCode(), MedicalRecordEnum.INCOMPLETE_INFO.getMessage());
        }

        // 查询原有病历记录
        PatientMedicalRecord existingRecord = getOne(
                new LambdaQueryWrapper<PatientMedicalRecord>()
                        .eq(PatientMedicalRecord::getRecordUuid, record.getRecordUuid())
        );

        if (existingRecord == null) {
            throw new BusinessException(CommonEnum.FAIL.getCode(), MedicalRecordEnum.RECORD_NOT_EXIST.getMessage());
        }

        // 获取当前登录用户信息
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        User user = userMapper.selectById(id);

        // 检查权限（医生只能修改自己的病历记录）
        if (user.getRole() == 0 && !existingRecord.getDoctorNumber().equals(user.getNumber())) {
            throw new BusinessException(CommonEnum.FAIL.getCode(), MedicalRecordEnum.NO_PERMISSION.getMessage());
        }

        // 保留不能修改的字段
        record.setPatientUuid(existingRecord.getPatientUuid());
        record.setPatientName(existingRecord.getPatientName());

        // 更新数据库
        boolean isUpdated = update(
                record,
                new LambdaQueryWrapper<PatientMedicalRecord>()
                        .eq(PatientMedicalRecord::getRecordUuid, record.getRecordUuid())
        );

        if (!isUpdated) {
            throw new BusinessException(CommonEnum.FAIL.getCode(), MedicalRecordEnum.UPDATE_FAIL.getMessage());
        }

        return true;
    }

    @Override
    @Transactional
    public Boolean removeMedicalRecordByUuid(String recordUuid) {
        // 验证数据
        if (recordUuid == null || recordUuid.isEmpty()) {
            throw new BusinessException(CommonEnum.FAIL.getCode(), MedicalRecordEnum.RECORD_UUID_EMPTY.getMessage());
        }

        // 查询病历记录
        PatientMedicalRecord record = getOne(
                new LambdaQueryWrapper<PatientMedicalRecord>()
                        .eq(PatientMedicalRecord::getRecordUuid, recordUuid)
        );

        if (record == null) {
            throw new BusinessException(CommonEnum.FAIL.getCode(), MedicalRecordEnum.RECORD_NOT_EXIST.getMessage());
        }

        // 获取当前登录用户信息
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        User user = userMapper.selectById(id);

        // 检查权限（医生只能删除自己的病历记录）
        if (user.getRole() == 0 && !record.getDoctorNumber().equals(user.getNumber())) {
            throw new BusinessException(CommonEnum.FAIL.getCode(), MedicalRecordEnum.NO_PERMISSION.getMessage());
        }

        // 删除数据
        boolean isDeleted = remove(
                new LambdaQueryWrapper<PatientMedicalRecord>()
                        .eq(PatientMedicalRecord::getRecordUuid, recordUuid)
        );

        if (!isDeleted) {
            throw new BusinessException(CommonEnum.FAIL.getCode(), MedicalRecordEnum.DELETE_FAIL.getMessage());
        }

        return true;
    }


    @Override
    public PageDTO<PatientMedicalRecordVO> queryMedicalRecordPage(PatientMedicalRecordQuery query) {
        String recordUuid = query.getRecordUuid();
        String patientUuid = query.getPatientUuid();
        String patientName = query.getPatientName();
        Integer doctorNumber = query.getDoctorNumber();
        String doctorName = query.getDoctorName();

        // 获取当前登录用户信息
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        User user = userMapper.selectById(id);
        Integer userRole = user != null ? user.getRole() : null;

        // 构建分页查询条件
        Page<PatientMedicalRecord> page = query.toMpPageDefaultSortByCreateTime();

        // 构建查询条件
        LambdaQueryWrapper<PatientMedicalRecord> queryWrapper = new LambdaQueryWrapper<PatientMedicalRecord>()
                .like(recordUuid != null, PatientMedicalRecord::getRecordUuid, recordUuid)
                .like(patientUuid != null, PatientMedicalRecord::getPatientUuid, patientUuid)
                .like(patientName != null, PatientMedicalRecord::getPatientName, patientName)
                .eq(doctorNumber != null, PatientMedicalRecord::getDoctorNumber, doctorNumber)
                .like(doctorName != null, PatientMedicalRecord::getDoctorName, doctorName);

        // 如果用户的角色是医生，则只查询该医生的病历
        if (userRole != null && userRole == 0) {
            queryWrapper.eq(PatientMedicalRecord::getDoctorNumber, user.getNumber());
        }

        // 执行分页查询
        Page<PatientMedicalRecord> p = medicalRecordMapper.selectPage(page, queryWrapper);

        // 将查询结果转换为VO
        return PageDTO.of(p, record -> BeanUtil.copyProperties(record, PatientMedicalRecordVO.class));
    }
}