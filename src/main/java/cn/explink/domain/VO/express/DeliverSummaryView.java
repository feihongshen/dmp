/**
 *
 */
package cn.explink.domain.VO.express;

import java.util.List;

/**
 * @author songkaojun 2015年8月9日
 */
public class DeliverSummaryView {
	/**
	 * 汇总单表头汇总项
	 */
	private DeliverSummaryItem headSummary;

	/**
	 * 汇总单表体项列表
	 */
	private List<DeliverSummaryItem> bodySummaryList;

	public DeliverSummaryItem getHeadSummary() {
		return this.headSummary;
	}

	public void setHeadSummary(DeliverSummaryItem headSummary) {
		this.headSummary = headSummary;
	}

	public List<DeliverSummaryItem> getBodySummaryList() {
		return this.bodySummaryList;
	}

	public void setBodySummaryList(List<DeliverSummaryItem> bodySummaryList) {
		this.bodySummaryList = bodySummaryList;
	}

}
