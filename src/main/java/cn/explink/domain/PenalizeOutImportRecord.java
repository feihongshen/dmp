/**
 *
 */
package cn.explink.domain;

/**
 * @author Administrator
 *
 */
public class PenalizeOutImportRecord {
private long id;
private long importFlag;
private long userid;
private String starttime;
//private String endtime;
private int successCounts;
private int failCounts;
private int totalCounts;
/**
 * @return the id
 */
public long getId() {
	return this.id;
}
/**
 * @param id the id to set
 */
public void setId(long id) {
	this.id = id;
}
/**
 * @return the importFlag
 */
public long getImportFlag() {
	return this.importFlag;
}
/**
 * @param importFlag the importFlag to set
 */
public void setImportFlag(long importFlag) {
	this.importFlag = importFlag;
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
/*public String getEndtime() {
	return this.endtime;
}
*//**
 * @param endtime the endtime to set
 *//*
public void setEndtime(String endtime) {
	this.endtime = endtime;
}*/
/**
 * @return the successCounts
 */
public int getSuccessCounts() {
	return this.successCounts;
}
/**
 * @param successCounts the successCounts to set
 */
public void setSuccessCounts(int successCounts) {
	this.successCounts = successCounts;
}
/**
 * @return the failCounts
 */
public int getFailCounts() {
	return this.failCounts;
}
/**
 * @param failCounts the failCounts to set
 */
public void setFailCounts(int failCounts) {
	this.failCounts = failCounts;
}
/**
 * @return the totalCounts
 */
public int getTotalCounts() {
	return this.totalCounts;
}
/**
 * @param totalCounts the totalCounts to set
 */
public void setTotalCounts(int totalCounts) {
	this.totalCounts = totalCounts;
}

}
