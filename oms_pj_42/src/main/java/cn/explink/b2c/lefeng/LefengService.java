package cn.explink.b2c.lefeng;

import java.io.IOException;
import java.util.List;

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
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.B2CData;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.RestHttpServiceHanlder;

@Service
public class LefengService {
	@Autowired
	private GetDmpDAO getdmpDAO;
	@Autowired
	private B2cTools b2ctools;
	@Autowired
	B2CDataDAO b2cDataDAO;

	private Logger logger = LoggerFactory.getLogger(LefengService.class);

	public Lefeng getLefeng(int key) {
		Lefeng lefeng = null;
		if (this.getObjectMethod(key) != null) {
			JSONObject jsonObj = JSONObject.fromObject(this.getObjectMethod(key));
			lefeng = (Lefeng) JSONObject.toBean(jsonObj, Lefeng.class);
		} else {
			lefeng = new Lefeng();
		}
		return lefeng;
	}

	/**
	 * 获取反库状态
	 */
	public LefengFlowEnum filterFlowState(long flowordertype, long deliverystate, int cwbordertype) {
		for (LefengFlowEnum e : LefengFlowEnum.values()) {
			if ((flowordertype != FlowOrderTypeEnum.YiShenHe.getValue()) && (e.getFlowtype() == flowordertype)) {
				return e;
			}
		}
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()) {
			if ((deliverystate == DeliveryStateEnum.PeiSongChengGong.getValue()) || (deliverystate == DeliveryStateEnum.ShangMenHuanChengGong.getValue())
					|| (deliverystate == DeliveryStateEnum.ShangMenTuiChengGong.getValue())) {
				return LefengFlowEnum.Received;
			}
			if ((deliverystate == DeliveryStateEnum.QuanBuTuiHuo.getValue()) || (deliverystate == DeliveryStateEnum.BuFenTuiHuo.getValue())
					|| (deliverystate == DeliveryStateEnum.ShangMenJuTui.getValue())) {
				return LefengFlowEnum.DeliveryFailed;
			}
			if (deliverystate == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
				return LefengFlowEnum.DeliveryException;
			}
			if (deliverystate == DeliveryStateEnum.HuoWuDiuShi.getValue()) {
				return LefengFlowEnum.DeliveryException;
			}
		}
		return null;
	}

	/**
	 * 获取乐峰网配置信息的接口
	 *
	 * @param key
	 * @return
	 */
	private String getObjectMethod(int key) {
		try {
			JointEntity obj = this.getdmpDAO.getJointEntity(key);
			return obj.getJoint_property();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public void feedback_status() {

		Lefeng lefeng = this.getLefeng(B2cEnum.Lefeng.getKey());
		if (!this.b2ctools.isB2cOpen(B2cEnum.Lefeng.getKey())) {
			this.logger.info("未开0乐峰0的对接!");
			return;
		}

		this.sendCwbStatus_To_lefeng(lefeng, FlowOrderTypeEnum.RuKu.getValue());
		this.sendCwbStatus_To_lefeng(lefeng, FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
		this.sendCwbStatus_To_lefeng(lefeng, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue());
		this.sendCwbStatus_To_lefeng(lefeng, FlowOrderTypeEnum.FenZhanLingHuo.getValue());
		this.sendCwbStatus_To_lefeng(lefeng, FlowOrderTypeEnum.YiShenHe.getValue());

	}

	/**
	 * 状态反馈接口开始
	 *
	 * @param smile
	 */
	private void sendCwbStatus_To_lefeng(Lefeng lefeng, long flowordertype) {
		try {

			int i = 0;
			while (true) {
				List<B2CData> lefengDataList = this.b2cDataDAO.getDataListByFlowStatus(flowordertype, lefeng.getCustomerids(), lefeng.getSearch_number());
				i++;
				if (i > 100) {
					String warning = "查询0乐蜂网0状态反馈已经超过100次循环，可能存在程序未知异常,请及时查询并处理!";
					this.logger.warn(warning);
					return;
				}

				if ((lefengDataList == null) || (lefengDataList.size() == 0)) {
					this.logger.info("当前没有要推送0乐峰网0的数据");
					return;
				}
				this.DealWithBuildXMLAndSending(lefeng, lefengDataList);

			}

		} catch (Exception e) {
			String errorinfo = "发送0乐蜂网0状态反馈遇到不可预知的异常";
			this.logger.error(errorinfo, e);
		}

	}

	public LefengXmlNote getXMLNoteMethod(String jsoncontent) throws JsonParseException, JsonMappingException, IOException {
		return JacksonMapper.getInstance().readValue(jsoncontent, LefengXmlNote.class);
	}

	public ReturnData getReturnData(String jsonString) throws JsonParseException, JsonMappingException, IOException {
		return JacksonMapper.getInstance().readValue(jsonString, ReturnData.class);
	}

	private void DealWithBuildXMLAndSending(Lefeng lefeng, List<B2CData> datalist) throws Exception {
		// <?xml version="1.0" encoding="UTF-8"?>
		StringBuffer sub = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sub.append("<lefeng xmlns=\"http://vo.ws.ordertracer.lefeng.com\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:vo=\"http://vo.ws.ordertracer.lefeng.com\">");
		sub.append("<agent id=\"" + lefeng.getAgentId() + "\">");
		sub.append("<name>" + lefeng.getAgentName() + "</name>");
		sub.append("<phone>" + lefeng.getAgentPhone() + "</phone>");
		sub.append("<website>" + lefeng.getAgentWebsite() + "</website>");
		sub.append("</agent>");
		// 推送订单状态的数量
		long number = datalist.size();
		sub.append("<orders size=\"" + number + "\">");
		String b2cidsString = "";
		for (B2CData data : datalist) {
			String jsoncontent = data.getJsoncontent();
			LefengXmlNote note = this.getXMLNoteMethod(jsoncontent);
			b2cidsString += data.getB2cid() + ",";
			sub.append("<vo:order>");
			sub.append("<vo:sponsorId>" + data.getCwb() + "</vo:sponsorId>");
			sub.append("<orderNum>" + note.getTranscwb() + "</orderNum>");
			sub.append("<routes>");
			sub.append("<route code=\"" + note.getCode() + "\">");
			if (data.getFlowordertype() != 9) {
				sub.append("<courier>" + note.getUserName() + "</courier>");

			} else {
				sub.append("<courier>" + note.getUserName() + "-" + note.getPhone() + "</courier>");

			}
			sub.append("<time>" + note.getTime() + "</time>");
			sub.append("<state>" + note.getState() + "</state>");
			sub.append("</route>");
			sub.append("</routes>");
			sub.append("</vo:order>");
		}
		sub.append("</orders>");
		sub.append("</lefeng>");

		this.logger.info("生成符合乐蜂网的xml数据：{}", sub.toString());
		b2cidsString = b2cidsString.length() > 0 ? b2cidsString.substring(0, b2cidsString.length() - 1) : b2cidsString;

		String responseString = RestHttpServiceHanlder.sendHttptoServer(sub.toString(), lefeng.getSearch_url());
		if (responseString.isEmpty()) {
			this.logger.warn("请求0乐蜂网0返回xml为空，跳出循环,throw Exception,xml={}", responseString);
			return;
		}
		this.logger.info("状态反馈0乐峰网0[返回信息]-XML={}", responseString);

		ReturnData returnData = this.getReturnData(responseString);
		if (returnData == null) {
			this.logger.warn("请求0乐蜂网0解析xml为空，跳出循环,throw Exception,xml={}", responseString);
			return;

		}
		if (returnData.getCode().equals("1")) {
			this.b2cDataDAO.updateMultiB2cIdSQLResponseStatus_AllSuccess(b2cidsString);
			this.logger.info("返回失败消息：xml={}" + returnData.getMessage());

		} else if (returnData.getCode().equals("0")) {
			this.b2cDataDAO.updateMultiB2cIdSQLResponseStatus_AllFaild(b2cidsString);
			this.logger.info("返回成功消息：xml={}" + returnData.getMessage());

		}

		// 发送给dmp
		// flowFromJMSB2cService.sendTodmp(b2cids);
	}
}
