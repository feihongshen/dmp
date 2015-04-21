package cn.explink.b2c.vipshop;

/**
 * 唯品会对接设置配置属性
 * 
 * @author Administrator
 *
 */
public class VipShop {

	private String shipper_no; // 承运商编码
	private String private_key; // 加密秘钥
	private int getMaxCount; // 每次获取订单最大数量
	private int sendMaxCount; // 每次推送最大数量
	private String getCwb_URL; // 获取订单的URl
	private String sendCwb_URL; // 反馈URL
	private String customerids; // 在系统中的id
	private long vipshop_seq; // 每次获取的seq，之后不断累加
	private long warehouseid; // 订单导入库房ID
	private int isopendownload; // 是否开启订单下载接口
	private int forward_hours; // 提前N个小时发送，可动态配置的。
	private int isTuoYunDanFlag; // 是否开启托运单 模式，生成多个批次
	private int cancelOrIntercept; // 0 开启取消， 1拦截
	private int isOpenLefengflag; //下载配送单 是否只开启乐蜂下载 0关闭， 1开启    

	public int getIsOpenLefengflag() {
		return isOpenLefengflag;
	}

	public void setIsOpenLefengflag(int isOpenLefengflag) {
		this.isOpenLefengflag = isOpenLefengflag;
	}

	public int getCancelOrIntercept() {
		return cancelOrIntercept;
	}

	public void setCancelOrIntercept(int cancelOrIntercept) {
		this.cancelOrIntercept = cancelOrIntercept;
	}

	private int isShangmentuiFlag; // 是否开启上门退业务 0 关闭，1开启

	public int getIsShangmentuiFlag() {
		return isShangmentuiFlag;
	}

	public void setIsShangmentuiFlag(int isShangmentuiFlag) {
		this.isShangmentuiFlag = isShangmentuiFlag;
	}

	public int getIsTuoYunDanFlag() {
		return isTuoYunDanFlag;
	}

	public void setIsTuoYunDanFlag(int isTuoYunDanFlag) {
		this.isTuoYunDanFlag = isTuoYunDanFlag;
	}

	public int getForward_hours() {
		return forward_hours;
	}

	public void setForward_hours(int forward_hours) {
		this.forward_hours = forward_hours;
	}

	public int getIsopendownload() {
		return isopendownload;
	}

	public void setIsopendownload(int isopendownload) {
		this.isopendownload = isopendownload;
	}

	public long getWarehouseid() {
		return warehouseid;
	}

	public void setWarehouseid(long warehouseid) {
		this.warehouseid = warehouseid;
	}

	public long getVipshop_seq() {
		return vipshop_seq;
	}

	public void setVipshop_seq(long vipshop_seq) {
		this.vipshop_seq = vipshop_seq;
	}

	public String getShipper_no() {
		return shipper_no;
	}

	public void setShipper_no(String shipper_no) {
		this.shipper_no = shipper_no;
	}

	public String getPrivate_key() {
		return private_key;
	}

	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}

	public int getGetMaxCount() {
		return getMaxCount;
	}

	public void setGetMaxCount(int getMaxCount) {
		this.getMaxCount = getMaxCount;
	}

	public int getSendMaxCount() {
		return sendMaxCount;
	}

	public void setSendMaxCount(int sendMaxCount) {
		this.sendMaxCount = sendMaxCount;
	}

	public String getGetCwb_URL() {
		return getCwb_URL;
	}

	public void setGetCwb_URL(String getCwb_URL) {
		this.getCwb_URL = getCwb_URL;
	}

	public String getSendCwb_URL() {
		return sendCwb_URL;
	}

	public void setSendCwb_URL(String sendCwb_URL) {
		this.sendCwb_URL = sendCwb_URL;
	}

	public String getCustomerids() {
		return customerids;
	}

	public void setCustomerids(String customerids) {
		this.customerids = customerids;
	}

}
