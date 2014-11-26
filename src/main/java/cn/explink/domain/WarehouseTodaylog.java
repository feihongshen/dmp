package cn.explink.domain;

public class WarehouseTodaylog {
	private long id = 0; // 主键
	private long userid = 0; // 用户id
	private String cteatetime = ""; // 创建时间
	private String starttime = ""; // 统计的开始时间
	private String endtime = ""; // 统计的结束时间
	private long warehouseid = 0; // 库房id (备用以后有多个库房)
	private long customerid = 0; // 供货商id
	private long zuori_kucun = 0; // 昨日库存
	private long jinri_weiruku = 0; // 今日未入库
	private long jinri_yiruku = 0; // 今日已入库
	private long jinri_daocuohuo = 0; // 今日倒错货
	private long jinri_rukuheji = 0; // 今日入库
	private long jinri_chukuzaitu = 0; // 今日出库在途
	private long jinri_chukuyidaozhan = 0; // 今日已到站
	private long jinri_kucun = 0; // 今日库存

	// ====加字段====
	private long jinri_weichukuyidaozhan = 0; // 今日未出库已到站
	private long zuori_chukuzaitu = 0; // 昨日出库在途

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public String getCteatetime() {
		return cteatetime;
	}

	public void setCteatetime(String cteatetime) {
		this.cteatetime = cteatetime;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public long getWarehouseid() {
		return warehouseid;
	}

	public void setWarehouseid(long warehouseid) {
		this.warehouseid = warehouseid;
	}

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public long getZuori_kucun() {
		return zuori_kucun;
	}

	public void setZuori_kucun(long zuori_kucun) {
		this.zuori_kucun = zuori_kucun;
	}

	public long getJinri_weiruku() {
		return jinri_weiruku;
	}

	public void setJinri_weiruku(long jinri_weiruku) {
		this.jinri_weiruku = jinri_weiruku;
	}

	public long getJinri_yiruku() {
		return jinri_yiruku;
	}

	public void setJinri_yiruku(long jinri_yiruku) {
		this.jinri_yiruku = jinri_yiruku;
	}

	public long getJinri_daocuohuo() {
		return jinri_daocuohuo;
	}

	public void setJinri_daocuohuo(long jinri_daocuohuo) {
		this.jinri_daocuohuo = jinri_daocuohuo;
	}

	public long getJinri_rukuheji() {
		return jinri_rukuheji;
	}

	public void setJinri_rukuheji(long jinri_rukuheji) {
		this.jinri_rukuheji = jinri_rukuheji;
	}

	public long getJinri_chukuzaitu() {
		return jinri_chukuzaitu;
	}

	public void setJinri_chukuzaitu(long jinri_chukuzaitu) {
		this.jinri_chukuzaitu = jinri_chukuzaitu;
	}

	public long getJinri_chukuyidaozhan() {
		return jinri_chukuyidaozhan;
	}

	public void setJinri_chukuyidaozhan(long jinri_chukuyidaozhan) {
		this.jinri_chukuyidaozhan = jinri_chukuyidaozhan;
	}

	public long getJinri_kucun() {
		return jinri_kucun;
	}

	public void setJinri_kucun(long jinri_kucun) {
		this.jinri_kucun = jinri_kucun;
	}

	public long getJinri_weichukuyidaozhan() {
		return jinri_weichukuyidaozhan;
	}

	public void setJinri_weichukuyidaozhan(long jinri_weichukuyidaozhan) {
		this.jinri_weichukuyidaozhan = jinri_weichukuyidaozhan;
	}

	public long getZuori_chukuzaitu() {
		return zuori_chukuzaitu;
	}

	public void setZuori_chukuzaitu(long zuori_chukuzaitu) {
		this.zuori_chukuzaitu = zuori_chukuzaitu;
	}

}
