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

import cn.explink.domain.FnDfAdjustmentRecord;

@Component
public class FnDfAdjustmentRecordDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class AdjustmentRecordRowMapper implements
			RowMapper<FnDfAdjustmentRecord> {
		@Override
		public FnDfAdjustmentRecord mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			FnDfAdjustmentRecord adjustmentRecord = new FnDfAdjustmentRecord();
			adjustmentRecord.setAdj_id(rs.getLong("adj_id"));
			adjustmentRecord.setOrder_no(rs.getString("order_no"));
			adjustmentRecord.setBill_no(rs.getString("bill_no"));
			adjustmentRecord.setAdjust_bill_no(rs.getString("adjust_bill_no"));
			adjustmentRecord.setCustomer_id(rs.getLong("customer_id"));
			adjustmentRecord
					.setAdjust_amount(rs.getBigDecimal("adjust_amount"));
			adjustmentRecord.setRemark(rs.getString("remark"));
			adjustmentRecord.setCreator(rs.getString("creator"));
			adjustmentRecord.setCreate_time(rs.getString("create_time"));
			adjustmentRecord.setAdj_state(rs.getInt("adj_state"));
			adjustmentRecord.setOrderType(rs.getInt("order_type"));
			return adjustmentRecord;
		}
	}

	public void creAdjustmentRecord(final FnDfAdjustmentRecord adjustmentRecord) {
		this.jdbcTemplate
				.update("insert into fn_df_adjustment_record (order_no,org_id,bill_no,adjust_bill_no,customer_id,"
						+ "adjust_amount,remark,creator,create_time,"
						+ "adj_state,order_type) " + "values(?,?,?,?,?,?,?,?,?,?,?)",
						new PreparedStatementSetter() {
							@Override
							public void setValues(PreparedStatement ps)
									throws SQLException {
								ps.setString(1, adjustmentRecord.getOrder_no());
								ps.setLong(2, adjustmentRecord.getOrg_id());
								ps.setString(3, adjustmentRecord.getBill_no());
								ps.setString(4,adjustmentRecord.getAdjust_bill_no());
								ps.setLong(5, adjustmentRecord.getCustomer_id());
								ps.setBigDecimal(6,adjustmentRecord.getAdjust_amount());
								ps.setString(7, adjustmentRecord.getRemark());
								ps.setString(8, adjustmentRecord.getCreator());
								ps.setString(9,adjustmentRecord.getCreate_time());
								ps.setInt(10, adjustmentRecord.getAdj_state());
								ps.setInt(11, adjustmentRecord.getOrderType());
							}
						});
	}

	/**
	 * 通过订单获取到对应订单的调整单
	 * 
	 * @param cwbid
	 * @return
	 */
	public List<FnDfAdjustmentRecord> getFnDfAdjustmentRecordByCwbs(String cwbs) {
		String sql = "select * from fn_df_adjustment_record where order_no in ( '"
				+ cwbs + "')";
		return this.jdbcTemplate.query(sql, new AdjustmentRecordRowMapper());
	}

	// FnDfAdjustStateEnum
	public int delAdjustmentRecord(String cwb) {
		String sql = "DELETE FROM fn_df_adjustment_record WHERE order_no= ? ";
		return this.jdbcTemplate.update(sql, cwb);
	}

	public int delAdjustmentRecords(String cwbs) {
		String sql = "DELETE FROM fn_df_adjustment_record WHERE order_no in ('"
				+ cwbs + "')";
		return this.jdbcTemplate.update(sql);
	}

	public int queryBillDetail(String cwb) {
		String sql = "select count(*) from fn_df_bill_detail where order_no = ?";
		return this.jdbcTemplate.queryForInt(sql, cwb);
	}
}
