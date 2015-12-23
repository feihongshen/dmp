package cn.explink.controller.express;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import cn.explink.domain.User;
import cn.explink.domain.VO.express.CwbOrderBillInfo;
import cn.explink.domain.VO.express.ExpressBillParams4Create;
import cn.explink.domain.VO.express.ExpressBillParamsVO4Query;
import cn.explink.domain.VO.express.ExpressOpeAjaxResult;
import cn.explink.domain.VO.express.ExpressPayImportParams;
import cn.explink.domain.VO.express.SelectReturnVO;
import cn.explink.domain.VO.express.UserInfo;
import cn.explink.enumutil.express.ExpressBillOperFlag;
import cn.explink.enumutil.express.ExpressBillTypeEnum;
import cn.explink.enumutil.express.ExpressEffectiveEnum;
import cn.explink.service.Excel2003Extractor;
import cn.explink.service.Excel2007Extractor;
import cn.explink.service.ExcelExtractor;
import cn.explink.service.express.ExpressAcrossPayFreightCheckService;
import cn.explink.util.Tools;
import cn.explink.util.express.ExpressImportSelfException;
import cn.explink.util.express.Page;

/**
 * 跨省运费对账(应付)
 * 
 * @author jiangyu 2015年8月14日
 *
 */
@Controller
@RequestMapping("/acrossPayFreightCheck")
public class ExpressAcrossPayFreightCheckController extends ExpressBillCommonController {

	@Autowired
	ExpressAcrossPayFreightCheckService expressAcrossPayFreightCheckService;

	@Autowired
	Excel2007Extractor excel2007Extractor;

	@Autowired
	Excel2003Extractor excel2003Extractor;

	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	@RequestMapping("/index")
	public String index(Model model, ExpressBillParamsVO4Query params, HttpServletRequest request) {
		User user = getSessionUser();
		params.setUser(user);
		UserInfo userInfo = expressAcrossPayFreightCheckService.initUserInfo(user);
		model.addAttribute("params", params);
		model.addAttribute("userInfo", userInfo);
		return "express/acrossPayProvinceFreight/acrossPayfreightCheck";
	}

	@RequestMapping("/billList4AcrossPay/{page}")
	public String getBillList(@PathVariable("page") long page, Model model, ExpressBillParamsVO4Query params, HttpServletRequest request) {
		User user = getSessionUser();
		params.setPage(page);
		params.setUser(user);
		params.setOperFlag(ExpressBillOperFlag.AcrossProvincePay.getValue());
		params.setBillType(ExpressBillTypeEnum.AcrossProvincePayableBill.getValue());
		Map<String, Object> map = expressAcrossPayFreightCheckService.getFreightBillList(params);
		UserInfo userInfo = expressAcrossPayFreightCheckService.initUserInfo(user);
		Long recordCount = (Long) map.get("count");
		cn.explink.util.Page page_obj = new cn.explink.util.Page(recordCount.intValue(), page, cn.explink.util.Page.ONE_PAGE_NUMBER);
		model.addAttribute("billList", map.get("list"));
		model.addAttribute("params", params);
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("page", page);
		model.addAttribute("page_obj", page_obj);
		return "express/acrossPayProvinceFreight/acrossPayfreightCheck";
	}

	@RequestMapping("/deleteBillById4AcrossPay")
	@ResponseBody
	public ExpressOpeAjaxResult deleteCustBillById(HttpServletRequest request) {
		Long billId = request.getParameter("billId") == null ? Long.valueOf(0) : Long.valueOf(request.getParameter("billId"));
		Integer billType = ExpressBillOperFlag.AcrossProvincePay.getValue();
		ExpressOpeAjaxResult res = expressAcrossPayFreightCheckService.deleteBillByBillId(billId, billType);
		return res;
	}

	@RequestMapping("/createBillRecord4AcrossPay")
	public ModelAndView createBillRecord4AcrossPay(HttpServletRequest request, ExpressBillParams4Create params) {
		User user = getSessionUser();
		params.setUser(user);
		UserInfo userInfo = expressAcrossPayFreightCheckService.initUserInfo(user);
		params.setReceivableProvinceName(userInfo.getProvinceName());// 应收省份
		params.setReceProvince4Create(userInfo.getProvinceId());
		Map<Long, String> provincesMap = expressAcrossPayFreightCheckService.initProvince();
		params.setPayableProvinceName(provincesMap.get(params.getPayProvince4Create()));
		params.setOpeFlag(ExpressBillOperFlag.AcrossProvincePay.getValue());
		params.setBillType(ExpressBillTypeEnum.AcrossProvincePayableBill.getValue());

		ModelAndView view = new ModelAndView("express/acrossPayProvinceFreight/editAcrossPayBillInfo");

		ExpressOpeAjaxResult result = expressAcrossPayFreightCheckService.createBillAlong(params);
		if (result.getStatus()) {
			view.addObject("expressBill", result.getObj());
			view.addObject("cwbListItem", new ArrayList<CwbOrderBillInfo>());
			view.addObject("total", 0);
			view.addObject("pageTotal", 0);
			view.addObject("pageno", 0);
			view.addObject("createParams", params);
		} else {
			view = new ModelAndView("express/customerfreight/norecord");
			view.addObject("errorMsg", result.getMsg());
		}
		return view;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/switch2EditView4AcrossPay")
	public ModelAndView switch2EditView(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView view = new ModelAndView("express/acrossPayProvinceFreight/editAcrossPayBillInfo");
		Long billId = request.getParameter("billId") == null ? Long.valueOf(0) : Long.valueOf(request.getParameter("billId"));
		Long page = request.getParameter("page") == null ? Long.valueOf(1) : Long.valueOf(request.getParameter("page"));
		Map<String, Object> map = expressAcrossPayFreightCheckService.getEditViewBillInfo(billId, page, ExpressEffectiveEnum.Effective.getValue());

		List<CwbOrderBillInfo> list = (List<CwbOrderBillInfo>) map.get("list");
		Page<CwbOrderBillInfo> pageEntity = (Page<CwbOrderBillInfo>) map.get("page");

		view.addObject("expressBill", map.get("view"));
		view.addObject("cwbListItem", list);
		view.addObject("total", pageEntity.getTotalCount());
		view.addObject("pageTotal", pageEntity.getTotalPages());
		view.addObject("pageno", pageEntity.getPageNo());
		view.addObject("reasonNeed", 0);//不需要显示原因
		return view;
	}
	
	
	//保存按钮的操作
	@RequestMapping("/updateRecordByBillId4AcrossPay")
	@ResponseBody
	public ExpressOpeAjaxResult updateRecordById(HttpServletRequest request, HttpServletResponse response, ExpressBillParams4Create params) {
		Long billId = request.getParameter("billId") == null ? Long.valueOf(0) : Long.valueOf(request.getParameter("billId"));
		String remark = request.getParameter("remark") == null ? "" : request.getParameter("remark");
		ExpressOpeAjaxResult res = expressAcrossPayFreightCheckService.updateBillByBillId(billId, remark);
		return res;
	}
	
	/**
	 * 获取省份信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getSelectInfo4AcrossPay")
	@ResponseBody
	public List<SelectReturnVO> initPayAbleProvinceSelect(HttpServletRequest request, HttpServletResponse response) {
		Long currentBranchId = getSessionUser().getBranchid();
		List<SelectReturnVO> res = expressAcrossPayFreightCheckService.initReceAbleProvinceSelect(currentBranchId);
		return res;
	}
	
	/**
	 * 导入excel
	 * @param request
	 * @param response
	 * @param file
	 * @param params
	 * @return
	 */
	@RequestMapping("/importReceOrders")
	@ResponseBody
	public String importReceOrders(HttpServletRequest request, HttpServletResponse response, @RequestParam("Filedata") final MultipartFile file,ExpressPayImportParams params) {
		ExcelExtractor excelExtractor = this.getExcelExtractor(file);
		User user = getSessionUser();
		params.setUser(user);
		params.setDate(new Date());
		ExpressOpeAjaxResult ajaxJson = new ExpressOpeAjaxResult();
		try {
			if (excelExtractor != null) {
				ajaxJson = this.expressAcrossPayFreightCheckService.importExcelForBill(file.getInputStream(), excelExtractor,params);
			} else {
				ajaxJson.setStatus(false);
				ajaxJson.setMsg("不可识别的文件");
			}
		}catch (ExpressImportSelfException e) {
			logger.info("校验错误："+e.getMessage());
			ajaxJson.setStatus(false);
			ajaxJson.setMsg(e.getMessage());
		}catch (Exception e) {
			ajaxJson.setStatus(false);
			ajaxJson.setMsg("系统异常，请联系管理员...");
			logger.info("系统异常："+e.getMessage());
		}
		
		return Tools.obj2json(ajaxJson);
	}
	/**
	 * 匹配版本
	 * @param file
	 * @return
	 */
	private ExcelExtractor getExcelExtractor(MultipartFile file) {
		String originalFilename = file.getOriginalFilename();
		if (originalFilename.endsWith("xlsx")) {
			return this.excel2007Extractor;
		} else if (originalFilename.endsWith(".xls")) {
			return this.excel2003Extractor;
		}
		return null;
	}
	
	/**
	 * 导入之后和核对表格数据的刷新
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/switch2EditView4AcrossPayPart")
	public ModelAndView switch2EditView4AcrossPayPart(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView view = new ModelAndView("express/acrossPayProvinceFreight/importTableData");
		Long billId = request.getParameter("billId") == null ? Long.valueOf(0) : Long.valueOf(request.getParameter("billId"));
		Long page = request.getParameter("page") == null ? Long.valueOf(1) : Long.valueOf(request.getParameter("page"));
		//是否需要显示原因
		Map<String, Object> map = expressAcrossPayFreightCheckService.getEditViewBillInfo(billId, page,ExpressEffectiveEnum.UnEffective.getValue(),true);
		List<CwbOrderBillInfo> list = (List<CwbOrderBillInfo>) map.get("list");
		Page<CwbOrderBillInfo> pageEntity = (Page<CwbOrderBillInfo>) map.get("page");
		view.addObject("expressBill", map.get("view"));
		view.addObject("cwbListItem", list);
		view.addObject("total", pageEntity.getTotalCount());
		view.addObject("pageTotal", pageEntity.getTotalPages());
		view.addObject("pageno", pageEntity.getPageNo());
		view.addObject("reasonNeed", 0);//不需要显示原因
		return view;
	}
	
	/**
	 * 核对数据
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/verifyImportRecords")
	public ModelAndView verifyImportRecords(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView view = new ModelAndView("express/acrossPayProvinceFreight/importTableData");
		Long billId = request.getParameter("billId") == null ? Long.valueOf(0) : Long.valueOf(request.getParameter("billId"));
		Long page = request.getParameter("page") == null ? Long.valueOf(1) : Long.valueOf(request.getParameter("page"));
		
		Map<String, Object> map = expressAcrossPayFreightCheckService.verifyRecords(billId, page,ExpressEffectiveEnum.UnEffective.getValue());
		
		List<CwbOrderBillInfo> list = (List<CwbOrderBillInfo>) map.get("list");
		Page<CwbOrderBillInfo> pageEntity = (Page<CwbOrderBillInfo>) map.get("page");
		
		view.addObject("expressBill", map.get("view"));
		view.addObject("cwbListItem", list);
		view.addObject("total", pageEntity.getTotalCount());
		view.addObject("pageTotal", pageEntity.getTotalPages());
		view.addObject("pageno", pageEntity.getPageNo());
		view.addObject("reasonNeed", 1);//需要显示原因
		return view;
	}

}
