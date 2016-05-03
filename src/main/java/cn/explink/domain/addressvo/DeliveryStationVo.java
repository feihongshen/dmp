package cn.explink.domain.addressvo;

public class DeliveryStationVo {

	private Long externalId;

	private String name;

	private Long customerId;
	/**
	 * 机构编码tpsbranchcode
	 */
	private String stationCode;

	public Long getExternalId() {
		return externalId;
	}

	public void setExternalId(Long externalId) {
		this.externalId = externalId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	@Override
	public String toString() {
		return "DeliveryStationVo [externalId=" + externalId + ", name=" + name + ", customerId=" + customerId
				+ ", stationCode=" + stationCode + "]";
	}

}
