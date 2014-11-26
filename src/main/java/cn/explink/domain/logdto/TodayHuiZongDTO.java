package cn.explink.domain.logdto;

import java.math.BigDecimal;

/**
 * 今日汇总
 * 
 * @author Administrator
 *
 */
public class TodayHuiZongDTO {

	// 昨日库存 今日到货 今日应投 今日收款 今日出库 今日库存
	private long zuorikucun_count = 0; // 昨日库存
	private BigDecimal zuorikucun_money = new BigDecimal(0);//

	private long jinridaohuo_count = 0; // 今日到货
	private BigDecimal jinridaohuo_money = new BigDecimal(0);//

	private long jinriyingtou_count = 0;// 今日应投
	private BigDecimal jinriyingtou_money = new BigDecimal(0);//

	private long jinrishoukuan_count = 0;// 今日收款
	private BigDecimal jinrishoukuan_money = new BigDecimal(0);//

	private long jinrichuku_count = 0;// 今日出库
	private BigDecimal jinrichuku_money = new BigDecimal(0);//

	private long jinrikucun_count = 0;// 今日库存
	private BigDecimal jinrikucun_money = new BigDecimal(0);//

	private BigDecimal deliveryRate = new BigDecimal(0);// 投递率

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
		return jinrikucun_count;
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

}
