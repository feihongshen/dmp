package cn.explink.dao;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.CwbOrder;
import cn.explink.domain.CwbOrderCopyForDmp;
import cn.explink.domain.CwbOrderTail;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.DateDayUtil;
import cn.explink.util.Page;
import cn.explink.util.StreamingStatementCreator;
import cn.explink.util.StringUtil;

@Component
public class CwbDAO {

	@Autowired
	DeliverySuccessfulDAO deliverySuccessfulDAO;

	private Logger logger = LoggerFactory.getLogger(CwbDAO.class);

	public static String md5(String params) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(params.getBytes());
			byte[] b = md.digest();
			String result = "";
			String temp = "";

			for (int i = 0; i < 16; i++) {
				temp = Integer.toHexString(b[i] & 0xFF);
				if (temp.length() == 1)
					temp = "0" + temp;
				result += temp;
			}
			return result;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private final class CwbMapper implements RowMapper<CwbOrder> {
		@Override
		public CwbOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
			CwbOrder cwbOrder = new CwbOrder();
			cwbOrder.setOpscwbid(rs.getLong("opscwbid"));
			cwbOrder.setStartbranchid(rs.getString("startbranchid") == null ? 0 : rs.getLong("startbranchid"));
			cwbOrder.setNextbranchid(rs.getString("nextbranchid") == null ? 0 : rs.getLong("nextbranchid"));
			cwbOrder.setBacktocustomer_awb(StringUtil.nullConvertToEmptyString(rs.getString("backtocustomer_awb")));
			cwbOrder.setCwbflowflag(StringUtil.nullConvertToEmptyString(rs.getString("cwbflowflag")));
			cwbOrder.setCarrealweight(rs.getBigDecimal("carrealweight"));
			cwbOrder.setCartype(StringUtil.nullConvertToEmptyString(rs.getString("cartype")));
			cwbOrder.setCarwarehouse(StringUtil.nullConvertToEmptyString(rs.getString("carwarehouse")));
			cwbOrder.setCarsize(StringUtil.nullConvertToEmptyString(rs.getString("carsize")));
			cwbOrder.setBackcaramount(rs.getBigDecimal("backcaramount"));
			cwbOrder.setSendcarnum(rs.getString("sendcarnum") == null ? 0 : rs.getLong("sendcarnum"));
			cwbOrder.setBackcarnum(rs.getString("backcarnum") == null ? 0 : rs.getLong("backcarnum"));
			cwbOrder.setCaramount(rs.getBigDecimal("caramount"));
			cwbOrder.setBackcarname(StringUtil.nullConvertToEmptyString(rs.getString("backcarname")));
			cwbOrder.setSendcarname(StringUtil.nullConvertToEmptyString(rs.getString("sendcarname")));
			cwbOrder.setDeliverid(rs.getString("deliverid") == null ? 0 : rs.getLong("deliverid"));
			cwbOrder.setEmailfinishflag(rs.getString("emailfinishflag") == null ? 0 : rs.getInt("emailfinishflag"));
			cwbOrder.setReacherrorflag(rs.getString("reacherrorflag") == null ? 0 : rs.getInt("reacherrorflag"));
			cwbOrder.setOrderflowid(rs.getString("orderflowid") == null ? 0 : rs.getLong("orderflowid"));
			cwbOrder.setFlowordertype(rs.getLong("flowordertype"));
			cwbOrder.setCwbreachbranchid(rs.getString("cwbreachbranchid") == null ? 0 : rs.getLong("cwbreachbranchid"));
			cwbOrder.setCwbreachdeliverbranchid(rs.getString("customerid") == null ? 0 : rs.getLong("cwbreachdeliverbranchid"));
			cwbOrder.setPodfeetoheadflag(StringUtil.nullConvertToEmptyString(rs.getString("podfeetoheadflag")));
			cwbOrder.setPodfeetoheadtime(StringUtil.nullConvertToEmptyString(rs.getString("podfeetoheadtime")));
			cwbOrder.setPodfeetoheadchecktime(StringUtil.nullConvertToEmptyString(rs.getString("podfeetoheadchecktime")));
			cwbOrder.setPodfeetoheadcheckflag(StringUtil.nullConvertToEmptyString(rs.getString("podfeetoheadcheckflag")));
			cwbOrder.setLeavedreasonid(rs.getString("customerid") == null ? 0 : rs.getLong("leavedreasonid"));
			cwbOrder.setDeliversubscribeday(StringUtil.nullConvertToEmptyString(rs.getString("deliversubscribeday")));
			cwbOrder.setCustomerwarehouseid(StringUtil.nullConvertToEmptyString(rs.getString("customerwarehouseid")));
			cwbOrder.setEmaildate(StringUtil.nullConvertToEmptyString(rs.getString("emaildate")));
			cwbOrder.setShiptime(StringUtil.nullConvertToEmptyString(rs.getString("shiptime")));
			cwbOrder.setServiceareaid(rs.getString("serviceareaid") == null ? 0 : rs.getLong("serviceareaid"));
			cwbOrder.setCustomerid(rs.getString("customerid") == null ? 0 : rs.getLong("customerid"));
			cwbOrder.setShipcwb(StringUtil.nullConvertToEmptyString(rs.getString("shipcwb")));
			cwbOrder.setConsigneeno(StringUtil.nullConvertToEmptyString(rs.getString("consigneeno")));
			cwbOrder.setConsigneename(StringUtil.nullConvertToEmptyString(rs.getString("consigneename")));
			cwbOrder.setConsigneeaddress(StringUtil.nullConvertToEmptyString(rs.getString("consigneeaddress")));
			cwbOrder.setConsigneepostcode(StringUtil.nullConvertToEmptyString(rs.getString("consigneepostcode")));
			cwbOrder.setConsigneephone(StringUtil.nullConvertToEmptyString(rs.getString("consigneephone")));
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
			cwbOrder.setCwbordertypeid(StringUtil.nullConvertToEmptyString(rs.getString("cwbordertypeid")));
			cwbOrder.setConsigneemobile(StringUtil.nullConvertToEmptyString(rs.getString("consigneemobile")));
			cwbOrder.setTranscwb(StringUtil.nullConvertToEmptyString(rs.getString("transcwb")));
			cwbOrder.setDestination(StringUtil.nullConvertToEmptyString(rs.getString("destination")));
			cwbOrder.setCwbdelivertypeid(StringUtil.nullConvertToEmptyString(rs.getString("cwbdelivertypeid")));
			cwbOrder.setExceldeliver(StringUtil.nullConvertToEmptyString(rs.getString("exceldeliver")));
			cwbOrder.setExcelbranch(StringUtil.nullConvertToEmptyString(rs.getString("excelbranch")));
			cwbOrder.setExcelimportuserid(rs.getLong("excelimportuserid"));
			cwbOrder.setState(rs.getLong("state"));
			cwbOrder.setCommonname(StringUtil.nullConvertToEmptyString(rs.getString("commonname")));
			cwbOrder.setNewfollownotes(StringUtil.nullConvertToEmptyString(rs.getString("newfollownotes")));
			cwbOrder.setMarksflag(rs.getLong("marksflag"));
			cwbOrder.setMarksflagmen(StringUtil.nullConvertToEmptyString(rs.getString("marksflagmen")));
			cwbOrder.setCommonid(rs.getLong("commonid"));
			cwbOrder.setAllfollownotes(StringUtil.nullConvertToEmptyString(rs.getString("allfollownotes")));
			cwbOrder.setPrimitivemoney(rs.getBigDecimal("primitivemoney"));
			cwbOrder.setMarksflagtime(StringUtil.nullConvertToEmptyString(rs.getString("marksflagtime")));
			cwbOrder.setEdittime(StringUtil.nullConvertToEmptyString(rs.getString("edittime")));
			cwbOrder.setEditman(StringUtil.nullConvertToEmptyString(rs.getString("editman")));
			cwbOrder.setSigninman(StringUtil.nullConvertToEmptyString(rs.getString("signinman")));
			cwbOrder.setSignintime(StringUtil.nullConvertToEmptyString(rs.getString("signintime")));
			cwbOrder.setEditsignintime(StringUtil.nullConvertToEmptyString(rs.getString("editsignintime")));
			cwbOrder.setReturngoodsremark(StringUtil.nullConvertToEmptyString(rs.getString("returngoodsremark")));
			cwbOrder.setCommonnumber(StringUtil.nullConvertToEmptyString(rs.getString("commonnumber")));
			cwbOrder.setAuditstate(rs.getLong("auditstate"));
			cwbOrder.setAuditor(StringUtil.nullConvertToEmptyString(rs.getString("auditor")));
			cwbOrder.setAudittime(StringUtil.nullConvertToEmptyString(rs.getString("audittime")));
			cwbOrder.setCustomername(StringUtil.nullConvertToEmptyString(rs.getString("customername")));
			cwbOrder.setFloworderid(rs.getInt("floworderid"));
			cwbOrder.setBranchid(rs.getString("branchid") == null ? 0 : rs.getInt("branchid"));
			cwbOrder.setEmaildateid(rs.getInt("emaildateid"));
			cwbOrder.setInstoreroomtime(StringUtil.nullConvertToEmptyString(rs.getString("instoreroomtime")));

			cwbOrder.setRemark1(StringUtil.nullConvertToEmptyString(rs.getString("remark1")));
			cwbOrder.setRemark2(StringUtil.nullConvertToEmptyString(rs.getString("remark2")));
			cwbOrder.setRemark3(StringUtil.nullConvertToEmptyString(rs.getString("remark3")));
			cwbOrder.setRemark4(StringUtil.nullConvertToEmptyString(rs.getString("remark4")));
			cwbOrder.setRemark5(StringUtil.nullConvertToEmptyString(rs.getString("remark5")));
			cwbOrder.setStartbranchname(StringUtil.nullConvertToEmptyString(rs.getString("startbranchname")));
			cwbOrder.setNextbranchname(StringUtil.nullConvertToEmptyString(rs.getString("nextbranchname")));
			cwbOrder.setOutstoreroomtime(StringUtil.nullConvertToEmptyString(rs.getString("outstoreroomtime")));
			cwbOrder.setInSitetime(StringUtil.nullConvertToEmptyString(rs.getString("inSitetime")));
			cwbOrder.setPickGoodstime(StringUtil.nullConvertToEmptyString(rs.getString("pickGoodstime")));
			cwbOrder.setSendSuccesstime(StringUtil.nullConvertToEmptyString(rs.getString("sendSuccesstime")));
			cwbOrder.setGobacktime(StringUtil.nullConvertToEmptyString(rs.getString("gobacktime")));
			cwbOrder.setGoclasstime(StringUtil.nullConvertToEmptyString(rs.getString("goclasstime")));
			cwbOrder.setNowtime(StringUtil.nullConvertToEmptyString(rs.getString("nowtime")));
			cwbOrder.setLeavedreasonStr(StringUtil.nullConvertToEmptyString(rs.getString("leavedreasonStr")));
			cwbOrder.setInhouse(StringUtil.nullConvertToEmptyString(rs.getString("inhouse")));
			cwbOrder.setRealweight(rs.getBigDecimal("realweight"));
			cwbOrder.setGoodsremark(StringUtil.nullConvertToEmptyString(rs.getString("goodsremark")));
			cwbOrder.setPaytype(StringUtil.nullConvertToEmptyString(rs.getString("paytype")));
			cwbOrder.setCustomerwarehousename(StringUtil.nullConvertToEmptyString(rs.getString("customerwarehousename")));
			cwbOrder.setCarwarehousename(StringUtil.nullConvertToEmptyString(rs.getString("carwarehousename")));
			cwbOrder.setAuditEganstate(rs.getLong("auditEganstate"));
			cwbOrder.setOperatorName(StringUtil.nullConvertToEmptyString(rs.getString("operatorName")));
			cwbOrder.setLeavedreasonStr(StringUtil.nullConvertToEmptyString(rs.getString("leavedreasonStr")));
			cwbOrder.setBackreason(StringUtil.nullConvertToEmptyString(rs.getString("backreason")));
			cwbOrder.setPodremarkStr(StringUtil.nullConvertToEmptyString(rs.getString("podremarkStr")));
			cwbOrder.setLeavedreasonid(rs.getLong("leavedreasonid"));
			cwbOrder.setFirstleavedreasonid(rs.getLong("firstleavedreasonid"));
			cwbOrder.setBackreasonid(rs.getLong("backreasonid"));

			cwbOrder.setDelivername(StringUtil.nullConvertToEmptyString(rs.getString("delivername")));
			cwbOrder.setExpt_code(StringUtil.nullConvertToEmptyString(rs.getString("expt_code")));
			cwbOrder.setExpt_msg(StringUtil.nullConvertToEmptyString(rs.getString("expt_msg")));
			cwbOrder.setOrderResultType(rs.getLong("orderResultType"));
			cwbOrder.setTargetcarwarehouse(rs.getLong("targetcarwarehouse"));
			cwbOrder.setTargetcarwarehouseName(rs.getString("targetcarwarehouseName"));
			cwbOrder.setMulti_shipcwb(rs.getString("multi_shipcwb"));
			cwbOrder.setTuihuoid(rs.getLong("tuihuoid"));
			cwbOrder.setZhongzhuanid(rs.getLong("zhongzhuanid"));

			cwbOrder.setDeliverybranchid(rs.getLong("deliverybranchid"));
			cwbOrder.setCurrentbranchid(rs.getLong("currentbranchid"));

			cwbOrder.setResendtime(StringUtil.nullConvertToEmptyString(rs.getString("resendtime")));
			cwbOrder.setTuihuozhaninstoreroomtime(StringUtil.nullConvertToEmptyString(rs.getString("tuihuozhaninstoreroomtime")));
			cwbOrder.setPackagecode(StringUtil.nullConvertToEmptyString(rs.getString("packagecode")));

			cwbOrder.setPaytype_old(StringUtil.nullConvertToEmptyString(rs.getString("paytype_old")));

			cwbOrder.setCustomerbrackhouseremark(StringUtil.nullConvertToEmptyString(rs.getString("customerbrackhouseremark")));
			cwbOrder.setTuihuochuzhantime(StringUtil.nullConvertToEmptyString(rs.getString("tuihuochuzhantime")));
			cwbOrder.setTuigonghuoshangchukutime(StringUtil.nullConvertToEmptyString(rs.getString("tuigonghuoshangchukutime")));
			cwbOrder.setZhongzhuanrukutime(StringUtil.nullConvertToEmptyString(rs.getString("zhongzhuanrukutime")));
			cwbOrder.setZhongzhuanzhanchukutime(StringUtil.nullConvertToEmptyString(rs.getString("zhongzhuanzhanchukutime")));

			cwbOrder.setDeliverystate((rs.getString("deliverystate") == null ? 0 : Long.parseLong(rs.getString("deliverystate"))));
			return cwbOrder;
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

	private final class CwbGHSDL implements RowMapper<CwbOrder> {
		@Override
		public CwbOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
			CwbOrder cwbOrder = new CwbOrder();
			cwbOrder.setOpscwbid(rs.getLong("id"));
			cwbOrder.setCustomerid(rs.getLong("customerid"));
			return cwbOrder;
		}
	}

	private final class CwbIntefaceMaper implements RowMapper<CwbOrder> {
		@Override
		public CwbOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
			CwbOrder cwbOrder = new CwbOrder();
			cwbOrder.setOpscwbid(rs.getLong("opscwbid"));
			cwbOrder.setCwb(StringUtil.nullConvertToEmptyString(rs.getString("cwb")));
			cwbOrder.setEdittime(StringUtil.nullConvertToEmptyString(rs.getString("edittime")));
			cwbOrder.setStartbranchid(rs.getLong("startbranchid"));
			cwbOrder.setNextbranchid(rs.getLong("nextbranchid"));
			cwbOrder.setNewfollownotes(StringUtil.nullConvertToEmptyString(rs.getString("newfollownotes")));
			cwbOrder.setAllfollownotes(StringUtil.nullConvertToEmptyString(rs.getString("allfollownotes")));
			cwbOrder.setFlowordertype(rs.getLong("flowordertype"));
			cwbOrder.setInstoreroomtime(rs.getString("instoreroomtime"));
			cwbOrder.setOutstoreroomtime(rs.getString("outstoreroomtime"));
			cwbOrder.setGobacktime(rs.getString("gobacktime"));
			return cwbOrder;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 入库、小件员领货、反馈的状态，是否已准备推送
	 * 
	 * @param duijie_type
	 * @return
	 */
	public List<CwbOrder> getCwbsByDuijieType(int duijie_type, int type) {
		String sql = "select * from express_ops_cwb_detail where  state=1 ";
		if (duijie_type == 1) {
			sql += " and ruku_dangdang_flag =" + type;
		} else if (duijie_type == 2) {
			sql += " and chuku_dangdang_flag =" + type;
		} else if (duijie_type == 3) {
			sql += " and deliverystate_dangdang_flag =" + type;
		}

		// sql += "  limit 0,1000 ";
		return jdbcTemplate.query(sql, new CwbMapper());
	}

	public CwbOrder getCwbByCwb(String cwb) {
		List<CwbOrder> list = new ArrayList<CwbOrder>();
		CwbOrder cwbOrder = null;
		try {
			list = jdbcTemplate.query("SELECT * from express_ops_cwb_detail where cwb=? and state=1", new CwbMapper(), cwb);
			if (list != null && list.size() > 0) {
				cwbOrder = list.get(0);
			}
		} catch (EmptyResultDataAccessException e) {
			cwbOrder = null;
		}
		return cwbOrder;
	}

	public int getCwbByCwbCount(String cwb) {
		return jdbcTemplate.queryForInt("SELECT count(1) from express_ops_cwb_detail where cwb=? and state=1", cwb);
	}

	public List<CwbOrder> getCwbOrderByInterface(String cwb) {
		try {

			return jdbcTemplate.query("select * from express_ops_cwb_detail  where state =1 and cwb in(" + cwb + ") or transcwb in(" + cwb + ")", new CwbIntefaceMaper());
		} catch (Exception e) {
			return null;
		}
	}

	public List<CwbOrder> getCwbOrderByCwbs(String cwb) {
		try {

			return jdbcTemplate.query("select * from express_ops_cwb_detail  where state =1 and cwb in(" + cwb + ") ", new CwbMapper());
		} catch (Exception e) {
			return null;
		}
	}

	private String getCwbOrderByPageWhereSql(String sql, long customerid, long branchid, String beginemaildate, String endemaildate, String ordercwb, long servicearea, String emailfinishflag,
			String emaildate) {

		if (customerid > 0 || branchid > 0 || beginemaildate.length() > 0 || endemaildate.length() > 0 || ordercwb.length() > 0 || servicearea > 0 || emailfinishflag.length() > 0
				|| emaildate.length() > 0) {
			StringBuffer w = new StringBuffer();
			sql += " where ";
			if (customerid > 0) {
				w.append(" and customerid=" + customerid);
			}
			if (branchid > 0) {
				w.append(" and startbranchid=" + branchid);
			}
			if (emaildate.length() > 0) {
				w.append(" and emaildate='" + emaildate + "'");
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
			if (ordercwb.trim().length() > 0) {
				w.append(" and cwb in (");
				for (String cwb : ordercwb.split("\r\n")) {
					w.append("'" + cwb.trim());
					w.append("',");
				}
				String u = w.substring(0, w.length() - 1) + ")";
				w = new StringBuffer(u);
			}
			w.append(" and state=1");
			sql += w.substring(4, w.length());
		} else {
			sql += " where state=1";
		}
		return sql;
	}

	public List<CwbOrder> getcwbOrderByPage(long page, long customerid, long branchid, String beginemaildate, String endemaildate, String ordercwb, long servicearea, String emailfinishflag,
			String emaildate) {
		String sql = "select * from express_ops_cwb_detail";
		sql = this.getCwbOrderByPageWhereSql(sql, customerid, branchid, beginemaildate, endemaildate, ordercwb, servicearea, emailfinishflag, emaildate);
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		List<CwbOrder> cwborderList = jdbcTemplate.query(sql, new CwbMapper());
		return cwborderList;
	}

	public long getcwborderCount(long customerid, long branchid, String beginemaildate, String endemaildate, String ordercwb, long servicearea, String emailfinishflag, String emaildate) {
		String sql = "select count(1) from express_ops_cwb_detail";
		sql = this.getCwbOrderByPageWhereSql(sql, customerid, branchid, beginemaildate, endemaildate, ordercwb, servicearea, emailfinishflag, emaildate);
		return jdbcTemplate.queryForInt(sql);
	}

	private String delCwbOrderByPageWhereSql(String sql, String ordercwb) {
		if (ordercwb.length() > 0) {
			sql += " where cwb in (";
			StringBuffer w = new StringBuffer();
			if (ordercwb.trim().length() > 0) {
				for (String cwb : ordercwb.split("\r\n")) {
					w.append("'" + cwb.trim());
					w.append("',");
				}
				String u = w.substring(0, w.length() - 1) + ")";
				w = new StringBuffer(u);
			}
			sql += w.substring(0, w.length());
		}
		return sql;
	}

	public List<CwbOrder> getCwbOrderByCwb(String cwb) {
		return jdbcTemplate.query("select * from express_ops_cwb_detail where state =1 and cwb in(" + cwb + ")", new CwbMapper());
	}

	public long getCwbCountByCwb(String cwb) {
		return jdbcTemplate.queryForLong("select count(1) from express_ops_cwb_detail where state=1 and cwb in(" + cwb + ")");
	}

	public List<CwbOrder> getCwbByGroupid(long groupid) {
		String sql = "select * from express_ops_cwb_detail cd left outer join express_ops_groupdetail gd on cd.cwb=gd.cwb where gd.groupid=" + groupid;
		return jdbcTemplate.query(sql, new CwbMapper());
	}

	public void saveCwbFoeRemark(String cwbremark, long multicwbnum, String cwb) {
		String sql = "update express_ops_cwb_detail set cwbremark='" + cwbremark + "',sendcarnum=" + multicwbnum + " where state=1 and cwb='" + cwb + "'";
		jdbcTemplate.update(sql);
	}

	public void creCwbOrder(final CwbOrder co) {
		String sql = "insert into express_ops_cwb_detail( cwb,sendcarname,caramount,consigneename," + "consigneephone,consigneeaddress, " + "emaildate, customercommand, " + "deliverid,flowordertype,"
				+ "commonnumber,delivername," + " customerid,customername, " + "branchname,floworderid, " + "commonname,commonid,branchid, " + "nextbranchid," + "startbranchid, "
				+ "backtocustomer_awb, " + "cwbflowflag, " + "carrealweight, " + " cartype, " + " carwarehouse," + " carsize, " + " sendcarnum, " + " backcarnum, " + " backcarname, "
				+ " emailfinishflag," + " reacherrorflag, " + " orderflowid, " + " cwbreachbranchid," + " cwbreachdeliverbranchid, " + " podfeetoheadflag, " + " podfeetoheadtime, "
				+ " podfeetoheadchecktime, " + " podfeetoheadcheckflag, " + " leavedreasonid, " + " deliversubscribeday, " + " customerwarehouseid, " + " serviceareaid, " + " shipcwb, "
				+ " consigneeno, " + " consigneepostcode, " + " cwbremark, " + " transway, " + " cwbprovince, " + " cwbcity, " + " cwbcounty," + " receivablefee, " + " paybackfee, " + " shipperid, "
				+ " cwbordertypeid, " + " consigneemobile," + " transcwb," + " destination," + " cwbdelivertypeid, " + " exceldeliver," + " excelbranch," + " excelimportuserid," + " state,"
				+ " emaildateid," + " remark1," + " remark2," + " remark3," + " remark4," + " remark5," + "startbranchname," + " nextbranchname,carwarehousename,"
				+ "customerwarehousename,paytype_old,paytype,targetcarwarehouse,targetcarwarehouseName,nowtime,multi_shipcwb,tuihuoid,zhongzhuanid,deliverybranchid,currentbranchid,firstleavedreasonid,shouldfare,infactfare) "
				+ "values(?,?,?,?,?,?,?,?,?,?" + ",?,?,?,?,?,?,?,?,?,?" + ",?,?,?,?,?,?,?,?,?,?" + ",?,?,?,?,?,?,?,?,?,?" + ",?,?,?,?,?,?,?,?,?,?" + ",?,?,?,?,?,?,?,?,?,?" + ",?,?,?,?,?,?,?,?,?,?"
				+ ",?,?,?,?,?,?,?,?,?,?" + ",?,?,?,?,?,?)";
		jdbcTemplate.update(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setString(1, co.getCwb());
				ps.setString(2, co.getSendcarname());
				ps.setBigDecimal(3, co.getCaramount());
				ps.setString(4, co.getConsigneename());
				ps.setString(5, co.getConsigneephone());
				ps.setString(6, co.getConsigneeaddress());
				ps.setString(7, co.getEmaildate());
				ps.setString(8, co.getCustomercommand());
				ps.setLong(9, co.getDeliverid());
				ps.setLong(10, co.getFlowordertype());
				ps.setString(11, co.getCommonnumber());
				ps.setString(12, co.getDelivername());
				ps.setLong(13, co.getCustomerid());
				ps.setString(14, co.getCustomername());
				ps.setString(15, co.getBranchname());
				ps.setInt(16, co.getFloworderid());
				ps.setString(17, co.getCommonname());
				ps.setLong(18, co.getCommonid());
				ps.setLong(19, co.getBranchid());
				ps.setLong(20, co.getNextbranchid());
				ps.setLong(21, co.getStartbranchid());
				ps.setString(22, co.getBacktocustomer_awb());
				ps.setString(23, co.getCwbflowflag());
				ps.setBigDecimal(24, co.getCarrealweight());
				ps.setString(25, co.getCartype());
				ps.setString(26, co.getCarwarehouse());
				ps.setString(27, co.getCarsize());
				ps.setLong(28, co.getSendcarnum());
				ps.setLong(29, co.getBackcarnum());
				ps.setString(30, co.getBackcarname());
				ps.setInt(31, co.getEmailfinishflag());
				ps.setInt(32, co.getReacherrorflag());
				ps.setLong(33, co.getOrderflowid());
				ps.setLong(34, co.getCwbreachbranchid());
				ps.setLong(35, co.getCwbreachdeliverbranchid());
				ps.setString(36, co.getPodfeetoheadflag());
				ps.setString(37, co.getPodfeetoheadtime());
				ps.setString(38, co.getPodfeetoheadchecktime());
				ps.setString(39, co.getPodfeetoheadcheckflag());
				ps.setLong(40, co.getLeavedreasonid());
				ps.setString(41, co.getDeliversubscribeday());
				ps.setString(42, co.getCustomerwarehouseid());
				ps.setLong(43, co.getServiceareaid());
				ps.setString(44, co.getShipcwb());
				ps.setString(45, co.getConsigneeno());
				ps.setString(46, co.getConsigneepostcode());
				ps.setString(47, co.getCwbremark());
				ps.setString(48, co.getTransway());
				ps.setString(49, co.getCwbprovince());
				ps.setString(50, co.getCwbcity());
				ps.setString(51, co.getCwbcounty());
				ps.setBigDecimal(52, co.getReceivablefee());
				ps.setBigDecimal(53, co.getPaybackfee());
				ps.setLong(54, co.getShipperid());
				ps.setString(55, co.getCwbordertypeid());
				ps.setString(56, co.getConsigneemobile());
				ps.setString(57, co.getTranscwb());
				ps.setString(58, co.getDestination());
				ps.setString(59, co.getCwbdelivertypeid());
				ps.setString(60, co.getExceldeliver());
				ps.setString(61, co.getExcelbranch());
				ps.setLong(62, co.getExcelimportuserid());
				ps.setLong(63, co.getState());
				ps.setLong(64, co.getEmaildateid());
				ps.setString(65, co.getRemark1());
				ps.setString(66, co.getRemark2());
				ps.setString(67, co.getRemark3());
				ps.setString(68, co.getRemark4());
				ps.setString(69, co.getRemark5());
				ps.setString(70, co.getStartbranchname());
				ps.setString(71, co.getNextbranchname());
				ps.setString(72, co.getCarwarehousename());
				ps.setString(73, co.getCustomerwarehousename());
				ps.setString(74, co.getPaytype_old());
				ps.setString(75, co.getPaytype());
				ps.setLong(76, co.getTargetcarwarehouse());
				ps.setString(77, co.getTargetcarwarehouseName());
				ps.setString(78, co.getNowtime());
				ps.setString(79, co.getMulti_shipcwb());
				ps.setLong(80, co.getTuihuoid());
				ps.setLong(81, co.getZhongzhuanid());
				ps.setLong(82, co.getDeliverybranchid());
				ps.setLong(83, co.getCurrentbranchid());
				ps.setLong(84, co.getFirstleavedreasonid());
				ps.setBigDecimal(85, co.getShouldfare());
				ps.setBigDecimal(86, co.getInfactfare());
			}
		});

	}

	public void updateCwbOrder(final CwbOrder co) {
		String sql = "update express_ops_cwb_detail set " + "cwb=?,sendcarname=?,caramount=?,consigneename=?,consigneephone=?,"
				+ "consigneeaddress=?, emaildate=?, customercommand=?, deliverid=?,flowordertype=?," + "commonnumber=?,delivername=?, customerid=?,customername=?,branchname=?,"
				+ "floworderid=?, commonname=?,commonid=?,branchid=?,nextbranchid=?," + "startbranchid=?, backtocustomer_awb=?, cwbflowflag=?, carrealweight=?, cartype=?, "
				+ "carwarehouse=?, carsize=?,  sendcarnum=?, backcarnum=?,backcarname=?, " + " emailfinishflag=?,reacherrorflag=?,orderflowid=?,  cwbreachbranchid=?, cwbreachdeliverbranchid=?, "
				+ " podfeetoheadflag=?,  podfeetoheadtime=?,  podfeetoheadchecktime=?,  podfeetoheadcheckflag=?,  leavedreasonid=?, "
				+ " deliversubscribeday=?,customerwarehouseid=?,  serviceareaid=?, shipcwb=?, consigneeno=?, " + " consigneepostcode=?, cwbremark=?,  transway=?, cwbprovince=?, cwbcity=?, "
				+ " cwbcounty=?, receivablefee=?,  paybackfee=?,shipperid=?, cwbordertypeid=?, " + " consigneemobile=?, transcwb=?,destination=?, cwbdelivertypeid=?,  exceldeliver=?,"
				+ " excelbranch=?, excelimportuserid=?, state=?, emaildateid=?, remark1=?," + " remark2=?, remark3=?, remark4=?, remark5=?,startbranchname=?,"
				+ " nextbranchname=?,carwarehousename=?,customerwarehousename=?,targetcarwarehouse=?,targetcarwarehouseName=?,nowtime=?, "
				+ " tuihuoid=?,zhongzhuanid=?,deliverybranchid=?,currentbranchid=?,resendtime=?,auditstate=?," + " packagecode=?,weishuakareasonid=?,weishuakareason=?,losereasonid=?,"
				+ " losereason=?,paytype_old=?,paytype=?," + " fdeliverid=?,receivedfee=?,returnedfee=?,businessfee=?,deliverystate=?" + ",cash=?,pos=?,posremark=?,checkfee=?,checkremark=?,"
				+ "otherfee=?,podremarkid=?,deliverstateremark=?"
				+ ",customerbrackhouseremark=?,signintime=?,signinman=?,backreasonid=?,backreason=?,payupbranchid=?,historybranchname=?,firstleavedreasonid=?,shouldfare=?,infactfare=?  where cwb=? and state=1 ";
		try {
			jdbcTemplate.update(sql, new PreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					// TODO Auto-generated method stub
					ps.setString(1, co.getCwb());
					ps.setString(2, co.getSendcarname());
					ps.setBigDecimal(3, co.getCaramount());
					ps.setString(4, co.getConsigneename());
					ps.setString(5, co.getConsigneephone());
					ps.setString(6, co.getConsigneeaddress());
					ps.setString(7, co.getEmaildate());
					ps.setString(8, co.getCustomercommand());
					ps.setLong(9, co.getDeliverid());
					ps.setLong(10, co.getFlowordertype());
					ps.setString(11, co.getCommonnumber());
					ps.setString(12, co.getDelivername());
					ps.setLong(13, co.getCustomerid());
					ps.setString(14, co.getCustomername());
					ps.setString(15, co.getBranchname());
					ps.setInt(16, co.getFloworderid());
					ps.setString(17, co.getCommonname());
					ps.setLong(18, co.getCommonid());
					ps.setLong(19, co.getBranchid());
					ps.setLong(20, co.getNextbranchid());
					ps.setLong(21, co.getStartbranchid());
					ps.setString(22, co.getBacktocustomer_awb());
					ps.setString(23, co.getCwbflowflag());
					ps.setBigDecimal(24, co.getCarrealweight());
					ps.setString(25, co.getCartype());
					ps.setString(26, co.getCarwarehouse());
					ps.setString(27, co.getCarsize());
					ps.setLong(28, co.getSendcarnum());
					ps.setLong(29, co.getBackcarnum());
					ps.setString(30, co.getBackcarname());
					ps.setInt(31, co.getEmailfinishflag());
					ps.setInt(32, co.getReacherrorflag());
					ps.setLong(33, co.getOrderflowid());
					ps.setLong(34, co.getCwbreachbranchid());
					ps.setLong(35, co.getCwbreachdeliverbranchid());
					ps.setString(36, co.getPodfeetoheadflag());
					ps.setString(37, co.getPodfeetoheadtime());
					ps.setString(38, co.getPodfeetoheadchecktime());
					ps.setString(39, co.getPodfeetoheadcheckflag());
					ps.setLong(40, co.getLeavedreasonid());
					ps.setString(41, co.getDeliversubscribeday());
					ps.setString(42, co.getCustomerwarehouseid());
					ps.setLong(43, co.getServiceareaid());
					ps.setString(44, co.getShipcwb());
					ps.setString(45, co.getConsigneeno());
					ps.setString(46, co.getConsigneepostcode());
					ps.setString(47, co.getCwbremark());
					ps.setString(48, co.getTransway());
					ps.setString(49, co.getCwbprovince());
					ps.setString(50, co.getCwbcity());
					ps.setString(51, co.getCwbcounty());
					ps.setBigDecimal(52, co.getReceivablefee());
					ps.setBigDecimal(53, co.getPaybackfee());
					ps.setLong(54, co.getShipperid());
					ps.setString(55, co.getCwbordertypeid());
					ps.setString(56, co.getConsigneemobile());
					ps.setString(57, co.getTranscwb());
					ps.setString(58, co.getDestination());
					ps.setString(59, co.getCwbdelivertypeid());
					ps.setString(60, co.getExceldeliver());
					ps.setString(61, co.getExcelbranch());
					ps.setLong(62, co.getExcelimportuserid());
					ps.setLong(63, co.getState());
					ps.setLong(64, co.getEmaildateid());
					ps.setString(65, co.getRemark1());
					ps.setString(66, co.getRemark2());
					ps.setString(67, co.getRemark3());
					ps.setString(68, co.getRemark4());
					ps.setString(69, co.getRemark5());
					ps.setString(70, co.getStartbranchname());
					ps.setString(71, co.getNextbranchname());
					ps.setString(72, co.getCarwarehousename());
					ps.setString(73, co.getCustomerwarehousename());
					ps.setLong(74, co.getTargetcarwarehouse());
					ps.setString(75, co.getTargetcarwarehouseName());
					ps.setString(76, co.getNowtime());
					ps.setLong(77, co.getTuihuoid());
					ps.setLong(78, co.getZhongzhuanid());
					ps.setLong(79, co.getDeliverybranchid());
					ps.setLong(80, co.getCurrentbranchid());
					ps.setString(81, co.getResendtime());
					// ps.setString(82, co.getTuihuozhaninstoreroomtime());
					ps.setLong(82, co.getAuditstate());
					ps.setString(83, co.getPackagecode());
					ps.setLong(84, co.getWeishuakareasonid());
					ps.setString(85, co.getWeishuakareason());
					ps.setLong(86, co.getLosereasonid());
					ps.setString(87, co.getLosereason());
					ps.setString(88, co.getPaytype_old());
					ps.setString(89, co.getPaytype());

					ps.setLong(90, co.getFdeliverid());
					ps.setBigDecimal(91, co.getReceivedfee());
					ps.setBigDecimal(92, co.getReturnedfee());
					ps.setBigDecimal(93, co.getBusinessfee());
					ps.setLong(94, co.getDeliverystate());
					ps.setBigDecimal(95, co.getCash());
					ps.setBigDecimal(96, co.getPos());
					ps.setString(97, co.getPosremark());
					ps.setBigDecimal(98, co.getCheckfee());
					ps.setString(99, co.getCheckremark());
					ps.setBigDecimal(100, co.getOtherfee());
					ps.setLong(101, co.getPodremarkid());
					ps.setString(102, co.getDeliverstateremark());
					ps.setString(103, co.getCustomerbrackhouseremark());
					// ps.setString(104, co.getTuihuochuzhantime());
					// ps.setString(105, co.getTuigonghuoshangchukutime());
					ps.setString(104, co.getSignintime());
					ps.setString(105, co.getSigninman());
					ps.setLong(106, co.getBackreasonid());
					ps.setString(107, co.getBackreason());
					ps.setLong(108, co.getPayupbranchid());
					ps.setString(109, co.getHistorybranchname());
					ps.setLong(110, co.getFirstleavedreasonid());
					ps.setBigDecimal(111, co.getShouldfare());
					ps.setBigDecimal(112, co.getInfactfare());

					ps.setString(113, co.getCwb());

				}
			});
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	public void updateByTranscwb(int orderResultType, String signintime, String transcwb, boolean isOk) {
		jdbcTemplate.update("update express_ops_cwb_detail set orderResultType=?" + (isOk ? ",signinman=consigneename,signintime='" + signintime + "' ,editsignintime='" + signintime + "' " : "")
				+ " where state =1 and ( transcwb=? or cwb=? )", orderResultType, transcwb, transcwb);
	}

	public void updateDeliveryByTranscwb(int orderResultType, String signintime, String transcwb, boolean isOk) {
		jdbcTemplate.update("update express_ops_cwb_detail set orderResultType=?" + (isOk ? ",signinman=consigneename,signintime='" + signintime + "' ,editsignintime='" + signintime + "' " : "")
				+ " where state =1 and ( transcwb=? or cwb=? )", orderResultType, transcwb, transcwb);
	}

	/* 从mgr拷贝过来的 定时器查询 begin */

	/**
	 * 更新订单状态与签收人签收时间等
	 * 
	 * @param allfollownotes
	 * @param newfollownotes
	 * @param edittime
	 * @param transcwb
	 * @param isOk
	 *            是否已签收
	 */
	public void saveCwbByTranscwb(String allfollownotes, String newfollownotes, String edittime, String transcwb) {
		String sql = "update express_ops_cwb_detail set allfollownotes=?,newfollownotes=?,edittime=? where state =1 and transcwb=? or cwb=?";
		jdbcTemplate.update(sql, allfollownotes, newfollownotes, edittime, transcwb, transcwb);
	}

	/* 从mgr拷贝过来的 定时器查询 end */
	// -----------订单查询分页查询--------------
	private String getCwbOrderByPageWhereSqlD(String sql, long customerid, String commonnumber, int surpassdate, int marksflag) {

		if (customerid > 0 || commonnumber.length() > 0 || surpassdate != 0 || marksflag > 0) {
			StringBuffer w = new StringBuffer();
			sql += " where ";
			if (customerid > 0) {
				w.append(" and customerid=" + customerid);
			}
			if (commonnumber.length() > 0) {
				w.append(" and commonnumber= '" + commonnumber + "'");
			}
			if (surpassdate != 0) {
				String emaildate = DateDayUtil.getDateBefore("", surpassdate);
				w.append(" and emaildate <'" + emaildate + "'");
			}
			if (marksflag > 0) {
				w.append(" and marksflag=" + marksflag);
			}
			w.append(" and orderResultType <> " + DeliveryStateEnum.PeiSongChengGong.getValue());
			sql += w.substring(4, w.length()) + " and state =1 ";
		} else {
			sql += " where marksflag=0 and orderResultType <> " + DeliveryStateEnum.PeiSongChengGong.getValue() + " and state =1 ";
		}
		return sql;
	}

	public List<CwbOrder> getcwbOrderByPageD(long page, long customerid, String commonnumber, int surpassdate, int marksflag, String orderName) {
		String sql = "select * from express_ops_cwb_detail";
		sql = this.getCwbOrderByPageWhereSqlD(sql, customerid, commonnumber, surpassdate, marksflag);
		sql += " order by " + orderName + " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		List<CwbOrder> cwborderList = jdbcTemplate.query(sql, new CwbMapper());
		return cwborderList;
	}

	public List<CwbOrder> getcwbOrderByPageDExport(long customerid, String commonnumber, int surpassdate, int marksflag, String orderName) {
		String sql = "select * from express_ops_cwb_detail";
		sql = this.getCwbOrderByPageWhereSqlD(sql, customerid, commonnumber, surpassdate, marksflag);
		sql += " order by " + orderName;
		List<CwbOrder> cwborderList = jdbcTemplate.query(sql, new CwbMapper());
		return cwborderList;
	}

	public long getcwborderCountD(long customerid, String commonnumber, int surpassdate, int marksflag) {
		String sql = "select count(1) from express_ops_cwb_detail";
		sql = this.getCwbOrderByPageWhereSqlD(sql, customerid, commonnumber, surpassdate, marksflag);
		return jdbcTemplate.queryForInt(sql);
	}

	// ------------批量修改-----------
	public void batchEdit(String cwbremark, String cwb) {
		jdbcTemplate.update("update express_ops_cwb_detail set cwbremark=CONCAT(IF(cwbremark IS NULL,'',cwbremark),' " + cwbremark + "') where state =1 and cwb= '" + cwb + "'");
	}

	private String getCwbWhereSql(String sql, String ordercwb) {
		if (ordercwb.length() > 0) {
			sql += " where cwb in (";
			StringBuffer w = new StringBuffer();
			if (ordercwb.trim().length() > 0) {
				for (String cwb : ordercwb.split("\r\n")) {
					w.append("'" + cwb.trim() + "',");
				}
				String u = w.substring(0, w.length() - 1) + ")";
				w = new StringBuffer(u);
			}
			sql += w.substring(0, w.length()) + " and state =1 ";
		}
		return sql;
	}

	public List<CwbOrder> getCwbByCwbs(String cwb) {
		String sql = "select * from express_ops_cwb_detail";
		sql = this.getCwbWhereSql(sql, cwb);
		return jdbcTemplate.query(sql, new CwbMapper());
	}

	// ------------批量修改换单号-----------
	public void transcwbbatchEdit(String transcwb, String cwb) {
		jdbcTemplate.update("update express_ops_cwb_detail set transcwb=? where state=1 and cwb=?", transcwb, cwb);
	}

	// ------------退货管理-----------

	private String getCwbOrderByPageWhereSqlR(String sql, long customerid, String commonnumber, long auditstate, long auditEganstate, String beginshiptime, String endshiptime) {

		if (customerid > 0 || commonnumber.length() > 0 || auditstate > -2 || auditEganstate > -1 || beginshiptime.length() > 0 || endshiptime.length() > 0) {
			StringBuffer w = new StringBuffer();
			sql += " where ";
			if (customerid > 0) {
				w.append(" and customerid=" + customerid);
			}
			if (commonnumber.length() > 0) {
				w.append(" and commonnumber= '" + commonnumber + "'");
			}
			if (auditstate > -2) {
				w.append(" and auditstate=" + auditstate);
			}
			if (auditEganstate > -2) {
				w.append(" and auditEganstate=" + auditEganstate);
			}
			if (beginshiptime.length() > 0) {
				w.append(" and emaildate >= '" + beginshiptime + " 00:00:00'");
			}
			if (endshiptime.length() > 0) {
				w.append(" and emaildate <= '" + endshiptime + " 23:59:59'");
			}
			w.append(" and flowordertype in (" + FlowOrderTypeEnum.TuiHuoZhanYouHuoWuDanRuKu.getValue() + "," + FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue() + ","
					+ FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue() + ")");
			sql += w.substring(4, w.length()) + " and state =1 ";
		} else {
			sql += " where flowordertype in (" + FlowOrderTypeEnum.TuiHuoZhanYouHuoWuDanRuKu.getValue() + "," + FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue() + ","
					+ FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue() + ") " + " and state =1 ";
		}
		return sql;
	}

	public List<CwbOrder> getcwbOrderByPageR(long page, long customerid, String commonnumber, long auditstate, long auditEganstate, String beginshiptime, String endshiptime, String orderbyName) {
		String sql = "select * from express_ops_cwb_detail";
		sql = this.getCwbOrderByPageWhereSqlR(sql, customerid, commonnumber, auditstate, auditEganstate, beginshiptime, endshiptime);
		sql += " order by " + orderbyName + " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		List<CwbOrder> cwborderList = jdbcTemplate.query(sql, new CwbMapper());
		return cwborderList;
	}

	public List<CwbOrder> getcwbOrderByPageRexport(long customerid, String commonnumber, long auditstate, long auditEganstate, String beginshiptime, String endshiptime, String orderbyName) {
		String sql = "select * from express_ops_cwb_detail";
		sql = this.getCwbOrderByPageWhereSqlR(sql, customerid, commonnumber, auditstate, auditEganstate, beginshiptime, endshiptime);
		sql += " order by " + orderbyName;
		List<CwbOrder> cwborderList = jdbcTemplate.query(sql, new CwbMapper());
		return cwborderList;
	}

	public long getcwborderCountR(long customerid, String commonnumber, long auditstate, long auditEganstate, String beginshiptime, String endshiptime) {
		String sql = "select count(1) from express_ops_cwb_detail";
		sql = this.getCwbOrderByPageWhereSqlR(sql, customerid, commonnumber, auditstate, auditEganstate, beginshiptime, endshiptime);
		return jdbcTemplate.queryForInt(sql);
	}

	// ------------退货管理END-----------

	// 批量撤销审核
	public void rebackgoodsExameVerifyBycat(String cwbandtranscwb) {
		jdbcTemplate.update("update express_ops_cwb_detail set  auditstate =0 where state=1 and cwb = '" + cwbandtranscwb + "' or transcwb = '" + cwbandtranscwb + "'");
	}

	// 审核
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public void backgoodsExameVerifyByCwb(String cwb, int auditstate, String realname) {
		jdbcTemplate.update("update express_ops_cwb_detail set  auditstate ='" + auditstate + "',audittime='" + df.format(new Date()) + "',auditor='" + realname + "'  where state=1 and cwb= '" + cwb
				+ "'");
	}

	// 退货再投审核
	public void backgoodsExameVerifyEganByCwb(String cwb, int auditstate) {
		jdbcTemplate.update("update express_ops_cwb_detail set  auditEganstate ='" + auditstate + "' where state=1 and cwb= '" + cwb + "'");
	}

	// 批量审核
	public void backgoodsExameVerifyByCwbOrTranscwb(String cwbandtranscwb) {
		jdbcTemplate.update("update express_ops_cwb_detail set  auditstate =1 where state=1 and cwb = '" + cwbandtranscwb + "' or transcwb = '" + cwbandtranscwb + "'");
	}

	// 保存退货备注
	public void saveReturngoodsremark(String returngoodsremark, String cwb) {
		jdbcTemplate.update("update express_ops_cwb_detail set  returngoodsremark =CONCAT(IF(returngoodsremark IS NULL,'',returngoodsremark),' \n" + returngoodsremark + "') where state=1 and cwb = '"
				+ cwb + "'");
	}

	// ------------退货批量审核-----------
	private String getCwbOrderByPageWhereSqlRB(String sql, String cwbandtranscwb) {

		if (cwbandtranscwb.trim().length() > 0) {
			sql += " where orderResultType in (" + DeliveryStateEnum.ShangMenTuiChengGong.getValue() + "," + DeliveryStateEnum.ShangMenHuanChengGong.getValue() + ","
					+ DeliveryStateEnum.QuanBuTuiHuo.getValue() + "," + DeliveryStateEnum.BuFenTuiHuo.getValue() + ") ";
			StringBuffer w = new StringBuffer();
			w.append(" and  (cwb in (");
			for (String cwb : cwbandtranscwb.split("\r\n")) {
				w.append("'" + cwb.trim());
				w.append("',");
			}
			String u = w.substring(0, w.length() - 1) + ")";
			w = new StringBuffer(u);
			w.append(" or transcwb in (");
			for (String transcwb : cwbandtranscwb.split("\r\n")) {
				w.append("'" + transcwb.trim());
				w.append("',");
			}
			String h = w.substring(0, w.length() - 1) + "))";
			w = new StringBuffer(h);
			sql += w.toString() + " and state =1 ";
		} else {
			sql += " where orderResultType in (" + DeliveryStateEnum.ShangMenTuiChengGong.getValue() + "," + DeliveryStateEnum.ShangMenHuanChengGong.getValue() + ","
					+ DeliveryStateEnum.QuanBuTuiHuo.getValue() + "," + DeliveryStateEnum.BuFenTuiHuo.getValue() + ") " + " and state =1 ";
		}
		return sql;
	}

	public List<CwbOrder> getcwbOrderByPageRB(long page, String cwbandtranscwb, String orderName) {
		String sql = "select * from express_ops_cwb_detail";
		sql = this.getCwbOrderByPageWhereSqlRB(sql, cwbandtranscwb);
		sql += " order by " + orderName + " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		List<CwbOrder> cwborderList = jdbcTemplate.query(sql, new CwbMapper());
		return cwborderList;
	}

	public long getcwborderCountRB(String cwbandtranscwb) {
		String sql = "select count(1) from express_ops_cwb_detail";
		sql = this.getCwbOrderByPageWhereSqlRB(sql, cwbandtranscwb);
		return jdbcTemplate.queryForInt(sql);
	}

	// ------------退货批量审核END-----------

	private String getImportSql(String sql, String signinman, String signintime, int orderResultType) {
		if (orderResultType > 0 || signinman.length() > 0 || signintime.length() > 0) {
			StringBuffer w = new StringBuffer();

			if (orderResultType > 0) {
				w.append(",orderResultType=" + orderResultType);
			}
			if (signinman.length() > 0) {
				w.append(",signinman='" + signinman + "'");
			}
			if (signintime.length() > 0) {
				w.append(",editsignintime='" + signintime + "'");
			}
			sql += w;
		}
		return sql;
	}

	// 预警导入修改订单
	public void saveImportOrder(String cwb, String signinman, String signintime, String cwbremark, int orderResultType) {
		String sql = "update express_ops_cwb_detail set cwbremark=CONCAT(IF(cwbremark IS NULL,'',cwbremark),' " + (cwbremark == null ? "" : cwbremark) + "')";
		String sql1 = getImportSql(sql, signinman, signintime, orderResultType);
		sql1 = sql1 + "  where state=1 and cwb='" + cwb + "'";
		jdbcTemplate.update(sql1);
	}

	public void updateOrderFlowType(int flowordertype, long deliverid, String delivername, int branchid, String branchname, String transcwb, CwbOrderCopyForDmp order) {
		String sql = "update express_ops_cwb_detail set flowordertype=?,deliverid=?,delivername=?,branchid=?,branchname=?,startbranchid=?,nextbranchid=? where state=1 and  cwb=? ";
		jdbcTemplate.update(sql, flowordertype, deliverid, delivername, branchid, branchname, order.getStartbranchid(), order.getNextbranchid(), transcwb, transcwb);
	}

	// jms 根据反馈 更新订单表
	public void updateOrderByFankui(String cwb, int deliveryid, String fdelivername, BigDecimal receivedfee, BigDecimal returnedfee, BigDecimal businessfee, long deliverystate, BigDecimal cash,
			BigDecimal pos, String posremark, BigDecimal checkfee, String checkremark, long receivedfeeuser, long statisticstate, BigDecimal otherfee, long podremarkid, String deliverstateremark,
			long gcaid, long gobackid, long branchid, String payupbranchname, String podremarkStr, String receivedfeeuserName) {
		String sql = "update express_ops_cwb_detail set " + "fdeliverid=?," + "fdelivername=?," + "receivedfee=?," + "returnedfee=?," + "businessfee=?," + "deliverystate=?," + "cash=?," + "pos=?,"
				+ "posremark=?," + "checkfee=?," + "checkremark=?," + "receivedfeeuser=?," + "statisticstate=?," + "otherfee=?," + "podremarkid=?," + "deliverstateremark=?," + "gcaid=?,"
				+ "gobackid=?," + "payupbranchid=?," + "payupbranchname=?," + "podremarkStr=?," + "receivedfeeuserName=?" + " where state=1 and cwb=? ";
		jdbcTemplate.update(sql, deliveryid, fdelivername, receivedfee, returnedfee, businessfee, deliverystate, cash, pos, posremark, checkfee, checkremark, receivedfeeuser, statisticstate,
				otherfee, podremarkid, deliverstateremark, gcaid, gobackid, branchid, payupbranchname, podremarkStr, receivedfeeuserName, cwb);

	}

	// Jms数据失效
	public void updateLoseCwbOrder(int emaildateid) {
		String sql = "update express_ops_cwb_detail set state=0 where emaildateid=?";
		jdbcTemplate.update(sql, emaildateid);
	}

	public List<String> getCwbListByEmaildateid(int emaildateid) {
		String sql = "select cwb from  express_ops_cwb_detail  where emaildateid=?";
		return jdbcTemplate.queryForList(sql, String.class, emaildateid);
	}

	// Jms修改站点
	public void updateBranchAndDeliverid(int branchid, long deliverid, String excelbranch, String exceldeliver, String cwb) {
		String sql = "update express_ops_cwb_detail set branchid=?,nextbranchid=?, nextbranchname=?,deliverid=?,excelbranch=?,exceldeliver=? where cwb=?";
		jdbcTemplate.update(sql, branchid, branchid, excelbranch, deliverid, excelbranch, exceldeliver, cwb);
	}

	// Jms 反馈时修改小件员id和名称
	public void updateDeliver(long deliverid, String exceldeliver, String cwb) {
		String sql = "update express_ops_cwb_detail set  deliverid=?,exceldeliver=?,fdeliverid=?,fdelivername=? where cwb=?";
		jdbcTemplate.update(sql, deliverid, exceldeliver, deliverid, exceldeliver, cwb);
	}

	// Jms修改保存退货原因
	public void updatebackreason(String backreason, long backreasonid, String expt_code, String expt_msg, String cwb) {
		String sql = "update express_ops_cwb_detail set backreason=?,backreasonid=?,expt_code=?,expt_msg=? where cwb=?";
		jdbcTemplate.update(sql, backreason, backreasonid, expt_code, expt_msg, cwb);
	}

	// Jms修改保存滞留原因
	public void updateleavereason(String leavereason, long leaveid, String expt_code, String expt_msg, String cwb) {
		String sql = "update express_ops_cwb_detail set leavedreasonStr=? ,leavedreasonid=?,expt_code=?,expt_msg=?  where cwb=?";
		jdbcTemplate.update(sql, leavereason, leaveid, expt_code, expt_msg, cwb);
	}

	// jms数据导入中的批量修改
	public String dataBatchEditWhereSql(long customerwarehouseid, long serviceareaid, String editemaildate) {
		String sql = "";
		if (customerwarehouseid != 0 || serviceareaid != 0 || editemaildate.length() != 0) {
			StringBuffer str = new StringBuffer();
			if (customerwarehouseid != 0) {
				str.append(" customerwarehouseid = '" + customerwarehouseid + "',");
			}
			if (serviceareaid != 0) {
				str.append(" serviceareaid = '" + serviceareaid + "',");
			}
			if (editemaildate.length() != 0) {
				str.append(" emaildate = '" + editemaildate + "',");
			}
			sql = str.substring(0, str.length() - 1);
		}
		return sql;
	}

	public String dataBatchEditWhereSql1(String editemaildate) {
		String sql = "";
		if (editemaildate.length() != 0) {
			sql += " set emaildate = '" + editemaildate + "'";
		}
		return sql;
	}

	public void dataBatchEditForDetail(long customerwarehouseid, long serviceareaid, String editemaildate, long emaildateid) {
		String sql = this.dataBatchEditWhereSql(customerwarehouseid, serviceareaid, editemaildate);
		jdbcTemplate.update("update express_ops_cwb_detail set " + sql + " where state =1 and emaildateid='" + emaildateid + "'");
	}

	public void dataBatchEditForOrderFlow(String editemaildate, long emaildateid) {
		if (editemaildate.length() > 0) {
			String sql = this.dataBatchEditWhereSql1(editemaildate);
			jdbcTemplate.update("update express_ops_order_flow " + sql + " where state =1 and emaildateid='" + emaildateid + "'");
		}
	}

	// jms数据导入中的数据交换
	public void giveReserve(String changefield, String changefield1, long emaildateid) {
		String sql = "update express_ops_cwb_detail set  reserve=" + changefield + ",reserve1=" + changefield1 + " where state =1 and emaildateid='" + emaildateid + "'";
		jdbcTemplate.update(sql);
	}

	public int changeField(String changefield, String changefield1, long emaildateid) {
		String sql = "update express_ops_cwb_detail set " + changefield + "= reserve1," + changefield1 + "= reserve where state =1 and emaildateid='" + emaildateid + "'";
		return jdbcTemplate.update(sql);
	}

	// 换列如果含有Receivable 代收金额
	public void UpdateFlowOrderIfExistAmountLine(String changefield, String changefield1, long emaildateid) {
		String sql = "update express_ops_order_flow set caramount=? where emaildateid=? ";
		if ("receivablefee".equalsIgnoreCase(changefield)) {
			jdbcTemplate.update(sql, changefield, emaildateid);
		} else if ("receivablefee".equalsIgnoreCase(changefield1)) {
			jdbcTemplate.update(sql, changefield1, emaildateid);
		}

	}

	// jms更新反馈时 待发送给当当状态
	public void updateOrderjiontFankuiType(String cwb, long date, String username) {
		String sql = "update express_ops_cwb_detail set deliverystate_dangdang_flag=?,operatorName=?  where deliverystate_dangdang_flag<1 and cwb in(" + cwb + ") ";
		jdbcTemplate.update(sql, date, username);
	}

	// jms更新付款方式
	public void updatePaytype(String cwb, String paytype) {
		String sql = "update express_ops_cwb_detail set paytype=? where cwb=?";
		jdbcTemplate.update(sql, paytype, cwb);
	}

	// jms 上交款时 更新最新订单上交款状态
	public void updatePayUptype(String cwb) {
		String sql = "update express_ops_cwb_detail set ispayUp=1 where cwb=?";
		jdbcTemplate.update(sql, cwb);
	}

	// Jms修改配送结果
	public void updateOrderresultType(int flowordertype, String cwb) {
		String sql = "update express_ops_cwb_detail set orderResultType=? where state=1  and cwb=? ";
		jdbcTemplate.update(sql, flowordertype, cwb);
	}

	// jms更新最新时间
	public void UpdateGoclassId(String cwb, long gobackid) {
		String sql = "update express_ops_cwb_detail set gcaid=? where cwb in(" + cwb + ")";
		jdbcTemplate.update(sql, gobackid);
	}

	/**
	 * 根据不同环节的状态查询订单的信息
	 * 
	 * @param emailStartTime
	 * @param eamilEndTime
	 * @param flowordertype
	 * @param customerid
	 * @param isnow
	 * @param exportType
	 * @return
	 */
	public List<CwbOrder> searchCwbDetailByFlowOrdertype(long type, String emailStartTime, String eamilEndTime, long customerid, long page) {
		String sql = " SELECT * " + " FROM express_ops_cwb_detail WHERE state=1 AND customername<>'' ";
		if (customerid != -1) {
			sql += " AND customerid=" + customerid;
		}
		if (emailStartTime != null && !"".equals(emailStartTime) && eamilEndTime != null && !"".equals(eamilEndTime)) {
			sql += " AND emaildate between '" + emailStartTime + "' and '" + eamilEndTime + "' ";
		}
		// if (type > 1) {
		// sql += " AND flowordertype=" + type;
		// } else if (type == -1) {
		// sql += " AND flowordertype not in ("
		// + FlowOrderTypeEnum.PeiSongChengGong.getValue() + ","
		// + FlowOrderTypeEnum.JuShou.getValue() + ")";
		// }
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		try {
			return jdbcTemplate.query(sql, new CwbMapper());
		} catch (DataAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<CwbOrder> searchCwbDetailByFlowOrdertypeBack(String type, String emailStartTime, String eamilEndTime, long customerid, long page, String startaudittime, String endaudittime) {
		String sql = " SELECT * " + " FROM express_ops_cwb_detail WHERE state=1 AND customername<>'' ";
		if (customerid != -1) {
			sql += " AND customerid=" + customerid;
		}
		if (emailStartTime != null && !"".equals(emailStartTime)) {
			sql += " and emaildate >= '" + emailStartTime + "'";
		}
		if (eamilEndTime != null && !"".equals(eamilEndTime)) {
			sql += " and emaildate <= '" + eamilEndTime + "' ";
		}
		if ("tuihuo".equals(type)) {
			if (startaudittime != null && !"".equals(startaudittime)) {
				sql += " and audittime >= '" + startaudittime + "'";
			}
			if (endaudittime != null && !"".equals(endaudittime)) {
				sql += " and audittime <= '" + endaudittime + "' ";
			}
			sql += " AND orderResultType IN (" + DeliveryStateEnum.QuanBuTuiHuo.getValue() + "," + DeliveryStateEnum.ShangMenTuiChengGong.getValue() + ") AND ("
					+ FlowOrderTypeEnum.TuiHuoZhanYouHuoWuDanRuKu.getValue() + "," + FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue() + "," + FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue() + ") ";
		} else if ("zongliang".equals(type)) {
			sql += " ";
		}
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		try {
			return jdbcTemplate.query(sql, new CwbMapper());
		} catch (DataAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	public long searchCountFlowOrdertype(long type, String emailStartTime, String eamilEndTime, long customerid, long page) {
		long count = 0;
		String sql = " SELECT count(1) " + " FROM express_ops_cwb_detail WHERE state=1 AND customername<>'' ";
		if (customerid != -1) {
			sql += " AND customerid=" + customerid;
		}
		if (emailStartTime != null && !"".equals(emailStartTime) && eamilEndTime != null && !"".equals(eamilEndTime)) {
			sql += " and emaildate between '" + emailStartTime + "' and '" + eamilEndTime + "' ";
		}
		// if (type > 1) {
		// sql += " AND flowordertype=" + type;
		// } else if (type == -1) {
		// sql += " AND flowordertype not in ("
		// + FlowOrderTypeEnum.PeiSongChengGong.getValue() + ","
		// + FlowOrderTypeEnum.JuShou.getValue() + ")";
		// }
		try {
			count = jdbcTemplate.queryForInt(sql);
		} catch (DataAccessException e) {
			e.printStackTrace();

		}
		return count;
	}

	public long searchCountFlowOrdertypeback(String type, String emailStartTime, String eamilEndTime, long customerid, long page, String startaudittime, String endaudittime) {
		long count = 0;
		String sql = " SELECT count(1) " + " FROM express_ops_cwb_detail WHERE state=1 AND customername<>'' ";
		if (customerid != -1) {
			sql += " AND customerid=" + customerid;
		}
		if (emailStartTime != null && !"".equals(emailStartTime)) {
			sql += " and emaildate >= '" + emailStartTime + "'";
		}
		if (eamilEndTime != null && !"".equals(eamilEndTime)) {
			sql += " and emaildate <= '" + eamilEndTime + "' ";
		}
		if ("tuihuo".equals(type)) {
			if (startaudittime != null && !"".equals(startaudittime)) {
				sql += " and audittime >= '" + startaudittime + "'";
			}
			if (endaudittime != null && !"".equals(endaudittime)) {
				sql += " and audittime <= '" + endaudittime + "' ";
			}
			sql += " AND orderResultType IN (" + DeliveryStateEnum.QuanBuTuiHuo.getValue() + "," + DeliveryStateEnum.ShangMenTuiChengGong.getValue() + ") AND flowordertype in("
					+ FlowOrderTypeEnum.TuiHuoZhanYouHuoWuDanRuKu.getValue() + "," + FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue() + "," + FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue() + ") ";
		} else if ("zongliang".equals(type)) {
			sql += " ";
		}
		try {
			count = jdbcTemplate.queryForInt(sql);
		} catch (DataAccessException e) {
			e.printStackTrace();

		}
		return count;
	}

	public List<CwbOrder> searchCwbDetailByFlowOrdertype_excel(long type, String emailStartTime, String eamilEndTime, long customerid) {
		String sql = " SELECT * " + " FROM express_ops_cwb_detail WHERE state=1 AND customername<>'' ";
		if (customerid != -1) {
			sql += " AND customerid=" + customerid;
		}
		if (emailStartTime != null && !"".equals(emailStartTime) && eamilEndTime != null && !"".equals(eamilEndTime)) {
			sql += " AND emaildate between '" + emailStartTime + "' and '" + eamilEndTime + "' ";
		}
		// if (type > 1) {
		// sql += " AND flowordertype=" + type;
		// } else if (type == -1) {
		// sql += " AND flowordertype not in ("
		// + FlowOrderTypeEnum.PeiSongChengGong.getValue() + ","
		// + FlowOrderTypeEnum.JuShou.getValue() + ")";
		// }
		try {
			return jdbcTemplate.query(sql, new CwbMapper());
		} catch (DataAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	// 退货款结算导出
	public List<CwbOrder> searchCwbDetailByFlowOrdertypeback_excel(String type, String emailStartTime, String eamilEndTime, long customerid, String startaudittime, String endaudittime) {
		String sql = " SELECT * " + " FROM express_ops_cwb_detail WHERE state=1 AND customername<>'' ";
		if (customerid != -1) {
			sql += " AND customerid=" + customerid;
		}
		if (emailStartTime != null && !"".equals(emailStartTime)) {
			sql += " and emaildate >= '" + emailStartTime + "'";
		}
		if (eamilEndTime != null && !"".equals(eamilEndTime)) {
			sql += " and emaildate <= '" + eamilEndTime + "' ";
		}
		if ("tuihuo".equals(type)) {
			if (startaudittime != null && !"".equals(startaudittime)) {
				sql += " and audittime >= '" + startaudittime + "'";
			}
			if (endaudittime != null && !"".equals(endaudittime)) {
				sql += " and audittime <= '" + endaudittime + "' ";
			}
			sql += " AND orderResultType IN (" + DeliveryStateEnum.QuanBuTuiHuo.getValue() + "," + DeliveryStateEnum.ShangMenTuiChengGong.getValue() + ") AND flowordertype in("
					+ FlowOrderTypeEnum.TuiHuoZhanYouHuoWuDanRuKu.getValue() + "," + FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue() + "," + FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue() + ") ";
		} else if ("zongliang".equals(type)) {
			sql += " ";
		}
		try {
			return jdbcTemplate.query(sql, new CwbMapper());
		} catch (DataAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	public long getcustomeridByCwb(String cwb) {
		String sql = "select  customerid from express_ops_cwb_detail where state=1 and cwb = '" + cwb + "'";
		return jdbcTemplate.queryForLong(sql);
	}

	private String getCwbByWhereSql(String sql, long carwarehouse, long flowordertype, long startbranchid, long currentbranchid) {
		if (startbranchid > 0 || currentbranchid > 0 || carwarehouse > 0 || flowordertype > 0) {
			StringBuffer str = new StringBuffer();
			sql += " where ";
			if (startbranchid > 0) {
				str.append(" and startbranchid =" + startbranchid);
			}
			if (currentbranchid > 0) {
				str.append(" and currentbranchid =" + currentbranchid);
			}
			if (flowordertype > 0 && flowordertype < 4) {
				str.append(" and flowordertype <" + flowordertype);
			}
			if (flowordertype == 4) {
				str.append(" and flowordertype =" + flowordertype);
			}
			if (flowordertype == 6) {
				str.append(" and flowordertype =" + flowordertype);
			}
			if (carwarehouse > 0) {
				str.append(" and carwarehouse=" + carwarehouse);
			}
			str.append(" and state =1 ");
			sql += str.substring(4, str.length());
		}
		return sql;
	}

	public List<CwbOrder> getCwbByTypeAndCarwarehouse(long page, long carwarehouse, long flowordertype, long startbranchid, long currentbranchid) {
		String sql = "select * from express_ops_cwb_detail ";
		sql = this.getCwbByWhereSql(sql, carwarehouse, flowordertype, startbranchid, currentbranchid);

		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new CwbMapper());
	}

	public long getCwbByTypeAndCarwarehouseCount(long carwarehouse, long flowordertype, long startbranchid, long currentbranchid) {
		String sql = "select count(1) from express_ops_cwb_detail";
		sql = this.getCwbByWhereSql(sql, carwarehouse, flowordertype, startbranchid, currentbranchid);
		return jdbcTemplate.queryForInt(sql);
	}

	public String getCwbByTypeAndCarwarehouseSQL(long carwarehouse, long flowordertype, long startbranchid, long currentbranchid) {
		String sql = "select * from express_ops_cwb_detail ";
		sql = this.getCwbByWhereSql(sql, carwarehouse, flowordertype, startbranchid, currentbranchid);
		return sql;
	}

	// /============oms dmp移植业务过来的查询方法=======

	public List<CwbOrder> getCwborderList(long page, long datetype, String begindate, String enddate, long customerid, String commonnumber, String orderName, long customerwarehouseid,
			long cwbordertypeid, long dispatchbranchid, long kufangid, long paywayid, long dispatchdeliveryid, String consigneename, String consigneemobile, long beginWeight, long endWeight,
			long beginsendcarnum, long endsendcarnum, String carsize, long flowordertype, String[] operationOrderResultTypes, String packagecode) {

		String sql = "select * from express_ops_cwb_detail";
		sql = this.getCwbOrderByPageWhereSqlHmjQ(sql, datetype, begindate, enddate, customerid, commonnumber, customerwarehouseid, cwbordertypeid, dispatchbranchid, kufangid, paywayid,
				dispatchdeliveryid, consigneename, consigneemobile, beginWeight, endWeight, beginsendcarnum, endsendcarnum, carsize, flowordertype, operationOrderResultTypes, packagecode);
		sql += " order by " + orderName + " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new CwbMapper());
	}

	public String getSQLExport(long datetype, String begindate, String enddate, long customerid, String commonnumber, long customerwarehouseid, long startbranchid, long nextbranchid,
			long cwbordertypeid, String orderflowcwbs, long currentBranchid, long dispatchbranchid, long kufangid, long paywayid, long dispatchdeliveryid, String consigneename,
			String consigneemobile, long beginWatht, long endWatht, long beginsendcarnum, long endsendcarnum, String carsize, long flowordertype, String[] deliverystates, String packagecode, long page) {
		try {
			String sql1 = "select count(1) from express_ops_cwb_detail";
			sql1 = this.getCwbOrderByPageWhereSqlHmjQ(sql1, datetype, begindate, enddate, customerid, commonnumber, customerwarehouseid, cwbordertypeid, dispatchbranchid, kufangid, paywayid,
					dispatchdeliveryid, consigneename, consigneemobile, beginWatht, endWatht, beginsendcarnum, endsendcarnum, carsize, flowordertype, deliverystates, packagecode);

			logger.info("大数据导出数据：数据量：{}", jdbcTemplate.queryForLong(sql1));
		} catch (Exception e) {
			logger.error("大数据导出数据：获取数据量异常");
		}
		String sql = "select * from express_ops_cwb_detail";
		sql = this.getCwbOrderByPageWhereSqlHmjQ(sql, datetype, begindate, enddate, customerid, commonnumber, customerwarehouseid, cwbordertypeid, dispatchbranchid, kufangid, paywayid,
				dispatchdeliveryid, consigneename, consigneemobile, beginWatht, endWatht, beginsendcarnum, endsendcarnum, carsize, flowordertype, deliverystates, packagecode);

		sql += " limit " + page + " ," + Page.EXCEL_PAGE_NUMBER;
		return sql;
	}

	public long getcwborderCount(long datetype, String begindate, String enddate, long customerid, String commonnumber, String orderName, long customerwarehouseid, long cwbordertypeid,
			long dispatchbranchid, long kufangid, long paywayid, long dispatchdeliveryid, String consigneename, String consigneemobile, long beginWeight, long endWeight, long beginsendcarnum,
			long endsendcarnum, String carsize, long flowordertype, String[] operationOrderResultTypes, String packagecode) {

		String sql = "select count(1) from express_ops_cwb_detail";
		sql = this.getCwbOrderByPageWhereSqlHmjQ(sql, datetype, begindate, enddate, customerid, commonnumber, customerwarehouseid, cwbordertypeid, dispatchbranchid, kufangid, paywayid,
				dispatchdeliveryid, consigneename, consigneemobile, beginWeight, endWeight, beginsendcarnum, endsendcarnum, carsize, flowordertype, operationOrderResultTypes, packagecode);
		return jdbcTemplate.queryForLong(sql);
	}

	public CwbOrder getcwborderSum(long datetype, String begindate, String enddate, long customerid, String commonnumber, String orderName, long customerwarehouseid, long cwbordertypeid,
			long dispatchbranchid, long kufangid, long paywayid, long dispatchdeliveryid, String consigneename, String consigneemobile, long beginWeight, long endWeight, long beginsendcarnum,
			long endsendcarnum, String carsize, long flowordertype, String[] operationOrderResultTypes, String packagecode) {

		String sql = "select sum(receivablefee) as receivablefee,sum(paybackfee) as paybackfee from express_ops_cwb_detail ";
		sql = this.getCwbOrderByPageWhereSqlHmjQ(sql, datetype, begindate, enddate, customerid, commonnumber, customerwarehouseid, cwbordertypeid, dispatchbranchid, kufangid, paywayid,
				dispatchdeliveryid, consigneename, consigneemobile, beginWeight, endWeight, beginsendcarnum, endsendcarnum, carsize, flowordertype, operationOrderResultTypes, packagecode);
		try {
			return jdbcTemplate.queryForObject(sql, new CwbMOneyMapper());
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			return new CwbOrder();
		}
	}

	private String getCwbOrderByPageWhereSqlHmjQ(String sql, long datetype, String begindate, String enddate, long customerid, String commonnumber, long customerwarehouseid, long cwbordertypeid,
			long dispatchbranchid, long kufangid, long paywayid, long dispatchdeliveryid, String consigneename, String consigneemobile, long beginWeight, long endWeight, long beginsendcarnum,
			long endsendcarnum, String carsize, long flowordertype, String[] deliverystates, String packagecode) {

		if (datetype > 0 || begindate.length() > 0 || enddate.length() > 0 || customerid > 0 || commonnumber.length() > 0 || customerwarehouseid > 0 || cwbordertypeid > 0 || dispatchbranchid > 0
				|| kufangid > 0 || paywayid > 0 || dispatchdeliveryid > 0 || consigneename.length() > 0 || consigneemobile.length() > 0 || beginWeight > -1 || endWeight > -1 || beginsendcarnum > -1
				|| endsendcarnum > -1 || carsize.length() > 0 || flowordertype > 0 || (deliverystates != null && deliverystates.length > 0) || packagecode.length() > 0) {

			StringBuffer w = new StringBuffer();
			sql += " where ";
			if (datetype == 1) {
				if (begindate.length() > 0) {
					w.append(" and emaildate >='" + begindate + "'");
				}
				if (enddate.length() > 0) {
					w.append(" and emaildate <= '" + enddate + "'");
				}
			} else if (datetype == 2) {
				if (begindate.length() > 0) {
					w.append(" and instoreroomtime >='" + begindate + "'");
				}
				if (enddate.length() > 0) {
					w.append(" and instoreroomtime <= '" + enddate + "'");
				}
			} else if (datetype == 3) {
				if (begindate.length() > 0) {
					w.append(" and outstoreroomtime >='" + begindate + "'");
				}
				if (enddate.length() > 0) {
					w.append(" and outstoreroomtime <= '" + enddate + "'");
				}
			} else if (datetype == 4) {
				if (begindate.length() > 0) {
					w.append(" and inSitetime >='" + begindate + "'");
				}
				if (enddate.length() > 0) {
					w.append(" and inSitetime <= '" + enddate + "'");
				}
			} else if (datetype == 5) {
				if (begindate.length() > 0) {
					w.append(" and pickGoodstime >='" + begindate + "'");
				}
				if (enddate.length() > 0) {
					w.append(" and pickGoodstime <= '" + enddate + "'");
				}
			} else if (datetype == 7) {
				if (begindate.length() > 0) {
					w.append(" and gobacktime >='" + begindate + "'");
				}
				if (enddate.length() > 0) {
					w.append(" and gobacktime <= '" + enddate + "'");
				}
			} else if (datetype == 8) {
				if (begindate.length() > 0) {
					w.append(" and goclasstime >='" + begindate + "'");
				}
				if (enddate.length() > 0) {
					w.append(" and goclasstime <= '" + enddate + "'");
				}
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

			if (dispatchbranchid > 0) {
				w.append(" and deliverybranchid=" + dispatchbranchid);
			}
			if (kufangid > 0) {
				w.append(" and carwarehouse= " + kufangid);
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
				w.append(" and consigneename like '%" + consigneename + "%'");
			}
			if (consigneemobile.length() > 0) {
				w.append(" and (consigneemobile like '%" + consigneemobile + "%' or consigneephone like '%" + consigneemobile + "%' )");
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
			if (deliverystates != null && deliverystates.length > 0) {
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

	// jms更新操作时间
	public void updateFlowTime(String cwb, String flowTime, String flowName) {
		String sql = "update express_ops_cwb_detail set " + flowName + "=?,nowtime=?  where cwb=? and state=1 ";
		jdbcTemplate.update(sql, flowTime, flowTime, cwb);
	}

	public void updateFlowTime(String cwb, String flowTime) {
		String sql = "update express_ops_cwb_detail set nowtime=?  where cwb=? and state=1 ";
		jdbcTemplate.update(sql, flowTime, cwb);
	}

	public String getSQLExportTuotou(String begindate, String enddate, long isaudit, long isauditTime, String customerids, String cwbordertypeids, long paywayid, String dispatchbranchids,
			long deliverid, String operationOrderResultTypes, long page, Integer paybackfeeIsZero) {
		String sql = "SELECT de.* FROM `express_ops_cwb_detail` as de right join ops_delivery_successful as  ds on de.cwb=ds.cwb where de.state=1  ";
		if (begindate.length() > 0 || enddate.length() > 0 || isaudit > 0 || isauditTime > 0 || customerids.length() > 0 || cwbordertypeids.length() > 0 || paywayid > 0
				|| dispatchbranchids.length() > 0 || deliverid > 0 || operationOrderResultTypes.length() > 0 || paybackfeeIsZero > -1) {
			if (isauditTime == 0) {// 强制索引
				sql += " and ds.deliverytime>='" + begindate + "'  and ds.deliverytime<='" + enddate + "' ";
			} else {
				sql += " and ds.audittime >='" + begindate + "'  and ds.audittime <='" + enddate + "' ";
			}
			if (isaudit == 0) {
				sql += " and ds.auditstate<1";
			} else if (isaudit == 1) {
				sql += " and ds.auditstate>0";
			}

			if (customerids.length() > 0) {
				sql += " and ds.customerid in(" + customerids + ") ";
			}
			if (cwbordertypeids.length() > 0) {
				sql += " and ds.cwbordertypeid in(" + cwbordertypeids + ") ";
			}
			if (dispatchbranchids.length() > 0) {
				sql += " and ds.branchid in(" + dispatchbranchids + ") ";
			}
			if (operationOrderResultTypes.length() > 0) {
				sql += " and ds.deliverystate in(" + operationOrderResultTypes + ") ";
			}
			if (paywayid > 0) {
				sql += " and ds.paywayid = " + paywayid;
			}
			if (deliverid > 0) {
				sql += " and ds.deliveryid = " + deliverid;
			}
			if (paybackfeeIsZero > -1) {
				if (paybackfeeIsZero == 0) {
					sql += " and de.receivablefee=0 ";
				} else {
					sql += " and de.receivablefee>0 ";
				}
			}
		}
		sql += " limit " + page * Page.EXCEL_PAGE_NUMBER + " ," + Page.EXCEL_PAGE_NUMBER;
		logger.info("离线导出sql:{}", sql);
		return sql;
	}

	public String getSQLExportDanLiangChaXun(String begindate, String enddate, String customerids, String kufangid) {
		String sql = "SELECT * FROM express_ops_cwb_detail WHERE emaildate>='" + begindate + "' AND emaildate<='" + enddate + "' ";
		if (!kufangid.equals("0")) {
			sql += "and carwarehouse=" + kufangid;
		}
		if (!customerids.equals("")) {
			sql += " and customerid in (" + customerids + ") ";
		}
		sql += " AND state=1  ";
		logger.info("离线导出sql:{}", sql);
		return sql;
	}

	public String getSQLExportDanLiangChaXunForRuKu(String begindate, String enddate, String customerids, String kufangid) {
		String sql = "SELECT cd.*  FROM ops_kufangruku as ku left join express_ops_cwb_detail cd on ku.cwb=cd.cwb WHERE ku.intowarehousetime>='" + begindate + "' AND ku.intowarehousetime<='"
				+ enddate + "' and cd.state=1 ";
		if (!kufangid.equals("0")) {
			sql += " and ku.intobranchid=" + kufangid;
		}
		if (!customerids.equals("")) {
			sql += " and ku.customerid in (" + customerids + ") ";
		}
		logger.info("离线导出sql:{}", sql);
		return sql;
	}

	public String getSQLExportDanLiangChaXunForChuKu(String begindate, String enddate, String customerids, String kufangid) {
		String sql = "SELECT cd.*  FROM ops_delivery_chuku as ku left join express_ops_cwb_detail cd on ku.cwb=cd.cwb WHERE ku.outstoreroomtime>='" + begindate + "' AND ku.outstoreroomtime<='"
				+ enddate + "' and cd.state=1 ";
		if (!kufangid.equals("0")) {
			sql += " and ku.startbranchid=" + kufangid;
		}
		if (!customerids.equals("")) {
			sql += " and ku.customerid in (" + customerids + ") ";
		}
		logger.info("离线导出sql:{}", sql);
		return sql;
	}

	public String getSQLExportKuFangChuku(long page, String begindate, String enddate, String customerids, String kufangids, String nextbranchids, String cwbordertypeids) {
		String sql = "SELECT cd.*,dc.outstoreroomtimeuserid FROM `express_ops_cwb_detail` as cd right join ops_delivery_chuku as  dc on cd.cwb=dc.cwb where cd.state=1 "
				+ " and dc.outstoreroomtime >='" + begindate + "' and dc.outstoreroomtime <='" + enddate + "'";
		if (customerids.length() > 0 || cwbordertypeids.length() > 0 || nextbranchids.length() > 0 || kufangids.length() > 0) {

			if (kufangids.length() > 0) {
				sql += " and dc.startbranchid in(" + kufangids + ")";
			}
			if (customerids.length() > 0) {
				sql += " and dc.customerid in(" + customerids + ") ";
			}
			if (cwbordertypeids.length() > 0) {
				sql += " and dc.cwbordertypeid in(" + cwbordertypeids + ") ";
			}
			if (nextbranchids.length() > 0) {
				sql += " and dc.nextbranchid in(" + nextbranchids + ") ";
			}
		}
		sql += " limit " + page * Page.EXCEL_PAGE_NUMBER + " ," + Page.EXCEL_PAGE_NUMBER;
		logger.info("库房出库统计离线导出sql:{}", sql);
		return sql;
	}

	public String getSQLExportKDKChuku(long page, String begindate, String enddate, String customerids, String kufangids, String nextbranchids, String cwbordertypeids) {
		String sql = "SELECT cd.* FROM `express_ops_cwb_detail` as cd right join ops_delivery_kdkchuku as  dc on cd.cwb=dc.cwb where cd.state=1 " + " and dc.outstoreroomtime >='" + begindate
				+ "' and dc.outstoreroomtime <='" + enddate + "'";
		if (customerids.length() > 0 || cwbordertypeids.length() > 0 || nextbranchids.length() > 0 || kufangids.length() > 0) {

			if (kufangids.length() > 0) {
				sql += " and dc.startbranchid in(" + kufangids + ")";
			}
			if (customerids.length() > 0) {
				sql += " and dc.customerid in(" + customerids + ") ";
			}
			if (cwbordertypeids.length() > 0) {
				sql += " and dc.cwbordertypeid in(" + cwbordertypeids + ") ";
			}
			if (nextbranchids.length() > 0) {
				sql += " and dc.nextbranchid in(" + nextbranchids + ") ";
			}
		}
		sql += " limit " + page * Page.EXCEL_PAGE_NUMBER + " ," + Page.EXCEL_PAGE_NUMBER;
		logger.info("库对库出库统计离线导出sql:{}", sql);
		return sql;
	}

	public String getSQLExportDaoHuo(long page, String begindate, String enddate, long customerid, long startbranchid, String currentbranchids, String cwbordertypeids, long isnowdata) {
		String sql = "SELECT cd.* FROM `express_ops_cwb_detail` as cd right join ops_delivery_daohuo as  dh FORCE INDEX(Delivery_Daohuo_InSitetime_dx)"
				+ " on cd.cwb=dh.cwb where cd.state=1 and dh.inSitetime >='" + begindate + "' and dh.inSitetime <='" + enddate + "' and dh.isnow = " + isnowdata;
		if (customerid > 0 || cwbordertypeids.length() > 0 || currentbranchids.length() > 0 || startbranchid > 0) {

			if (startbranchid > 0) {
				sql += " and dh.startbranchid = " + startbranchid;
			}
			if (customerid > 0) {
				sql += " and dh.customerid =" + customerid;
			}
			if (cwbordertypeids.length() > 0) {
				sql += " and dh.cwbordertypeid in(" + cwbordertypeids + ") ";
			}
			if (currentbranchids.length() > 0) {
				sql += " and dh.currentbranchid in(" + currentbranchids + ") ";
			}
		}
		sql += " limit " + page * Page.EXCEL_PAGE_NUMBER + " ," + Page.EXCEL_PAGE_NUMBER;
		logger.info("分站到货统计离线导出sql:{}", sql);
		return sql;
	}

	public String getSQLExportDaoHuoHuizong(String begindate, String enddate, long branchid) {
		String sql = "SELECT cd.* FROM `express_ops_cwb_detail` as cd right join ops_delivery_daohuo as  dh FORCE INDEX(Delivery_Daohuo_InSitetime_dx)"
				+ " on cd.cwb=dh.cwb where cd.state=1 and dh.inSitetime >='" + begindate + "' and dh.inSitetime <='" + enddate + "'" + " and dh.currentbranchid =" + branchid;
		logger.info("站点到货汇总离线导出sql:{}", sql);
		return sql;
	}

	public String getSQLExportZhiLiu(long page, String begindate, String enddate, String dispatchbranchids, String customerids, long deliveryid, String cwbordertypeids, long isauditTime, long isaudit) {
		String sql = "SELECT de.* FROM `express_ops_cwb_detail` as de right join ops_delivery_zhiliu as  dz on de.cwb=dz.cwb where de.state=1  ";
		if (begindate.length() > 0 || enddate.length() > 0 || isaudit > 0 || isauditTime > 0 || customerids.length() > 0 || cwbordertypeids.length() > 0 || dispatchbranchids.length() > 0
				|| deliveryid > 0) {
			if (isauditTime == 0) {// 强制索引
				sql += " and dz.deliverytime>='" + begindate + "'  and dz.deliverytime<='" + enddate + "' ";
			} else {
				sql += " and dz.audittime >='" + begindate + "'  and dz.audittime <='" + enddate + "' ";
			}
			if (isaudit >= 0) {
				if (isaudit == 0) {
					sql += " and dz.gcaid<1";
				} else if (isaudit == 1) {
					sql += " and dz.gcaid>0";
				}

			}
			if (customerids.length() > 0) {
				sql += " and dz.customerid in(" + customerids + ") ";
			}
			if (cwbordertypeids.length() > 0) {
				sql += " and dz.cwbordertypeid in(" + cwbordertypeids + ") ";
			}
			if (dispatchbranchids.length() > 0) {
				sql += " and dz.branchid in(" + dispatchbranchids + ") ";
			}
			if (deliveryid > 0) {
				sql += " and dz.deliveryid = " + deliveryid;
			}
		}
		sql += " limit " + page * Page.EXCEL_PAGE_NUMBER + " ," + Page.EXCEL_PAGE_NUMBER;
		logger.info("离线导出sql:{}", sql);
		return sql;
	}

	public String getSQLExportZhongZhuanTongJizong(String begindate, String enddate, String nextbranchids, String startbranchids) {
		String sql = "SELECT cd.*,cd.zhongzhuanrukutime as zhongzhuanzhangintime,cd.zhongzhuanzhanchukutime  as zhongzhuanzhangouttime FROM `express_ops_cwb_detail` as cd right join `ops_zhongzhuan` as zz FORCE INDEX(ZZ_Zhongzhuanoutstoreroomtime_Idx) "
				+ " on cd.cwb=zz.cwb where  cd.state=1 and zz.zhongzhuanoutstoreroomtime>='" + begindate + "'  and zz.zhongzhuanoutstoreroomtime<='" + enddate + "' ";
		if (nextbranchids.length() > 0) {
			sql += " and zz.nextbranchid in(" + nextbranchids + ") ";
		}
		if (startbranchids.length() > 0) {
			sql += " and zz.startbranchid in(" + startbranchids + ") ";
		}
		logger.info("中转订单统计离线导出sql:{}", sql);
		return sql;
	}

	public String getSQLExportTuiHuoChuZhan(long page, String begindate, String enddate, String branchids, String customerids, long istuihuozhanruku) {
		String sql = "SELECT de.* FROM `express_ops_cwb_detail` as de right join ops_delivery_tuihuochuzhan as  dt on de.cwb=dt.cwb where de.state=1  ";
		if (branchids.length() > 0 || customerids.length() > 0 || istuihuozhanruku > 0 || begindate.length() > 0 || enddate.length() > 0) {
			if (begindate.length() > 0) {
				sql += " and dt.tuihuochuzhantime>='" + begindate + "'";
			}
			if (enddate.length() > 0) {
				sql += " and dt.tuihuochuzhantime<='" + enddate + "'";
			}
			if (branchids.length() > 0) {
				sql += " and dt.tuihuobranchid in(" + branchids + ")";
			}
			if (customerids.length() > 0) {
				sql += " and dt.customerid in(" + customerids + ")";
			}
			if (istuihuozhanruku != 0) {
				if (istuihuozhanruku == 1) {
					sql += " and dt.tuihuozhanrukutime <>'' ";
				} else {
					sql += " and dt.tuihuozhanrukutime ='' ";
				}
			}
		}
		sql += " limit " + page * Page.EXCEL_PAGE_NUMBER + " ," + Page.EXCEL_PAGE_NUMBER;
		return sql;
	}

	public String getSQLExportJuShou(long page, String begindate, String enddate, String dispatchbranchids, String customerids, long deliveryid, String cwbordertypeids, long isauditTime,
			long isaudit, String operationOrderResultTypes) {
		String sql = "SELECT de.* FROM `express_ops_cwb_detail` as de right join ops_delivery_jushou as  dj on de.cwb=dj.cwb where de.state=1  ";
		if (begindate.length() > 0 || enddate.length() > 0 || isaudit > 0 || isauditTime > 0 || operationOrderResultTypes.length() > 0 || customerids.length() > 0 || cwbordertypeids.length() > 0
				|| dispatchbranchids.length() > 0 || deliveryid > 0) {
			if (isauditTime == 0) {// 强制索引
				sql += " and dj.deliverytime>='" + begindate + "'  and dj.deliverytime<='" + enddate + "' ";
			} else {
				sql += " and dj.audittime >='" + begindate + "'  and dj.audittime <='" + enddate + "' ";
			}
			if (isaudit >= 0) {
				if (isaudit == 0) {
					sql += " and dj.gcaid<1";
				} else if (isaudit == 1) {
					sql += " and dj.gcaid>0";
				}

			}
			if (customerids.length() > 0) {
				sql += " and dj.customerid in(" + customerids + ") ";
			}
			if (cwbordertypeids.length() > 0) {
				sql += " and dj.cwbordertypeid in(" + cwbordertypeids + ") ";
			}
			if (dispatchbranchids.length() > 0) {
				sql += " and dj.branchid in(" + dispatchbranchids + ") ";
			}
			if (operationOrderResultTypes.length() > 0) {
				sql += " and dj.deliverystate in(" + operationOrderResultTypes + ") ";
			}
			if (deliveryid > 0) {
				sql += " and dj.deliveryid = " + deliveryid;
			}
		}
		sql += " limit " + page * Page.EXCEL_PAGE_NUMBER + " ," + Page.EXCEL_PAGE_NUMBER;
		logger.info("离线导出sql:{}", sql);
		return sql;
	}

	public String getSQLExportTiHuo(long page, String begindate, String enddate, String customerids) {
		String sql = "SELECT cd.cwb,cd.consigneename,cd.consigneeaddress,cd.customerid,th.tihuotime FROM `express_ops_cwb_detail` as cd right join ops_cwb_tihuo as th FORCE INDEX(TiHuo_TiHuoTime_Idx) on cd.cwb=th.cwb "
				+ " where cd.state=1 and th.tihuotime >='" + begindate + "' and th.tihuotime <='" + enddate + "'";
		if (customerids.length() > 0) {
			sql += " and th.customerid in(" + customerids + ") ";
		}
		sql += " limit " + page * Page.EXCEL_PAGE_NUMBER + " ," + Page.EXCEL_PAGE_NUMBER;
		logger.info("提货订单统计离线导出sql:{}", sql);
		return sql;
	}

	/**
	 * 上游 根据emaildateid 获取 下游 已经获取的订单
	 * 
	 * @param emaildateid
	 * @param page
	 * @return
	 */
	public List<CwbOrder> getCwbByEmaildate(long emaildateid, long page) {
		String sql = "SELECT cd.* FROM `express_ops_cwb_detail`  cd  LEFT JOIN commen_cwb_order co ON cd.cwb=co.cwb WHERE cd.state=1 and co.emaildateid='" + emaildateid + "'";
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + "," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new CwbMapper());
	}

	/**
	 * 导出
	 * 
	 * @param emaildateid
	 * @return
	 */
	public String getSQLExportCommon(long emaildateid) {
		String sql = "SELECT cd.*,co.commencode as acommoncode FROM `express_ops_cwb_detail`cd  LEFT JOIN  commen_cwb_order co ON cd.cwb=co.cwb WHERE cd.state=1 and co.emaildateid='" + emaildateid
				+ "'";
		return sql;
	}

	/**
	 * 根据订单查询
	 * 
	 * @param cwbs
	 * @param page
	 * @return
	 */
	public List<CwbOrder> getCwbByCwbsAndEmaildate(String cwbs, long page) {
		String sql = "SELECT cd.*  FROM `express_ops_cwb_detail`  cd  LEFT JOIN commen_cwb_order co ON cd.cwb=co.cwb WHERE cd.state=1 and co.cwb IN(" + cwbs + ")";
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + "," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new CwbMapper());
	}

	/**
	 * 按照订单 导出
	 * 
	 * @param cwbs
	 * @return
	 */
	public String getSQLExportCommonBycwbs(String cwbs) {
		String sql = "SELECT cd.*,co.commencode as acommoncode FROM `express_ops_cwb_detail`  cd  LEFT JOIN commen_cwb_order co ON cd.cwb=co.cwb WHERE cd.state=1 and co.cwb IN(" + cwbs + ")";
		return sql;
	}

	public String getSQLExportKuFangChuKuHuiZong(long page, long customerid, long kufangid, long nextbranchid, String begindate, String enddate) {
		String sql = "SELECT cd.* FROM `express_ops_cwb_detail` as cd right join ops_delivery_chuku as  dc FORCE INDEX(Delivery_Chuku_Outstoreroomtime_Idx) on cd.cwb=dc.cwb where cd.state=1 "
				+ " and dc.outstoreroomtime >='" + begindate + "' and dc.outstoreroomtime <= '" + enddate + "'" + " and dc.startbranchid=" + kufangid;

		if (customerid > 0) {
			sql += " and dc.customerid = " + customerid;
		}
		if (nextbranchid > 0) {
			sql += " and dc.nextbranchid = " + nextbranchid;
		}
		sql += " limit " + page * Page.EXCEL_PAGE_NUMBER + " ," + Page.EXCEL_PAGE_NUMBER;
		return sql;
	}

	/**
	 * 退货站入库统计 导出
	 * 
	 * @param page
	 * @param begindate
	 * @param enddate
	 * @param branchids
	 * @param customerids
	 * @param cwbordertypeids
	 * @return
	 */
	public String getSQLExportTuiHuoZhanRuKu(long page, String begindate, String enddate, String branchids, String customerids, String cwbordertypeids) {
		String sql = "SELECT de.* FROM `express_ops_cwb_detail` as de right join ops_tuihuozhanruku as  dt on de.cwb=dt.cwb " + "where de.state=1 and dt.rukutime>='" + begindate
				+ "' and dt.rukutime<='" + enddate + "'";
		if (branchids.length() > 0 || customerids.length() > 0 || cwbordertypeids.length() > 0) {
			if (branchids.length() > 0) {
				sql += " and dt.tuihuobranchid in(" + branchids + ")";
			}
			if (customerids.length() > 0) {
				sql += " and dt.customerid in(" + customerids + ")";
			}
			if (cwbordertypeids.length() > 0) {
				sql += " and dt.cwbordertypeid in(" + cwbordertypeids + ")";
			}

		}
		sql += " limit " + page * Page.EXCEL_PAGE_NUMBER + " ," + Page.EXCEL_PAGE_NUMBER;
		return sql;
	}

	public String getSQLExportKuFangZaiTu(long page, String begindate, String enddate, String kufangids, String nextbranchids, String cwbordertypeids, long datetype) {
		String sql = "SELECT de.* FROM `express_ops_cwb_detail` as de right join ops_kufangzaitu as okf on de.cwb=okf.cwb where de.state=1  ";
		if (begindate.length() > 0 || enddate.length() > 0 || datetype > 0 || kufangids.length() > 0 || cwbordertypeids.length() > 0 || nextbranchids.length() > 0) {
			if (datetype == 1) {
				sql += " and okf.emaildate>='" + begindate + "'  and okf.emaildate<='" + enddate + "' ";
			} else {
				sql += " and okf.outwarehousetime >='" + begindate + "'  and okf.outwarehousetime <='" + enddate + "' ";
			}
			if (cwbordertypeids.length() > 0) {
				sql += " and okf.cwbordertypeid in(" + cwbordertypeids + ") ";
			}
			if (kufangids.length() > 0) {
				sql += " and okf.outbranchid  in(" + kufangids + ") ";
			}
			if (nextbranchids.length() > 0) {
				sql += " and okf.nextbranchid in(" + nextbranchids + ")";
			}
		}
		sql += " limit " + page * Page.EXCEL_PAGE_NUMBER + " ," + Page.EXCEL_PAGE_NUMBER;
		logger.info("离线导出sql:{}", sql);
		return sql;
	}

	public String getSQLExportKuFangRuKuTongJi(long page, String isruku, String begindate, String enddate, String emaildatebegin, String emaildateend, long kufangid, long cwbordertypeid,
			String customers) {
		String sql = "SELECT de.*,okf.intowarehouseuserid FROM `express_ops_cwb_detail` as de right join ops_kufangruku as okf on de.cwb=okf.cwb where de.state=1  ";
		if ("false".equals(isruku)) {
			sql += " and okf.emaildate>='" + emaildatebegin + "' and okf.emaildate<='" + emaildateend + "'";
			sql += " and okf.isruku=0";
		} else {
			sql += " and okf.intowarehousetime>='" + begindate + "' and okf.intowarehousetime<='" + enddate + "'";
			if (emaildatebegin.length() > 0) {
				sql += " and okf.emaildate>='" + emaildatebegin + "'";
			}
			if (emaildateend.length() > 0) {
				sql += " and okf.emaildate<='" + emaildateend + "'";
			}
			sql += " and okf.isruku=1";
		}
		if (kufangid > 0) {
			sql += " and okf.intobranchid=" + kufangid;
		}
		if (cwbordertypeid > 0) {
			sql += " and okf.cwbordertypeid=" + cwbordertypeid;
		}
		if (customers.length() > 0) {
			sql += " and okf.customerid in(" + customers + ")";
		}
		sql += " limit " + page * Page.EXCEL_PAGE_NUMBER + " ," + Page.EXCEL_PAGE_NUMBER;
		return sql;
	}

	/**
	 * 客户发货统计 导出
	 * 
	 * @param page
	 * @param begindate
	 * @param enddate
	 * @param cwbordertypeids
	 * @param kufangids
	 * @param customers
	 * @param flowordertype
	 * @return
	 */
	public String getSQLExportKeHuFaHuoTongJi(long page, String begindate, String enddate, String cwbordertypeids, String kufangids, String customers, long flowordertype, String servicetype) {
		String sql = "select * from  express_ops_cwb_detail where emaildate>='" + begindate + "' and emaildate<='" + enddate + "' and state=1 ";
		sql = this.getSqlForKeHuFaHuoTongJi(sql, cwbordertypeids, kufangids, customers, flowordertype, servicetype);
		sql += " limit " + page * Page.EXCEL_PAGE_NUMBER + " ," + Page.EXCEL_PAGE_NUMBER;
		return sql;
	}

	private String getSqlForKeHuFaHuoTongJi(String sql, String cwbordertypeids, String kufangids, String customers, long flowordertype, String servicetype) {
		if (cwbordertypeids.length() > 0) {
			sql += " and cwbordertypeid in(" + cwbordertypeids + ")";
		}
		if (kufangids.length() > 0) {
			sql += " and carwarehouse in( " + kufangids + ")";
		}
		if (customers.length() > 0) {
			sql += " and customerid in(" + customers + ")";
		}

		if (servicetype.length() > 0 && !"全部".equals(servicetype)) {
			sql += " and cartype = '" + servicetype + "'";
		}

		if (flowordertype > 0) {
			sql += " and flowordertype = " + flowordertype;
		}
		return sql;
	}

	private String getSqlForFenZhanDaoHuoHuiZong(String sql, String cwbordertypeids, String currentbranchids, String customers, String kufangids, Integer isnow) {
		if (cwbordertypeids.length() > 0) {
			sql += " and cwbordertypeid in(" + cwbordertypeids + ")";
		}
		if (currentbranchids.length() > 0) {
			sql += " and currentbranchid in( " + currentbranchids + ")";
		}
		if (customers.length() > 0 && !customers.trim().equals("0")) {

			sql += " and customerid in(" + customers + ")";
		}
		if (kufangids.length() > 0) {
			sql += " and startbranchid in(" + kufangids + ") ";
		}
		if (isnow > 0) {
			sql += " and isnow = " + isnow;
		}
		return sql;
	}

	public long getKeHuFaHuoTongJiCount(String begindate, String enddate, String customerids, String cwbordertypeids, String kufangids, long flowordertype, String servicetype) {
		String sql = "select count(1) from  express_ops_cwb_detail where emaildate>='" + begindate + "' and emaildate<='" + enddate + "' and state=1 ";
		sql = this.getSqlForKeHuFaHuoTongJi(sql, cwbordertypeids, kufangids, customerids, flowordertype, servicetype);
		return jdbcTemplate.queryForLong(sql);
	}

	public CwbOrder getcwborderSumHuiZong(String begindate, String enddate, String customerids, String cwbordertypeids, String kufangids, long flowordertype) {
		String sql = "select sum(receivablefee) as receivablefee,sum(paybackfee) as paybackfee from express_ops_cwb_detail where emaildate>='" + begindate + "' and emaildate<='" + enddate + "'";
		sql = this.getSqlForKeHuFaHuoTongJi(sql, cwbordertypeids, kufangids, customerids, flowordertype, "");
		try {
			return jdbcTemplate.queryForObject(sql, new CwbMOneyMapper());
		} catch (DataAccessException e) {
			return new CwbOrder();
		}
	}

	public List<CwbOrder> getcwbOrderByPageHuiZong(long page, String begindate, String enddate, String customerids, String cwbordertypeids, String kufangids, long flowordertype) {
		String sql = "select * from  express_ops_cwb_detail where emaildate>='" + begindate + "' and emaildate<='" + enddate + "'";
		sql = this.getSqlForKeHuFaHuoTongJi(sql, cwbordertypeids, kufangids, customerids, flowordertype, "");
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new CwbMapper());
	}

	public List<CwbOrder> getKeHuFaHuoTongJi(long page, String begindate, String enddate, String customerids, String cwbordertypeids, String kufangids, long flowordertype, String servicetype) {
		String sql = "select * from  express_ops_cwb_detail where emaildate>='" + begindate + "' and emaildate<='" + enddate + "' and state=1 ";
		sql = this.getSqlForKeHuFaHuoTongJi(sql, cwbordertypeids, kufangids, customerids, flowordertype, servicetype);
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new CwbMapper());
	}

	public CwbOrder getKeHuFaHuoTongJiSum(String begindate, String enddate, String customerids, String cwbordertypeids, String kufangids, long flowordertype, String servicetype) {
		String sql = "select sum(receivablefee) as receivablefee,sum(paybackfee) as paybackfee from express_ops_cwb_detail where emaildate>='" + begindate + "' and emaildate<='" + enddate
				+ "' and state=1 ";
		sql = this.getSqlForKeHuFaHuoTongJi(sql, cwbordertypeids, kufangids, customerids, flowordertype, servicetype);
		try {
			return jdbcTemplate.queryForObject(sql, new CwbMOneyMapper());
		} catch (DataAccessException e) {
			return new CwbOrder();
		}
	}

	// 报表方式导出
	public Map<Long, Map<Long, Long>> getKeHuFaHuoHuiZongMap(String begindate, String enddate, String customers, String cwbordertypeids, String kufangids, long flowordertype, String servicetype) {
		String sql = "select * from  express_ops_cwb_detail where emaildate>='" + begindate + "' and emaildate<='" + enddate + "' and state=1 ";
		sql = this.getSqlForKeHuFaHuoTongJi(sql, cwbordertypeids, kufangids, customers, flowordertype, servicetype);
		// 库房 供货商 数量
		final Map<Long, Map<Long, Long>> huiZongMap = new HashMap<Long, Map<Long, Long>>();
		jdbcTemplate.query(new StreamingStatementCreator(sql), new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				// 如果库房已经存在 就存进去 没有 就put
				if (!huiZongMap.containsKey(rs.getLong("carwarehouse"))) {
					Map<Long, Long> customerMap = new HashMap<Long, Long>();
					huiZongMap.put(rs.getLong("carwarehouse"), customerMap);
				}
				long tempcount = 1l;
				if (!huiZongMap.get(rs.getLong("carwarehouse")).containsKey(rs.getLong("customerid"))) {
					huiZongMap.get(rs.getLong("carwarehouse")).put(rs.getLong("customerid"), 0l);
				}
				huiZongMap.get(rs.getLong("carwarehouse")).put(rs.getLong("customerid"), huiZongMap.get(rs.getLong("carwarehouse")).get(rs.getLong("customerid")) + tempcount);
			}

		});
		return huiZongMap;
	}

	/**
	 * 综合查询生成xls的SQL
	 * 
	 * @param tail
	 * @param page
	 * @return
	 */
	public String getSQLExportZongHeChaXun(CwbOrderTail tail, long page) {

		String sql = "SELECT de.*,ds.newpaywayid FROM `express_ops_cwb_detail` as de right join  commen_cwb_order_tail as  ds on de.cwb=ds.cwb where de.state=1 ";

		if (tail.getBegintime() != null && tail.getCurquerytimecolumn() != null) {

			sql = sql + " and ds." + tail.getCurquerytimecolumn() + ">='" + tail.getBegintime() + "'";

		}
		if (tail.getEndtime() != null && tail.getCurquerytimecolumn() != null) {

			sql = sql + " and ds." + tail.getCurquerytimecolumn() + "<='" + tail.getEndtime() + "'";

		}
		/** 当前站 **/
		if (tail.getBranchid().length() > 0) {
			sql += "  and ds.branchid in(" + tail.getBranchid() + ")";
		}

		if (tail.getDeliverybranchid().length() > 0) {
			sql += "  and ds.deliverybranchid in(" + tail.getDeliverybranchid() + ")";
		}
		if (tail.getNextbranchid().length() > 0) {
			sql += "  and ds.nextbranchid in(" + tail.getNextbranchid() + ")";
		}

		if (tail.getCustomerstr().length() > 0) {
			sql += " and ds.customerid in(" + tail.getCustomerstr() + ")";
		}

		if (tail.getCwbordertypestr().length() > 0) {
			sql += "  and ds.cwbordertypeid in(" + tail.getCwbordertypestr() + ")";
		}

		if (tail.getDeliverystateStr().length() > 0) {
			sql += "  and ds.deliverystate in(" + tail.getDeliverystateStr() + ")";
		}

		/** 归班状态 **/
		if (!tail.getGobackstate().equals("-1")) {
			if (tail.getGobackstate().equals("0")) {
				sql = sql + " and ds.flowordertype<>36";
			} else {
				sql = sql + " and ds.flowordertype=36";
			}
		}

		/** 支付方式 **/
		if (!tail.getPaywayid().equals("-1")) {
			sql = sql + " and ds.paywayid='" + tail.getPaywayid() + "'";
		}
		/** 当前支付方式 **/
		if (!tail.getNewpaywayid().equals("-1")) {
			sql = sql + " and ds.newpaywayid='" + tail.getNewpaywayid() + "'";
		}
		/** 订单状态 **/
		if (tail.getFlowordertype() != -1) {
			sql = sql + " and ds.flowordertype=" + tail.getFlowordertype() + "";
		}
		
		sql += " limit " + page * Page.EXCEL_PAGE_NUMBER + " ," + Page.EXCEL_PAGE_NUMBER;

		System.out.println("sql:" + sql);

		logger.info("综合查询统计离线导出sql:{}", sql);

		return sql;
	}

	// 报表方式导出
	public Map<Long, Map<Long, Long>> getFenZhanDaoHuoHuiZongMap(String begindate, String enddate, String customers, String cwbordertypeids, String currentbranchid, String kufangid, Integer isshow) {
		String sql = "select currentbranchid,customerid from  ops_delivery_daohuo where emaildate>='" + begindate + "' and emaildate<='" + enddate + "'";
		sql = this.getSqlForFenZhanDaoHuoHuiZong(sql, cwbordertypeids, currentbranchid, customers, kufangid, isshow);
		System.out.println("sql:" + sql);
		// 站点 供货商 数量
		final Map<Long, Map<Long, Long>> huiZongMap = new HashMap<Long, Map<Long, Long>>();
		jdbcTemplate.query(new StreamingStatementCreator(sql), new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				// 如果站点已经存在 就存进去 没有 就put
				if (!huiZongMap.containsKey(rs.getLong("currentbranchid"))) {
					Map<Long, Long> customerMap = new HashMap<Long, Long>();
					huiZongMap.put(rs.getLong("currentbranchid"), customerMap);
				}
				long tempcount = 1l;
				if (!huiZongMap.get(rs.getLong("currentbranchid")).containsKey(rs.getLong("customerid"))) {
					huiZongMap.get(rs.getLong("currentbranchid")).put(rs.getLong("customerid"), 0l);
				}
				huiZongMap.get(rs.getLong("currentbranchid")).put(rs.getLong("customerid"), huiZongMap.get(rs.getLong("currentbranchid")).get(rs.getLong("customerid")) + tempcount);
			}

		});
		return huiZongMap;
	}

	/**
	 * 客户发货时间
	 * 
	 * @param emaildateStar
	 * @param emaildateEnd
	 * @param kufangid
	 * @return
	 */

	public List<CwbOrder> getCwbByCustomeridAndEmaildate(String emaildateStar, String emaildateEnd, long kufangid) {
		String sql = "SELECT * FROM express_ops_cwb_detail WHERE emaildate>='" + emaildateStar + "' AND emaildate<='" + emaildateEnd + " 23:59:59' ";
		if (kufangid > 0) {
			sql += "and Carwarehouse=" + kufangid;
		}
		sql += " AND state=1 order by emaildate ";
		logger.info("客户发货时间：" + sql);
		return jdbcTemplate.query(sql, new CwbMapper());
	}

	/**
	 * 客户发货时间
	 * 
	 * @param emaildateStar
	 * @param emaildateEnd
	 * @param kufangid
	 * @return
	 */

	public List<CwbOrder> getEditByCustomeridAndEmaildate(String emaildateEnd, long kufangid, long customerid, long page) {
		String sql = "SELECT * FROM express_ops_cwb_detail WHERE emaildate>='" + emaildateEnd + " 00:00:00' AND emaildate<='" + emaildateEnd + " 23:59:59' ";
		if (kufangid > 0) {
			sql += "and carwarehouse=" + kufangid;
		}
		if (customerid > 0) {
			sql += " and customerid=" + customerid;
		}
		sql += " AND state=1  ";
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new CwbMapper());
	}

	public long getCountByCustomeridAndEmaildate(String emaildateEnd, long kufangid, long customerid) {
		String sql = "SELECT count(1) FROM express_ops_cwb_detail  WHERE emaildate>='" + emaildateEnd + " 00:00:00' AND emaildate<='" + emaildateEnd + " 23:59:59' ";
		if (kufangid > 0) {
			sql += "and carwarehouse=" + kufangid;
		}
		if (customerid > 0) {
			sql += " and customerid=" + customerid;
		}
		sql += " AND state=1  ";
		return jdbcTemplate.queryForLong(sql);
	}

	/**
	 * 获取亚马逊到站延误订单
	 * 
	 * @param time
	 * @param page
	 * @return
	 */
	public List<CwbOrder> getAmazonZitiByPage(long time, long page) {
		String sql = "select de.* from express_ops_b2c_amazon_ziti as az left join express_ops_cwb_detail as de " + " on az.cwb=de.cwb  where de.state=1 and az.createtime<" + time
				+ " and az.state=0  limit 0," + page;
		return jdbcTemplate.query(sql, new CwbMapper());
	}

	public long getAmazonZitiByBranchid(long time, long branchid) {
		String sql = "select count(1) from express_ops_b2c_amazon_ziti as az left join express_ops_cwb_detail as de " + " on az.cwb=de.cwb  where de.state=1 and az.createtime<" + time
				+ " and az.branchid=?";
		return jdbcTemplate.queryForLong(sql, branchid);
	}

	public List<CwbOrder> getAmazonZitiListByBranchid(long time, long branchid, long page) {
		String sql = "select * from express_ops_b2c_amazon_ziti as az left join express_ops_cwb_detail as de " + " on az.cwb=de.cwb  where de.state=1 and az.createtime<" + time + " and az.branchid=?";
		sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + "," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new CwbMapper(), branchid);
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
	 * @param deliverystate
	 *            反馈状态 置为0 配送结果与反馈表一致 会根据反馈的状态而变更，而领货时是0
	 */
	public void updateForChongZhiShenHe(String cwb, Long nextbranchid, long flowordertype, Long currentbranchid, long deliverystate) {
		jdbcTemplate.update("update express_ops_cwb_detail set nextbranchid=?,flowordertype=?" + ",currentbranchid=?,deliverystate=? where cwb=?", nextbranchid, flowordertype, currentbranchid,
				deliverystate, cwb);
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
	public void updateXiuGaiJinE(String cwb, BigDecimal receivablefee, BigDecimal paybackfee) {
		jdbcTemplate.update("update express_ops_cwb_detail set receivablefee=?,paybackfee=? where cwb=?", receivablefee, paybackfee, cwb);
	}

	/**
	 * 修改订单支付方式 修改订单表字段
	 * 
	 * @param opscwbid
	 *            要修改的订单
	 * @param newpaywayid
	 *            订单要修改为的支付方式
	 */
	public void updateXiuGaiZhiFuFangShi(String cwb, int newpaywayid) {
		jdbcTemplate.update("update express_ops_cwb_detail set newpaywayid=? where cwb=?", newpaywayid, cwb);
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
	public void updateXiuGaiDingDanLeiXing(String cwb, int cwbordertypeid, long deliverystate) {
		jdbcTemplate.update("update express_ops_cwb_detail set cwbordertypeid=?,deliverystate=? where cwb=?", cwbordertypeid, deliverystate, cwb);

	}
}
