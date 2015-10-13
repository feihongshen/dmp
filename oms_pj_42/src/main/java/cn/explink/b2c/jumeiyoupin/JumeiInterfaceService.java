package cn.explink.b2c.jumeiyoupin;

import org.springframework.stereotype.Service;

@Service
public class JumeiInterfaceService {

	public String returnJumeiXml(String xmlstr, String cwbinfo, String unixstamp, String MD5Data, String sign) {

		StringBuffer responesXML = new StringBuffer();
		responesXML.append("orderresponse");

		// <row resultcount="1">
		// <cwb>123456789</cwb><!--快递单号 -->
		// <trackdatetime>2010-05-01 12:00:00</trackdatetime><!--操作时间 -->
		// <trackstatus> deferred,11,客户电话关机</trackstatus><!--当前状态 -->
		// < cs_note >客户新电话号码为：13511232123，请继续派送< cs_note ><!--客服答复内容-->
		// <update_time>2010-05-01 13:00:00<update_time><!--客服答复时间-->
		// </row>
		// </result>

		// <orderresponse>
		// <cwb>123456789</cwb><!--快递单号 --> <!—操作类型-->
		// <company>申通快递</company>
		// <success>true</success>
		// </orderresponse>

		// <orderrsponse>
		// <cwb>123456789</cwb><!--快递单号 --> <!—操作类型-->
		// <company>申通快递</company>
		// <success>False</success>
		// <reason>R1</reason>
		// </orderrsponse>

		return "";
	}
}
