package cn.explink.util;

import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 * 导出Excel工具类
 * 
 * @author goodboyloveyy
 * 
 */
public abstract class ExcelUtilsOld {

	/**
	 * 生成excel的通用模版
	 * 
	 * @param response
	 *            响应,设置生成的文件类型,文件头编码方式和文件名,以及输出
	 * @param firstLine
	 *            标题字符串数组 String[]
	 * @param sheetName
	 *            工作表名
	 * @param fileName
	 *            文件名
	 */

	public void excel(HttpServletResponse response, String[] firstLine, String sheetName, String fileName) throws Exception {

		SXSSFWorkbook wb = new SXSSFWorkbook(); // excel文件,一个excel文件包含多个表

		Sheet sheet = wb.createSheet(); // 表，一个表包含多个行

		wb.setSheetName(0, sheetName); // 设置sheet中文编码

		// 设置字体等样式
		Font font = wb.createFont();

		// font.setFontHeightInPoints((short) 12);

		font.setFontName("Courier New");

		CellStyle style = wb.createCellStyle();

		style.setFont(font);

		// style.setWrapText(true);

		// style.setAlignment(HSSFCellStyle.ALIGN_LEFT);

		// style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		// 单元格

		Row row = sheet.createRow(0); // 由HSSFSheet生成行

		row.setHeightInPoints((float) 15); // 设置行高

		// 生成首行标题

		for (short j = 0; j < firstLine.length; j++) {

			Cell cell = row.createCell(j); // 由行生成单元格

			cell.setCellStyle(style); // 设置单元格样式

			cell.setCellValue(firstLine[j]);

			// sheet.setColumnWidth(j, (short) (5000)); //设置列宽

		}

		fillData(sheet, style); // 该方法由具体调用时进行实现

		// 导出
		OutputStream out = null;

		response.setContentType("application/x-msdownload"); // 设置生成的文件类型

		response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

		out = response.getOutputStream(); // 取得输出流

		wb.write(out); // 写入Excel
		out.close();

	}

	/**
	 * 该方法由生成具体的表格时去实现(方法体内进行填充数据)
	 * 
	 * @param sheet
	 * @param style
	 */
	public abstract void fillData(Sheet sheet, CellStyle style);

}
