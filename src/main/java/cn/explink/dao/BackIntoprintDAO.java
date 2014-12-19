package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.Backintowarehouse_print;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;
import cn.explink.enumutil.FlowOrderTypeEnum;

@Component
public class BackIntoprintDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class BackprintRowMapper implements RowMapper<Backintowarehouse_print> {
		@Override
		public Backintowarehouse_print mapRow(ResultSet rs, int rowNum) throws SQLException {
			Backintowarehouse_print b = new Backintowarehouse_print();
			b.setCwb(rs.getString("cwb"));
			b.setTranscwb(rs.getString("transcwb"));
			b.setUserid(rs.getLong("userid"));
			b.setIssignprint(rs.getLong("issignprint"));
			b.setCreatetime(rs.getString("createtime"));
			b.setFlowordertype(rs.getLong("flowordertype"));
			b.setBranchid(rs.getLong("branchid"));
			b.setStartbranchid(rs.getLong("startbranchid"));
			b.setNextbranchid(rs.getLong("nextbranchid"));
			b.setDeliverid(rs.getLong("deliverid"));
			b.setCustomerid(rs.getLong("customerid"));
			b.setBaleno(rs.getString("baleno"));
			b.setDriverid(rs.getLong("driverid"));
			b.setBackreasonid(rs.getLong("backreasonid"));
			b.setRemark1(rs.getString("remark1"));
			b.setRemark2(rs.getString("remark2"));
			b.setRemark3(rs.getString("remark3"));
			return b;
		}
	}

	public void creBackIntoprint(CwbOrder co, User user, long driverid, long nextbranchid, String baleno, String remark1, String remark2, String remark3) {
		String sql = "insert into `express_ops_backintowarehous_print` (`cwb`, `transcwb`, `userid`, `issignprint`, `createtime`, `flowordertype`, `startbranchid`, `branchid`, `nextbranchid`, `deliverid`, `customerid`, `baleno`, `driverid`, `backreasonid`, `remark1`, `remark2`, `remark3`) "
				+ "VALUES (?, ?, ?, ?, NOW(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		this.jdbcTemplate.update(sql, co.getCwb(), co.getTranscwb(), user.getUserid(), 0, FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), co.getStartbranchid(), user.getBranchid(), nextbranchid,
				co.getDeliverid(), co.getCustomerid(), baleno, driverid, co.getBackreasonid(), remark1, remark2, remark3);
	}

	public List<Backintowarehouse_print> getBackintoPrint(String starttime, String endtime, long flowordertype, String startbranchid, long driverid, long issignprint, User user) {
		String sql = "select * from express_ops_backintowarehous_print where createtime>=? and createtime<=? and flowordertype=?  and issignprint=? and branchid=" + user.getBranchid();
		if (driverid > 0) {
			sql += " and driverid=" + driverid;
		}
		if (!startbranchid.equals("0")) {
			sql += " and startbranchid in(" + startbranchid + ")";
		}

		return this.jdbcTemplate.query(sql, new BackprintRowMapper(), starttime, endtime, flowordertype, issignprint);
	}

	public List<Backintowarehouse_print> getBackintoPrintList(String starttime, String endtime, String cwbs, User user) {
		String sql = "select * from express_ops_backintowarehous_print where createtime>='" + starttime + "' and createtime<='" + endtime + "' and  cwb in (" + cwbs + ") and branchid="
				+ user.getBranchid();

		return this.jdbcTemplate.query(sql, new BackprintRowMapper());
	}
}
