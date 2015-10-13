package cn.explink.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderLifeCycleReportVO {
	
	private long id;
	
	private long customerid;
	
	private String customername;
	
	private BigDecimal amount;
	
	private Integer count;

	private int typeid;
	
	private int reportdate;
	
	private BigDecimal amount1;
	
	private Integer count1;

	private BigDecimal amount2;
	
	private Integer count2;

	private BigDecimal amount3;
	
	private Integer count3;

	private BigDecimal amount4;
	
	private Integer count4;

	private BigDecimal amount5;
	
	private Integer count5;

	private BigDecimal amount6;
	
	private Integer count6;

	private BigDecimal amount7;
	
	private Integer count7;

	private BigDecimal amount8;
	
	private Integer count8;

	private BigDecimal amount9;
	
	private Integer count9;

	private BigDecimal amount10;
	
	private Integer count10;

	private BigDecimal amount11;
	
	private Integer count11;

	private BigDecimal amount12;
	
	private Integer count12;

	private BigDecimal amount13;
	
	private Integer count13;

	private BigDecimal amount14;
	
	private Integer count14;
	
	/**
	 * 保存关联的订单号
	 */
	private List<String> orderSnapshotIdList = new ArrayList<String>();
	
	
	public void addOrderSnapshotId(String id){
		this.orderSnapshotIdList.add(id);
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public long getCustomerid() {
		return customerid;
	}


	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}


	public String getCustomername() {
		return customername;
	}


	public void setCustomername(String customername) {
		this.customername = customername;
	}


	public BigDecimal getAmount() {
		return amount;
	}


	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}


	public Integer getCount() {
		return count;
	}


	public void setCount(Integer count) {
		this.count = count;
	}


	public int getTypeid() {
		return typeid;
	}


	public void setTypeid(int typeid) {
		this.typeid = typeid;
	}


	public BigDecimal getAmount1() {
		return amount1;
	}


	public void setAmount1(BigDecimal amount1) {
		this.amount1 = amount1;
	}


	public Integer getCount1() {
		return count1;
	}


	public void setCount1(Integer count1) {
		this.count1 = count1;
	}


	public BigDecimal getAmount2() {
		return amount2;
	}


	public void setAmount2(BigDecimal amount2) {
		this.amount2 = amount2;
	}


	public Integer getCount2() {
		return count2;
	}


	public void setCount2(Integer count2) {
		this.count2 = count2;
	}


	public BigDecimal getAmount3() {
		return amount3;
	}


	public void setAmount3(BigDecimal amount3) {
		this.amount3 = amount3;
	}


	public Integer getCount3() {
		return count3;
	}


	public void setCount3(Integer count3) {
		this.count3 = count3;
	}


	public BigDecimal getAmount4() {
		return amount4;
	}


	public void setAmount4(BigDecimal amount4) {
		this.amount4 = amount4;
	}


	public Integer getCount4() {
		return count4;
	}


	public void setCount4(Integer count4) {
		this.count4 = count4;
	}


	public BigDecimal getAmount5() {
		return amount5;
	}


	public void setAmount5(BigDecimal amount5) {
		this.amount5 = amount5;
	}


	public Integer getCount5() {
		return count5;
	}


	public void setCount5(Integer count5) {
		this.count5 = count5;
	}


	public BigDecimal getAmount6() {
		return amount6;
	}


	public void setAmount6(BigDecimal amount6) {
		this.amount6 = amount6;
	}


	public Integer getCount6() {
		return count6;
	}


	public void setCount6(Integer count6) {
		this.count6 = count6;
	}


	public BigDecimal getAmount7() {
		return amount7;
	}


	public void setAmount7(BigDecimal amount7) {
		this.amount7 = amount7;
	}


	public Integer getCount7() {
		return count7;
	}


	public void setCount7(Integer count7) {
		this.count7 = count7;
	}


	public BigDecimal getAmount8() {
		return amount8;
	}


	public void setAmount8(BigDecimal amount8) {
		this.amount8 = amount8;
	}


	public Integer getCount8() {
		return count8;
	}


	public void setCount8(Integer count8) {
		this.count8 = count8;
	}


	public BigDecimal getAmount9() {
		return amount9;
	}


	public void setAmount9(BigDecimal amount9) {
		this.amount9 = amount9;
	}


	public Integer getCount9() {
		return count9;
	}


	public void setCount9(Integer count9) {
		this.count9 = count9;
	}


	public BigDecimal getAmount10() {
		return amount10;
	}


	public void setAmount10(BigDecimal amount10) {
		this.amount10 = amount10;
	}


	public Integer getCount10() {
		return count10;
	}


	public void setCount10(Integer count10) {
		this.count10 = count10;
	}


	public BigDecimal getAmount11() {
		return amount11;
	}


	public void setAmount11(BigDecimal amount11) {
		this.amount11 = amount11;
	}


	public Integer getCount11() {
		return count11;
	}


	public void setCount11(Integer count11) {
		this.count11 = count11;
	}


	public BigDecimal getAmount12() {
		return amount12;
	}


	public void setAmount12(BigDecimal amount12) {
		this.amount12 = amount12;
	}


	public Integer getCount12() {
		return count12;
	}


	public void setCount12(Integer count12) {
		this.count12 = count12;
	}


	public BigDecimal getAmount13() {
		return amount13;
	}


	public void setAmount13(BigDecimal amount13) {
		this.amount13 = amount13;
	}


	public Integer getCount13() {
		return count13;
	}


	public void setCount13(Integer count13) {
		this.count13 = count13;
	}


	public BigDecimal getAmount14() {
		return amount14;
	}


	public void setAmount14(BigDecimal amount14) {
		this.amount14 = amount14;
	}


	public Integer getCount14() {
		return count14;
	}


	public void setCount14(Integer count14) {
		this.count14 = count14;
	}


	public int getReportdate() {
		return reportdate;
	}


	public void setReportdate(int reportdate) {
		this.reportdate = reportdate;
	}


	public List<String> getOrderSnapshotIdList() {
		return orderSnapshotIdList;
	}


	public void setOrderSnapshotIdList(List<String> orderSnapshotIdList) {
		this.orderSnapshotIdList = orderSnapshotIdList;
	}
	
	
	
}
