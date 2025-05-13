package org.example.rest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.domain.dto.BindDoctorDto;
import org.example.domain.entity.Client;

public interface ClientService extends IService<Client> {
    boolean bindDoctorByDoctorNumber(BindDoctorDto bindDoctorDto);

    boolean unbindDoctorByDoctorNumber(BindDoctorDto bindDoctorDto);
}
