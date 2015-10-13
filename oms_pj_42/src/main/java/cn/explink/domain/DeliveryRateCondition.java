package cn.explink.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DELIVERY_RATE_CONDITIONS")
public class DeliveryRateCondition {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "NAME", length = 50, nullable = false)
	private String name;

	@Column(name = "STATUS", nullable = false)
	private Integer status;

	@Column(name = "SELECT_TYPE")
	private Integer selectType;

	@Column(name = "DELIVERY_RATE_REQUEST", length = 32)
	private String deliveryRateRequest;

	@Column(name = "USER_ID")
	private Long userId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getSelectType() {
		return selectType;
	}

	public void setSelectType(Integer selectType) {
		this.selectType = selectType;
	}

	public String getDeliveryRateRequest() {
		return deliveryRateRequest;
	}

	public void setDeliveryRateRequest(String deliveryRateRequest) {
		this.deliveryRateRequest = deliveryRateRequest;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}