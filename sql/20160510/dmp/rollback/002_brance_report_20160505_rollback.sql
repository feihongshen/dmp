ALTER TABLE `fn_brance_report`
DROP COLUMN `update_time`,
MODIFY COLUMN `create_time`  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间' ;

ALTER TABLE `fn_brance_report_adjust`
DROP COLUMN `update_time`,
MODIFY COLUMN `create_time`  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间' ;

ALTER TABLE `fn_brance_report_detail`
DROP COLUMN `update_time`,
MODIFY COLUMN `create_time`  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间' ;


