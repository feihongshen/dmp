package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.CwbOXODetailBean;
import cn.explink.enumutil.CwbOXOStateEnum;
import cn.explink.enumutil.CwbOXOTypeEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Component
public class CwbOXODetailDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private int OXOValue = CwbOrderTypeIdEnum.OXO.getValue();
	private int OXOJITValue = CwbOrderTypeIdEnum.OXO_JIT.getValue();
	
	private int OXOTypePick = CwbOXOTypeEnum.pick.getValue();
	private int OXOTypeDelivery = CwbOXOTypeEnum.delivery.getValue();
	
	
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
			cwbOXODetail.setPayWay(rs.getString("payway"));
			return cwbOXODetail;
		}
	}

	public List<CwbOXODetailBean> getCwbOXODetailByPage(long page, long currentBranchId,
			String startDate, String endDate, String cwbOXOTypeId,
			String cwbOXOStateId, String customerId, String cwbOrderTypeId) {
		String cwbOXOStateText = CwbOXOStateEnum.getTextByValue(Integer.valueOf(cwbOXOStateId));
		//针对OXO类型的订单先将一行记录拆分为两行，分别是揽收、配送；OXOJIT类型的订单对应一行，为揽收
		String sql =  "SElECT '"+cwbOXOStateText+"' as cwbstate, cwb.cwb,cus.customername,date_format(cwb.credate,'%Y-%m-%d %H:%i:%s') as credate,cwb.receivablefee,bra.branchname,pay.payway "
				     +" FROM express_set_payway pay, express_set_branch bra, express_set_customer_info cus, "
				     + "    ((  SELECT "+OXOTypePick+" as deliverytypeid,cwbordertypeid,cwb,customerid,credate,receivablefee,deliverybranchid,paywayid,pickbranchid,oxopickstate,oxodeliverystate FROM express_ops_cwb_detail WHERE state = 1 AND cwbordertypeid = "+OXOJITValue+") "
                            +"  UNION"
                            +"( SELECT a.deliverytypeid,b.* FROM (SELECT "+OXOValue+" as cwbordertypeid, "+OXOTypePick+" as deliverytypeid FROM DUAL UNION SELECT "+OXOValue+" as cwbordertypeid, "+OXOTypeDelivery+" as deliverytypeid FROM DUAL ) a "
                            +      "LEFT JOIN" 
                            +      "(SELECT cwbordertypeid,cwb,customerid,credate,receivablefee,deliverybranchid,paywayid,pickbranchid,oxopickstate,oxodeliverystate FROM express_ops_cwb_detail WHERE state = 1 AND cwbordertypeid = "+OXOValue+") b "
                            +      "ON a.cwbordertypeid = b.cwbordertypeid)) cwb "
		             + " WHERE cwb.customerid = cus.customerid "
		             + " AND  cwb.deliverybranchid = bra.branchid "
		             + " AND cwb.paywayid = pay.paywayid";
		
		if (startDate.length() > 0) {
			sql += " AND cwb.credate >= DATE('" + startDate + "')";
		}

		if (endDate.length() > 0) {
			sql += " AND cwb.credate <= DATE('" + endDate + "')";
		}

		if (cwbOXOTypeId.length() > 0 && !"0".equals(cwbOXOTypeId)) {// 20
			sql += " AND cwb.deliverytypeid = " + cwbOXOTypeId + "";
			if ((OXOTypePick + "").equals(cwbOXOTypeId)) {// 查询选中了揽件
				sql += " AND cwb.oxopickstate = " + cwbOXOStateId;
				sql += " AND cwb.pickbranchid = " + currentBranchId;
			} else if ((OXOTypeDelivery + "").equals(cwbOXOTypeId)) {// 查询选中了派件
				sql += " AND cwb.oxodeliverystate = " + cwbOXOStateId;
				sql += " AND cwb.deliverybranchid = " + currentBranchId;
			}
		} else {
			sql += " AND ((cwb.deliverytypeid = " + OXOTypePick
					+ " AND cwb.oxopickstate = " + cwbOXOStateId
					+ " AND cwb.pickbranchid = " + currentBranchId + ")"
					+ " OR (cwb.deliverytypeid = " + OXOTypeDelivery
					+ " AND cwb.oxodeliverystate = " + cwbOXOStateId
					+ " AND cwb.deliverybranchid = " + currentBranchId + " ))";
		}

		if (customerId.length() > 0 && !"0".equals(customerId)) {
			sql += " AND cus.customerid IN(" + customerId + ")";
		}

		if (cwbOrderTypeId.length() > 0 && !"0".equals(cwbOrderTypeId)) {
			sql += " AND cwb.cwbordertypeid = " + cwbOrderTypeId;
		} else {
			sql += " AND cwb.cwbordertypeid IN ("
					+ CwbOrderTypeIdEnum.OXO.getValue() + ","
					+ CwbOrderTypeIdEnum.OXO_JIT.getValue() + ")";
		}

		sql += " order by cwb asc limit " + ((page - 1) * Page.ONE_PAGE_NUMBER)
				+ " ," + Page.ONE_PAGE_NUMBER;
		return this.jdbcTemplate.query(sql, new CwbOXODetailRowMapper());
	}

	public long getCwbOXODetailCount(long currentBranchId, String startDate,
			String endDate, String cwbOXOTypeId, String cwbOXOStateId,
			String customerId, String cwbOrderTypeId) {
		//针对OXO类型的订单先将一行记录拆分为两行，分别是揽收、配送；OXOJIT类型的订单对应一行，为揽收
		String sql =  "SElECT count(1) "
				     +" FROM express_set_payway pay, express_set_branch bra, express_set_customer_info cus, "
				     + "    ((  SELECT "+OXOTypePick+" as deliverytypeid,cwbordertypeid,cwb,customerid,credate,receivablefee,deliverybranchid,paywayid,pickbranchid,oxopickstate,oxodeliverystate FROM express_ops_cwb_detail WHERE state = 1 AND cwbordertypeid = "+OXOJITValue+") "
                            +"  UNION"
                            +"( SELECT a.deliverytypeid,b.* FROM (SELECT "+OXOValue+" as cwbordertypeid, "+OXOTypePick+" as deliverytypeid FROM DUAL UNION SELECT "+OXOValue+" as cwbordertypeid, "+OXOTypeDelivery+" as deliverytypeid FROM DUAL ) a "
                            +      "LEFT JOIN" 
                            +      "(SELECT cwbordertypeid,cwb,customerid,credate,receivablefee,deliverybranchid,paywayid,pickbranchid,oxopickstate,oxodeliverystate FROM express_ops_cwb_detail WHERE state = 1 AND cwbordertypeid = "+OXOValue+") b "
                            +      "ON a.cwbordertypeid = b.cwbordertypeid)) cwb "
		             + " WHERE cwb.customerid = cus.customerid "
		             + " AND  cwb.deliverybranchid = bra.branchid "
		             + " AND cwb.paywayid = pay.paywayid";
		
		if (startDate.length() > 0) {
			sql += " AND cwb.credate >= DATE('" + startDate + "')";
		}

		if (endDate.length() > 0) {
			sql += " AND cwb.credate <= DATE('" + endDate + "')";
		}

		if (cwbOXOTypeId.length() > 0 && !"0".equals(cwbOXOTypeId)) {// 20
			sql += " AND cwb.deliverytypeid = " + cwbOXOTypeId + "";
			if ((OXOTypePick + "").equals(cwbOXOTypeId)) {// 查询选中了揽件
				sql += " AND cwb.oxopickstate = " + cwbOXOStateId;
				sql += " AND cwb.pickbranchid = " + currentBranchId;
			} else if ((OXOTypeDelivery + "").equals(cwbOXOTypeId)) {// 查询选中了派件
				sql += " AND cwb.oxodeliverystate = " + cwbOXOStateId;
				sql += " AND cwb.deliverybranchid = " + currentBranchId;
			}
		} else {
			sql += " AND ((cwb.deliverytypeid = " + OXOTypePick
					+ " AND cwb.oxopickstate = " + cwbOXOStateId
					+ " AND cwb.pickbranchid = " + currentBranchId + ")"
					+ " OR (cwb.deliverytypeid = " + OXOTypeDelivery
					+ " AND cwb.oxodeliverystate = " + cwbOXOStateId
					+ " AND cwb.deliverybranchid = " + currentBranchId + " ))";
		}

		if (customerId.length() > 0 && !"0".equals(customerId)) {
			sql += " AND cus.customerid IN(" + customerId + ")";
		}

		if (cwbOrderTypeId.length() > 0 && !"0".equals(cwbOrderTypeId)) {
			sql += " AND cwb.cwbordertypeid = " + cwbOrderTypeId;
		} else {
			sql += " AND cwb.cwbordertypeid IN ("
					+ CwbOrderTypeIdEnum.OXO.getValue() + ","
					+ CwbOrderTypeIdEnum.OXO_JIT.getValue() + ")";
		}
		return this.jdbcTemplate.queryForInt(sql);
	}
}
