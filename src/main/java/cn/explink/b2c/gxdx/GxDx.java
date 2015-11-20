/**
 * 
 */
package cn.explink.b2c.gxdx;

/**
 * @ClassName: GxDx
 * @Description: TODO
 * @Author: 王强
 * @Date: 2015年11月9日下午4:13:36
 */
public class GxDx {
	private String customerid;
	private String private_key;
	private String requestUrl;
	private int maxCount;
	private String logisticProviderID;
	private long exportbranchid; // 订单导入库房ID

	public long getExportbranchid() {
		return exportbranchid;
	}

	public void setExportbranchid(long exportbranchid) {
		this.exportbranchid = exportbranchid;
	}

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}


	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	public String getLogisticProviderID() {
		return logisticProviderID;
	}

	public void setLogisticProviderID(String logisticProviderID) {
		this.logisticProviderID = logisticProviderID;
	}

	public String getPrivate_key() {
		return private_key;
	}

	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

}
