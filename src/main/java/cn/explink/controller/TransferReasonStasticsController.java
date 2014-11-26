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
import cn.explink.dao.TransferReasonStasticsDao;
import cn.explink.dao.TransferResMatchDao;
import cn.explink.enumutil.ReasonTypeEnum;
import cn.explink.util.Page;

@Controller
@RequestMapping("/transferReasonStastics")
public class TransferReasonStasticsController {
	@Autowired
	ReasonDao reasonDao;
	@Autowired
	TransferReasonStasticsDao transferReasonStasticsDao;

	@RequestMapping("/list/{page}")
	public String list(Model model, @PathVariable("page") long page, @RequestParam(value = "systemreason", required = false, defaultValue = "") String systemreason) {

		return "transferResmatch/list";
	}

}
