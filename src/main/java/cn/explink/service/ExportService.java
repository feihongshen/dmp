package cn.explink.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Service;

import cn.explink.controller.AbnormalView;
import cn.explink.controller.ComplaintView;
import cn.explink.controller.CwbOrderView;
import cn.explink.controller.DeliveryStateView;
import cn.explink.controller.UserView;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.UserDAO;
import cn.explink.domain.AccountCwbDetailView;
import cn.explink.domain.AccountCwbFare;
import cn.explink.domain.AccountCwbFareDetail;
import cn.explink.domain.AccountCwbSummary;
import cn.explink.domain.AccountDeducDetailView;
import cn.explink.domain.AccountDeductRecordView;
import cn.explink.domain.BackSummary;
import cn.explink.domain.Branch;
import cn.explink.domain.BranchTodayLog;
import cn.explink.domain.Common;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbDiuShiView;
import cn.explink.domain.CwbKuaiDiView;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.GotoClassAuditing;
import cn.explink.domain.NewForExportJson;
import cn.explink.domain.OrderBackCheck;
import cn.explink.domain.PayUp;
import cn.explink.domain.Reason;
import cn.explink.domain.Remark;
import cn.explink.domain.Role;
import cn.explink.domain.SearcheditInfo;
import cn.explink.domain.TuihuoRecord;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.ComplaintAuditTypeEnum;
import cn.explink.enumutil.ComplaintTypeEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliverPayupArrearageapprovedEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.RealFlowOrderTypeEnum;
import cn.explink.enumutil.ReasonTypeEnum;
import cn.explink.enumutil.UserEmployeestatusEnum;

@Service
public class ExportService {

	@Autowired
	AdvancedQueryService advancedQueryService;
	@Autowired
	ReasonDao reasonDao;

	@Autowired
	DeliveryStateDAO deliveryStateDAO;

	@Autowired
	UserDAO userDAO;

	@Autowired
	BranchDAO branchDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public void SetPosPayFields(String[] cloumnName1, String[] cloumnName2, String[] cloumnName3) {

		cloumnName1[0] = "订单号";
		cloumnName2[0] = "cwb";
		cloumnName3[0] = "string";
		cloumnName1[1] = "支付方";
		cloumnName2[1] = "pos_code";
		cloumnName3[1] = "string";
		cloumnName1[2] = "付款金额";
		cloumnName2[2] = "payAmount";
		cloumnName3[2] = "double";
		cloumnName1[3] = "交易日期";
		cloumnName2[3] = "tradeTime";
		cloumnName3[3] = "string";
		cloumnName1[4] = "小件员";
		cloumnName2[4] = "tradeDeliverId";
		cloumnName3[4] = "string";
		cloumnName1[5] = "交易备注";
		cloumnName2[5] = "payRemark";
		cloumnName3[5] = "string";
		cloumnName1[6] = "签收人";
		cloumnName2[6] = "signName";
		cloumnName3[6] = "string";
		cloumnName1[7] = "签收类型";
		cloumnName2[7] = "signtypeid";
		cloumnName3[7] = "string";
		cloumnName1[8] = "发货时间";
		cloumnName2[8] = "deEmaildate";
		cloumnName3[8] = "string";
		cloumnName1[9] = "签收时间";
		cloumnName2[9] = "signTime";
		cloumnName3[9] = "string";
		cloumnName1[10] = "签收备注";
		cloumnName2[10] = "payDetail";
		cloumnName3[10] = "string";
		cloumnName1[11] = "是否撤销";
		cloumnName2[11] = "isSuccessFlag";
		cloumnName3[11] = "string";
		cloumnName1[12] = "站点";
		cloumnName2[12] = "branchid";
		cloumnName3[12] = "string";
		cloumnName1[13] = "供货商";
		cloumnName2[13] = "customerid";
		cloumnName3[13] = "string";
		cloumnName1[14] = "终端号";
		cloumnName2[14] = "terminal_no";
		cloumnName3[14] = "string";
		cloumnName1[15] = "原支付方式";
		cloumnName2[15] = "paywayid";
		cloumnName3[15] = "string";
		cloumnName1[16] = "现支付方式";
		cloumnName2[16] = "newpaywayid";
		cloumnName3[16] = "string";
	}

	public void setBale(String[] cloumnName1, String[] cloumnName2, String[] cloumnName3) {

		cloumnName1[0] = "包号";
		cloumnName2[0] = "baleno";
		cloumnName3[0] = "string";
		cloumnName1[1] = "出库时间";
		cloumnName2[1] = "cretime";
		cloumnName3[1] = "string";
		cloumnName1[2] = "出库库房";
		cloumnName2[2] = "branchid";
		cloumnName3[2] = "long";
		cloumnName1[3] = "下一站";
		cloumnName2[3] = "nextbranchid";
		cloumnName3[3] = "long";
		cloumnName1[4] = "订单数量";
		cloumnName2[4] = "cwbcount";
		cloumnName3[4] = "long";
		cloumnName1[5] = "包号状态";
		cloumnName2[5] = "balestate";
		cloumnName3[5] = "int";

	}

	public void SetCompitFields(String[] cloumnName1, String[] cloumnName2) {
		// 投诉日期、订单号、供货商、受理人、投诉类型、投诉机构、投诉对象、客户要求
		// 、配送结果、审核人、审核结果、审核时间、备注、发货时间、入库时间、到站时间、归班时间、客户地址、客户姓名 、客户电话
		cloumnName1[0] = "投诉日期";
		cloumnName2[0] = "CreateTime";
		cloumnName1[1] = "订单号";
		cloumnName2[1] = "Cwb";
		cloumnName1[2] = "供货商";
		cloumnName2[2] = "Customername";
		cloumnName1[3] = "受理人";
		cloumnName2[3] = "CreateUser";
		cloumnName1[4] = "投诉类型";
		cloumnName2[4] = "Type";
		cloumnName1[5] = "投诉机构";
		cloumnName2[5] = "Branchname";
		cloumnName1[6] = "投诉对象";
		cloumnName2[6] = "Deliveryid";
		cloumnName1[7] = "客户要求";
		cloumnName2[7] = "Content";
		cloumnName1[8] = "配送结果";
		cloumnName2[8] = "Deliverystate";
		cloumnName1[9] = "审核人";
		cloumnName2[9] = "AuditUser";
		cloumnName1[10] = "审核结果";
		cloumnName2[10] = "AuditType";
		cloumnName1[11] = "审核时间";
		cloumnName2[11] = "AuditTime";
		cloumnName1[12] = "备注";
		cloumnName2[12] = "AuditRemark";
		cloumnName1[13] = "发货时间";
		cloumnName2[13] = "Emaildate";
		cloumnName1[14] = "入库时间";
		cloumnName2[14] = "Instoreroomtime";
		cloumnName1[15] = "到站时间";
		cloumnName2[15] = "InSitetime";
		cloumnName1[16] = "归班时间";
		cloumnName2[16] = "Goclasstime";
		cloumnName1[17] = "客户地址";
		cloumnName2[17] = "Consigneeaddress";
		cloumnName1[18] = "客户姓名";
		cloumnName2[18] = "Consigneename";
		cloumnName1[19] = "客户电话";
		cloumnName2[19] = "Consigneephone";

		/* cloumnName1[9]= "当前状态"; cloumnName2[9]= "Orderflowtype"; */

	}

	public void SetCwbApplyTuiHuoFields(String[] cloumnName1, String[] cloumnName2) {
		// 订单号 供货商 订单类型 当前站点 退货站点 退货申请备注 配送结果 小件员 处理状态
		cloumnName1[0] = "订单号";
		cloumnName2[0] = "Cwb";
		cloumnName1[1] = "供货商";
		cloumnName2[1] = "Customername";
		cloumnName1[2] = "订单类型";
		cloumnName2[2] = "Cwbordertypeid";
		cloumnName1[3] = "当前站点";
		cloumnName2[3] = "Currentbranchname";
		cloumnName1[4] = "退货站点";
		cloumnName2[4] = "Applytuihuobranchname";
		cloumnName1[5] = "退货申请备注";
		cloumnName2[5] = "Applytuihuoremark";
		cloumnName1[6] = "配送结果";
		cloumnName2[6] = "Deliverystate";
		cloumnName1[7] = "小件员";
		cloumnName2[7] = "Delivername";
		cloumnName1[8] = "审核状态";
		cloumnName2[8] = "Applyishandle";
		cloumnName1[9] = "审核人";
		cloumnName2[9] = "Applyhandleusername";
		cloumnName1[10] = "退货审核备注";
		cloumnName2[10] = "Applyhandleremark";
		cloumnName1[11] = "审核时间";
		cloumnName2[11] = "Applyhandletime";
	}

	public void SetCwbApplyZhongZhuanFields(String[] cloumnName1, String[] cloumnName2) {
		// 订单号 供货商 订单类型 当前站点 中转站点 中转申请备注 配送结果 小件员 审核状态 审核人 中转审核备注 审核时间
		cloumnName1[0] = "订单号";
		cloumnName2[0] = "Cwb";
		cloumnName1[1] = "供货商";
		cloumnName2[1] = "Customername";
		cloumnName1[2] = "订单类型";
		cloumnName2[2] = "Cwbordertypeid";
		cloumnName1[3] = "当前站点";
		cloumnName2[3] = "Currentbranchname";
		cloumnName1[4] = "中转站点";
		cloumnName2[4] = "Applyzhongzhuanbranchname";
		cloumnName1[5] = "中转申请备注";
		cloumnName2[5] = "Applyzhongzhuanremark";
		cloumnName1[6] = "配送结果";
		cloumnName2[6] = "Deliverystate";
		cloumnName1[7] = "小件员";
		cloumnName2[7] = "Delivername";
		cloumnName1[8] = "审核状态";
		cloumnName2[8] = "Applyzhongzhuanishandle";
		cloumnName1[9] = "审核人";
		cloumnName2[9] = "Applyzhongzhuanhandleusername";
		cloumnName1[10] = "中转审核备注";
		cloumnName2[10] = "Applyzhongzhuanhandleremark";
		cloumnName1[11] = "审核时间";
		cloumnName2[11] = "Applyzhongzhuanhandletime";
	}

	// 问题件导出view
	public void SetAbnormalOrderFields(String[] cloumnName1, String[] cloumnName2) {
		cloumnName1[0] = "订单号";
		cloumnName2[0] = "Cwb";
		cloumnName1[1] = "供货商";
		cloumnName2[1] = "Customername";
		cloumnName1[2] = "发货时间";
		cloumnName2[2] = "Emaildate";
		cloumnName1[3] = "当时状态";
		cloumnName2[3] = "FlowordertypeName";
		cloumnName1[4] = "配送站点";
		cloumnName2[4] = "Deliverybranchname";
		cloumnName1[5] = "反馈人";
		cloumnName2[5] = "Creusername";
		cloumnName1[6] = "问题件类型";
		cloumnName2[6] = "Abnormaltype";
		cloumnName1[7] = "问题件反馈时间";
		cloumnName2[7] = "Credatetime";
		cloumnName1[8] = "问题件说明";
		cloumnName2[8] = "Describe";
	}

	// 审核页面导出
	public void SetAuditFields(String[] cloumnName1, String[] cloumnName2) {
		cloumnName1[0] = "订单号";
		cloumnName2[0] = "Cwb";
		cloumnName1[1] = "订单类型";
		cloumnName2[1] = "cwbordertypeid";
		cloumnName1[2] = "供货商";
		cloumnName2[2] = "customername";
		cloumnName1[3] = "收件人姓名";
		cloumnName2[3] = "consigneename";
		cloumnName1[4] = "收件人电话";
		cloumnName2[4] = "consigneemobile";
		cloumnName1[5] = "发货时间";
		cloumnName2[5] = "emaildate";
		cloumnName1[6] = "领货时间";
		cloumnName2[6] = "Createtime";
		cloumnName1[7] = "收件地址";
		cloumnName2[7] = "consigneeaddress";
		cloumnName1[8] = "应处理金额";
		cloumnName2[8] = "Businessfee";

	}

	// 审核页面 已反馈
	public void SetAuditFieldsYiFanKui(String[] cloumnName1, String[] cloumnName2) {
		cloumnName1[0] = "订单号";
		cloumnName2[0] = "Cwb";
		cloumnName1[1] = "订单类型";
		cloumnName2[1] = "cwbordertypeid";
		cloumnName1[2] = "供货商";
		cloumnName2[2] = "customername";
		cloumnName1[3] = "发货时间";
		cloumnName2[3] = "emaildate";
		cloumnName1[4] = "收件人地址";
		cloumnName2[4] = "consigneeaddress";
		cloumnName1[5] = "应处理金额";
		cloumnName2[5] = "Businessfee";
		cloumnName1[6] = "实收金额";
		cloumnName2[6] = "Receivedfee";
		cloumnName1[7] = "收款方式";
		cloumnName2[7] = "PaymentPattern";
		cloumnName1[8] = "备注信息";
		cloumnName2[8] = "Deliverstateremark";
		cloumnName1[9] = "取回商品";
		cloumnName2[9] = "Backcarname";
		cloumnName1[10] = "实退金额";
		cloumnName2[10] = "Returnedfee";
		cloumnName1[11] = "发出商品";
		cloumnName2[11] = "Sendcarname";
		cloumnName1[12] = "差异款金额";
		cloumnName2[12] = "Difference";
		cloumnName1[13] = "反馈备注";
		cloumnName2[13] = "Remarks";
		cloumnName1[14] = "配送结果";
		cloumnName2[14] = "deliverystate";

	}

	// 审核页面 已反馈
	public Object setAuditYifankuiObject(String[] cloumnName3, List<DeliveryStateView> views, Object a, int i, int k) {
		try {
			if (cloumnName3[i].equals("Cwb")) {
				a = views.get(k).getCwb();
			} else if (cloumnName3[i].equals("cwbordertypeid")) {
				a = "";
				if (views.get(k).getCwbordertypeid() > 0) {
					a = this.getCwbordertype(views.get(k).getCwbordertypeid());
				}
			} else if (cloumnName3[i].equals("customername")) {
				a = views.get(k).getCustomername();
			} else if (cloumnName3[i].equals("emaildate")) {
				a = views.get(k).getEmaildate();
			} else if (cloumnName3[i].equals("consigneeaddress")) {
				a = views.get(k).getConsigneeaddress();
			} else if (cloumnName3[i].equals("Businessfee")) {
				a = views.get(k).getBusinessfee();
			} else if (cloumnName3[i].equals("Receivedfee")) {
				a = views.get(k).getReceivedfee();
			} else if (cloumnName3[i].equals("PaymentPattern")) {
				a = views.get(k).getPaymentPattern();
			} else if (cloumnName3[i].equals("Deliverstateremark")) {
				a = views.get(k).getDeliverstateremark();
			} else if (cloumnName3[i].equals("Backcarname")) {
				a = views.get(k).getBackcarname();
			} else if (cloumnName3[i].equals("Returnedfee")) {
				a = views.get(k).getReturnedfee();
			} else if (cloumnName3[i].equals("Sendcarname")) {
				a = views.get(k).getSendcarname();
			} else if (cloumnName3[i].equals("Remarks")) {
				a = views.get(k).getRemarks();
			} else if (cloumnName3[i].equals("Difference")) {
				a = "";
				if (views.get(k).getDeliverystate() == DeliveryStateEnum.ShangMenHuanChengGong.getValue()) {
					a = views.get(k).getDifference();
				}
			} else if (cloumnName3[i].equals("deliverystate")) {
				a = "";
				if (views.get(k).getDeliverystate() > 0) {
					a = this.getDeliveryState(views.get(k).getDeliverystate());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return a;
	}

	private String getDeliveryState(long deliverystate) {
		String str = "";
		if (deliverystate > 0) {
			for (DeliveryStateEnum de : DeliveryStateEnum.values()) {
				if (de.getValue() == deliverystate) {
					str = de.getText();
					break;
				}
			}
		}
		return str;
	}

	public Object setAuditWeifankuiObject(String[] cloumnName3, List<DeliveryStateView> views, Object a, int i, int k) {
		try {

			if (cloumnName3[i].equals("Cwb")) {
				a = views.get(k).getCwb();
			} else if (cloumnName3[i].equals("cwbordertypeid")) {
				a = "";
				if (views.get(k).getCwbordertypeid() > 0) {
					a = this.getCwbordertype(views.get(k).getCwbordertypeid());
				}
			} else if (cloumnName3[i].equals("customername")) {
				a = views.get(k).getCustomername();
			} else if (cloumnName3[i].equals("consigneename")) {
				if (this.getSessionUser().getShownameflag() == 1) {
					a = views.get(k).getConsigneename();
				} else {
					a = "******";
				}
			} else if (cloumnName3[i].equals("consigneemobile")) {
				if (this.getSessionUser().getShowmobileflag() == 1) {
					a = views.get(k).getConsigneemobile();
				} else {
					a = "******";
				}
			} else if (cloumnName3[i].equals("emaildate")) {
				a = views.get(k).getEmaildate();
			} else if (cloumnName3[i].equals("Createtime")) {
				a = views.get(k).getCreatetime();
			} else if (cloumnName3[i].equals("consigneeaddress")) {
				a = views.get(k).getConsigneeaddress();
			} else if (cloumnName3[i].equals("Businessfee")) {
				a = views.get(k).getBusinessfee();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return a;
	}

	private String getCwbordertype(int cwbordertypeid) {
		String str = "";
		if (cwbordertypeid > 0) {
			for (CwbOrderTypeIdEnum ce : CwbOrderTypeIdEnum.values()) {
				if (ce.getValue() == cwbordertypeid) {
					str = ce.getText();
				}
			}
		}
		return str;
	}

	// 问题件处理功能导出
	public Object setAbnormalOrderObject(String[] cloumnName3, List<AbnormalView> views, Object a, int i, int k) {
		try {
			if (cloumnName3[i].equals("Cwb")) {
				a = views.get(k).getCwb();
			} else if (cloumnName3[i].equals("Customername")) {
				a = views.get(k).getCustomerName();
			} else if (cloumnName3[i].equals("Emaildate")) {
				a = views.get(k).getEmaildate();
			} else if (cloumnName3[i].equals("FlowordertypeName")) {
				a = views.get(k).getFlowordertype();
			} else if (cloumnName3[i].equals("Deliverybranchname")) {
				a = views.get(k).getBranchName();
			} else if (cloumnName3[i].equals("Creusername")) {
				a = views.get(k).getCreuserName();
			} else if (cloumnName3[i].equals("Abnormaltype")) {
				a = views.get(k).getAbnormalType();
			} else if (cloumnName3[i].equals("Credatetime")) {
				a = views.get(k).getCredatetime();
			} else if (cloumnName3[i].equals("Describe")) {
				a = views.get(k).getDescribe();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return a;
	}

	/*
	 * public Object setObjectA(String[] cloumnName3,ResultSet rs,int i,
	 * List<User> uList, Map<Long, Customer> cMap, List<Branch> bList,
	 * List<Common> commonList, DeliveryState ds, Map<String, String>
	 * allTime,List<CustomWareHouse> cWList,Map<String, Map<String, String>>
	 * remarkMap,List<Reason> reasonList) throws SQLException{ Object a = null;
	 * String cloumname = cloumnName3[i]; cloumname =
	 * cloumnName3[i].substring(0,1).toLowerCase() +
	 * cloumnName3[i].substring(1,cloumnName3[i].length()); try { if(
	 * "orderType".equals(cloumname)){ a = rs.getObject("cwbordertypeid"); for
	 * (CwbOrderTypeIdEnum f : CwbOrderTypeIdEnum.values()) { if("".equals(a)){
	 * a ="配送"; break; }else if(a.equals("-1")){ a ="配送"; break; } else if
	 * (f.getValue()==Integer.parseInt(a.toString()) ) { a =f.getText(); break;
	 * } } }else if("flowordertypeMethod".equals(cloumname)){ a =
	 * rs.getObject("flowordertype")==null?"0":rs.getObject("flowordertype");
	 * for (FlowOrderTypeEnum fote :FlowOrderTypeEnum.values()) {
	 * if(fote.getValue()==Integer.parseInt(a.toString())) return
	 * fote.getText(); } }else if("paytypeName".equals(cloumname)){ a =
	 * advancedQueryService
	 * .getPayWayType(rs.getObject("cwb").toString(),rs.getObject
	 * ("newpaywayid").toString()); }else
	 * if("statisticstateStr".equals(cloumname)){ a =""; for(DeliveryStateEnum
	 * dse:DeliveryStateEnum.values()){
	 * if(dse.getValue()==ds.getDeliverystate()){ a = dse.getText(); break; } }
	 * }else if("orderResultTypeText".equals(cloumname)){ a = ""; for
	 * (DeliveryStateEnum fote :DeliveryStateEnum.values()) {
	 * if(fote.getValue()==ds.getDeliverystate()){ a = fote.getText(); break; }
	 * } }else if("carwarehouse".equals(cloumname)){ a = ""; for(Branch b
	 * :bList){ if(b.getBranchid()==
	 * Long.parseLong(rs.getObject("carwarehouse")==
	 * null?"0":rs.getObject("carwarehouse").toString())){ a=b.getBranchname();
	 * break; } } }else if("customerwarehouseid".equals(cloumname)){ a = "";
	 * for(CustomWareHouse b :cWList){
	 * if(b.getWarehouseid()==rs.getLong("customerwarehouseid")){
	 * a=b.getCustomerwarehouse(); break; } } }else
	 * if("inwarhouseRemark".equals(cloumname)){ a =
	 * remarkMap.get(rs.getObject("Cwb"
	 * ).toString())==null?"":remarkMap.get(rs.getObject
	 * ("Cwb").toString()).get(ReasonTypeEnum.RuKuBeiZhu.getText()); }else
	 * if("posremark".equals(cloumname)){ a = ds.getPosremark(); }else
	 * if("checkremark".equals(cloumname)){ a = ds.getCheckremark(); }else
	 * if("deliverstateremark".equals(cloumname)){ a =
	 * ds.getDeliverstateremark(); }else if("businessfee".equals(cloumname)){ a
	 * = ds.getBusinessfee(); }else if("receivedfeeuserName".equals(cloumname)){
	 * a = ""; for(User u :uList){ if(u.getUserid()==ds.getReceivedfeeuser()){
	 * a=u.getRealname(); break; } } }else
	 * if("customerbrackhouseremark".equals(cloumname)){ a =
	 * allTime.get("customerbrackhouseremark"
	 * )==null?"":allTime.get("customerbrackhouseremark"); }else
	 * if("instoreroomtime".equals(cloumname)){ a =
	 * allTime.get("Instoreroomtime")==null?"":allTime.get("Instoreroomtime");
	 * }else if("inSitetime".equals(cloumname)){ a =
	 * allTime.get("InSitetime")==null?"":allTime.get("InSitetime"); }else
	 * if("pickGoodstime".equals(cloumname)){ a =
	 * allTime.get("PickGoodstime")==null?"":allTime.get("PickGoodstime"); }else
	 * if("outstoreroomtime".equals(cloumname)){ a =
	 * allTime.get("Outstoreroomtime")==null?"":allTime.get("Outstoreroomtime");
	 * }else if("goclasstime".equals(cloumname)){ a =
	 * allTime.get("Goclasstime")==null?"":allTime.get("Goclasstime"); }else
	 * if("newchangetime".equals(cloumname)){ a =
	 * allTime.get("Newchangetime")==null?"":allTime.get("Newchangetime"); }else
	 * if("gobacktime".equals(cloumname)){ a =
	 * allTime.get("Gobacktime")==null?"":allTime.get("Gobacktime"); }else if
	 * ("zhongzhuanzhangintime".equals(cloumname)) { //中转站入库时间
	 * a=allTime.get("zhongzhuanzhanIntime"
	 * )==null?"":allTime.get("zhongzhuanzhanIntime"); }else if
	 * ("zhongzhuanzhangouttime".equals(cloumname)) { //中转站出库时间
	 * a=allTime.get("zhongzhuanzhanOuttime"
	 * )==null?"":allTime.get("zhongzhuanzhanOuttime"); }else
	 * if("podremarkStr".equals(cloumname)){ a = ""; if(reasonList.size()>0){
	 * for(Reason reason : reasonList){
	 * if(ds.getPodremarkid()==reason.getReasonid()){ a =
	 * reason.getReasoncontent(); } } } }else
	 * if("leavedreason".equals(cloumname)){ a =
	 * rs.getObject("leavedreason")==null?"":rs.getString("leavedreason"); }else
	 * if("losereason".equals(cloumname)){
	 * a=rs.getObject("losereason")==null?"":rs.getString("losereason");
	 * 
	 * }else if ("tuihuochuzhantime".equals(cloumname)) {
	 * a=rs.getObject("tuihuochuzhantime"
	 * )==null?"":rs.getString("tuihuochuzhantime"); }else if
	 * ("tuihuozhanrukutime".equals(cloumname)) {
	 * a=rs.getObject("tuihuozhanrukutime"
	 * )==null?"":rs.getString("tuihuozhanrukutime"); }else
	 * if("tuigonghuoshangchukutime".equals(cloumname)){
	 * a=rs.getObject("tuigonghuoshangchukutime"
	 * )==null?"":rs.getString("tuigonghuoshangchukutime"); }else
	 * if("weishuakareason".equals(cloumname)){
	 * a=rs.getObject("weishuakareason")
	 * ==null?"":rs.getString("weishuakareason");
	 * 
	 * }else if("resendtime".equals(cloumname)){ a =
	 * rs.getObject("resendtime")==null?"":rs.getString("resendtime"); }else
	 * if("backreason".equals(cloumname)){ a =
	 * rs.getObject("backreason")==null?"":rs.getString("backreason"); }else
	 * if("customerid".equals(cloumname)){ a =
	 * cMap.get(rs.getLong("Customerid"))
	 * ==null?"":cMap.get(rs.getLong("Customerid")).getCustomername(); }else
	 * if("startbranchname".equals(cloumname)){ a = ""; for(Branch b :bList){
	 * if(b.getBranchid()==
	 * Long.parseLong(rs.getObject("startbranchid").toString())){
	 * a=b.getBranchname(); break; } } }else
	 * if("nextbranchname".equals(cloumname)){ a = ""; for(Branch b :bList){
	 * if(b.getBranchid()==
	 * Long.parseLong(rs.getObject("nextbranchid").toString())){
	 * a=b.getBranchname(); break; } } }else
	 * if("fdelivername".equals(cloumname)){ a = ""; for(User u :uList){
	 * if(u.getUserid()==ds.getDeliveryid()){ a=u.getRealname();
	 * if(u.getEmployeestatus()==UserEmployeestatusEnum.LiZhi.getValue()){ a =
	 * a+"(离职)"; } break; } } } else if("dReceivedfee".equals(cloumname)){ a =
	 * ds.getReceivedfee(); }else if("deliverid".equals(cloumname)){ a = "";
	 * for(User u :uList){ if(u.getUserid()==rs.getLong("deliverid")){
	 * a=u.getRealname();
	 * if(u.getEmployeestatus()==UserEmployeestatusEnum.LiZhi.getValue()){ a =
	 * a+"(离职)"; } break; } } }else if("commonid".equals(cloumname)){ a = "";
	 * for(Common c :commonList){ if(c.getId()==rs.getLong("commonid")){
	 * a=c.getCommonname(); break; } } }else
	 * if("commonnumber".equals(cloumname)){ a = ""; for(Common c :commonList){
	 * if(c.getId()==rs.getLong("commonid")){ a=c.getCommonnumber(); break; } }
	 * }else if("cash".equals(cloumname)){ a = ds.getCash(); }else
	 * if("pos".equals(cloumname)){ a = ds.getPos(); }else
	 * if("receivedfee".equals(cloumname)){ a = ds.getReceivedfee(); }else
	 * if("returnedfee".equals(cloumname)){ a = ds.getReturnedfee(); }else
	 * if("checkfee".equals(cloumname)){ a = ds.getCheckfee(); }else
	 * if("backcarname".equals(cloumname)){ a = rs.getString("backcarname");
	 * }else if("otherfee".equals(cloumname)){ a = ds.getOtherfee(); }else
	 * if("signinman".equals(cloumname)){ a =""; if(ds.getDeliverystate() ==
	 * DeliveryStateEnum.PeiSongChengGong.getValue()){ a =
	 * rs.getString("consigneename"); } }else
	 * if("signintime".equals(cloumname)){ a = ""; if(ds.getDeliverystate() ==
	 * DeliveryStateEnum.PeiSongChengGong.getValue()){ a =
	 * allTime.get("Gobacktime")==null?"":allTime.get("Gobacktime"); } }else
	 * if("excelbranch".equals(cloumname)){ a = ""; for(Branch b :bList){
	 * if(b.getBranchid()==rs.getLong("deliverybranchid")){ a=b.getBranchname();
	 * break; } } }else if("currentbranchname".equals(cloumname)){ a = "";
	 * for(Branch b :bList){ if(b.getBranchid()==rs.getLong("currentbranchid")){
	 * a=b.getBranchname(); break; } } }else
	 * if("deliverybranchname".equals(cloumname)){ a = ""; for(Branch b :bList){
	 * if(b.getBranchid()==ds.getDeliverybranchid()){ a=b.getBranchname();
	 * break; } } }else if("realbranchname".equals(cloumname)){ a = ""; long
	 * realbranchid = rs.getLong("currentbranchid"); if(realbranchid==0){
	 * realbranchid = rs.getLong("startbranchid"); } for(Branch b :bList){
	 * if(b.getBranchid()==realbranchid){ a=b.getBranchname(); break; } } }else
	 * if("realflowordertype".equals(cloumname)){ long realbranchid =
	 * rs.getLong("currentbranchid"); if(realbranchid==0){ realbranchid =
	 * rs.getLong("startbranchid"); } a =
	 * getRealflowordertype(bList,realbranchid, rs.getLong("flowordertype"));
	 * }else{ try{ a = rs.getObject(cloumname); }catch (Exception e) {} } }
	 * catch (InvalidResultSetAccessException e) { //
	 * System.out.println(cloumname); }
	 * //System.out.println("pp:"+System.currentTimeMillis()); return a;
	 * 
	 * }
	 */

	public Object setObjectA(String[] cloumnName3, Map<String, Object> mapRow, int i, List<User> uList, Map<Long, Customer> cMap, List<Branch> bList, List<Common> commonList,
			TuihuoRecord tuihuoRecord, DeliveryState ds, Map<String, String> allTime, List<CustomWareHouse> cWList, Map<String, Map<String, String>> remarkMap, List<Reason> reasonList,
			Map<String, String> cwbspayupidMap, Map<String, String> complaintMap) throws SQLException {
		Object a = null;
		Branch currentb = new Branch();
		if (ds != null) {
			for (Branch b : bList) {
				if (b.getBranchid() == ds.getDeliverybranchid()) {
					currentb = b;
					break;
				}
			}
		}
		String cloumname = cloumnName3[i];
		cloumname = cloumnName3[i].substring(0, 1).toLowerCase() + cloumnName3[i].substring(1, cloumnName3[i].length());
		try {
			if ("orderType".equals(cloumname)) {
				a = mapRow.get("cwbordertypeid");
				for (CwbOrderTypeIdEnum f : CwbOrderTypeIdEnum.values()) {
					if ("".equals(a)) {
						a = "配送";
						break;
					} else if (a.equals("-1")) {
						a = "配送";
						break;
					} else if (f.getValue() == Integer.parseInt(a.toString())) {
						a = f.getText();
						break;
					}
				}
			} else if ("flowordertypeMethod".equals(cloumname)) {
				a = mapRow.get("flowordertype") == null ? "0" : mapRow.get("flowordertype");
				for (FlowOrderTypeEnum fote : FlowOrderTypeEnum.values()) {
					if (fote.getValue() == Integer.parseInt(a.toString())) {
						return fote.getText();
					}
				}
			} else if ("paytypeName".equals(cloumname)) {
				a = this.advancedQueryService.getPayWayType(mapRow.get("cwb").toString(), mapRow.get("paywayid").toString());
			} else if ("newpayway".equals(cloumname)) {
				a = this.advancedQueryService.getPayWayType(mapRow.get("cwb").toString(), mapRow.get("newpaywayid").toString());
			} else if ("statisticstateStr".equals(cloumname)) {
				a = "";
				if (ds != null) {
					for (DeliveryStateEnum dse : DeliveryStateEnum.values()) {
						if (dse.getValue() == ds.getDeliverystate()) {
							a = dse.getText();
							break;
						}
					}
				}
			} else if ("orderResultTypeText".equals(cloumname)) {
				a = "";
				if (ds != null) {
					for (DeliveryStateEnum fote : DeliveryStateEnum.values()) {
						if (fote.getValue() == ds.getDeliverystate()) {
							a = fote.getText();
							break;
						}
					}
				}
			} else if ("carwarehouse".equals(cloumname)) {
				a = "";
				for (Branch b : bList) {
					if (b.getBranchid() == Long.parseLong(mapRow.get("carwarehouse") == null ? "0" : mapRow.get("carwarehouse").toString())) {
						a = b.getBranchname();
						break;
					}
				}
			} else if ("customerwarehouseid".equals(cloumname)) {
				a = "";
				for (CustomWareHouse b : cWList) {
					if (b.getWarehouseid() == Long.parseLong(mapRow.get("customerwarehouseid").toString())) {
						a = b.getCustomerwarehouse();
						break;
					}
				}
			}/*
			 * else if("inwarhouseRemark".equals(cloumname)){ a = remarkMap.get(
			 * mapRow.get("Cwb").toString())==null?"":remarkMap.get(
			 * mapRow.get("Cwb"
			 * ).toString()).get(ReasonTypeEnum.RuKuBeiZhu.getText()); }
			 */else if ("posremark".equals(cloumname)) {
				a = (ds != null) ? ds.getPosremark() : "";
			} else if ("checkremark".equals(cloumname)) {
				a = (ds != null) ? ds.getCheckremark() : "";
			} else if ("deliverstateremark".equals(cloumname)) {
				a = (ds != null) ? ds.getDeliverstateremark() : "";
			} else if ("businessfee".equals(cloumname)) {
				a = (ds != null) ? ds.getBusinessfee() : "";
			} else if ("receivedfeeuserName".equals(cloumname)) {
				a = "";
				if (ds != null) {
					for (User u : uList) {
						if (u.getUserid() == ds.getReceivedfeeuser()) {
							a = u.getRealname();
							break;
						}
					}
				}
			} else if ("tuihuochuzhantime".equals(cloumname)) {
				a = (tuihuoRecord != null) ? tuihuoRecord.getTuihuochuzhantime() : "";
			} else if ("tuihuozhanrukutime".equals(cloumname)) {
				a = (tuihuoRecord != null) ? tuihuoRecord.getTuihuozhanrukutime() : "";
			} else if ("tuigonghuoshangchukutime".equals(cloumname)) {
				a = allTime.get("tuigonghuoshangchukutime") == null ? "" : allTime.get("tuigonghuoshangchukutime");
			} else if ("zhongzhuanzhangintime".equals(cloumname)) {
				// 中转站入库时间
				a = allTime.get("zhongzhuanzhanIntime") == null ? "" : allTime.get("zhongzhuanzhanIntime");
			} else if ("zhongzhuanzhangouttime".equals(cloumname)) {
				// 中转站出库时间
				a = allTime.get("zhongzhuanzhanOuttime") == null ? "" : allTime.get("zhongzhuanzhanOuttime");
			} else if ("customerbrackhouseremark".equals(cloumname)) {
				a = allTime.get("customerbrackhouseremark") == null ? "" : allTime.get("customerbrackhouseremark");
			} else if ("instoreroomtime".equals(cloumname)) {
				a = allTime.get("Instoreroomtime") == null ? "" : allTime.get("Instoreroomtime");
			} else if ("inSitetime".equals(cloumname)) {
				a = allTime.get("InSitetime") == null ? "" : allTime.get("InSitetime");

			} else if ("outSitetime".equals(cloumname)) {
				a = allTime.get("OutSitetime") == null ? "" : allTime.get("OutSitetime");
			} else if ("pickGoodstime".equals(cloumname)) {
				a = allTime.get("PickGoodstime") == null ? "" : allTime.get("PickGoodstime");
			} else if ("outstoreroomtime".equals(cloumname)) {
				a = allTime.get("Outstoreroomtime") == null ? "" : allTime.get("Outstoreroomtime");
			} else if ("goclasstime".equals(cloumname)) {
				a = allTime.get("Goclasstime") == null ? "" : allTime.get("Goclasstime");
			} else if ("newchangetime".equals(cloumname)) {
				a = allTime.get("Newchangetime") == null ? "" : allTime.get("Newchangetime");
			} else if ("gobacktime".equals(cloumname)) {
				a = allTime.get("Gobacktime") == null ? "" : allTime.get("Gobacktime");
			} else if ("podremarkStr".equals(cloumname)) {
				a = "";
				if ((reasonList.size() > 0) && (ds != null)) {
					for (Reason reason : reasonList) {
						if (ds.getPodremarkid() == reason.getReasonid()) {
							a = reason.getReasoncontent();
						}
					}
				}
			} else if ("leavedreason".equals(cloumname)) {
				a = mapRow.get("leavedreason") == null ? "" : mapRow.get("leavedreason");
			} else if ("losereason".equals(cloumname)) {
				a = mapRow.get("losereason") == null ? "" : mapRow.get("losereason");

			} else if ("weishuakareason".equals(cloumname)) {
				a = mapRow.get("weishuakareason") == null ? "" : mapRow.get("weishuakareason");

			} else if ("resendtime".equals(cloumname)) {
				a = mapRow.get("resendtime") == null ? "" : mapRow.get("resendtime");
			} else if ("backreason".equals(cloumname)) {
				a = mapRow.get("backreason") == null ? "" : mapRow.get("backreason");
			} else if ("customerid".equals(cloumname)) {
				a = cMap.get(Long.parseLong(mapRow.get("Customerid").toString())) == null ? "" : cMap.get(Long.parseLong(mapRow.get("Customerid").toString())).getCustomername();
			} else if ("startbranchname".equals(cloumname)) {
				a = "";
				for (Branch b : bList) {
					if (b.getBranchid() == Long.parseLong(mapRow.get("startbranchid").toString())) {
						a = b.getBranchname();
						break;
					}
				}
			} else if ("nextbranchname".equals(cloumname)) {
				a = "";
				for (Branch b : bList) {
					if (b.getBranchid() == Long.parseLong(mapRow.get("nextbranchid").toString())) {
						a = b.getBranchname();
						break;
					}
				}
			}

			// TODO enum and map prov
			else if ("branchprovince".equals(cloumname)) {
				a = currentb.getBranchprovince();
			} else if ("branchcity".equals(cloumname)) {
				a = currentb.getBranchcity();
			} else if ("brancharea".equals(cloumname)) {
				a = currentb.getBrancharea();
			} else if ("branchstreet".equals(cloumname)) {
				a = currentb.getBranchstreet();
			}

			else if ("fdelivername".equals(cloumname)) {
				a = "";
				if (ds != null) {
					for (User u : uList) {
						if (u.getUserid() == ds.getDeliveryid()) {
							a = u.getRealname();
							if (u.getEmployeestatus() == UserEmployeestatusEnum.LiZhi.getValue()) {
								a = a + "(离职)";
							}
							break;
						}
					}
				}
			} else if ("dReceivedfee".equals(cloumname)) {
				a = (ds != null) ? ds.getReceivedfee() : "";
			} else if ("deliverid".equals(cloumname)) {
				a = "";
				for (User u : uList) {
					if (u.getUserid() == Long.parseLong(mapRow.get("deliverid").toString())) {
						a = u.getRealname();
						if (u.getEmployeestatus() == UserEmployeestatusEnum.LiZhi.getValue()) {
							a = a + "(离职)";
						}
						break;
					}
				}
			} else if ("commonid".equals(cloumname)) {
				a = "";
				for (Common c : commonList) {
					if (c.getId() == Long.parseLong(mapRow.get("commonid").toString())) {
						a = c.getCommonname();
						break;
					}
				}
			} else if ("commonnumber".equals(cloumname)) {
				a = "";
				for (Common c : commonList) {
					if (c.getId() == Long.parseLong(mapRow.get("commonid").toString())) {
						a = c.getCommonnumber();
						break;
					}
				}
			} else if ("cash".equals(cloumname)) {
				a = (ds != null) ? ds.getCash() : "";
			} else if ("pos".equals(cloumname)) {
				a = (ds != null) ? ds.getPos() : "";
			} else if ("receivedfee".equals(cloumname)) {
				a = (ds != null) ? ds.getReceivedfee() : "";
			} else if ("returnedfee".equals(cloumname)) {
				a = (ds != null) ? ds.getReturnedfee() : "";
			} else if ("checkfee".equals(cloumname)) {
				a = (ds != null) ? ds.getCheckfee() : "";
			} else if ("backcarname".equals(cloumname)) {
				a = mapRow.get("backcarname");
			} else if ("otherfee".equals(cloumname)) {
				a = (ds != null) ? ds.getOtherfee() : "";
			} else if ("signinman".equals(cloumname)) {
				a = "";
				if ((ds != null)
						&& ((ds.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue()) || (ds.getDeliverystate() == DeliveryStateEnum.ShangMenHuanChengGong.getValue()) || (ds
								.getDeliverystate() == DeliveryStateEnum.ShangMenTuiChengGong.getValue()))) {
					a = ds.getSign_man().length() == 0 ? mapRow.get("consigneename") : ds.getSign_man();

				}
			} else if ("signintime".equals(cloumname)) {
				a = "";
				if ((ds != null)
						&& ((ds.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue()) || (ds.getDeliverystate() == DeliveryStateEnum.ShangMenHuanChengGong.getValue()) || (ds
								.getDeliverystate() == DeliveryStateEnum.ShangMenTuiChengGong.getValue()))) {
					a = allTime.get("Gobacktime") == null ? "" : allTime.get("Gobacktime");
				}
			} else if ("excelbranch".equals(cloumname)) {
				a = "";
				for (Branch b : bList) {
					if (b.getBranchid() == Long.parseLong(mapRow.get("deliverybranchid").toString())) {
						a = b.getBranchname();
						break;
					}
				}
			} else if ("currentbranchname".equals(cloumname)) {
				a = "";
				for (Branch b : bList) {
					if (b.getBranchid() == Long.parseLong(mapRow.get("currentbranchid").toString())) {
						a = b.getBranchname();
						break;
					}
				}
			} else if ("deliverybranchname".equals(cloumname)) {
				a = "";
				if (ds != null) {
					for (Branch b : bList) {
						if (b.getBranchid() == ds.getDeliverybranchid()) {
							a = b.getBranchname();
							break;
						}
					}
				}
			} else if ("realbranchname".equals(cloumname)) {
				a = "";
				long realbranchid = Long.parseLong(mapRow.get("currentbranchid").toString());
				if (realbranchid == 0) {
					realbranchid = Long.parseLong(mapRow.get("startbranchid").toString());
				}
				for (Branch b : bList) {
					if (b.getBranchid() == realbranchid) {
						a = b.getBranchname();
						break;
					}
				}
			} else if ("realflowordertype".equals(cloumname)) {
				long realbranchid = Long.parseLong(mapRow.get("currentbranchid").toString());
				long nextbranchid = Long.parseLong(mapRow.get("nextbranchid").toString());
				if (realbranchid == 0) {
					realbranchid = Long.parseLong(mapRow.get("startbranchid").toString());
				}
				a = this.getRealflowordertype(bList, realbranchid, Long.parseLong(mapRow.get("flowordertype").toString()), nextbranchid);
			} else if ("ispayup".equals(cloumname)) {
				/*
				 * if ((cwbspayupidMap != null) && (cwbspayupidMap.size() > 0)
				 * && (cwbspayupidMap.get(mapRow.get("cwb").toString()) !=
				 * null)) { a =
				 * cwbspayupidMap.get(mapRow.get("cwb").toString()); }
				 */
				if (Long.parseLong(mapRow.get("fnorgoffsetflag").toString()) == 1) {
					a = "是";
				} else {
					a = "否";
				}
			} else if ("complaintContent".equals(cloumname)) {
				// 投诉受理-客服备注
				a = "";
				if ((complaintMap != null) && (complaintMap.size() > 0) && (complaintMap.get(mapRow.get("cwb").toString()) != null)) {
					a = complaintMap.get(mapRow.get("cwb").toString());
				}
			} else {
				a = mapRow.get(cloumname);
				a = this.setAbyUser(a, cloumname);
			}
		} catch (Exception e) {
			// System.out.println(cloumname);
		}
		// System.out.println("pp:"+System.currentTimeMillis());
		return a;

	}

	private Object setAbyUser(Object a, String cloumname) {
		if (cloumname.equals("consigneename")) {
			if (this.getSessionUser().getShownameflag() != 1) {
				a = "******";
			}
		}
		if (cloumname.equals("consigneephone")) {
			if (this.getSessionUser().getShowphoneflag() != 1) {
				a = "******";
			}
		}
		if (cloumname.equals("consigneemobile")) {
			if (this.getSessionUser().getShowmobileflag() != 1) {
				a = "******";
			}
		}
		return a;
	}

	public Object setObjectB(String[] cloumnName3, CwbOrder co, TuihuoRecord tuihuo, int i, List<User> uList, Map<Long, Customer> cMap, List<Branch> bList, List<Common> commonList, DeliveryState ds,
			Map<String, String> allTime, List<CustomWareHouse> cWList, Map<String, Map<String, String>> remarkMap, List<Reason> reasonList, String cwbspayup) {
		Object a = null;
		String cloumname = cloumnName3[i];
		cloumname = cloumnName3[i].substring(0, 1).toLowerCase() + cloumnName3[i].substring(1, cloumnName3[i].length());
		try {
			Branch currentb = new Branch();
			if (ds != null) {
				for (Branch b : bList) {
					if (b.getBranchid() == ds.getDeliverybranchid()) {
						currentb = b;
						break;
					}
				}
			}
			if ("orderType".equals(cloumname)) {
				a = co.getCwbordertypeid();
				for (CwbOrderTypeIdEnum f : CwbOrderTypeIdEnum.values()) {
					if ("".equals(a)) {
						a = "配送";
					} else if (a.equals("-1")) {
						a = "配送";
					} else if (f.getValue() == Integer.parseInt(a.toString())) {
						a = f.getText();
						break;
					}
				}
			} else if ("flowordertypeMethod".equals(cloumname)) {
				a = co.getFlowordertype();
				for (FlowOrderTypeEnum fote : FlowOrderTypeEnum.values()) {
					if (fote.getValue() == Integer.parseInt(a.toString())) {
						return fote.getText();
					}
				}
			} else if ("paytypeName".equals(cloumname)) {
				a = this.advancedQueryService.getPayWayType(co.getCwb(), co.getPaywayid() + "");
			} else if ("newpayway".equals(cloumname)) {
				a = this.advancedQueryService.getPayWayType(co.getCwb(), co.getNewpaywayid());
			} else if ("statisticstateStr".equals(cloumname)) {
				a = "";
				for (DeliveryStateEnum dse : DeliveryStateEnum.values()) {
					if (dse.getValue() == ds.getDeliverystate()) {
						a = dse.getText();
						break;
					}
				}
			} else if ("orderResultTypeText".equals(cloumname)) {
				a = "";
				for (DeliveryStateEnum fote : DeliveryStateEnum.values()) {
					if (fote.getValue() == ds.getDeliverystate()) {
						a = fote.getText();
						break;
					}
				}
			} else if ("carwarehouse".equals(cloumname)) {
				a = "";
				for (Branch b : bList) {
					if (b.getBranchid() == Long.parseLong(((co.getCarwarehouse() == null) || "".equals(co.getCarwarehouse())) ? "0" : co.getCarwarehouse())) {
						a = b.getBranchname();
						break;
					}
				}
			} else if ("customerwarehouseid".equals(cloumname)) {
				a = "";
				for (CustomWareHouse b : cWList) {
					if (b.getWarehouseid() == Long.parseLong(((co.getCustomerwarehouseid() == null) || "".equals(co.getCustomerwarehouseid())) ? "0" : co.getCustomerwarehouseid())) {
						a = b.getCustomerwarehouse();
						break;
					}
				}
			} else if ("inwarhouseRemark".equals(cloumname)) {
				a = remarkMap.get(co.getCwb()) == null ? "" : remarkMap.get(co.getCwb()).get(ReasonTypeEnum.RuKuBeiZhu.getText());
			} else if ("posremark".equals(cloumname)) {
				a = ds.getPosremark();
			} else if ("checkremark".equals(cloumname)) {
				a = ds.getCheckremark();
			} else if ("deliverstateremark".equals(cloumname)) {
				a = ds.getDeliverstateremark();
			} else if ("businessfee".equals(cloumname)) {
				a = ds.getBusinessfee();
			} else if ("receivedfeeuserName".equals(cloumname)) {
				a = "";
				for (User u : uList) {
					if (u.getUserid() == ds.getReceivedfeeuser()) {
						a = u.getRealname();
						break;
					}
				}
			} else if ("customerbrackhouseremark".equals(cloumname)) {
				a = allTime.get("customerbrackhouseremark") == null ? "" : allTime.get("customerbrackhouseremark");
			} else if ("tuigonghuoshangchukutime".equals(cloumname)) {
				a = allTime.get("tuigonghuoshangchukutime") == null ? "" : allTime.get("tuigonghuoshangchukutime");
			} else if ("instoreroomtime".equals(cloumname)) {
				a = allTime.get("Instoreroomtime") == null ? "" : allTime.get("Instoreroomtime");
			} else if ("inSitetime".equals(cloumname)) {
				a = allTime.get("InSitetime") == null ? "" : allTime.get("InSitetime");
			} else if ("outSitetime".equals(cloumname)) {
				a = allTime.get("OutSitetime") == null ? "" : allTime.get("OutSitetime");
			} else if ("pickGoodstime".equals(cloumname)) {
				a = allTime.get("PickGoodstime") == null ? "" : allTime.get("PickGoodstime");
			} else if ("outstoreroomtime".equals(cloumname)) {
				a = allTime.get("Outstoreroomtime") == null ? "" : allTime.get("Outstoreroomtime");
			} else if ("goclasstime".equals(cloumname)) {
				a = allTime.get("Goclasstime") == null ? "" : allTime.get("Goclasstime");
			} else if ("newchangetime".equals(cloumname)) {
				a = allTime.get("Newchangetime") == null ? "" : allTime.get("Newchangetime");
			} else if ("gobacktime".equals(cloumname)) {
				a = allTime.get("Gobacktime") == null ? "" : allTime.get("Gobacktime");
			} else if ("zhongzhuanzhangintime".equals(cloumname)) {
				// 中转站入库时间
				a = allTime.get("zhongzhuanzhanIntime") == null ? "" : allTime.get("zhongzhuanzhanIntime");
			} else if ("zhongzhuanzhangouttime".equals(cloumname)) {
				// 中转站出库时间
				a = allTime.get("zhongzhuanzhanOuttime") == null ? "" : allTime.get("zhongzhuanzhanOuttime");
			} else if ("podremarkStr".equals(cloumname)) {
				a = "";
				if (reasonList.size() > 0) {
					for (Reason reason : reasonList) {
						if (ds.getPodremarkid() == reason.getReasonid()) {
							a = reason.getReasoncontent();
						}
					}
				}
			} else if ("resendtime".equals(cloumname)) {
				a = co.getResendtime() == null ? "" : co.getResendtime();
			} else if ("leavedreason".equals(cloumname)) {
				a = co.getLeavedreason() == null ? "" : co.getLeavedreason();
			} else if ("backreason".equals(cloumname)) {
				a = co.getBackreason() == null ? "" : co.getBackreason();
			} else if ("losereason".equals(cloumname)) {
				a = co.getLosereason() == null ? "" : co.getLosereason();

			} else if ("tuihuochuzhantime".equals(cloumname)) {
				a = tuihuo.getTuihuochuzhantime() == null ? "" : tuihuo.getTuihuochuzhantime();
			} else if ("tuihuozhanrukutime".equals(cloumname)) {
				a = tuihuo.getTuihuozhanrukutime() == null ? "" : tuihuo.getTuihuozhanrukutime();
			} else if ("weishuakareason".equals(cloumname)) {
				a = co.getWeishuakareason() == null ? "" : co.getWeishuakareason();

			} else if ("customerid".equals(cloumname)) {
				a = cMap.get(co.getCustomerid()).getCustomername();
			} else if ("startbranchname".equals(cloumname)) {
				a = "";
				for (Branch b : bList) {
					if (b.getBranchid() == co.getStartbranchid()) {
						a = b.getBranchname();
						break;
					}
				}
			} else if ("nextbranchname".equals(cloumname)) {
				a = "";
				for (Branch b : bList) {
					if (b.getBranchid() == co.getNextbranchid()) {
						a = b.getBranchname();
						break;
					}
				}
			} else if ("fdelivername".equals(cloumname)) {
				a = "";
				for (User u : uList) {
					if (u.getUserid() == ds.getDeliveryid()) {
						a = u.getRealname();
						if (u.getEmployeestatus() == UserEmployeestatusEnum.LiZhi.getValue()) {
							a = a + "(离职)";
						}
						break;
					}
				}
			} else if ("dReceivedfee".equals(cloumname)) {
				a = ds.getReceivedfee();
			} else if ("deliverid".equals(cloumname)) {
				a = "";
				for (User u : uList) {
					if (u.getUserid() == co.getDeliverid()) {
						a = u.getRealname();
						if (u.getEmployeestatus() == UserEmployeestatusEnum.LiZhi.getValue()) {
							a = a + "(离职)";
						}
						break;
					}
				}
			} else if ("commonid".equals(cloumname)) {
				a = "";
				for (Common c : commonList) {
					if (c.getId() == co.getCommonid()) {
						a = c.getCommonname();
						break;
					}
				}
			} else if ("commonnumber".equals(cloumname)) {
				a = "";
				for (Common c : commonList) {
					if (c.getId() == co.getCommonid()) {
						a = c.getCommonnumber();
						break;
					}
				}
			} else if ("cash".equals(cloumname)) {
				a = ds.getCash();
			} else if ("pos".equals(cloumname)) {
				a = ds.getPos();
			} else if ("receivedfee".equals(cloumname)) {
				a = ds.getReceivedfee();
			} else if ("returnedfee".equals(cloumname)) {
				a = ds.getReturnedfee();
			} else if ("checkfee".equals(cloumname)) {
				a = ds.getCheckfee();
			} else if ("backcarname".equals(cloumname)) {
				a = co.getBackcarname();
			} else if ("otherfee".equals(cloumname)) {
				a = ds.getOtherfee();
			} else if ("signinman".equals(cloumname)) {
				a = "";
				if ((ds != null)
						&& ((ds.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue()) || (ds.getDeliverystate() == DeliveryStateEnum.ShangMenHuanChengGong.getValue()) || (ds
								.getDeliverystate() == DeliveryStateEnum.ShangMenTuiChengGong.getValue()))) {
					a = ds.getSign_man() == null ? "" : (ds.getSign_man().trim().length() == 0 ? co.getConsigneename() : ds.getSign_man());

				}
			} else if ("signintime".equals(cloumname)) {
				a = "";
				if ((ds != null)
						&& ((ds.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue()) || (ds.getDeliverystate() == DeliveryStateEnum.ShangMenHuanChengGong.getValue()) || (ds
								.getDeliverystate() == DeliveryStateEnum.ShangMenTuiChengGong.getValue()))) {
					a = ds.getSign_time() == null ? "" : ds.getSign_time();
				}
			} else if ("excelbranch".equals(cloumname)) {
				a = "";
				for (Branch b : bList) {
					if (b.getBranchid() == co.getDeliverybranchid()) {
						a = b.getBranchname();
						break;
					}
				}
			} else if ("currentbranchname".equals(cloumname)) {
				a = "";
				for (Branch b : bList) {
					if (b.getBranchid() == co.getCurrentbranchid()) {
						a = b.getBranchname();
						break;
					}
				}
			}

			else if ("branchprovince".equals(cloumname)) {
				a = currentb.getBranchprovince();
			} else if ("branchcity".equals(cloumname)) {
				a = currentb.getBranchcity();
			} else if ("brancharea".equals(cloumname)) {
				a = currentb.getBrancharea();
			} else if ("branchstreet".equals(cloumname)) {
				a = currentb.getBranchstreet();
			}

			else if ("deliverybranchname".equals(cloumname)) {
				a = "";
				for (Branch b : bList) {
					if (b.getBranchid() == ds.getDeliverybranchid()) {
						a = b.getBranchname();
						break;
					}
				}
			} else if ("realbranchname".equals(cloumname)) {
				a = "";
				long realbranchid = co.getCurrentbranchid();
				if (realbranchid == 0) {
					realbranchid = co.getStartbranchid();
				}
				for (Branch b : bList) {
					if (b.getBranchid() == realbranchid) {
						a = b.getBranchname();
						break;
					}
				}
			} else if ("realflowordertype".equals(cloumname)) {
				long realbranchid = co.getCurrentbranchid();
				if (realbranchid == 0) {
					realbranchid = co.getStartbranchid();
				}
				a = this.getRealflowordertype(bList, realbranchid, co.getFlowordertype(), co.getNextbranchid());
			} else if ("ispayup".equals(cloumname)) {
				if (cwbspayup != null) {
					a = cwbspayup;
				} else {
					a = "否";
				}
			} else {
				try {
					a = co.getClass().getMethod("get" + cloumnName3[i]).invoke(co);
				} catch (Exception e) {
					a = "";
					// System.out.println(cloumname);
				}
			}
		} catch (InvalidResultSetAccessException e) {
			// System.out.println(cloumname);
		}
		return a;

	}

	public Object setObjectB(String[] cloumnName3, List<User> userList, HttpServletRequest request1, List<ComplaintView> list, Object a, int i, int k) {
		try {
			if (cloumnName3[i].equals("Deliverystate")) {
				for (DeliveryStateEnum ds : DeliveryStateEnum.values()) {
					if (Long.parseLong(list.get(k).getClass().getMethod("get" + cloumnName3[i]).invoke(list.get(k)).toString()) == ds.getValue()) {
						a = ds.getText();
						break;
					}
				}
			} else if (cloumnName3[i].equals("Orderflowtype")) {
				for (FlowOrderTypeEnum ft : FlowOrderTypeEnum.values()) {
					if (Long.parseLong(list.get(k).getClass().getMethod("get" + cloumnName3[i]).invoke(list.get(k)).toString()) == ft.getValue()) {
						a = ft.getText();
						break;
					}
				}
			} else if (cloumnName3[i].equals("Type")) {
				for (ComplaintTypeEnum ct : ComplaintTypeEnum.values()) {
					if (Long.parseLong(list.get(k).getClass().getMethod("get" + cloumnName3[i]).invoke(list.get(k)).toString()) == ct.getValue()) {
						a = ct.getText();
						break;
					}
				}
			} else if (cloumnName3[i].equals("AuditType")) {
				for (ComplaintAuditTypeEnum cat : ComplaintAuditTypeEnum.values()) {
					if (Long.parseLong(list.get(k).getClass().getMethod("get" + cloumnName3[i]).invoke(list.get(k)).toString()) == cat.getValue()) {
						a = cat.getText();
						break;
					}
				}
			} else if (cloumnName3[i].equals("AuditUser")) {
				a = "";
				if (userList.size() > 0) {
					for (User u : userList) {
						if (Long.parseLong(list.get(k).getClass().getMethod("get" + cloumnName3[i]).invoke(list.get(k)).toString()) == u.getUserid()) {
							a = u.getRealname();
						}
					}
				}
			} else if (cloumnName3[i].equals("Deliveryid")) {
				a = "";
				if (userList.size() > 0) {
					for (User u : userList) {
						if (Long.parseLong(list.get(k).getClass().getMethod("get" + cloumnName3[i]).invoke(list.get(k)).toString()) == u.getUserid()) {
							a = u.getRealname();
						}
					}
				}
			} else {
				a = list.get(k).getClass().getMethod("get" + cloumnName3[i]).invoke(list.get(k));
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a;

	}

	public Object setAllpyTuiHuoObject(String[] cloumnName3, HttpServletRequest request1, List<CwbOrderView> list, Object a, int i, int k) {
		try {
			if (cloumnName3[i].equals("Deliverystate")) {
				for (DeliveryStateEnum ds : DeliveryStateEnum.values()) {
					if (Long.parseLong(list.get(k).getClass().getMethod("get" + cloumnName3[i]).invoke(list.get(k)).toString()) == ds.getValue()) {
						a = ds.getText();
						break;
					}
				}
			} else if (cloumnName3[i].equals("Applyishandle")) {
				a = "未处理";
				if (Long.parseLong(list.get(k).getClass().getMethod("get" + cloumnName3[i]).invoke(list.get(k)).toString()) == 1) {
					a = "已处理";
				}
			} else {
				a = list.get(k).getClass().getMethod("get" + cloumnName3[i]).invoke(list.get(k));
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a;
	}

	public Object setAllpyZhongZhuanObject(String[] cloumnName3, HttpServletRequest request1, List<CwbOrderView> list, Object a, int i, int k) {
		try {
			if (cloumnName3[i].equals("Deliverystate")) {
				for (DeliveryStateEnum ds : DeliveryStateEnum.values()) {
					if (Long.parseLong(list.get(k).getClass().getMethod("get" + cloumnName3[i]).invoke(list.get(k)).toString()) == ds.getValue()) {
						a = ds.getText();
						break;
					}
				}
			} else if (cloumnName3[i].equals("Applyzhongzhuanishandle")) {
				a = "未处理";
				if (Long.parseLong(list.get(k).getClass().getMethod("get" + cloumnName3[i]).invoke(list.get(k)).toString()) == 1) {
					a = "已处理";
				}
			} else {
				a = list.get(k).getClass().getMethod("get" + cloumnName3[i]).invoke(list.get(k));
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a;
	}

	public void SetCwbKuaiDiFields(String[] cloumnName1, String[] cloumnName2) {
		// 单号 揽收人 揽收站点 揽收时间 收件人 收件人手机号 收件人地址 费用总计 当前状态 备注
		cloumnName1[0] = "快递单号";
		cloumnName2[0] = "Cwb";
		cloumnName1[1] = "揽收人";
		cloumnName2[1] = "Lanshouusername";
		cloumnName1[2] = "揽收站点";
		cloumnName2[2] = "Lanshoubranchname";
		cloumnName1[3] = "揽收时间";
		cloumnName2[3] = "Lanshoutime";
		cloumnName1[4] = "收件人";
		cloumnName2[4] = "Consigneename";
		cloumnName1[5] = "收件人手机号";
		cloumnName2[5] = "Consigneemobile";
		cloumnName1[6] = "收件人地址";
		cloumnName2[6] = "Consigneeaddress";
		cloumnName1[7] = "费用总计";
		cloumnName2[7] = "Allfee";
		cloumnName1[8] = "当前状态";
		cloumnName2[8] = "Flowordertype";
		cloumnName1[9] = "备注";
		cloumnName2[9] = "Remark";
	}

	public Object setCwbKuaiDiObject(String[] cloumnName3, HttpServletRequest request1, List<CwbKuaiDiView> list, Object a, int i, int k) {
		try {
			if (cloumnName3[i].equals("Flowordertype")) {
				for (FlowOrderTypeEnum ft : FlowOrderTypeEnum.values()) {
					if (Long.parseLong(list.get(k).getClass().getMethod("get" + cloumnName3[i]).invoke(list.get(k)).toString()) == ft.getValue()) {
						a = ft.getText();
						break;
					}
				}
			} else {
				a = list.get(k).getClass().getMethod("get" + cloumnName3[i]).invoke(list.get(k));
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a;
	}

	public void SetDeliverPayupFields(String[] cloumnName1, String[] cloumnName2) {
		cloumnName1[0] = "站点";
		cloumnName2[0] = "Branchid";
		cloumnName1[1] = "小件员";
		cloumnName2[1] = "Deliverealuser";
		cloumnName1[2] = "交款时间";
		cloumnName2[2] = "Auditingtime";
		cloumnName1[3] = "交款方式";
		cloumnName2[3] = "Deliverpayuptype";

		cloumnName1[4] = "现金应收款";
		cloumnName2[4] = "Payupamount";
		cloumnName1[5] = "现金已交款";
		cloumnName2[5] = "Deliverpayupamount";
		cloumnName1[6] = "小件员交款时现金余额";
		cloumnName2[6] = "DeliverAccount";
		cloumnName1[7] = "现金欠款";
		cloumnName2[7] = "Deliverpayuparrearage";

		cloumnName1[8] = "POS应收款";
		cloumnName2[8] = "Payupamount_pos";
		cloumnName1[9] = "POS已交款";
		cloumnName2[9] = "Deliverpayupamount_pos";
		cloumnName1[10] = "小件员交款时POS余额";
		cloumnName2[10] = "DeliverPosAccount";
		cloumnName1[11] = "POS欠款";
		cloumnName2[11] = "Deliverpayuparrearage_pos";

		cloumnName1[12] = "审核备注";
		cloumnName2[12] = "Checkremark";
		cloumnName1[13] = "审核状态";
		cloumnName2[13] = "Deliverpayupapproved";
		cloumnName1[14] = "交款地址";
		cloumnName2[14] = "Payupaddress";
		cloumnName1[15] = "交易流水号";
		cloumnName2[15] = "Deliverpayupbanknum";
	}

	public Object setObjectC(String[] cloumnName3, HttpServletRequest request1, List<GotoClassAuditing> list, Object a, int i, int k, List<Branch> bList, List<User> uList) {
		try {
			if (cloumnName3[i].equals("Branchid")) {
				for (Branch b : bList) {
					if (list.get(k).getBranchid() == b.getBranchid()) {
						a = b.getBranchname();
						break;
					}
				}
			} else if (cloumnName3[i].equals("Deliverealuser")) {
				for (User u : uList) {
					if (list.get(k).getDeliverealuser() == u.getUserid()) {
						a = u.getRealname();
						break;
					}
				}
			} else if (cloumnName3[i].equals("Deliverpayuptype")) {
				a = list.get(k).getDeliverpayuptypeStr();
			} else if (cloumnName3[i].equals("Deliverpayupbanknum")) {
				a = (list.get(k).getDeliverpayupbanknum() == null) || "".equals(list.get(k).getDeliverpayupbanknum()) ? "无" : list.get(k).getDeliverpayupbanknum();
			} else if (cloumnName3[i].equals("Payupamount")) {
				a = list.get(k).getPayupamount();
			} else if (cloumnName3[i].equals("Deliverpayupamount")) {
				a = list.get(k).getDeliverpayupamount();
			} else if (cloumnName3[i].equals("DeliverAccount")) {
				a = list.get(k).getDeliverAccount();
			} else if (cloumnName3[i].equals("Deliverpayuparrearage")) {
				a = list.get(k).getDeliverpayuparrearage().negate();

			} else if (cloumnName3[i].equals("Payupamount_pos")) {
				a = list.get(k).getPayupamount_pos();
			} else if (cloumnName3[i].equals("Deliverpayupamount_pos")) {
				a = list.get(k).getDeliverpayupamount_pos();
			} else if (cloumnName3[i].equals("DeliverPosAccount")) {
				a = list.get(k).getDeliverPosAccount();
			} else if (cloumnName3[i].equals("Deliverpayuparrearage_pos")) {
				a = list.get(k).getDeliverpayuparrearage_pos().negate();

			} else if (cloumnName3[i].equals("Auditingtime")) {
				a = list.get(k).getClass().getMethod("get" + cloumnName3[i]).invoke(list.get(k)).toString();
			} else if (cloumnName3[i].equals("Payupaddress")) {
				a = list.get(k).getClass().getMethod("get" + cloumnName3[i]).invoke(list.get(k)).toString();
			} else if (cloumnName3[i].equals("Checkremark")) {
				a = list.get(k).getClass().getMethod("get" + cloumnName3[i]).invoke(list.get(k)).toString();
			} else if (cloumnName3[i].equals("Deliverpayupapproved")) {
				for (DeliverPayupArrearageapprovedEnum de : DeliverPayupArrearageapprovedEnum.values()) {
					if (de.getValue() == Long.parseLong(list.get(k).getClass().getMethod("get" + cloumnName3[i]).invoke(list.get(k)).toString())) {
						a = de.getText();
						break;
					}
				}
			} else {
				a = list.get(k).getClass().getMethod("get" + cloumnName3[i]).invoke(list.get(k));
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a;

	}

	public String getRealflowordertype(List<Branch> bList, long branchid, long flowordertype, long nextbranchid) {
		// 当前站点类型
		long sitetype = 0;
		// 下一站点类型
		long nextbranchsitetype = 0;
		for (Branch b : bList) {
			if (b.getBranchid() == branchid) {
				sitetype = b.getSitetype();
			}
			if (b.getBranchid() == nextbranchid) {
				nextbranchsitetype = b.getSitetype();
			}
		}
		if (flowordertype == FlowOrderTypeEnum.DaoRuShuJu.getValue()) {
			return RealFlowOrderTypeEnum.DaoRuShuJu.getText();
		} else if (flowordertype == FlowOrderTypeEnum.TiHuo.getValue()) {
			return RealFlowOrderTypeEnum.TiHuo.getText();
		} else if (flowordertype == FlowOrderTypeEnum.TiHuoYouHuoWuDan.getValue()) {
			return RealFlowOrderTypeEnum.TiHuo.getText();
		} else if ((flowordertype == FlowOrderTypeEnum.RuKu.getValue()) && (sitetype == BranchEnum.KuFang.getValue())) {
			return RealFlowOrderTypeEnum.KuFangRuKu.getText();
		} else if (((flowordertype == FlowOrderTypeEnum.RuKu.getValue()) && (sitetype == BranchEnum.ZhongZhuan.getValue())) || (flowordertype == FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue())) {
			return RealFlowOrderTypeEnum.ZhongZhuanZhanRuKu.getText();
		} else if (flowordertype == FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()) {
			return RealFlowOrderTypeEnum.TuiHuoZhanRuKu.getText();
		} else if ((flowordertype == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) && (sitetype == BranchEnum.KuFang.getValue())) {
			return RealFlowOrderTypeEnum.KuFangChuKuSaoMiao.getText();
		} else if ((flowordertype == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) && (sitetype == BranchEnum.ZhanDian.getValue()) && (nextbranchsitetype == BranchEnum.ZhongZhuan.getValue())) {
			// 中转出站
			return RealFlowOrderTypeEnum.ZhongZhuanChuZhanSaoMiao.getText();
		} else if ((flowordertype == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) && (sitetype == BranchEnum.ZhanDian.getValue()) && (nextbranchsitetype == BranchEnum.ZhanDian.getValue())) {
			// 站点出站
			return RealFlowOrderTypeEnum.ZhanDianChuZhanSaoMiao.getText();
		} else if (((flowordertype == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) && (sitetype == BranchEnum.ZhongZhuan.getValue()))
				|| (flowordertype == FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue())) {
			return RealFlowOrderTypeEnum.ZhongZhuanZhanChuKuSaoMiao.getText();
		} else if ((flowordertype == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) && (sitetype == BranchEnum.TuiHuo.getValue())) {
			return RealFlowOrderTypeEnum.TuiHuoZhanChuKuSaoMiao.getText();
		} else if (flowordertype == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) {
			return RealFlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getText();
		} else if ((flowordertype == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()) && (sitetype == BranchEnum.KuFang.getValue())) {
			return RealFlowOrderTypeEnum.KuFangYouHuoWuDanSaoMiao.getText();
		} else if (flowordertype == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()) {
			return RealFlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getText();
		} else if ((flowordertype == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()) && (sitetype == BranchEnum.ZhongZhuan.getValue())) {
			return RealFlowOrderTypeEnum.ZhongZhuanZhanYouHuoWuDanSaoMiao.getText();
		} else if ((flowordertype == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()) && (sitetype == BranchEnum.TuiHuo.getValue())) {
			return RealFlowOrderTypeEnum.TuiHuoZhanYouHuoWuDanSaoMiao.getText();
		} else if (flowordertype == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
			return RealFlowOrderTypeEnum.FenZhanLingHuo.getText();
		} else if (flowordertype == FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue()) {
			return RealFlowOrderTypeEnum.TuiGongYingShangChuKu.getText();
		} else if (flowordertype == FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue()) {
			return RealFlowOrderTypeEnum.GongYingShangJuShouFanKu.getText();
		} else if (flowordertype == FlowOrderTypeEnum.CheXiaoFanKui.getValue()) {
			return RealFlowOrderTypeEnum.CheXiaoFanKui.getText();
		} else if (flowordertype == FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong.getValue()) {
			return RealFlowOrderTypeEnum.GongHuoShangTuiHuoChenggong.getText();
		} else if (flowordertype == FlowOrderTypeEnum.YiFanKui.getValue()) {
			return RealFlowOrderTypeEnum.YiFanKui.getText();
		} else if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()) {
			return RealFlowOrderTypeEnum.YiShenHe.getText();
		} else if (flowordertype == FlowOrderTypeEnum.UpdateDeliveryBranch.getValue()) {
			return RealFlowOrderTypeEnum.UpdateDeliveryBranch.getText();
		} else if (flowordertype == FlowOrderTypeEnum.DaoCuoHuoChuLi.getValue()) {
			return RealFlowOrderTypeEnum.DaoCuoHuoChuLi.getText();
		} else if (flowordertype == FlowOrderTypeEnum.BeiZhu.getValue()) {
			return RealFlowOrderTypeEnum.BeiZhu.getText();
		} else if (flowordertype == FlowOrderTypeEnum.TuiHuoChuZhan.getValue()) {
			return RealFlowOrderTypeEnum.TuiHuoChuZhan.getText();
		} else if (flowordertype == FlowOrderTypeEnum.ShouGongXiuGai.getValue()) {
			return RealFlowOrderTypeEnum.ShouGongXiuGai.getText();
		} else if (flowordertype == FlowOrderTypeEnum.PosZhiFu.getValue()) {
			return RealFlowOrderTypeEnum.PosZhiFu.getText();
		} else if (flowordertype == FlowOrderTypeEnum.YiChangDingDanChuLi.getValue()) {
			return RealFlowOrderTypeEnum.YiChangDingDanChuLi.getText();
		} else if (flowordertype == FlowOrderTypeEnum.DingDanLanJie.getValue()) {
			return RealFlowOrderTypeEnum.DingDanLanJie.getText();
		} else if (flowordertype == FlowOrderTypeEnum.ShenHeWeiZaiTou.getValue()) {
			return RealFlowOrderTypeEnum.ShenHeWeiZaiTou.getText();
		}
		return "";
	}

	public Map<String, Map<String, String>> getInwarhouseRemarks(List<Remark> remarkList) {
		Map<String, Map<String, String>> cwbMap = new HashMap<String, Map<String, String>>();

		if (remarkList.size() > 0) {
			for (Remark remark : remarkList) {
				for (ReasonTypeEnum rt : ReasonTypeEnum.values()) {
					Map<String, String> reasonTypeMap = new HashMap<String, String>();
					if (remark.getRemarktype() == rt.getText()) {
						String remarks = cwbMap.get(remark.getCwb()) == null ? "" : cwbMap.get(remark.getCwb()).get(rt.getText()) + "," + remark.getRemark();
						reasonTypeMap.put(rt.getText(), remarks);
					}
					cwbMap.put(remark.getCwb(), reasonTypeMap);
				}
			}
		}
		return cwbMap;
	}

	// =============站点导出=================
	public void SetDeliverPayupFiel(String[] cloumnName1, String[] cloumnName2) {
		cloumnName1[0] = "站点";
		cloumnName2[0] = "Branchid";
		cloumnName1[1] = "站点上缴时间";
		cloumnName2[1] = "credatetime";
		cloumnName1[2] = "员工交款日期";
		cloumnName2[2] = "Auditingtime";
		cloumnName1[3] = "票数";
		cloumnName2[3] = "count";
		cloumnName1[4] = "当日应上缴";
		cloumnName2[4] = "mustPay";
		cloumnName1[5] = "当日实收（不含POS）";
		cloumnName2[5] = "payupamount";
		cloumnName1[6] = "现金实收[元]";
		cloumnName2[6] = "Account";
		cloumnName1[7] = "其他款项";
		cloumnName2[7] = "otherpayup";
		cloumnName1[8] = "累计欠款";
		cloumnName2[8] = "arrange";
		cloumnName1[9] = "POS实收";
		cloumnName2[9] = "amount_pos";
		cloumnName1[10] = "支票实收";
		cloumnName2[10] = "zhiAccount";
		cloumnName1[11] = "当日实收（含POS）";
		cloumnName2[11] = "Deliverpayupamount_pos";
		cloumnName1[12] = "上交款审核备注";
		cloumnName2[12] = "Checkremark";
		cloumnName1[13] = "上交款备注";
		cloumnName2[13] = "remark";
		cloumnName1[14] = "上交款方式";
		cloumnName2[14] = "Payupway";
		cloumnName1[15] = "交款类型";
		cloumnName2[15] = "type";
		cloumnName1[16] = "上交款人";
		cloumnName2[16] = "userup";
		cloumnName1[17] = "审核人";
		cloumnName2[17] = "Sureman";
		cloumnName1[18] = "审核时间";
		cloumnName2[18] = "sureTime";

	}

	public Object setexportC(String[] cloumnName3, HttpServletRequest request1, List<NewForExportJson> list, List<PayUp> plist, Object a, int i, int k, List<Branch> bList, List<User> uList) {
		try {
			if (cloumnName3[i].equals("Branchid")) {
				for (Branch b : bList) {
					if (plist.get(k).getBranchid() == b.getBranchid()) {
						a = b.getBranchname();
						break;
					}
				}
			} else

			if (cloumnName3[i].equals("credatetime")) {
				a = plist.get(k).getCredatetime();// 缴款时间
			} else if (cloumnName3[i].equals("Auditingtime")) {// 员工缴款时间
				a = plist.get(k).getCredatetime().substring(0, 10);
			} else if (cloumnName3[i].equals("count")) {// 票数
				a = list.get(k).getAduitJson().getInt("countCwb");
			} else if (cloumnName3[i].equals("mustPay")) {// 当日应上缴
				a = plist.get(k).getAmount();
			} else if (cloumnName3[i].equals("payupamount")) {// 当日实收不含（pos）
				a = list.get(k).getAduitJson().getDouble("sumCash");

			} else if (cloumnName3[i].equals("Account")) {// 现金实收
				a = list.get(k).getAduitJson().getDouble("sumCash") - list.get(k).getAduitJson().getDouble("sumOrderfee") - list.get(k).getAduitJson().getDouble("sumCheckfee");
			} else if (cloumnName3[i].equals("otherpayup")) {// 其他款项
				a = list.get(k).getAduitJson().getDouble("sumOrderfee");
			} else if (cloumnName3[i].equals("arrange")) {// 累计欠款
				a = 0;
				BigDecimal totalfee = BigDecimal.ZERO;
				Map<Long, BigDecimal> branchTotalFee = new HashMap<Long, BigDecimal>();
				for (Branch b : bList) {
					if (plist.get(k).getBranchid() == b.getBranchid()) {
						branchTotalFee.put(b.getBranchid(), b.getArrearagepayupaudit().add(b.getPosarrearagepayupaudit()));
					}
				}
				List<Long> keys = new ArrayList<Long>(branchTotalFee.keySet());
				for (int m = 0; i < keys.size(); i++) {
					totalfee = totalfee.add(branchTotalFee.get(keys.get(m)));
				}
				a = totalfee;
			} else if (cloumnName3[i].equals("amount_pos")) {// pos实收
				a = plist.get(k).getAmountPos();
			} else if (cloumnName3[i].equals("zhiAccount")) {// 支票实收
				a = list.get(k).getAduitJson().getDouble("sumCheckfee");
			} else if (cloumnName3[i].equals("Deliverpayupamount_pos")) {
				a = plist.get(k).getAmountPos().add(plist.get(k).getAmount());// 当日实收
			} else if (cloumnName3[i].equals("Checkremark")) {
				a = plist.get(k).getAuditingremark();
			} else if (cloumnName3[i].equals("remark")) {// 上交款审核备注
				a = plist.get(k).getRemark();
			} else if (cloumnName3[i].equals("Payupway")) {// 上交款备注
				if ((plist.get(k).getWay()) == 1) {
					a = "银行转账";
				} else {
					a = "现金支付";
				}
			} else if (cloumnName3[i].equals("type")) {// 上交款方式
				if ((plist.get(k).getWay()) == 1) {
					a = "贷款";
				} else {
					a = "罚款";
				}
			} else if (cloumnName3[i].equals("userup")) {// 上交款类型
				a = plist.get(k).getUpuserrealname();
			} else if (cloumnName3[i].equals("besureman")) {// 交款人
				a = plist.get(k).getAuditinguser();
			} else if (plist.get(k).getAuditingtime() != null) {
				if (cloumnName3[i].equals("Sureman")) {
					a = plist.get(k).getAuditinguser();
				}
				if (cloumnName3[i].equals("sureTime")) {

					a = plist.get(k).getAuditingtime();
				}
			} else {
				a = "未审核";

			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a;

	}

	/*
	 * //问题件导出view public void SetAbnormalOrderFields(String[]
	 * cloumnName1,String[] cloumnName2){ cloumnName1[0]= "订单号"; cloumnName2[0]=
	 * "Cwb"; cloumnName1[1]= "供货商"; cloumnName2[1]= "Customername";
	 * cloumnName1[2]= "发货时间"; cloumnName2[2]= "Emaildate"; cloumnName1[3]=
	 * "当前状态"; cloumnName2[3]= "FlowordertypeName"; cloumnName1[4]= "配送站点";
	 * cloumnName2[4]= "Deliverybranchname"; cloumnName1[5]= "反馈人";
	 * cloumnName2[5]= "Creusername"; cloumnName1[6]= "问题件类型"; cloumnName2[6]=
	 * "Abnormaltype"; cloumnName1[7]= "问题件说明"; cloumnName2[7]= "Describe"; }
	 */

	// 导出 user 用的 view
	public Object setUserObject(String[] cloumnName3, List<UserView> userViews, Object a, int i, int k) {
		try {
			if (cloumnName3[i].equals("userid")) {
				a = userViews.get(k).getUserid();
			} else if (cloumnName3[i].equals("realname")) {
				a = userViews.get(k).getRealname();
			} else if (cloumnName3[i].equals("branchname")) {
				a = userViews.get(k).getBranchname();
			} else if (cloumnName3[i].equals("rolename")) {
				a = userViews.get(k).getRolename();
			} else if (cloumnName3[i].equals("usermobile")) {
				a = userViews.get(k).getUsermobile();
			} else if (cloumnName3[i].equals("lasttime")) {
				a = userViews.get(k).getLastLoginTime();
			} else if (cloumnName3[i].equals("lastip")) {
				a = userViews.get(k).getLastLoginIp();
			} else if (cloumnName3[i].equals("employeestatus")) {
				a = userViews.get(k).getEmployeestatusName();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return a;
	}

	// set user view 用于导出
	public void SetUserFields(String[] cloumnName1, String[] cloumnName2) {
		cloumnName1[0] = "编号";
		cloumnName2[0] = "userid";
		cloumnName1[1] = "姓名";
		cloumnName2[1] = "realname";
		cloumnName1[2] = "所属机构";
		cloumnName2[2] = "branchname";
		cloumnName1[3] = "职位";
		cloumnName2[3] = "rolename";
		cloumnName1[4] = "手机";
		cloumnName2[4] = "usermobile";
		cloumnName1[5] = "最后登录时间";
		cloumnName2[5] = "lasttime";
		cloumnName1[6] = "最后登录IP";
		cloumnName2[6] = "lastip";
		cloumnName1[7] = "工作状态";
		cloumnName2[7] = "employeestatus";
	}

	public void SetDiuShiFields(String[] cloumnName1, String[] cloumnName2) {
		cloumnName1[0] = "订单号";
		cloumnName2[0] = "Cwb";
		cloumnName1[1] = "供货商";
		cloumnName2[1] = "Customername";
		cloumnName1[2] = "收件人";
		cloumnName2[2] = "Consigneename";
		cloumnName1[3] = "代收金额";
		cloumnName2[3] = "Receivedfee";
		cloumnName1[4] = "应退金额";
		cloumnName2[4] = "Paybackfee";
		cloumnName1[5] = "货物金额";
		cloumnName2[5] = "Caramount";
		cloumnName1[6] = "赔偿金额";
		cloumnName2[6] = "Payamount";
		cloumnName1[7] = "审核时间";
		cloumnName2[7] = "Shenhetime";
		cloumnName1[8] = "审核人";
		cloumnName2[8] = "ShenheName";
		cloumnName1[9] = " 审核人职位";
		cloumnName2[9] = "ShenheRole";
		cloumnName1[10] = "当前状态";
		cloumnName2[10] = "Flowordertype";
		cloumnName1[11] = "配送结果";
		cloumnName2[11] = "Deliverystate";

	}

	public Object setDiuShiObject(String[] cloumnName3, List<CwbDiuShiView> cwbDiuShiList, HttpServletRequest request1, Object a, int i, int k, List<Role> roleList) {
		try {
			if (cloumnName3[i].equals("ShenheRole")) {
				a = "";
				if (roleList.size() > 0) {
					for (Role r : roleList) {
						if (Long.parseLong(cwbDiuShiList.get(k).getClass().getMethod("get" + cloumnName3[i]).invoke(cwbDiuShiList.get(k)).toString()) == r.getRoleid()) {
							a = r.getRolename();
							break;
						}
					}
				}
			} else if (cloumnName3[i].equals("Deliverystate")) {
				for (DeliveryStateEnum ds : DeliveryStateEnum.values()) {
					if (Long.parseLong(cwbDiuShiList.get(k).getClass().getMethod("get" + cloumnName3[i]).invoke(cwbDiuShiList.get(k)).toString()) == ds.getValue()) {
						a = ds.getText();
						break;
					}
				}
			} else if (cloumnName3[i].equals("Flowordertype")) {
				for (FlowOrderTypeEnum ft : FlowOrderTypeEnum.values()) {
					if (Long.parseLong(cwbDiuShiList.get(k).getClass().getMethod("get" + cloumnName3[i]).invoke(cwbDiuShiList.get(k)).toString()) == ft.getValue()) {
						a = ft.getText();
						break;
					}
				}
			} else {
				a = cwbDiuShiList.get(k).getClass().getMethod("get" + cloumnName3[i]).invoke(cwbDiuShiList.get(k));
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a;

	}

	// 站点历史日志导出
	public void SetBranchLogFields(String[] cloumnName1, String[] cloumnName2) {
		cloumnName1[0] = "站点";
		cloumnName2[0] = "branchname";
		cloumnName1[1] = "昨日库存";
		cloumnName2[1] = "zuorikucun";
		cloumnName1[2] = "今日到货";
		cloumnName2[2] = "jinridaohuo";
		cloumnName1[3] = "今日应投";
		cloumnName2[3] = "jinriyingtou";
		cloumnName1[4] = "今日妥投";
		cloumnName2[4] = "jinrituotou";
		cloumnName1[5] = "今日出库";
		cloumnName2[5] = "jinrchuku";
		cloumnName1[6] = "拒收";
		cloumnName2[6] = "jushou";
		cloumnName1[7] = "分拣错误及转址";
		cloumnName2[7] = "zhongzhuan";
		cloumnName1[8] = "今日库存";
		cloumnName2[8] = "jinrikucun";
		cloumnName1[9] = "今日妥投率";
		cloumnName2[9] = "jinrituotourate";
		cloumnName1[10] = "次日应投量";
		cloumnName2[10] = "ciriyingtou";

	}

	public Object setBranchLogObject(String[] cloumnName3, List<BranchTodayLog> todayLogs, Map<Long, BranchTodayLog> tomorrwMap, Map<Long, String> branchnameMap, Object a, int i, int k) {
		try {
			if (cloumnName3[i].equals("branchname")) {
				a = branchnameMap.get(todayLogs.get(k).getBranchid());
			} else if (cloumnName3[i].equals("zuorikucun")) {
				a = todayLogs.get(k).getZuorikucun_count();
			} else if (cloumnName3[i].equals("jinridaohuo")) {
				a = todayLogs.get(k).getJinridaohuo_count();
			} else if (cloumnName3[i].equals("jinriyingtou")) {
				a = (todayLogs.get(k).getJinriyingtou_count() < todayLogs.get(k).getToday_fankui_linghuo() ? todayLogs.get(k).getToday_fankui_linghuo() : todayLogs.get(k).getJinriyingtou_count());
			} else if (cloumnName3[i].equals("jinrituotou")) {
				a = todayLogs.get(k).getJinrishoukuan_count();
			} else if (cloumnName3[i].equals("jushou")) {
				a = todayLogs.get(k).getToday_fankui_jushou() + todayLogs.get(k).getToday_fankui_bufenjushou();
			} else if (cloumnName3[i].equals("zhongzhuan")) {
				a = todayLogs.get(k).getToday_fankui_zhobgzhuan();
			} else if (cloumnName3[i].equals("jinrikucun")) {
				a = todayLogs.get(k).getJinrikucun_count();
			} else if (cloumnName3[i].equals("jinrichuku")) {
				a = todayLogs.get(k).getJinrichuku_count();
			} else if (cloumnName3[i].equals("jinrituotourate")) {
				BigDecimal b1 = new BigDecimal((todayLogs.get(k).getJinrishoukuan_count()) * 100);
				BigDecimal b2 = new BigDecimal((todayLogs.get(k).getJinriyingtou_count() < todayLogs.get(k).getToday_fankui_linghuo() ? todayLogs.get(k).getToday_fankui_linghuo() : todayLogs.get(k)
						.getJinriyingtou_count()));
				a = (todayLogs.get(k).getJinriyingtou_count()) == 0 ? "0" : b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
			} else if (cloumnName3[i].equals("ciriyingtou")) {
				BranchTodayLog todayLog = tomorrwMap.get(todayLogs.get(k).getBranchid());
				if (todayLog == null) {
					a = "";
				} else {
					a = (todayLog.getJinriyingtou_count() < todayLog.getToday_fankui_linghuo() ? todayLog.getToday_fankui_linghuo() : todayLog.getJinriyingtou_count());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return a;

	}

	public Object setAccountDelivery(String[] cloumnName3, HttpServletRequest request1, List<AccountCwbDetailView> list, Object a, int i, int k) {
		try {
			a = list.get(k).getClass().getMethod("get" + cloumnName3[i]).invoke(list.get(k));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return a;
	}

	/**
	 * 结算后付类型
	 * 
	 * @param cloumnName1
	 * @param cloumnName2
	 */
	public void SetAccountDeliveryFields(String[] cloumnName1, String[] cloumnName2) {
		// cloumnName1[0]= "类别"; cloumnName2[0]= "Typename";
		cloumnName1[0] = "订单号";
		cloumnName2[0] = "Cwb";
		cloumnName1[1] = "订单类型";
		cloumnName2[1] = "Cwbtype";
		cloumnName1[2] = "发货件数";
		cloumnName2[2] = "Sendcarnum";
		cloumnName1[3] = "出库件数";
		cloumnName2[3] = "Scannum";
		cloumnName1[4] = "货物价值[元]";
		cloumnName2[4] = "Caramount";
		cloumnName1[5] = "现金[元]";
		cloumnName2[5] = "Cash";
		cloumnName1[6] = "POS[元]";
		cloumnName2[6] = "Pos";
		cloumnName1[7] = "支票[元]";
		cloumnName2[7] = "Checkfee";
		cloumnName1[8] = "其他[元]";
		cloumnName2[8] = "Otherfee";
	}

	/**
	 * 结算先付类型
	 * 
	 * @param cloumnName1
	 * @param cloumnName2
	 */
	public void SetAccountOutwarehouseFields(String[] cloumnName1, String[] cloumnName2) {
		// cloumnName1[0]= "类别"; cloumnName2[0]= "Typename";
		cloumnName1[0] = "订单号";
		cloumnName2[0] = "Cwb";
		cloumnName1[1] = "订单类型";
		cloumnName2[1] = "Cwbtype";
		cloumnName1[2] = "发货件数";
		cloumnName2[2] = "Sendcarnum";
		cloumnName1[3] = "出库件数";
		cloumnName2[3] = "Scannum";
		cloumnName1[4] = "货物价值[元]";
		cloumnName2[4] = "Caramount";
		cloumnName1[5] = "代收货款[元]";
		cloumnName2[5] = "Receivablefee";
		cloumnName1[6] = "应退款[元]";
		cloumnName2[6] = "Paybackfee";
	}

	// 修改订单导出view
	public void SetEditOrderFields(String[] cloumnName1, String[] cloumnName2) {
		cloumnName1[0] = "订单号";
		cloumnName2[0] = "Cwb";
		cloumnName1[1] = "提醒站点";
		cloumnName2[1] = "deliverybranchid";
		cloumnName1[2] = "修改前姓名";
		cloumnName2[2] = "oldconsigneename";
		cloumnName1[3] = "姓名（修改）";
		cloumnName2[3] = "newconsigneename";
		cloumnName1[4] = "修改前电话";
		cloumnName2[4] = "oldconsigneemobile";
		cloumnName1[5] = "电话（修改）";
		cloumnName2[5] = "newconsigneemoblie";
		cloumnName1[6] = "修改前地址";
		cloumnName2[6] = "oldconsigneeaddress";
		cloumnName1[7] = "地址（修改）";
		cloumnName2[7] = "newconsigneeaddress";
		cloumnName1[8] = "修改前再次配送时间";
		cloumnName2[8] = "oldResendtime";
		cloumnName1[9] = "再次配送时间（修改）";
		cloumnName2[9] = "newResendtime";
		cloumnName1[10] = "修改前供货商需求";
		cloumnName2[10] = "oldcommand";
		cloumnName1[11] = "供货商需求（修改）";
		cloumnName2[11] = "newcommand";
		cloumnName1[12] = "修改时间";
		cloumnName2[12] = "cretime";
		cloumnName1[13] = "修改人";
		cloumnName2[13] = "crename";
		cloumnName1[14] = "修改前备注";
		cloumnName2[14] = "oldremark";
		cloumnName1[15] = "备注（修改）";
		cloumnName2[15] = "newremark";
	}

	// 订单修改处理功能导出
	public Object setEditOrderObject(String[] cloumnName3, List<SearcheditInfo> views, List<User> userlist, Object a, int i, int k) {
		try {
			if (cloumnName3[i].equals("Cwb")) {
				a = views.get(k).getCwb();
			} else if (cloumnName3[i].equals("deliverybranchid")) {
				a = this.branchDAO.getBranchByBranchid(views.get(k).getDeliverybranchid()).getBranchname();
			} else if (cloumnName3[i].equals("oldconsigneename")) {
				a = views.get(k).getOldconsigneename();
			} else if (cloumnName3[i].equals("newconsigneename")) {
				a = views.get(k).getNewconsigneename();
			} else if (cloumnName3[i].equals("oldconsigneemobile")) {
				a = views.get(k).getOldconsigneemobile();
			} else if (cloumnName3[i].equals("newconsigneemoblie")) {
				a = views.get(k).getNewconsigneemobile();
			} else if (cloumnName3[i].equals("oldconsigneeaddress")) {
				a = views.get(k).getOldconsigneeaddress();
			} else if (cloumnName3[i].equals("newconsigneeaddress")) {
				a = views.get(k).getNewconsigneeaddress();
			} else if (cloumnName3[i].equals("oldResendtime")) {
				a = views.get(k).getOldResendtime();
			} else if (cloumnName3[i].equals("newResendtime")) {
				a = views.get(k).getNewResendtime();
			} else if (cloumnName3[i].equals("oldcommand")) {
				a = views.get(k).getOldcommand();
			} else if (cloumnName3[i].equals("newcommand")) {
				a = views.get(k).getNewcommand();
			} else if (cloumnName3[i].equals("cretime")) {
				a = views.get(k).getCretime();
			} else if (cloumnName3[i].equals("crename")) {
				for (User u : userlist) {
					if (u.getUserid() == views.get(k).getCrename()) {
						a = u.getRealname();
					}
				}
			} else if (cloumnName3[i].equals("oldremark")) {
				a = views.get(k).getOldremark();
			} else if (cloumnName3[i].equals("newremark")) {
				a = views.get(k).getNewremark();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return a;
	}

	/**
	 * 扣款结算订单明细
	 * 
	 * @param cloumnName1
	 * @param cloumnName2
	 */
	public void SetAccountDeducDetailFields(String[] cloumnName1, String[] cloumnName2) {
		cloumnName1[0] = "站点名称";
		cloumnName2[0] = "Branchname";
		cloumnName1[1] = "订单号";
		cloumnName2[1] = "Cwb";
		cloumnName1[2] = "类型";
		cloumnName2[2] = "Recordtype";
		cloumnName1[3] = "交易金额";
		cloumnName2[3] = "Fee";
		cloumnName1[4] = "备注";
		cloumnName2[4] = "Memo";
		cloumnName1[5] = "操作时间";
		cloumnName2[5] = "Createtime";
		cloumnName1[6] = "操作人";
		cloumnName2[6] = "Username";
	}

	public Object setAccountDeducDetail(String[] cloumnName3, HttpServletRequest request1, List<AccountDeducDetailView> list, Object a, int i, int k) {
		try {
			a = list.get(k).getClass().getMethod("get" + cloumnName3[i]).invoke(list.get(k));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return a;
	}

	/**
	 * 扣款结算交易记录
	 * 
	 * @param cloumnName1
	 * @param cloumnName2
	 */
	public void SetAccountDeductRecordFields(String[] cloumnName1, String[] cloumnName2) {
		cloumnName1[0] = "类型";
		cloumnName2[0] = "Recordtype";
		cloumnName1[1] = "订单号";
		cloumnName2[1] = "Cwb";
		cloumnName1[2] = "交易金额";
		cloumnName2[2] = "Fee";
		cloumnName1[3] = "交易前余额";
		cloumnName2[3] = "Beforefee";
		cloumnName1[4] = "交易后余额";
		cloumnName2[4] = "Afterfee";
		cloumnName1[5] = "交易前欠款";
		cloumnName2[5] = "Beforedebt";
		cloumnName1[6] = "交易后欠款";
		cloumnName2[6] = "Afterdebt";
		cloumnName1[7] = "备注";
		cloumnName2[7] = "Memo";
		cloumnName1[8] = "操作人";
		cloumnName2[8] = "Username";
		cloumnName1[9] = "交易时间";
		cloumnName2[9] = "Createtime";
	}

	public Object setAccountDeductRecord(String[] cloumnName3, HttpServletRequest request1, List<AccountDeductRecordView> list, Object a, int i, int k) {
		try {
			a = list.get(k).getClass().getMethod("get" + cloumnName3[i]).invoke(list.get(k));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return a;
	}

	/**
	 * 退货审核
	 * 
	 * @param cloumnName1
	 * @param cloumnName2
	 */
	public void SetOrderBackCheckFields(String[] cloumnName1, String[] cloumnName2) {
		cloumnName1[0] = "订单号";
		cloumnName2[0] = "Cwb";
		cloumnName1[1] = "供货商";
		cloumnName2[1] = "Customername";
		cloumnName1[2] = "订单类型";
		cloumnName2[2] = "Cwbordertypename";
		cloumnName1[3] = "当前状态";
		cloumnName2[3] = "Flowordertypename";
		cloumnName1[4] = "配送结果";
		cloumnName2[4] = "Cwbstatename";
		cloumnName1[5] = "收件人";
		cloumnName2[5] = "Consigneename";
		cloumnName1[6] = "收件人手机";
		cloumnName2[6] = "Consigneephone";
		cloumnName1[7] = "收件人地址";
		cloumnName2[7] = "Consigneeaddress";
		cloumnName1[8] = "退货原因";
		cloumnName2[8] = "Backreason";
	}

	public Object setOrderBackCheck(String[] cloumnName3, HttpServletRequest request1, List<OrderBackCheck> list, Object a, int i, int k) {
		try {
			a = list.get(k).getClass().getMethod("get" + cloumnName3[i]).invoke(list.get(k));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return a;
	}

	public void setOrderArriveTimeFields(String[] cloumnName1, String[] cloumnName2, String[] cloumnName3) {
		cloumnName1[0] = "供货商";
		cloumnName2[0] = "customername";
		cloumnName3[0] = "string";
		cloumnName1[1] = "订单号";
		cloumnName2[1] = "cwb";
		cloumnName3[1] = "string";
		cloumnName1[2] = "发货时间";
		cloumnName2[2] = "outtime";
		cloumnName3[2] = "string";
		cloumnName1[3] = "入库时间";
		cloumnName2[3] = "intime";
		cloumnName3[3] = "string";
		cloumnName1[4] = "到车时间";
		cloumnName2[4] = "arrivetime";
		cloumnName3[4] = "string";
		cloumnName1[5] = "件数";
		cloumnName2[5] = "sendcarnum";
		cloumnName3[5] = "long";
		cloumnName1[6] = "已入库件数";
		cloumnName2[6] = "scannum";
		cloumnName3[6] = "long";
		cloumnName1[7] = "订单类型";
		cloumnName2[7] = "cwbordertypename";
		cloumnName3[7] = "string";
		cloumnName1[8] = "发货仓库";
		cloumnName2[8] = "outbranchname";
		cloumnName3[8] = "string";
		cloumnName1[9] = "入库库房";
		cloumnName2[9] = "inbranchname";
		cloumnName3[9] = "string";
	}

	public void setDeliveryClientYiLingHuoFields(String[] cloumnName1, String[] cloumnName2, String[] cloumnName3) {
		cloumnName1[0] = "订单号";
		cloumnName2[0] = "cwb";
		cloumnName3[0] = "string";
		cloumnName1[1] = "小件员";
		cloumnName2[1] = "delivername";
		cloumnName3[1] = "string";
		cloumnName1[2] = "供货商";
		cloumnName2[2] = "customername";
		cloumnName3[2] = "string";
		cloumnName1[3] = "发货时间";
		cloumnName2[3] = "emaildate";
		cloumnName3[3] = "string";
		cloumnName1[4] = "收件人";
		cloumnName2[4] = "consigneename";
		cloumnName3[4] = "string";
		cloumnName1[5] = "代收金额";
		cloumnName2[5] = "receivablefee";
		cloumnName3[5] = "string";
		cloumnName1[6] = "备注";
		cloumnName2[6] = "cwbremark";
		cloumnName3[6] = "string";
		cloumnName1[7] = "地址";
		cloumnName2[7] = "consigneeaddress";
		cloumnName3[7] = "string";
	}

	public void setDeliveryClientYiWeiPaiFields(String[] cloumnName1, String[] cloumnName2, String[] cloumnName3) {
		cloumnName1[0] = "订单号";
		cloumnName2[0] = "cwb";
		cloumnName3[0] = "string";
		cloumnName1[1] = "小件员";
		cloumnName2[1] = "delivername";
		cloumnName3[1] = "string";
		cloumnName1[2] = "委派员";
		cloumnName2[2] = "clientname";
		cloumnName3[2] = "string";
		cloumnName1[3] = "供货商";
		cloumnName2[3] = "customername";
		cloumnName3[3] = "string";
		cloumnName1[4] = "发货时间";
		cloumnName2[4] = "emaildate";
		cloumnName3[4] = "string";
		cloumnName1[5] = "收件人";
		cloumnName2[5] = "consigneename";
		cloumnName3[5] = "string";
		cloumnName1[6] = "代收金额";
		cloumnName2[6] = "receivablefee";
		cloumnName3[6] = "string";
		cloumnName1[7] = "备注";
		cloumnName2[7] = "cwbremark";
		cloumnName3[7] = "string";
		cloumnName1[8] = "地址";
		cloumnName2[8] = "consigneeaddress";
		cloumnName3[8] = "string";
	}

	/**
	 * 配送结果结算记录
	 * 
	 * @param cloumnName1
	 * @param cloumnName2
	 */
	public void SetAccountDeliverySummaryFields(String[] cloumnName1, String[] cloumnName2) {
		cloumnName1[0] = "站点";
		cloumnName2[0] = "Branchname";
		cloumnName1[1] = "加款金额";
		cloumnName2[1] = "Otheraddfee";
		cloumnName1[2] = "减款金额";
		cloumnName2[2] = "Othersubtractfee";
		cloumnName1[3] = "欠款订单数";
		cloumnName2[3] = "Qknums";
		cloumnName1[4] = "欠款金额[元]";
		cloumnName2[4] = "Qkcash";
		cloumnName1[5] = "本次应交订单数";
		cloumnName2[5] = "Tonums";
		cloumnName1[6] = "本次应交现金[元]";
		cloumnName2[6] = "Tocash";
		cloumnName1[7] = "本次应交POS[元]";
		cloumnName2[7] = "Topos";
		cloumnName1[8] = "本次应交支票[元]";
		cloumnName2[8] = "Tocheck";
		cloumnName1[9] = "本次应交其他[元]";
		cloumnName2[9] = "Toother";
		cloumnName1[10] = "本次应交合计[元]";
		cloumnName2[10] = "Tofee";

		cloumnName1[11] = "本次实交订单数";
		cloumnName2[11] = "Yjnums";
		cloumnName1[12] = "本次实交现金[元]";
		cloumnName2[12] = "Yjcash";
		cloumnName1[13] = "本次实交POS[元]";
		cloumnName2[13] = "Yjpos";
		cloumnName1[14] = "本次实交支票[元]";
		cloumnName2[14] = "Yjcheck";
		cloumnName1[15] = "本次实交其他[元]";
		cloumnName2[15] = "Yjother";
		cloumnName1[16] = "本次实交合计[元]";
		cloumnName2[16] = "Yjfee";

		cloumnName1[17] = "本次欠款订单数";
		cloumnName2[17] = "Wjnums";
		cloumnName1[18] = "本次欠款现金[元]";
		cloumnName2[18] = "Wjcash";
		cloumnName1[19] = "本次欠款POS[元]";
		cloumnName2[19] = "Wjpos";
		cloumnName1[20] = "本次欠款支票[元]";
		cloumnName2[20] = "Wjcheck";
		cloumnName1[21] = "本次欠款其他[元]";
		cloumnName2[21] = "Wjother";
		cloumnName1[22] = "本次欠款合计[元]";
		cloumnName2[22] = "Wjfee";

		cloumnName1[23] = "提交人";
		cloumnName2[23] = "Username";
		cloumnName1[24] = "提交时间";
		cloumnName2[24] = "Createtime";
		cloumnName1[25] = "备注";
		cloumnName2[25] = "Memo";
	}

	public Object setAccountCwbSummary(String[] cloumnName3, HttpServletRequest request1, List<AccountCwbSummary> list, Object a, int i, int k) {
		try {
			a = list.get(k).getClass().getMethod("get" + cloumnName3[i]).invoke(list.get(k));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return a;
	}

	/**
	 * 扣款结算交易记录
	 * 
	 * @param cloumnName1
	 * @param cloumnName2
	 */
	public void SetAccountSumFields(String[] cloumnName1, String[] cloumnName2) {
		cloumnName1[0] = "类型";
		cloumnName2[0] = "Recordtype";
		cloumnName1[1] = "账户增加金额[元]";
		cloumnName2[1] = "Beforefee";
		cloumnName1[2] = "账户减少金额[元]";
		cloumnName2[2] = "Afterfee";
		cloumnName1[3] = "单数";
		cloumnName2[3] = "Nums";
		cloumnName1[4] = "交易时间";
		cloumnName2[4] = "Createtime";
	}

	/**
	 * 账户管理
	 * 
	 * @param cloumnName1
	 * @param cloumnName2
	 */
	public void SetBranchListFields(String[] cloumnName1, String[] cloumnName2) {
		cloumnName1[0] = "站点";
		cloumnName2[0] = "Branchname";
		cloumnName1[1] = "信用额度[元]";
		cloumnName2[1] = "Credit";
		cloumnName1[2] = "可用额度[元]";
		cloumnName2[2] = "Credituse";
		cloumnName1[3] = "预扣帐户余额[元]";
		cloumnName2[3] = "Balance";
		cloumnName1[4] = "预扣欠款[元]";
		cloumnName2[4] = "Debt";
		cloumnName1[5] = "实扣帐户余额[元]";
		cloumnName2[5] = "Balancevirt";
		cloumnName1[6] = "实扣欠款[元]";
		cloumnName2[6] = "Debtvirt";
	}

	public Object setBranchList(String[] cloumnName3, HttpServletRequest request1, List<Branch> list, Object a, int i, int k) {
		try {
			a = list.get(k).getClass().getMethod("get" + cloumnName3[i]).invoke(list.get(k));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return a;
	}

	public void SetAccountCwbFareDetailFields(String[] cloumnName1, String[] cloumnName2) {
		cloumnName1[0] = "订单号";
		cloumnName2[0] = "Cwb";
		cloumnName1[1] = "小件员";
		cloumnName2[1] = "Userid";
		cloumnName1[2] = "供货商";
		cloumnName2[2] = "Customerid";
		cloumnName1[3] = "订单类型";
		cloumnName2[3] = "Cwbordertypeid";
		cloumnName1[4] = "配送站点";
		cloumnName2[4] = "Deliverybranchid";
		cloumnName1[5] = "归班审核时间";
		cloumnName2[5] = "Audittime";
		cloumnName1[6] = "配送结果";
		cloumnName2[6] = "Deliverystate";
		cloumnName1[7] = "应收运费金[元]";
		cloumnName2[7] = "Shouldfare";
		cloumnName1[8] = "实收运费[元]";
		cloumnName2[8] = "Infactfare";
		cloumnName1[9] = "交款状态";
		cloumnName2[9] = "Fareid";
		cloumnName1[10] = "交款时间";
		cloumnName2[10] = "Payuptime";
		cloumnName1[11] = "审核时间";
		cloumnName2[11] = "Verifytime";
	}

	public Object setAccountCwbFareDetailObject(String[] cloumnName3, List<AccountCwbFareDetail> accountCwbFareDetailList, HttpServletRequest request1, Object a, int i, int k,
			List<Branch> branchList, Map<Long, Customer> cMap, List<User> userList) {
		try {
			if (cloumnName3[i].equals("Customerid")) {
				long customerid = Long.parseLong(accountCwbFareDetailList.get(k).getClass().getMethod("get" + cloumnName3[i]).invoke(accountCwbFareDetailList.get(k)).toString());
				a = cMap.get(customerid) == null ? "" : cMap.get(customerid).getCustomername();
			} else if (cloumnName3[i].equals("Cwbordertypeid")) {
				for (CwbOrderTypeIdEnum ct : CwbOrderTypeIdEnum.values()) {
					if (Long.parseLong(accountCwbFareDetailList.get(k).getClass().getMethod("get" + cloumnName3[i]).invoke(accountCwbFareDetailList.get(k)).toString()) == ct.getValue()) {
						a = ct.getText();
						break;
					}
				}
			} else if (cloumnName3[i].equals("Deliverybranchid")) {
				a = "";
				if (branchList.size() > 0) {
					for (Branch b : branchList) {
						if (Long.parseLong(accountCwbFareDetailList.get(k).getClass().getMethod("get" + cloumnName3[i]).invoke(accountCwbFareDetailList.get(k)).toString()) == b.getBranchid()) {
							a = b.getBranchname();
							break;
						}
					}
				}
			} else if (cloumnName3[i].equals("Userid")) {
				a = "";
				if (userList.size() > 0) {
					for (User u : userList) {
						if (Long.parseLong(accountCwbFareDetailList.get(k).getClass().getMethod("get" + cloumnName3[i]).invoke(accountCwbFareDetailList.get(k)).toString()) == u.getUserid()) {
							a = u.getRealname();
							break;
						}
					}
				}
			} else if (cloumnName3[i].equals("Deliverystate")) {
				for (DeliveryStateEnum ds : DeliveryStateEnum.values()) {
					if (Long.parseLong(accountCwbFareDetailList.get(k).getClass().getMethod("get" + cloumnName3[i]).invoke(accountCwbFareDetailList.get(k)).toString()) == ds.getValue()) {
						a = ds.getText();
						break;
					}
				}
			} else if (cloumnName3[i].equals("Fareid")) {
				if (accountCwbFareDetailList.get(k).getVerifyflag() > 0) {
					a = "已审核";
				} else {
					a = "未交款";

					if (Long.parseLong(accountCwbFareDetailList.get(k).getClass().getMethod("get" + cloumnName3[i]).invoke(accountCwbFareDetailList.get(k)).toString()) > 0) {
						a = "已交款";
					}

				}
			} else {
				a = accountCwbFareDetailList.get(k).getClass().getMethod("get" + cloumnName3[i]).invoke(accountCwbFareDetailList.get(k));
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a;

	}

	public void SetAccountCwbFareDetailVerifyFields(String[] cloumnName1, String[] cloumnName2) {
		cloumnName1[0] = "订单号";
		cloumnName2[0] = "Cwb";
		cloumnName1[1] = "小件员";
		cloumnName2[1] = "Userid";
		cloumnName1[2] = "供货商";
		cloumnName2[2] = "Customerid";
		cloumnName1[3] = "订单类型";
		cloumnName2[3] = "Cwbordertypeid";
		cloumnName1[4] = "配送站点";
		cloumnName2[4] = "Deliverybranchid";
		cloumnName1[5] = "审核时间";
		cloumnName2[5] = "Audittime";
		cloumnName1[6] = "配送结果";
		cloumnName2[6] = "Deliverystate";
		cloumnName1[7] = "应收运费金[元]";
		cloumnName2[7] = "Shouldfare";
		cloumnName1[8] = "实收运费[元]";
		cloumnName2[8] = "Infactfare";
		cloumnName1[9] = "交款时间";
		cloumnName2[9] = "Payuptime";
		cloumnName1[10] = "交款方式";
		cloumnName2[10] = "Fee";
		cloumnName1[11] = "交款人";
		cloumnName2[11] = "User";
		cloumnName1[12] = "卡号";
		cloumnName2[12] = "Griocardno";
		cloumnName1[13] = "审核状态";
		cloumnName2[13] = "Verifytype";
		cloumnName1[14] = "审核时间";
		cloumnName2[14] = "Verifytime";

	}

	public Object setAccountCwbFareDetailVerifyObject(String[] cloumnName3, List<AccountCwbFareDetail> accountCwbFareDetailList, HttpServletRequest request1, Object a, int i, int k,
			List<Branch> branchList, Map<Long, Customer> cMap, List<User> userList, Map<Long, AccountCwbFare> accountFareMap) {
		try {
			if (cloumnName3[i].equals("Fee")) {

				double cashfee = accountFareMap.get(accountCwbFareDetailList.get(k).getFareid()).getCashfee() == null ? 0 : accountFareMap.get(accountCwbFareDetailList.get(k).getFareid())
						.getCashfee().doubleValue();
				double girofee = accountFareMap.get(accountCwbFareDetailList.get(k).getFareid()).getGirofee() == null ? 0 : accountFareMap.get(accountCwbFareDetailList.get(k).getFareid())
						.getGirofee().doubleValue();
				if (girofee > 0) {
					a = "转账";
				}
				if (cashfee > 0) {
					a = "现金";
				}
				if ((cashfee > 0) && (girofee > 0)) {
					a = "现金--转账";
				}

			} else if (cloumnName3[i].equals("User")) {
				String cashuser = accountFareMap.get(accountCwbFareDetailList.get(k).getFareid()).getCashuser();
				String girouser = accountFareMap.get(accountCwbFareDetailList.get(k).getFareid()).getGirouser();
				if (girouser.length() > 0) {
					a = girouser;
				}
				if (cashuser.length() > 0) {
					a = cashuser;
				}
				if ((cashuser.length() > 0) && (girouser.length() > 0)) {
					a = cashuser + "--" + girouser;
				}

			} else if (cloumnName3[i].equals("Griocardno")) {
				a = accountFareMap.get(accountCwbFareDetailList.get(k).getFareid()).getGirocardno();
			} else if (cloumnName3[i].equals("Customerid")) {
				long customerid = Long.parseLong(accountCwbFareDetailList.get(k).getClass().getMethod("get" + cloumnName3[i]).invoke(accountCwbFareDetailList.get(k)).toString());
				a = cMap.get(customerid) == null ? "" : cMap.get(customerid).getCustomername();
			} else if (cloumnName3[i].equals("Cwbordertypeid")) {
				for (CwbOrderTypeIdEnum ct : CwbOrderTypeIdEnum.values()) {
					if (Long.parseLong(accountCwbFareDetailList.get(k).getClass().getMethod("get" + cloumnName3[i]).invoke(accountCwbFareDetailList.get(k)).toString()) == ct.getValue()) {
						a = ct.getText();
						break;
					}
				}
			} else if (cloumnName3[i].equals("Deliverybranchid")) {
				a = "";
				if (branchList.size() > 0) {
					for (Branch b : branchList) {
						if (Long.parseLong(accountCwbFareDetailList.get(k).getClass().getMethod("get" + cloumnName3[i]).invoke(accountCwbFareDetailList.get(k)).toString()) == b.getBranchid()) {
							a = b.getBranchname();
							break;
						}
					}
				}
			} else if (cloumnName3[i].equals("Userid")) {
				a = "";
				if (userList.size() > 0) {
					for (User u : userList) {
						if (Long.parseLong(accountCwbFareDetailList.get(k).getClass().getMethod("get" + cloumnName3[i]).invoke(accountCwbFareDetailList.get(k)).toString()) == u.getUserid()) {
							a = u.getRealname();
							break;
						}
					}
				}
			} else if (cloumnName3[i].equals("Deliverystate")) {
				for (DeliveryStateEnum ds : DeliveryStateEnum.values()) {
					if (Long.parseLong(accountCwbFareDetailList.get(k).getClass().getMethod("get" + cloumnName3[i]).invoke(accountCwbFareDetailList.get(k)).toString()) == ds.getValue()) {
						a = ds.getText();
						break;
					}
				}
			} else if (cloumnName3[i].equals("Verifytype")) {
				if (accountCwbFareDetailList.get(k).getVerifyflag() > 0) {
					a = "已审核";
				} else {
					a = "未审核";
					/*
					 * if
					 * (Long.parseLong(accountCwbFareDetailList.get(k).getClass
					 * ().getMethod("get" +
					 * cloumnName3[i]).invoke(accountCwbFareDetailList
					 * .get(k)).toString()) > 0) { a = "已交款"; }
					 */
				}
			} else {
				a = accountCwbFareDetailList.get(k).getClass().getMethod("get" + cloumnName3[i]).invoke(accountCwbFareDetailList.get(k));
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a;

	}

	public void SetBackSummaryFields(String[] cloumnName1, String[] cloumnName2) {
		cloumnName1[0] = "日期";
		cloumnName2[0] = "Createtime";
		cloumnName1[1] = "站点超24H未到退货中心数量";
		cloumnName2[1] = "Nums24";
		cloumnName1[2] = "站点超72H未到退货中心数量";
		cloumnName2[2] = "Nums72";
		cloumnName1[3] = "退供应商出仓数量";
		cloumnName2[3] = "Numsout";
		cloumnName1[4] = "当日入库订单量";
		cloumnName2[4] = "Numsinto";
		cloumnName1[5] = "退货占比";
		cloumnName2[5] = "Percent";
	}

	public Object setBackSummary(String[] cloumnName3, HttpServletRequest request1, List<BackSummary> list, Object a, int i, int k) {
		try {
			a = list.get(k).getClass().getMethod("get" + cloumnName3[i]).invoke(list.get(k));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return a;
	}

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}
}
