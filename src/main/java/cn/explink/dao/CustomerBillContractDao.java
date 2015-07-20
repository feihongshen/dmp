package cn.explink.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import cn.explink.domain.CustomerBillContract;
import cn.explink.domain.VO.CustomerBillContractVO;
import cn.explink.domain.VO.SerachCustomerBillContractVO;

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
			c.setRefuseMoney(rs.getBigDecimal("refuse_money"));
			c.setTotalCharge(rs.getBigDecimal("total_charge"));
			c.setRemark(rs.getString("remark"));
			c.setDateCreateBill(rs.getString("date_create_bill"));
			c.setDateVerificationBill(rs.getString("date_verification_bill"));
			c.setCwbOrderType(rs.getLong("cwb_order_type"));
			c.setDateState(rs.getLong("date_state"));
			c.setCwbs("cwbs");
		
			
			return c;
		}
	}
	
	public void EditBill(CustomerBillContractVO cbv){
		StringBuilder sb = new StringBuilder();
		String sql="update customerbillcontract set total_charge=?,corresponding_cwb_num=?,distribution_money=?,transfer_money=?,refuse_money=?";
		if(Long.valueOf(cbv.getBillState())>0){
			sb.append(",bill_state="+Long.valueOf(cbv.getBillState()));
		}
		sql+=sb.toString();
		this.jdbcTemplate.update(sql,cbv.getTotalCharge(),cbv.getCorrespondingCwbNum(),cbv.getDistributionMoney(),cbv.getTransferMoney(),cbv.getRefuseMoney());
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
	 * @param intPage 
	 * @param start 
	 */
	public List<CustomerBillContract> findCustomerBillContract(String billBatches,
			long billState,String crestartdate,
			String creenddate,String verificationstratdate,String verificationenddate,
			long customerId,long cwbOrderType,String condition,String sequence, int start, int number
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
				sb.append(" limit "+start+","+number);
				sql+=sb.toString();
				System.out.println(sql);
				return jdbcTemplate.query(sql, new CustomerBillContractmapper());
			}
	
	
	
	
	public void updateCustomerBillContract(String cwbs,String billBatches,long correspondingCwbNum,
			BigDecimal deliveryMoney, BigDecimal distributionMoney,
			BigDecimal refuseMoney, BigDecimal transferMoney, BigDecimal totalCharge
			
			){
				StringBuilder sb = new StringBuilder();
				String sql="update customerbillcontract set cwbs=?,corresponding_cwb_num=?,delivery_money=?,distribution_money=?,transfer_money=?,refuse_money=?,total_charge=?";				
				
				this.jdbcTemplate.update(sql,cwbs,correspondingCwbNum,deliveryMoney,distributionMoney,transferMoney,refuseMoney,totalCharge);
			}
	/**
	 * 
	 * @param billBatches
	 * @param billState
	 * @param crestartdate
	 * @param creenddate
	 * @param verificationstratdate
	 * @param verificationenddate
	 * @param customerId
	 * @param cwbOrderType
	 * @param condition
	 * @param sequence
	 * @return  查询到的数据数量
	 */
	public long findCustomerBillContractCount(String billBatches,
			long billState,String crestartdate,
			String creenddate,String verificationstratdate,String verificationenddate,
			long customerId,long cwbOrderType
			){
				StringBuilder sb = new StringBuilder();
				String sql="select count(1) from customerbillcontract where 1=1";
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
				/*sb.append(" ORDER BY '"+condition+"'");
				sb.append(" '"+sequence+"'");*/
				sql+=sb.toString();
				System.out.println(sql);
				return jdbcTemplate.queryForLong(sql);
			}
	
			/**
			 * 
			 * @param id 账单id
			 */
			public void removebill(long id) {
				String sql="delete from customerbillcontract where id="+id;
				this.jdbcTemplate.update(sql);
			}
			
			public CustomerBillContract findbill(long id) {
				String sql="select * from customerbillcontract where id="+id;
				return this.jdbcTemplate.queryForObject(sql,new CustomerBillContractmapper());
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
			 * @param dateState 
			 */
			public void addBill(String billBatches, long initbillState,
					long customerid, String dateRange, long correspondingCwbNum,
					BigDecimal deliveryMoney, BigDecimal distributionMoney,
					BigDecimal transferMoney, BigDecimal refuseMoney, BigDecimal totalCharge,
					String remark,String createBillDate,long cwbtype, long dateState,String cwbs) {
				String sql="insert into customerbillcontract(bill_batches,bill_state,customer_id,date_range,corresponding_cwb_num,delivery_money,distribution_money,transfer_money,refuse_money,total_charge,remark,date_create_bill,cwb_order_type,date_state,cwbs) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				this.jdbcTemplate.update(sql,billBatches,initbillState,customerid,dateRange,correspondingCwbNum,deliveryMoney,distributionMoney,transferMoney,refuseMoney,totalCharge,remark,createBillDate,cwbtype,dateState,cwbs);
			}
			//查出所有账单按照账单批次
			public CustomerBillContract datebillBatche(String BillBatches) {	
				String sql="select * from customerbillcontract where bill_batches='"+BillBatches+"'";
				CustomerBillContract cbc=null;
				try {
					
					cbc=jdbcTemplate.queryForObject(sql,new CustomerBillContractmapper());

					return cbc;
				} catch (DataAccessException e) {
					// TODO Auto-generated catch block
					return null;
				}
			}
			
			public CustomerBillContract datebillBatcheById(long id) {	
				String sql="select * from customerbillcontract where id=?";
				CustomerBillContract cbc=null;
				try {
					
					cbc=jdbcTemplate.queryForObject(sql,new CustomerBillContractmapper(),id);

					return cbc;
				} catch (DataAccessException e) {
					// TODO Auto-generated catch block
					return null;
				}
			}
			
			//查出所有账单
			public List<CustomerBillContract> dateAllbillBatche() {	
				String sql="select * from customerbillcontract";
				List<CustomerBillContract> cbc=null;
				try {
					
					cbc=jdbcTemplate.query(sql,new CustomerBillContractmapper());

					return cbc;
				} catch (DataAccessException e) {
					// TODO Auto-generated catch block
					return null;
				}
			}

			public CustomerBillContract findCustomerBillContractById(long id) {
				String sql="select * from customerbillcontract where id=?";
				CustomerBillContract cbc=null;
				try {
					
					cbc=jdbcTemplate.queryForObject(sql,new CustomerBillContractmapper(),id);

					return cbc;
				} catch (DataAccessException e) {
					// TODO Auto-generated catch block
					return null;
				}
			}
			
			
			public CustomerBillContract findCustomerBillContractByBillBatches(String Batches) {
				String sql="select * from customerbillcontract where bill_batches='"+Batches+"'";
				CustomerBillContract cbc=null;
				try {
					
					cbc=jdbcTemplate.queryForObject(sql,new CustomerBillContractmapper());

					return cbc;
				} catch (DataAccessException e) {
					// TODO Auto-generated catch block
					return null;
				}
			}
			
			private final class CustomerBillContractVOmapper implements RowMapper<SerachCustomerBillContractVO>{

				@Override
				public SerachCustomerBillContractVO mapRow(ResultSet rs,
						int rowNum) throws SQLException {
					SerachCustomerBillContractVO sv = new SerachCustomerBillContractVO();
					sv.setCwb(rs.getString("cwb"));
					sv.setCwbOrderType(rs.getString("cwb_order_type"));
					sv.setCwbstate(rs.getString("cwb_state"));
					sv.setDeliveryMoney(rs.getBigDecimal("delivery_money"));
					sv.setDistributionMoney(rs.getBigDecimal("distribution_money"));
					sv.setPaywayid(rs.getString("pay_way_id"));
					sv.setRefuseMoney(rs.getBigDecimal("refuse_money"));
					sv.setTotalCharge(rs.getBigDecimal("total_charge"));
					sv.setTransferMoney(rs.getBigDecimal("transfer_money"));
					sv.setBillBatches(rs.getString("bill_batches"));
					return sv;
				}
				
			}
			
			
			public void addBillVo(SerachCustomerBillContractVO s){
				String sql="insert into customerbillcontractvo(cwb,cwb_order_type,cwb_state,delivery_money,distribution_money,pay_way_id,refuse_money,total_charge,transfer_money,bill_batches) values(?,?,?,?,?,?,?,?,?,?)";
				this.jdbcTemplate.update(sql,s.getCwb(),s.getCwbOrderType(),s.getCwbstate(),s.getDeliveryMoney(),s.getDistributionMoney(),s.getPaywayid(),s.getRefuseMoney(),s.getTotalCharge(),s.getTransferMoney(),s.getBillBatches());
			}
			
			public List<SerachCustomerBillContractVO> findSerachCustomerBillContractVOByBillBatches(String BillBatches, int start, int number){
					String sql="select * from customerbillcontractvo where bill_batches=? limit "+start+","+number;
					return this.jdbcTemplate.query(sql,new CustomerBillContractVOmapper(),BillBatches);
			}
			
			public SerachCustomerBillContractVO findSerachCustomerBillContractVOByBillBatches(String cwb){
				String sql="select * from customerbillcontractvo where cwb='"+cwb+"'";
				return this.jdbcTemplate.queryForObject(sql,new CustomerBillContractVOmapper());
		}
			public long findSerachCustomerBillContractVOByBillBatchesCount(String BillBatches){
				String sql="select count(1) from customerbillcontractvo where bill_batches=?";
				return this.jdbcTemplate.queryForLong(sql,BillBatches);
			}
			
			public void removeSerachCustomerBillContractVOByCwb(String cwb){
				 String sql="delete from customerbillcontractvo where cwb='"+cwb+"'";
				 this.jdbcTemplate.update(sql);
			}
			
			public void removeSerachCustomerBillContractVOBybillbatches(String billbatches){
				 String sql="delete from customerbillcontractvo where bill_batches='"+billbatches+"'";
				 this.jdbcTemplate.update(sql);
			}

}
