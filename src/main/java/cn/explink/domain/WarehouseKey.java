package cn.explink.domain;

public class WarehouseKey {
	private long id;
	private long targetcarwarehouseid;
	private String keyname;
	private long ifeffectflag;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTargetcarwarehouseid() {
		return targetcarwarehouseid;
	}

	public void setTargetcarwarehouseid(long targetcarwarehouseid) {
		this.targetcarwarehouseid = targetcarwarehouseid;
	}

	public String getKeyname() {
		return keyname;
	}

	public void setKeyname(String keyname) {
		this.keyname = keyname;
	}

	public long getIfeffectflag() {
		return ifeffectflag;
	}

	public void setIfeffectflag(long ifeffectflag) {
		this.ifeffectflag = ifeffectflag;
	}

}
