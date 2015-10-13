package cn.explink.b2c.liantong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2cDataOrderFlowDetail;
import cn.explink.b2c.tools.JacksonMapper;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.Customer;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.jms.dto.CwbOrderWithDeliveryState;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.jms.dto.DmpDeliveryState;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.DateTimeUtil;

@Service
public class BulidLiantongB2cData {

	/**
	 * 构建联通对象 并存储
	 */
	@Autowired
	LiantongService liantongService;
	@Autowired
	LiantongDAO liantongDAO;
	@Autowired
	GetDmpDAO getDmpdao;
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public String buildB2cData(CwbOrderWithDeliveryState cwbOrderWothDeliverystate, DmpOrderFlow orderFlow, DmpCwbOrder cwbOrder, long flowOrdertype, long delivery_state, Customer customer) {

		try {

			saveLiantongDatas(orderFlow, cwbOrder, flowOrdertype, delivery_state); // 存入联通查询表

			String flowStatus = liantongService.getFlowEnum(flowOrdertype, delivery_state);
			if (flowStatus == null) {
				logger.info("订单{}不属于要存储到b2cdata表的数据,flowordertype={}", cwbOrder.getCwb(), orderFlow.getFlowordertype());
				return null;
			}

			String acceptName = ""; // 签收人
			DmpDeliveryState dmpDeliveryState = cwbOrderWothDeliverystate.getDeliveryState();
			if (dmpDeliveryState != null
					&& (dmpDeliveryState.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue()
							&& dmpDeliveryState.getDeliverystate() == DeliveryStateEnum.ShangMenHuanChengGong.getValue() && dmpDeliveryState.getDeliverystate() == DeliveryStateEnum.ShangMenTuiChengGong
							.getValue())) {
				acceptName = cwbOrder.getConsigneename();
			}

			LiantongXMLNote liantongXMLNote = new LiantongXMLNote();
			liantongXMLNote.setOrder(cwbOrder.getCwb());
			liantongXMLNote.setMailNo(cwbOrder.getTranscwb());
			liantongXMLNote.setAcceptTime(DateTimeUtil.formatDate(orderFlow.getCredate()));
			liantongXMLNote.setAcceptName(acceptName);
			liantongXMLNote.setAcceptAddress(orderFlowDetail.getDetail(orderFlow));
			liantongXMLNote.setAcceptState(flowStatus);

			return JacksonMapper.getInstance().writeValueAsString(liantongXMLNote);

		} catch (Exception e) {
			logger.error("联通对接处理失败", e);
			return null;
		}

	}

	/**
	 * 存入联通查询表
	 * 
	 * @param orderFlow
	 * @param cwbOrder
	 * @param flowOrdertype
	 */
	private void saveLiantongDatas(DmpOrderFlow orderFlow, DmpCwbOrder cwbOrder, long flowOrdertype, long delivery_state) {

		try {
			String trackText = liantongService.getTrackEnum(flowOrdertype, delivery_state);

			if (trackText == null) {
				logger.warn("当前不属于存储[联通]所需要的状态cwb=" + orderFlow.getCwb() + ",orderFlow=" + orderFlow.getFlowordertype());
				return;
			}
			LiantongEntity lt = new LiantongEntity();
			lt.setCwb(orderFlow.getCwb());
			lt.setTranscwb(cwbOrder.getTranscwb());
			lt.setAcceptTime(DateTimeUtil.formatDate(orderFlow.getCredate()));
			lt.setAcceptAddress(getDmpdao.getNowBranch(orderFlow.getBranchid()).getBranchname());
			lt.setAcceptAction(orderFlowDetail.getDetail(orderFlow));
			lt.setAcceptName(getDmpdao.getUserById(orderFlow.getUserid()).getRealname());
			lt.setFlowordertype(flowOrdertype);
			liantongDAO.save(lt);
		} catch (Exception e) {
			logger.error("存入查询表异常，联通cwb=" + orderFlow.getCwb(), e);
		}
	}

}
