package cn.explink.domain;

public class Function {
	long functionid;
	String functionname;
	long menuid;
	int type;

	public long getFunctionid() {
		return functionid;
	}

	public void setFunctionid(long functionid) {
		this.functionid = functionid;
	}

	public String getFunctionname() {
		return functionname;
	}

	public void setFunctionname(String functionname) {
		this.functionname = functionname;
	}

	public long getMenuid() {
		return menuid;
	}

	public void setMenuid(long menuid) {
		this.menuid = menuid;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
