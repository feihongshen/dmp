package cn.explink.client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.SystemInstallDAO;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.exception.ExplinkRuntimeException;
import cn.explink.util.HttpUtil;
import cn.explink.util.JsonUtil;

@Service
public class OmsClient {

	private static final String DEFAULT_OMS_BASE_URL = "http://127.0.0.1:8080/oms/";

	@Autowired
	private SystemInstallDAO systemInstallDAO;

	/**
	 * 通知OMS /orderflow/saveFlowB2cSend
	 * 
	 * @param orderFlow
	 * @return
	 */
	public String saveFlowB2cSend(OrderFlow orderFlow) {
		String url = getOmsBaseUrl() + "orderflow/saveFlowB2cSend";
		return notifyOmsOrderFlow(orderFlow, url);
	}

	/**
	 * 通知OMS /orderflow/saveFlow
	 * 
	 * @param orderFlow
	 * @return
	 */
	public String saveFlow(OrderFlow orderFlow) {
		String url = getOmsBaseUrl() + "orderflow/saveFlow";
		return notifyOmsOrderFlow(orderFlow, url);
	}

	public String notifyOmsOrderFlow(OrderFlow orderFlow, String url) {
		Map<String, String> params = new HashMap<String, String>();
		try {
			params.put("orderFlow", JsonUtil.translateToJson(orderFlow));
		} catch (Exception e) {
			throw new ExplinkRuntimeException("translate order flow to json failed", e);
		}
		String response = null;
		try {
			response = HttpUtil.post(url, params);
		} catch (Exception e) {
			throw new ExplinkRuntimeException("notify OmsFailed.", e);
		}
		return response;
	}

	/**
	 * 从数据库配置中获取oms的base url
	 * 
	 * @return
	 */
	protected String getOmsBaseUrl() {
		//SystemInstall omsPathUrl = systemInstallDAO.getSystemInstallByName("omsPathUrl");
		SystemInstall omsUrl = systemInstallDAO.getSystemInstallByName("omsUrl");
		String omsBaseUrl = DEFAULT_OMS_BASE_URL;
		//if (omsPathUrl != null && omsUrl != null) {
			//omsBaseUrl = omsPathUrl.getValue() + omsUrl.getValue();
		if ( omsUrl != null) {
			omsBaseUrl =omsUrl.getValue();
		}
		return omsBaseUrl;
	}

}
