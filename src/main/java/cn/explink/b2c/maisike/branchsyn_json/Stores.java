package cn.explink.b2c.maisike.branchsyn_json;

import org.codehaus.jackson.annotate.JsonProperty;

public class Stores {
	private long id; // 自增唯一id
	@JsonProperty(value = "Sid")
	private String sid; // 唯一标识,发送数据的时候用到
	@JsonProperty(value = "SName")
	private String sname; // 营业厅名称
	@JsonProperty(value = "SArea")
	private String sarea; // 营业厅所在地区（格式：省 市 区）
	@JsonProperty(value = "SAddress")
	private String saddress; // 营业厅详细地址
	@JsonProperty(value = "SPhone")
	private String sphone; // 营业厅联系电话

	private String b2cenum; // 枚举号对应erp美剧好

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getB2cenum() {
		return b2cenum;
	}

	public void setB2cenum(String b2cenum) {
		this.b2cenum = b2cenum;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public String getSarea() {
		return sarea;
	}

	public void setSarea(String sarea) {
		this.sarea = sarea;
	}

	public String getSaddress() {
		return saddress;
	}

	public void setSaddress(String saddress) {
		this.saddress = saddress;
	}

	public String getSphone() {
		return sphone;
	}

	public void setSphone(String sphone) {
		this.sphone = sphone;
	}

}
