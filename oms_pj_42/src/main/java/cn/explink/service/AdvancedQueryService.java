package cn.explink.service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;
import org.springframework.stereotype.Service;

import cn.explink.dao.CwbDAO;
import cn.explink.dao.ExportDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.SetExportFieldDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Common;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.Reason;
import cn.explink.domain.Remark;
import cn.explink.domain.SetExportField;
import cn.explink.domain.User;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.ReasonTypeEnum;
import cn.explink.util.ExcelUtilsOld;
import cn.explink.util.StreamingStatementCreator;

@Service
public class AdvancedQueryService {
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	SetExportFieldDAO setExportFieldDAO;
	@Autowired
	SetExportFieldDAO setexportfieldDAO;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	ExportDAO exportDAO;
	@Autowired
	ExportService exportService;
	@Autowired
	GetDmpDAO getDmpDAO;

	@Autowired
	JdbcTemplate jdbcTemplate;

	public boolean checkBranchRepeat(List<Branch> branchlist, Branch branch) {
		for (int i = 0; i < branchlist.size(); i++) {
			if (branch.getBranchname().equals(branchlist.get(i).getBranchname())) {
				return true;
			}
		}
		return false;
	}

	public List<CwbOrder> getCwbOrderView(List<CwbOrder> clist, List<Customer> customerList, List<CustomWareHouse> customerWareHouseList, List<Branch> branchList, List<User> userList,
			List<Reason> reasonList, String begindate, String enddate) {
		List<CwbOrder> cwbOrderViewList = new ArrayList<CwbOrder>();
		if (clist.size() > 0) {
			for (CwbOrder cwbOrderView : clist) {
				cwbOrderView.setCustomername(this.getQueryCustomerName(customerList, cwbOrderView.getCustomerid()));// 供货商的名称
				String customwarehouse = this.getQueryCustomWareHouse(customerWareHouseList, Long.parseLong(cwbOrderView.getCustomerwarehouseid() == "" ? "0" : cwbOrderView.getCustomerwarehouseid()));
				cwbOrderView.setCustomerwarehousename(customwarehouse);
				cwbOrderView.setInhouse(this.getQueryBranchName(branchList, Integer.parseInt(cwbOrderView.getCarwarehouse() == "" ? "0" : cwbOrderView.getCarwarehouse())));// 入库仓库
				// +1
				cwbOrderView.setCurrentbranchname(this.getQueryBranchName(branchList, cwbOrderView.getCurrentbranchid()));// 当前所在机构名称

				cwbOrderView.setStartbranchname(this.getQueryBranchName(branchList, cwbOrderView.getStartbranchid()));// 上一站机构名称
				cwbOrderView.setNextbranchname(this.getQueryBranchName(branchList, cwbOrderView.getNextbranchid()));// 下一站机构名称
				// +1
				cwbOrderView.setDeliverybranch(this.getQueryBranchName(branchList, cwbOrderView.getDeliverybranchid()));// 配送站点
				cwbOrderView.setDelivername(this.getQueryUserName(userList, cwbOrderView.getDeliverid()));
				DeliveryState deliverystate = new DeliveryState();// this.getDeliveryByCwb(cwbOrderView.getCwb());
				cwbOrderView.setPaytype(this.getPayWayType(cwbOrderView.getCwb(), deliverystate));// 支付方式
				// +1
				cwbOrderView.setReturngoodsremark("退货站入库备注");

				cwbOrderView.setLeavedreasonStr(this.getQueryReason(reasonList, cwbOrderView.getLeavedreasonid()));// 滞留原因
				// +1
				cwbOrderView.setPodremarkStr(this.getQueryReason(reasonList, cwbOrderView.getPodremarkid()));// 配送结果备注
				// +1
				cwbOrderView.setInwarhouseremark(this.getInwarhouseRemarks(ReasonTypeEnum.RuKuBeiZhu.getText(), cwbOrderView.getCwb()));
				if (deliverystate != null) {
					cwbOrderView.setSigninman(deliverystate.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue() ? cwbOrderView.getConsigneename() : "");
					// +1
					cwbOrderView.setSignintime("");
					cwbOrderView.setPosremark(deliverystate.getPosremark());
					cwbOrderView.setCheckremark(deliverystate.getCheckremark());
					// +1
					cwbOrderView.setDeliverstateremark("deliverystate.getDeliverstateremark()");
				}
				// +1
				cwbOrderView.setCustomerbrackhouseremark("供货商拒收返库备注");
				cwbOrderViewList.add(cwbOrderView);

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

	public DeliveryState getDeliveryStateByCwb(String cwb) {
		List<DeliveryState> deliveryStateList = new ArrayList<DeliveryState>();
		// deliveryStateList = dmpDao.getDeliveryStateByCwb(cwb);
		DeliveryState deliverState = deliveryStateList.size() > 0 ? deliveryStateList.get(deliveryStateList.size() - 1) : new DeliveryState();
		return deliverState;
	}

	public String getInwarhouseRemarks(String remarktype, String cwb) {
		String remarks = "";
		List<Remark> remarkList = new ArrayList<Remark>(); // remarkDAO.getRemarkByTypeAndCwb(remarktype,cwb);
		if (remarkList.size() > 0) {
			for (Remark r : remarkList) {
				remarks += r.getRemark() + ",";
			}
		} else {
			remarks += ",";
		}
		return remarks.substring(0, remarks.length() - 1);
	}

	public void AdvanceQueryExportExcelMethod(HttpServletResponse response, HttpServletRequest request, long page) {
		/*
		 * String mouldfieldids2 = request.getParameter("exportmould2"); // 导出模板
		 * 
		 * String[] cloumnName1 = {}; // 导出的列名 String[] cloumnName2 = {}; //
		 * 导出的英文列名
		 * 
		 * if (mouldfieldids2 != null && !"0".equals(mouldfieldids2)) { // 选择模板
		 * List<SetExportField> listSetExportField =
		 * getDmpDAO.getSetExportFieldByExportstate(mouldfieldids2); cloumnName1
		 * = new String[listSetExportField.size()]; cloumnName2 = new
		 * String[listSetExportField.size()]; for (int k = 0, j = 0; j <
		 * listSetExportField.size(); j++, k++) { cloumnName1[k] =
		 * listSetExportField.get(j).getFieldname(); cloumnName2[k] =
		 * listSetExportField.get(j).getFieldenglishname(); } } else {
		 * List<SetExportField> listSetExportField =
		 * getDmpDAO.getSetExportFieldByExportstate("0"); cloumnName1 = new
		 * String[listSetExportField.size()]; cloumnName2 = new
		 * String[listSetExportField.size()]; for (int k = 0, j = 0; j <
		 * listSetExportField.size(); j++, k++) { cloumnName1[k] =
		 * listSetExportField.get(j).getFieldname(); cloumnName2[k] =
		 * listSetExportField.get(j).getFieldenglishname(); } } final String[]
		 * cloumnName = cloumnName1; final String[] cloumnName3 = cloumnName2;
		 * String sheetName = "订单信息"; // sheet的名称 SimpleDateFormat df = new
		 * SimpleDateFormat("yyyy-MM-dd_HH-mm-ss"); String fileName = "Order_" +
		 * df.format(new Date()) + ".xlsx"; // 文件名 try { // 查询出数据 long
		 * customerwarehouseid =
		 * Long.parseLong(request.getParameter("customerwarehouseid"
		 * ).toString()); long cwbordertypeid =
		 * Long.parseLong(request.getParameter("cwbordertypeid").toString());
		 * long startbranchid =
		 * Long.parseLong(request.getParameter("startbranchid").toString());
		 * long nextbranchid =
		 * Long.parseLong(request.getParameter("nextbranchid").toString()); long
		 * datetype =
		 * Long.parseLong(request.getParameter("datetype").toString()); String
		 * orderflowcwbs = ""; String begindate =
		 * request.getParameter("begindate").toString(); String enddate =
		 * request.getParameter("enddate").toString(); String commonnumber =
		 * request.getParameter("commonnumber").toString(); long customerid =
		 * Long.parseLong(request.getParameter("customerid").toString()); long
		 * currentBranchid =
		 * Long.parseLong(request.getParameter("currentBranchid").toString());
		 * long dispatchbranchid =
		 * Long.parseLong(request.getParameter("dispatchbranchid").toString());
		 * long kufangid =
		 * Long.parseLong(request.getParameter("kufangid").toString()); long
		 * paywayid =
		 * Long.parseLong(request.getParameter("paywayid").toString()); long
		 * dispatchdeliveryid =
		 * Long.parseLong(request.getParameter("dispatchdeliveryid"
		 * ).toString()); String consigneename =
		 * request.getParameter("consigneename").toString(); String
		 * consigneemobile = request.getParameter("consigneemobile").toString();
		 * String packagecode = request.getParameter("packagecode").toString();
		 * long beginWatht =
		 * Long.parseLong(request.getParameter("beginWatht").toString()); long
		 * endWatht =
		 * Long.parseLong(request.getParameter("endWatht").toString()); long
		 * beginsendcarnum =
		 * Long.parseLong(request.getParameter("beginsendcarnum").toString());
		 * long endsendcarnum =
		 * Long.parseLong(request.getParameter("endsendcarnum").toString());
		 * String carsize = request.getParameter("carsize").toString(); long
		 * flowordertype =
		 * Long.parseLong(request.getParameter("flowordertype").toString());
		 * String[] deliverystates =
		 * request.getParameterValues("deliverystates")==null?new
		 * String[]{}:request.getParameterValues("deliverystates");
		 * 
		 * 
		 * 
		 * final String sql= cwbDAO.getSQLExport(datetype, begindate, enddate,
		 * customerid, commonnumber, customerwarehouseid, startbranchid,
		 * nextbranchid, cwbordertypeid, orderflowcwbs, currentBranchid,
		 * dispatchbranchid, kufangid, paywayid, dispatchdeliveryid,
		 * consigneename, consigneemobile, beginWatht, endWatht,
		 * beginsendcarnum, endsendcarnum, carsize, flowordertype,
		 * deliverystates, packagecode, page);
		 * 
		 * ExcelUtilsOld excelUtil = new ExcelUtilsOld() { //
		 * 生成工具类实例，并实现填充数据的抽象方法
		 * 
		 * @Override public void fillData(final Sheet sheet, final CellStyle
		 * style) {
		 * 
		 * final List<Reason> reasonList = getDmpDAO.getAllReason(); final
		 * List<User> uList = getDmpDAO.getAllUsers(); final Map<Long,Customer>
		 * cMap = getDmpDAO.getAllCustomersToMap(); final List<Branch> bList =
		 * getDmpDAO.getAllBranchs(); final List<Common> commonList =
		 * getDmpDAO.getAllCommons(); final List<CustomWareHouse> cWList =
		 * getDmpDAO.getCustomWareHouse(); jdbcTemplate.query(new
		 * StreamingStatementCreator(sql), new RowCallbackHandler(){ private int
		 * count=0;
		 * 
		 * @Override public void processRow(ResultSet rs) throws SQLException {
		 * Row row = sheet.createRow(count + 1); row.setHeightInPoints((float)
		 * 15);
		 * //System.out.println(ds.getCwb()+":"+System.currentTimeMillis()); for
		 * (int i = 0; i < cloumnName.length; i++) { Cell cell =
		 * row.createCell((short) i); cell.setCellStyle(style); Object a =
		 * exportService.setObjectA(cloumnName3, new
		 * ResultSetWrappingSqlRowSet(rs), i ,
		 * uList,cMap,bList,commonList,cWList,reasonList); cell.setCellValue(a
		 * == null ? "" : a.toString()); } count++;
		 * 
		 * }});
		 * 
		 * } }; excelUtil.excel(response, cloumnName, sheetName, fileName); //
		 * System.out.println("get end:"+System.currentTimeMillis());
		 * 
		 * } catch (Exception e) { e.printStackTrace(); }
		 */
	}

	// public void AdvanceQueryExportExcelMethod(HttpServletResponse response,
	// HttpServletRequest request) {
	// String mouldfieldids2 = request.getParameter("exportmould2"); // 导出模板
	//
	// String[] cloumnName1 = {}; // 导出的列名
	// String[] cloumnName2 = {}; // 导出的英文列名
	//
	// if (mouldfieldids2 != null && !"0".equals(mouldfieldids2)) { // 选择模板
	// List<SetExportField> listSetExportField =
	// exportmouldDAO.getSetExportFieldByStrs(mouldfieldids2);
	// cloumnName1 = new String[listSetExportField.size()];
	// cloumnName2 = new String[listSetExportField.size()];
	// for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
	// cloumnName1[k] = listSetExportField.get(j).getFieldname();
	// cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
	// }
	// } else {
	// List<SetExportField> listSetExportField =
	// exportmouldDAO.getSetExportFieldByStrs("0");
	// cloumnName1 = new String[listSetExportField.size()];
	// cloumnName2 = new String[listSetExportField.size()];
	// for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
	// cloumnName1[k] = listSetExportField.get(j).getFieldname();
	// cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
	// }
	// }
	// final String[] cloumnName = cloumnName1;
	// final String[] cloumnName3 = cloumnName2;
	// String sheetName = "订单信息"; // sheet的名称
	// SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-MM-ss");
	// String fileName = "Order_" + df.format(new Date()) + ".xlsx"; // 文件名
	// try {
	// // 查询出数据
	// String cwbandtranscwb =
	// request.getSession().getAttribute("cwbandtranscwb").toString();
	// long datetype =
	// Long.parseLong(request.getSession().getAttribute("datetype").toString());
	// String begindate =
	// request.getSession().getAttribute("begindate").toString();
	// String enddate = request.getSession().getAttribute("enddate").toString();
	// long customerid =
	// Long.parseLong(request.getSession().getAttribute("customerid").toString());
	// String commonnumber =
	// request.getSession().getAttribute("commonnumber").toString();
	// String orderResultTypeStr =
	// request.getSession().getAttribute("orderResultTypeStr").toString();
	// long customerwarehouseid =
	// Long.parseLong(request.getSession().getAttribute("customerwarehouseid").toString());
	// long cwbordertypeid =
	// Long.parseLong(request.getSession().getAttribute("cwbordertypeid").toString());
	// long startbranchid =
	// Long.parseLong(request.getSession().getAttribute("startbranchid").toString());
	// long nextbranchid =
	// Long.parseLong(request.getSession().getAttribute("nextbranchid").toString());
	// long kufangid =
	// Long.parseLong(request.getSession().getAttribute("kufangid").toString());
	// long paytype =
	// Long.parseLong(request.getSession().getAttribute("paytype").toString());
	// long startdelivername =
	// Long.parseLong(request.getSession().getAttribute("startdelivername").toString());
	// long nextdelivername =
	// Long.parseLong(request.getSession().getAttribute("nextdelivername").toString());
	// String consigneename =
	// request.getSession().getAttribute("consigneename").toString();
	// String consigneemobile =
	// request.getSession().getAttribute("consigneemobile").toString();
	// long beginWatht =
	// Long.parseLong(request.getSession().getAttribute("beginWatht").toString());
	// long endWatht =
	// Long.parseLong(request.getSession().getAttribute("endWatht").toString());
	// long beginsendcarnum =
	// Long.parseLong(request.getSession().getAttribute("beginsendcarnum").toString());
	// long endsendcarnum =
	// Long.parseLong(request.getSession().getAttribute("endsendcarnum").toString());
	// String carsize = request.getSession().getAttribute("carsize").toString();
	// long flowordertype =
	// Long.parseLong(request.getSession().getAttribute("flowordertype").toString());
	// final String sql= cwbDAO.getSQLExport(cwbandtranscwb.replaceAll("'",
	// "\\\\'"), datetype, begindate, enddate, customerid, commonnumber,
	// orderResultTypeStr,
	// customerwarehouseid, cwbordertypeid, startbranchid,nextbranchid,
	// kufangid, paytype, startdelivername,nextbranchid,
	// consigneename.replaceAll("'", "\\\\'"), consigneemobile.replaceAll("'",
	// "\\\\'"), beginWatht,
	// endWatht, beginsendcarnum, endsendcarnum, carsize, flowordertype);
	// // final List<ExportDTO> list =
	// // exportDAO.getExportHmj(cwbandtranscwb, datetype, begindate,
	// // enddate, customerid, commonnumber, flowordertype, orderName);
	//
	// ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
	// @Override
	// public void fillData(final Sheet sheet, final CellStyle style) {
	// jdbcTemplate.query(sql, new RowCallbackHandler(){
	//
	// private int count=0;
	// @Override
	// public void processRow(ResultSet rs) throws SQLException {
	// Row row = sheet.createRow(count + 1);
	// row.setHeightInPoints((float) 15);
	// for (int i = 0; i < cloumnName.length; i++) {
	// Cell cell = row.createCell((short) i);
	// cell.setCellStyle(style);
	// Object a = exportService.setObjectA(cloumnName3, new
	// ResultSetWrappingSqlRowSet(rs), i);
	// cell.setCellValue(a == null ? "" : a.toString());
	// }
	// count++;
	//
	// }});
	//
	// }
	// };
	// excelUtil.excel(response, cloumnName, sheetName, fileName);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
}
