/**
 *
 */
package cn.explink.domain.customerCoutract;

/**
 * 押金信息实体
 *
 * @author wangqiang
 */
public class DepositInformation {
	//
	private Long id;
	// 合同表主键
	private Long contractid;
	// 押金退还日期
	private String depositreturndate;
	// 押金退还金额
	private String depositreturnsum;
	// 退款人
	private String refundpeople;
	// 收款人
	private String payee;
	// 备注
	private String remarks;

	/**
	 *
	 */
	public DepositInformation() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getContractid() {
		return this.contractid;
	}

	public void setContractid(Long contractid) {
		this.contractid = contractid;
	}

	public String getDepositreturndate() {
		return this.depositreturndate;
	}

	public void setDepositreturndate(String depositreturndate) {
		this.depositreturndate = depositreturndate;
	}

	public String getDepositreturnsum() {
		return this.depositreturnsum;
	}

	public void setDepositreturnsum(String depositreturnsum) {
		this.depositreturnsum = depositreturnsum;
	}

	public String getRefundpeople() {
		return this.refundpeople;
	}

	public void setRefundpeople(String refundpeople) {
		this.refundpeople = refundpeople;
	}

	public String getPayee() {
		return this.payee;
	}

	public void setPayee(String payee) {
		this.payee = payee;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
