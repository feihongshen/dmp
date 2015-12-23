package cn.explink.domain.express;

import java.util.Date;

/**
 * 快递业务中调用Tps接口异常记录
 * 
 * @author jiangyu 2015年9月8日
 *
 */
public class ExpressTpsInterfaceExcepRecord {

	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 预订单号
	 */
	private String preOrderNo;
	/**
	 * 运单号
	 */
	private String transNo;
	/**
	 * 包号
	 */
	private String packageNo;

	/**
	 * 异常原因
	 */
	private String errMsg;
	/**
	 * 所处操作环节
	 */
	private Integer operationType;
	/**
	 * 方法的参数
	 */
	private String methodParams;
	/**
	 * 执行次数
	 */
	private Integer executeCount;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	/**
	 * 再次调用成功
	 */
	private Integer opeFlag;

	/**
	 * 订单的操作状态【对应订单操作环节的枚举】
	 */
	private Long flowOrderType = 0L;
	/**
	 * 备注
	 */
	private String remark;

	public ExpressTpsInterfaceExcepRecord() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPreOrderNo() {
		return preOrderNo;
	}

	public void setPreOrderNo(String preOrderNo) {
		this.preOrderNo = preOrderNo;
	}

	public String getTransNo() {
		return transNo;
	}

	public void setTransNo(String transNo) {
		this.transNo = transNo;
	}

	public String getPackageNo() {
		return packageNo;
	}

	public void setPackageNo(String packageNo) {
		this.packageNo = packageNo;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public Integer getOperationType() {
		return operationType;
	}

	public void setOperationType(Integer operationType) {
		this.operationType = operationType;
	}

	public String getMethodParams() {
		return methodParams;
	}

	public void setMethodParams(String methodParams) {
		this.methodParams = methodParams;
	}

	public Integer getExecuteCount() {
		return executeCount;
	}

	public void setExecuteCount(Integer executeCount) {
		this.executeCount = executeCount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getOpeFlag() {
		return opeFlag;
	}

	public void setOpeFlag(Integer opeFlag) {
		this.opeFlag = opeFlag;
	}

	public Long getFlowOrderType() {
		return flowOrderType;
	}

	public void setFlowOrderType(Long flowOrderType) {
		this.flowOrderType = flowOrderType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
