package cn.explink.b2c.gztl.returnData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "TMSFeedback")
@XmlAccessorType(XmlAccessType.FIELD)
public class TmsFeedback {
	/**
	 * <TMSFeedback> <id>275994899,275994898</id> <success>true</success>
	 * <remark/> </TMSFeedback>
	 */
	/**
	 * 给广州通路返回订单状态时，广州通路那边返回的xml格式的字段
	 */
	private String id;// 请求报文中的id，多条记录用“，”分隔
	private String success;// 成功为：true；失败为：false
	private String remark;// 失败原因

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSuccess() {
		return this.success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
