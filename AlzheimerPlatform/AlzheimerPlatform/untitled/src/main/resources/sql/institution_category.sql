create table institution_category
(
    id             int auto_increment comment '种类id'
        primary key,
    category_name  varchar(255)                       not null comment '种类名',
    category_alias varchar(255)                       null comment '种类别名',
    create_user    int                                null comment '创建者（id）',
    create_time    datetime default (now())           null comment '创建时间',
    update_time    datetime default CURRENT_TIMESTAMP null comment '更新时间',
    level          int                                not null comment '机构等级',
    level_name     varchar(255)                       not null comment '机构种类名',
    remark         varchar(255)                       not null comment '详细信息'
)
    comment '医疗机构种类表' collate = utf8mb4_bg_0900_ai_ci;

INSERT INTO health_system.institution_category (category_name, category_alias, create_user, create_time, update_time, level, level_name, remark) VALUES ('三级甲等市立医院', '三甲市立医院', 110110, '2025-03-09 22:09:32', '2025-03-09 22:09:32', 1, '三级甲等医院', '三级医院通常提供综合性医疗服务，拥有较为完善的医疗设施和技术力量，是地区或国家级的重点医院，负责复杂病症的治疗和科研任务。三级甲等医院是等级最高的');
INSERT INTO health_system.institution_category (category_name, category_alias, create_user, create_time, update_time, level, level_name, remark) VALUES ('三级乙等中医院', '三乙中医院', 110110, '2025-03-09 22:09:32', '2025-03-09 22:09:32', 1, '三级甲等医院', '三级医院通常提供综合性医疗服务，拥有较为完善的医疗设施和技术力量，是地区或国家级的重点医院，负责复杂病症的治疗和科研任务。三级甲等医院是等级最高的');
INSERT INTO health_system.institution_category (category_name, category_alias, create_user, create_time, update_time, level, level_name, remark) VALUES ('二级甲等县级市医院', '二甲市级医院', 110110, '2025-03-09 22:09:32', '2025-03-09 22:09:32', 2, '二级医院', '二级医院通常提供较为全面的医疗服务，但其技术和医疗设施较三级医院稍弱，通常以区县为单位，覆盖基层医疗需求');
INSERT INTO health_system.institution_category (category_name, category_alias, create_user, create_time, update_time, level, level_name, remark) VALUES ('二级乙等县级养老院', '县级养老院', 110110, '2025-03-09 22:09:32', '2025-03-09 22:09:32', 2, '二级医院', '二级医院通常提供较为全面的医疗服务，但其技术和医疗设施较三级医院稍弱，通常以区县为单位，覆盖基层医疗需求');
INSERT INTO health_system.institution_category (category_name, category_alias, create_user, create_time, update_time, level, level_name, remark) VALUES ('市护理院', '一级护理医院', 110110, '2025-03-09 22:09:32', '2025-03-09 22:09:32', 3, '一级医院', '一级医院主要提供基本的医疗服务和健康检查，主要面向基层社区，具有较少的医疗设施，通常用于日常的健康管理和初步治疗');
INSERT INTO health_system.institution_category (category_name, category_alias, create_user, create_time, update_time, level, level_name, remark) VALUES ('普通社区医院', '社区医院', 110110, '2025-03-11 13:56:54', '2025-03-11 13:56:54', 4, '社区医院', '除了传统的医院外，还有专门针对老年人的护理院和康复医院。这些机构提供针对老年人的长期护理、康复治疗及护理等服务');
INSERT INTO health_system.institution_category (category_name, category_alias, create_user, create_time, update_time, level, level_name, remark) VALUES ('街道养老办', '社区医院', 110110, '2025-03-11 13:57:42', '2025-03-11 13:57:42', 4, '社区医院', '除了传统的医院外，还有专门针对老年人的护理院和康复医院。这些机构提供针对老年人的长期护理、康复治疗及护理等服务');
