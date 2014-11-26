package cn.explink.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.explink.dao.ExceedFeeDAO;
import cn.explink.domain.ExceedFee;
import cn.explink.domain.User;
import cn.explink.service.ExceedFeeService;
import cn.explink.service.ExplinkUserDetail;

@RequestMapping("/exceedfee")
@Controller
public class ExceedFeeController {

	@Autowired
	ExceedFeeDAO exceedFeeDAO;
	@Autowired
	ExceedFeeService exceedFeeService;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/list")
	public String list(Model model) {
		model.addAttribute("exceedFee", exceedFeeDAO.getExceedFee());
		return "exceedfee/list";
	}

	@RequestMapping("/save/{exceedid}")
	public String save(Model model, HttpServletRequest request, @PathVariable("exceedid") long exceedid) {
		ExceedFee exceedFee = exceedFeeService.loadFormForExceedFeeToEdit(request, exceedid);
		exceedFeeDAO.saveExceedFee(exceedFee);
		model.addAttribute("exceedFee", exceedFeeDAO.getExceedFee());
		logger.info("operatorUser={},超额提示设置->save", getSessionUser().getUsername());
		model.addAttribute("editsave", "修改成功");
		return "exceedfee/list";
	}

}
