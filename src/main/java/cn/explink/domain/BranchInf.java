package cn.explink.domain;

import java.util.Date;


/**
 * 站点机构接口表
 * @author jian.xie
 *
 */
public class BranchInf {
	
	private long 					infId;						// 主键
	
	private long 					branchid;					// 站点主键
	
	private String 					branchname;					// 机构名称
	
	private String 					tpsbranchcode;				//上传tps时所用的机构编码
	
	private String 					branchprovince;				// 省份
	
	private String 					branchcity;					// 城市

	private String 					brancharea;					// 区城
	
	private String					password;					// 密码
	
	private long					recBranchid;				// 删除时用来替换的机构id

	private Date					createDate;					// 创建时间
	
	private String					createUser;					// 创建人
	
	private boolean					isSync;						// 是否同步
	
	private String					operType;					// 操作类型

	public long getInfId() {
		return infId;
	}

	public void setInfId(long infId) {
		this.infId = infId;
	}

	public long getBranchid() {
		return branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public String getBranchname() {
		return branchname;
	}

	public void setBranchname(String branchname) {
		this.branchname = branchname;
	}

	public String getTpsbranchcode() {
		return tpsbranchcode;
	}

	public void setTpsbranchcode(String tpsbranchcode) {
		this.tpsbranchcode = tpsbranchcode;
	}

	public String getBranchprovince() {
		return branchprovince;
	}

	public void setBranchprovince(String branchprovince) {
		this.branchprovince = branchprovince;
	}

	public String getBranchcity() {
		return branchcity;
	}

	public void setBranchcity(String branchcity) {
		this.branchcity = branchcity;
	}

	public String getBrancharea() {
		return brancharea;
	}

	public void setBrancharea(String brancharea) {
		this.brancharea = brancharea;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getRecBranchid() {
		return recBranchid;
	}

	public void setRecBranchid(long recBranchid) {
		this.recBranchid = recBranchid;
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

	public boolean getIsSync() {
		return isSync;
	}

	public void setIsSync(boolean isSync) {
		this.isSync = isSync;
	}

	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}
	
}
