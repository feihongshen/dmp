package cn.explink.b2c.dangdang_dataimport;

/**
 * 返回给当当如果出现bug则
 * 
 * @author Administrator
 *
 */
public class DangDangError {
	private String order_id;
	private String error_code;

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getError_code() {
		return error_code;
	}

	public void setError_code(String error_code) {
		this.error_code = error_code;
	}
}
