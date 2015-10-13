package cn.explink.b2c.lefeng;

public class ReturnData {
	/**
	 * 从乐蜂网传来的信息Json所对应的类
	 */
	private String[] items;
	private String total;
	private String code;
	private String message;

	public String getTotal() {
		return this.total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String[] getItems() {
		return this.items;
	}

	public void setItems(String[] items) {
		this.items = items;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
