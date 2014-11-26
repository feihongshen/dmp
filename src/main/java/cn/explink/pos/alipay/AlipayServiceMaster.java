package cn.explink.pos.alipay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlipayServiceMaster {
	@Autowired
	private AlipayService_toLogin alipayService_toLogin;
	@Autowired
	private AlipayService_toBackOut alipayService_toBackOut;
	@Autowired
	private AlipayService_toBackOutFinish alipayService_toBackOutFinish;
	@Autowired
	private AlipayService_toCwbSearch alipayService_toCwbSearch;
	@Autowired
	private AlipayService_toCwbSign alipayService_toCwbSign;
	@Autowired
	private AlipayService_toExptFeedBack alipayService_toExptFeedBack;
	@Autowired
	private AlipayService_toPayAmount alipayService_toPayAmount;
	@Autowired
	private AlipayService_toPayFinish alipayService_toPayFinish;
	@Autowired
	private AlipayService_toSearchExptCode alipayService_toSearchExptCode;

	public AlipayService_toBackOut getAlipayService_toBackOut() {
		return alipayService_toBackOut;
	}

	public void setAlipayService_toBackOut(AlipayService_toBackOut alipayService_toBackOut) {
		this.alipayService_toBackOut = alipayService_toBackOut;
	}

	public AlipayService_toBackOutFinish getAlipayService_toBackOutFinish() {
		return alipayService_toBackOutFinish;
	}

	public void setAlipayService_toBackOutFinish(AlipayService_toBackOutFinish alipayService_toBackOutFinish) {
		this.alipayService_toBackOutFinish = alipayService_toBackOutFinish;
	}

	public AlipayService_toCwbSearch getAlipayService_toCwbSearch() {
		return alipayService_toCwbSearch;
	}

	public void setAlipayService_toCwbSearch(AlipayService_toCwbSearch alipayService_toCwbSearch) {
		this.alipayService_toCwbSearch = alipayService_toCwbSearch;
	}

	public AlipayService_toCwbSign getAlipayService_toCwbSign() {
		return alipayService_toCwbSign;
	}

	public void setAlipayService_toCwbSign(AlipayService_toCwbSign alipayService_toCwbSign) {
		this.alipayService_toCwbSign = alipayService_toCwbSign;
	}

	public AlipayService_toExptFeedBack getAlipayService_toExptFeedBack() {
		return alipayService_toExptFeedBack;
	}

	public void setAlipayService_toExptFeedBack(AlipayService_toExptFeedBack alipayService_toExptFeedBack) {
		this.alipayService_toExptFeedBack = alipayService_toExptFeedBack;
	}

	public AlipayService_toPayAmount getAlipayService_toPayAmount() {
		return alipayService_toPayAmount;
	}

	public void setAlipayService_toPayAmount(AlipayService_toPayAmount alipayService_toPayAmount) {
		this.alipayService_toPayAmount = alipayService_toPayAmount;
	}

	public AlipayService_toPayFinish getAlipayService_toPayFinish() {
		return alipayService_toPayFinish;
	}

	public void setAlipayService_toPayFinish(AlipayService_toPayFinish alipayService_toPayFinish) {
		this.alipayService_toPayFinish = alipayService_toPayFinish;
	}

	public AlipayService_toSearchExptCode getAlipayService_toSearchExptCode() {
		return alipayService_toSearchExptCode;
	}

	public void setAlipayService_toSearchExptCode(AlipayService_toSearchExptCode alipayService_toSearchExptCode) {
		this.alipayService_toSearchExptCode = alipayService_toSearchExptCode;
	}

	public AlipayService_toLogin getAlipayService_toLogin() {
		return alipayService_toLogin;
	}

	public void setAlipayService_toLogin(AlipayService_toLogin alipayService_toLogin) {
		this.alipayService_toLogin = alipayService_toLogin;
	}
}
