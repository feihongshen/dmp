package cn.explink.b2c.mmb;

import java.util.List;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.B2CData;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.RestHttpServiceHanlder;

@Service
public class MmbService {

	@Autowired
	private GetDmpDAO getdmpDAO;
	@Autowired
	private B2cTools b2ctools;
	@Autowired
	B2CDataDAO b2cDataDAO;

	private Logger logger = LoggerFactory.getLogger(MmbService.class);

	public Mmb getMmb(int key) {
		Mmb mmb = null;
		if (this.getObjectMethod(key) != null) {
			JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
			mmb = (Mmb) JSONObject.toBean(jsonObj, Mmb.class);
		} else {
			mmb = new Mmb();
		}
		return mmb;
	}

	/**
	 * 获取买卖宝配置信息的接口
	 * 
	 * @param key
	 * @return
	 */
	private String getObjectMethod(int key) {
		try {
			JointEntity obj = getdmpDAO.getJointEntity(key);
			return obj.getJoint_property();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取反馈状态
	 * 
	 * @return
	 */
	public MmbFlowEnum getFlowStateBymmb(long flowordertype, long deliverystate) {
		for (MmbFlowEnum e : MmbFlowEnum.values()) {
			if (flowordertype != FlowOrderTypeEnum.YiShenHe.getValue() && e.getFlowtype() == flowordertype) {
				return e;
			}
		}
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()) {
			if (deliverystate == DeliveryStateEnum.PeiSongChengGong.getValue() || deliverystate == DeliveryStateEnum.ShangMenHuanChengGong.getValue()
					|| deliverystate == DeliveryStateEnum.BuFenTuiHuo.getValue()) {
				return MmbFlowEnum.QianShou;
			}
			if (deliverystate == DeliveryStateEnum.QuanBuTuiHuo.getValue() || deliverystate == DeliveryStateEnum.BuFenTuiHuo.getValue() || deliverystate == DeliveryStateEnum.ShangMenJuTui.getValue()) {
				return MmbFlowEnum.JuShou;
			}
		}

		return null;
	}

	/**
	 * 反馈[买卖宝]订单信息
	 */
	public void feedback_status() {

		if (!b2ctools.isB2cOpen(B2cEnum.MaiMaiBao.getKey())) {
			logger.info("未开0买卖宝0的对接!");
			return;
		}
		Mmb mmb = getMmb(B2cEnum.MaiMaiBao.getKey());
		sendCwbStatus_To_mmb(mmb);

	}

	/**
	 * 状态反馈接口开始
	 * 
	 * @param smile
	 */
	private void sendCwbStatus_To_mmb(Mmb mmb) {

		try {

			int i = 0;
			while (true) {
				List<B2CData> datalist = b2cDataDAO.getDataListByFlowStatus(mmb.getCustomerid(), mmb.getMaxCount());
				i++;
				if (i > 100) {
					logger.warn("查询0买卖宝0状态反馈已经超过100次循环，可能存在程序未知异常,请及时查询并处理!");
					return;
				}

				if (datalist == null || datalist.size() == 0) {
					logger.info("当前没有要推送0买卖宝0的数据");
					return;
				}
				DealWithBuildXMLAndSending(mmb, datalist);

			}

		} catch (Exception e) {
			logger.error("发送0买卖宝0状态反馈遇到不可预知的异常", e);
		}

	}

	private void DealWithBuildXMLAndSending(Mmb mmb, List<B2CData> datalist) throws Exception {

		for (B2CData b2cdata : datalist) {
			String jsoncontent = b2cdata.getJsoncontent();

			// MmbJSONNote
			// mmbJSONNote=JacksonMapper.getInstance().readValue(jsoncontent,
			// MmbJSONNote.class);

			// jsoncontent=JacksonMapper.getInstance().writeValueAsString(mmbJSONNote);

			String responseInfo = RestHttpServiceHanlder.sendHttptoServer(jsoncontent, mmb.getSend_url());

			logger.info("状态反馈0买卖宝0[返回信息]-XML={}", responseInfo);

			int state = responseInfo.equalsIgnoreCase("OK") ? 1 : 2;
			String remark = responseInfo.length() > 50 ? responseInfo.substring(0, 50) : responseInfo;
			b2cDataDAO.updateFlagAndRemarkByCwb(b2cdata.getB2cid(), state, remark);

		}

	}

}
