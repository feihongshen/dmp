package cn.explink.b2c.tmall;

public class Tmall {

	private String partner; // 物流公司的唯一标识
	private String tms_service_code; // 物流公司唯一编号
	private String out_biz_code; // 外部业务编码
	private String service_code; // 服务编码
	private String private_key; // 私钥信息
	private String post_url; // 推送订单状态URL;

	private String getInfo_url; // 自己公司的接口（固定，用来被动接收WLB推送的数据信息）
	private String customerids; // Tmall在系统中的id，如果是多个按逗号隔开
	private int selectMaxCount = 1000; // 查询tmall订单每次查询最大数据
	private String serviceTel; // 客服电话，tmall规定如果发生异常则需要联系物流公司客服

	private int b2c_enum; // 对应的enmu
	private String data_format; // 解析数据的格式 //XML 和JSON
	private int isCallBackAccept; // 是否回传览收状态 0 不回传 1回传 （根据tmall业务需求变动）
	private int acceptflag; // 揽收标准 1 以导入数据作为揽收标准 2以入库作为揽收标准

	// //////////////////
	private int isCallBackError; // 是否回传TMS_ERROR异常信息 传TMS_ERROR异常信息 0回传 1不回传
									// ,默认为旧版回传

	private String expt_url; // 异常回传url 新

	public String getExpt_url() {
		return expt_url;
	}

	public void setExpt_url(String expt_url) {
		this.expt_url = expt_url;
	}

	public int getIsCallBackError() {
		return isCallBackError;
	}

	public void setIsCallBackError(int isCallBackError) {
		this.isCallBackError = isCallBackError;
	}

	public int getAcceptflag() {
		return acceptflag;
	}

	public void setAcceptflag(int acceptflag) {
		this.acceptflag = acceptflag;
	}

	public int getIsCallBackAccept() {
		return isCallBackAccept;
	}

	public void setIsCallBackAccept(int isCallBackAccept) {
		this.isCallBackAccept = isCallBackAccept;
	}

	public String getData_format() {
		return data_format;
	}

	public void setData_format(String data_format) {
		this.data_format = data_format;
	}

	public int getB2c_enum() {
		return b2c_enum;
	}

	public void setB2c_enum(int b2c_enum) {
		this.b2c_enum = b2c_enum;
	}

	public String getServiceTel() {
		return serviceTel;
	}

	public void setServiceTel(String serviceTel) {
		this.serviceTel = serviceTel;
	}

	public void setSelectMaxCount(int selectMaxCount) {
		this.selectMaxCount = selectMaxCount;
	}

	public int getSelectMaxCount() {
		return selectMaxCount;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getTms_service_code() {
		return tms_service_code;
	}

	public void setTms_service_code(String tms_service_code) {
		this.tms_service_code = tms_service_code;
	}

	public String getOut_biz_code() {
		return out_biz_code;
	}

	public void setOut_biz_code(String out_biz_code) {
		this.out_biz_code = out_biz_code;
	}

	public String getService_code() {
		return service_code;
	}

	public void setService_code(String service_code) {
		this.service_code = service_code;
	}

	public String getPrivate_key() {
		return private_key;
	}

	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}

	public String getPost_url() {
		return post_url;
	}

	public void setPost_url(String post_url) {
		this.post_url = post_url;
	}

	public String getGetInfo_url() {
		return getInfo_url;
	}

	public void setGetInfo_url(String getInfo_url) {
		this.getInfo_url = getInfo_url;
	}

	public String getCustomerids() {
		return customerids;
	}

	public void setCustomerids(String customerids) {
		this.customerids = customerids;
	}

}
