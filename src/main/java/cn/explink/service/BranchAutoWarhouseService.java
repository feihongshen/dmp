package cn.explink.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.CwbDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;

@Service
public class BranchAutoWarhouseService {
	@Autowired
	private CwbDAO cwbDAO;
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	UserDAO userDAO;

	public void branchAutointoWarhouse(CwbOrder cwbOrder, Branch branch) {

		// if ((cwbOrder.getFlowordertype() ==
		// CwbFlowOrderTypeEnum.WeiDaoHuo.getValue()) ||
		// (cwbOrder.getFlowordertype() ==
		// CwbFlowOrderTypeEnum.TiHuo.getValue())
		// || (cwbOrder.getFlowordertype() ==
		// CwbFlowOrderTypeEnum.TiHuoYouHuoWuDan.getValue())) {
		//
		// this.cwbDAO.updateDeliveryBranchid(branch.getBranchname(),
		// branch.getBranchid(), cwbOrder.getCwb(), null);
		// } else {
		// this.cwbDAO.updateDeliveryBranchidAndNextbranchid(branch.getBranchname(),
		// branch.getBranchid(), cwbOrder.getCwb(), null);
		// }

		// 调用分站到货扫描
		User user = this.userDAO.getAllUserByid(1l); //
		String scancwb = cwbOrder.getCwb();
		String cwb = this.cwbOrderService.translateCwb(cwbOrder.getCwb());
		this.cwbOrderService.substationGoods(user, cwb, scancwb, 0, 0, "", "0", false);

	}
}
