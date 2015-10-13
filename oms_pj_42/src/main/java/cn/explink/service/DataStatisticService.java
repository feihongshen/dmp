package cn.explink.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.ChukuDataResultDAO;
import cn.explink.dao.CommonDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryChukuDAO;
import cn.explink.dao.DownloadManagerDAO;
import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.KeHuFaHuoDataDao;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.ChukuDataResult;
import cn.explink.domain.Common;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.CwbOrderTail;
import cn.explink.domain.CwbTiHuo;
import cn.explink.domain.DeliveryChuku;
import cn.explink.domain.DeliveryDaohuo;
import cn.explink.domain.DeliveryJuShou;
import cn.explink.domain.DeliverySuccessful;
import cn.explink.domain.DeliveryZhiLiu;
import cn.explink.domain.DownloadManager;
import cn.explink.domain.KDKDeliveryChuku;
import cn.explink.domain.KuFangRuKuOrder;
import cn.explink.domain.KuFangZaiTuOrder;
import cn.explink.domain.Reason;
import cn.explink.domain.Remark;
import cn.explink.domain.SetExportField;
import cn.explink.domain.TuiHuoChuZhanOrder;
import cn.explink.domain.TuiHuoZhanRuKuOrder;
import cn.explink.domain.User;
import cn.explink.domain.ZhongZhuan;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.FlowOrderColumnMap;
import cn.explink.enumutil.ModelEnum;
import cn.explink.enumutil.UserEmployeestatusEnum;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.ExcelUtils;
import cn.explink.util.Page;
import cn.explink.util.StreamingStatementCreator;

@Service
public class DataStatisticService {

	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	GetDmpDAO getDmpDAO;
	@Autowired
	ExportExcelService exportExcelService;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	DownloadManagerDAO downloadManagerDAO;
	@Autowired
	DeliveryChukuDAO deliveryChukuDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	CommonDAO commonDAO;
	@Autowired
	CustomWareHouseDAO customWareHouseDAO;
	@Autowired
	ExportService exportService;
	@Autowired
	DownloadManagerService downloadManagerService;
	@Autowired
	ChukuDataResultDAO chukuDataResultDAO;
	@Autowired
	KeHuFaHuoDataDao keHuFaHuoDataDao;
	@Autowired
	ExportmouldDAO exportmouldDAO;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public boolean checkBranchRepeat(List<Branch> branchlist, Branch branch) {
		for (int i = 0; i < branchlist.size(); i++) {
			if (branch.getBranchname().equals(branchlist.get(i).getBranchname())) {
				return true;
			}
		}
		return false;
	}

	public List<String> getList(String[] strArr) {
		List<String> strList = new ArrayList<String>();
		if ((strArr != null) && (strArr.length > 0)) {
			for (String str : strArr) {
				strList.add(str);
			}
		}
		return strList;
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

	/**
	 * 订单数*（customerList+branchList+userList） 谨慎使用
	 *
	 * @param clist
	 * @param delList
	 * @param customerList
	 * @param branchList
	 * @param userList
	 * @return
	 */
	public List<CwbOrder> getCwbOrderViewCount10(List<CwbOrder> clist, List<DeliverySuccessful> delList, List<Customer> customerList, List<Branch> branchList, List<User> userList) {
		List<CwbOrder> cwbOrderViewList = new ArrayList<CwbOrder>();
		if ((delList.size() > 0) && (clist.size() > 0)) {
			for (DeliverySuccessful delSuc : delList) {
				for (CwbOrder cwborder : clist) {
					if (cwborder.getCwb().equals(delSuc.getCwb())) {
						CwbOrder cwbOrderView = cwborder;
						cwbOrderView.setCustomername(this.getQueryCustomerName(customerList, delSuc.getCustomerid()));// 供货商的名称
						cwbOrderView.setDeliverybranch(this.getQueryBranchName(branchList, delSuc.getBranchid()));// 配送站点
						cwbOrderView.setDelivername(this.getQueryUserName(userList, delSuc.getDeliveryid()));// 小件员
						cwbOrderView.setPaytype(delSuc.getPaywayid() + "");
						cwbOrderViewList.add(cwbOrderView);
						break;
					}
				}

			}
		}
		return cwbOrderViewList;
	}

	/**
	 * 库房出库统计封装实体
	 *
	 * @param clist
	 * @param delList
	 * @param customerList
	 * @param branchList
	 * @param userList
	 * @return
	 */
	public List<CwbOrder> getChukuCwbOrderViewCount10(List<CwbOrder> clist, List<DeliveryChuku> delList, List<Customer> customerList, List<Branch> branchList, List<User> userList) {
		List<CwbOrder> cwbOrderViewList = new ArrayList<CwbOrder>();
		if ((delList.size() > 0) && (clist.size() > 0)) {
			for (DeliveryChuku delChuku : delList) {
				for (CwbOrder cwborder : clist) {
					if (cwborder.getCwb().equals(delChuku.getCwb())) {
						CwbOrder cwbOrderView = cwborder;
						cwbOrderView.setCustomername(this.getQueryCustomerName(customerList, delChuku.getCustomerid()));// 供货商的名称
						cwbOrderView.setDeliverybranch(this.getQueryBranchName(branchList, cwborder.getDeliverybranchid()));// 配送站点
						cwbOrderView.setNextbranchname(this.getQueryBranchName(branchList, delChuku.getNextbranchid()));// 下一站
						cwbOrderView.setOutstoreroomtime(delChuku.getOutstoreroomtime());// 出库时间
						cwbOrderView.setPaytype(cwborder.getPaytypeNameOld());// 原支付方式
						cwbOrderView.setOperatorName(this.getQueryUserName(userList, delChuku.getOutstoreroomtimeuserid()));// 出库操作人
						cwbOrderViewList.add(cwbOrderView);
						break;
					}
				}
			}
		}
		return cwbOrderViewList;
	}

	/**
	 * 库对库出库统计封装实体
	 *
	 * @param clist
	 * @param delList
	 * @param customerList
	 * @param branchList
	 * @param userList
	 * @return
	 */
	public List<CwbOrder> getKDKChukuCwbOrderViewCount10(List<CwbOrder> clist, List<KDKDeliveryChuku> delList, List<Customer> customerList, List<Branch> branchList, List<User> userList) {
		List<CwbOrder> cwbOrderViewList = new ArrayList<CwbOrder>();
		if ((delList.size() > 0) && (clist.size() > 0)) {
			for (KDKDeliveryChuku delChuku : delList) {
				for (CwbOrder cwborder : clist) {
					if (cwborder.getCwb().equals(delChuku.getCwb())) {
						CwbOrder cwbOrderView = cwborder;
						cwbOrderView.setCustomername(this.getQueryCustomerName(customerList, delChuku.getCustomerid()));// 供货商的名称
						cwbOrderView.setDeliverybranch(this.getQueryBranchName(branchList, cwborder.getDeliverybranchid()));// 配送站点
						cwbOrderView.setNextbranchname(this.getQueryBranchName(branchList, delChuku.getNextbranchid()));// 下一站
						cwbOrderView.setOutstoreroomtime(delChuku.getOutstoreroomtime());// 出库时间
						cwbOrderView.setPaytype(cwborder.getPaytypeNameOld());// 原支付方式
						cwbOrderViewList.add(cwbOrderView);
						break;
					}
				}
			}
		}
		return cwbOrderViewList;
	}

	/**
	 * 分站到货统计封装实体
	 *
	 * @param clist
	 * @param delList
	 * @param customerList
	 * @param branchList
	 * @param userList
	 * @return
	 */
	public List<CwbOrder> getDaohuoCwbOrderViewCount10(List<CwbOrder> clist, List<DeliveryDaohuo> delList, List<Customer> customerList, List<Branch> branchList, List<User> userList) {
		List<CwbOrder> cwbOrderViewList = new ArrayList<CwbOrder>();
		if ((delList.size() > 0) && (clist.size() > 0)) {
			for (DeliveryDaohuo delDaohuo : delList) {
				for (CwbOrder cwborder : clist) {
					if (cwborder.getCwb().equals(delDaohuo.getCwb())) {
						CwbOrder cwbOrderView = cwborder;
						cwbOrderView.setCustomername(this.getQueryCustomerName(customerList, delDaohuo.getCustomerid()));// 供货商的名称
						cwbOrderView.setStartbranchname(this.getQueryBranchName(branchList, delDaohuo.getStartbranchid()));// 上一站点
						cwbOrderView.setCurrentbranchname(this.getQueryBranchName(branchList, delDaohuo.getCurrentbranchid()));// 当前站点
						cwbOrderView.setInSitetime(delDaohuo.getInSitetime());// 到货时间
						cwbOrderView.setDeliverybranch(this.getQueryBranchName(branchList, cwborder.getDeliverybranchid()));// 配送站点
						cwbOrderViewList.add(cwbOrderView);
						break;
					}
				}
			}
		}
		return cwbOrderViewList;
	}

	/**
	 * 中转订单统计封装实体
	 *
	 * @param clist
	 * @param zhongzhuanList
	 * @param customerList
	 * @param branchList
	 * @param userList
	 * @return
	 */
	public List<CwbOrder> getZhongzhuanCwbOrderViewCount10(List<CwbOrder> clist, List<ZhongZhuan> zhongzhuanList, List<Customer> customerList, List<Branch> branchList, List<User> userList) {
		List<CwbOrder> cwbOrderViewList = new ArrayList<CwbOrder>();
		if ((zhongzhuanList.size() > 0) && (clist.size() > 0)) {
			for (ZhongZhuan zhongZhuan : zhongzhuanList) {
				for (CwbOrder cwborder : clist) {
					if (cwborder.getCwb().equals(zhongZhuan.getCwb())) {
						CwbOrder cwbOrderView = cwborder;
						cwbOrderView.setCustomername(this.getQueryCustomerName(customerList, zhongZhuan.getCustomerid()));// 供货商的名称
						cwbOrderView.setInsitebranchname(this.getQueryBranchName(branchList, zhongZhuan.getInsitebranchid()));// 到货站点
						cwbOrderView.setInSitetime(cwborder.getInSitetime());// 到货时间
						cwbOrderView.setCarwarehousename(this.getQueryBranchName(branchList, Long.parseLong(cwborder.getCarwarehouse())));
						cwbOrderViewList.add(cwbOrderView);
						break;
					}
				}
			}
		}
		return cwbOrderViewList;
	}

	/**
	 * 提货订单统计封装实体
	 *
	 * @param clist
	 * @param tihuoList
	 * @param customerList
	 * @return
	 */
	public List<CwbOrder> getTiHuoCwbOrderViewCount10(List<CwbOrder> clist, List<CwbTiHuo> tihuoList, List<Customer> customerList) {
		List<CwbOrder> cwbOrderViewList = new ArrayList<CwbOrder>();
		if ((tihuoList.size() > 0) && (clist.size() > 0)) {
			for (CwbTiHuo cwbTiHuo : tihuoList) {
				for (CwbOrder cwborder : clist) {
					if (cwborder.getCwb().equals(cwbTiHuo.getCwb())) {
						CwbOrder cwbOrderView = cwborder;
						cwbOrderView.setCustomername(this.getQueryCustomerName(customerList, cwbTiHuo.getCustomerid()));// 供货商的名称
						cwbOrderView.setCreatetime(cwbTiHuo.getTihuotime());
						cwbOrderViewList.add(cwbOrderView);
						break;
					}
				}
			}
		}
		return cwbOrderViewList;
	}

	/**
	 * 拒收订单 汇总 封装
	 *
	 * @param orderlist
	 * @param djList
	 * @param customerlist
	 * @param branchList
	 * @param userList
	 * @param reasonList
	 * @param begindate
	 * @param enddate
	 * @param remarkList
	 * @return
	 */
	public List<CwbOrder> getJuShouCwbOrderViewCount10(List<CwbOrder> orderlist, List<DeliveryJuShou> djList, List<Customer> customerlist, List<Branch> branchList, List<Reason> reasonList) {
		List<CwbOrder> cwbOrderViewList = new ArrayList<CwbOrder>();
		if ((djList.size() > 0) && (orderlist.size() > 0)) {
			for (DeliveryJuShou dj : djList) {
				for (CwbOrder cwbOrder : orderlist) {
					if (dj.getCwb().equals(cwbOrder.getCwb())) {
						CwbOrder order = cwbOrder;
						order.setCustomername(this.getQueryCustomerName(customerlist, cwbOrder.getCustomerid()));
						order.setDeliverybranch(this.getQueryBranchName(branchList, dj.getBranchid()));
						order.setBackreason(cwbOrder.getBackreason());
						order.setAuditstate(dj.getGcaid());
						cwbOrderViewList.add(order);
					}
				}
			}
		}
		return cwbOrderViewList;
	}

	public String getQueryCustomerName(List<Customer> customerList, long customerid) {
		String customername = "";
		for (Customer c : customerList) {
			if (c.getCustomerid() == customerid) {
				customername = c.getCustomername();
				break;
			}
		}
		return customername;
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

	/**
	 * 检查导出状况，是否重复导出
	 *
	 * @param response
	 * @param request
	 * @param page
	 * @param sign
	 * @param userid
	 * @return
	 */
	public String DataStatisticsExportExcelCheck(HttpServletResponse response, HttpServletRequest request, long page, long sign, long userid) {
		if (sign == ModelEnum.TuoTouDingdanhuizong.getValue()) {
			return this.checkTuotou(response, request, page, userid);
		}

		if (sign == ModelEnum.KuFangChuKuDingdantongji.getValue()) {
			return this.checkOutWarehouseData(response, request, page, userid);
		}
		if (sign == ModelEnum.ZhiLiuDingdanhuizong.getValue()) {
			return this.checkZhiLiuHuiZong(response, request, page, userid);
		}
		if (sign == ModelEnum.FenZhanDaoHuotongji.getValue()) {
			return this.checkDaoHuoData(response, request, page, userid);
		}
		if (sign == ModelEnum.ZhanDianDaoHuohuizong.getValue()) {
			return this.checkDaoHuoHuiZong(response, request, userid);
		}
		if (sign == ModelEnum.TuiHuoChuZhanTongJi.getValue()) {
			return this.checkTuiHuoChuZhanTongJi(response, request, page, userid);
		}
		if (sign == ModelEnum.ZhongZhuanDingDanTongJi.getValue()) {
			return this.checkZhongZhuanTongJi(response, request, userid);
		}

		if (sign == ModelEnum.JuShouDingdanhuizong.getValue()) {
			return this.checkJuShouHuiZong(response, request, page, userid);
		}

		if (sign == ModelEnum.TiHuoDingDanTongJi.getValue()) {
			return this.checkTiHuoDingDanTongJi(response, request, page, userid);
		}
		// 库房出库统计（云）导出报表
		if (sign == ModelEnum.KuFangChuKuHuiZong.getValue()) {
			return this.checkKuFangChuKuHuiZong(response, request, userid);
		}

		if (sign == ModelEnum.KDKChuKuDingdantongji.getValue()) {
			return this.checkKDKOutWarehouseData(response, request, page, userid);
		}
		if (sign == ModelEnum.TuiHuoZhanRuKuTongJi.getValue()) {
			return this.checkTuiHuoZhanRuKuTongJi(response, request, page, userid);
		}
		if (sign == ModelEnum.KuFangZaiTuTongJi.getValue()) {
			return this.checkKuFangZaiTuTongJi(response, request, page, userid);
		}
		if (sign == ModelEnum.KuFangRuKuTongJi.getValue()) {

			return this.checkKuFangRuKuTongJi(response, request, page, userid);
		}
		if (sign == ModelEnum.KeHuFaHuoTongJi.getValue()) {
			return this.checkKeHuFaHuoTongJi(response, request, page, userid);
		}
		if (sign == ModelEnum.KeHuFaHuoHuiZong.getValue()) {

			return this.checkKeHuFaHuoHuiZong(response, request, userid);
		}
		if (sign == ModelEnum.ZongHeChaXun.getValue()) {
			return this.checkZongHeChaXun(response, request, page, userid);
		}
		if (sign == ModelEnum.DanliangChaxun.getValue()) {
			return this.checkDanliangchaxun(response, request, page, userid);
		}
		/*
		 * if (sign==ModelEnum.FenZhanDaoHuoHuiZong.getValue()) { return
		 * checkFenZhanDaoHuoHuiZong(response,request,userid); }
		 */
		return "{}";
	}

	/**
	 * 客户发货汇总 报表形式
	 *
	 * @param response
	 * @param request
	 * @param page
	 * @param userid
	 * @return
	 */
	private String checkKeHuFaHuoHuiZong(HttpServletResponse response, HttpServletRequest request, long userid) {
		JSONObject json = new JSONObject();
		try {
			// 查询出数据
			String[] cwbordertypeid = request.getParameterValues("cwbordertypeid1") == null ? new String[] {} : request.getParameterValues("cwbordertypeid1");
			String begindate = request.getParameter("begindate1") == null ? "" : request.getParameter("begindate1").toString();
			String enddate = request.getParameter("enddate1") == null ? "" : request.getParameter("enddate1").toString();
			String[] kufangid = request.getParameterValues("kufangid1") == null ? new String[] {} : request.getParameterValues("kufangid1");
			long flowordertype = request.getParameter("flowordertype1") == null ? -1 : Long.parseLong(request.getParameter("flowordertype1"));
			String[] customerids = request.getParameterValues("customerid1") == null ? new String[] {} : request.getParameterValues("customerid1");
			String servicetype = request.getParameter("servicetype1") == null ? "全部" : request.getParameter("servicetype1").toString();
			String customers = "";
			if (customerids.length > 0) {
				customers = this.getStrings(customerids);
			}
			String kufangids = "";
			if (kufangid.length > 0) {
				kufangids = this.getStrings(kufangid);
			}
			String cwbordertypeids = "";
			if (cwbordertypeid.length > 0) {
				cwbordertypeids = this.getStrings(cwbordertypeid);
			}
			// 验证导出条件是否存在
			String datajson = this.setKeHuFaHuoHuiZongJson(begindate, enddate, customers, cwbordertypeids, kufangids, userid, flowordertype, servicetype);

			DownloadManager down = this.downloadManagerDAO.getDownloadManagerByJson(datajson);

			if (down != null) {
				json.put("errorCode", 2);
				json.put("remark", "该导出操作已经在下载列表中！");
				return json.toString();
			} else {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
				String fileName = "Order_" + df.format(new Date()) + "_"; // 文件名
				String otherName = "报表";
				String lastStr = ".xlsx";// 文件名后缀
				fileName = fileName + otherName + lastStr;
				String cnfilename = "客户发货汇总_" + df.format(new Date()) + "_" + otherName + lastStr;
				String fileUrl = this.getDmpDAO.getFileUrl() + "download" + System.getProperty("file.separator") + new SimpleDateFormat("yyyy-MM-dd").format(new Date())
						+ System.getProperty("file.separator");
				down = this.setDownloadManager(datajson, fileName, cnfilename, fileUrl, ModelEnum.KeHuFaHuoHuiZong.getValue(), userid);
				this.downloadManagerDAO.creDownloadManager(down);
				json.put("errorCode", 0);
				json.put("remark", "已进入离线导出");
				return json.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("errorCode", 2);
			json.put("remark", "系统导出异常！");
			return json.toString();
		}
	}

	private String setKeHuFaHuoHuiZongJson(String begindate, String enddate, String customers, String cwbordertypeids, String kufangids, long userid, long flowordertype, String servicetype) {
		JSONObject json = new JSONObject();
		json.put("begindate", begindate);
		json.put("enddate", enddate);
		json.put("customers", customers);
		json.put("cwbordertypeids", cwbordertypeids);
		json.put("kufangids", kufangids);
		json.put("userid", userid);
		json.put("flowordertype", flowordertype);
		json.put("servicetype", servicetype);
		return json.toString();
	}

	private String setFenZhanDaoHuoHuiZongJson(String begindate, String enddate, String customers, String cwbordertypeids, String currentbranchid, String kufangid, long userid, Integer isshow) {
		JSONObject json = new JSONObject();
		json.put("begindate", begindate);
		json.put("enddate", enddate);
		json.put("customers", customers);
		json.put("kufangid", kufangid);
		json.put("cwbordertypeids", cwbordertypeids);
		json.put("currentbranchid", currentbranchid);
		json.put("isshow", isshow);
		json.put("userid", userid);
		return json.toString();
	}

	/**
	 * 客户发货统计 导出
	 *
	 * @param response
	 * @param request
	 * @param page
	 * @param userid
	 * @return
	 */
	private String checkKeHuFaHuoTongJi(HttpServletResponse response, HttpServletRequest request, long page, long userid) {
		JSONObject json = new JSONObject();
		String mouldfieldids = request.getParameter("exportmould2"); // 导出模板

		try {
			// 查询出数据
			String[] cwbordertypeid = request.getParameterValues("cwbordertypeid1") == null ? new String[] {} : request.getParameterValues("cwbordertypeid1");
			String begindate = request.getParameter("begindate1") == null ? "" : request.getParameter("begindate1").toString();
			String enddate = request.getParameter("enddate1") == null ? "" : request.getParameter("enddate1").toString();
			String[] kufangid = request.getParameterValues("kufangid1") == null ? new String[] {} : request.getParameterValues("kufangid1");
			long flowordertype = request.getParameter("flowordertype1") == null ? -1 : Long.parseLong(request.getParameter("flowordertype1"));
			String[] customerids = request.getParameterValues("customerid1") == null ? new String[] {} : request.getParameterValues("customerid1");
			String servicetype = request.getParameter("servicetype1") == null ? "全部" : request.getParameter("servicetype1").toString();
			String customers = "";
			if (customerids.length > 0) {
				customers = this.getStrings(customerids);
			}
			String kufangids = "";
			Branch branch = this.getDmpDAO.getNowBranch(this.getDmpDAO.getUserById(userid).getBranchid());
			List<Branch> kufangList = this.getDmpDAO.getQueryBranchByBranchsiteAndUserid(userid,
					BranchEnum.KuFang.getValue() + "," + BranchEnum.TuiHuo.getValue() + "," + BranchEnum.ZhongZhuan.getValue());

			if ((branch.getSitetype() == BranchEnum.KuFang.getValue()) || (branch.getSitetype() == BranchEnum.TuiHuo.getValue()) || (branch.getSitetype() == BranchEnum.ZhongZhuan.getValue())) {
				if (kufangList.size() == 0) {
					kufangList.add(branch);
				} else {
					if (!this.checkBranchRepeat(kufangList, branch)) {
						kufangList.add(branch);
					}
				}
			}
			if((kufangid.length==0)&&(kufangids.length()==0)){
				for(Branch kf:kufangList)
				{
					kufangids+=kf.getBranchid()+",";
				}
				if ((kufangids.length() > 0)&&kufangids.contains(",")) {
					kufangids = kufangids.substring(0, kufangids.length() - 1);
				}
			}
			if (kufangid.length > 0) {
				kufangids = this.getStrings(kufangid);
			}
			String cwbordertypeids = "";
			if (cwbordertypeid.length > 0) {
				cwbordertypeids = this.getStrings(cwbordertypeid);
			}
			// 验证导出条件是否存在
			String datajson = this.setKeHuFaHuoTongJiJson(begindate, enddate, customers, cwbordertypeids, kufangids, page, mouldfieldids, userid, flowordertype, servicetype);

			DownloadManager down = this.downloadManagerDAO.getDownloadManagerByJson(datajson);

			if (down != null) {
				json.put("errorCode", 2);
				json.put("remark", "该导出操作已经在下载列表中！");
				return json.toString();
			} else {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
				String fileName = "Order_" + df.format(new Date()) + "_"; // 文件名
				String otherName = "";
				String lastStr = ".xlsx";// 文件名后缀
				long count = Long.parseLong(request.getParameter("count") == null ? "0" : request.getParameter("count").toString());
				if (count > 0) {
					if (((count / Page.EXCEL_PAGE_NUMBER) + ((count % Page.EXCEL_PAGE_NUMBER) > 0 ? 1 : 0)) == 1) {
						otherName = "1-" + count;
					} else {
						otherName = ((((page * Page.EXCEL_PAGE_NUMBER) + 1) > count) ? count : ((page * Page.EXCEL_PAGE_NUMBER) + 1)) + "_"
								+ (((page + 1) * Page.EXCEL_PAGE_NUMBER) > count ? count : (page + 1) * Page.EXCEL_PAGE_NUMBER);
					}
				}
				fileName = fileName + otherName + lastStr;
				String cnfilename = "客户发货统计_" + df.format(new Date()) + "_" + otherName + lastStr;
				String fileUrl = this.getDmpDAO.getFileUrl() + "download" + System.getProperty("file.separator") + new SimpleDateFormat("yyyy-MM-dd").format(new Date())
						+ System.getProperty("file.separator");
				down = this.setDownloadManager(datajson, fileName, cnfilename, fileUrl, ModelEnum.KeHuFaHuoTongJi.getValue(), userid);
				this.downloadManagerDAO.creDownloadManager(down);
				json.put("errorCode", 0);
				json.put("remark", "已进入离线导出");
				return json.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("errorCode", 2);
			json.put("remark", "系统导出异常！");
			return json.toString();
		}
	}

	private String setKeHuFaHuoTongJiJson(String begindate, String enddate, String customers, String cwbordertypeids, String kufangids, long page, String mouldfieldids, long userid,
			long flowordertype, String servicetype) {
		JSONObject json = new JSONObject();
		json.put("begindate", begindate);
		json.put("enddate", enddate);
		json.put("customers", customers);
		json.put("cwbordertypeids", cwbordertypeids);
		json.put("kufangids", kufangids);
		json.put("page", page);
		json.put("mouldfieldids", mouldfieldids);
		json.put("userid", userid);
		json.put("flowordertype", flowordertype);
		json.put("servicetype", servicetype);
		return json.toString();
	}

	/**
	 * 库房入库统计(云) 导出
	 *
	 * @param response
	 * @param request
	 * @param page
	 * @param userid
	 * @return
	 */
	private String checkKuFangRuKuTongJi(HttpServletResponse response, HttpServletRequest request, long page, long userid) {
		JSONObject json = new JSONObject();
		String mouldfieldids = request.getParameter("exportmould2"); // 导出模板

		try {
			// 查询出数据
			long cwbordertypeid = request.getParameter("cwbordertypeid1") == null ? -2 : Long.parseLong(request.getParameter("cwbordertypeid1").toString());
			String begindate = request.getParameter("begindate1") == null ? "" : request.getParameter("begindate1").toString();
			String enddate = request.getParameter("enddate1") == null ? "" : request.getParameter("enddate1").toString();
			String emaildatebegin = request.getParameter("emaildatebegin1") == null ? "" : request.getParameter("emaildatebegin1").toString();
			String emaildateend = request.getParameter("emaildateend1") == null ? "" : request.getParameter("emaildateend1").toString();
			long kufangid = request.getParameter("kufangid1") == null ? -1 : Long.parseLong(request.getParameter("kufangid1"));
			String isruku = request.getParameter("isruku1") == null ? "false" : request.getParameter("isruku1").toString();
			String[] customerids = request.getParameterValues("customerids1") == null ? new String[] {} : request.getParameterValues("customerids1");
			String customers = "";
			if (customerids.length > 0) {
				customers = this.getStrings(customerids);
			}
			// 验证导出条件是否存在
			String datajson = this.setKuFangRuKuJson(isruku, begindate, enddate, emaildatebegin, emaildateend, customers, cwbordertypeid, kufangid, page, mouldfieldids, userid);

			DownloadManager down = this.downloadManagerDAO.getDownloadManagerByJson(datajson);

			if (down != null) {
				json.put("errorCode", 2);
				json.put("remark", "该导出操作已经在下载列表中！");
				return json.toString();
			} else {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
				String fileName = "Order_" + df.format(new Date()) + "_"; // 文件名
				String otherName = "";
				String lastStr = ".xlsx";// 文件名后缀
				long count = Long.parseLong(request.getParameter("count") == null ? "0" : request.getParameter("count").toString());
				if (count > 0) {
					if (((count / Page.EXCEL_PAGE_NUMBER) + ((count % Page.EXCEL_PAGE_NUMBER) > 0 ? 1 : 0)) == 1) {
						otherName = "1-" + count;
					} else {
						otherName = ((((page * Page.EXCEL_PAGE_NUMBER) + 1) > count) ? count : ((page * Page.EXCEL_PAGE_NUMBER) + 1)) + "_"
								+ (((page + 1) * Page.EXCEL_PAGE_NUMBER) > count ? count : (page + 1) * Page.EXCEL_PAGE_NUMBER);
					}
				}
				fileName = fileName + otherName + lastStr;
				String cnfilename = "库房入库统计_" + df.format(new Date()) + "_" + otherName + lastStr;
				String fileUrl = this.getDmpDAO.getFileUrl() + "download" + System.getProperty("file.separator") + new SimpleDateFormat("yyyy-MM-dd").format(new Date())
						+ System.getProperty("file.separator");
				down = this.setDownloadManager(datajson, fileName, cnfilename, fileUrl, ModelEnum.KuFangRuKuTongJi.getValue(), userid);
				this.downloadManagerDAO.creDownloadManager(down);
				json.put("errorCode", 0);
				json.put("remark", "已进入离线导出");
				return json.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("errorCode", 2);
			json.put("remark", "系统导出异常！");
			return json.toString();
		}
	}

	private String setKuFangRuKuJson(String isruku, String begindate, String enddate, String emaildatebegin, String emaildateend, String customers, long cwbordertypeid, long kufangid, long page,
			String mouldfieldids, long userid) {
		JSONObject json = new JSONObject();
		json.put("isruku", isruku);
		json.put("begindate", begindate);
		json.put("enddate", enddate);
		json.put("emaildatebegin", emaildatebegin);
		json.put("emaildateend", emaildateend);
		json.put("customers", customers);
		json.put("cwbordertypeid", cwbordertypeid);
		json.put("kufangid", kufangid);
		json.put("page", page);
		json.put("mouldfieldids", mouldfieldids);
		json.put("userid", userid);
		return json.toString();
	}

	/*
	 * private String setKuFangRuKuJson(long rukuflag, String starttime, String
	 * endtime, String customers, long cwbordertypeid, long kufangid, long page,
	 * String mouldfieldids, long userid) { JSONObject json=new JSONObject();
	 * json.put("rukuflag", rukuflag); json.put("starttime", starttime);
	 * json.put("endtime", endtime); json.put("customers", customers);
	 * json.put("cwbordertypeid", cwbordertypeid); json.put("kufangid",
	 * kufangid); json.put("page", page); json.put("mouldfieldids",
	 * mouldfieldids); json.put("userid", userid); return json.toString(); }
	 */

	/**
	 * 库房在途统计
	 *
	 * @param response
	 * @param request
	 * @param page
	 * @param userid
	 * @return
	 */
	private String checkKuFangZaiTuTongJi(HttpServletResponse response, HttpServletRequest request, long page, long userid) {
		JSONObject json = new JSONObject();
		String mouldfieldids = request.getParameter("exportmould2"); // 导出模板

		try {
			// 查询出数据

			String[] cwbordertypeids = request.getParameterValues("cwbordertypeid1") == null ? new String[] {} : request.getParameterValues("cwbordertypeid1");
			String[] nextbranchid = request.getParameterValues("nextbranchid1") == null ? new String[] {} : request.getParameterValues("nextbranchid1");
			String[] kufangid = request.getParameterValues("kufangid1") == null ? new String[] {} : request.getParameterValues("kufangid1");
			String begindate = request.getParameter("begindate1") == null ? "" : request.getParameter("begindate1").toString();
			String enddate = request.getParameter("enddate1") == null ? "" : request.getParameter("enddate1").toString();
			long datetype = request.getParameter("datetype1") == null ? -1 : Long.parseLong(request.getParameter("datetype1").toString());

			// 验证导出条件是否存在
			String datajson = this.setKuFangZaiTuJson(datetype, begindate, enddate, this.getStrings(nextbranchid), this.getStrings(kufangid), this.getStrings(cwbordertypeids), page, mouldfieldids,
					userid);

			DownloadManager down = this.downloadManagerDAO.getDownloadManagerByJson(datajson);

			if (down != null) {
				json.put("errorCode", 2);
				json.put("remark", "该导出操作已经在下载列表中！");
				return json.toString();
			} else {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
				String fileName = "Order_" + df.format(new Date()) + "_"; // 文件名
				String otherName = "";
				String lastStr = ".xlsx";// 文件名后缀
				long count = Long.parseLong(request.getParameter("count") == null ? "0" : request.getParameter("count").toString());
				if (count > 0) {
					if (((count / Page.EXCEL_PAGE_NUMBER) + ((count % Page.EXCEL_PAGE_NUMBER) > 0 ? 1 : 0)) == 1) {
						otherName = "1-" + count;
					} else {
						otherName = ((((page * Page.EXCEL_PAGE_NUMBER) + 1) > count) ? count : ((page * Page.EXCEL_PAGE_NUMBER) + 1)) + "_"
								+ (((page + 1) * Page.EXCEL_PAGE_NUMBER) > count ? count : (page + 1) * Page.EXCEL_PAGE_NUMBER);
					}
				}
				fileName = fileName + otherName + lastStr;
				String cnfilename = "库房在途统计_" + df.format(new Date()) + "_" + otherName + lastStr;
				String fileUrl = this.getDmpDAO.getFileUrl() + "download" + System.getProperty("file.separator") + new SimpleDateFormat("yyyy-MM-dd").format(new Date())
						+ System.getProperty("file.separator");
				down = this.setDownloadManager(datajson, fileName, cnfilename, fileUrl, ModelEnum.KuFangZaiTuTongJi.getValue(), userid);
				this.downloadManagerDAO.creDownloadManager(down);
				json.put("errorCode", 0);
				json.put("remark", "已进入离线导出");
				return json.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("errorCode", 2);
			json.put("remark", "系统导出异常！");
			return json.toString();
		}
	}

	private String setKuFangZaiTuJson(long datetype, String begindate, String enddate, String nextbranchid, String kufangid, String cwbordertypeids, long page, String mouldfieldids, long userid) {
		JSONObject json = new JSONObject();
		json.put("datetype", datetype);
		json.put("begindate", begindate);
		json.put("enddate", enddate);
		json.put("nextbranchid", nextbranchid);
		json.put("kufangid", kufangid);
		json.put("cwbordertypeids", cwbordertypeids);
		json.put("page", page);
		json.put("mouldfieldids", mouldfieldids);
		json.put("userid", userid);
		return json.toString();
	}

	/**
	 * 退货站入库统计
	 *
	 * @param response
	 * @param request
	 * @param page
	 * @param userid
	 * @return
	 */
	private String checkTuiHuoZhanRuKuTongJi(HttpServletResponse response, HttpServletRequest request, long page, long userid) {
		JSONObject json = new JSONObject();
		String mouldfieldids = request.getParameter("exportmould2"); // 导出模板

		try {
			// 查询出数据
			String begindate = request.getParameter("begindate1") == null ? "" : request.getParameter("begindate1").toString();
			String enddate = request.getParameter("enddate1") == null ? "" : request.getParameter("enddate1").toString();
			String[] customerids = request.getParameterValues("customerid1") == null ? new String[] {} : request.getParameterValues("customerid1");
			String[] branchids = request.getParameterValues("branchid1") == null ? new String[] {} : request.getParameterValues("branchid1");
			String[] cwbordertypeids = request.getParameterValues("cwbordertypeid1") == null ? new String[] {} : request.getParameterValues("cwbordertypeid1");

			// 验证导出条件是否存在
			String datajson = this.setTuiHuoZhanRuKuJson(begindate, enddate, this.getStrings(customerids), this.getStrings(branchids), this.getStrings(cwbordertypeids), page, mouldfieldids, userid);

			DownloadManager down = this.downloadManagerDAO.getDownloadManagerByJson(datajson);

			if (down != null) {
				json.put("errorCode", 2);
				json.put("remark", "该导出操作已经在下载列表中！");
				return json.toString();
			} else {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
				String fileName = "Order_" + df.format(new Date()) + "_"; // 文件名
				String otherName = "";
				String lastStr = ".xlsx";// 文件名后缀
				long count = Long.parseLong(request.getParameter("count") == null ? "0" : request.getParameter("count").toString());
				if (count > 0) {
					if (((count / Page.EXCEL_PAGE_NUMBER) + ((count % Page.EXCEL_PAGE_NUMBER) > 0 ? 1 : 0)) == 1) {
						otherName = "1-" + count;
					} else {
						otherName = ((((page * Page.EXCEL_PAGE_NUMBER) + 1) > count) ? count : ((page * Page.EXCEL_PAGE_NUMBER) + 1)) + "_"
								+ (((page + 1) * Page.EXCEL_PAGE_NUMBER) > count ? count : (page + 1) * Page.EXCEL_PAGE_NUMBER);
					}
				}
				fileName = fileName + otherName + lastStr;
				String cnfilename = "退货站入库统计_" + df.format(new Date()) + "_" + otherName + lastStr;
				String fileUrl = this.getDmpDAO.getFileUrl() + "download" + System.getProperty("file.separator") + new SimpleDateFormat("yyyy-MM-dd").format(new Date())
						+ System.getProperty("file.separator");
				down = this.setDownloadManager(datajson, fileName, cnfilename, fileUrl, ModelEnum.TuiHuoZhanRuKuTongJi.getValue(), userid);
				this.downloadManagerDAO.creDownloadManager(down);
				json.put("errorCode", 0);
				json.put("remark", "已进入离线导出");
				return json.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("errorCode", 2);
			json.put("remark", "系统导出异常！");
			return json.toString();
		}
	}

	private String setTuiHuoZhanRuKuJson(String begindate, String enddate, String customerids, String branchids, String cwbordertypeids, long page, String mouldfieldids, long userid) {
		JSONObject json = new JSONObject();
		json.put("begindate", begindate);
		json.put("enddate", enddate);
		json.put("customerids", customerids);
		json.put("branchids", branchids);
		json.put("cwbordertypeids", cwbordertypeids);
		json.put("page", page);
		json.put("mouldfieldids", mouldfieldids);
		json.put("userid", userid);
		return json.toString();
	}

	private String checkTuiHuoChuZhanTongJi(HttpServletResponse response, HttpServletRequest request, long page, long userid) {
		JSONObject json = new JSONObject();
		String mouldfieldids = request.getParameter("exportmould2"); // 导出模板
		try {
			// 查询出数据
			String begindate = request.getParameter("begindate1") == null ? "" : request.getParameter("begindate1").toString();
			String enddate = request.getParameter("enddate1") == null ? "" : request.getParameter("enddate1").toString();
			String[] customerids = request.getParameterValues("customerid1") == null ? new String[] {} : request.getParameterValues("customerid1");
			String[] branchids = request.getParameterValues("branchid1") == null ? new String[] {} : request.getParameterValues("branchid1");
			long istuihuozhanruku = request.getParameter("istuihuozhanruku1") == null ? -1 : Long.parseLong(request.getParameter("istuihuozhanruku1").toString());
			// 验证导出条件是否存在
			String datajson = this.setTuiHuoChuZhanJson(begindate, enddate, this.getStrings(customerids), this.getStrings(branchids), page, istuihuozhanruku, mouldfieldids, userid);

			DownloadManager down = this.downloadManagerDAO.getDownloadManagerByJson(datajson);

			if (down != null) {
				json.put("errorCode", 2);
				json.put("remark", "该导出操作已经在下载列表中！");
				return json.toString();
			} else {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
				String fileName = "Order_" + df.format(new Date()) + "_"; // 文件名
				String otherName = "";
				String lastStr = ".xlsx";// 文件名后缀
				long count = Long.parseLong(request.getParameter("count") == null ? "0" : request.getParameter("count").toString());
				if (count > 0) {
					if (((count / Page.EXCEL_PAGE_NUMBER) + ((count % Page.EXCEL_PAGE_NUMBER) > 0 ? 1 : 0)) == 1) {
						otherName = "1-" + count;
					} else {
						otherName = ((((page * Page.EXCEL_PAGE_NUMBER) + 1) > count) ? count : ((page * Page.EXCEL_PAGE_NUMBER) + 1)) + "_"
								+ (((page + 1) * Page.EXCEL_PAGE_NUMBER) > count ? count : (page + 1) * Page.EXCEL_PAGE_NUMBER);
					}
				}
				fileName = fileName + otherName + lastStr;
				String cnfilename = "退货出站统计_" + df.format(new Date()) + "_" + otherName + lastStr;
				String fileUrl = this.getDmpDAO.getFileUrl() + "download" + System.getProperty("file.separator") + new SimpleDateFormat("yyyy-MM-dd").format(new Date())
						+ System.getProperty("file.separator");
				down = this.setDownloadManager(datajson, fileName, cnfilename, fileUrl, ModelEnum.TuiHuoChuZhanTongJi.getValue(), userid);
				this.downloadManagerDAO.creDownloadManager(down);
				json.put("errorCode", 0);
				json.put("remark", "已进入离线导出");
				return json.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("errorCode", 2);
			json.put("remark", "系统导出异常！");
			return json.toString();
		}
	}

	private String checkKuFangChuKuHuiZong(HttpServletResponse response, HttpServletRequest request, long userid) {
		JSONObject json = new JSONObject();
		try {
			// 查询出数据
			String begindate = request.getParameter("begindate1") == null ? "" : request.getParameter("begindate1").toString();
			String enddate = request.getParameter("enddate1") == null ? "" : request.getParameter("enddate1").toString();
			String[] kufangid = request.getParameterValues("kufangid1") == null ? new String[] {} : request.getParameterValues("kufangid1");
			String[] customerid = request.getParameterValues("customerid1") == null ? new String[] {} : request.getParameterValues("customerid1");
			String[] nextbranchid = request.getParameterValues("nextbranchid1") == null ? new String[] {} : request.getParameterValues("nextbranchid1");

			// 验证导出条件是否存在
			String datajson = this.setKuFangChuKuHuiZongJson(begindate, enddate, this.getStrings(customerid), this.getStrings(kufangid), this.getStrings(nextbranchid), userid);

			DownloadManager down = this.downloadManagerDAO.getDownloadManagerByJson(datajson);

			if (down != null) {
				json.put("errorCode", 2);
				json.put("remark", "该导出操作已经在下载列表中！");
				return json.toString();
			} else {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
				String fileName = "Order_" + df.format(new Date()); // 文件名
				String lastStr = ".xlsx";// 文件名后缀
				fileName = fileName + lastStr;
				String cnfilename = "库房出库汇总_" + df.format(new Date()) + lastStr;
				String fileUrl = this.getDmpDAO.getFileUrl() + "download" + System.getProperty("file.separator") + new SimpleDateFormat("yyyy-MM-dd").format(new Date())
						+ System.getProperty("file.separator");
				down = this.setDownloadManager(datajson, fileName, cnfilename, fileUrl, ModelEnum.KuFangChuKuHuiZong.getValue(), userid);
				this.downloadManagerDAO.creDownloadManager(down);
				json.put("errorCode", 0);
				json.put("remark", "已进入离线导出");
				return json.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("errorCode", 2);
			json.put("remark", "系统导出异常！");
			return json.toString();
		}
	}

	private String checkJuShouHuiZong(HttpServletResponse response, HttpServletRequest request, long page, long userid) {
		JSONObject json = new JSONObject();
		String mouldfieldids = request.getParameter("exportmould2"); // 导出模板
		try {
			// 查询出数据
			String[] cwbordertypeids = request.getParameterValues("cwbordertypeid1") == null ? new String[] {} : request.getParameterValues("cwbordertypeid1");
			String begindate = request.getParameter("begindate1") == null ? "" : request.getParameter("begindate1").toString();
			String enddate = request.getParameter("enddate1") == null ? "" : request.getParameter("enddate1").toString();
			String[] customerids = request.getParameterValues("customerid1") == null ? new String[] {} : request.getParameterValues("customerid1");
			String[] dispatchbranchid = request.getParameterValues("dispatchbranchid1") == null ? new String[] {} : request.getParameterValues("dispatchbranchid1");
			long isaudit = request.getParameter("isaudit1") == null ? -1 : Long.parseLong(request.getParameter("isaudit1").toString());
			long isauditTime = request.getParameter("isauditTime1") == null ? -1 : Long.parseLong(request.getParameter("isauditTime1").toString());
			long deliverid = request.getParameter("deliverid1") == null ? -1 : Long.parseLong(request.getParameter("deliverid1").toString());
			String[] operationOrderResultTypes = request.getParameterValues("operationOrderResultTypes1") == null ? new String[] {} : request.getParameterValues("operationOrderResultTypes1");

			// 验证导出条件是否存在
			String datajson = this.setJuShouJson(begindate, enddate, isaudit, isauditTime, this.getStrings(customerids), this.getStrings(cwbordertypeids), this.getStrings(dispatchbranchid),
					deliverid, this.getStrings(operationOrderResultTypes), page, mouldfieldids, userid);

			DownloadManager down = this.downloadManagerDAO.getDownloadManagerByJson(datajson);

			if (down != null) {
				json.put("errorCode", 2);
				json.put("remark", "该导出操作已经在下载列表中！");
				return json.toString();
			} else {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
				String fileName = "Order_" + df.format(new Date()) + "_"; // 文件名
				String otherName = "";
				String lastStr = ".xlsx";// 文件名后缀
				long count = Long.parseLong(request.getParameter("count") == null ? "0" : request.getParameter("count").toString());
				if (count > 0) {
					if (((count / Page.EXCEL_PAGE_NUMBER) + ((count % Page.EXCEL_PAGE_NUMBER) > 0 ? 1 : 0)) == 1) {
						otherName = "1-" + count;
					} else {
						otherName = ((((page * Page.EXCEL_PAGE_NUMBER) + 1) > count) ? count : ((page * Page.EXCEL_PAGE_NUMBER) + 1)) + "_"
								+ (((page + 1) * Page.EXCEL_PAGE_NUMBER) > count ? count : (page + 1) * Page.EXCEL_PAGE_NUMBER);
					}
				}
				fileName = fileName + otherName + lastStr;
				String cnfilename = "拒收订单汇总_" + df.format(new Date()) + "_" + otherName + lastStr;
				String fileUrl = this.getDmpDAO.getFileUrl() + "download" + System.getProperty("file.separator") + new SimpleDateFormat("yyyy-MM-dd").format(new Date())
						+ System.getProperty("file.separator");
				down = this.setDownloadManager(datajson, fileName, cnfilename, fileUrl, ModelEnum.JuShouDingdanhuizong.getValue(), userid);
				this.downloadManagerDAO.creDownloadManager(down);
				json.put("errorCode", 0);
				json.put("remark", "已进入离线导出");
				return json.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("errorCode", 2);
			json.put("remark", "系统导出异常！");
			return json.toString();
		}
	}

	private String checkTiHuoDingDanTongJi(HttpServletResponse response, HttpServletRequest request, long page, long userid) {
		JSONObject json = new JSONObject();
		try {
			// 查询出数据
			String begindate = request.getParameter("begindate1") == null ? "" : request.getParameter("begindate1").toString();
			String enddate = request.getParameter("enddate1") == null ? "" : request.getParameter("enddate1").toString();
			String[] customerids = request.getParameterValues("customerid1") == null ? new String[] {} : request.getParameterValues("customerid1");

			// 验证导出条件是否存在
			String datajson = this.setTiHuoJson(begindate, enddate, this.getStrings(customerids), page, userid);

			DownloadManager down = this.downloadManagerDAO.getDownloadManagerByJson(datajson);

			if (down != null) {
				json.put("errorCode", 2);
				json.put("remark", "该导出操作已经在下载列表中！");
				return json.toString();
			} else {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
				String fileName = "Order_" + df.format(new Date()) + "_"; // 文件名
				String otherName = "";
				String lastStr = ".xlsx";// 文件名后缀
				long count = Long.parseLong(request.getParameter("count") == null ? "0" : request.getParameter("count").toString());
				if (count > 0) {
					if (((count / Page.EXCEL_PAGE_NUMBER) + ((count % Page.EXCEL_PAGE_NUMBER) > 0 ? 1 : 0)) == 1) {
						otherName = "1-" + count;
					} else {
						otherName = ((((page * Page.EXCEL_PAGE_NUMBER) + 1) > count) ? count : ((page * Page.EXCEL_PAGE_NUMBER) + 1)) + "_"
								+ (((page + 1) * Page.EXCEL_PAGE_NUMBER) > count ? count : (page + 1) * Page.EXCEL_PAGE_NUMBER);
					}
				}
				fileName = fileName + otherName + lastStr;
				String cnfilename = "提货订单统计_" + df.format(new Date()) + "_" + otherName + lastStr;
				String fileUrl = this.getDmpDAO.getFileUrl() + "download" + System.getProperty("file.separator") + new SimpleDateFormat("yyyy-MM-dd").format(new Date())
						+ System.getProperty("file.separator");
				down = this.setDownloadManager(datajson, fileName, cnfilename, fileUrl, ModelEnum.TiHuoDingDanTongJi.getValue(), userid);
				this.downloadManagerDAO.creDownloadManager(down);
				json.put("errorCode", 0);
				json.put("remark", "已进入离线导出");
				return json.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("errorCode", 2);
			json.put("remark", "系统导出异常！");
			return json.toString();
		}
	}

	private String checkDanliangchaxun(HttpServletResponse response, HttpServletRequest request, long page, long userid) {
		JSONObject json = new JSONObject();
		try {
			// 查询出数据
			String kufangid = request.getParameter("kufangid") == null ? "0" : request.getParameter("kufangid").toString();
			String enddate = request.getParameter("timer") == null ? "" : request.getParameter("timer").toString();
			String customeri = request.getParameter("customerid");
			long kufangids = Integer.parseInt(kufangid);
			String mouldfieldids = request.getParameter("mouldfieldids");
			String select = request.getParameter("select");
			// 验证导出条件是否存在
			String datajson = this.setDanliang(kufangids, enddate, Integer.valueOf(customeri), page, mouldfieldids, userid, select);

			DownloadManager down = this.downloadManagerDAO.getDownloadManagerByJson(datajson);

			if (down != null) {
				json.put("errorCode", 2);
				json.put("remark", "该导出操作已经在下载列表中！");
				return json.toString();
			} else {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
				String fileName = "单量查询_" + df.format(new Date()) + "_"; // 文件名
				String otherName = "";
				String lastStr = ".xlsx";// 文件名后缀
				long count = Long.parseLong(request.getParameter("count") == null ? "0" : request.getParameter("count").toString());
				if (count > 0) {
					if (((count / Page.EXCEL_PAGE_NUMBER) + ((count % Page.EXCEL_PAGE_NUMBER) > 0 ? 1 : 0)) == 1) {
						otherName = "1-" + count;
					} else {
						otherName = ((((page * Page.EXCEL_PAGE_NUMBER) + 1) > count) ? count : ((page * Page.EXCEL_PAGE_NUMBER) + 1)) + "_"
								+ (((page + 1) * Page.EXCEL_PAGE_NUMBER) > count ? count : (page + 1) * Page.EXCEL_PAGE_NUMBER);
					}
				}
				fileName = fileName + otherName + lastStr;
				String cnfilename = "单量查询_" + df.format(new Date()) + "_" + otherName + lastStr;
				String fileUrl = this.getDmpDAO.getFileUrl() + "download" + System.getProperty("file.separator") + new SimpleDateFormat("yyyy-MM-dd").format(new Date())
						+ System.getProperty("file.separator");
				down = this.setDownloadManager(datajson, fileName, cnfilename, fileUrl, ModelEnum.DanliangChaxun.getValue(), userid);
				this.downloadManagerDAO.creDownloadManager(down);
				json.put("errorCode", 0);
				json.put("remark", "已进入离线导出");
				return json.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("errorCode", 2);
			json.put("remark", "系统导出异常！");
			return json.toString();
		}
	}

	private String checkZhiLiuHuiZong(HttpServletResponse response, HttpServletRequest request, long page, long userid) {
		JSONObject json = new JSONObject();
		String mouldfieldids = request.getParameter("exportmould2"); // 导出模板
		try {
			// 查询出数据

			String[] cwbordertypeids = request.getParameterValues("cwbordertypeid1") == null ? new String[] {} : request.getParameterValues("cwbordertypeid1");
			String begindate = request.getParameter("begindate1") == null ? "" : request.getParameter("begindate1").toString();
			String enddate = request.getParameter("enddate1") == null ? "" : request.getParameter("enddate1").toString();
			String[] customerids = request.getParameterValues("customerid1") == null ? new String[] {} : request.getParameterValues("customerid1");
			String[] dispatchbranchid = request.getParameterValues("dispatchbranchid1") == null ? new String[] {} : request.getParameterValues("dispatchbranchid1");
			long isaudit = request.getParameter("isaudit1") == null ? -1 : Long.parseLong(request.getParameter("isaudit1").toString());
			long isauditTime = request.getParameter("isauditTime1") == null ? -1 : Long.parseLong(request.getParameter("isauditTime1").toString());
			long deliverid = request.getParameter("deliverid1") == null ? -1 : Long.parseLong(request.getParameter("deliverid1").toString());
			// String[] operationOrderResultTypes =
			// request.getParameterValues("operationOrderResultTypes1")==null?new
			// String[]{}:request.getParameterValues("operationOrderResultTypes1");
			// 验证导出条件是否存在
			String datajson = this.setZhiLiuJson(begindate, enddate, isaudit, isauditTime, this.getStrings(customerids), this.getStrings(cwbordertypeids), this.getStrings(dispatchbranchid),
					deliverid, page, mouldfieldids, userid);

			DownloadManager down = this.downloadManagerDAO.getDownloadManagerByJson(datajson);

			if (down != null) {
				json.put("errorCode", 2);
				json.put("remark", "该导出操作已经在下载列表中！");
				return json.toString();
			} else {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
				String fileName = "Order_" + df.format(new Date()) + "_"; // 文件名
				String otherName = "";
				String lastStr = ".xlsx";// 文件名后缀
				long count = Long.parseLong(request.getParameter("count") == null ? "0" : request.getParameter("count").toString());
				if (count > 0) {
					if (((count / Page.EXCEL_PAGE_NUMBER) + ((count % Page.EXCEL_PAGE_NUMBER) > 0 ? 1 : 0)) == 1) {
						otherName = "1-" + count;
					} else {
						otherName = ((((page * Page.EXCEL_PAGE_NUMBER) + 1) > count) ? count : ((page * Page.EXCEL_PAGE_NUMBER) + 1)) + "_"
								+ (((page + 1) * Page.EXCEL_PAGE_NUMBER) > count ? count : (page + 1) * Page.EXCEL_PAGE_NUMBER);
					}
				}
				fileName = fileName + otherName + lastStr;
				String cnfilename = "滞留订单汇总_" + df.format(new Date()) + "_" + otherName + lastStr;
				String fileUrl = this.getDmpDAO.getFileUrl() + "download" + System.getProperty("file.separator") + new SimpleDateFormat("yyyy-MM-dd").format(new Date())
						+ System.getProperty("file.separator");
				down = this.setDownloadManager(datajson, fileName, cnfilename, fileUrl, ModelEnum.ZhiLiuDingdanhuizong.getValue(), userid);
				this.downloadManagerDAO.creDownloadManager(down);
				json.put("errorCode", 0);
				json.put("remark", "已进入离线导出");
				return json.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("errorCode", 2);
			json.put("remark", "系统导出异常！");
			return json.toString();
		}
	}

	private String checkTuotou(HttpServletResponse response, HttpServletRequest request, long page, long userid) {
		JSONObject json = new JSONObject();
		String mouldfieldids = request.getParameter("exportmould2"); // 导出模板
		try {
			// 查询出数据
			String[] cwbordertypeids = request.getParameterValues("cwbordertypeid1") == null ? new String[] {} : request.getParameterValues("cwbordertypeid1");
			String begindate = request.getParameter("begindate1") == null ? "" : request.getParameter("begindate1").toString();
			String enddate = request.getParameter("enddate1") == null ? "" : request.getParameter("enddate1").toString();
			String[] customerids = request.getParameterValues("customerid1") == null ? new String[] {} : request.getParameterValues("customerid1");
			String[] dispatchbranchid = request.getParameterValues("dispatchbranchid1") == null ? new String[] {} : request.getParameterValues("dispatchbranchid1");
			long paywayid = request.getParameter("paytype1") == null ? -1 : Long.parseLong(request.getParameter("paytype1").toString());
			long isaudit = request.getParameter("isaudit1") == null ? -1 : Long.parseLong(request.getParameter("isaudit1").toString());
			long isauditTime = request.getParameter("isauditTime1") == null ? -1 : Long.parseLong(request.getParameter("isauditTime1").toString());
			long deliverid = request.getParameter("deliverid1") == null ? -1 : Long.parseLong(request.getParameter("deliverid1").toString());
			String[] operationOrderResultTypes = request.getParameterValues("operationOrderResultTypes1") == null ? new String[] {} : request.getParameterValues("operationOrderResultTypes1");
			Integer paybackfeeIsZero = request.getParameter("paybackfeeIsZero1") == null ? -1 : Integer.parseInt(request.getParameter("paybackfeeIsZero1").toString());
			// 验证导出条件是否存在
			String datajson = this.setJson(begindate, enddate, isaudit, isauditTime, this.getStrings(customerids), this.getStrings(cwbordertypeids), paywayid, this.getStrings(dispatchbranchid),
					deliverid, this.getStrings(operationOrderResultTypes), page, mouldfieldids, userid, paybackfeeIsZero);

			DownloadManager down = this.downloadManagerDAO.getDownloadManagerByJson(datajson);

			if (down != null) {
				json.put("errorCode", 2);
				json.put("remark", "该导出操作已经在下载列表中！");
				return json.toString();
			} else {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
				String fileName = "Order_" + df.format(new Date()) + "_"; // 文件名
				String otherName = "";
				String lastStr = ".xlsx";// 文件名后缀
				long count = Long.parseLong(request.getParameter("count") == null ? "0" : request.getParameter("count").toString());
				if (count > 0) {
					if (((count / Page.EXCEL_PAGE_NUMBER) + ((count % Page.EXCEL_PAGE_NUMBER) > 0 ? 1 : 0)) == 1) {
						otherName = "1-" + count;
					} else {
						otherName = ((((page * Page.EXCEL_PAGE_NUMBER) + 1) > count) ? count : ((page * Page.EXCEL_PAGE_NUMBER) + 1)) + "_"
								+ (((page + 1) * Page.EXCEL_PAGE_NUMBER) > count ? count : (page + 1) * Page.EXCEL_PAGE_NUMBER);
					}
				}
				fileName = fileName + otherName + lastStr;
				String cnfilename = "妥投订单汇总_" + df.format(new Date()) + "_" + otherName + lastStr;
				String fileUrl = this.getDmpDAO.getFileUrl() + "download" + System.getProperty("file.separator") + new SimpleDateFormat("yyyy-MM-dd").format(new Date())
						+ System.getProperty("file.separator");
				down = this.setDownloadManager(datajson, fileName, cnfilename, fileUrl, ModelEnum.TuoTouDingdanhuizong.getValue(), userid);
				this.downloadManagerDAO.creDownloadManager(down);
				json.put("errorCode", 0);
				json.put("remark", "已进入离线导出");
				return json.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("errorCode", 2);
			json.put("remark", "系统导出异常！");
			return json.toString();
		}
	}

	public String checkOutWarehouseData(HttpServletResponse response, HttpServletRequest request, long page, long userid) {
		JSONObject json = new JSONObject();
		String mouldfieldids = request.getParameter("exportmould2"); // 导出模板
		try {
			// 查询出数据
			String begindate = request.getParameter("begindate1") == null ? "" : request.getParameter("begindate1").toString();
			String enddate = request.getParameter("enddate1") == null ? "" : request.getParameter("enddate1").toString();
			String[] customerid = request.getParameterValues("customerid1") == null ? new String[] {} : request.getParameterValues("customerid1");
			String[] kufangid = request.getParameterValues("kufangid1") == null ? new String[] {} : request.getParameterValues("kufangid1");
			String[] nextbranchid = request.getParameterValues("nextbranchid1") == null ? new String[] {} : request.getParameterValues("nextbranchid1");
			String[] cwbordertypeid = request.getParameterValues("cwbordertypeid1") == null ? new String[] {} : request.getParameterValues("cwbordertypeid1");

			// 验证导出条件是否存在
			String datajson = this.setOutWarehouseDataJson(begindate, enddate, this.getStrings(kufangid), this.getStrings(customerid), this.getStrings(nextbranchid), this.getStrings(cwbordertypeid),
					page, mouldfieldids, userid);

			DownloadManager down = this.downloadManagerDAO.getDownloadManagerByJson(datajson);

			if (down != null) {
				json.put("errorCode", 2);
				json.put("remark", "该导出操作已经在下载列表中！");
				return json.toString();
			} else {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
				String fileName = "Order_" + df.format(new Date()) + "_"; // 文件名
				String otherName = "";
				String lastStr = ".xlsx";// 文件名后缀
				long count = Long.parseLong(request.getParameter("count") == null ? "0" : request.getParameter("count").toString());
				if (count > 0) {
					if (((count / Page.EXCEL_PAGE_NUMBER) + ((count % Page.EXCEL_PAGE_NUMBER) > 0 ? 1 : 0)) == 1) {
						otherName = "1-" + count;
					} else {
						otherName = ((((page * Page.EXCEL_PAGE_NUMBER) + 1) > count) ? count : ((page * Page.EXCEL_PAGE_NUMBER) + 1)) + "_"
								+ (((page + 1) * Page.EXCEL_PAGE_NUMBER) > count ? count : (page + 1) * Page.EXCEL_PAGE_NUMBER);
					}
				}
				fileName = fileName + otherName + lastStr;
				String cnfilename = "库房出库订单统计_" + df.format(new Date()) + "_" + otherName + lastStr;
				String fileUrl = this.getDmpDAO.getFileUrl() + "download" + System.getProperty("file.separator") + new SimpleDateFormat("yyyy-MM-dd").format(new Date())
						+ System.getProperty("file.separator");
				down = this.setDownloadManager(datajson, fileName, cnfilename, fileUrl, ModelEnum.KuFangChuKuDingdantongji.getValue(), userid);
				this.downloadManagerDAO.creDownloadManager(down);
				json.put("errorCode", 0);
				json.put("remark", "已进入离线导出");
				return json.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("errorCode", 2);
			json.put("remark", "系统导出异常！");
			return json.toString();
		}
	}

	// 库对库出库统计导出
	public String checkKDKOutWarehouseData(HttpServletResponse response, HttpServletRequest request, long page, long userid) {
		JSONObject json = new JSONObject();
		String mouldfieldids = request.getParameter("exportmould2"); // 导出模板
		try {
			// 查询出数据
			String begindate = request.getParameter("begindate1") == null ? "" : request.getParameter("begindate1").toString();
			String enddate = request.getParameter("enddate1") == null ? "" : request.getParameter("enddate1").toString();
			String[] kufangid = request.getParameterValues("kufangid1") == null ? new String[] {} : request.getParameterValues("kufangid1");
			String[] customerid = request.getParameterValues("customerid1") == null ? new String[] {} : request.getParameterValues("customerid1");
			String[] nextbranchid = request.getParameterValues("nextbranchid1") == null ? new String[] {} : request.getParameterValues("nextbranchid1");
			String[] cwbordertypeid = request.getParameterValues("cwbordertypeid1") == null ? new String[] {} : request.getParameterValues("cwbordertypeid1");

			// 验证导出条件是否存在
			String datajson = this.setKDKOutWarehouseDataJson(begindate, enddate, this.getStrings(kufangid), this.getStrings(customerid), this.getStrings(nextbranchid),
					this.getStrings(cwbordertypeid), page, mouldfieldids, userid);

			DownloadManager down = this.downloadManagerDAO.getDownloadManagerByJson(datajson);

			if (down != null) {
				json.put("errorCode", 2);
				json.put("remark", "该导出操作已经在下载列表中！");
				return json.toString();
			} else {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
				String fileName = "Order_" + df.format(new Date()) + "_"; // 文件名
				String otherName = "";
				String lastStr = ".xlsx";// 文件名后缀
				long count = Long.parseLong(request.getParameter("count") == null ? "0" : request.getParameter("count").toString());
				if (count > 0) {
					if (((count / Page.EXCEL_PAGE_NUMBER) + ((count % Page.EXCEL_PAGE_NUMBER) > 0 ? 1 : 0)) == 1) {
						otherName = "1-" + count;
					} else {
						otherName = ((((page * Page.EXCEL_PAGE_NUMBER) + 1) > count) ? count : ((page * Page.EXCEL_PAGE_NUMBER) + 1)) + "_"
								+ (((page + 1) * Page.EXCEL_PAGE_NUMBER) > count ? count : (page + 1) * Page.EXCEL_PAGE_NUMBER);
					}
				}
				fileName = fileName + otherName + lastStr;
				String cnfilename = "库对库出库订单统计_" + df.format(new Date()) + "_" + otherName + lastStr;
				String fileUrl = this.getDmpDAO.getFileUrl() + "download" + System.getProperty("file.separator") + new SimpleDateFormat("yyyy-MM-dd").format(new Date())
						+ System.getProperty("file.separator");
				down = this.setDownloadManager(datajson, fileName, cnfilename, fileUrl, ModelEnum.KDKChuKuDingdantongji.getValue(), userid);
				this.downloadManagerDAO.creDownloadManager(down);
				json.put("errorCode", 0);
				json.put("remark", "已进入离线导出");
				return json.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("errorCode", 2);
			json.put("remark", "系统导出异常！");
			return json.toString();
		}
	}

	public String checkDaoHuoData(HttpServletResponse response, HttpServletRequest request, long page, long userid) {
		JSONObject json = new JSONObject();
		String mouldfieldids = request.getParameter("exportmould2"); // 导出模板
		try {
			// 查询出数据
			String begindate = request.getParameter("begindate1") == null ? "" : request.getParameter("begindate1").toString();
			String enddate = request.getParameter("enddate1") == null ? "" : request.getParameter("enddate1").toString();
			long kufangid = request.getParameter("kufangid1") == null ? -1 : Long.parseLong(request.getParameter("kufangid1"));
			long customerid = request.getParameter("customerid1") == null ? 0 : Long.parseLong(request.getParameter("customerid1"));
			long isnowdata = request.getParameter("isnowdata") == null ? 0 : Long.parseLong(request.getParameter("isnowdata"));

			String[] currentBranchid = request.getParameterValues("currentBranchid1") == null ? new String[] {} : request.getParameterValues("currentBranchid1");
			String[] cwbordertypeid = request.getParameterValues("cwbordertypeid1") == null ? new String[] {} : request.getParameterValues("cwbordertypeid1");

			// 验证导出条件是否存在
			String datajson = this.setDaoHuoDataJson(begindate, enddate, kufangid, customerid, this.getStrings(currentBranchid), this.getStrings(cwbordertypeid), isnowdata, page, mouldfieldids,
					userid);

			DownloadManager down = this.downloadManagerDAO.getDownloadManagerByJson(datajson);

			if (down != null) {
				json.put("errorCode", 2);
				json.put("remark", "该导出操作已经在下载列表中！");
				return json.toString();
			} else {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
				String fileName = "Order_" + df.format(new Date()) + "_"; // 文件名
				String otherName = "";
				String lastStr = ".xlsx";// 文件名后缀
				long count = Long.parseLong(request.getParameter("count") == null ? "0" : request.getParameter("count").toString());
				if (count > 0) {
					if (((count / Page.EXCEL_PAGE_NUMBER) + ((count % Page.EXCEL_PAGE_NUMBER) > 0 ? 1 : 0)) == 1) {
						otherName = "1-" + count;
					} else {
						otherName = ((((page * Page.EXCEL_PAGE_NUMBER) + 1) > count) ? count : ((page * Page.EXCEL_PAGE_NUMBER) + 1)) + "_"
								+ (((page + 1) * Page.EXCEL_PAGE_NUMBER) > count ? count : (page + 1) * Page.EXCEL_PAGE_NUMBER);
					}
				}
				fileName = fileName + otherName + lastStr;
				String cnfilename = "分站到货统计_" + df.format(new Date()) + "_" + otherName + lastStr;
				String fileUrl = this.getDmpDAO.getFileUrl() + "download" + System.getProperty("file.separator") + new SimpleDateFormat("yyyy-MM-dd").format(new Date())
						+ System.getProperty("file.separator");
				down = this.setDownloadManager(datajson, fileName, cnfilename, fileUrl, ModelEnum.FenZhanDaoHuotongji.getValue(), userid);
				this.downloadManagerDAO.creDownloadManager(down);
				json.put("errorCode", 0);
				json.put("remark", "已进入离线导出");
				return json.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("errorCode", 2);
			json.put("remark", "系统导出异常！");
			return json.toString();
		}
	}

	public String checkDaoHuoHuiZong(HttpServletResponse response, HttpServletRequest request, long userid) {
		JSONObject json = new JSONObject();
		String mouldfieldids = request.getParameter("exportmould"); // 导出模板
		try {
			// 查询出数据
			String begindate = request.getParameter("begindate") == null ? "" : request.getParameter("begindate").toString();
			String enddate = request.getParameter("enddate") == null ? "" : request.getParameter("enddate").toString();
			long branchid = request.getParameter("branchid") == null ? -1 : Long.parseLong(request.getParameter("branchid"));

			// 验证导出条件是否存在
			String datajson = this.setDaoHuoHuiZongJson(begindate, enddate, branchid, mouldfieldids, userid);

			DownloadManager down = this.downloadManagerDAO.getDownloadManagerByJson(datajson);

			if (down != null) {
				json.put("errorCode", 2);
				json.put("remark", "该导出操作已经在下载列表中！");
				return json.toString();
			} else {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
				String fileName = "Order_" + df.format(new Date()) + "_"; // 文件名
				String otherName = "";
				String lastStr = ".xlsx";// 文件名后缀
				/*
				 * long count =
				 * Long.parseLong(request.getParameter("count")==null
				 * ?"0":request.getParameter("count").toString()); if(count>0){
				 * if
				 * (count/Page.EXCEL_PAGE_NUMBER+(count%Page.EXCEL_PAGE_NUMBER>
				 * 0?1:0)==1){ otherName = "1-"+count; }else{ otherName =
				 * ((page*Page.EXCEL_PAGE_NUMBER+1>count)?count:(page*Page.
				 * EXCEL_PAGE_NUMBER+1)) +"_"
				 * +((page+1)*Page.EXCEL_PAGE_NUMBER>count
				 * ?count:(page+1)*Page.EXCEL_PAGE_NUMBER); } }
				 */
				fileName = fileName + otherName + lastStr;
				String cnfilename = "站点到货汇总_" + df.format(new Date()) + "_" + otherName + lastStr;
				String fileUrl = this.getDmpDAO.getFileUrl() + "download" + System.getProperty("file.separator") + new SimpleDateFormat("yyyy-MM-dd").format(new Date())
						+ System.getProperty("file.separator");
				down = this.setDownloadManager(datajson, fileName, cnfilename, fileUrl, ModelEnum.ZhanDianDaoHuohuizong.getValue(), userid);
				this.downloadManagerDAO.creDownloadManager(down);
				json.put("errorCode", 0);
				json.put("remark", "已进入离线导出");
				return json.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("errorCode", 2);
			json.put("remark", "系统导出异常！");
			return json.toString();
		}
	}

	public String checkZhongZhuanTongJi(HttpServletResponse response, HttpServletRequest request, long userid) {
		JSONObject json = new JSONObject();
		String mouldfieldids = request.getParameter("exportmould2"); // 导出模板
		try {
			// 查询出数据
			String begindate = request.getParameter("begindate") == null ? "" : request.getParameter("begindate").toString();
			String enddate = request.getParameter("enddate") == null ? "" : request.getParameter("enddate").toString();
			long branchid = request.getParameter("branchid1") == null ? -1 : Long.parseLong(request.getParameter("branchid1"));
			String[] branchid2s = request.getParameterValues("branchid2s") == null ? new String[] {} : request.getParameterValues("branchid2s");
			String type = request.getParameter("type1") == null ? "startbranchid" : request.getParameter("type1");

			// 验证导出条件是否存在
			String datajson = this.setZhongZhuanTongJiJson(begindate, enddate, branchid, type, this.getStrings(branchid2s), mouldfieldids, userid);

			DownloadManager down = this.downloadManagerDAO.getDownloadManagerByJson(datajson);

			if (down != null) {
				json.put("errorCode", 2);
				json.put("remark", "该导出操作已经在下载列表中！");
				return json.toString();
			} else {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
				String fileName = "Order_" + df.format(new Date()) + "_"; // 文件名
				String otherName = "";
				String lastStr = ".xlsx";// 文件名后缀
				/*
				 * long count =
				 * Long.parseLong(request.getParameter("count")==null
				 * ?"0":request.getParameter("count").toString()); if(count>0){
				 * if
				 * (count/Page.EXCEL_PAGE_NUMBER+(count%Page.EXCEL_PAGE_NUMBER>
				 * 0?1:0)==1){ otherName = "1-"+count; }else{ otherName =
				 * ((page*Page.EXCEL_PAGE_NUMBER+1>count)?count:(page*Page.
				 * EXCEL_PAGE_NUMBER+1)) +"_"
				 * +((page+1)*Page.EXCEL_PAGE_NUMBER>count
				 * ?count:(page+1)*Page.EXCEL_PAGE_NUMBER); } }
				 */
				fileName = fileName + otherName + lastStr;
				String cnfilename = "中转订单统计(云)_" + df.format(new Date()) + "_" + otherName + lastStr;
				String fileUrl = this.getDmpDAO.getFileUrl() + "download" + System.getProperty("file.separator") + new SimpleDateFormat("yyyy-MM-dd").format(new Date())
						+ System.getProperty("file.separator");
				down = this.setDownloadManager(datajson, fileName, cnfilename, fileUrl, ModelEnum.ZhongZhuanDingDanTongJi.getValue(), userid);
				this.downloadManagerDAO.creDownloadManager(down);
				json.put("errorCode", 0);
				json.put("remark", "已进入离线导出");
				return json.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("errorCode", 2);
			json.put("remark", "系统导出异常！");
			return json.toString();
		}
	}

	/**
	 * 公共导出入口。只要在枚举中判断该执行哪个策略
	 *
	 * @param response
	 * @param request
	 * @param page
	 * @param sign
	 * @param userid
	 */
	public void repeatExportExcelMethod(DownloadManager down) {
		User user = this.getDmpDAO.getUserById(down.getUserid());
		if (user != null) {
			this.exportExcelService.setUser(user);
		}
		if (down.getModelid() == ModelEnum.TuoTouDingdanhuizong.getValue()) {// 进入妥投订单导出
			this.tuotouExcel(down);
		}
		if (down.getModelid() == ModelEnum.KuFangChuKuDingdantongji.getValue()) {// 进入库房出库订单统计导出
			this.kufangchukuExcel(down);
		}

		if (down.getModelid() == ModelEnum.KDKChuKuDingdantongji.getValue()) {// 进入库对库出库统计导出
			this.kdkchukuExcel(down);
		}
		if (down.getModelid() == ModelEnum.ZhiLiuDingdanhuizong.getValue()) {// 分站滞留汇总
			this.zhiliuhuizongExcel(down);
		}
		if (down.getModelid() == ModelEnum.FenZhanDaoHuotongji.getValue()) {// 进入分站到货统计导出
			this.daohuoExcel(down);
		}
		if (down.getModelid() == ModelEnum.ZhanDianDaoHuohuizong.getValue()) {// 进入站点到货汇总导出
			this.daohuoHuiZongExcel(down);
		}
		if (down.getModelid() == ModelEnum.TuiHuoChuZhanTongJi.getValue()) {// 进入站点到货汇总导出
			this.tuihouchuzhantongjiExcel(down);
		}

		if (down.getModelid() == ModelEnum.ZhongZhuanDingDanTongJi.getValue()) {// 进入中转订单统计导出
			this.zhongZhuanTongJiExcel(down);
		}
		if (down.getModelid() == ModelEnum.JuShouDingdanhuizong.getValue()) {// 拒收订单汇总
			this.jushouExcel(down);
		}
		if (down.getModelid() == ModelEnum.TiHuoDingDanTongJi.getValue()) {// 提货订单统计
			this.tihuoExcel(down);
		}
		if (down.getModelid() == ModelEnum.KuFangChuKuHuiZong.getValue()) {// 库房出库订单汇总
			this.kufangchukuhuizongExcel(down);
		}
		if (down.getModelid() == ModelEnum.TuiHuoZhanRuKuTongJi.getValue()) {// 退货站入库统计
			this.tuihuozhanrukutongjiExcel(down);
		}
		if (down.getModelid() == ModelEnum.KuFangZaiTuTongJi.getValue()) {// 退货站入库统计
			this.kuFangZaiTuTongJi(down);
		}
		if (down.getModelid() == ModelEnum.KuFangRuKuTongJi.getValue()) {// 库房入库统计
			this.KuFangRuKuTongJi(down);
		}
		if (down.getModelid() == ModelEnum.KeHuFaHuoTongJi.getValue()) {// 客户发货统计
			this.KeHuFaHuoTongJi(down);
		}
		if (down.getModelid() == ModelEnum.KeHuFaHuoHuiZong.getValue()) {// 客户发货统计
			this.KeHuFaHuoHuiZong(down);
		}
		if (down.getModelid() == ModelEnum.ZongHeChaXun.getValue()) {// 客户发货统计
			this.ZongHeChaXunExcel(down);
		}
		if (down.getModelid() == ModelEnum.DanliangChaxun.getValue()) {
			this.excelDanliangChaxun(down);
		}

	}

	/**
	 * 客户发货汇总 报表形式
	 *
	 * @param down
	 */
	private void KeHuFaHuoHuiZong(DownloadManager down) {
		String fileName = down.getFilename(); // 文件名
		String fileUrl = down.getFileurl();// 文件路径
		JSONObject json = JSONObject.fromObject(down.getDatajson());
		String begindate = json.getString("begindate");
		String enddate = json.getString("enddate");
		String cwbordertypeids = json.getString("cwbordertypeids");
		String kufangids = json.getString("kufangids");
		String customers = json.getString("customers");
		long flowordertype = json.getLong("flowordertype");
		String servicetype = json.getString("servicetype");
		// 库房 供货商 数量
		Map<Long, Map<Long, Long>> huiZongMap = this.cwbDAO.getKeHuFaHuoHuiZongMap(begindate, enddate, customers, cwbordertypeids, kufangids, flowordertype, servicetype);
		SXSSFWorkbook wb = new SXSSFWorkbook(); // excel文件,一个excel文件包含多个表
		Sheet sheet = wb.createSheet(); // 表，一个表包含多个行
		String filename = "客户发货汇总--报表" + DateTimeUtil.getNowDate() + ".xlsx";
		wb.setSheetName(0, filename);
		// 设置字体等样式
		Font font = wb.createFont();
		font.setFontName("Courier New");
		CellStyle style = wb.createCellStyle();
		style.setFont(font);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// 单元格
		Row row = sheet.createRow(0); // 由HSSFSheet生成行
		row.setHeightInPoints(15); // 设置行高
		String[] firstLine = new String[3];
		firstLine[0] = "库   房";
		firstLine[1] = "供货商";
		firstLine[2] = "合    计";
		List<Long> kufangidSet = new ArrayList<Long>(huiZongMap.keySet());
		List<Customer> customerList = new ArrayList<Customer>();
		List<Branch> branchList = this.getDmpDAO.getAllBranchs();
		// 供货商列表
		if (customers.length() > 0) {
			customerList = this.getDmpDAO.getCustomerByIds(customers);
		} else {
			customerList = this.getDmpDAO.getAllCustomers();
		}
		if ((kufangidSet != null) && (kufangidSet.size() > 0)) {
			sheet.addMergedRegion(new CellRangeAddress(0, (short) 0, 1, (short) customerList.size())); // 合并第一行
																										// 供货商
																										// 单元格
			sheet.createFreezePane(1, 0, kufangidSet.size() + 1, 0);// 冻结第一列
			// (int firstRow, int lastRow, int firstCol, int lastCol
			sheet.addMergedRegion(new CellRangeAddress(0, (short) 1, 0, (short) 0));// 库房的合并单元格
			sheet.addMergedRegion(new CellRangeAddress(0, (short) 1, customerList.size() + 1, (short) customerList.size() + 1));// 库房的合并单元格
			Cell cell = row.createCell(0);
			cell.setCellStyle(style);
			cell.setCellValue(firstLine[0]);
			Cell cusCell = row.createCell(1);
			cusCell.setCellStyle(style);
			cusCell.setCellValue(firstLine[1]);
			Row secRow = sheet.createRow(1);
			Cell countCell = row.createCell(customerList.size() + 1);
			countCell.setCellStyle(style);
			countCell.setCellValue(firstLine[2]);// 第一行写入结束
			for (int j = 1; j < (customerList.size() + 1); j++) {
				Cell customerCell = secRow.createCell(j);
				customerCell.setCellStyle(style);
				customerCell.setCellValue(customerList.get(j - 1).getCustomername());
			}

			for (int i = 1; i < (kufangidSet.size() + 1); i++) {
				Row item = sheet.createRow(i + 1);
				Cell kufangCell = item.createCell(0);
				kufangCell.setCellStyle(style);
				kufangCell.setCellValue(this.getQueryBranchName(branchList, kufangidSet.get(i - 1)));
				// 开始循环对应站点的 供货商
				long countForKufang = 0l;
				for (int k = 1; k < (customerList.size() + 1); k++) {
					Cell customerCell = item.createCell(k);
					customerCell.setCellStyle(style);
					long num = huiZongMap.get(kufangidSet.get(i - 1)).get(customerList.get(k - 1).getCustomerid()) == null ? 0 : huiZongMap.get(kufangidSet.get(i - 1)).get(
							customerList.get(k - 1).getCustomerid());
					customerCell.setCellValue(num);
					countForKufang += num;
					this.keHuFaHuoDataDao.creDate(begindate, enddate, kufangidSet.get(i - 1), customerList.get(k - 1).getCustomerid(), num, down.getId());
				}
				Cell countForKufangCell = item.createCell(customerList.size() + 1);
				countForKufangCell.setCellStyle(style);
				countForKufangCell.setCellValue(countForKufang);
			}

			// 下面的合计
			Row countForDown = sheet.createRow(kufangidSet.size() + 2);
			Cell firCell = countForDown.createCell(0);
			firCell.setCellStyle(style);
			firCell.setCellValue("合计");
			long total = 0l;
			for (int f = 1; f < (customerList.size() + 1); f++) {
				long countForDownLong = 0l;
				for (int i = 1; i < (kufangidSet.size() + 1); i++) {
					long numDown = huiZongMap.get(kufangidSet.get(i - 1)).get(customerList.get(f - 1).getCustomerid()) == null ? 0 : huiZongMap.get(kufangidSet.get(i - 1)).get(
							customerList.get(f - 1).getCustomerid());
					countForDownLong += numDown;
				}

				Cell customerCell = countForDown.createCell(f);
				customerCell.setCellStyle(style);
				customerCell.setCellValue(countForDownLong);
				total += countForDownLong;
			}
			Cell bigCell = countForDown.createCell(customerList.size() + 1);
			bigCell.setCellStyle(style);
			bigCell.setCellValue(total);

		}
		File f = new File(fileUrl);
		File f2 = new File(fileUrl + fileName);

		try {
			if (!f.exists()) {
				f.mkdirs();
			}
			f2.createNewFile();
			OutputStream os = new FileOutputStream(f2);
			wb.write(os);
			os.close();
			this.downloadManagerDAO.updateStateById(1, down.getId(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			System.out.println("文件写入完成");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 客户发货统计 导出
	 *
	 * @param down
	 */
	private void KeHuFaHuoTongJi(DownloadManager down) {
		String sheetName = "订单信息"; // sheet的名称
		String fileName = down.getFilename(); // 文件名
		String fileUrl = down.getFileurl();// 文件路径
		JSONObject json = JSONObject.fromObject(down.getDatajson());
		String begindate = json.getString("begindate");
		String enddate = json.getString("enddate");
		String cwbordertypeids = json.getString("cwbordertypeids");
		String kufangids = json.getString("kufangids");
		String customers = json.getString("customers");
		long flowordertype = json.getLong("flowordertype");
		long page = json.getLong("page");
		String mouldfieldids = json.getString("mouldfieldids");
		String servicetype = json.getString("servicetype");

		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		if ((mouldfieldids != null) && !"0".equals(mouldfieldids)) { // 选择模板
			List<SetExportField> listSetExportField = this.getDmpDAO.getSetExportFieldByExportstate(mouldfieldids);
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		} else {
			List<SetExportField> listSetExportField = this.getDmpDAO.getSetExportFieldByExportstate("0");
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		}
		final String[] cloumnName4 = cloumnName1;
		final String[] cloumnName5 = cloumnName2;
		final String[] cloumnName6 = cloumnName3;
		final String sql = this.cwbDAO.getSQLExportKeHuFaHuoTongJi(page, begindate, enddate, cwbordertypeids, kufangids, customers, flowordertype, servicetype);
		final long mapid = down.getId();
		this.exportExcel(sql, cloumnName4, cloumnName5, cloumnName6, mapid, sheetName, fileName, fileUrl);
	}

	/**
	 * 库房入库统计 导出
	 *
	 * @param down
	 */
	private void KuFangRuKuTongJi(DownloadManager down) {
		String sheetName = "订单信息"; // sheet的名称
		String fileName = down.getFilename(); // 文件名
		String fileUrl = down.getFileurl();// 文件路径
		JSONObject json = JSONObject.fromObject(down.getDatajson());
		String isruku = json.getString("isruku");
		String begindate = json.getString("begindate");
		String enddate = json.getString("enddate");
		String emaildatebegin = json.getString("emaildatebegin");
		String emaildateend = json.getString("emaildateend");
		String customers = json.getString("customers");
		long kufangid = json.getLong("kufangid");
		long cwbordertypeid = json.getLong("cwbordertypeid");
		long page = json.getLong("page");
		String mouldfieldids = json.getString("mouldfieldids");

		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		if ((mouldfieldids != null) && !"0".equals(mouldfieldids)) { // 选择模板
			List<SetExportField> listSetExportField = this.getDmpDAO.getSetExportFieldByExportstate(mouldfieldids);
			cloumnName1 = new String[listSetExportField.size() + 1];
			cloumnName2 = new String[listSetExportField.size() + 1];
			cloumnName3 = new String[listSetExportField.size() + 1];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
			// ===添加入库操作人zs===
			cloumnName1[listSetExportField.size()] = "入库操作人";
			cloumnName2[listSetExportField.size()] = "intowarehouseuserid";
			cloumnName3[listSetExportField.size()] = "String";

		} else {
			List<SetExportField> listSetExportField = this.getDmpDAO.getSetExportFieldByExportstate("0");
			cloumnName1 = new String[listSetExportField.size() + 1];
			cloumnName2 = new String[listSetExportField.size() + 1];
			cloumnName3 = new String[listSetExportField.size() + 1];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
			// ===添加入库操作人zs===
			cloumnName1[listSetExportField.size()] = "入库操作人";
			cloumnName2[listSetExportField.size()] = "intowarehouseuserid";
			cloumnName3[listSetExportField.size()] = "String";
		}
		final String[] cloumnName4 = cloumnName1;
		final String[] cloumnName5 = cloumnName2;
		final String[] cloumnName6 = cloumnName3;
		final String sql = this.cwbDAO.getSQLExportKuFangRuKuTongJi(page, isruku, begindate, enddate, emaildatebegin, emaildateend, kufangid, cwbordertypeid, customers);
		final long mapid = down.getId();
		this.exportExcel(sql, cloumnName4, cloumnName5, cloumnName6, mapid, sheetName, fileName, fileUrl);
	}

	/**
	 * 库房在途统计 导出
	 *
	 * @param down
	 */
	private void kuFangZaiTuTongJi(DownloadManager down) {
		String sheetName = "订单信息"; // sheet的名称
		String fileName = down.getFilename(); // 文件名
		String fileUrl = down.getFileurl();// 文件路径
		JSONObject json = JSONObject.fromObject(down.getDatajson());

		long datetype = json.getLong("datetype");
		String nextbranchid = json.getString("nextbranchid");
		String begindate = json.getString("begindate");
		String enddate = json.getString("enddate");
		String kufangid = json.getString("kufangid");
		String cwbordertypeids = json.getString("cwbordertypeids");
		long page = json.getLong("page");
		String mouldfieldids = json.getString("mouldfieldids");
		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		if ((mouldfieldids != null) && !"0".equals(mouldfieldids)) { // 选择模板
			List<SetExportField> listSetExportField = this.getDmpDAO.getSetExportFieldByExportstate(mouldfieldids);
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		} else {
			List<SetExportField> listSetExportField = this.getDmpDAO.getSetExportFieldByExportstate("0");
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		}
		final String[] cloumnName4 = cloumnName1;
		final String[] cloumnName5 = cloumnName2;
		final String[] cloumnName6 = cloumnName3;
		final String sql = this.cwbDAO.getSQLExportKuFangZaiTu(page, begindate, enddate, kufangid, nextbranchid, cwbordertypeids, datetype);
		final long mapid = down.getId();
		this.exportExcel(sql, cloumnName4, cloumnName5, cloumnName6, mapid, sheetName, fileName, fileUrl);
	}

	/**
	 * 退货入库统计 导出
	 *
	 * @param down
	 */
	private void tuihuozhanrukutongjiExcel(DownloadManager down) {
		String sheetName = "订单信息"; // sheet的名称
		String fileName = down.getFilename(); // 文件名
		String fileUrl = down.getFileurl();// 文件路径
		JSONObject json = JSONObject.fromObject(down.getDatajson());
		String begindate = json.getString("begindate");
		String enddate = json.getString("enddate");
		String customerids = json.getString("customerids");
		String branchids = json.getString("branchids");
		String cwbordertypeids = json.getString("cwbordertypeids");
		long page = json.getLong("page");
		String mouldfieldids = json.getString("mouldfieldids");
		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		if ((mouldfieldids != null) && !"0".equals(mouldfieldids)) { // 选择模板
			List<SetExportField> listSetExportField = this.getDmpDAO.getSetExportFieldByExportstate(mouldfieldids);
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		} else {
			List<SetExportField> listSetExportField = this.getDmpDAO.getSetExportFieldByExportstate("0");
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		}
		final String[] cloumnName4 = cloumnName1;
		final String[] cloumnName5 = cloumnName2;
		final String[] cloumnName6 = cloumnName3;
		final String sql = this.cwbDAO.getSQLExportTuiHuoZhanRuKu(page, begindate, enddate, branchids, customerids, cwbordertypeids);
		final long mapid = down.getId();
		this.exportExcel(sql, cloumnName4, cloumnName5, cloumnName6, mapid, sheetName, fileName, fileUrl);
	}

	/**
	 * 退货出站统计 导出
	 *
	 * @param down
	 */
	private void tuihouchuzhantongjiExcel(DownloadManager down) {
		String sheetName = "订单信息"; // sheet的名称
		String fileName = down.getFilename(); // 文件名
		String fileUrl = down.getFileurl();// 文件路径
		JSONObject json = JSONObject.fromObject(down.getDatajson());
		String begindate = json.getString("begindate");
		String enddate = json.getString("enddate");
		String customerids = json.getString("customerids");
		String branchids = json.getString("branchids");
		long page = json.getLong("page");
		long istuihuozhanruku = json.getLong("istuihuozhanruku");
		String mouldfieldids = json.getString("mouldfieldids");
		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		if ((mouldfieldids != null) && !"0".equals(mouldfieldids)) { // 选择模板
			List<SetExportField> listSetExportField = this.getDmpDAO.getSetExportFieldByExportstate(mouldfieldids);
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		} else {
			List<SetExportField> listSetExportField = this.getDmpDAO.getSetExportFieldByExportstate("0");
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		}
		final String[] cloumnName4 = cloumnName1;
		final String[] cloumnName5 = cloumnName2;
		final String[] cloumnName6 = cloumnName3;
		final String sql = this.cwbDAO.getSQLExportTuiHuoChuZhan(page, begindate, enddate, branchids, customerids, istuihuozhanruku);
		final long mapid = down.getId();
		this.exportExcel(sql, cloumnName4, cloumnName5, cloumnName6, mapid, sheetName, fileName, fileUrl);

	}

	/**
	 * 拒收 订单汇总的导出
	 *
	 * @param down
	 */
	private void jushouExcel(DownloadManager down) {
		String sheetName = "订单信息"; // sheet的名称
		String fileName = down.getFilename(); // 文件名
		String fileUrl = down.getFileurl();// 文件路径
		JSONObject json = JSONObject.fromObject(down.getDatajson());
		String cwbordertypeids = json.getString("cwbordertypeids");
		String begindate = json.getString("begindate");
		String enddate = json.getString("enddate");
		String customerids = json.getString("customerids");
		String dispatchbranchids = json.getString("dispatchbranchids");
		long isauditTime = json.getLong("isauditTime");
		long isaudit = json.getLong("isaudit");
		long deliveryid = json.getLong("deliverid");
		long page = json.getLong("page");
		String mouldfieldids2 = json.getString("mouldfieldids"); // 导出模板
		String operationOrderResultTypes = json.getString("operationOrderResultTypes");
		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		if ((mouldfieldids2 != null) && !"0".equals(mouldfieldids2)) { // 选择模板
			List<SetExportField> listSetExportField = this.getDmpDAO.getSetExportFieldByExportstate(mouldfieldids2);
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		} else {
			List<SetExportField> listSetExportField = this.getDmpDAO.getSetExportFieldByExportstate("0");
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		}
		final String[] cloumnName4 = cloumnName1;
		final String[] cloumnName5 = cloumnName2;
		final String[] cloumnName6 = cloumnName3;
		final String sql = this.cwbDAO.getSQLExportJuShou(page, begindate, enddate, dispatchbranchids, customerids, deliveryid, cwbordertypeids, isauditTime, isaudit, operationOrderResultTypes);
		final long mapid = down.getId();
		this.exportExcel(sql, cloumnName4, cloumnName5, cloumnName6, mapid, sheetName, fileName, fileUrl);
	}

	/**
	 * 提货订单统计
	 *
	 * @param down
	 */
	private void tihuoExcel(DownloadManager down) {
		String sheetName = "订单信息"; // sheet的名称
		String fileName = down.getFilename(); // 文件名
		String fileUrl = down.getFileurl();// 文件路径
		JSONObject json = JSONObject.fromObject(down.getDatajson());
		String begindate = json.getString("begindate");
		String enddate = json.getString("enddate");
		String customerids = json.getString("customerids");
		long page = json.getLong("page");
		String[] cloumnName1 = new String[5]; // 导出的列名
		String[] cloumnName2 = new String[5]; // 导出的英文列名
		String[] cloumnName3 = new String[5]; // 导出的数据类型

		this.exportExcelService.SetTiHuoFields(cloumnName1, cloumnName2, cloumnName3);
		final String[] cloumnName4 = cloumnName1;
		final String[] cloumnName5 = cloumnName2;
		final String[] cloumnName6 = cloumnName3;
		final String sql = this.cwbDAO.getSQLExportTiHuo(page, begindate, enddate, customerids);
		final long mapid = down.getId();
		this.tiHuoExportExcel(sql, cloumnName4, cloumnName5, cloumnName6, mapid, sheetName, fileName, fileUrl);
	}

	/**
	 * 滞留订单汇总的 导出
	 *
	 * @param down
	 */
	private void zhiliuhuizongExcel(DownloadManager down) {
		String sheetName = "订单信息"; // sheet的名称
		String fileName = down.getFilename(); // 文件名
		String fileUrl = down.getFileurl();// 文件路径
		JSONObject json = JSONObject.fromObject(down.getDatajson());
		String cwbordertypeids = json.getString("cwbordertypeids");
		String begindate = json.getString("begindate");
		String enddate = json.getString("enddate");
		String customerids = json.getString("customerids");
		String dispatchbranchids = json.getString("dispatchbranchids");
		long isauditTime = json.getLong("isauditTime");
		long isaudit = json.getLong("isaudit");
		long deliveryid = json.getLong("deliverid");
		long page = json.getLong("page");
		String mouldfieldids2 = json.getString("mouldfieldids"); // 导出模板
		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		if ((mouldfieldids2 != null) && !"0".equals(mouldfieldids2)) { // 选择模板
			List<SetExportField> listSetExportField = this.getDmpDAO.getSetExportFieldByExportstate(mouldfieldids2);
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		} else {
			List<SetExportField> listSetExportField = this.getDmpDAO.getSetExportFieldByExportstate("0");
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		}
		final String[] cloumnName4 = cloumnName1;
		final String[] cloumnName5 = cloumnName2;
		final String[] cloumnName6 = cloumnName3;
		final String sql = this.cwbDAO.getSQLExportZhiLiu(page, begindate, enddate, dispatchbranchids, customerids, deliveryid, cwbordertypeids, isauditTime, isaudit);
		final long mapid = down.getId();
		this.exportExcel(sql, cloumnName4, cloumnName5, cloumnName6, mapid, sheetName, fileName, fileUrl);
	}

	/**
	 * 妥投导出方法
	 *
	 * @param down
	 */
	private void tuotouExcel(DownloadManager down) {
		String sheetName = "订单信息"; // sheet的名称
		String fileName = down.getFilename(); // 文件名
		String fileUrl = down.getFileurl();// 文件路径
		JSONObject json = JSONObject.fromObject(down.getDatajson());
		String cwbordertypeids = json.getString("cwbordertypeids");
		String begindate = json.getString("begindate");
		String enddate = json.getString("enddate");
		String customerids = json.getString("customerids");
		String dispatchbranchid = json.getString("dispatchbranchids");
		long paywayid = json.getLong("paywayid");
		long isaudit = json.getLong("isaudit");
		long isauditTime = json.getLong("isauditTime");
		long deliverid = json.getLong("deliverid");
		long page = json.getLong("page");
		Integer paybackfeeIsZero = json.getInt("paybackfeeIsZero");
		String operationOrderResultTypes = json.getString("operationOrderResultTypes");
		String mouldfieldids2 = json.getString("mouldfieldids"); // 导出模板
		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		if ((mouldfieldids2 != null) && !"0".equals(mouldfieldids2)) { // 选择模板
			List<SetExportField> listSetExportField = this.getDmpDAO.getSetExportFieldByExportstate(mouldfieldids2);
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		} else {
			List<SetExportField> listSetExportField = this.getDmpDAO.getSetExportFieldByExportstate("0");
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		}
		final String[] cloumnName4 = cloumnName1;
		final String[] cloumnName5 = cloumnName2;
		final String[] cloumnName6 = cloumnName3;
		final String sql = this.cwbDAO.getSQLExportTuotou(begindate, enddate, isaudit, isauditTime, customerids, cwbordertypeids, paywayid, dispatchbranchid, deliverid, operationOrderResultTypes,
				page, paybackfeeIsZero);
		// downloadManagerService.saveXiazaitoMap(down.getId(), 0);//正在下载
		final long mapid = down.getId();
		this.exportExcel(sql, cloumnName4, cloumnName5, cloumnName6, mapid, sheetName, fileName, fileUrl);

	}

	/**
	 * 单量查询
	 *
	 * @param down
	 */
	private void excelDanliangChaxun(DownloadManager down) {
		String sheetName = "单量查询"; // sheet的名称
		String fileName = down.getFilename(); // 文件名
		String fileUrl = down.getFileurl();// 文件路径
		JSONObject json = JSONObject.fromObject(down.getDatajson());
		String kufangid = json.getString("kufangid");
		String begindate = json.getString("begindate");
		String enddate = json.getString("enddate");
		String customerids = json.getString("customerids");
		String type = json.getString("select");
		String mouldfieldids2 = json.getString("mouldfieldids"); // 导出模板
		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		if ((mouldfieldids2 != null) && !"0".equals(mouldfieldids2)) { // 选择模板
			List<SetExportField> listSetExportField = this.getDmpDAO.getSetExportFieldByExportstate(mouldfieldids2);
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		} else {
			List<SetExportField> listSetExportField = this.getDmpDAO.getSetExportFieldByExportstate("0");
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		}
		final String[] cloumnName4 = cloumnName1;
		final String[] cloumnName5 = cloumnName2;
		final String[] cloumnName6 = cloumnName3;
		String sql1 = "";
		if (type.equals("1")) {
			sql1 = this.cwbDAO.getSQLExportDanLiangChaXun(begindate, enddate, customerids, kufangid);
		} else if (type.equals("2")) {
			sql1 = this.cwbDAO.getSQLExportDanLiangChaXunForRuKu(begindate, enddate, customerids, kufangid);
		} else {
			sql1 = this.cwbDAO.getSQLExportDanLiangChaXunForChuKu(begindate, enddate, customerids, kufangid);
		}
		final String sql = sql1;
		final long mapid = down.getId();
		this.exportExcel(sql, cloumnName4, cloumnName5, cloumnName6, mapid, sheetName, fileName, fileUrl);

	}

	// 库房出库订单统计
	private void kufangchukuExcel(DownloadManager down) {
		String sheetName = "订单信息"; // sheet的名称
		String fileName = down.getFilename(); // 文件名
		String fileUrl = down.getFileurl();// 文件路径
		JSONObject json = JSONObject.fromObject(down.getDatajson());

		String cwbordertypeids = json.getString("cwbordertypeid");
		String begindate = json.getString("begindate");
		String enddate = json.getString("enddate");
		String customerids = json.getString("customerid");
		String nextbranchids = json.getString("nextbranchid");
		String kufangids = json.getString("kufangid");
		long page = json.getLong("page");
		String mouldfieldids2 = json.getString("mouldfieldids"); // 导出模板
		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		if ((mouldfieldids2 != null) && !"0".equals(mouldfieldids2)) { // 选择模板
			List<SetExportField> listSetExportField = this.getDmpDAO.getSetExportFieldByExportstate(mouldfieldids2);
			cloumnName1 = new String[listSetExportField.size() + 1];
			cloumnName2 = new String[listSetExportField.size() + 1];
			cloumnName3 = new String[listSetExportField.size() + 1];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
			// ===添加出库操作人zs===
			cloumnName1[listSetExportField.size()] = "出库操作人";
			cloumnName2[listSetExportField.size()] = "outstoreroomtimeuserid";
			cloumnName3[listSetExportField.size()] = "String";
		} else {
			List<SetExportField> listSetExportField = this.getDmpDAO.getSetExportFieldByExportstate("0");
			cloumnName1 = new String[listSetExportField.size() + 1];
			cloumnName2 = new String[listSetExportField.size() + 1];
			cloumnName3 = new String[listSetExportField.size() + 1];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
			// ===添加出库操作人zs===
			cloumnName1[listSetExportField.size()] = "出库操作人";
			cloumnName2[listSetExportField.size()] = "outstoreroomtimeuserid";
			cloumnName3[listSetExportField.size()] = "String";
		}
		final String[] cloumnName4 = cloumnName1;
		final String[] cloumnName5 = cloumnName2;
		final String[] cloumnName6 = cloumnName3;
		final String sql = this.cwbDAO.getSQLExportKuFangChuku(page, begindate, enddate, customerids, kufangids, nextbranchids, cwbordertypeids);
		// downloadManagerService.saveXiazaitoMap(down.getId(), 0);//正在下载
		final long mapid = down.getId();
		this.exportExcel(sql, cloumnName4, cloumnName5, cloumnName6, mapid, sheetName, fileName, fileUrl);

	}

	// 库对库出库导出
	private void kdkchukuExcel(DownloadManager down) {
		String sheetName = "订单信息"; // sheet的名称
		String fileName = down.getFilename(); // 文件名
		String fileUrl = down.getFileurl();// 文件路径
		JSONObject json = JSONObject.fromObject(down.getDatajson());

		String cwbordertypeids = json.getString("cwbordertypeid");
		String begindate = json.getString("begindate");
		String enddate = json.getString("enddate");
		String customerids = json.getString("customerid");
		String nextbranchids = json.getString("nextbranchid");
		String kufangids = json.getString("kufangid");
		long page = json.getLong("page");
		String mouldfieldids2 = json.getString("mouldfieldids"); // 导出模板
		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		if ((mouldfieldids2 != null) && !"0".equals(mouldfieldids2)) { // 选择模板
			List<SetExportField> listSetExportField = this.getDmpDAO.getSetExportFieldByExportstate(mouldfieldids2);
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		} else {
			List<SetExportField> listSetExportField = this.getDmpDAO.getSetExportFieldByExportstate("0");
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		}
		final String[] cloumnName4 = cloumnName1;
		final String[] cloumnName5 = cloumnName2;
		final String[] cloumnName6 = cloumnName3;
		final String sql = this.cwbDAO.getSQLExportKDKChuku(page, begindate, enddate, customerids, kufangids, nextbranchids, cwbordertypeids);
		// downloadManagerService.saveXiazaitoMap(down.getId(), 0);//正在下载
		final long mapid = down.getId();
		this.exportExcel(sql, cloumnName4, cloumnName5, cloumnName6, mapid, sheetName, fileName, fileUrl);

	}

	/**
	 * 库房出库统计（云）导出报表
	 */
	private void kufangchukuhuizongExcel(DownloadManager down) {
		long mapid = down.getId();

		JSONObject json = JSONObject.fromObject(down.getDatajson());

		String begindate = json.getString("begindate");
		String enddate = json.getString("enddate");
		String customerid = json.getString("customerid");
		String kufangid = json.getString("kufangid");
		String nextbranchid = json.getString("nextbranchid");
		long userid = json.getLong("userid");
		User user = this.getDmpDAO.getUserById(userid);

		// 加载供货商
		List<Customer> customerlist = new ArrayList<Customer>();
		if (customerid.length() > 0) {
			customerlist = this.getDmpDAO.getCustomerByIds(customerid);
		} else {
			customerlist = this.getDmpDAO.getAllCustomers();
		}
		// 加载站点区域权限
		List<Branch> branchAllList = new ArrayList<Branch>();
		String nextbranchids = "";
		if ((user != null) && (user.getUserid() > 0)) {
			if (nextbranchid.length() > 0) {
				branchAllList = this.getDmpDAO.getBranchByBranchids(nextbranchid);
			} else {
				branchAllList = this.getDmpDAO.getBranchListByUser(user.getUserid());
			}

			for (Branch b : branchAllList) {
				nextbranchids += b.getBranchid() + ",";
			}

		}

		// 如果是点击查询按钮，封装查询数据
		begindate = begindate.length() == 0 ? DateTimeUtil.getNowTime() : begindate;
		enddate = enddate.length() == 0 ? DateTimeUtil.getNowTime() : enddate;
		// 下一站id拼串
		nextbranchids = nextbranchids.length() > 0 ? nextbranchids.substring(0, nextbranchids.length() - 1) : "";

		this.creChukuDataResultList(customerlist, kufangid, begindate, enddate, nextbranchids, mapid, user.getUserid());

		this.downloadManagerDAO.updateStateById(1, mapid, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));// 完成下载

	}

	private void daohuoExcel(DownloadManager down) {
		String sheetName = "订单信息"; // sheet的名称
		String fileName = down.getFilename(); // 文件名
		String fileUrl = down.getFileurl();// 文件路径
		JSONObject json = JSONObject.fromObject(down.getDatajson());

		String cwbordertypeids = json.getString("cwbordertypeid");
		String begindate = json.getString("begindate");
		String enddate = json.getString("enddate");
		long customerid = json.getLong("customerid");
		String currentBranchids = json.getString("currentBranchid");
		long kufangid = json.getLong("kufangid");
		long isnowdata = json.getLong("isnowdata");
		long page = json.getLong("page");
		String mouldfieldids2 = json.getString("mouldfieldids"); // 导出模板
		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		if ((mouldfieldids2 != null) && !"0".equals(mouldfieldids2)) { // 选择模板
			List<SetExportField> listSetExportField = this.getDmpDAO.getSetExportFieldByExportstate(mouldfieldids2);
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		} else {
			List<SetExportField> listSetExportField = this.getDmpDAO.getSetExportFieldByExportstate("0");
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		}
		final String[] cloumnName4 = cloumnName1;
		final String[] cloumnName5 = cloumnName2;
		final String[] cloumnName6 = cloumnName3;
		final String sql = this.cwbDAO.getSQLExportDaoHuo(page, begindate, enddate, customerid, kufangid, currentBranchids, cwbordertypeids, isnowdata);
		// downloadManagerService.saveXiazaitoMap(down.getId(), 0);//正在下载
		final long mapid = down.getId();
		this.exportExcel(sql, cloumnName4, cloumnName5, cloumnName6, mapid, sheetName, fileName, fileUrl);

	}

	private void daohuoHuiZongExcel(DownloadManager down) {
		String sheetName = "订单信息"; // sheet的名称
		String fileName = down.getFilename(); // 文件名
		String fileUrl = down.getFileurl();// 文件路径
		JSONObject json = JSONObject.fromObject(down.getDatajson());

		String begindate = json.getString("begindate");
		String enddate = json.getString("enddate");
		long branchid = json.getLong("branchid");
		String mouldfieldids2 = json.getString("mouldfieldids"); // 导出模板
		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		if ((mouldfieldids2 != null) && !"0".equals(mouldfieldids2)) { // 选择模板
			List<SetExportField> listSetExportField = this.getDmpDAO.getSetExportFieldByExportstate(mouldfieldids2);
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		} else {
			List<SetExportField> listSetExportField = this.getDmpDAO.getSetExportFieldByExportstate("0");
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		}
		final String[] cloumnName4 = cloumnName1;
		final String[] cloumnName5 = cloumnName2;
		final String[] cloumnName6 = cloumnName3;
		final String sql = this.cwbDAO.getSQLExportDaoHuoHuizong(begindate, enddate, branchid);
		// downloadManagerService.saveXiazaitoMap(down.getId(), 0);//正在下载
		final long mapid = down.getId();
		this.exportExcel(sql, cloumnName4, cloumnName5, cloumnName6, mapid, sheetName, fileName, fileUrl);

	}

	private void zhongZhuanTongJiExcel(DownloadManager down) {
		String sheetName = "订单信息"; // sheet的名称
		String fileName = down.getFilename(); // 文件名
		String fileUrl = down.getFileurl();// 文件路径
		JSONObject json = JSONObject.fromObject(down.getDatajson());

		String begindate = json.getString("begindate");
		String enddate = json.getString("enddate");
		long branchid = json.getLong("branchid");
		String type = json.getString("type");
		String branchid2 = json.getString("branchid2");
		String mouldfieldids2 = json.getString("mouldfieldids"); // 导出模板

		String nextbranchids = "";
		String startbranchids = "";

		if (type.equals("startbranchid")) {
			nextbranchids = branchid2;
			startbranchids = branchid + "";
		} else if (type.equals("nextbranchid")) {
			nextbranchids = branchid + "";
			startbranchids = branchid2;
		}

		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		if ((mouldfieldids2 != null) && !"0".equals(mouldfieldids2)) { // 选择模板
			List<SetExportField> listSetExportField = this.getDmpDAO.getSetExportFieldByExportstate(mouldfieldids2);
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		} else {
			List<SetExportField> listSetExportField = this.getDmpDAO.getSetExportFieldByExportstate("0");
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		}
		final String[] cloumnName4 = cloumnName1;
		final String[] cloumnName5 = cloumnName2;
		final String[] cloumnName6 = cloumnName3;
		final String sql = this.cwbDAO.getSQLExportZhongZhuanTongJizong(begindate, enddate, nextbranchids, startbranchids);
		// downloadManagerService.saveXiazaitoMap(down.getId(), 0);//正在下载
		final long mapid = down.getId();
		this.exportExcel(sql, cloumnName4, cloumnName5, cloumnName6, mapid, sheetName, fileName, fileUrl);

	}

	public void kufangchukuexportFormExcel(final long mapid, String fileName, String fileUrl, List<Branch> branchList, List<Customer> customerlist, Map<String, Long> branchAndCustomerMap,
			Map<Long, Long> customerAllMap, Map<Long, Long> branchAllMap) {
		try {
			/*
			 * if(!downloadManagerService.checkMap(mapid)){
			 * System.out.println("导出终止!"); File myFilePath = new
			 * File(fileUrl+fileName); if (!myFilePath.exists()) {//判断文件是否已经生成
			 * return; } myFilePath.delete(); // 删除空文件 return; }
			 */

			SXSSFWorkbook wb = new SXSSFWorkbook(); // excel文件,一个excel文件包含多个表
			Sheet sheet = wb.createSheet(); // 表，一个表包含多个行
			String filename = "库房出库汇总_" + DateTimeUtil.getNowDate() + ".xlsx";
			wb.setSheetName(0, filename);
			// 设置字体等样式
			Font font = wb.createFont();
			font.setFontName("Courier New");
			CellStyle style = wb.createCellStyle();
			style.setFont(font);
			style.setAlignment(CellStyle.ALIGN_CENTER);
			style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

			// 单元格
			Row row = sheet.createRow(0); // 由HSSFSheet生成行
			row.setHeightInPoints(15); // 设置行高
			// 第一行第一列
			Cell cell = row.createCell(0);
			cell.setCellStyle(style);
			cell.setCellValue("站点");
			// 第一行最后一列
			Cell cusCell = row.createCell(customerlist.size() + 1);
			cusCell.setCellStyle(style);
			cusCell.setCellValue("合计");
			// 第一行非第一列，非最后一列
			long count = 0l;
			if ((customerlist != null) && (customerlist.size() > 0)) {
				for (Customer c : customerlist) {
					// 行，显示供货商
					Cell firstLine = row.createCell(customerlist.indexOf(c) + 1);
					firstLine.setCellStyle(style);
					firstLine.setCellValue(c.getCustomername());
				}
			}

			if ((branchList != null) && (branchList.size() > 0)) {
				// 冻结第一列
				sheet.createFreezePane(1, 0, branchList.size() + 1, 0);
				for (int i = 1; i <= (branchList.size() + 1); i++) {
					// 列，是站点名称
					Row rowFir = sheet.createRow(i);
					Cell deliveryCell = rowFir.createCell(0);
					deliveryCell.setCellStyle(style);
					if (i == (branchList.size() + 1)) {
						deliveryCell.setCellValue("合计");
					} else {
						deliveryCell.setCellValue(branchList.get(i - 1).getBranchname());
					}
					for (int j = 1; j < (customerlist.size() + 1); j++) {
						// 对应站点对应供货商的值
						Cell cellFir2 = rowFir.createCell(j);
						cellFir2.setCellStyle(style);
						if (i == (branchList.size() + 1)) {
							long countBycustomerid = customerAllMap.get(customerlist.get(j - 1).getCustomerid()) == null ? 0 : customerAllMap.get(customerlist.get(j - 1).getCustomerid());
							cellFir2.setCellValue(countBycustomerid);
						} else {
							long customeridbranchidcount = branchAndCustomerMap.get(branchList.get(i - 1).getBranchid() + "_" + customerlist.get(j - 1).getCustomerid()) == null ? 0
									: branchAndCustomerMap.get(branchList.get(i - 1).getBranchid() + "_" + customerlist.get(j - 1).getCustomerid());
							cellFir2.setCellValue(customeridbranchidcount);
						}
					}

					Cell cellFir = rowFir.createCell(customerlist.size() + 1);
					cellFir.setCellStyle(style);
					if (i == (branchList.size() + 1)) {
						// 右下角的总合计
						cellFir.setCellValue(count);
					} else {
						// 右边合计
						long branchcount = branchAllMap.get(branchList.get(i - 1).getBranchid()) == null ? 0 : branchAllMap.get(branchList.get(i - 1).getBranchid());
						count += branchcount;
						cellFir.setCellValue(branchcount);
					}

				}
			}

			File f = new File(fileUrl);
			File f2 = new File(fileUrl + fileName);

			try {
				if (!f.exists()) {
					f.mkdirs();
				}
				f2.createNewFile();
				OutputStream os = new FileOutputStream(f2);
				wb.write(os);
				os.close();
				System.out.println("文件写入完成");
			} catch (Exception e) {
				e.printStackTrace();
			}

			this.logger.info("导出完成:" + fileUrl + fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 公共导出方法
	 *
	 * @param sql
	 * @param cloumnName4
	 * @param cloumnName5
	 * @param cloumnName6
	 * @param mapid
	 * @param sheetName
	 * @param fileName
	 * @param fileUrl
	 */
	private void exportExcel(final String sql, final String[] cloumnName4, final String[] cloumnName5, final String[] cloumnName6, final long mapid, String sheetName, String fileName, String fileUrl) {

		try {
			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					final List<User> uList = DataStatisticService.this.getDmpDAO.getUserForALL();
					final Map<Long, Customer> cMap = DataStatisticService.this.getDmpDAO.getAllCustomersToMap();
					final List<Branch> bList = DataStatisticService.this.getDmpDAO.getAllBranchs();
					final List<Common> commonList = DataStatisticService.this.getDmpDAO.getAllCommons();
					final List<CustomWareHouse> cWList = DataStatisticService.this.getDmpDAO.getCustomWareHouse();
					List<Remark> remarkList = DataStatisticService.this.getDmpDAO.getAllRemark();
					final Map<String, Map<String, String>> remarkMap = DataStatisticService.this.exportExcelService.getInwarhouseRemarks(remarkList);
					final List<Reason> reasonList = DataStatisticService.this.getDmpDAO.getAllReason();
					final List<String> cwbList = new ArrayList<String>();

					DataStatisticService.this.jdbcTemplate.query(new StreamingStatementCreator(sql), new ResultSetExtractor<Object>() {
						private int count = 0;

						public void processRow(ResultSet rs) throws SQLException {

							if (cwbList.indexOf(rs.getString("cwb")) > -1) {
								return;
							}
							cwbList.add(rs.getString("cwb"));
							Row row = sheet.createRow(this.count + 1);
							row.setHeightInPoints(15);
							for (int i = 0; i < cloumnName4.length; i++) {
								Cell cell = row.createCell((short) i);
								cell.setCellStyle(style);
								Object a = DataStatisticService.this.exportExcelService.setObjectA(cloumnName5, rs, i, uList, cMap, bList, commonList, cWList, remarkMap, reasonList);
								if (cloumnName6[i].equals("double")) {
									cell.setCellValue(a == null ? BigDecimal.ZERO.doubleValue() : a.equals("") ? BigDecimal.ZERO.doubleValue() : Double.parseDouble(a.toString()));
								} else {
									cell.setCellValue(a == null ? "" : a.toString());
								}
							}
							this.count++;
						}

						@Override
						public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
							while (rs.next()) {
								if (!DataStatisticService.this.downloadManagerService.checkMap(mapid)) {
									return DataStatisticService.this.downloadManagerService.checkMap(mapid);
								}
								this.processRow(rs);
							}
							DataStatisticService.this.downloadManagerDAO.updateStateById(1, mapid, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));// 完成下载
							return null;
						}

					});
				}
			};
			excelUtil.excel(cloumnName4, sheetName, fileName, fileUrl, this.downloadManagerService.checkMap(mapid));
			this.logger.info("导出完成:" + fileUrl + fileName);
			this.downloadManagerService.down_task();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void tiHuoExportExcel(final String sql, final String[] cloumnName4, final String[] cloumnName5, final String[] cloumnName6, final long mapid, String sheetName, String fileName,
			String fileUrl) {

		try {
			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					final Map<Long, Customer> cMap = DataStatisticService.this.getDmpDAO.getAllCustomersToMap();
					final List<String> cwbList = new ArrayList<String>();

					DataStatisticService.this.jdbcTemplate.query(new StreamingStatementCreator(sql), new ResultSetExtractor<Object>() {
						private int count = 0;

						public void processRow(ResultSet rs) throws SQLException {
							if (cwbList.indexOf(rs.getString("cwb")) > -1) {
								return;
							}
							cwbList.add(rs.getString("cwb"));

							Row row = sheet.createRow(this.count + 1);
							row.setHeightInPoints(15);
							for (int i = 0; i < cloumnName4.length; i++) {
								Cell cell = row.createCell((short) i);
								cell.setCellStyle(style);
								Object a = DataStatisticService.this.exportExcelService.setTiHuoObject(cloumnName5, rs, i, cMap);
								cell.setCellValue(a == null ? "" : a.toString());
							}
							this.count++;
						}

						@Override
						public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
							while (rs.next()) {
								if (!DataStatisticService.this.downloadManagerService.checkMap(mapid)) {
									return DataStatisticService.this.downloadManagerService.checkMap(mapid);
								}
								this.processRow(rs);
							}
							DataStatisticService.this.downloadManagerDAO.updateStateById(1, mapid, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));// 完成下载
							return null;
						}

					});
				}
			};
			excelUtil.excel(cloumnName4, sheetName, fileName, fileUrl, this.downloadManagerService.checkMap(mapid));
			this.logger.info("导出完成:" + fileUrl + fileName);
			this.downloadManagerService.down_task();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String setJson(String begindate, String enddate, long isaudit, long isauditTime, String customerids, String cwbordertypeids, long paywayid, String dispatchbranchids, long deliverid,
			String operationOrderResultTypes, long page, String mouldfieldids, long userid, Integer paybackfeeIsZero) {
		JSONObject json = new JSONObject();
		json.put("begindate", begindate);
		json.put("enddate", enddate);
		json.put("isaudit", isaudit);
		json.put("isauditTime", isauditTime);
		json.put("customerids", customerids);
		json.put("cwbordertypeids", cwbordertypeids);
		json.put("paywayid", paywayid);
		json.put("dispatchbranchids", dispatchbranchids);
		json.put("deliverid", deliverid);
		json.put("operationOrderResultTypes", operationOrderResultTypes);
		json.put("page", page);
		json.put("mouldfieldids", mouldfieldids);
		json.put("userid", userid);
		json.put("paybackfeeIsZero", paybackfeeIsZero);
		return json.toString();
	}

	private String setOutWarehouseDataJson(String begindate, String enddate, String kufangid, String customerid, String nextbranchid, String cwbordertypeid, long page, String mouldfieldids,
			long userid) {
		JSONObject json = new JSONObject();
		json.put("begindate", begindate);
		json.put("enddate", enddate);
		json.put("kufangid", kufangid);
		json.put("customerid", customerid);
		json.put("nextbranchid", nextbranchid);
		json.put("cwbordertypeid", cwbordertypeid);
		json.put("page", page);
		json.put("mouldfieldids", mouldfieldids);
		json.put("userid", userid);

		return json.toString();
	}

	// 封装库对库出库的查询条件
	private String setKDKOutWarehouseDataJson(String begindate, String enddate, String kufangid, String customerid, String nextbranchid, String cwbordertypeid, long page, String mouldfieldids,
			long userid) {
		JSONObject json = new JSONObject();
		json.put("begindate", begindate);
		json.put("enddate", enddate);
		json.put("kufangid", kufangid);
		json.put("customerid", customerid);
		json.put("nextbranchid", nextbranchid);
		json.put("cwbordertypeid", cwbordertypeid);
		json.put("page", page);
		json.put("mouldfieldids", mouldfieldids);
		json.put("userid", userid);

		return json.toString();
	}

	private String setDaoHuoDataJson(String begindate, String enddate, long kufangid, long customerid, String currentBranchid, String cwbordertypeid, long isnowdata, long page, String mouldfieldids,
			long userid) {
		JSONObject json = new JSONObject();
		json.put("begindate", begindate);
		json.put("enddate", enddate);
		json.put("kufangid", kufangid);
		json.put("customerid", customerid);
		json.put("currentBranchid", currentBranchid);
		json.put("cwbordertypeid", cwbordertypeid);
		json.put("isnowdata", isnowdata);
		json.put("page", page);
		json.put("mouldfieldids", mouldfieldids);
		json.put("userid", userid);

		return json.toString();
	}

	private String setDaoHuoHuiZongJson(String begindate, String enddate, long branchid, String mouldfieldids, long userid) {
		JSONObject json = new JSONObject();
		json.put("begindate", begindate);
		json.put("enddate", enddate);
		json.put("branchid", branchid);
		json.put("mouldfieldids", mouldfieldids);
		json.put("userid", userid);

		return json.toString();
	}

	private String setZhongZhuanTongJiJson(String begindate, String enddate, long branchid, String type, String branchid2, String mouldfieldids, long userid) {
		JSONObject json = new JSONObject();
		json.put("begindate", begindate);
		json.put("enddate", enddate);
		json.put("branchid", branchid);
		json.put("type", type);
		json.put("branchid2", branchid2);
		json.put("mouldfieldids", mouldfieldids);
		json.put("userid", userid);

		return json.toString();
	}

	private String setZhiLiuJson(String begindate, String enddate, long isaudit, long isauditTime, String customerids, String cwbordertypeids, String dispatchbranchids, long deliverid, long page,
			String mouldfieldids, long userid) {

		JSONObject json = new JSONObject();
		json.put("begindate", begindate);
		json.put("enddate", enddate);
		json.put("isaudit", isaudit);
		json.put("isauditTime", isauditTime);
		json.put("customerids", customerids);
		json.put("cwbordertypeids", cwbordertypeids);
		json.put("dispatchbranchids", dispatchbranchids);
		json.put("deliverid", deliverid);
		// json.put("operationOrderResultTypes", operationOrderResultTypes);
		json.put("page", page);
		json.put("mouldfieldids", mouldfieldids);
		json.put("userid", userid);
		return json.toString();
	}

	private String setJuShouJson(String begindate, String enddate, long isaudit, long isauditTime, String customerids, String cwbordertypeids, String dispatchbranchids, long deliverid,
			String operationOrderResultTypes, long page, String mouldfieldids, long userid) {
		JSONObject json = new JSONObject();
		json.put("begindate", begindate);
		json.put("enddate", enddate);
		json.put("isaudit", isaudit);
		json.put("isauditTime", isauditTime);
		json.put("customerids", customerids);
		json.put("cwbordertypeids", cwbordertypeids);
		json.put("dispatchbranchids", dispatchbranchids);
		json.put("deliverid", deliverid);
		json.put("operationOrderResultTypes", operationOrderResultTypes);
		json.put("page", page);
		json.put("mouldfieldids", mouldfieldids);
		json.put("userid", userid);
		return json.toString();
	}

	private String setKuFangChuKuHuiZongJson(String begindate, String enddate, String customerid, String kufangid, String nextbranchid, long userid) {
		JSONObject json = new JSONObject();
		json.put("begindate", begindate);
		json.put("enddate", enddate);
		json.put("customerid", customerid);
		json.put("kufangid", kufangid);
		json.put("nextbranchid", nextbranchid);
		json.put("userid", userid);
		return json.toString();
	}

	private String setTiHuoJson(String begindate, String enddate, String customerids, long page, long userid) {
		JSONObject json = new JSONObject();
		json.put("begindate", begindate);
		json.put("enddate", enddate);
		json.put("customerids", customerids);
		json.put("page", page);
		json.put("userid", userid);
		return json.toString();
	}

	private String setDanliang(long kufangid, String enddate, long customerids, long page, String mouldfieldids, long userid, String select) {
		JSONObject json = new JSONObject();
		json.put("begindate", enddate + " 00:00:00");
		json.put("enddate", enddate + " 23:59:59");
		json.put("kufangid", kufangid);
		json.put("customerids", customerids);
		json.put("page", page);
		json.put("mouldfieldids", mouldfieldids);
		json.put("userid", userid);
		json.put("select", select);
		return json.toString();
	}

	private String setTuiHuoChuZhanJson(String begindate, String enddate, String customerids, String branchids, long page, long istuihuozhanruku, String mouldfieldids, long userid) {
		JSONObject json = new JSONObject();
		json.put("begindate", begindate);
		json.put("enddate", enddate);
		json.put("customerids", customerids);
		json.put("branchids", branchids);
		json.put("page", page);
		json.put("istuihuozhanruku", istuihuozhanruku);
		json.put("mouldfieldids", mouldfieldids);
		json.put("userid", userid);
		return json.toString();
	}

	private String setZongHeJson(CwbOrderTail tail, long page, String mouldfieldids, long userid) {
		JSONObject json = new JSONObject();
		json.put("begindate", tail.getBegintime());
		json.put("enddate", tail.getEndtime());
		System.out.println(tail.getCurquerytimecolumn());
		json.put("curquerytimecolumn", FlowOrderColumnMap.ORDER_FLOW_TAIL_MAP.get(tail.getCurquerytimecolumn()).replace("credate_", ""));
		json.put("cwb", tail.getCwb());
		json.put("isaudit", tail.getGobackstate());
		json.put("dispatchbranchids", this.getStrings(tail.getDispatchbranchids()));
		json.put("curdispatchbranchids", this.getStrings(tail.getCurdispatchbranchids()));
		json.put("nextdispatchbranchids", this.getStrings(tail.getNextdispatchbranchids()));
		json.put("deliverystateStr", this.getStrings(tail.getOperationOrderResultTypes()));
		json.put("customerid", this.getStrings(tail.getCustomerids()));
		json.put("paytype", tail.getPaywayid());
		json.put("curpaytype", tail.getNewpaywayid());
		json.put("cwbordertypeid", this.getStrings(tail.getCwbordertypeids()));
		json.put("flowordertype", tail.getFlowordertype());
		json.put("page", page);
		json.put("mouldfieldids", mouldfieldids);
		json.put("userid", userid);
		return json.toString();
	}

	private DownloadManager setDownloadManager(String datajson, String filename, String cnfilename, String fileurl, int modelid, long userid) {
		DownloadManager down = new DownloadManager();
		down.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		down.setDatajson(datajson);
		down.setFilename(filename);
		down.setCnfilename(cnfilename);
		down.setFileurl(fileurl);
		down.setModelid(modelid);
		down.setState(-1);
		down.setTimeout(20);
		down.setUserid(userid);
		return down;
	}

	public List<CwbOrder> getTuiHuoChuZhanCwbOrderViewCount10(List<CwbOrder> orderlist, List<TuiHuoChuZhanOrder> tlist, List<Customer> customerlist, List<Branch> branchList) {
		List<CwbOrder> cList = new ArrayList<CwbOrder>();
		if ((tlist.size() > 0) && (orderlist.size() > 0)) {
			for (TuiHuoChuZhanOrder to : tlist) {
				for (CwbOrder cwbOrder : orderlist) {
					if (to.getCwb().equals(cwbOrder.getCwb())) {
						CwbOrder order = cwbOrder;
						order.setCustomername(this.getQueryCustomerName(customerlist, cwbOrder.getCustomerid()));
						order.setStartbranchname(this.getQueryBranchName(branchList, to.getTuihuobranchid()));
						cList.add(order);
						break;
					}
				}
			}

		}
		return cList;
	}

	public List<CwbOrder> getZhiLiuCwbOrderViewCount10(List<CwbOrder> orderlist, List<DeliveryZhiLiu> dzlList, List<Customer> customerlist, List<Branch> branchList, List<Reason> reasonList) {
		List<CwbOrder> cList = new ArrayList<CwbOrder>();
		// 10个字段 cwb 供货商 发货时间 应收金额 订单类型 配送站点 出库时间 滞留原因 再次配送时间 审核状态
		if ((dzlList.size() > 0) && (orderlist.size() > 0)) {
			for (DeliveryZhiLiu dz : dzlList) {
				for (CwbOrder cwbOrder : orderlist) {
					if (dz.getCwb().equals(cwbOrder.getCwb())) {
						CwbOrder order = cwbOrder;
						order.setCustomername(this.getQueryCustomerName(customerlist, cwbOrder.getCustomerid()));
						order.setDeliverybranch(this.getQueryBranchName(branchList, dz.getBranchid()));
						order.setLeavedreasonStr(this.getQueryReason(reasonList, cwbOrder.getLeavedreasonid()));
						order.setAuditstate(dz.getGcaid());
						cList.add(order);
						break;
					}
				}
			}

		}
		return cList;
	}

	public String getQueryReason(List<Reason> reasonList, long leavedreasonid) {
		String reasonString = "";
		for (Reason reason : reasonList) {
			if (reason.getReasonid() == leavedreasonid) {
				reasonString = reason.getReasoncontent();
				break;
			}
		}
		return reasonString;
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

	/**
	 * 库房出库汇总功能--根据供货商和下一站得到相应的map
	 *
	 * @param customerList
	 * @param branchList
	 * @param kufangid
	 * @param begindate
	 * @param enddate
	 * @param nextbranchid
	 * @return
	 */
	public void creChukuDataResultList(List<Customer> customerList, String kufangids, String begindate, String enddate, String nextbranchids, long downid, long userid) {
		if (customerList.size() > 0) {
			if (kufangids.length() > 0) {
				for (int i = 0; i < kufangids.split(",").length; i++) {
					long kufangid = kufangids.split(",")[i].length() == 0 ? 0 : Long.parseLong(kufangids.split(",")[i]);
					for (Customer c : customerList) {
						List<DeliveryChuku> chukuGroupByCustomeridlist = this.deliveryChukuDAO.getDeliveryChukuByoutstoreroomtimeAndstartbranchid(kufangid, begindate, enddate, c.getCustomerid(),
								nextbranchids);
						JSONArray objArr = new JSONArray();

						for (DeliveryChuku dc : chukuGroupByCustomeridlist) {
							JSONObject obj = new JSONObject();
							obj.put("bid", dc.getNextbranchid());
							obj.put("num", dc.getId());
							objArr.add(obj);
						}

						ChukuDataResult cdr = new ChukuDataResult();
						cdr.setBegindate(begindate);
						cdr.setCustomerid(c.getCustomerid());
						cdr.setEnddate(enddate);
						cdr.setExportid(downid);
						cdr.setKufangid(kufangid);
						cdr.setResult(objArr.toString());
						this.chukuDataResultDAO.creChukudataResult(cdr);

					}
				}
			}
		}
	}

	public Map<String, Long> getBranchAndCustomerMap(List<ChukuDataResult> chukuDataResultList) {

		Map<String, Long> branchMap = new HashMap<String, Long>();
		if (chukuDataResultList.size() > 0) {
			for (ChukuDataResult cdr : chukuDataResultList) {
				JSONArray objArr = JSONArray.fromObject(cdr.getResult());

				for (int i = 0; i < objArr.size(); i++) {
					JSONObject obj = objArr.getJSONObject(i);
					long temp = branchMap.get(obj.getLong("bid") + "_" + cdr.getCustomerid()) == null ? 0 : branchMap.get(obj.getLong("bid") + "_" + cdr.getCustomerid());
					branchMap.put(obj.getLong("bid") + "_" + cdr.getCustomerid(), obj.getLong("num") + temp);
				}
			}
		}

		return branchMap;
	}

	public Map<Long, Long> getcustomerAllMap(List<ChukuDataResult> chukuDataResultList) {
		Map<Long, Long> customerMap = new HashMap<Long, Long>();
		if (chukuDataResultList.size() > 0) {

			for (ChukuDataResult cdr : chukuDataResultList) {
				JSONArray objArr = JSONArray.fromObject(cdr.getResult());
				long count = 0;
				for (int i = 0; i < objArr.size(); i++) {
					JSONObject obj = objArr.getJSONObject(i);
					count += obj.getLong("num");
				}
				if (customerMap.get(cdr.getCustomerid()) == null) {
					customerMap.put(cdr.getCustomerid(), 0l);
				}
				customerMap.put(cdr.getCustomerid(), customerMap.get(cdr.getCustomerid()) + count);
			}
		}
		return customerMap;
	}

	public Map<Long, Long> getbranchAllMap(List<ChukuDataResult> chukuDataResultList) {
		Map<Long, Long> branchMap = new HashMap<Long, Long>();
		if (chukuDataResultList.size() > 0) {

			for (ChukuDataResult cdr : chukuDataResultList) {
				JSONArray objArr = JSONArray.fromObject(cdr.getResult());
				long count = 0;
				for (int i = 0; i < objArr.size(); i++) {
					JSONObject obj = objArr.getJSONObject(i);
					if (branchMap.get(obj.getLong("bid")) == null) {
						branchMap.put(obj.getLong("bid"), 0l);
					}
					count = branchMap.get(obj.getLong("bid")) + obj.getLong("num");
					branchMap.put(obj.getLong("bid"), count);
				}
			}
		}

		return branchMap;
	}

	public List<CwbOrder> getChukuCollectDataCwbOrderViewCount10(List<CwbOrder> clist, List<DeliveryChuku> delList, List<Customer> customerList, List<Branch> branchList,
			List<CustomWareHouse> customerWareHouseList) {
		List<CwbOrder> cwbOrderViewList = new ArrayList<CwbOrder>();
		if ((delList.size() > 0) && (clist.size() > 0)) {
			for (DeliveryChuku delChuku : delList) {
				for (CwbOrder cwborder : clist) {
					if (cwborder.getCwb().equals(delChuku.getCwb())) {
						CwbOrder cwbOrderView = cwborder;
						cwbOrderView.setCustomername(this.getQueryCustomerName(customerList, delChuku.getCustomerid()));// 供货商的名称
						cwbOrderView.setNextbranchname(this.getQueryBranchName(branchList, delChuku.getNextbranchid()));// 下一站
						cwbOrderView.setOutstoreroomtime(delChuku.getOutstoreroomtime());// 出库时间
						cwbOrderView.setCustomerwarehousename(this.getQueryCustomWareHouse(customerWareHouseList,
								Long.parseLong(cwborder.getCustomerwarehouseid() == "" ? "0" : cwborder.getCustomerwarehouseid())));// 发货仓库
						cwbOrderViewList.add(cwbOrderView);
						break;
					}
				}
			}
		}
		return cwbOrderViewList;
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

	/**
	 * 退货站入库 统计
	 *
	 * @param orderlist
	 * @param tuihuoRecordList
	 * @param customerList
	 * @param branchList
	 * @return
	 */
	public List<CwbOrder> getTuiHuoZhanRuKuIndex(List<CwbOrder> orderlist, List<TuiHuoZhanRuKuOrder> tuihuoRecordList, List<Customer> customerList, List<Branch> branchList) {
		List<CwbOrder> cwbOrderViewList = new ArrayList<CwbOrder>();
		if ((tuihuoRecordList != null) && (tuihuoRecordList.size() > 0)) {

			for (TuiHuoZhanRuKuOrder to : tuihuoRecordList) {
				for (CwbOrder cwbOrder : orderlist) {
					if (to.getCwb().equals(cwbOrder.getCwb())) {
						CwbOrder co = cwbOrder;
						co.setCustomername(this.getQueryCustomerName(customerList, co.getCustomerid()));
						co.setStartbranchname(this.getQueryBranchName(branchList, to.getTuihuobranchid()));
						co.setTuihuozhaninstoreroomtime(to.getRukutime());
						cwbOrderViewList.add(co);
					}
				}
			}
		}
		return cwbOrderViewList;
	}

	/**
	 * 库房在途统计汇总
	 *
	 * @param zaiTuOrders
	 * @param orderlist
	 * @param customerList
	 * @param customerWareHouseList
	 * @param branchList
	 * @param userList
	 * @param reasonList
	 * @param remarkList
	 * @return
	 */
	public List<CwbOrder> getKuFangZaiTuHuiZongViewIndex(List<KuFangZaiTuOrder> zaiTuOrders, List<CwbOrder> orderlist, List<Customer> customerList, List<CustomWareHouse> customerWareHouseList,
			List<Branch> branchList, List<User> userList, List<Reason> reasonList, List<Remark> remarkList) {
		List<CwbOrder> cwbOrderViewList = new ArrayList<CwbOrder>();
		if ((zaiTuOrders.size() > 0) && (orderlist.size() > 0)) {
			for (KuFangZaiTuOrder kf : zaiTuOrders) {
				for (CwbOrder cwborder : orderlist) {
					if (cwborder.getCwb().equals(kf.getCwb())) {
						CwbOrder cwbOrderView = cwborder;
						cwbOrderView.setCustomername(this.getQueryCustomerName(customerList, kf.getCustomerid()));
						cwbOrderView.setOutstoreroomtime(kf.getOutwarehousetime());
						cwbOrderView.setDeliverybranch(this.getQueryBranchName(branchList, kf.getDeliverybranchid()));
						cwbOrderViewList.add(cwbOrderView);
						break;
					}
				}
			}
		}
		return cwbOrderViewList;
	}

	public List<CwbOrder> getKuFangRuKuHuiZongViewIndex(List<KuFangRuKuOrder> kuFangRuKuOrders, List<CwbOrder> orderlist, List<Customer> customerList, List<Branch> branchs, List<User> userList) {
		List<CwbOrder> cwbOrders = new ArrayList<CwbOrder>();
		if ((kuFangRuKuOrders != null) && (kuFangRuKuOrders.size() > 0)) {
			for (KuFangRuKuOrder kf : kuFangRuKuOrders) {
				for (CwbOrder co : orderlist) {
					if (co.getCwb().equals(kf.getCwb())) {
						CwbOrder cwbOrder = co;
						cwbOrder.setCustomername(this.getQueryCustomerName(customerList, co.getCustomerid()));
						cwbOrder.setDeliverybranch(this.getQueryBranchName(branchs, co.getDeliverybranchid()));
						cwbOrder.setInstoreroomtime(kf.getIntowarehousetime()); // 入库时间
						cwbOrder.setOperatorName(this.getQueryUserName(userList, kf.getIntowarehouseuserid()));// 入库操作人
						cwbOrders.add(cwbOrder);

					}
				}
			}
		}

		return cwbOrders;
	}

	public List<CwbOrder> getKeHuFaHuoTongJiCwbOrderView(List<CwbOrder> clist, List<Customer> customerList, List<Branch> branchs) {
		List<CwbOrder> cwbOrders = new ArrayList<CwbOrder>();
		if ((clist != null) && (clist.size() > 0)) {
			for (CwbOrder cwbOrder : clist) {
				CwbOrder order = cwbOrder;
				order.setCustomername(this.getQueryCustomerName(customerList, order.getCustomerid()));
				order.setDeliverybranch(this.getQueryBranchName(branchs, order.getDeliverybranchid()));
				cwbOrders.add(order);
			}
		}
		return cwbOrders;
	}

	private String checkZongHeChaXun(HttpServletResponse response, HttpServletRequest request, long page, long userid) {
		JSONObject json = new JSONObject();
		String mouldfieldids = request.getParameter("exportmould2"); // 导出模板
		try {
			// 查询出数据

			// 验证导出条件是否存在

			CwbOrderTail tail = new CwbOrderTail();

			tail.setCurquerytimecolumn(request.getParameter("curquerytimetype"));

			tail.setBegintime(request.getParameter("begindate"));

			tail.setEndtime(request.getParameter("enddate"));

			tail.setDispatchbranchids(request.getParameterValues("dispatchbranchid") == null ? new String[] {} : request.getParameterValues("dispatchbranchid"));

			tail.setCurdispatchbranchids(request.getParameterValues("curdispatchbranchid") == null ? new String[] {} : request.getParameterValues("curdispatchbranchid"));

			tail.setNextdispatchbranchids(request.getParameterValues("nextdispatchbranchid") == null ? new String[] {} : request.getParameterValues("nextdispatchbranchid"));

			tail.setCustomerids(request.getParameterValues("customerid") == null ? new String[] {} : request.getParameterValues("customerid"));

			tail.setPaywayid(request.getParameter("paytype"));

			tail.setNewpaywayid(request.getParameter("curpaytype"));

			tail.setCwbordertypeids(request.getParameterValues("cwbordertypeid") == null ? new String[] {} : request.getParameterValues("cwbordertypeid"));

			tail.setOperationOrderResultTypes(request.getParameterValues("operationOrderResultType") == null ? new String[] {} : request.getParameterValues("operationOrderResultType"));

			tail.setFlowordertype(request.getParameter("cwbstate") == null ? -1 : Long.parseLong(request.getParameter("cwbstate").toString()));

			tail.setGobackstate(request.getParameter("isaudit"));

			String datajson = this.setZongHeJson(tail, page, mouldfieldids, userid);

			DownloadManager down = this.downloadManagerDAO.getDownloadManagerByJson(datajson);

			if (down != null) {
				json.put("errorCode", 2);
				json.put("remark", "该导出操作已经在下载列表中！");
				return json.toString();
			} else {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
				String fileName = "Order_Tail_" + df.format(new Date()) + "_"; // 文件名
				String otherName = "";
				String lastStr = ".xlsx";// 文件名后缀
				long count = Long.parseLong(request.getParameter("count") == null ? "0" : request.getParameter("count").toString());
				if (count > 0) {
					if (((count / Page.EXCEL_PAGE_NUMBER) + ((count % Page.EXCEL_PAGE_NUMBER) > 0 ? 1 : 0)) == 1) {
						otherName = "1-" + count;
					} else {
						otherName = ((((page * Page.EXCEL_PAGE_NUMBER) + 1) > count) ? count : ((page * Page.EXCEL_PAGE_NUMBER) + 1)) + "_"
								+ (((page + 1) * Page.EXCEL_PAGE_NUMBER) > count ? count : (page + 1) * Page.EXCEL_PAGE_NUMBER);
					}
				}
				fileName = fileName + otherName + lastStr;
				String cnfilename = "综合查询_" + df.format(new Date()) + "_" + otherName + lastStr;
				String fileUrl = this.getDmpDAO.getFileUrl() + "download" + System.getProperty("file.separator") + new SimpleDateFormat("yyyy-MM-dd").format(new Date())
						+ System.getProperty("file.separator");
				down = this.setDownloadManager(datajson, fileName, cnfilename, fileUrl, ModelEnum.ZongHeChaXun.getValue(), userid);
				this.downloadManagerDAO.creDownloadManager(down);
				json.put("errorCode", 0);
				json.put("remark", "已进入离线导出");
				return json.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("errorCode", 2);
			json.put("remark", "系统导出异常！");
			return json.toString();
		}
	}

	// 库房出库订单统计
	private void ZongHeChaXunExcel(DownloadManager down) {
		String sheetName = "综合查询信息"; // sheet的名称
		String fileName = down.getFilename(); // 文件名
		String fileUrl = down.getFileurl();// 文件路径
		JSONObject json = JSONObject.fromObject(down.getDatajson());

		String begindate = json.getString("begindate");
		String enddate = json.getString("enddate");
		String curquerytimecolumn = json.getString("curquerytimecolumn");
		String isaudit = json.getString("isaudit");
		String dispatchbranchids = json.getString("dispatchbranchids");
		String curdispatchbranchids = json.getString("curdispatchbranchids");
		String nextdispatchbranchids = json.getString("nextdispatchbranchids");
		String deliverystateStr = json.getString("deliverystateStr");
		String paytype = json.getString("paytype");
		String curpaytype = json.getString("curpaytype");
		String customerid = json.getString("customerid");
		String cwbordertypeid = json.getString("cwbordertypeid");
		String flowordertype = json.getString("flowordertype");
		String userid = json.getString("userid");

		CwbOrderTail tail = new CwbOrderTail();

		tail.setBegintime(begindate);
		tail.setEndtime(enddate);
		tail.setCurquerytimecolumn(curquerytimecolumn);
		tail.setGobackstate(isaudit);
		tail.setBranchid(curdispatchbranchids);
		tail.setDeliverybranchid(dispatchbranchids);
		tail.setNextbranchid(nextdispatchbranchids);
		tail.setPaywayid(paytype);
		tail.setNewpaywayid(curpaytype);
		tail.setCustomerstr(customerid);
		tail.setCwbordertypestr(cwbordertypeid);
		tail.setFlowordertype(Long.parseLong(flowordertype));
		tail.setDeliverystateStr(deliverystateStr);
		long page = json.getLong("page");
		String mouldfieldids2 = json.getString("mouldfieldids"); // 导出模板
		// tail.

		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		if ((mouldfieldids2 != null) && !"0".equals(mouldfieldids2)) { // 选择模板
			List<SetExportField> listSetExportField = this.getDmpDAO.getSetExportFieldByExportstate(mouldfieldids2);
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		} else {
			List<SetExportField> listSetExportField = this.getDmpDAO.getSetExportFieldByExportstate("0");
			cloumnName1 = new String[listSetExportField.size()];
			cloumnName2 = new String[listSetExportField.size()];
			cloumnName3 = new String[listSetExportField.size()];
			for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
				cloumnName1[k] = listSetExportField.get(j).getFieldname();
				cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
				cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
			}
		}
		final String[] cloumnName4 = cloumnName1;
		final String[] cloumnName5 = cloumnName2;
		final String[] cloumnName6 = cloumnName3;
		final String sql = this.cwbDAO.getSQLExportZongHeChaXun(tail, page);
		// downloadManagerService.saveXiazaitoMap(down.getId(), 0);//正在下载
		final long mapid = down.getId();
		this.exportExcel(sql, cloumnName4, cloumnName5, cloumnName6, mapid, sheetName, fileName, fileUrl);

	}

	/**
	 * 检测分站到货汇总的下载参数是否进入下载模式
	 *
	 * @param response
	 * @param request
	 * @param userid
	 * @return
	 */
	private String checkFenZhanDaoHuoHuiZong(HttpServletResponse response, HttpServletRequest request, long userid) {
		JSONObject json = new JSONObject();
		try {
			// 查询出数据
			String[] customerids = request.getParameterValues("customerid1") == null ? new String[] {} : request.getParameterValues("customerid1");
			String begindate = request.getParameter("begindate1") == null ? "" : request.getParameter("begindate1").toString();
			String enddate = request.getParameter("enddate1") == null ? "" : request.getParameter("enddate1").toString();
			Integer isnowdata = request.getParameter("isnowdata") == null ? -1 : Integer.parseInt(request.getParameter("isnowdata"));
			String[] kufangid = request.getParameterValues("kufangid1") == null ? new String[] {} : request.getParameterValues("kufangid1");
			String[] currentbranchid = request.getParameterValues("currentBranchid1") == null ? new String[] {} : request.getParameterValues("currentBranchid1");
			String[] cwbordertypeid = request.getParameterValues("cwbordertypeid1") == null ? new String[] {} : request.getParameterValues("cwbordertypeid1");

			String customers = "";
			if (customerids.length > 0) {
				customers = this.getStrings(customerids);
			}
			String currentbranchids = "";
			if (currentbranchid.length > 0) {
				currentbranchids = this.getStrings(currentbranchid);
			}
			String cwbordertypeids = "";
			if (cwbordertypeid.length > 0) {
				cwbordertypeids = this.getStrings(cwbordertypeid);
			}
			String kufangids = "";
			if (kufangid.length > 0) {
				kufangids = this.getStrings(kufangid);
			}

			// 验证导出条件是否存在
			String datajson = this.setFenZhanDaoHuoHuiZongJson(begindate, enddate, customers, cwbordertypeids, currentbranchids, kufangids, userid, isnowdata);

			DownloadManager down = this.downloadManagerDAO.getDownloadManagerByJson(datajson);

			if (down != null) {
				json.put("errorCode", 2);
				json.put("remark", "该导出操作已经在下载列表中！");
				return json.toString();
			} else {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
				String fileName = "Order_" + df.format(new Date()) + "_"; // 文件名
				String otherName = "报表";
				String lastStr = ".xlsx";// 文件名后缀
				fileName = fileName + otherName + lastStr;
				String cnfilename = "分站到货汇总_" + df.format(new Date()) + "_" + otherName + lastStr;
				String fileUrl = this.getDmpDAO.getFileUrl() + "download" + System.getProperty("file.separator") + new SimpleDateFormat("yyyy-MM-dd").format(new Date())
						+ System.getProperty("file.separator");
				down = this.setDownloadManager(datajson, fileName, cnfilename, fileUrl, ModelEnum.FenZhanDaoHuoHuiZong.getValue(), userid);
				this.downloadManagerDAO.creDownloadManager(down);
				json.put("errorCode", 0);
				json.put("remark", "已进入离线导出");
				return json.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("errorCode", 2);
			json.put("remark", "系统导出异常！");
			return json.toString();
		}
	}

	/**
	 * 分站到货统计（云）生成excel
	 *
	 * @author xiaobao
	 * @param down
	 */
	private void FenZhanDaoHuoHuiZong(DownloadManager down) {
		String fileName = down.getFilename(); // 文件名
		String fileUrl = down.getFileurl();// 文件路径
		JSONObject json = JSONObject.fromObject(down.getDatajson());
		String begindate = json.getString("begindate");
		String enddate = json.getString("enddate");
		String cwbordertypeids = json.getString("cwbordertypeids");
		String currentbranchid = json.getString("currentbranchid");
		String customers = json.getString("customers");
		String kufangid = json.getString("kufangid");
		Integer isshow = json.getInt("isshow");
		// 站点 供货商 数量
		Map<Long, Map<Long, Long>> huiZongMap = this.cwbDAO.getFenZhanDaoHuoHuiZongMap(begindate, enddate, customers, cwbordertypeids, currentbranchid, kufangid, isshow);
		SXSSFWorkbook wb = new SXSSFWorkbook(); // excel文件,一个excel文件包含多个表
		Sheet sheet = wb.createSheet(); // 表，一个表包含多个行
		String filename = "分站到货汇总--报表" + DateTimeUtil.getNowDate() + ".xlsx";
		wb.setSheetName(0, filename);
		// 设置字体等样式
		Font font = wb.createFont();
		font.setFontName("Courier New");
		CellStyle style = wb.createCellStyle();
		style.setFont(font);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// 单元格
		Row row = sheet.createRow(0); // 由HSSFSheet生成行
		row.setHeightInPoints(15); // 设置行高
		String[] firstLine = new String[3];
		firstLine[0] = "站     点";
		firstLine[1] = "供货商";
		firstLine[2] = "合    计";
		List<Long> fenzhanidSet = new ArrayList<Long>(huiZongMap.keySet());
		List<Customer> customerList = new ArrayList<Customer>();
		List<Branch> branchList = this.getDmpDAO.getAllBranchs();
		// 供货商列表
		if ((customers.length() > 0) && !customers.trim().equals("0")) {
			customerList = this.getDmpDAO.getCustomerByIds(customers);
		} else {
			customerList = this.getDmpDAO.getAllCustomers();
		}
		if ((fenzhanidSet != null) && (fenzhanidSet.size() > 0)) {
			sheet.addMergedRegion(new CellRangeAddress(0, (short) 0, 1, (short) customerList.size())); // 合并第一行
																										// 供货商
																										// 单元格
			sheet.createFreezePane(1, 0, fenzhanidSet.size() + 1, 0);// 冻结第一列
			// (int firstRow, int lastRow, int firstCol, int lastCol
			sheet.addMergedRegion(new CellRangeAddress(0, (short) 1, 0, (short) 0));// 库房的合并单元格
			sheet.addMergedRegion(new CellRangeAddress(0, (short) 1, customerList.size() + 1, (short) customerList.size() + 1));// 库房的合并单元格
			Cell cell = row.createCell(0);
			cell.setCellStyle(style);
			cell.setCellValue(firstLine[0]);
			Cell cusCell = row.createCell(1);
			cusCell.setCellStyle(style);
			cusCell.setCellValue(firstLine[1]);
			Row secRow = sheet.createRow(1);
			Cell countCell = row.createCell(customerList.size() + 1);
			countCell.setCellStyle(style);
			countCell.setCellValue(firstLine[2]);// 第一行写入结束
			for (int j = 1; j < (customerList.size() + 1); j++) {
				Cell customerCell = secRow.createCell(j);
				customerCell.setCellStyle(style);
				customerCell.setCellValue(customerList.get(j - 1).getCustomername());
			}

			for (int i = 1; i < (fenzhanidSet.size() + 1); i++) {
				Row item = sheet.createRow(i + 1);
				Cell kufangCell = item.createCell(0);
				kufangCell.setCellStyle(style);
				kufangCell.setCellValue(this.getQueryBranchName(branchList, fenzhanidSet.get(i - 1)));
				// 开始循环对应站点的 供货商
				long countForFenZhan = 0l;
				for (int k = 1; k < (customerList.size() + 1); k++) {
					Cell customerCell = item.createCell(k);
					customerCell.setCellStyle(style);
					long num = huiZongMap.get(fenzhanidSet.get(i - 1)).get(customerList.get(k - 1).getCustomerid()) == null ? 0 : huiZongMap.get(fenzhanidSet.get(i - 1)).get(
							customerList.get(k - 1).getCustomerid());
					customerCell.setCellValue(num);
					countForFenZhan += num;
					// keHuFaHuoDataDao.creDate(begindate,enddate,fenzhanidSet.get(i-1),customerList.get(k-1).getCustomerid(),num,down.getId());
				}
				Cell countForKufangCell = item.createCell(customerList.size() + 1);
				countForKufangCell.setCellStyle(style);
				countForKufangCell.setCellValue(countForFenZhan);
			}

			// 下面的合计
			Row countForDown = sheet.createRow(fenzhanidSet.size() + 2);
			Cell firCell = countForDown.createCell(0);
			firCell.setCellStyle(style);
			firCell.setCellValue("合计");
			long total = 0l;
			for (int f = 1; f < (customerList.size() + 1); f++) {
				long countForDownLong = 0l;
				for (int i = 1; i < (fenzhanidSet.size() + 1); i++) {
					long numDown = huiZongMap.get(fenzhanidSet.get(i - 1)).get(customerList.get(f - 1).getCustomerid()) == null ? 0 : huiZongMap.get(fenzhanidSet.get(i - 1)).get(
							customerList.get(f - 1).getCustomerid());
					countForDownLong += numDown;
				}

				Cell customerCell = countForDown.createCell(f);
				customerCell.setCellStyle(style);
				customerCell.setCellValue(countForDownLong);
				total += countForDownLong;
			}
			Cell bigCell = countForDown.createCell(customerList.size() + 1);
			bigCell.setCellStyle(style);
			bigCell.setCellValue(total);

		}
		File f = new File(fileUrl);
		File f2 = new File(fileUrl + fileName);

		try {
			if (!f.exists()) {
				f.mkdirs();
			}
			f2.createNewFile();
			OutputStream os = new FileOutputStream(f2);
			wb.write(os);
			os.close();
			this.downloadManagerDAO.updateStateById(1, down.getId(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			System.out.println("FenZhanDaoHuoHuiZong(DownloadManager down) 文件写入完成");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 获得指定日期的前一天
	 *
	 * @param specifiedDay
	 * @return
	 * @throws Exception
	 */
	public static String getSpecifiedDayBefore(String specifiedDay) {
		// SimpleDateFormat simpleDateFormat = new
		// SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (Exception e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day - 1);

		String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return dayBefore;
	}

	/**
	 * 查询时间大小
	 *
	 * @param dat1
	 * @param dat2
	 * @return 0=，1<
	 */
	public static int dateCompare(Date dat1, Date dat2) {
		int dateComPareFlag = 0;

		dateComPareFlag = dat2.compareTo(dat1);

		return dateComPareFlag;
	}

	/*
	 * public void ExportExcelMethod(long kufangid,long customerid,String
	 * timmer,String type ) {
	 *
	 * String[] cloumnName1 = {}; // 导出的列名 String[] cloumnName2 = {}; // 导出的英文列名
	 * String[] cloumnName3 = {}; // 导出的数据类型
	 *
	 * List<SetExportField> listSetExportField =
	 * exportmouldDAO.getSetExportFieldByStrs("0"); cloumnName1 = new
	 * String[listSetExportField.size()]; cloumnName2 = new
	 * String[listSetExportField.size()]; cloumnName3 = new
	 * String[listSetExportField.size()]; for (int k = 0, j = 0; j <
	 * listSetExportField.size(); j++, k++) { cloumnName1[k] =
	 * listSetExportField.get(j).getFieldname(); cloumnName2[k] =
	 * listSetExportField.get(j).getFieldenglishname(); cloumnName3[k] =
	 * listSetExportField.get(j).getExportdatatype(); } final String[]
	 * cloumnName4 = cloumnName1; final String[] cloumnName5 = cloumnName2;
	 * final String[] cloumnName6 = cloumnName3; String sheetName = "订单地址信息"; //
	 * sheet的名称 SimpleDateFormat df = new
	 * SimpleDateFormat("yyyy-MM-dd_HH-mm-ss"); String fileName = "Order_" +
	 * df.format(new Date()) + "_"; // 文件名 String otherName = "";
	 *
	 * String lastStr = ".xlsx";//文件名后缀
	 *
	 * fileName = fileName + timmer + lastStr; try {
	 *
	 * final String sql=
	 * cwbDAO.getcwbOrderByPageIsMyWarehouseSql(customerid,cwb,
	 * emaildate,CwbOrderAddressCodeEditTypeEnum
	 * .getText(addressCodeEditType),branchid);
	 *
	 * ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
	 *
	 * @Override public void fillData(final Sheet sheet, final CellStyle style)
	 * { final List<User> uList = userDAO.getAllUser(); final List<Branch> bList
	 * = branchDAO.getAllBranches(); jdbcTemplate.query(new
	 * StreamingStatementCreator(sql), new ResultSetExtractor<Object>() {
	 * private int count = 0; ColumnMapRowMapper columnMapRowMapper = new
	 * ColumnMapRowMapper(); private List<Map<String, Object>> recordbatch = new
	 * ArrayList<Map<String, Object>>();
	 *
	 * public void processRow(ResultSet rs) throws SQLException {
	 *
	 *
	 * Map<String, Object> mapRow = columnMapRowMapper.mapRow(rs, count);
	 * recordbatch.add(mapRow); count++; if(count%100==0){ writeBatch(); }
	 *
	 *
	 * }
	 *
	 * private void writeSingle(Map<String, Object> mapRow,TuihuoRecord
	 * tuihuoRecord,DeliveryState ds ,Map<String, String> allTime,int
	 * rownum,Map<String, String> cwbspayupidMap,Map<String, String>
	 * complaintMap) throws SQLException { Row row = sheet.createRow(rownum+1);
	 * row.setHeightInPoints((float) 15); for (int i = 0; i <
	 * cloumnName4.length; i++) { Cell cell = row.createCell((short) i);
	 * cell.setCellStyle(style); // sheet.setColumnWidth(i, (short) (5000)); //
	 * //设置列宽 Object a = exportService.setObjectA(cloumnName5, mapRow, i, uList,
	 * cMap, bList, commonList, tuihuoRecord,ds, allTime, cWList, remarkMap,
	 * reasonList,cwbspayupidMap,complaintMap); if
	 * (cloumnName6[i].equals("double")) { cell.setCellValue(a == null ?
	 * BigDecimal.ZERO.doubleValue() : a.equals("") ?
	 * BigDecimal.ZERO.doubleValue() : Double.parseDouble(a.toString())); } else
	 * { cell.setCellValue(a == null ? "" : a.toString()); } } }
	 *
	 * @Override public Object extractData(ResultSet rs) throws SQLException,
	 * DataAccessException { while (rs.next()) { this.processRow(rs); }
	 * writeBatch(); return null; }
	 *
	 * public void writeBatch() throws SQLException{ if(recordbatch.size()>0){
	 * List<String> cwbs=new ArrayList<String>(); for(Map<String, Object>
	 * mapRow:recordbatch){ cwbs.add(mapRow.get("cwb").toString()); }
	 * Map<String,DeliveryState> deliveryStates=getDeliveryListByCwbs(cwbs);
	 * Map<String, TuihuoRecord> tuihuorecoredMap=getTuihuoRecoredMap(cwbs);
	 * Map<String, String> cwbspayupMsp=getcwbspayupidMap(cwbs); Map<String,
	 * String> complaintMap = getComplaintMap(cwbs); Map<String,Map<String,
	 * String>> orderflowList=dataStatisticsService.
	 * getOrderFlowByCredateForDetailAndExportAllTime(cwbs,bList); int size =
	 * recordbatch.size(); for(int i=0;i<size;i++){ String
	 * cwb=recordbatch.get(i).get("cwb").toString();
	 * writeSingle(recordbatch.get(
	 * i),tuihuorecoredMap.get(cwb),deliveryStates.get
	 * (cwb),orderflowList.get(cwb),count-size+i,cwbspayupMsp,complaintMap); }
	 * recordbatch.clear(); } }
	 *
	 * private Map<String,TuihuoRecord > getTuihuoRecoredMap(List<String> cwbs){
	 * Map<String,TuihuoRecord> map=new HashMap<String, TuihuoRecord>(); for
	 * (TuihuoRecord tuihuoRecord : tuihuoRecordDAO.getTuihuoRecordByCwbs(cwbs))
	 * { map.put(tuihuoRecord.getCwb(), tuihuoRecord); } return map; }
	 *
	 * private Map<String,DeliveryState> getDeliveryListByCwbs(List<String>
	 * cwbs) { Map<String,DeliveryState> map=new HashMap<String,
	 * DeliveryState>(); for(DeliveryState
	 * deliveryState:deliveryStateDAO.getActiveDeliveryStateByCwbs(cwbs)){
	 * map.put(deliveryState.getCwb(), deliveryState); } return map; } private
	 * Map<String,String> getComplaintMap(List<String> cwbs) {
	 * Map<String,String> complaintMap=new HashMap<String, String>();
	 * for(Complaint complaint:complaintDAO.getActiveComplaintByCwbs(cwbs)){
	 * complaintMap.put(complaint.getCwb(), complaint.getContent()); } return
	 * complaintMap; } private Map<String,String> getcwbspayupidMap(List<String>
	 * cwbs) { Map<String,String> cwbspayupidMap=new HashMap<String, String>();
	 * for(DeliveryState
	 * deliveryState:deliveryStateDAO.getActiveDeliveryStateByCwbs(cwbs)){
	 * String ispayup = "否"; GotoClassAuditing goclass =
	 * gotoClassAuditingDAO.getGotoClassAuditingByGcaid
	 * (deliveryState.getGcaid());
	 *
	 * if(goclass!=null&&goclass.getPayupid()!=0){ ispayup = "是"; }
	 * cwbspayupidMap.put(deliveryState.getCwb(), ispayup); } return
	 * cwbspayupidMap; } }); jdbcTemplate.query(new
	 * StreamingStatementCreator(sql), new RowCallbackHandler(){ private int
	 * count=0;
	 *
	 * @Override public void processRow(ResultSet rs) throws SQLException { Row
	 * row = sheet.createRow(count + 1); row.setHeightInPoints((float) 15);
	 *
	 * DeliveryState ds = getDeliveryByCwb(rs.getString("cwb"));
	 * Map<String,String> allTime =
	 * getOrderFlowByCredateForDetailAndExportAllTime(rs.getString("cwb"));
	 *
	 * for (int i = 0; i < cloumnName4.length; i++) { Cell cell =
	 * row.createCell((short) i); cell.setCellStyle(style);
	 * //sheet.setColumnWidth(i, (short) (5000)); //设置列宽 Object a =
	 * exportService.setObjectA(cloumnName5, rs, i ,
	 * uList,cMap,bList,commonList,ds,allTime,cWList,remarkMap,reasonList);
	 * if(cloumnName6[i].equals("double")){ cell.setCellValue(a == null ?
	 * BigDecimal.ZERO.doubleValue() :
	 * a.equals("")?BigDecimal.ZERO.doubleValue()
	 * :Double.parseDouble(a.toString())); }else{ cell.setCellValue(a == null ?
	 * "" : a.toString()); } } count++;
	 *
	 * }});
	 *
	 * } }; excelUtil.excel(response, cloumnName4, sheetName, fileName);
	 *
	 * } catch (Exception e) { e.printStackTrace(); } }
	 */
}
