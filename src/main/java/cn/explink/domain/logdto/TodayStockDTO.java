package cn.explink.domain.logdto;

import java.math.BigDecimal;

/**
 * 今日库存日报
 * 
 * @author Administrator
 *
 */
public class TodayStockDTO {

	private long zuorikucun = 0; // 昨日库存

	private long jinridaohuo = 0; // 今日到货

	private long jinrituodou = 0; // 今日妥投

	private long jinrizhongzhuanchuku = 0; // 今日中转出库

	private long jinrituihuochuku = 0; // 今日退货出库

	private long jinrikucun = 0;// 今日库存

	private long jushou_kuicun = 0;// 拒收的库存

	private long zhiliu_kuicun = 0;// 滞留的库存

	private long weiguiban_kuicun = 0;// 所有未归班的库存

	private long zuori_jushou_kuicun = 0;// 昨日拒收的库存

	private long zuori_zhiliu_kuicun = 0;// 昨日滞留的库存

	private long zuori_weiguiban_kuicun = 0;// 昨日所有未归班的库存

	private long zuori_toudi_daozhanweiling = 0;// 昨日到站未领

	private long toudi_daozhanweiling = 0;// 今日到站未领
	private long today_fankui_diushi = 0;// 货物丢失

	public long getZuorikucun() {
		return zuorikucun;
	}

	public void setZuorikucun(long zuorikucun) {
		this.zuorikucun = zuorikucun;
	}

	public long getJinridaohuo() {
		return jinridaohuo;
	}

	public void setJinridaohuo(long jinridaohuo) {
		this.jinridaohuo = jinridaohuo;
	}

	public long getJinrituodou() {
		return jinrituodou;
	}

	public void setJinrituodou(long jinrituodou) {
		this.jinrituodou = jinrituodou;
	}

	public long getJinrizhongzhuanchuku() {
		return jinrizhongzhuanchuku;
	}

	public void setJinrizhongzhuanchuku(long jinrizhongzhuanchuku) {
		this.jinrizhongzhuanchuku = jinrizhongzhuanchuku;
	}

	public long getJinrituihuochuku() {
		return jinrituihuochuku;
	}

	public void setJinrituihuochuku(long jinrituihuochuku) {
		this.jinrituihuochuku = jinrituihuochuku;
	}

	public long getJinrikucun() {
		return jinrikucun;
	}

	public void setJinrikucun(long jinrikucun) {
		this.jinrikucun = jinrikucun;
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

	public long getZuori_toudi_daozhanweiling() {
		return zuori_toudi_daozhanweiling;
	}

	public void setZuori_toudi_daozhanweiling(long zuori_toudi_daozhanweiling) {
		this.zuori_toudi_daozhanweiling = zuori_toudi_daozhanweiling;
	}

	public long getToudi_daozhanweiling() {
		return toudi_daozhanweiling;
	}

	public void setToudi_daozhanweiling(long toudi_daozhanweiling) {
		this.toudi_daozhanweiling = toudi_daozhanweiling;
	}

	public long getToday_fankui_diushi() {
		return today_fankui_diushi;
	}

	public void setToday_fankui_diushi(long today_fankui_diushi) {
		this.today_fankui_diushi = today_fankui_diushi;
	}

}
