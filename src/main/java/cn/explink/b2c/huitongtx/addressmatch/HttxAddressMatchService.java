package cn.explink.b2c.huitongtx.addressmatch;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
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
import cn.explink.b2c.huitongtx.HuitongtxService;
import cn.explink.b2c.huitongtx.addressmatch.json.MatchData;
import cn.explink.b2c.huitongtx.addressmatch.json.MatchResponse;
import cn.explink.b2c.huitongtx.addressmatch.json_receiver.MatchRequest;
import cn.explink.b2c.huitongtx.addressmatch.json_send.MatchResultData;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.RestHttpServiceHanlder;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.domain.Branch;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.service.addressmatch.AddressMatchService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.MD5.MD5Util;

@Service
public class HttxAddressMatchService {

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
	@Autowired
	HttxEditBranchDAO httxEditBranchDAO;
	@Autowired
	HuitongtxService huitongtxService;

	private static Logger logger = LoggerFactory.getLogger(HttxAddressMatchService.class);

	// public static void main(String[] args) throws JsonParseException,
	// JsonMappingException, IOException {
	// String
	// requestXml="[{\"taskcode\":\"32087\",\"receiver_city\":\"北京\",\"receiver_address\":\"海淀区东王庄小区8号楼\"}]";
	//
	// List<MatchRequest> matchRequestlist =
	// JacksonMapper.getInstance().readValue(requestXml,new
	// TypeReference<List<MatchRequest>>() {}); //json转化为对象
	//
	// for(MatchRequest ss:matchRequestlist){
	// System.out.println(ss.getTaskcode());
	// System.out.println(ss.getReceiver_address());
	// System.out.println(ss.getReceiver_city());
	// }
	// }
	//

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

				try {
					String receiver_city = matchRequest.getReceiver_city();
					String consigneeaddress = matchRequest.getReceiver_address();
					String taskcode = matchRequest.getTaskcode();

					MatchData matchdata = new MatchData();
					matchdata.setTaskcode(taskcode);

					long taskCount = httxEditBranchDAO.getHttxCountByCwb(taskcode);
					if (taskCount > 0) {
						matchdata.setStatus("0");
						matchdata.setMessage("该信息已存在");
						retdatalist.add(matchdata);
						continue;
					}

					if (consigneeaddress == null || consigneeaddress.isEmpty() || consigneeaddress.contains("null")) {
						matchdata.setStatus("0");
						matchdata.setMessage("地址为空");
						retdatalist.add(matchdata);
						continue;
					}

					matchdata.setStatus("1");
					matchdata.setMessage("SUCCESS");
					retdatalist.add(matchdata);

					// String
					// ssss="[{\"id\":\"246038\",\"station\":null,\"error\":\"1\",\"memo\":null}]";

					JSONObject addressJson = addressMatchService.matchAddressByPublicInterface(taskcode, consigneeaddress); // 启动匹配过程

					String id = addressJson.getString("id"); // 请求唯一编码
																// id=taskcode
					String station = addressJson.getString("station"); // 对应返回的站点ID
					if (station == null || station.isEmpty()) {
						station = "0";
					}
					Branch branch = branchDAO.getBranchByBranchid(Long.valueOf(station));
					int matchtype = Long.valueOf(station) > 0 ? 1 : 0; // 判断是否自动匹配
																		// 1自动匹配，0
																		// 未匹配
					String matchbranch = station.equals("0") ? "" : branch.getBranchname();

					String dealtime = matchtype == 1 ? DateTimeUtil.getNowTime() : "";

					// 插入临时匹配表中
					httxEditBranchDAO.creHttxEditBranchData(taskcode, receiver_city, consigneeaddress, DateTimeUtil.getNowTime(), dealtime, 0, matchtype, matchbranch, branch.getBranchid());
				} catch (Exception e) {
					logger.error("请求站点匹配处理某个数据异常taskcode=" + matchRequest.getTaskcode(), e);
				}

			}

			String responseJSON = responseMessage("0", "SUCESS", retdatalist); // 返回成功

			logger.info("返回汇通天下-站点匹配,responseJSON={}", responseJSON);

			return responseJSON;

		} catch (Exception e) {
			logger.error("快行线站点匹配-系统未知异常", e);
			return responseMessage("-1", e.getMessage(), null);
		}

	}

	// public static void main(String[] args) {
	//
	// String
	// ssss="{\"id\":\"246038\",\"station\":null,\"error\":\"1\",\"memo\":null}";
	// JSONObject addressJson =JSONObject.fromObject(ssss);
	//
	// String station=addressJson.getString("station"); //对应返回的站点ID
	// if(station==null||station.isEmpty()||station.contains("null")){
	// station="0";
	// }
	// long namess=Long.valueOf(station);
	// System.out.println("结果："+namess);
	//
	//
	//
	//
	// }

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
			logger.info("签名不一致");
			// throw new RuntimeException("签名验证异常sign="+sign);
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

		// logger.info(strs);
		return strs;

	}

	/**
	 * 查询未推送的地址数据,回传给快行线
	 */
	public void SendHttxBranchMatchResult(int matchtype, String starttime, String endtime) {

		try {
			Huitongtx httx = huitongtxService.getHuitongtx(B2cEnum.Huitongtx.getKey());

			int loopcount = 0;
			while (true) {
				loopcount++;
				if (loopcount > httx.getAddressMaxCount()) {
					logger.warn("当前查询快行线站点匹配次数已超出{}次,return", loopcount);
					return;
				}
				List<HttxEditBranch> httxBranchList = httxEditBranchDAO.getHttxEditBranchSendList(httx.getAddressMaxCount(), matchtype, starttime, endtime);
				if (httxBranchList == null || httxBranchList.size() == 0) {
					logger.info("当前没有需要回传快行线站点匹配结果的数据,循环次数={}", loopcount);
					return;
				}

				List<MatchResultData> sendjsonlist = new ArrayList<MatchResultData>();
				for (HttxEditBranch httxb : httxBranchList) {
					MatchResultData matchresult = new MatchResultData();

					matchresult.setTaskcode(httxb.getTaskcode());
					matchresult.setMatch_status(getMatchStatus(httxb));
					String branchcode = getBranchList().get(httxb.getBranchid()) == null ? "" : getBranchList().get(httxb.getBranchid()).getBranchcode();
					matchresult.setStation_code(branchcode);
					matchresult.setStation_name(httxb.getMatchbranch());
					matchresult.setRemark("");
					sendjsonlist.add(matchresult);
				}
				String jsoncontent = JacksonMapper.getInstance().writeValueAsString(sendjsonlist);// bean转化为json

				Map<String, String> params = requestParams(httx, jsoncontent); // 拼接请求参数

				String responseJson = RestHttpServiceHanlder.sendHttptoServer(params, httx.getAddressSender_url()); // 请求快行线接口并返回

				logger.info("站点匹配-当前快行线返回responseJson={}", responseJson);
				if (responseJson == null || responseJson.isEmpty()) {
					logger.info("站点匹配-当前快行线返回为空,跳出");
					return;
				}

				MatchResponse matchBean = JacksonMapper.getInstance().readValue(responseJson, MatchResponse.class);

				if (!"0".equals(matchBean.getCode())) {
					logger.error("站点匹配-当前快行线返回异常return");
					return;
				}
				for (MatchData data : matchBean.getMatchData()) {
					String taskcode = data.getTaskcode();
					String status = data.getStatus(); // 推送结果 1.成功 ，0失败
					String message = data.getMessage();
					long sendflag = "1".equals(status) ? System.currentTimeMillis() : 2;
					httxEditBranchDAO.updateSendStaus(taskcode, message, DateTimeUtil.getNowTime(), sendflag);
				}

			}

		} catch (Exception e) {
			logger.error("手动回传匹配结果异常", e);
		}

	}

	private Map<String, String> requestParams(Huitongtx httx, String jsoncontent) {
		Map<String, String> params = buildParmsMap("ips2.api.station", DateTimeUtil.getNowTime(), httx.getApp_key(), httx.getPrivate_key(), jsoncontent);

		String sign_str = httx.getPrivate_key() + HuitongtxConfig.createLinkString(params) + httx.getPrivate_key();
		String sign = MD5Util.md5(sign_str).toUpperCase();
		params.put("sign", sign);
		return params;
	}

	public static Map<String, String> buildParmsMap(String method, String timestamp, String app_key, String app_secret, String data) {
		Map<String, String> parmsMap = new HashMap<String, String>();
		parmsMap.put("method", method);
		parmsMap.put("timestamp", timestamp);
		parmsMap.put("app_key", app_key);
		parmsMap.put("data", data);

		return parmsMap;
	}

	// public static void main(String[] args) {
	//
	// Map<String, String> parmsMap = new HashMap<String, String>();
	// parmsMap.put("method", "ips2.api.station");
	// parmsMap.put("timestamp", "2014-04-16 14:37:20");
	// parmsMap.put("app_key", "bjzzyt_api");
	// parmsMap.put("data",
	// "[{\"taskcode\":\"10294\",\"match_status\":\"2\",\"station_code\":\"003\",\"station_name\":\"红庙\",\"remark\":\"\"}]");
	// String private_key="e14d5d564f1d1be4728655f9a72ab2a8";
	//
	// String
	// sign_str=private_key+HuitongtxConfig.createLinkString(parmsMap)+private_key;
	//
	// System.out.println(sign_str);
	//
	// }

	private String getMatchStatus(HttxEditBranch httxb) {
		String match_status = "";
		int matchtype = httxb.getMatchtype();
		if (matchtype == MatchTypeEnum.DiZhiKu.getValue()) {
			match_status = "1";
		} else if (matchtype == MatchTypeEnum.RenGong.getValue()) {
			match_status = "2";
		} else if (matchtype == MatchTypeEnum.WeiPiPei.getValue()) {
			match_status = "3";
		}
		return match_status;
	}

	private Map<Long, Branch> getBranchList() {
		Map<Long, Branch> branchMap = new HashMap<Long, Branch>();

		List<Branch> branchlist = branchDAO.getAllBranches();

		for (Branch branch : branchlist) {
			branchMap.put(branch.getBranchid(), branch);
		}

		return branchMap;

	}

}
