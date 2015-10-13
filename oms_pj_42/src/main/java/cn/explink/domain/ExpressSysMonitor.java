package cn.explink.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "express_sys_monitor")
public class ExpressSysMonitor implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(name = "OPTIME")
	private String optime;
	@Column(name = "TYPE")
	private String type;
	@Column(name = "DEALFLAG")
	private int dealflag = 0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOptime() {
		return optime;
	}

	public void setOptime(String optime) {
		this.optime = optime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getDealflag() {
		return dealflag;
	}

	public void setDealflag(int dealflag) {
		this.dealflag = dealflag;
	}

}
