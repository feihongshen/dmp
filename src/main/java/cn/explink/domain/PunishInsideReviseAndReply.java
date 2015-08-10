package cn.explink.domain;

import java.math.BigDecimal;

public class PunishInsideReviseAndReply {
	private long id;
	private long dutybranchid;
	private long dutynameAdd;
	private BigDecimal revisegoodprice;
	private BigDecimal reviseqitaprice;
	private BigDecimal koufajine;
	private String describe;
	
	
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
	public BigDecimal getRevisegoodprice() {
		return revisegoodprice;
	}
	public void setRevisegoodprice(BigDecimal revisegoodprice) {
		this.revisegoodprice = revisegoodprice;
	}
	public BigDecimal getReviseqitaprice() {
		return reviseqitaprice;
	}
	public void setReviseqitaprice(BigDecimal reviseqitaprice) {
		this.reviseqitaprice = reviseqitaprice;
	}
	public BigDecimal getKoufajine() {
		return koufajine;
	}
	public void setKoufajine(BigDecimal koufajine) {
		this.koufajine = koufajine;
	}
	
}
