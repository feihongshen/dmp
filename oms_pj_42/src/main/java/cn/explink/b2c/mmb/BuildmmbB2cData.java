package cn.explink.b2c.mmb;

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
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.JacksonMapper;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.User;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.jms.dto.CwbOrderWithDeliveryState;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.jms.dto.DmpDeliveryState;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.DateTimeUtil;

@Service
public class BuildmmbB2cData {
	private Logger logger = LoggerFactory.getLogger(BuildmmbB2cData.class);
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;

	@Autowired
	GetDmpDAO getDmpdao;
	@Autowired
	MmbService mmbService;
	@Autowired
	private B2cTools b2ctools;

	public String BuildmmbMethod(DmpOrderFlow orderFlow, long flowOrdertype, DmpCwbOrder cwbOrder, long delivery_state, CwbOrderWithDeliveryState cwbOrderWothDeliverystate, ObjectMapper objectMapper)
			throws IOException, JsonGenerationException, JsonMappingException {

		MmbFlowEnum mmbFlowEnum = mmbService.getFlowStateBymmb(flowOrdertype, delivery_state);
		String mmbState = mmbFlowEnum == null ? null : mmbFlowEnum.getMmbcode();

		if (mmbState == null) {
			logger.warn("该流程不属于买卖宝推送流程cwb={},deliverystate={}", cwbOrder.getCwb(), cwbOrderWothDeliverystate);
			return null;
		}
		logger.info("订单号：{}封装成0买卖宝0所需要的json----开始,状态：{}", cwbOrder.getCwb(), flowOrdertype);
		Mmb mmb = mmbService.getMmb(B2cEnum.MaiMaiBao.getKey());

		Branch branch = getDmpdao.getNowBranch(orderFlow.getBranchid());

		MmbJSONNote mmbJSONNote = new MmbJSONNote();
		String message = getMessage(orderFlow, cwbOrder, cwbOrderWothDeliverystate, mmbState, mmb);
		mmbJSONNote.setKey(mmb.getKey());
		mmbJSONNote.setNu(orderFlow.getCwb());
		mmbJSONNote.setTime(DateTimeUtil.formatDate(orderFlow.getCredate()));
		mmbJSONNote.setStatus(mmbState);
		mmbJSONNote.setMessage(message);

		mmbJSONNote.setProvince(branch.getBranchprovince());
		mmbJSONNote.setCity(branch.getBranchcity());
		mmbJSONNote.setDistrict(branch.getBrancharea());

		// jsonContent=JacksonMapper.getInstance().writeValueAsString(mmbJSONNote);

		logger.info("订单号：{}封装成0买卖宝0所需要的json----结束,状态：{}", cwbOrder.getCwb(), cwbOrder.getFlowordertype());
		return objectMapper.writeValueAsString(mmbJSONNote);

	}

	private String getMessage(DmpOrderFlow orderFlow, DmpCwbOrder cwbOrder, CwbOrderWithDeliveryState cwbOrderWothDeliverystate, String mmbState, Mmb mmb) {
		String message = "";
		if ("1".equals(mmbState)) {
			message = "快件已由快递公司揽收";
		} else if ("2".equals(mmbState)) {
			Branch branch = getDmpdao.getNowBranch(cwbOrder.getNextbranchid());
			message = "已到达" + mmb.getCompanyname() + "分拣中心,下一站将发往" + branch.getBranchname();
		} else if ("4".equals(mmbState)) {
			Branch branch = getDmpdao.getNowBranch(orderFlow.getBranchid());
			message = "到达" + mmb.getCompanyname() + branch.getBranchname() + "站，等待配送员投递";
		} else if ("5".equals(mmbState)) {
			User deliverUser = getDmpdao.getUserById(cwbOrderWothDeliverystate.getDeliveryState().getDeliveryid());
			message = mmb.getCompanyname() + "配送员" + deliverUser.getRealname() + "已经出发投递，电话: " + deliverUser.getUsermobile();
		} else if ("6".equals(mmbState)) {
			message = "货物由" + mmb.getCompanyname() + "退供货商成功";
		} else if ("7".equals(mmbState)) {
			DmpDeliveryState deliverstate = cwbOrderWothDeliverystate.getDeliveryState();
			User deliverUser = getDmpdao.getUserById(cwbOrderWothDeliverystate.getDeliveryState().getDeliveryid());
			message = "货物已由[" + getDmpdao.getNowBranch(orderFlow.getBranchid()).getBranchname() + "]的小件员[" + deliverUser.getRealname() + "]反馈为["
					+ DeliveryStateEnum.getByValue((int) deliverstate.getDeliverystate()).getText() + "]";
		}
		return message;
	}

	public static void main(String[] args) {
		String transcwb = "";
		String cwb = "1111";

		String way_no = transcwb == null || transcwb.isEmpty() ? cwb : transcwb;
		System.out.println(way_no);
	}

}
