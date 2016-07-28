package cn.explink.domain;


public class BranchSyncResultVo implements java.io.Serializable {
	
	private static final long serialVersionUID = -7873112119688063347L;
	
	private String branchName;
	
	private String carrierCode;
	
	private String carrierSiteCode;
	
	private String result;
	
	private String message;

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getCarrierCode() {
		return carrierCode;
	}

	public void setCarrierCode(String carrierCode) {
		this.carrierCode = carrierCode;
	}

	public String getCarrierSiteCode() {
		return carrierSiteCode;
	}

	public void setCarrierSiteCode(String carrierSiteCode) {
		this.carrierSiteCode = carrierSiteCode;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
