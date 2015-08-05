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
import cn.explink.domain.ExpressSetBranchDeliveryFeeBill;
import cn.explink.domain.ExpressSetBranchDeliveryFeeBillDetail;
import cn.explink.domain.VO.ExpressSetBranchDeliveryFeeBillDetailVO;
import cn.explink.domain.VO.ExpressSetBranchDeliveryFeeBillVO;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Component
public class BranchDeliveryFeeBillDAO {

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
	
	private final class BranchDeliveryFeeBillDetailMapper implements
	RowMapper<ExpressSetBranchDeliveryFeeBillDetail> {
		@Override
		public ExpressSetBranchDeliveryFeeBillDetail mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			ExpressSetBranchDeliveryFeeBillDetail branchDeliveryFeeBillDetail = new ExpressSetBranchDeliveryFeeBillDetail();
			branchDeliveryFeeBillDetail.setId(rs.getInt("id"));
			branchDeliveryFeeBillDetail.setBillId(rs.getInt("billId"));
			branchDeliveryFeeBillDetail.setCwb(rs.getString("cwb"));
			branchDeliveryFeeBillDetail.setFlowordertype(rs.getInt("flowordertype"));
			branchDeliveryFeeBillDetail.setCwbordertypeid(rs.getString("cwbordertypeid"));
			branchDeliveryFeeBillDetail.setCustomerid(rs.getInt("customerid"));
			branchDeliveryFeeBillDetail.setIsReceived(rs.getInt("isReceived"));
			branchDeliveryFeeBillDetail.setReceivablefee(rs.getBigDecimal("receivablefee"));
			branchDeliveryFeeBillDetail.setEmaildate(rs.getString("emaildate"));
			branchDeliveryFeeBillDetail.setNewpaywayid(rs.getString("newpaywayid"));
			branchDeliveryFeeBillDetail.setPodtime(rs.getString("podtime"));
			branchDeliveryFeeBillDetail.setSumFee(rs.getBigDecimal("sumFee"));
			branchDeliveryFeeBillDetail.setBasicFee(rs.getBigDecimal("basicFee"));
			branchDeliveryFeeBillDetail.setCollectionSubsidyFee(rs.getBigDecimal("collectionSubsidyFee"));
			branchDeliveryFeeBillDetail.setAreaSubsidyFee(rs.getBigDecimal("areaSubsidyFee"));
			branchDeliveryFeeBillDetail.setExceedSubsidyFee(rs.getBigDecimal("exceedSubsidyFee"));
			branchDeliveryFeeBillDetail.setBusinessSubsidyFee(rs.getBigDecimal("businessSubsidyFee"));
			branchDeliveryFeeBillDetail.setAttachSubsidyFee(rs.getBigDecimal("attachSubsidyFee"));
			branchDeliveryFeeBillDetail.setDeliverySumFee(rs.getBigDecimal("deliverySumFee"));
			branchDeliveryFeeBillDetail.setDeliveryBasicFee(rs.getBigDecimal("deliveryBasicFee"));
			branchDeliveryFeeBillDetail.setDeliveryCollectionSubsidyFee(rs.getBigDecimal("deliveryCollectionSubsidyFee"));
			branchDeliveryFeeBillDetail.setDeliveryAreaSubsidyFee(rs.getBigDecimal("deliveryAreaSubsidyFee"));
			branchDeliveryFeeBillDetail.setDeliveryExceedSubsidyFee(rs.getBigDecimal("deliveryExceedSubsidyFee"));
			branchDeliveryFeeBillDetail.setDeliveryBusinessSubsidyFee(rs.getBigDecimal("deliveryBusinessSubsidyFee"));
			branchDeliveryFeeBillDetail.setDeliveryAttachSubsidyFee(rs.getBigDecimal("deliveryAttachSubsidyFee"));
			branchDeliveryFeeBillDetail.setPickupSumFee(rs.getBigDecimal("pickupSumFee"));
			branchDeliveryFeeBillDetail.setPickupCollectionSubsidyFee(rs.getBigDecimal("pickupCollectionSubsidyFee"));
			branchDeliveryFeeBillDetail.setPickupAreaSubsidyFee(rs.getBigDecimal("pickupAreaSubsidyFee"));
			branchDeliveryFeeBillDetail.setPickupExceedSubsidyFee(rs.getBigDecimal("pickupExceedSubsidyFee"));
			branchDeliveryFeeBillDetail.setPickupAttachSubsidyFee(rs.getBigDecimal("pickupAttachSubsidyFee"));
			branchDeliveryFeeBillDetail.setPickupBasicFee(rs.getBigDecimal("pickupBasicFee"));
			branchDeliveryFeeBillDetail.setPickupBusinessSubsidyFee(rs.getBigDecimal("pickupBusinessSubsidyFee"));
			
			return branchDeliveryFeeBillDetail;
		}
	}
	
	private final class BranchDeliveryFeeBillDetailVOMapper implements
	RowMapper<ExpressSetBranchDeliveryFeeBillDetailVO> {
		@Override
		public ExpressSetBranchDeliveryFeeBillDetailVO mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			ExpressSetBranchDeliveryFeeBillDetailVO branchDeliveryFeeBillDetailVO = new ExpressSetBranchDeliveryFeeBillDetailVO();
			branchDeliveryFeeBillDetailVO.setId(rs.getInt("id"));
			branchDeliveryFeeBillDetailVO.setBillId(rs.getInt("billId"));
			branchDeliveryFeeBillDetailVO.setCwb(rs.getString("cwb"));
			branchDeliveryFeeBillDetailVO.setFlowordertype(rs.getInt("flowordertype"));
			branchDeliveryFeeBillDetailVO.setCwbordertypeid(rs.getString("cwbordertypeid"));
			branchDeliveryFeeBillDetailVO.setCustomerid(rs.getInt("customerid"));
			branchDeliveryFeeBillDetailVO.setIsReceived(rs.getInt("isReceived"));
			branchDeliveryFeeBillDetailVO.setReceivablefee(rs.getBigDecimal("receivablefee"));
			branchDeliveryFeeBillDetailVO.setEmaildate(rs.getString("emaildate"));
			branchDeliveryFeeBillDetailVO.setNewpaywayid(rs.getString("newpaywayid"));
			branchDeliveryFeeBillDetailVO.setPodtime(rs.getString("podtime"));
			branchDeliveryFeeBillDetailVO.setSumFee(rs.getBigDecimal("sumFee"));
			branchDeliveryFeeBillDetailVO.setBasicFee(rs.getBigDecimal("basicFee"));
			branchDeliveryFeeBillDetailVO.setCollectionSubsidyFee(rs.getBigDecimal("collectionSubsidyFee"));
			branchDeliveryFeeBillDetailVO.setAreaSubsidyFee(rs.getBigDecimal("areaSubsidyFee"));
			branchDeliveryFeeBillDetailVO.setExceedSubsidyFee(rs.getBigDecimal("exceedSubsidyFee"));
			branchDeliveryFeeBillDetailVO.setBusinessSubsidyFee(rs.getBigDecimal("businessSubsidyFee"));
			branchDeliveryFeeBillDetailVO.setAttachSubsidyFee(rs.getBigDecimal("attachSubsidyFee"));
			branchDeliveryFeeBillDetailVO.setDeliverySumFee(rs.getBigDecimal("deliverySumFee"));
			branchDeliveryFeeBillDetailVO.setDeliveryBasicFee(rs.getBigDecimal("deliveryBasicFee"));
			branchDeliveryFeeBillDetailVO.setDeliveryCollectionSubsidyFee(rs.getBigDecimal("deliveryCollectionSubsidyFee"));
			branchDeliveryFeeBillDetailVO.setDeliveryAreaSubsidyFee(rs.getBigDecimal("deliveryAreaSubsidyFee"));
			branchDeliveryFeeBillDetailVO.setDeliveryExceedSubsidyFee(rs.getBigDecimal("deliveryExceedSubsidyFee"));
			branchDeliveryFeeBillDetailVO.setDeliveryBusinessSubsidyFee(rs.getBigDecimal("deliveryBusinessSubsidyFee"));
			branchDeliveryFeeBillDetailVO.setDeliveryAttachSubsidyFee(rs.getBigDecimal("deliveryAttachSubsidyFee"));
			branchDeliveryFeeBillDetailVO.setPickupSumFee(rs.getBigDecimal("pickupSumFee"));
			branchDeliveryFeeBillDetailVO.setPickupCollectionSubsidyFee(rs.getBigDecimal("pickupCollectionSubsidyFee"));
			branchDeliveryFeeBillDetailVO.setPickupAreaSubsidyFee(rs.getBigDecimal("pickupAreaSubsidyFee"));
			branchDeliveryFeeBillDetailVO.setPickupExceedSubsidyFee(rs.getBigDecimal("pickupExceedSubsidyFee"));
			branchDeliveryFeeBillDetailVO.setPickupAttachSubsidyFee(rs.getBigDecimal("pickupAttachSubsidyFee"));
			branchDeliveryFeeBillDetailVO.setPickupBasicFee(rs.getBigDecimal("pickupBasicFee"));
			branchDeliveryFeeBillDetailVO.setPickupBusinessSubsidyFee(rs.getBigDecimal("pickupBusinessSubsidyFee"));
			branchDeliveryFeeBillDetailVO.setCwbOrderCount(rs.getInt("cwbOrderCount"));
			
			return branchDeliveryFeeBillDetailVO;
		}
	}
	
	private final class BranchDeliveryFeeBillDetailVOListMapper implements
	RowMapper<ExpressSetBranchDeliveryFeeBillDetailVO> {
		@Override
		public ExpressSetBranchDeliveryFeeBillDetailVO mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			ExpressSetBranchDeliveryFeeBillDetailVO branchDeliveryFeeBillDetailVO = new ExpressSetBranchDeliveryFeeBillDetailVO();
			branchDeliveryFeeBillDetailVO.setCustomerid(rs.getInt("customerid"));
			branchDeliveryFeeBillDetailVO.setIsReceived(rs.getInt("isReceived"));
			branchDeliveryFeeBillDetailVO.setCwbOrderCount(rs.getInt("cwbOrderCount"));
			branchDeliveryFeeBillDetailVO.setDeliveryBasicFee(rs.getBigDecimal("deliveryBasicFee"));
			branchDeliveryFeeBillDetailVO.setDeliveryCollectionSubsidyFee(rs.getBigDecimal("deliveryCollectionSubsidyFee"));
			branchDeliveryFeeBillDetailVO.setDeliveryAreaSubsidyFee(rs.getBigDecimal("deliveryAreaSubsidyFee"));
			branchDeliveryFeeBillDetailVO.setDeliveryExceedSubsidyFee(rs.getBigDecimal("deliveryExceedSubsidyFee"));
			branchDeliveryFeeBillDetailVO.setDeliveryBusinessSubsidyFee(rs.getBigDecimal("deliveryBusinessSubsidyFee"));
			branchDeliveryFeeBillDetailVO.setDeliveryAttachSubsidyFee(rs.getBigDecimal("deliveryAttachSubsidyFee"));
			branchDeliveryFeeBillDetailVO.setDeliverySumFee(rs.getBigDecimal("deliverySumFee"));
			
			return branchDeliveryFeeBillDetailVO;
		}
	}
	
	private final class DeliveryFeeMapper implements
	RowMapper<ExpressSetBranchDeliveryFeeBillDetailVO> {
		@Override
		public ExpressSetBranchDeliveryFeeBillDetailVO mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			ExpressSetBranchDeliveryFeeBillDetailVO branchDeliveryFeeBillDetailVO = new ExpressSetBranchDeliveryFeeBillDetailVO();
			branchDeliveryFeeBillDetailVO.setCwbOrderCount(rs.getInt("cwbOrderCount"));
			branchDeliveryFeeBillDetailVO.setDeliveryBasicFee(rs.getBigDecimal("deliveryBasicFee"));
			branchDeliveryFeeBillDetailVO.setDeliveryCollectionSubsidyFee(rs.getBigDecimal("deliveryCollectionSubsidyFee"));
			branchDeliveryFeeBillDetailVO.setDeliveryAreaSubsidyFee(rs.getBigDecimal("deliveryAreaSubsidyFee"));
			branchDeliveryFeeBillDetailVO.setDeliveryExceedSubsidyFee(rs.getBigDecimal("deliveryExceedSubsidyFee"));
			branchDeliveryFeeBillDetailVO.setDeliveryBusinessSubsidyFee(rs.getBigDecimal("deliveryBusinessSubsidyFee"));
			branchDeliveryFeeBillDetailVO.setDeliveryAttachSubsidyFee(rs.getBigDecimal("deliveryAttachSubsidyFee"));
			branchDeliveryFeeBillDetailVO.setDeliverySumFee(rs.getBigDecimal("deliverySumFee"));
			
			return branchDeliveryFeeBillDetailVO;
		}
	}
	
	private final class PickupFeeMapper implements
	RowMapper<ExpressSetBranchDeliveryFeeBillDetailVO> {
		@Override
		public ExpressSetBranchDeliveryFeeBillDetailVO mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			ExpressSetBranchDeliveryFeeBillDetailVO branchDeliveryFeeBillDetailVO = new ExpressSetBranchDeliveryFeeBillDetailVO();
			branchDeliveryFeeBillDetailVO.setCwbOrderCount(rs.getInt("cwbOrderCount"));
			branchDeliveryFeeBillDetailVO.setPickupSumFee(rs.getBigDecimal("pickupSumFee"));
			branchDeliveryFeeBillDetailVO.setPickupCollectionSubsidyFee(rs.getBigDecimal("pickupCollectionSubsidyFee"));
			branchDeliveryFeeBillDetailVO.setPickupAreaSubsidyFee(rs.getBigDecimal("pickupAreaSubsidyFee"));
			branchDeliveryFeeBillDetailVO.setPickupExceedSubsidyFee(rs.getBigDecimal("pickupExceedSubsidyFee"));
			branchDeliveryFeeBillDetailVO.setPickupAttachSubsidyFee(rs.getBigDecimal("pickupAttachSubsidyFee"));
			branchDeliveryFeeBillDetailVO.setPickupBasicFee(rs.getBigDecimal("pickupBasicFee"));
			branchDeliveryFeeBillDetailVO.setPickupBusinessSubsidyFee(rs.getBigDecimal("pickupBusinessSubsidyFee"));
			
			return branchDeliveryFeeBillDetailVO;
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
	
	public long createBranchDeliveryFeeBillDetail(
			final ExpressSetBranchDeliveryFeeBillDetail branchDeliveryFeeBillDetail) {
		KeyHolder key = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(
					java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement(
						"insert into express_set_branch_delivery_fee_bill_detail("
								+ "billId,cwb,flowordertype,cwbordertypeid,customerid,isReceived,receivablefee,emaildate,"
								+ "newpaywayid,podtime,sumFee,basicFee,collectionSubsidyFee,areaSubsidyFee,"
								+ "exceedSubsidyFee,businessSubsidyFee,attachSubsidyFee,deliverySumFee,"
								+ "deliveryBasicFee,deliveryCollectionSubsidyFee,deliveryAreaSubsidyFee,"
								+ "deliveryExceedSubsidyFee,deliveryBusinessSubsidyFee,deliveryAttachSubsidyFee,"
								+ "pickupSumFee,pickupCollectionSubsidyFee,pickupAreaSubsidyFee,pickupExceedSubsidyFee,"
								+ "pickupAttachSubsidyFee,pickupBasicFee,pickupBusinessSubsidyFee"
								+ ") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
								new String[] { "id" });
				int i = 1;
				
				ps.setInt(i++, branchDeliveryFeeBillDetail.getBillId());
				ps.setString(i++, branchDeliveryFeeBillDetail.getCwb());
				ps.setInt(i++, branchDeliveryFeeBillDetail.getFlowordertype());
				ps.setString(i++, branchDeliveryFeeBillDetail.getCwbordertypeid());
				ps.setInt(i++, branchDeliveryFeeBillDetail.getCustomerid());
				ps.setInt(i++, branchDeliveryFeeBillDetail.getIsReceived());
				ps.setBigDecimal(i++, branchDeliveryFeeBillDetail.getReceivablefee());
				ps.setString(i++, branchDeliveryFeeBillDetail.getEmaildate());
				ps.setString(i++, branchDeliveryFeeBillDetail.getNewpaywayid());
				ps.setString(i++, branchDeliveryFeeBillDetail.getPodtime());
				ps.setBigDecimal(i++, branchDeliveryFeeBillDetail.getSumFee());
				ps.setBigDecimal(i++, branchDeliveryFeeBillDetail.getBasicFee());
				ps.setBigDecimal(i++, branchDeliveryFeeBillDetail.getCollectionSubsidyFee());
				ps.setBigDecimal(i++, branchDeliveryFeeBillDetail.getAreaSubsidyFee());
				ps.setBigDecimal(i++, branchDeliveryFeeBillDetail.getExceedSubsidyFee());
				ps.setBigDecimal(i++, branchDeliveryFeeBillDetail.getBusinessSubsidyFee());
				ps.setBigDecimal(i++, branchDeliveryFeeBillDetail.getAttachSubsidyFee());
				ps.setBigDecimal(i++, branchDeliveryFeeBillDetail.getDeliverySumFee());
				ps.setBigDecimal(i++, branchDeliveryFeeBillDetail.getDeliveryBasicFee());
				ps.setBigDecimal(i++, branchDeliveryFeeBillDetail.getDeliveryCollectionSubsidyFee());
				ps.setBigDecimal(i++, branchDeliveryFeeBillDetail.getDeliveryAreaSubsidyFee());
				ps.setBigDecimal(i++, branchDeliveryFeeBillDetail.getDeliveryExceedSubsidyFee());
				ps.setBigDecimal(i++, branchDeliveryFeeBillDetail.getDeliveryBusinessSubsidyFee());
				ps.setBigDecimal(i++, branchDeliveryFeeBillDetail.getDeliveryAttachSubsidyFee());
				ps.setBigDecimal(i++, branchDeliveryFeeBillDetail.getPickupSumFee());
				ps.setBigDecimal(i++, branchDeliveryFeeBillDetail.getPickupCollectionSubsidyFee());
				ps.setBigDecimal(i++, branchDeliveryFeeBillDetail.getPickupAreaSubsidyFee());
				ps.setBigDecimal(i++, branchDeliveryFeeBillDetail.getPickupExceedSubsidyFee());
				ps.setBigDecimal(i++, branchDeliveryFeeBillDetail.getPickupAttachSubsidyFee());
				ps.setBigDecimal(i++, branchDeliveryFeeBillDetail.getPickupBasicFee());
				ps.setBigDecimal(i++, branchDeliveryFeeBillDetail.getBusinessSubsidyFee());
				
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

	public int deleteBranchDeliveryFeeBill(String ids) {
		String sql = "delete from express_set_branch_delivery_fee_bill where id in ("
				+ ids + ")";
		return this.jdbcTemplate.update(sql);
	}
	
	public int deleteBranchDeliveryFeeBillDetail(int billId, String cwbs) {
		String sql = "delete from express_set_branch_delivery_fee_bill_detail where billId= " + billId;
		if(StringUtils.isNotBlank(cwbs)){
			sql += " and cwb not in (" + cwbs + ")";
		}
		return this.jdbcTemplate.update(sql);
	}
	
	public int deleteBranchDeliveryFeeBillDetailByBillIds(String billIds) {
		String sql = "delete from express_set_branch_delivery_fee_bill_detail where billId in (" + billIds + ")";
		return this.jdbcTemplate.update(sql);
	}

	public List<ExpressSetBranchDeliveryFeeBill> getBranchDeliveryFeeBillList() {
		String sql = "select * from express_set_branch_delivery_fee_bill";
		return jdbcTemplate.query(sql, new BranchDeliveryFeeBillMapper());
	}
	
	public List<ExpressSetBranchDeliveryFeeBillDetail> getBranchDeliveryFeeBillDetailList(int billId) {
		String sql = "select * from express_set_branch_delivery_fee_bill_detail where billId=?";
		return jdbcTemplate.query(sql, new BranchDeliveryFeeBillDetailMapper(), billId);
	}
	
	public List<ExpressSetBranchDeliveryFeeBillDetail> getBranchDeliveryFeeBillDetailListByBillIds(String billIds) {
		String sql = "select * from express_set_branch_delivery_fee_bill_detail where billId in (" + billIds + ")";
		return jdbcTemplate.query(sql, new BranchDeliveryFeeBillDetailMapper());
	}
	
	public List<ExpressSetBranchDeliveryFeeBillDetail> getBranchDeliveryFeeBillDetailList() {
		String sql = "select * from express_set_branch_delivery_fee_bill_detail ";
		return jdbcTemplate.query(sql, new BranchDeliveryFeeBillDetailMapper());
	}
	
	public List<ExpressSetBranchDeliveryFeeBillDetail> getBranchDeliveryFeeBillDetailList(String cwbs) {
		String sql = "select * from express_set_branch_delivery_fee_bill_detail where cwb in (" + cwbs + ")";
		return jdbcTemplate.query(sql, new BranchDeliveryFeeBillDetailMapper());
	}
	
	public List<ExpressSetBranchDeliveryFeeBillDetailVO> getBranchDeliveryFeeBillDetailVOList(String cwbs) {
		String sql = "select customerid,isReceived,count(*) as cwbOrderCount,sum(deliveryBasicFee) as deliveryBasicFee,"
				+ "sum(deliveryCollectionSubsidyFee) as deliveryCollectionSubsidyFee,sum(deliveryAreaSubsidyFee) as deliveryAreaSubsidyFee,"
				+ "sum(deliveryExceedSubsidyFee) as deliveryExceedSubsidyFee,sum(deliveryBusinessSubsidyFee) as deliveryBusinessSubsidyFee,"
				+ "sum(deliveryAttachSubsidyFee) as deliveryAttachSubsidyFee,sum(deliverySumFee) as deliverySumFee "
				+ " from express_set_branch_delivery_fee_bill_detail where deliverySumFee != 0.00 and cwb in (" + cwbs + ") group by customerid,isReceived";
		return jdbcTemplate.query(sql, new BranchDeliveryFeeBillDetailVOListMapper());
	}
	
	public ExpressSetBranchDeliveryFeeBillDetailVO getDeliveryFee(String cwbs) {
		String sql = "select count(*) as cwbOrderCount,sum(deliveryBasicFee) as deliveryBasicFee,sum(deliveryCollectionSubsidyFee) as deliveryCollectionSubsidyFee,"
				+ "sum(deliveryAreaSubsidyFee) as deliveryAreaSubsidyFee,sum(deliveryExceedSubsidyFee) as deliveryExceedSubsidyFee,"
				+ "sum(deliveryBusinessSubsidyFee) as deliveryBusinessSubsidyFee,sum(deliveryAttachSubsidyFee) as deliveryAttachSubsidyFee,"
				+ "sum(deliverySumFee) as deliverySumFee "
				+ " from express_set_branch_delivery_fee_bill_detail where deliverySumFee != 0.00 and cwb in (" + cwbs + ") group by billId";
		return jdbcTemplate.queryForObject(sql, new DeliveryFeeMapper());
	}
	
	public ExpressSetBranchDeliveryFeeBillDetailVO getPickupFee(String cwbs) {
		String sql = "select count(*) as cwbOrderCount,sum(pickupBasicFee) as pickupBasicFee,sum(pickupCollectionSubsidyFee) as pickupCollectionSubsidyFee,"
				+ "sum(pickupAreaSubsidyFee) as pickupAreaSubsidyFee,sum(pickupExceedSubsidyFee) as pickupExceedSubsidyFee,"
				+ "sum(pickupBusinessSubsidyFee) as pickupBusinessSubsidyFee,sum(pickupAttachSubsidyFee) as pickupAttachSubsidyFee,"
				+ "sum(pickupSumFee) as pickupSumFee "
				+ " from express_set_branch_delivery_fee_bill_detail where pickupSumFee != 0.00 and cwb in (" + cwbs + ") group by billId";
		return jdbcTemplate.queryForObject(sql, new PickupFeeMapper());
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
			long page, ExpressSetBranchDeliveryFeeBillVO branchDeliveryFeeBillVO) {

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
				sql += " and DATE_FORMAT(pb.createDate,'%Y-%m-%d %H:%i:%s') >= DATE_FORMAT('"
						+ branchDeliveryFeeBillVO.getCreateDateFrom() + "','%Y-%m-%d %H:%i:%s')";
			}
			if (StringUtils.isNotBlank(branchDeliveryFeeBillVO
					.getCreateDateTo())) {
				sql += " and DATE_FORMAT(pb.createDate,'%Y-%m-%d %H:%i:%s') <= DATE_ADD(DATE_FORMAT('"
						+ branchDeliveryFeeBillVO.getCreateDateTo() + "','%Y-%m-%d %H:%i:%s'),INTERVAL 1 DAY)";
			}
			if (StringUtils.isNotBlank(branchDeliveryFeeBillVO
					.getHeXiaoDateFrom())) {
				sql += " and DATE_FORMAT(pb.heXiaoDate,'%Y-%m-%d %H:%i:%s') >= DATE_FORMAT('"
						+ branchDeliveryFeeBillVO.getHeXiaoDateFrom() + "','%Y-%m-%d %H:%i:%s')";
			}
			if (StringUtils.isNotBlank(branchDeliveryFeeBillVO
					.getHeXiaoDateTo())) {
				sql += " and DATE_FORMAT(pb.heXiaoDate,'%Y-%m-%d %H:%i:%s') <= DATE_ADD(DATE_FORMAT('"
						+ branchDeliveryFeeBillVO.getHeXiaoDateTo() + "','%Y-%m-%d %H:%i:%s'),INTERVAL 1 DAY)";
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
				if("branchName".equalsIgnoreCase(branchDeliveryFeeBillVO.getContractColumn())){
					sql += " order by b.branchname";
				} else {
					sql += " order by pb."
							+ branchDeliveryFeeBillVO.getContractColumn();
				}
			}
			if (StringUtils.isNotBlank(branchDeliveryFeeBillVO
					.getContractColumnOrder())) {
				sql += " " + branchDeliveryFeeBillVO.getContractColumnOrder();
			}
		}

		if(sql.indexOf("order by") > -1) {
			sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		} else {
			sql += " order by id desc limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
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
				sql += " and DATE_FORMAT(pb.createDate,'%Y-%m-%d %H:%i:%s') >= DATE_FORMAT('"
						+ branchDeliveryFeeBillVO.getCreateDateFrom() + "','%Y-%m-%d %H:%i:%s')";
			}
			if (StringUtils.isNotBlank(branchDeliveryFeeBillVO
					.getCreateDateTo())) {
				sql += " and DATE_FORMAT(pb.createDate,'%Y-%m-%d %H:%i:%s') <= DATE_ADD(DATE_FORMAT('"
						+ branchDeliveryFeeBillVO.getCreateDateTo() + "','%Y-%m-%d %H:%i:%s'),INTERVAL 1 DAY)";
			}
			if (StringUtils.isNotBlank(branchDeliveryFeeBillVO
					.getHeXiaoDateFrom())) {
				sql += " and DATE_FORMAT(pb.heXiaoDate,'%Y-%m-%d %H:%i:%s') >= DATE_FORMAT('"
						+ branchDeliveryFeeBillVO.getHeXiaoDateFrom() + "','%Y-%m-%d %H:%i:%s')";
			}
			if (StringUtils.isNotBlank(branchDeliveryFeeBillVO
					.getHeXiaoDateTo())) {
				sql += " and DATE_FORMAT(pb.heXiaoDate,'%Y-%m-%d %H:%i:%s') <= DATE_ADD(DATE_FORMAT('"
						+ branchDeliveryFeeBillVO.getHeXiaoDateTo() + "','%Y-%m-%d %H:%i:%s'),INTERVAL 1 DAY)";
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
				if("branchName".equalsIgnoreCase(branchDeliveryFeeBillVO.getContractColumn())){
					sql += " order by b.branchname";
				} else {
					sql += " order by pb."
							+ branchDeliveryFeeBillVO.getContractColumn();
				}
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
			String onSql, String dateColumn, String cwbs) {

		String sql = "select distinct cwb.* from express_ops_cwb_detail cwb "
				+ " left join express_ops_delivery_state d "
				+ " on cwb.cwb = d.cwb "	
				+ leftJoinSql
				+ onSql
				+ " where branchfeebillexportflag=0 ";

		if (branchDeliveryFeeBill != null) {
			if (StringUtils.isNotBlank(branchDeliveryFeeBill
					.getBeginDate())) {
				sql += " and DATE_FORMAT(" + dateColumn + ",'%Y-%m-%d %H:%i:%s') >= DATE_FORMAT('"
						+ branchDeliveryFeeBill.getBeginDate() + "','%Y-%m-%d %H:%i:%s') ";
			}
			if (StringUtils.isNotBlank(branchDeliveryFeeBill
					.getEndDate())) {
				sql += " and DATE_FORMAT(" + dateColumn + ",'%Y-%m-%d %H:%i:%s') <= DATE_ADD(DATE_FORMAT('"
						+ branchDeliveryFeeBill.getEndDate() + "','%Y-%m-%d %H:%i:%s'),INTERVAL 1 DAY) ";
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
		if(StringUtils.isNotBlank(cwbs)){
			sql += " and cwb.cwb not in (" + cwbs + ")";
		}

		return jdbcTemplate.query(sql, new CwbMapper());
	}

	public ExpressSetBranchDeliveryFeeBill getMaxBillBatch(String billBatch) {
		String sql = "select * from express_set_branch_delivery_fee_bill where billBatch like '%" + billBatch + "%' order by billBatch desc limit 0,1";
		try{
			return jdbcTemplate.queryForObject(sql, new BranchDeliveryFeeBillMapper());
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

	public void updateCwbOrder(int branchfeebillexportflag, String cwbs) {
		String cwbsql = "update express_ops_cwb_detail set branchfeebillexportflag=" + branchfeebillexportflag
				+ " where cwb in (" + cwbs + ")";
		this.jdbcTemplate.update(cwbsql);
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
