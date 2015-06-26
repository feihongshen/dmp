/**
 *
 */
package cn.explink.dao.contractManagement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.explink.domain.customerCoutract.CustomerContractManagement;
import cn.explink.domain.customerCoutract.DepositInformation;
import cn.explink.enumutil.coutracManagementEnum.ContractStateEnum;

/**
 * @author wangqiang
 */
@Component
public class CustomerContractDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final class CustomerContractRowMapper implements RowMapper<CustomerContractManagement> {
		@Override
		public CustomerContractManagement mapRow(ResultSet rs, int rowNum) throws SQLException {
			CustomerContractManagement customerContractManagement = new CustomerContractManagement();
			customerContractManagement.setId(rs.getLong("id"));
			customerContractManagement.setNumber(rs.getString("number"));
			customerContractManagement.setContractstatus(rs.getInt("contractstatus"));
			customerContractManagement.setContractstartdate(rs.getString("contractstartdate"));
			customerContractManagement.setContractenddate(rs.getString("contractenddate"));
			customerContractManagement.setCustomerid(rs.getLong("customerid"));
			customerContractManagement.setPartyaname(rs.getString("partyaname"));
			customerContractManagement.setYifangquancheng(rs.getString("yifangquancheng"));
			customerContractManagement.setContracttype(rs.getInt("contracttype"));
			customerContractManagement.setLoanssettlementcycle(rs.getInt("loanssettlementcycle"));
			customerContractManagement.setLoansandsettlementway(rs.getInt("Loansandsettlementway"));
			customerContractManagement.setOthercontractors(rs.getString("othercontractors"));
			customerContractManagement.setPaifeisettlementcycle(rs.getInt("paifeisettlementcycle"));
			customerContractManagement.setPaifeisettlementtype(rs.getInt("paifeisettlementtype"));
			customerContractManagement.setWhetherhavedeposit(rs.getInt("whetherhavedeposit"));
			customerContractManagement.setMarketingprincipal(rs.getString("marketingprincipal"));
			customerContractManagement.setInvoicetype(rs.getInt("invoicetype"));
			customerContractManagement.setTaxrate(rs.getDouble("taxrate"));
			customerContractManagement.setCollectionloanbank(rs.getString("collectionloanbank"));
			customerContractManagement.setCollectionloanbankaccount(rs.getString("collectionloanbankaccount"));
			customerContractManagement.setExpensebank(rs.getString("expensebank"));
			customerContractManagement.setExpensebankaccount(rs.getString("expensebankaccount"));
			customerContractManagement.setContractdescription(rs.getString("contractdescription"));
			customerContractManagement.setContractname(rs.getString("contractname"));
			customerContractManagement.setContractaccessory(rs.getString("contractaccessory"));
			customerContractManagement.setDepositpaymentdate(rs.getString("depositpaymentdate"));
			customerContractManagement.setDepositpaymentperson(rs.getString("depositpaymentperson"));
			customerContractManagement.setDepositpaymentamount(rs.getString("depositpaymentamount"));
			customerContractManagement.setDepositgatherperson(rs.getString("depositgatherperson"));
			return customerContractManagement;
		}

	}

	private final class DepositInformationRowMapper implements RowMapper<DepositInformation> {
		@Override
		public DepositInformation mapRow(ResultSet rs, int rowNum) throws SQLException {
			DepositInformation depositInformation = new DepositInformation();
			depositInformation.setId(rs.getLong("id"));
			depositInformation.setContractid(rs.getLong("contractid"));
			depositInformation.setDepositreturndate(rs.getString("depositreturndate"));
			depositInformation.setDepositreturnsum(rs.getString("depositreturnsum"));
			depositInformation.setPayee(rs.getString("payee"));
			depositInformation.setRefundpeople(rs.getString("refundpeople"));
			depositInformation.setRemarks(rs.getString("remarks"));
			return depositInformation;
		}
	}

	/**
	 * 新增合同
	 *
	 * @param createContract
	 * @return
	 */
	public void createContract(CustomerContractManagement contractManagement) {
		String sql = "insert into express_set_customer_contract_management(number,contractstatus,contractstartdate,contractenddate,customerid,partyaname,yifangquancheng,"
				+ "contracttype,loanssettlementcycle,Loansandsettlementway,othercontractors,paifeisettlementcycle,paifeisettlementtype,whetherhavedeposit,marketingprincipal,"
				+ "invoicetype,taxrate,collectionloanbank,collectionloanbankaccount,expensebank,expensebankaccount,contractdescription,depositpaymentdate,depositpaymentperson,"
				+ "depositpaymentamount,depositgatherperson,contractaccessory) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		this.jdbcTemplate.update(sql, contractManagement.getNumber(), contractManagement.getContractstatus(), contractManagement.getContractstartdate(), contractManagement.getContractenddate(),
				contractManagement.getCustomerid(), contractManagement.getPartyaname(), contractManagement.getYifangquancheng(), contractManagement.getContracttype(),
				contractManagement.getLoanssettlementcycle(), contractManagement.getLoansandsettlementway(), contractManagement.getOthercontractors(), contractManagement.getPaifeisettlementcycle(),
				contractManagement.getPaifeisettlementtype(), contractManagement.getWhetherhavedeposit(), contractManagement.getMarketingprincipal(), contractManagement.getInvoicetype(),
				contractManagement.getTaxrate(), contractManagement.getCollectionloanbank(), contractManagement.getCollectionloanbankaccount(), contractManagement.getExpensebank(),
				contractManagement.getExpensebankaccount(), contractManagement.getContractdescription(), contractManagement.getDepositpaymentdate(), contractManagement.getDepositpaymentperson(),
				contractManagement.getDepositpaymentamount(), contractManagement.getDepositgatherperson(), contractManagement.getContractaccessory());
	}

	// 删除字表记录
	public void deleteContractId(Long id) {
		String sql = "delete from express_set_deposit_information where contractid='" + id + "'";
		this.jdbcTemplate.update(sql);
	}

	// 添加字表记录
	public void createContractDetail(DepositInformation depositInformation) {
		String str = "insert into express_set_deposit_information(contractid,depositreturndate,depositreturnsum,refundpeople,payee,remarks) values(?,?,?,?,?,?)";
		this.jdbcTemplate.update(str, depositInformation.getContractid(), depositInformation.getDepositreturndate(), depositInformation.getDepositreturnsum(), depositInformation.getRefundpeople(),
				depositInformation.getPayee(), depositInformation.getRemarks());
	}

	// 查询字表中指定的记录
	public List<DepositInformation> getContractDetailListByContractId(Long id) {
		String sql = "select * from express_set_deposit_information where contractid='" + id + "'";
		return this.jdbcTemplate.query(sql, new DepositInformationRowMapper());
	}

	/**
	 * 删除合同
	 */
	public void deleteContract(Long id) {
		String deleteContractSql = "delete from express_set_customer_contract_management where id='" + id + "'";
		String deleteDepositSql = "delete from express_set_deposit_information where contractid='" + id + "'";
		this.jdbcTemplate.update(deleteDepositSql);
		this.jdbcTemplate.update(deleteContractSql);
	}

	public void alterSuspend(Long id) {
		String sql = "update express_set_customer_contract_management set  contractstatus='" + ContractStateEnum.HeTongZhongZhi.getValue() + "' where id='" + id + "'";
		this.jdbcTemplate.update(sql);
	}

	/**
	 * 修改合同信息
	 */
	public void modificationContract(CustomerContractManagement contractManagement) {
		StringBuffer updatesql = new StringBuffer("update express_set_customer_contract_management set");
		final List<Object> param = new ArrayList<Object>();
		if (contractManagement.getContractstatus() != null) {
			updatesql.append(" contractstatus = ?");
			param.add(contractManagement.getContractstatus());
		}
		if ((contractManagement.getPartyaname() != null) && (contractManagement.getPartyaname() != "")) {
			updatesql.append(", partyaname = ?");
			param.add(contractManagement.getPartyaname());
		}
		if ((contractManagement.getYifangquancheng() != null) && (contractManagement.getYifangquancheng() != "")) {
			updatesql.append(", yifangquancheng = ? ");
			param.add(contractManagement.getYifangquancheng());
		}
		if ((contractManagement.getOthercontractors() != null) && (contractManagement.getOthercontractors() != "")) {
			updatesql.append(", othercontractors= ?");
			param.add(contractManagement.getOthercontractors());
		}
		if (contractManagement.getLoanssettlementcycle() != null) {
			updatesql.append(", loanssettlementcycle = ?");
			param.add(contractManagement.getLoanssettlementcycle());
		}
		if (contractManagement.getLoansandsettlementway() != null) {
			updatesql.append(", Loansandsettlementway = ?");
			param.add(contractManagement.getLoansandsettlementway());
		}
		if (contractManagement.getPaifeisettlementcycle() != null) {
			updatesql.append(", paifeisettlementcycle = ?");
			param.add(contractManagement.getPaifeisettlementcycle());
		}
		if (contractManagement.getPaifeisettlementtype() != null) {
			updatesql.append(", paifeisettlementtype = ?");
			param.add(contractManagement.getPaifeisettlementtype());
		}
		if ((contractManagement.getMarketingprincipal() != null) && (contractManagement.getMarketingprincipal() != "")) {
			updatesql.append(", marketingprincipal= ?");
			param.add(contractManagement.getMarketingprincipal());
		}
		if (contractManagement.getInvoicetype() != null) {
			updatesql.append(", invoicetype= ?");
			param.add(contractManagement.getInvoicetype());
		}
		if (contractManagement.getTaxrate() != 0) {
			updatesql.append(", taxrate= ?");
			param.add(contractManagement.getTaxrate());
		}
		if ((contractManagement.getCollectionloanbank() != null) && (contractManagement.getCollectionloanbank() != "")) {
			updatesql.append(", collectionloanbank= ?");
			param.add(contractManagement.getCollectionloanbank());
		}
		if ((contractManagement.getCollectionloanbankaccount() != null) && (contractManagement.getCollectionloanbankaccount() != "")) {
			updatesql.append(", collectionloanbankaccount= ?");
			param.add(contractManagement.getCollectionloanbankaccount());
		}
		if ((contractManagement.getExpensebank() != null) && (contractManagement.getExpensebank() != "")) {
			updatesql.append(", expensebank= ?");
			param.add(contractManagement.getExpensebank());
		}
		if ((contractManagement.getExpensebankaccount() != null) && (contractManagement.getExpensebankaccount() != "")) {
			updatesql.append(", expensebankaccount = ?");
			param.add(contractManagement.getExpensebankaccount());
		}
		if ((contractManagement.getContractdescription() != null) && (contractManagement.getContractdescription() != "")) {
			updatesql.append(", contractdescription = ? ");
			param.add(contractManagement.getContractdescription());
		}
		if ((contractManagement.getDepositgatherperson() != null) && (contractManagement.getDepositgatherperson() != "")) {
			updatesql.append(", depositgatherperson = ?");
			param.add(contractManagement.getDepositgatherperson());
		}

		updatesql.append(" where id=" + contractManagement.getId());
		this.jdbcTemplate.update(updatesql.toString(), new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				for (int i = 0; i < param.size(); i++) {
					ps.setObject(i + 1, param.get(i));
				}
			}
		});
	}

	/**
	 * 查询结果
	 *
	 * @param page
	 * @param contractManagement
	 * @param createStatrtTime
	 * @param createEndTime
	 * @param overStartTime
	 * @param overEndTime
	 * @param sort
	 * @param method
	 * @return
	 */
	public List<CustomerContractManagement> getCustomerContractList(CustomerContractManagement contractManagement, String createStatrtTime, String createEndTime, String overStartTime,
			String overEndTime, String sort, String method) {
		StringBuffer selectSql = new StringBuffer("SELECT * FROM express_set_customer_contract_management WHERE 1=1 ");
		String sql = this.getCustomerContractByPageWhereSql(selectSql.toString(), contractManagement, createStatrtTime, createEndTime, overStartTime, overEndTime, sort, method);
		sql = selectSql.toString() + sql;
		List<CustomerContractManagement> list = this.jdbcTemplate.query(sql, new CustomerContractRowMapper());
		return list;
	}

	/**
	 * 查询条件
	 *
	 * @param sql
	 * @param contractManagement
	 * @param createStatrtTime
	 * @param createEndTime
	 * @param overStartTime
	 * @param overEndTime
	 * @param sort
	 * @param method
	 * @return
	 */
	private String getCustomerContractByPageWhereSql(String sql, CustomerContractManagement contractManagement, String createStatrtTime, String createEndTime, String overStartTime,
			String overEndTime, String sort, String method) {
		StringBuffer whereSql = new StringBuffer();
		if (contractManagement != null) {
			if ((contractManagement.getId() != null) && (contractManagement.getId() != 0)) {
				whereSql.append(" and id=" + contractManagement.getId());
			}
			if ((contractManagement.getNumber() != null) && (contractManagement.getNumber() != "")) {
				whereSql.append(" and number like '%" + contractManagement.getNumber() + "%'");
			}
			if (contractManagement.getContractstatus() == null) {
				whereSql.append(" and contractstatus =" + contractManagement.getContractstatus());
			}
			if ((contractManagement.getCustomerid() != null) && (contractManagement.getCustomerid() != 0)) {
				whereSql.append(" and customerid =" + contractManagement.getCustomerid());
			}
			if ((contractManagement.getPartyaname() != null) && (contractManagement.getPartyaname() != "")) {
				whereSql.append(" and partyaname like '%" + contractManagement.getPartyaname() + "%'");
			}
			if ((contractManagement.getMarketingprincipal() != null) && (contractManagement.getMarketingprincipal() != "")) {
				whereSql.append(" and marketingprincipal like '%" + contractManagement.getMarketingprincipal() + "%'");
			}
			if ((contractManagement.getOthercontractors() != "") && (contractManagement.getOthercontractors() != null)) {
				whereSql.append(" and othercontractors like '%" + contractManagement.getOthercontractors() + "%'");
			}
			if ((contractManagement.getContractdescription() != null) && (contractManagement.getContractdescription() != "")) {
				whereSql.append(" and contractdescription like '%" + contractManagement.getContractdescription() + "%'");
			}
			if ((contractManagement.getLoansandsettlementway() != null) && (contractManagement.getLoansandsettlementway() != 0)) {
				whereSql.append(" and Loansandsettlementway=" + contractManagement.getLoansandsettlementway());
			}
			if ((createStatrtTime != null) && (createStatrtTime != "") && ((createEndTime != null) && (createEndTime != ""))) {
				whereSql.append(" and '" + createStatrtTime + "'< contractstartdate < '" + createEndTime + "'");
			}
			if ((overStartTime != null) && ((overEndTime != null) & (overStartTime != "")) && (overEndTime != "")) {
				whereSql.append(" and '" + overStartTime + "'< contractenddate <'" + overEndTime + "'");
			}
			if ((contractManagement.getWhetherhavedeposit() != null) && (contractManagement.getWhetherhavedeposit() != 0)) {
				whereSql.append(" and whetherhavedeposit=" + contractManagement.getWhetherhavedeposit());
			}
			if ((sort != null) && (sort != "") && (method != null) && (method != "")) {
				StringBuffer orderBuffer = new StringBuffer();
				orderBuffer.append(" order by " + sort + "  " + method);
				String sqlString = whereSql.append(orderBuffer).toString();
				return sqlString;
			}
		}
		String sqlString = whereSql.toString();
		return sqlString;
	}

	/**
	 * 根据id查询所有记录
	 */
	public List<CustomerContractManagement> queryByid(Long id) {
		String sql = "select * from express_set_customer_contract_management where id = " + id;
		return this.jdbcTemplate.query(sql, new CustomerContractRowMapper());
	}

	public List<CustomerContractManagement> getMaxNumber() {
		String sql = "select * from express_set_customer_contract_management order by number desc ";
		return this.jdbcTemplate.query(sql, new CustomerContractRowMapper());
	}

}
