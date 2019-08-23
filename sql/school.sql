/*
Navicat MySQL Data Transfer
Source Server         : 47.98.125.232
Source Server Version : 50727
Source Host           : 47.98.125.232:3306
Source Database       : febs_base
Target Server Type    : MYSQL
Target Server Version : 50727
File Encoding         : 65001
Date: 2019-08-17 13:04:36
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for jcc_area
-- ----------------------------
DROP TABLE IF EXISTS `jcc_area`;
CREATE TABLE `jcc_area` (
  `area_code` varchar(30) NOT NULL,
  `province` varchar(30) DEFAULT NULL,
  `city` varchar(30) DEFAULT NULL,
  `country` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`area_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jcc_area
-- ----------------------------
INSERT INTO `jcc_area` VALUES ('1001', '湖南省', '长沙市', '雨花区');
INSERT INTO `jcc_area` VALUES ('1002', '湖南省', '长沙市', '天心区');

-- ----------------------------
-- Table structure for jcc_class_info
-- ----------------------------
DROP TABLE IF EXISTS `jcc_class_info`;
CREATE TABLE `jcc_class_info` (
  `class_id` int(11) NOT NULL,
  `class_name` varchar(30) DEFAULT NULL,
  `username` varchar(30) DEFAULT NULL,
  `grade_id` int(11) DEFAULT NULL,
  `year` char(20) DEFAULT NULL,
  PRIMARY KEY (`class_id`),
  KEY `FK_Reference_18` (`grade_id`),
  CONSTRAINT `FK_Reference_18` FOREIGN KEY (`grade_id`) REFERENCES `jcc_grade_info` (`grade_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jcc_class_info
-- ----------------------------

-- ----------------------------
-- Table structure for jcc_classroom_info
-- ----------------------------
DROP TABLE IF EXISTS `jcc_classroom_info`;
CREATE TABLE `jcc_classroom_info` (
  `id` int(11) NOT NULL,
  `school_id` int(11) DEFAULT NULL,
  `location` char(100) DEFAULT NULL,
  `contain_num` int(11) DEFAULT NULL,
  `purpose` char(100) DEFAULT NULL,
  `url` char(100) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_Reference_22` (`school_id`),
  CONSTRAINT `jcc_classroom_info_ibfk_1` FOREIGN KEY (`school_id`) REFERENCES `jcc_school_info` (`school_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jcc_classroom_info
-- ----------------------------

-- ----------------------------
-- Table structure for jcc_device_info
-- ----------------------------
DROP TABLE IF EXISTS `jcc_device_info`;
CREATE TABLE `jcc_device_info` (
  `device_id` int(11) NOT NULL AUTO_INCREMENT,
  `device_name` varchar(30) DEFAULT NULL,
  `school_id` int(11) DEFAULT NULL,
  `username` varchar(30) DEFAULT NULL,
  `buyt_time` date DEFAULT NULL,
  `device_type` varchar(30) DEFAULT NULL,
  `firm_id` int(11) DEFAULT NULL,
  `classroom_id` int(11) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  `num` int(11) DEFAULT NULL,
  PRIMARY KEY (`device_id`),
  KEY `FK_Reference_15` (`school_id`),
  KEY `FK_Reference_21` (`firm_id`),
  KEY `FK_Reference_28` (`classroom_id`),
  CONSTRAINT `FK_Reference_21` FOREIGN KEY (`firm_id`) REFERENCES `jcc_firm_info` (`firm_id`),
  CONSTRAINT `FK_Reference_28` FOREIGN KEY (`classroom_id`) REFERENCES `jcc_classroom_info` (`id`),
  CONSTRAINT `jcc_device_info_ibfk_1` FOREIGN KEY (`school_id`) REFERENCES `jcc_school_info` (`school_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jcc_device_info
-- ----------------------------
INSERT INTO `jcc_device_info` VALUES ('1', '投影仪', null, 'kk', '2019-08-15', '硬件', null, null, '1', '1');
INSERT INTO `jcc_device_info` VALUES ('2', '2', null, '2', '2014-12-25', '2', null, null, '0', '0');

-- ----------------------------
-- Table structure for jcc_firm_info
-- ----------------------------
DROP TABLE IF EXISTS `jcc_firm_info`;
CREATE TABLE `jcc_firm_info` (
  `firm_id` int(11) NOT NULL,
  `firm_name` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`firm_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jcc_firm_info
-- ----------------------------

-- ----------------------------
-- Table structure for jcc_grade_info
-- ----------------------------
DROP TABLE IF EXISTS `jcc_grade_info`;
CREATE TABLE `jcc_grade_info` (
  `grade_id` int(11) NOT NULL,
  `grade_name` varchar(30) DEFAULT NULL,
  `school_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`grade_id`),
  KEY `FK_Reference_23` (`school_id`),
  CONSTRAINT `jcc_grade_info_ibfk_1` FOREIGN KEY (`school_id`) REFERENCES `jcc_school_info` (`school_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jcc_grade_info
-- ----------------------------

-- ----------------------------
-- Table structure for jcc_operate_desc
-- ----------------------------
DROP TABLE IF EXISTS `jcc_operate_desc`;
CREATE TABLE `jcc_operate_desc` (
  `id` int(11) NOT NULL,
  `title` char(100) DEFAULT NULL,
  `author` char(50) DEFAULT NULL,
  `upload_time` date DEFAULT NULL,
  `update_time` date DEFAULT NULL,
  `attach_address` char(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jcc_operate_desc
-- ----------------------------

-- ----------------------------
-- Table structure for jcc_resource_evaluate_info
-- ----------------------------
DROP TABLE IF EXISTS `jcc_resource_evaluate_info`;
CREATE TABLE `jcc_resource_evaluate_info` (
  `id` int(11) NOT NULL,
  `product_id` int(11) DEFAULT NULL,
  `comment_content` longtext,
  `comment_date` date DEFAULT NULL,
  `comment_user` char(50) DEFAULT NULL,
  `score` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_Reference_25` (`product_id`),
  CONSTRAINT `FK_Reference_25` FOREIGN KEY (`product_id`) REFERENCES `jcc_resource_info` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jcc_resource_evaluate_info
-- ----------------------------

-- ----------------------------
-- Table structure for jcc_resource_info
-- ----------------------------
DROP TABLE IF EXISTS `jcc_resource_info`;
CREATE TABLE `jcc_resource_info` (
  `product_id` int(11) NOT NULL,
  `product_name` varchar(30) NOT NULL,
  `school_id` int(11) DEFAULT NULL,
  `grade_id` int(11) DEFAULT NULL,
  `subject_id` int(11) DEFAULT NULL,
  `resource_location` varchar(250) DEFAULT NULL,
  `username` varchar(30) DEFAULT NULL,
  `create_time` date DEFAULT NULL,
  `modify_time` date DEFAULT NULL,
  `resouce_type` int(11) DEFAULT NULL,
  `rrtlevel1` int(11) DEFAULT NULL,
  `auditstatus` int(11) DEFAULT NULL,
  `resouce_order` int(11) DEFAULT NULL,
  PRIMARY KEY (`product_id`),
  KEY `FK_Reference_13` (`school_id`),
  KEY `FK_Reference_19` (`grade_id`),
  KEY `FK_Reference_26` (`subject_id`),
  CONSTRAINT `FK_Reference_19` FOREIGN KEY (`grade_id`) REFERENCES `jcc_grade_info` (`grade_id`),
  CONSTRAINT `FK_Reference_26` FOREIGN KEY (`subject_id`) REFERENCES `jcc_subject_info` (`subject_id`),
  CONSTRAINT `jcc_resource_info_ibfk_1` FOREIGN KEY (`school_id`) REFERENCES `jcc_school_info` (`school_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jcc_resource_info
-- ----------------------------

-- ----------------------------
-- Table structure for jcc_school_info
-- ----------------------------
DROP TABLE IF EXISTS `jcc_school_info`;
CREATE TABLE `jcc_school_info` (
  `school_id` int(11) NOT NULL AUTO_INCREMENT,
  `school_name` varchar(30) DEFAULT NULL,
  `introduce` varchar(1000) DEFAULT NULL,
  `school_type` varchar(30) DEFAULT NULL,
  `linkman` varchar(30) DEFAULT NULL,
  `linkphone` varchar(20) DEFAULT NULL,
  `postcode` varchar(10) DEFAULT NULL,
  `area_code` char(30) DEFAULT NULL,
  `address` varchar(100) DEFAULT NULL,
  `lng` varchar(20) DEFAULT NULL,
  `lat` varchar(20) DEFAULT NULL,
  `create_time` date DEFAULT NULL,
  `certificate_book` char(100) DEFAULT NULL,
  `country_leader_name` char(50) DEFAULT NULL,
  `country_date` date DEFAULT NULL,
  `city_leader_name` char(50) DEFAULT NULL,
  `city_date` date DEFAULT NULL,
  `province_leader_name` char(50) DEFAULT NULL,
  `province_date` date DEFAULT NULL,
  `state` int(4) DEFAULT NULL,
  PRIMARY KEY (`school_id`),
  KEY `FK_Reference_31` (`area_code`),
  CONSTRAINT `jcc_school_info_ibfk_1` FOREIGN KEY (`area_code`) REFERENCES `jcc_area` (`area_code`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jcc_school_info
-- ----------------------------
INSERT INTO `jcc_school_info` VALUES ('13', 'jck学校', null, '主校', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

-- ----------------------------
-- Table structure for jcc_school_timetable
-- ----------------------------
DROP TABLE IF EXISTS `jcc_school_timetable`;
CREATE TABLE `jcc_school_timetable` (
  `id` int(11) NOT NULL,
  `user_id` bigint(11) DEFAULT NULL,
  `course_name` char(50) DEFAULT NULL,
  `begin_date` date DEFAULT NULL,
  `duration` int(11) DEFAULT NULL,
  `school` char(100) DEFAULT NULL,
  `grade` char(30) DEFAULT NULL,
  `class_id` char(30) DEFAULT NULL,
  `subject` char(50) DEFAULT NULL,
  `term` char(30) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `foreign_key_userid` (`user_id`),
  CONSTRAINT `foreign_key_userid` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jcc_school_timetable
-- ----------------------------

-- ----------------------------
-- Table structure for jcc_subject_info
-- ----------------------------
DROP TABLE IF EXISTS `jcc_subject_info`;
CREATE TABLE `jcc_subject_info` (
  `subject_id` int(11) NOT NULL,
  `subject_name` char(50) DEFAULT NULL,
  PRIMARY KEY (`subject_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jcc_subject_info
-- ----------------------------

-- ----------------------------
-- Table structure for jcc_topic_info
-- ----------------------------
DROP TABLE IF EXISTS `jcc_topic_info`;
CREATE TABLE `jcc_topic_info` (
  `special_id` int(11) NOT NULL,
  `special_name` varchar(30) DEFAULT NULL,
  `create_date` date DEFAULT NULL,
  `create_user` char(30) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  `update_date` date DEFAULT NULL,
  `update_user` char(30) DEFAULT NULL,
  `special_desc` char(200) DEFAULT NULL,
  PRIMARY KEY (`special_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jcc_topic_info
-- ----------------------------

-- ----------------------------
-- Table structure for jcc_topic_resource
-- ----------------------------
DROP TABLE IF EXISTS `jcc_topic_resource`;
CREATE TABLE `jcc_topic_resource` (
  `special_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  PRIMARY KEY (`special_id`,`product_id`),
  KEY `FK_Reference_24` (`product_id`),
  CONSTRAINT `FK_Reference_17` FOREIGN KEY (`special_id`) REFERENCES `jcc_topic_info` (`special_id`),
  CONSTRAINT `FK_Reference_24` FOREIGN KEY (`product_id`) REFERENCES `jcc_resource_info` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jcc_topic_resource
-- ----------------------------

-- ----------------------------
-- Table structure for t_dept
-- ----------------------------
DROP TABLE IF EXISTS `t_dept`;
CREATE TABLE `t_dept` (
  `DEPT_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '部门ID',
  `PARENT_ID` bigint(20) NOT NULL COMMENT '上级部门ID',
  `DEPT_NAME` varchar(100) NOT NULL COMMENT '部门名称',
  `ORDER_NUM` bigint(20) DEFAULT NULL COMMENT '排序',
  `CREATE_TIME` date DEFAULT NULL COMMENT '创建时间',
  `MODIFY_TIME` date DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`DEPT_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='部门表';

-- ----------------------------
-- Records of t_dept
-- ----------------------------
INSERT INTO `t_dept` VALUES ('1', '0', '开发部', '1', '2019-06-14', null);
INSERT INTO `t_dept` VALUES ('2', '1', '开发一部', '1', '2019-06-14', null);
INSERT INTO `t_dept` VALUES ('3', '1', '开发二部', '2', '2019-06-14', null);
INSERT INTO `t_dept` VALUES ('4', '0', '采购部', '2', '2019-06-14', null);
INSERT INTO `t_dept` VALUES ('5', '0', '财务部', '3', '2019-06-14', null);
INSERT INTO `t_dept` VALUES ('6', '0', '销售部', '4', '2019-06-14', null);
INSERT INTO `t_dept` VALUES ('7', '0', '工程部', '5', '2019-06-14', null);
INSERT INTO `t_dept` VALUES ('8', '0', '行政部', '6', '2019-06-14', null);
INSERT INTO `t_dept` VALUES ('10', '0', '系统部', '7', '2019-06-14', null);

-- ----------------------------
-- Table structure for t_eximport
-- ----------------------------
DROP TABLE IF EXISTS `t_eximport`;
CREATE TABLE `t_eximport` (
  `FIELD1` varchar(20) NOT NULL COMMENT '字段1',
  `FIELD2` int(11) NOT NULL COMMENT '字段2',
  `FIELD3` varchar(100) NOT NULL COMMENT '字段3',
  `CREATE_TIME` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='Excel导入导出测试';

-- ----------------------------
-- Records of t_eximport
-- ----------------------------
INSERT INTO `t_eximport` VALUES ('字段1', '1', 'mrbird0@gmail.com', '2019-06-13');
INSERT INTO `t_eximport` VALUES ('字段1', '2', 'mrbird1@gmail.com', '2019-06-13');
INSERT INTO `t_eximport` VALUES ('字段1', '3', 'mrbird2@gmail.com', '2019-06-13');
INSERT INTO `t_eximport` VALUES ('字段1', '4', 'mrbird3@gmail.com', '2019-06-13');
INSERT INTO `t_eximport` VALUES ('字段1', '5', 'mrbird4@gmail.com', '2019-06-13');
INSERT INTO `t_eximport` VALUES ('字段1', '6', 'mrbird5@gmail.com', '2019-06-13');
INSERT INTO `t_eximport` VALUES ('字段1', '7', 'mrbird6@gmail.com', '2019-06-13');
INSERT INTO `t_eximport` VALUES ('字段1', '8', 'mrbird7@gmail.com', '2019-06-13');
INSERT INTO `t_eximport` VALUES ('字段1', '9', 'mrbird8@gmail.com', '2019-06-13');
INSERT INTO `t_eximport` VALUES ('字段1', '10', 'mrbird9@gmail.com', '2019-06-13');
INSERT INTO `t_eximport` VALUES ('字段1', '11', 'mrbird10@gmail.com', '2019-06-13');
INSERT INTO `t_eximport` VALUES ('字段1', '12', 'mrbird11@gmail.com', '2019-06-13');
INSERT INTO `t_eximport` VALUES ('字段1', '13', 'mrbird12@gmail.com', '2019-06-13');
INSERT INTO `t_eximport` VALUES ('字段1', '14', 'mrbird13@gmail.com', '2019-06-13');
INSERT INTO `t_eximport` VALUES ('字段1', '15', 'mrbird14@gmail.com', '2019-06-13');
INSERT INTO `t_eximport` VALUES ('字段1', '16', 'mrbird15@gmail.com', '2019-06-13');
INSERT INTO `t_eximport` VALUES ('字段1', '17', 'mrbird16@gmail.com', '2019-06-13');
INSERT INTO `t_eximport` VALUES ('字段1', '18', 'mrbird17@gmail.com', '2019-06-13');
INSERT INTO `t_eximport` VALUES ('字段1', '19', 'mrbird18@gmail.com', '2019-06-13');
INSERT INTO `t_eximport` VALUES ('字段1', '20', 'mrbird19@gmail.com', '2019-06-13');

-- ----------------------------
-- Table structure for t_generator_config
-- ----------------------------
DROP TABLE IF EXISTS `t_generator_config`;
CREATE TABLE `t_generator_config` (
  `id` int(11) NOT NULL COMMENT '主键',
  `author` varchar(20) NOT NULL COMMENT '作者',
  `base_package` varchar(50) NOT NULL COMMENT '基础包名',
  `entity_package` varchar(20) NOT NULL COMMENT 'entity文件存放路径',
  `mapper_package` varchar(20) NOT NULL COMMENT 'mapper文件存放路径',
  `mapper_xml_package` varchar(20) NOT NULL COMMENT 'mapper xml文件存放路径',
  `service_package` varchar(20) NOT NULL COMMENT 'servcie文件存放路径',
  `service_impl_package` varchar(20) NOT NULL COMMENT 'serviceImpl文件存放路径',
  `controller_package` varchar(20) NOT NULL COMMENT 'controller文件存放路径',
  `is_trim` char(1) NOT NULL COMMENT '是否去除前缀 1是 0否',
  `trim_value` varchar(10) DEFAULT NULL COMMENT '前缀内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='代码生成配置表';

-- ----------------------------
-- Records of t_generator_config
-- ----------------------------
INSERT INTO `t_generator_config` VALUES ('1', 'psy', 'cc.mrbird.febs.basicInfo', 'entity', 'mapper', 'mapper', 'service', 'service.impl', 'controller', '1', 'jcc_');

-- ----------------------------
-- Table structure for t_job
-- ----------------------------
DROP TABLE IF EXISTS `t_job`;
CREATE TABLE `t_job` (
  `JOB_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务id',
  `BEAN_NAME` varchar(50) NOT NULL COMMENT 'spring bean名称',
  `METHOD_NAME` varchar(50) NOT NULL COMMENT '方法名',
  `PARAMS` varchar(50) DEFAULT NULL COMMENT '参数',
  `CRON_EXPRESSION` varchar(20) NOT NULL COMMENT 'cron表达式',
  `STATUS` char(2) NOT NULL COMMENT '任务状态  0：正常  1：暂停',
  `REMARK` varchar(50) DEFAULT NULL COMMENT '备注',
  `CREATE_TIME` date DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`JOB_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='定时任务表';

-- ----------------------------
-- Records of t_job
-- ----------------------------
INSERT INTO `t_job` VALUES ('1', 'testTask', 'test', 'mrbird', '0/1 * * * * ?', '1', '有参任务调度测试~~', '2018-02-24');
INSERT INTO `t_job` VALUES ('2', 'testTask', 'test1', null, '0/10 * * * * ?', '1', '无参任务调度测试', '2018-02-24');
INSERT INTO `t_job` VALUES ('3', 'testTask', 'test', 'hello world', '0/1 * * * * ?', '1', '有参任务调度测试,每隔一秒触发', '2018-02-26');
INSERT INTO `t_job` VALUES ('11', 'testTask', 'test2', null, '0/5 * * * * ?', '1', '测试异常', '2018-02-26');

-- ----------------------------
-- Table structure for t_job_log
-- ----------------------------
DROP TABLE IF EXISTS `t_job_log`;
CREATE TABLE `t_job_log` (
  `LOG_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务日志id',
  `JOB_ID` bigint(20) NOT NULL COMMENT '任务id',
  `BEAN_NAME` varchar(100) NOT NULL COMMENT 'spring bean名称',
  `METHOD_NAME` varchar(100) NOT NULL COMMENT '方法名',
  `PARAMS` varchar(200) DEFAULT NULL COMMENT '参数',
  `STATUS` char(2) NOT NULL COMMENT '任务状态    0：成功    1：失败',
  `ERROR` text COMMENT '失败信息',
  `TIMES` decimal(11,0) DEFAULT NULL COMMENT '耗时(单位：毫秒)',
  `CREATE_TIME` date DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`LOG_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5803 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='调度日志表';

-- ----------------------------
-- Records of t_job_log
-- ----------------------------

-- ----------------------------
-- Table structure for t_log
-- ----------------------------
DROP TABLE IF EXISTS `t_log`;
CREATE TABLE `t_log` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `USERNAME` varchar(50) DEFAULT NULL COMMENT '操作用户',
  `OPERATION` text COMMENT '操作内容',
  `TIME` decimal(11,0) DEFAULT NULL COMMENT '耗时',
  `METHOD` text COMMENT '操作方法',
  `PARAMS` text COMMENT '方法参数',
  `IP` varchar(64) DEFAULT NULL COMMENT '操作者IP',
  `CREATE_TIME` date DEFAULT NULL COMMENT '创建时间',
  `location` varchar(50) DEFAULT NULL COMMENT '操作地点',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1142 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='操作日志表';

-- ----------------------------
-- Table structure for t_login_log
-- ----------------------------
DROP TABLE IF EXISTS `t_login_log`;
CREATE TABLE `t_login_log` (
  `ID` bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `USERNAME` varchar(50) NOT NULL COMMENT '用户名',
  `LOGIN_TIME` date NOT NULL COMMENT '登录时间',
  `LOCATION` varchar(50) DEFAULT NULL COMMENT '登录地点',
  `IP` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `SYSTEM` varchar(50) DEFAULT NULL COMMENT '操作系统',
  `BROWSER` varchar(50) DEFAULT NULL COMMENT '浏览器',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=144 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='登录日志表';

-- ----------------------------
-- Records of t_login_log
-- ----------------------------
INSERT INTO `t_login_log` VALUES ('64', 'mrbird', '2019-08-15', '内网IP|0|0|内网IP|内网IP', '192.168.137.1', 'Windows 10', 'Chrome 70');

-- ----------------------------
-- Table structure for t_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_menu`;
CREATE TABLE `t_menu` (
  `MENU_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单/按钮ID',
  `PARENT_ID` bigint(20) NOT NULL COMMENT '上级菜单ID',
  `MENU_NAME` varchar(50) NOT NULL COMMENT '菜单/按钮名称',
  `URL` varchar(50) DEFAULT NULL COMMENT '菜单URL',
  `PERMS` text COMMENT '权限标识',
  `ICON` varchar(50) DEFAULT NULL COMMENT '图标',
  `TYPE` char(2) NOT NULL COMMENT '类型 0菜单 1按钮',
  `ORDER_NUM` bigint(20) DEFAULT NULL COMMENT '排序',
  `CREATE_TIME` date NOT NULL COMMENT '创建时间',
  `MODIFY_TIME` date DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`MENU_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=200 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='菜单表';

-- ----------------------------
-- Records of t_menu
-- ----------------------------
INSERT INTO `t_menu` VALUES ('1', '0', '系统管理', null, null, 'layui-icon-setting', '0', '1', '2017-12-27', null);
INSERT INTO `t_menu` VALUES ('2', '0', '系统监控', '', '', 'layui-icon-alert', '0', '6', '2017-12-27', '2019-08-17');
INSERT INTO `t_menu` VALUES ('3', '1', '用户管理', '/system/user', 'user:view', 'layui-icon-meh', '0', '1', '2017-12-27', '2019-06-13');
INSERT INTO `t_menu` VALUES ('4', '1', '角色管理', '/system/role', 'role:view', '', '0', '2', '2017-12-27', '2019-06-13');
INSERT INTO `t_menu` VALUES ('5', '1', '菜单管理', '/system/menu', 'menu:view', '', '0', '3', '2017-12-27', '2019-06-13');
INSERT INTO `t_menu` VALUES ('6', '1', '部门管理', '/system/dept', 'dept:view', '', '0', '4', '2017-12-27', '2019-06-14');
INSERT INTO `t_menu` VALUES ('8', '2', '在线用户', '/monitor/online', 'online:view', '', '0', '1', '2017-12-27', '2019-06-13');
INSERT INTO `t_menu` VALUES ('10', '2', '系统日志', '/monitor/log', 'log:view', '', '0', '2', '2017-12-27', '2019-06-13');
INSERT INTO `t_menu` VALUES ('11', '3', '新增用户', null, 'user:add', null, '1', null, '2017-12-27', null);
INSERT INTO `t_menu` VALUES ('12', '3', '修改用户', null, 'user:update', null, '1', null, '2017-12-27', null);
INSERT INTO `t_menu` VALUES ('13', '3', '删除用户', null, 'user:delete', null, '1', null, '2017-12-27', null);
INSERT INTO `t_menu` VALUES ('14', '4', '新增角色', null, 'role:add', null, '1', null, '2017-12-27', null);
INSERT INTO `t_menu` VALUES ('15', '4', '修改角色', null, 'role:update', null, '1', null, '2017-12-27', null);
INSERT INTO `t_menu` VALUES ('16', '4', '删除角色', null, 'role:delete', null, '1', null, '2017-12-27', null);
INSERT INTO `t_menu` VALUES ('17', '5', '新增菜单', null, 'menu:add', null, '1', null, '2017-12-27', null);
INSERT INTO `t_menu` VALUES ('18', '5', '修改菜单', null, 'menu:update', null, '1', null, '2017-12-27', null);
INSERT INTO `t_menu` VALUES ('19', '5', '删除菜单', null, 'menu:delete', null, '1', null, '2017-12-27', null);
INSERT INTO `t_menu` VALUES ('20', '6', '新增部门', null, 'dept:add', null, '1', null, '2017-12-27', null);
INSERT INTO `t_menu` VALUES ('21', '6', '修改部门', null, 'dept:update', null, '1', null, '2017-12-27', null);
INSERT INTO `t_menu` VALUES ('22', '6', '删除部门', null, 'dept:delete', null, '1', null, '2017-12-27', null);
INSERT INTO `t_menu` VALUES ('23', '8', '踢出用户', null, 'user:kickout', null, '1', null, '2017-12-27', null);
INSERT INTO `t_menu` VALUES ('24', '10', '删除日志', null, 'log:delete', null, '1', null, '2017-12-27', '2019-06-06');
INSERT INTO `t_menu` VALUES ('101', '0', '任务调度', '', '', 'layui-icon-time-circle', '0', '5', '2018-02-24', '2019-08-17');
INSERT INTO `t_menu` VALUES ('102', '101', '定时任务', '/job/job', 'job:view', '', '0', '1', '2018-02-24', '2018-04-25');
INSERT INTO `t_menu` VALUES ('103', '102', '新增任务', null, 'job:add', null, '1', null, '2018-02-24', null);
INSERT INTO `t_menu` VALUES ('104', '102', '修改任务', null, 'job:update', null, '1', null, '2018-02-24', null);
INSERT INTO `t_menu` VALUES ('105', '102', '删除任务', null, 'job:delete', null, '1', null, '2018-02-24', null);
INSERT INTO `t_menu` VALUES ('106', '102', '暂停任务', null, 'job:pause', null, '1', null, '2018-02-24', null);
INSERT INTO `t_menu` VALUES ('107', '102', '恢复任务', null, 'job:resume', null, '1', null, '2018-02-24', null);
INSERT INTO `t_menu` VALUES ('108', '102', '立即执行任务', null, 'job:run', null, '1', null, '2018-02-24', null);
INSERT INTO `t_menu` VALUES ('109', '101', '调度日志', '/job/log', 'job:log:view', '', '0', '2', '2018-02-24', '2019-06-09');
INSERT INTO `t_menu` VALUES ('110', '109', '删除日志', null, 'job:log:delete', null, '1', null, '2018-02-24', null);
INSERT INTO `t_menu` VALUES ('113', '2', 'Redis监控', '/monitor/redis/info', 'redis:view', '', '0', '4', '2018-06-28', '2019-06-13');
INSERT INTO `t_menu` VALUES ('114', '2', 'Redis终端', '/monitor/redis/terminal', 'redis:terminal:view', '', '0', '5', '2018-06-28', '2019-06-13');
INSERT INTO `t_menu` VALUES ('127', '2', '请求追踪', '/monitor/httptrace', 'httptrace:view', '', '0', '6', '2019-05-27', '2019-06-13');
INSERT INTO `t_menu` VALUES ('128', '2', '系统信息', null, null, null, '0', '7', '2019-05-27', null);
INSERT INTO `t_menu` VALUES ('129', '128', 'JVM信息', '/monitor/jvm', 'jvm:view', '', '0', '1', '2019-05-27', '2019-06-13');
INSERT INTO `t_menu` VALUES ('130', '128', 'Tomcat信息', '/monitor/tomcat', 'tomcat:view', '', '0', '2', '2019-05-27', '2019-06-13');
INSERT INTO `t_menu` VALUES ('131', '128', '服务器信息', '/monitor/server', 'server:view', '', '0', '3', '2019-05-27', '2019-06-13');
INSERT INTO `t_menu` VALUES ('136', '2', '登录日志', '/monitor/loginlog', 'loginlog:view', '', '0', '3', '2019-05-29', '2019-06-13');
INSERT INTO `t_menu` VALUES ('137', '0', '代码生成', '', '', 'layui-icon-verticalright', '0', '7', '2019-06-03', '2019-08-17');
INSERT INTO `t_menu` VALUES ('138', '137', '生成配置', '/generator/configure', 'generator:configure:view', null, '0', '1', '2019-06-03', null);
INSERT INTO `t_menu` VALUES ('139', '137', '代码生成', '/generator/generator', 'generator:view', '', '0', '2', '2019-06-03', '2019-06-13');
INSERT INTO `t_menu` VALUES ('165', '138', '修改配置', null, 'generator:configure:update', null, '1', null, '2019-06-13', '2019-06-13');
INSERT INTO `t_menu` VALUES ('166', '139', '生成代码', null, 'generator:generate', null, '1', null, '2019-06-13', null);
INSERT INTO `t_menu` VALUES ('170', '10', '导出Excel', null, 'log:export', null, '1', null, '2019-06-13', null);
INSERT INTO `t_menu` VALUES ('171', '136', '删除日志', null, 'loginlog:delete', null, '1', null, '2019-06-13', '2019-06-13');
INSERT INTO `t_menu` VALUES ('172', '136', '导出Excel', null, 'loginlog:export', null, '1', null, '2019-06-13', null);
INSERT INTO `t_menu` VALUES ('173', '102', '导出Excel', null, 'job:export', null, '1', null, '2019-06-13', null);
INSERT INTO `t_menu` VALUES ('174', '109', '导出Excel', null, 'job:log:export', null, '1', null, '2019-06-13', '2019-06-13');
INSERT INTO `t_menu` VALUES ('175', '0', '数据管理', '', '', 'layui-icon-control', '0', '4', '2019-08-16', '2019-08-17');
INSERT INTO `t_menu` VALUES ('177', '175', '地区管理', '/basicInfo/area', 'area:view', '', '0', '1', '2019-08-16', '2019-08-16');
INSERT INTO `t_menu` VALUES ('179', '177', '删除地区', null, 'area:delete', null, '1', null, '2019-08-16', '2019-08-16');
INSERT INTO `t_menu` VALUES ('180', '177', '新增地区', null, 'area:add', null, '1', null, '2019-08-16', null);
INSERT INTO `t_menu` VALUES ('181', '177', '修改地区', null, 'area:update', null, '1', null, '2019-08-16', null);
INSERT INTO `t_menu` VALUES ('183', '0', '基础管理', '', '', 'layui-icon-key', '0', '2', '2019-08-16', '2019-08-17');
INSERT INTO `t_menu` VALUES ('186', '183', '学校管理', '/schoolInfo', 'schoolInfo:view', '', '0', '1', '2019-08-16', '2019-08-17');
INSERT INTO `t_menu` VALUES ('190', '0', '资源管理', '', '', 'layui-icon-cloud-server', '0', '3', '2019-08-17', '2019-08-17');
INSERT INTO `t_menu` VALUES ('192', '1', '其它权限', '', '', '', '0', '5', '2019-08-17', null);
INSERT INTO `t_menu` VALUES ('193', '192', '查看图标', null, 'febs:icons:view', null, '1', null, '2019-08-17', null);
INSERT INTO `t_menu` VALUES ('195', '190', '资源类别', '/resource/category', '', '', '0', '2', '2019-08-17', '2019-08-17');
INSERT INTO `t_menu` VALUES ('196', '190', '资源列表', '/resource/list', '', '', '0', '1', '2019-08-17', '2019-08-17');
INSERT INTO `t_menu` VALUES ('197', '190', '资源专题', '/resource/subject', '', '', '0', '3', '2019-08-17', '2019-08-17');
INSERT INTO `t_menu` VALUES ('199', '190', '资源评论', '/resource/comment', '', '', '0', '4', '2019-08-17', '2019-08-17');

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `ROLE_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `ROLE_NAME` varchar(100) NOT NULL COMMENT '角色名称',
  `REMARK` varchar(100) DEFAULT NULL COMMENT '角色描述',
  `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
  `MODIFY_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`ROLE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色表';

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES ('1', '系统管理员', '系统管理员，拥有所有操作权限 ^_^', '2019-06-14 16:23:11', '2019-08-17 11:53:25');
INSERT INTO `t_role` VALUES ('2', '注册账户', '注册账户，拥有查看，新增权限（新增用户除外）和导出Excel权限', '2019-06-14 16:00:15', '2019-06-14 20:47:47');
INSERT INTO `t_role` VALUES ('77', 'Redis监控员', '负责Redis模块', '2019-06-14 20:49:22', null);
INSERT INTO `t_role` VALUES ('78', '系统监控员', '负责整个系统监控模块', '2019-06-14 20:50:07', null);
INSERT INTO `t_role` VALUES ('79', '跑批人员', '负责任务调度跑批模块', '2019-06-14 20:51:02', null);
INSERT INTO `t_role` VALUES ('80', '开发人员', '拥有代码生成模块的权限', '2019-06-14 20:51:26', null);

-- ----------------------------
-- Table structure for t_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_role_menu`;
CREATE TABLE `t_role_menu` (
  `ROLE_ID` bigint(20) NOT NULL COMMENT '角色ID',
  `MENU_ID` bigint(20) NOT NULL COMMENT '菜单/按钮ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色菜单关联表';

-- ----------------------------
-- Records of t_role_menu
-- ----------------------------
INSERT INTO `t_role_menu` VALUES ('2', '1');
INSERT INTO `t_role_menu` VALUES ('2', '3');
INSERT INTO `t_role_menu` VALUES ('2', '161');
INSERT INTO `t_role_menu` VALUES ('2', '4');
INSERT INTO `t_role_menu` VALUES ('2', '14');
INSERT INTO `t_role_menu` VALUES ('2', '162');
INSERT INTO `t_role_menu` VALUES ('2', '5');
INSERT INTO `t_role_menu` VALUES ('2', '17');
INSERT INTO `t_role_menu` VALUES ('2', '163');
INSERT INTO `t_role_menu` VALUES ('2', '6');
INSERT INTO `t_role_menu` VALUES ('2', '20');
INSERT INTO `t_role_menu` VALUES ('2', '164');
INSERT INTO `t_role_menu` VALUES ('2', '2');
INSERT INTO `t_role_menu` VALUES ('2', '8');
INSERT INTO `t_role_menu` VALUES ('2', '10');
INSERT INTO `t_role_menu` VALUES ('2', '170');
INSERT INTO `t_role_menu` VALUES ('2', '136');
INSERT INTO `t_role_menu` VALUES ('2', '172');
INSERT INTO `t_role_menu` VALUES ('2', '113');
INSERT INTO `t_role_menu` VALUES ('2', '114');
INSERT INTO `t_role_menu` VALUES ('2', '127');
INSERT INTO `t_role_menu` VALUES ('2', '128');
INSERT INTO `t_role_menu` VALUES ('2', '129');
INSERT INTO `t_role_menu` VALUES ('2', '130');
INSERT INTO `t_role_menu` VALUES ('2', '131');
INSERT INTO `t_role_menu` VALUES ('2', '101');
INSERT INTO `t_role_menu` VALUES ('2', '102');
INSERT INTO `t_role_menu` VALUES ('2', '173');
INSERT INTO `t_role_menu` VALUES ('2', '109');
INSERT INTO `t_role_menu` VALUES ('2', '174');
INSERT INTO `t_role_menu` VALUES ('2', '137');
INSERT INTO `t_role_menu` VALUES ('2', '138');
INSERT INTO `t_role_menu` VALUES ('2', '139');
INSERT INTO `t_role_menu` VALUES ('2', '115');
INSERT INTO `t_role_menu` VALUES ('2', '132');
INSERT INTO `t_role_menu` VALUES ('2', '133');
INSERT INTO `t_role_menu` VALUES ('2', '135');
INSERT INTO `t_role_menu` VALUES ('2', '134');
INSERT INTO `t_role_menu` VALUES ('2', '126');
INSERT INTO `t_role_menu` VALUES ('2', '159');
INSERT INTO `t_role_menu` VALUES ('2', '116');
INSERT INTO `t_role_menu` VALUES ('2', '117');
INSERT INTO `t_role_menu` VALUES ('2', '119');
INSERT INTO `t_role_menu` VALUES ('2', '120');
INSERT INTO `t_role_menu` VALUES ('2', '121');
INSERT INTO `t_role_menu` VALUES ('2', '122');
INSERT INTO `t_role_menu` VALUES ('2', '123');
INSERT INTO `t_role_menu` VALUES ('2', '118');
INSERT INTO `t_role_menu` VALUES ('2', '125');
INSERT INTO `t_role_menu` VALUES ('2', '167');
INSERT INTO `t_role_menu` VALUES ('2', '168');
INSERT INTO `t_role_menu` VALUES ('2', '169');
INSERT INTO `t_role_menu` VALUES ('77', '2');
INSERT INTO `t_role_menu` VALUES ('77', '113');
INSERT INTO `t_role_menu` VALUES ('77', '114');
INSERT INTO `t_role_menu` VALUES ('78', '2');
INSERT INTO `t_role_menu` VALUES ('78', '8');
INSERT INTO `t_role_menu` VALUES ('78', '23');
INSERT INTO `t_role_menu` VALUES ('78', '10');
INSERT INTO `t_role_menu` VALUES ('78', '24');
INSERT INTO `t_role_menu` VALUES ('78', '170');
INSERT INTO `t_role_menu` VALUES ('78', '136');
INSERT INTO `t_role_menu` VALUES ('78', '171');
INSERT INTO `t_role_menu` VALUES ('78', '172');
INSERT INTO `t_role_menu` VALUES ('78', '113');
INSERT INTO `t_role_menu` VALUES ('78', '114');
INSERT INTO `t_role_menu` VALUES ('78', '127');
INSERT INTO `t_role_menu` VALUES ('78', '128');
INSERT INTO `t_role_menu` VALUES ('78', '129');
INSERT INTO `t_role_menu` VALUES ('78', '130');
INSERT INTO `t_role_menu` VALUES ('78', '131');
INSERT INTO `t_role_menu` VALUES ('79', '101');
INSERT INTO `t_role_menu` VALUES ('79', '102');
INSERT INTO `t_role_menu` VALUES ('79', '103');
INSERT INTO `t_role_menu` VALUES ('79', '104');
INSERT INTO `t_role_menu` VALUES ('79', '105');
INSERT INTO `t_role_menu` VALUES ('79', '106');
INSERT INTO `t_role_menu` VALUES ('79', '107');
INSERT INTO `t_role_menu` VALUES ('79', '108');
INSERT INTO `t_role_menu` VALUES ('79', '173');
INSERT INTO `t_role_menu` VALUES ('79', '109');
INSERT INTO `t_role_menu` VALUES ('79', '110');
INSERT INTO `t_role_menu` VALUES ('79', '174');
INSERT INTO `t_role_menu` VALUES ('80', '137');
INSERT INTO `t_role_menu` VALUES ('80', '138');
INSERT INTO `t_role_menu` VALUES ('80', '165');
INSERT INTO `t_role_menu` VALUES ('80', '139');
INSERT INTO `t_role_menu` VALUES ('80', '166');
INSERT INTO `t_role_menu` VALUES ('1', '1');
INSERT INTO `t_role_menu` VALUES ('1', '3');
INSERT INTO `t_role_menu` VALUES ('1', '11');
INSERT INTO `t_role_menu` VALUES ('1', '12');
INSERT INTO `t_role_menu` VALUES ('1', '13');
INSERT INTO `t_role_menu` VALUES ('1', '4');
INSERT INTO `t_role_menu` VALUES ('1', '14');
INSERT INTO `t_role_menu` VALUES ('1', '15');
INSERT INTO `t_role_menu` VALUES ('1', '16');
INSERT INTO `t_role_menu` VALUES ('1', '5');
INSERT INTO `t_role_menu` VALUES ('1', '17');
INSERT INTO `t_role_menu` VALUES ('1', '18');
INSERT INTO `t_role_menu` VALUES ('1', '19');
INSERT INTO `t_role_menu` VALUES ('1', '6');
INSERT INTO `t_role_menu` VALUES ('1', '20');
INSERT INTO `t_role_menu` VALUES ('1', '21');
INSERT INTO `t_role_menu` VALUES ('1', '22');
INSERT INTO `t_role_menu` VALUES ('1', '192');
INSERT INTO `t_role_menu` VALUES ('1', '193');
INSERT INTO `t_role_menu` VALUES ('1', '183');
INSERT INTO `t_role_menu` VALUES ('1', '186');
INSERT INTO `t_role_menu` VALUES ('1', '190');
INSERT INTO `t_role_menu` VALUES ('1', '196');
INSERT INTO `t_role_menu` VALUES ('1', '195');
INSERT INTO `t_role_menu` VALUES ('1', '197');
INSERT INTO `t_role_menu` VALUES ('1', '199');
INSERT INTO `t_role_menu` VALUES ('1', '175');
INSERT INTO `t_role_menu` VALUES ('1', '177');
INSERT INTO `t_role_menu` VALUES ('1', '179');
INSERT INTO `t_role_menu` VALUES ('1', '180');
INSERT INTO `t_role_menu` VALUES ('1', '181');
INSERT INTO `t_role_menu` VALUES ('1', '101');
INSERT INTO `t_role_menu` VALUES ('1', '102');
INSERT INTO `t_role_menu` VALUES ('1', '103');
INSERT INTO `t_role_menu` VALUES ('1', '104');
INSERT INTO `t_role_menu` VALUES ('1', '105');
INSERT INTO `t_role_menu` VALUES ('1', '106');
INSERT INTO `t_role_menu` VALUES ('1', '107');
INSERT INTO `t_role_menu` VALUES ('1', '108');
INSERT INTO `t_role_menu` VALUES ('1', '173');
INSERT INTO `t_role_menu` VALUES ('1', '109');
INSERT INTO `t_role_menu` VALUES ('1', '110');
INSERT INTO `t_role_menu` VALUES ('1', '174');
INSERT INTO `t_role_menu` VALUES ('1', '2');
INSERT INTO `t_role_menu` VALUES ('1', '8');
INSERT INTO `t_role_menu` VALUES ('1', '23');
INSERT INTO `t_role_menu` VALUES ('1', '10');
INSERT INTO `t_role_menu` VALUES ('1', '24');
INSERT INTO `t_role_menu` VALUES ('1', '170');
INSERT INTO `t_role_menu` VALUES ('1', '136');
INSERT INTO `t_role_menu` VALUES ('1', '171');
INSERT INTO `t_role_menu` VALUES ('1', '172');
INSERT INTO `t_role_menu` VALUES ('1', '113');
INSERT INTO `t_role_menu` VALUES ('1', '114');
INSERT INTO `t_role_menu` VALUES ('1', '127');
INSERT INTO `t_role_menu` VALUES ('1', '128');
INSERT INTO `t_role_menu` VALUES ('1', '129');
INSERT INTO `t_role_menu` VALUES ('1', '130');
INSERT INTO `t_role_menu` VALUES ('1', '131');
INSERT INTO `t_role_menu` VALUES ('1', '137');
INSERT INTO `t_role_menu` VALUES ('1', '138');
INSERT INTO `t_role_menu` VALUES ('1', '165');
INSERT INTO `t_role_menu` VALUES ('1', '139');
INSERT INTO `t_role_menu` VALUES ('1', '166');

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `USER_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `USERNAME` varchar(50) NOT NULL COMMENT '用户名',
  `PASSWORD` varchar(128) NOT NULL COMMENT '密码',
  `DEPT_ID` bigint(20) DEFAULT NULL COMMENT '部门ID',
  `EMAIL` varchar(128) DEFAULT NULL COMMENT '邮箱',
  `MOBILE` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `STATUS` char(1) NOT NULL COMMENT '状态 0锁定 1有效',
  `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
  `MODIFY_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  `LAST_LOGIN_TIME` date DEFAULT NULL COMMENT '最近访问时间',
  `SSEX` char(1) DEFAULT NULL COMMENT '性别 0男 1女 2保密',
  `IS_TAB` char(1) DEFAULT NULL COMMENT '是否开启tab，0关闭 1开启',
  `THEME` varchar(10) DEFAULT NULL COMMENT '主题',
  `AVATAR` varchar(100) DEFAULT NULL COMMENT '头像',
  `DESCRIPTION` varchar(100) DEFAULT NULL COMMENT '描述',
  `SCHOOL_ID` int(11) DEFAULT NULL,
  `CLASS_ID` int(11) DEFAULT NULL,
  `CONTACT` varchar(50) DEFAULT NULL,
  `EMER_PHONE` varchar(11) DEFAULT NULL,
  `RECENT_LOCATION` varchar(11) DEFAULT NULL,
  PRIMARY KEY (`USER_ID`),
  KEY `CLASS_ID` (`CLASS_ID`),
  KEY `SCHOOL_ID` (`SCHOOL_ID`),
  CONSTRAINT `t_user_ibfk_1` FOREIGN KEY (`CLASS_ID`) REFERENCES `jcc_class_info` (`class_id`),
  CONSTRAINT `t_user_ibfk_2` FOREIGN KEY (`SCHOOL_ID`) REFERENCES `jcc_school_info` (`school_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户表';

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('1', 'MrBird', 'cb62ad1497597283961545d608f80241', '1', 'mrbird@qq.com', '17788888888', '1', '2019-06-14 20:39:22', '2019-06-14 20:44:42', '2019-08-17', '0', '1', 'white', 'cnrhVkzwxjPwAaCfPbdc.png', '我是帅比作者。', null, null, null, null, null);
INSERT INTO `t_user` VALUES ('2', 'Scott', '1d685729d113cfd03872f154939bee1c', '10', 'scott@gmail.com', '17722222222', '1', '2019-06-14 20:55:53', '2019-06-14 21:05:43', '2019-08-16', '0', '1', 'black', 'gaOngJwsRYRaVAuXXcmB.png', '我是scott。', null, null, null, null, null);
INSERT INTO `t_user` VALUES ('4', 'Micaela', '9f2daa2c7bed6870fcbb5b9a51d6300e', '10', 'Micaela@163.com', '17733333333', '1', '2019-06-14 21:10:13', '2019-06-14 21:11:26', '2019-06-14', '0', '0', 'white', '20180414165909.jpg', '我叫米克拉', null, null, null, null, null);
INSERT INTO `t_user` VALUES ('5', 'Jana', '176679b77b3c3e352bd3b30ddc81083e', '8', 'Jana@126.com', '17744444444', '1', '2019-06-14 21:12:16', '2019-06-14 21:12:52', '2019-06-14', '1', '1', 'white', '20180414165821.jpg', '大家好，我叫简娜', null, null, null, null, null);
INSERT INTO `t_user` VALUES ('7', 'Margot', '31379841b9f4bfde22b8b40471e9a6ce', '9', 'Margot@qq.com', '13444444444', '1', '2019-06-14 21:17:53', '2019-06-14 21:18:59', '2019-06-14', '1', '1', 'white', '20180414165834.jpg', '大家好我叫玛戈', null, null, null, null, null);

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role` (
  `USER_ID` bigint(20) NOT NULL COMMENT '用户ID',
  `ROLE_ID` bigint(20) NOT NULL COMMENT '角色ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户角色关联表';

-- ----------------------------
-- Records of t_user_role
-- ----------------------------
INSERT INTO `t_user_role` VALUES ('1', '1');
INSERT INTO `t_user_role` VALUES ('2', '2');
INSERT INTO `t_user_role` VALUES ('4', '78');
INSERT INTO `t_user_role` VALUES ('5', '79');
INSERT INTO `t_user_role` VALUES ('7', '78');
INSERT INTO `t_user_role` VALUES ('7', '79');
INSERT INTO `t_user_role` VALUES ('7', '80');


-- ----------------------------
-- Table structure for t_dict
-- ----------------------------
DROP TABLE IF EXISTS `t_dict`;
CREATE TABLE `t_dict` (
  `dict_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典ID',
  `k` varchar(30) NOT NULL COMMENT '键',
  `v` varchar(100) NOT NULL COMMENT '值',
  `field` varchar(100) NOT NULL COMMENT '字段名称',
  PRIMARY KEY (`dict_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of t_dict
-- ----------------------------
INSERT INTO `t_dict` VALUES (null, '1', '中班', 'grade');
INSERT INTO `t_dict` VALUES (null, '2', '大班', 'grade');
INSERT INTO `t_dict` VALUES (null, '3', '一年级', 'grade');
INSERT INTO `t_dict` VALUES (null, '4', '二年级', 'grade');
INSERT INTO `t_dict` VALUES (null, '5', '三年级', 'grade');
INSERT INTO `t_dict` VALUES (null, '6', '四年级', 'grade');
INSERT INTO `t_dict` VALUES (null, '7', '五年级', 'grade');
INSERT INTO `t_dict` VALUES (null, '8', '六年级', 'grade');
INSERT INTO `t_dict` VALUES (null, '9', '初一', 'grade');
INSERT INTO `t_dict` VALUES (null, '10', '初二', 'grade');
INSERT INTO `t_dict` VALUES (null, '11', '初三', 'grade');
INSERT INTO `t_dict` VALUES (null, '12', '高一', 'grade');
INSERT INTO `t_dict` VALUES (null, '13', '高二', 'grade');
INSERT INTO `t_dict` VALUES (null, '14', '高三', 'grade');
INSERT INTO `t_dict` VALUES (null, '1', '语文', 'subject');
INSERT INTO `t_dict` VALUES (null, '2', '数学', 'subject');
INSERT INTO `t_dict` VALUES (null, '3', '英语', 'subject');
INSERT INTO `t_dict` VALUES (null, '4', '物理', 'subject');
INSERT INTO `t_dict` VALUES (null, '5', '化学', 'subject');
INSERT INTO `t_dict` VALUES (null, '6', '生物', 'subject');
INSERT INTO `t_dict` VALUES (null, '7', '地理', 'subject');
INSERT INTO `t_dict` VALUES (null, '8', '政治', 'subject');
INSERT INTO `t_dict` VALUES (null, '9', '历史', 'subject');
INSERT INTO `t_dict` VALUES (null, 'avi', '视频文件', 'file_type');
INSERT INTO `t_dict` VALUES (null, 'doc', 'word文档', 'file_type');
INSERT INTO `t_dict` VALUES (null, 'pdf', 'pdf文档', 'file_type');
INSERT INTO `t_dict` VALUES (null, 'xls', 'excel表', 'file_type');
INSERT INTO `t_dict` VALUES (null, 'txt', '文本文档', 'file_type');
INSERT INTO `t_dict` VALUES (null, 'zip', '压缩包', 'file_type');
INSERT INTO `t_dict` VALUES (null, 'image', '图片', 'file_type');

-- ----------------------------
-- Table structure for r_category
-- ----------------------------
DROP TABLE IF EXISTS `r_category`;
CREATE TABLE `r_category` (
  `category_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '类别ID',
  `parent_id` bigint(20) NOT NULL COMMENT '上级类别ID',
  `category_name` varchar(50) NOT NULL COMMENT '类别名称',
  `icon` varchar(255) DEFAULT NULL COMMENT '图标',
  `order_num` bigint(20) DEFAULT NULL COMMENT '排序',
  `show_status` int(1) DEFAULT 1 COMMENT '显示状态：0->不显示；1->显示',
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='类别表';

-- ----------------------------
-- Records of r_category
-- ----------------------------
INSERT INTO `r_category` VALUES ('1', '0', '基础学科', '', '0', '1');
INSERT INTO `r_category` VALUES ('2', '0', '职业考试', '', '0', '1');
INSERT INTO `r_category` VALUES ('3', '0', '编程语言', '', '0', '1');
INSERT INTO `r_category` VALUES ('4', '0', '艺术特长', '', '0', '1');
INSERT INTO `r_category` VALUES ('5', '0', '课外资源', '', '0', '1');
INSERT INTO `r_category` VALUES ('11', '1', '名师课堂', '', '0', '1');
INSERT INTO `r_category` VALUES ('12', '1', '同步课程', '', '0', '1');
INSERT INTO `r_category` VALUES ('22', '2', '计算机类认证', '', '0', '1');
INSERT INTO `r_category` VALUES ('23', '2', '金融会计考试', '', '0', '1');
INSERT INTO `r_category` VALUES ('31', '3', 'Java', '', '0', '1');
INSERT INTO `r_category` VALUES ('32', '3', 'C', '', '0', '1');
INSERT INTO `r_category` VALUES ('33', '3', 'C++', '', '0', '1');
INSERT INTO `r_category` VALUES ('34', '3', 'Python', '', '0', '1');
INSERT INTO `r_category` VALUES ('41', '4', '美术', '', '0', '1');
INSERT INTO `r_category` VALUES ('42', '4', '音乐', '', '0', '1');
INSERT INTO `r_category` VALUES ('43', '4', '体育', '', '0', '1');
INSERT INTO `r_category` VALUES ('44', '4', '舞蹈', '', '0', '1');
INSERT INTO `r_category` VALUES ('51', '5', '小说', '', '0', '1');
INSERT INTO `r_category` VALUES ('52', '5', '电影', '', '0', '1');
INSERT INTO `r_category` VALUES ('53', '5', '旅游', '', '0', '1');

-- ----------------------------
-- Table structure for r_resource
-- ----------------------------
DROP TABLE IF EXISTS `r_resource`;
CREATE TABLE `r_resource` (
  `resource_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '资源ID',
  `resource_name` varchar(255) NOT NULL COMMENT '资源名称',
  `creator` varchar(50) NOT NULL COMMENT '创建人',
  `avatar` varchar(255) DEFAULT NULL COMMENT '创建人头像',
  `school` varchar(50) DEFAULT NULL COMMENT '学校',
  `grade` varchar(50) DEFAULT NULL COMMENT '年级',
  `subject` varchar(50) DEFAULT NULL COMMENT '科目',
  `category_id` bigint(20) DEFAULT NULL COMMENT '类别ID',
  `category_name` varchar(50) DEFAULT NULL COMMENT '类别名称',
  `file_type` varchar(30) DEFAULT NULL COMMENT '文件类型',
  `pic` varchar(255) DEFAULT NULL COMMENT '资源图片',
  `url` varchar(500) NOT NULL COMMENT '资源地址',
  `description` varchar(500) DEFAULT NULL COMMENT '资源介绍',
  `read_count` int(11) DEFAULT NULL COMMENT '阅读数',
  `comment_count` int(11) DEFAULT NULL COMMENT '评论数',
  `star` int(3) DEFAULT NULL COMMENT '评分：0->5',
  `order_num` bigint(20) DEFAULT NULL COMMENT '排序',
  `status` int(1) DEFAULT 0 COMMENT '审核状态：0->未审核；1->审核通过；2->审核不通过',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`resource_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='资源表';

-- ----------------------------
-- Records of r_resource
-- ----------------------------

-- ----------------------------
-- Table structure for r_subject
-- ----------------------------
DROP TABLE IF EXISTS `r_subject`;
CREATE TABLE `r_subject` (
  `subject_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '专题ID',
  `subject_name` varchar(255) NOT NULL COMMENT '专题名称',
  `creator` varchar(50) NOT NULL COMMENT '创建人',
  `avatar` varchar(255) DEFAULT NULL COMMENT '创建人头像',
  `description` varchar(1000) DEFAULT NULL COMMENT '专题描述',
  `category_id` bigint(20) DEFAULT NULL COMMENT '类别ID',
  `category_name` varchar(50) DEFAULT NULL COMMENT '类别名称',
  `read_count` int(11) DEFAULT NULL COMMENT '阅读数',
  `pic` varchar(255) DEFAULT NULL COMMENT '专题图片',
  `order_num` bigint(20) DEFAULT NULL COMMENT '排序',
  `show_status` int(1) DEFAULT NULL COMMENT '显示状态：0->不显示；1->显示',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`subject_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='专题表';

-- ----------------------------
-- Records of r_subject
-- ----------------------------

-- ----------------------------
-- Table structure for r_subject_resource
-- ----------------------------
DROP TABLE IF EXISTS `r_subject_resource`;
CREATE TABLE `r_subject_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `subject_id` bigint(20) DEFAULT NULL,
  `resource_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8 COMMENT='专题资源关系表';

-- ----------------------------
-- Table structure for r_comment
-- ----------------------------
DROP TABLE IF EXISTS `r_comment`;
CREATE TABLE `r_comment` (
  `comment_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `resource_id` bigint(20) NOT NULL COMMENT '资源ID',
  `resource_name` varchar(255) NOT NULL COMMENT '资源名称',
  `user_name` varchar(50) NOT NULL COMMENT '评论人',
  `user_ip` varchar(64) DEFAULT NULL COMMENT '评价的ip',
  `user_avatar` varchar(255) DEFAULT NULL COMMENT '评论人头像',
  `star` int(3) DEFAULT NULL COMMENT '评分：0->5',
  `content` varchar(1000) NOT NULL COMMENT '评论内容',
  `show_status` int(1) DEFAULT NULL COMMENT '显示状态：0->不显示；1->显示',
  `replay_count` int(11) DEFAULT NULL COMMENT '回复数量',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`comment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资源评价表';

-- ----------------------------
-- Table structure for r_comment_replay
-- ----------------------------
DROP TABLE IF EXISTS `r_comment_replay`;
CREATE TABLE `r_comment_replay` (
  `comment_replay_id` bigint(20) NOT NULL AUTO_INCREMENT  COMMENT '回复ID',
  `comment_id` bigint(20) DEFAULT NULL COMMENT '评论ID',
  `user_name` varchar(50) DEFAULT NULL COMMENT '回复人',
  `user_avatar` varchar(255) DEFAULT NULL COMMENT '回复人头像',
  `content` varchar(1000) DEFAULT NULL COMMENT '回复内容',
  `create_time` datetime DEFAULT NULL COMMENT '回复时间',
  PRIMARY KEY (`comment_replay_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资源评价回复表';