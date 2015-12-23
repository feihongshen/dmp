package cn.explink.domain.addressvo;

public class PreOrderVo {

	private String preorderId;

	private String addressLine;

	private Long vendorId;

	private Long customerId;

	public String getOrderId() {
		return preorderId;
	}

	public void setOrderId(String orderId) {
		this.preorderId = orderId;
	}

	public String getAddressLine() {
		return addressLine;
	}

	public void setAddressLine(String addressLine) {
		this.addressLine = addressLine;
	}

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

}
