package cn.explink.b2c.tools.b2cmonntor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.util.Page;

@Component
public class B2cJointMonitorDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class B2cDataMapper implements RowMapper<B2CMonitorData> {
		@Override
		public B2CMonitorData mapRow(ResultSet rs, int rowNum) throws SQLException {
			B2CMonitorData en = new B2CMonitorData();
			en.setB2cid(rs.getLong("b2cid"));
			en.setCwb(rs.getString("cwb"));
			en.setCustomerid(rs.getLong("customerid"));
			en.setExpt_reason(rs.getString("expt_reason"));
			en.setFlowordertype(rs.getLong("flowordertype"));
			en.setHand_deal_flag(rs.getInt("hand_deal_flag"));
			en.setPosttime(rs.getString("posttime"));
			en.setRemark(rs.getString("remark"));
			en.setSend_b2c_flag(rs.getLong("send_b2c_flag"));

			return en;
		}
	}

	public List<B2CMonitorData> selectB2cMonitorDataList(String cwb, String customerid, long flowordertype, long send_b2c_flag, long hand_deal_flag, String starttime, String endtime, long page) {
		String sql = "SELECT * FROM  express_save_b2cdata where 1=1  ";
		sql += this.whereSqlByB2cMonitorData(cwb, customerid, flowordertype, send_b2c_flag, hand_deal_flag, starttime, endtime);
		if (page > 0) {
			sql += " ORDER BY cwb ASC ,posttime ASC limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		} else {
			sql += " ORDER BY cwb ASC ,posttime ASC ";
		}

		return jdbcTemplate.query(sql, new B2cDataMapper());
	}

	public long selectB2cMonitorDataCount(String cwb, String customerid, long flowordertype, long send_b2c_flag, long hand_deal_flag, String starttime, String endtime) {
		String sql = "SELECT count(1) FROM  express_save_b2cdata where 1=1  ";
		sql += this.whereSqlByB2cMonitorData(cwb, customerid, flowordertype, send_b2c_flag, hand_deal_flag, starttime, endtime);

		return jdbcTemplate.queryForLong(sql);
	}

	private String whereSqlByB2cMonitorData(String cwb, String customerid, long flowtype, long send_b2c_flag, long hand_deal_flag, String starttime, String endtime) {
		String sql = "";
		if (cwb != null && !"".equals(cwb)) {
			sql += " and cwb='" + cwb + "' ";
		} else {
			if (flowtype > 0) {
				sql += " and flowordertype='" + flowtype + "' ";
			}
			if (!"".equals(starttime) && !"".equals(endtime)) {
				sql += " and posttime between '" + starttime + "' and '" + endtime + "' ";
			}
			if (!"0".equals(customerid)) {
				sql += " and customerid='" + customerid + "' ";
			}

			if (send_b2c_flag > -1) {
				if (send_b2c_flag == 3) {
					sql += " and send_b2c_flag in(0,2)";
				} else {
					sql += " and send_b2c_flag=" + send_b2c_flag;
				}
			}
			sql += " and hand_deal_flag=" + hand_deal_flag;
		}

		return sql;
	}

	public List<B2CMonitorData> selectB2cIdsByB2cMonitorDataList(String cwb, String customerid, long flowordertype, long send_b2c_flag, long hand_deal_flag, String starttime, String endtime) {
		String sql = "SELECT * FROM  express_save_b2cdata where 1=1  ";
		sql += this.whereSqlByB2cMonitorData(cwb, customerid, flowordertype, send_b2c_flag, hand_deal_flag, starttime, endtime);
		sql += " limit 0,5000";

		return jdbcTemplate.query(sql, new B2cDataMapper());
	}

	/**
	 * 查询重发流程JMS的时候
	 * 
	 * @param cwb
	 * @param flowtype
	 * @return
	 */
	public long checkIsRepeatDataFlag(String cwb, int flowtype, String posttime) {
		String sql = "SELECT count(1) FROM  express_save_b2cdata where cwb='" + cwb + "' and flowordertype=" + flowtype + " and posttime='" + posttime + "'";
		return jdbcTemplate.queryForLong(sql);
	}

	public void saveB2cDataMonitor(String cwb, long flowtype, long customerid, String posttime) {
		String sql = "insert into  express_save_b2cdata (cwb,flowordertype,customerid,posttime) values (?,?,?,?)";
		jdbcTemplate.update(sql, cwb, flowtype, customerid, posttime);
	}

	public void updateB2cDataMonitor(String cwb, long flowtype, long send_b2c_flag, String expt_reason) {
		String sql = "update express_save_b2cdata set send_b2c_flag=?,expt_reason=? where cwb=? and flowordertype=? and send_b2c_flag in (0,2)";
		jdbcTemplate.update(sql, send_b2c_flag, expt_reason, cwb, flowtype);
	}

	public void updateB2cDataMonitorDeal(long b2cids) {
		String sql = "update express_save_b2cdata set hand_deal_flag=1 where b2cid = " + b2cids;
		jdbcTemplate.update(sql);
	}

}
