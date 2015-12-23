package cn.explink.domain.express;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

/**
 *
 * @description 货款账单导入明细表实体
 * @author  刘武强
 * @data   2015年8月21日
 */
public class ExpressCodBillDetail {
	/**
	 * 主键id
	 */
	private Long id;
	/**
	 * 订单号
	 */
	private String orderNo;
	/**
	 * 件数
	 */
	private Long goodNum;
	/**
	 * 揽件人
	 */
	private String collectPerson;
	/**
	 * 配送人
	 */
	private String deliveryPerson;
	/**
	 * 代收货款（元）
	 */
	private BigDecimal cod;
	/**
	 * 配送站点id
	 */
	private Long deliveryBranchId;
	/**
	 * 配送站点
	 */
	private String deliveryBranch;
	/**
	 * 账单id
	 */
	private Long billId;
	/**
	 * 账单编号
	 */
	private String billNo;
	/**
	 * 生效标识(0：无效，1：生效)
	 */
	private Integer effectFlag;
	/**
	 * 未匹配原因
	 */
	private String dismatchReason;
	/**
	 * 导入人id
	 */
	private Long importPersonId;
	/**
	 * 导入人
	 */
	private String importPerson;
	/**
	 * 导入时间
	 */
	@DateTimeFormat(iso = ISO.DATE)
	private Date importTime;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Long getGoodNum() {
		return this.goodNum;
	}

	public void setGoodNum(Long goodNum) {
		this.goodNum = goodNum;
	}

	public String getCollectPerson() {
		return this.collectPerson;
	}

	public void setCollectPerson(String collectPerson) {
		this.collectPerson = collectPerson;
	}

	public BigDecimal getCod() {
		return this.cod;
	}

	public void setCod(BigDecimal cod) {
		this.cod = cod;
	}

	public Long getDeliveryBranchId() {
		return this.deliveryBranchId;
	}

	public void setDeliveryBranchId(Long deliveryBranchId) {
		this.deliveryBranchId = deliveryBranchId;
	}

	public String getDeliveryBranch() {
		return this.deliveryBranch;
	}

	public void setDeliveryBranch(String deliveryBranch) {
		this.deliveryBranch = deliveryBranch;
	}

	public Long getBillId() {
		return this.billId;
	}

	public void setBillId(Long billId) {
		this.billId = billId;
	}

	public String getBillNo() {
		return this.billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public Integer getEffectFlag() {
		return this.effectFlag;
	}

	public void setEffectFlag(Integer effectFlag) {
		this.effectFlag = effectFlag;
	}

	public String getDismatchReason() {
		return this.dismatchReason;
	}

	public void setDismatchReason(String dismatchReason) {
		this.dismatchReason = dismatchReason;
	}

	public Long getImportPersonId() {
		return this.importPersonId;
	}

	public void setImportPersonId(Long importPersonId) {
		this.importPersonId = importPersonId;
	}

	public String getImportPerson() {
		return this.importPerson;
	}

	public void setImportPerson(String importPerson) {
		this.importPerson = importPerson;
	}

	public Date getImportTime() {
		return this.importTime;
	}

	public void setImportTime(Date importTime) {
		this.importTime = importTime;
	}

	public String getDeliveryPerson() {
		return this.deliveryPerson;
	}

	public void setDeliveryPerson(String deliveryPerson) {
		this.deliveryPerson = deliveryPerson;
	}

}
