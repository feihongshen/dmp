package cn.explink.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.ReasonDao;
import cn.explink.dao.TransferResMatchDao;
import cn.explink.enumutil.ReasonTypeEnum;
import cn.explink.util.Page;

@Controller
@RequestMapping("/transferReason")
public class TransferResMatchController {
	@Autowired
	ReasonDao reasonDao;
	@Autowired
	TransferResMatchDao transferReasonDao;

	@RequestMapping("/list/{page}")
	public String list(Model model, @PathVariable("page") long page, @RequestParam(value = "systemreason", required = false, defaultValue = "") String systemreason) {

		model.addAttribute("reasonList", transferReasonDao.getReasonMatchByPage(page, systemreason));
		model.addAttribute("page_obj", new Page(transferReasonDao.getReasonMatchCountByPage(systemreason), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "transferResmatch/list";
	}

	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable("id") long id, Model model) {

		model.addAttribute("transferResMatch", transferReasonDao.getTransferResMatch(id));
		model.addAttribute("transreasonList", transferReasonDao.getSystemTransferResList());
		model.addAttribute("reasonList", reasonDao.getAllReasonByReasonType(ReasonTypeEnum.ChangeTrains.getValue()));
		return "transferResmatch/edit";
	}

	@RequestMapping("/add")
	public String add(Model model) {

		model.addAttribute("transreasonList", transferReasonDao.getSystemTransferResList());

		model.addAttribute("reasonList", reasonDao.getAllReasonByReasonType(ReasonTypeEnum.ChangeTrains.getValue()));
		return "transferResmatch/add";
	}

	@RequestMapping("/del/{id}")
	public @ResponseBody String del(Model model, @PathVariable("id") long id) {
		transferReasonDao.deleteReason(id);

		return "{\"errorCode\":0,\"error\":\"操作成功\"}";

	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(Model model, HttpServletRequest request, @PathVariable("id") long id, @RequestParam("transferReasonid") long transferReasonid,
			@RequestParam("reasonid") long reasonid) {

		long counts = transferReasonDao.IsExistsExptMatchFlag(reasonid, transferReasonid);
		if (counts > 0) {
			return "{\"errorCode\":1,\"error\":\"该匹配已存在\"}";
		}
		long counts1 = transferReasonDao.IsExistsReasonFlag(reasonid);
		if (counts1 > 0) {
			return "{\"errorCode\":1,\"error\":\"常用语中转原因最多只能匹配一次\"}";
		}

		transferReasonDao.saveReason(reasonid, transferReasonid, id);
		return "{\"errorCode\":0,\"error\":\"修改成功\"}";
	}

	@RequestMapping("/create")
	public @ResponseBody String create(Model model, HttpServletRequest request, @RequestParam("reasonid") long reasonid, @RequestParam("transferReasonid") long transferReasonid) {

		long counts = transferReasonDao.IsExistsExptMatchFlag(reasonid, transferReasonid);
		if (counts > 0) {
			return "{\"errorCode\":1,\"error\":\"该匹配已存在\"}";
		}
		long counts1 = transferReasonDao.IsExistsReasonFlag(reasonid);
		if (counts1 > 0) {
			return "{\"errorCode\":1,\"error\":\"常用语中转原因最多只能匹配一次\"}";
		}
		transferReasonDao.creReason(reasonid, transferReasonid);
		return "{\"errorCode\":0,\"error\":\"新建成功\"}";
	}

}
