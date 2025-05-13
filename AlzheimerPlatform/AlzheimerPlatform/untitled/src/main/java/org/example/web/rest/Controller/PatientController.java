package org.example.web.rest.Controller;

import cn.hutool.core.bean.BeanUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.example.common.enums.CommonEnum;
import org.example.common.enums.PatientEnum;
import org.example.common.result.Result;
import org.example.domain.dto.PageDTO;
import org.example.domain.dto.PatientDTO;
import org.example.domain.entity.Patient;
import org.example.domain.dto.PatientUpdateDTO;
import org.example.domain.query.PatientQuery;
import org.example.domain.vo.PatientCountVO;
import org.example.domain.vo.PatientVO;
import org.example.web.rest.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping
    public Result addPatient(@Valid @RequestBody PatientDTO patientDTO) {
        Patient patient = BeanUtil.copyProperties(patientDTO, Patient.class);
        Boolean isSaved =patientService.addPatient(patient);
        return Result.success(CommonEnum.SUCCESS.getCode(), PatientEnum.ADD_SUCCESS.getMessage(), isSaved);
    }

    @PutMapping
    public Result updatePatient(@Valid @RequestBody PatientUpdateDTO patientUpdateDTO) {
        Patient patient = BeanUtil.copyProperties(patientUpdateDTO, Patient.class);
        Boolean isUpdated = patientService.updatePatient(patient);
        return Result.success(CommonEnum.SUCCESS.getCode(), PatientEnum.UPDATE_SUCCESS.getMessage(), isUpdated);
    }

    @DeleteMapping("/{uuid}")
    public Result deletePatient(@PathVariable String uuid) {
        Boolean isDeleted = patientService.removePatientByUuid(uuid);
        return Result.success(CommonEnum.SUCCESS.getCode(), PatientEnum.DELETE_SUCCESS.getMessage(), isDeleted);
    }

    @GetMapping("/{uuid}")
    public Result<PatientVO> getPatientById(@PathVariable String uuid) {
        PatientVO result = patientService.getPatientById(uuid);
        return Result.success(CommonEnum.SUCCESS.getCode(), PatientEnum.QUERY_SUCCESS.getMessage(), result);
    }

    @GetMapping("/list")
    public List<Patient> getAllPatients() {
        return patientService.list();
    }

    @Operation(summary ="患者分页条件查询接口")
    @GetMapping("/page")
    public Result<PageDTO<PatientVO>> queryPatientPage(PatientQuery patientQuery){
        PageDTO<PatientVO> page= patientService.queryPatientPage(patientQuery);
        return Result.success(CommonEnum.SUCCESS.getCode(), PatientEnum.QUERY_SUCCESS.getMessage(), page);
    }

    @Operation(summary ="查询患者男女数据")
    @GetMapping("/PatientCount")
    public Result<PatientCountVO> querySexCategory(){
        PatientCountVO patientCountVO = patientService.QueryPatientCount();
        return Result.success(CommonEnum.SUCCESS.getCode(), PatientEnum.QUERY_SUCCESS.getMessage(), patientCountVO);
    }

    @Operation(summary = "查询当前用户近五日新增患者")
    @GetMapping("/countNewPatients")
    public Result<List<Map<String, Object>>> countNewPatientsInLastFiveDays() {
        List<Map<String, Object>> countList = patientService.countNewPatientsInLastFiveDays();
        return Result.success(CommonEnum.SUCCESS.getCode(), "查询成功", countList);
    }

    @Operation(summary = "查询患者年龄分布")
    @GetMapping("/ageDistribution")
    public Result<Map<String, Integer>> getPatientAgeDistribution() {
        Map<String, Integer> ageDistribution = patientService.getPatientAgeDistribution();
        return Result.success(CommonEnum.SUCCESS.getCode(), PatientEnum.QUERY_SUCCESS.getMessage(), ageDistribution);
    }

}
