package cn.explink.b2c.homegou;

import java.util.HashMap;
import java.util.List;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class HomegouInsertCwbDetailTimmer {
	private Logger logger = LoggerFactory.getLogger(HomegouInsertCwbDetailTimmer.class);

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
	HomegouService homegouService;

	@Autowired
	CwbDAO cwbDAO;
	@Produce(uri = "jms:topic:addressmatch")
	ProducerTemplate addressmatch;
	@Autowired
	DataImportService dataImportService;

	@Autowired
	private MqExceptionDAO mqExceptionDAO;
	
	/**
	 * 家有购物定时器，查询临时表，插入数据到detail表中。
	 */

	public void selectTempAndInsertToCwbDetail(int yhd_key) {
		try {
			int isOpenFlag = jointService.getStateForJoint(B2cEnum.HomeGou.getKey());
			if (isOpenFlag == 0) {
				logger.warn("未开启家有购物订单下载接口");
				return;
			}
			Homegou cj = homegouService.getHomeGou(B2cEnum.HomeGou.getKey());

			List<CwbOrderDTO> cwbOrderList = dataImportDAO_B2c.getCwbOrderTempByKeys(cj.getCustomerids() + "," + cj.getCustomerid_tuihuo());

			if (cwbOrderList == null || cwbOrderList.size() == 0) {
				logger.info("当前0家有购物0没有待插入detail表的数据...");
				return;
			}
			for (CwbOrderDTO cwbOrder : cwbOrderList) {
				CwbOrder order = cwbDAO.getCwbByCwb(cwbOrder.getCwb());
				if (order != null) { // 要合单子
					logger.warn("查询临时表-检测到0家有购物0有重复数据,已过滤!订单号：{},运单号:{}", cwbOrder.getCwb(), cwbOrder.getShipcwb());
				} else {
					// insert
					User user = new User();
					user.setUserid(1);
					long warehouse_id = cj.getWarehouseid();
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
							this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode("selectTempAndInsertToCwbDetail")
									.buildExceptionInfo(e.getMessage()).buildTopic(this.addressmatch.getDefaultEndpoint().getEndpointUri())
									.buildMessageHeaderObject(map).getMqException());
						}
					}

				}
				dataImportDAO_B2c.update_CwbDetailTempByCwb(cwbOrder.getOpscwbid());
			}
		} catch (Exception e) {
			logger.error("0家有购物0定时器临时表插入或修改方法执行异常!异常原因", e);
		}
	}

}
