package cn.explink.b2c.tools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.explink.core_down.EpaiApiDAO;
import cn.explink.b2c.tools.encodingSetting.EncodingSettingDAO;
import cn.explink.b2c.tools.poscodeMapp.PoscodeMappDAO;
import cn.explink.b2c.tools.power.JointPowerDAO;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CommonDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.enumutil.BranchEnum;
import cn.explink.util.Page;

@Controller
@RequestMapping("/jointManage")
public class JointController {
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointPowerDAO powerDao;
	@Autowired
	ExptReasonDAO exptReasonDAO;
	@Autowired
	ExptCodeJointDAO exptCodeJointDAO;
	@Autowired
	ReasonDao reasonDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	PoscodeMappDAO poscodeMappDAO;

	@Autowired
	EpaiApiDAO epaiApiDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	CommonExptDao commonExptDao;
	@Autowired
	CommonDAO commonDAO;
	@Autowired
	JobUtil jobUtil;
	
	@Autowired
	EncodingSettingDAO encodingSettingDAO;

	@RequestMapping("/init")
	public @ResponseBody String initTaskTimmer(Model model) {
		jobUtil.updateBatcnitialThreadMap();

		return "初始化定时器成功";
	}

	@RequestMapping("/")
	public String jointManagerEnterPage(Model model) {
		model.addAttribute("posList", jiontDAO.getJointEntityByPos());
		return "jointmanage/posjoint";
	}

	@RequestMapping("/jointb2c")
	public String jointManagerByB2CEnterPage(Model model) {
		model.addAttribute("b2cList", jiontDAO.getJointEntityByB2c());
		model.addAttribute("b2cenumlist", powerDao.getJointPowerList(1));
		return "jointmanage/b2cjoint";
	}

	@RequestMapping("/jointpos")
	public String jointManagerByPosEnterPage(Model model) {
		model.addAttribute("posList", jiontDAO.getJointEntityByPos());
		return "jointmanage/posjoint";
	}

	@RequestMapping("/exptreason/{page}")
	public String jointManagerByExptReasonEnterPage(@PathVariable("page") long page, @RequestParam(value = "support_key", required = false, defaultValue = "-1") long support_key, Model model) {
		model.addAttribute("exptreasonlist", exptReasonDAO.getExptReasonListByKey(support_key, page, 0));
		model.addAttribute("customerlist", customerDAO.getAllCustomers());
		model.addAttribute("page_obj", new Page(exptReasonDAO.getExptReasonListCount(support_key, 0), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "jointmanage/exptreason";
	}

	@RequestMapping("/getchengyunshang/{page}")
	public String getCusreason(@PathVariable("page") long page, @RequestParam(value = "support_key", required = false, defaultValue = "-1") long support_key, Model model) {
		model.addAttribute("exptreasonlist", commonExptDao.getAllcommonListByKey(page));
		model.addAttribute("commonlist", commonDAO.getAllCommons());
		model.addAttribute("page_obj", new Page(commonExptDao.getExptReasonListCount(support_key), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);

		return "jointmanage/cusreason";

	}

	@RequestMapping("/exptcodejoint/{page}")
	public String jointManagerByExptCodeJointEnterPage(@PathVariable("page") long page, @RequestParam(value = "expt_type", required = false, defaultValue = "-1") long expt_type,
			@RequestParam(value = "support_key", required = false, defaultValue = "-1") long support_key, Model model) {

		model.addAttribute("exptmatchlist", exptCodeJointDAO.getExpMatchListByKey(expt_type, support_key, -1));
		model.addAttribute("customerlist", customerDAO.getAllCustomers());
		model.addAttribute("page", page);
		return "jointmanage/jointexptcode";
	}

	@RequestMapping("/Cuscodejoint/{page}")
	public String jointManagerByCuscodejointJointEnterPage(@PathVariable("page") long page, @RequestParam(value = "expt_type", required = false, defaultValue = "-1") long expt_type,
			@RequestParam(value = "support_key", required = false, defaultValue = "-1") long support_key, Model model) {
		model.addAttribute("page_obj", new Page(commonExptDao.getExptReasonJointListCount(), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("exptmatchlist", commonExptDao.getExpMatchListByKeyandcode(-1, -1, page));
		model.addAttribute("commonlist", commonDAO.getAllCommons());
		model.addAttribute("page", page);
		return "jointmanage/cuscodejoint";
	}

	@RequestMapping("/poscodemapp/{page}")
	public String poscodemapp(@PathVariable("page") long page, @RequestParam(value = "posenum", required = false, defaultValue = "-1") long posenum, Model model) {
		model.addAttribute("poscodelist", poscodeMappDAO.getPoscodeMappList(posenum, page));
		model.addAttribute("customerlist", customerDAO.getAllCustomers());
		Page pageM = new Page(poscodeMappDAO.getPoscodeMappCount(posenum), page, Page.ONE_PAGE_NUMBER);
		model.addAttribute("page_obj", pageM);
		model.addAttribute("page", page);
		return "jointmanage/poscodemapp";
	}
	
	@RequestMapping("/encodingsetting/{page}")
	public String encodingsetting(@PathVariable("page") long page, @RequestParam(value = "customerid", required = false, defaultValue = "-1") long customerid, Model model) {
		model.addAttribute("encodingsetlist", encodingSettingDAO.getEncodingSettingList(customerid, page));
		model.addAttribute("customerlist", customerDAO.getAllCustomers());
		Page pageM = new Page(encodingSettingDAO.getEncodingSettingCount(customerid), page, Page.ONE_PAGE_NUMBER);
		model.addAttribute("page_obj", pageM);
		model.addAttribute("page", page);
		return "jointmanage/encodingsetting";
	}

	@RequestMapping("/epaiApi/{page}")
	public String epaiApi(@PathVariable("page") long page, Model model) {
		model.addAttribute("epailist", epaiApiDAO.getEpaiApiList());
		model.addAttribute("warehouselist", branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		model.addAttribute("customerlist", customerDAO.getAllCustomers());

		return "jointmanage/epaiApi_down/epaiapi";
	}
	
	@RequestMapping("/address/{page}")
	public String address(@PathVariable("page") long page, Model model) {
		return "jointmanage/address";
	}
}
