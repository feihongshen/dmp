package cn.explink.b2c.gome;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Gome_Master {

	@Autowired
	private GomeService_GetOrders gomeService_GetOrders;
	@Autowired
	private GomeService_SuccessOrders gomeService_SuccessOrders;

	public GomeService_GetOrders getGomeService_GetOrders() {
		return gomeService_GetOrders;
	}

	public void setGomeService_GetOrders(GomeService_GetOrders gomeService_GetOrders) {
		this.gomeService_GetOrders = gomeService_GetOrders;
	}

	public GomeService_SuccessOrders getGomeService_SuccessOrders() {
		return gomeService_SuccessOrders;
	}

	public void setGomeService_SuccessOrders(GomeService_SuccessOrders gomeService_SuccessOrders) {
		this.gomeService_SuccessOrders = gomeService_SuccessOrders;
	}

}
