package cn.explink.pos.chinaums;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.util.MD5.MD5Util;

@Controller
@RequestMapping("/chinaums_test")
public class ChinaUmsInterfaceTest {
	private String RequestXML(String content) throws Exception {
		// content = URLEncoder.encode(content, "utf-8");
		URL url = new URL("http://123.178.27.74/dmp4499/chinaums/");
		// URL url = new URL("http://127.0.0.1:8080/dmp/chinaums/");
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		httpURLConnection.setRequestProperty("content-type", "text/xml");
		httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
		httpURLConnection.setRequestProperty("contentType", "utf-8");
		httpURLConnection.setDoOutput(true);
		httpURLConnection.setDoInput(true);
		httpURLConnection.setRequestMethod("POST");
		httpURLConnection.setConnectTimeout(5000);
		httpURLConnection.setReadTimeout(5000);
		httpURLConnection.connect();
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8"));
		out.write(content);
		out.flush();
		// 接收服务器的返回：
		BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
		StringBuilder buffer = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			buffer.append(line);
		}
		return buffer.toString();
	}

	@RequestMapping("/test")
	public String test(HttpServletRequest request) {
		return "jointmanage/chinaums/test";
	}

	public static void main(String[] args) {

		String login = "<?xml version=\"1.0\"  encoding=\"UTF-8\"  ?>" + "<transaction>" + "<transaction_header>" + "<version>1.0</version>" + "<transtype>P001</transtype>"
				+ "<employno>888888</employno>" + "<termid>12345678</termid>" + "<request_time>20120913140101</request_time>" + "<mac>0123456789ABCDEF0123456789ABCDEF</mac>" + "</transaction_header>"
				+ "<transaction_body>" + "<passwd>" + MD5Util.md5("888888").toUpperCase() + "</passwd>" + "</transaction_body>" + "</transaction>";
		System.out.println("登录报文：" + login);

		String content = "<?xml version=\"1.0\"  encoding=\"UTF-8\"  ?>" + "<transaction>" + "<transaction_header>" + "<version>1.0</version>" + "<transtype>P004</transtype>"
				+ "<employno>0720001</employno>" + "<termid>12345678</termid>" + "<request_time>20120913140101</request_time>" + "<mac>0123456789ABCDEF0123456789ABCDEF</mac>"
				+ "</transaction_header>" + "<transaction_body>" + "<orderno>0412345608</orderno>" + "</transaction_body>" + "</transaction>";
		System.out.println("查询报文：" + content);

		String sign = "<?xml version=\"1.0\"  encoding=\"UTF-8\"  ?>" + "<transaction>" + "<transaction_header>" + "<version>1.0</version>" + "<transtype>P003</transtype>" + "<employno>cy</employno>"
				+ "<termid>12345678</termid>" + "<request_time>20120913140101</request_time>" + "<mac>BB7FB67C6523932688179F558FEF984E</mac>" + "</transaction_header>" + "<transaction_body>"
				+ "<orderno>124</orderno>" + "<cod>66.20</cod>" + "<payway>02</payway>" + "<banktrace>123456789012</banktrace>" + "<postrace>123456</postrace>"
				+ "<tracetime>20130416125703</tracetime>" + "<cardid>42270987123123123</cardid>" + "<signpeople>0</signpeople>" + "<dssn>125</dssn>" + "<dsname>乐购</dsname>" + "</transaction_body>"
				+ "</transaction>";
		System.out.println("签收报文：" + sign);
		String signCanl = "<?xml version=\"1.0\"  encoding=\"UTF-8\"  ?>" + "<transaction>" + "<transaction_header>" + "<version>1.0</version>" + "<transtype>P006</transtype>"
				+ "<employno>0720001</employno>" + "<termid>12345678</termid>" + "<request_time>20120913140101</request_time>" + "<mac>0123456789ABCDEF0123456789ABCDEF</mac>"
				+ "</transaction_header>" + "<transaction_body>" + "<orderno>153</orderno>" + "<cod>32.4</cod>" + "<banktrace>123456789012</banktrace>" + "<postrace>123456</postrace>"
				+ "<cardid>42270987123123123</cardid>" + "</transaction_body>" + "</transaction>";
		System.out.println("撤销签收报文：" + signCanl);
		String logout = "<?xml version=\"1.0\"  encoding=\"UTF-8\"  ?>" + "<transaction>" + "<transaction_header>" + "<version>1.0</version>" + "<transtype>P101</transtype>"
				+ "<employno>888888</employno>" + "<termid>12345678</termid>" + "<request_time>20120913140101</request_time>" + "<mac>0123456789ABCDEF0123456789ABCDEF</mac>" + "</transaction_header>"
				+ "<transaction_body>" + "</transaction_body>" + "</transaction>";
		System.out.println("登出报文：" + logout);
		String yichang = "<?xml version=\"1.0\"  encoding=\"UTF-8\"  ?>" + "<transaction>" + "<transaction_header>" + "<version>1.0</version>" + "<transtype>P005</transtype>"
				+ "<employno>0720001</employno>" + "<termid>12345678</termid>" + "<request_time>20120913140101</request_time>" + "<mac>0123456789ABCDEF0123456789ABCDEF</mac>"
				+ "</transaction_header>" + "<transaction_body>" + "<orderno>0412345608</orderno>" + "<errorcode>01</errorcode>" + "<memo>客户未接电话</memo>" + "<urgent>0</urgent>" + "</transaction_body>"
				+ "</transaction>";
		System.out.println("异常单反馈报文：" + yichang);

	}

}
