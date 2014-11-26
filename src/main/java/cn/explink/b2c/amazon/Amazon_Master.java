package cn.explink.b2c.amazon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Amazon_Master {

	@Autowired
	private AmazonService_GetOrders amazonService_GetOrders;

	public AmazonService_GetOrders getAmazonService_GetOrders() {
		return amazonService_GetOrders;
	}

	public void setAmazonService_GetOrders(AmazonService_GetOrders amazonService_GetOrders) {
		this.amazonService_GetOrders = amazonService_GetOrders;
	}

}
