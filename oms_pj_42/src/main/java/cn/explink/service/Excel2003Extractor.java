package cn.explink.service;

import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cn.explink.exception.BadExcelException;

@Component
public class Excel2003Extractor extends ExcelExtractor {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	protected List<Object> getRows(InputStream fis) {
		List<Object> rows = new ArrayList<Object>();
		try {
			HSSFWorkbook xwb = null;
			try {
				xwb = new HSSFWorkbook(fis);
			} catch (RuntimeException e) {
				e.printStackTrace();
				logger.error("文件异常，{}", e.getMessage());
				throw new BadExcelException();
			}
			HSSFSheet sheet = xwb.getSheetAt(0);

			logger.info("开始解析");

			int rowindex = 1;
			while (true) {
				HSSFRow row = sheet.getRow(rowindex++); // 从第二行开始，取出每一行
				if (row == null) {
					break;
				}
				logger.info("解析 excel数据第" + (rowindex - 1) + "行");
				HSSFCell xCellfirst1 = row.getCell(0);
				if (xCellfirst1 == null) {
					break;
				}
				rows.add(row);

			}
			logger.info("解析excel结束：" + "合计" + (rowindex - 1) + "条");
		} catch (Exception e) {
			e.printStackTrace();

		}
		return rows;
	}

	protected Map getRowsTitle(InputStream fis) {
		Map rows = new HashMap();
		try {
			HSSFWorkbook xwb = null;
			try {
				xwb = new HSSFWorkbook(fis);
			} catch (RuntimeException e) {
				e.printStackTrace();
				logger.error("文件异常，{}", e.getMessage());
				throw new BadExcelException();
			}
			HSSFSheet sheet = xwb.getSheetAt(0);

			logger.info("开始解析");

			int rowindex = 0;
			while (true) {
				HSSFRow row = sheet.getRow(0); // 从第二行开始，取出每一行
				if (row == null) {
					break;
				}

				HSSFCell xCellLast = row.getCell(rowindex);
				if (xCellLast == null) {
					break;
				}
				String titleName = row.getCell(rowindex).getStringCellValue();
				rows.put(titleName, rowindex + 1);
				rowindex++;
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return rows;
	}

	protected String getXRowCellData(Object row2, int columnNum) {

		HSSFRow row = (HSSFRow) row2;
		HSSFCell cell = row.getCell((columnNum - 1));
		if (cell == null) {
			return "";
		} // 判断取到的cell值是否为null
		String cellvalue = "";
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:
			cellvalue = cell.getStringCellValue();
			break;
		case HSSFCell.CELL_TYPE_NUMERIC: // 数字
			cellvalue = cell.getNumericCellValue() + "";
			if (cellvalue.indexOf(".") != -1 && cellvalue.indexOf("E") != -1) {
				java.text.DecimalFormat df = new java.text.DecimalFormat();
				try {
					cellvalue = df.parse(cellvalue).toString();
				} catch (ParseException e) {
					// return orignale string
				}
			}
			break;
		case HSSFCell.CELL_TYPE_FORMULA:
			cellvalue = cell.getCellFormula() + "";
			break;
		default:
			cellvalue = "";
			break;
		}
		cellvalue = strtovalid(cellvalue);

		cellvalue.replace(" ", "").replace(",", "").replace(".", "").replace("，", "").replace("。", "");
		return cellvalue;

	}

}
