package cn.explink.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JointService;
import cn.explink.b2c.tps.TpsCwbFlowService;
import cn.explink.dao.BaleCwbDao;
import cn.explink.dao.BaleDao;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.ExportmouldDAO;
import cn.explink.domain.Bale;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.TransCwbDetail;
import cn.explink.domain.User;
import cn.explink.enumutil.BaleStateEnum;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.CwbOrderPDAEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.exception.CwbException;
import cn.explink.service.BaleService;
import cn.explink.service.CwbOrderService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.KfzdOrderService;
import cn.explink.service.mps.MPSOptStateService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.Page;
import cn.explink.util.ServiceUtil;

@Controller
@RequestMapping("/bale")
public class BaleController {
	private Logger logger = LoggerFactory.getLogger(BaleController.class);
	
	@Autowired
	BaleDao baleDAO;
	@Autowired
	BaleCwbDao baleCwbDao;
	@Autowired
	JointService jointService;
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	ExportmouldDAO exportmouldDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	BaleService baleService;
	@Autowired
	KfzdOrderService kfzdOrderService;
	@Autowired
	TpsCwbFlowService tpsCwbFlowService;
	@Autowired
	private MPSOptStateService mpsOptStateService ;

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/add")
	public String add(Model model) {
		return "bale/add";
	}

	@RequestMapping("/create")
	public @ResponseBody String create(Model model, @RequestParam(value = "baleno", required = true) String baleno) {
		String states = BaleStateEnum.SaoMiaoZhong.getValue() + "," + BaleStateEnum.WeiDaoZhan.getValue();
		Bale bale = this.baleDAO.getBaleOnway(baleno);
		if (bale!=null) {
			return "{\"errorCode\":1,\"error\":\"包已存在\"}";
		} else {
			this.baleDAO.create(baleno);
			return "{\"errorCode\":0,\"error\":\"创建成功\"}";
		}
	}

	@RequestMapping("/left/{page}")
	public String left(Model model, @PathVariable(value = "page") long page, @RequestParam(value = "chukubegindate", required = false, defaultValue = "") String chukubegindate,
			@RequestParam(value = "chukuenddate", required = false, defaultValue = "") String chukuenddate, @RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow,
			@RequestParam(value = "showLetfOrRight", required = false, defaultValue = "1") long showLetfOrRight) {
		List<CwbOrder> clist = new ArrayList<CwbOrder>();
		List<Bale> bList = new ArrayList<Bale>();
		Page pageparm = new Page();
		int isOpenFlag = this.jointService.getStateForJoint(B2cEnum.Amazon.getKey());
		model.addAttribute("isAmazonOpen", isOpenFlag);
		if (isshow != 0) {
			bList = this.baleDAO.getBaleByChukuDate(chukubegindate, chukuenddate, page);
			pageparm = new Page(this.baleDAO.getBaleByChukuDateCount(chukubegindate, chukuenddate), page, Page.ONE_PAGE_NUMBER);
			Map<Long, Branch> branchMap = new HashMap<Long, Branch>();
			for (Branch branch : this.branchDAO.getAllBranches()) {
				branchMap.put(branch.getBranchid(), branch);
			}
			model.addAttribute("branchMap", branchMap);
		}
		model.addAttribute("orderlist", clist);
		model.addAttribute("baleList", bList);
		model.addAttribute("customerlist", this.customerDAO.getAllCustomers());
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("currentime", DateTimeUtil.getCurrentDate());
		model.addAttribute("page", page);
		model.addAttribute("showLetfOrRight", showLetfOrRight);
		return "/neworderquery/left";

	}

	@RequestMapping("/show/{baleid}/{page}")
	public String show(Model model, @PathVariable(value = "baleid") long baleid, @PathVariable(value = "page") long page) {
		List<String> cwbsList = this.baleCwbDao.getCwbsByBale(String.valueOf(baleid));
		List<CwbOrder> cwborderList = new ArrayList<CwbOrder>();
		long cwborderCount = 0;
		if ((cwbsList != null) && (cwbsList.size() > 0)) {
			String cwbs = "";
			for (String cwb : cwbsList) {
				cwb = this.cwbOrderService.translateCwb(cwb);
				cwbs += "'" + cwb + "',";
			}
			if (cwbs.contains(",")) {
				cwbs = cwbs.substring(0, cwbs.lastIndexOf(","));
			}
			// cwborderList = this.cwbDAO.getCwbOrderByBaleid(baleid, page);
			// cwborderCount = this.cwbDAO.getCwbOrderByBaleidCount(baleid);
			cwborderList = this.cwbDAO.getCwbOrderByCwbs(page, cwbs);
			cwborderCount = this.cwbDAO.getCwbOrderByCwbsCount(cwbs);
		}
		model.addAttribute("cwborderList", cwborderList);
		model.addAttribute("page_obj", new Page(cwborderCount, page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);

		Map<Long, Branch> branchMap = new HashMap<Long, Branch>();
		for (Branch branch : this.branchDAO.getAllBranches()) {
			branchMap.put(branch.getBranchid(), branch);
		}
		model.addAttribute("branchMap", branchMap);
		model.addAttribute("customerMap", this.customerDAO.getAllCustomersToMap());
		model.addAttribute("exportmouldlist", this.exportmouldDAO.getAllExportmouldByUser(this.getSessionUser().getRoleid()));

		model.addAttribute("baleid", baleid);

		return "/neworderquery/show";

	}

	@RequestMapping("/exportExcel")
	public void exportExcel(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "baleid", required = false, defaultValue = "0") long baleid,
			@RequestParam(value = "page", required = false, defaultValue = "0") long page) {
		this.baleService.exportExcelMethod(response, request, baleid, page);

	}

	@RequestMapping("/exportExcelBale")
	public void exportExcelBale(Model model, HttpServletResponse response, HttpServletRequest request,
			@RequestParam(value = "chukubegindate", required = false, defaultValue = "") String chukubegindate,
			@RequestParam(value = "chukuenddate", required = false, defaultValue = "") String chukuenddate) {
		this.baleService.baleExcel(request, response, chukubegindate, chukuenddate);

	}

	/**
	 * 库房出库、退货出站根据包号扫描订单检查
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/baleaddcwbCheck/{cwb}/{baleno}")
	public @ResponseBody ExplinkResponse baleaddcwbCheck(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable(value = "cwb") String cwb,
			@PathVariable(value = "baleno") String baleno, @RequestParam(value = "branchid", required = true, defaultValue = "0") long branchid,
			@RequestParam(value = "flag", required = true, defaultValue = "0") long flag, @RequestParam(value = "confirmflag", required = false, defaultValue = "0") long confirmflag,
			@RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid) {
		JSONObject obj = new JSONObject();
		ExplinkResponse explinkResponse = new ExplinkResponse("000000", "", obj);
		String scancwb = cwb;
		obj.put("scancwb", scancwb);
		obj.put("baleno", baleno);
		cwb = this.cwbOrderService.translateCwb(cwb);
		CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);

		try {
			if (co != null) {
				/***************mod by yurong.liang 2016-07-08 ********/
				// 合包出库扫描快递单时提示：不允许在这里操作快递单！
				/*if (this.isExpressOrder(co)) {
					throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), "不允许在这里操作快递单！");
				}*/
				/****************mod end******************************/
				long nextbranchid = co.getNextbranchid();
				String nextbranchname = "";
				long deliverybranchid = co.getDeliverybranchid();
				String deliverybranchname = "";
				if (nextbranchid == 0) {
					nextbranchname = "未知站点";
				} else {
					nextbranchname = this.branchDAO.getBranchByBranchid(nextbranchid).getBranchname();
				}
				if (deliverybranchid == 0) {
					deliverybranchname = "未知站点";
				} else {
					deliverybranchname = this.branchDAO.getBranchByBranchid(deliverybranchid).getBranchname();
				}
				if (confirmflag == 0 && flag != 4) {//退供货商出库不校验下一站
					if (co != null) {
						if (co.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) {
							if ((co != null) && (co.getNextbranchid() != branchid) && (co.getFlowordertype() != FlowOrderTypeEnum.DaoRuShuJu.getValue())) {
								throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.BU_SHI_ZHE_GE_MU_DI_DI, nextbranchname);
							}
						} else {
							if ((co.getFlowordertype() != FlowOrderTypeEnum.DaoRuShuJu.getValue()) && (branchid != co.getDeliverybranchid()) && (co.getNextbranchid() != branchid)) {
								throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.BU_SHI_ZHE_GE_MU_DI_DI, deliverybranchname);
							}
						}
					}
				}
			}

			// 封包检查
			if (flag == 1) {// 库房出库
				this.baleService.baleaddcwbChukuCheck(this.getSessionUser(), baleno.trim(), scancwb.trim(), confirmflag == 1, this.getSessionUser().getBranchid(), branchid);
			} else if (flag == 2) {// 退货出站
				this.baleService.baleaddcwbTuiHuoCheck(this.getSessionUser(), baleno, scancwb, confirmflag == 1, this.getSessionUser().getBranchid(), branchid);
			} else if (flag == 3) {// 中转出站
				this.baleService.baleaddcwbzhongzhuanchuzhanCheck(this.getSessionUser(), baleno.trim(), scancwb.trim(), confirmflag == 1, this.getSessionUser().getBranchid(), branchid);
			} else if (flag == 4) {// 退供货商出库
				this.baleService.baleaddcwbToCustomerCheck(this.getSessionUser(), baleno, scancwb, this.getSessionUser().getBranchid(), branchid);
			} else if(flag == 5){// 中转库出库
				this.baleService.baleaddcwbZhongzhuanChuKuCheck(this.getSessionUser(), baleno.trim(), scancwb.trim(), confirmflag == 1, this.getSessionUser().getBranchid(), branchid);
			}
			obj.put("errorcode", "000000");
		} catch (CwbException e) {
			this.logger.error("cwb="+cwb,e);
			obj.put("errorcode", "111111");
			obj.put("errorenum", e.getError());
			obj.put("errorinfo", e.getMessage());
			explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.Feng_Bao.getVediourl());
		}
		return explinkResponse;
	}

	/**
	 * 判断是否是快递单
	 *
	 * @param cwbOrder
	 * @return
	 */
	private boolean isExpressOrder(CwbOrder cwbOrder) {
		if (cwbOrder == null) {
			return false;
		}
		if (cwbOrder.getCwbordertypeid() == CwbOrderTypeIdEnum.Express.getValue()) {
			return true;
		}
		return false;
	}

	/**
	 * 根据包号扫描订单
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/baleaddcwb/{cwb}/{baleno}")
	@Transactional
	public @ResponseBody ExplinkResponse baleaddcwb(Model model, HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "cwb") String cwb,@PathVariable(value = "baleno") String baleno, 
			@RequestParam(value = "branchid", required = true, defaultValue = "0") long branchid ,
			@RequestParam(value = "carrealweight", required = true, defaultValue = "0") BigDecimal carrealweight) {
		JSONObject obj = new JSONObject();
		ExplinkResponse explinkResponse = new ExplinkResponse("000000", "", obj);
		try {
			Bale baleOld = this.baleDAO.getBaleWeifengbaoByLock(baleno.trim()); 	//加悲观锁解决：
																				//#1502 出库扫描 出库后订单数增加，但是件数不增加（扫描数与发货数不一致）
																				//以下都是轻操作应该无性能问题
			CwbOrder cwbOrder=this.baleService.baleaddcwb(this.getSessionUser(), baleno.trim(), cwb.trim(), branchid);
//			cwb=this.cwbOrderService.translateCwb(cwb);
			
//			this.cwbDAO.updateScannumAuto(cwb);
			Customer customer = this.customerDAO.getCustomerById(cwbOrder.getCustomerid());
			Bale bale = this.baleDAO.getBaleWeifengbaoByLock(baleno.trim());
			this.baleDAO.updateAddBaleScannum(bale.getId()); //要点：做完所有的写操作最后再读出！
			bale = this.baleDAO.getBaleById(bale.getId());
			this.tpsCwbFlowService.save(cwbOrder,cwb.trim(), FlowOrderTypeEnum.ChuKuSaoMiao,this.getSessionUser().getBranchid(),null,true,null,null);
			long successCount = bale.getCwbcount();
			long scannum = bale.getScannum();
//			if(customer.getIsUsetranscwb() == 2){
//				this.cwbDAO.saveCwbWeight(carrealweight, cwb);
//			}else{
//				//扫描运单号，首先更新当前运单的货物重量，如果当前已扫描件数和订单总件数一致时，才更新订单表的货物重量
//				this.mpsOptStateService.updateTransCwbDetailWeight(cwbOrder.getCwb(), cwb, carrealweight);
//				if(cwbOrder.getScannum() == cwbOrder.getSendcarnum()){
//					BigDecimal totalWeight = getWeightForOrder(cwb) ;
//					this.cwbDAO.saveCwbWeight(totalWeight, cwb);
//				}
//			}
			// add by bruce shangguan 20160530  如果是首次扫描订单号/运单号,就直接更新订单重量；否则就在订单原重量的基础上累加，再更新订单重量
			if(cwbOrder.getScannum() == 1){
				this.cwbDAO.saveCwbWeight(carrealweight, cwbOrder.getCwb());
			}else{
				BigDecimal totalWeight = cwbOrder.getCarrealweight() == null ? BigDecimal.ZERO : cwbOrder.getCarrealweight() ;
				totalWeight = totalWeight.add(carrealweight);
				this.cwbDAO.saveCwbWeight(totalWeight, cwbOrder.getCwb());
			}
			// 如果扫描运单号，就更新当前运单的货物重量
			if(customer != null && customer.getIsUsetranscwb() != 2){
				this.mpsOptStateService.updateTransCwbDetailWeight(cwbOrder.getCwb(), cwb, carrealweight);
			}
			obj.put("newCarrealWeight", carrealweight) ;
			// end 20160530
			obj.put("successCount", successCount);
			obj.put("scannum", scannum);
			obj.put("errorcode", "000000");
		} catch (CwbException e) {
			this.logger.error("cwb="+cwb,e);
			obj.put("errorcode", "111111");
			obj.put("errorinfo", e.getMessage());
			explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.Feng_Bao.getVediourl());
		}
		return explinkResponse;
	}
	
	/**
	 * 根据订单号，获取其所有的运单，并统计所有运单重量
	 * @param orderNumber
	 * @return
	 */
	private BigDecimal getWeightForOrder(String orderNumber){
		BigDecimal totalWeight = BigDecimal.ZERO ;
		List<TransCwbDetail> translist =  this.mpsOptStateService.queryTransCwbDetail(orderNumber) ;
		if(translist == null || translist.size() == 0){
			return totalWeight ;
		}
		for (TransCwbDetail transcwbDetail : translist) {
			if(transcwbDetail.getWeight() != null){
				totalWeight = totalWeight.add(transcwbDetail.getWeight()) ;
			}
		}
		return totalWeight ;
	}
	
	/**
	 * 退货出站根据包号扫描订单
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/baletuihuochuzhanaddcwb/{cwb}/{baleno}")
	@Transactional
	public @ResponseBody ExplinkResponse baletuihuochuzhanaddcwb(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable(value = "cwb") String cwb,
			@PathVariable(value = "baleno") String baleno, @RequestParam(value = "branchid", required = true, defaultValue = "0") long branchid) {
		JSONObject obj = new JSONObject();
		ExplinkResponse explinkResponse = new ExplinkResponse("000000", "", obj);
		try {
			
			Bale baleOld = this.baleDAO.getBaleWeifengbaoByLock(baleno.trim()); //加悲观锁解决：
				
			this.baleService.baletuihuochuzhanaddcwb(this.getSessionUser(), baleno.trim(), cwb.trim(), branchid);
			Bale bale = this.baleDAO.getBaleWeifengbao(baleno.trim());
			this.baleDAO.updateAddBaleScannum(bale.getId());
			bale = this.baleDAO.getBaleById(bale.getId());
			long successCount = bale.getCwbcount();
			long scannum = bale.getScannum();
			obj.put("successCount", successCount);
			obj.put("scannum", scannum);
			obj.put("errorcode", "000000");
		} catch (CwbException e) {
			this.logger.error("cwb="+cwb,e);
			obj.put("errorcode", "111111");
			obj.put("errorinfo", e.getMessage());
			explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.Feng_Bao.getVediourl());
		}
		return explinkResponse;
	}
	
	
	/**
	 * 退供应商出库根据包号扫描订单
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/baletuigonghuoshangchukuaddcwb/{cwb}/{baleno}")
	@Transactional
	public @ResponseBody ExplinkResponse baletuigonghuoshangchukuaddcwb(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable(value = "cwb") String cwb,
			@PathVariable(value = "baleno") String baleno, @RequestParam(value = "branchid", required = true, defaultValue = "0") long branchid) {
		JSONObject obj = new JSONObject();
		ExplinkResponse explinkResponse = new ExplinkResponse("000000", "", obj);
		try {
			
			Bale baleOld = this.baleDAO.getBaleWeifengbaoByLock(baleno.trim()); //加悲观锁解决：
			this.baleService.baletuigonghuoshangchukuaddcwb(this.getSessionUser(), baleno.trim(), cwb.trim(), branchid);
			Bale bale = this.baleDAO.getBaleWeifengbao(baleno.trim());
			this.baleDAO.updateAddBaleScannum(bale.getId());
			//要点：做完所有的写操作最后再读出！
			bale = this.baleDAO.getBaleById(bale.getId());
			long successCount = bale.getCwbcount();
			long scannum = bale.getScannum();
			obj.put("successCount", successCount);
			obj.put("scannum", scannum);
			obj.put("errorcode", "000000");
		} catch (CwbException e) {
			this.logger.error("cwb="+cwb,e);
			obj.put("errorcode", "111111");
			obj.put("errorinfo", e.getMessage());
			explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.Feng_Bao.getVediourl());
		}
		return explinkResponse;
	}


	/**
	 * 根据包号扫描订单
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/sortAndChangeBaleAddCwb/{cwb}/{baleno}")
	@Transactional
	public @ResponseBody ExplinkResponse sortAndChangeBaleAddCwb(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable(value = "cwb") String cwb,
			@PathVariable(value = "baleno") String baleno, @RequestParam(value = "branchid", required = true, defaultValue = "0") long branchid,
			@RequestParam(value = "carrealweight", required = false, defaultValue = "0") BigDecimal carrealweight) {
		JSONObject obj = new JSONObject();
		ExplinkResponse explinkResponse = new ExplinkResponse("000000", "", obj);
		String scancwb = cwb.trim();
		cwb = this.cwbOrderService.translateCwb(scancwb);
		CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);
		if (co != null) {
			try {
				Bale baleOld = this.baleDAO.getBaleWeifengbaoByLock(baleno.trim()); //加悲观锁解决：
				// 判断订单状态是否为中转
				if (this.isZhongzhuanOrder(co)) {
					// 调用中转出库扫描逻辑
					this.baleService.sortAndChangeBaleAddCwb(this.getSessionUser(), baleno.trim(), scancwb, branchid);
				} else {
					// 调用分拣出库扫描逻辑
					CwbOrder cwbOrder=this.baleService.baleaddcwb(this.getSessionUser(), baleno.trim(), scancwb, branchid);
					this.tpsCwbFlowService.save(cwbOrder,scancwb, FlowOrderTypeEnum.ChuKuSaoMiao,this.getSessionUser().getBranchid(),null,true,null,null);
				}
				
				Bale bale = this.baleDAO.getBaleWeifengbao(baleno.trim());
				this.baleDAO.updateAddBaleScannum(bale.getId());
				bale = this.baleDAO.getBaleById(bale.getId());
				long successCount = bale.getCwbcount();
				long scannum = bale.getScannum();
				// add by bruce shangguan 20160530  如果是首次扫描订单号/运单号,就直接更新订单重量；否则就在订单原重量的基础上累加，再更新订单重量
				Customer customer = this.customerDAO.getCustomerById(co.getCustomerid());
				if(co.getScannum() == 1){
					this.cwbDAO.saveCwbWeight(carrealweight, co.getCwb());
				}else{
					BigDecimal totalWeight = co.getCarrealweight() == null ? BigDecimal.ZERO : co.getCarrealweight() ;
					totalWeight = totalWeight.add(carrealweight);
					this.cwbDAO.saveCwbWeight(totalWeight, co.getCwb());
				}
				// 如果扫描运单号，就更新当前运单的货物重量
				if(customer != null && customer.getIsUsetranscwb() != 2){
					this.mpsOptStateService.updateTransCwbDetailWeight(co.getCwb(), cwb, carrealweight);
				}
				obj.put("newCarrealWeight", carrealweight) ;
				// end 20160530
				obj.put("successCount", successCount);
				obj.put("scannum", scannum);
				obj.put("errorcode", "000000");
			} catch (CwbException e) {
				this.logger.error("cwb="+cwb,e);
				obj.put("errorcode", "111111");
				obj.put("errorinfo", e.getMessage());
				explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.Feng_Bao.getVediourl());
			}

		}

		return explinkResponse;
	}

	/**
	 * 判断订单是否为中转，通过订单操作环节是否为中转入库或中转出库或者订单的下站点是中转
	 */
	private boolean isZhongzhuanOrder(CwbOrder co) {
		long nextbranchid = co.getNextbranchid();
		Branch nextbranch = this.branchDAO.getBranchById(nextbranchid);

		return ((FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue() == co.getFlowordertype()) || (FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue() == co.getFlowordertype())) || (nextbranch == null) ? true
				: nextbranch.getSitetype() == BranchEnum.ZhongZhuan.getValue();

	}

	/**
	 * 库房出库、退货出站根据包号扫描订单检查
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/sortAndChangeBaleAddCwbCheck/{cwb}/{baleno}")
	public @ResponseBody ExplinkResponse sortAndChangeBaleAddCwbCheck(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable(value = "cwb") String cwb,
			@PathVariable(value = "baleno") String baleno, @RequestParam(value = "branchid", required = true, defaultValue = "0") long branchid,
			@RequestParam(value = "flag", required = true, defaultValue = "0") long flag, @RequestParam(value = "confirmflag", required = false, defaultValue = "0") long confirmflag,
			@RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid) {
		JSONObject obj = new JSONObject();
		ExplinkResponse explinkResponse = new ExplinkResponse("000000", "", obj);
		String scancwb = cwb;
		obj.put("scancwb", scancwb);
		obj.put("baleno", baleno);
		cwb = this.cwbOrderService.translateCwb(scancwb);
		CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);

		try {
			if (co == null) {
				//订单不存在
				throw new CwbException(cwb, FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue(), ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
			} else {
				// 合包出库扫描快递单时提示：不允许在这里操作快递单！
				/*if (this.isExpressOrder(co)) {
					throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), "不允许在这里操作快递单！");
				}*/
			
				long nextbranchid = co.getNextbranchid();
				String nextbranchname = "";
				long deliverybranchid = co.getDeliverybranchid();
				String deliverybranchname = "";
				if (nextbranchid == 0) {
					nextbranchname = "未知站点";
				} else {
					nextbranchname = this.branchDAO.getBranchByBranchid(nextbranchid).getBranchname();
				}
				if (deliverybranchid == 0) {
					deliverybranchname = "未知站点";
				} else {
					deliverybranchname = this.branchDAO.getBranchByBranchid(deliverybranchid).getBranchname();
				}
				if (confirmflag == 0) {
					if (co != null) {
						if (co.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) {
							if ((co != null) && (co.getNextbranchid() != branchid) && (co.getFlowordertype() != FlowOrderTypeEnum.DaoRuShuJu.getValue())) {
								throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.BU_SHI_ZHE_GE_MU_DI_DI, nextbranchname);
							}
						} else {
							if ((co.getFlowordertype() != FlowOrderTypeEnum.DaoRuShuJu.getValue()) && (branchid != co.getDeliverybranchid()) && (co.getNextbranchid() != branchid)) {
								throw new CwbException(cwb, FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.BU_SHI_ZHE_GE_MU_DI_DI, deliverybranchname);
							}
						}
					}
				}
			}
			long currentBranchId = this.getSessionUser().getBranchid();

			// 封包检查
			if (flag == 1) {// 库房出库
				//如果订单是中转出库，那么查询区域权限设置的【中转库】
				if(isZhongzhuanOrder(co)){
					currentBranchId = kfzdOrderService.getBranchIdFromUserBranchMapping(BranchEnum.ZhongZhuan);
					
					this.baleService.baleaddcwbZhongzhuanChuKuCheck(this.getSessionUser(), baleno.trim(), scancwb.trim(), confirmflag == 1, currentBranchId, branchid);
				}else{
					this.baleService.baleaddcwbChukuCheck(this.getSessionUser(), baleno.trim(), scancwb.trim(), confirmflag == 1, currentBranchId, branchid);
				}
				
			} 
			obj.put("errorcode", "000000");
		} catch (CwbException e) {
			this.logger.error("cwb="+cwb,e);
			obj.put("errorcode", "111111");
			obj.put("errorenum", e.getError());
			obj.put("errorinfo", e.getMessage());
			explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.Feng_Bao.getVediourl());
		}
		return explinkResponse;
	}

	/**
	 * 封包
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/fengbao/{baleno}")
	public @ResponseBody ExplinkResponse fengbao(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable(value = "baleno") String baleno,
			@RequestParam(value = "branchid", required = true, defaultValue = "0") long branchid) {
		JSONObject obj = new JSONObject();
		ExplinkResponse explinkResponse = new ExplinkResponse("000000", "", obj);
		try {
			this.baleService.fengbao(this.getSessionUser(), baleno.trim(), branchid);
			obj.put("errorcode", "000000");
			explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());
		} catch (CwbException e) {
			this.logger.error("baleno="+baleno,e);
			obj.put("errorcode", "111111");
			obj.put("errorinfo", e.getMessage());
			explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.Feng_Bao.getVediourl());
		}
		return explinkResponse;
	}

	/**
	 * 按包 库房出库扫描
	 *
	 * @param model
	 * @param deliverybranchid
	 *            中转出站正确的配送站点
	 * @return
	 */
	@RequestMapping("/balechuku/{baleno}")
	public @ResponseBody ExplinkResponse balechuku(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable("baleno") String baleno,
			@RequestParam(value = "branchid", required = true, defaultValue = "0") long branchid, @RequestParam(value = "driverid", required = true, defaultValue = "0") long driverid,
			@RequestParam(value = "truckid", required = false, defaultValue = "0") long truckid, @RequestParam(value = "reasonid", required = false, defaultValue = "0") long reasonid,
			@RequestParam(value = "deliverybranchid", required = false, defaultValue = "0") long deliverybranchid) {
		JSONObject obj = new JSONObject();
		ExplinkResponse explinkResponse = new ExplinkResponse("000000", "", obj);
		if (!"".equals(baleno.trim())) {
			boolean flag = true;// 封包是否成功
			// ======封包操作========
			Bale bale=null;
			try {
				bale=this.baleService.fengbao(this.getSessionUser(), baleno.trim(), branchid);
				obj.put("errorcode", "000000");
			} catch (CwbException e) {
				this.logger.error("baleno="+baleno,e);
				flag = false;
				obj.put("errorcode", "111111");
				obj.put("errorinfo", "(按包出库异常)" + e.getMessage());
				explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.Feng_Bao.getVediourl());
			}

			// =====封包成功后出库======
//			if (flag) {
//				// 根据包号查找订单信息
//				// List<CwbOrder> cwbOrderList =
//				// this.cwbDAO.getListByPackagecodeExcel(baleno.trim());
//				List<String> cwbs = this.baleCwbDao.getCwbsByBaleNO(baleno.trim());
//				List<CwbOrder> errorList = new ArrayList<CwbOrder>();
//				List<BaleView> errorListView = new ArrayList<BaleView>();
//				// if ((cwbOrderList != null) && !cwbOrderList.isEmpty()) {
//				long successCount = 0;
//				long errorCount = 0;
//				for (String cwb : cwbs) {
//					try {
//						// 订单出库 只有分站中转给中转站的订单 才传入true
//						boolean iszhongzhuanout = this.getIsZhongZhuanOutFlag(branchid);
//
//						CwbOrder cwbOrder = this.cwbOrderService.outWarehous(this.getSessionUser(), cwb, cwb, driverid, truckid, branchid, 0, false, "", baleno, reasonid, iszhongzhuanout, true);
//						successCount++;
//
//						// ====中转出站 正确的配送站点==========
//						if ((deliverybranchid > 0) && (branchid != deliverybranchid)) {
//							Branch deliverybranch = this.branchDAO.getBranchByBranchid(deliverybranchid);
//							CwbOrderAddressCodeEditTypeEnum addressCodeEditType = CwbOrderAddressCodeEditTypeEnum.WeiPiPei;
//							if ((cwbOrder.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.DiZhiKu.getValue())
//									|| (cwbOrder.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.XiuGai.getValue())) {// 如果修改的数据原来是地址库匹配的或者是后来修改的
//																																	// 都将匹配状态变更为修改
//								addressCodeEditType = CwbOrderAddressCodeEditTypeEnum.XiuGai;
//							} else if ((cwbOrder.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.WeiPiPei.getValue())
//									|| (cwbOrder.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.RenGong.getValue())) {// 如果修改的数据原来是为匹配的
//																																	// 或者是人工匹配的
//																																	// 都将匹配状态变更为人工修改
//								addressCodeEditType = CwbOrderAddressCodeEditTypeEnum.RenGong;
//							}
//							this.cwbOrderService.updateDeliveryOutBranch(this.getSessionUser(), cwbOrder, deliverybranch, addressCodeEditType, branchid);
//							obj.put("cwbdeliverybranchname", deliverybranch.getBranchname());
//							obj.put("cwbdeliverybranchnamewav", request.getContextPath() + ServiceUtil.wavPath + (deliverybranch.getBranchwavfile() == null ? "" : deliverybranch.getBranchwavfile()));
//						}
//						// ====中转出站 正确的配送站点End==========
//
//					} catch (CwbException e) {
//						cwb = this.cwbOrderService.translateCwb(cwb);
//						CwbOrder co = this.cwbDAO.getCwbByCwbLock(cwb);
//						//如果单号系统不存在（或者已经失效）
//						if( null ==  co){
//							co = new CwbOrder();
//							co.setCwb(cwb);
//						}
//						errorCount++;
//						co.setRemark1(e.getMessage());// 异常原因
//						errorList.add(co);
//						// exceptionCwbDAO.createExceptionCwb(cwb,
//						// ce.getFlowordertye(), ce.getMessage(),
//						// getSessionUser().getBranchid(),
//						// getSessionUser().getUserid(),
//						// cwbOrder==null?0:cwbOrder.getCustomerid(), 0, 0,
//						// 0, "");
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//				if (errorCount > 0) {
//					List<Customer> customerList = this.customerDAO.getAllCustomers();
//					errorListView = this.baleService.getCwbOrderCustomerView(errorList, customerList);
//					obj.put("errorListView", errorListView);
//					obj.put("errorinfo", "(按包出库)" + baleno + "包号，成功" + successCount + "单，失败" + errorCount + "单");
//					explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.Feng_Bao.getVediourl());
//				} else {
//					Bale bale=this.baleDAO.getBaleByBaleno(baleno,BaleStateEnum.YiFengBao.getValue());
//					obj.put("errorinfo", "(按包出库成功)" + baleno + "包号共" + bale.getScannum() + "件");
//					explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());
//				}
//				if (successCount > 0) {
//					// 更改包的状态
//					this.baleDAO.updateBalesate(baleno, BaleStateEnum.YiFengBaoChuKu.getValue());
//				}
//			}
			
			if( flag ){
				/**
				 * gztl 封包逻辑变更
				 */
				if(bale!=null){
					obj.put("errorinfo", "(按包出库成功)" + baleno + "包号共" + bale.getScannum() + "件");
					explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());
					this.baleDAO.updateBalesate(bale.getId(), BaleStateEnum.YiFengBaoChuKu.getValue());
				}
			}
			// }
		}
		return explinkResponse;
	}

	private boolean getIsZhongZhuanOutFlag(long branchid) {
		boolean iszhongzhuanout = false;
		Branch branch = this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid());
		Branch zzbranch = this.branchDAO.getBranchByBranchid(branchid);
		if ((branch.getSitetype() == BranchEnum.ZhanDian.getValue()) && (zzbranch.getSitetype() == BranchEnum.ZhongZhuan.getValue())) {
			iszhongzhuanout = true;
		}
		return iszhongzhuanout;
	}

	/**
	 * 按包 中转库房出库扫描
	 *
	 * @param model
	 * @param deliverybranchid
	 *            中转出站正确的配送站点
	 * @return
	 */
	@RequestMapping("/balechangechuku/{baleno}")
	public @ResponseBody ExplinkResponse balechangechuku(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable("baleno") String baleno,
			@RequestParam(value = "branchid", required = true, defaultValue = "0") long branchid, @RequestParam(value = "driverid", required = true, defaultValue = "0") long driverid,
			@RequestParam(value = "truckid", required = false, defaultValue = "0") long truckid, @RequestParam(value = "reasonid", required = false, defaultValue = "0") long reasonid,
			@RequestParam(value = "deliverybranchid", required = false, defaultValue = "0") long deliverybranchid) {
		JSONObject obj = new JSONObject();
		ExplinkResponse explinkResponse = new ExplinkResponse("000000", "", obj);
		if (!"".equals(baleno.trim())) {
			boolean flag = true;// 封包是否成功
			// ======封包操作========
			Bale bale=null;
			try {
				bale=this.baleService.fengbao(this.getSessionUser(), baleno.trim(), branchid);
			} catch (CwbException e) {
				this.logger.error("baleno="+baleno,e);
				flag = false;
				obj.put("errorcode", "111111");
				obj.put("errorinfo", "(按包出库异常)" + e.getMessage());
				explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.Feng_Bao.getVediourl());
			}

			// =====封包成功后出库======
			/*if (flag) {
				// 根据包号查找订单信息
				List<CwbOrder> cwbOrderList = this.cwbDAO.getListByPackagecodeExcel(baleno.trim());
				List<CwbOrder> errorList = new ArrayList<CwbOrder>();
				List<BaleView> errorListView = new ArrayList<BaleView>();
				if ((cwbOrderList != null) && !cwbOrderList.isEmpty()) {
					long successCount = 0;
					long errorCount = 0;
					for (CwbOrder co : cwbOrderList) {
						try {
							// 订单出库
							CwbOrder cwbOrder = this.cwbOrderService.changeoutWarehous(this.getSessionUser(), co.getCwb(), co.getCwb(), driverid, truckid, branchid, 0, false, "", baleno, reasonid,
									false, true);
							successCount++;

							// ====中转出站 正确的配送站点==========
							if ((deliverybranchid > 0) && (branchid != deliverybranchid)) {
								Branch deliverybranch = this.branchDAO.getBranchByBranchid(deliverybranchid);
								CwbOrderAddressCodeEditTypeEnum addressCodeEditType = CwbOrderAddressCodeEditTypeEnum.WeiPiPei;
								if ((cwbOrder.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.DiZhiKu.getValue())
										|| (cwbOrder.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.XiuGai.getValue())) {// 如果修改的数据原来是地址库匹配的或者是后来修改的
									// 都将匹配状态变更为修改
									addressCodeEditType = CwbOrderAddressCodeEditTypeEnum.XiuGai;
								} else if ((cwbOrder.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.WeiPiPei.getValue())
										|| (cwbOrder.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.RenGong.getValue())) {// 如果修改的数据原来是为匹配的
									// 或者是人工匹配的
									// 都将匹配状态变更为人工修改
									addressCodeEditType = CwbOrderAddressCodeEditTypeEnum.RenGong;
								}
								this.cwbOrderService.updateDeliveryOutBranch(this.getSessionUser(), cwbOrder, deliverybranch, addressCodeEditType, branchid);
								obj.put("cwbdeliverybranchname", deliverybranch.getBranchname());
								obj.put("cwbdeliverybranchnamewav",
										request.getContextPath() + ServiceUtil.wavPath + (deliverybranch.getBranchwavfile() == null ? "" : deliverybranch.getBranchwavfile()));
							}
							// ====中转出站 正确的配送站点End==========

						} catch (CwbException e) {
							errorCount++;
							co.setRemark1(e.getMessage());// 异常原因
							errorList.add(co);
							// exceptionCwbDAO.createExceptionCwb(cwb,
							// ce.getFlowordertye(), ce.getMessage(),
							// getSessionUser().getBranchid(),
							// getSessionUser().getUserid(),
							// cwbOrder==null?0:cwbOrder.getCustomerid(), 0, 0,
							// 0, "");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if (errorCount > 0) {
						List<Customer> customerList = this.customerDAO.getAllCustomers();
						errorListView = this.baleService.getCwbOrderCustomerView(errorList, customerList);
						obj.put("errorListView", errorListView);
						obj.put("errorinfo", "(按包出库)" + baleno + "包号，成功" + successCount + "件，失败" + errorCount + "件");
						explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.Feng_Bao.getVediourl());
					} else {
						obj.put("errorinfo", "(按包出库成功)" + baleno + "包号共" + successCount + "件");
						explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());
					}
					if (successCount > 0) {
						// 更改包的状态
						this.baleDAO.updateBalesate(baleno, BaleStateEnum.YiFengBaoChuKu.getValue());
					}
				}
			}*/
			
			//参考 baleChuKu的实现，只是更新包的状态
			if( flag ){
				if(bale!=null){
					obj.put("errorinfo", "(按包出库成功)" + baleno + "包号共" + bale.getScannum() + "件");
					explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());
					this.baleDAO.updateBalesate(bale.getId(), BaleStateEnum.YiFengBaoChuKu.getValue());
				}
			}
		}
		return explinkResponse;
	}

	/**
	 * 到货扫描 按包 到货扫描
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/baledaohuo/{baleno}/{cwb}")
	public @ResponseBody ExplinkResponse baledaohuo(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable("baleno") String baleno, @PathVariable("cwb") String cwb,
			@RequestParam(value = "driverid", required = false, defaultValue = "0") long driverid, @RequestParam(value = "requestbatchno", required = true, defaultValue = "0") long requestbatchno,
			@RequestParam(value = "comment", required = true, defaultValue = "") String comment) {
		JSONObject obj = new JSONObject();
		ExplinkResponse explinkResponse = new ExplinkResponse("000000", "", obj);
		if (!"".equals(baleno.trim()) && !"".equals(cwb.trim())) {
			Bale isbale = baleService.getBaleForHebaoDaohuo(request, baleno, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao);

			//CwbOrder iscwb = this.cwbDAO.getCwbByCwb(cwb);
			if ("0".equals(baleno) || (isbale == null)) {
				obj.put("errorinfo", "(合包到货异常)  无此" + baleno + "包号");
				explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.Feng_Bao.getVediourl());
				return explinkResponse;
			} else if (isbale.getBalestate() == BaleStateEnum.YiDaoHuo.getValue()) {
				obj.put("errorinfo", "(合包到货异常)  " + isbale.getBaleno() + "已被拆包");
				explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.Feng_Bao.getVediourl());
				return explinkResponse;
			} 
			
			boolean inbale=this.baleService.inBale(isbale.getId(), cwb);
			if (!inbale) {
				obj.put("errorinfo", "(合包到货异常)" + cwb + "单号不在此包号中");
				explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.Feng_Bao.getVediourl());
				return explinkResponse;
			} else {
				try {
					// 订单到货
					CwbOrder cwbOrder = this.cwbOrderService.substationGoods(this.getSessionUser(), cwb, cwb, driverid, requestbatchno, comment, "", true);
					// 更改包的状态
					this.baleDAO.updateBalesate(isbale.getId(), BaleStateEnum.BuKeYong.getValue());
					obj.put("errorinfo", "(合包到货)" + cwb + "到货成功");
					explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());
				} catch (CwbException e) {
					this.logger.error("cwb="+cwb,e);
					obj.put("errorinfo", "(合包到货异常)" + cwb + e.getMessage());
					explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.Feng_Bao.getVediourl());
				}
			}
		}
		return explinkResponse;
	}

	/**
	 * 按包退货出站
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/baletuihuochuzhan/{baleno}")
	public @ResponseBody ExplinkResponse baletuihuochuzhan(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable("baleno") String baleno,
			@RequestParam(value = "branchid", required = true, defaultValue = "0") long branchid, @RequestParam(value = "driverid", required = true, defaultValue = "0") long driverid,
			@RequestParam(value = "truckid", required = false, defaultValue = "0") long truckid) {
		JSONObject obj = new JSONObject();
		ExplinkResponse explinkResponse = new ExplinkResponse("000000", "", obj);
		if (!"".equals(baleno.trim())) {
			boolean flag = true;// 封包是否成功
			// ======封包操作========
			Bale bale=null;
			try {
				bale=this.baleService.fengbao(this.getSessionUser(), baleno.trim(), branchid);
			} catch (CwbException e) {
				this.logger.error("baleno="+baleno,e);
				flag = false;
				obj.put("errorcode", "111111");
				obj.put("errorinfo", "(按包出站异常)" + e.getMessage());
				explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.Feng_Bao.getVediourl());
			}

			/*// =====封包成功后出库======
			if (flag) {
				// 根据包号查找订单信息
				List<CwbOrder> cwbOrderList = this.cwbDAO.getListByPackagecodeExcel(baleno.trim());
				List<CwbOrder> errorList = new ArrayList<CwbOrder>();
				List<BaleView> errorListView = new ArrayList<BaleView>();
				if ((cwbOrderList != null) && !cwbOrderList.isEmpty()) {
					long successCount = 0;
					long errorCount = 0;
					for (CwbOrder co : cwbOrderList) {
						try {
							// 订单出站
							CwbOrder cwbOrder = this.cwbOrderService.outUntreadWarehous(this.getSessionUser(), co.getCwb(), co.getCwb(), driverid, truckid, branchid, 0, false, "", baleno, true);
							successCount++;
						} catch (CwbException e) {
							errorCount++;
							co.setRemark1(e.getMessage());// 异常原因
							errorList.add(co);
							// exceptionCwbDAO.createExceptionCwb(cwb,
							// ce.getFlowordertye(), ce.getMessage(),
							// getSessionUser().getBranchid(),
							// getSessionUser().getUserid(),
							// cwbOrder==null?0:cwbOrder.getCustomerid(), 0, 0,
							// 0, "");
						}
					}
					if (errorCount > 0) {
						List<Customer> customerList = this.customerDAO.getAllCustomers();
						errorListView = this.baleService.getCwbOrderCustomerView(errorList, customerList);
						obj.put("errorListView", errorListView);
						obj.put("errorinfo", "(按包出站)" + baleno + "包号，成功" + successCount + "件，失败" + errorCount + "件");
						explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.Feng_Bao.getVediourl());
					} else {
						obj.put("errorinfo", "(按包出站成功)" + baleno + "包号共" + successCount + "件");
						explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());
					}
					if (successCount > 0) {
						// 更改包的状态
						this.baleDAO.updateBalesate(baleno, BaleStateEnum.YiFengBaoChuKu.getValue());
					}
				}
			}*/
			
			//参考 baleChuKu的实现，只是更新包的状态
			if( flag ){
				if(bale!=null){
					obj.put("errorinfo", "(按包出库成功)" + baleno + "包号共" + bale.getScannum() + "件");
					explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());
					this.baleDAO.updateBalesate(bale.getId(), BaleStateEnum.YiFengBaoChuKu.getValue());
				}
			}
		}
		return explinkResponse;
	}

	/**
	 * 退货站入库 按包到货
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/baletuihuodaohuo/{baleno}/{cwb}")
	public @ResponseBody ExplinkResponse baletuihuodaohuo(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable("baleno") String baleno,
			@PathVariable("cwb") String cwb, @RequestParam(value = "driverid", required = false, defaultValue = "0") long driverid,
			@RequestParam(value = "comment", required = true, defaultValue = "") String comment) {
		JSONObject obj = new JSONObject();
		ExplinkResponse explinkResponse = new ExplinkResponse("000000", "", obj);
		if (!"".equals(baleno.trim()) && !"".equals(cwb.trim())) {
			Bale isbale = this.baleService.getBaleForHebaoDaohuo(request, baleno, FlowOrderTypeEnum.TuiHuoZhanRuKu);
			//CwbOrder iscwb = this.cwbDAO.getCwbByCwb(cwb);
			if ("0".equals(baleno) || (isbale == null)) {
				obj.put("errorinfo", "(合包到货异常)" + baleno + "包号不存在");
				explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.Feng_Bao.getVediourl());
				return explinkResponse;
			} 
			boolean inbale=this.baleService.inBale(isbale.getId(), cwb);
			if (!inbale) {
				obj.put("errorinfo", "(合包到货异常)" + cwb + "单号不在此包号中");
				explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.Feng_Bao.getVediourl());
				return explinkResponse;
			} else {
				try {
					// 订单到货
					CwbOrder cwbOrder = this.cwbOrderService.backIntoWarehous(this.getSessionUser(), cwb, cwb, driverid, 0, comment, true, 0, 0);
					// 更改包的状态
					this.baleDAO.updateBalesate(isbale.getId(), BaleStateEnum.YiDaoHuo.getValue());
					obj.put("errorinfo", "(合包到货)" + cwb + "到货成功");
					explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());
				} catch (CwbException e) {
					this.logger.error("cwb="+cwb,e);
					obj.put("errorinfo", "(合包到货异常)" + cwb + e.getMessage());
					explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.Feng_Bao.getVediourl());
				}
			}
		}
		return explinkResponse;
	}

	/**
	 * 中转站入库扫描 按包到货
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/balezhongzhuandaohuo/{baleno}/{cwb}")
	public @ResponseBody ExplinkResponse balezhongzhuandaohuo(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable("baleno") String baleno,
			@PathVariable("cwb") String cwb, @RequestParam(value = "driverid", required = false, defaultValue = "0") long driverid,
			@RequestParam(value = "comment", required = true, defaultValue = "") String comment, @RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid) {
		JSONObject obj = new JSONObject();
		ExplinkResponse explinkResponse = new ExplinkResponse("000000", "", obj);
		if (!"".equals(baleno.trim()) && !"".equals(cwb.trim())) {
			Bale isbale = this.baleDAO.getBaleOnway(baleno.trim());
			//CwbOrder iscwb = this.cwbDAO.getCwbByCwb(cwb);
			if ("0".equals(baleno) || (isbale == null)) {
				obj.put("errorinfo", "(合包到货异常)" + baleno + "包号不存在");
				explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.Feng_Bao.getVediourl());
				return explinkResponse;
			} 
			boolean inbale=this.baleService.inBale(isbale.getId(), cwb);
			if (!inbale) {
				obj.put("errorinfo", "(合包到货异常)" + cwb + "单号不在此包号中");
				explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.Feng_Bao.getVediourl());
				return explinkResponse;
			} else {
				try {
					// 订单到货
					String scancwb = cwb;
					cwb = this.cwbOrderService.translateCwb(cwb);
					this.cwbOrderService.changeintoWarehous(this.getSessionUser(), cwb, scancwb, customerid, 0, 0, comment, "", false, 0, 0);

					// 更改包的状态
					this.baleDAO.updateBalesate(isbale.getId(), BaleStateEnum.YiDaoHuo.getValue());
					obj.put("errorinfo", "(合包到货)" + cwb + "到货成功");
					explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());
				} catch (CwbException e) {
					this.logger.error("cwb="+cwb,e);
					obj.put("errorinfo", "(合包到货异常)" + cwb + e.getMessage());
					explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.Feng_Bao.getVediourl());
				}
			}
		}
		return explinkResponse;
	}

	@RequestMapping("/baletocustomerchuku/{baleno}")
	public @ResponseBody ExplinkResponse baletocustomerchuku(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable("baleno") String baleno,
			@RequestParam(value = "branchid", required = true, defaultValue = "0") long branchid) {
		JSONObject obj = new JSONObject();
		ExplinkResponse explinkResponse = new ExplinkResponse("000000", "", obj);
		if (!"".equals(baleno.trim())) {
			boolean flag = true;// 封包是否成功
			// ======封包操作========
			Bale bale=null;
			try {
				bale=this.baleService.fengbao(this.getSessionUser(), baleno.trim(), branchid);
			} catch (CwbException e) {
				this.logger.error("baleno="+baleno,e);
				flag = false;
				obj.put("errorcode", "111111");
				obj.put("errorinfo", "(按包出库异常)" + e.getMessage());
				explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.Feng_Bao.getVediourl());
			}

			/*// =====封包成功后出库======
			if (flag) {
				// 根据包号查找订单信息
				List<CwbOrder> cwbOrderList = this.cwbDAO.getListByPackagecodeExcel(baleno.trim());
				List<CwbOrder> errorList = new ArrayList<CwbOrder>();
				List<BaleView> errorListView = new ArrayList<BaleView>();
				if ((cwbOrderList != null) && !cwbOrderList.isEmpty()) {
					long successCount = 0;
					long errorCount = 0;
					for (CwbOrder co : cwbOrderList) {
						try {
							// 订单出库
							CwbOrder cwbOrder = this.cwbOrderService.backtocustom(this.getSessionUser(), co.getCwb(), co.getCwb(), 0, baleno, true);
							successCount++;
						} catch (CwbException e) {
							errorCount++;
							co.setRemark1(e.getMessage());// 异常原因
							errorList.add(co);
						}
					}
					if (errorCount > 0) {
						List<Customer> customerList = this.customerDAO.getAllCustomers();
						errorListView = this.baleService.getCwbOrderCustomerView(errorList, customerList);
						obj.put("errorListView", errorListView);
						obj.put("errorinfo", "(按包出库)" + baleno + "包号，成功" + successCount + "件，失败" + errorCount + "件");
						explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.Feng_Bao.getVediourl());
					} else {
						obj.put("errorinfo", "(按包出库成功)" + baleno + "包号共" + successCount + "件");
						explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());
					}
					if (successCount > 0) {
						// 更改包的状态
						this.baleDAO.updateBalesate(baleno, BaleStateEnum.YiFengBaoChuKu.getValue());
					}
				}
			}*/
			//参考 baleChuKu的实现，只是更新包的状态
			if( flag ){
				if(bale!=null){
					obj.put("errorinfo", "(按包出库成功)" + baleno + "包号共" + bale.getScannum() + "件");
					explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());
					this.baleDAO.updateBalesate(bale.getId(), BaleStateEnum.YiFengBaoChuKu.getValue());
				}
			}
		}
		return explinkResponse;
	}

	/**
	 * 中转库出库：包添加订单
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/baleZhongZhuanChuKuAddCwb/{cwb}/{baleno}")
	@Transactional
	public @ResponseBody ExplinkResponse baleZhongZhuanChuKuAddCwb(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable(value = "cwb") String cwb,
			@PathVariable(value = "baleno") String baleno, @RequestParam(value = "branchid", required = true, defaultValue = "0") long branchid) {
		JSONObject obj = new JSONObject();
		ExplinkResponse explinkResponse = new ExplinkResponse("000000", "", obj);
		try {
			Bale baleOld = this.baleDAO.getBaleWeifengbaoByLock(baleno.trim());//加悲观锁解决：
																			  //#1502 出库扫描 出库后订单数增加，但是件数不增加（扫描数与发货数不一致）
																			  //以下都是轻操作应该无性能问题
			this.baleService.baleZhongZhuanChuKuAddCwb(this.getSessionUser(), baleno.trim(), cwb.trim(), branchid);

			Bale bale = this.baleDAO.getBaleWeifengbao(baleno.trim()); //要点：做完所有的写操作最后再读出！
			this.baleDAO.updateAddBaleScannum(bale.getId());
			bale = this.baleDAO.getBaleById(bale.getId());
			long successCount = bale.getCwbcount();
			long scannum = bale.getScannum();
			obj.put("successCount", successCount);
			obj.put("scannum", scannum);
			obj.put("errorcode", "000000");
		} catch (CwbException e) {
			this.logger.error("cwb="+cwb,e);
			obj.put("errorcode", "111111");
			obj.put("errorinfo", e.getMessage());
			explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.Feng_Bao.getVediourl());
		}
		return explinkResponse;
	}

}
