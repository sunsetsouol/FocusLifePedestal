/*
 Navicat Premium Data Transfer

 Source Server         : QGStudio
 Source Server Type    : MySQL
 Source Server Version : 50733
 Source Host           : 39.98.41.126:3306
 Source Schema         : pedestal

 Target Server Type    : MySQL
 Target Server Version : 50733
 File Encoding         : 65001

 Date: 17/04/2024 09:45:30
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for focus_group
-- ----------------------------

drop
    database if exists pedestal;
create
    database pedestal;

use
    pedestal;
DROP TABLE IF EXISTS `focus_group`;
CREATE TABLE `focus_group`
(
    `id`            int(11)                                                        NOT NULL,
    `name`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '群组名',
    `head_image`    varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'https://gitee.com/sunsetsouol/pic/raw/pic/picture/202404141831439.png' COMMENT '头像',
    `owner_user_id` int(11)                                                        NOT NULL COMMENT '群主userId',
    `description`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '群组描述',
    `member_number` int(11)                                                        NOT NULL COMMENT '成员人数',
    `version`       int(11)                                                        NOT NULL DEFAULT 0 COMMENT '乐观锁',
    `deleted`       int(11)                                                        NULL     DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for focus_group_invite
-- ----------------------------
DROP TABLE IF EXISTS `focus_group_invite`;

-- ----------------------------
-- Table structure for focus_on_event
-- ----------------------------
DROP TABLE IF EXISTS `focus_on_event`;
CREATE TABLE `focus_on_event`
(
    `id`              int(11)                                                       NOT NULL AUTO_INCREMENT,
    `focus_id`        int(11)                                                       NOT NULL COMMENT 'foucus事件的id',
    `real_start_time` datetime                                                      NOT NULL COMMENT '真实开始时间',
    `focus_time`      int(11)                                                       NOT NULL COMMENT '专注时间（单位：分)',
    `suspend_time`    int(11)                                                       NOT NULL DEFAULT 0 COMMENT '暂停次数',
    `is_completed`    int(1)                                                        NOT NULL COMMENT '是否完成',
    `note`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '备注',
    `space_id`        int(11)                                                       NULL     DEFAULT NULL COMMENT '空间id（null就不是空间内的专注）',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 9
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for focus_on_template
-- ----------------------------
DROP TABLE IF EXISTS `focus_on_template`;
CREATE TABLE `focus_on_template`
(
    `id`                    int(11)                                                       NOT NULL AUTO_INCREMENT,
    `mission_name`          varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '模板名字',
    `user_id`               int(11)                                                       NOT NULL COMMENT '用户id',
    `focus_start_time`      varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '专注开始时间',
    `focus_duration`        int(11)                                                       NOT NULL COMMENT '专注持续时间（单位分）',
    `completion`            int(4)                                                        NOT NULL DEFAULT 0 COMMENT '完成次数',
    `completion_total_time` int(11)                                                       NOT NULL DEFAULT 0 COMMENT '总时长（分钟）',
    `note`                  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '备注',
    `deleted`               int(1)                                                        NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 59
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for friendship
-- ----------------------------
DROP TABLE IF EXISTS `friendship`;
CREATE TABLE `friendship`
(
    `id`              bigint(20)                                                    NOT NULL AUTO_INCREMENT,
    `from_id`         int(11)                                                       NOT NULL COMMENT '发送者id',
    `to_id`           int(11)                                                       NOT NULL COMMENT '接收者id',
    `status`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'application' COMMENT '状态（application，agree，disagree，overdue）',
    `create_time`     datetime                                                      NOT NULL default CURRENT_TIMESTAMP COMMENT '发送时间',
    `expiration_time` datetime                                                      NOT NULL COMMENT '过期时间',
    `deleted`         int(11)                                                       NULL     DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `apply_union` (`from_id`, `to_id`, `deleted`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 13
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pedestal
-- ----------------------------
DROP TABLE IF EXISTS `pedestal`;
CREATE TABLE `pedestal`
(
    `id`        int(11)                                                       NOT NULL AUTO_INCREMENT,
    `equipment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备号',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for space
-- ----------------------------
DROP TABLE IF EXISTS `space`;
CREATE TABLE `space`
(
    `id`            bigint(20)                                                    NOT NULL AUTO_INCREMENT,
    `name`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '名字',
    `owner_user_id` int(11)                                                       NULL     DEFAULT NULL COMMENT '空间拥有者用户id',
    `description`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '空间描述',
    `head_image`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'https://gitee.com/sunsetsouol/pic/raw/pic/picture/202404141831439.png' COMMENT '头像',
    `member_number` int(11)                                                       NOT NULL DEFAULT 1 COMMENT '空间人数',
    `version`       int(11)                                                       NOT NULL DEFAULT 0 COMMENT '乐观锁',
    `deleted`       int(11)                                                       NULL     DEFAULT 0 COMMENT '逻辑删除',
    `create_time`   datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP on update current_timestamp COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for space_invite
-- ----------------------------
DROP TABLE IF EXISTS `space_invite`;
CREATE TABLE `space_invite`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT,
    `inviter_id`    int(11)    NOT NULL COMMENT '邀请人id',
    `space_id`      int(11)    NOT NULL COMMENT '空间id',
    `to_user_id`    int(11)    NULL     DEFAULT NULL COMMENT '受邀请用户id',
    `create_time`   datetime   NOT NULL default CURRENT_TIMESTAMP COMMENT '邀请时间',
    `validaty_time` datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP on update current_timestamp COMMENT '有效期',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`                        int(11)                                                       NOT NULL AUTO_INCREMENT,
    `name`                      varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '昵称',
    `head_image`                varchar(225) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'https://gitee.com/sunsetsouol/pic/raw/pic/picture/202404141831439.png' COMMENT '头像',
    `email`                     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '邮箱',
    `password`                  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
    `default_water_intake`      int(11)                                                       NOT NULL DEFAULT 1000 COMMENT '默认喝水量',
    `default_reminder_interval` int(11)                                                       NOT NULL DEFAULT 30 COMMENT '提醒间隔',
    `total_completion_time`     int(11)                                                       NOT NULL DEFAULT 0 COMMENT '总专注时长',
    `total_water_intake`        int(11)                                                       NOT NULL DEFAULT 0 COMMENT '总喝水量',
    `uid`                       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `email_union` (`email`) USING BTREE,
    UNIQUE INDEX `uid_union` (`uid`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 9
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user_pedestal_map
-- ----------------------------
DROP TABLE IF EXISTS `user_pedestal_map`;
CREATE TABLE `user_pedestal_map`
(
    `id`          int(11) NOT NULL AUTO_INCREMENT,
    `user_id`     int(11) NOT NULL COMMENT '用户id',
    `pedestal_id` int(11) NOT NULL COMMENT '底座id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_space
-- ----------------------------
DROP TABLE IF EXISTS `user_space`;
CREATE TABLE `user_space`
(
    `id`               int(11)    NOT NULL AUTO_INCREMENT,
    `user_id`          int(11)    NOT NULL COMMENT '用户id',
    `space_id`         bigint(11) NOT NULL COMMENT '空间id',
    `event_id`         int(11)    NULL     DEFAULT NULL COMMENT '正在专注事件id',
    `focus_start_time` datetime   NULL     DEFAULT NULL COMMENT '上一次专注开始时间',
    `focus_times`      int(11)    NOT NULL DEFAULT 0 COMMENT '空间专注次数',
    `total_focus_time` int(11)    NOT NULL DEFAULT 0 COMMENT '空间总专注时长',
    `deleted`          int(11)    NULL     DEFAULT 0 COMMENT '逻辑删除',
    `update_time`      datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_time`      datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 7
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for water_intake
-- ----------------------------
DROP TABLE IF EXISTS `water_intake`;
CREATE TABLE `water_intake`
(
    `id`            int(11) NOT NULL AUTO_INCREMENT,
    `user_id`       int(11) NOT NULL COMMENT '用户id',
    `intake_date`   date    NOT NULL COMMENT '日期',
    `intake_target` int(11) NULL DEFAULT NULL COMMENT '目标摄水量',
    `intake_real`   int(11) NULL DEFAULT NULL COMMENT '实际摄水量',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `user_date_union` (`user_id`, `intake_date`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 16
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;

DROP TABLE IF EXISTS `private_chat_history`;
CREATE TABLE `private_chat_history`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT,
    `from_id`     int(11)      NOT NULL COMMENT '发送者id',
    `to_id`       int(11)      NOT NULL COMMENT '接收者id',
    `type`        varchar(255) NOT NULL COMMENT '类型（文本或邀请）',
    `context`     text         NOT NULL COMMENT '内容',
    `create_time` datetime     NOT NULL default CURRENT_TIMESTAMP COMMENT '发送时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `group_chat_history`;
CREATE TABLE `group_chat_history`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT,
    `from_id`     int(11)      NOT NULL COMMENT '发送者id',
    `group_id`    int(11)      NOT NULL COMMENT '群聊id',
    `type`        varchar(255) NOT NULL COMMENT '类型',
    `context`     text         NOT NULL COMMENT '消息文本',
    `create_time` datetime     NOT NULL default CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4