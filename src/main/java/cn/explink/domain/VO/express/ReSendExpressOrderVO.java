package cn.explink.domain.VO.express;

public class ReSendExpressOrderVO {
	/*
	 * tps接口表id
	 */
	private Long id;
	/*
	 * tps接口表参数
	 */
	private String methodParams;
	/*
	 * 运单号
	 */
	private String transNo;
	/*
	 * 异常描述
	 */
	private String errMsg;
	/*
	 * 结果
	 */
	private int opeFlag;
	/*
	 * 创建时间
	 */
	private String createTime;
	
	//操作类型
	private int operationType;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMethodParams() {
		return this.methodParams;
	}

	public void setMethodParams(String methodParams) {
		this.methodParams = methodParams;
	}

	public String getTransNo() {
		return this.transNo;
	}

	public void setTransNo(String transNo) {
		this.transNo = transNo;
	}

	public String getErrMsg() {
		return this.errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public int getOpeFlag() {
		return this.opeFlag;
	}

	public void setOpeFlag(int opeFlag) {
		this.opeFlag = opeFlag;
	}

	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getOperationType() {
		return operationType;
	}

	public void setOperationType(int operationType) {
		this.operationType = operationType;
	}

}
