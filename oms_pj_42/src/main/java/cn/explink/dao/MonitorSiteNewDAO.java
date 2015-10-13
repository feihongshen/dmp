package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import cn.explink.controller.EmaildateTDO;
import cn.explink.controller.MonitorDTO;
import cn.explink.domain.CwbOrder;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Component
public class MonitorSiteNewDAO {

	private final class MonMapper implements RowMapper<MonitorDTO> {
		@Override
		public MonitorDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			MonitorDTO monDto = new MonitorDTO();
			monDto.setCountsum(rs.getLong("countsum"));
			monDto.setCaramountsum(rs.getBigDecimal("caramountsum"));
			monDto.setPaybackfee(rs.getBigDecimal("paybackfee"));
			return monDto;
		}
	}

	private final class MonitorIncludePosMapper implements RowMapper<MonitorDTO> {
		@Override
		public MonitorDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			MonitorDTO monDto = new MonitorDTO();
			monDto.setCountsum(rs.getLong("countsum"));
			monDto.setCaramountsum(rs.getBigDecimal("caramountsum"));
			monDto.setCaramountpos(rs.getBigDecimal("caramountpos"));
			monDto.setPaybackfee(rs.getBigDecimal("paybackfee"));
			return monDto;
		}
	}

	private final class EmaildateMapper implements RowMapper<EmaildateTDO> {
		@Override
		public EmaildateTDO mapRow(ResultSet rs, int rowNum) throws SQLException {
			EmaildateTDO emaildate = new EmaildateTDO();
			emaildate.setEmaildate(rs.getString("emaildate"));
			emaildate.setEmaildateid(rs.getLong("emaildateid"));
			emaildate.setCustomername(rs.getString("customername"));

			return emaildate;
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
			cwbOrder.setCustomername(StringUtil.nullConvertToEmptyString(rs.getString("customername")));
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

			cwbOrder.setFdeliverid(rs.getLong("fdeliverid"));
			cwbOrder.setFdelivername(StringUtil.nullConvertToEmptyString(rs.getString("fdelivername")));
			cwbOrder.setReceivedfee(rs.getBigDecimal("receivedfee"));
			cwbOrder.setReturnedfee(rs.getBigDecimal("returnedfee"));
			cwbOrder.setBusinessfee(rs.getBigDecimal("businessfee"));
			cwbOrder.setDeliverystate(rs.getLong("deliverystate"));
			cwbOrder.setCash(rs.getBigDecimal("cash"));
			cwbOrder.setPos(rs.getBigDecimal("pos"));
			cwbOrder.setPosremark(StringUtil.nullConvertToEmptyString(rs.getString("posremark")));
			cwbOrder.setMobilepodtime(rs.getTimestamp("mobilepodtime"));
			cwbOrder.setCheckfee(rs.getBigDecimal("checkfee"));
			cwbOrder.setCheckremark(StringUtil.nullConvertToEmptyString(rs.getString("checkremark")));
			cwbOrder.setReceivedfeeuser(rs.getLong("receivedfeeuser"));
			cwbOrder.setStatisticstate(rs.getLong("statisticstate"));
			cwbOrder.setCreatetime(StringUtil.nullConvertToEmptyString(rs.getString("createtime")));
			cwbOrder.setOtherfee(rs.getBigDecimal("otherfee"));
			cwbOrder.setPodremarkid(rs.getLong("podremarkid"));
			cwbOrder.setDeliverstateremark(StringUtil.nullConvertToEmptyString(rs.getString("deliverstateremark")));
			cwbOrder.setGcaid(rs.getInt("gcaid"));
			cwbOrder.setGobackid(rs.getLong("gobackid"));
			cwbOrder.setPayupbranchid(rs.getLong("payupbranchid"));
			cwbOrder.setPayupbranchname(StringUtil.nullConvertToEmptyString(rs.getString("payupbranchname")));
			cwbOrder.setPodremarkStr(StringUtil.nullConvertToEmptyString(rs.getString("podremarkStr")));
			cwbOrder.setReceivedfeeuserName(StringUtil.nullConvertToEmptyString(rs.getString("receivedfeeuserName")));
			cwbOrder.setPayuprealname(StringUtil.nullConvertToEmptyString(rs.getString("payuprealname")));

			cwbOrder.setYoudanwuhuoBranchid(rs.getLong("youdanwuhuoBranchid"));
			cwbOrder.setYouhuowudanBranchid(rs.getLong("youhuowudanBranchid"));
			cwbOrder.setTuotouTime(rs.getLong("tuotouTime"));
			cwbOrder.setYoujieguoTime(rs.getLong("youjieguoTime"));
			cwbOrder.setLeavedreasonStr(StringUtil.nullConvertToEmptyString(rs.getString("leavedreasonStr")));
			cwbOrder.setBackreason(StringUtil.nullConvertToEmptyString(rs.getString("backreason")));
			cwbOrder.setPodremarkStr(StringUtil.nullConvertToEmptyString(rs.getString("podremarkStr")));
			cwbOrder.setDelivername(StringUtil.nullConvertToEmptyString(rs.getString("delivername")));
			cwbOrder.setExpt_code(StringUtil.nullConvertToEmptyString(rs.getString("expt_code")));
			cwbOrder.setExpt_msg(StringUtil.nullConvertToEmptyString(rs.getString("expt_msg")));
			cwbOrder.setOrderResultType(rs.getLong("orderResultType"));
			return cwbOrder;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private String getnextbranchidSql(String sql, String crateStartdate, String crateEnddate, String customerid, long branchid, String startinSitetime, String endinSitetime) {
		if (crateStartdate.length() > 0 || crateEnddate.length() > 0 || customerid.length() > 0 || branchid > 0 || startinSitetime.length() > 0 || endinSitetime.length() > 0) {
			StringBuffer w = new StringBuffer();
			if (crateStartdate.length() > 0) {
				w.append(" AND emaildate>='" + crateStartdate + "' ");
			}
			if (crateEnddate.length() > 0) {
				w.append(" AND emaildate<='" + crateEnddate + "' ");
			}
			if (startinSitetime.length() > 0) {
				w.append(" AND inSitetime>='" + startinSitetime + "'");
			}
			if (endinSitetime.length() > 0) {
				w.append(" AND inSitetime<='" + endinSitetime + "'");
			}
			if (customerid.length() > 0) {
				w.append(" AND customerid IN(" + customerid + ")");
			}

			if (branchid > 0) {
				w.append(" AND nextbranchid=" + branchid);
			}
			sql += w;
		}
		return sql;
	}

	private String getstartbranchidSql(String sql, String crateStartdate, String crateEnddate, String customerid, long branchid, String startinSitetime, String endinSitetime) {
		if (crateStartdate.length() > 0 || crateEnddate.length() > 0 || customerid.length() > 0 || branchid > 0 || startinSitetime.length() > 0 || endinSitetime.length() > 0) {
			StringBuffer w = new StringBuffer();
			if (crateStartdate.length() > 0) {
				w.append(" AND emaildate>='" + crateStartdate + "' ");
			}
			if (crateEnddate.length() > 0) {
				w.append(" AND emaildate<='" + crateEnddate + "' ");
			}
			if (startinSitetime.length() > 0) {
				w.append(" AND inSitetime>='" + startinSitetime + "'");
			}
			if (endinSitetime.length() > 0) {
				w.append(" AND inSitetime<='" + endinSitetime + "'");
			}
			if (customerid.length() > 0) {
				w.append(" AND customerid IN(" + customerid + ")");
			}

			if (branchid > 0) {
				w.append(" AND startbranchid=" + branchid);
			}
			sql += w;
		}
		return sql;
	}

	private String getPyupstartbranchidSql(String sql, String crateStartdate, String crateEnddate, String customerid, long branchid, String startinSitetime, String endinSitetime) {
		if (crateStartdate.length() > 0 || crateEnddate.length() > 0 || customerid.length() > 0 || branchid > 0 || startinSitetime.length() > 0 || endinSitetime.length() > 0) {
			StringBuffer w = new StringBuffer();
			if (crateStartdate.length() > 0) {
				w.append(" AND a.emaildate>='" + crateStartdate + "' ");
			}
			if (crateEnddate.length() > 0) {
				w.append(" AND a.emaildate<='" + crateEnddate + "' ");
			}
			if (startinSitetime.length() > 0) {
				w.append(" AND a.inSitetime>='" + startinSitetime + "'");
			}
			if (endinSitetime.length() > 0) {
				w.append(" AND a.inSitetime<='" + endinSitetime + "'");
			}
			if (customerid.length() > 0) {
				w.append(" AND a.customerid IN(" + customerid + ")");
			}
			if (branchid > 0) {
				w.append(" AND b.branchid=" + branchid);
			}
			sql += w;
		}
		return sql;
	}

	/**
	 * 常规的获取订单的统计数据，返回订单数量和金额
	 * 
	 * @param crateStartdate
	 *            节点时间 （开始）
	 * @param crateEnddate
	 *            节点时间 （结束）
	 * @param shipStartTime
	 *            发货时间 （开始）
	 * @param shipEndTime
	 *            发货时间 （结束）
	 * @param emailStartTime
	 *            邮件时间（开始）
	 * @param eamilEndTime
	 *            邮件时间（结束）
	 * @param emaildate
	 *            批次时间（字符串 如："'2012-06-12 12:23:20','2012-06-13 12:23:20'"）
	 * @param flowordertype
	 *            节点订单状态（字符串 如："2,3,4,7"）
	 * @param customerid
	 *            供货商 （字符串 如："1,2,3"）
	 * @param isnow
	 *            是否当前状态
	 * @return 算法：订单在某段时间内 在各种状态下的统计 比如：批次号为“2012-06-12 12:23:20”
	 *         ,供货商id为"1",统计最近7天的入库数量和金额
	 */
	// 应到站总量
	public MonitorDTO getSite(String crateStartdate, String crateEnddate, String customerid, long branchid, String startinSitetime, String endinSitetime) {
		String sql = "SELECT SUM(receivablefee) AS caramountsum,SUM(paybackfee) AS paybackfee,COUNT(1) countsum FROM  express_ops_cwb_detail where state=1 ";
		sql = this.getnextbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, "", "");
		return jdbcTemplate.queryForObject(sql, new MonMapper());
	}

	// 应到站总量List
	public List<CwbOrder> getSiteList(String crateStartdate, String crateEnddate, String customerid, long branchid, long page, String startinSitetime, String endinSitetime) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 ";
		sql = this.getnextbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, "", "");
		if (page > 0) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}
		return jdbcTemplate.query(sql, new CwbMapper());
	}

	// 应到站总量List
	public String getSiteSql(String crateStartdate, String crateEnddate, String customerid, long branchid, long page, String startinSitetime, String endinSitetime) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 ";
		sql = this.getnextbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, "", "");
		return sql;
	}

	// 应到站总量条数
	public long getSiteCount(String crateStartdate, String crateEnddate, String customerid, long branchid, String startinSitetime, String endinSitetime) {
		String sql = "SELECT count(1) FROM  express_ops_cwb_detail where state=1 ";
		sql = this.getnextbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, "", "");
		return jdbcTemplate.queryForLong(sql);
	}

	// 未到货
	public MonitorDTO getWeidaohuo(String crateStartdate, String crateEnddate, String customerid, long branchid, String startinSitetime, String endinSitetime) {
		String sql = "SELECT SUM(receivablefee) AS caramountsum,SUM(paybackfee) AS paybackfee,COUNT(1) countsum FROM  express_ops_cwb_detail where state=1 and flowordertype in(6,14,17) ";
		sql = this.getnextbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, "", "");
		return jdbcTemplate.queryForObject(sql, new MonMapper());
	}

	// 未到货List
	public List<CwbOrder> getWeidaohuoList(String crateStartdate, String crateEnddate, String customerid, long branchid, long page, String startinSitetime, String endinSitetime) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype in(6,14,17)";
		sql = this.getnextbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, "", "");
		if (page > 0) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}
		return jdbcTemplate.query(sql, new CwbMapper());
	}

	// 未到货List
	public String getWeidaohuoSql(String crateStartdate, String crateEnddate, String customerid, long branchid, long page, String startinSitetime, String endinSitetime) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype in(6,14,17)";
		sql = this.getnextbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, "", "");
		return sql;
	}

	// 未到货条数
	public long getWeidaohuoCount(String crateStartdate, String crateEnddate, String customerid, long branchid, String startinSitetime, String endinSitetime) {
		String sql = "SELECT count(1) FROM  express_ops_cwb_detail where state=1 and flowordertype in(6,14,17) ";
		sql = this.getnextbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, "", "");
		return jdbcTemplate.queryForLong(sql);
	}

	// 入库未领
	public MonitorDTO getRukuweiling(String crateStartdate, String crateEnddate, String customerid, long branchid, String startinSitetime, String endinSitetime) {
		String sql = "SELECT SUM(receivablefee) AS caramountsum,SUM(paybackfee) AS paybackfee,COUNT(1) countsum FROM  express_ops_cwb_detail where state=1 and flowordertype = 7 ";
		sql = this.getstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		return jdbcTemplate.queryForObject(sql, new MonMapper());
	}

	// 入库未领List
	public List<CwbOrder> getRukuweilingList(String crateStartdate, String crateEnddate, String customerid, long branchid, long page, String startinSitetime, String endinSitetime) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype = 7  ";
		sql = this.getstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		if (page > 0) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}
		return jdbcTemplate.query(sql, new CwbMapper());
	}

	// 入库未领List
	public String getRukuweilingSql(String crateStartdate, String crateEnddate, String customerid, long branchid, long page, String startinSitetime, String endinSitetime) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype = 7  ";
		sql = this.getstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		return sql;
	}

	// 入库未领条数
	public long getRukuweilingCount(String crateStartdate, String crateEnddate, String customerid, long branchid, String startinSitetime, String endinSitetime) {
		String sql = "SELECT count(1) FROM  express_ops_cwb_detail where state=1 and flowordertype = 7  ";
		sql = this.getstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		return jdbcTemplate.queryForLong(sql);
	}

	// 有货无单
	public MonitorDTO getYouhuowudan(String crateStartdate, String crateEnddate, String customerid, long branchid, String startinSitetime, String endinSitetime) {
		String sql = "SELECT SUM(receivablefee) AS caramountsum,SUM(paybackfee) AS paybackfee,COUNT(1) countsum FROM  express_ops_cwb_detail where state=1 and flowordertype in(7,8) and youhuowudanBranchid="
				+ branchid + " ";
		sql = this.getstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		return jdbcTemplate.queryForObject(sql, new MonMapper());
	}

	// 有货无单List
	public List<CwbOrder> getYouhuowudanList(String crateStartdate, String crateEnddate, String customerid, long branchid, long page, String startinSitetime, String endinSitetime) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype in(7,8) and youhuowudanBranchid=" + branchid + " ";
		sql = this.getstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		if (page > 0) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}
		return jdbcTemplate.query(sql, new CwbMapper());
	}

	// 有货无单List
	public String getYouhuowudanSql(String crateStartdate, String crateEnddate, String customerid, long branchid, long page, String startinSitetime, String endinSitetime) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype in(7,8) and youhuowudanBranchid=" + branchid + " ";
		sql = this.getstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		return sql;
	}

	// 有货无单总量条数
	public long getYouhuowudanCount(String crateStartdate, String crateEnddate, String customerid, long branchid, String startinSitetime, String endinSitetime) {
		String sql = "SELECT count(1) FROM  express_ops_cwb_detail where state=1 and flowordertype in(7,8) and youhuowudanBranchid=" + branchid + " ";
		sql = this.getstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		return jdbcTemplate.queryForLong(sql);
	}

	// 有单无货
	public MonitorDTO getYoudanwuhuo(String crateStartdate, String crateEnddate, String customerid, long branchid, String startinSitetime, String endinSitetime) {
		String sql = "SELECT SUM(receivablefee) AS caramountsum,SUM(paybackfee) AS paybackfee,COUNT(1) countsum FROM  express_ops_cwb_detail where state=1 and flowordertype in(7,8) and youdanwuhuoBranchid="
				+ branchid + " ";
		sql = this.getnextbranchidSql(sql, crateStartdate, crateEnddate, customerid, 0, startinSitetime, endinSitetime);
		return jdbcTemplate.queryForObject(sql, new MonMapper());
	}

	// 有单无货List
	public List<CwbOrder> getYoudanwuhuoList(String crateStartdate, String crateEnddate, String customerid, long branchid, long page, String startinSitetime, String endinSitetime) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype in(7,8) and youdanwuhuoBranchid=" + branchid + " ";
		sql = this.getnextbranchidSql(sql, crateStartdate, crateEnddate, customerid, 0, startinSitetime, endinSitetime);
		if (page > 0) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}
		return jdbcTemplate.query(sql, new CwbMapper());
	}

	// 有单无货List
	public String getYoudanwuhuoSql(String crateStartdate, String crateEnddate, String customerid, long branchid, long page, String startinSitetime, String endinSitetime) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype in(7,8) and youdanwuhuoBranchid=" + branchid + " ";
		sql = this.getnextbranchidSql(sql, crateStartdate, crateEnddate, customerid, 0, startinSitetime, endinSitetime);
		return sql;
	}

	// 有单无货总量条数
	public long getYoudanwuhuoCount(String crateStartdate, String crateEnddate, String customerid, long branchid, String startinSitetime, String endinSitetime) {
		String sql = "SELECT count(1) FROM  express_ops_cwb_detail where state=1 and flowordertype in(7,8) and youdanwuhuoBranchid=" + branchid + " ";
		sql = this.getnextbranchidSql(sql, crateStartdate, crateEnddate, customerid, 0, startinSitetime, endinSitetime);
		return jdbcTemplate.queryForLong(sql);
	}

	// 异常单
	public MonitorDTO getYichangdan(String crateStartdate, String crateEnddate, String customerid, long branchid, String startinSitetime, String endinSitetime) {
		String sql = "SELECT SUM(receivablefee) AS caramountsum,SUM(paybackfee) AS paybackfee,COUNT(1) countsum FROM  express_ops_cwb_detail where state=1 and flowordertype =30 ";
		sql = this.getnextbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		return jdbcTemplate.queryForObject(sql, new MonMapper());
	}

	// 异常单List
	public List<CwbOrder> getYichangdanList(String crateStartdate, String crateEnddate, String customerid, long branchid, long page, String startinSitetime, String endinSitetime) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype=30 ";
		sql = this.getnextbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		if (page > 0) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}
		return jdbcTemplate.query(sql, new CwbMapper());
	}

	// 异常单List
	public String getYichangdanSql(String crateStartdate, String crateEnddate, String customerid, long branchid, long page, String startinSitetime, String endinSitetime) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype=30 ";
		sql = this.getnextbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		return sql;
	}

	// 异常单条数
	public long getYichangdanCount(String crateStartdate, String crateEnddate, String customerid, long branchid, String startinSitetime, String endinSitetime) {
		String sql = "SELECT count(1) FROM  express_ops_cwb_detail where state=1 and flowordertype=30  ";
		sql = this.getnextbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		return jdbcTemplate.queryForLong(sql);
	}

	// 已领货
	public MonitorDTO getYilinghuo(String crateStartdate, String crateEnddate, String customerid, long branchid, String startinSitetime, String endinSitetime) {
		String sql = "SELECT SUM(receivablefee) AS caramountsum,SUM(paybackfee) AS paybackfee,COUNT(1) countsum FROM  express_ops_cwb_detail where state=1 and flowordertype =9 ";
		sql = this.getstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		return jdbcTemplate.queryForObject(sql, new MonMapper());
	}

	// 已领货List
	public List<CwbOrder> getYilinghuoList(String crateStartdate, String crateEnddate, String customerid, long branchid, long page, String startinSitetime, String endinSitetime) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype=9 ";
		sql = this.getstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		if (page > 0) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}
		return jdbcTemplate.query(sql, new CwbMapper());
	}

	// 已领货List
	public String getYilinghuoSql(String crateStartdate, String crateEnddate, String customerid, long branchid, long page, String startinSitetime, String endinSitetime) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype=9 ";
		sql = this.getstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		return sql;
	}

	// 已领货条数
	public long getYilinghuoCount(String crateStartdate, String crateEnddate, String customerid, long branchid, String startinSitetime, String endinSitetime) {
		String sql = "SELECT count(1) FROM  express_ops_cwb_detail where state=1 and flowordertype=9 ";
		sql = this.getstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		return jdbcTemplate.queryForLong(sql);
	}

	// 遗留库存
	public MonitorDTO getYiliudan(String crateStartdate, String crateEnddate, String customerid, long branchid, String startinSitetime, String endinSitetime) {
		String sql = "SELECT SUM(receivablefee) AS caramountsum,SUM(paybackfee) AS paybackfee,COUNT(1) countsum FROM  express_ops_cwb_detail where state=1 and flowordertype=23 ";
		sql = this.getstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		return jdbcTemplate.queryForObject(sql, new MonMapper());
	}

	// 遗留库存List
	public List<CwbOrder> getYiliudanList(String crateStartdate, String crateEnddate, String customerid, long branchid, long page, String startinSitetime, String endinSitetime) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype=23 ";
		sql = this.getstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		if (page > 0) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}
		return jdbcTemplate.query(sql, new CwbMapper());
	}

	// 遗留库存List
	public String getYiliudanSql(String crateStartdate, String crateEnddate, String customerid, long branchid, long page, String startinSitetime, String endinSitetime) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype=23 ";
		sql = this.getstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		return sql;
	}

	// 遗留库存条数
	public long getYiliudanCount(String crateStartdate, String crateEnddate, String customerid, long branchid, String startinSitetime, String endinSitetime) {
		String sql = "SELECT count(1) FROM  express_ops_cwb_detail where state=1 and flowordertype=23 ";
		sql = this.getstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		return jdbcTemplate.queryForLong(sql);
	}

	// 退货库存
	public MonitorDTO getTuihuokuncun(String crateStartdate, String crateEnddate, String customerid, long branchid, String startinSitetime, String endinSitetime) {
		String sql = "SELECT SUM(receivablefee) AS caramountsum,SUM(paybackfee) AS paybackfee,COUNT(1) countsum FROM  express_ops_cwb_detail where state=1 and flowordertype in (15,16,21) ";
		sql = this.getstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		return jdbcTemplate.queryForObject(sql, new MonMapper());
	}

	// 退货库存List
	public List<CwbOrder> getTuihuokuncunList(String crateStartdate, String crateEnddate, String customerid, long branchid, long page, String startinSitetime, String endinSitetime) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype in (15,16,21) ";
		sql = this.getstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		if (page > 0) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}
		return jdbcTemplate.query(sql, new CwbMapper());
	}

	// 退货库存List
	public String getTuihuokuncunSql(String crateStartdate, String crateEnddate, String customerid, long branchid, long page, String startinSitetime, String endinSitetime) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype in (15,16,21) ";
		sql = this.getstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		return sql;
	}

	// 退货库存条数
	public long getTuihuokuncunCount(String crateStartdate, String crateEnddate, String customerid, long branchid, String startinSitetime, String endinSitetime) {
		String sql = "SELECT count(1) FROM  express_ops_cwb_detail where state=1 and flowordertype in (15,16,21) ";
		sql = this.getstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		return jdbcTemplate.queryForLong(sql);
	}

	// 滞留库存
	public MonitorDTO getZhiliukucun(String crateStartdate, String crateEnddate, String customerid, long branchid, String startinSitetime, String endinSitetime) {
		String sql = "SELECT SUM(receivablefee) AS caramountsum,SUM(paybackfee) AS paybackfee,COUNT(1) countsum FROM  express_ops_cwb_detail where state=1 and flowordertype=23 ";
		sql = this.getstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		return jdbcTemplate.queryForObject(sql, new MonMapper());
	}

	// 滞留库存List
	public List<CwbOrder> getZhiliukucunList(String crateStartdate, String crateEnddate, String customerid, long branchid, long page, String startinSitetime, String endinSitetime) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype=23 ";
		sql = this.getstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		if (page > 0) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}
		return jdbcTemplate.query(sql, new CwbMapper());
	}

	// 滞留库存List
	public String getZhiliukucunSql(String crateStartdate, String crateEnddate, String customerid, long branchid, long page, String startinSitetime, String endinSitetime) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype=23 ";
		sql = this.getstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		return sql;
	}

	// 滞留库存条数
	public long getZhiliukucunCount(String crateStartdate, String crateEnddate, String customerid, long branchid, String startinSitetime, String endinSitetime) {
		String sql = "SELECT count(1) FROM  express_ops_cwb_detail where state=1 and flowordertype=23 ";
		sql = this.getstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		return jdbcTemplate.queryForLong(sql);
	}

	// 中转在途
	public MonitorDTO getZhongzhuanzaitu(String crateStartdate, String crateEnddate, String customerid, long branchid, String startinSitetime, String endinSitetime) {
		String sql = "SELECT SUM(receivablefee) AS caramountsum,SUM(paybackfee) AS paybackfee,COUNT(1) countsum FROM  express_ops_cwb_detail where state=1 and flowordertype in (10,14) ";
		sql = this.getstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		return jdbcTemplate.queryForObject(sql, new MonMapper());
	}

	// 中转在途List
	public List<CwbOrder> getZhongzhuanzaituList(String crateStartdate, String crateEnddate, String customerid, long branchid, long page, String startinSitetime, String endinSitetime) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype in (10,14) ";
		sql = this.getstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		if (page > 0) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}
		return jdbcTemplate.query(sql, new CwbMapper());
	}

	// 中转在途List
	public String getZhongzhuanzaituSql(String crateStartdate, String crateEnddate, String customerid, long branchid, long page, String startinSitetime, String endinSitetime) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype in (10,14) ";
		sql = this.getstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		return sql;
	}

	// 中转在途条数
	public long getZhongzhuanzaituCount(String crateStartdate, String crateEnddate, String customerid, long branchid, String startinSitetime, String endinSitetime) {
		String sql = "SELECT count(1) FROM  express_ops_cwb_detail where state=1 and flowordertype in (10,14) ";
		sql = this.getstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		return jdbcTemplate.queryForLong(sql);
	}

	// 退货在途
	public MonitorDTO getTuihuozaitu(String crateStartdate, String crateEnddate, String customerid, long branchid, String startinSitetime, String endinSitetime) {
		String sql = "SELECT SUM(receivablefee) AS caramountsum,SUM(paybackfee) AS paybackfee,COUNT(1) countsum FROM  express_ops_cwb_detail where state=1 and flowordertype in (11,27) ";
		sql = this.getstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		return jdbcTemplate.queryForObject(sql, new MonMapper());
	}

	// 退货在途List
	public List<CwbOrder> getTuihuozaituList(String crateStartdate, String crateEnddate, String customerid, long branchid, long page, String startinSitetime, String endinSitetime) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype in (11,27) ";
		sql = this.getstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		if (page > 0) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}
		return jdbcTemplate.query(sql, new CwbMapper());
	}

	// 退货在途List
	public String getTuihuozaituSql(String crateStartdate, String crateEnddate, String customerid, long branchid, long page, String startinSitetime, String endinSitetime) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype in (11,27) ";
		sql = this.getstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		return sql;
	}

	// 退货在途条数
	public long getTuihuozaituCount(String crateStartdate, String crateEnddate, String customerid, long branchid, String startinSitetime, String endinSitetime) {
		String sql = "SELECT count(1) FROM  express_ops_cwb_detail where state=1 and flowordertype in (11,27) ";
		sql = this.getstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		return jdbcTemplate.queryForLong(sql);
	}

	// 未交款
	public MonitorDTO getWeijiaokuan(String crateStartdate, String crateEnddate, String customerid, long branchid, String startinSitetime, String endinSitetime) {
		String sql = "SELECT SUM(receivedfee-pos) AS caramountsum,SUM(pos) AS caramountpos,SUM(paybackfee) AS paybackfee, COUNT(1) countsum FROM express_ops_cwb_detail AS a "
				+ "LEFT JOIN express_ops_goto_class_auditing AS b ON  a.gcaid=b.classid WHERE b.payupid=0 and a.state=1 and a.orderResultType in (18,19,20,22,25) ";
		sql = this.getPyupstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		return jdbcTemplate.queryForObject(sql, new MonitorIncludePosMapper());
	}

	// 未交款List
	public List<CwbOrder> getWeijiaokuanList(String crateStartdate, String crateEnddate, String customerid, long branchid, long page, String startinSitetime, String endinSitetime) {
		String sql = "SELECT a.* FROM express_ops_cwb_detail AS a "
				+ "LEFT JOIN express_ops_goto_class_auditing AS b ON  a.gcaid=b.classid WHERE b.payupid=0 and a.state=1 and a.orderResultType in (18,19,20,22,25)  ";
		sql = this.getPyupstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		if (page > 0) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}
		return jdbcTemplate.query(sql, new CwbMapper());
	}

	// 未交款List
	public String getWeijiaokuanSql(String crateStartdate, String crateEnddate, String customerid, long branchid, long page, String startinSitetime, String endinSitetime) {
		String sql = "SELECT a.* FROM express_ops_cwb_detail AS a "
				+ "LEFT JOIN express_ops_goto_class_auditing AS b ON  a.gcaid=b.classid WHERE b.payupid=0 and a.state=1 and a.orderResultType in (18,19,20,22,25)  ";
		sql = this.getPyupstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		return sql;
	}

	// 未交款条数
	public long getWeijiaokuanCount(String crateStartdate, String crateEnddate, String customerid, long branchid, String startinSitetime, String endinSitetime) {
		String sql = "SELECT count(1) FROM express_ops_cwb_detail AS a "
				+ "LEFT JOIN express_ops_goto_class_auditing AS b ON  a.gcaid=b.classid WHERE b.payupid=0  and a.state=1 and a.orderResultType in (18,19,20,22,25)  ";
		sql = this.getPyupstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		return jdbcTemplate.queryForLong(sql);
	}

	// 已交款
	public MonitorDTO getQiankuan(String crateStartdate, String crateEnddate, String customerid, long branchid, String startinSitetime, String endinSitetime) {
		String sql = "SELECT SUM(receivedfee-pos) AS caramountsum,SUM(pos) AS caramountpos,SUM(paybackfee) AS paybackfee,COUNT(1) countsum FROM express_ops_cwb_detail AS a "
				+ "LEFT JOIN express_ops_goto_class_auditing AS b ON  a.gcaid=b.classid WHERE b.payupid>0  and a.state=1 and a.orderResultType in (18,19,20,22,25)  ";
		sql = this.getPyupstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		return jdbcTemplate.queryForObject(sql, new MonitorIncludePosMapper());
	}

	// 已交款List
	public List<CwbOrder> getQiankuanList(String crateStartdate, String crateEnddate, String customerid, long branchid, long page, String startinSitetime, String endinSitetime) {
		String sql = "SELECT a.* FROM express_ops_cwb_detail AS a "
				+ "LEFT JOIN express_ops_goto_class_auditing AS b ON  a.gcaid=b.classid WHERE b.payupid>0  and a.state=1 and a.orderResultType in (18,19,20,22,25)  ";
		sql = this.getPyupstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		if (page > 0) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}
		return jdbcTemplate.query(sql, new CwbMapper());
	}

	// 已交款List
	public String getQiankuanSql(String crateStartdate, String crateEnddate, String customerid, long branchid, long page, String startinSitetime, String endinSitetime) {
		String sql = "SELECT a.* FROM express_ops_cwb_detail AS a "
				+ "LEFT JOIN express_ops_goto_class_auditing AS b ON  a.gcaid=b.classid WHERE b.payupid>0  and a.state=1 and a.orderResultType in (18,19,20,22,25)  ";
		sql = this.getPyupstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		return sql;
	}

	// 已交款条数
	public long getQiankuanCount(String crateStartdate, String crateEnddate, String customerid, long branchid, String startinSitetime, String endinSitetime) {
		String sql = "SELECT count(1) FROM express_ops_cwb_detail AS a "
				+ "LEFT JOIN express_ops_goto_class_auditing AS b ON  a.gcaid=b.classid WHERE b.payupid>0  and a.state=1 and a.orderResultType in (18,19,20,22,25)  ";
		sql = this.getPyupstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		return jdbcTemplate.queryForLong(sql);
	}

	// 妥投
	public MonitorDTO getTuotou(String crateStartdate, String crateEnddate, String customerid, long branchid, String startinSitetime, String endinSitetime) {
		String sql = "SELECT SUM(receivablefee) AS caramountsum,SUM(paybackfee) AS paybackfee,COUNT(1) countsum FROM  express_ops_cwb_detail where state=1 and orderResultType in (18,19,20,22)  ";
		sql = this.getstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		return jdbcTemplate.queryForObject(sql, new MonMapper());
	}

	// 妥投List
	public List<CwbOrder> getTuotouList(String crateStartdate, String crateEnddate, String customerid, long branchid, long page, String startinSitetime, String endinSitetime) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and orderResultType in (18,19,20,22)  ";
		sql = this.getstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		if (page > 0) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}
		return jdbcTemplate.query(sql, new CwbMapper());
	}

	// 妥投List
	public String getTuotouSql(String crateStartdate, String crateEnddate, String customerid, long branchid, long page, String startinSitetime, String endinSitetime) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and orderResultType in (18,19,20,22)  ";
		sql = this.getstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		return sql;
	}

	// 妥投条数
	public long getTuotouCount(String crateStartdate, String crateEnddate, String customerid, long branchid, String startinSitetime, String endinSitetime) {
		String sql = "SELECT count(1) FROM  express_ops_cwb_detail where state=1 and orderResultType in (18,19,20,22)   ";
		sql = this.getstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		return jdbcTemplate.queryForLong(sql);
	}

	// 未妥投
	public MonitorDTO getWeituotou(String crateStartdate, String crateEnddate, String customerid, long branchid, String startinSitetime, String endinSitetime) {
		String sql = "SELECT SUM(receivablefee) AS caramountsum,SUM(paybackfee) AS paybackfee,COUNT(1) countsum FROM  express_ops_cwb_detail where state=1 and orderResultType in(21,23,24,25)  ";
		sql = this.getstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		return jdbcTemplate.queryForObject(sql, new MonMapper());
	}

	// 未妥投List
	public List<CwbOrder> getWeituotouList(String crateStartdate, String crateEnddate, String customerid, long branchid, long page, String startinSitetime, String endinSitetime) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and orderResultType in(21,23,24,25)  ";
		sql = this.getstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		if (page > 0) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}
		return jdbcTemplate.query(sql, new CwbMapper());
	}

	// 未妥投List
	public String getWeituotouSql(String crateStartdate, String crateEnddate, String customerid, long branchid, long page, String startinSitetime, String endinSitetime) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and orderResultType in(21,23,24,25)  ";
		sql = this.getstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		return sql;
	}

	// 未妥投条数
	public long getWeituotouCount(String crateStartdate, String crateEnddate, String customerid, long branchid, String startinSitetime, String endinSitetime) {
		String sql = "SELECT count(1) FROM  express_ops_cwb_detail where state=1 and orderResultType in(21,23,24,25)  ";
		sql = this.getstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		return jdbcTemplate.queryForLong(sql);
	}

	// 其他
	public MonitorDTO getQita(String crateStartdate, String crateEnddate, String customerid, long branchid, String startinSitetime, String endinSitetime) {
		String sql = "SELECT SUM(receivablefee) AS caramountsum,SUM(paybackfee) AS paybackfee,COUNT(1) countsum FROM  express_ops_cwb_detail where state=1 and flowordertype in(27,28,29)  ";
		sql = this.getstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		return jdbcTemplate.queryForObject(sql, new MonMapper());
	}

	// 其他List
	public List<CwbOrder> getQitaList(String crateStartdate, String crateEnddate, String customerid, long branchid, long page, String startinSitetime, String endinSitetime) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype in(27,28,29) ";
		sql = this.getstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		if (page > 0) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}
		return jdbcTemplate.query(sql, new CwbMapper());
	}

	// 其他List
	public String getQitaSql(String crateStartdate, String crateEnddate, String customerid, long branchid, long page, String startinSitetime, String endinSitetime) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype in(27,28,29) ";
		sql = this.getstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		return sql;
	}

	// 其他条数
	public long getQitaCount(String crateStartdate, String crateEnddate, String customerid, long branchid, String startinSitetime, String endinSitetime) {
		String sql = "SELECT count(1) FROM  express_ops_cwb_detail where state=1 and flowordertype in(27,28,29) ";
		sql = this.getstartbranchidSql(sql, crateStartdate, crateEnddate, customerid, branchid, startinSitetime, endinSitetime);
		return jdbcTemplate.queryForLong(sql);
	}

	/**
	 * 按站点查询所选时间内的所有批次号
	 * 
	 * @param startdate
	 * @param enddate
	 * @param branchid
	 * @return
	 */
	public List<EmaildateTDO> getEmailDateAndBrandId(String startdate, String enddate, long branchid) {
		String sql = "SELECT emaildate,emaildateid,customername FROM express_ops_cwb_detail WHERE emaildate >='" + startdate + "' AND emaildate <= '" + enddate
				+ "'  AND emaildate is not null AND emaildateid>0 AND state=1 GROUP BY emaildateid ORDER BY emaildate DESC";
		if (branchid > 0) {
			sql = "SELECT emaildate,emaildateid,customername FROM express_ops_cwb_detail WHERE emaildate >='" + startdate + "' AND emaildate <= '" + enddate
					+ "'  AND emaildate is not null AND emaildateid>0 AND state=1   AND branchid=" + branchid + " GROUP BY emaildateid ORDER BY emaildate DESC";
		}
		return jdbcTemplate.query(sql, new EmaildateMapper());
	}
}
