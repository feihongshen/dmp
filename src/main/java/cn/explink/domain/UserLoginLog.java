/**
 * 
 */
package cn.explink.domain;

/**
 * 用户登录日志
 * 
 * @author wangwei 2016年8月31日
 * 
 */
public class UserLoginLog {
	private long id;

	private String username; // 用户登录名

	// 登录尝试次数限制
	private int lastLoginState; // 上次登录状态（1-成功，0-失败）
	private int loginFailCount; // 累计连续登录错误次数
	private String lastLoginTryTime; // 上次尝试登录时间

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getLastLoginState() {
		return lastLoginState;
	}

	public void setLastLoginState(int lastLoginState) {
		this.lastLoginState = lastLoginState;
	}

	public int getLoginFailCount() {
		return loginFailCount;
	}

	public void setLoginFailCount(int loginFailCount) {
		this.loginFailCount = loginFailCount;
	}

	public String getLastLoginTryTime() {
		return this.lastLoginTryTime;
	}

	public void setLastLoginTryTime(String lastLoginTryTime) {
		this.lastLoginTryTime = lastLoginTryTime;
	}
}
