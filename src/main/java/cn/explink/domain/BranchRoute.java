package cn.explink.domain;

public class BranchRoute {
	private long fromBranchId;
	private long toBranchId;
	int type;// 1为双向，2位仅正向，3位仅倒向

	public long getFromBranchId() {
		return fromBranchId;
	}

	public void setFromBranchId(long fromBranchId) {
		this.fromBranchId = fromBranchId;
	}

	public long getToBranchId() {
		return toBranchId;
	}

	public void setToBranchId(long toBranchId) {
		this.toBranchId = toBranchId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
