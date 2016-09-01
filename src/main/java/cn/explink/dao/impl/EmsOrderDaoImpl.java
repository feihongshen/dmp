package cn.explink.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.vipshop.mercury.util.DateUtil;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.dao.IEmsOrderDao;
import cn.explink.domain.EmsOrderPO;
import cn.explink.domain.Field;
import cn.explink.domain.QueryCondition;
import cn.explink.enumutil.CwbStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.OrderTypeEnum;
import cn.explink.util.SecurityUtil;
import cn.explink.util.Tools;

@Repository("emsOrderDao")
public class EmsOrderDaoImpl implements IEmsOrderDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate ;
	
	private final class  EmsOrderPOMapper implements RowMapper<EmsOrderPO>{

		@Override
		public EmsOrderPO mapRow(ResultSet rs, int rowNum) throws SQLException {
			EmsOrderPO emsOrderPO = new EmsOrderPO() ;
			emsOrderPO.setOrderNumber(rs.getString("order_number"));
			emsOrderPO.setOrderType(rs.getInt("order_type"));
			emsOrderPO.setOrderTypeDesc(OrderTypeEnum.getByValue(rs.getInt("order_type")));
			emsOrderPO.setOrderStatus(rs.getInt("order_status"));
			emsOrderPO.setOrderStatusDesc(CwbStateEnum.getTextByValue(rs.getInt("order_status")));
			emsOrderPO.setOrderCurrentStatus(rs.getInt("order_current_status"));
			FlowOrderTypeEnum orderTypeEnum = FlowOrderTypeEnum.getText(rs.getInt("order_current_status")) ;
			if(orderTypeEnum != null){
				emsOrderPO.setOrderCurrentStatusDesc(orderTypeEnum.getText());
			}
			emsOrderPO.setBranchName(rs.getString("brance_name"));
			String deliveryTime = rs.getString("delivery_time") ;
			if(!StringUtils.isEmpty(deliveryTime)){
				deliveryTime =  deliveryTime.substring(0, 19);
			}
			emsOrderPO.setDeliveryTime(deliveryTime);
			emsOrderPO.setDeliveryCustomer(rs.getString("delivery_customer"));
			emsOrderPO.setRecipientAddress(rs.getString("recipient_address"));
			emsOrderPO.setRecipientMobile(SecurityUtil.getInstance().decrypt(rs.getString("recipient_mobile")));
			return emsOrderPO;
		}
		
	}

	/**
	 * 获取未推送EMS订单的记录总数
	 * @param qc
	 * @return
	 */
	@Override
	public int getEmsUnpushOrderCount(QueryCondition qc) {
		StringBuffer sql = new StringBuffer("select count(de.cwb)") ;
		sql.append("  from express_ops_cwb_detail de ,express_set_customer_info info ,express_set_branch br ") ;
		sql.append(" where info.customerid = de.customerid ") ;
		sql.append(" and de.deliverybranchid = br.branchid ") ;
		sql.append(" and br.branchid = ").append(B2cEnum.EMS.getKey()) ;
		sql.append(" and case when de.ismpsflag = 1 and info.mpsswitch != 0 then de.ismpsflag = 1 ") ;
		sql.append(" else de.receivablefee > 0 and  de.shouldfare > 0 end  ") ;
		sql.append(" and de.state = 1 ") ;
		sql.append(this.setEmsUnpushOrderSqlCondition(qc)) ;
		return this.jdbcTemplate.queryForInt(sql.toString());
	}
	
	

	/**
	 * 获取未推送EMS订单的记录
	 * @param qc
	 * @return
	 */
	@Override
	public List<EmsOrderPO> queryEmsUnpushOrder(QueryCondition qc) {
		StringBuffer sql = new StringBuffer("select de.cwb as order_number, de.cwbordertypeid as order_type ,de.cwbstate as order_status ") ;
		sql.append(" , de.flowordertype as order_current_status , br.branchname as brance_name ,  de.emaildate as delivery_time ") ;
		sql.append(" , info.customername as delivery_customer , de.consigneeaddress as recipient_address ,  de.consigneemobile as recipient_mobile ") ;
		sql.append("  from express_ops_cwb_detail de ,express_set_customer_info info ,express_set_branch br ") ;
		sql.append(" where info.customerid = de.customerid ") ;
		sql.append(" and de.deliverybranchid = br.branchid ") ;
		sql.append(" and br.branchid = ").append(B2cEnum.EMS.getKey()) ;
		sql.append(" and case when de.ismpsflag = 1 and info.mpsswitch != 0 then de.ismpsflag = 1 ") ;
		sql.append(" else de.receivablefee > 0 and  de.shouldfare > 0 end  ") ;
		sql.append(" and de.state = 1 ") ;
		sql.append(this.setEmsUnpushOrderSqlCondition(qc)) ;
		sql.append(" limit ? , ? ") ;
		return this.jdbcTemplate.query(sql.toString(), new EmsOrderPOMapper() , (qc.getRealPage() - 1) * qc.getPageSize() , qc.getPageSize());
	}
	
	private String setEmsUnpushOrderSqlCondition(QueryCondition qc) {
		StringBuffer sql = new StringBuffer() ;
		for (Field field : qc.getConditions()) {
			String fp = field.getFieldParam();
			String fv = field.getFieldValue().toString();
			 if("customer_ids".equals(fp) && !Tools.isEmpty(fv)){
				sql.append(" and de.customerid in (").append(fv).append(")") ;
			}else if("order_type".equals(fp) && !cn.explink.util.Tools.isEmpty(fv)){
				sql.append(" and de.cwbordertypeid = ").append(fv) ;
			}else if("start_time".equals(fp) && !cn.explink.util.Tools.isEmpty(fv)){
				sql.append(" and de.emaildate >= '").append(fv).append("'") ;
			}else if("end_time".equals(fp) && !cn.explink.util.Tools.isEmpty(fv)){
				sql.append(" and de.emaildate <= '").append(fv).append("'") ;
			}
		}
		return sql.toString();
	}

}
