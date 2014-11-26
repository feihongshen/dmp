package cn.explink.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.domain.User;
import cn.explink.enumutil.PrintTemplateOpertatetypeEnum;
import cn.explink.print.template.PrintTemplate;
import cn.explink.print.template.PrintTemplateDAO;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.util.Page;

@Controller
@RequestMapping("/printtemplate")
public class PrintTemplateController {

	@Autowired
	PrintTemplateDAO printTemplateDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/add")
	public String add() {
		return "/template/add";
	}

	@RequestMapping("/create")
	public @ResponseBody String create(Model model, @RequestParam(value = "name", required = true) String name, @RequestParam(value = "customname", required = true) String customname,
			@RequestParam(value = "detail", required = true) String detail, @RequestParam(value = "shownum", required = false, defaultValue = "1") long shownum,
			@RequestParam(value = "opertatetype", required = false) long opertatetype) {
		List<PrintTemplate> ptList = printTemplateDAO.getPrintTemplateByWhere(name, detail, shownum, opertatetype, customname);

		if (ptList.size() > 0) {
			return "{\"errorCode\":0,\"error\":\"该模版设置已存在\"}";
		} else {
			if (opertatetype % 2 == 0) {
				shownum = 1;
			}

			long templatetype = 1;

			if (opertatetype == PrintTemplateOpertatetypeEnum.ChuKuHuiZong.getValue() || opertatetype == PrintTemplateOpertatetypeEnum.LingHuoHuiZong.getValue()
					|| opertatetype == PrintTemplateOpertatetypeEnum.TuiGongYingShangChuKuHuiZong.getValue() || opertatetype == PrintTemplateOpertatetypeEnum.TuiHuoChuZhanHuiZong.getValue()
					|| opertatetype == PrintTemplateOpertatetypeEnum.ZhongZhuanChuZhanHuiZong.getValue() || opertatetype == PrintTemplateOpertatetypeEnum.ZhanDianChuZhanHuiZong.getValue()) {
				templatetype = 2;
			}
			PrintTemplate pt = new PrintTemplate();

			pt.setName(name);
			pt.setDetail(detail);
			pt.setShownum(shownum);
			pt.setOpertatetype(opertatetype);
			pt.setCustomname(customname);
			pt.setTemplatetype(templatetype);
			long id = printTemplateDAO.crePrintTemplate(pt);
			logger.info("operatorUser={},交接单模版设置->create", getSessionUser().getUsername());
			return "{\"errorCode\":" + id + ",\"error\":\"创建成功\"}";
		}
	}

	@RequestMapping("/list/{page}")
	public String list(Model model, @PathVariable("page") long page, @RequestParam(value = "name", required = false, defaultValue = "") String name,
			@RequestParam(value = "opertatetype", required = false, defaultValue = "0") long opertatetype) {
		List<PrintTemplate> ptList = printTemplateDAO.getPrintTemplateByPage(page, name, "", opertatetype);
		model.addAttribute("ptList", ptList);
		model.addAttribute("page_obj", new Page(printTemplateDAO.getPrintTemplateCount(name, "", opertatetype), page, Page.ONE_PAGE_NUMBER));
		return "/template/list";
	}

	@RequestMapping("/edit/{id}")
	public String edit(Model model, @PathVariable("id") int id) {
		model.addAttribute("printtemplate", printTemplateDAO.getPrintTemplate(id));
		return "/template/edit";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(@PathVariable("id") int id, @RequestParam(value = "name", required = true) String name, @RequestParam(value = "customname", required = true) String customname,
			@RequestParam(value = "detail", required = true) String detail, @RequestParam(value = "shownum", required = false, defaultValue = "1") long shownum,
			@RequestParam(value = "opertatetype", required = false) long opertatetype) throws Exception {
		List<PrintTemplate> ptList = printTemplateDAO.getPrintTemplateByWhere(name, detail, shownum, opertatetype, customname);

		if (ptList.size() > 0) {
			return "{\"errorCode\":1,\"error\":\"该模版设置已存在\"}";
		} else {
			PrintTemplate pt = printTemplateDAO.getPrintTemplate(id);
			if (opertatetype % 2 == 0) {
				shownum = 1;
			}
			printTemplateDAO.savePrintTemplateById(name, detail, id, shownum, opertatetype, customname, pt.getTemplatetype());
			logger.info("operatorUser={},交接单模版设置->save", getSessionUser().getUsername());
			return "{\"errorCode\":0,\"error\":\"保存成功\"}";
		}
	}

	@RequestMapping("/testprinting_default/{id}")
	public String outbillprinting_default(Model model, @PathVariable("id") long id) {
		PrintTemplate printTemplate = printTemplateDAO.getPrintTemplate(id);

		model.addAttribute("template", printTemplate);

		if (printTemplate.getTemplatetype() == 1) {
			return "/template/testprinting_template";
		} else if (printTemplate.getTemplatetype() == 2) {
			return "/template/testhuizongprinting_template";
		}
		return null;
	}

	@RequestMapping("/del/{id}")
	public @ResponseBody String del(@PathVariable("id") int id) throws Exception {
		printTemplateDAO.delPrintTemplateById(id);
		logger.info("operatorUser={},交接单模版设置->del", getSessionUser().getUsername());
		return "{\"errorCode\":0,\"error\":\"删除成功\"}";
	}
}
