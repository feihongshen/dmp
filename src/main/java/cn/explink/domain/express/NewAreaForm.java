package cn.explink.domain.express;

public class NewAreaForm {
	/**
	 * 地址的id
	 */
	private int addressId;
	/**
	 * 地址的代码
	 */
	private String addressCode;
	/**
	 * 地址的名字
	 */
	private String addressName;
	/**
	 * 地址的层级
	 */
	private int addressLevel;
	/**
	 * 地址的显隐
	 */
	private int isDirectly;
	/**
	 * 父地址的代码
	 */
	private String parentCode;
	/**
	 * 地址的显隐
	 */
	private String parentName;

	public String getAddressCode() {
		return this.addressCode;
	}

	public void setAddressCode(String addressCode) {
		this.addressCode = addressCode;
	}

	public String getAddressName() {
		return this.addressName;
	}

	public void setAddressName(String addressName) {
		this.addressName = addressName;
	}

	public int getAddressLevel() {
		return this.addressLevel;
	}

	public void setAddressLevel(int addressLevel) {
		this.addressLevel = addressLevel;
	}

	public int getIsDirectly() {
		return this.isDirectly;
	}

	public void setIsDirectly(int isDirectly) {
		this.isDirectly = isDirectly;
	}

	public String getParentCode() {
		return this.parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public String getParentName() {
		return this.parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public int getAddressId() {
		return this.addressId;
	}

	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}

}
