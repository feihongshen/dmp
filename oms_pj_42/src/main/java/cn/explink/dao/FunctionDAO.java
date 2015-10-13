package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.Function;
import cn.explink.util.StringUtil;

@Component
public class FunctionDAO {
	/**
	 * 机构功能编号
	 */
	public static final int BRANCH_FUNCTION_TYPE_NO = 0;

	private final class FunctionMapper implements RowMapper<Function> {
		@Override
		public Function mapRow(ResultSet rs, int rowNum) throws SQLException {
			Function function = new Function();
			function.setFunctionid(rs.getLong("functionid"));
			function.setFunctionname(StringUtil.nullConvertToEmptyString(rs.getString("functionname")));
			function.setMenuid(rs.getLong("menuid"));
			function.setType(rs.getInt("type"));
			return function;
		}
	}

	@Autowired
	JdbcTemplate jdbcTemplate;

	// 获取功能列表
	public List<Function> getFunctionListForType(long type) {
		StringBuffer sql = new StringBuffer("select * from express_set_function where type=" + type + " order by functionid");
		List<Function> list = jdbcTemplate.query(sql.toString(), new FunctionMapper());
		return list;
	}
}
