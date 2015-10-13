package cn.explink.b2c.dpfoss;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.dpfoss.sign.UploadSignRequest;
import cn.explink.b2c.dpfoss.sign.UploadSignResponse;
import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.JacksonMapper;
import cn.explink.b2c.tools.Mail;
import cn.explink.domain.B2CData;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;

@Service
public class DpfossService {
	@Autowired
	private B2cTools b2ctools;
	@Autowired
	private B2CDataDAO b2CDataDAO;

	private Logger logger = LoggerFactory.getLogger(DpfossService.class);

	// 获取配置信息
	public Dpfoss getDpfossSettingMethod(int key) {
		Dpfoss hxg = null;
		String objectMethod = b2ctools.getObjectMethod(key).getJoint_property();
		if (objectMethod != null) {
			JSONObject jsonObj = JSONObject.fromObject(objectMethod);
			hxg = (Dpfoss) JSONObject.toBean(jsonObj, Dpfoss.class);
		} else {
			hxg = new Dpfoss();
		}
		return hxg;
	}

	/**
	 * 订单状态反馈的公用类
	 */
	public long feedback_status() {
		long calcCount = 0;
		long check = 0;
		// 上传签收接口
		for (B2cEnum em : B2cEnum.values()) {
			if (em.getMethod().contains("DPFoss")) {
				if (!b2ctools.isB2cOpen(em.getKey())) {
					logger.info("未开启0德邦物流0上传签收单接口");
					continue;
				}
				calcCount += uploadSignOrderMethod(FlowOrderTypeEnum.YiShenHe.getValue(), em.getKey());
				check++;

			}
		}
		if (check == 0) {
			return -1;
		}
		return calcCount;

	}

	/**
	 * 订单配送track 反馈
	 */
	public void SubmitTrackInfo(long flowordertype, String trackinfo, int b2ckey) {
		Dpfoss dpfoss = getDpfossSettingMethod(b2ckey);
		if (!b2ctools.isB2cOpen(b2ckey)) {
			logger.info("未开启0德邦物流0跟踪反馈接口");
			return;
		}

		logger.info("跟踪状态推送至德邦request={},url={}", trackinfo, dpfoss.getUploadTrack_url());

		String reponseinfo = DpfossHttpSendHander.sendHttpServicetoDpfoss(trackinfo, dpfoss.getUploadTrack_url(), dpfoss.getServiceCode_uploadTrack(), dpfoss.getAgent(), dpfoss.getPwd());

		logger.info("德邦物流接口返回response={}", reponseinfo);

		// List<UploadTrackResponse> responselist
		// =JacksonMapper.getInstance().readValue(reponseinfo,new
		// TypeReference<List<UploadTrackResponse>>() {});

	}

	/**
	 * 0德邦物流 上传签收单接口
	 */
	public long uploadSignOrderMethod(long flowordertype, int b2ckey) {
		long calcCount = 0;

		Dpfoss dpfoss = getDpfossSettingMethod(b2ckey);
		if (!b2ctools.isB2cOpen(b2ckey)) {
			logger.info("未开启0德邦物流0上传签收单接口");
			return -1;
		}

		try {

			int i = 0;
			while (true) {
				List<B2CData> dpfosslist = b2CDataDAO.getDataListByFlowStatus(flowordertype, dpfoss.getCustomerids(), dpfoss.getMaxCount());

				i++;
				if (i > 30) {
					String warning = "查询0德邦物流上传签收单信息反馈已经超过30次循环，可能存在程序未知异常,请及时查询并处理!" + b2ckey;
					logger.warn(warning);
					return 0;
				}

				if (dpfosslist == null || dpfosslist.size() == 0) {
					logger.info("当前没有要推送0德邦物流0上传签收单接口的数据" + b2ckey);
					return 0;
				}
				String b2cids = "";
				List<UploadSignRequest> signlist = new ArrayList<UploadSignRequest>();
				for (B2CData data : dpfosslist) {
					UploadSignRequest note = getDpfossSignedXMLNoteMethod(data.getJsoncontent());
					note.setSignUpId(String.valueOf(data.getB2cid()));
					signlist.add(note);
					b2cids += data.getB2cid() + ",";
				}

				String signRequest = new ObjectMapper().writeValueAsString(signlist);

				logger.info(b2ckey + "状态推送至德邦 userName=" + dpfoss.getAgent() + ",request={},url={}", signRequest, dpfoss.getUploadSign_url());

				String reponseinfo = DpfossHttpSendHander.sendHttpServicetoDpfoss(signRequest, dpfoss.getUploadSign_url(), dpfoss.getServiceCode_uploadSign(), dpfoss.getAgent(), dpfoss.getPwd());

				logger.info(b2ckey + "德邦物流接口返回response={}", reponseinfo);
				if (reponseinfo == null || reponseinfo.isEmpty()) {

					b2CDataDAO.updateMultiB2cIdSQLResponseStatus_AllSuccess(b2cids.substring(0, b2cids.length() - 1));
					logger.warn("德邦物流接口返回空，标示成功1");

				} else if (reponseinfo.contains("Connectiontimeout")) {

					logger.info("发送至德邦订单请求超时");
					continue;
				} else {
					List<UploadSignResponse> responselist = JacksonMapper.getInstance().readValue(reponseinfo, new TypeReference<List<UploadSignResponse>>() {
					});

					if (responselist != null && responselist.size() == 0) {
						b2CDataDAO.updateMultiB2cIdSQLResponseStatus_AllSuccess(b2cids.substring(0, b2cids.length() - 1));
						logger.warn("德邦物流接口返回空，标示成功2,b2cids=" + b2cids);
					}
					for (UploadSignResponse deals : responselist) {
						int send_b2c_flag = deals.getSuccess().equalsIgnoreCase("1") ? 1 : 2;
						b2CDataDAO.updateB2cIdSQLResponseStatus(Long.valueOf(deals.getSignUpId()), send_b2c_flag, deals.getExceptionType() + "," + deals.getExceptionCode() + "," + deals.getMessage());
					}
				}

				calcCount += dpfosslist.size();

			}

		} catch (Exception e) {
			logger.error("调用0德邦物流0webservice服务器异常" + e.getMessage(), e);

		}
		return calcCount;

	}

	public UploadSignRequest getDPFossXMLNoteMethod(String jsoncontent) throws JsonParseException, JsonMappingException, IOException {
		return new ObjectMapper().readValue(jsoncontent, UploadSignRequest.class);
	}

	public String getDpfossTrackEnum(long flowordertype, long deliverystate) {
		if (flowordertype != FlowOrderTypeEnum.YiShenHe.getValue()) {
			for (DpfossTrackFlowEnum TEnum : DpfossTrackFlowEnum.values()) {
				if (flowordertype == TEnum.getDelivery_state() && TEnum.getState() == 0) {
					return TEnum.getRequest_code();
				}
			}
		}

		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && (deliverystate == DeliveryStateEnum.QuanBuTuiHuo.getValue() || deliverystate == DeliveryStateEnum.BuFenTuiHuo.getValue())) {
			return DpfossTrackFlowEnum.JuShou.getRequest_code();
		}

		return null;

	}

	public UploadSignRequest getDpfossSignedXMLNoteMethod(String jsoncontent) {
		try {
			return new ObjectMapper().readValue(jsoncontent, UploadSignRequest.class);
		} catch (Exception e) {
			logger.error("获取DpfossSigned异常！jsoncontent:" + jsoncontent + e);
			return null;
		}
	}

	public String getDpfossSignedEnum(long deliverystate) {
		for (DpfossSignedEnum TEnum : DpfossSignedEnum.values()) {
			if (deliverystate == TEnum.getDelivery_state()) {
				return TEnum.getRequest_code();
			}
		}

		return null;

	}

}
