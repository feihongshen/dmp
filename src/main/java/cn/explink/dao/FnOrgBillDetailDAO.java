package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.FnOrgBillDetail;
/**
 * 账单明细表DAO
 * @author 140624
 *
 */
@Component
public class FnOrgBillDetailDAO{
	private final class FnOrgBillDetailRowMapper implements RowMapper<FnOrgBillDetail> 	{
		/* (non-Javadoc)
		 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
		 */
		@Override
		public FnOrgBillDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			FnOrgBillDetail fnOrgBillDetail=new FnOrgBillDetail();

			fnOrgBillDetail.setId(rs.getLong("id"));
			fnOrgBillDetail.setBillId(rs.getLong("bill_id"));
			fnOrgBillDetail.setOrderNo(rs.getString("order_no"));
			fnOrgBillDetail.setOrderId(rs.getString("order_id"));
			fnOrgBillDetail.setStatus(rs.getInt("status"));
			fnOrgBillDetail.setPayMethod(rs.getInt("pay_method"));
			fnOrgBillDetail.setPicker(rs.getString("picker"));
			fnOrgBillDetail.setGoodsAmount(rs.getBigDecimal("goods_amount"));
			fnOrgBillDetail.setVerifyAmount(rs.getBigDecimal("verify_amount"));
			fnOrgBillDetail.setRechargesId(rs.getLong("recharges_id"));
			fnOrgBillDetail.setSignTime(rs.getString("sign_time"));
			fnOrgBillDetail.setRemark(rs.getString("remark"));
			
			return fnOrgBillDetail;
		}
	}
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 通过订单获取到对应订单的调整单
	 * @param cwbid
	 * @return
	 */
	public List<FnOrgBillDetail> getFnOrgBillDetailByCwb(String cwbid) {
		String sql = "select * from `fn_org_bill_detail` where `order_no`=?  ";
		return this.jdbcTemplate.query(sql, new FnOrgBillDetailRowMapper(), cwbid);
	}
}
