package cn.explink.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import cn.explink.enumutil.CustomerAutoArrivalBranEnum;
import cn.explink.enumutil.CwbFlowOrderTypeEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.VipExchangeFlagEnum;

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
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public void branchAutointoWarhouse(CwbOrder cwbOrder, Branch branch) {

		if (cwbOrder.getCwbordertypeid() != CwbOrderTypeIdEnum.Shangmentui.getValue()) {
			return;
		}

		Customer customer = this.customerDAO.getCustomerById(cwbOrder.getCustomerid());
		//如果没有找到客户信息，或者客户没有开启揽退单自动到货，那么就直接结束 ---刘武强20161026
		if (customer == null || (customer.getAutoArrivalBranchFlag() != CustomerAutoArrivalBranEnum.kaiqi.getValue()&&cwbOrder.getExchangeflag()==VipExchangeFlagEnum.NO.getValue())) {
			this.logger.info("cwb={},揽退单是否自动到货标志：AutoArrivalBranchFlag={},结束自动到货", cwbOrder.getCwb(), customer.getAutoArrivalBranchFlag());
			return;
		}
		
		this.logger.info("自动到货cwb={},b2cenum={}", cwbOrder.getCwb(), customer.getB2cEnum());
		
		//不论是唯品会的揽退单，还是其他客户的揽退单，都应该做自动到货 -----刘武强20161017
/*		if ((customer.getB2cEnum() != null) && !customer.getB2cEnum().equals(this.getB2cEnumKeys(customer, "vipshop"))) {
			return;
		}*/

		if ((cwbOrder.getFlowordertype() == CwbFlowOrderTypeEnum.WeiDaoHuo.getValue()) || (cwbOrder.getFlowordertype() == CwbFlowOrderTypeEnum.TiHuo.getValue())
				|| (cwbOrder.getFlowordertype() == CwbFlowOrderTypeEnum.TiHuoYouHuoWuDan.getValue())
				||(cwbOrder.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()
						&&cwbOrder.getExchangeflag()==VipExchangeFlagEnum.YES.getValue())) {

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
