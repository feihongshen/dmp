package cn.explink.b2c.auto.order.handle;

import cn.explink.b2c.auto.order.vo.InfDmpOrderSendVO;
import cn.explink.b2c.vipshop.VipShop;

/**
 * 订单处理接口
 * @author jian.xie
 *
 */
public interface IOrderHandler {
	
	public void dealWith(InfDmpOrderSendVO orderSend,VipShop vipshop);

}
