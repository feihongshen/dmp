/**
 *
 */
package cn.explink.domain;

/**
 * @author Administrator
 *
 */
public class SalaryCount {
private String batchid;//批次id
private int batchstate;//批次状态
private long branchid;//站点
private String starttime;//开始期间
private String endtime;//结束期间
private long usercount;//配送人员人数
private long userid;//核销人
private String operationTime;//核销日期
private String remark;//备注
/**
 * @return the batchid
 */
public String getBatchid() {
	return this.batchid;
}
/**
 * @param batchid the batchid to set
 */
public void setBatchid(String batchid) {
	this.batchid = batchid;
}
/**
 * @return the batchstate
 */
public int getBatchstate() {
	return this.batchstate;
}
/**
 * @param batchstate the batchstate to set
 */
public void setBatchstate(int batchstate) {
	this.batchstate = batchstate;
}
/**
 * @return the branchid
 */
public long getBranchid() {
	return this.branchid;
}
/**
 * @param branchid the branchid to set
 */
public void setBranchid(long branchid) {
	this.branchid = branchid;
}
/**
 * @return the starttime
 */
public String getStarttime() {
	return this.starttime;
}
/**
 * @param starttime the starttime to set
 */
public void setStarttime(String starttime) {
	this.starttime = starttime;
}
/**
 * @return the endtime
 */
public String getEndtime() {
	return this.endtime;
}
/**
 * @param endtime the endtime to set
 */
public void setEndtime(String endtime) {
	this.endtime = endtime;
}
/**
 * @return the usercount
 */
public long getUsercount() {
	return this.usercount;
}
/**
 * @param usercount the usercount to set
 */
public void setUsercount(long usercount) {
	this.usercount = usercount;
}
/**
 * @return the userid
 */
public long getUserid() {
	return this.userid;
}
/**
 * @param userid the userid to set
 */
public void setUserid(long userid) {
	this.userid = userid;
}
/**
 * @return the operationTime
 */
public String getOperationTime() {
	return this.operationTime;
}
/**
 * @param operationTime the operationTime to set
 */
public void setOperationTime(String operationTime) {
	this.operationTime = operationTime;
}
/**
 * @return the remark
 */
public String getRemark() {
	return this.remark;
}
/**
 * @param remark the remark to set
 */
public void setRemark(String remark) {
	this.remark = remark;
}


}
