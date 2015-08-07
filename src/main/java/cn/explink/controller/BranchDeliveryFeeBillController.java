package cn.explink.controller;

import java.io.IOException;
import java.math.BigDecimal;
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

import cn.explink.dao.BranchContractDAO;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.BranchDeliveryFeeBillDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.PenalizeTypeDAO;
import cn.explink.dao.RoleDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.ExpressSetBranchContract;
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
	@Autowired
	BranchContractDAO branchContractDAO;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy
				.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/branchDeliveryFeeBillList/{page}")
	public String branchDeliveryFeeBillList(@PathVariable("page") long page, Model model,
			ExpressSetBranchDeliveryFeeBillVO queryConditionVO) {
		// 加盟商站点列表
		List<Branch> branchList = this.branchDAO.getJoinBranchByPage(1, "", "");
		// 账单状态枚举
		Map<Integer, String> billStateMap = DeliveryFeeBillStateEnum.getMap();
		// 账单的订单类型枚举
		Map<Integer, String> cwbTypeMap = DeliveryFeeBillCwbTypeEnum.getMap();
		// 账单的日期类型枚举
		Map<Integer, String> dateTypeMap = DeliveryFeeBillDateTypeEnum.getMap();
		// 加盟商派费账单列表
		List<ExpressSetBranchDeliveryFeeBill> list = this.branchDeliveryFeeBillDAO
				.queryBranchDeliveryFeeBill(page, queryConditionVO);
		// 加盟商派费账单列表条数
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
		// 当前登录用户
		User user = getSessionUser();
		if (user != null) {
			long userId = user.getUserid();
			branchDeliveryFeeBill.setCreator(new Long(userId).intValue());
		}
		// 创建加盟商派费账单
		int id = this.branchDeliveryFeeBillService.createBranchDeliveryFeeBill(branchDeliveryFeeBill);
		// 加盟商站点列表
		List<Branch> branchList = this.branchDAO.getJoinBranchByPage(1, "", "");
		// 账单状态枚举
		Map<Integer, String> billStateMap = DeliveryFeeBillStateEnum.getMap();
		// 账单的订单类型枚举
		Map<Integer, String> cwbTypeMap = DeliveryFeeBillCwbTypeEnum.getMap();
		// 账单的日期类型枚举
		Map<Integer, String> dateTypeMap = DeliveryFeeBillDateTypeEnum.getMap();
		// 订单状态枚举
		Map<Integer, String> cwbStateMap = FlowOrderTypeEnum.getMap();
		// 订单类型枚举
		Map<Integer, String> cwbOrderTypeMap = CwbOrderTypeIdEnum.getMap();
		// 支付方式枚举
		Map<Integer, String> payTypeMap = PaytypeEnum.getMap();
		// 账单列表
		List<ExpressSetBranchDeliveryFeeBill> list = this.branchDeliveryFeeBillDAO
				.queryBranchDeliveryFeeBill(1, new ExpressSetBranchDeliveryFeeBillVO());
		// 账单列表条数
		int count = this.branchDeliveryFeeBillDAO
				.queryBranchDeliveryFeeBillCount(new ExpressSetBranchDeliveryFeeBillVO());
		// 创建的账单信息
		ExpressSetBranchDeliveryFeeBillVO branchDeliveryFeeBillVO = this.branchDeliveryFeeBillService
				.getBranchDeliveryFeeBillVO(id);
		// 普通权限
		int jiesuanAuthority = 0;
		// 高级权限
		int jiesuanAdvanceAuthority = 0;
		if(user != null){
			// 当前登录所属的角色id
			long roleid = user.getRoleid();
			// 当前登录所属的角色
			Role role = this.roleDAO.getRolesByRoleid(roleid);
			if(role != null){
				if("结算".equals(role.getRolename())){
					jiesuanAuthority = 1;
					jiesuanAdvanceAuthority = 1;
				}
			}
		}
		Page page_obj = new Page(count, 1, Page.ONE_PAGE_NUMBER);
		
		model.addAttribute("page", 1);
		model.addAttribute("page_obj", page_obj);
		model.addAttribute("jiesuanAuthority", jiesuanAuthority);
		model.addAttribute("jiesuanAdvanceAuthority", jiesuanAdvanceAuthority);
		model.addAttribute("weiShenHeState", DeliveryFeeBillStateEnum.WeiShenHe.getValue());
		model.addAttribute("yiShenHeState", DeliveryFeeBillStateEnum.YiShenHe.getValue());
		model.addAttribute("yiHeXiaoState", DeliveryFeeBillStateEnum.YiHeXiao.getValue());
		model.addAttribute("branchDeliveryFeeBillList", list);
		model.addAttribute("branchList", branchList);
		model.addAttribute("billStateMap", billStateMap);
		model.addAttribute("cwbTypeMap", cwbTypeMap);
		model.addAttribute("dateTypeMap", dateTypeMap);
		model.addAttribute("cwbStateMap", cwbStateMap);
		model.addAttribute("cwbOrderTypeMap", cwbOrderTypeMap);
		model.addAttribute("payTypeMap", payTypeMap);
		model.addAttribute("updatePage", 1);
		model.addAttribute("branchDeliveryFeeBillVO", branchDeliveryFeeBillVO);
		return "branchDeliveryFeeBill/branchDeliveryFeeBillList";
	}

	@RequestMapping("/updateBranchDeliveryFeeBillPage")
	public String updateBranchDeliveryFeeBillPage(int id, Model model) {
		// 加盟商站点列表
		List<Branch> branchList = this.branchDAO.getJoinBranchByPage(1, "", "");
		// 账单状态枚举
		Map<Integer, String> billStateMap = DeliveryFeeBillStateEnum.getMap();
		// 订单状态枚举
		Map<Integer, String> cwbStateMap = FlowOrderTypeEnum.getMap();
		// 账单的订单类型枚举
		Map<Integer, String> cwbTypeMap = DeliveryFeeBillCwbTypeEnum.getMap();
		// 账单的日期类型枚举
		Map<Integer, String> dateTypeMap = DeliveryFeeBillDateTypeEnum.getMap();
		// 订单类型枚举
		Map<Integer, String> cwbOrderTypeMap = CwbOrderTypeIdEnum.getMap();
		// 支付方式枚举
		Map<Integer, String> payTypeMap = PaytypeEnum.getMap();
		// 账单列表
		List<ExpressSetBranchDeliveryFeeBill> list = this.branchDeliveryFeeBillDAO
				.queryBranchDeliveryFeeBill(1, new ExpressSetBranchDeliveryFeeBillVO());
		// 账单列表条数
		int count = this.branchDeliveryFeeBillDAO
				.queryBranchDeliveryFeeBillCount(new ExpressSetBranchDeliveryFeeBillVO());
		// 所要修改或者查看的账单信息
		ExpressSetBranchDeliveryFeeBillVO branchDeliveryFeeBillVO = this.branchDeliveryFeeBillService
				.getBranchDeliveryFeeBillVO(id);
		// 当前登录用户
		User user = getSessionUser();
		// 普通权限
		int jiesuanAuthority = 0;
		// 高级权限
		int jiesuanAdvanceAuthority = 0;
		if(user != null){
			// 当前登录用户的角色id
			long roleid = user.getRoleid();
			// 当前登录用户的角色
			Role role = this.roleDAO.getRolesByRoleid(roleid);
			if(role != null){
				if("结算".equals(role.getRolename())){
					jiesuanAuthority = 1;
					jiesuanAdvanceAuthority = 1;
				}
			}
		}
		Page page_obj = new Page(count, 1, Page.ONE_PAGE_NUMBER);
		
		model.addAttribute("page", 1);
		model.addAttribute("page_obj", page_obj);
		model.addAttribute("jiesuanAuthority", jiesuanAuthority);
		model.addAttribute("jiesuanAdvanceAuthority", jiesuanAdvanceAuthority);
		model.addAttribute("weiShenHeState", DeliveryFeeBillStateEnum.WeiShenHe.getValue());
		model.addAttribute("yiShenHeState", DeliveryFeeBillStateEnum.YiShenHe.getValue());
		model.addAttribute("yiHeXiaoState", DeliveryFeeBillStateEnum.YiHeXiao.getValue());
		model.addAttribute("branchList", branchList);
		model.addAttribute("branchDeliveryFeeBillList", list);
		model.addAttribute("billStateMap", billStateMap);
		model.addAttribute("cwbTypeMap", cwbTypeMap);
		model.addAttribute("dateTypeMap", dateTypeMap);
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
		// 账单状态枚举
		Map<Integer, String> billStateMap = DeliveryFeeBillStateEnum.getMap();
		// 订单状态枚举
		Map<Integer, String> cwbStateMap = FlowOrderTypeEnum.getMap();
		// 账单的订单类型枚举
		Map<Integer, String> cwbTypeMap = DeliveryFeeBillCwbTypeEnum.getMap();
		// 账单的日期类型枚举
		Map<Integer, String> dateTypeMap = DeliveryFeeBillDateTypeEnum.getMap();
		// 订单类型枚举
		Map<Integer, String> cwbOrderTypeMap = CwbOrderTypeIdEnum.getMap();
		// 支付方式枚举
		Map<Integer, String> payTypeMap = PaytypeEnum.getMap();
		// 账单列表
		List<ExpressSetBranchDeliveryFeeBill> list = this.branchDeliveryFeeBillDAO
				.queryBranchDeliveryFeeBill(1, new ExpressSetBranchDeliveryFeeBillVO());
		// 账单列表条数
		int count = this.branchDeliveryFeeBillDAO
				.queryBranchDeliveryFeeBillCount(new ExpressSetBranchDeliveryFeeBillVO());
		// 根据加盟商名称和加盟商地址查询的加盟商列表
		List<Branch> branches= this.branchDAO.getBranchByPage(page, billVO.getBranchname(), billVO.getBranchaddress());
//		int count = new Long(this.branchDAO.getBranchCount(billVO.getBranchname(), billVO.getBranchaddress())).intValue();
		// 分页信息
		Page page_obj = new Page(count, page, Page.ONE_PAGE_NUMBER);
		// 当前登录用户
		User user = getSessionUser();
		// 普通权限
		int jiesuanAuthority = 0;
		// 高级权限
		int jiesuanAdvanceAuthority = 0;
		if(user != null){
			// 当前登录用户的角色id
			long roleid = user.getRoleid();
			// 当前登录用户的角色
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
		model.addAttribute("cwbStateMap", cwbStateMap);
		model.addAttribute("cwbOrderTypeMap", cwbOrderTypeMap);
		model.addAttribute("payTypeMap", payTypeMap);
		model.addAttribute("addPage", 1);
		model.addAttribute("branchListPage", 1);
		return "branchDeliveryFeeBill/branchDeliveryFeeBillList";
	}

	@RequestMapping("/updateBranchDeliveryFeeBill")
	@ResponseBody
	public String updateBranchDeliveryFeeBill(
			ExpressSetBranchDeliveryFeeBillVO branchDeliveryFeeBillVO) {
		// 当前登录用户
		User user = getSessionUser();
		// 当前登录用户id
		int userId = 0;
		if (user != null) {
			userId = new Long(user.getUserid()).intValue();
		}
		if(branchDeliveryFeeBillVO.getBillState() == DeliveryFeeBillStateEnum.YiShenHe.getValue()){
			// 账单已审核状态
			// 设置审核人、审核日期
			branchDeliveryFeeBillVO.setShenHePerson(userId);
			branchDeliveryFeeBillVO.setShenHeDate(DateTimeUtil.getNowDate());
		} else if(branchDeliveryFeeBillVO.getBillState() == DeliveryFeeBillStateEnum.YiHeXiao.getValue()){
			// 账单已核销状态
			// 设置核销人、核销日期
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
		// 按客户导出
		// 上下文路径
        String contextPath = request.getContextPath();
		// 根路径
		String rootPath  = request.getSession().getServletContext().getRealPath("/");
		this.branchDeliveryFeeBillService.exportByCustomer(content, contextPath, rootPath);
		return "{\"errorCode\":0,\"error\":\"导出成功\"}";
	}
	
	@RequestMapping("/getExportData")
	@ResponseBody
	public ExpressSetBranchDeliveryFeeBillVO getExportData(
			@RequestParam(value = "cwbs", defaultValue = "", required = true) String cwbs, HttpServletRequest request) throws IOException {
		// 返回值VO
		ExpressSetBranchDeliveryFeeBillVO rtnVO = new ExpressSetBranchDeliveryFeeBillVO();
		// 返回值Map，key为客户id，value为返回值list
		Map<Object, Object> rtnMap = new HashMap<Object, Object>();
		// 返回值list，元素为按客户分组的派送费记录，理论上每个list应该包含三个元素，分别是，代收订单、非代收订单、小计(代收订单和非代收订单相应项相加之和)
		List<ExpressSetBranchDeliveryFeeBillDetailVO> rtnList = null;
		cwbs = StringUtil.getStringsByStringList(Arrays.asList(cwbs.split(",")));
		// 客户Map
		final Map<Long, Customer> cMap = this.customerDAO.getAllCustomersToMap();
		// 获取按客户分组的派送费列表
		List<ExpressSetBranchDeliveryFeeBillDetailVO> billDetailVOList = this.branchDeliveryFeeBillDAO.getBranchDeliveryFeeBillDetailVOList(cwbs);
		// 每个客户对应的小计VO
		ExpressSetBranchDeliveryFeeBillDetailVO billDetailVOByCustomer = new ExpressSetBranchDeliveryFeeBillDetailVO();
		// 设置是否代收标志，0为非代收、1为代收、2为小计
		billDetailVOByCustomer.setIsReceived(2);
		ExpressSetBranchDeliveryFeeBillDetailVO billDetailVO = null;
		if(billDetailVOList != null && !billDetailVOList.isEmpty()){
			for(int i = 0; i < billDetailVOList.size(); i++){
				billDetailVO = billDetailVOList.get(i);
				if(billDetailVO != null){
					if(billDetailVO.getCustomerid() != 0){
						// 首先判断返回值Map中是否包含相应的客户id
						// 若不包含，则添加key、value对；若包含，则根据客户id，获取返回值list，并添加当前按客户分组的派送费记录
						if(rtnMap.get(cMap.get((long)billDetailVO.getCustomerid()).getCustomername()) == null){
							// 代收订单或者非代收订单
							rtnList = new ArrayList<ExpressSetBranchDeliveryFeeBillDetailVO>();
							rtnList.add(billDetailVO);
							rtnMap.put(cMap.get((long)billDetailVO.getCustomerid()).getCustomername(),rtnList);
							// 小计
							billDetailVOByCustomer.setCustomerid(billDetailVO.getCustomerid());
							billDetailVOByCustomer.setCwbOrderCount(billDetailVO.getCwbOrderCount());
							billDetailVOByCustomer.setDeliverySumFee(billDetailVO.getDeliverySumFee());
							billDetailVOByCustomer.setDeliveryBasicFee(billDetailVO.getDeliveryBasicFee());
							billDetailVOByCustomer.setDeliveryCollectionSubsidyFee(billDetailVO.getDeliveryCollectionSubsidyFee());
							billDetailVOByCustomer.setDeliveryAreaSubsidyFee(billDetailVO.getDeliveryAreaSubsidyFee());
							billDetailVOByCustomer.setDeliveryExceedSubsidyFee(billDetailVO.getDeliveryExceedSubsidyFee());
							billDetailVOByCustomer.setDeliveryBusinessSubsidyFee(billDetailVO.getDeliveryBusinessSubsidyFee());
							billDetailVOByCustomer.setDeliveryAttachSubsidyFee(billDetailVO.getDeliveryAttachSubsidyFee());
						} else {
							// 非代收订单或者代收订单
							rtnList = (List<ExpressSetBranchDeliveryFeeBillDetailVO>)rtnMap.get(cMap.get((long)billDetailVO.getCustomerid()).getCustomername());
							rtnList.add(billDetailVO);
							// 小计
							billDetailVOByCustomer.setCwbOrderCount(billDetailVO.getCwbOrderCount() + billDetailVOByCustomer.getCwbOrderCount());
							billDetailVOByCustomer.setDeliverySumFee(billDetailVO.getDeliverySumFee().add(billDetailVOByCustomer.getDeliverySumFee()));
							billDetailVOByCustomer.setDeliveryBasicFee(billDetailVO.getDeliveryBasicFee().add(billDetailVOByCustomer.getDeliveryBasicFee()));
							billDetailVOByCustomer.setDeliveryCollectionSubsidyFee(billDetailVO.getDeliveryCollectionSubsidyFee().add(billDetailVOByCustomer.getDeliveryCollectionSubsidyFee()));
							billDetailVOByCustomer.setDeliveryAreaSubsidyFee(billDetailVO.getDeliveryAreaSubsidyFee().add(billDetailVOByCustomer.getDeliveryAreaSubsidyFee()));
							billDetailVOByCustomer.setDeliveryExceedSubsidyFee(billDetailVO.getDeliveryExceedSubsidyFee().add(billDetailVOByCustomer.getDeliveryExceedSubsidyFee()));
							billDetailVOByCustomer.setDeliveryBusinessSubsidyFee(billDetailVO.getDeliveryBusinessSubsidyFee().add(billDetailVOByCustomer.getDeliveryBusinessSubsidyFee()));
							billDetailVOByCustomer.setDeliveryAttachSubsidyFee(billDetailVO.getDeliveryAttachSubsidyFee().add(billDetailVOByCustomer.getDeliveryAttachSubsidyFee()));
							rtnList.add(billDetailVOByCustomer);
						}
					}
				}
			}
		}
		// 返回值VO设置按客户分组的派送费数据
		rtnVO.setCustomerDeliveryFee(rtnMap);
		// 应付配送费金额BD
		BigDecimal payableFeeBD = new BigDecimal(0);
		// 派送费总和
		ExpressSetBranchDeliveryFeeBillDetailVO deliveryFeeObj = this.branchDeliveryFeeBillDAO.getDeliveryFee(cwbs);
		if(deliveryFeeObj != null){
			payableFeeBD = payableFeeBD.add(deliveryFeeObj.getDeliverySumFee());
		}
		// 返回值VO设置总派送费数据
		rtnVO.setDeliveryFeeObj(deliveryFeeObj);
		// 提货费总和
		ExpressSetBranchDeliveryFeeBillDetailVO pickupFeeObj = this.branchDeliveryFeeBillDAO.getPickupFee(cwbs);
		if(pickupFeeObj != null){
			payableFeeBD = payableFeeBD.add(pickupFeeObj.getPickupSumFee());
		}
		// 返回值VO设置总提货费数据
		rtnVO.setPickupFeeObj(pickupFeeObj);
		// 获取当前加盟商的质控条款(从加盟商合同中获取)
		List<ExpressSetBranchDeliveryFeeBillDetail> detailList = this.branchDeliveryFeeBillDAO.getBranchDeliveryFeeBillDetailList(cwbs);
		if(detailList != null && !detailList.isEmpty() && detailList.get(0) != null){
			int billId = detailList.get(0).getBillId();
			ExpressSetBranchDeliveryFeeBill bill = this.branchDeliveryFeeBillDAO.getBranchDeliveryFeeBillListById(billId);
			if(bill != null){
				List<ExpressSetBranchContract>  branchContractList = this.branchContractDAO.getBranchContractListByBranchId(bill.getBranchId());
				if(branchContractList != null && !branchContractList.isEmpty() && branchContractList.get(0) != null){
					rtnVO.setQualityControlClause(branchContractList.get(0).getQualityControlClause());
				}
			}
		}
		// 应付配送费金额(目前扣减项目无法取值，所以暂由'-扣减项目'代替)
		String payableFee = payableFeeBD + "-扣减项目";
		rtnVO.setPayableFee(payableFee);
		return rtnVO;
	}
	
	@RequestMapping("/validateBranch")
	@ResponseBody
	public String validateBranch(
			@RequestParam(value = "branchId", defaultValue = "", required = true) int branchId) {
		// 判断加盟商站点是否关联派费规则，默认不关联
		String rtnStr = "{\"isExist\":0}";
		Branch branch = this.branchDAO.getBranchByBranchid(branchId);
		if(branch != null && branch.getPfruleid() != 0){
			rtnStr = "{\"isExist\":1}";
		}
		return rtnStr;
	}
}
