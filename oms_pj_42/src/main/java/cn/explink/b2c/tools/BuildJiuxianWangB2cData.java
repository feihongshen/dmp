package cn.explink.b2c.tools;

import java.io.IOException;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.explink.b2c.jiuxian.JiuxianService;
import cn.explink.b2c.jiuxian.JiuxianWang;
import cn.explink.b2c.jiuxian.JiuxianWangFlowEunm;
import cn.explink.b2c.jiuxian.JiuxianXMLNote;
import cn.explink.dao.GetDmpDAO;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.jms.dto.CwbOrderWithDeliveryState;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.DateTimeUtil;

@Service
public class BuildJiuxianWangB2cData {
	@Autowired
	JiuxianService jiuxianService;
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;
	@Autowired
	GetDmpDAO getDmpdao;

	public JiuxianWang getJiuxianData() {
		return jiuxianService.getJiuxianwang(B2cEnum.Jiuxian.getKey());
	}

	private Logger logger = LoggerFactory.getLogger(JiuxianWang.class);

	public String BuildBenlaiMethod(DmpOrderFlow orderFlow, long flowOrdertype, DmpCwbOrder cwbOrder, long delivery_state, CwbOrderWithDeliveryState cwbOrderWothDeliverystate,
			ObjectMapper objectMapper) throws IOException, JsonGenerationException, JsonMappingException {
		JiuxianWang jiuxian = getJiuxianData();

		String status = "";// 操作详情状态
		String onfig = getJiuxianFlowEnum(flowOrdertype, delivery_state);
		if (onfig == null) {
			logger.info("当前状态不属于[酒仙网]推送的状态,cwb={}", cwbOrder.getCwb());
			return null;
		}
		status = orderFlowDetail.getDetail(orderFlow);
		JiuxianXMLNote xmlnote = new JiuxianXMLNote();
		if (flowOrdertype == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
			xmlnote.setCourier(getDmpdao.getUserById(cwbOrder.getDeliverid()).getRealname());// 小件员
		} else {

			xmlnote.setCourier(getDmpdao.getUserById(orderFlow.getUserid()).getRealname());// 操作人
		}
		xmlnote.setCwb(orderFlow.getCwb());
		xmlnote.setTranscwb(cwbOrder.getTranscwb());
		xmlnote.setState_desc(status);
		xmlnote.setOperatetype(flowOrdertype);
		xmlnote.setAcceptTime(DateTimeUtil.formatDate(orderFlow.getCredate()));
		String name = "";
		if (flowOrdertype == FlowOrderTypeEnum.YiShenHe.getValue()) {
			if (delivery_state == DeliveryStateEnum.PeiSongChengGong.getValue() || delivery_state == DeliveryStateEnum.ShangMenTuiChengGong.getValue()
					|| delivery_state == DeliveryStateEnum.ShangMenHuanChengGong.getValue()) {
				xmlnote.setType("SC_Sign");
				name = "签收";
				xmlnote.setSignman(cwbOrder.getConsigneename());
			} else if (delivery_state == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
				xmlnote.setType("SC_Trans");
				name = "在途";
			} else {
				xmlnote.setType("SC_Back");
				name = "拒收";
			}
		} else if (flowOrdertype == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
			xmlnote.setCourier(getDmpdao.getUserById(cwbOrder.getDeliverid()).getRealname());// 小件员
			xmlnote.setContact_phone(getDmpdao.getUserById(cwbOrder.getDeliverid()).getUsermobile());
			xmlnote.setType("SC_Trans");
			name = "在途";
		} else {
			xmlnote.setType("SC_Trans");
			name = "在途";
		}
		xmlnote.setStatus(name);

		logger.info("订单号={} 封装[酒仙网]所需要的配送结果json---,状态{}，return", cwbOrder.getCwb(), flowOrdertype);

		return objectMapper.writeValueAsString(xmlnote);
	}

	public String getJiuxianFlowEnum(long flowordertype, long deliverystate) {
		for (JiuxianWangFlowEunm SEnum : JiuxianWangFlowEunm.values()) {
			if (flowordertype == SEnum.getFlowordertype()) {
				return SEnum.getText();
			}
		}
		return null;
	}
}
