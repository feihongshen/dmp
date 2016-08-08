package cn.explink.b2c.tps;

public class TpsAutoCfg {
	private int autoOpenFlag; //是否开启自动化分拣对接: 0不开启;1开启
	private long warehouseid; // 订单导入库房ID
	private int batchnoOpenFlag; //是否开启tps交接单对接: 0不开启;1开启
	private int hebaoFlag; //是否需要合包: 0需要;1不需要

	public int getAutoOpenFlag() {
		return autoOpenFlag;
	}

	public void setAutoOpenFlag(int autoOpenFlag) {
		this.autoOpenFlag = autoOpenFlag;
	}

	public long getWarehouseid() {
		return warehouseid;
	}

	public void setWarehouseid(long warehouseid) {
		this.warehouseid = warehouseid;
	}

	public int getBatchnoOpenFlag() {
		return batchnoOpenFlag;
	}

	public void setBatchnoOpenFlag(int batchnoOpenFlag) {
		this.batchnoOpenFlag = batchnoOpenFlag;
	}

	public int getHebaoFlag() {
		return hebaoFlag;
	}

	public void setHebaoFlag(int hebaoFlag) {
		this.hebaoFlag = hebaoFlag;
	}

}
