package cn.explink.controller.fnc;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.core.common.model.json.DataGridReturn;
import cn.explink.core.utils.PoiExcelUtils;
import cn.explink.core.utils.PoiExcelUtils.ColDef;
import cn.explink.dao.CustomerDAO;
import cn.explink.domain.OrderLifeCycleReportVO;
import cn.explink.enumutil.CwbOrderLifeCycleTypeIdEnum;
import cn.explink.service.fnc.OrderLifeCycleReportService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.Tools;

@Controller
@RequestMapping("/orderlifecycle")
public class OrderLifeCycleReportController {
	
	private Logger logger = LoggerFactory
			.getLogger(OrderLifeCycleReportController.class);
	
	@Autowired
	private CustomerDAO customerDao;
	
	@Autowired
	private OrderLifeCycleReportService orderLifeCycleReportService;
	
	@RequestMapping("/index")
	public String index(Model model){
		
		model.addAttribute("customerList", customerDao.getAllCustomers());
		model.addAttribute("nowdate", DateTimeUtil.getDateBeforeDay(1));//set to the day before current date
		
		return "/orderlifecycle/index";
	}
	
	/**
	 * 签收站点余额报表数据
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/list")
	@ResponseBody
	public void list(Model model,
			@RequestParam(value = "customers", required = true, defaultValue="") String customers,
			@RequestParam(value = "queryDate", required = true, defaultValue="") String queryDate,
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		DataGridReturn dg = this.orderLifeCycleReportService.getOrderLifecycleReportData(customers,queryDate);
		Tools.outData2Page(Tools.obj2json(dg), response);
		
	}
	
	
	@RequestMapping("/getCwbOrderDetailList")
	@ResponseBody
	public void getCwbOrderDetailList(Model model,
			@RequestParam(value = "page", required = false, defaultValue="1") int page,
			@RequestParam(value = "pageSize", required = false, defaultValue="10") int pageSize,
			@RequestParam(value = "customerid", required = false, defaultValue="0") long customerid,
			@RequestParam(value = "typeid", required = false, defaultValue="0") int typeid,
			@RequestParam(value = "reportdate", required = false, defaultValue="0") int reportdate,
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		OrderLifeCycleReportVO reportVO = new OrderLifeCycleReportVO();
		reportVO.setTypeid(typeid);
		reportVO.setCustomerid(customerid);
		reportVO.setReportdate(reportdate);
		
		DataGridReturn dg = this.orderLifeCycleReportService.getCwbOrderDetail(reportVO, page, pageSize);
		Tools.outData2Page(Tools.obj2json(dg), response);
	}
	
	/**
	 * 签收单个站点余额报表数据导出
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/export2Excel")
	@ResponseBody
	public void export2Excel(
			@RequestParam(value = "customers", required = false, defaultValue="") String customers,
			@RequestParam(value = "queryDate", required = true, defaultValue="") String queryDate,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		DataGridReturn dg = this.orderLifeCycleReportService.getOrderLifecycleReportData(customers,queryDate);
		
		// 列定义
		List<ColDef> colDefs = PoiExcelUtils.getExcelColDefFromRequest(request);
		Workbook workbook = new HSSFWorkbook();
		/** create workbook header row */
		// head font style
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(font);
		style.setFillForegroundColor(IndexedColors.LIME.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setWrapText(true);
		Sheet sheet = workbook.createSheet("Sheet1");
		sheet.createFreezePane(0, 2, 0, 2);// 冻结标题
		Row row1 = sheet.createRow(0);
		Row row2 = sheet.createRow(1);
		
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));//merge 1 and 2 row
		
		
		for (int i = 0; i < colDefs.size(); i++) {
			
			ColDef colDef = colDefs.get(i);
			String title = colDef.getTitle();
			Cell cell = null;
			
			if(title.equals("票数") || title.equals("金额") ){
				cell = row2.createCell(i); // 2nd row
			}	
			else {
				cell = row1.createCell(i); // 1st row
			}
			
			cell.setCellValue(title);
			cell.setCellStyle(style);
			sheet.setColumnWidth(i, PoiExcelUtils.pixel2WidthUnits(colDef.getWidth()));
		}
		
		for (int i = 0, columnIndex = 1; i < CwbOrderLifeCycleTypeIdEnum.getMaxValue(); i++) {
			
			sheet.addMergedRegion(new CellRangeAddress(0, 0, columnIndex, columnIndex + 1)); //merge neighbor column
			
			Cell cell1 = row1.createCell(columnIndex);
			cell1.setCellValue(CwbOrderLifeCycleTypeIdEnum.getTextByValue((i+1)));
			cell1.setCellStyle(style);
			sheet.setColumnWidth(columnIndex, PoiExcelUtils.pixel2WidthUnits(100));
			
			columnIndex += 2;
		}

		/** create data rows */
		for (int i = 1; i < (dg.getRows().size() + 1); i++) {
			Row row = sheet.createRow(i + 1);
			Object bean = dg.getRows().get(i - 1);
			for (int j = 0; j < colDefs.size(); j++) {
				Cell cell = row.createCell(j);
				String beanProp = colDefs.get(j).getField();
				try {
					Object cellValue = null;
					cellValue = PropertyUtils.getProperty(bean, beanProp);
					cell.setCellValue(ConvertUtils.convert(cellValue));
				} catch (Exception e) {
					this.logger.warn("Failed to add bean property to excel, will ignore. Property: " + beanProp + " of " + bean, e);
					cell.setCellValue("");
				}
			}
		}

		/** create 汇总 rows */
		for (int i = 1; i < (dg.getFooter().size() + 1); i++) {
			Row row = sheet.createRow(dg.getRows().size() + 2);
			Object bean = dg.getFooter().get(i - 1);
			for (int j = 0; j < colDefs.size(); j++) {
				Cell cell = row.createCell(j);
				String beanProp = colDefs.get(j).getField();
				try {
					Object cellValue = null;
					cellValue = PropertyUtils.getProperty(bean, beanProp);
					cell.setCellValue(ConvertUtils.convert(cellValue));
				} catch (Exception e) {
					this.logger.warn("Failed to add bean property to excel, will ignore. Property: " + beanProp + " of " + bean, e);
					cell.setCellValue("");
				}
			}
		}

		// 向客户端写文件
		PoiExcelUtils.setExcelResponseHeader(response, "ord_lifecycle" + DateTimeUtil.formatDate(new Date()) + ".xls");
		OutputStream out = response.getOutputStream();
		workbook.write(out);
		out.flush();
	}
	
	@RequestMapping("/manualtrigger")
	public String toManualTriggerPage(Model model,HttpServletRequest request){
		
		String token = request.getParameter("token");
		if(token != null && token.equals("dmpdmp123456789")){
			model.addAttribute("nowdate", DateTimeUtil.getNowDate());//set to the day before current date
			return "/orderlifecycle/manualtrigger";
		}else{
			return "/";
		}
	}
	
	@RequestMapping("/gencwbdetails")
	@ResponseBody
	public Map<String,Object> genOrderSnapshot(Model model, @RequestParam(value = "date", required = true) String date
			,@RequestParam(value = "batchSize", required = true, defaultValue="100") int batchSize
			){
		
		Map<String,Object> result = new HashMap<String,Object>();
		
		try {
			long st = System.currentTimeMillis();
			orderLifeCycleReportService.genLifeCycleOrderDetail(batchSize, date);
			
			long et = System.currentTimeMillis() - st;
			
			result.put("success", "true");
			result.put("total_spend_time" , et + "ms");
			
		} catch (Exception e) {
			result.put("success", "false");
			result.put("errmsg",e.getMessage());
		}
		return result;
	}
	
	@RequestMapping("/genrpt")
	@ResponseBody
	public Map<String,Object> genOrderSnapshot(Model model, @RequestParam(value = "date", required = true) String date){
		
		
		Map<String,Object> result = new HashMap<String,Object>();
		
		try {
			long st = System.currentTimeMillis();
			
			orderLifeCycleReportService.genLifeCycleReport(date);
			
			long et = System.currentTimeMillis() - st;
			
			result.put("success", "true");
			result.put("total_spend_time" , et + "ms");
			
		} catch (Exception e) {
			result.put("success", "false");
			result.put("errmsg",e.getMessage());
		}
		return result;
	}
	
}
