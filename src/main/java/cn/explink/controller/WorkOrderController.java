package cn.explink.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CsPushSmsDao;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.SmsConfigDAO;
import cn.explink.dao.SmsManageDao;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.UserDAO;
import cn.explink.dao.WorkOrderDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.CsComplaintAccept;
import cn.explink.domain.CsComplaintAcceptExportVO;
import cn.explink.domain.CsComplaintAcceptVO;
import cn.explink.domain.CsComplaintAcceptViewVo;
import cn.explink.domain.CsConsigneeInfo;
import cn.explink.domain.CsConsigneeInfoVO;
import cn.explink.domain.CsShenSuChat;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.CwbOrderAndCustomname;
import cn.explink.domain.Reason;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.User;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.entity.CsPushSms;
import cn.explink.enumutil.ComplaintResultEnum;
import cn.explink.enumutil.ComplaintStateEnum;
import cn.explink.enumutil.CwbStateEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.service.CwbOrderService;
import cn.explink.service.Excel2003Extractor;
import cn.explink.service.Excel2007Extractor;
import cn.explink.service.ExcelExtractor;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.service.SmsSendService;
import cn.explink.service.WorkOrderService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.ExcelUtils;
import cn.explink.util.HttpUtil;
import cn.explink.util.JsonUtil;
import cn.explink.util.Page;
import cn.explink.util.ResourceBundleUtil;
import cn.explink.util.SecurityUtil;
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
	private UserDAO userDao;
	@Autowired
	private SystemInstallDAO systeminstalldao;
	@Autowired
	private CwbOrderService cos;
	@Autowired
	private ReasonDao reasondao;
	@Autowired
	private BranchDAO branchDao;
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
	private SmsSendService smsSendService;
	@Autowired
	private SmsConfigDAO smsConfigDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	ExportService es;
	@Autowired
	SmsManageDao smsManageDao;
	@Autowired
	private CsPushSmsDao csPushSmsDao;
	@Autowired
	private CustomerDAO customerDao;
	@Autowired
	private RedisTemplate<String, Serializable> redisTemplate;
	@Autowired
	private Excel2007Extractor excel2007Extractor;
	@Autowired
	private Excel2003Extractor excel2003Extractor;
	
	/**工单号锁缓存超时时间，单位：秒*/
	public static final int ACCEPTNO_LOCK_CACHE_TIMEOUT = 5;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}
	
	@RequestMapping("/CustomerServicesAcceptWorkOrder")
	public String CustomerServicesAcceptWorkOrder(Model model){	
		model.addAttribute("KeHuLeiXingAllReason", reasondao.getKeHuLeiXingAllReason());
		return "workorder/CustomerServicesAcceptWorkOrder";
	}
/*	@RequestMapping("/CustomerServicesAcceptWorkOrder01")
	public String CustomerServicesAcceptWorkOrder01(){
		
		return "workorder/CustomerServicesAcceptWorkOrder01";
	}*/

	
	@RequestMapping("/CallerArchivalRepository/{page}")
	public String CallerArchivalRepository(Model model,CsConsigneeInfo cci,@PathVariable(value="page") long page){
			model.addAttribute("page", page);
			model.addAttribute("page_obj", new Page(workorderdao.getCsConsigneeInfocount(cci.getName(),cci.getPhoneonOne(),cci.getConsigneeType()), page, Page.ONE_PAGE_NUMBER));			
			model.addAttribute("ccilist", workorderdao.queryAllCsConsigneeInfo(page,cci.getName(),cci.getPhoneonOne(),cci.getConsigneeType()));
			model.addAttribute("KeHuLeiXingAllReason", reasondao.getKeHuLeiXingAllReason());
			return "workorder/CallerArchivalRepository";
	}
	@RequestMapping("/EditEditMaintain/{id}")
	public String EditEditMaintain(Model model,@PathVariable(value="id") int id) throws Exception{
		CsConsigneeInfo ccf=workorderdao.queryById(id);
		model.addAttribute("ccf", ccf);
		model.addAttribute("KeHuLeiXingAllReason", reasondao.getKeHuLeiXingAllReason());
		return "workorder/EditMaintain";		
	}
	
	@RequestMapping("/editCallerArchival")
	@ResponseBody
	public String editCallerArchival(CsConsigneeInfo ccf){
		workorderdao.editAllCsConsigneeInfo(ccf);
		logger.info(ccf.getCallerremark()+"123");
		return "{\"errorCode\":0,\"error\":\"修改成功\"}";
	}
	
	@RequestMapping("/addCallerArchival")
	@ResponseBody
	public String addCallerArchival(CsConsigneeInfo ccf){
		ccf.setContactNum(1);
		ccf.setContactLastTime(DateTimeUtil.getNowTime());
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
	public String CreateQueryWorkOrder(CsComplaintAccept a,Model model,@RequestParam(value="opscwbid",defaultValue="",required=true) int opscwbid,@RequestParam(value="CallerPhoneValue",defaultValue="",required=true) String CallerPhoneValue){
		String transcwb="G"+DateTimeUtil.getNowTimeNo()+((int)Math.random()*10);	
		CwbOrder cl=cwbdao.getCwbOrderByOpscwbid(opscwbid);
		CsComplaintAccept cca = new CsComplaintAccept();
			cca.setPhoneOne(CallerPhoneValue);
			cca.setProvence(cl.getConsigneeaddress());
			cca.setAcceptNo(transcwb);
			cca.setOrderNo(cl.getCwb());
			cca.setCwbstate(cl.getCwbstate());//(CwbStateEnum.getByValue((int) cl.getCwbstate()).getText());
			cca.setFlowordertype(cl.getFlowordertype());//(CwbFlowOrderTypeEnum.getText(cl.getFlowordertype()).getText());
			Branch b=branchDao.getbranchname(cl.getCurrentbranchid());
			if(b!=null){
			cca.setCurrentBranch(b.getBranchname());
			}else{
			cca.setCurrentBranch("");	
			}
			
		
		model.addAttribute("cca", cca);

		return "workorder/CreateQueryWorkOrder";
	}
	
	@RequestMapping("/CreateComplainWorkOrder")
	public String CreateComplainWorkOrder(Model model,@RequestParam(value="opscwbid",defaultValue="",required=true) int opscwbid,@RequestParam(value="CallerPhoneValue",defaultValue="",required=true) String CallerPhoneValue){
		String transcwb = createAcceptNo();
		CsComplaintAccept co=workorderdao.getCsComplaintAcceptByAcceptNo(transcwb);
//		while(co!=null){
//			transcwb="G"+DateTimeUtil.getNowTimeNo()+((int)Math.random()*10);
//			co=workorderdao.getCsComplaintAcceptByAcceptNo(transcwb);
//		}
		
		if(co != null){
			transcwb=transcwb+"1";
		}
		CwbOrder cl=cwbdao.getCwbOrderByOpscwbid(opscwbid);
		CsComplaintAccept ca = new CsComplaintAccept();
			ca.setProvence(cl.getCwbprovince());
			ca.setPhoneOne(CallerPhoneValue);
			ca.setAcceptNo(transcwb); //存入工单
			ca.setOrderNo(cl.getCwb());//存入订单
			ca.setCwbstate(cl.getCwbstate());//存入订单状态
			ca.setFlowordertype(cl.getFlowordertype());//存入订单操作状态
			ca.setCustomerid(cl.getCustomerid());
			Branch b=branchDao.getbranchname(cl.getCurrentbranchid());//存入站点信息
			if(b!=null){
			ca.setCurrentBranch(b.getBranchname());
			}else{
			ca.setCurrentBranch("");	
			}
			List<Branch> lb=branchDao.getAllBranches();
			List<Reason> lr=reasondao.addWO();
			model.addAttribute("lr", lr);
			model.addAttribute("ca", ca);
			model.addAttribute("lb", lb);
		return "workorder/CreateComplainWorkOrder";
	}
	
	/**
	 * 创建工单号
	 * @return
	 * @author neo01.huang
	 * 2016-5-25
	 */
	@ResponseBody
	@RequestMapping("createAcceptNo")
	public String createAcceptNo() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String acceptNo = null;
		synchronized (this) {
			Date date = new Date();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			acceptNo = "G" + df.format(date);
			acceptNo = acceptNo.substring(0, acceptNo.length() - 2);
			if (!tryAcceptNoLock(acceptNo)) {
				date = new Date();
				acceptNo = "G" + df.format(date) + "" + ((int)Math.random()*10);
				
			} 
		}
		return acceptNo;
	}
	
	/**
	 * 尝试获取工单号分布式锁
	 * @return
	 */
	private boolean tryAcceptNoLock(final String acceptNo) {
		try {
			
			final String key = ResourceBundleUtil.RedisPrefix + ":acceptNo:" + acceptNo; 
			Boolean isSet = redisTemplate.execute(new RedisCallback<Boolean>() {
				@Override
				public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
					boolean isSetSuccess = connection.setNX(key.getBytes(), acceptNo.getBytes());
					if (isSetSuccess) {
						connection.expire(key.getBytes(), ACCEPTNO_LOCK_CACHE_TIMEOUT);
					}
					return isSetSuccess;
				}
			});
			
			if (isSet) {
				return true;
			}
			
		} catch (Exception e) {
			return false;
		}
		return false;
	}
	
	@RequestMapping("/NewAddMaintain")
	public String NewAddMaintain(Model model){
		
		model.addAttribute("KeHuLeiXingAllReason", reasondao.getKeHuLeiXingAllReason());
		return "workorder/NewAddMaintain";
	}
	@RequestMapping("/OrgVerify")
	public String OrgVerify(Model model,@RequestParam(value="acceptNo",defaultValue="",required=true) String acceptNo) throws Exception{
		CsComplaintAccept cca=workorderdao.findGoOnacceptWOByWorkOrder(acceptNo);
		CwbOrder co=cwbdao.getOneCwbOrderByCwb(cca.getOrderNo());
		String customerName=customerDAO.findcustomername(co.getCustomerid()).getCustomername();
		CsConsigneeInfo cci=workorderdao.queryByPhoneNum(cca.getPhoneOne());		
		String nowtime =DateTimeUtil.getNowTime();
		String uname=getSessionUser().getUsername();
		cca.setHeshiUser(uname);
		cca.setHeshiTime(nowtime);
		List<Branch> lb=branchDao.getAllBranches();	
		model.addAttribute("OneLevel", reasondao.getReasonByReasonid(cca.getComplaintOneLevel()).getReasoncontent()==null?"":reasondao.getReasonByReasonid(cca.getComplaintOneLevel()).getReasoncontent());
		model.addAttribute("TwoLevel", reasondao.getReasonByReasonid(cca.getComplaintTwoLevel()).getReasoncontent()==null?"":reasondao.getReasonByReasonid(cca.getComplaintTwoLevel()).getReasoncontent());
		model.addAttribute("lb", lb);		
		model.addAttribute("cci", cci);
		model.addAttribute("alluser",userDao.getAllUser());
		model.addAttribute("cca", cca);		//存入工单信息
		model.addAttribute("co", co);    //存入订单信息表
		model.addAttribute("customerName",customerName);
		return "workorder/OrgVerify";
	}
	@RequestMapping("/CustomerServiceAdjudicate")
	public String CustomerServiceAdjudicate(Model model,@RequestParam(value="acceptNo",defaultValue="",required=true) String acceptNo) {     //客服结案
		CsComplaintAccept cca=workorderdao.findGoOnacceptWOByWorkOrder(acceptNo);		
		CwbOrder co=cwbdao.getOneCwbOrderByCwb(cca.getOrderNo());
		String customerName=customerDAO.findcustomername(co.getCustomerid()).getCustomername();
		String nowtime =DateTimeUtil.getNowTime();
		String uname=getSessionUser().getUsername();
		cca.setJieanUser(uname);
		cca.setJieanTime(nowtime);
		CsConsigneeInfo cci=workorderdao.queryByPhoneNum(cca.getPhoneOne())==null?null:workorderdao.queryByPhoneNum(cca.getPhoneOne());
		List<Branch> lb=branchDao.getAllBranches();	
		model.addAttribute("OneLevel", reasondao.getReasonByReasonid(cca.getComplaintOneLevel()).getReasoncontent()==null?"":reasondao.getReasonByReasonid(cca.getComplaintOneLevel()).getReasoncontent());
		model.addAttribute("TwoLevel", reasondao.getReasonByReasonid(cca.getComplaintTwoLevel()).getReasoncontent()==null?"":reasondao.getReasonByReasonid(cca.getComplaintTwoLevel()).getReasoncontent());
		model.addAttribute("lb", lb);		
		model.addAttribute("cci", cci);
		model.addAttribute("alluser",userDao.getAllUser());
		model.addAttribute("cca", cca);		//存入工单信息
		model.addAttribute("co", co);    //存入订单信息表
		model.addAttribute("customerName",customerName);
		return "workorder/CustomerServiceAdjudicate";
	}
	@RequestMapping("/OrgAppeal")
	public String OrgAppeal(Model model,@RequestParam(value="acceptNo",defaultValue="",required=true) String acceptNo){
		CsComplaintAccept cca=workorderdao.findGoOnacceptWOByWorkOrder(acceptNo);
		CwbOrder co=cwbdao.getOneCwbOrderByCwb(cca.getOrderNo());
		String customerName=customerDAO.findcustomername(co.getCustomerid()).getCustomername();
		CsConsigneeInfo cci=workorderdao.queryByPhoneNum(cca.getPhoneOne())==null?null:workorderdao.queryByPhoneNum(cca.getPhoneOne());
		List<Branch> lb=branchDao.getAllBranches();
		String nowtime =DateTimeUtil.getNowTime();
		String uname=getSessionUser().getUsername();
		
		cca.setShensuUser(uname);
		cca.setComplaintTime(nowtime);
		List<CsShenSuChat> cschatlist=workorderdao.getCsShenSuChatContentByAcceptNo(acceptNo);
		model.addAttribute("cschatlist", cschatlist);   
		model.addAttribute("OneLevel", reasondao.getReasonByReasonid(cca.getComplaintOneLevel()).getReasoncontent()==null?"":reasondao.getReasonByReasonid(cca.getComplaintOneLevel()).getReasoncontent());
		model.addAttribute("TwoLevel", reasondao.getReasonByReasonid(cca.getComplaintTwoLevel()).getReasoncontent()==null?"":reasondao.getReasonByReasonid(cca.getComplaintTwoLevel()).getReasoncontent());
		model.addAttribute("customerName",customerName);
		model.addAttribute("lb", lb);   
		model.addAttribute("alluser",userDao.getAllUser());
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
		List<Branch> lb=branchDao.getAllBranches();
		String nowtime =DateTimeUtil.getNowTime();
		String uname=getSessionUser().getUsername();
		cca.setChongshenUser(uname);
		cca.setJieanchongshenTime(nowtime);
		model.addAttribute("OneLevel", reasondao.getReasonByReasonid(cca.getComplaintOneLevel()).getReasoncontent());
		model.addAttribute("TwoLevel", reasondao.getReasonByReasonid(cca.getComplaintTwoLevel()).getReasoncontent());
		
		model.addAttribute("lb", lb);  
		model.addAttribute("alluser",userDao.getAllUser());
		model.addAttribute("cci", cci);
		model.addAttribute("cca", cca);		//存入工单信息
		model.addAttribute("co", co);    //存入订单信息表
		return "workorder/AdjudicateRetrial";
	}
	//添加客服收件人数据
	@RequestMapping("/addCsConsigneeInfo")
	public String add(CsConsigneeInfo cci,Model model){
		List<CsConsigneeInfo> cs=workorderdao.queryListCsConsigneeInfo(cci.getPhoneonOne());
		
		if(cs.size()<1){
			CsConsigneeInfo cf = new CsConsigneeInfo();
			cf.setName(cci.getName());
			cf.setSex(cci.getSex());
			cf.setConsigneeType(cci.getConsigneeType());
			cf.setContactNum(1);
			cf.setContactLastTime(DateTimeUtil.getNowTime());
			cf.setPhoneonOne(cci.getPhoneonOne());
			cf.setCity(cci.getCity());
			cf.setProvince(cci.getProvince());
		
			workorderservice.addcsconsigneeInfo(cf);
		}
			
		model.addAttribute("KeHuLeiXingAllReason", reasondao.getKeHuLeiXingAllReason());
		return "workorder/CustomerServicesAcceptWorkOrder";	
	}
	
	@RequestMapping("/selectByPhoneNum")
	@ResponseBody
	public CsConsigneeInfo selectByPhoneNum(HttpServletRequest req){
		String phone=req.getParameter("phoneonOne");		
		CsConsigneeInfo cci=workorderservice.querycciByPhoneNum(phone);
		String nowtime=DateTimeUtil.getNowTime();
		CsConsigneeInfo cci1=null;
		if(cci!=null){
		int newcn=cci.getContactNum()+1;
		workorderdao.updateConnum(newcn,phone);
		cci1=workorderservice.querycciByPhoneNum(phone);
		cci1.setContactLastTime(nowtime);
		}

		return cci1;		
	}
	
	@RequestMapping("/SelectdetalForm/{page}")
	@ResponseBody
	public Map<String, Object> SelectdetalForm(HttpServletRequest req,@PathVariable(value="page") long currentPage,Model model){
		String phone=req.getParameter("phoneonOne");
		List<CwbOrder> cwborderlist=workorderservice.SelectCwbdetalForm(phone,currentPage);
		long count =workorderservice.SelectDetalFormCount(phone);
		Map<String,Object> pageResult = new HashMap<String, Object>();
		//TODO 分页逻辑
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
		pageResult.put("page",currentPage);
		pageResult.put("page_obj", new Page(workorderservice.SelectDetalFormCount(phone), currentPage, Page.ONE_PAGE_NUMBER));		
		pageResult.put("list",lc);
		pageResult.put("currentPage", 1);
		pageResult.put("pageSize",Page.ONE_PAGE_NUMBER);
		pageResult.put("pageNum",(long)Math.ceil(count/Page.ONE_PAGE_NUMBER));
		return pageResult;
	}
	
	@RequestMapping("/selectDetalFormByCondition/{page}")
	@ResponseBody
	public Map<String,Object> selectDetalFormByCondition(CwbOrderAndCustomname coc,Model model,
			@RequestParam(value="staremaildate",defaultValue="",required=false) String staremaildate,
			@RequestParam(value="endemaildate",defaultValue="",required=false) String endemaildate,
			@PathVariable(value="page") long page
			) throws Exception{
	
		String cwb1 = cos.translateCwb(coc.getCwb());
		coc.setCwb(cwb1);	
		List<CwbOrder> lc=cwbdao.SelectDetalFormByCondition(coc,staremaildate,endemaildate,page);
		Map<String,Object> pageResult = new HashMap<String, Object>();
		List<CwbOrderAndCustomname> lco= new ArrayList<CwbOrderAndCustomname>();
		for(CwbOrder c:lc){
			CwbOrderAndCustomname co = new CwbOrderAndCustomname();
				co.setId((c.getOpscwbid()));
				co.setCwb(c.getCwb());
				co.setTranscwb(c.getTranscwb());
				Customer customer = customerDAO.findcustomername(c.getCustomerid());
				if (customer != null && customer.getCustomername() != null) {
					co.setCustomername(customer.getCustomername());
				}
				co.setEmaildate(c.getEmaildate());
				co.setConsigneename(c.getConsigneename());
				co.setConsigneeaddress(c.getConsigneeaddress());
				co.setConsigneemobile(SecurityUtil.getInstance().decrypt(c.getConsigneemobile()));
				co.setCwbstate(CwbStateEnum.getByValue(c.getCwbstate()).getText());
				lco.add(co);				
		}
		pageResult.put("page",page);
		pageResult.put("page_obj", new Page(cwbdao.SelectDetalFormByConditionCount(coc,staremaildate,endemaildate), page, Page.ONE_PAGE_NUMBER));		
		pageResult.put("list",lco);
		pageResult.put("currentPage", 1);
		pageResult.put("pageSize",Page.ONE_PAGE_NUMBER);
		return pageResult;
	}
	

	
	@RequestMapping("/saveWorkOrderQueryF")
	@ResponseBody
	public String saveWorkOrderQueryForm(CsComplaintAccept cca){
		String accepttime=DateTimeUtil.formatDate(new Date());
		cca.setAcceptTime(accepttime);		
		CwbOrder co=cwbdao.getCwbByCwb(cca.getOrderNo());
		cca.setCustomerid(co.getCustomerid());
		String username=getSessionUser().getUsername();
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
		String accepttime=DateTimeUtil.formatDate(new Date());
		cca.setAcceptTime(accepttime);
		CwbOrder co=cwbdao.getCwbByCwb(cca.getOrderNo());
		cca.setCustomerid(co.getCustomerid());
		String username=getSessionUser().getUsername();
		cca.setHandleUser(username);
		
		//add by neo01.huang,加入工单号重复校验
		CsComplaintAccept csComplaintAccept = workorderdao.getCsComplaintAcceptByAcceptNo(cca.getAcceptNo());
		if (csComplaintAccept != null) {
			return "{\"errorCode\":1,\"error\":工单号重复，" + cca.getAcceptNo() + "\"}";
		}
		
		workorderdao.saveComplainWorkOrderF(cca);
	
		return "{\"errorCode\":0,\"error\":\"保存成功\"}";
	}
	
	
	@RequestMapping("/updateComplainWorkOrderF")
	@ResponseBody
	public String updateComplainWorkOrderF(CsComplaintAccept cca){
		String accepttime=DateTimeUtil.formatDate(new Date());
		cca.setAcceptTime(accepttime);
		workorderdao.updateComplainWorkOrderF(cca);	
		return "{\"errorCode\":0,\"error\":\"受理成功\"}";
	}
	

	@RequestMapping("/findGoOnAcceptWO/{page}")  //通过手机号查询工单
	@ResponseBody
	public Map<String,Object> findGoOnAcceptWO(Model model,HttpServletRequest req,@PathVariable(value="page") long page){
		String phone=req.getParameter("phoneonOne");
		List<CsComplaintAccept> lcs=workorderdao.findGoOnacceptWO(phone,page);
		Map<String,Object> pageResult = new HashMap<String, Object>();
		List<CsComplaintAcceptVO> lc=new ArrayList<CsComplaintAcceptVO>();
		for(CsComplaintAccept c:lcs){
			CsComplaintAcceptVO ca = new CsComplaintAcceptVO();
			ca.setAcceptNo(c.getAcceptNo());
			ca.setOrderNo(c.getOrderNo());
			ca.setPhoneOne(c.getPhoneOne());
			ca.setAcceptTime(c.getAcceptTime());
		/*	ca.setComplaintType(c.getComplaintType());*/
			ca.setContent(c.getContent());
			ca.setComplaintState(c.getComplaintState());
			ca.setProvence(c.getProvence());
			/*ca.setShowcomplaintTypeName(ComplaintTypeEnum.getByValue(c.getComplaintType()).getText());*/
			ca.setShowComplaintStateName(ComplaintStateEnum.getByValue(c.getComplaintState()));
			ca.setComplaintUser(c.getComplaintUser());
			ca.setQueryContent(c.getQueryContent());
			lc.add(ca);
		}
		pageResult.put("page",page);
		pageResult.put("page_obj", new Page(workorderdao.findGoOnacceptWOCount(phone), page, Page.ONE_PAGE_NUMBER));		
		pageResult.put("list",lc);
		model.addAttribute("Username", getSessionUser().getUsername());
		
		return pageResult;

	}
	
	@RequestMapping("/findGoOnAcceptWOByCWB/{page}") 
	@ResponseBody
	public Map<String,Object> findGoOnAcceptWOByCWB(Model model,HttpServletRequest req,@PathVariable(value="page") long page){
		String cwb=req.getParameter("cwb");
		List<CsComplaintAccept> lcs=workorderdao.findGoOnacceptWOByCWB(cwb,page);
		Map<String,Object> pageResult = new HashMap<String, Object>();
		List<CsComplaintAcceptVO> lc=new ArrayList<CsComplaintAcceptVO>();
		for(CsComplaintAccept c:lcs){
			CsComplaintAcceptVO ca = new CsComplaintAcceptVO();
			ca.setAcceptNo(c.getAcceptNo());
			ca.setOrderNo(c.getOrderNo());
			ca.setPhoneOne(c.getPhoneOne());
			ca.setProvence(c.getProvence());
			ca.setAcceptTime(c.getAcceptTime());
			/*ca.setComplaintType(c.getComplaintType());*/
			ca.setComplaintState(c.getComplaintState());
			/*ca.setShowcomplaintTypeName(ComplaintTypeEnum.getByValue(c.getComplaintType()).getText());*/
			ca.setContent(c.getContent());
			ca.setShowComplaintStateName(ComplaintStateEnum.getByValue(c.getComplaintState()));
			ca.setComplaintUser(c.getComplaintUser());
			ca.setQueryContent(c.getQueryContent());
			lc.add(ca);
		}
		pageResult.put("page",page);
		pageResult.put("page_obj", new Page(workorderdao.findGoOnacceptWOByCWBCount(cwb), page, Page.ONE_PAGE_NUMBER));		
		pageResult.put("list",lc);
		
		return pageResult;

	}
	
	@RequestMapping("/GoOnacceptWo")
	public String GoOnacceptWo(HttpServletRequest req,Model model){
		String workorder=req.getParameter("workorder");		
			String RuKuTime=null;
			CsComplaintAccept lcs=workorderdao.findGoOnacceptWOByWorkOrder(workorder);	
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
			List<Reason> alltworeason=reasondao.getAllTwoLevelReason();
			List<Branch> lb=branchDao.getAllBranches();
			
			model.addAttribute("lr", lr);
			model.addAttribute("alltworeason", alltworeason);
			model.addAttribute("lcs",lcs );
			model.addAttribute("conMobile",conMobile );
			model.addAttribute("RuKuTime",RuKuTime);
			model.addAttribute("lb", lb);
			model.addAttribute("alluser",userDao.getAllUser());
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
		model.addAttribute("lc", lc);		
		return "workorder/WaitManageWorkOrder";
	}
	/*工单号 	订单号 	工单类型 	工单状态 	
	来电人姓名 	来电号码 	被投诉机构 	工单受理人 	
	受理时间 	投诉一级分类 	投诉二级分类 	
	投诉处理结果 	是否扣罚 	客户名称	催件次数*/
	@RequestMapping("/WorkOrderQueryManage/{page}")  /*WorkOrderManageQuery*/
	public String WorkOrderManageQuery(@PathVariable(value="page") long page,Model model,CsComplaintAcceptVO cv) throws Exception{
		List<CsComplaintAccept> lcs=null;
		StringBuffer sb = new StringBuffer();
		String ncwbs="";
		if(!StringUtils.isEmpty(cv.getOrderNo())){
			String cwb1 = cos.translateCwb(cv.getOrderNo());
			String cwb[]=cwb1.trim().split("\r\n");			
			for(String str:cwb){
				str = str.trim();
				sb=sb.append("'"+str+"',");
			}
			ncwbs=sb.substring(0, sb.length()-1);	
		}	
		StringBuffer sb1 = new StringBuffer();
		String workorders="";
		if(!StringUtils.isEmpty(cv.getAcceptNo())){		
		String workorder[]=cv.getAcceptNo().trim().split("\r\n");		
		for(String str1:workorder){
			str1 = str1.trim();
			sb1=sb1.append("'"+str1+"',");
		}
		workorders=sb1.substring(0, sb1.length()-1);
		}	
		
		SystemInstall systemInstall=systeminstalldao.getSystemInstall("ServiceID");
		String roleids=systemInstall==null?new SystemInstall().getValue():systemInstall.getValue();
		Page p=null;		
		boolean b = false;
		String roleidss[]=roleids.split(",");
		for(String s:roleidss){
			if(getSessionUser().getRoleid()==1||(getSessionUser().getRoleid()+"").equals(s)){
				b=true;
				break;
			}
		}
		if(b==true){		
			lcs=workorderdao.findGoOnacceptWOByCWBs(page,ncwbs,cv,workorders);		
			p=new Page(workorderdao.findGoOnacceptWOByCWBsCount(ncwbs,cv,workorders), page, Page.ONE_PAGE_NUMBER);			
		}else
		{    
			lcs=workorderdao.findGoOnacceptWOByCWBsnew(page,ncwbs,cv,workorders,userDao.getbranchidbyuserid(getSessionUser().getUserid()).getBranchid());
			p=new Page(workorderdao.findGoOnacceptWOByCWBsCountnew(ncwbs,cv,workorders,userDao.getbranchidbyuserid(getSessionUser().getUserid()).getBranchid()), page, Page.ONE_PAGE_NUMBER);
		}		
		model.addAttribute("roleids",roleids);
		model.addAttribute("page", page);		
		model.addAttribute("page_obj",p);				
		List<CsComplaintAccept> lc=new ArrayList<CsComplaintAccept>();
		Map<String,String> connameList=new HashMap<String, String>();
		Map<Long,String> customerList=new HashMap<Long, String>();
		for(CsComplaintAccept c:lcs){
			CsComplaintAccept ca = new CsComplaintAccept();
			ca.setAcceptNo(c.getAcceptNo());
			ca.setOrderNo(c.getOrderNo());
			/*ca.setComplaintType(c.getComplaintType());*/
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
			ca.setCuijianNum(c.getCuijianNum());
			ca.setIfpunish(c.getIfpunish());
			lc.add(ca);			
			String cname=workorderdao.queryByPhoneone(c.getPhoneOne())==null?"":workorderdao.queryByPhoneone(c.getPhoneOne()).getName();
			connameList.put(c.getPhoneOne(), cname);
			String customerName=customerDAO.findcustomername(c.getCustomerid()).getCustomername();
			customerList.put(c.getCustomerid(),customerName);			
	}	
		/*List<CwbOrder> co=cwbdao.getCwbByCwbs(ncwbs);*/
		List<Reason> alltworeason=reasondao.getAllTwoLevelReason();
		List<Branch> lb=branchDao.getAllBranches();
		List<Reason> lr=reasondao.addWO();
		List<CsComplaintAccept> lcsa=workorderdao.refreshWOFPage();
		model.addAttribute("heshiTime", Integer.valueOf(systeminstalldao.getSystemInstallByName("heshiTime").getValue()));
		model.addAttribute("customernameList", customerList);
		model.addAttribute("lr", lr);
		model.addAttribute("lcsa", lcsa);
		model.addAttribute("alltworeason", alltworeason);
		model.addAttribute("lb", lb); 
		model.addAttribute("lc", lc);
		model.addAttribute("connameList", connameList);
		model.addAttribute("alluser",userDao.getAllUser());
		model.addAttribute("currentuser", getSessionUser().getRoleid());
		model.addAttribute("customers",customerDAO.getAllCustomers());
		String[] customerStr = {};
		model.addAttribute("customerStr", cv.getCustomerIds() == null ? customerStr : cv.getCustomerIds().split(","));
		return "workorder/WorkOrderQueryManage";		
	}
	@RequestMapping("/selectBranch")
	@ResponseBody
	public List<Branch> selectBranch(@RequestParam(value="branchname") String branchname){
		List<Branch> branchs = branchDao.getBranchByBranchnameMoHu(branchname);
		return branchs;
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
		cca.setHeshiUser(user.getUsername());
		cca.setHeshiTime(DateTimeUtil.getNowTime());
		workorderdao.OrgChangecomplaintState(cca);
		
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
		cca.setHeshiUser(getSessionUser().getUsername());
		cca.setHeshiTime(DateTimeUtil.getNowTime());
		workorderdao.OrgChangecomplaintState(cca);
		
		return "{\"errorCode\":0,\"error\":\"核实成功\"}";	
	}
	
	
	
	@RequestMapping("/WorkorderDetail/{acceptNo}")
	public String Workorderdetail(Model model,@PathVariable(value="acceptNo") String acceptNo){
		CsComplaintAccept cca=workorderdao.findGoOnacceptWOByWorkOrder(acceptNo);
		CwbOrder co=cwbdao.getOneCwbOrderByCwb(cca.getOrderNo());
		CsConsigneeInfo cci=workorderdao.queryByPhoneNum(cca.getPhoneOne());
		List<Branch> lb=branchDao.getAllBranches();
		String nowtime =DateTimeUtil.getNowTime();
		String uname=getSessionUser().getUsername();
		cca.setHandleUser(uname);
		cca.setJieanchongshenTime(nowtime);
		List<CsShenSuChat> cschatlist=workorderdao.getCsShenSuChatContentByAcceptNo(acceptNo);
		model.addAttribute("cschatlist", cschatlist);   
		model.addAttribute("OneLevel", reasondao.getReasonByReasonid(cca.getComplaintOneLevel()).getReasoncontent());
		model.addAttribute("TwoLevel", reasondao.getReasonByReasonid(cca.getComplaintTwoLevel()).getReasoncontent());		
		model.addAttribute("lb", lb);   
		model.addAttribute("cci", cci);
		model.addAttribute("cca", cca);		//存入工单信息
		model.addAttribute("co", co);    //存入订单信息表
		model.addAttribute("alluser",userDao.getAllUser());
		
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
			cv.setConsigneeType(reasondao.getReasonByReasonid(c.getConsigneeType()).getReasoncontent());
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
		logger.error("", e);
	}
		
		
	}
	
	@RequestMapping("/exportWorkOrderExcle")
	public void exportWorkOrderExcle(Model model, HttpServletResponse response, HttpServletRequest request,CsComplaintAccept cca) {

		String[] cloumnName1 = new String[14]; // 导出的列名
		String[] cloumnName2 = new String[14]; // 导出的英文列名
		es.setCsComplaintAcceptVO(cloumnName1,cloumnName2);
		final String[] cloumnName = cloumnName1;
		final String[] cloumnName3 = cloumnName2;
		String sheetName = "工单信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "CsComplaintAccept_" + df.format(new Date()) + ".xlsx"; // 文件名
	 try{	
		String cwbs=request.getParameter("orderNo");
		/*String complaintType = request.getParameter("complaintType")==null?"-1":request.getParameter("complaintType");*/
		String orderNo = request.getParameter("orderNo")==null?"":request.getParameter("orderNo");
		String complaintState = request.getParameter("complaintState")==null?"-1":request.getParameter("complaintState");
		String complaintOneLevel = request.getParameter("complaintOneLevel")==null?"-1":request.getParameter("complaintOneLevel");
		String codOrgId = request.getParameter("codOrgId")==null?"-1":request.getParameter("codOrgId");
		String complaintResult = request.getParameter("complaintResult")==null?"-1":request.getParameter("complaintResult");
		String complaintTwoLevel = request.getParameter("complaintTwoLevel")==null?"-1":request.getParameter("complaintTwoLevel");
		String beginRangeTime = request.getParameter("beginRangeTime")==null?"":request.getParameter("beginRangeTime");
		String endRangeTime = request.getParameter("endRangeTime")==null?"":request.getParameter("endRangeTime");
		String handleUser = request.getParameter("handleUser")==null?"":request.getParameter("handleUser");
		String ifpunish = request.getParameter("ifpunish")==null?"-1":request.getParameter("ifpunish");
		String acceptNo = request.getParameter("acceptNo");
		CsComplaintAcceptVO cc = new CsComplaintAcceptVO();
		/*cc.setComplaintType(Integer.valueOf(complaintType));*/
		cc.setOrderNo(orderNo);
		cc.setComplaintState(Integer.valueOf(complaintState));
		cc.setComplaintOneLevel(Integer.valueOf(complaintOneLevel));
		cc.setCodOrgId(Integer.valueOf(codOrgId));
		cc.setComplaintResult(Integer.valueOf(complaintResult));
		cc.setComplaintTwoLevel(Integer.valueOf(complaintTwoLevel));
		cc.setHandleUser(handleUser);
		cc.setIfpunish(Integer.valueOf(ifpunish));
		cc.setBeginRangeTime(beginRangeTime);
		cc.setEndRangeTime(endRangeTime);
		
		
		/**
		 * 下面是查询
		 */
		
		List<CsComplaintAccept> lcs=null;
		StringBuffer sb = new StringBuffer();
		String ncwbs="";
		if(!StringUtils.isEmpty(cwbs)){
			String cwb1 = cos.translateCwb(cwbs);
			String cwb[]=cwb1.trim().split("\r\n");			
			for(String str:cwb){
				str = str.trim();
				sb=sb.append("'"+str+"',");
			}
			ncwbs=sb.substring(0, sb.length()-1);	
		}	
		StringBuffer sb1 = new StringBuffer();
		String workorders="";
		if(!StringUtils.isEmpty(acceptNo)){		
		String workorder[]=acceptNo.trim().split("\r\n");		
		for(String str1:workorder){
			str1 = str1.trim();
			sb1=sb1.append("'"+str1+"',");
		}
		workorders=sb1.substring(0, sb1.length()-1);
		}
		
		SystemInstall systemInstall=systeminstalldao.getSystemInstall("ServiceID");
		String roleids=systemInstall==null?new SystemInstall().getValue():systemInstall.getValue();		
		if(getSessionUser().getRoleid()==1||roleids.contains(getSessionUser().getRoleid()+"")){		
			lcs=workorderdao.findGoOnacceptWOByCWBs1(ncwbs,cc,workorders);		
		
		}else
		{    
			lcs=workorderdao.findGoOnacceptWOByCWBsnewNoPage(ncwbs,cc,workorders,userDao.getbranchidbyuserid(getSessionUser().getUserid()).getBranchid());
		}		
	
		/*lcs=workorderdao.findGoOnacceptWOByCWBs1(ncwbs,cc,workorders);*/
	final List<CsComplaintAcceptExportVO> lc=new ArrayList<CsComplaintAcceptExportVO>();
		for(CsComplaintAccept c:lcs){
			CsComplaintAcceptExportVO ca = new CsComplaintAcceptExportVO();
			ca.setAcceptNo(c.getAcceptNo());
			ca.setOrderNo(c.getOrderNo());
			ca.setComplaintState(ComplaintStateEnum.getByValue1(c.getComplaintState()).getText());
			ca.setCodOrgId(branchDao.getBranchByBranchid(c.getCodOrgId()).getBranchname());
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
			ca.setName(workorderdao.queryByPhoneone(c.getPhoneOne())==null?"":workorderdao.queryByPhoneone(c.getPhoneOne()).getName());
			ca.setIfpunish(c.getIfpunish()==2?"是":"否");
			ca.setCuijianNum(c.getCuijianNum()>0?c.getCuijianNum():0);
			ca.setHandleUser(userDao.getUserByUsername(c.getHandleUser()).getRealname());
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
		logger.error("", e);
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
		String JieAncomplaintResult=StringUtil.nullConvertToEmptyString(req.getParameter("complaintResult"));
		CsComplaintAccept cca = new CsComplaintAccept();
		cca.setDownloadjieanpath(downloadname);
		cca.setJieanremark(jieanremark);
		cca.setId(Integer.valueOf(jid));
		cca.setComplaintState(Integer.valueOf(JieAnChangeComplaintState));
		cca.setJieanUser(getSessionUser().getUsername());
		cca.setJieanTime(DateTimeUtil.getNowTime());
		cca.setComplaintResult(Integer.valueOf(JieAncomplaintResult));
		workorderdao.ChangecomplaintState(cca);
		
		return "{\"errorCode\":0,\"error\":\"结案处理成功\"}";	
	}
	@RequestMapping("/JieAnChangeComplaintState")
	@ResponseBody
	public String JieAnChangeComplaintState(HttpServletRequest req){
		String jieanremark=StringUtil.nullConvertToEmptyString(req.getParameter("jieanremark"));
		String jid= StringUtil.nullConvertToEmptyString(req.getParameter("id"));
		String JieAnChangeComplaintState=StringUtil.nullConvertToEmptyString(req.getParameter("complaintState"));
		String JieAncomplaintResult=StringUtil.nullConvertToEmptyString(req.getParameter("complaintResult"));
		CsComplaintAccept cca = new CsComplaintAccept();
		cca.setComplaintResult(Integer.valueOf(JieAncomplaintResult));
		cca.setJieanremark(jieanremark);
		if(!jid.equals("")){
		cca.setId(Integer.valueOf(jid));
		}
		cca.setComplaintState(Integer.valueOf(JieAnChangeComplaintState));
		cca.setJieanUser(getSessionUser().getUsername());
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
		if(!jid.equals("")){
		cca.setId(Integer.valueOf(jid));
		}
		cca.setComplaintState(Integer.valueOf(shensuChangeComplaintState));
		cca.setShensuUser(getSessionUser().getUsername());
		cca.setComplaintTime(DateTimeUtil.getNowTime());
		workorderdao.OrgChangecomplaintState(cca);
		
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
		cca.setShensuUser(getSessionUser().getUsername());
		cca.setComplaintTime(DateTimeUtil.getNowTime());
		workorderdao.OrgChangecomplaintState(cca);
		
		return "{\"errorCode\":0,\"error\":\"申诉处理成功\"}";	
	}
	
	@RequestMapping("/JieAnChongShenChangeComplaintStateFile")
	@ResponseBody
	public String JieAnChongShenChangeComplaintStateFile(HttpServletRequest req,
		@RequestParam(value = "Filedata", required = false) MultipartFile file){		
		String jieanchongshenremark=StringUtil.nullConvertToEmptyString(req.getParameter("jieanchongshenremark"));
		String jid= StringUtil.nullConvertToEmptyString(req.getParameter("id"));
		String JieAnChongShenChangeComplaintState=StringUtil.nullConvertToEmptyString(req.getParameter("complaintState"));
		String downloadname=workorderservice.loadexceptfile(file);
		String JieAncomplaintResult=StringUtil.nullConvertToEmptyString(req.getParameter("complaintResult"));
		CsComplaintAccept cca = new CsComplaintAccept();
		cca.setComplaintResult(Integer.valueOf(JieAncomplaintResult));
		cca.setDownloadchongshenpath(downloadname);
		cca.setJieanchongshenremark(jieanchongshenremark);
		cca.setId(Integer.valueOf(jid));
		cca.setComplaintState(Integer.valueOf(JieAnChongShenChangeComplaintState));
		cca.setChongshenUser(getSessionUser().getUsername());
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
		String JieAncomplaintResult=StringUtil.nullConvertToEmptyString(req.getParameter("complaintResult"));
		CsComplaintAccept cca = new CsComplaintAccept();
		cca.setComplaintResult(Integer.valueOf(JieAncomplaintResult));
		cca.setJieanchongshenremark(jieanchongshenremark);
		cca.setId(Integer.valueOf(jid));
		cca.setComplaintState(Integer.valueOf(JieAnChongShenChangeComplaintState));
		cca.setChongshenUser(getSessionUser().getUsername());
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
			logger.error("", e);
		} 

	}

	
	@RequestMapping("/getBeiTouSuRenValue")
	@ResponseBody
	public List<User> getBeiTouSuRenValue(@RequestParam(value="codOrgId" ,defaultValue="",required=true) long codOrgId){
		List<User> u=userDao.getBeiTouSuRen(codOrgId);
	return u;		
	}
	
	
	@RequestMapping("/getTwoValueByOneReason")
	@ResponseBody
	public List<Reason> getTwoValueByOneReason(HttpServletRequest req){
		String leave=req.getParameter("complaintOneLevel");
	return reasondao.getSecondLevelReason(Integer.valueOf(leave));		
	}
	
	
	
	/**
	 * 发送催件短信
	 * @param request
	 * @return
	 */
	@RequestMapping("/smsSend")
	@ResponseBody
	public String smsSend(CsComplaintAccept cca,HttpServletRequest request){
		
		String accepttime=DateTimeUtil.formatDate(new Date());
		cca.setAcceptTime(accepttime);
		CwbOrder co=cwbdao.getCwbByCwb(cca.getOrderNo());
		cca.setCustomerid(co.getCustomerid());
		String username=getSessionUser().getUsername();
		cca.setHandleUser(username);
		
		//add by neo01.huang,加入工单号重复校验
		CsComplaintAccept csComplaintAccept = workorderdao.getCsComplaintAcceptByAcceptNo(cca.getAcceptNo());
		if (csComplaintAccept != null) {
			return "{\"errorCode\":1,\"error\":工单号重复，" + cca.getAcceptNo() + "\"}";
		}
		workorderdao.saveComplainWorkOrderF(cca);
		
		//构建数据
		String cwbScan = cca.getOrderNo();
		String workOrderNo = cca.getAcceptNo();
		String message = cca.getContent();
		int compBranch = cca.getCodOrgId();
		String compUser = cca.getComplaintUser();
		String cwb = cos.translateCwb(cwbScan);
		CwbOrder cwbOrder = cwbdao.getCwbByCwb(cwb);
		FlowOrderTypeEnum cwbOrderFlow = FlowOrderTypeEnum.getText(cwbOrder.getFlowordertype());
		DeliveryStateEnum cwbDeliverState = DeliveryStateEnum.getByValue(cwbOrder.getDeliverystate());
		String smsSendMobile = null;
		Long receiveId = null;
		Integer receiveType = null;
		/** 短信逻辑  */
		//分站领货、反馈为
		if( FlowOrderTypeEnum.FenZhanLingHuo.equals(cwbOrderFlow) ||
			FlowOrderTypeEnum.YiFanKui.equals(cwbOrderFlow) && DeliveryStateEnum.ZhiLiuZiDongLingHuo.equals(cwbDeliverState)){
			User deliver = userDao.getUserByUserid(cwbOrder.getDeliverid());
			smsSendMobile = deliver.getUsermobile();
			receiveId = Long.valueOf(deliver.getUserid());
			receiveType = Integer.valueOf(2);
		}
		//其他状态订单
		else{
			Branch currentBranch = branchDao.getBranchById(cwbOrder.getCurrentbranchid());
			smsSendMobile = currentBranch.getBranchmobile();
			receiveId = Long.valueOf(currentBranch.getBranchid());
			receiveType = Integer.valueOf(1);
		}
		
		//初始催件短信对象
		CsPushSms csPushSms = new CsPushSms(cwbScan, workOrderNo, Integer.valueOf(ComplaintStateEnum.DaiHeShi.getValue()), username, DateTimeUtil.getNowTime(), message,Long.valueOf(compBranch),compUser,receiveId,receiveType,smsSendMobile);
		
		String msg = "";
		int j = 0;
		int i = 0;
		String errorMsg = "";
		//发送短信
		try {
			if(StringUtils.isEmpty(smsSendMobile)){
				throw new RuntimeException("当前负责人未设置手机号，请前往设置页面进行设置！");
			}
			if (smsSendMobile.trim().length() > 0) {
				logger.info("短信发送，单量：{}", smsSendMobile.split(",").length);
				for (String mobile : smsSendMobile.split(",")) {
					if (mobile.trim().length() == 0) {
						continue;
					}
					if(StringUtils.isEmpty(message)){
						errorMsg += "催件短信内容为空，请确认工单内容！";
						continue;
					}else{
						message = "订单号：" + cwbScan + "；催件内容：" + message;
					}
					if (!isMobileNO(mobile)) {
						logger.info("短信发送，手机号：{}", mobile.trim());
						j++;
						errorMsg += "手机号：[" + mobile.trim() + "]:手机号格式不正确";
						continue;
					}
					if (mobile != null && !"".equals(mobile)) {
						logger.info("短信发送，手机号：{}", mobile.trim());
						try {
							msg = smsSendService.sendSms(mobile, message, 1, 0, "催件短信", getSessionUser().getUserid(), HttpUtil.getUserIp(request));
							logger.info("短信发送，手机号：{}  结果：{}", mobile.trim(), msg);
							if ("发送短信成功".equals(msg)) {
								i++;
							} else {
								errorMsg += "手机号：[" + mobile.trim() + "]:" + msg;
								j++;
							}
						} catch (UnsupportedEncodingException e) {
							logger.error("短信发送，异常", e);
							j++;
							errorMsg += "手机号：[" + mobile.trim() + "]:短信发送网络异常";
						}
					} else {
						j++;
						errorMsg += "手机号：[" + mobile.trim() + "]:手机号为空";
					}
				}
			}
			logger.info("短信发送，成功单数：{}", i);
			logger.info("短信发送，失败单数：{}", j);

		} catch (Exception e) {
			logger.error("短信发送，异常", e);
			errorMsg += e.getMessage();
		}
		
		if( StringUtils.isEmpty(errorMsg) && "发送短信成功".equals(msg)){
			this.csPushSmsDao.createReId(csPushSms);
			return "{\"errorCode\":0,\"error\":\"催件短信发送成功！\"}";
		}else{
			return "{\"errorCode\":1,\"error\":\"催件短信发送失败："+ errorMsg +"\"}";
		}
		
	}
	
	// 验证手机号码的合法性
	private static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^1\\d{10}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
	
	@RequestMapping("/huiFuChat")
	@ResponseBody
	public String huiFuChat(HttpServletRequest req){
		String nowTime=DateTimeUtil.getNowTime();
		long unameid=getSessionUser().getUserid();
		String content=req.getParameter("textareavalue");
		String acceptNo=req.getParameter("acceptNo");
		workorderdao.huifuChat(nowTime,content,unameid,acceptNo);
		return "{\"success\":0,\"successdata\":\"回复成功\"}";
		
	}
	
	@RequestMapping("/WorkManageBatch")
	public String workManageBatch(Model model){
		return "workorder/WorkManageBatch";
	}
	
	@RequestMapping("/WorkManageBatchUploadFile")
	public @ResponseBody String WorkManageBatchUploadFile(
			HttpServletRequest request,
			@RequestParam(value = "Filedata", required = false) MultipartFile file) {
		String jsonString = "";
		try {
			AjaxJson ajaxJson = new AjaxJson();
			long bTime = new Date().getTime();
			this.logger.info("工单导入开始...");
			String typeValue = request.getParameter("type");
			if (StringUtils.isBlank(typeValue)) {
				ajaxJson.setStatus(false);
				ajaxJson.setMsg("导入处理类型为空！");
				return JsonUtil.translateToJson(ajaxJson);
			}
			int type = Integer.valueOf(typeValue);
			ExcelExtractor extractor = getExcelExtractor(file);
			Map<String, List<CsComplaintAcceptViewVo>> result = workorderservice
					.importWorkOrders(file.getInputStream(), extractor,
							getSessionUser().getUsername(), type);
			ajaxJson.setMap(result);
			jsonString = JsonUtil.translateToJson(ajaxJson);
			long eTime = new Date().getTime();
			this.logger.info("工单导入结束，耗时" + (eTime - bTime) + "ms");
		} catch (Exception e) {
			this.logger.error("导入工单时出现异常", e);
		}
		return jsonString;
	}
	
	/*
	 * 新建模板下载
	 */
	@RequestMapping("/DownloadTemplateNew")
	public void DownloadTemplateNew(HttpServletRequest request, HttpServletResponse response) throws IOException {
		downloadTemplate(request, response, true);
	}
	
	/*
	 * 处理模板下载
	 */
	@RequestMapping("/DownloadTemplateHandle")
	public void DownloadTemplateHandle(HttpServletRequest request, HttpServletResponse response) throws IOException {
		downloadTemplate(request, response, false);
	}
	
	private void downloadTemplate(HttpServletRequest request, HttpServletResponse response, boolean isNew) {
		try {
			String name = isNew ? "ImportTemplate_new.xlsx" : "ImportTemplate_handle.xlsx";
	        String filePath = request.getSession().getServletContext().getRealPath("/") + "/uppda/" + name;
	        File file = new File(filePath);
			// 以流的形式下载文件。
			InputStream fis;
			fis = new BufferedInputStream(new FileInputStream(file));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();
			// 设置response的Header
			response.setContentType("application/ms-excel");
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(name.getBytes(), "iso8859-1"));
			response.addHeader("Content-Length", "" + file.length());
			OutputStream out = new BufferedOutputStream(response.getOutputStream());
			out.write(buffer);
			out.flush();
			out.close();
		} catch (Exception e) {
			logger.error("工单导入下载模板发送异常", e);
		}
	}
	
	private ExcelExtractor getExcelExtractor(MultipartFile file) {
		String originalFilename = file.getOriginalFilename();
		if (originalFilename.endsWith("xlsx")) {
			return this.excel2007Extractor;
		} else if (originalFilename.endsWith(".xls")) {
			return this.excel2003Extractor;
		}
		return null;
	}
}
