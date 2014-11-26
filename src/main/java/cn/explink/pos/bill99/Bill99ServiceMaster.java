package cn.explink.pos.bill99;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Bill99ServiceMaster {
	@Autowired
	private Bill99Service_toLogin bill99Service_toLogin;
	@Autowired
	private Bill99Service_toCwbSign bill99Service_toCwbSign;
	@Autowired
	private Bill99Service_toBackOut bill99Service_toBackOut;
	@Autowired
	private Bill99Service_toCwbSearch bill99Service_toCwbSearch;
	@Autowired
	private Bill99Service_toExptFeedBack bill99Service_toExptFeedBack;
	@Autowired
	private Bill99Service_toPayAmount bill99Service_toPayAmount;
	@Autowired
	private Bill99Service_toReverseDept bill99Service_toReverseDept;

	public Bill99Service_toLogin getBill99Service_toLogin() {
		return bill99Service_toLogin;
	}

	public void setBill99Service_toLogin(Bill99Service_toLogin bill99Service_toLogin) {
		this.bill99Service_toLogin = bill99Service_toLogin;
	}

	public Bill99Service_toCwbSign getBill99Service_toCwbSign() {
		return bill99Service_toCwbSign;
	}

	public void setBill99Service_toCwbSign(Bill99Service_toCwbSign bill99Service_toCwbSign) {
		this.bill99Service_toCwbSign = bill99Service_toCwbSign;
	}

	public Bill99Service_toBackOut getBill99Service_toBackOut() {
		return bill99Service_toBackOut;
	}

	public void setBill99Service_toBackOut(Bill99Service_toBackOut bill99Service_toBackOut) {
		this.bill99Service_toBackOut = bill99Service_toBackOut;
	}

	public Bill99Service_toCwbSearch getBill99Service_toCwbSearch() {
		return bill99Service_toCwbSearch;
	}

	public void setBill99Service_toCwbSearch(Bill99Service_toCwbSearch bill99Service_toCwbSearch) {
		this.bill99Service_toCwbSearch = bill99Service_toCwbSearch;
	}

	public Bill99Service_toExptFeedBack getBill99Service_toExptFeedBack() {
		return bill99Service_toExptFeedBack;
	}

	public void setBill99Service_toExptFeedBack(Bill99Service_toExptFeedBack bill99Service_toExptFeedBack) {
		this.bill99Service_toExptFeedBack = bill99Service_toExptFeedBack;
	}

	public Bill99Service_toPayAmount getBill99Service_toPayAmount() {
		return bill99Service_toPayAmount;
	}

	public void setBill99Service_toPayAmount(Bill99Service_toPayAmount bill99Service_toPayAmount) {
		this.bill99Service_toPayAmount = bill99Service_toPayAmount;
	}

	public Bill99Service_toReverseDept getBill99Service_toReverseDept() {
		return bill99Service_toReverseDept;
	}

	public void setBill99Service_toReverseDept(Bill99Service_toReverseDept bill99Service_toReverseDept) {
		this.bill99Service_toReverseDept = bill99Service_toReverseDept;
	}

}
