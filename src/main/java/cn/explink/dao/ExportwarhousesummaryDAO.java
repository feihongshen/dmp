package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Component;

import cn.explink.domain.CwbOrder;
import cn.explink.domain.Exportwarhousesummary;
import cn.explink.domain.User;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Component
public class ExportwarhousesummaryDAO {
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	private final class CwbMapper implements RowMapper<CwbOrder> {
		@Override
		public CwbOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
			CwbOrder cwbOrder = new CwbOrder();
			cwbOrder.setOpscwbid(rs.getLong("opscwbid"));
			cwbOrder.setStartbranchid(rs.getLong("startbranchid"));
			cwbOrder.setNextbranchid(rs.getLong("nextbranchid"));
			cwbOrder.setBacktocustomer_awb(StringUtil.nullConvertToEmptyString(rs.getString("backtocustomer_awb")));
			cwbOrder.setCwbflowflag(StringUtil.nullConvertToEmptyString(rs.getString("cwbflowflag")));
			cwbOrder.setCarrealweight(rs.getBigDecimal("carrealweight"));
			cwbOrder.setCartype(StringUtil.nullConvertToEmptyString(rs.getString("cartype")));
			cwbOrder.setCarwarehouse(StringUtil.nullConvertToEmptyString(rs.getString("carwarehouse")));
			cwbOrder.setCarsize(StringUtil.nullConvertToEmptyString(rs.getString("carsize")));
			cwbOrder.setBackcaramount(rs.getBigDecimal("backcaramount"));
			cwbOrder.setSendcarnum(rs.getLong("sendcarnum"));
			cwbOrder.setBackcarnum(rs.getLong("backcarnum"));
			cwbOrder.setCaramount(rs.getBigDecimal("caramount"));
			cwbOrder.setBackcarname(StringUtil.nullConvertToEmptyString(rs.getString("backcarname")));
			cwbOrder.setSendcarname(StringUtil.nullConvertToEmptyString(rs.getString("sendcarname")));
			cwbOrder.setDeliverid(rs.getLong("deliverid"));
			cwbOrder.setEmailfinishflag(rs.getInt("emailfinishflag"));
			cwbOrder.setReacherrorflag(rs.getInt("reacherrorflag"));
			cwbOrder.setOrderflowid(rs.getLong("orderflowid"));
			cwbOrder.setFlowordertype(rs.getLong("flowordertype"));
			cwbOrder.setCwbreachbranchid(rs.getLong("cwbreachbranchid"));
			cwbOrder.setCwbreachdeliverbranchid(rs.getLong("cwbreachdeliverbranchid"));
			cwbOrder.setPodfeetoheadflag(StringUtil.nullConvertToEmptyString(rs.getString("podfeetoheadflag")));
			cwbOrder.setPodfeetoheadcheckflag(StringUtil.nullConvertToEmptyString(rs.getString("podfeetoheadcheckflag")));
			cwbOrder.setLeavedreasonid(rs.getLong("leavedreasonid"));
			cwbOrder.setCustomerwarehouseid(StringUtil.nullConvertToEmptyString(rs.getString("customerwarehouseid")));
			cwbOrder.setEmaildate(StringUtil.nullConvertToEmptyString(rs.getString("emaildate")));
			cwbOrder.setEmaildateid(rs.getLong("emaildateid"));
			cwbOrder.setServiceareaid(rs.getLong("serviceareaid"));
			cwbOrder.setCustomerid(rs.getLong("customerid"));
			cwbOrder.setShipcwb(StringUtil.nullConvertToEmptyString(rs.getString("shipcwb")));
			cwbOrder.setConsigneeno(StringUtil.nullConvertToEmptyString(rs.getString("consigneeno")));
			cwbOrder.setConsigneeaddress(StringUtil.nullConvertToEmptyString(rs.getString("consigneeaddress")));
			cwbOrder.setConsigneepostcode(StringUtil.nullConvertToEmptyString(rs.getString("consigneepostcode")));
			cwbOrder.setCwbremark(StringUtil.nullConvertToEmptyString(rs.getString("cwbremark")));
			cwbOrder.setCustomercommand(StringUtil.nullConvertToEmptyString(rs.getString("customercommand")));
			cwbOrder.setTransway(StringUtil.nullConvertToEmptyString(rs.getString("transway")));
			cwbOrder.setCwbprovince(StringUtil.nullConvertToEmptyString(rs.getString("cwbprovince")));
			cwbOrder.setCwbcity(StringUtil.nullConvertToEmptyString(rs.getString("cwbcity")));
			cwbOrder.setCwbcounty(StringUtil.nullConvertToEmptyString(rs.getString("cwbcounty")));
			cwbOrder.setReceivablefee(rs.getBigDecimal("receivablefee"));
			cwbOrder.setPaybackfee(rs.getBigDecimal("paybackfee"));
			cwbOrder.setCwb(StringUtil.nullConvertToEmptyString(rs.getString("cwb")));
			cwbOrder.setShipperid(rs.getLong("shipperid"));
			cwbOrder.setCwbordertypeid(rs.getInt("cwbordertypeid"));
			cwbOrder.setTranscwb(StringUtil.nullConvertToEmptyString(rs.getString("transcwb")));
			cwbOrder.setDestination(StringUtil.nullConvertToEmptyString(rs.getString("destination")));
			cwbOrder.setCwbdelivertypeid(StringUtil.nullConvertToEmptyString(rs.getString("cwbdelivertypeid")));
			cwbOrder.setExceldeliver(StringUtil.nullConvertToEmptyString(rs.getString("exceldeliver")));
			cwbOrder.setExcelbranch(StringUtil.nullConvertToEmptyString(rs.getString("excelbranch")));
			cwbOrder.setExcelimportuserid(rs.getLong("excelimportuserid"));
			cwbOrder.setState(rs.getLong("state"));
			cwbOrder.setPrinttime(rs.getString("printtime"));
			cwbOrder.setCommonid(rs.getLong("commonid"));
			cwbOrder.setCommoncwb(StringUtil.nullConvertToEmptyString(rs.getString("commoncwb")));
			cwbOrder.setSigntypeid(rs.getLong("signtypeid"));
			cwbOrder.setPodrealname(rs.getString("podrealname"));
			cwbOrder.setPodtime(rs.getString("podtime"));
			cwbOrder.setPodsignremark(rs.getString("podsignremark"));
			cwbOrder.setModelname(rs.getString("modelname"));
			cwbOrder.setScannum(rs.getLong("scannum"));
			cwbOrder.setIsaudit(rs.getLong("isaudit"));
			cwbOrder.setRemark1(StringUtil.nullConvertToEmptyString(rs.getString("remark1")));
			cwbOrder.setRemark2(StringUtil.nullConvertToEmptyString(rs.getString("remark2")));
			cwbOrder.setRemark3(StringUtil.nullConvertToEmptyString(rs.getString("remark3")));
			cwbOrder.setRemark4(StringUtil.nullConvertToEmptyString(rs.getString("remark4")));
			cwbOrder.setRemark5(StringUtil.nullConvertToEmptyString(rs.getString("remark5")));
			cwbOrder.setBackreason(rs.getString("backreason"));
			cwbOrder.setLeavedreason(rs.getString("leavedreason"));
			cwbOrder.setPaywayid(rs.getLong("paywayid"));
			cwbOrder.setNewpaywayid(rs.getString("newpaywayid"));
			cwbOrder.setTuihuoid(rs.getLong("tuihuoid"));
			cwbOrder.setDeliverybranchid(rs.getLong("deliverybranchid"));
			cwbOrder.setCwbstate(rs.getLong("cwbstate"));
			cwbOrder.setCurrentbranchid(rs.getLong("currentbranchid"));
			cwbOrder.setBackreasonid(rs.getLong("backreasonid"));
			cwbOrder.setLeavedreasonid(rs.getLong("leavedreasonid"));
			cwbOrder.setPackagecode(rs.getString("packagecode"));
			cwbOrder.setMulti_shipcwb(rs.getString("multi_shipcwb"));
			cwbOrder.setDeliverystate(rs.getInt("deliverystate"));
			cwbOrder.setBackreturnreason(rs.getString("backreturnreason"));
			cwbOrder.setBackreturnreasonid(rs.getLong("backreturnreasonid"));
			cwbOrder.setHandleperson(rs.getLong("handleperson"));
			cwbOrder.setHandlereason(rs.getString("handlereason"));
			cwbOrder.setHandleresult(rs.getLong("handleresult"));
			cwbOrder.setAddresscodeedittype(rs.getInt("addresscodeedittype"));
			cwbOrder.setResendtime(rs.getString("resendtime"));
			cwbOrder.setWeishuakareason(rs.getString("weishuakareason"));
			cwbOrder.setWeishuakareasonid(rs.getLong("weishuakareasonid"));
			cwbOrder.setLosereason(rs.getString("losereason"));
			cwbOrder.setLosereasonid(rs.getLong("losereasonid"));
			cwbOrder.setShouldfare(rs.getBigDecimal("shouldfare"));
			cwbOrder.setInfactfare(rs.getBigDecimal("infactfare"));
			ExportwarhousesummaryDAO.this.setValueByUser(rs, cwbOrder);
			return cwbOrder;
		}
	}

	private void setValueByUser(ResultSet rs, CwbOrder cwbOrder) throws SQLException {
		if (this.getUser().getShownameflag() != 1) {
			cwbOrder.setConsigneename("******");
		} else {
			cwbOrder.setConsigneename(StringUtil.nullConvertToEmptyString(rs.getString("consigneename")));
		}
		if (this.getUser().getShowphoneflag() != 1) {
			cwbOrder.setConsigneephone("******");
		} else {
			cwbOrder.setConsigneephone(StringUtil.nullConvertToEmptyString(rs.getString("consigneephone")));
		}
		if (this.getUser().getShowmobileflag() != 1) {
			cwbOrder.setConsigneemobile("******");
		} else {
			cwbOrder.setConsigneemobile(StringUtil.nullConvertToEmptyString(rs.getString("consigneemobile")));
		}
		cwbOrder.setConsigneemobileOfkf(StringUtil.nullConvertToEmptyString(rs.getString("consigneemobile")));
		cwbOrder.setConsigneenameOfkf(StringUtil.nullConvertToEmptyString(rs.getString("consigneename")));
		cwbOrder.setConsigneephoneOfkf(StringUtil.nullConvertToEmptyString(rs.getString("consigneephone")));
	}

	private User getUser() {
		return this.getSessionUser() == null ? new User() : this.getSessionUser();
	}

	private User getSessionUser() {
		ExplinkUserDetail userDetail = new ExplinkUserDetail();
		try {
			userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		} catch (Exception e) {
			User u = new User();
			u.setShowmobileflag(1);
			u.setShowphoneflag(1);
			u.setShownameflag(1);
			return u;
		}
		return userDetail.getUser();

	}

	private final class BranchSum implements RowMapper<Exportwarhousesummary> {
		@Override
		public Exportwarhousesummary mapRow(ResultSet rs, int rowNum) throws SQLException {
			Exportwarhousesummary ex = new Exportwarhousesummary();
			ex.setBranchId(rs.getInt("nextbranchid"));
			ex.setCredate(rs.getString("credate"));
			ex.setBranchSum(rs.getInt("branchsum"));
			return ex;
		}
	}

	private final class Cwbs implements RowMapper<CwbOrder> {
		@Override
		public CwbOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
			CwbOrder cwb = new CwbOrder();
			cwb.setCwb(rs.getString("cwb"));
			return cwb;
		}
	}

	private final class Warhouse implements RowMapper<Exportwarhousesummary> {
		@Override
		public Exportwarhousesummary mapRow(ResultSet rs, int rowNum) throws SQLException {
			Exportwarhousesummary cwb = new Exportwarhousesummary();
			cwb.setCwbs(rs.getString("cwb"));
			cwb.setBranchId(rs.getInt("branchId"));
			return cwb;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 每个站点每天数据统计
	 *
	 * @param startbranchid
	 * @param strtime
	 * @param endtime
	 * @return
	 */
	public List<Exportwarhousesummary> getBranchSum(String nextbranchid, String startbranchid, String strtime, String endtime) {
		String sql = "select SUBSTR(credate,1,10) as credate , nextbranchid, COUNT(cwb) as branchsum" + " from express_ops_warehouse_to_branch  " + " WHERE " + " startbranchid in (" + startbranchid
				+ ") " + " and nextbranchid in (" + nextbranchid + ") " + " and   type='1' " + " and   state='1' " + " and   credate > '" + strtime + "'" + " and credate < '" + endtime + "'"
				+ " GROUP BY 1, 2 ";
		try {
			return this.jdbcTemplate.query(sql, new BranchSum());
		} catch (Exception ee) {
			return null;
		}
	}

	/**
	 * 所有站点入库每天统计
	 *
	 * @param startbranchid
	 * @param strtime
	 * @param endtime
	 * @return
	 */
	public List<Exportwarhousesummary> getBranchAllSum(String nextbranchid, String startbranchid, String strtime, String endtime) {
		String sql = "select SUBSTR(credate,1,10) as credate , nextbranchid, COUNT(cwb) as branchsum" + " from express_ops_warehouse_to_branch  " + " WHERE " + " startbranchid in (" + startbranchid
				+ ") " + " and nextbranchid in (" + nextbranchid + ") " + " and   type='1' " + " and   state='1' " + " and   credate > '" + strtime + "'" + " and credate < '" + endtime + "'"
				+ " GROUP BY 1 ";
		try {
			return this.jdbcTemplate.query(sql, new BranchSum());

		} catch (Exception ee) {
			return null;
		}
	}

	/**
	 * 库房入库每天总计
	 *
	 * @param branchid
	 * @param strtime
	 * @param endtime
	 * @return
	 */

	public List<Exportwarhousesummary> getWarhouseSum(String branchid, String strtime, String endtime) {
		String sql = "select SUBSTR(credate,1,10) as credate , branchid as nextbranchid ,COUNT(cwb) as branchsum" + " from express_ops_order_intowarhouse   " + " WHERE " + " branchid in (" + branchid
				+ ") " + " and   flowordertype='4' " + " and   state='1' " + " and   credate > '" + strtime + "'" + " and   credate < '" + endtime + "'" + " GROUP BY 1 ";
		try {
			return this.jdbcTemplate.query(sql, new BranchSum());

		} catch (Exception ee) {
			return null;
		}
	}

	/**
	 * 库房入库总计
	 *
	 * @param branchid
	 * @param strtime
	 * @param endtime
	 * @return
	 */
	public int getWarhouseAllSum(String branchid, String strtime, String endtime) {
		String sql = "select COUNT(cwb) as branchsum" + " from express_ops_order_intowarhouse   " + " WHERE " + " branchid in (" + branchid + ") " + " and   flowordertype='4' " + " and   state='1' "
				+ " and   credate > '" + strtime + "'" + " and   credate < '" + endtime + "'";
		try {
			return this.jdbcTemplate.queryForInt(sql);

		} catch (Exception ee) {
			return 0;
		}
	}

	/**
	 * 每个站点所有天数的合计
	 *
	 * @param startbranchid
	 * @param strtime
	 * @param endtime
	 * @return
	 */
	public List<Exportwarhousesummary> getBranchAllDay(String nextbranchid, String startbranchid, String strtime, String endtime) {
		String sql = "select SUBSTR(credate,1,10) as credate , nextbranchid, COUNT(cwb) as branchsum" + " from express_ops_warehouse_to_branch  " + " WHERE " + " startbranchid in (" + startbranchid
				+ ") " + " and nextbranchid in (" + nextbranchid + ") " + " and   type='1' " + " and   state='1' " + " and   credate > '" + strtime + "'" + " and credate < '" + endtime + "'"
				+ " GROUP BY 2 ";
		try {
			return this.jdbcTemplate.query(sql, new BranchSum());
		} catch (Exception ee) {
			return null;
		}
	}

	/**
	 * 所有站点所有天数合计
	 *
	 * @param startbranchid
	 * @param strtime
	 * @param endtime
	 * @return
	 */
	public int getAllBranchAllDay(String nextbranchid, String startbranchid, String strtime, String endtime) {
		String sql = " select COUNT(cwb) as branchsum" + " from express_ops_warehouse_to_branch  " + " WHERE " + " startbranchid in (" + startbranchid + ") " + " and nextbranchid in (" + nextbranchid
				+ ") " + " and   type='1' " + " and   state='1' " + " and   credate > '" + strtime + "'" + " and credate < '" + endtime + "'";
		try {
			return this.jdbcTemplate.queryForInt(sql);
		} catch (Exception ee) {
			return 0;
		}
	}

	/**
	 * 库房每天入库订单
	 *
	 * @param startbranchid
	 * @param strtime
	 * @param endtime
	 * @return
	 */
	public List<CwbOrder> getCwbsByeveryday(long page, String branchid, String strtime) {
		String sql = " select cwb " + " from express_ops_order_intowarhouse  " + " WHERE " + " branchid in (" + branchid + ") " + " and   flowordertype='4' " + " and   state='1' "
				+ " and SUBSTR(credate,1,10) = '" + strtime + "'" + " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		;
		try {
			return this.jdbcTemplate.query(sql, new Cwbs());
		} catch (Exception ee) {
			return null;
		}
	}

	public List<CwbOrder> getCwbsByeverydayexcel(long page, String branchid, String strtime) {
		String sql = " select cwb " + " from express_ops_order_intowarhouse  " + " WHERE " + " branchid in (" + branchid + ") " + " and   flowordertype='4' " + " and   state='1' "
				+ " and SUBSTR(credate,1,10) = '" + strtime + "'" + " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		;
		try {
			return this.jdbcTemplate.query(sql, new Cwbs());
		} catch (Exception ee) {
			return null;
		}
	}

	public int getCwbsByeverydaycount(String branchid, String strtime) {
		String sql = " select COUNT(cwb) " + " from express_ops_order_intowarhouse  " + " WHERE " + " branchid in (" + branchid + ") " + " and   flowordertype='4' " + " and   state='1' "
				+ " and SUBSTR(credate,1,10) = '" + strtime + "'";
		try {
			return this.jdbcTemplate.queryForInt(sql);
		} catch (Exception ee) {
			return 0;
		}
	}

	/**
	 * 库房所有天数入库订单
	 *
	 * @param startbranchid
	 * @param strtime
	 * @param endtime
	 * @return
	 */
	public List<CwbOrder> getCwbsByAlleveryday(long page, String branchid, String strtime, String endtime) {
		String sql = " select cwb " + " from express_ops_order_intowarhouse  " + " WHERE " + " branchid in (" + branchid + ") " + " and   flowordertype='4' " + " and   state='1' "
				+ " and credate > '" + strtime + "'" + " and credate < '" + endtime + "'" + " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		;

		try {
			return this.jdbcTemplate.query(sql, new Cwbs());
		} catch (Exception ee) {
			return null;
		}
	}

	public int getCwbsByAlleverydaycount(String branchid, String strtime, String endtime) {
		String sql = " select COUNT(cwb) " + " from express_ops_order_intowarhouse  " + " WHERE " + " branchid in (" + branchid + ") " + " and   flowordertype='4' " + " and   state='1' "
				+ " and credate > '" + strtime + "'" + " and credate < '" + endtime + "'";
		try {
			return this.jdbcTemplate.queryForInt(sql);
		} catch (Exception ee) {
			return 0;
		}
	}

	/**
	 * 站点每天订单
	 *
	 * @param startbranchid
	 * @param strtime
	 * @param endtime
	 * @return
	 */
	public List<CwbOrder> getCwbsByBrancheveryday(long page, String branchid, String strtime) {
		String sql = " select cwb " + " from express_ops_warehouse_to_branch  " + " WHERE " + " nextbranchid in (" + branchid + ") " + " and   type='1' " + " and   state='1' "
				+ " and SUBSTR(credate,1,10) = '" + strtime + "'" + " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		;
		try {
			return this.jdbcTemplate.query(sql, new Cwbs());
		} catch (Exception ee) {
			return null;
		}
	}

	public int getCwbsByBrancheverydaycount(String branchid, String strtime) {
		String sql = " select Count(cwb) " + " from express_ops_warehouse_to_branch  " + " WHERE " + " nextbranchid in (" + branchid + ") " + " and   type='1' " + " and   state='1' "
				+ " and SUBSTR(credate,1,10) = '" + strtime + "'";
		try {
			return this.jdbcTemplate.queryForInt(sql);
		} catch (Exception ee) {
			return 0;
		}
	}

	/**
	 * 每个站点所有天数订单
	 *
	 * @param startbranchid
	 * @param strtime
	 * @param endtime
	 * @return
	 */
	public List<CwbOrder> getCwbsByBranchAllday(long page, String branchid, String strtime, String endtime) {
		String sql = " select cwb " + " from express_ops_warehouse_to_branch  " + " WHERE " + " nextbranchid in (" + branchid + ") " + " and   type='1' " + " and   state='1' " + " and credate > '"
				+ strtime + "'" + " and credate < '" + endtime + "'" + " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		;
		try {
			return this.jdbcTemplate.query(sql, new Cwbs());
		} catch (Exception ee) {
			return null;
		}
	}

	public int getCwbsByBranchAlldaycount(String branchid, String strtime, String endtime) {
		String sql = " select COUNT(cwb) " + " from express_ops_warehouse_to_branch  " + " WHERE " + " nextbranchid in (" + branchid + ") " + " and   type='1' " + " and   state='1' "
				+ " and credate > '" + strtime + "'" + " and credate < '" + endtime + "'";
		try {
			return this.jdbcTemplate.queryForInt(sql);
		} catch (Exception ee) {
			return 0;
		}
	}

	/**
	 * 所有站点每天订单
	 *
	 * @param startbranchid
	 * @param strtime
	 * @param endtime
	 * @return
	 */
	public List<CwbOrder> getCwbsByALLBrancheveryday(long page, String branchid, String strtime, String endtime) {
		String sql = " select cwb " + " from express_ops_warehouse_to_branch  " + " WHERE " + " nextbranchid in (" + branchid + ") " + " and   type='1' " + " and   state='1' " + " and credate > '"
				+ strtime + "'" + " and credate < '" + endtime + "'" + " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		;
		try {
			return this.jdbcTemplate.query(sql, new Cwbs());
		} catch (Exception ee) {
			return null;
		}
	}

	public int getCwbsByALLBrancheverydaycount(String branchid, String strtime, String endtime) {
		String sql = " select COUNT(cwb) " + " from express_ops_warehouse_to_branch  " + " WHERE " + " nextbranchid in (" + branchid + ") " + " and   type='1' " + " and   state='1' "
				+ " and credate > '" + strtime + "'" + " and credate < '" + endtime + "'";
		try {
			return this.jdbcTemplate.queryForInt(sql);
		} catch (Exception ee) {
			return 0;
		}
	}

	public List<CwbOrder> getCwbsDetail(String cwb) {
		String sql = " select * " + " from express_ops_cwb_detail  " + " WHERE " + " cwb in (" + cwb + ") " + " and   state='1' ";
		try {
			return this.jdbcTemplate.query(sql, new CwbMapper());
		} catch (Exception ee) {
			return null;
		}
	}

	// 根据订单号失效
	public void dataLoseByCwb(String cwb) {
		this.jdbcTemplate.update("update express_ops_warehouse_to_branch set state=0  where state =1 and cwb=? ", cwb);
	}

	// 根据订单号失效
	public void LoseintowarhouseByCwb(String cwb) {
		this.jdbcTemplate.update("update express_ops_order_intowarhouse set state=0  where state =1 and cwb=? ", cwb);
	}

	public Exportwarhousesummary getIntowarhouse(String cwb) {
		String sql = " select *  from express_ops_order_intowarhouse   WHERE  cwb = '" + cwb + "' and   state=1 ";
		try {
			return this.jdbcTemplate.queryForObject(sql, new Warhouse());
		} catch (Exception ee) {
			return null;
		}
	}

	public void setIntowarhouse(String cwb, long branchid, String credate, Long userid, int flowordertype) {
		this.jdbcTemplate.update("insert into express_ops_order_intowarhouse(cwb,branchid,credate,userid,flowordertype) " + " values(?,?,?,?,?)", cwb, branchid, credate, userid, flowordertype);
	}

	public void updateIntowarhouse(String cwb, long branchid, String credate, Long userid, int flowordertype) {
		this.jdbcTemplate.update("update  express_ops_order_intowarhouse set branchid=?,credate=?,userid=?,flowordertype=? where cwb=?", branchid, credate, userid, flowordertype, cwb);
	}
	
	public List<CwbOrder> findcwbByCwbsAndDateAndtype(String cwbs,String startdate,String enddate,String cwbtypeid){
		String sql="select * from express_ops_order_intowarhouse where cwb in("+cwbs+") and state=1  and credate>'"+startdate+"' and credate<'"+enddate+"'";
		List<CwbOrder> cwblist=jdbcTemplate.query(sql, new Cwbs());
		List<CwbOrder> cwborderlist=null;
		if(!cwbtypeid.equals("")&&Integer.valueOf(cwbtypeid)>0){
			StringBuilder sb = new StringBuilder();
			String listcwbs="";	
				for(CwbOrder str:cwblist){
					sb=sb.append("'"+str.getCwb()+"',");
				}
				listcwbs=sb.substring(0, sb.length()-1);
			String sql1="select * from express_ops_cwb_detail where cwb in("+listcwbs+") and state=1  and cwbordertypeid='"+cwbtypeid+"'";
			cwborderlist=jdbcTemplate.query(sql1,new CwbMapper());
		}else{  
			cwborderlist=cwblist;
		}
		return cwborderlist;
	}
	
	public List<CwbOrder> findcwbByCwbsAndDateAndtypePage(String cwbs,String startdate,String enddate,String cwbtypeid,int start,int pageSize){
		String sql="select * from express_ops_order_intowarhouse where cwb in("+cwbs+") and state=1  and credate>'"+startdate+"' and credate<'"+enddate+"' limit "+start+","+pageSize;
		List<CwbOrder> cwblist=jdbcTemplate.query(sql, new Cwbs());
		List<CwbOrder> cwborderlist=null;
		if(!cwbtypeid.equals("")&&Integer.valueOf(cwbtypeid)>0){
			StringBuilder sb = new StringBuilder();
			String listcwbs="";	
				for(CwbOrder str:cwblist){
					sb=sb.append("'"+str.getCwb()+"',");
				}
				listcwbs=sb.substring(0, sb.length()-1);
			String sql1="select * from express_ops_cwb_detail where cwb in("+listcwbs+") and state=1  and cwbordertypeid='"+cwbtypeid+"'";
			cwborderlist=jdbcTemplate.query(sql1,new CwbMapper());
		}else{  
			cwborderlist=cwblist;
		}
		return cwborderlist;
	}
	
	public List<CwbOrder> findcwbByCwbsAndDateAndtypePageLike(String cwbs,String startdate,String enddate,String cwbtypeid,int start,int pageSize){
		String sql="select * from express_ops_order_intowarhouse where cwb like '%"+cwbs+"%' and state=1 and credate>'"+startdate+"' and credate<'"+enddate+"' limit "+start+","+pageSize;
		List<CwbOrder> cwblist=jdbcTemplate.query(sql, new Cwbs());
		List<CwbOrder> cwborderlist=null;
		if(!cwbtypeid.equals("")&&Integer.valueOf(cwbtypeid)>0){
			StringBuilder sb = new StringBuilder();
			String listcwbs="";	
				for(CwbOrder str:cwblist){
					sb=sb.append("'"+str.getCwb()+"',");
				}
				listcwbs=sb.substring(0, sb.length()-1);
			String sql1="select * from express_ops_cwb_detail where cwb in("+listcwbs+") and state=1  and cwbordertypeid='"+cwbtypeid+"'";
			cwborderlist=jdbcTemplate.query(sql1,new CwbMapper());
		}else{  
			cwborderlist=cwblist;
		}
		return cwborderlist;
	}
	
	public long findcwbByCwbsAndDateAndtypeLikeCount(String cwbs,String startdate,String enddate,String cwbtypeid){
		String sql="select * from express_ops_order_intowarhouse where cwb like '%"+cwbs+"%' and state=1  and credate>'"+startdate+"' and credate<'"+enddate+"'";
		List<CwbOrder> cwblist=jdbcTemplate.query(sql, new Cwbs());
		String sqlcount="select count(1) from express_ops_order_intowarhouse where cwb in("+cwbs+") and state=1  and credate>'"+startdate+"' and credate<'"+enddate+"'";
		long count=jdbcTemplate.queryForLong(sqlcount);
		long totalcount=0;
		if(!cwbtypeid.equals("")&&Integer.valueOf(cwbtypeid)>0){
			StringBuilder sb = new StringBuilder();
			String listcwbs="";	
				for(CwbOrder str:cwblist){
					sb=sb.append("'"+str.getCwb()+"',");
				}
				listcwbs=sb.substring(0, sb.length()-1);
			String sql1="select count(1) from express_ops_cwb_detail where cwb in("+listcwbs+") and state=1  and cwbordertypeid='"+cwbtypeid+"'";
			totalcount=jdbcTemplate.queryForLong(sql1);
		}else{  
			totalcount=count;
		}
		return totalcount;
	}
	public long findcwbByCwbsAndDateAndtypeCount(String cwbs,String startdate,String enddate,String cwbtypeid){
		String sql="select * from express_ops_order_intowarhouse where cwb in ("+cwbs+") and state=1  and credate>'"+startdate+"' and credate<'"+enddate+"'";
		List<CwbOrder> cwblist=jdbcTemplate.query(sql, new Cwbs());
		String sqlcount="select count(1) from express_ops_order_intowarhouse where cwb in("+cwbs+") and state=1  and credate>'"+startdate+"' and credate<'"+enddate+"'";
		long count=jdbcTemplate.queryForLong(sqlcount);
		long totalcount=0;
		if(!cwbtypeid.equals("")&&Integer.valueOf(cwbtypeid)>0){
			StringBuilder sb = new StringBuilder();
			String listcwbs="";	
				for(CwbOrder str:cwblist){
					sb=sb.append("'"+str.getCwb()+"',");
				}
				listcwbs=sb.substring(0, sb.length()-1);
			String sql1="select count(1) from express_ops_cwb_detail where cwb in("+listcwbs+") and state=1  and cwbordertypeid='"+cwbtypeid+"'";
			totalcount=jdbcTemplate.queryForLong(sql1);
		}else{  
			totalcount=count;
		}
		return totalcount;
	}
}
