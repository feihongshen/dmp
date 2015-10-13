package cn.explink.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/monitor_brank")
public class MonitorController_brank {

	/*
	 * @Autowired JdbcTemplate jdbcTemplate;
	 * 
	 * @Autowired MonitorService monitorService;
	 * 
	 * @Autowired DeliveryService deliveryService;
	 * 
	 * @Autowired CustomerDAO customerDao;
	 * 
	 * @Autowired BranchDAO branchDao;
	 * 
	 * @Autowired SecurityContextHolderStrategy securityContextHolderStrategy;
	 * //第一页
	 * 
	 * @RequestMapping("/date/{page}") public String list(Model model,
	 * 
	 * @PathVariable("page")long page,
	 * 
	 * @RequestParam(value = "crateStartdate",required =
	 * false,defaultValue="")String crateStartdate,
	 * 
	 * @RequestParam(value = "crateEnddate",required =
	 * false,defaultValue="")String crateEnddate,
	 * 
	 * @RequestParam(value = "shipStartTime",required =
	 * false,defaultValue="")String shipStartTime,
	 * 
	 * @RequestParam(value = "shipEndTime",required =
	 * false,defaultValue="")String shipEndTime,
	 * 
	 * @RequestParam(value = "emailStartTime",required =
	 * false,defaultValue="")String emailStartTime,
	 * 
	 * @RequestParam(value = "eamilEndTime",required =
	 * false,defaultValue="")String eamilEndTime,
	 * 
	 * @RequestParam(value = "controlStrEmail",required =
	 * false,defaultValue="")String controlStrEmail,
	 * 
	 * @RequestParam(value = "branchid",required = false,defaultValue="-1")long
	 * branchid,
	 * 
	 * @RequestParam(value = "controlStrCustomer",required =
	 * false,defaultValue="")String controlStrCustomer, HttpServletResponse
	 * response,HttpServletRequest request){
	 * 
	 * List<EmaildateTDO> emailList=
	 * deliveryService.getEmaildateAndBrandidList(crateStartdate,
	 * crateEnddate,-1); List<EmaildateTDO> emailListByShiptime=
	 * deliveryService.getEmaildateAndBrandidList(shipStartTime,
	 * shipEndTime,-1); List<EmaildateTDO> emailListByEmailTime=
	 * deliveryService.getEmaildateAndBrandidList(emailStartTime,
	 * eamilEndTime,-1); model.addAttribute("emailList", emailList); //查询所有的站点
	 * List<Branch> branchnameList = branchDao.getAllBranches();
	 * model.addAttribute("branchnameList", branchnameList); //查询当前站点
	 * ExplinkUserDetail userDetail= (ExplinkUserDetail)
	 * securityContextHolderStrategy
	 * .getContext().getAuthentication().getPrincipal(); long branchidSession =
	 * userDetail == null?-1:userDetail.getUser().getBranchid(); long
	 * branchidPram = branchid==-1? branchidSession:branchid;
	 * model.addAttribute("branchidSession", branchidPram); String emaildate =
	 * "".equals(controlStrEmail)?"":controlStrEmail.substring(0,
	 * controlStrEmail.length()-1); String customerid =
	 * "".equals(controlStrCustomer)?"":controlStrCustomer.substring(0,
	 * controlStrCustomer.length()-1); //将条件保存session中 setSetions(request,
	 * crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime,
	 * eamilEndTime, emaildate, customerid,branchidPram); if(page == 2 || page
	 * == 3 || page == 4 ){ setCustomerlist(model, customerid,branchid); }
	 * if(page == 1){//数据监控 setDateModels(model, crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate,
	 * customerid,branchidPram); return "/monitor/date/dateMonitor"; } else
	 * if(page == 2){//数据监控（数据组） setDateGroupModels(model, crateStartdate,
	 * crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime,
	 * emaildate, customerid,branchidPram); if(emaildate.equals("")){ for
	 * (EmaildateTDO emaildateTDO : emailList) { emaildate +=
	 * emaildateTDO.getEmaildate()+","; }
	 * request.getSession().setAttribute("emaildateYoudanwuhuo",
	 * emaildate.length()>0?emaildate.substring(0, emaildate.length()-1):""); }
	 * setYoudanwuhuo(model, emaildate, -1, customerid, crateStartdate,
	 * crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime);
	 * return "/monitor/dateGroup/dateGroupMonitor"; }else if(page ==
	 * 3){//库房信息监控 setHouseModels(model, crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate,
	 * customerid,branchidPram); if(emaildate.equals("")){ for (EmaildateTDO
	 * emaildateTDO : emailListByEmailTime) { emaildate +=
	 * emaildateTDO.getEmaildate()+","; }
	 * request.getSession().setAttribute("emaildateYoudanwuhuo",
	 * emaildate.length()>0?emaildate.substring(0, emaildate.length()-1):""); }
	 * setYoudanwuhuo(model, emaildate, -1, customerid, crateStartdate,
	 * crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime);
	 * setWeiruku(model, emaildate, -1, "2,4,7", customerid, crateStartdate,
	 * crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime);
	 * return "/monitor/house/houseMonitor"; } else if(page == 4){//站点信息监控
	 * setSiteModels(model, crateStartdate, crateEnddate, shipStartTime,
	 * shipEndTime, emailStartTime, eamilEndTime, emaildate,
	 * customerid,branchidPram); if(emaildate.equals("")){ for (EmaildateTDO
	 * emaildateTDO : emailListByEmailTime) { emaildate +=
	 * emaildateTDO.getEmaildate()+","; }
	 * request.getSession().setAttribute("emaildateYoudanwuhuo",
	 * emaildate.length()>0?emaildate.substring(0, emaildate.length()-1):""); }
	 * setYoudanwuhuo(model, emaildate, -1, customerid, crateStartdate,
	 * crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime);
	 * return "/monitor/site/siteMonitor"; } else if(page == 7){//异常信息监控 String
	 * emaildateYoudanwuhuo = emaildate; if(emaildate.equals("")){ for
	 * (EmaildateTDO emaildateTDO : emailListByShiptime) { emaildateYoudanwuhuo
	 * += emaildateTDO.getEmaildate()+","; }
	 * request.getSession().setAttribute("emaildateYoudanwuhuo",
	 * emaildateYoudanwuhuo.length()>0?emaildateYoudanwuhuo.substring(0,
	 * emaildateYoudanwuhuo.length()-1):""); }
	 * request.getSession().setAttribute("branchidPram", branchid);
	 * setExpModels(model, crateStartdate, crateEnddate, shipStartTime,
	 * shipEndTime, emailStartTime, eamilEndTime, emaildate,
	 * customerid,branchid,emaildateYoudanwuhuo); return
	 * "/monitor/exption/exptionMonitor"; } else if(page == 8){//财务信息监控 String
	 * emaildateYoudanwuhuo = emaildate; if(emaildate.equals("")){ for
	 * (EmaildateTDO emaildateTDO : emailListByShiptime) { emaildateYoudanwuhuo
	 * += emaildateTDO.getEmaildate()+","; }
	 * request.getSession().setAttribute("emaildateYoudanwuhuo",
	 * emaildateYoudanwuhuo.length()>0?emaildateYoudanwuhuo.substring(0,
	 * emaildateYoudanwuhuo.length()-1):""); }
	 * request.getSession().setAttribute("branchidPram", branchid);
	 * setMoneyModels(model, crateStartdate, crateEnddate, shipStartTime,
	 * shipEndTime, emailStartTime, eamilEndTime, emaildate,
	 * customerid,branchid,emaildateYoudanwuhuo); return
	 * "/monitor/money/moneyMonitor"; } else{ setDateModels(model,
	 * crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime,
	 * eamilEndTime, emaildate, customerid,branchidPram); return
	 * "/monitor/date/dateMonitor"; } } //第一页导出
	 * 
	 * @RequestMapping("/dateExport/{page}") public String dateExp(Model model,
	 * 
	 * @PathVariable("page")long page, HttpServletResponse
	 * response,HttpServletRequest request){ String crateStartdate =
	 * request.getSession().getAttribute("crateStartdate").toString(); String
	 * crateEnddate =
	 * request.getSession().getAttribute("crateEnddate").toString(); String
	 * shipStartTime =
	 * request.getSession().getAttribute("shipStartTime").toString(); String
	 * shipEndTime =
	 * request.getSession().getAttribute("shipEndTime").toString(); String
	 * emailStartTime =
	 * request.getSession().getAttribute("emailStartTime").toString(); String
	 * eamilEndTime =
	 * request.getSession().getAttribute("eamilEndTime").toString(); String
	 * emaildate = request.getSession().getAttribute("emaildate").toString();
	 * String emaildateYoudanwuhuo =
	 * request.getSession().getAttribute("emaildateYoudanwuhuo").toString();
	 * String customerid =
	 * request.getSession().getAttribute("customerid").toString(); long branchid
	 * = new Long(request.getSession().getAttribute("branchid").toString());
	 * if(page == 1){ setDateModels(model, crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate,
	 * customerid,branchid); return "/monitor/date/dateMonitorExpo"; } else
	 * if(page==2){ setDateGroupModels(model, crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate,
	 * customerid,branchid); setYoudanwuhuo(model, emaildateYoudanwuhuo, -1,
	 * customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime,
	 * emailStartTime, eamilEndTime); return
	 * "/monitor/dateGroup/dateGroupMonitorExpo"; } else if(page==3){
	 * setHouseModels(model, crateStartdate, crateEnddate, shipStartTime,
	 * shipEndTime, emailStartTime, eamilEndTime, emaildate,
	 * customerid,branchid); setYoudanwuhuo(model, emaildateYoudanwuhuo, -1,
	 * customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime,
	 * emailStartTime, eamilEndTime); setWeiruku(model, emaildateYoudanwuhuo,
	 * -1, "2,4,7", customerid, crateStartdate, crateEnddate, shipStartTime,
	 * shipEndTime, emailStartTime, eamilEndTime); return
	 * "/monitor/house/houseExpo"; } else if(page==4){ setSiteModels(model,
	 * crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime,
	 * eamilEndTime, emaildate, customerid,branchid); setYoudanwuhuo(model,
	 * emaildateYoudanwuhuo, -1, customerid, crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime); return
	 * "/monitor/site/siteExpo"; } else if(page==7){ long branchidPram = new
	 * Long(request.getSession().getAttribute("branchidPram").toString());
	 * setExpModels(model, crateStartdate, crateEnddate, shipStartTime,
	 * shipEndTime, emailStartTime, eamilEndTime, emaildate,
	 * customerid,branchidPram,emaildateYoudanwuhuo); return
	 * "/monitor/exption/exptionExpo"; } else{ setDateModels(model,
	 * crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime,
	 * eamilEndTime, emaildate, customerid,branchid); return
	 * "/monitor/date/dateMonitorExpo"; }
	 * 
	 * } //查看详细页
	 * 
	 * @RequestMapping("/dateshow/{page}") public String dateShow(Model model,
	 * 
	 * @PathVariable("page")long page,
	 * 
	 * @RequestParam(value = "flowType",required = false,defaultValue="")String
	 * flowType,
	 * 
	 * @RequestParam(value = "branchPramid",required =
	 * false,defaultValue="-1")long branchPramid, HttpServletResponse
	 * response,HttpServletRequest request){ String crateStartdate =
	 * request.getSession().getAttribute("crateStartdate").toString(); String
	 * crateEnddate =
	 * request.getSession().getAttribute("crateEnddate").toString(); String
	 * shipStartTime =
	 * request.getSession().getAttribute("shipStartTime").toString(); String
	 * shipEndTime =
	 * request.getSession().getAttribute("shipEndTime").toString(); String
	 * emailStartTime =
	 * request.getSession().getAttribute("emailStartTime").toString(); String
	 * eamilEndTime =
	 * request.getSession().getAttribute("eamilEndTime").toString(); String
	 * emaildate = request.getSession().getAttribute("emaildate").toString();
	 * String emaildateYoudanwuhuo =
	 * request.getSession().getAttribute("emaildateYoudanwuhuo").toString();
	 * String customerid =
	 * request.getSession().getAttribute("customerid").toString(); long branchid
	 * = new Long(request.getSession().getAttribute("branchid").toString());
	 * request.getSession().setAttribute("flowType", flowType); if(page == 1){
	 * showDate(model, crateStartdate, crateEnddate, shipStartTime, shipEndTime,
	 * emailStartTime, eamilEndTime, emaildate, flowType, customerid,branchid);
	 * return "/monitor/date/dateShow"; }else if(page == 2){ showDate(model,
	 * crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime,
	 * eamilEndTime, emaildate, flowType, customerid,branchid); return
	 * "/monitor/dateGroup/dateGroupShow"; }else if(page == 3){
	 * if(flowType.equals("ydwh")){ showDateYouhuowudan(model,
	 * emaildateYoudanwuhuo, -1, customerid, crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime); }else
	 * if(flowType.equals("wrk")){ showDateWeiruku(model, emaildateYoudanwuhuo,
	 * -1, "2,4,7", customerid, crateStartdate, crateEnddate, shipStartTime,
	 * shipEndTime, emailStartTime, eamilEndTime); }else
	 * if(flowType.equals("kc")){ showDateKucun(model, crateStartdate,
	 * crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime,
	 * emaildate, "2,4,7", customerid,branchid); } else{ showDate(model,
	 * crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime,
	 * eamilEndTime, emaildate, flowType, customerid,branchid); } return
	 * "/monitor/house/houseShow"; } else if(page == 4){
	 * if(flowType.equals("ydwh")){ showDateYouhuowudan(model,
	 * emaildateYoudanwuhuo, -1, customerid, crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime); } else{
	 * showDate(model, crateStartdate, crateEnddate, shipStartTime, shipEndTime,
	 * emailStartTime, eamilEndTime, emaildate, flowType, customerid,branchid);
	 * } return "/monitor/site/siteShow"; } else if(page == 7){
	 * if(flowType.equals("ydwh")){ showDateYouhuowudan(model,
	 * emaildateYoudanwuhuo, branchPramid, customerid, crateStartdate,
	 * crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime);
	 * } else{ showDate(model, crateStartdate, crateEnddate, shipStartTime,
	 * shipEndTime, emailStartTime, eamilEndTime, emaildate, flowType,
	 * customerid,branchPramid); }
	 * request.getSession().setAttribute("branchPramid", branchPramid); return
	 * "/monitor/exption/exptionShow"; } else{ showDate(model, crateStartdate,
	 * crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime,
	 * emaildate, flowType, customerid,branchid); return
	 * "/monitor/date/dateShow"; }
	 * 
	 * } // 详细页导出
	 * 
	 * @RequestMapping("/dateShowExport/{page}") public String dateShowExp(Model
	 * model,
	 * 
	 * @PathVariable("page")long page, HttpServletResponse
	 * response,HttpServletRequest request){ String crateStartdate =
	 * request.getSession().getAttribute("crateStartdate").toString(); String
	 * crateEnddate =
	 * request.getSession().getAttribute("crateEnddate").toString(); String
	 * shipStartTime =
	 * request.getSession().getAttribute("shipStartTime").toString(); String
	 * shipEndTime =
	 * request.getSession().getAttribute("shipEndTime").toString(); String
	 * emailStartTime =
	 * request.getSession().getAttribute("emailStartTime").toString(); String
	 * eamilEndTime =
	 * request.getSession().getAttribute("eamilEndTime").toString(); String
	 * emaildate = request.getSession().getAttribute("emaildate").toString();
	 * String emaildateYoudanwuhuo =
	 * request.getSession().getAttribute("emaildateYoudanwuhuo").toString();
	 * String customerid =
	 * request.getSession().getAttribute("customerid").toString(); String
	 * flowType = request.getSession().getAttribute("flowType").toString(); long
	 * branchid = new
	 * Long(request.getSession().getAttribute("branchid").toString()); if(page
	 * == 1){ showDate(model, crateStartdate, crateEnddate, shipStartTime,
	 * shipEndTime, emailStartTime, eamilEndTime, emaildate, flowType,
	 * customerid,branchid); return "/monitor/date/dateMonitorShowExpo"; }else
	 * if(page == 2){ showDate(model, crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate,
	 * flowType, customerid,branchid); return
	 * "/monitor/dateGroup/dateGroupMonitorShowExpo"; }else if(page == 3){
	 * if(flowType.equals("ydwh")){ showDateYouhuowudan(model,
	 * emaildateYoudanwuhuo, -1, customerid, crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime); }else
	 * if(flowType.equals("wrk")){ showDateWeiruku(model, emaildateYoudanwuhuo,
	 * -1, "2,4,7", customerid, crateStartdate, crateEnddate, shipStartTime,
	 * shipEndTime, emailStartTime, eamilEndTime); } else
	 * if(flowType.equals("kc")){ showDateKucun(model, crateStartdate,
	 * crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime,
	 * emaildateYoudanwuhuo, "2,4,7", customerid,branchid); } else{
	 * showDate(model, crateStartdate, crateEnddate, shipStartTime, shipEndTime,
	 * emailStartTime, eamilEndTime, emaildate, flowType, customerid,branchid);
	 * } return "/monitor/house/houseShowExpo"; } else if(page == 4){
	 * if(flowType.equals("ydwh")){ showDateYouhuowudan(model,
	 * emaildateYoudanwuhuo, -1, customerid, crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime); } else{
	 * showDate(model, crateStartdate, crateEnddate, shipStartTime, shipEndTime,
	 * emailStartTime, eamilEndTime, emaildate, flowType, customerid,branchid);
	 * } return "/monitor/site/siteShowExpo"; } else if(page == 7){ long
	 * branchPramid = new
	 * Long(request.getSession().getAttribute("branchPramid").toString());
	 * if(flowType.equals("ydwh")){ showDateYouhuowudan(model,
	 * emaildateYoudanwuhuo, branchPramid, customerid, crateStartdate,
	 * crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime);
	 * } else{ showDate(model, crateStartdate, crateEnddate, shipStartTime,
	 * shipEndTime, emailStartTime, eamilEndTime, emaildate, flowType,
	 * customerid,branchPramid); } return "/monitor/exption/exptionShowExpo"; }
	 * else{ showDate(model, crateStartdate, crateEnddate, shipStartTime,
	 * shipEndTime, emailStartTime, eamilEndTime, emaildate, flowType,
	 * customerid,branchid); return "/monitor/date/dateMonitorShowExpo"; }
	 * 
	 * 
	 * } // 返回第一页面
	 * 
	 * @RequestMapping("/back/{page}") public String goback(Model model,
	 * 
	 * @PathVariable("page")long page, HttpServletResponse
	 * response,HttpServletRequest request){ String crateStartdate =
	 * request.getSession().getAttribute("crateStartdate").toString(); String
	 * crateEnddate =
	 * request.getSession().getAttribute("crateEnddate").toString(); String
	 * shipStartTime =
	 * request.getSession().getAttribute("shipStartTime").toString(); String
	 * shipEndTime =
	 * request.getSession().getAttribute("shipEndTime").toString(); String
	 * emailStartTime =
	 * request.getSession().getAttribute("emailStartTime").toString(); String
	 * eamilEndTime =
	 * request.getSession().getAttribute("eamilEndTime").toString(); String
	 * emaildate = request.getSession().getAttribute("emaildate").toString();
	 * String emaildateYoudanwuhuo =
	 * request.getSession().getAttribute("emaildateYoudanwuhuo").toString();
	 * String customerid =
	 * request.getSession().getAttribute("customerid").toString(); long branchid
	 * = new Long(request.getSession().getAttribute("branchid").toString());
	 * List<EmaildateTDO> emailList=
	 * deliveryService.getEmaildateAndBrandidList(crateStartdate,
	 * crateEnddate,-1); model.addAttribute("emailList", emailList); if(page ==
	 * 1){ setDateModels(model, crateStartdate, crateEnddate, shipStartTime,
	 * shipEndTime, emailStartTime, eamilEndTime, emaildate,
	 * customerid,branchid); return "/monitor/date/dateMonitor"; }else if(page
	 * == 2){ setCustomerlist(model, customerid,branchid);
	 * setDateGroupModels(model, crateStartdate, crateEnddate, shipStartTime,
	 * shipEndTime, emailStartTime, eamilEndTime, emaildate,
	 * customerid,branchid); setYoudanwuhuo(model, emaildateYoudanwuhuo, -1,
	 * customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime,
	 * emailStartTime, eamilEndTime); return
	 * "/monitor/dateGroup/dateGroupMonitor"; } else if(page == 3){
	 * setCustomerlist(model, customerid,branchid); setHouseModels(model,
	 * crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime,
	 * eamilEndTime, emaildate, customerid,branchid); setYoudanwuhuo(model,
	 * emaildateYoudanwuhuo, -1, customerid, crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime);
	 * setWeiruku(model, emaildateYoudanwuhuo, -1, "2,4,7", customerid,
	 * crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime,
	 * eamilEndTime); return "/monitor/house/houseMonitor"; } else if(page ==
	 * 4){ setCustomerlist(model, customerid,branchid); setSiteModels(model,
	 * crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime,
	 * eamilEndTime, emaildate, customerid,branchid); setYoudanwuhuo(model,
	 * emaildateYoudanwuhuo, -1, customerid, crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime); return
	 * "/monitor/site/siteMonitor"; } else if(page == 7){ long branchidPram =
	 * new Long(request.getSession().getAttribute("branchidPram").toString());
	 * setCustomerlist(model, customerid,branchidPram); setExpModels(model,
	 * crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime,
	 * eamilEndTime, emaildate, customerid,branchidPram,emaildateYoudanwuhuo);
	 * return "/monitor/exption/exptionMonitor"; } else{ setDateModels(model,
	 * crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime,
	 * eamilEndTime, emaildate, customerid,branchid); return
	 * "/monitor/date/dateMonitor"; } }
	 * 
	 * //======以下都是封装方法=============///// //存储数据监控数据 private void
	 * setDateModels(Model model,String crateStartdate,String crateEnddate,
	 * String shipStartTime,String shipEndTime, String emailStartTime,String
	 * eamilEndTime, String emaildate,String customerid ,long branchid) { //导入数据
	 * model.addAttribute("daoruDate",
	 * monitorService.getMonitorDate(crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate, "",
	 * customerid,1,branchid)); model.addAttribute("daoruDateType", ""); //库房
	 * model
	 * .addAttribute("kufangDate",monitorService.getMonitorDate(crateStartdate,
	 * crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime,
	 * emaildate, "2,3,4,5", customerid,1,branchid));
	 * model.addAttribute("kufangDateType",
	 * FlowOrderTypeEnum.TiHuo.getValue()+","+"3,4,5"); //在途
	 * model.addAttribute("zaituDate",
	 * monitorService.getMonitorDate(crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate,
	 * "6,10,11,14,17,27", customerid,1,branchid));
	 * model.addAttribute("zaituDateType", "6,10,11,14,17,27"); //站
	 * model.addAttribute
	 * ("zhanDate",monitorService.getMonitorDate(crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate,
	 * "7,8", customerid,1,branchid)); model.addAttribute("zhanDateType","7,8");
	 * //人
	 * model.addAttribute("renDate",monitorService.getMonitorDate(crateStartdate
	 * , crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime,
	 * emaildate, "9", customerid,1,branchid));
	 * model.addAttribute("renDateType","9"); //退货站
	 * model.addAttribute("tuihuoDate"
	 * ,monitorService.getMonitorDate(crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate,
	 * "15,16", customerid,1,branchid)); model.addAttribute("tuihuoDateType",
	 * "15,16"); //中转站
	 * model.addAttribute("zhongzhuanDate",monitorService.getMonitorDate
	 * (crateStartdate, crateEnddate, shipStartTime, shipEndTime,
	 * emailStartTime, eamilEndTime, emaildate, "12,13",
	 * customerid,1,branchid)); model.addAttribute("zhongzhuanDateType",
	 * "12,13"); //成功
	 * model.addAttribute("chenggongDate",monitorService.getMonitorDate
	 * (crateStartdate, crateEnddate, shipStartTime, shipEndTime,
	 * emailStartTime, eamilEndTime, emaildate, "18,19,20",
	 * customerid,1,branchid));
	 * model.addAttribute("chenggongDateType","18,19,20"); //丢失
	 * model.addAttribute
	 * ("diushiDate",monitorService.getMonitorDate(crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate,
	 * "25", customerid,1,branchid)); model.addAttribute("diushiDateType","25");
	 * //异常
	 * model.addAttribute("yichangDate",monitorService.getMonitorDate(crateStartdate
	 * , crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime,
	 * emaildate, "21,22,23,24,28", customerid,1,branchid));
	 * model.addAttribute("yichangDateType", "21,22,23,24,28"); //差
	 * model.addAttribute
	 * ("chaDate",monitorService.getMonitorDate(crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate,
	 * "35", customerid,1,branchid)); model.addAttribute("chaDateType","35"); }
	 * //存储数据组数据 private void setDateGroupModels(Model model,String
	 * crateStartdate,String crateEnddate, String shipStartTime,String
	 * shipEndTime, String emailStartTime,String eamilEndTime, String
	 * emaildate,String customerid,long branchid) { //导入数据
	 * model.addAttribute("daoruDate",
	 * monitorService.getMonitorDate(crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate, "",
	 * customerid,1,branchid)); model.addAttribute("daoruDateType", ""); //有单无货
	 * 
	 * //有货无单 model.addAttribute("youhuowudanDate",
	 * monitorService.getMonitorDate(crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate,
	 * "3,5", customerid,1,branchid)); model.addAttribute("youhuowudanDateType",
	 * "3,5");
	 * 
	 * } //存储库房信息 private void setHouseModels(Model model,String
	 * crateStartdate,String crateEnddate, String shipStartTime,String
	 * shipEndTime, String emailStartTime,String eamilEndTime, String
	 * emaildate,String customerid,long branchid) { //导入数据
	 * model.addAttribute("daoruDate",
	 * monitorService.getMonitorDate(crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate, "",
	 * customerid,1,branchid)); model.addAttribute("daoruDateType", ""); //未入库
	 * 
	 * //已入库 model.addAttribute("yirukuDate",
	 * monitorService.getMonitorDate(crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate,
	 * "2,4,7", customerid,-1,branchid)); model.addAttribute("yirukuDateType",
	 * "2,4,7"); //有单无货 //有货无单 model.addAttribute("youhuowudanDate",
	 * monitorService.getMonitorDate(crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate,
	 * "3,5", customerid,1,branchid)); model.addAttribute("youhuowudanDateType",
	 * "3,5"); //出库在途 model.addAttribute("chukuzaituDate",
	 * monitorService.getMonitorDate(crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate, "6",
	 * customerid,1,branchid)); model.addAttribute("chukuzaituDateType", "6");
	 * //库存 model.addAttribute("kucunDate",
	 * monitorService.getMonitorDate(crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate,
	 * "2,4,7", customerid,-1,branchid)); model.addAttribute("kucunDateType",
	 * "kc");
	 * 
	 * } //站点信息 private void setSiteModels(Model model,String
	 * crateStartdate,String crateEnddate, String shipStartTime,String
	 * shipEndTime, String emailStartTime,String eamilEndTime, String
	 * emaildate,String customerid,long branchid) { //导入数据
	 * model.addAttribute("daoruDate",
	 * monitorService.getMonitorDate(crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate, "",
	 * customerid,1,branchid)); model.addAttribute("daoruDateType", ""); //未到货
	 * model.addAttribute("weidaohuoDate",
	 * monitorService.getMonitorDate(crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate, "6",
	 * customerid,1,branchid)); model.addAttribute("weidaohuoDateType", "6");
	 * //入库未领 model.addAttribute("rukuweilingDate",
	 * monitorService.getMonitorDate(crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate, "7",
	 * customerid,1,branchid)); model.addAttribute("rukuweilingDateType", "7");
	 * //有货无单 model.addAttribute("youhuowudanDate",
	 * monitorService.getMonitorDate(crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate, "8",
	 * customerid,1,branchid)); model.addAttribute("youhuowudanDateType", "8");
	 * //异常单 model.addAttribute("yichangdanDate",
	 * monitorService.getMonitorDate(crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate,
	 * "29", customerid,1,branchid)); model.addAttribute("yichangdanDateType",
	 * "29"); //已领货 model.addAttribute("yilinghuoDate",
	 * monitorService.getMonitorDate(crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate, "9",
	 * customerid,1,branchid)); model.addAttribute("yilinghuoDateType", "9");
	 * //库存退货 model.addAttribute("kucuntuihuoDate",
	 * monitorService.getMonitorDate(crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate,
	 * "19,20,21,22,28", customerid,1,branchid));
	 * model.addAttribute("kucuntuihuoDateType", "19,20,21,22,28"); //库存滞留
	 * model.addAttribute("kucunzhiliuDate",
	 * monitorService.getMonitorDate(crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate,
	 * "23", customerid,1,branchid)); model.addAttribute("kucunzhiliuDateType",
	 * "23"); // 在途中转 model.addAttribute("zaituzhongzhuanDate",
	 * monitorService.getMonitorDate(crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate,
	 * "10", customerid,1,branchid));
	 * model.addAttribute("zaituzhongzhuanDateType", "10"); //在途退货
	 * model.addAttribute("zaitutuihuoDate",
	 * monitorService.getMonitorDate(crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate,
	 * "11", customerid,1,branchid)); model.addAttribute("zaitutuihuoDateType",
	 * "11"); //未交款 model.addAttribute("weijiaokuanDate",
	 * monitorService.getMonitorDate(crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate,
	 * "18,19,20,22", customerid,1,branchid));
	 * model.addAttribute("weijiaokuanDateType", "18,19,20,22");
	 * 
	 * //遗留单 超过一天 model.addAttribute("yiliudanDate",
	 * monitorService.getMonitorDate(crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate, "4",
	 * customerid,1,branchid)); model.addAttribute("yiliudanDateType", "4");
	 * //欠款 model.addAttribute("qiankuanDate",
	 * monitorService.getMonitorDate(crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate,
	 * "25,31", customerid,1,branchid)); model.addAttribute("qiankuanDateType",
	 * "25,31");
	 * 
	 * //已领货：9 （所有已领货未反馈 按人） // -遗留单：（入库未派 超过一天） //库存 -退货：（19、20、21、22、28） //
	 * -滞留：23
	 * 
	 * //欠款：25、30 -31 }
	 * 
	 * //有单无货 统计数量 private void setYoudanwuhuo(Model model,String emaildate,long
	 * branchid,String customerid, String crateStartdate,String crateEnddate,
	 * String shipStartTime,String shipEndTime, String emailStartTime,String
	 * eamilEndTime){ model.addAttribute("youdanwuhuoDate",
	 * monitorService.getMonitorDateYoudanwuhuo(emaildate, branchid, customerid,
	 * crateStartdate, crateEnddate, shipStartTime, shipEndTime, emailStartTime,
	 * eamilEndTime)); model.addAttribute("youdanwuhuoDateType", "ydwh"); }
	 * //未入库 统计数量 private void setWeiruku(Model model,String emaildate,long
	 * branchid,String flowordertype, String customerid,String
	 * crateStartdate,String crateEnddate, String shipStartTime,String
	 * shipEndTime, String emailStartTime,String eamilEndTime){
	 * model.addAttribute("weirukuDate",
	 * monitorService.getMonitorDateWeiruku(emaildate, branchid, flowordertype,
	 * customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime,
	 * emailStartTime, eamilEndTime)); model.addAttribute("weirukuDateType",
	 * "wrk"); }
	 * 
	 * // 查询具体订单数据 private void showDate(Model model,String
	 * crateStartdate,String crateEnddate, String shipStartTime,String
	 * shipEndTime, String emailStartTime,String eamilEndTime, String
	 * emaildate,String flowordertype,String customerid,long branchid){
	 * model.addAttribute("showDateList",
	 * monitorService.getMonitorOrderList(crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate,
	 * flowordertype, customerid,1,branchid)); } //有单无货 查看 private void
	 * showDateYouhuowudan(Model model,String emaildate,long branchid,String
	 * customerid, String crateStartdate,String crateEnddate, String
	 * shipStartTime,String shipEndTime, String emailStartTime,String
	 * eamilEndTime){ model.addAttribute("showDateList",
	 * monitorService.getMonitorOrderYoudanwuhuoList(emaildate, branchid,
	 * customerid, crateStartdate, crateEnddate, shipStartTime, shipEndTime,
	 * emailStartTime, eamilEndTime)); } //未入库 查看 private void
	 * showDateWeiruku(Model model,String emaildate,long branchid,String
	 * flowordertype, String customerid,String crateStartdate,String
	 * crateEnddate, String shipStartTime,String shipEndTime, String
	 * emailStartTime,String eamilEndTime){ model.addAttribute("showDateList",
	 * monitorService.getMonitorOrderWeirukuList(emaildate, branchid,
	 * flowordertype, customerid, crateStartdate, crateEnddate, shipStartTime,
	 * shipEndTime, emailStartTime, eamilEndTime)); } // 查询库存 private void
	 * showDateKucun(Model model,String crateStartdate,String crateEnddate,
	 * String shipStartTime,String shipEndTime, String emailStartTime,String
	 * eamilEndTime, String emaildate,String flowordertype,String
	 * customerid,long branchid){ model.addAttribute("showDateList",
	 * monitorService.getMonitorOrderList(crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate,
	 * flowordertype, customerid,-1,branchid)); } //异常信息监控 private void
	 * setExpModels(Model model,String crateStartdate,String crateEnddate,
	 * String shipStartTime,String shipEndTime, String emailStartTime,String
	 * eamilEndTime, String emaildate,String customerid,long branchid,String
	 * emaildateYoudanwuhuo) { List<Branch> branchnameList =
	 * branchDao.getAllBranches(); Map map = new HashMap(); if(branchnameList !=
	 * null && branchnameList.size()>0 && branchid < 0){ List<MonitorDTO>
	 * youhuowudanList = monitorService.getExpMonitorDate(crateStartdate,
	 * crateEnddate, shipStartTime, shipEndTime, emailStartTime, eamilEndTime,
	 * emaildate, "8", customerid,1,branchid); List<MonitorDTO> yichangdanList
	 * =monitorService.getExpMonitorDate(crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate,
	 * "29", customerid,1,branchid); List<MonitorDTO> youdanwuhuoList
	 * =monitorService.getExpMonitorDateYoudanwuhuo(emaildateYoudanwuhuo,
	 * branchid, customerid, crateStartdate, crateEnddate, shipStartTime,
	 * shipEndTime, emailStartTime, eamilEndTime); for (int i = 0; i <
	 * branchnameList.size(); i++) { Map map1 = new HashMap();
	 * if(youhuowudanList!=null && youhuowudanList.size()>0){ for (int j = 0; j
	 * < youhuowudanList.size(); j++) { if(youhuowudanList.get(j).getBranchid()
	 * == branchnameList.get(i).getBranchid() ){
	 * map1.put("youhuowudan_"+branchnameList.get(i).getBranchid(),
	 * youhuowudanList.get(j).getCountsum()); break; } } }
	 * if(yichangdanList!=null && yichangdanList.size()>0){ for (int j = 0; j <
	 * yichangdanList.size(); j++) { if(yichangdanList.get(j).getBranchid() ==
	 * branchnameList.get(i).getBranchid() ){
	 * map1.put("yichangdan_"+branchnameList.get(i).getBranchid(),
	 * yichangdanList.get(j).getCountsum()); break; } } }
	 * if(youdanwuhuoList!=null && youdanwuhuoList.size()>0){ for (int j = 0; j
	 * < youdanwuhuoList.size(); j++) { if(youdanwuhuoList.get(j).getBranchid()
	 * == branchnameList.get(i).getBranchid() ){
	 * map1.put("youdanwuhuo_"+branchnameList.get(i).getBranchid(),
	 * youdanwuhuoList.get(j).getCountsum()); break; } } }
	 * map.put(branchnameList.get(i).getBranchid(), map1); }
	 * 
	 * }else if(branchid > 0){ List<MonitorDTO> youhuowudanList =
	 * monitorService.getExpMonitorDate(crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate, "8",
	 * customerid,1,branchid); List<MonitorDTO> yichangdanList
	 * =monitorService.getExpMonitorDate(crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate,
	 * "29", customerid,1,branchid); List<MonitorDTO> youdanwuhuoList
	 * =monitorService.getExpMonitorDateYoudanwuhuo(emaildateYoudanwuhuo,
	 * branchid, customerid, crateStartdate, crateEnddate, shipStartTime,
	 * shipEndTime, emailStartTime, eamilEndTime);
	 * 
	 * Map map1 = new HashMap(); if(youhuowudanList!=null &&
	 * youhuowudanList.size()>0){ for (int j = 0; j < youhuowudanList.size();
	 * j++) { if(youhuowudanList.get(j).getBranchid() == branchid ){
	 * map1.put("youhuowudan_"+branchid, youhuowudanList.get(j).getCountsum());
	 * break; } } } if(yichangdanList!=null && yichangdanList.size()>0){ for
	 * (int j = 0; j < yichangdanList.size(); j++) {
	 * if(yichangdanList.get(j).getBranchid() == branchid ){
	 * map1.put("yichangdan_"+branchid, yichangdanList.get(j).getCountsum());
	 * break; } } } if(youdanwuhuoList!=null && youdanwuhuoList.size()>0){ for
	 * (int j = 0; j < youdanwuhuoList.size(); j++) {
	 * if(youdanwuhuoList.get(j).getBranchid() == branchid ){
	 * map1.put("youdanwuhuo_"+branchid, youdanwuhuoList.get(j).getCountsum());
	 * break; } } } map.put(branchid, map1); }
	 * model.addAttribute("branchObjectNow", branchid>0?
	 * branchDao.getBranchById(branchid):null);
	 * model.addAttribute("branchidNow", branchid);
	 * model.addAttribute("exptionMap", map);
	 * model.addAttribute("youhuowudanDateType", "8");
	 * model.addAttribute("yichangdanDateType", "29");
	 * model.addAttribute("youdanwuhuoDateType", "ydwh"); //异常单
	 * 
	 * }
	 * 
	 * //财务信息监控 private void setMoneyModels(Model model,String
	 * crateStartdate,String crateEnddate, String shipStartTime,String
	 * shipEndTime, String emailStartTime,String eamilEndTime, String
	 * emaildate,String customerid,long branchid,String emaildateYoudanwuhuo) {
	 * List<Branch> branchnameList = branchDao.getAllBranches(); Map map = new
	 * HashMap(); if(branchnameList != null && branchnameList.size()>0 &&
	 * branchid < 0){ List<MonitorDTO> yirukuList =
	 * monitorService.getExpMonitorDate(crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate,
	 * "2,4,7", customerid,-1,branchid); List<MonitorDTO> yijiaokuanList
	 * =monitorService.getExpMonitorDate(crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate,
	 * "30", customerid,1,branchid); List<MonitorDTO> qiankuanList
	 * =monitorService.getExpMonitorDate(crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate,
	 * "30", customerid,1,branchid); List<MonitorDTO> kucunList
	 * =monitorService.getExpMonitorDate(crateStartdate, crateEnddate,
	 * shipStartTime, shipEndTime, emailStartTime, eamilEndTime, emaildate,
	 * "2,4,7", customerid,1,branchid); for (int i = 0; i <
	 * branchnameList.size(); i++) { Map map1 = new HashMap();
	 * if(yirukuList!=null && yirukuList.size()>0){ for (int j = 0; j <
	 * yirukuList.size(); j++) { if(yirukuList.get(j).getBranchid() ==
	 * branchnameList.get(i).getBranchid() ){
	 * map1.put("yiruku_"+branchnameList.get(i).getBranchid(),
	 * yirukuList.get(j).getCountsum()); break; } } } if(yijiaokuanList!=null &&
	 * yijiaokuanList.size()>0){ for (int j = 0; j < yijiaokuanList.size(); j++)
	 * { if(yijiaokuanList.get(j).getBranchid() ==
	 * branchnameList.get(i).getBranchid() ){
	 * map1.put("yijiaokuan_"+branchnameList.get(i).getBranchid(),
	 * yijiaokuanList.get(j).getCaramountsum()); break; } } }
	 * if(qiankuanList!=null && qiankuanList.size()>0){ for (int j = 0; j <
	 * qiankuanList.size(); j++) { if(qiankuanList.get(j).getBranchid() ==
	 * branchnameList.get(i).getBranchid() ){
	 * map1.put("qiankuan_"+branchnameList.get(i).getBranchid(),
	 * qiankuanList.get(j).getCaramountsum()); break; } } } if(kucunList!=null
	 * && kucunList.size()>0){ for (int j = 0; j < kucunList.size(); j++) {
	 * if(kucunList.get(j).getBranchid() == branchnameList.get(i).getBranchid()
	 * ){ map1.put("kucun_"+branchnameList.get(i).getBranchid(),
	 * kucunList.get(j).getCountsum()); break; } } }
	 * 
	 * map.put(branchnameList.get(i).getBranchid(), map1); }
	 * 
	 * } model.addAttribute("moneyMap", map);
	 * model.addAttribute("yirukuDateType", "2,4,7");
	 * model.addAttribute("yijiaokuanDateType", "30");
	 * model.addAttribute("qiankuanDateType", "qiank");
	 * model.addAttribute("kucunDateType", "kc"); //异常单
	 * 
	 * } //保存基本查询条件的session private void setSetions(HttpServletRequest
	 * request,String crateStartdate,String crateEnddate, String
	 * shipStartTime,String shipEndTime, String emailStartTime,String
	 * eamilEndTime, String emaildate,String customerid,long branchid){
	 * request.getSession().setAttribute("crateStartdate", crateStartdate);
	 * request.getSession().setAttribute("crateEnddate", crateEnddate);
	 * request.getSession().setAttribute("shipStartTime", shipStartTime);
	 * request.getSession().setAttribute("shipEndTime", shipEndTime);
	 * request.getSession().setAttribute("emailStartTime", emailStartTime);
	 * request.getSession().setAttribute("eamilEndTime", eamilEndTime);
	 * request.getSession().setAttribute("emaildate", emaildate);
	 * request.getSession().setAttribute("customerid", customerid);
	 * request.getSession().setAttribute("branchid", branchid); }
	 * //保存前台传递过来的供货商解析后存储到列表中 private void setCustomerlist(Model model,String
	 * customerid,long branchid){ List<Customer> cumstrListAll =
	 * customerDao.getAllCustomers(); //保存前台传递过来的供货商解析后存储到列表中 List cumstrList1 =
	 * new ArrayList(); if(customerid != null && customerid.length()>0){
	 * String[] cStr = customerid.split(","); for (int i = 0; i < cStr.length;
	 * i++) { cumstrList1.add(cStr[i]); } } model.addAttribute("cumstrList1",
	 * cumstrList1); model.addAttribute("cumstrListAll", cumstrListAll);
	 * //查询所有的站点 List<Branch> branchnameList = branchDao.getAllBranches();
	 * model.addAttribute("branchnameList", branchnameList); //查询当前站点
	 * ExplinkUserDetail userDetail= (ExplinkUserDetail)
	 * securityContextHolderStrategy
	 * .getContext().getAuthentication().getPrincipal(); long branchidSession =
	 * userDetail == null?-1:userDetail.getUser().getBranchid(); long
	 * branchidPram = branchid==-1? branchidSession:branchid;
	 * model.addAttribute("branchidSession", branchidPram);
	 * 
	 * }
	 */

}