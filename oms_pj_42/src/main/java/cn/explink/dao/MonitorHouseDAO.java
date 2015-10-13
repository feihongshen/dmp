package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.jfree.data.time.Hour;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import cn.explink.controller.EmaildateTDO;
import cn.explink.controller.MonitorDTO;
import cn.explink.controller.MonitorView;
import cn.explink.domain.CwbOrder;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Component
public class MonitorHouseDAO {

	private final class MonMapper implements RowMapper<MonitorDTO> {
		@Override
		public MonitorDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			MonitorDTO monDto = new MonitorDTO();
			monDto.setCountsum(rs.getLong("countsum"));
			monDto.setCaramountsum(rs.getBigDecimal("caramountsum"));
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

	private final class MonitorViewMapper implements RowMapper<MonitorView> {
		@Override
		public MonitorView mapRow(ResultSet rs, int rowNum) throws SQLException {
			MonitorView monView = new MonitorView();
			monView.setCarwarehouse(rs.getLong("carwarehouse"));
			monView.setYingrukusum(rs.getBigDecimal("yingrukusum"));
			monView.setYingrukucount(rs.getLong("yingrukucount"));
			monView.setWeirukusum(rs.getBigDecimal("weirukusum"));
			monView.setWeirukucount(rs.getLong("weirukucount"));
			monView.setChukuzaitusum(rs.getBigDecimal("chukuzaitusum"));
			monView.setChukuzaitucount(rs.getLong("chukuzaitucount"));
			monView.setKucunsum(rs.getBigDecimal("kucunsum"));
			monView.setKucuncount(rs.getLong("kucuncount"));
			return monView;
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

	private String getrukuSql(String sql, String crateStartdate, String crateEnddate, String customerid, long branchid) {
		if (crateStartdate.length() > 0 || crateEnddate.length() > 0 || customerid.length() > 0 || branchid > 0) {
			StringBuffer w = new StringBuffer();
			if (crateStartdate.length() > 0) {
				w.append(" AND emaildate>='" + crateStartdate + "' ");
			}
			if (crateEnddate.length() > 0) {
				w.append(" AND emaildate<='" + crateEnddate + "' ");
			}
			if (customerid.length() > 0) {
				w.append(" AND customerid IN(" + customerid + ")");
			}

			if (branchid > 0) {
				w.append(" AND (carwarehouse='" + branchid + "' or targetcarwarehouse=" + branchid + ") ");
			}
			sql += w;
		}
		return sql;
	}

	private String getYoudanwuhuoSql(String sql, String crateStartdate, String crateEnddate, String customerid, long branchid, long emaildateid) {
		if (crateStartdate.length() > 0 || crateEnddate.length() > 0 || customerid.length() > 0 || branchid > 0 || emaildateid > 0) {
			StringBuffer w = new StringBuffer();
			if (crateStartdate.length() > 0) {
				w.append(" AND emaildate>='" + crateStartdate + "' ");
			}
			if (crateEnddate.length() > 0) {
				w.append(" AND emaildate<='" + crateEnddate + "' ");
			}
			if (customerid.length() > 0) {
				w.append(" AND customerid IN(" + customerid + ")");
			}

			if (branchid > 0) {
				w.append(" AND (carwarehouse='" + branchid + "' or targetcarwarehouse=" + branchid + ")");
			}

			if (emaildateid > 0) {
				w.append(" AND emaildateid =" + emaildateid);
			}

			sql += w;
		}
		return sql;
	}

	private String getYoudanwuhuoSqlList(String sql, String crateStartdate, String crateEnddate, String customeridStr, long branchid, String emaildateidStr) {
		if (crateStartdate.length() > 0 || crateEnddate.length() > 0 || customeridStr.length() > 0 || branchid > 0 || emaildateidStr.length() > 0) {
			StringBuffer w = new StringBuffer();
			if (crateStartdate.length() > 0) {
				w.append(" AND emaildate>='" + crateStartdate + "' ");
			}
			if (crateEnddate.length() > 0) {
				w.append(" AND emaildate<='" + crateEnddate + "' ");
			}
			if (customeridStr.length() > 0) {
				w.append(" AND customerid IN(" + customeridStr + ")");
			}

			if (branchid > 0) {
				w.append(" AND (carwarehouse='" + branchid + "' or targetcarwarehouse=" + branchid + ")");
			}

			if (emaildateidStr.length() > 0) {
				w.append(" AND emaildateid in(" + emaildateidStr + ")");
			} else {
				w.append(" AND 1=2");
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
	// 入库总量

	public MonitorView getMonitorView(long branchid) {
		try {
			String sql = " SELECT carwarehouse,SUM(receivablefee) AS yingrukusum,COUNT(1) AS yingrukucount,"
					+ "SUM( CASE WHEN flowordertype<4 THEN receivablefee END) AS weirukusum,COUNT( CASE WHEN flowordertype<4 THEN 1 END) AS weirukucount,"
					+ "SUM( CASE WHEN flowordertype=6 AND startbranchid =" + branchid + " THEN receivablefee END) AS chukuzaitusum,COUNT( CASE WHEN flowordertype=6 AND startbranchid =" + branchid
					+ " THEN 1 END) AS chukuzaitucount," + "SUM( CASE WHEN flowordertype=4 AND currentbranchid =" + branchid
					+ " THEN receivablefee END) AS kucunsum,COUNT( CASE WHEN flowordertype=4 AND currentbranchid =" + branchid + " THEN 1 END) AS kucuncount "
					+ "FROM express_ops_cwb_detail WHERE state=1 AND carwarehouse =" + branchid + " GROUP BY carwarehouse ";
			return jdbcTemplate.queryForObject(sql, new MonitorViewMapper());
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			return null;
		}

	}

	/*
	 * public List<MonitorDTO> getHouse(){ String sql =
	 * "SELECT SUM(receivablefee) AS caramountsum,COUNT(1)  as countsum FROM  express_ops_cwb_detail where  state=1 GROUP BY customerid	"
	 * ; //sql = this.getrukuSql(sql, crateStartdate, crateEnddate,
	 * customerid,branchid); return jdbcTemplate.query(sql, new MonMapper()); }
	 * //入库总量List public List<CwbOrder> getHouseList(String
	 * crateStartdate,String crateEnddate, String customerid,long branchid,long
	 * page){ String sql =
	 * "SELECT * FROM  express_ops_cwb_detail where state=1 "; sql =
	 * this.getrukuSql(sql, crateStartdate, crateEnddate, customerid,branchid);
	 * if(page > 0){ sql +=
	 * " limit "+(page-1)*Page.ONE_PAGE_NUMBER+" ,"+Page.ONE_PAGE_NUMBER; }
	 * return jdbcTemplate.query(sql, new CwbMapper()); } //入库总量List public
	 * String getHouseSql(String crateStartdate,String crateEnddate, String
	 * customerid,long branchid,long page){ String sql =
	 * "SELECT * FROM  express_ops_cwb_detail where state=1 "; sql =
	 * this.getrukuSql(sql, crateStartdate, crateEnddate, customerid,branchid);
	 * return sql; } //入库总量条数 public long getHouseCount(String
	 * crateStartdate,String crateEnddate, String customerid,long branchid){
	 * String sql =
	 * "SELECT count(1) FROM  express_ops_cwb_detail where state=1 "; sql =
	 * this.getrukuSql(sql, crateStartdate, crateEnddate, customerid,branchid);
	 * return jdbcTemplate.queryForLong(sql); }
	 * 
	 * //未入库 public MonitorDTO getWeiruku(String crateStartdate,String
	 * crateEnddate, String customerid,long branchid){ String sql =
	 * "SELECT SUM(receivablefee) AS caramountsum,COUNT(1) as  countsum FROM  express_ops_cwb_detail where state=1 and flowordertype < 4 "
	 * ; sql = this.getrukuSql(sql, crateStartdate, crateEnddate,
	 * customerid,branchid); return jdbcTemplate.queryForObject(sql, new
	 * MonMapper()); } //未入库List public List<CwbOrder> getWeirukuList(String
	 * crateStartdate,String crateEnddate, String customerid,long branchid,long
	 * page){ String sql =
	 * "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype < 4 "
	 * ; sql = this.getrukuSql(sql, crateStartdate, crateEnddate,
	 * customerid,branchid); if(page > 0){ sql +=
	 * " limit "+(page-1)*Page.ONE_PAGE_NUMBER+" ,"+Page.ONE_PAGE_NUMBER; }
	 * return jdbcTemplate.query(sql, new CwbMapper()); } //未入库List public
	 * String getWeirukuSql(String crateStartdate,String crateEnddate, String
	 * customerid,long branchid,long page){ String sql =
	 * "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype < 4 "
	 * ; sql = this.getrukuSql(sql, crateStartdate, crateEnddate,
	 * customerid,branchid); return sql; } //未入库总量条数 public long
	 * getWeirukuCount(String crateStartdate,String crateEnddate, String
	 * customerid,long branchid){ String sql =
	 * "SELECT count(1) FROM  express_ops_cwb_detail where state=1 and flowordertype < 4 "
	 * ; sql = this.getrukuSql(sql, crateStartdate, crateEnddate,
	 * customerid,branchid); return jdbcTemplate.queryForLong(sql); } //已入库
	 * public MonitorDTO getYiruku(String crateStartdate,String crateEnddate,
	 * String customerid,long branchid){ String sql =
	 * "SELECT SUM(receivablefee) AS caramountsum,COUNT(1) as  countsum FROM  express_ops_cwb_detail where state=1 and flowordertype >= 4 "
	 * ; sql = this.getrukuSql(sql, crateStartdate, crateEnddate,
	 * customerid,branchid); return jdbcTemplate.queryForObject(sql, new
	 * MonMapper()); } //已入库List public List<CwbOrder> getYirukuList(String
	 * crateStartdate,String crateEnddate, String customerid,long branchid,long
	 * page){ String sql =
	 * "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype >= 4  "
	 * ; sql = this.getrukuSql(sql, crateStartdate, crateEnddate,
	 * customerid,branchid); if(page > 0){ sql +=
	 * " limit "+(page-1)*Page.ONE_PAGE_NUMBER+" ,"+Page.ONE_PAGE_NUMBER; }
	 * return jdbcTemplate.query(sql, new CwbMapper()); } //已入库List public
	 * String getYirukuSql(String crateStartdate,String crateEnddate, String
	 * customerid,long branchid,long page){ String sql =
	 * "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype >= 4  "
	 * ; sql = this.getrukuSql(sql, crateStartdate, crateEnddate,
	 * customerid,branchid); return sql; } //已入库总量条数 public long
	 * getYirukuCount(String crateStartdate,String crateEnddate, String
	 * customerid,long branchid){ String sql =
	 * "SELECT count(1) FROM  express_ops_cwb_detail where state=1 and flowordertype >= 4  "
	 * ; sql = this.getrukuSql(sql, crateStartdate, crateEnddate,
	 * customerid,branchid); return jdbcTemplate.queryForLong(sql); } //有货无单
	 * public MonitorDTO getYouhuowudan(String crateStartdate,String
	 * crateEnddate, String customerid,long branchid){ String sql =
	 * "SELECT SUM(receivablefee) AS caramountsum,COUNT(1) as  countsum FROM  express_ops_cwb_detail where state=1 and flowordertype in(5) "
	 * ; sql = this.getrukuSql(sql, crateStartdate, crateEnddate,
	 * customerid,branchid); return jdbcTemplate.queryForObject(sql, new
	 * MonMapper()); } //有货无单List public List<CwbOrder>
	 * getYouhuowudanList(String crateStartdate,String crateEnddate, String
	 * customerid,long branchid,long page){ String sql =
	 * "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype in(5)  "
	 * ; sql = this.getrukuSql(sql, crateStartdate, crateEnddate,
	 * customerid,branchid); if(page > 0){ sql +=
	 * " limit "+(page-1)*Page.ONE_PAGE_NUMBER+" ,"+Page.ONE_PAGE_NUMBER; }
	 * return jdbcTemplate.query(sql, new CwbMapper()); } //有货无单List public
	 * String getYouhuowudanSql(String crateStartdate,String crateEnddate,
	 * String customerid,long branchid,long page){ String sql =
	 * "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype in(5)  "
	 * ; sql = this.getrukuSql(sql, crateStartdate, crateEnddate,
	 * customerid,branchid); return sql; } //有货无单总量条数 public long
	 * getYouhuowudanCount(String crateStartdate,String crateEnddate, String
	 * customerid,long branchid){ String sql =
	 * "SELECT count(1) FROM  express_ops_cwb_detail where state=1 and flowordertype in(5)  "
	 * ; sql = this.getrukuSql(sql, crateStartdate, crateEnddate,
	 * customerid,branchid); return jdbcTemplate.queryForLong(sql); } //有单无货
	 * public MonitorDTO getYoudanwuhuo(String crateStartdate,String
	 * crateEnddate, String customerid,long branchid,String emaildateid){ String
	 * sql =
	 * "SELECT SUM(receivablefee) AS caramountsum,COUNT(1) as  countsum FROM  express_ops_cwb_detail where  flowordertype = 1 and state=1 "
	 * ; sql = getYoudanwuhuoSqlList(sql, crateStartdate, crateEnddate,
	 * customerid,branchid,emaildateid); return jdbcTemplate.queryForObject(sql,
	 * new MonMapper()); } //有单无货List public List<CwbOrder>
	 * getYoudanwuhuoList(String crateStartdate,String crateEnddate, String
	 * customerid,long branchid,String emaildateid ,long page){ String sql =
	 * "SELECT *  FROM  express_ops_cwb_detail where flowordertype = 1 and state=1 "
	 * ; sql = getYoudanwuhuoSqlList(sql, crateStartdate, crateEnddate,
	 * customerid,branchid,emaildateid); if(page > 0){ sql +=
	 * " limit "+(page-1)*Page.ONE_PAGE_NUMBER+" ,"+Page.ONE_PAGE_NUMBER; }
	 * return jdbcTemplate.query(sql, new CwbMapper()); } //有单无货List public
	 * String getYoudanwuhuoSql(String crateStartdate,String crateEnddate,
	 * String customerid,long branchid,String emaildateid ,long page){ String
	 * sql =
	 * "SELECT *  FROM  express_ops_cwb_detail where flowordertype = 1 and state=1 "
	 * ; sql = getYoudanwuhuoSqlList(sql, crateStartdate, crateEnddate,
	 * customerid,branchid,emaildateid); return sql; } //有单无货条件满足 public long
	 * getYoudanwuhuoCountCheck(String crateStartdate,String crateEnddate,
	 * String customerid,long branchid,long emaildateid){ String sql =
	 * "SELECT  COUNT(1) AS deliverycount FROM  express_ops_cwb_detail  WHERE  "
	 * + " flowordertype >= 4 and state=1"; sql = getYoudanwuhuoSql(sql, "", "",
	 * customerid,branchid,emaildateid); return jdbcTemplate.queryForLong(sql);
	 * } //有单无货总量条数 public long getYoudanwuhuoCount(String crateStartdate,String
	 * crateEnddate, String customerid,long branchid,String emaildateid){ String
	 * sql =
	 * "SELECT count(1)  FROM  express_ops_cwb_detail where flowordertype = 1 and state=1 "
	 * ; sql = getYoudanwuhuoSqlList(sql, crateStartdate, crateEnddate,
	 * customerid,branchid,emaildateid); return jdbcTemplate.queryForLong(sql);
	 * } //出库在途 public MonitorDTO getChukuzaitu(String crateStartdate,String
	 * crateEnddate, String customerid,long branchid){ String sql =
	 * "SELECT SUM(receivablefee) AS caramountsum,COUNT(1) as  countsum FROM  express_ops_cwb_detail where state=1 and flowordertype = 6 "
	 * ; sql = this.getrukuSql(sql, crateStartdate, crateEnddate,
	 * customerid,branchid); return jdbcTemplate.queryForObject(sql, new
	 * MonMapper()); } //出库在途List public List<CwbOrder> getChukuzaituList(String
	 * crateStartdate,String crateEnddate, String customerid,long branchid,long
	 * page){ String sql =
	 * "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype = 6  "
	 * ; sql = this.getrukuSql(sql, crateStartdate, crateEnddate,
	 * customerid,branchid); if(page > 0){ sql +=
	 * " limit "+(page-1)*Page.ONE_PAGE_NUMBER+" ,"+Page.ONE_PAGE_NUMBER; }
	 * return jdbcTemplate.query(sql, new CwbMapper()); } //出库在途List public
	 * String getChukuzaituSql(String crateStartdate,String crateEnddate, String
	 * customerid,long branchid,long page){ String sql =
	 * "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype = 6  "
	 * ; sql = this.getrukuSql(sql, crateStartdate, crateEnddate,
	 * customerid,branchid); return sql; } //出库在途总量条数 public long
	 * getChukuzaituCount(String crateStartdate,String crateEnddate, String
	 * customerid,long branchid){ String sql =
	 * "SELECT count(1) FROM  express_ops_cwb_detail where state=1 and flowordertype = 6  "
	 * ; sql = this.getrukuSql(sql, crateStartdate, crateEnddate,
	 * customerid,branchid); return jdbcTemplate.queryForLong(sql); } //库存
	 * public MonitorDTO getKucun(String crateStartdate,String crateEnddate,
	 * String customerid,long branchid){ String sql =
	 * "SELECT SUM(receivablefee) AS caramountsum,COUNT(1) as  countsum FROM  express_ops_cwb_detail where state=1 and flowordertype in (4,5) "
	 * ; sql = this.getrukuSql(sql, crateStartdate, crateEnddate,
	 * customerid,branchid); return jdbcTemplate.queryForObject(sql, new
	 * MonMapper()); } //库存List public List<CwbOrder> getKucunList(String
	 * crateStartdate,String crateEnddate, String customerid,long branchid,long
	 * page){ String sql =
	 * "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype in (4,5) "
	 * ; sql = this.getrukuSql(sql, crateStartdate, crateEnddate,
	 * customerid,branchid); if(page > 0){ sql +=
	 * " limit "+(page-1)*Page.ONE_PAGE_NUMBER+" ,"+Page.ONE_PAGE_NUMBER; }
	 * return jdbcTemplate.query(sql, new CwbMapper()); } //库存List public String
	 * getKucunSql(String crateStartdate,String crateEnddate, String
	 * customerid,long branchid,long page){ String sql =
	 * "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype in (4,5) "
	 * ; sql = this.getrukuSql(sql, crateStartdate, crateEnddate,
	 * customerid,branchid); return sql; } //库存总量条数 public long
	 * getKucunCount(String crateStartdate,String crateEnddate, String
	 * customerid,long branchid){ String sql =
	 * "SELECT count(1) FROM  express_ops_cwb_detail where state=1 and flowordertype in (4,5) "
	 * ; sql = this.getrukuSql(sql, crateStartdate, crateEnddate,
	 * customerid,branchid); return jdbcTemplate.queryForLong(sql); }
	 */

	// /-------------------------------------以下是退货和中转的统计------------------------------------------

	private String getzhongzhuanSql(String sql, String crateStartdate, String crateEnddate, String customerid, long branchid) {
		if (crateStartdate.length() > 0 || crateEnddate.length() > 0 || customerid.length() > 0 || branchid > 0) {
			StringBuffer w = new StringBuffer();
			if (crateStartdate.length() > 0) {
				w.append(" AND emaildate>='" + crateStartdate + "' ");
			}
			if (crateEnddate.length() > 0) {
				w.append(" AND emaildate<='" + crateEnddate + "' ");
			}
			if (customerid.length() > 0) {
				w.append(" AND customerid IN(" + customerid + ")");
			}
			if (branchid > 0) {
				w.append(" AND zhongzhuanid =" + branchid);
			}

			sql += w;
		}
		return sql;
	}

	private String getzhongzhuanWeirukuSql(String sql, String crateStartdate, String crateEnddate, String customerid, long branchid) {
		if (crateStartdate.length() > 0 || crateEnddate.length() > 0 || customerid.length() > 0 || branchid > 0) {
			StringBuffer w = new StringBuffer();
			if (crateStartdate.length() > 0) {
				w.append(" AND emaildate>='" + crateStartdate + "' ");
			}
			if (crateEnddate.length() > 0) {
				w.append(" AND emaildate<='" + crateEnddate + "' ");
			}
			if (customerid.length() > 0) {
				w.append(" AND customerid IN(" + customerid + ")");
			}
			if (branchid > 0) {
				w.append(" AND zhongzhuanid =" + branchid);
			}
			sql += w;
		}
		return sql;
	}

	private String getzhongzhuanchukuSql(String sql, String crateStartdate, String crateEnddate, String customerid, long branchid) {
		if (crateStartdate.length() > 0 || crateEnddate.length() > 0 || customerid.length() > 0 || branchid > 0) {
			StringBuffer w = new StringBuffer();
			if (crateStartdate.length() > 0) {
				w.append(" AND emaildate>='" + crateStartdate + "' ");
			}
			if (crateEnddate.length() > 0) {
				w.append(" AND emaildate<='" + crateEnddate + "' ");
			}
			if (customerid.length() > 0) {
				w.append(" AND customerid IN(" + customerid + ")");
			}

			if (branchid > 0) {
				w.append(" AND startbranchid =" + branchid + "");
				w.append(" AND zhongzhuanid =" + branchid + "");
			}

			sql += w;
		}
		return sql;
	}

	private String gettuihuoSql(String sql, String crateStartdate, String crateEnddate, String customerid, long branchid) {
		if (crateStartdate.length() > 0 || crateEnddate.length() > 0 || customerid.length() > 0 || branchid > 0) {
			StringBuffer w = new StringBuffer();
			if (crateStartdate.length() > 0) {
				w.append(" AND emaildate>='" + crateStartdate + "' ");
			}
			if (crateEnddate.length() > 0) {
				w.append(" AND emaildate<='" + crateEnddate + "' ");
			}
			if (customerid.length() > 0) {
				w.append(" AND customerid IN(" + customerid + ")");
			}
			if (branchid > 0) {
				w.append(" AND tuihuoid =" + branchid);
			}
			sql += w;
		}
		return sql;
	}

	private String gettuihuoWeirukuSql(String sql, String crateStartdate, String crateEnddate, String customerid, long branchid) {
		if (crateStartdate.length() > 0 || crateEnddate.length() > 0 || customerid.length() > 0 || branchid > 0) {
			StringBuffer w = new StringBuffer();
			if (crateStartdate.length() > 0) {
				w.append(" AND emaildate>='" + crateStartdate + "' ");
			}
			if (crateEnddate.length() > 0) {
				w.append(" AND emaildate<='" + crateEnddate + "' ");
			}
			if (customerid.length() > 0) {
				w.append(" AND customerid IN(" + customerid + ")");
			}
			if (branchid > 0) {
				w.append(" AND tuihuoid =" + branchid);
			}
			sql += w;
		}
		return sql;
	}

	private String gettuihuochukuSql(String sql, String crateStartdate, String crateEnddate, String customerid, long branchid) {
		if (crateStartdate.length() > 0 || crateEnddate.length() > 0 || customerid.length() > 0 || branchid > 0) {
			StringBuffer w = new StringBuffer();
			if (crateStartdate.length() > 0) {
				w.append(" AND emaildate>='" + crateStartdate + "' ");
			}
			if (crateEnddate.length() > 0) {
				w.append(" AND emaildate<='" + crateEnddate + "' ");
			}
			if (customerid.length() > 0) {
				w.append(" AND customerid IN(" + customerid + ")");
			}

			if (branchid > 0) {
				w.append(" AND startbranchid =" + branchid + "");
				w.append(" AND tuihuoid =" + branchid);
			}
			sql += w;
		}
		return sql;
	}

	// 入库总量
	public MonitorDTO getzhongzhuanAll(String crateStartdate, String crateEnddate, String customerid, long branchid) {
		String sql = "SELECT SUM(receivablefee) AS caramountsum,COUNT(1) as  countsum FROM  express_ops_cwb_detail where state=1 ";
		sql = this.getzhongzhuanSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		return jdbcTemplate.queryForObject(sql, new MonMapper());
	}

	// 入库总量List
	public List<CwbOrder> getzhongzhuanAllList(String crateStartdate, String crateEnddate, String customerid, long branchid, long page) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 ";
		sql = this.getzhongzhuanSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		if (page > 0) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}
		return jdbcTemplate.query(sql, new CwbMapper());
	}

	// 入库总量List
	public String getSql(String crateStartdate, String crateEnddate, String customerid, long branchid, long page) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 ";
		sql = this.getzhongzhuanSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		return sql;
	}

	// 入库总量条数
	public long getzhongzhuanAllCount(String crateStartdate, String crateEnddate, String customerid, long branchid) {
		String sql = "SELECT count(1) FROM  express_ops_cwb_detail where state=1 ";
		sql = this.getzhongzhuanSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		return jdbcTemplate.queryForLong(sql);
	}

	// 未入库
	public MonitorDTO getzhongzhuanWeiruku(String crateStartdate, String crateEnddate, String customerid, long branchid) {
		String sql = "SELECT SUM(receivablefee) AS caramountsum,COUNT(1) as  countsum FROM  express_ops_cwb_detail where state=1 and flowordertype = 10 ";
		sql = this.getzhongzhuanWeirukuSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		return jdbcTemplate.queryForObject(sql, new MonMapper());
	}

	// 未入库List
	public List<CwbOrder> getzhongzhuanWeirukuList(String crateStartdate, String crateEnddate, String customerid, long branchid, long page) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype = 10 ";
		sql = this.getzhongzhuanWeirukuSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		if (page > 0) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}
		return jdbcTemplate.query(sql, new CwbMapper());
	}

	// 未入库List
	public String getzhongzhuanWeirukuSql(String crateStartdate, String crateEnddate, String customerid, long branchid, long page) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype = 10 ";
		sql = this.getzhongzhuanWeirukuSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		if (page > 0) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}
		return sql;
	}

	// 未入库总量条数
	public long getzhongzhuanWeirukuCount(String crateStartdate, String crateEnddate, String customerid, long branchid) {
		String sql = "SELECT count(1) FROM  express_ops_cwb_detail where state=1 and flowordertype = 10 ";
		sql = this.getzhongzhuanWeirukuSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		return jdbcTemplate.queryForLong(sql);
	}

	// 已入库
	public MonitorDTO getZhongzhuanYiruku(String crateStartdate, String crateEnddate, String customerid, long branchid) {
		String sql = "SELECT SUM(receivablefee) AS caramountsum,COUNT(1) as  countsum FROM  express_ops_cwb_detail where state=1 and flowordertype = 12 ";
		sql = this.getzhongzhuanSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		return jdbcTemplate.queryForObject(sql, new MonMapper());
	}

	// 已入库List
	public List<CwbOrder> getzhongzhuanYirukuList(String crateStartdate, String crateEnddate, String customerid, long branchid, long page) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype = 12  ";
		sql = this.getzhongzhuanSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		if (page > 0) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}
		return jdbcTemplate.query(sql, new CwbMapper());
	}

	// 已入库List
	public String getzhongzhuanYirukuSql(String crateStartdate, String crateEnddate, String customerid, long branchid, long page) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype = 12  ";
		sql = this.getzhongzhuanSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		return sql;
	}

	// 已入库总量条数
	public long getzhongzhuanYirukuCount(String crateStartdate, String crateEnddate, String customerid, long branchid) {
		String sql = "SELECT count(1) FROM  express_ops_cwb_detail where state=1 and flowordertype = 12  ";
		sql = this.getzhongzhuanSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		return jdbcTemplate.queryForLong(sql);
	}

	// 有货无单
	public MonitorDTO getzhongzhuanYouhuowudan(String crateStartdate, String crateEnddate, String customerid, long branchid) {
		String sql = "SELECT SUM(receivablefee) AS caramountsum,COUNT(1) as  countsum FROM  express_ops_cwb_detail where state=1 and flowordertype in(13) ";
		sql = this.getzhongzhuanSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		return jdbcTemplate.queryForObject(sql, new MonMapper());
	}

	// 有货无单List
	public List<CwbOrder> getzhongzhuanYouhuowudanList(String crateStartdate, String crateEnddate, String customerid, long branchid, long page) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype in(13)  ";
		sql = this.getzhongzhuanSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		if (page > 0) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}
		return jdbcTemplate.query(sql, new CwbMapper());
	}

	// 有货无单List
	public String getzhongzhuanYouhuowudanSql(String crateStartdate, String crateEnddate, String customerid, long branchid, long page) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype in(13)  ";
		sql = this.getzhongzhuanSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		if (page > 0) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}
		return sql;
	}

	// 有货无单总量条数
	public long getzhongzhuanYouhuowudanCount(String crateStartdate, String crateEnddate, String customerid, long branchid) {
		String sql = "SELECT count(1) FROM  express_ops_cwb_detail where state=1 and flowordertype in(13)  ";
		sql = this.getzhongzhuanSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		return jdbcTemplate.queryForLong(sql);
	}

	// 出库在途
	public MonitorDTO getzhongzhuanChukuzaitu(String crateStartdate, String crateEnddate, String customerid, long branchid) {
		String sql = "SELECT SUM(receivablefee) AS caramountsum,COUNT(1) as countsum FROM  express_ops_cwb_detail where state=1 and flowordertype = 14 ";
		sql = this.getzhongzhuanchukuSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		return jdbcTemplate.queryForObject(sql, new MonMapper());
	}

	// 出库在途List
	public List<CwbOrder> getzhongzhuanChukuzaituList(String crateStartdate, String crateEnddate, String customerid, long branchid, long page) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype = 14 ";
		sql = this.getzhongzhuanchukuSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		if (page > 0) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}
		return jdbcTemplate.query(sql, new CwbMapper());
	}

	// 出库在途List
	public String getzhongzhuanChukuzaituSql(String crateStartdate, String crateEnddate, String customerid, long branchid, long page) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype = 14 ";
		sql = this.getzhongzhuanchukuSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		return sql;
	}

	// 出库在途总量条数
	public long getzhongzhuanChukuzaituCount(String crateStartdate, String crateEnddate, String customerid, long branchid) {
		String sql = "SELECT count(1) FROM  express_ops_cwb_detail where state=1 and flowordertype = 14  ";
		sql = this.getzhongzhuanchukuSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		return jdbcTemplate.queryForLong(sql);
	}

	// 库存
	public MonitorDTO getzhongzhuanKucun(String crateStartdate, String crateEnddate, String customerid, long branchid) {
		String sql = "SELECT SUM(receivablefee) AS caramountsum,COUNT(1)  as countsum FROM  express_ops_cwb_detail where state=1 and flowordertype in (12,13) ";
		sql = this.getzhongzhuanSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		return jdbcTemplate.queryForObject(sql, new MonMapper());
	}

	// 库存List
	public List<CwbOrder> getzhongzhuanKucunList(String crateStartdate, String crateEnddate, String customerid, long branchid, long page) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype in (12,13) ";
		sql = this.getzhongzhuanSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		if (page > 0) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}
		return jdbcTemplate.query(sql, new CwbMapper());
	}

	// 库存List
	public String getzhongzhuanKucunSql(String crateStartdate, String crateEnddate, String customerid, long branchid, long page) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype in (12,13) ";
		sql = this.getzhongzhuanSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		return sql;
	}

	// 库存总量条数
	public long getzhongzhuanKucunCount(String crateStartdate, String crateEnddate, String customerid, long branchid) {
		String sql = "SELECT count(1) FROM  express_ops_cwb_detail where state=1 and flowordertype in (12,13) ";
		sql = this.getzhongzhuanSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		return jdbcTemplate.queryForLong(sql);
	}

	// ------------------------退货组=============
	// 入库总量
	public MonitorDTO gettuihuoAll(String crateStartdate, String crateEnddate, String customerid, long branchid) {
		String sql = "SELECT SUM(receivablefee) AS caramountsum,COUNT(1) as  countsum FROM  express_ops_cwb_detail where state=1 ";
		sql = this.gettuihuoSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		return jdbcTemplate.queryForObject(sql, new MonMapper());
	}

	// 入库总量List
	public List<CwbOrder> gettuihuoAllList(String crateStartdate, String crateEnddate, String customerid, long branchid, long page) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 ";
		sql = this.gettuihuoSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		if (page > 0) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}
		return jdbcTemplate.query(sql, new CwbMapper());
	}

	// 入库总量List
	public String gettuihuoAllSql(String crateStartdate, String crateEnddate, String customerid, long branchid, long page) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 ";
		sql = this.gettuihuoSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		return sql;
	}

	// 入库总量条数
	public long gettuihuoAllCount(String crateStartdate, String crateEnddate, String customerid, long branchid) {
		String sql = "SELECT count(1) FROM  express_ops_cwb_detail where state=1 ";
		sql = this.gettuihuoSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		return jdbcTemplate.queryForLong(sql);
	}

	// 未入库
	public MonitorDTO gettuihuoWeiruku(String crateStartdate, String crateEnddate, String customerid, long branchid) {
		String sql = "SELECT SUM(receivablefee) AS caramountsum,COUNT(1) as  countsum FROM  express_ops_cwb_detail where state=1 and flowordertype = 11 ";
		sql = this.gettuihuoWeirukuSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		return jdbcTemplate.queryForObject(sql, new MonMapper());
	}

	// 未入库List
	public List<CwbOrder> gettuihuoWeirukuList(String crateStartdate, String crateEnddate, String customerid, long branchid, long page) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype = 11 ";
		sql = this.gettuihuoWeirukuSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		if (page > 0) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}
		return jdbcTemplate.query(sql, new CwbMapper());
	}

	// 未入库List
	public String gettuihuoWeirukuSql(String crateStartdate, String crateEnddate, String customerid, long branchid, long page) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype = 11 ";
		sql = this.gettuihuoWeirukuSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		return sql;
	}

	// 未入库总量条数
	public long gettuihuoWeirukuCount(String crateStartdate, String crateEnddate, String customerid, long branchid) {
		String sql = "SELECT count(1) FROM  express_ops_cwb_detail where state=1 and flowordertype = 11 ";
		sql = this.gettuihuoWeirukuSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		return jdbcTemplate.queryForLong(sql);
	}

	// 已入库
	public MonitorDTO gettuihuoYiruku(String crateStartdate, String crateEnddate, String customerid, long branchid) {
		String sql = "SELECT SUM(receivablefee) AS caramountsum,COUNT(1) as  countsum FROM  express_ops_cwb_detail where state=1 and flowordertype = 15 ";
		sql = this.gettuihuoSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		return jdbcTemplate.queryForObject(sql, new MonMapper());
	}

	// 已入库List
	public List<CwbOrder> gettuihuoYirukuList(String crateStartdate, String crateEnddate, String customerid, long branchid, long page) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype = 15  ";
		sql = this.gettuihuoSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		if (page > 0) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}
		return jdbcTemplate.query(sql, new CwbMapper());
	}

	// 已入库List
	public String gettuihuoYirukuSql(String crateStartdate, String crateEnddate, String customerid, long branchid, long page) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype = 15  ";
		sql = this.gettuihuoSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		if (page > 0) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}
		return sql;
	}

	// 已入库总量条数
	public long gettuihuoYirukuCount(String crateStartdate, String crateEnddate, String customerid, long branchid) {
		String sql = "SELECT count(1) FROM  express_ops_cwb_detail where state=1 and flowordertype = 15  ";
		sql = this.gettuihuoSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		return jdbcTemplate.queryForLong(sql);
	}

	// 有货无单
	public MonitorDTO gettuihuoYouhuowudan(String crateStartdate, String crateEnddate, String customerid, long branchid) {
		String sql = "SELECT SUM(receivablefee) AS caramountsum,COUNT(1) as  countsum FROM  express_ops_cwb_detail where state=1 and flowordertype in(16) ";
		sql = this.gettuihuoSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		return jdbcTemplate.queryForObject(sql, new MonMapper());
	}

	// 有货无单List
	public List<CwbOrder> gettuihuoYouhuowudanList(String crateStartdate, String crateEnddate, String customerid, long branchid, long page) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype in(16)  ";
		sql = this.gettuihuoSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		if (page > 0) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}
		return jdbcTemplate.query(sql, new CwbMapper());
	}

	// 有货无单List
	public String gettuihuoYouhuowudanSql(String crateStartdate, String crateEnddate, String customerid, long branchid, long page) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype in(16)  ";
		sql = this.gettuihuoSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		if (page > 0) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}
		return sql;
	}

	// 有货无单总量条数
	public long gettuihuoYouhuowudanCount(String crateStartdate, String crateEnddate, String customerid, long branchid) {
		String sql = "SELECT count(1) FROM  express_ops_cwb_detail where state=1 and flowordertype in(16)  ";
		sql = this.gettuihuoSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		return jdbcTemplate.queryForLong(sql);
	}

	// 出库在途
	public MonitorDTO gettuihuoChukuzaitu(String crateStartdate, String crateEnddate, String customerid, long branchid) {
		String sql = "SELECT SUM(receivablefee) AS caramountsum,COUNT(1) as countsum FROM  express_ops_cwb_detail where state=1 and flowordertype in (17,27) ";
		sql = this.gettuihuochukuSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		return jdbcTemplate.queryForObject(sql, new MonMapper());
	}

	// 出库在途List
	public List<CwbOrder> gettuihuoChukuzaituList(String crateStartdate, String crateEnddate, String customerid, long branchid, long page) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype in (17,27) ";
		sql = this.gettuihuochukuSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		if (page > 0) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}
		return jdbcTemplate.query(sql, new CwbMapper());
	}

	// 出库在途List
	public String gettuihuoChukuzaituSql(String crateStartdate, String crateEnddate, String customerid, long branchid, long page) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype in (17,27) ";
		sql = this.gettuihuochukuSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		if (page > 0) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}
		return sql;
	}

	// 出库在途总量条数
	public long gettuihuoChukuzaituCount(String crateStartdate, String crateEnddate, String customerid, long branchid) {
		String sql = "SELECT count(1) FROM  express_ops_cwb_detail where state=1 and flowordertype in (17,27)  ";
		sql = this.gettuihuochukuSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		return jdbcTemplate.queryForLong(sql);
	}

	// 库存
	public MonitorDTO gettuihuoKucun(String crateStartdate, String crateEnddate, String customerid, long branchid) {
		String sql = "SELECT SUM(receivablefee) AS caramountsum,COUNT(1)  as countsum FROM  express_ops_cwb_detail where state=1 and flowordertype in (15,16) ";
		sql = this.gettuihuoSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		return jdbcTemplate.queryForObject(sql, new MonMapper());
	}

	// 库存List
	public List<CwbOrder> gettuihuoKucunList(String crateStartdate, String crateEnddate, String customerid, long branchid, long page) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype in (15,16) ";
		sql = this.gettuihuoSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		if (page > 0) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}
		return jdbcTemplate.query(sql, new CwbMapper());
	}

	// 库存List
	public String gettuihuoKucunSql(String crateStartdate, String crateEnddate, String customerid, long branchid, long page) {
		String sql = "SELECT * FROM  express_ops_cwb_detail where state=1 and flowordertype in (15,16) ";
		sql = this.gettuihuoSql(sql, crateStartdate, crateEnddate, customerid, branchid);
		if (page > 0) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}
		return sql;
	}

	// 库存总量条数
	public long gettuihuoKucunCount(String crateStartdate, String crateEnddate, String customerid, long branchid) {
		String sql = "SELECT count(1) FROM  express_ops_cwb_detail where state=1 and flowordertype in (15,16) ";
		sql = this.getzhongzhuanSql(sql, crateStartdate, crateEnddate, customerid, branchid);
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
