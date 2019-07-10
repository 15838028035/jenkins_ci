CREATE DATABASE /*!32312 IF NOT EXISTS*/`jenkins_ci` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `jenkins_ci`;


SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for UPM_USER
-- ----------------------------
DROP TABLE IF EXISTS `jenkins_job`;
CREATE TABLE `jenkins_job` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `job_name` varchar(255) DEFAULT NULL COMMENT 'job名称',
  `GIT_URL` varchar(255) DEFAULT NULL COMMENT 'git url地址',
  `GIT_BRANCH` varchar(255) DEFAULT NULL COMMENT '分支名称',
  `GIT_CREDIT` varchar(255) DEFAULT NULL COMMENT 'jenkins凭据参数',
  `IS_USE_GIT_TAG` boolean DEFAULT false COMMENT '是否使用git tag',
  `IS_RUN_SONAR` boolean DEFAULT false COMMENT '是否执行sonar扫描',
  `IS_RUN_SONAR_HTML` boolean DEFAULT false COMMENT '是否执行sonar html扫描',
  `IS_GEN_DOCKER_IMG` boolean DEFAULT false COMMENT '是否执行docker镜像',
  
  `IS_DEPLOY_NEXUS` boolean DEFAULT false COMMENT '是否发布到nexus中',
  `MODEL_NAMES` varchar(255) DEFAULT NULL COMMENT '镜像模块名称',
  `IS_SEND_EMAIL` boolean DEFAULT false COMMENT '是否发送邮件通知',
  `IS_MAVEN_DEBUG` boolean DEFAULT false COMMENT '是否启用maven调试，默认值false',
  
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_user_name` varchar(255) DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_user_name` varchar(255) DEFAULT NULL COMMENT '更新人姓名',
  `update_time` datetime DEFAULT NULL  COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='jenkins任务信息';


