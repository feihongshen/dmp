package cn.explink.domain;

/**
 * 中转原因匹配
 * 
 * @author Administrator
 *
 */
public class TransferResMatch {

	private int id;
	private int reasonid;
	private String reasonname;
	private int transferReasonid;
	private String transferreasonname;
	private String remark;

	public String getReasonname() {
		return reasonname;
	}

	public void setReasonname(String reasonname) {
		this.reasonname = reasonname;
	}

	public String getTransferreasonname() {
		return transferreasonname;
	}

	public void setTransferreasonname(String transferreasonname) {
		this.transferreasonname = transferreasonname;
	}

	public int getTransferReasonid() {
		return transferReasonid;
	}

	public void setTransferReasonid(int transferReasonid) {
		this.transferReasonid = transferReasonid;
	}

	public int getReasonid() {
		return reasonid;
	}

	public void setReasonid(int reasonid) {
		this.reasonid = reasonid;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
