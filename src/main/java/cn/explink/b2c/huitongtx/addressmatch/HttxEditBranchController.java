package cn.explink.b2c.huitongtx.addressmatch;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.huitongtx.Huitongtx;
import cn.explink.b2c.huitongtx.HuitongtxService;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.BranchDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrder;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.CwbOrderAddressCodeEditTypeEnum;
import cn.explink.exception.CwbException;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.Page;

@Controller
@RequestMapping("/httxeditbranch")
public class HttxEditBranchController {
	private Logger logger = LoggerFactory.getLogger(HttxEditBranchController.class);
	@Autowired
	HttxAddressMatchService httxAddressMatchService;
	@Autowired
	HuitongtxService huitongtxService;
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;

	@Autowired
	BranchDAO branchDAO;

	@Autowired
	HttxEditBranchDAO httxEditBranchDAO;

	@RequestMapping("/editBranch")
	public String editBranch(Model model, HttpServletRequest request, @RequestParam(value = "starttime", required = false, defaultValue = "") String starttime,
			@RequestParam(value = "endtime", required = false, defaultValue = "") String endtime,
			@RequestParam(value = "addressCodeEditType", defaultValue = "-1", required = false) int addressCodeEditType, // 是否是已匹配的站点
			@RequestParam(value = "branchid", defaultValue = "0", required = false) long branchid, @RequestParam(value = "onePageNumber", defaultValue = "10", required = false) long onePageNumber, // 每页记录数
			@RequestParam(value = "matchtype", defaultValue = "-1", required = false) int matchtype, // 匹配类型
			@RequestParam(value = "sendflag", defaultValue = "0", required = false) long sendflag) {
		model.addAttribute("branchlist", branchDAO.getBanchByBranchidForStock("" + BranchEnum.ZhanDian.getValue()));

		int page = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
		List<HttxEditBranch> httxBranchList = new ArrayList<HttxEditBranch>();
		Page pageobj = new Page();
		long NotSuccess = 0; // 未匹配
		long SuccessAddress = 0;// 自动匹配

		long SuccessRenGong = 0; // 人工匹配
		long sendSuccess = 0; // 已推送

		if (starttime.equals("")) {
			starttime = DateTimeUtil.getDateBefore(3);
		}
		if (endtime.equals("")) {
			endtime = DateTimeUtil.getNowTime();
		}
		if (addressCodeEditType != -1) {
			matchtype = addressCodeEditType;
		} else {
			matchtype = -1;
		}

		httxBranchList = httxEditBranchDAO.getHttxEditBranchList(starttime, endtime, matchtype, branchid, sendflag, page, onePageNumber);
		pageobj = new Page(httxEditBranchDAO.getHttxEditBranchCount(starttime, endtime, matchtype, branchid, sendflag), page, onePageNumber);

		NotSuccess = httxEditBranchDAO.getHttxEditBranchCount(starttime, endtime, MatchTypeEnum.WeiPiPei.getValue(), branchid, sendflag);
		SuccessAddress = httxEditBranchDAO.getHttxEditBranchCount(starttime, endtime, MatchTypeEnum.DiZhiKu.getValue(), branchid, sendflag);

		SuccessRenGong = httxEditBranchDAO.getHttxEditBranchCount(starttime, endtime, MatchTypeEnum.RenGong.getValue(), branchid, sendflag);

		sendSuccess = httxEditBranchDAO.getHttxEditBranchCount(starttime, endtime, -1, branchid, 3);

		model.addAttribute("httxBranchList", httxBranchList);
		model.addAttribute("page_obj", pageobj);
		model.addAttribute("page", page);
		model.addAttribute("NotSuccess", NotSuccess);
		model.addAttribute("SuccessAddress", SuccessAddress);
		model.addAttribute("SuccessRenGong", SuccessRenGong);
		model.addAttribute("sendSuccess", sendSuccess);
		model.addAttribute("AllAddress", SuccessAddress + NotSuccess + SuccessRenGong);
		model.addAttribute("starttime", starttime);
		model.addAttribute("endtime", endtime);

		return "b2cdj/huitongtx/httxmatchBranch";
	}

	/**
	 * 前端ajax提交信息
	 * 
	 * @param cwb
	 * @param excelbranch
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/editmatchbranch/{id}")
	public @ResponseBody String editexcel(@PathVariable("id") long id, @RequestParam(value = "excelbranch", required = false) String excelbranch, HttpServletRequest request) throws Exception {
		List<Branch> lb = new ArrayList<Branch>();
		List<Branch> branchnamelist = branchDAO.getBranchByBranchnameCheck(excelbranch);
		if (branchnamelist.size() > 0) {
			lb = branchnamelist;
		} else {
			lb = branchDAO.getBranchByBranchcode(excelbranch);
		}
		if (lb.size() == 0) {
			return "{\"errorCode\":1,\"error\":\"没有找到指定站点\"}";
		}

		long branchid = lb.get(0).getBranchid();
		String branchname = lb.get(0).getBranchname();

		try {
			int matchtype_new = 0;

			HttxEditBranch httxbranch = httxEditBranchDAO.getHttxEditBranchById(id);
			int matchtype = httxbranch.getMatchtype();
			if (matchtype == MatchTypeEnum.DiZhiKu.getValue()) {
				matchtype_new = MatchTypeEnum.RenGong.getValue();
			} else if (matchtype == MatchTypeEnum.WeiPiPei.getValue()) {
				matchtype_new = MatchTypeEnum.RenGong.getValue();
			}

			httxEditBranchDAO.updateHttxEditBranchData(DateTimeUtil.getNowTime(), branchname, branchid, matchtype_new, id);

			return "{\"errorCode\":0,\"error\":\"操作成功\",\"excelbranch\":\"" + lb.get(0).getBranchname() + "\"}";
		} catch (CwbException ce) {
			return "{\"errorCode\":" + ce.getError().getValue() + ",\"error\":\"" + ce.getMessage() + "\",\"excelbranch\":\"" + branchname + "\"}";
		}

	}

	/**
	 * 前端ajax提交信息
	 * 
	 * @param cwb
	 * @param excelbranch
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/selectCount")
	public @ResponseBody String selectCount(HttpServletRequest request, @RequestParam(value = "matchtype", defaultValue = "-1", required = false) int matchtype,
			@RequestParam(value = "starttime", required = false, defaultValue = "") String starttime, @RequestParam(value = "endtime", required = false, defaultValue = "") String endtime) {

		long selectCount = httxEditBranchDAO.getHttxNoSendSelect(matchtype, starttime, endtime);

		return "{\"selectCount\":" + selectCount + "}";
	}

	/**
	 * 前段手动提交回传快行线
	 * 
	 * @param cwb
	 * @param excelbranch
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/matchresultsubmit")
	public String matchresultsubmit(@RequestParam(value = "matchtype", defaultValue = "-1", required = false) int matchtype,
			@RequestParam(value = "starttime", required = false, defaultValue = "") String starttime, @RequestParam(value = "endtime", required = false, defaultValue = "") String endtime) {
		if (starttime.equals("")) {
			starttime = DateTimeUtil.getDateBefore(3);
		}
		if (endtime.equals("")) {
			endtime = DateTimeUtil.getNowTime();
		}
		httxAddressMatchService.SendHttxBranchMatchResult(matchtype, starttime, endtime); // 执行查询匹配结果信息并推送

		logger.info("执行了手动推送快行线订单数据");

		return "redirect:/httxeditbranch/editBranch";
	}

}
