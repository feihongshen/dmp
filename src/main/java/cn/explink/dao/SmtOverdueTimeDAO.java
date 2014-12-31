package cn.explink.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.explink.domain.CwbOrder;
import cn.explink.domain.orderflow.OrderFlow;

@Repository
public class SmtOverdueTimeDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate = null;

	@Autowired
	private CwbDAO cwbDAO = null;

	private void updateSmtOverdueTime(OrderFlow orderFlow) {
		CwbOrder cwbOrder = this.getCwbOrder(orderFlow);
		if ((cwbOrder == null) || (cwbOrder.getCwbordertypeid() != 2)) {
			return;
		}
		this.updateTime(orderFlow);
	}

	private void insertSmtOverdueTime() {

	}

	private void insertTime(OrderFlow orderFlow) {

	}

	private void updateTime(OrderFlow orderFlow) {
	}

	private CwbOrder getCwbOrder(OrderFlow orderFlow) {
		return this.getCwbDAO().getCwbByCwb(orderFlow.getCwb());
	}

	private boolean isCwbExist(OrderFlow orderFlow) {
		String sql = "select count(1) from express_ops_smt_cwb_opt_time where cwb = ?";
		int count = this.getJdbcTemplate().queryForInt(sql, orderFlow.getCwb());

		return count != 0;
	}

	private JdbcTemplate getJdbcTemplate() {
		return this.jdbcTemplate;
	}

	private CwbDAO getCwbDAO() {
		return this.cwbDAO;
	}

}
