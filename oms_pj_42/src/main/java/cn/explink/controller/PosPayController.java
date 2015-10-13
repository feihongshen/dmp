package cn.explink.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.PosPayDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.service.PosPayService;
import cn.explink.util.Page;

@RequestMapping("/pospay")
@Controller
public class PosPayController {

	@Autowired
	PosPayService posPayService;
	@Autowired
	PosPayDAO posPayDAO;
	@Autowired
	GetDmpDAO getDmpDAO;
	@Autowired
	UserDAO userDAO;

	@RequestMapping("/list/{page}")
	public String list(
			@PathVariable("page") long page,
			Model model,
			HttpServletRequest request,
			@RequestParam(value = "cwb", required = false, defaultValue = "") String cwb,
			@RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid, // 小件员
			@RequestParam(value = "deliverid", required = false, defaultValue = "0") long deliverid, // 小件员
			@RequestParam(value = "branchid", required = false, defaultValue = "0") long branchid, // 站点条件
			@RequestParam(value = "payname", required = false, defaultValue = "") String payname, // 付款人
			@RequestParam(value = "pos_code", required = false, defaultValue = "") String pos_code, // 支付方
			@RequestParam(value = "pos_signtypeid", required = false, defaultValue = "-1") int pos_signtypeid, // 签收类型
			@RequestParam(value = "starttime", required = false, defaultValue = "") String starttime, @RequestParam(value = "endtime", required = false, defaultValue = "") String endtime,
			@RequestParam(value = "starttime_sp", required = false, defaultValue = "") String starttime_sp, @RequestParam(value = "endtime_sp", required = false, defaultValue = "") String endtime_sp,
			@RequestParam(value = "pos_backoutflag", required = false, defaultValue = "-1") int pos_backoutflag // 是否撤销
	) {
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		long branchidSession = getDmpDAO.getNowBrancheId(dmpid);
		User user = getDmpDAO.getLogUser(dmpid);
		Branch nowBranch = getDmpDAO.getNowBranch(branchidSession); // 获取当前站点的类型
		int branchtype = nowBranch.getSitetype();
		if (nowBranch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
			model.addAttribute("nowbranch", nowBranch);
		}
		List<Branch> branchlist = getDmpDAO.getBranchListByCawWuAndUser(branchidSession, user.getUserid());
		if (nowBranch.getSitetype() == BranchEnum.KuFang.getValue() || nowBranch.getSitetype() == BranchEnum.TuiHuo.getValue() || nowBranch.getSitetype() == BranchEnum.ZhongZhuan.getValue()
				|| nowBranch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
			if (branchlist.size() == 0) {
				branchlist.add(nowBranch);
			} else {
				if (!posPayService.checkBranchRepeat(branchlist, nowBranch)) {
					branchlist.add(nowBranch);
				}
			}
		}

		model.addAttribute("pospaylist", posPayService.PosPayRecord_selectByList(pos_code, customerid, deliverid, branchid, payname, cwb, starttime, endtime, pos_signtypeid, branchidSession,
				branchtype, starttime_sp, endtime_sp, pos_backoutflag, page));
		model.addAttribute(
				"page_obj",
				new Page(posPayDAO.PosPayRecord_selectByListCount(pos_code, customerid, deliverid, branchid, payname, cwb, starttime, endtime, pos_signtypeid, branchidSession, branchtype,
						starttime_sp, endtime_sp, pos_backoutflag), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("deliverlist", getDmpDAO.getAllDeliver(branchid > 0 ? branchid : branchidSession));
		model.addAttribute("branchlist", branchlist);
		model.addAttribute("customerlist", getDmpDAO.getAllCustomers());
		model.addAttribute("page", page);

		// 保存查询条件到request
		this.saveConditionsToRequest(request, cwb, customerid, deliverid, branchid, payname, pos_code, pos_signtypeid, starttime, endtime, starttime_sp, endtime_sp, pos_backoutflag);
		return "funds/pospay/pospaylist";
	}

	@RequestMapping("/save_excel")
	public void saveAsExcel(Model model, HttpServletRequest request, HttpServletResponse response) {
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		long branchidSession = getDmpDAO.getNowBrancheId(dmpid);
		int branchtype = getDmpDAO.getNowBranch(branchidSession).getSitetype(); // 获取当前站点的类型
		String cwb = request.getSession().getAttribute("cwb").toString();
		long deliverid = Long.parseLong(request.getSession().getAttribute("deliverid").toString());
		long branchid = Long.parseLong(request.getSession().getAttribute("branchid").toString());
		String payname = request.getSession().getAttribute("paynameid").toString();
		String pos_code = request.getSession().getAttribute("pos_code").toString();
		int pos_signtypeid = Integer.parseInt(request.getSession().getAttribute("pos_signtypeid").toString());
		String starttime = request.getSession().getAttribute("starttime").toString();
		String endtime = request.getSession().getAttribute("endtime").toString();
		long customerid = Long.parseLong(request.getSession().getAttribute("customerid").toString());

		String starttime_sp = request.getSession().getAttribute("starttime_sp").toString();
		String endtime_sp = request.getSession().getAttribute("endtime_sp").toString();
		int pos_backoutflag = Integer.parseInt(request.getSession().getAttribute("pos_backoutflag").toString());

		posPayService.PosPayRecord_selectSaveAsExcel(pos_code, customerid, deliverid, branchid, payname, cwb, starttime, endtime, pos_signtypeid, branchidSession, branchtype, starttime_sp,
				endtime_sp, pos_backoutflag, response);

	}

	@RequestMapping("/updatebranch")
	public @ResponseBody String updateCustomerwarehouse(Model model, @RequestParam("branchid") long branchid) {
		if (branchid > 0) {
			List<User> list = getDmpDAO.getAllDeliver(branchid);
			return JSONArray.fromObject(list).toString();
		} else {
			return "[]";
		}

	}

	private void saveConditionsToRequest(HttpServletRequest request, String cwb, long customerid, long deliverid, long branchid, String payname, String pos_code, int pos_signtypeid, String starttime,
			String endtime, String starttime_sp, String endtime_sp, int pos_backoutflag) {
		request.setAttribute("cwb", cwb);
		request.setAttribute("deliverid", deliverid);
		request.setAttribute("branchid", branchid);
		request.setAttribute("paynameid", payname);
		request.setAttribute("pos_code", pos_code);
		request.setAttribute("pos_signtypeid", pos_signtypeid);
		request.setAttribute("starttime", starttime);
		request.setAttribute("endtime", endtime);
		request.setAttribute("customerid", customerid);
		request.setAttribute("pos_backoutflag", pos_backoutflag);

		request.setAttribute("starttime_sp", starttime_sp);
		request.setAttribute("endtime_sp", endtime_sp);

		request.getSession().setAttribute("cwb", cwb);
		request.getSession().setAttribute("deliverid", deliverid);
		request.getSession().setAttribute("branchid", branchid);
		request.getSession().setAttribute("paynameid", payname);
		request.getSession().setAttribute("pos_code", pos_code);
		request.getSession().setAttribute("pos_signtypeid", pos_signtypeid);
		request.getSession().setAttribute("starttime", starttime);
		request.getSession().setAttribute("endtime", endtime);
		request.getSession().setAttribute("customerid", customerid);
		request.getSession().setAttribute("starttime_sp", starttime_sp);
		request.getSession().setAttribute("endtime_sp", endtime_sp);
		request.getSession().setAttribute("pos_backoutflag", pos_backoutflag);

	}

}
