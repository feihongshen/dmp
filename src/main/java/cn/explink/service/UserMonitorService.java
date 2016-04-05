package cn.explink.service;

import java.util.List;

import net.sf.json.JSONObject;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.UserDAO;
import cn.explink.domain.User;

@Service
public class UserMonitorService {

	@Produce(uri = "jms:topic:userMonitor")
	ProducerTemplate userMonitorProducerTemplate;
	private Logger logger = LoggerFactory.getLogger(UserMonitorService.class);

	@Autowired
	private UserDAO userDAO;

	/**
	 * 监控 员工设置的变化 根据userid
	 * 
	 * @param user
	 */
	public void userMonitorById(long userid) {
		try {
			User user = null;
			JSONObject us = null;
			if (userid > 0) {
				user = userDAO.getAllUserByid(userid);
				us = buildUserJson(user);
			}
			if (us != null) {
				this.send(us.toString());
				logger.info("监听一个用户send jms,userid:{}", userid);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	/**
	 * 监控 员工设置的变化 根据username
	 * 
	 * @param user
	 */
	public void userMonitorByUsername(String username) {
		try {
			User user = null;

			JSONObject us = null;
			if (username != null && !username.isEmpty()) {
				List<User> userlist = userDAO.getUsersByUsername(username);
				user = userlist.get(0);
				us = buildUserJson(user);

			}
			if (us != null) {
				this.send(us.toString());
				logger.info("监听一个用户send jms,username:{}", username);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	private JSONObject buildUserJson(User user) {
		JSONObject us = new JSONObject();

		us.put("branchid", user.getBranchid());
		us.put("employeestatus", user.getEmployeestatus());
		// us.put("employeestatusName", user.getEmployeestatusName());
		us.put("idcardno", user.getIdcardno());
		us.put("realname", user.getRealname());
		us.put("roleid", user.getRoleid());
		us.put("showphoneflag", user.getShowphoneflag());
		us.put("userDeleteFlag", user.getUserDeleteFlag());
		us.put("useraddress", user.getUseraddress());
		us.put("usercustomerid", user.getUsercustomerid());
		us.put("useremail", user.getUseremail());
		us.put("userid", user.getUserid());
		us.put("usermobile", user.getUsermobile());
		us.put("username", user.getUsername());
		us.put("password", user.getPassword());
		us.put("userphone", user.getUserphone());
		us.put("userremark", user.getUserremark());
		us.put("usersalary", user.getUsersalary());
		us.put("userwavfile", user.getUserwavfile());
		us.put("deliverManCode", user.getDeliverManCode());
		return us;
	}

	public void send(String parms) {

		try {
			userMonitorProducerTemplate.sendBodyAndHeader(null, "userMonitor", parms);
		} catch (Exception ee) {

			logger.error("send userMonitor message error", ee);
		}
	}

}
