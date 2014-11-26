package cn.explink.b2c.vipshop.address;

/**
 * 唯品会对接设置配置属性
 * 
 * @author Administrator
 *
 */
public class VipShopAddress {

	private String shipper_no; // 承运商账号
	private String private_key; // 加密秘钥
	private int getMaxCount; // 每次获取地址个数最大数量

	public String getShipper_no() {
		return shipper_no;
	}

	public void setShipper_no(String shipper_no) {
		this.shipper_no = shipper_no;
	}

	public String getPrivate_key() {
		return private_key;
	}

	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}

	public int getGetMaxCount() {
		return getMaxCount;
	}

	public void setGetMaxCount(int getMaxCount) {
		this.getMaxCount = getMaxCount;
	}

}
