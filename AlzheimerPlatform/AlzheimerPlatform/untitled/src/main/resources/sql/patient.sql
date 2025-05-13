create table patient
(
    uuid          varchar(36)                        not null comment 'uuid'
        primary key,
    name          varchar(50)                        not null comment '病人姓名',
    id_card       char(18)                           not null comment '身份证号码',
    gender        tinyint                            not null comment '性别 (1: 男, 2: 女)',
    age           int                                null comment '年龄',
    birth_date    varchar(36)                        not null comment '出生日期yyyy-MM-dd',
    phone         varchar(11)                        null comment '联系电话',
    address       varchar(255)                       null comment '住址',
    create_time   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP null comment '更新时间',
    remark        text                               null comment '备注',
    is_dead       tinyint  default 0                 not null comment '是否死亡(0或者，1死亡）',
    doctor_number int      default 887375            not null comment '负责医生'
)
    comment '病人表';

create index id_card
    on patient (id_card);

create index idx_birth_date
    on patient (birth_date);

create index idx_gender
    on patient (gender);

create index idx_name
    on patient (name);

create index idx_phone
    on patient (phone);

INSERT INTO health_system.patient (uuid, name, id_card, gender, age, birth_date, phone, address, create_time, update_time, remark, is_dead, doctor_number) VALUES ('07d9785a-d47e-43d0-9179-70da5e0a5f52', '李静', '140101199202223456', 2, 33, '1992-02-22', '13876543210', '江苏省南京市', '2025-04-07 09:05:11', '2025-04-07 09:08:25', '无', 0, 110110);
INSERT INTO health_system.patient (uuid, name, id_card, gender, age, birth_date, phone, address, create_time, update_time, remark, is_dead, doctor_number) VALUES ('0c739cb6-bef3-4b76-b8d6-9abf3c5bfe91', '赵磊', '150101198611112345', 1, 68, '1986-11-11', '13998765432', '浙江省杭州市', '2025-04-06 16:33:25', '2025-04-06 16:35:40', '无', 0, 110110);
INSERT INTO health_system.patient (uuid, name, id_card, gender, age, birth_date, phone, address, create_time, update_time, remark, is_dead, doctor_number) VALUES ('11be38b2-35c1-47f1-8e43-b440e918195a', '杨洋', '170101199608202345', 2, 78, '1996-08-20', '13934567890', '四川省成都市', '2025-04-04 10:56:13', '2025-04-04 10:58:30', '无', 0, 110110);
INSERT INTO health_system.patient (uuid, name, id_card, gender, age, birth_date, phone, address, create_time, update_time, remark, is_dead, doctor_number) VALUES ('3149d294-972e-4667-94a5-c93ba5c1fdf1', '王未', '320501200303125288', 2, 55, '1970-02-03', '17302983234', '苏州虎丘区', '2025-04-22 14:08:22', '2025-04-22 14:08:22', '有冠心病患病史', 0, 110110);
INSERT INTO health_system.patient (uuid, name, id_card, gender, age, birth_date, phone, address, create_time, update_time, remark, is_dead, doctor_number) VALUES ('37e9e205-d314-4bc0-a399-9c9a24f756d8', '刘奕', '320501200301271212', 1, 65, '1960-01-05', '18306130222', '苏州市工业园区琼姬路', '2025-04-24 16:38:45', '2025-04-11 16:38:45', '有心脑血管病史', 0, 10001);
INSERT INTO health_system.patient (uuid, name, id_card, gender, age, birth_date, phone, address, create_time, update_time, remark, is_dead, doctor_number) VALUES ('4f09b877-5ac2-40fa-a65e-47f7d14fd38c', '郑凯', '180101199512312345', 1, 76, '1995-12-31', '13865478901', '陕西省西安市', '2025-04-03 14:10:52', '2025-04-03 14:12:40', '无', 0, 110110);
INSERT INTO health_system.patient (uuid, name, id_card, gender, age, birth_date, phone, address, create_time, update_time, remark, is_dead, doctor_number) VALUES ('5203c6ba-5c9d-49a4-841f-1f2323cf7d43', '孙波', '160101199303303456', 1, 55, '1993-03-30', '13854321678', '北京市海淀区', '2025-04-05 11:12:19', '2025-04-05 11:15:02', '无', 0, 110110);
INSERT INTO health_system.patient (uuid, name, id_card, gender, age, birth_date, phone, address, create_time, update_time, remark, is_dead, doctor_number) VALUES ('5ac3410f-6212-40b0-94a7-b3150ea2ad0c', '张涛', '130101197901011234', 1, 46, '1979-01-01', '13765432109', '广东省广州市', '2025-04-21 14:20:44', '2025-04-08 14:25:55', '无', 0, 110110);
INSERT INTO health_system.patient (uuid, name, id_card, gender, age, birth_date, phone, address, create_time, update_time, remark, is_dead, doctor_number) VALUES ('6ce7a03b-baf4-4b0d-82ba-50712c359d84', '张三', '320501200301263633', 1, 52, '1973-01-01', '13913737282', '南京玄武区某街道', '2025-05-09 15:05:46', '2025-05-09 15:05:46', '有冠心病病史', 0, 887375);
INSERT INTO health_system.patient (uuid, name, id_card, gender, age, birth_date, phone, address, create_time, update_time, remark, is_dead, doctor_number) VALUES ('793b5f99-d209-438f-84b5-b2d5f8ed2f93', '黄娜', '190101199801012345', 2, 65, '1998-01-01', '13987651234', '福建省厦门市', '2025-04-02 09:22:44', '2025-04-02 09:25:11', '无', 0, 110110);
INSERT INTO health_system.patient (uuid, name, id_card, gender, age, birth_date, phone, address, create_time, update_time, remark, is_dead, doctor_number) VALUES ('994c93e9-fd45-4a38-9ccb-f9a06f197392', '奕轩', '110101199001011234', 1, 35, '1990-01-01', '13812345678', '北京市朝阳区', '2025-04-23 16:47:52', '2025-04-08 16:50:41', '无', 0, 110110);
INSERT INTO health_system.patient (uuid, name, id_card, gender, age, birth_date, phone, address, create_time, update_time, remark, is_dead, doctor_number) VALUES ('994c93e9-fd45-4a38-9ccb-f9a06f19739d', '刘奕轩', '320501200301275257', 1, 17, '2008-04-02', '18306130531', '南京市玄武区', '2025-04-08 06:15:01', '2025-04-08 06:15:01', '1111', 0, 111111);
INSERT INTO health_system.patient (uuid, name, id_card, gender, age, birth_date, phone, address, create_time, update_time, remark, is_dead, doctor_number) VALUES ('9aaafa31-298e-4b1b-9297-db876cd47cdb', '张立', '340301200102034343', 1, 38, '1987-02-03', '18024349248', '苏州市', '2025-04-25 06:36:53', '2025-04-25 06:36:53', '无既往病史
', 0, 110110);
INSERT INTO health_system.patient (uuid, name, id_card, gender, age, birth_date, phone, address, create_time, update_time, remark, is_dead, doctor_number) VALUES ('a41d4138-0424-4355-bc7e-31e972cf3068', '孙志', '529392200102025252', 1, 54, '1971-04-22', '17473739234', '苏州市工业园区', '2025-04-22 14:09:11', '2025-04-22 14:09:11', '124', 0, 110110);
INSERT INTO health_system.patient (uuid, name, id_card, gender, age, birth_date, phone, address, create_time, update_time, remark, is_dead, doctor_number) VALUES ('a9ca0dd6b019473d9e3daf827eca13fe', '刘刘刘', '320501200301275257', 1, 67, '2000-02-01', '15362626262', '苏州市姑苏区', '2025-04-21 17:34:24', '2025-04-08 17:34:24', '无', 0, 111111);
INSERT INTO health_system.patient (uuid, name, id_card, gender, age, birth_date, phone, address, create_time, update_time, remark, is_dead, doctor_number) VALUES ('ab9543d4-049b-11f0-b6db-7c8ae187fc8b', '李四', '110101199202020022', 2, 64, '1992-02-02', '13800138002', '上海市浦东新区', '2025-04-07 16:25:09', '2025-03-19 16:25:09', '无', 0, 110110);
INSERT INTO health_system.patient (uuid, name, id_card, gender, age, birth_date, phone, address, create_time, update_time, remark, is_dead, doctor_number) VALUES ('ab955609-049b-11f0-b6db-7c8ae187fc8b', '王五', '110101199303030033', 1, 66, '1993-03-03', '13800138003', '广州市天河区', '2025-04-06 16:25:09', '2025-03-19 16:25:09', '无', 0, 110110);
INSERT INTO health_system.patient (uuid, name, id_card, gender, age, birth_date, phone, address, create_time, update_time, remark, is_dead, doctor_number) VALUES ('ab95591c-049b-11f0-b6db-7c8ae187fc8b', '赵六', '110101199404040044', 2, 58, '1994-04-04', '13800138004', '深圳市南山区', '2025-03-19 16:25:09', '2025-03-19 16:25:09', '无', 0, 887375);
INSERT INTO health_system.patient (uuid, name, id_card, gender, age, birth_date, phone, address, create_time, update_time, remark, is_dead, doctor_number) VALUES ('ab955ab6-049b-11f0-b6db-7c8ae187fc8b', '孙七', '110101199505050055', 1, 66, '1995-05-05', '13800138005', '成都市武侯区', '2025-04-21 16:25:09', '2025-03-19 16:25:09', '无', 0, 110110);
INSERT INTO health_system.patient (uuid, name, id_card, gender, age, birth_date, phone, address, create_time, update_time, remark, is_dead, doctor_number) VALUES ('ab955c16-049b-11f0-b6db-7c8ae187fc8b', '周八', '110101199606060066', 2, 66, '1996-06-06', '13800138006', '杭州市西湖区', '2025-05-09 16:25:09', '2025-03-19 16:25:09', '无', 0, 887375);
INSERT INTO health_system.patient (uuid, name, id_card, gender, age, birth_date, phone, address, create_time, update_time, remark, is_dead, doctor_number) VALUES ('ab955e5b-049b-11f0-b6db-7c8ae187fc8b', '吴九', '110101199707070077', 1, 71, '1954-01-02', '13800138007', '重庆市渝中区', '2025-05-09 16:25:09', '2025-05-12 08:31:43', '无', 0, 887375);
INSERT INTO health_system.patient (uuid, name, id_card, gender, age, birth_date, phone, address, create_time, update_time, remark, is_dead, doctor_number) VALUES ('ab956007-049b-11f0-b6db-7c8ae187fc8b', '郑十', '110101199808080088', 2, 67, '1998-08-08', '13800138008', '南京市鼓楼区', '2025-05-09 16:25:09', '2025-03-19 16:25:09', '无', 0, 887375);
INSERT INTO health_system.patient (uuid, name, id_card, gender, age, birth_date, phone, address, create_time, update_time, remark, is_dead, doctor_number) VALUES ('ac5e918b-d6ea-42d0-8d4e-b83ca4d91d8e', '周敏', '200101200010101234', 2, 66, '2000-10-10', '13912349876', '天津市和平区', '2025-04-01 08:55:01', '2025-04-01 08:57:45', '无', 0, 110110);
INSERT INTO health_system.patient (uuid, name, id_card, gender, age, birth_date, phone, address, create_time, update_time, remark, is_dead, doctor_number) VALUES ('dba92159-6f12-4bcd-a789-baebbbe74ef0', '吴华盛', '320501200301275256', 1, 45, '2025-03-02', '18306130532', '南京市栖霞区', '2025-05-08 09:11:56', '2025-05-10 09:11:56', '1', 0, 887375);
INSERT INTO health_system.patient (uuid, name, id_card, gender, age, birth_date, phone, address, create_time, update_time, remark, is_dead, doctor_number) VALUES ('e6b4909a-7d2b-4a9d-8e3d-0a395478dfcd', '王明', '120101198505051234', 1, 40, '1985-05-05', '13987654321', '上海市浦东新区', '2025-04-23 10:12:03', '2025-04-09 10:15:00', '无', 0, 887375);
INSERT INTO health_system.patient (uuid, name, id_card, gender, age, birth_date, phone, address, create_time, update_time, remark, is_dead, doctor_number) VALUES ('f6a4a7f8-70fe-48d7-ae5a-cbdc4d2a77e1', '王波', '320501200301273636', 2, 55, '2025-02-11', '18104040404', '南京市浦口区', '2025-05-10 09:08:43', '2025-05-09 09:08:43', '1', 0, 887375);
INSERT INTO health_system.patient (uuid, name, id_card, gender, age, birth_date, phone, address, create_time, update_time, remark, is_dead, doctor_number) VALUES ('f8de54b0-5458-4d26-92b0-7c85a2fd46e4', '刘伟', '320402303020302020', 1, 55, '1970-03-05', '18311329292', '苏州市', '2025-05-09 13:54:38', '2025-05-09 13:54:38', '无', 0, 110110);
