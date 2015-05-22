package cn.explink.b2c.gztl.feedbackxmldto;

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
	 * <MSD> <Feedbacks> <Feedback> <id>17353</id>
	 * <logisticProviderId>ZSRB</logisticProviderId>
	 * <waybillNo>1047541898</waybillNo> <status>派件</status>
	 * <stateTime>2014/9/12 9:22:00</stateTime>
	 * <operatorName>761602</operatorName> <operatorTime>2014-09-12
	 * 09:03:39</operatorTime> <deliveryman/> <reason/> <scanId>中山火炬站</scanId>
	 * <preSiteName/> <nextSiteName/> <remark/> <payMethod/>
	 * <receiveTime>2014-09-15 17:29:27</receiveTime> </Feedback>
	 * <Feedback>******</Feedback> <Feedback>******</Feedback> </Feedbacks>
	 * </MSD>
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
