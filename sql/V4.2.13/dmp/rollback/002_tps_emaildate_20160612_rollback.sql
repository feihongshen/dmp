ALTER TABLE `express_ops_tps_flow_tmp` DROP COLUMN `sendweight`,DROP COLUMN `sendemaildate`;

ALTER TABLE `express_ops_tps_flow_tmp_sent` DROP COLUMN `sendweight`,DROP COLUMN `sendemaildate`;

DROP TABLE IF EXISTS `express_ops_tps_flow_tmp_mps`;

ALTER TABLE `express_ops_tps_flow_tmp` DROP COLUMN `weight`,DROP COLUMN `volume`;

ALTER TABLE `express_ops_tps_flow_tmp_sent` DROP COLUMN `weight`,DROP COLUMN `volume`;