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
public class AccountClient {

	private static final String DEFAULT_ACCOUNT_BASE_URL = "http://127.0.0.1:8080/account";

	private static final String BACKEND_DMPID = "backendRequester";

	@Autowired
	private SystemInstallDAO systemInstallDAO;

	/**
	 * 通知Account /orderflow
	 * 
	 * @param orderFlow
	 * @return
	 */
	public String accountOrderFlow(OrderFlow orderFlow) {
		String url = getAccountUrl() + "jmsCenter/saveEmailAndCwbDetail";
		return notifyOrderFlow(orderFlow, url);
	}

	public String notifyOrderFlow(OrderFlow orderFlow, String url) {
		Map<String, String> params = new HashMap<String, String>();
		try {
			params.put("dmpid", BACKEND_DMPID);
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
		// System.out.println(response);
		return response;
	}

	/**
	 * 从数据库配置中获取oms的base url
	 * 
	 * @return
	 */
	protected String getAccountUrl() {
		SystemInstall accountPathUrl = systemInstallDAO.getSystemInstallByName("accountPathUrl");
		SystemInstall accountUrl = systemInstallDAO.getSystemInstallByName("accountUrl");
		String accountBaseUrl = DEFAULT_ACCOUNT_BASE_URL;
		if (accountPathUrl != null && accountUrl != null) {
			accountBaseUrl = accountPathUrl.getValue() + accountUrl.getValue();
		}
		return accountBaseUrl;
	}

}
