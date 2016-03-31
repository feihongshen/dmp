package cn.explink.b2c.wenxuan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Wenxuan {

	private static Logger logger = LoggerFactory.getLogger(Wenxuan.class);
	
	private String apikey;
	private String apiSecret; // 密钥
	private int maxCount;
	private String downloadUrl;
	private int pagesize;
	private String wabillUrl;

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	private String pwd;

	public String getWabillUrl() {
		return wabillUrl;
	}

	public void setWabillUrl(String wabillUrl) {
		this.wabillUrl = wabillUrl;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	private long warehouseid; // 库房
	private String customerid; // 供货商 id
	private int beforhours; // 提前的小时数
	private int loopcount; // 循环次数

	public String getApikey() {
		return apikey;
	}

	public void setApikey(String apikey) {
		this.apikey = apikey;
	}

	public String getApiSecret() {
		return apiSecret;
	}

	public void setApiSecret(String apiSecret) {
		this.apiSecret = apiSecret;
	}

	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	public long getWarehouseid() {
		return warehouseid;
	}

	public void setWarehouseid(long warehouseid) {
		this.warehouseid = warehouseid;
	}

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public int getBeforhours() {
		return beforhours;
	}

	public void setBeforhours(int beforhours) {
		this.beforhours = beforhours;
	}

	public int getLoopcount() {
		return loopcount;
	}

	public void setLoopcount(int loopcount) {
		this.loopcount = loopcount;
	}

	public static void main(String[] args) {
		String url = "http://api.winxuan.com/v1/logistics";
		logger.info(url.substring(url.indexOf(".com") + 4));
	}

}
