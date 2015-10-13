package cn.explink.b2c.wanxiang;

import java.io.IOException;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.letv.LetvXMLNote;
import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.JacksonMapper;
import cn.explink.b2c.wanxiang.bean.WxResponse;
import cn.explink.b2c.wanxiang.bean.WxUnmarchal;
import cn.explink.dao.CustomerDAO;
import cn.explink.domain.B2CData;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.MD5.MD5Util;

@Service
public class WanxiangService {

	private Logger logger = LoggerFactory.getLogger(WanxiangService.class);

	@Autowired
	private B2cTools b2ctools;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	B2CDataDAO b2cDataDAO;

	protected static ObjectMapper jacksonmapper = JacksonMapper.getInstance();

	// 获取配置信息
	public Wanxiang getWanxiang(int key) {
		Wanxiang lt = null;
		String objectMethod = b2ctools.getObjectMethod(key).getJoint_property();
		if (objectMethod != null) {
			JSONObject jsonObj = JSONObject.fromObject(objectMethod);
			lt = (Wanxiang) JSONObject.toBean(jsonObj, Wanxiang.class);
		} else {
			lt = new Wanxiang();
		}
		return lt;
	}

	public String postHTTPJsonDataToWanXiang(String jsoncontent, String post_url) throws Exception {

		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(post_url);
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");

		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(20000);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(20000);
		// 填入各个表单域的值
		NameValuePair[] data = { new NameValuePair("JSONDATA", jsoncontent), };

		// 将表单的值放入postMethod中
		postMethod.setRequestBody(data);

		httpClient.executeMethod(postMethod); // post数据
		String str = postMethod.getResponseBodyAsString();
		postMethod.releaseConnection();

		return str;
	}

	public String postHTTPJsonDataToWanXiang_new(String user_name, String pass_word, String jsoncontent, String md5Data, String post_url) throws Exception {

		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(post_url);
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");

		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(20000);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(20000);
		// 填入各个表单域的值
		NameValuePair[] data = { new NameValuePair("USER_NAME", user_name), new NameValuePair("PASSWORD", pass_word), new NameValuePair("para_xml", jsoncontent),
				new NameValuePair("md5Data", md5Data), };

		// 将表单的值放入postMethod中
		postMethod.setRequestBody(data);

		httpClient.executeMethod(postMethod); // post数据
		String str = postMethod.getResponseBodyAsString();
		postMethod.releaseConnection();

		return str;
	}

	public String getFlowEnum(long flowordertype, long deliverystate, String cwbordertypeid) {

		for (WanxiangFlowEnum em : WanxiangFlowEnum.values()) {
			if (em.getIsResultFlag() == 0) {
				if (flowordertype == em.getOnwer_code()) {
					return em.getWx_code();
				}
			}
		}

		// 审核
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
			return WanxiangFlowEnum.FenZhanZhiLiu.getWx_code();
		}

		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()
				&& (deliverystate == DeliveryStateEnum.PeiSongChengGong.getValue() || deliverystate == DeliveryStateEnum.ShangMenTuiChengGong.getValue() || deliverystate == DeliveryStateEnum.ShangMenHuanChengGong
						.getValue())) {
			return WanxiangFlowEnum.PeiSongChengGong.getWx_code();
		}
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()
				&& (deliverystate == DeliveryStateEnum.BuFenTuiHuo.getValue() || deliverystate == DeliveryStateEnum.QuanBuTuiHuo.getValue() || deliverystate == DeliveryStateEnum.ShangMenJuTui
						.getValue())) {
			return WanxiangFlowEnum.JuShou.getWx_code();
		}

		return null;

	}

	public String getFlowEnumNew(long flowordertype, long deliverystate, String cwbordertypeid) {

		for (WanxiangNewFlowEnum em : WanxiangNewFlowEnum.values()) {
			if (em.getIsResultFlag() == 0) {
				if (flowordertype == em.getOnwer_code()) {
					return em.getWx_code();
				}
			}
		}

		// 审核
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
			return WanxiangNewFlowEnum.FenZhanZhiLiu.getWx_code();
		}
		// 配送成功
		if (flowordertype == FlowOrderTypeEnum.YiFanKui.getValue()
				&& (deliverystate == DeliveryStateEnum.PeiSongChengGong.getValue() || deliverystate == DeliveryStateEnum.ShangMenTuiChengGong.getValue())) {
			return WanxiangNewFlowEnum.PeiSongChengGong.getWx_code();
		}
		// 配送成功 审核
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()
				&& (deliverystate == DeliveryStateEnum.PeiSongChengGong.getValue() || deliverystate == DeliveryStateEnum.ShangMenTuiChengGong.getValue())) {
			return WanxiangNewFlowEnum.PeiSongChengGongConfrim.getWx_code();
		}
		// 上门换成功
		if (flowordertype == FlowOrderTypeEnum.YiFanKui.getValue() && (deliverystate == DeliveryStateEnum.ShangMenHuanChengGong.getValue())) {
			return WanxiangNewFlowEnum.ShangmenHuanChengGong.getWx_code();
		}

		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && (deliverystate == DeliveryStateEnum.QuanBuTuiHuo.getValue() || deliverystate == DeliveryStateEnum.ShangMenJuTui.getValue())) {
			// return WanxiangNewFlowEnum.JuShou.getWx_code();
		}
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.BuFenTuiHuo.getValue()) {
			// return WanxiangNewFlowEnum.BufenTuihuo.getWx_code();
		}

		return null;

	}

	/**
	 * 反馈[万象]订单信息
	 */
	public void feedback_status() {

		if (!b2ctools.isB2cOpen(B2cEnum.Wanxiang.getKey())) {
			logger.info("未开0万象0的对接!");
			return;
		}
		Wanxiang wx = getWanxiang(B2cEnum.Wanxiang.getKey());
		sendCwbStatus_To_wx(wx);

	}

	/**
	 * 状态反馈接口开始
	 * 
	 * @param smile
	 */
	private void sendCwbStatus_To_wx(Wanxiang wx) {

		try {

			int i = 0;
			while (true) {
				List<B2CData> datalist = b2cDataDAO.getDataListByFlowStatus(wx.getCustomerid(), wx.getMaxCount());
				i++;
				if (i > 100) {
					logger.warn("查询0万象0状态反馈已经超过100次循环，可能存在程序未知异常,请及时查询并处理!");
					return;
				}

				if (datalist == null || datalist.size() == 0) {
					logger.info("当前没有要推送0万象0的数据");
					return;
				}
				DealWithBuildXMLAndSending(wx, datalist);

			}

		} catch (Exception e) {
			logger.error("发送0万象0状态反馈遇到不可预知的异常", e);
		}

	}

	private void DealWithBuildXMLAndSending(Wanxiang wx, List<B2CData> datalist) throws Exception {

		for (B2CData data : datalist) {
			String jsoncontent = data.getJsoncontent();
			jsoncontent = jsoncontent.substring(1, jsoncontent.length() - 1);

			logger.info("当前[推送]0万象0状态={},USER_NAME={},PASSWORD={},jsoncontent={}", new Object[] { data.getFlowordertype(), wx.getUser_name(), wx.getPass_word(), jsoncontent });

			String md5Data = MD5Util.md5(jsoncontent + wx.getPrivate_key());
			String response = postHTTPJsonDataToWanXiang_new(wx.getUser_name(), wx.getPass_word(), jsoncontent, md5Data, wx.getUrl());

			logger.info("当前0万象0[返回]状态={},response={},cwb={}" + data.getCwb(), data.getFlowordertype(), response);

			WxResponse wxresp = WxUnmarchal.Unmarchal(response);
			String state_total = wxresp.getState();
			if (!"S0".equals(state_total)) {
				logger.error("万象推送系统错误,return.response=" + response);
				return;
			}

			int state = wxresp.getLog().getError_code().equalsIgnoreCase("B0") ? 1 : 2;
			String remark = wxresp.getLog().getError_msg();

			b2cDataDAO.updateFlagAndRemarkByCwb(data.getB2cid(), state, remark);

		}

	}

}
