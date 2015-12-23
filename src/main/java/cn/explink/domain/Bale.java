package cn.explink.domain;

import java.sql.Timestamp;
import java.util.Date;

public class Bale {

	private long id;
	private String baleno;
	private long balestate;
	private long branchid;
	private long groupid;
	private Timestamp cretime;
	private long nextbranchid;
	private long cwbcount;
	/**
	 * 合包人id
	 */
	private int handlerid;
	/**
	 * 合包人姓名
	 */
	private String handlername;
	private long scannum;

	public Bale() {
	}

	public Bale(long id, String baleno, long balestate, long branchid, long groupid, Date cretime, long nextbranchid, long cwbcount) {
		super();
		this.id = id;
		this.baleno = baleno;
		this.balestate = balestate;
		this.branchid = branchid;
		this.groupid = groupid;
		this.nextbranchid = nextbranchid;
		this.cwbcount = cwbcount;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBaleno() {
		return this.baleno;
	}

	public void setBaleno(String baleno) {
		this.baleno = baleno;
	}

	public long getBalestate() {
		return this.balestate;
	}

	public void setBalestate(long balestate) {
		this.balestate = balestate;
	}

	public long getBranchid() {
		return this.branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public long getGroupid() {
		return this.groupid;
	}

	public void setGroupid(long groupid) {
		this.groupid = groupid;
	}

	public Timestamp getCretime() {
		return this.cretime;
	}

	public void setCretime(Timestamp cretime) {
		this.cretime = cretime;
	}

	public long getNextbranchid() {
		return this.nextbranchid;
	}

	public void setNextbranchid(long nextbranchid) {
		this.nextbranchid = nextbranchid;
	}

	public long getCwbcount() {
		return this.cwbcount;
	}

	public void setCwbcount(long cwbcount) {
		this.cwbcount = cwbcount;
	}

	public int getHandlerid() {
		return this.handlerid;
	}

	public void setHandlerid(int handlerid) {
		this.handlerid = handlerid;
	}

	public String getHandlername() {
		return this.handlername;
	}

	public void setHandlername(String handlername) {
		this.handlername = handlername;
	}

	/**
	 * @return the scannum
	 */
	public long getScannum() {
		return this.scannum;
	}

	/**
	 * @param scannum
	 *            the scannum to set
	 */
	public void setScannum(long scannum) {
		this.scannum = scannum;
	}
}
