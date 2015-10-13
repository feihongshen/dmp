package cn.explink.b2c.zhongliang;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.JacksonMapper;
import cn.explink.b2c.zhongliang.xml.Request_UpdateStatus;
import cn.explink.b2c.zhongliang.xml.Request_orderStatus;
import cn.explink.dao.CustomerDAO;
import cn.explink.domain.B2CData;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.RestHttpServiceHanlder;

@Service
public class ZhongliangService {

	@Autowired
	private B2cTools b2ctools;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	B2CDataDAO b2cDataDAO;

	protected static ObjectMapper jacksonmapper = JacksonMapper.getInstance();
	private Logger logger = LoggerFactory.getLogger(ZhongliangService.class);

	public void builderOrder() {

	}

	public Zhongliang getZhongliang(int key) {
		Zhongliang zl = null;
		String objectMethod = b2ctools.getObjectMethod(key).getJoint_property();
		if (objectMethod != null) {
			JSONObject jsonObj = JSONObject.fromObject(objectMethod);
			zl = (Zhongliang) JSONObject.toBean(jsonObj, Zhongliang.class);
		} else {
			zl = new Zhongliang();
		}
		return zl;
	}

	public String getFlowEnum(long flowordertype, long deliverystate) {
		for (ZhongliangTrackEnum em : ZhongliangTrackEnum.values()) {
			if (em.getFlowordertype() != FlowOrderTypeEnum.YiShenHe.getValue()) {
				if (flowordertype == em.getFlowordertype()) {
					return em.getState();
				}
			}
		}

		// 导入数据
		if (flowordertype == FlowOrderTypeEnum.DaoRuShuJu.getValue() && (deliverystate == DeliveryStateEnum.WeiFanKui.getValue())) {
			return ZhongliangTrackEnum.Daorushuju.getState();
		}
		// 入库
		if (flowordertype == FlowOrderTypeEnum.RuKu.getValue() && (deliverystate == DeliveryStateEnum.WeiFanKui.getValue())) {
			return ZhongliangTrackEnum.RuKu.getState();
		}
		// 出库
		if (flowordertype == FlowOrderTypeEnum.ChuKuSaoMiao.getValue() && (deliverystate == DeliveryStateEnum.WeiFanKui.getValue())) {
			return ZhongliangTrackEnum.Chuku.getState();
		}
		// 到货
		if (flowordertype == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() && (deliverystate == DeliveryStateEnum.WeiFanKui.getValue())) {
			return ZhongliangTrackEnum.Daohuo.getState();
		}
		// 领货
		if (flowordertype == FlowOrderTypeEnum.FenZhanLingHuo.getValue() && (deliverystate == DeliveryStateEnum.WeiFanKui.getValue())) {
			return ZhongliangTrackEnum.Linhuo.getState();
		}
		// 拒收
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && (deliverystate == DeliveryStateEnum.QuanBuTuiHuo.getValue() || deliverystate == DeliveryStateEnum.BuFenTuiHuo.getValue())) {
			return ZhongliangTrackEnum.JuShou.getState();
		}
		// 签收
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()
				&& (deliverystate == DeliveryStateEnum.PeiSongChengGong.getValue() || deliverystate == DeliveryStateEnum.ShangMenTuiChengGong.getValue() || deliverystate == DeliveryStateEnum.ShangMenHuanChengGong
						.getValue())) {
			return ZhongliangTrackEnum.QianShou.getState();
		}

		return null;
	}

	/**
	 * 反馈[中粮]订单信息
	 */
	public void feedback_status() {

		if (!b2ctools.isB2cOpen(B2cEnum.Zhongliang.getKey())) {
			logger.info("未开中粮的对接!");
			return;
		}

		Zhongliang zl = getZhongliang(B2cEnum.Zhongliang.getKey());

		sendCwbStatus_To_wx(zl);

	}

	/**
	 * 状态反馈接口开始
	 * 
	 * @param smile
	 */
	private void sendCwbStatus_To_wx(Zhongliang zl) {

		try {

			int i = 0;
			while (true) {
				List<B2CData> datalist = b2cDataDAO.getDataListByFlowStatus(zl.getCustomerid(), Long.parseLong(zl.getNums()));
				i++;
				if (i > 100) {
					logger.warn("查询中粮状态反馈已经超过100次循环，可能存在程序未知异常,请及时查询并处理!");
					return;
				}

				if (datalist == null || datalist.size() == 0) {
					logger.info("当前没有要推送中粮的数据");
					return;
				}
				DealWithBuildXMLAndSending(zl, datalist);

			}

		} catch (Exception e) {
			logger.error("发送中粮状态反馈遇到不可预知的异常", e);
		}

	}

	public Request_orderStatus getXMLNoteMethod(String jsoncontent) throws JsonParseException, JsonMappingException, IOException {
		return new ObjectMapper().readValue(jsoncontent, Request_orderStatus.class);
	}

	private void DealWithBuildXMLAndSending(Zhongliang zl, List<B2CData> datalist) throws Exception {
		String cwb = null;
		for (B2CData data : datalist) {
			cwb = data.getCwb();
			try {
				String jsonconten = data.getJsoncontent();
				Request_orderStatus orderStatus = JacksonMapper.getInstance().readValue(jsonconten, Request_orderStatus.class);
				Request_UpdateStatus up = orderStatus.getResponse_body().getUpdateStatus();
				String Remark = up.getRemark() == null ? "" : up.getRemark();
				String Xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + "<Request service=\"OrderStatus\" lang=\"zh-CN\">" + "<Head>client</Head>" + "<Body>" + "<UpdateStatus>" + "<SendorderID>"
						+ up.getSendorderID() + "</SendorderID>" + "<InfoType>" + up.getInfoType() + "</InfoType>" + "<OrderStatus>" + up.getOrderStatus() + "</OrderStatus>" + "<ChangeTime>"
						+ up.getChangeTime() + "</ChangeTime>" + "<MailNO>" + up.getMailNo() + "</MailNO>" + "<Remark>" + Remark + "</Remark>" + "</UpdateStatus>" + "</Body>" + "</Request>";
				logger.info("中粮状态回传请求XML{}", Xml);
				Map<String, String> map = checkData(zl, "up");
				map.put("XML", Xml);
				RestHttpServiceHanlder.sendHttptoServer(map, zl.getOrderStatus_url());
				b2cDataDAO.updateB2cIdSQLResponseStatus(Long.valueOf(data.getB2cid()), 1, "");
			} catch (Exception e) {
				logger.error("中粮状态回传异常" + cwb, e);
			}
		}
	}

	public Map<String, String> checkData(Zhongliang zl, String RequestType) {
		String clientid = zl.getClientId();
		String verifyData = VerifyDataUtil.encryptData(clientid, zl.getClientFlag(), zl.getClientKey(), zl.getClientConst());
		Map<String, String> map = new HashMap<String, String>();
		map.put("clientid", clientid);
		map.put("verifyData", verifyData);
		map.put("RequestType", RequestType);
		return map;
	}

}
