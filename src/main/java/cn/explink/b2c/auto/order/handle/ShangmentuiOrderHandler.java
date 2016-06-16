package cn.explink.b2c.auto.order.handle;

import org.springframework.stereotype.Component;

import cn.explink.b2c.auto.order.vo.InfDmpOrderSendVO;

/**
 * 上门退订单处理
 * 注意揽退单的新增、修改、取消顺序
 * @author jian.xie
 *
 */
@Component("shangmentuiOrderHandler")
public class ShangmentuiOrderHandler implements IOrderHandler{

	@Override
	public void dealWith(InfDmpOrderSendVO orderSend) {
		// TODO Auto-generated method stub
		
	}

}
