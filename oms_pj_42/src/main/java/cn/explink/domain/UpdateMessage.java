package cn.explink.domain;

/**
 * 存储各个云功能的最新更新数据时间表
 *
 */
public class UpdateMessage {
	private long id;
	private String menuname;
	private String lastupdatetime;
	private long menunvalue;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMenuname() {
		return menuname;
	}

	public void setMenuname(String menuname) {
		this.menuname = menuname;
	}

	public String getLastupdatetime() {
		return lastupdatetime;
	}

	public void setLastupdatetime(String lastupdatetime) {
		this.lastupdatetime = lastupdatetime;
	}

	public long getMenunvalue() {
		return menunvalue;
	}

	public void setMenunvalue(long menunvalue) {
		this.menunvalue = menunvalue;
	}

}
