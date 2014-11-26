package cn.explink.pos.bill99;

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

@Controller
@RequestMapping("/bill99inter_test")
public class Bill99InterfaceTest {
	private String RequestXML(String content) throws Exception {
		// content = URLEncoder.encode(content, "utf-8");
		URL url = new URL("http://localhost:8080/dmp/bill99/");
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

	@RequestMapping("/search")
	public @ResponseBody String test_search(HttpServletRequest request) {
		String cwb = request.getParameter("cwb");
		String xmlstr = XML_Search(cwb);
		try {

			return RequestXML(xmlstr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	@RequestMapping("/login")
	public @ResponseBody String test_login(HttpServletRequest request) {
		String xmlstr = XML_Login();
		try {

			return RequestXML(xmlstr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	@RequestMapping("/pay")
	public @ResponseBody String test_pay(HttpServletRequest request) {
		String cwb = request.getParameter("cwb");
		String pay_type = request.getParameter("pay_type");
		String amount = request.getParameter("amount");
		String xmlstr = XML_payAmount(cwb, pay_type, amount);
		try {

			return RequestXML(xmlstr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	@RequestMapping("/sign")
	public @ResponseBody String test_sign(HttpServletRequest request) {
		String cwb = request.getParameter("cwb");
		String xmlstr = XML_sign(cwb);
		try {

			return RequestXML(xmlstr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping("/expt")
	public @ResponseBody String test_expt(HttpServletRequest request) {
		String cwb = request.getParameter("cwb");
		String xmlstr = XML_exptFeedBack(cwb);
		try {

			return RequestXML(xmlstr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping("/backout")
	public @ResponseBody String test_backout(HttpServletRequest request) {
		String cwb = request.getParameter("cwb");
		String xmlstr = XML_backOut(cwb);
		try {

			return RequestXML(xmlstr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private String XML_Login() {
		String content = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" + "<Transaction>" + "<Transaction_Header>" + "<version>1.0</version>" + "<transaction_sn>200908101234</transaction_sn>"
				+ "<transaction_id>MI0001</transaction_id>" + "<requester>bill99</requester>" + "<target>explink</target>" + "<request_time>201209131400</request_time>"
				+ "<MAC>L38N6uOqnVwj9bg8fb8MKqMZLhr1tcJKkM8FmwnuRDf</MAC>" + "</Transaction_Header>" + "<Transaction_Body>" + "<delivery_man>111111</delivery_man>"
				+ "<password>f676d5db3ad94cfabd8fb5326f1eec</password>" + "</Transaction_Body>" + "</Transaction>";
		return content;
	}

	private String XML_Search(String cwb) {
		String content = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" + "<Transaction>" + "<Transaction_Header>" + "<version>1.0</version>" + "<transaction_sn>200908101234</transaction_sn>"
				+ "<transaction_id>MI0010</transaction_id>" + "<requester>bill99</requester>" + "<target>explink</target>" + "<request_time>201209131400</request_time>"
				+ "<MAC>L38N6uOqnVwj9bg8fb8MKqMZLhr1tcJKkM8FmwnuRDf</MAC>" + "<ext_attributes>" + "<delivery_man>111111</delivery_man>" + "<txn_type>01</txn_type>" + "</ext_attributes>"
				+ "</Transaction_Header>" + "<Transaction_Body>" + "<orderId>" + cwb + "</orderId>" + "</Transaction_Body>" + "</Transaction>";
		return content;
	}

	private String XML_payAmount(String cwb, String pay_type, String amount) {
		// 支付。
		String content = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" + "<Transaction>" + "<Transaction_Header>" + "<version>1.0</version>" + "<transaction_sn>200908101234</transaction_sn>"
				+ "<transaction_id>MI0005</transaction_id>" + "<requester>bill99</requester>" + "<target>explink</target>" + "<request_time>201209131400</request_time>"
				+ "<MAC>L38N6uOqnVwj9bg8fb8MKqMZLhr1tcJKkM8FmwnuRDf</MAC>" + "<ext_attributes>" + "<delivery_man>111111</delivery_man>" + "<delivery_name>张三</delivery_name>"
				+ "<delivery_dept_no>105</delivery_dept_no>" + "<delivery_dept>国贸站</delivery_dept>" + "</ext_attributes>" + "</Transaction_Header>" + "<Transaction_Body>" + "<order_no>" + cwb
				+ "</order_no>" + "<order_payable_amt>" + amount + "</order_payable_amt>" + "<pay_type>" + pay_type + "</pay_type>" + "<idTxn>1010</idTxn>" + "<terminal_id>222222</terminal_id>"
				+ "<trace_no>1111111</trace_no>" + "</Transaction_Body>" + "</Transaction>";
		return content;
	}

	private String XML_sign(String cwb) {
		// 签收
		String content = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" + "<Transaction>" + "<Transaction_Header>" + "<version>1.0</version>" + "<transaction_sn>200908101234</transaction_sn>"
				+ "<transaction_id>MI0006</transaction_id>" + "<requester>bill99</requester>" + "<target>explink</target>" + "<request_time>201209131400</request_time>"
				+ "<MAC>L38N6uOqnVwj9bg8fb8MKqMZLhr1tcJKkM8FmwnuRDf</MAC>" + "<ext_attributes>" + "<delivery_man>111111</delivery_man>" + "<delivery_name>张三</delivery_name>"
				+ "<delivery_dept_no>105</delivery_dept_no>" + "<delivery_dept>国贸站</delivery_dept>" + "</ext_attributes>" + "</Transaction_Header>" + "<Transaction_Body>" + "<order_no>" + cwb
				+ "</order_no>" + "<signee>张鹏凯</signee>" + "<consignee_sign_flag>Y</consignee_sign_flag>" + "<idTxn>1010</idTxn>" + "</Transaction_Body>" + "</Transaction>";
		return content;
	}

	private String XML_exptFeedBack(String cwb) {
		// 异常反馈
		String content = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" + "<Transaction>" + "<Transaction_Header>" + "<version>1.0</version>" + "<transaction_sn>200908101234</transaction_sn>"
				+ "<transaction_id>MI0008</transaction_id>" + "<requester>bill99</requester>" + "<target>explink</target>" + "<request_time>201209131400</request_time>"
				+ "<MAC>L38N6uOqnVwj9bg8fb8MKqMZLhr1tcJKkM8FmwnuRDf</MAC>" + "<ext_attributes>" + "<delivery_man>111111</delivery_man>" + "<delivery_name>张三</delivery_name>"
				+ "<delivery_dept_no>105</delivery_dept_no>" + "<delivery_dept>国贸站</delivery_dept>" + "<ex_packages>1</ex_packages>" + "</ext_attributes>" + "</Transaction_Header>"
				+ "<Transaction_Body>" + "<order_no>" + cwb + "</order_no>" + "<ex_code>32</ex_code>" + "<ex_desc>收件人不想要</ex_desc>" + "</Transaction_Body>" + "</Transaction>";
		return content;
	}

	private String XML_backOut(String cwb) {
		// 撤销
		String content = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" + "<Transaction>" + "<Transaction_Header>" + "<version>1.0</version>" + "<transaction_sn>200908101234</transaction_sn>"
				+ "<transaction_id>MI0007</transaction_id>" + "<requester>bill99</requester>" + "<target>explink</target>" + "<request_time>201209131400</request_time>"
				+ "<MAC>L38N6uOqnVwj9bg8fb8MKqMZLhr1tcJKkM8FmwnuRDf</MAC>" + "<ext_attributes>" + "<delivery_man>111111</delivery_man>" + "<delivery_name>张三</delivery_name>"
				+ "<delivery_dept_no>105</delivery_dept_no>" + "<delivery_dept>国贸站</delivery_dept>" + "</ext_attributes>" + "</Transaction_Header>" + "<Transaction_Body>" + "<order_no>" + cwb
				+ "</order_no>" + "<void_amt>20</void_amt>" + "<pay_type>01</pay_type>" + "<idTxn>1010</idTxn>" + "<terminal_id>222222</terminal_id>" + "<void_trace_no>1111111</void_trace_no>"
				+ "</Transaction_Body>" + "</Transaction>";

		return content;
	}

	private String XML_Reversal() {
		// 冲正接口
		String content = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" + "<Transaction>" + "<Transaction_Header>" + "<version>1.0</version>" + "<transaction_sn>200908101234</transaction_sn>"
				+ "<transaction_id>MI0011</transaction_id>" + "<requester>bill99</requester>" + "<target>explink</target>" + "<request_time>201209131400</request_time>"
				+ "<MAC>L38N6uOqnVwj9bg8fb8MKqMZLhr1tcJKkM8FmwnuRDf</MAC>" + "<ext_attributes>" + "<delivery_man>zs</delivery_man>" + "<delivery_name>张三</delivery_name>"
				+ "<delivery_dept_no>105</delivery_dept_no>" + "<delivery_dept>国贸站</delivery_dept>" + "</ext_attributes>" + "</Transaction_Header>" + "<Transaction_Body>"
				+ "<order_no>12062109291829</order_no>" + "<reverse_amt>100</reverse_amt>" + "<pay_type>01</pay_type>" + "<reverse_tran_type>01</reverse_tran_type>"
				+ "<reverse_trace_no>222222</reverse_trace_no>" + "<terminal_id>1111111</terminal_id>" + "</Transaction_Body>" + "</Transaction>";

		return content;
	}

}
