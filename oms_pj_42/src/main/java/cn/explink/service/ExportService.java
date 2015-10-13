package cn.explink.service;

import java.lang.reflect.InvocationTargetException;
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
public class ExportService {
	@Autowired
	GetDmpDAO getDmpDAO;

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

	public Object setObjectA(String[] cloumnName3, Map<String, Object> mapRow, int i, List<User> uList, Map<Long, Customer> cMap, List<Branch> bList, List<Common> commonList,
			List<CustomWareHouse> cWList, Map<String, Map<String, String>> remarkMap, List<Reason> reasonList) throws SQLException {
		Object a = null;
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
					if (fote.getValue() == Integer.parseInt(a.toString()))
						return fote.getText();
				}
			} else if ("paytypeName".equals(cloumname)) {
				a = this.getPayWayType(mapRow.get("paytype").toString());
			} else if ("statisticstateStr".equals(cloumname)) {
				a = "";
				for (DeliveryStateEnum dse : DeliveryStateEnum.values()) {
					if (dse.getValue() == Integer.parseInt(mapRow.get("deliverystate").toString())) {
						a = dse.getText();
						break;
					}
				}
			} else if ("orderResultTypeText".equals(cloumname)) {
				a = "";
				for (DeliveryStateEnum dse : DeliveryStateEnum.values()) {
					if (dse.getValue() == Integer.parseInt(mapRow.get("deliverystate").toString())) {
						a = dse.getText();
						break;
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
			} else if ("inwarhouseRemark".equals(cloumname)) {
				a = remarkMap.get(mapRow.get("Cwb").toString()) == null ? "" : remarkMap.get(mapRow.get("Cwb").toString()).get(ReasonTypeEnum.RuKuBeiZhu.getText());
			} else if ("posremark".equals(cloumname)) {
				a = mapRow.get("posremark");
			} else if ("checkremark".equals(cloumname)) {
				a = mapRow.get("checkremark");
			} else if ("deliverstateremark".equals(cloumname)) {
				a = mapRow.get("deliverstateremark");
			} else if ("businessfee".equals(cloumname)) {
				a = mapRow.get("businessfee");
			} else if ("receivedfeeuserName".equals(cloumname)) {
				a = "";
				for (User u : uList) {
					if (u.getUserid() == Long.parseLong(mapRow.get("receivedfeeuser").toString())) {
						a = u.getRealname();
						break;
					}
				}
			} else if ("tuihuochuzhantime".equals(cloumname)) {
				a = mapRow.get("tuihuozhaninstoreroomtime");
			} else if ("tuihuozhanrukutime".equals(cloumname)) {
				a = mapRow.get("tuihuozhaninstoreroomtime");
			} else if ("tuigonghuoshangchukutime".equals(cloumname)) {
				a = mapRow.get("tuihuozhaninstoreroomtime");
				;
			} else if ("zhongzhuanzhangintime".equals(cloumname)) {
				// 中转站入库时间
				a = mapRow.get("tuihuozhaninstoreroomtime");
			} else if ("zhongzhuanzhangouttime".equals(cloumname)) {
				// 中转站出库时间
				a = mapRow.get("tuihuozhaninstoreroomtime");
			} else if ("customerbrackhouseremark".equals(cloumname)) {
				a = mapRow.get("tuihuozhaninstoreroomtime");
			} else if ("instoreroomtime".equals(cloumname)) {
				a = mapRow.get("tuihuozhaninstoreroomtime");
			} else if ("inSitetime".equals(cloumname)) {
				a = mapRow.get("tuihuozhaninstoreroomtime");
			} else if ("pickGoodstime".equals(cloumname)) {
				a = mapRow.get("tuihuozhaninstoreroomtime");
			} else if ("outstoreroomtime".equals(cloumname)) {
				a = mapRow.get("tuihuozhaninstoreroomtime");
			} else if ("goclasstime".equals(cloumname)) {
				a = mapRow.get("tuihuozhaninstoreroomtime");
			} else if ("newchangetime".equals(cloumname)) {
				a = mapRow.get("tuihuozhaninstoreroomtime");
			} else if ("gobacktime".equals(cloumname)) {
				a = mapRow.get("tuihuozhaninstoreroomtime");
			} else if ("podremarkStr".equals(cloumname)) {
				a = "";
				if (reasonList.size() > 0) {
					for (Reason reason : reasonList) {
						if (Long.parseLong(mapRow.get("podremarkid").toString()) == reason.getReasonid()) {
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
			} else if ("fdelivername".equals(cloumname)) {
				a = "";
				for (User u : uList) {
					if (u.getUserid() == Long.parseLong(mapRow.get("fdeliverid").toString())) {
						a = u.getRealname();
						if (u.getEmployeestatus() == UserEmployeestatusEnum.LiZhi.getValue()) {
							a = a + "(离职)";
						}
						break;
					}
				}
			} else if ("dReceivedfee".equals(cloumname)) {
				a = mapRow.get("receivedfee");
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
				a = mapRow.get("cash");
			} else if ("pos".equals(cloumname)) {
				a = mapRow.get("pos");
			} else if ("receivedfee".equals(cloumname)) {
				a = mapRow.get("receivedfee");
			} else if ("returnedfee".equals(cloumname)) {
				a = mapRow.get("returnedfee");
			} else if ("checkfee".equals(cloumname)) {
				a = mapRow.get("checkfee");
			} else if ("backcarname".equals(cloumname)) {
				a = mapRow.get("backcarname");
			} else if ("otherfee".equals(cloumname)) {
				a = mapRow.get("otherfee");
			} else if ("signinman".equals(cloumname)) {
				a = mapRow.get("signinman");
			} else if ("signintime".equals(cloumname)) {
				a = mapRow.get("signintime");
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
				for (Branch b : bList) {
					if (b.getBranchid() == Long.parseLong(mapRow.get("deliverybranchid").toString())) {
						a = b.getBranchname();
						break;
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
				if (realbranchid == 0) {
					realbranchid = Long.parseLong(mapRow.get("startbranchid").toString());
				}
				a = getRealflowordertype(bList, realbranchid, Long.parseLong(mapRow.get("flowordertype").toString()));
			} else {
				a = mapRow.get(cloumname);
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
		} else if (flowordertype == FlowOrderTypeEnum.RuKu.getValue() && sitetype == BranchEnum.KuFang.getValue()) {
			return RealFlowOrderTypeEnum.KuFangRuKu.getText();
		} else if (flowordertype == FlowOrderTypeEnum.RuKu.getValue() && sitetype == BranchEnum.ZhongZhuan.getValue()) {
			return RealFlowOrderTypeEnum.ZhongZhuanZhanRuKu.getText();
		} else if (flowordertype == FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()) {
			return RealFlowOrderTypeEnum.TuiHuoZhanRuKu.getText();
		} else if (flowordertype == FlowOrderTypeEnum.ChuKuSaoMiao.getValue() && sitetype == BranchEnum.KuFang.getValue()) {
			return RealFlowOrderTypeEnum.KuFangChuKuSaoMiao.getText();
		} else if (flowordertype == FlowOrderTypeEnum.ChuKuSaoMiao.getValue() && sitetype == BranchEnum.ZhanDian.getValue()) {
			return RealFlowOrderTypeEnum.ZhongZhuanZhanChuZhanSaoMiao.getText();
		} else if (flowordertype == FlowOrderTypeEnum.ChuKuSaoMiao.getValue() && sitetype == BranchEnum.ZhongZhuan.getValue()) {
			return RealFlowOrderTypeEnum.ZhongZhuanZhanChuKuSaoMiao.getText();
		} else if (flowordertype == FlowOrderTypeEnum.ChuKuSaoMiao.getValue() && sitetype == BranchEnum.TuiHuo.getValue()) {
			return RealFlowOrderTypeEnum.TuiHuoZhanChuKuSaoMiao.getText();
		} else if (flowordertype == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) {
			return RealFlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getText();
		} else if (flowordertype == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue() && sitetype == BranchEnum.KuFang.getValue()) {
			return RealFlowOrderTypeEnum.KuFangYouHuoWuDanSaoMiao.getText();
		} else if (flowordertype == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()) {
			return RealFlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getText();
		} else if (flowordertype == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue() && sitetype == BranchEnum.ZhongZhuan.getValue()) {
			return RealFlowOrderTypeEnum.ZhongZhuanZhanYouHuoWuDanSaoMiao.getText();
		} else if (flowordertype == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue() && sitetype == BranchEnum.TuiHuo.getValue()) {
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
			a = ((Complaint) list.get(k)).getClass().getMethod("get" + cloumnName3[i]).invoke((Complaint) list.get(k));
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

}
