package cn.explink.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.domain.CwbOrder;
import cn.explink.domain.ExpressSetBranchDeliveryFeeBill;
import cn.explink.domain.VO.ExpressSetBranchDeliveryFeeBillVO;
import cn.explink.service.PunishInsideService;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Component
public class BranchDeliveryFeeBillDAO {
	@Autowired
	PunishInsideService punishInsideService;

	private final class BranchDeliveryFeeBillMapper implements
			RowMapper<ExpressSetBranchDeliveryFeeBill> {
		@Override
		public ExpressSetBranchDeliveryFeeBill mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			ExpressSetBranchDeliveryFeeBill branchDeliveryFeeBill = new ExpressSetBranchDeliveryFeeBill();
			branchDeliveryFeeBill.setId(rs.getInt("id"));
			branchDeliveryFeeBill.setBillBatch(rs.getString("billBatch"));
			branchDeliveryFeeBill.setBillState(rs.getInt("billState"));
			branchDeliveryFeeBill.setBranchId(rs.getInt("branchId"));
			branchDeliveryFeeBill.setCreateDate(rs.getString("createDate"));
			branchDeliveryFeeBill.setHeXiaoDate(rs.getString("heXiaoDate"));
			branchDeliveryFeeBill.setDateType(rs.getInt("dateType"));
			branchDeliveryFeeBill.setBeginDate(rs.getString("beginDate"));
			branchDeliveryFeeBill.setEndDate(rs.getString("endDate"));
			branchDeliveryFeeBill.setCwbs(rs.getString("cwbs"));
			branchDeliveryFeeBill.setCwbType(rs.getInt("cwbType"));
			branchDeliveryFeeBill.setCwbCount(rs.getInt("cwbCount"));
			branchDeliveryFeeBill.setDeliveryFee(rs
					.getBigDecimal("deliveryFee"));
			branchDeliveryFeeBill.setRemark(rs.getString("remark"));
			branchDeliveryFeeBill.setCreator(rs.getInt("creator"));
			branchDeliveryFeeBill.setHeXiaoPerson(rs.getInt("heXiaoPerson"));
			branchDeliveryFeeBill.setShenHePerson(rs.getInt("shenHePerson"));
			branchDeliveryFeeBill.setShenHeDate(rs.getString("shenHeDate"));

			return branchDeliveryFeeBill;
		}
	}
	
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
			cwbOrder.setTimelimited(StringUtil.nullConvertToEmptyString(rs.getString("timelimited")));
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
			cwbOrder.setHistorybranchname(rs.getString("historybranchname"));
			cwbOrder.setGoodsType(rs.getInt("goods_type"));
			cwbOrder.setOutareaflag(rs.getInt("outareaflag"));
			cwbOrder.setChangereason(rs.getString("changereason"));
			cwbOrder.setChangereasonid(rs.getLong("changereasonid"));
			cwbOrder.setFirstchangereasonid(rs.getLong("firstchangereasonid"));
			/*cwbOrder.setZhongzhuanreasonid(rs.getLong("zhongzhuanreasonid"));
			cwbOrder.setZhongzhuanreason(rs.getString("zhongzhuanreason"));*/
			cwbOrder.setFnorgoffset(rs.getBigDecimal("fnorgoffset"));
			cwbOrder.setFnorgoffsetflag(rs.getInt("fnorgoffsetflag"));
//			CwbDAO.this.setValueByUser(rs, cwbOrder);

			return cwbOrder;
		}

	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public long createBranchDeliveryFeeBill(
			final ExpressSetBranchDeliveryFeeBill branchDeliveryFeeBill) {
		KeyHolder key = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(
					java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement(
						"insert into express_set_branch_delivery_fee_bill("
								+ "billBatch,billState,branchId,createDate,heXiaoDate,"
								+ "dateType,beginDate,endDate,cwbs,cwbType,cwbCount,"
								+ "deliveryFee,remark,creator,heXiaoPerson,shenHePerson,shenHeDate"
								+ ") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
						new String[] { "id" });
				int i = 1;

				ps.setString(i++, branchDeliveryFeeBill.getBillBatch());
				ps.setInt(i++, branchDeliveryFeeBill.getBillState());
				ps.setInt(i++, branchDeliveryFeeBill.getBranchId());
				ps.setString(i++, branchDeliveryFeeBill.getCreateDate());
				ps.setString(i++, branchDeliveryFeeBill.getHeXiaoDate());
				ps.setInt(i++, branchDeliveryFeeBill.getDateType());
				ps.setString(i++, branchDeliveryFeeBill.getBeginDate());
				ps.setString(i++, branchDeliveryFeeBill.getEndDate());
				ps.setString(i++, branchDeliveryFeeBill.getCwbs());
				ps.setInt(i++, branchDeliveryFeeBill.getCwbType());
				ps.setInt(i++, branchDeliveryFeeBill.getCwbCount());
				ps.setBigDecimal(i++, branchDeliveryFeeBill.getDeliveryFee());
				ps.setString(i++, branchDeliveryFeeBill.getRemark());
				ps.setInt(i++, branchDeliveryFeeBill.getCreator());
				ps.setInt(i++, branchDeliveryFeeBill.getHeXiaoPerson());
				ps.setInt(i++, branchDeliveryFeeBill.getShenHePerson());
				ps.setString(i++, branchDeliveryFeeBill.getShenHeDate());

				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

	public void updateBranchDeliveryFeeBill(
			final ExpressSetBranchDeliveryFeeBill branchDeliveryFeeBill) {

		this.jdbcTemplate
				.update("update express_set_branch_delivery_fee_bill set "
						+ "billState=?,heXiaoDate=?,cwbs=?,"
						+ "cwbCount=?,deliveryFee=?,remark=?,"
						+ "heXiaoPerson=?,shenHePerson=?,shenHeDate=? where id=?",
						new PreparedStatementSetter() {
							@Override
							public void setValues(PreparedStatement ps)
									throws SQLException {
								int i = 1;

								ps.setInt(i++,
										branchDeliveryFeeBill.getBillState());
								ps.setString(i++,
										branchDeliveryFeeBill.getHeXiaoDate());
								ps.setString(i++,
										branchDeliveryFeeBill.getCwbs());
								ps.setInt(i++,
										branchDeliveryFeeBill.getCwbCount());
								ps.setBigDecimal(i++,
										branchDeliveryFeeBill.getDeliveryFee());
								ps.setString(i++,
										branchDeliveryFeeBill.getRemark());
								ps.setInt(i++,
										branchDeliveryFeeBill.getHeXiaoPerson());
								ps.setInt(i++,
										branchDeliveryFeeBill.getShenHePerson());
								ps.setString(i++,
										branchDeliveryFeeBill.getShenHeDate());
								ps.setInt(i++, branchDeliveryFeeBill.getId());
							}
						});
	}

	public void updateBranchDeliveryFeeBill(final String cwbs,
			final BigDecimal deliveryFee, final int cwbCount, final int id) {
		this.jdbcTemplate.update(
				"update express_set_branch_delivery_fee_bill set "
						+ "cwbs=?,deliveryFee=?,cwbCount=? where id=?",
				new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps)
							throws SQLException {
						ps.setString(1, cwbs);
						ps.setBigDecimal(2, deliveryFee);
						ps.setInt(3, cwbCount);
						ps.setInt(4, id);
					}
				});
	}

	public int deleteBranchDeliveryFeeBill(String ids) {
		String sql = "delete from express_set_branch_delivery_fee_bill where id in ("
				+ ids + ")";
		return this.jdbcTemplate.update(sql);
	}

	public List<ExpressSetBranchDeliveryFeeBill> getBranchDeliveryFeeBillList() {
		String sql = "select * from express_set_branch_delivery_fee_bill";
		return jdbcTemplate.query(sql, new BranchDeliveryFeeBillMapper());
	}

	public ExpressSetBranchDeliveryFeeBill getBranchDeliveryFeeBillListById(
			int id) {
		try {
			String sql = "select * from express_set_branch_delivery_fee_bill where id=?";
			return jdbcTemplate.queryForObject(sql,
					new BranchDeliveryFeeBillMapper(), id);
		} catch (DataAccessException e) {
			return null;
		}
	}

	public List<ExpressSetBranchDeliveryFeeBill> queryBranchDeliveryFeeBill(
			ExpressSetBranchDeliveryFeeBillVO branchDeliveryFeeBillVO) {

		String sql = "select * from express_set_branch_delivery_fee_bill pb "
				+ " left join express_set_branch b on pb.branchId = b.branchid "
				+ "where 1=1 ";

		if (branchDeliveryFeeBillVO != null) {
			if (StringUtils.isNotBlank(branchDeliveryFeeBillVO.getBillBatch())) {
				sql += " and pb.billBatch like '%"
						+ branchDeliveryFeeBillVO.getBillBatch() + "%' ";
			}
			if (branchDeliveryFeeBillVO.getBillState() != 0) {
				sql += " and pb.billState= '"
						+ branchDeliveryFeeBillVO.getBillState() + "' ";
			}

			if (StringUtils.isNotBlank(branchDeliveryFeeBillVO
					.getCreateDateFrom())) {
				sql += " and pb.createDate>= '"
						+ branchDeliveryFeeBillVO.getCreateDateFrom() + "' ";
			}
			if (StringUtils.isNotBlank(branchDeliveryFeeBillVO
					.getCreateDateTo())) {
				sql += " and pb.createDate<= '"
						+ branchDeliveryFeeBillVO.getCreateDateTo() + "' ";
			}
			if (StringUtils.isNotBlank(branchDeliveryFeeBillVO
					.getHeXiaoDateFrom())) {
				sql += " and pb.heXiaoDate>= '"
						+ branchDeliveryFeeBillVO.getHeXiaoDateFrom() + "' ";
			}
			if (StringUtils.isNotBlank(branchDeliveryFeeBillVO
					.getHeXiaoDateTo())) {
				sql += " and pb.heXiaoDate<= '"
						+ branchDeliveryFeeBillVO.getHeXiaoDateTo() + "' ";
			}
			if (StringUtils.isNotBlank(branchDeliveryFeeBillVO.getBranchName())) {
				sql += " and b.branchname like '%"
						+ branchDeliveryFeeBillVO.getBranchName() + "%' ";
			}
			if (branchDeliveryFeeBillVO.getCwbType() != 0) {
				sql += " and pb.cwbType = "
						+ branchDeliveryFeeBillVO.getCwbType();
			}
			if (StringUtils.isNotBlank(branchDeliveryFeeBillVO
					.getContractColumn())) {
				sql += " order by pb."
						+ branchDeliveryFeeBillVO.getContractColumn();
			}
			if (StringUtils.isNotBlank(branchDeliveryFeeBillVO
					.getContractColumnOrder())) {
				sql += " " + branchDeliveryFeeBillVO.getContractColumnOrder();
			}
		}

		return jdbcTemplate.query(sql, new BranchDeliveryFeeBillMapper());
	}
	
	public int queryBranchDeliveryFeeBillCount(
			ExpressSetBranchDeliveryFeeBillVO branchDeliveryFeeBillVO) {
		
		String sql = "select count(1) from express_set_branch_delivery_fee_bill pb "
				+ " left join express_set_branch b on pb.branchId = b.branchid "
				+ "where 1=1 ";
		
		if (branchDeliveryFeeBillVO != null) {
			if (StringUtils.isNotBlank(branchDeliveryFeeBillVO.getBillBatch())) {
				sql += " and pb.billBatch like '%"
						+ branchDeliveryFeeBillVO.getBillBatch() + "%' ";
			}
			if (branchDeliveryFeeBillVO.getBillState() != 0) {
				sql += " and pb.billState= '"
						+ branchDeliveryFeeBillVO.getBillState() + "' ";
			}
			
			if (StringUtils.isNotBlank(branchDeliveryFeeBillVO
					.getCreateDateFrom())) {
				sql += " and pb.createDate>= '"
						+ branchDeliveryFeeBillVO.getCreateDateFrom() + "' ";
			}
			if (StringUtils.isNotBlank(branchDeliveryFeeBillVO
					.getCreateDateTo())) {
				sql += " and pb.createDate<= '"
						+ branchDeliveryFeeBillVO.getCreateDateTo() + "' ";
			}
			if (StringUtils.isNotBlank(branchDeliveryFeeBillVO
					.getHeXiaoDateFrom())) {
				sql += " and pb.heXiaoDate>= '"
						+ branchDeliveryFeeBillVO.getHeXiaoDateFrom() + "' ";
			}
			if (StringUtils.isNotBlank(branchDeliveryFeeBillVO
					.getHeXiaoDateTo())) {
				sql += " and pb.heXiaoDate<= '"
						+ branchDeliveryFeeBillVO.getHeXiaoDateTo() + "' ";
			}
			if (StringUtils.isNotBlank(branchDeliveryFeeBillVO.getBranchName())) {
				sql += " and b.branchname like '%"
						+ branchDeliveryFeeBillVO.getBranchName() + "%' ";
			}
			if (branchDeliveryFeeBillVO.getCwbType() != 0) {
				sql += " and pb.cwbType = "
						+ branchDeliveryFeeBillVO.getCwbType();
			}
			if (StringUtils.isNotBlank(branchDeliveryFeeBillVO
					.getContractColumn())) {
				sql += " order by pb."
						+ branchDeliveryFeeBillVO.getContractColumn();
			}
			if (StringUtils.isNotBlank(branchDeliveryFeeBillVO
					.getContractColumnOrder())) {
				sql += " " + branchDeliveryFeeBillVO.getContractColumnOrder();
			}
		}
		
		return jdbcTemplate.queryForInt(sql);
	}
	
	
	public List<CwbOrder> queryBranchDeliveryFeeBill(
			ExpressSetBranchDeliveryFeeBill branchDeliveryFeeBill, String leftJoinSql,
			String onSql, String dateColumn) {

		String sql = "select cwb.* from express_ops_cwb_detail cwb "
				+ " left join express_ops_delivery_state d "
				+ leftJoinSql
				+ " on cwb.cwb = d.cwb "	
				+ onSql
				+ " where 1=1 ";

		if (branchDeliveryFeeBill != null) {
			if (StringUtils.isNotBlank(branchDeliveryFeeBill
					.getBeginDate())) {
				sql += " and " + dateColumn + " >= '"
						+ branchDeliveryFeeBill.getBeginDate() + "' ";
			}
			if (StringUtils.isNotBlank(branchDeliveryFeeBill
					.getEndDate())) {
				sql += " and " + dateColumn + " <= '"
						+ branchDeliveryFeeBill.getEndDate() + "' ";
			}
			if (branchDeliveryFeeBill.getBranchId() != 0) {
				sql += " and d.deliverybranchid = "
						+ branchDeliveryFeeBill.getBranchId();
			}
			if (branchDeliveryFeeBill.getCwbType() != 0) {
				sql += " and cwb.cwbordertypeid = "
						+ branchDeliveryFeeBill.getCwbType();
			}
		}

		return jdbcTemplate.query(sql, new CwbMapper());
	}

	public List<ExpressSetBranchDeliveryFeeBill> getMaxBillBatch() {
		String sql = "select * from express_set_branch_delivery_fee_bill order by billBatch desc";
		return jdbcTemplate.query(sql, new BranchDeliveryFeeBillMapper());
	}

	public BigDecimal calculateSumPrice(String punishNos) {
		String sql = "SELECT SUM(punishInsideprice)as sumprice FROM express_ops_punishInside_detail where 1=1";
		if (StringUtils.isNotBlank(punishNos)) {
			sql += " And punishNo IN(" + punishNos + ")";
		}
		return this.jdbcTemplate.queryForObject(sql, BigDecimal.class);
	}

	public List<ExpressSetBranchDeliveryFeeBill> getBranchDeliveryFeeBillByChukuDate(
			String begindate, String enddate, long page) {
		String sql = "select * from express_ops_bale where cretime>=? and cretime<=? ";
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ,"
				+ Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new BranchDeliveryFeeBillMapper(),
				begindate, enddate);
	}

	public long getBranchDeliveryFeeBillByChukuDateCount(String begindate,
			String enddate) {
		String sql = "select count(1) from express_ops_bale where cretime>=? and cretime<=? ";
		return jdbcTemplate.queryForLong(sql, begindate, enddate);
	}

	public ExpressSetBranchDeliveryFeeBill getBranchDeliveryFeeBillOneByBranchDeliveryFeeBillnoLock(
			String baleno) {
		try {
			String sql = "select * from express_ops_bale where baleno=? for update";
			return jdbcTemplate.queryForObject(sql,
					new BranchDeliveryFeeBillMapper(), baleno);
		} catch (DataAccessException e) {
			return null;
		}
	}

}
