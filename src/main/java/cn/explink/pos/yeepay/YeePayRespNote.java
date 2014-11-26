package cn.explink.pos.yeepay;

import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.User;

/**
 * 存储yeepay返回信息的实体
 * 
 * @author Administrator
 *
 */
public class YeePayRespNote {

	private String version; // 版本号
	private String serviceCode; // 业务编码
	private String transactionID; // 交易流水号
	private String srcSysID; // 发起请求方编码
	private String dstSysID; // 应答方编码
	private String reqTime; // 请求时间
	// / private ExtendAtt ExtendAtt; // 扩展属性
	private String hMAC; // 验签
	private String employee_ID; // 收派员工号 username
	private String password; // 密码
	private String employee_Name; // 收派员姓名

	private String company_Code; // 收派员单位编码
	private String company_Tel; // 收派员单位电话
	private String company_Name; // 收派员单位名称
	private String company_Addr; // 收派员单位地址
	private String result_code; // 返回码
	private String result_msg; // 返回码 说明
	private long deliverystate; // 反馈结果 默认0
	private long branchid; // 站点Id
	private String signname;
	private String signtel;
	private String signremark;
	private User user; // 操作员对象

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getSignname() {
		return signname;
	}

	public void setSignname(String signname) {
		this.signname = signname;
	}

	public String getSigntel() {
		return signtel;
	}

	public void setSigntel(String signtel) {
		this.signtel = signtel;
	}

	public String getSignremark() {
		return signremark;
	}

	public void setSignremark(String signremark) {
		this.signremark = signremark;
	}

	public long getDeliverystate() {
		return deliverystate;
	}

	public void setDeliverystate(long deliverystate) {
		this.deliverystate = deliverystate;
	}

	public long getBranchid() {
		return branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public long getDeliverid() {
		return deliverid;
	}

	public void setDeliverid(long deliverid) {
		this.deliverid = deliverid;
	}

	private String Order_No; // 运单号
	private String SignStandard; // 签收标准
	private String Receiver_Name; // 收件人姓名
	private String Receiver_Addr; // 收件人地址
	private String Receiver_Tel; // 收件人电话
	private String Receiver_OrderNo; // 系统中真实的运单号

	private String Order_AMT; // 订单金额（精确到分）
	private String Biz_Name; // 发件商名（例如凡客商城）
	private String AMT; // 金额（精确到分）
	private String Sub_Station_Name; // 所属分站名
	private String Sub_Station_Code; // 所属分编号
	private String Serial_NO; // 接入方流水号

	private String Sorting_Name; // 分拣名称
	private String Weight; // 重量(单位：千克)
	private String Checked_Items; // 托运物品
	private String Biz_Code; // 发件商的商户编号（例如凡客商城的商户编号）,没有则为空

	private String Pa_Details; //
	private String Pc_AutoSplit; //

	private String Order_Status; //
	private String Order_Status_Msg; // 订单状态

	private CwbOrder cwbOrder; // 订单对象
	private DeliveryState deliverstate;
	private long deliverid; // 小件员id
	private String username; // 用户名

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	private String podremark; // 配送结果备注r
	private int system_pay_type; // 系统中的支付类型，对应paytype枚举

	public int getSystem_pay_type() {
		return system_pay_type;
	}

	public void setSystem_pay_type(int system_pay_type) {
		this.system_pay_type = system_pay_type;
	}

	public String getPodremark() {
		return podremark;
	}

	public void setPodremark(String podremark) {
		this.podremark = podremark;
	}

	public DeliveryState getDeliverstate() {
		return deliverstate;
	}

	public void setDeliverstate(DeliveryState deliverstate) {
		this.deliverstate = deliverstate;
	}

	public CwbOrder getCwbOrder() {
		return cwbOrder;
	}

	public void setCwbOrder(CwbOrder cwbOrder) {
		this.cwbOrder = cwbOrder;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

	public String getSrcSysID() {
		return srcSysID;
	}

	public void setSrcSysID(String srcSysID) {
		this.srcSysID = srcSysID;
	}

	public String getDstSysID() {
		return dstSysID;
	}

	public void setDstSysID(String dstSysID) {
		this.dstSysID = dstSysID;
	}

	public String getReqTime() {
		return reqTime;
	}

	public void setReqTime(String reqTime) {
		this.reqTime = reqTime;
	}

	public String gethMAC() {
		return hMAC;
	}

	public void sethMAC(String hMAC) {
		this.hMAC = hMAC;
	}

	public String getEmployee_ID() {
		return employee_ID;
	}

	public void setEmployee_ID(String employee_ID) {
		this.employee_ID = employee_ID;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmployee_Name() {
		return employee_Name;
	}

	public void setEmployee_Name(String employee_Name) {
		this.employee_Name = employee_Name;
	}

	public String getCompany_Code() {
		return company_Code;
	}

	public void setCompany_Code(String company_Code) {
		this.company_Code = company_Code;
	}

	public String getCompany_Tel() {
		return company_Tel;
	}

	public void setCompany_Tel(String company_Tel) {
		this.company_Tel = company_Tel;
	}

	public String getCompany_Name() {
		return company_Name;
	}

	public void setCompany_Name(String company_Name) {
		this.company_Name = company_Name;
	}

	public String getCompany_Addr() {
		return company_Addr;
	}

	public void setCompany_Addr(String company_Addr) {
		this.company_Addr = company_Addr;
	}

	public String getResult_code() {
		return result_code;
	}

	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}

	public String getResult_msg() {
		return result_msg;
	}

	public void setResult_msg(String result_msg) {
		this.result_msg = result_msg;
	}

	public String getOrder_No() {
		return Order_No;
	}

	public void setOrder_No(String order_No) {
		Order_No = order_No;
	}

	public String getSignStandard() {
		return SignStandard;
	}

	public void setSignStandard(String signStandard) {
		SignStandard = signStandard;
	}

	public String getReceiver_Name() {
		return Receiver_Name;
	}

	public void setReceiver_Name(String receiver_Name) {
		Receiver_Name = receiver_Name;
	}

	public String getReceiver_Addr() {
		return Receiver_Addr;
	}

	public void setReceiver_Addr(String receiver_Addr) {
		Receiver_Addr = receiver_Addr;
	}

	public String getReceiver_Tel() {
		return Receiver_Tel;
	}

	public void setReceiver_Tel(String receiver_Tel) {
		Receiver_Tel = receiver_Tel;
	}

	public String getReceiver_OrderNo() {
		return Receiver_OrderNo;
	}

	public void setReceiver_OrderNo(String receiver_OrderNo) {
		Receiver_OrderNo = receiver_OrderNo;
	}

	public String getOrder_AMT() {
		return Order_AMT;
	}

	public void setOrder_AMT(String order_AMT) {
		Order_AMT = order_AMT;
	}

	public String getBiz_Name() {
		return Biz_Name;
	}

	public void setBiz_Name(String biz_Name) {
		Biz_Name = biz_Name;
	}

	public String getAMT() {
		return AMT;
	}

	public void setAMT(String aMT) {
		AMT = aMT;
	}

	public String getSub_Station_Name() {
		return Sub_Station_Name;
	}

	public void setSub_Station_Name(String sub_Station_Name) {
		Sub_Station_Name = sub_Station_Name;
	}

	public String getSub_Station_Code() {
		return Sub_Station_Code;
	}

	public void setSub_Station_Code(String sub_Station_Code) {
		Sub_Station_Code = sub_Station_Code;
	}

	public String getSerial_NO() {
		return Serial_NO;
	}

	public void setSerial_NO(String serial_NO) {
		Serial_NO = serial_NO;
	}

	public String getSorting_Name() {
		return Sorting_Name;
	}

	public void setSorting_Name(String sorting_Name) {
		Sorting_Name = sorting_Name;
	}

	public String getWeight() {
		return Weight;
	}

	public void setWeight(String weight) {
		Weight = weight;
	}

	public String getChecked_Items() {
		return Checked_Items;
	}

	public void setChecked_Items(String checked_Items) {
		Checked_Items = checked_Items;
	}

	public String getBiz_Code() {
		return Biz_Code;
	}

	public void setBiz_Code(String biz_Code) {
		Biz_Code = biz_Code;
	}

	public String getPa_Details() {
		return Pa_Details;
	}

	public void setPa_Details(String pa_Details) {
		Pa_Details = pa_Details;
	}

	public String getPc_AutoSplit() {
		return Pc_AutoSplit;
	}

	public void setPc_AutoSplit(String pc_AutoSplit) {
		Pc_AutoSplit = pc_AutoSplit;
	}

	public String getOrder_Status() {
		return Order_Status;
	}

	public void setOrder_Status(String order_Status) {
		Order_Status = order_Status;
	}

	public String getOrder_Status_Msg() {
		return Order_Status_Msg;
	}

	public void setOrder_Status_Msg(String order_Status_Msg) {
		Order_Status_Msg = order_Status_Msg;
	}

}
