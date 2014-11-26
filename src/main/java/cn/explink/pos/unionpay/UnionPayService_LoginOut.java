package cn.explink.pos.unionpay;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.explink.pos.tools.EmployeeInfo;

@Service
public class UnionPayService_LoginOut extends UnionPayService {

	private Logger logger = LoggerFactory.getLogger(UnionPayService_LoginOut.class);

	/**
	 * 派送员登出
	 * 
	 * @return
	 */
	public String toLoginOut(JSONObject jsondata) {

		EmployeeInfo employee = jiontDAO.getEmployeeInfo(jsondata.getString("LoginName"));
		if (employee == null) {
			return super.RespPublicMsg("01", "用户名[" + jsondata.getString("LoginName") + "]不存在");
		}
		logger.info("用户名为{}的投递员成功登出!", employee.getUsername());
		return super.RespPublicMsg("00", "");
	}

}
