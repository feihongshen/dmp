package cn.explink.domain;

public class SalaryImport {
	private String filename;//导入工资选项名
	private String filetext;//中文名
	private int whichvalue;//固定值或者临时值
	private int ischecked;//是否被选中（工资条）
	private int addordeduct;//描述增减项
	
	public int getAddordeduct() {
		return addordeduct;
	}
	public void setAddordeduct(int addordeduct) {
		this.addordeduct = addordeduct;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getFiletext() {
		return filetext;
	}
	public void setFiletext(String filetext) {
		this.filetext = filetext;
	}
	public int getWhichvalue() {
		return whichvalue;
	}
	public void setWhichvalue(int whichvalue) {
		this.whichvalue = whichvalue;
	}
	public int getIschecked() {
		return ischecked;
	}
	public void setIschecked(int ischecked) {
		this.ischecked = ischecked;
	}
	
}
