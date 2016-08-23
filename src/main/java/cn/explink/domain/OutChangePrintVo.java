package cn.explink.domain;

import java.util.List;
import java.util.Map;

public class OutChangePrintVo {
	private Map<Integer, List<String>> cwbmap;
	private int count = 0;// 订单数
	private int sum = 0;// 件数
	private String starttime;
	private String endtime;
	private String userbranchname;
	private String nextbranchname;
	private String username;
	private String printtime;
	public Map<Integer, List<String>> getCwbmap() {
		return cwbmap;
	}
	public void setCwbmap(Map<Integer, List<String>> cwbmap) {
		this.cwbmap = cwbmap;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getSum() {
		return sum;
	}
	public void setSum(int sum) {
		this.sum = sum;
	}
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getUserbranchname() {
		return userbranchname;
	}
	public void setUserbranchname(String userbranchname) {
		this.userbranchname = userbranchname;
	}
	public String getNextbranchname() {
		return nextbranchname;
	}
	public void setNextbranchname(String nextbranchname) {
		this.nextbranchname = nextbranchname;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPrinttime() {
		return printtime;
	}
	public void setPrinttime(String printtime) {
		this.printtime = printtime;
	}
}