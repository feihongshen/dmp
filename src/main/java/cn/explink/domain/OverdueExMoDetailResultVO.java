package cn.explink.domain;

import java.util.List;

public class OverdueExMoDetailResultVO {

	private int page = 0;

	private int pageCount = 0;

	private int count = 0;

	private List<OverdueExMoDetailVO> resultList = null;

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageCount() {
		return this.pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getCount() {
		return this.count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<OverdueExMoDetailVO> getResultList() {
		return this.resultList;
	}

	public void setResultList(List<OverdueExMoDetailVO> resultList) {
		this.resultList = resultList;
	}

}
