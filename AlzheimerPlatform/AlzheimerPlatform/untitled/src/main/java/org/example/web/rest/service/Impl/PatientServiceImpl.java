package org.example.web.rest.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.common.enums.CommonEnum;
import org.example.common.enums.PatientEnum;
import org.example.exception.BusinessException;
import org.example.domain.entity.Patient;
import org.example.domain.entity.PatientMedicalRecord;
import org.example.domain.entity.User;
import org.example.domain.dto.PageDTO;
import org.example.domain.query.PatientQuery;
import org.example.domain.vo.PatientCountVO;
import org.example.domain.vo.PatientVO;
import org.example.utils.AgeCalculatorUtil;
import org.example.utils.DateUtils;
import org.example.utils.ThreadLocalUtil;
import org.example.web.rest.mapper.PatientMapper;
import org.example.web.rest.mapper.PatientMedicalRecordMapper;
import org.example.web.rest.mapper.UserMapper;
import org.example.web.rest.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

import static org.example.utils.desensitizeUitl.desensitize;

@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements PatientService {

    @Autowired
    private PatientMapper patientMapper;

    @Autowired
    private PatientMedicalRecordMapper patientMedicalRecordMapper;

    @Autowired
    private UserMapper userMapper;

    // 定义最大患者数量常量
    private static final int MAX_PATIENTS_PER_DOCTOR = 40;

    @Transactional
    public Boolean addPatient(Patient patient) {

        QueryWrapper<Patient> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("doctor_number", patient.getDoctorNumber());
        long doctorPatientCount = patientMapper.selectCount(queryWrapper);
        // 如果该医生已有超过50个患者，抛出BusinessException
        if (doctorPatientCount >= MAX_PATIENTS_PER_DOCTOR) {
            throw new BusinessException(CommonEnum.FAIL.getCode(), PatientEnum.DOCTOR_NO_SURPLUS.getMessage());
        }

        patient.setUuid(UUID.randomUUID().toString());

        // 使用封装好的 stringToDate 方法转换
        Date birthDate = DateUtils.stringToDate(patient.getBirthDate());

        // 计算年龄
        patient.setAge(AgeCalculatorUtil.calculateAge(birthDate));
        System.out.println("年龄：" + patient.getAge());
        // 设置创建时间和更新时间
        patient.setCreateTime(new Date());
        patient.setUpdateTime(new Date());

        // 插入患者信息
        int patientInsertResult = patientMapper.insert(patient);
        if (patientInsertResult <= 0) {
            throw new BusinessException(CommonEnum.FAIL.getCode(), PatientEnum.ADD_FAIL.getMessage());
        }

        // 插入患者病历信息
        PatientMedicalRecord patientMedicalRecord = new PatientMedicalRecord();
        patientMedicalRecord.setPatientUuid(patient.getUuid());
        patientMedicalRecord.setRecordUuid(UUID.randomUUID().toString());
        patientMedicalRecord.setPatientName(patient.getName());
        patientMedicalRecord.setDoctorNumber(patient.getDoctorNumber());
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("number", patient.getDoctorNumber());
        User user = userMapper.selectOne(userQueryWrapper);
        patientMedicalRecord.setDoctorName(user.getNickname());
        patientMedicalRecord.setDiagnosisPic("https://healthplantformbucket.oss-cn-shanghai.aliyuncs.com/d2c24aba-8f4b-4610-817a-952cc5030f01.png");


        int recordInsertResult = patientMedicalRecordMapper.insert(patientMedicalRecord);
        System.out.println("病历记录插入成功setRecordUuid");
        if (recordInsertResult <= 0) {
            throw new BusinessException(CommonEnum.FAIL.getCode(), PatientEnum.ADD_FAIL.getMessage());
        }

        return true;
    }


    @Override
    public Boolean updatePatient(Patient patient) {
        // 校验患者信息是否为空
        if (patient == null || patient.getUuid() == null) {
            throw new BusinessException(CommonEnum.FAIL.getCode(), PatientEnum.UPDATE_FAIL.getMessage());
        }

        // 校验手机号是否唯一（如果手机号发生变化）
        if (patient.getPhone() != null && !patient.getPhone().equals(patient.getPhone())) {
            // 判断手机号是否已被其他患者使用
            QueryWrapper<Patient> phoneQueryWrapper = new QueryWrapper<>();
            phoneQueryWrapper.eq("phone", patient.getPhone());
            // 排除当前患者自己
            phoneQueryWrapper.ne("uuid", patient.getUuid());
            long existingPhoneCount = patientMapper.selectCount(phoneQueryWrapper);
            if (existingPhoneCount > 0) {
                throw new BusinessException(CommonEnum.FAIL.getCode(), "手机号已存在，无法更新");
            }
        }

        QueryWrapper<Patient> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("doctor_number", patient.getDoctorNumber());
        long doctorPatientCount = patientMapper.selectCount(queryWrapper);

        // 如果该医生已有超过50个患者，抛出BusinessException
        if (doctorPatientCount >= MAX_PATIENTS_PER_DOCTOR) {
            throw new BusinessException(CommonEnum.FAIL.getCode(), PatientEnum.DOCTOR_NO_SURPLUS.getMessage());
        }

        patient.setUpdateTime(new Date());
        // 使用封装好的 stringToDate 方法转换
        Date birthDate = DateUtils.stringToDate(patient.getBirthDate());
        // 计算年龄
        patient.setAge(AgeCalculatorUtil.calculateAge(birthDate));
        // 设置创建时间和更新时间
        // 更新患者信息
        boolean isUpdated = update(patient, new LambdaQueryWrapper<Patient>().eq(Patient::getUuid, patient.getUuid()));
        if (!isUpdated) {
            throw new BusinessException(CommonEnum.FAIL.getCode(), PatientEnum.UPDATE_FAIL.getMessage());
        }
        return true;
    }

    @Override
    public Boolean removePatientByUuid(String uuid) {
        // 使用 MyBatis-Plus 的 remove 方法
        boolean isDeleted = this.remove(new QueryWrapper<Patient>().eq("uuid", uuid));

        if (isDeleted) {
            return true;
        } else {
            throw new BusinessException(CommonEnum.FAIL.getCode(), PatientEnum.DELETE_FAIL.getMessage());
        }
    }


    @Override
    public PatientVO getPatientById(String uuid) {
        // 查询患者信息
        Patient patient = getOne(new LambdaQueryWrapper<Patient>().eq(Patient::getUuid, uuid));
        if (patient == null) {
            throw new BusinessException(CommonEnum.FAIL.getCode(), PatientEnum.QUERY_FAIL.getMessage());
        }
        // 转换为PatientVO对象
        return BeanUtil.copyProperties(patient, PatientVO.class);
    }

    @Override
    public PageDTO<PatientVO> queryPatientPage(PatientQuery patientQuery) {
        String uuid = patientQuery.getUuid();
        String name = patientQuery.getName();
        String idCard = patientQuery.getIdCard();
        String phone = patientQuery.getPhone();

        // 从 ThreadLocal 获取当前线程的用户信息
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        User user = userMapper.selectById(id);  // 查询用户角色
        Integer userRole = user != null ? user.getRole() : null;  // 获取用户角色

        // 构建分页查询条件
        Page<Patient> page = patientQuery.toMpPageDefaultSortByCreateTime();
        // 使用 LambdaQueryWrapper 构建查询条件
        LambdaQueryWrapper<Patient> queryWrapper = new LambdaQueryWrapper<Patient>()
                .like(uuid != null, Patient::getUuid, uuid)
                .like(name != null, Patient::getName, name)
                .like(idCard != null, Patient::getIdCard, idCard)
                .like(phone != null, Patient::getPhone, phone);

        // 如果用户的角色是0（医生），则增加过滤条件，只查询该医生的患者
        if (userRole != null && userRole == 0) {
            queryWrapper.eq(Patient::getDoctorNumber, user.getNumber());  // 只查询当前医生的患者
        }

        // 执行分页查询
        Page<Patient> p = patientMapper.selectPage(page, queryWrapper);

        // 将查询结果转换为 PatientVO，并对 uuid 和 idCard 进行脱敏处理
        return PageDTO.of(p, patient -> {
            PatientVO patientVO = BeanUtil.copyProperties(patient, PatientVO.class);

            // 对 idCard 进行脱敏处理
            if (patientVO.getIdCard() != null && patientVO.getIdCard().length() > 4) {
                patientVO.setIdCard(desensitize(patientVO.getIdCard(), 3, 4));
            }

            return patientVO;
        });
    }

    @Override
    public PatientCountVO QueryPatientCount() {
        PatientCountVO patientCountVO = new PatientCountVO();

        int maleCount = Math.toIntExact(patientMapper.selectCount(new QueryWrapper<Patient>().eq("gender", 1)));

        int femaleCount = Math.toIntExact(patientMapper.selectCount(new QueryWrapper<Patient>().eq("gender", 2)));

        int totalCount = Math.toIntExact(patientMapper.selectCount(new QueryWrapper<Patient>().eq("is_dead",0)));

        patientCountVO.setMaleCount(maleCount);
        patientCountVO.setFemaleCount(femaleCount);
        patientCountVO.setTotalCount(totalCount);

        return patientCountVO;
    }

    @Override
    public List<Map<String, Object>> countNewPatientsInLastFiveDays() {

        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        // 查询用户角色
        User user = userMapper.selectById(id);

        // 获取当前医生的编号
        Integer doctorNumber = user.getNumber();

        // 获取近五天的日期列表
        List<String> dateList = getLastFiveDays();

        // 查询每个日期的新增患者数量
        List<Map<String, Object>> result = new ArrayList<>();

        for (String date : dateList){
            LocalDate currentDate = LocalDate.parse(date);
            LocalDate nextDate = currentDate.plusDays(1); // 下一天的日期

            Integer count = patientMapper.countNewPatientsByDate(doctorNumber, date, nextDate.toString());
            Map<String, Object> data = new HashMap<>();
            data.put("date", date);
            data.put("count", count);
            result.add(data);
        }
        return result;
    }

    //获取前五天的日期
    private List<String> getLastFiveDays() {
        // 获取过去五天的日期
        List<String> dateList = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (int i = 0; i < 5; i++) {
            dateList.add(today.minusDays(i).toString());  // 格式化为 yyyy-MM-dd
        }

        return dateList;
    }

    @Override
    public Map<String, Integer> getPatientAgeDistribution() {
        Map<String, Integer> ageDistribution = new LinkedHashMap<>();
        
        // 初始化年龄区间
        ageDistribution.put("0-18岁", 0);
        ageDistribution.put("19-35岁", 0);
        ageDistribution.put("36-50岁", 0);
        ageDistribution.put("51-65岁", 0);
        ageDistribution.put("66-80岁", 0);
        ageDistribution.put("80岁以上", 0);
        
        try {
            // 获取所有患者信息
            List<Patient> patients = this.list();
            
            // 统计各年龄区间的患者数量
            for (Patient patient : patients) {
                Integer age = patient.getAge();
                if (age == null) {
                    continue;
                }
                
                if (age <= 18) {
                    ageDistribution.put("0-18岁", ageDistribution.get("0-18岁") + 1);
                } else if (age <= 35) {
                    ageDistribution.put("19-35岁", ageDistribution.get("19-35岁") + 1);
                } else if (age <= 50) {
                    ageDistribution.put("36-50岁", ageDistribution.get("36-50岁") + 1);
                } else if (age <= 65) {
                    ageDistribution.put("51-65岁", ageDistribution.get("51-65岁") + 1);
                } else if (age <= 80) {
                    ageDistribution.put("66-80岁", ageDistribution.get("66-80岁") + 1);
                } else {
                    ageDistribution.put("80岁以上", ageDistribution.get("80岁以上") + 1);
                }
            }

            
        } catch (Exception e) {
            log.error("统计患者年龄分布失败", e);
            // 发生异常时清空Map并添加错误信息
            ageDistribution.clear();
            ageDistribution.put("统计失败", 0);
        }
        
        return ageDistribution;
    }
}