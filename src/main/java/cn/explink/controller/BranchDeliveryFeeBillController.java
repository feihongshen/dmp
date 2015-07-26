package cn.explink.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.BranchDeliveryFeeBillDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.PenalizeTypeDAO;
import cn.explink.dao.RoleDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.ExpressSetBranchDeliveryFeeBill;
import cn.explink.domain.ExpressSetBranchDeliveryFeeBillDetail;
import cn.explink.domain.Role;
import cn.explink.domain.User;
import cn.explink.domain.VO.ExpressSetBranchDeliveryFeeBillDetailVO;
import cn.explink.domain.VO.ExpressSetBranchDeliveryFeeBillVO;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryFeeBillCwbTypeEnum;
import cn.explink.enumutil.DeliveryFeeBillDateTypeEnum;
import cn.explink.enumutil.DeliveryFeeBillStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.service.BranchDeliveryFeeBillService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.ExcelUtils;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Controller
@RequestMapping("/branchDeliveryFeeBill")
public class BranchDeliveryFeeBillController {

	@Autowired
	BranchDeliveryFeeBillDAO branchDeliveryFeeBillDAO;
	@Autowired
	BranchDeliveryFeeBillService branchDeliveryFeeBillService;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	RoleDAO roleDAO;
	@Autowired
	PenalizeTypeDAO penalizeTypeDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	ExportService exportService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy
				.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/branchDeliveryFeeBillList/{page}")
	public String branchDeliveryFeeBillList(@PathVariable("page") long page, Model model,
			ExpressSetBranchDeliveryFeeBillVO queryConditionVO) {
		
		List<Branch> branchList = this.branchDAO.getAllBranches();
		// 订单状态枚举
		Map<Integer, String> billStateMap = DeliveryFeeBillStateEnum.getMap();
		// 订单类型枚举
		Map<Integer, String> cwbTypeMap = DeliveryFeeBillCwbTypeEnum.getMap();
		// 日期类型枚举
		Map<Integer, String> dateTypeMap = DeliveryFeeBillDateTypeEnum.getMap();
		List<ExpressSetBranchDeliveryFeeBill> list = this.branchDeliveryFeeBillDAO
				.queryBranchDeliveryFeeBill(queryConditionVO);
		int count = this.branchDeliveryFeeBillDAO
				.queryBranchDeliveryFeeBillCount(queryConditionVO);
		Page page_obj = new Page(count, page, Page.ONE_PAGE_NUMBER);
		
		model.addAttribute("page", page);
		model.addAttribute("page_obj", page_obj);
		model.addAttribute("branchDeliveryFeeBillList", list);
		model.addAttribute("queryConditionVO", queryConditionVO);
		model.addAttribute("branchList", branchList);
		model.addAttribute("billStateMap", billStateMap);
		model.addAttribute("cwbTypeMap", cwbTypeMap);
		model.addAttribute("dateTypeMap", dateTypeMap);
		model.addAttribute("weiShenHeState", DeliveryFeeBillStateEnum.WeiShenHe.getValue());
		return "branchDeliveryFeeBill/branchDeliveryFeeBillList";
	}

	@RequestMapping("/addBranchDeliveryFeeBill")
	public String addBranchDeliveryFeeBill(
			ExpressSetBranchDeliveryFeeBill branchDeliveryFeeBill, Model model) {

		User user = getSessionUser();
		if (user != null) {
			long userId = user.getUserid();
			branchDeliveryFeeBill.setCreator(new Long(userId).intValue());
		}
		int id = this.branchDeliveryFeeBillService.createBranchDeliveryFeeBill(branchDeliveryFeeBill);
		
		List<Branch> branchList = this.branchDAO.getAllBranches();
		Map<Integer, String> billStateMap = DeliveryFeeBillStateEnum.getMap();
		Map<Integer, String> cwbStateMap = FlowOrderTypeEnum.getMap();
		Map<Integer, String> cwbOrderTypeMap = CwbOrderTypeIdEnum.getMap();
		//支付方式
		List<ExpressSetBranchDeliveryFeeBill> list = this.branchDeliveryFeeBillDAO
				.queryBranchDeliveryFeeBill(new ExpressSetBranchDeliveryFeeBillVO());
		ExpressSetBranchDeliveryFeeBillVO branchDeliveryFeeBillVO = this.branchDeliveryFeeBillService
				.getBranchDeliveryFeeBillVO(id);
		int jiesuanAuthority = 0;
		int jiesuanAdvanceAuthority = 0;
		if(user != null){
			long roleid = user.getRoleid();
			Role role = this.roleDAO.getRolesByRoleid(roleid);
			if(role != null){
				if("结算".equals(role.getRolename())){
					jiesuanAuthority = 1;
					jiesuanAdvanceAuthority = 1;
				}
			}
		}

		model.addAttribute("jiesuanAuthority", jiesuanAuthority);
		model.addAttribute("jiesuanAdvanceAuthority", jiesuanAdvanceAuthority);
		model.addAttribute("weiShenHeState", DeliveryFeeBillStateEnum.WeiShenHe.getValue());
		model.addAttribute("yiShenHeState", DeliveryFeeBillStateEnum.YiShenHe.getValue());
		model.addAttribute("yiHeXiaoState", DeliveryFeeBillStateEnum.YiHeXiao.getValue());
		model.addAttribute("branchDeliveryFeeBillList", list);
		model.addAttribute("branchList", branchList);
		model.addAttribute("billStateMap", billStateMap);
		model.addAttribute("cwbStateMap", cwbStateMap);
		model.addAttribute("cwbOrderTypeMap", cwbOrderTypeMap);
		model.addAttribute("updatePage", 1);
		model.addAttribute("branchDeliveryFeeBillVO", branchDeliveryFeeBillVO);
		return "branchDeliveryFeeBill/branchDeliveryFeeBillList";
	}

	@RequestMapping("/updateBranchDeliveryFeeBillPage")
	public String updateBranchDeliveryFeeBillPage(int id, Model model) {

		List<Branch> branchList = this.branchDAO.getAllBranches();
		Map<Integer, String> billStateMap = DeliveryFeeBillStateEnum.getMap();
		Map<Integer, String> cwbStateMap = FlowOrderTypeEnum.getMap();
		Map<Integer, String> cwbOrderTypeMap = CwbOrderTypeIdEnum.getMap();
		Map<Integer, String> payTypeMap = PaytypeEnum.getMap();
		List<ExpressSetBranchDeliveryFeeBill> list = this.branchDeliveryFeeBillDAO
				.queryBranchDeliveryFeeBill(new ExpressSetBranchDeliveryFeeBillVO());
		ExpressSetBranchDeliveryFeeBillVO branchDeliveryFeeBillVO = this.branchDeliveryFeeBillService
				.getBranchDeliveryFeeBillVO(id);
		User user = getSessionUser();
		int jiesuanAuthority = 0;
		int jiesuanAdvanceAuthority = 0;
		if(user != null){
			long roleid = user.getRoleid();
			Role role = this.roleDAO.getRolesByRoleid(roleid);
			if(role != null){
				if("结算".equals(role.getRolename())){
					jiesuanAuthority = 1;
					jiesuanAdvanceAuthority = 1;
				}
			}
		}

		model.addAttribute("jiesuanAuthority", jiesuanAuthority);
		model.addAttribute("jiesuanAdvanceAuthority", jiesuanAdvanceAuthority);
		model.addAttribute("weiShenHeState", DeliveryFeeBillStateEnum.WeiShenHe.getValue());
		model.addAttribute("yiShenHeState", DeliveryFeeBillStateEnum.YiShenHe.getValue());
		model.addAttribute("yiHeXiaoState", DeliveryFeeBillStateEnum.YiHeXiao.getValue());
		model.addAttribute("branchList", branchList);
		model.addAttribute("branchDeliveryFeeBillList", list);
		model.addAttribute("billStateMap", billStateMap);
		model.addAttribute("cwbStateMap", cwbStateMap);
		model.addAttribute("cwbOrderTypeMap", cwbOrderTypeMap);
		model.addAttribute("payTypeMap", payTypeMap);
		model.addAttribute("updatePage", 1);
		model.addAttribute("branchDeliveryFeeBillVO", branchDeliveryFeeBillVO);
		return "branchDeliveryFeeBill/branchDeliveryFeeBillList";
	}

	@RequestMapping("/branchList/{page}")
	public String branchList(@PathVariable("page") long page, ExpressSetBranchDeliveryFeeBillVO billVO,
			Model model) {

		List<Branch> branchList = this.branchDAO.getAllBranches();
		Map<Integer, String> billStateMap = DeliveryFeeBillStateEnum.getMap();
		Map<Integer, String> cwbTypeMap = DeliveryFeeBillCwbTypeEnum.getMap();
		Map<Integer, String> dateTypeMap = DeliveryFeeBillDateTypeEnum.getMap();
		List<ExpressSetBranchDeliveryFeeBill> list = this.branchDeliveryFeeBillDAO
				.queryBranchDeliveryFeeBill(new ExpressSetBranchDeliveryFeeBillVO());
		int count = this.branchDeliveryFeeBillDAO
				.queryBranchDeliveryFeeBillCount(new ExpressSetBranchDeliveryFeeBillVO());
		List<Branch> branches= this.branchDAO.getBranchByPage(page, billVO.getBranchname(), billVO.getBranchaddress());
//		int count = new Long(this.branchDAO.getBranchCount(billVO.getBranchname(), billVO.getBranchaddress())).intValue();
		
		Page page_obj = new Page(count, page, Page.ONE_PAGE_NUMBER);
		User user = getSessionUser();
		int jiesuanAuthority = 0;
		int jiesuanAdvanceAuthority = 0;
		if(user != null){
			long roleid = user.getRoleid();
			Role role = this.roleDAO.getRolesByRoleid(roleid);
			if(role != null){
				if("结算".equals(role.getRolename())){
					jiesuanAuthority = 1;
					jiesuanAdvanceAuthority = 1;
				}
			}
		}

		model.addAttribute("jiesuanAuthority", jiesuanAuthority);
		model.addAttribute("jiesuanAdvanceAuthority", jiesuanAdvanceAuthority);
		model.addAttribute("weiShenHeState", DeliveryFeeBillStateEnum.WeiShenHe.getValue());
		model.addAttribute("yiShenHeState", DeliveryFeeBillStateEnum.YiShenHe.getValue());
		model.addAttribute("yiHeXiaoState", DeliveryFeeBillStateEnum.YiHeXiao.getValue());
		model.addAttribute("page", page);
		model.addAttribute("page_obj", page_obj);
		model.addAttribute("branchDeliveryFeeBillList", list);
		model.addAttribute("branchDeliveryFeeBillVO", billVO);
		model.addAttribute("branchList", branches);
		model.addAttribute("billStateMap", billStateMap);
		model.addAttribute("cwbTypeMap", cwbTypeMap);
		model.addAttribute("dateTypeMap", dateTypeMap);
		model.addAttribute("addPage", 1);
		model.addAttribute("branchListPage", 1);
		return "branchDeliveryFeeBill/branchDeliveryFeeBillList";
	}

	@RequestMapping("/updateBranchDeliveryFeeBill")
	@ResponseBody
	public String updateBranchDeliveryFeeBill(
			ExpressSetBranchDeliveryFeeBillVO branchDeliveryFeeBillVO) {
		User user = getSessionUser();
		int userId = 0;
		if (user != null) {
			userId = new Long(user.getUserid()).intValue();
		}
		if(branchDeliveryFeeBillVO.getBillState() == DeliveryFeeBillStateEnum.YiShenHe.getValue()){
			branchDeliveryFeeBillVO.setShenHePerson(userId);
			branchDeliveryFeeBillVO.setShenHeDate(DateTimeUtil.getNowDate());
		} else if(branchDeliveryFeeBillVO.getBillState() == DeliveryFeeBillStateEnum.YiHeXiao.getValue()){
			branchDeliveryFeeBillVO.setHeXiaoPerson(userId);;
			branchDeliveryFeeBillVO.setHeXiaoDate(DateTimeUtil.getNowDate());
		}
		this.branchDeliveryFeeBillService.updateBranchDeliveryFeeBill(branchDeliveryFeeBillVO);
		return "{\"errorCode\":0,\"error\":\"修改成功\"}";
	}

	@RequestMapping("/deleteBranchDeliveryFeeBill")
	@ResponseBody
	public String deleteBranchDeliveryFeeBill(
			@RequestParam(value = "ids", defaultValue = "", required = true) String ids) {
		this.branchDeliveryFeeBillService.deleteBranchDeliveryFeeBill(ids);
		return "{\"errorCode\":0,\"error\":\"删除成功\"}";
	}

	/**
	 * 加盟商派费账单按明细导出Excel
	 *
	 * @param model
	 */
	@RequestMapping("/exportByDetail")
	public void exportByDetail(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs) {
		String[] cloumnName1 = new String[17]; // 导出的列名
		String[] cloumnName2 = new String[17]; // 导出的英文列名
		this.exportService.SetBranchDeliveryFeeBillFields(cloumnName1, cloumnName2);
		final String[] cloumnName = cloumnName1;
		final String[] cloumnName3 = cloumnName2;
		String sheetName = "加盟商派费"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "BranchDeliveryFeeBill_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			final Map<Long, Customer> cMap = this.customerDAO.getAllCustomersToMap();
			cwbs = StringUtil.getStringsByStringList(Arrays.asList(cwbs.split(",")));
			final List<ExpressSetBranchDeliveryFeeBillDetail> list = this.branchDeliveryFeeBillDAO.getBranchDeliveryFeeBillDetailList(cwbs);

			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(Sheet sheet, CellStyle style) {
					for (int k = 0; k < list.size(); k++) {
						Row row = sheet.createRow(k + 1);
						row.setHeightInPoints(15);
						for (int i = 0; i < cloumnName.length; i++) {
							Cell cell = row.createCell((short) i);
							cell.setCellStyle(style);
							Object a = null;
							// 给导出excel赋值
							a = BranchDeliveryFeeBillController.this.exportService.setBranchDeliveryFeeBillDetailObject(cloumnName3, list, a, i, k, cMap);
							cell.setCellValue(a == null ? "" : a.toString());
						}
					}
				}
			};
			excelUtil.excel(response, cloumnName, sheetName, fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/exportByCustomer")
	@ResponseBody
	public String exportByCustomer(
			@RequestParam(value = "content", defaultValue = "", required = true) String content, HttpServletRequest request) throws IOException {
//		File directory = new File("");// 参数为空
        String contextPath = request.getContextPath();
		
		String rootPath  = request.getSession().getServletContext().getRealPath("/");
		this.branchDeliveryFeeBillService.exportByCustomer(content, contextPath, rootPath);
		return "{\"errorCode\":0,\"error\":\"导出成功\"}";
	}
	
	@RequestMapping("/getExportData")
	public ExpressSetBranchDeliveryFeeBillVO getExportData(
			@RequestParam(value = "cwbs", defaultValue = "", required = true) String cwbs, HttpServletRequest request) throws IOException {
		ExpressSetBranchDeliveryFeeBillVO rtnVO = new ExpressSetBranchDeliveryFeeBillVO();
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		List<ExpressSetBranchDeliveryFeeBillDetailVO> rtnList = null;
		cwbs = StringUtil.getStringsByStringList(Arrays.asList(cwbs.split(",")));
		
		final Map<Long, Customer> cMap = this.customerDAO.getAllCustomersToMap();
		List<ExpressSetBranchDeliveryFeeBillDetailVO> billDetailVOList = this.branchDeliveryFeeBillDAO.getBranchDeliveryFeeBillDetailVOList(cwbs);
		ExpressSetBranchDeliveryFeeBillDetailVO billDetailVO = null;
		if(billDetailVOList != null && !billDetailVOList.isEmpty()){
			for(int i = 0; i < billDetailVOList.size(); i++){
				billDetailVO = billDetailVOList.get(i);
				if(billDetailVO != null){
					if(billDetailVO.getCustomerid() != 0){
						if(rtnMap.get(cMap.get(billDetailVO.getCustomerid()).getCustomername()) == null){
							rtnList = new ArrayList<ExpressSetBranchDeliveryFeeBillDetailVO>();
							rtnList.add(billDetailVO);
							rtnMap.put(cMap.get(billDetailVO.getCustomerid()).getCustomername(),rtnList);
						} else {
							rtnList = (List<ExpressSetBranchDeliveryFeeBillDetailVO>)rtnMap.get(cMap.get(billDetailVO.getCustomerid()).getCustomername());
							rtnList.add(billDetailVO);
						}
					}
				}
			}
		}
		rtnVO.setCustomerDeliveryFee(rtnMap);
		ExpressSetBranchDeliveryFeeBillDetailVO deliveryFeeObj = this.branchDeliveryFeeBillDAO.getDeliveryFee(cwbs);
		rtnVO.setDeliveryFeeObj(deliveryFeeObj);
		ExpressSetBranchDeliveryFeeBillDetailVO pickupFeeObj = this.branchDeliveryFeeBillDAO.getPickupFee(cwbs);
		rtnVO.setPickupFeeObj(pickupFeeObj);
		return rtnVO;
	}
}
