package cn.explink.dao.impl;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import cn.explink.dao.interfac.IBranceReportAdjustDao;
import cn.explink.domain.BranceReportAdjustPO;

@Repository("branceReportAdjustDao")
public class BranceReportAdjustDaoImpl implements IBranceReportAdjustDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate ;
	
	private static final String TABLE_NAME = "fn_brance_report_adjust" ;
	
	/**
	 * 添加应付调整单明细记录
	 * @param adjustPO
	 */
	@Override
	public void addBranceReportAdjust(final BranceReportAdjustPO adjustPO) {
		StringBuffer sql = new StringBuffer("insert into ") ;
		sql.append(TABLE_NAME) ;
		sql.append("(customer_id,order_number,order_type,order_status,operate_status,branch_id") ;
		sql.append(",receivable_amount,pay_amount,pay_type,adjust_amount,create_time)") ;
		sql.append(" values(?,?,?,?,?,?,?,?,?,?,?)") ;
		this.jdbcTemplate.update(sql.toString() , new PreparedStatementSetter() {		
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, adjustPO.getCustomerId() == null ? 0 : adjustPO.getCustomerId());
				ps.setString(2 , !StringUtils.isEmpty(adjustPO.getOrderNumber()) ? adjustPO.getOrderNumber() : "") ;
				ps.setInt(3 , adjustPO.getOrderType());
				ps.setLong(4, adjustPO.getOrderStatus());
				ps.setLong(5, adjustPO.getOperateStatus());
				ps.setLong(6, adjustPO.getBranchId() == null ? 0 : adjustPO.getBranchId());
				ps.setBigDecimal(7, adjustPO.getReceivableAmount() != null ? adjustPO.getReceivableAmount() : BigDecimal.ZERO);
				ps.setBigDecimal(8, adjustPO.getPayAmount() != null ? adjustPO.getPayAmount() : BigDecimal.ZERO);
				ps.setInt(9, adjustPO.getPayType());
				ps.setBigDecimal(10, adjustPO.getAdjustAmount());
				ps.setString(11, !StringUtils.isEmpty(adjustPO.getCreateTime()) ? adjustPO.getCreateTime() : cn.explink.util.DateTimeUtil.getNowTime());
			}
		}) ;
	}

}
