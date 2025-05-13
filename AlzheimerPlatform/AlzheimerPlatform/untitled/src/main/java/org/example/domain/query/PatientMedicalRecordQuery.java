package org.example.domain.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(description = "病历查询对象")
public class PatientMedicalRecordQuery extends PageQuery {

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
}