package cn.explink.b2c.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.User;

/**
 * 对基础信息进行缓存
 * 
 * @author Administrator
 *
 */
@Service
public class CacheBaseListenerImp implements CacheBaseListener, ApplicationListener<ContextRefreshedEvent> {

	private Logger logger = LoggerFactory.getLogger(CacheBaseListenerImp.class);

	@Autowired
	GetDmpDAO getDmpdao;

	private static List<Customer> customerlist = new ArrayList<Customer>();
	private static List<Branch> branchlist = new ArrayList<Branch>();
	private static List<User> userlist = new ArrayList<User>();

	public void initCustomerList() {
		customerlist = getDmpdao.getAllCustomers();
		logger.info("初始化customer成功");
	}

	public void initBranchList() {
		branchlist = getDmpdao.getAllBranchs();
		logger.info("初始化branch成功");
	}

	public void initUserList() {
		userlist = getDmpdao.getAllUsers();
		logger.info("初始化user成功");
	}

	@Override
	public void initAll() {
		customerlist = getDmpdao.getAllCustomers();
		branchlist = getDmpdao.getAllBranchs();
		userlist = getDmpdao.getAllUsers();
		logger.info("初始化oms基础数据成功");
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// /this.initAll();
	}

	@Override
	public void onChange(Map<String, String> parameters) {
		
		
		if (parameters == null) {
			this.initAll();
		}
		logger.info("oms基础信息同步成功,key={}",parameters.keySet());
		
		if (parameters.keySet().contains("customer")) {
			this.initCustomerList();
		}
		if (parameters.keySet().contains("branch")) {
			this.initBranchList();
		}
		if (parameters.keySet().contains("user")) {
			this.initUserList();
		}
	}

	private List<Customer> getCustomerList() {
		if (customerlist.size() == 0) {
			this.initCustomerList();
		}
		return customerlist;
	}

	private List<Branch> getBranchList() {
		if (branchlist.size() == 0) {
			this.initBranchList();
		}
		return branchlist;
	}

	private List<User> getUserList() {
		if (userlist.size() == 0) {
			this.initUserList();
		}
		return userlist;
	}

	@Override
	public Customer getCustomer(long customerid) {
		try {

			for (Customer cust : getCustomerList()) {
				if (cust.getCustomerid() == customerid) {
					return cust;
				}
			}

		} catch (Exception e) {
			logger.error("获取供货商异常", e);
		}
		return null;
	}

	@Override
	public Branch getBranch(long branchid) {
		try {

			for (Branch branch : this.getBranchList()) {
				if (branch.getBranchid() == branchid) {
					return branch;
				}
			}

		} catch (Exception e) {
			logger.error("获取站点信息异常", e);
		}
		return null;
	}

	@Override
	public User getUser(long userid) {
		try {

			for (User user : this.getUserList()) {
				if (user.getUserid() == userid) {
					return user;
				}
			}

		} catch (Exception e) {
			logger.error("获取员工信息异常", e);
		}
		return null;
	}

}
