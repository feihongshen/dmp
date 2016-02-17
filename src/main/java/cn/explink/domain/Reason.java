package cn.explink.domain;

public class Reason implements java.io.Serializable {

	private static final long serialVersionUID = -7044007969710540156L;
	
	private long reasonid;
	private String reasoncontent;
	private long reasontype;
	private int whichreason;
	private int parentid;
	private int changealowflag; //中转是否要申请 1是  0否
	private int interceptType; //拦截原因的类型

	public Reason() {
		// TODO Auto-generated constructor stub
	}
	
	public Reason(long reasonid, String reasoncontent) {
		super();
		this.reasonid = reasonid;
		this.reasoncontent = reasoncontent;
	}

	public int getChangealowflag() {
		return this.changealowflag;
	}

	public void setChangealowflag(int changealowflag) {
		this.changealowflag = changealowflag;
	}

	public int getParentid() {
		return this.parentid;
	}

	public void setParentid(int parentid) {
		this.parentid = parentid;
	}

	public int getWhichreason() {
		return this.whichreason;
	}

	public void setWhichreason(int whichreason) {
		this.whichreason = whichreason;
	}

	public long getReasonid() {
		return this.reasonid;
	}

	public void setReasonid(long reasonid) {
		this.reasonid = reasonid;
	}

	public String getReasoncontent() {
		return this.reasoncontent;
	}

	public void setReasoncontent(String reasoncontent) {
		this.reasoncontent = reasoncontent;
	}

	public long getReasontype() {
		return this.reasontype;
	}

	public void setReasontype(long reasontype) {
		this.reasontype = reasontype;
	}

	public int getInterceptType() {
		return this.interceptType;
	}

	public void setInterceptType(int interceptType) {
		this.interceptType = interceptType;
	}

}
