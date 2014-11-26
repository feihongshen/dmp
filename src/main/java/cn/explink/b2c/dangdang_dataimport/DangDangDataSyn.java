package cn.explink.b2c.dangdang_dataimport;

public class DangDangDataSyn {

	private String express_id; // 快递公司的唯一标识

	private String private_key; // 加密密钥
	private String own_url; // 当当请求 我方的URL
	private String own_url_test; // 测试url
	private String customerid_tushu; // 当当 图书 -自营
	private String customerid_baihuo; // 当当 百货和天津百货-合并(用发货仓库区分) -自营
	private String customerid_dangrida; // 当当 自营 -当日达

	private String customerid_zhaoshang; // 当当 招商-外部商家

	private String charcode; // 当当 自营 -当日达
	private long drd_starttime; // 当日达开始时间
	private long drd_endtime; // 当日达结束时间
	private int isopen_drdflag; // 是否开启当当当日递的标识,如果开启，则按照时间段卡。

	private long ruleEmaildateHours; // 按照时间点作为批次生成时间

	public int getIsopen_drdflag() {
		return isopen_drdflag;
	}

	public long getRuleEmaildateHours() {
		return ruleEmaildateHours;
	}

	public void setRuleEmaildateHours(long ruleEmaildateHours) {
		this.ruleEmaildateHours = ruleEmaildateHours;
	}

	public void setIsopen_drdflag(int isopen_drdflag) {
		this.isopen_drdflag = isopen_drdflag;
	}

	public String getOwn_url_test() {
		return own_url_test;
	}

	public long getDrd_starttime() {
		return drd_starttime;
	}

	public void setDrd_starttime(long drd_starttime) {
		this.drd_starttime = drd_starttime;
	}

	public long getDrd_endtime() {
		return drd_endtime;
	}

	public void setDrd_endtime(long drd_endtime) {
		this.drd_endtime = drd_endtime;
	}

	public String getCharcode() {
		return charcode;
	}

	public void setCharcode(String charcode) {
		this.charcode = charcode;
	}

	public void setOwn_url_test(String own_url_test) {
		this.own_url_test = own_url_test;
	}

	private long branchid; // 订单导入库房id

	public long getBranchid() {
		return branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public String getCustomerid_baihuo() {
		return customerid_baihuo;
	}

	public void setCustomerid_baihuo(String customerid_baihuo) {
		this.customerid_baihuo = customerid_baihuo;
	}

	public String getExpress_id() {
		return express_id;
	}

	public void setExpress_id(String express_id) {
		this.express_id = express_id;
	}

	public String getPrivate_key() {
		return private_key;
	}

	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}

	public String getOwn_url() {
		return own_url;
	}

	public void setOwn_url(String own_url) {
		this.own_url = own_url;
	}

	public String getCustomerid_tushu() {
		return customerid_tushu;
	}

	public void setCustomerid_tushu(String customerid_tushu) {
		this.customerid_tushu = customerid_tushu;
	}

	public String getCustomerid_dangrida() {
		return customerid_dangrida;
	}

	public void setCustomerid_dangrida(String customerid_dangrida) {
		this.customerid_dangrida = customerid_dangrida;
	}

	public String getCustomerid_zhaoshang() {
		return customerid_zhaoshang;
	}

	public void setCustomerid_zhaoshang(String customerid_zhaoshang) {
		this.customerid_zhaoshang = customerid_zhaoshang;
	}

}
