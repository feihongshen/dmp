package cn.explink.test;

import java.math.BigDecimal;

/**
 * 属性
 * @author Administrator
 *
 */
public class TypeVo {

	private long num; //个数
	private String cwb; //订单号，查明细的时候用到
	public String getCwb() {
		return cwb;
	}
	public void setCwb(String cwb) {
		this.cwb = cwb;
	}
	private double amount; //金额
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public long getNum() {
		return num;
	}
	public void setNum(long num) {
		this.num = num;
	}
	public TypeVo(long num,double amount){
		this.num=num;
		this.amount=amount;
	}
	public TypeVo(){
	}
	
}
