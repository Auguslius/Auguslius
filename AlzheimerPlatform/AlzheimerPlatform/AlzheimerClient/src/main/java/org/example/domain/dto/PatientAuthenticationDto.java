package org.example.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 患者认证DTO类
 *
 * @author lyx
 */
@Data
public class PatientAuthenticationDto {

    /**
     * 唯一标识
     */
    private String uuid;

    /**
     * 真实姓名
     */
    private String name;

    /**
     * 联系方式
     */
    private String phone;

    /**
     * 邮件
     */
    private String email;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 生日
     */
    private String birthDate;


    /**
     * 性别
     */
    private String gender;

    /**
     * 住址
     */
    private String address;

    /**
     * 既往病史
     */
    private String remark;

}