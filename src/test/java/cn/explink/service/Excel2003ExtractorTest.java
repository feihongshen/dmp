package cn.explink.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class Excel2003ExtractorTest {

	Excel2003Extractor excel2007Extractor=new Excel2003Extractor();
	@Test
	public void test() throws FileNotFoundException {
		List<Object> rows = excel2007Extractor.getRows(new FileInputStream("src/test/java/cn/explink/service/书飞网.xls"));
		Assert.assertEquals("中原逸雄", excel2007Extractor.getXRowCellData(rows.get(0), 2));
		Assert.assertEquals("190465", excel2007Extractor.getXRowCellData(rows.get(0), 1));
		Assert.assertEquals("10.0", excel2007Extractor.getXRowCellData(rows.get(0), 7));
		Assert.assertEquals("2.0", excel2007Extractor.getXRowCellData(rows.get(0), 9));
	}

}
