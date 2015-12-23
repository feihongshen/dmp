package cn.explink.domain.VO.express;

public class BatchCount {
	private long allcwbnum = 0;
	private long thissuccess = 0;
	private long youhuowudanCount = 0;

	public BatchCount(long allcwbnum, long thissuccess, long youhuowudanCount) {
		super();
		this.allcwbnum = allcwbnum;
		this.thissuccess = thissuccess;
		this.youhuowudanCount = youhuowudanCount;
	}

	public long getAllcwbnum() {
		return this.allcwbnum;
	}

	public void setAllcwbnum(long allcwbnum) {
		this.allcwbnum = allcwbnum;
	}

	public long getThissuccess() {
		return this.thissuccess;
	}

	public void setThissuccess(long thissuccess) {
		this.thissuccess = thissuccess;
	}

	public long getYouhuowudanCount() {
		return this.youhuowudanCount;
	}

	public void setYouhuowudanCount(long youhuowudanCount) {
		this.youhuowudanCount = youhuowudanCount;
	}

}