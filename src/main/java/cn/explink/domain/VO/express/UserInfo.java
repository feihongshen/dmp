package cn.explink.domain.VO.express;
/**
 * 记录当前登录人的相关信息
 * @author jiangyu 2015年8月15日
 *
 */
public class UserInfo {
	
	private Long branchId;
	
	private String branchName;
	
	private Long provinceId;
	
	private String provinceName;
	
	private Long userId;
	
	private String userName;
	
	public UserInfo() {
		// TODO Auto-generated constructor stub
	}
	
	public UserInfo(Long branchId, String branchName, Long provinceId, String provinceName, Long userId, String userName) {
		super();
		this.branchId = branchId;
		this.branchName = branchName;
		this.provinceId = provinceId;
		this.provinceName = provinceName;
		this.userId = userId;
		this.userName = userName;
	}
	public UserInfo(Long branchId, String branchName,Long userId, String userName) {
		super();
		this.branchId = branchId;
		this.branchName = branchName;
		this.userId = userId;
		this.userName = userName;
	}
	
	

	public Long getBranchId() {
		return branchId;
	}

	public void setBranchId(Long branchId) {
		this.branchId = branchId;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public Long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
