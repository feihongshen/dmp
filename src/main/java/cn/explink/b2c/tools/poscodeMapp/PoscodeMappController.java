package cn.explink.b2c.tools.poscodeMapp;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.CustomerDAO;
import cn.explink.util.Page;

@Controller
@RequestMapping("/poscodemapp")
public class PoscodeMappController {

	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	PoscodeMappDAO poscodeMappDAO;
	@Autowired
	PoscodeMappService poscodeMappService;

	@RequestMapping("/add")
	public String add(Model model) throws Exception {
		model.addAttribute("customerlist", customerDAO.getAllCustomers());
		return "jointmanage/poscodemapp/add";
	}

	@RequestMapping("/create")
	public @ResponseBody String create(HttpServletRequest request, Model model, @RequestParam(value = "customerid", required = false, defaultValue = "") long customerid,
			@RequestParam(value = "posenum", required = false, defaultValue = "") long posenum) {

		boolean isExistsFlag = poscodeMappDAO.IsExistsPosCodeFlag(posenum, customerid);

		if (isExistsFlag) {
			return "{\"errorCode\":1,\"error\":\"已存在该设置!\"}";
		} else {
			PoscodeMapp pc = poscodeMappService.LoadingExptEntity(request, posenum);
			poscodeMappDAO.createExptReason(pc);
			return "{\"errorCode\":0,\"error\":\"新建成功\"}";
		}

	}

	@RequestMapping("/list/{page}")
	public String poscodeMapplist(HttpServletRequest request, @PathVariable("page") long page, @RequestParam(value = "posenum", required = false, defaultValue = "-1") long posenum, Model model) {

		model.addAttribute("poscodelist", poscodeMappDAO.getPoscodeMappList(posenum, page));
		Page pageM = new Page(poscodeMappDAO.getPoscodeMappCount(posenum), page, Page.ONE_PAGE_NUMBER);
		model.addAttribute("page_obj", pageM);
		model.addAttribute("page", page);
		model.addAttribute("posenum", posenum);
		model.addAttribute("customerlist", customerDAO.getAllCustomers());
		return "jointmanage/poscodemapp";
	}

	@RequestMapping("/del/{id}")
	public @ResponseBody String del(Model model, @PathVariable("id") long poscodeid) {
		poscodeMappDAO.exptReasonDel(poscodeid);
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";

	}

	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable("id") int key, Model model) {
		model.addAttribute("customerlist", customerDAO.getAllCustomers());
		model.addAttribute("poscodemapp", poscodeMappDAO.getExptReasonEntityByKey(key));
		model.addAttribute("exptid", key);
		return "jointmanage/poscodemapp/edit";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(@PathVariable("id") long poscodeid, Model model, HttpServletRequest request) {

		PoscodeMapp pc = poscodeMappService.LoadingExptEntity(request, 0);
		poscodeMappDAO.SaveExptReason(pc, poscodeid);
		return "{\"errorCode\":0,\"error\":\"修改成功\"}";

	}

}
