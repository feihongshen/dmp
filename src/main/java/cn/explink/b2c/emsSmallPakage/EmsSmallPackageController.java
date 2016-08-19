package cn.explink.b2c.emsSmallPakage;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.ems.SendToEMSOrder;
import cn.explink.core.utils.PoiExcelUtils;
import cn.explink.core.utils.PoiExcelUtils.ColDef;
import cn.explink.domain.CwbOrder;
import cn.explink.exception.CwbException;
import cn.explink.service.CwbOrderService;

import com.pjbest.splitting.aspect.DataSource;
import com.pjbest.splitting.routing.DatabaseType;

@Controller
@RequestMapping("/emsSmallPackage")
public class EmsSmallPackageController {
	
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	EmsSmallPakageService emsSmallPakageService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping("/print")
	public String print() {
		return"/emssmallpackage/emsprint";
	}
	
	@RequestMapping("/query")
	public String query() {
		return"/emssmallpackage/emsquery";
	}
	
	@RequestMapping("/queryCwb")
	@ResponseBody
	public Map<String, Object> querycwb(Model model,
			@RequestParam(value = "scancwb", required = true, defaultValue="") String scancwb,
			@RequestParam(value = "printcwb", required = true, defaultValue="") String printcwb,
			@RequestParam(value = "rebingcwb", required = true, defaultValue="") String rebingcwb,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		boolean isPrint = false;
		try {
			scancwb = scancwb.trim();
			printcwb = printcwb.trim();
			rebingcwb = rebingcwb.trim();
			
			CwbOrder cwbOrder = this.emsSmallPakageService.validateCwb(scancwb);
			if ("1".equals(printcwb)) {//打印邮政面单
				isPrint = true;
			}
			List<SendToEMSOrder> list= null;
			if ("1".equals(rebingcwb)) { //重新绑定邮政运单号
				list = this.emsSmallPakageService.getTransListByCwb(scancwb,cwbOrder.getCwb());
			}
			map.put("cwbOrder", cwbOrder);//订单信息
			map.put("list", list);//订单邮政运单关系列表
		} catch (CwbException e) {
			logger.error("绑定邮政小包运单号发生异常", e);
			map.put("result", "{\"result\":\""+ e.getMessage() +"\"}");
			map.put("list", null);
			return map;
		} catch (Exception e) {
			logger.error("绑定邮政小包运单号发生异常", e);
			map.put("result", "{\"result\":\"绑定邮政小包运单号发生异常\"}");
			map.put("list", null);
			return map;
		}
		map.put("isPrint", isPrint);
		map.put("result", "{\"result\":\"success\"}");
		return map;
	}
	
	@RequestMapping("/bingCwb")
	@ResponseBody
	public Map<String,Object> bingcwb(Model model,
			@RequestParam(value = "scancwb", required = true, defaultValue="") String scancwb,
			@RequestParam(value = "scantranscwb", required = true, defaultValue="") String scantranscwb,
			@RequestParam(value = "emsscancwb", required = true, defaultValue="") String emsscancwb,
			HttpServletRequest request, HttpServletResponse response) {
		scancwb = scancwb.trim();
		scantranscwb = scantranscwb.trim();
		emsscancwb = emsscancwb.trim();
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			this.emsSmallPakageService.bingCwb(scancwb, scantranscwb, emsscancwb);
			map.put("list", this.emsSmallPakageService.getEMSViewListByTransCwb("","","","",""));
		} catch(DuplicateKeyException e) {
			logger.error("绑定邮政小包运单号发生异常", e);
			map.put("result", "{\"result\":\"订单/运单已经绑定邮政运单号！\"}");
			map.put("list", null);
			return map;
		} catch (CwbException e) {
			logger.error("绑定邮政小包运单号发生异常", e);
			map.put("result", "{\"result\":\""+ e.getMessage() +"\"}");
			map.put("list", null);
			return map;
		} catch (Exception e) {
			logger.error("绑定邮政小包运单号发生异常", e);
			map.put("result", "{\"result\":\"绑定邮政小包运单号发生异常\"}");
			map.put("list", null);
			return map;
		}
		map.put("result", "{\"result\":\"success\"}");
		return map;
	}
	
	
	@RequestMapping("/rebingCwb")
	@ResponseBody
	public Map<String,Object> rebingcwb(Model model,
			@RequestParam(value = "emscwb", required = true, defaultValue="") String emscwb,
			@RequestParam(value = "emscwbOld", required = true, defaultValue="") String emscwbOld,
			HttpServletRequest request, HttpServletResponse response) {
		emscwb = emscwb.trim();
		emscwbOld = emscwbOld.trim();
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			this.emsSmallPakageService.rebingCwb(emscwb, emscwbOld);
			map.put("list", this.emsSmallPakageService.getEMSViewListByTransCwb("","","","",""));
		} catch (CwbException e) {
			logger.error("绑定邮政小包运单号发生异常", e);
			map.put("result", "{\"result\":\""+ e.getMessage() +"\"}");
			map.put("list", null);
			return map;
		} catch (Exception e) {
			logger.error("绑定邮政小包运单号发生异常", e);
			map.put("result", "{\"result\":\"绑定邮政小包运单号发生异常\"}");
			map.put("list", null);
			return map;
		}
		map.put("result", "{\"result\":\"success\"}");
		return map;
	}
	
	
	@RequestMapping("/queryCwbList")
	@ResponseBody
	public Map<String, Object> querycwbList(Model model,
			@RequestParam(value = "cwbtype", required = true, defaultValue="") String cwbtype,
			@RequestParam(value = "querycwb", required = true, defaultValue="") String querycwb,
			@RequestParam(value = "starttime", required = true, defaultValue="") String starttime,
			@RequestParam(value = "endtime", required = true, defaultValue="") String endtime,
			@RequestParam(value = "status", required = true, defaultValue="") String status,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			map.put("list", this.emsSmallPakageService.getEMSViewListByTransCwb(cwbtype,querycwb,starttime,endtime,status));
		} catch (CwbException e) {
			logger.error("查询邮政订单发生异常", e);
			map.put("result", "{\"result\":\""+ e.getMessage() +"\"}");
			map.put("list", null);
			return map;
		} catch (Exception e) {
			logger.error("绑定邮政小包运单号发生异常", e);
			map.put("result", "{\"result\":\"查询邮政订单发生异常\"}");
			map.put("list", null);
			return map;
		}
		map.put("result", "{\"result\":\"success\"}");
		return map;
	}
	
	/**
	 * 签收单个站点余额报表数据导出
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/export")
	@ResponseBody
	public void export2Excel(
			@RequestParam(value = "cwbtype", required = true, defaultValue="") String cwbtype,
			@RequestParam(value = "querycwb", required = true, defaultValue="") String querycwb,
			@RequestParam(value = "starttime", required = true, defaultValue="") String starttime,
			@RequestParam(value = "endtime", required = true, defaultValue="") String endtime,
			@RequestParam(value = "status", required = true, defaultValue="") String status,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		//DataGridReturn dg = this.orderLifeCycleReportService.getOrderLifecycleReportData(customers,queryDate);
		List<EmsSmallPackageViewVo> list = this.emsSmallPakageService.getEMSViewListByTransCwb(cwbtype,querycwb,starttime,endtime,status);
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
		Row row1 = sheet.createRow(0);
		
		
		for (int i = 0; i < colDefs.size(); i++) {
			ColDef colDef = colDefs.get(i);
			String title = colDef.getTitle();
			Cell cell = row1.createCell(i);
			cell.setCellValue(title);
			cell.setCellStyle(style);
			sheet.setColumnWidth(i, PoiExcelUtils.pixel2WidthUnits(colDef.getWidth()));
		}
		
		/** create data rows */
		for (int i = 0; i < list.size(); i++) {
			Row row = sheet.createRow(i+1);
			Object bean = list.get(i);
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
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		PoiExcelUtils.setExcelResponseHeader(response, "ems_cwb_" + df.format(new Date()) + ".xls");
		OutputStream out = response.getOutputStream();
		workbook.write(out);
		out.flush();
	}
	
}