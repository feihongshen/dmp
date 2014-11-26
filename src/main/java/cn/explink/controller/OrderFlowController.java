package cn.explink.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.explink.dao.CwbDAO;
import cn.explink.dao.OrderFlowDAO;

@Controller
@RequestMapping("/orderflow")
public class OrderFlowController {

	@Autowired
	CwbDAO cwbDao;
	@Autowired
	OrderFlowDAO orderFlowDAO;

	@RequestMapping("/view/{cwb}")
	public String view(@PathVariable("cwb") String cwb, Model model) {
		model.addAttribute("orderFlowList", orderFlowDAO.getOrderFlowListByCwb(cwb));
		model.addAttribute("cwb", cwbDao.getCwbByCwb(cwb));
		return "orderflow/view";
	}

}