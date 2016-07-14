package cn.explink.domain.express2.VO;

import java.util.Date;

/**
 * 预约单Vo
 * @date 2016年5月13日 下午6:04:39
 */
public class ReserveOrderVo {
	
	/**
	 * ID
	 */
	private Integer omReserveOrderId;
	
	/**
	 * 预约单号
	 */
	private String reserveOrderNo;
	
	/**
	 * 预约时间
	 */
	private Date appointTime;
	
	/**
	 * 预约时间-字符
	 */
	private String appointTimeStr;
	
	/**
	 * 寄货人
	 */
	private String cnorName;
	
	/**
	 * 手机
	 */
	private String cnorMobile;
	
	/**
	 * 寄件电话
	 */
	private String cnorTel;
	
	/**
	 * 寄件地址
	 */
	private String cnorAddr;
	
	/**
	 * 寄件详细地址，包含省市区
	 */
	private String cnorDetailAddr;
	
	/**
	 * 预约上门时间
	 */
	private Date requireTime;
	
	/**
	 * 预约上门时间-字符
	 */
	private String requireTimeStr;
	
	/**
	 * 预约单状态
	 */
	private Byte reserveOrderStatus;
	
	/**
	 * 预约单状态值
	 */
	private String reserveOrderStatusName;
	
	/**
	 * 原因
	 */
	private String reason;
	
	/**
	 * 运单号
	 */
	private String transportNo;
	
	/**
	 * 揽件机构编号
	 */
	private String acceptOrg;
	
	/**
	* 揽件机构名称
	*/
	private String acceptOrgName;
	
	/**
	* 揽件员
	*/
	private String courier;
	
	/**
	* 揽件员名称
	*/
	private String courierName;
	
	/**
	 * 备注
	 */
	private String cnorRemark;

    /**
     * 版本号
     */
    private long recordVersion;

    private int operateType;
	
	/**
	* 发货省份
	*/
	private String cnorProvName;
	/**
	* 发货城市
	*/
	private String cnorCityName;
	/**
	* 发货地区
	*/
	private String cnorRegionName;

    /**
     * 备注
     */
    private String remark;

    /**
     * DMP 站点编码 branchCode
     */
    private String carrierSiteCode;
    
    /**
     * 寄件公司
     */
    private String custName;
    
    //寄件省份编码
    private String cnorProvCode;
    //寄件城市编码
    private String cnorCityCode;
    //寄件区域编码
    private String cnorRegionCode;
    //寄件街道编码
    private String cnorTownCode;
    //寄件街道
    private String cnorTownName;
    
	public Integer getOmReserveOrderId() {
		return omReserveOrderId;
	}

	public void setOmReserveOrderId(Integer omReserveOrderId) {
		this.omReserveOrderId = omReserveOrderId;
	}

	public String getReserveOrderNo() {
		return reserveOrderNo;
	}

	public void setReserveOrderNo(String reserveOrderNo) {
		this.reserveOrderNo = reserveOrderNo;
	}

	public Date getAppointTime() {
		return appointTime;
	}

	public void setAppointTime(Date appointTime) {
		this.appointTime = appointTime;
	}

	public String getAppointTimeStr() {
		return appointTimeStr;
	}

	public void setAppointTimeStr(String appointTimeStr) {
		this.appointTimeStr = appointTimeStr;
	}

	public String getCnorName() {
		return cnorName;
	}

	public void setCnorName(String cnorName) {
		this.cnorName = cnorName;
	}

	public String getCnorMobile() {
		return cnorMobile;
	}

	public void setCnorMobile(String cnorMobile) {
		this.cnorMobile = cnorMobile;
	}

	public String getCnorTel() {
		return cnorTel;
	}

	public void setCnorTel(String cnorTel) {
		this.cnorTel = cnorTel;
	}

	public String getCnorAddr() {
		return cnorAddr;
	}

	public void setCnorAddr(String cnorAddr) {
		this.cnorAddr = cnorAddr;
	}

	public String getCnorDetailAddr() {
		return cnorDetailAddr;
	}

	public void setCnorDetailAddr(String cnorDetailAddr) {
		this.cnorDetailAddr = cnorDetailAddr;
	}

	public Date getRequireTime() {
		return requireTime;
	}

	public void setRequireTime(Date requireTime) {
		this.requireTime = requireTime;
	}

	public String getRequireTimeStr() {
		return requireTimeStr;
	}

	public void setRequireTimeStr(String requireTimeStr) {
		this.requireTimeStr = requireTimeStr;
	}

	public Byte getReserveOrderStatus() {
		return reserveOrderStatus;
	}

	public void setReserveOrderStatus(Byte reserveOrderStatus) {
		this.reserveOrderStatus = reserveOrderStatus;
	}

	public String getReserveOrderStatusName() {
		return reserveOrderStatusName;
	}

	public void setReserveOrderStatusName(String reserveOrderStatusName) {
		this.reserveOrderStatusName = reserveOrderStatusName;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getTransportNo() {
		return transportNo;
	}

	public void setTransportNo(String transportNo) {
		this.transportNo = transportNo;
	}

	public String getAcceptOrg() {
		return acceptOrg;
	}

	public void setAcceptOrg(String acceptOrg) {
		this.acceptOrg = acceptOrg;
	}

	public String getAcceptOrgName() {
		return acceptOrgName;
	}

	public void setAcceptOrgName(String acceptOrgName) {
		this.acceptOrgName = acceptOrgName;
	}

	public String getCourier() {
		return courier;
	}

	public void setCourier(String courier) {
		this.courier = courier;
	}

	public String getCourierName() {
		return courierName;
	}

	public void setCourierName(String courierName) {
		this.courierName = courierName;
	}

	public String getCnorRemark() {
		return cnorRemark;
	}

	public void setCnorRemark(String cnorRemark) {
		this.cnorRemark = cnorRemark;
	}

	public long getRecordVersion() {
		return recordVersion;
	}

	public void setRecordVersion(long recordVersion) {
		this.recordVersion = recordVersion;
	}

	public int getOperateType() {
		return operateType;
	}

	public void setOperateType(int operateType) {
		this.operateType = operateType;
	}

	public String getCnorProvName() {
		return cnorProvName;
	}

	public void setCnorProvName(String cnorProvName) {
		this.cnorProvName = cnorProvName;
	}

	public String getCnorCityName() {
		return cnorCityName;
	}

	public void setCnorCityName(String cnorCityName) {
		this.cnorCityName = cnorCityName;
	}

	public String getCnorRegionName() {
		return cnorRegionName;
	}

	public void setCnorRegionName(String cnorRegionName) {
		this.cnorRegionName = cnorRegionName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCarrierSiteCode() {
		return carrierSiteCode;
	}

	public void setCarrierSiteCode(String carrierSiteCode) {
		this.carrierSiteCode = carrierSiteCode;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getCnorProvCode() {
		return cnorProvCode;
	}

	public void setCnorProvCode(String cnorProvCode) {
		this.cnorProvCode = cnorProvCode;
	}

	public String getCnorCityCode() {
		return cnorCityCode;
	}

	public void setCnorCityCode(String cnorCityCode) {
		this.cnorCityCode = cnorCityCode;
	}

	public String getCnorRegionCode() {
		return cnorRegionCode;
	}

	public void setCnorRegionCode(String cnorRegionCode) {
		this.cnorRegionCode = cnorRegionCode;
	}

	public String getCnorTownCode() {
		return cnorTownCode;
	}

	public void setCnorTownCode(String cnorTownCode) {
		this.cnorTownCode = cnorTownCode;
	}


	public String getCnorTownName() {
		return cnorTownName;
	}

	public void setCnorTownName(String cnorTownName) {
		this.cnorTownName = cnorTownName;
	}

	@Override
	public String toString() {
		return "ReserveOrderVo [omReserveOrderId=" + omReserveOrderId + ", reserveOrderNo=" + reserveOrderNo
				+ ", appointTime=" + appointTime + ", appointTimeStr=" + appointTimeStr + ", cnorName=" + cnorName
				+ ", cnorMobile=" + cnorMobile + ", cnorTel=" + cnorTel + ", cnorAddr=" + cnorAddr + ", cnorDetailAddr="
				+ cnorDetailAddr + ", requireTime=" + requireTime + ", requireTimeStr=" + requireTimeStr
				+ ", reserveOrderStatus=" + reserveOrderStatus + ", reserveOrderStatusName=" + reserveOrderStatusName
				+ ", reason=" + reason + ", transportNo=" + transportNo + ", acceptOrg=" + acceptOrg
				+ ", acceptOrgName=" + acceptOrgName + ", courier=" + courier + ", courierName=" + courierName
				+ ", cnorRemark=" + cnorRemark + ", recordVersion=" + recordVersion + ", operateType=" + operateType
				+ ", cnorProvName=" + cnorProvName + ", cnorCityName=" + cnorCityName + ", cnorRegionName="
				+ cnorRegionName + ", remark=" + remark + ", carrierSiteCode=" + carrierSiteCode + ", custName="
				+ custName + "]";
	}
}
