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

import com.pjbest.splitting.aspect.DataSource;
import com.pjbest.splitting.routing.DatabaseType;

import cn.explink.domain.OutWarehouseGroup;
import cn.explink.enumutil.OutWarehouseGroupEnum;
import cn.explink.enumutil.OutwarehousegroupOperateEnum;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

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
			owg.setCustomerid(rs.getLong("customerid"));
			owg.setCurrentbranchid(rs.getLong("currentbranchid"));
			owg.setCwbs(StringUtil.nullConvertToEmptyString(rs.getString("cwbs")));
			owg.setSign(rs.getLong("sign"));
			owg.setBaleno(rs.getString("baleno"));
			owg.setBaleid(rs.getLong("baleid"));
			return owg;
		}

	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<OutWarehouseGroup> getAllOutWarehouse() {
		String sql = "select * from express_ops_outwarehousegroup";
		return this.jdbcTemplate.query(sql, new OutWarehouseGroupRowMapper());
	}

	public long creOutWarehouseGroup(final long driverid, final long truckid, final long branchid, final String printtime, final long operatetype, final long customerid, final long currentbranchid,
			final String cwbs) {

		KeyHolder key = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into express_ops_outwarehousegroup (driverid,truckid,state,branchid,printtime,operatetype,customerid,currentbranchid,cwbs,sign) "
						+ "values(?,?,?,?,?,?,?,?,?,?)", new String[] { "id" });
				ps.setLong(1, driverid);
				ps.setLong(2, truckid);
				if (operatetype == OutwarehousegroupOperateEnum.FenZhanLingHuo.getValue()) {
					ps.setLong(3, OutWarehouseGroupEnum.PaiSongZhong.getValue());
				} else {
					ps.setLong(3, OutWarehouseGroupEnum.SaoMiaoZhong.getValue());
				}
				ps.setLong(4, branchid);
				ps.setString(5, printtime);
				ps.setLong(6, operatetype);
				ps.setLong(7, customerid);
				ps.setLong(8, currentbranchid);
				ps.setString(9, cwbs);
				ps.setLong(10, 1);

				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

	public void creOutWarehouseGroupForDeliver(final long driverid, int id) {
		String sql = "update express_ops_outwarehousegroup set driverid=?,state=1 where id=?";
		this.jdbcTemplate.update(sql, driverid, id);
	}

	public void editOutWarehouseGroup(long id) {
		this.jdbcTemplate.update("update express_ops_outwarehousegroup set state=1 where id =?", id);
	}

	public void editOutWarehouseGroupForState(long state, long id) {
		this.jdbcTemplate.update("update express_ops_outwarehousegroup set state=? where id =?", state, id);
	}

	private String getOutWarehouseGroupByPageWhereSql2(String sql, long branchid, String beginemaildate, String endemaildate, long driverid,long truckid, long operatetype, long customerid, long currentbranchid) {
		if ((branchid > 0) || (beginemaildate.length() > 0) || (endemaildate.length() > 0) || (driverid > 0) || (operatetype > 0) || (customerid > 0)) {
			StringBuffer w = new StringBuffer();
			sql += " where ";
			if(truckid>0){
				w.append(" and truckid=" + truckid);
			}
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
			if (customerid > 0) {
				w.append(" and customerid=" + customerid);
			}
			if (operatetype == OutwarehousegroupOperateEnum.FenZhanLingHuo.getValue()) {
				w.append(" and state in(" + OutWarehouseGroupEnum.PaiSongZhong.getValue() + "," + OutWarehouseGroupEnum.FengBao.getValue() + ") and operatetype=" + operatetype);
			} else {
				w.append(" and state in(" + OutWarehouseGroupEnum.SaoMiaoZhong.getValue() + "," + OutWarehouseGroupEnum.FengBao.getValue() + ") and operatetype=" + operatetype);
			}
			sql += w.substring(4, w.length());
		}
		if (sql.length() == 0) {
			sql += " where currentbranchid=" + currentbranchid + " order by printtime desc";
		} else {
			sql += " and currentbranchid=" + currentbranchid + " order by printtime desc";
		}
		return sql;
	}




	// //////////////////////////////////////分页查询部分///////////////////////////////////////////////////
	private String getOutWarehouseGroupByPageWhereSql(String sql, long branchid, String beginemaildate, String endemaildate, long driverid, long operatetype, long customerid, long currentbranchid) {
		if ((branchid > 0) || (beginemaildate.length() > 0) || (endemaildate.length() > 0) || (driverid > 0) || (operatetype > 0) || (customerid > 0)) {
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
			if (customerid > 0) {
				w.append(" and customerid=" + customerid);
			}
			if (operatetype == OutwarehousegroupOperateEnum.FenZhanLingHuo.getValue()) {
				w.append(" and state in(" + OutWarehouseGroupEnum.PaiSongZhong.getValue() + "," + OutWarehouseGroupEnum.FengBao.getValue() + ") and operatetype=" + operatetype);
			} else {
				w.append(" and state in(" + OutWarehouseGroupEnum.SaoMiaoZhong.getValue() + "," + OutWarehouseGroupEnum.FengBao.getValue() + ") and operatetype=" + operatetype);
			}
			sql += w.substring(4, w.length());
		}
		if (sql.length() == 0) {
			sql += " where currentbranchid=" + currentbranchid + " order by printtime desc";
		} else {
			sql += " and currentbranchid=" + currentbranchid + " order by printtime desc";
		}
		return sql;
	}

	private String getOutWarehouseGroupByPageWhereSql(String sql, String branchid, String beginemaildate, String endemaildate, long driverid, long operatetype, long customerid) {
		if ((branchid.length() > 0) || (beginemaildate.length() > 0) || (endemaildate.length() > 0) || (driverid > 0) || (operatetype > 0) || (customerid > 0)) {
			StringBuffer w = new StringBuffer();
			sql += " where ";
			if (branchid.length() > 0) {
				w.append(" and branchid in(" + branchid + ")");
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
			if (customerid > 0) {
				w.append(" and customerid=" + customerid);
			}
			if (operatetype == OutwarehousegroupOperateEnum.FenZhanLingHuo.getValue()) {
				w.append(" and state in(" + OutWarehouseGroupEnum.PaiSongZhong.getValue() + "," + OutWarehouseGroupEnum.FengBao.getValue() + ") and operatetype=" + operatetype + " order by printtime");
			} else {
				w.append(" and state in(" + OutWarehouseGroupEnum.SaoMiaoZhong.getValue() + "," + OutWarehouseGroupEnum.FengBao.getValue() + ") and operatetype=" + operatetype + " order by printtime");
			}
			sql += w.substring(4, w.length());
		}
		return sql;
	}

	@DataSource(DatabaseType.REPLICA)
	public List<OutWarehouseGroup> getOutWarehouseGroupByPage(long page, long branchid, String beginemaildate, String endemaildate, long driverid, long operatetype, long customerid,
			long currentbranchid) {
		String sql = "select * from express_ops_outwarehousegroup";
		sql = this.getOutWarehouseGroupByPageWhereSql(sql, branchid, beginemaildate, endemaildate, driverid, operatetype, customerid, currentbranchid);
		sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		List<OutWarehouseGroup> outwarehouseList = this.jdbcTemplate.query(sql, new OutWarehouseGroupRowMapper());
		return outwarehouseList;
	}

	@DataSource(DatabaseType.REPLICA)
	public List<OutWarehouseGroup> getOutWarehouseGroupByPage2(long page, long branchid, String beginemaildate, String endemaildate, long driverid,long truckid, long operatetype, long customerid,
			long currentbranchid) {
		String sql = "select * from express_ops_outwarehousegroup";
		sql = this.getOutWarehouseGroupByPageWhereSql2(sql, branchid, beginemaildate, endemaildate, driverid,truckid, operatetype, customerid, currentbranchid);
		sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		List<OutWarehouseGroup> outwarehouseList = this.jdbcTemplate.query(sql, new OutWarehouseGroupRowMapper());
		return outwarehouseList;
	}

	@DataSource(DatabaseType.REPLICA)
	public List<OutWarehouseGroup> getOutWarehouseGroupByPage(long page, String branchid, String beginemaildate, String endemaildate, long driverid, long operatetype, long customerid) {
		String sql = "select * from express_ops_outwarehousegroup";
		sql = this.getOutWarehouseGroupByPageWhereSql(sql, branchid, beginemaildate, endemaildate, driverid, operatetype, customerid);
		sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		List<OutWarehouseGroup> outwarehouseList = this.jdbcTemplate.query(sql, new OutWarehouseGroupRowMapper());
		return outwarehouseList;
	}

	@DataSource(DatabaseType.REPLICA)
	public long getOutWarehouseGroupCount(long branchid, String beginemaildate, String endemaildate, long driverid, long operatetype, long customerid, long currentbranchid) {
		String sql = "select count(1) from express_ops_outwarehousegroup";
		sql = this.getOutWarehouseGroupByPageWhereSql(sql, branchid, beginemaildate, endemaildate, driverid, operatetype, customerid, currentbranchid);
		return this.jdbcTemplate.queryForInt(sql);
	}

	@DataSource(DatabaseType.REPLICA)
	public long getOutWarehouseGroupCount(String branchid, String beginemaildate, String endemaildate, long driverid, long operatetype, long customerid) {
		String sql = "select count(1) from express_ops_outwarehousegroup";
		sql = this.getOutWarehouseGroupByPageWhereSql(sql, branchid, beginemaildate, endemaildate, driverid, operatetype, customerid);
		return this.jdbcTemplate.queryForInt(sql);
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////
	public List<OutWarehouseGroup> getOutWarehouseGroupByid(long page, String id) {
		String sql = "select * from express_ops_outwarehousegroup where id in (" + id + ")";
		sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		return this.jdbcTemplate.query(sql, new OutWarehouseGroupRowMapper());
	}

	public OutWarehouseGroup getOutWarehouseGroupByid(long id) {
		try {
			String sql = "select * from express_ops_outwarehousegroup where id=?";
			return this.jdbcTemplate.queryForObject(sql, new OutWarehouseGroupRowMapper(), id);
		} catch (EmptyResultDataAccessException erdae) {
			return null;
		}
	}

	public OutWarehouseGroup getOutWarehouseGroupByWhere(long branchid, long driverid, long truckid, long state, long operatetype, long customerid, String credate) {
		try {
			String sql = "select * from express_ops_outwarehousegroup ";
			sql = this.getOutWarehouseByWhereSql(sql, 0, branchid, driverid, truckid, state, operatetype, customerid, credate);
			sql += " order by credate desc";
			List<OutWarehouseGroup> owgList = this.jdbcTemplate.query(sql, new OutWarehouseGroupRowMapper());
			if (owgList.size() > 0) {
				return owgList.get(0);
			}
			return null;
		} catch (EmptyResultDataAccessException erd) {
			return null;
		}
	}

	public OutWarehouseGroup getOutWarehouseGroupByDT(long driverid, long truckid) {
		try {
			String sql = "select * from express_ops_outwarehousegroup where driverid=? and truckid=? and state=0";
			return this.jdbcTemplate.queryForObject(sql, new OutWarehouseGroupRowMapper(), driverid, truckid);
		} catch (EmptyResultDataAccessException erd) {
			return null;
		}
	}

	public long getOutWarehouseCount(long id, long state1, long branchid, long driverid, long truckid, long state2, long operatetype, long customerid) {
		String sql = "update express_ops_outwarehousegroup set state=?";
		sql = this.getOutWarehouseByWhereSql(sql, id, branchid, driverid, truckid, state2, operatetype, customerid, "");
		return this.jdbcTemplate.update(sql, state1);
	}

	public void saveOutWarehouseById(long state, long id) {
		String sql = "update express_ops_outwarehousegroup set state=? where id=?";
		this.jdbcTemplate.update(sql, state, id);
	}

	private String getOutWarehouseByWhereSql(String sql, long id, long branchid, long driverid, long truckid, long state, long operatetype, long customerid, String credate) {
		if ((id > 0) || (branchid > 0) || (driverid > 0) || (truckid > 0) || (state > 0) || (operatetype > 0) || (customerid > 0) || (credate.length() > 0)) {
			StringBuffer w = new StringBuffer();
			sql += " where ";
			if (id > 0) {
				w.append(" and id=" + id);
			}
			if (branchid > 0) {
				w.append(" and branchid=" + branchid);
			}
			if (driverid > 0) {
				w.append(" and driverid=" + driverid);
			}
			if (truckid > 0) {
				w.append(" and truckid=" + truckid);
			}
			if (state > 0) {
				w.append(" and state=" + state);
			}
			if (customerid > 0) {
				w.append(" and customerid=" + customerid);
			}
			if (credate.length() > 0) {
				w.append(" and credate > '" + credate + "'");
			}
			w.append(" and printtime='' and operatetype=" + operatetype);
			sql += w.substring(4, w.length());
		}
		return sql;
	}

	public void savePrinttime(String printtime, long id) {
		String sql = "update express_ops_outwarehousegroup set printtime=? where id=?";
		this.jdbcTemplate.update(sql, printtime, id);
	}

	public void savePrinttimeAndState(String printtime, long state, long id) {
		String sql = "update express_ops_outwarehousegroup set printtime=?,state=? where id=?";
		this.jdbcTemplate.update(sql, printtime, state, id);
	}

	/**
	 * 针对其他批次扫描过程中的批次数据更新
	 *
	 * @param driverid
	 * @param truckid
	 * @param state
	 * @param branchid
	 * @param operatetype
	 */
	public void saveByBranchidAndOperateType(long driverid, long truckid, long state, long branchid, long operatetype) {
		String sql = "update express_ops_outwarehousegroup set driverid=?,truckid=?,state=? where branchid=? and operatetype=?";
		this.jdbcTemplate.update(sql, driverid, truckid, state, branchid, operatetype);
	}

	/**
	 * 针对小件员领货批次扫描过程中的批次数据更新
	 *
	 * @param driverid
	 * @param truckid
	 * @param state
	 * @param branchid
	 * @param operatetype
	 */
	public void saveByBranchidAndOperateTypeForDriverid(long driverid, long truckid, long state, long branchid, long operatetype) {
		String sql = "update express_ops_outwarehousegroup set truckid=?,state=? where branchid=? and operatetype=? and driverid=?";
		this.jdbcTemplate.update(sql, truckid, state, branchid, operatetype, driverid);
	}

	public OutWarehouseGroup getAllOutWarehouseGroupByWhere(long id, long branchid, long driverid, long truckid, long state, long operatetype, long customerid) {
		try {
			String sql = "select * from express_ops_outwarehousegroup ";
			sql = this.getOutWarehouseByWhereSql(sql, id, branchid, driverid, truckid, state, operatetype, customerid, "");
			sql += " order by credate desc";
			List<OutWarehouseGroup> olist = this.jdbcTemplate.query(sql, new OutWarehouseGroupRowMapper());
			if (olist.size() > 0) {
				return olist.get(0);
			}
			return null;
		} catch (EmptyResultDataAccessException erd) {
			return null;
		}
	}

	public List<OutWarehouseGroup> getOutWarehouseGroupByOperatetype(long operatetype) {
		String sql = "select * from express_ops_outwarehousegroup where operatetype=? and state=1";
		return this.jdbcTemplate.query(sql, new OutWarehouseGroupRowMapper(), operatetype);
	}

	public List<Long> getOutWarehouseGroupByCreatetime(String begindate, String enddate) {
		String sql = "select id from express_ops_outwarehousegroup where credate >= ? and credate <= ? and sign=0 ";
		return this.jdbcTemplate.queryForList(sql, Long.class, begindate, enddate);
	}

	public void saveOutWarehouseGroupByid(String cwbs, long id) {
		String sql = "update express_ops_outwarehousegroup set cwbs=?,sign=1 where id=? and sign=0";
		this.jdbcTemplate.update(sql, cwbs, id);
	}
	public int updateOutwarehousegroupBalenoByID(String baleno,long baleid,long id){
		String sql = "update express_ops_outwarehousegroup set baleno=?,baleid=? where id=?";
		return this.jdbcTemplate.update(sql,baleno,baleid,id);
	}
}
