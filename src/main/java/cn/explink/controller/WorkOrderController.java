package cn.explink.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.WorkOrderDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.CsComplaintAccept;
import cn.explink.domain.CsComplaintAcceptVO;
import cn.explink.domain.CsConsigneeInfo;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.CwbOrderAndCustomname;
import cn.explink.domain.Reason;
import cn.explink.domain.User;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.CwbStateEnum;
import cn.explink.service.CwbOrderService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.WorkOrderService;
import cn.explink.util.DateTimeUtil;
/**
 * 
 * @author wangzhiyong
 * @作用 返回到相应的工单页面
 */
@Controller
@RequestMapping("/workorder")      
public class WorkOrderController {
	@Autowired
	private CwbOrderService cos;
	@Autowired
	private ReasonDao reasondao;
	@Autowired
	private BranchDAO branchdao;
	@Autowired
	private CustomerDAO cd;
	@Autowired
	private WorkOrderDAO workorderdao;
	@Autowired
	private WorkOrderService workorderservice;
	@Autowired
	private OrderFlowDAO orderflowdao;
	@Autowired
	private CwbDAO cwbdao;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	
	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy
				.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}
	
	@RequestMapping("/CustomerServicesAcceptWorkOrder")
	public String CustomerServicesAcceptWorkOrder(){
		
		return "workorder/CustomerServicesAcceptWorkOrder";
	}
	@RequestMapping("/CustomerServicesAcceptWorkOrder01")
	public String CustomerServicesAcceptWorkOrder01(){
		
		return "workorder/CustomerServicesAcceptWorkOrder01";
	}

	
	@RequestMapping("/CallerArchivalRepository")
	public String CallerArchivalRepository(Model model){
		List<CsConsigneeInfo> ccilist=workorderdao.queryAllCsConsigneeInfo();
		model.addAttribute("ccilist", ccilist);
		
		
		return "workorder/CallerArchivalRepository";
	}
	
	@RequestMapping("/EditEditMaintain")
	public String EditEditMaintain(Model model,@RequestParam(value="phoneone",defaultValue="",required=true) String phone) throws Exception{
		CsConsigneeInfo ccf=workorderdao.queryByPhoneNum(phone);
		
		return "workorder/EditMaintain";
	}
	@RequestMapping("/addCallerArchival")
	@ResponseBody
	public String addCallerArchival(CsConsigneeInfo ccf){
		workorderdao.saveAllCsConsigneeInfo(ccf);
		return "{\"errorCode\":0,\"error\":\"添加成功\"}";
	}
	@RequestMapping("/removeCallerArchival")
	@ResponseBody
	public String removeCallerArchival(@RequestParam(value="phoneone",defaultValue="",required=true) String phone){
		workorderdao.remove(phone);
		return "{\"errorCode\":0,\"error\":\"删除成功\"}";
	}
	
	@RequestMapping("/WaitManageWorkOrder")
	public String WaitManageWorkOrder(){
		
		return "workorder/WaitManageWorkOrder";
	}
	
	@RequestMapping("/CreateQueryWorkOrder")
	public String CreateQueryWorkOrder(Model model,@RequestParam(value="opscwbid",defaultValue="",required=true) int opscwbid){
		String transcwb="G"+DateTimeUtil.getNowTimeNo()+((int)Math.random()*10);	
		CwbOrder cl=cwbdao.createQueryWo(opscwbid);
		CsComplaintAccept cca = new CsComplaintAccept();
			cca.setPhoneOne(cl.getConsigneemobile());
			cca.setProvence(cl.getConsigneeaddress());
			cca.setAcceptNo(transcwb);
			cca.setOrderNo(cl.getCwb());
			cca.setCwbstate(cl.getCwbstate());//(CwbStateEnum.getByValue((int) cl.getCwbstate()).getText());
			cca.setFlowordertype(cl.getFlowordertype());//(CwbFlowOrderTypeEnum.getText(cl.getFlowordertype()).getText());
			Branch b=branchdao.getbranchname(cl.getCurrentbranchid());
			if(b!=null){
			cca.setCurrentBranch(b.getBranchname());
			}else{
			cca.setCurrentBranch("");	
			}
		model.addAttribute("cca", cca);

		return "workorder/CreateQueryWorkOrder";
	}
	
	@RequestMapping("/CreateComplainWorkOrder")
	public String CreateComplainWorkOrder(Model model,@RequestParam(value="opscwbid",defaultValue="",required=true) int opscwbid){
		String transcwb="G"+DateTimeUtil.getNowTimeNo()+((int)Math.random()*10);	
		CwbOrder cl=cwbdao.createQueryWo(opscwbid);
		CsComplaintAccept ca = new CsComplaintAccept();
			ca.setProvence(cl.getConsigneeaddress());
			ca.setPhoneOne(cl.getConsigneemobile());
			ca.setAcceptNo(transcwb); //存入工单
			ca.setOrderNo(cl.getCwb());//存入订单
			ca.setCwbstate(cl.getCwbstate());//存入订单状态
			ca.setFlowordertype(cl.getFlowordertype());//存入订单操作状态
			Branch b=branchdao.getbranchname(cl.getCurrentbranchid());//存入站点信息
			if(b!=null){
			ca.setCurrentBranch(b.getBranchname());
			}else{
			ca.setCurrentBranch("");	
			}
			List<Branch> lb=branchdao.getAllBranches();
			List<Reason> lr=reasondao.addWO();
			List<Reason> lrs=null;
			for(Reason r:lr){
				 lrs=reasondao.getSecondLevelReason(r.getReasonid());
			}
			
			model.addAttribute("lr", lr);
			model.addAttribute("lrs", lrs);
			model.addAttribute("ca", ca);
			model.addAttribute("lb", lb);
		return "workorder/CreateComplainWorkOrder";
	}
	
	@RequestMapping("/NewAddMaintain")
	public String NewAddMaintain(){
		
		
		return "workorder/NewAddMaintain";
	}
	@RequestMapping("/OrgVerify")
	public String OrgVerify(Model model,@RequestParam(value="acceptNo",defaultValue="",required=true) String acceptNo) throws Exception{
		CsComplaintAccept cca=workorderdao.findGoOnacceptWOByWorkOrder(acceptNo);
		CwbOrder co=cwbdao.getOneCwbOrderByCwb(cca.getOrderNo());
		CsConsigneeInfo cci=workorderdao.queryByPhoneNum(cca.getPhoneOne());
		List<Branch> lb=branchdao.getAllBranches();		
		model.addAttribute("lb", lb);		
		model.addAttribute("cci", cci);
		model.addAttribute("cca", cca);		//存入工单信息
		model.addAttribute("co", co);    //存入订单信息表
		return "workorder/OrgVerify";
	}
	@RequestMapping("/CustomerServiceAdjudicate")
	public String CustomerServiceAdjudicate(Model model,@RequestParam(value="acceptNo",defaultValue="",required=true) String acceptNo) throws Exception{     //客服结案
		CsComplaintAccept cca=workorderdao.findGoOnacceptWOByWorkOrder(acceptNo);
		CwbOrder co=cwbdao.getOneCwbOrderByCwb(cca.getOrderNo());
		CsConsigneeInfo cci=workorderdao.queryByPhoneNum(cca.getPhoneOne());
		List<Branch> lb=branchdao.getAllBranches();		
		model.addAttribute("lb", lb);		
		model.addAttribute("cci", cci);
		model.addAttribute("cca", cca);		//存入工单信息
		model.addAttribute("co", co);    //存入订单信息表
		return "workorder/CustomerServiceAdjudicate";
	}
	@RequestMapping("/OrgAppeal")
	public String OrgAppeal(Model model,@RequestParam(value="acceptNo",defaultValue="",required=true) String acceptNo) throws Exception{
		CsComplaintAccept cca=workorderdao.findGoOnacceptWOByWorkOrder(acceptNo);
		CwbOrder co=cwbdao.getOneCwbOrderByCwb(cca.getOrderNo());
		CsConsigneeInfo cci=workorderdao.queryByPhoneNum(cca.getPhoneOne());
		List<Branch> lb=branchdao.getAllBranches();
		
		model.addAttribute("lb", lb);   
		model.addAttribute("cci", cci);
		model.addAttribute("cca", cca);		//存入工单信息
		model.addAttribute("co", co);    //存入订单信息表
		return "workorder/OrgAppeal";
	}
	@RequestMapping("/AdjudicateRetrial")
	public String AdjudicateRetrial(Model model,@RequestParam(value="acceptNo",defaultValue="",required=true) String acceptNo) throws Exception{
		CsComplaintAccept cca=workorderdao.findGoOnacceptWOByWorkOrder(acceptNo);
		CwbOrder co=cwbdao.getOneCwbOrderByCwb(cca.getOrderNo());
		CsConsigneeInfo cci=workorderdao.queryByPhoneNum(cca.getPhoneOne());
		List<Branch> lb=branchdao.getAllBranches();
		
		model.addAttribute("lb", lb);   
		model.addAttribute("cci", cci);
		model.addAttribute("cca", cca);		//存入工单信息
		model.addAttribute("co", co);    //存入订单信息表
		return "workorder/AdjudicateRetrial";
	}
	//添加客服收件人数据
	@RequestMapping("/addCsConsigneeInfo")
	public String add(CsConsigneeInfo cci){
		
		workorderservice.addcsconsigneeInfo(cci);
		return "workorder/CustomerServicesAcceptWorkOrder";	
	}
	
	@RequestMapping("/selectByPhoneNum")
	@ResponseBody
	public CsConsigneeInfo selectByPhoneNum(HttpServletRequest req) throws Exception{
		String phone=req.getParameter("phoneonOne");
		CsConsigneeInfo cci=null;
		if(workorderservice.querycciByPhoneNum(phone)!=null){
			cci=workorderservice.querycciByPhoneNum(phone);
		}
		return cci;		
	}
	
	@RequestMapping("/SelectdetalForm")
	@ResponseBody
	public  List<CwbOrderAndCustomname> SelectdetalForm(HttpServletRequest req){
		String phone=req.getParameter("phoneonOne");
		List<CwbOrder> cwborderlist=workorderservice.SelectCwbdetalForm(phone);
		List<CwbOrderAndCustomname> lc= new ArrayList<CwbOrderAndCustomname>();
		for(CwbOrder c:cwborderlist){
			CwbOrderAndCustomname co = new CwbOrderAndCustomname();
				co.setId(c.getOpscwbid());
				co.setCwb(c.getCwb());
				co.setTranscwb(c.getTranscwb());
				co.setCustomername(cd.findcustomername(c.getCustomerid()).getCustomername());
				co.setEmaildate(c.getEmaildate());
				co.setConsigneename(c.getConsigneename());
				co.setConsigneeaddress(c.getConsigneeaddress());
				co.setConsigneemobile(c.getConsigneemobile());
				co.setCwbstate(CwbStateEnum.getByValue(c.getCwbstate()).getText());
				lc.add(co);				
		}		
				
		return lc;
	}
	
	@RequestMapping("/selectDetalFormByCondition")
	@ResponseBody
	public List<CwbOrderAndCustomname> selectDetalFormByCondition(CwbOrderAndCustomname coc,Model model){
	
		String cwb1 = cos.translateCwb(coc.getCwb());
		System.out.println(cwb1);
		coc.setCwb(cwb1);
		List<CwbOrder> lc=cwbdao.SelectDetalFormByCondition(coc);
	
		List<CwbOrderAndCustomname> lco= new ArrayList<CwbOrderAndCustomname>();
		//System.out.println(lc.get(0).getCwb());
		for(CwbOrder c:lc){
			CwbOrderAndCustomname co = new CwbOrderAndCustomname();
				co.setId((c.getOpscwbid()));
				co.setCwb(c.getCwb());
				co.setTranscwb(c.getTranscwb());
				co.setCustomername(cd.findcustomername(c.getCustomerid()).getCustomername());
				co.setEmaildate(c.getEmaildate());
				co.setConsigneename(c.getConsigneename());
				co.setConsigneeaddress(c.getConsigneeaddress());
				co.setConsigneemobile(c.getConsigneemobile());
				co.setCwbstate(CwbStateEnum.getByValue(c.getCwbstate()).getText());
				lco.add(co);				
		}
		//model.addAttribute("lco", lco);
		return lco;
	}
	
/*	@RequestMapping("/createqueryWo")
	@ResponseBody
	public CsComplaintAccept createqueryWo(@RequestParam(value="opscwbid",defaultValue="",required=true) int opscwbidHttpServletRequest req){
		String trcwb=req.getParameter("cwb");
		String transcwb="G"+DateTimeUtil.getNowTimeNo()+((int)Math.random()*10);	
		CwbOrder cl=cwbdao.createQueryWo(opscwbid);
		CsComplaintAccept cca = new CsComplaintAccept();
			cca.setAcceptNo(transcwb);
			cca.setOrderNo(cl.getCwb());
			cca.setCwbstate(CwbStateEnum.getByValue((int) cl.getCwbstate()).getText());
			cca.setFlowordertype(CwbFlowOrderTypeEnum.getText(cl.getFlowordertype()).getText());
			Branch b=branchdao.getbranchname(cl.getCurrentbranchid());
			if(b!=null){
			cca.setCurrentBranch(b.getBranchname());
			}else{
			cca.setCurrentBranch("");	
			}
		System.out.println(cca.getAcceptNo());
		System.out.println(cca.getOrderNo());
		System.out.println(cca.getCwbstate());
		System.out.println(cca.getFlowordertype());
		System.out.println(cca.getCurrentBranch());
			
			return cca;		
	}*/
	
	@RequestMapping("/saveWorkOrderQueryF")
	@ResponseBody
	public String saveWorkOrderQueryForm(CsComplaintAccept cca){
		//System.out.println(cca.getComplaintState());
		String accepttime=DateTimeUtil.formatDate(new Date());
		cca.setAcceptTime(accepttime);		
		workorderdao.savequeryworkorderForm(cca);
	
		return "{\"errorCode\":0,\"error\":\"保存成功\"}";
	}
	
	@RequestMapping("/saveComplainWorkOrderF")
	@ResponseBody
	public String saveComplainWorkOrderF(CsComplaintAccept cca){
		//System.out.println(cca.getComplaintState());
		String accepttime=DateTimeUtil.formatDate(new Date());
		cca.setAcceptTime(accepttime);
		workorderdao.saveComplainWorkOrderF(cca);
	
		return "{\"errorCode\":0,\"error\":\"保存成功\"}";
	}
	
	
	@RequestMapping("/updateComplainWorkOrderF")
	@ResponseBody
	public String updateComplainWorkOrderF(CsComplaintAccept cca){
		String accepttime=DateTimeUtil.formatDate(new Date());
		cca.setAcceptTime(accepttime);
		workorderdao.updateComplainWorkOrderF(cca);;
	
		return "{\"errorCode\":0,\"error\":\"受理成功\"}";
	}
	

	@RequestMapping("/findGoOnAcceptWO")  //通过手机号查询工单
	@ResponseBody
	public List<CsComplaintAccept> findGoOnAcceptWO(Model model,HttpServletRequest req){
		String phone=req.getParameter("phoneonOne");
		//System.out.println(phone+"~~");
		List<CsComplaintAccept> lcs=workorderdao.findGoOnacceptWO(phone);
		List<CsComplaintAccept> lc=new ArrayList<CsComplaintAccept>();
		for(CsComplaintAccept c:lcs){
			CsComplaintAccept ca = new CsComplaintAccept();
			ca.setAcceptNo(c.getAcceptNo());
			ca.setOrderNo(c.getOrderNo());
			ca.setPhoneOne(c.getPhoneOne());
			ca.setAcceptTime(c.getAcceptTime());
			ca.setComplaintType(c.getComplaintType());
			ca.setContent(c.getContent());
			ca.setComplaintState(c.getComplaintState());
			ca.setProvence(c.getProvence());
			lc.add(ca);
		}
		
		return lc;

	}
	
	@RequestMapping("/findGoOnAcceptWOByCWB")  //通过手机号查询工单
	@ResponseBody
	public List<CsComplaintAccept> findGoOnAcceptWOByCWB(Model model,HttpServletRequest req){
		String cwb=req.getParameter("cwb");
		//System.out.println(cwb+"~~");
		List<CsComplaintAccept> lcs=workorderdao.findGoOnacceptWOByCWB(cwb);
		List<CsComplaintAccept> lc=new ArrayList<CsComplaintAccept>();
		for(CsComplaintAccept c:lcs){
			CsComplaintAccept ca = new CsComplaintAccept();
			ca.setAcceptNo(c.getAcceptNo());
			ca.setOrderNo(c.getOrderNo());
			ca.setPhoneOne(c.getPhoneOne());
			ca.setAcceptTime(c.getAcceptTime());
			ca.setComplaintType(c.getComplaintType());
			ca.setContent(c.getContent());
			ca.setComplaintState(c.getComplaintState());
			ca.setProvence(c.getProvence()/*workorderdao.queryprovinceByphone(cwb).get(workorderdao.queryprovinceByphone(cwb).size()-1).getProvince()*/);
			lc.add(ca);
		}
		
		return lc;

	}
	
	@RequestMapping("/GoOnacceptWo")
	public String GoOnacceptWo(HttpServletRequest req,Model model){
		String workorder=req.getParameter("workorder");
		CsComplaintAccept lcs=null;
		if(workorder!=null){
			lcs=workorderdao.findGoOnacceptWOByWorkOrder(workorder);	
		}
			String cwb=lcs.getOrderNo();  //获取订单号
			List<CwbOrder> lco=cwbdao.getAllCwbOrderByCwb(cwb);
			String conMobile=lco.get(0).getConsigneemobile();
//			List<OrderFlow> lof=orderflowdao.getRuKuTimeByCwb(cwb);			
			Date createTime=orderflowdao.getRuKuTimeByCwb(cwb).getCredate();
			SimpleDateFormat dateformat1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String RuKuTime=dateformat1.format(createTime); //获取入库时间
			List<Reason> lr=reasondao.addWO();
			List<Reason> lrs=null;
			for(Reason r:lr){
				 lrs=reasondao.getSecondLevelReason(r.getReasonid());
			}
			List<Branch> lb=branchdao.getAllBranches();
			
			model.addAttribute("lr", lr);
			model.addAttribute("lrs", lrs);
			model.addAttribute("lcs",lcs );
			model.addAttribute("conMobile",conMobile );
			model.addAttribute("RuKuTime",RuKuTime );
			model.addAttribute("lb", lb);
			
			return "workorder/CreateGoOnAcceptWorkOrder";			
	}
	
	@RequestMapping("/refreshwo")
	public String refreshWo(CsComplaintAccept ca,Model model){
		
		List<CsComplaintAccept> lc=	workorderdao.refreshWO(ca.getComplaintState());
		System.out.println(lc.size());
		model.addAttribute("lc", lc);
		
		return "workorder/WaitManageWorkOrder";
	}
	/*工单号 	订单号 	工单类型 	工单状态 	
	来电人姓名 	来电号码 	被投诉机构 	工单受理人 	
	受理时间 	投诉一级分类 	投诉二级分类 	
	投诉处理结果 	是否扣罚 	客户名称	催件次数*/
	@RequestMapping("/WorkOrderManageQuery")
	public String WorkOrderManageQuery(HttpServletRequest req,Model model,CsComplaintAcceptVO cv) throws Exception{
	
		String cwb[]=cv.getOrderNo().split("\r\n");
		StringBuffer sb = new StringBuffer();
		for(String str:cwb){
			sb=sb.append("'"+str+"',");
		}
		String ncwbs=new String();
		 ncwbs=sb.substring(0, sb.length()-1);		
	List<CsComplaintAccept> lcs=workorderdao.findGoOnacceptWOByCWBs(ncwbs,cv);
		
		List<CsComplaintAccept> lc=new ArrayList<CsComplaintAccept>();
		List<CsConsigneeInfo> consigneeinfolist=null;
		Map<String,List<CsConsigneeInfo>> maplist=new HashMap<String,List<CsConsigneeInfo>>();
		for(CsComplaintAccept c:lcs){
			CsComplaintAccept ca = new CsComplaintAccept();
			ca.setAcceptNo(c.getAcceptNo());
			ca.setOrderNo(c.getOrderNo());
			ca.setComplaintType(c.getComplaintType());
			ca.setComplaintState(c.getComplaintState());
			ca.setCodOrgId(c.getCodOrgId());
			ca.setComplaintOneLevel(c.getComplaintOneLevel());
			ca.setComplaintTwoLevel(c.getComplaintTwoLevel());
			ca.setComplaintResult(c.getComplaintResult());
			ca.setAcceptTime(c.getAcceptTime());
			ca.setPhoneOne(c.getPhoneOne());			
			lc.add(ca);
			consigneeinfolist=workorderdao.queryByPhoneone(c.getPhoneOne());
			maplist.put(c.getPhoneOne(), consigneeinfolist);
			
	}
		List<CwbOrder> co=cwbdao.getCwbByCwbs(ncwbs);
		String username=getSessionUser().getUsername();
		List<Branch> lb=branchdao.getAllBranches();
		List<CsComplaintAccept> lcsa=workorderdao.refreshWOFPage();
		List<Reason> lr=reasondao.addWO();
		List<Reason> lrs=null;
		for(Reason r:lr){
			 lrs=reasondao.getSecondLevelReason(r.getReasonid());
		}
		
		model.addAttribute("lr", lr);
		model.addAttribute("lrs", lrs);
		model.addAttribute("lcsa", lcsa);
		model.addAttribute("lb", lb); 
		model.addAttribute("lc", lc);
		model.addAttribute("username", username);
		model.addAttribute("maplist", maplist);
		model.addAttribute("co", co);	
	
		return "workorder/WorkOrderQueryManage";		
	}
	@RequestMapping("/WorkOrderQueryManage")
	public String WorkOrderQueryManage(Model model){
		List<CsComplaintAccept> lcsa=workorderdao.refreshWOFPage();
		List<Reason> lr=reasondao.addWO();
		List<Reason> lrs=null;
		for(Reason r:lr){
			 lrs=reasondao.getSecondLevelReason(r.getReasonid());
		}
		List<Branch> lb=branchdao.getAllBranches();
		
		model.addAttribute("lr", lr);
		model.addAttribute("lrs", lrs);
		model.addAttribute("lcsa", lcsa);
		model.addAttribute("lb", lb);
		
		return "workorder/WorkOrderQueryManage";
	}
	
}
