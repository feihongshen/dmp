package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.ChukuDataResult;
import cn.explink.util.StringUtil;

@Component
public class ChukuDataResultDAO {
	private final class ChukuDataResultMapper implements RowMapper<ChukuDataResult> {
		@Override
		public ChukuDataResult mapRow(ResultSet rs, int rowNum) throws SQLException {
			ChukuDataResult cdr = new ChukuDataResult();

			cdr.setId(rs.getLong("id"));
			cdr.setBegindate(StringUtil.nullConvertToEmptyString(rs.getString("begindate")));
			cdr.setEnddate(StringUtil.nullConvertToEmptyString(rs.getString("enddate")));
			cdr.setKufangid(rs.getLong("kufangid"));
			cdr.setCustomerid(rs.getLong("customerid"));
			cdr.setResult(rs.getString("result"));
			cdr.setExportid(rs.getLong("exportid"));

			return cdr;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void creChukudataResult(final ChukuDataResult cdr) {

		jdbcTemplate.update("INSERT INTO `ops_chukudataresult`(`begindate`,`enddate`,`kufangid`,`customerid`," + "`result`,`exportid`) " + " VALUES ( ?,?,?,?,?,?)", new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, cdr.getBegindate());
				ps.setString(2, cdr.getEnddate());
				ps.setLong(3, cdr.getKufangid());
				ps.setLong(4, cdr.getCustomerid());
				ps.setString(5, cdr.getResult());
				ps.setLong(6, cdr.getExportid());
			}
		});

	}

	public List<ChukuDataResult> getChukuDataResultByExportid(long exportid) {
		String sql = "select * from ops_chukudataresult where exportid=? ";
		return jdbcTemplate.query(sql, new ChukuDataResultMapper(), exportid);
	}

}
