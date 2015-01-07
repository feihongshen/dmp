package cn.explink.domain;

import java.util.List;

public class SmtFareSettleDetailResultVO {

	private int page = 1;

	private int pageCount = 1;

	private int count = 0;

	private List<SmtFareSettleDetailVO> resultList = null;

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

	public List<SmtFareSettleDetailVO> getResultList() {
		return this.resultList;
	}

	public void setResultList(List<SmtFareSettleDetailVO> resultList) {
		this.resultList = resultList;
	}

}
