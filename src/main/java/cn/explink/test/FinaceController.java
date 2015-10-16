package cn.explink.test;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.explink.dao.BranchDAO;
import cn.explink.domain.Branch;
import cn.explink.enumutil.BranchEnum;

@Controller
@RequestMapping("/finace")
public class FinaceController {
	@Autowired
	FinaceDAO finaceDAO;
	@Autowired
	FinaceService finaceService;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	AccountService accountService;
	
	private Logger logger = LoggerFactory.getLogger(FinaceController.class);

	
	@RequestMapping("/produceTxt")
	public String produceTxt(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "deliverybranchid", required = false, defaultValue = "0") long  deliverybranchid,
			@RequestParam(value = "chongzhiAmount", required = false, defaultValue = "0") double  chongzhiAmount) 
	{
		List<Branch> branchlist=branchDAO.getBanchByBranchidForStock(BranchEnum.ZhanDian.getValue()+"");
		request.setAttribute("branchlist",branchlist);
		accountService.calc(deliverybranchid, chongzhiAmount,request);
		return "finance/deliveryBranchFinace";

	}
	
	
	@RequestMapping("/")
	public String page(HttpServletRequest request) 
	{
		List<Branch> branchlist=branchDAO.getBanchByBranchidForStock(BranchEnum.ZhanDian.getValue()+"");
		request.setAttribute("branchlist",branchlist);
		return "finance/deliveryBranchFinace";

	}

	@RequestMapping("/download_detail")
	public void download_detail(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "deliverybranchid", required = false, defaultValue = "0") long  deliverybranchid,
			@RequestParam(value = "deliverystate", required = false, defaultValue = "0") long  deliverystate) throws Exception 
	{
		finaceService.download_detail(deliverybranchid, deliverystate,response);
		
	}
	
	
	@RequestMapping("/koukuan_diff")
	public void koukuan_diff(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "deliverybranchid", required = false, defaultValue = "0") long  deliverybranchid,
			@RequestParam(value = "deliverystate", required = false, defaultValue = "0") long  deliverystate) throws Exception 
	{
		finaceService.koukuan_diff(deliverybranchid, deliverystate,response);
		
	}
	
	
	
	@RequestMapping("/jushouzhongzhuan_detail")
	public void jushouzhongzhuan_detail(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "deliverybranchid", required = false, defaultValue = "0") long  deliverybranchid,
			@RequestParam(value = "flowordertype", required = false, defaultValue = "0") long  flowordertype) throws Exception 
	{
		finaceService.jushouzhongzhuan_detail(deliverybranchid, flowordertype,response);
		
	}
	
	@RequestMapping("/kkqzckdiffdetail_detail")
	public void kkqzckdiffdetail_detail(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "deliverybranchid", required = false, defaultValue = "0") long  deliverybranchid,
			@RequestParam(value = "flowordertype", required = false, defaultValue = "0") long  flowordertype) throws Exception 
	{
		finaceService.kkqzckdiffdetail_detail(deliverybranchid, flowordertype,response);
		
	}
	
	
	/**
	 * 导出差异订单
	 * @param request
	 * @param response
	 * @param deliverybranchid
	 * @param flowordertype
	 * @throws Exception
	 */
	@RequestMapping("/exportdiff")
	public void exportdiff(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "deliverybranchid", required = false, defaultValue = "0") long  deliverybranchid,
			@RequestParam(value = "flowordertype", required = false, defaultValue = "0") long  flowordertype,
			@RequestParam(value = "datadiff", required = false, defaultValue = "0") double  datadiff) throws Exception 
	{
		finaceService.download_diff(deliverybranchid, flowordertype,datadiff,response);
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@RequestMapping("/jushouzhongzhuan_diff")
	public void jushouzhongzhuan_diff(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "deliverybranchid", required = false, defaultValue = "0") long  deliverybranchid,
			@RequestParam(value = "flowordertype", required = false, defaultValue = "0") long  flowordertype) throws Exception 
	{
		finaceService.jushouzhongzhuan_diff(deliverybranchid, flowordertype,response);
		
	}
	
	@RequestMapping("/koukuanjushouzhongzhuan_diff")
	public void koukuanjushouzhongzhuan_diff(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "deliverybranchid", required = false, defaultValue = "0") long  deliverybranchid) throws Exception 
	{
		finaceService.koukuanjushouzhongzhuan_diff(deliverybranchid,response);
		
	}

}
