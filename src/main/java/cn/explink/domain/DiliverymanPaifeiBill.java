/**
 *
 */
package cn.explink.domain;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author wangqiang 配送员派费账单实体
 */
public class DiliverymanPaifeiBill {
	private Integer id;
	private Integer billstate;// 账单状态
	private String billbatch;// 账单批次
	private Integer diliveryman;// 配送员
	private Integer theirsite;// 所属站点
	private String billestablishdate;// 创建时间
	private String billverificationdate;// 核销时间
	private Integer ordertype;// 订单类型
	private Integer ordersum;// 对应订单数
	private BigDecimal paifeimoney;// 派费金额
	private String remarks;// 备注
	private String orderids;// 对应所有订单
	private String daterange;// 日期范围

	// 只作为查询条件，不与数据库关联
	private String billCreationStartDate;// 账单创建开始时间
	private String billCreationEndDate;// 账单创建结束时间
	private String billVerificationStrartDate;// 账单核销开始时间
	private String billVerificationEndDate;// 账单核销结束时间
	private String sortField;// 排序字段
	private String orderingMethod;// 排序方法
	private List<DiliverymanPaifeiOrder> diliverymanPaifeiOrderList;

	/**
	 *
	 */
	public DiliverymanPaifeiBill() {
		// TODO Auto-generated constructor stub
	}

	public String getOrderids() {
		return this.orderids;
	}

	public void setOrderids(String orderids) {
		this.orderids = orderids;
	}

	public String getDaterange() {
		return this.daterange;
	}

	public void setDaterange(String daterange) {
		this.daterange = daterange;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBillstate() {
		return this.billstate;
	}

	public void setBillstate(Integer billstate) {
		this.billstate = billstate;
	}

	public String getBillbatch() {
		return this.billbatch;
	}

	public void setBillbatch(String billbatch) {
		this.billbatch = billbatch;
	}

	public Integer getDiliveryman() {
		return this.diliveryman;
	}

	public void setDiliveryman(Integer diliveryman) {
		this.diliveryman = diliveryman;
	}

	public Integer getTheirsite() {
		return this.theirsite;
	}

	public void setTheirsite(Integer theirsite) {
		this.theirsite = theirsite;
	}

	public String getBillestablishdate() {
		return this.billestablishdate;
	}

	public void setBillestablishdate(String billestablishdate) {
		this.billestablishdate = billestablishdate;
	}

	public String getBillverificationdate() {
		return this.billverificationdate;
	}

	public void setBillverificationdate(String billverificationdate) {
		this.billverificationdate = billverificationdate;
	}

	public Integer getOrdertype() {
		return this.ordertype;
	}

	public void setOrdertype(Integer ordertype) {
		this.ordertype = ordertype;
	}

	public Integer getOrdersum() {
		return this.ordersum;
	}

	public void setOrdersum(Integer ordersum) {
		this.ordersum = ordersum;
	}

	public BigDecimal getPaifeimoney() {
		return this.paifeimoney;
	}

	public void setPaifeimoney(BigDecimal paifeimoney) {
		this.paifeimoney = paifeimoney;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getBillCreationStartDate() {
		return this.billCreationStartDate;
	}

	public void setBillCreationStartDate(String billCreationStartDate) {
		this.billCreationStartDate = billCreationStartDate;
	}

	public String getBillCreationEndDate() {
		return this.billCreationEndDate;
	}

	public void setBillCreationEndDate(String billCreationEndDate) {
		this.billCreationEndDate = billCreationEndDate;
	}

	public String getBillVerificationStrartDate() {
		return this.billVerificationStrartDate;
	}

	public void setBillVerificationStrartDate(String billVerificationStrartDate) {
		this.billVerificationStrartDate = billVerificationStrartDate;
	}

	public String getBillVerificationEndDate() {
		return this.billVerificationEndDate;
	}

	public void setBillVerificationEndDate(String billVerificationEndDate) {
		this.billVerificationEndDate = billVerificationEndDate;
	}

	public String getSortField() {
		return this.sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public String getOrderingMethod() {
		return this.orderingMethod;
	}

	public void setOrderingMethod(String orderingMethod) {
		this.orderingMethod = orderingMethod;
	}

	public List<DiliverymanPaifeiOrder> getDiliverymanPaifeiOrderList() {
		return this.diliverymanPaifeiOrderList;
	}

	public void setDiliverymanPaifeiOrderList(List<DiliverymanPaifeiOrder> diliverymanPaifeiOrderList) {
		this.diliverymanPaifeiOrderList = diliverymanPaifeiOrderList;
	}

}
