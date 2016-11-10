package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.BranchRoute;
import cn.explink.util.Page;

@Component
public class BranchRouteDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class BranchRouteRowMapper implements RowMapper<BranchRoute> {
		@Override
		public BranchRoute mapRow(ResultSet rs, int rowNum) throws SQLException {
			BranchRoute branchRoute = new BranchRoute();
			branchRoute.setFromBranchId(rs.getLong("fromBranchId"));
			branchRoute.setToBranchId(rs.getLong("toBranchId"));
			branchRoute.setType(rs.getInt("type"));
			return branchRoute;
		}
	}

	public List<BranchRoute> getAllBranchRoute() {
		String sql = "select * from express_set_branch_route ";
		return this.jdbcTemplate.query(sql, new BranchRouteRowMapper());
	}

	public List<BranchRoute> getBranchRouteByWhere(long page, long fromBranchId, long toBranchId, long type) {
		String sql = "select * from express_set_branch_route ";
		sql = this.getBranchRouteByPageWhereSql(sql, fromBranchId, toBranchId, type);
		sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;

		List<BranchRoute> brList = this.jdbcTemplate.query(sql, new BranchRouteRowMapper());
		return brList;
	}

	public List<BranchRoute> getBranchRouteByWheresql(long fromBranchId, long toBranchId, long type) {
		String sql = "select * from express_set_branch_route ";
		sql = this.getBranchRouteByPageWhereSql(sql, fromBranchId, toBranchId, type);

		List<BranchRoute> brList = this.jdbcTemplate.query(sql, new BranchRouteRowMapper());
		return brList;
	}

	public long getBranchRouteCount(long fromBranchId, long toBranchId, long type) {
		String sql = "SELECT count(1) from express_set_branch_route";
		sql = this.getBranchRouteByPageWhereSql(sql, fromBranchId, toBranchId, type);
		return this.jdbcTemplate.queryForLong(sql);
	}

	private String getBranchRouteByPageWhereSql(String sql, long fromBranchId, long toBranchId, long type) {
		if ((fromBranchId > 0) || (toBranchId > 0) || (type > 0)) {
			StringBuffer w = new StringBuffer();
			sql += " where ";
			if (fromBranchId > 0) {
				w.append(" and fromBranchId=" + fromBranchId);
			}
			if (toBranchId > 0) {
				w.append(" and toBranchId=" + toBranchId);
			}
			if (type > 0) {
				w.append(" and type=" + type);
			}
			sql += w.substring(4, w.length());
		}
		return sql;
	}

	public void creBranchRoute(long fromBranchId, long toBranchId, long type) {
		String sql = "insert into express_set_branch_route (fromBranchId,toBranchId,type) values(?,?,?)";
		this.jdbcTemplate.update(sql, fromBranchId, toBranchId, type);
	}

	public void saveBranchRouteByWhere(long oldfromBranchId, long oldtoBranchId, long oldtype, long fromBranchId, long toBranchId, long type) {
		String sql = "update express_set_branch_route set fromBranchId=?,toBranchId=?,type=? where fromBranchId=? and toBranchId=? and type=? ";
		this.jdbcTemplate.update(sql, fromBranchId, toBranchId, type, oldfromBranchId, oldtoBranchId, oldtype);
	}

	public void deleteBranchRouteByWhere(long fromBranchId, long toBranchId, long type) {
		String sql = "DELETE FROM express_set_branch_route WHERE fromBranchId = ? and toBranchId=? and type=? ";
		this.jdbcTemplate.update(sql, fromBranchId, toBranchId, type);
	}

	public void deleteBranchRouteBatch(long fromBranchId, long type) {
		String sql = "DELETE FROM express_set_branch_route WHERE fromBranchId = ? and type=? ";
		this.jdbcTemplate.update(sql, fromBranchId, type);
	}

	/**
	 *
	 * @param fromBranchId
	 * @param toBranchIds
	 * @param type
	 */
	public void setBranchRoutesql(long fromBranchId, List<Long> toBranchIds, int type) {

		for (Long l : toBranchIds) {
			String sql = "insert into express_set_branch_route(fromBranchId,toBranchId,type) values(?,?,?)";
			this.jdbcTemplate.update(sql, fromBranchId, l.longValue(), type);
		}
		// TODO Auto-generated method stub
		//查询（已经保存的不进行  处理）
		//未保存的 （凭借  保存语句）

	}

	/**
	 *
	 * @Title: getInterceptBranch
	 * @description 查询流向配置里数据
	 * @author 刘武强
	 * @date  2016年1月11日下午4:59:03
	 * @param  @param fromBranchId
	 * @param  @param type
	 * @param  @return
	 * @return  List<BranchRoute>
	 * @throws
	 */
	public List<BranchRoute> getNextBranch(Long fromBranchId, long type) {
		String sql = "SELECT * FROM express_set_branch_route WHERE fromBranchId = ? and type=?";
		return this.jdbcTemplate.query(sql, new BranchRouteRowMapper(), fromBranchId, type);
	}

	/**
	 *
	 * @Title: getNextBranchByType
	 * @description 通过路由类型（type）查询流向配置里数据
	 * @author 刘武强
	 * @date  2016年1月11日下午5:07:03
	 * @param  @param type
	 * @param  @return
	 * @return  List<BranchRoute>
	 * @throws
	 */
	public List<BranchRoute> getNextBranchByType(long type) {
		String sql = "SELECT * FROM express_set_branch_route WHERE type=?";
		return this.jdbcTemplate.query(sql, new BranchRouteRowMapper(), type);
	}
	
	/**
	 * 根据起始站ids 流向类型  获取N多货物流向。
	 * @param fromBranchIds
	 * @param type
	 * @return
	 */
	public List<BranchRoute> getNextBranchByfromBranchIdsAndType(String fromBranchIds,long type){
		String sql = "SELECT * FROM express_set_branch_route WHERE fromBranchId in ("+fromBranchIds+") and type=?";
		return this.jdbcTemplate.query(sql, new BranchRouteRowMapper(), type);
	}
}
