package cn.explink.domain;

public class Truck {

	private long truckid;
	private String truckno;
	private String trucktype;
	private Float truckoil;
	private String truckway;
	private Float truckkm;
	private Float truckstartkm;
	private int truckdriver;
	private int truckflag;

	public long getTruckid() {
		return truckid;
	}

	public void setTruckid(long truckid) {
		this.truckid = truckid;
	}

	public String getTruckno() {
		return truckno;
	}

	public void setTruckno(String truckno) {
		this.truckno = truckno;
	}

	public String getTrucktype() {
		return trucktype;
	}

	public void setTrucktype(String trucktype) {
		this.trucktype = trucktype;
	}

	public Float getTruckoil() {
		return truckoil;
	}

	public void setTruckoil(Float truckoil) {
		this.truckoil = truckoil;
	}

	public void setTruckoil(String truckoil) {
		try {
			this.truckoil = new Float(truckoil);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("耗油量不是有效的数字格式:" + truckoil);
		}
	}

	public String getTruckway() {
		return truckway;
	}

	public void setTruckway(String truckway) {
		this.truckway = truckway;
	}

	public Float getTruckkm() {
		return truckkm;
	}

	public void setTruckkm(Float truckkm) {
		this.truckkm = truckkm;
	}

	public void setTruckkm(String truckkm) {
		try {
			this.truckkm = new Float(truckkm);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("公里数不是有效的数字格式:" + truckkm);
		}
	}

	public Float getTruckstartkm() {
		return truckstartkm;
	}

	public void setTruckstartkm(Float truckstartkm) {
		this.truckstartkm = truckstartkm;
	}

	public void setTruckstartkm(String truckstartkm) {
		try {
			this.truckstartkm = new Float(truckstartkm);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("起始里程数不是有效的数字格式:" + truckstartkm);
		}
	}

	public int getTruckdriver() {
		return truckdriver;
	}

	public void setTruckdriver(int truckdriver) {
		this.truckdriver = truckdriver;
	}

	public int getTruckflag() {
		return truckflag;
	}

	public void setTruckflag(int truckflag) {
		this.truckflag = truckflag;
	}

}
