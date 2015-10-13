package cn.explink.b2c.yonghuics;

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
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.User;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.jms.dto.CwbOrderWithDeliveryState;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.jms.dto.DmpDeliveryState;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.MD5.MD5Util;

@Service
public class BulidYonghuiB2cData {
	private Logger logger = LoggerFactory.getLogger(BulidYonghuiB2cData.class);
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;
	@Autowired
	GetDmpDAO getDmpdao;
	@Autowired
	YonghuiService yonghuiService;

	public String buildYonghuiMethod(DmpOrderFlow orderFlow, long flowOrdertype, CwbOrderWithDeliveryState cwbOrderWothDeliverystate, long delivery_state, String yihaodian_key,
			ObjectMapper objectMapper) throws IOException, JsonGenerationException, JsonMappingException {
		DmpCwbOrder cwbOrder = cwbOrderWothDeliverystate.getCwbOrder();
		DmpDeliveryState dmpDelivery = cwbOrderWothDeliverystate.getDeliveryState();
		YonghuiFlowEnum flowEnum = yonghuiService.getYonghuiFlowEnum(flowOrdertype, delivery_state);
		if (flowEnum == null) {
			return null;
		}

		logger.info("订单号：{}封装成永辉超市所需要的json----开始,状态：{}", cwbOrder.getCwb(), flowOrdertype);
		Yonghui yh = yonghuiService.getYihaodianSettingMethod(B2cEnum.YongHuics.getKey());

		YonghuiJSONnote note = new YonghuiJSONnote();
		note.setUserCode(yh.getUserCode());
		String requestTime = DateTimeUtil.getNowTime("yyyyMMddHHmmss");
		note.setRequestTime(requestTime);
		note.setSign(MD5Util.md5(yh.getUserCode() + requestTime + yh.getPrivate_key()));
		note.setSheetid(cwbOrder.getCwb());
		note.setBagno(cwbOrder.getTranscwb()); // ?这个多个分隔的问题怎么解决？
		note.setFlag(flowEnum.getState() + "");

		String sender = "";
		String sendphone = "";
		if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
			User deliverUser = getDmpdao.getUserById(cwbOrder.getDeliverid());
			sender = deliverUser.getRealname();
			sendphone = deliverUser.getUsermobile();
		}
		note.setSender(sender);
		note.setSendphone(sendphone);
		note.setSdate(DateTimeUtil.formatDate(orderFlow.getCredate()));
		note.setNote(orderFlowDetail.getDetail(orderFlow));

		logger.info("订单号：{}封装成永辉超市所需要的json----结束,状态：{}", cwbOrder.getCwb(), flowOrdertype);
		return objectMapper.writeValueAsString(note);

	}

}
