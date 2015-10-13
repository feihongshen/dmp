package cn.explink.b2c.yihaodian.xmldto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "OrderExportCondition")
public class OrderExportConditionDto implements Serializable {

	private static final long serialVersionUID = 3709314849656726241L;
	// 用户代码
	private String userCode;
	// 请求时间
	private String requestTime;
	// 签名
	private String sign;
	// 每次返回订单数
	private Integer pageSize;

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

}
