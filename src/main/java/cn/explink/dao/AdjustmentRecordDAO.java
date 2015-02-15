package cn.explink.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.sf.json.JSONObject;

import org.junit.Test;
import org.neo4j.cypher.internal.compiler.v2_1.docbuilders.internalDocBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.explink.domain.AbnormalOrder;
import cn.explink.domain.AbnormalWriteBack;
import cn.explink.domain.AdjustmentRecord;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;
import cn.explink.enumutil.VerificationEnum;
import cn.explink.util.Page;

@Component
public class AdjustmentRecordDAO{
	private final class AdjustmentRecordRowMapper implements RowMapper<AdjustmentRecord> {
		@Override
		public AdjustmentRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
			AdjustmentRecord adjustmentRecord = new AdjustmentRecord();

			adjustmentRecord.setId(rs.getLong("id"));
			adjustmentRecord.setOrder_no(rs.getString("order_no"));
			adjustmentRecord.setBill_no(rs.getString("bill_no"));
			adjustmentRecord.setBill_id(rs.getLong("billid"));
			adjustmentRecord.setAdjust_bill_no(rs.getString("adjust_bill_no"));
			adjustmentRecord.setCustomer_id(rs.getLong("customer_id"));
			adjustmentRecord.setReceive_fee(rs.getBigDecimal("receive_fee"));
			adjustmentRecord.setRefund_fee(rs.getBigDecimal("refund_fee"));
			adjustmentRecord.setModify_fee(rs.getBigDecimal("modify_fee"));
			adjustmentRecord.setAdjust_amount(rs.getBigDecimal("adjust_amount"));
			adjustmentRecord.setRemark(rs.getString("remark"));
			adjustmentRecord.setCreator(rs.getString("creator"));
			adjustmentRecord.setCreate_time(rs.getString("create_time"));
			adjustmentRecord.setStatus(rs.getInt("status"));
			adjustmentRecord.setCheck_user(rs.getString("check_user"));
			adjustmentRecord.setCheck_time(rs.getString("check_time"));
			adjustmentRecord.setOrder_type(rs.getInt("order_type"));
			return adjustmentRecord;
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;
	public void creAdjustmentRecord(final AdjustmentRecord adjustmentRecord) {
		this.jdbcTemplate.update("insert into fn_adjustment_record (order_no,bill_no,adjust_bill_no,customer_id," + "receive_fee,refund_fee,modify_fee,adjust_amount,remark,creator,create_time,"
				+ "status,check_user,check_time,order_type) " + "values(?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?)",
				new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, adjustmentRecord.getOrder_no());
						ps.setString(2, adjustmentRecord.getBill_no());
						ps.setString(3, adjustmentRecord.getAdjust_bill_no());
						ps.setLong(4, adjustmentRecord.getCustomer_id());
						ps.setBigDecimal(5, adjustmentRecord.getReceive_fee());
						ps.setBigDecimal(6, adjustmentRecord.getRefund_fee());
						ps.setBigDecimal(7, adjustmentRecord.getModify_fee());
						ps.setBigDecimal(8, adjustmentRecord.getAdjust_amount());
						ps.setString(9, adjustmentRecord.getRemark());
						ps.setString(10, adjustmentRecord.getCreator());
						ps.setString(11, adjustmentRecord.getCreate_time());
						ps.setInt(12, adjustmentRecord.getStatus());
						ps.setString(13, adjustmentRecord.getCheck_user());
						ps.setString(14, adjustmentRecord.getCheck_time());
						ps.setInt(15, adjustmentRecord.getOrder_type());
					}

				});
	}
	
	/**
	 * 通过订单获取到对应订单的调整单
	 * @param cwbid
	 * @return
	 */
	public List<AdjustmentRecord> getAdjustmentRecordByCwb(String cwbid) {
		String sql = "select * from fn_adjustment_record where `order_no`=?  ";
		return this.jdbcTemplate.query(sql, new AdjustmentRecordRowMapper(), cwbid);
	}
	public void delAdjustmentRecord(String cwb) {
		String sql="DELETE FROM fn_adjustment_record WHERE order_no=? AND status="+VerificationEnum.WeiHeXiao.getValue();
		this.jdbcTemplate.update(sql, cwb);
	}
	/**
	 * 修改调整单方法
	 * @param adjustmentRecord
	 */
	public void updateAdjustmentRecord(AdjustmentRecord adjustmentRecord,long id){
		try {
			String sql="UPDATE fn_adjustment_record SET receive_fee=?,refund_fee=?,modify_fee=?,adjust_amount=?,remark=?,creator=?,create_time=? WHERE id=?";
			
			BigDecimal receive_fee=adjustmentRecord.getReceive_fee();
			BigDecimal refund_fee=adjustmentRecord.getRefund_fee();
			BigDecimal modify_fee=adjustmentRecord.getModify_fee();
			BigDecimal adjust_amount=adjustmentRecord.getAdjust_amount();
			String remark=adjustmentRecord.getRemark();
			String creator=adjustmentRecord.getCreator();
			String create_time=adjustmentRecord.getCreate_time();
			this.jdbcTemplate.update(sql,receive_fee,refund_fee,modify_fee,adjust_amount,remark,creator,create_time,id);	
		} catch (Exception e) {
			e.printStackTrace();
		}

	}	
}
