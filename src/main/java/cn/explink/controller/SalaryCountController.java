/**
 *
 */
package cn.explink.controller;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import cn.explink.domain.SalaryGather;
import cn.explink.domain.User;
import cn.explink.enumutil.BatchSateEnum;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.BranchTypeEnum;
import cn.explink.service.DataStatisticsService;
import cn.explink.service.Excel2003Extractor;
import cn.explink.service.Excel2007Extractor;
import cn.explink.service.ExcelExtractor;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.SalaryGatherService;
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
	@Autowired
	DataStatisticsService dataStatisticsService;
	@Autowired
	SalaryGatherService salaryGatherService;
	
	/**
	 * 控制查询条件时使用(线程变量)
	 */
	public static final ThreadLocal<Map<List<SalaryGather>, Map<Integer, SalaryCount>>> QUERY_CONDITION = new ThreadLocal<Map<List<SalaryGather>, Map<Integer, SalaryCount>>>();
	
	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}
	
	@SuppressWarnings("unchecked")
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
			/*@RequestParam(value = "sgList",required = false,defaultValue = "") List<SalaryGather> sgList,
			@RequestParam(value = "edit",required = false,defaultValue = "0") int edit,*/
			Model model) {
		
		List<Branch> branchList=this.branchDAO.getBranchssBycontractflag(BranchTypeEnum.ZhiYing.getValue()+"",BranchEnum.ZhanDian.getValue());
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
		
		List<SalaryGather> sgList = null;
		Map<Integer, SalaryCount> isMap = null;
		Integer itg = 0;
		SalaryCount sc = null;
		Map<List<SalaryGather>, Map<Integer, SalaryCount>> sgMap = QUERY_CONDITION.get();
		QUERY_CONDITION.set(null);
		if(sgMap!=null){
			Set<List<SalaryGather>> keyset = sgMap.keySet();
			Iterator<List<SalaryGather>> it = keyset.iterator();
			while(it.hasNext()){
				sgList = (List<SalaryGather>)it.next();
				isMap = sgMap.get(sgList);
				Set<Integer> ite = isMap.keySet();
				Iterator<Integer> iterator = ite.iterator();
				while(iterator.hasNext()){
					itg = iterator.next();
					sc = isMap.get(itg);
				}
			}
		}
		//List<SalaryCount> scList = this.salaryCountDAO.getAllSalaryCounts();//用于查询时的下拉框
		//model.addAttribute("scList",scList);
		SalaryCount salary = new SalaryCount();
		salary.setIsnow(-1);
		model.addAttribute("salary",salary);
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
		
		model.addAttribute("sgList",sgList);//
		model.addAttribute("edit",itg);//标识
		model.addAttribute("salarycount",sc);//批次信息
		
		return "salary/salaryCount/list";
	}
	@RequestMapping("/credata")
	public String credata(SalaryCount salaryCount,Model model) {
		if(salaryCount.getIsnow()==1){
			this.salaryCountDAO.updatesalaryCount(salaryCount);
		}else{
			salaryCount.setBatchid(System.currentTimeMillis()+"");
			this.salaryCountDAO.cresalaryCount(salaryCount);
			/*this.salaryGatherDao.cresalaryGather(salaryCount);*/
			SalaryCount salary=this.salaryCountDAO.getSalaryCountBybatchid(salaryCount.getBatchid());
			List<Branch> branchList=this.branchDAO.getBranchssBycontractflag(BranchTypeEnum.ZhiYing.getValue()+"",BranchEnum.ZhanDian.getValue());
			List<User> userList=this.userDAO.getAllUser();
			model.addAttribute("salary", salary);
			model.addAttribute("branchList", branchList);
			model.addAttribute("batchStateEnum", BatchSateEnum.values());
			model.addAttribute("edit", 1);
			model.addAttribute("userList", userList);
		}
		return "salary/salaryCount/list";
	}
	
	@RequestMapping("/seeOralter")
	@ResponseBody
	public String seeOralter(Model model,HttpServletRequest request){
		String batchid = request.getParameter("batchid");
		SalaryCount salary=this.salaryCountDAO.getSalaryCountBybatchid(batchid);
		List<Branch> branchList=this.branchDAO.getBranchssBycontractflag(BranchTypeEnum.ZhiYing.getValue()+"",BranchEnum.ZhanDian.getValue());
		List<SalaryGather> salaryGathers=salaryGatherDao.getSalaryGathers(batchid);
		for(Branch b:branchList){
			if(b.getBranchid()==salary.getBranchid()){
				salary.setBranchname(b.getBranchname());
			}
		}
		try {
			salary.setSalaryGathers(salaryGathers);
			return JsonUtil.translateToJson(salary);
		} catch (Exception e) {
			e.printStackTrace();	
			return null;
		} 
	}

	@RequestMapping("/delete")
	public  @ResponseBody
	Map<String, Long> delete(@RequestParam(value = "ids",required = false,defaultValue = "") String ids) throws Exception {
		Map<String, Long> map=new HashMap<String, Long>();
		long counts=0;
		if((ids!=null)&&(ids.length()>0)){
			
			String str = "";
			String[] strArray = ids.split(",");
			for(String st : strArray){
				str += "'"+st+"',";
				long lon = this.salaryCountDAO.getSalarCountyByid(st);
				if(lon==0){
					map.put("counts", lon);
					return map;
				}
			}
			String strs = "";
			if(str.length()>0){
				strs = str.substring(0,str.length()-1);
			}
			counts=this.salaryCountDAO.deleteSalarCountyByids(strs);
		}
		map.put("counts", counts);
		return map;
	}
	
	
	//核销
	@RequestMapping("/hexiao")
	public @ResponseBody
	String heXiao(@RequestParam(value = "userids",required = false,defaultValue = "") String ids,
			@RequestParam(value = "batchid",required = false,defaultValue = "") String batchid){
		
		try{
			User use = this.getSessionUser();
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String dateStr = sdf.format(date);
			//核销主方法(返回核销成功的工资条数)
			long counts = this.salaryGatherService.getHexiaoCounts(ids,use,dateStr,batchid);
			return "{\"errorCode\":0,\"error\":"+counts+"}";
		}catch(Exception e){
			e.printStackTrace();
			return "{\"errorCode\":1,\"error\":\"核销系统异常！\"}";
		}
	}
	
	@RequestMapping("/importData")
	public String importData(Model model, final HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "Filedata", required = false) final MultipartFile file,
			 @RequestParam(value = "salarydata", required = false,defaultValue="") String data,
			//@RequestParam(value = "salarycount", required = false) Object salarycount
			 @RequestParam(value = "batchid", required = false,defaultValue="") String batchid,
			 @RequestParam(value = "batchstate", required = false,defaultValue="0") int batchstate,
			 @RequestParam(value = "branchnam", required = false,defaultValue="0") long branchid,
			 @RequestParam(value = "starttime", required = false,defaultValue="") String starttime,
			 @RequestParam(value = "endtime", required = false,defaultValue="") String endtime,
			 @RequestParam(value = "remark", required = false,defaultValue="") String remark
			) throws Exception {
		SalaryCount sc = new SalaryCount();
		String[] strArray = data.split(","); 
		//if(!"".equals(data)&&strArray.length>0){
		if(!"".equals(data)){
			sc.setBatchid(strArray[0]);
			sc.setBatchstate(Integer.parseInt(strArray[1]));
			sc.setBranchid(Long.parseLong(strArray[2]));
			sc.setStarttime(strArray[3]);
			sc.setEndtime(strArray[4]);
			sc.setRemark(strArray[5]);
			sc.setBranchname(strArray[6]);
		}else{
			sc.setBatchid(batchid);
			sc.setBatchstate(batchstate);
			sc.setBranchid(branchid);
			sc.setStarttime(starttime);
			sc.setEndtime(endtime);
			sc.setRemark(remark);
		}
		/*else{
			long branchid = Long.parseLong(request.getParameter("branchid"));
			//sc = (SalaryCount)salarycount;
		}*/
		
		final ExcelExtractor excelExtractor = this.getExcelExtractor(file);
		final InputStream inputStream = file.getInputStream();
		final User user=this.getSessionUser();
		final long importflag=System.currentTimeMillis();
		List<SalaryGather> sgList = new ArrayList<SalaryGather>();
		List<Branch> branchList=this.branchDAO.getBranchssBycontractflag(BranchTypeEnum.ZhiYing.getValue()+"",BranchEnum.ZhanDian.getValue());
		if (excelExtractor != null) {
			//数据导入并计算工资
			this.processFile(excelExtractor, inputStream,importflag,user,sc);
			//导入之后进行查询
			sgList = this.salaryGatherDao.getSalaryGathers(sc.getBatchid());
			for(SalaryGather sg : sgList){
				sg.setBranchname(this.dataStatisticsService.getQueryBranchName(branchList, sg.getBranchid()));
			}
			
		}
		Map<List<SalaryGather>, Map<Integer, SalaryCount>> sgscinteList = new HashMap<List<SalaryGather>, Map<Integer,SalaryCount>>();
		Map<Integer,SalaryCount> scMap = new HashMap<Integer, SalaryCount>();
		scMap.put(new Integer(1),sc);
		sgscinteList.put(sgList, scMap);
		QUERY_CONDITION.set(sgscinteList);

		return "redirect:list/1";
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
	protected void processFile(ExcelExtractor excelExtractor, InputStream inputStream, long importflag,User user,SalaryCount sc ) {
		excelExtractor.extractSalaryGather(inputStream,importflag,user,sc);

	}
	@RequestMapping("/removeDeliveryUserSalaryData")
	public @ResponseBody String removeDeliveryUserSalaryData(@RequestParam(value="userids",defaultValue="",required=false)String userids,
			@RequestParam(value="batchid",defaultValue="0",required=false)long batchid
			){
		try {
			int count=salaryCountDAO.getSalaryCountWithHexiao(BatchSateEnum.Yihexiao.getValue(), batchid);
			if(count>0){
				return "{\"errorCode\":1,\"error\":\"已核销的批次号不能移除数据\"}";
			}
			//根据小件员的id来删除相关的信息
			int influencecount=salaryGatherDao.removeDeliveryUserSalaryData(userids,batchid);
			if (influencecount>0) {
				return "{\"errorCode\":0,\"error\":\"移除成功\"}";
			}else {
				return "{\"errorCode\":0,\"error\":\"移除失败\"}";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{\"errorCode\":1,\"error\":\"系统内部异常，请排查！！\"}";
		}
		
	}
	
}
