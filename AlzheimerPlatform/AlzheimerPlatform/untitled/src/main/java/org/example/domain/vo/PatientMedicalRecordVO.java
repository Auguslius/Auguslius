package org.example.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "病人病历实体")
public class PatientMedicalRecordVO {

    @Schema(description = "病历唯一标识")
    private String recordUuid;

    @Schema(description = "患者唯一标识")
    private String patientUuid;

    @Schema(description = "患者名")
    private String patientName;

    @Schema(description = "医生编号")
    private Integer doctorNumber;

    @Schema(description = "医生名")
    private String doctorName;

    @Schema(description = "诊断信息")
    private String diagnosis;

    @Schema(description = "治疗信息")
    private String treatment;

    @Schema(description = "实际诊断图片")
    private String diagnosisPic;

    @Schema(description = "机构名")
    private String institution;

    @Schema(description = "机构唯一标识")
    private String institutionUuid;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}