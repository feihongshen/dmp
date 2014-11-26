package cn.explink.domain;

import java.math.BigDecimal;

public class ExceedFee {
	long id;
	BigDecimal exceedfee;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BigDecimal getExceedfee() {
		return exceedfee;
	}

	public void setExceedfee(BigDecimal exceedfee) {
		this.exceedfee = exceedfee;
	}

}
