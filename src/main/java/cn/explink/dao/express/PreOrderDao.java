package cn.explink.dao.express;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.express.ExpressPreOrder;

/**
 * 预订单处理Dao
 *
 * @author wangzy
 */

@Component
public class PreOrderDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class ExpressPreOrderRowMapper implements RowMapper<ExpressPreOrder> {
		@Override
		public ExpressPreOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
			ExpressPreOrder expressPreOrder = new ExpressPreOrder();
			expressPreOrder.setId(rs.getInt("id"));
			expressPreOrder.setPreOrderNo(rs.getString("pre_order_no"));
			// TODO
			return expressPreOrder;
		}

	}

	/**
	 * 根据预订单号查询记录
	 *
	 * @param preOrderNo
	 * @return
	 */
	public ExpressPreOrder getActivePreOrderByOrderNo(String preOrderNo) {
		try {
			// 后期用枚举
			String sql = "select * from express_ops_preorder where pre_order_no=? and excute_state =2 and status=0 order by id desc limit 0,1 ";
			return this.jdbcTemplate.queryForObject(sql, new ExpressPreOrderRowMapper(), preOrderNo);
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}
	}

	public List<ExpressPreOrder> getPreOrderByExcuteStateAndDelivermanId(List<Integer> excuteStateList, Integer delivermanId) {
		StringBuffer sql = new StringBuffer("select id,pre_order_no,send_person,cellphone,telephone,arrange_time,collect_address from express_ops_preorder where 1=1");
		if ((excuteStateList != null) && !excuteStateList.isEmpty()) {
			sql.append(" and excute_state ");
			sql.append(this.assembleInByIntegerList(excuteStateList));
		}
		if ((delivermanId != null) && (delivermanId != Integer.valueOf(-1))) {
			sql.append(" and deliverman_id =" + delivermanId);
		}
		return this.jdbcTemplate.query(sql.toString(), new ExpressPreOrderRowMapper());
	}

	private String assembleInByIntegerList(List<Integer> integerList) {
		if ((integerList == null) || integerList.isEmpty()) {
			return "";
		}
		StringBuffer sql = new StringBuffer(" in (");
		StringBuffer inItemSql = new StringBuffer();
		for (Integer integer : integerList) {
			inItemSql.append(integer);
			inItemSql.append(",");
		}
		String inItemSqlStr = inItemSql.toString();
		sql.append(inItemSqlStr.substring(0, inItemSqlStr.length() - 1));// 去掉最后一个逗号
		sql.append(")");
		return sql.toString();
	}
}
