package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import cn.explink.domain.CreateSelectTerm;

@Component
public class CreateSelectTermDAO {

	private final class CreateSelectTermRowMapper implements RowMapper<CreateSelectTerm> {
		@Override
		public CreateSelectTerm mapRow(ResultSet rs, int rowNum) throws SQLException {
			CreateSelectTerm createSelectTerm = new CreateSelectTerm();
			createSelectTerm.setId(rs.getLong("id"));
			createSelectTerm.setTermname(rs.getString("termname"));
			createSelectTerm.setTermContent(rs.getString("termContent"));
			return createSelectTerm;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<CreateSelectTerm> getAllCreateSelectTerms() {
		return jdbcTemplate.query("select * from express_ops_createselect_term", new CreateSelectTermRowMapper());
	}

}
