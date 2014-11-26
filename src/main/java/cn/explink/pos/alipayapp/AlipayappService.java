package cn.explink.pos.alipayapp;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.UserDAO;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.service.CwbOrderService;
import cn.explink.util.StringUtil;
import cn.explink.util.MD5.MD5Util;

@Service
public class AlipayappService {
	private Logger logger = LoggerFactory.getLogger(AlipayappService.class);
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	AlipayappServiceMaster alipayappServiceMaster;
	@Autowired
	CwbDAO cwbDAO;

	@Autowired
	UserDAO userDAO;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	CwbOrderService cwbOrderService;

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

	public Alipayapp getAlipayapp(int key) {
		Alipayapp alipay = new Alipayapp();
		if (!"".equals(getObjectMethod(key))) {
			JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
			alipay = (Alipayapp) JSONObject.toBean(jsonObj, Alipayapp.class);
		} else {
			alipay = new Alipayapp();
		}

		return alipay == null ? new Alipayapp() : alipay;
	}

	public void update(int joint_num, int state) {
		jiontDAO.UpdateState(joint_num, state);
	}

	public void edit(HttpServletRequest request, int joint_num) {
		Alipayapp alipay = new Alipayapp();
		String input_charset = StringUtil.nullConvertToEmptyString(request.getParameter("input_charset"));
		String sign_type = StringUtil.nullConvertToEmptyString(request.getParameter("sign_type"));
		String url = StringUtil.nullConvertToEmptyString(request.getParameter("url"));
		String private_key = StringUtil.nullConvertToEmptyString(request.getParameter("private_key"));
		String logistics_code = StringUtil.nullConvertToEmptyString(request.getParameter("logistics_code"));

		alipay.setInput_charset(input_charset);
		alipay.setSign_type(sign_type);
		alipay.setUrl(url);
		alipay.setPrivate_key(private_key);
		alipay.setLogistics_code(logistics_code);

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

	/**
	 * 处理alipaycodApp的请求
	 * 
	 * @return code
	 * @throws UnsupportedEncodingException
	 */
	public String AnalyzParamsByAlipayapp(Alipayapp alipayapp, AlipayParam param) {

		try {

			ValidateBaseSetUp(alipayapp, param); // 验证基础信息

			Map<String, String> parmsMap = buildParmsMap(param);

			Map<String, String> paraFilter = AlipayCore.paraFilter(parmsMap); // 过滤不必要的加密类

			String createLinkString = AlipayCore.createLinkString(paraFilter); // 把数组所有元素排序，并按照“参数=参数值”

			boolean ispassSign = ValidateSign(alipayapp, createLinkString, param.getSign()); // 签名验证
			if (!ispassSign) {
				return createXMLResponse_exception(alipayapp.getPrivate_key(), param.getSign_type(), "ILLEGAL_SIGN");
			}

			if (param.getService().equals("alipay.logistics.billpayinfo.query")) { // 执行查询接口
				return alipayappServiceMaster.getAlipayCodAppService_search().toCwbSearch(alipayapp, param);

			}
			if (param.getService().equals("alipay.logistics.bill.payresult")) { // 执行反馈接口

				return alipayappServiceMaster.getAlipayCodAppService_pay().toCwbPayAmount(alipayapp, param);
			}

			return null;
		} catch (Exception e) {
			logger.error("alipay-app请求发生未知异常" + e.getMessage(), e);
			return createXMLResponse_exception(alipayapp.getPrivate_key(), param.getSign_type(), e.getMessage());
		}

	}

	/**
	 * 构建返回异常的Xml
	 * 
	 * @param alipaycodapp
	 * @param sign_type
	 * @return
	 */
	public String createXMLResponse_exception(String private_key, String sign_type, String error) {
		Map<String, String> respMap = createExceptionResponseMap(error);
		String createLinkStringResp = AlipayCore.createLinkString(respMap);
		String respSign = MD5Util.md5(createLinkStringResp + private_key);

		String response = AlipayappXMLHandler.createXMLException(error, respSign, sign_type);
		logger.error("返回支付宝CODAPP-" + response);
		return response;
	}

	private Map<String, String> createExceptionResponseMap(String error) {
		Map<String, String> respMap = new HashMap<String, String>();
		respMap.put("is_success", "F");
		respMap.put("error", error);
		return respMap;
	}

	private boolean ValidateSign(Alipayapp alipayapp, String createLinkString, String sign) {

		if (alipayapp.getSign_type().equals("MD5")) {
			String local_signstr = createLinkString + alipayapp.getPrivate_key();
			String local_sign = MD5Util.md5(local_signstr);
			System.out.println(local_sign);
			if (!local_sign.equalsIgnoreCase(sign)) {
				logger.info("MD5签名校验失败，localsign=" + local_sign + ",sign=" + sign + ",createLinkString=" + createLinkString);
				return false;
			}
			return true;
		}

		return false;

	}

	public static void main(String[] args) {
		String str = "amount=0.50&amt_can_modify=N&deliver_mobile=&goods_name=&is_success=T&payable=Yd3be1706ac415441a265c3e67edecfe9";

		System.out.println(MD5Util.md5(str));
	}

	private Map<String, String> buildParmsMap(AlipayParam param) {
		Map<String, String> parmsMap = new HashMap<String, String>();
		parmsMap.put("service", param.getService());
		parmsMap.put("charset", param.getCharset());
		parmsMap.put("sign_type", param.getSign_type());
		parmsMap.put("sign", param.getSign());

		parmsMap.put("bill_no", param.getBill_no());

		if (param.getService().equals("alipay.logistics.billpayinfo.query")) {
			parmsMap.put("logistics_code", param.getLogistics_code());
		} else if (param.getService().equals("alipay.logistics.bill.payresult")) {
			parmsMap.put("bill_id", param.getBill_id());
			parmsMap.put("pay_amount", param.getPay_amount());
			parmsMap.put("query_amount", param.getQuery_amount());
			parmsMap.put("pay_status", param.getPay_status());
			parmsMap.put("trade_time", param.getTrade_time());
		}

		return parmsMap;
	}

	/**
	 * 验证基础设置是否正常
	 * 
	 * @param alipayapp
	 * @param partner
	 * @param _input_charset
	 * @param sign_type
	 */
	private void ValidateBaseSetUp(Alipayapp alipayapp, AlipayParam param) {

		if (!alipayapp.getInput_charset().equalsIgnoreCase(param.getCharset())) {
			throw new RuntimeException("请求charset=" + param.getCharset() + "不正确,当前设置charset=" + alipayapp.getInput_charset());
		}
		if (!alipayapp.getSign_type().equalsIgnoreCase(param.getSign_type())) {
			throw new RuntimeException("请求sign_type=" + param.getSign_type() + "不正确,当前设置sign_type=" + alipayapp.getSign_type());
		}

		if (param.getService().equals("alipay.logistics.billpayinfo.query")) {
			if (!alipayapp.getLogistics_code().equalsIgnoreCase(param.getLogistics_code())) {
				throw new RuntimeException("请求logistics_code=" + param.getLogistics_code() + "不正确,当前设置logistics_code=" + alipayapp.getLogistics_code());
			}
		}

	}

	public static String parse(String xml) {
		xml = xml.replaceAll("&", "&amp;");
		xml = xml.replaceAll("<", "&lt;");
		xml = xml.replaceAll(">", "&gt;");
		xml = xml.replaceAll("'", "&apos;");
		xml = xml.replaceAll("\"", "&quot;");
		return xml;
	}

}
