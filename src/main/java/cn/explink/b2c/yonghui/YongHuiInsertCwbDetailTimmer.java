package cn.explink.b2c.yonghui;

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
import cn.explink.b2c.yonghui.domain.YongHui;
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
import cn.explink.support.transcwb.TransCwbDao;

@Service
public class YongHuiInsertCwbDetailTimmer {
	private Logger logger = LoggerFactory.getLogger(YongHuiInsertCwbDetailTimmer.class);

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
	YongHuiServices yonghuiServices;
	@Autowired
	CwbDAO cwbDAO;
	@Produce(uri = "jms:topic:addressmatch")
	ProducerTemplate addressmatch;
	@Autowired
	DataImportService dataImportService;
	@Autowired
	TransCwbDao transCwbDao;

	@Autowired
	private MqExceptionDAO mqExceptionDAO;
	
	/**
	 * 一号店定时器，查询临时表，插入数据到detail表中。
	 */
	public void selectTempAndInsertToCwbDetail() {
		int key = B2cEnum.Yonghui.getKey();
		int isOpenFlag = this.jointService.getStateForJoint(key);
		if (isOpenFlag == 0) {
			this.logger.info("未开启[永辉超市]对接！yhd_key={},当前获取订单插入临时表-----", key);
			return;
		}
		YongHui yh = this.yonghuiServices.getYonghui(key);

		List<CwbOrderDTO> cwbOrderList = this.dataImportDAO_B2c.getCwbOrderTempByKeys(yh.getCustomerid() + "");
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
			if ((k * batch) > cwbOrderList.size()) {
				toIdx = cwbOrderList.size();
			}
			List<CwbOrderDTO> subList = cwbOrderList.subList(fromIndex, toIdx);
			this.ImportSubList(yh, subList);
			k++;
		}

	}

	@Transactional
	public void ImportSubList(YongHui yh, List<CwbOrderDTO> cwbOrderList) {

		for (CwbOrderDTO cwbOrder : cwbOrderList) {
			try {
				this.ImportSignOrder(yh, cwbOrder);
			} catch (Exception e) {
				this.logger.error("定时器临时表插入或修改方法执行异常!cwb=" + cwbOrder.getCwb(), e);
			}
		}
	}

	private void ImportSignOrder(YongHui yh, CwbOrderDTO cwbOrder) {

		CwbOrder order = this.cwbDAO.getCwbByCwb(cwbOrder.getCwb());
		if (order != null) { // 要合单子

			int tempValue = 0; // 变量控制是否追加
			String transcwb = order.getTranscwb();
			if ((transcwb != null) && !transcwb.isEmpty()) {

				for (String transc : transcwb.split(",")) {
					if (transc.equals(cwbOrder.getTranscwb())) {
						this.logger.info("检测到运单号={}已存在，不追加", cwbOrder.getTranscwb());
						tempValue++;
					}
				}
			}

			if (tempValue == 0) {
				this.cwbDAO.appendTranscwb(cwbOrder.getCwb(), cwbOrder.getTranscwb());
				this.transCwbDao.saveTranscwb(cwbOrder.getTranscwb(), cwbOrder.getCwb());

				this.logger.info("查询临时表-检测到有需追加运单号!订单号：{},运单号:{}", cwbOrder.getCwb(), cwbOrder.getTranscwb());
			}

		} else {
			User user = new User();
			user.setUserid(1);
			long warehouse_id = yh.getWarehouserid();
			long warehouseid = warehouse_id != 0 ? warehouse_id : this.dataImportService_B2c.getTempWarehouseIdForB2c(); // 获取虚拟库房
																															// Id,所有的B2C对接都会导入默认的虚拟库房里面，方便能够统计到。
			user.setBranchid(warehouseid);
			EmailDate ed = this.dataImportService.getOrCreateEmailDate(cwbOrder.getCustomerid(), 0, warehouseid);

			this.emaildateDAO.editEditEmaildateForCwbcountAdd(ed.getEmaildateid());

			this.cwbOrderService.insertCwbOrder(cwbOrder, cwbOrder.getCustomerid(), ed.getWarehouseid(), user, ed);
			this.logger.info("定时器临时表插入detail表成功!cwb={},shipcwb={}", cwbOrder.getCwb(), cwbOrder.getShipcwb());

			if ((cwbOrder.getExcelbranch() == null) || (cwbOrder.getExcelbranch().length() == 0)) {
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
		this.dataImportDAO_B2c.update_CwbDetailTempByCwb(cwbOrder.getOpscwbid());
	}
}
