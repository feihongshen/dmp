package cn.explink.domain.VO.express;

public class ExtralInfo4Address {
	/**
	 * 订单号/预订单号
	 */
	private String cwb;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 带匹配的地址
	 */
	private String address;
	/**
	 * 调用者标示，AddressMatchEnum为各调用者枚举
	 */
	private int addressMatcher;

	public ExtralInfo4Address() {
		// TODO Auto-generated constructor stub
	}

	public ExtralInfo4Address(String cwb, Long userId, String address) {
		super();
		this.cwb = cwb;
		this.userId = userId;
		this.address = address;
	}

	public ExtralInfo4Address(String cwb, Long userId, String address, int addressMatcher) {
		super();
		this.cwb = cwb;
		this.userId = userId;
		this.address = address;
		this.addressMatcher = addressMatcher;
	}

	public String getCwb() {
		return this.cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getAddressMatcher() {
		return this.addressMatcher;
	}

	public void setAddressMatcher(int addressMatcher) {
		this.addressMatcher = addressMatcher;
	}

}
