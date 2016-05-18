package cn.explink.b2c.tools.b2cmonntor;

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

import com.pjbest.splitting.aspect.DataSource;
import com.pjbest.splitting.routing.DatabaseType;

import cn.explink.util.Page;

@Component
public class B2cAutoDownloadMonitorDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class B2cDownloadMonitor implements RowMapper<B2cAutoDownloadMonitor> {
		@Override
		public B2cAutoDownloadMonitor mapRow(ResultSet rs, int rowNum) throws SQLException {
			B2cAutoDownloadMonitor en = new B2cAutoDownloadMonitor();
			en.setId(rs.getLong("id"));
			en.setCretime(rs.getString("cretime"));
			en.setCustomerid(rs.getString("customerid"));
			en.setCount(rs.getLong("count"));
			en.setRemark(rs.getString("remark"));

			return en;
		}
	}

	@DataSource(DatabaseType.REPLICA)
	public List<B2cAutoDownloadMonitor> selectB2cMonitorDataList(String customerid, String starttime, String endtime, long page) {
		String sql = "SELECT * FROM  b2c_download_record where 1=1  ";
		sql += this.whereSqlByB2cMonitorData(customerid, starttime, endtime);
		if (page > 0) {
			sql += " ORDER BY cretime DESC limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		} else {
			sql += " ORDER BY cretime DESC  ";
		}

		return jdbcTemplate.query(sql, new B2cDownloadMonitor());
	}

	@DataSource(DatabaseType.REPLICA)
	public long selectB2cMonitorDataCount(String customerid, String starttime, String endtime) {
		String sql = "SELECT count(1) FROM  b2c_download_record where 1=1  ";
		sql += this.whereSqlByB2cMonitorData(customerid, starttime, endtime);

		return jdbcTemplate.queryForLong(sql);
	}

	private String whereSqlByB2cMonitorData(String customerid, String starttime, String endtime) {
		String sql = "";
		if (customerid.length() > 0) {
			sql += " and    customerid  like '%" + customerid + "%'  ";
		}
		if (!"".equals(starttime) && !"".equals(endtime)) {
			sql += " and cretime between '" + starttime + "' and '" + endtime + "' ";
		}

		return sql;
	}

	public void saveB2cDownloadRecord(final String customerid, final String cretime, final long count, final String remark) {
		try {
			jdbcTemplate.update("insert into b2c_download_record(customerid,cretime,count,remark) values (?,?,?,?)", new PreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					// TODO Auto-generated method stub
					ps.setString(1, customerid);
					ps.setString(2, cretime);
					ps.setLong(3, count);
					ps.setString(4, remark);

				}
			});
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
