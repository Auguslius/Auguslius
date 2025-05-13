package org.example.rest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.domain.entity.User;
import org.example.domain.vo.UserVO;

import java.util.List;

public interface DoctorService extends IService<User> {
    List<User> getDoctorByInstitutionName(String institution);

    UserVO getDoctorMsg(int doctorNumber);
}
