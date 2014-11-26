package cn.explink.b2c.rufengda;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Rufengda_Master {

	@Autowired
	private RufengdaService_GetOrders rufengdaService_GetOrders;
	@Autowired
	private RufengdaService_SuccessOrders rufengdaService_SuccessOrders;
	@Autowired
	private RufengdaService_SynUserInfo rufengdaService_SynUserInfo;

	public RufengdaService_SynUserInfo getRufengdaService_SynUserInfo() {
		return rufengdaService_SynUserInfo;
	}

	public void setRufengdaService_SynUserInfo(RufengdaService_SynUserInfo rufengdaService_SynUserInfo) {
		this.rufengdaService_SynUserInfo = rufengdaService_SynUserInfo;
	}

	public RufengdaService_GetOrders getRufengdaService_GetOrders() {
		return rufengdaService_GetOrders;
	}

	public void setRufengdaService_GetOrders(RufengdaService_GetOrders rufengdaService_GetOrders) {
		this.rufengdaService_GetOrders = rufengdaService_GetOrders;
	}

	public RufengdaService_SuccessOrders getRufengdaService_SuccessOrders() {
		return rufengdaService_SuccessOrders;
	}

	public void setRufengdaService_SuccessOrders(RufengdaService_SuccessOrders rufengdaService_SuccessOrders) {
		this.rufengdaService_SuccessOrders = rufengdaService_SuccessOrders;
	}

}
