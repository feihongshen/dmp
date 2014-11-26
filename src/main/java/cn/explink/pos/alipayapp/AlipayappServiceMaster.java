package cn.explink.pos.alipayapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlipayappServiceMaster {

	@Autowired
	private AlipayappService_search alipayCodAppService_search;

	@Autowired
	private AlipayappService_pay alipayCodAppService_pay;

	public AlipayappService_search getAlipayCodAppService_search() {
		return alipayCodAppService_search;
	}

	public void setAlipayCodAppService_search(AlipayappService_search alipayCodAppService_search) {
		this.alipayCodAppService_search = alipayCodAppService_search;
	}

	public AlipayappService_pay getAlipayCodAppService_pay() {
		return alipayCodAppService_pay;
	}

	public void setAlipayCodAppService_pay(AlipayappService_pay alipayCodAppService_pay) {
		this.alipayCodAppService_pay = alipayCodAppService_pay;
	}

}
