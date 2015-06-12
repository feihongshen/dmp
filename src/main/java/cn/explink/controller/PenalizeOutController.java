package cn.explink.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.PenalizeOut;
import cn.explink.domain.PenalizeType;
import cn.explink.domain.User;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.PenalizeSateEnum;
import cn.explink.service.ExplinkUserDetail;
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
	@RequestMapping("/cancelpenalizeOut")
	public String cancelpenalizeOut(Model model) throws Exception {

		return "/penalize/penalizeOut/cancelpenalizeOut";
	}
	@RequestMapping("/addpenalizeIn")
	public String addpenalizeIn(Model model) throws Exception {
		List<Branch> branchList=this.branchDAO.getBranchAllzhandian(BranchEnum.ZhanDian.getValue()+","+BranchEnum.KeFu.getValue());
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
}