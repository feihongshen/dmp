package cn.explink.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.explink.dao.AbnormalOrderDAO;
import cn.explink.dao.AbnormalTypeDAO;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.PenalizeTypeDAO;
import cn.explink.dao.PunishInsideDao;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.UserDAO;
import cn.explink.dao.WorkOrderDAO;
import cn.explink.domain.AbnormalOrder;
import cn.explink.domain.AbnormalType;
import cn.explink.domain.Branch;
import cn.explink.domain.CsComplaintAccept;
import cn.explink.domain.PenalizeInside;
import cn.explink.domain.PenalizeInsideShenhe;
import cn.explink.domain.PenalizeType;
import cn.explink.domain.PunishGongdanView;
import cn.explink.domain.Reason;
import cn.explink.domain.User;
import cn.explink.enumutil.PunishInsideStateEnum;
import cn.explink.service.AbnormalService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.service.PunishInsideService;
import cn.explink.util.ExcelUtils;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@Controller
@RequestMapping("/inpunish")
public class PunishInsideController {
	@Autowired
	SystemInstallDAO systemInstallDAO;
	@Autowired
	PenalizeTypeDAO penalizeTypeDAO;
	@Autowired
	WorkOrderDAO workOrderDAO;
	@Autowired
	ReasonDao reasonDao;
	@Autowired
	ExportService exportService;
	@Autowired
	AbnormalTypeDAO abnormalTypeDAO;
	@Autowired
	AbnormalOrderDAO abnormalOrderDAO;
	@Autowired
	PunishInsideDao punishInsideDao;
	@Autowired
	PunishInsideService punishInsideService;
	@Autowired
	AbnormalService abnormalService;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}
	//进入对内扣罚主页面
	@RequestMapping("/inpunishdetail/{page}")
	public String inpunishdetail(Model model,HttpServletRequest request,HttpServletResponse response,
			@PathVariable(value="page")long page,
			@RequestParam(value="cwb",defaultValue="",required=false)String cwb,
			@RequestParam(value="dutybranchid",defaultValue="0",required=false)long dutybranchid,
			@RequestParam(value="cwbpunishtype",defaultValue="0",required=false)long cwbpunishtype,
			@RequestParam(value="dutyname",defaultValue="0",required=false)long dutynameid,
			@RequestParam(value="cwbstate",defaultValue="0",required=false)long cwbstate,
			@RequestParam(value="punishbigsort",defaultValue="0",required=false)long punishbigsort,
			@RequestParam(value="punishsmallsort",defaultValue="0",required=false)long punishsmallsort,
			@RequestParam(value="begindate",defaultValue="",required=false)String begindate,
			@RequestParam(value="enddate",defaultValue="",required=false)String enddate,
			@RequestParam(value="isshow",defaultValue="0",required=false)long isshow
			){
			List<PenalizeType> penalizebigList = this.penalizeTypeDAO.getPenalizeTypeByType(1);
			List<PenalizeType> penalizesmallList = this.penalizeTypeDAO.getPenalizeTypeByType(2);
			List<AbnormalType> atlist = this.abnormalTypeDAO.getAllAbnormalTypeByName();
			String quot = "'", quotAndComma = "',";
			StringBuffer cwbs1 = new StringBuffer();
			if (cwb.length() > 0) {
			for (String cwbStr : cwb.split("\r\n")) {
				if (cwbStr.trim().length() == 0) {
					continue;
				}
				cwbs1 = cwbs1.append(quot).append(cwbStr.trim()).append(quotAndComma);
				}
			}
			String cwbs = "";
			if (cwbs1.length() > 0) {
				cwbs = cwbs1.substring(0, cwbs1.length() - 1);
			}
			List<PenalizeInside> penalizeInsides=new ArrayList<PenalizeInside>();
			String punishsumprice="";
			int count=0;
			//isshow为1时为查询的时候，为0时为进入页面的时候
			if (isshow==1) {
				penalizeInsides=punishInsideDao.findByCondition(page,cwbs,dutybranchid,cwbpunishtype,dutynameid,cwbstate,punishbigsort,punishsmallsort,begindate,enddate);
				count=punishInsideDao.findByConditionSum(cwbs, dutybranchid, cwbpunishtype, dutynameid, cwbstate, punishbigsort, punishsmallsort, begindate, enddate);
				punishsumprice=punishInsideDao.calculateSumPrice(cwbs, dutybranchid, cwbpunishtype, dutynameid, cwbstate, punishbigsort, punishsmallsort, begindate, enddate);
			}
			User user=this.getSessionUser();
			List<Branch> branchs = this.branchDAO.getAllEffectBranches();
			List<User> users = this.userDAO.getAllUser();
			List<Reason> lr=reasonDao.addWO();
			model.addAttribute("branchList", branchs);
			model.addAttribute("users", users);
			model.addAttribute("page", page);
			model.addAttribute("page_obj", new Page(count, page, Page.ONE_PAGE_NUMBER));//需要做一下修改
			model.addAttribute("sitetype", this.branchDAO.getBranchById(this.getSessionUser().getBranchid()).getSitetype());
			model.addAttribute("userid", user.getUserid());
			model.addAttribute("roleid", user.getRoleid());
			model.addAttribute("penalizeInsides", penalizeInsides);
			model.addAttribute("abnormalTypeList", atlist);
			model.addAttribute("lr", lr);
			model.addAttribute("punishsumprice", punishsumprice);
			model.addAttribute("penalizebigList", penalizebigList);
			model.addAttribute("penalizesmallList", penalizesmallList);
			return "penalize/penalizeIn/list";
	}
	//进入根据订单创建对内扣罚单的弹出框
	@RequestMapping("/createinpunishbycwb")
	public String createinpunishbycwb(Model model){
		List<Branch> branchs = this.branchDAO.getAllEffectBranches();
		List<PenalizeType> penalizebigList = this.penalizeTypeDAO.getPenalizeTypeByType(1);
		List<PenalizeType> penalizesmallList = this.penalizeTypeDAO.getPenalizeTypeByType(2);
		model.addAttribute("branchList", branchs);
		model.addAttribute("penalizebigList", penalizebigList);
		model.addAttribute("penalizesmallList", penalizesmallList);
		return "penalize/penalizeIn/createbycwb";
		
	}

	//根据订单创建对内扣罚单（无附件上传）
	@RequestMapping("/createbycwbwithoutfile")
	public @ResponseBody String createbycwbwithoutfile(HttpServletRequest request){
		try {
			String cwb = StringUtil.nullConvertToEmptyString(request.getParameter("cwb")).trim();
			PenalizeInside penalizeInsidesingle=punishInsideDao.getInsidebycwb(cwb);
			if (penalizeInsidesingle!=null) {
				return "{\"errorCode\":1,\"error\":\"该订单已经生成扣罚单\"}";
			}
			PenalizeInside penalizeInside=punishInsideService.changePageValue(request);
			penalizeInside.setFileposition("");
			//将相关的信息存入表中
			punishInsideDao.createPunishInside(penalizeInside);
			return "{\"errorCode\":0,\"error\":\"操作成功\"}";
			} catch (Exception e) {
			return "{\"errorCode\":1,\"error\":\"操作失败\"}";
		}
	}
	//根据订单创建对内扣罚单（有附件上传）
	@RequestMapping("/createbycwbwithfile")
	public @ResponseBody String createbycwbwithfile(HttpServletRequest request,
			@RequestParam(value = "Filedata", required = false) MultipartFile file
			){
		try {
			String cwb = StringUtil.nullConvertToEmptyString(request.getParameter("cwb")).trim();
			PenalizeInside penalizeInsidesingle=punishInsideDao.getInsidebycwb(cwb);
			if (penalizeInsidesingle!=null) {
				return "{\"errorCode\":1,\"error\":\"该订单已经生成扣罚单\"}";
			}
			PenalizeInside penalizeInside=punishInsideService.changePageValue(request);
			//获得上传文件的文件名
			String filename=abnormalService.loadexceptfile(file);
			penalizeInside.setFileposition(filename);
			punishInsideDao.createPunishInside(penalizeInside);
			return "{\"errorCode\":0,\"error\":\"操作成功\"}";
			} catch (Exception e) {
			return "{\"errorCode\":1,\"error\":\"操作失败\"}";
		}
		
	}

	//带文件的根据工单创建扣罚单表单提交
	@RequestMapping("/submitPunishCreateBygongdanLoadfile")
	public @ResponseBody String submitPunishCreateBygongdanLoadfile(HttpServletRequest request,
			@RequestParam(value = "Filedata", required = false) MultipartFile file
			){
		try {
			String type1=StringUtil.nullConvertToEmptyString(request.getParameter("type1"));
			//订单号
			String cwbhhh=StringUtil.nullConvertToEmptyString(request.getParameter("cwbhhh"+type1));
			PenalizeInside penalizeInside1=punishInsideDao.getInsidebycwb(cwbhhh);
			if (penalizeInside1!=null) {
				return "{\"errorCode\":0,\"error\":\"该问题件已经创建过对内扣罚单，不能再次创建\"}";
			}
			PenalizeInside penalizeInside=punishInsideService.switchTowantDataWithQuestion(request);
			String filepath=punishInsideService.loadexceptfile(file);
			penalizeInside.setFileposition(filepath);
			return "{\"errorCode\":0,\"error\":\"操作成功\"}";
		} catch (Exception e) {
			this.logger.error("根据工单创建对内扣罚单的时候出现异常", e);
		return "{\"errorCode\":1,\"error\":\"操作失败\"}";
		}
	}
	//不带文件的根据工单创建扣罚单
	@RequestMapping("/submitPunishCreateBygongdanLoad")
	public @ResponseBody String submitPunishCreateBygongdanLoad(HttpServletRequest request,Model model){
		try{
		String type1=StringUtil.nullConvertToEmptyString(request.getParameter("type1"));
		//订单号
		String cwbhhh=StringUtil.nullConvertToEmptyString(request.getParameter("cwbhhh"+type1));
		PenalizeInside penalizeInside1=punishInsideDao.getInsidebycwb(cwbhhh);
		if (penalizeInside1!=null) {
			return "{\"errorCode\":0,\"error\":\"该工单已经创建过对内扣罚单，不能再次创建\"}";
		}
		PenalizeInside penalizeInside=punishInsideService.switchTowantDataWithQuestion(request);
		punishInsideDao.createPunishInside(penalizeInside);
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
		} catch (Exception e) {
			return "{\"errorCode\":1,\"error\":\"操作失败\"}";
		}

	} 
	//进入申诉的页面
	@RequestMapping("/shensupage")
	public   String shensupage(Model model,@RequestParam(value="ids",defaultValue="",required=false)String ids){
		model.addAttribute("ids", ids);
		
		return "penalize/penalizeIn/shensupunish";
	}
	//带文件的申诉处理
	@RequestMapping("/submitPunishShensufile")
	public @ResponseBody String submitPunishShensufile(
			@RequestParam(value="shensutype",required=false,defaultValue="")String shensutype,
			@RequestParam(value="describe",required=false,defaultValue="")String describe,
			@RequestParam(value="ids",required=false,defaultValue="")String ids,
			@RequestParam(value = "Filedata", required = false) MultipartFile file
			){
		long punishinsidestate=PunishInsideStateEnum.daishenhe.getValue();
		User user=this.getSessionUser();
		long count=0;
		String filepath=punishInsideService.loadexceptfile(file);
		String[] arrayIds = ids.split(",");
		for (int i = 0; i < arrayIds.length; i++) {
			try {
				if (punishInsideService.checkisshenhe(Long.parseLong(arrayIds[i]))) {
					count++;
					continue;
				}
				if (!punishInsideService.switchhourTomill(Long.parseLong(arrayIds[i]))) {
					count++;
					continue;
				}
				punishInsideDao.updateShensuPunishInside(Integer.parseInt(arrayIds[i]),Integer.parseInt(shensutype),describe,filepath,user.getUserid(),punishinsidestate);
			} catch (NumberFormatException e) {
				count++;
			}
		}
		if (count==0) {
			return "{\"errorCode\":0,\"error\":\"申诉操作全部成功\"}";
		}else if (count==arrayIds.length) {
			return "{\"errorCode\":1,\"error\":\"申诉操作全部失败(可能由于订单已经超出时效或者已经操作审核)\"}";

		}else {
			return "{\"errorCode\":1,\"error\":\"申诉操作部分成功（可能由于订单已经超出时效或者已经操作审核）\"}";
		}
		
	}
	//不带文件的申诉处理
	@RequestMapping("/submitPunishShensu")
	public @ResponseBody String submitPunishShensu(
			@RequestParam(value="shensutype",required=false,defaultValue="")String shensutype,
			@RequestParam(value="describe",required=false,defaultValue="")String describe,
			@RequestParam(value="ids",required=false,defaultValue="")String ids
			){
		User user=this.getSessionUser();
		long count=0;
		String[] arrayIds = ids.split(",");
		for (int i = 0; i < arrayIds.length; i++) {
			try {
				if (punishInsideService.checkisshenhe(Long.parseLong(arrayIds[i]))) {
					count++;
					continue;
				}
				if (!punishInsideService.switchhourTomill(Long.parseLong(arrayIds[i]))) {
					count++;
					continue;
				}
				long punishinsidestate=PunishInsideStateEnum.daishenhe.getValue();
				punishInsideDao.updateShensuPunishInside(Integer.parseInt(arrayIds[i]),Integer.parseInt(shensutype),describe,"",user.getUserid(),punishinsidestate);
			} catch (NumberFormatException e) {
				count++;
			}
		}
		if (count==0) {
			return "{\"errorCode\":0,\"error\":\"申诉操作全部成功\"}";
		}else if (count==arrayIds.length) {
			return "{\"errorCode\":1,\"error\":\"申诉操作全部失败(可能由于订单已经超出时效或者已经操作审核)\"}";

		}else {
			return "{\"errorCode\":1,\"error\":\"申诉操作部分成功（可能由于订单已经超出时效或者已经操作审核）\"}";
		}
	}
	//在根据工单创建扣罚单的页面查询问题件的想关信息
	@RequestMapping("/createinpunishbyQuestNo")
	public @ResponseBody List<AbnormalPunishView> createinpunishbyQuestNo(
			@RequestParam(value="cwb",defaultValue="",required=false)String cwb,
			@RequestParam(value="wenticwb",defaultValue="",required=false)String wenticwb,
			@RequestParam(value="wentitype",defaultValue="0",required=false)long wentitype,
			@RequestParam(value="wentistate",defaultValue="0",required=false)long wentistate,
			@RequestParam(value="wentibegindateh",defaultValue="",required=false)String wentibegindateh,
			@RequestParam(value="wentienddateh",defaultValue="",required=false)String wentienddateh
			){
		List<AbnormalOrder> abnormalOrders=abnormalOrderDAO.getAbnormalToCreateKoufaInside(cwb,wenticwb,wentitype,wentistate,wentibegindateh,wentienddateh);
		List<AbnormalPunishView> abnormlaList=punishInsideService.changeWantData(abnormalOrders);
		return abnormlaList;
	}
	//根据问题件创建扣罚单(不带文件)
	@RequestMapping("/submitPunishCreateBywentijian")
	public @ResponseBody String submitPunishCreateBywentijian(
			HttpServletRequest request
			){
		try {
			String type1=StringUtil.nullConvertToEmptyString(request.getParameter("type1"));
			//订单号
			String cwbhhh=StringUtil.nullConvertToEmptyString(request.getParameter("cwbhhh"+type1));
			PenalizeInside penalizeInside1=punishInsideDao.getInsidebycwb(cwbhhh);
			if (penalizeInside1!=null) {
				return "{\"errorCode\":0,\"error\":\"该问题件已经创建过对内扣罚单，不能再次创建\"}";
			}
			PenalizeInside penalizeInside=punishInsideService.switchTowantDataWithQuestion(request);
			punishInsideDao.createPunishInside(penalizeInside);
			return "{\"errorCode\":0,\"error\":\"操作成功\"}";
		} catch (Exception e) {
		return "{\"errorCode\":1,\"error\":\"操作失败\"}";
		}
	}
	//根据问题件创建扣罚单(带文件)
	@RequestMapping("/submitPunishCreateBywentijianfile")
	public @ResponseBody String submitPunishCreateBywentijianfile(HttpServletRequest request,
			@RequestParam(value = "Filedata", required = false) MultipartFile file
			){
		try {
			String type1=StringUtil.nullConvertToEmptyString(request.getParameter("type1"));
			//订单号
			String cwbhhh=StringUtil.nullConvertToEmptyString(request.getParameter("cwbhhh"+type1));
			PenalizeInside penalizeInside1=punishInsideDao.getInsidebycwb(cwbhhh);
			if (penalizeInside1!=null) {
				return "{\"errorCode\":0,\"error\":\"该问题件已经创建过对内扣罚单，不能再次创建\"}";
			}
			PenalizeInside penalizeInside=punishInsideService.switchTowantDataWithQuestion(request);
			String filepath=punishInsideService.loadexceptfile(file);
			penalizeInside.setFileposition(filepath);
			return "{\"errorCode\":0,\"error\":\"操作成功\"}";
		} catch (Exception e) {
			this.logger.error("根据问题创建对内扣罚单的时候出现异常", e);
		return "{\"errorCode\":1,\"error\":\"操作失败\"}";
		}
		
	}
	/**
	 * 进入审核页面
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping("/shenhepage")
	public String shenhepage(Model model,@RequestParam(value="id",defaultValue="",required=false)long id){
		PenalizeInside penalizeInside=punishInsideDao.getInsidebyid(id);
		PenalizeInsideView penPunishinsideView=punishInsideService.changedatatoshehe(penalizeInside);
		model.addAttribute("chuangjianfilepath", penPunishinsideView.getCreateFileposition());
		model.addAttribute("shensuposition", penPunishinsideView.getShensufileposition());
		model.addAttribute("id", id);
		model.addAttribute("penPunishinsideView", penPunishinsideView);
		return "penalize/penalizeIn/shenhe";
	}
	/**
	 * 进入查看详情页面
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping("/findthisValue")
	public String findthisValue(Model model,@RequestParam(value="id",defaultValue="",required=false)long id){
		PenalizeInside penalizeInside=punishInsideDao.getInsidebyid(id);
		PenalizeInsideView penPunishinsideView=punishInsideService.changedatatoshehe(penalizeInside);
		model.addAttribute("chuangjianfilepath", penPunishinsideView.getCreateFileposition());
		model.addAttribute("shensuposition", penPunishinsideView.getShensufileposition());
		model.addAttribute("shensuposition", penPunishinsideView.getShenhefileposition());
		model.addAttribute("id", id);
		model.addAttribute("penPunishinsideView", penPunishinsideView);
		return "penalize/penalizeIn/finddetail";
	}
	/**
	 * 对内扣罚的审核（带文件）
	 * @param request
	 * @param file
	 * @return
	 */
	@RequestMapping("/submitHandleShenheResultAddfile")
	public @ResponseBody String  submitHandleShenheResultAddfile(HttpServletRequest request,
			@RequestParam(value = "Filedata", required = false) MultipartFile file
			){
		User user=this.getSessionUser();
		String id=StringUtil.nullConvertToEmptyString(request.getParameter("id")).trim();
		if (punishInsideService.checkisshenhe(Long.parseLong(id))) {
			return "{\"errorCode\":1,\"error\":\"对内扣罚单已经审核过，不允许再次审核\"}";

		}
		if (!punishInsideService.switchhourTomill(Long.parseLong(id))) {
			return "{\"errorCode\":1,\"error\":\"对内扣罚单已经失效\"}";
		}
		PenalizeInsideShenhe pennishinsideShenhe=punishInsideService.getpunInsideShenhe(request);
		String filename=punishInsideService.loadexceptfile(file);
		pennishinsideShenhe.setShenheposition(filename);
		pennishinsideShenhe.setShenheuserid(user.getUserid());
		try {
			punishInsideDao.updatePunishShenhe(pennishinsideShenhe);
			return "{\"errorCode\":0,\"error\":\"操作成功\"}";
		} catch (Exception e) {
			this.logger.error("对内扣罚审核带文件的失败", e);
			return "{\"errorCode\":1,\"error\":\"操作失败\"}";
		}
	} 
	/**
	 * 不带文件的对内扣罚的审核
	 * @param request
	 * @return
	 */
	@RequestMapping("/submitHandleShenheResult")
	public @ResponseBody String submitHandleShenheResult(HttpServletRequest request){
		User user=this.getSessionUser();
		String id=StringUtil.nullConvertToEmptyString(request.getParameter("id")).trim();
		if (punishInsideService.checkisshenhe(Long.parseLong(id))) {
			return "{\"errorCode\":1,\"error\":\"对内扣罚单已经审核过，不允许再次审核\"}";

		}
		if (!punishInsideService.switchhourTomill(Long.parseLong(id))) {
			return "{\"errorCode\":1,\"error\":\"对内扣罚单已经失效\"}";
		}
		PenalizeInsideShenhe pennishinsideShenhe=punishInsideService.getpunInsideShenhe(request);
		pennishinsideShenhe.setShenheposition("");
		pennishinsideShenhe.setShenheuserid(user.getUserid());

		try {
			punishInsideDao.updatePunishShenhe(pennishinsideShenhe);
			return "{\"errorCode\":0,\"error\":\"操作成功\"}";
		} catch (Exception e) {
			this.logger.error("对内扣罚审核带文件的失败", e);
			return "{\"errorCode\":1,\"error\":\"操作失败\"}";
		}
	}
	@RequestMapping("/exportExcle")
	public void exportExcelwithPunishInside(HttpServletRequest request,HttpServletResponse response,Model model){
		String[] cloumnName1 = new String[14]; // 导出的列名
		String[] cloumnName2 = new String[14]; // 导出的英文列名

		this.exportService.SetPunishInsideFields(cloumnName1, cloumnName2);
		final String[] cloumnName = cloumnName1;
		final String[] cloumnName3 = cloumnName2;
		String sheetName = "对内扣罚单"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "PunishInside_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			// 查询出数据
			String cwb = request.getParameter("cwbStrs") == null ? "" : request.getParameter("cwbStrs").toString();
			String begindate = request.getParameter("bedindate1") == null ? "" : request.getParameter("bedindate1").toString();
			String enddate = request.getParameter("enddate1") == null ? "" : request.getParameter("enddate1").toString();
			long dutybranchid = request.getParameter("dutybranchid1") == null ? 0 : Long.parseLong(request.getParameter("dutybranchid1").toString());
			long dutynameid = request.getParameter("dutyname1") == null ? 0 : Long.parseLong(request.getParameter("dutyname1").toString());
			long cwbpunishtype = request.getParameter("cwbpunishtype1") == null ? 0 : Long.parseLong(request.getParameter("cwbpunishtype1").toString());
			long cwbstate = request.getParameter("cwbstate") == null ? 0 : Long.parseLong(request.getParameter("cwbstate").toString());
			long punishbigsort = request.getParameter("punishbigsort1") == null ? 0 : Long.parseLong(request.getParameter("punishbigsort1").toString());
			long punishsmallsort = request.getParameter("punishsmallsort1") == null ? 0 : Long.parseLong(request.getParameter("punishsmallsort1").toString());
			String quot = "'", quotAndComma = "',";
			StringBuffer cwbs1 = new StringBuffer();
			if (cwb.length() > 0) {
				for (String cwbStr : cwb.split("\r\n")) {
					if (cwbStr.trim().length() == 0) {
						continue;
					}
					cwbs1 = cwbs1.append(quot).append(cwbStr).append(quotAndComma);
				}
			}
			String cwbs = new String();
			if (cwbs1.length() > 0) {
				cwbs = cwbs1.substring(0, cwbs1.length() - 1);
			}
			List<PenalizeInside> penalizeInsides=punishInsideDao.findByCondition(-9,cwbs,dutybranchid,cwbpunishtype,dutynameid,cwbstate,punishbigsort,punishsmallsort,begindate,enddate);
			String punishsumprice=punishInsideDao.calculateSumPrice(cwbs, dutybranchid, cwbpunishtype, dutynameid, cwbstate, punishbigsort, punishsmallsort, begindate, enddate);
			final List<PenalizeInsideView> penalizeInsideViews=punishInsideService.setViews(penalizeInsides,punishsumprice);
			ExcelUtils excelUtil = new ExcelUtils() {

				@Override
				public void fillData(Sheet sheet, CellStyle style) {
					for (int k = 0; k < penalizeInsideViews.size(); k++) {
						Row row = sheet.createRow(k + 1);
						row.setHeightInPoints(15);
						for (int i = 0; i < cloumnName.length; i++) {
							Cell cell = row.createCell((short) i);
							cell.setCellStyle(style);
							Object a = null;
							// 给导出excel赋值
							a = exportService.setPunishInsideObject(cloumnName3, penalizeInsideViews, a, i, k);
							cell.setCellValue(a == null ? "" : a.toString());
						}
					}
				} 
			
			};
			excelUtil.excel(response, cloumnName, sheetName, fileName);
		} catch (Exception e) {
			this.logger.error("对内扣罚单导出出现问题", e);
		}
	}
	//弹出框查询工单信息
	@RequestMapping("/querygogdan")
	public @ResponseBody List<PunishGongdanView> querygogdan(
			@RequestParam(value="cwb",defaultValue="",required=false)String cwb,
			@RequestParam(value="gongdancwb",defaultValue="",required=false)String gongdancwb,
			@RequestParam(value="gongdantype",defaultValue="0",required=false)long gongdantype,
			@RequestParam(value="gongdanstate",defaultValue="0",required=false)long gongdanstate,
			@RequestParam(value="complainresultcontent",defaultValue="0",required=false)long complainresultcontent,
			@RequestParam(value="complainedmechanism",defaultValue="0",required=false)long complainedmechanism,
			@RequestParam(value="begindateh",defaultValue="",required=false)String begindateh,
			@RequestParam(value="enddateh",defaultValue="",required=false)String enddateh,
			@RequestParam(value="tousuonesort",defaultValue="0",required=false)long tousuonesort,
			@RequestParam(value="tousutwosort",defaultValue="0",required=false)long tousutwosort
			
			){
		try {
			String ncwbs="";
			if (!cwb.equals("")) {
				StringBuffer sb = new StringBuffer();
				for(String str:cwb.split(",")){
					sb=sb.append("'"+str+"',");
				}
				ncwbs=sb.substring(0, sb.length()-1);
			}
			
			List<CsComplaintAccept> lcs = workOrderDAO.findGoOnacceptWOByCWBsAdd(ncwbs,gongdancwb,gongdantype,gongdanstate,complainresultcontent,complainedmechanism,begindateh,enddateh,tousuonesort,tousutwosort);
			List<PunishGongdanView> punishGongdanViews=punishInsideService.setViewsGongdan(lcs);
			return punishGongdanViews;
		} catch (Exception e) {
			return new ArrayList<PunishGongdanView>();
		}		
	}
}
