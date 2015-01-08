package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import cn.explink.domain.PrintcwbDetail;

@Repository
public class PrintcwbDetailDAO {

	private final class PrintcwbDetailRowMapper implements RowMapper<PrintcwbDetail> {
		@Override
		public PrintcwbDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			PrintcwbDetail printcwbDetail = new PrintcwbDetail();
			printcwbDetail.setId(rs.getLong("id"));
			printcwbDetail.setUserid(rs.getLong("userid"));
			printcwbDetail.setOperatetype(rs.getLong("operatetype"));
			printcwbDetail.setCredate(rs.getTimestamp("credate"));
			printcwbDetail.setPrintdetail(rs.getString("printdetail"));

			return printcwbDetail;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 创建一条打印记录
	 *
	 * @param pd
	 * @return key
	 */
	public long crePrintcwbDetail(final PrintcwbDetail pd) {
		KeyHolder key = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into express_ops_printcwb_detail (userid,credate,operatetype,printdetail) " + "values(?,?,?,? )", new String[] { "floworderid" });
				ps.setLong(1, pd.getUserid());
				ps.setTimestamp(2, pd.getCredate());
				ps.setLong(3, pd.getOperatetype());
				ps.setString(4, pd.getPrintdetail() == null ? "" : pd.getPrintdetail().toString());
				return ps;
			}
		}, key);
		pd.setId(key.getKey().longValue());
		return key.getKey().longValue();
	}

}
