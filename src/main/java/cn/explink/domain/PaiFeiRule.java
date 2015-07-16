/**
 *
 */
package cn.explink.domain;

import java.math.BigDecimal;

/**
 * @author Administrator
 *
 */
public class PaiFeiRule {
/**
 * 主键id
 */
private long id;
/**
 * 派费结算规则编号
 */
private String pfruleNO;
/**
 * 派费规则名称
 */
private String name;
/**
 * 派费规则类型
 */
private int type;
/**
 * 状态
 */
private int state;
/**
 * 备注
 */
private String remark;
/**
 * 拒收派费金额
 */
private BigDecimal jushouPFfee;
/**
 * 创建人
 */
private long creuserid;
/**
 * @return the id
 */
public long getId() {
	return this.id;
}
/**
 * @param id the id to set
 */
public void setId(long id) {
	this.id = id;
}
/**
 * @return the pfruleNO
 */
public String getPfruleNO() {
	return this.pfruleNO;
}
/**
 * @param pfruleNO the pfruleNO to set
 */
public void setPfruleNO(String pfruleNO) {
	this.pfruleNO = pfruleNO;
}
/**
 * @return the name
 */
public String getName() {
	return this.name;
}
/**
 * @param name the name to set
 */
public void setName(String name) {
	this.name = name;
}
/**
 * @return the type
 */
public int getType() {
	return this.type;
}
/**
 * @param type the type to set
 */
public void setType(int type) {
	this.type = type;
}
/**
 * @return the state
 */
public int getState() {
	return this.state;
}
/**
 * @param state the state to set
 */
public void setState(int state) {
	this.state = state;
}
/**
 * @return the remark
 */
public String getRemark() {
	return this.remark;
}
/**
 * @param remark the remark to set
 */
public void setRemark(String remark) {
	this.remark = remark;
}
/**
 * @return the jushouPFfee
 */
public BigDecimal getJushouPFfee() {
	return this.jushouPFfee;
}
/**
 * @param jushouPFfee the jushouPFfee to set
 */
public void setJushouPFfee(BigDecimal jushouPFfee) {
	this.jushouPFfee = jushouPFfee;
}
/**
 * @return the creuserid
 */
public long getCreuserid() {
	return this.creuserid;
}
/**
 * @param creuserid the creuserid to set
 */
public void setCreuserid(long creuserid) {
	this.creuserid = creuserid;
}

}
