package cn.explink.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import cn.explink.enumutil.UserEmployeestatusEnum;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable {
	long userid;
	String username;
	String oldusername;
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
	BigDecimal usersalary;
	long showphoneflag;
	long shownameflag;
	long showmobileflag;
	String useremail;
	// int deliverpaytype;
	String userwavfile;
	// int branchmanagerflag;
	long roleid;
	long userDeleteFlag;

	String deliverManCode; // 配送员编码 add 20130319

	public String getDeliverManCode() {
		return this.deliverManCode;
	}

	public void setDeliverManCode(String deliverManCode) {
		this.deliverManCode = deliverManCode;
	}

	public long getUserid() {
		return this.userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return this.username;
	}

	public String getOldusername() {
		return this.oldusername;
	}

	public void setOldusername(String oldusername) {
		this.oldusername = oldusername;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRealname() {
		return this.realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getBranchid() {
		return this.branchid;
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
		return this.usercustomerid;
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
		return this.idcardno;
	}

	public void setIdcardno(String idcardno) {
		this.idcardno = idcardno;
	}

	public int getEmployeestatus() {
		return this.employeestatus;
	}

	public String getEmployeestatusName() {
		for (UserEmployeestatusEnum ue : UserEmployeestatusEnum.values()) {
			if (ue.getValue() == this.employeestatus) {
				return ue.getText();
			}
		}
		return "";
	}

	public void setEmployeestatus(int employeestatus) {
		this.employeestatus = employeestatus;
	}

	public String getUserphone() {
		return this.userphone;
	}

	public void setUserphone(String userphone) {
		this.userphone = userphone;
	}

	public String getUsermobile() {
		return this.usermobile;
	}

	public void setUsermobile(String usermobile) {
		this.usermobile = usermobile;
	}

	public String getUseraddress() {
		return this.useraddress;
	}

	public void setUseraddress(String useraddress) {
		this.useraddress = useraddress;
	}

	public String getUserremark() {
		return this.userremark;
	}

	public void setUserremark(String userremark) {
		this.userremark = userremark;
	}

	public BigDecimal getUsersalary() {
		return this.usersalary;
	}

	public void setUsersalary(BigDecimal usersalary) {
		this.usersalary = usersalary;
	}


	public long getShowphoneflag() {
		return this.showphoneflag;
	}

	public void setShowphoneflag(long showphoneflag) {
		this.showphoneflag = showphoneflag;
	}

	public long getShownameflag() {
		return this.shownameflag;
	}

	public void setShownameflag(long shownameflag) {
		this.shownameflag = shownameflag;
	}

	public long getShowmobileflag() {
		return this.showmobileflag;
	}

	public void setShowmobileflag(long showmobileflag) {
		this.showmobileflag = showmobileflag;
	}

	public String getUseremail() {
		return this.useremail;
	}

	public void setUseremail(String useremail) {
		this.useremail = useremail;
	}

	public String getUserwavfile() {
		return this.userwavfile;
	}

	public void setUserwavfile(String userwavfile) {
		this.userwavfile = userwavfile;
	}

	public long getRoleid() {
		return this.roleid;
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
		return this.userDeleteFlag;
	}

	public void setUserDeleteFlag(long userDeleteFlag) {
		this.userDeleteFlag = userDeleteFlag;
	}

}
