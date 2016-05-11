drop table `fn_station_sign_order_details_snapshot_express`;

ALTER TABLE `fn_station_sign_order_details_snapshot`
	DROP COLUMN `instationid`,
	DROP COLUMN `packagefee`,
	DROP COLUMN `insuredfee`,
	DROP COLUMN `totalfee`,
	DROP COLUMN `paymethod`,
	DROP COLUMN `collectorid`,
	DROP COLUMN `instationdatetime`,
	DROP INDEX `order_snapshot_deliveryid_idx`;