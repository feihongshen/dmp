package cn.explink.pos.unionpay;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.explink.pos.tools.EmployeeInfo;

@Service
public class UnionPayService_Login extends UnionPayService {

	private Logger logger = LoggerFactory.getLogger(UnionPayService_Login.class);

	private String respMsg_toLogin(EmployeeInfo user) {
		logger.info("移动POS(UnionPay)派送员登录成功！response=00,username=" + user.getUsername() + ",realname=" + user.getRealname());
		return "{\"Response\":\"00\",\"Name\":\"" + user.getRealname() + "\",\"DepartmentCode\":\"" + user.getBranchid() + "\",\"DepartmentName\":\"" + user.getBranchname()
				+ "\",\"ErrorDescription\":\"\"}";
	}

	/**
	 * 派送员登录
	 * 
	 * @return
	 */
	public String toLogin(JSONObject jsondata) {

		EmployeeInfo employee = jiontDAO.getEmployeeByUserNameAndPassWord(jsondata.getString("LoginName"), jsondata.getString("Password"));
		if (employee == null) {
			return super.RespPublicMsg("01", "用户名或密码不正确");
		}

		return respMsg_toLogin(employee);
	}

}
