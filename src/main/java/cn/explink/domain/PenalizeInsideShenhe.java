package cn.explink.domain;

import java.math.BigDecimal;

public class PenalizeInsideShenhe {
	private long id;
	private String shenhedescribe;
	private long shenheresult;//1.审核为扣罚成立。2为审核为扣罚撤销
	private BigDecimal shenhepunishprice;
	private String shenheposition;
	private long shenheuserid;
	private long punishcwbstate;
	private BigDecimal resultqitaprice;//最终其它扣罚金额
	private BigDecimal resultgoodprice;//最终货物扣罚金额
	
	
	
	
	
	public BigDecimal getResultqitaprice() {
		return resultqitaprice;
	}
	public void setResultqitaprice(BigDecimal resultqitaprice) {
		this.resultqitaprice = resultqitaprice;
	}
	public BigDecimal getResultgoodprice() {
		return resultgoodprice;
	}
	public void setResultgoodprice(BigDecimal resultgoodprice) {
		this.resultgoodprice = resultgoodprice;
	}
	public long getPunishcwbstate() {
		return punishcwbstate;
	}
	public void setPunishcwbstate(long punishcwbstate) {
		this.punishcwbstate = punishcwbstate;
	}
	public long getShenheuserid() {
		return shenheuserid;
	}
	public void setShenheuserid(long shenheuserid) {
		this.shenheuserid = shenheuserid;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getShenhedescribe() {
		return shenhedescribe;
	}
	public void setShenhedescribe(String shenhedescribe) {
		this.shenhedescribe = shenhedescribe;
	}
	
	public long getShenheresult() {
		return shenheresult;
	}
	public void setShenheresult(long shenheresult) {
		this.shenheresult = shenheresult;
	}
	
	public BigDecimal getShenhepunishprice() {
		return shenhepunishprice;
	}
	public void setShenhepunishprice(BigDecimal shenhepunishprice) {
		this.shenhepunishprice = shenhepunishprice;
	}
	public String getShenheposition() {
		return shenheposition;
	}
	public void setShenheposition(String shenheposition) {
		this.shenheposition = shenheposition;
	}
	
}
