package cn.explink.domain.VO.express;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.explink.domain.User;
import cn.explink.util.Tools;

/**
 * 创建账单的时候封装的参数
 * 
 * @author jiangyu 2015年8月11日
 *
 */
public class ExpressBillParams4Create {
	
	private static Logger logger = LoggerFactory.getLogger(ExpressBillParams4Create.class);
	/**
	 * 账单编号
	 */
	private String billNo4Create="";
	/**
	 * 客户名称
	 */
	private Long customerId4Create=0L;
	/**
	 * 站点
	 */
	private Long branchId4Create=0L;
	/**
	 * 付款方式
	 */
	private Integer payMethod4Create=0;
	/**
	 * 截止时间
	 */
	private String deadLineTime4Create="";
	/**
	 * 备注
	 */
	private String remark4Create="";
	/**
	 * 操作的标识
	 */
	private Integer opeFlag;

	/**
	 * 当前页
	 */
	private Long page=1L;

	private BigDecimal transFee=BigDecimal.ZERO;

	private BigDecimal receiveableFee = BigDecimal.ZERO;

	private Integer orderCount=0;

	private Integer billType=0;

	private String customerName="";

	private String branchName="";

	private Long receProvince4Create=0L;

	private String receivableProvinceName="";

	private Long payProvince4Create=0L;

	private String payableProvinceName="";

	private User user;
	/**
	 * 是否是预览
	 */
	private Boolean isPreScan;
	/**
	 * billId
	 */
	private Long billId;
	
	public ExpressBillParams4Create() {
		// TODO Auto-generated constructor stub
	}

	public String getBillNo4Create() {
		return billNo4Create;
	}

	public void setBillNo4Create(String billNo4Create) {
		this.billNo4Create = billNo4Create;
	}

	public Long getCustomerId4Create() {
		return customerId4Create;
	}

	public void setCustomerId4Create(Long customerId4Create) {
		this.customerId4Create = customerId4Create;
	}

	public Long getBranchId4Create() {
		return branchId4Create;
	}

	public void setBranchId4Create(Long branchId4Create) {
		this.branchId4Create = branchId4Create;
	}

	public Integer getPayMethod4Create() {
		return payMethod4Create;
	}

	public void setPayMethod4Create(Integer payMethod4Create) {
		this.payMethod4Create = payMethod4Create;
	}

	public String getDeadLineTime4Create() {
		return deadLineTime4Create;
	}

	public void setDeadLineTime4Create(String deadLineTime4Create) {
		this.deadLineTime4Create = deadLineTime4Create;
	}

	public String getRemark4Create() {
		return remark4Create;
	}

	public void setRemark4Create(String remark4Create) {
		this.remark4Create = remark4Create;
	}

	public Integer getOpeFlag() {
		return opeFlag;
	}

	public void setOpeFlag(Integer opeFlag) {
		this.opeFlag = opeFlag;
	}

	public Long getPage() {
		return page;
	}

	public void setPage(Long page) {
		this.page = page;
	}

	public BigDecimal getTransFee() {
		return transFee;
	}

	public void setTransFee(BigDecimal transFee) {
		this.transFee = transFee;
	}

	public BigDecimal getReceiveableFee() {
		return receiveableFee;
	}

	public void setReceiveableFee(BigDecimal receiveableFee) {
		this.receiveableFee = receiveableFee;
	}

	public Integer getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(Integer orderCount) {
		this.orderCount = orderCount;
	}

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	

	public String getReceivableProvinceName() {
		return receivableProvinceName;
	}

	public void setReceivableProvinceName(String receivableProvinceName) {
		this.receivableProvinceName = receivableProvinceName;
	}

	

	public String getPayableProvinceName() {
		return payableProvinceName;
	}

	public void setPayableProvinceName(String payableProvinceName) {
		this.payableProvinceName = payableProvinceName;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getDateSeconds() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timeStr = this.getDeadLineTime4Create();
		if (!Tools.isEmpty(timeStr)) {
			try {
				return sdf.parse(timeStr+" 00:00:00").getTime();
			} catch (ParseException e) {
				logger.error("", e);
			}
		}
		return 0L;
	}

	public Long getReceProvince4Create() {
		return receProvince4Create;
	}

	public void setReceProvince4Create(Long receProvince4Create) {
		this.receProvince4Create = receProvince4Create;
	}

	public Long getPayProvince4Create() {
		return payProvince4Create;
	}

	public void setPayProvince4Create(Long payProvince4Create) {
		this.payProvince4Create = payProvince4Create;
	}

	public Boolean getIsPreScan() {
		return isPreScan;
	}

	public void setIsPreScan(Boolean isPreScan) {
		this.isPreScan = isPreScan;
	}

	public Long getBillId() {
		return billId;
	}

	public void setBillId(Long billId) {
		this.billId = billId;
	}
}
