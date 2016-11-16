package cn.explink.b2c.vipshop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

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
import cn.explink.b2c.tools.VipShopCwbTempInsertTask;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.EmailDateDAO;
import cn.explink.dao.MqExceptionDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.EmailDate;
import cn.explink.domain.MqExceptionBuilder;
import cn.explink.domain.User;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.IsmpsflagEnum;
import cn.explink.enumutil.MPSAllArrivedFlagEnum;
import cn.explink.enumutil.MpsTypeEnum;
import cn.explink.service.CwbOrderService;
import cn.explink.service.DataImportService;
import cn.explink.support.transcwb.TransCwbDao;

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
	
	@Autowired
	TransCwbDao transCwbDao;
	
	@Autowired
	private MqExceptionDAO mqExceptionDAO;

	/**
	 * 唯品会定时器，查询临时表，插入数据到detail表中。
	 */
	
	public void selectTempAndInsertToCwbDetails(){
		List<B2cEnum> enumList = new ArrayList<B2cEnum>();
		for (B2cEnum enums : B2cEnum.values()) { // 遍历唯品会enum，可能有多个枚举
			if (enums.getMethod().contains("vipshop")) {
				int isOpenFlag = jointService.getStateForJoint(enums.getKey());
				if (isOpenFlag == 0) {
					logger.info("未开启vipshop[" + enums.getKey() + "]对接！");
					continue;
				}
				enumList.add(enums);
//				selectTempAndInsertToCwbDetail(enums.getKey());
			}
		}
		if(enumList.size() >= 1){
			ForkJoinPool forkJoinPool = new ForkJoinPool();
			try{
				forkJoinPool.submit(new VipShopCwbTempInsertTask(this, enumList));
			}finally{
				try {
					forkJoinPool.shutdown();
					if (!forkJoinPool.awaitTermination(1, TimeUnit.HOURS)) {
						logger.info("第一次forkJoinPool.awaitTermination线程池线程未完成");
						forkJoinPool.shutdownNow();
						while(!forkJoinPool.awaitTermination(1, TimeUnit.MINUTES)){
							logger.info("shutdownNow()forkJoinPool.awaitTermination线程池线程未完成");
							forkJoinPool.shutdownNow();
						};
					}
				} catch (InterruptedException e) {
					logger.info("forkJoinPool.awaitTermination出错", e);
				}
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
				//String result = dealWithOders(vipshop, lefengCustomerid);
				
				String result = dealWithOrders(vipshop, lefengCustomerid);				
				if(result==null){
					break;
				}
			}
		} catch (Exception e) {
			logger.error("0唯品会0定时器临时表插入或修改方法执行异常!异常原因:", e);
		}
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
			//Added by leoliao at 2016-0316 同步临时表的信息（做一个信息补偿）
			CwbOrderDTO cwbOrderNew = dataImportDAO_B2c.getCwbByCwbB2ctemp(order.getCwb());
			
			//临时表已集齐但业务表未集齐的一票多件订单需要做信息补偿
			if(order.getIsmpsflag() == MpsTypeEnum.YiPiaoDuoJian.getValue() && order.getMpsallarrivedflag() != MPSAllArrivedFlagEnum.YES.getValue()
			   && cwbOrderNew != null && cwbOrderNew.getMpsallarrivedflag() == MPSAllArrivedFlagEnum.YES.getValue() ){
				
				String strCwb       = order.getCwb();
				String strTranscwbs = cwbOrderNew.getTranscwb();
				int    totalPack    = cwbOrderNew.getSendcargonum();
				
				//一票多件订单需要做信息补偿
				cwbDAO.updateTmsPackageCondition(strCwb, strTranscwbs, totalPack, MPSAllArrivedFlagEnum.YES.getValue(), MpsTypeEnum.YiPiaoDuoJian.getValue());

				//添加订单与运单关联记录
				String allTranscwb = cwbOrderNew.getTranscwb()==null?"":cwbOrderNew.getTranscwb();
				String strSplit    = cwbOrderService.getSplitstring(allTranscwb);
				String[] arrTranscwb = allTranscwb.split(strSplit);
				for(String transcwb : arrTranscwb){
					if(transcwb == null || transcwb.trim().equals("")){
						continue;
					}
					
					String selectCwb = transCwbDao.getCwbByTransCwb(transcwb);
					if(selectCwb == null || selectCwb.trim().equals("")){
						transCwbDao.saveTranscwb(transcwb, strCwb);
					}
				}
				
				//添加运单信息(该方法已经做了防重处理)
				this.dataImportService.insertTransCwbDetail(cwbOrderNew, order.getEmaildate());
			}
			//Added end
			
			logger.warn("[唯品会]查询临时表-检测到有重复数据,已过滤!订单号：{},运单号:{}", cwbOrder.getCwb(), cwbOrder.getShipcwb());
		} else {
			User user = new User();
			user.setUserid(1);
			long warehouse_id = warehouseId;
			long warehouseid = warehouse_id != 0 ? warehouse_id : dataImportService_B2c.getTempWarehouseIdForB2c(); // 获取虚拟库房
																													// Id,所有的B2C对接都会导入默认的虚拟库房里面，方便能够统计到。
			user.setBranchid(warehouseid);
			
			//Added by leoliao at 2016-03-16 集包模式的一票多件，需要重新从临时表取数据
			if(cwbOrder.getIsmpsflag() == MpsTypeEnum.YiPiaoDuoJian.getValue()){
				logger.info("------Old CwbOrderDTO:sendcarnum={},transcwb={}, mpsallarrivedflag={}------", 
						    cwbOrder.getSendcargonum(), cwbOrder.getTranscwb(), cwbOrder.getMpsallarrivedflag());
				
				CwbOrderDTO cwbOrderNew = dataImportDAO_B2c.getCwbByCwbB2ctemp(cwbOrder.getCwb());
				if(cwbOrderNew != null){
					cwbOrder = cwbOrderNew;
					
					logger.info("------New CwbOrderDTO:sendcarnum={},transcwb={}, mpsallarrivedflag={}------", 
							cwbOrderNew.getSendcargonum(), cwbOrderNew.getTranscwb(), cwbOrderNew.getMpsallarrivedflag());
				}
			}
			//Added end
			
			EmailDate ed = null;
			if (vipshop.getIsTuoYunDanFlag() == 0) {
				if(vipshop.getIsCreateTimeToEmaildateFlag()==1){
					ed = dataImportService.getEmailDate_B2CByEmaildate(Integer.valueOf(customerid), 0, warehouseid, cwbOrder.getEmaildate());
				}else{
					ed = dataImportService.getOrCreateEmailDate(cwbOrder.getCustomerid(), 0, warehouseid);
				}

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
				try{
					this.logger.info("消息发送端：addressmatch, header={}", map.toString());
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
	

	/**
	 * 执行转业务处理
	 * @author leo01.liao
	 * @param vipshop
	 * @param lefengCustomerid
	 * @return String
	 */
	private String dealWithOrders(VipShop vipshop, String lefengCustomerid) {
		String cwbordertypeids = CwbOrderTypeIdEnum.Peisong.getValue() + "," +
								 CwbOrderTypeIdEnum.Shangmentui.getValue() + "," +
								 CwbOrderTypeIdEnum.Shangmenhuan.getValue();
		String customerids     = (vipshop.getCustomerids() + "," + lefengCustomerid);
		//int    maxCount = 2000; //vipshop.getGetMaxCount();
		
		//获取未转业务的记录
		List<CwbOrderDTO> listCwbOrderDto = dataImportDAO_B2c.getCwbOrderTempByKeysExtends(customerids, cwbordertypeids);
		if(listCwbOrderDto == null || listCwbOrderDto.isEmpty()){
			return null;
		}
		
		List<CwbOrderDTO> listCwbOrderDtoIsMps   = new ArrayList<CwbOrderDTO>();
		List<CwbOrderDTO> listCwbOrderDtoIsNoMps = new ArrayList<CwbOrderDTO>();
		for(CwbOrderDTO cwbOrderDto : listCwbOrderDto){
			if(cwbOrderDto.getIsmpsflag() == IsmpsflagEnum.yes.getValue()){
				//集包模式
				listCwbOrderDtoIsMps.add(cwbOrderDto);
			}else{
				//非集包模式
				listCwbOrderDtoIsNoMps.add(cwbOrderDto);
			}
		}
		
		//获取虚拟库房Id,所有的B2C对接都会导入默认的虚拟库房里面，方便能够统计到。
		long warehouseId = vipshop.getWarehouseid() != 0 ? vipshop.getWarehouseid() : dataImportService_B2c.getTempWarehouseIdForB2c();
				
		//集包模式的订单转业务
		for(CwbOrderDTO cwbOrderDtoIsMps : listCwbOrderDtoIsMps){
			try{				
				this.ImportSingleOrder(vipshop.getCustomerids(), warehouseId, cwbOrderDtoIsMps.getOpscwbid(), vipshop);
			}catch(Exception ee){
				logger.error("定时器临时表插入或修改方法[dealWithOrders-mps]执行异常!customerids=" + customerids, ee);
			}
		}		
		
		//非集包模式的订单转业务
		int countNoMps = listCwbOrderDtoIsNoMps.size();
		if(countNoMps > 0){
			int k = 1;
			int batch = 50;
			while (true) {
				try{
					int fromIndex = (k - 1) * batch;
					if (fromIndex >= countNoMps) {
						break;
					}
					
					int toIdx = k * batch;
					if (k * batch > countNoMps) {
						toIdx = countNoMps;
					}
										
					List<CwbOrderDTO> subList = listCwbOrderDtoIsNoMps.subList(fromIndex, toIdx);
					this.ImportBatchOrder(vipshop.getCustomerids(), warehouseId, subList, vipshop);
					
					k++;
				}catch(Exception ee){
					logger.error("定时器临时表插入或修改方法[dealWithOrders-no mps]执行异常!customerids=" + customerids, ee);
				}
			}
		}
		
		return "OK";
	}
	
	/**
	 * 非集包模式下的订单转业务时一批订单一个事务
	 * @param customerid
	 * @param warehouseId
	 * @param listCwbOrderDto
	 * @param vipshop
	 */
	@Transactional
	private void ImportBatchOrder(String customerid, long warehouseId, List<CwbOrderDTO> listCwbOrderDto, VipShop vipshop) {
		if(listCwbOrderDto == null || listCwbOrderDto.isEmpty()){
			return;
		}
		
		for(CwbOrderDTO cwbOrderDto : listCwbOrderDto){
			long b2cTempOpscwbid = cwbOrderDto.getOpscwbid();
			
			String   strCwb   = cwbOrderDto.getCwb(); //订单号
			CwbOrder cwbOrder = cwbDAO.getCwbByCwb(strCwb);
			if(cwbOrder != null){
				//更新已转业务标识
				dataImportDAO_B2c.update_CwbDetailTempByCwb(b2cTempOpscwbid);
				
				logger.warn("[唯品会]查询临时表-检测到有重复数据,已过滤!cwb={},transcwb={}", cwbOrderDto.getCwb(), cwbOrderDto.getTranscwb());
				
				continue;
			}
			
			//订单转业务表处理
			this.processIntoTable(customerid, warehouseId, cwbOrderDto, vipshop);
			
			//更新已转业务标识
			dataImportDAO_B2c.update_CwbDetailTempByCwb(b2cTempOpscwbid);
		}
	}

	/**
	 * 集包模式的一票多件转业务时一条订单一个事务
	 * @author leo01.liao
	 * @param customerid
	 * @param warehouseId
	 * @param b2cTempOpscwbid
	 * @param vipshop
	 */
	@Transactional
	private void ImportSingleOrder(String customerid, long warehouseId, long b2cTempOpscwbid, VipShop vipshop) {
		CwbOrderDTO cwbOrderDto = dataImportDAO_B2c.getCwbByCwbB2ctempOpscwbidLock(b2cTempOpscwbid);
		if(cwbOrderDto == null){
			return;
		}
		
		String   strCwb   = cwbOrderDto.getCwb(); //订单号
		CwbOrder cwbOrder = cwbDAO.getCwbByCwb(strCwb);
		if(cwbOrder != null){
			//已转业务
			//一票多件更新业务表对应的数据项(临时表数据更新了，会更新转业务标识[getDataFlag]为0以重新同步[只同步相应的数据项：发货件数/运单号/是否已集齐等])
			if(cwbOrder.getIsmpsflag() == IsmpsflagEnum.yes.getValue()){
				logger.info("======一票多件[cwb="+strCwb+"]更新业务表对应的数据项==Begin====");
				
				String emaildate    = cwbOrderDto.getEmaildate();
				String strTranscwbs = cwbOrderDto.getTranscwb()==null?"":cwbOrderDto.getTranscwb();
				int    totalPack    = cwbOrderDto.getSendcargonum();
				int    ismpsflag    = cwbOrderDto.getIsmpsflag();
				int    mpsallarrivedflag = cwbOrderDto.getMpsallarrivedflag();
				
				//更新业务表对应的数据项
				cwbDAO.updateTmsPackageCondition(strCwb, strTranscwbs, totalPack, mpsallarrivedflag, ismpsflag);
	
				//添加订单与运单关联记录
				String   strSplit    = cwbOrderService.getSplitstring(strTranscwbs);
				String[] arrTranscwb = strTranscwbs.split(strSplit);
				for(String transcwb : arrTranscwb){
					if(transcwb == null || transcwb.trim().equals("")){
						continue;
					}
					
					String selectCwb = transCwbDao.getCwbByTransCwb(transcwb);
					if(selectCwb == null || selectCwb.trim().equals("")){
						transCwbDao.saveTranscwb(transcwb, strCwb);
					}
				}
				
				//添加运单信息(该方法已经做了防重处理)
				this.dataImportService.insertTransCwbDetail(cwbOrderDto, emaildate);
				
				//把发货时间写入订单表
				cwbDAO.updateEmaildate(strCwb, emaildate);
				
				//把发货时间写入运单表
				if(arrTranscwb != null && arrTranscwb.length > 0){
					dataImportService.updateEmaildate(Arrays.asList(arrTranscwb), emaildate);
				}
				
				logger.info("======一票多件[cwb="+strCwb+"]更新业务表对应的数据项==End====");
			}
			
			logger.warn("[唯品会]查询临时表-检测到有重复数据,已过滤!cwb={},transcwb={}", cwbOrderDto.getCwb(), cwbOrderDto.getTranscwb());
		}else{
			//订单转业务表处理
			this.processIntoTable(customerid, warehouseId, cwbOrderDto, vipshop);
		}
		
		//更新已转业务标识
		dataImportDAO_B2c.update_CwbDetailTempByCwb(cwbOrderDto);
	}
	
	/**
	 * 订单转业务表处理
	 * @param customerid
	 * @param warehouseId
	 * @param cwbOrderDto
	 * @param vipshop
	 */
	private void processIntoTable(String customerid, long warehouseId, CwbOrderDTO cwbOrderDto, VipShop vipshop){
		User user = new User();
		user.setUserid(1);
		user.setBranchid(warehouseId);
		
		EmailDate ed = null;
		if (vipshop.getIsTuoYunDanFlag() == 0) {
			ed = dataImportService.getOrCreateEmailDate(cwbOrderDto.getCustomerid(), 0, warehouseId);
//			if(vipshop.getIsCreateTimeToEmaildateFlag()==1){
//				ed = dataImportService.getEmailDate_B2CByEmaildate(Integer.valueOf(customerid), 0, warehouseId, cwbOrderDto.getEmaildate());
//			}else{
//				ed = dataImportService.getOrCreateEmailDate(cwbOrderDto.getCustomerid(), 0, warehouseId);
//			}
		} else {
			ed = dataImportService.getEmailDate_B2CByEmaildate(cwbOrderDto.getCustomerid(), cwbOrderDto.getCustomerwarehouseid(), warehouseId, cwbOrderDto.getEmaildate());
		}
		
		emaildateDAO.editEditEmaildateForCwbcountAdd(ed.getEmaildateid());
		cwbOrderService.insertCwbOrder(cwbOrderDto, cwbOrderDto.getCustomerid(), warehouseId, user, ed);
		logger.info("[唯品会]定时器临时表插入detail表成功!cwb={},shipcwb={},transcwb={}", cwbOrderDto.getCwb(), cwbOrderDto.getShipcwb(), cwbOrderDto.getTranscwb());
        cwbOrderService.handleShangMenTuiCwbDeliveryPermit(cwbOrderDto); //处理唯品会上门退订单领货标识  2016-06-16
		
		if (cwbOrderDto.getExcelbranch() == null || cwbOrderDto.getExcelbranch().length() == 0) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("cwb", cwbOrderDto.getCwb());
			map.put("userid", "1");
			try{
				addressmatch.sendBodyAndHeaders(null, map);
			}catch(Exception e){
				logger.error("", e);
				//写MQ异常表
				this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".processIntoTable")
						.buildExceptionInfo(e.toString()).buildTopic(this.addressmatch.getDefaultEndpoint().getEndpointUri())
						.buildMessageHeaderObject(map).getMqException());
			}
		}
	}
	
	//add by zhouhuan tps对接新增非唯品会订单转业务定时器，查询临时表，插入数据到detail表中 2016-08-05
	public void selectAllTempAndInsertToCwbDetails(){
		for (B2cEnum enums : B2cEnum.values()) { // 遍历唯品会enum，可能有多个枚举
			if (enums.getMethod().contains("tpsother")) {
				int isOpenFlag = jointService.getStateForJoint(enums.getKey());
				if (isOpenFlag == 0) {
					logger.info("未开启vipshop[" + enums.getKey() + "]对接！");
					continue;
				}
				selectTempAndInsertToCwbDetail(enums.getKey());
			}
		}
		
	}
	
}
