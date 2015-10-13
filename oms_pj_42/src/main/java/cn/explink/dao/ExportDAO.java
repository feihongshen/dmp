package cn.explink.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import cn.explink.controller.ExportDTO;
import cn.explink.domain.CwbOrder;
import cn.explink.util.StringUtil;

@Component
public class ExportDAO {

	private final class ExportDTORowMapper implements RowMapper<ExportDTO> {
		@Override
		public ExportDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			ExportDTO exportDTO = new ExportDTO();

			exportDTO.setCwb(StringUtil.nullConvertToEmptyString(rs.getString("cwb")));
			exportDTO.setCommonname(StringUtil.nullConvertToEmptyString(rs.getString("commonname")));
			exportDTO.setCustomername(StringUtil.nullConvertToEmptyString(rs.getString("customername")));
			exportDTO.setTranscwb(StringUtil.nullConvertToEmptyString(rs.getString("transcwb")));
			exportDTO.setConsigneename(StringUtil.nullConvertToEmptyString(rs.getString("consigneename")));
			exportDTO.setConsigneeaddress(StringUtil.nullConvertToEmptyString(rs.getString("consigneeaddress")));
			exportDTO.setConsigneepostcode(StringUtil.nullConvertToEmptyString(rs.getString("consigneepostcode")));
			exportDTO.setConsigneepostcode(StringUtil.nullConvertToEmptyString(rs.getString("consigneepostcode")));
			exportDTO.setConsigneephone(StringUtil.nullConvertToEmptyString(rs.getString("consigneephone")));
			exportDTO.setConsigneemobile(StringUtil.nullConvertToEmptyString(rs.getString("consigneemobile")));
			exportDTO.setSendcarname(StringUtil.nullConvertToEmptyString(rs.getString("sendcarname")));
			exportDTO.setBackcarname(StringUtil.nullConvertToEmptyString(rs.getString("backcarname")));
			exportDTO.setCarrealweight(rs.getBigDecimal("carrealweight"));
			exportDTO.setReceivablefee(rs.getBigDecimal("receivablefee"));
			exportDTO.setCwbremark(StringUtil.nullConvertToEmptyString(rs.getString("cwbremark")));
			exportDTO.setExceldeliver(StringUtil.nullConvertToEmptyString(rs.getString("exceldeliver")));
			exportDTO.setExcelbranch(StringUtil.nullConvertToEmptyString(rs.getString("excelbranch")));
			exportDTO.setShipcwb(StringUtil.nullConvertToEmptyString(rs.getString("shipcwb")));
			exportDTO.setConsigneeno(StringUtil.nullConvertToEmptyString(rs.getString("consigneeno")));
			exportDTO.setCaramount(rs.getBigDecimal("caramount"));
			exportDTO.setCustomercommand(StringUtil.nullConvertToEmptyString(rs.getString("customercommand")));
			exportDTO.setCartype(StringUtil.nullConvertToEmptyString(rs.getString("cartype")));
			exportDTO.setCarsize(rs.getBigDecimal("carsize"));
			exportDTO.setBackcaramount(rs.getBigDecimal("backcaramount"));
			exportDTO.setDestination(StringUtil.nullConvertToEmptyString(rs.getString("destination")));
			exportDTO.setTransway(StringUtil.nullConvertToEmptyString(rs.getString("transway")));
			exportDTO.setShipperid(StringUtil.nullConvertToEmptyString(rs.getString("shipperid")));
			exportDTO.setSendcarnum(StringUtil.nullConvertToEmptyString(rs.getString("sendcarnum")));
			exportDTO.setBackcarnum(StringUtil.nullConvertToEmptyString(rs.getString("backcarnum")));
			exportDTO.setCwbprovince(StringUtil.nullConvertToEmptyString(rs.getString("cwbprovince")));
			exportDTO.setCwbcity(StringUtil.nullConvertToEmptyString(rs.getString("cwbcity")));
			exportDTO.setCwbcounty(StringUtil.nullConvertToEmptyString(rs.getString("cwbcounty")));
			exportDTO.setCarwarehouse(StringUtil.nullConvertToEmptyString(rs.getString("carwarehouse")));
			exportDTO.setCwbordertypeid(rs.getLong("cwbordertypeid"));
			exportDTO.setEdittime(StringUtil.nullConvertToEmptyString(rs.getString("edittime")));
			exportDTO.setFlowordertype(rs.getLong("flowordertype"));
			exportDTO.setAuditor(StringUtil.nullConvertToEmptyString(rs.getString("auditor")));
			exportDTO.setNewfollownotes(StringUtil.nullConvertToEmptyString(rs.getString("newfollownotes")));
			exportDTO.setMarksflagmen(StringUtil.nullConvertToEmptyString(rs.getString("marksflagmen")));
			exportDTO.setPrimitivemoney(rs.getBigDecimal("primitivemoney"));
			exportDTO.setSignintime(StringUtil.nullConvertToEmptyString(rs.getString("signintime")));
			exportDTO.setSigninman(StringUtil.nullConvertToEmptyString(rs.getString("signinman")));
			exportDTO.setEditman(StringUtil.nullConvertToEmptyString(rs.getString("editman")));
			exportDTO.setCredate(StringUtil.nullConvertToEmptyString(rs.getString("credate")));
			exportDTO.setBranchname(StringUtil.nullConvertToEmptyString(rs.getString("branchname")));
			exportDTO.setReceivedfee(rs.getBigDecimal("receivedfee"));
			exportDTO.setReturnedfee(rs.getBigDecimal("returnedfee"));
			exportDTO.setDeliverystate(rs.getLong("deliverystate"));
			exportDTO.setCash(rs.getBigDecimal("cash"));
			exportDTO.setPos(rs.getBigDecimal("pos"));
			exportDTO.setPosremark(StringUtil.nullConvertToEmptyString(rs.getString("posremark")));
			exportDTO.setMobilepodtime(StringUtil.nullConvertToEmptyString(rs.getString("mobilepodtime")));
			exportDTO.setCheckfee(rs.getBigDecimal("checkfee"));
			exportDTO.setCheckremark(StringUtil.nullConvertToEmptyString(rs.getString("checkremark")));
			exportDTO.setReceivedfeeuser(StringUtil.nullConvertToEmptyString(rs.getString("receivedfeeuser")));
			exportDTO.setStatisticstate(rs.getLong("statisticstate"));
			exportDTO.setOtherfee(rs.getBigDecimal("otherfee"));
			exportDTO.setBusinessfee(rs.getBigDecimal("businessfee"));
			exportDTO.setCreatetime(rs.getString("returngoodsremark"));
			exportDTO.setUserid(rs.getLong("userid"));
			// exportDTO.setReturngoodsremark(rs.getString("returngoodsremark"));

			return exportDTO;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private String getCwbOrderByPageWhereSqlQ(String sql, String cwbandtranscwb, long datetype, String begindate, String enddate, long customerid, String commonnumber, long flowordertype) {

		if (cwbandtranscwb.length() > 0 || datetype > 0 || begindate.length() > 0 || enddate.length() > 0 || customerid > 0 || commonnumber.length() > 0 || flowordertype > 0) {
			StringBuffer w = new StringBuffer();
			sql += " where ";
			if (cwbandtranscwb.trim().length() > 0) {
				w.append(" and  (c.cwb in (");
				for (String cwb : cwbandtranscwb.split("\r\n")) {
					w.append("'" + cwb.trim());
					w.append("',");
				}
				String u = w.substring(0, w.length() - 1) + ")";
				w = new StringBuffer(u);
				w.append(" or c.transcwb in (");
				for (String cwb : cwbandtranscwb.split("\r\n")) {
					w.append("'" + cwb.trim());
					w.append("',");
				}
				String h = w.substring(0, w.length() - 1) + "))";
				w = new StringBuffer(h);
			}
			if (datetype == 1) {
				if (begindate.length() > 0) {
					w.append(" and c.emaildate >='" + begindate + " 00:00:00'");
				}
				if (enddate.length() > 0) {
					w.append(" and c.emaildate <= '" + enddate + " 23:59:59'");
				}
			}
			if (datetype == 2) {
				if (begindate.length() > 0) {
					w.append(" and c.signintime >='" + begindate + " 00:00:00'");
				}
				if (enddate.length() > 0) {
					w.append(" and c.signintime <= '" + enddate + " 23:59:59'");
				}
			}
			if (customerid > 0) {
				w.append(" and c.customerid= " + customerid);
			}
			if (commonnumber.length() > 0) {
				w.append(" and c.commonnumber= '" + commonnumber + "'");
			}
			if (flowordertype > 0) {
				w.append(" and c.flowordertype= " + flowordertype);
			}
			w.append(" and c.state=1 and f.isnow =1 ");
			sql += w.substring(4, w.length());
		} else {
			sql += " where c.state=1 and f.isnow =1 ";
		}
		return sql;
	}

	public List<ExportDTO> getExport(String cwbandtranscwb, long datetype, String begindate, String enddate, long customerid, String commonnumber, long flowordertype, String orderName) {

		String sql = "SELECT c.*,f.credate,f.userid,f.isnow,d.* FROM express_ops_cwb_detail AS c LEFT JOIN " + "express_ops_order_flow AS f ON  c.cwb = f.cwb LEFT JOIN "
				+ "express_ops_delivery_state AS d ON c.cwb = d.cwb AND f.cwb =d.cwb ";

		sql = this.getCwbOrderByPageWhereSqlQ(sql, cwbandtranscwb, datetype, begindate, enddate, customerid, commonnumber, flowordertype);

		List<ExportDTO> exportDTO = jdbcTemplate.query(sql, new ExportDTORowMapper());
		return exportDTO;
	}

	public List<ExportDTO> getExportHmj(String cwbandtranscwb, long datetype, String begindate, String enddate, long customerid, String commonnumber, long flowordertype, String orderName) {

		String sql = "SELECT c.*,f.credate,f.userid,f.isnow,d.* FROM express_ops_cwb_detail AS c LEFT JOIN " + "express_ops_order_flow AS f ON  c.cwb = f.cwb LEFT JOIN "
				+ "express_ops_delivery_state AS d ON c.cwb = d.cwb AND f.cwb =d.cwb ";

		sql = this.getCwbOrderByPageWhereSqlQ(sql, cwbandtranscwb, datetype, begindate, enddate, customerid, commonnumber, flowordertype);

		List<ExportDTO> exportDTO = jdbcTemplate.query(sql, new ExportDTORowMapper());
		return exportDTO;
	}

}
