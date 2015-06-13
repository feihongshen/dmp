package cn.explink.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
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
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.WorkOrderDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.CsComplaintAccept;
import cn.explink.domain.CsComplaintAcceptExportVO;
import cn.explink.domain.CsComplaintAcceptVO;
import cn.explink.domain.CsConsigneeInfo;
import cn.explink.domain.CsConsigneeInfoVO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.CwbOrderAndCustomname;
import cn.explink.domain.Reason;
import cn.explink.domain.User;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.ComplaintResultEnum;
import cn.explink.enumutil.ComplaintStateEnum;
import cn.explink.enumutil.ComplaintTypeEnum;
import cn.explink.enumutil.CwbStateEnum;
import cn.explink.service.CwbOrderService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.service.WorkOrderService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.ExcelUtils;
import cn.explink.util.Page;
import cn.explink.util.ResourceBundleUtil;
import cn.explink.util.StringUtil;
/**
 * 
 * @author wangzhiyong
 * @作用 返回到相应的工单页面
 */
@Controller
@RequestMapping("/workorder")      
public class WorkOrderController {
	@Autowired
	private SystemInstallDAO systeminstalldao;
	@Autowired
	private CwbOrderService cos;
	@Autowired
	private ReasonDao reasondao;
	@Autowired
	private BranchDAO branchdao;
	@Autowired
	private CustomerDAO customerDAO;
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
	@Autowired
	ExportService es;
	

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
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

	
	@RequestMapping("/CallerArchivalRepository/{page}")
	public String CallerArchivalRepository(Model model,CsConsigneeInfo cci,@PathVariable(value="page") long page){
		
			/*List<CsConsigneeInfo> ccilist=workorderdao.queryAllCsConsigneeInfo(page,cci.getName(),cci.getPhoneonOne(),cci.getConsigneeType());*/
			model.addAttribute("page", page);
			System.out.println(cci.getConsigneeType());
			/*if(cci.getName()!=null&&cci.getName().length()>0||cci.getPhoneonOne()!=null&&cci.getPhoneonOne().length()>0||cci.getConsigneeType()>=0){*/
			model.addAttribute("page_obj", new Page(workorderdao.getCsConsigneeInfocount(cci.getName(),cci.getPhoneonOne(),cci.getConsigneeType()), page, Page.ONE_PAGE_NUMBER));			
			model.addAttribute("ccilist", workorderdao.queryAllCsConsigneeInfo(page,cci.getName(),cci.getPhoneonOne(),cci.getConsigneeType()));
			/*}*/
		return "workorder/CallerArchivalRepository";
	}
	/*@RequestMapping("/QueryCallerInfo/{page}")
	public String QueryCallerInfo(Model model,CsConsigneeInfo cci,@PathVariable(value="page") long page){
		if(cci!=null){
			List<CsConsigneeInfo> ccilist=workorderdao.queryAllCsConsigneeInfo(page,cci.getName(),cci.getPhoneonOne(),cci.getConsigneeType());
			model.addAttribute("page_obj", new Page(workorderdao.getCsConsigneeInfocount(cci.getName(),cci.getPhoneonOne(),cci.getConsigneeType()), page, Page.ONE_PAGE_NUMBER));
			model.addAttribute("page", page);
			model.addAttribute("ccilist", ccilist);
		}
			return "workorder/CallerArchivalRepository";
	}*/
	
	@RequestMapping("/EditEditMaintain/{id}")
	public String EditEditMaintain(Model model,@PathVariable(value="id") int id) throws Exception{
		System.out.println(id);
		CsConsigneeInfo ccf=workorderdao.queryById(id);
		model.addAttribute("ccf", ccf);
		
		return "workorder/EditMaintain";		
	}
	
	@RequestMapping("/editCallerArchival")
	@ResponseBody
	public String editCallerArchival(CsConsigneeInfo ccf){
		workorderdao.editAllCsConsigneeInfo(ccf);
		return "{\"errorCode\":0,\"error\":\"修改成功\"}";
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
		CwbOrder cl=cwbdao.getCwbOrderByOpscwbid(opscwbid);
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
		CwbOrder cl=cwbdao.getCwbOrderByOpscwbid(opscwbid);
		CsComplaintAccept ca = new CsComplaintAccept();
			ca.setProvence(cl.getCwbprovince());
			ca.setPhoneOne(cl.getConsigneemobile());
			ca.setAcceptNo(transcwb); //存入工单
			ca.setOrderNo(cl.getCwb());//存入订单
			ca.setCwbstate(cl.getCwbstate());//存入订单状态
			ca.setFlowordertype(cl.getFlowordertype());//存入订单操作状态
			ca.setCustomerid(cl.getCustomerid());
			System.out.println(cl.getCurrentbranchid());
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
		System.out.println(acceptNo+"`~11`");
		CwbOrder co=cwbdao.getOneCwbOrderByCwb(cca.getOrderNo());
		CsConsigneeInfo cci=workorderdao.queryByPhoneNum(cca.getPhoneOne());		
		String nowtime =DateTimeUtil.getNowTime();
		String uname=getSessionUser().getRealname();
		cca.setHeshiUser(uname);
		cca.setHeshiTime(nowtime);
		List<Branch> lb=branchdao.getAllBranches();	
		model.addAttribute("OneLevel", reasondao.getReasonByReasonid(cca.getComplaintOneLevel()).getReasoncontent());
		model.addAttribute("TwoLevel", reasondao.getReasonByReasonid(cca.getComplaintTwoLevel()).getReasoncontent());
		model.addAttribute("lb", lb);		
		model.addAttribute("cci", cci);
		model.addAttribute("cca", cca);		//存入工单信息
		model.addAttribute("co", co);    //存入订单信息表
		return "workorder/OrgVerify";
	}
	@RequestMapping("/CustomerServiceAdjudicate")
	public String CustomerServiceAdjudicate(Model model,@RequestParam(value="acceptNo",defaultValue="",required=true) String acceptNo) {     //客服结案
		CsComplaintAccept cca=workorderdao.findGoOnacceptWOByWorkOrder(acceptNo);		
		CwbOrder co=cwbdao.getOneCwbOrderByCwb(cca.getOrderNo());
		String nowtime =DateTimeUtil.getNowTime();
		String uname=getSessionUser().getRealname();
		cca.setJieanUser(uname);
		cca.setJieanTime(nowtime);
		CsConsigneeInfo cci=workorderdao.queryByPhoneNum(cca.getPhoneOne())==null?null:workorderdao.queryByPhoneNum(cca.getPhoneOne());
		List<Branch> lb=branchdao.getAllBranches();	
		model.addAttribute("OneLevel", reasondao.getReasonByReasonid(cca.getComplaintOneLevel()).getReasoncontent());
		model.addAttribute("TwoLevel", reasondao.getReasonByReasonid(cca.getComplaintTwoLevel()).getReasoncontent());
		model.addAttribute("lb", lb);		
		model.addAttribute("cci", cci);
		model.addAttribute("cca", cca);		//存入工单信息
		model.addAttribute("co", co);    //存入订单信息表
		return "workorder/CustomerServiceAdjudicate";
	}
	@RequestMapping("/OrgAppeal")
	public String OrgAppeal(Model model,@RequestParam(value="acceptNo",defaultValue="",required=true) String acceptNo){
		CsComplaintAccept cca=workorderdao.findGoOnacceptWOByWorkOrder(acceptNo);
		CwbOrder co=cwbdao.getOneCwbOrderByCwb(cca.getOrderNo());
		
		CsConsigneeInfo cci=workorderdao.queryByPhoneNum(cca.getPhoneOne())==null?null:workorderdao.queryByPhoneNum(cca.getPhoneOne());
		List<Branch> lb=branchdao.getAllBranches();
		String nowtime =DateTimeUtil.getNowTime();
		String uname=getSessionUser().getRealname();
		cca.setShensuUser(uname);
		cca.setComplaintTime(nowtime);
		model.addAttribute("OneLevel", reasondao.getReasonByReasonid(cca.getComplaintOneLevel()).getReasoncontent());
		model.addAttribute("TwoLevel", reasondao.getReasonByReasonid(cca.getComplaintTwoLevel()).getReasoncontent());
		
		model.addAttribute("lb", lb);   
		model.addAttribute("cci", cci);
		model.addAttribute("cca", cca);		//存入工单信息
		model.addAttribute("co", co);    //存入订单信息表
		return "workorder/OrgAppeal";
	}
	@RequestMapping("/AdjudicateRetrial")
	public String AdjudicateRetrial(Model model,@RequestParam(value="acceptNo",defaultValue="",required=true) String acceptNo){
		CsComplaintAccept cca=workorderdao.findGoOnacceptWOByWorkOrder(acceptNo);
		CwbOrder co=cwbdao.getOneCwbOrderByCwb(cca.getOrderNo());
		CsConsigneeInfo cci=workorderdao.queryByPhoneNum(cca.getPhoneOne())==null?null:workorderdao.queryByPhoneNum(cca.getPhoneOne());
		List<Branch> lb=branchdao.getAllBranches();
		String nowtime =DateTimeUtil.getNowTime();
		String uname=getSessionUser().getRealname();
		cca.setChongshenUser(uname);
		cca.setJieanchongshenTime(nowtime);
		model.addAttribute("OneLevel", reasondao.getReasonByReasonid(cca.getComplaintOneLevel()).getReasoncontent());
		model.addAttribute("TwoLevel", reasondao.getReasonByReasonid(cca.getComplaintTwoLevel()).getReasoncontent());
		
		model.addAttribute("lb", lb);   
		model.addAttribute("cci", cci);
		model.addAttribute("cca", cca);		//存入工单信息
		model.addAttribute("co", co);    //存入订单信息表
		return "workorder/AdjudicateRetrial";
	}
	//添加客服收件人数据
	@RequestMapping("/addCsConsigneeInfo")
	public String add(CsConsigneeInfo cci){
		List<CsConsigneeInfo> cs=workorderdao.queryListCsConsigneeInfo(cci.getPhoneonOne());
		
		if(cs.size()<=1){
			workorderservice.addcsconsigneeInfo(cci);
		}
			
		
		return "workorder/CustomerServicesAcceptWorkOrder";	
	}
	
	@RequestMapping("/selectByPhoneNum")
	@ResponseBody
	public CsConsigneeInfo selectByPhoneNum(HttpServletRequest req){
		String phone=req.getParameter("phoneonOne");		
		CsConsigneeInfo cci=workorderservice.querycciByPhoneNum(phone);
		
		return cci;		
	}
	
	@RequestMapping("/SelectdetalForm")
	@ResponseBody
	public  List<CwbOrderAndCustomname> SelectdetalForm(HttpServletRequest req){
		String phone=req.getParameter("phoneonOne");
		List<CwbOrder> cwborderlist=workorderservice.SelectCwbdetalForm(phone)==null?null:workorderservice.SelectCwbdetalForm(phone);
		List<CwbOrderAndCustomname> lc= new ArrayList<CwbOrderAndCustomname>();
		for(CwbOrder c:cwborderlist){
			CwbOrderAndCustomname co = new CwbOrderAndCustomname();
				co.setId(c.getOpscwbid());
				co.setCwb(c.getCwb());
				co.setTranscwb(c.getTranscwb());//
				co.setCustomername(customerDAO.findcustomername(c.getCustomerid()).getCustomername());//express_set_customer_info
				co.setEmaildate(c.getEmaildate());
				co.setConsigneename(c.getConsigneename());
				co.setConsigneeaddress(c.getConsigneeaddress());
				co.setConsigneemobile(c.getConsigneemobile());
				co.setCwbstate(CwbStateEnum.getByValue(c.getCwbstate()).getText());
				co.setCustomerid(c.getCustomerid());
				lc.add(co);				
		}		
				
		return lc;
	}
	
	@RequestMapping("/selectDetalFormByCondition")
	@ResponseBody
	public List<CwbOrderAndCustomname> selectDetalFormByCondition(CwbOrderAndCustomname coc,Model model){
	
		String cwb1 = cos.translateCwb(coc.getCwb());
		coc.setCwb(cwb1);
		List<CwbOrder> lc=cwbdao.SelectDetalFormByCondition(coc)==null?null:cwbdao.SelectDetalFormByCondition(coc);
	
		List<CwbOrderAndCustomname> lco= new ArrayList<CwbOrderAndCustomname>();
		//System.out.println(lc.get(0).getCwb());
		for(CwbOrder c:lc){
			CwbOrderAndCustomname co = new CwbOrderAndCustomname();
				co.setId((c.getOpscwbid()));
				co.setCwb(c.getCwb());
				co.setTranscwb(c.getTranscwb());
				co.setCustomername(customerDAO.findcustomername(c.getCustomerid()).getCustomername());
				co.setEmaildate(c.getEmaildate());
				co.setConsigneename(c.getConsigneename());
				co.setConsigneeaddress(c.getConsigneeaddress());
				co.setConsigneemobile(c.getConsigneemobile());
				co.setCwbstate(CwbStateEnum.getByValue(c.getCwbstate()).getText());
				lco.add(co);				
		}
		
		return lco;
	}
	

	
	@RequestMapping("/saveWorkOrderQueryF")
	@ResponseBody
	public String saveWorkOrderQueryForm(CsComplaintAccept cca){
		//System.out.println(cca.getComplaintState());
		String accepttime=DateTimeUtil.formatDate(new Date());
		cca.setAcceptTime(accepttime);		
		CwbOrder co=cwbdao.getCwbByCwb(cca.getOrderNo());
		cca.setCustomerid(co.getCustomerid());
		String username=getSessionUser().getRealname();
		cca.setHandleUser(username);
		workorderdao.savequeryworkorderForm(cca);
	
		return "{\"errorCode\":0,\"error\":\"保存成功\"}";
		
	}
	
	
	@RequestMapping("/updateWorkOrderQueryF")
	@ResponseBody
	public String updateWorkOrderQueryF(CsComplaintAccept cca){	
		workorderdao.ChangeGoOnAcceptcomplaintState(cca);	
		return "{\"errorCode\":0,\"error\":\"处理成功\"}";
		
	}
	
	@RequestMapping("/saveComplainWorkOrderF")
	@ResponseBody
	public String saveComplainWorkOrderF(CsComplaintAccept cca){
		//System.out.println(cca.getComplaintState());
		String accepttime=DateTimeUtil.formatDate(new Date());
		cca.setAcceptTime(accepttime);
		CwbOrder co=cwbdao.getCwbByCwb(cca.getOrderNo());
		cca.setCustomerid(co.getCustomerid());
		String username=getSessionUser().getRealname();
		cca.setHandleUser(username);
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
	public List<CsComplaintAcceptVO> findGoOnAcceptWO(Model model,HttpServletRequest req){
		String phone=req.getParameter("phoneonOne");
		//System.out.println(phone+"~~");
		List<CsComplaintAccept> lcs=workorderdao.findGoOnacceptWO(phone);
		List<CsComplaintAcceptVO> lc=new ArrayList<CsComplaintAcceptVO>();
		for(CsComplaintAccept c:lcs){
			CsComplaintAcceptVO ca = new CsComplaintAcceptVO();
			ca.setAcceptNo(c.getAcceptNo());
			ca.setOrderNo(c.getOrderNo());
			ca.setPhoneOne(c.getPhoneOne());
			ca.setAcceptTime(c.getAcceptTime());
			ca.setComplaintType(c.getComplaintType());
			ca.setContent(c.getContent());
			ca.setComplaintState(c.getComplaintState());
			ca.setProvence(c.getProvence());
			ca.setShowcomplaintTypeName(ComplaintTypeEnum.getByValue(c.getComplaintType()).getText());
			ca.setShowComplaintStateName(ComplaintStateEnum.getByValue(c.getComplaintState()).getText());
			ca.setComplaintUser(c.getComplaintUser());
			ca.setQueryContent(c.getQueryContent());
			lc.add(ca);
		}
		
		return lc;

	}
	
	@RequestMapping("/findGoOnAcceptWOByCWB") 
	@ResponseBody
	public List<CsComplaintAcceptVO> findGoOnAcceptWOByCWB(Model model,HttpServletRequest req){
		String cwb=req.getParameter("cwb");
		List<CsComplaintAccept> lcs=workorderdao.findGoOnacceptWOByCWB(cwb);
		List<CsComplaintAcceptVO> lc=new ArrayList<CsComplaintAcceptVO>();
		for(CsComplaintAccept c:lcs){
			CsComplaintAcceptVO ca = new CsComplaintAcceptVO();
			ca.setAcceptNo(c.getAcceptNo());
			ca.setOrderNo(c.getOrderNo());
			ca.setPhoneOne(c.getPhoneOne());
		/*	ca.setCity(cwbdao.getCwbByCwb(c.getOrderNo()).getCwbprovince());*/
			ca.setProvence(c.getProvence());
			ca.setAcceptTime(c.getAcceptTime());
			ca.setComplaintType(c.getComplaintType());
			ca.setComplaintState(c.getComplaintState());
			ca.setShowcomplaintTypeName(ComplaintTypeEnum.getByValue(c.getComplaintType()).getText());
			ca.setContent(c.getContent());
			ca.setShowComplaintStateName(ComplaintStateEnum.getByValue(c.getComplaintState()).getText());
			ca.setComplaintUser(c.getComplaintUser());
			ca.setQueryContent(c.getQueryContent());
			lc.add(ca);
		}
		
		return lc;

	}
	
	@RequestMapping("/GoOnacceptWo")
	public String GoOnacceptWo(HttpServletRequest req,Model model){
		String workorder=req.getParameter("workorder");
		CsComplaintAccept lcs=null;
		String RuKuTime=null;
		if(workorder!=null){
			lcs=workorderdao.findGoOnacceptWOByWorkOrder(workorder);	
		}
			String cwb=lcs.getOrderNo();  //获取订单号
			CwbOrder lco=cwbdao.getOneCwbOrderByCwb(cwb);
			String conMobile=lco.getConsigneemobile();
			OrderFlow lof=orderflowdao.getRuKuTimeByCwb(cwb)==null?null:orderflowdao.getRuKuTimeByCwb(cwb);	
			if(lof!=null){
			Date createTime=lof.getCredate();
			SimpleDateFormat dateformat1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			RuKuTime=dateformat1.format(createTime); //获取入库时间
			}
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
			model.addAttribute("RuKuTime",RuKuTime);
			model.addAttribute("lb", lb);
			
			return "workorder/CreateGoOnAcceptWorkOrder";			
	}
	
	
	
	
	@RequestMapping("/GoOnacceptWoQuery")
	public String GoOnacceptWoQuery(HttpServletRequest req,Model model){
		String workorder=req.getParameter("workorder");
		CsComplaintAccept lcs=workorderdao.findGoOnacceptWOByWorkOrder(workorder);
		model.addAttribute("lcs", lcs);
			return "workorder/CreateGoonAcceptQueryWorkOrder";			
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
	
		String cwb[]=cv.getOrderNo().trim().split("\r\n");
		StringBuffer sb = new StringBuffer();
		for(String str:cwb){
			sb=sb.append("'"+str+"',");
		}
		String ncwbs=sb.substring(0, sb.length()-1);		
	List<CsComplaintAccept> lcs=workorderdao.findGoOnacceptWOByCWBs(ncwbs,cv);		
		List<CsComplaintAccept> lc=new ArrayList<CsComplaintAccept>();
		Map<String,String> connameList=new HashMap<String, String>();
		Map<Long,String> customerList=new HashMap<Long, String>();
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
			ca.setCustomerid(c.getCustomerid());
			ca.setHandleUser(c.getHandleUser());
			ca.setJieanTime(c.getJieanTime());
			lc.add(ca);			
			String cname=workorderdao.queryByPhoneone(c.getPhoneOne())==null?"":workorderdao.queryByPhoneone(c.getPhoneOne()).getName();
			connameList.put(c.getPhoneOne(), cname);
			String customerName=customerDAO.findcustomername(c.getCustomerid()).getCustomername();
			customerList.put(c.getCustomerid(),customerName);			
	}
		
		
		List<CwbOrder> co=cwbdao.getCwbByCwbs(ncwbs);
		/*String username=getSessionUser().getRealname();*/
		List<Branch> lb=branchdao.getAllBranches();
		List<Reason> lr=reasondao.addWO();
		List<Reason> lrs=null;
		for(Reason r:lr){
			 lrs=reasondao.getSecondLevelReason(r.getReasonid());
		}
		
		
		model.addAttribute("shensuendTime", Integer.valueOf(systeminstalldao.getSystemInstallByName("shensuendTime").getValue()));
		model.addAttribute("customernameList", customerList);
		model.addAttribute("lr", lr==null?null:lr);
		model.addAttribute("lrs", lrs==null?null:lrs);
	
		model.addAttribute("lb", lb==null?null:lb); 
		model.addAttribute("lc", lc==null?null:lc);
		/*model.addAttribute("username", username==null?null:username);*/
		model.addAttribute("connameList", connameList);
		model.addAttribute("co", co==null?null:co);
		
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
	
	@RequestMapping("/ChangeComplaintState")
	@ResponseBody
	public String ChangeComplaintState(CsComplaintAccept cca){
		workorderdao.ChangecomplaintState(cca);
		
		return "{\"errorCode\":0,\"error\":\"工单处理成功\"}";
	}
	
	@RequestMapping("/HeshiChangeComplaintStateFile")
	@ResponseBody
	public String HeshiChangeComplaintStateFile(HttpServletRequest req,@RequestParam(value = "Filedata", required = false) MultipartFile file){
		String heshiremark=StringUtil.nullConvertToEmptyString(req.getParameter("remark"));
		String vid= StringUtil.nullConvertToEmptyString(req.getParameter("id"));
		String AlreadyVerifycomplaintState=StringUtil.nullConvertToEmptyString(req.getParameter("complaintState"));
		String downloadname=workorderservice.loadexceptfile(file);
		CsComplaintAccept cca = new CsComplaintAccept();
		cca.setDownloadheshipath(downloadname);
		cca.setRemark(heshiremark);
		cca.setId(Integer.valueOf(vid));
		//添加个字段保存文件名字
		
		cca.setComplaintState(Integer.valueOf(AlreadyVerifycomplaintState));
		User user=this.getSessionUser();
		cca.setHeshiUser(user.getRealname());
		cca.setHeshiTime(DateTimeUtil.getNowTime());
		workorderdao.ChangecomplaintState(cca);
		
		return "{\"errorCode\":0,\"error\":\"核实成功\"}";	
	}
	@RequestMapping("/HeshiChangeComplaintState")
	@ResponseBody
	public String HeshiChangeComplaintState(HttpServletRequest req){
		String heshiremark=StringUtil.nullConvertToEmptyString(req.getParameter("remark"));
		String vid= StringUtil.nullConvertToEmptyString(req.getParameter("id"));
		String AlreadyVerifycomplaintState=StringUtil.nullConvertToEmptyString(req.getParameter("complaintState"));
		CsComplaintAccept cca = new CsComplaintAccept();
		cca.setRemark(heshiremark);
		cca.setId(Integer.valueOf(vid));
		cca.setComplaintState(Integer.valueOf(AlreadyVerifycomplaintState));
		cca.setHeshiUser(getSessionUser().getRealname());
		cca.setHeshiTime(DateTimeUtil.getNowTime());
		workorderdao.ChangecomplaintState(cca);
		
		return "{\"errorCode\":0,\"error\":\"核实成功\"}";	
	}
	
	
	
	@RequestMapping("/WorkorderDetail/{acceptNo}")
	public String Workorderdetail(Model model,@PathVariable(value="acceptNo") String acceptNo){
		CsComplaintAccept cca=workorderdao.findGoOnacceptWOByWorkOrder(acceptNo);
		System.out.println(acceptNo);
		CwbOrder co=cwbdao.getOneCwbOrderByCwb(cca.getOrderNo());
		CsConsigneeInfo cci=workorderdao.queryByPhoneNum(cca.getPhoneOne())==null?null:workorderdao.queryByPhoneNum(cca.getPhoneOne());
		List<Branch> lb=branchdao.getAllBranches();
		String nowtime =DateTimeUtil.getNowTime();
		String uname=getSessionUser().getRealname();
		cca.setHandleUser(uname);
		cca.setJieanchongshenTime(nowtime);
		model.addAttribute("OneLevel", reasondao.getReasonByReasonid(cca.getComplaintOneLevel()).getReasoncontent());
		model.addAttribute("TwoLevel", reasondao.getReasonByReasonid(cca.getComplaintTwoLevel()).getReasoncontent());
		
		model.addAttribute("lb", lb);   
		model.addAttribute("cci", cci);
		model.addAttribute("cca", cca);		//存入工单信息
		model.addAttribute("co", co);    //存入订单信息表
		return "workorder/WorkorderDetails";
	}
	
	
	@RequestMapping("/exportExcle")
	public void exportExcle(Model model, HttpServletResponse response, HttpServletRequest request) {

		String[] cloumnName1 = new String[10]; // 导出的列名
		String[] cloumnName2 = new String[10]; // 导出的英文列名
		
		es.setcsconsigneeinfo(cloumnName1,cloumnName2);
		final String[] cloumnName = cloumnName1;
		final String[] cloumnName3 = cloumnName2;
		String sheetName = "来电人信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Csconsigneeinfo_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
		String name=request.getParameter("name");
		String phone=request.getParameter("phoneonOne");
		String consigneeType =request.getParameter("consigneeType");
		int  consigneeTypeValue=Integer.valueOf(consigneeType);
		 List<CsConsigneeInfo> ccilist1=workorderdao.queryAllCsConsigneeInfo1(name,phone,consigneeTypeValue);
	final List<CsConsigneeInfoVO> ccilist=new ArrayList<CsConsigneeInfoVO>();
		for(CsConsigneeInfo c:ccilist1){
			CsConsigneeInfoVO cv = new CsConsigneeInfoVO();
			cv.setName(c.getName());
			cv.setSex(c.getSex()==1?"男":"女");
			cv.setPhoneonOne(c.getPhoneonOne());
			cv.setPhoneonTwo(c.getPhoneonTwo());
			cv.setMailBox(c.getMailBox());
			cv.setProvince(c.getProvince());
			cv.setCity(c.getCity());
			cv.setConsigneeType(c.getConsigneeType()==1?"普通客户":"VIP客户");
			cv.setContactLastTime(c.getContactLastTime());
			cv.setContactNum(c.getContactNum());
			ccilist.add(cv);
			
		}
		ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
			@Override
			public void fillData(Sheet sheet, CellStyle style) {
				for (int k = 0; k < ccilist.size(); k++) {
					Row row = sheet.createRow(k + 1);
					row.setHeightInPoints(15);
					for (int i = 0; i < cloumnName.length; i++) {
						Cell cell = row.createCell((short) i);
						cell.setCellStyle(style);
						Object a = null;
						// 给导出excel赋值
						a = es.setCSInfoObject(cloumnName3,ccilist,a, i, k);
						cell.setCellValue(a == null ? "" : a.toString());
					}
				}
			}
		};
		
		excelUtil.excel(response, cloumnName, sheetName, fileName);
	} catch (Exception e) {
		e.printStackTrace();
	}
		
		
	}
	
	@RequestMapping("/exportWorkOrderExcle")
	public void exportWorkOrderExcle(Model model, HttpServletResponse response, HttpServletRequest request,CsComplaintAccept cca) {

		String[] cloumnName1 = new String[15]; // 导出的列名
		String[] cloumnName2 = new String[15]; // 导出的英文列名
		es.setCsComplaintAcceptVO(cloumnName1,cloumnName2);
		final String[] cloumnName = cloumnName1;
		final String[] cloumnName3 = cloumnName2;
		String sheetName = "工单信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "CsComplaintAccept_" + df.format(new Date()) + ".xlsx"; // 文件名
	 try{	
		String cwbs=request.getParameter("orderNo");
		String complaintType = request.getParameter("complaintType")==null?"-1":request.getParameter("complaintType");
		String orderNo = request.getParameter("orderNo")==null?"":request.getParameter("orderNo");
		String complaintState = request.getParameter("complaintState")==null?"-1":request.getParameter("complaintState");
		String complaintOneLevel = request.getParameter("complaintOneLevel")==null?"-1":request.getParameter("complaintOneLevel");
		String codOrgId = request.getParameter("codOrgId")==null?"-1":request.getParameter("codOrgId");
		String complaintResult = request.getParameter("complaintResult")==null?"-1":request.getParameter("complaintResult");
		String complaintTwoLevel = request.getParameter("complaintTwoLevel")==null?"-1":request.getParameter("complaintTwoLevel");
		String beginRangeTime = request.getParameter("beginRangeTime")==null?"":request.getParameter("beginRangeTime");
		String endRangeTime = request.getParameter("endRangeTime")==null?"":request.getParameter("endRangeTime");
		/*String handleUser = request.getParameter("handleUser")==null?"":request.getParameter("handleUser");
		String ifpunish = request.getParameter("ifpunish")==null?"-1":request.getParameter("ifpunish");*/
		CsComplaintAcceptVO cc = new CsComplaintAcceptVO();
		cc.setComplaintType(Integer.valueOf(complaintType));
		cc.setOrderNo(orderNo);
		cc.setComplaintState(Integer.valueOf(complaintState));
		cc.setComplaintOneLevel(Integer.valueOf(complaintOneLevel));
		cc.setCodOrgId(Integer.valueOf(codOrgId));
		cc.setComplaintResult(Integer.valueOf(complaintResult));
		cc.setComplaintTwoLevel(Integer.valueOf(complaintTwoLevel));
		cc.setBeginTime(beginRangeTime);
		cc.setEndTime(endRangeTime);
		
		/**
		 * 下面是查询
		 */
		String cwb[]=cwbs.trim().split("\r\n");
		StringBuffer sb = new StringBuffer();
		for(String str:cwb){
			sb=sb.append("'"+str+"',");
		}
		String ncwbs=sb.substring(0, sb.length()-1);		
	List<CsComplaintAccept> lcs=workorderdao.findGoOnacceptWOByCWBs(ncwbs,cc);		
	System.out.println(lcs.get(0).getComplaintOneLevel());
	final List<CsComplaintAcceptExportVO> lc=new ArrayList<CsComplaintAcceptExportVO>();
		for(CsComplaintAccept c:lcs){
			CsComplaintAcceptExportVO ca = new CsComplaintAcceptExportVO();
			ca.setAcceptNo(c.getAcceptNo());
			ca.setOrderNo(c.getOrderNo());
			ca.setComplaintType(ComplaintTypeEnum.getByValue(c.getComplaintType()).getText());
			ca.setComplaintState(ComplaintStateEnum.getByValue(c.getComplaintState()).getText());
			ca.setCodOrgId(branchdao.getBranchByBranchid(c.getCodOrgId()).getBranchname());
			if(c.getComplaintOneLevel()!=0){
			ca.setComplaintOneLevel(reasondao.getReasonByReasonid(c.getComplaintOneLevel()).getReasoncontent());
			}else{
				ca.setComplaintOneLevel("");
			}
			
			if(c.getComplaintTwoLevel()!=0){
				ca.setComplaintTwoLevel(reasondao.getReasonByReasonid(c.getComplaintTwoLevel()).getReasoncontent());
			}else{
				ca.setComplaintTwoLevel("");
			}			
			ca.setComplaintResult(ComplaintResultEnum.getByValue(c.getComplaintResult()).getText());
			ca.setAcceptTime(c.getAcceptTime());
			ca.setPhoneOne(c.getPhoneOne());	
			ca.setCustomername(customerDAO.findcustomername(c.getCustomerid()).getCustomername()==null?"":customerDAO.findcustomername(c.getCustomerid()).getCustomername());
			ca.setName(workorderdao.queryByPhoneone(c.getPhoneOne()).getName());
			ca.setIfpunish(1);
			ca.setCuijianNum(10);
			ca.setHandleUser(c.getHandleUser());
			lc.add(ca);				
		}
		
		ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
			@Override
			public void fillData(Sheet sheet, CellStyle style) {
				for (int k = 0; k < lc.size(); k++) {
					Row row = sheet.createRow(k + 1);
					row.setHeightInPoints(15);
					for (int i = 0; i < cloumnName.length; i++) {
						Cell cell = row.createCell((short) i);
						cell.setCellStyle(style);
						Object a = null;
						// 给导出excel赋值
						a = es.setCsComplaintAcceptExportVO(cloumnName3,lc,a, i, k);
						cell.setCellValue(a == null ? "" : a.toString());
					}
				}
			}
		};
		
		excelUtil.excel(response, cloumnName, sheetName, fileName);
	} catch (Exception e) {
		e.printStackTrace();
	}
		
	}	
	
	
	
	@RequestMapping("/JieAnChangeComplaintStateFile")
	@ResponseBody
	public String JieAnChangeComplaintStateFile(HttpServletRequest req,
		@RequestParam(value = "Filedata", required = false) MultipartFile file){		
		String jieanremark=StringUtil.nullConvertToEmptyString(req.getParameter("jieanremark"));
		String jid= StringUtil.nullConvertToEmptyString(req.getParameter("id"));
		String JieAnChangeComplaintState=StringUtil.nullConvertToEmptyString(req.getParameter("complaintState"));
		String downloadname=workorderservice.loadexceptfile(file);
		CsComplaintAccept cca = new CsComplaintAccept();
		cca.setDownloadjieanpath(downloadname);
		cca.setJieanremark(jieanremark);
		cca.setId(Integer.valueOf(jid));
		cca.setComplaintState(Integer.valueOf(JieAnChangeComplaintState));
		cca.setJieanUser(getSessionUser().getRealname());
		cca.setJieanTime(DateTimeUtil.getNowTime());
		workorderdao.ChangecomplaintState(cca);
		
		return "{\"errorCode\":0,\"error\":\"结案处理成功\"}";	
	}
	@RequestMapping("/JieAnChangeComplaintState")
	@ResponseBody
	public String JieAnChangeComplaintState(HttpServletRequest req){
		String jieanremark=StringUtil.nullConvertToEmptyString(req.getParameter("jieanremark"));
		String jid= StringUtil.nullConvertToEmptyString(req.getParameter("id"));
		String JieAnChangeComplaintState=StringUtil.nullConvertToEmptyString(req.getParameter("complaintState"));
		CsComplaintAccept cca = new CsComplaintAccept();
		cca.setJieanremark(jieanremark);
		cca.setId(Integer.valueOf(jid));
		cca.setComplaintState(Integer.valueOf(JieAnChangeComplaintState));
		cca.setJieanUser(getSessionUser().getRealname());
		cca.setJieanTime(DateTimeUtil.getNowTime());
		workorderdao.ChangecomplaintState(cca);
		
		return "{\"errorCode\":0,\"error\":\"结案处理成功\"}";
	}
	
	
	@RequestMapping("/ShenSuChangeComplaintStateFile")
	@ResponseBody
	public String ShenSuChangeComplaintStateFile(HttpServletRequest req,
		@RequestParam(value = "Filedata", required = false) MultipartFile file){		
		String shensuremark=StringUtil.nullConvertToEmptyString(req.getParameter("shensuremark"));
		String jid= StringUtil.nullConvertToEmptyString(req.getParameter("id"));
		String shensuChangeComplaintState=StringUtil.nullConvertToEmptyString(req.getParameter("complaintState"));
		String downloadname=workorderservice.loadexceptfile(file);
		CsComplaintAccept cca = new CsComplaintAccept();
		cca.setDownloadshensupath(downloadname);
		cca.setShensuremark(shensuremark);
		cca.setId(Integer.valueOf(jid));
		cca.setComplaintState(Integer.valueOf(shensuChangeComplaintState));
		cca.setShensuUser(getSessionUser().getRealname());
		cca.setComplaintTime(DateTimeUtil.getNowTime());
		workorderdao.ChangecomplaintState(cca);
		
		return "{\"errorCode\":0,\"error\":\"申诉处理成功\"}";	
	}
	@RequestMapping("/ShenSuChangeComplaintState")
	@ResponseBody
	public String ShenSuChangeComplaintState(HttpServletRequest req){
		String shensuremark=StringUtil.nullConvertToEmptyString(req.getParameter("shensuremark"));
		String jid= StringUtil.nullConvertToEmptyString(req.getParameter("id"));
		String shensuChangeComplaintState=StringUtil.nullConvertToEmptyString(req.getParameter("complaintState"));
		CsComplaintAccept cca = new CsComplaintAccept();
		cca.setShensuremark(shensuremark);
		cca.setId(Integer.valueOf(jid));
		cca.setComplaintState(Integer.valueOf(shensuChangeComplaintState));
		cca.setShensuUser(getSessionUser().getRealname());
		cca.setComplaintTime(DateTimeUtil.getNowTime());
		workorderdao.ChangecomplaintState(cca);
		
		return "{\"errorCode\":0,\"error\":\"申诉处理成功\"}";	
	}
	
	@RequestMapping("/JieAnChongShenChangeComplaintStateFile")
	@ResponseBody
	public String JieAnChongShenChangeComplaintStateFile(HttpServletRequest req,
		@RequestParam(value = "Filedata", required = false) MultipartFile file){		
		String jieanchongshenremark=StringUtil.nullConvertToEmptyString(req.getParameter("jieanchongshenremark"));
		String jid= StringUtil.nullConvertToEmptyString(req.getParameter("id"));
		String JieAnChongShenChangeComplaintState=StringUtil.nullConvertToEmptyString(req.getParameter("complaintState"));
		/*String downloadjieanpath =workorderdao.getCsComplaintAccept(Integer.valueOf(jid)).getDownloadjieanpath();*/
		String downloadname=workorderservice.loadexceptfile(file);
		CsComplaintAccept cca = new CsComplaintAccept();
		/*StringBuilder sb = new StringBuilder();
		if(downloadjieanpath==null){
			cca.setDownloadchongshenpath(downloadname);
		}else{
			sb.append(downloadjieanpath).append(",").append(downloadname);
			cca.setDownloadchongshenpath(sb.toString());
		}*/
		cca.setDownloadchongshenpath(downloadname);
		cca.setJieanchongshenremark(jieanchongshenremark);
		cca.setId(Integer.valueOf(jid));
		cca.setComplaintState(Integer.valueOf(JieAnChongShenChangeComplaintState));
		cca.setChongshenUser(getSessionUser().getRealname());
		cca.setJieanchongshenTime(DateTimeUtil.getNowTime());
		workorderdao.ChangecomplaintState(cca);
		
		return "{\"errorCode\":0,\"error\":\"结案重审处理成功\"}";	
	}
	@RequestMapping("/JieAnChongShenChangeComplaintState")
	@ResponseBody
	public String JieAnChongShenChangeComplaintState(HttpServletRequest req){
		String jieanchongshenremark=StringUtil.nullConvertToEmptyString(req.getParameter("jieanchongshenremark"));
		String jid= StringUtil.nullConvertToEmptyString(req.getParameter("id"));
		String JieAnChongShenChangeComplaintState=StringUtil.nullConvertToEmptyString(req.getParameter("complaintState"));
		CsComplaintAccept cca = new CsComplaintAccept();
		
		cca.setJieanchongshenremark(jieanchongshenremark);
		cca.setId(Integer.valueOf(jid));
		cca.setComplaintState(Integer.valueOf(JieAnChongShenChangeComplaintState));
		cca.setChongshenUser(getSessionUser().getRealname());
		cca.setJieanchongshenTime(DateTimeUtil.getNowTime());
		workorderdao.ChangecomplaintState(cca);
		
		return "{\"errorCode\":0,\"error\":\"结案重审处理成功\"}";	
	}
	@RequestMapping("/download")
	public void filedownload(HttpServletRequest request,HttpServletResponse response){
		try {
			String filePath=request.getParameter("filepathurl");
			String filePathaddress=ResourceBundleUtil.EXCEPTPATH+filePath;
			File file=new File(filePathaddress);
			 
 
			// 取得文件名。
			String filename = file.getName();
			// 以流的形式下载文件。
			InputStream fis = new BufferedInputStream(new FileInputStream(filePathaddress));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();
			// 设置response的Header
			response.setContentType("application/ms-excel");
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
			response.addHeader("Content-Length", "" + file.length());
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}

	
	
	
	
	
	
}
