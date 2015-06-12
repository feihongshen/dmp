/**
 *
 */
package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.PenalizeType;
import cn.explink.util.Page;

/**
 * @author Administrator
 *
 */
@Component
public class PenalizeTypeDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class PenalizeTypeRowMapper implements RowMapper<PenalizeType> {

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet,
		 * int)
		 */
		@Override
		public PenalizeType mapRow(ResultSet rs, int rowNum) throws SQLException {
			PenalizeType type = new PenalizeType();
			type.setId(rs.getInt("id"));
			type.setText(rs.getString("text"));
			type.setType(rs.getInt("type"));
			type.setParent(rs.getInt("parent"));
			type.setState(rs.getInt("state"));
			return type;
		}
	}

	public int crePenalizeType(PenalizeType type) throws Exception {
		String sql = "insert into express_set_penalizeType(text,type,parent)values(?,?,?) ";
		return this.jdbcTemplate.update(sql, type.getText(), type.getType(), type.getParent());
	}

	public List<PenalizeType> getPenalizeTypeByType(int type) {
		try {
			String sql = " select * from express_set_penalizeType where state=1 and type=?";
			return this.jdbcTemplate.query(sql, new PenalizeTypeRowMapper(), type);
		} catch (Exception e) {
			return null;
		}
	}

	public PenalizeType getPenalizeTypeById(int id) {
		try {
			String sql = "select * from express_set_penalizeType where id=? ";
			return this.jdbcTemplate.queryForObject(sql, new PenalizeTypeRowMapper(), id);
		} catch (Exception e) {
			return null;
		}
	}

	public PenalizeType getPenalizeTypeByText(String text) {
		String sql = "select * from express_set_penalizeType where text=? limit 1 ";
		try {
			return this.jdbcTemplate.queryForObject(sql, new PenalizeTypeRowMapper(), text);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @param page
	 * @return
	 */
	public List<PenalizeType> getPenalizeTypeByText(long page, String text) {
		try {
			String sql = "select * from express_set_penalizeType ";
			if ((text != null) && (text.length() > 0)) {
				sql += " where text like '%" + text + "%'";
			}
			sql += " order by state desc limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
			return this.jdbcTemplate.query(sql, new PenalizeTypeRowMapper());
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @return
	 */
	public long getPenalizeTypeCount(String text) {
		try {
			String sql = "select count(1) from express_set_penalizeType ";
			if ((text != null) && (text.length() > 0)) {
				sql += " where text like '%" + text + "%'";
			}
			return this.jdbcTemplate.queryForLong(sql);
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * @param penalizeType
	 */
	public int savePenalizeType(PenalizeType penalizeType) {
		try {
			String sql = " update express_set_penalizeType set type=?,parent=?,text=? where id=?";
			return this.jdbcTemplate.update(sql, penalizeType.getType(), penalizeType.getParent(), penalizeType.getText(), penalizeType.getId());
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * @param id
	 */
	public int delPenalizeType(int id, int state) {
		try {
			String sql = "update express_set_penalizeType set state=? where id=?";
			return this.jdbcTemplate.update(sql, state, id);
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * @param id
	 */
	public int delPenalizeTypeData(int id) {
		try {
			String sql = "delete from express_set_penalizeType  where id=?";
			return this.jdbcTemplate.update(sql, id);
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * @param id
	 * @return
	 */
	public List<PenalizeType> getPenalizeTypeByParent(int id) {
		String sql = "select * from express_set_penalizeType where parent=? and state=1 ";
		try {
			return this.jdbcTemplate.query(sql, new PenalizeTypeRowMapper(), id);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @param text
	 * @return
	 */
	public PenalizeType getPenalizeTypeByTextAndOne(String text, int id) {

		String sql = "select * from express_set_penalizeType where text=? and id<>? limit 1 ";
		try {
			return this.jdbcTemplate.queryForObject(sql, new PenalizeTypeRowMapper(), text, id);
		} catch (Exception e) {
			return null;
		}
	}
}
