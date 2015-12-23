/**
 *
 */
package cn.explink.domain.VO.express;

import java.math.BigDecimal;
import java.util.List;

import cn.explink.domain.express.CwbOrderForCombineQuery;

/**
 * @author songkaojun 2015年10月10日
 */
public class CombineQueryView {

	/**
	 * 运单总数
	 */
	private int waybillTotalCount;
	/**
	 * 总件数
	 *
	 */
	private int itemTotalCount;

	/**
	 * 费用合计
	 *
	 */
	private BigDecimal feeTotalNum;

	/**
	 * 运单信息列表
	 */
	private List<CwbOrderForCombineQuery> cwbOrderList;

	public int getWaybillTotalCount() {
		return this.waybillTotalCount;
	}

	public void setWaybillTotalCount(int waybillTotalCount) {
		this.waybillTotalCount = waybillTotalCount;
	}

	public int getItemTotalCount() {
		return this.itemTotalCount;
	}

	public void setItemTotalCount(int itemTotalCount) {
		this.itemTotalCount = itemTotalCount;
	}

	public BigDecimal getFeeTotalNum() {
		return this.feeTotalNum;
	}

	public void setFeeTotalNum(BigDecimal feeTotalNum) {
		this.feeTotalNum = feeTotalNum;
	}

	public List<CwbOrderForCombineQuery> getCwbOrderList() {
		return this.cwbOrderList;
	}

	public void setCwbOrderList(List<CwbOrderForCombineQuery> cwbOrderList) {
		this.cwbOrderList = cwbOrderList;
	}

}
