package cn.explink.b2c.auto.order.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import cn.explink.controller.CwbOrderDTO;
import cn.explink.service.DataImportService;
import cn.explink.util.StringUtil;

@Service
public class TPSDataImportDAO_B2c {
	@SuppressWarnings("unused")
	private Logger logger = LoggerFactory.getLogger(DataImportService.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@SuppressWarnings("unused")
	private final class CwbDTOMapper implements RowMapper<CwbOrderDTO> {
		@Override
		public CwbOrderDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			CwbOrderDTO cwbOrder = new CwbOrderDTO();
			cwbOrder.setStartbranchid(rs.getLong("startbranchid"));
			cwbOrder.setCustomerwarehouseid(rs.getLong("customerwarehouseid"));
			cwbOrder.setEmaildate(StringUtil.nullConvertToEmptyString(rs.getString("emaildate")));
			cwbOrder.setEmaildateid(rs.getLong("emaildateid"));
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
			cwbOrder.setCwbordertypeid(rs.getLong("cwbordertypeid"));
			cwbOrder.setConsigneemobile(StringUtil.nullConvertToEmptyString(rs.getString("consigneemobile")));
			cwbOrder.setTranscwb(StringUtil.nullConvertToEmptyString(rs.getString("transcwb")));
			cwbOrder.setDestination(StringUtil.nullConvertToEmptyString(rs.getString("destination")));
			cwbOrder.setCwbdelivertypeid(rs.getLong("cwbdelivertypeid"));
			cwbOrder.setExceldeliver(StringUtil.nullConvertToEmptyString(rs.getString("exceldeliver")));
			cwbOrder.setExcelbranch(StringUtil.nullConvertToEmptyString(rs.getString("excelbranch")));
			cwbOrder.setCargorealweight(rs.getBigDecimal("carrealweight"));
			cwbOrder.setModelname(rs.getString("modelname"));

			cwbOrder.setPaywayid(rs.getLong("paywayid"));
			cwbOrder.setNewpaywayid(rs.getString("newpaywayid"));
			cwbOrder.setMulti_shipcwb(rs.getString("multi_shipcwb"));
			cwbOrder.setCargovolume(rs.getBigDecimal("cargovolume"));
			cwbOrder.setConsignoraddress(rs.getString("consignoraddress"));
			cwbOrder.setCargoamount(rs.getBigDecimal("caramount"));
			cwbOrder.setSendcargoname(rs.getString("sendcarname"));
			cwbOrder.setBackcargoname(rs.getString("backcarname"));
			cwbOrder.setOpscwbid(rs.getLong("opscwbid"));
			cwbOrder.setSendcargonum(rs.getInt("sendcarnum"));
			cwbOrder.setBackcargonum(rs.getInt("backcarnum"));
			cwbOrder.setRemark1(rs.getString("remark1"));
			cwbOrder.setRemark2(rs.getString("remark2"));
			cwbOrder.setRemark3(rs.getString("remark3"));
			cwbOrder.setRemark4(rs.getString("remark4"));
			cwbOrder.setRemark5(rs.getString("remark5"));
			cwbOrder.setCargotype(rs.getString("cartype"));
			cwbOrder.setCargosize(rs.getString("carsize"));
			cwbOrder.setCommoncwb(StringUtil.nullConvertToEmptyString(rs.getString("commoncwb")));
			cwbOrder.setShouldfare(rs.getBigDecimal("shouldfare"));
			cwbOrder.setInfactfare(rs.getBigDecimal("infactfare"));
			cwbOrder.setResendtime(rs.getString("resendtime"));

			return cwbOrder;
		}
	}
	
	//修改订单表
	public void updateBycwb(final CwbOrderDTO order) {
		String sql = "update express_ops_cwb_detail_b2ctemp set consigneename=? ,sendcarnum=?,consigneemobile=?,consigneephone=?,consigneepostcode=?,"
				+ "consigneeaddress=?,receivablefee=?,customercommand=?,remark2=?,remark5=?,carrealweight=?,paywayid=?," + "cartype=?,cwbordertypeid=?,shouldfare=? "
				+ " where cwb =? and state=1  ";
		jdbcTemplate.update(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, order.getConsigneename().toString());
				ps.setString(2, order.getSendcargoname().toString());
				ps.setString(3, order.getConsigneemobile().toString());
				ps.setString(4, order.getConsigneephone().toString());
				ps.setString(5, order.getConsigneepostcode().toString());

				ps.setString(6, order.getConsignoraddress().toString());
				ps.setString(7, order.getReceivablefee().toString());
				ps.setString(8, order.getCustomercommand().toString());
				ps.setString(9, order.getRemark2().toString());
				ps.setString(10, order.getRemark5().toString());
				ps.setString(11, order.getCargorealweight().toString());
				ps.setLong(12, order.getPaywayid());

				ps.setString(13, order.getCargotype().toString());
				ps.setLong(14, order.getCwbordertypeid());
				ps.setString(15, order.getShouldfare().toString());
				ps.setString(16, order.getCwb().toString());
			}
		});

	}
	
	//修改临时表
	public void updateTempBycwb(final CwbOrderDTO order) {
		String sql = "update express_ops_cwb_detail set consigneename=? ,sendcarnum=?,consigneemobile=?,consigneephone=?,consigneepostcode=?,"
				+ "consigneeaddress=?,receivablefee=?,customercommand=?,remark1=?,remark2=?,remark3=?,remark4=?,remark5=?,carrealweight=?,paywayid=?," + "cartype=?,cwbordertypeid=?,shouldfare=? "
				+ " where cwb =? and state=1  ";
		this.jdbcTemplate.update(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, order.getConsigneename().toString());
				ps.setString(2, order.getSendcargoname().toString());
				ps.setString(3, order.getConsigneemobile().toString());
				ps.setString(4, order.getConsigneephone().toString());
				ps.setString(5, order.getConsigneepostcode().toString());

				ps.setString(6, order.getConsignoraddress().toString());
				ps.setString(7, order.getReceivablefee().toString());
				ps.setString(8, order.getCustomercommand().toString());
				ps.setString(9, order.getRemark2().toString());
				ps.setString(10, order.getRemark5().toString());
				ps.setString(11, order.getCargorealweight().toString());
				ps.setLong(12, order.getPaywayid());

				ps.setString(13, order.getCargotype().toString());
				ps.setLong(14, order.getCwbordertypeid());
				ps.setString(15, order.getShouldfare().toString());
				ps.setString(16, order.getCwb().toString());
			}
		});

	}
	
}
