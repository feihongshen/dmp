package cn.explink.service.addressmatch;

public class MatchResult {
	private long branchid;
	private String key;

	public MatchResult(long branchid, String key) {
		super();
		this.branchid = branchid;
		this.key = key;
	}

	public long getBranchid() {
		return branchid;
	}

	public String getKey() {
		return key;
	}

}
