package cn.explink.domain.VO.express;

import java.math.BigDecimal;

/**
 * 针对不同的类型运费
 * @author jiangyu 2015年8月12日
 *
 */
public class ExpressBillSummary {
	
	private String countName="";
	
	private Long count=0L;
	
	private String feeName="";
	
	private BigDecimal fee = BigDecimal.ZERO;
	
	public ExpressBillSummary() {
		// TODO Auto-generated constructor stub
	}

	public String getCountName() {
		return countName;
	}

	public void setCountName(String countName) {
		this.countName = countName;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public String getFeeName() {
		return feeName;
	}

	public void setFeeName(String feeName) {
		this.feeName = feeName;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
}
