package cn.explink.domain.addressvo;

public class DelivererVo {

	/**
	 * 地址库省份ID
	 */
	private Long customerId;
	
	/**
	 * 站点ID -> branchid
	 */
	private Long externalStationId;
	
	/**
	 * 小件员ID -> userid
	 */
	private Long externalId;
	
	/**
	 * 小件员用户名 -> username
	 */
	private String userCode;

	/**
	 * 小件员名称 -> realname
	 */
	private String name;

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getExternalStationId() {
		return externalStationId;
	}

	public void setExternalStationId(Long externalStationId) {
		this.externalStationId = externalStationId;
	}

	public Long getExternalId() {
		return externalId;
	}

	public void setExternalId(Long externalId) {
		this.externalId = externalId;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "DelivererVo [customerId=" + customerId + ", externalStationId=" + externalStationId + ", externalId="
				+ externalId + ", userCode=" + userCode + ", name=" + name + "]";
	}
}
