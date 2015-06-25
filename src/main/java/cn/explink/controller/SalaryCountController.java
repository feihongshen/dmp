/**
 *
 */
package cn.explink.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.SalaryCountDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.SalaryCount;
import cn.explink.domain.User;
import cn.explink.enumutil.BatchSateEnum;
import cn.explink.enumutil.BranchEnum;
import cn.explink.util.Page;

/**
 * @author Administrator
 *
 */
@Controller
@RequestMapping("salaryCount")
public class SalaryCountController {
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	SalaryCountDAO salaryCountDAO;
	@RequestMapping("/list/{page}")
	public String list(@PathVariable("page") long page,
			@RequestParam(value = "batchid", required = false, defaultValue = "") String batchid,
			@RequestParam(value = "batchstate",required = false,defaultValue = "-1") int batchstate,
			@RequestParam(value = "branchid", required = false, defaultValue = "0") long branchid,
			@RequestParam(value = "starttime",required = false,defaultValue = "") String starttime,
			@RequestParam(value = "endtime",required = false,defaultValue = "") String endtime,
			@RequestParam(value = "realname",required = false,defaultValue = "") String realname,
			@RequestParam(value = "operationTime",required = false,defaultValue = "") String operationTime,
			@RequestParam(value = "orderbyname",required = false,defaultValue = "") String orderbyname,
			@RequestParam(value = "orderbyway",required = false,defaultValue = "") String orderbyway,
			Model model) {
		List<Branch> branchList=this.branchDAO.getAllBranchBySiteType(BranchEnum.ZhanDian.getValue());
		List<User> userList=this.userDAO.getAllUser();
		String userid="";
		if((null!=userList)&&(userList.size()>0)&&(null!=realname)&&(realname.length()>0)){
			for(User user:userList)
			{
				if(user.getRealname().contains(realname)){
				userid+=user.getUserid()+",";
				}
			}
		}
		if(userid.contains(","))
		{
			userid=userid.substring(0,userid.lastIndexOf(","));
		}
		List<SalaryCount> salaryCountList=this.salaryCountDAO.getSalaryCountByPage(page,batchid,batchstate,branchid,starttime,endtime,userid,operationTime,orderbyname,orderbyway);
		int count=this.salaryCountDAO.getSalaryCountByCount(batchid, batchstate, branchid, starttime, endtime, userid, operationTime, orderbyname, orderbyway);

		Page page_obj = new Page(count, page, Page.ONE_PAGE_NUMBER);
		model.addAttribute("page", page);
		model.addAttribute("page_obj", page_obj);
		model.addAttribute("userList", userList);
		model.addAttribute("branchList", branchList);
		model.addAttribute("batchStateEnum", BatchSateEnum.values());
		model.addAttribute("salaryCountList", salaryCountList);

		model.addAttribute("batchid", batchid);
		model.addAttribute("batchstate", batchstate);
		model.addAttribute("branchid", branchid);
		model.addAttribute("starttime", starttime);
		model.addAttribute("endtime", endtime);
		model.addAttribute("realname", realname);
		model.addAttribute("operationTime", operationTime);
		model.addAttribute("orderbyname", orderbyname);
		model.addAttribute("orderbyway", orderbyway);

		return "salary/salaryCount/list";
	}
	@RequestMapping("/credata")
	public String credata(SalaryCount salaryCount,Model model) {
		salaryCount.setBatchid(System.currentTimeMillis()+"");
		this.salaryCountDAO.cresalaryCount(salaryCount);
		SalaryCount salary=this.salaryCountDAO.getSalaryCountBybatchid(salaryCount.getBatchid());
		List<Branch> branchList=this.branchDAO.getAllBranchBySiteType(BranchEnum.ZhanDian.getValue());
		List<User> userList=this.userDAO.getAllUser();
		model.addAttribute("salary", salary);
		model.addAttribute("branchList", branchList);
		model.addAttribute("batchStateEnum", BatchSateEnum.values());
		model.addAttribute("edit", 1);
		model.addAttribute("userList", userList);
		return "salary/salaryCount/list";
	}
}
