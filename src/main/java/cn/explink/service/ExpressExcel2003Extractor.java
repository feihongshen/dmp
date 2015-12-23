/**
 *
 */
package cn.explink.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cn.explink.exception.BadExcelException;

/**
 * @author songkaojun 2015年8月20日
 */
@Component
public class ExpressExcel2003Extractor extends Excel2003Extractor {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public List<Object> getRows(InputStream fis) {
		List<Object> rows = new ArrayList<Object>();
		try {
			HSSFWorkbook xwb = null;
			try {
				xwb = new HSSFWorkbook(fis);
			} catch (RuntimeException e) {
				e.printStackTrace();
				this.logger.error("文件异常，{}", e.getMessage());
				throw new BadExcelException();
			}
			HSSFSheet sheet = xwb.getSheetAt(0);
			this.logger.info("开始解析");

			int rowindex = 3;
			while (true) {
				HSSFRow row = sheet.getRow(rowindex++); // 从第四行开始，取出每一行
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
			e.printStackTrace();

		}
		return rows;
	}

}
