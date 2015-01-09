package cn.explink.domain;

import java.util.List;
import java.util.Map;

public class OverdueResultVO {
	private List<String> showColList = null;

	private Map<Long, String> branchMap = null;

	private long venderId = -1;

	private String venderName = null;

	private Integer page = Integer.valueOf(1);

	private Integer pageCount = Integer.valueOf(1);

	private Integer count = Integer.valueOf(0);

	public List<String> getShowColList() {
		return this.showColList;
	}

	public void setShowColList(List<String> showColList) {
		this.showColList = showColList;
	}

	public Map<Long, String> getBranchMap() {
		return this.branchMap;
	}

	public void setBranchMap(Map<Long, String> branchMap) {
		this.branchMap = branchMap;
	}

	public Integer getPage() {
		return this.page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPageCount() {
		return this.pageCount;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	public Integer getCount() {
		return this.count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public long getVenderId() {
		return this.venderId;
	}

	public void setVenderId(long venderId) {
		this.venderId = venderId;
	}

	public String getVenderName() {
		return this.venderName;
	}

	public void setVenderName(String venderName) {
		this.venderName = venderName;
	}

}
