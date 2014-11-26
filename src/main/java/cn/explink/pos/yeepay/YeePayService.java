package cn.explink.pos.yeepay;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.ExceptionCwbDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.User;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.pos.tools.PosEnum;
import cn.explink.pos.tools.PosPayDAO;
import cn.explink.pos.tools.PosPayService;
import cn.explink.pos.yeepay.xml.YeePayUnmarchal;
import cn.explink.pos.yeepay.xml.YeepayRequest;
import cn.explink.service.CwbOrderService;
import cn.explink.util.StringUtil;

@Service
public class YeePayService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	YeePayServiceMaster yeePayServiceMaster;

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	ExceptionCwbDAO exceptionCwbDAO;

	@Autowired
	PosPayService posPayService;
	@Autowired
	PosPayDAO posPayDAO;
	private Md5PasswordEncoder md5 = new Md5PasswordEncoder();

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

	public YeePay getYeePaySettingMethod(int key) {
		YeePay YeePay = null;
		if (!"".equals(getObjectMethod(key))) {
			JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
			YeePay = (YeePay) JSONObject.toBean(jsonObj, YeePay.class);
		} else {
			YeePay = new YeePay();
		}

		return YeePay == null ? new YeePay() : YeePay;
	}

	public void edit(HttpServletRequest request, int joint_num) {
		YeePay YeePay = new YeePay();
		String requester = StringUtil.nullConvertToEmptyString(request.getParameter("requester"));
		String targeter = StringUtil.nullConvertToEmptyString(request.getParameter("targeter"));
		String privatekey = StringUtil.nullConvertToEmptyString(request.getParameter("privatekey"));
		String isotheroperator = StringUtil.nullConvertToEmptyString(request.getParameter("isotheroperator"));
		YeePay.setRequester(requester);
		YeePay.setTargeter(targeter);
		YeePay.setPrivatekey(privatekey);
		YeePay.setIsotheroperator(Integer.valueOf(isotheroperator));

		JSONObject jsonObj = JSONObject.fromObject(YeePay);
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
	 * 处理YeePay的请求
	 * 
	 * @return code
	 * @throws UnsupportedEncodingException
	 */
	public String AnalyzXMLByYeePay(String xmlstr, YeePay yeePay) {
		if (xmlstr == null || "".equals(xmlstr)) {
			logger.error("yeepay传输信息解析XML为空!");
			return "解析XML为空!";
		}

		try {
			YeepayRequest rootnote = YeePayUnmarchal.Unmarchal(xmlstr);

			String SrcSysID = rootnote.getSessionHead().getSrcSysID();
			String DstSysID = rootnote.getSessionHead().getDstSysID();
			String requester = yeePay.getRequester();
			String targeter = yeePay.getTargeter();
			if (!SrcSysID.equals(requester)) {
				logger.error("yeepay请求方配置信息有误!");
				return "传输信息[请求方]有误!";
			}
			// if(!DstSysID.equals(targeter)){
			// logger.error("yeepay应答方配置信息有误!");
			// return "传输信息[应答方]有误!";
			// }
			if (!ValidateMAC_publicMethod(rootnote, yeePay, xmlstr)) {
				return "签名验证失败";
			}
			return DealWithYeePayInterface(yeePay, rootnote, xmlstr);

		} catch (Exception e) {
			logger.error("Convert XML to YeePayRequest Bean Exception,XML=" + xmlstr, e);
			return "Convert XML to YeePayRequest Bean Exception! reason=" + e.getMessage();
		}

	}

	private String DealWithYeePayInterface(YeePay yeePay, YeepayRequest rootnote, String xmlstr) {
		String servicecode = rootnote.getSessionHead().getServiceCode();
		logger.info("获取YeePay的业务编码{},请求报文信息:{}", servicecode, xmlstr);
		if ("COD201".equals(servicecode)) {// 派送员登陆
			return yeePayServiceMaster.getYeePayService_toLogin().toLogin(servicecode, rootnote, yeePay);
		}
		if ("COD202".equals(servicecode)) { // 运单查询
			return yeePayServiceMaster.getYeePayService_toCwbSearch().toCwbSearch(servicecode, rootnote, yeePay);
		}
		if ("COD204".equals(servicecode)) {// 付款通知接口
			return yeePayServiceMaster.getYeePayService_toPayAmount().toPayAmountForPos(servicecode, rootnote, yeePay);
		}
		if ("COD203".equals(servicecode)) {// 签收通知接口
			return yeePayServiceMaster.getYeePayService_toSignCwb().toSignCwb(servicecode, rootnote, yeePay);
		}
		if ("COD206".equals(servicecode)) { // 异常派单接口
			return yeePayServiceMaster.getYeePayService_toExptFeedBack().toExceptionFeedBack(servicecode, rootnote, yeePay);
		}
		if ("COD205".equals(servicecode)) { // 撤销交易通知接口
			return yeePayServiceMaster.getYeePayService_toBackOut().toBackOut(servicecode, rootnote, yeePay);
		}

		return "无此接口" + servicecode;
	}

	/**
	 * yeepay签名验证的公共方法
	 * 
	 * @param jobject
	 * @param yeePay
	 * @param xmlDOC
	 * @return
	 */
	private boolean ValidateMAC_publicMethod(YeepayRequest rootnote, YeePay yeePay, String xmlDOC) {
		String servicecode = rootnote.getSessionHead().getServiceCode();
		String xmltrimStr = xmlDOC.replaceAll(" ", "");
		String xmltrim = xmltrimStr.substring(xmltrimStr.indexOf("<SessionHead>"));
		String xmlsB = xmltrim.substring(0, xmltrim.indexOf("<HMAC>"));
		String xmlsE = xmltrim.substring(xmltrim.indexOf("</HMAC>") + ("</HMAC>".length()), xmltrim.indexOf("</COD-MS>"));
		String checkHMAC = xmlsB + xmlsE;
		Md5PasswordEncoder md5 = new Md5PasswordEncoder();
		md5.setEncodeHashAsBase64(false);
		// 获取HMAC
		String HMAC = rootnote.getSessionHead().gethMAC();
		// 对数据加密 (MD5)
		String md5token = md5.encodePassword(checkHMAC + yeePay.getPrivatekey(), null);
		if (!HMAC.equals(md5token)) {
			logger.error("yeepay接口" + servicecode + "签名验证失败!签名验证的内容:" + checkHMAC);
			return false;
		} else {
			logger.info("yeepay接口{}签名验证成功!", servicecode);
			return true;
		}
	}

	protected int getYeePayExptOrderType(String expt_code) {
		int ordertype = 0;
		for (YeePayExptEnum f : YeePayExptEnum.values()) {
			if (expt_code.equals(f.getExpt_code())) {
				ordertype = f.getExpt_orderType();
				break;
			}
		}
		return ordertype;
	}

	protected String getYeePayExptOrderTypeName(String expt_code) {
		String ordertypename = "";
		for (YeePayExptEnum f : YeePayExptEnum.values()) {
			if (expt_code.equals(f.getExpt_code())) {
				ordertypename = f.getExpt_msg();
				break;
			}
		}
		return ordertypename;
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

	protected long getUserIdByUserName(String deliver_man) {
		long deliverid = 0;
		try {
			List<User> userlist = userDAO.getUsersByUsername(deliver_man);
			if (userlist != null && userlist.size() > 0) {
				deliverid = userlist.get(0).getUserid();
			}
		} catch (Exception e) {

		}
		return deliverid;
	}

	/**
	 * 为YeePayRespNote封装 公用对象
	 * 
	 * @param rootnote
	 * @param YeePayRespNote
	 * @return
	 */
	protected YeePayRespNote BuildYeePayRespClass(YeepayRequest rootnote, YeePayRespNote RespNote) {
		String username = rootnote.getSessionBody().getEmployee_ID();
		String username1 = rootnote.getSessionHead().getExtendAtt() != null ? rootnote.getSessionHead().getExtendAtt().getEmployee_ID() : null;
		String username2 = rootnote.getSessionBody().getExtendAtt() != null ? rootnote.getSessionBody().getExtendAtt().getEmployee_ID() : null;
		String deliverUserName = "";
		if (username1 != null) {
			deliverUserName = username1;
		} else if (username != null) {
			deliverUserName = username;
		} else {
			deliverUserName = username2;
		}

		User user = userDAO.getUserByUsername(deliverUserName);
		RespNote.setUser(user);
		RespNote.setDeliverid(user.getUserid());
		RespNote.setUsername(user.getUsername());
		String order_no = rootnote.getSessionBody().getOrder_No();
		String order_no1 = rootnote.getSessionBody().getExtendAtt() != null ? rootnote.getSessionBody().getExtendAtt().getOrderNo() : null;
		String order_no2 = rootnote.getSessionBody().getExtendAtt() != null ? rootnote.getSessionBody().getExtendAtt().getOrder_No() : null;
		String cwb = order_no;
		if (order_no1 != null) {
			cwb = order_no1;
		} else if (order_no2 != null) {
			cwb = order_no2;
		}

		long deliverid = 0;
		YeePay yeePay = getYeePaySettingMethod(PosEnum.YeePay.getKey());
		if (yeePay.getIsotheroperator() == 1) { // 限制他人刷卡，只能自己刷自己名下订单
			deliverid = RespNote.getDeliverid();
		}

		RespNote.setCwbOrder(cwbDAO.getCwbDetailByCwbAndDeliverId(deliverid, cwb));
		DeliveryState ds = deliveryStateDAO.getDeliveryStateByCwb_posHelper(cwb, RespNote.getDeliverid()); // 如果根据订单号可以查到对象，则返回，如果查询不到，则调用receiveGoods创建。
		RespNote.setBranchid(user.getBranchid());
		RespNote.setDeliverstate(ds);
		RespNote.setOrder_No(cwb);
		RespNote.setServiceCode(rootnote.getSessionHead().getServiceCode()); // 请求业务编码区分
		RespNote.setEmployee_ID(rootnote.getSessionBody().getEmployee_ID());

		return RespNote;
	}

}
