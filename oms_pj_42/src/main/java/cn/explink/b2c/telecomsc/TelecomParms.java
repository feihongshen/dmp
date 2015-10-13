package cn.explink.b2c.telecomsc;

/**
 * 存储电信商城 状态回传 接口的参数信息
 * 
 * @author Administrator
 *
 */
public class TelecomParms {

	private String outtransno; // 外部交易号
	private String distribution_id; // 配送单编号 对应cwb
	private String order_Id; // 订单号 transcwb
	private String distribution_status;// 物流状态
	private String distribution_desc;// 物流描述
	private String stranded_reason; // 滞留原因
	private String stranded_num; // 滞留次数
	private String return_reason; // 退货原因
	private String couriers_name;// 投递员名称

	private String couriers_phone;// 投递员电话
	private String receivables_info; // 收款信息 支付方式 字典
	private String current_time; // 物流发生时间
	private String idcard_pic_address; // 照片地址
	private String sign; // 签名
	private String signtype; // 签名方式

	public String getOuttransno() {
		return outtransno;
	}

	public void setOuttransno(String outtransno) {
		this.outtransno = outtransno;
	}

	public String getDistribution_id() {
		return distribution_id;
	}

	public void setDistribution_id(String distribution_id) {
		this.distribution_id = distribution_id;
	}

	public String getOrder_Id() {
		return order_Id;
	}

	public void setOrder_Id(String order_Id) {
		this.order_Id = order_Id;
	}

	public String getDistribution_status() {
		return distribution_status;
	}

	public void setDistribution_status(String distribution_status) {
		this.distribution_status = distribution_status;
	}

	public String getDistribution_desc() {
		return distribution_desc;
	}

	public void setDistribution_desc(String distribution_desc) {
		this.distribution_desc = distribution_desc;
	}

	public String getStranded_reason() {
		return stranded_reason;
	}

	public void setStranded_reason(String stranded_reason) {
		this.stranded_reason = stranded_reason;
	}

	public String getStranded_num() {
		return stranded_num;
	}

	public void setStranded_num(String stranded_num) {
		this.stranded_num = stranded_num;
	}

	public String getReturn_reason() {
		return return_reason;
	}

	public void setReturn_reason(String return_reason) {
		this.return_reason = return_reason;
	}

	public String getCouriers_name() {
		return couriers_name;
	}

	public void setCouriers_name(String couriers_name) {
		this.couriers_name = couriers_name;
	}

	public String getCouriers_phone() {
		return couriers_phone;
	}

	public void setCouriers_phone(String couriers_phone) {
		this.couriers_phone = couriers_phone;
	}

	public String getReceivables_info() {
		return receivables_info;
	}

	public void setReceivables_info(String receivables_info) {
		this.receivables_info = receivables_info;
	}

	public String getCurrent_time() {
		return current_time;
	}

	public void setCurrent_time(String current_time) {
		this.current_time = current_time;
	}

	public String getIdcard_pic_address() {
		return idcard_pic_address;
	}

	public void setIdcard_pic_address(String idcard_pic_address) {
		this.idcard_pic_address = idcard_pic_address;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSigntype() {
		return signtype;
	}

	public void setSigntype(String signtype) {
		this.signtype = signtype;
	}

	// public static void main(String[] args) {
	// Scanner input = new Scanner(System.in);
	// while(true){
	// System.out.print("请输入：");
	// String val = input.next();
	// System.out.println("输出="+val.toLowerCase());
	// if(val.equals("return")){
	// System.out.println("您已退出!");
	// return;
	// }
	// }
	//
	//
	// }

}
