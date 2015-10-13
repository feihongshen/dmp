package cn.explink.dto;

public class CarAmountStatiticsDTO {

	private long totalcount;
	private double totalamount;
	private long backcount;
	private double backamount;
	private long successcount;
	private double successamount;
	private long kucuncount;
	private double kucunamount;
	private String customername;
	private long customerid;

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public long getTotalcount() {
		return totalcount;
	}

	public void setTotalcount(long totalcount) {
		this.totalcount = totalcount;
	}

	public double getTotalamount() {
		return totalamount;
	}

	public void setTotalamount(double totalamount) {
		this.totalamount = totalamount;
	}

	public long getBackcount() {
		return backcount;
	}

	public void setBackcount(long backcount) {
		this.backcount = backcount;
	}

	public double getBackamount() {
		return backamount;
	}

	public void setBackamount(double backamount) {
		this.backamount = backamount;
	}

	public long getSuccesscount() {
		return successcount;
	}

	public void setSuccesscount(long successcount) {
		this.successcount = successcount;
	}

	public double getSuccessamount() {
		return successamount;
	}

	public void setSuccessamount(double successamount) {
		this.successamount = successamount;
	}

	public long getKucuncount() {
		return kucuncount;
	}

	public void setKucuncount(long kucuncount) {
		this.kucuncount = kucuncount;
	}

	public double getKucunamount() {
		return kucunamount;
	}

	public void setKucunamount(double kucunamount) {
		this.kucunamount = kucunamount;
	}

}
