package cn.explink.b2c.weisuda.xml;

import java.util.Date;

import cn.explink.util.DateTimeUtil;

public class TestXML {

	public static void main(String[] args) throws Exception {
		/*
		 * String xmlStr = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
		 * "<root>" + "<item>" + "<courier_code>TT0012</courier_code>" +
		 * "<order_id>76763763872</order_id>" +
		 * "<bound_time>138786797887</bound_time>" + "</item>" + "<item>" +
		 * "<courier_code>TT0013</courier_code>" +
		 * "<order_id>76763763873</order_id>" +
		 * "<bound_time>138786797888</bound_time>" + "</item>" + "</root>";
		 * PushOrders_Root root=(PushOrders_Root)
		 * ObjectUnMarchal.XmltoPOJO(xmlStr, new PushOrders_Root());
		 * 
		 * String xml=ObjectUnMarchal.POJOtoXml( root); System.out.println(xml);
		 * String xmlStr1 = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
		 * "<root>" + "<order_id>76763763872</order_id>" +
		 * "<order_id>76763763872</order_id>" +
		 * "<order_id>76763763872</order_id>" +
		 * "<order_id>76763763872</order_id>" +
		 * "<order_id>76763763873</order_id>" + "</root>"; PushOrders_back_Root
		 * backroot=(PushOrders_back_Root) ObjectUnMarchal.XmltoPOJO(xmlStr1,
		 * new PushOrders_back_Root());
		 * 
		 * String xml1=ObjectUnMarchal.POJOtoXml( backroot);
		 * System.out.println(xml1); String xmlStr2 =
		 * "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +"<root>" +"<item>"
		 * +"<order_id>76763763872</order_id>"
		 * +"<courier_code>76763763872</courier_code>"
		 * +"<order_status>1</order_status>" +"<consignee>签收人</consignee>"
		 * +"<opertime>2014年9月18日 15:20:39</opertime>" +"<reason>拒收原因</reason>"
		 * +"<memo>备注</memo>" +"<paymethod>1</paymethod>" +"<money>12.5</money>"
		 * +"<reject_map>http://xxx.com/xxx.jpg</reject_map>"
		 * +"<pay_status>1</pay_status>" +"</item>" +"</root>";
		 * GetUnVerifyOrders_back_Root
		 * getUnVerifyOrders_Root=(GetUnVerifyOrders_back_Root)
		 * ObjectUnMarchal.XmltoPOJO(xmlStr2, new
		 * GetUnVerifyOrders_back_Root());
		 * 
		 * String xml2=ObjectUnMarchal.POJOtoXml(getUnVerifyOrders_Root);
		 * System.out.println(xml2); String xmlStr3 =
		 * "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +"<root>" +"<item>"
		 * +"<order_id>76763763872</order_id>" +"</item>" +"<item>"
		 * +"<order_id>76763763873</order_id>" +"</item>" +"</root>";
		 * UpdateUnVerifyOrders_Root
		 * updateUnVerifyOrders_Root=(UpdateUnVerifyOrders_Root)
		 * ObjectUnMarchal.XmltoPOJO(xmlStr3, new UpdateUnVerifyOrders_Root());
		 * 
		 * String xml3=ObjectUnMarchal.POJOtoXml(updateUnVerifyOrders_Root);
		 * System.out.println(xml3); String xmlStr4 =
		 * "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +"<root>"
		 * +"<order_id>76763763872</order_id>"
		 * +"<order_id>76763763873</order_id>"
		 * +"<order_id>76763763874</order_id>"
		 * +"<order_id>76763763875</order_id>"
		 * +"<order_id>76763763876</order_id>"
		 * +"<order_id>76763763877</order_id>" +"</root>"; UpdateOrders_root
		 * updateOrders_root=(UpdateOrders_root)
		 * ObjectUnMarchal.XmltoPOJO(xmlStr4, new UpdateOrders_root());
		 * SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		 * System.out.println(format.parse("2014-09-19 09:57:15").getTime());
		 * String xml4=ObjectUnMarchal.POJOtoXml(updateOrders_root);
		 * System.out.println
		 * (DateTimeUtil.StringToDate("2014-09-19 09:57:15").getTime());
		 * System.out.println(xml4);
		 */
		String[] cwb = { "100000001", "100000002", "100000003", "100000004", "100000005", "100000006", "100000007", "100000008" };
		String[] good_name = { "运动裤", "上衣", "篮球鞋", "阿甘鞋", "帽子" };
		String[] good_branch = { "耐克", "李宁", "阿迪达斯", "newbalance", "乔丹" };
		String[] goods_pic_url = { "http://img3.imgtn.bdimg.com/it/u=2033152201,1709937956&fm=21&gp=0.jpg", "http://img0.imgtn.bdimg.com/it/u=3172279546,2160533268&fm=21&gp=0.jpg",
				"http://img0.imgtn.bdimg.com/it/u=802379824,3278411666&fm=21&gp=0.jpg", "http://img2.imgtn.bdimg.com/it/u=1095592609,3530544173&fm=23&gp=0.jpg",
				"http://img5.imgtn.bdimg.com/it/u=3084772777,1615018424&fm=23&gp=0.jpg" };
		String[] return_reason = { "和实物不匹配，很失望", "不是正品！", "不想要了", "尺寸不合适", "质量不是很好" };
		for (int i = 0; i < 20; i++) {
			System.out
					.println("INSERT INTO `peisongdata_dmp_test`.`orders_goods` (`id`, `cwb`, `cretime`, `goods_code`, `goods_brand`, `goods_name`, `goods_spec`, `goods_num`, `return_reason`, `goods_pic_url`, `remark1`, `remark2`, `remark3`, `remark4`, `remark5`, `remark6`, `remark7`, `remark8`, `remark9`, `remark10`) VALUES ('"
							+ i
							+ "', '"
							+ cwb[(int) (Math.random() * 8)]
							+ "', '"
							+ DateTimeUtil.formatDate(new Date())
							+ "', '"
							+ 100100101
							+ i
							+ "', '"
							+ good_branch[(int) (Math.random() * 2)]
							+ "', '"
							+ good_name[(int) (Math.random() * 5)]
							+ "', '"
							+ (int) (398 * Math.random())
							+ "', '1', '"
							+ return_reason[(int) (Math.random() * 5)]
							+ "', '"
							+ goods_pic_url[(int) (Math.random() * 5)] + "', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);");
		}

	}

}
