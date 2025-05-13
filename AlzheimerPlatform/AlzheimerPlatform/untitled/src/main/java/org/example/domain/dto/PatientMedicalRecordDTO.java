package org.example.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(description = "病人病历实体")
public class PatientMedicalRecordDTO {

    @Schema(description = "患者唯一标识")
    private String patientUuid;

    @Schema(description = "医生编号")
    private Integer doctorNumber;

    @Schema(description = "诊断信息")
    private String diagnosis;

    @Schema(description = "治疗信息")
    private String treatment;

    @Schema(description = "实际诊断图片")
    private String diagnosisPic;


}