package org.example.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "绑定医生")
public class BindDoctorDto {

    @Schema(description = "医生工号")
    private Integer doctorNumber;

    @Schema(description = "用户uuid")
    private String uuid;
}
