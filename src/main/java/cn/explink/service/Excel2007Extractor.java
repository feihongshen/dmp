package cn.explink.service;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cn.explink.exception.BadExcelException;

@Component
public class Excel2007Extractor extends ExcelExtractor {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	protected List<Object> getRows(InputStream fis) {
		List<Object> rows = new ArrayList<Object>();
		try {
			XSSFWorkbook xwb = null;
			try {
				xwb = new XSSFWorkbook(fis);
			} catch (RuntimeException e) {
				e.printStackTrace();
				logger.error("文件异常，{}", e.getMessage());
				throw new BadExcelException();
			}
			XSSFSheet sheet = xwb.getSheetAt(0);

			logger.info("开始解析");

			int rowindex = 1;
			while (true) {
				XSSFRow row = sheet.getRow(rowindex++); // 从第二行开始，取出每一行
				if (row == null) {
					break;
				}
				logger.info("解析 excel数据第" + (rowindex - 1) + "行");
				XSSFCell xCellfirst1 = row.getCell(0);
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

	protected String getXRowCellDateData(Object row2, int columnNum) {
		XSSFRow row = (XSSFRow) row2;
		XSSFCell cell = row.getCell((short) (columnNum - 1));
		if (cell == null) {
			return "";
		} // 判断取到的cell值是否为null

		String cellvalue = "";
		try {
			cellvalue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cell.getDateCellValue());
		} catch (Exception e) {

		}

		// cellvalue = strtovalid(cellvalue);

		return cellvalue;
	}

	protected String getXRowCellData(Object row2, int columnNum) {
		return getXRowCellData(row2, columnNum, false);
	}

	@Override
	protected String getXRowCellData(Object row2, int columnNum, boolean escapeAddress) {
		XSSFRow row = (XSSFRow) row2;
		XSSFCell cell = row.getCell((short) (columnNum - 1));
		if (cell == null) {
			return "";
		} // 判断取到的cell值是否为null
		String cellvalue = "";
		switch (cell.getCellType()) {
		case XSSFCell.CELL_TYPE_STRING:
			cellvalue = cell.getStringCellValue();
			break;
		case XSSFCell.CELL_TYPE_NUMERIC: // 数字
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
		case XSSFCell.CELL_TYPE_FORMULA:
			cellvalue = cell.getCellFormula() + "";
			break;
		default:
			cellvalue = "";
			break;
		}
		cellvalue = strtovalid(cellvalue);

		return cellvalue;
	}

}
