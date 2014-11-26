package cn.explink.b2c.homegobj.xmldto;

import javax.xml.bind.annotation.XmlElement;

public class Page {

	private int page_no;
	private long page_size;

	private String page_count;//
	private String end_flag; //

	@XmlElement(name = "page_count")
	public String getPage_count() {
		return page_count;
	}

	public void setPage_count(String page_count) {
		this.page_count = page_count;
	}

	@XmlElement(name = "end_flag")
	public String getEnd_flag() {
		return end_flag;
	}

	public void setEnd_flag(String end_flag) {
		this.end_flag = end_flag;
	}

	@XmlElement(name = "page_no")
	public int getPage_no() {
		return page_no;
	}

	public void setPage_no(int page_no) {
		this.page_no = page_no;
	}

	@XmlElement(name = "page_size")
	public long getPage_size() {
		return page_size;
	}

	public void setPage_size(long page_size) {
		this.page_size = page_size;
	}

}
