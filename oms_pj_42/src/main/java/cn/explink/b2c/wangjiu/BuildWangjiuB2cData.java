package cn.explink.b2c.wangjiu;

import java.io.IOException;
import java.sql.Timestamp;

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
import cn.explink.domain.Branch;
import cn.explink.domain.User;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.jms.dto.DmpDeliveryState;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.DateTimeUtil;

@Service
public class BuildWangjiuB2cData {
	private Logger logger = LoggerFactory.getLogger(BuildWangjiuB2cData.class);
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;

	@Autowired
	GetDmpDAO getDmpdao;
	@Autowired
	WangjiuService wangjiuService;
	@Autowired
	WangjiuDAO wangjiuDAO;

	public String buildWangjiuMethod(DmpOrderFlow orderFlow, long flowOrdertype, DmpCwbOrder cwbOrder, long delivery_state, DmpDeliveryState dmpDeliveryState, ObjectMapper objectMapper)
			throws IOException, JsonGenerationException, JsonMappingException {

		saveWangjiuFlow(orderFlow, cwbOrder, flowOrdertype, delivery_state); // 存入联通查询表

		String dmsReceiveStatus = wangjiuService.getFlowEnum(flowOrdertype, delivery_state);
		if (dmsReceiveStatus == null) {
			logger.info("订单号：{} 不属于[网酒]所需要的json---,状态{}，return", cwbOrder.getCwb(), flowOrdertype);
			return null;
		}

		WangjiuXmlNote note = new WangjiuXmlNote();

		note.setMailNo(orderFlow.getCwb());
		note.setInfoContent(dmsReceiveStatus);

		User operatorUser = getDmpdao.getUserById(orderFlow.getUserid());
		note.setName(operatorUser.getRealname());
		note.setAcceptTime(Timestamp.valueOf(DateTimeUtil.getNowTime()));

		return objectMapper.writeValueAsString(note);

	}

	/**
	 * 存入网酒网跟踪查询表
	 * 
	 * @param orderFlow
	 * @param cwbOrder
	 * @param flowOrdertype
	 */
	private void saveWangjiuFlow(DmpOrderFlow orderFlow, DmpCwbOrder cwbOrder, long flowOrdertype, long delivery_state) {

		try {
			String trackText = wangjiuService.getTrackEnum(flowOrdertype, delivery_state);

			if (trackText == null) {
				logger.warn("当前不属于存储网酒网所需要的状态cwb=" + orderFlow.getCwb() + ",orderFlow=" + orderFlow.getFlowordertype());
				return;
			}
			Branch operatorBranch = getDmpdao.getNowBranch(orderFlow.getBranchid());
			User operatorUser = getDmpdao.getUserById(orderFlow.getUserid());
			WangjiuEntity lt = new WangjiuEntity();
			lt.setMailNo(orderFlow.getCwb());
			lt.setOrderStatus(trackText);
			lt.setAcceptTime(orderFlow.getCredate());
			lt.setAcceptAddress(operatorBranch.getBranchname());
			lt.setStatus("true");
			lt.setName(operatorUser.getRealname());
			lt.setRemark(orderFlowDetail.getDetail(orderFlow));
			lt.setFlowordertype(flowOrdertype);

			wangjiuDAO.save(lt);
		} catch (Exception e) {
			logger.error("存入查询表异常，联通cwb=" + orderFlow.getCwb(), e);
		}
	}

}
