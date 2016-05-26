-- 修改表tpo_send_do_inf
ALTER TABLE tpo_send_do_inf
  ADD COLUMN operate_type TINYINT(4) DEFAULT 0  NOT NULL  COMMENT '0 新增，1 修改，-1 取消',
  ADD COLUMN state TINYINT(4) DEFAULT 1  NOT NULL  COMMENT '是否失效：0.失效，1.有效';
