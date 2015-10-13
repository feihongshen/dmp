package cn.explink.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.User;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.BranchEnum;
import cn.explink.util.Page;

@RequestMapping("/operation")
@Controller
public class OperationSelectController {

	@Autowired
	GetDmpDAO getDmpDAO;
	@Autowired
	OrderFlowDAO orderFlowDAO;

	@RequestMapping("/list/{page}")
	public String list(Model model, @PathVariable("page") long page, @RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid,
			@RequestParam(value = "startbranchid", required = false, defaultValue = "-1") long startbranchid, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate,
			@RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow,
			HttpServletRequest request) {
		List<OrderFlow> orderFlowList = new ArrayList<OrderFlow>();
		Page pageparm = new Page();
		if (isshow == 1) {
			orderFlowList = orderFlowDAO.getOrderFlowByPage(page, customerid, startbranchid, begindate, enddate);
			pageparm = new Page(orderFlowDAO.getOrderFlowCount(customerid, startbranchid, begindate, enddate), page, Page.ONE_PAGE_NUMBER);
		}
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		long branchidSession = getDmpDAO.getNowBrancheId(dmpid);
		Branch branch = getDmpDAO.getNowBranch(branchidSession); // 获取当前站点的类型
		User user = getDmpDAO.getLogUser(dmpid);
		String type = BranchEnum.KuFang.getValue() + "," + BranchEnum.TuiHuo.getValue() + "," + BranchEnum.ZhongZhuan.getValue() + "," + BranchEnum.ZhanDian.getValue();
		List<Branch> branchlist = getDmpDAO.getBranchListByTypeAndUser(type, user.getUserid());
		if (branch.getSitetype() == BranchEnum.KuFang.getValue() || branch.getSitetype() == BranchEnum.TuiHuo.getValue() || branch.getSitetype() == BranchEnum.ZhongZhuan.getValue()
				|| branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
			if (branchlist.size() == 0) {
				branchlist.add(branch);
			} else {
				if (!this.checkBranchRepeat(branchlist, branch)) {
					branchlist.add(branch);
				}
			}
		}
		model.addAttribute("branchList", branchlist);
		model.addAttribute("orderFlowList", orderFlowList);
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);
		return "operationselect/oplist";
	}

	public boolean checkBranchRepeat(List<Branch> branchlist, Branch branch) {
		for (int i = 0; i < branchlist.size(); i++) {
			if (branch.getBranchname().equals(branchlist.get(i).getBranchname())) {
				return true;
			}
		}
		return false;
	}

}
