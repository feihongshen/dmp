package cn.explink.b2c.gztlfeedback.feedbackxmldto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MSD")
@XmlAccessorType(XmlAccessType.FIELD)
public class InsertFeedbackOrderData {
	/**
	 * 将广州通路反馈回来的订单信息(xml格式的)转化为对应的对象的信息
	 */
	@XmlElementWrapper(name = "Feedbacks")
	@XmlElement(name = "Feedback")
	private List<OrderFeedback> feedbacks;

	public List<OrderFeedback> getFeedbacks() {
		return this.feedbacks;
	}

	public void setFeedbacks(List<OrderFeedback> feedbacks) {
		this.feedbacks = feedbacks;
	}

}
