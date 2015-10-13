package cn.explink.b2c.yihaodian;

import java.math.BigDecimal;
import java.util.List;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.B2cDataOrderFlowDetail;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.FlowFromJMSB2cService;
import cn.explink.b2c.yihaodian.xmldto.OrderDeliveryLogDto;
import cn.explink.b2c.yihaodian.xmldto.OrderDeliveryResultDto;
import cn.explink.b2c.yihaodian.xmldto.ReturnDto;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.B2CData;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.MD5.MD5Util;

@Service
public class YihaodianService {
	private Logger logger = LoggerFactory.getLogger(YihaodianService.class);
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
	 * 获取一号店配置信息
	 * 
	 * @param key
	 * @return
	 */
	public Yihaodian getYihaodianSettingMethod(int key) {
		Yihaodian yihaodian = null;
		String objectMethod = b2ctools.getObjectMethod(key).getJoint_property();
		if (objectMethod != null) {
			JSONObject jsonObj = JSONObject.fromObject(objectMethod);
			yihaodian = (Yihaodian) JSONObject.toBean(jsonObj, Yihaodian.class);
		} else {
			yihaodian = new Yihaodian();
		}
		return yihaodian;
	}

	/**
	 * 获取Yihaodian XML Note
	 * 
	 * @param jsoncontent
	 * @return
	 */
	public YihaodianXMLNote getYihaodianXMLNoteMethod(String jsoncontent) {
		try {
			JSONObject jsonObj = JSONObject.fromObject(jsoncontent);
			return (YihaodianXMLNote) JSONObject.toBean(jsonObj, YihaodianXMLNote.class);
		} catch (Exception e) {
			logger.error("获取YihaodianXMLNote异常！jsoncontent:" + jsoncontent + e);
			return null;
		}
	}

	/**
	 * 一号店定时器的总开关方法 -投递结果反馈
	 */
	public long YiHaoDianInterfaceInvoke(int yhd_key) {
		long calcCount = 0;
		if (!b2ctools.isB2cOpen(yhd_key)) {
			logger.info("未开启[一号店]对接,yhd_key={}", yhd_key);
			return -1;
		}
		calcCount = DeliveryResultByYiHaoDian(yhd_key);
		return calcCount;
	}

	/**
	 * 投递结果反馈 只推送结果为 0 send_b2c_flag=0
	 */
	public long DeliveryResultByYiHaoDian(int yhd_key) {
		long calcCount = 0;
		Yihaodian yihaodian = getYihaodianSettingMethod(yhd_key);
		try {
			
			String ywcustomerid=yihaodian.getYwcustomerid()==null||yihaodian.getYwcustomerid().isEmpty()?"0":yihaodian.getYwcustomerid();
			
			List<B2CData> datalist = b2cDataDAO.getDataListByFlowStatus(FlowOrderTypeEnum.YiShenHe.getValue(), yihaodian.getCustomerids()+","+ywcustomerid, yihaodian.getCallBackCount());
			if (datalist == null || datalist.size() == 0) {
				return 0;
			}
			String b2cids = "";
			for (B2CData b2cdata : datalist) {
				OrderDeliveryResultDto condto = getOrderDeliverResultDto(yihaodian, b2cdata);
				ReturnDto returnDto = restTemplate.DeliveryResult(yihaodian.getDeliveryResult_URL(), condto); // 返回dto
				if (!returnDto.getErrCode().equals(YihaodianExpEmum.Success.getErrCode())) {

					b2cDataDAO.updateB2cIdSQLResponseStatus(b2cdata.getB2cid(), 2, returnDto.getErrMsg());
					logger.info("回调[一号店]的[投递结果反馈]接口-返回异常:errCode={},errMsg={},cwb=" + b2cdata.getCwb(), returnDto.getErrCode(), returnDto.getErrMsg());

				} else {
					b2cDataDAO.updateB2cIdSQLResponseStatus(b2cdata.getB2cid(), 1, "");
					logger.info("成功的调用[一号店]的[投递结果反馈]接口,修改为已推送,当前反馈运单号={}", b2cdata.getCwb());
					b2cids += b2cdata.getB2cid() + ",";
				}
			}

			b2cids = b2cids.length() > 0 ? b2cids.substring(0, b2cids.length() - 1) : "0";
			// 发送给dmp
			flowFromJMSB2cService.sendTodmp(b2cids);

			if (datalist != null && datalist.size() > 0) {
				DeliveryResultByYiHaoDian(yhd_key);
			}

			calcCount += datalist.size();
		} catch (Exception e) {
			logger.error("error info by request yihaodian  DeliveryResultByYiHaoDian  interface!" + e);

		}
		return calcCount;
	}

	/**
	 * 生成一个Dto
	 * 
	 * @param yihaodian
	 * @param b2cdata
	 * @return
	 */
	private OrderDeliveryResultDto getOrderDeliverResultDto(Yihaodian yihaodian, B2CData b2cdata) {
		OrderDeliveryResultDto condto = new OrderDeliveryResultDto();
		YihaodianXMLNote note = getYihaodianXMLNoteMethod(b2cdata.getJsoncontent());
		condto.setUserCode(yihaodian.getUserCode());
		String nowtime = DateTimeUtil.getNowTime();
		condto.setRequestTime(nowtime);
		condto.setSign(MD5Util.md5(yihaodian.getUserCode() + b2cdata.getCwb() + note.getDeliverystate() + note.getAmount() + nowtime + yihaodian.getPrivate_key()));
		condto.setShipmentCode(b2cdata.getCwb());
		condto.setAmount(note.getAmount());
		condto.setPayTime(note.getPayTime());
		condto.setDeliveryState(note.getDeliverystate());
		condto.setPayMethod(1); // 一号店要求写死 为cash 1
		condto.setCashAmount(note.getCashAmount());
		condto.setCardAmount(BigDecimal.ZERO);
		condto.setCheckAmount(BigDecimal.ZERO);
		return condto;
	}

	/**
	 * 跟踪日志反馈
	 * 
	 * @param flowordertype
	 * @param delivery_state
	 * @return
	 */
	public void DeliveryLogFeedBack(DmpOrderFlow orderFlow, long delivery_state, long flowOrdertype, int yhd_key) {
		Yihaodian yihaodian = getYihaodianSettingMethod(yhd_key);
		if (!b2ctools.isB2cOpen(yhd_key)) {
			logger.info("未开Yihaodian的对接,yhd_key={}", yhd_key);
			return;
		}
		OrderDeliveryLogDto logdto = new OrderDeliveryLogDto();
		logdto.setUserCode(yihaodian.getUserCode());
		String nowtime = DateTimeUtil.getNowTime();
		YihaodianFlowEnum yihaodianEnum = getYihaodianFlowEnum(flowOrdertype, delivery_state);
		logdto.setRequestTime(nowtime);
		logdto.setSign(MD5Util.md5(yihaodian.getUserCode() + orderFlow.getCwb() + yihaodianEnum.getYihaodian_state() + nowtime + yihaodian.getPrivate_key()));
		logdto.setShipmentCode(orderFlow.getCwb());
		logdto.setOperationTypeEnumValue(yihaodianEnum.getYihaodian_state());
		logdto.setOperationType(getYihaodianFlowEnum(flowOrdertype, delivery_state).getText());
		logdto.setOperationTime(DateTimeUtil.formatDate(orderFlow.getCredate()));
		logdto.setOperator(getDmpdao.getUserById(orderFlow.getUserid()).getRealname());
		logdto.setRemark(orderFlowDetail.getDetail(orderFlow));

		ReturnDto returnDto = restTemplate.orderDeliveryLog(yihaodian.getTrackLog_URL(), logdto); // 返回dto
		if (returnDto == null) {
			logger.info("跟踪日志反馈，发送超时。。yhd_key={},cwb={}", yhd_key, orderFlow.getCwb());
		}
		if (returnDto != null && YihaodianExpEmum.Success.getErrCode().equals(returnDto.getErrCode())) {
			logger.info("跟踪日志反馈[一号店]-反馈成功，订单号={},状态={}", orderFlow.getCwb(), yihaodianEnum.getText());
		} else {
			logger.info("跟踪日志反馈[一号店]-反馈失败，订单号=" + orderFlow.getCwb() + "，errCode=" + returnDto.getErrCode() + ",errMsg=" + returnDto.getErrMsg());
		}
	}

	public YihaodianFlowEnum getYihaodianFlowEnum(long flowordertype, long delivery_state) {
		if (flowordertype != FlowOrderTypeEnum.YiShenHe.getValue()) {
			for (YihaodianFlowEnum TEnum : YihaodianFlowEnum.values()) {
				if (flowordertype == TEnum.getFlowordertype()) {
					return TEnum;
				}
			}
		}
		if (delivery_state == DeliveryStateEnum.PeiSongChengGong.getValue()) {
			return YihaodianFlowEnum.PeiSongChengGong;
		}
		if (delivery_state == DeliveryStateEnum.QuanBuTuiHuo.getValue()) {
			return YihaodianFlowEnum.JuShou;
		}
		if (delivery_state == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
			return YihaodianFlowEnum.FenZhanZhiLiu;
		}
		// if(delivery_state==DeliveryStateEnum.ShangMenHuanChengGong.getValue()){
		// return YihaodianFlowEnum.ShangMenHuanChengGong;
		// }
		// if(delivery_state==DeliveryStateEnum.ShangMenTuiChengGong.getValue()){
		// return YihaodianFlowEnum.ShangMenTuiChengGong;
		// }
		// if(delivery_state==DeliveryStateEnum.ShangMenJuTui.getValue()){
		// return YihaodianFlowEnum.JuShou;
		// }
		if (delivery_state == DeliveryStateEnum.BuFenTuiHuo.getValue()) {
			return YihaodianFlowEnum.BuFenShiBai;
		}
		return null;

	}

	/**
	 * 根据我方配送结果转化为 一号店的配送结果
	 * 
	 * @return
	 */
	public int getDeliveryStateByYihaodian(long deliverystate) {
		if (deliverystate == DeliveryStateEnum.PeiSongChengGong.getValue()) {
			return YiHaoDian_DeliveryStateEnum.PeiSongChengGong.getValue();
		}
		if (deliverystate == DeliveryStateEnum.BuFenTuiHuo.getValue()) {
			return YiHaoDian_DeliveryStateEnum.BuFenChengGong.getValue();
		}
		if (deliverystate == DeliveryStateEnum.QuanBuTuiHuo.getValue()) {
			return YiHaoDian_DeliveryStateEnum.PeiSongShiBai.getValue();
		}
		if (deliverystate == DeliveryStateEnum.ShangMenHuanChengGong.getValue()) {
			return YiHaoDian_DeliveryStateEnum.PeiSongChengGong.getValue();
		}
		if (deliverystate == DeliveryStateEnum.ShangMenTuiChengGong.getValue()) {
			return YiHaoDian_DeliveryStateEnum.PeiSongChengGong.getValue();
		}
		if (deliverystate == DeliveryStateEnum.ShangMenJuTui.getValue()) {
			return YiHaoDian_DeliveryStateEnum.PeiSongShiBai.getValue();
		}
		return 0;
	}

	/**
	 * 投递结果反馈 只推送结果为 2 send_b2c_flag=2 每 1 个小时 推送一次 最多能推送 100次
	 */
	public void DeliveryResultByYiHaoDianAgain(int yhd_key) {
		Yihaodian yihaodian = getYihaodianSettingMethod(yhd_key);
		try {
			String remarklike = "06"; // 模糊关键词 ，确保只识别 存在的订单且是反馈为 拒收的。 06 表明拒收异常的
			List<B2CData> datalist = b2cDataDAO.getDataListFailedByFlowStatus(FlowOrderTypeEnum.YiShenHe.getValue(), yihaodian.getCustomerids(), yihaodian.getCallBackCount(), 50, remarklike);
			if (datalist == null || datalist.size() == 0) {
				return;
			}

			for (B2CData b2cdata : datalist) {
				OrderDeliveryResultDto condto = getOrderDeliverResultDto(yihaodian, b2cdata);
				ReturnDto returnDto = restTemplate.DeliveryResult(yihaodian.getDeliveryResult_URL(), condto); // 返回dto

				if (!returnDto.getErrCode().equals(YihaodianExpEmum.Success.getErrCode())) {

					b2cDataDAO.updateB2cIdSQLResponseStatus(b2cdata.getB2cid(), 2, returnDto.getErrCode() + returnDto.getErrMsg());
					logger.info("回调[一号店]的[投递结果反馈][再次推送]接口-返回异常:errCode={},errMsg={},cwb=" + b2cdata.getCwb(), returnDto.getErrCode(), returnDto.getErrMsg());

				} else {
					b2cDataDAO.updateB2cIdSQLResponseStatus(b2cdata.getB2cid(), 1, "");
					logger.info("调用[一号店]的[投递结果反馈][再次推送]接口返回成功,修改为已推送,当前反馈运单号={}", b2cdata.getCwb());
				}
			}

		} catch (Exception e) {
			logger.error("error info by request yihaodian  DeliveryResultByYiHaoDian  interface Again!" + e);
			e.printStackTrace();
		}
	}

}
