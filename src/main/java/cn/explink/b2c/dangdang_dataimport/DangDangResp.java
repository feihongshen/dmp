package cn.explink.b2c.dangdang_dataimport;

import java.util.List;

/**
 * 返回给当当的 helper尸体
 * 
 * @author Administrator
 *
 */
public class DangDangResp {

	private String error_code;
	private List<DangDangError> error_list;

	public String getError_code() {
		return error_code;
	}

	public void setError_code(String error_code) {
		this.error_code = error_code;
	}

	public List<DangDangError> getError_list() {
		return error_list;
	}

	public void setError_list(List<DangDangError> error_list) {
		this.error_list = error_list;
	}
}
