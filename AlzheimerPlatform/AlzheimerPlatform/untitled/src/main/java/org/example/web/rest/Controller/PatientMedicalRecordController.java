package org.example.web.rest.Controller;

import cn.hutool.core.bean.BeanUtil;
import io.swagger.v3.oas.annotations.Operation;
import org.example.common.enums.CommonEnum;
import org.example.common.enums.MedicalRecordEnum;
import org.example.domain.entity.PatientMedicalRecord;
import org.example.domain.dto.PageDTO;
import org.example.domain.dto.PatientMedicalRecordDTO;
import org.example.domain.query.PatientMedicalRecordQuery;
import org.example.domain.vo.PatientMedicalRecordVO;
import org.example.common.result.Result;
import org.example.web.rest.service.PatientMedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medical-records")
public class PatientMedicalRecordController {

    @Autowired
    private PatientMedicalRecordService medicalRecordService;

    @Operation(summary = "新增病历记录")
    @PostMapping
    public Result addMedicalRecord(@Validated @RequestBody PatientMedicalRecordDTO recordDTO) {
        PatientMedicalRecord record = BeanUtil.copyProperties(recordDTO, PatientMedicalRecord.class);
        Boolean isSaved = medicalRecordService.addMedicalRecord(record);
        return Result.success(CommonEnum.SUCCESS.getCode(), MedicalRecordEnum.ADD_SUCCESS.getMessage(), isSaved);
    }

    @Operation(summary = "更新病历记录")
    @PutMapping("/{recordUuid}")
    public Result updateMedicalRecord(@PathVariable String recordUuid, @Validated @RequestBody PatientMedicalRecordDTO recordDTO) {
        PatientMedicalRecord record = BeanUtil.copyProperties(recordDTO, PatientMedicalRecord.class);
        record.setRecordUuid(recordUuid);
        Boolean isUpdated = medicalRecordService.updateMedicalRecord(record);
        return Result.success(CommonEnum.SUCCESS.getCode(), MedicalRecordEnum.UPDATE_SUCCESS.getMessage(), isUpdated);
    }

    @Operation(summary = "删除病历记录")
    @DeleteMapping("/{recordUuid}")
    public Result deleteMedicalRecord(@PathVariable String recordUuid) {
        Boolean isDeleted = medicalRecordService.removeMedicalRecordByUuid(recordUuid);
        return Result.success(CommonEnum.SUCCESS.getCode(), MedicalRecordEnum.DELETE_SUCCESS.getMessage(), isDeleted);
    }

    @Operation(summary = "病历分页条件查询")
    @GetMapping("/page")
    public Result<PageDTO<PatientMedicalRecordVO>> queryMedicalRecordPage(PatientMedicalRecordQuery query) {
        PageDTO<PatientMedicalRecordVO> page = medicalRecordService.queryMedicalRecordPage(query);
        return Result.success(CommonEnum.SUCCESS.getCode(), MedicalRecordEnum.QUERY_SUCCESS.getMessage(), page);
    }
}