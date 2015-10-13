package cn.explink.b2c.yangguang;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2cDataOrderFlowDetail;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.User;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.jms.dto.DmpDeliveryState;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.DateTimeUtil;

@Service
public class BulidYangGuangB2cData {
	private Logger logger = LoggerFactory.getLogger(BulidYangGuangB2cData.class);
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;
	@Autowired
	GetDmpDAO getDmpdao;
	@Autowired
	YangGuangService yangGuangService;
	@Autowired
	UserDAO userDAO;
	@Autowired
	private B2cTools b2ctools;

	public YangGuang getYangGuang() {
		return yangGuangService.getYangGuangSettingMethod(B2cEnum.YangGuang.getKey());
	}

	public String BuildYangGuangMethod(DmpOrderFlow orderFlow, long flowOrdertype, DmpCwbOrder cwbOrder, DmpDeliveryState deliveryState, long delivery_state, ObjectMapper objectMapper)
			throws IOException, JsonGenerationException, JsonMappingException {
		YangGuangFlowEnum ygenum = yangGuangService.getYangGuangFlowEnum(flowOrdertype, delivery_state, cwbOrder.getCwbordertypeid());
		if (ygenum == null) {
			logger.info("订单号：{} 不属于0央广购物0所需要的json---,状态{}，return", cwbOrder.getCwb(), flowOrdertype);
			return null;
		} else {

			List<YangGuangdiff> YangGuangdifflist = yangGuangService.filterYangGuangDiffs(yangGuangService.getYangGuangDiffs(B2cEnum.YangGuang.getKey()));
			long customerid = cwbOrder.getCustomerid();
			if (YangGuangdifflist == null || YangGuangdifflist.size() == 0) {
				logger.warn("未进行设置央广购物任何配置");
				return null;
			}
			String express_id = "";
			for (YangGuangdiff diff : YangGuangdifflist) {
				if (diff.getCustomerids().equals(String.valueOf(customerid))) {
					express_id = diff.getExpress_id();
				}
			}

			if (cwbOrder.getTranscwb() == null || cwbOrder.getTranscwb().isEmpty()) {
				logger.warn("0央广0excel导入的数据不做存储!cwb={}", cwbOrder.getCwb());
				return null;
			}

			YangGuangXMLNote xmlnote = new YangGuangXMLNote();
			xmlnote.setOrderNo(cwbOrder.getTranscwb());
			xmlnote.setShippNo(cwbOrder.getCwb());
			xmlnote.setWb_I_No(cwbOrder.getShipcwb());
			xmlnote.setExptReason(getExptReasonCode(cwbOrder, deliveryState.getDeliverystate()));
			xmlnote.setExpress_id(express_id);
			xmlnote.setDeliveryDate(DateTimeUtil.formatDateLong(orderFlow.getCredate(), "yyyyMMdd"));
			xmlnote.setDeliveryResult(String.valueOf(ygenum.getState()));

			logger.info("订单号：{}封装成0央广购物0所需要的json----状态：{}", cwbOrder.getCwb(), flowOrdertype);
			return objectMapper.writeValueAsString(xmlnote);
		}
	}

	private String getExptReasonCode(DmpCwbOrder cwbOrder, long deliveryState) {
		String reason_code = b2ctools.getExptReasonByB2c(cwbOrder.getLeavedreasonid(), cwbOrder.getBackreasonid(), cwbOrder.getCustomerid() + "", deliveryState).getExpt_code();
		if (reason_code == null || "".equals(reason_code)) {
			if (cwbOrder.getBackreasonid() != 0) {
				reason_code = "03";
			}
			if (cwbOrder.getLeavedreasonid() != 0) {
				reason_code = "02";
			}

		}
		return reason_code;
	}
}
