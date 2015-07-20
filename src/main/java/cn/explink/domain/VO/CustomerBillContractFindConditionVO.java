package cn.explink.domain.VO;


public class CustomerBillContractFindConditionVO {
	private String billBatches;

	private String billState;

	private String crestartdate;

	private String creenddate;

	private String verificationstratdate;

	private String verificationenddate;

	private String customerId;

	private String cwbOrderType;

	private String condition;

	private String sequence;

	public void setBillBatches(String billBatches){
	this.billBatches = billBatches;
	}
	public String getBillBatches(){
	return this.billBatches;
	}
	public void setBillState(String billState){
	this.billState = billState;
	}
	public String getBillState(){
	return this.billState;
	}
	public void setCrestartdate(String crestartdate){
	this.crestartdate = crestartdate;
	}
	public String getCrestartdate(){
	return this.crestartdate;
	}
	public void setCreenddate(String creenddate){
	this.creenddate = creenddate;
	}
	public String getCreenddate(){
	return this.creenddate;
	}
	public void setVerificationstratdate(String verificationstratdate){
	this.verificationstratdate = verificationstratdate;
	}
	public String getVerificationstratdate(){
	return this.verificationstratdate;
	}
	public void setVerificationenddate(String verificationenddate){
	this.verificationenddate = verificationenddate;
	}
	public String getVerificationenddate(){
	return this.verificationenddate;
	}
	public void setCustomerId(String customerId){
	this.customerId = customerId;
	}
	public String getCustomerId(){
	return this.customerId;
	}
	public void setCwbOrderType(String cwbOrderType){
	this.cwbOrderType = cwbOrderType;
	}
	public String getCwbOrderType(){
	return this.cwbOrderType;
	}
	public void setCondition(String condition){
	this.condition = condition;
	}
	public String getCondition(){
	return this.condition;
	}
	public void setSequence(String sequence){
	this.sequence = sequence;
	}
	public String getSequence(){
	return this.sequence;
	}
		
}