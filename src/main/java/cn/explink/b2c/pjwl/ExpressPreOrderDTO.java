package cn.explink.b2c.pjwl;

import java.util.Date;

/**
 * 预订单的dto 用于接收tps预订单转换后的容器
 * 
 * @author jiangyu 2015年8月26日
 *
 */
public class ExpressPreOrderDTO {
	/**
	 * 主键id
	 */
	private Long id;
	/**
	 * 预订单查询
	 */
	private String reserveOrderNo;
	/**
	 * 客户编码
	 */
	private String custCode;

	/**
	 * 发货省
	 */

	private String cnorProv;

	/**
	 * 发货市
	 */

	private String cnorCity;

	/**
	 * 发货区
	 */

	private String cnorRegion;

	/**
	 * 发货街道
	 */

	private String cnorTown;

	/**
	 * 发货地址
	 */

	private String cnorAddr;

	/**
	 * 发货联系人
	 */

	private String cnorName;

	/**
	 * 发货联系手机
	 */

	private String cnorMobile;

	/**
	 * 发货电话
	 */

	private String cnorTel;

	/**
	 * 发货备注
	 */

	private String cnorRemark;

	/**
	 * 承运商编码
	 */

	private String carrierCode;

	/**
	 * 预定单状态
	 */

	private Integer reserveOrderStatus;
	/**
	 * 订单的类型
	 */
	private Integer orderType=0;
	/**
	 * 是否失效的状态
	 */
	private Integer state=1;
	/**
	 * 是否存在
	 */
	private Integer isExist=0;
	/**
	 * 创建时间
	 */
	private Date createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReserveOrderNo() {
		return reserveOrderNo;
	}

	public void setReserveOrderNo(String reserveOrderNo) {
		this.reserveOrderNo = reserveOrderNo;
	}

	public String getCustCode() {
		return custCode;
	}

	public void setCustCode(String custCode) {
		this.custCode = custCode;
	}

	public String getCnorProv() {
		return cnorProv;
	}

	public void setCnorProv(String cnorProv) {
		this.cnorProv = cnorProv;
	}

	public String getCnorCity() {
		return cnorCity;
	}

	public void setCnorCity(String cnorCity) {
		this.cnorCity = cnorCity;
	}

	public String getCnorRegion() {
		return cnorRegion;
	}

	public void setCnorRegion(String cnorRegion) {
		this.cnorRegion = cnorRegion;
	}

	public String getCnorTown() {
		return cnorTown;
	}

	public void setCnorTown(String cnorTown) {
		this.cnorTown = cnorTown;
	}

	public String getCnorAddr() {
		return cnorAddr;
	}

	public void setCnorAddr(String cnorAddr) {
		this.cnorAddr = cnorAddr;
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

	public String getCnorRemark() {
		return cnorRemark;
	}

	public void setCnorRemark(String cnorRemark) {
		this.cnorRemark = cnorRemark;
	}

	public String getCarrierCode() {
		return carrierCode;
	}

	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}

	public Integer getReserveOrderStatus() {
		return reserveOrderStatus;
	}

	public void setReserveOrderStatus(Integer reserveOrderStatus) {
		this.reserveOrderStatus = reserveOrderStatus;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getIsExist() {
		return isExist;
	}

	public void setIsExist(Integer isExist) {
		this.isExist = isExist;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
