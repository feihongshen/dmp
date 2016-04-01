package cn.explink.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.explink.util.poi.excel.entity.ExcelTitle;

public class ExportUtil4Express {

	private static Logger logger = LoggerFactory.getLogger(ExportUtil4Express.class);
	
	public static void exportXls(HttpServletRequest request, HttpServletResponse response, List<?> list, Class<?> clz, String fileName) {
		response.setContentType("application/vnd.ms-excel");
		String codedFileName = null;
		OutputStream fOut = null;
		try {
			if (fileName.indexOf("流水号") > -1) {
				codedFileName = fileName;
			} else {
				codedFileName = fileName + "" + new Date().getTime();
			}
			// 根据浏览器进行转码，使其支持中文文件名
			if (BrowserUtils.isIE(request)) {
				response.setHeader("content-disposition", "attachment;filename=" + java.net.URLEncoder.encode(codedFileName, "UTF-8") + ".xls");
			} else {
				String newtitle = new String(codedFileName.getBytes("UTF-8"), "ISO8859-1");
				response.setHeader("content-disposition", "attachment;filename=" + newtitle + ".xls");
			}
			// 产生工作簿对象
			HSSFWorkbook workbook = null;
			workbook = ExcelExportUtil.exportExcel(new ExcelTitle(fileName, null, fileName), clz, list);
			fOut = response.getOutputStream();

			workbook.write(fOut);

		} catch (Exception e) {
			logger.error("", e);
		} finally {
			try {
				fOut.flush();
				fOut.close();
			} catch (IOException e) {
				logger.error("", e);
			}
		}
	}
}
