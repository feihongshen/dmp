package cn.explink.b2c.yonghuics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.mmb.MmbJSONNote;
import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.B2cDataOrderFlowDetail;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.FlowFromJMSB2cService;
import cn.explink.b2c.tools.HttpClienCommon;
import cn.explink.b2c.tools.JacksonMapper;
import cn.explink.b2c.yihaodian.RestTemplateClient;
import cn.explink.b2c.yihaodian.xmldto.ReturnDto;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.B2CData;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.RestHttpServiceHanlder;

@Service
public class YonghuiService {
	private Logger logger = LoggerFactory.getLogger(YonghuiService.class);
	@Autowired
	RestTemplateClient restTemplate;
	@Autowired
	B2cTools b2ctools;
	@Autowired
	B2CDataDAO b2cDataDAO;
	@Autowired
	GetDmpDAO getDmpdao;
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;
	@Autowired
	FlowFromJMSB2cService flowFromJMSB2cService;

	/**
	 * 获取永辉超市配置信息
	 * 
	 * @param key
	 * @return
	 */
	public Yonghui getYihaodianSettingMethod(int key) {
		Yonghui yihaodian = null;
		String objectMethod = b2ctools.getObjectMethod(key).getJoint_property();
		if (objectMethod != null) {
			JSONObject jsonObj = JSONObject.fromObject(objectMethod);
			yihaodian = (Yonghui) JSONObject.toBean(jsonObj, Yonghui.class);
		} else {
			yihaodian = new Yonghui();
		}
		return yihaodian;
	}

	/**
	 * 获取Yihaodian XML Note
	 * 
	 * @param jsoncontent
	 * @return
	 */
	public YonghuiJSONnote getYihaodianXMLNoteMethod(String jsoncontent) {
		try {
			JSONObject jsonObj = JSONObject.fromObject(jsoncontent);
			return (YonghuiJSONnote) JSONObject.toBean(jsonObj, YonghuiJSONnote.class);
		} catch (Exception e) {
			logger.error("获取YihaodianXMLNote异常！jsoncontent:" + jsoncontent + e);
			return null;
		}
	}

	/**
	 * 永辉超市定时器的总开关方法 -投递结果反馈
	 */
	public void YiHaoDianInterfaceInvoke(int yhd_key) {
		long calcCount = 0;
		if (!b2ctools.isB2cOpen(yhd_key)) {
			logger.info("未开启[永辉超市]对接,yhd_key={}", yhd_key);
			return;
		}
		// DeliveryResultByYiHaoDian(yhd_key);

	}

	public YonghuiFlowEnum getYonghuiFlowEnum(long flowordertype, long delivery_state) {
		if (flowordertype != FlowOrderTypeEnum.YiShenHe.getValue()) {
			for (YonghuiFlowEnum TEnum : YonghuiFlowEnum.values()) {
				if (flowordertype == TEnum.getFlowordertype()) {
					return TEnum;
				}
			}
		}
		if (delivery_state == DeliveryStateEnum.PeiSongChengGong.getValue() || delivery_state == DeliveryStateEnum.ShangMenHuanChengGong.getValue()
				|| delivery_state == DeliveryStateEnum.ShangMenTuiChengGong.getValue()) {
			return YonghuiFlowEnum.PeiSongChengGong;
		}
		if (delivery_state == DeliveryStateEnum.QuanBuTuiHuo.getValue() || delivery_state == DeliveryStateEnum.BuFenTuiHuo.getValue()) {
			return YonghuiFlowEnum.JuShou;
		}

		if (delivery_state == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
			return YonghuiFlowEnum.ZhiLiu;
		}

		return null;

	}

	/**
	 * 反馈[永辉超市]订单信息
	 */
	public void feedback_status() {

		if (!b2ctools.isB2cOpen(B2cEnum.YongHuics.getKey())) {
			logger.info("未开0永辉超市0的对接!");
			return;
		}
		Yonghui yh = getYihaodianSettingMethod(B2cEnum.YongHuics.getKey());
		sendCwbStatus_To_YongHui(yh);

	}

	/**
	 * 状态反馈接口开始
	 * 
	 * @param smile
	 */
	private void sendCwbStatus_To_YongHui(Yonghui yh) {

		try {

			int i = 0;
			while (true) {
				List<B2CData> datalist = b2cDataDAO.getDataListByFlowStatus(yh.getCustomerids(), yh.getCallBackCount());
				i++;
				if (i > 100) {
					logger.warn("查询0永辉超市0状态反馈已经超过100次循环，可能存在程序未知异常,请及时查询并处理!");
					return;
				}

				if (datalist == null || datalist.size() == 0) {
					logger.info("当前没有要推送0永辉超市0的数据");
					return;
				}
				DealWithBuildXMLAndSending(yh, datalist);

			}

		} catch (Exception e) {
			logger.error("发送0永辉超市0状态反馈遇到不可预知的异常", e);
		}

	}

	private void DealWithBuildXMLAndSending(Yonghui yh, List<B2CData> datalist) throws Exception {

		for (B2CData b2cdata : datalist) {
			String jsoncontent = b2cdata.getJsoncontent();

			YonghuiJSONnote yonghuiJSONnote = JacksonMapper.getInstance().readValue(jsoncontent, YonghuiJSONnote.class);
			yonghuiJSONnote.setBagno(b2cdata.getShipcwb());

			jsoncontent = JacksonMapper.getInstance().writeValueAsString(yonghuiJSONnote);

			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("content", jsoncontent);

			// String
			// responseInfo=RestHttpServiceHanlder.sendHttptoServer(paramsMap,yh.getTrackLog_URL());

			String responseInfo = HttpClienCommon.post(paramsMap, null, yh.getTrackLog_URL(), 5000, 5000, "utf-8");

			logger.info("状态反馈0永辉超市0[返回信息]-XML={}", responseInfo);

			ReturnDto returnDto = JacksonMapper.getInstance().readValue(responseInfo, ReturnDto.class);
			String remark = "00".equals(returnDto.getErrCode()) ? "成功" : (returnDto.getErrCode() + returnDto.getErrMsg());
			int sendflag = "00".equals(returnDto.getErrCode()) ? 1 : 2;
			b2cDataDAO.updateFlagAndRemarkByCwb(b2cdata.getB2cid(), sendflag, remark);

		}

	}

}
