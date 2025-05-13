create table password_reset_requests
(
    request_id     int auto_increment comment '请求ID'
        primary key,
    number         int                                  not null comment '用户编号',
    email          varchar(100)                         null comment '邮箱',
    phone          varchar(20)                          null comment '手机号',
    token          varchar(255)                         not null comment '重置密码令牌',
    requested_time datetime   default CURRENT_TIMESTAMP null comment '请求时间',
    used           tinyint(1) default 0                 null comment '是否已使用',
    create_by      varchar(50)                          null comment '创建人',
    create_time    datetime   default CURRENT_TIMESTAMP null comment '创建时间',
    update_by      varchar(50)                          null comment '修改人',
    update_time    datetime                             null on update CURRENT_TIMESTAMP comment '修改时间'
)
    comment '密码重置请求表';

INSERT INTO health_system.password_reset_requests (number, email, phone, token, requested_time, used, create_by, create_time, update_by, update_time) VALUES (16, 'wacc1232141@qq.com', null, '3dcf1c39-d367-4ddf-ba7b-82b134e66192', '2025-03-13 12:54:22', 0, 'lyxlyx2', '2025-03-13 20:54:22', null, null);
INSERT INTO health_system.password_reset_requests (number, email, phone, token, requested_time, used, create_by, create_time, update_by, update_time) VALUES (16, null, '18306130532', 'f2c4ce65-724c-4792-9868-df3819688340', '2025-03-13 12:59:37', 0, 'lyxlyx2', '2025-03-13 20:59:37', null, null);
INSERT INTO health_system.password_reset_requests (number, email, phone, token, requested_time, used, create_by, create_time, update_by, update_time) VALUES (110110, null, '18306130532', '30b3998a-ebe4-4666-8e7e-e7c25ff65232', '2025-03-13 13:34:34', 1, 'lyxlyx2', '2025-03-13 21:34:33', null, '2025-03-13 13:34:49');
INSERT INTO health_system.password_reset_requests (number, email, phone, token, requested_time, used, create_by, create_time, update_by, update_time) VALUES (110110, null, '18306130532', '4f42e284-9447-4c1d-8cda-0986e953fefb', '2025-03-13 14:13:14', 0, 'lyxlyx2', '2025-03-13 22:13:13', null, null);
INSERT INTO health_system.password_reset_requests (number, email, phone, token, requested_time, used, create_by, create_time, update_by, update_time) VALUES (110110, null, '18306130532', 'ca0f24eb-f036-4825-8f94-aa50a3e6e91e', '2025-03-13 14:14:41', 0, 'lyxlyx2', '2025-03-13 22:14:41', null, null);
INSERT INTO health_system.password_reset_requests (number, email, phone, token, requested_time, used, create_by, create_time, update_by, update_time) VALUES (110110, null, '18306130532', '3ee69d4c-ab81-4e63-b73e-219b52c758dc', '2025-03-13 14:16:52', 0, 'lyxlyx2', '2025-03-13 22:16:51', null, null);
INSERT INTO health_system.password_reset_requests (number, email, phone, token, requested_time, used, create_by, create_time, update_by, update_time) VALUES (110110, null, '18306130532', 'cdfa3734-f1f0-4bc8-973e-5fb264f0125c', '2025-03-13 14:29:36', 0, 'lyxlyx2', '2025-03-13 22:29:35', null, null);
INSERT INTO health_system.password_reset_requests (number, email, phone, token, requested_time, used, create_by, create_time, update_by, update_time) VALUES (110110, null, '18306130532', 'bb0d2d93-1fe0-4fbd-94e7-0b27418e8c69', '2025-03-13 14:33:42', 0, 'lyxlyx2', '2025-03-13 22:33:42', null, null);
INSERT INTO health_system.password_reset_requests (number, email, phone, token, requested_time, used, create_by, create_time, update_by, update_time) VALUES (110110, null, '18306130532', '1fd91cc4-86c4-4dfb-99a2-d17784a353f6', '2025-03-13 14:34:06', 0, 'lyxlyx2', '2025-03-13 22:34:06', null, null);
INSERT INTO health_system.password_reset_requests (number, email, phone, token, requested_time, used, create_by, create_time, update_by, update_time) VALUES (110110, null, '18306130532', '1e90eb70-43b6-42ff-a841-3e1792f062cc', '2025-03-13 14:40:21', 0, 'lyxlyx2', '2025-03-13 22:40:21', null, null);
INSERT INTO health_system.password_reset_requests (number, email, phone, token, requested_time, used, create_by, create_time, update_by, update_time) VALUES (110110, null, '18306130532', 'd2d6727e-3e29-4f2f-839f-09f1d5152090', '2025-03-13 14:41:46', 0, 'lyxlyx2', '2025-03-13 22:41:46', null, null);
INSERT INTO health_system.password_reset_requests (number, email, phone, token, requested_time, used, create_by, create_time, update_by, update_time) VALUES (110110, null, '18306130532', '08dea2a2-3c14-4d33-ac90-77eb072b8a6a', '2025-03-13 14:44:15', 0, 'lyxlyx2', '2025-03-13 22:44:15', null, null);
INSERT INTO health_system.password_reset_requests (number, email, phone, token, requested_time, used, create_by, create_time, update_by, update_time) VALUES (110110, null, '18306130532', 'ee413de5-89b2-4c2b-9ce2-675d148fa86e', '2025-03-13 14:52:35', 1, 'lyxlyx2', '2025-03-13 22:52:34', null, '2025-03-13 14:52:43');
INSERT INTO health_system.password_reset_requests (number, email, phone, token, requested_time, used, create_by, create_time, update_by, update_time) VALUES (110110, null, '18306130532', 'f12b0814-1cff-4817-9d00-f77f7b07eca2', '2025-03-13 14:53:33', 1, 'lyxlyx2', '2025-03-13 22:53:33', null, '2025-03-13 14:53:46');
INSERT INTO health_system.password_reset_requests (number, email, phone, token, requested_time, used, create_by, create_time, update_by, update_time) VALUES (110110, null, '18306130532', '84c71fbf-9d8f-4ec3-adda-2701c3f309c3', '2025-04-22 14:22:53', 0, 'lyxlyx2', '2025-04-22 22:22:53', null, null);
