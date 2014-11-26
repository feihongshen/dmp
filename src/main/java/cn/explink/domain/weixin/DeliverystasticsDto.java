package cn.explink.domain.weixin;

/**
 * 微信查询 派件量统计 1,站点 2,员工
 * 
 * @author Administrator
 *
 */
public class DeliverystasticsDto {

	private int id;
	private int type; // 1.站点 2.派送员
	private long branchid;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	private String branchname;
	private long userid;
	private String realname;
	private String searchdate;// 查询日期，精确到天

	private long today_linghuo;

	public String getSearchdate() {
		return searchdate;
	}

	public void setSearchdate(String searchdate) {
		this.searchdate = searchdate;
	}

	private long today_chenggong;
	private long today_zhiliu;
	private long today_jushou;
	private long today_bufenjushou;
	private long today_diushi;

	public long getToday_diushi() {
		return today_diushi;
	}

	public void setToday_diushi(long today_diushi) {
		this.today_diushi = today_diushi;
	}

	private String userDeliveryList;

	public String getUserDeliveryList() {
		return userDeliveryList;
	}

	public void setUserDeliveryList(String userDeliveryList) {
		this.userDeliveryList = userDeliveryList;
	}

	public long getToday_bufenjushou() {
		return today_bufenjushou;
	}

	public void setToday_bufenjushou(long today_bufenjushou) {
		this.today_bufenjushou = today_bufenjushou;
	}

	private long today_zhongzhuan;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getBranchid() {
		return branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public String getBranchname() {
		return branchname;
	}

	public void setBranchname(String branchname) {
		this.branchname = branchname;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public long getToday_linghuo() {
		return today_linghuo;
	}

	public void setToday_linghuo(long today_linghuo) {
		this.today_linghuo = today_linghuo;
	}

	public long getToday_chenggong() {
		return today_chenggong;
	}

	public void setToday_chenggong(long today_chenggong) {
		this.today_chenggong = today_chenggong;
	}

	public long getToday_zhiliu() {
		return today_zhiliu;
	}

	public void setToday_zhiliu(long today_zhiliu) {
		this.today_zhiliu = today_zhiliu;
	}

	public long getToday_jushou() {
		return today_jushou;
	}

	public void setToday_jushou(long today_jushou) {
		this.today_jushou = today_jushou;
	}

	public long getToday_zhongzhuan() {
		return today_zhongzhuan;
	}

	public void setToday_zhongzhuan(long today_zhongzhuan) {
		this.today_zhongzhuan = today_zhongzhuan;
	}

}
