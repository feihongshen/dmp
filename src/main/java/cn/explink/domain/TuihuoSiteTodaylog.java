package cn.explink.domain;

public class TuihuoSiteTodaylog {
	private long id = 0; // 主键
	private long userid = 0; // 用户id
	private String cteatetime = ""; // 创建时间
	private String starttime = ""; // 统计的开始时间
	private String endtime = ""; // 统计的结束时间
	private long tuihuoid = 0; // 退货站id (备用以后有多个库房)
	private long customerid = 0; // 供货商id
	private long zhandianyingtui = 0; // 站点应退
	private long zhandiantuihuochukuzaitu = 0;// 站点退货出站在途
	private long tuihuozhanruku = 0;// 退货站入库
	private long tuihuozhantuihuochukuzaitu = 0;// 退货站退货再投
	private long tuigonghuoshangchuku = 0;// 退供货商出库
	private long gonghuoshangshouhuo = 0;// 供货商收货
	private long gonghuoshangjushoufanku = 0;// 供货商拒收反馈

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

	public long getTuihuoid() {
		return tuihuoid;
	}

	public void setTuihuoid(long tuihuoid) {
		this.tuihuoid = tuihuoid;
	}

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public long getZhandianyingtui() {
		return zhandianyingtui;
	}

	public void setZhandianyingtui(long zhandianyingtui) {
		this.zhandianyingtui = zhandianyingtui;
	}

	public long getZhandiantuihuochukuzaitu() {
		return zhandiantuihuochukuzaitu;
	}

	public void setZhandiantuihuochukuzaitu(long zhandiantuihuochukuzaitu) {
		this.zhandiantuihuochukuzaitu = zhandiantuihuochukuzaitu;
	}

	public long getTuihuozhanruku() {
		return tuihuozhanruku;
	}

	public void setTuihuozhanruku(long tuihuozhanruku) {
		this.tuihuozhanruku = tuihuozhanruku;
	}

	public long getTuihuozhantuihuochukuzaitu() {
		return tuihuozhantuihuochukuzaitu;
	}

	public void setTuihuozhantuihuochukuzaitu(long tuihuozhantuihuochukuzaitu) {
		this.tuihuozhantuihuochukuzaitu = tuihuozhantuihuochukuzaitu;
	}

	public long getTuigonghuoshangchuku() {
		return tuigonghuoshangchuku;
	}

	public void setTuigonghuoshangchuku(long tuigonghuoshangchuku) {
		this.tuigonghuoshangchuku = tuigonghuoshangchuku;
	}

	public long getGonghuoshangshouhuo() {
		return gonghuoshangshouhuo;
	}

	public void setGonghuoshangshouhuo(long gonghuoshangshouhuo) {
		this.gonghuoshangshouhuo = gonghuoshangshouhuo;
	}

	public long getGonghuoshangjushoufanku() {
		return gonghuoshangjushoufanku;
	}

	public void setGonghuoshangjushoufanku(long gonghuoshangjushoufanku) {
		this.gonghuoshangjushoufanku = gonghuoshangjushoufanku;
	}

}
