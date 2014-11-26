package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.domain.Bale;
import cn.explink.enumutil.BaleStateEnum;
import cn.explink.util.Page;

@Component
public class BaleDao {
	private final class BaleMapper implements RowMapper<Bale> {
		@Override
		public Bale mapRow(ResultSet rs, int rowNum) throws SQLException {
			Bale bale = new Bale();
			bale.setId(rs.getLong("id"));
			bale.setBaleno(rs.getString("baleno"));
			bale.setBalestate(rs.getLong("balestate"));
			bale.setBranchid(rs.getLong("branchid"));
			bale.setGroupid(rs.getLong("groupid"));
			bale.setCretime(rs.getTimestamp("cretime"));
			bale.setCwbcount(rs.getLong("cwbcount"));
			bale.setNextbranchid(rs.getLong("nextbranchid"));
			return bale;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public long createBale(final Bale bale) {
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into express_ops_bale(baleno,balestate,branchid,nextbranchid,cwbcount) values(?,?,?,?,?)", new String[] { "id" });
				ps.setString(1, bale.getBaleno());
				ps.setLong(2, bale.getBalestate());
				ps.setLong(3, bale.getBranchid());
				ps.setLong(4, bale.getNextbranchid());
				ps.setLong(5, bale.getCwbcount());
				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

	public long create(final String baleno) {
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into express_ops_bale(baleno) values(?)", new String[] { "id" });
				ps.setString(1, baleno);
				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

	public Bale getBaleByBalestateAndBranchid(String baleno, long balestate, long branchid) {
		try {
			String sql = "select * from express_ops_bale where baleno=? and balestate=? and branchid=?";
			return jdbcTemplate.queryForObject(sql, new BaleMapper(), baleno, balestate, branchid);
		} catch (Exception e) {
			return null;
		}
	}

	public List<Bale> getBaleByBalenoAndBranchid(String baleno, long branchid) {
		String sql = "select * from express_ops_bale where baleno=? and branchid=?";
		return jdbcTemplate.query(sql, new BaleMapper(), baleno, branchid);
	}

	public List<Bale> getBaleByBalestate(String baleno, long balestate) {
		String sql = "select * from express_ops_bale where baleno=? and balestate=?";
		return jdbcTemplate.query(sql, new BaleMapper(), baleno, balestate);
	}

	public List<Bale> getBaleByBalenoAndBalestate(String baleno, String balestates) {
		String sql = "select * from express_ops_bale where baleno=? and balestate in(" + balestates + ")";
		return jdbcTemplate.query(sql, new BaleMapper(), baleno);
	}

	public List<Bale> getBaleByBalestate(long balestate) {
		String sql = "select * from express_ops_bale where balestate=?";
		return jdbcTemplate.query(sql, new BaleMapper(), balestate);
	}

	public void saveForBranchid(long id, long branchid, long groupid, long balestate) {
		String sql = "update express_ops_bale set branchid=?,groupid=?,balestate=? where id=?";
		jdbcTemplate.update(sql, branchid, groupid, balestate, id);
	}

	public Bale getBaleByBaleno(String baleno, long balestate) {
		try {
			String sql = "select * from express_ops_bale where baleno=? and balestate=?";
			return jdbcTemplate.queryForObject(sql, new BaleMapper(), baleno, balestate);
		} catch (DataAccessException e) {
			return null;
		}
	}

	/*
	 * public void saveForBaleState(long balestate,long groupid,long branchid){
	 * String sql = "update express_ops_bale set balestate="+balestate+
	 * " where id in(select baleid from express_ops_groupdetail where groupid="
	 * +groupid
	 * +") and branchid="+branchid+" and balestate="+BaleStateEnum.SaoMiaoZhong
	 * .getValue(); jdbcTemplate.update(sql); }
	 */

	public void saveForState(String baleno, long branchid, long balestate) {
		String sql = "update express_ops_bale set balestate=? where branchid=? and baleno=? and balestate=? ";
		jdbcTemplate.update(sql, balestate, branchid, baleno, BaleStateEnum.WeiDaoZhan.getValue());
	}

	public void saveForBalestate(String baleno, long balestate, long oldbalestate) {
		String sql = "update express_ops_bale set balestate=? where baleno=? and balestate=? ";
		jdbcTemplate.update(sql, balestate, baleno, oldbalestate);
	}

	public void saveForBranchidAndState(String baleno, long branchid, long balestate) {
		String sql = "update express_ops_bale set branchid=?,balestate=? where baleno=? and balestate=? ";
		jdbcTemplate.update(sql, branchid, balestate, baleno, BaleStateEnum.WeiDaoZhan.getValue());
	}

	public void saveForBranchidAndGroupid(long branchid, long balestate, long groupid) {
		String sql = "update express_ops_bale set balestate=? where branchid=? and groupid=? and balestate=?";
		jdbcTemplate.update(sql, balestate, branchid, groupid, BaleStateEnum.WeiDaoZhan.getValue());
	}

	public void saveById(long balestate, long id) {
		String sql = "update express_ops_bale set balestate=? where id=?";
		jdbcTemplate.update(sql, balestate, id);
	}

	public List<Bale> getBaleByBaleno(String baleno) {
		String sql = "select * from express_ops_bale where baleno=?";
		return jdbcTemplate.query(sql, new BaleMapper(), baleno);

	}

	public void saveForBaleCount(long id, long cwbcount) {
		String sql = "update express_ops_bale set cwbcount=?  where id=? ";
		jdbcTemplate.update(sql, cwbcount, id);
	}

	public List<Bale> getBaleByChukuDate(String begindate, String enddate, long page) {
		String sql = "select * from express_ops_bale where cretime>=? and cretime<=? ";
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new BaleMapper(), begindate, enddate);
	}

	public long getBaleByChukuDateCount(String begindate, String enddate) {
		String sql = "select count(1) from express_ops_bale where cretime>=? and cretime<=? ";
		return jdbcTemplate.queryForLong(sql, begindate, enddate);
	}

	public Bale getBaleByCwb(String cwb) {
		try {
			String sql = "select * from express_ops_bale where cwb=? ";
			return jdbcTemplate.queryForObject(sql, new BaleMapper(), cwb);
		} catch (DataAccessException e) {
			return null;
		}
	}

	public Bale getBaleOneByBaleno(String baleno) {
		try {
			String sql = "select * from express_ops_bale where baleno=? ";
			return jdbcTemplate.queryForObject(sql, new BaleMapper(), baleno);
		} catch (DataAccessException e) {
			return null;
		}
	}

	public Bale getBaleOneByBalenoLock(String baleno) {
		try {
			String sql = "select * from express_ops_bale where baleno=? for update";
			return jdbcTemplate.queryForObject(sql, new BaleMapper(), baleno);
		} catch (DataAccessException e) {
			return null;
		}
	}

	public void updateSubBaleCount(String baleno) {
		String sql = "update express_ops_bale set cwbcount=cwbcount-1 where baleno=? ";
		jdbcTemplate.update(sql, baleno);
	}

	public void updateAddBaleCount(String baleno) {
		String sql = "update express_ops_bale set cwbcount=cwbcount+1 where baleno=? ";
		jdbcTemplate.update(sql, baleno);
	}

	public void updateBalesate(String baleno, long balestate) {
		String sql = "update express_ops_bale set balestate=? where baleno=? ";
		jdbcTemplate.update(sql, balestate, baleno);
	}

	public List<Bale> getBaleByBalePrint(long branchid, String baleno, String strtime, String endtime) {
		String sql = "select * from express_ops_bale where branchid=? and cwbcount>0";
		if (baleno.length() > 0) {
			sql += " and baleno='" + baleno + "'";
		}
		if (strtime.length() > 0) {
			sql += " and cretime>'" + strtime + "'";
		}
		if (endtime.length() > 0) {
			sql += " and cretime<'" + endtime + "'";
		}
		return jdbcTemplate.query(sql, new BaleMapper(), branchid);
	}

}
