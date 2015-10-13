package cn.explink.b2c.yixun;

import net.sf.json.JSONObject;
import cn.explink.util.MD5.MD5Util;

public class YiXunDTO {

	private String feedback_url; // 访问服务器的地址
	private String apikey;
	private String secret; // 密钥
	private String format;

	private String method;

	private String packageno; // 单号
	private String type; // 问题类型
	private String abnormal_desc;// 问题件描述
	private String createtime; // 登记时间(必须是可转为日期格式的字符串yyyy-MM-dd HH:mm:ss)
	private String desc; // 跟踪描述
	private int status; // 签收状态（1妥投 2拒收）
	private String paysysno; // 支付方式（1现金 2刷卡 3支票）
	private Double amt; // 支付金额（单位分，非货到付款为0）
	private String note; // 支票号或POS机号，没有则为空
	private String signtime; // 签收/拒收时间(必须是可转为日期格式的字符串yyyy-MM-dd HH:mm:ss)

	public boolean init(String joint_property) {
		try {
			JSONObject jsonObj = JSONObject.fromObject(joint_property);
			YiXun yixun = (YiXun) JSONObject.toBean(jsonObj, YiXun.class);
			this.feedback_url = yixun.getFeedback_url();
			this.apikey = yixun.getApikey();
			this.secret = yixun.getSecret();
			this.format = "json";
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 转化成问题件反馈的url
	 * 
	 * @return
	 */
	public String toReportAbnormalUrl() {
		return toReportAbnormalUrl(this.packageno, this.type, this.abnormal_desc, this.createtime, this.method);
	}

	/**
	 * 转化成问题件反馈的url
	 * 
	 * @return
	 */
	// http://180.168.108.42:8061/ApiService.aspx?apikey=d35e2efe5b0e0cc9&createtime=2013-2-21
	// 11:30:01&desc=outscan&format=xml&method=reportscanrecord&packageno=12102602788811&sign=13666c02425ac03d6ddb214441d6c187
	public String toReportAbnormalUrl(String packageno, String type, String desc, String createtime, String method) {
		String sign = MD5Util.MD5Encode(new StringBuffer().append("apikey").append(this.apikey).append("createtime").append(createtime).append("desc").append(desc).append("format")
				.append(this.format).append("methodreportabnormal").append("packageno").append(packageno).append("type").append(type).append(secret).toString(), "UTF-8");
		StringBuffer toYiXun = new StringBuffer();
		toYiXun = toYiXun.append(this.feedback_url).append("?apikey=").append(this.apikey).append("&method=" + method).append("&format=").append(this.format).append("&packageno=").append(packageno)
				.append("&type=").append(type).append("&desc=").append(desc).append("&createtime=").append(createtime).append("&sign=").append(sign);
		return toYiXun.toString();
	}

	/**
	 * 转化成签收状态反馈的url
	 * 
	 * @return
	 */
	public String toReportSignstatusUrl() {
		return toReportSignstatusUrl(this.packageno, this.status, this.paysysno, this.amt, this.note, this.signtime, this.method);
	}

	/**
	 * 转化成签收状态反馈的url
	 * 
	 * @return
	 */
	public String toReportSignstatusUrl(String packageno, int status, String paysysno, Double amt, String note, String signtime, String method) {
		String sign = MD5Util.MD5Encode(new StringBuffer().append("amt").append(amt).append("apikey").append(this.apikey).append("format").append(this.format).append("methodreportsignstatus")
		// .append("note").append(note)

				.append("packageno").append(packageno).append("paysysno").append(paysysno).append("signtime").append(signtime).append("status").append(status).append(secret).toString(), "UTF-8");
		StringBuffer toYiXun = new StringBuffer();
		toYiXun = toYiXun.append(this.feedback_url).append("?apikey=").append(this.apikey).append("&method=" + this.method).append("&format=").append(this.format).append("&packageno=")
				.append(packageno).append("&status=").append(status).append("&paysysno=").append(paysysno).append("&amt=").append(amt)
				// .append("&note=").append(note)
				.append("&signtime=").append(signtime).append("&sign=").append(sign);
		return toYiXun.toString();
	}

	/**
	 * 转化成跟踪信息反馈的url
	 * 
	 * @return
	 */
	public String toReportScanrecordUrl() {
		return toReportScanrecordUrl(this.packageno, this.desc, this.createtime);
	}

	/**
	 * 转化成跟踪信息反馈的url
	 * 
	 * @return
	 */
	// http://180.168.108.42:8061/ApiService.aspx?apikey=d35e2efe5b0e0cc9&createtime=2013-2-21
	// 11:30:01&desc=outscan&format=xml&method=reportscanrecord&packageno=12102602788811&sign=13666c02425ac03d6ddb214441d6c187
	public String toReportScanrecordUrl(String packageno, String desc, String createtime) {

		String sign = MD5Util.MD5Encode(new StringBuffer().append("apikey").append(this.apikey).append("createtime").append(createtime).append("desc").append(desc).append("format")
				.append(this.format).append("methodreportscanrecord").append("packageno").append(packageno).append(secret).toString(), "UTF-8");

		StringBuffer toYiXun = new StringBuffer();
		toYiXun = toYiXun.append(this.feedback_url).append("?apikey=").append(this.apikey).append("&method=reportscanrecord").append("&format=").append(this.format).append("&packageno=")
				.append(packageno).append("&desc=").append(desc).append("&createtime=").append(createtime).append("&sign=").append(sign);
		return toYiXun.toString();
	}

	public String getApikey() {
		return apikey;
	}

	public void setApikey(String apikey) {
		this.apikey = apikey;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getPackageno() {
		return packageno;
	}

	public void setPackageno(String packageno) {
		this.packageno = packageno;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAbnormal_desc() {
		return abnormal_desc;
	}

	public void setAbnormal_desc(String abnormal_desc) {
		this.abnormal_desc = abnormal_desc;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getPaysysno() {
		return paysysno;
	}

	public void setPaysysno(String paysysno) {
		this.paysysno = paysysno;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getSigntime() {
		return signtime;
	}

	public void setSigntime(String signtime) {
		this.signtime = signtime;
	}

	public String getUrl() {
		return feedback_url;
	}

	public void setUrl(String url) {
		this.feedback_url = url;
	}

	public Double getAmt() {
		return amt;
	}

	public void setAmt(Double amt) {
		this.amt = amt;
	}

}
