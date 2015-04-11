/*
Navicat MySQL Data Transfer

Source Server         : wzy
Source Server Version : 50162
Source Host           : localhost:3306
Source Database       : pj_dmp

Target Server Type    : MYSQL
Target Server Version : 50162
File Encoding         : 65001

Date: 2015-04-11 10:04:11
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
INSERT INTO `dmp40_function` VALUES ('10', '0', '基础资料', '10', 'menu_item_jczl', 'null');
INSERT INTO `dmp40_function` VALUES ('1010', '1', '客户管理', '1010', '', '10');
INSERT INTO `dmp40_function` VALUES ('101010', '2', '客户基本信息', '101010', 'customer/list/1?', '1010');
INSERT INTO `dmp40_function` VALUES ('101020', '2', '仓库设置', '101020', 'customerwarehouses/list/1?', '1010');
INSERT INTO `dmp40_function` VALUES ('101030', '2', '订单导入模板', '101030', 'excelcolumn/list?', '1010');
INSERT INTO `dmp40_function` VALUES ('1020', '1', '承运商管理', '1020', '', '10');
INSERT INTO `dmp40_function` VALUES ('102010', '2', '承运商基本信息', '102010', 'common/list/1?', '1020');
INSERT INTO `dmp40_function` VALUES ('102020', '2', '承运商面单模板', '102020', 'commonmodel/tosetcoordinate?', '1020');
INSERT INTO `dmp40_function` VALUES ('1025', '1', '订单类型管理', '1025', 'cwbordertype/list/1?', '10');
INSERT INTO `dmp40_function` VALUES ('1028', '1', '支付方式管理', '1028', '/payway/list/1?', '10');
INSERT INTO `dmp40_function` VALUES ('1030', '1', '车辆管理', '1030', 'truck/list/1?', '10');
INSERT INTO `dmp40_function` VALUES ('1040', '1', '常用语管理', '1040', 'reason/list/1?', '10');
INSERT INTO `dmp40_function` VALUES ('15', '0', '地址库', '15', 'menu_item_dzk', '');
INSERT INTO `dmp40_function` VALUES ('1510', '1', '登录地址库', '1510', '', '15');
INSERT INTO `dmp40_function` VALUES ('1550', '1', '配送员关联维护', '1550', 'addressdelivertostation/inittree?', '15');
INSERT INTO `dmp40_function` VALUES ('20', '0', '用户管理', '20', 'menu_item_yhgl', '');
INSERT INTO `dmp40_function` VALUES ('2010', '1', '机构管理', '2010', 'branch/list/1?', '20');
INSERT INTO `dmp40_function` VALUES ('2020', '1', '岗位管理', '2020', 'role/list?', '20');
INSERT INTO `dmp40_function` VALUES ('2030', '1', '人员管理', '2030', 'user/list/1?', '20');
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
INSERT INTO `dmp40_function` VALUES ('4030', '1', '匹配管理', '4030', 'dataimport/editBranch?', '40');
INSERT INTO `dmp40_function` VALUES ('4040', '1', '生成运单号', '4040', 'dataimport/reproducttranscwb?', '40');
INSERT INTO `dmp40_function` VALUES ('4050', '1', '导入数据失效', '4050', 'dataimport/datalose?', '40');
INSERT INTO `dmp40_function` VALUES ('50', '0', '分拨管理', '50', 'menu_item_fbgl', '');
INSERT INTO `dmp40_function` VALUES ('5010', '1', '库房操作', '5010', '', '50');
INSERT INTO `dmp40_function` VALUES ('501010', '2', '分拣库入库', '501010', 'PDA/intowarhouse?', '5010');
INSERT INTO `dmp40_function` VALUES ('501020', '2', '分拣库出库', '501020', 'PDA/exportwarhouse?', '5010');
INSERT INTO `dmp40_function` VALUES ('501025', '2', '中转库入库', '501025', 'PDA/changeintowarhouse?', '5010');
INSERT INTO `dmp40_function` VALUES ('501030', '2', '中转库出库', '501030', 'PDA/changeexportwarhouse?', '5010');
INSERT INTO `dmp40_function` VALUES ('501040', '2', '退货再投', '501045', 'PDA/exportwarhouse?', '5010');
INSERT INTO `dmp40_function` VALUES ('501045', '2', '退货库入库', '501040', 'PDA/backimport?', '5010');
INSERT INTO `dmp40_function` VALUES ('501050', '2', '退客户出库', '501050', 'PDA/backtocustomer?', '5010');
INSERT INTO `dmp40_function` VALUES ('501055', '2', '退客户拒收返库', '501055', 'PDA/customerrefuseback?', '5010');
INSERT INTO `dmp40_function` VALUES ('501060', '2', '返单入库', '501060', 'returnCwbs/returnCwbsintowarhouse?', '5010');
INSERT INTO `dmp40_function` VALUES ('501065', '2', '异常匹配处理', '501065', 'meh/matchexceptionhandle?', '5010');
INSERT INTO `dmp40_function` VALUES ('501070', '2', '到错货处理', '501070', 'cwborder/daocuohuolist/1', '5010');
INSERT INTO `dmp40_function` VALUES ('5020', '1', '库房打印', '5020', '', '50');
INSERT INTO `dmp40_function` VALUES ('502010', '2', '分拣出库打印', '502010', 'warehousegroupdetail/outlist/1?', '5020');
INSERT INTO `dmp40_function` VALUES ('502020', '2', '上门退订单打印', '502020', 'cwborder/selectforkfsmt/1?', '5020');
INSERT INTO `dmp40_function` VALUES ('5040', '1', '站点操作', '5040', '', '50');
INSERT INTO `dmp40_function` VALUES ('504010', '2', '站点到货', '504010', 'PDA/branchimortdetail?', '5040');
INSERT INTO `dmp40_function` VALUES ('504020', '2', '小件员领货', '504020', 'PDA/branchdeliverdetail?', '5040');
INSERT INTO `dmp40_function` VALUES ('504030', '2', '站点出站', '504030', 'PDA/branchexportwarhouse?', '5040');
INSERT INTO `dmp40_function` VALUES ('504035', '2', '归班反馈', '504035', 'delivery/auditView?', '5040');
INSERT INTO `dmp40_function` VALUES ('504040', '2', '中转出站', '504040', 'PDA/branchchangeexport?', '5040');
INSERT INTO `dmp40_function` VALUES ('504050', '2', '退货出站', '504050', 'PDA/branchbackexport?', '5040');
INSERT INTO `dmp40_function` VALUES ('504065', '2', '返单出站', '504065', 'returnCwbs/returnCwbsbackexport?', '5040');
INSERT INTO `dmp40_function` VALUES ('504070', '2', '上门退订单分派', '504070', 'smt/smtorderdispatch?', '5040');
INSERT INTO `dmp40_function` VALUES ('504075', '2', '部分退货反馈', '504075', 'orderpartgoodsreturn/ordergoodslist/1?', '5040');
INSERT INTO `dmp40_function` VALUES ('5050', '1', '站点打印', '5050', '', '50');
INSERT INTO `dmp40_function` VALUES ('505010', '2', '领货打印', '505010', 'warehousegroup/deliverlist/1?', '5050');
INSERT INTO `dmp40_function` VALUES ('505020', '2', '中转出站打印', '505020', 'warehousegroupdetail/branchzhongzhuanoutlist/1?', '5050');
INSERT INTO `dmp40_function` VALUES ('505030', '2', '退货出站打印', '505030', 'warehousegroupdetail/returnlist/1?', '5050');
INSERT INTO `dmp40_function` VALUES ('60', '0', '客服管理', '60', 'menu_item_kfgl', '');
INSERT INTO `dmp40_function` VALUES ('6010', '1', '订单查询', '6010', 'order/orderQuery?', '60');
INSERT INTO `dmp40_function` VALUES ('6020', '1', '订单处理', '6020', '', '60');
INSERT INTO `dmp40_function` VALUES ('602010', '2', '订单失效', '602010', 'cwborder/losecwbBatch?', '6020');
INSERT INTO `dmp40_function` VALUES ('602015', '2', '订单拦截', '602015', 'cwborder/toTuiHuo?', '6020');
INSERT INTO `dmp40_function` VALUES ('602020', '2', '订单操作审核', '602020', 'cwborder/toTuiHuo1?', '6020');
INSERT INTO `dmp40_function` VALUES ('6025', '1', '订单修改', '6025', '', '60');
INSERT INTO `dmp40_function` VALUES ('602530', '2', '订单修改申请', '602530', 'applyeditdeliverystate/toCreateApplyEditDeliverystate/1?', '6025');
INSERT INTO `dmp40_function` VALUES ('602540', '2', '订单修改受理', '602540', 'applyeditdeliverystate/tohandleApplyEditDeliverystateList/1?', '6025');
INSERT INTO `dmp40_function` VALUES ('602550', '2', '订单状态修改', '602550', 'editcwb/start?', '6025');
INSERT INTO `dmp40_function` VALUES ('602560', '2', '订单信息修改', '602560', 'editcwb/editCwbInfo?', '6025');
INSERT INTO `dmp40_function` VALUES ('6030', '1', '问题件', '6030', '', '60');
INSERT INTO `dmp40_function` VALUES ('603010', '2', '问题件类型', '603010', 'abnormalType/list/1?', '6030');
INSERT INTO `dmp40_function` VALUES ('603020', '2', '创建问题件', '603020', 'abnormalOrder/toCreateabnormal?', '6030');
INSERT INTO `dmp40_function` VALUES ('603030', '2', '问题件处理', '603030', 'abnormalOrder/toHandleabnormal/1?', '6030');
INSERT INTO `dmp40_function` VALUES ('6040', '1', '查询投诉', '6040', '', '60');
INSERT INTO `dmp40_function` VALUES ('604010', '2', '投诉受理', '604010', 'order/complaintlist/1?', '6040');
INSERT INTO `dmp40_function` VALUES ('604020', '2', '催单投诉', '604020', 'complaint/cuijiantousufozhanzhang/1?', '6040');
INSERT INTO `dmp40_function` VALUES ('604030', '2', '投诉处理', '604030', 'complaint/list/1?', '6040');
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
INSERT INTO `dmp40_function` VALUES ('702010', '2', '今日分拣库日志', '702010', 'warehouseLog/nowlog?', '7020');
INSERT INTO `dmp40_function` VALUES ('702020', '2', '历史分拣库日志', '702020', 'warehouseLog/historylog?', '7020');
INSERT INTO `dmp40_function` VALUES ('7030', '1', '站点日志', '7030', '', '70');
INSERT INTO `dmp40_function` VALUES ('703010', '2', '今日站点日志', '703010', 'logtoday/todayArrivalAllAjax?', '7030');
INSERT INTO `dmp40_function` VALUES ('703020', '2', '历史站点日志', '703020', 'logtoday/allhistorylog/1?', '7030');
INSERT INTO `dmp40_function` VALUES ('7040', '1', '超期监控', '7040', '', '70');
INSERT INTO `dmp40_function` VALUES ('704010', '2', '超期异常时效', '704010', 'ExceptionMonitor/system?', '7040');
INSERT INTO `dmp40_function` VALUES ('704020', '2', '超期异常监控', '704020', 'ExceptionMonitor/operationTimeOut?', '7040');
INSERT INTO `dmp40_function` VALUES ('7050', '1', '操作监控', '7050', '', '70');
INSERT INTO `dmp40_function` VALUES ('705010', '2', '扫描监控', '705010', 'cwborderPDA/controlForBranchImport/1?', '7050');
INSERT INTO `dmp40_function` VALUES ('705020', '2', '对接异常监控', '705020', 'b2cjointmonitor/list/1?', '7050');
INSERT INTO `dmp40_function` VALUES ('80', '0', '财务结算', '80', 'menu_item_cwjs', '');
INSERT INTO `dmp40_function` VALUES ('8020', '1', '客户返款结算', '8020', '', '80');
INSERT INTO `dmp40_function` VALUES ('802010', '2', '客户账单核对', '802010', '', '8020');
INSERT INTO `dmp40_function` VALUES ('802020', '2', '客户账单管理', '802020', '', '8020');
INSERT INTO `dmp40_function` VALUES ('802030', '2', '账单调整查询', '802030', '', '8020');
INSERT INTO `dmp40_function` VALUES ('802040', '2', '应付客户报表', '802040', '', '8020');
INSERT INTO `dmp40_function` VALUES ('8030', '1', '站内返款结算', '8030', '', '80');
INSERT INTO `dmp40_function` VALUES ('803010', '2', '账单生成策略', '803010', '', '8030');
INSERT INTO `dmp40_function` VALUES ('803020', '2', '站点账单生成', '803020', '', '8030');
INSERT INTO `dmp40_function` VALUES ('803030', '2', '站内账单管理', '803030', '', '8030');
INSERT INTO `dmp40_function` VALUES ('803040', '2', '签收站点余额报表', '803040', '', '8030');
INSERT INTO `dmp40_function` VALUES ('8035', '1', '站内扣款结算', '8035', '', '80');
INSERT INTO `dmp40_function` VALUES ('803510', '2', '账户管理', '803510', 'accountdeductrecord/branchList/1?', '8035');
INSERT INTO `dmp40_function` VALUES ('803520', '2', '预扣款冲正', '803520', 'accountdeductrecord/detailList?', '8035');
INSERT INTO `dmp40_function` VALUES ('803530', '2', '账户信息', '803530', 'gztl/accountdeductrecord/accountBasic/1?', '8035');
INSERT INTO `dmp40_function` VALUES ('803540', '2', '本站账户信息', '803540', 'accountdeductrecord/accountBasic/1?', '8035');
INSERT INTO `dmp40_function` VALUES ('8040', '1', '上门退运费结算', '8040', '', '80');
INSERT INTO `dmp40_function` VALUES ('804005', '2', '时效设置', '804005', 'time_effective/?', '8040');
INSERT INTO `dmp40_function` VALUES ('804010', '2', '运费交款', '804010', 'accountcwbfaresubmit/accountfaresubmitlist/1?', '8040');
INSERT INTO `dmp40_function` VALUES ('804020', '2', '运费审核', '804020', 'accountcwbfaredetailVerify/accountfarelist/1?', '8040');
INSERT INTO `dmp40_function` VALUES ('804050', '2', '站点运费统计', '804050', 'smtfaresettle/station/1?', '8040');
INSERT INTO `dmp40_function` VALUES ('804055', '2', '小件员运费统计', '804055', 'smtfaresettle/deliver/1?', '8040');
INSERT INTO `dmp40_function` VALUES ('8050', '1', '财务综合报表', '8050', '', '80');
INSERT INTO `dmp40_function` VALUES ('805020', '2', '返款统计报表', '805020', '', '8050');
INSERT INTO `dmp40_function` VALUES ('90', '0', '高级查询', '90', 'menu_item_gjcx', '');
INSERT INTO `dmp40_function` VALUES ('9005', '1', '订单批量查询', '9005', 'batchselectcwb/list/1?', '90');
INSERT INTO `dmp40_function` VALUES ('9010', '1', '库房数据查询', '9010', '', '90');
INSERT INTO `dmp40_function` VALUES ('901010', '2', '客户发货统计', '901010', 'datastatistics/fahuodata/1?', '9010');
INSERT INTO `dmp40_function` VALUES ('901020', '2', '分拣库入库统计', '901020', 'datastatistics/intowarehousedata/1?', '9010');
INSERT INTO `dmp40_function` VALUES ('901030', '2', '分拣库出库统计', '901030', 'datastatistics/outwarehousedata/1?', '9010');
INSERT INTO `dmp40_function` VALUES ('901040', '2', '退货库入库统计', '901040', 'datastatistics/tuihuozhanrukulist/1?', '9010');
INSERT INTO `dmp40_function` VALUES ('901050', '2', '退货库出库统计', '901050', 'datastatistics/tuihuochuzhanlist/1?', '9010');
INSERT INTO `dmp40_function` VALUES ('9020', '1', '站点数据查询', '9020', '', '90');
INSERT INTO `dmp40_function` VALUES ('902010', '2', '分站到货统计', '902010', 'datastatistics/daohuodata/1?', '9020');
INSERT INTO `dmp40_function` VALUES ('902020', '2', '站点出站统计', '902020', 'datastatistics/zhandianchuzhanlist/1?', '9020');
INSERT INTO `dmp40_function` VALUES ('902040', '2', '中转订单统计', '902040', 'datastatistics/zhongzhuandata/1?', '9020');
INSERT INTO `dmp40_function` VALUES ('9030', '1', '订单数据汇总', '9030', '', '90');
INSERT INTO `dmp40_function` VALUES ('903010', '2', '妥投订单汇总', '903010', 'datastatistics/tuotousearch/1?', '9030');
INSERT INTO `dmp40_function` VALUES ('903020', '2', '滞留订单汇总', '903020', 'datastatistics/zhiliusearch/1?', '9030');
INSERT INTO `dmp40_function` VALUES ('903030', '2', '拒收订单汇总', '903030', 'datastatistics/jushousearch/1?', '9030');
INSERT INTO `dmp40_function` VALUES ('903040', '2', '退客户订单汇总', '903040', 'datastatistics/tuigonghuoshangsearch/1?', '9030');
INSERT INTO `dmp40_function` VALUES ('903050', '2', '上门退揽收汇总表', '903050', 'overdueexmo/1?', '9030');
INSERT INTO `dmp40_function` VALUES ('9040', '1', '小件员查询', '9040', '', '90');
INSERT INTO `dmp40_function` VALUES ('904010', '2', '小件员领货查询', '904010', 'delivery/searchDeliveryLead/1?', '9040');
INSERT INTO `dmp40_function` VALUES ('904020', '2', '小件员工作量统计', '904020', 'deliverycash/list?', '9040');
