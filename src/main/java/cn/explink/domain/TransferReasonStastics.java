package cn.explink.domain;

/**
 * 中转原因 统计
 * 
 * @author Administrator
 *
 */
public class TransferReasonStastics {

	private long id;
	private String inwarehousetime; // 入库时间
	private String outwarehousetime; // 出库时间
	private String cwb; // 订单号
	private int transferreasonid; // 中转原因id

	public long getId() {
		return id;
	}

	public int getTransferreasonid() {
		return transferreasonid;
	}

	public void setTransferreasonid(int transferreasonid) {
		this.transferreasonid = transferreasonid;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getInwarehousetime() {
		return inwarehousetime;
	}

	public void setInwarehousetime(String inwarehousetime) {
		this.inwarehousetime = inwarehousetime;
	}

	public String getOutwarehousetime() {
		return outwarehousetime;
	}

	public void setOutwarehousetime(String outwarehousetime) {
		this.outwarehousetime = outwarehousetime;
	}

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public String getNowtime() {
		return nowtime;
	}

	public void setNowtime(String nowtime) {
		this.nowtime = nowtime;
	}

	private String nowtime;

}
