package cn.explink.domain;

public class SetExportField {

	private int id;
	private String fieldname;
	private long exportstate;
	private String fieldenglishname;
	private long orderlevel;
	private String exportdatatype;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFieldname() {
		return fieldname;
	}

	public void setFieldname(String fieldname) {
		this.fieldname = fieldname;
	}

	public long getExportstate() {
		return exportstate;
	}

	public void setExportstate(long exportstate) {
		this.exportstate = exportstate;
	}

	public String getFieldenglishname() {
		return fieldenglishname;
	}

	public void setFieldenglishname(String fieldenglishname) {
		this.fieldenglishname = fieldenglishname;
	}

	public long getOrderlevel() {
		return orderlevel;
	}

	public void setOrderlevel(long orderlevel) {
		this.orderlevel = orderlevel;
	}

	public String getExportdatatype() {
		return exportdatatype;
	}

	public void setExportdatatype(String exportdatatype) {
		this.exportdatatype = exportdatatype;
	}

}
