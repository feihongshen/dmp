package cn.explink.dao.express;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
public class PreOrderDao implements Serializable {
	/**
	 * @description TODO
	 * @author 刘武强
	 * @date  2015年8月4日上午10:38:33
	 */
	private static final long serialVersionUID = -3711933464597436655L;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 *
	 * @description 将预订单查询结果转化为实体对象的RowMapper(全部字段)由于和志宇的冲突，暂时注释
	 * @author  刘武强
	 * @data   2015年8月3日
	 */
	/*private final class ExpressPreOrderRowMapper implements RowMapper<ExpressPreOrder> {
		@Override
		public ExpressPreOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
			ExpressPreOrder expressPreOrder = new ExpressPreOrder();
			expressPreOrder.setId(rs.getInt("id"));
			expressPreOrder.setPreOrderNo(rs.getString("pre_order_no"));
			expressPreOrder.setStatus(rs.getInt("status"));
			expressPreOrder.setExcuteState(rs.getInt("excute_state"));
			expressPreOrder.setSendPerson(rs.getString("send_person"));
			expressPreOrder.setCellphone(rs.getString("cellphone"));
			expressPreOrder.setTelephone(rs.getString("telephone"));
			expressPreOrder.setCollectAddress(rs.getString("collect_address"));
			expressPreOrder.setReason(rs.getString("reason"));
			expressPreOrder.setBranchId(rs.getInt("branch_id"));
			expressPreOrder.setBranchName(rs.getString("branch_name"));
			expressPreOrder.setHandleTime(rs.getDate("handle_time"));
			expressPreOrder.setHandleUserId(rs.getInt("handle_user_id"));
			expressPreOrder.setHandleUserName(rs.getString("handle_user_name"));
			expressPreOrder.setDistributeDeliverman_time(rs.getDate("distribute_deliverman_time"));
			expressPreOrder.setCreateTime(rs.getDate("create_time"));
			expressPreOrder.setArrangeTime(rs.getDate("arrange_time"));
			expressPreOrder.setDelivermanId(rs.getInt("deliverman_id"));
			expressPreOrder.setDelivermanName(rs.getString("deliverman_name"));
			expressPreOrder.setDistributeUserId(rs.getInt("distribute_user_id"));
			expressPreOrder.setDistributeUserName(rs.getString("distribute_user_name"));
			expressPreOrder.setOrderNo(rs.getString("order_no"));
			expressPreOrder.setFeedbackFirstReasonId(rs.getInt("feedback_first_reason_id"));
			expressPreOrder.setFeedbackFirstReason(rs.getString("feedback_first_reason"));
			expressPreOrder.setFeedbackSecondReasonId(rs.getInt("feedback_second_reason_id"));
			expressPreOrder.setFeedbackSecondReason(rs.getString("feedback_second_reason"));
			expressPreOrder.setFeedbackRemark(rs.getString("feedback_remark"));
			expressPreOrder.setFeedbackUserId(rs.getInt("feedback_user_id"));
			expressPreOrder.setFeedbackUserName(rs.getString("feedback_user_name"));
			expressPreOrder.setFeedbackUserName(rs.getString("feedback_time"));
			return expressPreOrder;
		}
	}
	*/
	/**
	 *
	 * @description 预订单查询中用到的RowMapper
	 * @author  刘武强
	 * @data   2015年8月3日
	 */
	/*private final class PreOrderQueryRowMapper implements RowMapper<PreOrderQueryDateVO> {
		@Override
		public PreOrderQueryDateVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			PreOrderQueryDateVO preOrderQueryDateVO = new PreOrderQueryDateVO();
			preOrderQueryDateVO.setId(rs.getInt("id"));
			preOrderQueryDateVO.setPreOrderNo(rs.getString("pre_order_no"));
			preOrderQueryDateVO.setExcuteState(rs.getInt("excute_state"));
			preOrderQueryDateVO.setSendPerson(rs.getString("send_person"));
			preOrderQueryDateVO.setCellphone(rs.getString("cellphone"));
			preOrderQueryDateVO.setTelephone(rs.getString("telephone"));
			preOrderQueryDateVO.setCollectAddress(rs.getString("collect_address"));
			preOrderQueryDateVO.setBranchId(rs.getInt("branch_id"));
			preOrderQueryDateVO.setBranchName(rs.getString("branch_name"));
			return preOrderQueryDateVO;
		}
	}*/

	public Map<String, Object> getPreOrderQueryInfo(String start, String end, String excuteType, String mobile, String preordercode, String sender, String station, long page, int pageNumber) {
		Map<String, Object> map = new HashMap<String, Object>();
		/*List<PreOrderQueryDateVO> list = new ArrayList<PreOrderQueryDateVO>();*/
		StringBuffer sql = new StringBuffer();
		StringBuffer countsql = new StringBuffer();
		StringBuffer where = new StringBuffer();
		final List<Object> params = new ArrayList<Object>();
		int count = 0;

		sql.append("select * from express_ops_preorder");
		countsql.append("select count(*) t from express_ops_preorder");
		where.append(" ").append("where status=1");
		if ((start != null) && StringUtils.isNotBlank(start.toString())) {
			where.append(" ").append("and create_time>=?");
			params.add(start);
		}
		if ((end != null) && StringUtils.isNotBlank(end.toString())) {
			where.append(" ").append("and create_time<=?");
			params.add(end);
		}
		if (StringUtils.isNotBlank(excuteType)) {
			where.append(" ").append("and excute_state=?");
			params.add(excuteType);
		} else {
			where.append(" ").append("and excute_state=1");
		}
		if (StringUtils.isNotBlank(mobile)) {
			where.append(" ").append("and cellphone like ?");
			params.add("%" + mobile + "%");
		}
		if (StringUtils.isNotBlank(preordercode)) {
			where.append(" ").append("and pre_order_no like ?");
			params.add("%" + preordercode + "%");
		}
		if (StringUtils.isNotBlank(sender)) {
			where.append(" ").append("and send_person like ?");
			params.add("%" + sender + "%");
		}
		if (StringUtils.isNotBlank(station) && !"0".equals(station)) {
			where.append(" ").append("and branch_id=?");
			params.add(station);
		}
		countsql.append(where);
		if (page > 0) {
			where.append(" limit " + ((page - 1) * pageNumber) + " ," + pageNumber);
		}
		sql.append(where);

		/*PreparedStatementSetter ps = new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				for (int i = 0; i < params.size(); i++) {
					ps.setObject(i + 1, params.get(i));
				}
			}
		};*/

		Object[] args = new Object[params.size()];
		for (int i = 0; i < params.size(); i++) {
			args[i] = params.get(i);
		}
		//查询页面数据
		/*list = this.jdbcTemplate.query(sql.toString(), args, new PreOrderQueryRowMapper());
		//查询数据总量--前面参数已经绑定，所以不需要再次绑定
		count = this.jdbcTemplate.queryForInt(countsql.toString(), args);
		map.put("list", list);*/
		map.put("count", count);
		return map;
	}

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
