/**
 *
 */
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

import cn.explink.domain.PaiFeiRule;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

/**
 * @author Administrator
 *
 */
@Component
public class PaiFeiRuleDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	final class PaiFeiRuleRowMapper implements RowMapper<PaiFeiRule> {
		@Override
		public PaiFeiRule mapRow(ResultSet rs, int rowNum) throws SQLException {
			PaiFeiRule pf = new PaiFeiRule();
			pf.setId(rs.getLong("id"));
			pf.setName(StringUtil.nullConvertToEmptyString(rs.getString("name")));
			pf.setPfruleNO(StringUtil.nullConvertToEmptyString(rs.getString("pfruleNO")));
			pf.setRemark(StringUtil.nullConvertToEmptyString(rs.getString("remark")));
			pf.setType(rs.getInt("type"));
			pf.setState(rs.getInt("state"));
			pf.setJushouPFfee(rs.getBigDecimal("jushouPFfee"));
			pf.setCreuserid(rs.getLong("creuserid"));
			return pf;
		}
	}

	public int crePaiFeiRule(final PaiFeiRule pf) {
		String sql = "insert into `express_ops_paifeirule` ( `pfruleNO`, `name`, `type`, `state`, `jushouPFfee`, `remark`,`creuserid`) VALUES ( ?, ?, ?, ?, ?, ?,?);";
		return this.jdbcTemplate.update(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, pf.getPfruleNO());
				ps.setString(2, pf.getName());
				ps.setInt(3, pf.getType());
				ps.setInt(4, pf.getState());
				ps.setBigDecimal(5, pf.getJushouPFfee());
				ps.setString(6, pf.getRemark());
				ps.setLong(7, pf.getCreuserid());
			}
		});
	}

	/**
	 * @param name
	 * @param state
	 * @param type
	 * @param remark
	 * @param orderby
	 * @param orderbyType
	 * @return
	 */
	public List<PaiFeiRule> getPaiFeiRules(long page, String name, int state, int type, String remark, String orderby, String orderbyType) {
		String sql = "select * from express_ops_paifeirule where 1=1 ";
		try {
			sql += this.creConditions(name, state, type, remark, orderby, orderbyType);
			sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
			return this.jdbcTemplate.query(sql, new PaiFeiRuleRowMapper());
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @param name
	 * @param state
	 * @param type
	 * @param remark
	 * @param orderby
	 * @param orderbyType
	 * @return
	 */
	private String creConditions(String name, int state, int type, String remark, String orderby, String orderbyType) {

		String sql = "";

		if (StringUtil.nullConvertToEmptyString(name).length() > 0) {
			sql += " and name like '%" + name + "%'";
		}
		if (state > 0) {
			sql += " and state=" + state;
		}
		if (type > 0) {
			sql += " and type=" + type;
		}
		if (StringUtil.nullConvertToEmptyString(remark).length() > 0) {
			sql += " and remark like '%" + remark + "%'";
		}
		if ((StringUtil.nullConvertToEmptyString(orderby).length() > 0) && (StringUtil.nullConvertToEmptyString(orderbyType).length() > 0)) {
			sql += " order by " + orderby + " " + orderbyType;
		}
		return sql;
	}

	/**
	 * @param name
	 * @param state
	 * @param type
	 * @param remark
	 * @param orderby
	 * @param orderbyType
	 * @return
	 */
	public int getPaiFeiRulesCounts(String name, int state, int type, String remark, String orderby, String orderbyType) {
		String sql = "select count(1) from express_ops_paifeirule where 1=1 ";
		try {
			sql += this.creConditions(name, state, type, remark, orderby, orderbyType);
			return this.jdbcTemplate.queryForInt(sql);
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * @param pfruleNO
	 * @return
	 */
	public PaiFeiRule getPaiFeiRuleByNO(String pfruleNO) {
		String sql = "select * from express_ops_paifeirule where  pfruleNO='"+pfruleNO+"' limit 1;";
		try {
			return this.jdbcTemplate.queryForObject(sql, new PaiFeiRuleRowMapper());
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * @param pfruleNO
	 * @return
	 */
	public PaiFeiRule getPaiFeiRuleById(long pfruleid) {
		String sql = "select * from express_ops_paifeirule where  id='"+pfruleid+"' limit 1;";
		try {
			return this.jdbcTemplate.queryForObject(sql, new PaiFeiRuleRowMapper());
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @param no
	 * @return
	 */
	public int deletePaiFeiRuleByPfRuleNO(String pfruleNO) {
		String sql="delete from express_ops_paifeirule where  pfruleNO='"+pfruleNO+"'";
		return this.jdbcTemplate.update(sql);
	}

	/**
	 * @return
	 */
	public List<PaiFeiRule> getPaiFeiRuleByType(int typeid) {
		String sql = "select * from express_ops_paifeirule where  type=? ";
		try {
			return this.jdbcTemplate.query(sql, new PaiFeiRuleRowMapper(), typeid);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @param rule
	 * @return
	 */
	public int updatePaiFeiRule(final PaiFeiRule pf) {
		String sql = "update `express_ops_paifeirule` set   `name`=?, `type`=?, `state`=?, `jushouPFfee`=?, `remark`=?,`creuserid`=? where id=?";
		return this.jdbcTemplate.update(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, pf.getName());
				ps.setInt(2, pf.getType());
				ps.setInt(3, pf.getState());
				ps.setBigDecimal(4, pf.getJushouPFfee());
				ps.setString(5, pf.getRemark());
				ps.setLong(6, pf.getCreuserid());
				ps.setLong(7, pf.getId());
			}
		});
	}
}
