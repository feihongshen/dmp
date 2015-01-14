package cn.explink.domain;

import java.util.ArrayList;
import java.util.List;

import cn.explink.controller.OverdueExMoController.TDCell;

public class OverdueBranchResultVO {

	private Long branchId = null;

	private List<TDCell> resultList = new ArrayList<TDCell>();

	public OverdueBranchResultVO(Long branchId) {
		this.branchId = branchId;
	}

	public Long getBranchId() {
		return this.branchId;
	}

	public void setBranchId(Long branchId) {
		this.branchId = branchId;
	}

	public List<TDCell> getResultList() {
		return this.resultList;
	}

	public void setResultList(List<TDCell> resultList) {
		this.resultList = resultList;
	}

}
