package cn.explink.b2c.gztl.sendFeedbackData;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "TMS")
@XmlAccessorType(XmlAccessType.FIELD)
public class SendFeedbackDatas {
	@XmlElementWrapper(name = "TMSSendOrders")
	@XmlElement(name = "TMSSendOrder")
	private List<SendFeedbackData> tmsSendOrders;

	public List<SendFeedbackData> getTmsSendOrders() {
		return this.tmsSendOrders;
	}

	public void setTmsSendOrders(List<SendFeedbackData> tmsSendOrders) {
		this.tmsSendOrders = tmsSendOrders;
	}

}
