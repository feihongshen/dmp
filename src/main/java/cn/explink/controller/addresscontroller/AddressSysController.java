package cn.explink.controller.addresscontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/addresssyncontroller")
public class AddressSysController {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 初始化----用户点击菜单后加载0级树
	 * 
	 * @param addressId
	 * @return
	 */
	@RequestMapping("/")
	public @ResponseBody String syn(Model model) {
		try {
			synUser();
			synBrach();
			synCustomer();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void synUser() {
		String sql = "INSERT INTO SCHEDULED_TASKS (	TASK_TYPE, STATUS, REFERENCE_TYPE, REFERENCE_ID, FIRE_TIME, COMPLETED_TIME, CREATED_AT, TRY_COUNT) "
				+ " SELECT 'synaddressusercreat', '0', 'userId', userid, SYSDATE(), SYSDATE(), SYSDATE(), 0 " + " FROM express_set_user " + " WHERE roleid = 2";
		jdbcTemplate.update(sql);
	}

	public void synBrach() {
		String sql = "INSERT INTO SCHEDULED_TASKS ( TASK_TYPE, STATUS, REFERENCE_TYPE, REFERENCE_ID, FIRE_TIME, COMPLETED_TIME, CREATED_AT, TRY_COUNT) "
				+ " SELECT 'synaddressbranchcreat', '0', 'branchId', branchid, SYSDATE(), SYSDATE(), SYSDATE(), 0 " + " FROM express_set_branch	" + " WHERE sitetype = 2";
		jdbcTemplate.update(sql);
	}

	public void synCustomer() {
		String sql = "INSERT INTO SCHEDULED_TASKS ( TASK_TYPE, STATUS, REFERENCE_TYPE, REFERENCE_ID, FIRE_TIME, COMPLETED_TIME, CREATED_AT, TRY_COUNT) "
				+ " SELECT 'synaddresscustomercreat', '0', 'customerId', customerid, SYSDATE(), SYSDATE(), SYSDATE(), 0 " + " FROM express_set_customer_info";
		jdbcTemplate.update(sql);
	}
}
