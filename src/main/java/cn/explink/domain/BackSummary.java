package cn.explink.domain;

public class BackSummary {
	private long summaryid;
	private long nums24;
	private long nums72;
	private long numsout;
	private long numsinto;
	private String createtime;

	// 不在数据库
	private String percent;// 退货占比

	public String getPercent() {
		return percent;
	}

	public void setPercent(String percent) {
		this.percent = percent;
	}

	public long getSummaryid() {
		return summaryid;
	}

	public void setSummaryid(long summaryid) {
		this.summaryid = summaryid;
	}

	public long getNums24() {
		return nums24;
	}

	public void setNums24(long nums24) {
		this.nums24 = nums24;
	}

	public long getNums72() {
		return nums72;
	}

	public void setNums72(long nums72) {
		this.nums72 = nums72;
	}

	public long getNumsout() {
		return numsout;
	}

	public void setNumsout(long numsout) {
		this.numsout = numsout;
	}

	public long getNumsinto() {
		return numsinto;
	}

	public void setNumsinto(long numsinto) {
		this.numsinto = numsinto;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
}
