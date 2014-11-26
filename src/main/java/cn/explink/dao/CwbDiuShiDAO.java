package cn.explink.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.CwbDiuShi;
import cn.explink.enumutil.CwbDiuShiIsHandleEnum;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Component
public class CwbDiuShiDAO {
	private final class CwbDiuShiMapper implements RowMapper<CwbDiuShi> {
		@Override
		public CwbDiuShi mapRow(ResultSet rs, int rowNum) throws SQLException {
			CwbDiuShi cwbDiuShi = new CwbDiuShi();

			cwbDiuShi.setId(rs.getLong("id"));
			cwbDiuShi.setCwb(rs.getString("cwb"));
			cwbDiuShi.setShenhetime(StringUtil.nullConvertToEmptyString(rs.getString("shenhetime")));
			cwbDiuShi.setCustomerid(rs.getLong("customerid"));
			cwbDiuShi.setCwbordertypeid(rs.getLong("cwbordertypeid"));
			cwbDiuShi.setUserid(rs.getLong("userid"));
			cwbDiuShi.setReceivablefee(rs.getBigDecimal("receivablefee") == null ? BigDecimal.ZERO : rs.getBigDecimal("receivablefee"));
			cwbDiuShi.setPaybackfee(rs.getBigDecimal("paybackfee") == null ? BigDecimal.ZERO : rs.getBigDecimal("paybackfee"));
			cwbDiuShi.setCaramount(rs.getBigDecimal("caramount") == null ? BigDecimal.ZERO : rs.getBigDecimal("caramount"));
			cwbDiuShi.setPayamount(rs.getBigDecimal("payamount") == null ? BigDecimal.ZERO : rs.getBigDecimal("payamount"));
			cwbDiuShi.setBranchid(rs.getLong("branchid"));
			cwbDiuShi.setIshandle(rs.getLong("ishandle"));
			cwbDiuShi.setIsendstate(rs.getLong("isendstate"));
			cwbDiuShi.setHandletime(rs.getString("handletime"));
			cwbDiuShi.setHandleuserid(rs.getLong("handleuserid"));

			return cwbDiuShi;
		}
	}

	private final class DeliveryNotDetailMapper implements RowMapper<CwbDiuShi> {
		@Override
		public CwbDiuShi mapRow(ResultSet rs, int rowNum) throws SQLException {
			CwbDiuShi cwbDiuShi = new CwbDiuShi();

			cwbDiuShi.setId(rs.getLong("id"));
			// cwbDiuShi.setNextbranchid(rs.getLong("nextbranchid"));

			return cwbDiuShi;
		}
	}

	private final class CwbDiuShiSumMapper implements RowMapper<CwbDiuShi> {
		@Override
		public CwbDiuShi mapRow(ResultSet rs, int rowNum) throws SQLException {
			CwbDiuShi cwbDiuShi = new CwbDiuShi();
			cwbDiuShi.setId(rs.getLong("id"));
			cwbDiuShi.setReceivablefee(rs.getBigDecimal("receivablefee"));
			cwbDiuShi.setPaybackfee(rs.getBigDecimal("paybackfee"));
			cwbDiuShi.setCaramount(rs.getBigDecimal("caramount"));
			cwbDiuShi.setPayamount(rs.getBigDecimal("payamount"));

			return cwbDiuShi;
		}
	}

	private final class DeliveryGroupNextbranchidMapper implements RowMapper<CwbDiuShi> {
		@Override
		public CwbDiuShi mapRow(ResultSet rs, int rowNum) throws SQLException {
			CwbDiuShi cwbDiuShi = new CwbDiuShi();

			cwbDiuShi.setId(rs.getLong("id"));
			// cwbDiuShi.setNextbranchid(rs.getLong("nextbranchid"));

			return cwbDiuShi;
		}
	}

	private final class DeliveryGroupCustomeridMapper implements RowMapper<CwbDiuShi> {
		@Override
		public CwbDiuShi mapRow(ResultSet rs, int rowNum) throws SQLException {
			CwbDiuShi cwbDiuShi = new CwbDiuShi();

			cwbDiuShi.setId(rs.getLong("id"));
			cwbDiuShi.setCustomerid(rs.getLong("customerid"));

			return cwbDiuShi;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void creCwbDiuShi(final CwbDiuShi cwbDiuShi) {
		jdbcTemplate.update("INSERT INTO `op_cwb_diushi`(`cwb`,`shenhetime`,`customerid`,`cwbordertypeid`," + "`userid`,`receivablefee`,`paybackfee`,`branchid`,`caramount`,`payamount`) "
				+ "VALUES ( ?,?,?,?,?,?,?,?,?,?)", new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, cwbDiuShi.getCwb());
				ps.setString(2, cwbDiuShi.getShenhetime());
				ps.setLong(3, cwbDiuShi.getCustomerid());
				ps.setLong(4, cwbDiuShi.getCwbordertypeid());
				ps.setLong(5, cwbDiuShi.getUserid());
				ps.setBigDecimal(6, cwbDiuShi.getReceivablefee());
				ps.setBigDecimal(7, cwbDiuShi.getPaybackfee());
				ps.setLong(8, cwbDiuShi.getBranchid());
				ps.setBigDecimal(9, cwbDiuShi.getCaramount());
				ps.setBigDecimal(10, cwbDiuShi.getPayamount());
			}
		});
	}

	public void saveCwbDiuShi(final CwbDiuShi cwbDiuShi) {
		jdbcTemplate.update("UPDATE `op_cwb_diushi` SET " + "`shenhetime`=?,`cwbordertypeid`=?,"
				+ "`userid`=?,`customerid`=?,`receivablefee`=?,`paybackfee`=?,`branchid`=?,`caramount`=?,`payamount`=? WHERE `cwb`=?", new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, cwbDiuShi.getShenhetime());
				ps.setLong(2, cwbDiuShi.getCwbordertypeid());
				ps.setLong(3, cwbDiuShi.getUserid());
				ps.setLong(4, cwbDiuShi.getCustomerid());
				ps.setBigDecimal(5, cwbDiuShi.getReceivablefee());
				ps.setBigDecimal(6, cwbDiuShi.getPaybackfee());
				ps.setLong(7, cwbDiuShi.getBranchid());
				ps.setBigDecimal(8, cwbDiuShi.getCaramount());
				ps.setBigDecimal(9, cwbDiuShi.getPayamount());
				ps.setString(10, cwbDiuShi.getCwb());
			}
		});
	}

	public void saveCwbDiuShiById(String handletime, long handleuserid, BigDecimal payamount, long id) {
		jdbcTemplate.update("UPDATE `op_cwb_diushi` SET `handletime`=?,`handleuserid`=?,`payamount`=?,`ishandle`=? WHERE `id`=?", handletime, handleuserid, payamount,
				CwbDiuShiIsHandleEnum.YiChuLi.getValue(), id);
	}

	public void saveCwbDiuShiByCwb(long isendstate, String cwb) {
		jdbcTemplate.update("UPDATE `op_cwb_diushi` SET `isendstate`=? WHERE `cwb`=?", isendstate, cwb);
	}

	public CwbDiuShi getCwbDiuShiById(long id) {
		try {
			String sql = "SELECT * FROM op_cwb_diushi where id=?";
			return jdbcTemplate.queryForObject(sql, new CwbDiuShiMapper(), id);
		} catch (DataAccessException e) {
			return null;
		}
	}

	public long getCwbDiuShiByCwbCount(String cwb) {
		String sql = "SELECT count(1) FROM op_cwb_diushi where cwb=?";
		return jdbcTemplate.queryForLong(sql, cwb);
	}

	public List<CwbDiuShi> getCwbDiuShiListNOPage(String begindate, String enddate, String customerids, String branchids, long ishandle) {
		String sql = "SELECT * FROM op_cwb_diushi FORCE INDEX(Diushi_Shenhetime_Idx) where shenhetime >='" + begindate + "' and shenhetime <='" + enddate + "'";
		sql = setWhereSql(sql, customerids, branchids, ishandle);
		System.out.println(sql);
		return jdbcTemplate.query(sql, new CwbDiuShiMapper());
	}

	public List<CwbDiuShi> getCwbDiuShiList(long page, String begindate, String enddate, String customerids, String branchids, long ishandle) {
		String sql = "SELECT * FROM op_cwb_diushi FORCE INDEX(Diushi_Shenhetime_Idx) where shenhetime >='" + begindate + "' and shenhetime <='" + enddate + "'";
		sql = setWhereSql(sql, customerids, branchids, ishandle);
		sql += "  limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		System.out.println(sql);
		return jdbcTemplate.query(sql, new CwbDiuShiMapper());
	}

	public long getCwbDiuShiCount(String begindate, String enddate, String customerids, String branchids, long ishandle) {
		String sql = "SELECT count(1) FROM op_cwb_diushi FORCE INDEX(Diushi_Shenhetime_Idx) where shenhetime >='" + begindate + "' and shenhetime <='" + enddate + "'";
		sql = setWhereSql(sql, customerids, branchids, ishandle);
		return jdbcTemplate.queryForLong(sql);
	}

	public CwbDiuShi getCwbDiuShiSum(String begindate, String enddate, String customerids, String branchids, long ishandle) {
		String sql = "SELECT count(1) as id,sum(receivablefee) as receivablefee,sum(paybackfee) as paybackfee,sum(caramount) as caramount,sum(payamount) as payamount"
				+ " FROM op_cwb_diushi FORCE INDEX(Diushi_Shenhetime_Idx) where shenhetime >='" + begindate + "' and shenhetime <='" + enddate + "'";
		sql = setWhereSql(sql, customerids, branchids, ishandle);
		return jdbcTemplate.queryForObject(sql, new CwbDiuShiSumMapper());
	}

	public List<CwbDiuShi> checkCwbDiuShi(String cwb) {
		String sql = "SELECT * FROM op_cwb_diushi  WHERE cwb=? limit 0,1";
		return jdbcTemplate.query(sql, new CwbDiuShiMapper(), cwb);
	}

	public void deleteCwbDiuShi(String cwb) {
		String sql = "DELETE FROM op_cwb_diushi WHERE cwb=? ";
		jdbcTemplate.update(sql, cwb);
	}

	/**
	 * 
	 * @param sql
	 * @param customerids
	 * @param branchids
	 * @param ishandle
	 * @return
	 */
	public String setWhereSql(String sql, String customerids, String branchids, long ishandle) {
		if (customerids.length() > 0 || branchids.length() > 0) {
			if (customerids.length() > 0) {
				sql += " and customerid in(" + customerids + ") ";
			}
			if (branchids.length() > 0) {
				sql += " and branchid in(" + branchids + ") ";
			}
		}
		if (ishandle == 0) {// 未处理
			sql += " and ishandle=" + ishandle;
		} else if (ishandle == 1) {// 已处理
			sql += " and ishandle=" + ishandle;
		}
		return sql;
	}

	public List<CwbDiuShi> getCwbDiuShiCollectList(long page, long customerid, long kufangid, long nextbranchid, String begindate, String enddate) {
		String sql = "SELECT * FROM op_cwb_diushi FORCE INDEX(Diushi_Shenhetime_Idx) where outstoreroomtime>='" + begindate + "' and outstoreroomtime <= '" + enddate + "'";

		if (customerid > 0) {
			sql += " and customerid=" + customerid;
		}
		if (nextbranchid > 0) {
			sql += " and nextbranchid =" + nextbranchid;
		}
		if (kufangid > 0) {
			sql += " and startbranchid = " + kufangid;
		}
		sql += "  limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		System.out.println(sql);
		return jdbcTemplate.query(sql, new CwbDiuShiMapper());
	}

	public List<CwbDiuShi> getCwbDiuShiGroupByNextbranchid(long kufangid, String begindate, String enddate) {
		String sql = "select nextbranchid,count(1)as id from op_cwb_diushi FORCE INDEX(Diushi_Shenhetime_Idx) where "
				+ " outstoreroomtime >=? and outstoreroomtime <= ? and startbranchid = ? GROUP BY nextbranchid";

		return jdbcTemplate.query(sql, new DeliveryGroupNextbranchidMapper(), begindate, enddate, kufangid);
	}

	public List<CwbDiuShi> getCwbDiuShiGroupByCustomerid(long kufangid, String begindate, String enddate, String nextbranchids) {
		String sql = "select customerid,count(1)as id from op_cwb_diushi FORCE INDEX(Diushi_Shenhetime_Idx) where outstoreroomtime >=? and outstoreroomtime <=? and startbranchid=?  and nextbranchid in("
				+ nextbranchids + ") GROUP BY customerid";

		return jdbcTemplate.query(sql, new DeliveryGroupCustomeridMapper(), begindate, enddate, kufangid);
	}

	public List<CwbDiuShi> getCwbDiuShiByoutstoreroomtimeAndstartbranchid(long kufangid, String begindate, String enddate, long customerid, String nextbranchids) {
		String sql = "SELECT COUNT(1) as id,nextbranchid FROM op_cwb_diushi " + "FORCE INDEX(Diushi_Shenhetime_Idx) WHERE outstoreroomtime >=? AND outstoreroomtime <=? "
				+ "AND startbranchid=? and customerid=? ";
		if (nextbranchids.length() > 0) {
			sql += " and nextbranchid in(" + nextbranchids + ")";
		}
		sql += " GROUP BY nextbranchid";
		return jdbcTemplate.query(sql, new DeliveryNotDetailMapper(), begindate, enddate, kufangid, customerid);
	}

}
