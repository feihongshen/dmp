package cn.explink.b2c.tools;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.CustomerDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.domain.Reason;
import cn.explink.enumutil.ReasonTypeEnum;

@Controller
@RequestMapping("/exptcodejoint")
public class ExptCodeJointController {

	@Autowired
	ExptReasonService exptReasonService;
	@Autowired
	ExptCodeJointService exptCodeJointService;
	@Autowired
	ExptReasonDAO exptReasonDAO;
	@Autowired
	ExptCodeJointDAO exptCodeJointDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	ReasonDao reasonDAO;

	@RequestMapping("/add")
	public String add(Model model) throws Exception {
		model.addAttribute("customerlist", customerDAO.getAllCustomers());
		return "jointmanage/exptcodejoint/add";
	}

	@RequestMapping("/reasonByType")
	public @ResponseBody String reasonByType(Model model, @RequestParam("expt_type") long expt_type) {
		if (expt_type > 0) {
			List<Reason> reasonlist = reasonDAO.getAllReasonByReasonType(expt_type);
			return JSONArray.fromObject(reasonlist).toString();
		} else {
			return "[]";
		}

	}

	@RequestMapping("/searchExptReasonById")
	public @ResponseBody String searchExptReasonById(Model model, @RequestParam("expt_type") long expt_type, @RequestParam("support_key") String support_keystr) {
		int support_key = -1;
		int b2c_flag = 0;
		String support_keyarr[] = support_keystr.split("_");
		if (support_keyarr != null && support_keyarr.length > 1) { // 标示为b2c
			support_key = Integer.parseInt(support_keyarr[1].toString());
			b2c_flag = 1;
		} else {
			support_key = Integer.parseInt(support_keystr);
		}
		if (expt_type > 0 && (support_key >= 0||support_key==-2)) {
			List<ExptReason> reasonlist = exptReasonDAO.getExptReasonListByMoreKey(support_key, expt_type, b2c_flag);
			return JSONArray.fromObject(reasonlist).toString();
		} else {
			return "[]";
		}

	}

	@RequestMapping("/create")
	public @ResponseBody String create(HttpServletRequest request, Model model, @RequestParam(value = "reasonid", required = false, defaultValue = "-1") long reasonid,
			@RequestParam(value = "exptsupportreason", required = false, defaultValue = "-1") long exptsupportreason) {

		boolean isExistsFlag = exptCodeJointDAO.IsExistsExptMatchFlag(reasonid, exptsupportreason);
		if (isExistsFlag) {
			return "{\"errorCode\":1,\"error\":\"该条异常码配对信息已存在!\"}";
		} else {
			ExptCodeJoint exptcode = exptCodeJointService.LoadingExptEntity(request);
			exptCodeJointDAO.createExptCodeReason(exptcode);
			return "{\"errorCode\":0,\"error\":\"新建成功\"}";
		}

	}

	@RequestMapping("/list/{page}")
	public String jointManagerByExptReasonEnterPage(HttpServletRequest request, @PathVariable("page") long page,
			@RequestParam(value = "expt_type", required = false, defaultValue = "-1") long expt_type,
			@RequestParam(value = "support_key", required = false, defaultValue = "-1") String support_keystr, Model model) {
		long support_key = -1;
		int b2c_flag = -1;
		String support_keyarr[] = support_keystr.split("_");
		if (support_keyarr != null && support_keyarr.length > 1) { // 标示为b2c
			support_key = Long.parseLong(support_keyarr[1].toString());
			b2c_flag = 1;
		} else {
			support_key = Long.parseLong(support_keystr);
			b2c_flag = 0;
		}
		if ("-1".equals(support_keystr)) {
			b2c_flag = -1;
		}
		model.addAttribute("customerlist", customerDAO.getAllCustomers());
		model.addAttribute("exptmatchlist", exptCodeJointDAO.getExpMatchListByKey(expt_type, support_key, b2c_flag));
		model.addAttribute("expt_type", expt_type);
		model.addAttribute("support_key", support_key);
		return "jointmanage/jointexptcode";
	}

	@RequestMapping("/del/{id}")
	public @ResponseBody String del(Model model, @PathVariable("id") long key) {
		exptCodeJointDAO.exptcodeJoint_delete(key);
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";

	}

	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable("id") int key, Model model) {
		model.addAttribute("exptcodereason", exptCodeJointDAO.getExpMatchListByKeyEdit(key));
		model.addAttribute("exptid", key);
		model.addAttribute("customerlist", customerDAO.getAllCustomers());
		return "jointmanage/exptcodejoint/edit";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(HttpServletRequest request, Model model, @RequestParam(value = "reasonid", required = false, defaultValue = "-1") long reasonid,
			@RequestParam(value = "exptsupportreason", required = false, defaultValue = "-1") long exptsupportreason,
			@RequestParam(value = "exptcodeid", required = false, defaultValue = "-1") long exptcodeid) {
		boolean isExistsFlag = exptCodeJointDAO.IsExistsExptMatchFlag_update(reasonid, exptsupportreason, exptcodeid);
		if (isExistsFlag) {
			return "{\"errorCode\":1,\"error\":\"该条匹配对已存在!\"}";
		} else {
			ExptCodeJoint exptcode = exptCodeJointService.LoadingExptEntity(request);
			exptCodeJointDAO.updateExptCodeReason(exptcode, exptcodeid);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		}
	}

}
