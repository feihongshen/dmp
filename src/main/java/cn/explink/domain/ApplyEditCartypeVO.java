package cn.explink.domain;

import java.math.BigDecimal;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name="express_ops_applyeditcartype")
public class ApplyEditCartypeVO {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;//id自增
	private String cwb;//订单号
	private String transcwb;
	private long customerid;//客户ID
	private String customername;//客户姓名
	private int doType;//订单类型
	private String originalCartype;//货物类型(修改前)
	private String applyCartype;//货物类型(修改后)
	private BigDecimal carrealweight;//货物重量
	private String carsize;//货物尺寸
	private long applyBranchid;//申请机构
	private String applyBranchname;//申请机构名称
	private long applyUserid;//申请者ID
	private String applyUsername;//申请者姓名
	private String applyTime;//申请日期
	private long reviewUserid;//审核者ID
	private String reviewUsername;//审核者姓名
	private String reviewTime;//审核日期
	private int reviewStatus;//审核状态,0.未处理1.审核通过2.审核不通过
	private String remark;
	
	//下面的字段不从数据表里获取
	@Transient
	private String doTypeName;//订单类型名称，对应关系可查看CwbOrderTypeIdEnum.java
	@Transient
	private String reviewStatusName;//审核状态名称
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCwb() {
		return cwb;
	}
	public void setCwb(String cwb) {
		this.cwb = cwb;
	}
	public long getCustomerid() {
		return customerid;
	}
	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}
	public int getDoType() {
		return doType;
	}
	public void setDoType(int doType) {
		this.doType = doType;
	}
	public String getOriginalCartype() {
		return originalCartype;
	}
	public void setOriginalCartype(String originalCartype) {
		this.originalCartype = originalCartype;
	}
	public String getApplyCartype() {
		return applyCartype;
	}
	public void setApplyCartype(String applyCartype) {
		this.applyCartype = applyCartype;
	}
	public BigDecimal getCarrealweight() {
		return carrealweight;
	}
	public void setCarrealweight(BigDecimal carrealweight) {
		this.carrealweight = carrealweight;
	}
	public long getApplyBranchid() {
		return applyBranchid;
	}
	public void setApplyBranchid(long applyBranchid) {
		this.applyBranchid = applyBranchid;
	}
	public long getApplyUserid() {
		return applyUserid;
	}
	public void setApplyUserid(long applyUserid) {
		this.applyUserid = applyUserid;
	}
	public String getApplyUsername() {
		return applyUsername;
	}
	public void setApplyUsername(String applyUsername) {
		this.applyUsername = applyUsername;
	}
	public String getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}
	public long getReviewUserid() {
		return reviewUserid;
	}
	public void setReviewUserid(long reviewUserid) {
		this.reviewUserid = reviewUserid;
	}
	public String getReviewUsername() {
		return reviewUsername;
	}
	public void setReviewUsername(String reviewUsername) {
		this.reviewUsername = reviewUsername;
	}
	public String getReviewTime() {
		return reviewTime;
	}
	public void setReviewTime(String reviewTime) {
		this.reviewTime = reviewTime;
	}
	public int getReviewStatus() {
		return reviewStatus;
	}
	public void setReviewStatus(int reviewStatus) {
		this.reviewStatus = reviewStatus;
	}
	public String getCarsize() {
		return carsize;
	}
	public void setCarsize(String carsize) {
		this.carsize = carsize;
	}
	public String getTranscwb() {
		return transcwb;
	}
	public void setTranscwb(String transcwb) {
		this.transcwb = transcwb;
	}
	public String getApplyBranchname() {
		return applyBranchname;
	}
	public void setApplyBranchname(String applyBranchname) {
		this.applyBranchname = applyBranchname;
	}
	public String getCustomername() {
		return customername;
	}
	public void setCustomername(String customername) {
		this.customername = customername;
	}
	public String getDoTypeName() {
		return doTypeName;
	}
	public void setDoTypeName(String doTypeName) {
		this.doTypeName = doTypeName;
	}
	
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
	
	public String getReviewStatusName() {
		return reviewStatusName;
	}
	public void setReviewStatusName(String reviewStatusName) {
		this.reviewStatusName = reviewStatusName;
	}
	@Override
	public String toString() {
		return "ApplyEditCartypeVO [id=" + id + ", cwb=" + cwb + ", transcwb=" + transcwb + ", customerid=" + customerid
				+ ", customername=" + customername + ", doType=" + doType + ", originalCartype=" + originalCartype
				+ ", applyCartype=" + applyCartype + ", carrealweight=" + carrealweight + ", carsize=" + carsize
				+ ", applyBranchid=" + applyBranchid + ", applyBranchname=" + applyBranchname + ", applyUserid="
				+ applyUserid + ", applyUsername=" + applyUsername + ", applyTime=" + applyTime + ", reviewUserid="
				+ reviewUserid + ", reviewUsername=" + reviewUsername + ", reviewTime=" + reviewTime + ", reviewStatus="
				+ reviewStatus + ", remark=" + remark + ", doTypeName=" + doTypeName + ", reviewStatusName="
				+ reviewStatusName + "]";
	}

	
	

}
