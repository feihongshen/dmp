package cn.explink.pos.alipay;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.dangdang.DangDang;
import cn.explink.b2c.tools.ExptCodeJointDAO;
import cn.explink.b2c.tools.ExptReasonDAO;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.poscodeMapp.PoscodeMappDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.ExceptionCwbDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.UserDAO;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.User;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.pos.alipay.xml.AlipayUnmarchal;
import cn.explink.pos.alipay.xml.Transaction;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.pos.tools.PosEnum;
import cn.explink.pos.tools.PosPayDAO;
import cn.explink.pos.tools.PosPayService;
import cn.explink.service.CwbOrderService;
import cn.explink.util.StringUtil;
import cn.explink.util.pos.RSACoder;

@Service
public class AlipayService {
	private Logger logger = LoggerFactory.getLogger(AlipayService.class);
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	PosPayDAO posPayDAO;
	@Autowired
	PosPayService posPayService;
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	ReasonDao reasonDao;
	@Autowired
	ExptReasonDAO exptReasonDAO;
	@Autowired
	ExptCodeJointDAO exptcodeJointDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	AlipayServiceMaster alipayServiceMaster;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	ExceptionCwbDAO exceptionCwbDAO;

	@Autowired
	PoscodeMappDAO poscodeMappDAO;

	protected ObjectMapper jacksonmapper = JacksonMapper.getInstance();

	private String getObjectMethod(int key) {
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

	public AliPay getAlipaySettingMethod(int key) {
		AliPay alipay = new AliPay();
		if (!"".equals(getObjectMethod(key))) {
			JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
			alipay = (AliPay) JSONObject.toBean(jsonObj, AliPay.class);
		} else {
			alipay = new AliPay();
		}

		return alipay == null ? new AliPay() : alipay;
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

	public void edit(HttpServletRequest request, int joint_num) {
		AliPay alipay = new AliPay();
		String requester = StringUtil.nullConvertToEmptyString(request.getParameter("requester"));
		String targeter = StringUtil.nullConvertToEmptyString(request.getParameter("targeter"));
		String privateKey = StringUtil.nullConvertToEmptyString(request.getParameter("privateKey"));
		String publicKey = StringUtil.nullConvertToEmptyString(request.getParameter("publicKey"));

		String isotherdeliverupdate = StringUtil.nullConvertToEmptyString(request.getParameter("isotherdeliverupdate"));// 刷卡是否更新派送员
		String isotheroperator = StringUtil.nullConvertToEmptyString(request.getParameter("isotheroperator"));// 刷卡是否更新派送员

		String request_url = StringUtil.nullConvertToEmptyString(request.getParameter("request_url"));

		alipay.setRequester(requester);
		alipay.setTargeter(targeter);
		alipay.setPublicKey(publicKey);
		alipay.setPrivateKey(privateKey);
		alipay.setIsotherdeliverupdate(Integer.parseInt(isotherdeliverupdate));
		alipay.setIsotheroperator(Integer.valueOf(isotheroperator));
		alipay.setRequest_url(request_url);

		JSONObject jsonObj = JSONObject.fromObject(alipay);
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

	protected User getUser(long userid) {
		return userDAO.getUserByUserid(userid);
	}

	/**
	 * 处理alipay的请求
	 * 
	 * @return code
	 * @throws UnsupportedEncodingException
	 */
	public String AnalyzXMLByAlipay(AliPay alipay, String xmlstr) {
		if (xmlstr != null && !"".equals(xmlstr)) {
			try {

				Transaction rootnote = AlipayUnmarchal.Unmarchal(xmlstr);
				String requester = rootnote.getTransaction_Header().getRequester();
				String target = rootnote.getTransaction_Header().getTarget();
				String requester_set = alipay.getRequester();
				String target_set = alipay.getTargeter();

				if (!requester.equals(requester_set) || !target.equals(target_set)) {
					logger.error("alipay请求方或应答方配置信息有误!");
					return "请求方或应答方配置信息有误";
				}
				if (!ValidateMAC_publicMethod(rootnote, alipay, xmlstr)) {
					logger.error("alipay请求签名验证失败!xml=" + xmlstr);
					// return "签名验证失败";
				}
				return DealWithAlipayInterface(alipay, xmlstr, rootnote);
			} catch (Exception e) {
				e.printStackTrace();
				return "Convert XML to Transaction Bean Exception! reason=" + e;
			}

		} else {
			return "AliPay request XML is null.";
		}

	}

	/**
	 * alipay请求的接口 判断
	 * 
	 * @param alipay
	 * @param xmlstr
	 * @param rootnote
	 * @return
	 */
	private String DealWithAlipayInterface(AliPay alipay, String xmlstr, Transaction rootnote) {
		String transaction_id = "";
		try {
			transaction_id = rootnote.getTransaction_Header().getTransaction_id();
			logger.info("获取alipay的业务编码[" + transaction_id + "];请求XML:" + xmlstr);
			if ("MI0001".equals(transaction_id)) {// 派送员登陆
				return alipayServiceMaster.getAlipayService_toLogin().tologin(rootnote, alipay);
			}
			if ("MI0005".equals(transaction_id)) { // 派送运单支付反馈
				return alipayServiceMaster.getAlipayService_toPayAmount().toPayAmountForPos(rootnote, alipay);
			}
			if ("MI0006".equals(transaction_id)) {// 派件签收结果反馈
				return alipayServiceMaster.getAlipayService_toCwbSign().toCwbSign(rootnote, alipay);
			}
			if ("MI0007".equals(transaction_id)) {// 撤销交易结果反馈
				return alipayServiceMaster.getAlipayService_toBackOut().toBackOut(rootnote, alipay);
			}
			if ("MI0008".equals(transaction_id)) { // 派件异常反馈
				return alipayServiceMaster.getAlipayService_toExptFeedBack().toExceptionFeedBack(rootnote, alipay);
			}
			if ("MI0010".equals(transaction_id)) { // 运单查询
				return alipayServiceMaster.getAlipayService_toCwbSearch().toCwbSearch(rootnote, alipay);
			}
			if ("MI0015".equals(transaction_id)) { // 分单支付完成通知接口
				return alipayServiceMaster.getAlipayService_toPayFinish().toPayFinish(rootnote, alipay);
			}
			if ("MI0016".equals(transaction_id)) { // 分单撤销完成结果反馈
				return alipayServiceMaster.getAlipayService_toBackOutFinish().toBackOutFinish(rootnote, alipay);
			}
			if ("MI0021".equals(transaction_id)) { // 查询商户派件异常码
				return alipayServiceMaster.getAlipayService_toSearchExptCode().toSearchExptCode(rootnote, alipay);
			}
		} catch (Exception e) {
			logger.error("处理Alipay请求发生异常，异常原因：", e);
			e.printStackTrace();
			return "处理Alipay请求发生异常" + e.getMessage();
		}
		return "无此接口" + transaction_id;
	}

	public static void main(String[] args) {
		String checkMACdata = "<Transaction_Header><transaction_id>MI0006</transaction_id><requester>1111111111</requester><target>YTFH</target><request_time>20140227045756</request_time><version>1.0</version><ext_attributes><delivery_dept_no></delivery_dept_no><delivery_dept></delivery_dept><delivery_man>6008</delivery_man><delivery_name></delivery_name></ext_attributes></Transaction_Header><Transaction_Body><order_no>7524421842</order_no><signee>赵雅杰</signee><consignee_sign_flag>Y</consignee_sign_flag></Transaction_Body>";
		String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCyY4PQTkUpb+HKgJ/R4fFkCSK0 ZTNu7N5/t7jMo/oKzTatAXkkbsKhgeQTAWzzFi8XFp6om58w8mrY5XZCJWEFOiYf zQ8eAUjAPmIU8VQWWfRpiIHO7N9OsfY3ZSHNkkWHN/XPmCzTTNcFUq2PhUwzpZjM tVp3UXCZLKFY1VFB3QIDAQAB";
		String MAC = "cIOdPIkVKbz9mp0IuA2fuh8wkQUZvu0FG3J4aGMpiwr331iMIAyKzjZtdfOoaZqe9Pcp26R68BawQKNuJnLluZuD5Np7FjPgtEj0ajU5PS5KBoqXKGoe3MzUAGMC1M74ngPrUAuUwFYzzpumB3V7cIDMQ64DV14k9HPbfdSqAjs=";
		boolean checkMACflag = false;
		try {
			checkMACflag = RSACoder.verify(checkMACdata.getBytes(), publicKey, MAC);

		} catch (Exception e) {

			e.printStackTrace();
		}
		System.out.println(checkMACflag);

	}

	private boolean ValidateMAC_publicMethod(Transaction rootnote, AliPay alipay, String xmlDOC) {
		String transaction_id = rootnote.getTransaction_Header().getTransaction_id();
		String xmltrimStr = xmlDOC.replaceAll(" ", "");
		String xmltrim = xmltrimStr.substring(xmltrimStr.indexOf("<Transaction_Header>"));
		String xmlsB = xmltrim.substring(0, xmltrim.indexOf("<MAC>"));
		String xmlsE = xmltrim.substring(xmltrim.indexOf("</MAC>") + 6, xmltrim.indexOf("</Transaction>"));
		String checkMACdata = xmlsB + xmlsE;
		logger.info(transaction_id + "签名验证的内容:" + checkMACdata);
		// 验证签名
		boolean checkMACflag = false;
		try {
			checkMACflag = RSACoder.verify(checkMACdata.getBytes(), alipay.getPublicKey(), rootnote.getTransaction_Header().getMAC());
			logger.info("checkMACdata=" + checkMACdata + ",public_key=" + alipay.getPublicKey() + ",MAC=" + rootnote.getTransaction_Header().getMAC() + ",checkMACflag=" + checkMACflag);
		} catch (Exception e) {
			logger.error("alipay签名验证异常!业务编码");
			e.printStackTrace();
		}

		return checkMACflag;
	}

	/**
	 * 返回一个签名的结果,用于返回给alipay信息时候加密
	 * 
	 * @param alipay
	 * @param str
	 * @return
	 */
	protected String CreateRespSign(AliPay alipay, String str) {
		String MAC = "";
		try {
			MAC = RSACoder.sign(str.getBytes(), alipay.getPrivateKey());
		} catch (Exception e) {
			logger.error("移动POS(alipay):返回签名加密异常!", e);
			e.printStackTrace();
		}
		return MAC;
	}

	public void update(int joint_num, int state) {
		jiontDAO.UpdateState(joint_num, state);
	}

	public DangDang getDangDangSettingMethod(int key) {
		DangDang dangdang = new DangDang();
		if (!"".equals(getObjectMethod(key))) {
			JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
			dangdang = (DangDang) JSONObject.toBean(jsonObj, DangDang.class);
		} else {
			dangdang = null;
		}
		return dangdang == null ? new DangDang() : dangdang;
	}

	public long getPodResultIdByCwb(String cwbordertypeid) {
		if (cwbordertypeid.equals(CwbOrderTypeIdEnum.Shangmenhuan.getValue() + "")) {
			return DeliveryStateEnum.ShangMenHuanChengGong.getValue();
		} else if (cwbordertypeid.equals(CwbOrderTypeIdEnum.Shangmentui.getValue() + "")) {
			return DeliveryStateEnum.ShangMenTuiChengGong.getValue();
		} else {
			return DeliveryStateEnum.PeiSongChengGong.getValue();
		}
	}

	/**
	 * 为AlipayRespNote封装 公用对象
	 * 
	 * @param rootnote
	 * @param alipayRespNote
	 * @return
	 */
	protected AlipayRespNote BuildAlipayRespClass(Transaction rootnote, AlipayRespNote alipayRespNote) {
		alipayRespNote.setDeliverid(getUserIdByUserName(rootnote.getTransaction_Header().getExt_attributes().getDelivery_man()));
		String cwbTransCwb = cwbOrderService.translateCwb(rootnote.getTransaction_Body().getOrder_no()); // 可能是订单号也可能是运单号

		long deliverid = 0;
		AliPay alipay = getAlipaySettingMethod(PosEnum.AliPay.getKey());
		if (alipay.getIsotheroperator() == 1) { // 限制他人刷卡，只能自己刷自己名下订单
			deliverid = alipayRespNote.getDeliverid() == 0 ? -1 : alipayRespNote.getDeliverid();
		}
		alipayRespNote.setCwbOrder(cwbDAO.getCwbDetailByCwbAndDeliverId(deliverid, cwbTransCwb));

		alipayRespNote.setBranchid(userDAO.getUserByUsername(rootnote.getTransaction_Header().getExt_attributes().getDelivery_man()).getBranchid());
		// DeliveryState ds =
		// deliveryStateDAO.getActiveDeliveryStateByCwb(cwb111);
		// //支付时已经更新各种款项备注了，签收时需要查询出来
		DeliveryState ds = deliveryStateDAO.getDeliveryStateByCwb_posHelper(cwbTransCwb, alipayRespNote.getDeliverid()); // 如果根据订单号可以查到对象，则返回，如果查询不到，则调用receiveGoods创建。

		alipayRespNote.setDeliverstate(ds);
		alipayRespNote.setOrder_no(cwbTransCwb);
		alipayRespNote.setTransaction_id(rootnote.getTransaction_Header().getTransaction_id());
		alipayRespNote.setDelivery_man(rootnote.getTransaction_Header().getExt_attributes().getDelivery_man());
		return alipayRespNote;
	}

}
