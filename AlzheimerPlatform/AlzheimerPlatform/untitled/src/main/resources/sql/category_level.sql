create table category_level
(
    level      int          not null comment '对应level字段',
    level_name varchar(255) not null comment '对应level_name',
    constraint category_level_pk
        unique (level_name),
    constraint category_level_pk_2
        unique (level)
)
    comment '种类级别表' collate = utf8mb4_bg_0900_ai_ci;

INSERT INTO health_system.category_level (level, level_name) VALUES (1, '三级医院');
INSERT INTO health_system.category_level (level, level_name) VALUES (2, '二级医院');
INSERT INTO health_system.category_level (level, level_name) VALUES (3, '一级医院');
INSERT INTO health_system.category_level (level, level_name) VALUES (4, '社区医院');
