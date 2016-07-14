delete from inf_q_blob_triggers where trigger_name='doTime_expressTransOrder';
SET FOREIGN_KEY_CHECKS = 0; 
delete from inf_q_triggers where trigger_name='doTime_expressTransOrder';
SET FOREIGN_KEY_CHECKS = 1; 