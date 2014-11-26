package cn.explink.b2c.homegobj.xmldto;

import javax.xml.bind.annotation.XmlElement;

public class RequestBody {

	private String start_time;
	private String end_time;
	private Page requestPage;

	@XmlElement(name = "end_time")
	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	@XmlElement(name = "start_time")
	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	@XmlElement(name = "page")
	public Page getRequestPage() {
		return requestPage;
	}

	public void setRequestPage(Page requestPage) {
		this.requestPage = requestPage;
	}

}
