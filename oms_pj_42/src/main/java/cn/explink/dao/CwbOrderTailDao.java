package cn.explink.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.CwbOrderTail;
import cn.explink.domain.FinanceAuditStatusEnum;
import cn.explink.domain.GoodsTypeEnum;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.jms.dto.DmpDeliveryState;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.Page;
import cn.explink.vo.delivery.DeliveryRateComputeType;
import cn.explink.vo.delivery.DeliveryRateQueryType;
import cn.explink.vo.delivery.DeliveryRateRequest;
import cn.explink.vo.delivery.DeliveryRateResult;
import cn.explink.vo.delivery.DeliveryRateTimeType;

@Component
public class CwbOrderTailDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class CwbOrderTailRowMapper implements RowMapper<CwbOrderTail> {

		@Override
		public CwbOrderTail mapRow(ResultSet rs, int rowNum) throws SQLException {
			// TODO Auto-generated method stub

			CwbOrderTail cwb_order_tail = new CwbOrderTail();
			cwb_order_tail.setCwb(rs.getString("cwb"));
			cwb_order_tail.setPaywayid(rs.getString("paywayid"));
			cwb_order_tail.setNewpaywayid(rs.getString("newpaywayid"));
			cwb_order_tail.setCustomerid(rs.getLong("customerid"));
			cwb_order_tail.setCwbordertypeid(rs.getInt("cwbordertypeid"));
			cwb_order_tail.setBranchid(rs.getString("branchid"));
			cwb_order_tail.setDeliverybranchid(rs.getString("deliverybranchid"));
			cwb_order_tail.setNextbranchid(rs.getString("nextbranchid"));
			cwb_order_tail.setCwbstate(rs.getLong("cwbstate"));
			cwb_order_tail.setGobackstate(rs.getString("gobackstate"));
			cwb_order_tail.setReceivablefee(rs.getBigDecimal("receivablefee"));
			cwb_order_tail.setPaybackfee(rs.getBigDecimal("paybackfee"));
			cwb_order_tail.setDeliverystate(rs.getInt("deliverystate"));
			cwb_order_tail.setFlowordertype(rs.getLong("flowordertype"));

			cwb_order_tail.setEmaildatetime(rs.getString("emaildatetime"));
			cwb_order_tail.setCouplebacktime(rs.getString("couplebacktime"));
			cwb_order_tail.setChecktime(rs.getString("checktime"));
			cwb_order_tail.setCustomerbacktime(rs.getString("customerbacktime"));
			cwb_order_tail.setTuihuointowarehoustime(rs.getString("tuihuointowarehoustime"));
			cwb_order_tail.setChangeouttowarehoustime(rs.getString("changeouttowarehoustime"));
			cwb_order_tail.setHousetohousetime(rs.getString("housetohousetime"));
			cwb_order_tail.setGetgoodstime(rs.getString("getgoodstime"));
			cwb_order_tail.setIntowarehoustime(rs.getString("intowarehoustime"));
			cwb_order_tail.setOutwarehousetime(rs.getString("outwarehousetime"));
			cwb_order_tail.setSubstationgoodstime(rs.getString("substationgoodstime"));
			cwb_order_tail.setReceivegoodstime(rs.getString("receivegoodstime"));
			cwb_order_tail.setReturngoodsoutwarehousetime(rs.getString("returngoodsoutwarehousetime"));
			cwb_order_tail.setChangeintowarehoustime(rs.getString("changeintowarehoustime"));

			cwb_order_tail.setZhandianouttozhongzhuantime(rs.getString("zhandianouttozhongzhuantime"));
			cwb_order_tail.setTuihuoouttozhandiantime(rs.getString("tuihuoouttozhandiantime"));
			cwb_order_tail.setGonghuoshangchenggongtime(rs.getString("gonghuoshangchenggongtime"));
			cwb_order_tail.setGonghuoshangjushoutime(rs.getString("gonghuoshangjushoutime"));
			cwb_order_tail.setStrGoodsType(getGoodsTypeString(rs));
			cwb_order_tail.setStrFinanceAuditStatus(getFinanceAuditStatusString(rs));

			return cwb_order_tail;
		}
	}

	private String getGoodsTypeString(ResultSet rs) throws SQLException {
		int nGoodsType = rs.getInt("goods_type");

		return GoodsTypeEnum.values()[nGoodsType].getName();
	}

	private String getFinanceAuditStatusString(ResultSet rs) throws SQLException {
		int submit = rs.getInt("payupid");
		if (submit == -2) {
			return "未提交";
		}
		StringBuilder strStatus = new StringBuilder();
		if (submit == -1) {
			strStatus.append("小件员交款");
		} else {
			strStatus.append("站点交款");
		}
		int auditStatus = rs.getInt("deliverpayupapproved");
		if (auditStatus == 1) {
			strStatus.append("审核");
		} else {
			strStatus.append("未审核");
		}
		return strStatus.toString();
	}

	private final class DeliveryRateRowMapper implements RowMapper<DeliveryRateResult> {

		@Override
		public DeliveryRateResult mapRow(ResultSet rs, int rowNum) throws SQLException {
			DeliveryRateResult result = new DeliveryRateResult();
			result.setBranchOrCustomer(rs.getInt(1));
			result.setDate(rs.getDate(2));
			result.setTotal(rs.getInt(3));
			int columnCount = rs.getMetaData().getColumnCount();
			if (columnCount == 5) {
				result.setCustomer(rs.getInt(5));
			}
			// int remainingColumnCount = columnCount - 3;
			List<Integer> deliveryCountList = new ArrayList<Integer>();
			result.setDeliveryCount(deliveryCountList);
			deliveryCountList.add(rs.getInt(4));
			// for (int i = 0; i < remainingColumnCount; i++) {
			// deliveryCountList.add(rs.getInt(i + 4));
			// }
			return result;
		}

	}

	private final class DeliveryRateRowMapper1 implements RowMapper<DeliveryRateResult> {

		@Override
		public DeliveryRateResult mapRow(ResultSet rs, int rowNum) throws SQLException {
			DeliveryRateResult result = new DeliveryRateResult();
			result.setBranchOrCustomer(rs.getInt(1));
			result.setDate(rs.getDate(2));
			result.setTotal(rs.getInt(3));
			int columnCount = rs.getMetaData().getColumnCount();
			int remainingColumnCount = columnCount - 3;
			List<Integer> deliveryCountList = new ArrayList<Integer>();
			result.setDeliveryCount(deliveryCountList);
			for (int i = 0; i < remainingColumnCount; i++) {
				deliveryCountList.add(rs.getInt(i + 4));
			}
			return result;
		}

	}

	private final class DeliveryRateRowMapper2 implements RowMapper<DeliveryRateResult> {

		@Override
		public DeliveryRateResult mapRow(ResultSet rs, int rowNum) throws SQLException {
			DeliveryRateResult result = new DeliveryRateResult();
			result.setBranchOrCustomer(rs.getInt(1));
			result.setDate(rs.getDate(2));
			result.setTotal(rs.getInt(3));
			result.setCustomer(rs.getInt(4));
			int columnCount = rs.getMetaData().getColumnCount();
			int remainingColumnCount = columnCount - 4;
			List<Integer> deliveryCountList = new ArrayList<Integer>();
			result.setDeliveryCount(deliveryCountList);
			for (int i = 0; i < remainingColumnCount; i++) {
				deliveryCountList.add(rs.getInt(i + 5));
			}
			return result;
		}

	}

	private final class TailSumMapper implements RowMapper<CwbOrderTail> {
		@Override
		public CwbOrderTail mapRow(ResultSet rs, int rowNum) throws SQLException {
			CwbOrderTail cwb_order_tail = new CwbOrderTail();
			cwb_order_tail.setId(rs.getLong("id"));
			cwb_order_tail.setReceivablefee(rs.getBigDecimal("receivablefee"));
			cwb_order_tail.setPaybackfee(rs.getBigDecimal("paybackfee"));

			return cwb_order_tail;
		}
	}

	/**
	 * 保存当前操作
	 * 
	 * @param orderFlow
	 * @param dmpCwbOrder
	 * @param deliverystate
	 * @param column_value_map
	 */
	public void saveOrUpdateOrderTail(final DmpOrderFlow orderFlow, final DmpCwbOrder del, final DmpDeliveryState deliverystate, String timeName) {

		int count = jdbcTemplate.queryForInt("SELECT COUNT(1) FROM commen_cwb_order_tail WHERE cwb='" + orderFlow.getCwb() + "'");

		String sql = "";
		if (count == 0) {
			// insert 操作
			if (del.getFlowordertype() == 1 || "emaildatetime".equals(timeName) || "".equals(timeName)) {
				sql = "INSERT INTO `commen_cwb_order_tail`(`cwb`,`paywayid`,`newpaywayid`,`customerid`,`cwbordertypeid`,"
						+ "`receivablefee`,`paybackfee`,`branchid`,`deliverybranchid`,`nextbranchid`,`deliverystate`,`flowordertype`," + "`gobackstate`,`emaildatetime`) "
						+ "VALUES (?,?,?,?,?,?,?,?,?,?" + ",?,?,?,?)";
				jdbcTemplate.update(sql, new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, del.getCwb());
						ps.setLong(2, del.getPaywayid());
						ps.setString(3, del.getNewpaywayid()== null ?"0":del.getNewpaywayid());
						ps.setLong(4, del.getCustomerid());
						ps.setString(5, del.getCwbordertypeid());
						ps.setBigDecimal(6, del.getReceivablefee());
						ps.setBigDecimal(7, del.getPaybackfee());
						ps.setLong(8, orderFlow.getBranchid());
						ps.setLong(9, del.getDeliverybranchid());
						ps.setLong(10, del.getNextbranchid());
						ps.setLong(11, deliverystate == null ? 0 : deliverystate.getDeliverystate());
						ps.setLong(12, del.getFlowordertype());
						ps.setLong(13, deliverystate == null ? 0 : deliverystate.getGcaid());
						ps.setString(14, del.getEmaildate());
					}
				});
			} else {
				sql = "INSERT INTO `commen_cwb_order_tail`(`cwb`,`paywayid`,`newpaywayid`,`customerid`,`cwbordertypeid`,"
						+ "`receivablefee`,`paybackfee`,`branchid`,`deliverybranchid`,`nextbranchid`,`deliverystate`,`flowordertype`," + "`gobackstate`,`emaildatetime`," + timeName + ") "
						+ "VALUES (?,?,?,?,?,?,?,?,?,?" + ",?,?,?,?,?)";
				jdbcTemplate.update(sql, new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, del.getCwb());
						ps.setLong(2, del.getPaywayid());
						ps.setString(3, del.getNewpaywayid());
						ps.setLong(4, del.getCustomerid());
						ps.setString(5, del.getCwbordertypeid());
						ps.setBigDecimal(6, del.getReceivablefee());
						ps.setBigDecimal(7, del.getPaybackfee());
						ps.setLong(8, orderFlow.getBranchid());
						ps.setLong(9, del.getDeliverybranchid());
						ps.setLong(10, del.getNextbranchid());
						ps.setLong(11, deliverystate == null ? 0 : deliverystate.getDeliverystate());
						ps.setLong(12, del.getFlowordertype());
						ps.setLong(13, deliverystate == null ? 0 : deliverystate.getGcaid());
						ps.setString(14, del.getEmaildate());
						ps.setString(15, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()));
					}
				});
			}

		} else {
			jdbcTemplate.update("UPDATE `commen_cwb_order_tail` SET " + "`newpaywayid`=?,`branchid`=?,`deliverybranchid`=?,`nextbranchid`=?,`deliverystate`=?,"
					+ "`cwbstate`=?,`gobackstate`=?,flowordertype=?"
					+ (!timeName.equals("") ? "," + timeName + "='" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()) + "'" : " ") + " " + " WHERE `cwb`=?",
					new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps) throws SQLException {
							// TODO Auto-generated method stub
							ps.setString(1, del.getNewpaywayid());
							ps.setLong(2, orderFlow.getBranchid());
							ps.setLong(3, del.getDeliverybranchid());
							ps.setLong(4, del.getNextbranchid());
							ps.setLong(5, deliverystate == null ? 0 : deliverystate.getDeliverystate());
							ps.setLong(6, del.getCwbstate());
							ps.setLong(7, deliverystate == null ? 0 : deliverystate.getGcaid());
							ps.setLong(8, del.getFlowordertype());
							ps.setString(9, del.getCwb());
						}
					});
		}

	}

	public List<CwbOrderTail> getTailList(CwbOrderTail tail, long page) {
		String sql = "SELECT * FROM commen_cwb_order_tail ";
		sql = setWhereSql(sql, tail);
		sql += "  limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new CwbOrderTailRowMapper());
	}

	public CwbOrderTail getTailSum(CwbOrderTail tail) {
		String sql = "SELECT count(1) as id, sum(receivablefee) as receivablefee,sum(paybackfee) as paybackfee FROM commen_cwb_order_tail  ";
		sql = setWhereSql(sql, tail);
		return jdbcTemplate.queryForObject(sql, new TailSumMapper());
	}

	public void delTailByCwbs(String cwbs) {
		String sql = "delete FROM commen_cwb_order_tail where cwb in(" + cwbs + ") ";
		jdbcTemplate.update(sql);
	}

	private String setWhereSql(String sql, CwbOrderTail tail) {

		if (tail.getBegintime() != null && tail.getCurquerytimecolumn() != null) {
			sql = sql + " where " + tail.getCurquerytimecolumn() + ">='" + tail.getBegintime() + "'";
		}
		if (tail.getEndtime() != null && tail.getCurquerytimecolumn() != null) {
			sql = sql + " and " + tail.getCurquerytimecolumn() + "<='" + tail.getEndtime() + "'";
		}
		/** 配送站 **/
		if (tail.getDispatchbranchids().length > 0) {
			String dispatchbranchids = getStrings(tail.getDispatchbranchids());
			sql = sql + " and deliverybranchid in(" + dispatchbranchids + ")";
		}
		/** 当前站 **/
		if (tail.getCurdispatchbranchids().length > 0) {
			String branchids = getStrings(tail.getCurdispatchbranchids());
			sql = sql + " and branchid in(" + branchids + ")";

		}
		/** 下一站 **/
		if (tail.getNextdispatchbranchids().length > 0) {
			String nextbranchids = getStrings(tail.getNextdispatchbranchids());
			sql = sql + " and nextbranchid in(" + nextbranchids + ")";
		}
		/** 供应商 **/
		if (tail.getCustomerids().length > 0) {
			String customerids = getStrings(tail.getCustomerids());
			sql = sql + " and customerid in(" + customerids + ")";
		}
		/** 订单类型 **/
		if (tail.getCwbordertypeids().length > 0) {
			String cwbordertypeids = getStrings(tail.getCwbordertypeids());
			sql = sql + " and cwbordertypeid in(" + cwbordertypeids + ")";
		}
		/** 订单类型 **/
		if (tail.getOperationOrderResultTypes().length > 0) {
			String deliverystates = getStrings(tail.getOperationOrderResultTypes());
			sql = sql + " and deliverystate in(" + deliverystates + ")";
		}

		/** 归班状态 **/
		if (!tail.getGobackstate().equals("-1")) {
			if (tail.getGobackstate().equals("0")) {
				sql = sql + " and flowordertype<>36";
			} else {
				sql = sql + " and flowordertype=36";
			}
		}

		/** 支付方式 **/
		if (!tail.getPaywayid().equals("-1")) {
			sql = sql + " and paywayid='" + tail.getPaywayid() + "'";
		}
		/** 当前支付方式 **/
		if (!tail.getNewpaywayid().equals("-1")) {
			sql = sql + " and newpaywayid='" + tail.getNewpaywayid() + "'";
		}
		/** 订单状态 **/
		if (tail.getFlowordertype() != -1) {
			sql = sql + " and flowordertype=" + tail.getFlowordertype() + "";
		}
		/** 财务审核状态 **/
		int financeAuditIndex = tail.getFinanceAuditStatus();
		if (financeAuditIndex != -1) {
			sql = this.appendFinanceAuditStatusSql(sql, financeAuditIndex);
		}
		/** 货物类型 **/
		if (tail.getGoodsType() != -1) {
			sql = sql + " and goods_type=" + tail.getGoodsType() + "";
		}
		return sql;
	}

	private String appendFinanceAuditStatusSql(String sql, int financeAuditIndex) {
		FinanceAuditStatusEnum status = FinanceAuditStatusEnum.values()[financeAuditIndex];
		// 已经审核.
		if (FinanceAuditStatusEnum.BranchAudited.equals(status) || FinanceAuditStatusEnum.DeliverAudited.equals(status)) {
			sql = sql + " and deliverpayupapproved = 1";
		} else {
			sql = sql + " and deliverpayupapproved = 0";
		}
		if (FinanceAuditStatusEnum.BranchAudited.equals(status) || FinanceAuditStatusEnum.BranchNotAudited.equals(status)) {
			sql = sql + " and payupid > -1";
		} else {
			sql = sql + " and payupid = -1";
		}
		return sql;
	}

	private String getStrings(String[] strArr) {
		String strs = "";
		if (strArr.length > 0) {
			for (String str : strArr) {
				strs += str + ",";
			}
		}
		if (strs.length() > 0) {
			strs = strs.substring(0, strs.length() - 1);
		}
		return strs;
	}

	/**
	 * 重置审核状态 修改订单表字段
	 * 
	 * @param nextbranchid
	 *            下一站 目前逻辑如此 改为对应货物所在站点的中转站id
	 * @param flowordertype
	 *            改为9 领货状态
	 * @param deliverystate
	 *            反馈状态 置为0 配送结果与反馈表一致 会根据反馈的状态而变更，而领货时是0
	 */
	public void updateForChongZhiShenHe(String cwb, Long nextbranchid, long flowordertype, long deliverystate) {
		jdbcTemplate.update("update commen_cwb_order_tail set couplebacktime='',checktime='',nextbranchid=?,flowordertype=?,deliverystate=? where cwb=?", nextbranchid, flowordertype, deliverystate,
				cwb);
	}

	public void updateXiuGaiJinE(String cwb, BigDecimal receivablefee, BigDecimal paybackfee) {
		jdbcTemplate.update("update commen_cwb_order_tail set receivablefee=?,paybackfee=? where cwb=?", receivablefee, paybackfee, cwb);
	}

	public void updateXiuGaiZhiFuFangShi(String cwb, int newpaywayid) {
		jdbcTemplate.update("update commen_cwb_order_tail set newpaywayid=? where cwb=?", newpaywayid, cwb);
	}

	public void updateXiuGaiDingDanLeiXing(String cwb, int cwbordertypeid, long deliverystate) {
		jdbcTemplate.update("update commen_cwb_order_tail set cwbordertypeid=?,deliverystate=? where cwb=?", cwbordertypeid, deliverystate, cwb);
	}

	public List<DeliveryRateResult> queryDeliveryRate(DeliveryRateRequest request) {
		StringBuilder sql = new StringBuilder();
		DeliveryRateQueryType queryType = request.getQueryType();
		String firstField = queryType.getFirstField();
		String secondField = queryType.getSecondField();
		String thirdField = queryType.getThirdField();
		// 第一个查询条件，站点或者供应商
		List<Long> firstCondition = null;
		List<Long> thirdCondition = null;
		if (("branchid").equals(firstField)) {
			firstCondition = request.getBranchIds();
			thirdCondition = request.getCustomerIds();
		}
		if ("customerid".equals(firstField)) {
			firstCondition = request.getCustomerIds();
			thirdCondition = request.getBranchIds();
		}
		sql.append("select ").append(firstField).append(", DATE_FORMAT(").append(secondField);
		sql.append(",'%Y-%m-%d'), COUNT(1) as total");

		if (thirdCondition != null) {
			sql.append(" ,customerid");
		}
		List<DeliveryRateTimeType> timeTypes = request.getTimeTypes();
		DeliveryRateComputeType computeType = request.getComputeType();
		for (DeliveryRateTimeType timeType : timeTypes) {
			// 订单发货状态为妥投, 参阅DeliveryStateEnum
			sql.append(", SUM(CASE WHEN deliverystate in (1, 2, 3)");

			// 设置时限
			if (!DeliveryRateTimeType.all.equals(timeType)) {
				sql.append(" AND TIMESTAMPDIFF(HOUR, ").append(secondField);
				sql.append(", ").append(computeType.getField()).append(")");
				sql.append(" <= ").append(timeType.getValue());
			}
			sql.append(" THEN 1 ELSE 0 END)");
		}
		sql.append(" FROM commen_cwb_order_tail");
		sql.append(" WHERE ").append(firstField).append(" in (");

		for (int i = 0; i < firstCondition.size(); i++) {
			if (i > 0) {
				sql.append(", ");
			}
			sql.append(firstCondition.get(i));
		}

		// 第二个查询条件，按站点到货, 按小件员领货,按库房入库, 按供应商发货
		sql.append(") and ").append(secondField).append(" >= ?");
		sql.append(" and ").append(secondField).append(" < ?");

		if (thirdCondition != null) {
			sql.append(" AND ").append(thirdField).append(" IN (");
			for (int i = 0; i < thirdCondition.size(); i++) {
				if (i > 0) {
					sql.append(", ");
				}
				sql.append(thirdCondition.get(i));
			}
			sql.append(" )");
		}

		// 分组
		sql.append(" group by 1, 2");
		if (thirdCondition != null) {
			sql.append(" ,4");
		}
		// 排序
		sql.append(" order by 1, 2");
		if (thirdCondition != null) {
			sql.append(" ,4");
		}
		if (thirdCondition != null) {
			return jdbcTemplate.query(sql.toString(), new DeliveryRateRowMapper2(), request.getStartDate(), request.getEndDate());
		} else {
			return jdbcTemplate.query(sql.toString(), new DeliveryRateRowMapper1(), request.getStartDate(), request.getEndDate());
		}

	}

	/**
	 * 自行指定时效的妥投率查询
	 * 
	 * @param deliveryRateRequest
	 * @param startDate
	 * @param thresholdDate
	 * @param isNextDay
	 * @return
	 */
	public List<DeliveryRateResult> queryDeliveryRate(DeliveryRateRequest request, Date startDate, Date endDate, Date thresholdDate) {
		StringBuilder sql = new StringBuilder();
		DeliveryRateQueryType queryType = request.getQueryType();
		String firstField = queryType.getFirstField();
		String secondField = queryType.getSecondField();
		String thirdField = queryType.getThirdField();
		List<Long> firstCondition = null;
		List<Long> thirdCondition = null;
		if (("branchid").equals(firstField)) {
			firstCondition = request.getBranchIds();
			thirdCondition = request.getCustomerIds();
		}
		if ("customerid".equals(firstField)) {
			firstCondition = request.getCustomerIds();
			thirdCondition = request.getBranchIds();
		}

		sql.append("select ").append(firstField);
		sql.append(", '").append(DateTimeUtil.formatDate(startDate, "yyyy-MM-dd"));
		sql.append("', COUNT(1) as total");

		DeliveryRateComputeType computeType = request.getComputeType();
		// 订单发货状态为妥投, 参阅DeliveryStateEnum
		sql.append(", SUM(CASE WHEN deliverystate in (1, 2, 3)");

		// 设置时限
		sql.append(" AND ").append(computeType.getField());
		sql.append(" < ? ");
		sql.append(" THEN 1 ELSE 0 END)");
		if (thirdCondition != null) {
			sql.append(" ,customerid");
		}
		sql.append(" FROM commen_cwb_order_tail");
		sql.append(" WHERE ").append(firstField).append(" in (");
		// 第一个查询条件，站点或者供应商

		for (int i = 0; i < firstCondition.size(); i++) {
			if (i > 0) {
				sql.append(", ");
			}
			sql.append(firstCondition.get(i));
		}

		// 第二个查询条件，按站点到货, 按小件员领货,按库房入库, 按供应商发货
		sql.append(") and ").append(secondField).append(" >= ?");
		sql.append(" and ").append(secondField).append(" < ?");

		if (thirdCondition != null) {
			sql.append(" AND ").append(thirdField).append(" IN (");
			for (int i = 0; i < thirdCondition.size(); i++) {
				if (i > 0) {
					sql.append(", ");
				}
				sql.append(thirdCondition.get(i));
			}
			sql.append(" )");
		}
		// 分组
		sql.append(" group by 1");
		if (thirdCondition != null) {
			sql.append(" ,5");
		}
		// 排序
		sql.append(" order by 1");
		if (thirdCondition != null) {
			sql.append(" ,5");
		}
		return jdbcTemplate.query(sql.toString(), new DeliveryRateRowMapper(), thresholdDate, startDate, endDate);
	}
}
