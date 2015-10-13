package cn.explink.b2c.wanxiang;

/**
 * 封装万象的json类
 * 
 * @author Administrator 统一使用前缀wx_ ,方便转化时候替换
 */
public class WanxiangJson {

	private String wx_BILLNO;// 运单单号
	private String wx_PACKAGEID; // 封包ID
	private String wx_OPERATEDEP;// 操作部门
	private String wx_OPERATETYPE; // 操作类型
	private String wx_OPERATETIME; // 操作时间
	private String wx_REASON; // 异常原因
	private String wx_REMARK; // 备注
	private String wx_OPERATORNAME;// 派件人
	private String wx_OPERATORTEL; // 派件人电话
	private String wx_CUSTOMER_CODE; // 客户编号
	private String sign; // 加密签名

	public String getWx_BILLNO() {
		return wx_BILLNO;
	}

	public void setWx_BILLNO(String wx_BILLNO) {
		this.wx_BILLNO = wx_BILLNO;
	}

	public String getWx_PACKAGEID() {
		return wx_PACKAGEID;
	}

	public void setWx_PACKAGEID(String wx_PACKAGEID) {
		this.wx_PACKAGEID = wx_PACKAGEID;
	}

	public String getWx_OPERATEDEP() {
		return wx_OPERATEDEP;
	}

	public void setWx_OPERATEDEP(String wx_OPERATEDEP) {
		this.wx_OPERATEDEP = wx_OPERATEDEP;
	}

	public String getWx_OPERATETYPE() {
		return wx_OPERATETYPE;
	}

	public void setWx_OPERATETYPE(String wx_OPERATETYPE) {
		this.wx_OPERATETYPE = wx_OPERATETYPE;
	}

	public String getWx_OPERATETIME() {
		return wx_OPERATETIME;
	}

	public void setWx_OPERATETIME(String wx_OPERATETIME) {
		this.wx_OPERATETIME = wx_OPERATETIME;
	}

	public String getWx_REASON() {
		return wx_REASON;
	}

	public void setWx_REASON(String wx_REASON) {
		this.wx_REASON = wx_REASON;
	}

	public String getWx_REMARK() {
		return wx_REMARK;
	}

	public void setWx_REMARK(String wx_REMARK) {
		this.wx_REMARK = wx_REMARK;
	}

	public String getWx_OPERATORNAME() {
		return wx_OPERATORNAME;
	}

	public void setWx_OPERATORNAME(String wx_OPERATORNAME) {
		this.wx_OPERATORNAME = wx_OPERATORNAME;
	}

	public String getWx_OPERATORTEL() {
		return wx_OPERATORTEL;
	}

	public void setWx_OPERATORTEL(String wx_OPERATORTEL) {
		this.wx_OPERATORTEL = wx_OPERATORTEL;
	}

	public String getWx_CUSTOMER_CODE() {
		return wx_CUSTOMER_CODE;
	}

	public void setWx_CUSTOMER_CODE(String wx_CUSTOMER_CODE) {
		this.wx_CUSTOMER_CODE = wx_CUSTOMER_CODE;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
}
