create table institution
(
    uuid                    varchar(36)                        not null comment 'uuid'
        primary key,
    institution_name        varchar(255)                       not null comment '机构名',
    institution_phone       varchar(255)                       not null comment '机构电话',
    address                 varchar(512)                       null comment '详细地址',
    institution_category_id int                                not null comment '机构种类',
    institution_level       int                                not null comment '机构级别',
    status                  tinyint  default 0                 not null comment '状态: 0-禁用, 1-启用, 2-审核中',
    create_time             datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time             datetime default CURRENT_TIMESTAMP not null comment '更新时间',
    列_name                 int                                null,
    constraint institution_pk
        unique (uuid),
    constraint institution_pk_2
        unique (institution_name),
    constraint institution_pk_3
        unique (uuid),
    constraint institution_pk_4
        unique (uuid),
    constraint institution_pk_5
        unique (institution_name)
)
    collate = utf8mb4_general_ci;

create index idx_level
    on institution (institution_level);

create index idx_status_category
    on institution (status, institution_category_id);

INSERT INTO health_system.institution (uuid, institution_name, institution_phone, address, institution_category_id, institution_level, status, create_time, update_time, 列_name) VALUES ('065bb7b0-eeab-476d-a25a-bd1a1287a9dd', '苏州市相城区养老院', '63627291', '苏州市相城区某街道32号', 5, 3, 1, '2025-04-22 20:59:09', '2025-04-22 12:59:09', null);
INSERT INTO health_system.institution (uuid, institution_name, institution_phone, address, institution_category_id, institution_level, status, create_time, update_time, 列_name) VALUES ('08c88faa-24b0-4673-9917-e59cb5d0728a', '苏州市中医院', '(0532)65222220', '沧浪新城杨素路18号,吴中西路889号', 2, 1, 1, '2025-03-15 16:35:07', '2025-03-15 08:35:08', null);
INSERT INTO health_system.institution (uuid, institution_name, institution_phone, address, institution_category_id, institution_level, status, create_time, update_time, 列_name) VALUES ('08c88faa-24b0-4673-9917-e59cb5d0728b', '苏州市立医院（本部）', '51265155348', '中国江苏省苏州市沧浪区道前街26号', 1, 1, 1, '2025-03-15 16:35:07', '2025-03-15 08:35:08', null);
INSERT INTO health_system.institution (uuid, institution_name, institution_phone, address, institution_category_id, institution_level, status, create_time, update_time, 列_name) VALUES ('4d5f65b2-b9a4-4fef-8857-cc49047e9616', '苏州工业园区星海医院', '051262513651', '苏州市苏州工业园区葑春街400号', 4, 2, 1, '2025-04-22 20:51:03', '2025-04-22 12:51:03', null);
INSERT INTO health_system.institution (uuid, institution_name, institution_phone, address, institution_category_id, institution_level, status, create_time, update_time, 列_name) VALUES ('5ccc9f95-e7b6-40fb-8605-f6a6d6272667', '常熟市第五人民医院', '051252867102', '苏州市常熟市珠江路289号', 3, 2, 1, '2025-04-22 20:51:45', '2025-04-22 12:51:46', null);
INSERT INTO health_system.institution (uuid, institution_name, institution_phone, address, institution_category_id, institution_level, status, create_time, update_time, 列_name) VALUES ('60bb185a-6f27-482e-a4c6-cbba68d04566', '苏州东吴中西医结合医院', '67371738', '苏州市桐泾南路81号', 4, 2, 1, '2025-04-22 20:52:33', '2025-04-22 12:52:33', null);
INSERT INTO health_system.institution (uuid, institution_name, institution_phone, address, institution_category_id, institution_level, status, create_time, update_time, 列_name) VALUES ('76571ec7-0de1-43c3-992f-0836c9c86318', '苏州市立医院附属护理院', '629394942', '苏州市姑苏区某街道', 5, 3, 1, '2025-04-22 20:54:40', '2025-04-22 12:54:40', null);
INSERT INTO health_system.institution (uuid, institution_name, institution_phone, address, institution_category_id, institution_level, status, create_time, update_time, 列_name) VALUES ('8128b368-329f-491f-b867-7175b14433dd', '苏州市唯亭社区养老院', '62485832', '苏州市工业园区唯亭街道45号', 8, 4, 1, '2025-04-22 21:00:13', '2025-04-22 13:00:14', null);
INSERT INTO health_system.institution (uuid, institution_name, institution_phone, address, institution_category_id, institution_level, status, create_time, update_time, 列_name) VALUES ('8db4bbc9-13c9-499f-b306-855739a61467', '苏州市湖东社区', '18082347142', '苏州市工业园区湖东某街道', 7, 4, 1, '2025-04-22 20:57:17', '2025-04-22 12:57:17', null);
INSERT INTO health_system.institution (uuid, institution_name, institution_phone, address, institution_category_id, institution_level, status, create_time, update_time, 列_name) VALUES ('aacf64e2-f2c5-481b-86a5-8d81f0d66548', '苏州工业园区护理院', '17392927371', '苏州市工业园区金鸡湖街道', 5, 3, 1, '2025-04-22 20:55:47', '2025-04-22 12:55:47', null);
INSERT INTO health_system.institution (uuid, institution_name, institution_phone, address, institution_category_id, institution_level, status, create_time, update_time, 列_name) VALUES ('dc40e3ad-e60c-4591-bea4-1409ea650d3b', '昆山市第一人民医院', '67502032', '苏州市昆山市某某街道66号', 3, 2, 1, '2025-04-22 21:17:01', '2025-04-22 13:17:01', null);
INSERT INTO health_system.institution (uuid, institution_name, institution_phone, address, institution_category_id, institution_level, status, create_time, update_time, 列_name) VALUES ('f6072565-876a-4305-b163-edc7d15f2392', '苏州市第五人民医院', '87806050', '苏州市相城区广前路10号', 1, 1, 1, '2025-03-17 17:37:27', '2025-03-17 10:04:04', null);
