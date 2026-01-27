create table furniture
(
    id             bigint auto_increment comment 'id'
        primary key,
    furniture_name varchar(512) not null comment '家具名称',
    position_x     bigint       not null comment 'x坐标',
    position_y     bigint       not null comment 'y坐标',
    width          bigint       not null comment '宽度',
    height         bigint       not null comment '高度',
    room_id        bigint       not null,
    file_name      varchar(512) not null comment '图片名称'
)
    comment '家具';

create index furniture_room_id_index
    on furniture (room_id);

create table house
(
    id         bigint auto_increment comment 'id'
        primary key,
    house_name varchar(512) not null comment '房子名称'
)
    comment '房子';

create table house_user
(
    id       bigint auto_increment comment 'id'
        primary key,
    user_id  bigint not null comment '用户id',
    house_id bigint not null comment '房子id'
);

create table room
(
    id        bigint auto_increment comment 'id'
        primary key,
    room_name varchar(512) not null comment '房间名称',
    house_id  bigint       not null comment '房子id'
)
    comment '房间';

create table user
(
    id       bigint auto_increment comment 'id'
        primary key,
    username varchar(512) not null comment '用户名',
    password varchar(512) not null comment '密码'
)
    comment '用户表';

