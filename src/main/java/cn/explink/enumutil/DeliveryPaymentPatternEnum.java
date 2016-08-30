package cn.explink.enumutil;

/**
 * 订单付款支付类型
 * @author chunlei05.li
 * @date 2016年8月25日 下午12:54:47
 */
public enum DeliveryPaymentPatternEnum {
	
	CASH(1, "现金"), POS(2, "POS机"), CODPOS(3, "支付宝COD扫码"), CHECKFEE(4, "支票"),  OTHERFEE(5, "其他方式");
	
	private int payno;
	
	private String payname;
	
	DeliveryPaymentPatternEnum(int payno, String payname) {
		this.setPayno(payno);
		this.setPayname(payname);
	}
	
	public static DeliveryPaymentPatternEnum getByPayno(int payno) {
		for (DeliveryPaymentPatternEnum e : DeliveryPaymentPatternEnum.values()) {
			if (e.payno == payno) {
				return e;
			}
		}
		return null;
	}

	public int getPayno() {
		return payno;
	}

	public void setPayno(int payno) {
		this.payno = payno;
	}

	public String getPayname() {
		return payname;
	}

	public void setPayname(String payname) {
		this.payname = payname;
	}
}
