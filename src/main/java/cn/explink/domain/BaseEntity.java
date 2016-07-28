/* 
 * Copyright (c) 2008-2014 VIP.COM, All rights reserved. 
 */
package cn.explink.domain;

import java.io.Serializable;
import java.util.Date;



/**
 * Entity基类
 * @author wangwei
 * @version v1.0, 206年7月15日
 */
public class BaseEntity implements Serializable{
	private static final long serialVersionUID = 3267700727195341984L;

	/**
	 * 创建人
	 */
	private String createdByUser;
	
	/**
	 * 创建人机构
	 */
	private String createdOffice;
	
	/**
	 * 创建时间
	 */
	private Date createdDtmLoc;
	
	/**
	 * 创建时区
	 */
	private String createdTimeZone;
	
	/**
	 * 更新人
	 */
	private String updatedByUser;
	
	/**
	 * 创建人机构
	 */
	private String updatedOffice;
	
	/**
	 * 更新时间
	 */
	private Date updatedDtmLoc;
	
	/**
	 * 创建时区域
	 */
	private String updatedTimeZone;
	
	/**
	 * 版本号
	 */
	private Long recordVersion;

	//逻辑删除
	private Byte isDelete;
	
	public Byte getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Byte isDelete) {
		this.isDelete = isDelete;
	}

	public String getCreatedOffice() {
		return createdOffice;
	}

	public void setCreatedOffice(String createdOffice) {
		this.createdOffice = createdOffice == null ? "" : createdOffice;
	}

	public Date getCreatedDtmLoc() {
		return createdDtmLoc;
	}

	public void setCreatedDtmLoc(Date createdDtmLoc) {
		this.createdDtmLoc = createdDtmLoc == null ? new Date() : createdDtmLoc;
	}

	public String getCreatedTimeZone() {
		return createdTimeZone;
	}

	public void setCreatedTimeZone(String createdTimeZone) {
		this.createdTimeZone = createdTimeZone == null ? "" : createdTimeZone;
	}


	public String getUpdatedOffice() {
		return updatedOffice;
	}

	public void setUpdatedOffice(String updatedOffice) {
		this.updatedOffice = updatedOffice == null ? "" : updatedOffice;
	}
 
	public Date getUpdatedDtmLoc() {
		return updatedDtmLoc;
	}

	public void setUpdatedDtmLoc(Date updatedDtmLoc) {
		this.updatedDtmLoc = updatedDtmLoc == null ? new Date() : updatedDtmLoc;
	}

	public String getUpdatedTimeZone() {
		return updatedTimeZone;
	}

	public void setUpdatedTimeZone(String updatedTimeZone) {
		this.updatedTimeZone = updatedTimeZone == null ? "" : updatedTimeZone;
	}

	public Long getRecordVersion() {
		return recordVersion;
	}

	public void setRecordVersion(Long recordVersion) {
		this.recordVersion = recordVersion == null ? 0L : recordVersion;
	}

	public String getCreatedByUser() {
		return createdByUser;
	}

	public void setCreatedByUser(String createdByUser) {
		this.createdByUser = createdByUser == null ? "" : createdByUser;
	}

	public String getUpdatedByUser() {
		return updatedByUser;
	}

	public void setUpdatedByUser(String updatedByUser) {
		this.updatedByUser = updatedByUser == null ? "" : updatedByUser;
	}
	
	

}