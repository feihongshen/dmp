package cn.explink.jms;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

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
import cn.explink.domain.User;
import cn.explink.service.ExplinkUserDetail;

public class ExplinkAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	@Autowired
	CustomerDAO customerDAO;

	@Autowired
	DepartDAO departDAO;

	@Autowired
	BranchDAO branchDAO;
	
	@Autowired
	Properties propertyPlaceHolder;
	
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
		Customer customer = customerDAO.getCustomerById(user.getUsercustomerid());
		usermap.put("customername", customer == null ? "" : customer.getCustomername());
		usermap.put("branchname", branchDAO.getBranchByBranchid(user.getBranchid()).getBranchname());
		usermap.put("usercustomerid", user.getUsercustomerid());
		// usermap.put("usertypeflag", user.getUsertypeflag());
		// Department department = departDAO.getDepartment(user.getDepartid());
		// usermap.put("departname", department==null?"":department.getName());
		usermap.put("isImposedOutWarehouse", user.getIsImposedOutWarehouse());
		usermap.put("roleid", user.getRoleid());
		session.setAttribute("usermap", usermap);
		session.setAttribute("showphoneflag", user.getShowphoneflag());

		{
			try {
				String url = request.getRequestURL().toString();
				if (Boolean.valueOf(propertyPlaceHolder.getProperty("login.use.ssl"))) {
					int pos = url.indexOf("://");
					if (pos > 0) {
						String hp = url.substring(pos + 3);
						int index = hp.indexOf("/");
						if (index > 0) {
							//修复问题：登陆多次后连接重复出现
							//String nhp = "http://" + hp.substring(0, index) + request.getContextPath() + getDefaultTargetUrl();
							String nhp = "http://" + hp.substring(0, index) + request.getContextPath() + "/" ;
							this.setDefaultTargetUrl(nhp);
							System.out.println("redirect to : " + nhp);
							response.sendRedirect(nhp);
							return;
						}
					}
				}
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}

	}

}
