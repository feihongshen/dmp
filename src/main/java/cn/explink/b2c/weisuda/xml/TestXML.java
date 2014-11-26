package cn.explink.b2c.weisuda.xml;

public class TestXML {

	public static void main(String[] args) throws Exception {
		String xmlStr = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + "<root>" + "<item>" + "<courier_code>TT0012</courier_code>" + "<order_id>76763763872</order_id>"
				+ "<bound_time>138786797887</bound_time>" + "</item>" + "<item>" + "<courier_code>TT0013</courier_code>" + "<order_id>76763763873</order_id>" + "<bound_time>138786797888</bound_time>"
				+ "</item>" + "</root>";
		PushOrders_Root root = (PushOrders_Root) ObjectUnMarchal.XmltoPOJO(xmlStr, new PushOrders_Root());

		String xml = ObjectUnMarchal.POJOtoXml(root);
		System.out.println(xml);
		String xmlStr1 = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + "<root>" + "<order_id>76763763872</order_id>" + "<order_id>76763763872</order_id>" + "<order_id>76763763872</order_id>"
				+ "<order_id>76763763872</order_id>" + "<order_id>76763763873</order_id>" + "</root>";
		PushOrders_back_Root backroot = (PushOrders_back_Root) ObjectUnMarchal.XmltoPOJO(xmlStr1, new PushOrders_back_Root());

		String xml1 = ObjectUnMarchal.POJOtoXml(backroot);
		System.out.println(xml1);
		String xmlStr2 = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + "<root>" + "<item>" + "<order_id>76763763872</order_id>" + "<order_status>1</order_status>" + "<consignee>签收人</consignee>"
				+ "<reason>拒收原因</reason>" + "<memo>备注</memo>" + "<paymethod>1</paymethod>" + "<money>12.5</money>" + "<reject_map>http://xxx.com/xxx.jpg</reject_map>" + "</item>" + "</root>";
		GetUnVerifyOrders_back_Root getUnVerifyOrders_Root = (GetUnVerifyOrders_back_Root) ObjectUnMarchal.XmltoPOJO(xmlStr2, new GetUnVerifyOrders_back_Root());

		String xml2 = ObjectUnMarchal.POJOtoXml(getUnVerifyOrders_Root);
		System.out.println(xml2);
		String xmlStr3 = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + "<root>" + "<item>" + "<order_id>76763763872</order_id>" + "</item>" + "<item>" + "<order_id>76763763873</order_id>"
				+ "</item>" + "</root>";
		UpdateUnVerifyOrders_Root updateUnVerifyOrders_Root = (UpdateUnVerifyOrders_Root) ObjectUnMarchal.XmltoPOJO(xmlStr3, new UpdateUnVerifyOrders_Root());

		String xml3 = ObjectUnMarchal.POJOtoXml(updateUnVerifyOrders_Root);
		System.out.println(xml3);
	}

}
