package cn.explink.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.PenalizeOutDAO;
import cn.explink.dao.PenalizeTypeDAO;
import cn.explink.dao.PunishInsideDao;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.PenalizeInside;
import cn.explink.domain.PenalizeOut;
import cn.explink.domain.PenalizeType;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.PenalizeSateEnum;
import cn.explink.enumutil.PunishInsideStateEnum;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.PenalizeOutService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.Page;

@Controller
@RequestMapping("/penalizeOut")
public class PenalizeOutController {

	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	PenalizeTypeDAO penalizeTypeDAO;
	@Autowired
	PenalizeOutDAO penalizeOutDAO;
	@Autowired
	PunishInsideDao punishInsideDao;
	@Autowired
	PenalizeOutService penalizeOutService;

	//private Logger logger = LoggerFactory.getLogger(this.getClass());
	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}
	@RequestMapping("/list/{page}")
	public String list(@PathVariable("page") long page,@RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs,
			@RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid,
			@RequestParam(value = "penalizeOutbig", required = false, defaultValue = "0") int penalizeOutbig,
			@RequestParam(value = "penalizeOutsmall", required = false, defaultValue = "0") int penalizeOutsmall,
			@RequestParam(value = "penalizeState", required = false, defaultValue = "0") int penalizeState,
			@RequestParam(value = "flowordertype", required = false, defaultValue = "0") long flowordertype,
			@RequestParam(value = "starttime", required = false, defaultValue = "") String starttime,
			@RequestParam(value = "endtime", required = false, defaultValue = "") String endtime,
			@RequestParam(value = "isnow", required = false, defaultValue = "0") int isnow,
			Model model) {
		List<Customer>  customerList= this.customerDAO.getAllCustomers();
		List<Branch> branchList=this.branchDAO.getBranchAllzhandian(BranchEnum.ZhanDian.getValue()+","+BranchEnum.KeFu.getValue());
		List<User> userList=this.userDAO.getAllUser();
		List<PenalizeType> penalizebigList=this.penalizeTypeDAO.getPenalizeTypeByType(1);
		List<PenalizeType> penalizesmallList=this.penalizeTypeDAO.getPenalizeTypeByType(2);
		StringBuilder cwbstr = new StringBuilder("");
		BigDecimal penalizeOutfeeSum=new BigDecimal(0);
		if (cwbs.length() > 0) {
			for (String cwbStr : cwbs.split("\r\n")) {
				if (cwbStr.trim().length() == 0) {
					continue;
				}
				cwbstr = cwbstr.append("'").append(cwbStr.trim()).append("',");
			}
			if (cwbstr.length() > 0) {
				cwbstr = cwbstr.deleteCharAt(cwbstr.lastIndexOf(","));
			}
		}
		List<PenalizeOut> penalizeOutList=new ArrayList<PenalizeOut>();
		int count=0;
		if(isnow>0){
		 penalizeOutList=this.penalizeOutDAO.getPenalizeOutByList(cwbstr.toString(), flowordertype, customerid, penalizeOutbig, penalizeOutsmall,penalizeState, starttime, endtime, page);
		 count=this.penalizeOutDAO.getPenalizeOutByListCount(cwbstr.toString(), flowordertype, customerid, penalizeOutbig, penalizeOutsmall,penalizeState, starttime, endtime);
		 Page page_obj = new Page(count, page, Page.ONE_PAGE_NUMBER);
		 model.addAttribute("page", page);
		 model.addAttribute("page_obj", page_obj);
		 penalizeOutfeeSum=this.penalizeOutDAO.getPenalizeOutByPenalizeOutfeeSum(cwbstr.toString(), flowordertype, customerid, penalizeOutbig, penalizeOutsmall,penalizeState, starttime, endtime);
		}
		model.addAttribute("branchList", branchList);
		model.addAttribute("customerList", customerList);
		model.addAttribute("penalizebigList", penalizebigList);
		model.addAttribute("penalizesmallList", penalizesmallList);
		model.addAttribute("flowordertypes",FlowOrderTypeEnum.values());
		model.addAttribute("penalizeSates",PenalizeSateEnum.values());
		model.addAttribute("penalizeOutList",penalizeOutList);
		model.addAttribute("userList",userList);
		model.addAttribute("penalizeSateEnums",PenalizeSateEnum.values());
		model.addAttribute("penalizeOutfeeSum",penalizeOutfeeSum);
		/**--数据回显--**/
		model.addAttribute("cwbs",cwbs);
		model.addAttribute("cwbstr",cwbstr.toString());
		model.addAttribute("customerid",customerid);
		model.addAttribute("penalizeOutbig",penalizeOutbig);
		model.addAttribute("penalizeOutsmall",penalizeOutsmall);
		model.addAttribute("flowordertype",flowordertype);
		model.addAttribute("penalizeState",penalizeState);
		model.addAttribute("starttime",starttime);
		model.addAttribute("endtime",endtime);
		return "/penalize/penalizeOut/list";
	}

	@RequestMapping("/addpenalizeOut")
	public String addpenalizeOut(Model model) throws Exception {
		List<PenalizeType> penalizebigList=this.penalizeTypeDAO.getPenalizeTypeByType(1);
		List<PenalizeType> penalizesmallList=this.penalizeTypeDAO.getPenalizeTypeByType(2);
		model.addAttribute("penalizebigList", penalizebigList);
		model.addAttribute("penalizesmallList", penalizesmallList);
		return "/penalize/penalizeOut/addpenalizeOut";
	}
	@RequestMapping("/cancelpenalizeOut/{id}")
	public String cancelpenalizeOut(@PathVariable("id") int penalizeOutId ,Model model) throws Exception {
		PenalizeOut penalizeOut=this.penalizeOutDAO.getPenalizeOutByPenalizeOutId(penalizeOutId);
		List<Customer>  customerList= this.customerDAO.getAllCustomers();
		List<PenalizeType> penalizeTypeList=this.penalizeTypeDAO.getAllPenalizeType();
		String penalizeOutbigStr="";
		String penalizeOutsmallStr="";
		String customername="";
		String flowordertypeText="";
		for (PenalizeType pType : penalizeTypeList) {
			if (pType.getId() == penalizeOut.getPenalizeOutbig()) {
				penalizeOutbigStr = pType.getText();
			}
			if (pType.getId() == penalizeOut.getPenalizeOutsmall()) {
				penalizeOutsmallStr = pType.getText();
			}
		}
		for(Customer cus:customerList)
		{
			if(cus.getCustomerid()==penalizeOut.getCustomerid())
			{
				customername=cus.getCustomername();
			}
		}
		for(FlowOrderTypeEnum flow:FlowOrderTypeEnum.values())
		{
			if(flow.getValue()==penalizeOut.getFlowordertype())
			{
				flowordertypeText=flow.getText();
			}
		}
		model.addAttribute("penalizeOutbigStr",penalizeOutbigStr);
		model.addAttribute("penalizeOutsmallStr", penalizeOutsmallStr);
		model.addAttribute("customername", customername);
		model.addAttribute("flowordertypeText", flowordertypeText);
		model.addAttribute("penalizeOut", penalizeOut);
		return "/penalize/penalizeOut/cancelpenalizeOut";
	}
	@RequestMapping("/addpenalizeIn/{id}")
	public String addpenalizeIn(@PathVariable("id") int penalizeOutId ,Model model) throws Exception {
		List<Branch> branchList=this.branchDAO.getBranchAllzhandian(BranchEnum.ZhanDian.getValue()+","+BranchEnum.KeFu.getValue());
		PenalizeOut penalizeOut=this.penalizeOutDAO.getPenalizeOutByPenalizeOutId(penalizeOutId);
		List<Customer>  customerList= this.customerDAO.getAllCustomers();
		List<PenalizeType> penalizeTypeList=this.penalizeTypeDAO.getAllPenalizeType();
		String penalizeOutbigStr="";
		String penalizeOutsmallStr="";
		String customername="";
		String flowordertypeText="";
		for (PenalizeType pType : penalizeTypeList) {
			if (pType.getId() == penalizeOut.getPenalizeOutbig()) {
				penalizeOutbigStr = pType.getText();
			}
			if (pType.getId() == penalizeOut.getPenalizeOutsmall()) {
				penalizeOutsmallStr = pType.getText();
			}
		}
		for(Customer cus:customerList)
		{
			if(cus.getCustomerid()==penalizeOut.getCustomerid())
			{
				customername=cus.getCustomername();
			}
		}
		for(FlowOrderTypeEnum flow:FlowOrderTypeEnum.values())
		{
			if(flow.getValue()==penalizeOut.getFlowordertype())
			{
				flowordertypeText=flow.getText();
			}
		}
		model.addAttribute("penalizeOutbigStr",penalizeOutbigStr);
		model.addAttribute("penalizeOutsmallStr", penalizeOutsmallStr);
		model.addAttribute("customername", customername);
		model.addAttribute("flowordertypeText", flowordertypeText);
		model.addAttribute("penalizeOut", penalizeOut);
		model.addAttribute("branchList", branchList);


		return "/penalize/penalizeOut/addpenalizeIn";
	}
	@RequestMapping("/initaddpenalizeOut")
	public @ResponseBody Map<String, String>  initaddpenalizeOut(Model model,@RequestParam(value = "cwb", required = false, defaultValue = "") String cwb) throws Exception {
		CwbOrder cwbOrder=null;
		if(cwb.trim().length()>0) {
			cwbOrder=this.cwbDAO.getCwbByCwb(cwb);
		}
		Map<String, String> map=new HashMap<String, String>();
		if(cwbOrder!=null){
		map.put("flowordertyleValue", cwbOrder.getFlowordertype()+"");
		map.put("flowordertyleText", FlowOrderTypeEnum.getText(cwbOrder.getFlowordertype()).getText());
		map.put("customerid", cwbOrder.getCustomerid()+"");
		map.put("customername", this.customerDAO.getCustomerById(cwbOrder.getCustomerid()).getCustomername());
		map.put("receivablefee", cwbOrder.getReceivablefee()+"");
		}
		return map;
	}
	@RequestMapping("/addpenalizeOutData")
	public @ResponseBody String  addpenalizeOutData(PenalizeOut out, Model model) throws Exception {
		if(out!=null){
			PenalizeOut penalizeOut=this.penalizeOutDAO.getPenalizeOutByIsNull(out.getCwb(), out.getPenalizeOutsmall(),out.getPenalizeOutfee());
			if(penalizeOut!=null)
			{
				return "{\"errorCode\":1,\"error\":\"该记录已经存在！\"}";
			}
			if(this.cwbDAO.getCwbByCwb(out.getCwb())==null){
				return "{\"errorCode\":1,\"error\":\"订单不存在！\"}";
			}
			if(out.getPenalizeOutfee()==null){
				return "{\"errorCode\":1,\"error\":\"赔付金额不能为空！\"}";
			}
			if(out.getReceivablefee().compareTo(out.getPenalizeOutfee())==-1)
			{
				return "{\"errorCode\":1,\"error\":\"赔付金额不能大于订单金额！\"}";
			}
			if(out.getPenalizeOutbig()==0)
			{
				return "{\"errorCode\":1,\"error\":\"赔付大类不能为空！\"}";
			}
			if(out.getPenalizeOutsmall()==0)
			{
				return "{\"errorCode\":1,\"error\":\"赔付小类不能为空！\"}";
			}
			if(out.getPenalizeOutContent().trim().length()<=0)
			{
				return "{\"errorCode\":1,\"error\":\"赔付说明不能为空！\"}";
			}
			out.setCreateruser(this.getSessionUser().getUserid());
			out.setPenalizeOutstate(PenalizeSateEnum.Successful.getValue());
			int count=this.penalizeOutDAO.crePenalizeOut(out);
			if(count==1){
				return "{\"errorCode\":0,\"error\":\"创建成功！\"}";
			}
		}
		return "";
	}
	@RequestMapping("/addpenalizeInData")
	public @ResponseBody String  addpenalizeInData(
			@RequestParam(value = "penalizeOutId", required = false, defaultValue = "0") int penalizeOutId,
			@RequestParam(value = "dutybranchid", required = false, defaultValue = "") int dutybranchid,
			@RequestParam(value = "punishdescribe", required = false, defaultValue = "") String punishdescribe,
			@RequestParam(value = "punishInsideprice", required = false, defaultValue = "0") BigDecimal punishInsideprice,
			@RequestParam(value = "dutypersonname", required = false, defaultValue = "0") String dutypersonname,
			 Model model) throws Exception {
		PenalizeOut out=this.penalizeOutDAO.getPenalizeOutByPenalizeOutId(penalizeOutId);
		if (out != null) {
			User user=null;
			PenalizeInside penalizeInside=new PenalizeInside();
			if((dutypersonname!=null)&&(dutypersonname.trim().length()>0)) {
				user=this.userDAO.getUsersByRealnameAndBranchid(dutypersonname.trim(), dutybranchid);
				if(user!=null){
					penalizeInside.setDutypersonid(user.getUserid());
				}
				else{
					return "{\"errorCode\":1,\"error\":\"创建对内扣罚失败！责任人不存在,或已离职\"}";
				}
			}
			penalizeInside.setCwb(out.getCwb());
			penalizeInside.setCreateBySource(1);
			penalizeInside.setSourceNo(out.getCwb());
			penalizeInside.setDutybranchid(dutybranchid);
			penalizeInside.setCwbstate(out.getFlowordertype());
			penalizeInside.setCwbPrice(out.getReceivablefee());
			penalizeInside.setPunishInsideprice(punishInsideprice);
			penalizeInside.setPunishbigsort(out.getPenalizeOutbig());
			penalizeInside.setPunishsmallsort(out.getPenalizeOutsmall());
			penalizeInside.setCreateuserid(this.getSessionUser().getUsercustomerid());
			penalizeInside.setPunishdescribe(punishdescribe);
			penalizeInside.setPunishcwbstate(PunishInsideStateEnum.chuangjian.getValue());
			penalizeInside.setCreDate(DateTimeUtil.getNowTime());
			try {
			List<PenalizeInside> sInsideList=this.punishInsideDao.getPenalizeInsideIsNull(out.getCwb(), dutybranchid, out.getPenalizeOutsmall());
				if ((sInsideList != null)&&(sInsideList.size()>0)) {
					return "{\"errorCode\":1,\"error\":\"对内扣罚创建失败！ 对内扣罚已经创建\"}";
				} else {

					this.punishInsideDao.createPunishInside(penalizeInside);
					return "{\"errorCode\":0,\"error\":\"创建对内扣罚成功！\"}";
				}
			} catch (Exception e) {
				return "{\"errorCode\":1,\"error\":\"创建对内扣罚失败！\"}";
			}
		}

		 return "{\"errorCode\":1,\"error\":\"创建对内扣罚失败！\"}";
	}
	@RequestMapping("/cancelpenalizeOutData")
	public @ResponseBody String  cancelpenalizeOutData(
			@RequestParam(value = "cancelContent", required = false, defaultValue = "") String cancelContent,
			@RequestParam(value = "penalizeOutId", required = false, defaultValue = "0") int penalizeOutId,
			Model model) throws Exception {
		if(penalizeOutId>0){
			int  couns=this.penalizeOutDAO.cancelpenalizeOutDataById(penalizeOutId,PenalizeSateEnum.Cancel.getValue(),cancelContent);
			if(couns>0)
			{
				return "{\"errorCode\":0,\"error\":\"撤销对外赔付成功！\"}";
			}
		}
		return "{\"errorCode\":1,\"error\":\"撤销对外赔付失败！\"}";
	}
	@RequestMapping("/exportExcel")
	public void exportExcle(HttpServletResponse response,
			@RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs,
			@RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid,
			@RequestParam(value = "penalizeOutbig", required = false, defaultValue = "0") int penalizeOutbig,
			@RequestParam(value = "penalizeOutsmall", required = false, defaultValue = "0") int penalizeOutsmall,
			@RequestParam(value = "penalizeState", required = false, defaultValue = "0") int penalizeState,
			@RequestParam(value = "flowordertype", required = false, defaultValue = "0") long flowordertype,
			@RequestParam(value = "starttime", required = false, defaultValue = "") String starttime,
			@RequestParam(value = "endtime", required = false, defaultValue = "") String endtime) {

		this.penalizeOutService.exportExcels(response, cwbs, customerid, penalizeOutbig, penalizeOutsmall, penalizeState, flowordertype, starttime, endtime);
	}

}