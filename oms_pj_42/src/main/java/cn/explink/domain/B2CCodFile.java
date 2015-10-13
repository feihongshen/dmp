package cn.explink.domain;

public class B2CCodFile {

	private long id;
	private long filecount;// 文件递增数量
	private long type;// 1.亚马逊

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getFilecount() {
		return filecount;
	}

	public void setFilecount(long filecount) {
		this.filecount = filecount;
	}

	public long getType() {
		return type;
	}

	public void setType(long type) {
		this.type = type;
	}

}
