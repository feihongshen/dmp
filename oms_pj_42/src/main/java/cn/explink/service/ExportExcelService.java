package cn.explink.service;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Common;
import cn.explink.domain.Complaint;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.Customer;
import cn.explink.domain.Reason;
import cn.explink.domain.Remark;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.enumutil.RealFlowOrderTypeEnum;
import cn.explink.enumutil.ReasonTypeEnum;
import cn.explink.enumutil.UserEmployeestatusEnum;

@Service
public class ExportExcelService {
	@Autowired
	GetDmpDAO getDmpDAO;
	private User user;

	public void SetCompitFields(String[] cloumnName1, String[] cloumnName2) {
		//
		cloumnName1[0] = "投诉类型";
		cloumnName2[0] = "Complainttype";
		cloumnName1[1] = "投诉单号";
		cloumnName2[1] = "Complaintcwb";
		cloumnName1[2] = "投诉人编号";
		cloumnName2[2] = "Complaintcustomerid";
		cloumnName1[3] = "投诉人";
		cloumnName2[3] = "Complaintcontactman";
		cloumnName1[4] = "投诉人联系电话";
		cloumnName2[4] = "Complaintphone";
		cloumnName1[5] = "被投诉人编号";
		cloumnName2[5] = "Complaintuserid";
		cloumnName1[6] = "被投诉人备注";
		cloumnName2[6] = "Complaintuserdesc";
		cloumnName1[7] = "初次受理人";
		cloumnName2[7] = "Complaintcreateuser";
		cloumnName1[8] = "投诉记录时间";
		cloumnName2[8] = "Complainttime";
		cloumnName1[9] = "审核状态";
		cloumnName2[9] = "CheckflagStr";
		cloumnName1[10] = "投诉处理状态";
		cloumnName2[10] = "ComplaintflagStr";
		cloumnName1[11] = "投诉内容";
		cloumnName2[11] = "Complaintcontent";
		cloumnName1[12] = "投诉处理结果";
		cloumnName2[12] = "Complaintresult";
		cloumnName1[13] = "是否属实";
		cloumnName2[13] = "IssureStr";

	}

	public Object setObjectA(String[] cloumnName3, ResultSet rs, int i, List<User> uList, Map<Long, Customer> cMap, List<Branch> bList, List<Common> commonList, List<CustomWareHouse> cWList,
			Map<String, Map<String, String>> remarkMap, List<Reason> reasonList) throws SQLException {
		Object a = null;
		String cloumname = cloumnName3[i];
		cloumname = cloumnName3[i].substring(0, 1).toLowerCase() + cloumnName3[i].substring(1, cloumnName3[i].length());
		try {
			if ("orderType".equals(cloumname)) {
				a = rs.getObject("cwbordertypeid");
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
				a = rs.getObject("flowordertype") == null ? "0" : rs.getObject("flowordertype");
				for (FlowOrderTypeEnum fote : FlowOrderTypeEnum.values()) {
					if (fote.getValue() == Integer.parseInt(a.toString())) {
						return fote.getText();
					}
				}
			} else if ("paytypeName".equals(cloumname)) {
				a = this.getPayWayType(rs.getObject("paytype_old").toString());
			} else if ("newpayway".equals(cloumname)) {
				a = this.getPayWayType(rs.getObject("paytype").toString());
			} else if ("statisticstateStr".equals(cloumname)) {
				a = "";
				if (Integer.parseInt(rs.getObject("flowordertype") == null ? "0" : rs.getObject("flowordertype").toString()) == FlowOrderTypeEnum.YiShenHe.getValue()) {
					return "已审核";
				} else {
					return "未审核";
				}
			} else if ("orderResultTypeText".equals(cloumname)) {
				a = "";
				for (DeliveryStateEnum dse : DeliveryStateEnum.values()) {
					if (dse.getValue() == Integer.parseInt(rs.getObject("deliverystate") == null ? "0" : rs.getObject("deliverystate").toString())) {
						a = dse.getText();
						break;
					}
				}
			} else if ("carwarehouse".equals(cloumname)) {
				a = "";
				for (Branch b : bList) {
					if (b.getBranchid() == Long.parseLong(rs.getObject("carwarehouse") == null ? "0" : rs.getObject("carwarehouse").toString())) {
						a = b.getBranchname();
						break;
					}
				}
			} else if ("customerwarehouseid".equals(cloumname)) {
				a = "";
				for (CustomWareHouse b : cWList) {
					if (b.getWarehouseid() == Long.parseLong(rs.getObject("customerwarehouseid") == null ? "0" : rs.getObject("customerwarehouseid").toString())) {
						a = b.getCustomerwarehouse();
						break;
					}
				}
			} else if ("inwarhouseRemark".equals(cloumname)) {
				a = remarkMap.get(rs.getObject("cwb").toString()) == null ? "" : remarkMap.get(rs.getObject("cwb").toString()).get(ReasonTypeEnum.RuKuBeiZhu.getText());
			} else if ("posremark".equals(cloumname)) {
				a = rs.getObject("posremark");
			} else if ("checkremark".equals(cloumname)) {
				a = rs.getObject("checkremark");
			} else if ("deliverstateremark".equals(cloumname)) {
				a = rs.getObject("deliverstateremark");
			} else if ("businessfee".equals(cloumname)) {
				a = rs.getObject("businessfee");
			} else if ("receivedfeeuserName".equals(cloumname)) {
				a = "";
				for (User u : uList) {
					if (u.getUserid() == Long.parseLong(rs.getObject("receivedfeeuser") == null ? "0" : rs.getObject("receivedfeeuser").toString())) {
						a = u.getRealname();
						break;
					}
				}
			} else if ("tuihuochuzhantime".equals(cloumname)) {// 退货出站时间，加字段
				a = rs.getObject("tuihuochuzhantime");//
			} else if ("tuihuozhanrukutime".equals(cloumname)) {
				a = rs.getObject("tuihuozhaninstoreroomtime");
			} else if ("tuigonghuoshangchukutime".equals(cloumname)) {// 加字段
				a = rs.getObject("tuigonghuoshangchukutime");//
			} else if ("zhongzhuanzhangintime".equals(cloumname)) {
				// 中转站入库时间
				a = rs.getObject("zhongzhuanrukutime");//
			} else if ("zhongzhuanzhangouttime".equals(cloumname)) {
				// 中转站出库时间
				a = rs.getObject("zhongzhuanzhanchukutime");//
			} else if ("customerbrackhouseremark".equals(cloumname)) {
				a = rs.getObject("customerbrackhouseremark");//
			} else if ("instoreroomtime".equals(cloumname)) {
				a = rs.getObject("instoreroomtime");
			} else if ("inSitetime".equals(cloumname)) {
				a = rs.getObject("inSitetime");
			} else if ("pickGoodstime".equals(cloumname)) {
				a = rs.getObject("pickGoodstime");
			} else if ("outstoreroomtime".equals(cloumname)) {
				a = rs.getObject("outstoreroomtime");
			} else if ("goclasstime".equals(cloumname)) {
				a = rs.getObject("goclasstime");
			} else if ("newchangetime".equals(cloumname)) {
				a = rs.getObject("nowtime");
			} else if ("gobacktime".equals(cloumname)) {
				a = rs.getObject("gobacktime");
			} else if ("podremarkStr".equals(cloumname)) {
				a = "";
				if (reasonList.size() > 0) {
					for (Reason reason : reasonList) {
						if (Long.parseLong(rs.getObject("podremarkid") == null ? "0" : rs.getObject("podremarkid").toString()) == reason.getReasonid()) {
							a = reason.getReasoncontent();
						}
					}
				}
			} else if ("leavedreason".equals(cloumname)) {
				a = rs.getObject("leavedreason") == null ? "" : rs.getObject("leavedreason");
			} else if ("losereason".equals(cloumname)) {
				a = rs.getObject("losereason") == null ? "" : rs.getObject("losereason");

			} else if ("weishuakareason".equals(cloumname)) {
				a = rs.getObject("weishuakareason") == null ? "" : rs.getObject("weishuakareason");

			} else if ("resendtime".equals(cloumname)) {
				a = rs.getObject("resendtime") == null ? "" : rs.getObject("resendtime");
			} else if ("backreason".equals(cloumname)) {
				a = rs.getObject("backreason") == null ? "" : rs.getObject("backreason");
			} else if ("customerid".equals(cloumname)) {
				a = cMap.get(Long.parseLong(rs.getObject("customerid") == null ? "0" : rs.getObject("customerid").toString())) == null ? "" : cMap.get(
						Long.parseLong(rs.getObject("Customerid").toString())).getCustomername();
			} else if ("startbranchname".equals(cloumname)) {
				a = "";
				for (Branch b : bList) {
					if (b.getBranchid() == Long.parseLong(rs.getObject("startbranchid") == null ? "0" : rs.getObject("startbranchid").toString())) {
						a = b.getBranchname();
						break;
					}
				}
			} else if ("nextbranchname".equals(cloumname)) {
				a = "";
				for (Branch b : bList) {
					if (b.getBranchid() == Long.parseLong(rs.getObject("nextbranchid") == null ? "0" : rs.getObject("nextbranchid").toString())) {
						a = b.getBranchname();
						break;
					}
				}
			} else if ("fdelivername".equals(cloumname)) {
				a = "";
				for (User u : uList) {
					if (u.getUserid() == Long.parseLong(rs.getObject("fdeliverid") == null ? "0" : rs.getObject("fdeliverid").toString())) {
						a = u.getRealname();
						if (u.getEmployeestatus() == UserEmployeestatusEnum.LiZhi.getValue()) {
							a = a + "(离职)";
						}
						break;
					}
				}
			} else if ("dReceivedfee".equals(cloumname)) {
				a = rs.getObject("receivedfee");
			} else if ("deliverid".equals(cloumname)) {
				a = "";
				for (User u : uList) {
					if (u.getUserid() == Long.parseLong(rs.getObject("deliverid") == null ? "0" : rs.getObject("deliverid").toString())) {
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
					if (c.getId() == Long.parseLong(rs.getObject("commonid") == null ? "0" : rs.getObject("commonid").toString())) {
						a = c.getCommonname();
						break;
					}
				}
			} else if ("commonnumber".equals(cloumname)) {
				a = "";
				for (Common c : commonList) {
					if (c.getId() == Long.parseLong(rs.getObject("commonid") == null ? "0" : rs.getObject("commonid").toString())) {
						a = c.getCommonnumber();
						break;
					}
				}
			} else if ("cash".equals(cloumname)) {
				a = rs.getObject("cash");
			} else if ("pos".equals(cloumname)) {
				a = rs.getObject("pos");
			} else if ("receivedfee".equals(cloumname)) {
				a = rs.getObject("receivedfee");
			} else if ("returnedfee".equals(cloumname)) {
				a = rs.getObject("returnedfee");
			} else if ("checkfee".equals(cloumname)) {
				a = rs.getObject("checkfee");
			} else if ("backcarname".equals(cloumname)) {
				a = rs.getObject("backcarname");
			} else if ("otherfee".equals(cloumname)) {
				a = rs.getObject("otherfee");
			} else if ("signinman".equals(cloumname)) {
				a = rs.getObject("signinman");
			} else if ("signintime".equals(cloumname)) {
				a = rs.getObject("signintime");
			} else if ("excelbranch".equals(cloumname)) {
				a = "";
				for (Branch b : bList) {
					if (b.getBranchid() == Long.parseLong(rs.getObject("deliverybranchid") == null ? "0" : rs.getObject("deliverybranchid").toString())) {
						a = b.getBranchname();
						break;
					}
				}
			} else if ("currentbranchname".equals(cloumname)) {
				a = "";
				for (Branch b : bList) {
					if (b.getBranchid() == Long.parseLong(rs.getObject("currentbranchid") == null ? "0" : rs.getObject("currentbranchid").toString())) {
						a = b.getBranchname();
						break;
					}
				}
			} else if ("deliverybranchname".equals(cloumname)) {
				a = "";
				for (Branch b : bList) {
					if (b.getBranchid() == Long.parseLong(rs.getObject("payupbranchid") == null ? "0" : rs.getObject("payupbranchid").toString())) {
						a = b.getBranchname();
						break;
					}
				}
			} else if ("realbranchname".equals(cloumname)) {
				a = "";
				long realbranchid = Long.parseLong(rs.getObject("currentbranchid") == null ? "0" : rs.getObject("currentbranchid").toString());
				if (realbranchid == 0) {
					realbranchid = Long.parseLong(rs.getObject("startbranchid") == null ? "0" : rs.getObject("startbranchid").toString());
				}
				for (Branch b : bList) {
					if (b.getBranchid() == realbranchid) {
						a = b.getBranchname();
						break;
					}
				}
			} else if ("realflowordertype".equals(cloumname)) {
				long realbranchid = Long.parseLong(rs.getObject("currentbranchid") == null ? "0" : rs.getObject("currentbranchid").toString());
				if (realbranchid == 0) {
					realbranchid = Long.parseLong(rs.getObject("startbranchid") == null ? "0" : rs.getObject("startbranchid").toString());
				}
				a = this.getRealflowordertype(bList, realbranchid, Long.parseLong(rs.getObject("flowordertype") == null ? "0" : rs.getObject("flowordertype").toString()));
			} else if ("ispayup".equals(cloumname)) {
				long payupid = Long.parseLong(rs.getObject("payupid") == null ? "0" : rs.getObject("payupid").toString());
				if (payupid > 0) {
					a = "是";
				} else {
					a = "否";
				}
			}

			else if ("intowarehouseuserid".equals(cloumname)) {// 入库操作人
				a = "";
				for (User u : uList) {
					if (u.getUserid() == Long.parseLong(rs.getObject("intowarehouseuserid") == null ? "0" : rs.getObject("intowarehouseuserid").toString())) {
						a = u.getRealname();
						break;
					}
				}
			} else if ("outstoreroomtimeuserid".equals(cloumname)) {// 出库操作人
				a = "";
				for (User u : uList) {
					if (u.getUserid() == Long.parseLong(rs.getObject("outstoreroomtimeuserid") == null ? "0" : rs.getObject("outstoreroomtimeuserid").toString())) {
						a = u.getRealname();
						break;
					}
				}
			//应收派费
			}else if("Shouldfare".equals(cloumname)){
				a = rs.getObject("shouldfare");
			
			//实收派费
			}else if("Infactfare".equals(cloumname)){
				a = rs.getObject("infactfare");
				
			//滞留一级原因
			}else if("stayFirstLevel".equals(cloumname)){
				a = "";
				if (reasonList.size() > 0) {
					for (Reason reason : reasonList) {
						if (Long.parseLong(rs.getObject("firstleavedreasonid") == null ? "0" : rs.getObject("firstleavedreasonid").toString()) == reason.getReasonid()) {
							a = reason.getReasoncontent();
						}
					}
				}
				
			//滞留二级原因
			}else if("staySecondLevel".equals(cloumname)){
				a = "";
				if (reasonList.size() > 0) {
					for (Reason reason : reasonList) {
						if (Long.parseLong(rs.getObject("leavedreasonid") == null ? "0" : rs.getObject("leavedreasonid").toString()) == reason.getReasonid()) {
							a = reason.getReasoncontent();
						}
					}
				}
			}

			else {
				a = rs.getObject(cloumname);
				a = this.setAbyUser(a, cloumname);
			}
		} catch (Exception e) {
			// System.out.println(cloumname);
		}
		// System.out.println("pp:"+System.currentTimeMillis());
		return a;

	}

	/**
	 * 承运商 打印 获取 承运商名称
	 *
	 * @param cloumnName3
	 * @param rs
	 * @param i
	 * @param uList
	 * @param cMap
	 * @param bList
	 * @param commonList
	 * @param cWList
	 * @param remarkMap
	 * @param reasonList
	 * @return
	 * @throws SQLException
	 */
	public Object setObjectAForCommon(String[] cloumnName3, ResultSet rs, int i, List<User> uList, Map<Long, Customer> cMap, List<Branch> bList, List<Common> commonList, List<CustomWareHouse> cWList,
			Map<String, Map<String, String>> remarkMap, List<Reason> reasonList) throws SQLException {
		Object a = null;
		String cloumname = cloumnName3[i];
		cloumname = cloumnName3[i].substring(0, 1).toLowerCase() + cloumnName3[i].substring(1, cloumnName3[i].length());
		try {
			if ("orderType".equals(cloumname)) {
				a = rs.getObject("cwbordertypeid");
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
				a = rs.getObject("flowordertype") == null ? "0" : rs.getObject("flowordertype");
				for (FlowOrderTypeEnum fote : FlowOrderTypeEnum.values()) {
					if (fote.getValue() == Integer.parseInt(a.toString())) {
						return fote.getText();
					}
				}
			} else if ("paytypeName".equals(cloumname)) {
				a = this.getPayWayType(rs.getObject("paytype").toString());
			} else if ("statisticstateStr".equals(cloumname)) {
				a = "";
				for (DeliveryStateEnum dse : DeliveryStateEnum.values()) {
					if (dse.getValue() == Integer.parseInt(rs.getObject("deliverystate") == null ? "0" : rs.getObject("deliverystate").toString())) {
						a = dse.getText();
						break;
					}
				}
			} else if ("orderResultTypeText".equals(cloumname)) {
				a = "";
				for (DeliveryStateEnum dse : DeliveryStateEnum.values()) {
					if (dse.getValue() == Integer.parseInt(rs.getObject("deliverystate") == null ? "0" : rs.getObject("deliverystate").toString())) {
						a = dse.getText();
						break;
					}
				}
			} else if ("carwarehouse".equals(cloumname)) {
				a = "";
				for (Branch b : bList) {
					if (b.getBranchid() == Long.parseLong(rs.getObject("carwarehouse") == null ? "0" : rs.getObject("carwarehouse").toString())) {
						a = b.getBranchname();
						break;
					}
				}
			} else if ("customerwarehouseid".equals(cloumname)) {
				a = "";
				for (CustomWareHouse b : cWList) {
					if (b.getWarehouseid() == Long.parseLong(rs.getObject("customerwarehouseid") == null ? "0" : rs.getObject("customerwarehouseid").toString())) {
						a = b.getCustomerwarehouse();
						break;
					}
				}
			} else if ("inwarhouseRemark".equals(cloumname)) {
				a = remarkMap.get(rs.getObject("cwb").toString()) == null ? "" : remarkMap.get(rs.getObject("cwb").toString()).get(ReasonTypeEnum.RuKuBeiZhu.getText());
			} else if ("posremark".equals(cloumname)) {
				a = rs.getObject("posremark");
			} else if ("checkremark".equals(cloumname)) {
				a = rs.getObject("checkremark");
			} else if ("deliverstateremark".equals(cloumname)) {
				a = rs.getObject("deliverstateremark");
			} else if ("businessfee".equals(cloumname)) {
				a = rs.getObject("businessfee");
			} else if ("receivedfeeuserName".equals(cloumname)) {
				a = "";
				for (User u : uList) {
					if (u.getUserid() == Long.parseLong(rs.getObject("receivedfeeuser") == null ? "0" : rs.getObject("receivedfeeuser").toString())) {
						a = u.getRealname();
						break;
					}
				}
			} else if ("tuihuochuzhantime".equals(cloumname)) {// 退货出站时间，加字段
				a = rs.getObject("tuihuochuzhantime");//
			} else if ("tuihuozhanrukutime".equals(cloumname)) {
				a = rs.getObject("tuihuozhaninstoreroomtime");
			} else if ("tuigonghuoshangchukutime".equals(cloumname)) {// 加字段
				a = rs.getObject("tuigonghuoshangchukutime");//
			} else if ("zhongzhuanzhangintime".equals(cloumname)) {
				// 中转站入库时间
				a = rs.getObject("zhongzhuanzhangintime");//
			} else if ("zhongzhuanzhangouttime".equals(cloumname)) {
				// 中转站出库时间
				a = rs.getObject("zhongzhuanzhangouttime");//
			} else if ("customerbrackhouseremark".equals(cloumname)) {
				a = rs.getObject("customerbrackhouseremark");//
			} else if ("instoreroomtime".equals(cloumname)) {
				a = rs.getObject("instoreroomtime");
			} else if ("inSitetime".equals(cloumname)) {
				a = rs.getObject("inSitetime");
			} else if ("pickGoodstime".equals(cloumname)) {
				a = rs.getObject("pickGoodstime");
			} else if ("outstoreroomtime".equals(cloumname)) {
				a = rs.getObject("outstoreroomtime");
			} else if ("goclasstime".equals(cloumname)) {
				a = rs.getObject("goclasstime");
			} else if ("newchangetime".equals(cloumname)) {
				a = rs.getObject("nowtime");
			} else if ("gobacktime".equals(cloumname)) {
				a = rs.getObject("gobacktime");
			} else if ("podremarkStr".equals(cloumname)) {
				a = "";
				if (reasonList.size() > 0) {
					for (Reason reason : reasonList) {
						if (Long.parseLong(rs.getObject("podremarkid") == null ? "0" : rs.getObject("podremarkid").toString()) == reason.getReasonid()) {
							a = reason.getReasoncontent();
						}
					}
				}
			} else if ("leavedreason".equals(cloumname)) {
				a = rs.getObject("leavedreason") == null ? "" : rs.getObject("leavedreason");
			} else if ("losereason".equals(cloumname)) {
				a = rs.getObject("losereason") == null ? "" : rs.getObject("losereason");

			} else if ("weishuakareason".equals(cloumname)) {
				a = rs.getObject("weishuakareason") == null ? "" : rs.getObject("weishuakareason");

			} else if ("resendtime".equals(cloumname)) {
				a = rs.getObject("resendtime") == null ? "" : rs.getObject("resendtime");
			} else if ("backreason".equals(cloumname)) {
				a = rs.getObject("backreason") == null ? "" : rs.getObject("backreason");
			} else if ("customerid".equals(cloumname)) {
				a = cMap.get(Long.parseLong(rs.getObject("customerid") == null ? "0" : rs.getObject("customerid").toString())) == null ? "" : cMap.get(
						Long.parseLong(rs.getObject("Customerid").toString())).getCustomername();
			} else if ("startbranchname".equals(cloumname)) {
				a = "";
				for (Branch b : bList) {
					if (b.getBranchid() == Long.parseLong(rs.getObject("startbranchid") == null ? "0" : rs.getObject("startbranchid").toString())) {
						a = b.getBranchname();
						break;
					}
				}
			} else if ("nextbranchname".equals(cloumname)) {
				a = "";
				for (Branch b : bList) {
					if (b.getBranchid() == Long.parseLong(rs.getObject("nextbranchid") == null ? "0" : rs.getObject("nextbranchid").toString())) {
						a = b.getBranchname();
						break;
					}
				}
			} else if ("fdelivername".equals(cloumname)) {
				a = "";
				for (User u : uList) {
					if (u.getUserid() == Long.parseLong(rs.getObject("fdeliverid") == null ? "0" : rs.getObject("fdeliverid").toString())) {
						a = u.getRealname();
						if (u.getEmployeestatus() == UserEmployeestatusEnum.LiZhi.getValue()) {
							a = a + "(离职)";
						}
						break;
					}
				}
			} else if ("dReceivedfee".equals(cloumname)) {
				a = rs.getObject("receivedfee");
			} else if ("deliverid".equals(cloumname)) {
				a = "";
				for (User u : uList) {
					if (u.getUserid() == Long.parseLong(rs.getObject("deliverid") == null ? "0" : rs.getObject("deliverid").toString())) {
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
					if (rs.getObject("acommoncode") != null) {
						if (c.getCommonnumber().equals(rs.getObject("acommoncode"))) {
							a = c.getCommonname();
							break;
						}
					}
				}
			} else if ("commonnumber".equals(cloumname)) {
				a = "";
				for (Common c : commonList) {
					if (c.getId() == Long.parseLong(rs.getObject("commonid") == null ? "0" : rs.getObject("commonid").toString())) {
						a = c.getCommonnumber();
						break;
					}
				}
			} else if ("cash".equals(cloumname)) {
				a = rs.getObject("cash");
			} else if ("pos".equals(cloumname)) {
				a = rs.getObject("pos");
			} else if ("receivedfee".equals(cloumname)) {
				a = rs.getObject("receivedfee");
			} else if ("returnedfee".equals(cloumname)) {
				a = rs.getObject("returnedfee");
			} else if ("checkfee".equals(cloumname)) {
				a = rs.getObject("checkfee");
			} else if ("backcarname".equals(cloumname)) {
				a = rs.getObject("backcarname");
			} else if ("otherfee".equals(cloumname)) {
				a = rs.getObject("otherfee");
			} else if ("signinman".equals(cloumname)) {
				a = rs.getObject("signinman");
			} else if ("signintime".equals(cloumname)) {
				a = rs.getObject("signintime");
			} else if ("excelbranch".equals(cloumname)) {
				a = "";
				for (Branch b : bList) {
					if (b.getBranchid() == Long.parseLong(rs.getObject("deliverybranchid") == null ? "0" : rs.getObject("deliverybranchid").toString())) {
						a = b.getBranchname();
						break;
					}
				}
			} else if ("currentbranchname".equals(cloumname)) {
				a = "";
				for (Branch b : bList) {
					if (b.getBranchid() == Long.parseLong(rs.getObject("currentbranchid") == null ? "0" : rs.getObject("currentbranchid").toString())) {
						a = b.getBranchname();
						break;
					}
				}
			} else if ("deliverybranchname".equals(cloumname)) {
				a = "";
				for (Branch b : bList) {
					if (b.getBranchid() == Long.parseLong(rs.getObject("deliverybranchid") == null ? "0" : rs.getObject("deliverybranchid").toString())) {
						a = b.getBranchname();
						break;
					}
				}
			} else if ("realbranchname".equals(cloumname)) {
				a = "";
				long realbranchid = Long.parseLong(rs.getObject("currentbranchid") == null ? "0" : rs.getObject("currentbranchid").toString());
				if (realbranchid == 0) {
					realbranchid = Long.parseLong(rs.getObject("startbranchid") == null ? "0" : rs.getObject("startbranchid").toString());
				}
				for (Branch b : bList) {
					if (b.getBranchid() == realbranchid) {
						a = b.getBranchname();
						break;
					}
				}
			} else if ("realflowordertype".equals(cloumname)) {
				long realbranchid = Long.parseLong(rs.getObject("currentbranchid") == null ? "0" : rs.getObject("currentbranchid").toString());
				if (realbranchid == 0) {
					realbranchid = Long.parseLong(rs.getObject("startbranchid") == null ? "0" : rs.getObject("startbranchid").toString());
				}
				a = this.getRealflowordertype(bList, realbranchid, Long.parseLong(rs.getObject("flowordertype") == null ? "0" : rs.getObject("flowordertype").toString()));
			} else if ("ispayup".equals(cloumname)) {
				long payupid = Long.parseLong(rs.getObject("payupid") == null ? "0" : rs.getObject("payupid").toString());
				if (payupid > 0) {
					a = "是";
				} else {
					a = "否";
				}
			} else {
				a = rs.getObject(cloumname);
			}
		} catch (Exception e) {
			// System.out.println(cloumname);
		}
		// System.out.println("pp:"+System.currentTimeMillis());
		return a;

	}

	public String getPayWayType(String payway) {
		StringBuffer str = new StringBuffer();
		String paywaytype = "";
		for (String newpayway : payway.split(",")) {
			for (PaytypeEnum pe : PaytypeEnum.values()) {
				if (Long.parseLong(newpayway) == pe.getValue()) {
					str.append(pe.getText()).append(",");
				}
			}
		}
		if (str.length() > 0) {
			paywaytype = str.substring(0, str.length() - 1);
		} else {
			paywaytype = "现金";
		}
		return paywaytype;
	}

	public String getRealflowordertype(List<Branch> bList, long branchid, long flowordertype) {
		long sitetype = 0;
		for (Branch b : bList) {
			if (b.getBranchid() == branchid) {
				sitetype = b.getSitetype();
			}
		}
		if (flowordertype == FlowOrderTypeEnum.DaoRuShuJu.getValue()) {
			return RealFlowOrderTypeEnum.DaoRuShuJu.getText();
		} else if (flowordertype == FlowOrderTypeEnum.TiHuo.getValue()) {
			return RealFlowOrderTypeEnum.TiHuo.getText();
		} else if (flowordertype == FlowOrderTypeEnum.TiHuoYouHuoWuDan.getValue()) {
			return RealFlowOrderTypeEnum.TiHuoYouHuoWuDan.getText();
		} else if ((flowordertype == FlowOrderTypeEnum.RuKu.getValue()) && (sitetype == BranchEnum.KuFang.getValue())) {
			return RealFlowOrderTypeEnum.KuFangRuKu.getText();
		} else if ((flowordertype == FlowOrderTypeEnum.RuKu.getValue()) && (sitetype == BranchEnum.ZhongZhuan.getValue())) {
			return RealFlowOrderTypeEnum.ZhongZhuanZhanRuKu.getText();
		} else if (flowordertype == FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()) {
			return RealFlowOrderTypeEnum.TuiHuoZhanRuKu.getText();
		} else if ((flowordertype == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) && (sitetype == BranchEnum.KuFang.getValue())) {
			return RealFlowOrderTypeEnum.KuFangChuKuSaoMiao.getText();
		} else if ((flowordertype == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) && (sitetype == BranchEnum.ZhanDian.getValue())) {
			return RealFlowOrderTypeEnum.ZhongZhuanZhanChuZhanSaoMiao.getText();
		} else if ((flowordertype == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) && (sitetype == BranchEnum.ZhongZhuan.getValue())) {
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

	public Object setObjectB(String[] cloumnName3, HttpServletRequest request1, List<Complaint> list, Object a, int i, int k) {
		try {
			a = list.get(k).getClass().getMethod("get" + cloumnName3[i]).invoke(list.get(k));
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

	public static boolean writerFile(String filePathName, String content) {

		boolean flag = false;

		OutputStreamWriter osw = null;

		try {
			if ((filePathName != null) && !"".equals(filePathName)) {
				osw = new OutputStreamWriter(new FileOutputStream(filePathName));
			}

		} catch (FileNotFoundException e1) {
			flag = false;
			e1.printStackTrace();
		}

		if (osw != null) {
			BufferedWriter bw = new BufferedWriter(osw);

			try {

				if ((content != null) && !"".equals(content)) {

					bw.write(content);
					flag = true;

				}
			} catch (IOException e) {

				flag = false;
				e.printStackTrace();
			}

			finally {
				try

				{
					bw.close();
					osw.close();
				} catch (IOException e) {
					flag = false;
					e.printStackTrace();
				}
			}

		}
		return flag;

	}

	public void SetTiHuoFields(String[] cloumnName1, String[] cloumnName2, String[] cloumnName3) {
		cloumnName1[0] = "订单号";
		cloumnName2[0] = "Cwb";
		cloumnName3[0] = "string";
		cloumnName1[1] = "供货商";
		cloumnName2[1] = "Customername";
		cloumnName3[0] = "string";
		cloumnName1[2] = "提货时间";
		cloumnName2[2] = "Tihuotime";
		cloumnName3[0] = "string";
		cloumnName1[3] = "收件人";
		cloumnName2[3] = "Consigneename";
		cloumnName3[0] = "string";
		cloumnName1[4] = "收件人地址";
		cloumnName2[4] = "Consigneeaddress";
		cloumnName3[0] = "string";
	}

	// 提货订单统计功能导出
	public Object setTiHuoObject(String[] cloumnName3, ResultSet rs, int i, Map<Long, Customer> cMap) {
		Object a = null;
		try {
			if (cloumnName3[i].equals("Cwb")) {
				a = rs.getString("cwb");
			} else if (cloumnName3[i].equals("Customername")) {
				a = cMap.get(Long.parseLong(rs.getObject("customerid") == null ? "0" : rs.getObject("customerid").toString())) == null ? "" : cMap.get(
						Long.parseLong(rs.getObject("Customerid").toString())).getCustomername();
			} else if (cloumnName3[i].equals("Tihuotime")) {
				a = rs.getString("tihuotime");
			} else if (cloumnName3[i].equals("Consigneename")) {
				a = rs.getString("consigneename");
			} else if (cloumnName3[i].equals("Consigneeaddress")) {
				a = rs.getString("consigneeaddress");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return a;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	private Object setAbyUser(Object a, String cloumname) {
		if (cloumname.equals("consigneename")) {
			if (this.getUser().getShownameflag() != 1) {
				a = "******";
			}
		}
		if (cloumname.equals("consigneephone")) {
			if (this.getUser().getShowphoneflag() != 1) {
				a = "******";
			}
		}
		if (cloumname.equals("consigneemobile")) {
			if (this.getUser().getShowmobileflag() != 1) {
				a = "******";
			}
		}
		return a;
	}
}
