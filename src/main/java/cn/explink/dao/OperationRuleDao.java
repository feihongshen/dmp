package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.OperationRule;
import cn.explink.enumutil.OpertaionResultEnum;
import cn.explink.util.Page;

@Component
public class OperationRuleDao {

	public class OperationRuleMapper implements RowMapper<OperationRule> {

		@Override
		public OperationRule mapRow(ResultSet rs, int rowNum) throws SQLException {
			OperationRule operationRule = new OperationRule();
			operationRule.setId(rs.getLong("id"));
			operationRule.setName(rs.getString("name"));
			operationRule.setExpression(rs.getString("expression"));
			operationRule.setResult(OpertaionResultEnum.getEnum(rs.getLong("result")));
			operationRule.setFlowordertype(rs.getInt("flowordertype"));
			operationRule.setOrder(rs.getInt("order"));
			operationRule.setErrormessage(rs.getString("errormessage"));
			operationRule.setCreator(rs.getLong("creator"));
			operationRule.setCreateDate(rs.getTimestamp("createDate"));
			operationRule.setModifier(rs.getLong("modifier"));
			operationRule.setModifyDate(rs.getTimestamp("modifyDate"));
			return operationRule;
		}

	}

	@Autowired
	JdbcTemplate jdbcTemplate;

	public List<OperationRule> getAllOperationRule() {
		return jdbcTemplate.query("select * from express_set_operation_rule", new OperationRuleMapper());
	}

	public void creOperationRule(String name, String expression, int result, int flowordertype, int order, String errormessage, long creator, long modifier, String modifyDate) {
		String sql = "insert into express_set_operation_rule(`name`,`expression`,`result`,`flowordertype`,`order`,`errormessage`,`creator`,`modifier`,`modifyDate`) values(?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.update(sql, name, expression, result, flowordertype, order, errormessage, creator, modifier, modifyDate);
	}

	public List<OperationRule> getOperationRuleByPage(long page, int flowordertype) {
		String sql = "select * from express_set_operation_rule ";

		if (flowordertype > 0) {
			StringBuffer sb = new StringBuffer();
			sql += " where ";
			if (flowordertype > 0) {
				sb.append(" and flowordertype=" + flowordertype);
			}
			sql += sb.substring(4, sb.length());
		}
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new OperationRuleMapper());
	}

	public long getOperationRuleCount(long page, int flowordertype) {
		String sql = "select count(1) from express_set_operation_rule ";
		if (flowordertype > 0) {
			StringBuffer sb = new StringBuffer();
			sql += " where ";
			if (flowordertype > 0) {
				sb.append(" and flowordertype=" + flowordertype);
			}
			sql += sb.substring(4, sb.length());
		}
		return jdbcTemplate.queryForLong(sql);
	}

	public OperationRule getOperationRuleById(long id) {
		String sql = "select * from express_set_operation_rule where id=?";
		return jdbcTemplate.queryForObject(sql, new OperationRuleMapper(), id);
	}

	public void delOperationRule(long id) {
		String sql = "delete from express_set_operation_rule where id=?";
		jdbcTemplate.update(sql, id);
	}

	public void saveOperationRule(long id, String name, String expression, int result, int flowordertype, int order, String errormessage, long modifier) {
		String sql = "update express_set_operation_rule set `name`=?,`expression`=?,`result`=?,`flowordertype`=?,`order`=?,`errormessage`=?,`modifier`=? where `id`=? ";
		jdbcTemplate.update(sql, name, expression, result, flowordertype, order, errormessage, modifier, id);

	}
}
