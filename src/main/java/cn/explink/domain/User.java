package cn.explink.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import cn.explink.enumutil.UserEmployeestatusEnum;

public class User implements Serializable {
	
	private static final long serialVersionUID = 4430175434189183927L;
	
	long userid;
	String oldusername;
	String username;
	String realname;
	// String lastusername;
	String password;	// POS登录密码
	String webPassword;	// 网页登录密码
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

	private BigDecimal deliverAccount = BigDecimal.ZERO;// 小件员现金帐户余额-小件员交款功能
	private BigDecimal deliverPosAccount = BigDecimal.ZERO;// 小件员POS帐户余额-小件员交款功能
	private BigDecimal usersalary = BigDecimal.ZERO;
	private String lastLoginIp;
	private String lastLoginTime;
	private long pfruleid;// 派费规则id
	
	//工资计算维护字段(LX)
	private int sex;//性别(1 男 2 女)
	private String startworkdate;//入职日期
	private String jobnum;//工号
	private int jiesuanstate;//结算状态
	private BigDecimal maxcutpayment;//最高扣款额度
	private BigDecimal fixedadvance;//固定预付款
	private BigDecimal basicadvance;//基础预付款
	private long fallbacknum;//保底单量
	private BigDecimal lateradvance;//后期预付款
	private BigDecimal basicfee;//基本派费
	private BigDecimal areafee;//区域派费

	public BigDecimal getBasicfee() {
		return basicfee;
	}

	public void setBasicfee(BigDecimal basicfee) {
		this.basicfee = basicfee;
	}

	public BigDecimal getAreafee() {
		return areafee;
	}

	public void setAreafee(BigDecimal areafee) {
		this.areafee = areafee;
	}
	
	public BigDecimal getLateradvance() {
		return lateradvance;
	}

	public void setLateradvance(BigDecimal lateradvance) {
		this.lateradvance = lateradvance;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getStartworkdate() {
		return startworkdate;
	}

	public void setStartworkdate(String startworkdate) {
		this.startworkdate = startworkdate;
	}

	public String getJobnum() {
		return jobnum;
	}

	public void setJobnum(String jobnum) {
		this.jobnum = jobnum;
	}

	public int getJiesuanstate() {
		return jiesuanstate;
	}

	public void setJiesuanstate(int jiesuanstate) {
		this.jiesuanstate = jiesuanstate;
	}

	public BigDecimal getMaxcutpayment() {
		return maxcutpayment;
	}

	public void setMaxcutpayment(BigDecimal maxcutpayment) {
		this.maxcutpayment = maxcutpayment;
	}

	public BigDecimal getBasicadvance() {
		return basicadvance;
	}

	public void setBasicadvance(BigDecimal basicadvance) {
		this.basicadvance = basicadvance;
	}

	public BigDecimal getFixedadvance() {
		return fixedadvance;
	}

	public void setFixedadvance(BigDecimal fixedadvance) {
		this.fixedadvance = fixedadvance;
	}


	public long getFallbacknum() {
		return fallbacknum;
	}

	public void setFallbacknum(long fallbacknum) {
		this.fallbacknum = fallbacknum;
	}

	/**
	 * @return the pfruleid
	 */
	public long getPfruleid() {
		return this.pfruleid;
	}

	/**
	 * @param pfruleid the pfruleid to set
	 */
	public void setPfruleid(long pfruleid) {
		this.pfruleid = pfruleid;
	}
	public String getLastLoginIp() {
		return this.lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public String getLastLoginTime() {
		return this.lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	int isImposedOutWarehouse;// 是否拥有 请指出库权限 1是 0 否 默认1

	public int getIsImposedOutWarehouse() {
		return this.isImposedOutWarehouse;
	}

	public void setIsImposedOutWarehouse(int isImposedOutWarehouse) {
		this.isImposedOutWarehouse = isImposedOutWarehouse;
	}

	public BigDecimal getDeliverAccount() {
		return this.deliverAccount;
	}

	public void setDeliverAccount(BigDecimal deliverAccount) {
		this.deliverAccount = deliverAccount;
	}

	public BigDecimal getDeliverPosAccount() {
		return this.deliverPosAccount;
	}

	public void setDeliverPosAccount(BigDecimal deliverPosAccount) {
		this.deliverPosAccount = deliverPosAccount;
	}

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

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOldusername() {
		return this.oldusername;
	}

	public void setOldusername(String oldusername) {
		this.oldusername = oldusername;
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
	
	public String getWebPassword() {
		return this.webPassword;
	}

	public void setWebPassword(String webPassword) {
		this.webPassword = webPassword;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + (int) (this.userid ^ (this.userid >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		User other = (User) obj;
		if (this.userid != other.userid) {
			return false;
		}
		return true;
	}

}
