package cn.explink.b2c.happyGo;

public class HappyGoAnalisisXml {

	public static String AnalisisyXml(int page, String xml) {
		String returnxml = "";
		for (int i = 0; i < page; i++) {
			int x = i + 1;
			returnxml = xml.replaceAll("row" + x, "explink");
		}
		return returnxml;
	}

}
