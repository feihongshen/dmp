package cn.explink.domain.VO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import cn.explink.domain.ExpressSetBranchDeliveryFeeBillDetail;

public class ExpressSetBranchDeliveryFeeBillVO {
	private int id;
	private String billBatch;
	private int billState;
	private int branchId;
	private String branchName;
	private String createDate;
	private String createDateFrom;
	private String createDateTo;
	private String heXiaoDate;
	private String heXiaoDateFrom;
	private String heXiaoDateTo;
	private int dateType;
	private String beginDate;
	private String endDate;
	private String cwbs;
	private int cwbType;
	private int cwbCount;
	private BigDecimal deliveryFee;
	private String remark;
	private int creator;
	private int heXiaoPerson;
	private int shenHePerson;
	private String shenHeDate;
	private String contractColumn;
	private String contractColumnOrder;
	private String branchname;
	private String branchaddress;
	private List<ExpressSetBranchDeliveryFeeBillDetail> billDetailList;
	private Map<Object, Object> customerDeliveryFee;
	private ExpressSetBranchDeliveryFeeBillDetailVO deliveryFeeObj;
	private ExpressSetBranchDeliveryFeeBillDetailVO pickupFeeObj;
	private String qualityControlClause;

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setBillBatch(String billBatch) {
		this.billBatch = billBatch;
	}

	public String getBillBatch() {
		return billBatch;
	}

	public void setBillState(int billState) {
		this.billState = billState;
	}

	public int getBillState() {
		return billState;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setHeXiaoDate(String heXiaoDate) {
		this.heXiaoDate = heXiaoDate;
	}

	public String getHeXiaoDate() {
		return heXiaoDate;
	}

	public void setDateType(int dateType) {
		this.dateType = dateType;
	}

	public int getDateType() {
		return dateType;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setCwbs(String cwbs) {
		this.cwbs = cwbs;
	}

	public String getCwbs() {
		return cwbs;
	}

	public void setCwbType(int cwbType) {
		this.cwbType = cwbType;
	}

	public int getCwbType() {
		return cwbType;
	}

	public void setCwbCount(int cwbCount) {
		this.cwbCount = cwbCount;
	}

	public int getCwbCount() {
		return cwbCount;
	}

	public void setDeliveryFee(BigDecimal deliveryFee) {
		this.deliveryFee = deliveryFee;
	}

	public BigDecimal getDeliveryFee() {
		return deliveryFee;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRemark() {
		return remark;
	}

	public String getCreateDateFrom() {
		return createDateFrom;
	}

	public void setCreateDateFrom(String createDateFrom) {
		this.createDateFrom = createDateFrom;
	}

	public String getCreateDateTo() {
		return createDateTo;
	}

	public void setCreateDateTo(String createDateTo) {
		this.createDateTo = createDateTo;
	}

	public String getHeXiaoDateFrom() {
		return heXiaoDateFrom;
	}

	public void setHeXiaoDateFrom(String heXiaoDateFrom) {
		this.heXiaoDateFrom = heXiaoDateFrom;
	}

	public String getHeXiaoDateTo() {
		return heXiaoDateTo;
	}

	public void setHeXiaoDateTo(String heXiaoDateTo) {
		this.heXiaoDateTo = heXiaoDateTo;
	}

	public String getContractColumn() {
		return contractColumn;
	}

	public void setContractColumn(String contractColumn) {
		this.contractColumn = contractColumn;
	}

	public String getContractColumnOrder() {
		return contractColumnOrder;
	}

	public void setContractColumnOrder(String contractColumnOrder) {
		this.contractColumnOrder = contractColumnOrder;
	}

	public int getCreator() {
		return creator;
	}

	public void setCreator(int creator) {
		this.creator = creator;
	}

	public int getHeXiaoPerson() {
		return heXiaoPerson;
	}

	public void setHeXiaoPerson(int heXiaoPerson) {
		this.heXiaoPerson = heXiaoPerson;
	}

	public int getShenHePerson() {
		return shenHePerson;
	}

	public void setShenHePerson(int shenHePerson) {
		this.shenHePerson = shenHePerson;
	}

	public String getShenHeDate() {
		return shenHeDate;
	}

	public void setShenHeDate(String shenHeDate) {
		this.shenHeDate = shenHeDate;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public Map<Object, Object> getCustomerDeliveryFee() {
		return customerDeliveryFee;
	}

	public void setCustomerDeliveryFee(Map<Object, Object> customerDeliveryFee) {
		this.customerDeliveryFee = customerDeliveryFee;
	}

	public ExpressSetBranchDeliveryFeeBillDetailVO getDeliveryFeeObj() {
		return deliveryFeeObj;
	}

	public void setDeliveryFeeObj(
			ExpressSetBranchDeliveryFeeBillDetailVO deliveryFeeObj) {
		this.deliveryFeeObj = deliveryFeeObj;
	}

	public ExpressSetBranchDeliveryFeeBillDetailVO getPickupFeeObj() {
		return pickupFeeObj;
	}

	public void setPickupFeeObj(
			ExpressSetBranchDeliveryFeeBillDetailVO pickupFeeObj) {
		this.pickupFeeObj = pickupFeeObj;
	}

	public String getQualityControlClause() {
		return qualityControlClause;
	}

	public void setQualityControlClause(String qualityControlClause) {
		this.qualityControlClause = qualityControlClause;
	}

	public String getBranchname() {
		return branchname;
	}

	public void setBranchname(String branchname) {
		this.branchname = branchname;
	}

	public String getBranchaddress() {
		return branchaddress;
	}

	public void setBranchaddress(String branchaddress) {
		this.branchaddress = branchaddress;
	}

	public List<ExpressSetBranchDeliveryFeeBillDetail> getBillDetailList() {
		return billDetailList;
	}

	public void setBillDetailList(
			List<ExpressSetBranchDeliveryFeeBillDetail> billDetailList) {
		this.billDetailList = billDetailList;
	}

}
