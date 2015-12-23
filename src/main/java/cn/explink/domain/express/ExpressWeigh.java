/**
 *
 */
package cn.explink.domain.express;

import java.util.Date;

/**
 * 电子称称重
 *
 * @author songkaojun 2015年10月22日
 */
public class ExpressWeigh {
	private long id;
	private String cwb;
	private double weight;
	private long branchid;
	private String branchname;
	private long handlerid;
	private String handlername;
	private Date handletime;

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCwb() {
		return this.cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public double getWeight() {
		return this.weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public long getBranchid() {
		return this.branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public String getBranchname() {
		return this.branchname;
	}

	public void setBranchname(String branchname) {
		this.branchname = branchname;
	}

	public long getHandlerid() {
		return this.handlerid;
	}

	public void setHandlerid(long handlerid) {
		this.handlerid = handlerid;
	}

	public String getHandlername() {
		return this.handlername;
	}

	public void setHandlername(String handlername) {
		this.handlername = handlername;
	}

	public Date getHandletime() {
		return this.handletime;
	}

	public void setHandletime(Date handletime) {
		this.handletime = handletime;
	}

}
