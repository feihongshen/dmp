package cn.explink.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.controller.AbnormalView;
import cn.explink.dao.AbnormalOrderDAO;
import cn.explink.dao.AbnormalTypeDAO;
import cn.explink.dao.AbnormalWriteBackDAO;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.AbnormalType;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;
import cn.explink.enumutil.FlowOrderTypeEnum;

@Service
public class AbnormalService {
	@Autowired
	AbnormalTypeDAO abnormalTypeDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	AbnormalOrderDAO abnormalOrderDAO;
	@Autowired
	AbnormalWriteBackDAO abnormalWriteBackDAO;

	public List<AbnormalView> setViews(List<JSONObject> alist, List<Branch> branchs, List<User> users, List<Customer> customers, List<AbnormalType> atlist) {

		List<AbnormalView> views = new ArrayList<AbnormalView>();
		if (alist.size() > 0) {

			for (JSONObject a : alist) {
				AbnormalView view = new AbnormalView();
				view.setId(a.getInt("id"));
				view.setFlowordertype(this.getFloworderType(a.getLong("flowordertype")));
				view.setAbnormalType(this.getAbnormalType(atlist, a.getLong("abnormaltypeid")));
				view.setBranchName(this.getBranchName(branchs, a.getLong("branchid")));
				view.setCreuserName(this.getCreName(users, a.getLong("creuserid")));
				view.setCwb(a.getString("cwb"));
				view.setCustomerName(this.getCustomer(customers, a.getLong("customerid")));
				view.setDescribe(a.getString("describe"));
				view.setEmaildate(a.getString("emaildate"));
				view.setIshandle(a.getLong("ishandle"));
				views.add(view);
			}
		}
		return views;
	}

	private String getCustomer(List<Customer> customers, long long1) {
		String customername = "";
		for (Customer customer : customers) {
			if (customer.getCustomerid() == long1) {
				customername = customer.getCustomername();
				break;
			}
		}
		return customername;
	}

	private String getCreName(List<User> users, long userid) {
		String username = "";
		for (User user : users) {
			if (user.getUserid() == userid) {
				username = user.getRealname();
				break;
			}
		}

		return username;
	}

	private String getBranchName(List<Branch> branchs, long long1) {
		String branchname = "";
		for (Branch branch : branchs) {
			if (branch.getBranchid() == long1) {
				branchname = branch.getBranchname();
				break;
			}
		}
		return branchname;
	}

	private String getAbnormalType(List<AbnormalType> atlist, long long1) {
		String type = "";
		for (AbnormalType abnormalType : atlist) {
			if (abnormalType.getId() == long1) {
				type = abnormalType.getName();
				break;
			}
		}
		return type;
	}

	private String getFloworderType(long type) {
		return FlowOrderTypeEnum.getText(type).getText();
	}

	/**
	 * 创建
	 * 
	 * @param co
	 * @param user
	 * @param abnormaltypeid
	 * @param nowtime
	 * @param mapForAbnormalorder
	 */
	@Transactional
	public void creAbnormalOrder(CwbOrder co, User user, long abnormaltypeid, String nowtime, Map<Long, JSONObject> mapForAbnormalorder, long action) {
		// long abnormalorderid =
		// abnormalOrderDAO.creAbnormalOrderLong(co.getOpscwbid(),
		// co.getCustomerid(), "", user.getUserid(), user.getBranchid(),
		// abnormaltypeid, nowtime);
		long abnormalorderid = this.abnormalOrderDAO.creAbnormalOrderLong(co, "", user.getUserid(), user.getBranchid(), abnormaltypeid, nowtime);
		this.abnormalWriteBackDAO.creAbnormalOrder(co.getOpscwbid(), "", user.getUserid(), action, nowtime, abnormalorderid, abnormaltypeid, co.getCwb());
		JSONObject json = new JSONObject();
		json.put("abnormalorderid", abnormalorderid);
		json.put("abnormalordertype", abnormaltypeid);
		mapForAbnormalorder.put(co.getOpscwbid(), json);
	}

}
