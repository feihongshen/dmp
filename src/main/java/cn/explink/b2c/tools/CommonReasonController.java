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

import cn.explink.dao.CommonDAO;
import cn.explink.util.Page;

@Controller
@RequestMapping("/Commreason")
public class CommonReasonController {
	@Autowired
	CommonDAO commonDAO;
	@Autowired
	ExptReasonDAO exptReasonDAO;
	@Autowired
	CommonExptDao commonExptDao;
	@Autowired
	commonExptService commonExptService;

	@RequestMapping("/add")
	public String add(Model model) throws Exception {
		model.addAttribute("customerlist", commonDAO.getAllCommons());
		return "jointmanage/commonexpt/add";
	}

	@RequestMapping("/create")
	public @ResponseBody String create(HttpServletRequest request, Model model, @RequestParam(value = "expt_code", required = false, defaultValue = "") String expt_code,
			@RequestParam(value = "customercode", required = false, defaultValue = "") String commoncode) {
		boolean isExistsFlag = commonExptDao.IsExistsExptJointReasonFlag(commoncode, expt_code);

		if (isExistsFlag) {
			return "{\"errorCode\":1,\"error\":\"该条异常码已存在!\"}";
		} else {
			CommonReason expt = commonExptService.LoadingExptEntity(request);
			commonExptDao.createCommonReason(expt);
			return "{\"errorCode\":0,\"error\":\"新建成功\"}";
		}

	}

	@RequestMapping("/list/{page}")
	public String jointManagerByExptReasonEnterPage(HttpServletRequest request, @PathVariable("page") long page,
			@RequestParam(value = "support_key", required = false, defaultValue = "-1") int support_keystr, Model model) {

		model.addAttribute("exptreasonlist", commonExptDao.getcommonListByCommonid(page, support_keystr));
		model.addAttribute("page_obj", new Page(commonExptDao.getExptReasonListCount(support_keystr), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		model.addAttribute("support_key", support_keystr);
		model.addAttribute("commonlist", commonDAO.getAllCommons());
		return "jointmanage/cusreason";
	}

	@RequestMapping("/del/{id}")
	public @ResponseBody String del(Model model, @PathVariable("id") long key) {
		commonExptDao.exptReasonDel(key);
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";

	}

	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable("id") int key, Model model) {
		model.addAttribute("customerlist", commonDAO.getAllCommons());
		model.addAttribute("exptreason", commonExptDao.getExptReasonEntityByKey(key));
		model.addAttribute("exptid", key);
		return "jointmanage/commonexpt/edit";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(@PathVariable("id") long exptid, Model model, @RequestParam(value = "expt_code", required = false, defaultValue = "") String expt_code,
			@RequestParam(value = "customercode", required = false, defaultValue = "") String commoncode, @RequestParam(value = "expt_msg", required = false, defaultValue = "") String expt_msg,
			HttpServletRequest request) {

		boolean isExistsFlag = commonExptDao.IsExistsExptReasonFlag(commoncode, expt_code, expt_msg);

		if (isExistsFlag) {
			return "{\"errorCode\":1,\"error\":\"该条异常码已存在!\"}";
		} else {
			CommonReason expt = commonExptService.LoadingExptEntity(request);
			commonExptDao.SaveExptReason(expt, exptid);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		}
	}

	/*
	 * 关联表
	 */
	@RequestMapping("/show/{page}")
	public String jointManagerByExptReasonEnterPage(HttpServletRequest request, @PathVariable("page") long page,
			@RequestParam(value = "expt_type", required = false, defaultValue = "-1") long expt_type, @RequestParam(value = "support_key", required = false, defaultValue = "-1") long support_keystr,
			Model model) {
		model.addAttribute("commonlist", commonDAO.getAllCommons());
		model.addAttribute("page", page);
		model.addAttribute("page_obj", new Page(commonExptDao.getExptReasonJointListCount(), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("exptmatchlist", commonExptDao.getExpMatchListByKeyandcode(expt_type, support_keystr, page));
		model.addAttribute("expt_type", expt_type);
		model.addAttribute("support_key", support_keystr);
		return "jointmanage/cuscodejoint";
	}

	@RequestMapping("/deljoint/{id}")
	public @ResponseBody String deljoint(Model model, @PathVariable("id") long key) {
		commonExptDao.exptcodeJoint_delete(key);
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";

	}

	@RequestMapping("/editjoint/{id}")
	public String editjoint(@PathVariable("id") int key, Model model) {
		model.addAttribute("customerlist", commonDAO.getAllCommons());
		model.addAttribute("exptreason", commonExptDao.getExpMatchListByKeyEdit(key));
		model.addAttribute("exptid", key);

		return "jointmanage/commonexpt/editjoint";
	}

	@RequestMapping("/jointSave")
	public @ResponseBody String savejoint(Model model, @RequestParam(value = "expt_type", required = false, defaultValue = "") long expt_type,
			@RequestParam(value = "reasonid", required = false, defaultValue = "") long reasonid, @RequestParam(value = "exptcodeid", required = false, defaultValue = "") long commoncode,
			@RequestParam(value = "exptsupportreason", required = false, defaultValue = "-1") long exptid,

			HttpServletRequest request) {

		boolean isExistsFlag = commonExptDao.IsExistsExptMatchFlag(reasonid, exptid);

		if (isExistsFlag) {
			return "{\"errorCode\":1,\"error\":\"该条异常码已存在!\"}";
		} else {
			CommonJoint expt = commonExptService.LoadingJointEntity(request);
			commonExptDao.updateCommonJointCodeReason(expt, commoncode);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		}
	}

	@RequestMapping("/createJoint")
	public @ResponseBody String createJoint(HttpServletRequest request, Model model, @RequestParam(value = "reasonid", required = false, defaultValue = "-1") long reasonid,
			@RequestParam(value = "support_key", required = false, defaultValue = "-1") long support_key,
			@RequestParam(value = "exptsupportreason", required = false, defaultValue = "-1") long exptsupportreason) {

		boolean isExistsFlag = commonExptDao.IsExistsExptMatchFlag(reasonid, exptsupportreason);
		if (isExistsFlag) {
			return "{\"errorCode\":1,\"error\":\"该条异常码配对信息已存在!\"}";
		} else {
			CommonJoint expt = commonExptService.LoadingJointEntity(request);
			commonExptDao.createExptCodeReason(expt);
			return "{\"errorCode\":0,\"error\":\"新建成功\"}";
		}

	}

	@RequestMapping("/addjoint")
	public String addjoint(Model model) throws Exception {
		model.addAttribute("customerlist", commonDAO.getAllCommons());
		return "jointmanage/commonexpt/addjoint";
	}

	@RequestMapping("/searchExptReasonById")
	public @ResponseBody String searchExptReasonById(Model model, @RequestParam("expt_type") long expt_type, @RequestParam("support_key") long support_keystr) {
		List<CommonReason> reasonlist = commonExptDao.getExptReasonListByMoreKey(support_keystr, expt_type);
		return JSONArray.fromObject(reasonlist).toString();

	}
}
