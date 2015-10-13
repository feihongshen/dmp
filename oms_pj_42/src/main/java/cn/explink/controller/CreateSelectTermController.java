package cn.explink.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.explink.dao.CreateSelectTermDAO;
import cn.explink.domain.CreateSelectTerm;

@RequestMapping("/createselectterm")
@Controller
public class CreateSelectTermController {

	@Autowired
	CreateSelectTermDAO createSelectTermDao;

	@RequestMapping("/list")
	public String list(Model model) {
		List<CreateSelectTerm> list = createSelectTermDao.getAllCreateSelectTerms();
		model.addAttribute("createselecttermList", list);
		return "recordselect/createselectterm/list";
	}
}
