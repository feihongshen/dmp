/**
 *
 */
package cn.explink.dao.express;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import cn.explink.domain.express.ExpressWeigh;

/**
 * @author songkaojun 2015年10月23日
 */
@Repository
public class ExpressWeighDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public boolean isWeightExist(String cwb) {
		StringBuffer sql = new StringBuffer("select id from express_ops_weigh where cwb='" + cwb + "'");
		ExpressWeigh expressWeigh = null;
		try {
			expressWeigh = this.jdbcTemplate.queryForObject(sql.toString(), new ExpressWeighRowMapper());
		} catch (DataAccessException e) {
			return false;
		}
		if (expressWeigh != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 创建预订单记录
	 *
	 * @param detailAddresStr
	 * @param preOrderDto
	 */
	public long insert(final ExpressWeigh expressWeigh) {
		final StringBuffer sql = new StringBuffer();
		sql.append(" insert into express_ops_weigh ");
		sql.append(" (cwb,weight,branchid,branchname,handlerid,handlername,handletime)");
		sql.append(" values(?,?,?,?,?,?,?)");
		KeyHolder key = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				int i = 0;
				ps.setString(++i, expressWeigh.getCwb());
				ps.setDouble(++i, expressWeigh.getWeight());
				ps.setLong(++i, expressWeigh.getBranchid());
				ps.setString(++i, expressWeigh.getBranchname());
				ps.setLong(++i, expressWeigh.getHandlerid());
				ps.setString(++i, expressWeigh.getHandlername());
				ps.setTimestamp(++i, new Timestamp(expressWeigh.getHandletime().getTime()));// 创建时间
				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

	public void updateWeight(ExpressWeigh expressWeigh) {
		String sql = "update express_ops_weigh set weight=?,branchid=?,branchname=?,handlerid=?,handlername=?,handletime=? where cwb=?";
		this.jdbcTemplate.update(sql, expressWeigh.getWeight(), expressWeigh.getBranchid(), expressWeigh.getBranchname(), expressWeigh.getHandlerid(), expressWeigh.getHandlername(), expressWeigh
				.getHandletime(), expressWeigh.getCwb());
	}

	private final class ExpressWeighRowMapper implements RowMapper<ExpressWeigh> {
		@Override
		public ExpressWeigh mapRow(ResultSet rs, int rowNum) throws SQLException {
			ExpressWeigh expressWeigh = new ExpressWeigh();
			expressWeigh.setId(rs.getLong("id"));
			return expressWeigh;
		}

	}

	/**
	 *
	 * @description  ExpressWeigh全属性的rowmap
	 * @author  刘武强
	 * @data   2015年11月17日
	 */
	private final class ExpressWeighAllRowMapper implements RowMapper<ExpressWeigh> {
		@Override
		public ExpressWeigh mapRow(ResultSet rs, int rowNum) throws SQLException {
			ExpressWeigh expressWeigh = new ExpressWeigh();
			expressWeigh.setId(rs.getLong("id"));
			expressWeigh.setCwb(rs.getString("cwb"));
			expressWeigh.setWeight(rs.getDouble("weight"));
			expressWeigh.setBranchid(rs.getLong("branchid"));
			expressWeigh.setBranchname(rs.getString("branchname"));
			expressWeigh.setHandlerid(rs.getLong("handlerid"));
			expressWeigh.setHandlername(rs.getString("handlername"));
			expressWeigh.setHandletime(rs.getTimestamp("handletime"));
			return expressWeigh;
		}

	}

	/**
	 *
	 * @Title: getExpressWeighByCwb
	 * @description 根据站点id，运单号，查询最新的重量记录
	 * @author 刘武强
	 * @date  2015年11月17日上午9:19:06
	 * @param  @param cwb
	 * @param  @param branchid
	 * @param  @return
	 * @return  ExpressWeigh
	 * @throws
	 */

	public ExpressWeigh getExpressWeighByCwb(String cwb, Long branchid) {
		List<ExpressWeigh> info = new ArrayList<ExpressWeigh>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM	express_ops_weigh WHERE cwb =? AND branchid = ? ORDER BY 	handletime DESC limit 1");
		info = this.jdbcTemplate.query(sql.toString(), new ExpressWeighAllRowMapper(), cwb, branchid);
		return info.size() > 0 ? info.get(0) : null;
	}

    public ExpressWeigh getExpressWeighByCwb(String cwb) {
        List<ExpressWeigh> info = new ArrayList<ExpressWeigh>();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM express_ops_weigh WHERE cwb =? ORDER BY handletime DESC limit 1");
        info = this.jdbcTemplate.query(sql.toString(), new ExpressWeighAllRowMapper(), cwb);
        return info.size() > 0 ? info.get(0) : null;
    }
}
