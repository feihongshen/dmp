package cn.explink.b2c.sfxhm;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.JacksonMapper;
import cn.explink.dao.CustomerDAO;
import cn.explink.dbPool.DBOperator;
import cn.explink.domain.B2CData;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;

@Service
public class SfxhmService {

	private Logger logger = LoggerFactory.getLogger(SfxhmService.class);

	@Autowired
	private B2cTools b2ctools;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	B2CDataDAO b2cDataDAO;

	// 获取配置信息
	public Sfxhm getSfxhm(int key) {
		Sfxhm lt = null;
		String objectMethod = b2ctools.getObjectMethod(key).getJoint_property();
		if (objectMethod != null) {
			JSONObject jsonObj = JSONObject.fromObject(objectMethod);
			lt = (Sfxhm) JSONObject.toBean(jsonObj, Sfxhm.class);
		} else {
			lt = new Sfxhm();
		}
		return lt;
	}

	public String getFlowEnum(long flowordertype, long deliverystate) {

		for (SfxhmTrackEnum em : SfxhmTrackEnum.values()) {
			if (em.getFlowordertype() != FlowOrderTypeEnum.YiShenHe.getValue()) {
				if (flowordertype == em.getFlowordertype()) {
					return em.getState();
				}
			}
		}

		// 滞留
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
			return SfxhmTrackEnum.ZhiLiu.getState();
		}

		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()
				&& (deliverystate == DeliveryStateEnum.PeiSongChengGong.getValue() || deliverystate == DeliveryStateEnum.ShangMenTuiChengGong.getValue() || deliverystate == DeliveryStateEnum.ShangMenHuanChengGong
						.getValue())) {
			return SfxhmTrackEnum.QianShou.getState();
		}
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()
				&& (deliverystate == DeliveryStateEnum.BuFenTuiHuo.getValue() || deliverystate == DeliveryStateEnum.QuanBuTuiHuo.getValue() || deliverystate == DeliveryStateEnum.ShangMenJuTui
						.getValue())) {
			return null;
		}

		return null;

	}

	/**
	 * 反馈[顺丰-小红帽]订单信息
	 */
	public void feedback_status() {

		if (!b2ctools.isB2cOpen(B2cEnum.SFexpressXHM.getKey())) {
			logger.info("未开0顺丰-小红帽0的对接!");
			return;
		}
		Sfxhm sf = getSfxhm(B2cEnum.SFexpressXHM.getKey());
		sendCwbStatus_To_wx(sf);

	}

	/**
	 * 状态反馈接口开始
	 * 
	 * @param smile
	 */
	private void sendCwbStatus_To_wx(Sfxhm xm) {

		try {

			int i = 0;
			while (true) {
				List<B2CData> datalist = b2cDataDAO.getDataListByFlowStatus(xm.getCustomerid(), xm.getMaxCount());
				i++;
				if (i > 100) {
					logger.warn("查询0顺丰-小红帽0状态反馈已经超过100次循环，可能存在程序未知异常,请及时查询并处理!");
					return;
				}

				if (datalist == null || datalist.size() == 0) {
					logger.info("当前没有要推送0顺丰-小红帽0的数据");
					return;
				}

				for (B2CData data : datalist) {

					logger.info("当前小红帽推送-顺丰存储过程信息={}", data.getJsoncontent());

					SfxhmNote note = getSfxhmBean(data.getJsoncontent());
					String result = buildParamsInvokeRemoteServer(note);

					logger.info("当前顺丰cwb={}返回={},flowordertype=" + data.getFlowordertype(), data.getCwb(), result);

					b2cDataDAO.updateB2cIdSQLResponseStatus(data.getB2cid(), Integer.valueOf(result), "1".equals(result) ? "成功" : "失败");

				}

			}

		} catch (Exception e) {
			logger.error("发送0顺丰-小红帽0状态反馈遇到不可预知的异常", e);
		}

	}

	public SfxhmNote getSfxhmBean(String jsoncontent) throws JsonParseException, JsonMappingException, IOException {
		return JacksonMapper.getInstance().readValue(jsoncontent, SfxhmNote.class);
	}

	/**
	 * 调用远程数据库的存储过程下载订单
	 * 
	 * @param downloadCount
	 */
	public String buildParamsInvokeRemoteServer(SfxhmNote note) {
		DBOperator db = new DBOperator("expressurl#0");
		String callsql = "p_upload_route_param(?,?,?,?,?,?,?,?,?)";
		Object para[] = { "int", note.getScan_type(), "in", "", "string", note.getMailno(), "in", "", "string", note.getScan_time(), "in", "", "string", note.getUpload_time(), "in", "", "int", "1",
				"in", "", "int", "1", "in", "", "int", "1", "in", "", "string", note.getNote(), "in", "", "int", "", "out", "result" };
		Map<String, Object> map = db.executeProcedure(callsql, para);

		List<Map<String, String>> retlist = (List<Map<String, String>>) map.get("return_para_list");

		return retlist.get(0).get("result");

	}

}
