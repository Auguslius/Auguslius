package org.example.domain.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "病人更新dto")
@TableName("patient")
public class PatientUpdateDTO {

    @Schema(description = "uuid")
    private String uuid;

    @NotNull(message = "姓名不能为空")
    @Size(min = 1, max = 50, message = "姓名长度应在1到50个字符之间")
    @Schema(description = "病人姓名")
    private String name;

    @NotNull(message = "性别不能为空")
    @Schema(description = "性别 (1: 男, 2: 女)")
    private Integer gender;

    @NotNull(message = "出生日期不能为空")
    @Schema(description = "出生日期")
    private String birthDate;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Schema(description = "联系电话")
    private String phone;

    @Schema(description = "住址")
    private String address;

    @Schema(description = "医生编号")
    private Integer doctorNumber;

    @Schema(description = "备注")
    private String remark;
}
