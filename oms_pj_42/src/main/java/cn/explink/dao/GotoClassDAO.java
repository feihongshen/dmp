package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import cn.explink.domain.GotoClassAuditing;

@Component
public class GotoClassDAO {

	private final class GotoClassAuditingRowMapper implements RowMapper<GotoClassAuditing> {

		@Override
		public GotoClassAuditing mapRow(ResultSet rs, int rowNum) throws SQLException {
			// TODO Auto-generated method stub
			GotoClassAuditing gotoClass = new GotoClassAuditing();
			gotoClass.setId(rs.getInt("id"));
			gotoClass.setAuditingtime(rs.getString("auditingtime"));
			gotoClass.setPayupamount(rs.getBigDecimal("payupamount"));
			gotoClass.setReceivedfeeuser(rs.getInt("receivedfeeuser"));
			gotoClass.setBranchid(rs.getInt("branchid"));
			gotoClass.setPayupid(rs.getInt("payupid"));
			gotoClass.setClassid(rs.getLong("classid"));
			gotoClass.setGobackidstr(rs.getString("gobackidstr"));
			return gotoClass;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void creGotoClassAuditing(final GotoClassAuditing gotoClass) {

		jdbcTemplate.update("insert into express_ops_goto_class_auditing(auditingtime,payupamount,receivedfeeuser,branchid,payupid,classid,gobackidstr) values(?,?,?,?,?,?,?)",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						// TODO Auto-generated method stub
						ps.setString(1, gotoClass.getAuditingtime());
						ps.setBigDecimal(2, gotoClass.getPayupamount());
						ps.setInt(3, gotoClass.getReceivedfeeuser());
						ps.setInt(4, gotoClass.getBranchid());
						ps.setInt(5, gotoClass.getPayupid());
						ps.setLong(6, gotoClass.getClassid());
						ps.setString(7, gotoClass.getGobackidstr());
					}
				});

	}

	public long checkGoclass(long classid) {
		String sql = "select count(1) from express_ops_goto_class_auditing  where classid =" + classid;
		return jdbcTemplate.queryForLong(sql);

	}

	public void UpdateGotoClassAuditing(long payupid, String classids) {
		String sql = "update express_ops_goto_class_auditing set payupid=? where classid in (" + classids + ") ";
		jdbcTemplate.update(sql, payupid);

	}

	public String getGobackidStr(String classidStr) {
		StringBuffer str = new StringBuffer();
		String GobackidStr = null;
		List<GotoClassAuditing> list = jdbcTemplate.query("select * from express_ops_goto_class_auditing where classid in(" + classidStr + ")", new GotoClassAuditingRowMapper());
		if (list.size() > 0) {
			for (GotoClassAuditing g : list) {
				str.append(g.getGobackidstr()).append(",");
			}
			GobackidStr = str.substring(0, str.length() - 1);
		}
		return GobackidStr;
	}

}
