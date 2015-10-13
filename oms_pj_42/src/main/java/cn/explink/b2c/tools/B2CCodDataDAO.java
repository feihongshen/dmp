package cn.explink.b2c.tools;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.B2CCodData;
import cn.explink.domain.B2CCodFile;

@Component
public class B2CCodDataDAO {
	@Autowired
	GetDmpDAO getDmpDAO;
	private Logger logger = LoggerFactory.getLogger(B2CCodDataDAO.class);

	private final class ComMapper implements RowMapper<B2CCodData> {
		@Override
		public B2CCodData mapRow(ResultSet rs, int rowNum) throws SQLException {
			B2CCodData b2CData = new B2CCodData();
			b2CData.setId(rs.getLong("id"));
			b2CData.setCustomerid(rs.getLong("customerid"));
			b2CData.setState(rs.getLong("state"));
			b2CData.setCwb(rs.getString("cwb"));
			b2CData.setDatajson(rs.getString("datajson"));
			b2CData.setPosttime(rs.getString("posttime"));
			b2CData.setCretime(rs.getString("cretime"));
			b2CData.setRemark(rs.getString("remark"));
			b2CData.setDeliverystate(rs.getLong("deliverystate"));
			return b2CData;
		}
	}

	private final class FileMapper implements RowMapper<B2CCodFile> {
		@Override
		public B2CCodFile mapRow(ResultSet rs, int rowNum) throws SQLException {
			B2CCodFile b2CFile = new B2CCodFile();
			b2CFile.setId(rs.getLong("id"));
			b2CFile.setFilecount(rs.getLong("filecount"));
			b2CFile.setType(rs.getLong("type"));

			return b2CFile;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<B2CCodData> selectB2cMonitorDataList(String customerid, long page) {
		String sql = "SELECT * FROM  express_send_b2c_cod where state in(0) and customerid=? limit 0," + page + " ";

		return jdbcTemplate.query(sql, new ComMapper(), customerid);
	}

	public void saveB2CData(final B2CCodData b2CData) {
		jdbcTemplate.update("insert into express_send_b2c_cod(`cwb`,`customerid`,`datajson`,`cretime`,`posttime`,`remark`,`deliverystate`)" + " VALUES ( ?,?,?,?,?,?,?)",
				new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						// TODO Auto-generated method stub
						ps.setString(1, b2CData.getCwb());
						ps.setLong(2, b2CData.getCustomerid());
						ps.setString(3, b2CData.getDatajson());
						ps.setString(4, b2CData.getCretime());
						ps.setString(5, b2CData.getPosttime());
						ps.setString(6, b2CData.getRemark());
						ps.setLong(7, b2CData.getDeliverystate());
					}
				});
	}

	public long check(String cwb, String posttime) {
		String sql = "SELECT count(1) FROM  express_send_b2c_cod where cwb='" + cwb + "' and posttime='" + posttime + "' ";
		return jdbcTemplate.queryForLong(sql);
	}

	public long checkState(String cwb, long state) {
		String sql = "SELECT count(1) FROM  express_send_b2c_cod where cwb='" + cwb + "' and state=" + state + " ";
		return jdbcTemplate.queryForLong(sql);
	}

	public long updateStateByCwbs(String cwbs, long state) {
		String sql = "update  express_send_b2c_cod set state=" + state + " where cwb in(" + cwbs + ") ";
		return jdbcTemplate.update(sql);
	}

	public long updateDatajsonByCwb(String cwb, String posttime, String datajson) {
		String sql = "update  express_send_b2c_cod set datajson=?,posttime=? where cwb =? and state=0 ";
		return jdbcTemplate.update(sql, datajson, posttime, cwb);
	}

	public B2CCodFile getFileCount(long type) {
		try {
			String sql = "SELECT * FROM  express_send_b2c_filecount where type=?  limit 0,1 ";
			return jdbcTemplate.queryForObject(sql, new FileMapper(), type);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	public void saveB2CFile(final long type) {
		jdbcTemplate.update("insert into express_send_b2c_filecount(`filecount`,`type`)" + " VALUES ( ?,?)", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setLong(1, 1);
				ps.setLong(2, type);

			}
		});
	}

	public long updateFileCount(long type, long filecount) {
		String sql = "update  express_send_b2c_filecount set filecount=? where type =? ";
		return jdbcTemplate.update(sql, filecount, type);
	}

}
