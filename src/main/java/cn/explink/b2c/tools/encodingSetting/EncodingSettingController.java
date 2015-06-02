package cn.explink.b2c.tools.encodingSetting;

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
@RequestMapping("/encodingsetting")
public class EncodingSettingController {

	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	EncodingSettingDAO encodingSettingDAO;
	@Autowired
	EncodingSettingService encodingSettingService;

	@RequestMapping("/add")
	public String add(Model model) throws Exception {
		model.addAttribute("customerlist", customerDAO.getAllCustomers());
		return "jointmanage/encodingsetting/add";
	}

	@RequestMapping("/create")
	public @ResponseBody String create(HttpServletRequest request, Model model, @RequestParam(value = "customerid", required = false, defaultValue = "") long customerid
			) {

		boolean isExistsFlag = encodingSettingDAO.IsExistsPosCodeFlag( customerid);

		if (isExistsFlag) {
			return "{\"errorCode\":1,\"error\":\"已存在该设置!\"}";
		} else {
			EncodingSetting pc = encodingSettingService.LoadingExptEntity(request, customerid);
			encodingSettingDAO.createExptReason(pc);
			return "{\"errorCode\":0,\"error\":\"新建成功\"}";
		}

	}

	@RequestMapping("/list/{page}")
	public String encodingSettinglist(HttpServletRequest request, @PathVariable("page") long page, @RequestParam(value = "customerid", required = false, defaultValue = "-1") long customerid, Model model) {

		model.addAttribute("encodinglist", encodingSettingDAO.getEncodingSettingList(customerid, page));
		Page pageM = new Page(encodingSettingDAO.getEncodingSettingCount(customerid), page, Page.ONE_PAGE_NUMBER);
		model.addAttribute("page_obj", pageM);
		model.addAttribute("page", page);
		
		model.addAttribute("customerlist", customerDAO.getAllCustomers());
		return "jointmanage/encodingsetting";
	}

	@RequestMapping("/del/{id}")
	public @ResponseBody String del(Model model, @PathVariable("id") long poscodeid) {
		encodingSettingDAO.exptReasonDel(poscodeid);
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";

	}

	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable("id") int key, Model model) {
		model.addAttribute("customerlist", customerDAO.getAllCustomers());
		model.addAttribute("encodingsetting", encodingSettingDAO.getExptReasonEntityByKey(key));
		model.addAttribute("exptid", key);
		return "jointmanage/encodingsetting/edit";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(@PathVariable("id") long poscodeid, Model model, HttpServletRequest request) {

		EncodingSetting pc = encodingSettingService.LoadingExptEntity(request, 0);
		encodingSettingDAO.SaveExptReason(pc, poscodeid);
		return "{\"errorCode\":0,\"error\":\"修改成功\"}";

	}

}
