package cn.explink.pos.tonglianpos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TlmposServiceMaster {
	@Autowired
	private TlmposService_toLogin tlmposService_toLogin;
	@Autowired
	private TlmposService_toBackOut tlmposService_toBackOut;

	@Autowired
	private TlmposService_toCwbSearch tlmposService_toCwbSearch;
	@Autowired
	private TlmposService_toCwbSign tlmposService_toCwbSign;

	@Autowired
	private TlmposService_toPayAmount tlmposService_toPayAmount;

	@Autowired
	private TlmposService_toExptFeedBack tlmposService_toExptFeedBack;
	@Autowired
	private TlmposService_toSmt tlmposService_toSmt;

	public TlmposService_toSmt getTlmposService_toSmt() {
		return this.tlmposService_toSmt;
	}

	public void setTlmposService_toSmt(TlmposService_toSmt tlmposService_toSmt) {
		this.tlmposService_toSmt = tlmposService_toSmt;
	}

	public TlmposService_toLogin getTlmposService_toLogin() {
		return this.tlmposService_toLogin;
	}

	public void setTlmposService_toLogin(TlmposService_toLogin tlmposService_toLogin) {
		this.tlmposService_toLogin = tlmposService_toLogin;
	}

	public TlmposService_toBackOut getTlmposService_toBackOut() {
		return this.tlmposService_toBackOut;
	}

	public void setTlmposService_toBackOut(TlmposService_toBackOut tlmposService_toBackOut) {
		this.tlmposService_toBackOut = tlmposService_toBackOut;
	}

	public TlmposService_toCwbSearch getTlmposService_toCwbSearch() {
		return this.tlmposService_toCwbSearch;
	}

	public void setTlmposService_toCwbSearch(TlmposService_toCwbSearch tlmposService_toCwbSearch) {
		this.tlmposService_toCwbSearch = tlmposService_toCwbSearch;
	}

	public TlmposService_toCwbSign getTlmposService_toCwbSign() {
		return this.tlmposService_toCwbSign;
	}

	public void setTlmposService_toCwbSign(TlmposService_toCwbSign tlmposService_toCwbSign) {
		this.tlmposService_toCwbSign = tlmposService_toCwbSign;
	}

	public TlmposService_toPayAmount getTlmposService_toPayAmount() {
		return this.tlmposService_toPayAmount;
	}

	public void setTlmposService_toPayAmount(TlmposService_toPayAmount tlmposService_toPayAmount) {
		this.tlmposService_toPayAmount = tlmposService_toPayAmount;
	}

	public TlmposService_toExptFeedBack getTlmposService_toExptFeedBack() {
		return this.tlmposService_toExptFeedBack;
	}

	public void setTlmposService_toExptFeedBack(TlmposService_toExptFeedBack tlmposService_toExptFeedBack) {
		this.tlmposService_toExptFeedBack = tlmposService_toExptFeedBack;
	}

}
