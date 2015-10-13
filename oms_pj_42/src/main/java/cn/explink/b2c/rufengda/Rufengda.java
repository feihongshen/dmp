package cn.explink.b2c.rufengda;

/**
 * 如风达对接设置entity
 * 
 * @author Administrator
 *
 */
public class Rufengda {

	private String ws_url; // webservice 的url
	private String customerid; // 在系统中的customerid,只能唯一一个
	private String lcId; // 如风达分配的唯一标识
	private int maxCount; // 每次推送最大数量
	private String nowtime; // 从此时间段以后 查询出来的订单都会推送给 如风达

	private long warehouseid; // 订单入库库房

	public long getWarehouseid() {
		return warehouseid;
	}

	public void setWarehouseid(long warehouseid) {
		this.warehouseid = warehouseid;
	}

	private int isopensignflag; // kaiqi sign

	// java中配置.net中的加密信息 拆分存储
	private String modulePuk; // 公钥<Modulus></Modulus> 节点中的内容
	private String exponentPublic; // 公钥<Exponent></Exponent>中的内容
	private String modulePrk; // 私钥<Modulus></Modulus>节点中的内容
	private String exponetPrivate; // 私钥<D></D> 节点中的内容

	private String des_key; // 解密地址和电话用的key，规则key=des_key+count+lcId;

	public String getModulePuk() {
		return modulePuk;
	}

	public void setModulePuk(String modulePuk) {
		this.modulePuk = modulePuk;
	}

	public String getExponentPublic() {
		return exponentPublic;
	}

	public void setExponentPublic(String exponentPublic) {
		this.exponentPublic = exponentPublic;
	}

	public String getModulePrk() {
		return modulePrk;
	}

	public void setModulePrk(String modulePrk) {
		this.modulePrk = modulePrk;
	}

	public String getExponetPrivate() {
		return exponetPrivate;
	}

	public void setExponetPrivate(String exponetPrivate) {
		this.exponetPrivate = exponetPrivate;
	}

	public String getDes_key() {
		return des_key;
	}

	public void setDes_key(String desKey) {
		des_key = desKey;
	}

	public int getIsopensignflag() {
		return isopensignflag;
	}

	public void setIsopensignflag(int isopensignflag) {
		this.isopensignflag = isopensignflag;
	}

	public String getWs_url() {
		return ws_url;
	}

	public void setWs_url(String ws_url) {
		this.ws_url = ws_url;
	}

	public String getLcId() {
		return lcId;
	}

	public void setLcId(String lcId) {
		this.lcId = lcId;
	}

	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	public String getNowtime() {
		return nowtime;
	}

	public void setNowtime(String nowtime) {
		this.nowtime = nowtime;
	}

	public Rufengda() {

	}

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

}
