package cn.explink.domain;

import java.util.Date;

/**
 * 用户接口表
 * @author jian.xie
 *
 */
public class UserInf {
	
	private long				infId;					// 主键
	
	private long				userid;					// 用户id
	
	private String				username;				// 登录名
	
	private String				realname;				// 真实名字
	
	private String				usermobile;				// 手机号
	
	private String				password;				// 密码
	
	private boolean				isSync;					// 是否同步
	
	private byte				status;					// 状态 0、有效，1、失效。 为了兼容品骏达
	
	private Date				createDate;				// 创建时间
	
	private String				createUser;				// 创建人
	
	private long				branchid;				// 机构id
	
	private String				oldusername;			// 旧username
	
	private int					times;					// 同步次数

	public long getInfId() {
		return infId;
	}

	public void setInfId(long infId) {
		this.infId = infId;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

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

	public String getUsermobile() {
		return usermobile;
	}

	public void setUsermobile(String usermobile) {
		this.usermobile = usermobile;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean getIsSync() {
		return isSync;
	}

	public void setIsSync(boolean isSync) {
		this.isSync = isSync;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public long getBranchid() {
		return branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public String getOldusername() {
		return oldusername;
	}

	public void setOldusername(String oldusername) {
		this.oldusername = oldusername;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}
	
}
