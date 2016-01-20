package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.FnDfBillDetail;

@Component
public class FnDfBillDetailDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class BillDetailRowMapper implements RowMapper<FnDfBillDetail> {
		@Override
		public FnDfBillDetail mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			FnDfBillDetail detail = new FnDfBillDetail();
			detail.setId(rs.getLong("detail_id"));
			detail.setOrderNo(rs.getString("order_no"));
			detail.setBillId(rs.getLong("bill_id"));
			detail.setOrderStatus(rs.getInt("order_status"));
			detail.setOrderType(rs.getInt("order_type"));
			detail.setSite(rs.getString("site"));
			detail.setDeliveryAmount(rs.getBigDecimal("delivery_num"));
			detail.setVolume(rs.getBigDecimal("volume"));
			detail.setWeight(rs.getBigDecimal("weight"));
			detail.setUserAddress(rs.getString("user_address"));
			detail.setDeliveryAmount(rs.getBigDecimal("delivery_amount"));
			detail.setInsiteTime(rs.getDate("insite_time"));
			detail.setAuditingTime(rs.getDate("auditing_time"));
			return detail;
		}
	}
	
	public List<FnDfBillDetail> queryFnDfBillDetailByCwb(String cwb) {
		String sql = "select * from fn_df_bill_detail where order_no = ?";
		return this.jdbcTemplate.query(sql, new BillDetailRowMapper(), cwb);
	}
}
