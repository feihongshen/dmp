package cn.explink.b2c.auto.order.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.explink.service.DfFeeService;
import net.sf.json.JSONObject;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.auto.order.dao.TPSDataImportDAO_B2c;
import cn.explink.b2c.auto.order.vo.TPSOrder;
import cn.explink.b2c.auto.order.vo.TPSOrderDetails;
import cn.explink.b2c.tmall.CwbColumnSet;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.JointService;
import cn.explink.b2c.vipshop.ReaderXMLHandler;
import cn.explink.b2c.vipshop.SOAPHandler;
import cn.explink.b2c.vipshop.VipGathercompEnum;
import cn.explink.b2c.vipshop.VipShop;
import cn.explink.b2c.vipshop.VipShopGetCwbDataService;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.AccountCwbFareDetailDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.MqExceptionDAO;
import cn.explink.dao.OrderGoodsDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.EmailDate;
import cn.explink.domain.ExcelColumnSet;
import cn.explink.domain.MqExceptionBuilder;
import cn.explink.domain.OrderGoods;
import cn.explink.domain.User;
import cn.explink.enumutil.CwbOXOStateEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.IsmpsflagEnum;
import cn.explink.enumutil.MPSAllArrivedFlagEnum;
import cn.explink.enumutil.MpsTypeEnum;
import cn.explink.enumutil.MpsswitchTypeEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.exception.CwbException;
import cn.explink.service.CwbOrderService;
import cn.explink.service.DataImportService;
import cn.explink.support.transcwb.TransCwbDao;
import cn.explink.util.DateTimeUtil;

@Service
public class TPSGetOrderDataService {
	
	@Autowired
	ReaderXMLHandler readXMLHandler;
	@Autowired
	DataImportService_B2c dataImportService_B2c;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	@Autowired
	TPSDataImportDAO_B2c tpsDataImportDAO_B2c;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	DataImportService dataImportService;
	@Autowired
	CwbDAO cwbDAO;
	@Produce(uri = "jms:topic:addressmatchOXO")
	ProducerTemplate addressmatch;
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	AccountCwbFareDetailDAO accountCwbFareDetailDAO;
	@Autowired
	OrderGoodsDAO orderGoodsDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	VipShopGetCwbDataService vipShopGetCwbDataService;
	@Autowired
	TPSOrderImportService_B2c tPSOrderImportService_B2c;
	@Autowired
	SOAPHandler soapHandler;
	@Autowired
	CwbColumnSet cwbColumnSet;
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;
	@Autowired
	TransCwbDao transCwbDao;
    @Autowired
    DfFeeService dfFeeService;

	@Autowired
	private MqExceptionDAO mqExceptionDAO;
	
	private Logger logger = LoggerFactory.getLogger(TPSGetOrderDataService.class);
    
	 public VipShop getVipShop(int key) {
		JointEntity obj = this.jiontDAO.getJointEntity(key);
		if(obj == null){
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(obj.getJoint_property());
		VipShop vipshop = (VipShop) JSONObject.toBean(jsonObj, VipShop.class);
		return vipshop;
	}
	 
		/**
		 * 构建请求，解析返回信息,获取接口运单信息
		 */
		/*public void getOrdersByTPS(int vipshop_key){
	    	VipShop vipshop = this.getVipShop(vipshop_key);
			int isOpenFlag = this.jointService.getStateForJoint(vipshop_key);
			if (isOpenFlag == 0) {
				this.logger.info("未开启TPS自动化[" + vipshop_key + "]对接！");
				return;
			}
			if (vipshop.getIsopendownload() == 0) {
				this.logger.info("未开启TPS自动化[" + vipshop_key + "]订单下载接口");
				return;
			}
	        this.logger.info("消费下发承运商订单状态接口表的物流状态信息:");
	        try {
	        	String msg ="[{'businessType':60,'buyerAddress':'广东省121','buyerName':'104104101',"
	            		+ "'cmdType':'new','custOrderNo':'huanPS007',"
	            		+"'details':[],'dirty':false,'extRowStatus':0,'feight':0.00,"
	            		+"'isCod':false,'isDeleted':false,'isSend':false,'logged':false,'totalPack':12,"
	            		+"'mobile':'104104101','orderDeliveryBatch':'0','orderSn':'','postCode':'',"
	            		+"'requiredTime':'','rowStatus':2,'serviceType':1,'storeContacts':'何宇新',"
	            		+"'storeTel':'13570507226','tel':'104104101','transportDay':0,'transportType':'',"
	            		+"'valuationValue':0,'vipClub':0,'warehouse':'VIP_NH','warehouseAddr':'花海仓20号',"
	            		+ "'codAmount':12.0,'originalWeight':12.0,'originalVolume':12.0,'customerName':'唯品会',"
	            		+"'salesPlatform':'VIP','brandName':'韩都衣舍','storeName':'guag','cnorProv':'广东',"
	            		+"'cnorCity':'广州','cnorRegion':'荔湾','cnorAddr':'花海','custCode':8,'orgName':'',"
	            		+"'orderSum':89,'payment':1,'boxNo':'23'}]";
	        	
	        	TPSOrder order = new TPSOrder();
	 		    order.setAddTime(null);
	 		    order.setBoxNo("");
	 		    order.setBrandName("");
	 		    order.setBusinessType(30);
	 		    order.setBuyerAddress("104104101");
	 		    order.setBuyerName("104104101");
	 		    order.setCmdType("003");
	 		    order.setCnorAddr("13570507226");
	 		    order.setCnorCity("广州市");
	 		    order.setCnorProv("广东省");
	 		    order.setCnorRegion("荔湾区");
	 		    order.setCod(true);
	 		    order.setCreateTime(1451280407936L);
	 		    order.setCodAmount(new BigDecimal(11.0));
	 		    order.setCustCode("NHLDP089");
	 		    order.setCustOrderNo("huan100");
	 		    order.setCustomerName("换");
	 		    order.setDetails(null);
	 		    order.setFeight(new BigDecimal(11.0));
	 		    order.setGoGetReturnTime("");
	 		    order.setInfCarrierPickTaskSendId(0);
	 		    order.setIsCod(true);
	 		    order.setMobile("104104101");
	 		    order.setOrderDeliveryBatch("0");
	 		    order.setOrderSn("1020000133830");
	 		    order.setOrderSum(new BigDecimal(111.0));
	 		    order.setOrgCode("");
	 		    order.setOrgName("飞远-荔湾站2");
	 		    order.setOriginalVolume(new BigDecimal(111.0));
	 		    order.setOriginalWeight(new BigDecimal(111.0));
	 		    order.setPayment(-1);
	 		    order.setPostCode("");
	 		    order.setRecordVersion(0L);
	 		    order.setRequiredTime("");
	 		    order.setSalesPlatform("");
	 		    order.setStoreContacts("周欢");
	 		    order.setStoreTel("13570507226");
	 		    order.setStoreName("");
	 		    order.setTotalPack(0);
	 		    order.setTransportDay("0");
	 		    order.setTransportType("");
	 		    order.setValuationValue(new BigDecimal(11.0));
	 		    order.setVipClub(0);
	 		    order.setWarehouse("VIP_NH");
	 		    order.setWarehouseAddr("花海仓20号");
	 		    JSONArray jsonArray = JSONArray.fromObject(order);
	 		    String json = jsonArray.toString();
	 		    
	 		   JSONArray jsonobjArray = JSONArray.fromObject(json);
	 		   List<TPSOrder> list = (List<TPSOrder>)JSONArray.toCollection(jsonobjArray,TPSOrder.class); 
		        @SuppressWarnings({ "deprecation", "rawtypes" })
		        msg = new String(e.getPayload(), "utf-8");
	            this.logger.info("消费下发承运商订单状态接口表的物流状态信息接收到报文：" + msg);
	            TPSOrder order1 = (TPSOrder) list.get(0);
	            //InfCarrierOrderStatusTransportTrackVo trackVo = vo.getItem();
	            if (null == order1) {
	                this.logger.error("消费下发承运商订单状态接口表的物流状态信息异常：item部分为空");
	            }
	            if ((order1 == null)) {
	    			this.logger.info("请求TPS自动化订单信息-获取订单信息失败!");
	    			return;
	    		}
	    		if(order1.getBusinessType()==20 || order1.getBusinessType()==40){
	    			this.extractedOXODataImport(order1,vipshop);
	    		}else{
	    			//普通接口数据导入
	    			if(null!=order1){
	    				//返回的报文订单信息解析
	    				CwbOrderDTO cwbOrder = this.parseXmlDetailInfo(vipshop,order1);
	    				//是否开启托运单模式，生成多个批次 0 不开启
	    				if (vipshop.getIsTuoYunDanFlag() == 0) {
	    					//普通单在没有开启托运单模式下，数据插入临时表
	    					this.extractedDataImport(vipshop_key, vipshop, cwbOrder);
	    				} else {
	    					//普通单在开启托运单模式下，数据插入临时表
	    					this.extractedDataImportByEmaildate(vipshop_key, vipshop, cwbOrder);
	    				}
	    			}
	    		}
	        }catch(Exception e){
	        	e.printStackTrace();
	        }
		}*/

	
	/**
	 * 订单报文实体 转换 为 CwbOrderDTO
	 * @param cwbOrder
	 * @param order
	 * @param vipshop
	 */
	private void convertOrderVoToCwbOrderDTO(CwbOrderDTO cwbOrder,TPSOrder order,VipShop vipshop){
		if(StringUtils.isNotBlank(order.getBusinessType().toString())){
			if(order.getBusinessType()==20){
				cwbOrder.setCwbordertypeid(CwbOrderTypeIdEnum.OXO_JIT.getValue());
			}
			else if(order.getBusinessType()==40){
				cwbOrder.setCwbordertypeid(CwbOrderTypeIdEnum.OXO.getValue());
			}
			else if(order.getBusinessType()==30){//保留类型
				//TODO保留类型
				cwbOrder.setCwbordertypeid(4);//default OXO
			}else{
				cwbOrder.setCwbordertypeid(4); //default OXO
			}
		}
		cwbOrder.setRemark1(order.getBrandName()+"&"+order.getStoreName()+"&"+order.getStoreContacts()+"&"+order.getStoreTel());//品牌商&提货门店&提货联系人&提货联系方式
		cwbOrder.setRemark2(order.getCustomerName());//订单客户，还不确定存到哪个字段 
		cwbOrder.setRemark3(order.getValuationValue().toString());//保价价值 ,新增字段
		cwbOrder.setRemark4(order.getCnorProv()+"&"+order.getCnorCity()+"&"+order.getCnorRegion()+"&"+order.getCnorAddr());//发货省，市，区，详细地址
		cwbOrder.setRemark5(order.getWarehouse()+"&"+order.getWarehouseAddr());//仓库+仓库地址 
	
		cwbOrder.setCustomercommand(order.getRequiredTime());//要求提货时间
		cwbOrder.setTranscwb(order.getOrderSn());//TMS订单号
		cwbOrder.setCwb(order.getCustOrderNo()); //订单号，tps运单号
		cwbOrder.setConsigneename(order.getBuyerName());//收件人
		cwbOrder.setConsigneeaddress(order.getBuyerAddress());//收件人地址
		cwbOrder.setConsigneephone(order.getTel());//收件人电话
		cwbOrder.setConsigneemobile(order.getMobile());//收件人手机
		cwbOrder.setCargoamount(order.getOrderSum());//商品价格
		cwbOrder.setReceivablefee(order.getCodAmount());
		if(StringUtils.isNotBlank(order.getPayment().toString())){
			if(order.getPayment()==-1){ //非货到付款
				cwbOrder.setPaywayid(PaytypeEnum.Pos.getValue());
				cwbOrder.setNewpaywayid(PaytypeEnum.Pos.getValue()+"");
			}
			
			if(order.getPayment()==0){//货到付款现金支付
				cwbOrder.setPaywayid(PaytypeEnum.Xianjin.getValue());
				cwbOrder.setNewpaywayid(PaytypeEnum.Xianjin.getValue()+"");
			}
			
			if(order.getPayment()==1){//货到付款刷卡支付
				cwbOrder.setPaywayid(PaytypeEnum.Pos.getValue());
				cwbOrder.setNewpaywayid(PaytypeEnum.Pos.getValue()+"");
			}
			
			if(order.getPayment()==2){//货到付款支付宝 支付
				cwbOrder.setPaywayid(PaytypeEnum.CodPos.getValue());
				cwbOrder.setNewpaywayid(PaytypeEnum.CodPos.getValue()+"");
			}
			
		}else{
			cwbOrder.setPaywayid(PaytypeEnum.Xianjin.getValue());
			cwbOrder.setNewpaywayid(PaytypeEnum.Xianjin.getValue()+"");
		}
		cwbOrder.setConsigneepostcode(order.getPostCode());//收件人邮政编码
		cwbOrder.setCargorealweight(order.getOriginalWeight());//重量
		cwbOrder.setCargovolume(order.getOriginalVolume()); //体积
		
		cwbOrder.setBackcargoname("");
		
		try {
			//发货件数
			cwbOrder.setSendcargonum((order.getTotalPack()==null || order.getTotalPack()==0?1:order.getTotalPack()));//箱数
		} catch (Exception e) {
			// TODO Auto-generated catch block
			cwbOrder.setSendcargonum("1");
		}
		
		//orderMap.put("", order.getSalesPlatform());//销售平台，不确定要不要存
		cwbOrder.setCargotype(order.getTransportType());//品类
		
		cwbOrder.setShouldfare(order.getFeight());//运费
		//orderMap.put("", order.getOrgName()); //提货站点名称
		//orderMap.put("", order.getOrgCode());//提货站点编码
		cwbOrder.setCustomerid(Long.valueOf(vipshop.getCustomerids())); //客户id
		//cwbOrder.setStartbranchid(vipshop.getWarehouseid());
		//团购标识
		cwbOrder.setVipclub(order.getVipClub().equals("3")?1:0);
		//tps运单号	
		cwbOrder.setTpsTranscwb(order.getOrderSn());
	}
	
	/**
	 * 订单报文实体转换为 map
	 * @param order
	 * @param vipshop
	 * @return
	 */
	private Map<String,String> convertOrderVoToMap(TPSOrder order){
		Map<String,String> orderMap = new HashMap<String,String>();
		orderMap.put("consigneename", order.getBuyerName());
		orderMap.put("sendcarnum", order.getTotalPack().toString());
		orderMap.put("consigneemobile", order.getMobile());
		orderMap.put("consigneephone", order.getTel());
		orderMap.put("consigneepostcode", order.getPostCode());
		
		orderMap.put("consigneeaddress", order.getBuyerAddress());
		orderMap.put("receivablefee", order.getCodAmount().toString());
		orderMap.put("customercommand", order.getRequiredTime());
		orderMap.put("remark1", order.getBrandName()+"&"+order.getStoreName()+"&"+order.getStoreContacts()+"&"+order.getStoreTel());
		orderMap.put("remark2", order.getCustomerName());
		orderMap.put("remark3", order.getValuationValue().toString());
		orderMap.put("remark4", order.getCnorProv()+"&"+order.getCnorCity()+"&"+order.getCnorRegion()+"&"+order.getCnorAddr());
		
		orderMap.put("remark5", order.getWarehouse()+"&"+order.getWarehouseAddr());
		orderMap.put("cargorealweight", order.getOriginalWeight().toString());
		
		if(null!=order.getPayment()){
			if(order.getPayment().intValue()==-1){ //非货到付款
				orderMap.put("paywayid", "" + PaytypeEnum.Qita.getValue());
			}
			
			if(order.getPayment().intValue()==0){//货到付款现金支付
				orderMap.put("paywayid", "" + PaytypeEnum.Xianjin.getValue());
			}
			
			if(order.getPayment().intValue()==1){//货到付款刷卡支付
				orderMap.put("paywayid", "" + PaytypeEnum.Pos.getValue());
			}
			
			if(order.getPayment().intValue()==2){//货到付款支付宝 支付
				orderMap.put("paywayid", "" + PaytypeEnum.CodPos.getValue());
			}
		}
		
		if(StringUtils.isBlank(order.getPayment().toString())){
			orderMap.put("paywayid", "" + PaytypeEnum.Qita.getValue());
		}
		orderMap.put("cargotype", order.getTransportType());
		if(null!=order.getBusinessType()){
			if(order.getBusinessType()==20){
				orderMap.put("cwbordertypeid", CwbOrderTypeIdEnum.OXO_JIT.getValue()+"");
			}
			else if(order.getBusinessType()==40){
				orderMap.put("cwbordertypeid", CwbOrderTypeIdEnum.OXO.getValue()+"");
			}
			else{
				orderMap.put("cwbordertypeid", ""+4);
			}
		}
		orderMap.put("shouldfare", order.getFeight().toString());
		orderMap.put("cwb", order.getCustOrderNo());
		
		return orderMap;
		
	}

	/**
	 * OXO数据插入到临时表
	 * @param orders
	 * @throws Exception 
	 */
	@Transactional
	public void extractedOXODataImport(TPSOrder order,VipShop vipshop){
	
		long warehouse_id = vipshop.getWarehouseid(); // 获取虚拟库房
		long warehouseid = warehouse_id != 0 ? warehouse_id : dataImportService_B2c.getTempWarehouseIdForB2c(); // 获取虚拟库房
		long customerid = Long.valueOf(vipshop.getCustomerids()); //客户id
		EmailDate ed = dataImportService.getOrCreateEmailDate(Long.valueOf(vipshop.getCustomerids()), 0, warehouseid); //生成批次
		User user = new User();
		user.setUserid(1);
		user.setBranchid(warehouseid);
		try{
			/**
			 * 处理新增命令 逻辑
			 */
			if("new".equalsIgnoreCase(order.getCmdType())){
				//如果临时表中已存在对应记录 则 continue
				if(dataImportDAO_B2c.getCwbByCwbB2ctemp(order.getCustOrderNo()) != null){
					logger.warn("接收到VipShop_OXO发来cmd_type='new'指令，cwb="+order.getCustOrderNo()+"。但express_ops_cwb_detail_b2ctemp表中已存在对应记录，系统将不做任何操作。");
					return;
				}
				CwbOrderDTO cwbOrder = new CwbOrderDTO();
				convertOrderVoToCwbOrderDTO(cwbOrder,order,vipshop);
				dataImportDAO_B2c.insertCwbOrder_toTempTable(cwbOrder, customerid, warehouseid, user, ed);

			}
			/**
			 * 处理编码命令 逻辑
			 */
			else if("edit".equalsIgnoreCase(order.getCmdType())){
				CwbOrderDTO cwbOrder_temp = dataImportDAO_B2c.getCwbByCwbB2ctemp(order.getCustOrderNo());
				if(cwbOrder_temp == null ){//如果临时表还不存在 指定 cwb的订单数据 ，则continue
					logger.warn("接收到VipShop_OXO发来cmd_type='edit'指令，cwb="+order.getCustOrderNo()+"。但未在express_ops_cwb_detail_b2ctemp表中找到对应记录，系统将不做任何操作。");
					return;
				}
				
				if(cwbOrder_temp.getGetDataFlag() == 0){//如果临时表数据没有同步到了主表，只需修改临时表数据
					//update 临时表
					dataImportDAO_B2c.updateBycwb(this.convertOrderVoToMap(order));
					return;
				}
				
				if(cwbOrder_temp.getGetDataFlag() != 0){//如果临时表数据已经同步到了主表,修改主表数据
					CwbOrder cwbOrder_biz = cwbDAO.getCwbByCwb(order.getCustOrderNo());
					if(cwbOrder_biz != null){
						//如果已揽收成功，不允编辑
						if(cwbOrder_biz.getOxopickstate() == CwbOXOStateEnum.Processed.getValue()){
							logger.warn("接收到VipShop_OXO发来cmd_type='edit'指令，cwb="+order.getCustOrderNo()+"。但express_ops_cwb_detail表中该记录的揽收状态为 已处理，系统将不做修改操作。");
							return;
						}
						Map<String,String> orderMap  = this.convertOrderVoToMap(order);
						cwbDAO.updateBycwb(orderMap);							
						//如果发货地址 和 收货地址发生了变化，需要重新地址解析
						if(StringUtils.isNotBlank(orderMap.get("remark4")) && !orderMap.get("remark4").equals(cwbOrder_biz.getRemark4())){
							//重发解析揽件站点
							HashMap<String, Object> map = new HashMap<String, Object>();
							map.put("cwb", cwbOrder_biz.getCwb());
							map.put("userid", "1");
							map.put("address", orderMap.get("remark4").replaceAll("&", ""));
							map.put("notifytype", 0);
							try{
								this.logger.info("消息发送端：addressmatch, header={}", map.toString());
								addressmatch.sendBodyAndHeaders(null, map);
							}catch(Exception e){
								logger.error("", e);
								//写MQ异常表
								this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".extractedOXODataImport")
										.buildExceptionInfo(e.toString()).buildTopic(this.addressmatch.getDefaultEndpoint().getEndpointUri())
										.buildMessageHeaderObject(map).getMqException());
							}
							
						}
						if(StringUtils.isNotBlank(orderMap.get("consigneeaddress")) && !orderMap.get("consigneeaddress").equals(cwbOrder_biz.getConsigneeaddress())){
							//重新解析派件站点
							HashMap<String, Object> map = new HashMap<String, Object>();
							map.put("cwb", cwbOrder_biz.getCwb());
							map.put("userid", "1");
							map.put("address", orderMap.get("consigneeaddress"));
							map.put("notifytype", 1);
							try{
								addressmatch.sendBodyAndHeaders(null, map);//解析派件站点
							}catch(Exception e){
								logger.error("", e);
								//写MQ异常表
								this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".extractedOXODataImport")
										.buildExceptionInfo(e.toString()).buildTopic(this.addressmatch.getDefaultEndpoint().getEndpointUri())
										.buildMessageHeaderObject(map).getMqException());
							}
						}
		
					}
				}
			}
			/**
			 * 处理取消命令 逻辑
			 */
			else if("cancel".equalsIgnoreCase(order.getCmdType())){
				CwbOrder cwbOrder_biz = cwbDAO.getCwbByCwb(order.getCustOrderNo());
				if(cwbOrder_biz != null){
					//如果已揽收成功，不允取消
					if(cwbOrder_biz.getOxopickstate() == CwbOXOStateEnum.Processed.getValue()){
						logger.warn("接收到VipShop_OXO发来cmd_type='cancel'指令，cwb="+order.getCustOrderNo()+"。但express_ops_cwb_detail表中该记录的揽收状态为 已处理，系统将不做取消操作。");
					}
				}
				dataImportDAO_B2c.dataLoseB2ctempByCwb(order.getCustOrderNo());
				this.cwbDAO.dataLoseByCwb(order.getCustOrderNo());
				cwbOrderService.datalose_vipshop(order.getCustOrderNo());

                // added by Steve PENG 20160722 start TPS自动化 OXO, 订单失效后，需要对派费操作
                dfFeeService.saveFeeRelativeAfterOrderDisabled(order.getCustOrderNo());
                // added by Steve PENG 20160722 end
			}
		}catch(Exception e){
			logger.error("VIP_OXO数据插入临时表发生未知异常cwb=" + order.getCustOrderNo(), e);
			CwbException ex=null;
			long flowordertye=FlowOrderTypeEnum.DaoRuShuJu.getValue();
			if(e instanceof CwbException){
				flowordertye=((CwbException)e).getFlowordertye();
				ex=(CwbException)e;
			}else{
				ex=new CwbException(order.getCustOrderNo(),flowordertye,e.getMessage());
			}
			throw ex;
		}
	}
	
	/**
	 * 验证返回的sign
	 *
	 * @param paseSign
	 * @return
	 */
	public boolean checkSignResponseInfo(String paseSign, String MD5Str) {
		if (paseSign.equals(MD5Str)) {
			return true;
		}
		return true;
	}

	//普通单在没有开启托运单模式下，数据插入临时表
	@Transactional
	public void extractedDataImport(int vipshop_key, VipShop vipshop, CwbOrderDTO order) {
		long customerid = Long.valueOf(order.getCustomerid());
		try {
			long warehouseid = vipshop.getWarehouseid();
			//long ewarehouseid = warehouseid == 0 ? dataImportService_B2c.getTempWarehouseIdForB2c() : warehouseid;
			//开启以出仓时间作为批次标记
			String emaildate = order.getRemark2();
			 
			//Added by leoliao at 2016-03-09 如果传过来的出仓时间为空，则使用当前日期作为批次时间
			if(emaildate == null || emaildate.trim().equals("")){
				emaildate = DateTimeUtil.getNowDate() + " 00:00:00";
				order.setRemark2(emaildate);
			}

			//导入批次表(express_ops_emaildate)emaildatetime使用当天第一条进入DMP系统订单的创建时间
			EmailDate ed = dataImportService.getOrCreateEmailDate(customerid, 0, warehouseid);
			//临时表(express_ops_cwb_detail_b2ctemp)和正式表(express_ops_cwb_detail)的emaildate(发货时间)取TMS的出仓时间
			ed.setEmaildatetime(emaildate);
			//数据导入系统入口
			tPSOrderImportService_B2c.Analizy_DataDealByB2c(customerid, B2cEnum.VipShop_TPSAutomate.getMethod(), order, warehouseid,ed);
			this.logger.info("TPS自动化普通单在没有开启托运单模式下，数据插入临时表处理成功！");
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("TPS自动化普通单在没有开启托运单模式下，数据插入临时表异常,cwb=" + order.getCwb() + "message:", e);
			CwbException ex=null;
			long flowordertye=FlowOrderTypeEnum.DaoRuShuJu.getValue();
			if(e instanceof CwbException){
				flowordertye=((CwbException)e).getFlowordertye();
				ex=(CwbException)e;
			}else{
				ex=new CwbException(order.getCwb(),flowordertye,e.getMessage());
			}
			throw ex;
		}
	}
	
	//普通单在开启托运单模式下，数据插入临时表
	@Transactional
	public void extractedDataImportByEmaildate(int vipshop_key,
			VipShop vipshop, CwbOrderDTO order) throws Exception {
		long customerid = Long.valueOf(order.getCustomerid());
		try {
			String emaildate = order.getRemark4();
			if(emaildate == null || emaildate.trim().equals("")){
				emaildate = DateTimeUtil.getNowDate() + " 00:00:00";				
				order.setRemark4(emaildate);
			}
			long warehouseid = vipshop.getWarehouseid();
			long ewarehouseid = warehouseid == 0 ? dataImportService_B2c.getTempWarehouseIdForB2c() : warehouseid;
			EmailDate ed = dataImportService.getEmailDate_B2CByEmaildate(customerid, 0, ewarehouseid, emaildate);
			//数据导入系统入口
			tPSOrderImportService_B2c.Analizy_DataDealByB2c(customerid, B2cEnum.VipShop_TPSAutomate.getMethod(), order, warehouseid, ed);
			this.logger.error("TPS自动化普通单在开启托运单模式下，数据插入临时表成功!cwb=" + order.getCwb());
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("TPS自动化普通单在开启托运单模式下，数据插入临时表异常!cwb=" + order.getCwb(), e);
			CwbException ex=null;
			long flowordertye=FlowOrderTypeEnum.DaoRuShuJu.getValue();
			if(e instanceof CwbException){
				flowordertye=((CwbException)e).getFlowordertye();
				ex=(CwbException)e;
			}else{
				ex=new CwbException(order.getCwb(),flowordertye,e.getMessage());
			}
			throw ex;
		}
	}
	
	//更新订单临时表，订单表，并获取接口传过来的相应订单信息
	public CwbOrderDTO parseXmlDetailInfo(VipShop vipshop, TPSOrder order,int mpsswitch) {
		/*String b2cFlag = B2cEnum.VipShop_TPSAutomate.getMethod();
		ExcelColumnSet excelColumnSet = cwbColumnSet.getEexcelColumnSetByB2cJoint(b2cFlag);*/
		CwbOrderDTO orderDTO = new CwbOrderDTO();
		String cust_order_no = null;
		//CwbOrderDTO objOrder = null;
		try {
			String order_sn = order.getOrderSn();
			cust_order_no = order.getCustOrderNo();
			String transport_day = order.getTransportDay();
			String add_time = this.toDateForm(order.getAddTime());// 出仓时间
			Integer total_pack = order.getTotalPack(); // 新增箱数
			String cargotype = order.getTransportType();
			String sendcarnum = order.getTotalPack().toString();
			/**
			 * 新增参数
			 */
			int ext_pay_type = (null==order.getPayment()||"".equals(order.getPayment().toString())) ? 0 : order.getPayment(); // 扩展支付方式
			int paywayid = (ext_pay_type==1) ? PaytypeEnum.Pos.getValue() : PaytypeEnum.Xianjin.getValue();
			String created_dtm_loc = this.toDateForm(order.getCreateTime());//记录生成时间
			String order_delivery_batch = order.getOrderDeliveryBatch(); // 1（默认）-一配订单：2-二配订单
			if ("1".equals(order_delivery_batch)) {
				order_delivery_batch = "一配订单";
			} else if ("2".equals(order_delivery_batch)) {
				order_delivery_batch = "二配订单";
			} else {
				order_delivery_batch = "普通订单";
			}
			BigDecimal feight = order.getFeight(); // 上门退运费
			//20：OXO-JIT,30：配送,40：OXO直送,60：上门退
			int business_type = order.getBusinessType();
			String cwbordertype = (business_type==60) ? String.valueOf(CwbOrderTypeIdEnum.Shangmentui.getValue()) : String.valueOf(CwbOrderTypeIdEnum.Peisong.getValue());
			BigDecimal original_weight = null;
			BigDecimal original_volume = null;
			if(business_type==30){
				original_weight = new BigDecimal("0");/**/; // 重量
				original_volume = new BigDecimal("0");/**/; // 体积
			}else{
				original_weight = (String.valueOf(order.getOriginalWeight()).equals("") ? new BigDecimal("0") : order.getOriginalWeight());
				original_volume = (String.valueOf(order.getOriginalVolume()).equals("") ? new BigDecimal("0") : order.getOriginalVolume());
			}
			String is_gatherpack = order.getIsGatherpack().toString(); //1：表示此订单需要承运商站点集包 0：表示唯品会仓库整单出仓
			String is_gathercomp = order.getIsGathercomp().toString(); //最后一箱:1最后一箱 ，0默认 
			
			String warehouse_addr = order.getWarehouseAddr(); // 仓库地址
			String pack_nos = order.getBoxNo();// 箱号
			String lastFetchTime = this.toDateForm(order.getLastFetchTime());//建议最晚揽件时间
			String go_get_return_time = order.getGoGetReturnTime(); //预约上门揽退时间
			String caramount = (null==order.getOrderSum()||"".equals(order.getOrderSum().toString())) ? "0" : order.getOrderSum().toString();
			
			// : 新建
			// edit:修改
			// cancel
			// : 删除
			// 客退上门揽件会有修改/删除
			String remarkFreight = "";
			
			if (cwbordertype.equals(String.valueOf(CwbOrderTypeIdEnum.Shangmentui.getValue()))) {
				int freight_d=feight.compareTo(BigDecimal.ZERO); //和0，Zero比较
				/*if(r==0) //等于
				if(r==1) //大于
				if(r==-1) //小于*/
				if (freight_d ==1) {
					remarkFreight = "现付";
				} else {
					remarkFreight = "到付";
				}
			}
			
			String transcwb=pack_nos!=null&&!pack_nos.isEmpty()?pack_nos:order_sn;
			String customer_name = order.getCustomerName();
			String customerid=vipshop.getCustomerids();  //默认选择唯品会customerid
			
			//Modified by leoliao at 2016-05-20
			//区分乐蜂订单逻辑如下：根据上游系统提供的接口数据字段do_type值来区分是否为乐蜂订单，当且仅当do_type值为1时，订单为乐蜂订单。
			int do_type = order.getDoType()==null?999 : order.getDoType().intValue();
			if(vipshop.getIsOpenLefengflag()==1){//开启乐蜂网
				//if((customer_name==null||customer_name.isEmpty()||!customer_name.contains("乐蜂"))&&!cwbordertype.equals(String.valueOf(CwbOrderTypeIdEnum.Shangmentui.getValue()))){
				if(do_type != 1 && !cwbordertype.equals(String.valueOf(CwbOrderTypeIdEnum.Shangmentui.getValue()))){
					this.logger.info("开启乐峰网标识,但非乐峰订单cwb={}则返回null!", cust_order_no);
					return null;
				}
			}
			
			//if((customer_name!=null&&customer_name.contains("乐蜂"))){
			if(do_type == 1){
				customerid = (vipshop.getLefengCustomerid()==null || vipshop.getLefengCustomerid().isEmpty()?vipshop.getCustomerids() : vipshop.getLefengCustomerid());
			}
			//Modified end
			
			orderDTO.setCwb(cust_order_no);
			orderDTO.setTranscwb(transcwb);
			orderDTO.setConsigneename(order.getBuyerName().isEmpty()?"":order.getBuyerName());
			if(is_gatherpack.trim().equals("1") && total_pack!=null && total_pack==0){
				int transcwbLength = transcwb.split(",").length;
				orderDTO.setSendcargonum(transcwbLength);
			}else{
				orderDTO.setSendcargonum(total_pack.toString().isEmpty() ? 1 : total_pack);
			}
			
			orderDTO.setConsigneephone(order.getTel());
			orderDTO.setConsigneemobile(order.getMobile());
			orderDTO.setConsigneepostcode(order.getPostCode());
			orderDTO.setConsigneeaddress(order.getBuyerAddress());
			orderDTO.setReceivablefee(order.getCodAmount());
			orderDTO.setCustomercommand("送货时间要求:" + transport_day + ",订单配送批次:" + order_delivery_batch + "," + remarkFreight + ",预约揽收时间："+go_get_return_time);
			orderDTO.setSendcargoname("[发出商品]");
			orderDTO.setCustomerid(Integer.parseInt(customerid));
			orderDTO.setRemark2(vipshop.getIsCreateTimeToEmaildateFlag()==1?add_time:created_dtm_loc);// 如果开启生成批次，则remark2是出仓时间，否则是订单生成时间
			orderDTO.setCargorealweight(original_weight);// 重量
			orderDTO.setCargovolume(original_volume);//体积
			orderDTO.setPaywayid(paywayid);// 支付方式
			orderDTO.setCargotype(cargotype); // 服务类别
			orderDTO.setRemark5(customer_name+"/"+warehouse_addr); // 仓库地址
			orderDTO.setCwbordertypeid(Integer.parseInt(cwbordertype));
			orderDTO.setShouldfare((null==feight) ? new BigDecimal(0) : feight);
			orderDTO.setCargoamount(caramount);
			orderDTO.setNewpaywayid(paywayid + "");
			orderDTO.setExcelbranch(orderDTO.getExcelbranch()==null?"":orderDTO.getExcelbranch());//站点
			orderDTO.setRemark1(orderDTO.getRemark1()==null?"":orderDTO.getRemark1());
			orderDTO.setRemark3(orderDTO.getRemark3()==null?"":orderDTO.getRemark3());
			orderDTO.setRemark4(orderDTO.getRemark4()==null?"":orderDTO.getRemark4());
			orderDTO.setIsaudit(orderDTO.getIsaudit());
			//团购标识
			orderDTO.setVipclub(order.getVipClub().equals("3")?1:0);
			//tps运单号	
			orderDTO.setTpsTranscwb(order_sn);
			//objOrder = this.getCwbOrderAccordingtoConf(excelColumnSet,orderDTO);
			orderDTO.setIsmpsflag(choseIsmpsflag(is_gatherpack,is_gathercomp,sendcarnum,mpsswitch));
			orderDTO.setMpsallarrivedflag(choseMspallarrivedflag(is_gathercomp,is_gatherpack,sendcarnum,mpsswitch));
			
			CwbOrderDTO cwbOrderDTO = dataImportDAO_B2c.getCwbB2ctempByCwb(cust_order_no);
			//集包相关代码处理
			mpsallPackage(vipshop, cust_order_no, is_gatherpack, is_gathercomp,pack_nos, total_pack, cwbOrderDTO,mpsswitch,orderDTO);
			
			String cmd_type = order.getCmdType(); // 操作指令new
			if (cwbordertype.equals(String.valueOf(CwbOrderTypeIdEnum.Shangmentui.getValue()))) {
				
				if ("edit".equalsIgnoreCase(cmd_type)) {
					//修改订单表
					this.tpsDataImportDAO_B2c.updateBycwb(orderDTO);
					//修改临时表
					this.tpsDataImportDAO_B2c.updateTempBycwb(orderDTO);
					return null;
				}
				// 订单取消
				if ("cancel".equalsIgnoreCase(cmd_type)) {
					
					if(vipshop.getCancelOrIntercept()==0){ //取消
						//cust_order_no订单号，根据订单号失效临时中订单数据
						this.dataImportDAO_B2c.dataLoseB2ctempByCwb(cust_order_no);
						// 根据订单号失效订单表中对应订单数据
						this.cwbDAO.dataLoseByCwb(cust_order_no);
						//根据订单号，删除对应商品表中商品数据
						orderGoodsDAO.loseOrderGoods(cust_order_no);
						//处理订单失效相关信息
						cwbOrderService.datalose_vipshop(cust_order_no);
						// add by bruce shangguan 20160608  报障编号:1729 ,揽退成功之后失效的订单在运费交款存在
						this.accountCwbFareDetailDAO.deleteAccountCwbFareDetailByCwb(cust_order_no) ;
						// end 20160608  报障编号:1729

                        // added by Steve PENG 20160722 start TPS自动化 上门退, 订单失效后，需要对派费操作
                        dfFeeService.saveFeeRelativeAfterOrderDisabled(cust_order_no);
                        // added by Steve PENG 20160722 end

					}else{ //拦截
						//cwbOrderService.auditToTuihuo(userDAO.getAllUserByid(1), order_sn, order_sn, FlowOrderTypeEnum.DingDanLanJie.getValue(),1);
						cwbOrderService.tuihuoHandleVipshop(userDAO.getAllUserByid(1), cust_order_no, cust_order_no,0);
					}
					return null;
				}

			}
			
			if (cwbordertype.equals(String.valueOf(CwbOrderTypeIdEnum.Shangmentui.getValue()))) {

				if ("new".equalsIgnoreCase(cmd_type)) {
					// 插入商品列表,try防止异常
					this.insertOrderGoods(order, cust_order_no);
				}
			}
			
			if (cwbOrderDTO != null ) {
				if(is_gatherpack.equals("0")){
					this.logger.info("获取唯品会订单有重复,已过滤...cwb={}", cust_order_no);
					return null;
				//集单模式校验重复
				}else if(is_gatherpack.equals("1")){
					return null;
					/*String oldTranscwb = cwbOrderDTO.getTranscwb();
					String currentTranscwb = transcwb;
					if(oldTranscwb.split(",").length==currentTranscwb.split(",").length){
						if(orderDTO.getMpsallarrivedflag()==VipGathercompEnum.Last.getValue()){
							return null;
						}
					}
					if(oldTranscwb.split(",").length>currentTranscwb.split(",").length){
						return null;
					}*/
				}
				
			}

			
			
			if ("".equals(cust_order_no)) { // 若订单号为空，则继续。
				this.logger.info("获取订单信息为空");
				return null;
			}
			this.logger.info("TPS自动化订单cwb={}", cust_order_no);
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("TPS自动化订单下载处理转订单对象异常,cwb=" + cust_order_no, e);
			CwbException ex=null;
			long flowordertye=FlowOrderTypeEnum.DaoRuShuJu.getValue();
			if(e instanceof CwbException){
				flowordertye=((CwbException)e).getFlowordertye();
				ex=(CwbException)e;
			}else{
				ex=new CwbException(order.getCustOrderNo(),flowordertye,e.getMessage());
			}
			throw ex;
		}
		return orderDTO;
	}
	
	private String toDateForm(Long data){
		String dataStr="";
		if(data!=null){
			Timestamp scurrtest = new Timestamp(data);
			dataStr = String.valueOf(scurrtest).substring(0,19);// 简易最迟配送时间
		}
		return dataStr;
	}
	
	//将接收的货物信息存入商品表
	private void insertOrderGoods(TPSOrder order, String cust_order_no) {
		try {
			List<TPSOrderDetails> goodslist = (List<TPSOrderDetails>) order.getDetails();
			if ((goodslist != null) && (goodslist.size() > 0)) {
				List<OrderGoods> orderGoodsList = null;
				orderGoodsList = orderGoodsDAO.getOrderGoodsList(cust_order_no);
				if(orderGoodsList.size()!=0){
					return;
				}
				for (TPSOrderDetails good : goodslist) {
					OrderGoods ordergoods = new OrderGoods();
					ordergoods.setCwb(cust_order_no);
					ordergoods.setCretime(DateTimeUtil.getNowTime());
					ordergoods.setGoods_brand(good.getGoodsBrand());
					ordergoods.setGoods_code(good.getGoodsCode());
					ordergoods.setGoods_name(good.getGoodsName());
					ordergoods.setGoods_num(good.getGoodsNum().toString());
					ordergoods.setGoods_pic_url(good.getGoodsPicUrl());
					ordergoods.setGoods_spec(good.getGoodsSize());
					ordergoods.setReturn_reason(good.getRemark());
					ordergoods.setCretime(DateTimeUtil.getNowTime());
					this.orderGoodsDAO.CreateOrderGoods(ordergoods);

				}
			}
		} catch (Exception e) {
			this.logger.error("获取商品列表异常,单号=" + cust_order_no, e);
		}
	}
	
	//根据订单模板字段获取对象
	public CwbOrderDTO getCwbOrderAccordingtoConf(ExcelColumnSet excelColumnSet, CwbOrderDTO order) throws Exception {
		CwbOrderDTO cwbOrder = new CwbOrderDTO();
		cwbOrder.setCwb(order.getCwb());// 订单号
		if (excelColumnSet.getTranscwbindex() != 0) {
			cwbOrder.setTranscwb(order.getTranscwb());
			;// 运单号
		}
		if (excelColumnSet.getConsigneenameindex() != 0) {
			cwbOrder.setConsigneename(order.getConsigneename());// 收件人名称
		}
		if (excelColumnSet.getConsigneeaddressindex() != 0) {
			cwbOrder.setConsigneeaddress(order.getConsigneeaddress());// 收件人地址
		}
		if (excelColumnSet.getConsigneepostcodeindex() != 0) {
			cwbOrder.setConsigneepostcode(order.getConsigneepostcode());// 收件人邮编
		}
		if (excelColumnSet.getConsigneephoneindex() != 0) {
			cwbOrder.setConsigneephone(order.getConsigneephone());// 收件人电话
		}
		if (excelColumnSet.getSendcargonameindex() != 0) {
			cwbOrder.setSendcargoname(order.getSendcargoname());// 发出商品名称
		}
		if (excelColumnSet.getBackcargonameindex() != 0) {
			cwbOrder.setBackcargoname(order.getBackcargoname());
		}
		if (excelColumnSet.getReceivablefeeindex() != 0) {
			cwbOrder.setReceivablefee(order.getReceivablefee());// 代收货款应收金额
		}
		if (excelColumnSet.getPaybackfeeindex() != 0) {
			cwbOrder.setPaybackfee(order.getPaybackfee());// 上门退货应退金额
		}
		if (excelColumnSet.getCargorealweightindex() != 0) {
			cwbOrder.setCargorealweight(order.getCargorealweight());// 货物重量kg
		}
		if (excelColumnSet.getExcelbranchindex() != 0) {
			cwbOrder.setExcelbranch(order.getExcelbranch() == null ? "" : order.getExcelbranch());// 站点
		}
		if (excelColumnSet.getCwbremarkindex() != 0) {
			cwbOrder.setCwbremark(order.getCwbremark());// 订单备注
		}

		if (excelColumnSet.getEmaildateindex() != 0) {
			cwbOrder.setEmaildate(order.getEmaildate());// 发货时间Id
		} else {
			cwbOrder.setEmaildate(DateTimeUtil.getNowTime());
		}

		if (excelColumnSet.getConsigneenoindex() != 0) {
			cwbOrder.setConsigneeno(order.getConsigneeno());// 收件人编号
		}

		if (excelColumnSet.getCargoamountindex() != 0) {
			cwbOrder.setCargoamount(order.getCargoamount());// 货物金额
		}
		if (excelColumnSet.getCustomercommandindex() != 0) {
			cwbOrder.setCustomercommand(order.getCustomercommand());// 客户要求
		}
		if (excelColumnSet.getCargotypeindex() != 0) {

			cwbOrder.setCargotype(order.getCargotype());// 货物类别
		}
		if (excelColumnSet.getCargosizeindex() != 0) {
			cwbOrder.setCargosize(order.getCargosize());// 商品尺寸
		}
		if (excelColumnSet.getBackcargoamountindex() != 0) {
			cwbOrder.setBackcargoamount(order.getCargoamount());
		}
		if (excelColumnSet.getDestinationindex() != 0) {
			cwbOrder.setDestination(order.getDestination());// 目的地
		}
		if (excelColumnSet.getTranswayindex() != 0) {
			cwbOrder.setTransway(order.getTranscwb());// 运输方式
		}

		if (excelColumnSet.getSendcargonumindex() != 0) {
			cwbOrder.setSendcargonum(order.getSendcargonum());// 发货数量
		}
		if (excelColumnSet.getCommoncwb() != 0) {
			cwbOrder.setCommoncwb(order.getCommoncwb());// commoncwb快乐购
		}
		if (excelColumnSet.getBackcargonumindex() != 0) {
			cwbOrder.setBackcargonum(order.getBackcargonum());// 取货数量
		}
		if (excelColumnSet.getCwbordertypeindex() != 0) {
			cwbOrder.setCwbordertypeid(order.getCwbordertypeid());// 订单类型（1配送
		} else {
			cwbOrder.guessCwbordertypeid();
		}

		if (excelColumnSet.getPaywayindex() != 0) {
			cwbOrder.setPaywayid(order.getPaywayid());
			cwbOrder.setNewpaywayid(String.valueOf(order.getPaywayid()));
		} else {
			cwbOrder.guessPaywayid();
			cwbOrder.setNewpaywayid(PaytypeEnum.Xianjin.getValue() + "");
		}
		if (excelColumnSet.getCwbdelivertypeindex() != 0) {
			cwbOrder.setCwbdelivertypeid(order.getCwbdelivertypeid());
		}
		if (excelColumnSet.getCwbprovinceindex() != 0) {
			cwbOrder.setCwbprovince(order.getCwbprovince());
		}
		if (excelColumnSet.getCwbcityindex() != 0) {
			cwbOrder.setCwbcity(order.getCwbcity());
		}
		if (excelColumnSet.getCwbcountyindex() != 0) {
			cwbOrder.setCwbcounty(order.getCwbcounty());
		}
		if (excelColumnSet.getConsigneemobileindex() != 0) {
			cwbOrder.setConsigneemobile(order.getConsigneemobile());
		}

		if (excelColumnSet.getShipcwbindex() != 0) {
			cwbOrder.setShipcwb(order.getShipcwb());
		}
		if (excelColumnSet.getWarehousenameindex() != 0) {
			cwbOrder.setCustomerwarehouseid(order.getCustomerwarehouseid());

		}
		if (excelColumnSet.getIsaudit() != 0) {
			cwbOrder.setIsaudit(String.valueOf(order.getIsaudit()) == null ? 0 : order.getIsaudit());
		}
		//cwbOrder.setStartbranchid(branchid);

		if (excelColumnSet.getModelnameindex() != 0) {
			cwbOrder.setModelname(order.getModelname());
		}

		if (excelColumnSet.getCargovolumeindex() != 0) {
			cwbOrder.setCargovolume(order.getCargovolume());
		}
		if (excelColumnSet.getConsignoraddressindex() != 0) {
			cwbOrder.setConsignoraddress(order.getConsignoraddress());
		}
		if (excelColumnSet.getTmall_notifyidindex() != 0) {
			cwbOrder.setTmall_notifyid(order.getTmall_notifyid());
		}

		if (excelColumnSet.getMulti_shipcwbindex() != 0) {
			cwbOrder.setMulti_shipcwb(order.getMulti_shipcwb());
		}

		if (excelColumnSet.getRemark1index() != 0) {
			cwbOrder.setRemark1(order.getRemark1() == null ? "" : order.getRemark1());
		}
		if (excelColumnSet.getRemark2index() != 0) {
			cwbOrder.setRemark2(order.getRemark2() == null ? "" : order.getRemark2());
		}
		if (excelColumnSet.getRemark3index() != 0) {
			cwbOrder.setRemark3(order.getRemark3() == null ? "" : order.getRemark3());
		}
		if (excelColumnSet.getRemark4index() != 0) {
			cwbOrder.setRemark4(order.getRemark4() == null ? "" : order.getRemark4());
		}
		if (excelColumnSet.getRemark5index() != 0) {
			cwbOrder.setRemark5(order.getRemark5() == null ? "" : order.getRemark5());
		}

		if (excelColumnSet.getCwbordertypeidindex() != 0) {
			cwbOrder.setCwbordertypeid((String.valueOf(order.getCwbordertypeid())) == null ? 1 : order.getCwbordertypeid());
		}

		if (excelColumnSet.getShouldfareindex() != 0) {
			BigDecimal b = new BigDecimal(0);
			cwbOrder.setShouldfare(String.valueOf(order.getShouldfare()) == null ? b : order.getShouldfare());
		}
		cwbOrder.setDefaultCargoName();

		return cwbOrder;
	}
	
	//集单逻辑
	private void  mpsallPackage(VipShop vipshop, String cust_order_no,
			String is_gatherpack, String is_gathercomp, String pack_nos,
			int total_pack, CwbOrderDTO cwbOrderDTO,int mpsswitch,
			CwbOrderDTO currentOrderDTO) {
		if(mpsswitch==MpsswitchTypeEnum.WeiKaiQiJiDan.getValue()){
			return;
		}
		if(!"1".equals(is_gatherpack)){ //是否集包模式
			return;
		}
		//订单不存在，则不需要处理
		if(cwbOrderDTO==null){
			return;
		}
		
		/**更新发货时间
		 * 产品层面要改成的是这样的：
		 * 如果TMS推过来的数据有最后一件标志了就把发货时间写入订单表里面&运单表，
		 * 如果TMS推过来的数据没有最后一件标志那就把发货时间写到运单表里面
		 */
		String emaildate = currentOrderDTO.getRemark2(); //paraMap.get("remark2"); //发货时间
		String[] arrTranscwb = pack_nos.split(",");
		if("1".equals(is_gatherpack) && (arrTranscwb.length==total_pack)){
			//更新临时表的发货时间
			dataImportDAO_B2c.update_CwbDetailTempEmaildateByCwb(cust_order_no, emaildate);
			
			//把发货时间写入订单表
			cwbDAO.updateEmaildate(cust_order_no, emaildate);
			
			//把发货时间写入运单表
			if(arrTranscwb != null && arrTranscwb.length > 0){
				dataImportService.updateEmaildate(Arrays.asList(arrTranscwb), emaildate);
			}
		}else if("1".equals(is_gatherpack) && arrTranscwb.length!=total_pack){
			//把发货时间写入运单表
			if(arrTranscwb != null && arrTranscwb.length > 0){
				dataImportService.updateEmaildate(Arrays.asList(arrTranscwb), emaildate);
			}
		}
		//更新发货时间结束
		
		//一票多件，并且到齐了，排重returen
		if(cwbOrderDTO!=null&&cwbOrderDTO.getMpsallarrivedflag()==MPSAllArrivedFlagEnum.YES.getValue()){
			return;
		}
		
		/**
		     兼容以下情况，故不做拦截而是改为一票一件：
	            先产生is_gatherpack=1,is_gathercomp=0,total_pack=1的订单数据。
	           后面又产生一条is_gatherpack=1,is_gathercomp=1,total_pack=1的订单数据
	   **/
		if("1".equals(is_gatherpack)&&"1".equals(is_gathercomp)&&"1".equals(total_pack)){
			dataImportDAO_B2c.updateTmsPackageCondition(cust_order_no, pack_nos, Integer.valueOf(total_pack), MPSAllArrivedFlagEnum.NO.getValue(), MpsTypeEnum.PuTong.getValue());
			if(cwbOrderDTO.getGetDataFlag() != 0){
				cwbDAO.updateTmsPackageCondition(cust_order_no, pack_nos, Integer.valueOf(total_pack), MPSAllArrivedFlagEnum.NO.getValue(),MpsTypeEnum.PuTong.getValue());
			}
			return;
		}
		
		/*
		 * 由于tps下发dmp数据顺序可能错乱，在is_gathercomp标志为1（获取已集齐）之前，total_pack值都为0，
		 * 在is_gathercomp标志为1及之后，total_pack值都为订单总箱数
		 */
		int mpsallarrivedflag=0;
		if(arrTranscwb.length==total_pack){ //到齐
			mpsallarrivedflag=MPSAllArrivedFlagEnum.YES.getValue();
		}
		
		String oldTranscwb = cwbOrderDTO.getTranscwb();
		String currentTranscwb = currentOrderDTO.getTranscwb();
		//后者大于前者，覆盖前者
		if(oldTranscwb.split(",").length<currentTranscwb.split(",").length){
			this.changeInfoOfOrder(cwbOrderDTO,cust_order_no,pack_nos,
					currentOrderDTO.getSendcargonum(),mpsallarrivedflag);
		}
		//后者==前者，移除不是最后一箱的
		if(oldTranscwb.split(",").length==currentTranscwb.split(",").length){
			if((Integer.valueOf(currentOrderDTO.getMpsallarrivedflag())==VipGathercompEnum.Default.getValue()
					&&Integer.valueOf(cwbOrderDTO.getMpsallarrivedflag())==VipGathercompEnum.Default.getValue())
					||Integer.valueOf(currentOrderDTO.getMpsallarrivedflag())==VipGathercompEnum.Last.getValue()){
				this.changeInfoOfOrder(cwbOrderDTO,cust_order_no,pack_nos,
						currentOrderDTO.getSendcargonum(),mpsallarrivedflag);
			}
		}
		//后者小于前者，不做修改
		if(oldTranscwb.split(",").length>currentTranscwb.split(",").length){
			return;
		}
	}
	
	//更行临时表或者订单表集单相关信息
	private void changeInfoOfOrder(CwbOrderDTO cwbOrderDTO,String cust_order_no,String pack_nos,
			int total_pack,int mpsallarrivedflag){
		long b2cTempOpscwbid = cwbOrderDTO.getOpscwbid();
		dataImportDAO_B2c.updateTmsPackageCondition(b2cTempOpscwbid, pack_nos, Integer.valueOf(total_pack), mpsallarrivedflag, IsmpsflagEnum.yes.getValue());
		dataImportDAO_B2c.update_CwbDetailTempByCwb(0, b2cTempOpscwbid);
	}
	
	private int choseIsmpsflag(String is_gatherpack,String is_gathercomp,String sendcarnum,int mpsswitch) {
		int ismpsflag=IsmpsflagEnum.no.getValue(); //'是否一票多件(集包模式)：0默认；1是一票多件'; 
		
		//开启集单，并且运单为多个，则默认为一票多件
		if(mpsswitch!=MpsswitchTypeEnum.WeiKaiQiJiDan.getValue()){
			
			//拦截 开启集单模式，总件数只有一件的数据 is_gatherpack=1，is_gathercomp=1，total_pack=1  --->这就是个单包裹(罗冬确认)
			if("1".equals(is_gatherpack)&&"1".equals(is_gathercomp)&&"1".equals(sendcarnum)){
				return IsmpsflagEnum.no.getValue();
			}
			if("1".equals(is_gatherpack)&&Long.valueOf(sendcarnum)>1){
				return IsmpsflagEnum.yes.getValue();
			}
			if("1".equals(is_gatherpack)&&"0".equals(is_gathercomp)){
				return IsmpsflagEnum.yes.getValue();
			}
			
		}
		return ismpsflag;
	}
	
private Integer choseMspallarrivedflag(String is_gathercomp,String is_gatherpack,String sendcarnum,int mpsswitch) {
		
	if(mpsswitch!=MpsswitchTypeEnum.WeiKaiQiJiDan.getValue()){
		//集包并且是最后一箱并且箱号大于1
		if("1".equals(is_gatherpack)&&"1".equals(is_gathercomp)&&Long.valueOf(sendcarnum)>1){ 
			return VipGathercompEnum.Last.getValue();
		}
	}
	return VipGathercompEnum.Default.getValue();
}
}
