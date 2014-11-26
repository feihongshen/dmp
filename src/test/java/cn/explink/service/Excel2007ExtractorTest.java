package cn.explink.service;

import org.junit.Assert;
import org.junit.Test;

public class Excel2007ExtractorTest {

	Excel2007Extractor excel2007Extractor=new Excel2007Extractor();
	@Test
	public void test() {
		Assert.assertEquals("重庆市南岸区弹子石莫比石代2栋27一6", excel2007Extractor.strtovalid("重庆市南岸区弹子石莫比石代2栋27一6"));
		Assert.assertEquals("重庆市龙湖南苑5-6-503", excel2007Extractor.strtovalid("重庆市龙湖南苑5-6-503"));

	}
	
	

}
