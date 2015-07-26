package cn.explink.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.domain.CwbOrder;
import cn.explink.domain.ExpressSetExceedSubsidyApply;
import cn.explink.domain.VO.ExpressSetExceedSubsidyApplyVO;
import cn.explink.enumutil.ExceedSubsidyApplyStateEnum;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Component
public class ExceedSubsidyApplyDAO {

	private final class ExceedSubsidyApplyMapper implements
			RowMapper<ExpressSetExceedSubsidyApply> {
		@Override
		public ExpressSetExceedSubsidyApply mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			ExpressSetExceedSubsidyApply exceedSubsidyApply = new ExpressSetExceedSubsidyApply();
			exceedSubsidyApply.setId(rs.getInt("id"));
			exceedSubsidyApply.setApplyNo(rs.getString("applyNo"));
			exceedSubsidyApply.setApplyState(rs.getInt("applyState"));
			exceedSubsidyApply.setDeliveryPerson(rs.getInt("deliveryPerson"));
			exceedSubsidyApply.setCwbOrder(rs.getString("cwbOrder"));
			exceedSubsidyApply.setCwbOrderState(rs.getInt("cwbOrderState"));
			exceedSubsidyApply
					.setReceiveAddress(rs.getString("receiveAddress"));
			exceedSubsidyApply.setIsExceedAreaSubsidy(rs
					.getInt("isExceedAreaSubsidy"));
			exceedSubsidyApply.setExceedAreaSubsidyRemark(rs
					.getString("exceedAreaSubsidyRemark"));
			exceedSubsidyApply.setExceedAreaSubsidyAmount(rs
					.getBigDecimal("exceedAreaSubsidyAmount"));
			exceedSubsidyApply.setIsBigGoodsSubsidy(rs
					.getInt("isBigGoodsSubsidy"));
			exceedSubsidyApply.setBigGoodsSubsidyRemark(rs
					.getString("bigGoodsSubsidyRemark"));
			exceedSubsidyApply.setBigGoodsSubsidyAmount(rs
					.getBigDecimal("bigGoodsSubsidyAmount"));
			exceedSubsidyApply.setRemark(rs.getString("remark"));
			exceedSubsidyApply.setShenHePerson(rs.getInt("shenHePerson"));
			exceedSubsidyApply.setShenHeTime(rs.getString("shenHeTime"));
			exceedSubsidyApply.setShenHeIdea(rs.getString("shenHeIdea"));
			exceedSubsidyApply.setCreateTime(rs.getString("createTime"));
			exceedSubsidyApply.setUpdateTime(rs.getString("updateTime"));

			return exceedSubsidyApply;
		}
	}

	private final class CwbMapper implements RowMapper<CwbOrder> {

		@Override
		public CwbOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
			CwbOrder cwbOrder = new CwbOrder();
			cwbOrder.setOpscwbid(rs.getLong("opscwbid"));
			cwbOrder.setStartbranchid(rs.getLong("startbranchid"));
			cwbOrder.setNextbranchid(rs.getLong("nextbranchid"));
			cwbOrder.setBacktocustomer_awb(StringUtil
					.nullConvertToEmptyString(rs
							.getString("backtocustomer_awb")));
			cwbOrder.setCwbflowflag(StringUtil.nullConvertToEmptyString(rs
					.getString("cwbflowflag")));
			cwbOrder.setCarrealweight(rs.getBigDecimal("carrealweight"));
			cwbOrder.setCartype(StringUtil.nullConvertToEmptyString(rs
					.getString("cartype")));
			cwbOrder.setCarwarehouse(StringUtil.nullConvertToEmptyString(rs
					.getString("carwarehouse")));
			cwbOrder.setCarsize(StringUtil.nullConvertToEmptyString(rs
					.getString("carsize")));
			cwbOrder.setBackcaramount(rs.getBigDecimal("backcaramount"));
			cwbOrder.setSendcarnum(rs.getLong("sendcarnum"));
			cwbOrder.setBackcarnum(rs.getLong("backcarnum"));
			cwbOrder.setCaramount(rs.getBigDecimal("caramount"));
			cwbOrder.setBackcarname(StringUtil.nullConvertToEmptyString(rs
					.getString("backcarname")));
			cwbOrder.setSendcarname(StringUtil.nullConvertToEmptyString(rs
					.getString("sendcarname")));
			cwbOrder.setDeliverid(rs.getLong("deliverid"));
			cwbOrder.setEmailfinishflag(rs.getInt("emailfinishflag"));
			cwbOrder.setReacherrorflag(rs.getInt("reacherrorflag"));
			cwbOrder.setOrderflowid(rs.getLong("orderflowid"));
			cwbOrder.setFlowordertype(rs.getLong("flowordertype"));
			cwbOrder.setCwbreachbranchid(rs.getLong("cwbreachbranchid"));
			cwbOrder.setCwbreachdeliverbranchid(rs
					.getLong("cwbreachdeliverbranchid"));
			cwbOrder.setPodfeetoheadflag(StringUtil.nullConvertToEmptyString(rs
					.getString("podfeetoheadflag")));
			cwbOrder.setPodfeetoheadcheckflag(StringUtil
					.nullConvertToEmptyString(rs
							.getString("podfeetoheadcheckflag")));
			cwbOrder.setLeavedreasonid(rs.getLong("leavedreasonid"));
			cwbOrder.setCustomerwarehouseid(StringUtil
					.nullConvertToEmptyString(rs
							.getString("customerwarehouseid")));
			cwbOrder.setEmaildate(StringUtil.nullConvertToEmptyString(rs
					.getString("emaildate")));
			cwbOrder.setEmaildateid(rs.getLong("emaildateid"));
			cwbOrder.setServiceareaid(rs.getLong("serviceareaid"));
			cwbOrder.setCustomerid(rs.getLong("customerid"));
			cwbOrder.setShipcwb(StringUtil.nullConvertToEmptyString(rs
					.getString("shipcwb")));
			cwbOrder.setConsigneeno(StringUtil.nullConvertToEmptyString(rs
					.getString("consigneeno")));
			cwbOrder.setConsigneeaddress(StringUtil.nullConvertToEmptyString(rs
					.getString("consigneeaddress")));
			cwbOrder.setConsigneepostcode(StringUtil
					.nullConvertToEmptyString(rs.getString("consigneepostcode")));
			cwbOrder.setCwbremark(StringUtil.nullConvertToEmptyString(rs
					.getString("cwbremark")));
			cwbOrder.setCustomercommand(StringUtil.nullConvertToEmptyString(rs
					.getString("customercommand")));
			cwbOrder.setTransway(StringUtil.nullConvertToEmptyString(rs
					.getString("transway")));
			cwbOrder.setCwbprovince(StringUtil.nullConvertToEmptyString(rs
					.getString("cwbprovince")));
			cwbOrder.setCwbcity(StringUtil.nullConvertToEmptyString(rs
					.getString("cwbcity")));
			cwbOrder.setCwbcounty(StringUtil.nullConvertToEmptyString(rs
					.getString("cwbcounty")));
			cwbOrder.setReceivablefee(rs.getBigDecimal("receivablefee"));
			cwbOrder.setPaybackfee(rs.getBigDecimal("paybackfee"));
			cwbOrder.setCwb(StringUtil.nullConvertToEmptyString(rs
					.getString("cwb")));
			cwbOrder.setShipperid(rs.getLong("shipperid"));
			cwbOrder.setCwbordertypeid(rs.getInt("cwbordertypeid"));
			cwbOrder.setTranscwb(StringUtil.nullConvertToEmptyString(rs
					.getString("transcwb")));
			cwbOrder.setDestination(StringUtil.nullConvertToEmptyString(rs
					.getString("destination")));
			cwbOrder.setCwbdelivertypeid(StringUtil.nullConvertToEmptyString(rs
					.getString("cwbdelivertypeid")));
			cwbOrder.setExceldeliver(StringUtil.nullConvertToEmptyString(rs
					.getString("exceldeliver")));
			cwbOrder.setExcelbranch(StringUtil.nullConvertToEmptyString(rs
					.getString("excelbranch")));
			cwbOrder.setTimelimited(StringUtil.nullConvertToEmptyString(rs
					.getString("timelimited")));
			cwbOrder.setExcelimportuserid(rs.getLong("excelimportuserid"));
			cwbOrder.setState(rs.getLong("state"));
			cwbOrder.setPrinttime(rs.getString("printtime"));
			cwbOrder.setCommonid(rs.getLong("commonid"));
			cwbOrder.setCommoncwb(StringUtil.nullConvertToEmptyString(rs
					.getString("commoncwb")));
			cwbOrder.setSigntypeid(rs.getLong("signtypeid"));
			cwbOrder.setPodrealname(rs.getString("podrealname"));
			cwbOrder.setPodtime(rs.getString("podtime"));
			cwbOrder.setPodsignremark(rs.getString("podsignremark"));
			cwbOrder.setModelname(rs.getString("modelname"));
			cwbOrder.setScannum(rs.getLong("scannum"));
			cwbOrder.setIsaudit(rs.getLong("isaudit"));
			cwbOrder.setRemark1(StringUtil.nullConvertToEmptyString(rs
					.getString("remark1")));
			cwbOrder.setRemark2(StringUtil.nullConvertToEmptyString(rs
					.getString("remark2")));
			cwbOrder.setRemark3(StringUtil.nullConvertToEmptyString(rs
					.getString("remark3")));
			cwbOrder.setRemark4(StringUtil.nullConvertToEmptyString(rs
					.getString("remark4")));
			cwbOrder.setRemark5(StringUtil.nullConvertToEmptyString(rs
					.getString("remark5")));
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
			/*
			 * cwbOrder.setZhongzhuanreasonid(rs.getLong("zhongzhuanreasonid"));
			 * cwbOrder.setZhongzhuanreason(rs.getString("zhongzhuanreason"));
			 */
			cwbOrder.setFnorgoffset(rs.getBigDecimal("fnorgoffset"));
			cwbOrder.setFnorgoffsetflag(rs.getInt("fnorgoffsetflag"));
			// CwbDAO.this.setValueByUser(rs, cwbOrder);

			return cwbOrder;
		}

	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public long createExceedSubsidyApply(
			final ExpressSetExceedSubsidyApply exceedSubsidyApply) {
		KeyHolder key = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(
					java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement(
						"insert into express_set_exceed_subsidy_apply("
								+ "applyNo,applyState,deliveryPerson,cwbOrder,cwbOrderState,receiveAddress,"
								+ "isExceedAreaSubsidy,exceedAreaSubsidyRemark,exceedAreaSubsidyAmount,"
								+ "isBigGoodsSubsidy,bigGoodsSubsidyRemark,bigGoodsSubsidyAmount,remark,"
								+ "shenHePerson,shenHeTime,shenHeIdea,createTime,updateTime"
								+ ") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
						new String[] { "id" });
				int i = 1;

				ps.setString(i++, exceedSubsidyApply.getApplyNo());
				ps.setInt(i++, exceedSubsidyApply.getApplyState());
				ps.setInt(i++, exceedSubsidyApply.getDeliveryPerson());
				ps.setString(i++, exceedSubsidyApply.getCwbOrder());
				ps.setInt(i++, exceedSubsidyApply.getCwbOrderState());
				ps.setString(i++, exceedSubsidyApply.getReceiveAddress());
				ps.setInt(i++, exceedSubsidyApply.getIsExceedAreaSubsidy());
				ps.setString(i++, exceedSubsidyApply.getExceedAreaSubsidyRemark());
				ps.setBigDecimal(i++, exceedSubsidyApply.getExceedAreaSubsidyAmount());
				ps.setInt(i++, exceedSubsidyApply.getIsBigGoodsSubsidy());
				ps.setString(i++, exceedSubsidyApply.getBigGoodsSubsidyRemark());
				ps.setBigDecimal(i++, exceedSubsidyApply.getBigGoodsSubsidyAmount());
				ps.setString(i++, exceedSubsidyApply.getRemark());
				ps.setInt(i++, exceedSubsidyApply.getShenHePerson());
				ps.setString(i++, exceedSubsidyApply.getShenHeTime());
				ps.setString(i++, exceedSubsidyApply.getShenHeIdea());
				ps.setString(i++, exceedSubsidyApply.getCreateTime());
				ps.setString(i++, exceedSubsidyApply.getUpdateTime());

				return ps;
			}
		}, key);
		return key.getKey().longValue();
	}

	public void updateExceedSubsidyApply(
			final ExpressSetExceedSubsidyApply exceedSubsidyApply) {

		this.jdbcTemplate
				.update("update express_set_exceed_subsidy_apply set "
						+ "applyState=?,cwbOrder=?,cwbOrderState=?,receiveAddress=?,"
						+ "isExceedAreaSubsidy=?,exceedAreaSubsidyRemark=?,exceedAreaSubsidyAmount=?,"
						+ "isBigGoodsSubsidy=?,bigGoodsSubsidyRemark=?,bigGoodsSubsidyAmount=?,"
						+ "remark=?,shenHePerson=?,shenHeTime=?,shenHeIdea=?,updateTime=? where id=?",
						new PreparedStatementSetter() {
							@Override
							public void setValues(PreparedStatement ps)
									throws SQLException {
								int i = 1;

								ps.setInt(i++, exceedSubsidyApply.getApplyState());
								ps.setString(i++, exceedSubsidyApply.getCwbOrder());
								ps.setInt(i++, exceedSubsidyApply.getCwbOrderState());
								ps.setString(i++, exceedSubsidyApply.getReceiveAddress());
								ps.setInt(i++, exceedSubsidyApply.getIsExceedAreaSubsidy());
								ps.setString(i++, exceedSubsidyApply.getExceedAreaSubsidyRemark());
								ps.setBigDecimal(i++, exceedSubsidyApply.getExceedAreaSubsidyAmount());
								ps.setInt(i++, exceedSubsidyApply.getIsBigGoodsSubsidy());
								ps.setString(i++, exceedSubsidyApply.getBigGoodsSubsidyRemark());
								ps.setBigDecimal(i++, exceedSubsidyApply.getBigGoodsSubsidyAmount());
								ps.setString(i++, exceedSubsidyApply.getRemark());
								ps.setInt(i++, exceedSubsidyApply.getShenHePerson());
								ps.setString(i++, exceedSubsidyApply.getShenHeTime());
								ps.setString(i++, exceedSubsidyApply.getShenHeIdea());
								ps.setString(i++, exceedSubsidyApply.getUpdateTime());
								ps.setInt(i++, exceedSubsidyApply.getId());
							}
						});
	}

	public void updateExceedSubsidyApply(final String cwbs,
			final BigDecimal deliveryFee, final int cwbCount, final int id) {
		/*this.jdbcTemplate.update(
				"update express_set_exceed_subsidy_apply set "
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
				});*/
	}

	public int deleteExceedSubsidyApply(String ids) {
		String sql = "delete from express_set_exceed_subsidy_apply where id in ("
				+ ids + ")";
		return this.jdbcTemplate.update(sql);
	}

	public List<ExpressSetExceedSubsidyApply> getExceedSubsidyApplyList() {
		String sql = "select * from express_set_exceed_subsidy_apply";
		return jdbcTemplate.query(sql, new ExceedSubsidyApplyMapper());
	}

	public ExpressSetExceedSubsidyApply getExceedSubsidyApplyListById(int id) {
		try {
			String sql = "select * from express_set_exceed_subsidy_apply where id=?";
			return jdbcTemplate.queryForObject(sql,
					new ExceedSubsidyApplyMapper(), id);
		} catch (DataAccessException e) {
			return null;
		}
	}

	public List<ExpressSetExceedSubsidyApply> queryExceedSubsidyApply(
			long page, ExpressSetExceedSubsidyApplyVO exceedSubsidyApplyVO) {

		String sql = "select * from express_set_exceed_subsidy_apply a "
				+ " left join express_set_user u on a.deliveryPerson = u.userid "
				+ "where 1=1 ";

		if (exceedSubsidyApplyVO != null) {
			if (StringUtils.isNotBlank(exceedSubsidyApplyVO.getCwbOrder())) {
				sql += " and a.cwbOrder like '%"
						+ exceedSubsidyApplyVO.getCwbOrder() + "%' ";
			}
			if (exceedSubsidyApplyVO.getApplyState() != 0) {
				sql += " and a.applyState= '"
						+ exceedSubsidyApplyVO.getApplyState() + "' ";
			} else if(exceedSubsidyApplyVO.getIsAdvanceAuthority() == 1){
				sql += " and a.applyState!= '"
						+ ExceedSubsidyApplyStateEnum.XinJian.getValue() + "' ";
			}
			if (exceedSubsidyApplyVO.getDeliveryPerson() != 0) {
				sql += " and a.deliveryPerson= '"
						+ exceedSubsidyApplyVO.getDeliveryPerson() + "' ";
			}
			if (StringUtils.isNotBlank(exceedSubsidyApplyVO.getDeliveryPersonName())) {
				sql += " and u.realname like '%"
						+ exceedSubsidyApplyVO.getDeliveryPersonName() + "%' ";
			}
			if (StringUtils
					.isNotBlank(exceedSubsidyApplyVO.getColumn())) {
				if(exceedSubsidyApplyVO.getColumn().equalsIgnoreCase("deliveryPersonName")){
					sql += " order by u.realname";
				} else {
					sql += " order by a."
							+ exceedSubsidyApplyVO.getColumn();
				}
			}
			if (StringUtils.isNotBlank(exceedSubsidyApplyVO.getColumnOrder())) {
				sql += " " + exceedSubsidyApplyVO.getColumnOrder();
			}
		}

		if(sql.indexOf("order by") > -1) {
			sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		} else {
			sql += " order by id desc limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		}
		return jdbcTemplate.query(sql, new ExceedSubsidyApplyMapper());
	}
	
	public int queryExceedSubsidyApplyCount(
			ExpressSetExceedSubsidyApplyVO exceedSubsidyApplyVO) {
		
		String sql = "select count(1) from express_set_exceed_subsidy_apply a "
				+ " left join express_set_user u on a.deliveryPerson = u.userid "
				+ "where 1=1 ";
		
		if (exceedSubsidyApplyVO != null) {
			if (StringUtils.isNotBlank(exceedSubsidyApplyVO.getCwbOrder())) {
				sql += " and a.cwbOrder like '%"
						+ exceedSubsidyApplyVO.getCwbOrder() + "%' ";
			}
			if (exceedSubsidyApplyVO.getApplyState() != 0) {
				sql += " and a.applyState= '"
						+ exceedSubsidyApplyVO.getApplyState() + "' ";
			} else if(exceedSubsidyApplyVO.getIsAdvanceAuthority() == 1){
				sql += " and a.applyState!= '"
						+ ExceedSubsidyApplyStateEnum.XinJian.getValue() + "' ";
			}
			if (exceedSubsidyApplyVO.getDeliveryPerson() != 0) {
				sql += " and a.deliveryPerson= '"
						+ exceedSubsidyApplyVO.getDeliveryPerson() + "' ";
			}
			if (StringUtils.isNotBlank(exceedSubsidyApplyVO.getDeliveryPersonName())) {
				sql += " and u.realname like '%"
						+ exceedSubsidyApplyVO.getDeliveryPersonName() + "%' ";
			}
			if (StringUtils
					.isNotBlank(exceedSubsidyApplyVO.getColumn())) {
				if(exceedSubsidyApplyVO.getColumn().equalsIgnoreCase("deliveryPersonName")){
					sql += " order by u.realname";
				} else {
					sql += " order by a."
							+ exceedSubsidyApplyVO.getColumn();
				}
			}
			if (StringUtils.isNotBlank(exceedSubsidyApplyVO.getColumnOrder())) {
				sql += " " + exceedSubsidyApplyVO.getColumnOrder();
			}
		}
		
		return jdbcTemplate.queryForInt(sql);
	}
	
	public ExpressSetExceedSubsidyApply getMaxApplyNo(String applyNo) {
		String sql = "select * from express_set_exceed_subsidy_apply where applyNo like '%" + applyNo + "%' order by applyNo desc limit 0,1";
		try{
			return jdbcTemplate.queryForObject(sql, new ExceedSubsidyApplyMapper());
		}catch (EmptyResultDataAccessException e) {
            return null;   
        }
	}

	public BigDecimal calculateSumPrice(String punishNos) {
		String sql = "SELECT SUM(punishInsideprice)as sumprice FROM express_ops_punishInside_detail where 1=1";
		if (StringUtils.isNotBlank(punishNos)) {
			sql += " And punishNo IN(" + punishNos + ")";
		}
		return this.jdbcTemplate.queryForObject(sql, BigDecimal.class);
	}

	public List<ExpressSetExceedSubsidyApply> getExceedSubsidyApplyByChukuDate(
			String begindate, String enddate, long page) {
		String sql = "select * from express_ops_bale where cretime>=? and cretime<=? ";
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ,"
				+ Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new ExceedSubsidyApplyMapper(),
				begindate, enddate);
	}

	public long getExceedSubsidyApplyByChukuDateCount(String begindate,
			String enddate) {
		String sql = "select count(1) from express_ops_bale where cretime>=? and cretime<=? ";
		return jdbcTemplate.queryForLong(sql, begindate, enddate);
	}

	public ExpressSetExceedSubsidyApply getExceedSubsidyApplyOneByExceedSubsidyApplynoLock(
			String baleno) {
		try {
			String sql = "select * from express_ops_bale where baleno=? for update";
			return jdbcTemplate.queryForObject(sql,
					new ExceedSubsidyApplyMapper(), baleno);
		} catch (DataAccessException e) {
			return null;
		}
	}

}
