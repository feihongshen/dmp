package cn.explink.controller;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import cn.explink.dao.BranchDAO;

import cn.explink.domain.ApplyEditCartypeResultView;
import cn.explink.domain.ApplyEditCartypeVO;
import cn.explink.domain.Branch;
import cn.explink.domain.User;
import cn.explink.enumutil.ApplyEditCartypeReviewStatusEnum;

import cn.explink.enumutil.CwbOrderTypeIdEnum;

import cn.explink.service.ApplyEditCartypeService;
import cn.explink.service.BranchService;

import cn.explink.service.ExplinkUserDetail;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.ExcelUtilsHandler;
import cn.explink.util.express.Page;


@Controller
@RequestMapping("/applyediteditcartype")
public class ApplyEditCartypeController {
	
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	ApplyEditCartypeService applyEditCartypeService;
	
	@Autowired
	BranchService branchService;
	@Autowired
	BranchDAO branchDao;
	
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 获取session用户信息
	 * @return
	 */
	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}
	
	/**
	 * 跳转到订/运单货物类型修改页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/toapply")
	public String toApply(Model model)
	{
		
		return "/applyeditcartype/to_apply";

	}
	

	/**
	 * 提交货物类型修改申请
	 * @param cwbs 订/运单号
	 * @param applyCartype
	 * @return
	 */
	@RequestMapping("/applyByCwbs")
	public @ResponseBody Map<String,List<ApplyEditCartypeResultView>> applyByCwbs(
						@RequestParam(value = "cwbs", defaultValue = "", required = false)  String cwbs,
						@RequestParam(value = "applyCartype", defaultValue = "大件", required = false) String applyCartype,
						@RequestParam(value = "remark", defaultValue = "", required = false)  String remark) 
	{
		return applyEditCartypeService.applyByCwds(cwbs, getSessionUser(), applyCartype,remark);
		
	}
	
	/**
	 * 查询审核列表
	 * @param model
	 * @param page
	 * @param pageSize
	 * @param cwbs
	 * @param branchid
	 * @param applyUserid
	 * @param isReview
	 * @param reviewStatus
	 * @param startApplyTime
	 * @param endApplyTime
	 * @return
	 */
	@RequestMapping("/reviewlist")
	public String reviewList(Model model)
	{
		User user = getSessionUser();
		List<Branch> branchList = null;
		//是否为客服角色
		boolean isCS = false;
		if(user.getRoleid() == 1L){
			isCS = true;
		}
		
		if(isCS){
			branchList = branchService.getBranchs();
		}else{
			branchList = branchDao.getBranchToUser(user.getUserid());
		}
		model.addAttribute("isCS", isCS);
		model.addAttribute("startApplyTime", DateTimeUtil.getDateBefore(30));
		model.addAttribute("endApplyTime", DateTimeUtil.getNowTime());
		model.addAttribute("branchList", branchList);
		model.addAttribute("reviewStatusList", ApplyEditCartypeReviewStatusEnum.values());
		
		return "/applyeditcartype/review_list";

	}
	
  	/**
	 * 查询审核列表
	 * @param model
	 * @param page
	 * @param pageSize
	 * @param cwbs
	 * @param branchid
	 * @param applyUserid
	 * @param isReview
	 * @param reviewStatus
	 * @param startApplyTime
	 * @param endApplyTime
	 * @return
	 */
	@RequestMapping("/apiReviewList")
	@ResponseBody
	public Page<ApplyEditCartypeVO> apiReviewList(
		@RequestParam(value = "page", defaultValue = "1", required = false)  int page,
		@RequestParam(value = "pageSize", defaultValue = "10", required = false)  int pageSize,
		@RequestParam(value = "cwbs", defaultValue = "", required = false)  String cwbs,	//订单号
		@RequestParam(value = "branchid",defaultValue = "-999", required = false)  String branchid, //机构
		@RequestParam(value = "applyUserid",defaultValue = "", required = false) String applyUserid, //申请者
		@RequestParam(value = "isReview",  required = false) Boolean isReview, //审核结果
		@RequestParam(value = "reviewStatus", required = false) Integer reviewStatus,//审核状态
		@RequestParam(value = "startApplyTime", defaultValue = "", required = false) String startApplyTime, //开始申请时间
		@RequestParam(value = "endApplyTime", defaultValue = "", required = false) String endApplyTime )//结束申请时间
	{
		//处理输入的订单号，保证只把正确格式的订单号传入查询条件
		String allCwbs = formatCwbs(cwbs);
		//获取审核列表
		Page<ApplyEditCartypeVO> pageInfo = applyEditCartypeService.queryApplyEditCartype(allCwbs, branchid, applyUserid, isReview,
																					reviewStatus, startApplyTime, endApplyTime, page, pageSize);
		if(pageInfo!=null){
			formatApplyList(pageInfo.getList());
		}
	
		
		return pageInfo;

	}
	
	/**
	 * 审核申请
	 * @param model
	 * @param isReviewPass 是否审核通过
	 * @param applyIds 是否审核通过
	 * @param page
	 * @param pageSize
	 * @param cwbs
	 * @param branchid
	 * @param applyUserid
	 * @param isReview
	 * @param reviewStatus
	 * @param startApplyTime
	 * @param endApplyTime
	 * @return
	 */
	@RequestMapping("/apiReview")
	@ResponseBody
	public Map<String,List<ApplyEditCartypeResultView>> review(
		@RequestParam(value = "isReviewPass", defaultValue = "true", required = false)  boolean isReviewPass,//是否审核通过
		@RequestParam(value = "applyIds", required = false)  Long[] applyIds)//是否审核通过
	{
		Map<String,List<ApplyEditCartypeResultView>>  map = null;
		if(isReviewPass){
			map = applyEditCartypeService.reviewPass(applyIds, getSessionUser());
		}else{
			map = applyEditCartypeService.reviewDenied(applyIds, getSessionUser());
		}
		return map;
		
	}
	
	/**
	 * 按用户查询条件导出excel
	 * @param model
	 * @param response
	 * @param cwbs
	 * @param branchid
	 * @param applyUserid
	 * @param isReview
	 * @param reviewStatus
	 * @param startApplyTime
	 * @param endApplyTime
	 */
	@RequestMapping("/exportExcel")
	public void reviewList(Model model, HttpServletResponse response, 
		@RequestParam(value = "cwbs", defaultValue = "", required = false)  String cwbs,	//订单号
		@RequestParam(value = "branchid",defaultValue = "-999", required = false)  String branchid, //机构
		@RequestParam(value = "applyUserid",defaultValue = "", required = false) String applyUserid, //申请者
		@RequestParam(value = "isReview",  required = false) Boolean isReview, //审核结果
		@RequestParam(value = "reviewStatus", required = false) Integer reviewStatus,//审核状态
		@RequestParam(value = "startApplyTime", defaultValue = "", required = false) String startApplyTime, //开始申请时间
		@RequestParam(value = "endApplyTime", defaultValue = "", required = false) String endApplyTime )//结束申请时间
	{
		String allCwb = formatCwbs(cwbs);
		List<ApplyEditCartypeVO> list = applyEditCartypeService.queryApplyEditCartype(allCwb, branchid, applyUserid, isReview,
											reviewStatus, startApplyTime, endApplyTime);
		String[]cloumnZ = new String[]{"订单号","运单号","客户","订/运单类型","原货物类型","申请修改货物类型","长宽高（mm）","重量（kg）","申请机构",
											"申请人","申请时间","审核人","审核时间","审核结果","备注"};
		String[]cloumnName = new String[]{"Cwb","Transcwb","Customername","DoTypeName","OriginalCartype","ApplyCartype","Carsize","Carrealweight","ApplyBranchname",
											"ApplyUsername","ApplyTime","ReviewUsername","ReviewTime","ReviewStatusName","Remark"};
		//设置订单类型名称
		formatApplyList(list);
		try {
			ExcelUtilsHandler.exportExcelHandler(response, cloumnZ, cloumnName, "货物类型更改申请列表", "货物类型更改申请列表.xls", list);
		} catch (Exception e) {
			logger.error("", e);
		}
		
	}
	
	/**
	 * 格式化VO，暂时只设置商品类型
	 * @param list
	 */
	private void formatApplyList(List<ApplyEditCartypeVO> list){
		if(list!=null && !list.isEmpty()){
			for(ApplyEditCartypeVO vo : list){
				if(vo.getCarrealweight()!=null && vo.getCarrealweight().floatValue() <0.01f){
					vo.setCarrealweight(null);
				}
				vo.setDoTypeName(CwbOrderTypeIdEnum.getTextByValue(vo.getDoType()));
				vo.setReviewStatusName(ApplyEditCartypeReviewStatusEnum.getByValue(vo.getReviewStatus()).getText());
			}
		}
	}
	
	private String formatCwbs(String cwbs){
		StringBuffer allCwbs = new StringBuffer();
		int totalCwbNum = 0;
		if(StringUtils.isEmpty(cwbs)){
			return cwbs;
		}
		//回车分割
		String[] cwbstrs = cwbs.split("\r\n");
		for(String tempCwb : cwbstrs){
			String oneCwb = tempCwb.trim();
			if(oneCwb.equals("")){
				continue;
			}
			totalCwbNum++;
			if(totalCwbNum>1){
				allCwbs.append(",");
			}
			allCwbs.append("'").append(oneCwb).append("'");
		}

		return allCwbs.toString();
	}

	
}