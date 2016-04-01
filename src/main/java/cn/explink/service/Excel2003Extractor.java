package cn.explink.service;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cn.explink.exception.BadExcelException;

@Component
public class Excel2003Extractor extends ExcelExtractor {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public List<Object> getRows(InputStream fis) {
		List<Object> rows = new ArrayList<Object>();
		try {
			HSSFWorkbook xwb = null;
			try {
				xwb = new HSSFWorkbook(fis);
			} catch (RuntimeException e) {
				this.logger.error("文件异常，{}", e);
				throw new BadExcelException();
			}
			HSSFSheet sheet = xwb.getSheetAt(0);
			this.logger.info("开始解析");

			int rowindex = 1;
			while (true) {
				HSSFRow row = sheet.getRow(rowindex++); // 从第二行开始，取出每一行
				if (row == null) {
					break;
				}
				this.logger.info("解析 excel数据第" + (rowindex - 1) + "行");
				HSSFCell xCellfirst1 = row.getCell(0);
				if (xCellfirst1 == null) {
					break;
				}
				rows.add(row);

			}
			this.logger.info("解析excel结束：" + "合计" + (rowindex - 1) + "条");
		} catch (Exception e) {
			logger.error("", e);
		}
		return rows;
	}

	/**
	 *
	 * @Title: getRows
	 * @description 从第rowindex行开始，获取所有数据行
	 * @author 刘武强
	 * @date  2015年10月10日下午4:48:16
	 * @param  @param fis
	 * @param  @param rowindex
	 * @param  @return
	 * @return  List<Object>
	 * @throws
	 */
	@Override
	public List<Object> getRows(InputStream fis, int rowindex) {
		List<Object> rows = new ArrayList<Object>();
		try {
			HSSFWorkbook xwb = null;
			try {
				xwb = new HSSFWorkbook(fis);
			} catch (RuntimeException e) {
				this.logger.error("文件异常，{}", e);
				throw new BadExcelException();
			}
			HSSFSheet sheet = xwb.getSheetAt(0);
			this.logger.info("开始解析");

			while (true) {
				HSSFRow row = sheet.getRow(rowindex++); // 从第二行开始，取出每一行
				if (row == null) {
					break;
				}
				this.logger.info("解析 excel数据第" + (rowindex - 1) + "行");
				HSSFCell xCellfirst1 = row.getCell(0);
				if (xCellfirst1 == null) {
					break;
				}
				rows.add(row);

			}
			this.logger.info("解析excel结束：" + "合计" + (rowindex - 1) + "条");
		} catch (Exception e) {
			logger.error("", e);
		}
		return rows;
	}

	@Override
	public String getXRowCellData(Object row2, int columnNum) {
		return this.getXRowCellData(row2, columnNum, false);
	}

	@Override
	protected String getXRowCellData(Object row2, int columnNum, boolean escapeAddress) {

		HSSFRow row = (HSSFRow) row2;
		HSSFCell cell = row.getCell((columnNum - 1));
		if (cell == null) {
			return "";
		} // 判断取到的cell值是否为null
		String cellvalue = "";
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			cellvalue = cell.getStringCellValue();
			break;
		case Cell.CELL_TYPE_NUMERIC: // 数字
			cellvalue = cell.getNumericCellValue() + "";
			if ((cellvalue.indexOf(".") != -1) && (cellvalue.indexOf("E") != -1)) {
				java.text.DecimalFormat df = new java.text.DecimalFormat();
				try {
					cellvalue = df.parse(cellvalue).toString();
				} catch (ParseException e) {
					// return orignale string
				}
			}
			break;
		case Cell.CELL_TYPE_FORMULA:
			cellvalue = cell.getCellFormula() + "";
			break;
		default:
			cellvalue = "";
			break;
		}
		return this.strtovalid(cellvalue);
	}

	@Override
	protected String getXRowCellDateData(Object row2, int columnNum) {
		HSSFRow row = (HSSFRow) row2;
		HSSFCell cell = row.getCell((columnNum - 1));
		if (cell == null) {
			return "";
		} // 判断取到的cell值是否为null

		String cellvalue = "";
		try {
			cellvalue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cell.getDateCellValue());
		} catch (Exception e) {
		}

		cellvalue = this.strtovalid(cellvalue);

		return cellvalue;
	}

	@Override
	public boolean getXRowCellType(Object row2, int num) {
		boolean flag = true;
		HSSFRow row = (HSSFRow) row2;
		for (int i = 1; i < num; i++) {
			HSSFCell cell = row.getCell(i - 1);
			// 判断取到的cell值是否为null
			if (cell == null) {
				continue;
			}
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				flag = true;
				break;
			case Cell.CELL_TYPE_BLANK: // 空
				flag = true;
				break;
			default:
				flag = false;
				break;
			}
			if (!flag) {
				break;
			}
		}
		return flag;
	}

	@Override
	public void changeXRowCellTypeToString(Object row2, int num) {
		HSSFRow row = (HSSFRow) row2;
		for (int i = 1; i < num; i++) {
			HSSFCell cell = row.getCell(i - 1);
			// 将单元格设置为类型
			if (cell != null) {
				cell.setCellType(Cell.CELL_TYPE_STRING);
			}
		}

	}

}
