package cn.explink.b2c.yangguang;

public class YangGuangdiff {

	private String username; // 访问的用户名
	private String pwd; // 访问的密码
	private String express_id; // 央广分配给快递公司的唯一ID,id 是固定不变的。一般为两位数
	private String customerids; // 供货商名称
	private long warehouseid; // 订单入库库房ID
	private int isopen; // 是否开启

	public int getIsopen() {
		return isopen;
	}

	public void setIsopen(int isopen) {
		this.isopen = isopen;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getExpress_id() {
		return express_id;
	}

	public void setExpress_id(String express_id) {
		this.express_id = express_id;
	}

	public String getCustomerids() {
		return customerids;
	}

	public void setCustomerids(String customerids) {
		this.customerids = customerids;
	}

	public long getWarehouseid() {
		return warehouseid;
	}

	public void setWarehouseid(long warehouseid) {
		this.warehouseid = warehouseid;
	}

}
