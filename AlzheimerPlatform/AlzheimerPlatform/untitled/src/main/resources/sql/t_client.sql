create table t_client
(
    id              bigint auto_increment comment '主键ID'
        primary key,
    uuid            varchar(36)                        not null comment 'UUID',
    username        varchar(50)                        not null comment '用户名',
    password        varchar(100)                       not null comment '密码',
    phone           varchar(20)                        null comment '手机号',
    email           varchar(100)                       null comment '邮箱',
    real_name       varchar(50)                        null comment '姓名',
    status          tinyint  default 0                 not null comment '状态：0-未审核，1-正常，2-禁用，3-审核失败',
    login_count     int      default 0                 null comment '登录次数',
    last_login_time datetime                           null comment '最后登录时间',
    create_time     datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint uk_username
        unique (username),
    constraint uk_uuid
        unique (uuid)
)
    comment '客户信息表' collate = utf8mb4_general_ci;

create index idx_email
    on t_client (email);

create index idx_phone
    on t_client (phone);

INSERT INTO health_system.t_client (uuid, username, password, phone, email, real_name, status, login_count, last_login_time, create_time, update_time) VALUES ('2373e4ee96584d809caa5112b79e3d22', 'testuser', '123456', '13800138000', '1909221500@qq.com', 'liuyixuan', 1, 35, '2025-05-09 17:58:30', '2025-03-27 13:06:45', '2025-04-01 15:46:14');
INSERT INTO health_system.t_client (uuid, username, password, phone, email, real_name, status, login_count, last_login_time, create_time, update_time) VALUES ('1103986f176b48c29ee57fb1822fb4eb', 'liuyixuan', '123456', '18306130532', '1909221500@qq.com', 'liuyixuan', 1, 0, null, '2025-03-31 18:08:02', '2025-03-31 18:08:02');
INSERT INTO health_system.t_client (uuid, username, password, phone, email, real_name, status, login_count, last_login_time, create_time, update_time) VALUES ('9ca9e51a3c9b484581bfba5b8b5e83b0', 'test', '123456', '18101010101', '1909112344@qq.com', 'liuyixu1', 1, 0, null, '2025-03-31 18:39:36', '2025-03-31 18:39:36');
INSERT INTO health_system.t_client (uuid, username, password, phone, email, real_name, status, login_count, last_login_time, create_time, update_time) VALUES ('a9ca0dd6b019473d9e3daf827eca13fe', 'FrankLiu', '123456', '15362626262', '1909221533@qq.com', '刘刘刘', 1, 8, '2025-04-11 16:10:55', '2025-04-08 17:32:24', '2025-04-08 17:34:24');
INSERT INTO health_system.t_client (uuid, username, password, phone, email, real_name, status, login_count, last_login_time, create_time, update_time) VALUES ('37e9e205-d314-4bc0-a399-9c9a24f756d8', 'lyx411', '123456', '18306130222', '1909221600@qq.com', '刘奕', 1, 37, '2025-05-12 16:38:11', '2025-04-11 16:37:35', '2025-04-11 16:38:45');
INSERT INTO health_system.t_client (uuid, username, password, phone, email, real_name, status, login_count, last_login_time, create_time, update_time) VALUES ('b84131d7-092c-4520-a268-d705750abbb7', 'lyx0127', '123456', null, null, null, 1, 1, '2025-04-22 22:59:18', '2025-04-22 22:59:02', '2025-04-22 22:59:03');
INSERT INTO health_system.t_client (uuid, username, password, phone, email, real_name, status, login_count, last_login_time, create_time, update_time) VALUES ('6ce7a03b-baf4-4b0d-82ba-50712c359d84', 'abctest', '123456', '13913737282', '1830292833@qq.com', '张三', 1, 1, '2025-05-09 15:04:24', '2025-05-09 15:03:59', '2025-05-09 15:05:46');
