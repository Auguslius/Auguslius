package org.example.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 客户端用户实体类
 * 
 * @author admin
 */
@Data
@TableName("t_client")
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * UUID
     */
    private String uuid;
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 密码
     */
    private String password;
    
    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 姓名
     */
    private String realName;
    
    /**
     * 状态：0-未审核，1-正常，2-禁用，3-审核失败
     */
    private Integer status;
    
    /**
     * 登录次数
     */
    private Integer loginCount;
    
    /**
     * 最后登录时间
     */
    private Date lastLoginTime;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 更新时间
     */
    private Date updateTime;


} 