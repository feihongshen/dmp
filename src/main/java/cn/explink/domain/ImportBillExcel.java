package cn.explink.domain;

import java.math.BigDecimal;

/**
 *
 * @author wangzhiyong
 *
 */
public class ImportBillExcel {
	private long id;
	private String cwb;
	private String billBatches;
	private BigDecimal jijiaMoney;
	private BigDecimal	xuzhongMoney;
	private BigDecimal  fandanMoney;
	private BigDecimal fanchengMoney;
	private BigDecimal daishoukuanshouxuMoney;
	private BigDecimal posShouxuMoney;
	private BigDecimal baojiaMoney;
	private BigDecimal baozhuangMoney;
	private BigDecimal ganxianbutieMoney;

	
	public String getBillBatches() {
		return billBatches;
	}
	public void setBillBatches(String billBatches) {
		this.billBatches = billBatches;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCwb() {
		return cwb;
	}
	public void setCwb(String cwb) {
		this.cwb = cwb;
	}
	public BigDecimal getJijiaMoney() {
		return jijiaMoney;
	}
	public void setJijiaMoney(BigDecimal jijiaMoney) {
		this.jijiaMoney = jijiaMoney;
	}
	public BigDecimal getXuzhongMoney() {
		return xuzhongMoney;
	}
	public void setXuzhongMoney(BigDecimal xuzhongMoney) {
		this.xuzhongMoney = xuzhongMoney;
	}
	public BigDecimal getFandanMoney() {
		return fandanMoney;
	}
	public void setFandanMoney(BigDecimal fandanMoney) {
		this.fandanMoney = fandanMoney;
	}
	public BigDecimal getFanchengMoney() {
		return fanchengMoney;
	}
	public void setFanchengMoney(BigDecimal fanchengMoney) {
		this.fanchengMoney = fanchengMoney;
	}
	public BigDecimal getDaishoukuanshouxuMoney() {
		return daishoukuanshouxuMoney;
	}
	public void setDaishoukuanshouxuMoney(BigDecimal daishoukuanshouxuMoney) {
		this.daishoukuanshouxuMoney = daishoukuanshouxuMoney;
	}
	public BigDecimal getPosShouxuMoney() {
		return posShouxuMoney;
	}
	public void setPosShouxuMoney(BigDecimal posShouxuMoney) {
		this.posShouxuMoney = posShouxuMoney;
	}
	public BigDecimal getBaojiaMoney() {
		return baojiaMoney;
	}
	public void setBaojiaMoney(BigDecimal baojiaMoney) {
		this.baojiaMoney = baojiaMoney;
	}
	public BigDecimal getBaozhuangMoney() {
		return baozhuangMoney;
	}
	public void setBaozhuangMoney(BigDecimal baozhuangMoney) {
		this.baozhuangMoney = baozhuangMoney;
	}
	public BigDecimal getGanxianbutieMoney() {
		return ganxianbutieMoney;
	}
	public void setGanxianbutieMoney(BigDecimal ganxianbutieMoney) {
		this.ganxianbutieMoney = ganxianbutieMoney;
	}
	
	

}
