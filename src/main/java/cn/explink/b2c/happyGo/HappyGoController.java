package cn.explink.b2c.happyGo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.jumei.AnalyzXMLJuMeiHandler;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.BranchDAO;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.EmailDate;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.util.DateTimeUtil;

@Controller
@RequestMapping("/happyGo")
public class HappyGoController {
	@Autowired
	HappyGoService happyGoService;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	DataImportService_B2c dataImportservice;

	private Logger logger = LoggerFactory.getLogger(HappyGoController.class);

	@RequestMapping("/requestHappy")
	public @ResponseBody String connectionHappygo() {
		HappyGo happy = happyGoService.getHappyGo(B2cEnum.happyGo.getKey());
		happyGoService.judgmentLanded(happy);
		happyGoService.callbackLanded(happy);
		return "请求快乐购结束！！！";
	}

	@RequestMapping("/Insert")
	public @ResponseBody String connectionHappy(HttpServletRequest request, HttpServletResponse response) {
		HappyGo happy = happyGoService.getHappyGo(B2cEnum.happyGo.getKey());
		happyGoService.insertDelivery(happy, FunctionForHappy.ChuKu.getText());// detail进入主表定时器别忘记加
		happyGoService.insertCallback(happy, FunctionForHappy.HuiShou.getText());
		return "订单导入执行完毕！！";

	}

	@RequestMapping("/go")
	public @ResponseBody String go() {
		happyGoService.happyGoForDetail();
		happyGoService.timerHappy();
		return "执行定时器完毕！！！！";

	}

	@RequestMapping("/testhappygo")
	public String test() {

		return "/b2cdj/testFile";

	}

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("warehouselist", branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		model.addAttribute("happyObject", happyGoService.getHappyGo(B2cEnum.happyGo.getKey()));
		model.addAttribute("joint_num", key);
		return "/b2cdj/happy";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(@PathVariable("id") int joint_num, HttpServletRequest request) {

		String password = request.getParameter("password");
		if (password != null && "explink".equals(password)) {
			happyGoService.edit(request, joint_num);

			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":0,\"error\":\"密码错误！\"}";
		}
	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		happyGoService.update(key, state);

		// 保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	@RequestMapping("/happyyou")
	public String ToInsertHappigoInfo(Model model, HttpServletRequest request, @RequestParam(value = "test", required = false, defaultValue = "-1") long test) {
		String dd = "失败";
		if (test > 0) {
			HappyGo happy = happyGoService.getHappyGo(B2cEnum.happyGo.getKey());
			String code = request.getParameter("leixing");
			String PICI = request.getParameter("pici");
			String retrun_xml = request.getParameter("testarea");
			String time = request.getParameter("time");
			dd = happyGoService.ggHappy(retrun_xml, code, happy, PICI, time);
		}
		model.addAttribute("dd", dd);
		return "/b2cdj/testHappiInfo";

	}

	@RequestMapping("/ggHappy")
	public void gghappy() {
		happyGoService.gethappygoInfoTimmer();
	}
}
