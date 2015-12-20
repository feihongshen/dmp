package cn.explink.pos.chinaums;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChinaUmsServiceMaster {
	@Autowired
	private ChinaUmsService_toLogin chinaUmsService_toLogin;
	@Autowired
	private ChinaUmsService_toLogout chinaUmsService_toLogout;
	@Autowired
	private ChinaUmsService_toBackOut chinaUmsService_toBackOut;
	@Autowired
	private ChinaUmsService_toCwbSearch chinaUmsService_toCwbSearch;
	@Autowired
	private ChinaUmsService_toExptFeedBack chinaUmsService_toExptFeedBack;
	@Autowired
	private ChinaUmsService_toPayAmount chinaUmsService_toPayAmount;

	public ChinaUmsService_toLogin getChinaUmsService_toLogin() {
		return chinaUmsService_toLogin;
	}

	public void setChinaUmsService_toLogin(ChinaUmsService_toLogin chinaUmsService_toLogin) {
		this.chinaUmsService_toLogin = chinaUmsService_toLogin;
	}

	public ChinaUmsService_toLogout getChinaUmsService_toLogout() {
		return chinaUmsService_toLogout;
	}

	public void setChinaUmsService_toLogout(ChinaUmsService_toLogout chinaUmsService_toLogout) {
		this.chinaUmsService_toLogout = chinaUmsService_toLogout;
	}

	public ChinaUmsService_toBackOut getChinaUmsService_toBackOut() {
		return chinaUmsService_toBackOut;
	}

	public void setChinaUmsService_toBackOut(ChinaUmsService_toBackOut chinaUmsService_toBackOut) {
		this.chinaUmsService_toBackOut = chinaUmsService_toBackOut;
	}

	public ChinaUmsService_toCwbSearch getChinaUmsService_toCwbSearch() {
		return chinaUmsService_toCwbSearch;
	}

	public void setChinaUmsService_toCwbSearch(ChinaUmsService_toCwbSearch chinaUmsService_toCwbSearch) {
		this.chinaUmsService_toCwbSearch = chinaUmsService_toCwbSearch;
	}

	public ChinaUmsService_toExptFeedBack getChinaUmsService_toExptFeedBack() {
		return chinaUmsService_toExptFeedBack;
	}

	public void setChinaUmsService_toExptFeedBack(ChinaUmsService_toExptFeedBack chinaUmsService_toExptFeedBack) {
		this.chinaUmsService_toExptFeedBack = chinaUmsService_toExptFeedBack;
	}

	public ChinaUmsService_toPayAmount getChinaUmsService_toPayAmount() {
		return chinaUmsService_toPayAmount;
	}

	public void setChinaUmsService_toPayAmount(ChinaUmsService_toPayAmount chinaUmsService_toPayAmount) {
		this.chinaUmsService_toPayAmount = chinaUmsService_toPayAmount;
	}

}
