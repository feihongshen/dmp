package cn.explink.domain;

/**
 * 库房出库汇总功能查询条件以及结果表
 *
 */
public class ChukuDataResult {
	private long id;
	private String begindate;
	private String enddate;
	private long kufangid;
	private long customerid;
	private String result;
	private long exportid;

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public long getExportid() {
		return exportid;
	}

	public void setExportid(long exportid) {
		this.exportid = exportid;
	}

}
