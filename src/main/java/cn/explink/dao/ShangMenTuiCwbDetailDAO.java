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

import com.pjbest.splitting.aspect.DataSource;
import com.pjbest.splitting.routing.DatabaseType;

import cn.explink.domain.ShangMenTuiCwbDetail;
import cn.explink.util.StringUtil;

@Component
public class ShangMenTuiCwbDetailDAO {
	private final class ShangMenTuiCwbDetailMapper implements RowMapper<ShangMenTuiCwbDetail> {

		@Override
		public ShangMenTuiCwbDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			ShangMenTuiCwbDetail shangMenTuiCwbDetail = new ShangMenTuiCwbDetail();
			shangMenTuiCwbDetail.setId(rs.getLong("id"));
			shangMenTuiCwbDetail.setBackcarnum(rs.getLong("backcarnum"));
			shangMenTuiCwbDetail.setCustomerid(rs.getLong("customerid"));
			shangMenTuiCwbDetail.setConsigneename(StringUtil.nullConvertToEmptyString(rs.getString("consigneename")));
			shangMenTuiCwbDetail.setConsigneeaddress(StringUtil.nullConvertToEmptyString(rs.getString("consigneeaddress")));
			shangMenTuiCwbDetail.setConsigneephone(StringUtil.nullConvertToEmptyString(rs.getString("consigneephone")));
			shangMenTuiCwbDetail.setPaybackfee(rs.getBigDecimal("paybackfee"));
			shangMenTuiCwbDetail.setCwb(StringUtil.nullConvertToEmptyString(rs.getString("cwb")));
			shangMenTuiCwbDetail.setConsigneemobile(StringUtil.nullConvertToEmptyString(rs.getString("consigneemobile")));
			shangMenTuiCwbDetail.setPrinttime(rs.getString("printtime"));
			shangMenTuiCwbDetail.setRemark3(StringUtil.nullConvertToEmptyString(rs.getString("remark3")));
			shangMenTuiCwbDetail.setRemark4(StringUtil.nullConvertToEmptyString(rs.getString("remark4")));
			shangMenTuiCwbDetail.setRemark5(StringUtil.nullConvertToEmptyString(rs.getString("remark5")));
			shangMenTuiCwbDetail.setCarwarehouseid(rs.getLong("carwarehouseid"));
			shangMenTuiCwbDetail.setBackcarname(StringUtil.nullConvertToEmptyString(rs.getString("backcarname")));
			shangMenTuiCwbDetail.setConsigneepostcode(StringUtil.nullConvertToEmptyString(rs.getString("consigneepostcode")));
			shangMenTuiCwbDetail.setEmaildate(StringUtil.nullConvertToEmptyString(rs.getString("emaildate")));
			shangMenTuiCwbDetail.setEmaildateid(rs.getLong("emaildateid"));
			shangMenTuiCwbDetail.setDeliverybranchid(rs.getLong("deliverybranchid"));

			return shangMenTuiCwbDetail;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void insertShangMenTuiCwbDetail(final ShangMenTuiCwbDetail shangMenTuiCwbDetail) {
		jdbcTemplate.update("insert into shangmentuicwb_detail (cwb,consigneename,consigneeaddress,consigneephone,paybackfee,"
				+ "customerid,consigneemobile,backcarnum,carwarehouseid,remark3,remark4,remark5,printtime,backcarname," + "consigneepostcode,emaildate,emaildateid,deliverybranchid) "
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {

				ps.setString(1, shangMenTuiCwbDetail.getCwb());
				ps.setString(2, shangMenTuiCwbDetail.getConsigneename());
				ps.setString(3, shangMenTuiCwbDetail.getConsigneeaddress());
				ps.setString(4, shangMenTuiCwbDetail.getConsigneephone());
				ps.setFloat(5, shangMenTuiCwbDetail.getPaybackfee().floatValue());
				ps.setLong(6, shangMenTuiCwbDetail.getCustomerid());
				ps.setString(7, shangMenTuiCwbDetail.getConsigneemobile());
				ps.setLong(8, shangMenTuiCwbDetail.getBackcarnum());
				ps.setLong(9, shangMenTuiCwbDetail.getCarwarehouseid());
				ps.setString(10, shangMenTuiCwbDetail.getRemark3());
				ps.setString(11, shangMenTuiCwbDetail.getRemark4());
				ps.setString(12, shangMenTuiCwbDetail.getRemark5());
				ps.setString(13, shangMenTuiCwbDetail.getPrinttime());
				ps.setString(14, shangMenTuiCwbDetail.getBackcarname());
				ps.setString(15, shangMenTuiCwbDetail.getConsigneepostcode());
				ps.setString(16, shangMenTuiCwbDetail.getEmaildate());
				ps.setLong(17, shangMenTuiCwbDetail.getEmaildateid());
				ps.setLong(18, shangMenTuiCwbDetail.getDeliverybranchid());
			}

		});
	}

	public void saveShangMenTuiCwbDetailByCwb(ShangMenTuiCwbDetail shangMenTuiCwbDetail) {
		String sql = "update shangmentuicwb_detail set consigneename=?,consigneeaddress=?,consigneephone=?,paybackfee=?," + "customerid=?,consigneemobile=?,backcarnum=?,carwarehouseid=?,"
				+ "remark3=?,remark4=?,remark5=?,printtime=?,backcarname=?," + "consigneepostcode=?,emaildate=?,emaildateid=?,deliverybranchid=? where cwb=? ";

		jdbcTemplate.update(sql, shangMenTuiCwbDetail.getConsigneename(), shangMenTuiCwbDetail.getConsigneeaddress(), shangMenTuiCwbDetail.getConsigneephone(), shangMenTuiCwbDetail.getPaybackfee(),
				shangMenTuiCwbDetail.getCustomerid(), shangMenTuiCwbDetail.getConsigneemobile(), shangMenTuiCwbDetail.getBackcarnum(), shangMenTuiCwbDetail.getCarwarehouseid(),
				shangMenTuiCwbDetail.getRemark3(), shangMenTuiCwbDetail.getRemark4(), shangMenTuiCwbDetail.getRemark5(), shangMenTuiCwbDetail.getPrinttime(), shangMenTuiCwbDetail.getBackcarname(),
				shangMenTuiCwbDetail.getConsigneepostcode(), shangMenTuiCwbDetail.getEmaildate(), shangMenTuiCwbDetail.getEmaildateid(), shangMenTuiCwbDetail.getDeliverybranchid(),
				shangMenTuiCwbDetail.getCwb());
	}

	public long getShangMenTuiCwbDetailCountByCwb(String cwb) {
		String sql = "select count(1) from shangmentuicwb_detail where cwb=? ";
		return jdbcTemplate.queryForLong(sql, cwb);
	}

	@DataSource(DatabaseType.REPLICA)
	public List<String> getShangMenTuiCwbDetailByCustomerid(String customerids, long printType, String begindate, String enddate, long deliverybranchid,String orders,String selectype) {
		if (selectype.equals("1")) {
			if (!orders.isEmpty()&&orders.length()>0){
				String sql = "select distinct sd.cwb from shangmentuicwb_detail sd left join express_ops_cwb_detail cd "
						+ " on sd.cwb=cd.cwb where cd.delivery_permit=0 and sd.cwb IN("+orders+")" ;
				return jdbcTemplate.queryForList(sql, String.class);
			}
		}
		StringBuffer sql = new StringBuffer();
	    sql.append("select distinct sd.cwb from shangmentuicwb_detail sd left join express_ops_cwb_detail cd "
				+ " on sd.cwb=cd.cwb where cd.state=1 and cd.delivery_permit=0 ");
		if (printType == 0) {
			sql.append(" and cd.flowordertype IN(1,2,3,4,6,7,8,37) ");
			sql.append(" and cd.printtime='' ");
		} else {
			sql.append(" and cd.printtime >= '" + begindate + "'  and cd.printtime <= '" + enddate + "'");
		}
		
		if (customerids.length() > 0) {
			sql.append(" and sd.customerid in(" + customerids + ")");
		}
		if (deliverybranchid > 0) {
			sql.append(" and sd.deliverybranchid=" + deliverybranchid);
		}
		System.out.println(sql);
		return jdbcTemplate.queryForList(sql.toString(), String.class);
	}

	public void saveShangMenTuiCwbDetailForPrinttime(String cwb, String printtime) {
		String sql = "update shangmentuicwb_detail set printtime='"+printtime+"' where cwb=? ";
		 jdbcTemplate.update(sql, cwb);
	}

	public List<ShangMenTuiCwbDetail> getShangMenTuiCwbDetailByCwbs(String cwbs) {
		return jdbcTemplate.query("SELECT * from shangmentuicwb_detail where cwb in(" + cwbs + ") ", new ShangMenTuiCwbDetailMapper());
	}

	// 同时更改上门退订单表中的字段
	public void updateRemark5ByCwb(String cwb, String remark5, String consigneeName, String consigneMobile) {
		String sql = " update shangmentuicwb_detail set remark5=?,consigneename=?,consigneemobile=? where cwb =? ";
		jdbcTemplate.update(sql, remark5, consigneeName, consigneMobile, cwb);
	}

	// 同时更改上门退订单表中的字段
	public void updateRemark3ByCwb(String cwb, String remark) {
		String sql = " update shangmentuicwb_detail set remark3=? WHERE cwb=? ";
		jdbcTemplate.update(sql, remark, cwb);
	}
	
	
	// 同时更改上门退订单表中的字段
		public void deletePrintRecord(String cwb) {
			String sql = " delete from  shangmentuicwb_detail where cwb= ? ";
			jdbcTemplate.update(sql, cwb);
		}
}
