package cn.explink.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.core.common.model.json.DataGridReturn;
import cn.explink.core.utils.PoiExcelUtils;
import cn.explink.core.utils.PoiExcelUtils.ColDef;
import cn.explink.core.utils.WebUtils;
import cn.explink.domain.QueryCondition;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.service.IEmsOrderManagerService;

@RequestMapping("/emsOrderUnpushManager")
@Controller
public class EmsOrderUnpushController {
	
	@Autowired
	@Qualifier("emsOrderManagerService")
	private IEmsOrderManagerService emsOrderManagerService ;

	@RequestMapping(params = "index")
	public String index(Model model){
		model.addAttribute("customerList", this.emsOrderManagerService.queryAllCustomers()) ;
		model.addAttribute("orderTypes", CwbOrderTypeIdEnum.values()) ;
		return "/emsOrder/emsOrderUnpustList";
	}
	
	@RequestMapping(params = "query")
	@ResponseBody
	public void query(
			@RequestParam(value = "customerIds", required = true, defaultValue = "") String customerIds,
			@RequestParam(value = "orderType", required = false, defaultValue = "-2") int orderType,
			@RequestParam(value = "startTime", required = false, defaultValue ="") String startTime,
			@RequestParam(value = "endTime", required = false, defaultValue ="") String endTime,
			@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		QueryCondition queryCondition = buildQuery(request);
		DataGridReturn dg = this.emsOrderManagerService.queryEmsOrderList(queryCondition) ;
		cn.explink.util.Tools.outData2Page(Tools.obj2json(dg), response);
	}
	
	@RequestMapping(params = "exportReportExcel")
	@ResponseBody
	public void exportReportExcel(
			@RequestParam(value = "customerIds", required = true, defaultValue = "") String customerIds,
			@RequestParam(value = "orderType", required = false, defaultValue = "-2") int orderType,
			@RequestParam(value = "startTime", required = false, defaultValue ="") String startTime,
			@RequestParam(value = "endTime", required = false, defaultValue ="") String endTime,
			@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		QueryCondition qc = new QueryCondition();
		qc.setPageSize(10000);
		qc.setCurrentPage(WebUtils.getIntParam(request, "pageNumber", 1));
		qc.eq("order_type", orderType);
		qc.ge("start_time", startTime);
		qc.le("end_time", endTime);
		qc.in("customer_ids", customerIds);
		DataGridReturn dg = this.emsOrderManagerService.queryEmsOrderList(qc) ;
		List<ColDef> colDefs = PoiExcelUtils.getExcelColDefFromRequest(request);
		Workbook workbook = PoiExcelUtils.createExcelSheet(colDefs, dg.getRows());
		PoiExcelUtils.setExcelResponseHeader(response, "brance_report_export.xlsx");
		OutputStream out = response.getOutputStream();
		workbook.write(out);
		out.flush();
	}
	
	private QueryCondition buildQuery(HttpServletRequest request) {
		QueryCondition qc = new QueryCondition();
		qc.setPageSize(WebUtils.getIntParam(request, "pageSize", 10));
		qc.setCurrentPage(WebUtils.getIntParam(request, "pageNumber", 1));
		qc.eq("order_type", WebUtils.getIntParam(request, "orderType", -2));
		qc.ge("start_time", request.getParameter("startTime"));
		qc.le("end_time", request.getParameter("endTime"));
		qc.in("customer_ids", request.getParameter("customerIds"));
		return qc;
	}
}
