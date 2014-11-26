package cn.explink.domain;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import cn.explink.enumutil.FlowOrderTypeEnum;

@XmlRootElement
public class CwbKuaiDiView {

	// 中转站 站名 收件人公司
	private String cwb; // 快递单号
	private long flowordertype; // 操作类型，具体对应枚举类FlowOrderTypeEnum
	private String consigneeno = "";// 收件人编号
	private String consigneename = "";// 收件人名称
	private String consigneeaddress = "";// 收件人地址
	private String consigneepostcode = "";// 收件人邮编
	private String consigneephone = "";// 收件人电话
	private String consigneemobile = "";// 收件人手机
	private long lanshoubranchid; // 揽收机构id
	private String lanshoubranchname; // 揽收机构
	private long lanshouuserid; // 揽收人id
	private String lanshouusername; // 揽收人
	private long paisongbranchid; // 派送机构id
	private long paisonguserid; // 派送人id
	private long zhongzhuanbranchid; // 中转站id
	private String paisongtime;// 派送时间
	private String lanshoutime;// 揽收时间
	private String sendconsigneename;// 寄件人
	private String sendconsigneecompany;// 寄件人公司
	private String sendconsigneeareacode;// 寄件人区号
	private String sendconsigneecity;// 寄件人城市
	private String sendconsigneeaddress;// 寄件人地址
	private String sendconsigneemobile;// 寄件人手机
	private String sendconsigneephone;// 寄件人座机
	private String sendconsigneepostcode;// 寄件人邮编
	private long paytype;// 付款方式
	private String consigneeareacode;// 收件人区号
	private BigDecimal packingfee = BigDecimal.ZERO; // 包装费
	private BigDecimal insuredrate; // 保价率
	private BigDecimal safefee = BigDecimal.ZERO; // 保险费
	private BigDecimal transitfee = BigDecimal.ZERO; // 运费
	private BigDecimal weightfee = BigDecimal.ZERO; // 计费重量
	private BigDecimal realweight = BigDecimal.ZERO; // 实际重量
	private BigDecimal nowfee = BigDecimal.ZERO; // 现付款项
	private BigDecimal futurefee = BigDecimal.ZERO; // 到付款项
	private BigDecimal packagefee = BigDecimal.ZERO; // 封装费
	private BigDecimal otherfee = BigDecimal.ZERO; // 其他费用
	private BigDecimal allfee = BigDecimal.ZERO; // 费用总计
	private String remark;// 备注
	private long signstate;// 签收状态
	private String signman;// 签收人
	private String signtime;// 签收时间
	private long edituserid;// 修改人
	private String edittime;// 修改时间
	private String zhongzhuanzhanbranchname;// 中转站点名
	private String paisongusername;// 派送人姓名
	private String paisongzhandianname;
	private String shoujianrencompany;
	private String cwbcity;// 收件人城市
	private String sendcarname;// 品名
	private long sendcarnum;
	private BigDecimal carrealweight;// 重量
	private BigDecimal receivablefee;// 应收款
	private long chang;
	private long kuan;
	private long gao;

	public long getChang() {
		return chang;
	}

	public void setChang(long chang) {
		this.chang = chang;
	}

	public long getKuan() {
		return kuan;
	}

	public void setKuan(long kuan) {
		this.kuan = kuan;
	}

	public long getGao() {
		return gao;
	}

	public void setGao(long gao) {
		this.gao = gao;
	}

	public BigDecimal getReceivablefee() {
		return receivablefee;
	}

	public void setReceivablefee(BigDecimal receivablefee) {
		this.receivablefee = receivablefee;
	}

	public BigDecimal getCarrealweight() {
		return carrealweight;
	}

	public void setCarrealweight(BigDecimal carrealweight) {
		this.carrealweight = carrealweight;
	}

	public long getSendcarnum() {
		return sendcarnum;
	}

	public void setSendcarnum(long sendcarnum) {
		this.sendcarnum = sendcarnum;
	}

	public String getSendcarname() {
		return sendcarname;
	}

	public void setSendcarname(String sendcarname) {
		this.sendcarname = sendcarname;
	}

	public String getCwbcity() {
		return cwbcity;
	}

	public void setCwbcity(String cwbcity) {
		this.cwbcity = cwbcity;
	}

	public String getShoujianrencompany() {
		return shoujianrencompany;
	}

	public void setShoujianrencompany(String shoujianrencompany) {
		this.shoujianrencompany = shoujianrencompany;
	}

	public String getPaisongzhandianname() {
		return paisongzhandianname;
	}

	public void setPaisongzhandianname(String paisongzhandianname) {
		this.paisongzhandianname = paisongzhandianname;
	}

	public String getPaisongusername() {
		return paisongusername;
	}

	public void setPaisongusername(String paisongusername) {
		this.paisongusername = paisongusername;
	}

	public String getZhongzhuanzhanbranchname() {
		return zhongzhuanzhanbranchname;
	}

	public void setZhongzhuanzhanbranchname(String zhongzhuanzhanbranchname) {
		this.zhongzhuanzhanbranchname = zhongzhuanzhanbranchname;
	}

	public void setConsigneephone(String consigneephone) {
		this.consigneephone = consigneephone;
	}

	public String getCwb() {
		return cwb == null ? "" : cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public String getConsigneename() {
		return consigneename == null ? "" : consigneename;
	}

	public void setConsigneename(String consigneename) {
		this.consigneename = consigneename;
	}

	public String getConsigneeaddress() {
		return consigneeaddress == null ? "" : consigneeaddress;
	}

	public void setConsigneeaddress(String consigneeaddress) {
		if (consigneeaddress == null) {
			this.consigneeaddress = "";
			return;
		}
		this.consigneeaddress = consigneeaddress.replaceAll("#", "").replaceAll("[*]", "").replaceAll(" ", "").replaceAll("\\p{P}", "");
	}

	public String getConsigneepostcode() {
		return consigneepostcode == null ? "" : consigneepostcode;
	}

	public void setConsigneepostcode(String consigneepostcode) {
		this.consigneepostcode = consigneepostcode;
	}

	public String getConsigneephone() {
		return consigneephone == null ? "" : consigneephone;
	}

	public void setConsigneephone(String consigneephone, boolean guessMobile) {
		this.consigneephone = consigneephone;
		if (guessMobile) {
			setConsigneemobile(consigneephone);
		}
	}

	public String getConsigneemobile() {
		return consigneemobile == null ? "" : consigneemobile;
	}

	public void setConsigneemobile(String consigneemobile) {
		this.consigneemobile = (consigneemobile);
	}

	public String getConsigneeno() {
		return consigneeno == null ? "" : consigneeno;
	}

	public void setConsigneeno(String consigneeno) {
		this.consigneeno = (consigneeno);
	}

	public long getFlowordertype() {
		return flowordertype;
	}

	public void setFlowordertype(long flowordertype) {
		this.flowordertype = flowordertype;
	}

	public String getEdittime() {
		return edittime == null ? "" : edittime;
	}

	public void setEdittime(String edittime) {
		this.edittime = edittime;
	}

	public String getFlowordertypeMethod() {
		for (FlowOrderTypeEnum fote : FlowOrderTypeEnum.values()) {
			if (fote.getValue() == flowordertype)
				return fote.getText();
		}
		return "";
	}

	public long getLanshoubranchid() {
		return lanshoubranchid;
	}

	public void setLanshoubranchid(long lanshoubranchid) {
		this.lanshoubranchid = lanshoubranchid;
	}

	public String getLanshoubranchname() {
		return lanshoubranchname;
	}

	public void setLanshoubranchname(String lanshoubranchname) {
		this.lanshoubranchname = lanshoubranchname;
	}

	public long getLanshouuserid() {
		return lanshouuserid;
	}

	public void setLanshouuserid(long lanshouuserid) {
		this.lanshouuserid = lanshouuserid;
	}

	public String getLanshouusername() {
		return lanshouusername;
	}

	public void setLanshouusername(String lanshouusername) {
		this.lanshouusername = lanshouusername;
	}

	public long getPaisongbranchid() {
		return paisongbranchid;
	}

	public void setPaisongbranchid(long paisongbranchid) {
		this.paisongbranchid = paisongbranchid;
	}

	public long getPaisonguserid() {
		return paisonguserid;
	}

	public void setPaisonguserid(long paisonguserid) {
		this.paisonguserid = paisonguserid;
	}

	public long getZhongzhuanbranchid() {
		return zhongzhuanbranchid;
	}

	public void setZhongzhuanbranchid(long zhongzhuanbranchid) {
		this.zhongzhuanbranchid = zhongzhuanbranchid;
	}

	public String getPaisongtime() {
		return paisongtime;
	}

	public void setPaisongtime(String paisongtime) {
		this.paisongtime = paisongtime;
	}

	public String getLanshoutime() {
		return lanshoutime;
	}

	public void setLanshoutime(String lanshoutime) {
		this.lanshoutime = lanshoutime;
	}

	public String getSendconsigneename() {
		return sendconsigneename;
	}

	public void setSendconsigneename(String sendconsigneename) {
		this.sendconsigneename = sendconsigneename;
	}

	public String getSendconsigneecompany() {
		return sendconsigneecompany;
	}

	public void setSendconsigneecompany(String sendconsigneecompany) {
		this.sendconsigneecompany = sendconsigneecompany;
	}

	public String getSendconsigneeareacode() {
		return sendconsigneeareacode;
	}

	public void setSendconsigneeareacode(String sendconsigneeareacode) {
		this.sendconsigneeareacode = sendconsigneeareacode;
	}

	public String getSendconsigneecity() {
		return sendconsigneecity;
	}

	public void setSendconsigneecity(String sendconsigneecity) {
		this.sendconsigneecity = sendconsigneecity;
	}

	public String getSendconsigneeaddress() {
		return sendconsigneeaddress;
	}

	public void setSendconsigneeaddress(String sendconsigneeaddress) {
		this.sendconsigneeaddress = sendconsigneeaddress;
	}

	public String getSendconsigneemobile() {
		return sendconsigneemobile;
	}

	public void setSendconsigneemobile(String sendconsigneemobile) {
		this.sendconsigneemobile = sendconsigneemobile;
	}

	public String getSendconsigneephone() {
		return sendconsigneephone;
	}

	public void setSendconsigneephone(String sendconsigneephone) {
		this.sendconsigneephone = sendconsigneephone;
	}

	public String getSendconsigneepostcode() {
		return sendconsigneepostcode;
	}

	public void setSendconsigneepostcode(String sendconsigneepostcode) {
		this.sendconsigneepostcode = sendconsigneepostcode;
	}

	public long getPaytype() {
		return paytype;
	}

	public void setPaytype(long paytype) {
		this.paytype = paytype;
	}

	public String getConsigneeareacode() {
		return consigneeareacode;
	}

	public void setConsigneeareacode(String consigneeareacode) {
		this.consigneeareacode = consigneeareacode;
	}

	public BigDecimal getPackingfee() {
		return packingfee;
	}

	public void setPackingfee(BigDecimal packingfee) {
		this.packingfee = packingfee;
	}

	public BigDecimal getInsuredrate() {
		return insuredrate;
	}

	public void setInsuredrate(BigDecimal insuredrate) {
		this.insuredrate = insuredrate;
	}

	public BigDecimal getSafefee() {
		return safefee;
	}

	public void setSafefee(BigDecimal safefee) {
		this.safefee = safefee;
	}

	public BigDecimal getTransitfee() {
		return transitfee;
	}

	public void setTransitfee(BigDecimal transitfee) {
		this.transitfee = transitfee;
	}

	public BigDecimal getWeightfee() {
		return weightfee;
	}

	public void setWeightfee(BigDecimal weightfee) {
		this.weightfee = weightfee;
	}

	public BigDecimal getRealweight() {
		return realweight;
	}

	public void setRealweight(BigDecimal realweight) {
		this.realweight = realweight;
	}

	public BigDecimal getNowfee() {
		return nowfee;
	}

	public void setNowfee(BigDecimal nowfee) {
		this.nowfee = nowfee;
	}

	public BigDecimal getFuturefee() {
		return futurefee;
	}

	public void setFuturefee(BigDecimal futurefee) {
		this.futurefee = futurefee;
	}

	public BigDecimal getPackagefee() {
		return packagefee;
	}

	public void setPackagefee(BigDecimal packagefee) {
		this.packagefee = packagefee;
	}

	public BigDecimal getOtherfee() {
		return otherfee;
	}

	public void setOtherfee(BigDecimal otherfee) {
		this.otherfee = otherfee;
	}

	public BigDecimal getAllfee() {
		return allfee;
	}

	public void setAllfee(BigDecimal allfee) {
		this.allfee = allfee;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public long getSignstate() {
		return signstate;
	}

	public void setSignstate(long signstate) {
		this.signstate = signstate;
	}

	public String getSignman() {
		return signman;
	}

	public void setSignman(String signman) {
		this.signman = signman;
	}

	public String getSigntime() {
		return signtime;
	}

	public void setSigntime(String signtime) {
		this.signtime = signtime;
	}

	public long getEdituserid() {
		return edituserid;
	}

	public void setEdituserid(long edituserid) {
		this.edituserid = edituserid;
	}

}
