package cn.explink.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.explink.dao.AutomaticSetDAO;
import cn.explink.domain.AutomaticSet;

@RequestMapping("/JmsCenter")
@Controller
public class JmsCenterController {
	@Autowired
	AutomaticSetDAO automaticSetDAO;

	@RequestMapping("/editlink")
	public String editlink(Model model, @RequestParam("allParm") String allParm) {

		automaticSetDAO.saveAutomaticSetToIsauto();
		if (allParm.length() > 0) {
			String[] arr = allParm.substring(0, allParm.length() - 1).split(",");
			long isauto = 1;
			String nowlinkname = "";
			for (int i = 0; i < arr.length; i++) {
				nowlinkname = arr[i];
			}

			automaticSetDAO.saveAutomaticSetByNowlinkname(isauto, nowlinkname);
		}

		List<AutomaticSet> aslist = automaticSetDAO.getAllAutomaticSet();
		model.addAttribute("aslist", aslist);
		// orderFlowSendToService.init();
		return "automaticset/list";
	}

	@RequestMapping("/getlink")
	public String getlink(Model model) {
		List<AutomaticSet> aslist = automaticSetDAO.getAllAutomaticSet();
		model.addAttribute("aslist", aslist);
		return "automaticset/edit";
	}

}
