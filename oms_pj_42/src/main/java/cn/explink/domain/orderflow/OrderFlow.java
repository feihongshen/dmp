package cn.explink.domain.orderflow;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import net.sf.json.JSONObject;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import cn.explink.dao.OrderFlowDAO;
import cn.explink.domain.User;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;

public class OrderFlow {
	protected long floworderid;
	protected String cwb;
	protected long branchid;
	protected Timestamp credate;
	protected long userid;
	protected int flowordertype;
	protected int isnow;
	protected JSONObject floworderdetail;
	protected String username;
	protected String branchname;//
	protected int customerid;
	protected Timestamp emaildate;
	protected Timestamp shiptime;
	protected BigDecimal caramount;
	protected int exportType; // 导出状态 0 未导出 1 导出未审核 2 导出审核
	protected int isGo;
	protected int outwarehouseid;
	protected int state;
	protected long emaildateid;
	protected String customername; // 供货商名称
	protected long startbranchid;// 当前站点id
	protected long nextbranchid;// 下一站id
	protected long excelbranchid;// 目的站id
	protected String carwarehouse;// 入库仓库
	protected String startbranchname;// 当前站名称
	protected String nextbranchname;// 下一站名称
	protected String excelbranchname;// 目的站名称
	protected long deliverid;// 小件员id
	protected String delivername;// 小件员名称
	protected String signinman;// 签收人
	protected long orderResultType;// 反馈状态

	// ===新加字段
	private long targetcarwarehouse;// 目标仓库
	private String targetcarwarehouseName;// 目标仓库名称

	public long getOrderResultType() {
		return orderResultType;
	}

	public void setOrderResultType(long orderResultType) {
		this.orderResultType = orderResultType;
	}

	public long getTargetcarwarehouse() {
		return targetcarwarehouse;
	}

	public void setTargetcarwarehouse(long targetcarwarehouse) {
		this.targetcarwarehouse = targetcarwarehouse;
	}

	public String getTargetcarwarehouseName() {
		return targetcarwarehouseName;
	}

	public void setTargetcarwarehouseName(String targetcarwarehouseName) {
		this.targetcarwarehouseName = targetcarwarehouseName;
	}

	public String getSigninman() {
		return signinman;
	}

	public void setSigninman(String signinman) {
		this.signinman = signinman;
	}

	public long getDeliverid() {
		return deliverid;
	}

	public void setDeliverid(long deliverid) {
		this.deliverid = deliverid;
	}

	public String getDelivername() {
		return delivername;
	}

	public void setDelivername(String delivername) {
		this.delivername = delivername;
	}

	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public OrderFlow() {
	}

	public OrderFlow(long floworderid, String cwb, long branchid, Timestamp credate, long userid, JSONObject floworderdetail, int flowordertype, int customerid, Timestamp emaildate,
			Timestamp shiptime, BigDecimal caramount, int outwarehouseid, String customername) {
		super();
		this.floworderid = floworderid;
		this.cwb = cwb;
		this.branchid = branchid;
		this.credate = credate;
		this.userid = userid;
		this.floworderdetail = floworderdetail;
		this.flowordertype = flowordertype;
		this.customerid = customerid;
		this.emaildate = emaildate;
		this.shiptime = shiptime;
		this.caramount = caramount;
		this.outwarehouseid = outwarehouseid;
		this.customername = customername;

	}

	/**
	 * @return 获得flowordertype的值对应的文字
	 */
	public String getFlowordertypeText() {
		for (FlowOrderTypeEnum fote : FlowOrderTypeEnum.values()) {
			if (fote.getValue() == flowordertype)
				return fote.getText();
		}
		return "";
	}

	// public String getFloworderTrackInfo(User user){
	// String returnStrs="";
	// for (FlowOrderTypeEnum fote :FlowOrderTypeEnum.values()) {
	// if(flowordertype==fote.DaoRuShuJu.getValue()){
	// returnStrs=fote.DaoRuShuJu.getText()+",当前库房"+startbranchname;
	// }
	// else if(flowordertype==fote.RuKu.getValue()){
	// returnStrs=fote.RuKu.getText()+",当前库房"+startbranchname;
	// }else if(flowordertype==fote.ChuKuSaoMiao.getValue()){
	// returnStrs=fote.ChuKuSaoMiao.getText()+",货物从["+startbranchname+"]出库，发往["+excelbranchname+"]";
	// }else if(flowordertype==fote.FenZhanDaoHuoSaoMiao.getValue()){
	// returnStrs=fote.FenZhanDaoHuoSaoMiao.getText()+",货物已送达分站["+startbranchname+"]";
	// }else if(flowordertype==fote.FenZhanLingHuo.getValue()){
	// returnStrs="货物由分站["+startbranchname+"]的小件员["+user.getRealname()+"]开始配送,联系电话:["+(user.getUsermobile())+"]";
	// }else if(flowordertype==fote.PeiSongChengGong.getValue()){
	// returnStrs="已配送成功";
	// }else if(flowordertype==fote.ZhongZhuanZhanChuKuSaoMiao.getValue()){
	// returnStrs=fote.ZhongZhuanZhanChuKuSaoMiao.getText();
	// }else if(flowordertype==fote.ZhongZhuanChuKuSaoMiao.getValue()){
	// returnStrs=fote.ZhongZhuanChuKuSaoMiao.getText();
	// }else if(flowordertype==fote.CheXiaoFanKui.getValue()){
	// returnStrs=fote.CheXiaoFanKui.getText();
	// }else if(flowordertype==fote.ZhongZhuanZhanRuKu.getValue()){
	// returnStrs=fote.ZhongZhuanZhanRuKu.getText();
	// }else if(flowordertype==fote.ZhongZhuanZhanYouHuoWuDanRuKu.getValue()){
	// returnStrs=fote.ZhongZhuanZhanYouHuoWuDanRuKu.getText();
	// }else if(flowordertype==fote.YouHuoWuDan.getValue()){
	// returnStrs=fote.YouHuoWuDan.getText();
	// }else if(flowordertype==fote.TuiHuoZhanZaiTouSaoMiao.getValue()){
	// returnStrs=fote.TuiHuoZhanZaiTouSaoMiao.getText();
	// }else if(flowordertype==fote.TuiHuoZhanYouHuoWuDanRuKu.getValue()){
	// returnStrs=fote.TuiHuoZhanYouHuoWuDanRuKu.getText();
	// }else if(flowordertype==fote.TuiHuoZhanRuKu.getValue()){
	// returnStrs=fote.TuiHuoZhanRuKu.getText();
	// }else if(flowordertype==fote.TuiHuoChuKuSaoMiao.getValue()){
	// returnStrs=fote.TuiHuoChuKuSaoMiao.getText();
	// }else if(flowordertype==fote.TuiGongYingShangChuKu.getValue()){
	// returnStrs=fote.TuiGongYingShangChuKu.getText();
	// }else if(flowordertype==fote.TiHuoYouHuoWuDan.getValue()){
	// returnStrs=fote.TiHuoYouHuoWuDan.getText();
	// }else if(flowordertype==fote.TiHuo.getValue()){
	// returnStrs=fote.TiHuo.getText();
	// }else if(flowordertype==fote.ShangMenTuiChengGong.getValue()){
	// returnStrs=fote.ShangMenTuiChengGong.getText();
	// }else if(flowordertype==fote.ShangMenJuTui.getValue()){
	// returnStrs=fote.ShangMenJuTui.getText();
	// }else if(flowordertype==fote.ShangMenHuanChengGong.getValue()){
	// returnStrs=fote.ShangMenHuanChengGong.getText();
	// }else if(flowordertype==fote.HuoWuDiuShi.getValue()){
	// returnStrs=fote.HuoWuDiuShi.getText();
	// }else if(flowordertype==fote.FenZhanZhiLiu.getValue()){
	// returnStrs=fote.FenZhanZhiLiu.getText();
	// }
	// else if(flowordertype==fote.JuShou.getValue()){
	// returnStrs=fote.JuShou.getText();
	// }
	// break;
	//
	// }
	//
	// return returnStrs;
	// }

	/**
	 * @return 获得flowordertype的值对应的解析JSON方法
	 */
	public String getFlowordertypeMethod() {
		for (FlowOrderTypeEnum fote : FlowOrderTypeEnum.values()) {
			if (fote.getValue() == flowordertype)
				return fote.getMethod();
		}
		return "";
	}

	public String toHtml() throws Exception {

		Method m = this.getClass().getMethod("get" + getFlowordertypeMethod());
		String html = (String) m.invoke(this);
		return html;
	}

	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public String getImportCwb() {
		ImportCwbToJson icTJ = new ImportCwbToJson();
		return icTJ.getBody(this);
	}

	public String getIntoWarehous() {
		IntoWarehousToJson iwTJ = new IntoWarehousToJson();
		return iwTJ.getBody(this);
	}

	public String getOutWarehouse() {
		OutWarehouseToJson owTJ = new OutWarehouseToJson();
		return owTJ.getBody(this);
	}

	public String getNoListIntoWarehous() {
		NoListIntoWarehousToJson niwtj = new NoListIntoWarehousToJson();
		return niwtj.getBody(this);
	}

	public String getSubstationGoods() {
		SubstationGoodsToJson sgtj = new SubstationGoodsToJson();
		return sgtj.getBody(this);
	}

	public String getSubstationGoodsNoList() {
		SubstationGoodsNoListToJson sgnltj = new SubstationGoodsNoListToJson();
		return sgnltj.getBody(this);
	}

	public String getReceiveGoods() {
		ReceiveGoodsToJson rgtj = new ReceiveGoodsToJson();
		return rgtj.getBody(this);
	}

	public String getReturnGoodsOutwarehouse() {
		ReturnGoodsOutwarehouseToJson rgotj = new ReturnGoodsOutwarehouseToJson();
		return rgotj.getBody(this);
	}

	public String getChangeGoodsOutwarehouse() {
		ChangeGoodsOutwarehouseToJson cgotj = new ChangeGoodsOutwarehouseToJson();
		return cgotj.getBody(this);
	}

	public String getGetGoods() {
		GetGoodsToJson ggtj = new GetGoodsToJson();
		return ggtj.getBody(this);
	}

	public String getChangeIntoWarehous() {
		ChangeIntoWarehousToJson ciwtj = new ChangeIntoWarehousToJson();
		return ciwtj.getBody(this);
	}

	public String getChangeNoListIntoWarehous() {
		ChangeNoListIntoWarehousToJson cniwtj = new ChangeNoListIntoWarehousToJson();
		return cniwtj.getBody(this);
	}

	public String getBackIntoWarehous() {
		BackIntoWarehousToJson biwtj = new BackIntoWarehousToJson();
		return biwtj.getBody(this);
	}

	public String getBackNoListIntoWarehous() {
		BackNoListIntoWarehousToJson bniwtj = new BackNoListIntoWarehousToJson();
		return bniwtj.getBody(this);
	}

	public String getTransBranchOutWarehouse() {
		TransBranchOutWarehouseToJson tbowtj = new TransBranchOutWarehouseToJson();
		return tbowtj.getBody(this);
	}

	public String getGetGoodsnoListIntoWarehous() {
		GetGoodsNoListIntoWarehousToJson ggnliwtj = new GetGoodsNoListIntoWarehousToJson();
		return ggnliwtj.getBody(this);
	}

	public String getBackReturnOutWarehous() {
		BackReturnOutWarehousToJson browtj = new BackReturnOutWarehousToJson();
		return browtj.getBody(this);
	}

	public String getSuccess() {
		SuccessToJson stj = new SuccessToJson();
		return stj.getBody(this);
	}

	public String getBackToDoorSuccess() {
		BackToDoorSuccessToJson btdstj = new BackToDoorSuccessToJson();
		return btdstj.getBody(this);
	}

	public String getChangeToDoorSuccess() {
		ChangeToDoorSuccessToJson ctdstj = new ChangeToDoorSuccessToJson();
		return ctdstj.getBody(this);
	}

	public String getReturnGoods() {
		ReturnGoodsToJson rgtj = new ReturnGoodsToJson();
		return rgtj.getBody(this);
	}

	public String getSomeReturnGoods() {
		SomeReturnGoodsToJson srgtj = new SomeReturnGoodsToJson();
		return srgtj.getBody(this);
	}

	public String getStayGoods() {
		StayGoodsToJson sgtj = new StayGoodsToJson();
		return sgtj.getBody(this);
	}

	public String getBackToDoorFail() {
		BackToDoorFailToJson btdftj = new BackToDoorFailToJson();
		return btdftj.getBody(this);
	}

	public String getGoodsLose() {
		GoodsLoseToJson gltj = new GoodsLoseToJson();
		return gltj.getBody(this);
	}

	public String getOthers() {
		OthersToJson otj = new OthersToJson();
		return otj.getBody(this);
	}

	public StringBuffer getBody() {
		StringBuffer body = new StringBuffer();
		body.append("<p>").append("</span><span>" + df.format(this.getCredate())).append("</p>");
		if (flowordertype == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
			body.append("<p>").append("在" + this.getStartbranchname() + ":" + this.getFlowordertypeText()).append("</p>");
			body.append("<p>").append("小件员:" + this.getDelivername()).append("</p>");
			body.append("<p>").append("操作员：" + this.getUsername()).append("</p>");
		} else if (orderResultType == DeliveryStateEnum.PeiSongChengGong.getValue()) {
			body.append("<p>").append("" + this.getFlowordertypeText()).append("</p>");
			body.append("<p>").append("签收人：本人签收").append("</p>");
			body.append("<p>").append("小件员:" + this.getDelivername()).append("</p>");
		} else {
			body.append("<p>").append("在" + this.getStartbranchname() + ":" + this.getFlowordertypeText()).append("</p>");
			body.append("<p>").append("操作员：" + this.getUsername()).append("</p>");
		}
		return body;
	}

	public StringBuffer getFloworderTrackInfoBody(User user) {
		// String returnStrs="";
		StringBuffer returnStrs = new StringBuffer();
		returnStrs.append("<td align=\"center\" width=\"200px\">").append("" + df.format(this.getCredate())).append("</td>");
		if (flowordertype == FlowOrderTypeEnum.RuKu.getValue()) {
			returnStrs.append("<td align=\"center\">").append(this.getUsername()).append("</td>");
			returnStrs.append("<td>" + FlowOrderTypeEnum.RuKu.getText() + ",当前库房" + this.getStartbranchname()).append("</td>");
		} else if (flowordertype == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) {
			returnStrs.append("<td align=\"center\">").append(this.getUsername()).append("</td>");
			returnStrs.append("<td>" + FlowOrderTypeEnum.ChuKuSaoMiao.getText() + ",货物从[" + this.getStartbranchname() + "]出库，发往[" + this.getNextbranchname() + "]").append("</td>");

		} else if (flowordertype == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) {
			returnStrs.append("<td align=\"center\">").append(this.getUsername()).append("</td>");
			returnStrs.append("<td>" + FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getText() + ",货物已送达[" + this.getStartbranchname() + "]").append("</td>");

		} else if (flowordertype == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
			returnStrs.append("<td align=\"center\">").append(this.getUsername()).append("</td>");
			returnStrs.append(
					"<td>" + "货物由[" + this.getStartbranchname() + "]的小件员[" + (user.getRealname() == null ? "" : user.getRealname()) + "]开始配送,联系电话:["
							+ (user.getUsermobile() == null ? "" : user.getUsermobile()) + "]").append("</td>");

		} else if (orderResultType == DeliveryStateEnum.PeiSongChengGong.getValue()) {
			returnStrs.append("<td align=\"center\">").append(this.getUsername()).append("</td>");
			returnStrs.append("<td>" + "已配送成功").append("小件员:" + this.getDelivername())
					.append("签收人：<font id=\"signinmanID\" color=\"red\">" + (this.getSigninman() == null || "".equals(this.getSigninman()) ? "本人签收" : this.getSigninman()) + "<font>").append("</td>");
		} else {
			returnStrs.append("<td align=\"center\">").append(this.getUsername()).append("</td>");
			returnStrs.append("<td>").append("在" + this.getStartbranchname() + ":" + this.getFlowordertypeText()).append("</td>");
		}
		return returnStrs;
	}

	public long getFloworderid() {
		return floworderid;
	}

	public void setFloworderid(long floworderid) {
		this.floworderid = floworderid;
	}

	public String getCwb() {
		return cwb;
	}

	public void setCwb(String cwb) {
		this.cwb = cwb;
	}

	public long getBranchid() {
		return branchid;
	}

	public void setBranchid(long branchid) {
		this.branchid = branchid;
	}

	public Timestamp getCredate() {
		return credate;
	}

	public String getCredateStr() {
		if (credate != null && !credate.toString().equals("")) {
			return credate.toString().substring(0, credate.toString().length() - 2);
		} else {
			return "";
		}
	}

	public void setCredate(Timestamp credate) {
		this.credate = credate;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public JSONObject getFloworderdetail() {
		return floworderdetail;
	}

	public void setFloworderdetail(JSONObject floworderdetail) {
		this.floworderdetail = floworderdetail;
	}

	public int getFlowordertype() {
		return flowordertype;
	}

	public void setFlowordertype(int flowordertype) {
		this.flowordertype = flowordertype;
	}

	public int getIsnow() {
		return isnow;
	}

	public void setIsnow(int isnow) {
		this.isnow = isnow;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getBranchname() {
		return branchname;
	}

	public void setBranchname(String branchname) {
		this.branchname = branchname;
	}

	public int getCustomerid() {
		return customerid;
	}

	public void setCustomerid(int customerid) {
		this.customerid = customerid;
	}

	public Timestamp getEmaildate() {
		return emaildate;
	}

	public void setEmaildate(Timestamp emaildate) {
		this.emaildate = emaildate;
	}

	public Timestamp getShiptime() {
		return shiptime;
	}

	public void setShiptime(Timestamp shiptime) {
		this.shiptime = shiptime;
	}

	public BigDecimal getCaramount() {
		return caramount;
	}

	public void setCaramount(BigDecimal caramount) {
		this.caramount = caramount;
	}

	public long getEmaildateid() {
		return emaildateid;
	}

	public void setEmaildateid(long emaildateid) {
		this.emaildateid = emaildateid;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getOutwarehouseid() {
		return outwarehouseid;
	}

	public void setOutwarehouseid(int outwarehouseid) {
		this.outwarehouseid = outwarehouseid;
	}

	public int getIsGo() {
		return isGo;
	}

	public void setIsGo(int isGo) {
		this.isGo = isGo;
	}

	public int getExportType() {
		return exportType;
	}

	public void setExportType(int exportType) {
		this.exportType = exportType;
	}

	public long getStartbranchid() {
		return startbranchid;
	}

	public void setStartbranchid(long startbranchid) {
		this.startbranchid = startbranchid;
	}

	public long getNextbranchid() {
		return nextbranchid;
	}

	public void setNextbranchid(long nextbranchid) {
		this.nextbranchid = nextbranchid;
	}

	public long getExcelbranchid() {
		return excelbranchid;
	}

	public void setExcelbranchid(long excelbranchid) {
		this.excelbranchid = excelbranchid;
	}

	public String getCarwarehouse() {
		return carwarehouse;
	}

	public void setCarwarehouse(String carwarehouse) {
		this.carwarehouse = carwarehouse;
	}

	public String getStartbranchname() {
		return startbranchname;
	}

	public void setStartbranchname(String startbranchname) {
		this.startbranchname = startbranchname;
	}

	public String getExcelbranchname() {
		return excelbranchname;
	}

	public void setExcelbranchname(String excelbranchname) {
		this.excelbranchname = excelbranchname;
	}

	public String getNextbranchname() {
		return nextbranchname;
	}

	public void setNextbranchname(String nextbranchname) {
		this.nextbranchname = nextbranchname;
	}

	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("/META-INF/spring/applicationContext*.xml");
		// OrderFlowDAO orderFlowDAO = (OrderFlowDAO)
		// ctx.getBean("orderFlowDAO");
		JdbcTemplate jdbcTemplate = (JdbcTemplate) ctx.getBean("jdbcTemplate");
		// orderFlowDAO.creOrderFlow(new OrderFlow(0,10,1, new Date(), 1,
		// null,FlowOrderTypeEnum.DaoRuShuJu.getValue()));
		// Long tim = System.currentTimeMillis();
		// String [] sql =
		// "SELECT CONVERT(realname USING gb2312) FROM express_set_user INTO OUTFILE".trim().split("\n");
		// jdbcTemplate.execute("SELECT CONVERT(realname USING gb2312) FROM express_set_user INTO OUTFILE 'd:/"+tim+".xls'");
		// jdbcTemplate.update("insert into express_ops_order_flow (cwb,branchid,credate,userid,floworderdetail,flowordertype)"
		// +
		// " values('adfasdf',1,now(),1,'{}',1)"
		System.out.println(jdbcTemplate.update(""));
		System.out.println(new OrderFlowDAO().getClass().getSimpleName());// 获取类名
	}
}
