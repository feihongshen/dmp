package cn.explink.vo.delivery;

public class DeliveryRate {

	private Integer count;

	private String rate;

	private Boolean highlight;

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public Boolean getHighlight() {
		return highlight;
	}

	public void setHighlight(Boolean highlight) {
		this.highlight = highlight;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DeliveryRate [");
		if (count != null)
			builder.append("count=").append(count).append(", ");
		if (rate != null)
			builder.append("rate=").append(rate).append(", ");
		if (highlight != null)
			builder.append("highlight=").append(highlight).append(", ");
		builder.append("]");
		return builder.toString();
	}

}
