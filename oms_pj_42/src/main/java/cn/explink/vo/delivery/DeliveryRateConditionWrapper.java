package cn.explink.vo.delivery;

import java.io.IOException;
import java.util.List;

import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.DeliveryRateCondition;
import cn.explink.util.JsonUtil;
import cn.explink.vo.delivery.util.DeliveryRateDisplayUtil;

public class DeliveryRateConditionWrapper {

	private DeliveryRateCondition deliveryRateCondition;

	private String query;

	public DeliveryRateConditionWrapper(DeliveryRateCondition deliveryRateCondition, List<Branch> branchList, List<Customer> customerList) {
		this.deliveryRateCondition = deliveryRateCondition;
		try {
			DeliveryRateRequest deliveryRateRequest = JsonUtil.readValue(deliveryRateCondition.getDeliveryRateRequest(), DeliveryRateRequest.class);
			this.query = DeliveryRateDisplayUtil.buildQuery(deliveryRateRequest, branchList, customerList);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public DeliveryRateCondition getDeliveryRateCondition() {
		return deliveryRateCondition;
	}

	public void setDeliveryRateCondition(DeliveryRateCondition deliveryRateCondition) {
		this.deliveryRateCondition = deliveryRateCondition;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

}
