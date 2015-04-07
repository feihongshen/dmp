/*
Navicat MySQL Data Transfer

Source Server         : wzy
Source Server Version : 50162
Source Host           : localhost:3306
Source Database       : pj_dmp

Target Server Type    : MYSQL
Target Server Version : 50162
File Encoding         : 65001

Date: 2015-04-07 10:08:03
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for dmp40_function
-- ----------------------------
DROP TABLE IF EXISTS `dmp40_function`;
CREATE TABLE `dmp40_function` (
  `ID` varchar(100) NOT NULL,
  `functionlevel` smallint(6) DEFAULT NULL,
  `functionname` varchar(100) DEFAULT NULL,
  `functionorder` varchar(100) DEFAULT NULL,
  `functionurl` varchar(100) DEFAULT NULL,
  `parentfunctionid` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dmp40_function
-- ----------------------------
INSERT INTO `dmp40_function` VALUES ('10', '0', '基础资料', '10', 'menu_item_jczl', null);
INSERT INTO `dmp40_function` VALUES ('1010', '1', '客户管理', '1010', '', '10');
INSERT INTO `dmp40_function` VALUES ('101010', '2', '客户基本信息', '101010', 'customer/list/1?', '1010');
INSERT INTO `dmp40_function` VALUES ('101020', '2', '仓库设置', '101020', 'customerwarehouses/list/1?', '1010');
INSERT INTO `dmp40_function` VALUES ('101030', '2', '订单导入模板', '101030', 'excelcolumn/list?', '1010');
INSERT INTO `dmp40_function` VALUES ('1020', '1', '承运商管理', '1020', '', '10');
INSERT INTO `dmp40_function` VALUES ('102010', '2', '承运商基本信息', '102010', 'common/list/1?', '1020');
INSERT INTO `dmp40_function` VALUES ('102020', '2', '承运商面单模板', '102020', 'commonmodel/tosetcoordinate?', '1020');
INSERT INTO `dmp40_function` VALUES ('1025', '1', '订单类型管理', '1025', 'cwbordertype/list/1?', '10');
INSERT INTO `dmp40_function` VALUES ('1030', '1', '车辆管理', '1030', 'truck/list/1?', '10');
INSERT INTO `dmp40_function` VALUES ('1040', '1', '常用语管理', '1040', 'reason/list/1?', '10');
INSERT INTO `dmp40_function` VALUES ('1045', '1', '中转原因匹配', '1045', 'transferReason/list/1?', '10');
INSERT INTO `dmp40_function` VALUES ('20', '0', '用户管理', '20', 'menu_item_yhgl', '');
INSERT INTO `dmp40_function` VALUES ('2010', '1', '机构管理', '2010', 'branch/list/1?', '20');
INSERT INTO `dmp40_function` VALUES ('2020', '1', '岗位管理', '2020', 'role/list?', '20');
INSERT INTO `dmp40_function` VALUES ('2030', '1', '用户管理', '2030', 'user/list/1?', '20');
INSERT INTO `dmp40_function` VALUES ('2040', '1', '区域权限设置', '2040', 'userBranchControl/list/1?', '20');
INSERT INTO `dmp40_function` VALUES ('2050', '1', '小件员管理', '2050', 'user/listbranch/1?', '20');
INSERT INTO `dmp40_function` VALUES ('30', '0', '系统设置', '30', 'menu_item_xtsz', '');
INSERT INTO `dmp40_function` VALUES ('3010', '1', '系统参数', '3010', 'systeminstall/list/1?', '30');
INSERT INTO `dmp40_function` VALUES ('3012', '1', '对接管理', '3012', 'jointManage/?', '30');
INSERT INTO `dmp40_function` VALUES ('3014', '1', '超额提示', '3014', 'exceedfee/list?', '30');
INSERT INTO `dmp40_function` VALUES ('3020', '1', '模板设置', '3020', '', '30');
INSERT INTO `dmp40_function` VALUES ('302010', '2', '订单导出模板', '302010', 'setexportcwb/list/1?', '3020');
INSERT INTO `dmp40_function` VALUES ('302020', '2', '默认导出模板', '302020', 'setdefaultexport/list?', '3020');
INSERT INTO `dmp40_function` VALUES ('302040', '2', '交接单模板', '302040', 'printtemplate/list/1?', '3020');
INSERT INTO `dmp40_function` VALUES ('3030', '1', '流程设置', '3030', '', '30');
INSERT INTO `dmp40_function` VALUES ('303010', '2', '货物流向设置', '303010', 'branchRouteControl/toNextStopPage?', '3030');
INSERT INTO `dmp40_function` VALUES ('303020', '2', '订单操作流程', '303020', 'cwbStateControl/list?', '3030');
INSERT INTO `dmp40_function` VALUES ('303030', '2', '订单状态流程', '303030', 'cwbAllStateControl/list?', '3030');
INSERT INTO `dmp40_function` VALUES ('40', '0', '订单处理', '40', 'menu_item_ddcl', '');
INSERT INTO `dmp40_function` VALUES ('4020', '1', '订单导入', '4020', 'dataimport/excelimportPage?', '40');
INSERT INTO `dmp40_function` VALUES ('4030', '1', '匹配管理', '4030', 'dataimport/excelimportPage?', '40');
INSERT INTO `dmp40_function` VALUES ('4040', '1', '生成运单号', '4040', 'dataimport/excelimportPage?', '40');
INSERT INTO `dmp40_function` VALUES ('4050', '1', '导入数据失效', '4050', 'dataimport/excelimportPage?', '40');
INSERT INTO `dmp40_function` VALUES ('50', '0', '分拨管理', '50', 'menu_item_fbgl', '');
INSERT INTO `dmp40_function` VALUES ('5010', '1', '库房操作', '5010', '', '50');
INSERT INTO `dmp40_function` VALUES ('501010', '2', '分拣库入库', '501010', 'PDA/intowarhouse?', '5010');
INSERT INTO `dmp40_function` VALUES ('501020', '2', '分拣库出库', '501020', 'PDA/exportwarhouse?', '5010');
INSERT INTO `dmp40_function` VALUES ('5020', '1', '库房打印', '5020', '', '50');
INSERT INTO `dmp40_function` VALUES ('502010', '2', '分拣出库打印', '502010', 'warehousegroupdetail/outlist/1?', '5020');
INSERT INTO `dmp40_function` VALUES ('5040', '1', '站点操作', '5040', '', '50');
INSERT INTO `dmp40_function` VALUES ('504010', '2', '站点到货', '504010', 'PDA/branchimortdetail?', '5040');
INSERT INTO `dmp40_function` VALUES ('504020', '2', '小件员领货', '504020', 'PDA/branchdeliverdetail?', '5040');
INSERT INTO `dmp40_function` VALUES ('504030', '2', '站点出站', '504030', 'PDA/branchexportwarhouse?', '5040');
INSERT INTO `dmp40_function` VALUES ('504040', '2', '中转出站', '504040', 'PDA/branchchangeexport?', '5040');
INSERT INTO `dmp40_function` VALUES ('504050', '2', '退货出站', '504050', 'PDA/branchbackexport?', '5040');
INSERT INTO `dmp40_function` VALUES ('504060', '2', '归班反馈', '504060', 'delivery/batchEditDeliveryState?', '5040');
INSERT INTO `dmp40_function` VALUES ('504070', '2', '归班审核', '504070', 'delivery/auditView?', '5040');
INSERT INTO `dmp40_function` VALUES ('5050', '1', '站点打印', '5050', '', '50');
INSERT INTO `dmp40_function` VALUES ('505010', '2', '领货打印', '505010', 'warehousegroup/deliverlist/1?', '5050');
INSERT INTO `dmp40_function` VALUES ('505020', '2', '中转出站打印', '505020', 'warehousegroupdetail/branchzhongzhuanoutlist/1?', '5050');
INSERT INTO `dmp40_function` VALUES ('505030', '2', '退货出站打印', '505030', 'warehousegroupdetail/returnlist/1?', '5050');
INSERT INTO `dmp40_function` VALUES ('60', '0', '客服管理', '60', 'menu_item_kfgl', '');
INSERT INTO `dmp40_function` VALUES ('6010', '1', '订单查询', '6010', 'order/orderQuery?', '60');
INSERT INTO `dmp40_function` VALUES ('6020', '1', '订单处理', '6020', '', '60');
INSERT INTO `dmp40_function` VALUES ('602010', '2', '订单失效', '602010', 'cwborder/losecwbBatch?', '6020');
INSERT INTO `dmp40_function` VALUES ('602020', '2', '订单审核', '602020', 'cwborder/toTuiHuo?', '6020');
INSERT INTO `dmp40_function` VALUES ('602030', '2', '订单修改申请', '602030', 'applyeditdeliverystate/toCreateApplyEditDeliverystate/1?', '6020');
INSERT INTO `dmp40_function` VALUES ('602040', '2', '订单修改受理', '602040', 'applyeditdeliverystate/tohandleApplyEditDeliverystateList/1?', '6020');
INSERT INTO `dmp40_function` VALUES ('602050', '2', '订单修改', '602050', 'editcwb/start?', '6020');
INSERT INTO `dmp40_function` VALUES ('6030', '1', '问题件处理', '6030', '', '60');
INSERT INTO `dmp40_function` VALUES ('603010', '2', '问题件类型', '603010', 'abnormalType/list/1?', '6030');
INSERT INTO `dmp40_function` VALUES ('603020', '2', '创建问题件', '603020', 'abnormalOrder/toCreateabnormal?', '6030');
INSERT INTO `dmp40_function` VALUES ('603030', '2', '问题件处理', '603030', 'abnormalOrder/toHandleabnormal/1?', '6030');
INSERT INTO `dmp40_function` VALUES ('6040', '1', '查询投诉', '6040', '', '60');
INSERT INTO `dmp40_function` VALUES ('604010', '2', '投诉处理', '604010', 'complaint/list/1?', '6040');
INSERT INTO `dmp40_function` VALUES ('6050', '1', '异常处理', '6050', '', '60');
INSERT INTO `dmp40_function` VALUES ('605010', '2', '异常订单处理', '605010', 'cwborder/toEnd?', '6050');
INSERT INTO `dmp40_function` VALUES ('6060', '1', '短信发送', '6060', '', '60');
INSERT INTO `dmp40_function` VALUES ('606010', '2', '短信群发', '606010', 'sms/viewrole?', '6060');
INSERT INTO `dmp40_function` VALUES ('606020', '2', '短信账户管理', '606020', 'smsconfigmodel/setsmsview?', '6060');
INSERT INTO `dmp40_function` VALUES ('606030', '2', '短信发送查询', '606030', 'sms/sendList?', '6060');
INSERT INTO `dmp40_function` VALUES ('70', '0', '运营监控', '70', 'menu_item_yyjk', '');
INSERT INTO `dmp40_function` VALUES ('7010', '1', '妥投率', '7010', '', '70');
INSERT INTO `dmp40_function` VALUES ('701010', '2', '妥投率条件', '701010', 'datastatistics/mobileDeliveryRateSetup?', '7010');
INSERT INTO `dmp40_function` VALUES ('701020', '2', '妥投率查询', '701020', 'datastatistics/deliveryRateList?', '7010');
INSERT INTO `dmp40_function` VALUES ('7020', '1', '库房日志', '7020', '', '70');
INSERT INTO `dmp40_function` VALUES ('702010', '2', '今日库房日志', '702010', 'warehouseLog/nowlog?', '7020');
INSERT INTO `dmp40_function` VALUES ('702020', '2', '历史库房日志', '702020', 'warehouseLog/historylog?', '7020');
INSERT INTO `dmp40_function` VALUES ('7030', '1', '站点日志', '7030', '', '70');
INSERT INTO `dmp40_function` VALUES ('703010', '2', '今日站点日志', '703010', 'logtoday/todayArrivalAjax?', '7030');
INSERT INTO `dmp40_function` VALUES ('703020', '2', '历史站点日志', '703020', 'logtoday/allhistorylog/1?', '7030');
INSERT INTO `dmp40_function` VALUES ('7040', '1', '超期监控', '7040', '', '70');
INSERT INTO `dmp40_function` VALUES ('704010', '2', '超期异常时效', '704010', 'ExceptionMonitor/system?', '7040');
INSERT INTO `dmp40_function` VALUES ('704020', '2', '超期异常监控', '704020', 'ExceptionMonitor/operationTimeOut?', '7040');
INSERT INTO `dmp40_function` VALUES ('7050', '1', '操作监控', '7050', '', '70');
INSERT INTO `dmp40_function` VALUES ('705010', '2', '扫描监控', '705010', 'cwborderPDA/controlForBranchImport/1?', '7050');
INSERT INTO `dmp40_function` VALUES ('705020', '2', '对接异常监控', '705020', 'b2cjointmonitor/list/1?', '7050');
INSERT INTO `dmp40_function` VALUES ('80', '0', '财务结算', '80', 'menu_item_cwjs', '');
INSERT INTO `dmp40_function` VALUES ('8010', '1', '预扣款结算', '8010', '', '80');
INSERT INTO `dmp40_function` VALUES ('801010', '2', '账户管理', '801010', 'accountdeductrecord/branchList/1?', '8010');
INSERT INTO `dmp40_function` VALUES ('801020', '2', '预扣款冲正', '801020', 'accountdeductrecord/detailList?', '8010');
INSERT INTO `dmp40_function` VALUES ('801030', '2', '账户信息', '801030', 'gztl/accountdeductrecord/accountBasic/1?', '8010');
INSERT INTO `dmp40_function` VALUES ('8020', '1', '客户返款结算', '8020', '', '80');
INSERT INTO `dmp40_function` VALUES ('802010', '2', '客户账单核对', '802010', '', '8020');
INSERT INTO `dmp40_function` VALUES ('802020', '2', '客户账单管理', '802020', '', '8020');
INSERT INTO `dmp40_function` VALUES ('802030', '2', '账单调整查询', '802030', '', '8020');
INSERT INTO `dmp40_function` VALUES ('8030', '1', '站内返款结算', '8030', '', '80');
INSERT INTO `dmp40_function` VALUES ('803010', '2', '账单生成策略', '803010', '', '8030');
INSERT INTO `dmp40_function` VALUES ('803020', '2', '站点账单生成', '803020', '', '8030');
INSERT INTO `dmp40_function` VALUES ('803030', '2', '站内账单管理', '803030', '', '8030');
INSERT INTO `dmp40_function` VALUES ('8040', '1', '报表统计', '8040', '', '80');
INSERT INTO `dmp40_function` VALUES ('804010', '2', '签收站点余额报表', '804010', '', '8040');
INSERT INTO `dmp40_function` VALUES ('804020', '2', '返款统计报表', '804020', '', '8040');
INSERT INTO `dmp40_function` VALUES ('804030', '2', '应付客户报表', '804030', '', '8040');
INSERT INTO `dmp40_function` VALUES ('804040', '2', '站点派件统计', '804040', '', '8040');
INSERT INTO `dmp40_function` VALUES ('90', '0', '高级查询', '90', 'menu_item_gjcx', '');
INSERT INTO `dmp40_function` VALUES ('9005', '1', '订单批量查询', '9005', 'batchselectcwb/list/1?', '90');
INSERT INTO `dmp40_function` VALUES ('9010', '1', '客户发货统计', '9010', 'datastatistics/fahuodata/1?', '90');
INSERT INTO `dmp40_function` VALUES ('9015', '1', '库房入库统计', '9015', 'datastatistics/intowarehousedata/1?', '90');
INSERT INTO `dmp40_function` VALUES ('9020', '1', '库房出库统计', '9020', 'datastatistics/outwarehousedata/1?', '90');
INSERT INTO `dmp40_function` VALUES ('9025', '1', '分站到货统计', '9025', 'datastatistics/daohuodata/1?', '90');
INSERT INTO `dmp40_function` VALUES ('9030', '1', '站点出站统计', '9030', 'datastatistics/zhandianchuzhanlist/1?', '90');
INSERT INTO `dmp40_function` VALUES ('9035', '1', '退货出站统计', '9035', 'datastatistics/tuihuochuzhanlist/1?', '90');
INSERT INTO `dmp40_function` VALUES ('9040', '1', '中转订单统计', '9040', 'datastatistics/zhongzhuandata/1?', '90');
INSERT INTO `dmp40_function` VALUES ('9043', '1', '小件员领货查询', '9043', 'delivery/searchDeliveryLead/1?', '90');
INSERT INTO `dmp40_function` VALUES ('9045', '1', '小件员工作量统计', '9045', 'deliverycash/list?', '90');
INSERT INTO `dmp40_function` VALUES ('9050', '1', '妥投订单汇总', '9050', 'datastatistics/tuotousearch/1?', '90');
INSERT INTO `dmp40_function` VALUES ('9055', '1', '滞留订单汇总', '9055', 'datastatistics/zhiliusearch/1?', '90');
INSERT INTO `dmp40_function` VALUES ('9060', '1', '拒收订单汇总', '9060', 'datastatistics/jushousearch/1?', '90');
INSERT INTO `dmp40_function` VALUES ('9065', '1', '退客户订单汇总', '9065', 'datastatistics/tuigonghuoshangsearch/1?', '90');
