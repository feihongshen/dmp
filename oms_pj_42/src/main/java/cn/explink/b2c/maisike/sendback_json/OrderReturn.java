package cn.explink.b2c.maisike.sendback_json;

import java.util.List;

import cn.explink.b2c.maisike.senddata_json.OrderPackage;
import cn.explink.b2c.maisike.senddata_json.OrderStatus;

public class OrderReturn {

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
	private String chgosn;// 换货订单号， 暂时没有
	private String returnproname; // 退换货商品名称
	private int returnpronum; // 退货商品数量
	private String returnpaytype; // 退款支付方式
	private float returnmoney; // 应退金额
	private String returntype; // 退换类型
	private int isvisit; // 是否上门取件 0 非上门 1上门 ,都默认为0
	private String returntime;

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

	public String getChgosn() {
		return chgosn;
	}

	public void setChgosn(String chgosn) {
		this.chgosn = chgosn;
	}

	public String getReturnproname() {
		return returnproname;
	}

	public void setReturnproname(String returnproname) {
		this.returnproname = returnproname;
	}

	public int getReturnpronum() {
		return returnpronum;
	}

	public void setReturnpronum(int returnpronum) {
		this.returnpronum = returnpronum;
	}

	public String getReturnpaytype() {
		return returnpaytype;
	}

	public void setReturnpaytype(String returnpaytype) {
		this.returnpaytype = returnpaytype;
	}

	public float getReturnmoney() {
		return returnmoney;
	}

	public void setReturnmoney(float returnmoney) {
		this.returnmoney = returnmoney;
	}

	public String getReturntype() {
		return returntype;
	}

	public void setReturntype(String returntype) {
		this.returntype = returntype;
	}

	public int getIsvisit() {
		return isvisit;
	}

	public void setIsvisit(int isvisit) {
		this.isvisit = isvisit;
	}

	public String getReturntime() {
		return returntime;
	}

	public void setReturntime(String returntime) {
		this.returntime = returntime;
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
