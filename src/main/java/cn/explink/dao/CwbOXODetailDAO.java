package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.CwbOXODetailBean;
import cn.explink.enumutil.CwbFlowOrderTypeEnum;
import cn.explink.enumutil.CwbOXOStateEnum;
import cn.explink.enumutil.CwbOXOTypeEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
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
	
	
	private String fenZhanDaoHuo = CwbFlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "";
	private String fenZhanLingHuo = CwbFlowOrderTypeEnum.FenZhanLingHuo.getValue() + "";
	private String yiShenHe = CwbFlowOrderTypeEnum.YiShenHe.getValue() + "";
	
	private String OXOStateUnProcessedValue = CwbOXOStateEnum.UnProcessed.getValue() +""; 
	private String OXOStateProcessingValue = CwbOXOStateEnum.Processing.getValue() +""; 
	private String OXOStateProcessedValue = CwbOXOStateEnum.Processed.getValue() +""; 

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
			cwbOXODetail.setOxoType(rs.getString("oxotype"));
			return cwbOXODetail;
		}
	}

	public List<CwbOXODetailBean> getCwbOXODetailByPage(long page,
			long currentBranchId, String startDate, String endDate,
			String cwbOXOTypeId, String cwbOXOStateId, String customerId,
			String cwbOrderTypeId) {

		// 揽件指令查询SQL
		String pickSql = "select "
				+ OXOTypePick
				+ " as oxotypeid,cwbordertypeid,cwb,customerid,credate,oxopickstate as cwbstate,receivablefee,pickbranchid as branchid,paywayid  from express_ops_cwb_detail where state = 1 and pickbranchid = "
				+ currentBranchId + " and cwbordertypeid in (" + OXOValue + ","
				+ OXOJITValue + ")";
		if (startDate.length() > 0) {
			pickSql += " and credate >= DATE('" + startDate + "')";
		}
		if (endDate.length() > 0) {
			pickSql += " and credate <= DATE('" + endDate + "')";
		}
		if (customerId.length() > 0 && !"0".equals(customerId)) {
			pickSql += " and customerid in (" + customerId + ")";
		}
		if (cwbOXOStateId.length() > 0) {
			pickSql += " and oxopickstate =" + cwbOXOStateId;
		}
		if (cwbOrderTypeId.length() > 0 && !"0".equals(cwbOrderTypeId)) {
			pickSql += " and cwbordertypeid =" + cwbOrderTypeId;
		}

		//配送指令查询SQL
		String deliverySql = "select "
				+ OXOTypeDelivery
				+ " as oxotypeid, cwbordertypeid,cwb,customerid,credate,oxodeliverystate as cwbstate,receivablefee,deliverybranchid as branchid,paywayid  from express_ops_cwb_detail where state = 1 "
				+ " and deliverybranchid = " + currentBranchId 
				+ " and cwbordertypeid =" + OXOValue 
				+ " and currentbranchid = " + currentBranchId
				+ " and oxopickstate = " + OXOStateProcessedValue;
		    
		if (startDate.length() > 0) {
			deliverySql += " and credate >= DATE('" + startDate + "')";
		}
		if (endDate.length() > 0) {
			deliverySql += " and credate <= DATE('" + endDate + "')";
		}
		if (customerId.length() > 0 && !"0".equals(customerId)) {
			deliverySql += " and customerid in (" + customerId + ")";
		}
		if (cwbOXOStateId.length() > 0) {
			deliverySql += " and oxodeliverystate =" + cwbOXOStateId;
		}
		if (cwbOrderTypeId.length() > 0 && !"0".equals(cwbOrderTypeId)) {
			deliverySql += " and cwbordertypeid =" + cwbOrderTypeId;
		}
		
		if (OXOStateUnProcessedValue.equals(cwbOXOStateId)) {
			deliverySql += " and flowordertype = " + fenZhanDaoHuo;
		} else if (OXOStateProcessingValue.equals(cwbOXOStateId)) {
			deliverySql += " and flowordertype = " + fenZhanLingHuo;
		} else if (OXOStateProcessedValue.equals(cwbOXOStateId)) {
			deliverySql += " and flowordertype = " + yiShenHe;
		}
		
		String sql = "select case when oxotypeid = 1 then '揽收' when oxotypeid = 2 then '配送' end as oxotype,case when cwbstate = 0 then '未处理' when cwbstate= 1 then '处理中' when cwbstate=2 then '已处理' end as cwbstate ,cwb.cwb,cus.customername,date_format(cwb.credate,'%Y-%m-%d %H:%i:%s') as credate,cwb.receivablefee,bra.branchname,pay.payway  FROM express_set_payway pay, express_set_branch bra,express_set_customer_info cus,(";
		if ("".equals(cwbOXOTypeId) || "0".equals(cwbOXOTypeId)) { // 揽收、配送
			sql += pickSql + " union " + deliverySql;
		} else {
			if (OXOTypePick.equals(cwbOXOTypeId)) { // 揽收
				sql += pickSql;
			} else if (OXOTypeDelivery.equals(cwbOXOTypeId)) {// 配送
				sql += deliverySql;
			}
		}
		sql += ")cwb where cwb.customerid = cus.customerid and  cwb.branchid = bra.branchid  and cwb.paywayid = pay.paywayid order by cwb.cwb asc limit "
				+ ((page - 1) * Page.ONE_PAGE_NUMBER)
				+ " ,"
				+ Page.ONE_PAGE_NUMBER;

		return this.jdbcTemplate.query(sql, new CwbOXODetailRowMapper());
	}

	public long getCwbOXODetailCount(long currentBranchId, String startDate,
			String endDate, String cwbOXOTypeId, String cwbOXOStateId,
			String customerId, String cwbOrderTypeId) {

		String pickSql = "select "
				+ OXOTypePick
				+ " as oxotypeid,cwbordertypeid,cwb,customerid,credate,oxopickstate as cwbstate,receivablefee,pickbranchid as branchid,paywayid  from express_ops_cwb_detail where state = 1 and pickbranchid = "
				+ currentBranchId + " and cwbordertypeid in (" + OXOValue + ","
				+ OXOJITValue + ")";
		if (startDate.length() > 0) {
			pickSql += " and credate >= DATE('" + startDate + "')";
		}
		if (endDate.length() > 0) {
			pickSql += " and credate <= DATE('" + endDate + "')";
		}
		if (customerId.length() > 0 && !"0".equals(customerId)) {
			pickSql += " and customerid in (" + customerId + ")";
		}
		if (cwbOXOStateId.length() > 0) {
			pickSql += " and oxopickstate =" + cwbOXOStateId;
		}
		if (cwbOrderTypeId.length() > 0 && !"0".equals(cwbOrderTypeId)) {
			pickSql += " and cwbordertypeid =" + cwbOrderTypeId;
		}

		String deliverySql = "select "
				+ OXOTypeDelivery
				+ " as oxotypeid, cwbordertypeid,cwb,customerid,credate,oxodeliverystate as cwbstate,receivablefee,deliverybranchid as branchid,paywayid  from express_ops_cwb_detail where state = 1 "
				+ " and deliverybranchid = " + currentBranchId 
				+ " and cwbordertypeid =" + OXOValue 
				+ " and currentbranchid = " + currentBranchId
				+ " and oxopickstate = " + OXOStateProcessedValue;
		    

		if (startDate.length() > 0) {
			deliverySql += " and credate >= DATE('" + startDate + "')";
		}
		if (endDate.length() > 0) {
			deliverySql += " and credate <= DATE('" + endDate + "')";
		}
		if (customerId.length() > 0 && !"0".equals(customerId)) {
			deliverySql += " and customerid in (" + customerId + ")";
		}
		if (cwbOXOStateId.length() > 0) {
			deliverySql += " and oxodeliverystate =" + cwbOXOStateId;
		}
		if (cwbOrderTypeId.length() > 0 && !"0".equals(cwbOrderTypeId)) {
			deliverySql += " and cwbordertypeid =" + cwbOrderTypeId;
		}
		
		if (OXOStateUnProcessedValue.equals(cwbOXOStateId)) {
			deliverySql += " and flowordertype = " + fenZhanDaoHuo;
		} else if (OXOStateProcessingValue.equals(cwbOXOStateId)) {
			deliverySql += " and flowordertype = " + fenZhanLingHuo;
		} else if (OXOStateProcessedValue.equals(cwbOXOStateId)) {
			deliverySql += " and flowordertype = " + yiShenHe;
		}
		
		String sql = "select count(1)  FROM express_set_payway pay, express_set_branch bra,express_set_customer_info cus,(";
		if ("".equals(cwbOXOTypeId) || "0".equals(cwbOXOTypeId)) { // 揽收、配送
			sql += pickSql + " union " + deliverySql;
		} else {
			if (OXOTypePick.equals(cwbOXOTypeId)) { // 揽收
				sql += pickSql;
			} else if (OXOTypeDelivery.equals(cwbOXOTypeId)) {// 配送
				sql += deliverySql;
			}
		}
		sql += ")cwb where cwb.customerid = cus.customerid and  cwb.branchid = bra.branchid  and cwb.paywayid = pay.paywayid";
		return this.jdbcTemplate.queryForInt(sql);
	}
}
