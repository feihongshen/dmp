package cn.explink.domain;

public class PayWay {

	private long id;
	private String payway;
	private long paywayid;
	private long issetflag;

	public PayWay() {
	}

	public PayWay(long id, String payway, long paywayid, long issetflag) {
		super();
		this.id = id;
		this.payway = payway;
		this.paywayid = paywayid;
		this.issetflag = issetflag;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPayway() {
		return payway;
	}

	public void setPayway(String payway) {
		this.payway = payway;
	}

	public long getPaywayid() {
		return paywayid;
	}

	public void setPaywayid(long paywayid) {
		this.paywayid = paywayid;
	}

	public long getIssetflag() {
		return issetflag;
	}

	public void setIssetflag(long issetflag) {
		this.issetflag = issetflag;
	}

}
