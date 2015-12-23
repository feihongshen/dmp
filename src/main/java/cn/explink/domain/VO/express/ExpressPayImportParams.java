package cn.explink.domain.VO.express;

import java.util.Date;

import cn.explink.domain.User;


/**
 * 导入的应收订单的参数
 * @author jiangyu 2015年8月20日
 *
 */
public class ExpressPayImportParams {
	/**
	 * 账单id
	 */
	private Long billId;
	/**
	 * 账单号
	 */
	private String billNo;
	/**
	 * 用户
	 */
	private User user;
	/**
	 * 导入时间
	 */
	private Date date;
	
	public ExpressPayImportParams() {
	}

	public Long getBillId() {
		return billId;
	}

	public void setBillId(Long billId) {
		this.billId = billId;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
