package cn.explink.domain.logdto;

import java.math.BigDecimal;

public class TodayDeliveryDTO {
	// =======今日投递日报=============
	private long today_fankui_daohuo = 0;// 今日到货
	private long today_fankui_zhiliu = 0;// 今日滞留
	private long today_fankui_linghuo = 0;// 今日领货
	private long today_fankui_zuoriweiguiban = 0;// 昨日未归班
	private long today_fankui_peisongchenggong_count = 0;// 配送成功单数
	private BigDecimal today_fankui_peisongchenggong_money = new BigDecimal(0);// 配送成功金额
	private long today_fankui_jushou = 0;// 拒收
	private long today_fankui_leijizhiliu = 0;// 累计滞留
	private long today_fankui_bufenjushou = 0;// 累计滞留
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

	private long toudi_daozhanweiling;// 到站未领
	private long zuori_toudi_daozhanweiling;// 到站未领

	private long today_fankuiweiguiban_heji_count = 0;// 反馈未归班合计单数
	private BigDecimal today_fankuiweiguiban_heji_yingshou_money = new BigDecimal(0);// 反馈未归班计应收金额
	private BigDecimal today_fankuiweiguiban_heji_yingtui_money = new BigDecimal(0);// 反馈未归班合计应退金额
	private long zuori_fankui_leijizhiliu = 0;// 昨日滞留 数据时动态的

	private long jinri_lousaodaozhan = 0;// 漏扫到站

	private long zuori_linghuoweifankui = 0;// 昨日领货未反馈

	private long zuori_zhiliu_kuicun = 0;// 昨日滞留库存
	private long zuori_weiguiban_kuicun = 0;// 昨日未归班 库存
	private BigDecimal tuotoulv = new BigDecimal(0);// 妥投率

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

	public long getToudi_daozhanweiling() {
		return toudi_daozhanweiling;
	}

	public void setToudi_daozhanweiling(long toudi_daozhanweiling) {
		this.toudi_daozhanweiling = toudi_daozhanweiling;
	}

	public long getZuori_toudi_daozhanweiling() {
		return zuori_toudi_daozhanweiling;
	}

	public void setZuori_toudi_daozhanweiling(long zuori_toudi_daozhanweiling) {
		this.zuori_toudi_daozhanweiling = zuori_toudi_daozhanweiling;
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

	public long getZuori_linghuoweifankui() {
		return zuori_linghuoweifankui;
	}

	public void setZuori_linghuoweifankui(long zuori_linghuoweifankui) {
		this.zuori_linghuoweifankui = zuori_linghuoweifankui;
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

	public BigDecimal getTuotoulv() {
		return tuotoulv;
	}

	public void setTuotoulv(BigDecimal tuotoulv) {
		this.tuotoulv = tuotoulv;
	}

}
