package cn.explink.pos.yalian;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONObject;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.ExptCodeJointDAO;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.ShangMenTuiCwbDetailDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.pos.tools.PosEnum;
import cn.explink.service.CwbOrderService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.StringUtil;

@Service
public class YalianDMPService {
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	ExptCodeJointDAO exptCodeJointDAO;
	@Autowired
	ShangMenTuiCwbDetailDAO shangMenTuiCwbDetailDAO;

	private Logger logger = LoggerFactory.getLogger(YalianDMPService.class);

	private String getObjectMethod(int key) {
		JointEntity obj = null;
		String posValue = "";
		try {
			obj = jiontDAO.getJointEntity(key);
			posValue = obj.getJoint_property();
		} catch (Exception e) {
		}
		return posValue;
	}

	public void update(int joint_num, int state) {
		jiontDAO.UpdateState(joint_num, state);
	}

	public void edit(HttpServletRequest request, int joint_num) {
		YalianApp mobile = new YalianApp();
		String sender_url = StringUtil.nullConvertToEmptyString(request.getParameter("trick_url"));
		String receiver_url = StringUtil.nullConvertToEmptyString(request.getParameter("request_url"));
		String liantong = StringUtil.nullConvertToEmptyString(request.getParameter("liantong"));
		String dianxin = StringUtil.nullConvertToEmptyString(request.getParameter("dianxin"));
		String yidong = StringUtil.nullConvertToEmptyString(request.getParameter("yidong"));

		mobile.setTrick_url(sender_url);
		mobile.setRequest_url(receiver_url);
		mobile.setCode_dianxin(dianxin);
		mobile.setCode_yidong(yidong);
		mobile.setCode_liantong(liantong);

		JSONObject jsonObj = JSONObject.fromObject(mobile);
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

	/*
	 * 登录接口
	 */
	public String JudgmentLanded(String xml) {

		String resp_code = ""; // 验证消息编码
		String resp_msg = ""; // 返回信息描述
		String deliverName = "";// 用户名
		String deliverPwd = "";// 密码
		String mobile = "";
		try {
			Map<String, Object> map = getHttpXml(xml);
			deliverName = (String) map.get("deliverName");
			deliverPwd = (String) map.get("deliverPwd");
			// employee=jiontDAO.getEmployeeInfo(deliverName);
			User user = userDAO.getUserByUsername(deliverName);
			if (user == null) {
				resp_code = YalianAppEnum.usernull.getResp_code();
				resp_msg = YalianAppEnum.usernull.getResp_msg();
				logger.error("登陆失败,没有此用户名!" + deliverName);
			} else {
				String pwd = user.getPassword(); // 数据库获取的password
				// 转换密码,改为md5加密,去掉奇数位0
				if (pwd.equalsIgnoreCase(deliverPwd)) { // 密码相同
					resp_code = YalianAppEnum.Success.getResp_code();
					resp_msg = YalianAppEnum.Success.getResp_msg();
					// phone=employee.getBranchmobile();
					mobile = user.getUsermobile();
					logger.info("亚联App登录成功，当前用户名{},密码{}", deliverName, deliverPwd);
				} else {
					resp_code = YalianAppEnum.passwordwrong.getResp_code();
					resp_msg = YalianAppEnum.passwordwrong.getResp_msg();
					logger.info("亚联App:用户名为{}登录失败，密码验证错误!", deliverName);
				}
			}

		} catch (Exception e) {

			resp_code = YalianAppEnum.xitongcuowu.getResp_code();
			resp_msg = YalianAppEnum.xitongcuowu.getResp_msg();
			logger.error("亚联App登录接口出现异常", e);
		}
		// 生成响应报文
		String param[] = { deliverName, mobile, resp_code, resp_msg };
		String responseXml = createXMLMessage_Login(param, 1);
		logger.info("我们需要返回给亚联App的xml:{}", responseXml);
		return responseXml;
	}

	/*
	 * 订单确认接口
	 */

	public String ComfirmReceive(String xml) {
		String cwb = "";
		String responseXml = "";
		try {
			Map<String, Object> map = getHttpXml(xml);
			cwb = (String) map.get("cwb");
			String status = "确认配送中";
			String time = (String) map.get("operateDate");
			cwbDAO.updateYalianApp(cwb, status + "-----" + time);
			shangMenTuiCwbDetailDAO.updateRemark3ByCwb(cwb, status + "-----" + time);
			String param[] = { cwb, YalianAppEnum.Success.getResp_code(), YalianAppEnum.Success.getResp_msg() };
			responseXml = createXMLMessage_Login(param, 2);
		} catch (Exception e) {
			e.printStackTrace();
			String param[] = { cwb, YalianAppEnum.xitongcuowu.getResp_code(), YalianAppEnum.xitongcuowu.getResp_msg() };
			responseXml = createXMLMessage_Login(param, 2);
		}
		logger.info("我们需要返回给亚联App的xml:{}", responseXml);
		return responseXml;

	}

	private String createXMLMessage_Login(String param[], int flag) {
		String requestxml = "";
		if (flag == 1) {
			requestxml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?> " + "<Response>" + "<deliverName>" + param[0] + "</deliverName>" + "<deliverNum>" + param[1] + "</deliverNum>" + "<statusCode>"
					+ param[2] + "</statusCode>" + "<errorInfo>" + param[3] + "</errorInfo>" + "</Response>";
		}
		if (flag == 2) {
			requestxml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?> " + "<Response>" + "<cwb>" + param[0] + "</cwb>" + "<statusCode>" + param[1] + "</statusCode>" + "<errorInfo>" + param[2]
					+ "</errorInfo>" + "</Response>";
		}
		return requestxml;
	}

	public Map<String, Object> getHttpXml(String xml) throws Exception {
		InputStream iStream = new ByteArrayInputStream(xml.getBytes("utf-8"));
		SAXReader saxReader = new SAXReader();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Reader r = new InputStreamReader(iStream, "utf-8");
		Document document = saxReader.read(r);
		Element employees = document.getRootElement();
		for (Iterator i = employees.elementIterator(); i.hasNext();) {
			Element employee = (Element) i.next();
			returnMap.put(employee.getName(), employee.getText());

		}
		return returnMap;
	}

	public YalianApp getyalianapp(int key) {
		YalianApp yalian = new YalianApp();
		if (!"".equals(getObjectMethod(key))) {
			JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
			yalian = (YalianApp) JSONObject.toBean(jsonObj, YalianApp.class);
		} else {
			yalian = new YalianApp();
		}
		return yalian == null ? new YalianApp() : yalian;
	}

	/*
	 * 订单反馈
	 */
	public String RebackXmlForYalain(int key, String xml) {
		String cwb = "";
		try {
			Map<String, Object> map = getHttpXml(xml);
			cwb = (String) map.get("cwb");
			cwb = cwbOrderService.translateCwb(cwb); // 可能是订单号也可能是运单
			String userName = (String) map.get("userName");
			String deliverMobile = (String) map.get("deliverMobile");
			String status = (String) map.get("status");
			String backReasonId = (String) map.get("backReasonId");
			String leavedReasonId = (String) map.get("leavedReasonId");
			String receivedFee = (String) map.get("receivedFeeCash");
			double receivedFeeCash = Double.valueOf(receivedFee);
			String receivedType = (String) map.get("receivedType");
			String remark = (String) map.get("remark");
			String operateDate = (String) map.get("operateDate");

			CwbOrder cwborder = cwbDAO.getCwbByCwb(cwb);

			if (cwborder == null) {
				String[] param = { cwb, YalianAppEnum.Danhaonull.getResp_code(), YalianAppEnum.Danhaonull.getResp_msg() };
				return createXMLMessage_Login(param, 2);// 单号不存在
			}
			List<User> deliveryList = userDAO.getUserByUsernameAndMobile((String) map.get("deliverMobile"));
			if (deliveryList == null | deliveryList.size() == 0 | !deliveryList.get(0).getUsername().equals(userName) | deliveryList.size() > 1) {
				String[] param = { cwb, YalianAppEnum.usernull.getResp_code(), YalianAppEnum.usernull.getResp_msg() };
				return createXMLMessage_Login(param, 2);// "用户名不存在或者电话号码存在重复";
			}
			User deliveryUser = deliveryList.get(0);
			// 把参数放在map集合中
			ManageXmlForConfirm(status, backReasonId, leavedReasonId, receivedFeeCash, receivedType, remark, cwborder, deliveryUser);
			String[] param = { cwb, YalianAppEnum.Success.getResp_code(), YalianAppEnum.Success.getResp_msg() };
			return createXMLMessage_Login(param, 2);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("亚联订单接口反馈出现异常{}", e);
			String[] param = { cwb, YalianAppEnum.xitongcuowu.getResp_code(), YalianAppEnum.xitongcuowu.getResp_msg() };
			return createXMLMessage_Login(param, 2);
		}

	}

	private void ManageXmlForConfirm(String status, String backReasonId, String leavedReasonId, double receivedFeeCash, String receivedType, String remark, CwbOrder cwborder, User deliveryUser) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		long type = Long.parseLong(status);
		long backedreasonid = 0;
		long leavedreasonid = 0;
		if (type == DeliveryStateEnum.JuShou.getValue() || type == DeliveryStateEnum.ShangMenJuTui.getValue()) {
			backedreasonid = (exptCodeJointDAO.getExpMatchListByPosCode(backReasonId, PosEnum.MobileEtong.getKey())).getReasonid();
		} else if (type == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
			leavedreasonid = (exptCodeJointDAO.getExpMatchListByPosCode(leavedReasonId, PosEnum.MobileEtong.getKey())).getReasonid();
		}
		parameters.put("deliverid", new Long((long) deliveryUser.getUserid()));
		parameters.put("podresultid", type);
		parameters.put("backreasonid", backedreasonid);
		parameters.put("leavedreasonid", leavedreasonid);
		if (receivedType.equals("1")) {
			parameters.put("receivedfeepos", (BigDecimal.valueOf(receivedFeeCash)));
			parameters.put("receivedfeecash", (BigDecimal.ZERO));
		} else {
			parameters.put("receivedfeecash", (BigDecimal.valueOf(receivedFeeCash)));
			parameters.put("receivedfeepos", (BigDecimal.ZERO));
		}
		parameters.put("receivedfeecheque", BigDecimal.ZERO);
		parameters.put("receivedfeeother", BigDecimal.ZERO);
		parameters.put("paybackedfee", BigDecimal.ZERO);
		parameters.put("podremarkid", (long) 0);
		parameters.put("checkremark", receivedType.equals("0") ? "现金支付" : "");
		parameters.put("posremark", receivedType.equals("1") ? "pos支付" : "");
		parameters.put("deliverstateremark", "手机终端反馈-" + remark);
		parameters.put("owgid", 0);
		parameters.put("sessionbranchid", deliveryUser.getBranchid());
		parameters.put("sessionuserid", deliveryUser.getUserid());
		parameters.put("sign_typeid", 1);
		parameters.put("sign_man", cwborder.getConsigneename());
		parameters.put("sign_time", DateTimeUtil.getNowTime());
		parameters.put("nosysyemflag", 1);//

		cwbOrderService.deliverStatePod(deliveryUser, cwborder.getCwb(), cwborder.getCwb(), parameters);
	}

	public String ModificationXmlforConsignee(int key, String xml) {
		String cwb = "";
		try {
			Map<String, Object> map = getHttpXml(xml);
			cwb = (String) map.get("cwb");
			String cwb1 = cwbOrderService.translateCwb(cwb); // 可能是订单号也可能是运单号
			String consigneeName = (String) map.get("consigneeName");
			String consigneMobile = (String) map.get("consigneMobile");
			String consigneeCerdType = (String) map.get("consigneeCerdType");
			String consigneeCerdNum = (String) map.get("consigneeCerdNum");
			String operateDate = (String) map.get("operateDate");
			CwbOrder cwborder = cwbDAO.getCwbByCwb(cwb1);
			if (cwborder == null) {
				String[] param = { cwb, YalianAppEnum.Danhaonull.getResp_code(), YalianAppEnum.Danhaonull.getResp_msg() };
				String reaponsexml = createXMLMessage_Login(param, 2);
				return reaponsexml;
			}
			String remark5 = "更改收件人" + consigneeName + operateDate + "[原收件人：" + cwborder.getConsigneename() + ",电话：" + cwborder.getConsigneemobile() + "],代收人证件类型：" + consigneeCerdType + "证件号："
					+ consigneeCerdNum;
			logger.info("remark5，{}", remark5);
			String reaponsexml = "";
			dataImportDAO_B2c.updateRemark5Bycwb(cwb, remark5, consigneMobile);
			shangMenTuiCwbDetailDAO.updateRemark5ByCwb(cwb, remark5, consigneeName, consigneMobile);
			String[] param = { cwb, YalianAppEnum.Success.getResp_code(), YalianAppEnum.Success.getResp_msg() };
			reaponsexml = createXMLMessage_Login(param, 2);
			return reaponsexml;
		} catch (Exception e) {
			logger.error("亚联update错误:", e);
			String[] param = { cwb, YalianAppEnum.xitongcuowu.getResp_code(), YalianAppEnum.xitongcuowu.getResp_msg() };
			String reaponsexml = createXMLMessage_Login(param, 2);
			return reaponsexml;
		}
	}

}
