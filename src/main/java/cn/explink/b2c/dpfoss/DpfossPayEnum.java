package cn.explink.b2c.dpfoss;

public enum DpfossPayEnum {

	CH("CH", "现金"), CD("CD", "银行卡"), TT("TT", "电汇"), NT("NT", "支票"), OL("OL", "网上支付"), CT("CT", "月结"), DT("DT", "临时欠款"), FC("FC", "到付"), ;
	private String paycode;

	public String getPaycode() {
		return paycode;
	}

	public void setPaycode(String paycode) {
		this.paycode = paycode;
	}

	public String getPayname() {
		return payname;
	}

	public void setPayname(String payname) {
		this.payname = payname;
	}

	private String payname;

	private DpfossPayEnum(String paycode, String payname) {
		this.paycode = paycode;
		this.payname = payname;
	}

}
