package cn.explink.b2c.dpfoss;

import java.util.HashMap;
import java.util.List;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JointService;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.EmailDateDAO;
import cn.explink.dao.MqExceptionDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.EmailDate;
import cn.explink.domain.MqExceptionBuilder;
import cn.explink.domain.User;
import cn.explink.service.CwbOrderService;
import cn.explink.service.DataImportService;

@Service
public class DpfossInsertCwbDetailTimmer {
	private Logger logger = LoggerFactory.getLogger(DpfossInsertCwbDetailTimmer.class);

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
	DpfossService dpfossService;

	@Produce(uri = "jms:topic:addressmatch")
	ProducerTemplate addressmatch;
	@Autowired
	DataImportService dataImportService;
	
	@Autowired
	private MqExceptionDAO mqExceptionDAO;

	/**
	 * 定时器，查询临时表，插入数据到detail表中。
	 */
	public void selectTempAndInsertToCwbDetailTask() {
		for (B2cEnum enums : B2cEnum.values()) { // 遍历唯品会enum，可能有多个枚举
			if (enums.getMethod().contains("DPFoss")) {
				selectTempAndInsertToCwbDetail(enums.getKey());
				logger.info("执行了德邦物流查询临时表的定时器!");
			}
		}
	}

	@Transactional
	public void ImportSubList(String customerid, long tmallWarehouseId, List<CwbOrderDTO> cwbOrderList) {

		for (CwbOrderDTO cwbOrder : cwbOrderList) {
			try {
				ImportSignOrder(customerid, tmallWarehouseId, cwbOrder);
			} catch (Exception e) {
				logger.error("定时器临时表插入或修改方法执行异常!customerid=" + customerid, e);
			}
		}
	}

	public void selectTempAndInsertToCwbDetail(int key) {

		try {
			Dpfoss dp = dpfossService.getDpfoss(key);
			int isOpenFlag = jointService.getStateForJoint(key);
			if (isOpenFlag == 0) {
				logger.info("未开启德邦对接-----" + key);
				return;
			}

			List<CwbOrderDTO> cwbOrderList = dataImportDAO_B2c.getCwbOrderTempByKeys(dp.getCustomerids());
			if (cwbOrderList == null) {
				return;
			}
			if (cwbOrderList.size() == 0) {
				return;
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
				ImportSubList(dp.getCustomerids(), dp.getWarehouseid(), subList);
				k++;
			}

		} catch (Exception e) {
			logger.error("0德邦物流0定时器临时表插入或修改方法执行异常!异常原因:", e);
		}
	}

	private void ImportSignOrder(String customerid, long warehouseId, CwbOrderDTO cwbOrder) {

		CwbOrder order = cwbDAO.getCwbByCwb(cwbOrder.getCwb());
		if (order != null) { // 要合单子
			logger.warn("[德邦物流]查询临时表-检测到有重复数据,已过滤!订单号：{},运单号:{}", cwbOrder.getCwb(), cwbOrder.getShipcwb());
		} else {
			User user = new User();
			user.setUserid(1);
			long warehouse_id = warehouseId;
			long warehouseid = warehouse_id != 0 ? warehouse_id : dataImportService_B2c.getTempWarehouseIdForB2c(); // 获取虚拟库房
																													// Id,所有的B2C对接都会导入默认的虚拟库房里面，方便能够统计到。
			user.setBranchid(warehouseid);
			EmailDate ed = dataImportService.getOrCreateEmailDate(cwbOrder.getCustomerid(), 0, warehouseid);

			emaildateDAO.editEditEmaildateForCwbcountAdd(ed.getEmaildateid());

			cwbOrderService.insertCwbOrder(cwbOrder, cwbOrder.getCustomerid(), ed.getWarehouseid(), user, ed);
			logger.info("[德邦物流]定时器临时表插入detail表成功!cwb={},shipcwb={}", cwbOrder.getCwb(), cwbOrder.getShipcwb());

			if (cwbOrder.getExcelbranch() == null || cwbOrder.getExcelbranch().length() == 0) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("cwb", cwbOrder.getCwb());
				map.put("userid", "1");
				try{
					addressmatch.sendBodyAndHeaders(null, map);
				}catch(Exception e){
					logger.error("", e);
					//写MQ异常表
					this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode("ImportSignOrder")
							.buildExceptionInfo(e.getMessage()).buildTopic(this.addressmatch.getDefaultEndpoint().getEndpointUri())
							.buildMessageHeaderObject(map).getMqException());
				}
			}
		}
		dataImportDAO_B2c.update_CwbDetailTempByCwb(cwbOrder.getOpscwbid());
	}

}
