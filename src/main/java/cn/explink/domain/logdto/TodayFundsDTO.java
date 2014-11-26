package cn.explink.domain.logdto;

import java.math.BigDecimal;

/**
 * 今日款项日报
 * 
 * @author Administrator
 *
 */
public class TodayFundsDTO {

	// ----------- 实收款------------

	private long peisongchenggong = 0; // 配送成功
	private BigDecimal peisongchenggong_cash_amount = new BigDecimal(0);// 所有货物中现金所占的总金额
	private BigDecimal peisongchenggong_pos_amount = new BigDecimal(0);// 所有货物中pos刷卡所占的总金额
	private BigDecimal peisongchenggong_checkfee_amount = new BigDecimal(0);// 所有货物中支票所占的总金额

	private long shangmentuichenggong = 0; // 上门退成功
	private BigDecimal shangmentuichenggong_cash_amount = new BigDecimal(0);// 所有货物中现金所占的总金额

	private long shangmenhuanchenggong = 0; // 上门换成功
	private BigDecimal shangmenhuanchenggong_yingshou_amount = new BigDecimal(0);// 上门换成功应收金额
	private BigDecimal shangmenhuanchenggong_yingtui_amount = new BigDecimal(0);// 上门换成功应退金额

	// 实收总计
	private BigDecimal shishou_sum_amount = new BigDecimal(0);// 款项实收合计

	// ----------- 实缴款 ------------
	private BigDecimal shijiaokuan_cash_amount = new BigDecimal(0);// 所有货物中现金所占的总金额
	private BigDecimal shijiaokuan_pos_amount = new BigDecimal(0);// 所有货物中pos刷卡所占的总金额
	private BigDecimal shijiaokuan_checkfee_amount = new BigDecimal(0);// 所有货物中支票所占的总金额

	public long getPeisongchenggong() {
		return peisongchenggong;
	}

	public void setPeisongchenggong(long peisongchenggong) {
		this.peisongchenggong = peisongchenggong;
	}

	public BigDecimal getPeisongchenggong_cash_amount() {
		return peisongchenggong_cash_amount;
	}

	public void setPeisongchenggong_cash_amount(BigDecimal peisongchenggong_cash_amount) {
		this.peisongchenggong_cash_amount = peisongchenggong_cash_amount;
	}

	public BigDecimal getPeisongchenggong_pos_amount() {
		return peisongchenggong_pos_amount;
	}

	public void setPeisongchenggong_pos_amount(BigDecimal peisongchenggong_pos_amount) {
		this.peisongchenggong_pos_amount = peisongchenggong_pos_amount;
	}

	public BigDecimal getPeisongchenggong_checkfee_amount() {
		return peisongchenggong_checkfee_amount;
	}

	public void setPeisongchenggong_checkfee_amount(BigDecimal peisongchenggong_checkfee_amount) {
		this.peisongchenggong_checkfee_amount = peisongchenggong_checkfee_amount;
	}

	public long getShangmentuichenggong() {
		return shangmentuichenggong;
	}

	public void setShangmentuichenggong(long shangmentuichenggong) {
		this.shangmentuichenggong = shangmentuichenggong;
	}

	public BigDecimal getShangmentuichenggong_cash_amount() {
		return shangmentuichenggong_cash_amount;
	}

	public void setShangmentuichenggong_cash_amount(BigDecimal shangmentuichenggong_cash_amount) {
		this.shangmentuichenggong_cash_amount = shangmentuichenggong_cash_amount;
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

	// === 新加统计字段
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
		return peisongchenggong_cash_amount.add(peisongchenggong_pos_amount).add(peisongchenggong_checkfee_amount).add(shangmenhuanchenggong_yingshou_amount)
				.subtract(shangmentuichenggong_cash_amount).subtract(shangmenhuanchenggong_yingtui_amount);
	}

	public void setShishou_sum_amount(BigDecimal shishou_sum_amount) {
		this.shishou_sum_amount = shishou_sum_amount;
	}

}
