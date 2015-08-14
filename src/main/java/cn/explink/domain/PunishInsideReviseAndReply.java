package cn.explink.domain;

import java.math.BigDecimal;


public class PunishInsideReviseAndReply {
	private long id;
	private long dutybranchid;
	private long dutynameAdd;
	private String revisegoodprice;
	private String reviseqitaprice;
	private String koufajine;
	private String describe;
	private BigDecimal revisegoodpriceNew;
	private BigDecimal reviseqitapriceNew;
	private BigDecimal koufajineNew;
	
	
	public BigDecimal getKoufajineNew() {
		return koufajineNew;
	}
	public void setKoufajineNew(BigDecimal koufajineNew) {
		this.koufajineNew = koufajineNew;
	}
	public String getKoufajine() {
		return koufajine;
	}
	public void setKoufajine(String koufajine) {
		this.koufajine = koufajine;
	}
	public BigDecimal getRevisegoodpriceNew() {
		return revisegoodpriceNew;
	}
	public void setRevisegoodpriceNew(BigDecimal revisegoodpriceNew) {
		this.revisegoodpriceNew = revisegoodpriceNew;
	}
	public BigDecimal getReviseqitapriceNew() {
		return reviseqitapriceNew;
	}
	public void setReviseqitapriceNew(BigDecimal reviseqitapriceNew) {
		this.reviseqitapriceNew = reviseqitapriceNew;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getDutybranchid() {
		return dutybranchid;
	}
	public void setDutybranchid(long dutybranchid) {
		this.dutybranchid = dutybranchid;
	}
	public long getDutynameAdd() {
		return dutynameAdd;
	}
	public void setDutynameAdd(long dutynameAdd) {
		this.dutynameAdd = dutynameAdd;
	}
	public String getRevisegoodprice() {
		return revisegoodprice;
	}
	public void setRevisegoodprice(String revisegoodprice) {
		this.revisegoodprice = revisegoodprice;
	}
	public String getReviseqitaprice() {
		return reviseqitaprice;
	}
	public void setReviseqitaprice(String reviseqitaprice) {
		this.reviseqitaprice = reviseqitaprice;
	}

	
}
