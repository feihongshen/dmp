package cn.explink.b2c.vipshop.oxo;

import java.util.HashMap;
import java.util.List;

import cn.explink.service.DfFeeService;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JointService;
import cn.explink.b2c.vipshop.VipShop;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.EmailDateDAO;
import cn.explink.dao.MqExceptionDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.EmailDate;
import cn.explink.domain.MqExceptionBuilder;
import cn.explink.domain.User;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.service.CwbOrderService;
import cn.explink.service.DataImportService;

@Service
public class VipShopOXOInsertCwbDetailTimmer {
	private Logger logger = LoggerFactory.getLogger(VipShopOXOInsertCwbDetailTimmer.class);

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
	VipShopOXOGetCwbDataService vipShopOXOGetCwbDataService;
	@Autowired
	CwbDAO cwbDAO;

	@Autowired
	DataImportService dataImportService;
	
	@Produce(uri = "jms:topic:addressmatchOXO")
	ProducerTemplate addressmatch;

	@Autowired
	private MqExceptionDAO mqExceptionDAO;
    @Autowired
    private DfFeeService dfFeeService;
	
	/**
	 * OXO定时器，查询临时表，插入数据到detail表中。
	 */
	public void selectTempAndInsertToCwbDetail(int vipshop_key) {

		VipShop vipshop = vipShopOXOGetCwbDataService.getVipShop(vipshop_key);
		int isOpenFlag = jointService.getStateForJoint(vipshop_key);
		if (isOpenFlag == 0) {
			logger.info("未开启[VipShop_OXO]对接！B2CEnum_key={},当前获取订单插入临时表-----", vipshop_key);
			return;
		}
		String cwbordertypeids=CwbOrderTypeIdEnum.OXO.getValue()+","+CwbOrderTypeIdEnum.OXO_JIT.getValue();
		List<CwbOrderDTO> cwbOrderList = dataImportDAO_B2c.getCwbOrderTempByKeysExtends(vipshop.getCustomerids(),cwbordertypeids);
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
			ImportSubList(vipshop, subList);
			k++;
		}

	}

	@Transactional
	public void ImportSubList(VipShop vipshop, List<CwbOrderDTO> cwbOrderList) {

		for (CwbOrderDTO cwbOrder : cwbOrderList) {
			try {
				ImportSignOrder(vipshop, cwbOrder);
			} catch (Exception e) {
				logger.error("定时器临时表插入或修改方法执行异常!cwb=" + cwbOrder.getCwb(), e);
			}
		}
	}

	private void ImportSignOrder(VipShop vipshop, CwbOrderDTO cwbOrder) {
		CwbOrder order = cwbDAO.getCwbByCwb(cwbOrder.getCwb());
		if (order != null) { // 要合单子
			logger.warn("查询临时表-检测到有重复数据,已过滤!订单号：{},运单号:{}", cwbOrder.getCwb(), cwbOrder.getShipcwb());
		} else {
			User user = new User();
			user.setUserid(1);
			long warehouse_id = vipshop.getWarehouseid();
			long warehouseid = warehouse_id != 0 ? warehouse_id : dataImportService_B2c.getTempWarehouseIdForB2c(); // 获取虚拟库房
																													// Id,所有的B2C对接都会导入默认的虚拟库房里面，方便能够统计到。
			user.setBranchid(warehouseid);
			EmailDate ed = dataImportService.getOrCreateEmailDate(cwbOrder.getCustomerid(), 0, warehouseid);

			emaildateDAO.editEditEmaildateForCwbcountAdd(ed.getEmaildateid());

			cwbOrderService.insertCwbOrder(cwbOrder, cwbOrder.getCustomerid(), ed.getWarehouseid(), user, ed);
			logger.info("定时器临时表插入detail表成功!cwb={},shipcwb={}", cwbOrder.getCwb(), cwbOrder.getShipcwb());

            //added by Steve PENG. OXO/OXOJIT 导入后生需要成派费订单的相关操作。 start
            //commented by Steve PENG. OXO的单将会在归班审核后导入， OXOJIT 目前业务不会计费。
//            dfFeeService.saveFeeRelativeAfterOXOImport(cwbOrder.getCwb(), user);
            //added by Steve PENG. OXO/OXOJIT 导入后生需要成派费订单的相关操作。 end

			if(StringUtils.isNotBlank(cwbOrder.getRemark4())){
				String pickAddress = cwbOrder.getRemark4().replaceAll("&", "");
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("cwb", cwbOrder.getCwb());
				map.put("userid", "1");
				map.put("address", pickAddress);
				map.put("notifytype", 0);
				try{
					this.logger.info("消息发送端：addressmatch, header={}", map.toString());
					addressmatch.sendBodyAndHeaders(null, map);//解析提货站点
				}catch(Exception e){
					logger.error("", e);
					//写MQ异常表
					this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".ImportSignOrder")
							.buildExceptionInfo(e.toString()).buildTopic(this.addressmatch.getDefaultEndpoint().getEndpointUri())
							.buildMessageHeaderObject(map).getMqException());
				}
			}
			
			if(StringUtils.isNotBlank(cwbOrder.getConsigneeaddress())){
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("cwb", cwbOrder.getCwb());
				map.put("userid", "1");
				map.put("address", cwbOrder.getConsigneeaddress());
				map.put("notifytype", 1);
				try{
					addressmatch.sendBodyAndHeaders(null, map);//解析派件站点
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
