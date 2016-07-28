package cn.explink.b2c.vipshop.oxo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.explink.service.DfFeeService;
import net.sf.json.JSONObject;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.JointService;
import cn.explink.b2c.vipshop.ReaderXMLHandler;
import cn.explink.b2c.vipshop.VipShop;
import cn.explink.b2c.vipshop.VipShopConfig;
import cn.explink.b2c.vipshop.VipShopExceptionHandler;
import cn.explink.b2c.vipshop.oxo.response.TpsOrderVo;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.MqExceptionDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.EmailDate;
import cn.explink.domain.MqExceptionBuilder;
import cn.explink.domain.User;
import cn.explink.enumutil.CwbOXOStateEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.service.CustomerService;
import cn.explink.service.CwbOrderService;
import cn.explink.service.DataImportService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.XmlUtil;
import cn.explink.util.MD5.MD5Util;

@Service
public class VipShopOXOGetCwbDataService {
	
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	ReaderXMLHandler readXMLHandler;
	@Autowired
	DataImportService_B2c dataImportService_B2c;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	@Autowired
	JointService jointService;
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
	CustomerService customerService;
    @Autowired
    DfFeeService dfFeeService;

	@Autowired
	private MqExceptionDAO mqExceptionDAO;
	
	private Logger logger = LoggerFactory.getLogger(VipShopOXOGetCwbDataService.class);

	public VipShop getVipShop(int key) {
		JointEntity obj = this.jiontDAO.getJointEntity(key);
		if(obj == null){
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(obj.getJoint_property());
		VipShop vipshop = (VipShop) JSONObject.toBean(jsonObj, VipShop.class);
		return vipshop;
	}

	@Transactional
	public void edit(HttpServletRequest request, int joint_num) {
		VipShop vipshop = new VipShop();
		vipshop.setShipper_no(request.getParameter("shipper_no"));
		vipshop.setPrivate_key(request.getParameter("private_key"));
		vipshop.setGetMaxCount(Integer.parseInt(request.getParameter("getMaxCount")));
		if(StringUtils.isNotBlank(request.getParameter("sendMaxCount"))){
			vipshop.setSendMaxCount(Integer.parseInt(request.getParameter("sendMaxCount")));
		}
		vipshop.setWarehouseid(Long.parseLong(request.getParameter("warehouseid")));
		vipshop.setGetCwb_URL(request.getParameter("getCwb_URL"));
		vipshop.setSendCwb_URL(request.getParameter("sendCwb_URL"));
		vipshop.setCustomerids(request.getParameter("customerids"));
		vipshop.setIsopendownload(Integer.parseInt(request.getParameter("isopendownload")));
		vipshop.setVipshop_seq(Long.parseLong(request.getParameter("vipshop_seq")));
		vipshop.setOxoState_URL(request.getParameter("oxoState_URL"));
		String customerids = request.getParameter("customerids");
			
		String oldCustomerids = "";

		JSONObject jsonObj = JSONObject.fromObject(vipshop);
		JointEntity jointEntity = this.jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			this.jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerids = this.getVipShop(joint_num).getCustomerids();
			} catch (Exception e) {
			}
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			this.jiontDAO.Update(jointEntity);
		}
		// 保存 枚举到供货商表中
		this.customerDAO.updateB2cEnumByJoint_num(customerids, oldCustomerids, joint_num);
		this.customerService.initCustomerList();
	}

	public void updateMaxSEQ(int joint_num, VipShop vipshop) {
		VipShop vip = new VipShop();
		vip.setShipper_no(vipshop.getShipper_no());
		vip.setPrivate_key(vipshop.getPrivate_key());
		vip.setGetMaxCount(vipshop.getGetMaxCount());
		vip.setSendMaxCount(vipshop.getSendMaxCount());
		vip.setGetCwb_URL(vipshop.getGetCwb_URL());
		vip.setSendCwb_URL(vipshop.getSendCwb_URL());
		vip.setCustomerids(vipshop.getCustomerids());
		vip.setVipshop_seq(vipshop.getVipshop_seq());
		vip.setWarehouseid(vipshop.getWarehouseid());
		vip.setIsopendownload(vipshop.getIsopendownload());
		JSONObject jsonObj = JSONObject.fromObject(vip);
		JointEntity jointEntity = this.jiontDAO.getJointEntity(joint_num);
		jointEntity.setJoint_num(joint_num);
		jointEntity.setJoint_property(jsonObj.toString());
		this.jiontDAO.Update(jointEntity);

	}

	public void update(int joint_num, int state) {
		this.jiontDAO.UpdateState(joint_num, state);
	}

	/**
	 * 获取唯品会订单信息
	 */
	public long getOrdersByVipShopOXO(int vipshop_key) {
		VipShop vipshop = this.getVipShop(vipshop_key);
		int isOpenFlag = this.jointService.getStateForJoint(vipshop_key);
		if (isOpenFlag == 0) {
			this.logger.info("未开启VipShop_OXO[" + vipshop_key + "]对接！");
			return -1;
		}
		if (vipshop.getIsopendownload() == 0) {
			this.logger.info("未开启VipShop_OXO[" + vipshop_key + "]订单下载接口");
			return -1;
		}

		// 构建请求，解析返回信息
		TpsOrderVo responseVo = this.requestHttpAndCallBackAnaly(vipshop);

		if ((responseVo == null)) {
			this.logger.error("系统返回xml字符串为空或解析xml失败！");
			return -1;
		}
		
		String sys_response_code = responseVo.getHead().getSysResponseCode() == null ? ""  : responseVo.getHead().getSysResponseCode(); // 返回码
		String sys_response_msg = responseVo.getHead().getSysRespnoseMsg() == null ? ""  : responseVo.getHead().getSysRespnoseMsg(); // 返回说明
		try {
			VipShopExceptionHandler.respValidateMessage(sys_response_code, sys_response_msg, vipshop);
		} catch (Exception e) {
			this.logger.error("返回VipShop_OXO订单查询信息验证失败！异常原因:", e);
			return -1;
		}
		if (!"S00".equals(sys_response_code)) {
			this.logger.info("当前唯品会返回信息异常，sys_response_code={}", sys_response_code);
			return -1;
		}

		this.logger.info("请求VipShop_OXO订单信息-返回码：[S00],success ,sys_response_msg={}", sys_response_msg);
			
		if(responseVo.getOrders() == null || CollectionUtils.isEmpty(responseVo.getOrders().getOrder())){
			this.logger.info("请求VipShop_OXO订单信息-没有获取到订单或者订单信息重复！,当前SEQ={}", vipshop.getVipshop_seq());
			return -1;
		}
		
		extractedDataImport(responseVo.getOrders().getOrder(),vipshop);

		return 1;

	}
	
	/**
	 * 订单报文实体 转换 为 CwbOrderDTO
	 * @param cwbOrder
	 * @param order
	 * @param vipshop
	 */
	private void convertOrderVoToCwbOrderDTO(CwbOrderDTO cwbOrder,TpsOrderVo.Orders.Order order,VipShop vipshop){
		if(StringUtils.isNotBlank(order.getBusinessType())){
			if(order.getBusinessType().equals("20")){
				cwbOrder.setCwbordertypeid(CwbOrderTypeIdEnum.OXO_JIT.getValue());
			}
			else if(order.getBusinessType().equals("40")){
				cwbOrder.setCwbordertypeid(CwbOrderTypeIdEnum.OXO.getValue());
			}
			else if(order.getBusinessType().equals("30")){//保留类型
				//TODO保留类型
				cwbOrder.setCwbordertypeid(4);//default OXO
			}else{
				cwbOrder.setCwbordertypeid(4); //default OXO
			}
		}
		cwbOrder.setRemark1(order.getBrandName()+"&"+order.getStoreName()+"&"+order.getStoreContacts()+"&"+order.getStoreTel());//品牌商&提货门店&提货联系人&提货联系方式
		cwbOrder.setRemark2(order.getCustomerName());//订单客户，还不确定存到哪个字段 
		cwbOrder.setRemark3(order.getValuationValue());//保价价值 ,新增字段
		cwbOrder.setRemark4(order.getCnorProv()+"&"+order.getCnorCity()+"&"+order.getCnorRegion()+"&"+order.getCnorAddr());//发货省，市，区，详细地址
		cwbOrder.setRemark5(order.getWarehouse()+"&"+order.getWarehouseAddr());//仓库+仓库地址 
	
		cwbOrder.setCustomercommand(order.getRequiredTime());//要求提货时间
		cwbOrder.setTranscwb(order.getOrderSn());//TMS订单号
		cwbOrder.setCwb(order.getCustOrderNo()); //订单号，tps运单号
		cwbOrder.setConsigneename(order.getBuyerName());//收件人
		cwbOrder.setConsigneeaddress(order.getBuyerAddress());//收件人地址
		cwbOrder.setConsigneephone(order.getTel());//收件人电话
		cwbOrder.setConsigneemobile(order.getMobile());//收件人手机
		cwbOrder.setCargoamount(order.getGoodsMoney());//商品价格
		cwbOrder.setReceivablefee(order.getMoney());
		if(StringUtils.isNotBlank(order.getExtPayType())){
			if(order.getExtPayType().equals("-1")){ //非货到付款
				cwbOrder.setPaywayid(PaytypeEnum.Pos.getValue());
				cwbOrder.setNewpaywayid(PaytypeEnum.Pos.getValue()+"");
			}
			
			if(order.getExtPayType().equals("0")){//货到付款现金支付
				cwbOrder.setPaywayid(PaytypeEnum.Xianjin.getValue());
				cwbOrder.setNewpaywayid(PaytypeEnum.Xianjin.getValue()+"");
			}
			
			if(order.getExtPayType().equals("1")){//货到付款刷卡支付
				cwbOrder.setPaywayid(PaytypeEnum.Pos.getValue());
				cwbOrder.setNewpaywayid(PaytypeEnum.Pos.getValue()+"");
			}
			
			if(order.getExtPayType().equals("2")){//货到付款支付宝 支付
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
		
		try {
			//发货件数
			cwbOrder.setSendcargonum((order.getTotalPack()==null || "0".equals(order.getTotalPack().trim()))?"1":Integer.parseInt(order.getTotalPack())+"" );//箱数
		} catch (Exception e) {
			// TODO Auto-generated catch block
			cwbOrder.setSendcargonum("1");
		}
		
		//orderMap.put("", order.getSalesPlatform());//销售平台，不确定要不要存
		cwbOrder.setCargotype(order.getTransportType());//品类
		
		cwbOrder.setShouldfare(order.getFreight());//运费
		//orderMap.put("", order.getOrgName()); //提货站点名称
		//orderMap.put("", order.getOrgCode());//提货站点编码
		cwbOrder.setCustomerid(Long.valueOf(vipshop.getCustomerids())); //客户id
		//cwbOrder.setStartbranchid(vipshop.getWarehouseid());
		
		if(order.getVipClub()==null){
			//团购标识
			cwbOrder.setVipclub(0);
		}else{
			//团购标识
			cwbOrder.setVipclub(order.getVipClub().equals("3")?1:0);
		}
		
	}
	
	/**
	 * 订单报文实体转换为 map
	 * @param order
	 * @param vipshop
	 * @return
	 */
	private Map<String,String> convertOrderVoToMap(TpsOrderVo.Orders.Order order){
		Map<String,String> orderMap = new HashMap<String,String>();
		orderMap.put("consigneename", order.getBuyerName());
		orderMap.put("sendcarnum", order.getTotalPack());
		orderMap.put("consigneemobile", order.getMobile());
		orderMap.put("consigneephone", order.getTel());
		orderMap.put("consigneepostcode", order.getPostCode());
		
		orderMap.put("consigneeaddress", order.getBuyerAddress());
		orderMap.put("receivablefee", order.getMoney());
		orderMap.put("customercommand", order.getRequiredTime());
		orderMap.put("remark1", order.getBrandName()+"&"+order.getStoreName()+"&"+order.getStoreContacts()+"&"+order.getStoreTel());
		orderMap.put("remark2", order.getCustomerName());
		orderMap.put("remark3", order.getValuationValue());
		orderMap.put("remark4", order.getCnorProv()+"&"+order.getCnorCity()+"&"+order.getCnorRegion()+"&"+order.getCnorAddr());
		
		orderMap.put("remark5", order.getWarehouse()+"&"+order.getWarehouseAddr());
		orderMap.put("cargorealweight", order.getOriginalWeight());
		
		if(StringUtils.isNotBlank(order.getExtPayType())){
			if(order.getExtPayType().equals("-1")){ //非货到付款
				orderMap.put("paywayid", "" + PaytypeEnum.Qita.getValue());
			}
			
			if(order.getExtPayType().equals("0")){//货到付款现金支付
				orderMap.put("paywayid", "" + PaytypeEnum.Xianjin.getValue());
			}
			
			if(order.getExtPayType().equals("1")){//货到付款刷卡支付
				orderMap.put("paywayid", "" + PaytypeEnum.Pos.getValue());
			}
			
			if(order.getExtPayType().equals("2")){//货到付款支付宝 支付
				orderMap.put("paywayid", "" + PaytypeEnum.CodPos.getValue());
			}
		}
		
		if(StringUtils.isBlank(order.getExtPayType())){
			orderMap.put("paywayid", "" + PaytypeEnum.Qita.getValue());
		}
		orderMap.put("cargotype", order.getTransportType());
		
		//Added by leoliao at 2016-06-21 转换为DMP的订单类型
		String cwbOrderTypeId = String.valueOf(CwbOrderTypeIdEnum.OXO.getValue());
		if(StringUtils.isNotBlank(order.getBusinessType())){
			if(order.getBusinessType().equals("20")){
				cwbOrderTypeId = String.valueOf(CwbOrderTypeIdEnum.OXO_JIT.getValue());
			}else if(order.getBusinessType().equals("40")){
				cwbOrderTypeId = String.valueOf(CwbOrderTypeIdEnum.OXO.getValue());
			}else if(order.getBusinessType().equals("30")){
				//保留类型
				cwbOrderTypeId = String.valueOf(CwbOrderTypeIdEnum.OXO.getValue()); //default OXO
			}else{
				cwbOrderTypeId = String.valueOf(CwbOrderTypeIdEnum.OXO.getValue()); //default OXO
			}
		}
		orderMap.put("cwbordertypeid", cwbOrderTypeId);
		//Added end
		
		//orderMap.put("cwbordertypeid", "" + Long.valueOf(order.getBusinessType()));
		orderMap.put("shouldfare", order.getFreight());
		orderMap.put("cwb", order.getCustOrderNo());
		
		return orderMap;
		
	}

	/**
	 * 插入到临时表
	 * @param orders
	 */
	@Transactional
	public void extractedDataImport(List<TpsOrderVo.Orders.Order> orders,VipShop vipshop){
	
		long warehouse_id = vipshop.getWarehouseid(); // 获取虚拟库房
		long warehouseid = warehouse_id != 0 ? warehouse_id : dataImportService_B2c.getTempWarehouseIdForB2c(); // 获取虚拟库房
		
		long customerid = Long.valueOf(vipshop.getCustomerids()); //客户id

		EmailDate ed = dataImportService.getOrCreateEmailDate(Long.valueOf(vipshop.getCustomerids()), 0, warehouseid); //生成批次
		User user = new User();
		user.setUserid(1);
		user.setBranchid(warehouseid);
		
		long maxSeq = vipshop.getVipshop_seq();
		
		for(TpsOrderVo.Orders.Order order : orders){
			try{
				long id =  Long.valueOf(order.getId()).longValue();
				if(maxSeq < id){ //把每次请求返回的最大id作为下次请求的seq.
					maxSeq = id;
				}
				
				/**
				 * 处理新增命令 逻辑
				 */
				if("new".equalsIgnoreCase(order.getCmdType())){
					//如果临时表中已存在对应记录 则 continue
					if(dataImportDAO_B2c.getCwbByCwbB2ctemp(order.getCustOrderNo()) != null){
						logger.warn("接收到VipShop_OXO发来cmd_type='new'指令，cwb="+order.getCustOrderNo()+"。但express_ops_cwb_detail_b2ctemp表中已存在对应记录，系统将不做任何操作。");
						continue;
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
						continue;
					}
					
					if(cwbOrder_temp.getGetDataFlag() == 0){//如果临时表数据没有同步到了主表，只需修改临时表数据
						//update 临时表
						dataImportDAO_B2c.updateBycwb(this.convertOrderVoToMap(order));
						continue;
					}
					
					if(cwbOrder_temp.getGetDataFlag() != 0){//如果临时表数据已经同步到了主表,修改主表数据
						CwbOrder cwbOrder_biz = cwbDAO.getCwbByCwb(order.getCustOrderNo());
						if(cwbOrder_biz != null){
							//如果已揽收成功，不允编辑
							if(cwbOrder_biz.getOxopickstate() == CwbOXOStateEnum.Processed.getValue()){
								logger.warn("接收到VipShop_OXO发来cmd_type='edit'指令，cwb="+order.getCustOrderNo()+"。但express_ops_cwb_detail表中该记录的揽收状态为 已处理，系统将不做修改操作。");
								continue;
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
									this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".extractedDataImport")
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
									this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode(this.getClass().getSimpleName() + ".extractedDataImport")
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
							continue;
						}
					}
					dataImportDAO_B2c.dataLoseB2ctempByCwb(order.getCustOrderNo());
					this.cwbDAO.dataLoseByCwb(order.getCustOrderNo());
					cwbOrderService.datalose_vipshop(order.getCustOrderNo());

                    // added by Steve PENG 20160722 start TMS OXO, 订单失效后，需要对派费操作
                    dfFeeService.saveFeeRelativeAfterOrderDisabled(order.getCustOrderNo());
                    // added by Steve PENG 20160722 end
				}
			
			}catch(Exception e){
				logger.error("VIP_OXO数据插入临时表发生未知异常cwb=" + order.getCustOrderNo(), e);
			}
			
		}
		vipshop.setVipshop_seq(maxSeq);
		this.updateMaxSEQ(B2cEnum.VipShop_OXO.getKey(), vipshop);
	}
	

	/**
	 * 构建请求，解析返回
	 *
	 * @param vipshop
	 * @return
	 */
	private TpsOrderVo requestHttpAndCallBackAnaly(VipShop vipshop) {
		String request_time = DateTimeUtil.getNowTime();
		String requestXML = this.StringXMLRequest(vipshop, request_time);
		String MD5Str = vipshop.getPrivate_key() + VipShopConfig.version + request_time + vipshop.getShipper_no() + vipshop.getVipshop_seq() + vipshop.getGetMaxCount();
		String sign = MD5Util.md5(MD5Str, "UTF-8").toLowerCase();
		String endpointUrl = vipshop.getGetCwb_URL();
		String response_XML = null;

		this.logger.info("获取VipShop_OXO订单XML={}", requestXML);

		try {
			response_XML = this.HTTPInvokeWs(endpointUrl, VipShopOXOConfig.nameSpace, VipShopOXOConfig.requestMethodName, requestXML, sign);
		} catch (Exception e) {
			this.logger.error("处理唯品会OXO订单请求异常！返回信息：" + response_XML + ",异常原因：" + e.getMessage(), e);
			return null;
		}

		String orderXML = this.readXMLHandler.parseOXORspSOAP(ReaderXMLHandler.parseBack(response_XML));
		this.logger.info("当前下载唯品会XML={}", orderXML);
		TpsOrderVo obj = null;
		try {
			obj = XmlUtil.toObject(TpsOrderVo.class, orderXML);
		} catch (Exception e1) {
			this.logger.error("转换唯品会OXO响应报文为TpsOrderVo实体类异常。响应报文内容为："+orderXML ,e1);
		}
		
		return obj;
		
	}

	private String StringXMLRequest(VipShop vipshop, String request_time) {
		StringBuffer sub = new StringBuffer();
		sub.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sub.append("<request>");
		sub.append("<head>");
		sub.append("<version>" + VipShopConfig.version + "</version>");
		sub.append("<request_time>" + request_time + "</request_time>");
		sub.append("<cust_code>" + vipshop.getShipper_no() + "</cust_code>");
		sub.append("<seq>" + vipshop.getVipshop_seq() + "</seq>");
		sub.append("<count>" + vipshop.getGetMaxCount() + "</count>");
		sub.append("</head>");
		sub.append("</request>");
		return sub.toString();
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

	
	private String HTTPInvokeWs(String endpointUrl, String nameSpace, String methodName, String requestXML, String sign) throws Exception {
		StringBuffer result = null;
		OutputStream out = null;
		BufferedReader in = null;
		try {
			String soapActionString = nameSpace + "/" + methodName;
			StringBuffer paramXml = new StringBuffer();
			paramXml.append("<content>" + readXMLHandler.parse(requestXML) + "</content>");
			paramXml.append("<sign>" + sign.toLowerCase() + "</sign>");
			paramXml.append("<serviceCode>S201</serviceCode>");

			String soap = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><soapenv:Body><" + methodName + " xmlns:tps=\"" + nameSpace + "\">" + paramXml + "</" + methodName
					+ "></soapenv:Body></soapenv:Envelope>";
			logger.info("soap方式请求格式：" + soap);
			URL url = new URL(endpointUrl);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestProperty("Content-Length", String.valueOf(soap.getBytes("UTF-8").length));
			httpConn.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			httpConn.setRequestProperty("soapActionString", soapActionString);
			
			httpConn.setRequestMethod("POST");
			httpConn.setReadTimeout(60000);
			httpConn.setConnectTimeout(60000);
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			out = httpConn.getOutputStream();
			out.write(soap.getBytes("UTF-8"));
			out.flush();

			InputStreamReader isr = new InputStreamReader(httpConn.getInputStream(), "UTF-8");

			in = new BufferedReader(isr);
			result = new StringBuffer("");
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				result.append(inputLine);
			}

		} catch (Throwable e) {
	        logger.error("", e);
			throw new Exception("WebService服务链路异常:" + e.getMessage(), e);
		} finally {
			if (out != null) {
				out.close();
			}
			if (in != null) {
				in.close();
			}

		}
		return result.toString();
	}
	public static void main(String[] args) {
		
//		VipShop vipshop = new VipShop();
//		vipshop.setGetCwb_URL("http://test.tps.vip.com:7172/ws/tpsCarrierService");
//		vipshop.setPrivate_key("30113");
//		vipshop.setShipper_no("NHLDP089");
//		vipshop.setVipshop_seq(0);
//		vipshop.setGetMaxCount(10);
//		VipShopOXOGetCwbDataService service = new VipShopOXOGetCwbDataService();
//		service.requestHttpAndCallBackAnaly(vipshop);
		
	}
	
	
	
}
