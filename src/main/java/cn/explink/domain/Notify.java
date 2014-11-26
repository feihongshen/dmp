package cn.explink.domain;

public class Notify {
	private int id;
	private String title;// 标题
	private int type;// 1 公告 2 通知
	private String content;// 内容
	private long creuserid;// 发布人
	private String cretime;// 发布时间
	private long edituserid;// 修改人
	private String edittime;// 修改时间
	private int istop;// 默认0 置顶标识 1为置顶
	private int state;// 默认1 是否删除标识 0为删除

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getCreuserid() {
		return creuserid;
	}

	public void setCreuserid(long creuserid) {
		this.creuserid = creuserid;
	}

	public String getCretime() {
		return cretime;
	}

	public void setCretime(String cretime) {
		this.cretime = cretime;
	}

	public long getEdituserid() {
		return edituserid;
	}

	public void setEdituserid(long edituserid) {
		this.edituserid = edituserid;
	}

	public String getEdittime() {
		return edittime;
	}

	public void setEdittime(String edittime) {
		this.edittime = edittime;
	}

	public int getIstop() {
		return istop;
	}

	public void setIstop(int istop) {
		this.istop = istop;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

}
