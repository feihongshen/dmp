package cn.explink.b2c.tools;

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
@RequestMapping("/exptreason")
public class ExptReasonController {

	@Autowired
	ExptReasonService exptReasonService;
	@Autowired
	ExptReasonDAO exptReasonDAO;
	@Autowired
	CustomerDAO customerDAO;

	@RequestMapping("/add")
	public String add(Model model) throws Exception {
		model.addAttribute("customerlist", customerDAO.getAllCustomers());
		return "jointmanage/exptreason/add";
	}

	@RequestMapping("/create")
	public @ResponseBody String create(HttpServletRequest request, Model model, @RequestParam(value = "expt_code", required = false, defaultValue = "") String expt_code,
			@RequestParam(value = "support_key", required = false, defaultValue = "") String support_keystr) {
		int support_key = -1;
		int b2c_flag = 0;
		String support_keyarr[] = support_keystr.split("_");
		if (support_keyarr != null && support_keyarr.length > 1) { // 标示为b2c
			support_key = Integer.parseInt(support_keyarr[1].toString());
			b2c_flag = 1;
		}
		String expt_type = request.getParameter("expt_type");
		boolean isExistsFlag = exptReasonDAO.IsExistsExptReasonFlag(support_key, expt_code, b2c_flag, expt_type);

		if (isExistsFlag) {
			return "{\"errorCode\":1,\"error\":\"该条异常码已存在!\"}";
		} else {
			ExptReason expt = exptReasonService.LoadingExptEntity(request, support_key, b2c_flag);
			exptReasonDAO.createExptReason(expt);
			return "{\"errorCode\":0,\"error\":\"新建成功\"}";
		}

	}

	@RequestMapping("/list/{page}")
	public String jointManagerByExptReasonEnterPage(HttpServletRequest request, @PathVariable("page") long page,
			@RequestParam(value = "support_key", required = false, defaultValue = "-1") String support_keystr, Model model) {
		long support_key = -1;
		int b2c_flag = 0;
		String support_keyarr[] = support_keystr.split("_");
		if (support_keyarr != null && support_keyarr.length > 1) { // 标示为b2c
			support_key = Long.parseLong(support_keyarr[1].toString());
			b2c_flag = 1;
		} else {
			support_key = Long.parseLong(support_keystr);
		}

		model.addAttribute("exptreasonlist", exptReasonDAO.getExptReasonListByKey(support_key, page, b2c_flag));
		model.addAttribute("page_obj", new Page(exptReasonDAO.getExptReasonListCount(support_key, b2c_flag), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		model.addAttribute("support_key", support_key);
		model.addAttribute("support_key_selected", support_keystr); //Added by leoliao at 2016-07-01 解决POSEnum枚举类里面的编码与客户ID冲突
		model.addAttribute("customerlist", customerDAO.getAllCustomers());
		return "jointmanage/exptreason";
	}

	public static void main(String[] args) {
		String aa = "1_15";
		String bb[] = aa.split("_");

		System.out.println(bb[1]);
	}

	@RequestMapping("/del/{id}")
	public @ResponseBody String del(Model model, @PathVariable("id") long key) {
		exptReasonDAO.exptReasonDel(key);
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";

	}

	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable("id") int key, Model model) {
		model.addAttribute("customerlist", customerDAO.getAllCustomers());
		model.addAttribute("exptreason", exptReasonDAO.getExptReasonEntityByKey(key));
		model.addAttribute("exptid", key);
		return "jointmanage/exptreason/edit";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(@PathVariable("id") long exptid, Model model, @RequestParam(value = "expt_code", required = false, defaultValue = "") String expt_code,
			@RequestParam(value = "support_key", required = false, defaultValue = "") String support_keystr, HttpServletRequest request) {
		int support_key = -1;
		int b2c_flag = 0;
		String support_keyarr[] = support_keystr.split("_");
		if (support_keyarr != null && support_keyarr.length > 1) { // 标示为b2c
			support_key = Integer.parseInt(support_keyarr[1].toString());
			b2c_flag = 1;
		}
		boolean isExistsFlag = exptReasonDAO.IsExistsExptReasonExceptOwnFlag(support_key + "", expt_code, exptid, b2c_flag);

		if (isExistsFlag) {
			return "{\"errorCode\":1,\"error\":\"该条异常码已存在!\"}";
		} else {
			ExptReason expt = exptReasonService.LoadingExptEntity(request, support_key, b2c_flag);
			exptReasonDAO.SaveExptReason(expt, exptid);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		}
	}

}
