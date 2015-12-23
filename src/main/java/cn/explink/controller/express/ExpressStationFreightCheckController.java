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
import cn.explink.enumutil.express.ExpressBillOperFlag;
import cn.explink.enumutil.express.ExpressBillTypeEnum;
import cn.explink.service.express.ExpressStationFreightCheckService;
import cn.explink.util.express.Page;
/**
 * 站点运费对账
 * @author jiangyu 2015年8月14日
 *
 */
@Controller
@RequestMapping("/stationFreightCheck")
public class ExpressStationFreightCheckController extends ExpressBillCommonController {
	@Autowired
	ExpressStationFreightCheckService expressStationFreightCheckService;
	
	@RequestMapping("/index")
	public String index(Model model,ExpressBillParamsVO4Query params,HttpServletRequest request) {
		return "express/stationfreight/stationfreightCheck";
	}
	
	@RequestMapping("/billList4Station/{page}")
	public String getBillList(@PathVariable("page") long page, Model model,ExpressBillParamsVO4Query params,HttpServletRequest request) {
		User user = getSessionUser();
		params.setPage(page);
		params.setUser(user);
		params.setOperFlag(ExpressBillOperFlag.Station.getValue());
		params.setBillType(ExpressBillTypeEnum.StationFreightBill.getValue());
		Map<String, Object> map  = expressStationFreightCheckService.getFreightBillList(params);
		Long recordCount = (Long) map.get("count");
		cn.explink.util.Page page_obj = new cn.explink.util.Page(recordCount.intValue(), page, cn.explink.util.Page.ONE_PAGE_NUMBER);
		model.addAttribute("billList", map.get("list"));
		model.addAttribute("params", params);
		model.addAttribute("page", page);
		model.addAttribute("page_obj", page_obj);
		return "express/stationfreight/stationfreightCheck";
	}
	
	@RequestMapping("/deleteBillById4Station")
	@ResponseBody
	public ExpressOpeAjaxResult deleteCustBillById(HttpServletRequest request) {
		Long billId = request.getParameter("billId")==null?Long.valueOf(0):Long.valueOf(request.getParameter("billId"));
		Integer billType = ExpressBillOperFlag.Station.getValue();
		ExpressOpeAjaxResult res = expressStationFreightCheckService.deleteBillByBillId(billId,billType);
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/preScanView4Station")
	public ModelAndView preScanView(HttpServletRequest request, HttpServletResponse response,ExpressBillParams4Create params) {
		params.setOpeFlag(ExpressBillOperFlag.Station.getValue());
		params.setIsPreScan(true);
		Map<String, Object> map = expressStationFreightCheckService.getCwbInfoList(params);
		Boolean resFlag = (Boolean) map.get("isCorrect");
		ModelAndView view = new ModelAndView("express/customerfreight/preScanList");
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
	@RequestMapping("/createBillRecord4Station")
	public ModelAndView createCustBillRecord(HttpServletRequest request,ExpressBillParams4Create params) {
		User user = getSessionUser();
		params.setUser(user);
		params.setOpeFlag(ExpressBillOperFlag.Station.getValue());
		params.setBillType(ExpressBillTypeEnum.StationFreightBill.getValue());
		Map<Long, String> branchMap = expressStationFreightCheckService.getBranchName();
		//站点名称
		params.setBranchName(branchMap.get(params.getBranchId4Create()));
		
		ModelAndView view = new ModelAndView("express/stationfreight/editStationBillInfo");
		
		ExpressOpeAjaxResult result = expressStationFreightCheckService.createBill(params);
		
		if(result.getStatus()){
			params.setIsPreScan(false);//编辑之后的记录
			Map<String, Object> map = expressStationFreightCheckService.getCwbInfoList(params);
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
	@RequestMapping("/switch2EditView4Station")
	public ModelAndView switch2EditView(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView view = new ModelAndView("express/stationfreight/editStationBillInfo");
		Long billId = request.getParameter("billId")==null?Long.valueOf(0):Long.valueOf(request.getParameter("billId"));
		Long page = request.getParameter("page")==null?Long.valueOf(1):Long.valueOf(request.getParameter("page"));
		Integer opeFlag = ExpressBillOperFlag.Station.getValue();
		Map<String, Object> map = expressStationFreightCheckService.getEditViewBillInfo(billId,page,opeFlag);
		
		List<CwbOrderBillInfo> list = (List<CwbOrderBillInfo>) map.get("list");
		Page<CwbOrderBillInfo> pageEntity = (Page<CwbOrderBillInfo>) map.get("page");
		
		view.addObject("expressBill", map.get("view"));
		view.addObject("cwbListItem", list);
		view.addObject("total", pageEntity.getTotalCount());
		view.addObject("pageTotal", pageEntity.getTotalPages());
		view.addObject("pageno", pageEntity.getPageNo());
		return view;
	}
	
	@RequestMapping("/updateRecordByBillId4Station")
	@ResponseBody
	public ExpressOpeAjaxResult updateRecordById(HttpServletRequest request, HttpServletResponse response,ExpressBillParams4Create params) {
		Long billId = request.getParameter("billId")==null?Long.valueOf(0):Long.valueOf(request.getParameter("billId"));
		String remark = request.getParameter("remark")==null?"":request.getParameter("remark");
		ExpressOpeAjaxResult res = expressStationFreightCheckService.updateBillByBillId(billId,remark);
		return res;
	}
	
	@RequestMapping("/getSelectInfo4Station")
	@ResponseBody
	public List<SelectReturnVO> getStationSelectInfo(HttpServletRequest request, HttpServletResponse response) {
		List<SelectReturnVO> res = expressStationFreightCheckService.getStationSelectInfo();
		return res;
	}
	
}
