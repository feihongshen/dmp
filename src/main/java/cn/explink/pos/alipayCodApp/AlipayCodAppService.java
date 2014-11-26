package cn.explink.pos.alipayCodApp;

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
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.UserDAO;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.util.StringUtil;
import cn.explink.util.MD5.MD5Util;

@Service
public class AlipayCodAppService {
	private Logger logger = LoggerFactory.getLogger(AlipayCodAppService.class);
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	AlipayCodAppServiceMaster alipayCodAppServiceMaster;
	@Autowired
	CwbDAO cwbDAO;

	@Autowired
	UserDAO userDAO;
	@Autowired
	OrderFlowDAO orderFlowDAO;

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

	public AliPayCodApp getAlipaySettingMethod(int key) {
		AliPayCodApp alipay = new AliPayCodApp();
		if (!"".equals(getObjectMethod(key))) {
			JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
			alipay = (AliPayCodApp) JSONObject.toBean(jsonObj, AliPayCodApp.class);
		} else {
			alipay = new AliPayCodApp();
		}

		return alipay == null ? new AliPayCodApp() : alipay;
	}

	public void update(int joint_num, int state) {
		jiontDAO.UpdateState(joint_num, state);
	}

	public void edit(HttpServletRequest request, int joint_num) {
		AliPayCodApp alipay = new AliPayCodApp();
		String partner = StringUtil.nullConvertToEmptyString(request.getParameter("partner"));
		String input_charset = StringUtil.nullConvertToEmptyString(request.getParameter("input_charset"));
		String sign_type = StringUtil.nullConvertToEmptyString(request.getParameter("sign_type"));
		String url = StringUtil.nullConvertToEmptyString(request.getParameter("url"));
		String private_key = StringUtil.nullConvertToEmptyString(request.getParameter("private_key"));
		String logistics_code = StringUtil.nullConvertToEmptyString(request.getParameter("logistics_code"));

		alipay.setPartner(partner);
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
	public String AnalyzXMLByAlipayCodApp(AliPayCodApp alipaycodapp, String service, String partner, String _input_charset, String sign_type, String sign, String timestamp, String logistics_bill_no,
			String logistics_code, String ord_pmt_time, String ord_pmt_amt) {

		try {

			ValidateBaseSetUp(alipaycodapp, partner, _input_charset, sign_type, logistics_code); // 验证基础信息

			Map<String, String> parmsMap = buildParmsMap(service, partner, _input_charset, sign_type, sign, timestamp, logistics_bill_no, logistics_code, ord_pmt_time, ord_pmt_amt);

			Map<String, String> paraFilter = AlipayCore.paraFilter(parmsMap); // 过滤不必要的加密类

			String createLinkString = AlipayCore.createLinkString(paraFilter); // 把数组所有元素排序，并按照“参数=参数值”

			boolean ispassSign = ValidateSign(alipaycodapp, createLinkString, sign);
			if (!ispassSign) {
				return createXMLResponse_exception(alipaycodapp.getPrivate_key(), sign_type, "ILLEGAL_SIGN");
			}

			if (service.equals("alipay.logistics.bill.query")) {
				return alipayCodAppServiceMaster.getAlipayCodAppService_search().toCwbSearch(alipaycodapp, service, partner, _input_charset, sign_type, timestamp, logistics_bill_no, logistics_code);

			}
			if (service.equals("alipay.logistics.bill.pay.notify")) {

				return alipayCodAppServiceMaster.getAlipayCodAppService_pay().toCwbPayAmount(alipaycodapp, service, partner, _input_charset, sign_type, timestamp, logistics_bill_no, logistics_code,
						ord_pmt_time, ord_pmt_amt);
			}

			return null;
		} catch (Exception e) {
			logger.error("支付宝CODAPP请求发生未知异常" + e.getMessage(), e);
			return createXMLResponse_exception(alipaycodapp.getPrivate_key(), sign_type, e.getMessage());
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

		String response = AlipayCodAppXMLHandler.createXMLException(error, respSign, sign_type);
		logger.error("返回支付宝CODAPP-" + response);
		return response;
	}

	private Map<String, String> createExceptionResponseMap(String error) {
		Map<String, String> respMap = new HashMap<String, String>();
		respMap.put("is_success", "F");
		respMap.put("error", error);
		return respMap;
	}

	private boolean ValidateSign(AliPayCodApp alipaycodapp, String createLinkString, String sign) {

		if (alipaycodapp.getSign_type().equals("MD5")) {
			String local_signstr = createLinkString + alipaycodapp.getPrivate_key();
			String local_sign = MD5Util.md5(local_signstr);
			if (!local_sign.equalsIgnoreCase(sign)) {
				logger.info("MD5签名校验失败，localsign=" + local_sign + ",sign=" + sign + ",createLinkString=" + createLinkString);
				return false;
			}
			return true;
		}

		return false;

	}

	public static void main(String[] args) {
		String str = "charset=UTF-8&logistics_bill_no=14011986729422&logistics_code=GZWPH&partner=2088011963129657&service=alipay.logistics.bill.pay.notify&timestamp=2014-01-22 13:45:56";
		String private_key = "dz1ivka0uz6794ogtmr373yvl980vgki";
		System.out.println(MD5Util.md5(str + private_key));
	}

	private Map<String, String> buildParmsMap(String service, String partner, String _input_charset, String sign_type, String sign, String timestamp, String logistics_bill_no, String logistics_code,
			String ord_pmt_time, String ord_pmt_amt) {
		Map<String, String> parmsMap = new HashMap<String, String>();
		if (service.equals("alipay.logistics.bill.query")) {
			parmsMap.put("service", service);
			parmsMap.put("partner", partner);
			parmsMap.put("charset", _input_charset);
			parmsMap.put("sign_type", sign_type);
			parmsMap.put("sign", sign);
			parmsMap.put("timestamp", timestamp);
			parmsMap.put("logistics_bill_no", logistics_bill_no);
			parmsMap.put("logistics_code", logistics_code);
		} else if (service.equals("alipay.logistics.bill.pay.notify")) {
			parmsMap.put("service", service);
			parmsMap.put("partner", partner);
			parmsMap.put("charset", _input_charset);
			parmsMap.put("sign_type", sign_type);
			parmsMap.put("sign", sign);
			parmsMap.put("timestamp", timestamp);
			parmsMap.put("logistics_bill_no", logistics_bill_no);
			parmsMap.put("logistics_code", logistics_code);
			parmsMap.put("ord_pmt_time", ord_pmt_time);
			parmsMap.put("ord_pmt_amt", ord_pmt_amt);

		}

		return parmsMap;
	}

	/**
	 * 验证基础设置是否正常
	 * 
	 * @param alipaycodapp
	 * @param partner
	 * @param _input_charset
	 * @param sign_type
	 */
	private void ValidateBaseSetUp(AliPayCodApp alipaycodapp, String partner, String _input_charset, String sign_type, String logistics_code) {
		// if(!alipaycodapp.getPartner().equals(partner)){
		// throw new
		// RuntimeException("请求partner="+partner+"不正确,当前设置partner="+alipaycodapp.getPartner());
		// }
		if (!alipaycodapp.getInput_charset().equalsIgnoreCase(_input_charset)) {
			throw new RuntimeException("请求charset=" + _input_charset + "不正确,当前设置charset=" + alipaycodapp.getInput_charset());
		}
		if (!alipaycodapp.getSign_type().equalsIgnoreCase(sign_type)) {
			throw new RuntimeException("请求sign_type=" + sign_type + "不正确,当前设置sign_type=" + alipaycodapp.getSign_type());
		}

		if (!alipaycodapp.getLogistics_code().equalsIgnoreCase(logistics_code)) {
			throw new RuntimeException("请求logistics_code=" + logistics_code + "不正确,当前设置logistics_code=" + alipaycodapp.getLogistics_code());
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
