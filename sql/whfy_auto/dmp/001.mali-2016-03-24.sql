-- 机构管理
ALTER TABLE `express_set_branch` ADD COLUMN `outputno` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT '自动分拣线的出货口' AFTER `branchemail`;

-- 上货口表
CREATE TABLE `express_set_entrance` (
`entranceno`  varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT '上货口编号' ,
`entranceip`  varchar(19) NOT NULL COMMENT '上货口ip' ,
`enable` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否启用，0：启用，1：停用'
);

-- 系统参数
INSERT INTO `express_set_system_install` ( `name`, `value`, `chinesename`) VALUES ( 'AutoAllocating', '0', '自动分拨开关（0：关闭，1：开启）');