package org.example.web.rest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.domain.entity.Patient;
import org.example.domain.dto.PageDTO;
import org.example.domain.query.PatientQuery;
import org.example.domain.vo.PatientCountVO;
import org.example.domain.vo.PatientVO;

import java.util.List;
import java.util.Map;

public interface PatientService extends IService<Patient> {
    Boolean updatePatient(Patient patient);

    Boolean addPatient(Patient patient);

    Boolean removePatientByUuid(String uuid);

    PatientVO getPatientById(String uuid);

    PageDTO<PatientVO> queryPatientPage(PatientQuery patientQuery);

    PatientCountVO QueryPatientCount();

    List<Map<String, Object>> countNewPatientsInLastFiveDays();

    Map<String, Integer> getPatientAgeDistribution();
}
