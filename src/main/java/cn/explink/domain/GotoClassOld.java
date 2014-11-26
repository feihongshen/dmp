package cn.explink.domain;

import java.math.BigDecimal;

public class GotoClassOld {

	private long id;
	private long gotoclassauditingid;
	private long nownumber;// 当日领货
	private long yiliu;// 遗留
	private long lishi_weishenhe;// 历史未审核
	private long zanbuchuli;// 暂不处理

	public long getSumCount() {
		return nownumber + yiliu + lishi_weishenhe + zanbuchuli;
	}

	private long peisong_chenggong;// 配送成功
	private BigDecimal peisong_chenggong_amount;// 配送成功金额
	private BigDecimal peisong_chenggong_pos_amount;// 配送成功金额
	private long tuihuo;// 退货
	private BigDecimal tuihuo_amount;// 退货金额
	private long bufentuihuo;// 部分退货
	private BigDecimal bufentuihuo_amount;// 部分退货金额
	private BigDecimal bufentuihuo_pos_amount;// 部分退货金额
	private long zhiliu;// 滞留
	private BigDecimal zhiliu_amount;// 滞留金额
	private long shangmentui_chenggong;// 上门退成功
	private BigDecimal shangmentui_chenggong_amount;// 上门退成功金额
	private BigDecimal shangmentui_chenggong_fare;// 上门退成功运费
	private long shangmentui_jutui;// 上门退拒退
	private BigDecimal shangmentui_jutui_amount;// 上门退拒退金额
	private BigDecimal shangmentui_jutui_fare;// 上门退拒退运费
	private long shangmenhuan_chenggong;// 上门换成功
	private BigDecimal shangmenhuan_chenggong_amount;// 上门换成功金额
	private BigDecimal shangmenhuan_chenggong_pos_amount;// 上门换成功金额
	private long diushi;// 丢失
	private BigDecimal diushi_amount;// 丢失金额

	public long getSumReturnCount() {
		return peisong_chenggong + tuihuo + bufentuihuo + zhiliu + shangmentui_chenggong + shangmentui_jutui + shangmenhuan_chenggong + diushi;
	}

	public BigDecimal getSumReturnCountAmount() {
		return peisong_chenggong_amount.add(tuihuo_amount).add(bufentuihuo_amount).add(zhiliu_amount).add(shangmentui_chenggong_amount).add(shangmentui_jutui_amount)
				.add(shangmenhuan_chenggong_amount).add(diushi_amount);
	}

	public BigDecimal getSumReturnCountPosAmount() {
		return peisong_chenggong_pos_amount.add(bufentuihuo_pos_amount).add(shangmenhuan_chenggong_pos_amount);
	}

	public BigDecimal getPeisong_chenggong_pos_amount() {
		return peisong_chenggong_pos_amount;
	}

	public void setPeisong_chenggong_pos_amount(BigDecimal peisong_chenggong_pos_amount) {
		this.peisong_chenggong_pos_amount = peisong_chenggong_pos_amount;
	}

	public BigDecimal getBufentuihuo_pos_amount() {
		return bufentuihuo_pos_amount;
	}

	public void setBufentuihuo_pos_amount(BigDecimal bufentuihuo_pos_amount) {
		this.bufentuihuo_pos_amount = bufentuihuo_pos_amount;
	}

	public BigDecimal getShangmenhuan_chenggong_pos_amount() {
		return shangmenhuan_chenggong_pos_amount;
	}

	public void setShangmenhuan_chenggong_pos_amount(BigDecimal shangmenhuan_chenggong_pos_amount) {
		this.shangmenhuan_chenggong_pos_amount = shangmenhuan_chenggong_pos_amount;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getGotoclassauditingid() {
		return gotoclassauditingid;
	}

	public void setGotoclassauditingid(long gotoclassauditingid) {
		this.gotoclassauditingid = gotoclassauditingid;
	}

	public long getNownumber() {
		return nownumber;
	}

	public void setNownumber(long nownumber) {
		this.nownumber = nownumber;
	}

	public long getYiliu() {
		return yiliu;
	}

	public void setYiliu(long yiliu) {
		this.yiliu = yiliu;
	}

	public long getLishi_weishenhe() {
		return lishi_weishenhe;
	}

	public void setLishi_weishenhe(long lishi_weishenhe) {
		this.lishi_weishenhe = lishi_weishenhe;
	}

	public long getZanbuchuli() {
		return zanbuchuli;
	}

	public void setZanbuchuli(long zanbuchuli) {
		this.zanbuchuli = zanbuchuli;
	}

	public long getPeisong_chenggong() {
		return peisong_chenggong;
	}

	public void setPeisong_chenggong(long peisong_chenggong) {
		this.peisong_chenggong = peisong_chenggong;
	}

	public BigDecimal getPeisong_chenggong_amount() {
		return peisong_chenggong_amount;
	}

	public void setPeisong_chenggong_amount(BigDecimal peisong_chenggong_amount) {
		this.peisong_chenggong_amount = peisong_chenggong_amount;
	}

	public long getTuihuo() {
		return tuihuo;
	}

	public void setTuihuo(long tuihuo) {
		this.tuihuo = tuihuo;
	}

	public BigDecimal getTuihuo_amount() {
		return tuihuo_amount;
	}

	public void setTuihuo_amount(BigDecimal tuihuo_amount) {
		this.tuihuo_amount = tuihuo_amount;
	}

	public long getBufentuihuo() {
		return bufentuihuo;
	}

	public void setBufentuihuo(long bufentuihuo) {
		this.bufentuihuo = bufentuihuo;
	}

	public BigDecimal getBufentuihuo_amount() {
		return bufentuihuo_amount;
	}

	public void setBufentuihuo_amount(BigDecimal bufentuihuo_amount) {
		this.bufentuihuo_amount = bufentuihuo_amount;
	}

	public long getZhiliu() {
		return zhiliu;
	}

	public void setZhiliu(long zhiliu) {
		this.zhiliu = zhiliu;
	}

	public BigDecimal getZhiliu_amount() {
		return zhiliu_amount;
	}

	public void setZhiliu_amount(BigDecimal zhiliu_amount) {
		this.zhiliu_amount = zhiliu_amount;
	}

	public long getShangmentui_chenggong() {
		return shangmentui_chenggong;
	}

	public void setShangmentui_chenggong(long shangmentui_chenggong) {
		this.shangmentui_chenggong = shangmentui_chenggong;
	}

	public BigDecimal getShangmentui_chenggong_amount() {
		return shangmentui_chenggong_amount;
	}

	public void setShangmentui_chenggong_amount(BigDecimal shangmentui_chenggong_amount) {
		this.shangmentui_chenggong_amount = shangmentui_chenggong_amount;
	}

	public long getShangmentui_jutui() {
		return shangmentui_jutui;
	}

	public void setShangmentui_jutui(long shangmentui_jutui) {
		this.shangmentui_jutui = shangmentui_jutui;
	}

	public BigDecimal getShangmentui_jutui_amount() {
		return shangmentui_jutui_amount;
	}

	public void setShangmentui_jutui_amount(BigDecimal shangmentui_jutui_amount) {
		this.shangmentui_jutui_amount = shangmentui_jutui_amount;
	}

	public long getShangmenhuan_chenggong() {
		return shangmenhuan_chenggong;
	}

	public void setShangmenhuan_chenggong(long shangmenhuan_chenggong) {
		this.shangmenhuan_chenggong = shangmenhuan_chenggong;
	}

	public BigDecimal getShangmenhuan_chenggong_amount() {
		return shangmenhuan_chenggong_amount;
	}

	public void setShangmenhuan_chenggong_amount(BigDecimal shangmenhuan_chenggong_amount) {
		this.shangmenhuan_chenggong_amount = shangmenhuan_chenggong_amount;
	}

	public long getDiushi() {
		return diushi;
	}

	public void setDiushi(long diushi) {
		this.diushi = diushi;
	}

	public BigDecimal getDiushi_amount() {
		return diushi_amount;
	}

	public void setDiushi_amount(BigDecimal diushi_amount) {
		this.diushi_amount = diushi_amount;
	}

	public BigDecimal getShangmentui_chenggong_fare() {
		return shangmentui_chenggong_fare;
	}

	public void setShangmentui_chenggong_fare(BigDecimal shangmentui_chenggong_fare) {
		this.shangmentui_chenggong_fare = shangmentui_chenggong_fare;
	}

	public BigDecimal getShangmentui_jutui_fare() {
		return shangmentui_jutui_fare;
	}

	public void setShangmentui_jutui_fare(BigDecimal shangmentui_jutui_fare) {
		this.shangmentui_jutui_fare = shangmentui_jutui_fare;
	}

}
