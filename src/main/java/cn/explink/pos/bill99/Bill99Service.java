package cn.explink.pos.bill99;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.poscodeMapp.PoscodeMappDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.ExceptionCwbDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.UserDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.User;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.ReasonTypeEnum;
import cn.explink.pos.bill99.xml.Bill99Unmarchal;
import cn.explink.pos.bill99.xml.Transaction;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.pos.tools.PosEnum;
import cn.explink.pos.tools.PosPayDAO;
import cn.explink.pos.tools.PosPayService;
import cn.explink.service.CwbOrderService;
import cn.explink.util.StringUtil;
import cn.explink.util.pos.CertificateCoderUtil;

@Service
public class Bill99Service {
	private Logger logger = LoggerFactory.getLogger(Bill99Service.class);
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	ReasonDao reasonDao;
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	PosPayDAO posPayDAO;
	@Autowired
	PosPayService posPayService;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	Bill99ServiceMaster bill99ServiceMaster;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	ExceptionCwbDAO exceptionCwbDAO;
	@Autowired
	PoscodeMappDAO poscodeMappDAO;

	protected ObjectMapper jacksonmapper = JacksonMapper.getInstance();

	public String getObjectMethod(int key) {
		JointEntity obj = null;
		String posValue = "";
		try {
			obj = jiontDAO.getJointEntity(key);
			posValue = obj.getJoint_property();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return posValue;
	}

	public Bill99 getBill99SettingMethod(int key) {
		Bill99 bill99 = null;
		if (!"".equals(getObjectMethod(key))) {
			JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
			bill99 = (Bill99) JSONObject.toBean(jsonObj, Bill99.class);
		} else {
			bill99 = new Bill99();
		}

		return bill99 == null ? new Bill99() : bill99;
	}

	public void edit(HttpServletRequest request, int joint_num) {
		Bill99 bill99 = new Bill99();
		String version = StringUtil.nullConvertToEmptyString(request.getParameter("version"));
		String requester = StringUtil.nullConvertToEmptyString(request.getParameter("requester"));
		String targeter = StringUtil.nullConvertToEmptyString(request.getParameter("targeter"));
		String ipodalias = StringUtil.nullConvertToEmptyString(request.getParameter("ipodalias"));
		String ipodpassword = StringUtil.nullConvertToEmptyString(request.getParameter("ipodpassword"));
		String ipodrequestFileName = StringUtil.nullConvertToEmptyString(request.getParameter("ipodrequestFileName"));
		String ipodresponseFileName = StringUtil.nullConvertToEmptyString(request.getParameter("ipodresponseFileName"));
		String isotheroperator = StringUtil.nullConvertToEmptyString(request.getParameter("isotheroperator"));// 是否限制他人刷卡
																												// 1限制(只能刷分配自己名下)，
																												// 2不限制（任何人都可刷）

		String isupdateDeliverid = StringUtil.nullConvertToEmptyString(request.getParameter("isupdateDeliverid"));// 他人刷卡是否修改派送员为POS机登录派送员1是，0否
		String request_url = StringUtil.nullConvertToEmptyString(request.getParameter("request_url"));
		String isopensignflag = StringUtil.nullConvertToEmptyString(request.getParameter("isopensignflag"));
		
		String resultCustomerid = StringUtil.nullConvertToEmptyString(request.getParameter("resultCustomerid"));

		bill99.setVersion(version);
		bill99.setRequester(requester);
		bill99.setTargeter(targeter);
		bill99.setIpodalias(ipodalias);
		bill99.setIpodpassword(ipodpassword);
		bill99.setIpodrequestFileName(ipodrequestFileName);
		bill99.setIpodresponseFileName(ipodresponseFileName);
		bill99.setIsotheroperator(Integer.valueOf(isotheroperator));
		bill99.setIsupdateDeliverid(Integer.valueOf(isupdateDeliverid));
		bill99.setRequest_url(request_url);
		bill99.setIsopensignflag(Integer.valueOf(isopensignflag));
		bill99.setResultCustomerid(resultCustomerid);

		JSONObject jsonObj = JSONObject.fromObject(bill99);
		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Update(jointEntity);
		}
	}

	public void update(int joint_num, int state) {
		jiontDAO.UpdateState(joint_num, state);
	}

	/**
	 * 处理bill99的请求
	 * 
	 * @return code
	 * @throws UnsupportedEncodingException
	 */

	public String AnalyzXMLByBill99(String xmlstr, String returnCode, Bill99 bill99) {
		Transaction rootnote;
		try {
			rootnote = Bill99Unmarchal.Unmarchal(xmlstr);
			String target = rootnote.getTransaction_Header().getTarget();
			String target_set = bill99.getTargeter();
			if (!target.equals(target_set)) {
				logger.error("bill99应答方配置信息有误!");
				return "应答方配置信息有误";
			}
			if (bill99.getIsopensignflag() == 1) {
				if (!ValidateMAC_publicMethod(rootnote, bill99, xmlstr)) {
					logger.info("Bill99签名验证失败!" + "billl99请求接口,请求XML:" + xmlstr);
					return "<resp_code>01</response_code><resp_msg>请求签名验证失败</response_msg>";
				}
			}

			return DealWithBill99Interface(returnCode, bill99, xmlstr, rootnote);
		} catch (Exception e) {
			logger.error("", e);
			return "Convert XML to Transaction Bean Exception! reason=" + e;

		}

	}

	/**
	 * bill99的接口
	 * 
	 * @param returnCode
	 * @param bill99
	 * @param xmlstr
	 * @param jobject
	 * @return
	 */
	private String DealWithBill99Interface(String returnCode, Bill99 bill99, String xmlstr, Transaction rootnote) {
		String transaction_id = rootnote.getTransaction_Header().getTransaction_id();
		logger.info("获取bill99的业务编码[" + transaction_id + "];请求XML:" + xmlstr);
		if ("MI0001".equals(transaction_id)) { // 派送员登陆
			returnCode = bill99ServiceMaster.getBill99Service_toLogin().tologin(rootnote, bill99);
		} else if ("MI0005".equals(transaction_id)) { // 派送运单支付反馈
			returnCode = bill99ServiceMaster.getBill99Service_toPayAmount().toPayAmountForPos(rootnote, bill99);
		} else if ("MI0006".equals(transaction_id)) { // 派件签收结果反馈
			returnCode = bill99ServiceMaster.getBill99Service_toCwbSign().toCwbSign(rootnote, bill99);
		} else if ("MI0007".equals(transaction_id)) { // 撤销交易结果反馈
			returnCode = bill99ServiceMaster.getBill99Service_toBackOut().toBackOut(rootnote, bill99);
		} else if ("MI0008".equals(transaction_id)) { // 派件异常反馈
			returnCode = bill99ServiceMaster.getBill99Service_toExptFeedBack().toExceptionFeedBack(rootnote, bill99);
		} else if ("MI0010".equals(transaction_id)) { // 运单查询
			returnCode = bill99ServiceMaster.getBill99Service_toCwbSearch().toCwbSearch(rootnote, bill99);
		} else if ("MI0011".equals(transaction_id)) { // 交易冲正
			returnCode = bill99ServiceMaster.getBill99Service_toReverseDept().toReverseDept(rootnote, bill99);
		}
		return returnCode;
	}

	private boolean ValidateMAC_publicMethod(Transaction rootnote, Bill99 bill99, String xmlDOC) {
		String transaction_id = rootnote.getTransaction_Header().getTransaction_id();
		String xmltrimStr = xmlDOC;
		String xmlSub = xmltrimStr.substring(xmltrimStr.indexOf("<Transaction_Body>"), xmltrimStr.lastIndexOf("</Transaction_Body>") + 19);
		CertificateCoderUtil cfcu = new CertificateCoderUtil(bill99);
		// 验证签名
		boolean checkMACflag = false;
		String MAC = rootnote.getTransaction_Header().getMAC();
		try {
			checkMACflag = cfcu.verify(xmlSub.getBytes("utf-8"), MAC);
			logger.info(transaction_id + "MAC：" + MAC + ";签名验证的内容:" + xmlSub + ";验证结果：[" + (checkMACflag) + "]");
		} catch (Exception e) {
			logger.error("bill99签名验证异常!业务编码:" + transaction_id, e);
		}
		return checkMACflag;
	}

	public long getDeliveryStateByReasonType(int code) {
		long deliverystate = -1;
		long reasontype = reasonDao.getReasonTypeByExptCode(code + "");
		if (reasontype == ReasonTypeEnum.BeHelpUp.getValue()) {
			deliverystate = DeliveryStateEnum.FenZhanZhiLiu.getValue();
		} else if (reasontype == ReasonTypeEnum.ReturnGoods.getValue()) {
			deliverystate = DeliveryStateEnum.JuShou.getValue();
		}
		return deliverystate;
	}

	protected long getUserIdByUserName(String deliver_man) {
		long deliverid = 0;
		try {
			List<User> userlist = userDAO.getUsersByUsername(deliver_man);
			if (userlist != null && userlist.size() > 0) {
				deliverid = userlist.get(0).getUserid();
			}
		} catch (Exception e) {
			logger.error("POS查询-没有查询到小件员[" + deliver_man + "]", e);
		}
		return deliverid;
	}

	protected User getUser(long userid) {
		return userDAO.getUserByUserid(userid);
	}

	public long getPodResultIdByCwb(int cwbordertypeid) {
		if (cwbordertypeid == (CwbOrderTypeIdEnum.Shangmenhuan.getValue())) {
			return DeliveryStateEnum.ShangMenHuanChengGong.getValue();
		} else if (cwbordertypeid == (CwbOrderTypeIdEnum.Shangmentui.getValue())) {
			return DeliveryStateEnum.ShangMenTuiChengGong.getValue();
		} else {
			return DeliveryStateEnum.PeiSongChengGong.getValue();
		}
	}

	/**
	 * 返回签名的公共方法
	 * 
	 * @param cfcu
	 * @param str
	 * @return
	 */
	public String Bill99RespSign(CertificateCoderUtil cfcu, String str) {
		String MAC = "";
		try {
			MAC = cfcu.sign(str.getBytes("utf-8"));
		} catch (Exception e) {
			logger.error("移动POS(bill99):运单支付生成MAC异常!", e);
		}
		return MAC;
	}

	/**
	 * 为Bill99RespNote封装 公用对象
	 * 
	 * @param rootnote
	 * @param alipayRespNote
	 * @return
	 */
	protected Bill99RespNote BuildBill99RespClass(Transaction rootnote, Bill99RespNote respNote) {
		respNote.setDeliverid(getUserIdByUserName(rootnote.getTransaction_Header().getExt_attributes().getDelivery_man()));
		String orderId = rootnote.getTransaction_Body().getOrderId();
		String order_no = rootnote.getTransaction_Body().getOrder_no();
		String cwb = null;

		if (orderId != null && !"".equals(orderId)) {
			cwb = orderId;
		} else {
			cwb = order_no;
		}

		String cwbTransCwb = cwbOrderService.translateCwb(cwb); // 可能是订单号也可能是运单号

		long deliverid = 0;
		long branchid = 0;
		Bill99 bill99 = getBill99SettingMethod(PosEnum.Bill99.getKey());
		if (bill99.getIsotheroperator() == 1) { // 限制他人刷卡，只能自己刷自己名下订单
			deliverid = respNote.getDeliverid();
			// 获取当前站点id，默认当前登录人所在的id，如果限制刷卡则 branchid为登录人，反之则为领货人所在id
			branchid = userDAO.getUserByUsername(rootnote.getTransaction_Header().getExt_attributes().getDelivery_man()).getBranchid();
		}

		CwbOrder co = cwbDAO.getCwbDetailByCwbAndDeliverId(deliverid, cwbTransCwb);
		respNote.setCwbOrder(cwbDAO.getCwbDetailByCwbAndDeliverId(deliverid, cwbTransCwb));
		
		boolean selectPower = validatorSelectPower(bill99, co);
		
		if(selectPower){
			respNote.setCwbOrder(null);
		}
		
		DeliveryState ds = deliveryStateDAO.getDeliveryStateByCwb_posHelper(cwbTransCwb, respNote.getDeliverid()); // 如果根据订单号可以查到对象，则返回，如果查询不到，则调用receiveGoods创建。

		branchid = branchid == 0 ? ds.getDeliverybranchid() : branchid;

		respNote.setBranchid(branchid);

		respNote.setDeliverstate(ds);
		respNote.setOrder_no(cwbTransCwb);
		respNote.setTransaction_id(rootnote.getTransaction_Header().getTransaction_id());
		respNote.setDelivery_man(rootnote.getTransaction_Header().getExt_attributes().getDelivery_man());
		return respNote;
	}

	//是否验证权限允许查询访问
	private boolean validatorSelectPower(Bill99 bill99, CwbOrder co) {
		String customeridArrs[]=bill99.getResultCustomerid().split(",");
		if(customeridArrs!=null&&customeridArrs.length>0){
			for(String customerid:customeridArrs){
				if(co.getCustomerid()==Long.valueOf(customerid)){
					return true;
				}
			}
		}
		return false;
	}

}
