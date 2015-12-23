package cn.explink.domain.VO.express;
/**
 * 站点运费
 * @author jiangyu 2015年8月11日
 *
 */
public class ExpressStationBillPageView extends ExpressBillBasePageView{
	private Long branchId;
	
	private String branchName;
	
	public ExpressStationBillPageView() {
		super();
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

	public ExpressStationBillPageView(Long branchId, String branchName) {
		super();
		this.branchId = branchId;
		this.branchName = branchName;
	}
	
	
}
