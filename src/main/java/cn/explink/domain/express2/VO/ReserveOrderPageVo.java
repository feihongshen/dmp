package cn.explink.domain.express2.VO;

import java.util.List;

import cn.explink.domain.express2.VO.ReserveOrderVo;

public class ReserveOrderPageVo {
	
	/**
	 * 结果总记录数
	 */
	private int totalRecord;
	
	/**
	* 预订单信息对象
	*/
	private List<ReserveOrderVo> reserveOrderVoList;

	public int getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}

	public List<ReserveOrderVo> getReserveOrderVoList() {
		return reserveOrderVoList;
	}

	public void setReserveOrderVoList(List<ReserveOrderVo> reserveOrderVoList) {
		this.reserveOrderVoList = reserveOrderVoList;
	}

	@Override
	public String toString() {
		return "ReserveOrderPageVo [totalRecord=" + totalRecord + ", reserveOrderVoList=" + reserveOrderVoList + "]";
	}
}
