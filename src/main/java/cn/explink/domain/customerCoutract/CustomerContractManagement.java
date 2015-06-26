/**
 *
 */
package cn.explink.domain.customerCoutract;

import java.util.List;

/**
 * 客户合同管理实体
 *
 * @author wangqiang
 */
public class CustomerContractManagement {
	private Long id;
	// 编号
	private String number;
	// 合同状态
	private Integer contractstatus;
	// 合同开始日期
	private String contractstartdate;
	// 合同结束日期
	private String contractenddate;
	// 客户id
	private Long customerid;
	// 甲方名称
	private String partyaname;
	// 乙方全称
	private String yifangquancheng;
	// 合同类型
	private Integer contracttype;
	// 货款结算周期
	private Integer loanssettlementcycle;
	// 货款结算方式
	private Integer Loansandsettlementway;
	// 其他合同商
	private String othercontractors;
	// 派费结算周期
	private Integer paifeisettlementcycle;
	// 派费结算方式
	private Integer paifeisettlementtype;
	// 是否有押金
	private Integer whetherhavedeposit;
	// 营销负责人
	private String marketingprincipal;
	// 发票类型
	private Integer invoicetype;
	// 税率
	private double taxrate;
	// 代收贷款银行
	private String collectionloanbank;
	// 代收贷款银行账户
	private String collectionloanbankaccount;
	// 费用银行
	private String expensebank;
	// 费用银行账户
	private String expensebankaccount;
	// 合同详细描述
	private String contractdescription;
	// 合同名称
	private String contractname;
	// 合同附件
	private String contractaccessory;
	// 押金支付日期
	private String depositpaymentdate;
	// 押金支付人
	private String depositpaymentperson;
	// 押金支付金额
	private String depositpaymentamount;
	// 押金收取人
	private String depositgatherperson;

	private String depositInformationStr;
	private List<DepositInformation> depositInformationList;

	/**
	 *
	 */
	public CustomerContractManagement() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumber() {
		return this.number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Integer getContractstatus() {
		return this.contractstatus;
	}

	public void setContractstatus(Integer contractstatus) {
		this.contractstatus = contractstatus;
	}

	public Long getCustomerid() {
		return this.customerid;
	}

	public void setCustomerid(Long customerid) {
		this.customerid = customerid;
	}

	public String getPartyaname() {
		return this.partyaname;
	}

	public void setPartyaname(String partyaname) {
		this.partyaname = partyaname;
	}

	public String getContractstartdate() {
		return this.contractstartdate;
	}

	public void setContractstartdate(String contractstartdate) {
		this.contractstartdate = contractstartdate;
	}

	public String getContractenddate() {
		return this.contractenddate;
	}

	public void setContractenddate(String contractenddate) {
		this.contractenddate = contractenddate;
	}

	public Integer getContracttype() {
		return this.contracttype;
	}

	public void setContracttype(Integer contracttype) {
		this.contracttype = contracttype;
	}

	public Integer getLoanssettlementcycle() {
		return this.loanssettlementcycle;
	}

	public void setLoanssettlementcycle(Integer loanssettlementcycle) {
		this.loanssettlementcycle = loanssettlementcycle;
	}

	public Integer getLoansandsettlementway() {
		return this.Loansandsettlementway;
	}

	public void setLoansandsettlementway(Integer loansandsettlementway) {
		this.Loansandsettlementway = loansandsettlementway;
	}

	public String getOthercontractors() {
		return this.othercontractors;
	}

	public void setOthercontractors(String othercontractors) {
		this.othercontractors = othercontractors;
	}

	public Integer getPaifeisettlementcycle() {
		return this.paifeisettlementcycle;
	}

	public void setPaifeisettlementcycle(Integer paifeisettlementcycle) {
		this.paifeisettlementcycle = paifeisettlementcycle;
	}

	public Integer getPaifeisettlementtype() {
		return this.paifeisettlementtype;
	}

	public void setPaifeisettlementtype(Integer paifeisettlementtype) {
		this.paifeisettlementtype = paifeisettlementtype;
	}

	public Integer getWhetherhavedeposit() {
		return this.whetherhavedeposit;
	}

	public void setWhetherhavedeposit(Integer whetherhavedeposit) {
		this.whetherhavedeposit = whetherhavedeposit;
	}

	public String getMarketingprincipal() {
		return this.marketingprincipal;
	}

	public void setMarketingprincipal(String marketingprincipal) {
		this.marketingprincipal = marketingprincipal;
	}

	public Integer getInvoicetype() {
		return this.invoicetype;
	}

	public void setInvoicetype(Integer invoicetype) {
		this.invoicetype = invoicetype;
	}

	public double getTaxrate() {
		return this.taxrate;
	}

	public void setTaxrate(double taxrate) {
		this.taxrate = taxrate;
	}

	public String getContractdescription() {
		return this.contractdescription;
	}

	public void setContractdescription(String contractdescription) {
		this.contractdescription = contractdescription;
	}

	public String getContractname() {
		return this.contractname;
	}

	public void setContractname(String contractname) {
		this.contractname = contractname;
	}

	public String getContractaccessory() {
		return this.contractaccessory;
	}

	public void setContractaccessory(String contractaccessory) {
		this.contractaccessory = contractaccessory;
	}

	public String getDepositpaymentdate() {
		return this.depositpaymentdate;
	}

	public void setDepositpaymentdate(String depositpaymentdate) {
		this.depositpaymentdate = depositpaymentdate;
	}

	public String getDepositpaymentperson() {
		return this.depositpaymentperson;
	}

	public void setDepositpaymentperson(String depositpaymentperson) {
		this.depositpaymentperson = depositpaymentperson;
	}

	public String getDepositpaymentamount() {
		return this.depositpaymentamount;
	}

	public void setDepositpaymentamount(String depositpaymentamount) {
		this.depositpaymentamount = depositpaymentamount;
	}

	public String getDepositgatherperson() {
		return this.depositgatherperson;
	}

	public void setDepositgatherperson(String depositgatherperson) {
		this.depositgatherperson = depositgatherperson;
	}

	public String getYifangquancheng() {
		return this.yifangquancheng;
	}

	public void setYifangquancheng(String yifangquancheng) {
		this.yifangquancheng = yifangquancheng;
	}

	public String getCollectionloanbank() {
		return this.collectionloanbank;
	}

	public void setCollectionloanbank(String collectionloanbank) {
		this.collectionloanbank = collectionloanbank;
	}

	public String getCollectionloanbankaccount() {
		return this.collectionloanbankaccount;
	}

	public void setCollectionloanbankaccount(String collectionloanbankaccount) {
		this.collectionloanbankaccount = collectionloanbankaccount;
	}

	public String getExpensebank() {
		return this.expensebank;
	}

	public void setExpensebank(String expensebank) {
		this.expensebank = expensebank;
	}

	public String getExpensebankaccount() {
		return this.expensebankaccount;
	}

	public void setExpensebankaccount(String expensebankaccount) {
		this.expensebankaccount = expensebankaccount;
	}

	public String getDepositInformationStr() {
		return depositInformationStr;
	}

	public void setDepositInformationStr(String depositInformationStr) {
		this.depositInformationStr = depositInformationStr;
	}

	public List<DepositInformation> getDepositInformationList() {
		return depositInformationList;
	}

	public void setDepositInformationList(List<DepositInformation> depositInformationList) {
		this.depositInformationList = depositInformationList;
	}

}
