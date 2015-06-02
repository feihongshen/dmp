package cn.explink.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Component;

import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.MatchExceptionOrder;
import cn.explink.domain.SmtOrder;
import cn.explink.domain.Smtcount;
import cn.explink.domain.User;
import cn.explink.domain.addressvo.DelivererVo;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.CwbFlowOrderTypeEnum;
import cn.explink.enumutil.CwbOrderAddressCodeEditTypeEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.CwbStateEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.ReturnCwbsTypeEnum;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Component
public class CwbDAO {
	private Logger logger = LoggerFactory.getLogger(CwbDAO.class);
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

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

	private User getUser() {
		return CwbDAO.this.getSessionUser() == null ? new User() : CwbDAO.this.getSessionUser();
	}

	private void setValueByUser(ResultSet rs, CwbOrder cwbOrder) throws SQLException {
		if (CwbDAO.this.getUser().getShownameflag() != 1) {
			cwbOrder.setConsigneename("******");
		} else {
			cwbOrder.setConsigneename(StringUtil.nullConvertToEmptyString(rs.getString("consigneename")));
		}
		if (CwbDAO.this.getUser().getShowphoneflag() != 1) {
			cwbOrder.setConsigneephone("******");
		} else {
			cwbOrder.setConsigneephone(StringUtil.nullConvertToEmptyString(rs.getString("consigneephone")));
		}
		if (CwbDAO.this.getUser().getShowmobileflag() != 1) {
			cwbOrder.setConsigneemobile("******");
		} else {
			cwbOrder.setConsigneemobile(StringUtil.nullConvertToEmptyString(rs.getString("consigneemobile")));
		}
		cwbOrder.setConsigneemobileOfkf(StringUtil.nullConvertToEmptyString(rs.getString("consigneemobile")));
		cwbOrder.setConsigneenameOfkf(StringUtil.nullConvertToEmptyString(rs.getString("consigneename")));
		cwbOrder.setConsigneephoneOfkf(StringUtil.nullConvertToEmptyString(rs.getString("consigneephone")));

	}

	private void setValueByUser(ResultSet rs, JSONObject obj) throws SQLException {
		if (CwbDAO.this.getUser().getShownameflag() != 1) {
			obj.put("consigneename", "******");
		} else {
			obj.put("consigneename", StringUtil.nullConvertToEmptyString(rs.getString("consigneename")));
		}
		if (CwbDAO.this.getUser().getShowphoneflag() != 1) {
			obj.put("consigneephone", "******");
		} else {
			obj.put("consigneephone", StringUtil.nullConvertToEmptyString(rs.getString("consigneephone")));
		}
		if (CwbDAO.this.getUser().getShowmobileflag() != 1) {
			obj.put("consigneemobile", "******");
		} else {
			obj.put("consigneemobile", StringUtil.nullConvertToEmptyString(rs.getString("consigneemobile")));
		}
		obj.put("consigneemobileOfkf", StringUtil.nullConvertToEmptyString(rs.getString("consigneemobile")));
		obj.put("consigneenameOfkf", StringUtil.nullConvertToEmptyString(rs.getString("consigneename")));
		obj.put("consigneephoneOfkf", StringUtil.nullConvertToEmptyString(rs.getString("consigneephone")));
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
			cwbOrder.setZhongzhuanreasonid(rs.getLong("zhongzhuanreasonid"));
			cwbOrder.setZhongzhuanreason(rs.getString("zhongzhuanreason"));
			cwbOrder.setFnorgoffset(rs.getBigDecimal("fnorgoffset"));
			cwbOrder.setFnorgoffsetflag(rs.getInt("fnorgoffsetflag"));
			CwbDAO.this.setValueByUser(rs, cwbOrder);

			return cwbOrder;
		}

	}

	private final class CwbPayMapper implements RowMapper<JSONObject> {

		@Override
		public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
			JSONObject obj = new JSONObject();
			obj.put("opscwbid", rs.getLong("opscwbid"));
			obj.put("startbranchid", rs.getLong("startbranchid"));
			obj.put("nextbranchid", rs.getLong("nextbranchid"));
			obj.put("backtocustomer_awb", StringUtil.nullConvertToEmptyString(rs.getString("backtocustomer_awb")));
			obj.put("cwbflowflag", StringUtil.nullConvertToEmptyString(rs.getString("cwbflowflag")));
			obj.put("carrealweight", rs.getBigDecimal("carrealweight"));
			obj.put("cartype", StringUtil.nullConvertToEmptyString(rs.getString("cartype")));
			obj.put("carwarehouse", StringUtil.nullConvertToEmptyString(rs.getString("carwarehouse")));
			obj.put("carsize", StringUtil.nullConvertToEmptyString(rs.getString("carsize")));
			obj.put("backcaramount", rs.getBigDecimal("backcaramount"));
			obj.put("sendcarnum", rs.getLong("sendcarnum"));
			obj.put("backcarnum", rs.getLong("backcarnum"));
			obj.put("caramount", rs.getBigDecimal("caramount"));
			obj.put("backcarname", StringUtil.nullConvertToEmptyString(rs.getString("backcarname")));
			obj.put("sendcarname", StringUtil.nullConvertToEmptyString(rs.getString("sendcarname")));
			obj.put("deliverid", rs.getLong("deliverid"));
			obj.put("emailfinishflag", rs.getInt("emailfinishflag"));
			obj.put("reacherrorflag", rs.getInt("reacherrorflag"));
			obj.put("orderflowid", rs.getLong("orderflowid"));
			obj.put("flowordertype", rs.getLong("flowordertype"));
			obj.put("cwbreachbranchid", rs.getLong("cwbreachbranchid"));
			obj.put("cwbreachdeliverbranchid", rs.getLong("cwbreachdeliverbranchid"));
			obj.put("podfeetoheadflag", StringUtil.nullConvertToEmptyString(rs.getString("podfeetoheadflag")));
			obj.put("podfeetoheadcheckflag", StringUtil.nullConvertToEmptyString(rs.getString("podfeetoheadcheckflag")));
			obj.put("leavedreasonid", rs.getLong("leavedreasonid"));
			obj.put("customerwarehouseid", StringUtil.nullConvertToEmptyString(rs.getString("customerwarehouseid")));
			obj.put("emaildate", StringUtil.nullConvertToEmptyString(rs.getString("emaildate")));
			obj.put("emaildateid", rs.getLong("emaildateid"));
			obj.put("serviceareaid", rs.getLong("serviceareaid"));
			obj.put("customerid", rs.getLong("customerid"));
			obj.put("shipcwb", StringUtil.nullConvertToEmptyString(rs.getString("shipcwb")));
			obj.put("consigneeno", StringUtil.nullConvertToEmptyString(rs.getString("consigneeno")));

			CwbDAO.this.setValueByUser(rs, obj);
			obj.put("consigneeaddress", StringUtil.nullConvertToEmptyString(rs.getString("consigneeaddress")));
			obj.put("consigneepostcode", StringUtil.nullConvertToEmptyString(rs.getString("consigneepostcode")));
			obj.put("cwbremark", StringUtil.nullConvertToEmptyString(rs.getString("cwbremark")));
			obj.put("customercommand", StringUtil.nullConvertToEmptyString(rs.getString("customercommand")));
			obj.put("transway", StringUtil.nullConvertToEmptyString(rs.getString("transway")));
			obj.put("cwbprovince", StringUtil.nullConvertToEmptyString(rs.getString("cwbprovince")));
			obj.put("cwbcity", StringUtil.nullConvertToEmptyString(rs.getString("cwbcity")));
			obj.put("cwbcounty", StringUtil.nullConvertToEmptyString(rs.getString("cwbcounty")));
			obj.put("receivablefee", rs.getBigDecimal("receivablefee"));
			obj.put("paybackfee", rs.getBigDecimal("paybackfee"));
			obj.put("cwb", StringUtil.nullConvertToEmptyString(rs.getString("cwb")));
			obj.put("shipperid", rs.getLong("shipperid"));
			obj.put("cwbordertypeid", rs.getInt("cwbordertypeid"));
			obj.put("transcwb", StringUtil.nullConvertToEmptyString(rs.getString("transcwb")));
			obj.put("destination", StringUtil.nullConvertToEmptyString(rs.getString("destination")));
			obj.put("cwbdelivertypeid", StringUtil.nullConvertToEmptyString(rs.getString("cwbdelivertypeid")));
			obj.put("exceldeliver", StringUtil.nullConvertToEmptyString(rs.getString("exceldeliver")));
			obj.put("excelbranch", StringUtil.nullConvertToEmptyString(rs.getString("excelbranch")));
			obj.put("timelimited", StringUtil.nullConvertToEmptyString(rs.getString("timelimited")));
			obj.put("excelimportuserid", rs.getLong("excelimportuserid"));
			obj.put("state", rs.getLong("state"));
			obj.put("printtime", rs.getString("printtime"));
			obj.put("commonid", rs.getLong("commonid"));
			obj.put("commoncwb", rs.getString("commoncwb"));
			obj.put("signtypeid", rs.getLong("signtypeid"));
			obj.put("podrealname", rs.getString("podrealname"));
			obj.put("podtime", rs.getString("podtime"));
			obj.put("podsignremark", rs.getString("podsignremark"));
			obj.put("modelname", rs.getString("modelname"));
			obj.put("scannum", rs.getLong("scannum"));
			obj.put("isaudit", rs.getLong("isaudit"));
			obj.put("remark1", StringUtil.nullConvertToEmptyString(rs.getString("remark1")));
			obj.put("remark2", StringUtil.nullConvertToEmptyString(rs.getString("remark2")));
			obj.put("remark3", StringUtil.nullConvertToEmptyString(rs.getString("remark3")));
			obj.put("remark4", StringUtil.nullConvertToEmptyString(rs.getString("remark4")));
			obj.put("remark5", StringUtil.nullConvertToEmptyString(rs.getString("remark5")));
			obj.put("backreason", rs.getString("backreason"));
			obj.put("leavedreason", rs.getString("leavedreason"));
			obj.put("paywayid", rs.getLong("paywayid"));
			obj.put("newpaywayid", rs.getString("newpaywayid"));
			obj.put("tuihuoid", rs.getLong("tuihuoid"));
			obj.put("deliverybranchid", rs.getLong("deliverybranchid"));
			obj.put("cwbstate", rs.getLong("cwbstate"));
			obj.put("currentbranchid", rs.getLong("currentbranchid"));
			obj.put("backreasonid", rs.getLong("backreasonid"));
			obj.put("leavedreasonid", rs.getLong("leavedreasonid"));
			obj.put("packagecode", rs.getString("packagecode"));
			obj.put("multi_shipcwb", rs.getString("multi_shipcwb"));
			obj.put("deliverystate", rs.getInt("deliverystate"));
			obj.put("auditingtime", rs.getString("auditingtime"));
			obj.put("receivedfee", rs.getBigDecimal("receivedfee"));
			obj.put("deliverytime", rs.getString("deliverytime"));
			obj.put("pushtime", rs.getString("pushtime"));
			return obj;
		}

	}

	private final class CwbBackPayMapper implements RowMapper<JSONObject> {

		@Override
		public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
			JSONObject obj = new JSONObject();
			obj.put("opscwbid", rs.getLong("opscwbid"));
			obj.put("startbranchid", rs.getLong("startbranchid"));
			obj.put("nextbranchid", rs.getLong("nextbranchid"));
			obj.put("backtocustomer_awb", StringUtil.nullConvertToEmptyString(rs.getString("backtocustomer_awb")));
			obj.put("cwbflowflag", StringUtil.nullConvertToEmptyString(rs.getString("cwbflowflag")));
			obj.put("carrealweight", rs.getBigDecimal("carrealweight"));
			obj.put("cartype", StringUtil.nullConvertToEmptyString(rs.getString("cartype")));
			obj.put("carwarehouse", StringUtil.nullConvertToEmptyString(rs.getString("carwarehouse")));
			obj.put("carsize", StringUtil.nullConvertToEmptyString(rs.getString("carsize")));
			obj.put("backcaramount", rs.getBigDecimal("backcaramount"));
			obj.put("sendcarnum", rs.getLong("sendcarnum"));
			obj.put("backcarnum", rs.getLong("backcarnum"));
			obj.put("caramount", rs.getBigDecimal("caramount"));
			obj.put("backcarname", StringUtil.nullConvertToEmptyString(rs.getString("backcarname")));
			obj.put("sendcarname", StringUtil.nullConvertToEmptyString(rs.getString("sendcarname")));
			obj.put("deliverid", rs.getLong("deliverid"));
			obj.put("emailfinishflag", rs.getInt("emailfinishflag"));
			obj.put("reacherrorflag", rs.getInt("reacherrorflag"));
			obj.put("orderflowid", rs.getLong("orderflowid"));
			obj.put("flowordertype", rs.getLong("flowordertype"));
			obj.put("cwbreachbranchid", rs.getLong("cwbreachbranchid"));
			obj.put("cwbreachdeliverbranchid", rs.getLong("cwbreachdeliverbranchid"));
			obj.put("podfeetoheadflag", StringUtil.nullConvertToEmptyString(rs.getString("podfeetoheadflag")));
			obj.put("podfeetoheadcheckflag", StringUtil.nullConvertToEmptyString(rs.getString("podfeetoheadcheckflag")));
			obj.put("leavedreasonid", rs.getLong("leavedreasonid"));
			obj.put("customerwarehouseid", StringUtil.nullConvertToEmptyString(rs.getString("customerwarehouseid")));
			obj.put("emaildate", StringUtil.nullConvertToEmptyString(rs.getString("emaildate")));
			obj.put("emaildateid", rs.getLong("emaildateid"));
			obj.put("serviceareaid", rs.getLong("serviceareaid"));
			obj.put("customerid", rs.getLong("customerid"));
			obj.put("shipcwb", StringUtil.nullConvertToEmptyString(rs.getString("shipcwb")));
			obj.put("consigneeno", StringUtil.nullConvertToEmptyString(rs.getString("consigneeno")));
			obj.put("consigneeaddress", StringUtil.nullConvertToEmptyString(rs.getString("consigneeaddress")));
			obj.put("consigneepostcode", StringUtil.nullConvertToEmptyString(rs.getString("consigneepostcode")));
			obj.put("cwbremark", StringUtil.nullConvertToEmptyString(rs.getString("cwbremark")));
			obj.put("customercommand", StringUtil.nullConvertToEmptyString(rs.getString("customercommand")));
			obj.put("transway", StringUtil.nullConvertToEmptyString(rs.getString("transway")));
			obj.put("cwbprovince", StringUtil.nullConvertToEmptyString(rs.getString("cwbprovince")));
			obj.put("cwbcity", StringUtil.nullConvertToEmptyString(rs.getString("cwbcity")));
			obj.put("cwbcounty", StringUtil.nullConvertToEmptyString(rs.getString("cwbcounty")));
			obj.put("receivablefee", rs.getBigDecimal("receivablefee"));
			obj.put("paybackfee", rs.getBigDecimal("paybackfee"));
			obj.put("cwb", StringUtil.nullConvertToEmptyString(rs.getString("cwb")));
			obj.put("shipperid", rs.getLong("shipperid"));
			obj.put("cwbordertypeid", rs.getInt("cwbordertypeid"));
			obj.put("transcwb", StringUtil.nullConvertToEmptyString(rs.getString("transcwb")));
			obj.put("destination", StringUtil.nullConvertToEmptyString(rs.getString("destination")));
			obj.put("cwbdelivertypeid", StringUtil.nullConvertToEmptyString(rs.getString("cwbdelivertypeid")));
			obj.put("exceldeliver", StringUtil.nullConvertToEmptyString(rs.getString("exceldeliver")));
			obj.put("excelbranch", StringUtil.nullConvertToEmptyString(rs.getString("excelbranch")));
			obj.put("timelimited", StringUtil.nullConvertToEmptyString(rs.getString("timelimited")));
			obj.put("excelimportuserid", rs.getLong("excelimportuserid"));
			obj.put("state", rs.getLong("state"));
			obj.put("printtime", rs.getString("printtime"));
			obj.put("commonid", rs.getLong("commonid"));
			obj.put("commoncwb", rs.getString("commoncwb"));
			obj.put("signtypeid", rs.getLong("signtypeid"));
			obj.put("podrealname", rs.getString("podrealname"));
			obj.put("podtime", rs.getString("podtime"));
			obj.put("podsignremark", rs.getString("podsignremark"));
			obj.put("modelname", rs.getString("modelname"));
			obj.put("scannum", rs.getLong("scannum"));
			obj.put("isaudit", rs.getLong("isaudit"));
			obj.put("remark1", StringUtil.nullConvertToEmptyString(rs.getString("remark1")));
			obj.put("remark2", StringUtil.nullConvertToEmptyString(rs.getString("remark2")));
			obj.put("remark3", StringUtil.nullConvertToEmptyString(rs.getString("remark3")));
			obj.put("remark4", StringUtil.nullConvertToEmptyString(rs.getString("remark4")));
			obj.put("remark5", StringUtil.nullConvertToEmptyString(rs.getString("remark5")));
			obj.put("backreason", rs.getString("backreason"));
			obj.put("leavedreason", rs.getString("leavedreason"));
			obj.put("paywayid", rs.getLong("paywayid"));
			obj.put("newpaywayid", rs.getString("newpaywayid"));
			obj.put("tuihuoid", rs.getLong("tuihuoid"));
			obj.put("deliverybranchid", rs.getLong("deliverybranchid"));
			obj.put("cwbstate", rs.getLong("cwbstate"));
			obj.put("currentbranchid", rs.getLong("currentbranchid"));
			obj.put("backreasonid", rs.getLong("backreasonid"));
			obj.put("leavedreasonid", rs.getLong("leavedreasonid"));
			obj.put("packagecode", rs.getString("packagecode"));
			obj.put("multi_shipcwb", rs.getString("multi_shipcwb"));
			obj.put("deliverystate", rs.getInt("deliverystate"));
			obj.put("auditingtime", rs.getString("auditingtime"));
			obj.put("returnedfee", rs.getBigDecimal("returnedfee"));
			obj.put("receivedfee", rs.getBigDecimal("receivedfee"));
			obj.put("deliverystate", rs.getInt("deliverystate"));
			obj.put("deliverytime", rs.getString("deliverytime"));
			CwbDAO.this.setValueByUser(rs, obj);
			return obj;
		}
	}

	private final class CwbAndPrintMapper implements RowMapper<JSONObject> {
		@Override
		public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
			JSONObject obj = new JSONObject();
			obj.put("customername", rs.getString("customername"));
			obj.put("cwbcount", rs.getString("cwbcount"));
			obj.put("balenum", rs.getString("balenum"));
			obj.put("goodscount", rs.getString("goodscount"));
			return obj;
		}
	}

	private final class CwbForChuKuPrintMapper implements RowMapper<CwbOrder> {

		@Override
		public CwbOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
			CwbOrder cwbOrder = new CwbOrder();
			cwbOrder.setNextbranchid(rs.getLong("nextbranchid"));
			cwbOrder.setCarrealweight(rs.getBigDecimal("carrealweight"));
			cwbOrder.setCarsize(StringUtil.nullConvertToEmptyString(rs.getString("carsize")));
			cwbOrder.setSendcarnum(rs.getLong("sendcarnum"));
			cwbOrder.setBackcarnum(rs.getLong("backcarnum"));
			cwbOrder.setCaramount(rs.getBigDecimal("caramount"));
			cwbOrder.setCustomerid(rs.getLong("customerid"));
			cwbOrder.setConsigneeaddress(StringUtil.nullConvertToEmptyString(rs.getString("consigneeaddress")));
			cwbOrder.setConsigneepostcode(StringUtil.nullConvertToEmptyString(rs.getString("consigneepostcode")));
			cwbOrder.setCwbremark(StringUtil.nullConvertToEmptyString(rs.getString("cwbremark")));
			cwbOrder.setReceivablefee(rs.getBigDecimal("receivablefee"));
			cwbOrder.setPaybackfee(rs.getBigDecimal("paybackfee"));
			cwbOrder.setCwb(StringUtil.nullConvertToEmptyString(rs.getString("cwb")));
			cwbOrder.setCwbordertypeid(rs.getInt("cwbordertypeid"));
			cwbOrder.setPaywayid(rs.getLong("paywayid"));
			CwbDAO.this.setValueByUser(rs, cwbOrder);
			return cwbOrder;
		}

	}

	private final class GroupCustomerIdMapper implements RowMapper<JSONObject> {
		@Override
		public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
			JSONObject obj = new JSONObject();
			obj.put("customerid", rs.getString("customerid"));
			obj.put("cwbcount", rs.getString("cwbcount"));
			obj.put("receivablefee", rs.getBigDecimal("receivablefee"));
			obj.put("tuotoucount", rs.getString("tuotoucount"));
			obj.put("tuotoureceivablefee", rs.getBigDecimal("tuotoureceivablefee"));
			obj.put("jushoucount", rs.getString("jushoucount"));
			obj.put("jushoureceivablefee", rs.getBigDecimal("jushoureceivablefee"));
			obj.put("zhiliucount", rs.getString("zhiliucount"));
			obj.put("zhiliureceivablefee", rs.getBigDecimal("zhiliureceivablefee"));
			obj.put("diushicount", rs.getString("diushicount"));
			obj.put("diushireceivablefee", rs.getBigDecimal("diushireceivablefee"));
			obj.put("wujieguocount", rs.getString("wujieguocount"));
			obj.put("wujieguoreceivablefee", rs.getBigDecimal("wujieguoreceivablefee"));
			return obj;
		}
	}

	private final class YiTongJiMapper implements RowMapper<CwbOrder> {
		@Override
		public CwbOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
			CwbOrder obj = new CwbOrder();
			obj.setOpscwbid(rs.getLong("opscwbid"));
			obj.setSendcarnum(rs.getLong("sendcarnum"));
			return obj;
		}
	}

	private final class SmtCountMapper implements RowMapper<Smtcount> {
		@Override
		public Smtcount mapRow(ResultSet rs, int rowNum) throws SQLException {
			Smtcount obj = new Smtcount();
			obj.setCount(rs.getLong("count"));
			obj.setPscount(rs.getLong("pscount"));
			obj.setSmhcount(rs.getLong("smhcount"));
			obj.setSmtcount(rs.getLong("smtcount"));
			return obj;
		}
	}

	private final class CwbFeeMapper implements RowMapper<JSONObject> {
		@Override
		public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
			JSONObject obj = new JSONObject();
			obj.put("cwbcount", rs.getString("cwbcount"));
			obj.put("receivablefees", rs.getBigDecimal("receivablefees"));
			obj.put("paybackfees", rs.getBigDecimal("paybackfees"));
			return obj;
		}
	}

	private final class CwbAndChuKuPrintMapper implements RowMapper<JSONObject> {
		@Override
		public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
			JSONObject obj = new JSONObject();
			obj.put("customername", rs.getString("customername"));
			obj.put("deliverid", rs.getLong("deliverid"));
			obj.put("cwbcount", rs.getString("cwbcount"));
			obj.put("sendcarnum", rs.getString("sendcarnum"));
			obj.put("backcarnum", rs.getString("backcarnum"));
			obj.put("caramount", rs.getString("caramount"));
			obj.put("receivablefee", rs.getString("receivablefee"));
			obj.put("paybackfee", rs.getString("paybackfee"));
			return obj;
		}
	}

	private final class CwbForPrintMapper implements RowMapper<JSONObject> {
		@Override
		public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
			JSONObject obj = new JSONObject();
			obj.put("cwb", rs.getString("cwb"));
			obj.put("nextbranchid", rs.getString("nextbranchid"));
			obj.put("flowordertype", rs.getString("flowordertype"));
			obj.put("createtime", rs.getString("createtime"));
			return obj;
		}
	}

	private final class CwbForBackToCustomerMapper implements RowMapper<JSONObject> {
		@Override
		public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
			JSONObject obj = new JSONObject();
			obj.put("cwb", rs.getString("cwb"));
			obj.put("customerid", rs.getString("customerid"));
			obj.put("flowordertype", rs.getString("flowordertype"));
			obj.put("createtime", rs.getString("createtime"));
			return obj;
		}
	}

	private final class CwbForLHPrintMapper implements RowMapper<JSONObject> {
		@Override
		public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
			JSONObject obj = new JSONObject();
			obj.put("cwb", rs.getString("cwb"));
			obj.put("deliverid", rs.getString("deliverid"));
			obj.put("flowordertype", rs.getString("flowordertype"));
			obj.put("createtime", rs.getString("createtime"));
			return obj;
		}
	}

	private final class CwbSomePrintMapper implements RowMapper<JSONObject> {
		@Override
		public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
			JSONObject obj = new JSONObject();
			obj.put("customername", rs.getString("customername"));
			obj.put("cwb", rs.getString("cwb"));
			obj.put("receivablefee", rs.getString("receivablefee"));

			return obj;
		}
	}

	private final class CwbSomeYZPrintMapper implements RowMapper<JSONObject> {
		@Override
		public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
			JSONObject obj = new JSONObject();
			obj.put("customername", rs.getString("customername"));
			obj.put("cwb", rs.getString("cwb"));
			obj.put("receivablefee", rs.getString("receivablefee"));
			obj.put("sendcarnum", rs.getString("sendcarnum"));

			return obj;
		}
	}

	private final class CwbSmalMaper implements RowMapper<CwbOrder> {
		@Override
		public CwbOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
			CwbOrder cwbOrder = new CwbOrder();
			cwbOrder.setOpscwbid(rs.getLong("opscwbid"));
			if (CwbDAO.this.getUser().getShowmobileflag() == 1) {
				cwbOrder.setConsigneemobile(StringUtil.nullConvertToEmptyString(rs.getString("consigneemobile")));
			} else {
				cwbOrder.setConsigneemobile("******");
			}
			cwbOrder.setCustomerid(rs.getLong("customerid"));
			return cwbOrder;
		}
	}

	private final class CwbDetailNotDetailMapper implements RowMapper<CwbOrder> {
		@Override
		public CwbOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
			CwbOrder co = new CwbOrder();

			co.setStartbranchid(rs.getLong("startbranchid"));
			co.setNextbranchid(rs.getLong("nextbranchid"));
			co.setFlowordertype(rs.getLong("flowordertype"));
			co.setCwb(StringUtil.nullConvertToEmptyString(rs.getString("cwb")));
			co.setCurrentbranchid(rs.getLong("currentbranchid"));
			co.setDeliverid(rs.getLong("deliverid"));
			co.setCustomerid(rs.getLong("customerid"));
			return co;
		}
	}

	private final class CwbLinghuoMapper implements RowMapper<JSONObject> {

		JSONObject obj = new JSONObject();

		@Override
		public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
			JSONObject obj = new JSONObject();
			obj.put("deliveryid", rs.getLong("deliveryid"));
			obj.put("customerid", rs.getLong("customerid"));
			if (CwbDAO.this.getUser().getShownameflag() != 1) {
				obj.put("consigneename", "******");
			} else {
				obj.put("consigneename", StringUtil.nullConvertToEmptyString(rs.getString("consigneename")));
			}
			obj.put("consigneeaddress", StringUtil.nullConvertToEmptyString(rs.getString("consigneeaddress")));
			obj.put("receivablefee", rs.getBigDecimal("receivablefee"));
			obj.put("cwb", StringUtil.nullConvertToEmptyString(rs.getString("cwb")));
			obj.put("state", rs.getLong("state"));
			obj.put("deliverystate", rs.getInt("deliverystate"));
			obj.put("receivedfee", rs.getBigDecimal("receivedfee"));
			obj.put("createtime", rs.getString("createtime"));
			return obj;
		}
	}

	private final class CwbFDMapper implements RowMapper<CwbOrder> {
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

			cwbOrder.setFankuitime(rs.getString("fankuitime"));
			cwbOrder.setShenhetime(rs.getString("shenhetime"));
			CwbDAO.this.setValueByUser(rs, cwbOrder);
			try {
				if (rs.getString("chuzhantime") != null) {
					cwbOrder.setChuzhantime(rs.getString("chuzhantime"));
				}
			} catch (Exception e) {

			}
			return cwbOrder;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public CwbOrder getCwbByCwb(String cwb) {
		try {
			return this.jdbcTemplate.queryForObject("SELECT * from express_ops_cwb_detail where cwb=? and state=1 limit 0,1", new CwbMapper(), cwb);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public CwbOrder getCwbByCwbLock(String cwb) {
		try {
			return this.jdbcTemplate.queryForObject("SELECT * from express_ops_cwb_detail where cwb=? and state=1 for update", new CwbMapper(), cwb);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<CwbOrder> getCwbsByEmailDateId(long emaildateid) {
		return this.jdbcTemplate.query("select * from express_ops_cwb_detail where emaildateid=? and state=1", new CwbMapper(), emaildateid);
	}

	public List<CwbOrder> getCwbsByEmailDateIds(String emaildateids) {
		return this.jdbcTemplate.query("select * from express_ops_cwb_detail where emaildateid in(" + emaildateids + ") and state=1", new CwbMapper());
	}

	/**
	 * 按发货时间分组查询供货商的发货数据
	 * 
	 * @param customerids
	 * @param beginemaildate
	 * @param endemaildate
	 * @return
	 */
	public List<JSONObject> getCwbsByEmailDate(String customerids, String beginemaildate, String endemaildate) {
		return this.jdbcTemplate.query("SELECT customerid," + "COUNT(1) AS cwbcount ," + "SUM(receivablefee) AS receivablefee,"
				+ "SUM(CASE WHEN (flowordertype IN(35,36)  AND deliverystate IN(1,3,2)) THEN 1 ELSE 0 END) AS tuotoucount ,"
				+ "SUM(CASE WHEN (flowordertype IN(35,36)  AND deliverystate IN(1,3,2)) THEN receivablefee ELSE 0 END) AS tuotoureceivablefee , "
				+ "SUM(CASE WHEN (flowordertype IN(35,36)  AND deliverystate IN(4,5,7)) THEN 1 ELSE 0 END) AS jushoucount ,"
				+ "SUM(CASE WHEN (flowordertype IN(35,36)  AND deliverystate IN(4,5,7)) THEN receivablefee ELSE 0 END) AS jushoureceivablefee , "
				+ "SUM(CASE WHEN (flowordertype IN(35,36)  AND deliverystate IN(6)) THEN 1 ELSE 0 END) AS zhiliucount ,"
				+ "SUM(CASE WHEN (flowordertype IN(35,36)  AND deliverystate IN(6)) THEN receivablefee ELSE 0 END) AS zhiliureceivablefee , "
				+ "SUM(CASE WHEN (flowordertype IN(35,36)  AND deliverystate IN(8)) THEN 1 ELSE 0 END) AS diushicount ,"
				+ "SUM(CASE WHEN (flowordertype IN(35,36)  AND deliverystate IN(8)) THEN receivablefee ELSE 0 END) AS diushireceivablefee , "
				+ "SUM(CASE WHEN (flowordertype NOT IN(35,36) ) THEN 1 ELSE 0 END) AS wujieguocount ,"
				+ "SUM(CASE WHEN (flowordertype NOT IN(35,36) ) THEN receivablefee ELSE 0 END) AS wujieguoreceivablefee " + " FROM express_ops_cwb_detail where emaildate >='" + beginemaildate
				+ "' and emaildate<='" + endemaildate + "' " + "" + (customerids.length() > 0 ? " and customerid in(" + customerids + ")" : " ") + " and state=1 group by customerid",
				new GroupCustomerIdMapper());
	}

	/**
	 * 按发货时间查询供货商的发货数据
	 * 
	 * @param customerids
	 * @param beginemaildate
	 * @param endemaildate
	 * @param page
	 * @return
	 */
	public List<CwbOrder> getCwbOrderByEmailDate(String customerids, String beginemaildate, String endemaildate, long page) {
		return this.jdbcTemplate.query("select * from express_ops_cwb_detail " + " where emaildate >='" + beginemaildate + "' and emaildate<='" + endemaildate + "' " + ""
				+ (customerids.length() > 0 ? " and customerid in(" + customerids + ")" : " ") + " and state=1 limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER,
				new CwbMapper());
	}

	public long getCwbOrderByEmailDateCount(String customerids, String beginemaildate, String endemaildate) {
		return this.jdbcTemplate.queryForLong("select count(1) from express_ops_cwb_detail " + " where emaildate >='" + beginemaildate + "' and emaildate<='" + endemaildate + "' " + ""
				+ (customerids.length() > 0 ? " and customerid in(" + customerids + ")" : " ") + " and state=1 ");
	}

	/**
	 * 按发货时间查询供货商的妥投数据
	 * 
	 * @param customerids
	 * @param beginemaildate
	 * @param endemaildate
	 * @return
	 */
	public List<CwbOrder> getCwbsByEmailDateAndTuotou(String customerids, String beginemaildate, String endemaildate, long page) {
		return this.jdbcTemplate.query("select *  from express_ops_cwb_detail " + " where emaildate >='" + beginemaildate + "' and emaildate<='" + endemaildate + "' " + ""
				+ (customerids.length() > 0 ? " and customerid in(" + customerids + ")" : " ") + " and state=1 and flowordertype in(" + FlowOrderTypeEnum.YiFanKui.getValue() + ","
				+ FlowOrderTypeEnum.YiShenHe.getValue() + ") " + " and deliverystate in(" + DeliveryStateEnum.PeiSongChengGong.getValue() + "," + DeliveryStateEnum.ShangMenHuanChengGong.getValue()
				+ "," + DeliveryStateEnum.ShangMenTuiChengGong.getValue() + ") " + "  limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER, new CwbMapper());
	}

	public long getCwbsByEmailDateAndTuotouCount(String customerids, String beginemaildate, String endemaildate) {
		return this.jdbcTemplate.queryForLong("select count(1) from express_ops_cwb_detail " + " where emaildate >='" + beginemaildate + "' and emaildate<='" + endemaildate + "' " + ""
				+ (customerids.length() > 0 ? " and customerid in(" + customerids + ")" : " ") + " and state=1 and flowordertype in(" + FlowOrderTypeEnum.YiFanKui.getValue() + ","
				+ FlowOrderTypeEnum.YiShenHe.getValue() + ") " + " and deliverystate in(" + DeliveryStateEnum.PeiSongChengGong.getValue() + "," + DeliveryStateEnum.ShangMenHuanChengGong.getValue()
				+ "," + DeliveryStateEnum.ShangMenTuiChengGong.getValue() + ") ");
	}

	/**
	 * 按发货时间分组查询供货商的拒收数据：包括拒收、上门退拒退、部分拒收
	 * 
	 * @param customerids
	 * @param beginemaildate
	 * @param endemaildate
	 * @return
	 */
	public List<CwbOrder> getCwbsByEmailDateAndJushou(String customerids, String beginemaildate, String endemaildate, long page) {
		return this.jdbcTemplate.query("select * from express_ops_cwb_detail " + " where emaildate >='" + beginemaildate + "' and emaildate<='" + endemaildate + "' " + ""
				+ (customerids.length() > 0 ? " and customerid in(" + customerids + ")" : " ") + " and state=1 and flowordertype in(" + FlowOrderTypeEnum.YiFanKui.getValue() + ","
				+ FlowOrderTypeEnum.YiShenHe.getValue() + ") " + " and deliverystate in(" + DeliveryStateEnum.JuShou.getValue() + "," + DeliveryStateEnum.ShangMenJuTui.getValue() + ","
				+ DeliveryStateEnum.BuFenTuiHuo.getValue() + ") " + "  limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER, new CwbMapper());
	}

	public long getCwbsByEmailDateAndJushouCount(String customerids, String beginemaildate, String endemaildate) {
		return this.jdbcTemplate.queryForLong("select count(1) from express_ops_cwb_detail " + " where emaildate >='" + beginemaildate + "' and emaildate<='" + endemaildate + "' " + ""
				+ (customerids.length() > 0 ? " and customerid in(" + customerids + ")" : " ") + " and state=1 and flowordertype in(" + FlowOrderTypeEnum.YiFanKui.getValue() + ","
				+ FlowOrderTypeEnum.YiShenHe.getValue() + ") " + " and deliverystate in(" + DeliveryStateEnum.JuShou.getValue() + "," + DeliveryStateEnum.ShangMenJuTui.getValue() + ","
				+ DeliveryStateEnum.BuFenTuiHuo.getValue() + ") ");
	}

	/**
	 * 按发货时间分组查询供货商的滞留数据
	 * 
	 * @param customerids
	 * @param beginemaildate
	 * @param endemaildate
	 * @return
	 */
	public List<CwbOrder> getCwbsByEmailDateAndZhiliu(String customerids, String beginemaildate, String endemaildate, long page) {
		return this.jdbcTemplate.query("select * from express_ops_cwb_detail " + " where emaildate >='" + beginemaildate + "' and emaildate<='" + endemaildate + "' " + ""
				+ (customerids.length() > 0 ? " and customerid in(" + customerids + ")" : " ") + " and state=1 and flowordertype in(" + FlowOrderTypeEnum.YiFanKui.getValue() + ","
				+ FlowOrderTypeEnum.YiShenHe.getValue() + ") " + " and deliverystate in(" + DeliveryStateEnum.FenZhanZhiLiu.getValue() + ") " + "  limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ,"
				+ Page.ONE_PAGE_NUMBER, new CwbMapper());
	}

	public long getCwbsByEmailDateAndZhiliuCount(String customerids, String beginemaildate, String endemaildate) {
		return this.jdbcTemplate.queryForLong("select count(1) from express_ops_cwb_detail " + " where emaildate >='" + beginemaildate + "' and emaildate<='" + endemaildate + "' " + ""
				+ (customerids.length() > 0 ? " and customerid in(" + customerids + ")" : " ") + " and state=1 and flowordertype in(" + FlowOrderTypeEnum.YiFanKui.getValue() + ","
				+ FlowOrderTypeEnum.YiShenHe.getValue() + ") " + " and deliverystate in(" + DeliveryStateEnum.FenZhanZhiLiu.getValue() + ") ");
	}

	/**
	 * 按发货时间分组查询供货商的货物丢失数据
	 * 
	 * @param customerids
	 * @param beginemaildate
	 * @param endemaildate
	 * @return
	 */
	public List<CwbOrder> getCwbsByEmailDateAndDiushi(String customerids, String beginemaildate, String endemaildate, long page) {
		return this.jdbcTemplate.query("select * from express_ops_cwb_detail " + " where emaildate >='" + beginemaildate + "' and emaildate<='" + endemaildate + "' " + ""
				+ (customerids.length() > 0 ? " and customerid in(" + customerids + ")" : " ") + " and state=1 and flowordertype in(" + FlowOrderTypeEnum.YiFanKui.getValue() + ","
				+ FlowOrderTypeEnum.YiShenHe.getValue() + ") " + " and deliverystate in(" + DeliveryStateEnum.HuoWuDiuShi.getValue() + ") " + "  limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ,"
				+ Page.ONE_PAGE_NUMBER, new CwbMapper());
	}

	public long getCwbsByEmailDateAndDiushiCount(String customerids, String beginemaildate, String endemaildate) {
		return this.jdbcTemplate.queryForLong("select count(1) from express_ops_cwb_detail " + " where emaildate >='" + beginemaildate + "' and emaildate<='" + endemaildate + "' " + ""
				+ (customerids.length() > 0 ? " and customerid in(" + customerids + ")" : " ") + " and state=1 and flowordertype in(" + FlowOrderTypeEnum.YiFanKui.getValue() + ","
				+ FlowOrderTypeEnum.YiShenHe.getValue() + ") " + " and deliverystate in(" + DeliveryStateEnum.HuoWuDiuShi.getValue() + ") ");
	}

	/**
	 * 按发货时间分组查询供货商的无结果数据
	 * 
	 * @param customerids
	 * @param beginemaildate
	 * @param endemaildate
	 * @return
	 */
	public List<CwbOrder> getCwbsByEmailDateAndWujieguo(String customerids, String beginemaildate, String endemaildate, long page) {
		return this.jdbcTemplate.query("select * from express_ops_cwb_detail " + " where emaildate >='" + beginemaildate + "' and emaildate<='" + endemaildate + "' " + ""
				+ (customerids.length() > 0 ? " and customerid in(" + customerids + ")" : " ") + " and state=1 and flowordertype not in(" + FlowOrderTypeEnum.YiFanKui.getValue() + ","
				+ FlowOrderTypeEnum.YiShenHe.getValue() + ") " + "  limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER, new CwbMapper());
	}

	public long getCwbsByEmailDateAndWujieguoCount(String customerids, String beginemaildate, String endemaildate) {
		return this.jdbcTemplate.queryForLong("select count(1) from express_ops_cwb_detail " + " where emaildate >='" + beginemaildate + "' and emaildate<='" + endemaildate + "' " + ""
				+ (customerids.length() > 0 ? " and customerid in(" + customerids + ")" : " ") + " and state=1 and flowordertype not in(" + FlowOrderTypeEnum.YiFanKui.getValue() + ","
				+ FlowOrderTypeEnum.YiShenHe.getValue() + ") ");
	}

	public long getCwbCountByEmailDateId(long emaildateid) {
		return this.jdbcTemplate.queryForLong("select count(1) from express_ops_cwb_detail where emaildateid=? and state=1", emaildateid);
	}

	private String getCwbOrderByPageWhereSql(String sql, long customerid, long startbranchid, long nextbranchid, String beginemaildate, String endemaildate, String ordercwb, long servicearea,
			String emailfinishflag, String reacherrorflag, long emaildateid, long branchIsNull, long cwbstate) {

		if ((customerid > 0) || (startbranchid > 0) || (nextbranchid > 0) || (beginemaildate.length() > 0) || (endemaildate.length() > 0) || (ordercwb.length() > 0) || (servicearea > 0)
				|| (emailfinishflag.length() > 0) || (reacherrorflag.length() > 0) || (emaildateid > 0) || (branchIsNull > 0) || (cwbstate > 0)) {
			StringBuffer w = new StringBuffer();
			sql += " where ";
			if (customerid > 0) {
				w.append(" and customerid=" + customerid);
			}
			if (startbranchid > 0) {
				w.append(" and startbranchid=" + startbranchid);
			}
			if (nextbranchid > 0) {
				w.append(" and nextbranchid=" + nextbranchid);
			}
			if (emaildateid > 0) {
				w.append(" and emaildateid='" + emaildateid + "'");
			}
			if (beginemaildate.length() > 0) {
				w.append(" and emaildate > '" + beginemaildate + "'");
			}
			if (endemaildate.length() > 0) {
				w.append(" and emaildate < '" + endemaildate + "'");
			}
			if (servicearea > 0) {
				w.append(" and serviceareaid=" + servicearea);
			}
			if (emailfinishflag.length() > 0) {
				w.append(" and emailfinishflag='" + emailfinishflag + "'");
			}
			if (reacherrorflag.length() > 0) {
				w.append(" and reacherrorflag in(" + reacherrorflag + ")");
			}
			if (cwbstate > 0) {
				w.append(" and cwbstate=" + cwbstate);
			}

			if (ordercwb.trim().length() > 0) {
				w.append(" and cwb in (");
				for (String cwb : ordercwb.split("\r\n")) {
					if (cwb.trim().length() == 0) {
						continue;
					}
					w.append("'" + cwb.trim());
					w.append("',");
				}
				String u = w.substring(0, w.length() - 1) + ")";
				w = new StringBuffer(u);
			}
			if (branchIsNull == 1) {
				w.append(" and  excelbranch IS NOT NULL AND excelbranch<>'' ");
			} else if (branchIsNull == 2) {
				w.append(" and (excelbranch is null or excelbranch='') ");
			}
			w.append(" and state=1");
			sql += w.substring(4, w.length());
		} else {
			sql += " where state=1";
		}
		this.logger.info("sql:" + sql);
		return sql;

	}

	/*
	 * public List<CwbOrder> getcwbOrderByPageIsMyWarehouse(int page,long
	 * emaildateid,long branchIsNull, long onePageNumber){ String sql =
	 * "select * from express_ops_cwb_detail where emaildateid= "+emaildateid;
	 * sql +=
	 * " order by nextbranchid asc,consigneeaddress desc limit "+(page-1)*
	 * onePageNumber+" ,"+onePageNumber; List<CwbOrder> cwborderList =
	 * jdbcTemplate.query(sql, new CwbMapper()); return cwborderList; }
	 */
	public List<CwbOrder> getcwbOrderByPageIsMyWarehouse(long page, long customerid, String ordercwb, long emaildateid, CwbOrderAddressCodeEditTypeEnum addressCodeEditType, long onePageNumber,
			long branchid) {
		String sql = "select * from express_ops_cwb_detail where state=1 ";
		StringBuffer w = new StringBuffer();

		if (ordercwb.trim().length() > 0) {
			w.append(" and cwb in (" + ordercwb+ ")");
		} else if ((customerid > 0) || (emaildateid > 0) || (addressCodeEditType != null) || (branchid > 0)) {
			if (customerid > 0) {
				w.append(" and customerid=" + customerid);
			}
			if (emaildateid > 0) {
				w.append(" and emaildateid='" + emaildateid + "'");
			}
			if (addressCodeEditType != null) {
				w.append(" and  addresscodeedittype=").append(addressCodeEditType.getValue());
			}
			if (branchid > 0) {
				w.append(" and deliverybranchid=" + branchid);
			}
			
		}
		sql += w.toString();
		sql += " order by CONVERT( excelbranch USING gbk ) COLLATE gbk_chinese_ci DESC,consigneeaddress desc ,nextbranchid ";
		if (page > 0) {
			sql += " limit " + ((page - 1) * onePageNumber) + " ," + onePageNumber;
		}

		List<CwbOrder> cwborderList = this.jdbcTemplate.query(sql, new CwbMapper());
		return cwborderList;
	}

	public String getcwbOrderByPageIsMyWarehouseSql(long customerid, String ordercwb, long emaildateid, CwbOrderAddressCodeEditTypeEnum addressCodeEditType, long branchid) {
		String sql = "select * from express_ops_cwb_detail where state=1 ";
		StringBuffer w = new StringBuffer();

		if (ordercwb.trim().length() > 0) {
			w.append(" and cwb in(" + ordercwb + ")");
		} else if ((customerid > 0) || (emaildateid > 0) || (addressCodeEditType != null) || (branchid > 0)) {

			if (customerid > 0) {
				w.append(" and customerid=" + customerid);
			}
			if (emaildateid > 0) {
				w.append(" and emaildateid='" + emaildateid + "'");
			}

			if (addressCodeEditType != null) {
				w.append(" and  addresscodeedittype=").append(addressCodeEditType.getValue());
			}
			if (branchid > 0) {
				w.append(" and deliverybranchid=" + branchid);
			}
		}
		sql += w.toString();

		sql += " order by CONVERT( excelbranch USING gbk ) COLLATE gbk_chinese_ci DESC,consigneeaddress desc ";
		return sql;
	}

	public long getcwborderCountIsMyWarehouse(long customerid, String ordercwb, long emaildateid, CwbOrderAddressCodeEditTypeEnum addressCodeEditType, long branchid) {
		String sql = "select count(1) from express_ops_cwb_detail where state=1 ";
		StringBuffer w = new StringBuffer();
		if (ordercwb.trim().length() > 0) {
			w.append(" and cwb in (" + ordercwb+ ")");
		} else if ((customerid > 0) || (emaildateid > 0) || (addressCodeEditType != null) || (branchid > 0)) {
			if (customerid > 0) {
				w.append(" and customerid=" + customerid);
			}
			if (emaildateid > 0) {
				w.append(" and emaildateid='" + emaildateid + "'");
			}

			if (addressCodeEditType != null) {
				w.append(" and  addresscodeedittype=").append(addressCodeEditType.getValue());
			}
			if (branchid > 0) {
				w.append(" and deliverybranchid=" + branchid);
			}
		}
		sql += w.toString();
		return this.jdbcTemplate.queryForInt(sql);
	}

	public List<CwbOrder> getcwbOrderByPage(long page, long customerid, long branchid, String beginemaildate, String endemaildate, String ordercwb, long servicearea, String emailfinishflag,
			String reacherrorflag, long emaildateid) {
		String sql = "select * from express_ops_cwb_detail";
		sql = this.getCwbOrderByPageWhereSql(sql, customerid, branchid, 0, beginemaildate, endemaildate, ordercwb, servicearea, emailfinishflag, reacherrorflag, emaildateid, 0, 0);
		sql += " order by nextbranchid asc,consigneeaddress desc limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		List<CwbOrder> cwborderList = this.jdbcTemplate.query(sql, new CwbMapper());
		return cwborderList;
	}

	public List<CwbOrder> getcwbOrderByPage(long page, long emaildateid) {
		return this.getcwbOrderByPage(page, 0, 0, "", "", "", 0, "", "", emaildateid);
	}

	public long getcwborderCount(long emaildateid) {
		return this.getcwborderCount(0, 0, "", "", "", 0, "", "", emaildateid);
	}

	public long getcwborderCount(long customerid, long branchid, String beginemaildate, String endemaildate, String ordercwb, long servicearea, String emailfinishflag, String reacherrorflag,
			long emaildateid) {
		String sql = "select count(1) from express_ops_cwb_detail";
		sql = this.getCwbOrderByPageWhereSql(sql, customerid, branchid, 0, beginemaildate, endemaildate, ordercwb, servicearea, emailfinishflag, reacherrorflag, emaildateid, 0, 0);
		return this.jdbcTemplate.queryForInt(sql);
	}

	/**
	 * 查询当前以匹配的数
	 * 
	 * @param customerid
	 * @param branchid
	 * @param beginemaildate
	 * @param endemaildate
	 * @param ordercwb
	 * @param isAddressOk
	 *            0 为全部 1为匹配的 2为未处理的
	 * @return
	 */
	public long getcwborderCountIsNotAddress(long customerid, String beginemaildate, String endemaildate, String ordercwb, long emaildateid, CwbOrderAddressCodeEditTypeEnum addressCodeEditType) {
		String sql = "select count(1) from express_ops_cwb_detail where state=1 ";
		StringBuffer w = new StringBuffer();
		if (ordercwb.trim().length() > 0) {
			w.append(" and cwb in (" + ordercwb+ ")");
		} else if ((customerid > 0) || (beginemaildate.length() > 0) || (endemaildate.length() > 0) || (emaildateid > 0)) {
			if (customerid > 0) {
				w.append(" and customerid=" + customerid);
			}
			if (emaildateid > 0) {
				w.append(" and emaildateid='" + emaildateid + "'");
			}
			if (beginemaildate.length() > 0) {
				w.append(" and emaildate > '" + beginemaildate + "'");
			}
			if (endemaildate.length() > 0) {
				w.append(" and emaildate < '" + endemaildate + "'");
			}
		}
		sql += w.toString();

		if (addressCodeEditType != null) {
			sql += " and addresscodeedittype=" + addressCodeEditType.getValue();
		}
		this.logger.info("sql:" + sql);
		return this.jdbcTemplate.queryForInt(sql);
	}

	public void updateScannum(String cwb, long scannum) {
		this.jdbcTemplate.update("update express_ops_cwb_detail set scannum=? where cwb=? and state = 1  ", scannum, cwb);
	}

	public CwbOrder getCwbOrderByOpscwbid(long opscwbid) {
		return this.jdbcTemplate.queryForObject("select * from express_ops_cwb_detail where opscwbid=? and state=1 ", new CwbMapper(), opscwbid);
	}

	public void saveCwbOrder(final CwbOrder cwborder) {

		this.jdbcTemplate.update("update express_ops_cwb_detail set consigneeno=?,consigneename=?,consigneeaddress=?,consigneepostcode=?,consigneephone=? where opscwbid =? and state = 1 ",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, cwborder.getConsigneeno());
						ps.setString(2, cwborder.getConsigneename());
						ps.setString(3, cwborder.getConsigneeaddress());
						ps.setString(4, cwborder.getConsigneepostcode());
						ps.setString(5, cwborder.getConsigneephone());
						ps.setLong(6, cwborder.getOpscwbid());
					}
				});
	}

	public List<CwbOrder> getCwbByGroupid(long groupid) {
		String sql = "select * from express_ops_cwb_detail cd right outer join ( select * from express_ops_groupdetail where groupid=?) gd on cd.cwb=gd.cwb where cd.cwb<>'null' and cd.state =1 ";
		return this.jdbcTemplate.query(sql, new CwbMapper(), groupid);
	}

	public void updateSendCarNum(long multicwbnum, String cwb) {
		String sql = "update express_ops_cwb_detail set sendcarnum=? where cwb=? and state = 1 ";
		this.jdbcTemplate.update(sql, multicwbnum, cwb);
	}

	public void changeCwbGoodsType(String cwbs, int goodsType) {
		this.jdbcTemplate.update("update express_ops_cwb_detail set goods_type=? where cwb in(" + cwbs + ")", goodsType);
	}

	/**
	 * 查询存在的订单号.
	 * 
	 * @param cwbs
	 *            1,2,3.
	 * @return 去重订单号.
	 */
	public List<CwbOrder> queryForChangeGoodsType(String[] cwbArray) {
		String cwbs = this.getChangeGoodsTypeSqInParam(cwbArray);
		String sql = "select cwb from express_ops_cwb_detail where cwb in (" + cwbs + ")";
		List<CwbOrder> orderList = this.jdbcTemplate.query(sql, new CwbChangeGoodsTypeRowMapper());
		Set<String> signedCwbSet = this.querySignedCwb(cwbs);
		for (CwbOrder order : orderList) {
			if (signedCwbSet.contains(order.getCwb())) {
				order.setPodrealname("signed");
			}
		}
		return orderList;
	}

	public Set<String> querySignedCwb(String cwbs) {
		String sql = "select cwb from express_ops_delivery_state where sign_man is not  null and sign_man !='' and cwb in (" + cwbs + ")";
		List<String> resultList = this.jdbcTemplate.queryForList(sql, String.class);

		return new HashSet<String>(resultList);
	}

	private String getChangeGoodsTypeSqInParam(String[] cwbArray) {
		StringBuilder sqlInParam = new StringBuilder();
		for (String cwb : cwbArray) {
			sqlInParam.append("'");
			sqlInParam.append(cwb);
			sqlInParam.append("',");
		}
		return sqlInParam.substring(0, sqlInParam.length() - 1);
	}

	private class CwbChangeGoodsTypeRowMapper implements RowMapper<CwbOrder> {

		@Override
		public CwbOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
			String cwb = rs.getString("cwb");
			CwbOrder newOrder = new CwbOrder();
			newOrder.setCwb(cwb);

			return newOrder;
		}
	}

	public List<CwbOrder> getCwbOrderByCwbordertypeidAndBranchid(long page, long cwbordertypeid, long branchid, String customerids, long printType, String begindate, String enddate) {
		String sql = "select * from express_ops_cwb_detail where cwbordertypeid=? ";
		sql += " and (deliverybranchid=" + branchid + " or nextbranchid=" + branchid + ") ";
		if (branchid > -1) {
		}
		if (customerids.length() > 0) {
			sql += " and customerid in(" + customerids + ")";
		}
		if (printType == 0) {
			sql += " and printtime='' ";
		}
		if (printType == 1) {
			sql += " and printtime >= '" + begindate + "'  and printtime <= '" + enddate + "'";
		}
		sql += " and state = 1 ";
		sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		return this.jdbcTemplate.query(sql, new CwbMapper(), cwbordertypeid);
	}

	public long getCwbOrderCount(long cwbordertypeid, long branchid, String customerids, long printType, String begindate, String enddate) {
		String sql = "select count(1) from express_ops_cwb_detail where cwbordertypeid=? ";
		if (branchid > -1) {
			sql += " and (deliverybranchid=" + branchid + " or nextbranchid=" + branchid + ") ";
		}
		if (customerids.length() > 0) {
			sql += " and customerid in(" + customerids + ")";
		}
		if (printType == 0) {
			sql += " and printtime='' ";
		}
		if (printType == 1) {
			sql += " and printtime >= '" + begindate + "'  and printtime <= '" + enddate + "'";
		}
		sql += " and state = 1 ";
		return this.jdbcTemplate.queryForInt(sql, cwbordertypeid);
	}

	public Long getCwbByEmailDateCount(String emaildate) {
		return this.jdbcTemplate.queryForLong("SELECT count(1) from express_ops_cwb_detail where emaildate=? and state =1 ", emaildate);
	}

	public List<CwbOrder> getCwbByPrinttime(long deliverybranchid, long nextbranchid, String customerids) {
		try {
			String sql = "";
			if (deliverybranchid == nextbranchid) {
				sql = "select * from express_ops_cwb_detail where cwbordertypeid=" + CwbOrderTypeIdEnum.Shangmentui.getValue() + " and (deliverybranchid=" + deliverybranchid + " or nextbranchid="
						+ nextbranchid + ") " + " and printtime='' ";
				if (customerids.length() > 0) {
					sql += " and customerid in(" + customerids + ")";
				}
				sql += " and state =1 ";
			} else {
				sql = "select * from express_ops_cwb_detail where cwbordertypeid=" + CwbOrderTypeIdEnum.Shangmentui.getValue() + " and deliverybranchid=" + deliverybranchid + " "
						+ " and nextbranchid=" + nextbranchid + " and printtime='' ";
				if (customerids.length() > 0) {
					sql += " and customerid in(" + customerids + ")";
				}
				sql += " and state =1 ";
			}
			return this.jdbcTemplate.query(sql, new CwbMapper());
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}
	}

	public void saveCwbForPrinttime(String cwb, String printtime) {
		String sql = "update express_ops_cwb_detail set printtime=? where cwb=? and state =1 ";
		this.jdbcTemplate.update(sql, printtime, cwb);
	}

	public CwbOrder getCwbByCommoncwb(String commoncwb) {
		try {
			String sql = "select * from  express_ops_cwb_detail where commoncwb=? and state =1 ";
			return this.jdbcTemplate.queryForObject(sql, new CwbMapper(), commoncwb);
		} catch (EmptyResultDataAccessException ee) {
			return null;
		}

	}

	public String getSqlCwbForLingHuoPrint(long deliverid, long flowordertype) {
		String sql = "SELECT cd.* FROM  express_ops_cwb_detail  cd LEFT  JOIN express_ops_groupdetail gd ON cd.cwb=gd.cwb " + " WHERE cd.deliverid=" + deliverid + " AND gd.flowordertype="
				+ flowordertype + " AND cd.state =1 AND gd.issignprint=0 ";
		return sql;
	}

	public void saveCwbForChangecwb(String carrealweight, String carsize, long scanways, String scancwb) {
		String sql = "";
		if (scanways == 1) {
			sql = "update express_ops_cwb_detail set carrealweight=?,carsize=? where cwb=? and state =1 ";
		} else {
			sql = "update express_ops_cwb_detail set carrealweight=?,carsize=? where commoncwb=? and state =1 ";
		}
		this.jdbcTemplate.update(sql, carrealweight, carsize, scancwb);
	}

	public void saveCwbForPrintChangecwb(String carrealweight, String carsize, String transcwb, String cwb) {
		String sql = "update express_ops_cwb_detail set carrealweight=?,carsize=?,transcwb=? where cwb=? and state =1 ";
		this.jdbcTemplate.update(sql, carrealweight, carsize, transcwb, cwb);
	}

	public void saveCwbForPrintChangecwb(String transcwb, String cwb) {
		String sql = "update express_ops_cwb_detail set transcwb=? where cwb=? and state =1";
		this.jdbcTemplate.update(sql, transcwb, cwb);
	}

	// 数据导入中的数据失效
	public void dataLose(long emaildateid) {
		this.jdbcTemplate.update("update express_ops_cwb_detail set state=0  where state =1 and emaildateid=?", emaildateid);
	}

	// 使订单有效
	public void dataRelive(long emaildateid) {
		this.jdbcTemplate.update("update express_ops_cwb_detail set state=1  where state =0 and emaildateid=?", emaildateid);
	}

	public void updateDeliveryBranchid(String excelbranch, long branchid, String cwb, CwbOrderAddressCodeEditTypeEnum addressCodeEditType) {
		this.jdbcTemplate.update("update express_ops_cwb_detail set excelbranch=?,deliverybranchid=?,addresscodeedittype=?  where state =1 and cwb=?", excelbranch, branchid,
				addressCodeEditType.getValue(), cwb);
	}

	public void updateDeliveryBranchidAndNextbranchid(String excelbranch, long branchid, String cwb, CwbOrderAddressCodeEditTypeEnum addressCodeEditType) {
		this.jdbcTemplate.update("update express_ops_cwb_detail set excelbranch=?,deliverybranchid=?,nextbranchid=?,addresscodeedittype=?   where state =1 and cwb=?", excelbranch, branchid, branchid,
				addressCodeEditType.getValue(), cwb);
	}

	/**
	 * 新地址库匹配成功后处理
	 * 
	 * @param excelbranch
	 * @param branchid
	 * @param cwb
	 * @param addressCodeEditType
	 * @param delivererList
	 * @param timeLimitList
	 */
	public void updateAddressDeliveryBranchid(String excelbranch, long branchid, String cwb, CwbOrderAddressCodeEditTypeEnum addressCodeEditType, List<DelivererVo> delivererList,
			List<Integer> timeLimitList) {
		StringBuffer sql = new StringBuffer();
		sql.append("update express_ops_cwb_detail set excelbranch = ? ,deliverybranchid = ? ,addresscodeedittype = ? ");
		if ((delivererList != null) && (delivererList.size() == 1)) {
			sql.append(" ,deliverid = ").append(delivererList.get(0).getExternalId()).append(",exceldeliver = '").append(delivererList.get(0).getName()).append("'");
		}
		if ((timeLimitList != null) && (timeLimitList.size() == 1)) {
			sql.append(" ,timelimited = '").append(timeLimitList.get(0)).append("'");
		}
		sql.append(" where state = 1 and cwb = ?");
		this.jdbcTemplate.update(sql.toString(), excelbranch, branchid, addressCodeEditType.getValue(), cwb);
	}

	/**
	 * 新地址库匹配成功后处理
	 * 
	 * @param excelbranch
	 * @param branchid
	 * @param cwb
	 * @param addressCodeEditType
	 * @param delivererList
	 * @param timeLimitList
	 */
	public void updateAddressDeliveryBranchidAndNextbranchid(String excelbranch, long branchid, String cwb, CwbOrderAddressCodeEditTypeEnum addressCodeEditType, List<DelivererVo> delivererList,
			List<Integer> timeLimitList) {
		StringBuffer sql = new StringBuffer();
		sql.append("update express_ops_cwb_detail set excelbranch = ? ,deliverybranchid = ? ,nextbranchid = ? ,addresscodeedittype = ?");
		if ((delivererList != null) && (delivererList.size() == 1)) {
			sql.append(" ,deliverid = ").append(delivererList.get(0).getExternalId()).append(",exceldeliver = '").append(delivererList.get(0).getName()).append("'");
		}
		if ((timeLimitList != null) && (timeLimitList.size() == 1)) {
			sql.append(" ,timelimited = '").append(timeLimitList.get(0)).append("'");
		}
		sql.append(" where state = 1 and cwb = ?");
		this.jdbcTemplate.update(sql.toString(), excelbranch, branchid, branchid, addressCodeEditType.getValue(), cwb);
	}

	public void updateTuihuoBranchid(long branchid, String cwb) {
		this.jdbcTemplate.update("update express_ops_cwb_detail set tuihuoid=?  where state =1 and cwb=?", branchid, cwb);
	}

	public void updateCwbState(String cwb, CwbStateEnum cwbstate) {
		String sql = "update express_ops_cwb_detail set cwbstate=? where cwb=? and state=1";
		this.jdbcTemplate.update(sql, cwbstate.getValue(), cwb);
	}

	public void updateNextBranchid(String cwb, long nextbranchid) {
		String sql = "update express_ops_cwb_detail set nextbranchid=? where cwb=? and state=1";
		this.jdbcTemplate.update(sql, nextbranchid, cwb);
	}

	/**
	 * 修改客户备注信息
	 * 
	 * @param cwb
	 * @param cwbstate
	 */
	public void updateCwbRemark(String cwb, String csremark) {
		String sql = "update express_ops_cwb_detail set cwbremark=? where cwb=? and state=1 ";
		this.jdbcTemplate.update(sql, csremark, cwb);
	}

	public void updateDeliveryStateBycwb(String cwb, long deliverystate) {
		String sql = "update express_ops_cwb_detail set deliverystate=? where cwb=? and state=1 ";
		this.jdbcTemplate.update(sql, deliverystate, cwb);
	}

	public void saveIsaudit(String cwb, long isaudit) {
		this.jdbcTemplate.update("update express_ops_cwb_detail set isaudit=?  where state =1 and cwb=?", isaudit, cwb);
	}

	public List<JSONObject> getDetailForPrint(long groupid) {
		String sql = "SELECT c.customername,COUNT(1) cwbcount,COUNT(DISTINCT gd.baleid) balenum,SUM(cd.sendcarnum) goodscount "
				+ "FROM express_ops_cwb_detail cd RIGHT OUTER JOIN express_ops_groupdetail gd  ON cd.cwb=gd.cwb " + "LEFT JOIN express_set_customer_info c ON cd.customerid=c.`customerid` "
				+ "WHERE gd.groupid=? AND cd.state=1 GROUP BY cd.customerid ";
		return this.jdbcTemplate.query(sql, new CwbAndPrintMapper(), groupid);
	}

	public List<JSONObject> getDetailForChuKuPrint(String cwbs) {
		if (cwbs.length() == 0) {
			cwbs = "0";
		}
		String sql = "SELECT c.customername,cd.deliverid,COUNT(1) cwbcount,SUM(cd.sendcarnum) sendcarnum,SUM(cd.backcarnum) backcarnum,SUM(cd.caramount) caramount"
				+ ",SUM(cd.receivablefee) receivablefee,SUM(cd.paybackfee) paybackfee FROM  express_ops_cwb_detail cd  "
				+ "LEFT OUTER JOIN express_set_customer_info c ON cd.customerid=c.customerid  WHERE cd.cwb in(" + cwbs + ") and cd.state=1 GROUP BY cd.customerid ";
		return this.jdbcTemplate.query(sql, new CwbAndChuKuPrintMapper());
	}

	public List<JSONObject> getSomeDetailForPrint(long groupid) {
		String sql = "EXPLAIN SELECT c.customername,cd.cwb,cd.receivablefee FROM express_ops_cwb_detail cd " + "RIGHT OUTER JOIN express_ops_groupdetail gd  ON cd.cwb=gd.cwb  "
				+ "LEFT OUTER JOIN express_set_customer_info c ON cd.customerid=c.customerid WHERE gd.groupid=? AND cd.state=1 GROUP BY cd.customerid";
		return this.jdbcTemplate.query(sql, new CwbSomePrintMapper(), groupid);
	}

	public List<JSONObject> getSomeDetailForYZPrint(long groupid) {
		String sql = "SELECT c.customername,cd.cwb,cd.receivablefee,cd.sendcarnum FROM express_ops_cwb_detail cd " + "RIGHT OUTER JOIN express_ops_groupdetail  gd  ON cd.cwb=gd.cwb "
				+ "LEFT OUTER JOIN express_set_customer_info c ON cd.customerid=c.customerid WHERE gd.groupid=? AND cd.state=1 GROUP BY cd.customerid ";
		return this.jdbcTemplate.query(sql, new CwbSomeYZPrintMapper(), groupid);
	}

	public List<CwbOrder> getCwbByCwbs(String cwbs) {
		return this.jdbcTemplate.query("SELECT * from express_ops_cwb_detail where cwb in(" + cwbs + ") and state=1 ORDER BY CONVERT( consigneeaddress USING gbk ) COLLATE gbk_chinese_ci ASC",
				new CwbMapper());
	}

	public List<String> getCwbByOpscwbids(String opscwbids) {
		return this.jdbcTemplate.queryForList("SELECT cwb from express_ops_cwb_detail where opscwbid in(" + opscwbids
				+ ") and state=1 ORDER BY CONVERT( consigneeaddress USING gbk ) COLLATE gbk_chinese_ci ASC", String.class);
	}

	public long getCwbByCustomerid(long customerid, long carwarehouse) {
		String sql = "SELECT count(*) from express_ops_cwb_detail where state=1 ";
		if (customerid > 0) {
			sql += " and customerid=" + customerid;
		}
		sql += " and flowordertype in(" + FlowOrderTypeEnum.DaoRuShuJu.getValue() + "," + FlowOrderTypeEnum.TiHuo.getValue() + ") and carwarehouse=?";
		return this.jdbcTemplate.queryForInt(sql, carwarehouse);
	}

	public List<Map<String, Object>> getChukubyBranchid(long branchid, long nextbranchid, int cwbstate) {
		String sql = "SELECT COUNT(1) count,(CASE WHEN SUM(sendcarnum) IS NULL THEN 0 ELSE SUM(sendcarnum) END ) sum FROM express_ops_cwb_detail WHERE currentbranchid=? and flowordertype<>? and state=1";

		if ((nextbranchid == 0) || (nextbranchid == -1)) {
			sql += " and nextbranchid<>0 ";
		} else {
			sql += " and nextbranchid =" + nextbranchid;
		}
		if (cwbstate > -1) {
			sql += " and cwbstate=" + cwbstate;
		}
		return this.jdbcTemplate.queryForList(sql, branchid, FlowOrderTypeEnum.TiHuo.getValue());
	}

	public List<Map<String, Object>> getZhongZhuanZhanChukubyBranchid(long branchid, long nextbranchid) {
		String sql = "SELECT COUNT(1) count,(CASE WHEN SUM(sendcarnum) IS NULL THEN 0 ELSE SUM(sendcarnum) END ) sum "
				+ "FROM express_ops_cwb_detail WHERE currentbranchid=? and flowordertype=? and state=1 ";

		if (nextbranchid > 0) {
			sql += " and nextbranchid =" + nextbranchid;
		}
		return this.jdbcTemplate.queryForList(sql, branchid, FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue());
	}

	public long getZhanDianChuZhanbyBranchid(long branchid, long deliverybranchid, long flowordertype) {
		String sql = "SELECT COUNT(1) FROM express_ops_cwb_detail WHERE currentbranchid=? and deliverybranchid=? and flowordertype=? and state=1";

		return this.jdbcTemplate.queryForLong(sql, branchid, deliverybranchid, flowordertype);
	}

	/**
	 * 得到出库已出库数据
	 * 
	 * @param startbranchid
	 * @param deliverybranchid
	 * @param nextbranchid
	 * @param flowordertype
	 * @return
	 */
	public long getYiChuKubyBranchid(long startbranchid, long nextbranchid, long flowordertype) {
		String sql = "SELECT COUNT(1) FROM express_ops_cwb_detail WHERE startbranchid=? and flowordertype=? and state=1";
		if (nextbranchid > 0) {
			sql += " and nextbranchid=" + nextbranchid;
		}

		return this.jdbcTemplate.queryForLong(sql, startbranchid, flowordertype);
	}

	/**
	 * 得到出库已出库数据列表
	 * 
	 * @param startbranchid
	 * @param deliverybranchid
	 * @param nextbranchid
	 * @param flowordertype
	 * @return
	 */
	public List<CwbOrder> getYiChuKubyBranchidList(long startbranchid, long nextbranchid, long flowordertype, long page) {
		String sql = "SELECT * FROM express_ops_cwb_detail WHERE startbranchid=" + startbranchid + " and flowordertype=" + flowordertype + " and state=1 ";
		if (nextbranchid > 0) {
			sql += " and nextbranchid=" + nextbranchid;
		}

		sql += " limit ?,?";
		return this.jdbcTemplate.query(sql, new CwbMapper(), (page - 1) * Page.DETAIL_PAGE_NUMBER, Page.DETAIL_PAGE_NUMBER);
	}

	/**
	 * 不分页 得到出库已出库数据列表
	 * 
	 * @param startbranchid
	 * @param nextbranchid
	 * @param flowordertype
	 * @return
	 */
	public List<CwbOrder> getYiChuKubyBranchidList(long startbranchid, long nextbranchid, long flowordertype) {
		String sql = "SELECT * FROM express_ops_cwb_detail WHERE startbranchid=? and flowordertype=? and state=1";
		if (nextbranchid > 0) {
			sql += " and nextbranchid=" + nextbranchid;
		}
		return this.jdbcTemplate.query(sql, new CwbMapper(), startbranchid, flowordertype);
	}

	/**
	 * 得到站点出站已出站数据
	 * 
	 * @param startbranchid
	 * @param deliverybranchid
	 * @param nextbranchid
	 * @param flowordertype
	 * @return
	 */
	public long getZhanDianYiChuZhanbyBranchid(long startbranchid, long deliverybranchid, long nextbranchid, long flowordertype) {
		String sql = "SELECT COUNT(1) FROM express_ops_cwb_detail WHERE startbranchid=?  and nextbranchid=? and flowordertype=? and state=1";

		return this.jdbcTemplate.queryForLong(sql, startbranchid, nextbranchid, flowordertype);
	}

	/**
	 * 得到站点出站已出站数据列表
	 * 
	 * @param startbranchid
	 * @param deliverybranchid
	 * @param nextbranchid
	 * @param flowordertype
	 * @return
	 */
	public List<CwbOrder> getZhanDianYiChuZhanbyBranchidList(List<String> cwbs) {
		List<CwbOrder> orderlist = new ArrayList<CwbOrder>();
		if ((cwbs == null) || (cwbs.size() == 0)) {
			return orderlist;
		}
		StringBuffer sb = new StringBuffer();
		for (String s : cwbs) {
			sb.append("'");
			sb.append(s);
			sb.append("',");
		}
		String cwb = sb.length() == 0 ? "" : sb.substring(0, sb.length() - 1).toString();
		String sql = "SELECT * FROM express_ops_cwb_detail  WHERE cwb in(" + cwb + ")  and state=1";
		orderlist.addAll(this.jdbcTemplate.query(sql, new CwbMapper()));
		return orderlist;
	}

	public List<CwbOrder> getZhanDianChuZhanbyBranchidList(long branchid, long deliverybranchid, long flowordertype) {
		String sql = "SELECT * FROM express_ops_cwb_detail WHERE currentbranchid=? and deliverybranchid=? and flowordertype=? and state=1";

		return this.jdbcTemplate.query(sql, new CwbMapper(), branchid, deliverybranchid, flowordertype);
	}

	public List<CwbOrder> getChukuForCwbOrderByBranchid(long currentbranchid, int cwbstate, long page, long nextbranchid) {
		String sql = "";
		if (nextbranchid > 0) {
			sql = "SELECT * FROM express_ops_cwb_detail WHERE currentbranchid=" + currentbranchid + " and nextbranchid=" + nextbranchid + "  and flowordertype<>" + FlowOrderTypeEnum.TiHuo.getValue()
					+ " and state=1 ";
		} else {
			sql = "SELECT * FROM express_ops_cwb_detail WHERE currentbranchid=" + currentbranchid + " and nextbranchid<>0  and flowordertype<>" + FlowOrderTypeEnum.TiHuo.getValue() + " and state=1 ";
		}
		if (cwbstate > -1) {
			sql += " and cwbstate=" + cwbstate;
		}
		sql += " limit ?,?";
		return this.jdbcTemplate.query(sql, new CwbMapper(), (page - 1) * Page.DETAIL_PAGE_NUMBER, Page.DETAIL_PAGE_NUMBER);
	}

	public List<CwbOrder> getZhongZhuanZhanChukuForCwbOrderByBranchid(long currentbranchid, long page, long nextbranchid) {
		String sql = "SELECT * FROM express_ops_cwb_detail WHERE currentbranchid=" + currentbranchid + " and flowordertype=" + FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue() + " and state=1 ";
		if (nextbranchid > 0) {
			sql += " and nextbranchid=" + nextbranchid;
		}
		sql += " limit ?,?";
		return this.jdbcTemplate.query(sql, new CwbMapper(), (page - 1) * Page.DETAIL_PAGE_NUMBER, Page.DETAIL_PAGE_NUMBER);
	}

	/**
	 * 退供应商 未出库（所有）
	 * 
	 * @param branchid
	 * @return
	 */
	public List<CwbOrder> getTGYSCKListbyBranchid(long branchid) {
		String sql = "SELECT * FROM express_ops_cwb_detail WHERE currentbranchid =? and cwbstate=?  and flowordertype<>? and state=1 ";
		return this.jdbcTemplate.query(sql, new CwbMapper(), branchid, CwbStateEnum.TuiGongYingShang.getValue(), FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue());
	}

	public List<CwbOrder> getChukuForCwbOrder(long branchid, int cwbstate, long page) {
		String sql = "SELECT * FROM express_ops_cwb_detail WHERE currentbranchid=? and nextbranchid<>0 and flowordertype<>? and state=1";

		if (cwbstate > -1) {
			sql += " and cwbstate=" + cwbstate;
		}
		sql += " limit ?,?";
		return this.jdbcTemplate.query(sql, new CwbMapper(), (page - 1) * Page.DETAIL_PAGE_NUMBER, Page.DETAIL_PAGE_NUMBER);
	}

	public List<CwbOrder> getChukuForCwbOrder(long currentbranchid, int cwbstate) {
		String sql = "SELECT * FROM express_ops_cwb_detail WHERE currentbranchid=" + currentbranchid + " and nextbranchid<>0  and flowordertype<>" + FlowOrderTypeEnum.TiHuo.getValue()
				+ " and state=1 ";
		if (cwbstate > -1) {
			sql += " and cwbstate=" + cwbstate;
		}
		return this.jdbcTemplate.query(sql, new CwbMapper());
	}

	public List<CwbOrder> getKDKChukuForCwbOrder(long branchid, long nextbranchid, int cwbstate) {
		String sql = "SELECT * FROM express_ops_cwb_detail WHERE currentbranchid=? and flowordertype<>? and state=1";

		if ((nextbranchid == 0) || (nextbranchid == -1)) {
			sql += " and nextbranchid<>0 ";
		} else {
			sql += " and nextbranchid =" + nextbranchid;
		}

		if (cwbstate > -1) {
			sql += " and cwbstate=" + cwbstate;
		}
		return this.jdbcTemplate.query(sql, new CwbMapper(), branchid, FlowOrderTypeEnum.TiHuo.getValue());
	}

	public CwbOrder getDaoRubyBranchid(long branchid, long customerid) {
		String sql = "SELECT COUNT(1) as opscwbid,(CASE WHEN SUM(sendcarnum) IS NULL THEN 0 ELSE SUM(sendcarnum) END ) sendcarnum FROM express_ops_cwb_detail WHERE carwarehouse=? and state=1 and flowordertype=?";
		if (customerid > 0) {
			sql += " and customerid =" + customerid;
		}
		return this.jdbcTemplate.queryForObject(sql, new YiTongJiMapper(), branchid, FlowOrderTypeEnum.DaoRuShuJu.getValue());
	}

	public List<Map<String, Object>> getRukubyBranchid(long branchid, long sitetype, long customerid, long emaildateid) {
		if (sitetype == BranchEnum.ZhanDian.getValue()) {
			return this.jdbcTemplate.queryForList(
					"SELECT COUNT(1) count,(CASE WHEN SUM(sendcarnum) IS NULL THEN 0 ELSE SUM(sendcarnum) END ) sum FROM express_ops_cwb_detail WHERE nextbranchid =? and currentbranchid=0 and flowordertype='"
							+ FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "' and state=1 ", branchid);
		}
		String sql = "SELECT COUNT(1) count,(CASE WHEN SUM(sendcarnum) IS NULL THEN 0 ELSE SUM(sendcarnum) END ) sum FROM express_ops_cwb_detail WHERE  nextbranchid =? and currentbranchid=0 and state=1 and flowordertype<>"
				+ FlowOrderTypeEnum.FenZhanLingHuo.getValue() + " and flowordertype<>" + FlowOrderTypeEnum.YiFanKui.getValue();
		if (sitetype == BranchEnum.ZhongZhuan.getValue()) {
			sql = "SELECT COUNT(1) count,(CASE WHEN SUM(sendcarnum) IS NULL THEN 0 ELSE SUM(sendcarnum) END ) sum FROM express_ops_cwb_detail WHERE  nextbranchid =? and currentbranchid=0 and state=1 and flowordertype = "
					+ FlowOrderTypeEnum.ChuKuSaoMiao.getValue();
		}
		if (customerid > 0) {
			sql += " and customerid =" + customerid;
		}
		if (emaildateid > 0) {
			sql += " and emaildateid =" + emaildateid;
		}
		return this.jdbcTemplate.queryForList(sql, branchid);
	}

	public List<Map<String, Object>> getZhongZhuanZhanRukubyBranchid(long branchid, long sitetype, long customerid) {
		if (sitetype == BranchEnum.ZhanDian.getValue()) {
			return this.jdbcTemplate.queryForList(
					"SELECT COUNT(1) count,(CASE WHEN SUM(sendcarnum) IS NULL THEN 0 ELSE SUM(sendcarnum) END ) sum FROM express_ops_cwb_detail WHERE nextbranchid =? and currentbranchid=0 and flowordertype='"
							+ FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "' and state=1 ", branchid);
		}
		String sql = "SELECT COUNT(1) count,(CASE WHEN SUM(sendcarnum) IS NULL THEN 0 ELSE SUM(sendcarnum) END ) sum FROM express_ops_cwb_detail WHERE  nextbranchid =? and currentbranchid=0 and state=1 and flowordertype<>"
				+ FlowOrderTypeEnum.FenZhanLingHuo.getValue() + " and flowordertype<>" + FlowOrderTypeEnum.YiFanKui.getValue();
		if (sitetype == BranchEnum.ZhongZhuan.getValue()) {
			sql = "SELECT COUNT(1) count,(CASE WHEN SUM(sendcarnum) IS NULL THEN 0 ELSE SUM(sendcarnum) END ) sum FROM express_ops_cwb_detail WHERE  nextbranchid =? and currentbranchid=0 and state=1 and flowordertype = "
					+ FlowOrderTypeEnum.ChuKuSaoMiao.getValue();
		}
		if (customerid > 0) {
			sql += " and customerid =" + customerid;
		}
		return this.jdbcTemplate.queryForList(sql, branchid);
	}

	/**
	 * 已入库订单统计
	 * 
	 * @param branchid
	 * @param customerid
	 * @return
	 */
	public CwbOrder getYiRukubyBranchid(long branchid, long customerid, long emaildateid) {
		String sql = "SELECT COUNT(1) as opscwbid,(CASE WHEN SUM(sendcarnum) IS NULL THEN 0 ELSE SUM(sendcarnum) END ) sendcarnum FROM express_ops_cwb_detail WHERE currentbranchid=? and state=1 and flowordertype="
				+ FlowOrderTypeEnum.RuKu.getValue();
		if (customerid > 0) {
			sql += " and customerid =" + customerid;
		}
		if (emaildateid > 0) {
			sql += " and emaildateid =" + emaildateid;
		}
		return this.jdbcTemplate.queryForObject(sql, new YiTongJiMapper(), branchid);
	}

	/**
	 * 中转站已入库订单统计
	 * 
	 * @param branchid
	 * @param customerid
	 * @return
	 */
	public CwbOrder getZhongZhuanZhanYiRukubyBranchid(long branchid, long customerid) {
		String sql = "SELECT COUNT(1) as opscwbid,(CASE WHEN SUM(sendcarnum) IS NULL THEN 0 ELSE SUM(sendcarnum) END ) sendcarnum FROM express_ops_cwb_detail WHERE currentbranchid=? and state=1 and flowordertype="
				+ FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue();
		if (customerid > 0) {
			sql += " and customerid =" + customerid;
		}
		return this.jdbcTemplate.queryForObject(sql, new YiTongJiMapper(), branchid);
	}

	/**
	 * 已入库订单列表
	 * 
	 * @param branchid
	 * @param customerid
	 * @param page
	 * @return
	 */
	public List<CwbOrder> getYiRukubyBranchidList(long branchid, long customerid, long page, long emaildateid) {
		String sql = "SELECT * FROM express_ops_cwb_detail WHERE currentbranchid=? and state=1 and flowordertype=" + FlowOrderTypeEnum.RuKu.getValue();
		if (customerid > 0) {
			sql += " and customerid =" + customerid;
		}
		if (emaildateid > 0) {
			sql += " and emaildateid =" + emaildateid;
		}
		sql += " limit ?,?";
		return this.jdbcTemplate.query(sql, new CwbMapper(), branchid, (page - 1) * Page.DETAIL_PAGE_NUMBER, Page.DETAIL_PAGE_NUMBER);
	}

	/**
	 * 中转站已入库订单列表
	 * 
	 * @param branchid
	 * @param customerid
	 * @param page
	 * @return
	 */
	public List<CwbOrder> getZhongZhuanZhanYiRukubyBranchidList(long branchid, long customerid, long page) {
		String sql = "SELECT * FROM express_ops_cwb_detail WHERE currentbranchid=? and state=1 and flowordertype=" + FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue();
		if (customerid > 0) {
			sql += " and customerid =" + customerid;
		}
		sql += " limit ?,?";
		return this.jdbcTemplate.query(sql, new CwbMapper(), branchid, (page - 1) * Page.DETAIL_PAGE_NUMBER, Page.DETAIL_PAGE_NUMBER);
	}

	/**
	 * 已到货订单统计
	 * 
	 * @param branchid
	 * @return
	 */
	public CwbOrder getYiDaohuobyBranchid(long branchid) {
		String sql = "SELECT COUNT(1) as opscwbid,(CASE WHEN SUM(sendcarnum) IS NULL THEN 0 ELSE SUM(sendcarnum) END ) sendcarnum FROM express_ops_cwb_detail WHERE currentbranchid=? and state=1 and flowordertype="
				+ FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue();
		return this.jdbcTemplate.queryForObject(sql, new YiTongJiMapper(), branchid);
	}

	/**
	 * 已到货订单列表
	 * 
	 * @param branchid
	 * @param page
	 * @return
	 */
	public List<CwbOrder> getYiDaohuobyBranchidList(long branchid, long page) {
		String sql = "SELECT * FROM express_ops_cwb_detail WHERE currentbranchid=? and state=1 and flowordertype=" + FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue();
		sql += " limit ?,?";
		return this.jdbcTemplate.query(sql, new CwbMapper(), branchid, (page - 1) * Page.DETAIL_PAGE_NUMBER, Page.DETAIL_PAGE_NUMBER);
	}

	public String getYiDaohuobyBranchidListSql(long branchid) {
		String sql = "SELECT * FROM express_ops_cwb_detail WHERE currentbranchid=" + branchid + " and state=1 and flowordertype=" + FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue();
		return sql;
	}

	public long getJinRiWeiDaoHuoCount(long nextbranchid, String flowordertypes, String cwbs) {
		String sql = "SELECT COUNT(1) count FROM express_ops_cwb_detail WHERE nextbranchid =? and flowordertype in(" + flowordertypes + ") and state=1 and cwb in(" + cwbs + ")";
		return this.jdbcTemplate.queryForLong(sql, nextbranchid);
	}

	public long getJinRiWeiDaoHuoCount(String flowordertypes, long branchid, String cwbs) {
		String sql = "SELECT COUNT(1) count FROM express_ops_cwb_detail WHERE currentbranchid=" + branchid + " and  flowordertype in(" + flowordertypes + ") and state=1 and cwb in(" + cwbs + ")";
		return this.jdbcTemplate.queryForLong(sql);
	}

	public long getJinRiWeiDaoHuoCount(String flowordertypes, String cwbs) {
		String sql = "SELECT COUNT(1) count FROM express_ops_cwb_detail WHERE   flowordertype in(" + flowordertypes + ") and state=1 and cwb in(" + cwbs + ")";
		return this.jdbcTemplate.queryForLong(sql);
	}

	public long getYuyueCount(String flowordertypes, String cwbs) {
		String sql = "SELECT COUNT(1) count FROM express_ops_cwb_detail WHERE  flowordertype in(" + flowordertypes + ") and state=1 and cwb in(" + cwbs + ")  and customercommand like '%预约%'";
		return this.jdbcTemplate.queryForLong(sql);
	}

	public long getHistoryWeiDaoHuoCount(long nextbranchid, String flowordertypes, String cwbs) {
		String sql = "SELECT COUNT(1) count FROM express_ops_cwb_detail WHERE nextbranchid =? and flowordertype in(" + flowordertypes + ") and state=1  and cwb not in(" + cwbs + ")";
		return this.jdbcTemplate.queryForLong(sql, nextbranchid);
	}

	public List<CwbOrder> getDaoRuByBranchidForList(long branchid) {
		String sql = "SELECT * FROM express_ops_cwb_detail WHERE carwarehouse=? and state=1 and flowordertype=?";
		return this.jdbcTemplate.query(sql, new CwbMapper(), branchid, FlowOrderTypeEnum.DaoRuShuJu.getValue());
	}

	/**
	 * 用于获取未入库的订单列表
	 * 
	 * @param branchid
	 *            当前机构
	 * @param sitetype
	 *            当前机构类型
	 * @param customerid
	 * @param page
	 * @return
	 */
	public List<CwbOrder> getRukuByBranchidForList(long branchid, long sitetype, long page, long customerid, long emaildateid) {

		String sql = "SELECT * FROM express_ops_cwb_detail WHERE  nextbranchid =? and currentbranchid=0 and state=1 and flowordertype<>" + FlowOrderTypeEnum.FenZhanLingHuo.getValue();
		if (sitetype == BranchEnum.ZhanDian.getValue()) {
			sql = "SELECT * FROM express_ops_cwb_detail WHERE nextbranchid =? and currentbranchid=0 and flowordertype='" + FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "' and state=1 ";
		}
		if (sitetype == BranchEnum.ZhongZhuan.getValue()) {
			sql = "SELECT * FROM express_ops_cwb_detail WHERE  nextbranchid =? and currentbranchid=0 and state=1 and flowordertype=" + FlowOrderTypeEnum.ChuKuSaoMiao.getValue();
		}
		if (customerid > 0) {
			sql += " and customerid=" + customerid;
		}
		if (emaildateid > 0) {
			sql += " and emaildateid =" + emaildateid;
		}
		sql += " limit ?,? ";
		return this.jdbcTemplate.query(sql, new CwbMapper(), branchid, (page - 1) * Page.DETAIL_PAGE_NUMBER, Page.DETAIL_PAGE_NUMBER);
	}

	/**
	 * 用于获取中转站未入库的订单列表
	 * 
	 * @param branchid
	 *            当前机构
	 * @param sitetype
	 *            当前机构类型
	 * @param customerid
	 * @param page
	 * @return
	 */
	public List<CwbOrder> getZhongZhuanZhanRukuByBranchidForList(long branchid, long sitetype, long page, long customerid) {

		String sql = "SELECT * FROM express_ops_cwb_detail WHERE  nextbranchid =? and currentbranchid=0 and state=1 and flowordertype<>" + FlowOrderTypeEnum.FenZhanLingHuo.getValue();
		if (sitetype == BranchEnum.ZhanDian.getValue()) {
			sql = "SELECT * FROM express_ops_cwb_detail WHERE nextbranchid =? and currentbranchid=0 and flowordertype='" + FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "' and state=1 ";
		}
		if (sitetype == BranchEnum.ZhongZhuan.getValue()) {
			sql = "SELECT * FROM express_ops_cwb_detail WHERE  nextbranchid =? and currentbranchid=0 and state=1 and flowordertype=" + FlowOrderTypeEnum.ChuKuSaoMiao.getValue();
		}
		if (customerid > 0) {
			sql += " and customerid=" + customerid;
		}
		sql += " limit ?,? ";
		return this.jdbcTemplate.query(sql, new CwbMapper(), branchid, (page - 1) * Page.DETAIL_PAGE_NUMBER, Page.DETAIL_PAGE_NUMBER);
	}

	public List<CwbOrder> getJinRiDaoHuoByBranchidForList(long nextbranchid, String flowordertypes, long page, String cwbs) {
		String sql = "SELECT * FROM express_ops_cwb_detail WHERE nextbranchid =? and flowordertype in(" + flowordertypes + ") and state=1 and cwb in(" + cwbs + ") limit ?,? ";
		return this.jdbcTemplate.query(sql, new CwbMapper(), nextbranchid, (page - 1) * Page.DETAIL_PAGE_NUMBER, Page.DETAIL_PAGE_NUMBER);
	}

	public List<CwbOrder> getJinRiDaoHuoByBranchidForList(String flowordertypes, long page, String cwbs) {
		String sql = "SELECT * FROM express_ops_cwb_detail WHERE  flowordertype in(" + flowordertypes + ") and state=1 and cwb in(" + cwbs + ") limit ?,? ";
		return this.jdbcTemplate.query(sql, new CwbMapper(), (page - 1) * Page.DETAIL_PAGE_NUMBER, Page.DETAIL_PAGE_NUMBER);
	}

	public List<CwbOrder> getHistoryDaoHuoByBranchidForList(long nextbranchid, String flowordertypes, long page, String cwbs) {
		String sql = "SELECT * FROM express_ops_cwb_detail WHERE nextbranchid =? and flowordertype in(" + flowordertypes + ") and state=1  and cwb not in(" + cwbs + ")  limit ?,? ";
		return this.jdbcTemplate.query(sql, new CwbMapper(), nextbranchid, (page - 1) * Page.DETAIL_PAGE_NUMBER, Page.DETAIL_PAGE_NUMBER);
	}

	public List<CwbOrder> getHistoryDaoHuoByBranchidForList(String flowordertypes, long branchid, long page, String cwbs) {
		String sql = "SELECT * FROM express_ops_cwb_detail WHERE currentbranchid=" + branchid + " and flowordertype in(" + flowordertypes + ") and state=1  and cwb  in(" + cwbs + ")  limit ?,? ";
		return this.jdbcTemplate.query(sql, new CwbMapper(), (page - 1) * Page.DETAIL_PAGE_NUMBER, Page.DETAIL_PAGE_NUMBER);
	}

	public List<CwbOrder> getHistoryDaoHuoByBranchidForList(String flowordertypes, long page, String cwbs) {
		String sql = "SELECT * FROM express_ops_cwb_detail WHERE  flowordertype in(" + flowordertypes + ") and state=1  and cwb  in(" + cwbs + ")  limit ?,? ";
		return this.jdbcTemplate.query(sql, new CwbMapper(), (page - 1) * Page.DETAIL_PAGE_NUMBER, Page.DETAIL_PAGE_NUMBER);
	}

	public List<String> getJinRiDaoHuoByBranchidForListNoPage(long nextbranchid, String flowordertypes, String cwbs) {
		String sql = "SELECT cwb FROM express_ops_cwb_detail WHERE nextbranchid =? and flowordertype in(" + flowordertypes + ") and state=1 and cwb in(" + cwbs + ") ";
		return this.jdbcTemplate.queryForList(sql, String.class, nextbranchid);
	}

	public List<String> getJinRiDaoHuoByBranchidForListNoPage(String flowordertypes, String cwbs) {
		String sql = "SELECT cwb FROM express_ops_cwb_detail WHERE  flowordertype in(" + flowordertypes + ") and state=1 and cwb in(" + cwbs + ") ";
		return this.jdbcTemplate.queryForList(sql, String.class);
	}

	public List<String> getHistoryDaoHuoByBranchidForListNoPage(long nextbranchid, String flowordertypes, String cwbs) {
		String sql = "SELECT cwb FROM express_ops_cwb_detail WHERE nextbranchid =? and flowordertype in(" + flowordertypes + ") and state=1 and cwb not in(" + cwbs + ")";
		return this.jdbcTemplate.queryForList(sql, String.class, nextbranchid);
	}

	public List<String> getHistoryDaoHuoByBranchidForListNoPage(String flowordertypes, String cwbs) {
		String sql = "SELECT cwb FROM express_ops_cwb_detail WHERE  flowordertype in(" + flowordertypes + ") and state=1 and cwb not in(" + cwbs + ")";
		return this.jdbcTemplate.queryForList(sql, String.class);
	}

	/*
	 * public List<String> getDaoHuoByBranchidForListNoPage(long
	 * nextbranchid,long currentbranchid,long flowordertype){ String sql =
	 * "SELECT cwb FROM express_ops_cwb_detail WHERE nextbranchid ="
	 * +nextbranchid+" and flowordertype="+flowordertype+" and state=1 ";
	 * if(currentbranchid>-1){ sql += "and currentbranchid="+currentbranchid; }
	 * return jdbcTemplate.queryForList(sql,String.class); }
	 */

	public List<Map<String, Object>> getBackRukubyBranchid(long branchid) {
		return this.jdbcTemplate
				.queryForList(
						"SELECT COUNT(1) count,(CASE WHEN SUM(sendcarnum) IS NULL THEN 0 ELSE SUM(sendcarnum) END ) sum FROM express_ops_cwb_detail FORCE INDEX(detail_nextbranchid_idx) WHERE nextbranchid =? and currentbranchid=0 and flowordertype='"
								+ FlowOrderTypeEnum.TuiHuoChuZhan.getValue() + "' and state=1 ", branchid);
	}

	public Smtcount getBackRukubyBranchidsmt(long branchid) {
		String sql = "SELECT COUNT(1) count,"
				+ "(CASE when sum(CASE WHEN cwbordertypeid=1  THEN 1  else 0 END ) is null then 0 else sum(CASE WHEN cwbordertypeid=1  THEN 1  else 0 END ) end) as pscount,"
				+ "(CASE when sum(CASE WHEN cwbordertypeid=2  THEN 1  else 0 END ) is null then 0 else sum(CASE WHEN cwbordertypeid=2  THEN 1  else 0 END ) end) as smtcount,"
				+ "(CASE when sum(CASE WHEN cwbordertypeid=3  THEN 1  else 0 END ) is null then 0 else sum(CASE WHEN cwbordertypeid=3  THEN 1  else 0 END ) end) as smhcount"
				+ " FROM express_ops_cwb_detail FORCE INDEX(detail_nextbranchid_idx) ";
		sql += "WHERE nextbranchid =? and currentbranchid=0 and flowordertype='" + FlowOrderTypeEnum.TuiHuoChuZhan.getValue() + "' and state=1 ";
		return this.jdbcTemplate.queryForObject(sql, new SmtCountMapper(), branchid);
	}

	public Smtcount getBackAndChangeRukubyBranchids(String branchids, long flowordertype) {
		String sql = "SELECT COUNT(1) count,"
				+ "(CASE when sum(CASE WHEN cwbordertypeid=1  THEN 1  else 0 END ) is null then 0 else sum(CASE WHEN cwbordertypeid=1  THEN 1  else 0 END ) end) as pscount,"
				+ "(CASE when sum(CASE WHEN cwbordertypeid=2  THEN 1  else 0 END ) is null then 0 else sum(CASE WHEN cwbordertypeid=2  THEN 1  else 0 END ) end) as smtcount,"
				+ "(CASE when sum(CASE WHEN cwbordertypeid=3  THEN 1  else 0 END ) is null then 0 else sum(CASE WHEN cwbordertypeid=3  THEN 1  else 0 END ) end) as smhcount"
				+ " FROM express_ops_operation_time FORCE INDEX(OTime_nextbranchid_Idx) ";
		sql += "WHERE nextbranchid in(" + branchids + ") and flowordertype=" + flowordertype + " ";
		return this.jdbcTemplate.queryForObject(sql, new SmtCountMapper());
	}

	// 退货站已入库订单
	public long getBackYiRukubyBranchid(long branchid) {
		return this.jdbcTemplate.queryForLong("SELECT COUNT(1) FROM express_ops_cwb_detail WHERE currentbranchid=? and flowordertype=? and state=1 ", branchid,
				FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue());
	}

	public Smtcount getBackYiRukubyBranchidsmt(long branchid) {
		String sql = "SELECT COUNT(1) count,"
				+ "(CASE when sum(CASE WHEN cwbordertypeid=1  THEN 1  else 0 END ) is null then 0 else sum(CASE WHEN cwbordertypeid=1  THEN 1  else 0 END ) end) as pscount,"
				+ "(CASE when sum(CASE WHEN cwbordertypeid=2  THEN 1  else 0 END ) is null then 0 else sum(CASE WHEN cwbordertypeid=2  THEN 1  else 0 END ) end) as smtcount,"
				+ "(CASE when sum(CASE WHEN cwbordertypeid=3  THEN 1  else 0 END ) is null then 0 else sum(CASE WHEN cwbordertypeid=3  THEN 1  else 0 END ) end) as smhcount"
				+ " FROM express_ops_cwb_detail FORCE INDEX(detail_currentbranchid_idx) ";
		sql += " WHERE currentbranchid=? and flowordertype=? and state=1";

		return this.jdbcTemplate.queryForObject(sql, new SmtCountMapper(), branchid, FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue());
	}

	public Smtcount getBackAndChangeYiRukubyBranchids(String branchids, long flowordertype) {
		String sql = "SELECT COUNT(1) count,"
				+ "(CASE when sum(CASE WHEN cwbordertypeid=1  THEN 1  else 0 END ) is null then 0 else sum(CASE WHEN cwbordertypeid=1  THEN 1  else 0 END ) end) as pscount,"
				+ "(CASE when sum(CASE WHEN cwbordertypeid=2  THEN 1  else 0 END ) is null then 0 else sum(CASE WHEN cwbordertypeid=2  THEN 1  else 0 END ) end) as smtcount,"
				+ "(CASE when sum(CASE WHEN cwbordertypeid=3  THEN 1  else 0 END ) is null then 0 else sum(CASE WHEN cwbordertypeid=3  THEN 1  else 0 END ) end) as smhcount"
				+ " FROM express_ops_operation_time FORCE INDEX(OTime_Branchid_Idx) ";
		sql += " WHERE branchid in(" + branchids + ") and flowordertype=" + flowordertype + " ";

		return this.jdbcTemplate.queryForObject(sql, new SmtCountMapper());
	}

	// 退货站已入库订单列表
	public List<CwbOrder> getBackYiRukuListbyBranchid(long branchid, long page) {
		return this.jdbcTemplate.query("SELECT * FROM express_ops_cwb_detail WHERE currentbranchid=? and flowordertype=? and state=1 limit ?,?", new CwbMapper(), branchid,
				FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), (page - 1) * Page.DETAIL_PAGE_NUMBER, Page.DETAIL_PAGE_NUMBER);
	}

	/**
	 * 退货站已入库订单列表,按订单号查询
	 * 
	 * @param cwbs
	 * @return
	 */
	public List<CwbOrder> getListbyCwbs(String cwbs) {
		return this.jdbcTemplate.query("SELECT * FROM express_ops_cwb_detail WHERE cwb in(" + cwbs + ") and state=1", new CwbMapper());
	}

	public List<CwbOrder> getBackYiRukuListbyBranchid(long branchid, long page, long cwbordertypeid) {
		return this.jdbcTemplate.query("SELECT * FROM express_ops_cwb_detail WHERE currentbranchid=? and flowordertype=? and state=1 and cwbordertypeid=? limit ?,?", new CwbMapper(), branchid,
				FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), cwbordertypeid, (page - 1) * Page.DETAIL_PAGE_NUMBER, Page.DETAIL_PAGE_NUMBER);
	}

	// 退货入库list
	public List<CwbOrder> getBackRukuByBranchidForList(long branchid, long page) {
		return this.jdbcTemplate.query("SELECT * FROM express_ops_cwb_detail WHERE nextbranchid =? and currentbranchid=0 and flowordertype=? and state=1 limit ?,? ", new CwbMapper(), branchid,
				FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), (page - 1) * Page.DETAIL_PAGE_NUMBER, Page.DETAIL_PAGE_NUMBER);
	}

	public List<CwbOrder> getBackRukuByBranchidForList(long branchid, long page, long cwbordertypeid) {
		return this.jdbcTemplate.query("SELECT * FROM express_ops_cwb_detail WHERE nextbranchid =? and currentbranchid=0 and flowordertype=? and state=1 and cwbordertypeid=? limit ?,? ",
				new CwbMapper(), branchid, FlowOrderTypeEnum.TuiHuoChuZhan.getValue(), cwbordertypeid, (page - 1) * Page.DETAIL_PAGE_NUMBER, Page.DETAIL_PAGE_NUMBER);
	}

	public long getWeiLingbyBranchid(long branchid) {
		String sql = "SELECT COUNT(1) FROM express_ops_cwb_detail WHERE nextbranchid =? and currentbranchid=? and flowordertype in('" + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()
				+ "','" + FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "') and state=1 ";
		return this.jdbcTemplate.queryForLong(sql, branchid, branchid);
	}

	// 得到已领货统计数据
	public long getYiLingHuoCountbyBranchid(String cwbs, long startbranchid, long deliverid) {
		String sql = "SELECT COUNT(1) FROM express_ops_cwb_detail FORCE INDEX(detail_cwb_idx) WHERE startbranchid =? and currentbranchid=0 and flowordertype=? and state=1 and cwb in(" + cwbs + ") ";
		if (deliverid > 0) {
			sql += " and deliverid=" + deliverid;
		}
		return this.jdbcTemplate.queryForLong(sql, startbranchid, FlowOrderTypeEnum.FenZhanLingHuo.getValue());
	}

	// 得到已领货统计数据
	public long getYuyuedaYiLingHuoCountbyBranchid(String cwbs, long startbranchid, long deliverid) {
		String sql = "SELECT COUNT(1) FROM express_ops_cwb_detail FORCE INDEX(detail_cwb_idx) WHERE startbranchid =? and currentbranchid=0 and flowordertype=? and state=1 and cwb in(" + cwbs
				+ ") and customercommand like '%预约%' ";
		if (deliverid > 0) {
			sql += " and deliverid=" + deliverid;
		}
		return this.jdbcTemplate.queryForLong(sql, startbranchid, FlowOrderTypeEnum.FenZhanLingHuo.getValue());
	}

	// 得到已领货数据列表
	public List<CwbOrder> getYiLingHuoListbyBranchidformingxi(String cwbs, long startbranchid, long deliverid, long page) {
		String sql = "SELECT * FROM express_ops_cwb_detail FORCE INDEX(detail_cwb_idx) WHERE startbranchid =" + startbranchid + " and currentbranchid=0 and flowordertype="
				+ FlowOrderTypeEnum.FenZhanLingHuo.getValue() + " and state=1 and cwb in(" + cwbs + ") ";
		if (deliverid > 0) {
			sql += " and deliverid=" + deliverid;
		}
		sql += " limit ?,?";
		return this.jdbcTemplate.query(sql, new CwbMapper(), (page - 1) * Page.DETAIL_PAGE_NUMBER, Page.DETAIL_PAGE_NUMBER);
	}

	public String getYiLingHuoListbyBranchidformingxiSql(String cwbs, long startbranchid, long deliverid) {
		String sql = "SELECT * FROM express_ops_cwb_detail FORCE INDEX(detail_cwb_idx) WHERE startbranchid =" + startbranchid + " and currentbranchid=0 and flowordertype="
				+ FlowOrderTypeEnum.FenZhanLingHuo.getValue() + " and state=1 and cwb in(" + cwbs + ") ";
		if (deliverid > 0) {
			sql += " and deliverid=" + deliverid;
		}
		return sql;
	}

	public List<CwbOrder> getYiLingHuoListbyBranchid(String cwbs, long startbranchid, long deliverid) {
		String sql = "SELECT * FROM express_ops_cwb_detail FORCE INDEX(detail_cwb_idx) WHERE startbranchid =? and currentbranchid=0 and flowordertype=? and state=1 and cwb in(" + cwbs + ") ";
		if (deliverid > 0) {
			sql += " and deliverid=" + deliverid;
		}
		return this.jdbcTemplate.query(sql, new CwbMapper(), startbranchid, FlowOrderTypeEnum.FenZhanLingHuo.getValue());
	}

	/**
	 * 已领货 没有订单号
	 * 
	 * @param startbranchid
	 * @param deliverid
	 * @return
	 */
	public List<CwbOrder> getYiLingHuoListbyBranchidForService(long startbranchid, long deliverid) {
		String sql = "SELECT * FROM express_ops_cwb_detail FORCE INDEX(detail_cwb_idx) WHERE startbranchid =? and currentbranchid=0 and flowordertype=? and state=1 ";
		if (deliverid > 0) {
			sql += " and deliverid=" + deliverid;
		}
		return this.jdbcTemplate.query(sql, new CwbMapper(), startbranchid, FlowOrderTypeEnum.FenZhanLingHuo.getValue());
	}

	// 今日到货待领货订单
	public List<CwbOrder> getTodayWeiLingDaohuobyBranchidformingxi(long branchid, String cwbs, long deliverid) {
		String sql = "SELECT * FROM express_ops_cwb_detail WHERE  currentbranchid=" + branchid + " and flowordertype in('" + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue() + "','"
				+ FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "') and state=1 and cwb in(" + cwbs + ")";
		if (deliverid > 0) {
			sql += " and deliverid=" + deliverid;
		}
		return this.jdbcTemplate.query(sql, new CwbMapper());
	}

	public List<CwbOrder> getTodayWeiLingDaohuobyBranchid(long branchid, String cwbs) {
		String sql = "SELECT * FROM express_ops_cwb_detail WHERE  currentbranchid=? and flowordertype in('" + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue() + "','"
				+ FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "') and state=1 and cwb in(" + cwbs + ")";
		return this.jdbcTemplate.query(sql, new CwbMapper(), branchid);
	}

	public List<String> getTodayWeiLingDaohuoCwbsbyBranchid(long branchid, String cwbs) {

		String sql = "SELECT cwb FROM express_ops_cwb_detail WHERE  currentbranchid=" + branchid + " and flowordertype in('" + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue() + "','"
				+ FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "') and state=1 and cwb in(" + cwbs + ")";

		/*
		 * String sql =
		 * "SELECT cwb FROM express_ops_cwb_detail WHERE   flowordertype in('" +
		 * FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue() + "','"
		 * + FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() +
		 * "') and state=1 and cwb in(" + cwbs + ")";
		 */
		return this.jdbcTemplate.queryForList(sql, String.class);
	}

	// 历史到货待领货订单
	public List<CwbOrder> getHistoryyWeiLingDaohuobyBranchidformingxi(long branchid, String cwbs, long deliverid) {
		String sql = "SELECT * FROM express_ops_cwb_detail WHERE  currentbranchid=" + branchid + " and flowordertype in('" + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue() + "','"
				+ FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "') and state=1 ";
		if (cwbs.length() > 0) {
			sql += " and cwb not in(" + cwbs + ")";
		}
		if (deliverid > 0) {
			sql += " and deliverid=" + deliverid;
		}
		return this.jdbcTemplate.query(sql, new CwbMapper());
	}

	public List<CwbOrder> getHistoryyWeiLingDaohuobyBranchid(long branchid, String cwbs) {
		String sql = "SELECT * FROM express_ops_cwb_detail WHERE  currentbranchid=? and flowordertype in('" + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue() + "','"
				+ FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "') and state=1 ";
		if (cwbs.length() > 0) {
			sql += " and cwb not in(" + cwbs + ")";
		}
		return this.jdbcTemplate.query(sql, new CwbMapper(), branchid);
	}

	public List<String> getHistoryyWeiLingDaohuocwbsbyBranchid(long branchid, String cwbs) {
		String sql = "SELECT cwb FROM express_ops_cwb_detail WHERE nextbranchid =" + branchid + " and currentbranchid=" + branchid + " and flowordertype in('"
				+ FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue() + "','" + FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "') and state=1 ";
		if (cwbs.length() > 0) {
			sql += " and cwb not in(" + cwbs + ")";
		}
		return this.jdbcTemplate.queryForList(sql, String.class);
	}

	// 今日滞留待领货订单
	public List<CwbOrder> getTodayWeiLingZhiliuByWhereListformingxi(long deliveryState, long currentbranchid, String cwbs, long deliverid) {
		String sql = "select * from express_ops_cwb_detail where deliverystate =" + deliveryState + " and currentbranchid=" + currentbranchid + " and state=1 and flowordertype="
				+ FlowOrderTypeEnum.YiShenHe.getValue() + " and cwb in(" + cwbs + ")";
		if (deliverid > 0) {
			sql += " and deliverid=" + deliverid;
		}
		return this.jdbcTemplate.query(sql, new CwbMapper());
	}

	public List<CwbOrder> getTodayWeiLingZhiliuByWhereList(long deliveryState, long currentbranchid, String cwbs) {
		String sql = "select * from express_ops_cwb_detail where deliverystate =? and currentbranchid=? and state=1 and flowordertype=? and cwb in(" + cwbs + ")";
		return this.jdbcTemplate.query(sql, new CwbMapper(), deliveryState, currentbranchid, FlowOrderTypeEnum.YiShenHe.getValue());
	}

	public List<String> getTodayWeiLingZhiliuCwbsByWhereList(long deliveryState, long currentbranchid, String cwbs, long deliverid) {
		String sql = "select cwb from express_ops_cwb_detail where deliverystate =" + deliveryState + " and currentbranchid=" + currentbranchid + " and state=1 and flowordertype="
				+ FlowOrderTypeEnum.YiShenHe.getValue() + " and cwb in(" + cwbs + ")";
		if (deliverid > 0) {
			sql += " and deliverid=" + deliverid;
		}
		return this.jdbcTemplate.queryForList(sql, String.class);
	}

	// 历史滞留待领货订单
	public List<CwbOrder> getHistoryWeiLingZhiliuByWhereListformingxi(long deliveryState, long currentbranchid, String cwbs, long deliverid) {
		String sql = "select * from express_ops_cwb_detail where deliverystate =" + deliveryState + " and currentbranchid=" + currentbranchid + " and state=1 and flowordertype="
				+ FlowOrderTypeEnum.YiShenHe.getValue();
		if (cwbs.length() > 0) {
			sql += " and cwb not in(" + cwbs + ")";
		}
		if (deliverid > 0) {
			sql += " and deliverid=" + deliverid;
		}
		return this.jdbcTemplate.query(sql, new CwbMapper());
	}

	public List<CwbOrder> getHistoryWeiLingZhiliuByWhereList(long deliveryState, long currentbranchid, String cwbs, long deliverid) {
		String sql = "select * from express_ops_cwb_detail where deliverystate =" + deliveryState + " and currentbranchid=" + currentbranchid + " and state=1 and flowordertype="
				+ FlowOrderTypeEnum.YiShenHe.getValue();
		if (cwbs.length() > 0) {
			sql += " and cwb in(" + cwbs + ")";
		}
		if (deliverid > 0) {
			sql += " and deliverid=" + deliverid;
		}
		return this.jdbcTemplate.query(sql, new CwbMapper());
	}

	public List<CwbOrder> getHistoryWeiLingZhiliuByWhereList(long deliveryState, long currentbranchid, String cwbs) {
		String sql = "select * from express_ops_cwb_detail where deliverystate =? and currentbranchid=? and state=1 and flowordertype=?";
		if (cwbs.length() > 0) {
			sql += " and cwb not in(" + cwbs + ")";
		}
		return this.jdbcTemplate.query(sql, new CwbMapper(), deliveryState, currentbranchid, FlowOrderTypeEnum.YiShenHe.getValue());
	}

	public List<String> getHistoryWeiLingZhiliuCwbsByWhereList(long deliveryState, long currentbranchid, String cwbs, long deliverid) {
		String sql = "select cwb from express_ops_cwb_detail where deliverystate =" + deliveryState + " and currentbranchid=" + currentbranchid + " and state=1 and flowordertype="
				+ FlowOrderTypeEnum.YiShenHe.getValue();
		if (cwbs.length() > 0) {
			sql += " and cwb  in(" + cwbs + ")";
		}
		if (deliverid > 0) {
			sql += " and deliverid=" + deliverid;
		}
		return this.jdbcTemplate.queryForList(sql, String.class);
	}

	public long getTGYSCKbyBranchid(long branchid) {
		String sql = "SELECT COUNT(1) FROM express_ops_cwb_detail WHERE currentbranchid =? and cwbstate=? and flowordertype <>? and state=1 ";
		return this.jdbcTemplate.queryForLong(sql, branchid, CwbStateEnum.TuiGongYingShang.getValue(), FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue());
	}

	public Smtcount getTGYSCKbyBranchidsmt(long branchid) {
		String sql = "SELECT COUNT(1) count,"
				+ "(CASE when sum(CASE WHEN cwbordertypeid=1  THEN 1  else 0 END ) is null then 0 else sum(CASE WHEN cwbordertypeid=1  THEN 1  else 0 END ) end) as pscount,"
				+ "(CASE when sum(CASE WHEN cwbordertypeid=2  THEN 1  else 0 END ) is null then 0 else sum(CASE WHEN cwbordertypeid=2  THEN 1  else 0 END ) end) as smtcount,"
				+ "(CASE when sum(CASE WHEN cwbordertypeid=3  THEN 1  else 0 END ) is null then 0 else sum(CASE WHEN cwbordertypeid=3  THEN 1  else 0 END ) end) as smhcount"
				+ " FROM express_ops_cwb_detail FORCE INDEX(detail_currentbranchid_idx) ";
		sql += " WHERE currentbranchid =? and cwbstate=? and flowordertype <>? and state=1 ";
		return this.jdbcTemplate.queryForObject(sql, new SmtCountMapper(), branchid, CwbStateEnum.TuiGongYingShang.getValue(), FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue());
	}

	/**
	 * 退供货商 未出库（有分页）
	 * 
	 * @param branchid
	 * @param page
	 * @return
	 */
	public List<CwbOrder> getTGYSCKListbyBranchid(long branchid, long page) {
		String sql = "SELECT * FROM express_ops_cwb_detail WHERE currentbranchid =? and cwbstate=?  and flowordertype<>? and state=1 ";
		sql += " limit " + ((page - 1) * Page.DETAIL_PAGE_NUMBER) + "," + Page.DETAIL_PAGE_NUMBER;
		return this.jdbcTemplate.query(sql, new CwbMapper(), branchid, CwbStateEnum.TuiGongYingShang.getValue(), FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue());
	}

	public List<CwbOrder> getTGYSCKListbyBranchid(long branchid, long page, long cwbordertypeid) {
		String sql = "SELECT * FROM express_ops_cwb_detail WHERE currentbranchid =? and cwbstate=?  and flowordertype<>? and state=1 and cwbordertypeid=?";
		sql += " limit " + ((page - 1) * Page.DETAIL_PAGE_NUMBER) + "," + Page.DETAIL_PAGE_NUMBER;
		return this.jdbcTemplate.query(sql, new CwbMapper(), branchid, CwbStateEnum.TuiGongYingShang.getValue(), FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue(), cwbordertypeid);
	}

	// 退供货商出库已出库数据
	public long getTGYSYCKbyBranchid(long branchid) {
		String sql = "SELECT COUNT(1) FROM express_ops_cwb_detail WHERE startbranchid=? and flowordertype=? and state=1 ";
		return this.jdbcTemplate.queryForLong(sql, branchid, FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue());
	}

	public List<CwbOrder> getRukuDetailbyBranchid(long branchid, long startbranchid, Page page) {
		if (startbranchid == 0) {
			return this.jdbcTemplate.query("SELECT * FROM express_ops_cwb_detail WHERE state=1 and currentbranchid=0 and nextbranchid =? limit ?,?", new CwbMapper(), branchid);
		}
		return this.jdbcTemplate
				.query("SELECT COUNT(1) count,(CASE WHEN SUM(sendcarnum) IS NULL THEN 0 ELSE SUM(sendcarnum) END ) sum FROM express_ops_cwb_detail WHERE  currentbranchid=0 and nextbranchid =? and startbranchid=? and state=1 ",
						new CwbMapper(), branchid, startbranchid);
	}

	public void saveCwbForBackreason(String cwb, String backreason, long backreasonid) {
		String sql = "update express_ops_cwb_detail set backreason='" + backreason + "',backreasonid=" + backreasonid + " where cwb='" + cwb + "'";
		this.jdbcTemplate.update(sql);
	}

	public void saveCwbForWeishuakareason(String cwb, String weishuakareason, long weishuakareasonid) {
		String sql = "update express_ops_cwb_detail set weishuakareason=?,weishuakareasonid=? where cwb=?";
		this.jdbcTemplate.update(sql, weishuakareason, weishuakareasonid, cwb);
	}

	// 保存货物丢失原因
	public void saveCwbForDiushireason(String cwb, String losereason, long losereasonid) {
		String sql = "update express_ops_cwb_detail set losereason=?,losereasonid=? where cwb=?";
		this.jdbcTemplate.update(sql, losereason, losereasonid, cwb);
	}

	public void saveCwbForBackReturnreason(String cwb, String backreturnreason, long backreturnreasonid) {
		String sql = "update express_ops_cwb_detail set backreturnreason=?,backreturnreasonid=? where cwb=?";
		this.jdbcTemplate.update(sql, backreturnreason, backreturnreasonid, cwb);
	}

	public void saveCwbForLeavereason(String cwb, String leavedreason, long leavedreasonid, int firstlevelreasonid) {
		String sql = "update express_ops_cwb_detail set leavedreason=?,leavedreasonid=?, firstlevelid=? where cwb=?";
		this.jdbcTemplate.update(sql, leavedreason, leavedreasonid, firstlevelreasonid, cwb);
	}
	
	public void saveCwbForChangereason(String cwb, String changereason, long changereasonid,long firstchangereasonid) {
		String sql = "update express_ops_cwb_detail set changereason=?,changereasonid=?,firstchangereasonid=? where cwb=?";
		this.jdbcTemplate.update(sql, changereason, changereasonid,firstchangereasonid, cwb);
	}

	private final class CwbCountAndSumByEmaildateIdMapper implements RowMapper<BigDecimal[]> {
		@Override
		public BigDecimal[] mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new BigDecimal[] { rs.getBigDecimal("num"), StringUtil.nullConvertToBigDecimal(rs.getBigDecimal("receivablefee")),
					StringUtil.nullConvertToBigDecimal(rs.getBigDecimal("paybackfee")) };

		}
	}

	/**
	 * 
	 * @param emaildateid
	 * @return String[] 0是单数 1是应收金额 2是应退金额
	 */
	public BigDecimal[] getCwbByEmailDateId(long emaildateid) {
		String sql = "SELECT count(1) as num, SUM(receivablefee) as receivablefee,SUM(paybackfee) as paybackfee  FROM express_ops_cwb_detail WHERE emaildateid=? and state=1";
		return this.jdbcTemplate.queryForObject(sql, new CwbCountAndSumByEmaildateIdMapper(), emaildateid);

	}

	/**
	 * 获得大于toDay时间的对应emailFinishFlag状态的branchid机构的customerid供货商的订单与金额的汇总
	 * 
	 * @param emailFinishFlag
	 *            对应EmailFinishFlagEnum
	 * @param branchid
	 *            机构编号
	 * @param customerid
	 *            供货商编号
	 * @param toDay
	 *            时间
	 * @return String[] 0是单数 1是应收金额 2是应退金额
	 */
	public BigDecimal[] getCwbByEmailFinishFlagAndBranchidAndCustomerList(long emailFinishFlag, long branchid, long customerid, String toDay) {
		String sql = "SELECT count(1) as num, SUM(receivablefee) as receivablefee,SUM(paybackfee) as paybackfee  FROM express_ops_cwb_detail WHERE credate>? and emailfinishflag=? and carwarehouse=? and customerid=? and state=1";
		return this.jdbcTemplate.queryForObject(sql, new CwbCountAndSumByEmaildateIdMapper(), toDay, emailFinishFlag, branchid, customerid);

	}

	public String getSQLExport(long datetype, String begindate, String enddate, long customerid, String commonnumber, long customerwarehouseid, long startbranchid, long nextbranchid,
			long cwbordertypeid, String orderflowcwbs, long currentBranchid, long dispatchbranchid, long kufangid, long paywayid, long dispatchdeliveryid, String consigneename,
			String consigneemobile, long beginWatht, long endWatht, long beginsendcarnum, long endsendcarnum, String carsize, long flowordertype, String[] deliverystates, String packagecode, long page) {
		String sql = "select * from express_ops_cwb_detail";
		sql = this.getCwbOrderByPageWhereSqlHmjQ(sql, datetype, begindate, enddate, customerid, commonnumber, customerwarehouseid, startbranchid, nextbranchid, cwbordertypeid, orderflowcwbs,
				currentBranchid, dispatchbranchid, kufangid, paywayid, dispatchdeliveryid, consigneename, consigneemobile, beginWatht, endWatht, beginsendcarnum, endsendcarnum, carsize,
				flowordertype, deliverystates, packagecode);

		sql += " limit " + page + " ," + Page.EXCEL_PAGE_NUMBER;
		return sql;
	}

	public String getSQLExportHuiZong(long page, String begindate, String enddate, String customeridStr, String startbranchids, String nextbranchids, String cwbordertypeids, String orderflowcwbs,
			String currentBranchids, String dispatchbranchids, String kufangids, long flowordertype, long paywayid, long sign, Integer paybackfeeIsZero, String servicetype) {
		String sql = "select * from express_ops_cwb_detail";

		sql = this.getCwbOrderByPageWhereSqlHuiZong(sql, begindate, enddate, customeridStr, startbranchids, nextbranchids, cwbordertypeids, orderflowcwbs, currentBranchids, dispatchbranchids,
				kufangids, flowordertype, paywayid, sign, paybackfeeIsZero, servicetype);

		sql += " limit " + page + " ," + Page.EXCEL_PAGE_NUMBER;
		return sql;
	}

	public String getSQLExportKeFu(String cwbs) {
		String sql = "select * from express_ops_cwb_detail where cwb in(" + cwbs + ") and state=1 ";
		return sql;
	}

	public long getcwborderCountHmjQ(long datetype, String begindate, String enddate, long customerid, String commonnumber, String deliverystateCwbs, long customerwarehouseid, long startbranchid,
			long nextbranchid, long cwbordertypeid, String orderflowcwbs, String deliverycwbs, long currentBranchid, long dispatchbranchid, long kufangid, long paywayid, long dispatchdeliveryid,
			String consigneename, String consigneemobile, long beginWeight, long endWeight, long beginsendcarnum, long endsendcarnum, String carsize, long flowordertype, String[] deliverystates,
			String packagecode) {
		String sql = "select count(1) from express_ops_cwb_detail";
		sql = this.getCwbOrderByPageWhereSqlHmjQ(sql, datetype, begindate, enddate, customerid, commonnumber, customerwarehouseid, startbranchid, nextbranchid, cwbordertypeid, orderflowcwbs,
				currentBranchid, dispatchbranchid, kufangid, paywayid, dispatchdeliveryid, consigneename, consigneemobile, beginWeight, endWeight, beginsendcarnum, endsendcarnum, carsize,
				flowordertype, deliverystates, packagecode);
		try {
			return this.jdbcTemplate.queryForInt(sql);
		} catch (DataAccessException e) {
			return 0;
		}
	}

	public long getcwborderCountHuiZong(String begindate, String enddate, String customerids, String startbranchids, String nextbranchids, String cwbordertypeids, String orderflowcwbs,
			String currentBranchid, String dispatchbranchids, String kufangids, long flowordertype, long paywayid, long sign, String servicetype) {
		String sql = "select count(1) from express_ops_cwb_detail";
		sql = this.getCwbOrderByPageWhereSqlHuiZong(sql, begindate, enddate, customerids, startbranchids, nextbranchids, cwbordertypeids, orderflowcwbs, currentBranchid, dispatchbranchids, kufangids,
				flowordertype, paywayid, sign, -1, servicetype);
		try {
			return this.jdbcTemplate.queryForInt(sql);
		} catch (DataAccessException e) {
			return 0;
		}
	}

	// 分站到货统计统计订单数
	public long getcwborderDaoHuoCount(String customerids, String cwbordertypeids, String orderflowcwbs, String kufangids, String flowordertypes) {
		String sql = "select count(1) from express_ops_cwb_detail where cwb in (" + orderflowcwbs + ") and state=1 ";

		if ((customerids.length() > 0) || (cwbordertypeids.length() > 0) || (kufangids.length() > 0) || (flowordertypes.length() > 0)) {

			StringBuffer w = new StringBuffer();
			if (customerids.length() > 0) {
				w.append(" and customerid in(" + customerids + ")");
			}

			if (kufangids.length() > 0) {
				w.append(" and carwarehouse in(" + kufangids + ")");
			}
			if (cwbordertypeids.length() > 0) {
				w.append(" and cwbordertypeid in(" + cwbordertypeids + ")");
			}
			if (flowordertypes.length() > 0) {
				w.append(" and flowordertype in(" + flowordertypes + ")");
			}
			sql += w.toString();
		}
		try {
			return this.jdbcTemplate.queryForInt(sql);
		} catch (DataAccessException e) {
			return 0;
		}
	}

	/**
	 * 库房在途订单汇总按出库时间查询出订单号
	 */
	public List<String> getzaitucwborderCwb(String startbranchids, String nextbranchids, String cwbordertypeids, long flowordertype, String cwbs) {
		String sql = "SELECT cwb FROM express_ops_cwb_detail WHERE state=1 and cwb in(" + cwbs + ")";
		if ((startbranchids.length() > 0) || (nextbranchids.length() > 0) || (cwbordertypeids.length() > 0) || (flowordertype > 0)) {
			StringBuffer w = new StringBuffer();

			if (startbranchids.length() > 0) {
				w.append(" and startbranchid in(" + startbranchids + ")");
			}
			if (nextbranchids.length() > 0) {
				w.append(" and nextbranchid in(" + nextbranchids + ")");
			}
			if (cwbordertypeids.length() > 0) {
				w.append(" and cwbordertypeid in( " + cwbordertypeids + ")");
			}
			if (flowordertype > 0) {
				w.append(" and flowordertype = " + flowordertype);
			}
			sql += w.toString();
		}
		return this.jdbcTemplate.queryForList(sql, String.class);
	}

	/**
	 * 库房在途订单汇总按发货时间查询订单总数
	 */
	public long getzaitucwborderCount(String begindate, String enddate, String startbranchids, String nextbranchids, String cwbordertypeids, long flowordertype) {
		String sql = "SELECT count(1) FROM express_ops_cwb_detail WHERE state=1 and emaildate >=? and emaildate <= ?";
		if ((startbranchids.length() > 0) || (nextbranchids.length() > 0) || (cwbordertypeids.length() > 0) || (flowordertype > 0)) {
			StringBuffer w = new StringBuffer();

			if (startbranchids.length() > 0) {
				w.append(" and startbranchid in(" + startbranchids + ")");
			}
			if (nextbranchids.length() > 0) {
				w.append(" and nextbranchid in(" + nextbranchids + ")");
			}
			if (cwbordertypeids.length() > 0) {
				w.append(" and cwbordertypeid in( " + cwbordertypeids + ")");
			}
			if (flowordertype > 0) {
				w.append(" and flowordertype = " + flowordertype);
			}
			sql += w.toString();
		}
		try {
			return this.jdbcTemplate.queryForLong(sql, begindate, enddate);
		} catch (DataAccessException e) {
			return 0;
		}
	}

	/**
	 * 库房在途订单汇总按发货时间查询出订单的总金额
	 */
	public CwbOrder getzaitucwborderSum(long datetype, String begindate, String enddate, String startbranchids, String nextbranchids, String cwbordertypeids, long flowordertype) {
		String sql = "SELECT sum(receivablefee) as receivablefee,sum(paybackfee) as paybackfee FROM express_ops_cwb_detail WHERE state=1 and emaildate >=? and emaildate <=?";
		if ((startbranchids.length() > 0) || (nextbranchids.length() > 0) || (cwbordertypeids.length() > 0) || (flowordertype > 0)) {
			StringBuffer w = new StringBuffer();

			if (startbranchids.length() > 0) {
				w.append(" and startbranchid in(" + startbranchids + ")");
			}
			if (nextbranchids.length() > 0) {
				w.append(" and nextbranchid in(" + nextbranchids + ")");
			}
			if (cwbordertypeids.length() > 0) {
				w.append(" and cwbordertypeid in( " + cwbordertypeids + ")");
			}
			if (flowordertype > 0) {
				w.append(" and flowordertype = " + flowordertype);
			}
			sql += w.toString();
		}
		try {
			return this.jdbcTemplate.queryForObject(sql, new CwbMOneyMapper(), begindate, enddate);
		} catch (DataAccessException e) {
			return new CwbOrder();
		}
	}

	/**
	 * 根据订单号查询出订单的总金额
	 */
	public CwbOrder getcwborderSumBycwbs(String cwbs) {
		String sql = "SELECT sum(receivablefee) as receivablefee,sum(paybackfee) as paybackfee FROM express_ops_cwb_detail WHERE state=1 and cwb in(" + cwbs + ")";
		try {
			return this.jdbcTemplate.queryForObject(sql, new CwbMOneyMapper());
		} catch (DataAccessException e) {
			return new CwbOrder();
		}
	}

	/**
	 * 库房在途订单汇总按发货时间查询出每页的订单list
	 */
	public List<CwbOrder> getzaitucwbOrderByPage(long page, String orderName, String begindate, String enddate, String startbranchids, String nextbranchids, String cwbordertypeids, long flowordertype) {
		String sql = "SELECT * FROM express_ops_cwb_detail WHERE state=1 and emaildate >=? and emaildate <= ?";
		if ((startbranchids.length() > 0) || (nextbranchids.length() > 0) || (cwbordertypeids.length() > 0) || (flowordertype > 0)) {
			StringBuffer w = new StringBuffer();
			if (startbranchids.length() > 0) {
				w.append(" and startbranchid in(" + startbranchids + ")");
			}
			if (nextbranchids.length() > 0) {
				w.append(" and nextbranchid in(" + nextbranchids + ")");
			}
			if (cwbordertypeids.length() > 0) {
				w.append(" and cwbordertypeid in( " + cwbordertypeids + ")");
			}
			if (flowordertype > 0) {
				w.append(" and flowordertype = " + flowordertype);
			}
			sql += w.toString();
		}

		sql += " order by " + orderName + " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		return this.jdbcTemplate.query(sql, new CwbMapper(), begindate, enddate);
	}

	/**
	 * 库房在途订单汇总导出的sql
	 */
	public String getzaitucwbOrderSQL(long datetype, String begindate, String enddate, String startbranchids, String nextbranchids, String cwbordertypeids, long flowordertype) {
		String sql = "SELECT * FROM express_ops_cwb_detail WHERE state=1 and emaildate >='" + begindate + "' and emaildate <= '" + enddate + "'";
		if ((startbranchids.length() > 0) || (nextbranchids.length() > 0) || (cwbordertypeids.length() > 0) || (flowordertype > 0)) {
			StringBuffer w = new StringBuffer();

			if (startbranchids.length() > 0) {
				w.append(" and startbranchid in(" + startbranchids + ")");
			}
			if (nextbranchids.length() > 0) {
				w.append(" and nextbranchid in(" + nextbranchids + ")");
			}
			if (cwbordertypeids.length() > 0) {
				w.append(" and cwbordertypeid in( " + cwbordertypeids + ")");
			}
			if (flowordertype > 0) {
				w.append(" and flowordertype = " + flowordertype);
			}
			sql += w.toString();
		}

		return sql;
	}

	public long getcwborderCountHuiZong(String customerids, String cwbordertypeids, String orderflowcwbs, long flowordertype, long paywayid, String[] operationOrderResultTypes,
			Integer paybackfeeIsZero) {
		String sql = "select count(1) from express_ops_cwb_detail";
		sql = this.getCwbOrderByPageWhereSqlHuiZong(sql, customerids, cwbordertypeids, orderflowcwbs, flowordertype, paywayid, operationOrderResultTypes, paybackfeeIsZero);
		try {
			return this.jdbcTemplate.queryForInt(sql);
		} catch (DataAccessException e) {
			return 0;
		}
	}

	private final class CwbMOneyMapper implements RowMapper<CwbOrder> {
		@Override
		public CwbOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
			CwbOrder cwbOrder = new CwbOrder();
			cwbOrder.setReceivablefee(rs.getBigDecimal("receivablefee"));
			cwbOrder.setPaybackfee(rs.getBigDecimal("paybackfee"));
			return cwbOrder;
		}
	}

	public CwbOrder getcwborderSumHmjQ(long datetype, String begindate, String enddate, long customerid, String commonnumber, String deliverystateCwbs, long customerwarehouseid, long startbranchid,
			long nextbranchid, long cwbordertypeid, String orderflowcwbs, String deliverycwbs, long currentBranchid, long dispatchbranchid, long kufangid, long paywayid, long dispatchdeliveryid,
			String consigneename, String consigneemobile, long beginWeight, long endWeight, long beginsendcarnum, long endsendcarnum, String carsize, long flowordertype, String[] deliverystates,
			String packagecode) {
		String sql = "select sum(receivablefee) as receivablefee,sum(paybackfee) as paybackfee from express_ops_cwb_detail";
		sql = this.getCwbOrderByPageWhereSqlHmjQ(sql, datetype, begindate, enddate, customerid, commonnumber, customerwarehouseid, startbranchid, nextbranchid, cwbordertypeid, orderflowcwbs,
				currentBranchid, dispatchbranchid, kufangid, paywayid, dispatchdeliveryid, consigneename, consigneemobile, beginWeight, endWeight, beginsendcarnum, endsendcarnum, carsize,
				flowordertype, deliverystates, packagecode);
		try {
			return this.jdbcTemplate.queryForObject(sql, new CwbMOneyMapper());
		} catch (DataAccessException e) {
			return new CwbOrder();
		}
	}

	public CwbOrder getcwborderSumHuiZong(String begindate, String enddate, String customerids, String startbranchids, String nextbranchids, String cwbordertypeids, String orderflowcwbs,
			String currentBranchid, String dispatchbranchids, String kufangids, long flowordertype, long paywayid, long sign, String servicetype) {
		String sql = "select sum(receivablefee) as receivablefee,sum(paybackfee) as paybackfee from express_ops_cwb_detail";
		sql = this.getCwbOrderByPageWhereSqlHuiZong(sql, begindate, enddate, customerids, startbranchids, nextbranchids, cwbordertypeids, orderflowcwbs, currentBranchid, dispatchbranchids, kufangids,
				flowordertype, paywayid, sign, -1, servicetype);
		try {
			return this.jdbcTemplate.queryForObject(sql, new CwbMOneyMapper());
		} catch (DataAccessException e) {
			return new CwbOrder();
		}
	}

	// 分站到货统计查询总金额
	public CwbOrder getcwborderDaoHuoSum(String customerids, String cwbordertypeids, String orderflowcwbs, String kufangids, String flowordertypes) {
		String sql = "select sum(receivablefee) as receivablefee,sum(paybackfee) as paybackfee from express_ops_cwb_detail where cwb in (" + orderflowcwbs + ") and state=1 ";

		if ((customerids.length() > 0) || (cwbordertypeids.length() > 0) || (kufangids.length() > 0) || (flowordertypes.length() > 0)) {

			StringBuffer w = new StringBuffer();
			if (customerids.length() > 0) {
				w.append(" and customerid in(" + customerids + ")");
			}

			if (kufangids.length() > 0) {
				w.append(" and carwarehouse in(" + kufangids + ")");
			}
			if (cwbordertypeids.length() > 0) {
				w.append(" and cwbordertypeid in(" + cwbordertypeids + ")");
			}
			if (flowordertypes.length() > 0) {
				w.append(" and flowordertype in(" + flowordertypes + ")");
			}
			sql += w.toString();
		}
		try {
			return this.jdbcTemplate.queryForObject(sql, new CwbMOneyMapper());
		} catch (DataAccessException e) {
			return new CwbOrder();
		}
	}

	public CwbOrder getcwborderSumHuiZong(String customerids, String cwbordertypeids, String orderflowcwbs, long flowordertype, long paywayid, String[] operationOrderResultTypes,
			Integer paybackfeeIsZero) {
		String sql = "select sum(receivablefee) as receivablefee,sum(paybackfee) as paybackfee from express_ops_cwb_detail";
		sql = this.getCwbOrderByPageWhereSqlHuiZong(sql, customerids, cwbordertypeids, orderflowcwbs, flowordertype, paywayid, operationOrderResultTypes, paybackfeeIsZero);
		try {
			return this.jdbcTemplate.queryForObject(sql, new CwbMOneyMapper());
		} catch (DataAccessException e) {
			return new CwbOrder();
		}
	}

	private String getCwbOrderByPageWhereSqlHmjQ(String sql, long datetype, String begindate, String enddate, long customerid, String commonnumber, long customerwarehouseid, long startbranchid,
			long nextbranchid, long cwbordertypeid, String orderflowcwbs, long currentBranchid, long dispatchbranchid, long kufangid, long paywayid, long dispatchdeliveryid, String consigneename,
			String consigneemobile, long beginWeight, long endWeight, long beginsendcarnum, long endsendcarnum, String carsize, long flowordertype, String[] deliverystates, String packagecode) {

		if ((datetype > 0) || (begindate.length() > 0) || (enddate.length() > 0) || (customerid > 0) || (commonnumber.length() > 0) || (customerwarehouseid > 0) || (startbranchid > 0)
				|| (nextbranchid > 0) || (cwbordertypeid > 0) || (orderflowcwbs.length() > 0) || (currentBranchid > 0) || (dispatchbranchid > 0) || (kufangid > 0) || (paywayid > 0)
				|| (dispatchdeliveryid > 0) || (consigneename.length() > 0) || (consigneemobile.length() > 0) || (beginWeight > -1) || (endWeight > -1) || (beginsendcarnum > -1)
				|| (endsendcarnum > -1) || (carsize.length() > 0) || (flowordertype > 0) || ((deliverystates != null) && (deliverystates.length > 0)) || (packagecode.length() > 0)) {

			StringBuffer w = new StringBuffer();
			sql += " where ";
			if (datetype == 1) {
				if (begindate.length() > 0) {
					w.append(" and emaildate >='" + begindate + "'");
				}
				if (enddate.length() > 0) {
					w.append(" and emaildate <= '" + enddate + "'");
				}
			}
			if (orderflowcwbs.length() > 0) {
				w.append(" and cwb in (" + orderflowcwbs + ")");
			}
			if (customerid > 0) {
				w.append(" and customerid= " + customerid);
			}
			if (commonnumber.length() > 0) {
				w.append(" and commonnumber= '" + commonnumber + "'");
			}
			if (customerwarehouseid > 0) {
				w.append(" and customerwarehouseid=" + customerwarehouseid);
			}
			if (currentBranchid > 0) {
				w.append(" and currentbranchid=" + currentBranchid);
			}
			if (dispatchbranchid > 0) {
				w.append(" and deliverybranchid=" + dispatchbranchid);
			}
			if (kufangid > 0) {
				w.append(" and carwarehouse= " + kufangid);
			}
			if (startbranchid > 0) {
				w.append(" and startbranchid= " + startbranchid);
			}
			if (nextbranchid > 0) {
				w.append(" and nextbranchid= " + nextbranchid);
			}
			if (cwbordertypeid > 0) {
				w.append(" and cwbordertypeid= " + cwbordertypeid);
			}
			if (paywayid > 0) {
				w.append(" and newpaywayid = '" + paywayid + "'");
			}
			if (dispatchdeliveryid > 0) {
				w.append(" and deliverid =" + dispatchdeliveryid);
			}
			if (consigneename.length() > 0) {
				w.append(" and consigneename ='" + consigneename + "'");
			}
			if (consigneemobile.length() > 0) {
				w.append(" and (consigneemobile = '" + consigneemobile + "' or consigneephone = '" + consigneemobile + "' )");
			}

			if (beginWeight > -1) {
				w.append(" and carrealweight >= " + beginWeight);
			}
			if (endWeight > -1) {
				w.append(" and carrealweight <= " + endWeight);
			}
			if (beginsendcarnum > -1) {
				w.append(" and sendcarnum >= " + beginsendcarnum);
			}
			if (endsendcarnum > -1) {
				w.append(" and sendcarnum <= " + endsendcarnum);
			}
			if (carsize.length() > 0) {
				w.append(" and carsize = '" + carsize + "'");
			}
			if (flowordertype > 0) {
				w.append(" and flowordertype = " + flowordertype);
			}
			if ((deliverystates != null) && (deliverystates.length > 0)) {
				StringBuffer buffer = new StringBuffer();
				for (String string : deliverystates) {
					buffer.append(string).append(",");
				}
				buffer.deleteCharAt(buffer.length() - 1);
				w.append(" and deliverystate in (" + buffer + ")");
			}
			if (packagecode.length() > 0) {
				w.append(" and packagecode = '" + packagecode + "'");
			}
			w.append(" and state=1");
			sql += w.substring(4, w.length());
		} else {
			sql += " where state=1";
		}
		return sql;
	}

	private String getCwbOrderByPageWhereSqlHuiZong(String sql, String customeridStr, String cwbordertypeidStr, String orderflowcwbs, long flowordertype, long paywayid,
			String[] operationOrderResultTypes, Integer paybackfeeIsZero) {
		sql += " where cwb in (" + orderflowcwbs + ") and state=1 ";

		if ((customeridStr.length() > 0) || (cwbordertypeidStr.length() > 0) || (flowordertype > 0) || (paywayid > 0) || (paybackfeeIsZero > -1)) {
			StringBuffer w = new StringBuffer();

			if (customeridStr.length() > 0) {
				w.append(" and customerid in(" + customeridStr + ")");
			}

			if (cwbordertypeidStr.length() > 0) {
				w.append(" and cwbordertypeid in(" + cwbordertypeidStr + ")");
			}
			if (flowordertype > 0) {
				w.append(" and flowordertype = " + flowordertype);
			}
			if (paywayid > 0) {
				w.append(" and newpaywayid = '" + paywayid + "'");
			}

			if (paybackfeeIsZero > -1) {
				if (paybackfeeIsZero == 0) {
					w.append(" and receivablefee=0 ");
				} else {
					w.append(" and receivablefee>0 ");
				}
			}
			sql += w.toString();
		}
		return sql;
	}

	/**
	 * 获取相应条件对应的sql组合字符串
	 * 
	 * @param sql
	 * @param begindate
	 * @param enddate
	 * @param customeridStr
	 * @param startbranchidStr
	 * @param nextbranchidStr
	 * @param cwbordertypeidStr
	 * @param orderflowcwbs
	 * @param currentBranchidStr
	 * @param dispatchbranchidStr
	 * @param kufangidStr
	 * @param flowordertype
	 * @param paywayid
	 * @param sign
	 * @param paybackfeeIsZero
	 *            有没有代收货款的条件 -1 为全部 0为 没有代收货款 1为有代收货款
	 * @return
	 */
	private String getCwbOrderByPageWhereSqlHuiZong(String sql, String begindate, String enddate, String customeridStr, String startbranchidStr, String nextbranchidStr, String cwbordertypeidStr,
			String orderflowcwbs, String currentBranchidStr, String dispatchbranchidStr, String kufangidStr, long flowordertype, long paywayid, long sign, Integer paybackfeeIsZero, String servicetype) {

		if (sign == 7) {
			sql += " where emaildate >='" + begindate + "' and emaildate <= '" + enddate + "' and state=1 ";
		} else {
			sql += " where  cwb in (" + orderflowcwbs + ") and state=1 ";
		}

		if ((begindate.length() > 0) || (enddate.length() > 0) || (customeridStr.length() > 0) || (startbranchidStr.length() > 0) || (nextbranchidStr.length() > 0) || (cwbordertypeidStr.length() > 0)
				|| (currentBranchidStr.length() > 0) || (dispatchbranchidStr.length() > 0) || (kufangidStr.length() > 0) || (flowordertype > 0) || (paywayid > 0)) {
			StringBuffer w = new StringBuffer();
			if ((begindate.length() > 0) && (sign != 7)) {
				w.append(" and emaildate >='" + begindate + "'");
			}
			if ((enddate.length() > 0) && (sign != 7)) {
				w.append(" and emaildate <= '" + enddate + "'");
			}
			if (customeridStr.length() > 0) {
				w.append(" and customerid in(" + customeridStr + ")");
			}

			if (currentBranchidStr.length() > 0) {
				w.append(" and currentbranchid in(" + currentBranchidStr + ")");
			}
			if (dispatchbranchidStr.length() > 0) {
				w.append(" and deliverybranchid in(" + dispatchbranchidStr + ")");
			}
			if (kufangidStr.length() > 0) {
				w.append(" and carwarehouse in( " + kufangidStr + ")");
			}
			if (startbranchidStr.length() > 0) {
				w.append(" and startbranchid in(" + startbranchidStr + ")");
			}
			if (nextbranchidStr.length() > 0) {
				w.append(" and nextbranchid in(" + nextbranchidStr + ")");
			}
			if (cwbordertypeidStr.length() > 0) {
				w.append(" and cwbordertypeid in( " + cwbordertypeidStr + ")");
			}
			if ((servicetype.length() > 0) && !"全部".equals(servicetype)) {
				w.append(" and cartype = '" + servicetype + "'");
			}
			if (flowordertype > 0) {
				w.append(" and flowordertype = " + flowordertype);
			}
			if (paywayid > 0) {
				w.append(" and newpaywayid = '" + paywayid + "'");
			}
			if (paybackfeeIsZero > -1) {
				if (paybackfeeIsZero == 0) {
					w.append(" and receivablefee=0 ");
				} else {
					w.append(" and receivablefee>0 ");
				}
			}
			sql += w.toString();
		}
		return sql;
	}

	public List<CwbOrder> getcwbOrderByPageHmjQ(long page, long datetype, String begindate, String enddate, long customerid, String commonnumber, String orderName, long customerwarehouseid,
			long startbranchid, long nextbranchid, long cwbordertypeid, String orderflowcwbs, String deliverycwbs, long currentBranchid, long dispatchbranchid, long kufangid, long paywayid,
			long dispatchdeliveryid, String consigneename, String consigneemobile, long beginWeight, long endWeight, long beginsendcarnum, long endsendcarnum, String carsize, long flowordertype,
			String[] deliverystates, String packagecode) {
		String sql = "select * from express_ops_cwb_detail";
		sql = this.getCwbOrderByPageWhereSqlHmjQ(sql, datetype, begindate, enddate, customerid, commonnumber, customerwarehouseid, startbranchid, nextbranchid, cwbordertypeid, orderflowcwbs,
				currentBranchid, dispatchbranchid, kufangid, paywayid, dispatchdeliveryid, consigneename, consigneemobile, beginWeight, endWeight, beginsendcarnum, endsendcarnum, carsize,
				flowordertype, deliverystates, packagecode);
		sql += " order by " + orderName + " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		return this.jdbcTemplate.query(sql, new CwbMapper());
	}

	public List<CwbOrder> getcwbOrderByPageHuiZong(long page, String begindate, String enddate, String orderName, String customerids, String startbranchids, String nextbranchids,
			String cwbordertypeids, String orderflowcwbs, String currentBranchid, String dispatchbranchids, String kufangid, long flowordertype, long paywayid, long sign, String servicetype) {
		String sql = "select * from express_ops_cwb_detail";
		sql = this.getCwbOrderByPageWhereSqlHuiZong(sql, begindate, enddate, customerids, startbranchids, nextbranchids, cwbordertypeids, orderflowcwbs, currentBranchid, dispatchbranchids, kufangid,
				flowordertype, paywayid, sign, -1, servicetype);
		sql += " order by " + orderName + " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		return this.jdbcTemplate.query(sql, new CwbMapper());
	}

	// 分站到货统计查询订单list
	public List<CwbOrder> getDaoHuocwbOrderByPage(long page, String customerids, String cwbordertypeids, String orderflowcwbs, String kufangids, String flowordertypes) {
		String sql = "select * from express_ops_cwb_detail where cwb in (" + orderflowcwbs + ") and state=1 ";

		if ((customerids.length() > 0) || (cwbordertypeids.length() > 0) || (kufangids.length() > 0) || (flowordertypes.length() > 0)) {

			StringBuffer w = new StringBuffer();
			if (customerids.length() > 0) {
				w.append(" and customerid in(" + customerids + ")");
			}

			if (kufangids.length() > 0) {
				w.append(" and carwarehouse in(" + kufangids + ")");
			}
			if (cwbordertypeids.length() > 0) {
				w.append(" and cwbordertypeid in(" + cwbordertypeids + ")");
			}
			if (flowordertypes.length() > 0) {
				w.append(" and flowordertype in(" + flowordertypes + ")");
			}
			sql += w.toString();
		}

		sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		return this.jdbcTemplate.query(sql, new CwbMapper());
	}

	public List<CwbOrder> getcwbOrderByPageHuiZong(long page, String orderName, String customerids, String cwbordertypeids, String orderflowcwbs, long flowordertype, long paywayid,
			String[] operationOrderResultTypes, Integer paybackfeeIsZero) {
		String sql = "select * from express_ops_cwb_detail";
		sql = this.getCwbOrderByPageWhereSqlHuiZong(sql, customerids, cwbordertypeids, orderflowcwbs, flowordertype, paywayid, operationOrderResultTypes, paybackfeeIsZero);
		sql += " order by " + orderName + " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		return this.jdbcTemplate.query(sql, new CwbMapper());
	}

	/**
	 * 根据小件员Id和Cwb查询订单详情
	 * 
	 * @param deliverid
	 * @param cwb
	 * @return
	 */
	public CwbOrder getCwbDetailByCwbAndDeliverId(long deliverid, String cwb) {
		String sql = "";
		try {
			sql = "select * from express_ops_cwb_detail where  cwb=?  and state=1  ";
			if (deliverid != 0) {
				sql += " and deliverid=" + deliverid;
			}
			sql += " limit 1";
			return this.jdbcTemplate.queryForObject(sql, new CwbMapper(), cwb);
		} catch (Exception e) {
			this.logger.error("POS查询订单未检索到数据,订单号：" + cwb + ",deliverid=" + deliverid + ",sql：" + sql);
			return null;
		}

	}

	/**
	 * 撤销
	 * 
	 * @param deliverid
	 * @param cwb
	 */
	public void saveCwbDetailForCancel(String cwb) {
		String cwbsql = "update express_ops_cwb_detail set flowordertype=?,backreasonid=0,backreason='',leavedreasonid=0,leavedreason='' where cwb=? and state=1";
		this.jdbcTemplate.update(cwbsql, CwbFlowOrderTypeEnum.CheXiaoFanKui.getValue(), cwb);
	}

	public void saveCwbForRemark(String cwbremark, long multicwbnum, String cwb) {
		String sql = "update express_ops_cwb_detail set cwbremark=?,sendcarnum=? where cwb=? and state = 1 ";
		this.jdbcTemplate.update(sql, cwbremark, multicwbnum, cwb);
	}

	public List<CwbOrder> getcwbOrderByWherePage(long page, long branchid) {
		String sql = "select * from express_ops_cwb_detail where flowordertype=?";
		if (branchid > 0) {
			sql += " and nextbranchid=" + branchid;
		}
		sql += " order by nextbranchid asc,consigneeaddress desc limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		List<CwbOrder> cwborderList = this.jdbcTemplate.query(sql, new CwbMapper(), CwbFlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue());
		return cwborderList;
	}

	public long getcwborderByWherePageCount(long branchid) {
		String sql = "select count(1) from express_ops_cwb_detail where flowordertype=?";
		if (branchid > 0) {
			sql += " and nextbranchid=" + branchid;
		}
		return this.jdbcTemplate.queryForInt(sql, CwbFlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue());
	}

	// ===========================监控使用==============================================
	private final class CwbStatisticsMapper implements RowMapper<JSONObject> {
		@Override
		public JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
			JSONObject obj = new JSONObject();
			obj.put("num", rs.getString("num"));
			obj.put("receivablefee", rs.getString("receivablefee") == null ? 0 : rs.getString("receivablefee"));
			obj.put("paybackfee", rs.getString("paybackfee") == null ? 0 : rs.getString("paybackfee"));
			return obj;
		}
	}

	public List<JSONObject> getCwbByNextbranchidAndFlowordertypeToJson(long branchid, String flowordertype) {
		return this.jdbcTemplate.query(
				"select count(1) as num,sum(receivablefee) as receivablefee,sum(paybackfee) as paybackfee from express_ops_cwb_detail where nextbranchid=? and flowordertype in (" + flowordertype
						+ ") and state=1", new CwbStatisticsMapper(), branchid);
	}

	public List<JSONObject> getCwbByCurrentbranchidAndFlowordertypeToJson(long branchid) {
		return this.jdbcTemplate.query("select count(1) as num,sum(receivablefee) as receivablefee,sum(paybackfee) as paybackfee from express_ops_cwb_detail where currentbranchid=? and state=1",
				new CwbStatisticsMapper(), branchid);
	}

	public List<CwbOrder> getCwbOrderByNextBranchidAndFlowordertypeToPage(long page, long branchid, String flowordertype) {
		return this.jdbcTemplate.query("select * from express_ops_cwb_detail where nextbranchid=? and flowordertype in (" + flowordertype + ") and state=1 limit ?,?", new CwbMapper(), branchid,
				((page - 1) * Page.ONE_PAGE_NUMBER), Page.ONE_PAGE_NUMBER);
	}

	public long getCwbOrderByNextBranchidAndFlowordertypeCount(long branchid, String flowordertype) {
		return this.jdbcTemplate.queryForLong("select count(1) from express_ops_cwb_detail where nextbranchid=? and flowordertype in (" + flowordertype + ") and state=1 ", branchid);
	}

	public List<CwbOrder> getCwbOrderByCurrentbranchidAndFlowordertypeToPage(long page, long branchid) {
		return this.jdbcTemplate.query("select * from express_ops_cwb_detail where currentbranchid=? and state=1 limit ?,?", new CwbMapper(), branchid, ((page - 1) * Page.ONE_PAGE_NUMBER),
				Page.ONE_PAGE_NUMBER);
	}

	public long getCwbOrderByCurrentbranchidAndFlowordertypeCount(long branchid) {
		return this.jdbcTemplate.queryForLong("select count(1) from express_ops_cwb_detail where currentbranchid=? and state=1", branchid);
	}

	public List<CwbOrder> getCwbOrderByDeliverybranchidAndDeliverystateToPage(long page, long branchid, int deliverystate) {
		return this.jdbcTemplate.query("select cd.* from express_ops_delivery_state ds left join express_ops_cwb_detail cd "
				+ "on ds.cwb=cd.cwb where ds.deliverybranchid=? and ds.deliverystate=? and cd.state=1 and ds.state =1 limit ?,?", new CwbMapper(), branchid, deliverystate,
				((page - 1) * Page.ONE_PAGE_NUMBER), Page.ONE_PAGE_NUMBER);
	}

	public long getCwbOrderByDeliverybranchidAndDeliverystateCount(long branchid, int deliverystate) {
		return this.jdbcTemplate.queryForLong("select count(1) from express_ops_delivery_state ds left join express_ops_cwb_detail cd"
				+ " on ds.cwb=cd.cwb where ds.deliverybranchid=? and ds.deliverystate=? and cd.state=1 and ds.state =1 ", branchid, deliverystate);
	}

	public List<CwbOrder> getCwbByPackageCode(String packageCode) {
		return this.jdbcTemplate.query("select * from express_ops_cwb_detail where packageCode=? and state=1", new CwbMapper(), packageCode);
	}

	// ===========================监控使用=======END=======================================
	public List<CwbOrder> getCwbOrderByCwbs(long page, String cwbs) {
		return this.jdbcTemplate.query("select * from express_ops_cwb_detail where cwb in (" + cwbs + ") and state=1 limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER,
				new CwbMapper());
	}

	public long getCwbOrderCwbsCount(String cwbs) {
		return this.jdbcTemplate.queryForLong("select count(1) from express_ops_cwb_detail where cwb in (" + cwbs + ") and state=1 ");
	}

	// ==============货款结算===============
	/**
	 * 按供货商、反馈状态、反馈日期 查询订单数量、应收金额、应退金额
	 * 
	 * @param customerid
	 * @param deliverystate
	 * @param credate
	 * @return
	 */
	public List<CwbOrder> getCwbListByCustomeridAndFloworderTypeowAndCredate(long customerid, long flowordertype, String startCredate, String endCredate, long page) {
		return this.jdbcTemplate.query("select * from express_ops_cwb_detail  where  " + " customerid=? and flowordertype >= ? and emaildate >=?  and emaildate <=?  and state=1 limit "
				+ ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER, new CwbMapper(), customerid, flowordertype, startCredate, endCredate);
	}

	public long getCountByCustomeridAndFloworderTypeowAndCredate(long customerid, long flowordertype, String startCredate, String endCredate, long page) {
		return this.jdbcTemplate.queryForLong("select count(1) from express_ops_cwb_detail  where  " + " customerid=? and flowordertype >= ? and emaildate >=?  and emaildate <=?  and state=1 ",
				customerid, flowordertype, startCredate, endCredate);
	}

	// =====================库房日志统计 获取list=========begin===================
	/**
	 * 未入库 flowordertype = 1
	 * 
	 * @param branchid
	 * @param customerid
	 * @param flowordertype
	 * @param page
	 * @return
	 */
	public List<CwbOrder> getWeirukuList(long branchid, long customerid, String flowordertypes, long page) {
		String sql = " SELECT * FROM express_ops_cwb_detail" + " WHERE nextbranchid=?  and flowordertype in (" + flowordertypes + ") AND state=1 AND customerid=? limit ?,?";

		return this.jdbcTemplate.query(sql, new CwbMapper(), branchid, customerid, (page - 1) * Page.ONE_PAGE_NUMBER, Page.ONE_PAGE_NUMBER);
	}

	/**
	 * 未入库 flowordertype = 1 不限制供货商
	 * 
	 * @param branchid
	 * @param flowordertype
	 * @param page
	 * @return
	 */
	public List<CwbOrder> getWeirukuList(long branchid, String flowordertypes, long page) {
		String sql = " SELECT * FROM express_ops_cwb_detail" + " WHERE nextbranchid=? and flowordertype in (" + flowordertypes + ")  AND state=1 limit ?,?";

		return this.jdbcTemplate.query(sql, new CwbMapper(), branchid, (page - 1) * Page.ONE_PAGE_NUMBER, Page.ONE_PAGE_NUMBER);
	}

	/**
	 * 导出未入库的
	 * 
	 * @param branchid
	 * @param customerid
	 * @param flowordertype
	 * @return
	 */
	public String getWeirukuListSql(long branchid, long customerid, String flowordertypes) {
		String sql = " SELECT * FROM express_ops_cwb_detail" + " WHERE nextbranchid=" + branchid + "  and flowordertype in (" + flowordertypes + ")  AND state=1 AND customerid=" + customerid + " ";

		return sql;
	}

	/**
	 * 导出未入库的 全部供货商
	 * 
	 * @param branchid
	 * @param flowordertype
	 * @return
	 */
	public String getWeirukuListSql(long branchid, String flowordertypes) {
		String sql = " SELECT * FROM express_ops_cwb_detail" + " WHERE nextbranchid=" + branchid + " and flowordertype in (" + flowordertypes + ")   AND state=1  ";
		return sql;
	}

	/**
	 * 到错货 flowordertype =8
	 * 
	 * @param branchid
	 * @param customerid
	 * @param flowordertype
	 * @param page
	 * @return
	 */
	public List<CwbOrder> getDaocuohuoList(long branchid, long customerid, long flowordertype, long page) {
		String sql = " SELECT * FROM express_ops_cwb_detail" + " WHERE currentbranchid=? AND flowordertype=? AND state=1 AND customerid=? limit ?,?";

		return this.jdbcTemplate.query(sql, new CwbMapper(), branchid, flowordertype, customerid, (page - 1) * Page.ONE_PAGE_NUMBER, Page.ONE_PAGE_NUMBER);
	}

	/**
	 * 到错货 flowordertype =8 不限制供货商
	 * 
	 * @param branchid
	 * @param flowordertype
	 * @param page
	 * @return
	 */
	public List<CwbOrder> getDaocuohuoList(long branchid, long flowordertype, long page) {
		String sql = " SELECT * FROM express_ops_cwb_detail" + " WHERE currentbranchid=? AND flowordertype=? AND state=1  limit ?,?";

		return this.jdbcTemplate.query(sql, new CwbMapper(), branchid, flowordertype, (page - 1) * Page.ONE_PAGE_NUMBER, Page.ONE_PAGE_NUMBER);
	}

	/**
	 * 导出到错货 flowordertype =8
	 * 
	 * @param branchid
	 * @param customerid
	 * @param flowordertype
	 * @return
	 */
	public String getDaocuohuoListSql(long branchid, long customerid, long flowordertype) {
		String sql = " SELECT * FROM express_ops_cwb_detail" + " WHERE currentbranchid=" + branchid + " AND flowordertype=" + flowordertype + " AND state=1 AND customerid = " + customerid + " ";
		return sql;
	}

	/**
	 * 导出全部供货商的到错货 flowordertype =8
	 * 
	 * @param branchid
	 * @param flowordertype
	 * @return
	 */
	public String getDaocuohuoListSql(long branchid, long flowordertype) {
		String sql = " SELECT * FROM express_ops_cwb_detail" + " WHERE currentbranchid=" + branchid + " AND flowordertype=" + flowordertype + " AND state=1 ";
		return sql;
	}

	// 昨日出库在途 flowordertype =6
	public List<CwbOrder> getZuorichukuzaituList(long currentbranchid, long flowordertype, long customerid, String startTime, long page) {
		String sql = " SELECT cd.* " + "FROM  express_ops_cwb_detail cd LEFT JOIN  express_ops_order_flow of ON cd.`cwb`=of.`cwb` "
				+ "WHERE cd.flowordertype=? AND cd.startbranchid=? AND of.flowordertype=? " + " AND of.isnow=1 AND of.`credate`<? and cd.customerid=? and cd.state=1 limit ?,?";
		return this.jdbcTemplate.query(sql, new CwbMapper(), flowordertype, currentbranchid, flowordertype, startTime, customerid, (page - 1) * Page.ONE_PAGE_NUMBER, Page.ONE_PAGE_NUMBER);
	}

	/**
	 * 导出昨日出库在途 flowordertype =6
	 * 
	 * @param currentbranchid
	 * @param flowordertype
	 * @param customerid
	 * @param startTime
	 * @return
	 */
	public String getZuorichukuzaituListSql(long currentbranchid, String flowordertypes, long customerid, String startTime) {
		String sql = " SELECT cd.* " + "FROM  express_ops_cwb_detail cd LEFT JOIN  express_ops_order_flow of ON cd.`cwb`=of.`cwb` " + "WHERE cd.flowordertype in(" + flowordertypes
				+ ") AND cd.startbranchid=" + currentbranchid + " AND of.flowordertype in(" + flowordertypes + ") " + " AND of.isnow=1 AND  cd.state=1 and of.`credate`<'" + startTime
				+ "' and cd.customerid=" + customerid + " ";
		return sql;
	}

	/**
	 * 导出全部供货商 昨日出库在途 flowordertype =6
	 * 
	 * @param flowordertype
	 * @param customerid
	 * @param startTime
	 * @return
	 */
	public String getZuorichukuzaituListSql(long currentbranchid, String flowordertypes, String startTime) {
		String sql = " SELECT cd.* " + "FROM  express_ops_cwb_detail cd LEFT JOIN  express_ops_order_flow of ON cd.`cwb`=of.`cwb` " + "WHERE cd.flowordertype in(" + flowordertypes
				+ ") AND cd.startbranchid=" + currentbranchid + " AND of.flowordertype in(" + flowordertypes + ") " + " AND of.isnow=1 AND cd.state=1 and of.`credate`<'" + startTime + "' ";
		return sql;
	}

	/**
	 * 今日出库 flowordertype =6
	 * 
	 * @param currentbranchid
	 * @param flowordertype
	 * @param customerid
	 * @param startTime
	 * @param page
	 * @return
	 */
	public List<CwbOrder> getJinrichukuList(long currentbranchid, String flowordertypes, long customerid, String startTime, long page) {
		String sql = " SELECT DISTINCT cd.* " + "FROM  express_ops_cwb_detail cd LEFT JOIN  express_ops_order_flow of ON cd.`cwb`=of.`cwb` " + "WHERE of.branchid=? AND of.flowordertype in("
				+ flowordertypes + ") " + "  and of.`credate`>=? and cd.customerid=? AND  cd.state=1 limit ?,?";
		return this.jdbcTemplate.query(sql, new CwbMapper(), currentbranchid, startTime, customerid, (page - 1) * Page.ONE_PAGE_NUMBER, Page.ONE_PAGE_NUMBER);
	}

	/**
	 * 今日出库 flowordertype =6 不限制供货商
	 * 
	 * @param currentbranchid
	 * @param flowordertype
	 * @param startTime
	 * @param page
	 * @return
	 */
	public List<CwbOrder> getJinrichukuList(long currentbranchid, String flowordertypes, String startTime, long page) {
		String sql = " SELECT DISTINCT cd.* " + "FROM  express_ops_cwb_detail cd LEFT JOIN  express_ops_order_flow of ON cd.`cwb`=of.`cwb` " + "WHERE of.branchid=? AND of.flowordertype in("
				+ flowordertypes + ") " + "  and of.`credate`>=? AND  cd.state=1 limit ?,?";
		return this.jdbcTemplate.query(sql, new CwbMapper(), currentbranchid, startTime, (page - 1) * Page.ONE_PAGE_NUMBER, Page.ONE_PAGE_NUMBER);
	}

	// 今日出库在途 flowordertype =6
	public List<CwbOrder> getJinrichukuzaituList(long currentbranchid, long flowordertype, long customerid, String startTime, long page) {
		String sql = " SELECT cd.* " + "FROM  express_ops_cwb_detail cd LEFT JOIN  express_ops_order_flow of ON cd.`cwb`=of.`cwb` "
				+ "WHERE cd.flowordertype=? AND cd.startbranchid=? AND of.flowordertype=? " + " AND of.isnow=1 AND of.`credate`>=? and cd.customerid=? AND  cd.state=1 limit ?,?";
		return this.jdbcTemplate.query(sql, new CwbMapper(), flowordertype, currentbranchid, flowordertype, startTime, customerid, (page - 1) * Page.ONE_PAGE_NUMBER, Page.ONE_PAGE_NUMBER);
	}

	/**
	 * 导出今日出库在途 flowordertype =6
	 * 
	 * @param currentbranchid
	 * @param flowordertype
	 * @param customerid
	 * @param startTime
	 * @return
	 */
	public String getJinrichukuzaituListSql(long currentbranchid, String flowordertypes, long customerid, String startTime) {
		String sql = " SELECT DISTINCT cd.* " + "FROM  express_ops_cwb_detail cd LEFT JOIN  express_ops_order_flow of ON cd.`cwb`=of.`cwb` " + "WHERE  of.branchid=" + currentbranchid
				+ " AND of.flowordertype in(" + flowordertypes + ") " + "  AND of.`credate`>='" + startTime + "' and cd.customerid=" + customerid + " AND  cd.state=1";
		return sql;
	}

	/**
	 * 导出全部供货商今日出库在途 flowordertype =6
	 * 
	 * @param currentbranchid
	 * @param flowordertype
	 * @param startTime
	 * @return
	 */
	public String getJinrichukuzaituListSql(long currentbranchid, String flowordertypes, String startTime) {
		String sql = " SELECT DISTINCT cd.* " + "FROM  express_ops_cwb_detail cd LEFT JOIN  express_ops_order_flow of ON cd.`cwb`=of.`cwb` " + "WHERE  of.branchid=" + currentbranchid
				+ " AND of.flowordertype in(" + flowordertypes + ") " + "  AND of.`credate`>='" + startTime + "' AND  cd.state=1 ";
		return sql;
	}

	/**
	 * 今日库存
	 * 
	 * @param currentbranchid
	 * @param customerid
	 * @param page
	 * @return
	 */
	public List<CwbOrder> getJinriKucun(long currentbranchid, long customerid, long page) {
		String sql = "SELECT * FROM express_ops_cwb_detail WHERE currentbranchid=? AND state=1 AND customerid=? limit ?,?";
		return this.jdbcTemplate.query(sql, new CwbMapper(), currentbranchid, customerid, (page - 1) * Page.ONE_PAGE_NUMBER, Page.ONE_PAGE_NUMBER);
	}

	/**
	 * 今日库存 不限制供货商
	 * 
	 * @param currentbranchid
	 * @param customerid
	 * @param page
	 * @return
	 */
	public List<CwbOrder> getJinriKucun(long currentbranchid, long page) {
		String sql = "SELECT * FROM express_ops_cwb_detail WHERE currentbranchid=? AND state=1 limit ?,?";
		return this.jdbcTemplate.query(sql, new CwbMapper(), currentbranchid, (page - 1) * Page.ONE_PAGE_NUMBER, Page.ONE_PAGE_NUMBER);
	}

	/**
	 * 导出今日库存
	 * 
	 * @param currentbranchid
	 * @param customerid
	 * @return
	 */
	public String getJinriKucunSql(long currentbranchid, long customerid) {
		String sql = "SELECT * FROM express_ops_cwb_detail WHERE currentbranchid=" + currentbranchid + " AND state=1 AND customerid=" + customerid + " ";
		return sql;
	}

	/**
	 * 导出全部供货商今日库存
	 * 
	 * @param currentbranchid
	 * @param customerid
	 * @return
	 */
	public String getJinriKucunSql(long currentbranchid) {
		String sql = "SELECT * FROM express_ops_cwb_detail WHERE currentbranchid=" + currentbranchid + " AND state=1  ";
		return sql;
	}

	/**
	 * 库房 漏扫到站 flowordertype =6
	 * 
	 * @param flowordertype
	 * @param branchid
	 * @param customerid
	 * @param startTime
	 * @param page
	 * @return
	 */
	public List<CwbOrder> getLousaodaozhan(String flowordertypes, long branchid, long customerid, String startTime, long page) {
		String sql = "SELECT DISTINCT cd.* FROM  express_ops_cwb_detail cd LEFT JOIN  " + " express_ops_order_flow of ON cd.`cwb`=of.`cwb` "
				+ " WHERE  cd.state=1 AND of.branchid=? AND of.flowordertype in(" + flowordertypes + ") AND of.`credate`>=?  " + " AND of.`comment` = '系统自动处理' AND cd.customerid=?  limit ?,?";
		return this.jdbcTemplate.query(sql, new CwbMapper(), branchid, startTime, customerid, (page - 1) * Page.ONE_PAGE_NUMBER, Page.ONE_PAGE_NUMBER);
	}

	/**
	 * 库房 漏扫到站 flowordertype =6 不限制供货商
	 * 
	 * @param flowordertype
	 * @param branchid
	 * @param startTime
	 * @param page
	 * @return
	 */
	public List<CwbOrder> getLousaodaozhan(String flowordertypes, long branchid, String startTime, long page) {
		String sql = "SELECT DISTINCT cd.* FROM  express_ops_cwb_detail cd LEFT JOIN  " + " express_ops_order_flow of ON cd.`cwb`=of.`cwb` "
				+ " WHERE  cd.state=1 AND of.branchid=? AND of.flowordertype in(" + flowordertypes + ") AND of.`credate`>=?  " + " AND of.`comment` = '系统自动处理'  limit ?,?";
		return this.jdbcTemplate.query(sql, new CwbMapper(), branchid, startTime, (page - 1) * Page.ONE_PAGE_NUMBER, Page.ONE_PAGE_NUMBER);
	}

	// 站点 漏扫到站 flowordertype =6
	public List<CwbOrder> getLousaodaozhanByZhandian(long flowordertype, long branchid, String startTime, long page) {
		String sql = "SELECT DISTINCT cd.* FROM  express_ops_cwb_detail cd LEFT JOIN  " + " express_ops_order_flow of ON cd.`cwb`=of.`cwb` "
				+ " WHERE  cd.state=1 AND of.floworderdetail LIKE '%\"nextbranchid\":" + branchid + ",%'  AND of.flowordertype=? AND of.`credate`>=?  " + " AND of.`comment` = '系统自动处理'   limit ?,?";
		return this.jdbcTemplate.query(sql, new CwbMapper(), flowordertype, startTime, (page - 1) * Page.ONE_PAGE_NUMBER, Page.ONE_PAGE_NUMBER);
	}

	// 导出 站点 漏扫到站 flowordertype =6
	public String getLousaodaozhanByZhandianSql(long flowordertype, long branchid, String startTime) {
		String sql = "SELECT DISTINCT cd.* FROM  express_ops_cwb_detail cd LEFT JOIN  " + " express_ops_order_flow of ON cd.`cwb`=of.`cwb` "
				+ " WHERE  cd.state=1 AND of.floworderdetail LIKE '%\"nextbranchid\":" + branchid + ",%'  AND of.flowordertype=" + flowordertype + " AND of.`credate`>='" + startTime + "'  "
				+ " AND of.`comment` = '系统自动处理' ";
		return sql;
	}

	// 站点 漏扫到站的订单 flowordertype =6
	public List<CwbOrder> getLousaodaozhanByZhandianCwb(long flowordertype, long branchid, String startTime) {
		String sql = "SELECT DISTINCT cd.* FROM  express_ops_cwb_detail cd LEFT JOIN  " + " express_ops_order_flow of ON cd.`cwb`=of.`cwb` "
				+ " WHERE  cd.state=1 AND of.floworderdetail LIKE '%\"nextbranchid\":" + branchid + ",%'  AND of.flowordertype=? AND of.`credate`>=?  " + " AND of.`comment` = '系统自动处理'  ";
		return this.jdbcTemplate.query(sql, new CwbMapper(), flowordertype, startTime);
	}

	/**
	 * 导出漏扫到站 flowordertype =6
	 * 
	 * @param flowordertype
	 * @param branchid
	 * @param customerid
	 * @param startTime
	 * @return
	 */
	public String getLousaodaozhanSql(String flowordertypes, long branchid, long customerid, String startTime) {
		String sql = "SELECT DISTINCT cd.* FROM  express_ops_cwb_detail cd LEFT JOIN  " + " express_ops_order_flow of ON cd.`cwb`=of.`cwb` " + " WHERE  cd.state=1 AND of.branchid=" + branchid
				+ " AND of.flowordertype in(" + flowordertypes + ") " + " AND of.`credate`>='" + startTime + "'  " + " AND of.`comment` = '系统自动处理' AND cd.customerid=" + customerid + "  ";
		return sql;
	}

	/**
	 * 导出全部供货商漏扫到站 flowordertype =6
	 * 
	 * @param flowordertype
	 * @param branchid
	 * @param startTime
	 * @return
	 */
	public String getLousaodaozhanSql(String flowordertypes, long branchid, String startTime) {
		String sql = "SELECT DISTINCT cd.* FROM  express_ops_cwb_detail cd LEFT JOIN  " + " express_ops_order_flow of ON cd.`cwb`=of.`cwb` " + " WHERE  cd.state=1 AND of.branchid=" + branchid
				+ " AND of.flowordertype in(" + flowordertypes + ") " + " AND of.`credate`>='" + startTime + "'  " + " AND of.`comment` = '系统自动处理' ";
		return sql;
	}

	/**
	 * 所有漏扫到站的订单号 漏扫到站 flowordertype =6
	 * 
	 * @param flowordertype
	 * @param branchid
	 * @param customerid
	 * @param startTime
	 * @return
	 */
	public List<CwbOrder> getLousaodaozhanCwb(String flowordertypes, long branchid, long customerid, String startTime) {
		String sql = "SELECT DISTINCT cd.cwb as cwb FROM  express_ops_cwb_detail cd LEFT JOIN  " + " express_ops_order_flow of ON cd.`cwb`=of.`cwb` "
				+ " WHERE  cd.state=1 AND of.branchid=? AND of.flowordertype in(" + flowordertypes + ") " + " AND of.`credate`>=?  " + " AND of.`comment` = '系统自动处理' AND cd.customerid=? ";
		return this.jdbcTemplate.query(sql, new CwbMapper(), branchid, startTime, customerid);
	}

	/**
	 * 所有漏扫到站的订单号 漏扫到站 flowordertype =6 不限制供货商
	 * 
	 * @param flowordertype
	 * @param branchid
	 * @param startTime
	 * @return
	 */
	public List<CwbOrder> getLousaodaozhanCwb(String flowordertypes, long branchid, String startTime) {
		String sql = "SELECT DISTINCT cd.cwb as cwb FROM  express_ops_cwb_detail cd LEFT JOIN  " + " express_ops_order_flow of ON cd.`cwb`=of.`cwb` "
				+ " WHERE  cd.state=1 AND of.branchid=? AND of.flowordertype in(" + flowordertypes + ") " + " AND of.`credate`>=?  " + " AND of.`comment` = '系统自动处理'  ";
		return this.jdbcTemplate.query(sql, new CwbMapper(), branchid, startTime);
	}

	//
	/**
	 * 库房 出库已到站的list
	 * 
	 * @param flowordertype
	 *            7,8
	 * @param branchid
	 * @param customerid
	 * @param startTime
	 * @param cwbs
	 *            '5547609467','5545959063' 获取的是漏扫到站的订单号
	 * @param page
	 * @return
	 */
	public List<CwbOrder> getYichukudaozhan(String flowordertype, long customerid, String startTime, String cwbs, long page) {
		String sql = "SELECT DISTINCT cd.* FROM `express_set_branch` b RIGHT JOIN  express_ops_order_flow of ON of.branchid=b.branchid " + " LEFT JOIN express_ops_cwb_detail cd ON cd.cwb=of.cwb "
				+ " WHERE of.flowordertype IN (" + flowordertype + ") AND  of.credate>=?  " + " AND cd.state=1 AND b.`sitetype`=2 and cd.customerid=? " + " AND cd.cwb NOT IN (?)   limit ?,?";
		return this.jdbcTemplate.query(sql, new CwbMapper(), startTime, customerid, cwbs, (page - 1) * Page.ONE_PAGE_NUMBER, Page.ONE_PAGE_NUMBER);
	}

	/**
	 * 库房 出库已到站的list 不限制供货商
	 * 
	 * @param flowordertype
	 *            7,8
	 * @param branchid
	 * @param customerid
	 * @param startTime
	 * @param cwbs
	 *            '5547609467','5545959063' 获取的是漏扫到站的订单号
	 * @param page
	 * @return
	 */
	public List<CwbOrder> getYichukudaozhan(String flowordertype, String startTime, String cwbs, long page) {
		String sql = "SELECT DISTINCT cd.* FROM `express_set_branch` b RIGHT JOIN  express_ops_order_flow of ON of.branchid=b.branchid " + " LEFT JOIN express_ops_cwb_detail cd ON cd.cwb=of.cwb "
				+ " WHERE of.flowordertype IN (" + flowordertype + ") AND  of.credate>=?  " + " AND cd.state=1 AND b.`sitetype`=2 " + " AND cd.cwb NOT IN (?)   limit ?,?";
		return this.jdbcTemplate.query(sql, new CwbMapper(), startTime, cwbs, (page - 1) * Page.ONE_PAGE_NUMBER, Page.ONE_PAGE_NUMBER);
	}

	/**
	 * 站点 出库已到站的list
	 * 
	 * @param flowordertype
	 *            7,8
	 * @param branchid
	 * @param customerid
	 * @param startTime
	 * @param cwbs
	 *            '5547609467','5545959063' 获取的是漏扫到站的订单号
	 * @param page
	 * @return
	 */
	public List<CwbOrder> getYichukudaozhanByZhandian(String flowordertype, String startTime, String cwbs, long branchid, long page) {
		String sql = "SELECT DISTINCT cd.* FROM `express_set_branch` b RIGHT JOIN  express_ops_order_flow of ON of.branchid=b.branchid " + " LEFT JOIN express_ops_cwb_detail cd ON cd.cwb=of.cwb "
				+ " WHERE of.flowordertype IN (" + flowordertype + ") AND  of.credate>=?  " + " AND cd.state=1 AND b.`sitetype`=2 " + " AND cd.cwb NOT IN (" + cwbs + ") AND of.branchid=?  limit ?,?";
		return this.jdbcTemplate.query(sql, new CwbMapper(), startTime, branchid, (page - 1) * Page.ONE_PAGE_NUMBER, Page.ONE_PAGE_NUMBER);
	}

	/**
	 * 导出 站点 出库已到站的list
	 * 
	 * @param flowordertype
	 *            7,8
	 * @param branchid
	 * @param customerid
	 * @param startTime
	 * @param cwbs
	 *            '5547609467','5545959063' 获取的是漏扫到站的订单号
	 * @param page
	 * @return
	 */
	public String getYichukudaozhanByZhandianSql(String flowordertype, String startTime, String cwbs, long branchid) {
		String sql = "SELECT DISTINCT cd.* FROM `express_set_branch` b RIGHT JOIN  express_ops_order_flow of ON of.branchid=b.branchid " + " LEFT JOIN express_ops_cwb_detail cd ON cd.cwb=of.cwb "
				+ " WHERE of.flowordertype IN (" + flowordertype + ") AND  of.credate>='" + startTime + "'  " + " AND cd.state=1 AND b.`sitetype`=2 " + " AND cd.cwb NOT IN (" + cwbs
				+ ") AND of.branchid=" + branchid + " ";
		return sql;
	}

	/**
	 * 导出出库已到站的list
	 * 
	 * @param flowordertype
	 *            7,8
	 * @param branchid
	 * @param customerid
	 * @param startTime
	 * @param cwbs
	 *            '5547609467','5545959063' 获取的是漏扫到站的订单号
	 * @param page
	 * @return
	 */
	public String getYichukudaozhanSql(String flowordertype, long customerid, String startTime, String cwbs) {
		String sql = "SELECT DISTINCT cd.* FROM `express_set_branch` b RIGHT JOIN  express_ops_order_flow of ON of.branchid=b.branchid " + " LEFT JOIN express_ops_cwb_detail cd ON cd.cwb=of.cwb "
				+ " WHERE of.flowordertype IN (" + flowordertype + ") AND  of.credate>='" + startTime + "'  " + " AND cd.state=1 AND b.`sitetype`=2 and cd.customerid=" + customerid + " "
				+ " AND cd.cwb NOT IN (" + cwbs + ")  ";
		return sql;
	}

	/**
	 * 导出所有供货商出库已到站的list
	 * 
	 * @param flowordertype
	 *            7,8
	 * @param branchid
	 * @param startTime
	 * @param cwbs
	 *            '5547609467','5545959063' 获取的是漏扫到站的订单号
	 * @param page
	 * @return
	 */
	public String getYichukudaozhanSql(String flowordertype, String startTime, String cwbs) {
		String sql = "SELECT DISTINCT cd.* FROM `express_set_branch` b RIGHT JOIN  express_ops_order_flow of ON of.branchid=b.branchid " + " LEFT JOIN express_ops_cwb_detail cd ON cd.cwb=of.cwb "
				+ " WHERE of.flowordertype IN (" + flowordertype + ") AND  of.credate>='" + startTime + "'  " + " AND cd.state=1 AND b.`sitetype`=2 " + " AND cd.cwb NOT IN (" + cwbs + ")  ";
		return sql;
	}

	// 按订单号导出的sql
	public String getSqlByCwb(String cwbs) {
		return "select * from express_ops_cwb_detail where state=1 and cwb in(" + cwbs + ")";
	}

	public List<CwbOrder> getCwbOrderByCwbs(String cwbs) {
		String sql = "select * from express_ops_cwb_detail where state=1 and cwb in(" + cwbs + ")";
		return this.jdbcTemplate.query(sql, new CwbMapper());
	}

	public CwbOrder getSumByCwbs(String cwbs) {
		String sql = "select sum(receivablefee) as receivablefee,sum(paybackfee) as paybackfee from express_ops_cwb_detail where state=1 and cwb in(" + cwbs + ")";
		try {
			return this.jdbcTemplate.queryForObject(sql, new CwbMOneyMapper());
		} catch (DataAccessException e) {
			return new CwbOrder();
		}
	}

	// =====================库房日志统计 获取list==========end==================

	public List<CwbOrder> getListByEmaildateId(String emaildateids, String customerids, long customerwarehouseid, long auditState, int cwbOrderType) {
		String sql = "select * from express_ops_cwb_detail cd LEFT JOIN finance_audit_temp fat on cd.cwb=fat.cwb where cd.emaildateid in (" + emaildateids + ")  " + "and  cd.customerid in("
				+ customerids + ") ";
		if ((customerwarehouseid > -1) || (auditState > -1) || (cwbOrderType > -1)) {
			StringBuffer w = new StringBuffer();
			if (customerwarehouseid > -1) {
				w.append(" and  cd.customerwarehouseid=" + customerwarehouseid + " ");
			}
			if (auditState == 0) {// 如果要获取未审核的数据
				w.append(" and  fat.id IS NULL ");
			} else if (auditState == 1) {// 如果要获取已审核的数据
				w.append(" and  fat.id>0 ");
			}
			if (cwbOrderType > -1) {
				w.append(" and  cd.cwbordertypeid=" + cwbOrderType + " ");
			}
			w.append(" and state=1 ");
			sql += w.toString();
		}
		return this.jdbcTemplate.query(sql, new CwbMapper());
	}

	public String getCwbsListByCustomeridAndDeliverystateAndCredateWhere(String sql, String startCredate, String endCredate, String customerids, long deliverystate, long isaudit,
			long customerwarehouseid, long auditState, String paymentfordeliverystate_diushi, int dateType) {
		if (dateType == 1) {// 反馈时间
			if (startCredate.length() > 0) {
				sql += " and ds.deliverytime >='" + startCredate + "'";
			}
			if (endCredate.length() > 0) {
				sql += " and ds.deliverytime <='" + endCredate + "'";
			}
		} else {// 推送时间
			if (startCredate.length() > 0) {
				sql += " and ds.pushtime >='" + startCredate + "'";
			}
			if (endCredate.length() > 0) {
				sql += " and ds.pushtime <='" + endCredate + "'";
			}
		}

		if (deliverystate > 0) {
			sql += " and cd.deliverystate=" + deliverystate;
		} else {
			if (paymentfordeliverystate_diushi.equals("yes")) {
				sql += " and cd.deliverystate in(" + DeliveryStateEnum.PeiSongChengGong.getValue() + "," + DeliveryStateEnum.ShangMenHuanChengGong.getValue() + ","
						+ +DeliveryStateEnum.ShangMenTuiChengGong.getValue() + "," + DeliveryStateEnum.HuoWuDiuShi.getValue() + ")";
			} else {
				sql += " and cd.deliverystate in(" + DeliveryStateEnum.PeiSongChengGong.getValue() + "," + DeliveryStateEnum.ShangMenHuanChengGong.getValue() + ","
						+ +DeliveryStateEnum.ShangMenTuiChengGong.getValue() + ")";
			}
		}

		if ((isaudit > -1) || (customerwarehouseid > -1)) {
			if (isaudit == 0) {
				sql += " and ds.gcaid <= 0 ";
			} else {
				sql += " and ds.gcaid > 0 ";
			}
			if (customerwarehouseid > -1) {
				sql += " and  cd.customerwarehouseid=" + customerwarehouseid + " ";
			}
			if (auditState == 0) {// 如果要获取未审核的数据
				sql += " and  fat.id IS NULL ";
			} else if (auditState == 1) {// 如果要获取已审核的数据
				sql += " and  fat.id>0 ";
			}
		}
		return sql;
	}

	public List<String> getCwbsListByCustomeridAndDeliverystateAndCredate(String startCredate, String endCredate, String customerids, long deliverystate, long isaudit, long customerwarehouseid,
			long auditState, String paymentfordeliverystate_diushi, int dateType) {
		String sql = "SELECT ds.cwb FROM express_ops_cwb_detail " + "cd RIGHT JOIN express_ops_delivery_state ds ON cd.cwb=ds.cwb left join finance_audit_temp fat on cd.cwb=fat.cwb "
				+ " WHERE cd.state=1  AND ds.state=1 and cd.customerid in(" + customerids + ") ";
		sql = this.getCwbsListByCustomeridAndDeliverystateAndCredateWhere(sql, startCredate, endCredate, customerids, deliverystate, isaudit, customerwarehouseid, auditState,
				paymentfordeliverystate_diushi, dateType);
		return this.jdbcTemplate.queryForList(sql, String.class);

	}

	public List<JSONObject> getListByCustomeridAndDeliverystateAndCredate(long page, String startCredate, String endCredate, String customerids, long deliverystate, long isaudit,
			long customerwarehouseid, long auditState, String paymentfordeliverystate_diushi, int dateType) {
		String sql = "SELECT cd.*,ds.auditingtime,ds.deliverytime, ds.receivedfee,ds.pushtime FROM express_ops_cwb_detail "
				+ "cd RIGHT JOIN express_ops_delivery_state ds ON cd.cwb=ds.cwb left join finance_audit_temp fat on cd.cwb=fat.cwb " + " WHERE cd.state=1  AND ds.state=1 and cd.customerid in("
				+ customerids + ") ";
		sql = this.getCwbsListByCustomeridAndDeliverystateAndCredateWhere(sql, startCredate, endCredate, customerids, deliverystate, isaudit, customerwarehouseid, auditState,
				paymentfordeliverystate_diushi, dateType);
		sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		return this.jdbcTemplate.query(sql, new CwbPayMapper());
	}

	public String getListByCustomeridAndDeliverystateAndCredateByBackWhere(String sql, String cwbs, String customerids, long isout, long customerwarehouseid, long auditState) {
		if ((cwbs.length() > 0) || (customerids.length() > 0) || (customerwarehouseid > 0) || (isout > -1)) {
			if (isout == 0) {
				sql += " and ds.isout = 0 ";
			} else {
				sql += " and ds.isout = 1 ";
			}
			if (cwbs.length() > 0) {
				sql += " and cd.cwb in(" + cwbs + ")";
			}
			if (customerids.length() > 0) {
				sql += " and cd.customerid in(" + customerids + ")";
			}
			if (customerwarehouseid > -1) {
				sql += " and  cd.customerwarehouseid=" + customerwarehouseid + " ";
			}
			if (auditState == 0) {// 如果要获取未审核的数据
				if (isout == 0) {
					sql += " and ( fat.id IS NULL or fat.type<>4 )";
				} else {
					sql += " and ( fat.id IS NULL or fat.type<>3 )";
				}
			} else if (auditState == 1) {// 如果要获取已审核的数据
				if (isout == 0) {
					sql += " and  fat.type = 4 ";
				} else {
					sql += " and  fat.type = 3 ";
				}
			}
		}
		return sql;
	}

	public List<JSONObject> getListByCustomeridAndDeliverystateAndCredateByBack(long page, String cwbs, String customerids, long isout, long customerwarehouseid, long auditState) {
		String sql = "SELECT cd.*,ds.auditingtime,ds.deliverytime, ds.receivedfee,ds.returnedfee FROM express_ops_cwb_detail "
				+ "cd RIGHT JOIN express_ops_delivery_state ds ON cd.cwb=ds.cwb left join finance_audit_temp fat on cd.cwb=fat.cwb " + " WHERE cd.state=1  AND ds.state=1 ";
		sql = this.getListByCustomeridAndDeliverystateAndCredateByBackWhere(sql, cwbs, customerids, isout, customerwarehouseid, auditState);
		if (page > 0) {
			sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		}
		return this.jdbcTemplate.query(sql, new CwbBackPayMapper());

	}

	public JSONObject getListByCustomeridAndDeliverystateAndCredateNopage(String startCredate, String endCredate, String customerids, long deliverystate, long isaudit, long customerwarehouseid,
			long auditState, String paymentfordeliverystate_diushi, int dateType) {
		String sql = "SELECT COUNT(1) cwbcount,SUM(cd.receivablefee) receivablefees,SUM(cd.paybackfee) paybackfees FROM express_ops_cwb_detail cd RIGHT JOIN express_ops_delivery_state ds ON cd.cwb=ds.cwb "
				+ " left join finance_audit_temp fat on cd.cwb=fat.cwb " + " WHERE cd.state=1  AND ds.state=1 and cd.customerid in(" + customerids + ") ";

		sql = this.getCwbsListByCustomeridAndDeliverystateAndCredateWhere(sql, startCredate, endCredate, customerids, deliverystate, isaudit, customerwarehouseid, auditState,
				paymentfordeliverystate_diushi, dateType);
		return this.jdbcTemplate.queryForObject(sql, new CwbFeeMapper());

	}

	public JSONObject getListByCustomeridAndDeliverystateAndCredateNopageByBack(String cwbs, String customerids, long isout, long customerwarehouseid, long auditState) {
		String sql = "SELECT COUNT(1) cwbcount,SUM(ds.receivedfee) receivablefees,SUM(ds.returnedfee) paybackfees FROM express_ops_cwb_detail cd RIGHT JOIN express_ops_delivery_state ds "
				+ "ON cd.cwb=ds.cwb left join finance_audit_temp fat on cd.cwb=fat.cwb " + " WHERE cd.state=1  AND ds.state=1 ";
		sql = this.getListByCustomeridAndDeliverystateAndCredateByBackWhere(sql, cwbs, customerids, isout, customerwarehouseid, auditState);
		return this.jdbcTemplate.queryForObject(sql, new CwbFeeMapper());

	}

	private final class CwbStringMapper implements RowMapper<String> {
		@Override
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			String cwb = rs.getString("cwb");
			return cwb;
		}
	}

	public List<String> getListByCustomeridAndDeliverystateAndCredateNoPage(long page, String startCredate, String endCredate, String customerids, long deliverystate, long isaudit,
			long customerwarehouseid, long auditState, String paymentfordeliverystate_diushi, int dateType) {
		String sql = "SELECT cd.cwb FROM express_ops_cwb_detail cd RIGHT JOIN express_ops_delivery_state ds ON cd.cwb=ds.cwb " + " LEFT JOIN finance_audit_temp fat on cd.cwb=fat.cwb "
				+ "WHERE cd.state=1 AND ds.state=1 and cd.customerid in(" + customerids + ") ";

		sql = this.getCwbsListByCustomeridAndDeliverystateAndCredateWhere(sql, startCredate, endCredate, customerids, deliverystate, isaudit, customerwarehouseid, auditState,
				paymentfordeliverystate_diushi, dateType);
		sql += " limit " + page + " ," + Page.EXCEL_PAGE_NUMBER;
		return this.jdbcTemplate.query(sql, new CwbStringMapper());
	}

	public List<String> getListByCustomeridAndDeliverystateAndCredateNoPageByBack(long page, String cwbs, String customerids, long isaudit, long customerwarehouseid, long auditState, long isout) {
		String sql = "SELECT cd.cwb FROM express_ops_cwb_detail " + "cd RIGHT JOIN express_ops_delivery_state ds ON cd.cwb=ds.cwb left join finance_audit_temp fat on cd.cwb=fat.cwb "
				+ " WHERE cd.state=1  AND ds.state=1  ";
		sql = this.getListByCustomeridAndDeliverystateAndCredateByBackWhere(sql, cwbs, customerids, isout, customerwarehouseid, auditState);
		sql += " limit " + page + " ," + Page.EXCEL_PAGE_NUMBER;
		return this.jdbcTemplate.query(sql, new CwbStringMapper());
	}

	public long getListByCustomeridAndDeliverystateAndCredateCount(String startCredate, String endCredate, String customerids, long deliverystate, long isaudit, long customerwarehouseid,
			long auditState, String paymentfordeliverystate_diushi, int dateType) {
		String sql = "SELECT count(1) FROM express_ops_cwb_detail cd RIGHT JOIN express_ops_delivery_state ds ON cd.cwb=ds.cwb " + "left join finance_audit_temp fat on cd.cwb=fat.cwb "
				+ "WHERE cd.state=1  AND ds.state=1 and cd.customerid in(" + customerids + ") ";
		sql = this.getCwbsListByCustomeridAndDeliverystateAndCredateWhere(sql, startCredate, endCredate, customerids, deliverystate, isaudit, customerwarehouseid, auditState,
				paymentfordeliverystate_diushi, dateType);
		return this.jdbcTemplate.queryForInt(sql);

	}

	public long getListByCustomeridAndDeliverystateAndCredateCountByBack(String cwbs, String customerids, long isout, long customerwarehouseid, long auditState) {
		String sql = "SELECT count(1) FROM express_ops_cwb_detail cd RIGHT JOIN express_ops_delivery_state ds ON cd.cwb=ds.cwb " + "RIGHT JOIN express_ops_goto_class_auditing  gc "
				+ "ON ds.gcaid=gc.id left join finance_audit_temp fat on cd.cwb=fat.cwb " + "WHERE cd.state=1  AND ds.state=1 and cd.flowordertype="
				+ FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong.getValue() + " ";
		sql = this.getListByCustomeridAndDeliverystateAndCredateByBackWhere(sql, cwbs, customerids, isout, customerwarehouseid, auditState);
		return this.jdbcTemplate.queryForInt(sql);

	}

	public List<CwbOrder> getAllCwbOrderByCwb(String cwb) {
		return this.jdbcTemplate.query("select * from express_ops_cwb_detail  where state=1 and cwb =?", new CwbSmalMaper(), cwb);
	}

	public List<CwbOrder> getCwbOrderForOperationtimeout(long page, long outTime, String flowordertype, long branchid, long deliverystate, long nextbranchid) {
		String sql = "SELECT * FROM express_ops_cwb_detail cd RIGHT JOIN express_ops_operation_time ot ON cd.cwb=ot.cwb" + " WHERE ot.credate<? AND ot.flowordertype in(" + flowordertype
				+ ") AND cd.state=1 ";
		if (branchid > 0) {
			sql += " and ot.branchid=" + branchid;
		}
		if (deliverystate > 0) {
			sql += " and ot.deliverystate=" + deliverystate;
		}
		if (nextbranchid > 0) {
			sql += " and ot.nextbranchid=" + nextbranchid;
		}

		sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		return this.jdbcTemplate.query(sql, new CwbMapper(), outTime);
	}

	public long getCwbOrderForOperationtimeoutCount(long outTime, String flowordertype, long branchid, long deliverystate, long nextbranchid) {
		String sql = "SELECT count(1) FROM express_ops_cwb_detail cd RIGHT JOIN express_ops_operation_time ot ON cd.cwb=ot.cwb" + " WHERE ot.credate<? AND ot.flowordertype in(" + flowordertype
				+ ") AND cd.state=1 ";
		if (branchid > 0) {
			sql += " and ot.branchid=" + branchid;
		}
		if (deliverystate > 0) {
			sql += " and ot.deliverystate=" + deliverystate;
		}
		if (nextbranchid > 0) {
			sql += " and ot.nextbranchid=" + nextbranchid;
		}

		return this.jdbcTemplate.queryForLong(sql, outTime);
	}

	public List<CwbOrder> getCwbOrderForOperationtimeout(long page, long outTime, String flowordertype, long branchid, long deliverystate, long nextbranchid, long customerid, String begindate,
			String enddate) {
		String sql = "SELECT * FROM express_ops_cwb_detail cd RIGHT JOIN express_ops_operation_time ot ON cd.cwb=ot.cwb" + " WHERE ot.credate<? AND ot.flowordertype in(" + flowordertype
				+ ") AND cd.state=1 ";
		if (branchid > 0) {
			sql += " and ot.branchid=" + branchid;
		}
		if (deliverystate > 0) {
			sql += " and ot.deliverystate=" + deliverystate;
		}
		if (nextbranchid > 0) {
			sql += " and ot.nextbranchid=" + nextbranchid;
		}
		if (customerid > 0) {
			sql += " and ot.customerid=" + customerid;
		}
		if (begindate.length() > 0) {
			sql += " and ot.outwarehouseTime>='" + begindate + "' ";
		}
		if (enddate.length() > 0) {
			sql += " and ot.outwarehouseTime<='" + enddate + "' ";
		}
		sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		return this.jdbcTemplate.query(sql, new CwbMapper(), outTime);
	}

	public long getCwbOrderForOperationtimeoutCount(long outTime, String flowordertype, long branchid, long deliverystate, long nextbranchid, long customerid, String begindate, String enddate) {
		String sql = "SELECT count(1) FROM express_ops_cwb_detail cd RIGHT JOIN express_ops_operation_time ot ON cd.cwb=ot.cwb" + " WHERE ot.credate<? AND ot.flowordertype in(" + flowordertype
				+ ") AND cd.state=1 ";
		if (branchid > 0) {
			sql += " and ot.branchid=" + branchid;
		}
		if (deliverystate > 0) {
			sql += " and ot.deliverystate=" + deliverystate;
		}
		if (nextbranchid > 0) {
			sql += " and ot.nextbranchid=" + nextbranchid;
		}
		if (customerid > 0) {
			sql += " and ot.customerid=" + customerid;
		}
		if (begindate.length() > 0) {
			sql += " and ot.outwarehouseTime>='" + begindate + "' ";
		}
		if (enddate.length() > 0) {
			sql += " and ot.outwarehouseTime<='" + enddate + "' ";
		}

		return this.jdbcTemplate.queryForLong(sql, outTime);
	}

	public List<CwbOrder> getListByCwb(String cwb) {
		String sql = "select * from express_ops_cwb_detail where cwb =? and  state=1 ";

		return this.jdbcTemplate.query(sql, new CwbMapper(), cwb);
	}

	public List<CwbOrder> getListByCwbAndEmaildate(String cwb, String begindate, String enddate) {
		String sql = "select * from express_ops_cwb_detail where cwb =? and  state=1 and  emaildate >=? and emaildate <=?";
		return this.jdbcTemplate.query(sql, new CwbMapper(), cwb, begindate, enddate);
	}

	public List<CwbOrder> getListByPackagecode(String packagecode, long page) {
		String sql = "select * from express_ops_cwb_detail where packagecode =? and  state=1 ";
		sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		return this.jdbcTemplate.query(sql, new CwbMapper(), packagecode);
	}

	public List<CwbOrder> getListByTransCwb(String transcwb, long page) {
		String sql = "select de.* from express_ops_cwb_detail as de left join set_onetranscwb_to_morecwbs as sm on de.cwb=sm.cwb " + " where sm.transcwb =? and  de.state=1 ";
		sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		return this.jdbcTemplate.query(sql, new CwbMapper(), transcwb);
	}

	public List<CwbOrder> getListByTransCwbExcel(String transcwb) {
		String sql = "select de.* from express_ops_cwb_detail as de left join set_onetranscwb_to_morecwbs as sm on de.cwb=sm.cwb " + " where sm.transcwb =? and  de.state=1 ";
		return this.jdbcTemplate.query(sql, new CwbMapper(), transcwb);
	}

	public List<CwbOrder> getListByPackagecodeExcel(String packagecode) {
		String sql = "select * from express_ops_cwb_detail where packagecode =? and  state=1 ";
		return this.jdbcTemplate.query(sql, new CwbMapper(), packagecode);
	}

	public long getListByPackagecodeCount(String packagecode) {
		String sql = "select count(1) from express_ops_cwb_detail where packagecode =? and  state=1 ";

		return this.jdbcTemplate.queryForLong(sql, packagecode);
	}

	public long getListByTransCwbCount(String transcwb) {
		String sql = "select count(1) from express_ops_cwb_detail as de left join set_onetranscwb_to_morecwbs as sm on de.cwb=sm.cwb " + " where sm.transcwb =? and  de.state=1 ";
		return this.jdbcTemplate.queryForLong(sql, transcwb);
	}

	public List<CwbOrder> getListByCwb(String begindate, String enddate, long customerid, String consigneename, String consigneemobile, String consigneeaddress, long page) {
		String sql = "select * from express_ops_cwb_detail where emaildate >=? and emaildate <=? and state=1 ";
		if ((customerid > 0) || (consigneename.length() > 0) || (consigneemobile.length() > 0) || (consigneeaddress.length() > 0)) {
			StringBuffer w = new StringBuffer();
			if (customerid > 0) {
				w.append(" and  customerid=" + customerid);
			}
			if (consigneename.length() > 0) {
				w.append(" and consigneename = '" + consigneename + "'");
			}
			if (consigneemobile.length() > 0) {
				w.append(" and consigneemobile = '" + consigneemobile + "'");
			}
			if (consigneeaddress.length() > 0) {
				w.append(" and consigneeaddress like '%" + consigneeaddress + "%'");
			}
			sql += w.toString();
		}
		sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		return this.jdbcTemplate.query(sql, new CwbMapper(), begindate, enddate);
	}

	public List<CwbOrder> getListByCwbExcel(String begindate, String enddate, long customerid, String consigneename, String consigneemobile, String consigneeaddress) {
		String sql = "select * from express_ops_cwb_detail where emaildate >=? and emaildate <=? and state=1 ";
		if ((customerid > 0) || (consigneename.length() > 0) || (consigneemobile.length() > 0) || (consigneeaddress.length() > 0)) {
			StringBuffer w = new StringBuffer();
			if (customerid > 0) {
				w.append(" and  customerid=" + customerid);
			}
			if (consigneename.length() > 0) {
				w.append(" and consigneename = '" + consigneename + "'");
			}
			if (consigneemobile.length() > 0) {
				w.append(" and consigneemobile = '" + consigneemobile + "'");
			}
			if (consigneeaddress.length() > 0) {
				w.append(" and consigneeaddress like '%" + consigneeaddress + "%'");
			}
			sql += w.toString();
		}
		return this.jdbcTemplate.query(sql, new CwbMapper(), begindate, enddate);
	}

	public long getCountByCwb(String begindate, String enddate, long customerid, String consigneename, String consigneemobile, String consigneeaddress) {
		String sql = "select count(1) from express_ops_cwb_detail where emaildate >=? and emaildate <=? and state=1 ";
		if ((customerid > 0) || (consigneename.length() > 0) || (consigneemobile.length() > 0) || (consigneeaddress.length() > 0)) {
			StringBuffer w = new StringBuffer();
			if (customerid > 0) {
				w.append(" and  customerid=" + customerid);
			}
			if (consigneename.length() > 0) {
				w.append(" and consigneename = '" + consigneename + "'");
			}
			if (consigneemobile.length() > 0) {
				w.append(" and consigneemobile = '" + consigneemobile + "'");
			}
			if (consigneeaddress.length() > 0) {
				w.append(" and consigneeaddress like '%" + consigneeaddress + "%'");
			}
			sql += w.toString();
		}
		return this.jdbcTemplate.queryForLong(sql, begindate, enddate);
	}

	public List<CwbOrder> getCwbByFlowOrderType(FlowOrderTypeEnum flowOrderType) {
		return this.jdbcTemplate.query("SELECT * FROM express_ops_cwb_detail " + " WHERE flowordertype=? AND state=1 ", new CwbMapper(), flowOrderType.getValue());
	}

	public List<CwbOrder> getCwbByFlowOrderTypeAndDeliveryState(FlowOrderTypeEnum flowordertype, DeliveryStateEnum deliveryState) {
		return this.jdbcTemplate.query("SELECT * FROM  express_ops_cwb_detail   WHERE flowordertype=? AND state=1 AND deliverystate=? ", new CwbMapper(), flowordertype.getValue(),
				deliveryState.getValue());
	}

	/**
	 * 按站点区域发送短信，查询订单
	 * 
	 * @param branchid
	 * @param flowordertype
	 *            库房出库，站点到站，领货
	 * @return
	 */
	public List<CwbOrder> getOrderListByBranchidAndFlowtype(long branchid, int flowordertype) {
		return this.jdbcTemplate.query("SELECT * FROM  express_ops_cwb_detail   WHERE nextbranchid=? and flowordertype=? AND state=1 ", new CwbMapper(), branchid, flowordertype);
	}

	/**
	 * 按站点区域发送短信，查询滞留的订单
	 * 
	 * @param branchid
	 * @param deliverystate
	 *            滞留的状态 6
	 * @return
	 */
	public List<CwbOrder> getOrderListByBranchidAndDeliveryState(long branchid, long deliverystate) {
		return this.jdbcTemplate.query("SELECT * FROM  express_ops_cwb_detail   WHERE deliverybranchid=? and deliverystate=? AND state=1 ", new CwbMapper(), branchid, deliverystate);
	}

	public Long getCwbByFlowOrderTypeAndDeliveryStateAndCurrentbranchid(FlowOrderTypeEnum flowordertype, DeliveryStateEnum deliveryState, long currentbranchid, long nextbranchid) {
		return this.jdbcTemplate.queryForLong("SELECT count(1) FROM  express_ops_cwb_detail   WHERE currentbranchid=?  AND flowordertype=? AND state=1 AND deliverystate=? AND nextbranchid=?",
				currentbranchid, flowordertype.getValue(), deliveryState.getValue(), nextbranchid);
	}

	// 退货出站已出站统计
	public Long getCwbByFlowOrderTypeAndNextbranchidAndStartbranchid(long flowordertype, long startbranchid, long nextbranchid) {
		String sql = "SELECT count(1) FROM  express_ops_cwb_detail   WHERE startbranchid=?  AND flowordertype=? AND state=1";
		if (nextbranchid > 0) {
			sql += " AND nextbranchid=" + nextbranchid;
		}
		return this.jdbcTemplate.queryForLong(sql, startbranchid, flowordertype);
	}

	// 退货出站已出站列表
	public List<CwbOrder> getCwbByFlowOrderTypeAndNextbranchidAndStartbranchidList(long flowordertype, long startbranchid, long nextbranchid) {
		String sql = "SELECT * FROM  express_ops_cwb_detail   WHERE startbranchid=?  AND flowordertype=? AND state=1";
		if (nextbranchid > 0) {
			sql += " AND nextbranchid=" + nextbranchid;
		}
		return this.jdbcTemplate.query(sql, new CwbMapper(), startbranchid, flowordertype);
	}

	public List<CwbOrder> getCwbOrderByFlowOrderTypeAndDeliveryStateAndCurrentbranchid(FlowOrderTypeEnum flowordertype, DeliveryStateEnum deliveryState, long currentbranchid) {
		String sql = "SELECT * FROM  express_ops_cwb_detail   WHERE currentbranchid=?  AND flowordertype=? AND deliverystate=? AND state=1 and cwbstate=2";
		return this.jdbcTemplate.query(sql, new CwbMapper(), currentbranchid, flowordertype.getValue(), deliveryState.getValue());
	}

	public List<CwbOrder> getCwbOrderByTypeAndDeliveryStates(FlowOrderTypeEnum flowordertype, String deliveryStates, long currentbranchid) {
		String sql = "SELECT ocd.* FROM  express_ops_cwb_detail as ocd   right  join (select * from express_set_customer_info where needchecked=0) as esc on ocd.customerid= esc.customerid  WHERE currentbranchid=?  AND flowordertype=? AND deliverystate in( "
				+ deliveryStates + " ) AND state=1 ";
		return this.jdbcTemplate.query(sql, new CwbMapper(), currentbranchid, flowordertype.getValue());
	}

	public List<CwbOrder> getCwbOrderByFlowOrderTypeAndCurrentbranchid(long flowordertype, long currentbranchid) {
		String sql = "SELECT  * FROM  express_ops_cwb_detail  WHERE currentbranchid=?  AND flowordertype=? AND state=1 ";
		return this.jdbcTemplate.query(sql, new CwbMapper(), currentbranchid, flowordertype);
	}

	public List<CwbOrder> getCwbOrderByTypeAndbid(long flowordertype, long currentbranchid) {
		String sql = "SELECT  ocd.* FROM  express_ops_cwb_detail as ocd  right join (select * from express_set_customer_info where needchecked=1) as esc on ocd.customerid=esc.customerid   WHERE currentbranchid=?  AND flowordertype=? AND state=1 ";
		return this.jdbcTemplate.query(sql, new CwbMapper(), currentbranchid, flowordertype);
	}

	public long getCwbOrderByFlowOrderTypeAndCurrentbranchidCount(long flowordertype, long currentbranchid, long nextbranchid) {
		String sql = "SELECT count(1) FROM  express_ops_cwb_detail   WHERE currentbranchid=?  AND flowordertype=? AND state=1 AND nextbranchid=?";
		return this.jdbcTemplate.queryForLong(sql, currentbranchid, flowordertype, nextbranchid);
	}

	public void saveCwbOrderByExcelbranch(String excelbranch, long deliverybranchid) {
		String sql = "update express_ops_cwb_detail set excelbranch=? where deliverybranchid=?";
		this.jdbcTemplate.update(sql, excelbranch, deliverybranchid);
	}

	public List<CwbOrder> getZhanDianYingtuiList(long flowordertype, String deliverystates, long page) {
		String sql = "SELECT *  FROM express_ops_cwb_detail " + "WHERE  flowordertype=? and deliverystate in(" + deliverystates + ")  AND state=1  LIMIT ?,?";
		return this.jdbcTemplate.query(sql, new CwbMapper(), flowordertype, ((page - 1) * Page.ONE_PAGE_NUMBER), Page.ONE_PAGE_NUMBER);
	}

	public String getZhanDianYingtuiSql(long flowordertype, String deliverystates) {
		String sql = "SELECT *  FROM express_ops_cwb_detail " + "WHERE  flowordertype=" + flowordertype + " and deliverystate in(" + deliverystates + ")  AND state=1 ";
		return sql;
	}

	public List<CwbOrder> getCwbByCwbsPage(long page, String cwbs) {
		String sql = "SELECT * from express_ops_cwb_detail where cwb in(" + cwbs + ") and state=1 ";
		sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		System.out.println(sql);
		return this.jdbcTemplate.query(sql, new CwbMapper());
	}

	public List<CwbOrder> getCwbByCwbsPage(long page, String cwbs, int pagecount) {
		String sql = "SELECT * from express_ops_cwb_detail where cwb in(" + cwbs + ") and state=1 ";
		sql += " limit " + ((page - 1) * pagecount) + " ," + pagecount;
		return this.jdbcTemplate.query(sql, new CwbMapper());
	}

	// 根据订单号失效
	public void dataLoseByCwb(String cwb) {
		this.jdbcTemplate.update("update express_ops_cwb_detail set state=0  where state =1 and cwb=? ", cwb);
	}

	public void saveResendtimeByCwb(String resendtime, String cwb) {
		String sql = "update express_ops_cwb_detail set resendtime=? where cwb=? and state=1";
		this.jdbcTemplate.update(sql, resendtime, cwb);
	}

	public List<CwbOrder> getCwbDetailByParamAndCwbsPage(long page, String customerids, String emaildatebegin, String emaildateend, long cwbordertypeid, long nextbranchid) {
		String sql = "select * from express_ops_cwb_detail where state=1 and emaildate>='" + emaildatebegin + "' and emaildate<='" + emaildateend + "' " + "and flowordertype in("
				+ FlowOrderTypeEnum.DaoRuShuJu.getValue() + "," + FlowOrderTypeEnum.TiHuo.getValue() + "," + FlowOrderTypeEnum.TiHuoYouHuoWuDan.getValue() + ")";
		if (customerids.length() > 0) {
			sql += " and customerid in(" + customerids + ")";
		}
		if (cwbordertypeid > -2) {
			sql += " and cwbordertypeid=" + cwbordertypeid;
		}
		if (nextbranchid > 0) {
			sql += " and nextbranchid=" + nextbranchid;
		}

		sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;

		return this.jdbcTemplate.query(sql, new CwbMapper());
	}

	public String getCwbDetailByParamAndCwbsSql(long page, String customerids, String emaildatebegin, String emaildateend, long cwbordertypeid, long nextbranchid) {
		String sql = "select * from express_ops_cwb_detail where state=1 and emaildate>='" + emaildatebegin + "' and emaildate<='" + emaildateend + "' " + "and flowordertype in("
				+ FlowOrderTypeEnum.DaoRuShuJu.getValue() + "," + FlowOrderTypeEnum.TiHuo.getValue() + "," + FlowOrderTypeEnum.TiHuoYouHuoWuDan.getValue() + ")";
		if (customerids.length() > 0) {
			sql += " and customerid in(" + customerids + ")";
		}
		if (cwbordertypeid > -2) {
			sql += " and cwbordertypeid=" + cwbordertypeid;
		}
		if (nextbranchid > 0) {
			sql += " and nextbranchid=" + nextbranchid;
		}

		sql += " limit " + page + " ," + Page.EXCEL_PAGE_NUMBER;

		return sql;
	}

	public long getCwbDetailByParamAndCwbsCount(String customerids, String emaildatebegin, String emaildateend, long cwbordertypeid, long nextbranchid) {
		String sql = "select count(1) from express_ops_cwb_detail where state=1 and emaildate>='" + emaildatebegin + "' and emaildate<='" + emaildateend + "' " + "and flowordertype in("
				+ FlowOrderTypeEnum.DaoRuShuJu.getValue() + "," + FlowOrderTypeEnum.TiHuo.getValue() + "," + FlowOrderTypeEnum.TiHuoYouHuoWuDan.getValue() + ")";
		if (customerids.length() > 0) {
			sql += " and customerid in(" + customerids + ")";
		}
		if (cwbordertypeid > -2) {
			sql += " and cwbordertypeid=" + cwbordertypeid;
		}
		if (nextbranchid > 0) {
			sql += " and nextbranchid=" + nextbranchid;
		}

		return this.jdbcTemplate.queryForLong(sql);
	}

	public long getCwbDetailByParamAndCwbsCount(String emaildatebegin, String emaildateend, long cwbordertypeid, String cwbs, long nextbranchid) {
		String sql = "select count(1) from express_ops_cwb_detail where state=1 ";
		StringBuilder deliverystatesql = new StringBuilder();
		if (emaildatebegin.length() > 0) {
			deliverystatesql.append(" and emaildate>='" + emaildatebegin + "'");
		}
		if (emaildateend.length() > 0) {
			deliverystatesql.append(" and emaildate<='" + emaildateend + "'");
		}
		if (cwbordertypeid > -2) {
			deliverystatesql.append(" and cwbordertypeid=" + cwbordertypeid);
		}
		if (nextbranchid > 0) {
			deliverystatesql.append(" and nextbranchid=" + nextbranchid);
		}
		if (cwbs.length() > 0) {
			deliverystatesql.append(" and cwb not in(" + cwbs + ")");
		}

		sql += deliverystatesql.toString();

		return this.jdbcTemplate.queryForLong(sql);
	}

	// 用于查询所有符合要求的超期异常订单，便于超期异常订单的导出
	public List<String> getCwbOrderForOperationtimeout(long outTime, String flowordertype, long branchid, long deliverystate, long nextbranchid) {
		String sql = "SELECT cd.cwb FROM express_ops_cwb_detail cd RIGHT JOIN express_ops_operation_time ot ON cd.cwb=ot.cwb" + " WHERE ot.credate<'" + outTime + "' AND ot.flowordertype in("
				+ flowordertype + ") AND cd.state=1 ";
		if (branchid > 0) {
			sql += " and ot.branchid=" + branchid;
		}
		if (deliverystate > 0) {
			sql += " and ot.deliverystate=" + deliverystate;
		}
		if (nextbranchid > 0) {
			sql += " and ot.nextbranchid=" + nextbranchid;
		}
		return this.jdbcTemplate.queryForList(sql, String.class);

	}

	// 用于查询所有符合要求的超期异常订单，便于超期异常订单的导出
	public List<String> getCwbOrderForOperationtimeout(long outTime, String flowordertype, long branchid, long deliverystate, long nextbranchid, long customerid, String begindate, String enddate) {
		String sql = "SELECT cd.cwb FROM express_ops_cwb_detail cd RIGHT JOIN express_ops_operation_time ot ON cd.cwb=ot.cwb" + " WHERE ot.credate<'" + outTime + "' AND ot.flowordertype in("
				+ flowordertype + ") AND cd.state=1 ";
		if (branchid > 0) {
			sql += " and ot.branchid=" + branchid;
		}
		if (deliverystate > 0) {
			sql += " and ot.deliverystate=" + deliverystate;
		}
		if (nextbranchid > 0) {
			sql += " and ot.nextbranchid=" + nextbranchid;
		}
		if (customerid > 0) {
			sql += " and ot.customerid=" + customerid;
		}
		if (begindate.length() > 0) {
			sql += " and ot.outwarehouseTime>='" + begindate + "' ";
		}
		if (enddate.length() > 0) {
			sql += " and ot.outwarehouseTime<='" + enddate + "' ";
		}
		return this.jdbcTemplate.queryForList(sql, String.class);

	}

	// ==============================修改订单使用的方法 start
	// ==================================

	/**
	 * 重置审核状态 修改订单表字段
	 * 
	 * @param nextbranchid
	 *            下一站 目前逻辑如此 改为对应货物所在站点的中转站id
	 * @param flowordertype
	 *            改为9 领货状态
	 * @param currentbranchid
	 *            当前所属机构 改为0 因为领货后不算库存
	 * @param cwbstate
	 *            订单状态 置为1 将订单状态变为配送状态
	 * @param deliverystate
	 *            反馈状态 置为0 配送结果与反馈表一致 会根据反馈的状态而变更，而领货时是0
	 */
	public void updateForChongZhiShenHe(Long opscwbid, Long nextbranchid, FlowOrderTypeEnum flowordertype, Long currentbranchid, CwbStateEnum cwbstate, DeliveryStateEnum deliverystate,
			BigDecimal infactfare) {
		this.jdbcTemplate.update("update express_ops_cwb_detail set nextbranchid=?,flowordertype=?" + ",currentbranchid=?,cwbstate=?,deliverystate=?,infactfare=? where opscwbid=?", nextbranchid,
				flowordertype.getValue(), currentbranchid, cwbstate.getValue(), deliverystate.getValue(), infactfare, opscwbid);

	}

	/**
	 * 修改金额 修改订单表字段
	 * 
	 * @param opscwbid
	 *            要修改的订单
	 * @param receivablefee
	 *            代收金额
	 * @param paybackfee
	 *            代退金额
	 */
	public void updateXiuGaiJinE(Long opscwbid, BigDecimal receivablefee, BigDecimal paybackfee) {
		this.jdbcTemplate.update("update express_ops_cwb_detail set receivablefee=?,paybackfee=? where opscwbid=?", receivablefee, paybackfee, opscwbid);
	}

	/**
	 * 修改订单支付方式 修改订单表字段
	 * 
	 * @param opscwbid
	 *            要修改的订单
	 * @param newpaywayid
	 *            订单要修改为的支付方式
	 */
	public void updateXiuGaiZhiFuFangShi(long opscwbid, int newpaywayid) {
		this.jdbcTemplate.update("update express_ops_cwb_detail set newpaywayid=? where opscwbid=?", newpaywayid, opscwbid);
	}

	/**
	 * 修改订单类型 修改订单表字段
	 * 
	 * @param opscwbid
	 * @param newcwbordertypeid
	 *            新的订单类型
	 * @param deliverystate
	 *            配送结果随着订单类型而变
	 */
	public void updateXiuGaiDingDanLeiXing(long opscwbid, int newcwbordertypeid, DeliveryStateEnum deliverystate) {
		this.jdbcTemplate.update("update express_ops_cwb_detail set cwbordertypeid=?,deliverystate=? where opscwbid=?", newcwbordertypeid, deliverystate.getValue(), opscwbid);

	}

	// ==============================修改订单使用的方法 end
	// ==================================

	/**
	 * 得到待返单出站的订单信息
	 * 
	 * @param type
	 * @param branchid
	 * @param isnow
	 * @return
	 */
	public List<CwbOrder> getCwbOrderByReturncwbsforTypeAndBranchidAndIsnow(long type, long branchid, long isnow, String nowtime, long timetype, String starttime, String endtime, long customerid,boolean flag) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT cd.*,ds.deliverytime as fankuitime,rc.createtime as shenhetime FROM ops_returncwbs rc,express_ops_cwb_detail cd,express_ops_delivery_state ds WHERE rc.`cwb`=cd.`cwb` AND rc.cwb=ds.cwb"
				+ " AND rc.`type`=? AND rc.`branchid`=? AND rc.`isnow`=? AND cd.`state`=1 AND ds.`state`=1");
		if (flag) {
			if ((timetype == 1) && !"".equals(starttime) && !"".equals(endtime)) {// 发货时间
				sb.append(" and cd.emaildate>='" + starttime + " 00:00:00' and cd.emaildate<='" + endtime + " 59:59:59' ");
			} else if ((timetype == 2) && !"".equals(starttime) && !"".equals(endtime)) {// 反馈时间
				sb.append(" and ds.deliverytime>='" + starttime + " 00:00:00' and ds.deliverytime<='" + endtime + " 59:59:59' ");
			} else if ((timetype == 3) && !"".equals(starttime) && !"".equals(endtime)) {// 审核时间||返单时间
				sb.append(" and rc.createtime>='" + starttime + " 00:00:00' and rc.createtime<='" + endtime + " 59:59:59' ");
			} else {// 今天待返单
				sb.append(" and rc.createtime>='" + nowtime + "' ");
			}
			if (customerid > 0) {
				sb.append(" and rc.customerid=" + customerid);
			}
		}else {
			sb.append(" and rc.createtime>='" + nowtime + "' ");
		}
		return this.jdbcTemplate.query(sb.toString(), new CwbFDMapper(), type, branchid, isnow);
	}

	/**
	 * 得到待返单入库的订单信息
	 *
	 * @param type
	 * @param tobranchid
	 * @param isnow
	 * @return
	 */
	public List<CwbOrder> getCwbOrderByReturncwbsforTypeAndToBranchidAndIsnow(long type, long tobranchid, String nowtime, long timetype, String starttime, String endtime, long customerid) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT cd.*,ds.deliverytime as fankuitime,ds.auditingtime as shenhetime,rc.createtime as chuzhantime FROM ops_returncwbs rc,express_ops_cwb_detail cd,express_ops_delivery_state ds WHERE rc.`cwb`=cd.`cwb` AND rc.cwb=ds.cwb"
				+ " AND rc.`type`=? AND rc.`tobranchid`=? AND cd.`state`=1 ");
		if ((timetype == 1) && !"".equals(starttime) && !"".equals(endtime)) {// 发货时间
			sb.append(" and cd.emaildate>='" + starttime + " 00:00:00' and cd.emaildate<='" + endtime + " 59:59:59' ");
		} else if ((timetype == 2) && !"".equals(starttime) && !"".equals(endtime)) {// 反馈时间
			sb.append(" and ds.deliverytime>='" + starttime + " 00:00:00' and ds.deliverytime<='" + endtime + " 59:59:59' ");
		} else if ((timetype == 3) && !"".equals(starttime) && !"".equals(endtime)) {// 审核时间
			sb.append(" and ds.auditingtime>='" + starttime + " 00:00:00' and ds.auditingtime<='" + endtime + " 59:59:59' ");
		} else if ((timetype == 4) && !"".equals(starttime) && !"".equals(endtime)) {// 出站
			sb.append(" and rc.createtime>='" + starttime + " 00:00:00' and rc.createtime<='" + endtime + " 59:59:59' ");
		} else {// 今天待返单
			sb.append(" and rc.createtime>='" + nowtime + "' ");
		}
		if (customerid > 0) {
			sb.append(" and rc.customerid=" + customerid);
		}
			sb.append(" AND rc.`isnow`='0' ");

		return this.jdbcTemplate.query(sb.toString(), new CwbFDMapper(), type, tobranchid);
	}
	
	/**
	 * 返单待入库
	 *
	 * 得到待返单入库的订单信息
	 * 
	 * @param type
	 * @param tobranchid
	 * @param isnow
	 * @return
	 *//*
	public List<CwbOrder> getReturnCwbOrder(long type, long tobranchid, long timetype, String starttime, String endtime, long customerid) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT cd.*,ds.deliverytime as fankuitime,ds.auditingtime as shenhetime,rc.createtime as chuzhantime FROM ops_returncwbs rc,express_ops_cwb_detail cd,express_ops_delivery_state ds WHERE rc.`cwb`=cd.`cwb` AND rc.cwb=ds.cwb"
				+ " AND rc.`type`=? AND rc.`tobranchid`=? AND cd.`state`=1 ");
		if ((timetype == 1) && !"".equals(starttime) && !"".equals(endtime)) {// 发货时间
			sb.append(" and cd.emaildate>='" + starttime + " 00:00:00' and cd.emaildate<='" + endtime + " 59:59:59' ");
		} else if ((timetype == 2) && !"".equals(starttime) && !"".equals(endtime)) {// 反馈时间
			sb.append(" and ds.deliverytime>='" + starttime + " 00:00:00' and ds.deliverytime<='" + endtime + " 59:59:59' ");
		} else if ((timetype == 3) && !"".equals(starttime) && !"".equals(endtime)) {// 审核时间
			sb.append(" and ds.auditingtime>='" + starttime + " 00:00:00' and ds.auditingtime<='" + endtime + " 59:59:59' ");
		} else if ((timetype == 4) && !"".equals(starttime) && !"".equals(endtime)) {// 出站
			sb.append(" and rc.createtime>='" + starttime + " 00:00:00' and rc.createtime<='" + endtime + " 59:59:59' ");
		} 
		if (customerid > 0) {
			sb.append(" and rc.customerid=" + customerid);
		}
		if (type != ReturnCwbsTypeEnum.FanDanRuKu.getValue()) {
			sb.append(" AND rc.`isnow`='0' ");
		}
		return this.jdbcTemplate.query(sb.toString(), new CwbFDMapper(), type, tobranchid);
	}*/
	/**
	 * 返单出库与待返单出库
	 * @param type
	 * @param tobranchid
	 * @param nowtime
	 * @param timetype
	 * @param starttime
	 * @param endtime
	 * @param customerid
	 * @param flag
	 * @return
	 */
	public List<CwbOrder> getFandanchukuOrDaiFanDanchukuList(long type, long branchid, String nowtime, long timetype, String starttime, String endtime, long customerid,boolean flag){
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT cd.*,ds.deliverytime as fankuitime,ds.auditingtime as shenhetime,rc.createtime as chuzhantime FROM ops_returncwbs rc,express_ops_cwb_detail cd,express_ops_delivery_state ds WHERE rc.`cwb`=cd.`cwb` AND rc.cwb=ds.cwb"
				+ " AND rc.`type`=? AND rc.`branchid`=? AND cd.`state`=1 ");
		if (flag) {
			if ((timetype == 1) && !"".equals(starttime) && !"".equals(endtime)) {// 发货时间
				sb.append(" and cd.emaildate>='" + starttime + " 00:00:00' and cd.emaildate<='" + endtime + " 59:59:59' ");
			} else if ((timetype == 2) && !"".equals(starttime) && !"".equals(endtime)) {// 反馈时间
				sb.append(" and ds.deliverytime>='" + starttime + " 00:00:00' and ds.deliverytime<='" + endtime + " 59:59:59' ");
			} else if ((timetype == 3) && !"".equals(starttime) && !"".equals(endtime)) {// 审核时间
				sb.append(" and ds.auditingtime>='" + starttime + " 00:00:00' and ds.auditingtime<='" + endtime + " 59:59:59' ");
			} else if ((timetype == 4) && !"".equals(starttime) && !"".equals(endtime)) {// 入库时间
				sb.append(" and rc.createtime>='" + starttime + " 00:00:00' and rc.createtime<='" + endtime + " 59:59:59' ");
			} else {// 今天待出库时间
				sb.append(" and rc.createtime>='" + nowtime + "' ");
			}
			if (customerid > 0) {
				sb.append(" and rc.customerid=" + customerid);
			}
		}else {
			sb.append(" and rc.createtime>='" + nowtime + "' ");
		}
		if (type!=ReturnCwbsTypeEnum.FanDanChuKu.getValue()) {
			sb.append(" AND rc.`isnow`='0' ");
		}

		return this.jdbcTemplate.query(sb.toString(), new CwbFDMapper(), type, branchid);
	}
	
	
	/**
	 * 获得供货商拒收返库 待出库数量
	 * 
	 * @param branchid
	 * @return
	 */

	public long getCustomerRefusedCount(long branchid) {
		String sql = "SELECT COUNT(1) FROM express_ops_cwb_detail WHERE startbranchid =? and flowordertype=?  and state=1 ";
		return this.jdbcTemplate.queryForLong(sql, branchid, FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue());
	}

	/**
	 * 获得供货商拒收返库 已出库数量
	 * 
	 * @param branchid
	 * @return
	 */
	public long getCustomerRefusedForScan(long branchid) {
		String sql = "SELECT COUNT(1) FROM express_ops_cwb_detail WHERE currentbranchid =?  and flowordertype=? and state=1 ";
		return this.jdbcTemplate.queryForLong(sql, branchid, FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue());
	}

	/**
	 * 获得供货商拒收返库 待出库订单
	 * 
	 * @param branchid
	 * @return
	 */
	public List<CwbOrder> getCustomerRefusedListByBranchid(long branchid) {
		String sql = "SELECT * FROM express_ops_cwb_detail WHERE startbranchid =? and flowordertype=?  and state=1 ";
		return this.jdbcTemplate.query(sql, new CwbMapper(), branchid, FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue());
	}

	/**
	 * 获取 供货商拒收返库 已扫描
	 * 
	 * @param branchid
	 * @return
	 */
	public List<CwbOrder> getCustomerRefusedListForScan(long branchid) {
		String sql = "SELECT * FROM express_ops_cwb_detail WHERE currentbranchid =?  and flowordertype=? and state=1 ";
		return this.jdbcTemplate.query(sql, new CwbMapper(), branchid, FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue());
	}

	// 供货商发货汇总list
	public List<CwbOrder> getCustomerfahuodataList(long page, long customerid, long kufangid, String begindate, String enddate) {
		String sql = "select * from express_ops_cwb_detail where state=1 and customerid=?" + " and emaildate >=? and emaildate <=? ";

		if (kufangid > 0) {
			sql += " and carwarehouse = " + kufangid;
		}
		sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		return this.jdbcTemplate.query(sql, new CwbMapper(), customerid, begindate, enddate);
	}

	// 供货商发货汇总总数
	public long getCustomerfahuodataCount(long customerid, long kufangid, String begindate, String enddate) {
		String sql = "select count(1) from express_ops_cwb_detail where state=1 and customerid=?" + " and emaildate >=? and emaildate <= ?";

		if (kufangid > 0) {
			sql += " and carwarehouse = " + kufangid;
		}
		return this.jdbcTemplate.queryForLong(sql, customerid, begindate, enddate);
	}

	// 供货商发货汇总导出sql
	public String getCustomerfahuodataSql(long customerid, long kufangid, String begindate, String enddate) {
		String sql = "select * from express_ops_cwb_detail where state=1 and customerid=" + customerid + " and emaildate >='" + begindate + "' and emaildate <= '" + enddate + "'";

		if (kufangid > 0) {
			sql += " and carwarehouse = " + kufangid;
		}
		return sql;
	}

	public String getSQLExportComplaint(String cwbs, String consigneename, String consigneemobile, String consigneeaddress, String begindate, String enddate) {
		String sql = "select * from express_ops_cwb_detail where state=1  ";
		if (cwbs.length() > 0) {
			sql += " and cwb in (" + cwbs + ")";
		} else {
			if (begindate.length() > 0) {
				sql += " and  emaildate >='" + begindate + "'";
			}
			if (enddate.length() > 0) {
				sql += " and emaildate <='" + enddate + "'";
			}
			if (consigneename.length() > 0) {
				sql += " and consigneename = '" + consigneename + "'";
			}
			if (consigneemobile.length() > 0) {
				sql += " and consigneemobile = '" + consigneemobile + "'";
			}
			if (consigneeaddress.length() > 0) {
				sql += " and consigneeaddress like '%" + consigneeaddress + "%'";
			}
		}
		return sql;
	}

	/**
	 * 用于 入库 导出
	 * 
	 * @param customerid
	 * @param b
	 * @return
	 */
	public String getSqlExportByCusromeridweiruku(long customerid, Branch b) {
		String sql = "SELECT * FROM express_ops_cwb_detail WHERE  nextbranchid =" + b.getBranchid() + " and currentbranchid=0 and state=1 and flowordertype<>"
				+ FlowOrderTypeEnum.FenZhanLingHuo.getValue();
		if (b.getSitetype() == BranchEnum.ZhanDian.getValue()) {
			sql = "SELECT * FROM express_ops_cwb_detail WHERE nextbranchid =" + b.getBranchid() + " and currentbranchid=0 and flowordertype='" + FlowOrderTypeEnum.ChuKuSaoMiao.getValue()
					+ "' and state=1 ";
		}
		if (b.getSitetype() == BranchEnum.ZhongZhuan.getValue()) {
			sql = "SELECT * FROM express_ops_cwb_detail WHERE  nextbranchid =" + b.getBranchid() + " and currentbranchid=0 and state=1 and flowordertype=" + FlowOrderTypeEnum.ChuKuSaoMiao.getValue();
		}
		if (customerid > 0) {
			sql += " and customerid=" + customerid;
		}
		return sql;
	}

	/**
	 * 已入库 导出 sql
	 * 
	 * @param customerid
	 * @param b
	 * @return
	 */
	public String getSqlExportByCusromeridyiruku(long customerid, Branch b) {
		String sql = "SELECT * FROM express_ops_cwb_detail WHERE currentbranchid=" + b.getBranchid() + " and state=1 and flowordertype=" + FlowOrderTypeEnum.RuKu.getValue();
		if (b.getSitetype() == BranchEnum.ZhongZhuan.getValue()) {
			sql = "SELECT * FROM express_ops_cwb_detail WHERE currentbranchid=" + b.getBranchid() + " and state=1 and flowordertype=" + FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue();
		}

		if (customerid > 0) {
			sql += " and customerid =" + customerid;
		}
		return sql;
	}

	/**
	 * 未出库 导出 sql
	 * 
	 * @param branchid
	 * @param b
	 * @param cwbstate
	 * @return
	 */
	public String getSqlExportByBranchidweichuku(long branchid, Branch b, int cwbstate) {
		String sql = "";
		if (branchid > 0) {
			sql = "SELECT * FROM express_ops_cwb_detail WHERE currentbranchid=" + b.getBranchid() + " and nextbranchid=" + branchid + "  and flowordertype<>" + FlowOrderTypeEnum.TiHuo.getValue()
					+ " and state=1 ";
		} else {
			sql = "SELECT * FROM express_ops_cwb_detail WHERE currentbranchid=" + b.getBranchid() + " and nextbranchid<>0  and flowordertype<>" + FlowOrderTypeEnum.TiHuo.getValue() + " and state=1 ";
		}
		if (cwbstate > -1) {
			sql += " and cwbstate=" + cwbstate;
		}
		return sql;
	}

	/**
	 * 已出库 导出 sql
	 * 
	 * @param startbranchid
	 * @param nextbranchid
	 * @param value
	 * @return
	 */
	public String getSqlExportByBranchidyichuku(long startbranchid, long nextbranchid, int flowordertype) {
		String sql = "SELECT * FROM express_ops_cwb_detail WHERE startbranchid=" + startbranchid + " and flowordertype=" + flowordertype + " and state=1 ";
		if (nextbranchid > 0) {
			sql += " and nextbranchid=" + nextbranchid;
		}
		return sql;
	}

	/*
	 * 更新订单确认 电信亚联
	 */
	public void updateYalianApp(String cwb, String remark) {
		String sql = " update express_ops_cwb_detail set remark3=?  WHERE cwb=? and state=1 ";

		this.jdbcTemplate.update(sql, remark, cwb);
	}

	/**
	 * 退供货商 已出库
	 * 
	 * @param branchid
	 * @param page
	 * @return
	 */
	public List<CwbOrder> getTuiGongHuoShangYiChuKu(long branchid, long page) {
		String sql = "SELECT b.* FROM express_ops_operation_time a LEFT JOIN express_ops_cwb_detail b ON a.cwb=b.cwb WHERE a.branchid=" + branchid + " AND a.flowordertype="
				+ FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue() + " AND b.state=1 ";
		sql += " limit " + ((page - 1) * Page.DETAIL_PAGE_NUMBER) + "," + Page.DETAIL_PAGE_NUMBER;
		return this.jdbcTemplate.query(sql, new CwbMapper());
	}

	public List<CwbOrder> getTuiGongHuoShangYiChuKu(long branchid, long page, long cwbordertypeid) {
		String sql = "SELECT b.* FROM express_ops_operation_time a LEFT JOIN express_ops_cwb_detail b ON a.cwb=b.cwb WHERE a.branchid=" + branchid + " AND a.flowordertype="
				+ FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue() + " AND b.state=1 and b.cwbordertypeid=" + cwbordertypeid;
		sql += " limit " + ((page - 1) * Page.DETAIL_PAGE_NUMBER) + "," + Page.DETAIL_PAGE_NUMBER;
		return this.jdbcTemplate.query(sql, new CwbMapper());
	}

	/**
	 * 退供货商 已出库 导出
	 * 
	 * @param branchid
	 * @return
	 */
	public String getSqlExportBackToCustomerYichuku(long branchid) {
		String sql = "SELECT b.* FROM express_ops_operation_time a LEFT JOIN express_ops_cwb_detail b ON a.cwb=b.cwb WHERE a.branchid=" + branchid + " AND a.flowordertype="
				+ FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue() + " AND b.state=1 ";
		return sql;
	}

	/**
	 * 得到退供货商 已出库 件数
	 * 
	 * @param branchid
	 * @return
	 */
	public long getTGYSYCK(long branchid) {
		String sql = "SELECT count(1) FROM express_ops_operation_time  WHERE branchid=" + branchid + " AND flowordertype=" + FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue();
		return this.jdbcTemplate.queryForLong(sql);
	}

	public Smtcount getTGYSYCKsmt(long branchid) {
		String sql = "SELECT COUNT(1) count,"
				+ "(CASE when sum(CASE WHEN a.cwbordertypeid=1  THEN 1  else 0 END ) is null then 0 else sum(CASE WHEN a.cwbordertypeid=1  THEN 1  else 0 END ) end) as pscount,"
				+ "(CASE when sum(CASE WHEN a.cwbordertypeid=2  THEN 1  else 0 END ) is null then 0 else sum(CASE WHEN a.cwbordertypeid=2  THEN 1  else 0 END ) end) as smtcount,"
				+ "(CASE when sum(CASE WHEN a.cwbordertypeid=3  THEN 1  else 0 END ) is null then 0 else sum(CASE WHEN a.cwbordertypeid=3  THEN 1  else 0 END ) end) as smhcount"
				+ " FROM express_ops_cwb_detail a  , express_ops_operation_time b  FORCE INDEX(OTime_Branchid_Idx) ";
		sql += " WHERE a.cwb=b.cwb and a.state=1 and b.branchid=" + branchid + " AND b.flowordertype=" + FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue();
		return this.jdbcTemplate.queryForObject(sql, new SmtCountMapper());
	}

	/**
	 * 退供货商 未出库 导出
	 * 
	 * @param branchid
	 * @return
	 */
	public String getSqlExportBackToCustomerWeichuku(long branchid) {
		String sql = "SELECT * FROM express_ops_cwb_detail WHERE currentbranchid =" + branchid + " and cwbstate=" + CwbStateEnum.TuiGongYingShang.getValue() + "  and flowordertype<>"
				+ FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue() + " and state=1 ";
		return sql;
	}

	/**
	 * 中转出站打印 添加时间选项
	 * 
	 * @param page
	 * @param startbranchid
	 * @param nextbranchid
	 * @param flowordertype
	 * @param strtime
	 * @param endtime
	 * @return
	 */

	public List<String> getCwbByYPDJ(String customerids, String cwbs) {
		return this.jdbcTemplate.queryForList("SELECT cwb from express_ops_cwb_detail where cwb in(" + cwbs + ") " + "and state=1 and customerid in(" + customerids
				+ ") and sendcarnum<>scannum and cwbordertypeid=1 ", String.class);
	}

	public void updateDeliveryBranchidByCwb(String excelbranch, long branchid, String cwb) {
		this.jdbcTemplate.update("update express_ops_cwb_detail set excelbranch=?,deliverybranchid=?  where state =1 and cwb=?", excelbranch, branchid, cwb);
	}

	public void saveTranscwbByCwb(String transcwb, String cwb) {
		String sql = "update express_ops_cwb_detail set transcwb=? where cwb=? and state=1";
		this.jdbcTemplate.update(sql, transcwb, cwb);
	}

	public void updateCwbRemark5(String cwb, String remark5) {
		String sql = "update express_ops_cwb_detail set remark5=? where cwb=? and state=1 ";
		this.jdbcTemplate.update(sql, remark5, cwb);
	}

	public List<CwbOrder> getIntoCwbByCwbsPage(long page, String cwbs, String customers, long cwbordertypeid, String emaildatebegin, String emaildateend) {
		String sql = "SELECT * from express_ops_cwb_detail where cwb in(" + cwbs + ") and state=1 ";
		if (customers.length() > 0) {
			sql += " and customerid in(" + customers + ")";
		}

		if (cwbordertypeid > -2) {
			sql += " and cwbordertypeid=" + cwbordertypeid;
		}

		if (emaildatebegin.length() > 0) {
			sql += " and emaildate>='" + emaildatebegin + "'";
		}
		if (emaildateend.length() > 0) {
			sql += " and emaildate<='" + emaildateend + "'";
		}
		sql += " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		return this.jdbcTemplate.query(sql, new CwbMapper());
	}

	public long getIntoCwbByCwbsCount(String cwbs, String customers, long cwbordertypeid, String emaildatebegin, String emaildateend) {
		String sql = "SELECT count(1) from express_ops_cwb_detail where cwb in(" + cwbs + ") and state=1 ";
		if (customers.length() > 0) {
			sql += " and customerid in(" + customers + ")";
		}

		if (cwbordertypeid > -2) {
			sql += " and cwbordertypeid=" + cwbordertypeid;
		}

		if (emaildatebegin.length() > 0) {
			sql += " and emaildate>='" + emaildatebegin + "'";
		}
		if (emaildateend.length() > 0) {
			sql += " and emaildate<='" + emaildateend + "'";
		}
		return this.jdbcTemplate.queryForLong(sql);
	}

	public String getIntoCwbByCwbsSql(long page, String cwbs, String customers, long cwbordertypeid, String emaildatebegin, String emaildateend) {
		String sql = "SELECT * from express_ops_cwb_detail where cwb in(" + cwbs + ") and state=1 ";
		if (customers.length() > 0) {
			sql += " and customerid in(" + customers + ")";
		}

		if (cwbordertypeid > -2) {
			sql += " and cwbordertypeid=" + cwbordertypeid;
		}

		if (emaildatebegin.length() > 0) {
			sql += " and emaildate>='" + emaildatebegin + "'";
		}
		if (emaildateend.length() > 0) {
			sql += " and emaildate<='" + emaildateend + "'";
		}
		sql += " limit " + page + " ," + Page.EXCEL_PAGE_NUMBER;
		return sql;
	}

	public List<String> getCwbsListByEmailDateId(long emaildateid) {
		String sql = "SELECT cwb FROM express_ops_cwb_detail WHERE emaildateid=? and state=1";
		return this.jdbcTemplate.queryForList(sql, String.class, emaildateid);

	}

	public List<CwbOrder> getCwbOrderNotDetailByCwbs(String cwbs) {
		String sql = "select startbranchid,nextbranchid,flowordertype,cwb,currentbranchid,deliverid,customerid from express_ops_cwb_detail where cwb in(" + cwbs + ") and state=1";
		return this.jdbcTemplate.query(sql, new CwbDetailNotDetailMapper());
	}

	/**
	 * 库房出库统计 发货库房 支持多选
	 * 
	 * @param begindate
	 * @param enddate
	 * @param orderName
	 * @param customerids
	 * @param kufangids
	 * @param nextbranchids
	 * @param cwbordertypeids
	 * @param i
	 * @return
	 */
	public long getcwbOrderByOutWarehouseCountNew(String begindate, String enddate, String orderName, String customerids, String kufangids, String nextbranchids, String cwbordertypeids, int type) {
		String sql = "select count(1) from express_ops_warehouse_to_branch as wtb FORCE INDEX(WAREcredateIdx) left join "
				+ "express_ops_cwb_detail as de on wtb.cwb=de.cwb where de.state=1 and wtb.credate >=? and wtb.credate <=?";

		sql = this.getcwbOrderByOutWarehouseSqlNew(sql, customerids, kufangids, nextbranchids, cwbordertypeids, type);
		return this.jdbcTemplate.queryForLong(sql, begindate, enddate);
	}

	public CwbOrder getcwbOrderByOutWarehouseSumNew(String begindate, String enddate, String orderName, String customerids, String kufangids, String nextbranchids, String cwbordertypeids, int type) {
		String sql = "select  sum(de.receivablefee) as receivablefee,sum(de.paybackfee) as paybackfee  from express_ops_warehouse_to_branch as wtb FORCE INDEX(WAREcredateIdx) left join "
				+ "express_ops_cwb_detail as de on wtb.cwb=de.cwb where de.state=1 and wtb.credate >=? and wtb.credate <=?";
		sql = this.getcwbOrderByOutWarehouseSqlNew(sql, customerids, kufangids, nextbranchids, cwbordertypeids, type);
		try {
			return this.jdbcTemplate.queryForObject(sql, new CwbMOneyMapper(), begindate, enddate);
		} catch (DataAccessException e) {
			return new CwbOrder();
		}
	}

	public List<CwbOrder> getcwbOrderByOutWarehouseNew(long page, String begindate, String enddate, String orderName, String customerids, String kufangids, String nextbranchids,
			String cwbordertypeids, int type) {
		String sql = "select de.* from express_ops_warehouse_to_branch as wtb FORCE INDEX(WAREcredateIdx) left join "
				+ "express_ops_cwb_detail as de on wtb.cwb=de.cwb where de.state=1 and wtb.credate >=? and wtb.credate <=?";

		sql = this.getcwbOrderByOutWarehouseSqlNew(sql, customerids, kufangids, nextbranchids, cwbordertypeids, type);
		sql += " order by " + orderName + " limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER;
		return this.jdbcTemplate.query(sql, new CwbMapper(), begindate, enddate);

	}

	private String getcwbOrderByOutWarehouseSqlNew(String sql, String customerids, String kufangids, String nextbranchids, String cwbordertypeids, int type) {
		if ((customerids.length() > 0) || (kufangids.length() > 0) || (nextbranchids.length() > 0) || (cwbordertypeids.length() > 0) || (type > 0)) {
			if (customerids.length() > 0) {
				sql += " and de.customerid in(" + customerids + ")";
			}
			if (kufangids.length() > 0) {
				sql += " and wtb.startbranchid in(" + kufangids + ")";
			}
			if (nextbranchids.length() > 0) {
				sql += " and wtb.nextbranchid in(" + nextbranchids + ")";
			}
			if (cwbordertypeids.length() > 0) {
				sql += " and de.cwbordertypeid in(" + cwbordertypeids + ")";
			}
			if (type > 0) {
				sql += " and wtb.type =" + type;
			}
		}
		return sql;
	}

	/**
	 * 库房出库 发货库房支持多选 导出
	 * 
	 * @param page
	 * @param begindate
	 * @param enddate
	 * @param customerids
	 * @param kufangdis
	 * @param nextbranchids
	 * @param cwbordertypeids
	 * @param i
	 * @return
	 */
	public String getcwbOrderByOutWarehouseSqlNew(long page, String begindate, String enddate, String customerids, String kufangdis, String nextbranchids, String cwbordertypeids, int type) {
		String sql = "select de.* from express_ops_warehouse_to_branch as wtb FORCE INDEX(WAREcredateIdx) left join "
				+ "express_ops_cwb_detail as de on wtb.cwb=de.cwb where de.state=1 and wtb.credate >='" + begindate + "' and wtb.credate <='" + enddate + "'";
		sql = this.getcwbOrderByOutWarehouseSqlNew(sql, customerids, kufangdis, nextbranchids, cwbordertypeids, type);
		sql += " limit " + page + " ," + Page.EXCEL_PAGE_NUMBER;
		return sql;
	}

	// ==========================小件员领货查询====================================
	/**
	 * * 小件员领货查询List
	 * 
	 * @param page
	 * @param deliveryid
	 * @param start
	 * @param end
	 * @param deliverystate
	 * @return List
	 */

	public List<CwbOrder> getCwbByDeliveryStateAndBranchid(long deliveryid, long deliverystate) {
		String sql = "SELECT * " + "FROM  express_ops_cwb_detail  WHERE 1=1";
		if (deliveryid > 0) {
			sql += " and deliverid=" + deliveryid;
		}
		if (deliverystate > 0) {
			sql += " and deliverystate=" + deliverystate;
		}
		sql += "  and state=1 ";
		// 有性能问题，暂时不能使用
		return null;// jdbcTemplate.query(sql,new CwbMapper());
	}

	// =================================================修改签收人===========================================================================

	public int countDeliverystateAndCwb(String cwb) {
		String sql = "SELECT count(1) FROM `express_ops_cwb_detail` WHERE cwb=? and flowordertype=35 AND deliverystate=1 AND state=1 ";
		return this.jdbcTemplate.queryForInt(sql, cwb);

	}

	// =================================================打印乐蜂网面单===========================================================================

	public List<CwbOrder> queryByCwbAndType(String cwb, long type) {
		String sql = "SELECT * from express_ops_cwb_detail where cwb in(" + cwb + ") and state=1 and  cwbordertypeid=? ORDER BY CONVERT( consigneeaddress USING gbk ) COLLATE gbk_chinese_ci ASC";
		return this.jdbcTemplate.query(sql, new CwbMapper(), type);
	}

	public List<CwbOrder> getCwbOrderByBaleid(long baleid, long page) {
		return this.jdbcTemplate.query("select de.* from express_ops_bale_cwb as bw left join express_ops_cwb_detail de on bw.cwb=de.cwb" + " where de.state=1 and bw.baleid='" + baleid + "' "
				+ "limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER, new CwbMapper());
	}

	public long getCwbOrderByBaleidCount(long baleid) {
		return this.jdbcTemplate.queryForLong("select count(1) from express_ops_bale_cwb as bw left join express_ops_cwb_detail de on bw.cwb=de.cwb" + " where de.state=1 and bw.baleid='" + baleid
				+ "' ");
	}

	/**
	 * 出库打印 按照站点分开
	 * 
	 * @param cwbs
	 * @param nextbranchid
	 * @return
	 */
	public List<CwbOrder> getCwbByCwbsForPrint(String cwbs, String nextbranchid, long branchid, long flowordertype) {
		String sql = "SELECT cd.cwb,cd.customerid,cd.cwbordertypeid,cd.sendcarnum,cd.backcarnum,cd.caramount,cd.consigneename,"
				+ "cd.consigneeaddress,cd.consigneepostcode,cd.consigneemobile,cd.consigneephone,"
				+ "cd.receivablefee,cd.paybackfee,cd.carsize,cd.paywayid,cd.cwbremark,cd.carrealweight, op.nextbranchid AS nextbranchid "
				+ "FROM express_ops_groupdetail op LEFT JOIN express_ops_cwb_detail cd ON cd.cwb=op.cwb where op.cwb in(" + cwbs + ") " + "and op.nextbranchid in(" + nextbranchid
				+ ") and op.branchid=" + branchid + " and cd.state=1 and op.flowordertype=" + flowordertype;
		return this.jdbcTemplate.query(sql, new CwbForChuKuPrintMapper());
	}

	/**
	 * 分站到货 已到货
	 * 
	 * @param yidaohuo
	 * @param page
	 * @return
	 */
	public List<CwbOrder> getYiDaohuobyCwbsForFenzhandaohuo(String yidaohuo, long page) {
		String sql = "SELECT * FROM express_ops_cwb_detail WHERE  state=1 and cwb in (" + yidaohuo + ")";
		sql += " limit ?,?";
		return this.jdbcTemplate.query(sql, new CwbMapper(), (page - 1) * Page.DETAIL_PAGE_NUMBER, Page.DETAIL_PAGE_NUMBER);
	}

	/**
	 * 揽收 更新数据
	 * 
	 * @param cwb
	 * @param consigneename
	 * @param consigneeaddress
	 * @param consigneemobile
	 * @param cwbcity
	 * @param consigneepostcode
	 * @param sendcarname
	 * @param sendcarnum
	 * @param carrealweight
	 * @param receivablefee
	 * @param carsize
	 * @param sendconsigneephone
	 */
	public void updateCwbByParams(String cwb, String consigneename, String consigneeaddress, String consigneemobile, String cwbcity, String consigneepostcode, String sendcarname, long sendcarnum,
			BigDecimal carrealweight, BigDecimal receivablefee, String carsize, String sendconsigneephone) {
		String sql = " update express_ops_cwb_detail set  consigneename=?,consigneeaddress=?," + " consigneemobile=?,cwbcity=?,consigneepostcode=?,sendcarname=?,sendcarnum=?,"
				+ "carrealweight=?,receivablefee=?,carsize=?,consigneephone=?  where cwb=?";
		this.jdbcTemplate.update(sql, consigneename, consigneeaddress, consigneemobile, cwbcity, consigneepostcode, sendcarname, sendcarnum, carrealweight, receivablefee, carsize, sendconsigneephone,
				cwb);
	}

	// ===========================新增修改匹配站新页面=====================

	public List<CwbOrder> getcwborderListIsNotAddress(long page, long onePageNumber, long customerid, String beginemaildate, String endemaildate, String ordercwb, long emaildateid,
			CwbOrderAddressCodeEditTypeEnum addressCodeEditType, long flowordertype) {
		String sql = "select * from express_ops_cwb_detail where state=1 ";
		StringBuffer w = new StringBuffer();
		if (ordercwb.trim().length() > 0) {
			w.append(" and cwb='" + ordercwb + "'");
		} else if ((customerid > 0) || (beginemaildate.length() > 0) || (endemaildate.length() > 0) || (emaildateid > 0)) {
			if (customerid > 0) {
				w.append(" and customerid=" + customerid);
			}
			if (emaildateid > 0) {
				w.append(" and emaildateid='" + emaildateid + "'");
			}
			if (beginemaildate.length() > 0) {
				w.append(" and emaildate >='" + beginemaildate + "'");
			}
			if (endemaildate.length() > 0) {
				w.append(" and emaildate <= '" + endemaildate + "'");
			}
			if (flowordertype > 0) {
				w.append(" and flowordertype=" + flowordertype);
			}
		}
		sql += w.toString();

		if (addressCodeEditType != null) {
			sql += " and addresscodeedittype=" + addressCodeEditType.getValue();
		}
		sql += " order by excelbranch desc ";
		if (page > 0) {
			sql += " limit " + ((page - 1) * onePageNumber) + " ," + onePageNumber;
		}

		this.logger.info("sql:" + sql);
		List<CwbOrder> cwborderList = this.jdbcTemplate.query(sql, new CwbMapper());
		return cwborderList;
	}

	public long getcwbordercountIsNotAddress(long page, long onePageNumber, long customerid, String beginemaildate, String endemaildate, String ordercwb, long emaildateid,
			CwbOrderAddressCodeEditTypeEnum addressCodeEditType, long flowordertype) {
		String sql = "select count(1) from express_ops_cwb_detail where state=1 ";
		StringBuffer w = new StringBuffer();
		if (ordercwb.trim().length() > 0) {
			w.append(" and cwb='" + ordercwb + "'");
		} else if ((customerid > 0) || (beginemaildate.length() > 0) || (endemaildate.length() > 0) || (emaildateid > 0)) {
			if (customerid > 0) {
				w.append(" and customerid=" + customerid);
			}
			if (emaildateid > 0) {
				w.append(" and emaildateid='" + emaildateid + "'");
			}
			if (beginemaildate.length() > 0) {
				w.append(" and emaildate >='" + beginemaildate + "'");
			}
			if (endemaildate.length() > 0) {
				w.append(" and emaildate <= '" + endemaildate + "'");
			}
			if (flowordertype > 0) {
				w.append(" and flowordertype=" + flowordertype);
			}
		}
		sql += w.toString();

		if (addressCodeEditType != null) {
			sql += " and addresscodeedittype=" + addressCodeEditType.getValue();
		}

		this.logger.info("sql:" + sql);
		long a = this.jdbcTemplate.queryForLong(sql);
		return a;
	}

	public long getEditInfoCountIsNotAddress(String ordercwb, String type) {
		String sql = "select count(1) from express_ops_importedit where 1=1 ";
		if (ordercwb.trim().length() > 0) {
			sql += " and cwb=" + ordercwb;
		} else if (type.length() > 0) {
			sql += " and addresscodeedittype in(" + type + ")";
		}
		this.logger.info("已匹配sql:" + sql);
		return this.jdbcTemplate.queryForInt(sql);
	}

	/**
	 * 根据订单号 和 当前环节
	 * 
	 * @param string
	 * @param strings
	 * @return
	 */
	public List<CwbOrder> getCwbOrderByFlowordertypeAndCwbs(long branchid, String types, String cwbs) {
		String sql = "SELECT * FROM express_ops_cwb_detail WHERE currentbranchid=" + branchid + " and state=1 and cwb in (" + cwbs + ") and flowordertype in (" + types + ")";
		return this.jdbcTemplate.query(sql, new CwbMapper());
	}

	public List<CwbOrder> getCwbOrderByDeliverystateAndCwbs(int deliverystate, String historyzhiliucwbs) {

		String sql = "SELECT * FROM express_ops_cwb_detail WHERE  state=1 and cwb in (" + historyzhiliucwbs + ") and deliverystate = " + deliverystate;
		return this.jdbcTemplate.query(sql, new CwbMapper());
	}

	/**
	 * 合单修改multi_shipcwb
	 * 
	 */
	public void appendTranscwb(String cwb, String transcwb) {
		try {
			String sql = "update express_ops_cwb_detail set  sendcarnum=sendcarnum+1,multi_shipcwb=CONCAT(IF(multi_shipcwb IS NULL,'',multi_shipcwb),'," + transcwb
					+ "'), transcwb=CONCAT(IF(transcwb IS NULL,'',transcwb),'," + transcwb + "') where cwb='" + cwb + "' ";
			this.jdbcTemplate.update(sql);
			this.logger.info("订单[一件多票]追加成功，存储系统中为普通单子，sql={}", sql);
		} catch (DataAccessException e) {
			this.logger.error("订单[一件多票]追加失败", e);
		}
	}

	public List<CwbOrder> getCwbOrderByDelivery(String cwbs) {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from express_ops_cwb_detail where state=1");
		if (!"".equals(cwbs)) {
			sql.append(" and cwb in(" + cwbs + ")");
			String ordercwbs = "'" + cwbs.replace("'", "") + "'";
			sql.append(" ORDER BY FIND_IN_SET(cwb," + ordercwbs + ")");

		}
		return this.jdbcTemplate.query(sql.toString(), new CwbMapper());
	}

	/**
	 * 修改客户备注信息 remark1 remark2
	 * 
	 * @param cwb
	 * @param cwbstate
	 */
	public void updateCwbRemark1AndRemark2(String cwb, String remark1, String remark2) {
		String sql = "update express_ops_cwb_detail set remark1=?,remark2=? where cwb=? and state=1 ";
		this.jdbcTemplate.update(sql, remark1, remark2, cwb);
	}

	/**
	 * 根据条件查询客户单量
	 * 
	 * @param branchid
	 * @return
	 */
	public List<Map<String, Object>> getCwbStasticsByTime(String searchtime) {
		return this.jdbcTemplate
				.queryForList(
						"SELECT COUNT(1) AS counts,LEFT(emaildate,10) AS credate FROM express_ops_cwb_detail WHERE state=1 AND flowordertype>1 AND emaildate>? GROUP BY LEFT(emaildate,10) ORDER BY LEFT(emaildate,10) ",
						searchtime);
	}

	/**
	 * 根据条件查询 客户发货统计
	 * 
	 * @param branchid
	 * @return
	 */
	public List<Map<String, Object>> getFahuoStastics(String starttime, String endtime) {
		return this.jdbcTemplate
				.queryForList("SELECT COUNT(1) AS counts,LEFT(emaildate,10) AS credate,d.customerid,customername ," + " SUM(CASE WHEN cwbordertypeid=1 THEN 1 ELSE 0 END) AS peisongcount,"
						+ " SUM(CASE WHEN cwbordertypeid=2 THEN 1 ELSE 0 END) AS shangmentuicount," + " SUM(CASE WHEN cwbordertypeid=3 THEN 1 ELSE 0 END) AS shangmenhuancount "
						+ " FROM express_ops_cwb_detail d,express_set_customer_info c WHERE state=1 AND d.customerid=c.customerid  AND emaildate>=? AND emaildate<=? GROUP BY customerid  ", starttime,
						endtime);
	}

	public List<String> getListByEmaildateId(long branchid, long sitetype, long page, long customerid, long emaildateid) {
		String sql = "SELECT cwb FROM express_ops_cwb_detail WHERE  nextbranchid =? and currentbranchid=0 and state=1 and flowordertype<>" + FlowOrderTypeEnum.FenZhanLingHuo.getValue();
		if (sitetype == BranchEnum.ZhanDian.getValue()) {
			sql = "SELECT cwb FROM express_ops_cwb_detail WHERE nextbranchid =? and currentbranchid=0 and flowordertype='" + FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "' and state=1 ";
		}
		if (sitetype == BranchEnum.ZhongZhuan.getValue()) {
			sql = "SELECT cwb FROM express_ops_cwb_detail WHERE  nextbranchid =? and currentbranchid=0 and state=1 and flowordertype=" + FlowOrderTypeEnum.ChuKuSaoMiao.getValue();
		}
		if (customerid > 0) {
			sql += " and customerid=" + customerid;
		}
		if (emaildateid > 0) {
			sql += " and emaildateid =" + emaildateid;
		}
		return this.jdbcTemplate.queryForList(sql, String.class, branchid);
	}

	public void updatePackagecodeAndNextbranchid(String packagecode, long branchid, String cwb) {
		this.jdbcTemplate.update("update express_ops_cwb_detail set packagecode=?,nextbranchid=? where cwb=? and state=1 ", packagecode, branchid, cwb);
	}

	public void updateCwbInfactFare(String cwb, BigDecimal infactfare) {
		String sql = "update express_ops_cwb_detail set infactfare=? where cwb=? and state=1 ";
		this.jdbcTemplate.update(sql, infactfare, cwb);
	}

	public void updateBycwb(final Map<String, String> order) {
		String sql = "update express_ops_cwb_detail set consigneename=? ,sendcarnum=?,consigneemobile=?,consigneephone=?,consigneepostcode=?,"
				+ "consigneeaddress=?,receivablefee=?,customercommand=?,remark1=?,remark2=?,remark3=?,remark4=?,remark5=?,carrealweight=?,paywayid=?," + "cartype=?,cwbordertypeid=?,shouldfare=? "
				+ " where cwb =? and state=1  ";
		this.jdbcTemplate.update(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, order.get("consigneename").toString());
				ps.setString(2, order.get("sendcarnum").toString());
				ps.setString(3, order.get("consigneemobile").toString());
				ps.setString(4, order.get("consigneephone").toString());
				ps.setString(5, order.get("consigneepostcode").toString());

				ps.setString(6, order.get("consigneeaddress").toString());
				ps.setString(7, order.get("receivablefee").toString());
				ps.setString(8, order.get("customercommand").toString());
				ps.setString(9, order.get("remark1").toString());
				ps.setString(10, order.get("remark2").toString());
				ps.setString(11, order.get("remark3").toString());
				ps.setString(12, order.get("remark4").toString());
				ps.setString(13, order.get("remark5").toString());
				ps.setString(14, order.get("cargorealweight").toString());
				ps.setString(15, order.get("paywayid").toString());

				ps.setString(16, order.get("cargotype").toString());
				ps.setString(17, order.get("cwbordertypeid").toString());
				ps.setString(18, order.get("shouldfare").toString());
				ps.setString(19, order.get("cwb").toString());
			}
		});

	}

	// public long getCountPackagecode(String packagecode){
	// return
	// jdbcTemplate.queryForLong("select count(1) from express_ops_cwb_detail where packagecode=? ",packagecode);
	// }

	public List<Map<String, Object>> getCwbByPrintCwbs(String cwbs) {
		String sql = "SELECT COUNT(1) count,SUM(receivablefee) receivablefee,sum(carrealweight) carrealweight,sum(sendcarnum) sendcarnum " + "FROM express_ops_cwb_detail WHERE state=1 and cwb IN("
				+ cwbs + ") ";
		return this.jdbcTemplate.queryForList(sql);
	}

	public void updateCwbStateByIds(String opscwbids, CwbStateEnum cwbstate) {
		String sql = "update express_ops_cwb_detail set cwbstate=? where opscwbid in (" + opscwbids + ") and state=1";
		this.jdbcTemplate.update(sql, cwbstate.getValue());
	}

	public List<String> getWeirukuCwbs(long cwbordertypeid, long flowordertype, long nextbranchid) {
		String sql = "select cwb from express_ops_cwb_detail where  flowordertype=? and nextbranchid=? AND state=1 ";
		if (cwbordertypeid != 0) {
			sql += " and cwbordertypeid=" + cwbordertypeid;
		}
		return this.jdbcTemplate.queryForList(sql, String.class, flowordertype, nextbranchid);
	}

	public String getWeirukuCwbsToSQL(long cwbordertypeid, long flowordertype, long nextbranchid) {
		String sql = "select * from express_ops_cwb_detail where  flowordertype=" + flowordertype + " and nextbranchid=" + nextbranchid + " AND state=1 ";
		if (cwbordertypeid != 0) {
			sql += " and cwbordertypeid=" + cwbordertypeid;
		}
		return sql;
	}

	public String getWeirukuToSQL(long cwbordertypeid, long flowordertype, String nextbranchids) {
		String sql = "select de.* from express_ops_operation_time as ot left join express_ops_cwb_detail as de" + " on ot.cwb=de.cwb where  " + "ot.flowordertype=" + flowordertype
				+ " and ot.nextbranchid in(" + nextbranchids + ") AND de.state=1 ";
		if (cwbordertypeid != 0) {
			sql += " and ot.cwbordertypeid=" + cwbordertypeid;
		}
		return sql;
	}

	public List<String> getYirukuCwbs(long cwbordertypeid, long flowordertype, long currentbranchid) {
		String sql = "select cwb from express_ops_cwb_detail where flowordertype=? and currentbranchid=? AND state=1";
		if (cwbordertypeid != 0) {
			sql += " and cwbordertypeid=" + cwbordertypeid;
		}
		return this.jdbcTemplate.queryForList(sql, String.class, flowordertype, currentbranchid);
	}

	public String getYirukuCwbsToSQL(long cwbordertypeid, long flowordertype, long currentbranchid) {
		String sql = "select * from express_ops_cwb_detail where flowordertype=" + flowordertype + " " + "and currentbranchid=" + currentbranchid + " AND state=1";
		if (cwbordertypeid != 0) {
			sql += " and cwbordertypeid=" + cwbordertypeid;
		}
		return sql;
	}

	public String getYirukuToSQL(long cwbordertypeid, long flowordertype, String currentbranchids) {
		String sql = "select de.* from express_ops_operation_time as ot left join express_ops_cwb_detail as de" + " on ot.cwb=de.cwb where  ot.flowordertype=" + flowordertype + " "
				+ "and ot.branchid  in(" + currentbranchids + ") AND de.state=1";
		if (cwbordertypeid != 0) {
			sql += " and ot.cwbordertypeid=" + cwbordertypeid;
		}
		return sql;
	}

	public List<SmtOrder> querySmtOrder(String sql) {
		return this.jdbcTemplate.query(sql, new SmtOrderRowMap());
	}

	public int querySmtOrderCount(String sql) {
		return this.jdbcTemplate.queryForInt(sql);
	}

	private class SmtOrderRowMap implements RowMapper<SmtOrder> {

		@Override
		public SmtOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
			SmtOrder newOrder = new SmtOrder();
			newOrder.setCwbId(rs.getLong("opscwbid"));
			newOrder.setCwb(rs.getString("cwb"));
			if (CwbDAO.this.getUser().getShowmobileflag() == 1) {
				newOrder.setCustomerName(rs.getString("consigneename"));
			} else {
				newOrder.setCustomerName("******");
			}
			if (CwbDAO.this.getUser().getShowphoneflag() == 1) {
				newOrder.setPhone(rs.getString("consigneephone"));
			} else {
				newOrder.setPhone("******");
			}
			newOrder.setAddress(rs.getString("consigneeaddress"));
			newOrder.setReceivedFee(rs.getDouble("shouldfare"));
			newOrder.setMatchBranch(rs.getString("excelbranch"));
			newOrder.setDeliver(rs.getLong("deliverid"));

			return newOrder;
		}

		private String getPhoneNumber(ResultSet rs) throws SQLException {
			String phone = rs.getString("consigneephone");
			String mobile = rs.getString("consigneemobile");
			boolean phoneNull = phone == null;
			boolean mobileNull = mobile == null;
			if (phoneNull && mobileNull) {
				return new String();
			}
			if (phoneNull) {
				return mobile;
			}
			if (mobileNull) {
				return phone;
			}
			return phone + "/" + mobile;

		}
	}

	public List<MatchExceptionOrder> queryMatchExceptionOrder(String sql, boolean union) {
		if (union) {
			return this.jdbcTemplate.query(sql, new MatchExceptionOrderUnionRowMap());
		}
		return this.jdbcTemplate.query(sql, new MatchExceptionOrderRowMap());
	}

	public int queryMatchExceptionOrderCount(String sql) {
		return this.jdbcTemplate.queryForInt(sql);
	}

	private class MatchExceptionOrderRowMap implements RowMapper<MatchExceptionOrder> {

		private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		@Override
		public MatchExceptionOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
			MatchExceptionOrder newOrder = new MatchExceptionOrder();
			newOrder.setReportOutAreaTime(this.getFormat().format(rs.getTimestamp("credate")));
			newOrder.setReportOutAreaBranchId(rs.getLong("f.branchid"));
			newOrder.setReportOutAreaUserId(rs.getLong("f.userid"));
			newOrder.setCwb(rs.getString("d.cwb"));
			if (CwbDAO.this.getUser().getShownameflag() == 1) {
				newOrder.setCustomerName(rs.getString("d.consigneename"));
			} else {
				newOrder.setCustomerName("******");

			}
			if (CwbDAO.this.getUser().getShowphoneflag() == 1) {
				newOrder.setCustomerPhone(rs.getString("d.consigneephone"));
			} else {
				newOrder.setCustomerPhone("******");

			}
			newOrder.setCustomerAddress(rs.getString("d.consigneeaddress"));
			newOrder.setReceivedFee(rs.getDouble("d.shouldfare"));
			newOrder.setMatchBranchId(rs.getLong("d.deliverybranchid"));
			newOrder.setOutareaFlag(rs.getInt("d.outareaflag"));

			return newOrder;
		}

		private SimpleDateFormat getFormat() {
			return this.format;
		}

	}

	private class MatchExceptionOrderUnionRowMap implements RowMapper<MatchExceptionOrder> {

		private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		@Override
		public MatchExceptionOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
			MatchExceptionOrder newOrder = new MatchExceptionOrder();
			newOrder.setReportOutAreaTime(this.getFormat().format(rs.getTimestamp("credate")));
			newOrder.setReportOutAreaBranchId(rs.getLong("branchid"));
			newOrder.setReportOutAreaUserId(rs.getLong("userid"));
			newOrder.setCwb(rs.getString("cwb"));
			if (CwbDAO.this.getUser().getShownameflag() == 1) {
				newOrder.setCustomerName(rs.getString("consigneename"));
			} else {
				newOrder.setCustomerName("******");
			}
			if (CwbDAO.this.getUser().getShowphoneflag() == 1) {
				newOrder.setCustomerPhone(rs.getString("consigneephone"));
			} else {
				newOrder.setCustomerPhone("******");
			}
			newOrder.setCustomerAddress(rs.getString("consigneeaddress"));
			newOrder.setReceivedFee(rs.getDouble("shouldfare"));
			newOrder.setMatchBranchId(rs.getLong("deliverybranchid"));
			newOrder.setOutareaFlag(rs.getInt("outareaflag"));

			return newOrder;
		}

		private String getPhoneNumber(ResultSet rs) throws SQLException {
			String phone = rs.getString("consigneephone");
			String mobile = rs.getString("consigneemobile");
			boolean phoneNull = phone == null;
			boolean mobileNull = mobile == null;
			if (phoneNull && mobileNull) {
				return new String();
			}
			if (phoneNull) {
				return mobile;
			}
			if (mobileNull) {
				return phone;
			}
			return phone + "/" + mobile;

		}

		private SimpleDateFormat getFormat() {
			return this.format;
		}

	}

	public void updateOrderOutAreaStatus(String[] cwbs, Map<String, Long> branchMap) {
		// 上报超区时需要改变超区标识和将当前站点改为入库库房站点.
		String sql = "update express_ops_cwb_detail set outareaflag = 1,flowordertype = 60,nextbranchid = ? where cwb = ?";
		this.jdbcTemplate.batchUpdate(sql, this.getOutAreaParaList(cwbs, branchMap));
	}

	private List<Object[]> getOutAreaParaList(String[] cwbs, Map<String, Long> branchMap) {
		List<Object[]> paraList = new ArrayList<Object[]>();
		for (String cwb : cwbs) {
			Object[] objs = new Object[2];
			objs[0] = branchMap.get(cwb);
			objs[1] = cwb;
			paraList.add(objs);
		}
		return paraList;
	}

	public Map<String, Long> getImprotDataBranchMap(String[] cwbs) {
		Map<String, Long> branchMap = new HashMap<String, Long>();
		String strInPara = this.getInPara(cwbs);
		String sql = "select cwb , branchid from express_ops_order_flow where cwb in(" + strInPara + ") and flowordertype = 1";
		this.jdbcTemplate.query(sql, new FlowBranchRowHandler(branchMap));

		return branchMap;
	}

	private String getInPara(String[] cwbs) {
		StringBuilder inPara = new StringBuilder();
		for (String cwb : cwbs) {
			inPara.append("'");
			inPara.append(cwb);
			inPara.append("'");
			inPara.append(",");
		}
		return inPara.substring(0, inPara.length() - 1);
	}

	private class FlowBranchRowHandler implements RowCallbackHandler {

		private Map<String, Long> branchMap = null;

		public FlowBranchRowHandler(Map<String, Long> branchMap) {
			this.branchMap = branchMap;
		}

		@Override
		public void processRow(ResultSet rs) throws SQLException {
			this.getBranchMap().put(rs.getString("cwb"), rs.getLong("branchid"));
		}

		private Map<String, Long> getBranchMap() {
			return this.branchMap;
		}

	}

	public String getSqlExportBackToCustomerWeichukuOfcwbtype(long branchid, long cwbordertypeid) {
		String sql = "SELECT * FROM express_ops_cwb_detail WHERE currentbranchid =" + branchid + " and cwbstate=" + CwbStateEnum.TuiGongYingShang.getValue() + "  and flowordertype<>"
				+ FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue() + " and state=1 ";
		if (cwbordertypeid != 0) {
			sql += " and cwbordertypeid=" + cwbordertypeid;
		}

		return sql;
	}

	public String getSqlExportBackToCustomerYichukuOfcwbtype(long branchid, long cwbordertypeid) {
		String sql = "SELECT b.* FROM express_ops_operation_time a LEFT JOIN express_ops_cwb_detail b ON a.cwb=b.cwb WHERE a.branchid=" + branchid + " AND a.flowordertype="
				+ FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue() + " AND b.state=1 ";
		if (cwbordertypeid != 0) {
			sql += " and b.cwbordertypeid=" + cwbordertypeid;
		}
		return sql;
	}

	public void mehUpdateOutAreaOrderData(String cwb, long newBranchId, boolean isOutArea) {
		StringBuilder sql = new StringBuilder();
		sql.append("update express_ops_cwb_detail set deliverybranchid= ? ,currentbranchid = ? ");
		if (isOutArea) {
			sql.append(", outareaflag = 2 ");
		}
		sql.append(", flowordertype = 1 where cwb= ?");
		this.jdbcTemplate.update(sql.toString(), newBranchId, newBranchId, cwb);
	}


	public int getCwbOrderType(String cwb) {
		String sql = "select cwbordertypeid from express_ops_cwb_detail where cwb = ?";

		return this.jdbcTemplate.queryForInt(sql, cwb);
	}

	public String getCwbByCwbId(long cwbId) {
		String sql = "select cwb from express_ops_cwb_detail where opscwbid = ?";
		Object[] paras = new Object[] { cwbId };
		return this.jdbcTemplate.queryForObject(sql, paras, String.class);
	}

	public void updateDeliveridByCwb(String cwb, long deliverid) {
		String sql = "update express_ops_cwb_detail set deliverid=? where cwb=? and state=1 ";
		this.jdbcTemplate.update(sql, deliverid, cwb);
	}
	
	public void updateCwbRemarkPaytype(String cwb, String remark5) {
		String sql = "update express_ops_cwb_detail set remark5=? where cwb=? and state=1 ";
		this.jdbcTemplate.update(sql, remark5, cwb);
	}

	public Map<String, Object> getCwbIDsByBale(String baleid) {
		String sql = "select count(cwb) as cwbnum,IFNULL(sum(sendcarnum)+sum(backcarnum),0) as transcwbnum from express_ops_cwb_detail where state=1 and packagecode = " + baleid;
		return this.jdbcTemplate.queryForMap(sql);

	}

	public List<CwbOrder> getRukuByBranchidForList(long branchid, long sitetype, String orderby, long customerid, long emaildateid, long asc) {

		String sql = "SELECT * FROM express_ops_cwb_detail WHERE  nextbranchid =? and currentbranchid=0 and state=1 and flowordertype<>" + FlowOrderTypeEnum.FenZhanLingHuo.getValue();
		if (sitetype == BranchEnum.ZhanDian.getValue()) {
			sql = "SELECT * FROM express_ops_cwb_detail WHERE nextbranchid =? and currentbranchid=0 and flowordertype='" + FlowOrderTypeEnum.ChuKuSaoMiao.getValue() + "' and state=1 ";
		}
		if (sitetype == BranchEnum.ZhongZhuan.getValue()) {
			sql = "SELECT * FROM express_ops_cwb_detail WHERE  nextbranchid =? and currentbranchid=0 and state=1 and flowordertype=" + FlowOrderTypeEnum.ChuKuSaoMiao.getValue();
		}
		if (customerid > 0) {
			sql += " and customerid=" + customerid;
		}
		if (emaildateid > 0) {
			sql += " and emaildateid =" + emaildateid;
		}
		sql += " ORDER BY  " + orderby;
		if ((asc % 2) == 0) {
			sql += " ASC";
		} else {
			sql += " DESC";
		}
		sql += " limit " + Page.DETAIL_PAGE_NUMBER;
		System.out.println(sql);
		return this.jdbcTemplate.query(sql, new CwbMapper(), branchid);
	}

	public List<CwbOrder> getYiRukubyBranchidList(long branchid, long customerid, String orderby, long emaildateid, long asc) {
		String sql = "SELECT * FROM express_ops_cwb_detail WHERE currentbranchid=? and state=1 and flowordertype=" + FlowOrderTypeEnum.RuKu.getValue();
		if (customerid > 0) {
			sql += " and customerid =" + customerid;
		}
		if (emaildateid > 0) {
			sql += " and emaildateid =" + emaildateid;
		}
		sql += " ORDER BY  " + orderby;
		if ((asc % 2) == 0) {
			sql += " ASC";
		} else {
			sql += " DESC";
		}
		sql += " limit " + Page.DETAIL_PAGE_NUMBER;
		return this.jdbcTemplate.query(sql, new CwbMapper(), branchid);
	}

	public List<CwbOrder> getChukuForCwbOrderByBranchid(long currentbranchid, int cwbstate, String orderby, long nextbranchid, long asc) {
		String sql = "";
		if (nextbranchid > 0) {
			sql = "SELECT * FROM express_ops_cwb_detail WHERE currentbranchid=" + currentbranchid + " and nextbranchid=" + nextbranchid + "  and flowordertype<>" + FlowOrderTypeEnum.TiHuo.getValue()
					+ " and state=1 ";
		} else {
			sql = "SELECT * FROM express_ops_cwb_detail WHERE currentbranchid=" + currentbranchid + " and nextbranchid<>0  and flowordertype<>" + FlowOrderTypeEnum.TiHuo.getValue() + " and state=1 ";
		}
		if (cwbstate > -1) {
			sql += " and cwbstate=" + cwbstate;
		}
		sql += " ORDER BY  " + orderby;
		if ((asc % 2) == 0) {
			sql += " ASC";
		} else {
			sql += " DESC";
		}
		sql += " limit " + Page.DETAIL_PAGE_NUMBER;
		return this.jdbcTemplate.query(sql, new CwbMapper());
	}

	public List<CwbOrder> getCwbByCwbsPage(String orderby, String cwbs, long asc) {
		String sql = "SELECT * from express_ops_cwb_detail where cwb in(" + cwbs + ") and state=1 ";
		sql += " ORDER BY  " + orderby;
		if ((asc % 2) == 0) {
			sql += " ASC";
		} else {
			sql += " DESC";
		}
		sql += " limit " + Page.DETAIL_PAGE_NUMBER;
		return this.jdbcTemplate.query(sql, new CwbMapper());
	}

	/**
	 * 添加中转的原因在主表中
	 */
	public void updateZhongzhuanReason(String cwb, long reasonid, String reasonContent) {
		String sql = "update express_ops_cwb_detail set zhongzhuanreasonid=? ,zhongzhuanreason=?  where cwb=?";
		this.jdbcTemplate.update(sql, reasonid, reasonContent, cwb);
	}

	public List<CwbOrder> getMonitorLogByBranchid(String branchids, String customerids, String wheresql, long page) {
		StringBuffer sql = new StringBuffer("SELECT * FROM  `express_ops_cwb_detail` WHERE  " + wheresql + " AND state=1  "
				+ (customerids.length() > 0 ? (" and customerid in(" + customerids + ") ") : " ") + "  limit " + ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER);

		System.out.println("-- 生命周期监控查看明细:\n" + sql);
		List<CwbOrder> list = this.jdbcTemplate.query(sql.toString(), new CwbMapper());
		return list;
	}

	public long getMonitorLogByBranchid(String branchids, String customerids, String wheresql) {
		StringBuffer sql = new StringBuffer("SELECT count(1) FROM  `express_ops_cwb_detail` WHERE  " + wheresql + " AND state=1  "
				+ (customerids.length() > 0 ? (" and customerid in(" + customerids + ") ") : " "));

		System.out.println("-- 生命周期监控查看明细:\n" + sql);
		return this.jdbcTemplate.queryForLong(sql.toString());
	}

	public List<CwbOrder> getMonitorLogByType(String wheresql, String branchid, long page, String branchids) {

		StringBuffer sql = new StringBuffer("SELECT * FROM  `express_ops_cwb_detail` WHERE  " + wheresql + " and "
				+ (branchid.length() > 0 ? (" nextbranchid in(" + branchid + ")  and") : " nextbranchid IN(" + branchids + ") and ") + " nextbranchid>0 AND state=1  " + " limit "
				+ ((page - 1) * Page.ONE_PAGE_NUMBER) + " ," + Page.ONE_PAGE_NUMBER);

		List<CwbOrder> list = this.jdbcTemplate.query(sql.toString(), new CwbMapper());

		return list;
	}

	public String getMonitorLogByTypeSql(String wheresql, String branchid, String branchids) {

		StringBuffer sql = new StringBuffer("SELECT * FROM  `express_ops_cwb_detail` WHERE  ( " + wheresql + " (flowordertype in(1,2)  and "
				+ (branchid.length() > 0 ? (" nextbranchid in(" + branchid + ")  and") : " nextbranchid IN(" + branchids + ") and ") + " nextbranchid>0 ) )AND state=1  " + "");

		System.out.println(sql);

		return sql.toString();
	}

	public long getMonitorLogByType(String wheresql, String branchid, String branchids) {

		StringBuffer sql = new StringBuffer("SELECT count(1) FROM  `express_ops_cwb_detail` WHERE  " + wheresql + " and "
				+ (branchid.length() > 0 ? (" nextbranchid in(" + branchid + ")  and") : " nextbranchid IN(" + branchids + ") and ") + " nextbranchid>0 AND state=1  " + " ");

		return this.jdbcTemplate.queryForLong(sql.toString());
	}

	// 更改一票多件时运单不足的补入情况
	public void updateTranscwb(String cwb, String transcwbs) {
		String[] str1 = transcwbs.split(",");
		long sendcarnum = (long)str1.length;
		this.jdbcTemplate.update("update express_ops_cwb_detail set transcwb=? ,sendcarnum= "+sendcarnum+" where cwb=? and state = 1  ", transcwbs, cwb);
	}
	/**
	 * 根据小件员id查询其所配送的cwb信息
	 */
	
	public List<CwbOrder> findcwbinfoBydeliveryId(int deliverid){
		String sql="select * from express_ops_cwb_detail where deliverid=?";
		
		return this.jdbcTemplate.query(sql, new CwbMapper(),deliverid);
	}
}
