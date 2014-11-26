package cn.explink.controller;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.DeliveryState;

@Controller
@RequestMapping("/checkbranch")
public class CheckBranchController {
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	BranchDAO BranchDAO;

	/**
	 * 校验最后一次反馈站点接口
	 * 
	 * @param model
	 * @param orderNo
	 *            订单号
	 * @param userid
	 *            用户ID
	 * @return
	 */
	@RequestMapping("/address")
	public @ResponseBody String address(Model model, @RequestParam(value = "orderNo", required = true, defaultValue = "") String orderNo,
			@RequestParam(value = "userid", required = true, defaultValue = "0") long userid) {
		String station = "";// 站点名称
		String stationid = ""; // 站点ID
		JSONObject jsonObject = new JSONObject();
		// 查询反馈表
		DeliveryState deliveryState = deliveryStateDAO.getActiveDeliveryStateByCwb(orderNo);
		if (deliveryState != null && deliveryState.getDeliverybranchid() > 0) {
			Branch branch = BranchDAO.getBranchByBranchid(deliveryState.getDeliverybranchid());
			if (branch != null) {
				station = branch.getBranchname();
				stationid = String.valueOf(deliveryState.getDeliverybranchid());
			}
		}

		jsonObject.put("userid", userid);
		jsonObject.put("orderNo", orderNo);
		jsonObject.put("station", station);
		jsonObject.put("stationid", stationid);

		return jsonObject.toString();
	}

}
