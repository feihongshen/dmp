package cn.explink.domain.addressvo;

public class BatchSyncAdressResultVo {
	
	/**
	 * 登录名
	 */
	private String username;
	
	/**
	 * 姓名
	 */
	private String realname;
	
	/**
	 * 是否成功
	 */
	private String result;
	
	/**
	 * 信息
	 */
	private String message;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "BatchSyncAdressResultVo [username=" + username + ", realname=" + realname + ", result=" + result
				+ ", message=" + message + "]";
	}
}
