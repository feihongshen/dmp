package cn.explink.b2c.yihaodian;

public class Yihaodian {
	private String userCode; // 用户代码，用来标识哪个承运商
	private String private_key; // 密钥信息
	private int exportCwb_pageSize; // 获取订单每次获取数量最多200 导出
	private String exportCwb_URL; // 导出订单数据URL。
	private String deliveryResult_URL; // 配送结果反馈接口URL
	private String trackLog_URL; // 跟踪日志反馈接口URL
	private String customerids; // 在系统中的id
	private long warehouseid; // 订单入库库房Id
	private String updatePayResult_URL; // 订单支付信息修改URL
	private long callBackCount; // 每次回调数量
	private int isopenDataDownload; // 是否开启订单导入接口 0关闭 1打开
	private String ywcustomerid; //一号店药网customerid,如果 ywcustomerid=customerids,则指定同一个供货商
	
	private int isopenywaddressflag; //是否开启药网专用地址下载   0关闭 默认  1开启
	private String ywexportCwb_URL;
	private String ywexportSuccess_URL; // 订单数据导出成功后回调URL，告知一号店已经获取成功
	public String getYwexportSuccess_URL() {
		return ywexportSuccess_URL;
	}

	public void setYwexportSuccess_URL(String ywexportSuccess_URL) {
		this.ywexportSuccess_URL = ywexportSuccess_URL;
	}

	public int getIsopenywaddressflag() {
		return isopenywaddressflag;
	}

	public void setIsopenywaddressflag(int isopenywaddressflag) {
		this.isopenywaddressflag = isopenywaddressflag;
	}

	public String getYwexportCwb_URL() {
		return ywexportCwb_URL;
	}

	public void setYwexportCwb_URL(String ywexportCwb_URL) {
		this.ywexportCwb_URL = ywexportCwb_URL;
	}

	public String getYwdeliveryResult_URL() {
		return ywdeliveryResult_URL;
	}

	public void setYwdeliveryResult_URL(String ywdeliveryResult_URL) {
		this.ywdeliveryResult_URL = ywdeliveryResult_URL;
	}

	public String getYwtrackLog_URL() {
		return ywtrackLog_URL;
	}

	public void setYwtrackLog_URL(String ywtrackLog_URL) {
		this.ywtrackLog_URL = ywtrackLog_URL;
	}

	private String ywdeliveryResult_URL; // 配送结果反馈接口URL
	private String ywtrackLog_URL; // 跟踪日志反馈接口URL
	
	public String getYwcustomerid() {
		return ywcustomerid;
	}

	public void setYwcustomerid(String ywcustomerid) {
		this.ywcustomerid = ywcustomerid;
	}

	private int loopcount = 20; // 每次下载循环次数

	public int getLoopcount() {
		return loopcount;
	}

	public void setLoopcount(int loopcount) {
		this.loopcount = loopcount;
	}

	public int getIsopenDataDownload() {
		return isopenDataDownload;
	}

	public void setIsopenDataDownload(int isopenDataDownload) {
		this.isopenDataDownload = isopenDataDownload;
	}

	public long getCallBackCount() {
		return callBackCount;
	}

	public void setCallBackCount(long callBackCount) {
		this.callBackCount = callBackCount;
	}

	public String getUpdatePayResult_URL() {
		return updatePayResult_URL;
	}

	public void setUpdatePayResult_URL(String updatePayResult_URL) {
		this.updatePayResult_URL = updatePayResult_URL;
	}

	private String exportSuccess_URL; // 订单数据导出成功后回调URL，告知一号店已经获取成功。

	public String getExportSuccess_URL() {
		return exportSuccess_URL;
	}

	public void setExportSuccess_URL(String exportSuccess_URL) {
		this.exportSuccess_URL = exportSuccess_URL;
	}

	public long getWarehouseid() {
		return warehouseid;
	}

	public void setWarehouseid(long warehouseid) {
		this.warehouseid = warehouseid;
	}

	public String getCustomerids() {
		return customerids;
	}

	public void setCustomerids(String customerids) {
		this.customerids = customerids;
	}

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

	public String getExportCwb_URL() {
		return exportCwb_URL;
	}

	public int getExportCwb_pageSize() {
		return exportCwb_pageSize;
	}

	public void setExportCwb_pageSize(int exportCwb_pageSize) {
		this.exportCwb_pageSize = exportCwb_pageSize;
	}

	public void setExportCwb_URL(String exportCwb_URL) {
		this.exportCwb_URL = exportCwb_URL;
	}

	public String getDeliveryResult_URL() {
		return deliveryResult_URL;
	}

	public void setDeliveryResult_URL(String deliveryResult_URL) {
		this.deliveryResult_URL = deliveryResult_URL;
	}

	public String getTrackLog_URL() {
		return trackLog_URL;
	}

	public void setTrackLog_URL(String trackLog_URL) {
		this.trackLog_URL = trackLog_URL;
	}

}
