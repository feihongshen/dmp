package cn.explink.dao.express;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.b2c.pjwl.ExpressPreOrderDTO;
import cn.explink.domain.Branch;
import cn.explink.domain.Reason;
import cn.explink.domain.User;
import cn.explink.domain.VO.express.ExpressFeedBackParamsVO;
import cn.explink.domain.VO.express.PreOrderQueryDateVO;
import cn.explink.domain.VO.express.UseridAndBranchnameVO;
import cn.explink.domain.express.ExpressPreOrder;
import cn.explink.domain.express.ExpressPreOrderVOForDeal;
import cn.explink.enumutil.express.ExcuteStateEnum;
import cn.explink.enumutil.express.ExpressPreOrderStatus;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;
import cn.explink.util.Tools;

@Component
public class PreOrderDao implements Serializable {
	/**
	 * @description TODO
	 * @author 刘武强
	 * @date 2015年8月4日上午10:38:33
	 */
	private static final long serialVersionUID = -3711933464597436655L;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 *
	 * @description 将预订单查询结果转化为实体对象的RowMapper(全部字段)由于和志宇的冲突，暂时注释
	 * @author 刘武强
	 * @data 2015年8月3日
	 */
	/*
	 * private final class ExpressPreOrderRowMapper implements
	 * RowMapper<ExpressPreOrder> {
	 * 
	 * @Override public ExpressPreOrder mapRow(ResultSet rs, int rowNum) throws
	 * SQLException { ExpressPreOrder expressPreOrder = new ExpressPreOrder();
	 * expressPreOrder.setId(rs.getInt("id"));
	 * expressPreOrder.setPreOrderNo(rs.getString("pre_order_no"));
	 * expressPreOrder.setStatus(rs.getInt("status"));
	 * expressPreOrder.setExcuteState(rs.getInt("excute_state"));
	 * expressPreOrder.setSendPerson(rs.getString("send_person"));
	 * expressPreOrder.setCellphone(rs.getString("cellphone"));
	 * expressPreOrder.setTelephone(rs.getString("telephone"));
	 * expressPreOrder.setCollectAddress(rs.getString("collect_address"));
	 * expressPreOrder.setReason(rs.getString("reason"));
	 * expressPreOrder.setBranchId(rs.getInt("branch_id"));
	 * expressPreOrder.setBranchName(rs.getString("branch_name"));
	 * expressPreOrder.setHandleTime(rs.getDate("handle_time"));
	 * expressPreOrder.setHandleUserId(rs.getInt("handle_user_id"));
	 * expressPreOrder.setHandleUserName(rs.getString("handle_user_name"));
	 * expressPreOrder
	 * .setDistributeDeliverman_time(rs.getDate("distribute_deliverman_time"));
	 * expressPreOrder.setCreateTime(rs.getDate("create_time"));
	 * expressPreOrder.setArrangeTime(rs.getDate("arrange_time"));
	 * expressPreOrder.setDelivermanId(rs.getInt("deliverman_id"));
	 * expressPreOrder.setDelivermanName(rs.getString("deliverman_name"));
	 * expressPreOrder.setDistributeUserId(rs.getInt("distribute_user_id"));
	 * expressPreOrder
	 * .setDistributeUserName(rs.getString("distribute_user_name"));
	 * expressPreOrder.setOrderNo(rs.getString("order_no"));
	 * expressPreOrder.setFeedbackFirstReasonId
	 * (rs.getInt("feedback_first_reason_id"));
	 * expressPreOrder.setFeedbackFirstReason
	 * (rs.getString("feedback_first_reason"));
	 * expressPreOrder.setFeedbackSecondReasonId
	 * (rs.getInt("feedback_second_reason_id"));
	 * expressPreOrder.setFeedbackSecondReason
	 * (rs.getString("feedback_second_reason"));
	 * expressPreOrder.setFeedbackRemark(rs.getString("feedback_remark"));
	 * expressPreOrder.setFeedbackUserId(rs.getInt("feedback_user_id"));
	 * expressPreOrder.setFeedbackUserName(rs.getString("feedback_user_name"));
	 * expressPreOrder.setFeedbackUserName(rs.getString("feedback_time"));
	 * return expressPreOrder; } }
	 */
	/**
	 *
	 * @description 预订单查询中用到的RowMapper
	 * @author 刘武强
	 * @data 2015年8月3日
	 */
	/*
	 * private final class PreOrderQueryRowMapper implements
	 * RowMapper<PreOrderQueryDateVO> {
	 * 
	 * @Override public PreOrderQueryDateVO mapRow(ResultSet rs, int rowNum)
	 * throws SQLException { PreOrderQueryDateVO preOrderQueryDateVO = new
	 * PreOrderQueryDateVO(); preOrderQueryDateVO.setId(rs.getInt("id"));
	 * preOrderQueryDateVO.setPreOrderNo(rs.getString("pre_order_no"));
	 * preOrderQueryDateVO.setExcuteState(rs.getInt("excute_state"));
	 * preOrderQueryDateVO.setSendPerson(rs.getString("send_person"));
	 * preOrderQueryDateVO.setCellphone(rs.getString("cellphone"));
	 * preOrderQueryDateVO.setTelephone(rs.getString("telephone"));
	 * preOrderQueryDateVO.setCollectAddress(rs.getString("collect_address"));
	 * preOrderQueryDateVO.setBranchId(rs.getInt("branch_id"));
	 * preOrderQueryDateVO.setBranchName(rs.getString("branch_name")); return
	 * preOrderQueryDateVO; } }
	 */

	public Map<String, Object> getPreOrderQueryInfo(String start, String end, String excuteType, String mobile, String preordercode, String sender, String station, long page, int pageNumber) {
		Map<String, Object> map = new HashMap<String, Object>();
		/*
		 * List<PreOrderQueryDateVO> list = new
		 * ArrayList<PreOrderQueryDateVO>();
		 */
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

		/*
		 * PreparedStatementSetter ps = new PreparedStatementSetter() {
		 * 
		 * @Override public void setValues(PreparedStatement ps) throws
		 * SQLException { for (int i = 0; i < params.size(); i++) {
		 * ps.setObject(i + 1, params.get(i)); } } };
		 */

		Object[] args = new Object[params.size()];
		for (int i = 0; i < params.size(); i++) {
			args[i] = params.get(i);
		}
		// 查询页面数据
		/*
		 * list = this.jdbcTemplate.query(sql.toString(), args, new
		 * PreOrderQueryRowMapper()); //查询数据总量--前面参数已经绑定，所以不需要再次绑定 count =
		 * this.jdbcTemplate.queryForInt(countsql.toString(), args);
		 * map.put("list", list);
		 */
		map.put("count", count);
		return map;
	}

	private final class ExpressPreOrderRowMapper implements RowMapper<ExpressPreOrder> {
		private User user = null;

		public ExpressPreOrderRowMapper() {
		}

		public ExpressPreOrderRowMapper(User user) {
			super();
			this.user = user;
		}

		@Override
		public ExpressPreOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
			ExpressPreOrder expressPreOrder = new ExpressPreOrder();
			expressPreOrder.setId(rs.getInt("id"));
			expressPreOrder.setPreOrderNo(StringUtil.nullConvertToEmptyString(rs.getString("pre_order_no")));
			expressPreOrder.setStatus(rs.getInt("status"));
			expressPreOrder.setExcuteState(rs.getInt("excute_state"));
			expressPreOrder.setSendPerson(StringUtil.nullConvertToEmptyString(rs.getString("send_person")));
			expressPreOrder.setCellphone(StringUtil.nullConvertToEmptyString(rs.getString("cellphone")));
			expressPreOrder.setTelephone(StringUtil.nullConvertToEmptyString(rs.getString("telephone")));
			expressPreOrder.setCollectAddress(StringUtil.nullConvertToEmptyString(rs.getString("collect_address")));
			expressPreOrder.setReason(StringUtil.nullConvertToEmptyString(rs.getString("reason")));
			expressPreOrder.setBranchId(rs.getInt("branch_id"));
			expressPreOrder.setBranchName(rs.getString("branch_name"));
			expressPreOrder.setHandleTime(rs.getDate("handle_time"));
			expressPreOrder.setHandleUserId(rs.getInt("handle_user_id"));
			expressPreOrder.setHandleUserName(rs.getString("handle_user_name"));
			expressPreOrder.setDistributeDeliverman_time(StringUtil.formateSqlDateTime(rs.getTimestamp("distribute_deliverman_time")));
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
			expressPreOrder.setFeedbackTime(rs.getDate("feedback_time"));
			// added by jiangyu 预计下次揽件时间
			expressPreOrder.setNextPickTime(rs.getDate("next_pick_time"));
			// 敏感信息的控制显示
			if (this.user != null) {
				PreOrderDao.this.setValueInfoByUser(rs, expressPreOrder, this.user);
			}

			return expressPreOrder;
		}

	}

	private final class ExpressPreOrderVORowMapper implements RowMapper<ExpressPreOrderVOForDeal> {
		@Override
		public ExpressPreOrderVOForDeal mapRow(ResultSet rs, int rowNum) throws SQLException {
			ExpressPreOrderVOForDeal expressPreOrder = new ExpressPreOrderVOForDeal();
			expressPreOrder.setId(rs.getInt("id"));
			expressPreOrder.setPreOrderNo(StringUtil.nullConvertToEmptyString(rs.getString("pre_order_no")));
			expressPreOrder.setStatus(rs.getInt("status"));
			expressPreOrder.setExcuteState(rs.getInt("excute_state"));
			expressPreOrder.setSendPerson(StringUtil.nullConvertToEmptyString(rs.getString("send_person")));
			expressPreOrder.setCellphone(StringUtil.nullConvertToEmptyString(rs.getString("cellphone")));
			expressPreOrder.setTelephone(StringUtil.nullConvertToEmptyString(rs.getString("telephone")));
			expressPreOrder.setCollectAddress(StringUtil.nullConvertToEmptyString(rs.getString("collect_address")));
			expressPreOrder.setReason(StringUtil.nullConvertToEmptyString(rs.getString("reason")));
			expressPreOrder.setBranchId(rs.getInt("branch_id"));
			expressPreOrder.setBranchName(rs.getString("branch_name"));
			expressPreOrder.setHandleTime(rs.getDate("handle_time"));
			expressPreOrder.setHandleUserId(rs.getInt("handle_user_id"));
			expressPreOrder.setHandleUserName(rs.getString("handle_user_name"));
			expressPreOrder.setDistributeDeliverman_time(rs.getDate("distribute_deliverman_time"));
			if (rs.getTimestamp("create_time") != null) {
				String createTime = rs.getTimestamp("create_time").toString();
				expressPreOrder.setCreateTime(StringUtil.nullConvertToEmptyString(createTime.substring(0, createTime.length() - 2)));
			} else {
				expressPreOrder.setCreateTime("");
			}
			expressPreOrder.setArrangeTime(rs.getDate("arrange_time"));
			expressPreOrder.setDelivermanId(rs.getInt("deliverman_id"));
			expressPreOrder.setDelivermanName(rs.getString("deliverman_name"));
			expressPreOrder.setDistributeUserId(rs.getInt("distribute_user_id"));
			expressPreOrder.setDistributeUserName(rs.getString("distribute_user_name"));
			expressPreOrder.setOrderNo(rs.getString("order_no"));
			expressPreOrder.setFeedbackFirstReasonId(rs.getInt("feedback_first_reason_id"));
			if (rs.getString("feedback_first_reason") == null) {
				expressPreOrder.setFeedbackFirstReason("");
			} else {
				expressPreOrder.setFeedbackFirstReason(rs.getString("feedback_first_reason"));
			}
			expressPreOrder.setFeedbackSecondReasonId(rs.getInt("feedback_second_reason_id"));
			if (rs.getString("feedback_second_reason") == null) {
				expressPreOrder.setFeedbackSecondReason("");
			} else {
				expressPreOrder.setFeedbackSecondReason(rs.getString("feedback_second_reason"));
			}
			expressPreOrder.setFeedbackRemark(rs.getString("feedback_remark"));
			expressPreOrder.setFeedbackUserId(rs.getInt("feedback_user_id"));
			if (rs.getString("feedback_user_name") == null) {
				expressPreOrder.setFeedbackUserName("");
			} else {
				expressPreOrder.setFeedbackUserName(rs.getString("feedback_user_name"));
			}

			// expressPreOrder.setFeedbackUserName(rs.getString("feedback_user_name"));
			expressPreOrder.setFeedbackTime(rs.getDate("feedback_time"));
			// added by jiangyu 预计下次揽件时间
			expressPreOrder.setNextPickTime(rs.getDate("next_pick_time"));

			return expressPreOrder;
		}

	}

	/**
	 *
	 * @description 预订单查询中用到的RowMapper
	 * @author 刘武强
	 * @data 2015年8月3日
	 */
	private final class PreOrderQueryRowMapper implements RowMapper<PreOrderQueryDateVO> {
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
			preOrderQueryDateVO.setFeedbackUserId(rs.getInt("feedback_user_id"));
			preOrderQueryDateVO.setDelivermanId(rs.getInt("deliverman_id"));
			preOrderQueryDateVO.setDelivermanName(rs.getString("deliverman_name"));
			return preOrderQueryDateVO;
		}
	}

	private final class UseridAndBranchnameRowMapper implements RowMapper<UseridAndBranchnameVO> {
		@Override
		public UseridAndBranchnameVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			UseridAndBranchnameVO useridAndBranchnameVO = new UseridAndBranchnameVO();
			useridAndBranchnameVO.setUserid(rs.getInt("userid"));
			useridAndBranchnameVO.setBranchname(rs.getString("branchname"));
			return useridAndBranchnameVO;
		}
	}

	private final class PreOrderAssignRowMapper implements RowMapper<ExpressPreOrder> {
		@Override
		public ExpressPreOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
			ExpressPreOrder expressPreOrder = new ExpressPreOrder();
			expressPreOrder.setId(rs.getInt("id"));
			expressPreOrder.setPreOrderNo(rs.getString("pre_order_no"));
			expressPreOrder.setSendPerson(rs.getString("send_person"));
			expressPreOrder.setCellphone(rs.getString("cellphone"));
			expressPreOrder.setTelephone(rs.getString("telephone"));
			expressPreOrder.setDelivermanName(StringUtil.nullConvertToEmptyString(rs.getString("deliverman_name")));
			expressPreOrder.setArrangeTime(rs.getDate("arrange_time"));
			expressPreOrder.setCollectAddress(rs.getString("collect_address"));
			expressPreOrder.setExcuteStateStr(ExcuteStateEnum.getTextByValue(rs.getInt("excute_state")));
			return expressPreOrder;
		}
	}

	public Map<String, Object> getPreOrderQueryInfo(int status, String start, String end, String excuteType, String mobile, String preordercode, String sender, String station, long page,
			int pageNumber) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<PreOrderQueryDateVO> list = new ArrayList<PreOrderQueryDateVO>();
		StringBuffer sql = new StringBuffer();
		StringBuffer countsql = new StringBuffer();
		StringBuffer where = new StringBuffer();
		final List<Object> params = new ArrayList<Object>();
		int count = 0;

		sql.append("select * from express_ops_preorder");
		countsql.append("select count(id) t from express_ops_preorder");
		where.append(" ").append("where 1=1");

		if ((start != null) && StringUtils.isNotBlank(start.toString())) {
			where.append(" ").append("and create_time>=?");
			params.add(start);
		}
		if ((end != null) && StringUtils.isNotBlank(end.toString())) {
			where.append(" ").append("and create_time<=?");
			params.add(end);
		}
		if (StringUtils.isNotBlank(excuteType)) {
			if ("8".equals(excuteType)) {
				where.append(" ").append("and status=1");
			} else if ("9".equals(excuteType)) {
				where.append(" ").append("and status=2");
			} else {
				where.append(" ").append("and excute_state=?");
				where.append(" ").append("and status=0");
				params.add(Integer.parseInt(excuteType));
			}
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
			params.add(Integer.parseInt(station));
		}
		countsql.append(where);
		if (page > 0) {
			where.append(" limit " + ((page - 1) * pageNumber) + " ," + pageNumber);
		}
		sql.append(where);

		/*
		 * PreparedStatementSetter ps = new PreparedStatementSetter() {
		 * 
		 * @Override public void setValues(PreparedStatement ps) throws
		 * SQLException { for (int i = 0; i < params.size(); i++) {
		 * ps.setObject(i + 1, params.get(i)); } } };
		 */

		Object[] args = new Object[params.size()];
		for (int i = 0; i < params.size(); i++) {
			args[i] = params.get(i);
		}
		// 查询页面数据
		list = this.jdbcTemplate.query(sql.toString(), args, new PreOrderQueryRowMapper());
		count = this.jdbcTemplate.queryForInt(countsql.toString(), args);
		map.put("list", list);
		map.put("count", count);
		return map;
	}

	/**
	 *
	 * @Title: getUserAndBranchInfo
	 * @description 查出将所用用户id和其所在的站点的名称
	 * @author 刘武强
	 * @date 2015年9月21日下午6:59:45
	 * @param @return
	 * @return List
	 * @throws
	 */
	public List<UseridAndBranchnameVO> getUseridAndBranchnameInfo() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT u.userid,branchname FROM express_set_user u,express_set_branch b WHERE u.userDeleteFlag = 1 AND b.branchid = u.branchid;");
		return this.jdbcTemplate.query(sql.toString(), new UseridAndBranchnameRowMapper());
	}

	/**
	 *
	 * @Title: getDeliveryManByOrderNo
	 * @description 通过运单号查找小件员
	 * @author 刘武强
	 * @date 2015年8月10日下午12:46:47
	 * @param @param orderNo
	 * @param @param excuteState
	 * @param @param status
	 * @param @return
	 * @return ExpressPreOrder
	 * @throws
	 */
	public ExpressPreOrder getDeliveryManByOrderNo(String orderNo, int excuteState, int status) {
		ExpressPreOrder expressPreOrder = new ExpressPreOrder();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from express_ops_preorder where order_no=? and excute_state=? and status=?");
		List<ExpressPreOrder> infolist = this.jdbcTemplate.query(sql.toString(), new ExpressPreOrderRowMapper(), orderNo, excuteState, status);
		if (infolist.size() > 0) {
			expressPreOrder = infolist.get(0);
		}
		return expressPreOrder;
	}

	/**
	 * 查询预订单
	 *
	 * @author 王志宇
	 * @return
	 */
	public List<ExpressPreOrderVOForDeal> query(String preOrderNo, Integer status, long page) {
		StringBuffer sql = new StringBuffer("select * from express_ops_preorder where 1=1 AND express_ops_preorder.status=" + ExpressPreOrderStatus.NORMAL.getValue() + " and excute_state in ("
				+ ExcuteStateEnum.NotAllocatedStation.getValue() + "," + ExcuteStateEnum.StationSuperzone.getValue() + ")");
		if (!Tools.isEmpty(preOrderNo)) {
			// 去掉空格、回车
			preOrderNo = preOrderNo.replace("\n", "");
			preOrderNo = preOrderNo.trim();
			sql.append(" and pre_order_no like '%" + preOrderNo + "%'");
		}
		if (!Tools.isEmpty(status)) {
			sql.append(" and excute_state = " + status);
		} else {
			sql.append(" and excute_state = " + ExcuteStateEnum.NotAllocatedStation.getValue());
		}
		sql.append(" limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER);
		return this.jdbcTemplate.query(sql.toString(), new ExpressPreOrderVORowMapper());
	}

	/**
	 * 查询预订单数量
	 *
	 * @author 王志宇
	 * @return
	 */
	public int queryCount(String preOrderNo, Integer status) {
		StringBuffer sql = new StringBuffer("select count(1) from express_ops_preorder where 1=1 AND express_ops_preorder.status=" + ExpressPreOrderStatus.NORMAL.getValue() + " and excute_state in ("
				+ ExcuteStateEnum.NotAllocatedStation.getValue() + "," + ExcuteStateEnum.StationSuperzone.getValue() + ")");
		if (!"".equals(preOrderNo) && (preOrderNo != null)) {
			// 去掉空格、回车
			preOrderNo = preOrderNo.replace("\n", "");
			preOrderNo = preOrderNo.trim();
			sql.append(" and pre_order_no = '" + preOrderNo + "'");
		}
		if (status != null) {
			sql.append(" and excute_state = " + status);
		} else {
			sql.append(" and excute_state = " + ExcuteStateEnum.NotAllocatedStation.getValue());
		}
		return this.jdbcTemplate.queryForInt(sql.toString());
	}

	/**
	 * 修改预订单，关闭预订单 记录操作人，操作时间
	 *
	 * @author 王志宇
	 * @return
	 */
	public int updatePreOrderClose(String id, Long userId, String userName, Date date) {
		String sql = "UPDATE express_ops_preorder SET STATUS='" + ExpressPreOrderStatus.CLOSEORDER.getValue() + "',handle_user_id='" + userId + "',handle_user_name='" + userName + "',handle_time='"
				+ date + "'  WHERE id in (" + id + ")";
		int count = this.jdbcTemplate.update(sql);
		return count;
	}

	/**
	 * 修改预订单，退回总部 清空反馈信息，记录操作人，操作时间
	 *
	 * @author 王志宇
	 * @return
	 */
	public int updatePreOrderReturn(String id, Long userId, String userName, Date date) {
		String sql = "UPDATE express_ops_preorder SET STATUS='" + ExpressPreOrderStatus.RETURNORDER.getValue() + "',handle_user_id='" + userId + "',handle_user_name='" + userName + "',handle_time='"
				+ date + "',"
				+ "feedback_first_reason_id=NULL,feedback_first_reason=NULL,feedback_second_reason_id=NULL,feedback_second_reason=NULL,feedback_remark=NULL,feedback_user_id=NULL,feedback_time=NULL "
				+ "WHERE id in (" + id + ")";
		int count = this.jdbcTemplate.update(sql);
		return count;
	}

	/**
	 * 修改预订单，手动分配站点 记录操作人，操作时间,清空反馈信息
	 *
	 * @author 王志宇
	 * @return
	 */
	public int updatePreOrderHand(String id, int siteId, String siteName, Long userId, String userName, Date date) {
		String sql = "UPDATE express_ops_preorder SET branch_id='"
				+ siteId
				+ "',excute_state='"
				+ ExcuteStateEnum.AllocatedStation.getValue()
				+ "',branch_name='"
				+ siteName
				+ "',handle_user_id='"
				+ userId
				+ "',handle_user_name='"
				+ userName
				+ "',handle_time='"
				+ date
				+ "',"
				+ "deliverman_id=NULL,deliverman_name=NULL,distribute_user_id=NULL,distribute_user_name=NULL,feedback_first_reason_id=NULL,feedback_first_reason=NULL,feedback_second_reason_id=NULL,feedback_second_reason=NULL,feedback_remark=NULL,feedback_user_id=NULL,feedback_time=NULL "
				+ " WHERE id in (" + id + ")";
		int count = this.jdbcTemplate.update(sql);
		return count;
	}

	/**
	 * 修改预订单，自动分配站点 记录操作人，操作时间,清空反馈信息
	 *
	 * @author 王志宇
	 * @return
	 */
	public int updatePreOrderAuto(String preOrderNo, int siteId, String siteName, Long userId, String userName, Date date) {
		String sql = "UPDATE express_ops_preorder SET branch_id='"
				+ siteId
				+ "',excute_state='"
				+ ExcuteStateEnum.AllocatedStation.getValue()
				+ "',branch_name='"
				+ siteName
				+ "',handle_user_id='"
				+ userId
				+ "',handle_user_name='"
				+ userName
				+ "',handle_time='"
				+ date
				+ "',"
				+ "deliverman_id=NULL,deliverman_name=NULL,distribute_user_id=NULL,distribute_user_name=NULL,feedback_first_reason_id=NULL,feedback_first_reason=NULL,feedback_second_reason_id=NULL,feedback_second_reason=NULL,feedback_remark=NULL,feedback_user_id=NULL,feedback_time=NULL "
				+ " WHERE pre_order_no = '" + preOrderNo + "'";
		int count = this.jdbcTemplate.update(sql);
		return count;
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
			String sql = "select * from express_ops_preorder where pre_order_no=? and excute_state =? and status=? order by id desc limit 0,1 ";
			return this.jdbcTemplate.queryForObject(sql, new ExpressPreOrderRowMapper(), preOrderNo, ExcuteStateEnum.AllocatedDeliveryman.getValue(), ExpressPreOrderStatus.NORMAL.getValue());
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
	public List<ExpressPreOrder> getPreOrderByConditions(long page, List<Integer> excuteStateList, Long delivermanId, boolean isAdmin, long branchid) {
		StringBuffer sql = new StringBuffer("select id,deliverman_name,excute_state,pre_order_no,send_person,cellphone,telephone,arrange_time,collect_address from express_ops_preorder where 1=1");
		this.appendWhereSql(excuteStateList, delivermanId, isAdmin, branchid, sql);
		sql.append(" limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER);
		return this.jdbcTemplate.query(sql.toString(), new PreOrderAssignRowMapper());
	}

	public List<ExpressPreOrder> getPreOrderByPreOrderId(List<Integer> preOrderIdList) {
		if ((preOrderIdList == null) || preOrderIdList.isEmpty()) {
			return new ArrayList<ExpressPreOrder>();
		}
		StringBuffer sql = new StringBuffer("SELECT * from express_ops_preorder ");
		sql.append("where id");
		sql.append(Tools.assembleInByList(preOrderIdList));

		return this.jdbcTemplate.query(sql.toString(), new PreOrderAssignRowMapper());
	}

	private void appendWhereSql(List<Integer> excuteStateList, Long delivermanId, boolean isAdmin, long branchid, StringBuffer sql) {
		if ((excuteStateList != null) && !excuteStateList.isEmpty()) {
			sql.append(" and excute_state ");
			sql.append(Tools.assembleInByList(excuteStateList));
		}
		if ((delivermanId != null) && (delivermanId != Long.valueOf(-1))) {
			sql.append(" and deliverman_id =" + delivermanId);
		}
		if (!isAdmin) {
			sql.append(" and branch_id =" + branchid);
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
	public long getPreOrderCountByConditions(long page, List<Integer> excuteStateList, Long delivermanId, boolean isAdmin, long branchid) {
		StringBuffer sql = new StringBuffer("select count(1) from express_ops_preorder where 1=1 ");
		this.appendWhereSql(excuteStateList, delivermanId, isAdmin, branchid, sql);
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
	public boolean updateDeliverByIdList(List<Integer> idList, int excuteState, int delivermanId, String delivermanName, long distributeUserId, String distributeUserName) {
		if ((idList == null) || idList.isEmpty()) {
			return false;
		}
		StringBuffer sql = new StringBuffer(" update express_ops_preorder set");
		sql.append(" excute_state=" + excuteState + ",");
		sql.append(" deliverman_id=" + delivermanId + ",");
		sql.append(" deliverman_name='" + delivermanName + "',");
		sql.append(" distribute_user_id=" + distributeUserId + ",");
		sql.append(" distribute_user_name='" + distributeUserName + "',");
		sql.append(" distribute_deliverman_time='" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss") + "'");
		sql.append(" where id  ");
		sql.append(Tools.assembleInByList(idList));

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
		sql.append(Tools.assembleInByList(idList));

		int updateCount = this.jdbcTemplate.update(sql.toString());
		if (updateCount == idList.size()) {
			return true;
		}
		return false;
	}

	// ==============================================揽件反馈
	// Begin===============================================

	/**
	 * 查询记录
	 *
	 * @param deliveryId
	 * @return
	 */
	public List<ExpressPreOrder> getExpressFeedBackStateByDeliver(long deliveryId, User user) {
		return this.jdbcTemplate.query("select * from express_ops_preorder where deliverman_id=?  and excute_state>=? and status=? order by excute_state asc", new ExpressPreOrderRowMapper(user),
				deliveryId, ExcuteStateEnum.AllocatedDeliveryman.getValue(), ExpressPreOrderStatus.NORMAL.getValue());
	}

	/**
	 * 通过预订单号查询预订单
	 *
	 * @param preOrderNos
	 * @param user
	 * @return
	 */
	public List<ExpressPreOrder> getPreOrderByPreOrderNos(String preOrderNos, User user) {
		return this.jdbcTemplate.query("SELECT * from express_ops_preorder where pre_order_no in(" + preOrderNos + ") and status=? ", new ExpressPreOrderRowMapper(user),
				ExpressPreOrderStatus.NORMAL.getValue());
	}

	/**
	 * 通过预订单号查询预订单记录
	 *
	 * @param orderNo
	 * @return
	 */
	public ExpressPreOrder getPreOrderByOrderNo(String orderNo) {
		try {
			return this.jdbcTemplate.queryForObject("SELECT * from express_ops_preorder where pre_order_no=? and status=? limit 0,1", new ExpressPreOrderRowMapper(), orderNo,
					ExpressPreOrderStatus.NORMAL.getValue());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	/**
	 * 通过分配的机构查询预订单记录[未反馈]
	 *
	 * @param branchId
	 * @return
	 */
	public List<ExpressPreOrder> getPreOrderByBranchId(long branchId) {
		return this.jdbcTemplate.query("select * from express_ops_preorder where branch_id=?  and excute_state>=? and status=? ", new ExpressPreOrderRowMapper(), branchId,
				ExcuteStateEnum.AllocatedDeliveryman.getValue(), ExpressPreOrderStatus.NORMAL.getValue());
	}

	/**
	 * 查询机构分组的数据
	 *
	 * @param branchid
	 * @param userid
	 * @return
	 */
	public List<JSONObject> getpreOrderGroupByUserId(long branchId, long userId) {// AND
																					// u.userid=?
		return this.jdbcTemplate.query("SELECT u.realname,ds.deliverman_id,COUNT(1) AS num FROM express_ops_preorder ds "
				+ "LEFT JOIN express_set_user u ON ds.deliverman_id=u.userid WHERE ds.branch_id=?  and ds.excute_state>=? AND  ds.status=? GROUP BY ds.deliverman_id",
				new ExpressFeedBackRecordNoZero(), branchId, ExcuteStateEnum.AllocatedDeliveryman.getValue(), ExpressPreOrderStatus.NORMAL.getValue());
	}

	private final class ExpressFeedBackRecordNoZero implements RowMapper<JSONObject> {
		@Override
		public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
			JSONObject reJson = new JSONObject();
			reJson.put("deliveryid", rs.getString("deliverman_id"));
			reJson.put("delivername", StringUtil.nullConvertToEmptyString(rs.getString("realname")));
			reJson.put("num", rs.getString("num"));
			return reJson;
		}
	}

	/**
	 * 更新预订单的执行状态
	 *
	 * @param user
	 *            用户
	 * @param params
	 *            参数
	 * @param firstReason
	 *            一级原因
	 * @param secondReason
	 *            二级原因
	 * @return
	 */
	public int updatePreOrderExecuteState(final User user, final ExpressFeedBackParamsVO params, final Reason firstReason, final Reason secondReason) {

		StringBuffer sql = new StringBuffer();
		sql.append(" update express_ops_preorder set ");
		sql.append(" excute_state=" + params.getPickResultId().intValue() + ",");
		if (!Tools.isEmpty(params.getTransNo())) {
			sql.append(" order_no='" + params.getTransNo() + "',");
		}

		sql.append(" feedback_first_reason_id=" + Long.valueOf(firstReason.getReasonid()).intValue() + ",");
		if (!Tools.isEmpty(firstReason.getReasoncontent())) {
			sql.append(" feedback_first_reason='" + firstReason.getReasoncontent() + "',");
		}
		sql.append(" feedback_second_reason_id=" + Long.valueOf(secondReason.getReasonid()).intValue() + ",");
		if (!Tools.isEmpty(secondReason.getReasoncontent())) {
			sql.append(" feedback_second_reason='" + secondReason.getReasoncontent() + "',");
		}
		if (!Tools.isEmpty(params.getFeedBackRemark())) {
			sql.append(" feedback_remark='" + params.getFeedBackRemark() + "',");
		}
		sql.append(" feedback_user_id=" + Long.valueOf(user.getUserid()).intValue() + ",");
		sql.append(" feedback_user_name='" + user.getRealname() + "',");
		if (!Tools.isEmpty(params.getNextPickExpressTime())) {
			sql.append(" next_pick_time='" + params.getNextPickExpressTime() + "',");
		}
		if (!Tools.isEmpty(params.getFeedBackTime())) {
			sql.append(" feedback_time='" + params.getFeedBackTime() + "',");
		}
		StringBuffer realSql = new StringBuffer(sql.subSequence(0, sql.length() - 1));
		realSql.append(" where pre_order_no='" + params.getPreOrderNoEdit() + "'");
		realSql.append(" and deliverman_id=" + params.getDeliveryEditId().intValue() + "");
		realSql.append(" and status= " + ExpressPreOrderStatus.NORMAL.getValue());
		return this.jdbcTemplate.update(realSql.toString());
	}

	private void setValueInfoByUser(ResultSet rs, ExpressPreOrder preOrder, User user) throws SQLException {
		if (user.getShownameflag() != 1) {
			preOrder.setSendPerson("******");
		} else {
			preOrder.setSendPerson(rs.getString("send_person"));
		}

		if (user.getShowmobileflag() != 1) {
			preOrder.setCellphone("******");
		} else {
			preOrder.setCellphone(rs.getString("cellphone"));
		}

		if (user.getShowphoneflag() != 1) {
			preOrder.setTelephone("******");
		} else {
			preOrder.setTelephone(rs.getString("telephone"));
		}
	}

	/**
	 * 查询该运单号是否存在记录
	 * 
	 * @param transNo
	 * @return
	 */
	public Integer checkTransNoIsRepeat(String transNo) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(1) from express_ops_preorder ");
		sql.append(" where order_no='" + transNo + "'");
		return this.jdbcTemplate.queryForInt(sql.toString());
	}

	// ==============================================揽件反馈
	// End===============================================
	/**
	 * 查询预订单是否已经进入系统,过滤掉关闭的预订单
	 * 
	 * @param reserveOrderNo
	 * @return
	 */
	public ExpressPreOrder getPreOrderRecordByReserveOrderNo(String reserveOrderNo) {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from express_ops_preorder where status<>? and pre_order_no=? limit 0,1 ");
		try {
			return this.jdbcTemplate.queryForObject(sql.toString(), new ExpressPreOrderRowMapper(), ExpressPreOrderStatus.CLOSEORDER.getValue(), reserveOrderNo);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	/**
	 * 创建预订单记录
	 * 
	 * @param detailAddresStr
	 * @param preOrderDto
	 */
	public long insertPreOrderRecord(final ExpressPreOrderDTO dto, final String detailAddresStr, final Branch branch) {
		final StringBuffer sql = new StringBuffer();
		sql.append(" insert into express_ops_preorder ");
		sql.append(" (pre_order_no,status,send_person,");
		sql.append(" cellphone,telephone,collect_address,create_time");
		sql.append(" ,excute_state,branch_id,branch_name)");
		sql.append(" values(?,?,?,?,?,?,?,?,?,?)");
		KeyHolder key = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				int i = 0;
				ps.setString(++i, dto.getReserveOrderNo());
				ps.setInt(++i, Integer.valueOf(0));// 预订单状态
				ps.setString(++i, dto.getCnorName());// 寄件人姓名
				ps.setString(++i, dto.getCnorMobile());// 寄件人手机
				ps.setString(++i, dto.getCnorTel());// 电话
				ps.setString(++i, detailAddresStr);// 取件地址
				ps.setTimestamp(++i, new Timestamp(System.currentTimeMillis()));// 创建时间

				Integer executeState = ExcuteStateEnum.NotAllocatedStation.getValue();
				Long brachId = 0L;// 站点id
				String branchName = "";// 站点名称
				if (null != branch) {// 已匹配到了站点的逻辑
					executeState = ExcuteStateEnum.AllocatedStation.getValue();
					brachId = branch.getBranchid();
					branchName = branch.getBranchname();
				}
				ps.setInt(++i, executeState);// 执行状态
				ps.setInt(++i, brachId.intValue());// 站点id
				ps.setString(++i, branchName);// 站点名称
				return ps;
			}
		}, key);
		return key.getKey().longValue();

	}

	/**
	 * 将预订单状态置为关闭状态
	 * 
	 * @param preOrderDto
	 */
	public Integer updatePreOrderState2Closed(ExpressPreOrderDTO preOrderDto) {
		StringBuffer sql = new StringBuffer();
		sql.append(" update express_ops_preorder set status=? where pre_order_no=?");
		return this.jdbcTemplate.update(sql.toString(), ExpressPreOrderStatus.CLOSEORDER.getValue(), preOrderDto.getReserveOrderNo());
	}

	/**
	 * 更新记录的全部字段
	 * 
	 * @param dto
	 * @param detailAddresStr
	 * @param branch
	 * @return
	 */
	public int updatePreOrderRecord(ExpressPreOrderDTO dto, String detailAddresStr, Branch branch) {
		StringBuffer sql = new StringBuffer();
		Integer executeState = ExcuteStateEnum.NotAllocatedStation.getValue();
		Long brachId = 0L;// 站点id
		String branchName = "";// 站点名称
		if (null != branch) {// 已匹配到了站点的逻辑
			executeState = ExcuteStateEnum.AllocatedStation.getValue();
			brachId = branch.getBranchid();
			branchName = branch.getBranchname();
		}

		sql.append("update express_ops_preorder set ");
		sql.append(" status=?,send_person=?,");
		sql.append(" cellphone=?,telephone=?,collect_address=?,create_time=?,");
		sql.append(" excute_state=?,branch_id=?,branch_name=?");
		sql.append("where pre_order_no=?");
		return this.jdbcTemplate.update(sql.toString(), Integer.valueOf(0), dto.getCnorName(), dto.getCnorMobile(), dto.getCnorTel(), detailAddresStr, new Timestamp(System.currentTimeMillis()),
				executeState, brachId, branchName, dto.getReserveOrderNo());
	}

	/**
	 * 删除记录
	 * 
	 * @param reserveOrderNo
	 */
	public int deleteRecordByPreOrderNo(String reserveOrderNo) {
		StringBuffer sql = new StringBuffer();
		sql.append("delete from express_ops_preorder where pre_order_no=?");
		return this.jdbcTemplate.update(sql.toString(), reserveOrderNo);
	}
}
