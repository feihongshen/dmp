package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.KufangBranchMap;
import cn.explink.util.Page;

@Component
public class KufangBranchMapDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class KufangBranchMapRowMapper implements RowMapper<KufangBranchMap> {
		@Override
		public KufangBranchMap mapRow(ResultSet rs, int rowNum) throws SQLException {
			KufangBranchMap KufangBranchMap = new KufangBranchMap();
			KufangBranchMap.setFromBranchId(rs.getLong("fromBranchId"));
			KufangBranchMap.setToBranchId(rs.getLong("toBranchId"));
			return KufangBranchMap;
		}
	}

	public List<KufangBranchMap> getAllKufangBranchMap() {
		String sql = "select * from express_set_kufang_branch_map ";
		return this.jdbcTemplate.query(sql, new KufangBranchMapRowMapper());
	}

	public List<KufangBranchMap> getKufangBranchMapByWhere(long page, long fromBranchId, long toBranchId, long type) {
		String sql = "select * from express_set_kufang_branch_map ";
		sql = this.getKufangBranchMapByPageWhereSql(sql, fromBranchId, toBranchId);
		sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;

		List<KufangBranchMap> brList = this.jdbcTemplate.query(sql, new KufangBranchMapRowMapper());
		return brList;
	}

	public List<KufangBranchMap> getKufangBranchMapByWheresql(long fromBranchId, long toBranchId) {
		String sql = "select * from express_set_kufang_branch_map ";
		sql = this.getKufangBranchMapByPageWhereSql(sql, fromBranchId, toBranchId);

		List<KufangBranchMap> brList = this.jdbcTemplate.query(sql, new KufangBranchMapRowMapper());
		return brList;
	}

	public long getKufangBranchMapCount(long fromBranchId, long toBranchId) {
		String sql = "SELECT count(1) from express_set_kufang_branch_map";
		sql = this.getKufangBranchMapByPageWhereSql(sql, fromBranchId, toBranchId);
		return this.jdbcTemplate.queryForLong(sql);
	}

	private String getKufangBranchMapByPageWhereSql(String sql, long fromBranchId, long toBranchId) {
		if ((fromBranchId > 0) || (toBranchId > 0) ) {
			StringBuffer w = new StringBuffer();
			sql += " where ";
			if (fromBranchId > 0) {
				w.append(" and fromBranchId=" + fromBranchId);
			}
			if (toBranchId > 0) {
				w.append(" and toBranchId=" + toBranchId);
			}

			sql += w.substring(4, w.length());
		}
		return sql;
	}

	public void creKufangBranchMap(long fromBranchId, long toBranchId) {
		String sql = "insert into express_set_kufang_branch_map (fromBranchId,toBranchId) values(?,?)";
		this.jdbcTemplate.update(sql, fromBranchId, toBranchId);
	}

	public void deleteKufangBranchMap(long fromBranchId) {
		String sql = "DELETE FROM express_set_kufang_branch_map WHERE fromBranchId = ?";
		this.jdbcTemplate.update(sql, fromBranchId);
	}

	public List<KufangBranchMap> getKufangBranchMapByToBranch(long toBranchId) {
		String sql = "select * from express_set_kufang_branch_map where toBranchId=?";

		List<KufangBranchMap> brList = this.jdbcTemplate.query(sql, new Long[]{toBranchId},new KufangBranchMapRowMapper());
		return brList;
	}
}
