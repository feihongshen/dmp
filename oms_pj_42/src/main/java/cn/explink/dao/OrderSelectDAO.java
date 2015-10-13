package cn.explink.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import cn.explink.domain.CwbOrder;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Component
public class OrderSelectDAO {

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
			cwbOrder.setPodfeetoheadtime(StringUtil.nullConvertToEmptyString(rs.getString("podfeetoheadtime")));
			cwbOrder.setPodfeetoheadchecktime(StringUtil.nullConvertToEmptyString(rs.getString("podfeetoheadchecktime")));
			cwbOrder.setPodfeetoheadcheckflag(StringUtil.nullConvertToEmptyString(rs.getString("podfeetoheadcheckflag")));
			cwbOrder.setLeavedreasonid(rs.getLong("leavedreasonid"));
			cwbOrder.setDeliversubscribeday(StringUtil.nullConvertToEmptyString(rs.getString("deliversubscribeday")));
			cwbOrder.setCustomerwarehouseid(StringUtil.nullConvertToEmptyString(rs.getString("customerwarehouseid")));
			cwbOrder.setEmaildate(StringUtil.nullConvertToEmptyString(rs.getString("emaildate")));
			cwbOrder.setShiptime(StringUtil.nullConvertToEmptyString(rs.getString("shiptime")));
			cwbOrder.setServiceareaid(rs.getLong("serviceareaid"));
			cwbOrder.setCustomerid(rs.getLong("customerid"));
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
			cwbOrder.setBranchid(rs.getInt("branchid"));
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
			cwbOrder.setBackreasonid(rs.getLong("backreasonid"));

			cwbOrder.setDelivername(StringUtil.nullConvertToEmptyString(rs.getString("delivername")));
			cwbOrder.setExpt_code(StringUtil.nullConvertToEmptyString(rs.getString("expt_code")));
			cwbOrder.setExpt_msg(StringUtil.nullConvertToEmptyString(rs.getString("expt_msg")));
			cwbOrder.setPaytype_old(rs.getString("paytype_old"));
			cwbOrder.setOrderResultType(rs.getLong("orderResultType"));
			return cwbOrder;
		}
	}

	private final class CwbSimpleMapper implements RowMapper<CwbOrder> {
		@Override
		public CwbOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
			CwbOrder cwbOrder = new CwbOrder();
			cwbOrder.setOpscwbid(rs.getLong("opscwbid"));
			cwbOrder.setCwb(rs.getString("cwb"));
			cwbOrder.setStartbranchid(rs.getLong("startbranchid"));
			cwbOrder.setNextbranchid(rs.getLong("nextbranchid"));
			cwbOrder.setExceldeliver(rs.getString("exceldeliver"));
			cwbOrder.setDeliverid(rs.getLong("deliverid"));
			cwbOrder.setExcelbranch(rs.getString("excelbranch"));
			cwbOrder.setFlowordertype(rs.getLong("flowordertype"));
			return cwbOrder;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public CwbOrder getCwbByCwb(String cwb) {
		try {
			return jdbcTemplate.queryForObject("SELECT * from express_ops_cwb_detail where cwb=? and state=1", new CwbMapper(), cwb);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<CwbOrder> getCwbsByEmailDate(Date date) {
		return jdbcTemplate.query("select * from express_ops_cwb_detail where emaildate=? and state=1", new CwbMapper(), date);
	}

	public List<CwbOrder> getAllCwbOrder() {
		return jdbcTemplate.query("select * from express_ops_cwb_detail  where state=1", new CwbMapper());
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

	/**
	 * lansheng
	 * 
	 * @param sql
	 * @param consigneename
	 * @param consigneephone
	 * @param beginemaildate
	 * @param endemaildate
	 * @param sendcarname
	 * @param ordercwb
	 * @return
	 */
	private String getCwbOrderByPageWhereSql2(String sql, String consigneename, String consigneephone, String consigneemobile, String beginemaildate, String endemaildate, String sendcarname,
			String ordercwb) {

		if (consigneename.length() > 0 || consigneephone.length() > 0 || consigneemobile.length() > 0 || beginemaildate.length() > 0 || endemaildate.length() > 0 || sendcarname.length() > 0
				|| ordercwb.length() > 0) {
			StringBuffer w = new StringBuffer();
			sql += " where ";
			if (consigneename.length() > 0) {
				w.append(" and consigneename ='" + consigneename + "'");
			}
			if (consigneephone.length() > 0) {
				w.append(" and consigneephone like '%" + consigneephone + "%'");
			}
			if (consigneemobile.length() > 0) {
				w.append(" and consigneemobile='" + consigneemobile + "'");
			}
			if (beginemaildate.length() > 0) {
				w.append(" and emaildate > '" + beginemaildate + "'");
			}
			if (endemaildate.length() > 0) {
				w.append(" and emaildate < '" + endemaildate + "'");
			}
			if (sendcarname.length() > 0) {
				w.append(" and sendcarname like '%" + sendcarname + "%'");
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

	/**
	 * lansheng
	 * 
	 * @param page
	 * @param consigneename
	 * @param consigneephone
	 * @param consigneemobile
	 * @param beginemaildate
	 * @param endemaildate
	 * @param ordercwb
	 * @return
	 */
	public List<CwbOrder> getcwbOrderByPage2(long page, String consigneename, String consigneephone, String consigneemobile, String beginemaildate, String endemaildate, String sendcarname,
			String ordercwb) {
		String sql = "select * from express_ops_cwb_detail";
		sql = this.getCwbOrderByPageWhereSql2(sql, consigneename, consigneephone, consigneemobile, beginemaildate, endemaildate, sendcarname, ordercwb);
		sql += "  ORDER BY emaildate DESC limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		List<CwbOrder> cwborderList = jdbcTemplate.query(sql, new CwbMapper());
		return cwborderList;
	}

	public long getcwborderCount(long customerid, long branchid, String beginemaildate, String endemaildate, String ordercwb, long servicearea, String emailfinishflag, String emaildate) {
		String sql = "select count(1) from express_ops_cwb_detail";
		sql = this.getCwbOrderByPageWhereSql(sql, customerid, branchid, beginemaildate, endemaildate, ordercwb, servicearea, emailfinishflag, emaildate);
		return jdbcTemplate.queryForInt(sql);
	}

	/**
	 * lansheng
	 * 
	 * @param customerid
	 * @param branchid
	 * @param beginemaildate
	 * @param endemaildate
	 * @param ordercwb
	 * @param servicearea
	 * @param emailfinishflag
	 * @param emaildate
	 * @return
	 */
	public long getcwborderCount2(String consigneename, String consigneephone, String consigneemobile, String beginemaildate, String endemaildate, String sendcarname, String ordercwb) {
		String sql = "select count(1) from express_ops_cwb_detail";
		sql = this.getCwbOrderByPageWhereSql2(sql, consigneename, consigneephone, consigneemobile, beginemaildate, endemaildate, sendcarname, ordercwb);
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

	/**
	 * lansheng
	 * 
	 * @param of
	 * @return
	 */
	public long updateOrderFlow(String customercommand, String cwb) {
		return jdbcTemplate.update("update express_ops_cwb_detail set customercommand ='" + customercommand + "' where state=1 and cwb='" + cwb + "' ");
	}

	public long updateOrder(String cwbremack, String edittime, String editman, String cwb) {
		return jdbcTemplate.update("update express_ops_cwb_detail set cwbremark=CONCAT(IF(cwbremark IS NULL,'',cwbremark),' \n" + edittime + ":"
				+ (cwbremack == null || cwbremack.equals("null") ? "" : cwbremack) + "'),editman='" + editman + "'  where state =1 and ( cwb='" + cwb + "' or transcwb ='" + cwb + "' )");
	}

	public long updateReceivablefee(String receivablefee, String edittime, String editman, String cwb) {
		return jdbcTemplate.update("update express_ops_cwb_detail set receivablefee =" + receivablefee + ",edittime='" + edittime + "',editman='" + editman + "'  where state=1 and ( cwb='" + cwb
				+ "' or transcwb ='" + cwb + "' )");
	}

	public long updateReceivablefeeForOrderFlow(String receivablefee, String cwb) {
		return jdbcTemplate.update("update express_ops_order_flow set caramount =" + receivablefee + " where cwb='" + cwb + "' ");
	}

	public long updateMack(String cwb, String marksflagtime, String marksflagmen, long marksflag) {
		return jdbcTemplate.update("update express_ops_cwb_detail set marksflag =" + marksflag + ",marksflagtime='" + marksflagtime + "',marksflagmen='" + marksflagmen
				+ "'  where state=1 and ( cwb='" + cwb + "' or transcwb ='" + cwb + "' )");
	}

	public long updateSignin(String cwb, String editsignintime, long flowordertype) {
		return jdbcTemplate.update("update express_ops_cwb_detail set flowordertype =" + flowordertype + ",editsignintime='" + editsignintime + "' where state = 1 and ( cwb='" + cwb
				+ "' or transcwb ='" + cwb + "' )");
	}

	public long updateDelierySignin(String cwb, String editsignintime, long orderResultType) {
		return jdbcTemplate.update("update express_ops_cwb_detail set orderResultType =" + orderResultType + ",editsignintime='" + editsignintime + "' where state = 1 and ( cwb='" + cwb
				+ "' or transcwb ='" + cwb + "' )");
	}

	public long updateSigninman(String cwb, String signinman, long flowordertype) {
		return jdbcTemplate.update("update express_ops_cwb_detail set flowordertype =" + flowordertype + ",signinman='" + signinman + "' where state = 1 and ( cwb='" + cwb + "' or transcwb ='" + cwb
				+ "' )");
	}

	public long updateDeliverySigninman(String cwb, String signinman, long orderResultType) {
		return jdbcTemplate.update("update express_ops_cwb_detail set orderResultType =" + orderResultType + ",signinman='" + signinman + "' where state = 1 and ( cwb='" + cwb + "' or transcwb ='"
				+ cwb + "' )");
	}

	public void delCwbOrderByCwbs(String ordercwb) {
		String sql = "update express_ops_cwb_detail set state=0 ";
		sql = this.delCwbOrderByPageWhereSql(sql, ordercwb);
		jdbcTemplate.update(sql);
	}

	public CwbOrder getCwbOrderByOpscwbid(long opscwbid) {
		return jdbcTemplate.queryForObject("select * from express_ops_cwb_detail where state= 1 and opscwbid=?", new CwbMapper(), opscwbid);
	}

	/**
	 * lansheng
	 * 
	 * @param opscwbid
	 * @return
	 */
	public List<CwbOrder> getCwbOrderBycwbid(String cwbid) {
		return jdbcTemplate.query("select * from express_ops_cwb_detail where state =1 and cwb='" + cwbid + "'", new CwbMapper());
	}

	public List<CwbOrder> getCwbOrderByEmaildate(String emaildate, long flowordertype) {
		return jdbcTemplate.query("select * from express_ops_cwb_detail where state =1 and emaildate=? and flowordertype=?", new CwbMapper(), emaildate, flowordertype);
	}

	private String getWhereSql(String sql, String consigneename, String consigneemobile, String consigneephone, String consigneeaddress, String marksflagmen, long marksflag) {
		StringBuffer w = new StringBuffer();
		sql += " where ";
		if (consigneename.length() > 0 || consigneemobile.length() > 0 || consigneephone.length() > 0 || consigneeaddress.length() > 0) {
			if (consigneename.length() > 0) {
				w.append(" and consigneename='" + consigneename + "'");
			}
			if (consigneemobile.length() > 0) {
				w.append(" and consigneemobile='" + consigneemobile + "'");
			}
			if (consigneephone.length() > 0) {
				w.append(" and consigneephone='" + consigneephone + "'");
			}
			if (consigneeaddress.length() > 0) {
				w.append(" and consigneeaddress like '%" + consigneeaddress + "%'");
			}
		} else if (marksflag > 0) {
			w.append(" and marksflag=" + marksflag);
			w.append(" and marksflagmen='" + marksflagmen + "'");
		} else {
			w.append(" and marksflag=1");
		}
		sql += w.substring(4, w.length()) + " and state =1 ";
		return sql;
	}

	public List<CwbOrder> getAllCwbOrderByMarksflag(long page, String consigneename, String consigneemobile, String consigneephone, String consigneeaddress, String marksflagmen, String ordername,
			long marksflag) {
		String sql = "select * from express_ops_cwb_detail ";
		sql = getWhereSql(sql, consigneename, consigneemobile, consigneephone, consigneeaddress, marksflagmen, marksflag);
		sql += "  ORDER BY " + ordername + " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		return jdbcTemplate.query(sql, new CwbMapper());
	}

	public long getAllCwbOrderByMarksflagCount(String cwb, String consigneemobile, String consigneephone, String consigneeaddress, String marksflagmen, long marksflag) {
		String sql = "select count(1) from express_ops_cwb_detail ";
		sql = getWhereSql(sql, cwb, consigneemobile, consigneephone, consigneeaddress, marksflagmen, marksflag);
		return jdbcTemplate.queryForLong(sql);
	}

	public void saveCwbOrder(final CwbOrder cwborder) {

		jdbcTemplate.update("update express_ops_cwb_detail set consigneeno=?,consigneename=?,consigneeaddress=?,consigneepostcode=?,consigneephone=? where state =1 and  opscwbid =?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						// TODO Auto-generated method stub
						ps.setString(1, cwborder.getConsigneeno());
						ps.setString(2, cwborder.getConsigneename());
						ps.setString(3, cwborder.getConsigneeaddress());
						ps.setString(4, cwborder.getConsigneepostcode());
						ps.setString(5, cwborder.getConsigneephone());
						ps.setLong(6, cwborder.getOpscwbid());
					}
				});

	}

	public CwbOrder getCwbSimpleByCwb(String cwb) {
		try {
			return jdbcTemplate.queryForObject("SELECT cwb,startbranchid,nextbranchid,exceldeliver,deliverid,excelbranch,flowordertype " + "from express_ops_cwb_detail where cwb=? and state=1",
					new CwbSimpleMapper(), cwb);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<CwbOrder> getCwbDetailByEmailDate(long userid) {
		return jdbcTemplate.query("SELECT * FROM express_ops_cwb_detail WHERE state =1 and  emaildate IN(" + "SELECT emaildatetime FROM express_ops_emaildate WHERE userid='" + userid + "')",
				new CwbMapper());
	}

	public long getCwbDetailConutByEmailDate(long userid) {
		String sql = "SELECT COUNT(1) FROM express_ops_cwb_detail WHERE state =1 and  emaildate";
		return jdbcTemplate.queryForLong(sql);
	}

	public void saveCwbBySorting(long nextbranchid, String excelbranch, String cwb, long outWarehouseGroupId) {
		String sql = "update express_ops_cwb_detail set outwarehousegroupid=" + outWarehouseGroupId + ",nextbranchid=" + nextbranchid + ",excelbranch='" + excelbranch + "' where state =1 and cwb='"
				+ cwb + "'";
		jdbcTemplate.update(sql);

	}

	public List<CwbOrder> getCwbOrderByCWB(String cwb) {
		return jdbcTemplate.query("select * from express_ops_cwb_detail where cwb=?", new CwbMapper(), cwb);
	}

	public CwbOrder getCwbBySWBAndDeliverid(String cwb, long deliverid) {
		String sql = "select * from express_ops_cwb_detail where state =1 and cwb='" + cwb + "' and deliverid=" + deliverid;
		return jdbcTemplate.queryForObject(sql, new CwbMapper());
	}

	public List<CwbOrder> getCwbOrderByOutWarehouseGroupId(long outwarehousegroupid, long flowordertype) {
		return jdbcTemplate.query("select * from express_ops_cwb_detail where state =1 and outwarehousegroupid =? and flowordertype=?", new CwbMapper(), outwarehousegroupid, flowordertype);
	}

	public void saveByOutwarehousegroupid(int emailfinishflag, long outwarehousegroupid) {
		String sql = "update express_ops_cwb_detail set emailfinishflag=? where state =1 and outwarehousegroupid=?";
		jdbcTemplate.update(sql, emailfinishflag, outwarehousegroupid);
	}

	public List<CwbOrder> getCwbByBranchid(long branchid) {
		String sql = "select * from express_ops_cwb_detail where state =1 and branchid=?";
		return jdbcTemplate.query(sql, new CwbMapper(), branchid);

	}

	public List<CwbOrder> getCwbByOutwarehousegroupid(long outwarehousegroupid) {
		String sql = "select * from express_ops_cwb_detail where state =1 and branchgroupid=?";
		return jdbcTemplate.query(sql, new CwbMapper(), outwarehousegroupid);
	}

	public long getCwbByOutwarehousegroupidToPage(long driverid) {
		String sql = "SELECT COUNT(1) FROM express_ops_cwb_detail where state =1 and  outwarehousegroupid=?";
		return jdbcTemplate.queryForLong(sql, driverid);
	}

	public void getAllCwbForStoreRoom(long startbranchid, String emailfinishflag, String flowordertype) {
		String sql = "insert into express_ops_stock_detail(cwb,branchid,orderflowid) select cwb,startbranchid,orderflowid from express_ops_cwb_detail where startbranchid=" + startbranchid
				+ " and emailfinishflag in(" + emailfinishflag + ")" + "and flowordertype in(" + flowordertype + ")";
		jdbcTemplate.update(sql);
	}
}
