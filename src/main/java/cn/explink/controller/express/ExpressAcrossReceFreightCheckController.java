package cn.explink.controller.express;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.explink.domain.User;
import cn.explink.domain.VO.express.CwbOrderBillInfo;
import cn.explink.domain.VO.express.ExpressBillParams4Create;
import cn.explink.domain.VO.express.ExpressBillParamsVO4Query;
import cn.explink.domain.VO.express.ExpressBillSummary;
import cn.explink.domain.VO.express.ExpressOpeAjaxResult;
import cn.explink.domain.VO.express.SelectReturnVO;
import cn.explink.domain.VO.express.UserInfo;
import cn.explink.enumutil.express.ExpressBillOperFlag;
import cn.explink.enumutil.express.ExpressBillTypeEnum;
import cn.explink.service.express.ExpressAcrossReceFreightCheckService;
import cn.explink.util.express.Page;
/**
 * 跨省运费对账
 * @author jiangyu 2015年8月14日
 *
 */
@Controller
@RequestMapping("/acrossReceFreightCheck")
public class ExpressAcrossReceFreightCheckController extends ExpressBillCommonController{
	@Autowired
	ExpressAcrossReceFreightCheckService expressAcrossReceFreightCheckService;
	
	
	@RequestMapping("/index")
	public String index(Model model,ExpressBillParamsVO4Query params,HttpServletRequest request) {
		User user = getSessionUser();
		params.setUser(user);
		UserInfo userInfo = expressAcrossReceFreightCheckService.initUserInfo(user);
		model.addAttribute("params", params);
		model.addAttribute("userInfo", userInfo);
		return "express/acrossProvinceFreight/acrossRecefreightCheck";
	}
	
	@RequestMapping("/billList4AcrossRece/{page}")
	public String getBillList(@PathVariable("page") long page, Model model,ExpressBillParamsVO4Query params,HttpServletRequest request) {
		User user = getSessionUser();
		params.setPage(page);
		params.setUser(user);
		params.setOperFlag(ExpressBillOperFlag.AcrossProvinceRece.getValue());
		params.setBillType(ExpressBillTypeEnum.AcrossProvinceReceivableBill.getValue());
		Map<String, Object> map  = expressAcrossReceFreightCheckService.getFreightBillList(params);
		UserInfo userInfo = expressAcrossReceFreightCheckService.initUserInfo(user);
		Long recordCount = (Long) map.get("count");
		cn.explink.util.Page page_obj = new cn.explink.util.Page(recordCount.intValue(), page, cn.explink.util.Page.ONE_PAGE_NUMBER);
		model.addAttribute("billList", map.get("list"));
		model.addAttribute("params", params);
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("page", page);
		model.addAttribute("page_obj", page_obj);
		return "express/acrossProvinceFreight/acrossRecefreightCheck";
	}
	
	@RequestMapping("/deleteBillById4AcrossRece")
	@ResponseBody
	public ExpressOpeAjaxResult deleteCustBillById(HttpServletRequest request) {
		Long billId = request.getParameter("billId")==null?Long.valueOf(0):Long.valueOf(request.getParameter("billId"));
		Integer billType = ExpressBillOperFlag.AcrossProvinceRece.getValue();
		ExpressOpeAjaxResult res = expressAcrossReceFreightCheckService.deleteBillByBillId(billId,billType);
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/preScanView4AcrossRece")
	public ModelAndView preScanView(HttpServletRequest request, HttpServletResponse response,ExpressBillParams4Create params) {
		params.setOpeFlag(ExpressBillOperFlag.AcrossProvinceRece.getValue());
		params.setIsPreScan(true);
		Map<String, Object> map = expressAcrossReceFreightCheckService.getCwbInfoList(params);
		Boolean resFlag = (Boolean) map.get("isCorrect");
		ModelAndView view = new ModelAndView("express/acrossProvinceFreight/preScanAcrossReceList");
		if(resFlag){
			List<CwbOrderBillInfo> list = (List<CwbOrderBillInfo>) map.get("list");
			Page<CwbOrderBillInfo> page = (Page<CwbOrderBillInfo>) map.get("page");
			ExpressBillSummary summary = (ExpressBillSummary) map.get("summary");
			view.addObject("cwbListItem", list);
			view.addObject("total", page.getTotalCount());
			view.addObject("pageTotal", page.getTotalPages());
			view.addObject("pageno", params.getPage());
			view.addObject("createParams", params);
			view.addObject("summary", summary);
		}else {
			view = new ModelAndView("express/customerfreight/norecord");
			view.addObject("errorMsg", map.get("msg"));
		}
		return view;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/createBillRecord4AcrossRece")
	public ModelAndView createCustBillRecord(HttpServletRequest request,ExpressBillParams4Create params) {
		User user = getSessionUser();
		params.setUser(user);
		UserInfo userInfo = expressAcrossReceFreightCheckService.initUserInfo(user);
		params.setReceivableProvinceName(userInfo.getProvinceName());//应收省份
		params.setReceProvince4Create(userInfo.getProvinceId());
		Map<Long, String> provincesMap = expressAcrossReceFreightCheckService.initProvince();
		params.setPayableProvinceName(provincesMap.get(params.getPayProvince4Create()));
		params.setOpeFlag(ExpressBillOperFlag.AcrossProvinceRece.getValue());
		params.setBillType(ExpressBillTypeEnum.AcrossProvinceReceivableBill.getValue());
		
		ModelAndView view = new ModelAndView("express/acrossProvinceFreight/editAcrossReceBillInfo");
		
		ExpressOpeAjaxResult result = expressAcrossReceFreightCheckService.createBill(params);
		if(result.getStatus()){
			params.setIsPreScan(false);//编辑之后的记录
			Map<String, Object> map = expressAcrossReceFreightCheckService.getCwbInfoList(params);
			List<CwbOrderBillInfo> list = (List<CwbOrderBillInfo>) map.get("list");
			Page<CwbOrderBillInfo> page = (Page<CwbOrderBillInfo>) map.get("page");
			view.addObject("expressBill", result.getObj());
			view.addObject("cwbListItem", list);
			view.addObject("total", page.getTotalCount());
			view.addObject("pageTotal", page.getTotalPages());
			view.addObject("pageno", params.getPage());
			view.addObject("createParams", params);
		}else {
			view = new ModelAndView("express/customerfreight/norecord");
			view.addObject("errorMsg", result.getMsg());
		}
		return view;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/switch2EditView4AcrossRece")
	public ModelAndView switch2EditView(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView view = new ModelAndView("express/acrossProvinceFreight/editAcrossReceBillInfo");
		Long billId = request.getParameter("billId")==null?Long.valueOf(0):Long.valueOf(request.getParameter("billId"));
		Long page = request.getParameter("page")==null?Long.valueOf(1):Long.valueOf(request.getParameter("page"));
		Integer opeFlag = ExpressBillOperFlag.AcrossProvinceRece.getValue();
		Map<String, Object> map = expressAcrossReceFreightCheckService.getEditViewBillInfo(billId,page,opeFlag);
		
		List<CwbOrderBillInfo> list = (List<CwbOrderBillInfo>) map.get("list");
		Page<CwbOrderBillInfo> pageEntity = (Page<CwbOrderBillInfo>) map.get("page");
		
		view.addObject("expressBill", map.get("view"));
		view.addObject("cwbListItem", list);
		view.addObject("total", pageEntity.getTotalCount());
		view.addObject("pageTotal", pageEntity.getTotalPages());
		view.addObject("pageno", pageEntity.getPageNo());
		return view;
	}
	
	@RequestMapping("/updateRecordByBillId4AcrossRece")
	@ResponseBody
	public ExpressOpeAjaxResult updateRecordById(HttpServletRequest request, HttpServletResponse response,ExpressBillParams4Create params) {
		Long billId = request.getParameter("billId")==null?Long.valueOf(0):Long.valueOf(request.getParameter("billId"));
		String remark = request.getParameter("remark")==null?"":request.getParameter("remark");
		ExpressOpeAjaxResult res = expressAcrossReceFreightCheckService.updateBillByBillId(billId,remark);
		return res;
	}
	
	@RequestMapping("/getSelectInfo4AcrossRece")
	@ResponseBody
	public List<SelectReturnVO> initPayAbleProvinceSelect(HttpServletRequest request, HttpServletResponse response) {
		Long currentBranchId = getSessionUser().getBranchid();
		List<SelectReturnVO> res = expressAcrossReceFreightCheckService.initPayAbleProvinceSelect(currentBranchId);
		return res;
	}
}
