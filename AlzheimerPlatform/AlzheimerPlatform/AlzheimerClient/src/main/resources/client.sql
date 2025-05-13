-- 创建数据库表
CREATE TABLE `t_client` (
                            `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                            `uuid` varchar(32) NOT NULL COMMENT 'UUID',
                            `username` varchar(50) NOT NULL COMMENT '用户名',
                            `password` varchar(100) NOT NULL COMMENT '密码',
                            `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
                            `encrypt_phone` varchar(100) DEFAULT NULL COMMENT '加密后的手机号',
                            `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
                            `real_name` varchar(50) DEFAULT NULL COMMENT '姓名',
                            `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态：0-未审核，1-正常，2-禁用，3-审核失败',
                            `login_count` int DEFAULT '0' COMMENT '登录次数',
                            `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
                            `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            `remark` varchar(255) DEFAULT NULL COMMENT '备注',
                            `del_flag` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除标志：0-未删除，1-已删除',
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `uk_uuid` (`uuid`),
                            UNIQUE KEY `uk_username` (`username`),
                            KEY `idx_phone` (`phone`),
                            KEY `idx_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='客户信息表';

-- 创建触发器，在插入时自动生成UUID
DELIMITER //
CREATE TRIGGER before_insert_client
    BEFORE INSERT ON t_client
    FOR EACH ROW
BEGIN
    IF NEW.uuid IS NULL THEN
        SET NEW.uuid = UUID();
    END IF;
END //
DELIMITER ;;