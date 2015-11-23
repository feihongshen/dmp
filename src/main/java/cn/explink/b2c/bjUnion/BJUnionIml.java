package cn.explink.b2c.bjUnion;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.poscodeMapp.PoscodeMapp;
import cn.explink.b2c.tools.poscodeMapp.PoscodeMappDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.ExceptionCwbDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.User;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.exception.CwbException;
import cn.explink.pos.tools.EmployeeInfo;
import cn.explink.pos.tools.PosEnum;
import cn.explink.pos.tools.PosPayDAO;
import cn.explink.service.CwbOrderService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.MD5.MD5Util;

@Service
public class BJUnionIml implements BJUnionInterface{
	@Autowired
	JiontDAO jointDAO;
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	CwbDAO cwbDao;
	@Autowired
	UserDAO userDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	PosPayDAO posPayDAO;
	@Autowired
	ExceptionCwbDAO exceptionCwbDAO;
	@Autowired
	PoscodeMappDAO poscodeMappDAO;
	@Autowired
	CustomerDAO customerDao;
	
	//设置全局供货商变量
	private static Customer customer = null;
	
	public Customer initCustomer(){
		if(customer == null){
			customer = this.customerDao.getCustomerbyB2cenum(B2cEnum.HomeGou.getKey()+"");
		}
		return customer;
	}
	
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Override
	//处理登陆请求
	public String doLoginReq(Map<String, String> map, Object obj) throws Exception{
		String delivery_name = "";
		String delivery_dept_no = "";
		String delivery_dept = "";
		String delivery_zone = "";
		String employno = map.get("employno");
		String passwd = map.get("passwd");
		EmployeeInfo employInfo = this.jointDAO.getEmployeeInfo(employno);
		
		if(employInfo == null){
			map.put("response_code", ResponseEnum.DengLuFaildforname.getResponse_code());
			map.put("response_msg", ResponseEnum.DengLuFaildforname.getResponse_msg());
		}else{
			String psaaStr = MD5Util.md5(employInfo.getPassword()).toUpperCase();
			if(!psaaStr.equals(passwd)){
				map.put("response_code", ResponseEnum.DengLuFaild.getResponse_code());
				map.put("response_msg", ResponseEnum.DengLuFaild.getResponse_msg());
			}else{
				map.put("response_code", ResponseEnum.success.getResponse_code());
				map.put("response_msg", ResponseEnum.success.getResponse_msg());
				delivery_name = employInfo.getUsername();//
				delivery_dept_no = employInfo.getBranchcode();//单位编码
				delivery_dept = employInfo.getBranchname();
				delivery_zone = employInfo.getBranchaddress();
			}
		}
	
		String responseXmlbeforemac = getheader(map);
		String middle = 
				  "<response_code>"+map.get("response_code")+"</response_code>"
				+ "<response_msg>"+map.get("response_msg")+"</response_msg>";
		String responseXmlaftermac = "</transaction_header>"
				+ "<transaction_body>";
				if(map.get("response_code").equals(ResponseEnum.success.getResponse_code())){
					responseXmlaftermac += "<delivery_name>"+delivery_name+"</delivery_name>"
							+ "<delivery_dept_no>"+delivery_dept_no+"</delivery_dept_no>"
							+ "<delivery_dept>"+delivery_dept+"</delivery_dept>"
							+ "<delivery_zone>"+delivery_zone+"</delivery_zone>";
				}else{
					responseXmlaftermac += "";
				}
				responseXmlaftermac += "</transaction_body></transaction>";
		BJUnion bu = (BJUnion)obj;
		String responseStr = responseXmlbeforemac+middle+responseXmlaftermac;
		String private_key = bu.getPrivate_key();
		String mac = MD5Util.md5(responseStr+private_key).toUpperCase();
		this.logger.info("用于加密字符串:{},密钥信息:{}",responseStr,private_key);
		String responsexml = "<?xml version='1.0' encoding='utf-8'?>"+responseXmlbeforemac + middle + "<mac>"+mac+"</mac>"+responseXmlaftermac;
		this.logger.info("员工登陆时返回信息:{}",responsexml);
		return responsexml;
	}

	@Override
	//处理查询请求
	public String doQueryReq(Map<String, String> map, Object obj) throws Exception{
		Map<String, String> statusMap = new HashMap<String, String>();
		BJUnion bu = (BJUnion)obj;
		String amt = "";//订单金额
		String consignee = "";//收件人
		String consignee_contact = "";//收件人联系方式
		String consignee_address = "";//收件人地址
		String status = "";//快件状态
		String desc = "";//描述
		String orderno = map.get("order_no");
		String cwbTransCwb = this.cwbOrderService.translateCwb(orderno);
		CwbOrder co = this.cwbDao.getCwbByCwb(cwbTransCwb);
		
		if(co == null){
			map.put("response_code", ResponseEnum.ChaXunYiChang.getResponse_code());
			map.put("response_msg", ResponseEnum.ChaXunYiChang.getResponse_msg());
		}else{
			map.put("response_code", ResponseEnum.success.getResponse_code());
			map.put("response_msg", ResponseEnum.success.getResponse_msg());
			amt = co.getReceivablefee().toString();
			consignee = co.getConsigneename();
			consignee_contact  = co.getConsigneemobile();
			consignee_address = co.getConsigneeaddress();
			statusMap = getStatus(co);
			Iterator it=statusMap.keySet().iterator();
			while(it.hasNext()){
				Object key=it.next();
				status = key.toString();
				desc = statusMap.get(status);
			} 
		}
		String responseXmlbeforemac = getheader(map);
		String middle = 
				  "<response_code>"+map.get("response_code")+"</response_code>"
				+ "<response_msg>"+map.get("response_msg")+"</response_msg>";
		String responseXmlaftermac = "";
		if(ResponseEnum.ChaXunYiChang.getResponse_code().equals(map.get("response_code"))){
			responseXmlaftermac = "</transaction_header>"
					+ "<transaction_body>"
					+ "</transaction_body>"
					+ "</transaction>";
		}else{
			
			PoscodeMapp codemapping = this.poscodeMappDAO.getPosCodeByKey(co.getCustomerid(), PosEnum.BJUnion.getKey());
			String merchant_code="";
			if (codemapping != null) {
				merchant_code = codemapping.getCustomercode();
			}
			String ec_cwb = co.getCwb();
			try {
				if(co.getCustomerid() == initCustomer().getCustomerid()){
					ec_cwb = co.getTranscwb();
				}else{
					ec_cwb = co.getCwb();
				}
			} catch (Exception e) {
				this.logger.error("获取家有购物供货商信息异常:原因{}",e);
			}
			responseXmlaftermac = "</transaction_header>"
					+ "<transaction_body>"
					+ "<amt>"+amt+"</amt>"
					+ "<consignee>"+consignee+"</consignee>"
					+ "<consignee_contact>"+consignee_contact+"</consignee_contact>"
					+ "<consignee_address>"+consignee_address+"</consignee_address>"
					+ "<status>"+status+"</status>"
					+ "<desc>"+desc+"</desc>"
					+ "<merchant_code>"+merchant_code+"</merchant_code>"
					+ "<dlvryno></dlvryno>"//出库号
					+ "<dsorderno>"+ec_cwb+"</dsorderno>"//电商订单号
					+ "</transaction_body>"
					+ "</transaction>";
		}
	    
		String mac = MD5Util.md5(responseXmlbeforemac+middle+responseXmlaftermac+bu.getPrivate_key()).toUpperCase();
		String responsexml = "<?xml version='1.0' encoding='utf-8'?>"+responseXmlbeforemac + middle + "<mac>"+mac+"</mac>"+responseXmlaftermac;
		this.logger.info("运单查询时返回信息:{},查询单号:{}",responsexml,orderno);
		return responsexml;
	}

	@Override
	//处理签收请求
	public String doSignReq(Map<String, String> map, Object obj) throws Exception{
		//处理请求信息进行签收处理
		Map<String, String> lastMap = dealSignReq(map);//处理签收核心方法
		BJUnion bu = (BJUnion)obj;
		String responseXmlbeforemac = getheader(map);
		String middle = 
				"<response_code>"+lastMap.get("response_code")+"</response_code>"
			+ "<response_msg>"+lastMap.get("response_msg")+"</response_msg>";
		String responseXmlaftermac = "</transaction_header>"
				+ "<transaction_body>"
				+ "</transaction_body>"
				+ "</transaction>";
		String mac = MD5Util.md5(responseXmlbeforemac+middle+responseXmlaftermac+bu.getPrivate_key()).toUpperCase();
		String responsexml = "<?xml version='1.0' encoding='utf-8'?>"+responseXmlbeforemac + middle + "<mac>"+mac+"</mac>"+responseXmlaftermac;
		this.logger.info("签收操作时返回信息:{},订单号:{}",responsexml,map.get("order_no"));
		return responsexml;
	}

	@Override
	//处理撤销请求
	public String doRevokeReq(Map<String, String> map, Object obj) throws Exception{
		Map<String, String> lastMap = null; 
		lastMap = dealRevokeReq(map);
		BJUnion bu = (BJUnion)obj;
		String responseXmlbeforemac = getheader(map);
		String middle = 
				"<response_code>"+lastMap.get("response_code")+"</response_code>"
			+ "<response_msg>"+lastMap.get("response_msg")+"</response_msg>";
		String responseXmlaftermac = "</transaction_header>"
				+ "<transaction_body>"
				+ "</transaction_body>"
				+ "</transaction>";
		String mac = MD5Util.md5(responseXmlbeforemac+middle+responseXmlaftermac+bu.getPrivate_key()).toUpperCase();
		String responsexml = "<?xml version='1.0' encoding='utf-8'?>"+responseXmlbeforemac + middle + "<mac>"+mac+"</mac>"+responseXmlaftermac;
		this.logger.info("撤销反馈时返回信息:{},订单号:{}",responsexml,map.get("order_no"));
		return responsexml;
	}
	
	private String getheader(Map<String, String> map){
		String nowdatetime = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		nowdatetime = sdf.format(new Date());
		return   "<transaction>"
				+ "<transaction_header>"
				+ "<version>"+map.get("version")+"</version>"
				+ "<transaction_id>"+map.get("transaction_id")+"</transaction_id>"
				+ "<employno>"+map.get("employno")+"</employno>"
				+ "<termid>"+map.get("termid")+"</termid>"
				+ "<response_time>"+nowdatetime+"</response_time>"
				;
	}
	
	private Map<String, String> getStatus(CwbOrder co){
		Map<String, String> map = new HashMap<String, String>();
		String status = "";
		String desc = "";
		if((co.getDeliverid()==0)//在途中
				&&(co.getDeliverystate()==0)
				&&(co.getFlowordertype()!=FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue())){
			status = StatusEnum.zaitu.getIntstr();
			desc = StatusEnum.zaitu.getMsg();	
		}
		else if((co.getFlowordertype()==FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue())){
			status = StatusEnum.inbranch.getIntstr();
			desc = StatusEnum.inbranch.getMsg();
		}
		else if(co.getFlowordertype()==FlowOrderTypeEnum.FenZhanLingHuo.getValue()){
			status = StatusEnum.peisonging.getIntstr();
			desc = StatusEnum.peisonging.getMsg();
		}
		else if((co.getDeliverid()>0)//签收
				&&(co.getDeliverystate()==DeliveryStateEnum.PeiSongChengGong.getValue()
				||co.getDeliverystate()==DeliveryStateEnum.ShangMenHuanChengGong.getValue()
				||co.getDeliverystate()==DeliveryStateEnum.ShangMenTuiChengGong.getValue())
				){
			if((co.getFlowordertype()==FlowOrderTypeEnum.YiFanKui.getValue())
					||(co.getFlowordertype()==FlowOrderTypeEnum.YiShenHe.getValue())){
				status = StatusEnum.sign.getIntstr();
				desc = StatusEnum.sign.getMsg();
			}else if(co.getFlowordertype()==FlowOrderTypeEnum.CheXiaoFanKui.getValue()){
				status = StatusEnum.sign.getIntstr();
				desc = "撤销反馈状态";
			}
		}
		else if(co.getDeliverystate()==DeliveryStateEnum.FenZhanZhiLiu.getValue()
				&&(co.getFlowordertype()==FlowOrderTypeEnum.YiFanKui.getValue()||co.getFlowordertype()==FlowOrderTypeEnum.YiShenHe.getValue())){
			status = StatusEnum.zhiliu.getIntstr();
			desc = StatusEnum.zhiliu.getMsg();
		}
		else if(co.getDeliverystate()==DeliveryStateEnum.JuShou.getValue()
				||co.getDeliverystate()==DeliveryStateEnum.BuFenTuiHuo.getValue()
				||co.getDeliverystate()==DeliveryStateEnum.ShangMenJuTui.getValue()
				){
			status = StatusEnum.returns.getIntstr();
			desc = StatusEnum.returns.getMsg();
		}else{ //问题件
			status = StatusEnum.question.getIntstr();
			desc = StatusEnum.question.getMsg();
		} 
		map.put(status, desc);
		return map;
 	}
	
	//签收校验
	public Map<String, String> dealSignReq(Map<String, String> map){
		Map<String, String> lastMap = new HashMap<String, String>();
		Map<String, Object> parameters = new HashMap<String, Object>();
		String cwbORtranscwb = map.get("order_no");
		String cwbTransCwb = this.cwbOrderService.translateCwb(cwbORtranscwb); // 可能是订单号也可能是运单号
		CwbOrder cwbOrder = this.cwbDao.getCwbDetailByCwbAndDeliverId(0, cwbTransCwb);
		User user = null;
		DeliveryState ds = null;
		long deliveryid = 0;
		try{
			if(cwbOrder==null){
				lastMap.put("response_code",ResponseEnum.ChaXunYiChang.getResponse_code());
				lastMap.put("response_msg", ResponseEnum.ChaXunYiChang.getResponse_msg());
				return lastMap;
			}
			user = getUserName(cwbOrder);
			
			if(user!=null){
				deliveryid = user.getUserid();
				ds = this.deliveryStateDAO.getActiveDeliveryStateByCwb(cwbOrder.getCwb());
				BigDecimal pos = BigDecimal.ZERO;
				BigDecimal cash = BigDecimal.ZERO;
				BigDecimal paybackedfee = BigDecimal.ZERO;
				BigDecimal businessfee = BigDecimal.ZERO;
				if(ds != null){
					if((ds.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue()
							||ds.getDeliverystate() == DeliveryStateEnum.ShangMenHuanChengGong.getValue()
							||ds.getDeliverystate() == DeliveryStateEnum.ShangMenTuiChengGong.getValue())
							&&(cwbOrder.getFlowordertype()==FlowOrderTypeEnum.YiShenHe.getValue())){
						lastMap.put("response_code",ResponseEnum.havesign.getResponse_code());
						lastMap.put("response_msg", ResponseEnum.havesign.getResponse_msg());
						return lastMap;
					}
					
					businessfee = ds.getBusinessfee();
					if(businessfee.doubleValue()>0&&businessfee.doubleValue()!=Double.valueOf(map.get("order_payable_amt"))){
						lastMap.put("response_code",ResponseEnum.YingShouJinEYiChang.getResponse_code());
						lastMap.put("response_msg", ResponseEnum.YingShouJinEYiChang.getResponse_msg());
						return lastMap;
					}
					
					String paytype=map.get("pay_type");
					if("02".equals(paytype)){//刷卡
						pos=ds.getBusinessfee();
					}else{
						cash=ds.getBusinessfee();
					}
					
					if(cwbOrder.getCwbordertypeid()==CwbOrderTypeIdEnum.Shangmentui.getValue()){
						paybackedfee=ds.getBusinessfee();
					}
					
				
					parameters.put("deliverid", deliveryid);
					parameters.put("podresultid", getPodResultIdByCwb(cwbOrder.getCwbordertypeid()));
					parameters.put("backreasonid", (long) 0);
					parameters.put("leavedreasonid", (long) 0);
					parameters.put("receivedfeecash", cash);
					parameters.put("receivedfeepos", pos);
					parameters.put("receivedfeecheque", (long)0);
					parameters.put("receivedfeeother", BigDecimal.ZERO);
					parameters.put("paybackedfee", paybackedfee);
					parameters.put("podremarkid", (long) 0);
					parameters.put("posremark", ds.getPosremark() + "POS反馈");
					parameters.put("checkremark", "");
					parameters.put("deliverstateremark", "POS签收成功");
					parameters.put("owgid", 0);
					parameters.put("sessionbranchid", user.getBranchid());
					parameters.put("sessionuserid", user.getUserid());
					parameters.put("sign_typeid", 1); // 是否 已签收 （0，未签收，1已签收）
					parameters.put("sign_man", map.get("signee"));//签收人
					parameters.put("sign_time", DateTimeUtil.getNowTime());
					parameters.put("nosysyemflag", "1");//
					cwbOrderService.deliverStatePod(getUser(user.getUserid()), cwbTransCwb,cwbORtranscwb, parameters);
					deliveryStateDAO.updateOperatorIdByCwb(user.getUserid(), cwbORtranscwb);
					
					//将签收信息存入签收记录表中
					String signtype = map.get("consignee_sign_flag").equals("Y")?"本人签收":"他人签收";
					int signtypeint = 1;
					if("他人签收".equals(signtype)){
						signtypeint = 2;
					}
					String sign_remark = "bjUnion运单签收，单号" + cwbORtranscwb + ",签收类型：" + signtype + ",小件员:" + user.getRealname();
					this.posPayDAO.save_PosTradeDetailRecord(cwbORtranscwb, "", new BigDecimal(map.get("order_payable_amt")).doubleValue(), 0, 0, "",
							cwbOrder.getConsigneename(), signtypeint,sign_remark, 2, 1, "", PosEnum.BJUnion.getMethod(), 0, "");
					this.logger.info(sign_remark);
					lastMap.put("response_code",ResponseEnum.success.getResponse_code());
					lastMap.put("response_msg", ResponseEnum.success.getResponse_msg());
					return lastMap;
				}
			}
		}catch(CwbException e1){
			this.exceptionCwbDAO.createExceptionCwbScan(cwbORtranscwb, e1.getFlowordertye(), e1.getMessage(), user.getBranchid(), user.getUserid(),
			cwbOrder == null ? 0 : cwbOrder.getCustomerid(), 0, 0, 0, "",cwbORtranscwb);
			if (e1.getError().getValue() == ExceptionCwbErrorTypeEnum.YI_CHANG_DAN_HAO.getValue()) {
				logger.error("tlmpos运单签收,没有检索到数据" + cwbORtranscwb + ",小件员:" + user.getRealname(), e1);
				lastMap.put("response_code",ResponseEnum.ChaXunYiChang.getResponse_code());
				lastMap.put("response_msg", ResponseEnum.ChaXunYiChang.getResponse_msg());
			} else if (e1.getError().getValue() == ExceptionCwbErrorTypeEnum.BU_SHI_ZHE_GE_XIAO_JIAN_YUAN_DE_HUO.getValue()) {
				lastMap.put("response_code",ResponseEnum.ChaXunYiChang.getResponse_code());
				lastMap.put("response_msg", ResponseEnum.ChaXunYiChang.getResponse_msg());
				logger.error("tlmpos运单签收,不是此小件员的货" + cwbORtranscwb + ",当前小件员:" + user.getRealname() + e1);
			}
		}
		return new HashMap<String, String>();
	}
	
	private User getUser(long userid) {
		return this.userDAO.getUserByUserid(userid);
	}

	private User getUserName(CwbOrder cwbOrder) {
		User u=null;
		if(cwbOrder!=null){
			long deliveryId = cwbOrder.getDeliverid();
			if(deliveryId!=0){
				u= this.userDAO.getAllUserByid(deliveryId);
			}
		}
		return u;
	}
	
	public long getPodResultIdByCwb(int cwbordertypeid) {
		if (cwbordertypeid == CwbOrderTypeIdEnum.Shangmenhuan.getValue()) {
			return DeliveryStateEnum.ShangMenHuanChengGong.getValue();
		} else if (cwbordertypeid == (CwbOrderTypeIdEnum.Shangmentui.getValue())) {
			return DeliveryStateEnum.ShangMenTuiChengGong.getValue();
		} else {
			return DeliveryStateEnum.PeiSongChengGong.getValue();
		}
	}
	
	//校验撤销签收
	public Map<String, String> dealRevokeReq(Map<String, String> map){
		Map<String, String> lastMap = new HashMap<String, String>();
		String cwbORtranscwb = map.get("order_no");
		String cwbTransCwb = this.cwbOrderService.translateCwb(cwbORtranscwb); // 可能是订单号也可能是运单号
		CwbOrder cwbOrder = this.cwbDao.getCwbByCwb(cwbTransCwb);
		User user = null;
		DeliveryState ds = null;
		long deliveryid = 0;
		try{
			if(cwbOrder==null){
				lastMap.put("response_code",ResponseEnum.ChaXunYiChang.getResponse_code());
				lastMap.put("response_msg", ResponseEnum.ChaXunYiChang.getResponse_msg());
				return lastMap;
			}
			user = getUserName(cwbOrder);
			if(user!=null){
				deliveryid = user.getUserid();
				ds = this.deliveryStateDAO.getActiveDeliveryStateByCwb(cwbOrder.getCwb());
				if(ds != null){
					if (ds.getReceivedfee().doubleValue() != Double.valueOf(map.get("amt"))) {
						lastMap.put("response_code",ResponseEnum.YingShouJinEYiChang.getResponse_code());
						lastMap.put("response_msg", ResponseEnum.YingShouJinEYiChang.getResponse_msg());
						logger.error("buUnion运单撤销,撤销金额与原金额不一致,单号:" + cwbORtranscwb + ",小件员:" + user.getRealname());
						return lastMap;
					}
					if (ds.getReceivedfee().doubleValue()>0&&ds.getCash().doubleValue()>0) {
						lastMap.put("response_code",ResponseEnum.BuNengCheXiao.getResponse_code());
						lastMap.put("response_msg", ResponseEnum.BuNengCheXiao.getResponse_msg());
						logger.error("buUnion运单撤销,非POS刷卡支付不能撤销,单号:" + cwbORtranscwb + ",小件员:" + user.getRealname());
						return lastMap;
					}
					//验证通过=======执行撤销方法
					lastMap = revokeCwb(ds,map,cwbORtranscwb,user);
					return lastMap;
				}
			}
		}catch(Exception e){
			lastMap.put("response_code",ResponseEnum.QiTaShiBai.getResponse_code());
			lastMap.put("response_msg", e.getMessage());
			return lastMap;
		}
		return new HashMap<String, String>();
	}
	//执行撤销方法
	private Map<String, String> revokeCwb(DeliveryState ds,Map<String, String> map,String cwbORtranscwb,User user){
		Map<String, String> lastMap = new HashMap<String, String>();

		String deliverstateremark = "撤销交易";
		double amount_after = 0;
		try {
			amount_after = ds.getReceivedfee().doubleValue() - new BigDecimal(map.get("amt")).doubleValue();
			// 执行公共撤销方法
			cwbOrderService.deliverStatePodCancel(cwbORtranscwb, user.getBranchid(), user.getUserid(), deliverstateremark,amount_after);

			posPayDAO.save_PosTradeDetailRecord(cwbORtranscwb, deliverstateremark, 0,user.getUserid(), 1, deliverstateremark, "", 0, "", 1, 4,
					"0", PosEnum.BJUnion.getMethod(), 0, "");
			lastMap.put("response_code",ResponseEnum.success.getResponse_code());
			lastMap.put("response_msg", ResponseEnum.success.getResponse_code());
		} catch (Exception e) {
			logger.error("bjUnion撤销更新数据库异常！小件员:" + user.getRealname() + ",订单号:" + cwbORtranscwb, e);
			lastMap.put("response_code",ResponseEnum.QiTaShiBai.getResponse_code());
			lastMap.put("response_msg", e.getMessage());
		}
		return lastMap;
	}
}
