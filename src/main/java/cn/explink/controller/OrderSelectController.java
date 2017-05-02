package cn.explink.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.dpfoss.JsonUtil;
import cn.explink.b2c.ems.EMSDAO;
import cn.explink.b2c.ems.SendToEMSOrder;
import cn.explink.b2c.feiniuwang.CwbRequest;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.AbnormalOrderDAO;
import cn.explink.dao.AbnormalTypeDAO;
import cn.explink.dao.AbnormalWriteBackDAO;
import cn.explink.dao.AccountAreaDAO;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.BranchRouteDAO;
import cn.explink.dao.CommonDAO;
import cn.explink.dao.ComplaintDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.GotoClassAuditingDAO;
import cn.explink.dao.GroupDetailDao;
import cn.explink.dao.OrderAddressReviseDao;
import cn.explink.dao.OrderDeliveryClientDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.PunishDAO;
import cn.explink.dao.PunishInsideDao;
import cn.explink.dao.PunishTypeDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.RemarkDAO;
import cn.explink.dao.ReturnCwbsDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.TransCwbDetailDAO;
import cn.explink.dao.TranscwbOrderFlowDAO;
import cn.explink.dao.TuihuoRecordDAO;
import cn.explink.dao.UserDAO;
import cn.explink.dao.express.ExpressOrderDao;
import cn.explink.domain.AbnormalType;
import cn.explink.domain.AbnormalWriteBack;
import cn.explink.domain.AbnormalWriteBackView;
import cn.explink.domain.AccountArea;
import cn.explink.domain.AorderFlowView;
import cn.explink.domain.Branch;
import cn.explink.domain.Common;
import cn.explink.domain.Complaint;
import cn.explink.domain.ComplaintsView;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.GotoClassAuditing;
import cn.explink.domain.OrderAddressRevise;
import cn.explink.domain.OrderDeliveryClient;
import cn.explink.domain.PenalizeInside;
import cn.explink.domain.PunishType;
import cn.explink.domain.Reason;
import cn.explink.domain.Remark;
import cn.explink.domain.ReturnCwbs;
import cn.explink.domain.SetExportField;
import cn.explink.domain.SystemInstall;
import cn.explink.domain.TransCwbDetail;
import cn.explink.domain.TuihuoRecord;
import cn.explink.domain.User;
import cn.explink.domain.VO.BaleCwbClassifyVo;
import cn.explink.domain.VO.express.EmbracedOrderVO;
import cn.explink.domain.VO.express.JoinMessageVO;
import cn.explink.domain.json.QueryCwbResponseDto;
import cn.explink.domain.json.QueryOrder;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.domain.orderflow.TranscwbOrderFlow;
import cn.explink.enumutil.AbnormalWriteBackEnum;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.ComplaintAuditTypeEnum;
import cn.explink.enumutil.ComplaintTypeEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.ReturnCwbsTypeEnum;
import cn.explink.enumutil.TransCwbStateEnum;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.service.BaleService;
import cn.explink.service.ComplaintService;
import cn.explink.service.CwbOrderService;
import cn.explink.service.CwbOrderWithDeliveryState;
import cn.explink.service.CwbTranslator;
import cn.explink.service.DataStatisticsService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.service.express.DeliveryOrderTrackingService;
import cn.explink.support.transcwb.TranscwbView;
import cn.explink.util.DateDayUtil;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.ExcelUtils;
import cn.explink.util.Page;
import cn.explink.util.SecurityUtil;
import cn.explink.util.StreamingStatementCreator;
import cn.explink.util.StringUtil;
import cn.explink.util.MD5.MD5Util;

import com.alibaba.fastjson.JSON;
import com.pjbest.deliveryorder.bizservice.PjDeliveryOrderService;
import com.pjbest.deliveryorder.bizservice.PjDeliveryOrderServiceHelper;
import com.pjbest.deliveryorder.bizservice.PjDeliveryTrackInfo;
import com.pjbest.pjorganization.bizservice.service.SbOrgModel;
import com.pjbest.pjorganization.bizservice.service.SbOrgService;
import com.pjbest.pjorganization.bizservice.service.SbOrgServiceHelper;
import com.vip.osp.core.exception.OspException;

@Controller
@RequestMapping("/order")
public class OrderSelectController {
	@Autowired
	PunishInsideDao punishInsideDao;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	CwbDAO cwbDao;
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	GotoClassAuditingDAO gotoClassAuditingDAO;
	@Autowired
	RemarkDAO remarkDAO;
	@Autowired
	AccountAreaDAO accountAreaDAO;
	@Autowired
	CustomWareHouseDAO customWareHouseDAO;
	@Autowired
	GroupDetailDao groupDetailDao;
	@Autowired
	List<CwbTranslator> cwbTranslators;
	@Autowired
	AbnormalWriteBackDAO abnormalWriteBackDAO;
	@Autowired
	AbnormalTypeDAO abnormalTypeDAO;
	@Autowired
	AbnormalOrderDAO abnormalOrderDAO;
	@Autowired
	SystemInstallDAO systemInstallDAO;
	@Autowired
	ComplaintDAO complaintDAO;
	@Autowired
	TranscwbOrderFlowDAO transcwbOrderFlowDAO;
	@Autowired
	ReturnCwbsDAO returnCwbsDAO;
	@Autowired
	JointService jointService;
	@Autowired
	TransCwbDetailDAO tc;
	@Autowired
	EMSDAO eMSDAO;
	private Logger logger = LoggerFactory.getLogger(OrderSelectController.class);

	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	ExportService exportService;
	@Autowired
	ExportmouldDAO exportmouldDAO;
	@Autowired
	CommonDAO commonDAO;
	@Autowired
	ReasonDao reasonDAO;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	DataStatisticsService dataStatisticsService;
	@Autowired
	TuihuoRecordDAO tuihuoRecordDAO;
	@Autowired
	ComplaintService complaintService;
	@Autowired
	BranchRouteDAO branchRouteDAO;
	@Autowired
	OrderDeliveryClientDAO orderDeliveryClientDAO;
	@Autowired
	PunishTypeDAO punishTypeDAO;
	@Autowired
	PunishDAO punishDAO;
	@Autowired
	DeliveryOrderTrackingService deliveryOrderTrackingService;
	@Autowired
	private ExpressOrderDao expressOrderDao;
	@Autowired
	OrderAddressReviseDao orderAddressReviseDao;
	
	@Autowired
	private BaleService baleService;

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	public String translateCwb(String cwb) {
		for (CwbTranslator cwbTranslator : this.cwbTranslators) {
			String translateCwb = cwbTranslator.translate(cwb);
			if (StringUtils.hasLength(translateCwb)) {
				cwb = translateCwb;
			}
		}
		return cwb;
	}

	// 快速查询栏
	/*
	 * @RequestMapping("/queckSelectOrder/{cwb}") public String
	 * queckSelectOrder(Model model, @PathVariable(value = "cwb") String cwb) {
	 * cwb = translateCwb(cwb); CwbOrder order = cwbDao.getCwbByCwb(cwb);
	 * if(order==null){ return "/orderflow/orderNotExist"; } QuickSelectView
	 * view = new QuickSelectView(); BeanUtils.copyProperties(order, view);
	 * DeliveryState
	 * deliveryState=deliveryStateDAO.getActiveDeliveryStateByCwb(cwb);
	 * if(deliveryState!=null){ BeanUtils.copyProperties(deliveryState, view); }
	 * 
	 * List<OrderFlow> datalist = orderFlowDAO.getOrderFlowByCwb(cwb);
	 * List<OrderFlowView> views=new ArrayList<OrderFlowView>(); for (OrderFlow
	 * orderFlowAll:datalist) { OrderFlowView orderFlowView=new OrderFlowView();
	 * orderFlowView.setCreateDate(orderFlowAll.getCredate());
	 * orderFlowView.setOperator
	 * (userDAO.getUserByUserid(orderFlowAll.getUserid()));
	 * orderFlowView.setId(orderFlowAll.getFloworderid());
	 * orderFlowView.setDetail(getDetail(orderFlowAll));
	 * views.add(orderFlowView); } view.setOrderFlowList(views);
	 * view.setNewpaywayid(view.getNewpaywayid());
	 * view.setBackreason(order.getBackreason());
	 * view.setLeavedreason(order.getLeavedreason());
	 * 
	 * List<OrderFlow> rukuList =
	 * orderFlowDAO.getOrderFlowByCwbAndFlowordertype(
	 * cwb,FlowOrderTypeEnum.RuKu.getValue(),"",""); List<OrderFlow> linghuoList
	 * = orderFlowDAO.getOrderFlowByCwbAndFlowordertype(cwb,FlowOrderTypeEnum.
	 * FenZhanLingHuo.getValue(),"",""); Customer customer =
	 * customerDAO.getCustomerById(view.getCustomerid()); Branch
	 * invarhousebranch =
	 * branchDAO.getBranchByBranchid(Integer.parseInt(view.getCarwarehouse
	 * ()==null?"0":view.getCarwarehouse()==""?"0":view.getCarwarehouse()));
	 * Branch deliverybranch =
	 * branchDAO.getBranchByBranchid(view.getDeliverybranchid()); Branch
	 * nextbranch = branchDAO.getBranchByBranchid(view.getNextbranchid());
	 * GotoClassAuditing gotoClassAuditingGuiBan =
	 * gotoClassAuditingDAO.getGotoClassAuditingByGcaid(view.getGcaid()); User
	 * deliveryname = userDAO.getUserByUserid(view.getDeliverid());
	 * 
	 * model.addAttribute("deliveryname",deliveryname);
	 * model.addAttribute("customer",customer);
	 * model.addAttribute("invarhousebranch",invarhousebranch==null?new
	 * Branch():invarhousebranch);
	 * model.addAttribute("deliverybranch",deliverybranch==null?new
	 * Branch():deliverybranch);
	 * model.addAttribute("nextbranch",nextbranch==null?new
	 * Branch():nextbranch); model.addAttribute("startbranch",
	 * branchDAO.getBranchByBranchid(view.getStartbranchid()));
	 * model.addAttribute("currentbranch",
	 * branchDAO.getBranchByBranchid(view.getCurrentbranchid()));
	 * model.addAttribute
	 * ("orderFlowRuKu",rukuList.size()>0?rukuList.get(rukuList.size()-1):new
	 * OrderFlow());
	 * model.addAttribute("orderFlowLingHuo",linghuoList.size()>0?linghuoList
	 * .get(linghuoList.size()-1):new OrderFlow());
	 * model.addAttribute("gotoClassAuditingGuiBan"
	 * ,gotoClassAuditingGuiBan==null?new
	 * GotoClassAuditing():gotoClassAuditingGuiBan);
	 * model.addAttribute("deliveryChengGong",new DeliveryState());
	 * model.addAttribute("cwborder",cwbDao.getCwbByCwb(cwb)==null?new
	 * CwbOrder():cwbDao.getCwbByCwb(cwb));
	 * model.addAttribute("rejectiontime",new DeliveryState());
	 * model.addAttribute("view",view); //只有客服才能在订单查询界面加备注
	 * model.addAttribute("userInBranchType",
	 * branchDAO.getBranchByBranchid(getSessionUser
	 * ().getBranchid()).getSitetype());
	 * 
	 * //加问题件处理的过程 List<AbnormalWriteBack> backList =
	 * abnormalWriteBackDAO.getAbnormalOrderByOpscwbid(order.getOpscwbid());
	 * List<AbnormalWriteBackView> backViewList = new
	 * ArrayList<AbnormalWriteBackView>(); String typename ="--"; if(backList !=
	 * null && backList.size()>0){ List<AbnormalType> alist =
	 * abnormalTypeDAO.getAllAbnormalTypeByNameAll(); for (AbnormalWriteBack
	 * back : backList) { AbnormalWriteBackView backview = new
	 * AbnormalWriteBackView(); User user =
	 * userDAO.getUserByUserid(back.getCreuserid()); Branch branch =
	 * branchDAO.getBranchByBranchid(user.getBranchid()); backview.setCwb(cwb);
	 * backview.setCredatetime(back.getCredatetime());
	 * backview.setUsername(user.getRealname());
	 * backview.setBranchname(branch.getBranchname());
	 * backview.setDescribe(back.getDescribe()); if(typename.equals("--")){
	 * typename = getTypeName(back.getOpscwbid(), alist); }
	 * backview.setTypename(typename); backViewList.add(backview); } }
	 * 
	 * //投诉的订单记录 List<Complaint> comList = complaintDAO.getComplaintByCwb(cwb);
	 * List<ComplaintsView> comViewList = new ArrayList<ComplaintsView>();
	 * if(comList != null && comList.size()>0){ for (Complaint complaint :
	 * comList) { ComplaintsView complaintview = new ComplaintsView(); User
	 * user1 = userDAO.getUserByUserid(complaint.getCreateUser()); User user2 =
	 * userDAO.getUserByUserid(complaint.getAuditUser());
	 * 
	 * complaintview.setCwb(cwb);
	 * complaintview.setCreateTime(complaint.getCreateTime());
	 * complaintview.setAuditTime(complaint.getAuditTime());
	 * complaintview.setCreateUserName(user1.getRealname());
	 * complaintview.setAuditUserName(user2.getRealname());
	 * complaintview.setTypename(getComplaintTypeName(complaint.getType()));
	 * complaintview
	 * .setBranchname(branchDAO.getBranchByBranchid(user1.getBranchid
	 * ()).getBranchname()); complaintview.setContent(complaint.getContent());
	 * complaintview
	 * .setAuditTypeName(getComplaintAuditTypeName(complaint.getAuditType()));
	 * complaintview.setAuditRemark(complaint.getAuditRemark());
	 * comViewList.add(complaintview); } }
	 * 
	 * model.addAttribute("comViewList", comViewList);
	 * model.addAttribute("abnormalWriteBackViewList", backViewList); return
	 * "/orderflow/orderWorkRightQueck"; }
	 */

	// 快速查询栏
	@RequestMapping("/queckSelectOrder/{cwb}")
	public String queckSelectOrder(Model model, @PathVariable(value = "cwb") String cwb) {
		model.addAttribute("cwb", cwb);
		String[] cwbStr = cwb.split(",");
		if (cwbStr.length > 1) {// 输入多个订单号
			return "redirect:/order/queckSelectOrders";
		} else {// 输入一个订单号
			return "/orderflow/queckSelectOrder";
		}
	}

	/**
	 * 多个订单追踪
	 *
	 * @param model
	 * @param request
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/queckSelectOrders")
	public String queckSelectOrders(Model model, HttpServletRequest request, @RequestParam(value = "cwb", required = false, defaultValue = "") String cwb) {
		List<Map<String, Object>> selectOrdersList = new ArrayList<Map<String, Object>>();
		String[] cwbsVar = cwb.split(",");

		for (int i = 0; i < cwbsVar.length; i++) {
			if (cwbsVar[i].trim().length() == 0) {
				continue;
			}
			Map<String, Object> mapList = new HashMap<String, Object>();
			String cwbstr = cwbsVar[i];
			String scancwb = cwbstr;
			String oldCwb = cwbstr;

			mapList.put("scancwb", scancwb);

			cwbstr = this.translateCwb(cwbstr);
			CwbOrder order = this.cwbDao.getCwbByCwb(cwbstr);
			if (order == null) {
				continue;
			}

			QuickSelectView view = new QuickSelectView();
			BeanUtils.copyProperties(order, view);
			DeliveryState deliveryState = this.deliveryStateDAO.getActiveDeliveryStateByCwb(cwbstr);
			if (deliveryState != null) {
				BeanUtils.copyProperties(deliveryState, view);
			}

			List<TranscwbView> transcwbList = new ArrayList<TranscwbView>();
			long isypdjusetranscwb = this.customerDAO.getCustomerById(order.getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(order.getCustomerid()).getIsypdjusetranscwb();
			if (((order.getSendcarnum() > 1) || (order.getBackcarnum() > 1)) && ((order.getTranscwb().split(",").length > 1) || (order.getTranscwb().split(":").length > 1))
					&& (isypdjusetranscwb == 1)) {
				if (order.getTranscwb().indexOf(",") > -1) {
					for (String transcwb : order.getTranscwb().split(",")) {
						TranscwbOrderFlow tcof = this.transcwbOrderFlowDAO.getTranscwbOrderFlowByCwbAndState(transcwb, cwbstr);
						TranscwbView transcwbview = new TranscwbView();
						transcwbview.setCwb(order.getCwb());
						transcwbview.setTranscwb(transcwb);
						transcwbview.setFlowordername(tcof == null ? FlowOrderTypeEnum.DaoRuShuJu.getText() : tcof.getFlowordertypeText());
						transcwbList.add(transcwbview);
					}
				} else {
					for (String transcwb : order.getTranscwb().split(":")) {
						TranscwbOrderFlow tcof = this.transcwbOrderFlowDAO.getTranscwbOrderFlowByCwbAndState(transcwb, cwbstr);
						TranscwbView transcwbview = new TranscwbView();
						transcwbview.setCwb(order.getCwb());
						transcwbview.setTranscwb(transcwb);
						transcwbview.setFlowordername(tcof == null ? FlowOrderTypeEnum.DaoRuShuJu.getText() : tcof.getFlowordertypeText());
						transcwbList.add(transcwbview);
					}
				}
			}
			mapList.put("transcwbList", transcwbList);

			List<OrderFlowView> views = new ArrayList<OrderFlowView>();
			List<TranscwbOrderFlowView> transcwbviews = new ArrayList<TranscwbOrderFlowView>();
			List<AorderFlowView> forTrans = new ArrayList<AorderFlowView>();
			List<AorderFlowView> fororder = new ArrayList<AorderFlowView>();
			if (((order.getSendcarnum() > 1) || (order.getBackcarnum() > 1)) && ((order.getTranscwb().split(",").length > 1) || (order.getTranscwb().split(":").length > 1))
					&& (isypdjusetranscwb == 1)) {
				if (oldCwb.equals(order.getCwb())) {// 如果是原订单号查询，就获取和原来的订单流程
					List<OrderFlow> datalist = this.orderFlowDAO.getOrderFlowByCwb(cwbstr);
					for (OrderFlow orderFlowAll : datalist) {
						AorderFlowView a = new AorderFlowView();
						a.setId(orderFlowAll.getFloworderid());// 用于排序同时操作的相同时间的显示顺序
						a.setTime(orderFlowAll.getCredate().toString());
						a.setContent(this.getDetail(orderFlowAll));
						a.setUsername(this.userDAO.getUserByUserid(orderFlowAll.getUserid()).getRealname());
						fororder.add(a);
					}
				} else {// 就获取运单号的订单过程
					List<TranscwbOrderFlow> datalist = this.transcwbOrderFlowDAO.getTranscwbOrderFlowByScanCwb(scancwb, cwbstr);
					for (TranscwbOrderFlow transcwborderFlowAll : datalist) {
						AorderFlowView a = new AorderFlowView();
						a.setId(transcwborderFlowAll.getFloworderid());// 用于排序同时操作的相同时间的显示顺序
						a.setTime(transcwborderFlowAll.getCredate().toString());
						a.setContent(this.getDetailForYPDJ(transcwborderFlowAll));
						a.setUsername(this.userDAO.getUserByUserid(transcwborderFlowAll.getUserid()).getRealname());
						forTrans.add(a);
					}
				}

			} else {
				List<OrderFlow> datalist = this.orderFlowDAO.getOrderFlowByCwb(cwbstr);
				for (OrderFlow orderFlowAll : datalist) {
					AorderFlowView a = new AorderFlowView();
					a.setId(orderFlowAll.getFloworderid());// 用于排序同时操作的相同时间的显示顺序
					a.setTime(orderFlowAll.getCredate().toString());
					a.setUsername(this.userDAO.getUserByUserid(orderFlowAll.getUserid()).getRealname());
					a.setContent(this.getDetail(orderFlowAll));
					fororder.add(a);
				}
			}
			// 取cwbdetail表中最新的deliverybranchid
			view.setDeliverybranchid(order.getDeliverybranchid());
			view.setCwbstate(order.getCwbstate());
			view.setOrderFlowList(views);
			view.setTranscwborderFlowList(transcwbviews);
			view.setNewpaywayid(view.getNewpaywayid());
			view.setBackreason(order.getBackreason());
			view.setLeavedreason(order.getLeavedreason());
			mapList.put("view", view);

			// 加问题件处理的过程
			List<AorderFlowView> forabnormal = new ArrayList<AorderFlowView>();
			List<AbnormalWriteBack> backList = this.abnormalWriteBackDAO.getAbnormalOrderByOpscwbid(order.getOpscwbid());

			if ((backList != null) && (backList.size() > 0)) {
				List<AbnormalType> alist = this.abnormalTypeDAO.getAllAbnormalTypeByNameAll();
				for (AbnormalWriteBack back : backList) {
					String typename = "--";
					User user = this.userDAO.getUserByUserid(back.getCreuserid());
					Branch branch = this.branchDAO.getBranchByBranchid(user.getBranchid());
					if (typename.equals("--")) {
						typename = this.getTypeName(back.getAbnormalordertype(), alist);
					}
					String describe = (back.getDescribe() == null) | "".equals(back.getDescribe()) ? "" : "备注：" + back.getDescribe();
					AorderFlowView a = new AorderFlowView();
					a.setTime(back.getCredatetime());
					a.setUsername(user.getRealname());
					a.setContent(AbnormalWriteBackEnum.getTextByValue(back.getType()) + "<font color=\"red\">[问题件]类型：[" + typename + "]</font>在[<font color=\"red\">" + branch.getBranchname()
							+ "</font>]" + describe);
					forabnormal.add(a);
				}
			}

			// 投诉的订单记录
			List<Complaint> comList = this.complaintDAO.getComplaintByCwb(cwbstr);
			List<AorderFlowView> forAudit = new ArrayList<AorderFlowView>();// 已审核投诉
			List<AorderFlowView> forcomp = new ArrayList<AorderFlowView>();// 受理投诉
			if ((comList != null) && (comList.size() > 0)) {
				for (Complaint complaint : comList) {
					AorderFlowView creobj = new AorderFlowView();
					User user1 = this.userDAO.getUserByUserid(complaint.getCreateUser());

					creobj.setTime(complaint.getCreateTime());
					creobj.setUsername(user1.getRealname());
					creobj.setContent("在[<font color=\" red\">" + this.branchDAO.getBranchByBranchid(user1.getBranchid()).getBranchname() + "</font>]受理为[<font color=\"red\">"
							+ this.getComplaintTypeName(complaint.getType()) + "</font>] 备注：<font color=\"red\">" + complaint.getContent() + "</font> ");
					forAudit.add(creobj);

					if (complaint.getAuditUser() != 0) {
						// 订单已经投诉处理过
						AorderFlowView auditobj = new AorderFlowView();
						User user2 = this.userDAO.getUserByUserid(complaint.getAuditUser());

						auditobj.setTime(complaint.getAuditTime());
						auditobj.setUsername(user2.getRealname());
						auditobj.setContent("在[<font color=\" red\">" + this.branchDAO.getBranchByBranchid(user2.getBranchid()).getBranchname() + "</font>]将创建于 <font color=\"#82b8ef\"> "
								+ complaint.getCreateTime() + "</font>  的  <font color=\"red\"> " + this.getComplaintTypeName(complaint.getType()) + "</font>   审核为[<font color=\"red\">"
								+ this.getComplaintAuditTypeName(complaint.getAuditType()) + "</font>] <br/> 备注：<font color=\"red\">" + complaint.getAuditRemark() + "</font> ");
						forAudit.add(auditobj);
					}
				}
			}

			// 返单信息
			List<ReturnCwbs> returnCwbs = this.returnCwbsDAO.getReturnCwbsByCwb(cwbstr);
			;
			long isFeedbackcwb = this.customerDAO.getCustomerById(order.getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(order.getCustomerid()).getIsFeedbackcwb();
			if (isFeedbackcwb == 1) {
				returnCwbs = this.returnCwbsDAO.getReturnCwbsByCwbAll(cwbstr);
			}
			List<AorderFlowView> forReturnCwbs = new ArrayList<AorderFlowView>();
			if ((returnCwbs != null) && (returnCwbs.size() > 0)) {
				for (ReturnCwbs r : returnCwbs) {
					AorderFlowView a = new AorderFlowView();
					a.setTime(r.getCreatetime());
					a.setUsername(this.userDAO.getUserByUserid(r.getUserid()).getRealname());
					a.setContent("在<font color=\"red\">[" + this.branchDAO.getBranchByBranchid(r.getBranchid()).getBranchname() + "]</font>" + this.getReturnCwbsTypeName(r.getType()));
					forReturnCwbs.add(a);
				}
			}

			// 小件员委派信息
			List<AorderFlowView> forClient = new ArrayList<AorderFlowView>();
			List<OrderDeliveryClient> clientList = this.orderDeliveryClientDAO.getOrderDeliveryClientByCwb(oldCwb, 1);
			if ((clientList != null) && !clientList.isEmpty()) {
				for (OrderDeliveryClient c : clientList) {
					String deliverphone = this.getUserPhone(this.userDAO.getUserByUserid(c.getClientid()));
					AorderFlowView a = new AorderFlowView();
					a.setTime(c.getCreatetime());
					a.setUsername(this.userDAO.getUserByUserid(c.getUserid()).getRealname());
					a.setContent("货物从小件员<font color=\"red\">[" + this.userDAO.getUserByUserid(c.getDeliveryid()).getRealname() + "]</font>委托派送给小件员<font color=\"red\">["
							+ this.userDAO.getUserByUserid(c.getClientid()).getRealname() + "]</font>；小件员电话：<font color=\"red\">[" + deliverphone + "]</font>");
					forClient.add(a);
				}
			}

			List<AorderFlowView> as = new ArrayList<AorderFlowView>();// 临时list
			List<AorderFlowView> aorderFlowViews = new ArrayList<AorderFlowView>();// 总的订单流程list
			if ((forTrans != null) && (forTrans.size() > 0)) {
				as.addAll(forTrans);
			}
			if ((fororder != null) && (fororder.size() > 0)) {
				as.addAll(fororder);
			}
			if ((forabnormal != null) && (forabnormal.size() > 0)) {
				as.addAll(forabnormal);
			}
			if ((forAudit != null) && (forAudit.size() > 0)) {
				as.addAll(forAudit);
			}
			if ((forcomp != null) && (forcomp.size() > 0)) {
				as.addAll(forcomp);
			}
			if ((forReturnCwbs != null) && (forReturnCwbs.size() > 0)) {
				as.addAll(forReturnCwbs);
			}
			if ((forClient != null) && !forClient.isEmpty()) {
				as.addAll(forClient);
			}

			Map<String, AorderFlowView> map = new HashMap<String, AorderFlowView>();
			for (AorderFlowView a : as) {
				map.put(a.getTime() + a.getId() + "_" + as.indexOf(a), a);
			}
			List<String> keys = new ArrayList<String>(map.keySet());
			Collections.sort(keys);
			for (int j = 0; j < keys.size(); j++) {
				aorderFlowViews.add(map.get(keys.get(j)));
			}
			mapList.put("aorderFlowViews", aorderFlowViews);

			selectOrdersList.add(mapList);
		}

		model.addAttribute("selectOrdersList", selectOrdersList);

		return "/orderflow/queckSelectOrders";
	}

	// 快速查询功能中若为一票多件的，点击单个运单号的查询
	@RequestMapping("/queckSelectOrderright/{cwb}")
	public String queckSelectOrderright(Model model, @PathVariable(value = "cwb") String cwb) {
		String scancwb = cwb;
		String oldCwb = cwb;
		model.addAttribute("scancwb", scancwb);
		cwb = this.translateCwb(cwb);
		CwbOrder order = this.cwbDao.getCwbByCwb(cwb);
		String jspPage = "/orderflow/queckSelectOrderright";

		if (order == null) {
			return jspPage;
		}
		if (order.getCwbordertypeid() == CwbOrderTypeIdEnum.Express.getValue()) {
			jspPage = "/orderflow/deliveryOrderTrackingRight";
		} else {
			jspPage = "/orderflow/queckSelectOrderright";
		}
		QuickSelectView view = new QuickSelectView();
		BeanUtils.copyProperties(order, view);
		DeliveryState deliveryState = this.deliveryStateDAO.getActiveDeliveryStateByCwb(cwb);
		if (deliveryState != null) {
			BeanUtils.copyProperties(deliveryState, view);
		}
		List<TranscwbView> transcwbList = new ArrayList<TranscwbView>();
		long isypdjusetranscwb = this.customerDAO.getCustomerById(order.getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(order.getCustomerid()).getIsypdjusetranscwb();
		if (((order.getSendcarnum() > 1) || (order.getBackcarnum() > 1)) && ((order.getTranscwb().split(",").length > 1) || (order.getTranscwb().split(":").length > 1)) && (isypdjusetranscwb == 1)) {
			if (order.getTranscwb().indexOf(",") > -1) {
				for (String transcwb : order.getTranscwb().split(",")) {
					TranscwbOrderFlow tcof = this.transcwbOrderFlowDAO.getTranscwbOrderFlowByCwbAndState(transcwb, cwb);
					TranscwbView transcwbview = new TranscwbView();
					transcwbview.setCwb(order.getCwb());
					transcwbview.setTranscwb(transcwb);
					transcwbview.setFlowordername(tcof == null ? FlowOrderTypeEnum.DaoRuShuJu.getText() : tcof.getFlowordertypeText());					
					transcwbList.add(transcwbview);
				}
			} else {
				for (String transcwb : order.getTranscwb().split(":")) {
					TranscwbOrderFlow tcof = this.transcwbOrderFlowDAO.getTranscwbOrderFlowByCwbAndState(transcwb, cwb);
					TranscwbView transcwbview = new TranscwbView();
					transcwbview.setCwb(order.getCwb());
					transcwbview.setTranscwb(transcwb);
					transcwbview.setFlowordername(tcof == null ? FlowOrderTypeEnum.DaoRuShuJu.getText() : tcof.getFlowordertypeText());
					transcwbList.add(transcwbview);
				}
			}
			
			
		}
		model.addAttribute("transcwbList", transcwbList);

		List<OrderFlowView> views = new ArrayList<OrderFlowView>();
		List<TranscwbOrderFlowView> transcwbviews = new ArrayList<TranscwbOrderFlowView>();
		List<AorderFlowView> forTrans = new ArrayList<AorderFlowView>();
		List<AorderFlowView> fororder = new ArrayList<AorderFlowView>();
		if (((order.getSendcarnum() > 1) || (order.getBackcarnum() > 1)) && ((order.getTranscwb().split(",").length > 1) || (order.getTranscwb().split(":").length > 1)) && (isypdjusetranscwb == 1)) {
			boolean mpsSwitch =order.getIsmpsflag()>0;
			
			if (oldCwb.equals(order.getCwb())) {// 如果是原订单号查询，就获取和原来的订单流程
				List<OrderFlow> datalist = this.orderFlowDAO.getOrderFlowByCwb(cwb);
				for (OrderFlow orderFlowAll : datalist) {
					AorderFlowView a = new AorderFlowView();
					a.setId(orderFlowAll.getFloworderid());// 用于排序同时操作的相同时间的显示顺序
					a.setTime(orderFlowAll.getCredate().toString());
					a.setContent(this.getDetail(orderFlowAll));
					a.setUsername(this.userDAO.getUserByUserid(orderFlowAll.getUserid()).getRealname());
					fororder.add(a);
				}
				//如果开启集单模式走下面
				if(mpsSwitch){
				
					model.addAttribute("jdtype", 1); //1为开启集单模式
					if(order.getMpsoptstate() == 0){
						model.addAttribute("slowtranscwbtype",FlowOrderTypeEnum.DaoRuShuJu.getText());
					}else{
						model.addAttribute("slowtranscwbtype", FlowOrderTypeEnum.getByValue(order.getMpsoptstate()) == null ? FlowOrderTypeEnum.DaoRuShuJu.getText() : FlowOrderTypeEnum.getByValue(order.getMpsoptstate()).getText());
					}
				} 
				
			} else {// 就获取运单号的订单过程
				//String ypdjcurrentstate="";
				List<TranscwbOrderFlow> datalist = this.transcwbOrderFlowDAO.getTranscwbOrderFlowByScanCwb(scancwb, cwb);
				for (TranscwbOrderFlow transcwborderFlowAll : datalist) {
					AorderFlowView a = new AorderFlowView();
					a.setId(transcwborderFlowAll.getFloworderid());// 用于排序同时操作的相同时间的显示顺序
					a.setTime(transcwborderFlowAll.getCredate().toString());
					a.setContent(this.getDetailForYPDJ(transcwborderFlowAll));
					a.setUsername(this.userDAO.getUserByUserid(transcwborderFlowAll.getUserid()).getRealname());
					forTrans.add(a);
//					if(transcwborderFlowAll.getIsnow()==1){
//						ypdjcurrentstate=transcwborderFlowAll.getFlowordertypeText() == null ?  FlowOrderTypeEnum.DaoRuShuJu.getText() : transcwborderFlowAll.getFlowordertypeText();
//					}
				}
				//如果开启集单模式走下面
				if(mpsSwitch){
					TransCwbDetail transCwbDetail = tc.findTransCwbDetailByTransCwb(scancwb);
					model.addAttribute("jdtranstype",1);
					 
					
					if(transCwbDetail != null){
						model.addAttribute("transcwbstate",TransCwbStateEnum.getByValue(transCwbDetail.getTranscwbstate()) == null ? TransCwbStateEnum.PEISONG.getText() : TransCwbStateEnum.getByValue(transCwbDetail.getTranscwbstate()).getText());
						model.addAttribute("ypdjcurrentstate",FlowOrderTypeEnum.getByValue(transCwbDetail.getTranscwboptstate()) == null ? FlowOrderTypeEnum.DaoRuShuJu.getText() : FlowOrderTypeEnum.getByValue(transCwbDetail.getTranscwboptstate()).getText());
					}else{
						model.addAttribute("transcwbstate",TransCwbStateEnum.PEISONG.getText());
						model.addAttribute("ypdjcurrentstate",FlowOrderTypeEnum.DaoRuShuJu.getText());
					}
					
				}
			}

		} else {
			List<OrderFlow> datalist = this.orderFlowDAO.getOrderFlowByCwb(cwb);
			for (OrderFlow orderFlowAll : datalist) {
//				System.out.println(orderFlowAll.getCredate()+"    "+orderFlowAll.getCwb()+"   "+orderFlowAll.getFlowordertype());
				AorderFlowView a = new AorderFlowView();
				a.setId(orderFlowAll.getFloworderid());// 用于排序同时操作的相同时间的显示顺序
				a.setTime(orderFlowAll.getCredate().toString());
				a.setUsername(this.userDAO.getUserByUserid(orderFlowAll.getUserid()).getRealname());
				a.setContent(this.getDetail(orderFlowAll));
				fororder.add(a);
			}
		}
		// 取cwbdetail表中最新的deliverybranchid
		view.setDeliverybranchid(order.getDeliverybranchid());
		view.setCwbstate(order.getCwbstate());
		view.setOrderFlowList(views);
		view.setTranscwborderFlowList(transcwbviews);
		view.setNewpaywayid(view.getNewpaywayid());
		view.setBackreason(order.getBackreason());
		view.setLeavedreason(order.getLeavedreason());
		model.addAttribute("view", view);

		if (order.getCwbordertypeid() == CwbOrderTypeIdEnum.Express.getValue()) {
			PjDeliveryOrderService pjDeliveryOrderService = new PjDeliveryOrderServiceHelper.PjDeliveryOrderServiceClient();
			List<PjDeliveryTrackInfo> result = new ArrayList<PjDeliveryTrackInfo>();
			List<JoinMessageVO> resultShow = new ArrayList<JoinMessageVO>();
			//请求tps运单状态反馈 需注释
			/*try {
				result = pjDeliveryOrderService.getDeliveryOrderTracking(cwb);
			} catch (OspException e) {
				this.logger.info("请求TPS运单状态反馈接口异常,异常原因为{}", e.getMessage());
				model.addAttribute("aorderFlowViews", fororder);
				this.logger.error("", e);
				return jspPage;
			}*/
			if ((result == null) || (result.size() == 0)) {
				this.logger.info("请求TPS运单状态反馈接口异常,异常原因为{}");
				model.addAttribute("aorderFlowViews", fororder);
				return jspPage;
			}
			for (PjDeliveryTrackInfo temp : result) {
				this.logger.info("请求TPS运单状态反馈结果：" + JSON.toJSONString(temp));
				JoinMessageVO joinMessage = new JoinMessageVO();
				BeanUtils.copyProperties(temp, joinMessage);
				// joinMessage.connectMessage();
				joinMessage.replaceDetail();
				resultShow.add(joinMessage);
			}
			List<AorderFlowView> aorderFlowViews = new ArrayList<AorderFlowView>();// 总的订单流程list
			result = this.deliveryOrderTrackingService.orderList(result);// 订单排序
			int i = 0;
			for (JoinMessageVO temp : resultShow) {
				AorderFlowView a = new AorderFlowView();
				a.setId(i);// 用于排序同时操作的相同时间的显示顺序
				a.setTime(temp.getOperateTime());
				a.setUsername(temp.getOperator());
				// a.setContent(temp.getDescription());
				a.setContent(temp.getTransportDetail());
				aorderFlowViews.add(a);
			}
			model.addAttribute("aorderFlowViews", aorderFlowViews);
			return jspPage;
		}

		// 加问题件处理的过程

		List<AorderFlowView> forabnormal = new ArrayList<AorderFlowView>();
		List<AbnormalWriteBack> backList = this.abnormalWriteBackDAO.getAbnormalOrderByOpscwbid(order.getOpscwbid());

		if ((backList != null) && (backList.size() > 0)) {
			List<AbnormalType> alist = this.abnormalTypeDAO.getAllAbnormalTypeByNameAll();
			for (AbnormalWriteBack back : backList) {
				String typename = "--";
				User user = this.userDAO.getUserByUserid(back.getCreuserid());
				Branch branch = this.branchDAO.getBranchByBranchid(user.getBranchid());
				if (typename.equals("--")) {
					typename = this.getTypeName(back.getAbnormalordertype(), alist);
				}
				String describe = (back.getDescribe() == null) | "".equals(back.getDescribe()) ? "" : "备注：" + back.getDescribe();
				AorderFlowView a = new AorderFlowView();
				a.setTime(back.getCredatetime());
				a.setUsername(user.getRealname());
				a.setContent(AbnormalWriteBackEnum.getTextByValue(back.getType()) + "<font color=\"red\">[问题件]类型：[" + typename + "]</font>在[<font color=\"red\">" + branch.getBranchname() + "</font>]"
						+ describe);
				forabnormal.add(a);
			}
		}

		// 投诉的订单记录
		List<Complaint> comList = this.complaintDAO.getComplaintByCwb(cwb);
		List<AorderFlowView> forAudit = new ArrayList<AorderFlowView>();// 已审核投诉
		List<AorderFlowView> forcomp = new ArrayList<AorderFlowView>();// 受理投诉

		if ((comList != null) && (comList.size() > 0)) {
			for (Complaint complaint : comList) {
				AorderFlowView creobj = new AorderFlowView();
				User user1 = this.userDAO.getUserByUserid(complaint.getCreateUser());

				creobj.setTime(complaint.getCreateTime());
				creobj.setUsername(user1.getRealname());
				creobj.setContent("在[<font color=\" red\">" + this.branchDAO.getBranchByBranchid(user1.getBranchid()).getBranchname() + "</font>]受理为[<font color=\"red\">"
						+ this.getComplaintTypeName(complaint.getType()) + "</font>] 备注：<font color=\"red\">" + complaint.getContent() + "</font> ");
				forAudit.add(creobj);

				if (complaint.getAuditUser() != 0) {
					// 订单已经投诉处理过
					AorderFlowView auditobj = new AorderFlowView();
					User user2 = this.userDAO.getUserByUserid(complaint.getAuditUser());

					auditobj.setTime(complaint.getAuditTime());
					auditobj.setUsername(user2.getRealname());
					auditobj.setContent("在[<font color=\" red\">" + this.branchDAO.getBranchByBranchid(user2.getBranchid()).getBranchname() + "</font>]将创建于 <font color=\"#82b8ef\"> "
							+ complaint.getCreateTime() + "</font>  的  <font color=\"red\"> " + this.getComplaintTypeName(complaint.getType()) + "</font>   审核为[<font color=\"red\">"
							+ this.getComplaintAuditTypeName(complaint.getAuditType()) + "</font>] <br/> 备注：<font color=\"red\">" + complaint.getAuditRemark() + "</font> ");
					forAudit.add(auditobj);
				}
			}
		}

		// 返单信息
		List<ReturnCwbs> returnCwbs = this.returnCwbsDAO.getReturnCwbsByCwb(cwb);
		;
		long isFeedbackcwb = this.customerDAO.getCustomerById(order.getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(order.getCustomerid()).getIsFeedbackcwb();
		if (isFeedbackcwb == 1) {
			returnCwbs = this.returnCwbsDAO.getReturnCwbsByCwbAll(cwb);
		}

		List<AorderFlowView> forReturnCwbs = new ArrayList<AorderFlowView>();
		if ((returnCwbs != null) && (returnCwbs.size() > 0)) {
			for (ReturnCwbs r : returnCwbs) {
				AorderFlowView a = new AorderFlowView();
				a.setTime(r.getCreatetime());
				a.setUsername(this.userDAO.getUserByUserid(r.getUserid()).getRealname());
				a.setContent("在<font color=\"red\">[" + this.branchDAO.getBranchByBranchid(r.getBranchid()).getBranchname() + "]</font>" + this.getReturnCwbsTypeName(r.getType()));
				forReturnCwbs.add(a);
			}
		}

		// 小件员委派信息
		List<AorderFlowView> forClient = new ArrayList<AorderFlowView>();
		List<OrderDeliveryClient> clientList = this.orderDeliveryClientDAO.getOrderDeliveryClientByCwb(oldCwb, 1);
		if ((clientList != null) && !clientList.isEmpty()) {
			for (OrderDeliveryClient c : clientList) {
				String deliverphone = this.getUserPhone(this.userDAO.getUserByUserid(c.getClientid()));
				AorderFlowView a = new AorderFlowView();
				a.setTime(c.getCreatetime());
				a.setUsername(this.userDAO.getUserByUserid(c.getUserid()).getRealname());
				a.setContent("货物从小件员<font color=\"red\">[" + this.userDAO.getUserByUserid(c.getDeliveryid()).getRealname() + "]</font>委托派送给小件员<font color=\"red\">["
						+ this.userDAO.getUserByUserid(c.getClientid()).getRealname() + "]</font>；小件员电话：<font color=\"red\">[" + deliverphone + "]</font>");
				forClient.add(a);
			}
		}

		List<AorderFlowView> as = new ArrayList<AorderFlowView>();// 临时list
		List<AorderFlowView> aorderFlowViews = new ArrayList<AorderFlowView>();// 总的订单流程list
		if ((forTrans != null) && (forTrans.size() > 0)) {
			as.addAll(forTrans);
		}
		if ((fororder != null) && (fororder.size() > 0)) {
			as.addAll(fororder);
		}
		if ((forabnormal != null) && (forabnormal.size() > 0)) {
			as.addAll(forabnormal);
		}
		if ((forAudit != null) && (forAudit.size() > 0)) {
			as.addAll(forAudit);
		}
		if ((forcomp != null) && (forcomp.size() > 0)) {
			as.addAll(forcomp);
		}
		if ((forReturnCwbs != null) && (forReturnCwbs.size() > 0)) {
			as.addAll(forReturnCwbs);
		}
		if ((forClient != null) && !forClient.isEmpty()) {
			as.addAll(forClient);
		}

		Map<String, AorderFlowView> map = new HashMap<String, AorderFlowView>();
		for (AorderFlowView a : as) {
			map.put(a.getTime() + a.getId() + "_" + as.indexOf(a), a);
		}
		List<String> keys = new ArrayList<String>(map.keySet());
		Collections.sort(keys);
		for (int i = 0; i < keys.size(); i++) {
			aorderFlowViews.add(map.get(keys.get(i)));
		}
		model.addAttribute("aorderFlowViews", aorderFlowViews);
		return "/orderflow/queckSelectOrderright";
	}

	/**
	 *
	 * @Title: getStionInfo
	 * @description 当鼠标移动到轨迹的站点名称上市，浮窗显示站点信息
	 * @author 刘武强
	 * @date 2015年10月27日下午5:09:32
	 * @param @param operateOrgCode
	 * @param @return
	 * @return JSONObject
	 * @throws
	 */
	@RequestMapping("/getStionInfo")
	@ResponseBody
	public JSONObject getStionInfo(String carrierCode, String operateOrgCode) {
		JSONObject obj = new JSONObject();
		SbOrgService sbOrgService = new SbOrgServiceHelper.SbOrgServiceClient();
		List<SbOrgModel> resultList = new ArrayList<SbOrgModel>();
		try {
			resultList = sbOrgService.findSbOrgByCarrierAndSelfStation(carrierCode, operateOrgCode);
		} catch (Exception e) {
			this.logger.error("", e);
		}
		/*
		 * SbOrgModel a = new SbOrgModel(); a.setOrgName("测试站");
		 * a.setTelephone("15652549876"); a.setContactor("小刘");
		 * resultList.add(a);
		 */
		if ((resultList != null) && (resultList.size() > 0)) {
			obj.put("info", resultList.get(0));
		} else {
			obj.put("info", new SbOrgModel());
		}
		return obj;
	}

	@RequestMapping("/queckSelectOrderleft/{cwb}")
	public String queckSelectOrderleft(Model model, @PathVariable(value = "cwb") String cwb) throws Exception {
		cwb = this.translateCwb(cwb);
		CwbOrder order = this.cwbDao.getCwbByCwb(cwb);
		String jspPage = "";
		// 如果是快递单，则取快递单自己的左侧页面-刘武强10.20
		if ((order != null) && (order.getCwbordertypeid() == CwbOrderTypeIdEnum.Express.getValue())) {
			EmbracedOrderVO embracedOrder = this.expressOrderDao.getCwbOrderByCwb(cwb);
			embracedOrder = embracedOrder != null ? embracedOrder : new EmbracedOrderVO();
			embracedOrder.setConsignee_cellphone(SecurityUtil.getInstance().decrypt(embracedOrder.getConsignee_cellphone()));
			embracedOrder.setConsignee_telephone(SecurityUtil.getInstance().decrypt(embracedOrder.getConsignee_telephone()));			
			model.addAttribute("embracedOrder", embracedOrder);
			List<Customer> senderCustomer = this.customerDAO.getCustomerByCustomerid(embracedOrder.getSender_customerid() + "");
			List<Customer> consineerCustomer = this.customerDAO.getCustomerByCustomerid(embracedOrder.getConsignee_customerid() + "");

			model.addAttribute("senderCustomer", senderCustomer.size() > 0 ? senderCustomer.get(0) : new Customer());
			model.addAttribute("consineerCustomer", consineerCustomer.size() > 0 ? consineerCustomer.get(0) : new Customer());
			jspPage = "/orderflow/deliveryOrderTracking";
		} else {
			jspPage = "/orderflow/queckSelectOrderleft";
		}

		if (order == null) {
			return jspPage;
		}

		List<TranscwbView> transcwbList = new ArrayList<TranscwbView>();
		long isypdjusetranscwb = this.customerDAO.getCustomerById(order.getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(order.getCustomerid()).getIsypdjusetranscwb();
		if (((order.getSendcarnum() > 1) || (order.getBackcarnum() > 1)) && ((order.getTranscwb().split(",").length > 1) || (order.getTranscwb().split(":").length > 1)) && (isypdjusetranscwb == 1)) {
			if (order.getTranscwb().indexOf(",") > -1) {
				for (String transcwb : order.getTranscwb().split(",")) {
					if ("".equals(transcwb.trim())) {
						continue;
					}
					
					TranscwbOrderFlow tcof = this.transcwbOrderFlowDAO.getTranscwbOrderFlowByCwbAndState(transcwb, cwb);
					TranscwbView transcwbview = new TranscwbView();
					transcwbview.setCwb(order.getCwb());
					transcwbview.setTranscwb(transcwb);
					transcwbview.setFlowordername(tcof == null ? FlowOrderTypeEnum.DaoRuShuJu.getText() : tcof.getFlowordertypeText());
					//如果开启集单模式走下面
					boolean mpsSwitch=order.getIsmpsflag()>0;
					
					if(mpsSwitch){
					
						model.addAttribute("jdtype", 1); //1为开启集单模式
						TransCwbDetail transCwbDetail = tc.findTransCwbDetailByTransCwb(transcwb);
						
						if(transCwbDetail != null){
							transcwbview.setTranscwbstate(TransCwbStateEnum.getByValue(transCwbDetail.getTranscwbstate()).getText());
							transcwbview.setFlowordername(FlowOrderTypeEnum.getByValue(transCwbDetail.getTranscwboptstate()).getText());
						}else{
							transcwbview.setTranscwbstate(TransCwbStateEnum.PEISONG.getText());
							transcwbview.setFlowordername(FlowOrderTypeEnum.DaoRuShuJu.getText());
						}
					} 
					
					transcwbList.add(transcwbview);
					
//					TranscwbViewJiDanType  transcwbviewjd = new TranscwbViewJiDanType();
//					transcwbviewjd.setCwb(order.getCwb());
//					transcwbviewjd.setTranscwb(transcwb);
//					transcwbviewjd.setFlowordername(tc.findTransCwbDetailByTransCwb(transcwb).getTranscwbstate());
				}
			} else {
				for (String transcwb : order.getTranscwb().split(":")) {
					if ("".equals(transcwb.trim())) {
						continue;
					}
					TranscwbOrderFlow tcof = this.transcwbOrderFlowDAO.getTranscwbOrderFlowByCwbAndState(transcwb, cwb);
					TranscwbView transcwbview = new TranscwbView();
					transcwbview.setCwb(order.getCwb());
					transcwbview.setTranscwb(transcwb);
					transcwbview.setFlowordername(tcof == null ? FlowOrderTypeEnum.DaoRuShuJu.getText() : tcof.getFlowordertypeText());
					//如果开启集单模式走下面
					if(this.customerDAO.getCustomerById(order.getCustomerid()).getMpsswitch() != 0){
					
						model.addAttribute("jdtype", 1); //1为开启集单模式
						
						if(tc.findTransCwbDetailByTransCwb(transcwb) != null){
							transcwbview.setTranscwbstate(TransCwbStateEnum.getByValue(tc.findTransCwbDetailByTransCwb(transcwb).getTranscwbstate()).getText());
						}else{
							transcwbview.setTranscwbstate("");
						}
					} 
					transcwbList.add(transcwbview);
				}
			}
		}
		model.addAttribute("transcwbList", transcwbList);
		QuickSelectView view = new QuickSelectView();
		BeanUtils.copyProperties(order, view);
		DeliveryState deliveryState = this.deliveryStateDAO.getActiveDeliveryStateByCwb(cwb);
		if (deliveryState != null) {
			BeanUtils.copyProperties(deliveryState, view);
		}
		// 取cwbdetail表中最新的deliverybranchid
		view.setDeliverybranchid(order.getDeliverybranchid());
		view.setNewpaywayid(view.getNewpaywayid());
		view.setBackreason(order.getBackreason());
		view.setLeavedreason(order.getLeavedreason());
		view.setArea(order.getArea());
		view.setCity(order.getCity());
		CustomWareHouse customWareHouse = this.customWareHouseDAO.getWarehouseId(Long.parseLong(view.getCustomerwarehouseid()));
		String customWareHousename = customWareHouse == null ? "" : customWareHouse.getCustomerwarehouse();
		view.setCustomerwarehouseid(customWareHousename);

		List<OrderAddressRevise> orderAddressRevises = this.orderAddressReviseDao.getOrderAddressReviseByCwb(order.getCwb());
		String oldAddress = null;
		if ((orderAddressRevises != null) && (orderAddressRevises.size() > 0)) {
			String currentAddress = orderAddressRevises.get(0).getAddress();
			for (int i = 1; i < orderAddressRevises.size(); i++) {
				if (!currentAddress.trim().equals(orderAddressRevises.get(i).getAddress().trim())) {
					oldAddress = orderAddressRevises.get(i).getAddress().trim();
					break;
				}
			}
		}

		String oldconsigneeaddress = oldAddress == null ? "" : oldAddress;
		view.setOldconsigneeaddress(oldconsigneeaddress);
		
		view.setConsigneephone(SecurityUtil.getInstance().decrypt(view.getConsigneephone()));
		view.setConsigneemobile(SecurityUtil.getInstance().decrypt(view.getConsigneemobile()));
		
		List<OrderFlow> rukuList = this.orderFlowDAO.getOrderFlowByCwbAndFlowordertype(cwb, FlowOrderTypeEnum.RuKu.getValue(), "", "");
		List<OrderFlow> linghuoList = this.orderFlowDAO.getOrderFlowByCwbAndFlowordertype(cwb, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), "", "");
		Customer customer = this.customerDAO.getCustomerById(view.getCustomerid());
		Branch invarhousebranch = this.branchDAO.getBranchByBranchid(Integer.parseInt(view.getCarwarehouse() == null ? "0" : view.getCarwarehouse() == "" ? "0" : view.getCarwarehouse()));
		Branch deliverybranch = this.branchDAO.getBranchByBranchid(view.getDeliverybranchid());
		Branch nextbranch = this.branchDAO.getBranchByBranchid(view.getNextbranchid());
		GotoClassAuditing gotoClassAuditingGuiBan = this.gotoClassAuditingDAO.getGotoClassAuditingByGcaid(view.getGcaid());
		User deliveryname = this.userDAO.getUserByUserid(view.getDeliverid());
		Branch currentbranch = this.branchDAO.getBranchByBranchid(view.getCurrentbranchid());
		
		if(order.getIsmpsflag()==1 && order.getMpsallarrivedflag()!=1){
			model.addAttribute("isGathercomp", "0");
		}else{
			model.addAttribute("isGathercomp", "1");
		}
		
		model.addAttribute("deliveryname", deliveryname);
		model.addAttribute("customer", customer);
		model.addAttribute("invarhousebranch", invarhousebranch == null ? new Branch() : invarhousebranch);
		model.addAttribute("deliverybranch", deliverybranch == null ? new Branch() : deliverybranch);
		model.addAttribute("nextbranch", nextbranch == null ? new Branch() : nextbranch);
		model.addAttribute("startbranch", this.branchDAO.getBranchByBranchid(view.getStartbranchid()));
		model.addAttribute("currentbranch", currentbranch);
		model.addAttribute("orderFlowRuKu", rukuList.size() > 0 ? rukuList.get(rukuList.size() - 1) : new OrderFlow());
		model.addAttribute("orderFlowLingHuo", linghuoList.size() > 0 ? linghuoList.get(linghuoList.size() - 1) : new OrderFlow());
		model.addAttribute("gotoClassAuditingGuiBan", gotoClassAuditingGuiBan == null ? new GotoClassAuditing() : gotoClassAuditingGuiBan);
		model.addAttribute("deliveryChengGong", new DeliveryState());
		model.addAttribute("cwborder", this.cwbDao.getCwbByCwb(cwb) == null ? new CwbOrder() : this.cwbDao.getCwbByCwb(cwb));
		model.addAttribute("rejectiontime", new DeliveryState());
		model.addAttribute("view", view);
		// 只有客服才能在订单查询界面加备注
		model.addAttribute("userInBranchType", this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid()).getSitetype());
		return jspPage;
	}

	private String getComplaintTypeName(long complainttype) {
		String typeName = "";
		for (ComplaintTypeEnum complaintType : ComplaintTypeEnum.values()) {
			if (complainttype == complaintType.getValue()) {
				typeName = complaintType.getText();
				break;
			}
		}
		return typeName;
	}

	private String getComplaintAuditTypeName(long complaintaudittype) {
		String typeName = "";
		if (complaintaudittype > 0) {
			for (ComplaintAuditTypeEnum complaintAuditType : ComplaintAuditTypeEnum.values()) {
				if (complaintaudittype == complaintAuditType.getValue()) {
					typeName = complaintAuditType.getText();
					break;
				}
			}
		}
		return typeName;
	}

	private String getTypeName(long type, List<AbnormalType> alist) {
		String typeName = "";
		if ((alist != null) && (alist.size() > 0)) {
			for (AbnormalType abnormalType : alist) {
				if (type == abnormalType.getId()) {
					typeName = abnormalType.getName();
					break;
				}
			}
		}
		return typeName;
	}

	private ObjectMapper objectMapper = JacksonMapper.getInstance();

	private String getDetailForOutside(OrderFlow orderFlowAll) {
		try {
			CwbOrderWithDeliveryState cwbOrderWithDeliveryState = this.objectMapper.readValue(orderFlowAll.getFloworderdetail(), CwbOrderWithDeliveryState.class);
			CwbOrder cwbOrder = cwbOrderWithDeliveryState.getCwbOrder();
			String nextbranchname = this.getNextBranchName(cwbOrder);
			User user = this.getUser(orderFlowAll.getUserid());
			String phone = this.getUserPhone(user);
			String currentbranchname = this.branchDAO.getBranchByBranchid(orderFlowAll.getBranchid()).getBranchname();
			DeliveryState deliverystate = cwbOrderWithDeliveryState.getDeliveryState();
			DeliveryStateEnum deliverstateEnum = DeliveryStateEnum.HuoWuDiuShi;
			if (deliverystate != null) {
				deliverstateEnum = DeliveryStateEnum.getByValue((int) deliverystate.getDeliverystate());
			}

			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue()) {
				return MessageFormat.format("从<font color =\"red\">[{0}]</font>入库；联系电话：<font color =\"red\">[{1}]</font>；", currentbranchname, phone);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) {
				return MessageFormat.format("从<font color =\"red\">[{0}]</font>出库；下一站<font color =\"red\">[{1}]</font>，联系电话<font color =\"red\">[{2}]</font>；", currentbranchname, nextbranchname,
						phone);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue()) {
				return MessageFormat.format("从<font color =\"red\">[{0}]</font>库对库出库；下一站<font color =\"red\">[{1}]</font>，联系电话<font color =\"red\">[{2}]</font>；", currentbranchname, nextbranchname,
						phone);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) {
				phone = this.showBranchPhone(currentbranchname, phone);
				return MessageFormat.format("从<font color =\"red\">[{0}]</font>到货；联系电话：<font color =\"red\">[{1}]</font>；", currentbranchname, phone);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
				String deliverphone = this.getUserPhone(this.userDAO.getUserByUserid(cwbOrderWithDeliveryState.getDeliveryState().getDeliveryid()));
				return MessageFormat.format("货物已从<font color =\"red\">[{0}]</font>的小件员<font color =\"red\">[{1}]</font>分站领货；小件员电话：<font color =\"red\">[{2}]</font>", currentbranchname, this.userDAO
						.getUserByUserid(cwbOrderWithDeliveryState.getDeliveryState().getDeliveryid()).getRealname(), deliverphone);
			}
			if ((orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.YiFanKui.getValue()) && (deliverstateEnum != DeliveryStateEnum.HuoWuDiuShi)) {
				String deliverphone = this.getUserPhone(this.userDAO.getUserByUserid(cwbOrderWithDeliveryState.getDeliveryState().getDeliveryid()));
				return MessageFormat.format("货物已由<font color =\"red\">[{0}]</font>的小件员<font color =\"red\">[{1}]</font>反馈为<font color =\"red\">[{2}]</font>；小件员电话<font color =\"red\">[{3}]</font>；",
						currentbranchname, this.userDAO.getUserByUserid(cwbOrderWithDeliveryState.getDeliveryState().getDeliveryid()).getRealname(), deliverstateEnum.getText(), deliverphone);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.PosZhiFu.getValue()) {
				User users = this.userDAO.getUserByUserid(orderFlowAll.getUserid());
				return MessageFormat.format("移动POS支付，当前小件员<font color =\"red\">[{0}]</font>，", users.getRealname());
			}

		} catch (Exception e) {
			this.logger.error("", e);
			return null;
		}
		return null;
	}

	@RequestMapping("/showOrder/{cwb}")
	public String showOrder(Model model, @PathVariable(value = "cwb") String cwb) {
		cwb = this.translateCwb(cwb);
		CwbOrder order = this.cwbDao.getCwbByCwb(cwb);
		if (order == null) {
			return "/orderflow/orderNotExist";
		}
		QuickSelectView view = new QuickSelectView();
		BeanUtils.copyProperties(order, view);
		DeliveryState deliveryState = this.deliveryStateDAO.getActiveDeliveryStateByCwb(cwb);
		if (deliveryState != null) {
			BeanUtils.copyProperties(deliveryState, view);
		}

		List<OrderFlow> datalist = this.orderFlowDAO.getOrderFlowByCwb(cwb);
		List<OrderFlowView> views = new ArrayList<OrderFlowView>();
		for (OrderFlow orderFlowAll : datalist) {
			OrderFlowView orderFlowView = new OrderFlowView();
			orderFlowView.setCreateDate(orderFlowAll.getCredate());
			orderFlowView.setOperator(this.userDAO.getUserByUserid(orderFlowAll.getUserid()));
			orderFlowView.setId(orderFlowAll.getFloworderid());
			orderFlowView.setDetail(this.getDetail(orderFlowAll));
			views.add(orderFlowView);
		}

		// 取cwbdetail表中最新的deliverybranchid
		view.setDeliverybranchid(order.getDeliverybranchid());

		view.setOrderFlowList(views);
		view.setNewpaywayid(view.getNewpaywayid());
		view.setBackreason(order.getBackreason());
		view.setLeavedreason(order.getLeavedreason());

		List<OrderFlow> rukuList = this.orderFlowDAO.getOrderFlowByCwbAndFlowordertype(cwb, FlowOrderTypeEnum.RuKu.getValue(), "", "");
		List<OrderFlow> linghuoList = this.orderFlowDAO.getOrderFlowByCwbAndFlowordertype(cwb, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), "", "");
		Customer customer = this.customerDAO.getCustomerById(view.getCustomerid());
		Branch invarhousebranch = this.branchDAO.getBranchByBranchid(Integer.parseInt(view.getCarwarehouse() == null ? "0" : view.getCarwarehouse() == "" ? "0" : view.getCarwarehouse()));
		Branch deliverybranch = this.branchDAO.getBranchByBranchid(view.getDeliverybranchid());
		Branch nextbranch = this.branchDAO.getBranchByBranchid(view.getNextbranchid());
		GotoClassAuditing gotoClassAuditingGuiBan = this.gotoClassAuditingDAO.getGotoClassAuditingByGcaid(view.getGcaid());
		User deliveryname = this.userDAO.getUserByUserid(view.getDeliverid());

		model.addAttribute("deliveryname", deliveryname);
		model.addAttribute("customer", customer);
		model.addAttribute("invarhousebranch", invarhousebranch == null ? new Branch() : invarhousebranch);
		model.addAttribute("deliverybranch", deliverybranch == null ? new Branch() : deliverybranch);
		model.addAttribute("nextbranch", nextbranch == null ? new Branch() : nextbranch);
		model.addAttribute("startbranch", this.branchDAO.getBranchByBranchid(view.getStartbranchid()));
		model.addAttribute("currentbranch", this.branchDAO.getBranchByBranchid(view.getCurrentbranchid()));
		model.addAttribute("orderFlowRuKu", rukuList.size() > 0 ? rukuList.get(rukuList.size() - 1) : new OrderFlow());
		model.addAttribute("orderFlowLingHuo", linghuoList.size() > 0 ? linghuoList.get(linghuoList.size() - 1) : new OrderFlow());
		model.addAttribute("gotoClassAuditingGuiBan", gotoClassAuditingGuiBan == null ? new GotoClassAuditing() : gotoClassAuditingGuiBan);
		model.addAttribute("deliveryChengGong", new DeliveryState());
		model.addAttribute("cwborder", this.cwbDao.getCwbByCwb(cwb) == null ? new CwbOrder() : this.cwbDao.getCwbByCwb(cwb));
		model.addAttribute("rejectiontime", new DeliveryState());
		model.addAttribute("view", view);
		// 只有客服才能在订单查询界面加备注
		model.addAttribute("userInBranchType", this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid()).getSitetype());

		return "/complaint/showOrder";
	}

	private String getDetail(OrderFlow orderFlowAll) {
		try {
			String currentbranchname = this.branchDAO.getBranchByBranchid(orderFlowAll.getBranchid()).getBranchname();
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.ChaoQu.getValue()) {
				return MessageFormat.format("从<font color =\"red\">[{0}]</font>上报超区", currentbranchname);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.YiChangPiPeiYiChuLi.getValue()) {
				return MessageFormat.format("从<font color =\"red\">[{0}]</font>异常匹配已处理", currentbranchname);
			}
			CwbOrderWithDeliveryState cwbOrderWithDeliveryState = this.objectMapper.readValue(orderFlowAll.getFloworderdetail(), CwbOrderWithDeliveryState.class);
			CwbOrder cwbOrder = cwbOrderWithDeliveryState.getCwbOrder();
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.DaoRuShuJu.getValue()) {
				return MessageFormat.format("从<font color =\"red\">[{0}]</font>导入数据", currentbranchname);
			}
			String nextbranchname = StringUtil.nullConvertToEmptyString(this.getNextBranchName(cwbOrder));
			String deliverybranchname = StringUtil.nullConvertToEmptyString(this.getDeliveryBranchName(cwbOrder));
			String customername = StringUtil.nullConvertToEmptyString(this.customerDAO.getCustomerById(cwbOrder.getCustomerid()).getCustomername());
			User user = this.getUser(orderFlowAll.getUserid());
			String phone = StringUtil.nullConvertToEmptyString(this.getUserPhone(user));
			String comment = StringUtil.nullConvertToEmptyString(orderFlowAll.getComment()).trim();
			if (comment.length() > 0) {
				if (comment.lastIndexOf(",") == (comment.length() - 1)) {
					comment = comment.substring(0, comment.length() - 1);
				}
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.TiHuo.getValue()) {
				return MessageFormat.format("从<font color =\"red\">[{0}]</font>提货；供货商：<font color =\"red\">[{1}]</font>；联系电话：<font color =\"red\">[{2}]</font>；备注：<font color =\"red\">[{3}]</font>",
						currentbranchname, customername, phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue()) {
				return MessageFormat.format("从<font color =\"red\">[{0}]</font>入库；联系电话：<font color =\"red\">[{1}]</font>；备注：<font color =\"red\">[{2}]</font>", currentbranchname, phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()) {
				return MessageFormat.format("从<font color =\"red\">[{0}]</font>到错货入库；联系电话：<font color =\"red\">[{1}]</font>；备注：<font color =\"red\">[{2}]</font>", currentbranchname, phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.DaoCuoHuoChuLi.getValue()) {
				return MessageFormat.format("从<font color =\"red\">[{0}]</font>到错货处理；联系电话：<font color =\"red\">[{1}]</font>；备注：<font color =\"red\">[{2}]</font>", currentbranchname, phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.LanShouDaoHuo.getValue()) {
				return MessageFormat.format("从<font color =\"red\">[{0}]</font>揽收到货；联系电话：<font color =\"red\">[{1}]</font>；备注：<font color =\"red\">[{2}]</font>", currentbranchname, phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue()) {
				return MessageFormat.format("从<font color =\"red\">[{0}]</font>中转站入库；联系电话：<font color =\"red\">[{1}]</font>；备注：<font color =\"red\">[{2}]</font>", currentbranchname, phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) {
				return MessageFormat.format("从<font color =\"red\">[{0}]</font>出库；下一站<font color =\"red\">[{1}]</font>，目的站<font color =\"red\">[{2}]</font>；联系电话<font color =\"red\">[{3}]</font>；"
						+ "备注：<font color =\"red\">[{4}]</font>", currentbranchname, nextbranchname, deliverybranchname, phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue()) {
				return MessageFormat.format("从<font color =\"red\">[{0}]</font>中转站出库；下一站<font color =\"red\">[{1}]</font>，目的站<font color =\"red\">[{2}]</font>；联系电话<font color =\"red\">[{3}]</font>；"
						+ "备注：<font color =\"red\">[{4}]</font>", currentbranchname, nextbranchname, deliverybranchname, phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue()) {
				return MessageFormat.format("从<font color =\"red\">[{0}]</font>库对库出库；下一站<font color =\"red\">[{1}]</font>，目的站<font color =\"red\">[{2}]</font>；联系电话<font color =\"red\">[{3}]</font>；"
						+ "备注：<font color =\"red\">[{4}]</font>", currentbranchname, nextbranchname, deliverybranchname, phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.CheXiaoFanKui.getValue()) {
				return MessageFormat.format("货物由<font color =\"red\">[{0}]</font>撤销反馈；联系电话：<font color =\"red\">[{1}]</font>", currentbranchname, phone);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) {
				phone = this.showBranchPhone(currentbranchname, phone);
				return MessageFormat.format("从<font color =\"red\">[{0}]</font>到货；联系电话：<font color =\"red\">[{1}]</font>；备注：<font color =\"red\">[{2}]</font>", currentbranchname, phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
				// modified by jiangyu 20150917 begin
				// 反馈状态的获取
				DeliveryState deliveryState = cwbOrderWithDeliveryState.getDeliveryState();
				String deliverphone = "";// 小件员电话
				String realName = "";// 小件员姓名
				if (null != deliveryState) {
					User deliverMan = this.userDAO.getUserByUserid(deliveryState.getDeliveryid());
					deliverphone = this.getUserPhone(deliverMan);
					realName = deliverMan.getRealname();
				}

				if ((comment != null) && !comment.isEmpty() && comment.contains("正在派件") && comment.contains("派件人")) {

					return MessageFormat.format("货物已从<font color =\"red\">[{0}]</font>" + comment, currentbranchname);
				} else {
					return MessageFormat.format("货物已从<font color =\"red\">[{0}]</font>的小件员<font color =\"red\">[{1}]</font>分站领货；小件员电话：<font color =\"red\">[{2}]</font>", currentbranchname, realName,
							deliverphone);
				}
				// modified by jiangyu 20150917 end
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.YiFanKui.getValue()) {
				String deliverphone = this.getUserPhone(this.userDAO.getUserByUserid(cwbOrderWithDeliveryState.getDeliveryState().getDeliveryid()));
				return MessageFormat
						.format("货物已由<font color =\"red\">[{0}]</font>的小件员<font color =\"red\">[{1}]</font>反馈为<font color =\"red\">[{2}]</font>；小件员电话<font color =\"red\">[{3}]</font>；备注<font color =\"red\">[{4}]</font>；再次配送时间：<font color=\"red\">[{5}]</font>;未刷卡原因：<font color=\"red\">[{6}]</font>",
								currentbranchname, this.userDAO.getUserByUserid(cwbOrderWithDeliveryState.getDeliveryState().getDeliveryid()).getRealname(),
								DeliveryStateEnum.getByValue((int) cwbOrderWithDeliveryState.getDeliveryState().getDeliverystate()).getText(), deliverphone, comment, cwbOrder.getResendtime(),
								cwbOrder.getWeishuakareason());
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.TuiHuoChuZhan.getValue()) {
				return MessageFormat
						.format("货物已从<font color =\"red\">[{0}]</font>进行退货出库；下一站<font color =\"red\">[{1}]</font>，目的站<font color =\"red\">[{2}]</font>；联系电话：<font color =\"red\">[{3}]</font>；备注：<font color =\"red\">[{4}]</font>",
								currentbranchname, nextbranchname, deliverybranchname, phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()) {
				return MessageFormat.format("货物已到退货站<font color =\"red\">[{0}]</font>；联系电话：<font color =\"red\">[{1}]</font>；备注：<font color =\"red\">[{2}]</font>", currentbranchname, phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue()) {
				return MessageFormat.format("货物已由<font color =\"red\">[{0}]</font>退供货商出库；联系电话：<font color =\"red\">[{1}]</font>；备注：<font color =\"red\">[{2}]</font>", currentbranchname, phone,
						comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong.getValue()) {
				return MessageFormat.format("货物已由<font color =\"red\">[{0}]</font>退供货商成功；联系电话：<font color =\"red\">[{1}]</font>；备注：<font color =\"red\">[{2}]</font>", currentbranchname, phone,
						comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) {
				return MessageFormat.format("货物已由<font color =\"red\">[{0}]</font>的<font color =\"red\">[{1}]</font>审核；联系电话：<font color =\"red\">[{2}]</font>；备注：<font color =\"red\">[{3}]</font>",
						currentbranchname, this.userDAO.getUserByUserid(orderFlowAll.getUserid()).getRealname(), phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.UpdateDeliveryBranch.getValue()) {
				return MessageFormat.format("货物配送站点变更为<font color =\"red\">[{0}]</font>；操作人：<font color =\"red\">[{1}]</font>；联系电话：<font color =\"red\">[{2}]</font>", this.branchDAO
						.getBranchByBranchid(cwbOrder.getDeliverybranchid()).getBranchname(), this.userDAO.getUserByUserid(orderFlowAll.getUserid()).getRealname(), phone);
			}

			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.UpdatePickBranch.getValue()) {
				return MessageFormat.format("货物揽收站点变更为<font color =\"red\">[{0}]</font>；操作人：<font color =\"red\">[{1}]</font>；联系电话：<font color =\"red\">[{2}]</font>", this.branchDAO
						.getBranchByBranchid(cwbOrder.getPickbranchid()).getBranchname(), this.userDAO.getUserByUserid(orderFlowAll.getUserid()).getRealname(), phone);
			}

			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue()) {
				return MessageFormat.format("货物已由<font color =\"red\">[{0}]</font>退供货商拒收返库入库；联系电话：<font color =\"red\">[{1}]</font>；备注：<font color =\"red\">[{2}]</font>", currentbranchname, phone,
						comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.BeiZhu.getValue()) {
				return MessageFormat.format("货物被<font color =\"red\">[{0}]</font>添加了备注；联系电话；<font color =\"red\">[{1}]</font>；备注：<font color =\"red\">[{2}]</font>",
						this.userDAO.getUserByUserid(orderFlowAll.getUserid()).getRealname(), phone, comment);
			}

			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.PosZhiFu.getValue()) {
				User users = this.userDAO.getUserByUserid(orderFlowAll.getUserid());
				return MessageFormat.format("移动POS支付，当前小件员<font color =\"red\">[{0}]</font>，备注：<font color =\"red\">[{1}]</font>", users.getRealname(), comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.YiChangDingDanChuLi.getValue()) {
				return MessageFormat.format("货物被<font color =\"red\">[{0}]</font>审为异常订单处理；备注：<font color =\"red\">[{1}]</font>", this.userDAO.getUserByUserid(orderFlowAll.getUserid()).getRealname(),
						comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.DingDanLanJie.getValue()) {
				return MessageFormat.format("货物被<font color =\"red\">[{0}]</font>订单拦截；备注：<font color =\"red\">[{1}]</font>", this.userDAO.getUserByUserid(orderFlowAll.getUserid()).getRealname(),
						comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.ShenHeWeiZaiTou.getValue()) {
				return MessageFormat.format("货物被<font color =\"red\">[{0}]</font>审为退货再投；备注：<font color =\"red\">[{1}]</font>", this.userDAO.getUserByUserid(orderFlowAll.getUserid()).getRealname(),
						comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.ChaoQu.getValue()) {
				return MessageFormat.format("货物被<font color =\"red\">[{0}]</font>标记为超区；备注：<font color =\"red\">[{1}]</font>", this.userDAO.getUserByUserid(orderFlowAll.getUserid()).getRealname(),
						comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.YiChangPiPeiYiChuLi.getValue()) {
				return MessageFormat.format("货物被<font color =\"red\">[{0}]</font>异常匹配已处理；备注：<font color =\"red\">[{1}]</font>", this.userDAO.getUserByUserid(orderFlowAll.getUserid()).getRealname(),
						comment);
			}
			
//			-----------------------新增了    供货商拒收退货     这个状态,在查询时补入这个状态。
//			解决bug:历史遗留bug：客服退货出站审核为退供货商失败，查询订单是无日志记录和且状态还是维持退供货商出库的
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.GongYingShangJuShouTuiHuo.getValue()) {
				return MessageFormat.format("货物被<font color =\"red\">[{0}]</font>退供应商拒收退货；备注：<font color =\"red\">[{1}]</font>", this.userDAO.getUserByUserid(orderFlowAll.getUserid()).getRealname(),
						comment);
			}
//			-----------------------新增       退供应商拒收退货    状态        结束	
			
			// ==========================add begin by
			// wangzhiyu===========================================
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.YunDanLuRu.getValue()) {
				return MessageFormat.format("货物被<font color =\"red\">[{0}]</font>运单录入；备注：<font color =\"red\">[{1}]</font>", this.userDAO.getUserByUserid(orderFlowAll.getUserid()).getRealname(),
						comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.LanJianRuZhan.getValue()) {
				return MessageFormat.format("货物被<font color =\"red\">[{0}]</font>揽件入站；联系电话：<font color =\"red\">[{1}]</font>; 备注：<font color =\"red\">[{2}]</font>", this.userDAO.getUserByUserid(orderFlowAll.getUserid()).getRealname(),phone,
						comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.LanJianQueRen.getValue()) {
				return MessageFormat.format("货物被<font color =\"red\">[{0}]</font>揽件确认；联系电话：<font color =\"red\">[{1}]</font>; 备注：<font color =\"red\">[{2}]</font>", this.userDAO.getUserByUserid(orderFlowAll.getUserid()).getRealname(),phone,
						comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.LanJianChuZhan.getValue()) {
				return MessageFormat.format("货物被<font color =\"red\">[{0}]</font>揽件出站；联系电话：<font color =\"red\">[{1}]</font>; 备注：<font color =\"red\">[{2}]</font>", this.userDAO.getUserByUserid(orderFlowAll.getUserid()).getRealname(),phone,
						comment);
			}
			// ============================add
			// end=========================================================
			
			// add by vic.liang@pjbest.com 2016-08-20
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.BingEmsTrans.getValue()) {
				List<SendToEMSOrder> list = this.eMSDAO.getTransListByCwb(orderFlowAll.getCwb());
				String emsTrans = "";
				if (list != null) {
					for (SendToEMSOrder ems : list) {
						if (emsTrans.isEmpty())
							emsTrans += ems.getEmail_num();
						else 
							emsTrans += "," + ems.getEmail_num();
					}
				}
				return MessageFormat.format("货物在<font color =\"red\">[{0}]</font>转运邮政，单号：<font color =\"red\">[{1}]</font>", currentbranchname,emsTrans);
			}
			
		} catch (Exception e) {
			this.logger.error("", e);
			return null;
		}
		return null;
	}

	private String getDetailForYPDJ(TranscwbOrderFlow orderFlowAll) {
		try {
			CwbOrderWithDeliveryState cwbOrderWithDeliveryState = this.objectMapper.readValue(orderFlowAll.getFloworderdetail(), CwbOrderWithDeliveryState.class);
			CwbOrder cwbOrder = cwbOrderWithDeliveryState.getCwbOrder();
			String currentbranchname = this.branchDAO.getBranchByBranchid(orderFlowAll.getBranchid()).getBranchname();

			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.DaoRuShuJu.getValue()) {
				return MessageFormat.format("从<font color =\"red\">[{0}]</font>导入数据", currentbranchname);
			}
			String nextbranchname = this.getNextBranchName(cwbOrder);
			String deliverybranchname = this.getDeliveryBranchName(cwbOrder);
			String customername = this.customerDAO.getCustomerById(cwbOrder.getCustomerid()).getCustomername();
			User user = this.getUser(orderFlowAll.getUserid());
			String phone = this.getUserPhone(user);
			String comment = orderFlowAll.getComment();

			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.TiHuo.getValue()) {
				return MessageFormat.format("从<font color =\"red\">[{0}]</font>提货；供货商：<font color =\"red\">[{1}]</font>；联系电话：<font color =\"red\">[{2}]</font>；备注：<font color =\"red\">[{3}]</font>",
						currentbranchname, customername, phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue()) {
				return MessageFormat.format("从<font color =\"red\">[{0}]</font>入库；联系电话：<font color =\"red\">[{1}]</font>；备注：<font color =\"red\">[{2}]</font>", currentbranchname, phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()) {
				return MessageFormat.format("从<font color =\"red\">[{0}]</font>到错货入库；联系电话：<font color =\"red\">[{1}]</font>；备注：<font color =\"red\">[{2}]</font>", currentbranchname, phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.DaoCuoHuoChuLi.getValue()) {
				return MessageFormat.format("从<font color =\"red\">[{0}]</font>到错货处理；联系电话：<font color =\"red\">[{1}]</font>；备注：<font color =\"red\">[{2}]</font>", currentbranchname, phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue()) {
				return MessageFormat.format("从<font color =\"red\">[{0}]</font>中转站入库；联系电话：<font color =\"red\">[{1}]</font>；备注：<font color =\"red\">[{2}]</font>", currentbranchname, phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) {
				return MessageFormat.format("从<font color =\"red\">[{0}]</font>出库；下一站<font color =\"red\">[{1}]</font>，目的站<font color =\"red\">[{2}]</font>；联系电话<font color =\"red\">[{3}]</font>；"
						+ "备注：<font color =\"red\">[{4}]</font>", currentbranchname, nextbranchname, deliverybranchname, phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue()) {
				return MessageFormat.format("从<font color =\"red\">[{0}]</font>库对库出库；下一站<font color =\"red\">[{1}]</font>，目的站<font color =\"red\">[{2}]</font>；联系电话<font color =\"red\">[{3}]</font>；"
						+ "备注：<font color =\"red\">[{4}]</font>", currentbranchname, nextbranchname, deliverybranchname, phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue()) {
				return MessageFormat.format("从<font color =\"red\">[{0}]</font>中转站出库；下一站<font color =\"red\">[{1}]</font>，目的站<font color =\"red\">[{2}]</font>；联系电话<font color =\"red\">[{3}]</font>；"
						+ "备注：<font color =\"red\">[{4}]</font>", currentbranchname, nextbranchname, deliverybranchname, phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.CheXiaoFanKui.getValue()) {
				return MessageFormat.format("货物由<font color =\"red\">[{0}]</font>撤销反馈；联系电话：<font color =\"red\">[{1}]</font>", currentbranchname, phone);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) {
				phone = this.showBranchPhone(currentbranchname, phone);
				return MessageFormat.format("从<font color =\"red\">[{0}]</font>到货；联系电话：<font color =\"red\">[{1}]</font>；备注：<font color =\"red\">[{2}]</font>", currentbranchname, phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
				String deliverphone = this.getUserPhone(this.userDAO.getUserByUserid(cwbOrderWithDeliveryState.getDeliveryState().getDeliveryid()));
				return MessageFormat.format("货物已从<font color =\"red\">[{0}]</font>的小件员<font color =\"red\">[{1}]</font>分站领货；小件员电话：<font color =\"red\">[{2}]</font>", currentbranchname, this.userDAO
						.getUserByUserid(cwbOrderWithDeliveryState.getDeliveryState().getDeliveryid()).getRealname(), deliverphone);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.YiFanKui.getValue()) {
				String deliverphone = this.getUserPhone(this.userDAO.getUserByUserid(cwbOrderWithDeliveryState.getDeliveryState().getDeliveryid()));
				return MessageFormat
						.format("货物已由<font color =\"red\">[{0}]</font>的小件员<font color =\"red\">[{1}]</font>反馈为<font color =\"red\">[{2}]</font>；小件员电话<font color =\"red\">[{3}]</font>；备注<font color =\"red\">[{4}]</font>；再次配送时间：<font color=\"red\">[{5}]</font>;未刷卡原因：<font color=\"red\">[{6}]</font>",
								currentbranchname, this.userDAO.getUserByUserid(cwbOrderWithDeliveryState.getDeliveryState().getDeliveryid()).getRealname(),
								DeliveryStateEnum.getByValue((int) cwbOrderWithDeliveryState.getDeliveryState().getDeliverystate()).getText(), deliverphone, comment, cwbOrder.getResendtime(),
								cwbOrder.getWeishuakareason());
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.TuiHuoChuZhan.getValue()) {
				return MessageFormat
						.format("货物已从<font color =\"red\">[{0}]</font>进行退货出库；下一站<font color =\"red\">[{1}]</font>，目的站<font color =\"red\">[{2}]</font>；联系电话：<font color =\"red\">[{3}]</font>；备注：<font color =\"red\">[{4}]</font>",
								currentbranchname, nextbranchname, deliverybranchname, phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()) {
				return MessageFormat.format("货物已到退货站<font color =\"red\">[{0}]</font>；联系电话：<font color =\"red\">[{1}]</font>；备注：<font color =\"red\">[{2}]</font>", currentbranchname, phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue()) {
				return MessageFormat.format("货物已由<font color =\"red\">[{0}]</font>退供货商出库；联系电话：<font color =\"red\">[{1}]</font>；备注：<font color =\"red\">[{2}]</font>", currentbranchname, phone,
						comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong.getValue()) {
				return MessageFormat.format("货物已由<font color =\"red\">[{0}]</font>退供货商成功；联系电话：<font color =\"red\">[{1}]</font>；备注：<font color =\"red\">[{2}]</font>", currentbranchname, phone,
						comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) {
				return MessageFormat.format("货物已由<font color =\"red\">[{0}]</font>的<font color =\"red\">[{1}]</font>审核；联系电话：<font color =\"red\">[{2}]</font>；备注：<font color =\"red\">[{3}]</font>",
						currentbranchname, this.userDAO.getUserByUserid(orderFlowAll.getUserid()).getRealname(), phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.UpdateDeliveryBranch.getValue()) {
				return MessageFormat.format("货物配送站点变更为<font color =\"red\">[{0}]</font>；操作人：<font color =\"red\">[{1}]</font>；联系电话：<font color =\"red\">[{2}]</font>", this.branchDAO
						.getBranchByBranchid(cwbOrder.getDeliverybranchid()).getBranchname(), this.userDAO.getUserByUserid(orderFlowAll.getUserid()).getRealname(), phone);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.UpdatePickBranch.getValue()) {
				return MessageFormat.format("货物揽收站点变更为<font color =\"red\">[{0}]</font>；操作人：<font color =\"red\">[{1}]</font>；联系电话：<font color =\"red\">[{2}]</font>", this.branchDAO
						.getBranchByBranchid(cwbOrder.getPickbranchid()).getBranchname(), this.userDAO.getUserByUserid(orderFlowAll.getUserid()).getRealname(), phone);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue()) {
				return MessageFormat.format("货物已由<font color =\"red\">[{0}]</font>退供货商拒收返库入库；联系电话：<font color =\"red\">[{1}]</font>；备注：<font color =\"red\">[{2}]</font>", currentbranchname, phone,
						comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.BeiZhu.getValue()) {
				return MessageFormat.format("货物被<font color =\"red\">[{0}]</font>添加了备注；联系电话；<font color =\"red\">[{1}]</font>；备注：<font color =\"red\">[{2}]</font>",
						this.userDAO.getUserByUserid(orderFlowAll.getUserid()).getRealname(), phone, comment);
			}

			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.PosZhiFu.getValue()) {
				User users = this.userDAO.getUserByUserid(orderFlowAll.getUserid());
				return MessageFormat.format("移动POS支付，当前小件员<font color =\"red\">[{0}]</font>，备注：<font color =\"red\">[{1}]</font>", users.getRealname(), comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.YiChangDingDanChuLi.getValue()) {
				return MessageFormat.format("货物被<font color =\"red\">[{0}]</font>审为异常订单处理；备注：<font color =\"red\">[{1}]</font>", this.userDAO.getUserByUserid(orderFlowAll.getUserid()).getRealname(),
						comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.DingDanLanJie.getValue()) {
				return MessageFormat.format("货物被<font color =\"red\">[{0}]</font>订单拦截；备注：<font color =\"red\">[{1}]</font>", this.userDAO.getUserByUserid(orderFlowAll.getUserid()).getRealname(),
						comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.ShenHeWeiZaiTou.getValue()) {
				return MessageFormat.format("货物被<font color =\"red\">[{0}]</font>审为退货再投；备注：<font color =\"red\">[{1}]</font>", this.userDAO.getUserByUserid(orderFlowAll.getUserid()).getRealname(),
						comment);
			}

		} catch (Exception e) {
			this.logger.error("", e);
			return null;
		}
		return null;
	}

	private String getDetailExport(OrderFlow orderFlowAll) {
		try {
			CwbOrderWithDeliveryState cwbOrderWithDeliveryState = this.objectMapper.readValue(orderFlowAll.getFloworderdetail(), CwbOrderWithDeliveryState.class);
			CwbOrder cwbOrder = cwbOrderWithDeliveryState.getCwbOrder();
			String currentbranchname = this.branchDAO.getBranchByBranchid(orderFlowAll.getBranchid()).getBranchname();

			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.DaoRuShuJu.getValue()) {
				return MessageFormat.format("从[{0}]导入数据", currentbranchname);
			}
			String nextbranchname = StringUtil.nullConvertToEmptyString(this.getNextBranchName(cwbOrder));
			String deliverybranchname = StringUtil.nullConvertToEmptyString(this.getDeliveryBranchName(cwbOrder));
			User user = this.getUser(orderFlowAll.getUserid());
			String phone = StringUtil.nullConvertToEmptyString(this.getUserPhone(user));
			String comment = StringUtil.nullConvertToEmptyString(orderFlowAll.getComment());

			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue()) {
				return MessageFormat.format("从[{0}]入库；联系电话：[{1}]；备注：[{2}]", currentbranchname, phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()) {
				return MessageFormat.format("从[{0}]到错货入库；联系电话：[{1}]；备注：[{2}]", currentbranchname, phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.DaoCuoHuoChuLi.getValue()) {
				return MessageFormat.format("从[{0}]到错货处理；联系电话：[{1}]；备注：[{2}]", currentbranchname, phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.LanShouDaoHuo.getValue()) {
				return MessageFormat.format("从[{0}]揽收到货；联系电话：[{1}]；备注：[{2}]", currentbranchname, phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue()) {
				return MessageFormat.format("从[{0}]中转站入库；联系电话：[{1}]；备注：[{2}]", currentbranchname, phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) {
				return MessageFormat.format("从[{0}]出库；下一站[{1}]，目的站[{2}]；联系电话[{3}]；" + "备注：[{4}]", currentbranchname, nextbranchname, deliverybranchname, phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue()) {
				return MessageFormat.format("从[{0}]中转站出库；下一站[{1}]，目的站[{2}]；联系电话[{3}]；" + "备注：[{4}]", currentbranchname, nextbranchname, deliverybranchname, phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.KuDuiKuChuKuSaoMiao.getValue()) {
				return MessageFormat.format("从[{0}]库对库出库；下一站[{1}]，目的站[{2}]；联系电话[{3}]；" + "备注：[{4}]", currentbranchname, nextbranchname, deliverybranchname, phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.CheXiaoFanKui.getValue()) {
				return MessageFormat.format("货物由[{0}]撤销反馈；联系电话：[{1}]", currentbranchname, phone);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) {
				phone = this.showBranchPhone(currentbranchname, phone);
				return MessageFormat.format("从[{0}]到货；联系电话：[{1}]；备注：[{2}]", currentbranchname, phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
				String deliverphone = this.getUserPhone(this.userDAO.getUserByUserid(cwbOrderWithDeliveryState.getDeliveryState().getDeliveryid()));
				return MessageFormat.format("货物已从[{0}]的小件员[{1}]分站领货；小件员电话：[{2}]", currentbranchname, this.userDAO.getUserByUserid(cwbOrderWithDeliveryState.getDeliveryState().getDeliveryid())
						.getRealname(), deliverphone);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.YiFanKui.getValue()) {
				String deliverphone = this.getUserPhone(this.userDAO.getUserByUserid(cwbOrderWithDeliveryState.getDeliveryState().getDeliveryid()));
				return MessageFormat.format("货物已由[{0}]的小件员[{1}]反馈为[{2}]；小件员电话[{3}]；备注[{4}]", currentbranchname,
						this.userDAO.getUserByUserid(cwbOrderWithDeliveryState.getDeliveryState().getDeliveryid()).getRealname(),
						DeliveryStateEnum.getByValue((int) cwbOrderWithDeliveryState.getDeliveryState().getDeliverystate()).getText(), deliverphone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.TuiHuoChuZhan.getValue()) {
				return MessageFormat.format("货物已从[{0}]进行退货出库；下一站[{1}]，目的站[{2}]；联系电话：[{3}]；备注：[{4}]", currentbranchname, nextbranchname, deliverybranchname, phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()) {
				return MessageFormat.format("货物已到退货站[{0}]；联系电话：[{1}]；备注：[{2}]", currentbranchname, phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue()) {
				return MessageFormat.format("货物已由[{0}]退供货商出库；联系电话：[{1}]；备注：[{2}]", currentbranchname, phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.GongHuoShangTuiHuoChenggong.getValue()) {
				return MessageFormat.format("货物已由[{0}]退供货商成功；联系电话：[{1}]；备注：[{2}]", currentbranchname, phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) {
				return MessageFormat.format("货物已由[{0}]的[{1}]审核；联系电话：[{2}]；备注：[{3}]", currentbranchname, this.userDAO.getUserByUserid(orderFlowAll.getUserid()).getRealname(), phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.UpdateDeliveryBranch.getValue()) {
				return MessageFormat.format("货物配送站点变更为[{0}]；操作人：[{1}]；联系电话：[{2}]", this.branchDAO.getBranchByBranchid(cwbOrder.getDeliverybranchid()).getBranchname(),
						this.userDAO.getUserByUserid(orderFlowAll.getUserid()).getRealname(), phone);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.UpdatePickBranch.getValue()) {
				return MessageFormat.format("货物揽收站点变更为<font color =\"red\">[{0}]</font>；操作人：<font color =\"red\">[{1}]</font>；联系电话：<font color =\"red\">[{2}]</font>", this.branchDAO
						.getBranchByBranchid(cwbOrder.getPickbranchid()).getBranchname(), this.userDAO.getUserByUserid(orderFlowAll.getUserid()).getRealname(), phone);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue()) {
				return MessageFormat.format("货物已由[{0}]退供货商拒收返库入库；联系电话：[{1}]；备注：[{2}]", currentbranchname, phone, comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.BeiZhu.getValue()) {
				return MessageFormat.format("货物被[{0}]添加了备注；联系电话；[{1}]；备注：[{2}]", this.userDAO.getUserByUserid(orderFlowAll.getUserid()).getRealname(), phone, comment);
			}

			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.PosZhiFu.getValue()) {
				User users = this.userDAO.getUserByUserid(orderFlowAll.getUserid());
				return MessageFormat.format("移动POS支付，当前小件员[{0}]，备注：[{1}]", users.getRealname(), comment);
			}
			if (orderFlowAll.getFlowordertype() == FlowOrderTypeEnum.YiChangDingDanChuLi.getValue()) {
				return MessageFormat.format("货物被[{0}]审为异常订单处理；备注：[{1}]", this.userDAO.getUserByUserid(orderFlowAll.getUserid()).getRealname(), comment);
			}

		} catch (Exception e) {
			this.logger.error("", e);
			return null;
		}
		return null;
	}

	private String getReturnCwbsTypeName(long type) {
		String typeName = "";
		for (ReturnCwbsTypeEnum returnType : ReturnCwbsTypeEnum.values()) {
			if (type == returnType.getValue()) {
				typeName = returnType.getText();
				break;
			}
		}

		return typeName;
	}

	private String getUserPhone(User user) {
		if (user == null) {
			return "";
		}
		String phone = (user.getUserphone() == null) || (user.getUserphone().length() == 0) ? user.getUsermobile() : user.getUserphone();
		return phone;
	}

	private String getNextBranchName(CwbOrder cwborder) {
		Branch nextBranch = this.branchDAO.getBranchByBranchid(cwborder.getNextbranchid());
		if (nextBranch == null) {
			return "";
		}
		return nextBranch.getBranchname();
	}

	private String getDeliveryBranchName(CwbOrder cwborder) {
		Branch deliveryBranch = this.branchDAO.getBranchByBranchid(cwborder.getDeliverybranchid());
		if (deliveryBranch == null) {
			return "";
		}
		return deliveryBranch.getBranchname();
	}

	private User getUser(long userid) {
		return this.userDAO.getUserByUserid(userid);
	}

	private String getNewPayWayId(String cwb, String defaultNowPayWayid) {
		List<DeliveryState> deliveryStateList = this.deliveryStateDAO.getDeliveryStateByCwb(cwb);
		String newpaywayids = "";
		if (deliveryStateList.size() > 0) {
			for (DeliveryState ds : deliveryStateList) {
				if (ds.getCash().compareTo(BigDecimal.ZERO) > 0) {
					newpaywayids += "1,";
				}
				if (ds.getPos().compareTo(BigDecimal.ZERO) > 0) {
					newpaywayids += "2,";
				}
				if (ds.getCheckfee().compareTo(BigDecimal.ZERO) > 0) {
					newpaywayids += "3,";
				}
				if (ds.getOtherfee().compareTo(BigDecimal.ZERO) > 0) {
					newpaywayids += "4,";
				}
			}
		} else {
			newpaywayids += "1,";
		}
		if (newpaywayids.length() == 0) {
			newpaywayids = defaultNowPayWayid;
		}
		return newpaywayids.substring(0, newpaywayids.length() - 1);
	}

	@RequestMapping("/saveCwbRemark/{cwb}")
	public @ResponseBody String saveCwbRemark(Model model, @PathVariable(value = "cwb") String cwb, @RequestParam(value = "remark") String remark) {
		try {
			CwbOrder cwbOrder = this.cwbDao.getCwbByCwb(cwb);
			String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			String oldcwbremark = cwbOrder.getCwbremark().length() > 0 ? cwbOrder.getCwbremark() + "\n" : "";
			String newcwbremark = oldcwbremark + nowTime + "[" + this.getSessionUser().getRealname() + "]" + remark;
			this.cwbOrderService.updateCwbRemark(cwb, newcwbremark);
			JSONObject json = new JSONObject();
			json.put("errorCode", 0);
			json.put("remark", cwbOrder.getCwbremark() + "\n" + nowTime + "[" + this.getSessionUser().getRealname() + "]" + remark);
			return json.toString();
		} catch (Exception e) {
			JSONObject json = new JSONObject();
			json.put("errorCode", 1);
			json.put("remark", "保存失败,输入的备注过长");
			return json.toString();
		}
	}

	@RequestMapping("/orderFlowQueryByCwb")
	public String orderFlowQueryByCwb(Model model, @RequestParam(value = "cwb", required = false, defaultValue = "") String cwb, HttpServletResponse response, HttpServletRequest request) {
		SystemInstall telephoneSys = this.systemInstallDAO.getSystemInstallByName("telephone");
		String telephone = telephoneSys == null ? "" : telephoneSys.getValue();
		model.addAttribute("telephone", telephone);
		String remand = "";
		QuickSelectView view = new QuickSelectView();
		cwb = this.translateCwb(cwb);
		CwbOrder order = this.cwbDao.getCwbByCwb(cwb);
		if (order == null) {
			remand = "无此订单！";
			model.addAttribute("remand", remand);
			return "/orderflow/orderFlowQueryView";
		}
		BeanUtils.copyProperties(order, view);
		DeliveryState deliveryState = this.deliveryStateDAO.getActiveDeliveryStateByCwb(cwb);
		if (deliveryState != null) {
			BeanUtils.copyProperties(deliveryState, view);
		}
		List<OrderFlowView> views = new ArrayList<OrderFlowView>();
		List<OrderFlow> datalist = this.orderFlowDAO.getOrderFlowByCwb(cwb);
		if (datalist.size() == 0) {
			remand = "暂无订单流程！";
			model.addAttribute("remand", remand);
			return "/orderflow/orderFlowQueryView";
		}
		for (OrderFlow orderFlowAll : datalist) {
			OrderFlowView orderFlowView = new OrderFlowView();
			orderFlowView.setCreateDate(orderFlowAll.getCredate());
			orderFlowView.setOperator(this.userDAO.getUserByUserid(orderFlowAll.getUserid()));
			orderFlowView.setId(orderFlowAll.getFloworderid());
			orderFlowView.setDetail(this.getDetail(orderFlowAll));
			views.add(orderFlowView);
		}
		view.setOrderFlowList(views);
		// 取cwbdetail表中最新的deliverybranchid
		view.setDeliverybranchid(order.getDeliverybranchid());

		model.addAttribute("view", view);
		model.addAttribute("remand", remand);
		return "/orderflow/orderFlowQueryView";
	}

	/**
	 * 对外查询
	 *
	 * @param model
	 * @param cwb
	 * @param validateCode
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/orderFlowQueryForOutsideByCwb")
	public String orderFlowQueryForOutsideByCwb(Model model, @RequestParam(value = "cwb", required = false, defaultValue = "") String cwbs,
			@RequestParam(value = "validateCode", required = false, defaultValue = "") String validateCode, HttpServletResponse response, HttpServletRequest request) {

		if (validateCode.length() > 0) {
			if (this.checkYValidateCode(validateCode, request)) {
				List<JSONObject> viewList = new ArrayList<JSONObject>();
				List<Map<String, List<OrderFlowView>>> orderflowviewlist = new ArrayList<Map<String, List<OrderFlowView>>>();
				for (String cwb : cwbs.split("\r\n")) {
					if (cwb.trim().length() == 0) {
						continue;
					}
					if (!cwb.replace(" ", "").equals("")) {
						String remand = "";
						QuickSelectView view = new QuickSelectView();
						Map<String, List<OrderFlowView>> orderflowview = new HashMap<String, List<OrderFlowView>>();
						JSONObject obj = new JSONObject();
						cwb = this.translateCwb(cwb);
						CwbOrder order = this.cwbDao.getCwbByCwb(cwb);
						if (order == null) {
							remand = "无此订单！";
							/*
							 * model.addAttribute("remand", remand); return
							 * "/orderflow/orderFlowQueryForOutsideView";
							 */
							orderflowview.put(cwb, null);
							orderflowviewlist.add(orderflowview);

							obj.put("remand", remand);
							obj.put("cwb", cwb);
							viewList.add(obj);
							continue;
						}
						BeanUtils.copyProperties(order, view);
						DeliveryState deliveryState = this.deliveryStateDAO.getActiveDeliveryStateByCwb(cwb);
						if (deliveryState != null) {
							BeanUtils.copyProperties(deliveryState, view);
						}
						List<OrderFlowView> views = new ArrayList<OrderFlowView>();
						List<OrderFlow> datalist = this.orderFlowDAO.getOrderFlowByCwb(cwb);
						if (datalist.size() == 0) {
							remand = "暂无订单流程！";
							/*
							 * model.addAttribute("remand", remand); return
							 * "/orderflow/orderFlowQueryForOutsideView";
							 */
							orderflowview.put(cwb, null);
							orderflowviewlist.add(orderflowview);

							obj.put("remand", remand);
							obj.put("cwb", cwb);
							viewList.add(obj);
							continue;
						}
						for (OrderFlow orderFlowAll : datalist) {
							OrderFlowView orderFlowView = new OrderFlowView();
							orderFlowView.setCreateDate(orderFlowAll.getCredate());
							orderFlowView.setOperator(this.userDAO.getUserByUserid(orderFlowAll.getUserid()));
							orderFlowView.setId(orderFlowAll.getFloworderid());
							orderFlowView.setDetail(this.getDetailForOutside(orderFlowAll));
							views.add(orderFlowView);
						}
						view.setOrderFlowList(views);
						// 取cwbdetail表中最新的deliverybranchid
						view.setDeliverybranchid(order.getDeliverybranchid());

						orderflowview.put(cwb, views);
						orderflowviewlist.add(orderflowview);
						obj.put("remand", remand);
						obj.put("cwb", cwb);
						viewList.add(obj);
					}
				}
				model.addAttribute("viewList", viewList);
				model.addAttribute("orderflowviewlist", orderflowviewlist);
			} else {
				String remand = "验证码错误！";
				model.addAttribute("remand", remand);
				// return "/orderflow/orderFlowQueryForOutsideView";
			}
		}

		return "/orderflow/orderFlowQueryForOutsideView";
	}

	/**
	 * 小红帽-优速快递对外查询（没有验证码）
	 *
	 * @param model
	 * @param cwb
	 * @param validateCode
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/wumaorderFlowQueryForOutsideByCwb")
	public String wumaorderFlowQueryForOutsideByCwb(Model model, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs,
			@RequestParam(value = "sign", required = false, defaultValue = "") String sign, HttpServletResponse response, HttpServletRequest request) {

		List<JSONObject> viewList = new ArrayList<JSONObject>();
		String signStr = MD5Util.md5(cwbs + "explink");
		if (signStr.equals(sign)) {
			List<Map<String, List<OrderFlowView>>> orderflowviewlist = new ArrayList<Map<String, List<OrderFlowView>>>();
			List<Map<String, List<ComplaintsView>>> complaintsViewList = new ArrayList<Map<String, List<ComplaintsView>>>();
			List<Map<String, List<AbnormalWriteBackView>>> abnormalWriteBackViewList = new ArrayList<Map<String, List<AbnormalWriteBackView>>>();

			for (String cwb : cwbs.split("\r\n")) {
				if (cwb.trim().length() == 0) {
					continue;
				}
				String scancwb = cwb;
				String remand = "";
				JSONObject obj = new JSONObject();
				Map<String, List<OrderFlowView>> orderflowview = new HashMap<String, List<OrderFlowView>>();
				Map<String, List<ComplaintsView>> complaintsView = new HashMap<String, List<ComplaintsView>>();
				Map<String, List<AbnormalWriteBackView>> abnormalWriteBackView = new HashMap<String, List<AbnormalWriteBackView>>();

				cwb = this.translateCwb(cwb);
				CwbOrder order = this.cwbDao.getCwbByCwb(cwb);
				if (order == null) {
					remand = "无此订单！";
					obj.put("remand", remand);
					obj.put("cwb", cwb);
					viewList.add(obj);
					orderflowview.put(cwb, null);
					orderflowviewlist.add(orderflowview);
					complaintsView.put(cwb, null);
					complaintsViewList.add(complaintsView);
					abnormalWriteBackView.put(cwb, null);
					abnormalWriteBackViewList.add(abnormalWriteBackView);
					model.addAttribute("viewList", viewList);
					continue;
				}
				List<OrderFlowView> views = new ArrayList<OrderFlowView>();

				long isypdjusetranscwb = this.customerDAO.getCustomerById(order.getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(order.getCustomerid())
						.getIsypdjusetranscwb();

				if (((order.getSendcarnum() > 1) || (order.getBackcarnum() > 1)) && (order.getTranscwb().split(",").length > 1) && (isypdjusetranscwb == 1)) {

					if (scancwb.equals(order.getCwb())) {// 如果是原订单号查询，就获取和原来的订单流程
						List<OrderFlow> datalist = this.orderFlowDAO.getOrderFlowByCwb(cwb);
						if (datalist.size() == 0) {
							remand = "暂无订单流程！";
							obj.put("remand", remand);
							obj.put("cwb", cwb);
							viewList.add(obj);
							orderflowview.put(cwb, null);
							orderflowviewlist.add(orderflowview);
							complaintsView.put(cwb, null);
							complaintsViewList.add(complaintsView);
							abnormalWriteBackView.put(cwb, null);
							abnormalWriteBackViewList.add(abnormalWriteBackView);
							model.addAttribute("viewList", viewList);
							continue;
						}
						for (OrderFlow orderFlowAll : datalist) {
							OrderFlowView orderFlowView = new OrderFlowView();
							orderFlowView.setCreateDate(orderFlowAll.getCredate());
							orderFlowView.setOperator(this.userDAO.getUserByUserid(orderFlowAll.getUserid()));
							orderFlowView.setId(orderFlowAll.getFloworderid());
							orderFlowView.setDetail(this.getDetail(orderFlowAll));
							views.add(orderFlowView);
						}
					} else {// 就获取运单号的订单过程
						List<TranscwbOrderFlow> datalist = this.transcwbOrderFlowDAO.getTranscwbOrderFlowByScanCwb(scancwb, cwb);
						if (datalist.size() == 0) {
							remand = "暂无订单流程！";
							obj.put("remand", remand);
							obj.put("cwb", cwb);
							viewList.add(obj);
							orderflowview.put(cwb, null);
							orderflowviewlist.add(orderflowview);
							complaintsView.put(cwb, null);
							complaintsViewList.add(complaintsView);
							abnormalWriteBackView.put(cwb, null);
							abnormalWriteBackViewList.add(abnormalWriteBackView);
							model.addAttribute("viewList", viewList);
							continue;
						}
						for (TranscwbOrderFlow transcwborderFlowAll : datalist) {
							OrderFlowView orderFlowView = new OrderFlowView();
							orderFlowView.setCreateDate(transcwborderFlowAll.getCredate());
							orderFlowView.setOperator(this.userDAO.getUserByUserid(transcwborderFlowAll.getUserid()));
							orderFlowView.setId(transcwborderFlowAll.getFloworderid());
							orderFlowView.setDetail(this.getDetailForYPDJ(transcwborderFlowAll));
							views.add(orderFlowView);
						}
					}
				} else {
					List<OrderFlow> datalist = this.orderFlowDAO.getOrderFlowByCwb(cwb);
					for (OrderFlow orderFlowAll : datalist) {
						OrderFlowView orderFlowView = new OrderFlowView();
						orderFlowView.setCreateDate(orderFlowAll.getCredate());
						orderFlowView.setOperator(this.userDAO.getUserByUserid(orderFlowAll.getUserid()));
						orderFlowView.setId(orderFlowAll.getFloworderid());
						orderFlowView.setDetail(this.getDetail(orderFlowAll));
						views.add(orderFlowView);
					}
				}

				// 加问题件处理的过程
				List<AbnormalWriteBack> backList = this.abnormalWriteBackDAO.getAbnormalOrderByOpscwbid(order.getOpscwbid());
				List<AbnormalWriteBackView> backViewList = new ArrayList<AbnormalWriteBackView>();

				if ((backList != null) && (backList.size() > 0)) {
					List<AbnormalType> alist = this.abnormalTypeDAO.getAllAbnormalTypeByNameAll();
					for (AbnormalWriteBack back : backList) {
						String typename = "--";
						AbnormalWriteBackView backview = new AbnormalWriteBackView();
						User user = this.userDAO.getUserByUserid(back.getCreuserid());
						Branch branch = this.branchDAO.getBranchByBranchid(user.getBranchid());
						backview.setCwb(cwb);
						backview.setCredatetime(back.getCredatetime());
						backview.setUsername(user.getRealname());
						backview.setBranchname(branch.getBranchname());
						backview.setDescribe(back.getDescribe());
						if (typename.equals("--")) {
							typename = this.getTypeName(back.getAbnormalordertype(), alist);
						}
						backview.setTypename(typename);
						backViewList.add(backview);
					}
				}

				// 投诉的订单记录
				List<Complaint> comList = this.complaintDAO.getComplaintByCwb(cwb);
				List<ComplaintsView> comViewList = new ArrayList<ComplaintsView>();
				if ((comList != null) && (comList.size() > 0)) {
					for (Complaint complaint : comList) {
						ComplaintsView complaintview = new ComplaintsView();
						User user1 = this.userDAO.getUserByUserid(complaint.getCreateUser());
						User user2 = this.userDAO.getUserByUserid(complaint.getAuditUser());

						complaintview.setCwb(cwb);
						complaintview.setCreateTime(complaint.getCreateTime());
						complaintview.setAuditTime(complaint.getAuditTime());
						complaintview.setCreateUserName(user1.getRealname());
						complaintview.setAuditUserName(user2.getRealname());
						complaintview.setTypename(this.getComplaintTypeName(complaint.getType()));
						complaintview.setBranchname(this.branchDAO.getBranchByBranchid(user1.getBranchid()).getBranchname());
						complaintview.setContent(complaint.getContent());
						complaintview.setAuditTypeName(this.getComplaintAuditTypeName(complaint.getAuditType()));
						complaintview.setAuditRemark(complaint.getAuditRemark());
						comViewList.add(complaintview);
					}
				}

				orderflowview.put(cwb, views);
				orderflowviewlist.add(orderflowview);
				complaintsView.put(cwb, comViewList);
				complaintsViewList.add(complaintsView);
				abnormalWriteBackView.put(cwb, backViewList);
				abnormalWriteBackViewList.add(abnormalWriteBackView);
				obj.put("remand", remand);
				obj.put("cwb", cwb);

				viewList.add(obj);

				model.addAttribute("orderflowviewlist", orderflowviewlist);
				model.addAttribute("comViewList", complaintsViewList);
				model.addAttribute("abnormalWriteBackViewList", abnormalWriteBackViewList);
				model.addAttribute("viewList", viewList);
			}
		} else {
			String remand = "请求失败，请重试！";
			model.addAttribute("remand", remand);
		}
		return "/orderflow/wumaorderFlowQueryForOutsideView";
	}

	/**
	 * 没有验证码
	 *
	 * @param model
	 * @param cwbs
	 * @param sign
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/norderFlowQueryForOutsideByCwb")
	public String noorderFlowQueryForOutsideByCwb(Model model, @RequestParam(value = "cwb", required = false, defaultValue = "") String cwbs, HttpServletResponse response, HttpServletRequest request) {

		List<JSONObject> viewList = new ArrayList<JSONObject>();
		List<Map<String, List<OrderFlowView>>> orderflowviewlist = new ArrayList<Map<String, List<OrderFlowView>>>();
		List<Map<String, List<ComplaintsView>>> complaintsViewList = new ArrayList<Map<String, List<ComplaintsView>>>();
		List<Map<String, List<AbnormalWriteBackView>>> abnormalWriteBackViewList = new ArrayList<Map<String, List<AbnormalWriteBackView>>>();

		for (String cwb : cwbs.split("\r\n")) {
			if (cwb.trim().length() == 0) {
				continue;
			}
			String scancwb = cwb;
			String remand = "";
			JSONObject obj = new JSONObject();
			Map<String, List<OrderFlowView>> orderflowview = new HashMap<String, List<OrderFlowView>>();
			Map<String, List<ComplaintsView>> complaintsView = new HashMap<String, List<ComplaintsView>>();
			Map<String, List<AbnormalWriteBackView>> abnormalWriteBackView = new HashMap<String, List<AbnormalWriteBackView>>();

			cwb = this.translateCwb(cwb);
			CwbOrder order = this.cwbDao.getCwbByCwb(cwb);
			if (order == null) {
				remand = "无此订单！";
				obj.put("remand", remand);
				obj.put("cwb", cwb);
				viewList.add(obj);
				orderflowview.put(cwb, null);
				orderflowviewlist.add(orderflowview);
				complaintsView.put(cwb, null);
				complaintsViewList.add(complaintsView);
				abnormalWriteBackView.put(cwb, null);
				abnormalWriteBackViewList.add(abnormalWriteBackView);
				model.addAttribute("viewList", viewList);
				continue;
			}
			List<OrderFlowView> views = new ArrayList<OrderFlowView>();

			long isypdjusetranscwb = this.customerDAO.getCustomerById(order.getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(order.getCustomerid()).getIsypdjusetranscwb();

			if (((order.getSendcarnum() > 1) || (order.getBackcarnum() > 1)) && (order.getTranscwb().split(",").length > 1) && (isypdjusetranscwb == 1)) {

				if (scancwb.equals(order.getCwb())) {// 如果是原订单号查询，就获取和原来的订单流程
					List<OrderFlow> datalist = this.orderFlowDAO.getOrderFlowByCwb(cwb);
					if (datalist.size() == 0) {
						remand = "暂无订单流程！";
						obj.put("remand", remand);
						obj.put("cwb", cwb);
						viewList.add(obj);
						orderflowview.put(cwb, null);
						orderflowviewlist.add(orderflowview);
						complaintsView.put(cwb, null);
						complaintsViewList.add(complaintsView);
						abnormalWriteBackView.put(cwb, null);
						abnormalWriteBackViewList.add(abnormalWriteBackView);
						model.addAttribute("viewList", viewList);
						continue;
					}
					for (OrderFlow orderFlowAll : datalist) {
						OrderFlowView orderFlowView = new OrderFlowView();
						orderFlowView.setCreateDate(orderFlowAll.getCredate());
						orderFlowView.setOperator(this.userDAO.getUserByUserid(orderFlowAll.getUserid()));
						orderFlowView.setId(orderFlowAll.getFloworderid());
						orderFlowView.setDetail(this.getDetail(orderFlowAll));
						views.add(orderFlowView);
					}
				} else {// 就获取运单号的订单过程
					List<TranscwbOrderFlow> datalist = this.transcwbOrderFlowDAO.getTranscwbOrderFlowByScanCwb(scancwb, cwb);
					if (datalist.size() == 0) {
						remand = "暂无订单流程！";
						obj.put("remand", remand);
						obj.put("cwb", cwb);
						viewList.add(obj);
						orderflowview.put(cwb, null);
						orderflowviewlist.add(orderflowview);
						complaintsView.put(cwb, null);
						complaintsViewList.add(complaintsView);
						abnormalWriteBackView.put(cwb, null);
						abnormalWriteBackViewList.add(abnormalWriteBackView);
						model.addAttribute("viewList", viewList);
						continue;
					}
					for (TranscwbOrderFlow transcwborderFlowAll : datalist) {
						OrderFlowView orderFlowView = new OrderFlowView();
						orderFlowView.setCreateDate(transcwborderFlowAll.getCredate());
						orderFlowView.setOperator(this.userDAO.getUserByUserid(transcwborderFlowAll.getUserid()));
						orderFlowView.setId(transcwborderFlowAll.getFloworderid());
						orderFlowView.setDetail(this.getDetailForYPDJ(transcwborderFlowAll));
						views.add(orderFlowView);
					}
				}
			} else {
				List<OrderFlow> datalist = this.orderFlowDAO.getOrderFlowByCwb(cwb);
				for (OrderFlow orderFlowAll : datalist) {
					OrderFlowView orderFlowView = new OrderFlowView();
					orderFlowView.setCreateDate(orderFlowAll.getCredate());
					orderFlowView.setOperator(this.userDAO.getUserByUserid(orderFlowAll.getUserid()));
					orderFlowView.setId(orderFlowAll.getFloworderid());
					orderFlowView.setDetail(this.getDetail(orderFlowAll));
					views.add(orderFlowView);
				}
			}

			// 加问题件处理的过程
			List<AbnormalWriteBack> backList = this.abnormalWriteBackDAO.getAbnormalOrderByOpscwbid(order.getOpscwbid());
			List<AbnormalWriteBackView> backViewList = new ArrayList<AbnormalWriteBackView>();

			if ((backList != null) && (backList.size() > 0)) {
				List<AbnormalType> alist = this.abnormalTypeDAO.getAllAbnormalTypeByNameAll();
				for (AbnormalWriteBack back : backList) {
					String typename = "--";
					AbnormalWriteBackView backview = new AbnormalWriteBackView();
					User user = this.userDAO.getUserByUserid(back.getCreuserid());
					Branch branch = this.branchDAO.getBranchByBranchid(user.getBranchid());
					backview.setCwb(cwb);
					backview.setCredatetime(back.getCredatetime());
					backview.setUsername(user.getRealname());
					backview.setBranchname(branch.getBranchname());
					backview.setDescribe(back.getDescribe());
					if (typename.equals("--")) {
						typename = this.getTypeName(back.getAbnormalordertype(), alist);
					}
					backview.setTypename(typename);
					backViewList.add(backview);
				}
			}

			// 投诉的订单记录
			List<Complaint> comList = this.complaintDAO.getComplaintByCwb(cwb);
			List<ComplaintsView> comViewList = new ArrayList<ComplaintsView>();
			if ((comList != null) && (comList.size() > 0)) {
				for (Complaint complaint : comList) {
					ComplaintsView complaintview = new ComplaintsView();
					User user1 = this.userDAO.getUserByUserid(complaint.getCreateUser());
					User user2 = this.userDAO.getUserByUserid(complaint.getAuditUser());

					complaintview.setCwb(cwb);
					complaintview.setCreateTime(complaint.getCreateTime());
					complaintview.setAuditTime(complaint.getAuditTime());
					complaintview.setCreateUserName(user1.getRealname());
					complaintview.setAuditUserName(user2.getRealname());
					complaintview.setTypename(this.getComplaintTypeName(complaint.getType()));
					complaintview.setBranchname(this.branchDAO.getBranchByBranchid(user1.getBranchid()).getBranchname());
					complaintview.setContent(complaint.getContent());
					complaintview.setAuditTypeName(this.getComplaintAuditTypeName(complaint.getAuditType()));
					complaintview.setAuditRemark(complaint.getAuditRemark());
					comViewList.add(complaintview);
				}
			}

			orderflowview.put(cwb, views);
			orderflowviewlist.add(orderflowview);
			complaintsView.put(cwb, comViewList);
			complaintsViewList.add(complaintsView);
			abnormalWriteBackView.put(cwb, backViewList);
			abnormalWriteBackViewList.add(abnormalWriteBackView);
			obj.put("remand", remand);
			obj.put("cwb", cwb);

			viewList.add(obj);

			model.addAttribute("orderflowviewlist", orderflowviewlist);
			model.addAttribute("comViewList", complaintsViewList);
			model.addAttribute("abnormalWriteBackViewList", abnormalWriteBackViewList);
			model.addAttribute("viewList", viewList);
		}
		return "/orderflow/orderiframe";
	}

	/**
	 * 有验证码,显示所有环节
	 *
	 * @param model
	 * @param cwbs
	 * @param validateCode
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/orderFlowQueryNeedCheckAndAllByCwb")
	public String orderFlowQueryNeedCheckAndAllByCwb(Model model, @RequestParam(value = "cwb", required = false, defaultValue = "") String cwbs,
			@RequestParam(value = "validateCode", required = false, defaultValue = "") String validateCode, HttpServletResponse response, HttpServletRequest request) {
		if (validateCode.length() > 0) {
			if (this.checkYValidateCode(validateCode, request)) {
				List<JSONObject> viewList = new ArrayList<JSONObject>();
				List<Map<String, List<AorderFlowView>>> orderflowviewlist = new ArrayList<Map<String, List<AorderFlowView>>>();
				for (String cwb : cwbs.split("\r\n")) {
					if (cwb.trim().length() == 0) {
						continue;
					}
					String scancwb = cwb;
					String remand = "";
					String oldCwb = cwb;
					cwb = this.translateCwb(cwb);
					CwbOrder order = this.cwbDao.getCwbByCwb(cwb);
					Map<String, List<AorderFlowView>> orderflowview = new HashMap<String, List<AorderFlowView>>();
					JSONObject obj = new JSONObject();

					if (order == null) {
						remand = "无此订单！";
						orderflowview.put(cwb, null);
						orderflowviewlist.add(orderflowview);

						obj.put("remand", remand);
						obj.put("cwb", cwb);
						viewList.add(obj);
						model.addAttribute("viewList", viewList);
						continue;
					}

					long isypdjusetranscwb = this.customerDAO.getCustomerById(order.getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(order.getCustomerid())
							.getIsypdjusetranscwb();

					List<AorderFlowView> forTrans = new ArrayList<AorderFlowView>();
					List<AorderFlowView> fororder = new ArrayList<AorderFlowView>();
					if (((order.getSendcarnum() > 1) || (order.getBackcarnum() > 1)) && ((order.getTranscwb().split(",").length > 1) || (order.getTranscwb().split(":").length > 1))
							&& (isypdjusetranscwb == 1)) {

						if (oldCwb.equals(order.getCwb())) {// 如果是原订单号查询，就获取和原来的订单流程
							List<OrderFlow> datalist = this.orderFlowDAO.getOrderFlowByCwb(cwb);
							for (OrderFlow orderFlowAll : datalist) {
								AorderFlowView a = new AorderFlowView();
								a.setTime(orderFlowAll.getCredate().toString());
								a.setContent(this.getDetail(orderFlowAll));
								a.setUsername(this.userDAO.getUserByUserid(orderFlowAll.getUserid()).getRealname());
								fororder.add(a);
							}
						} else {// 就获取运单号的订单过程
							List<TranscwbOrderFlow> datalist = this.transcwbOrderFlowDAO.getTranscwbOrderFlowByScanCwb(scancwb, cwb);
							for (TranscwbOrderFlow transcwborderFlowAll : datalist) {
								AorderFlowView a = new AorderFlowView();
								a.setTime(transcwborderFlowAll.getCredate().toString());
								a.setContent(this.getDetailForYPDJ(transcwborderFlowAll));
								a.setUsername(this.userDAO.getUserByUserid(transcwborderFlowAll.getUserid()).getRealname());
								forTrans.add(a);
							}
						}

					} else {
						List<OrderFlow> datalist = this.orderFlowDAO.getOrderFlowByCwb(cwb);
						for (OrderFlow orderFlowAll : datalist) {
							AorderFlowView a = new AorderFlowView();
							a.setTime(orderFlowAll.getCredate().toString());
							a.setUsername(this.userDAO.getUserByUserid(orderFlowAll.getUserid()).getRealname());
							a.setContent(this.getDetail(orderFlowAll));
							fororder.add(a);
						}
					}

					// 加问题件处理的过程

					List<AorderFlowView> forabnormal = new ArrayList<AorderFlowView>();
					List<AbnormalWriteBack> backList = this.abnormalWriteBackDAO.getAbnormalOrderByOpscwbid(order.getOpscwbid());

					if ((backList != null) && (backList.size() > 0)) {
						List<AbnormalType> alist = this.abnormalTypeDAO.getAllAbnormalTypeByNameAll();
						for (AbnormalWriteBack back : backList) {
							String typename = "--";
							User user = this.userDAO.getUserByUserid(back.getCreuserid());
							Branch branch = this.branchDAO.getBranchByBranchid(user.getBranchid());
							if (typename.equals("--")) {
								typename = this.getTypeName(back.getAbnormalordertype(), alist);
							}
							String describe = (back.getDescribe() == null) | "".equals(back.getDescribe()) ? "创建" : "备注：" + back.getDescribe();
							AorderFlowView a = new AorderFlowView();
							a.setTime(back.getCredatetime());
							a.setUsername(user.getRealname());
							a.setContent("<font color=\"red\">[问题件]类型：[" + typename + "]</font>在[<font color=\"red\">" + branch.getBranchname() + "</font>]" + describe);
							forabnormal.add(a);
						}
					}

					// 投诉的订单记录
					List<Complaint> comList = this.complaintDAO.getComplaintByCwb(cwb);
					List<AorderFlowView> forAudit = new ArrayList<AorderFlowView>();// 已审核投诉
					List<AorderFlowView> forcomp = new ArrayList<AorderFlowView>();// 受理投诉

					if ((comList != null) && (comList.size() > 0)) {
						for (Complaint complaint : comList) {
							AorderFlowView creobj = new AorderFlowView();
							User user1 = this.userDAO.getUserByUserid(complaint.getCreateUser());

							creobj.setTime(complaint.getCreateTime());
							creobj.setUsername(user1.getRealname());
							creobj.setContent("在[<font color=\" red\">" + this.branchDAO.getBranchByBranchid(user1.getBranchid()).getBranchname() + "</font>]受理为[<font color=\"red\">"
									+ this.getComplaintTypeName(complaint.getType()) + "</font>] 备注：<font color=\"red\">" + complaint.getContent() + "</font> ");
							forAudit.add(creobj);

							if (complaint.getAuditUser() != 0) {
								// 订单已经投诉处理过
								AorderFlowView auditobj = new AorderFlowView();
								User user2 = this.userDAO.getUserByUserid(complaint.getAuditUser());

								auditobj.setTime(complaint.getAuditTime());
								auditobj.setUsername(user2.getRealname());
								auditobj.setContent("在[<font color=\" red\">" + this.branchDAO.getBranchByBranchid(user2.getBranchid()).getBranchname() + "</font>]将创建于 <font color=\"#82b8ef\"> "
										+ complaint.getCreateTime() + "</font>  的  <font color=\"red\"> " + this.getComplaintTypeName(complaint.getType()) + "</font>   审核为[<font color=\"red\">"
										+ this.getComplaintAuditTypeName(complaint.getAuditType()) + "</font>] <br/> 备注：<font color=\"red\">" + complaint.getAuditRemark() + "</font> ");
								forAudit.add(auditobj);
							}
						}
					}

					// 返单信息
					List<ReturnCwbs> returnCwbs = this.returnCwbsDAO.getReturnCwbsByCwb(cwb);
					;
					long isFeedbackcwb = this.customerDAO.getCustomerById(order.getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(order.getCustomerid()).getIsFeedbackcwb();
					if (isFeedbackcwb == 1) {
						returnCwbs = this.returnCwbsDAO.getReturnCwbsByCwbAll(cwb);
					}

					List<AorderFlowView> forReturnCwbs = new ArrayList<AorderFlowView>();
					if ((returnCwbs != null) && (returnCwbs.size() > 0)) {
						for (ReturnCwbs r : returnCwbs) {
							AorderFlowView a = new AorderFlowView();
							a.setTime(r.getCreatetime());
							a.setUsername(this.userDAO.getUserByUserid(r.getUserid()).getRealname());
							a.setContent("在<font color=\"red\">[" + this.branchDAO.getBranchByBranchid(r.getBranchid()).getBranchname() + "]</font>" + this.getReturnCwbsTypeName(r.getType()));
							forReturnCwbs.add(a);
						}
					}

					List<AorderFlowView> as = new ArrayList<AorderFlowView>();// 临时list
					List<AorderFlowView> aorderFlowViews = new ArrayList<AorderFlowView>();// 总的订单流程list
					if ((forTrans != null) && (forTrans.size() > 0)) {
						as.addAll(forTrans);
					}
					if ((fororder != null) && (fororder.size() > 0)) {
						as.addAll(fororder);
					}
					if ((forabnormal != null) && (forabnormal.size() > 0)) {
						as.addAll(forabnormal);
					}
					if ((forAudit != null) && (forAudit.size() > 0)) {
						as.addAll(forAudit);
					}
					if ((forcomp != null) && (forcomp.size() > 0)) {
						as.addAll(forcomp);
					}
					if ((forReturnCwbs != null) && (forReturnCwbs.size() > 0)) {
						as.addAll(forReturnCwbs);
					}

					Map<String, AorderFlowView> map = new HashMap<String, AorderFlowView>();
					for (AorderFlowView a : as) {
						map.put(a.getTime() + "_" + as.indexOf(a), a);
					}
					List<String> keys = new ArrayList<String>(map.keySet());
					Collections.sort(keys);
					for (int i = 0; i < keys.size(); i++) {
						aorderFlowViews.add(map.get(keys.get(i)));
					}

					obj.put("remand", remand);
					obj.put("cwb", cwb);
					viewList.add(obj);
					model.addAttribute("aorderFlowViews", aorderFlowViews);
					model.addAttribute("viewList", viewList);
				}
			} else {
				String remand = "验证码错误！";
				model.addAttribute("remand", remand);
			}
		}

		return "/orderflow/cwbCheckQueryForOutsideAllView";
	}

	private boolean checkYValidateCode(String validateCode, HttpServletRequest request) {
		if (request.getSession().getAttribute("validateYCode") != null) {
			if (request.getSession().getAttribute("validateYCode").equals(validateCode)) {
				return true;
			}
		}
		return false;
	}

	// 新版订单查询
	@RequestMapping("/orderQuery")
	public String query(Model model) {
		return "/neworderquery/order";
	}

	@RequestMapping("/left/{page}")
	public String left(Model model, @PathVariable(value = "page") long page, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs,
			@RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid,
			@RequestParam(value = "consigneename", required = false, defaultValue = "") String consigneename,
			@RequestParam(value = "consigneemobile", required = false, defaultValue = "") String consigneemobile,
			@RequestParam(value = "consigneeaddress", required = false, defaultValue = "") String consigneeaddress, @RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow,
			@RequestParam(value = "baleno", required = false, defaultValue = "") String baleno, @RequestParam(value = "transcwb", required = false, defaultValue = "") String transcwb,
			@RequestParam(value = "showLetfOrRight", required = false, defaultValue = "1") long showLetfOrRight,
			@RequestParam(value = "ischangetime", required = false, defaultValue = "0") long ischangetime) throws Exception {
		List<CwbOrder> clist = new ArrayList<CwbOrder>();
		Page pageparm = new Page();
		int isOpenFlag = this.jointService.getStateForJoint(B2cEnum.Amazon.getKey());
		model.addAttribute("isAmazonOpen", isOpenFlag);
		if (isshow != 0) {
			String enddate = DateDayUtil.getDateAfter(begindate, 10);
			clist = this.cwbOrderService.getListByCwbs(cwbs, begindate, enddate, customerid, consigneename, consigneemobile, consigneeaddress, baleno, transcwb, page);
			List<String> cwbList = new ArrayList<String>();
			for(CwbOrder c : clist) {
				String consigneemobileOfkfDecrypted = SecurityUtil.getInstance().decrypt(c.getConsigneemobileOfkf());
				consigneemobileOfkfDecrypted = hideMoiblePhoneCode(consigneemobileOfkfDecrypted);
				c.setConsigneemobileOfkf(consigneemobileOfkfDecrypted);
				cwbList.add(c.getCwb());
			}
			if(showLetfOrRight == 1) { // 按订单查询
				Map<String, List<BaleCwbClassifyVo>> baleCwbClassifyVoListMap = new HashMap<String, List<BaleCwbClassifyVo>>();
				Map<String, Integer> baleCwbSizeMap = new HashMap<String, Integer>();
				for(CwbOrder c : clist) {
					List<BaleCwbClassifyVo> baleCwbClassifyVoList = this.baleService.getBaleCwbClassifyVoList(c);
					baleCwbClassifyVoListMap.put(c.getCwb(), baleCwbClassifyVoList);
					int size = 0;
					for(BaleCwbClassifyVo vo : baleCwbClassifyVoList) {
						size += vo.getTranscwbList().size();
					}
					baleCwbSizeMap.put(c.getCwb(), size);
				}
			model.addAttribute("baleCwbClassifyVoListMap", baleCwbClassifyVoListMap);
				model.addAttribute("baleCwbSizeMap", baleCwbSizeMap);
			} else if(showLetfOrRight == 3) { // 按包号查询
				Map<String, BaleCwbClassifyVo> baleCwbClassifyVoMap = this.baleService.getBaleCwbClassifyVoMapByBaleno(cwbList, baleno);
				model.addAttribute("baleCwbClassifyVoMap", baleCwbClassifyVoMap);
			}
			model.addAttribute("customerMap", this.customerDAO.getAllCustomersToMap());
			pageparm = new Page(this.cwbOrderService.getCountByCwbs(cwbs, begindate, enddate, customerid, consigneename, consigneemobile, consigneeaddress, baleno, transcwb), page,
					Page.ONE_PAGE_NUMBER);
		}
		if (ischangetime == 0) {
			model.addAttribute("currentime", DateTimeUtil.getNowTime());
		} else {
			model.addAttribute("currentime", begindate);

		}
		model.addAttribute("orderlist", clist);
		model.addAttribute("customerlist", this.customerDAO.getAllCustomers());
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);
		model.addAttribute("showLetfOrRight", showLetfOrRight);
		model.addAttribute("cwbs", cwbs);
		return "/neworderquery/left";

	}
	
	private String hideMoiblePhoneCode(String consigneemobileOfkfDecrypted) {
		String result = "";
		if(consigneemobileOfkfDecrypted != null && consigneemobileOfkfDecrypted.length() > 4) {
			int length = consigneemobileOfkfDecrypted.length();
			result = consigneemobileOfkfDecrypted.substring(0, length - 4).replaceAll("\\d","*") 
					+ consigneemobileOfkfDecrypted.substring(length - 4);;
		} else {
			result = consigneemobileOfkfDecrypted;
		}
		return result;
	}

	@RequestMapping("/right/{cwb}")
	public String right(Model model, @PathVariable(value = "cwb") String cwb) throws Exception {
		CwbOrder order = this.cwbDao.getCwbByCwb(cwb);
		if (order == null) {
			model.addAttribute("view", null);
			return "/neworderquery/right";
		}
		QuickSelectView view = new QuickSelectView();
		BeanUtils.copyProperties(order, view);
		DeliveryState deliveryState = this.deliveryStateDAO.getActiveDeliveryStateByCwb(cwb);
		if (deliveryState != null) {
			BeanUtils.copyProperties(deliveryState, view);
		}
		List<OrderFlow> datalist = this.orderFlowDAO.getOrderFlowByCwb(cwb);
		List<OrderFlowView> views = new ArrayList<OrderFlowView>();
		for (OrderFlow orderFlowAll : datalist) {
			OrderFlowView orderFlowView = new OrderFlowView();
			orderFlowView.setCreateDate(orderFlowAll.getCredate());
			orderFlowView.setOperator(this.userDAO.getUserByUserid(orderFlowAll.getUserid()));
			orderFlowView.setId(orderFlowAll.getFloworderid());
			orderFlowView.setDetail(this.getDetail(orderFlowAll));
			views.add(orderFlowView);
		}
		// 取cwbdetail表中最新的deliverybranchid
		view.setDeliverybranchid(order.getDeliverybranchid());

		view.setOrderFlowList(views);
		view.setNewpaywayid(view.getNewpaywayid());
		view.setBackreason(order.getBackreason());
		view.setLeavedreason(order.getLeavedreason());
		view.setConsigneemobileOfkf(SecurityUtil.getInstance().decrypt(view.getConsigneemobileOfkf()));

		Customer customer = this.customerDAO.getCustomerById(view.getCustomerid());
		Branch deliverybranch = this.branchDAO.getBranchByBranchid(view.getDeliverybranchid());
		List<Remark> remarkList = this.remarkDAO.getRemarkByCwb(cwb);

		List<AccountArea> accountAreaList = this.accountAreaDAO.getAccountAreaByCustomerid(view.getCustomerid());
		AccountArea accountArea = new AccountArea();
		if ((accountAreaList != null) && (accountAreaList.size() > 0)) {
			accountArea = accountAreaList.get(0);
		}
		CustomWareHouse customWareHouse = this.customWareHouseDAO.getWarehouseId(Long.parseLong(view.getCustomerwarehouseid())) == null ? new CustomWareHouse() : this.customWareHouseDAO
				.getWarehouseId(Long.parseLong(view.getCustomerwarehouseid()));
		model.addAttribute("customWareHouse", customWareHouse);
		model.addAttribute("accountArea", accountArea);
		model.addAttribute("customer", customer);
		model.addAttribute("deliverybranch", deliverybranch == null ? new Branch() : deliverybranch);
		model.addAttribute("cwborder", this.cwbDao.getCwbByCwb(cwb) == null ? new CwbOrder() : this.cwbDao.getCwbByCwb(cwb));
		model.addAttribute("view", view);
		model.addAttribute("remarkList", remarkList);
		// 只有客服才能在订单查询界面加备注
		model.addAttribute("userInBranchType", this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid()).getSitetype());

		// 加问题件处理的过程
		List<AbnormalWriteBack> backList = this.abnormalWriteBackDAO.getAbnormalOrderByOpscwbid(order.getOpscwbid());
		List<AbnormalWriteBackView> backViewList = new ArrayList<AbnormalWriteBackView>();

		if ((backList != null) && (backList.size() > 0)) {
			List<AbnormalType> alist = this.abnormalTypeDAO.getAllAbnormalTypeByNameAll();

			for (AbnormalWriteBack back : backList) {
				String typename = "--";
				AbnormalWriteBackView backview = new AbnormalWriteBackView();
				User user = this.userDAO.getUserByUserid(back.getCreuserid());
				Branch branch = this.branchDAO.getBranchByBranchid(user.getBranchid());
				backview.setCwb(cwb);
				backview.setCredatetime(back.getCredatetime());
				backview.setUsername(user.getRealname());
				backview.setBranchname(branch.getBranchname());
				backview.setDescribe(back.getDescribe());
				if (typename.equals("--")) {
					typename = this.getTypeName(back.getAbnormalordertype(), alist);
				}
				backview.setTypename(typename);
				backViewList.add(backview);
			}
		}
		model.addAttribute("abnormalWriteBackViewList", backViewList);
		List<Branch> branchlist = this.branchDAO.getBranchBySiteType(BranchEnum.ZhanDian.getValue());
		List<User> userList = this.userDAO.getAllUser();
		List<PunishType> punishTypeList = this.punishTypeDAO.getAllPunishTypeByName();
		// List<Punish> punishList = this.punishDAO.getPunishByCwb(cwb);
		List<PenalizeInside> penalizeInsides = this.punishInsideDao.getInsidebycwb(cwb);
		String punishinsideShixiaoTime = this.systemInstallDAO.getSystemInstall("punishinsidezidongshenheshixiao") == null ? "" : this.systemInstallDAO.getSystemInstall(
				"punishinsidezidongshenheshixiao").getValue();
		model.addAttribute("branchlist", branchlist);
		model.addAttribute("punishList", penalizeInsides);
		model.addAttribute("userList", userList);
		model.addAttribute("punishTypeList", punishTypeList);
		model.addAttribute("punishinsideShixiaoTime", punishinsideShixiaoTime);
		return "/neworderquery/right";
	}

	@RequestMapping("/exportExcle")
	public void exportExcle(Model model, HttpServletResponse response, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs,
			@RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid,
			@RequestParam(value = "consigneename", required = false, defaultValue = "") String consigneename,
			@RequestParam(value = "consigneemobile", required = false, defaultValue = "") String consigneemobile,
			@RequestParam(value = "consigneeaddress", required = false, defaultValue = "") String consigneeaddress, @RequestParam(value = "baleno", required = false, defaultValue = "") String baleno,
			@RequestParam(value = "transcwb", required = false, defaultValue = "") String transcwb, @RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow,
			@RequestParam(value = "showLetfOrRight", required = false, defaultValue = "1") long showLetfOrRight) {
		String enddate = DateDayUtil.getDateAfter(begindate, 10);
		try {
			User us = this.getSessionUser();
			Branch deliverybranch = this.branchDAO.getBranchByBranchid(us.getBranchid());
			long count = this.cwbOrderService.getCountByCwbs(cwbs, begindate, enddate, customerid, consigneename, consigneemobile, consigneeaddress, baleno, transcwb);
			this.logger.info("订单数据量：" + count + ",大数据导出数据：用户名：{},站点：{}", us.getRealname(), deliverybranch.getBranchname());
		} catch (Exception e) {
			this.logger.error("大数据导出数据：获取用户名，站点异常");
		}
		List<CwbOrder> clist = this.cwbOrderService.getOrderList(cwbs, begindate, enddate, customerid, consigneename, consigneemobile, consigneeaddress, baleno, transcwb);
		List<OrderFlowExport> aorderFlowViews = new ArrayList<OrderFlowExport>();// 总的订单流程list
		if ((clist != null) && (clist.size() > 0)) {
			for (CwbOrder o : clist) {
				List<OrderFlow> datalist = this.orderFlowDAO.getOrderFlowByCwb(o.getCwb());
				List<OrderFlowExport> orderFlowList = new ArrayList<OrderFlowExport>();
				int i = 0;
				for (OrderFlow orderFlowAll : datalist) {
					OrderFlowExport orderFlowView = new OrderFlowExport();
					if (i == 0) {
						orderFlowView.setCwb(orderFlowAll.getCwb());
					}
					orderFlowView.setCreateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlowAll.getCredate()));
					orderFlowView.setOperatorName(this.userDAO.getUserByUserid(orderFlowAll.getUserid()).getRealname());
					orderFlowView.setId(orderFlowAll.getFloworderid());
					orderFlowView.setDetail(this.getDetailExport(orderFlowAll));
					orderFlowList.add(orderFlowView);

					i++;
				}

				List<Complaint> comList = this.complaintDAO.getComplaintByCwb(o.getCwb());
				List<OrderFlowExport> forAudit = new ArrayList<OrderFlowExport>();// 已审核投诉
				List<OrderFlowExport> forcomp = new ArrayList<OrderFlowExport>();// 受理投诉

				if ((comList != null) && (comList.size() > 0)) {
					for (Complaint complaint : comList) {
						OrderFlowExport comView = new OrderFlowExport();
						if (i == 0) {
							comView.setCwb(complaint.getCwb());
						}
						User user1 = this.userDAO.getUserByUserid(complaint.getCreateUser());
						comView.setCreateDate(complaint.getCreateTime());
						comView.setOperatorName(user1.getRealname());
						comView.setDetail("在" + this.branchDAO.getBranchByBranchid(user1.getBranchid()).getBranchname() + "受理为" + this.getComplaintTypeName(complaint.getType()) + " 备注："
								+ complaint.getContent());
						forAudit.add(comView);

						if (complaint.getAuditUser() != 0) {
							// 订单已经投诉处理过
							OrderFlowExport auditobj = new OrderFlowExport();
							User user2 = this.userDAO.getUserByUserid(complaint.getAuditUser());

							auditobj.setCreateDate(complaint.getAuditTime());
							auditobj.setOperatorName(user2.getRealname());
							auditobj.setDetail("在" + this.branchDAO.getBranchByBranchid(user2.getBranchid()).getBranchname() + "将创建于 " + complaint.getCreateTime() + "  的 "
									+ this.getComplaintTypeName(complaint.getType()) + "   审核为" + this.getComplaintAuditTypeName(complaint.getAuditType()) + " 备注：" + complaint.getAuditRemark());
							forcomp.add(auditobj);
						}
					}
					i++;
				}
				List<OrderFlowExport> as = new ArrayList<OrderFlowExport>();// 临时list

				if ((orderFlowList != null) && (orderFlowList.size() > 0)) {
					as.addAll(orderFlowList);
				}
				if ((forAudit != null) && (forAudit.size() > 0)) {
					as.addAll(forAudit);
				}
				if ((forcomp != null) && (forcomp.size() > 0)) {
					as.addAll(forcomp);
				}
				Map<String, OrderFlowExport> map = new HashMap<String, OrderFlowExport>();
				for (OrderFlowExport a : as) {
					map.put(a.getCreateDate() + "_" + as.indexOf(a), a);
				}
				List<String> keys = new ArrayList<String>(map.keySet());
				Collections.sort(keys);
				for (int m = 0; m < keys.size(); m++) {
					aorderFlowViews.add(map.get(keys.get(m)));
				}
			}
		}

		try {
			this.logger.info("大数据导出数据,订单过程条数：" + aorderFlowViews.size());
		} catch (Exception e) {
			this.logger.error("大数据导出数据：获取用户名，站点异常");
		}
		this.cwbOrderService.exportExcelMethod(response, aorderFlowViews);
	}

	@RequestMapping("/complaintlist/{page}")
	public String complaintlist(@PathVariable("page") long page, Model model, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs,
			@RequestParam(value = "consigneename", required = false, defaultValue = "") String consigneename,
			@RequestParam(value = "consigneemobile", required = false, defaultValue = "") String consigneemobile,
			@RequestParam(value = "consigneeaddress", required = false, defaultValue = "") String consigneeaddress,
			@RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow)
			throws ParseException {

		List<CwbOrder> clist = new ArrayList<CwbOrder>();
		Page pageparm = new Page();
		if (isshow != 0) {
			String enddate = "";
			if ((begindate != null) && (begindate.length() > 0)) {
				enddate = DateTimeUtil.getDateAfter(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(begindate).getTime(), 10);
			}

			clist = this.complaintService.getListByCwbs(cwbs, begindate, enddate, consigneename, consigneemobile, consigneeaddress, page);
			model.addAttribute("customerMap", this.customerDAO.getAllCustomersToMap());
			pageparm = new Page(this.complaintService.getCountByCwbs(cwbs, begindate, enddate, consigneename, consigneemobile, consigneeaddress), page, Page.ONE_PAGE_NUMBER);
		}
		model.addAttribute("orderlist", clist);
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);
		return "complaint/selectlist";
	}

	@RequestMapping("/exportComplaint")
	public void exportComplaint(HttpServletResponse response, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs,
			@RequestParam(value = "consigneename", required = false, defaultValue = "") String consigneename,
			@RequestParam(value = "consigneemobile", required = false, defaultValue = "") String consigneemobile,
			@RequestParam(value = "consigneeaddress", required = false, defaultValue = "") String consigneeaddress,
			@RequestParam(value = "begindate", required = false, defaultValue = "") String begindate) {
		// List<CwbOrder> clist = new ArrayList<CwbOrder>();
		if (cwbs.length() > 0) {
			StringBuffer str = new StringBuffer();
			String[] cwb = cwbs.trim().split("\r\n");
			List<String> cwbList = new ArrayList<String>();
			for (int i = 0; i < cwb.length; i++) {
				if (!cwbList.contains(cwb[i]) && (cwb[i].length() > 0)) {
					cwbList.add(cwb[i]);
					str = str.append("'").append(cwb[i]).append("',");
				}
			}
			cwbs = str.substring(0, str.length() - 1);
		}

		final String cwbsString = cwbs;
		String[] cloumnName1 = {}; // 导出的列名
		String[] cloumnName2 = {}; // 导出的英文列名
		String[] cloumnName3 = {}; // 导出的数据类型

		List<SetExportField> listSetExportField = this.exportmouldDAO.getSetExportFieldByStrs("0");
		cloumnName1 = new String[listSetExportField.size()];
		cloumnName2 = new String[listSetExportField.size()];
		cloumnName3 = new String[listSetExportField.size()];
		for (int k = 0, j = 0; j < listSetExportField.size(); j++, k++) {
			cloumnName1[k] = listSetExportField.get(j).getFieldname();
			cloumnName2[k] = listSetExportField.get(j).getFieldenglishname();
			cloumnName3[k] = listSetExportField.get(j).getExportdatatype();
		}
		final String[] cloumnName4 = cloumnName1;
		final String[] cloumnName5 = cloumnName2;
		final String[] cloumnName6 = cloumnName3;
		String sheetName = "订单信息"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Order_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			// 查询出数据
			if (begindate.length() == 0) {
				begindate = DateTimeUtil.getCurrentDayZeroTime().toString();
			}
			String enddate = DateTimeUtil.getDateAfter(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(begindate).getTime(), 10);
			final String sql = this.cwbDao.getSQLExportComplaint(cwbs, consigneename, consigneemobile, consigneeaddress, begindate, enddate);

			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					final List<User> uList = OrderSelectController.this.userDAO.getAllUser();
					final Map<Long, Customer> cMap = OrderSelectController.this.customerDAO.getAllCustomersToMap();
					final List<Branch> bList = OrderSelectController.this.branchDAO.getAllBranches();
					final List<Common> commonList = OrderSelectController.this.commonDAO.getAllCommons();
					final List<CustomWareHouse> cWList = OrderSelectController.this.customWareHouseDAO.getAllCustomWareHouse();
					List<Remark> remarkList = OrderSelectController.this.remarkDAO.getRemarkByCwbs(cwbsString);
					final Map<String, Map<String, String>> remarkMap = OrderSelectController.this.exportService.getInwarhouseRemarks(remarkList);
					final List<Reason> reasonList = OrderSelectController.this.reasonDAO.getAllReason();
					OrderSelectController.this.jdbcTemplate.query(new StreamingStatementCreator(sql), new ResultSetExtractor<Object>() {
						private int count = 0;
						ColumnMapRowMapper columnMapRowMapper = new ColumnMapRowMapper();
						private List<Map<String, Object>> recordbatch = new ArrayList<Map<String, Object>>();

						public void processRow(ResultSet rs) throws SQLException {

							Map<String, Object> mapRow = this.columnMapRowMapper.mapRow(rs, this.count);
							this.recordbatch.add(mapRow);
							this.count++;
							if ((this.count % 100) == 0) {
								this.writeBatch();
							}

						}

						private void writeSingle(Map<String, Object> mapRow, TuihuoRecord tuihuoRecord, DeliveryState ds, Map<String, String> allTime, int rownum, Map<String, String> cwbspayupidMap,
								Map<String, String> complaintMap) throws SQLException {
							Row row = sheet.createRow(rownum + 1);
							row.setHeightInPoints(15);
							for (int i = 0; i < cloumnName4.length; i++) {
								Cell cell = row.createCell((short) i);
								cell.setCellStyle(style);
								// sheet.setColumnWidth(i, (short) (5000));
								// //设置列宽
								Object a = OrderSelectController.this.exportService.setObjectA(cloumnName5, mapRow, i, uList, cMap, bList, commonList, tuihuoRecord, ds, allTime, cWList, remarkMap,
										reasonList, cwbspayupidMap, complaintMap);
								if (cloumnName6[i].equals("double")) {
									cell.setCellValue(a == null ? BigDecimal.ZERO.doubleValue() : a.equals("") ? BigDecimal.ZERO.doubleValue() : Double.parseDouble(a.toString()));
								} else {
									cell.setCellValue(a == null ? "" : a.toString());
								}
							}
						}

						@Override
						public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
							while (rs.next()) {
								this.processRow(rs);
							}
							this.writeBatch();
							return null;
						}

						public void writeBatch() throws SQLException {
							if (this.recordbatch.size() > 0) {
								List<String> cwbs = new ArrayList<String>();
								for (Map<String, Object> mapRow : this.recordbatch) {
									cwbs.add(mapRow.get("cwb").toString());
								}
								Map<String, DeliveryState> deliveryStates = this.getDeliveryListByCwbs(cwbs);
								Map<String, TuihuoRecord> tuihuorecoredMap = this.getTuihuoRecoredMap(cwbs);
								Map<String, String> cwbspayupMsp = this.getcwbspayupidMap(cwbs);
								Map<String, Map<String, String>> orderflowList = OrderSelectController.this.dataStatisticsService.getOrderFlowByCredateForDetailAndExportAllTime(cwbs, bList);
								Map<String, String> complaintMap = this.getComplaintMap(cwbs);

								int size = this.recordbatch.size();
								for (int i = 0; i < size; i++) {
									String cwb = this.recordbatch.get(i).get("cwb").toString();
									this.writeSingle(this.recordbatch.get(i), tuihuorecoredMap.get(cwb), deliveryStates.get(cwb), orderflowList.get(cwb), (this.count - size) + i, cwbspayupMsp,
											complaintMap);
								}
								this.recordbatch.clear();
							}
						}

						private Map<String, TuihuoRecord> getTuihuoRecoredMap(List<String> cwbs) {
							Map<String, TuihuoRecord> map = new HashMap<String, TuihuoRecord>();
							for (TuihuoRecord tuihuoRecord : OrderSelectController.this.tuihuoRecordDAO.getTuihuoRecordByCwbs(cwbs)) {
								map.put(tuihuoRecord.getCwb(), tuihuoRecord);
							}
							return map;
						}

						private Map<String, DeliveryState> getDeliveryListByCwbs(List<String> cwbs) {
							Map<String, DeliveryState> map = new HashMap<String, DeliveryState>();
							for (DeliveryState deliveryState : OrderSelectController.this.deliveryStateDAO.getActiveDeliveryStateByCwbs(cwbs)) {
								map.put(deliveryState.getCwb(), deliveryState);
							}
							return map;
						}

						private Map<String, String> getComplaintMap(List<String> cwbs) {
							Map<String, String> complaintMap = new HashMap<String, String>();
							for (Complaint complaint : OrderSelectController.this.complaintDAO.getActiveComplaintByCwbs(cwbs)) {
								complaintMap.put(complaint.getCwb(), complaint.getContent());
							}
							return complaintMap;
						}

						private Map<String, String> getcwbspayupidMap(List<String> cwbs) {
							Map<String, String> cwbspayupidMap = new HashMap<String, String>();
							/*
							 * for(DeliveryState deliveryState:deliveryStateDAO.
							 * getActiveDeliveryStateByCwbs(cwbs)){ String
							 * ispayup = "否"; GotoClassAuditing goclass =
							 * gotoClassAuditingDAO
							 * .getGotoClassAuditingByGcaid(deliveryState
							 * .getGcaid());
							 * 
							 * if(goclass!=null&&goclass.getPayupid()!=0){
							 * ispayup = "是"; }
							 * cwbspayupidMap.put(deliveryState.getCwb(),
							 * ispayup); }
							 */
							return cwbspayupidMap;
						}

					});

				}
			};
			excelUtil.excel(response, cloumnName4, sheetName, fileName);

		} catch (Exception e) {
			this.logger.error("", e);
		}

	}

	/**
	 * 订单数据 > 订单批量查询 多个订单追踪
	 *
	 * @param model
	 * @param request
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/batchselectorders")
	public String batchselectorders(Model model, HttpServletRequest request, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs) {
		List<Map<String, Object>> selectOrdersList = new ArrayList<Map<String, Object>>();
		String[] cwbsVar = cwbs.trim().split("\r\n");

		for (int i = 0; i < cwbsVar.length; i++) {
			if (cwbsVar[i].trim().length() == 0) {
				continue;
			}
			Map<String, Object> mapList = new HashMap<String, Object>();
			String cwbstr = cwbsVar[i];
			String scancwb = cwbstr;
			String oldCwb = cwbstr;

			mapList.put("scancwb", scancwb);

			cwbstr = this.translateCwb(cwbstr);
			CwbOrder order = this.cwbDao.getCwbByCwb(cwbstr);
			if (order == null) {
				continue;
			}

			QuickSelectView view = new QuickSelectView();
			BeanUtils.copyProperties(order, view);
			DeliveryState deliveryState = this.deliveryStateDAO.getActiveDeliveryStateByCwb(cwbstr);
			if (deliveryState != null) {
				BeanUtils.copyProperties(deliveryState, view);
			}

			List<TranscwbView> transcwbList = new ArrayList<TranscwbView>();
			long isypdjusetranscwb = this.customerDAO.getCustomerById(order.getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(order.getCustomerid()).getIsypdjusetranscwb();
			if (((order.getSendcarnum() > 1) || (order.getBackcarnum() > 1)) && ((order.getTranscwb().split(",").length > 1) || (order.getTranscwb().split(":").length > 1))
					&& (isypdjusetranscwb == 1)) {
				if (order.getTranscwb().indexOf(",") > -1) {
					for (String transcwb : order.getTranscwb().split(",")) {
						TranscwbOrderFlow tcof = this.transcwbOrderFlowDAO.getTranscwbOrderFlowByCwbAndState(transcwb, cwbstr);
						TranscwbView transcwbview = new TranscwbView();
						transcwbview.setCwb(order.getCwb());
						transcwbview.setTranscwb(transcwb);
						transcwbview.setFlowordername(tcof == null ? FlowOrderTypeEnum.DaoRuShuJu.getText() : tcof.getFlowordertypeText());
						transcwbList.add(transcwbview);
					}
				} else {
					for (String transcwb : order.getTranscwb().split(":")) {
						TranscwbOrderFlow tcof = this.transcwbOrderFlowDAO.getTranscwbOrderFlowByCwbAndState(transcwb, cwbstr);
						TranscwbView transcwbview = new TranscwbView();
						transcwbview.setCwb(order.getCwb());
						transcwbview.setTranscwb(transcwb);
						transcwbview.setFlowordername(tcof == null ? FlowOrderTypeEnum.DaoRuShuJu.getText() : tcof.getFlowordertypeText());
						transcwbList.add(transcwbview);
					}
				}
			}
			mapList.put("transcwbList", transcwbList);

			List<OrderFlowView> views = new ArrayList<OrderFlowView>();
			List<TranscwbOrderFlowView> transcwbviews = new ArrayList<TranscwbOrderFlowView>();
			List<AorderFlowView> forTrans = new ArrayList<AorderFlowView>();
			List<AorderFlowView> fororder = new ArrayList<AorderFlowView>();
			if (((order.getSendcarnum() > 1) || (order.getBackcarnum() > 1)) && ((order.getTranscwb().split(",").length > 1) || (order.getTranscwb().split(":").length > 1))
					&& (isypdjusetranscwb == 1)) {
				if (oldCwb.equals(order.getCwb())) {// 如果是原订单号查询，就获取和原来的订单流程
					List<OrderFlow> datalist = this.orderFlowDAO.getOrderFlowByCwb(cwbstr);
					for (OrderFlow orderFlowAll : datalist) {
						AorderFlowView a = new AorderFlowView();
						a.setId(orderFlowAll.getFloworderid());// 用于排序同时操作的相同时间的显示顺序
						a.setTime(orderFlowAll.getCredate().toString());
						a.setContent(this.getDetail(orderFlowAll));
						a.setUsername(this.userDAO.getUserByUserid(orderFlowAll.getUserid()).getRealname());
						fororder.add(a);
					}
				} else {// 就获取运单号的订单过程
					List<TranscwbOrderFlow> datalist = this.transcwbOrderFlowDAO.getTranscwbOrderFlowByScanCwb(scancwb, cwbstr);
					for (TranscwbOrderFlow transcwborderFlowAll : datalist) {
						AorderFlowView a = new AorderFlowView();
						a.setId(transcwborderFlowAll.getFloworderid());// 用于排序同时操作的相同时间的显示顺序
						a.setTime(transcwborderFlowAll.getCredate().toString());
						a.setContent(this.getDetailForYPDJ(transcwborderFlowAll));
						a.setUsername(this.userDAO.getUserByUserid(transcwborderFlowAll.getUserid()).getRealname());
						forTrans.add(a);
					}
				}

			} else {
				List<OrderFlow> datalist = this.orderFlowDAO.getOrderFlowByCwb(cwbstr);
				for (OrderFlow orderFlowAll : datalist) {
					AorderFlowView a = new AorderFlowView();
					a.setId(orderFlowAll.getFloworderid());// 用于排序同时操作的相同时间的显示顺序
					a.setTime(orderFlowAll.getCredate().toString());
					a.setUsername(this.userDAO.getUserByUserid(orderFlowAll.getUserid()).getRealname());
					a.setContent(this.getDetail(orderFlowAll));
					fororder.add(a);
				}
			}
			// 取cwbdetail表中最新的deliverybranchid
			view.setDeliverybranchid(order.getDeliverybranchid());
			view.setCwbstate(order.getCwbstate());
			view.setOrderFlowList(views);
			view.setTranscwborderFlowList(transcwbviews);
			view.setNewpaywayid(view.getNewpaywayid());
			view.setBackreason(order.getBackreason());
			view.setLeavedreason(order.getLeavedreason());
			mapList.put("view", view);

			// 加问题件处理的过程
			List<AorderFlowView> forabnormal = new ArrayList<AorderFlowView>();
			List<AbnormalWriteBack> backList = this.abnormalWriteBackDAO.getAbnormalOrderByOpscwbid(order.getOpscwbid());

			if ((backList != null) && (backList.size() > 0)) {
				List<AbnormalType> alist = this.abnormalTypeDAO.getAllAbnormalTypeByNameAll();
				for (AbnormalWriteBack back : backList) {
					String typename = "--";
					User user = this.userDAO.getUserByUserid(back.getCreuserid());
					Branch branch = this.branchDAO.getBranchByBranchid(user.getBranchid());
					if (typename.equals("--")) {
						typename = this.getTypeName(back.getAbnormalordertype(), alist);
					}
					String describe = (back.getDescribe() == null) | "".equals(back.getDescribe()) ? "" : "备注：" + back.getDescribe();
					AorderFlowView a = new AorderFlowView();
					a.setTime(back.getCredatetime());
					a.setUsername(user.getRealname());
					a.setContent(AbnormalWriteBackEnum.getTextByValue(back.getType()) + "<font color=\"red\">[问题件]类型：[" + typename + "]</font>在[<font color=\"red\">" + branch.getBranchname()
							+ "</font>]" + describe);
					forabnormal.add(a);
				}
			}

			// 投诉的订单记录
			List<Complaint> comList = this.complaintDAO.getComplaintByCwb(cwbstr);
			List<AorderFlowView> forAudit = new ArrayList<AorderFlowView>();// 已审核投诉
			List<AorderFlowView> forcomp = new ArrayList<AorderFlowView>();// 受理投诉
			if ((comList != null) && (comList.size() > 0)) {
				for (Complaint complaint : comList) {
					AorderFlowView creobj = new AorderFlowView();
					User user1 = this.userDAO.getUserByUserid(complaint.getCreateUser());

					creobj.setTime(complaint.getCreateTime());
					creobj.setUsername(user1.getRealname());
					creobj.setContent("在[<font color=\" red\">" + this.branchDAO.getBranchByBranchid(user1.getBranchid()).getBranchname() + "</font>]受理为[<font color=\"red\">"
							+ this.getComplaintTypeName(complaint.getType()) + "</font>] 备注：<font color=\"red\">" + complaint.getContent() + "</font> ");
					forAudit.add(creobj);

					if (complaint.getAuditUser() != 0) {
						// 订单已经投诉处理过
						AorderFlowView auditobj = new AorderFlowView();
						User user2 = this.userDAO.getUserByUserid(complaint.getAuditUser());

						auditobj.setTime(complaint.getAuditTime());
						auditobj.setUsername(user2.getRealname());
						auditobj.setContent("在[<font color=\" red\">" + this.branchDAO.getBranchByBranchid(user2.getBranchid()).getBranchname() + "</font>]将创建于 <font color=\"#82b8ef\"> "
								+ complaint.getCreateTime() + "</font>  的  <font color=\"red\"> " + this.getComplaintTypeName(complaint.getType()) + "</font>   审核为[<font color=\"red\">"
								+ this.getComplaintAuditTypeName(complaint.getAuditType()) + "</font>] <br/> 备注：<font color=\"red\">" + complaint.getAuditRemark() + "</font> ");
						forAudit.add(auditobj);
					}
				}
			}

			// 返单信息
			List<ReturnCwbs> returnCwbs = this.returnCwbsDAO.getReturnCwbsByCwb(cwbstr);
			;
			long isFeedbackcwb = this.customerDAO.getCustomerById(order.getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(order.getCustomerid()).getIsFeedbackcwb();
			if (isFeedbackcwb == 1) {
				returnCwbs = this.returnCwbsDAO.getReturnCwbsByCwbAll(cwbstr);
			}
			List<AorderFlowView> forReturnCwbs = new ArrayList<AorderFlowView>();
			if ((returnCwbs != null) && (returnCwbs.size() > 0)) {
				for (ReturnCwbs r : returnCwbs) {
					AorderFlowView a = new AorderFlowView();
					a.setTime(r.getCreatetime());
					a.setUsername(this.userDAO.getUserByUserid(r.getUserid()).getRealname());
					a.setContent("在<font color=\"red\">[" + this.branchDAO.getBranchByBranchid(r.getBranchid()).getBranchname() + "]</font>" + this.getReturnCwbsTypeName(r.getType()));
					forReturnCwbs.add(a);
				}
			}

			// 小件员委派信息
			List<AorderFlowView> forClient = new ArrayList<AorderFlowView>();
			List<OrderDeliveryClient> clientList = this.orderDeliveryClientDAO.getOrderDeliveryClientByCwb(oldCwb, 1);
			if ((clientList != null) && !clientList.isEmpty()) {
				for (OrderDeliveryClient c : clientList) {
					String deliverphone = this.getUserPhone(this.userDAO.getUserByUserid(c.getClientid()));
					AorderFlowView a = new AorderFlowView();
					a.setTime(c.getCreatetime());
					a.setUsername(this.userDAO.getUserByUserid(c.getUserid()).getRealname());
					a.setContent("货物从小件员<font color=\"red\">[" + this.userDAO.getUserByUserid(c.getDeliveryid()).getRealname() + "]</font>委托派送给小件员<font color=\"red\">["
							+ this.userDAO.getUserByUserid(c.getClientid()).getRealname() + "]</font>；小件员电话：<font color=\"red\">[" + deliverphone + "]</font>");
					forClient.add(a);
				}
			}

			List<AorderFlowView> as = new ArrayList<AorderFlowView>();// 临时list
			List<AorderFlowView> aorderFlowViews = new ArrayList<AorderFlowView>();// 总的订单流程list
			if ((forTrans != null) && (forTrans.size() > 0)) {
				as.addAll(forTrans);
			}
			if ((fororder != null) && (fororder.size() > 0)) {
				as.addAll(fororder);
			}
			if ((forabnormal != null) && (forabnormal.size() > 0)) {
				as.addAll(forabnormal);
			}
			if ((forAudit != null) && (forAudit.size() > 0)) {
				as.addAll(forAudit);
			}
			if ((forcomp != null) && (forcomp.size() > 0)) {
				as.addAll(forcomp);
			}
			if ((forReturnCwbs != null) && (forReturnCwbs.size() > 0)) {
				as.addAll(forReturnCwbs);
			}
			if ((forClient != null) && !forClient.isEmpty()) {
				as.addAll(forClient);
			}

			Map<String, AorderFlowView> map = new HashMap<String, AorderFlowView>();
			for (AorderFlowView a : as) {
				map.put(a.getTime() + a.getId() + "_" + as.indexOf(a), a);
			}
			List<String> keys = new ArrayList<String>(map.keySet());
			Collections.sort(keys);
			for (int j = 0; j < keys.size(); j++) {
				aorderFlowViews.add(map.get(keys.get(j)));
			}
			mapList.put("aorderFlowViews", aorderFlowViews);

			selectOrdersList.add(mapList);
		}

		model.addAttribute("selectOrdersList", selectOrdersList);

		return "/orderflow/queckSelectOrders";
	}

	public String showBranchPhone(String currentbranchname, String phone) {
		try {

			SystemInstall systemInstall = this.systemInstallDAO.getSystemInstallByName("showbranchPhone");
			systemInstall = systemInstall == null ? new SystemInstall() : systemInstall;
			if ((systemInstall.getValue() != null) && systemInstall.getValue().equalsIgnoreCase("yes")) {
				Branch branch = this.branchDAO.getBranchByBranchname(currentbranchname);
				branch = branch == null ? new Branch() : branch;
				phone = branch.getBranchphone();
			}

		} catch (Exception e) {
			return phone;
		}
		return phone;
	}
	@RequestMapping(value="/queryOrderFlowByCwb",method=RequestMethod.POST)
	@ResponseBody
	public String  queryOrderFlowByCwb(@RequestParam(value="cwb",required=true,defaultValue="")String cwb,@RequestParam(value="requestSign",required=true,defaultValue="")String requestSign) throws Exception{
		this.logger.info("请求参数：cwb={},requestSign={}",cwb,requestSign);
		QueryCwbResponseDto cwbResponseDto=new QueryCwbResponseDto();
		if(!StringUtils.hasText(cwb)||!StringUtils.hasText(requestSign)){
			this.logger.info("请求参数不全");
			cwbResponseDto.setReturnCode("02");
			cwbResponseDto.setRemark("请求参数不全");
			return JsonUtil.fromObject(cwbResponseDto);
		}
		String sign = MD5Util.md5(cwb.trim()+"explink", "UTF-8");
		if(!sign.equals(requestSign)){
			this.logger.info("签名验证失败：requestSign={},sign={}",requestSign,sign);
			cwbResponseDto.setReturnCode("01");
			cwbResponseDto.setRemark("签名失败");
			return JsonUtil.fromObject(cwbResponseDto);
		}
		List<OrderFlow> datalist = this.orderFlowDAO.getOrderFlowByCwb(cwb.trim());
		if(datalist.isEmpty()){
			this.logger.info("未检索到数据");
			cwbResponseDto.setReturnCode("03");
			cwbResponseDto.setRemark("未检索到数据");
			return JsonUtil.fromObject(cwbResponseDto);
		}
		
		List<QueryOrder> queryOrders=new ArrayList<QueryOrder>();
		for (OrderFlow orderFlow : datalist) {
			QueryOrder order=new QueryOrder();
			CwbOrderWithDeliveryState cwbOrderWithDeliveryState = this.objectMapper.readValue(orderFlow.getFloworderdetail(), CwbOrderWithDeliveryState.class);
			order.setDatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderFlow.getCredate()));
			order.setFloworderType(orderFlow.getFlowordertype());
			order.setStateCode(String.valueOf(DeliveryStateEnum.getByValue(Integer.valueOf(cwbOrderWithDeliveryState.getDeliveryState()==null?"0":cwbOrderWithDeliveryState.getDeliveryState().getDeliverystate()+"")).getValue()));
			order.setDetail(this.getDetailExport(orderFlow));
			queryOrders.add(order);
		}
		
		cwbResponseDto.setQueryOrders(queryOrders);
		cwbResponseDto.setReturnCode("00");
		cwbResponseDto.setRemark("成功");
		String jsonString  = JsonUtil.fromObject(cwbResponseDto);
		return jsonString;
		
		
	}
}