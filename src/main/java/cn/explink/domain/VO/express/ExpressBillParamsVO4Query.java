package cn.explink.domain.VO.express;

import cn.explink.domain.User;
import cn.explink.util.Tools;

/**
 * 账单的实体参数
 * 
 * @author jiangyu 2015年8月11日
 *
 */
public class ExpressBillParamsVO4Query {
	/**
	 * 账单编号
	 */
	private String billNo;
	/**
	 * 账单状态
	 */
	private Integer billState;
	/**
	 * 账单创建时间开始时间
	 */
	private String createBeginTime;
	/**
	 * 账单创建时间结束时间
	 */
	private String createEndTime;
	/**
	 * 核销时间开始时间
	 */
	private String verifyBeginTime;
	/**
	 * 核销时间结束时间
	 */
	private String verifyEndTime;
	/**
	 * 客户
	 */
	private Long customerId;
	/**
	 * 站点
	 */
	private Long branchId;
	/**
	 * 应付省份
	 */
	private Long payableProvinceId;
	/**
	 * 应收省份
	 */
	private Long receProvinceId;
	/**
	 * 排序字段
	 */
	private Integer orderField;
	/**
	 * 排序规则
	 */
	private String orderRule;
	/**
	 * 显示的页
	 */
	private Long page;

	/**
	 * 操作标识[枚举]
	 */
	private Integer operFlag;
	
	private User user;
	
	private Integer billType;

	public ExpressBillParamsVO4Query() {
		// TODO Auto-generated constructor stub
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public Integer getBillState() {
		return billState;
	}

	public void setBillState(Integer billState) {
		this.billState = billState;
	}

	public String getCreateBeginTime() {
		if (!Tools.isEmpty(createBeginTime)) {
			return createBeginTime + " 00:00:00";
		} else {
			return null;
		}
	}

	public void setCreateBeginTime(String createBeginTime) {
		this.createBeginTime = createBeginTime;
	}

	public String getCreateEndTime() {
		if (!Tools.isEmpty(createEndTime)) {
			return createEndTime + " 23:59:59";
		} else {
			return null;
		}
	}

	public void setCreateEndTime(String createEndTime) {
		this.createEndTime = createEndTime;
	}

	public String getVerifyBeginTime() {
		if (!Tools.isEmpty(verifyBeginTime)) {
			return verifyBeginTime + " 00:00:00";
		} else {
			return null;
		}
	}

	public void setVerifyBeginTime(String verifyBeginTime) {
		this.verifyBeginTime = verifyBeginTime;
	}

	public String getVerifyEndTime() {
		if (!Tools.isEmpty(verifyEndTime)) {
			return verifyEndTime + " 23:59:59";
		} else {
			return null;
		}
	}

	public void setVerifyEndTime(String verifyEndTime) {
		this.verifyEndTime = verifyEndTime;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getBranchId() {
		return branchId;
	}

	public void setBranchId(Long branchId) {
		this.branchId = branchId;
	}

	public Long getPayableProvinceId() {
		return payableProvinceId;
	}

	public void setPayableProvinceId(Long payableProvinceId) {
		this.payableProvinceId = payableProvinceId;
	}

	public Integer getOrderField() {
		return orderField;
	}

	public void setOrderField(Integer orderField) {
		this.orderField = orderField;
	}

	public String getOrderRule() {
		return orderRule;
	}

	public void setOrderRule(String orderRule) {
		this.orderRule = orderRule;
	}

	public Integer getOperFlag() {
		return operFlag;
	}

	public void setOperFlag(Integer operFlag) {
		this.operFlag = operFlag;
	}

	public Long getPage() {
		return page;
	}

	public void setPage(Long page) {
		this.page = page;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

	public Long getReceProvinceId() {
		return receProvinceId;
	}

	public void setReceProvinceId(Long receProvinceId) {
		this.receProvinceId = receProvinceId;
	}
}
