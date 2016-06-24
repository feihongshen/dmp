package cn.explink.vo;
/**
 * tps运单号实体
 * @author yurong.liang 2016-06-17
 */
public class TPStranscwb {
	private long id;//主键
	private String tpstranscwb;//品骏运单号
	private int printStatus;//打印状态
	private String printUser;//打印人
	private String printTime;//打印时间
	private String createTime;//创建时间
	private String updateTime;//更新时间
	private String printStatusTitle;//打印状态
	
	
	public String getPrintTime() {
		return printTime;
	}
	public void setPrintTime(String printTime) {
		this.printTime = printTime;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTpstranscwb() {
		return tpstranscwb;
	}
	public int getPrintStatus() {
		return printStatus;
	}
	public String getPrintUser() {
		return printUser;
	}
	public String getCreateTime() {
		return createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public String getPrintStatusTitle() {
		return printStatusTitle;
	}
	public void setTpstranscwb(String tpstranscwb) {
		this.tpstranscwb = tpstranscwb;
	}
	public void setPrintStatus(int printStatus) {
		this.printStatus = printStatus;
	}
	public void setPrintUser(String printUser) {
		this.printUser = printUser;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public void setPrintStatusTitle(String printStatusTitle) {
		this.printStatusTitle = printStatusTitle;
	}
}
