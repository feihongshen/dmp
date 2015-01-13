package cn.explink.service;

import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;

@Service
public class SmtService {

	@Autowired
	private CwbDAO cwbDAO = null;

	@Autowired
	private OrderFlowDAO orderFlowDAO = null;

	@Autowired
	private DeliveryStateDAO deliverStateDAO = null;

	@Transactional
	public JSONObject smtOrderOutArea(String[] cwbs, long curBranchId, long curUserId) {
		JSONObject errorObj = this.validateOutArea(cwbs);
		if (!errorObj.getBoolean("successed")) {
			return errorObj;
		}
		// 领货的数据可以进行分站到货.
		Map<String, Long> branchMap = this.getCwbDAO().getImprotDataBranchMap(cwbs);
		// 更新订单表设定订单状态为超区.
		this.getCwbDAO().updateOrderOutAreaStatus(cwbs, branchMap);
		// 更新订单流程表加入超区流程.
		// 存在多次超区可能需要修改超区流程的isnow = 1.
		this.getOrderFlowDAO().batchOutArea(cwbs, curBranchId, curUserId, branchMap);

		return errorObj;
	}

	private JSONObject validateOutArea(String[] cwbs) {
		JSONObject obj = new JSONObject();
		obj.put("successed", true);
		obj.put("msg", "");
		if (cwbs.length != 1) {
			return obj;
		}
		String cwb = cwbs[0];
		CwbOrder order = this.getCwbDAO().getCwbByCwb(cwb);
		if (order == null) {
			obj.put("successed", false);
			obj.put("msg", "订单不存在");
		} else {
			DeliveryState deliverSate = this.getDeliverStateDAO().getDeliveryByCwb(cwb);
			FlowOrderTypeEnum flowOrderType = FlowOrderTypeEnum.getText(order.getFlowordertype());
			boolean cond1 = FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.equals(flowOrderType);
			boolean cond2 = FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.equals(flowOrderType);
			boolean cond3 = DeliveryStateEnum.FenZhanZhiLiu.getValue() == deliverSate.getDeliverystate();
			if (!(cond1 || cond2 || cond3)) {
				obj.put("successed", false);
				obj.put("msg", this.getOutAreaFlowErrorMsg());
			}
		}

		return obj;
	}

	private String getOutAreaFlowErrorMsg() {
		return "订单状态为[分站到货][到错货][分站滞留]才允许上报超区.";
	}

	private CwbDAO getCwbDAO() {
		return this.cwbDAO;
	}

	private OrderFlowDAO getOrderFlowDAO() {
		return this.orderFlowDAO;
	}

	private DeliveryStateDAO getDeliverStateDAO() {
		return this.deliverStateDAO;
	}

}
