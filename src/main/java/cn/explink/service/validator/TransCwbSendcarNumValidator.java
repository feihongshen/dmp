package cn.explink.service.validator;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import cn.explink.controller.CwbOrderDTO;
import cn.explink.domain.Customer;
import cn.explink.service.CwbOrderValidator;

@Component
public class TransCwbSendcarNumValidator implements CwbOrderValidator {

	private Customer customer;
	@Override
	public void validate(CwbOrderDTO cwbOrder) {
		// 发货数量大于0并且运单号不为空验证数量是否相等
		if (customer != null && customer.getIsUsetranscwb() == 0) {//使用客户使用运单号
			if (cwbOrder.getSendcargonum() > 0 && cwbOrder.getSendcargonum() < 2000) {
				if (StringUtils.hasLength(cwbOrder.getTranscwb())) {
					int length = cwbOrder.getTranscwb().split(",").length;
					if (length != cwbOrder.getSendcargonum()) {
						throw new RuntimeException("发货数量与运单号数量不一致");
					}
				}
			}
		}
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
}
