package cn.explink.print.template;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class PrintTemplateTest {

	@Test
	public void test() {
		PrintTemplate printTemplate=new PrintTemplate();
		printTemplate.setDetail("[{\"width\":100,\"columnName\":\"订单号\",\"field\":\"cwb\"}]");
		List<PrintColumn> columns = printTemplate.getColumns();
		Assert.assertEquals(1, columns.size());
		PrintColumn printColumn = columns.get(0);
		Assert.assertEquals("100", printColumn.getWidth());
	}

}
