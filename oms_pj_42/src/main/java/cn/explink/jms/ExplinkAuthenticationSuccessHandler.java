package cn.explink.jms;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.DepartDAO;
import cn.explink.domain.Customer;
import cn.explink.domain.Department;
import cn.explink.domain.User;
import cn.explink.service.ExplinkUserDetail;

public class ExplinkAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	@Autowired
	CustomerDAO customerDAO;

	@Autowired
	DepartDAO departDAO;

	@Autowired
	BranchDAO branchDAO;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		super.onAuthenticationSuccess(request, response, authentication);
		HttpSession session = request.getSession(false);
		ExplinkUserDetail userDetail = (ExplinkUserDetail) authentication.getPrincipal();
		User user = userDetail.getUser();
		HashMap<String, Object> usermap = new HashMap<String, Object>();
		usermap.put("username", user.getUsername());
		usermap.put("branchid", user.getBranchid());
		usermap.put("realname", user.getRealname());
		usermap.put("userid", user.getUserid());
		// usermap.put("lastusername", user.getLastusername());
		Customer customer = customerDAO.getCustomer(user.getUsercustomerid());
		usermap.put("customername", customer == null ? "" : customer.getCustomername());
		usermap.put("branchname", branchDAO.getBranchByBranchid(user.getBranchid()).getBranchname());
		usermap.put("usercustomerid", user.getUsercustomerid());
		// usermap.put("usertypeflag", user.getUsertypeflag());
		// Department department = departDAO.getDepartment(user.getDepartid());
		// usermap.put("departname", department==null?"":department.getName());
		session.setAttribute("usermap", usermap);
	}

}
