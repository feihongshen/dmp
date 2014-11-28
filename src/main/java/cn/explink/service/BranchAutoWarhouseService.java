package cn.explink.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;
import cn.explink.enumutil.CwbFlowOrderTypeEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;

@Service
public class BranchAutoWarhouseService {
	@Autowired
	private CwbDAO cwbDAO;
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	UserDAO userDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	JointService jointService;

	public void branchAutointoWarhouse(CwbOrder cwbOrder, Branch branch) {

		if (cwbOrder.getCwbordertypeid() != CwbOrderTypeIdEnum.Shangmentui.getValue()) {
			return;
		}

		Customer customer = this.customerDAO.getCustomerById(cwbOrder.getCustomerid());
		if ((customer.getB2cEnum() != null) && !customer.getB2cEnum().equals(this.getB2cEnumKeys(customer, "vipshop"))) {
			return;
		}

		if ((cwbOrder.getFlowordertype() == CwbFlowOrderTypeEnum.WeiDaoHuo.getValue()) || (cwbOrder.getFlowordertype() == CwbFlowOrderTypeEnum.TiHuo.getValue())
				|| (cwbOrder.getFlowordertype() == CwbFlowOrderTypeEnum.TiHuoYouHuoWuDan.getValue())) {

			// 调用分站到货扫描
			User user = this.getOperatorUser(branch);

			String scancwb = cwbOrder.getCwb();
			String cwb = this.cwbOrderService.translateCwb(cwbOrder.getCwb());
			this.cwbOrderService.substationGoods(user, cwb, scancwb, 0, 0, "", "0", false);
		}

	}

	private User getOperatorUser(Branch branch) {
		List<User> userlist = this.userDAO.getAllUserbybranchid(branch.getBranchid()); //
		User user = null;
		for (User u : userlist) {
			if (u.getRoleid() == 4) {
				user = u;
			}
		}
		if (user == null) {
			user = userlist.get(0);
		}
		return user;
	}

	public String getB2cEnumKeys(Customer customer, String constainsStr) {
		for (B2cEnum enums : B2cEnum.values()) {
			if (enums.getMethod().contains(constainsStr)) {
				if (customer.getB2cEnum().equals(String.valueOf(enums.getKey()))) {
					int isOpenFlag = this.jointService.getStateForJoint(enums.getKey());
					if (isOpenFlag == 1) {
						return String.valueOf(enums.getKey());
					}
					return null;

				}
			}
		}
		return null;
	}
}
