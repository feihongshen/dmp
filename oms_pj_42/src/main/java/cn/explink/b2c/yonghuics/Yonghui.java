package cn.explink.b2c.yonghuics;

public class Yonghui {
	private String userCode; // 用户代码，用来标识哪个承运商
	private String private_key; // 密钥信息
	private int exportCwb_pageSize; // 获取订单每次获取数量最多200 导出
	private String download_URL; // 导出订单数据URL。
	private String callback_URL; // 下载完成回调
	private String trackLog_URL; // 订单信息回传url
	private String customerids; // 在系统中的id
	private long warehouseid; // 订单入库库房Id
	private long callBackCount; // 每次回调数量
	private int isopenDataDownload; // 是否开启订单导入接口 0关闭 1打开
	private int loopcount = 10; // 每次下载循环次数

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getPrivate_key() {
		return private_key;
	}

	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}

	public int getExportCwb_pageSize() {
		return exportCwb_pageSize;
	}

	public void setExportCwb_pageSize(int exportCwb_pageSize) {
		this.exportCwb_pageSize = exportCwb_pageSize;
	}

	public String getDownload_URL() {
		return download_URL;
	}

	public void setDownload_URL(String download_URL) {
		this.download_URL = download_URL;
	}

	public String getCallback_URL() {
		return callback_URL;
	}

	public void setCallback_URL(String callback_URL) {
		this.callback_URL = callback_URL;
	}

	public String getTrackLog_URL() {
		return trackLog_URL;
	}

	public void setTrackLog_URL(String trackLog_URL) {
		this.trackLog_URL = trackLog_URL;
	}

	public String getCustomerids() {
		return customerids;
	}

	public void setCustomerids(String customerids) {
		this.customerids = customerids;
	}

	public long getWarehouseid() {
		return warehouseid;
	}

	public void setWarehouseid(long warehouseid) {
		this.warehouseid = warehouseid;
	}

	public long getCallBackCount() {
		return callBackCount;
	}

	public void setCallBackCount(long callBackCount) {
		this.callBackCount = callBackCount;
	}

	public int getIsopenDataDownload() {
		return isopenDataDownload;
	}

	public void setIsopenDataDownload(int isopenDataDownload) {
		this.isopenDataDownload = isopenDataDownload;
	}

	public int getLoopcount() {
		return loopcount;
	}

	public void setLoopcount(int loopcount) {
		this.loopcount = loopcount;
	}

}
