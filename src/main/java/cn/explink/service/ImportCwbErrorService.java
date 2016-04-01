package cn.explink.service;

import net.sf.json.JSONObject;

import org.apache.camel.Consume;
import org.apache.camel.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.CwbErrorDAO;

@Service
public class ImportCwbErrorService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	CwbErrorDAO CwbErrorDAO;

	@Consume(uri = "jms:queue:cwborderinsert")
	public void saveError(@Header("errorOrder") String errorOrder) {
		JSONObject errorJson = JSONObject.fromObject(errorOrder);
		try {
			String cwb = errorJson.getJSONObject("cwbOrderDTO").getString("cwb");
			logger.info("import excel error order cwb:{}", cwb);
			String emaildate = errorJson.getJSONObject("cwbOrderDTO").getString("emaildate");
			long emaildateid = errorJson.getLong("emaildateid");
			String message = errorJson.getString("message");
			CwbErrorDAO.creCwbError(cwb, errorJson.toString(), emaildateid, emaildate, message);
		} catch (Exception e) {
			logger.error("", e);
			logger.error("error while doing import excel error order cwb:{}", errorJson.getJSONObject("cwbOrderDTO").getString("cwb"));
			logger.error(errorOrder);
		}
	}

	public void saveOrderError(JSONObject errorJson) {
		try {
			String cwb = errorJson.getJSONObject("cwbOrderDTO").getString("cwb");
			logger.info("import excel error order cwb:{}", cwb);
			String emaildate = errorJson.getJSONObject("cwbOrderDTO").getString("emaildate");
			long emaildateid = errorJson.getLong("emaildateid");
			String message = errorJson.getString("message");
			CwbErrorDAO.creCwbError(cwb, errorJson.toString(), emaildateid, emaildate, message);
		} catch (Exception e) {
			logger.error("", e);
			logger.error("error while doing import excel error order cwb:{}", errorJson.getJSONObject("cwbOrderDTO").getString("cwb"));
			logger.error(errorJson.toString());
		}
	}

}
