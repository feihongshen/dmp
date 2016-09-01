package cn.explink.b2c.auto.order.handle;

import org.springframework.stereotype.Component;

import cn.explink.b2c.auto.order.vo.InfDmpOrderSendVO;
import cn.explink.b2c.vipshop.VipShop;

/**
 * OXO订单处理
 * @author jian.xie
 *
 */
@Component("oXOOrderHandler")
public class OXOOrderHandler implements IOrderHandler {

	@Override
	public void dealWith(InfDmpOrderSendVO orderSend,VipShop vipshop) {
		// TODO Auto-generated method stub

	}

}
