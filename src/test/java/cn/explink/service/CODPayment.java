package cn.explink.service;

public class CODPayment {
	String header;
	String[] detailList;
	String trailer;
	
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String[] getDetailList() {
		return detailList;
	}
	public void setDetailList(String[] detailList) {
		this.detailList = detailList;
	}
	public String getTrailer() {
		return trailer;
	}
	public void setTrailer(String trailer) {
		this.trailer = trailer;
	}
}
