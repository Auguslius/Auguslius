package org.example.rest.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.common.Enum.InstitutionDoctorEnum;
import org.example.exception.BusinessException;
import org.example.domain.entity.User;
import org.example.domain.vo.UserVO;
import org.example.rest.service.DoctorService;
import org.example.web.rest.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorServiceImpl extends ServiceImpl<UserMapper, User> implements DoctorService {

    @Autowired
    private UserMapper userMapper;

    private static final String Institution = "institution";
    @Override
    public List<User> getDoctorByInstitutionName(String institution) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Institution,institution);
        return userMapper.selectList(queryWrapper);
    }
    @Override
    public UserVO getDoctorMsg(int doctorNumber) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNumber, doctorNumber);
        // 查询数据库
        User doctor = userMapper.selectOne(queryWrapper);
        if (doctor == null) {
            throw new BusinessException(
                    InstitutionDoctorEnum.DOCTOR_NOT_EXIST.getCode(),
                    InstitutionDoctorEnum.DOCTOR_NOT_EXIST.getMessage());
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(doctor, userVO);
        return userVO;
    }



}
