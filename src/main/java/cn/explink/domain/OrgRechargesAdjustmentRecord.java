package cn.explink.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * POSCOD自动回款调整 记录 实体
 * @author gordon.zhou
 *
 */
public class OrgRechargesAdjustmentRecord implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6516040496527414684L;
	/**
	 * 调整单id
	 */
	private Long id;
	/**
	 * 订单号
	 */
	private String cwb;
	/**
	 * 支付方式
	 */
	private Integer payMethod;
	/**
	 * 目的站
	 */
	private Long deliverybranchid;
	/**
	 * 小件员
	 */
	private Long deliveryid;
	/**
	 * 调整后的金额
	 */
	private BigDecimal adjustAmount;
	/**
	 * 签收时间
	 */
	private Date signTime;
	/**
	 * 创建人
	 */
	private String createdUser;
	/**
	 * 创建时间
	 */
	private Date createdTime;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCwb() {
		return cwb;
	}
	public void setCwb(String cwb) {
		this.cwb = cwb;
	}
	public Integer getPayMethod() {
		return payMethod;
	}
	public void setPayMethod(Integer payMethod) {
		this.payMethod = payMethod;
	}
	public Long getDeliverybranchid() {
		return deliverybranchid;
	}
	public void setDeliverybranchid(Long deliverybranchid) {
		this.deliverybranchid = deliverybranchid;
	}
	public Long getDeliveryid() {
		return deliveryid;
	}
	public void setDeliveryid(Long deliveryid) {
		this.deliveryid = deliveryid;
	}
	public BigDecimal getAdjustAmount() {
		return adjustAmount;
	}
	public void setAdjustAmount(BigDecimal adjustAmount) {
		this.adjustAmount = adjustAmount;
	}
	public Date getSignTime() {
		return signTime;
	}
	public void setSignTime(Date signTime) {
		this.signTime = signTime;
	}
	public String getCreatedUser() {
		return createdUser;
	}
	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	

	
}
