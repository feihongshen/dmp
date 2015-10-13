package cn.explink.b2c.jumeiyoupin;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2cDataOrderFlowDetail;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.ExptReason;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.DateTimeUtil;

@Service
public class BulidJuMeiB2cData {
	private Logger logger = LoggerFactory.getLogger(BulidJuMeiB2cData.class);
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;

	@Autowired
	JumeiYoupinService jumeiService;

	// transporting 运输中（数据导入 00）第三方开始从聚美仓库所在分拨中心，向目的地分拨中心发货
	// on_road 配送中(总站出库 03)第三方目的地分拨中心，发往下级站点或配送中 当聚美订单状态为“已出库”“运输中”时，可提交此状态
	// delivered 妥投0801 第三方订单配送成功 当聚美订单状态为“配送中”“滞留”时，可提交此状态
	// deferred 滞留0806 第三方订单因各类原因无法完成投递 当聚美订单状态为“配送中”“运输中”时，可以提交此状态
	// rejected 拒收0802 客户拒收 当聚美订单状态为“配送中”“滞留”时，可提交此状态
	// lost 第三方遗失订单 当聚美订单状态为“运输中”“配送中”“滞留”“拒收”时，可提交此状态
	public String BuildJuMeiDataMethod(DmpOrderFlow orderFlow, long flowOrdertype, DmpCwbOrder cwbOrder, long delivery_state, ObjectMapper objectMapper) throws IOException, JsonGenerationException,
			JsonMappingException {
		logger.info("订单号：{}封装成聚美优品所需要的json-----开始", cwbOrder.getCwb());
		JuMeiYouPin_json jumeiJson = new JuMeiYouPin_json();
		jumeiJson.setCwb(cwbOrder.getCwb());
		jumeiJson.setTrackdatetime(DateTimeUtil.formatDate(orderFlow.getCredate()));
		jumeiJson.setTrackevent(orderFlowDetail.getDetail(orderFlow));

		if (flowOrdertype == FlowOrderTypeEnum.DaoRuShuJu.getValue()) { // 入库
			JuMeiYouPin jumei = jumeiService.getJuMeiYouPinSettingMethod(B2cEnum.JuMeiYouPin.getKey()); // 获取配置信息
			String descirbe = jumei.getDescirbe();

			jumeiJson.setTrackstatus("on_road");
			jumeiJson.setTrackevent(orderFlowDetail.getDetail(orderFlow) + descirbe);

			logger.info("订单号：{} 封装成聚美优品所需要的json-----结束 库房导入数据", cwbOrder.getCwb());
			return objectMapper.writeValueAsString(jumeiJson);
		}

		if (flowOrdertype == FlowOrderTypeEnum.RuKu.getValue()) { // 入库
			jumeiJson.setTrackstatus("on_road");
			logger.info("订单号：{} 封装成聚美优品所需要的json-----结束 库房入库", cwbOrder.getCwb());
			return objectMapper.writeValueAsString(jumeiJson);
		}
		if (flowOrdertype == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) { // 出库扫描
			jumeiJson.setTrackstatus("on_road");
			logger.info("订单号：{} 封装成聚美优品所需要的json-----结束 库房出库", cwbOrder.getCwb());
			return objectMapper.writeValueAsString(jumeiJson);
		}
		if (flowOrdertype == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) { // 分站到货扫描
			jumeiJson.setTrackstatus("on_road");
			logger.info("订单号：{} 封装成聚美优品所需要的json-----结束 分站到货", cwbOrder.getCwb());
			return objectMapper.writeValueAsString(jumeiJson);
		}

		if (flowOrdertype == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) { // 原始入库
																			// 改为领货
			jumeiJson.setTrackstatus("on_road");
			logger.info("订单号：{} 封装成聚美优品所需要的json-----结束 领货", cwbOrder.getCwb());
			return objectMapper.writeValueAsString(jumeiJson);
		}

		if (flowOrdertype == FlowOrderTypeEnum.YiShenHe.getValue()
				&& (delivery_state == DeliveryStateEnum.PeiSongChengGong.getValue() || delivery_state == DeliveryStateEnum.ShangMenHuanChengGong.getValue() || delivery_state == DeliveryStateEnum.ShangMenTuiChengGong
						.getValue())) { // 配送成功
			jumeiJson.setTrackstatus("delivered");
			logger.info("订单号：{} 封装成聚美优品所需要的json-----结束 配送成功", cwbOrder.getCwb());
			return objectMapper.writeValueAsString(jumeiJson);
		}
		if (flowOrdertype == FlowOrderTypeEnum.YiShenHe.getValue() && delivery_state == DeliveryStateEnum.HuoWuDiuShi.getValue()) {// 货物丢失
			jumeiJson.setTrackstatus("lost");
			logger.info("订单号：{} 封装成聚美优品所需要的json-----结束 货物丢失", cwbOrder.getCwb());
			return objectMapper.writeValueAsString(jumeiJson);
		}

		if (flowOrdertype == FlowOrderTypeEnum.YiShenHe.getValue() && delivery_state == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {// 分站滞留
		// 11 联系不到客户
		// 12 与客户越好时间
		// 13 客户要求推迟时间收货
		// 14 订单超远点，定期配送
		// 15 客户要求修改配送地址
		// 16 天气问题
		// 17 其他原因
			ExptReason exptreason = jumeiService.getExptReasonByJuMei(cwbOrder.getLeavedreasonid(), 0, cwbOrder.getCustomerid() + "");
			String code = exptreason.getExpt_code() == null ? "17" : exptreason.getExpt_code();
			String msg = exptreason.getExpt_msg() == null ? "其他原因" : exptreason.getExpt_msg();
			jumeiJson.setTrackstatus("deferred," + code + "," + msg);
			logger.info("订单号：" + cwbOrder.getCwb() + "封装成聚美优品所需要的json-----结束 分站滞留");
			return objectMapper.writeValueAsString(jumeiJson);
		}
		if (flowOrdertype == FlowOrderTypeEnum.YiShenHe.getValue() && (delivery_state == DeliveryStateEnum.BuFenTuiHuo.getValue() || delivery_state == DeliveryStateEnum.QuanBuTuiHuo.getValue())) { // 拒收
		// 21 联系不到客户
		// 22 产品质量问题
		// 23 物流配送问题
		// 24 少发货
		// 25 客服通知取消订单
		// 26 恶意订单
		// 27 其他原因
			ExptReason exptreason = jumeiService.getExptReasonByJuMei(0, cwbOrder.getBackreasonid(), cwbOrder.getCustomerid() + "");
			String code = exptreason.getExpt_code() == null ? "27" : exptreason.getExpt_code();
			String msg = exptreason.getExpt_msg() == null ? "其他原因" : exptreason.getExpt_msg();
			jumeiJson.setTrackstatus("rejected," + code + "," + msg);
			logger.info("订单号：{} 封装成聚美优品所需要的json-----结束 拒收", cwbOrder.getCwb());
			return objectMapper.writeValueAsString(jumeiJson);
		}
		return null;
	}
}
