package cn.explink.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import cn.explink.dao.BaleCwbDao;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CommonDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.GotoClassAuditingDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.PayWayDao;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.RemarkDAO;
import cn.explink.dao.SetExportFieldDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.TuihuoRecordDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Bale;
import cn.explink.domain.BaleCwb;
import cn.explink.domain.Branch;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.GroupDetail;
import cn.explink.domain.PayWay;
import cn.explink.domain.PrintView;
import cn.explink.domain.Reason;
import cn.explink.domain.TuihuoRecord;
import cn.explink.domain.User;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.enumutil.UserEmployeestatusEnum;
import cn.explink.util.StringUtil;

@Service
public class WarehouseGroupDetailService {
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	SetExportFieldDAO setExportFieldDAO;
	@Autowired
	SetExportFieldDAO setexportfieldDAO;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	ExportService exportService;
	@Autowired
	ExportmouldDAO exportmouldDAO;
	@Autowired
	GotoClassAuditingDAO gotoClassAuditingDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	PayWayDao payWaydao;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	ReasonDao reasonDao;
	@Autowired
	CustomWareHouseDAO customWareHouseDAO;
	@Autowired
	RemarkDAO remarkDAO;
	@Autowired
	CommonDAO commonDAO;
	@Autowired
	SystemInstallDAO systemInstallDAO;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	TuihuoRecordDAO tuihuoRecordDAO;
	
	@Autowired
	BaleCwbDao baleCwbDao;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/*
	 * public List<PrintView> getChuKuView(List<GroupDetail> gdList,
	 * List<Customer> customerList,List<Branch> branchList){ List<PrintView>
	 * printViewList = new ArrayList<PrintView>(); if(gdList.size()>0 ){
	 * for(GroupDetail gd: gdList){ PrintView printView = new PrintView();
	 * printView.setCwb(gd.getCwb()); printView.setDeliverid(gd.getDeliverid());
	 * printView.setFlowordertype(gd.getFlowordertype());
	 * printView.setCustomername(this.getQueryCustomerName(customerList,
	 * gd.getCustomerid()));//供货商的名称
	 * printView.setNextbranchname(this.getQueryBranchName(branchList,
	 * gd.getNextbranchid()));//下一站
	 * printView.setOutstoreroomtime(gd.getCreatetime());//出库时间
	 * printViewList.add(printView);
	 * 
	 * }
	 * 
	 * } return printViewList; }
	 */

	public List<PrintView> getChuKuView(List<CwbOrder> clist, List<GroupDetail> gdList, List<Customer> customerList, List<Branch> branchList) {
		List<PrintView> printViewList = new ArrayList<PrintView>();
		if (gdList.size() > 0 && clist.size() > 0) {
			for (CwbOrder co : clist) {
				for (GroupDetail gd : gdList) {
					if (co.getCwb().trim().equals(gd.getCwb().trim())) {
						PrintView printView = new PrintView();
						// BeanUtils.copyProperties(co, printView);
						printView.setCwb(co.getCwb());
						printView.setDeliverid(gd.getDeliverid());
						printView.setFlowordertype(co.getFlowordertype());
						printView.setBaleno(gd.getBaleno()!=null&&gd.getBaleno().trim().length()>0?gd.getBaleno():co.getPackagecode());
						printView.setCustomername(this.getQueryCustomerName(customerList, co.getCustomerid()));// 供货商的名称
						printView.setNextbranchname(this.getQueryBranchName(branchList, gd.getNextbranchid()));// 下一站
						printView.setOutstoreroomtime(gd.getCreatetime());// 出库时间
						printView.setCwbordertypeid(co.getCwbordertypeid());
						printViewList.add(printView);
						break;
					}
				}
			}
		}
		return printViewList;
	}

	public Map<String, Map<String, String>> getOrderFlowByCredateForDetailAndExportAllTime(List<String> cwbs, List<Branch> branchlist) {

		Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
		try {
			List<OrderFlow> ofList = orderFlowDAO.getOrderFlowByCwbs(cwbs);
			for (OrderFlow of : ofList) {
				if (of.getFlowordertype() == FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue()) {
					getCwbRow(map, of).put("customerbrackhouseremark", of.getComment());
				} else if (of.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue()) {
					getCwbRow(map, of).put("Instoreroomtime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
					if (getsitetype(branchlist, of.getBranchid()) == BranchEnum.ZhongZhuan.getValue()) {
						getCwbRow(map, of).put("zhongzhuanzhanIntime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
					}
				} else if (of.getFlowordertype() == FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue()) {
					getCwbRow(map, of).put("zhongzhuanzhanIntime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
				} else if (of.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() || of.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()) {
					getCwbRow(map, of).put("InSitetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
				} else if (of.getFlowordertype() == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
					getCwbRow(map, of).put("PickGoodstime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
				} else if (of.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) {
					getCwbRow(map, of).put("Outstoreroomtime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
					if (getsitetype(branchlist, of.getBranchid()) == BranchEnum.ZhongZhuan.getValue()) {
						getCwbRow(map, of).put("zhongzhuanzhanOuttime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
					}
				} else if (of.getFlowordertype() == FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue()) {
					getCwbRow(map, of).put("zhongzhuanzhanOuttime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
				} else if (of.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) {
					getCwbRow(map, of).put("Goclasstime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
				} else if (of.getFlowordertype() == FlowOrderTypeEnum.YiFanKui.getValue()) {
					getCwbRow(map, of).put("Gobacktime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
				} else if (of.getFlowordertype() == FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue()) {
					getCwbRow(map, of).put("tuigonghuoshangchukutime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
				}
				if (getCwbRow(map, of).get("Newchangetime") == null || of.getCredate().getTime() > new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(getCwbRow(map, of).get("Newchangetime")).getTime()) {
					getCwbRow(map, of).put("Newchangetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
				}
			}
		} catch (Exception e) {
		}
		return map;
	}

	private long getsitetype(List<Branch> branchlist, long branchid) {
		long sitetype = 0;
		if (branchlist.size() > 0) {
			for (Branch b : branchlist) {
				if (branchid == b.getBranchid()) {
					sitetype = b.getSitetype();
					break;
				}
			}
		}
		return sitetype;
	}

	private Map<String, String> getCwbRow(Map<String, Map<String, String>> map, OrderFlow of) {
		if (map.get(of.getCwb()) == null) {
			map.put(of.getCwb(), new HashMap<String, String>());
		}
		Map<String, String> map2 = map.get(of.getCwb());
		return map2;
	}

	public Map<String, String> getOrderFlowByCredateForDetailAndExportAllTime(String cwb) {
		Map<String, String> reMap = new HashMap<String, String>();
		try {
			List<OrderFlow> ofList = orderFlowDAO.getOrderFlowByCwb(cwb);
			for (OrderFlow of : ofList) {
				if (of.getFlowordertype() == FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue()) {
					reMap.put("customerbrackhouseremark", of.getComment());
				} else if (of.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue()) {
					reMap.put("Instoreroomtime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
				} else if (of.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() || of.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()) {
					reMap.put("InSitetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
				} else if (of.getFlowordertype() == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
					reMap.put("PickGoodstime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
				} else if (of.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) {
					reMap.put("Outstoreroomtime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
				} else if (of.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) {
					reMap.put("Goclasstime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
				} else if (of.getFlowordertype() == FlowOrderTypeEnum.YiFanKui.getValue()) {
					reMap.put("Gobacktime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
				} else if (of.getFlowordertype() == FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue()) {
					reMap.put("tuigonghuoshangchukutime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
				}
			}

			if (ofList.size() > 0) {
				reMap.put("Newchangetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ofList.get(ofList.size() - 1).getCredate()));
			}
		} catch (Exception e) {
		}
		return reMap;
	}

	// 获取deliverystateids
	public String getDeliveryStateCwbs(List<DeliveryState> deliverystateList) {
		StringBuffer str = new StringBuffer();
		String deliverystatecwbs;
		if (deliverystateList.size() > 0) {
			for (DeliveryState d : deliverystateList) {
				str.append("'").append(d.getCwb()).append("',");
			}
		} else {
			str.append("'").append("',");
		}
		deliverystatecwbs = str.substring(0, str.length() - 1);
		return deliverystatecwbs;
	}

	// 获取paywayid
	public String getPayWayId(List<PayWay> paywayList) {
		StringBuffer str = new StringBuffer();
		String paywayid;
		if (paywayList.size() > 0) {
			for (PayWay p : paywayList) {
				str.append("'").append(p.getId()).append("',");
			}
		} else {
			str.append("'").append("',");
		}
		paywayid = str.substring(0, str.length() - 1);
		return paywayid;
	}

	// 获取orderflowid
	public String getOrderFlowCwbs(List<String> orderFlowCwbList) {
		StringBuffer str = new StringBuffer();
		String orderflowid = "";
		if (orderFlowCwbList.size() > 0) {
			for (String cwb : orderFlowCwbList) {
				str.append("'").append(cwb).append("',");
			}
		} else {
			str.append("'").append("',");
		}
		orderflowid = str.substring(0, str.length() - 1);
		return orderflowid;
	}

	// 获取
	public String getDeliveryCwbs(List<DeliveryState> deliveryStateList) {
		StringBuffer str = new StringBuffer();
		String deliveryid;
		if (deliveryStateList.size() > 0) {
			for (DeliveryState ds : deliveryStateList) {
				str.append("'").append(ds.getCwb()).append("',");
			}
		} else {
			str.append("'").append("',");
		}
		deliveryid = str.substring(0, str.length() - 1);
		return deliveryid;
	}

	public TuihuoRecord getQueryTuihuo(List<TuihuoRecord> tuihuoRecordList, String cwb) {
		TuihuoRecord tuihuo = new TuihuoRecord();
		for (TuihuoRecord tr : tuihuoRecordList) {
			if (tr.getCwb().equals(cwb)) {
				tuihuo = tr;
				break;
			}
		}
		return tuihuo;
	}

	public String getQueryCustomerName(List<Customer> customerList, long customerid) {
		String customername = "";
		for (Customer c : customerList) {
			if (c.getCustomerid() == customerid) {
				customername = c.getCustomername();
				if (c.getIfeffectflag() != 1) {
					customername += "<strong style=\"color:red;font-size:10\">[已停用]</strong>";
				}
				break;
			}
		}
		return customername;
	}

	public String getQueryCustomWareHouse(List<CustomWareHouse> customerWareHouseList, long customerwarehouseid) {
		String customerwarehouse = "";
		for (CustomWareHouse ch : customerWareHouseList) {
			if (ch.getWarehouseid() == customerwarehouseid) {
				customerwarehouse = ch.getCustomerwarehouse();
				break;
			}
		}
		return customerwarehouse;
	}

	public String getQueryBranchName(List<Branch> branchList, long branchid) {
		String branchname = "";
		for (Branch b : branchList) {
			if (b.getBranchid() == branchid) {
				branchname = b.getBranchname();
				break;
			}
		}
		return branchname;
	}

	public String getQueryUserName(List<User> userList, long userid) {
		String username = "";
		for (User u : userList) {
			if (u.getUserid() == userid) {
				username = u.getRealname();
				if (u.getEmployeestatus() == UserEmployeestatusEnum.LiZhi.getValue()) {
					username = username + "(离职)";
				}
				break;
			}
		}
		return username;
	}

	public String getQueryReason(List<Reason> reasonList, long reasonid) {
		String reasoncontent = "";
		for (Reason r : reasonList) {
			if (r.getReasonid() == reasonid) {
				reasoncontent = r.getReasoncontent();
				break;
			}
		}
		return reasoncontent;
	}

	public OrderFlow getOrderFlowByCwbAndType(String cwb, long flowordertype, String begindate, String enddate) {
		List<OrderFlow> orderflowList = new ArrayList<OrderFlow>();
		orderflowList = orderFlowDAO.getOrderFlowByCwbAndFlowordertype(cwb, flowordertype, begindate, enddate);
		OrderFlow orderflow = orderflowList.size() > 0 ? orderflowList.get(orderflowList.size() - 1) : new OrderFlow();
		return orderflow;
	}

	public OrderFlow getOrderFlowByCwb(String cwb) {
		List<OrderFlow> orderflowList = new ArrayList<OrderFlow>();
		orderflowList = orderFlowDAO.getAdvanceOrderFlowByCwb(cwb);
		OrderFlow orderflow = orderflowList.size() > 0 ? orderflowList.get(orderflowList.size() - 1) : new OrderFlow();
		return orderflow;
	}

	public DeliveryState getDeliveryStateByCwb(String cwb) {
		List<DeliveryState> deliveryStateList = new ArrayList<DeliveryState>();
		deliveryStateList = deliveryStateDAO.getDeliveryStateByCwb(cwb);
		DeliveryState deliverState = deliveryStateList.size() > 0 ? deliveryStateList.get(deliveryStateList.size() - 1) : new DeliveryState();
		return deliverState;
	}

	public String getPayWayType(String cwb, DeliveryState ds) {
		StringBuffer str = new StringBuffer();
		String paywaytype = "";
		if (ds.getCash().compareTo(BigDecimal.ZERO) == 1) {
			str.append("现金,");
		}
		if (ds.getPos().compareTo(BigDecimal.ZERO) == 1) {
			str.append("POS,");
		}
		if (ds.getCheckfee().compareTo(BigDecimal.ZERO) == 1) {
			str.append("支票,");
		}
		if (ds.getOtherfee().compareTo(BigDecimal.ZERO) == 1) {
			str.append("其它,");
		}
		if (str.length() > 0) {
			paywaytype = str.substring(0, str.length() - 1);
		} else {
			paywaytype = "现金";
		}
		return paywaytype;
	}

	public String getOldPayWayType(long payupid) {
		StringBuffer str = new StringBuffer();
		for (PaytypeEnum pe : PaytypeEnum.values()) {
			if (payupid == pe.getValue()) {
				str.append(pe.getText());
			}
		}

		return str.toString();
	}

	public DeliveryState getDeliveryByCwb(String cwb) {
		List<DeliveryState> delvieryList = deliveryStateDAO.getDeliveryStateByCwb(cwb);
		return delvieryList.size() > 0 ? delvieryList.get(delvieryList.size() - 1) : new DeliveryState();
	}

	public boolean checkBranchRepeat(List<Branch> branchlist, Branch branch) {
		for (int i = 0; i < branchlist.size(); i++) {
			if (branch.getBranchname().equals(branchlist.get(i).getBranchname())) {
				return true;
			}
		}
		return false;
	}

	public String getCwbs(String flowordertypes, String begindate, String enddate, String[] kufangids, String[] branchids) {
		String kufangidStr = "";
		String branchidStr = "";
		if (kufangids != null && kufangids.length > 0) {
			for (String kufangid : kufangids) {
				kufangidStr += kufangid + ",";
			}
			kufangidStr = (kufangidStr.length() > 0 ? kufangidStr.substring(0, kufangidStr.length() - 1) : "");
		}
		if (branchids != null && branchids.length > 0) {
			for (String branchid : branchids) {
				branchidStr += branchid + ",";
			}
			branchidStr = (branchidStr.length() > 0 ? branchidStr.substring(0, branchidStr.length() - 1) : "");
		}
		List<String> cwblist = orderFlowDAO.getOneCwbs(flowordertypes, begindate, enddate, kufangidStr, branchidStr);
		return this.getCwbs(cwblist);
	}

	public String getCwbs(List<String> cwblist) {
		StringBuffer str = new StringBuffer();
		String cwbs = "";
		if (cwblist != null && cwblist.size() > 0) {
			for (String cwb : cwblist) {
				str.append("'").append(cwb).append("',");
			}
			cwbs = str.substring(0, str.length() - 1);
		}
		return cwbs;
	}

	public String getStrings(String[] strArr) {
		String strs = "";
		if (strArr.length > 0) {
			for (String str : strArr) {
				strs += str + ",";
			}
		}

		if (strs.length() > 0) {
			strs = strs.substring(0, strs.length() - 1);
		}
		return strs;
	}

	public String getStrings(List<String> strArr) {
		String strs = "";
		if (strArr.size() > 0) {
			for (String str : strArr) {
				strs += "'" + str + "',";
			}
		}

		if (strs.length() > 0) {
			strs = strs.substring(0, strs.length() - 1);
		}
		return strs;
	}

	public List<String> getList(String[] strArr) {
		List<String> strList = new ArrayList<String>();
		if (strArr != null && strArr.length > 0) {
			for (String str : strArr) {
				strList.add(str);
			}
		}
		return strList;
	}

	public List<PrintView> getChuKuBaleView(List<GroupDetail> gdList, List<Bale> baleList, List<Branch> branchList) {
		List<PrintView> printViewList = new ArrayList<PrintView>();
		if (baleList != null && !baleList.isEmpty()) {
			for (Bale bale : baleList) {
				if (gdList != null && !gdList.isEmpty()) {
					for (GroupDetail gd : gdList) {
						if (bale.getId()==gd.getBaleid()) {
							PrintView printView = new PrintView();
							printView.setBaleid(bale.getId());
							printView.setPackagecode(bale.getBaleno());
							printView.setCustomerwarehouseid(this.getQueryBranchName(branchList, bale.getBranchid()));// 发货仓库
							printView.setOutstoreroomtime(bale.getCretime().toString());// 出库时间
							printView.setNextbranchname(this.getQueryBranchName(branchList, gd.getNextbranchid()));// 下一站
							printViewList.add(printView);
							break;
						}
					}
				}
			}
		}
		return printViewList;
	}

	public Map<String, Object> getChuKuBaleCwbView(List<Map<String, Object>> cwblist, String baleno,long baleid) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (cwblist != null && !cwblist.isEmpty()) {
				map.put("baleno", baleno);// 供货商的名称
				map.put("count", cwblist.get(0).get("count"));
				map.put("receivablefee", cwblist.get(0).get("receivablefee"));
				map.put("sendcarnum", baleCwbDao.getCwbCountByBaleId(baleid));
				map.put("carrealweight", cwblist.get(0).get("carrealweight"));
		}
		return map;
	}
	
	/**
	 * 查询打印
	 * @date 2016年8月11日 下午3:54:21
	 * @param cwbs
	 * @param nextbranchid
	 * @param branchid
	 * @param flowordertype
	 * @param cwbOrderTypeId
	 * @return
	 */
	public List<CwbOrder> getCwbByCwbsAndcwbTypeForPrint(String[] cwbs, long[] nextbranchid, long branchid,
			long flowordertype, String cwbOrderTypeId) {
		String cwbsStr = StringUtil.toDbInStr(cwbs);
		String nextbranchidStr = StringUtil.toDbInStr(nextbranchid);
		return this.cwbDAO.getCwbByCwbsAndcwbTypeForPrint(cwbsStr, nextbranchidStr, branchid, flowordertype, cwbOrderTypeId);
	}
}
