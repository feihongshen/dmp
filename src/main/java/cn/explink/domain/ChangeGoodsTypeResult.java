package cn.explink.domain;

import java.util.Map;
import java.util.Set;

public class ChangeGoodsTypeResult {

	private int successedCount = -1;

	private int totalCount = -1;

	private Map<String, Set<String>> errorMap = null;

	public int getSuccessedCount() {
		return successedCount;
	}

	public void setSuccessedCount(int successedCount) {
		this.successedCount = successedCount;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public Map<String, Set<String>> getErrorMap() {
		return errorMap;
	}

	public void setErrorMap(Map<String, Set<String>> errorMap) {
		this.errorMap = errorMap;
	}

}
