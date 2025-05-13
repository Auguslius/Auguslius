package org.example.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Schema
@Data
public class UserDTO {
    @Schema
    private Integer id; // 主键
    @Schema
    private String username; // 用户名
    @Schema
    private Integer age;//年龄
    @Schema
    private Integer number;//编号
    @Schema
    @NotEmpty(message = "昵称不能为空")
    @Pattern(regexp = "^\\S{1,10}$")
    private String nickname; // 昵称
    @Schema
    private String address; // 地址
    @Schema
    private String room;//科室
    @Schema
    private String phone;//电话
    @Schema
    private String position; // 职位
    @Schema
    private String institution;
    @Schema
    @NotEmpty(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email; // 邮箱
    @Schema
    private String status;//状态
    @Schema
    private Integer isAuthenticated;

}
