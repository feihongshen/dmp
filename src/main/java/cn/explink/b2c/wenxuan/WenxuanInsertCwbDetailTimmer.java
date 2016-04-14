package cn.explink.b2c.wenxuan;

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
public class WenxuanInsertCwbDetailTimmer {
	private Logger logger = LoggerFactory.getLogger(WenxuanInsertCwbDetailTimmer.class);
	@Autowired
	WenxuanService wenxuanService;

	@Autowired
	DataImportService_B2c dataImportService_B2c;
	@Autowired
	EmailDateDAO emaildateDAO;
	@Autowired
	JointService jointService;
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;

	@Produce(uri = "jms:topic:addressmatch")
	ProducerTemplate addressmatch;
	@Autowired
	DataImportService dataImportService;
	@Autowired
	CwbDAO cwbDAO;
	
	@Autowired
	private MqExceptionDAO mqExceptionDAO;

	/**
	 * 易迅定时器，查询临时表，插入数据到detail表中。
	 */

	public void selectTempAndInsertToCwbDetail() {

		int efastKey = B2cEnum.Wenxuan.getKey();
		int isOpenFlag = jointService.getStateForJoint(efastKey);
		if (isOpenFlag == 0) {
			logger.info("未开启[文轩网]对接！efastKey={}", efastKey);
			return;
		}
		dealWithEfastMethod(efastKey);

	}

	private void dealWithEfastMethod(int efastKey) {

		Wenxuan hg = wenxuanService.getWenxuan(efastKey);
		List<CwbOrderDTO> cwbOrderList = dataImportDAO_B2c.getCwbOrderTempByKeys(hg.getCustomerid());
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
			ImportSubList(hg, subList);
			k++;
		}
	}

	@Transactional
	public void ImportSubList(Wenxuan hg, List<CwbOrderDTO> cwbOrderList) {

		for (CwbOrderDTO cwbOrder : cwbOrderList) {
			try {
				ImportSignOrder(hg, cwbOrder);
			} catch (Exception e) {
				logger.error("定时器临时表插入或修改方法执行异常!cwb=" + cwbOrder.getCwb(), e);
			}
		}
	}

	private void ImportSignOrder(Wenxuan hg, CwbOrderDTO cwbOrder) {
		CwbOrder order = cwbDAO.getCwbByCwb(cwbOrder.getCwb());
		if (order != null) { // 要合单子
			logger.warn("查询临时表-检测到0文轩网0有重复数据,已过滤!订单号：{},运单号:{}", cwbOrder.getCwb(), cwbOrder.getShipcwb());
		} else {
			// insert
			User user = new User();
			user.setUserid(1);
			long warehouse_id = hg.getWarehouseid();
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
				try{
					addressmatch.sendBodyAndHeaders(null, map);
				}catch(Exception e){
					logger.error("", e);
					//写MQ异常表
					this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".ImportSignOrder")
							.buildExceptionInfo(e.toString()).buildTopic(this.addressmatch.getDefaultEndpoint().getEndpointUri())
							.buildMessageHeaderObject(map).getMqException());
				}
			}

		}
		dataImportDAO_B2c.update_CwbDetailTempByCwb(cwbOrder.getOpscwbid());
	}

}
