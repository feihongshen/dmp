package cn.explink.b2c.yihaodian;

/**
 * 一号店的订单类型
 * 
 * @author Administrator
 *
 */
public enum YiHaoDianOrderTypeEnum {
	PeiSong(1, "配送"), HuanHuoPeiSong(2, "换货配送单"), TuiHuoQuJian(5, "退货取件单"), HuanhuoQuJian(6, "换货取件单"), ;
	private int code;
	private String msg;

	private YiHaoDianOrderTypeEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
