package cn.explink.b2c.dangdang_dataimport;

/**
 * 当当配送方式的枚举
 * 
 * @author Administrator
 *
 */
public enum DeliveryTypeEnum {

	PuTongPeiSong("普通配送", 1), EMS("EMS", 2), PingYou("平邮", 3), JiaJiPeiSong("加急配送", 5), DangRiDi("当日达", 101), ShangMenZiTi("上门自提", 11), KuqiDiPeiSong("快递公司送货", 12), SiXiaoShiDa("4小时递", 7), WanJianSong(
			"晚间送", 13),guojikuaidi("国际快递",17),yuyuesonghuo("预约送货",18),dajianpeisong("大件配送",19)

	;
	private String msg_des; // 描述
	private int msg_code; // 编码

	private DeliveryTypeEnum(String msg_desc, int msg_code) {
		this.msg_code = msg_code;
		this.msg_des = msg_desc;
	}

	public String getMsg_des() {
		return msg_des;
	}

	public void setMsg_des(String msgDes) {
		msg_des = msgDes;
	}

	public int getMsg_code() {
		return msg_code;
	}

	public void setMsg_code(int msgCode) {
		msg_code = msgCode;
	}

}
