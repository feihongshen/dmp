package cn.explink.pos.yeepay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class YeePayServiceMaster {

	@Autowired
	private YeePayService_toLogin yeePayService_toLogin;
	@Autowired
	private YeePayService_toCwbSearch yeePayService_toCwbSearch;
	@Autowired
	private YeePayService_toPayAmount yeePayService_toPayAmount;
	@Autowired
	private YeePayService_toSignCwb yeePayService_toSignCwb;
	@Autowired
	private YeePayService_toExptFeedBack yeePayService_toExptFeedBack;
	@Autowired
	private YeePayService_toBackOut yeePayService_toBackOut;

	public YeePayService_toLogin getYeePayService_toLogin() {
		return yeePayService_toLogin;
	}

	public void setYeePayService_toLogin(YeePayService_toLogin yeePayService_toLogin) {
		this.yeePayService_toLogin = yeePayService_toLogin;
	}

	public YeePayService_toCwbSearch getYeePayService_toCwbSearch() {
		return yeePayService_toCwbSearch;
	}

	public void setYeePayService_toCwbSearch(YeePayService_toCwbSearch yeePayService_toCwbSearch) {
		this.yeePayService_toCwbSearch = yeePayService_toCwbSearch;
	}

	public YeePayService_toPayAmount getYeePayService_toPayAmount() {
		return yeePayService_toPayAmount;
	}

	public void setYeePayService_toPayAmount(YeePayService_toPayAmount yeePayService_toPayAmount) {
		this.yeePayService_toPayAmount = yeePayService_toPayAmount;
	}

	public YeePayService_toSignCwb getYeePayService_toSignCwb() {
		return yeePayService_toSignCwb;
	}

	public void setYeePayService_toSignCwb(YeePayService_toSignCwb yeePayService_toSignCwb) {
		this.yeePayService_toSignCwb = yeePayService_toSignCwb;
	}

	public YeePayService_toExptFeedBack getYeePayService_toExptFeedBack() {
		return yeePayService_toExptFeedBack;
	}

	public void setYeePayService_toExptFeedBack(YeePayService_toExptFeedBack yeePayService_toExptFeedBack) {
		this.yeePayService_toExptFeedBack = yeePayService_toExptFeedBack;
	}

	public YeePayService_toBackOut getYeePayService_toBackOut() {
		return yeePayService_toBackOut;
	}

	public void setYeePayService_toBackOut(YeePayService_toBackOut yeePayService_toBackOut) {
		this.yeePayService_toBackOut = yeePayService_toBackOut;
	}

}
