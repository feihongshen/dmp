/**
 *
 */
package cn.explink.controller;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.SalaryCountDAO;
import cn.explink.dao.SalaryGatherDao;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.SalaryCount;
import cn.explink.domain.User;
import cn.explink.enumutil.BatchSateEnum;
import cn.explink.enumutil.BranchTypeEnum;
import cn.explink.service.Excel2003Extractor;
import cn.explink.service.Excel2007Extractor;
import cn.explink.service.ExcelExtractor;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.util.JsonUtil;
import cn.explink.util.Page;

/**
 * @author Administrator
 *
 */
@Controller
@RequestMapping("salaryCount")
public class SalaryCountController {
	@Autowired
	Excel2007Extractor excel2007Extractor;
	@Autowired
	Excel2003Extractor excel2003Extractor;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	SalaryCountDAO salaryCountDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	SalaryGatherDao salaryGatherDao;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	
	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}
	
	@RequestMapping("/list/{page}")
	public String list(@PathVariable("page") long page,
			@RequestParam(value = "batchid", required = false, defaultValue = "") String batchid,
			@RequestParam(value = "batchstate",required = false,defaultValue = "-1") int batchstate,
			@RequestParam(value = "branchname", required = false, defaultValue = "") String branchname,
			@RequestParam(value = "starttime",required = false,defaultValue = "") String starttime,
			@RequestParam(value = "endtime",required = false,defaultValue = "") String endtime,
			@RequestParam(value = "realname",required = false,defaultValue = "") String realname,
			@RequestParam(value = "operationTime",required = false,defaultValue = "") String operationTime,
			@RequestParam(value = "orderbyname",required = false,defaultValue = "") String orderbyname,
			@RequestParam(value = "orderbyway",required = false,defaultValue = "") String orderbyway,
			Model model) {
		
		List<Branch> branchList=this.branchDAO.getBranchssBycontractflag(BranchTypeEnum.ZhiYing.getValue()+"");
		List<User> userList=this.userDAO.getAllUser();
		String userids="";
		String branchids="";
		if((null!=branchList)&&(branchList.size()>0)&&(null!=branchname)&&(branchname.length()>0)){
			for(Branch b:branchList){
				if(b.getBranchname().contains(branchname)){
					branchids+=b.getBranchid()+",";
				}
				
			}
		}
		if(branchids.contains(",")){
			branchids=branchids.substring(0,branchids.lastIndexOf(","));
		}
		if((branchname.length()>0)&&(branchids.length()==0)){
			branchids="-1";
		}
		if((null!=userList)&&(userList.size()>0)&&(null!=realname)&&(realname.length()>0)){
			for(User user:userList){
				if(user.getRealname().contains(realname)){
					userids+=user.getUserid()+",";
				}
			}
		}
		if(userids.contains(",")){
			userids=userids.substring(0,userids.lastIndexOf(","));
		}
		if((realname.length()>0)&&(userids.length()==0)){
			userids="-1";
		}

		List<SalaryCount> salaryCountList=this.salaryCountDAO.getSalaryCountByPage(page,batchid,batchstate,branchids,starttime,endtime,userids,operationTime,orderbyname,orderbyway);
		int count=this.salaryCountDAO.getSalaryCountByCount(batchid, batchstate, branchids, starttime, endtime, userids, operationTime, orderbyname, orderbyway);

		Page page_obj = new Page(count, page, Page.ONE_PAGE_NUMBER);
		model.addAttribute("page", page);
		model.addAttribute("page_obj", page_obj);
		model.addAttribute("userList", userList);
		model.addAttribute("branchList", branchList);
		model.addAttribute("batchStateEnum", BatchSateEnum.values());
		model.addAttribute("salaryCountList", salaryCountList);

		model.addAttribute("batchid", batchid);
		model.addAttribute("batchstate", batchstate);
		model.addAttribute("branchname", branchname);
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
		if(salaryCount.getIsnow()==1){
			this.salaryCountDAO.updatesalaryCount(salaryCount);
		}else{
			salaryCount.setBatchid(System.currentTimeMillis()+"");
			this.salaryCountDAO.cresalaryCount(salaryCount);
			this.salaryGatherDao.cresalaryGather(salaryCount);
			SalaryCount salary=this.salaryCountDAO.getSalaryCountBybatchid(salaryCount.getBatchid());
			List<Branch> branchList=this.branchDAO.getBranchssBycontractflag(BranchTypeEnum.ZhiYing.getValue()+"");
			List<User> userList=this.userDAO.getAllUser();
			model.addAttribute("salary", salary);
			model.addAttribute("branchList", branchList);
			model.addAttribute("batchStateEnum", BatchSateEnum.values());
			model.addAttribute("edit", 1);
			model.addAttribute("userList", userList);
		}
		return "salary/salaryCount/list";
	}
	
	
	@RequestMapping("/updatecredata")
	public void updatecredata(SalaryCount salaryCount) {
		
	}
	
	
	@RequestMapping("/seeOralter")
	@ResponseBody
	public String seeOralter(Model model,HttpServletRequest request){
		String batchid = request.getParameter("batchid");
		SalaryCount salary=this.salaryCountDAO.getSalaryCountBybatchid(batchid);
		List<Branch> branchList=this.branchDAO.getBranchssBycontractflag(BranchTypeEnum.ZhiYing.getValue()+"");
		for(Branch b:branchList){
			if(b.getBranchid()==salary.getBranchid()){
				salary.setBranchname(b.getBranchname());
			}
		}
		try {
			return JsonUtil.translateToJson(salary);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}

	@RequestMapping("/delete")
	public  @ResponseBody
	Map<String, Long> delete(@RequestParam(value = "ids",required = false,defaultValue = "") String ids) throws Exception {
		long counts=0;
		if((ids!=null)&&(ids.length()>0)){
			counts=this.salaryCountDAO.deleteSalarCountyByids(ids);
		}
		Map<String, Long> map=new HashMap<String, Long>();
		map.put("counts", counts);
		return map;
	}
	
	@RequestMapping("/save")
	public @ResponseBody
	String save(@RequestParam(value = "ids",required = false,defaultValue = "") String ids){
		
		return "";
	}
	
	@RequestMapping("/importData")
	public String importData(Model model, final HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "Filedata", required = false) final MultipartFile file) throws Exception {
		final ExcelExtractor excelExtractor = this.getExcelExtractor(file);
		final InputStream inputStream = file.getInputStream();
		final User user=this.getSessionUser();
		final long importflag=System.currentTimeMillis();
		if (excelExtractor != null) {

			this.processFile(excelExtractor, inputStream,importflag,user);

		} else {
			return "redirect:list/1";
		}

		return "redirect:list/1?importflag=" + importflag;
	}
	private ExcelExtractor getExcelExtractor(MultipartFile file) {
		String originalFilename = file.getOriginalFilename();
		if (originalFilename.endsWith(".xlsx")) {
			return this.excel2007Extractor;
		} else if (originalFilename.endsWith(".xls")) {
			return this.excel2003Extractor;
		}
		return null;
	}
	
	//人事数据导入
	protected void processFile(ExcelExtractor excelExtractor, InputStream inputStream, long importflag,User user) {
		excelExtractor.extractSalaryGather(inputStream,importflag,user);

	}
	
	
}
