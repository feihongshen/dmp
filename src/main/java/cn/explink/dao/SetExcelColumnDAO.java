package cn.explink.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.ExcelColumnSet;

@Component
public class SetExcelColumnDAO {

	private final class ExcelColumnSetRowMapper implements RowMapper<ExcelColumnSet> {
		@Override
		public ExcelColumnSet mapRow(ResultSet rs, int rowNum) throws SQLException {
			ExcelColumnSet excelColumnSet = new ExcelColumnSet();
			excelColumnSet.setColumnid(rs.getLong("columnid"));
			excelColumnSet.setCustomerid(rs.getLong("customerid"));
			excelColumnSet.setCwbindex(rs.getInt("cwbindex"));
			excelColumnSet.setConsigneenameindex(rs.getInt("consigneenameindex"));
			excelColumnSet.setConsigneeaddressindex(rs.getInt("consigneeaddressindex"));
			excelColumnSet.setConsigneepostcodeindex(rs.getInt("consigneepostcodeindex"));
			excelColumnSet.setConsigneephoneindex(rs.getInt("consigneephoneindex"));
			excelColumnSet.setConsigneemobileindex(rs.getInt("consigneemobileindex"));
			excelColumnSet.setCwbremarkindex(rs.getInt("cwbremarkindex"));
			excelColumnSet.setSendcargonameindex(rs.getInt("sendcargonameindex"));
			excelColumnSet.setBackcargonameindex(rs.getInt("backcargonameindex"));
			excelColumnSet.setCargorealweightindex(rs.getInt("cargorealweightindex"));
			excelColumnSet.setReceivablefeeindex(rs.getInt("receivablefeeindex"));
			excelColumnSet.setPaybackfeeindex(rs.getInt("paybackfeeindex"));
			excelColumnSet.setExceldeliverindex(rs.getInt("exceldeliverindex"));
			excelColumnSet.setExcelbranchindex(rs.getInt("excelbranchindex"));
			excelColumnSet.setShipcwbindex(rs.getInt("shipcwbindex"));
			excelColumnSet.setConsigneenoindex(rs.getInt("consigneenoindex"));
			excelColumnSet.setCargoamountindex(rs.getInt("cargoamountindex"));
			excelColumnSet.setCustomercommandindex(rs.getInt("customercommandindex"));
			excelColumnSet.setGetmobileflag(rs.getInt("getmobileflag"));
			excelColumnSet.setCargotypeindex(rs.getInt("cargotypeindex"));
			excelColumnSet.setCargowarehouseindex(rs.getInt("cargowarehouseindex"));
			excelColumnSet.setCargosizeindex(rs.getInt("cargosizeindex"));
			excelColumnSet.setBackcargoamountindex(rs.getInt("backcargoamountindex"));
			excelColumnSet.setDestinationindex(rs.getInt("destinationindex"));
			excelColumnSet.setTranswayindex(rs.getInt("transwayindex"));
			excelColumnSet.setCommonnumberindex(rs.getInt("commonnumberindex"));
			excelColumnSet.setSendcargonumindex(rs.getInt("sendcargonumindex"));
			excelColumnSet.setBackcargonumindex(rs.getInt("backcargonumindex"));
			excelColumnSet.setCwbprovinceindex(rs.getInt("cwbprovinceindex"));
			excelColumnSet.setCwbcityindex(rs.getInt("cwbcityindex"));
			excelColumnSet.setCwbcountyindex(rs.getInt("cwbcountyindex"));
			excelColumnSet.setUpdatetime(rs.getString("updatetime"));
			excelColumnSet.setWarehousenameindex(rs.getInt("warehousenameindex"));
			excelColumnSet.setCwbordertypeindex(rs.getInt("cwbordertypeindex"));
			excelColumnSet.setCwbdelivertypeindex(rs.getInt("cwbdelivertypeindex"));
			excelColumnSet.setTranscwbindex(rs.getInt("transcwbindex"));
			excelColumnSet.setEmaildateindex(rs.getInt("emaildateindex"));
			excelColumnSet.setAccountareaindex(rs.getInt("accountareaindex"));
			excelColumnSet.setUpdateuserid(rs.getInt("updateuserid"));
			excelColumnSet.setModelnameindex(rs.getInt("modelnameindex"));
			excelColumnSet.setRemark1index(rs.getInt("remark1index"));
			excelColumnSet.setRemark2index(rs.getInt("remark2index"));
			excelColumnSet.setRemark3index(rs.getInt("remark3index"));
			excelColumnSet.setRemark4index(rs.getInt("remark4index"));
			excelColumnSet.setRemark5index(rs.getInt("remark5index"));
			excelColumnSet.setPaywayindex(rs.getInt("paywayindex"));
			excelColumnSet.setShouldfareindex(rs.getInt("shouldfareindex"));
			excelColumnSet.setReturnnoindex(rs.getInt("returnnoindex"));
			excelColumnSet.setReturnaddressindex(rs.getInt("returnaddressindex"));
			try {
				excelColumnSet.setUpdateusername(rs.getString("realname"));
			} catch (Exception e) {
			}
			return excelColumnSet;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ExcelColumnSet getExcelColumnSetByCustomerid(long customerid) {
		ExcelColumnSet userList = jdbcTemplate.queryForObject("SELECT * from express_set_excel_column where customerid=?", new ExcelColumnSetRowMapper(), customerid);
		return userList;
	}

	public ExcelColumnSet getColumnid(long columnid) {
		ExcelColumnSet columnList = jdbcTemplate.queryForObject("SELECT * from express_set_excel_column where columnid=?", new ExcelColumnSetRowMapper(), columnid);
		return columnList;
	}

	public List<ExcelColumnSet> getExcelColumnAll() {
		List<ExcelColumnSet> columnList = jdbcTemplate.query("SELECT ec.*,u.realname from express_set_excel_column ec left join express_set_user u on ec.updateuserid=u.userid ",
				new ExcelColumnSetRowMapper());
		return columnList;
	}

	public void creColumn(final ExcelColumnSet excelcolumnset) {
		jdbcTemplate.update("insert into express_set_excel_column (customerid,cwbindex,consigneenameindex,consigneeaddressindex,"
				+ "consigneepostcodeindex,consigneephoneindex,consigneemobileindex,cwbremarkindex,sendcargonameindex,backcargonameindex,cargorealweightindex,"
				+ "receivablefeeindex,paybackfeeindex,exceldeliverindex,excelbranchindex,shipcwbindex,consigneenoindex,"
				+ "cargoamountindex,customercommandindex,getmobileflag,cargotypeindex,cargowarehouseindex,"
				+ "cargosizeindex,backcargoamountindex,destinationindex,transwayindex,commonnumberindex,sendcargonumindex,backcargonumindex,cwbprovinceindex,cwbcityindex,cwbcountyindex,"
				+ "warehousenameindex,cwbordertypeindex,cwbdelivertypeindex,transcwbindex,emaildateindex,updatetime,updateuserid,accountareaindex,modelnameindex,"
				+ "remark1index,remark2index,remark3index,remark4index,remark5index,paywayindex,shouldfareindex,returnnoindex,returnaddressindex) "
				+ "values(?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,? ,?,?,?,?,?,?,?,?,?,?)", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, excelcolumnset.getCustomerid());
				ps.setInt(2, excelcolumnset.getCwbindex());
				ps.setInt(3, excelcolumnset.getConsigneenameindex());
				ps.setInt(4, excelcolumnset.getConsigneeaddressindex());
				ps.setInt(5, excelcolumnset.getConsigneepostcodeindex());
				ps.setInt(6, excelcolumnset.getConsigneephoneindex());
				ps.setInt(7, excelcolumnset.getConsigneemobileindex());
				ps.setInt(8, excelcolumnset.getCwbremarkindex());
				ps.setInt(9, excelcolumnset.getSendcargonameindex());
				ps.setInt(10, excelcolumnset.getBackcargonameindex());
				ps.setInt(11, excelcolumnset.getCargorealweightindex());
				ps.setInt(12, excelcolumnset.getReceivablefeeindex());
				ps.setInt(13, excelcolumnset.getPaybackfeeindex());
				ps.setInt(14, excelcolumnset.getExceldeliverindex());
				ps.setInt(15, excelcolumnset.getExcelbranchindex());
				ps.setInt(16, excelcolumnset.getShipcwbindex());
				ps.setInt(17, excelcolumnset.getConsigneenoindex());
				ps.setInt(18, excelcolumnset.getCargoamountindex());
				ps.setInt(19, excelcolumnset.getCustomercommandindex());
				ps.setInt(20, excelcolumnset.getGetmobileflag());
				ps.setInt(21, excelcolumnset.getCargotypeindex());
				ps.setInt(22, excelcolumnset.getCargowarehouseindex());
				ps.setInt(23, excelcolumnset.getCargosizeindex());
				ps.setInt(24, excelcolumnset.getBackcargoamountindex());
				ps.setInt(25, excelcolumnset.getDestinationindex());
				ps.setInt(26, excelcolumnset.getTranswayindex());
				ps.setInt(27, excelcolumnset.getCommonnumberindex());
				ps.setInt(28, excelcolumnset.getSendcargonumindex());
				ps.setInt(29, excelcolumnset.getBackcargonumindex());
				ps.setInt(30, excelcolumnset.getCwbprovinceindex());
				ps.setInt(31, excelcolumnset.getCwbcityindex());
				ps.setInt(32, excelcolumnset.getCwbcountyindex());
				ps.setInt(33, excelcolumnset.getWarehousenameindex());
				ps.setInt(34, excelcolumnset.getCwbordertypeindex());
				ps.setInt(35, excelcolumnset.getCwbdelivertypeindex());
				ps.setInt(36, excelcolumnset.getTranscwbindex());
				ps.setInt(37, excelcolumnset.getEmaildateindex());
				ps.setString(38, excelcolumnset.getUpdatetime());
				ps.setLong(39, excelcolumnset.getUpdateuserid());
				ps.setInt(40, excelcolumnset.getAccountareaindex());
				ps.setInt(41, excelcolumnset.getModelnameindex());
				ps.setInt(42, excelcolumnset.getRemark1index());
				ps.setInt(43, excelcolumnset.getRemark2index());
				ps.setInt(44, excelcolumnset.getRemark3index());
				ps.setInt(45, excelcolumnset.getRemark4index());
				ps.setInt(46, excelcolumnset.getRemark5index());
				ps.setInt(47, excelcolumnset.getPaywayindex());
				ps.setInt(48, excelcolumnset.getShouldfareindex());
				ps.setInt(49, excelcolumnset.getReturnnoindex());
				ps.setInt(50, excelcolumnset.getReturnaddressindex());
			}

		});
	}

	public void saveColumn(final ExcelColumnSet excelColumnSer) {
		jdbcTemplate
				.update("UPDATE express_set_excel_column SET cwbindex=?,consigneenameindex=?,consigneeaddressindex=?,"
						+ "consigneepostcodeindex=?,consigneephoneindex=?,consigneemobileindex=?,cwbremarkindex=?,sendcargonameindex=?,backcargonameindex=?,cargorealweightindex=?,"
						+ "receivablefeeindex=?,paybackfeeindex=?,exceldeliverindex=?,excelbranchindex=?,shipcwbindex=?,consigneenoindex=?,"
						+ "cargoamountindex=?,customercommandindex=?,getmobileflag=?,cargotypeindex=?,cargowarehouseindex=?,"
						+ "cargosizeindex=?,backcargoamountindex=?,destinationindex=?,transwayindex=?,commonnumberindex=?,sendcargonumindex=?,backcargonumindex=?,cwbprovinceindex=?,cwbcityindex=?,cwbcountyindex=?,"
						+ "warehousenameindex=?,cwbordertypeindex=?,cwbdelivertypeindex=?,transcwbindex=?,emaildateindex=?,updatetime=?,updateuserid=?,accountareaindex=?,modelnameindex=?, "
						+ "remark1index=?,remark2index=?,remark3index=?,remark4index=?,remark5index=?,paywayindex=?,shouldfareindex=?,returnnoindex=?,returnaddressindex=? where columnid=?", new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setInt(1, excelColumnSer.getCwbindex());
						ps.setInt(2, excelColumnSer.getConsigneenameindex());
						ps.setInt(3, excelColumnSer.getConsigneeaddressindex());
						ps.setInt(4, excelColumnSer.getConsigneepostcodeindex());
						ps.setInt(5, excelColumnSer.getConsigneephoneindex());
						ps.setInt(6, excelColumnSer.getConsigneemobileindex());
						ps.setInt(7, excelColumnSer.getCwbremarkindex());
						ps.setInt(8, excelColumnSer.getSendcargonameindex());
						ps.setInt(9, excelColumnSer.getBackcargonameindex());
						ps.setInt(10, excelColumnSer.getCargorealweightindex());
						ps.setInt(11, excelColumnSer.getReceivablefeeindex());
						ps.setInt(12, excelColumnSer.getPaybackfeeindex());
						ps.setInt(13, excelColumnSer.getExceldeliverindex());
						ps.setInt(14, excelColumnSer.getExcelbranchindex());
						ps.setInt(15, excelColumnSer.getShipcwbindex());
						ps.setInt(16, excelColumnSer.getConsigneenoindex());
						ps.setInt(17, excelColumnSer.getCargoamountindex());
						ps.setInt(18, excelColumnSer.getCustomercommandindex());
						ps.setInt(19, excelColumnSer.getGetmobileflag());
						ps.setInt(20, excelColumnSer.getCargotypeindex());
						ps.setInt(21, excelColumnSer.getCargowarehouseindex());
						ps.setInt(22, excelColumnSer.getCargosizeindex());
						ps.setInt(23, excelColumnSer.getBackcargoamountindex());
						ps.setInt(24, excelColumnSer.getDestinationindex());
						ps.setInt(25, excelColumnSer.getTranswayindex());
						ps.setInt(26, excelColumnSer.getCommonnumberindex());
						ps.setInt(27, excelColumnSer.getSendcargonumindex());
						ps.setInt(28, excelColumnSer.getBackcargonumindex());
						ps.setInt(29, excelColumnSer.getCwbprovinceindex());
						ps.setInt(30, excelColumnSer.getCwbcityindex());
						ps.setInt(31, excelColumnSer.getCwbcountyindex());
						ps.setInt(32, excelColumnSer.getWarehousenameindex());
						ps.setInt(33, excelColumnSer.getCwbordertypeindex());
						ps.setInt(34, excelColumnSer.getCwbdelivertypeindex());
						ps.setInt(35, excelColumnSer.getTranscwbindex());
						ps.setInt(36, excelColumnSer.getEmaildateindex());
						ps.setString(37, excelColumnSer.getUpdatetime());
						ps.setLong(38, excelColumnSer.getUpdateuserid());
						ps.setInt(39, excelColumnSer.getAccountareaindex());
						ps.setLong(40, excelColumnSer.getModelnameindex());
						ps.setInt(41, excelColumnSer.getRemark1index());
						ps.setInt(42, excelColumnSer.getRemark2index());
						ps.setInt(43, excelColumnSer.getRemark3index());
						ps.setInt(44, excelColumnSer.getRemark4index());
						ps.setInt(45, excelColumnSer.getRemark5index());
						ps.setInt(46, excelColumnSer.getPaywayindex());
						ps.setLong(47, excelColumnSer.getShouldfareindex());
						ps.setInt(48, excelColumnSer.getReturnnoindex());
						ps.setInt(49, excelColumnSer.getReturnaddressindex());
						ps.setLong(50, excelColumnSer.getColumnid());

					}

				});
	}

	public void getDeleColumn(long columnid) {

		jdbcTemplate.update("delete from express_set_excel_column where columnid=?", columnid);
	}

	/**
	 * ��鹩����ģ���Ƿ��Ѿ����� һ��������ֻ����һ��ģ��
	 * 
	 * @param customerid
	 * @return
	 */
	public long getSetExcelColumnSetByCustomerid(long customerid) {
		String sql = " SELECT COUNT(1) FROM   express_set_excel_column  WHERE customerid= ?";
		return jdbcTemplate.queryForLong(sql, customerid);
	}

}
