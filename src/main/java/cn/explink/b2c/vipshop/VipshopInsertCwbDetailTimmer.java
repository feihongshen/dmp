package cn.explink.b2c.vipshop;

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
import cn.explink.dao.TransCwbDetailDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.EmailDate;
import cn.explink.domain.TransCwbDetail;
import cn.explink.domain.User;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.CwbStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.service.CwbOrderService;
import cn.explink.service.DataImportService;
import cn.explink.util.DateTimeUtil;

@Service
public class VipshopInsertCwbDetailTimmer {
	private Logger logger = LoggerFactory.getLogger(VipshopInsertCwbDetailTimmer.class);

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
	
	

	/**
	 * 唯品会定时器，查询临时表，插入数据到detail表中。
	 */
	
	public void selectTempAndInsertToCwbDetails(){
		
		for (B2cEnum enums : B2cEnum.values()) { // 遍历唯品会enum，可能有多个枚举
			if (enums.getMethod().contains("vipshop")) {
				int isOpenFlag = jointService.getStateForJoint(enums.getKey());
				if (isOpenFlag == 0) {
					logger.info("未开启vipshop[" + enums.getKey() + "]对接！");
					continue;
				}
				selectTempAndInsertToCwbDetail(enums.getKey());
			}
		}
		
	}

	public void selectTempAndInsertToCwbDetail(int b2ckey) {
		try {
			int isOpenFlag = jointService.getStateForJoint(b2ckey);
			if (isOpenFlag == 0) {
				logger.warn("未开启唯品会订单下载接口");
				return;
			}
			VipShop vipshop = vipShopGetCwbDataService.getVipShop(b2ckey);
			if (vipshop.getIsopendownload() == 0) {
				logger.info("未开启vipshop[" + b2ckey + "]临时表插入主表");
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
			logger.error("0唯品会0定时器临时表插入或修改方法执行异常!异常原因:", e);
			e.printStackTrace();
		}
	}

	private String dealWithOders(VipShop vipshop, String lefengCustomerid) {
		
		String cwbordertypeids=CwbOrderTypeIdEnum.Peisong.getValue()+","+CwbOrderTypeIdEnum.Shangmentui.getValue()+","+CwbOrderTypeIdEnum.Shangmenhuan.getValue();
		
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
			ImportSubList(vipshop.getCustomerids(), vipshop.getWarehouseid(), subList, vipshop);
			k++;
		}
		return "OK";
	}

	@Transactional
	public void ImportSubList(String customerid, long tmallWarehouseId, List<CwbOrderDTO> cwbOrderList, VipShop vipshop) {

		for (CwbOrderDTO cwbOrder : cwbOrderList) {
			try {
				ImportSignOrder(customerid, tmallWarehouseId, cwbOrder, vipshop);
			} catch (Exception e) {
				logger.error("定时器临时表插入或修改方法执行异常!customerid=" + customerid, e);
			}
		}
	}

	private void ImportSignOrder(String customerid, long warehouseId, CwbOrderDTO cwbOrder, VipShop vipshop) {

		CwbOrder order = cwbDAO.getCwbByCwb(cwbOrder.getCwb());
		if (order != null) { // 要合单子
			logger.warn("[唯品会]查询临时表-检测到有重复数据,已过滤!订单号：{},运单号:{}", cwbOrder.getCwb(), cwbOrder.getShipcwb());
		} else {
			User user = new User();
			user.setUserid(1);
			long warehouse_id = warehouseId;
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
			
			
			
			logger.info("[唯品会]定时器临时表插入detail表成功!cwb={},shipcwb={}", cwbOrder.getCwb(), cwbOrder.getShipcwb());

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
