package cn.explink.domain;

import java.util.ArrayList;
import java.util.List;

public class OverdueBranchResultVO {

	private Long branchId = null;

	private List<Object> resultList = new ArrayList<Object>();

	public OverdueBranchResultVO(Long branchId) {
		this.branchId = branchId;
	}

	public Long getBranchId() {
		return this.branchId;
	}

	public void setBranchId(Long branchId) {
		this.branchId = branchId;
	}

	public List<Object> getResultList() {
		return this.resultList;
	}

	public void setResultList(List<Object> resultList) {
		this.resultList = resultList;
	}

	public void addResult(List<Object> resultList) {
		this.resultList.addAll(resultList);
	}

}
