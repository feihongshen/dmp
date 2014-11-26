package cn.explink.b2c.saohuobang;

import org.springframework.stereotype.Component;

import cn.explink.b2c.saohuobang.xml.RequestOrder;

@Component
public class Track_Saohuobang {

	public static void validate(RequestOrder Order) {
		// 验证承运商是否对应订单记录
		if (Order.getLogisticProviderID() == null) {
			throw new RuntimeException("LogisticProviderID不能为空：" + Order.getLogisticProviderID());
		}
		if (Order.getTxLogisticID() == null) {
			throw new RuntimeException("txLogisticID(订单号)不能为空" + Order.getTxLogisticID());
		}
		if (Order.getCustomerId() == null) {
			throw new RuntimeException("customerId不能为空" + Order.getCustomerId());
		}
		if (Order.getType() == null || "0".equals(Order.getType())) {
			throw new RuntimeException("订单类型不能为空且不能为0" + Order.getType());
		}
		if (Order.getStoreName() == null) {
			throw new RuntimeException("StoreName信息都不能为空" + Order.getStoreName());
		}
		if (Order.getSupplierID() == null || Order.getSupplierName() == null) {
			throw new RuntimeException("SupplierID和Suppliername不能为空" + Order.getTxLogisticID());
		}
		if (Order.getReceiver().getAddress() == null) {
			throw new RuntimeException("收货地址不能为空" + Order.getTxLogisticID());
		}
		if (Order.getOrderCreateTime() == null) {
			throw new RuntimeException("下单时间不能为空" + Order.getTxLogisticID());
		}

	}

}
