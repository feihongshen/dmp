package cn.explink.b2c.auto.order.service;

import java.util.HashMap;
import java.util.List;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JointService;
import cn.explink.b2c.vipshop.VipShop;
import cn.explink.b2c.vipshop.VipShopGetCwbDataService;
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
import cn.explink.service.ExplinkUserDetail;

import org.springframework.security.core.context.SecurityContextHolderStrategy;

@Service
public class TPSInsertCwbDetailTimmer {
	private Logger logger = LoggerFactory.getLogger(TPSInsertCwbDetailTimmer.class);

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
	VipShopGetCwbDataService vipShopGetCwbDataService;

	@Autowired
	CwbDAO cwbDAO;
	@Produce(uri = "jms:topic:addressmatch")
	ProducerTemplate addressmatch;
	@Autowired
	DataImportService dataImportService;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	/*@Autowired
	ExceptionCwbDAO exceptionCwbDAO;*/
	
	@Autowired
	private MqExceptionDAO mqExceptionDAO;
	
	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	/**
	 * TPS自动化定时器，查询临时表，插入数据到detail表中。
	 */
	public void selectTempAndInsertToCwbDetails(){
		
		for (B2cEnum enums : B2cEnum.values()) { 
			//vipshop_tps_automate:TPS自动化订单下发接口
			if (enums.getMethod().equals("vipshop_tps_automate")) {
				int isOpenFlag = jointService.getStateForJoint(enums.getKey());
				if (isOpenFlag == 0) {
					logger.info("未开启TPS自动化[" + enums.getKey() + "]对接！");
					continue;
				}
				selectTempAndInsertToCwbDetail(enums.getKey());
			}
		}
		
	}
    
	//数据处理入库
	public void selectTempAndInsertToCwbDetail(int b2ckey) {
		try {
			int isOpenFlag = jointService.getStateForJoint(b2ckey);
			if (isOpenFlag == 0) {
				logger.warn("未开启TPS自动化订单下载接口");
				return;
			}
			VipShop vipshop = vipShopGetCwbDataService.getVipShop(b2ckey);
			if (vipshop.getIsopendownload() == 0) {
				logger.info("未开启TPS自动化[" + b2ckey + "]临时表插入主表");
				return;
			}
			String lefengCustomerid=vipshop.getLefengCustomerid()==null||vipshop.getLefengCustomerid().isEmpty()?vipshop.getCustomerids():vipshop.getLefengCustomerid();
			for(int i=0;i<15;i++){
				String result = dealWithOders(vipshop, lefengCustomerid);
				if(result==null){
					break;
				}
			}
		} catch (Exception e) {
			logger.error("0TPS自动化0定时器临时表插入或修改方法执行异常!异常原因:", e);
			e.printStackTrace();
		}
	}
    
	//处理订单数据
	private String dealWithOders(VipShop vipshop, String lefengCustomerid) {
		
		String cwbordertypeids=CwbOrderTypeIdEnum.Peisong.getValue()+","+CwbOrderTypeIdEnum.Shangmentui.getValue()+","+CwbOrderTypeIdEnum.Shangmenhuan.getValue()+
				","+CwbOrderTypeIdEnum.OXO.getValue()+","+CwbOrderTypeIdEnum.OXO_JIT.getValue();
				
		List<CwbOrderDTO> cwbOrderList=dataImportDAO_B2c.getCwbOrderTempByKeysExtends((vipshop.getCustomerids()+","+lefengCustomerid),cwbordertypeids);

		if (cwbOrderList == null) {
			return null;
		}
		if (cwbOrderList.size() == 0) {
			return null;
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
		return "OK";
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
    
	//将临时表中数据插入主表
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
			EmailDate ed = null;
			if (vipshop.getIsTuoYunDanFlag() == 0) {
				ed = dataImportService.getOrCreateEmailDate(cwbOrder.getCustomerid(), 0, warehouseid);

			} else {
				ed = dataImportService.getEmailDate_B2CByEmaildate(cwbOrder.getCustomerid(), cwbOrder.getCustomerwarehouseid(), warehouseid, cwbOrder.getEmaildate());
			}

			emaildateDAO.editEditEmaildateForCwbcountAdd(ed.getEmaildateid());
			cwbOrderService.insertCwbOrder(cwbOrder, cwbOrder.getCustomerid(), warehouseid, user, ed);
			logger.info("[TPS自动化]定时器临时表插入detail表成功!cwb={},shipcwb={}", cwbOrder.getCwb(), cwbOrder.getShipcwb());
			
			if(cwbOrder.getCwbordertypeid()==4||cwbOrder.getCwbordertypeid()==5){
				if(StringUtils.isNotBlank(cwbOrder.getRemark4())){
					String pickAddress = cwbOrder.getRemark4().replaceAll("&", "");
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("cwb", cwbOrder.getCwb());
					map.put("userid", "1");
					map.put("address", pickAddress);
					map.put("notifytype", 0);
					try{
						addressmatch.sendBodyAndHeaders(null, map);//解析提货站点
					}catch(Exception e){
						logger.error("", e);
						//写MQ异常表
						this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode("ImportSignOrder")
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
						this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode("ImportSignOrder")
								.buildExceptionInfo(e.toString()).buildTopic(this.addressmatch.getDefaultEndpoint().getEndpointUri())
								.buildMessageHeaderObject(map).getMqException());
					}
				}
            }else{
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
								.buildExceptionInfo(e.toString()).buildTopic(this.addressmatch.getDefaultEndpoint().getEndpointUri())
								.buildMessageHeaderObject(map).getMqException());
					}
    			}
            }
		}
		dataImportDAO_B2c.update_CwbDetailTempByCwb(cwbOrder.getOpscwbid());
		/*//模拟入库
		CwbOrder cwbOrderInsert = null;
	    try{
	    	cwbOrderInsert = cwbOrderService.intoWarehous(currentUser, orderInsert.getCwb(),orderInsert.getTranscwb(), orderInsert.getCustomerid(), orderInsert.getDeliverid(), orderInsert.getEmaildateid(), "", "", false);
		} catch (CwbException ce) {
			exceptionCwbDAO.createExceptionCwb(cwbOrderInsert.getCwb(), ce.getFlowordertye(),ce.getMessage(), currentUser.getBranchid(),currentUser.getUserid(), cwbOrder == null?0:cwbOrder.getCustomerid(), 0, 0, 0, "");
			logger.error("TPS自动化订单，入库失败！订单号：" + cwbOrderInsert.getCwb() + ";异常：" + ce.getMessage());
		}*/
	}

}
