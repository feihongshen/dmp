package cn.explink.b2c.huitongtx.addressmatch;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import net.sf.json.JSONObject;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.huitongtx.Huitongtx;
import cn.explink.b2c.huitongtx.HuitongtxConfig;
import cn.explink.b2c.huitongtx.addressmatch.json.MatchData;
import cn.explink.b2c.huitongtx.addressmatch.json.MatchResponse;
import cn.explink.b2c.huitongtx.addressmatch.json_receiver.MatchRequest;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.domain.Branch;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.service.addressmatch.AddressMatchService;
import cn.explink.util.MD5.MD5Util;

@Service
public class HttxEditBranchService {

	@Autowired
	JiontDAO jiontDAO;

	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	DataImportService_B2c dataImportInterface;
	@Autowired
	CustomWareHouseDAO customWarehouseDAO;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	@Autowired
	DataImportService_B2c dataImportService_B2c;
	@Autowired
	AddressMatchService addressMatchService;
	@Autowired
	BranchDAO branchDAO;

	private static Logger logger = LoggerFactory.getLogger(HttxEditBranchService.class);

	/**
	 * 接收处理汇通天下站点匹配的方法
	 * 
	 * @param yhd_key
	 * @return
	 * @throws Exception
	 * @throws JAXBException
	 * @throws UnsupportedEncodingException
	 */
	public String invokeAddressmatch(Huitongtx httx, String app_key, String sign, String timestamp, String method, String requestXml) {

		try {

			List<MatchRequest> matchRequestlist = JacksonMapper.getInstance().readValue(requestXml, new TypeReference<List<MatchRequest>>() {
			}); // json转化为对象

			try {
				validateBaseInfo(app_key, sign, httx.getPrivate_key(), timestamp, method, requestXml, httx, matchRequestlist);
			} catch (RuntimeException e1) {
				logger.error("验证异常", e1);
				return responseMessage("-2", e1.getMessage(), null);
			}

			List<MatchData> retdatalist = new ArrayList<MatchData>();

			for (MatchRequest matchRequest : matchRequestlist) {
				String consigneeaddress = matchRequest.getReceiver_address();
				String taskcode = matchRequest.getTaskcode();

				MatchData matchdata = new MatchData();
				matchdata.setTaskcode(taskcode);

				if (consigneeaddress == null || consigneeaddress.isEmpty()) {
					matchdata.setStatus("0");
					matchdata.setMessage("地址为空");
					retdatalist.add(matchdata);
					continue;
				}

				matchdata.setStatus("1");
				matchdata.setMessage("SUCCESS");
				retdatalist.add(matchdata);

				JSONObject addressJson = addressMatchService.matchAddressByPublicInterface(taskcode, consigneeaddress); // 启动匹配过程

				String id = addressJson.getString("id"); // 请求唯一编码 id=taskcode
				String station = addressJson.getString("station"); // 对应返回的站点ID
				if (station.isEmpty()) {
					station = "0";
				}
				Branch branch = branchDAO.getBranchByBranchid(Long.valueOf(station));

			}

			String responseJSON = responseMessage("0", "SUCESS", retdatalist); // 返回成功

			logger.info("返回汇通天下-站点匹配,responseJSON={}", responseJSON);

			return responseJSON;

		} catch (Exception e) {
			logger.error("一号店站点匹配-系统未知异常", e);
			return responseMessage("-1", e.getMessage(), null);
		}

	}

	/**
	 * 验证基础的配置是否正确
	 * 
	 * @param partner
	 * @param signtype
	 * @param sign
	 * @param notify_id
	 * @param notify_type
	 * @param content
	 * @return
	 */
	private void validateBaseInfo(String app_key, String sign, String private_key, String timestamp, String method, String data, Huitongtx httx, List<MatchRequest> matchRequest) {

		if (!httx.getApp_key().equals(app_key)) {
			throw new RuntimeException("指定TP唯一appkey=" + app_key + "不正确!");
		}

		Map<String, String> params = HuitongtxConfig.buildParmsMap(method, timestamp, app_key, private_key, data);
		String signstr = httx.getPrivate_key() + HuitongtxConfig.createLinkString(params) + httx.getPrivate_key();
		// logger.info("汇通天下签名字符串="+signstr);
		if (!sign.equalsIgnoreCase(MD5Util.md5(signstr))) {
			// logger.info("加密结果="+MD5Util.md5(signstr));
			throw new RuntimeException("签名验证异常sign=" + sign);
		}
		if (matchRequest.size() == 0) {
			throw new RuntimeException("请求地址数不能为空");
		}

		if (matchRequest.size() > httx.getAddressMaxCount()) {
			throw new RuntimeException("请求地址数不能大于" + httx.getAddressMaxCount() + "个");
		}
	}

	/**
	 * 反馈给汇通天下信息 失败信息
	 * 
	 * @param log_result
	 * @param log_event
	 * @return
	 */
	public String responseMessage(String code, String message, List<MatchData> datalist) {
		String strs = "";
		MatchResponse resp = new MatchResponse();
		resp.setCode(code);
		resp.setMessage(message);
		if (datalist != null) {
			resp.setMatchData(datalist);
		}

		try {
			strs = JacksonMapper.getInstance().writeValueAsString(resp);
		} catch (JsonGenerationException e) {
			logger.error("", e);
		} catch (JsonMappingException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		}

		logger.info(strs);
		return strs;

	}

}
