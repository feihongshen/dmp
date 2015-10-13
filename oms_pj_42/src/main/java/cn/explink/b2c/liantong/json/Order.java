package cn.explink.b2c.liantong.json;

import java.util.List;

/**
 * 单个订单信息
 * 
 * @author Administrator
 *
 */
public class Order {

	private String mailNo; // 物流单号
	private List<Step> steps;

	public String getMailNo() {
		return mailNo;
	}

	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}

	public List<Step> getSteps() {
		return steps;
	}

	public void setSteps(List<Step> steps) {
		this.steps = steps;
	}

}
