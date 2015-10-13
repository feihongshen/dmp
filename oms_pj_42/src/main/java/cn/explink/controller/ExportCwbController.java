package cn.explink.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.explink.dao.CwbDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.SetExportFieldDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.SetExportField;

@Controller
@RequestMapping("/exportcwb")
public class ExportCwbController {
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	SetExportFieldDAO setExportFieldDAO;
	@Autowired
	GetDmpDAO getDmpDAO;
	@Autowired
	JdbcTemplate jdbcTemplate;

	@RequestMapping("/list")
	public String list(Model model) {
		List<SetExportField> listSetExportField = setExportFieldDAO.getAllSetExportField();
		model.addAttribute("listSetExportField", listSetExportField);
		return "exportcwb/list";
	}

	@RequestMapping("/save")
	public String save(Model model, HttpServletRequest request) {
		setExportFieldDAO.saveGetExportStateZero();
		String fieldid[] = request.getParameterValues("fieldid");
		if (fieldid != null) {
			for (int i = 0; i < fieldid.length; i++) {
				setExportFieldDAO.saveSetExportFieldById(fieldid[i]);
			}
		}
		List<SetExportField> listSetExportField = setExportFieldDAO.getAllSetExportField();
		model.addAttribute("listSetExportField", listSetExportField);
		model.addAttribute("exportSuccess", "设置成功");
		return "exportcwb/list";
	}

	@RequestMapping("/exportdate")
	public void exportdate(Model model) {
		List<SetExportField> listSetExportField = setExportFieldDAO.getSetExportFieldByExportstate();
		StringBuffer str = new StringBuffer();
		String sql = "";
		if (listSetExportField != null) {
			for (SetExportField f : listSetExportField) {
				str.append(" CONVERT(" + f.getFieldenglishname() + " USING gb2312),");
			}
			sql += str.substring(0, str.length() - 1);
		} else {
			sql += " * ";
		}
		jdbcTemplate.execute("SELECT " + sql + " FROM express_ops_cwb_detail INTO OUTFILE 'd:/outOrder.xls'");
	}

}
