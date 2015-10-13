package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import cn.explink.controller.MonitorDTO;
import cn.explink.controller.MonitorOrderDTO;
import cn.explink.domain.CwbOrder;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Component
public class MonitorDAO {

	private final class MonMapper implements RowMapper<MonitorDTO> {
		@Override
		public MonitorDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			MonitorDTO monDto = new MonitorDTO();
			monDto.setCountsum(rs.getLong("countsum"));
			monDto.setCaramountsum(rs.getBigDecimal("caramountsum"));
			return monDto;
		}
	}

	private final class MonExpMapper implements RowMapper<MonitorDTO> {
		@Override
		public MonitorDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			MonitorDTO monDto = new MonitorDTO();
			monDto.setBranchid(rs.getLong("branchid"));
			monDto.setCountsum(rs.getLong("countsum"));
			monDto.setCaramountsum(rs.getBigDecimal("caramountsum"));
			return monDto;
		}
	}

	private final class MonOrderMapper implements RowMapper<MonitorOrderDTO> {
		@Override
		public MonitorOrderDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			MonitorOrderDTO monDto = new MonitorOrderDTO();
			monDto.setCwb(rs.getString("cwb"));
			monDto.setConsigneename(StringUtil.nullConvertToEmptyString(rs.getString("consigneename")));
			monDto.setSendcarname(StringUtil.nullConvertToEmptyString(rs.getString("sendcarname")));
			monDto.setConsigneemobile(StringUtil.nullConvertToEmptyString(rs.getString("consigneemobile")));
			monDto.setEmaildate(StringUtil.nullConvertToEmptyString(rs.getString("emaildate")));
			monDto.setShiptime(StringUtil.nullConvertToEmptyString(rs.getString("shiptime")));
			monDto.setBranchname(StringUtil.nullConvertToEmptyString(rs.getString("branchname")));
			monDto.setRealname(StringUtil.nullConvertToEmptyString(rs.getString("realname")));
			monDto.setCaramount(rs.getBigDecimal("caramount"));
			return monDto;
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

	private String getMonitorWhereSql(String sql, String crateStartdate, String crateEnddate, String shipStartTime, String shipEndTime, String emailStartTime, String eamilEndTime, String emaildate,
			String flowordertype, String customerid, long branchid) {
		if (crateStartdate.length() > 0 || crateEnddate.length() > 0 || shipStartTime.length() > 0 || shipEndTime.length() > 0 || emailStartTime.length() > 0 || eamilEndTime.length() > 0
				|| emaildate.length() > 0 || customerid.length() > 0 || flowordertype.length() > 0 || branchid > 0) {
			StringBuffer w = new StringBuffer();
			if (crateStartdate.length() > 0) {
				w.append(" AND credate>='" + crateStartdate + "' ");
			}
			if (crateEnddate.length() > 0) {
				w.append(" AND credate<='" + crateEnddate + "' ");
			}
			if (shipStartTime.length() > 0) {
				w.append(" AND shiptime >='" + shipStartTime + "' ");
			}
			if (shipEndTime.length() > 0) {
				w.append(" AND shiptime <='" + shipEndTime + "' ");
			}
			if (emailStartTime.length() > 0) {
				w.append(" AND emaildate >='" + emailStartTime + "' ");
			}
			if (eamilEndTime.length() > 0) {
				w.append(" AND emaildate <='" + eamilEndTime + "' ");
			}
			if (emaildate.length() > 0) {
				w.append(" AND emaildateid IN(" + emaildate + ")");
			}
			if (customerid.length() > 0) {
				w.append(" AND customerid IN(" + customerid + ")");
			}
			if (flowordertype.length() > 0) {
				w.append(" AND flowordertype in(" + flowordertype + ")");
			}
			if (branchid > 0) {
				w.append(" AND branchid=" + branchid);
			}
			sql += w;
		}
		return sql;
	}

	private String getMonitorDeliveryWhereSql(String sql, String crateStartdate, String crateEnddate, String shipStartTime, String shipEndTime, String emailStartTime, String eamilEndTime,
			String emaildate, String orderResultType, String flowordertype, String customerid, long branchid) {
		if (crateStartdate.length() > 0 || crateEnddate.length() > 0 || shipStartTime.length() > 0 || shipEndTime.length() > 0 || emailStartTime.length() > 0 || eamilEndTime.length() > 0
				|| emaildate.length() > 0 || customerid.length() > 0 || orderResultType.length() > 0 || flowordertype.length() > 0 || branchid > 0) {
			StringBuffer w = new StringBuffer();
			if (crateStartdate.length() > 0) {
				w.append(" AND credate>='" + crateStartdate + "' ");
			}
			if (crateEnddate.length() > 0) {
				w.append(" AND credate<='" + crateEnddate + "' ");
			}
			if (shipStartTime.length() > 0) {
				w.append(" AND shiptime >='" + shipStartTime + "' ");
			}
			if (shipEndTime.length() > 0) {
				w.append(" AND shiptime <='" + shipEndTime + "' ");
			}
			if (emailStartTime.length() > 0) {
				w.append(" AND emaildate >='" + emailStartTime + "' ");
			}
			if (eamilEndTime.length() > 0) {
				w.append(" AND emaildate <='" + eamilEndTime + "' ");
			}
			if (emaildate.length() > 0) {
				w.append(" AND emaildateid IN(" + emaildate + ")");
			}
			if (customerid.length() > 0) {
				w.append(" AND customerid IN(" + customerid + ")");
			}
			if (flowordertype.length() > 0) {
				w.append(" AND flowordertype in(" + flowordertype + ")");
			}
			if (orderResultType.length() > 0) {
				w.append(" AND orderResultType in (" + orderResultType + ")");
			}
			if (branchid > 0) {
				w.append(" AND branchid=" + branchid);
			}
			sql += w;
		}
		return sql;
	}

	private String getMonitorWhereSqlHouse(String sql, String crateStartdate, String crateEnddate, String shipStartTime, String shipEndTime, String emailStartTime, String eamilEndTime,
			String emaildate, String flowordertype, String customerid, long branchid) {
		if (crateStartdate.length() > 0 || crateEnddate.length() > 0 || shipStartTime.length() > 0 || shipEndTime.length() > 0 || emailStartTime.length() > 0 || eamilEndTime.length() > 0
				|| emaildate.length() > 0 || customerid.length() > 0 || flowordertype.length() > 0 || branchid > 0) {
			StringBuffer w = new StringBuffer();
			if (crateStartdate.length() > 0) {
				w.append(" AND credate>='" + crateStartdate + "' ");
			}
			if (crateEnddate.length() > 0) {
				w.append(" AND credate<='" + crateEnddate + "' ");
			}
			if (shipStartTime.length() > 0) {
				w.append(" AND shiptime >='" + shipStartTime + "' ");
			}
			if (shipEndTime.length() > 0) {
				w.append(" AND shiptime <='" + shipEndTime + "' ");
			}
			if (emailStartTime.length() > 0) {
				w.append(" AND emaildate >='" + emailStartTime + "' ");
			}
			if (eamilEndTime.length() > 0) {
				w.append(" AND emaildate <='" + eamilEndTime + "' ");
			}
			if (emaildate.length() > 0) {
				w.append(" AND emaildateid IN(" + emaildate + ")");
			}
			if (customerid.length() > 0) {
				w.append(" AND customerid IN(" + customerid + ")");
			}
			if (flowordertype.length() > 0) {
				w.append(" AND flowordertype in(" + flowordertype + ")");
			}
			if (branchid > 0) {
				w.append(" AND carwarehouse='" + branchid + "'");
			}
			sql += w;
		}
		return sql;
	}

	private String getMonitorOrderWhereSql(String sql, String crateStartdate, String crateEnddate, String shipStartTime, String shipEndTime, String emailStartTime, String eamilEndTime,
			String emaildate, String flowordertype, String customerid, long branchid) {
		if (crateStartdate.length() > 0 || crateEnddate.length() > 0 || shipStartTime.length() > 0 || shipEndTime.length() > 0 || emailStartTime.length() > 0 || eamilEndTime.length() > 0
				|| emaildate.length() > 0 || customerid.length() > 0 || flowordertype.length() > 0 || branchid > 0) {
			StringBuffer w = new StringBuffer();
			if (crateStartdate.length() > 0) {
				w.append(" AND e.credate>='" + crateStartdate + "' ");
			}
			if (crateEnddate.length() > 0) {
				w.append(" AND e.credate<='" + crateEnddate + "' ");
			}
			if (shipStartTime.length() > 0) {
				w.append(" AND e.shiptime>='" + shipStartTime + "' ");
			}
			if (shipEndTime.length() > 0) {
				w.append(" AND e.shiptime<='" + shipEndTime + "' ");
			}
			if (emailStartTime.length() > 0) {
				w.append(" AND e.emaildate>='" + emailStartTime + "' ");
			}
			if (eamilEndTime.length() > 0) {
				w.append(" AND e.emaildate<='" + eamilEndTime + "' ");
			}
			if (emaildate.length() > 0) {
				w.append(" AND e.emaildateid IN(" + emaildate + ")");
			}
			if (customerid.length() > 0) {
				w.append(" AND e.customerid IN(" + customerid + ")");
			}
			if (flowordertype.length() > 0) {
				w.append(" AND e.flowordertype in(" + flowordertype + ")");
			}
			if (branchid > 0) {
				w.append(" AND e.branchid =" + branchid);
			}
			sql += w;
		}
		return sql;
	}

	// 有单无货和未入库特殊处理
	private String getYoudanwuhuoSql(String sql, String crateStartdate, String crateEnddate, String shipStartTime, String shipEndTime, String emailStartTime, String eamilEndTime, String emaildate,
			String flowordertype, String customerid) {
		if (crateStartdate.length() > 0 || crateEnddate.length() > 0 || shipStartTime.length() > 0 || shipEndTime.length() > 0 || emailStartTime.length() > 0 || eamilEndTime.length() > 0
				|| emaildate.length() > 0 || customerid.length() > 0 || flowordertype.length() > 0) {
			StringBuffer w = new StringBuffer();
			if (crateStartdate.length() > 0) {
				w.append(" AND credate>='" + crateStartdate + "' ");
			}
			if (crateEnddate.length() > 0) {
				w.append(" AND credate<='" + crateEnddate + "' ");
			}
			if (shipStartTime.length() > 0) {
				w.append(" AND shiptime >='" + shipStartTime + "' ");
			}
			if (shipEndTime.length() > 0) {
				w.append(" AND shiptime <='" + shipEndTime + "' ");
			}
			if (emailStartTime.length() > 0) {
				w.append(" AND emaildate >='" + emailStartTime + "' ");
			}
			if (eamilEndTime.length() > 0) {
				w.append(" AND emaildate <='" + eamilEndTime + "' ");
			}
			if (customerid.length() > 0) {
				w.append(" AND customerid IN(" + customerid + ")");
			}
			if (flowordertype.length() > 0) {
				w.append(" AND flowordertype in(" + flowordertype + ")");
			}
			sql += w;
		}
		return sql;
	}

	// 有单无货和未入库特殊处理
	private String getYoudanwuhuoListSql(String sql, String crateStartdate, String crateEnddate, String shipStartTime, String shipEndTime, String emailStartTime, String eamilEndTime,
			String emaildate, String flowordertype, String customerid) {
		if (crateStartdate.length() > 0 || crateEnddate.length() > 0 || shipStartTime.length() > 0 || shipEndTime.length() > 0 || emailStartTime.length() > 0 || eamilEndTime.length() > 0
				|| emaildate.length() > 0 || customerid.length() > 0 || flowordertype.length() > 0) {
			StringBuffer w = new StringBuffer();
			if (crateStartdate.length() > 0) {
				w.append(" AND e.credate>='" + crateStartdate + "' ");
			}
			if (crateEnddate.length() > 0) {
				w.append(" AND e.credate<='" + crateEnddate + "' ");
			}
			if (shipStartTime.length() > 0) {
				w.append(" AND e.shiptime>='" + shipStartTime + "' ");
			}
			if (shipEndTime.length() > 0) {
				w.append(" AND e.shiptime<='" + shipEndTime + "' ");
			}
			if (emailStartTime.length() > 0) {
				w.append(" AND e.emaildate>='" + emailStartTime + "' ");
			}
			if (eamilEndTime.length() > 0) {
				w.append(" AND e.emaildate<='" + eamilEndTime + "' ");
			}
			if (customerid.length() > 0) {
				w.append(" AND e.customerid IN(" + customerid + ")");
			}
			if (flowordertype.length() > 0) {
				w.append(" AND e.flowordertype in(" + flowordertype + ")");
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
	public MonitorDTO getHouse(String crateStartdate, String crateEnddate, String shipStartTime, String shipEndTime, String emailStartTime, String eamilEndTime, String emaildate,
			String flowordertype, String customerid, int isnow, long branchid) {
		String sql = "SELECT SUM(caramount) AS caramountsum,COUNT(1) countsum FROM (SELECT caramount FROM express_ops_order_flow where " + (isnow == 1 ? " isnow=1 " : " isnow IN(0,1) ")
				+ " and state=1 ";
		sql = this.getMonitorWhereSqlHouse(sql, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate, flowordertype, customerid, branchid);
		sql = sql + "  GROUP BY cwb ) AS p";
		return jdbcTemplate.queryForObject(sql, new MonMapper());
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
	public MonitorDTO getMon(String crateStartdate, String crateEnddate, String shipStartTime, String shipEndTime, String emailStartTime, String eamilEndTime, String emaildate, String flowordertype,
			String customerid, int isnow, long branchid) {
		String sql = "SELECT SUM(caramount) AS caramountsum,COUNT(1) countsum FROM (SELECT caramount FROM express_ops_order_flow where " + (isnow == 1 ? " isnow=1 " : " isnow IN(0,1) ")
				+ " and state=1 ";
		sql = this.getMonitorWhereSql(sql, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate, flowordertype, customerid, branchid);
		sql = sql + "  GROUP BY cwb ) AS p";
		return jdbcTemplate.queryForObject(sql, new MonMapper());
	}

	public MonitorDTO getMonDelivery(String crateStartdate, String crateEnddate, String shipStartTime, String shipEndTime, String emailStartTime, String eamilEndTime, String emaildate,
			String orderResultType, String flowordertype, String customerid, int isnow, long branchid) {
		String sql = "SELECT SUM(caramount) AS caramountsum,COUNT(1) countsum FROM (SELECT caramount FROM express_ops_order_flow where " + (isnow == 1 ? " isnow=1 " : " isnow IN(0,1) ")
				+ " and state=1 ";
		sql = this.getMonitorDeliveryWhereSql(sql, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate, orderResultType, flowordertype, customerid,
				branchid);
		sql = sql + "  GROUP BY cwb ) AS p";
		return jdbcTemplate.queryForObject(sql, new MonMapper());
	}

	/**
	 * 常规的获取异常订单的统计数据，返回订单数量和金额
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
	public List<MonitorDTO> getExpMon(String crateStartdate, String crateEnddate, String shipStartTime, String shipEndTime, String emailStartTime, String eamilEndTime, String emaildate,
			String flowordertype, String customerid, int isnow, long branchid) {
		String sql = "SELECT branchid,SUM(caramount) AS caramountsum,COUNT(1) countsum FROM express_ops_order_flow where " + (isnow == 1 ? " isnow=1 " : " isnow IN(0,1) ") + " and state=1 ";
		sql = this.getMonitorWhereSql(sql, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate, flowordertype, customerid, branchid)
				+ " group by branchid ";
		return jdbcTemplate.query(sql, new MonExpMapper());
	}

	/**
	 * 常规的获取订单的订单的详细信息
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
	 *         ,供货商id为"1",统计最近7天的入库订单的详细信息
	 */
	public List<MonitorOrderDTO> getMonOrderList(String crateStartdate, String crateEnddate, String shipStartTime, String shipEndTime, String emailStartTime, String eamilEndTime, String emaildate,
			String flowordertype, String customerid, int isnow, long branchid, long page) {
		String sql1 = "SELECT d.branchname,d.delivername as realname, e.cwb,e.caramount,d.consigneename,d.consigneeaddress,d.sendcarname,d.consigneemobile,d.emaildate,d.shiptime FROM express_ops_order_flow AS e "
				+ "LEFT JOIN  express_ops_cwb_detail AS d ON e.cwb =d.cwb  " + "WHERE " + (isnow == 1 ? " e.isnow=1 " : " e.isnow IN(0,1) ") + " and e.state=1 ";
		String sql = this.getMonitorOrderWhereSql(sql1, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate, flowordertype, customerid, branchid);
		if (page > 0) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}
		return jdbcTemplate.query(sql, new MonOrderMapper());
	}

	public long getMonOrderListCount(String crateStartdate, String crateEnddate, String shipStartTime, String shipEndTime, String emailStartTime, String eamilEndTime, String emaildate,
			String flowordertype, String customerid, int isnow, long branchid) {
		String sql1 = "SELECT count(1) FROM express_ops_order_flow AS e " + "LEFT JOIN  express_ops_cwb_detail AS d ON e.cwb =d.cwb  " + "WHERE " + (isnow == 1 ? " e.isnow=1 " : " e.isnow IN(0,1) ")
				+ " and e.state=1 ";
		String sql = this.getMonitorOrderWhereSql(sql1, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate, flowordertype, customerid, branchid);
		return jdbcTemplate.queryForLong(sql);
	}

	/**
	 * 单独统计有单无货 算法：同一批次的货物，有其中1个或者一个以上做了扫描，剩下的未被扫描的，均算作有单无货
	 * 
	 * @param emaildate
	 *            批次时间（字符串 如："'2012-06-12 12:23:20'"）
	 * @param branchid
	 *            站点id
	 * @param customerid
	 *            供货商 （字符串 如："1,2,3"）
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
	 * @return 订单总数和金额
	 */
	public MonitorDTO getMonYoudanwuhuoHouse(String emaildate, long branchid, String customerid, String crateStartdate, String crateEnddate, String shipStartTime, String shipEndTime,
			String emailStartTime, String eamilEndTime) {
		String sql = "SELECT SUM(caramount) AS caramountsum,COUNT(1) countsum   FROM  express_ops_order_flow WHERE "
				+ "(SELECT  COUNT(1) AS deliverycount FROM  express_ops_order_flow  WHERE  isnow=1 " + " AND emaildateid IN(" + (emaildate.equals("") ? "''" : emaildate) + ") AND flowordertype >= 4 "
				+ (branchid == -1 ? "" : " AND carwarehouse=" + branchid);
		sql = getYoudanwuhuoSql(sql, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate, "", customerid);
		String sql2 = ") > 0 AND flowordertype < 4 AND  isnow=1   and state=1 " + " AND emaildateid IN(" + (emaildate.equals("") ? "''" : emaildate) + ")"
				+ (branchid == -1 ? "" : " AND carwarehouse=" + branchid);
		String sql3 = getYoudanwuhuoSql(sql + sql2, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate, "", customerid);
		return jdbcTemplate.queryForObject(sql3, new MonMapper());
	}

	/**
	 * 单独统计有单无货 算法：同一批次的货物，有其中1个或者一个以上做了扫描，剩下的未被扫描的，均算作有单无货
	 * 
	 * @param emaildate
	 *            批次时间（字符串 如："'2012-06-12 12:23:20'"）
	 * @param branchid
	 *            站点id
	 * @param customerid
	 *            供货商 （字符串 如："1,2,3"）
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
	 * @return 订单总数和金额
	 */
	public MonitorDTO getMonYoudanwuhuo(String emaildate, long branchid, String customerid, String crateStartdate, String crateEnddate, String shipStartTime, String shipEndTime,
			String emailStartTime, String eamilEndTime) {
		String sql = "SELECT SUM(caramount) AS caramountsum,COUNT(1) countsum   FROM  express_ops_order_flow WHERE "
				+ "(SELECT  COUNT(1) AS deliverycount FROM  express_ops_order_flow  WHERE  isnow=1 " + " AND emaildateid IN(" + (emaildate.equals("") ? "''" : emaildate) + ") AND flowordertype > 1 "
				+ (branchid == -1 ? "" : " AND branchid=" + branchid);
		sql = getYoudanwuhuoSql(sql, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate, "", customerid);
		String sql2 = ") > 0 AND flowordertype = 1 AND  isnow=1   and state=1 " + " AND emaildateid IN(" + (emaildate.equals("") ? "''" : emaildate) + ")"
				+ (branchid == -1 ? "" : " AND branchid=" + branchid);
		String sql3 = getYoudanwuhuoSql(sql + sql2, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate, "", customerid);
		return jdbcTemplate.queryForObject(sql3, new MonMapper());
	}

	/**
	 * 单独统计异常信息监控中有单无货 算法：同一批次的货物，有其中1个或者一个以上做了扫描，剩下的未被扫描的，均算作有单无货
	 * 
	 * @param emaildate
	 *            批次时间（字符串 如："'2012-06-12 12:23:20'"）
	 * @param branchid
	 *            站点id
	 * @param customerid
	 *            供货商 （字符串 如："1,2,3"）
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
	 * @return 订单总数和金额
	 */
	public List<MonitorDTO> getExpMonYoudanwuhuo(String emaildate, long branchid, String customerid, String crateStartdate, String crateEnddate, String shipStartTime, String shipEndTime,
			String emailStartTime, String eamilEndTime) {
		String sql = "SELECT branchid, SUM(caramount) AS caramountsum,COUNT(1) countsum   FROM  express_ops_order_flow WHERE "
				+ "(SELECT  COUNT(1) AS deliverycount FROM  express_ops_order_flow  WHERE  isnow=1  and state=1 " + " AND emaildateid IN(" + (emaildate.equals("") ? "''" : emaildate)
				+ ") AND flowordertype > 1 " + (branchid == -1 ? "" : " AND branchid=" + branchid);
		sql = getYoudanwuhuoSql(sql, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate, "", customerid);
		String sql2 = ") > 0 AND flowordertype = 1 AND  isnow=1   and state=1 " + " AND emaildateid IN(" + (emaildate.equals("") ? "''" : emaildate) + ")"
				+ (branchid == -1 ? "" : " AND branchid=" + branchid);
		String sql3 = getYoudanwuhuoSql(sql + sql2, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate, "", customerid) + " group by branchid";
		return jdbcTemplate.query(sql3, new MonExpMapper());
	}

	/**
	 * 单独统计有单无货 算法：同一批次的货物，有其中1个或者一个以上做了扫描，剩下的未被扫描的，均算作有单无货
	 * 
	 * @param emaildate
	 *            批次时间（字符串 如："'2012-06-12 12:23:20'"）
	 * @param branchid
	 *            站点id
	 * @param customerid
	 *            供货商 （字符串 如："1,2,3"）
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
	 * @return 订单详细列表
	 */
	public List<MonitorOrderDTO> getMonOrderYoudanwuhuoList(String emaildate, long branchid, String customerid, String crateStartdate, String crateEnddate, String shipStartTime, String shipEndTime,
			String emailStartTime, String eamilEndTime, long page) {
		String sql = "SELECT d.branchname,d.delivername as realname,e.cwb,e.caramount,d.consigneename,d.consigneeaddress,d.sendcarname,d.consigneemobile,d.emaildate,d.shiptime FROM express_ops_order_flow AS e "
				+ "LEFT JOIN  express_ops_cwb_detail AS d ON "
				+ "e.cwb =d.cwb  WHERE "
				+ "(SELECT  COUNT(1) AS deliverycount FROM  express_ops_order_flow  WHERE  isnow=1  and state=1 "
				+ "AND emaildateid IN(" + (emaildate.equals("") ? "''" : emaildate) + ") AND flowordertype > 1 " + (branchid == -1 ? "" : " AND branchid=" + branchid);
		sql = getYoudanwuhuoSql(sql, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate, "", customerid);
		String sql1 = ") > 0 AND e.flowordertype = 1 AND  e.isnow=1   and e.state=1 " + "AND e.emaildateid IN(" + (emaildate.equals("") ? "''" : emaildate) + ") ";
		String sql2 = getYoudanwuhuoListSql(sql + sql1, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate, "", customerid);
		if (page > 0) {
			sql2 += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}

		return jdbcTemplate.query(sql2, new MonOrderMapper());
	}

	public long getMonOrderYoudanwuhuoListCount(String emaildate, long branchid, String customerid, String crateStartdate, String crateEnddate, String shipStartTime, String shipEndTime,
			String emailStartTime, String eamilEndTime) {

		String sql = "SELECT count(1) FROM express_ops_order_flow AS e " + "LEFT JOIN  express_ops_cwb_detail AS d ON " + "e.cwb =d.cwb  WHERE "
				+ "(SELECT  COUNT(1) AS deliverycount FROM  express_ops_order_flow  WHERE  isnow=1  and state=1 " + "AND emaildateid IN(" + (emaildate.equals("") ? "''" : emaildate)
				+ ") AND flowordertype > 1 " + (branchid == -1 ? "" : " AND branchid=" + branchid);
		sql = getYoudanwuhuoSql(sql, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate, "", customerid);
		String sql1 = ") > 0 AND e.flowordertype = 1 AND  e.isnow=1   and e.state=1 " + "AND e.emaildateid IN(" + (emaildate.equals("") ? "''" : emaildate) + ") "
				+ (branchid == -1 ? "" : " AND e.branchid=" + branchid);
		String sql2 = getYoudanwuhuoListSql(sql + sql1, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate, "", customerid);
		return jdbcTemplate.queryForLong(sql2);
	}

	/**
	 * 单独统计未入库 算法：同一批次的货物，有其中1个或者一个以上做了入库扫描，剩下的未被扫描的，均算作未入库
	 * 
	 * @param emaildate
	 *            批次时间（字符串 如："'2012-06-12 12:23:20'"）
	 * @param branchid
	 *            站点id
	 * @param customerid
	 *            供货商 （字符串 如："1,2,3"）
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
	 * @return 订单总数和金额
	 */
	public MonitorDTO getMonWeirukuHouse(String emaildate, long branchid, String flowordertype, String customerid, String crateStartdate, String crateEnddate, String shipStartTime,
			String shipEndTime, String emailStartTime, String eamilEndTime) {
		String sql = "SELECT SUM(caramount) AS caramountsum,COUNT(1) countsum   FROM  express_ops_order_flow WHERE  flowordertype < 4 and isnow=1 "
				+ (branchid == -1 ? "" : " AND carwarehouse=" + branchid);
		sql = getYoudanwuhuoSql(sql, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate, "", customerid);
		return jdbcTemplate.queryForObject(sql, new MonMapper());
	}

	/**
	 * 单独统计未入库 算法：同一批次的货物，有其中1个或者一个以上做了入库扫描，剩下的未被扫描的，均算作未入库
	 * 
	 * @param emaildate
	 *            批次时间（字符串 如："'2012-06-12 12:23:20'"）
	 * @param branchid
	 *            站点id
	 * @param customerid
	 *            供货商 （字符串 如："1,2,3"）
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
	 * @return 订单总数和金额
	 */
	public MonitorDTO getMonWeiruku(String emaildate, long branchid, String flowordertype, String customerid, String crateStartdate, String crateEnddate, String shipStartTime, String shipEndTime,
			String emailStartTime, String eamilEndTime) {
		String sql = "SELECT SUM(caramount) AS caramountsum,COUNT(1) countsum   FROM  express_ops_order_flow WHERE "
				+ "(SELECT  COUNT(1) AS deliverycount FROM  express_ops_order_flow  WHERE  isnow=1  and state=1 " + " AND emaildateid IN(" + (emaildate.equals("") ? "''" : emaildate)
				+ ") AND flowordertype IN(" + flowordertype + ") " + (branchid == -1 ? "" : " AND branchid=" + branchid);
		sql = getYoudanwuhuoSql(sql, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate, "", customerid);
		String sql2 = ") > 0 AND flowordertype = 1 AND  isnow=1  and state=1  " + " AND emaildateid IN(" + (emaildate.equals("") ? "''" : emaildate) + ")"
				+ (branchid == -1 ? "" : " AND branchid=" + branchid);
		String sql3 = getYoudanwuhuoSql(sql + sql2, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate, "", customerid);
		return jdbcTemplate.queryForObject(sql3, new MonMapper());
	}

	/**
	 * 单独统计未入库 算法：同一批次的货物，有其中1个或者一个以上做了入库扫描，剩下的未被扫描的，均算作未入库
	 * 
	 * @param emaildate
	 *            批次时间（字符串 如："'2012-06-12 12:23:20'"）
	 * @param branchid
	 *            站点id
	 * @param customerid
	 *            供货商 （字符串 如："1,2,3"）
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
	 * @return 订单详细列表
	 */
	public List<MonitorOrderDTO> getMonOrderWeirukuList(String emaildate, long branchid, String flowordertype, String customerid, String crateStartdate, String crateEnddate, String shipStartTime,
			String shipEndTime, String emailStartTime, String eamilEndTime, long page) {
		String sql = "SELECT d.branchname,d.delivername as realname,e.cwb,e.caramount,d.consigneename,d.consigneeaddress,d.sendcarname,d.consigneemobile,d.emaildate,d.shiptime FROM express_ops_order_flow AS e "
				+ "LEFT JOIN  express_ops_cwb_detail AS d ON "
				+ "e.cwb =d.cwb  WHERE "
				+ "(SELECT  COUNT(1) AS deliverycount FROM  express_ops_order_flow  WHERE  isnow=1  and state=1 "
				+ "AND emaildateid IN(" + (emaildate.equals("") ? "''" : emaildate) + ") AND flowordertype IN(" + flowordertype + ") " + (branchid == -1 ? "" : " AND branchid=" + branchid);
		sql = getYoudanwuhuoSql(sql, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate, "", customerid);
		String sql1 = ") > 0 AND e.flowordertype = 1 AND  e.isnow=1   and e.state=1 " + "AND e.emaildateid IN(" + (emaildate.equals("") ? "''" : emaildate) + ") "
				+ (branchid == -1 ? "" : " AND branchid=" + branchid);
		String sql2 = getYoudanwuhuoListSql(sql + sql1, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate, "", customerid);
		if (page > 0) {
			sql2 += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}
		return jdbcTemplate.query(sql2, new MonOrderMapper());
	}

	public long getMonOrderWeirukuListCount(String emaildate, long branchid, String flowordertype, String customerid, String crateStartdate, String crateEnddate, String shipStartTime,
			String shipEndTime, String emailStartTime, String eamilEndTime) {

		String sql = "SELECT count(1) FROM express_ops_order_flow AS e " + "LEFT JOIN  express_ops_cwb_detail AS d ON " + "e.cwb =d.cwb  WHERE "
				+ "(SELECT  COUNT(1) AS deliverycount FROM  express_ops_order_flow  WHERE  isnow=1  and state=1 " + "AND emaildateid IN(" + (emaildate.equals("") ? "''" : emaildate)
				+ ") AND flowordertype IN(" + flowordertype + ") " + (branchid == -1 ? "" : " AND branchid=" + branchid);
		sql = getYoudanwuhuoSql(sql, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate, "", customerid);
		String sql1 = ") > 0 AND e.flowordertype = 1 AND  e.isnow=1   and e.state=1 " + "AND e.emaildateid IN(" + (emaildate.equals("") ? "''" : emaildate) + ") "
				+ (branchid == -1 ? "" : " AND branchid=" + branchid);
		String sql2 = getYoudanwuhuoListSql(sql + sql1, crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate, "", customerid);
		return jdbcTemplate.queryForLong(sql2);
	}

	public MonitorDTO getMonByFlowtype(long brandid, long flowordertype, String credate) {
		String sql = "SELECT SUM(caramount) AS caramountsum,COUNT(1) countsum FROM express_ops_cwb_detail where " + (brandid > 0 ? " startbranchid = " + brandid + " AND " : "") + " flowordertype="
				+ flowordertype + " AND nowtime <= '" + credate + "'AND state=1 ";
		if (flowordertype == 6) {
			sql = "SELECT SUM(caramount) AS caramountsum,COUNT(1) countsum FROM express_ops_cwb_detail where " + (brandid > 0 ? " nextbranchid = " + brandid + " AND " : "") + " flowordertype="
					+ flowordertype + " AND nowtime <= '" + credate + "' AND state=1 ";
		}
		return jdbcTemplate.queryForObject(sql, new MonMapper());
	}

	public List<MonitorOrderDTO> getMonByFlowtypeListExp(long brandid, long flowordertype, String credate) {
		String sql = "SELECT d.branchname,d.delivername as realname,e.cwb,e.caramount,d.consigneename,d.consigneeaddress,d.sendcarname,d.consigneemobile,d.emaildate,d.shiptime FROM "
				+ "express_ops_order_flow AS e LEFT JOIN  express_ops_cwb_detail AS d ON e.cwb =d.cwb  WHERE  e.isnow=1   and e.state=1 AND e.flowordertype =" + flowordertype + " AND "
				+ (brandid > 0 ? " e.branchid = " + brandid + " AND " : "") + " e.credate<= '" + credate + "'";
		return jdbcTemplate.query(sql, new MonOrderMapper());
	}

	public List<CwbOrder> getMonByFlowtypeList(long page, long brandid, long flowordertype, String credate) {
		String sql = "SELECT * FROM express_ops_cwb_detail where " + (brandid > 0 ? " startbranchid = " + brandid + " AND " : "") + " flowordertype=" + flowordertype + " AND nowtime <= '" + credate
				+ "'AND state=1 ";
		if (flowordertype == 6) {
			sql = "SELECT * FROM express_ops_cwb_detail where " + (brandid > 0 ? " nextbranchid = " + brandid + " AND " : "") + " flowordertype=" + flowordertype + " AND nowtime <= '" + credate
					+ "'AND state=1 ";
		}
		if (page > 0) {
			sql += " limit " + (page - 1) * Page.ONE_PAGE_NUMBER + " ," + Page.ONE_PAGE_NUMBER;
		}
		return jdbcTemplate.query(sql, new CwbMapper());
	}

	public SqlRowSet getMonByResultSet(long page, long brandid, long flowordertype, String credate) {
		String sql = "SELECT * FROM express_ops_cwb_detail where " + (brandid > 0 ? " startbranchid = " + brandid + " AND " : "") + " flowordertype=" + flowordertype + " AND nowtime <= '" + credate
				+ "'AND state=1 ";
		if (flowordertype == 6) {
			sql = "SELECT * FROM express_ops_cwb_detail where " + (brandid > 0 ? " nextbranchid = " + brandid + " AND " : "") + " flowordertype=" + flowordertype + " AND nowtime <= '" + credate
					+ "'AND state=1 ";
		}
		SqlRowSet r = jdbcTemplate.queryForRowSet(sql);
		return r;
	}

	public long getCountMonByFlowtypeList(long page, long brandid, long flowordertype, String credate) {
		String sql = "SELECT count(1) FROM " + "express_ops_order_flow AS e LEFT JOIN  express_ops_cwb_detail AS d ON e.cwb =d.cwb  WHERE  e.isnow=1   and e.state=1 AND e.flowordertype ="
				+ flowordertype + " AND e.branchid =" + brandid + "  AND e.credate<= '" + credate + "'";
		return jdbcTemplate.queryForInt(sql);
	}

	/**
	 * 监控站点有单无货有货无单
	 * 
	 * @return
	 */
	public List<MonitorDTO> getExptForBranch_youdanwuhuo(String emaildateid, long branchid, String starttime, String endtime) {
		String sql = "SELECT nextbranchid as branchid ,SUM(caramount) AS caramountsum,COUNT(1) countsum " + " FROM  express_ops_cwb_detail "
				+ " WHERE ( SELECT  COUNT(1) AS deliverycount FROM  express_ops_cwb_detail  WHERE  state=1 " + "  AND flowordertype>=" + FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue();
		if (branchid > 0) {
			sql += " AND nextbranchid=" + branchid;
		}
		sql += ") > 0 " + " AND flowordertype < " + FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + "   AND state=1   ";
		if (starttime != null && !"".equals(starttime) && endtime != null && !"".equals(endtime)) {
			sql += " AND emaildate between '" + starttime + "' and '" + endtime + "' ";
		}
		sql += " GROUP BY nextbranchid ";
		return jdbcTemplate.query(sql, new MonExpMapper());
	}

	/**
	 * 监控站点 有货无单
	 * 
	 * @return
	 */
	public List<MonitorDTO> getExptForBranch_youhuowudan(String emaildateid, long branchid, String starttime, String endtime) {
		String sql = " SELECT startbranchid as branchid,COUNT(1) AS countsum,0 AS caramountsum " + " FROM express_ops_cwb_detail WHERE flowordertype=8  ";
		if (branchid > 0) {
			sql += " and startbranchid=" + branchid;
		}
		if (starttime != null && !"".equals(starttime) && endtime != null && !"".equals(endtime)) {
			sql += " AND emaildate between '" + starttime + "' and '" + endtime + "' ";
		}
		sql += " GROUP BY startbranchid ";
		return jdbcTemplate.query(sql, new MonExpMapper());
	}

}
