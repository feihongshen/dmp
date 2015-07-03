package cn.explink.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import cn.explink.domain.CustomerBillContract;

@Repository
public class CustomerBillContractDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final class CustomerBillContractmapper implements RowMapper<CustomerBillContract>{

		@Override
		public CustomerBillContract mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			CustomerBillContract c = new CustomerBillContract();
			c.setId(rs.getLong("id"));
			c.setBillBatches(rs.getString("bill_batches"));
			c.setBillState(rs.getLong("bill_state"));
			c.setCustomerId(rs.getLong("customer_id"));
			c.setDateRange(rs.getString("date_range"));
			c.setCorrespondingCwbNum(rs.getLong("corresponding_cwb_num"));
			c.setDeliveryMoney(rs.getBigDecimal("delivery_money"));
			c.setDistributionMoney(rs.getBigDecimal("distribution_money"));
			c.setTransferMoney(rs.getBigDecimal("transfer_money"));
			c.setRefuseMoney(rs.getBigDecimal("transfer_money"));
			c.setTotalCharge(rs.getBigDecimal("total_charge"));
			c.setRemark(rs.getString("remark"));
			c.setDateCreateBill(rs.getString("date_create_bill"));
			c.setDateVerificationBill(rs.getString("date_verification_bill"));
			c.setCwbOrderType(rs.getLong("cwb_order_type"));
			
			return c;
		}
	}
	
	//根据查询条件查询账单
	/**
	 * 
	 * @param billBatches 批次号
	 * @param billState 账单状态
	 * @param crestartdate 创建时间 开始
	 * @param creenddate 创建时间 结束
	 * @param verificationstratdate 核销时间开始
	 * @param verificationenddate 核销时间结束
	 * @param customerId 客户
	 * @param cwbOrderType 订单类型
	 * @param condition 排序条件
	 * @param sequence  升降顺序
	 */
	public List<CustomerBillContract> findCustomerBillContract(String billBatches,
			long billState,String crestartdate,
			String creenddate,String verificationstratdate,String verificationenddate,
			long customerId,long cwbOrderType,String condition,String sequence
			){
				StringBuilder sb = new StringBuilder();
				String sql="select * from customerbillcontract where 1=1";
				if(!billBatches.equals("")){
					sb.append(" and bill_batches='"+billBatches+"'");
				}
				if(billState>0){
					sb.append(" and bill_state="+billState);
				}
				if(!crestartdate.equals("")&&!creenddate.equals("")){
					sb.append(" and date_create_bill>'"+crestartdate+"' and date_create_bill<'"+creenddate+"'");
				}
				if(!verificationstratdate.equals("")&&!verificationenddate.equals("")){
					sb.append(" and date_create_bill>'"+verificationstratdate+"' and date_create_bill<'"+verificationenddate+"'");
				}
				if(customerId>0){
					sb.append(" and customer_id="+customerId);
				}
				if(cwbOrderType>0){
					sb.append(" and cwb_order_type="+cwbOrderType);
				}
				
				sb.append(" ORDER BY '"+condition+"'");
				sb.append(" '"+sequence+"'");
				sql+=sb.toString();
				
				return jdbcTemplate.query(sql, new CustomerBillContractmapper());
			}
	
			/**
			 * 
			 * @param id 账单id
			 */
			public void removebill(long id) {
				String sql="delete * from customerbillcontract where id="+id;
				this.jdbcTemplate.update(sql);
			}
			
			/**
			 * 
			 * @param billBatches
			 * @param initbillState
			 * @param id
			 * @param dateRange
			 * @param correspondingCwbNum
			 * @param deliveryMoney
			 * @param distributionMoney
			 * @param transferMoney
			 * @param refuseMoney
			 * @param totalCharge
			 * @param remark
			 */
			public void addBill(String billBatches, long initbillState,
					long id, String dateRange, long correspondingCwbNum,
					long deliveryMoney, long distributionMoney,
					long transferMoney, long refuseMoney, long totalCharge,
					String remark) {
				String sql="insert into customerbillcontract(bill_batches,billState,id,dateRange,correspondingCwbNum,deliveryMoney,distributionMoney,transferMoney,refuseMoney,totalCharge,remark) values(?,?,?,?,?,?,?,?,?,?,?)";
				this.jdbcTemplate.update(sql,billBatches,initbillState,id,dateRange,correspondingCwbNum,deliveryMoney,distributionMoney,transferMoney,refuseMoney,totalCharge,remark);
			}
			//查出所有账单按照
			public String datebillBatche() {
				String sql="select * from customerbillcontract order by bill_batches desc";
				List<CustomerBillContract> list=jdbcTemplate.query(sql,new CustomerBillContractmapper());
				String BillBatche=list.get(0).getBillBatches();
				return BillBatche;
			}

}
