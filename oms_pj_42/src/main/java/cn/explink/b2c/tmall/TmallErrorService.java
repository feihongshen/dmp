package cn.explink.b2c.tmall;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.huitongtx.response.response;
import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.ExceptionTrace;
import cn.explink.b2c.tools.FlowFromJMSB2cService;
import cn.explink.b2c.tools.Mail;
import cn.explink.domain.B2CData;
import cn.explink.domain.Customer;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;

/**
 * tmall新增推送异常处理类
 * 
 * @author Administrator
 *
 */
@Service
public class TmallErrorService {
	private Logger logger = LoggerFactory.getLogger(TmallErrorService.class);
	@Autowired
	private B2cTools b2ctools;
	@Autowired
	private B2CDataDAO b2cDataDAO;
	@Autowired
	FlowFromJMSB2cService flowFromJMSB2cService;

	// 拼接XML
	private String buildExceptionXMLAndSend(Tmall tmall, B2CData b2cData, TmallXMLNote note) {// 签收前支付的标识
		StringBuffer sub = new StringBuffer();
		sub.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		sub.append("<request>");
		sub.append("<cpCode>" + tmall.getService_code() + "</cpCode>");
		sub.append("<logisticsId>" + note.getLogisticsId() + "</logisticsId>");
		sub.append("<mailNo>" + note.getMailNo() + "</mailNo>");
		sub.append("<exceptionCode>" + note.getExceptionCode() + "</exceptionCode>");
		sub.append("<exceptionTime>" + note.getExceptionTime() + "</exceptionTime>");
		sub.append("<actionCode>" + note.getActionCode() + "</actionCode>");
		sub.append("<nextDispatchTime>" + note.getNextDispatchTime() + "</nextDispatchTime>");
		sub.append("<remark>" + note.getRemark() + "</remark>");
		sub.append("<operatorMobile>" + note.getOperatorMobile() + "</operatorMobile>");
		sub.append("<operatorPhone>" + note.getOperatorPhone() + "</operatorPhone>");
		sub.append("<extendHandlingContent></extendHandlingContent>");
		sub.append("</request>");

		String response = "";
		try {
			response = TmallConfig.postHttpdataTotmallExpt(tmall.getExpt_url(), tmall.getPrivate_key(), sub.toString(), tmall.getService_code());
		} catch (Exception e) {
			logger.error("推送通信异常cwb=" + note.getMailNo(), e);
		}

		return response;

	}

	public void excuteTmallExceptionMethod(Tmall tmall, B2CData b2cData, TmallXMLNote note) {

		String responseXML = buildExceptionXMLAndSend(tmall, b2cData, note);

		logger.info("天猫异常信息返回xml={},cwb={}", responseXML, b2cData.getCwb());

		Map<String, Object> respMap = new HashMap<String, Object>();
		try {
			respMap = TmallConfig.Analyz_XmlDocByTmall(responseXML);
		} catch (Exception e1) {
			logger.error("解析tmall-异常返回异常" + b2cData.getCwb(), e1);
		}
		String is_success = respMap.get("success") == null ? "false" : respMap.get("success").toString();

		int send_b2c_flag = is_success.equals("true") ? 1 : 2;
		String error = respMap.get("errorCode") == null ? "未知异常" : respMap.get("errorCode").toString();
		String errorMsg = respMap.get("errorMsg") == null ? "未知异常" : respMap.get("errorMsg").toString();
		String is_remark = send_b2c_flag == 2 ? error + errorMsg : "";

		try {
			b2cDataDAO.updateB2cIdSQLResponseStatus(b2cData.getB2cid(), send_b2c_flag, is_remark);
		} catch (Exception e) {
			logger.error("更新推送表异常" + b2cData.getCwb(), e);
		}
	}

}
