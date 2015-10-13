package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import cn.explink.domain.ExceedFee;

@Component
public class ExceedFeeDAO {
	private final class ExceedFeeMapper implements RowMapper<ExceedFee> {
		@Override
		public ExceedFee mapRow(ResultSet rs, int rowNum) throws SQLException {
			ExceedFee ExceedFee = new ExceedFee();
			ExceedFee.setId(rs.getInt("exceedid"));
			ExceedFee.setExceedfee(rs.getDouble("exceedfee"));
			return ExceedFee;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ExceedFee getExceedFee() {
		ExceedFee exceedFee = jdbcTemplate.queryForObject("SELECT * FROM express_set_exceed_fee ", new ExceedFeeMapper());
		return exceedFee;
	}

	public void saveExceedFee(final ExceedFee exceedFee) {
		jdbcTemplate.update("update express_set_exceed_fee set exceedfee=? where exceedid=?", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setDouble(1, exceedFee.getExceedfee());
				ps.setLong(2, exceedFee.getId());
			}
		});
	}

}
