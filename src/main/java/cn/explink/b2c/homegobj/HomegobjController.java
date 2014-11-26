package cn.explink.b2c.homegobj;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.BranchDAO;
import cn.explink.enumutil.BranchEnum;
import cn.explink.util.Dom4jParseUtil;

/**
 * 家有购物北京ERP接口
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/homegobj")
public class HomegobjController {
	private Logger logger = LoggerFactory.getLogger(HomegobjController.class);
	@Autowired
	HomegobjService homegobjService;
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;

	@Autowired
	BranchDAO branchDAO;
	@Autowired
	HomegobjInsertCwbDetailTimmer homegobjInsertCwbDetailTimmer;

	@Autowired
	HomegobjService_getOrderDetailList homegobjService_getOrderDetailList;

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {

		String editJsp = "";
		for (B2cEnum fote : B2cEnum.values()) {
			if (fote.getKey() == key) {
				editJsp = fote.getMethod();
				break;
			}
		}
		model.addAttribute("homegobjObject", homegobjService.getHomegobj(key));
		model.addAttribute("warehouselist", branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		model.addAttribute("joint_num", key);
		return "b2cdj/homegobj";

	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(Model model, @PathVariable("id") int key, HttpServletRequest request) {

		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {

			homegobjService.edit(request, key);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
		// 保存

	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		homegobjService.update(key, state);
		// 保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";

	}

	@RequestMapping("/test")
	public @ResponseBody String test(Model model) {
		homegobjService_getOrderDetailList.excutorGetOrders();
		return "SUCCESS";
	}

	@RequestMapping("/request")
	public @ResponseBody String request(HttpServletRequest request, HttpServletResponse response) throws IOException {
		InputStream input = new BufferedInputStream(request.getInputStream());
		String XMLDOC = Dom4jParseUtil.getStringByInputStream(input); // 读取文件流，获得xml字符串
		logger.info("接收测试参数={}", XMLDOC);
		String xmlString = "<Jiayou><header><function_id>JIAYOU0001</function_id><app_id>sxhmj</app_id><signed>ac16fd2f5583972e5f79b302801f9f88</signed><resp_time>20140819113017</resp_time><resp_code>00</resp_code><resp_msg>成功</resp_msg></header><body><orders><order><invc_id>JY2550142360830</invc_id><ord_id>21138353</ord_id><hb_ord_id></hb_ord_id><ord_type>00</ord_type><cod_amt>499.0</cod_amt><length>41.0</length><width>35.0</width><height>38.0</height><sender><name>家有购物集团有限公司</name><postcode>251115</postcode><phone>010598676006387</phone><prov>山东省</prov><city>德州市</city><district>齐河县</district><address>盖世冠威物流园内19号京东库旁</address></sender><receiver><name>刘玉娥</name><phone>****</phone><mobile>137****5808</mobile><prov>陕西省</prov><city>安康市</city><district>汉滨区</district><address>张岭康多装饰有限责任公司王爽收</address></receiver><goods><good><outgo_no>19394316</outgo_no><good_nm>五粮液-52度百鸟朝凤精品团购组</good_nm><good_qty>1</good_qty><good_prc>499.0</good_prc></good></goods></order><order><invc_id>JY2550142360932</invc_id><ord_id>21138356</ord_id><hb_ord_id></hb_ord_id><ord_type>00</ord_type><cod_amt>499.0</cod_amt><length>41.0</length><width>35.0</width><height>38.0</height><sender><name>家有购物集团有限公司</name><postcode>251115</postcode><phone>010598676006387</phone><prov>山东省</prov><city>德州市</city><district>齐河县</district><address>盖世冠威物流园内19号京东库旁</address></sender><receiver><name>刘玉娥</name><phone>****</phone><mobile>137****5808</mobile><prov>陕西省</prov><city>安康市</city><district>汉滨区</district><address>张岭康多装饰有限责任公司王爽收</address></receiver><goods><good><outgo_no>19394317</outgo_no><good_nm>五粮液-52度百鸟朝凤精品团购组</good_nm><good_qty>1</good_qty><good_prc>499.0</good_prc></good></goods></order></orders><page><page_count>1</page_count><page_no>1</page_no><page_size>300</page_size><end_flag>Y</end_flag></page></body></Jiayou>";

		return xmlString;
	}

	@RequestMapping("/feedback")
	public @ResponseBody String feedback(HttpServletRequest request, HttpServletResponse response) throws IOException {
		InputStream input = new BufferedInputStream(request.getInputStream());
		String XMLDOC = Dom4jParseUtil.getStringByInputStream(input); // 读取文件流，获得xml字符串
		logger.info("接收测试参数={}", XMLDOC);
		String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
				+ "<Jiayou>"
				+ "<header>"
				+ "<function_id>JIAYOU0002</function_id>"
				+ "<app_id>114</app_id>"
				+ "<signed>cjEZbG4htYi/Lr/vw97AWPgOeu+FREHkbiBwx7wLsiYU0Mw377nQ0NFEtrIyv3HmgxW4mgzCmxNN8r/krzkXsXFNEfWZX3dA6OcTBugzl6Hg3/EJx6l4oRHRoLtkiTdu5iG2oU/AC1H3/Wo7+m+vX1lehKuhZXC5SpsZMMdBi2M=</signed>"
				+ "<resp_time>20131106182411</resp_time>" + "<resp_code>00</resp_code>" + "<resp_msg>成功</resp_msg>" + "</header>" + "</Jiayou>";
		return xmlString;
	}

	@RequestMapping("/timmer")
	public @ResponseBody String timmer(HttpServletRequest request, HttpServletResponse response) {
		long starttime = System.currentTimeMillis();
		long endtime = System.currentTimeMillis();

		homegobjInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();

		logger.info("执行了临时表-获取[家有购物北京]订单的定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
		return "家有购物北京ERP执行临时表成功";
	}

}
