package cn.explink.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 上门退运费结算结果VO.
 *
 * @author zhaoshb
 * @since DMP3.0
 */
public class SmtFareSettleResultVO {

	private int page = 1;

	private int pageCount = 1;

	private int count = 0;

	private List<SmtFareSettleVO> resultList = new ArrayList<SmtFareSettleVO>();

	public List<SmtFareSettleVO> getResultList() {
		return this.resultList;
	}

	public void setResultList(List<SmtFareSettleVO> resultList) {
		this.resultList = resultList;
	}

	public void addStationResult(Long stationId, String stationName, Long venderId, String venderName) {
		SmtFareSettleVO vo = new SmtFareSettleVO();
		vo.setStationId(stationId);
		vo.setStationName(stationName);
		vo.setVenderId(venderId);
		vo.setVenderName(venderName);
		this.getResultList().add(vo);
	}

	public void addDeliverResult(Long stationId, String stationName, Long venderId, String venderName) {
		SmtFareSettleVO vo = new SmtFareSettleVO();
		vo.setDeliverId(stationId);
		vo.setDeliverName(stationName);
		vo.setVenderId(venderId);
		vo.setVenderName(venderName);
		this.getResultList().add(vo);
	}

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

}
