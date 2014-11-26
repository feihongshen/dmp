package cn.explink.domain.logdto;

/**
 * 今日到货日报
 * 
 * @author Administrator
 *
 */
public class TodayArrivalDTO {

	// ----------- 今日应到货 ------------

	private long zuorishaohuo = 0; // 昨日少货

	private long zuoriweidaohuo = 0; // 昨日未到货

	private long zuoritazhandaohuo = 0; // 昨日他站到货

	private long jinriyichuku = 0; // 今日出库

	// ----------- 今日实到货 ------------
	private long weidaohuo = 0;// 未到货

	private long yidaohuo = 0;// 已到货

	private long tazhandaohuo = 0;// 他站到货

	private long shaohuo = 0;// 少货

	private long daocuohuo = 0;// 到错货

	private long jinri_lousaodaozhan = 0;// 漏扫到站

	public long getZuorishaohuo() {
		return zuorishaohuo;
	}

	public void setZuorishaohuo(long zuorishaohuo) {
		this.zuorishaohuo = zuorishaohuo;
	}

	public long getZuoriweidaohuo() {
		return zuoriweidaohuo;
	}

	public void setZuoriweidaohuo(long zuoriweidaohuo) {
		this.zuoriweidaohuo = zuoriweidaohuo;
	}

	public long getZuoritazhandaohuo() {
		return zuoritazhandaohuo;
	}

	public void setZuoritazhandaohuo(long zuoritazhandaohuo) {
		this.zuoritazhandaohuo = zuoritazhandaohuo;
	}

	public long getJinriyichuku() {
		return jinriyichuku;
	}

	public void setJinriyichuku(long jinriyichuku) {
		this.jinriyichuku = jinriyichuku;
	}

	public long getWeidaohuo() {
		return weidaohuo;
	}

	public void setWeidaohuo(long weidaohuo) {
		this.weidaohuo = weidaohuo;
	}

	public long getYidaohuo() {
		return yidaohuo;
	}

	public void setYidaohuo(long yidaohuo) {
		this.yidaohuo = yidaohuo;
	}

	public long getTazhandaohuo() {
		return tazhandaohuo;
	}

	public void setTazhandaohuo(long tazhandaohuo) {
		this.tazhandaohuo = tazhandaohuo;
	}

	public long getShaohuo() {
		return shaohuo;
	}

	public void setShaohuo(long shaohuo) {
		this.shaohuo = shaohuo;
	}

	public long getDaocuohuo() {
		return daocuohuo;
	}

	public void setDaocuohuo(long daocuohuo) {
		this.daocuohuo = daocuohuo;
	}

	public long getJinri_lousaodaozhan() {
		return jinri_lousaodaozhan;
	}

	public void setJinri_lousaodaozhan(long jinri_lousaodaozhan) {
		this.jinri_lousaodaozhan = jinri_lousaodaozhan;
	}

}
