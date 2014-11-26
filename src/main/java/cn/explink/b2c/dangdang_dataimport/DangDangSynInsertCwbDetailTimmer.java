package cn.explink.b2c.dangdang_dataimport;

import java.util.HashMap;
import java.util.List;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class DangDangSynInsertCwbDetailTimmer {
	private Logger logger = LoggerFactory.getLogger(DangDangSynInsertCwbDetailTimmer.class);

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
	DangDangDataSynService dangDangDataSynService;

	@Autowired
	CwbDAO cwbDAO;
	@Produce(uri = "jms:topic:addressmatch")
	ProducerTemplate addressmatch;
	@Autowired
	DataImportService dataImportService;

	/**
	 * 当当定时器，查询临时表，插入数据到detail表中。
	 */

	public long selectTempAndInsertToCwbDetail(int yhd_key) {
		DangDangDataSyn dangdang = dangDangDataSynService.getDangDang(yhd_key);
		int isOpenFlag = jointService.getStateForJoint(yhd_key);
		if (isOpenFlag == 0) {
			logger.info("未开启0当当0对接！yhd_key={},当前获取订单插入临时表-----", yhd_key);
			return -1;
		}
		List<CwbOrderDTO> cwbOrderList = dataImportDAO_B2c.getCwbOrderTempByKeys(dangdang.getCustomerid_baihuo() + "," + dangdang.getCustomerid_dangrida() + "," + dangdang.getCustomerid_tushu() + ","
				+ dangdang.getCustomerid_zhaoshang());
		if (cwbOrderList == null) {
			return 0;
		}
		if (cwbOrderList.size() == 0) {
			return 0;
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
			ImportSubList(dangdang, subList);
			k++;
		}
		return cwbOrderList.size();
	}

	@Transactional
	public void ImportSubList(DangDangDataSyn dangdang, List<CwbOrderDTO> cwbOrderList) {

		for (CwbOrderDTO cwbOrder : cwbOrderList) {
			try {
				ImportSignOrder(dangdang, cwbOrder);
			} catch (Exception e) {
				logger.error("定时器临时表插入或修改方法执行异常!cwb=" + cwbOrder.getCwb(), e);
			}
		}
	}

	private void ImportSignOrder(DangDangDataSyn dangdang, CwbOrderDTO cwbOrder) {
		CwbOrder order = cwbDAO.getCwbByCwb(cwbOrder.getCwb());

		if (order != null) { // 要合单子
			logger.warn("查询临时表-检测到0当当0有重复数据,已过滤!订单号：{},运单号:{}", cwbOrder.getCwb(), cwbOrder.getShipcwb());
		} else {
			User user = new User();
			user.setUserid(1);
			long warehouse_id = dangdang.getBranchid();
			long warehouseid = warehouse_id != 0 ? warehouse_id : dataImportService_B2c.getTempWarehouseIdForB2c(); // 获取虚拟库房
																													// Id,所有的B2C对接都会导入默认的虚拟库房里面，方便能够统计到。
			user.setBranchid(warehouseid);

			long ruleEmaildateHours = 0;
			if (String.valueOf(cwbOrder.getCustomerid()).equals(dangdang.getCustomerid_baihuo()) || String.valueOf(cwbOrder.getCustomerid()).equals(dangdang.getCustomerid_tushu())) {
				if (dangdang.getRuleEmaildateHours() > 0) {
					ruleEmaildateHours = dangdang.getRuleEmaildateHours();
				}
			}

			EmailDate ed = dataImportService.getOrCreateEmailDate_B2C(cwbOrder.getCustomerid(), 0, warehouseid, ruleEmaildateHours);
			cwbOrderService.insertCwbOrder(cwbOrder, cwbOrder.getCustomerid(), ed.getWarehouseid(), user, ed);

			emaildateDAO.editEditEmaildateForCwbcountAdd(ed.getEmaildateid());
			logger.info("0当当0定时器临时表插入detail表成功!cwb={},shipcwb={}", cwbOrder.getCwb(), cwbOrder.getShipcwb());

			try {
				if (cwbOrder.getExcelbranch() == null || cwbOrder.getExcelbranch().length() == 0) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("cwb", cwbOrder.getCwb());
					map.put("userid", "1");
					addressmatch.sendBodyAndHeaders(null, map);
				}
			} catch (Exception e) {
				logger.error("调用地址库异常", e);
				e.printStackTrace();
			}
		}
		dataImportDAO_B2c.update_CwbDetailTempByCwb(cwbOrder.getOpscwbid());
	}

}
