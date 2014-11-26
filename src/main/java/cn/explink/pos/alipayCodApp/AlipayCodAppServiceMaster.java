package cn.explink.pos.alipayCodApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlipayCodAppServiceMaster {

	@Autowired
	private AlipayCodAppService_search alipayCodAppService_search;

	@Autowired
	private AlipayCodAppService_pay alipayCodAppService_pay;

	public AlipayCodAppService_search getAlipayCodAppService_search() {
		return alipayCodAppService_search;
	}

	public void setAlipayCodAppService_search(AlipayCodAppService_search alipayCodAppService_search) {
		this.alipayCodAppService_search = alipayCodAppService_search;
	}

	public AlipayCodAppService_pay getAlipayCodAppService_pay() {
		return alipayCodAppService_pay;
	}

	public void setAlipayCodAppService_pay(AlipayCodAppService_pay alipayCodAppService_pay) {
		this.alipayCodAppService_pay = alipayCodAppService_pay;
	}

}
