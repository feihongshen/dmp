package cn.explink.b2c.gome;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 订单
 * 
 * @author Administrator
 *
 */
public class DeliveryInfo {

	private String businessCode;// 业务代码
	private String lspCode;// 物流公司编码
	private String lspAbbr;// 物流公司代码
	private String buid;// 渠道代码
	private String orderNumber;// 订单号
	private String waybillCode;// 工作单号
	private String statusCode;// 订单状态
	private String statusTime;// 状态产生时间
	private String reasonCode;// 原因代码
	private String reasonDesc;// 原因描述
	private String driverId;// 配送员id
	private String driverName;// 配送员姓名
	private String truckId;// 卡车号
	private String driverPhoneNumber;// 配送员电话
	private String lspName;// 物流公司名称
	private String lspPhoneNumber;// 物流公司电话
	private String sortingCenter;// 分拨点名称
	private String comments;// 备注

	/*
	 * <?xml version="1.0" encoding="UTF-8"?><tntVo> <buid>8270</buid>
	 * <lspCode>21000047</lspCode> <orderNumber>100111****</orderNumber>
	 * <businessCode>TNT</businessCode> <waybillCode>100111****</waybillCode>
	 * <statusCode>DO</statusCode> <statusTime>2011-11-12T01:29:05</statusTime>
	 * <driverId>1199</driverId> <driverName>张宏来</driverName>
	 * <driverPhoneNumber>18955115113\n13053081262</driverPhoneNumber>
	 * <sortingCenter>蜀山站</sortingCenter> </tntVo>
	 */
	public String getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}

	public String getLspCode() {
		return lspCode;
	}

	public void setLspCode(String lspCode) {
		this.lspCode = lspCode;
	}

	public String getLspAbbr() {
		return lspAbbr;
	}

	public void setLspAbbr(String lspAbbr) {
		this.lspAbbr = lspAbbr;
	}

	public String getBuid() {
		return buid;
	}

	public void setBuid(String buid) {
		this.buid = buid;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getWaybillCode() {
		return waybillCode;
	}

	public void setWaybillCode(String waybillCode) {
		this.waybillCode = waybillCode;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusTime() {
		try {
			if (statusTime.indexOf("T") > -1) {
				return statusTime;
			}
			long beforeSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(statusTime).getTime() - (8 * 60 * 60 * 1000);
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(beforeSS)).replace(" ", "T");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return statusTime.replace(" ", "T");
		}
	}

	public void setStatusTime(String statusTime) {
		this.statusTime = statusTime;
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	public String getReasonDesc() {
		return reasonDesc;
	}

	public void setReasonDesc(String reasonDesc) {
		this.reasonDesc = reasonDesc;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getTruckId() {
		return truckId;
	}

	public void setTruckId(String truckId) {
		this.truckId = truckId;
	}

	public String getDriverPhoneNumber() {
		return driverPhoneNumber;
	}

	public void setDriverPhoneNumber(String driverPhoneNumber) {
		this.driverPhoneNumber = driverPhoneNumber;
	}

	public String getLspName() {
		return lspName;
	}

	public void setLspName(String lspName) {
		this.lspName = lspName;
	}

	public String getLspPhoneNumber() {
		return lspPhoneNumber;
	}

	public void setLspPhoneNumber(String lspPhoneNumber) {
		this.lspPhoneNumber = lspPhoneNumber;
	}

	public String getSortingCenter() {
		return sortingCenter;
	}

	public void setSortingCenter(String sortingCenter) {
		this.sortingCenter = sortingCenter;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

}
