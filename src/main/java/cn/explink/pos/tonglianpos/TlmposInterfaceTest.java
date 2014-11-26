package cn.explink.pos.tonglianpos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.pos.tools.PosEnum;
import cn.explink.util.pos.RSACoder;

@Controller
@RequestMapping("/tlmpos_test")
public class TlmposInterfaceTest {
	@Autowired
	TlmposService tlmposService;

	private String RequestXML(String content) throws Exception {

		Tlmpos tlmpos = tlmposService.gettlmposSettingMethod(PosEnum.TongLianPos.getKey()); // 获取配置信息

		// content = URLEncoder.encode(content, "utf-8");
		URL url = new URL(tlmpos.getRequest_url());
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
		String delivery_man = request.getParameter("delivery_man");

		Tlmpos tlmpos = tlmposService.gettlmposSettingMethod(PosEnum.TongLianPos.getKey()); // 获取配置信息

		String xmlstr = XML_Search(cwb, delivery_man, tlmpos);
		try {

			return RequestXML(xmlstr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping("/pay")
	public @ResponseBody String test_pay(HttpServletRequest request) {
		String cwb = request.getParameter("cwb");
		String delivery_man = request.getParameter("delivery_man");
		String payamount = request.getParameter("payamount");

		Tlmpos tlmpos = tlmposService.gettlmposSettingMethod(PosEnum.TongLianPos.getKey()); // 获取配置信息

		String xmlstr = XML_payAmount(cwb, delivery_man, payamount, tlmpos);
		try {
			// System.out.println(RequestXML(xmlstr));
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
		String delivery_man = request.getParameter("delivery_man");
		Tlmpos tlmpos = tlmposService.gettlmposSettingMethod(PosEnum.TongLianPos.getKey()); // 获取配置信息
		String xmlstr = XML_sign(cwb, delivery_man, tlmpos);
		try {
			// System.out.println(RequestXML(xmlstr));
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
		String delivery_man = request.getParameter("delivery_man");
		Tlmpos tlmpos = tlmposService.gettlmposSettingMethod(PosEnum.TongLianPos.getKey()); // 获取配置信息
		String xmlstr = XML_exptFeedBack(cwb, delivery_man, tlmpos);
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
		String delivery_man = request.getParameter("delivery_man");
		String amount = request.getParameter("amount");
		Tlmpos tlmpos = tlmposService.gettlmposSettingMethod(PosEnum.TongLianPos.getKey()); // 获取配置信息
		String xmlstr = XML_backOut(cwb, delivery_man, amount, tlmpos);
		try {

			return RequestXML(xmlstr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private String XML_Search(String cwb, String delivery_man, Tlmpos tlmpos) {
		String content = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
				+ "<Transaction>"
				+ "<Transaction_Header>"
				+ "<transaction_id>MI0010</transaction_id>"
				+ "<requester>"
				+ tlmpos.getRequester()
				+ "</requester>"
				+ "<target>"
				+ tlmpos.getTargeter()
				+ "</target>"
				+ "<request_time>20121227163526</request_time>"
				+ "<version>1.0</version>"
				+ "<ext_attributes>"
				+ "<delivery_man>"
				+ delivery_man
				+ "</delivery_man>"
				+ "</ext_attributes>"
				+ "<MAC>lYCYJU/37w9HBIJw8O5jNzK2weJhl9ESoZUFpU+GHpq1SWMg1b79QrudNSGHFeAiIf13A777nZF7lYhROENAuX9QanxpX6B1T1jMp+BmgwNert+jMGmUcZAAheV5uaJTKScFMPy0pN0mqy9L9vOIUvRG/GZeGt4DmJlMs0miP+A=</MAC>"
				+ "</Transaction_Header>" + "<Transaction_Body>" + "<order_no>" + cwb + "</order_no>" + "</Transaction_Body>" + "</Transaction>";
		return content;
	}

	private String XML_payAmount(String cwb, String delivery_man, String payamount, Tlmpos tlmpos) {
		// 支付。
		String content = "<Transaction>" + "<Transaction_Header>" + "<transaction_id>MI0005</transaction_id> " + "<requester>" + tlmpos.getRequester() + "</requester> " + "<target>"
				+ tlmpos.getTargeter() + "</target>" + "<request_time>20101227000802</request_time> " + "<version>1.0</version><ext_attributes> " + "<delivery_dept_no>19403</delivery_dept_no> "
				+ "<delivery_dept>南京白下营业部</delivery_dept> " + "<delivery_man>" + delivery_man + "</delivery_man> " + "<delivery_name>翟成坤</delivery_name> "
				+ "<logistics_alias>DEPPON</logistics_alias> " + "</ext_attributes> " + "<MAC>O5/MOLl1Mvz+tYAQJlLewyTTGmJzjRkV5HCs/ "
				+ "sdGQQ3VIXu9LDmeXo1eBdZNri5K2ekUYAKRHgvj0phzEnzNRmsrOYH3jDbu0hrnX0dGHFd0mH" + "XDflBRqotMH+Y2NXgYdayn6rw4fWiC0DJXSB8rAZtrFGZY/ALqp0vX8JXAuV4=</MAC> " + "</Transaction_Header> "
				+ "<Transaction_Body> " + "<order_no>" + cwb + "</order_no> " + "<order_payable_amt>" + payamount + "</order_payable_amt> " + "<pay_type>02</pay_type> "
				+ "<terminal_id>80040952</terminal_id>" + "<trace_no>001147</trace_no> " + "<alipay_trace_no>2012080200065300242901</alipay_trace_no> "
				+ "<trade_no>20120802000052030000000400005890</trade_no> " + "<order_amt>" + payamount + "</order_amt> " + "<acq_type>single</acq_type> " + "</Transaction_Body> " + "</Transaction>";
		return content;
	}

	private String XML_sign(String cwb, String delivery_man, Tlmpos tlmpos) {
		// 签收
		String content = "<Transaction> " + "<Transaction_Header> " + "<transaction_id>MI0006</transaction_id> " + "<requester>" + tlmpos.getRequester() + "</requester> " + "<target>"
				+ tlmpos.getTargeter() + "</target> " + "<request_time>20101227001227</request_time> " + "<version>1.0</version> " + "<ext_attributes> " + "<delivery_dept_no>5361</delivery_dept_no> "
				+ "<delivery_dept>国贸站</delivery_dept> " + "<delivery_man>" + delivery_man + "</delivery_man> " + "<delivery_name>张三丰</delivery_name> " + "<logistics_alias>DEPPON</logistics_alias> "
				+ "</ext_attributes> " + "<MAC>MiZjYMW0loTMjMl3sLdWyOa3V/9N3itfSEWsMhfgN/fKWSK/kFd/Y/q/ " + "X+Re8BBIwW2b9w6RNvLy4S9Wv0iEuLjZbQFxhVJHAlCow9a6/62+09AAL/wzGvesOGNV9yc4Q"
				+ "4B63JZzXz+SPF60dcZq/Fd5JGQ+v9vgj1tHHMk1Flw=</MAC> " + "</Transaction_Header> " + "<Transaction_Body><order_no>" + cwb + "</order_no> " + "<signee>李庆辉</signee> "
				+ "<consignee_sign_flag>N</consignee_sign_flag> " + "</Transaction_Body> " + "</Transaction>";
		return content;
	}

	private String XML_exptFeedBack(String cwb, String delivery_man, Tlmpos tlmpos) {
		// 异常反馈
		String content = "<Transaction> " + "<Transaction_Header> " + "<transaction_id>MI0008</transaction_id>  " + "<requester>" + tlmpos.getRequester() + "</requester>  " + "<target>"
				+ tlmpos.getTargeter() + "</target>  " + "<request_time>20101227165134</request_time>  " + "<version>1.0</version>  " + "<ext_attributes>  "
				+ "<delivery_dept_no>8341</delivery_dept_no>  " + "<delivery_dept>上海闸北中兴路营业部</delivery_dept>  " + "<delivery_man>" + delivery_man + "</delivery_man>  "
				+ "<delivery_name>张三丰</delivery_name>  " + "<ex_packages>1</ex_packages>  " + "<logistics_alias>DEPPON</logistics_alias>  " + "</ext_attributes>  "
				+ "<MAC>BpJf9u459cnK8Pw/1zvonWAdNzTf48iYZHFe84ZKnNHNWlNBJCfkJfYQUEUrl3V/  " + "QZHZuGBWQ7Ecrmo7p9bxs9CMhgqBPz73W+AO2oabN5cvdFMnmAvw95uvBJzMmbbzq1dCm7Ewk "
				+ "pjCnIIV3kLRwXmzaJMuLdjUZz1sf4ANRsM=</MAC>  " + "</Transaction_Header>  " + "<Transaction_Body>  " + "<order_no>" + cwb + "</order_no>  " + "<ex_code>19</ex_code>  "
				+ "<ex_desc>客户拒收</ex_desc>  " + "</Transaction_Body>  " + "</Transaction> ";
		return content;
	}

	private String XML_backOut(String cwb, String delivery_man, String amount, Tlmpos tlmpos) {
		// 撤销

		String aaa = "<Transaction>" + "<Transaction_Header>" + "<transaction_id>MI0007</transaction_id>" + "<requester>" + tlmpos.getRequester() + "</requester>" + "<target>" + tlmpos.getTargeter()
				+ "</target>" + "<request_time>20101227084700</request_time>" + "<version>1.0</version>" + "<ext_attributes>" + "<delivery_dept_no>8618</delivery_dept_no>"
				+ "<delivery_dept>国贸站</delivery_dept>" + "<delivery_man>" + delivery_man + "</delivery_man>" + "<delivery_name>张三</delivery_name>" + "<logistics_alias>DEPPON</logistics_alias>"
				+ "</ext_attributes>" + "<MAC>iEw4q05THxeAMfZJ71aodlYf9qXAbemyNtdgJwyds44t/"
				+ "usrUEgZvAmfU1ZRT7JXH3GfhP/6Wl2LvNlabP0BRu3Ll1KawmDDz3CFjXjHJLr5thc1YQz0lwjJokk2r2fo6wD3AiVofr0ZzCPuju61T4WT/75kCKmAyh/o0rxVuYU=</MAC>" + "</Transaction_Header>"
				+ "<Transaction_Body>" + "<order_no>" + cwb + "</order_no>" + "<void_amt>" + amount + "</void_amt> " + "<terminal_id>80040099</terminal_id>" + "<void_trace_no>000147</void_trace_no>"
				+ "<acq_type>split</acq_type> " + "<alipay_trace_no>2012080200065300242901</alipay_trace_no>" + "<ori_alipay_trace_no>2012080200065300242902</ori_alipay_trace_no>"
				+ "<trade_no>20120802000052030000000400005890</trade_no> " + "</Transaction_Body> " + "</Transaction> ";

		return aaa;
	}

	private static boolean ValidateMAC_publicMethod(String xmlDOC) {

		// String transaction_id
		// =rootnote.getTransaction_Header().getTransaction_id();

		String public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDTGmkuheyC/DjpFadnQmFjN42V XIibP9lZI7IfWStKwPohbXPp/Nn0zKbaetWMzYJxRf3yW6RL8q9cJCNsL//xDqeT CR6p5JWDXH8ySH0NSE0fSVOVuaoILim6rNWAOZue4+XhnXcgYJbqsW1AMtPrswoG lC1iPBTs1MAhCXfR4wIDAQAB";

		String MAC = "BX0weQBxYqFdO32cvZm58rPq3Sx51CwMAN/8Z0MAbtVeewJOifXsOS8Sd+Z1mGHuhLGPwthzTnWdbsNnHYV4loDx+/fmZxvpsqbTBXesWl9LqcDxQ5IG09kqp4+jMNYjTIv4dW8qM1IjP+JiZinBQr0XtaN9YE0f5P2qZgMWonk=";

		String xmltrimStr = xmlDOC;
		String xmltrim = xmltrimStr.substring(xmltrimStr.indexOf("<Transaction_Header>"));
		String xmlsB = xmltrim.substring(0, xmltrim.indexOf("<MAC>"));
		String xmlsE = xmltrim.substring(xmltrim.indexOf("</MAC>") + 6, xmltrim.indexOf("</Transaction>"));
		String checkMACdata = xmlsB + xmlsE;
		System.out.println("签名验证的内容:" + checkMACdata);
		// 验证签名
		boolean checkMACflag = false;
		try {
			checkMACflag = RSACoder.verify(checkMACdata.getBytes(), public_key, MAC);
			System.out.println(checkMACflag);
		} catch (Exception e) {
			System.out.println("alipay签名验证异常!业务编码MI10001");
			e.printStackTrace();
		}

		return checkMACflag;
	}

	public static void main(String[] args) {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Transaction><Transaction_Header><transaction_id>MI0001</transaction_id><requester>alipay</requester><target>HWHQ</target><request_time>20130614173419</request_time><version>1.0</version><MAC>BX0weQBxYqFdO32cvZm58rPq3Sx51CwMAN/8Z0MAbtVeewJOifXsOS8Sd+Z1mGHuhLGPwthzTnWdbsNnHYV4loDx+/fmZxvpsqbTBXesWl9LqcDxQ5IG09kqp4+jMNYjTIv4dW8qM1IjP+JiZinBQr0XtaN9YE0f5P2qZgMWonk=</MAC></Transaction_Header><Transaction_Body><delivery_man>1217</delivery_man><password>E1ADC3949BA59ABBE56E057F2F883E</password></Transaction_Body></Transaction>";

		System.out.println(ValidateMAC_publicMethod(xml));

	}

}
