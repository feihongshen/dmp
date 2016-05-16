package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.pjbest.splitting.aspect.DataSource;
import com.pjbest.splitting.routing.DatabaseType;

import cn.explink.domain.TuihuoRecord;
import cn.explink.util.Page;

@Component
public class TuihuoRecordDAO {
	private Logger logger = LoggerFactory.getLogger(TuihuoRecordDAO.class);

	private final class TuihuoRecordMapper implements RowMapper<TuihuoRecord> {

		@Override
		public TuihuoRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
			TuihuoRecord tuihuoRecord = new TuihuoRecord();
			tuihuoRecord.setId(rs.getLong("id"));
			tuihuoRecord.setCwb(rs.getString("cwb"));
			tuihuoRecord.setBranchid(rs.getLong("branchid"));
			tuihuoRecord.setTuihuobranchid(rs.getLong("tuihuobranchid"));
			tuihuoRecord.setTuihuochuzhantime(rs.getString("tuihuochuzhantime"));
			tuihuoRecord.setTuihuozhanrukutime(rs.getString("tuihuozhanrukutime"));
			tuihuoRecord.setCustomerid(rs.getLong("customerid"));
			tuihuoRecord.setCwbordertypeid(rs.getInt("cwbordertypeid"));
			tuihuoRecord.setUserid(rs.getLong("userid"));
			return tuihuoRecord;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<TuihuoRecord> getTuihuoRecordByCwb(String cwb) {
		return jdbcTemplate.query("SELECT * from ops_tuihuorecord where cwb=? order by tuihuochuzhantime desc", new TuihuoRecordMapper(), cwb);
	}

	public long creTuihuoRecord(final TuihuoRecord tr) {
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into ops_tuihuorecord(cwb,branchid,tuihuobranchid,tuihuochuzhantime,customerid,cwbordertypeid,userid) values(?,?,?,?,?,?,?)", new String[] { "id" });
				ps.setString(1, tr.getCwb());
				ps.setLong(2, tr.getBranchid());
				ps.setLong(3, tr.getTuihuobranchid());
				ps.setString(4, tr.getTuihuochuzhantime());
				ps.setLong(5, tr.getCustomerid());
				ps.setLong(6, tr.getCwbordertypeid());
				ps.setLong(7, tr.getUserid());
				return ps;
			}
		}, key);
		tr.setId(key.getKey().longValue());
		return key.getKey().longValue();
	}

	public long updateTuihuoRecord(final TuihuoRecord tr) {
		try {
			String sql = "update ops_tuihuorecord set branchid=?,tuihuobranchid=?,tuihuochuzhantime=?,cwbordertypeid=?,userid=? where cwb=?";
			jdbcTemplate.update(sql, tr.getBranchid(), tr.getTuihuobranchid(), tr.getTuihuochuzhantime(), tr.getCwbordertypeid(), tr.getUserid(), tr.getCwb());
			return 1;
		} catch (Exception e) {
			logger.info("失败" + e);
			return 0;
		}
	}

	public void saveTuihuoRecordById(String tuihuozhanrukutime, long id) {
		String sql = "update ops_tuihuorecord set tuihuozhanrukutime=? where id=?";
		jdbcTemplate.update(sql, tuihuozhanrukutime, id);
	}

	@DataSource(DatabaseType.REPLICA)
	public List<TuihuoRecord> getTuihuoRecordByTuihuochuzhan(String begindate, String enddate, String branchids, String customerids, long istuihuozhanruku, long page) {
		String sql = "SELECT * from ops_tuihuorecord where tuihuochuzhantime >= ? and tuihuochuzhantime <=? ";
		if (branchids.length() > 0 || customerids.length() > 0 || istuihuozhanruku > 0) {
			if (branchids.length() > 0) {
				sql += " and branchid in(" + branchids + ")";
			}
			if (customerids.length() > 0) {
				sql += " and customerid in(" + customerids + ")";
			}
			if (istuihuozhanruku != 0) {
				if (istuihuozhanruku == 1) {
					sql += " and tuihuozhanrukutime <>'' and tuihuozhanrukutime >=tuihuochuzhantime ";
				} else {
					sql += " and tuihuochuzhantime>tuihuozhanrukutime ";
				}
			}
		}
		sql += "  limit ?,?";
		return jdbcTemplate.query(sql, new TuihuoRecordMapper(), begindate, enddate, (page - 1) * Page.ONE_PAGE_NUMBER, Page.ONE_PAGE_NUMBER);
	}

	@DataSource(DatabaseType.REPLICA)
	public long getCountTuihuoRecordByTuihuochuzhan(String begindate, String enddate, String branchids, String customerids, long istuihuozhanruku) {
		String sql = "SELECT count(1) from ops_tuihuorecord where tuihuochuzhantime >= ? and tuihuochuzhantime <=? ";
		if (branchids.length() > 0 || customerids.length() > 0 || istuihuozhanruku > 0) {
			if (branchids.length() > 0) {
				sql += " and branchid in(" + branchids + ")";
			}
			if (customerids.length() > 0) {
				sql += " and customerid in(" + customerids + ")";
			}
			if (istuihuozhanruku != 0) {
				if (istuihuozhanruku == 1) {
					sql += " and tuihuozhanrukutime <>'' and tuihuozhanrukutime >=tuihuochuzhantime ";
				} else {
					sql += " and tuihuochuzhantime>tuihuozhanrukutime ";
				}
			}
		}
		return jdbcTemplate.queryForLong(sql, begindate, enddate);
	}

	public List<TuihuoRecord> getTuihuoRecordByTuihuozhanruku(String begindate, String enddate, String branchid, String customerid, String cwbordertypeid) {
		String sql = "";
		if (begindate.length()>0||enddate.length()>0||branchid.length() > 0 || customerid.length() > 0 || cwbordertypeid.length() > 0) {
			StringBuffer w = new StringBuffer();
			sql = "SELECT * from ops_tuihuorecord  where id<>0";
			if(begindate.length()>0){
				w.append(" and tuihuozhanrukutime>="+begindate);
			}
			if(enddate.length()>0){
				w.append(" and tuihuozhanrukutime <="+enddate);
			}
			if (branchid.length() > 0) {
				w.append(" and branchid="+branchid);
			}
			if (customerid.length() > 0) {
				w.append(" and customerid="+customerid);
			}
			if (cwbordertypeid.length() > 0) {
				w.append(" and cwbordertypeid="+cwbordertypeid);
			}
			sql += w.toString();
		}
		return jdbcTemplate.query(sql, new TuihuoRecordMapper());
	}

	@DataSource(DatabaseType.REPLICA)
	public long getCountTuihuoRecordByTuihuozhanruku(String begindate, String enddate, String branchids, String customerids, String cwbordertypeids) {
		String sql = "SELECT count(1) from ops_tuihuorecord  where tuihuozhanrukutime >= ? and tuihuozhanrukutime <= ? ";
		if (branchids.length() > 0 || customerids.length() > 0 || cwbordertypeids.length() > 0) {
			StringBuffer w = new StringBuffer();
			if (branchids.length() > 0) {
				w.append(" and branchid in(" + branchids + ")");
			}
			if (customerids.length() > 0) {
				w.append(" and customerid in(" + customerids + ")");
			}
			if (cwbordertypeids.length() > 0) {
				w.append(" and cwbordertypeid in(" + cwbordertypeids + ")");
			}
			sql += w.toString();
		}
		return jdbcTemplate.queryForLong(sql, begindate, enddate);
	}

	@DataSource(DatabaseType.REPLICA)
	public List<TuihuoRecord> getTuihuoRecordByTuihuozhanrukuOnPage(String begindate, String enddate, String branchids, String customerids, String cwbordertypeids, long page) {
		String sql = "SELECT * from ops_tuihuorecord  where tuihuozhanrukutime >= ? and tuihuozhanrukutime <= ? ";
		if (branchids.length() > 0 || customerids.length() > 0 || cwbordertypeids.length() > 0) {
			StringBuffer w = new StringBuffer();
			if (branchids.length() > 0) {
				w.append(" and branchid in(" + branchids + ")");
			}
			if (customerids.length() > 0) {
				w.append(" and customerid in(" + customerids + ")");
			}
			if (cwbordertypeids.length() > 0) {
				w.append(" and cwbordertypeid in(" + cwbordertypeids + ")");
			}
			sql += w.toString();
		}
		sql += " limit ?,?";
		return jdbcTemplate.query(sql, new TuihuoRecordMapper(), begindate, enddate, (page - 1) * Page.ONE_PAGE_NUMBER, Page.ONE_PAGE_NUMBER);
	}

	public List<TuihuoRecord> getTuihuoRecordByCwbs(List<String> cwbs) {
		StringBuilder stringBuilder = new StringBuilder("select * from ops_tuihuorecord  where cwb in (");
		for (String cwb : cwbs) {
			stringBuilder.append("'").append(cwb).append("',");
		}
		if (cwbs.size() > 0) {
			stringBuilder.deleteCharAt(stringBuilder.length() - 1);
		}
		stringBuilder.append(")  ");
		return jdbcTemplate.query(stringBuilder.toString(), new TuihuoRecordMapper());
	}

	/**
	 * sql语句
	 * 
	 * @param begindate
	 * @param enddate
	 * @param branchids
	 * @param customerids
	 * @param istuihuozhanruku
	 * @return
	 */
	public String getSqlTuihuoRecordByTuihuo(String begindate, String enddate, String branchids, String customerids, String types) {
		String sql = "SELECT cb.* from ops_tuihuorecord  as op left join   express_ops_cwb_detail as cb on op.cwb=cb.cwb where cb.state=1 ";
		sql += " and op.tuihuozhanrukutime >= '" + begindate + "' and op.tuihuozhanrukutime <='" + enddate + "'";
		if (branchids.length() > 0 || customerids.length() > 0 || types.length() > 0) {
			if (branchids.length() > 0) {
				sql += " and op.branchid in(" + branchids + ")";
			}
			if (customerids.length() > 0) {
				sql += " and op.customerid in(" + customerids + ")";
			}
			if (types.length() > 0) {
				sql += " and op.cwbordertypeid in(" + types + ")";
			}
		}
		return sql;
	}

	public String getSqlTuihuoRecordBychuzhan(String begindate, String enddate, String branchids, String customerids, String types, long istuihuozhanruku) {
		String sql = "SELECT cb.* from ops_tuihuorecord  as op left join   express_ops_cwb_detail as cb on op.cwb=cb.cwb where cb.state=1 ";
		sql += " and op.tuihuochuzhantime >= '" + begindate + "' and op.tuihuochuzhantime <='" + enddate + "' ";
		if (istuihuozhanruku == 1) {
			// sql+=" and op.tuihuozhanrukutime >= '"+begindate+"' and op.tuihuozhanrukutime <='"+enddate+"'"
			// ;
			sql += " and tuihuochuzhantime<=tuihuozhanrukutime ";
		} else if (istuihuozhanruku == 2) {
			// sql+=" and op.tuihuochuzhantime >= '"+begindate+"' and op.tuihuochuzhantime <='"+enddate+"'"
			// ;
			sql += " and tuihuochuzhantime>tuihuozhanrukutime ";
		}
		if (branchids.length() > 0 || customerids.length() > 0 || istuihuozhanruku > 0 || types.length() > 0) {
			if (branchids.length() > 0) {
				sql += " and op.branchid in(" + branchids + ")";
			}
			if (customerids.length() > 0) {
				sql += " and op.customerid in(" + customerids + ")";
			}
			if (types.length() > 0) {
				sql += " and op.cwbordertypeid in(" + types + ")";
			}
		}
		return sql;
	}

}
