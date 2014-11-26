package cn.explink.b2c.lechong;

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

import cn.explink.b2c.lechong.ws.WebServiceServerByLechong;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.BranchDAO;
import cn.explink.enumutil.BranchEnum;
import cn.explink.util.Dom4jParseUtil;

@Controller
@RequestMapping("/lechong")
public class LechongController {

	private Logger logger = LoggerFactory.getLogger(LechongController.class);

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;
	@Autowired
	LechongService lechongService;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	LechongInsertCwbDetailTimmer smileInsertCwbDetailTimmer;
	@Autowired
	WebServiceServerByLechong webServiceServerByLechong;

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("lechongObject", lechongService.getLechong(key));
		model.addAttribute("warehouselist", branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		model.addAttribute("joint_num", key);
		return "b2cdj/lechong";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String smileSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {
		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {
			lechongService.edit(request, key);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		lechongService.update(key, state);
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	@RequestMapping("/addCwb")
	public @ResponseBody String addCwb(HttpServletRequest request) throws IOException {

		String xml = null;
		InputStream input = new BufferedInputStream(request.getInputStream());
		xml = Dom4jParseUtil.getStringByInputStream(input); // 读取文件流，获得xml字符串
		if (xml.length() == 0 || xml == null) {
			xml = request.getParameter("xml");
			if (xml.length() == 0 || xml == null) {
				return "INPUT";
			}
		}
		String str = lechongService.excutorGetOrders(xml);
		return str;
	}

	@RequestMapping("/addxml")
	public String addXml(HttpServletRequest request) throws IOException {

		return "b2cdj/lechong/addXml";
	}

	@RequestMapping("/delCwb")
	public @ResponseBody String delCwb(HttpServletRequest request) throws Exception {

		String xml = null;
		InputStream input = new BufferedInputStream(request.getInputStream());
		xml = Dom4jParseUtil.getStringByInputStream(input); // 读取文件流，获得xml字符串
		if (xml.length() == 0 || xml == null) {
			xml = request.getParameter("xml");
			if (xml.length() == 0 || xml == null) {
				return "INPUT";
			}
		}

		String xmlstr = lechongService.CancelOrders(xml);
		return xmlstr;
	}

	@RequestMapping("/delxml")
	public String delXml(HttpServletRequest request) throws IOException {

		return "b2cdj/lechong/delXml";
	}

	@RequestMapping("/feedback")
	public @ResponseBody String request(HttpServletRequest request, HttpServletResponse response) throws IOException {
		InputStream input = new BufferedInputStream(request.getInputStream());
		String XMLDOC = Dom4jParseUtil.getStringByInputStream(input); // 读取文件流，获得xml字符串
		logger.info("接收测试参数={}", XMLDOC);
		String cwb = request.getParameter("cwb");

		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>" + "<UpdateInfoResponse>" + "<DoID>" + cwb + "</DoID>" + "<MailNO>210020391</MailNO>" + "<flag>0</flag>" + "<desc>成功</desc>"
				+ "</UpdateInfoResponse>";
		return xml;
	}

	@RequestMapping("/feedback1")
	public @ResponseBody String request1(HttpServletRequest request, HttpServletResponse response) throws IOException {
		InputStream input = new BufferedInputStream(request.getInputStream());
		String XMLDOC = Dom4jParseUtil.getStringByInputStream(input); // 读取文件流，获得xml字符串
		logger.info("接收测试参数={}", XMLDOC);
		String cwb = request.getParameter("cwb");
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>" + "<UpdateInfoResponse>" + "<DoID>" + cwb + "</DoID>" + "<MailNO>210020391</MailNO>" + "<flag>1</flag>" + "<desc>处理失败</desc>"
				+ "</UpdateInfoResponse>";
		return xml;
	}

	@RequestMapping("/timmer")
	public @ResponseBody String timmer(HttpServletRequest request, HttpServletResponse response) {
		long starttime = System.currentTimeMillis();
		long endtime = System.currentTimeMillis();

		smileInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();

		logger.info("执行了临时表-获取[乐宠]订单的定时器,本次耗时:{}秒", ((endtime - starttime) / 1000));
		return "乐宠ERP执行临时表成功";
	}

}
