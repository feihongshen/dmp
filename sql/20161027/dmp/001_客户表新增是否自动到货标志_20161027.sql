ALTER TABLE `express_set_customer_info` 
ADD COLUMN `autoarrivalbranchflag` int(4) DEFAULT '0' NULL COMMENT '是否开启揽退单自动到货标志：0不开启,1开启';

