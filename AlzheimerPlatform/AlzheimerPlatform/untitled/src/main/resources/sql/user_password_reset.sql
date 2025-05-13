create table user_password_reset
(
    id               int auto_increment comment '密码重置ID'
        primary key,
    number           int                                 not null comment '用户编号',
    reset_token      varchar(255)                        not null comment '密码重置令牌',
    token_expiration timestamp                           not null comment '令牌过期时间',
    reset_method     enum ('email', 'phone')             null comment '重置方式',
    create_by        varchar(50)                         null comment '创建人',
    create_time      timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_by        varchar(50)                         null comment '修改人',
    update_time      timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '修改时间'
)
    comment '用户密码重置信息表';

