/**
 *
 */
package cn.explink.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.Test;

/**
 * @author songkaojun 2015年8月20日
 */
public class ExcelExtractorTest {
	@Test
	public void test() throws FileNotFoundException, IOException {
		InputStream inputStream = new FileInputStream("src/ExecelImportAndValidateUtilTest/java/cn/explink/service/跨省代收货款对账（应收）账单明细导入测试.xls");
		// ExcelExtractor excelExtractor = this.getExcelExtractor(file);
		ExpressExcel2003Extractor excel2003Extractor = new ExpressExcel2003Extractor();
		List<Object> rows = excel2003Extractor.getRows(inputStream);
		for (Object object : rows) {
			System.out.print(excel2003Extractor.getXRowCellData(object, 1));
			System.out.print(excel2003Extractor.getXRowCellData(object, 2));
			System.out.print(excel2003Extractor.getXRowCellData(object, 3));
			System.out.print(excel2003Extractor.getXRowCellData(object, 4));
			System.out.print(excel2003Extractor.getXRowCellData(object, 5));
			System.out.print(excel2003Extractor.getXRowCellData(object, 6));
			System.out.print(excel2003Extractor.getXRowCellData(object, 7));
			System.out.println(excel2003Extractor.getXRowCellData(object, 8));
		}
	}

	// private ExcelExtractor getExcelExtractor(MultipartFile file) {
	// String originalFilename = file.getOriginalFilename();
	// if (originalFilename.endsWith("xlsx")) {
	// return new Excel2007Extractor();
	// } else if (originalFilename.endsWith(".xls")) {
	// return new Excel2003Extractor();
	// }
	// return null;
	// }

}
