package cn.explink.b2c.explink.xmldto;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @ClassName: YiGuoShengXianOrder
 * @Description:
 * @author 王强
 * @date 2015年11月24日 上午11:24:41
 *
 */
@XmlRootElement(name = "Order")
public class OrderEntity{
	private String cwb;
	private String transcwb;
	private String consigneename;// 收件人
	private String cwbprovince;//省
	private String cwbcity;//市
	private String cwbcounty;//县
	private String consigneeaddress;// 收件人地址
	private String consigneepostcode;// 收件人邮编
	private String consigneephone;// 收件人电话
	private String consigneemobile;// 收件人手机
	private String sendcargoname;// 发货商品名称
	private String backcargoname;// 退回商品名称
	private BigDecimal receivablefee;// 应收金额
	private BigDecimal paybackfee;// 应退金额
	private BigDecimal cargorealweight;// 货物重量
	private BigDecimal cargoamount;// 货物金额
	private String cargotype;// 货物类型
	private String cargosize;// 货物尺寸
	private int sendcargonum;// 发货数量
	private int backcargonum;// 取回数量
	private int cwbordertypeid;// 订单类型
	private int cwbdelivertypeid;// 配送方式
	private String warehousename;// 发货库房
	private String sendtime;// 库房出库时间
	private String outtobranch;// 出库站点
	private String customercommand;// 客户要求
	private int paywayid;// 指定支付方式
	private String remark1;
	private String remark2;
	private String remark3;
	private String remark4;
	private String remark5;

	@XmlElement(name = "cwb")
	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	@XmlElement(name = "transcwb")
	public String getTranscwb() {
		return transcwb;
	}

	public void setTranscwb(String transcwb) {
		this.transcwb = transcwb;
	}

	@XmlElement(name = "consigneename")
	public String getConsigneename() {
		return consigneename;
	}

	public void setConsigneename(String consigneename) {
		this.consigneename = consigneename;
	}

	@XmlElement(name = "cwbprovince")
	public String getCwbprovince() {
		return cwbprovince;
	}
	
	public void setCwbprovince(String cwbprovince) {
		this.cwbprovince = cwbprovince;
	}
	
	@XmlElement(name = "cwbcity")
	public String getCwbcity() {
		return cwbcity;
	}
	
	public void setCwbcity(String cwbcity) {
		this.cwbcity = cwbcity;
	}
	
	@XmlElement(name = "cwbcounty")
	public String getCwbcounty() {
		return cwbcounty;
	}
	
	public void setCwbcounty(String cwbcounty) {
		this.cwbcounty = cwbcounty;
	}
	

	@XmlElement(name = "consigneeaddress")
	public String getConsigneeaddress() {
		return consigneeaddress;
	}

	public void setConsigneeaddress(String consigneeaddress) {
		this.consigneeaddress = consigneeaddress;
	}

	@XmlElement(name = "consigneepostcode")
	public String getConsigneepostcode() {
		return consigneepostcode;
	}

	public void setConsigneepostcode(String consigneepostcode) {
		this.consigneepostcode = consigneepostcode;
	}

	@XmlElement(name = "consigneephone")
	public String getConsigneephone() {
		return consigneephone;
	}

	public void setConsigneephone(String consigneephone) {
		this.consigneephone = consigneephone;
	}

	@XmlElement(name = "consigneemobile")
	public String getConsigneemobile() {
		return consigneemobile;
	}

	public void setConsigneemobile(String consigneemobile) {
		this.consigneemobile = consigneemobile;
	}

	@XmlElement(name = "sendcargoname")
	public String getSendcargoname() {
		return sendcargoname;
	}

	public void setSendcargoname(String sendcargoname) {
		this.sendcargoname = sendcargoname;
	}

	@XmlElement(name = "backcargoname")
	public String getBackcargoname() {
		return backcargoname;
	}

	public void setBackcargoname(String backcargoname) {
		this.backcargoname = backcargoname;
	}

	@XmlElement(name = "receivablefee")
	public BigDecimal getReceivablefee() {
		return receivablefee;
	}

	public void setReceivablefee(BigDecimal receivablefee) {
		this.receivablefee = receivablefee;
	}

	@XmlElement(name = "paybackfee")
	public BigDecimal getPaybackfee() {
		return paybackfee;
	}

	public void setPaybackfee(BigDecimal paybackfee) {
		this.paybackfee = paybackfee;
	}

	@XmlElement(name = "cargorealweight")
	public BigDecimal getCargorealweight() {
		return cargorealweight;
	}

	public void setCargorealweight(BigDecimal cargorealweight) {
		this.cargorealweight = cargorealweight;
	}

	@XmlElement(name = "cargoamount")
	public BigDecimal getCargoamount() {
		return cargoamount;
	}

	public void setCargoamount(BigDecimal cargoamount) {
		this.cargoamount = cargoamount;
	}

	@XmlElement(name = "cargotype")
	public String getCargotype() {
		return cargotype;
	}

	public void setCargotype(String cargotype) {
		this.cargotype = cargotype;
	}

	@XmlElement(name = "cargosize")
	public String getCargosize() {
		return cargosize;
	}

	public void setCargosize(String cargosize) {
		this.cargosize = cargosize;
	}

	@XmlElement(name = "sendcargonum")
	public int getSendcargonum() {
		return sendcargonum;
	}

	public void setSendcargonum(int sendcargonum) {
		this.sendcargonum = sendcargonum;
	}

	@XmlElement(name = "backcargonum")
	public int getBackcargonum() {
		return backcargonum;
	}

	public void setBackcargonum(int backcargonum) {
		this.backcargonum = backcargonum;
	}

	@XmlElement(name = "cwbordertypeid")
	public int getCwbordertypeid() {
		return cwbordertypeid;
	}

	public void setCwbordertypeid(int cwbordertypeid) {
		this.cwbordertypeid = cwbordertypeid;
	}

	@XmlElement(name = "cwbdelivertypeid")
	public int getCwbdelivertypeid() {
		return cwbdelivertypeid;
	}

	public void setCwbdelivertypeid(int cwbdelivertypeid) {
		this.cwbdelivertypeid = cwbdelivertypeid;
	}

	@XmlElement(name = "warehousename")
	public String getWarehousename() {
		return warehousename;
	}

	public void setWarehousename(String warehousename) {
		this.warehousename = warehousename;
	}

	@XmlElement(name = "sendtime")
	public String getSendtime() {
		return sendtime;
	}

	public void setSendtime(String sendtime) {
		this.sendtime = sendtime;
	}

	@XmlElement(name = "outtobranch")
	public String getOuttobranch() {
		return outtobranch;
	}

	public void setOuttobranch(String outtobranch) {
		this.outtobranch = outtobranch;
	}

	@XmlElement(name = "customercommand")
	public String getCustomercommand() {
		return customercommand;
	}

	public void setCustomercommand(String customercommand) {
		this.customercommand = customercommand;
	}

	@XmlElement(name = "paywayid")
	public int getPaywayid() {
		return paywayid;
	}

	public void setPaywayid(int paywayid) {
		this.paywayid = paywayid;
	}

	@XmlElement(name = "remark1")
	public String getRemark1() {
		return remark1;
	}

	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}

	@XmlElement(name = "remark2")
	public String getRemark2() {
		return remark2;
	}

	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}

	@XmlElement(name = "remark3")
	public String getRemark3() {
		return remark3;
	}

	public void setRemark3(String remark3) {
		this.remark3 = remark3;
	}

	@XmlElement(name = "remark4")
	public String getRemark4() {
		return remark4;
	}

	public void setRemark4(String remark4) {
		this.remark4 = remark4;
	}

	@XmlElement(name = "remark5")
	public String getRemark5() {
		return remark5;
	}

	public void setRemark5(String remark5) {
		this.remark5 = remark5;
	}

}
