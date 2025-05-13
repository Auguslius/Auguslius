package org.example.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;


@Data
@Schema(description = "用户实体")
@TableName("user")
public class User {

    @NotNull(message = "ID 不能为空")
    private Integer id; // 主键
    @Schema
    private String username; // 用户名
    @Schema
    private String password; // 密码
    @Schema
    private Integer age;
    @Schema
    private Integer number;//编号
    @Schema
    @NotEmpty(message = "昵称不能为空")
    @Pattern(regexp = "^\\S{1,10}$")
    private String nickname; // 昵称
    @Schema
    private String address; // 地址
    @Schema
    private String room;
    @Schema
    private String phone;
    @Schema
    private String position; // 职位
    @Schema
    private String institution;
    @Schema
    @NotEmpty(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email; // 邮箱
    @Schema
    private String userPic; // 用户头像地址
    @Schema
    private String status;//状态
    @Schema
    private Integer Role;
    @Schema
    private Integer isAuthenticated;
    @Schema
    @PastOrPresent(message = "创建时间不能是未来时间")
    private java.util.Date createTime; // 创建时间
    @Schema
    @PastOrPresent(message = "更新时间不能是未来时间")
    private java.util.Date updateTime; // 更新时间
}