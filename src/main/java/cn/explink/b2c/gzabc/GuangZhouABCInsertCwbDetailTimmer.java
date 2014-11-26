package cn.explink.b2c.gzabc;

import java.util.HashMap;
import java.util.List;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.hzabc.HangZhouABC;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JointService;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.EmailDateDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.EmailDate;
import cn.explink.domain.User;
import cn.explink.service.CwbOrderService;
import cn.explink.service.DataImportService;

@Service
public class GuangZhouABCInsertCwbDetailTimmer {
	private Logger logger = LoggerFactory.getLogger(GuangZhouABCInsertCwbDetailTimmer.class);

	@Autowired
	EmailDateDAO emaildateDAO;
	@Autowired
	JointService jointService;
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	DataImportService_B2c dataImportService_B2c;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	GuangZhouABCService guangZhouABCService;

	@Produce(uri = "jms:topic:addressmatch")
	ProducerTemplate addressmatch;
	@Autowired
	DataImportService dataImportService;

	/**
	 * ABC定时器，查询临时表，插入数据到detail表中。
	 */

	// public void selectTempAndInsertToCwbDetail(int key){
	// try {
	// GuangZhouABC gzabc=guangZhouABCService.getGuangZhougABC(key);
	// int isOpenFlag=jointService.getStateForJoint(key);
	// if (isOpenFlag == 0) {
	// logger.info("未开启0广州ABC0对接！yhd_key={},当前获取订单插入临时表-----",key);
	// return;
	// }
	// List<CwbOrderDTO>
	// cwbOrderList=dataImportDAO_B2c.getCwbOrderTempByKeys(gzabc.getCustomerids());
	//
	// if(cwbOrderList==null||cwbOrderList.size()==0){
	// logger.info("当前0广州ABC0没有待插入detail表的数据...");
	// return;
	// }
	// for(CwbOrderDTO cwbOrder:cwbOrderList){
	// CwbOrder order=cwbDAO.getCwbByCwb(cwbOrder.getCwb());
	// if (order!=null) { // 要合单子
	// logger.warn("查询临时表-检测到0广州ABC0有重复数据,已过滤!订单号：{},运单号:{}",cwbOrder.getCwb(),cwbOrder.getShipcwb());
	// }else{
	// //insert
	// User user=new User();
	// user.setUserid(1);
	// long warehouse_id=gzabc.getExportbranchid();
	// long warehouseid
	// =warehouse_id!=0?warehouse_id:dataImportService_B2c.getTempWarehouseIdForB2c();
	// //获取虚拟库房 Id,所有的B2C对接都会导入默认的虚拟库房里面，方便能够统计到。
	// user.setBranchid(warehouseid);
	// EmailDate ed =
	// dataImportService.getOrCreateEmailDate(cwbOrder.getCustomerid(),
	// 0,warehouseid);
	//
	// emaildateDAO.editEditEmaildateForCwbcountAdd(ed.getEmaildateid());
	//
	// cwbOrderService.insertCwbOrder(cwbOrder, cwbOrder.getCustomerid(),
	// ed.getWarehouseid(), user, ed);
	// logger.info("定时器临时表插入detail表成功!cwb={},shipcwb={}",cwbOrder.getCwb(),cwbOrder.getShipcwb());
	//
	// if(cwbOrder.getExcelbranch()==null||cwbOrder.getExcelbranch().length()==0){
	// HashMap<String, Object> map=new HashMap<String, Object>();
	// map.put("cwb", cwbOrder.getCwb());
	// map.put("userid", "1");
	// addressmatch.sendBodyAndHeaders(null, map);
	// }
	//
	// }
	// dataImportDAO_B2c.update_CwbDetailTempByCwb(cwbOrder.getOpscwbid());
	// }
	// } catch (Exception e) {
	// logger.error("0广州ABC0定时器临时表插入或修改方法执行异常!异常原因:"+e);
	// e.printStackTrace();
	// }
	// }

	public long selectTempAndInsertToCwbDetail(int key) {
		GuangZhouABC gzabc = guangZhouABCService.getGuangZhougABC(key);
		int isOpenFlag = jointService.getStateForJoint(key);
		if (isOpenFlag == 0) {
			logger.info("未开启0广州ABC0对接！yhd_key={},当前获取订单插入临时表-----", key);
			return -1;
		}
		List<CwbOrderDTO> cwbOrderList = dataImportDAO_B2c.getCwbOrderTempByKeys(gzabc.getCustomerids());
		if (cwbOrderList == null) {
			return -1;
		}
		if (cwbOrderList.size() == 0) {
			return -1;
		}
		int k = 1;
		int batch = 50;
		while (true) {
			int fromIndex = (k - 1) * batch;
			if (fromIndex >= cwbOrderList.size()) {
				break;
			}
			int toIdx = k * batch;
			if (k * batch > cwbOrderList.size()) {
				toIdx = cwbOrderList.size();
			}
			List<CwbOrderDTO> subList = cwbOrderList.subList(fromIndex, toIdx);
			ImportSubList(gzabc, subList);
			k++;
		}
		return cwbOrderList.size();
	}

	@Transactional
	public void ImportSubList(GuangZhouABC gzabc, List<CwbOrderDTO> cwbOrderList) {

		for (CwbOrderDTO cwbOrder : cwbOrderList) {
			try {
				ImportSignOrder(gzabc, cwbOrder);
			} catch (Exception e) {
				logger.error("定时器临时表插入或修改方法执行异常!cwb=" + cwbOrder.getCwb(), e);
			}
		}
	}

	private void ImportSignOrder(GuangZhouABC gzabc, CwbOrderDTO cwbOrder) {
		CwbOrder order = cwbDAO.getCwbByCwb(cwbOrder.getCwb());
		if (order != null) { // 要合单子
			logger.warn("查询临时表-检测到有重复数据,已过滤!订单号：{},运单号:{}", cwbOrder.getCwb(), cwbOrder.getShipcwb());
		} else {
			User user = new User();
			user.setUserid(1);
			long warehouse_id = gzabc.getExportbranchid();
			long warehouseid = warehouse_id != 0 ? warehouse_id : dataImportService_B2c.getTempWarehouseIdForB2c(); // 获取虚拟库房
																													// Id,所有的B2C对接都会导入默认的虚拟库房里面，方便能够统计到。
			user.setBranchid(warehouseid);
			EmailDate ed = dataImportService.getOrCreateEmailDate(cwbOrder.getCustomerid(), 0, warehouseid);

			emaildateDAO.editEditEmaildateForCwbcountAdd(ed.getEmaildateid());

			cwbOrderService.insertCwbOrder(cwbOrder, cwbOrder.getCustomerid(), ed.getWarehouseid(), user, ed);
			logger.info("定时器临时表插入detail表成功!cwb={},shipcwb={}", cwbOrder.getCwb(), cwbOrder.getShipcwb());

			if (cwbOrder.getExcelbranch() == null || cwbOrder.getExcelbranch().length() == 0) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("cwb", cwbOrder.getCwb());
				map.put("userid", "1");
				addressmatch.sendBodyAndHeaders(null, map);
			}

		}
		dataImportDAO_B2c.update_CwbDetailTempByCwb(cwbOrder.getOpscwbid());
	}

}
