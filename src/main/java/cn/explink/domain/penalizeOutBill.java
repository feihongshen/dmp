/**
 *
 */
package cn.explink.domain;

import java.math.BigDecimal;
import java.util.List;

/**
 * 对外赔付账单实体
 *
 * @author wangqiang
 */
public class penalizeOutBill {
	private Integer id;
	// 账单批次
	private String billbatches;
	// 状态状态
	private Integer billstate;
	// 客户id
	private String customerid;
	// 客户名称
	private String customername;
	// 赔付单号
	private String compensateodd;
	// 赔付大类
	private Integer compensatebig;
	// 赔付小类
	private Integer compensatesmall;
	// 赔付金额
	private BigDecimal compensatefee;
	// 赔付说明
	private String compensateexplain;
	// 创建人
	private long founder;
	// 创建日期
	private String createddate;
	// 审核人
	private Integer verifier;
	// 审核日期
	private String checktime;
	// 核销人
	private Integer verificationperson;
	// 核销日期
	private String verificationdate;
	private List<PenalizeOut> penalizeOutList;

	
	public Integer getVerifier() {
		return verifier;
	}

	public void setVerifier(Integer verifier) {
		this.verifier = verifier;
	}

	public Integer getVerificationperson() {
		return verificationperson;
	}

	public void setVerificationperson(Integer verificationperson) {
		this.verificationperson = verificationperson;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBillbatches() {
		return this.billbatches;
	}

	public void setBillbatches(String billbatches) {
		this.billbatches = billbatches;
	}

	public Integer getBillstate() {
		return this.billstate;
	}

	public void setBillstate(Integer billstate) {
		this.billstate = billstate;
	}

	public String getCustomerid() {
		return this.customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public String getCompensateodd() {
		return this.compensateodd;
	}

	public void setCompensateodd(String compensateodd) {
		this.compensateodd = compensateodd;
	}

	public Integer getCompensatebig() {
		return this.compensatebig;
	}

	public void setCompensatebig(Integer compensatebig) {
		this.compensatebig = compensatebig;
	}

	public Integer getCompensatesmall() {
		return this.compensatesmall;
	}

	public void setCompensatesmall(Integer compensatesmall) {
		this.compensatesmall = compensatesmall;
	}

	public BigDecimal getCompensatefee() {
		return this.compensatefee;
	}

	public void setCompensatefee(BigDecimal compensatefee) {
		this.compensatefee = compensatefee;
	}

	public String getCompensateexplain() {
		return this.compensateexplain;
	}

	public void setCompensateexplain(String compensateexplain) {
		this.compensateexplain = compensateexplain;
	}

	public long getFounder() {
		return this.founder;
	}

	public void setFounder(long founder) {
		this.founder = founder;
	}

	public String getCustomername() {
		return this.customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public String getCreateddate() {
		return this.createddate;
	}

	public void setCreateddate(String createddate) {
		this.createddate = createddate;
	}


	public String getChecktime() {
		return this.checktime;
	}

	public void setChecktime(String checktime) {
		this.checktime = checktime;
	}


	public String getVerificationdate() {
		return this.verificationdate;
	}

	public void setVerificationdate(String verificationdate) {
		this.verificationdate = verificationdate;
	}

	public List<PenalizeOut> getPenalizeOutList() {
		return this.penalizeOutList;
	}

	public void setPenalizeOutList(List<PenalizeOut> penalizeOutList) {
		this.penalizeOutList = penalizeOutList;
	}

}