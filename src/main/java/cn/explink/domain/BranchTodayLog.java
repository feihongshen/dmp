package cn.explink.domain;

import java.math.BigDecimal;

public class BranchTodayLog {
	private long id = 0;// 主键id
	private long branchid = 0;// 站点id
	private String createdate = "";// 操作时间
	private long userid = 0;// 操作人

	// =====今日到货日报===========
	private long yesterday_stortage = 0;// 昨日少货
	private long yesterday_not_arrive = 0;// 昨日未到货
	private long yesterday_other_site_arrive = 0;// 昨日他站到货
	private long today_out_storehouse = 0;// 今日已出库
	private long today_not_arrive = 0;// 今日未到货
	private long today_arrive = 0;// 今日已到货
	private long today_other_site_arrive = 0;// 今日他站到货
	private long today_stortage = 0;// 今日少货
	private long today_wrong_arrive = 0;// 今日到错货

	// =======今日投递日报=============
	private long today_fankui_daohuo = 0;// 今日到货
	private long today_fankui_zhiliu = 0;// 今日滞留
	private long today_fankui_linghuo = 0;// 今日领货
	private long today_fankui_zuoriweiguiban = 0;// 昨日未归班
	private long today_fankui_peisongchenggong_count = 0;// 配送成功单数
	private BigDecimal today_fankui_peisongchenggong_money = new BigDecimal(0);// 配送成功金额
	private long today_fankui_jushou = 0;// 拒收
	private long today_fankui_leijizhiliu = 0;// 累计滞留
	private long today_fankui_bufenjushou = 0;// 部分拒收
	private long today_fankui_zhobgzhuan = 0;// 中转
	private long today_fankui_shangmentuichenggong_count = 0;// 上门退成功单数
	private BigDecimal today_fankui_shangmentuichenggong_money = new BigDecimal(0);// 上门退成功应退金额
	private long today_fankui_shangmentuijutui = 0;// 上门退拒退
	private long today_fankui_shangmenhuanchenggong_count = 0;// 上门换成功单数
	private BigDecimal today_fankui_shangmenhuanchenggong_yingshou_money = new BigDecimal(0);// 上门换成功应收金额
	private BigDecimal today_fankui_shangmenhuanchenggong_yingtui_money = new BigDecimal(0);// 上门换成功应退金额
	private long today_fankui_shangmenhuanjuhuan = 0;// 上门换拒换
	private long today_fankui_diushi = 0;// 丢失破损
	private long today_fankui_heji_count = 0;// 反馈合计单数
	private BigDecimal today_fankui_heji_yingshou_money = new BigDecimal(0);// 反馈合计应收金额
	private BigDecimal today_fankui_heji_yingtui_money = new BigDecimal(0);// 反馈合计应退金额
	private long today_weifankui_heji_count = 0;// 未反馈合计单数
	private BigDecimal today_weifankui_heji_yingshou_money = new BigDecimal(0);// 未反馈合计应收金额
	private BigDecimal today_weifankui_heji_yingtui_money = new BigDecimal(0);// 未反馈合计应退金额

	// =============今日款项日报=======
	private long today_funds_peisongchenggong_count = 0;// 配送成功单数
	private BigDecimal today_funds_peisongchenggong_cash = new BigDecimal(0);// 配送成功现金
	private BigDecimal today_funds_peisongchenggong_pos = new BigDecimal(0);// 配送成功pos
	private BigDecimal today_funds_peisongchenggong_checkfee = new BigDecimal(0);// 支票
	private long today_funds_shangmentuichenggong_count = 0;// 上门退成功单数
	private BigDecimal today_funds_shangmentuichenggong_money = new BigDecimal(0);// 上门退成功实付金额
	private BigDecimal today_funds_shangmentuichenggong_cash = new BigDecimal(0);// 上门退成功现金
	private BigDecimal today_funds_shangmentuichenggong_pos = new BigDecimal(0);// 上门退成功pos
	private BigDecimal today_funds_shangmentuichenggong_checkfee = new BigDecimal(0);// 上门退成功支票
	// =======加字段
	private BigDecimal shijiaokuan_cash_amount = new BigDecimal(0);// 所有货物中现金所占的总金额
	private BigDecimal shijiaokuan_pos_amount = new BigDecimal(0);// 所有货物中pos刷卡所占的总金额
	private BigDecimal shijiaokuan_checkfee_amount = new BigDecimal(0);// 所有货物中支票所占的总金额

	// ================今日库存日报==============
	// 昨日库存（单） 今日到货（单） 今日妥投（单） 今日出库（单） 今日库存（单）
	private long yesterday_kucun_count = 0;// 昨日库存
	private long today_kucun_arrive = 0;// 今日到货
	private long today_kucun_sucess = 0;// 今日妥投
	private long today_kucun_tuihuochuku = 0;// 今日退货出库
	private long today_kucun_zhongzhuanchuku = 0;// 今日中转出库
	private long today_kucun_count = 0;// 今日库存（累计） =
										// jushou_kuicun+zhiliu_kuicun+weiguiban_kuicun+toudi_daozhanweiling

	// =============加一个字段 工作备注==========
	private String jobRemark = "";// 工作备注
	// ======1月7号 新加字段
	private long shangmenhuanchenggong = 0; // 上门换成功
	private BigDecimal shangmenhuanchenggong_yingshou_amount = new BigDecimal(0);// 上门换成功应收金额
	private BigDecimal shangmenhuanchenggong_yingtui_amount = new BigDecimal(0);// 上门换成功应退金额
	private BigDecimal shishou_sum_amount = new BigDecimal(0);// 款项实收合计

	// =================新增字段============

	// 昨日库存 今日到货 今日应投 今日收款 今日出库 今日库存 今日投递率
	private long zuorikucun_count = 0; // 昨日库存
	private BigDecimal zuorikucun_money = new BigDecimal(0);//

	private long jinridaohuo_count = 0; // 今日到货
	private BigDecimal jinridaohuo_money = new BigDecimal(0);//

	private long jinriyingtou_count = 0;// 今日应投
	private BigDecimal jinrishoukuan_money = new BigDecimal(0);//

	private long jinrishoukuan_count = 0;// 今日妥投
	private BigDecimal jinriyingtou_money = new BigDecimal(0);//

	private long jinrichuku_count = 0;// 今日出库
	private BigDecimal jinrichuku_money = new BigDecimal(0);//

	private long jinrikucun_count = 0;// 今日库存 =
										// jushou_kuicun+zhiliu_kuicun+weiguiban_kuicun+toudi_daozhanweiling
	private BigDecimal jinrikucun_money = new BigDecimal(0);//

	private BigDecimal deliveryRate = new BigDecimal(0);// 投递率
	// /==========加字段============
	private long jushou_kuicun = 0;// 拒收的库存

	private long zhiliu_kuicun = 0;// 滞留的库存

	private long weiguiban_kuicun = 0;// 所有未归班的库存

	private long zuori_jushou_kuicun = 0;// 昨日拒收的库存

	private long zuori_zhiliu_kuicun = 0;// 昨日滞留的库存

	private long zuori_weiguiban_kuicun = 0;// 昨日所有未归班的库存

	private long toudi_daozhanweiling = 0;// 到站未领

	// ===========加字段===========
	private long today_fankuiweiguiban_heji_count = 0;// 反馈未归班合计单数
	private BigDecimal today_fankuiweiguiban_heji_yingshou_money = new BigDecimal(0);// 反馈未归班计应收金额
	private BigDecimal today_fankuiweiguiban_heji_yingtui_money = new BigDecimal(0);// 反馈未归班合计应退金额
	private long zuori_fankui_leijizhiliu = 0;// 昨日滞留 数据时动态的

	private long jinri_lousaodaozhan = 0;// 漏扫到站

	// 新加字段
	private long zuori_toudi_daozhanweiling = 0;// 昨日到站未领

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getBranchid() {
		return branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public String getCreatedate() {
		if (createdate != null && createdate.length() > 19) {
			return createdate.substring(0, 19);
		}
		return createdate;
	}

	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public long getYesterday_stortage() {
		return yesterday_stortage;
	}

	public void setYesterday_stortage(long yesterday_stortage) {
		this.yesterday_stortage = yesterday_stortage;
	}

	public long getYesterday_not_arrive() {
		return yesterday_not_arrive;
	}

	public void setYesterday_not_arrive(long yesterday_not_arrive) {
		this.yesterday_not_arrive = yesterday_not_arrive;
	}

	public long getYesterday_other_site_arrive() {
		return yesterday_other_site_arrive;
	}

	public void setYesterday_other_site_arrive(long yesterday_other_site_arrive) {
		this.yesterday_other_site_arrive = yesterday_other_site_arrive;
	}

	public long getToday_out_storehouse() {
		return today_out_storehouse;
	}

	public void setToday_out_storehouse(long today_out_storehouse) {
		this.today_out_storehouse = today_out_storehouse;
	}

	public long getToday_not_arrive() {
		return today_not_arrive;
	}

	public void setToday_not_arrive(long today_not_arrive) {
		this.today_not_arrive = today_not_arrive;
	}

	public long getToday_arrive() {
		return today_arrive;
	}

	public void setToday_arrive(long today_arrive) {
		this.today_arrive = today_arrive;
	}

	public long getToday_other_site_arrive() {
		return today_other_site_arrive;
	}

	public void setToday_other_site_arrive(long today_other_site_arrive) {
		this.today_other_site_arrive = today_other_site_arrive;
	}

	public long getToday_stortage() {
		return today_stortage;
	}

	public void setToday_stortage(long today_stortage) {
		this.today_stortage = today_stortage;
	}

	public long getToday_wrong_arrive() {
		return today_wrong_arrive;
	}

	public void setToday_wrong_arrive(long today_wrong_arrive) {
		this.today_wrong_arrive = today_wrong_arrive;
	}

	public long getToday_fankui_daohuo() {
		return today_fankui_daohuo;
	}

	public void setToday_fankui_daohuo(long today_fankui_daohuo) {
		this.today_fankui_daohuo = today_fankui_daohuo;
	}

	public long getToday_fankui_zhiliu() {
		return today_fankui_zhiliu;
	}

	public void setToday_fankui_zhiliu(long today_fankui_zhiliu) {
		this.today_fankui_zhiliu = today_fankui_zhiliu;
	}

	public long getToday_fankui_linghuo() {
		return today_fankui_linghuo;
	}

	public void setToday_fankui_linghuo(long today_fankui_linghuo) {
		this.today_fankui_linghuo = today_fankui_linghuo;
	}

	public long getToday_fankui_zuoriweiguiban() {
		return today_fankui_zuoriweiguiban;
	}

	public void setToday_fankui_zuoriweiguiban(long today_fankui_zuoriweiguiban) {
		this.today_fankui_zuoriweiguiban = today_fankui_zuoriweiguiban;
	}

	public long getToday_fankui_peisongchenggong_count() {
		return today_fankui_peisongchenggong_count;
	}

	public void setToday_fankui_peisongchenggong_count(long today_fankui_peisongchenggong_count) {
		this.today_fankui_peisongchenggong_count = today_fankui_peisongchenggong_count;
	}

	public BigDecimal getToday_fankui_peisongchenggong_money() {
		return today_fankui_peisongchenggong_money;
	}

	public void setToday_fankui_peisongchenggong_money(BigDecimal today_fankui_peisongchenggong_money) {
		this.today_fankui_peisongchenggong_money = today_fankui_peisongchenggong_money;
	}

	public long getToday_fankui_jushou() {
		return today_fankui_jushou;
	}

	public void setToday_fankui_jushou(long today_fankui_jushou) {
		this.today_fankui_jushou = today_fankui_jushou;
	}

	public long getToday_fankui_leijizhiliu() {
		return today_fankui_leijizhiliu;
	}

	public void setToday_fankui_leijizhiliu(long today_fankui_leijizhiliu) {
		this.today_fankui_leijizhiliu = today_fankui_leijizhiliu;
	}

	public long getToday_fankui_bufenjushou() {
		return today_fankui_bufenjushou;
	}

	public void setToday_fankui_bufenjushou(long today_fankui_bufenjushou) {
		this.today_fankui_bufenjushou = today_fankui_bufenjushou;
	}

	public long getToday_fankui_zhobgzhuan() {
		return today_fankui_zhobgzhuan;
	}

	public void setToday_fankui_zhobgzhuan(long today_fankui_zhobgzhuan) {
		this.today_fankui_zhobgzhuan = today_fankui_zhobgzhuan;
	}

	public long getToday_fankui_shangmentuichenggong_count() {
		return today_fankui_shangmentuichenggong_count;
	}

	public void setToday_fankui_shangmentuichenggong_count(long today_fankui_shangmentuichenggong_count) {
		this.today_fankui_shangmentuichenggong_count = today_fankui_shangmentuichenggong_count;
	}

	public BigDecimal getToday_fankui_shangmentuichenggong_money() {
		return today_fankui_shangmentuichenggong_money;
	}

	public void setToday_fankui_shangmentuichenggong_money(BigDecimal today_fankui_shangmentuichenggong_money) {
		this.today_fankui_shangmentuichenggong_money = today_fankui_shangmentuichenggong_money;
	}

	public long getToday_fankui_shangmentuijutui() {
		return today_fankui_shangmentuijutui;
	}

	public void setToday_fankui_shangmentuijutui(long today_fankui_shangmentuijutui) {
		this.today_fankui_shangmentuijutui = today_fankui_shangmentuijutui;
	}

	public long getToday_fankui_shangmenhuanchenggong_count() {
		return today_fankui_shangmenhuanchenggong_count;
	}

	public void setToday_fankui_shangmenhuanchenggong_count(long today_fankui_shangmenhuanchenggong_count) {
		this.today_fankui_shangmenhuanchenggong_count = today_fankui_shangmenhuanchenggong_count;
	}

	public BigDecimal getToday_fankui_shangmenhuanchenggong_yingshou_money() {
		return today_fankui_shangmenhuanchenggong_yingshou_money;
	}

	public void setToday_fankui_shangmenhuanchenggong_yingshou_money(BigDecimal today_fankui_shangmenhuanchenggong_yingshou_money) {
		this.today_fankui_shangmenhuanchenggong_yingshou_money = today_fankui_shangmenhuanchenggong_yingshou_money;
	}

	public BigDecimal getToday_fankui_shangmenhuanchenggong_yingtui_money() {
		return today_fankui_shangmenhuanchenggong_yingtui_money;
	}

	public void setToday_fankui_shangmenhuanchenggong_yingtui_money(BigDecimal today_fankui_shangmenhuanchenggong_yingtui_money) {
		this.today_fankui_shangmenhuanchenggong_yingtui_money = today_fankui_shangmenhuanchenggong_yingtui_money;
	}

	public long getToday_fankui_shangmenhuanjuhuan() {
		return today_fankui_shangmenhuanjuhuan;
	}

	public void setToday_fankui_shangmenhuanjuhuan(long today_fankui_shangmenhuanjuhuan) {
		this.today_fankui_shangmenhuanjuhuan = today_fankui_shangmenhuanjuhuan;
	}

	public long getToday_fankui_diushi() {
		return today_fankui_diushi;
	}

	public void setToday_fankui_diushi(long today_fankui_diushi) {
		this.today_fankui_diushi = today_fankui_diushi;
	}

	public long getToday_fankui_heji_count() {
		return today_fankui_heji_count;
	}

	public void setToday_fankui_heji_count(long today_fankui_heji_count) {
		this.today_fankui_heji_count = today_fankui_heji_count;
	}

	public BigDecimal getToday_fankui_heji_yingshou_money() {
		return today_fankui_heji_yingshou_money;
	}

	public void setToday_fankui_heji_yingshou_money(BigDecimal today_fankui_heji_yingshou_money) {
		this.today_fankui_heji_yingshou_money = today_fankui_heji_yingshou_money;
	}

	public BigDecimal getToday_fankui_heji_yingtui_money() {
		return today_fankui_heji_yingtui_money;
	}

	public void setToday_fankui_heji_yingtui_money(BigDecimal today_fankui_heji_yingtui_money) {
		this.today_fankui_heji_yingtui_money = today_fankui_heji_yingtui_money;
	}

	public long getToday_weifankui_heji_count() {
		return today_weifankui_heji_count;
	}

	public void setToday_weifankui_heji_count(long today_weifankui_heji_count) {
		this.today_weifankui_heji_count = today_weifankui_heji_count;
	}

	public BigDecimal getToday_weifankui_heji_yingshou_money() {
		return today_weifankui_heji_yingshou_money;
	}

	public void setToday_weifankui_heji_yingshou_money(BigDecimal today_weifankui_heji_yingshou_money) {
		this.today_weifankui_heji_yingshou_money = today_weifankui_heji_yingshou_money;
	}

	public BigDecimal getToday_weifankui_heji_yingtui_money() {
		return today_weifankui_heji_yingtui_money;
	}

	public void setToday_weifankui_heji_yingtui_money(BigDecimal today_weifankui_heji_yingtui_money) {
		this.today_weifankui_heji_yingtui_money = today_weifankui_heji_yingtui_money;
	}

	public long getToday_funds_peisongchenggong_count() {
		return today_funds_peisongchenggong_count;
	}

	public void setToday_funds_peisongchenggong_count(long today_funds_peisongchenggong_count) {
		this.today_funds_peisongchenggong_count = today_funds_peisongchenggong_count;
	}

	public BigDecimal getToday_funds_peisongchenggong_cash() {
		return today_funds_peisongchenggong_cash;
	}

	public void setToday_funds_peisongchenggong_cash(BigDecimal today_funds_peisongchenggong_cash) {
		this.today_funds_peisongchenggong_cash = today_funds_peisongchenggong_cash;
	}

	public BigDecimal getToday_funds_peisongchenggong_pos() {
		return today_funds_peisongchenggong_pos;
	}

	public void setToday_funds_peisongchenggong_pos(BigDecimal today_funds_peisongchenggong_pos) {
		this.today_funds_peisongchenggong_pos = today_funds_peisongchenggong_pos;
	}

	public BigDecimal getToday_funds_peisongchenggong_checkfee() {
		return today_funds_peisongchenggong_checkfee;
	}

	public void setToday_funds_peisongchenggong_checkfee(BigDecimal today_funds_peisongchenggong_checkfee) {
		this.today_funds_peisongchenggong_checkfee = today_funds_peisongchenggong_checkfee;
	}

	public long getToday_funds_shangmentuichenggong_count() {
		return today_funds_shangmentuichenggong_count;
	}

	public void setToday_funds_shangmentuichenggong_count(long today_funds_shangmentuichenggong_count) {
		this.today_funds_shangmentuichenggong_count = today_funds_shangmentuichenggong_count;
	}

	public BigDecimal getToday_funds_shangmentuichenggong_money() {
		return today_funds_shangmentuichenggong_money;
	}

	public void setToday_funds_shangmentuichenggong_money(BigDecimal today_funds_shangmentuichenggong_money) {
		this.today_funds_shangmentuichenggong_money = today_funds_shangmentuichenggong_money;
	}

	public BigDecimal getToday_funds_shangmentuichenggong_cash() {
		return today_funds_shangmentuichenggong_cash;
	}

	public void setToday_funds_shangmentuichenggong_cash(BigDecimal today_funds_shangmentuichenggong_cash) {
		this.today_funds_shangmentuichenggong_cash = today_funds_shangmentuichenggong_cash;
	}

	public BigDecimal getToday_funds_shangmentuichenggong_pos() {
		return today_funds_shangmentuichenggong_pos;
	}

	public void setToday_funds_shangmentuichenggong_pos(BigDecimal today_funds_shangmentuichenggong_pos) {
		this.today_funds_shangmentuichenggong_pos = today_funds_shangmentuichenggong_pos;
	}

	public BigDecimal getToday_funds_shangmentuichenggong_checkfee() {
		return today_funds_shangmentuichenggong_checkfee;
	}

	public void setToday_funds_shangmentuichenggong_checkfee(BigDecimal today_funds_shangmentuichenggong_checkfee) {
		this.today_funds_shangmentuichenggong_checkfee = today_funds_shangmentuichenggong_checkfee;
	}

	public long getYesterday_kucun_count() {
		return yesterday_kucun_count;
	}

	public void setYesterday_kucun_count(long yesterday_kucun_count) {
		this.yesterday_kucun_count = yesterday_kucun_count;
	}

	public long getToday_kucun_arrive() {
		return today_kucun_arrive;
	}

	public void setToday_kucun_arrive(long today_kucun_arrive) {
		this.today_kucun_arrive = today_kucun_arrive;
	}

	public long getToday_kucun_sucess() {
		return today_kucun_sucess;
	}

	public void setToday_kucun_sucess(long today_kucun_sucess) {
		this.today_kucun_sucess = today_kucun_sucess;
	}

	public long getToday_kucun_tuihuochuku() {
		return today_kucun_tuihuochuku;
	}

	public void setToday_kucun_tuihuochuku(long today_kucun_tuihuochuku) {
		this.today_kucun_tuihuochuku = today_kucun_tuihuochuku;
	}

	public long getToday_kucun_zhongzhuanchuku() {
		return today_kucun_zhongzhuanchuku;
	}

	public void setToday_kucun_zhongzhuanchuku(long today_kucun_zhongzhuanchuku) {
		this.today_kucun_zhongzhuanchuku = today_kucun_zhongzhuanchuku;
	}

	public long getToday_kucun_count() {
		return jushou_kuicun + zhiliu_kuicun + weiguiban_kuicun + toudi_daozhanweiling;
	}

	public void setToday_kucun_count(long today_kucun_count) {
		this.today_kucun_count = today_kucun_count;
	}

	public String getJobRemark() {
		return jobRemark;
	}

	public void setJobRemark(String jobRemark) {
		this.jobRemark = jobRemark;
	}

	// /=============加字段=============
	public long getShangmenhuanchenggong() {
		return shangmenhuanchenggong;
	}

	public void setShangmenhuanchenggong(long shangmenhuanchenggong) {
		this.shangmenhuanchenggong = shangmenhuanchenggong;
	}

	public BigDecimal getShangmenhuanchenggong_yingshou_amount() {
		return shangmenhuanchenggong_yingshou_amount;
	}

	public void setShangmenhuanchenggong_yingshou_amount(BigDecimal shangmenhuanchenggong_yingshou_amount) {
		this.shangmenhuanchenggong_yingshou_amount = shangmenhuanchenggong_yingshou_amount;
	}

	public BigDecimal getShangmenhuanchenggong_yingtui_amount() {
		return shangmenhuanchenggong_yingtui_amount;
	}

	public void setShangmenhuanchenggong_yingtui_amount(BigDecimal shangmenhuanchenggong_yingtui_amount) {
		this.shangmenhuanchenggong_yingtui_amount = shangmenhuanchenggong_yingtui_amount;
	}

	public BigDecimal getShishou_sum_amount() {
		return shishou_sum_amount;
	}

	public void setShishou_sum_amount(BigDecimal shishou_sum_amount) {
		this.shishou_sum_amount = shishou_sum_amount;
	}

	// /=============再加字段=============
	public long getZuorikucun_count() {
		return zuorikucun_count;
	}

	public void setZuorikucun_count(long zuorikucun_count) {
		this.zuorikucun_count = zuorikucun_count;
	}

	public BigDecimal getZuorikucun_money() {
		return zuorikucun_money;
	}

	public void setZuorikucun_money(BigDecimal zuorikucun_money) {
		this.zuorikucun_money = zuorikucun_money;
	}

	public long getJinridaohuo_count() {
		return jinridaohuo_count;
	}

	public void setJinridaohuo_count(long jinridaohuo_count) {
		this.jinridaohuo_count = jinridaohuo_count;
	}

	public BigDecimal getJinridaohuo_money() {
		return jinridaohuo_money;
	}

	public void setJinridaohuo_money(BigDecimal jinridaohuo_money) {
		this.jinridaohuo_money = jinridaohuo_money;
	}

	public long getJinriyingtou_count() {
		return jinriyingtou_count;
	}

	public void setJinriyingtou_count(long jinriyingtou_count) {
		this.jinriyingtou_count = jinriyingtou_count;
	}

	public BigDecimal getJinrishoukuan_money() {
		return jinrishoukuan_money;
	}

	public void setJinrishoukuan_money(BigDecimal jinrishoukuan_money) {
		this.jinrishoukuan_money = jinrishoukuan_money;
	}

	public long getJinrishoukuan_count() {
		return jinrishoukuan_count;
	}

	public void setJinrishoukuan_count(long jinrishoukuan_count) {
		this.jinrishoukuan_count = jinrishoukuan_count;
	}

	public BigDecimal getJinriyingtou_money() {
		return jinriyingtou_money;
	}

	public void setJinriyingtou_money(BigDecimal jinriyingtou_money) {
		this.jinriyingtou_money = jinriyingtou_money;
	}

	public long getJinrichuku_count() {
		return jinrichuku_count;
	}

	public void setJinrichuku_count(long jinrichuku_count) {
		this.jinrichuku_count = jinrichuku_count;
	}

	public BigDecimal getJinrichuku_money() {
		return jinrichuku_money;
	}

	public void setJinrichuku_money(BigDecimal jinrichuku_money) {
		this.jinrichuku_money = jinrichuku_money;
	}

	public long getJinrikucun_count() {
		return jushou_kuicun + zhiliu_kuicun + weiguiban_kuicun + toudi_daozhanweiling;
	}

	public void setJinrikucun_count(long jinrikucun_count) {
		this.jinrikucun_count = jinrikucun_count;
	}

	public BigDecimal getJinrikucun_money() {
		return jinrikucun_money;
	}

	public void setJinrikucun_money(BigDecimal jinrikucun_money) {
		this.jinrikucun_money = jinrikucun_money;
	}

	public BigDecimal getDeliveryRate() {
		return deliveryRate;
	}

	public void setDeliveryRate(BigDecimal deliveryRate) {
		this.deliveryRate = deliveryRate;
	}

	public long getJushou_kuicun() {
		return jushou_kuicun;
	}

	public void setJushou_kuicun(long jushou_kuicun) {
		this.jushou_kuicun = jushou_kuicun;
	}

	public long getZhiliu_kuicun() {
		return zhiliu_kuicun;
	}

	public void setZhiliu_kuicun(long zhiliu_kuicun) {
		this.zhiliu_kuicun = zhiliu_kuicun;
	}

	public long getWeiguiban_kuicun() {
		return weiguiban_kuicun;
	}

	public void setWeiguiban_kuicun(long weiguiban_kuicun) {
		this.weiguiban_kuicun = weiguiban_kuicun;
	}

	public long getZuori_jushou_kuicun() {
		return zuori_jushou_kuicun;
	}

	public void setZuori_jushou_kuicun(long zuori_jushou_kuicun) {
		this.zuori_jushou_kuicun = zuori_jushou_kuicun;
	}

	public long getZuori_zhiliu_kuicun() {
		return zuori_zhiliu_kuicun;
	}

	public void setZuori_zhiliu_kuicun(long zuori_zhiliu_kuicun) {
		this.zuori_zhiliu_kuicun = zuori_zhiliu_kuicun;
	}

	public long getZuori_weiguiban_kuicun() {
		return zuori_weiguiban_kuicun;
	}

	public void setZuori_weiguiban_kuicun(long zuori_weiguiban_kuicun) {
		this.zuori_weiguiban_kuicun = zuori_weiguiban_kuicun;
	}

	public long getToudi_daozhanweiling() {
		return toudi_daozhanweiling;
	}

	public void setToudi_daozhanweiling(long toudi_daozhanweiling) {
		this.toudi_daozhanweiling = toudi_daozhanweiling;
	}

	public BigDecimal getShijiaokuan_cash_amount() {
		return shijiaokuan_cash_amount;
	}

	public void setShijiaokuan_cash_amount(BigDecimal shijiaokuan_cash_amount) {
		this.shijiaokuan_cash_amount = shijiaokuan_cash_amount;
	}

	public BigDecimal getShijiaokuan_pos_amount() {
		return shijiaokuan_pos_amount;
	}

	public void setShijiaokuan_pos_amount(BigDecimal shijiaokuan_pos_amount) {
		this.shijiaokuan_pos_amount = shijiaokuan_pos_amount;
	}

	public BigDecimal getShijiaokuan_checkfee_amount() {
		return shijiaokuan_checkfee_amount;
	}

	public void setShijiaokuan_checkfee_amount(BigDecimal shijiaokuan_checkfee_amount) {
		this.shijiaokuan_checkfee_amount = shijiaokuan_checkfee_amount;
	}

	public long getToday_fankuiweiguiban_heji_count() {
		return today_fankuiweiguiban_heji_count;
	}

	public void setToday_fankuiweiguiban_heji_count(long today_fankuiweiguiban_heji_count) {
		this.today_fankuiweiguiban_heji_count = today_fankuiweiguiban_heji_count;
	}

	public BigDecimal getToday_fankuiweiguiban_heji_yingshou_money() {
		return today_fankuiweiguiban_heji_yingshou_money;
	}

	public void setToday_fankuiweiguiban_heji_yingshou_money(BigDecimal today_fankuiweiguiban_heji_yingshou_money) {
		this.today_fankuiweiguiban_heji_yingshou_money = today_fankuiweiguiban_heji_yingshou_money;
	}

	public BigDecimal getToday_fankuiweiguiban_heji_yingtui_money() {
		return today_fankuiweiguiban_heji_yingtui_money;
	}

	public void setToday_fankuiweiguiban_heji_yingtui_money(BigDecimal today_fankuiweiguiban_heji_yingtui_money) {
		this.today_fankuiweiguiban_heji_yingtui_money = today_fankuiweiguiban_heji_yingtui_money;
	}

	public long getZuori_fankui_leijizhiliu() {
		return zuori_fankui_leijizhiliu;
	}

	public void setZuori_fankui_leijizhiliu(long zuori_fankui_leijizhiliu) {
		this.zuori_fankui_leijizhiliu = zuori_fankui_leijizhiliu;
	}

	public long getJinri_lousaodaozhan() {
		return jinri_lousaodaozhan;
	}

	public void setJinri_lousaodaozhan(long jinri_lousaodaozhan) {
		this.jinri_lousaodaozhan = jinri_lousaodaozhan;
	}

	public long getZuori_toudi_daozhanweiling() {
		return zuori_toudi_daozhanweiling;
	}

	public void setZuori_toudi_daozhanweiling(long zuori_toudi_daozhanweiling) {
		this.zuori_toudi_daozhanweiling = zuori_toudi_daozhanweiling;
	}

}
