package cn.explink.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.activemq.broker.UserIDBroker;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import cn.explink.dao.AbnormalOrderDAO;
import cn.explink.dao.AbnormalTypeDAO;
import cn.explink.dao.AbnormalWriteBackDAO;
import cn.explink.dao.AppearWindowDao;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.MissPieceDao;
import cn.explink.dao.RoleDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.AbnormalOrder;
import cn.explink.domain.AbnormalType;
import cn.explink.domain.AbnormalWriteBack;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.Function;
import cn.explink.domain.MissPiece;
import cn.explink.domain.MissPieceView;
import cn.explink.domain.Role;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.User;
import cn.explink.enumutil.AbnormalOrderHandleEnum;
import cn.explink.enumutil.AbnormalWriteBackEnum;
import cn.explink.enumutil.BranchEnum;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.service.AbnormalService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.service.UserService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.ExcelUtils;
import cn.explink.util.Page;
import cn.explink.util.ResourceBundleUtil;
import cn.explink.util.StringUtil;

@Controller
@RequestMapping("/abnormalOrder")
public class AbnormalOrderController {
	@Autowired
	MissPieceDao missPieceDao;
	@Autowired
	UserService userService;
	@Autowired
	UserDAO userDAO;
	@Autowired
	AbnormalTypeDAO abnormalTypeDAO;
	@Autowired
	AbnormalOrderDAO abnormalOrderDAO;
	@Autowired
	AbnormalWriteBackDAO abnormalWriteBackDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	ExportService exportService;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	AbnormalService abnormalService;
	@Autowired
	AppearWindowDao appearWindowDao;
	@Autowired
	SystemInstallDAO systemInstallDAO;
	@Autowired
	RoleDAO roleDao;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	/**
	 * 创建问题件
	 * 
	 * @param model
	 * @param cwb
	 * @param abnormaltypeid
	 * @return
	 *//*
	@RequestMapping("/toCreateabnormal")
	public  String toCreateabnormal(
			HttpServletRequest request,HttpServletResponse response,
			Model model,
			@RequestParam("file") CommonsMultipartFile[] files,
			@RequestParam(value = "cwb", defaultValue = "", required = false) String cwb,
			@RequestParam(value = "handleBranch", defaultValue = "5", required = false) long handleBranch,
			@RequestParam(value = "abnormalinfo", defaultValue = "", required = false) String abnormalinfo,
			@RequestParam(value = "abnormaltypeid", defaultValue = "0", required = false) long abnormaltypeid) {
		//问题件单号
		String questionNo="Q"+System.currentTimeMillis();
		//判断插入的问题件isfind是已经找到（1）还是未找到（0）
		boolean falg=false;
		Branch branch=branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid());
		if (branch!=null) {
			handleBranch=branch.getSitetype();
		}
		String name=abnormalService.getExceptname(request);
		String quot = "'", quotAndComma = "',";
		model.addAttribute("abnormalTypeList", this.abnormalTypeDAO.getAllAbnormalTypeByName());
		Map<Long, JSONObject> mapForAbnormalorder = new HashMap<Long, JSONObject>();
		if (cwb.length() > 0) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			String nowtime = df.format(date);
			List<CwbOrder> cwbList = new ArrayList<CwbOrder>();
			StringBuffer cwbs = new StringBuffer();
			for (String cwbStr : cwb.split("\r\n")) {
				if (cwbStr.trim().length() == 0) {
					continue;
				}
				cwbs = cwbs.append(quot).append(cwbStr).append(quotAndComma);
				CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwbStr);
				if (co != null) {
					//创建问题件时候查看丢失表里面是否有该订单号的相应的记录，有的话修改该丢失表里面的问题单号
					List<MissPiece> missPiece=missPieceDao.findMissPieceByCwb(co.getCwb());
					if (missPiece!=null&&missPiece.size()>0) {
						falg=true;
						missPieceDao.updateQuestionNo(questionNo,co.getCwb());
					}
					AbnormalOrder ab = this.abnormalOrderDAO.getAbnormalOrderByOpscwbidForObject(co.getOpscwbid());
					long action = AbnormalWriteBackEnum.ChuangJian.getValue();
					if (ab != null) {
						action = AbnormalWriteBackEnum.XiuGai.getValue();
					}
					this.abnormalService.creAbnormalOrder(co, this.getSessionUser(), abnormaltypeid, nowtime, mapForAbnormalorder, action, handleBranch,name,abnormalinfo,questionNo,falg);
					
				}
			}
			cwbList.addAll(this.cwbDAO.getCwbByCwbs(cwbs.substring(0, cwbs.length() - 1)));
			model.addAttribute("mapForAbnormal", mapForAbnormalorder);
			model.addAttribute("cwbList", cwbList);
			model.addAttribute("branchList", this.branchDAO.getAllEffectBranches());
			model.addAttribute("customerList", this.customerDAO.getAllCustomers());

		}
		boolean iszhandian = false;
		if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
			iszhandian = true;
		}
		model.addAttribute("iszhandian", iszhandian);

		return "/abnormalorder/createabnormal";
	}*/
	/**
	 * 创建问题件(主表中数据为未处理状态)
	 * 
	 * @param model
	 * @param cwb
	 * @param abnormaltypeid
	 * @return
	 */
	@RequestMapping("/toCreateabnormalAdd")
	public @ResponseBody String toCreateabnormalAdd(
			HttpServletRequest request,
			@RequestParam("file") CommonsMultipartFile[] files,
			@RequestParam(value = "cwb", defaultValue = "", required = false) String cwb,
			@RequestParam(value = "abnormalinfo", defaultValue = "", required = false) String abnormalinfo,
			@RequestParam(value = "abnormaltypeid", defaultValue = "0", required = false) long abnormaltypeid) {
		if (abnormalinfo.equals("最多输入100个字")) {
			abnormalinfo="";
		}
		Map<String, String> errorCwbs=new HashMap<String, String>();
		/*	//查看订单号是否存在，如果不存在不允许后进行创建问题件
		CwbOrder cwbOrder=cwbDAO.getCwbByCwb(cwb);
		if (cwbOrder==null) {
			return "{\"errorCode\":0,\"error\":\"该订单号'"+cwb+"'不存在，不允许创建！\"}";
		}
		//查看是否已经创建问题件
		AbnormalOrder abnormalOrder=abnormalOrderDAO.getAbnormalOrderByOCwb(cwb);
		if (abnormalOrder!=null) {
			return "{\"errorCode\":0,\"error\":\"该订单号'"+cwb+"'已经创建过问题件，不允许再次创建！\"}";
		}*/
		//判断插入的问题件isfind是已经找到（1）还是未找到（0）
		long isfind=0;
		Branch branch=branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid());
		long handleBranch=0;//0为没有操作机构
		if (branch!=null) {
			handleBranch=branch.getSitetype();
		}
		//上传的附件名
		String name=abnormalService.getExceptname(request);
		String quot = "'", quotAndComma = "',";
		Map<Long, JSONObject> mapForAbnormalorder = new HashMap<Long, JSONObject>();
		
			if (cwb.length() > 0) {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = new Date();
				String nowtime = df.format(date);
				StringBuffer cwbs = new StringBuffer();
				for (String cwbStr : cwb.split("\r\n")) {
					//自动生成问题件单号
					String questionNo="Q"+System.currentTimeMillis();
					try {
						if (cwbStr.trim().length() == 0) {
							continue;
						}
						cwbs = cwbs.append(quot).append(cwbStr).append(quotAndComma);
						CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwbStr);
						if (co==null) {
							errorCwbs.put(cwbStr, "该订单不存在");
							continue;
						}
						AbnormalOrder abnormalOrder=this.abnormalOrderDAO.getAbnormalOrderByOCwb(cwbStr);
						if (abnormalOrder!=null) {
							errorCwbs.put(cwbStr, "该订单已经创建为问题件");
							continue;
						}
						if (co != null) {
							//创建问题件时候查看丢失表里面是否有该订单号的相应的记录，有的话修改该丢失表里面的问题单号
							List<MissPiece> missPiece=missPieceDao.findMissPieceByCwb(co.getCwb());
							if (missPiece!=null&&missPiece.size()>0) {
								isfind=1;
								missPieceDao.updateQuestionNo(questionNo,co.getCwb());
							}
							AbnormalOrder ab = this.abnormalOrderDAO.getAbnormalOrderByOpscwbidForObject(co.getOpscwbid());
							long action = AbnormalWriteBackEnum.ChuangJian.getValue();
							//abnormalOrder的状态为未处理
							long ishandle=AbnormalOrderHandleEnum.weichuli.getValue();
						/*	if (ab != null) {
								action = AbnormalWriteBackEnum.XiuGai.getValue();
							}*/
							this.abnormalService.creAbnormalOrder(co, this.getSessionUser(), abnormaltypeid, nowtime, mapForAbnormalorder, action, handleBranch,name,abnormalinfo,questionNo,isfind,ishandle);
							
						}
					} catch (Exception e) {
						logger.error("问题件创建失败", e);
						errorCwbs.put(cwbStr, "创建失败");
					}
				}
				
			}
			if (errorCwbs.size()==0) {
				return "{\"errorCode\":0,\"error\":\"创建全部成功\"}";
			}else  if(errorCwbs.size()==cwb.split("\r\n").length){
				return "{\"errorCode\":1,\"error\":\"创建全部失败（可能由于其中有不存在或者已经创建问题件的订单）\"}";
			}else {
				return "{\"errorCode\":2,\"error\":\"创建问题件部分成功\"}";
			}
		
		
	}
	/**
	 * 进入创建问题件页面
	 * 
	 * @param model
	 * @param cwb
	 * @param abnormaltypeid
	 * @return
	 */
	@RequestMapping("/toCreateabnormalPage")
	public  String toCreateabnormalPage(Model model) {
		model.addAttribute("abnormalTypeList", this.abnormalTypeDAO.getAllAbnormalTypeByName());
		Branch branch = this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid());
		boolean iszhandian = false;
		if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
			iszhandian = true;
		}
		model.addAttribute("iszhandian", iszhandian);

		return "/abnormalorder/createabnormal";
	}

	/**
	 * 创建问题件的form提交
	 * 
	 * @param model
	 * @param cwbdetails
	 * @return
	 */
	/*@RequestMapping("/submitCreateabnormal")
	public @ResponseBody
	long submitCreateabnormal(Model model, @RequestParam(value = "cwbdetails", defaultValue = "", required = false) String cwbdetails) {

		model.addAttribute("abnormalTypeList", this.abnormalTypeDAO.getAllAbnormalTypeByName());
		long successCount = 0;

		if (cwbdetails == null) {
			return successCount;
		}

		JSONArray rJson = JSONArray.fromObject(cwbdetails);

		for (int i = 0; i < rJson.size(); i++) {
			String reason = rJson.getString(i);
			if (reason.equals("") || (reason.indexOf("_s_") == -1)) {
				continue;
			}
			String[] cwb_id = reason.split("_s_");
			if (cwb_id.length == 4) {
				try {
					if (!cwb_id[3].equals("") && !cwb_id[0].equals("0") && (cwb_id[2].split("_").length == 2) && !cwb_id[2].split("_")[0].equals("0") && !cwb_id[2].split("_")[1].equals("0")) {
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date date = new Date();
						String nowtime = df.format(date);

						// AbnormalOrder
						// ab=abnormalOrderDAO.getAbnormalOrderById(Long.parseLong(cwb_id[2].split("_")[1]));

						this.abnormalOrderDAO.saveAbnormalOrderByid(Long.parseLong(cwb_id[2].split("_")[1]), Long.parseLong(cwb_id[2].split("_")[0]), cwb_id[1]);

						// 改成修改 abnormalenum 添加新的类型

						this.abnormalWriteBackDAO.creAbnormalOrder(Long.parseLong(cwb_id[0]), cwb_id[1], this.getSessionUser().getUserid(), AbnormalWriteBackEnum.XiuGai.getValue(), nowtime,
								Long.parseLong(cwb_id[2].split("_")[1]), Long.parseLong(cwb_id[2].split("_")[0]), cwb_id[3],"");
						AbnormalOrder abnormalOrder = this.abnormalOrderDAO.getAbnormalOrderByOId(Long.parseLong(cwb_id[2].split("_")[1]));
						CwbOrder co = this.cwbDAO.getCwbOrderByOpscwbid(Long.parseLong(cwb_id[0]));
						List<User> kefurole = this.userDAO.getUserByRole(1);
						if (abnormalOrder != null) {
							for (User user : kefurole) {
								String json = "订单：" + co.getCwb() + "待处理";
								this.appearWindowDao.creWindowTime(json, "3", user.getUserid(), "1");
							}
						}
						successCount++;
					}
					this.logger.info("{} 成功", reason);
				} catch (Exception e) {
					e.printStackTrace();
					this.logger.error("{} 失败", reason);
				}
			} else {
				this.logger.info("{} 失败，格式不正确", reason);
			}
		}

		return successCount;
	}*/

	/**
	 * 问题件处理查询功能
	 * 
	 * @param page
	 * @param model
	 * @param cwb
	 * @param branchid
	 * @param abnormaltypeid
	 * @param ishandle
	 * @return
	 */
	@RequestMapping("/toHandleabnormal/{page}")
	public String toHandleabnormal(@PathVariable("page") long page, Model model, @RequestParam(value = "cwb", defaultValue = "", required = false) String cwb,
			@RequestParam(value = "createbranchid", defaultValue = "0", required = false) long createbranchid, 
			@RequestParam(value = "dutybranchid", defaultValue = "0", required = false) long dutybranchid, 
			@RequestParam(value = "abnormaltypeid", defaultValue = "0", required = false) long abnormaltypeid,
			@RequestParam(value = "ishandle", required = false, defaultValue = "-1") long ishandle, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate,
			@RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "customerid", required = false, defaultValue = "-1") long customerid,
			@RequestParam(value = "chuangjianbegindate", required = false, defaultValue = "") String chuangjianbegindate,
			@RequestParam(value = "chuangjianenddate", required = false, defaultValue = "") String chuangjianenddate,
			@RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow,
			@RequestParam(value = "dealresult", required = false, defaultValue = "0") long dealresult,
			@RequestParam(value = "losebackisornot", required = false, defaultValue = "-1") long losebackisornot

			) {
		User user=this.getSessionUser();
		long userid=user.getUserid();
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
		long handleBranch = 0;
		Branch branch = this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid());
		if ((branch.getSitetype() == BranchEnum.KuFang.getValue()) || (branch.getSitetype() == BranchEnum.KeFu.getValue())) {
			handleBranch = branch.getSitetype();
		}
		String cwbs = "";
		if (cwbs1.length() > 0) {
			cwbs = cwbs1.substring(0, cwbs1.length() - 1);
		}

		// 根据时间去abnormalWriteBack表查询符合条件的opscwbid

		List<JSONObject> abnormalOrderList = new ArrayList<JSONObject>();
		int count = 0;
		//findscope字段判断是查本站还是本人，管理员可以查询本站，个人只可以查询自己的
		long findscope=user.getRoleid();
		if (isshow == 1) {
			if (ishandle ==1 ) {

				if (chuangjianbegindate.length() == 0) {
					chuangjianbegindate = DateTimeUtil.getDateBefore(1);
				}
				if (chuangjianenddate.length() == 0) {
					chuangjianenddate = DateTimeUtil.getNowTime();
				}
				// count=abnormalOrderDAO.getAbnormalOrderAndCwbdetailByCwbAndBranchidAndAbnormaltypeidAndIshandleCount(cwbs,branchid,
				// abnormaltypeid, ishandle,begindate,enddate);
				// abnormalOrderList=abnormalOrderDAO.getAbnormalOrderAndCwbdetailByCwbAndBranchidAndAbnormaltypeidAndIshandle(page,cwbs,branchid,
				// abnormaltypeid, ishandle,begindate,enddate);
					//查询时机构类型不做限制
					abnormalOrderList = this.abnormalOrderDAO.getAbnormalOrderByCredatetimeofpage(page, chuangjianbegindate, chuangjianenddate, cwbs, createbranchid, abnormaltypeid, ishandle, customerid,
							handleBranch,dealresult,losebackisornot,dutybranchid,branch.getBranchid(),findscope,userid);
					count = this.abnormalOrderDAO.getAbnormalOrderByCredatetimeCount(chuangjianbegindate, chuangjianenddate, cwbs, createbranchid, abnormaltypeid, ishandle, customerid, handleBranch,dealresult,losebackisornot,dutybranchid,branch.getBranchid(),findscope,userid);
			} else {

				if (begindate.length() == 0) {
					begindate = DateTimeUtil.getDateBefore(1);
				}
				if (enddate.length() == 0) {
					enddate = DateTimeUtil.getNowTime();
				}
				count = this.abnormalOrderDAO.getAbnormalOrderAndCwbdetailByCwbAndBranchidAndAbnormaltypeidAndIshandleCount(cwbs, createbranchid, abnormaltypeid, ishandle, begindate, enddate, customerid,
						handleBranch,dealresult,losebackisornot,dutybranchid,branch.getBranchid(),findscope,userid);
				abnormalOrderList = this.abnormalOrderDAO.getAbnormalOrderAndCwbdetailByCwbAndBranchidAndAbnormaltypeidAndIshandle(page, cwbs, createbranchid, abnormaltypeid, ishandle, begindate, enddate,
						customerid, handleBranch,dealresult,losebackisornot,dutybranchid,branch.getBranchid(),findscope,userid);

			}
		}
		// 根据条件查询和上一步中查出的opscwbid来查询
		List<Branch> branchs = this.branchDAO.getAllEffectBranches();
		List<User> users = this.userDAO.getAllUser();
		List<Customer> customers = this.customerDAO.getAllCustomers();
		List<AbnormalType> atlist = this.abnormalTypeDAO.getAllAbnormalTypeByName();
		List<AbnormalView> views = this.abnormalService.setViews(abnormalOrderList, branchs, users, customers, atlist);
		/*
		 * List<AbnormalView> viewForShow=new ArrayList<AbnormalView>(); for
		 * (int i = (int) ((page-1)*Page.ONE_PAGE_NUMBER); i <
		 * Page.ONE_PAGE_NUMBER*page&&i<views.size(); i++) { viewFo
		 * rShow.add(views.get(i)); }
		 */

		model.addAttribute("chuangjianbegindate", chuangjianbegindate);
		model.addAttribute("chuangjianenddate", chuangjianenddate);

		model.addAttribute("branchList", branchs);
		model.addAttribute("abnormalTypeList", atlist);
		model.addAttribute("views", views);
		model.addAttribute("cwb", cwb);
		model.addAttribute("ishandle", ishandle);
		model.addAttribute("branchid", branch.getBranchid());
		/*
		 * model.addAttribute("page_obj", new
		 * Page(views.size(),page,Page.ONE_PAGE_NUMBER));
		 */
		List<Customer> customerlist = this.customerDAO.getAllCustomers();
		model.addAttribute("customerlist", customerlist);
		model.addAttribute("page", page);
		model.addAttribute("customerid", customerid);
		model.addAttribute("page_obj", new Page(count, page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("sitetype", this.branchDAO.getBranchById(this.getSessionUser().getBranchid()).getSitetype());
		model.addAttribute("userid", this.getSessionUser().getUserid());
		model.addAttribute("roleid", user.getRoleid());
		return "/abnormalorder/handleabnormallist";
	}

	@RequestMapping("getabnormalOrder/{id}")
	public String getabnormalOrder(@PathVariable("id") long id, Model model, @RequestParam(value = "type", required = false, defaultValue = "0") long type,
			@RequestParam(value = "isfind", required = false, defaultValue = "0") long isfind) {
		AbnormalOrder abnormalOrder = this.abnormalOrderDAO.getAbnormalOrderById(id);
		model.addAttribute("abnormalTypeList", this.abnormalTypeDAO.getAllAbnormalTypeByName());
		model.addAttribute("abnormalOrder", abnormalOrder);
		List<Branch> branchlList = this.branchDAO.getAllEffectBranches();
		HashMap<Long, String> branchMap = new HashMap<Long, String>();
		for (Branch b : branchlList) {
			branchMap.put(b.getBranchid(), b.getBranchname());
		}
		//初判责任人与责任机构
		String username="";
		String dutybranch="";
		long dutyuser=abnormalOrder.getDutypersonid();
		if (dutyuser!=0) {
			User user=userDAO.getUserByidAdd(dutyuser);
			if (user!=null) {
				username=user.getRealname();
				if (user.getBranchid()!=0) {
					dutybranch=branchDAO.getBranchName(user.getBranchid());
				}
			}
		}
		//最终责任人机构与当事人
		String lastdutybranchname="";
		String lastdutypersonName="";
		long dutybranchid=abnormalOrder.getLastdutybranchid();
		long dutypersonid=abnormalOrder.getLastdutyuserid();
		Branch branch=this.branchDAO.getBranchByIdAdd(dutybranchid);
		User user1=this.userDAO.getUserByidAdd(dutypersonid);
		if (branch!=null) {
			lastdutybranchname=branch.getBranchname();
		}
		if (user1!=null) {
			lastdutypersonName=user1.getRealname();
		}
		//创建人
		abnormalOrder.getCreuserid();
		User user=userDAO.getAllUserByid(dutyuser);
		long iskefu=0;
		if (this.branchDAO.getBranchById(this.getSessionUser().getBranchid()).getSitetype()==BranchEnum.KeFu.getValue()) {
			iskefu=1;
		}
		String createname="";
		if (user!=null) {
			createname=user.getRealname()==null?"":user.getRealname();
		}
		String jieanbranch="";
		String  jieanperson="";
		/*String lastdutybranch="";
		String lastdutyperson="";*/
		String jieantime="";
		String jieandescribe="";
		List<AbnormalWriteBack> abnormalWriteBacks=this.abnormalWriteBackDAO.getAbnormalWriteBackisjiean(abnormalOrder.getCwb());
		if (abnormalWriteBacks!=null) {
			if (abnormalWriteBacks.size()>0) {
				jieantime=abnormalWriteBacks.get(0).getCredatetime();
				jieandescribe=abnormalWriteBacks.get(0).getDescribe();
				/*long lastdutybranchid=abnormalWriteBacks.get(0).getLastdutybranchid();
				long lastdutypersonid=abnormalWriteBacks.get(0).getLastdutyuserid();
				if (lastdutybranchid!=0) {
					lastdutybranch=branchDAO.getBranchName(lastdutybranchid);
				}
				if (lastdutypersonid!=0) {
					User user2=userDAO.getAllUserByid(lastdutypersonid);
					if (user2!=null) {
						lastdutyperson=user2.getRealname();
					}
				}*/
				for (AbnormalWriteBack abnormalWriteBack : abnormalWriteBacks) {
					long userid=abnormalWriteBack.getCreuserid();
					User user2=userDAO.getAllUserByid(userid);
					if (user2!=null) {
						jieanperson=user2.getRealname();
						jieanbranch=branchDAO.getBranchName(user2.getBranchid());
					}
				}
			}
		}
		model.addAttribute("filepathsum", abnormalOrder.getFileposition());
		model.addAttribute("dutybranch",dutybranch);
		model.addAttribute("jieantime",jieantime);
		model.addAttribute("jieandescribe",jieandescribe);
		model.addAttribute("lastdutybranch",lastdutybranchname);
		model.addAttribute("lastdutyperson",lastdutypersonName);
		model.addAttribute("jieanperson",jieanperson);
		model.addAttribute("createname",createname);
		model.addAttribute("jieanbranch",jieanbranch);
		model.addAttribute("username",username);
		model.addAttribute("branchList", branchlList);
		model.addAttribute("isfind", isfind);
		model.addAttribute("branchMap", branchMap);
		model.addAttribute("customerList", this.customerDAO.getAllCustomers());
		model.addAttribute("userList", this.userDAO.getAllUser());
		model.addAttribute("cwborder", this.cwbDAO.getCwbOrderByOpscwbid(abnormalOrder.getOpscwbid()));
		model.addAttribute("abnormalWriteBackList", this.abnormalWriteBackDAO.getAbnormalOrderByOrderid(id));
		SystemInstall system = this.systemInstallDAO.getSystemInstall("showabnomal");
		String showabnomal = system == null ? "0" : system.getValue();
		model.addAttribute("showabnomal", showabnomal);
		Role role = this.roleDao.getRolesByRoleid(this.getSessionUser().getRoleid());
		model.addAttribute("role", role);
		model.addAttribute("iskefu", iskefu);
		if (type == 1) {
			if(isfind==1){
			return "/abnormalorder/nowhandleabnormalnew";
			}
			return "/abnormalorder/nowhandleabnormal";
		} else if (type == 2) {
			return "/abnormalorder/nowreplyabnormal";
		}
		return null;
	}
	//处理带文件的问题件处理
	@RequestMapping("/SubmitHandleabnormal")
	public @ResponseBody
	String SubmitHandleabnormal(
			HttpServletRequest request,
			@RequestParam(value = "Filedata", required = false) MultipartFile file
	) {
		try {
			String filepath=abnormalService.loadexceptfile(file);
			String dutyname = StringUtil.nullConvertToEmptyString(request.getParameter("dutyname")).trim();
			String dutybranchid = StringUtil.nullConvertToEmptyString(request.getParameter("dutybranchid"));
			String describe = StringUtil.nullConvertToEmptyString(request.getParameter("describe"));
			String id = StringUtil.nullConvertToEmptyString(request.getParameter("id"));
			String isfind = StringUtil.nullConvertToEmptyString(request.getParameter("isfind"));
			String cwb = StringUtil.nullConvertToEmptyString(request.getParameter("cwb"));
			String filepathsum=this.getFilepathSum(Integer.parseInt(id), filepath);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			String nowtime = df.format(date);
			AbnormalOrder ab = this.abnormalOrderDAO.getAbnormalOrderByOId(Integer.parseInt(id));
			if ((dutybranchid==""&&dutyname=="")||(dutybranchid.equals("undefined")&&dutyname.equals("undefined"))) {
				dutyname=ab.getDutypersonid()+"";
				dutybranchid=ab.getDutybrachid()+"";
			}
			long userid=this.getSessionUser().getUserid();
			long ishandle=0;
			//判断是谁处理的
			if (this.branchDAO.getBranchById(this.getSessionUser().getBranchid()).getSitetype()==BranchEnum.KeFu.getValue()) {
				ishandle=AbnormalOrderHandleEnum.kefuchuli.getValue();
			}else if (userid==ab.getCreuserid()) {
				ishandle=AbnormalOrderHandleEnum.chuangjianfangchuli.getValue();
			}else if (userid==ab.getDutypersonid()) {
				ishandle=AbnormalOrderHandleEnum.zerenfangchuli.getValue();
			}
			if (Integer.parseInt(isfind)== 1) {
				ishandle = ab.getIshandle();
			}
			this.abnormalOrderDAO.saveAbnormalOrderForIshandleAdd(Integer.parseInt(id), ishandle, nowtime,filepathsum,Integer.parseInt(dutyname),Integer.parseInt(dutybranchid));
			
			this.abnormalWriteBackDAO.creAbnormalOrder(ab.getOpscwbid(), describe, this.getSessionUser().getUserid(), AbnormalWriteBackEnum.ChuLi.getValue(), nowtime, ab.getId(),
					ab.getAbnormaltypeid(), cwb,filepath);
			/*
			 * String json = "订单：" + cwb + "已处理";
			 * this.appearWindowDao.creWindowTime(json, "4", ab.getCreuserid(),
			 * "1");
			 */
			return "{\"errorCode\":0,\"error\":\"操作成功\"}";
		} catch (Exception e) {
			return "{\"errorCode\":1,\"error\":\"操作失败\"}";
		}
	}
	//处理不带文件的问题件处理
	@RequestMapping("/SubmitHandleabnormalwithNofile")
	public @ResponseBody
	String SubmitHandleabnormalwithNofile(
			HttpServletRequest request
			) {
		try {
			String dutyname = StringUtil.nullConvertToEmptyString(request.getParameter("dutyname")).trim();
			String dutybranchid = StringUtil.nullConvertToEmptyString(request.getParameter("dutybranchid"));
			String describe = StringUtil.nullConvertToEmptyString(request.getParameter("describe"));
			String id = StringUtil.nullConvertToEmptyString(request.getParameter("id"));
			String isfind = StringUtil.nullConvertToEmptyString(request.getParameter("isfind"));
			String cwb = StringUtil.nullConvertToEmptyString(request.getParameter("cwb"));
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			String nowtime = df.format(date);
			AbnormalOrder ab = this.abnormalOrderDAO.getAbnormalOrderByOId(Integer.parseInt(id));
			if ((dutybranchid==""&&dutyname=="")||(dutybranchid.equals("undefined")&&dutyname.equals("undefined"))) {
				dutyname=ab.getDutypersonid()+"";
				dutybranchid=ab.getDutybrachid()+"";
			}
			long userid=this.getSessionUser().getUserid();
			long ishandle=0;
			//判断是谁处理的
			if (this.branchDAO.getBranchById(this.getSessionUser().getBranchid()).getSitetype()==BranchEnum.KeFu.getValue()) {
				ishandle=AbnormalOrderHandleEnum.kefuchuli.getValue();
			}else if (userid==ab.getCreuserid()) {
				ishandle=AbnormalOrderHandleEnum.chuangjianfangchuli.getValue();
			}else if (userid==ab.getDutypersonid()) {
				ishandle=AbnormalOrderHandleEnum.zerenfangchuli.getValue();
			}
			if (Integer.parseInt(isfind)== 1) {
				ishandle = ab.getIshandle();
			}
			this.abnormalOrderDAO.saveAbnormalOrderForIshandleAdd(Integer.parseInt(id), ishandle, nowtime,ab.getFileposition(),Integer.parseInt(dutyname),Integer.parseInt(dutybranchid));
			
			this.abnormalWriteBackDAO.creAbnormalOrder(ab.getOpscwbid(), describe, this.getSessionUser().getUserid(), AbnormalWriteBackEnum.ChuLi.getValue(), nowtime, ab.getId(),
					ab.getAbnormaltypeid(), cwb,"");
			/*
			 * String json = "订单：" + cwb + "已处理";
			 * this.appearWindowDao.creWindowTime(json, "4", ab.getCreuserid(),
			 * "1");
			 */
			return "{\"errorCode\":0,\"error\":\"操作成功\"}";
		} catch (Exception e) {
			return "{\"errorCode\":1,\"error\":\"操作失败\"}";
		}
	}

	/**
	 * 问题件回复
	 * 
	 * @param page
	 * @param model
	 * @param begindate
	 * @param enddate
	 * @param ishandle
	 * @param abnormaltypeid
	 * @return
	 */
	@RequestMapping("/toReplyabnormal/{page}")
	public String toReplyabnormal(@PathVariable("page") long page, Model model, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate,
			@RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "ishandle", required = false, defaultValue = "-1") long ishandle,
			@RequestParam(value = "abnormaltypeid", defaultValue = "0", required = false) long abnormaltypeid) {
		if (begindate.length() == 0) {
			begindate = DateTimeUtil.getDateBefore(1);
		}
		if (enddate.length() == 0) {
			enddate = DateTimeUtil.getNowTime();
		}
		List<AbnormalOrder> abnormalOrderList = this.abnormalOrderDAO.getAbnormalOrderByWhere(page, begindate, enddate, ishandle, abnormaltypeid, this.getSessionUser().getUserid());
		model.addAttribute("abnormalOrderList", abnormalOrderList);
		model.addAttribute("abnormalTypeList", this.abnormalTypeDAO.getAllAbnormalTypeByName());
		model.addAttribute("customerList", this.customerDAO.getAllCustomers());
		model.addAttribute("userList", this.userDAO.getAllUser());
		model.addAttribute("page_obj", new Page(this.abnormalOrderDAO.getAbnormalOrderCount(begindate, enddate, ishandle, abnormaltypeid, this.getSessionUser().getUserid()), page,
				Page.ONE_PAGE_NUMBER));
		return "/abnormalorder/replyabnormallist";
	}

/*	@RequestMapping("/SubmitReplyabnormal/{id}")
	public @ResponseBody
	String SubmitReplyabnormal(@PathVariable("id") long id, @RequestParam(value = "describe", defaultValue = "", required = false) String describe,
			@RequestParam(value = "cwb", defaultValue = "", required = false) String cwb) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			String nowtime = df.format(date);
			AbnormalOrder abnormalOrder = this.abnormalOrderDAO.getAbnormalOrderById(id);
			this.abnormalOrderDAO.saveAbnormalOrderForNohandle(id, AbnormalOrderHandleEnum.WeiChuLi.getValue());
			this.abnormalWriteBackDAO.creAbnormalOrder(abnormalOrder.getOpscwbid(), describe, this.getSessionUser().getUserid(), AbnormalWriteBackEnum.HuiFu.getValue(), nowtime,
					abnormalOrder.getId(), abnormalOrder.getAbnormaltypeid(), cwb,"");
			
			 * String json = "订单：" + cwb + "待处理"; List<User> kefurole =
			 * this.userDAO.getUserByRole(1); if (abnormalOrder != null) { for
			 * (User user : kefurole) { this.appearWindowDao.creWindowTime(json,
			 * "3", user.getUserid(), "1"); } }
			 
			return "{\"errorCode\":0,\"error\":\"操作成功\"}";
		} catch (Exception e) {
			return "{\"errorCode\":1,\"error\":\"操作失败\"}";
		}
	}*/

	@RequestMapping("/abnormaldataMove")
	public String abnormaldataMove(@RequestParam(value = "opscwbids", defaultValue = "", required = false) String opscwbids,
			@RequestParam(value = "ishandle", defaultValue = "0", required = false) long ishandle) {
		try {
			StringBuffer w = new StringBuffer();
			w.append("");
			if (!opscwbids.equals("")) {
				for (String opscwbid : opscwbids.split("\r\n")) {
					w.append("'" + opscwbid.trim() + "',");
				}
			}
			w.append("''");
			if (ishandle > 0) {
				List<AbnormalWriteBack> abnormalWriteBack = this.abnormalWriteBackDAO.getAbnormalOrderByOpscwbids(w.toString());

				List<String> cwbList = this.cwbDAO.getCwbByOpscwbids(w.toString());
				for (String cwb : cwbList) {
					CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);
					this.abnormalOrderDAO.abnormaldataMove(co);
					this.abnormalWriteBackDAO.abnormaldataMoveofcwb(co);
				}
				for (AbnormalWriteBack awb : abnormalWriteBack) {
					this.abnormalOrderDAO.abnormaldataMoveofhandletime(awb);
				}
			}
			return "/abnormalorder/abnormaldataMove";
		} catch (Exception e) {
			return "/abnormalorder/abnormaldataMove";
		}
	}
	//问题件导出
	@RequestMapping("/exportExcle")
	public void exportExcle(Model model, HttpServletResponse response, HttpServletRequest request) {

		String[] cloumnName1 = new String[9]; // 导出的列名
		String[] cloumnName2 = new String[9]; // 导出的英文列名

		this.exportService.SetAbnormalOrderFields(cloumnName1, cloumnName2);
		final String[] cloumnName = cloumnName1;
		final String[] cloumnName3 = cloumnName2;
		String sheetName = "问题件信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "AbnormalOrder_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			User user=this.getSessionUser();
			// 查询出数据
			String cwb = request.getParameter("cwbStrs") == null ? "" : request.getParameter("cwbStrs").toString();
			long createbranchid1 = request.getParameter("createbranchid1") == null ? 0 : Long.parseLong(request.getParameter("createbranchid1").toString());
			long dutybranchid1 = request.getParameter("dutybranchid1") == null ? 0 : Long.parseLong(request.getParameter("dutybranchid1").toString());
			long losebackisornot1 = request.getParameter("losebackisornot1") == null ? 0 : Long.parseLong(request.getParameter("losebackisornot1").toString());
			long dealresult1 = request.getParameter("dealresult1") == null ? 0 : Long.parseLong(request.getParameter("dealresult1").toString());
			long abnormaltypeid = request.getParameter("abnormaltypeid1") == null ? 0 : Long.parseLong(request.getParameter("abnormaltypeid1").toString());
			long ishandle = request.getParameter("ishandle1") == null ? 0 : Long.parseLong(request.getParameter("ishandle1").toString());
			long customerid = request.getParameter("customerid") == null ? -1 : Long.parseLong(request.getParameter("customerid").toString());
			String begindate = request.getParameter("begindate1") == null ? "" : request.getParameter("begindate1");
			String enddate = request.getParameter("enddate1") == null ? "" : request.getParameter("enddate1");
			String chuangjianbegindate = request.getParameter("chuangjianbegindate1") == null ? "" : request.getParameter("chuangjianbegindate1");
			String chuangjianenddate = request.getParameter("chuangjianenddate1") == null ? "" : request.getParameter("chuangjianenddate1");
			long findscope1=user.getRoleid();
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
			long handleBranch = 0;
			Branch branch = this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid());
			if ((branch.getSitetype() == BranchEnum.KuFang.getValue()) || (branch.getSitetype() == BranchEnum.KeFu.getValue())) {
				handleBranch = branch.getSitetype();
			}
			// 根据时间去abnormalWriteBack表查询符合条件的opscwbid
			// List<String> abnormalWriteBackOpscwbidList = new
			// ArrayList<String>();
			//判断人员权限
			long findscope=user.getRoleid();
			List<JSONObject> abnormalOrderList = new ArrayList<JSONObject>();
			//当ishandle这个字段为1与8的时候为修改与未处理状态，也就是为当前创建状态
			if (ishandle == 1||ishandle==8) {

				if (chuangjianbegindate.length() == 0) {
					chuangjianbegindate = DateTimeUtil.getDateBefore(1);
				}
				if (chuangjianenddate.length() == 0) {
					chuangjianenddate = DateTimeUtil.getNowTime();
				}
				// abnormalWriteBackOpscwbidList =
				// this.abnormalOrderDAO.getAbnormalOrderByCredatetime(chuangjianbegindate,
				// chuangjianenddate);
				//查询时机构类型不做限制(第一个参数为-6时不做页面限制)
				abnormalOrderList = this.abnormalOrderDAO.getAbnormalOrderByCredatetimeofpage(-6, chuangjianbegindate, chuangjianenddate, cwbs, createbranchid1, abnormaltypeid, ishandle, customerid,
						handleBranch,dealresult1,losebackisornot1,dutybranchid1,branch.getBranchid(),findscope,user.getUserid());

			} else {
				if (begindate.length() == 0) {
					begindate = DateTimeUtil.getDateBefore(1);
				}
				if (enddate.length() == 0) {
					enddate = DateTimeUtil.getNowTime();
				}
				// abnormalWriteBackOpscwbidList =
				// this.abnormalWriteBackDAO.getAbnormalOrderByCredatetime(begindate,
				// enddate);
				abnormalOrderList = this.abnormalOrderDAO.getAbnormalOrderAndCwbdetailByCwbAndBranchidAndAbnormaltypeidAndIshandle(-6, cwbs, createbranchid1, abnormaltypeid, ishandle, begindate, enddate,
						customerid, handleBranch,dealresult1,losebackisornot1,dutybranchid1,branch.getBranchid(),findscope,user.getUserid());

			}

			// 根据条件查询和上一步中查出的opscwbid来查询
			// abnormalOrderList =
			// this.abnormalOrderDAO.getAbnormalOrderAndCwbdetailByCwbAndBranchidAndAbnormaltypeidAndIshandle(this.getStrings(abnormalWriteBackOpscwbidList),
			// cwbs, branchid,
			// abnormaltypeid, ishandle);

			List<Branch> branchs = this.branchDAO.getAllBranches();
			List<User> users = this.userDAO.getAllUser();
			List<Customer> customers = this.customerDAO.getAllCustomers();
			List<AbnormalType> atlist = this.abnormalTypeDAO.getAllAbnormalTypeByName();
			final List<AbnormalView> views = this.abnormalService.setViews(abnormalOrderList, branchs, users, customers, atlist);
			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(Sheet sheet, CellStyle style) {
					for (int k = 0; k < views.size(); k++) {
						Row row = sheet.createRow(k + 1);
						row.setHeightInPoints(15);
						for (int i = 0; i < cloumnName.length; i++) {
							Cell cell = row.createCell((short) i);
							cell.setCellStyle(style);
							Object a = null;
							// 给导出excel赋值
							a = exportService.setAbnormalOrderObject(cloumnName3, views, a, i, k);
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

	@RequestMapping("/SubmitOverabnormal/{id}")
	public @ResponseBody
	String SubmitOverabnormal(@PathVariable("id") long id, 
			HttpServletRequest request,
			@RequestParam(value = "describe2", defaultValue = "", required = false) String describe2,
			@RequestParam(value = "cwb", defaultValue = "", required = false) String cwb) {
		try {
			String filepathsum=this.getFilepath(id, request);
			String filepath=abnormalService.getExceptname(request);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			String nowtime = df.format(date);
			AbnormalOrder ab = this.abnormalOrderDAO.getAbnormalOrderByOId(id);
			this.abnormalOrderDAO.saveAbnormalOrderForIshandle(id, AbnormalOrderHandleEnum.yichuli.getValue(), nowtime,filepathsum);
			this.abnormalWriteBackDAO.creAbnormalOrder(ab.getOpscwbid(), describe2, this.getSessionUser().getUserid(), AbnormalWriteBackEnum.ChuLi.getValue(), nowtime, ab.getId(),
					ab.getAbnormaltypeid(), cwb,filepath);
			/*
			 * String json = "订单：" + cwb + "已处理";
			 * this.appearWindowDao.creWindowTime(json, "4", ab.getCreuserid(),
			 * "1");
			 */
			return "{\"errorCode\":0,\"error\":\"操作成功\"}";
		} catch (Exception e) {
			return "{\"errorCode\":1,\"error\":\"操作失败\"}";
		}
	}
	public String getFilepath(long id,HttpServletRequest request){
		String filepath="";
		AbnormalOrder abnormalOrder=abnormalOrderDAO.getAbnormalOrderById(id);
		String name=abnormalService.getExceptname(request);
		if (abnormalOrder.getFileposition()!=null) {
			filepath=abnormalOrder.getFileposition()+","+name;
		}
		return filepath;
	}
	//将上传的文件名以,连起来
	public String getFilepathSum(long id,String name){
		String filepath="";
		AbnormalOrder abnormalOrder=abnormalOrderDAO.getAbnormalOrderById(id);
		if (abnormalOrder.getFileposition()!=null&&abnormalOrder.getFileposition()!="") {
			filepath=abnormalOrder.getFileposition()+","+name;
			return filepath;
		}else {
			return name;
		}
		
	}

	public String getStrings(List<String> strArr) {
		String strs = "0,";
		if (strArr.size() > 0) {
			for (String str : strArr) {
				strs += "'" + str + "',";
			}
		}

		if (strs.length() > 0) {
			strs = strs.substring(0, strs.length() - 1);
		}
		return strs;
	}

	public String getString(List<Long> list) {
		String str = "0,";
		if (list.size() > 0) {
			for (Long num : list) {
				str += num + ",";
			}

		}
		if (str.length() > 0) {
			str = str.substring(0, str.length() - 1);
		}
		return str;
	}

	@RequestMapping("/tofindabnormal/{page}")
	public String tofindabnormal(@PathVariable("page") long page, Model model, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate,
			@RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "ishandle", required = false, defaultValue = "-1") long ishandle,
			@RequestParam(value = "abnormaltypeid", defaultValue = "0", required = false) long abnormaltypeid, @RequestParam(value = "tip", defaultValue = "0", required = false) long tip) {
		if (begindate.length() == 0) {
			begindate = DateTimeUtil.getDateBefore(1);
		}
		if (enddate.length() == 0) {
			enddate = DateTimeUtil.getNowTime();
		}
		SystemInstall system = this.systemInstallDAO.getSystemInstall("showabnomal");
		String showabnomal = system == null ? "0" : system.getValue();
		model.addAttribute("showabnomal", showabnomal);
		List<Branch> branchlList = this.branchDAO.getAllEffectBranches();
		model.addAttribute("branchList", branchlList);
		List<AbnormalOrder> abnormalOrderList = new ArrayList<AbnormalOrder>();
		if (tip == 1) {
			abnormalOrderList = this.abnormalOrderDAO.getAbnormalOrderByWherefind(page, begindate, enddate, ishandle, abnormaltypeid);
			model.addAttribute("page_obj", new Page(this.abnormalOrderDAO.getAbnormalOrderCountfind(begindate, enddate, ishandle, abnormaltypeid), page, Page.ONE_PAGE_NUMBER));
		} else {
			model.addAttribute("page_obj", new Page());
		}
		model.addAttribute("abnormalOrderList", abnormalOrderList);
		model.addAttribute("abnormalTypeList", this.abnormalTypeDAO.getAllAbnormalTypeByName());
		model.addAttribute("customerList", this.customerDAO.getAllCustomers());
		model.addAttribute("userList", this.userDAO.getAllUser());

		model.addAttribute("starttime1", begindate);
		model.addAttribute("endtime1", enddate);
		model.addAttribute("ishandle1", ishandle);
		model.addAttribute("abnormaltypeid1", abnormaltypeid);
		return "/abnormalorder/tofindabnormal";
	}

	@RequestMapping("gotoBatch")
	public String gotoBatch(Model model, @RequestParam(value = "ids", required = false, defaultValue = "") String ids) {
		SystemInstall system = this.systemInstallDAO.getSystemInstall("showabnomal");
		String showabnomal = system == null ? "0" : system.getValue();
		model.addAttribute("showabnomal", showabnomal);
		model.addAttribute("user", this.getSessionUser());
		model.addAttribute("ids",ids);
		model.addAttribute("role", this.roleDao.getRolesByRoleid(this.getSessionUser().getRoleid()));
		return "/abnormalorder/nowhandleabnormalBatch";
	}

	@RequestMapping("/SubmitHandleabnormalBatch")
	public @ResponseBody
	String SubmitHandleabnormalBatch(@RequestParam(value = "ids", defaultValue = "", required = false) String ids,
			@RequestParam(value = "describe", defaultValue = "", required = false) String describe,
			@RequestParam(value = "ishandle", defaultValue = "0", required = false) long ishandle
			) {
		
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			String nowtime = df.format(date);
			String[] arrayIds = ids.split(",");

			for (int i = 0; i < arrayIds.length; i++) {
				int id = Integer.parseInt(arrayIds[i]);
				AbnormalOrder ab = this.abnormalOrderDAO.getAbnormalOrderByOId(id);
				//判断是哪一方处理（客服，创建方，责任方）
				if (this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid()).getSitetype()==BranchEnum.KeFu.getValue()) {
					ishandle=AbnormalOrderHandleEnum.kefuchuli.getValue();
				}else if (this.getSessionUser().getUserid()==ab.getDutypersonid()) {
					ishandle=AbnormalOrderHandleEnum.zerenfangchuli.getValue();
				}else if (this.getSessionUser().getUserid()==ab.getCreuserid()) {
					ishandle=AbnormalOrderHandleEnum.chuangjianfangchuli.getValue();
				}
				this.abnormalOrderDAO.saveAbnormalOrderForIshandleAdd(id, ishandle, nowtime);
				this.abnormalWriteBackDAO.creAbnormalOrder(ab.getOpscwbid(), describe, this.getSessionUser().getUserid(), AbnormalWriteBackEnum.ChuLi.getValue(), nowtime, ab.getId(),
						ab.getAbnormaltypeid(), ab.getCwb(),"");
				/*
				 * String json = "订单：" + ab.getCwb() + "已处理";
				 * this.appearWindowDao.creWindowTime(json, "4",
				 * ab.getCreuserid(), "1");
				 */
			}
			return "{\"errorCode\":0,\"error\":\"操作成功\"}";
		} catch (Exception e) {
			return "{\"errorCode\":1,\"error\":\"操作失败\"}";
		}
	}

	@RequestMapping("/exportExcleFind")
	public void exportExcleFind(Model model, HttpServletResponse response, @RequestParam(value = "starttime1", required = false, defaultValue = "") String starttime,
			@RequestParam(value = "endtime1", required = false, defaultValue = "") String endtime, @RequestParam(value = "ishandle1", required = false, defaultValue = "-1") long ishandle,
			@RequestParam(value = "abnormaltypeid1", defaultValue = "0", required = false) long abnormaltypeid) {

		String[] cloumnName1 = new String[9]; // 导出的列名
		String[] cloumnName2 = new String[9]; // 导出的英文列名

		this.exportService.SetAbnormalOrderFields(cloumnName1, cloumnName2);
		final String[] cloumnName = cloumnName1;
		final String[] cloumnName3 = cloumnName2;
		String sheetName = "问题件信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "AbnormalOrder_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			// 查询出数据

			List<Branch> branchs = this.branchDAO.getAllBranches();
			List<User> users = this.userDAO.getAllUser();
			List<Customer> customers = this.customerDAO.getAllCustomers();
			List<AbnormalType> atlist = this.abnormalTypeDAO.getAllAbnormalTypeByName();
			List<JSONObject> abnormalOrderList = this.abnormalOrderDAO.getAbnormalOrderByWherefindExport(starttime, endtime, ishandle, abnormaltypeid);
			final List<AbnormalView> views = this.abnormalService.setViews(abnormalOrderList, branchs, users, customers, atlist);
			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(Sheet sheet, CellStyle style) {
					for (int k = 0; k < views.size(); k++) {
						Row row = sheet.createRow(k + 1);
						row.setHeightInPoints(15);
						for (int i = 0; i < cloumnName.length; i++) {
							Cell cell = row.createCell((short) i);
							cell.setCellStyle(style);
							Object a = null;
							// 给导出excel赋值
							a = exportService.setAbnormalOrderObject(cloumnName3, views, a, i, k);
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
	@RequestMapping("/getbranchusers")
	public @ResponseBody String getbranchusers(HttpServletRequest request){
		String dutybranchid=request.getParameter("dutybranchid");
		List<User> branchusers=userDAO.getAllUserbybranchid(Integer.parseInt(dutybranchid));
		String returndataString="";
		try {
			returndataString=JacksonMapper.getInstance().writeValueAsString(branchusers);
		} catch (Exception e) {
			this.logger.info("AbnormalOrderController类中804行branchusers解析成json出错");
		}
		return returndataString;
	}
	//进入修改问题件的页面
	@RequestMapping("/revisequestionstate")
	public String revisequestionstate(Model model, @RequestParam(value = "ids", required = false, defaultValue = "") String ids){
		SystemInstall system = this.systemInstallDAO.getSystemInstall("showabnomal");
		model.addAttribute("abnormalTypeList", this.abnormalTypeDAO.getAllAbnormalTypeByName());
		String showabnomal = system == null ? "0" : system.getValue();
		model.addAttribute("showabnomal", showabnomal);
		model.addAttribute("user", this.getSessionUser());
		model.addAttribute("ids",ids);
		model.addAttribute("role", this.roleDao.getRolesByRoleid(this.getSessionUser().getRoleid()));
		return "/abnormalorder/revisequestionstate";
	}
	//修改创建过的问题件（不带文件上传的）
	@RequestMapping("/SubmitReviseQuestion")
	public @ResponseBody String submitReviseQuestion(HttpServletRequest request,@RequestParam(value = "ids", defaultValue = "", required = false) String ids,
			@RequestParam(value = "describe", defaultValue = "", required = false) String describe, @RequestParam(value = "abnormaltypeid", defaultValue = "0", required = false) long abnormaltypeid){
		try {
			//String filepath=this.abnormalService.getExceptname(request);
			long ishandle=AbnormalOrderHandleEnum.xiugai.getValue();
			User user=this.getSessionUser();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			String nowtime = df.format(date);
			long action=AbnormalWriteBackEnum.XiuGai.getValue();
			String[] arrayIds = ids.split(",");
			for (int i = 0; i < arrayIds.length; i++) {
				int id = Integer.parseInt(arrayIds[i]);
				AbnormalOrder ab = this.abnormalOrderDAO.getAbnormalOrderByOId(id);
				//String filepathsum=(ab.getFileposition()==null?"":ab.getFileposition())+filepath;
				CwbOrder co=this.cwbDAO.getCwbByCwbLock(ab.getCwb());
				abnormalOrderDAO.saveAbnormalOrderByidAdd(id, abnormaltypeid, describe,ishandle,nowtime);
				this.abnormalWriteBackDAO.creAbnormalOrder(co.getOpscwbid(), describe, user.getUserid(), action, nowtime, ab.getId(), abnormaltypeid, co.getCwb(),"");
				
			}
			return "{\"errorCode\":0,\"error\":\"操作成功\"}";
		} catch (Exception e) {
			return "{\"errorCode\":1,\"error\":\"操作失败\"}";
		}
	}
	//修改创建过的问题件（带文件上传的）
	@RequestMapping("/SubmitReviseQuestionAdd")
	public @ResponseBody String submitReviseQuestionAdd(HttpServletRequest request,
			@RequestParam(value = "Filedata", required = false) MultipartFile file
			){
		try {
			long ishandle=AbnormalOrderHandleEnum.xiugai.getValue();
			String ids = StringUtil.nullConvertToEmptyString(request.getParameter("ids"));
			String describe = StringUtil.nullConvertToEmptyString(request.getParameter("describe"));
			String abnormaltypeid = StringUtil.nullConvertToEmptyString(request.getParameter("abnormaltypeid"));
			String filepath=this.abnormalService.loadexceptfile(file);
			User user=this.getSessionUser();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			String nowtime = df.format(date);
			long action=AbnormalWriteBackEnum.XiuGai.getValue();
			String[] arrayIds = ids.split(",");
			for (int i = 0; i < arrayIds.length; i++) {
				long id = Long.parseLong(arrayIds[i]);
				AbnormalOrder ab = this.abnormalOrderDAO.getAbnormalOrderByOId(id);
				String filepathsum=(ab.getFileposition()==null?"":ab.getFileposition())+","+filepath;
				CwbOrder co=this.cwbDAO.getCwbByCwbLock(ab.getCwb());
				abnormalOrderDAO.saveAbnormalOrderByid(id, Integer.parseInt(abnormaltypeid), describe,ishandle,filepathsum,nowtime);
				this.abnormalWriteBackDAO.creAbnormalOrder(co.getOpscwbid(), describe, user.getUserid(), action, nowtime, ab.getId(), Integer.parseInt(abnormaltypeid), co.getCwb(),filepath);
				
			}
			return "{\"errorCode\":0,\"error\":\"操作成功\"}";
		} catch (Exception e) {
			return "{\"errorCode\":1,\"error\":\"操作失败\"}";
		}
	}
	//进入结案处理页面
	@RequestMapping("/resultdatadeallast")
	public String resultdatadeallast(Model model,@RequestParam(value="id",required=false,defaultValue="0") long id
			){
		//暂时只是 支持一单数据进行结案处理
		AbnormalOrder abnormalOrder = this.abnormalOrderDAO.getAbnormalOrderById(id);
		model.addAttribute("abnormalTypeList", this.abnormalTypeDAO.getAllAbnormalTypeByName());
		model.addAttribute("abnormalOrder", abnormalOrder);
		List<Branch> branchlList = this.branchDAO.getAllEffectBranches();
		HashMap<Long, String> branchMap = new HashMap<Long, String>();
		for (Branch b : branchlList) {
			branchMap.put(b.getBranchid(), b.getBranchname());
		}
		String branchdutyuser="";
		String branchname="";
		if (abnormalOrder.getDutypersonid()!=0) {
			User user=userDAO.getUserByidAdd(abnormalOrder.getDutypersonid());
			if (user!=null) {
				branchdutyuser=user.getRealname();
			}
			if(abnormalOrder.getDutybrachid()!=0){
				Branch branch=this.branchDAO.getBranchByIdAdd(abnormalOrder.getDutybrachid());
				if (branch!=null) {
					branchname=branch.getBranchname();
				}
			}
		}
		model.addAttribute("branchdutyuser",branchdutyuser);
		model.addAttribute("branchname",branchname);
		model.addAttribute("branchid",abnormalOrder.getDutybrachid());
		model.addAttribute("branchList", branchlList);
		model.addAttribute("branchMap", branchMap);
		model.addAttribute("customerList", this.customerDAO.getAllCustomers());
		model.addAttribute("userList", this.userDAO.getAllUser());
		model.addAttribute("cwborder", this.cwbDAO.getCwbOrderByOpscwbid(abnormalOrder.getOpscwbid()));
		model.addAttribute("abnormalWriteBackList", this.abnormalWriteBackDAO.getAbnormalOrderByOrderid(id));
		SystemInstall system = this.systemInstallDAO.getSystemInstall("showabnomal");
		String showabnomal = system == null ? "0" : system.getValue();
		model.addAttribute("showabnomal", showabnomal);
		Role role = this.roleDao.getRolesByRoleid(this.getSessionUser().getRoleid());
		model.addAttribute("role", role);
		model.addAttribute("filepathsum", abnormalOrder.getFileposition());
		return "/abnormalorder/resultdatadeallast";
	}
	//结案处理操作（不带文件）
	@RequestMapping("/submitHandleabnormalResult")
	public @ResponseBody String submitHandleabnormalResult(
			HttpServletRequest request,
			@RequestParam(value="id",defaultValue="",required=false) long id,
			@RequestParam(value="describe",required=false,defaultValue="") String describe,
			@RequestParam(value="dealresult",required=false,defaultValue="0") long dealresult,
			@RequestParam(value="dutybranchid",required=false,defaultValue="0") long dutybranchid,
			@RequestParam(value="dutyname",required=false,defaultValue="0") long dutyname
			){
		try {
			
			//判断已经结案的订单不能再次结案
			if (describe.equals("最多100个字")) {
				describe="";
			}
			User user=this.getSessionUser();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			String nowtime = df.format(date);
			AbnormalOrder ab = this.abnormalOrderDAO.getAbnormalOrderByOId(id);
			List<AbnormalWriteBack> abnormalWriteBacks=this.abnormalWriteBackDAO.getAbnormalWriteBackisjiean(ab.getCwb());
			if (abnormalWriteBacks!=null) {
				if (abnormalWriteBacks.size()>0) {
					return "{\"errorCode\":1,\"error\":\"操作失败(已经结案的订单不允许再次结案)\"}";
				}
			}
			long action=AbnormalWriteBackEnum.JieAN.getValue();
			long ishandle=AbnormalOrderHandleEnum.jieanchuli.getValue();
			abnormalService.reviseAbnormalAndwritebackLast(ab,describe,dealresult,dutybranchid,dutyname,ab.getFileposition(),action,user,nowtime,"",ishandle);
			return "{\"errorCode\":0,\"error\":\"操作成功\"}";
			} catch (Exception e) {
				return "{\"errorCode\":1,\"error\":\"操作失败\"}";
		}
		
	}
	//结案处理操作（带文件）
	@RequestMapping("/submitHandleabnormalResultAdd")
	public @ResponseBody String submitHandleabnormalResultAdd(
			HttpServletRequest request,
			@RequestParam(value = "Filedata", required = false) MultipartFile file
			){
		try {
			String dutyname = StringUtil.nullConvertToEmptyString(request.getParameter("dutyname")).trim();
			String describe = StringUtil.nullConvertToEmptyString(request.getParameter("describe")).trim();
			String dealresult = StringUtil.nullConvertToEmptyString(request.getParameter("dealresult")).trim();
			String dutybranchid = StringUtil.nullConvertToEmptyString(request.getParameter("dutybranchid")).trim();
			//String cwb = StringUtil.nullConvertToEmptyString(request.getParameter("cwb"));//暂时没用到
			String id = StringUtil.nullConvertToEmptyString(request.getParameter("id")).trim();
			//判断已经结案的订单不能再次结案
			if (describe=="最多100个字") {
				describe="";
			}
			User user=this.getSessionUser();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			String nowtime = df.format(date);
			String filepath=this.abnormalService.loadexceptfile(file);
			AbnormalOrder ab = this.abnormalOrderDAO.getAbnormalOrderByOId(Integer.parseInt(id));
			List<AbnormalWriteBack> abnormalWriteBacks=this.abnormalWriteBackDAO.getAbnormalWriteBackisjiean(ab.getCwb());
			if (abnormalWriteBacks!=null) {
				if (abnormalWriteBacks.size()>0) {
					return "{\"errorCode\":1,\"error\":\"操作失败(已经结案的订单不允许再次结案)\"}";
				}
			}
			String filepathsum=(ab.getFileposition()==null?"":ab.getFileposition())+","+filepath;
			long action=AbnormalWriteBackEnum.JieAN.getValue();
			long ishandle=AbnormalOrderHandleEnum.jieanchuli.getValue();
			abnormalService.reviseAbnormalAndwritebackLast(ab,describe,Integer.parseInt(dealresult),Integer.parseInt(dutybranchid),Integer.parseInt(dutyname),filepathsum,action,user,nowtime,filepath,ishandle);
			return "{\"errorCode\":0,\"error\":\"操作成功\"}";
		} catch (Exception e) {
			return "{\"errorCode\":1,\"error\":\"操作失败\"}";
		}
		
	}
	
	//跳转到丢失件创建页面
	@RequestMapping("/losebackcreate")
	public String losebackcreate(Model model){
		List<Branch> branchs=this.branchDAO.getAllBranches();
		model.addAttribute("branchList",branchs);
		return "/abnormalorder/losebackcreate";
	}
	
	//创建丢失件
	@RequestMapping("/toCreatMissPiece")
	public @ResponseBody String toCreatMissPiece(
			HttpServletRequest request,
			@RequestParam(value="cwb",defaultValue="",required=false) String cwbs,
			@RequestParam(value="callbackbranchid",defaultValue="0",required=false) long callbackbranchid,
			@RequestParam(value="abnormalinfo",defaultValue="",required=false) String abnormalinfo,
			@RequestParam("file") CommonsMultipartFile[] files

			){
		Map<String, String> failCwbs=new HashMap<String, String>();
		if (abnormalinfo.equals("最多输入100个字")) {
			abnormalinfo="";
		}
		String questionNo="";
		String filepath=this.abnormalService.getExceptname(request);
		String[] cwbStrings=cwbs.split("\r\n");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String nowtime = df.format(date);
		
			if (cwbStrings.length>0) {
				for (String cwb : cwbStrings) {
					try {
						CwbOrder cwbOrder=cwbDAO.getCwbByCwb(cwb);
						if (cwbOrder==null) {
							failCwbs.put(cwb, "不存在该订单号");
							//return "{\"errorCode\":0,\"error\":\"不存在订单号为'"+cwb+"'的订单，不能执行该操作\"}";
						}
						 List<MissPiece> missPiece=missPieceDao.findMissPieceByCwb(cwb);
						if (missPiece!=null&&missPiece.size()>0) {
							failCwbs.put(cwb, "该订单号已经创建为丢失找回");
							//return "{\"errorCode\":0,\"error\":\"该订单号'"+cwb+"'已经创建过为丢失找回，不能执行该操作\"}";
						}
						long customerid=cwbOrder.getCustomerid();
						long cwbtypeid=cwbOrder.getCwbordertypeid();
						long flowordertype=cwbOrder.getFlowordertype();
						User user=this.getSessionUser();
						long userid=user.getUserid();
						AbnormalOrder abnormalOrder=abnormalOrderDAO.getAbnormalOrderByOCwb(cwb);
						if (abnormalOrder!=null) {
							questionNo=abnormalOrder.getQuestionno();
							//修改问题件的是否丢失状态
							abnormalOrderDAO.updateMisspieceState(1,cwb);
						}
						missPieceDao.insertintoMissPiece(cwb,callbackbranchid,abnormalinfo,filepath,questionNo,nowtime,customerid,cwbtypeid,flowordertype,userid);
					} catch (Exception e) {
						failCwbs.put(cwb, "创建时异常失败");
						this.logger.error("订单号为'"+cwb+"'的订单创建丢失件的时候出现异常", e);
					}
				}
			}
			if (failCwbs.size()==0) {
				return "{\"errorCode\":0,\"error\":\"创建全部成功\"}";
			}else  if(failCwbs.size()==cwbStrings.length){
				return "{\"errorCode\":1,\"error\":\"创建全部失败（可能由于其中有不存在或者已经创建丢失件的订单）\"}";
			}else {
				return "{\"errorCode\":2,\"error\":\"创建问题件部分成功\"}";
			}
	}
	//进入根据条件查询的丢失件
	@RequestMapping("/losebackdetail/{page}")
	public String losebackdetail(Model model,
			@PathVariable("page") long page,
			@RequestParam(value="cwb",defaultValue="",required=false) String cwb,
			@RequestParam(value="customerid",defaultValue="0",required=false) long customerid,
			@RequestParam(value="cwbordertype",defaultValue="0",required=false) long cwbordertype,
			@RequestParam(value="losebackbranchid",defaultValue="0",required=false) long losebackbranchid,
			@RequestParam(value="begindate",defaultValue="",required=false) String strtime,
			@RequestParam(value="enddate",defaultValue="",required=false) String endtime,
			@RequestParam(value="isshow",defaultValue="0",required=false) long isshow
			
			){
		List<Customer> customers = this.customerDAO.getAllCustomers();
		List<Branch> branchs=branchDAO.getAllEffectBranches();
		List<User> users=userDAO.getAllUser();
		List<MissPieceView> missPieceViews=new ArrayList<MissPieceView>();
		List<MissPiece> missPiecesCounList=new ArrayList<MissPiece>();
		String quotAndComma = ",";
		StringBuffer cwbs1 = new StringBuffer();
		if (isshow==0) {
			strtime="";
			endtime="";
			}
		if (isshow==1) {
			
			if (cwb.length() > 0) {
				for (String cwbStr : cwb.split("\r\n")) {
					if (cwbStr.trim().length() == 0) {
						continue;
					}
					cwbs1 = cwbs1.append(cwbStr).append(quotAndComma);
				}
			}
			String cwbs = new String();
			if (cwbs1.length() > 0) {
				cwbs = cwbs1.substring(0, cwbs1.length() - 1);
			}
			
			List<MissPiece> missPieces=missPieceDao.findMissPieces(page, cwbs, customerid, cwbordertype, losebackbranchid, strtime, endtime);
			missPiecesCounList=missPieceDao.findMissPieces(-9, cwbs, customerid, cwbordertype, losebackbranchid, strtime, endtime);
			if (missPieces!=null&&missPieces.size()>0) {
				  missPieceViews=abnormalService.setMissPieceView(missPieces,branchs,users,customers);

			}
		}
		model.addAttribute("customerid",customerid);
		model.addAttribute("missPieces",missPieceViews);
		model.addAttribute("customers",customers);
		model.addAttribute("branchlist",branchs);
		model.addAttribute("cwbordertype");
		model.addAttribute("losebackbranchid", losebackbranchid);
		model.addAttribute("page", page);
		model.addAttribute("page_obj", new Page(missPiecesCounList.size(), page, Page.ONE_PAGE_NUMBER));
		return "abnormalorder/losebackdetail";
	}
	
	//丢失件导出
	@RequestMapping("/losebackExportExcle")
	public void losebackExportExcle(Model model, HttpServletResponse response, HttpServletRequest request){
		String[] cloumnName1 = new String[9]; // 导出的列名
		String[] cloumnName2 = new String[9]; // 导出的英文列名
		this.exportService.SetMisspieceFields(cloumnName1, cloumnName2);
		final String[] cloumnName = cloumnName1;
		final String[] cloumnName3 = cloumnName2;
		String sheetName = "丢失件信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "MissPiece_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			//前台点击导出时提交表单的查询条件，根据查询条件查询出符合导出的数据
			String cwb = request.getParameter("cwbStrs") == null ? "" : request.getParameter("cwbStrs").toString();
			long cwbordertype = request.getParameter("cwbordertype") == null ? 0 : Long.parseLong(request.getParameter("cwbordertype").toString());
			long losebackbranchid = request.getParameter("losebackbranchid") == null ? 0 : Long.parseLong(request.getParameter("losebackbranchid").toString());
			String begindate = request.getParameter("begindate1") == null ? "" : request.getParameter("begindate1");
			String enddate = request.getParameter("enddate1") == null ? "" : request.getParameter("enddate1");
			long customerid = request.getParameter("customerid") == null ? -1 : Long.parseLong(request.getParameter("customerid").toString());
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
			List<MissPiece> missPieces=missPieceDao.findMissPieces(0, cwbs, customerid, cwbordertype, losebackbranchid, begindate, enddate);
			List<Branch> branchs = this.branchDAO.getAllBranches();
			List<User> users = this.userDAO.getAllUser();
			List<Customer> customers = this.customerDAO.getAllCustomers();
			final List<MissPieceView> missPieceViews=abnormalService.setMissPieceView(missPieces,branchs,users,customers);
			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(Sheet sheet, CellStyle style) {
					for (int k = 0; k < missPieceViews.size(); k++) {
						Row row = sheet.createRow(k + 1);
						row.setHeightInPoints(15);
						for (int i = 0; i < cloumnName.length; i++) {
							Cell cell = row.createCell((short) i);
							cell.setCellStyle(style);
							Object a = null;
							// 给导出excel赋值
							a = exportService.setMissPieceObject(cloumnName3, missPieceViews, a, i, k);
							cell.setCellValue(a == null ? "" : a.toString());
						}
					}
				}
			};
			excelUtil.excel(response, cloumnName, sheetName, fileName);
		} catch (Exception e) {
			this.logger.error("问题件导出数据出现问题", e);
		}
		
	}
	/**
	 * 丢失订单作废
	 * @param ids
	 * @return
	 */
	@RequestMapping("/cancelMisspiece")
	public @ResponseBody String cancelMisspiece(
			@RequestParam(value="ids",defaultValue="",required=false) String ids)
		{
		long updatesum=0;
		if(!ids.equals("")){
			if (ids.split(",").length>0) {
				for (String cwb : ids.split(",")) {
					AbnormalOrder abnormalOrder=abnormalOrderDAO.getAbnormalOrderByOCwb(cwb);
					if (abnormalOrder!=null) {
						abnormalOrderDAO.updateMisspieceState(0, cwb);
					}
					missPieceDao.updateStateAdd(cwb);
					updatesum++;
				}
			}
		}
		if (updatesum==ids.split(",").length) {
			return "{\"errorCode\":0,\"error\":\"作废丢失订单全部成功\"}";
		}else if(updatesum>0&&updatesum<=ids.split(",").length){
			return "{\"errorCode\":1,\"error\":\"作废丢失订单部分成功\"}";

		}else {
			return "{\"errorCode\":1,\"error\":\"作废丢失订单全部失败\"}";
		}
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
	@RequestMapping("/losebackcwbout/{cwbinfo}")
	public String losebackcwboutdetail(Model model,@PathVariable("cwbinfo")String cwb){
		MissPiece missPiece=missPieceDao.findMissPieceByCwb(cwb).get(0);
		model.addAttribute("cwb", cwb);
		model.addAttribute("describeinfo", missPiece.getDescribeinfo());
		model.addAttribute("filepathsum", missPiece.getFilepath());
		return "/abnormalorder/losebackcwboutdetail";
	}
}