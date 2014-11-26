package cn.explink.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.BaleDao;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.ExportmouldDAO;
import cn.explink.domain.Bale;
import cn.explink.domain.BaleView;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;
import cn.explink.enumutil.BaleStateEnum;
import cn.explink.enumutil.CwbOrderAddressCodeEditTypeEnum;
import cn.explink.enumutil.CwbOrderPDAEnum;
import cn.explink.exception.CwbException;
import cn.explink.service.BaleService;
import cn.explink.service.CwbOrderService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.util.Page;
import cn.explink.util.ServiceUtil;

@Controller
@RequestMapping("/bale")
public class BaleController {

	@Autowired
	BaleDao baleDAO;
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

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/add")
	public String add(Model model) {
		return "bale/add";
	}

	@RequestMapping("/create")
	public @ResponseBody String create(Model model, @RequestParam(value = "baleno", required = true) String baleno) {
		String states = BaleStateEnum.SaoMiaoZhong.getValue() + "," + BaleStateEnum.WeiDaoZhan.getValue();
		List<Bale> balelist = baleDAO.getBaleByBalenoAndBalestate(baleno, states);
		if (balelist.size() > 0) {
			return "{\"errorCode\":1,\"error\":\"包已存在\"}";
		} else {
			baleDAO.create(baleno);
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
		int isOpenFlag = jointService.getStateForJoint(B2cEnum.Amazon.getKey());
		model.addAttribute("isAmazonOpen", isOpenFlag);
		if (isshow != 0) {
			bList = baleDAO.getBaleByChukuDate(chukubegindate, chukuenddate, page);
			pageparm = new Page(baleDAO.getBaleByChukuDateCount(chukubegindate, chukuenddate), page, Page.ONE_PAGE_NUMBER);
			Map<Long, Branch> branchMap = new HashMap<Long, Branch>();
			for (Branch branch : branchDAO.getAllBranches()) {
				branchMap.put(branch.getBranchid(), branch);
			}
			model.addAttribute("branchMap", branchMap);
		}
		model.addAttribute("orderlist", clist);
		model.addAttribute("baleList", bList);
		model.addAttribute("customerlist", customerDAO.getAllCustomers());
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);
		model.addAttribute("showLetfOrRight", showLetfOrRight);
		return "/neworderquery/left";

	}

	@RequestMapping("/show/{baleid}/{page}")
	public String show(Model model, @PathVariable(value = "baleid") long baleid, @PathVariable(value = "page") long page) {
		List<CwbOrder> cwborderList = cwbDAO.getCwbOrderByBaleid(baleid, page);
		long cwborderCount = cwbDAO.getCwbOrderByBaleidCount(baleid);
		model.addAttribute("cwborderList", cwborderList);
		model.addAttribute("page_obj", new Page(cwborderCount, page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);

		Map<Long, Branch> branchMap = new HashMap<Long, Branch>();
		for (Branch branch : branchDAO.getAllBranches()) {
			branchMap.put(branch.getBranchid(), branch);
		}
		model.addAttribute("branchMap", branchMap);
		model.addAttribute("customerMap", customerDAO.getAllCustomersToMap());
		model.addAttribute("exportmouldlist", exportmouldDAO.getAllExportmouldByUser(getSessionUser().getRoleid()));

		model.addAttribute("baleid", baleid);

		return "/neworderquery/show";

	}

	@RequestMapping("/exportExcel")
	public void exportExcel(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "baleid", required = false, defaultValue = "0") long baleid,
			@RequestParam(value = "page", required = false, defaultValue = "0") long page) {
		baleService.exportExcelMethod(response, request, baleid, page);

	}

	@RequestMapping("/exportExcelBale")
	public void exportExcelBale(Model model, HttpServletResponse response, HttpServletRequest request,
			@RequestParam(value = "chukubegindate", required = false, defaultValue = "") String chukubegindate,
			@RequestParam(value = "chukuenddate", required = false, defaultValue = "") String chukuenddate) {
		baleService.baleExcel(request, response, chukubegindate, chukuenddate);

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
			@RequestParam(value = "flag", required = true, defaultValue = "0") long flag, @RequestParam(value = "confirmflag", required = false, defaultValue = "0") long confirmflag) {
		JSONObject obj = new JSONObject();
		ExplinkResponse explinkResponse = new ExplinkResponse("000000", "", obj);
		try {
			// 封包检查
			if (flag == 1) {// 库房出库
				baleService.baleaddcwbChukuCheck(getSessionUser(), baleno.trim(), cwb.trim(), confirmflag == 1, getSessionUser().getBranchid(), branchid);
			} else if (flag == 2) {// 退货出站
				baleService.baleaddcwbTuiHuoCheck(getSessionUser(), baleno, cwb, confirmflag == 1, getSessionUser().getBranchid(), branchid);
			} else if (flag == 3) {// 中转出站
				baleService.baleaddcwbzhongzhuanchuzhanCheck(getSessionUser(), baleno.trim(), cwb.trim(), confirmflag == 1, getSessionUser().getBranchid(), branchid);
			} else if (flag == 4) {// 退供货商出库
				baleService.baleaddcwbToCustomerCheck(getSessionUser(), baleno, cwb, getSessionUser().getBranchid(), branchid);
			}
			obj.put("errorcode", "000000");
		} catch (CwbException e) {
			obj.put("errorcode", "111111");
			obj.put("errorenum", e.getError());
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
	@RequestMapping("/baleaddcwb/{cwb}/{baleno}")
	public @ResponseBody ExplinkResponse baleaddcwb(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable(value = "cwb") String cwb,
			@PathVariable(value = "baleno") String baleno, @RequestParam(value = "branchid", required = true, defaultValue = "0") long branchid) {
		JSONObject obj = new JSONObject();
		ExplinkResponse explinkResponse = new ExplinkResponse("000000", "", obj);
		try {
			baleService.baleaddcwb(getSessionUser(), baleno.trim(), cwb.trim(), branchid);
			Bale bale = baleDAO.getBaleOneByBaleno(baleno.trim());
			long successCount = bale.getCwbcount();
			obj.put("successCount", successCount);
			obj.put("errorcode", "000000");
		} catch (CwbException e) {
			obj.put("errorcode", "111111");
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
			baleService.fengbao(getSessionUser(), baleno.trim(), branchid);
			obj.put("errorcode", "000000");
			explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());
		} catch (CwbException e) {
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
			try {
				baleService.fengbao(getSessionUser(), baleno.trim(), branchid);
			} catch (CwbException e) {
				flag = false;
				obj.put("errorcode", "111111");
				obj.put("errorinfo", "(按包出库异常)" + e.getMessage());
				explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.Feng_Bao.getVediourl());
			}

			// =====封包成功后出库======
			if (flag) {
				// 根据包号查找订单信息
				List<CwbOrder> cwbOrderList = cwbDAO.getListByPackagecodeExcel(baleno.trim());
				List<CwbOrder> errorList = new ArrayList<CwbOrder>();
				List<BaleView> errorListView = new ArrayList<BaleView>();
				if (cwbOrderList != null && !cwbOrderList.isEmpty()) {
					long successCount = 0;
					long errorCount = 0;
					for (CwbOrder co : cwbOrderList) {
						try {
							// 订单出库
							CwbOrder cwbOrder = cwbOrderService.outWarehous(getSessionUser(), co.getCwb(), co.getCwb(), driverid, truckid, branchid, 0, false, "", baleno, reasonid, false, true);
							successCount++;

							// ====中转出站 正确的配送站点==========
							if (deliverybranchid > 0 && branchid != deliverybranchid) {
								Branch deliverybranch = branchDAO.getBranchByBranchid(deliverybranchid);
								CwbOrderAddressCodeEditTypeEnum addressCodeEditType = CwbOrderAddressCodeEditTypeEnum.WeiPiPei;
								if (cwbOrder.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.DiZhiKu.getValue()
										|| cwbOrder.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.XiuGai.getValue()) {// 如果修改的数据原来是地址库匹配的或者是后来修改的
																																	// 都将匹配状态变更为修改
									addressCodeEditType = CwbOrderAddressCodeEditTypeEnum.XiuGai;
								} else if (cwbOrder.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.WeiPiPei.getValue()
										|| cwbOrder.getAddresscodeedittype() == CwbOrderAddressCodeEditTypeEnum.RenGong.getValue()) {// 如果修改的数据原来是为匹配的
																																		// 或者是人工匹配的
																																		// 都将匹配状态变更为人工修改
									addressCodeEditType = CwbOrderAddressCodeEditTypeEnum.RenGong;
								}
								cwbOrderService.updateDeliveryOutBranch(getSessionUser(), cwbOrder, deliverybranch, addressCodeEditType, branchid);
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
						List<Customer> customerList = customerDAO.getAllCustomers();
						errorListView = baleService.getCwbOrderCustomerView(errorList, customerList);
						obj.put("errorListView", errorListView);
						obj.put("errorinfo", "(按包出库)" + baleno + "包号，成功" + successCount + "件，失败" + errorCount + "件");
						explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.Feng_Bao.getVediourl());
					} else {
						obj.put("errorinfo", "(按包出库成功)" + baleno + "包号共" + successCount + "件");
						explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());
					}
					if (successCount > 0) {
						// 更改包的状态
						baleDAO.updateBalesate(baleno, BaleStateEnum.YiFengBaoChuKu.getValue());
					}
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
			Bale isbale = baleDAO.getBaleOneByBaleno(baleno.trim());
			CwbOrder iscwb = cwbDAO.getCwbByCwb(cwb);
			if ("0".equals(baleno) || isbale == null) {
				obj.put("errorinfo", "(合包到货异常)" + baleno + "包号不存在");
				explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.Feng_Bao.getVediourl());
				return explinkResponse;
			} else if (iscwb != null && !baleno.equals(iscwb.getPackagecode())) {
				obj.put("errorinfo", "(合包到货异常)" + cwb + "单号不在此包号中");
				explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.Feng_Bao.getVediourl());
				return explinkResponse;
			} else {
				try {
					// 订单到货
					CwbOrder cwbOrder = cwbOrderService.substationGoods(getSessionUser(), cwb, cwb, driverid, requestbatchno, comment, "", true);
					// 更改包的状态
					baleDAO.updateBalesate(baleno, BaleStateEnum.YiDaoHuo.getValue());
					obj.put("errorinfo", "(合包到货)" + cwb + "到货成功");
					explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());
				} catch (CwbException e) {
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
			try {
				baleService.fengbao(getSessionUser(), baleno.trim(), branchid);
			} catch (CwbException e) {
				flag = false;
				obj.put("errorcode", "111111");
				obj.put("errorinfo", "(按包出站异常)" + e.getMessage());
				explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.Feng_Bao.getVediourl());
			}

			// =====封包成功后出库======
			if (flag) {
				// 根据包号查找订单信息
				List<CwbOrder> cwbOrderList = cwbDAO.getListByPackagecodeExcel(baleno.trim());
				List<CwbOrder> errorList = new ArrayList<CwbOrder>();
				List<BaleView> errorListView = new ArrayList<BaleView>();
				if (cwbOrderList != null && !cwbOrderList.isEmpty()) {
					long successCount = 0;
					long errorCount = 0;
					for (CwbOrder co : cwbOrderList) {
						try {
							// 订单出站
							CwbOrder cwbOrder = cwbOrderService.outUntreadWarehous(getSessionUser(), co.getCwb(), co.getCwb(), driverid, truckid, branchid, 0, false, "", baleno, true);
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
						List<Customer> customerList = customerDAO.getAllCustomers();
						errorListView = baleService.getCwbOrderCustomerView(errorList, customerList);
						obj.put("errorListView", errorListView);
						obj.put("errorinfo", "(按包出站)" + baleno + "包号，成功" + successCount + "件，失败" + errorCount + "件");
						explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.Feng_Bao.getVediourl());
					} else {
						obj.put("errorinfo", "(按包出站成功)" + baleno + "包号共" + successCount + "件");
						explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());
					}
					if (successCount > 0) {
						// 更改包的状态
						baleDAO.updateBalesate(baleno, BaleStateEnum.YiFengBaoChuKu.getValue());
					}
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
			Bale isbale = baleDAO.getBaleOneByBaleno(baleno.trim());
			CwbOrder iscwb = cwbDAO.getCwbByCwb(cwb);
			if ("0".equals(baleno) || isbale == null) {
				obj.put("errorinfo", "(合包到货异常)" + baleno + "包号不存在");
				explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.Feng_Bao.getVediourl());
				return explinkResponse;
			} else if (iscwb != null && !baleno.equals(iscwb.getPackagecode())) {
				obj.put("errorinfo", "(合包到货异常)" + cwb + "单号不在此包号中");
				explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.Feng_Bao.getVediourl());
				return explinkResponse;
			} else {
				try {
					// 订单到货
					CwbOrder cwbOrder = cwbOrderService.backIntoWarehous(getSessionUser(), cwb, cwb, driverid, 0, comment, true);
					// 更改包的状态
					baleDAO.updateBalesate(baleno, BaleStateEnum.YiDaoHuo.getValue());
					obj.put("errorinfo", "(合包到货)" + cwb + "到货成功");
					explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());
				} catch (CwbException e) {
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
			@RequestParam(value = "comment", required = true, defaultValue = "") String comment) {
		JSONObject obj = new JSONObject();
		ExplinkResponse explinkResponse = new ExplinkResponse("000000", "", obj);
		if (!"".equals(baleno.trim()) && !"".equals(cwb.trim())) {
			Bale isbale = baleDAO.getBaleOneByBaleno(baleno.trim());
			CwbOrder iscwb = cwbDAO.getCwbByCwb(cwb);
			if ("0".equals(baleno) || isbale == null) {
				obj.put("errorinfo", "(合包到货异常)" + baleno + "包号不存在");
				explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.Feng_Bao.getVediourl());
				return explinkResponse;
			} else if (iscwb != null && !baleno.equals(iscwb.getPackagecode())) {
				obj.put("errorinfo", "(合包到货异常)" + cwb + "单号不在此包号中");
				explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.Feng_Bao.getVediourl());
				return explinkResponse;
			} else {
				try {
					// 订单到货
					CwbOrder cwbOrder = cwbOrderService.intoWarehous(getSessionUser(), cwb, cwb, 0, driverid, 0, comment, "", true);
					// cwbOrder=cwborderService.intoWarehous(getSessionUser(),cwb,cwb,customerid,
					// driverid, requestbatchno,comment,"");
					// 更改包的状态
					baleDAO.updateBalesate(baleno, BaleStateEnum.YiDaoHuo.getValue());
					obj.put("errorinfo", "(合包到货)" + cwb + "到货成功");
					explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());
				} catch (CwbException e) {
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
			try {
				baleService.fengbao(getSessionUser(), baleno.trim(), branchid);
			} catch (CwbException e) {
				flag = false;
				obj.put("errorcode", "111111");
				obj.put("errorinfo", "(按包出库异常)" + e.getMessage());
				explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.Feng_Bao.getVediourl());
			}

			// =====封包成功后出库======
			if (flag) {
				// 根据包号查找订单信息
				List<CwbOrder> cwbOrderList = cwbDAO.getListByPackagecodeExcel(baleno.trim());
				List<CwbOrder> errorList = new ArrayList<CwbOrder>();
				List<BaleView> errorListView = new ArrayList<BaleView>();
				if (cwbOrderList != null && !cwbOrderList.isEmpty()) {
					long successCount = 0;
					long errorCount = 0;
					for (CwbOrder co : cwbOrderList) {
						try {
							// 订单出库
							CwbOrder cwbOrder = cwbOrderService.backtocustom(getSessionUser(), co.getCwb(), co.getCwb(), 0, baleno, true);
							successCount++;
						} catch (CwbException e) {
							errorCount++;
							co.setRemark1(e.getMessage());// 异常原因
							errorList.add(co);
						}
					}
					if (errorCount > 0) {
						List<Customer> customerList = customerDAO.getAllCustomers();
						errorListView = baleService.getCwbOrderCustomerView(errorList, customerList);
						obj.put("errorListView", errorListView);
						obj.put("errorinfo", "(按包出库)" + baleno + "包号，成功" + successCount + "件，失败" + errorCount + "件");
						explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.Feng_Bao.getVediourl());
					} else {
						obj.put("errorinfo", "(按包出库成功)" + baleno + "包号共" + successCount + "件");
						explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());
					}
					if (successCount > 0) {
						// 更改包的状态
						baleDAO.updateBalesate(baleno, BaleStateEnum.YiFengBaoChuKu.getValue());
					}
				}
			}
		}
		return explinkResponse;
	}

}
