package cn.explink.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import cn.explink.enumutil.UserEmployeestatusEnum;

public class User implements Serializable {
	long userid;
	String username;
	String realname;
	// String lastusername;
	String password;
	long branchid;
	long usercustomerid;
	// int usertypeflag;
	// long departid;

	String idcardno;
	int employeestatus;
	String userphone;
	String usermobile;
	String useraddress;
	String userremark;

	String showphoneflag;
	String useremail;
	// int deliverpaytype;
	String userwavfile;
	// int branchmanagerflag;
	long roleid;
	long userDeleteFlag;

	String deliverManCode; // 配送员编码 add 20130319

	private BigDecimal deliverAccount = BigDecimal.ZERO;// 小件员现金帐户余额-小件员交款功能
	private BigDecimal deliverPosAccount = BigDecimal.ZERO;// 小件员POS帐户余额-小件员交款功能
	private BigDecimal usersalary = BigDecimal.ZERO;
	private String lastLoginIp;
	private String lastLoginTime;

	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	int isImposedOutWarehouse;// 是否拥有 请指出库权限 1是 0 否 默认1

	public int getIsImposedOutWarehouse() {
		return isImposedOutWarehouse;
	}

	public void setIsImposedOutWarehouse(int isImposedOutWarehouse) {
		this.isImposedOutWarehouse = isImposedOutWarehouse;
	}

	public BigDecimal getDeliverAccount() {
		return deliverAccount;
	}

	public void setDeliverAccount(BigDecimal deliverAccount) {
		this.deliverAccount = deliverAccount;
	}

	public BigDecimal getDeliverPosAccount() {
		return deliverPosAccount;
	}

	public void setDeliverPosAccount(BigDecimal deliverPosAccount) {
		this.deliverPosAccount = deliverPosAccount;
	}

	public String getDeliverManCode() {
		return deliverManCode;
	}

	public void setDeliverManCode(String deliverManCode) {
		this.deliverManCode = deliverManCode;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getBranchid() {
		return branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	// public String getLastusername() {
	// return lastusername;
	// }
	// public void setLastusername(String lastusername) {
	// this.lastusername = lastusername;
	// }
	public long getUsercustomerid() {
		return usercustomerid;
	}

	public void setUsercustomerid(long usercustomerid) {
		this.usercustomerid = usercustomerid;
	}

	// public int getUsertypeflag() {
	// return usertypeflag;
	// }
	// public void setUsertypeflag(int usertypeflag) {
	// this.usertypeflag = usertypeflag;
	// }
	// public long getDepartid() {
	// return departid;
	// }
	// public void setDepartid(long departid) {
	// this.departid = departid;
	// }
	public String getIdcardno() {
		return idcardno;
	}

	public void setIdcardno(String idcardno) {
		this.idcardno = idcardno;
	}

	public int getEmployeestatus() {
		return employeestatus;
	}

	public String getEmployeestatusName() {
		for (UserEmployeestatusEnum ue : UserEmployeestatusEnum.values()) {
			if (ue.getValue() == this.employeestatus)
				return ue.getText();
		}
		return "";
	}

	public void setEmployeestatus(int employeestatus) {
		this.employeestatus = employeestatus;
	}

	public String getUserphone() {
		return userphone;
	}

	public void setUserphone(String userphone) {
		this.userphone = userphone;
	}

	public String getUsermobile() {
		return usermobile;
	}

	public void setUsermobile(String usermobile) {
		this.usermobile = usermobile;
	}

	public String getUseraddress() {
		return useraddress;
	}

	public void setUseraddress(String useraddress) {
		this.useraddress = useraddress;
	}

	public String getUserremark() {
		return userremark;
	}

	public void setUserremark(String userremark) {
		this.userremark = userremark;
	}

	public BigDecimal getUsersalary() {
		return usersalary;
	}

	public void setUsersalary(BigDecimal usersalary) {
		this.usersalary = usersalary;
	}

	public String getShowphoneflag() {
		return showphoneflag;
	}

	public void setShowphoneflag(String showphoneflag) {
		this.showphoneflag = showphoneflag;
	}

	public String getUseremail() {
		return useremail;
	}

	public void setUseremail(String useremail) {
		this.useremail = useremail;
	}

	public String getUserwavfile() {
		return userwavfile;
	}

	public void setUserwavfile(String userwavfile) {
		this.userwavfile = userwavfile;
	}

	public long getRoleid() {
		return roleid;
	}

	public void setRoleid(long roleid) {
		this.roleid = roleid;
	}

	/*
	 * public int getDeliverpaytype() { return deliverpaytype; } public void
	 * setDeliverpaytype(int deliverpaytype) { this.deliverpaytype =
	 * deliverpaytype; } public int getBranchmanagerflag() { return
	 * branchmanagerflag; } public void setBranchmanagerflag(int
	 * branchmanagerflag) { this.branchmanagerflag = branchmanagerflag; }
	 */
	public long getUserDeleteFlag() {
		return userDeleteFlag;
	}

	public void setUserDeleteFlag(long userDeleteFlag) {
		this.userDeleteFlag = userDeleteFlag;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (userid ^ (userid >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (userid != other.userid)
			return false;
		return true;
	}

}
