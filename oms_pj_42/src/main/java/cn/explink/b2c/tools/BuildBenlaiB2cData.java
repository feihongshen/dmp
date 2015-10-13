package cn.explink.b2c.tools;

import java.io.IOException;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.benlaishenghuo.BenlaiFlowEunm;
import cn.explink.b2c.benlaishenghuo.BenlaiXMLNote;
import cn.explink.b2c.benlaishenghuo.Benlaishenghuo;
import cn.explink.b2c.benlaishenghuo.BenlaishenghuoService;
import cn.explink.dao.GetDmpDAO;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.jms.dto.CwbOrderWithDeliveryState;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.DateTimeUtil;

@Service
public class BuildBenlaiB2cData {
	@Autowired
	BenlaishenghuoService benlaishenghuoService;
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;
	@Autowired
	GetDmpDAO getDmpdao;

	public Benlaishenghuo getBenlaishenghuo() {
		return benlaishenghuoService.getBenlaishenghuo(B2cEnum.Benlaishenghuo.getKey());
	}

	private Logger logger = LoggerFactory.getLogger(BenlaishenghuoService.class);

	public String BuildBenlaiMethod(DmpOrderFlow orderFlow, long flowOrdertype, DmpCwbOrder cwbOrder, long delivery_state, CwbOrderWithDeliveryState cwbOrderWothDeliverystate,
			ObjectMapper objectMapper) throws IOException, JsonGenerationException, JsonMappingException {
		Benlaishenghuo benlai = getBenlaishenghuo();

		String status = "";// 操作详情状态
		String onfig = getSaohuobangFlowEnum(flowOrdertype, delivery_state);
		if (onfig == null) {
			logger.info("当前状态不属于[benlai]推送的状态,cwb={}", cwbOrder.getCwb());
			return null;
		}
		status = orderFlowDetail.getDetail(orderFlow);
		BenlaiXMLNote xmlnote = new BenlaiXMLNote();
		xmlnote.setCwb(orderFlow.getCwb());
		xmlnote.setTranscwb(cwbOrder.getTranscwb());
		xmlnote.setStatus(status);
		xmlnote.setOperatetype(flowOrdertype);
		xmlnote.setAcceptTime(DateTimeUtil.formatDate(orderFlow.getCredate()));
		if (flowOrdertype == FlowOrderTypeEnum.YiShenHe.getValue()) {
			if (delivery_state == DeliveryStateEnum.PeiSongChengGong.getValue() || delivery_state == DeliveryStateEnum.ShangMenTuiChengGong.getValue()
					|| delivery_state == DeliveryStateEnum.ShangMenHuanChengGong.getValue()) {
				xmlnote.setType("1");
				xmlnote.setSignman(cwbOrder.getConsigneename());
			} else if (delivery_state == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
				xmlnote.setType("3");
			} else {
				xmlnote.setType("2");
			}
		} else if (flowOrdertype == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
			xmlnote.setCourier(getDmpdao.getUserById(cwbOrder.getDeliverid()).getRealname());// 小件员
			xmlnote.setContact_phone(getDmpdao.getUserById(cwbOrder.getDeliverid()).getUsermobile());
			xmlnote.setType("4");
		} else {
			xmlnote.setType("3");
		}

		logger.info("订单号={} 封装[benlai]所需要的配送结果json---,状态{}，return", cwbOrder.getCwb(), flowOrdertype);

		return objectMapper.writeValueAsString(xmlnote);
	}

	public String getSaohuobangFlowEnum(long flowordertype, long deliverystate) {
		for (BenlaiFlowEunm SEnum : BenlaiFlowEunm.values()) {
			if (flowordertype == SEnum.getFlowordertype()) {
				return SEnum.getText();
			}
		}
		return null;
	}
}