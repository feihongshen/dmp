package cn.explink.domain;

/**
 * 用户下载管理
 * 
 * @author Administrator
 *
 */
public class DownloadManager {
	private long id;
	private long userid;
	private String datajson;
	private int modelid;
	private String createtime;
	private int state;
	private String filename;
	private String fileurl;
	private long timeout;
	private String endtime;
	private String cnfilename;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public String getDatajson() {
		return datajson;
	}

	public void setDatajson(String datajson) {
		this.datajson = datajson;
	}

	public int getModelid() {
		return modelid;
	}

	public void setModelid(int modelid) {
		this.modelid = modelid;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFileurl() {
		return fileurl;
	}

	public void setFileurl(String fileurl) {
		this.fileurl = fileurl;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getCnfilename() {
		return cnfilename;
	}

	public void setCnfilename(String cnfilename) {
		this.cnfilename = cnfilename;
	}

	public String getStateStr() {
		// 完成、暂停、已下载、导出中，排队中
		if (state == 0) {
			return "<font color='blue'>导出中</font>";
		}
		if (state == 1) {
			return "<font color='green'>完成</font>";
		}
		if (state == 2) {
			return "<font color='red'>暂停</font>";
		}
		if (state == -1) {
			return "<font color='gray'>排队中</font>";
		}
		if (state == 3) {
			return "<font color='green'>已下载</font>";
		}
		return "<font>排队中</font>";
	}
}
