package cn.explink.controller;

import java.math.BigDecimal;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.OrderBackCheckDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.RemarkDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.OrderBackCheck;
import cn.explink.domain.Reason;
import cn.explink.domain.Remark;
import cn.explink.domain.User;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.ReasonTypeEnum;
import cn.explink.exception.CwbException;
import cn.explink.service.CwbOrderService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.service.OrderBackCheckService;
import cn.explink.util.ExcelUtils;

/**
 * 退款审核
 * 
 * @author zs
 * 
 */
@Controller
@RequestMapping("/orderBackCheck")
public class OrderBackCheckController {
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	OrderBackCheckService orderBackCheckService;
	@Autowired
	OrderBackCheckDAO orderBackCheckDAO;
	@Autowired
	ExportService exportService;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	JointService jointService;
	@Autowired
	SystemInstallDAO systemInstallDAO;
	@Autowired
	CwbDAO cwbDao;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	CustomWareHouseDAO customWareHouseDAO;
	@Autowired
	ReasonDao reasonDAO;
	@Autowired
	RemarkDAO remarkDAO;
	@Autowired
	ExportmouldDAO exportmouldDAO;
	@Autowired
	CwbOrderService cwbOrderService;
	
	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	/**
	 * 审核为允许退货出站
	 * 
	 * @param model
	 * @param request
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/toTuiHuoCheck")
	public String toTuiHuoCheck(Model model, HttpServletRequest request, @RequestParam(value = "cwb", defaultValue = "", required = false) String cwb,
			@RequestParam(value = "searchType", defaultValue = "0", required = false) String searchType,
			@RequestParam(value = "cwbtypeid", defaultValue = "0", required = false) String cwbtypeid,
			@RequestParam(value = "customerid", defaultValue = "0", required = false) String customerid,
			@RequestParam(value = "branchid", defaultValue = "0", required = false) String branchid,
			@RequestParam(value = "auditstate", defaultValue = "0", required = false) String auditstate,
			@RequestParam(value = "shenheresult", defaultValue = "0", required = false) String shenheresult,
			@RequestParam(value = "begindate", defaultValue = "0", required = false) String begindate,
			@RequestParam(value = "enddate", defaultValue = "0", required = false) String enddate
			) {
		
		String quot = "'", quotAndComma = "',";
		List<String> scancwblist = new ArrayList<String>();
		List<CwbOrder> cwborderlist = new ArrayList<CwbOrder>();
		
		model.addAttribute("exportmouldlist", exportmouldDAO.getAllExportmouldByUser(getSessionUser().getRoleid()));
		List<Branch> branchList = this.branchDAO.getQueryBranchByBranchidAndUserid(this.getSessionUser().getUserid(), BranchEnum.ZhanDian.getValue());
		Map<Long, String> map = new HashMap<Long, String>();
		for(Branch branch:branchList){
			map.put(branch.getBranchid(), branch.getBranchname());
		}
		List<Customer> customerList = this.customerDAO.getAllCustomers();
		model.addAttribute("customerList", customerList);
		model.addAttribute("branchList", branchList);
		StringBuffer cwbs = new StringBuffer("");
		if (cwb.length() > 0) {
			for (String cwbStr : cwb.split("\r\n")) {
				if (cwbStr.trim().length() == 0) {
					continue;
				}
				String lastcwb = orderBackCheckService.translateCwb(cwbStr);
				cwbs = cwbs.append(quot).append(lastcwb).append(quotAndComma);
				CwbOrder co = cwbDao.getCwbByCwb(lastcwb);
				if (co != null) {
					scancwblist.add(cwbStr.trim());
					cwborderlist.add(co);
				}

			}
		}
		
		String branchids = "";
		int isOpenFlag = this.jointService.getStateForJoint(B2cEnum.Amazon.getKey());
		model.addAttribute("amazonIsOpen", isOpenFlag);
		String isUseAuditTuiHuo = this.systemInstallDAO.getSystemInstall("isUseAuditTuiHuo") == null ? "no" : this.systemInstallDAO.getSystemInstall("isUseAuditTuiHuo").getValue();
		model.addAttribute("isUseAuditTuiHuo", isUseAuditTuiHuo);
		String isUseAuditZhongZhuan = this.systemInstallDAO.getSystemInstall("isUseAuditZhongZhuan") == null ? "no" : this.systemInstallDAO.getSystemInstall("isUseAuditTuiHuo").getValue();
		model.addAttribute("isUseAuditZhongZhuan", isUseAuditZhongZhuan);
		// 如果为选择站点则匹配用户权限
		if ((branchList != null) && !branchList.isEmpty()) {
			for (Branch listb : branchList) {
				branchids += listb.getBranchid() + ",";
			}
		}
		if (branchids.contains(",")) {
			branchids = branchids.substring(0, branchids.lastIndexOf(","));
		}
		
		List<OrderFlow> orderflowlist = orderFlowDAO.getOrderByCredate(FlowOrderTypeEnum.YiFanKui.getValue(),begindate,enddate);
		List<CwbOrderView> covlist = new ArrayList<CwbOrderView>();
		List<OrderBackCheck> orderbackList = new ArrayList<OrderBackCheck>();
		CwbOrderView cov = new CwbOrderView();
		// 根据订单号查询
		if (!"".equals(cwb.trim()) && "0".equals(searchType)) {
			List<OrderBackCheck> list = new ArrayList<OrderBackCheck>();
			for (String cwbStr : cwb.split("\r\n")) {
				if ("".equals(cwbStr.trim())) {
					continue;
				}
				OrderBackCheck o = this.orderBackCheckDAO.getOrderBackCheckByCwbAndBranch(cwbStr, branchids);
				if (o != null) {
					list.add(o);
				}
			}
			List<User> userList = this.userDAO.getAllUser();
			//List<CustomWareHouse> customerWareHouseList = customWareHouseDAO.getAllCustomWareHouse();
			//List<Reason> reasonList = reasonDAO.getAllReason();
		  //List<Remark> remarkList = remarkDAO.getRemarkByCwbs(cwbs.substring(0, cwbs.length() - 1));
			//model.addAttribute("cwbList", getCwbOrderView(scancwblist, cwborderlist, customerList, customerWareHouseList, branchList, userList, reasonList, remarkList));
			orderbackList = this.orderBackCheckService.getOrderBackCheckList(list, customerList, userList);
			for(OrderBackCheck obc:orderbackList){
				for(OrderFlow of:orderflowlist){
					if(obc.getCwb().equals(of.getCwb())){
						cov.setScancwb(String.valueOf(obc.getId()));
						cov.setCwb(of.getCwb());
						cov.setCwbordertypename(obc.getCwbordertypename());
						cov.setCustomername(obc.getCustomername());
						cov.setBranchname(map.get(obc.getBranchid()));
						cov.setCreatetime(String.valueOf(of.getCredate()));
						covlist.add(cov);
					}
				}
			}
			
		}else if(cwb==null||"".equals(cwb.trim())){
			orderbackList = orderBackCheckDAO.getCwborderss(Integer.parseInt(cwbtypeid), Long.parseLong(customerid), Long.parseLong(branchid));
			for(OrderBackCheck obc:orderbackList){
				for(OrderFlow of:orderflowlist){
					if(obc.getCwb().equals(of.getCwb())){
						cov.setScancwb(String.valueOf(obc.getId()));
						cov.setCwb(of.getCwb());
						cov.setCwbordertypename(obc.getCwbordertypename());
						cov.setCustomername(obc.getCustomername());
						cov.setBranchname(map.get(obc.getBranchid()));
						cov.setCreatetime(String.valueOf(of.getCredate()));
						covlist.add(cov);
					}
				}
			}
		}
		//model.addAttribute("orderbackList", orderbackList);
		model.addAttribute("covlist",covlist);
		
		/*// 查询全部
		if ("1".equals(searchType)) {
			List<OrderBackCheck> list = this.orderBackCheckDAO.getOrderBackCheckListByBranch(branchids);
			List<Customer> customerList = this.customerDAO.getAllCustomers();
			List<User> userList = this.userDAO.getAllUser();
			List<OrderBackCheck> orderbackList = this.orderBackCheckService.getOrderBackCheckList(list, customerList, userList);
			model.addAttribute("orderbackList", orderbackList);
			model.addAttribute("customerList", customerList);
		}
<<<<<<< .working
		*/
		/*int isOpenFlag = this.jointService.getStateForJoint(B2cEnum.Amazon.getKey());
		model.addAttribute("amazonIsOpen", isOpenFlag);*/

		return "auditorderstate/toTuiHuoCheck";
	}

	/**
	 * 提交审核
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("/save")
	public @ResponseBody
	String save(Model model, HttpServletRequest request, @RequestParam(value = "ids", required = false, defaultValue = "") String ids) {
		try {
			this.orderBackCheckService.save(ids, this.getSessionUser());
			return "{\"errorCode\":0,\"error\":\"审核成功\"}";
		} catch (CwbException e) {
			return "{\"errorCode\":1,\"error\":\"" + e.getMessage() + "\"}";
		}
	}

	/**
	 * 导出Excel
	 * 
	 * @param model
	 * @param response
	 * @param cwb
	 * @param request
	 */
	@RequestMapping("/export")
	public void export(Model model, HttpServletResponse response, @RequestParam(value = "cwb", required = false, defaultValue = "") String cwb,
			@RequestParam(value = "searchType", defaultValue = "0", required = false) String searchType, HttpServletRequest request) {
		List<Branch> branchList = this.branchDAO.getQueryBranchByBranchidAndUserid(this.getSessionUser().getUserid(), BranchEnum.ZhanDian.getValue());
		String branchids = "";
		// 如果为选择站点则匹配用户权限
		if ((branchList != null) && !branchList.isEmpty()) {
			for (Branch listb : branchList) {
				branchids += listb.getBranchid() + ",";
			}
		}
		branchids = branchids.substring(0, branchids.lastIndexOf(","));

		List<Customer> customerList = this.customerDAO.getAllCustomers();
		List<User> userList = this.userDAO.getAllUser();
		String[] cloumnName1 = new String[9]; // 导出的列名
		String[] cloumnName2 = new String[9]; // 导出的英文列名
		this.exportService.SetOrderBackCheckFields(cloumnName1, cloumnName2);
		final String[] cloumnName = cloumnName1;
		final String[] cloumnName3 = cloumnName2;
		final HttpServletRequest request1 = request;
		String sheetName = "退货审核"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Complaint_" + df.format(new Date()) + ".xlsx"; // 文件名

		List<OrderBackCheck> list = new ArrayList<OrderBackCheck>();
		// 根据订单号导出
		if (!"".equals(cwb.trim()) && "0".equals(searchType)) {
			for (String cwbStr : cwb.split("\r\n")) {
				if ("".equals(cwbStr.trim())) {
					continue;
				}
				OrderBackCheck o = this.orderBackCheckDAO.getOrderBackCheckByCwbAndBranch(cwbStr, branchids);
				if (o != null) {
					list.add(o);
				}
			}

		}// 根据权限导出全部
		if ("1".equals(searchType)) {
			list = this.orderBackCheckDAO.getOrderBackCheckListByBranch(branchids);
		}

		final List<OrderBackCheck> list1 = this.orderBackCheckService.getOrderBackCheckList(list, customerList, userList);
		try {
			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(Sheet sheet, CellStyle style) {
					for (int k = 0; k < list1.size(); k++) {
						Row row = sheet.createRow(k + 1);
						row.setHeightInPoints(15);
						for (int i = 0; i < cloumnName.length; i++) {
							Cell cell = row.createCell((short) i);
							cell.setCellStyle(style);
							Object a = null;
							// 给导出excel赋值
							a = OrderBackCheckController.this.exportService.setOrderBackCheck(cloumnName3, request1, list1, a, i, k);
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
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// 给CwbOrderView赋值
		public List<CwbOrderView> getCwbOrderView(List<String> scancwblist, List<CwbOrder> clist, List<Customer> customerList, List<CustomWareHouse> customerWareHouseList, List<Branch> branchList,
				List<User> userList, List<Reason> reasonList, List<Remark> remarkList) {
			List<CwbOrderView> cwbOrderViewList = new ArrayList<CwbOrderView>();
			if (clist.size() > 0) {
				for (CwbOrder c : clist) {
					int index = clist.indexOf(c);
					CwbOrderView cwbOrderView = new CwbOrderView();

					cwbOrderView.setCarwarehouse(c.getCarwarehouse());
					cwbOrderView.setScancwb(scancwblist.get(index));
					cwbOrderView.setCwb(c.getCwb());
					cwbOrderView.setTranscwb(c.getTranscwb());
					cwbOrderView.setEmaildate(c.getEmaildate());
					cwbOrderView.setCarrealweight(c.getCarrealweight());
					cwbOrderView.setCarsize(c.getCarsize());
					cwbOrderView.setSendcarnum(c.getSendcarnum());
					cwbOrderView.setCwbprovince(c.getCwbprovince());
					cwbOrderView.setCwbcity(c.getCwbcity());
					cwbOrderView.setCwbcounty(c.getCwbcounty());
					cwbOrderView.setConsigneeaddress(c.getConsigneeaddress());
					cwbOrderView.setConsigneename(c.getConsigneename());
					cwbOrderView.setConsigneemobile(c.getConsigneemobile());
					cwbOrderView.setConsigneephone(c.getConsigneephone());
					cwbOrderView.setConsigneepostcode(c.getConsigneepostcode());

					cwbOrderView.setCustomername(this.getQueryCustomerName(customerList, c.getCustomerid()));// 供货商的名称
					String customwarehouse = this.getQueryCustomWareHouse(customerWareHouseList, Long.parseLong(c.getCustomerwarehouseid()));
					cwbOrderView.setCustomerwarehousename(customwarehouse);
					cwbOrderView.setInhouse(this.getQueryBranchName(branchList, Integer.parseInt(c.getCarwarehouse() == "" ? "0" : c.getCarwarehouse())));// 入库仓库
					cwbOrderView.setCurrentbranchname(this.getQueryBranchName(branchList, c.getCurrentbranchid()));// 当前所在机构名称
					cwbOrderView.setStartbranchname(this.getQueryBranchName(branchList, c.getStartbranchid()));// 上一站机构名称
					cwbOrderView.setNextbranchname(this.getQueryBranchName(branchList, c.getNextbranchid()));// 下一站机构名称
					cwbOrderView.setDeliverybranch(this.getQueryBranchName(branchList, c.getDeliverybranchid()));// 配送站点
					cwbOrderView.setDelivername(this.getQueryUserName(userList, c.getDeliverid()));
					cwbOrderView.setRealweight(c.getCarrealweight());
					cwbOrderView.setCwbremark(c.getCwbremark());
					cwbOrderView.setReceivablefee(c.getReceivablefee());
					cwbOrderView.setCaramount(c.getCaramount());
					cwbOrderView.setPaybackfee(c.getPaybackfee());

					DeliveryState deliverystate = this.getDeliveryByCwb(c.getCwb());
					cwbOrderView.setPaytype(this.getPayWayType(c.getCwb(), deliverystate));// 支付方式
					cwbOrderView.setRemark1(c.getRemark1());
					cwbOrderView.setRemark2(c.getRemark2());
					cwbOrderView.setRemark3(c.getRemark3());
					cwbOrderView.setRemark4(c.getRemark4());
					cwbOrderView.setRemark5(c.getRemark5());
					cwbOrderView.setFlowordertype(c.getFlowordertype());
					cwbOrderView.setReturngoodsremark(this.getOrderFlowByCwbAndType(c.getCwb(), FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue(), "", "").getComment());
					String currentBranch = this.getQueryBranchName(branchList, c.getCurrentbranchid());
					cwbOrderView.setCurrentbranchname(currentBranch);
					cwbOrderView.setCwbstate(c.getCwbstate());
					if (c.getCurrentbranchid() == 0) {
						cwbOrderView.setCurrentbranchid(c.getStartbranchid());
					} else {
						cwbOrderView.setCurrentbranchid(c.getCurrentbranchid());
					}
					Date ruku = this.getOrderFlowByCwbAndType(c.getCwb(), FlowOrderTypeEnum.RuKu.getValue(), "", "").getCredate();
					Date chukusaomiao = this.getOrderFlowByCwbAndType(c.getCwb(), FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), "", "").getCredate();
					Date daohuosaomiao = this.getOrderFlowByCwbAndType(c.getCwb(), FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), "", "").getCredate();
					daohuosaomiao = daohuosaomiao == null ? this.getOrderFlowByCwbAndType(c.getCwb(), FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), "", "").getCredate() : daohuosaomiao;
					Date fenzhanlinghuo = this.getOrderFlowByCwbAndType(c.getCwb(), FlowOrderTypeEnum.FenZhanLingHuo.getValue(), "", "").getCredate();
					Date yifankui = this.getOrderFlowByCwbAndType(c.getCwb(), FlowOrderTypeEnum.YiFanKui.getValue(), "", "").getCredate();
					Date tuigonghuoshangchuku = this.getOrderFlowByCwbAndType(c.getCwb(), FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue(), "", "").getCredate();
					Date zuixinxiugai = this.getOrderFlowByCwb(c.getCwb()).getCredate();
					Date yishenhe = this.getOrderFlowByCwbAndType(c.getCwb(), FlowOrderTypeEnum.YiShenHe.getValue(), "", "").getCredate();
					cwbOrderView.setAuditstate(yishenhe == null ? 0 : 1);// 审核状态
					cwbOrderView.setInstoreroomtime(ruku != null ? sdf.format(ruku) : "");// 入库时间
					cwbOrderView.setOutstoreroomtime(chukusaomiao != null ? sdf.format(chukusaomiao) : "");// 出库时间
					cwbOrderView.setInSitetime(daohuosaomiao != null ? sdf.format(daohuosaomiao) : "");// 到站时间
					cwbOrderView.setPickGoodstime(fenzhanlinghuo != null ? sdf.format(fenzhanlinghuo) : "");// 小件员领货时间
					cwbOrderView.setGobacktime(yifankui != null ? sdf.format(yifankui) : "");// 反馈时间
					cwbOrderView.setGoclasstime(yishenhe == null ? "" : sdf.format(yishenhe));// 归班时间
					cwbOrderView.setNowtime(zuixinxiugai != null ? sdf.format(zuixinxiugai) : "");// 最新修改时间
					cwbOrderView.setTuigonghuoshangchukutime(tuigonghuoshangchuku != null ? sdf.format(tuigonghuoshangchuku) : "");// 退供货商拒收返库时间
					cwbOrderView.setBackreason(c.getBackreason());
					cwbOrderView.setLeavedreasonStr(this.getQueryReason(reasonList, c.getLeavedreasonid()));// 滞留原因
					// cwbOrderView.setExpt_code(); //异常编码
					cwbOrderView.setOrderResultType(c.getDeliverid());
					cwbOrderView.setPodremarkStr(this.getQueryReason(reasonList, this.getDeliveryStateByCwb(c.getCwb()).getPodremarkid()));// 配送结果备注
					cwbOrderView.setCartype(c.getCartype());
					cwbOrderView.setCwbdelivertypeid(c.getCwbdelivertypeid());
					cwbOrderView.setInwarhouseremark(exportService.getInwarhouseRemarks(remarkList).get(c.getCwb()) == null ? "" : exportService.getInwarhouseRemarks(remarkList).get(c.getCwb())
							.get(ReasonTypeEnum.RuKuBeiZhu.getText()));
					cwbOrderView.setCwbordertypeid(c.getCwbordertypeid() + "");// 订单类型
					cwbOrderView.setHandleperson(c.getHandleperson());
					cwbOrderView.setHandlereason(c.getHandlereason());
					cwbOrderView.setHandleresult(c.getHandleresult());

					if (deliverystate != null) {
						cwbOrderView.setSigninman(deliverystate.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue() ? c.getConsigneename() : "");
						cwbOrderView.setSignintime(deliverystate.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue() ? (yifankui != null ? sdf.format(yifankui) : "") : "");
						cwbOrderView.setPosremark(deliverystate.getPosremark());
						cwbOrderView.setCheckremark(deliverystate.getCheckremark());
						cwbOrderView.setDeliverstateremark(deliverystate.getDeliverstateremark());
						cwbOrderView.setCustomerbrackhouseremark(this.getOrderFlowByCwbAndType(c.getCwb(), FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue(), "", "").getComment());
						cwbOrderView.setDeliverystate(deliverystate.getDeliverystate());
						if (deliverystate.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue() && yifankui != null) {
							cwbOrderView.setSendSuccesstime(sdf.format(yifankui));// 配送成功时间
						} else if ((deliverystate.getDeliverystate() == DeliveryStateEnum.BuFenTuiHuo.getValue() || deliverystate.getDeliverystate() == DeliveryStateEnum.JuShou.getValue())
								&& yifankui != null) {
							cwbOrderView.setJushoutime(sdf.format(yifankui));// 拒收时间
						}
					}
					cwbOrderViewList.add(cwbOrderView);

				}
			}
			return cwbOrderViewList;
		}
		public String getQueryUserName(List<User> userList, long userid) {
			String username = "";
			for (User u : userList) {
				if (u.getUserid() == userid) {
					username = u.getRealname();
					break;
				}
			}
			return username;
		}

		public DeliveryState getDeliveryByCwb(String cwb) {
			List<DeliveryState> delvieryList = deliveryStateDAO.getDeliveryStateByCwb(cwb);
			return delvieryList.size() > 0 ? delvieryList.get(delvieryList.size() - 1) : new DeliveryState();
		}
		public String getQueryCustomWareHouse(List<CustomWareHouse> customerWareHouseList, long customerwarehouseid) {
			String customerwarehouse = "";
			for (CustomWareHouse ch : customerWareHouseList) {
				if (ch.getWarehouseid() == customerwarehouseid) {
					customerwarehouse = ch.getCustomerwarehouse();
					break;
				}
			}
			return customerwarehouse;
		}

		public String getPayWayType(String cwb, DeliveryState ds) {
			StringBuffer str = new StringBuffer();
			String paywaytype = "";
			if (ds.getCash().compareTo(BigDecimal.ZERO) == 1) {
				str.append("现金,");
			}
			if (ds.getPos().compareTo(BigDecimal.ZERO) == 1) {
				str.append("POS,");
			}
			if (ds.getCheckfee().compareTo(BigDecimal.ZERO) == 1) {
				str.append("支票,");
			}
			if (ds.getOtherfee().compareTo(BigDecimal.ZERO) == 1) {
				str.append("其它,");
			}
			if (str.length() > 0) {
				paywaytype = str.substring(0, str.length() - 1);
			} else {
				paywaytype = "现金";
			}
			return paywaytype;
		}

		public String getQueryBranchName(List<Branch> branchList, long branchid) {
			String branchname = "";
			for (Branch b : branchList) {
				if (b.getBranchid() == branchid) {
					branchname = b.getBranchname();
					break;
				}
			}
			return branchname;
		}

		public OrderFlow getOrderFlowByCwb(String cwb) {
			List<OrderFlow> orderflowList = new ArrayList<OrderFlow>();
			orderflowList = orderFlowDAO.getAdvanceOrderFlowByCwb(cwb);
			OrderFlow orderflow = orderflowList.size() > 0 ? orderflowList.get(orderflowList.size() - 1) : new OrderFlow();
			return orderflow;
		}

		public String getQueryReason(List<Reason> reasonList, long reasonid) {
			String reasoncontent = "";
			for (Reason r : reasonList) {
				if (r.getReasonid() == reasonid) {
					reasoncontent = r.getReasoncontent();
					break;
				}
			}
			return reasoncontent;
		}

		public DeliveryState getDeliveryStateByCwb(String cwb) {
			List<DeliveryState> deliveryStateList = new ArrayList<DeliveryState>();
			deliveryStateList = deliveryStateDAO.getDeliveryStateByCwb(cwb);
			DeliveryState deliverState = deliveryStateList.size() > 0 ? deliveryStateList.get(deliveryStateList.size() - 1) : new DeliveryState();
			return deliverState;
		}

		public String getQueryCustomerName(List<Customer> customerList, long customerid) {
			String customername = "";
			for (Customer c : customerList) {
				if (c.getCustomerid() == customerid) {
					customername = c.getCustomername();
					break;
				}
			}
			return customername;
		}

		public OrderFlow getOrderFlowByCwbAndType(String cwb, long flowordertype, String begindate, String enddate) {
			List<OrderFlow> orderflowList = new ArrayList<OrderFlow>();
			orderflowList = orderFlowDAO.getOrderFlowByCwbAndFlowordertype(cwb, flowordertype, begindate, enddate);
			OrderFlow orderflow = orderflowList.size() > 0 ? orderflowList.get(orderflowList.size() - 1) : new OrderFlow();
			return orderflow;
		}
}
