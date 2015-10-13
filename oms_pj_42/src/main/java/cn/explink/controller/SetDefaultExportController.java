package cn.explink.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.SetExportFieldDAO;
import cn.explink.domain.SetExportField;

@Controller
@RequestMapping("/setdefaultexport")
public class SetDefaultExportController {

	@Autowired
	SetExportFieldDAO setExportFieldDAO;

	@RequestMapping("/list")
	public String list(Model model) {
		return "dufaltexport/listf";
	}

	@RequestMapping("/edit")
	public String edit(Model model) {

		List<SetExportField> defaultExpFields = setExportFieldDAO.getDefaultExportField();
		List<SetExportField> listSetExportField = setExportFieldDAO.getAllSetExportField();
		int field[] = new int[defaultExpFields.size()];
		for (int i = 0; i < defaultExpFields.size(); i++) {
			field[i] = defaultExpFields.get(i).getId();
		}
		model.addAttribute("listSetExportField", listSetExportField);
		model.addAttribute("field", field);
		return "dufaltexport/editf";
	}

	@RequestMapping("/save")
	public @ResponseBody String save(Model model, HttpServletRequest request, @RequestParam(value = "fieldid", required = false, defaultValue = "") String fieldids) {

		String fieldid[] = {};
		String mouldfieldids = "";

		if (fieldids.length() > 0) {
			fieldid = fieldids.split(",");
		}
		if (fieldid.length > 0) {
			StringBuffer str = new StringBuffer();
			for (int i = 0; i < fieldid.length; i++) {
				str.append(fieldid[i] + ",");
			}
			mouldfieldids = str.substring(0, str.length() - 1).toString();
		}
		setExportFieldDAO.saveGetExportStateZero();
		setExportFieldDAO.saveDefaultExportFieldById(mouldfieldids);
		return "{\"errorCode\":0,\"error\":\"修改成功\"}";
	}

	@RequestMapping("/view")
	public String view(Model model) {

		List<SetExportField> defaultExpFields = setExportFieldDAO.getDefaultExportField();
		List<SetExportField> listSetExportField = setExportFieldDAO.getAllSetExportField();
		int field[] = new int[defaultExpFields.size()];
		for (int i = 0; i < defaultExpFields.size(); i++) {
			field[i] = defaultExpFields.get(i).getId();
		}
		model.addAttribute("listSetExportField", listSetExportField);
		model.addAttribute("field", field);
		return "dufaltexport/viewf";
	}

}
