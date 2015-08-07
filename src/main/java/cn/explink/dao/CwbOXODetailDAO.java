package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.CwbOXODetailBean;
import cn.explink.enumutil.CwbOXOTypeEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Component
public class CwbOXODetailDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private String OXOValue = CwbOrderTypeIdEnum.OXO.getValue() + "";
	private String OXOJITValue = CwbOrderTypeIdEnum.OXO_JIT.getValue() + "";

	private String OXOTypePick = CwbOXOTypeEnum.pick.getValue() + "";
	private String OXOTypeDelivery = CwbOXOTypeEnum.delivery.getValue() + "";

	private final class CwbOXODetailRowMapper implements
			RowMapper<CwbOXODetailBean> {
		@Override
		public CwbOXODetailBean mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			CwbOXODetailBean cwbOXODetail = new CwbOXODetailBean();
			cwbOXODetail.setCwb(StringUtil.nullConvertToEmptyString(rs
					.getString("cwb")));
			cwbOXODetail.setBranchName(StringUtil.nullConvertToEmptyString(rs
					.getString("branchname")));
			cwbOXODetail.setCreDate(rs.getString("credate"));
			cwbOXODetail.setCwbState(rs.getString("cwbstate"));
			cwbOXODetail.setReceivablefee(rs.getDouble("receivablefee"));
			cwbOXODetail.setCustomerName(rs.getString("customername"));
			cwbOXODetail.setOxoType(rs.getString("oxotype"));
			
			int payWayId = rs.getInt("paywayid");
			String payWay = "";
			Map<Integer, String> paywayMap = PaytypeEnum.getMap();
			for (Integer key : paywayMap.keySet()) {
				if (key == payWayId) {
					payWay = paywayMap.get(key);
				}
			}
			cwbOXODetail.setPayWay(payWay);
			return cwbOXODetail;
		}
	}

	public List<CwbOXODetailBean> getCwbOXODetailByPage(long page,
			long currentBranchId, String startDate, String endDate,
			String cwbOXOTypeId, String cwbOXOStateId, String customerId,
			String cwbOrderTypeId) {

		// 揽件指令查询SQL
		StringBuilder pickSql = new StringBuilder();
		pickSql.append("select "
				+ OXOTypePick
				+ " as oxotypeid,cwbordertypeid,cwb,customerid,credate,oxopickstate as cwbstate,receivablefee,pickbranchid as branchid,paywayid  from express_ops_cwb_detail where state = 1 and pickbranchid = "
				+ currentBranchId + " and cwbordertypeid in (" + OXOValue + ","
				+ OXOJITValue + ")");
		if (startDate.length() > 0) {
			pickSql.append(" and credate >= str_to_date('" + startDate
					+ "','%Y-%m-%d %H:%i:%s')");
		}
		if (endDate.length() > 0) {
			pickSql.append(" and credate <= str_to_date('" + endDate
					+ "','%Y-%m-%d %H:%i:%s')");
		}
		if (customerId.length() > 0 && !"0".equals(customerId)) {
			pickSql.append(" and customerid in (" + customerId + ")");
		}
		if (cwbOXOStateId.length() > 0) {
			pickSql.append(" and oxopickstate =" + cwbOXOStateId);
		}
		if (cwbOrderTypeId.length() > 0 && !"0".equals(cwbOrderTypeId)) {
			pickSql.append(" and cwbordertypeid =" + cwbOrderTypeId);
		}

		// 配送指令查询SQL
		StringBuilder deliverySql = new StringBuilder();

		deliverySql
				.append("select "
						+ OXOTypeDelivery
						+ " as oxotypeid, cwbordertypeid,cwb,customerid,credate,oxodeliverystate as cwbstate,receivablefee,deliverybranchid as branchid,paywayid  from express_ops_cwb_detail where state = 1 "
						+ " and deliverybranchid = " + currentBranchId
						+ " and cwbordertypeid =" + OXOValue);

		if (startDate.length() > 0) {
			deliverySql.append(" and credate >= str_to_date('" + startDate
					+ "','%Y-%m-%d %H:%i:%s')");
		}
		if (endDate.length() > 0) {
			deliverySql.append(" and credate <= str_to_date('" + endDate
					+ "','%Y-%m-%d %H:%i:%s')");
		}
		if (customerId.length() > 0 && !"0".equals(customerId)) {
			deliverySql.append(" and customerid in (" + customerId + ")");
		}
		if (cwbOXOStateId.length() > 0) {
			deliverySql.append(" and oxodeliverystate =" + cwbOXOStateId);
		}
		if (cwbOrderTypeId.length() > 0 && !"0".equals(cwbOrderTypeId)) {
			deliverySql.append(" and cwbordertypeid =" + cwbOrderTypeId);
		}

		StringBuilder sql = new StringBuilder();
		sql.append("select case when oxotypeid = 1 then '揽收' when oxotypeid = 2 then '配送' end as oxotype,case when cwbstate = 0 then '未处理' when cwbstate= 1 then '处理中' when cwbstate=2 then '已处理' end as cwbstate ,cwb.cwb,cus.customername,date_format(cwb.credate,'%Y-%m-%d %H:%i:%s') as credate,cwb.receivablefee,bra.branchname,cwb.paywayid  FROM express_set_payway pay, express_set_branch bra,express_set_customer_info cus,(");
		if ("".equals(cwbOXOTypeId) || "0".equals(cwbOXOTypeId)) { // 揽收、配送
			sql.append(pickSql + " union " + deliverySql);
		} else {
			if (OXOTypePick.equals(cwbOXOTypeId)) { // 揽收
				sql.append(pickSql);
			} else if (OXOTypeDelivery.equals(cwbOXOTypeId)) {// 配送
				sql.append(deliverySql);
			}
		}
		sql.append(")cwb where cwb.customerid = cus.customerid and  cwb.branchid = bra.branchid  and cwb.paywayid = pay.paywayid order by cwb.credate desc limit "
				+ ((page - 1) * Page.ONE_PAGE_NUMBER)
				+ " ,"
				+ Page.ONE_PAGE_NUMBER);

		return this.jdbcTemplate.query(sql.toString(),
				new CwbOXODetailRowMapper());
	}

	public long getCwbOXODetailCount(long currentBranchId, String startDate,
			String endDate, String cwbOXOTypeId, String cwbOXOStateId,
			String customerId, String cwbOrderTypeId) {
		// 揽件指令查询SQL
		StringBuilder pickSql = new StringBuilder();
		pickSql.append("select "
				+ OXOTypePick
				+ " as oxotypeid,cwbordertypeid,cwb,customerid,credate,oxopickstate as cwbstate,receivablefee,pickbranchid as branchid,paywayid  from express_ops_cwb_detail where state = 1 and pickbranchid = "
				+ currentBranchId + " and cwbordertypeid in (" + OXOValue + ","
				+ OXOJITValue + ")");
		if (startDate.length() > 0) {
			pickSql.append(" and credate >= str_to_date('" + startDate
					+ "','%Y-%m-%d %H:%i:%s')");
		}
		if (endDate.length() > 0) {
			pickSql.append(" and credate <= str_to_date('" + endDate
					+ "','%Y-%m-%d %H:%i:%s')");
		}
		if (customerId.length() > 0 && !"0".equals(customerId)) {
			pickSql.append(" and customerid in (" + customerId + ")");
		}
		if (cwbOXOStateId.length() > 0) {
			pickSql.append(" and oxopickstate =" + cwbOXOStateId);
		}
		if (cwbOrderTypeId.length() > 0 && !"0".equals(cwbOrderTypeId)) {
			pickSql.append(" and cwbordertypeid =" + cwbOrderTypeId);
		}

		// 配送指令查询SQL
		StringBuilder deliverySql = new StringBuilder();

		deliverySql
				.append("select "
						+ OXOTypeDelivery
						+ " as oxotypeid, cwbordertypeid,cwb,customerid,credate,oxodeliverystate as cwbstate,receivablefee,deliverybranchid as branchid,paywayid  from express_ops_cwb_detail where state = 1 "
						+ " and deliverybranchid = " + currentBranchId
						+ " and cwbordertypeid =" + OXOValue);

		if (startDate.length() > 0) {
			deliverySql.append(" and credate >= str_to_date('" + startDate
					+ "','%Y-%m-%d %H:%i:%s')");
		}
		if (endDate.length() > 0) {
			deliverySql.append(" and credate <= str_to_date('" + endDate
					+ "','%Y-%m-%d %H:%i:%s')");
		}
		if (customerId.length() > 0 && !"0".equals(customerId)) {
			deliverySql.append(" and customerid in (" + customerId + ")");
		}
		if (cwbOXOStateId.length() > 0) {
			deliverySql.append(" and oxodeliverystate =" + cwbOXOStateId);
		}
		if (cwbOrderTypeId.length() > 0 && !"0".equals(cwbOrderTypeId)) {
			deliverySql.append(" and cwbordertypeid =" + cwbOrderTypeId);
		}

		StringBuilder sql = new StringBuilder();
		sql.append("select count(1)  FROM express_set_payway pay, express_set_branch bra,express_set_customer_info cus,(");
		if ("".equals(cwbOXOTypeId) || "0".equals(cwbOXOTypeId)) { // 揽收、配送
			sql.append(pickSql + " union " + deliverySql);
		} else {
			if (OXOTypePick.equals(cwbOXOTypeId)) { // 揽收
				sql.append(pickSql);
			} else if (OXOTypeDelivery.equals(cwbOXOTypeId)) {// 配送
				sql.append(deliverySql);
			}
		}
		sql.append(")cwb where cwb.customerid = cus.customerid and  cwb.branchid = bra.branchid  and cwb.paywayid = pay.paywayid");
		return this.jdbcTemplate.queryForInt(sql.toString());
	}
}
