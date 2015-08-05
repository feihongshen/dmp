package cn.explink.dao.express;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.express.ExpressPreOrder;
import cn.explink.util.Page;

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

	/**
	 * 根据执行状态和小件员查询
	 *
	 * @param excuteStateList
	 * @param delivermanId
	 * @return
	 */
	public List<ExpressPreOrder> getPreOrderByExcuteStateAndDelivermanId(long page, List<Integer> excuteStateList, Integer delivermanId) {
		StringBuffer sql = new StringBuffer("select id,pre_order_no,send_person,cellphone,telephone,arrange_time,collect_address from express_ops_preorder where 1=1");
		this.appendWhereSql(excuteStateList, delivermanId, sql);
		sql.append(" limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER);
		return this.jdbcTemplate.query(sql.toString(), new ExpressPreOrderRowMapper());
	}

	private void appendWhereSql(List<Integer> excuteStateList, Integer delivermanId, StringBuffer sql) {
		if ((excuteStateList != null) && !excuteStateList.isEmpty()) {
			sql.append(" and excute_state ");
			sql.append(this.assembleInByIntegerList(excuteStateList));
		}
		if ((delivermanId != null) && (delivermanId != Integer.valueOf(-1))) {
			sql.append(" and deliverman_id =" + delivermanId);
		}
	}

	/**
	 * 根据条件查询预订单数量
	 * 
	 * @param page
	 * @param excuteStateList
	 * @param delivermanId
	 * @return
	 */
	public long getPreOrderCountByExcuteStateAndDelivermanId(long page, List<Integer> excuteStateList, Integer delivermanId) {
		StringBuffer sql = new StringBuffer("select count(1) from express_ops_preorder where 1=1 ");
		this.appendWhereSql(excuteStateList, delivermanId, sql);
		return this.jdbcTemplate.queryForInt(sql.toString());
	}

	/**
	 * 通过id更新小件员等信息
	 *
	 * @param idList
	 * @param delivermanId
	 * @param delivermanName
	 * @param distributeUserId
	 * @param distributeUserName
	 * @return
	 */
	public boolean updateDeliverByIdList(List<Integer> idList, int delivermanId, String delivermanName, long distributeUserId, String distributeUserName) {
		if ((idList == null) || idList.isEmpty()) {
			return false;
		}
		StringBuffer sql = new StringBuffer(" update express_ops_preorder set");
		sql.append(" deliverman_id=" + delivermanId + ",");
		sql.append(" deliverman_name='" + delivermanName + "',");
		sql.append(" distribute_user_id=" + distributeUserId + ",");
		sql.append(" distribute_user_name='" + distributeUserName + "',");
		sql.append(" distribute_deliverman_time='" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss") + "'");
		sql.append(" where id  ");
		sql.append(this.assembleInByIntegerList(idList));

		int updateCount = this.jdbcTemplate.update(sql.toString());
		if (updateCount == idList.size()) {
			return true;
		}
		return false;
	}

	/**
	 * 更加执行状态和原因
	 *
	 * @param idList
	 * @param note
	 * @return
	 */
	public boolean updateExcuteStateByIdList(List<Integer> idList, int excuteState, String note) {
		if ((idList == null) || idList.isEmpty()) {
			return false;
		}
		StringBuffer sql = new StringBuffer(" update express_ops_preorder set");
		sql.append(" excute_state=" + excuteState + ",");
		sql.append(" reason='" + note + "'");
		sql.append(" where id  ");
		sql.append(this.assembleInByIntegerList(idList));

		int updateCount = this.jdbcTemplate.update(sql.toString());
		if (updateCount == idList.size()) {
			return true;
		}
		return false;
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
