package cn.explink.dto;

/**
 * 投递率 返回实体
 * 
 * @author Administrator
 *
 */
public class DeliveryNewDTO {
	private long branchid; // 站点id
	private String customername; // 供货商名称
	private long customerid;// 供货商id
	private long tuoToucount;// 妥投数量
	private long ruKucount;// 入库数量
	private long youjiekucount;// 数量

	public long getBranchid() {
		return branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public long getCustomerid() {
		return customerid;
	}

	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	public long getTuoToucount() {
		return tuoToucount;
	}

	public void setTuoToucount(long tuoToucount) {
		this.tuoToucount = tuoToucount;
	}

	public long getRuKucount() {
		return ruKucount;
	}

	public void setRuKucount(long ruKucount) {
		this.ruKucount = ruKucount;
	}

	public long getYoujiekucount() {
		return youjiekucount;
	}

	public void setYoujiekucount(long youjiekucount) {
		this.youjiekucount = youjiekucount;
	}

}
