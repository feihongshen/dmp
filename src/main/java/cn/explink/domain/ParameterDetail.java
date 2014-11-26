package cn.explink.domain;

import java.sql.Timestamp;

public class ParameterDetail {
	private long id;
	private long flowordertype;
	private String filed;
	private String name;

	public ParameterDetail() {
	}

	public ParameterDetail(long id, long flowordertype, String filed, String name) {
		this.id = id;
		this.flowordertype = flowordertype;
		this.filed = filed;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getFlowordertype() {
		return flowordertype;
	}

	public void setFlowordertype(long flowordertype) {
		this.flowordertype = flowordertype;
	}

	public String getFiled() {
		return filed;
	}

	public void setFiled(String filed) {
		if (filed.equals("branchid")) {
			this.name = "下一站";
		} else if (filed.equals("customerid")) {
			this.name = "供货商";
		} else if (filed.equals("driverid")) {
			this.name = "司机";
		} else if (filed.equals("truckid")) {
			this.name = "车辆";
		} else if (filed.equals("baleid")) {
			this.name = "包号";
		} else if (filed.equals("comment")) {
			this.name = "备注";
		}
		this.filed = filed;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
