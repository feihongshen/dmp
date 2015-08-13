package cn.explink.service;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.SystemInstallDAO;
import cn.explink.domain.Common;
import cn.explink.domain.SystemInstall;
import cn.explink.util.JSONReslutUtil;
import cn.explink.util.StringUtil;

@Service
public class CommonService {

	@Autowired
	SystemInstallDAO systemInstallDAO;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public Common loadFormForCommon(HttpServletRequest request) {
		Common common = new Common();
		common.setId(Integer.parseInt(request.getParameter("id") == null ? "0" : request.getParameter("id")));
		common.setCommonname(StringUtil.nullConvertToEmptyString(request.getParameter("commonname")));
		common.setCommonnumber(StringUtil.nullConvertToEmptyString(request.getParameter("commonnumber")));
		common.setCommonstate(Integer.parseInt(request.getParameter("commonstate") == null ? "1" : request.getParameter("commonstate")));
		common.setOrderprefix(StringUtil.nullConvertToEmptyString(request.getParameter("orderprefix")));

		return common;
	}

	public void initCommonList() {
		try {
			//SystemInstall omsPathUrl = systemInstallDAO.getSystemInstallByName("omsPathUrl");
			SystemInstall omsUrl = systemInstallDAO.getSystemInstallByName("omsUrl");
			String url1 = "";
			if ( omsUrl != null) {
				url1 = omsUrl.getValue();
			} else {
				url1 = "http://127.0.0.1:8080/oms/";
			}
			final String url = url1;

			JSONReslutUtil.getResultMessage(url + "/epaicore/initCommonList", "flag=1", "POST").toString();

		} catch (Exception e) {
			logger.error("dmp承运商修改调用oms重置异常", e);
		}

	}

}
