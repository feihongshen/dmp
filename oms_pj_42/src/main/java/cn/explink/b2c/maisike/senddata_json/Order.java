package cn.explink.b2c.maisike.senddata_json;

import java.util.List;

public class Order {

	private String osn; // 订单唯一编号
	private int sid; // 营业厅唯一标识
	private String uname = ""; // 订单用户姓名
	private String uphone = ""; // 订单用户手机号或电话
	private String oarea = ""; // 订单所在地区（省 市 区 街道）
	private String oaddress = ""; // 订单配送详细地址
	private float oweight = 0; // 订单重量，单位:公斤
	private float ovolume = 0; // 订单体积，单位:立方米
	private String opaytype = ""; // 订单支付方式，请参考接口文档类型定义中派件支付方式类型定义
	private float opaymoney = 0; // 订单支付金额
	private int oischarge = 0;// 订单是否代收费，1收费，0不收费
	private float ochargemoney = 0;// 订单代收费金额，无代收费可填0

	private String otime = ""; // 订单生成时间

	private List<OrderPackage> opackagelist; // 订单包裹列表
	private List<OrderStatus> ostatuslist; // 订单历史状态列表

	public String getOsn() {
		return osn;
	}

	public void setOsn(String osn) {
		this.osn = osn;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getUphone() {
		return uphone;
	}

	public void setUphone(String uphone) {
		this.uphone = uphone;
	}

	public String getOarea() {
		return oarea;
	}

	public void setOarea(String oarea) {
		this.oarea = oarea;
	}

	public String getOaddress() {
		return oaddress;
	}

	public void setOaddress(String oaddress) {
		this.oaddress = oaddress;
	}

	public float getOweight() {
		return oweight;
	}

	public void setOweight(float oweight) {
		this.oweight = oweight;
	}

	public float getOvolume() {
		return ovolume;
	}

	public void setOvolume(float ovolume) {
		this.ovolume = ovolume;
	}

	public String getOpaytype() {
		return opaytype;
	}

	public void setOpaytype(String opaytype) {
		this.opaytype = opaytype;
	}

	public float getOpaymoney() {
		return opaymoney;
	}

	public void setOpaymoney(float opaymoney) {
		this.opaymoney = opaymoney;
	}

	public int getOischarge() {
		return oischarge;
	}

	public void setOischarge(int oischarge) {
		this.oischarge = oischarge;
	}

	public float getOchargemoney() {
		return ochargemoney;
	}

	public void setOchargemoney(float ochargemoney) {
		this.ochargemoney = ochargemoney;
	}

	public String getOtime() {
		return otime;
	}

	public void setOtime(String otime) {
		this.otime = otime;
	}

	public List<OrderPackage> getOpackagelist() {
		return opackagelist;
	}

	public void setOpackagelist(List<OrderPackage> opackagelist) {
		this.opackagelist = opackagelist;
	}

	public List<OrderStatus> getOstatuslist() {
		return ostatuslist;
	}

	public void setOstatuslist(List<OrderStatus> ostatuslist) {
		this.ostatuslist = ostatuslist;
	}

}
