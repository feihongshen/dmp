package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import cn.explink.domain.OutWarehouseGroup;
import cn.explink.enumutil.OutWarehouseGroupEnum;
import cn.explink.enumutil.OutwarehousegroupOperateEnum;
import cn.explink.util.Page;

@Component
public class OutWarehouseGroupDAO {

	private final class OutWarehouseGroupRowMapper implements RowMapper<OutWarehouseGroup> {
		@Override
		public OutWarehouseGroup mapRow(ResultSet rs, int rowNum) throws SQLException {
			OutWarehouseGroup owg = new OutWarehouseGroup();
			owg.setId(rs.getLong("id"));
			owg.setCredate(rs.getTimestamp("credate"));
			owg.setDriverid(rs.getLong("driverid"));
			owg.setTruckid(rs.getLong("truckid"));
			owg.setState(rs.getInt("state"));
			owg.setBranchid(rs.getInt("branchid"));
			owg.setPrinttime(rs.getString("printtime"));
			owg.setOperatetype(rs.getLong("operatetype"));
			return owg;
		}

	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public long creOutWarehouseGroup(final long driverid, final long truckid, final long branchid, final long operatetype) {

		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into express_ops_outwarehousegroup (driverid,truckid,state,branchid,printtime,operatetype) " + "values(?,?,?,?,?,?)", new String[] { "id" });
				ps.setLong(1, driverid);
				ps.setLong(2, truckid);
				if (operatetype == OutwarehousegroupOperateEnum.FenZhanLingHuo.getValue()) {
					ps.setLong(3, OutWarehouseGroupEnum.PaiSongZhong.getValue());
				} else {
					ps.setLong(3, OutWarehouseGroupEnum.SaoMiaoZhong.getValue());
				}
				ps.setLong(4, branchid);
				ps.setString(5, "");
				ps.setLong(6, operatetype);
				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

	public void creOutWarehouseGroupForDeliver(final long driverid, int id) {
		String sql = "update express_ops_outwarehousegroup set driverid=?,state=1 where id=?";
		jdbcTemplate.update(sql, driverid, id);
	}

	public void editOutWarehouseGroup(long id) {
		jdbcTemplate.update("update express_ops_outwarehousegroup set state=1 where id =" + id);
	}

	public void editOutWarehouseGroupForState(long state, long id) {
		jdbcTemplate.update("update express_ops_outwarehousegroup set state=" + state + " where id =" + id);
	}

	// //////////////////////////////////////分页查询部分///////////////////////////////////////////////////
	private String getOutWarehouseGroupByPageWhereSql(String sql, long branchid, String beginemaildate, String endemaildate, long driverid, long operatetype) {
		if (branchid > 0 || beginemaildate.length() > 0 || endemaildate.length() > 0 || driverid > 0 || operatetype > 0) {
			StringBuffer w = new StringBuffer();
			sql += " where ";
			if (branchid > 0) {
				w.append(" and branchid=" + branchid);
			}
			if (driverid > 0) {
				w.append(" and driverid=" + driverid);
			}
			if (beginemaildate.length() > 0) {
				w.append(" and credate > '" + beginemaildate + "'");
			}
			if (endemaildate.length() > 0) {
				w.append(" and credate < '" + endemaildate + "'");
			}
			if (operatetype == OutwarehousegroupOperateEnum.FenZhanLingHuo.getValue()) {
				w.append(" and state=3 and operatetype=" + operatetype + " order by printtime");
			} else {
				w.append(" and state=1 and operatetype=" + operatetype + " order by printtime");
			}
			sql += w.substring(4, w.length());
		}
		return sql;
	}

	public List<OutWarehouseGroup> getOutWarehouseGroupByPage(long page, long branchid, String beginemaildate, String endemaildate, long driverid, long operatetype) {
		String sql = "select * from express_ops_outwarehousegroup";
		sql = this.getOutWarehouseGroupByPageWhereSql(sql, branchid, beginemaildate, endemaildate, driverid, operatetype);
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		List<OutWarehouseGroup> outwarehouseList = jdbcTemplate.query(sql, new OutWarehouseGroupRowMapper());
		return outwarehouseList;
	}

	public long getOutWarehouseGroupCount(long branchid, String beginemaildate, String endemaildate, long driverid, long operatetype) {
		String sql = "select count(1) from express_ops_outwarehousegroup";
		sql = this.getOutWarehouseGroupByPageWhereSql(sql, branchid, beginemaildate, endemaildate, driverid, operatetype);
		return jdbcTemplate.queryForInt(sql);
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////
	public List<OutWarehouseGroup> getOutWarehouseGroupByid(long page, String id) {
		String sql = "select * from express_ops_outwarehousegroup where id in (" + id + ")";
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new OutWarehouseGroupRowMapper());
	}

	public OutWarehouseGroup getOutWarehouseGroupByid(long id) {
		try {
			String sql = "select * from express_ops_outwarehousegroup where id=?";
			return jdbcTemplate.queryForObject(sql, new OutWarehouseGroupRowMapper(), id);
		} catch (EmptyResultDataAccessException erdae) {
			return null;
		}
	}

	public OutWarehouseGroup getOutWarehouseGroupByWhere(long branchid, long driverid, long truckid, long state, long operatetype) {
		try {
			String sql = "select * from express_ops_outwarehousegroup ";
			sql = getOutWarehouseByWhereSql(sql, branchid, driverid, truckid, state, operatetype);
			return jdbcTemplate.queryForObject(sql, new OutWarehouseGroupRowMapper());
		} catch (EmptyResultDataAccessException erd) {
			return null;
		}
	}

	public OutWarehouseGroup getOutWarehouseGroupByDT(long driverid, long truckid) {
		try {
			String sql = "select * from express_ops_outwarehousegroup where driverid=" + driverid + " and truckid=" + truckid + " and state=0";
			return jdbcTemplate.queryForObject(sql, new OutWarehouseGroupRowMapper());
		} catch (EmptyResultDataAccessException erd) {
			return null;
		}
	}

	public long getOutWarehouseCount(long state1, long branchid, long driverid, long truckid, long state2, long operatetype) {
		String sql = "update express_ops_outwarehousegroup set state=" + state1;
		sql = getOutWarehouseByWhereSql(sql, branchid, driverid, truckid, state2, operatetype);
		return jdbcTemplate.update(sql);
	}

	private String getOutWarehouseByWhereSql(String sql, long branchid, long driverid, long truckid, long state, long operatetype) {
		if (branchid > 0 || driverid > 0 || truckid > 0 || state > 0 || operatetype > 0) {
			StringBuffer w = new StringBuffer();
			sql += " where ";
			if (branchid > 0) {
				w.append(" and branchid=" + branchid);
			}
			if (driverid > 0) {
				w.append(" and driverid=" + driverid);
			}
			if (truckid > 0) {
				w.append(" and truckid=" + truckid);
			}
			w.append(" and state=" + state + " and printtime='' and operatetype=" + operatetype);
			sql += w.substring(4, w.length());
		}
		return sql;
	}

	public void savePrinttime(String printtime, long id) {
		String sql = "update express_ops_outwarehousegroup set printtime='" + printtime + "' where id=" + id;
		jdbcTemplate.update(sql);
	}

	public void savePrinttimeAndState(String printtime, long state, long id) {
		String sql = "update express_ops_outwarehousegroup set printtime='" + printtime + "',state=" + state + " where id=" + id;
		jdbcTemplate.update(sql);
	}

	public void saveByBranchidAndOperateType(long driverid, long truckid, long branchid, long operatetype) {
		String sql = "update express_ops_outwarehousegroup set driverid=?,truckid=? where branchid=? and operatetype=?";
		jdbcTemplate.update(sql, driverid, truckid, branchid, operatetype);
	}

	public List<OutWarehouseGroup> getAllOutWarehouseGroupByWhere(long branchid, long driverid, long truckid, long state, long operatetype) {
		try {
			String sql = "select * from express_ops_outwarehousegroup ";
			sql = getOutWarehouseByWhereSql(sql, branchid, driverid, truckid, state, operatetype);
			return jdbcTemplate.query(sql, new OutWarehouseGroupRowMapper());
		} catch (EmptyResultDataAccessException erd) {
			return null;
		}
	}
}
