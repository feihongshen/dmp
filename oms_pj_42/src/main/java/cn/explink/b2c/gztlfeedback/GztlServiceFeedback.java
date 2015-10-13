package cn.explink.b2c.gztlfeedback;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.NewBeanInstanceStrategy;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.explink.xmldto.OrderDto;
import cn.explink.b2c.explink.xmldto.OrderFlowDto;
import cn.explink.b2c.gztl.CuscodeAndCustomerNameEnum;
import cn.explink.b2c.gztl.GztlService;
import cn.explink.b2c.gztl.sendFeedbackData.SendFeedbackData;
import cn.explink.b2c.gztl.sendFeedbackData.SendFeedbackDatas;
import cn.explink.b2c.gztlfeedback.feedbackreturndata.TMSSendOrder;
import cn.explink.b2c.gztlfeedback.feedbacksenddata.SendMSD;
import cn.explink.b2c.gztlfeedback.feedbacksenddata.SendOrder;
import cn.explink.b2c.gztlfeedback.feedbackxmldto.InsertFeedbackOrderData;
import cn.explink.b2c.gztlfeedback.feedbackxmldto.OrderFeedback;
import cn.explink.b2c.maisike.MaisikeService_Send2LvBranch;
import cn.explink.b2c.tools.B2cDataOrderFlowDetail;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.CacheBaseListener;
import cn.explink.b2c.tools.ExptReason;
import cn.explink.b2c.tools.JacksonMapper;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.CommonSendDataDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.WarehouseCommenDAO;
import cn.explink.domain.Customer;
import cn.explink.domain.WarehouseToCommen;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.MD5.MD5Util;

import com.fyps.web.webservice.monternet.EcisServiceHttpBindingStub;
import com.fyps.web.webservice.monternet.TraceArgs;

@Service
public class GztlServiceFeedback {
	private Logger logger = LoggerFactory.getLogger(MaisikeService_Send2LvBranch.class);
	private static final String FUJIANZHAN="fjFeiYuan";
	private static final String ZHEJIANGZHAN="zjabc";
	private static final String JIANGXIZHAN="jxfy";
	private static final String HUBEIZHAN="hbFeiYuan";
	@Autowired
	private GetDmpDAO getDmpDAO;
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;
	@Autowired
	private B2cTools b2ctools;

	@Autowired
	WarehouseCommenDAO WarehouseCommenDAO;
	@Autowired
	WarehouseCommenDAO warehouseCommenDAO;
	@Autowired
	CommonSendDataDAO commonSendDataDAO;

	@Autowired
	CacheBaseListener cacheBaseListener;
	
	/**
	 * 获取广州通路配置信息并转化成为Gztl这个类中的信息
	 *
	 * @param key
	 * @return
	 */
	public GztlFeedback getGztlFeedback(int key) {
		GztlFeedback gztl = null;
		if (this.getObjectMethod(key) != null) {
			JSONObject jsonObj = JSONObject.fromObject(this.getObjectMethod(key));
			gztl = (GztlFeedback) JSONObject.toBean(jsonObj, GztlFeedback.class);
		} else {
			gztl = new GztlFeedback();
		}
		return gztl;
	}

	/**
	 * 获取广州通路配置信息的接口
	 *
	 * @param key
	 * @return
	 */
	private String getObjectMethod(int key) {
		try {
			JointEntity obj = this.getDmpDAO.getJointEntity(key);
			return obj.getJoint_property();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 推送出库二级站信息广州通路
	 */
	public long sendTwoLeavelBranch() {
		List<WarehouseToCommen> datalist = new ArrayList<WarehouseToCommen>();
		long calcCount = 0;
		int b2cenum = B2cEnum.GuangzhoutongluWaifadan.getKey();

		if (!this.b2ctools.isB2cOpen(b2cenum)) {
			this.logger.info("未开出库0外发广州通路0的对接!");
			return -1;
		}
		GztlFeedback gztl = this.getGztlFeedback(b2cenum);

		long maxCount = gztl.getSearch_number();

		for (int i = 0; i < 1; i++) {
			try {
				long loopcount = gztl.getLoopCount() == 0 ? 5 : gztl.getLoopCount(); // 重发次数5

				for (ShipperIdAndShipperCode temp : ShipperIdAndShipperCode.values()) {
					String shippercode=temp.getShipped_code();
					String logisticproviderid = String.valueOf(temp.getLogisticproviderid());
					List<WarehouseToCommen> datalist01 = this.WarehouseCommenDAO.getCommenCwbListByCommon(shippercode, maxCount, loopcount); // 查询所有未推送数据
					datalist.addAll(datalist01);
				}
				if ((datalist == null) || (datalist.size() == 0)) {
					this.logger.info("当前没有待发送下游的数据-外发广州通路");
					return 0;
				}

				Map<String, WarehouseToCommen> sss = new HashMap<String, WarehouseToCommen>();
				for (WarehouseToCommen ss : datalist) {
					sss.put(ss.getCwb(), ss);
				}

				calcCount += datalist.size();
				String requestCwbs = this.getRequestDMPCwbArrs(datalist); // 封装为-上游oms请求dmp的参数.
				String responseJson = this.getDmpDAO.getDMPOrdersByCwbs(requestCwbs); // 根据订单号批量
																						// 请求dmp，返回订单集合

				List<OrderDto> respOrders = JacksonMapper.getInstance().readValue(responseJson, new TypeReference<List<OrderDto>>() {
				});
				if ((respOrders == null) || (respOrders.size() == 0)) {
					this.logger.info("当前没有待发送的广州通路数据");
					return 1;
				}
				this.excuteSendGztl_forward(gztl, datalist, respOrders, sss); // 正向物流配送

			} catch (Exception e) {
				// TODO Auto-generated catch block
				this.logger.error("处理发送外发广州通路数据发生未知异常", e);
			}

		}
		return calcCount;
	}

	private String getRequestDMPCwbArrs(List<WarehouseToCommen> datalist) {
		JSONArray jsonArr = new JSONArray();
		for (WarehouseToCommen common : datalist) {
			JSONObject jsonobj = new JSONObject();
			jsonobj.put("cwb", common.getCwb());
			jsonArr.add(jsonobj);
		}
		String requestCwbs = jsonArr.toString();
		return requestCwbs;
	}

	/**
	 * 执行配送类型订单的推送 正向物流 （包括配送）
	 *
	 * @param maisike
	 * @param datalist
	 * @param respOrders
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	private void excuteSendGztl_forward(GztlFeedback gztl, List<WarehouseToCommen> datalist, List<OrderDto> respOrders, Map<String, WarehouseToCommen> comenMap) {
		try {

			List<SendFeedbackData> orders = this.buildOutLv2BranchList_forward(respOrders, datalist, comenMap); // 构建请求List
			if ((orders == null) || (orders.size() == 0)) {
				this.logger.info("正向-当前没有推送外发单广州通路订单信息");
				return;
			}
			SendFeedbackDatas sendFeedbackDatas = new SendFeedbackDatas();
			sendFeedbackDatas.setTmsSendOrders(orders);
			String xmlString = this.beanToXml(sendFeedbackDatas);
			
			System.out.println(xmlString);
			// kk
			//String url = "http://119.145.78.171:8086/ECIS-INF/services/EcisService";
			String url = gztl.getSearch_url();

			EcisServiceHttpBindingStub ce = new EcisServiceHttpBindingStub(new URL(url), new org.apache.axis.client.Service());
			TraceArgs traceArgs = new TraceArgs();
			traceArgs.setCode(gztl.getCode());
			//System.out.println(gztl.getCode());
			traceArgs.setInvokeMethod(gztl.getInvokeMethod());
			//System.out.println(gztl.getInvokeMethod());
			//System.out.println(gztl.getPrivate_key());
			String sign = MD5Util.md5(xmlString + gztl.getPrivate_key());
			this.logger.info("广州通路key信息={}", sign);
			traceArgs.setSign(sign);
			traceArgs.setXml(URLEncoder.encode(xmlString,"utf-8"));
			this.logger.info("广州通路反馈兄弟推送出库xml信息={}",URLEncoder.encode(xmlString,"utf-8"));
			String responseData = URLDecoder.decode(ce.orderAndFeedbackApi(traceArgs), "UTF-8");
			this.logger.info("广州通路反馈兄弟推送出库返回来的响应：={}",responseData);
			//System.out.println(responseData);
			/**
			 * 在下面利用webservice实现参数的传递????还没实现
			 */
			// JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
			// factory.getInInterceptors().add(new LoggingInInterceptor());
			// factory.getOutInterceptors().add(new LoggingOutInterceptor());
			// factory.setAddress("http://model.web.fyps.com");
			// factory.setWsdlURL(gztl.getSearch_url());
			// factory.setServiceClass(GztlFeedbackWebservice.class);
			// GztlFeedbackWebservice service = (GztlFeedbackWebservice)
			// factory.create();
			// String responseData = service.orderAndFeedbackApi(gztl.getCode(),
			// gztl.getInvokeMethod(), gztl.getSign(), xmlString);
			// String responseData =
			// RestHttpServiceHanlder.sendHttptoServer(gztl.getCode(),
			// gztl.getInvokeMethod(), gztl.getSign(), xmlString); // 请求并返回,四个参数
			// String responseData = "";
			String requestJsonString = "{code:\"" + gztl.getCode() + "\"," + "invokeMethod:\"" + gztl.getInvokeMethod() + "\"," + "sign:\"" + gztl.getSign() + "\"," + "xmlString:\"" + xmlString
					+ "\"}";
			/**
			 * 对得到的订单进行表中的发送的标志进行修改cwbs
			 */
			this.logger.info("广州通路可返回信息={}", responseData);
			this.dealwithResponse(datalist, requestJsonString, responseData); // 返回处理
		} catch (Exception e) {
			this.logger.error("处理发送广州通路物流异常", e);
		}

	}

	private List<SendFeedbackData> buildOutLv2BranchList_forward(List<OrderDto> respOrders, List<WarehouseToCommen> commondataList, Map<String, WarehouseToCommen> comenMap)
			throws UnsupportedEncodingException {
		List<SendFeedbackData> orderList = new ArrayList<SendFeedbackData>();
		// List<Branch> branchlist = this.getDmpDAO.getAllBranchs();这个有什么作用
		for (OrderDto orderDto : respOrders) {
			String cwb = orderDto.getCwb();
			WarehouseToCommen wCommen = comenMap.get(cwb);
			if (orderDto.getCwbordertypeid() == CwbOrderTypeIdEnum.Peisong.getValue()||orderDto.getCwbordertypeid()==CwbOrderTypeIdEnum.Shangmenhuan.getValue()||orderDto.getCwbordertypeid()==CwbOrderTypeIdEnum.Shangmentui.getValue()) { // 只限于操作正向配送的订单数据
				try {
					SendFeedbackData sData = new SendFeedbackData();
					sData.setWaybillNo(orderDto.getCwb());// 运单号
					String subWaybillNo = "";
					if ((!GztlServiceFeedback.removezero(orderDto.getRemark1()).equals(orderDto.getTranscwb()))&&!(orderDto.getTranscwb().isEmpty())) {
						subWaybillNo = orderDto.getTranscwb();
					}
					sData.setSubWaybillNo(subWaybillNo);// 子运单号？？
					sData.setOrderNo(orderDto.getCwb());// 订单号
					
					
					
					Customer customer = this.cacheBaseListener.getCustomer(orderDto.getCustomerid());
					
					CuscodeAndCustomerNameEnum	customerCodeEnum = getCustomerEnum(customer);
					
					
					sData.setCustCode(customerCodeEnum.getCuscode());
					sData.setCustName(customerCodeEnum.getCustomerName());// 客户名称???
				
					
					sData.setOrderType(orderDto.getCwbordertypeid() + "");
					sData.setOrderDate(comenMap.get(orderDto.getCwb()).getCredate());// 订单日期
					sData.setDeliveryman("");// 配送人
					sData.setDeliverymanPhone("");// 配送人电话
					sData.setDeliverymanAddress("");// 配送人地址
					sData.setReceiverName(orderDto.getConsigneenameOfkf());
					String receiverPhone = "";
					if ((orderDto.getConsigneephoneOfkf() != null)&&(orderDto.getConsigneemobileOfkf() != null)) {
						receiverPhone = GztlServiceFeedback.removezero(orderDto.getConsigneephoneOfkf())+","+orderDto.getConsigneemobileOfkf();
					} else if (orderDto.getConsigneephoneOfkf() != null) {
						receiverPhone=GztlServiceFeedback.removezero(orderDto.getConsigneephoneOfkf());
					}else if (orderDto.getConsigneemobileOfkf() != null) {
						receiverPhone=GztlServiceFeedback.removezero(orderDto.getConsigneemobileOfkf());
					}
					sData.setReceiverPhone(receiverPhone);// 收件人电话
					sData.setReceiverAddress(orderDto.getConsigneeaddress());
					sData.setGoodsDetail(orderDto.getSendcargoname());
					String goodsNumString="";
					if (orderDto.getSendcargonum()==0) {
						goodsNumString=orderDto.getBackcargonum()+"";
					}else {
						goodsNumString=orderDto.getSendcargonum()+"";
					}
					sData.setGoodsNum(goodsNumString);// 配送件数
					sData.setDeliveryAmount("");// 配送合计
					sData.setWeight(orderDto.getCargorealweight() + "");
					String receiveable="";
					if (orderDto.getReceivablefee().toString().equals("0.00")) {
						receiveable=orderDto.getPaybackfee().toString();
					}else {
						receiveable=orderDto.getReceivablefee().toString();
					}
					sData.setReceivable(receiveable);// 应收款
					
					sData.setShippedDate(GztlServiceFeedback.parseDate(orderDto.getRemark2()));//发货时间
					sData.setInsurAmount("");
					String logisticproviderid = "";
					String shippedcode="";
					for (ShipperIdAndShipperCode shipperCode : ShipperIdAndShipperCode.values()) {
						if (shipperCode.getShipped_code().equalsIgnoreCase(wCommen.getCommencode())) {
							logisticproviderid = String.valueOf(shipperCode.getLogisticproviderid());
							shippedcode=shipperCode.getShipped_code();
							break;
						}
					}
					sData.setShippedCode(shippedcode);// 承运商代码？？？
					sData.setLogisticproviderid(logisticproviderid);// 承运商ID
					orderList.add(sData);
				} catch (Exception e) {
					e.printStackTrace();
					this.logger.error("构建单个对象异常cwb=" + orderDto.getCwb(), e);
				}
			}

		}

		return orderList;

	}

	private CuscodeAndCustomerNameEnum getCustomerEnum(Customer customer) {
		for (CuscodeAndCustomerNameEnum element : CuscodeAndCustomerNameEnum.values()) {
			if (element.getCuscode().equals(customer.getCustomercode())) {
				return element;
			}

		}
		return null;
	}
	
	/**
	 * 处理广州通路返回的信息节点
	 *
	 * @param datalist
	 * @param appdata
	 * @param responseData
	 * @throws IOException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 */
	private void dealwithResponse(List<WarehouseToCommen> datalist, String requestJsonString, String responseData) throws IOException, JsonParseException, JsonMappingException {
		List<String> cwbsList = new ArrayList<String>();
		if ((responseData == null) || responseData.isEmpty()) {
			this.logger.info("推送外发单广州通路订单信息返回空,原请求data={}", requestJsonString);
			for (WarehouseToCommen wcomm : datalist) {
				this.WarehouseCommenDAO.updateCommenCwbListBycwb(wcomm.getCwb(), "2", "请求返回空");
			}

			// continue;
		}
		TMSSendOrder tmSendOrder = (TMSSendOrder) this.xmlToObj(responseData, new TMSSendOrder());
		String cwbs = tmSendOrder.getWaybillNo();
		for (WarehouseToCommen warehouseToCommen : datalist) {
			cwbsList.add(warehouseToCommen.getCwb());
		}
		if ((cwbs != null) && (cwbs.length() > 0)) {
			String[] orders = cwbs.split(",");
			for (String cwb : orders) {
				this.WarehouseCommenDAO.updateCommenCwbListBycwb(cwb, DateTimeUtil.getNowTime(), "成功");
				cwbsList.remove(cwb);
			}
		}
		if ((cwbsList != null) && (cwbsList.size() > 0)) {
			for (String failcwb : cwbsList) {
				this.WarehouseCommenDAO.updateCommenCwbListBycwb(failcwb, "2", "失败");
			}
		}

	}

	/**
	 * 利用jaxb将字符串形式的xml生成ban
	 */
	public String beanToXml(Object object) {
		StringWriter strWriter = new StringWriter();
		String xmlString = "";
		try {
			JAXBContext context = JAXBContext.newInstance(object.getClass());
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
			marshaller.marshal(object, strWriter);
			xmlString = strWriter.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			this.logger.error("构建返回供货商的xml=" + xmlString, e);
		}
		return xmlString;
	}

	/**
	 * jaxb将xml转化为对象
	 *
	 * @param xml
	 * @param object
	 * @return
	 */
	public Object xmlToObj(String xml, Object object) {
		StringReader stringReader = new StringReader(xml);
		Object obj = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			obj = unmarshaller.unmarshal(stringReader);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			this.logger.error("将xml解析成GztlXmlElement类时出错", e);
		}
		return obj;
	}

	/**
	 * 推送出库二级站信息
	 *
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	public String receivedOrderFeedback(String xml, GztlFeedback gztl) throws Exception {
		
		List<SendOrder> sendOrders = new ArrayList<SendOrder>();
		String returnXml = "";
		StringBuffer noCwbs = new StringBuffer();
		StringBuffer yesCwbs = new StringBuffer();
		StringBuffer repeatCwbs = new StringBuffer();
		String repeatCwbString = "";
		String noCwbString = "";
		String yesCwbString = "";
		InsertFeedbackOrderData inOrderData = (InsertFeedbackOrderData) this.xmlToObj(xml, new InsertFeedbackOrderData());
		if (inOrderData == null) {
			this.logger.warn("外发单广州通路订单接收时，在推送出库二级站信息时，{}", "通过jaxb生成的InsertFeedbackOrderData类为空");
			return this.errorReturnData("F", "外发单信息接收时接收到的信息无feedback订单信息，请核查xml结构！！");
		}
		List<OrderFeedback> feedbacks = inOrderData.getFeedbacks();
		if ((feedbacks != null) && (feedbacks.size() > 0)) {
			for (OrderFeedback orderFeedback : feedbacks) {
				String cwb = orderFeedback.getId();//cwb为对方发来的id
				String willOrder=orderFeedback.getWaybillNo();
				// 在commen_cwb_order表中验证是否存在该订单的信息，在出库到广西飞远的站点时，会在里面生成信息
				long isexistscwbflag = this.warehouseCommenDAO.getCountByCwb(willOrder);
				if (isexistscwbflag > 0) {

					int flowordertype = this.getFlowordertype(orderFeedback.getStatus());// 流程状态
					long deliverystate = this.getDeliveryState(orderFeedback.getStatus());// 接收状态
					String logisticProviderId="";
					if(GztlServiceFeedback.FUJIANZHAN.equals(orderFeedback.getLogisticProviderId())){
						logisticProviderId="fjabc";
					}else if(GztlServiceFeedback.HUBEIZHAN.equals(orderFeedback.getLogisticProviderId())){
						logisticProviderId="hbfyorder";
					}else if(GztlServiceFeedback.JIANGXIZHAN.equals(orderFeedback.getLogisticProviderId())){
						logisticProviderId="jxfyorder";
					}else if(GztlServiceFeedback.ZHEJIANGZHAN.equals(orderFeedback.getLogisticProviderId())){
						logisticProviderId="GZTL";
					}else{
						logisticProviderId=orderFeedback.getLogisticProviderId();
					}
					// 验证是否有重发订单插入
					long isrepeatFlag = this.commonSendDataDAO.isExistsCwbFlag1(willOrder, logisticProviderId, orderFeedback.getOperatorTime(), String.valueOf(flowordertype),deliverystate);
					if (isrepeatFlag > 0) {
						repeatCwbs.append("," + cwb);
						continue;
					}
					yesCwbs.append("," + cwb);
					OrderFlowDto orderFlowDto = this.buildOrderFlowDto(orderFeedback, flowordertype, deliverystate);
					String jsonContent = JacksonMapper.getInstance().writeValueAsString(orderFlowDto);
					// 插入上游OMS临时表
					this.commonSendDataDAO.creCommenSendData(willOrder, 0, logisticProviderId.toUpperCase(), DateTimeUtil.getNowTime(), orderFeedback.getOperatorTime(), jsonContent, deliverystate,flowordertype,
							 "0");// 倒数第一个与倒数第二个分别为deliverystate与flowordertype不知道
					// 自动补审核
					if (flowordertype == FlowOrderTypeEnum.YiFanKui.getValue()) {
						flowordertype = FlowOrderTypeEnum.YiShenHe.getValue();
						orderFlowDto.setFlowordertype("36");
						String content = JacksonMapper.getInstance().writeValueAsString(orderFlowDto);
						// 插入上游OMS临时表
						this.commonSendDataDAO.creCommenSendData(willOrder, 0, logisticProviderId.toUpperCase(), DateTimeUtil.getNowTime(), orderFeedback.getOperatorTime(), content, deliverystate,
								flowordertype, "0");

					}

				} else {
					noCwbs.append("," + cwb);
				}
			}
			if ((yesCwbs != null) && (yesCwbs.length() > 0)) {
				yesCwbString = yesCwbs.substring(1, yesCwbs.length()).toString();
				String[] yesStrings = yesCwbString.split(",");
				for (String temp : yesStrings) {
					SendOrder sendOrder = new SendOrder();
					sendOrder.setId(temp);
					sendOrder.setRemark("接收反馈状态成功");
					sendOrder.setResult("T");
					sendOrders.add(sendOrder);
				}
				// String yesString = this.feedBackResponseXml(yesCwbString,
				// "T", "接收反馈状态成功");

			}
			if ((noCwbs != null) && (noCwbs.length() > 0)) {
				noCwbString = noCwbs.substring(1, noCwbs.length()).toString();
				String[] noString = noCwbString.split(",");
				for (String temp : noString) {
					SendOrder sendOrder = new SendOrder();
					sendOrder.setId(temp);
					sendOrder.setRemark("数据库中无此订单");
					sendOrder.setResult("F");
					sendOrders.add(sendOrder);
				}

			}
			if ((repeatCwbs != null) && (repeatCwbs.length() > 0)) {
				repeatCwbString = repeatCwbs.substring(1, repeatCwbs.length()).toString();
				String[] reStrings = repeatCwbString.split(",");
				for (String temp : reStrings) {
					SendOrder sendOrder = new SendOrder();
					sendOrder.setId(temp);
					sendOrder.setRemark("重发订单不允许插入");
					sendOrder.setResult("F");
					sendOrders.add(sendOrder);
				}
			}
			/**
			 * 将生成的类对象转化成返回的xml格式的数据
			 */
			if ((sendOrders != null) && (sendOrders.size() > 0)) {
				SendMSD sendMSD = new SendMSD();
				sendMSD.setOrders(sendOrders);
				returnXml = this.beanToXml(sendMSD);
			}
			return returnXml;
		}

		return null;
	}

	/**
	 * 生成OrderFlowDto,转化成所需要的jsoncontent
	 *
	 * @param userCode
	 * @param orderStatus
	 * @param cwb
	 * @param flowordertype
	 * @param deliverystate
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private OrderFlowDto buildOrderFlowDto(OrderFeedback orderFeedback, int flowordertype, long deliverystate) throws UnsupportedEncodingException {
		//ExptReason reason = this.getDmpDAO.getReasonidJointByB2c(Long.valueOf(orderFeedback.getReason()), orderFeedback.get,orderFeedback.getWaybillNo());
		OrderFlowDto orderFlowDto = new OrderFlowDto();
		orderFlowDto.setCustid("0");
		orderFlowDto.setCwb(orderFeedback.getWaybillNo());
		orderFlowDto.setDeliverystate(String.valueOf(deliverystate));// 接收状态
		orderFlowDto.setFloworderdetail(orderFeedback.getRemark());// 备注
		orderFlowDto.setFlowordertype(String.valueOf(flowordertype));//流程
		orderFlowDto.setOperatortime(orderFeedback.getOperatorTime());
		orderFlowDto.setDeliveryname(orderFeedback.getDeliveryman());//小件员姓名
		orderFlowDto.setDeliverymobile(orderFeedback.getDeliverymanMobile());//小件员电话
		long customerid=this.WarehouseCommenDAO.getCommenCwbBycwb(orderFeedback.getWaybillNo()).getCustomerid();
		if (flowordertype==35||flowordertype==36) {
			String except_code=orderFeedback.getStatus()+"_"+orderFeedback.getReason();
			ExptReason exptReason=this.b2ctools.getGztlExptReason(-2,except_code);
			String content="";
			if (exptReason!=null) {
				content=exptReason.getExpt_code();//我们系统里面的信息异常原因
				orderFlowDto.setExptcode(exptReason.getReasonid());//异常编码
				
		}
		}
		
		//orderFlowDto.setExptmsg("");//异常原因
		
		if (orderFeedback.getPayMethod() == null) {
			orderFlowDto.setPaytype(1); // 支付方式 待定义??
		} else if (orderFeedback.getPayMethod().equals("现金") || orderFeedback.getPayMethod().equals("0")) {
			orderFlowDto.setPaytype(1);
		} else {
			orderFlowDto.setPaytype(2);
		}

		orderFlowDto.setRequestTime(DateTimeUtil.getNowTime("yyyyMMddHHmmss"));
		orderFlowDto.setUserCode(String.valueOf(B2cEnum.GuangzhoutongluWaifadan.getKey()));
		return orderFlowDto;
	}

	private int getFlowordertype(String order_status) {
		//中转
		if (order_status.equals(GztlFeedbackEnum.ZhongZhuanChuZhan)) {
			return FlowOrderTypeEnum.ChuKuSaoMiao.getValue();
		}
		if (order_status.equals(GztlFeedbackEnum.ZhongzhuanZhanRuKu)) {
			return FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue();
		}
		if (order_status.equals(GztlFeedbackEnum.ZhuangZhuanZhanChuKuSaoMiao)) {
			return FlowOrderTypeEnum.ZhongZhuanZhanChuKuSaoMiao.getValue();
		}
		// 分站到货
		if (order_status.equals(GztlFeedbackEnum.FenZhanDaoHuo.getState())) {
			return FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue();
		}
		// 分站领货
		if (order_status.equals(GztlFeedbackEnum.Deliverying.getState())) {
			return FlowOrderTypeEnum.FenZhanLingHuo.getValue();
		}
		if (order_status.equals(GztlFeedbackEnum.TuiGongYingShangChuKu.getState())) {
			return FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue();
		}
		if (order_status.equals(GztlFeedbackEnum.TuiHuoZhanRuKu.getState())) {
			return FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue();
		}
		if (order_status.equals(GztlFeedbackEnum.TuiHuoChuZhan.getState())) {
			return FlowOrderTypeEnum.TuiHuoChuZhan.getValue();
		}
		if (order_status.equals(GztlFeedbackEnum.GongYingShangJuShouFanKu.getState())) {
			return FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue();
		}
		// 成功
		if (order_status.equals(GztlFeedbackEnum.ShouGongdiushi.getState()) || order_status.equals(GztlFeedbackEnum.BufenJushou.getState())
				|| order_status.equals(GztlFeedbackEnum.KehuYanqi.getState()) || order_status.equals(GztlFeedbackEnum.Peisongchenggong.getState())
				|| order_status.equals(GztlFeedbackEnum.Peisongshibai.getState()) || order_status.equals(GztlFeedbackEnum.Peisongyanchi.getState())) {
			return FlowOrderTypeEnum.YiFanKui.getValue();
		}

		return 0;
	}

	private long getDeliveryState(String order_status) {

		if (order_status.equals(GztlFeedbackEnum.ShouGongdiushi.getState())) {
			return DeliveryStateEnum.HuoWuDiuShi.getValue();
		}
		if (order_status.equals(GztlFeedbackEnum.BufenJushou.getState())) {
			return DeliveryStateEnum.BuFenTuiHuo.getValue();
		}
		if (order_status.equals(GztlFeedbackEnum.KehuYanqi.getState())) {
			return DeliveryStateEnum.FenZhanZhiLiu.getValue();// ??
		}
		if (order_status.equals(GztlFeedbackEnum.Peisongchenggong.getState())) {
			return DeliveryStateEnum.PeiSongChengGong.getValue();
		}
		if (order_status.equals(GztlFeedbackEnum.Peisongshibai.getState())) {
			return DeliveryStateEnum.QuanBuTuiHuo.getValue();// ?????????????
		}
		if (order_status.equals(GztlFeedbackEnum.Peisongyanchi.getState())) {
			return DeliveryStateEnum.FenZhanZhiLiu.getValue();// ??
		}

		return 0;
	}

	/**
	 * 系统中像签名错误等信息的返回构成的xml形式
	 *
	 * @param flag
	 * @param remark
	 * @return
	 */
	public String errorReturnData(String flag, String remark) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<MSD>");
		buffer.append("<Orders>");
		buffer.append("<Order>");
		buffer.append("<id></id>");
		buffer.append("<result>" + flag + "</result>");
		buffer.append("<remark>" + remark + "</remark>");
		buffer.append("</Order>");
		buffer.append("</Orders>");
		buffer.append("</MSD>");
		return buffer.toString();
	}
	

	/**
	 * 响应给客户的xml格式的信息
	 *
	 * @param cwb
	 * @param flag
	 * @param remark
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 */
	/*
	 * private String feedBackResponseXml(String cwb, String flag, String
	 * remark) { StringBuffer xmlBuffer = new StringBuffer();
	 * xmlBuffer.append("<MSD>"); xmlBuffer.append("<Orders>"); for (String temp
	 * : cwb.split(",")) { xmlBuffer.append("<Order>"); xmlBuffer.append("<id>"
	 * + temp + "</id>"); xmlBuffer.append("<result>" + flag + "</result>");
	 * xmlBuffer.append("<remark>" + remark + "</remark>");
	 * xmlBuffer.append("</Order>"); } xmlBuffer.append("</Orders>");
	 * xmlBuffer.append("</MSD>"); String xml = xmlBuffer.toString();
	 * this.logger.info("返回广州通路外发单订单反馈-xml={}", xml);
	 * 
	 * return xml; }
	 */
	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {
		// GztlServiceFeedback gFeedback = new GztlServiceFeedback();
		// InsertFeedbackOrderData insertFeedbackOrderData = new
		// InsertFeedbackOrderData();
		// String xmlString =
		// "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><MSD><Feedbacks><Feedback><id>17353</id><logisticProviderId>ZSRB</logisticProviderId><waybillNo>1047541898</waybillNo><status>派件</status><stateTime>2014/9/12 9:22:00</stateTime><operatorName>761602</operatorName><operatorTime>2014-09-12 09:03:39</operatorTime><deliveryman></deliveryman><deliverymanMobile></deliverymanMobile><reason></reason><scanId>中山火炬站</scanId><preSiteName></preSiteName><nextSiteName></nextSiteName><remark></remark><payMethod></payMethod><receiveTime>2014-09-15 17:29:27</receiveTime></Feedback></Feedbacks></MSD>";
		// InsertFeedbackOrderData inOrderData = (InsertFeedbackOrderData)
		// gFeedback.xmlToObj(xmlString, insertFeedbackOrderData);
		// System.out.println(inOrderData.getFeedbacks().size());
		/*
		 * List<SendOrder> sendOrders = new ArrayList<SendOrder>(); SendOrder
		 * sendOrder = new SendOrder(); sendOrder.setId("1");
		 * sendOrder.setRemark("hh"); sendOrder.setResult("kk");
		 * sendOrders.add(sendOrder); SendMSD sendMSD = new SendMSD();
		 * sendMSD.setOrders(sendOrders); GztlServiceFeedback gFeedback = new
		 * GztlServiceFeedback(); String xmlString =
		 * gFeedback.beanToXml(sendMSD); System.out.println(xmlString);
		 */
		/*StringBuffer logisticproviderids = new StringBuffer();
		for (ShipperIdAndShipperCode temp : ShipperIdAndShipperCode.values()) {
			logisticproviderids.append(",\"" + temp.getLogisticproviderid() + "\"");
		}
		String ccString = logisticproviderids.substring(1, logisticproviderids.length()).toString();
		System.out.println(ccString);*/
		/*GztlServiceFeedback gztlServiceFeedback=new GztlServiceFeedback();
		OrderFlowDto orderFlowDto=new OrderFlowDto();
		orderFlowDto.setFlowordertype("35");
		
		String content = JacksonMapper.getInstance().writeValueAsString(orderFlowDto);
		System.out.println(content);
		orderFlowDto.setFlowordertype("36");
		String content1 = JacksonMapper.getInstance().writeValueAsString(orderFlowDto);
		System.out.println(content1);*/
		

	}
	public static String  removezero(String content){
		StringBuffer buffer=new StringBuffer();
		if (content.indexOf(".0")!=-1) {
			String[] arrayContent=content.split("\\.0");
			for (int i = 0; i < arrayContent.length; i++) {
				buffer.append(arrayContent[i]);
			}
		}else {
			buffer.append(content);
		}
		return buffer.toString();
	}
	public static String  parseDate(String date){
		if(date.equals("")){
			return "";
		}
		String date5="";
		try {
			SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date date2=dateFormat.parse(date);
			SimpleDateFormat dateFormat2=new SimpleDateFormat("yyyy-MM-dd");
			date5=dateFormat2.format(date2);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date5;
	}
	
}
