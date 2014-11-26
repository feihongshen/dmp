package cn.explink.domain;

public class Exportmould {

	private long id;
	private String rolename;
	private long roleid;
	private String mouldname;
	private String mouldfieldids;
	private long status;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public long getRoleid() {
		return roleid;
	}

	public void setRoleid(long roleid) {
		this.roleid = roleid;
	}

	public String getMouldname() {
		return mouldname;
	}

	public void setMouldname(String mouldname) {
		this.mouldname = mouldname;
	}

	public String getMouldfieldids() {
		return mouldfieldids;
	}

	public void setMouldfieldids(String mouldfieldids) {
		this.mouldfieldids = mouldfieldids;
	}

	public long getStatus() {
		return status;
	}

	public void setStatus(long status) {
		this.status = status;
	}

}
