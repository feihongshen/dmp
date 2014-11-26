package cn.explink.b2c.gome.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class DetailVO {
	private List<Detail> detail;

	@XmlElement(name = "detail")
	public List<Detail> getDetail() {
		return detail;
	}

	public void setDetail(List<Detail> detail) {
		this.detail = detail;
	}

}
