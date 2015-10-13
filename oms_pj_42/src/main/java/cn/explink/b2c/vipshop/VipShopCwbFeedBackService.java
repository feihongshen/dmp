package cn.explink.b2c.vipshop;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.ExceptionTrace;
import cn.explink.b2c.tools.FlowFromJMSB2cService;
import cn.explink.b2c.tools.JacksonMapper;
import cn.explink.b2c.tools.Mail;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.B2CData;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.DateTimeUtil;

@Service
public class VipShopCwbFeedBackService {

	@Autowired
	private B2cTools b2ctools;
	@Autowired
	private B2CDataDAO b2cDataDAO;
	@Autowired
	SOAPHandler soapHandler;
	@Autowired
	ReaderXMLHandler readXMLHandler;
	@Autowired
	FlowFromJMSB2cService flowFromJMSB2cService;
	@Autowired
	GetDmpDAO getDmpdao;

	private Logger logger = LoggerFactory.getLogger(VipShopCwbFeedBackService.class);

	// 获取VipShop配置信息
	public VipShop getVipShopSettingMethod(int key) {
		VipShop vipshop = null;
		String objectMethod = this.b2ctools.getObjectMethod(key).getJoint_property();
		if (objectMethod != null) {
			JSONObject jsonObj = JSONObject.fromObject(objectMethod);
			vipshop = (VipShop) JSONObject.toBean(jsonObj, VipShop.class);
		} else {
			vipshop = new VipShop();
		}
		return vipshop;
	}

	// 获取vipshop XML Note
	public VipShopXMLNote getVipShopXMLNoteMethod(String jsoncontent) {
		try {
			JSONObject jsonObj = JSONObject.fromObject(jsoncontent);
			return (VipShopXMLNote) JSONObject.toBean(jsonObj, VipShopXMLNote.class);
		} catch (Exception e) {
			this.logger.error("获取VipShopXMLNote异常！jsoncontent:" + jsoncontent + e);
			return null;
		}
	}

	/**
	 * 反馈唯品会订单信息
	 */
	public long feedback_status(int vipshop_key) {
		long calcCount = 0;

		VipShop vipshop = this.getVipShopSettingMethod(vipshop_key); // 获取配置信息
		if (!this.b2ctools.isB2cOpen(vipshop_key)) {
			this.logger.info("未开vipshop的对接!vipshop_key={}", vipshop_key);
			return -1;
		}
		this.logger.info("=========VipShop状态反馈任务调度开启==========");
		calcCount += this.sendCwbStatus_To_VipShop(vipshop, FlowOrderTypeEnum.RuKu.getValue()); // code：4-配送中(库房入库)
		calcCount += this.sendCwbStatus_To_VipShop(vipshop, FlowOrderTypeEnum.ChuKuSaoMiao.getValue()); // code：4-配送中(库房出库)
		calcCount += this.sendCwbStatus_To_VipShop(vipshop, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()); // code：4-配送中(分站到货)
		calcCount += this.sendCwbStatus_To_VipShop(vipshop, FlowOrderTypeEnum.FenZhanLingHuo.getValue()); // code:4-配送中(分站投递)

		calcCount += this.sendCwbStatus_To_VipShop(vipshop, FlowOrderTypeEnum.YiShenHe.getValue()); // 已审核
		// -包括各种状态

		calcCount += this.sendCwbStatus_To_VipShop(vipshop, FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()); // 已审核
																											// -包括各种状态

		// 揽退单

		this.sendCwbStatus_To_VipShop_LanTui(vipshop, FlowOrderTypeEnum.DaoRuShuJu.getValue());
		this.sendCwbStatus_To_VipShop_LanTui(vipshop, FlowOrderTypeEnum.FenZhanLingHuo.getValue());
		this.sendCwbStatus_To_VipShop_LanTui(vipshop, FlowOrderTypeEnum.YiShenHe.getValue());

		String nowdateHours = DateTimeUtil.getNowTime("HH");
		int hours = Integer.valueOf(nowdateHours);
		if ((hours % 2) == 0) { // 每隔2的倍数来执行
			this.updateVipShopByGanXianHuiDan(vipshop.getCustomerids()); // 定时执行干线回单的脚本
		}

		this.logger.info("=========VipShop状态反馈任务调度结束==========");
		return calcCount;
	}

	/**
	 * 反馈的处理方法
	 * 
	 * @param vipshop
	 * @param vip_code
	 * @param flowordertype
	 */
	private long sendCwbStatus_To_VipShop(VipShop vipshop, int flowordertype) {

		long calcCount = 0;

		try {
			while (true) {
				calcCount++;
				String requestXML = "";
				
				if(calcCount>200){
					logger.warn("当前请求唯品会接口循环次数已超过200，跳出");
					return 0;
				}
				
				try {
					String lefengCustomerid=vipshop.getLefengCustomerid()==null||vipshop.getLefengCustomerid().isEmpty()?vipshop.getCustomerids():vipshop.getLefengCustomerid();
					List<B2CData> vipshopDataList = this.b2cDataDAO.getDataListByFlowStatus(flowordertype, vipshop.getCustomerids()+","+lefengCustomerid, vipshop.getSendMaxCount());
					if ((vipshopDataList == null) || (vipshopDataList.size() == 0)) {
						this.logger.info("当前没有要推送[vipshop]的数据,状态:flowordertype={}", flowordertype);
						return 0;
					}
					String request_time = DateTimeUtil.getNowTime();
					requestXML = this.AppendXMLString_peisong(vipshop, vipshopDataList, flowordertype, request_time);
					if (requestXML == null) {
						this.logger.info("当前没有要推送[vipshop]的数据，判断揽退,状态:flowordertype={}", flowordertype);
						return 0;
					}
					String md5HeplerXML = this.AppendXMLStringMD5Hepler(vipshop, vipshopDataList, flowordertype, request_time);
					
					String MD5Str = this.sendCreateMD5Str(vipshopDataList, flowordertype, request_time, vipshop, requestXML,md5HeplerXML);

					//this.logger.info("签名字符串：{}", MD5Str);

					String Sign = VipShopMD5Util.MD5(MD5Str).toLowerCase();
					String response_XML = null;
					try {
						response_XML = this.soapHandler.HTTPInvokeWs(vipshop.getSendCwb_URL(), VipShopConfig.nameSpace, VipShopConfig.statusBackMethodNameNew, requestXML, Sign,
								VipShopConfig.PEISONG_TYPE);
					} catch (Exception e) {
						this.logger.error("推送vipshop状态-异常！推送XML信息：" + response_XML + ",异常原因：" + e, e);
						continue;
					}
					this.logger.info("vipshop状态反馈XML={},flowordertype={}", ReaderXMLHandler.parseBack(response_XML), flowordertype);
					Map<String, Object> parseMap = null;
					try {
						parseMap = this.readXMLHandler.parseGetCwbInfo_VipShopXml(response_XML);
						this.logger.info("解析后的XML-Map,flowordertype={},parseMap={}", flowordertype, parseMap);
					} catch (Exception e) {
						this.logger.error("解析vipshop返回订单信息异常!,异常原因：" + e, e);
						return 0;
					}
					String sys_response_code = parseMap.get("sys_response_code") != null ? parseMap.get("sys_response_code").toString() : ""; // 返回码
					String sys_response_msg = parseMap.get("sys_respnose_msg") != null ? parseMap.get("sys_respnose_msg").toString() : ""; // 返回说明

					try {
						VipShopExceptionHandler.respValidateMessage(sys_response_code, sys_response_msg, vipshop);
					} catch (Exception e) {
						this.logger.error("返回vipshop订单查询信息验证失败！", e);
						e.printStackTrace();
						return 0;
					}

					if ("S00".equals(sys_response_code)) {
						this.DealWithResponseByVipShop(vipshop, flowordertype, parseMap, sys_response_msg);
					}


				} catch (Exception e) {
					String exptMessage = "[唯品会]订单状态反馈发送不可预知的异常！当前状态=" + flowordertype + "，当前请求的XML=" + requestXML;
					this.logger.error(exptMessage, e);
					return 0;
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return calcCount;

	}

	public int sendByCwbs(String cwbs, long send_b2c_flag, String b2cEnumkey) {
		int vipshop_key = Integer.parseInt(b2cEnumkey);

		VipShop vipshop = this.getVipShopSettingMethod(vipshop_key); // 获取配置信息
		if (!this.b2ctools.isB2cOpen(vipshop_key)) {
			this.logger.info("未开vipshop的对接!vipshop_key={}", vipshop_key);
			return 0;
		}
		String requestXML = "";
		try {
			List<B2CData> vipshopDataList1 = this.b2cDataDAO.getDataListByCwbs(cwbs, send_b2c_flag);
			if ((vipshopDataList1 == null) || (vipshopDataList1.size() == 0)) {
				this.logger.info("当前没有要推送[vipshop]的数据");
				return 0;
			}
			for (B2CData b2cData : vipshopDataList1) {
				List<B2CData> vipshopDataList = new ArrayList<B2CData>();
				vipshopDataList.add(b2cData);
				String request_time = DateTimeUtil.getNowTime();
				requestXML = this.AppendXMLString_peisong(vipshop, vipshopDataList, (int) b2cData.getFlowordertype(), request_time);
				
				String md5HeplerXML = this.AppendXMLStringMD5Hepler(vipshop, vipshopDataList, (int) b2cData.getFlowordertype(), request_time);
				
				String MD5Str = this.sendCreateMD5Str(vipshopDataList, (int) b2cData.getFlowordertype(), request_time, vipshop, requestXML,md5HeplerXML);
				String Sign = VipShopMD5Util.MD5(MD5Str);
				String response_XML = null;
				try {
					response_XML = this.soapHandler
							.HTTPInvokeWs(vipshop.getSendCwb_URL(), VipShopConfig.nameSpace, VipShopConfig.statusBackMethodNameNew, requestXML, Sign, VipShopConfig.PEISONG_TYPE);
				} catch (Exception e) {
					this.logger.error("推送vipshop状态-异常！推送XML信息：" + response_XML + ",异常原因：" + e, e);
					e.printStackTrace();
					return 0;
				}
				this.logger.info("vipshop状态反馈XML={},flowordertype={}", ReaderXMLHandler.parseBack(response_XML), (int) b2cData.getFlowordertype());
				Map<String, Object> parseMap = null;
				try {
					parseMap = this.readXMLHandler.parseGetCwbInfo_VipShopXml(response_XML);
					this.logger.info("解析后的XML-Map,flowordertype={},parseMap={}", (int) b2cData.getFlowordertype(), parseMap);
				} catch (Exception e) {
					this.logger.error("解析vipshop返回订单信息异常!,异常原因：" + e, e);
					e.printStackTrace();
					return 0;
				}
				String sys_response_code = parseMap.get("sys_response_code") != null ? parseMap.get("sys_response_code").toString() : ""; // 返回码
				String sys_response_msg = parseMap.get("sys_respnose_msg") != null ? parseMap.get("sys_respnose_msg").toString() : ""; // 返回说明

				try {
					VipShopExceptionHandler.respValidateMessage(sys_response_code, sys_response_msg, vipshop);
				} catch (Exception e) {
					this.logger.error("返回vipshop订单查询信息验证失败！", e);
					e.printStackTrace();
					return 0;
				}

				if ("S00".equals(sys_response_code)) {
					this.DealWithResponseByVipShop(vipshop, (int) b2cData.getFlowordertype(), parseMap, sys_response_msg);
				}
			}
		} catch (Exception e) {
			String exptMessage = "[唯品会]订单状态反馈发送不可预知的异常！当前请求的XML=" + requestXML;
			this.logger.error(exptMessage, e);
			exptMessage = ExceptionTrace.getExceptionTrace(e, exptMessage);
			Mail.LoadingAndSendMessage(exptMessage);
			return 0;
		}
		return 1;
	}

	private void DealWithResponseByVipShop(VipShop vipshop, int flowordertype, Map<String, Object> parseMap, String sys_response_msg) {
		this.logger.info("发送Vipshop订单反馈信息-返回码：[S00],success ,sys_response_msg：" + sys_response_msg);

		List<Map<String, String>> orderlist = (List<Map<String, String>>) parseMap.get("orderlist");
		for (Map<String, String> orderMap : orderlist) {
			String biz_response_code = orderMap.get("biz_response_code") == null ? "" : orderMap.get("biz_response_code").toString(); // 返回码
			String biz_response_msg = orderMap.get("biz_respnose_msg") == null ? "" : orderMap.get("biz_respnose_msg").toString();
			int send_b2c_flag = 1; // 默认全部成功
			try {
				VipShopExceptionHandler.respValidateBizExptMessage(biz_response_code, biz_response_msg, vipshop);
			} catch (Exception e) {
				this.logger.error("vipshop订单状态反馈异常!,flowordertype=" + flowordertype + ",b2cid=" + orderMap.get("cust_data_id") + e, e);
				send_b2c_flag = 2; // 推送该单子失败
			}
			try {
				String cust_data_id = orderMap.get("cust_data_id").toString();
				String b2cid = VipShopCwbFeedBackService.parseStrCut(cust_data_id);
				this.logger.info("修改vipshop结果:b2cid=" + b2cid + ",flowordertype=" + flowordertype + ",send_b2c_flag=" + send_b2c_flag + ",msg=" + biz_response_msg);

				String keyword = "干线回单"; // 根据关键词删除
				if (biz_response_msg.contains(keyword)) {
					biz_response_msg = keyword;
				}
				String keyword2 = "状态发生时间"; // 根据关键词 存储，重发
				if (biz_response_msg.contains(keyword2)) {
					biz_response_msg = keyword2;
				}

				this.b2cDataDAO.updateB2cIdSQLResponseStatus(Long.valueOf(b2cid), send_b2c_flag, biz_response_msg);
				// 发送给dmp
				this.flowFromJMSB2cService.sendTodmp(b2cid);
			} catch (Throwable e) {
				String exptMessage = "vipshop订单状态反馈更新数据库异常!,flowordertype=" + flowordertype + ",b2cid=" + orderMap.get("cust_data_id") + e;
				this.logger.error(exptMessage, e);
				throw new RuntimeException(exptMessage);
			}
			this.logger.info("vipshop订单状态反馈成功!flowordertype={},b2cid：" + orderMap.get("cust_data_id"), flowordertype);

		}
	}

	/**
	 * VipShop生成签名字符串
	 */
	public String sendCreateMD5Str(List<B2CData> vipshopDataList, int flowordertype, String request_time, VipShop vipshop, String requestXML,String md5HeplerXML) {
		String MD5Str = "";

		String version = "1.0";
		MD5Str = vipshop.getPrivate_key() + version + request_time + vipshop.getShipper_no();
		StringBuffer sub = new StringBuffer(MD5Str);
		requestXML = this.getXMLContentReplace(requestXML,md5HeplerXML);
		sub.append(requestXML);

		return sub.toString().replaceAll("null", "");
	}

	// 拼接XML
	private String AppendXMLString_peisong(VipShop vipshop, List<B2CData> vipshopDataList, int flowordertype, String request_time) {
		String version = "1.0";
		StringBuffer sub1 = new StringBuffer();
		sub1.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sub1.append("<request>");
		sub1.append("<head>");
		sub1.append("<version>" + version + "</version>");
		sub1.append("<request_time>" + request_time + "</request_time>");
		sub1.append("<cust_code>" + vipshop.getShipper_no() + "</cust_code>");
		sub1.append("</head>");
		sub1.append("<traces>");
		for (B2CData b2cData : vipshopDataList) {
			String jsoncontent = b2cData.getJsoncontent();
			VipShopXMLNote note = this.getVipShopXMLNoteMethod(jsoncontent);
			if(note== null){
				continue;
			}

			if (note.getCwbordertypeid() != CwbOrderTypeIdEnum.Peisong.getValue()) {
				this.logger.info("当前推送唯品会{}过滤揽退单,flowordertype={}", b2cData.getCwb(), b2cData.getFlowordertype());
				continue;
			}

			String order_status_info = note.getOrder_status_info();
			if (!note.getOrder_status().equals("33")) {
				order_status_info = note.getOrder_status_info().length() > 50 ? note.getOrder_status_info().substring(0, 49) : note.getOrder_status_info();
			}
			
			String sub2="";
			if (note.getOrder_status().equals("33")) {
				sub2="<delivery_name>" + note.getDeliverUser() + "</delivery_name>"
					+"<delivery_phone>" + note.getDeliverMobile() + "</delivery_phone>";
			}

			sub1.append("<trace>");
			sub1.append("<cust_data_id>" + (VipShopCwbFeedBackService.parseStrAdd(b2cData.getB2cid() + "")) + "</cust_data_id>");
			sub1.append("<order_sn>" + note.getOrder_sn() + "</order_sn>");
			sub1.append("<order_status>" + note.getOrder_status() + "</order_status>");
			sub1.append("<order_status_info>" + order_status_info + "</order_status_info>");
			sub1.append("<current_city_name>" + note.getCurrent_city_name() + "</current_city_name>");
			sub1.append("<order_status_time>" + note.getOrder_status_time() + "</order_status_time>");
			sub1.append("<sign_man>" + note.getSign_man() + "</sign_man>");
			sub1.append(sub2);
			sub1.append("<is_unpacked>" +(note.getIs_unpacked()==null?"":note.getIs_unpacked())+ "</is_unpacked>");
			sub1.append("<is_allograph_sign>" + note.getIs_allograph_sign()+ "</is_allograph_sign>");
			sub1.append("<allograph_tel>" + note.getSign_man_phone() + "</allograph_tel>");
			sub1.append("</trace>");

			if (note.getOrder_status().equals("33")) { // 如果是33状态 则自动创建虚拟 领货状态

				String order_status_msg = "货物已达[" + note.getDeliverBranch() + "]由派送员[" + note.getDeliverUser() + "]开始派送，投递员电话：[" + note.getDeliverMobile() + "]";
				note.setOrder_status_info_temp(order_status_msg);
				sub1.append("<trace>");
				sub1.append("<cust_data_id>" + (VipShopCwbFeedBackService.parseStrAdd(b2cData.getB2cid() + "_temp")) + "</cust_data_id>");
				sub1.append("<order_sn>" + note.getOrder_sn() + "</order_sn>");
				sub1.append("<order_status>" + VipShopFlowEnum.FenZhanLingHuo_temp.getVipshop_state() + "</order_status>");
				sub1.append("<order_status_info>" + order_status_msg + "</order_status_info>");
				sub1.append("<current_city_name>" + note.getCurrent_city_name() + "</current_city_name>");
				sub1.append("<order_status_time>" + note.getOrder_status_time() + "</order_status_time>");
				sub1.append("<sign_man>" + note.getSign_man() + "</sign_man>");
				sub1.append("<delivery_name>" + note.getDeliverUser() + "</delivery_name>");
				sub1.append("<delivery_phone>" + note.getDeliverMobile() + "</delivery_phone>");
				sub1.append("<is_unpacked></is_unpacked>");
				sub1.append("</trace>");
			}
		}
		sub1.append("</traces>");
		sub1.append("</request>");
		String returnStr = sub1.toString();
		if (returnStr.contains("<traces></traces>")) {
			return null;
		}

		this.logger.info("请求vipshop:状态[" + flowordertype + "],推送vipshop-XML：{}", returnStr);
		return returnStr.replaceAll("null", "");
	}
	
	
	
	
	// 拼接XML,MD5加密使用
		private String AppendXMLStringMD5Hepler(VipShop vipshop, List<B2CData> vipshopDataList, int flowordertype, String request_time) {

			String version = "1.0";
			StringBuffer sub1 = new StringBuffer();
			sub1.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			sub1.append("<request>");
			sub1.append("<head>");
			sub1.append("<version>" + version + "</version>");
			sub1.append("<request_time>" + request_time + "</request_time>");
			sub1.append("<cust_code>" + vipshop.getShipper_no() + "</cust_code>");
			sub1.append("</head>");
			sub1.append("<traces>");
			for (B2CData b2cData : vipshopDataList) {
				String jsoncontent = b2cData.getJsoncontent();
				VipShopXMLNote note = this.getVipShopXMLNoteMethod(jsoncontent);

				if (note.getCwbordertypeid() != CwbOrderTypeIdEnum.Peisong.getValue()) {
					this.logger.info("当前推送唯品会{}过滤揽退单,flowordertype={}", b2cData.getCwb(), b2cData.getFlowordertype());
					continue;
				}

				String order_status_info = note.getOrder_status_info();
				if (!note.getOrder_status().equals("33")) {
					order_status_info = note.getOrder_status_info().length() > 50 ? note.getOrder_status_info().substring(0, 49) : note.getOrder_status_info();
				}

				sub1.append("<trace>");
				sub1.append("<cust_data_id>" + (VipShopCwbFeedBackService.parseStrAdd(b2cData.getB2cid() + "")) + "</cust_data_id>");
				sub1.append("<order_sn>" + note.getOrder_sn() + "</order_sn>");
				sub1.append("<order_status>" + note.getOrder_status() + "</order_status>");
				sub1.append("<order_status_info>" + order_status_info + "</order_status_info>");
				sub1.append("<current_city_name>" + note.getCurrent_city_name() + "</current_city_name>");
				sub1.append("<order_status_time>" + note.getOrder_status_time() + "</order_status_time>");
				sub1.append("<sign_man>" + note.getSign_man() + "</sign_man>");
				
				sub1.append("<is_unpacked>" +(note.getIs_unpacked()==null?"":note.getIs_unpacked())+ "</is_unpacked>");
				sub1.append("</trace>");

				if (note.getOrder_status().equals("33")) { // 如果是33状态 则自动创建虚拟 领货状态

					String order_status_msg = "货物已达[" + note.getDeliverBranch() + "]由派送员[" + note.getDeliverUser() + "]开始派送，投递员电话：[" + note.getDeliverMobile() + "]";
					note.setOrder_status_info_temp(order_status_msg);
					sub1.append("<trace>");
					sub1.append("<cust_data_id>" + (VipShopCwbFeedBackService.parseStrAdd(b2cData.getB2cid() + "_temp")) + "</cust_data_id>");
					sub1.append("<order_sn>" + note.getOrder_sn() + "</order_sn>");
					sub1.append("<order_status>" + VipShopFlowEnum.FenZhanLingHuo_temp.getVipshop_state() + "</order_status>");
					sub1.append("<order_status_info>" + order_status_msg + "</order_status_info>");
					sub1.append("<current_city_name>" + note.getCurrent_city_name() + "</current_city_name>");
					sub1.append("<order_status_time>" + note.getOrder_status_time() + "</order_status_time>");
					sub1.append("<sign_man>" + note.getSign_man() + "</sign_man>");
					sub1.append("<is_unpacked></is_unpacked>");
					sub1.append("</trace>");
				}
			}
			sub1.append("</traces>");
			sub1.append("</request>");
			String returnStr = sub1.toString();
			if (returnStr.contains("<traces></traces>")) {
				return null;
			}

			return returnStr.replaceAll("null", "");
			
		}

	// 拼接XML
	private String AppendXMLString_lantui(VipShop vipshop, List<B2CData> vipshopDataList, int flowordertype, String request_time) {
		String version = "1.0";
		StringBuffer sub1 = new StringBuffer();
		sub1.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sub1.append("<request>");
		sub1.append("<head>");
		sub1.append("<version>" + version + "</version>");
		sub1.append("<request_time>" + request_time + "</request_time>");
		sub1.append("<cust_code>" + vipshop.getShipper_no() + "</cust_code>");
		sub1.append("</head>");
		sub1.append("<traces>");
		for (B2CData b2cData : vipshopDataList) {
			String jsoncontent = b2cData.getJsoncontent();
			VipShopXMLNote note = this.getVipShopXMLNoteMethod(jsoncontent);

			if (note.getCwbordertypeid() != CwbOrderTypeIdEnum.Shangmentui.getValue()) {
				this.logger.info("当前推送唯品会{}过滤配送单,flowordertype={}", b2cData.getCwb(), b2cData.getFlowordertype());
				continue;
			}

			String order_status_info = note.getOrder_status_info();

			// 补充上门揽件
			if (note.getOrder_status().equals(String.valueOf(VipShopFlowEnum.ShangMenTuiChengGong_t.getVipshop_state()))
					|| note.getOrder_status().equals(String.valueOf(VipShopFlowEnum.ShengMenJuTui_t.getVipshop_state()))) { // 如果是34状态
																															// 则自动创建虚拟
				String order_status_time = ((note.getShangmenlanshoutime() == null) || note.getShangmenlanshoutime().isEmpty() ? note.getOrder_status_time() : note.getShangmenlanshoutime()); // 上门揽退状态

				String order_status_infos = "上门揽件";
				sub1.append("<trace>");
				sub1.append("<cust_data_id>" + (VipShopCwbFeedBackService.parseStrAdd(b2cData.getB2cid() + "_temp")) + "</cust_data_id>");
				sub1.append("<order_sn>" + note.getOrder_sn() + "</order_sn>");
				sub1.append("<order_status>" + VipShopFlowEnum.ShangMenLanJian_t.getVipshop_state() + "</order_status>");
				sub1.append("<order_status_info>" + order_status_infos + "</order_status_info>");
				sub1.append("<current_city_name>" + note.getCurrent_city_name() + "</current_city_name>");
				sub1.append("<order_status_time>" + order_status_time + "</order_status_time>");
				sub1.append("<sign_man>" + note.getSign_man() + "</sign_man>");
				sub1.append("</trace>");
			}

			String sub_detail = this.buildOrderGoods(note); // 商品明细
			sub1.append("<trace>");
			sub1.append("<cust_data_id>" + (VipShopCwbFeedBackService.parseStrAdd(b2cData.getB2cid() + "")) + "</cust_data_id>");
			sub1.append("<order_sn>" + note.getOrder_sn() + "</order_sn>");
			sub1.append("<order_status>" + note.getOrder_status() + "</order_status>");
			sub1.append("<order_status_info>" + order_status_info + "</order_status_info>");
			sub1.append("<current_city_name>" + note.getCurrent_city_name() + "</current_city_name>");
			sub1.append("<order_status_time>" + note.getOrder_status_time() + "</order_status_time>");
			sub1.append("<sign_man>" + note.getSign_man() + "</sign_man>");
			sub1.append("<delivery_name>" + (note.getDeliver_name() == null ? "" : note.getDeliver_name()) + "</delivery_name>");
			sub1.append("<delivery_phone>" + (note.getDeliver_mobile() == null ? "" : note.getDeliver_mobile()) + "</delivery_phone>");
			if ((note.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()) && (sub_detail != null) && !sub_detail.isEmpty()) {
				sub1.append("<details>" + sub_detail + "</details>");
			}

			sub1.append("</trace>");

		}
		sub1.append("</traces>");
		sub1.append("</request>");
		String returnStr = sub1.toString();
		if (returnStr.contains("<traces></traces>")) {
			return null;
		}

		this.logger.info("请求vipshop:状态[" + flowordertype + "],推送vipshop-XML：{}", returnStr);
		return returnStr.replaceAll("null", "");
	}

	private String buildOrderGoods(VipShopXMLNote note) {

		StringBuffer sub_detail = new StringBuffer();

		if ((note.getDetails() != null) && !note.getDetails().isEmpty()) {

			try {
				List<OrderGoods> orderGoodslist = JacksonMapper.getInstance().readValue(note.getDetails(), new TypeReference<List<OrderGoods>>() {
				});

				if ((orderGoodslist != null) && (orderGoodslist.size() > 0)) {

					String reason = note.getGoods_reason();
					for (OrderGoods orderGoods : orderGoodslist) {
						String goodsReason=orderGoods.getReturn_reason();
						if(goodsReason== null||goodsReason.isEmpty()){
							goodsReason = reason;
						}
						sub_detail.append("<detail>");
						sub_detail.append("<goods_code><![CDATA[" + orderGoods.getGoods_code() + "]]></goods_code>");
						sub_detail.append("<goods_name><![CDATA[" + orderGoods.getGoods_name() + "]]></goods_name>");
						sub_detail.append("<goods_num>" + orderGoods.getGoods_num() + "</goods_num>");
						sub_detail.append("<fetch_goods_num>" + orderGoods.getShituicount() + "</fetch_goods_num>");
						sub_detail.append("<special_goods_num>" + orderGoods.getTepituicount() + "</special_goods_num>");
						sub_detail.append("<remark><![CDATA[" + goodsReason + "]]></remark>");
						sub_detail.append("</detail>");

					}
				}
			} catch (Exception e) {
				this.logger.error("构建商品明细异常", e);
			}
			return sub_detail.toString();
		}
		return "";

	}

	/**
	 * 替换掉json格式的节点
	 * 
	 * @return
	 */
	private String getXMLContentReplace(String xml,String md5HeplerXML) {

		md5HeplerXML = md5HeplerXML.substring(md5HeplerXML.indexOf("<traces>") + 8, md5HeplerXML.indexOf("</traces>"));
		md5HeplerXML = md5HeplerXML.replaceAll("<trace>", "").replaceAll("</trace>", "").replaceAll("<cust_data_id>", "").replaceAll("</cust_data_id>", "").replaceAll("<order_sn>", "").replaceAll("</order_sn>", "")
				.replaceAll("<order_status>", "").replaceAll("</order_status>", "").replaceAll("<order_status_info>", "").replaceAll("</order_status_info>", "").replaceAll("<current_city_name>", "")
				.replaceAll("</current_city_name>", "").replaceAll("<order_status_time>", "").replaceAll("</order_status_time>", "").replaceAll("<sign_man>", "").replaceAll("</sign_man>", "")
				.replaceAll("<is_unpacked></is_unpacked>", "").replaceAll("<is_unpacked>0</is_unpacked>", "").replaceAll("<is_unpacked>1</is_unpacked>", "")
				.replaceAll("<is_unpacked>0</is_unpacked>", "").replaceAll("<is_unpacked>1</is_unpacked>", "");
		
		return md5HeplerXML;
	}

	public String getVipShopFlowEnum(long flowordertype, long delivery_state, long cwbordertypeid) {

		if (cwbordertypeid == CwbOrderTypeIdEnum.Peisong.getValue()) {

			if (flowordertype == FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong.getValue()) {
				return null;
			}
			if (flowordertype == FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()) {
				return null;
			}
			if (flowordertype == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()) {
				return null;
			}

			if (flowordertype != FlowOrderTypeEnum.YiShenHe.getValue()) {
				for (VipShopFlowEnum TEnum : VipShopFlowEnum.values()) {
					if ((flowordertype == TEnum.getFlowordertype()) && (flowordertype != VipShopFlowEnum.Ruku_t.getFlowordertype())) {
						return TEnum.getVipshop_state() + "";
					}
				}
			}
			if (flowordertype == FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue()) {
				return null;
			}
			if (delivery_state == DeliveryStateEnum.PeiSongChengGong.getValue()) {
				return VipShopFlowEnum.PeiSongChengGong.getVipshop_state() + "";
			}
			if ((flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()) && (delivery_state == DeliveryStateEnum.ShangMenTuiChengGong.getValue())) {
				return VipShopFlowEnum.ShangMenTuiChengGong.getVipshop_state() + "";
			}
			if ((flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()) && (delivery_state == DeliveryStateEnum.ShangMenHuanChengGong.getValue())) {
				return VipShopFlowEnum.ShangMenHuanChengGong.getVipshop_state() + "";
			}
			if ((flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()) && (delivery_state == DeliveryStateEnum.QuanBuTuiHuo.getValue())) {
				return VipShopFlowEnum.JuShou.getVipshop_state() + "";
			}
			if ((flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()) && (delivery_state == DeliveryStateEnum.BuFenTuiHuo.getValue())) {
				return VipShopFlowEnum.BuFenJuShou.getVipshop_state() + "";
			}
			if ((flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()) && (delivery_state == DeliveryStateEnum.FenZhanZhiLiu.getValue())) {
				return VipShopFlowEnum.FenZhanZhiLiu.getVipshop_state() + "";
			}
			if ((flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()) && (delivery_state == DeliveryStateEnum.HuoWuDiuShi.getValue())) {
				return VipShopFlowEnum.HuoWuDiuShi.getVipshop_state() + "";
			}
		} else if (cwbordertypeid == CwbOrderTypeIdEnum.Shangmentui.getValue()) { // 上门退类型

			if (flowordertype == FlowOrderTypeEnum.DaoRuShuJu.getValue()) {
				return VipShopFlowEnum.Ruku_t.getVipshop_state() + "";
			}
			if (flowordertype == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
				return VipShopFlowEnum.FenZhanLingHuo_t.getVipshop_state() + "";
			}
			if ((flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()) && (delivery_state == DeliveryStateEnum.ShangMenTuiChengGong.getValue())) {
				return VipShopFlowEnum.ShangMenTuiChengGong_t.getVipshop_state() + "";
			}
			if ((flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()) && (delivery_state == DeliveryStateEnum.ShangMenJuTui.getValue())) {
				return VipShopFlowEnum.ShengMenJuTui_t.getVipshop_state() + "";
			}

		}

		return null;

	}

	private static String parseStrAdd(String str) {
		if (str.contains("_temp")) {
			return str;
		}
		return str + "_8";
	}

	private static String parseStrCut(String str) {
		if (str.contains("_8")) {
			return str.substring(0, str.indexOf("_8"));
		}
		if (str.contains("_1")) {
			return str.substring(0, str.indexOf("_1"));
		}
		if (str.contains("_temp")) {
			return str.substring(0, str.indexOf("_temp"));
		}
		return str;
	}

	/**
	 * 定时重发
	 */
	private void updateVipShopByGanXianHuiDan(String customerids) {
		try {
			String time = DateTimeUtil.getDateBefore(5);
			String keyword = "干线回单"; // 根据关键词删除
			this.b2cDataDAO.updateKeyWordByVipShop(customerids, time, keyword);
			String keyword2 = "状态发生时间";

			this.b2cDataDAO.updateKeyWordByVipShop(customerids, time, keyword2);

		} catch (Exception e) {
			this.logger.error("干线回单异常", e);

		}
	}

	/**
	 * 反馈的处理方法 揽退
	 * 
	 * @param vipshop
	 * @param vip_code
	 * @param flowordertype
	 */
	private long sendCwbStatus_To_VipShop_LanTui(VipShop vipshop, int flowordertype) {

		long calcCount = 0;

		try {
			while (true) {
				calcCount++;
				String requestXML = "";
				try {
					List<B2CData> vipshopDataList = this.b2cDataDAO.getDataListByFlowStatus(flowordertype, vipshop.getCustomerids(), vipshop.getSendMaxCount());
					if ((vipshopDataList == null) || (vipshopDataList.size() == 0)) {
						this.logger.info("当前没有要推送[vipshop]的数据,状态:flowordertype={}", flowordertype);
						return 0;
					}
					String request_time = DateTimeUtil.getNowTime();
					requestXML = this.AppendXMLString_lantui(vipshop, vipshopDataList, flowordertype, request_time);

					if (requestXML == null) {
						this.logger.info("当前没有要推送[vipshop]的数据，判断揽退,状态:flowordertype={}", flowordertype);
						return 0;
					}

					String MD5Str = this.sendCreateMD5Str_lantui(vipshopDataList, flowordertype, request_time, vipshop, requestXML);

					String Sign = VipShopMD5Util.MD5(MD5Str).toLowerCase();
					String response_XML = null;
					try {
						response_XML = this.soapHandler.HTTPInvokeWs(vipshop.getSendCwb_URL(), VipShopConfig.nameSpace, VipShopConfig.statusBackMethodNameNew, requestXML, Sign,
								VipShopConfig.TUIHUO_TYPE);
					} catch (Exception e) {
						this.logger.error("推送vipshop状态-异常！推送XML信息：" + response_XML + ",异常原因：" + e, e);
						e.printStackTrace();
						return 0;
					}
					this.logger.info("vipshop状态反馈XML={},flowordertype={}", ReaderXMLHandler.parseBack(response_XML), flowordertype);
					Map<String, Object> parseMap = null;
					try {
						parseMap = this.readXMLHandler.parseGetCwbInfo_VipShopXml(response_XML);
						this.logger.info("解析后的XML-Map,flowordertype={},parseMap={}", flowordertype, parseMap);
					} catch (Exception e) {
						this.logger.error("解析vipshop返回订单信息异常!,异常原因：" + e, e);
						e.printStackTrace();
						return 0;
					}
					String sys_response_code = parseMap.get("sys_response_code") != null ? parseMap.get("sys_response_code").toString() : ""; // 返回码
					String sys_response_msg = parseMap.get("sys_respnose_msg") != null ? parseMap.get("sys_respnose_msg").toString() : ""; // 返回说明

					try {
						VipShopExceptionHandler.respValidateMessage(sys_response_code, sys_response_msg, vipshop);
					} catch (Exception e) {
						this.logger.error("返回vipshop订单查询信息验证失败！", e);
						e.printStackTrace();
						return 0;
					}

					if ("S00".equals(sys_response_code)) {
						this.DealWithResponseByVipShop(vipshop, flowordertype, parseMap, sys_response_msg);
					}

				} catch (Exception e) {
					String exptMessage = "[唯品会]订单状态反馈发送不可预知的异常！当前状态=" + flowordertype + "，当前请求的XML=" + requestXML;
					this.logger.error(exptMessage, e);
					return 0;
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return calcCount;

	}

	/**
	 * VipShop生成签名字符串
	 */
	public String sendCreateMD5Str_lantui(List<B2CData> vipshopDataList, int flowordertype, String request_time, VipShop vipshop, String requestXML) {
		String MD5Str = "";

		String version = "1.0";
		MD5Str = vipshop.getPrivate_key() + version + request_time + vipshop.getShipper_no();
		StringBuffer sub1 = new StringBuffer(MD5Str);

		for (B2CData b2cData : vipshopDataList) {
			String jsoncontent = b2cData.getJsoncontent();
			VipShopXMLNote note = this.getVipShopXMLNoteMethod(jsoncontent);
			if (note.getCwbordertypeid() != CwbOrderTypeIdEnum.Shangmentui.getValue()) {
				this.logger.info("当前推送唯品会{}过滤配送单,flowordertype={}", b2cData.getCwb(), b2cData.getFlowordertype());
				continue;
			}
			String order_status_info = note.getOrder_status_info();
			// 补充上门揽件
			if (note.getOrder_status().equals(String.valueOf(VipShopFlowEnum.ShangMenTuiChengGong_t.getVipshop_state()))
					|| note.getOrder_status().equals(String.valueOf(VipShopFlowEnum.ShengMenJuTui_t.getVipshop_state()))) { // 如果是34状态
																															// 则自动创建虚拟
				String order_status_time = ((note.getShangmenlanshoutime() == null) || note.getShangmenlanshoutime().isEmpty() ? note.getOrder_status_time() : note.getShangmenlanshoutime()); // 上门揽退状态
																																																// //
																																																// 上门揽退状态
				String order_status_infos = "上门揽件";
				sub1.append((VipShopCwbFeedBackService.parseStrAdd(b2cData.getB2cid() + "_temp")));
				sub1.append(note.getOrder_sn());
				sub1.append(VipShopFlowEnum.ShangMenLanJian_t.getVipshop_state());
				sub1.append(order_status_infos);
				sub1.append(note.getCurrent_city_name());
				sub1.append(order_status_time);
				sub1.append(note.getSign_man());
			}
			sub1.append((VipShopCwbFeedBackService.parseStrAdd(b2cData.getB2cid() + "")));
			sub1.append(note.getOrder_sn());
			sub1.append(note.getOrder_status());
			sub1.append(order_status_info);
			sub1.append(note.getCurrent_city_name());
			sub1.append(note.getOrder_status_time());
			sub1.append(note.getSign_man());
		}

		return sub1.toString().replaceAll("null", "");
	}

	public static void main(String[] args) {
		String xml = "132456<delivery_name>";
		xml = xml.substring(0, xml.indexOf("<delivery_name>"));
		System.out.println(xml);
	}

}
