package cn.explink.domain;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 地址匹配日志表
 * @author neo01.huang
 * 2016-4-12
 */
public class AddressMatchLog implements Serializable {

	private static final long serialVersionUID = 1219641434805069865L;
	
	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 序号
	 */
	private String itemno;
	/**
	 * 地址
	 */
	private String address;
	/**
	 * 匹配状态，0：成功，1：未维护关键字，2：关键字无对应站点，98：multipleResult，99：exceptionResult
	 */
	private String matchStatus;
	/**
	 * 失败原因
	 */
	private String matchMsg;
	/**
	 * 创建时间
	 */
	private Timestamp createTime;
	/**
	 * 修改时间
	 */
	private Timestamp updateTime;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getItemno() {
		return itemno;
	}
	public void setItemno(String itemno) {
		this.itemno = itemno;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMatchStatus() {
		return matchStatus;
	}
	public void setMatchStatus(String matchStatus) {
		this.matchStatus = matchStatus;
	}
	public String getMatchMsg() {
		return matchMsg;
	}
	public void setMatchMsg(String matchMsg) {
		this.matchMsg = matchMsg;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	
}
