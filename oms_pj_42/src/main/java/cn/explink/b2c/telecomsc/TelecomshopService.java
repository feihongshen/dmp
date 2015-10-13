package cn.explink.b2c.telecomsc;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.JacksonMapper;
import cn.explink.dao.CustomerDAO;
import cn.explink.domain.B2CData;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.EncryptData;

@Service
public class TelecomshopService {

	private Logger logger = LoggerFactory.getLogger(TelecomshopService.class);

	@Autowired
	private B2cTools b2ctools;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	B2CDataDAO b2cDataDAO;

	protected static ObjectMapper jacksonmapper = JacksonMapper.getInstance();

	// 获取配置信息
	public Telecomshop getTelecomshop(int key) {
		Telecomshop telcom = null;
		String objectMethod = b2ctools.getObjectMethod(key).getJoint_property();
		if (objectMethod != null) {
			JSONObject jsonObj = JSONObject.fromObject(objectMethod);
			telcom = (Telecomshop) JSONObject.toBean(jsonObj, Telecomshop.class);
		} else {
			telcom = new Telecomshop();
		}
		return telcom;
	}

	public long feedback_status() {
		long calcCount = 0;

		if (!b2ctools.isB2cOpen(B2cEnum.Telecomshop.getKey())) {
			logger.info("未开0电信商城0的对接!");
			return -1;
		}
		Telecomshop telecom = this.getTelecomshop(B2cEnum.Telecomshop.getKey());

		calcCount += sendCwbStatus_To_telecom(telecom, FlowOrderTypeEnum.RuKu.getValue());
		calcCount += sendCwbStatus_To_telecom(telecom, FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
		calcCount += sendCwbStatus_To_telecom(telecom, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue());
		calcCount += sendCwbStatus_To_telecom(telecom, FlowOrderTypeEnum.FenZhanLingHuo.getValue());
		calcCount += sendCwbStatus_To_telecom(telecom, FlowOrderTypeEnum.YiFanKui.getValue());
		calcCount += sendCwbStatus_To_telecom(telecom, FlowOrderTypeEnum.YiShenHe.getValue());

		updateSendFaildInfo(telecom.getCustomerid());
		return calcCount;
	}

	/**
	 * 状态反馈接口开始
	 * 
	 * @param smile
	 */
	private long sendCwbStatus_To_telecom(Telecomshop com, long flowordertype) {
		long calcCount = 0;
		try {

			int i = 0;
			while (true) {
				List<B2CData> datalist = b2cDataDAO.getDataListByFlowStatus(flowordertype, com.getCustomerid(), com.getMaxCount());
				i++;
				if (i > 30) {
					logger.warn("查询0电信商城0状态反馈已经超过30次循环，可能存在程序未知异常,请及时查询并处理!");
					return 0;
				}

				if (datalist == null || datalist.size() == 0) {
					logger.info("当前没有要推送0电信商城0的数据");
					return 0;
				}

				for (B2CData data : datalist) {
					String JsonContent = data.getJsoncontent();
					Map<String, String> paramsMap = JacksonMapper.getInstance().readValue(JsonContent, Map.class);
					String response = postHTTPJsonDataToTelecom(paramsMap, com.getSender_url());
					if (response.contains("000000")) {
						b2cDataDAO.updateB2cIdSQLResponseStatus(data.getB2cid(), 1, "");
					} else {
						b2cDataDAO.updateB2cIdSQLResponseStatus(data.getB2cid(), 2, response);
					}

				}
				calcCount += datalist.size();

			}

		} catch (Exception e) {

			logger.error("发送0电信商城0状态反馈遇到不可预知的异常", e);
		}

		return calcCount;
	}

	/**
	 * 定时重发 重发2天前的失败数据
	 */
	private void updateSendFaildInfo(String customerids) {
		try {
			String time = DateTimeUtil.getDateBefore(2);

			b2cDataDAO.updateKeyWordByVipShop(customerids, time, null);

		} catch (Exception e) {
			logger.error("重发电信商城异常", e);
			e.printStackTrace();
		}
	}

	public String postHTTPJsonDataToTelecom(Map<String, String> parmsMap, String post_url) throws Exception {

		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(post_url);
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");

		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(30000);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(30000);
		postMethod.getParams().setSoTimeout(30 * 1000);

		List<String> keys = new ArrayList<String>(parmsMap.keySet());

		// logger.info("请求电信商城URL={}",post_url);

		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = "";
			if (key.equalsIgnoreCase("SIGN") || key.equalsIgnoreCase("SIGNTYPE")) {
				value = parmsMap.get(key);
			} else {
				value = EncryptData.encrypt(parmsMap.get(key));
			}

			postMethod.addParameter(new NameValuePair(key, value));
			// logger.info("key={},value={}",key,value);
		}

		// 将表单的值放入postMethod中
		httpClient.executeMethod(postMethod); // post数据

		String str = postMethod.getResponseBodyAsString();
		postMethod.releaseConnection();

		return str;
	}

	public String getFlowEnum(long flowordertype, long deliverystate, String cwbordertypeid) {

		for (TelecomFlowEnum em : TelecomFlowEnum.values()) {
			if (em.getIsResultFlag() == 0) {
				if (flowordertype == em.getOnwer_code()) {
					return em.getB2cCode();
				}
			}
		}

		if (flowordertype == FlowOrderTypeEnum.YiFanKui.getValue()) {
			if (deliverystate == DeliveryStateEnum.PeiSongChengGong.getValue() || deliverystate == DeliveryStateEnum.ShangMenHuanChengGong.getValue()
					|| deliverystate == DeliveryStateEnum.ShangMenTuiChengGong.getValue()) {
				return TelecomFlowEnum.ChengGong.getB2cCode();
			} else if (deliverystate == DeliveryStateEnum.QuanBuTuiHuo.getValue() || deliverystate == DeliveryStateEnum.BuFenTuiHuo.getValue()) {
				return TelecomFlowEnum.JuShou.getB2cCode();
			}

		} else if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()) {
			if (deliverystate == DeliveryStateEnum.PeiSongChengGong.getValue() || deliverystate == DeliveryStateEnum.ShangMenHuanChengGong.getValue()
					|| deliverystate == DeliveryStateEnum.ShangMenTuiChengGong.getValue()) {
				return TelecomFlowEnum.ChengGong_shenhe.getB2cCode();
			} else if (deliverystate == DeliveryStateEnum.QuanBuTuiHuo.getValue() || deliverystate == DeliveryStateEnum.BuFenTuiHuo.getValue()) {
				return TelecomFlowEnum.JuShou_shenhe.getB2cCode();
			}
		}
		return null;

	}

	/**
	 * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	 * 
	 * @param params
	 *            需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 * @throws UnsupportedEncodingException
	 */
	public static String createLinkString(LinkedHashMap<String, String> params, boolean sortflag) throws UnsupportedEncodingException {

		List<String> keys = new ArrayList<String>(params.keySet());
		if (sortflag) {
			Collections.sort(keys);
		}

		String prestr = "";

		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);

			if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
				prestr = prestr + key + "=" + URLEncoder.encode(value, "UTF-8");
			} else {
				prestr = prestr + key + "=" + URLEncoder.encode(value, "UTF-8") + "&";
			}
		}

		return prestr;
	}

}
