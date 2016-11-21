ALTER TABLE fn_orgrpt_order_recharge   
  ADD COLUMN recharge_target_id BIGINT(20) DEFAULT 0  NOT NULL  COMMENT '冲抵对象id', 
  ADD  INDEX Idx_recharge_target_id (recharge_target_id);

