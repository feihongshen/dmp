package cn.explink.b2c.gztlfeedback;

public enum ShipperIdAndShipperCode {
	fjabc(2,"fjabc","福建站"),
	sddsorder(8,"sddsorder","山东递速站"),
	njcborder(3,"njcborder","江苏晟邦站"),
	C9906(4,"C9906","北京大洋站"),
	GZTL_SH(7,"GZTL_SH","上海站"),
	GDKS(9,"GDKS","浙江凯树站"),
	jxfyorder(5,"jxfyorder","江西站"),
	hbfyorder(6,"hbfyorder","湖北站"),
	ZSRB(10,"ZSRB","中山火炬站"),
	GZTL(1,"GZTL","浙江站"),
	DGBY(11,"DGBY","东莞东站");
	private long  logisticproviderid;
	private String  shipped_code;
	private String explain;
	public long getLogisticproviderid() {
		return logisticproviderid;
	}
	public void setLogisticproviderid(long logisticproviderid) {
		this.logisticproviderid = logisticproviderid;
	}
	public String getShipped_code() {
		return shipped_code;
	}
	public void setShipped_code(String shipped_code) {
		this.shipped_code = shipped_code;
	}
	public String getExplain() {
		return explain;
	}
	public void setExplain(String explain) {
		this.explain = explain;
	}
	private ShipperIdAndShipperCode(long logisticproviderid, String shipped_code, String explain) {
		this.logisticproviderid = logisticproviderid;
		this.shipped_code = shipped_code;
		this.explain = explain;
	}
	


}
