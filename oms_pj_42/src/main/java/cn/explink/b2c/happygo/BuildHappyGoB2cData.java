package cn.explink.b2c.happygo;

import java.io.IOException;
import java.math.BigDecimal;

import net.sf.json.JSONObject;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.explink.b2c.happygo.HappyGo;
import cn.explink.b2c.happygo.HappyGoService;
import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.B2cDataOrderFlowDetail;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.ExptReason;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.B2CData;
import cn.explink.domain.User;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.jms.dto.CwbOrderWithDeliveryState;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.DateTimeUtil;

@Service
public class BuildHappyGoB2cData {
	private Logger logger = LoggerFactory.getLogger(BuildHappyGoB2cData.class);
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;
	@Autowired
	HappyGoService happyGoservice;
	@Autowired
	GetDmpDAO getDmpdao;
	@Autowired
	B2cTools b2cTools;
	@Autowired
	B2CDataDAO b2CDataDAO;

	public HappyGo getHappyGo() {
		return happyGoservice.getHappyGo(B2cEnum.happyGo.getKey());
	}

	public String BuildHappyGoMethod(DmpOrderFlow orderFlow, long flowOrdertype, DmpCwbOrder cwbOrder, long delivery_state, CwbOrderWithDeliveryState cwbOrderWothDeliverystate,
			ObjectMapper objectMapper) throws IOException, JsonGenerationException, JsonMappingException {
		HappyGo happy = this.getHappyGo();

		logger.info("=======================快乐购=====================================");

		if (flowOrdertype == FlowOrderTypeEnum.RuKu.getValue() && cwbOrder.getRemark2().contains("push")) {

			se99Dlist xmlnote = new se99Dlist();
			xmlnote.setOrder_no(cwbOrder.getTranscwb());// 上门换
			if (cwbOrder.getCommoncwb() != null && cwbOrder.getCommoncwb().length() > 0) {
				xmlnote.setOrder_no(cwbOrder.getCommoncwb());// 上门换
			}
			xmlnote.setWb_no(orderFlow.getCwb());
			xmlnote.setReceiver_name(cwbOrder.getConsigneename());
			xmlnote.setSend_date(DateTimeUtil.formatDate(orderFlow.getCredate(), "yyyy-MM-dd hh:mm:ss"));
			String num = String.valueOf(cwbOrder.getScannum());
			xmlnote.setPiece(num);
			xmlnote.setReceiver_addr(cwbOrder.getConsigneeaddress());
			xmlnote.setReceiver_tel(cwbOrder.getConsigneemobile());
			xmlnote.setWh_code(cwbOrder.getRemark2().toString().substring(7));
			xmlnote.setRequesttime("00");
			xmlnote.setReceive_remark("1");
			xmlnote.setWeight(cwbOrder.getCarrealweight().toString());
			xmlnote.setCod(FunctionForHappy.HOPE000005.getText());
			logger.info("订单号={} 封装快乐购干线到货接口所需要的入库信息-- [干线到货]-,状态{}，return", cwbOrder.getCwb(), flowOrdertype);
			return objectMapper.writeValueAsString(xmlnote);
		}
		// COD付款信息提交接口 代收款必须大于0
		if (flowOrdertype == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
			User sender = getDmpdao.getUserById(cwbOrder.getDeliverid());

			se99Dlist xmlnote = new se99Dlist();
			xmlnote.setWb_no(orderFlow.getCwb());
			xmlnote.setOrder_no(cwbOrder.getTranscwb());
			if (cwbOrder.getCommoncwb() != null && cwbOrder.getCommoncwb().length() > 0) {
				xmlnote.setOrder_no(cwbOrder.getCommoncwb());// 上门换
			}
			xmlnote.setSend_date(DateTimeUtil.formatDate(orderFlow.getCredate(), "yyyy-MM-dd hh:mm:ss"));
			xmlnote.setReceiver_name(cwbOrder.getConsigneename());
			// 小件员姓名，电话，id
			xmlnote.setSendername(sender.getUsername());
			xmlnote.setSendermobile(sender.getUsermobile());
			xmlnote.setSenderid(sender.getUserid());
			xmlnote.setReceiver_tel(cwbOrder.getConsigneemobile());
			xmlnote.setCod(FunctionForHappy.HOPE000018.getText());
			xmlnote.setWh_code(cwbOrder.getRemark2().toString().substring(7));
			logger.info("订单号={} 封装快乐购短信接口所需要的分站领货json---,状态{}，return", cwbOrder.getCwb(), flowOrdertype);
			return objectMapper.writeValueAsString(xmlnote);
		}
		if (flowOrdertype == FlowOrderTypeEnum.YiShenHe.getValue()) {

			se99Dlist xmlnote = doMixXmlForRetrun(orderFlow, cwbOrder, delivery_state, cwbOrderWothDeliverystate, happy);
			logger.info("订单号={} 封装快乐购所需要的配送结果json---,状态{}，return", cwbOrder.getCwb(), flowOrdertype);
			if (xmlnote == null) {
				return null;
			}

			return objectMapper.writeValueAsString(xmlnote);
		}

		return null;

	}

	private se99Dlist doMixXmlForRetrun(DmpOrderFlow orderFlow, DmpCwbOrder cwbOrder, long delivery_state, CwbOrderWithDeliveryState cwbOrderWothDeliverystate, HappyGo happy) {
		ExptReason exptReason = b2cTools.getExptReasonByB2c(cwbOrder.getLeavedreasonid(), cwbOrder.getBackreasonid(), String.valueOf(cwbOrder.getCustomerid()), delivery_state);
		se99Dlist xmlnote = new se99Dlist();
		xmlnote.setWh_code(cwbOrder.getRemark2().toString().substring(7));
		xmlnote.setSend_date(DateTimeUtil.formatDate(orderFlow.getCredate(), "yyyy-MM-dd hh:mm:ss"));
		xmlnote.setSend_gb(exptReason.getExpt_code());
		xmlnote.setSend_ornot("1");
		if (delivery_state == DeliveryStateEnum.BuFenTuiHuo.getValue() || delivery_state == DeliveryStateEnum.ShangMenJuTui.getValue() || delivery_state == DeliveryStateEnum.QuanBuTuiHuo.getValue()
				|| delivery_state == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
			xmlnote.setSend_ornot("0");
			logger.info("快乐购异常订单号={}的订单审核为拒收，时间：{}", orderFlow.getCwb(), DateTimeUtil.getNowTime());
			return null;
		}
		String num = String.valueOf(cwbOrder.getSendcarnum());
		xmlnote.setPiece(num);
		xmlnote.setState(cwbOrder.getRemark2());
		xmlnote.setReceiver_name(cwbOrder.getConsigneename());
		xmlnote.setReceiver_addr(cwbOrder.getConsigneeaddress());
		xmlnote.setNotice_amt(cwbOrder.getReceivablefee().toString());

		if ((delivery_state == DeliveryStateEnum.PeiSongChengGong.getValue() || delivery_state == DeliveryStateEnum.ShangMenHuanChengGong.getValue() || delivery_state == DeliveryStateEnum.ShangMenTuiChengGong
				.getValue()) && cwbOrder.getReceivablefee().compareTo(BigDecimal.ZERO) > 0) {
			xmlnote.setCod(FunctionForHappy.HOPE000020.getText());
			B2CData b2c = new B2CData();
			JSONObject datajson = JSONObject.fromObject(xmlnote);
			b2c.setJsoncontent(datajson.toString());
			b2c.setSend_b2c_flag(0);
			b2c.setCwb(orderFlow.getCwb());
			b2c.setCustomerid(Long.valueOf(happy.getCustomerid()));
			b2c.setFlowordertype(36);
			b2c.setDelId(cwbOrderWothDeliverystate.getDeliveryState().getDeliveryid());
			b2c.setPosttime(DateTimeUtil.formatDate(orderFlow.getCredate()));
			b2CDataDAO.saveB2CData(b2c, "null");
		}

		if (cwbOrder.getRemark2().contains("push")) {
			xmlnote.setCod(FunctionForHappy.HOPE000014.getText());// 送货结束登记接口
			if (cwbOrder.getCommoncwb() != null && cwbOrder.getCommoncwb().length() > 0) {// 说明是换货
				xmlnote.setWb_no(cwbOrder.getCwb());
				xmlnote.setOrder_no(cwbOrder.getCommoncwb());

			} else {
				xmlnote.setWb_no(cwbOrder.getCwb());
				xmlnote.setOrder_no(cwbOrder.getTranscwb());
			}
		} else {
			xmlnote.setCod(FunctionForHappy.HOPE000010.getText());
		}

		return xmlnote;
	}

}
