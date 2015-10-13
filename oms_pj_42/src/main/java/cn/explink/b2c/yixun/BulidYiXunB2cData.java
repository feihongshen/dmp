package cn.explink.b2c.yixun;

import java.io.IOException;
import java.math.BigDecimal;

import net.sf.json.JSONObject;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2cDataOrderFlowDetail;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.DeliveryState;
import cn.explink.jms.dto.CwbOrderWithDeliveryState;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.jms.dto.DmpDeliveryState;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.DateTimeUtil;

@Service
public class BulidYiXunB2cData {
	private Logger logger = LoggerFactory.getLogger(BulidYiXunB2cData.class);
	@Autowired
	YiXunService yixunService;
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;
	@Autowired
	GetDmpDAO getDmpdao;

	// @Autowired
	// DeliveryStateDAO deliveryStateDAO;
	public String BuildYiXunMethod(DmpOrderFlow orderFlow, long flowOrdertype, CwbOrderWithDeliveryState cwbOrderWothDeliverystate, long delivery_state, ObjectMapper objectMapper) throws IOException {

		DmpCwbOrder cwbOrder = cwbOrderWothDeliverystate.getCwbOrder();
		DmpDeliveryState dmpDelivery = cwbOrderWothDeliverystate.getDeliveryState();
		YiXunFlowEnum yixunReceive = yixunService.getYiXunFlowEnum(dmpDelivery == null ? 0 : dmpDelivery.getDeliverystate(), flowOrdertype);

		if (yixunReceive == null) {
			logger.info("[易迅]订单号={},flowOrdertype={} 不需要反馈 ，return", orderFlow.getCwb(), flowOrdertype);
			return null;
		}

		logger.info("[易迅]订单号：{}跟踪反馈,状态：{}", orderFlow.getCwb(), yixunReceive.getText());
		YiXunDTO yixunDTO = new YiXunDTO();

		if (cwbOrder != null) {
			String joint_property = yixunService.getObjectMethod(B2cEnum.YiXun.getKey());
			if (yixunDTO.init(joint_property)) {
				yixunDTO.setPackageno(orderFlow.getCwb());
				yixunDTO.setCreatetime(DateTimeUtil.getNowTime());
			} else {
				logger.info("[易迅]订单号：{} 初始化订单反馈地址失败{}", orderFlow.getCwb(), joint_property);
				return null;
			}
			// 跟踪环节 getSaveState=0
			yixunDTO.setMethod("reportscanrecord");
			yixunDTO.setDesc(yixunReceive.getText());
			logger.info("[易迅]订单号：{} 跟踪反馈结果 {}", orderFlow.getCwb(), yixunReceive.getText());

			String responseTrack = YiXunService.toYiXunServicePost(yixunDTO.toReportScanrecordUrl());

			logger.info("[易迅]跟踪信息======request={},response={}", yixunDTO.toReportScanrecordUrl(), responseTrack);

			if (yixunReceive.getSaveState() == -1) { // 问题件
				logger.info("[易迅]订单号：{} 问题件反馈,状态：{}", orderFlow.getCwb(), yixunReceive.getText());
				yixunDTO.setMethod("reportabnormal");
				yixunDTO.setType(yixunReceive.getText());
				yixunDTO.setAbnormal_desc(yixunReceive.getText());

				String responseProblem = YiXunService.toYiXunServicePost(yixunDTO.toReportAbnormalUrl());
				logger.info("[易迅]问题件反馈======request={},response={}", yixunDTO.toReportAbnormalUrl(), responseProblem);

				if (!responseProblem.contains("true")) { // 如果返回失败，则存入
															// 返回false是失败true是成功
					return JSONObject.fromObject(yixunDTO).toString();
				}
				return null;
			}

			if (yixunReceive.getSaveState() >= 1 && dmpDelivery != null) { // 签收反馈

				logger.info("[易迅]订单号：{} 签收反馈,状态：{}", cwbOrder != null ? cwbOrder.getCwb() : cwbOrder.getCwb(), yixunReceive.getText());
				yixunDTO.setMethod("reportsignstatus");
				yixunDTO.setStatus(yixunReceive.getSaveState());
				int status = yixunReceive.getSaveState();
				if ((dmpDelivery.getPos() == null ? 0 : dmpDelivery.getPos().doubleValue()) > 0) {
					yixunDTO.setPaysysno("2");
				} else if ((dmpDelivery.getCash() == null ? 0 : dmpDelivery.getCash().doubleValue()) > 0) {
					yixunDTO.setPaysysno("1");
				} else if ((dmpDelivery.getCheckfee() == null ? 0 : dmpDelivery.getCheckfee().doubleValue()) > 0) {
					yixunDTO.setPaysysno("3");
				} else {
					yixunDTO.setPaysysno("1");
				}
				if (status == 2) {
					yixunDTO.setPaysysno("1");
				}
				yixunDTO.setAmt(status == 1 ? cwbOrder.getReceivablefee().multiply(new BigDecimal(100)).doubleValue() : 0);
				yixunDTO.setNote("");
				yixunDTO.setSigntime(DateTimeUtil.getNowTime());
				String responseSign = YiXunService.toYiXunServicePost(yixunDTO.toReportSignstatusUrl());
				logger.info("[易迅]签收反馈======request={},response={}", yixunDTO.toReportSignstatusUrl(), responseSign);

				if (!responseSign.contains("true")) {
					return JSONObject.fromObject(yixunDTO).toString(); // 如果返回失败，则存入
				}
				return null;
			}
		}
		return null;

	}

}
