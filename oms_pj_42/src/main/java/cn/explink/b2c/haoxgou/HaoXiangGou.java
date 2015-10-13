package cn.explink.b2c.haoxgou;

/**
 * 对接 属性 设置
 * 
 * @author Administrator
 *
 */
public class HaoXiangGou {

	private String dlver_cd; // 配送公司标示

	private String customerids; // 在系统中的ids
	private String requst_url; // 请求webservice的路径
	private String des_key; // DES加密密钥
	private String password; // 访问密码
	private long warehouseid; // 订单入库接口
	private long maxCount; // 每次查询大小
	private String starttime; // 指定时间段 测试用
	private String endtime; // 指定时间段
	private int isopentestflag; // 是否开启测试指定时间段 ，0关闭，1开启， 开启之后，时间按照设置时间段来执行。
	private int selectHours; // 查询时间段
	private String pospaycode; // 支付代码 。根据不同的支付方来规定

	public String getPospaycode() {
		return pospaycode;
	}

	public void setPospaycode(String pospaycode) {
		this.pospaycode = pospaycode;
	}

	public int getSelectHours() {
		return selectHours;
	}

	public void setSelectHours(int selectHours) {
		this.selectHours = selectHours;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public int getIsopentestflag() {
		return isopentestflag;
	}

	public void setIsopentestflag(int isopentestflag) {
		this.isopentestflag = isopentestflag;
	}

	public long getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(long maxCount) {
		this.maxCount = maxCount;
	}

	public String getDlver_cd() {
		return dlver_cd;
	}

	public void setDlver_cd(String dlver_cd) {
		this.dlver_cd = dlver_cd;
	}

	public String getCustomerids() {
		return customerids;
	}

	public void setCustomerids(String customerids) {
		this.customerids = customerids;
	}

	public String getRequst_url() {
		return requst_url;
	}

	public void setRequst_url(String requst_url) {
		this.requst_url = requst_url;
	}

	public String getDes_key() {
		return des_key;
	}

	public void setDes_key(String des_key) {
		this.des_key = des_key;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getWarehouseid() {
		return warehouseid;
	}

	public void setWarehouseid(long warehouseid) {
		this.warehouseid = warehouseid;
	}

}
