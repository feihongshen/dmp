package cn.explink.service;

import java.util.ArrayList;
import java.util.List;
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
import cn.explink.enumutil.CwbFlowOrderTypeEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
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
		//Added by leoliao at 20160218 解决上报超区之后订单表下一站为null的问题
		if(cwbs == null || cwbs.length <= 0){
			JSONObject obj = new JSONObject();
			obj.put("successed", false);
			obj.put("msg", "订单号为空");
			return obj;
		}
			
		List<String> listCwb = new ArrayList<String>();
		for(String cwb : cwbs){			
			if(cwb == null || cwb.trim().equals("")){
				continue;
			}
			
			listCwb.add(cwb.trim());
		}
		
		String[] arrCwb = listCwb.toArray(new String[listCwb.size()]);
		//Added by leoliao at 20160218 end
		
		JSONObject errorObj = this.validateOutArea(arrCwb);
		if (!errorObj.getBoolean("successed")) {
			return errorObj;
		}
		// 领货的数据可以进行分站到货.
		Map<String, Long> branchMap = this.getCwbDAO().getImprotDataBranchMap(arrCwb);
		// 更新订单表设定订单状态为超区.
		this.getCwbDAO().updateOrderOutAreaStatus(arrCwb, branchMap);
		// 更新订单流程表加入超区流程.
		// 存在多次超区可能需要修改超区流程的isnow = 1.
		this.getOrderFlowDAO().batchOutArea(arrCwb, curBranchId, curUserId, branchMap);

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
		} else if(CwbOrderTypeIdEnum.Shangmentui.getValue() != order.getCwbordertypeid()){
			obj.put("successed", false);
			obj.put("msg", "非上门退订单，不允许做超区");
		}else {
			DeliveryState deliverSate = this.getDeliverStateDAO().getDeliveryByCwb(cwb);
			FlowOrderTypeEnum flowOrderType = FlowOrderTypeEnum.getText(order.getFlowordertype());
			boolean cond1 = FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.equals(flowOrderType);
			boolean cond2 = FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.equals(flowOrderType);
			boolean cond3 = DeliveryStateEnum.FenZhanZhiLiu.getValue() == deliverSate.getDeliverystate();
			boolean cond4 = FlowOrderTypeEnum.FenZhanLingHuo.equals(flowOrderType);
			if (!(cond1 || cond2 || cond3 || cond4)) {
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
