package cn.explink.util;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;

import org.junit.Assert;
import org.junit.Test;

public class StringUtilTest {

	@Test
	public void test() throws UnsupportedEncodingException {
		String s="D000002105881000984322 120130806596201001001001毕楚悦              0000-000000000000-00000000266100山东省 青岛市 崂山区 海尔路21号世纪华庭21号楼1单元302(提前联系)                                     1057242013韩国进口惠人原汁机HU500                           001固态                                              00010000002198用户明确要求货到后物流师傅要提前联系，因为不确定家里是否有人。                                      2013080700000004469285000000";
		Assert.assertEquals("毕楚悦", StringUtil.subStrByByteEncoding(s, 48,67, "GBK").trim()) ;
		Assert.assertEquals("山东省 青岛市 崂山区 海尔路21号世纪华庭21号楼1单元302(提前联系)", StringUtil.subStrByByteEncoding(s,100,199, "GBK").trim()) ;
	}

}
