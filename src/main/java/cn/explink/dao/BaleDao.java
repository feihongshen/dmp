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

import com.pjbest.splitting.aspect.DataSource;
import com.pjbest.splitting.routing.DatabaseType;

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
			bale.setScannum(rs.getLong("scannum"));
			return bale;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public long createBale(final Bale bale) {
		KeyHolder key = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into express_ops_bale(baleno,balestate,branchid,nextbranchid,cwbcount,handlerid,handlername) values(?,?,?,?,?,?,?)", new String[] { "id" });
				ps.setString(1, bale.getBaleno());
				ps.setLong(2, bale.getBalestate());
				ps.setLong(3, bale.getBranchid());
				ps.setLong(4, bale.getNextbranchid());
				ps.setLong(5, bale.getCwbcount());
				ps.setInt(6, bale.getHandlerid());
				ps.setString(7, bale.getHandlername());
				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

	public long create(final String baleno) {
		KeyHolder key = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
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
			return this.jdbcTemplate.queryForObject(sql, new BaleMapper(), baleno, balestate, branchid);
		} catch (Exception e) {
			return null;
		}
	}

	public List<Bale> getBaleByBalenoAndBranchid(String baleno, long branchid) {
		String sql = "select * from express_ops_bale where baleno=? and branchid=?";
		return this.jdbcTemplate.query(sql, new BaleMapper(), baleno, branchid);
	}

	public List<Bale> getBaleByBalestate(String baleno, long balestate) {
		String sql = "select * from express_ops_bale where baleno=? and balestate=?";
		return this.jdbcTemplate.query(sql, new BaleMapper(), baleno, balestate);
	}

	public List<Bale> getBaleByBalenoAndBalestate(String baleno, String balestates) {
		String sql = "select * from express_ops_bale where baleno=? and balestate in(" + balestates + ")";
		return this.jdbcTemplate.query(sql, new BaleMapper(), baleno);
	}

	public List<Bale> getBaleByBalestate(long balestate) {
		String sql = "select * from express_ops_bale where balestate=?";
		return this.jdbcTemplate.query(sql, new BaleMapper(), balestate);
	}

	public void saveForBranchid(long id, long branchid, long groupid, long balestate) {
		String sql = "update express_ops_bale set branchid=?,groupid=?,balestate=? where id=?";
		this.jdbcTemplate.update(sql, branchid, groupid, balestate, id);
	}

	public Bale getBaleByBaleno(String baleno, long balestate) {
		try {
			String sql = "select * from express_ops_bale where baleno=? and balestate=?";
			return this.jdbcTemplate.queryForObject(sql, new BaleMapper(), baleno, balestate);
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
		this.jdbcTemplate.update(sql, balestate, branchid, baleno, BaleStateEnum.WeiDaoZhan.getValue());
	}

	public void saveForBalestate(String baleno, long balestate, long oldbalestate) {
		String sql = "update express_ops_bale set balestate=? where baleno=? and balestate=? ";
		this.jdbcTemplate.update(sql, balestate, baleno, oldbalestate);
	}

	public void saveForBranchidAndState(String baleno, long branchid, long balestate) {
		String sql = "update express_ops_bale set branchid=?,balestate=? where baleno=? and balestate=? ";
		this.jdbcTemplate.update(sql, branchid, balestate, baleno, BaleStateEnum.WeiDaoZhan.getValue());
	}

	public void saveForBranchidAndGroupid(long branchid, long balestate, long groupid) {
		String sql = "update express_ops_bale set balestate=? where branchid=? and groupid=? and balestate=?";
		this.jdbcTemplate.update(sql, balestate, branchid, groupid, BaleStateEnum.WeiDaoZhan.getValue());
	}

	public void saveById(long balestate, long id) {
		String sql = "update express_ops_bale set balestate=? where id=?";
		this.jdbcTemplate.update(sql, balestate, id);
	}

	public List<Bale> getBaleByBaleno(String baleno) {
		String sql = "select * from express_ops_bale where baleno=?";
		return this.jdbcTemplate.query(sql, new BaleMapper(), baleno);

	}

	public void saveForBaleCount(long id, long cwbcount) {
		String sql = "update express_ops_bale set cwbcount=?  where id=? ";
		this.jdbcTemplate.update(sql, cwbcount, id);
	}

	public List<Bale> getBaleByChukuDate(String begindate, String enddate, long page) {
		String sql = "select * from express_ops_bale where cretime>=? and cretime<=? ";
		sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		return this.jdbcTemplate.query(sql, new BaleMapper(), begindate, enddate);
	}

	public long getBaleByChukuDateCount(String begindate, String enddate) {
		String sql = "select count(1) from express_ops_bale where cretime>=? and cretime<=? ";
		return this.jdbcTemplate.queryForLong(sql, begindate, enddate);
	}

	@Deprecated
	public Bale getBaleByCwb(String cwb) {
		try {
			String sql = "select * from express_ops_bale where cwb=? ";
			return this.jdbcTemplate.queryForObject(sql, new BaleMapper(), cwb);
		} catch (DataAccessException e) {
			return null;
		}
	}

	public Bale getBaleOneByBaleno(String baleno) {
		try {
			String sql = "select * from express_ops_bale where baleno=? ";
			return this.jdbcTemplate.queryForObject(sql, new BaleMapper(), baleno);
		} catch (DataAccessException e) {
			return null;
		}
	}

	public Bale getBaleOneByBalenoLock(String baleno) {
		try {
			String sql = "select * from express_ops_bale where baleno=? for update";
			return this.jdbcTemplate.queryForObject(sql, new BaleMapper(), baleno);
		} catch (DataAccessException e) {
			return null;
		}
	}

	public void updateSubBaleCount(String baleno) {
		String sql = "update express_ops_bale set cwbcount=cwbcount-1  where baleno=? ";
		this.jdbcTemplate.update(sql, baleno);
	}

	public void updateAddBaleCount(String baleno) {
		String sql = "update express_ops_bale set cwbcount=cwbcount+1 where baleno=? ";
		this.jdbcTemplate.update(sql, baleno);
	}

	public void updateAddBaleScannum(String baleno) {
		String sql = "update express_ops_bale set scannum=scannum+1 where baleno=? ";
		this.jdbcTemplate.update(sql, baleno);
	}

	public void updateBalesate(String baleno, long balestate) {
		String sql = "update express_ops_bale set balestate=? where baleno=? ";
		this.jdbcTemplate.update(sql, balestate, baleno);
	}

	@DataSource(DatabaseType.REPLICA)
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
		return this.jdbcTemplate.query(sql, new BaleMapper(), branchid);
	}

	public Bale getBaleById(long baleid) {
		try {
			String sql = "select * from express_ops_bale where id=? ";
			return this.jdbcTemplate.queryForObject(sql, new BaleMapper(), baleid);
		} catch (DataAccessException e) {
			return null;
		}
	}

	// added by jiangyu
	public void updateBalesateAndNextBranchId(String baleno, long balestate, long nextBranchId, long currentBranchId) {
		String sql = "update express_ops_bale set balestate=?,nextbranchid=?,branchid=? where baleno=? ";
		this.jdbcTemplate.update(sql, balestate, nextBranchId, currentBranchId, baleno);
	}

	public void updateBranchIdAndNextBranchId(String baleno, long nextBranchId, long currentBranchId) {
		String sql = "update express_ops_bale set nextbranchid=?,branchid=? where baleno=? ";
		this.jdbcTemplate.update(sql, nextBranchId, currentBranchId, baleno);
	}
}
