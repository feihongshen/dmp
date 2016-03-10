/**
 * 
 */
package cn.explink.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.orderflow.OrderNote;
import cn.explink.domain.orderflow.OrderTrack;

/**
 * @author Administrator
 *
 */
@Component
public class OrderTrackDAO {

	private final class OrderTrackRowMapper implements RowMapper<OrderNote> {
		@Override
		public OrderNote mapRow(ResultSet rs, int rowNum) throws SQLException {
			OrderNote note = new OrderNote();
			note.setOrderNo(rs.getString("order_no"));
			note.setTransOrderNo(rs.getString("transorder_no"));
			note.setOperationTime(rs.getString("operation_time"));
			note.setOperatorName(rs.getString("operator_name"));
			note.setOperationTrack(rs.getString("operation_track"));
	
			return note;
		}

	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 创建一条操作记录
	 * 
	 * @param of
	 * @return key
	 */
	public void creOrderTrack(final OrderNote tot) {
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
				PreparedStatement ps = null;
				ps = con.prepareStatement("insert into express_ops_carrier_order_track (order_no,transorder_no,operation_time,operator_name,operation_track) " + "values(?,?,?,?,?)");
				ps.setString(1, tot.getOrderNo());
				ps.setString(2, tot.getTransOrderNo());
				ps.setString(3,tot.getOperationTime());
				ps.setString(4, tot.getOperatorName());
				ps.setString(5, tot.getOperationTrack());
				return ps;
			}
		});
	}



	public List<OrderNote> getTranscwbOrderFlowByScanCwb(String transOrderNo, String orderNo) {
		//单号、运单号都为空，返回空集合
		if((null==orderNo||orderNo.isEmpty())&&(null==transOrderNo||transOrderNo.isEmpty())){
			return new ArrayList<OrderNote>();
		}
		String sql="select * from express_ops_carrier_order_track where ";
		//运单号为空,按订单号查询
		if(null==transOrderNo||transOrderNo.isEmpty()){
			sql+=" order_no= ? order by operation_time";
			return jdbcTemplate.query(sql,new OrderTrackRowMapper(),orderNo);
		}
		//订单号为空，则按运单号查询
		else if(null==orderNo||orderNo.isEmpty()){
			sql+=" transorder_no= ? order by operation_time";
			return jdbcTemplate.query(sql,new OrderTrackRowMapper(),transOrderNo);
		}
		//订单号和运单号均不为空
		else{
			sql+=" transorder_no= ? and order_no= ? order by operation_time";
			return jdbcTemplate.query(sql,new OrderTrackRowMapper(),transOrderNo,orderNo);
		}
		
	}
}

