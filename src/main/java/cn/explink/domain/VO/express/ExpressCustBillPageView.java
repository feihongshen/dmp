package cn.explink.domain.VO.express;
/**
 * 组织前台表格的显示字段
 * @author jiangyu 2015年8月11日
 *
 */
public class ExpressCustBillPageView extends ExpressBillBasePageView{
	/**
	 * 客户id
	 */
	private Long customerId;
	/**
	 * 客户名称
	 */
	private String customerName;
	
	public ExpressCustBillPageView() {
	}

	public ExpressCustBillPageView(Long customerId, String customerName) {
		super();
		this.customerId = customerId;
		this.customerName = customerName;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	
}
