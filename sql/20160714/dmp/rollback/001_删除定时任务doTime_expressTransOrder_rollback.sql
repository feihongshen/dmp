INSERT INTO `inf_q_triggers` (`trigger_name`, `trigger_group`, `job_name`, `job_group`, `is_volatile`, `description`, `next_fire_time`, `prev_fire_time`, `priority`, `trigger_state`, `trigger_type`, `start_time`, `end_time`, `calendar_name`, `misfire_instr`, `job_data`) VALUES ('doTime_expressTransOrder', 'DEFAULT', 'getPjwlExpressTransNoTask', 'DEFAULT', '0', NULL, '1468472400000', '1468472100000', '5', 'WAITING', 'BLOB', '1456928935000', '0', NULL, '0', '');
insert into `inf_q_blob_triggers` (`trigger_name`, `trigger_group`, `blob_data`) values('doTime_expressTransOrder','DEFAULT','');

