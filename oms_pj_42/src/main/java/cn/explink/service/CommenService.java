package cn.explink.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import cn.explink.domain.Branch;
import cn.explink.domain.Common;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.CwbOrderAll;
import cn.explink.enumutil.CwbOrderTypeIdEnum;

@Service
public class CommenService {

	public List<CwbOrderAll> getCwborderView(List<CwbOrder> cList, List<Customer> customers, List<Branch> branchs, List<Common> commons, List<CustomWareHouse> customWareHouses, String commoncode) {
		List<CwbOrderAll> cwbOrderAlls = new ArrayList<CwbOrderAll>();
		if (cList != null && cList.size() > 0) {
			for (CwbOrder cwb : cList) {
				CwbOrderAll ca = new CwbOrderAll();
				ca.setCwb(cwb.getCwb());
				ca.setCommonname(getCommenName(commons, cwb.getCommonid()));
				ca.setEmaildate(cwb.getEmaildate());
				ca.setOutstoreroomtime(cwb.getOutstoreroomtime());
				ca.setCwborderTypeName(getOrderTypeName(Long.parseLong(cwb.getCwbordertypeid())));
				ca.setFlowordertype(cwb.getFlowordertype());
				ca.setCustomerwarehousename(getWareshouseName(customWareHouses, Long.parseLong(cwb.getCustomerwarehouseid())));
				ca.setStartbranchname(getbranchame(branchs, cwb.getStartbranchid()));
				ca.setNextbranchname(getbranchame(branchs, cwb.getNextbranchid()));
				ca.setCustomername(getCustomerName(customers, cwb.getCustomerid()));
				ca.setCommonnumber(commoncode);
				cwbOrderAlls.add(ca);
			}
		}

		return cwbOrderAlls;
	}

	private String getWareshouseName(List<CustomWareHouse> customWareHouses, long customerwarehouseid) {
		String name = "";
		for (CustomWareHouse customWareHouse : customWareHouses) {
			if (customWareHouse.getWarehouseid() == customerwarehouseid) {
				name = customWareHouse.getCustomerwarehouse();
				break;
			}
		}

		return name;
	}

	private String getCustomerName(List<Customer> customers, long customerid) {
		String name = "";
		for (Customer customer : customers) {
			if (customer.getCustomerid() == customerid) {
				name = customer.getCustomername();
				break;
			}

		}
		return name;
	}

	private String getbranchame(List<Branch> branchs, Long customerwarehouseid) {
		String name = "";
		for (Branch branch : branchs) {
			if (branch.getBranchid() == customerwarehouseid) {
				name = branch.getBranchname();
				break;
			}
		}
		return name;
	}

	private String getOrderTypeName(long cwbordertypeid) {
		String typename = "";
		for (CwbOrderTypeIdEnum cotie : CwbOrderTypeIdEnum.values()) {
			if (cotie.getValue() == cwbordertypeid) {
				typename = cotie.getText();
				break;
			}

		}
		return typename;
	}

	private String getCommenName(List<Common> commons, long commonid) {
		String nameStr = "";
		for (Common common : commons) {
			if (common.getId() == commonid) {
				nameStr = common.getCommonname();
				break;
			}
		}
		return nameStr;
	}

	/**
	 * 根据 cwb 查询赋值
	 * 
	 * @param cList
	 * @param customers
	 * @param branchs
	 * @param commons
	 * @param customWareHouses
	 * @param cwbForcommonMap
	 * @return
	 */
	public List<CwbOrderAll> getCwborderViewForCwbs(List<CwbOrder> cList, List<Customer> customers, List<Branch> branchs, List<Common> commons, List<CustomWareHouse> customWareHouses,
			Map<String, String> cwbForcommonMap) {
		List<CwbOrderAll> cwbOrderAlls = new ArrayList<CwbOrderAll>();
		if (cList != null && cList.size() > 0) {
			for (CwbOrder cwb : cList) {
				CwbOrderAll ca = new CwbOrderAll();
				ca.setCwb(cwb.getCwb());
				ca.setCommonname(getCommenName(commons, cwb.getCommonid()));
				ca.setEmaildate(cwb.getEmaildate());
				ca.setOutstoreroomtime(cwb.getOutstoreroomtime());
				ca.setCwborderTypeName(getOrderTypeName(Long.parseLong(cwb.getCwbordertypeid())));
				ca.setFlowordertype(cwb.getFlowordertype());
				ca.setCustomerwarehousename(getWareshouseName(customWareHouses, Long.parseLong(cwb.getCustomerwarehouseid())));
				ca.setStartbranchname(getbranchame(branchs, cwb.getStartbranchid()));
				ca.setNextbranchname(getbranchame(branchs, cwb.getNextbranchid()));
				ca.setCustomername(getCustomerName(customers, cwb.getCustomerid()));
				ca.setCommonnumber(cwbForcommonMap.get(cwb.getCwb()));
				cwbOrderAlls.add(ca);
			}
		}

		return cwbOrderAlls;
	}

}
