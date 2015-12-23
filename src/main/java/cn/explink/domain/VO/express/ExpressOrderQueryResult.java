/**
 *
 */
package cn.explink.domain.VO.express;

import cn.explink.domain.CwbOrder;

/**
 * @author songkaojun 2015年10月16日
 */
public class ExpressOrderQueryResult {
	/**
	 * 快递单是否存在
	 */
	private boolean expressOrderExist;
	/**
	 * 是否已经合包
	 */
	private boolean inPackage;
	private CwbOrder cwbOrder;

	public boolean isExpressOrderExist() {
		return this.expressOrderExist;
	}

	public void setExpressOrderExist(boolean expressOrderExist) {
		this.expressOrderExist = expressOrderExist;
	}

	public boolean isInPackage() {
		return this.inPackage;
	}

	public void setInPackage(boolean inPackage) {
		this.inPackage = inPackage;
	}

	public CwbOrder getCwbOrder() {
		return this.cwbOrder;
	}

	public void setCwbOrder(CwbOrder cwbOrder) {
		this.cwbOrder = cwbOrder;
	}

}
