package cn.explink.controller;

import java.math.BigDecimal;

public class MonitorView {

	private long carwarehouse;// 入库仓库id

	private BigDecimal yingrukusum = BigDecimal.ZERO; // 应入库总金额
	private long yingrukucount;// 应入库数量

	private BigDecimal weirukusum = BigDecimal.ZERO;// 未入库总金额
	private long weirukucount;// 未入库数量

	private BigDecimal chukuzaitusum = BigDecimal.ZERO;// 出库在途
	private long chukuzaitucount;// 出库在途数量

	private BigDecimal kucunsum = BigDecimal.ZERO;// 库存总金额
	private long kucuncount;// 库存数量

	public long getCarwarehouse() {
		return carwarehouse;
	}

	public void setCarwarehouse(long carwarehouse) {
		this.carwarehouse = carwarehouse;
	}

	public BigDecimal getYingrukusum() {
		return yingrukusum;
	}

	public void setYingrukusum(BigDecimal yingrukusum) {
		this.yingrukusum = yingrukusum;
	}

	public long getYingrukucount() {
		return yingrukucount;
	}

	public void setYingrukucount(long yingrukucount) {
		this.yingrukucount = yingrukucount;
	}

	public BigDecimal getWeirukusum() {
		return weirukusum;
	}

	public void setWeirukusum(BigDecimal weirukusum) {
		this.weirukusum = weirukusum;
	}

	public long getWeirukucount() {
		return weirukucount;
	}

	public void setWeirukucount(long weirukucount) {
		this.weirukucount = weirukucount;
	}

	public BigDecimal getChukuzaitusum() {
		return chukuzaitusum;
	}

	public void setChukuzaitusum(BigDecimal chukuzaitusum) {
		this.chukuzaitusum = chukuzaitusum;
	}

	public long getChukuzaitucount() {
		return chukuzaitucount;
	}

	public void setChukuzaitucount(long chukuzaitucount) {
		this.chukuzaitucount = chukuzaitucount;
	}

	public BigDecimal getKucunsum() {
		return kucunsum;
	}

	public void setKucunsum(BigDecimal kucunsum) {
		this.kucunsum = kucunsum;
	}

	public long getKucuncount() {
		return kucuncount;
	}

	public void setKucuncount(long kucuncount) {
		this.kucuncount = kucuncount;
	}

}
