package cn.explink.domain;

public class KeHuFaHuoData {

	private int id;
	private String begindate;
	private String enddate;
	private long kufangid;
	private long customerid;
	private long exportid;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBegindate() {
		return begindate;
	}

	public void setBegindate(String begindate) {
		this.begindate = begindate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public long getKufangid() {
		return kufangid;
	}

	public void setKufangid(long kufangid) {
		this.kufangid = kufangid;
	}

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public long getExportid() {
		return exportid;
	}

	public void setExportid(long exportid) {
		this.exportid = exportid;
	}

}
