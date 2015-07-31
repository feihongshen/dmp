/**
 *
 */
package cn.explink.controller.express;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

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

import cn.explink.controller.ExplinkResponse;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.RoleDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbDetailView;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.Role;
import cn.explink.domain.User;

/**
 * 揽件分配/调整
 *
 * @author songkaojun 2015年7月30日
 */
@RequestMapping("/stationOperation")
@Controller
public class StationOperationController extends ExpressCommonController {
	@Autowired
	private UserDAO userDAO;

	@Autowired
	private RoleDAO roleDAO;

	@Autowired
	private CustomerDAO customerDAO;

	@Autowired
	private SecurityContextHolderStrategy securityContextHolderStrategy;

	/**
	 * 进入揽件分配/调整的功能页面
	 *
	 * @param model
	 * @param deliverid
	 * @param customerid
	 * @return
	 */
	@RequestMapping("/takeExpressAssign")
	public String branchdeliverdetail(Model model) {
		// TODO 今日待揽收列表
		List<CwbDetailView> todayToTakeList = new ArrayList<CwbDetailView>();
		// TO 今日已揽收列表
		List<CwbDetailView> todayTakedList = new ArrayList<CwbDetailView>();
		List<Customer> cList = new ArrayList<Customer>();

		List<User> deliverList = this.getDeliverList();

		model.addAttribute("todayToTakeList", todayToTakeList);
		model.addAttribute("todayTakedList", todayTakedList);
		model.addAttribute("deliverList", deliverList);
		return "express/stationOperation/takeExpressAssign";
	}

	@RequestMapping("/openAssignDlg")
	public String openAssignDlg(Model model, HttpServletRequest request) {
		List<User> deliverList = this.getDeliverList();
		model.addAttribute("deliverList", deliverList);
		return "express/stationOperation/assignDlg";
	}

	/**
	 * 进入批量操作
	 *
	 * @param model
	 * @param request
	 * @param response
	 * @param cwbs
	 * @param deliverid
	 * @return
	 */
	@RequestMapping("/takeExpressAssignBatch")
	public String takeExpressAssignBatch(Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "preOrderNos", required = false, defaultValue = "") String preOrderNos, @RequestParam(value = "deliverid", required = false, defaultValue = "0") long deliverid) {
		List<User> deliverList = this.getDeliverList();

		model.addAttribute("deliverList", deliverList);

		User deliveryUser = this.userDAO.getUserByUserid(deliverid);// 获取小件员
		List<Customer> cList = new ArrayList<Customer>();
		long allnum = 0;

		long linghuoSuccessCount = 0;

		List<JSONObject> objList = new ArrayList<JSONObject>();
		model.addAttribute("objList", objList);
		model.addAttribute("customerlist", cList);

		// TODO 今日待揽收
		List<CwbOrder> todayToTakeList = new ArrayList<CwbOrder>();
		List<CwbOrder> historyweilinghuolist = new ArrayList<CwbOrder>();// 历史待领货list
		List<CwbDetailView> historylistCwbOrders = new ArrayList<CwbDetailView>();

		model.addAttribute("todayToTakeList", todayToTakeList);
		model.addAttribute("historyweilinghuolist", historylistCwbOrders);
		model.addAttribute("todayweilinghuocount", todayToTakeList.size());
		model.addAttribute("historyweilinghuocount", historyweilinghuolist.size());
		model.addAttribute("yilinghuo", null);
		model.addAttribute("yilinghuolist", null);
		String msg = "";
		if (preOrderNos.length() > 0) {
			msg = "成功扫描" + linghuoSuccessCount + "单，异常" + (allnum - linghuoSuccessCount) + "单";
		}
		model.addAttribute("msg", msg);

		return "express/stationOperation/takeExpressAssignBatch";
	}

	/**
	 * 获取小件员
	 *
	 * @return
	 */
	private List<User> getDeliverList() {
		String roleids = "2,4";
		List<Role> roles = this.roleDAO.getRolesByIsdelivery();
		if ((roles != null) && (roles.size() > 0)) {
			for (Role r : roles) {
				roleids += "," + r.getRoleid();
			}
		}
		List<User> uList = this.userDAO.getUserByRolesAndBranchid(roleids, this.getSessionUser().getBranchid());
		return uList;
	}

	/**
	 * 获取快递数量
	 *
	 * @param deliverid
	 * @return
	 */
	@RequestMapping("/getExpressCount")
	public @ResponseBody JSONObject getExpressCount(@RequestParam(value = "deliverid", required = false, defaultValue = "0") long deliverid) {
		JSONObject obj = new JSONObject();
		obj.put("todayToTakeCount", 23);
		obj.put("todayTakedCount", 9);
		return obj;
	}

	/**
	 * 分配/调整
	 *
	 * @param model
	 * @param request
	 * @param response
	 * @param preOrderNo
	 * @param deliverid
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/assign/{preOrderNo}")
	public @ResponseBody ExplinkResponse assign(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable("preOrderNo") String preOrderNo,
			@RequestParam(value = "deliverid", required = true, defaultValue = "0") long deliverid) throws ParseException {
		// String scancwb = cwb;
		// cwb = this.cwborderService.translateCwb(cwb);
		// JSONObject obj = new JSONObject();
		// // 判断当前流程是否为今日，供上门退订单分派使用.(包括重复扫描)
		// this.addSmtDeliverPickingExtraMsg(obj, cwb);
		//
		// User deliveryUser = this.userDAO.getUserByUserid(deliverid);
		// CwbOrder cwbOrder =
		// this.cwborderService.receiveGoods(this.getSessionUser(),
		// deliveryUser, cwb, scancwb);
		// obj.put("cwbOrder", JSONObject.fromObject(cwbOrder));
		//
		// if (cwbOrder.getDeliverid() != 0) {
		// User user = this.userDAO.getUserByUserid(cwbOrder.getDeliverid());
		//
		// obj.put("cwbdelivername", user.getRealname());
		// obj.put("cwbdelivernamewav", request.getContextPath() +
		// ServiceUtil.wavPath + (user.getUserwavfile() == null ? "" :
		// user.getUserwavfile()));
		//
		// } else {
		// obj.put("cwbdelivername", "");
		// obj.put("cwbdelivernamewav", "");
		// }
		// //
		// 查询系统设置，得到name=showCustomer的express_set_system_install表中的value,加入到obj中
		// String jyp =
		// this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		// List<JsonContext> list = PDAController.test("[" + jyp + "]",
		// JsonContext.class);// 把json转换成list
		// String cwbcustomerid = String.valueOf(cwbOrder.getCustomerid());
		// String[] showcustomer = list.get(0).getCustomerid().split(",");
		// for (String s : showcustomer) {
		// if (s.equals(cwbcustomerid)) {
		// CwbOrder order = this.cwbDAO.getCwbByCwb(cwb);
		// Object a;
		// try {
		// a = order.getClass().getMethod("get" +
		// list.get(0).getRemark()).invoke(order);
		// obj.put("showRemark", a);
		// } catch (Exception e) {
		// e.printStackTrace();
		// obj.put("showRemark", "Erro");
		// }
		// }
		// }
		//
		// ExplinkResponse explinkResponse = new ExplinkResponse("000000", "",
		// obj);
		// // 加入货物类型声音.
		// this.addGoodsTypeWaveJSON(request, cwbOrder, explinkResponse);
		// String wavPath = null;
		// if
		// (explinkResponse.getStatuscode().equals(CwbOrderPDAEnum.OK.getCode()))
		// {
		// wavPath = request.getContextPath() + ServiceUtil.waverrorPath +
		// CwbOrderPDAEnum.OK.getVediourl();
		// } else {
		// wavPath = request.getContextPath() + ServiceUtil.waverrorPath +
		// CwbOrderPDAEnum.SYS_ERROR.getVediourl();
		// }
		// explinkResponse.addLastWav(wavPath);
		//
		// return explinkResponse;
		return new ExplinkResponse();
	}

	/**
	 * 今日待揽收
	 *
	 * @param page
	 * @param deliverid
	 * @param showCustomerSign
	 * @param customerlist
	 * @return
	 */
	@RequestMapping("/getMoreTodayToTakeList")
	public @ResponseBody List<CwbDetailView> getMoreTodayToTakeList(@RequestParam(value = "page", defaultValue = "1") long page, @RequestParam(value = "deliverid", defaultValue = "0") long deliverid) {
		// List<Branch> branchList = this.branchDAO.getAllBranches();
		// // 今日到货订单数
		// // List<String> todaydaohuocwbs =
		// //
		// orderFlowDAO.getOrderFlowLingHuoList(getSessionUser().getBranchid(),
		// //
		// FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()+","+FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(),
		// // DateTimeUtil.getCurrentDayZeroTime(), "");
		// List<String> todaydaohuocwbs =
		// this.operationTimeDAO.getOrderFlowLingHuoList(this.getSessionUser().getBranchid(),
		// FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + ","
		// + FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(),
		// DateTimeUtil.getCurrentDayZeroTime());
		//
		// // 今日滞留订单数
		// // List<String> todayzhiliucwbs =
		// //
		// orderFlowDAO.getOrderFlowLingHuoList(getSessionUser().getBranchid(),
		// // FlowOrderTypeEnum.YiShenHe.getValue()+"",
		// // DateTimeUtil.getCurrentDayZeroTime(), "");
		// List<String> todayzhiliucwbs =
		// this.operationTimeDAO.getjinrizhiliu(this.getSessionUser().getBranchid(),
		// DeliveryStateEnum.FenZhanZhiLiu.getValue(),
		// FlowOrderTypeEnum.YiShenHe.getValue(),
		// DateTimeUtil.getCurrentDayZeroTime());
		// // 今日到货订单
		// List<CwbOrder> todaydaohuolist = new ArrayList<CwbOrder>();
		// if (todaydaohuocwbs.size() > 0) {
		// todaydaohuolist =
		// this.cwbDAO.getTodayWeiLingDaohuobyBranchid(this.getSessionUser().getBranchid(),
		// this.getStrings(todaydaohuocwbs));
		// }
		// // 今日滞留订单
		// List<CwbOrder> todayzhiliulist = new ArrayList<CwbOrder>();
		// if (todayzhiliucwbs.size() > 0) {
		// todayzhiliulist =
		// this.cwbDAO.getTodayWeiLingZhiliuByWhereList(DeliveryStateEnum.FenZhanZhiLiu.getValue(),
		// this.getSessionUser().getBranchid(),
		// this.getStrings(todayzhiliucwbs));
		// }
		// List<CwbOrder> cList = new ArrayList<CwbOrder>();
		// cList.addAll(todaydaohuolist);
		// cList.addAll(todayzhiliulist);
		// // 系统设置是否显示订单备注
		// String showCustomer =
		// this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		// JSONArray showCustomerjSONArray = JSONArray.fromObject("[" +
		// showCustomer + "]");
		// List<CwbOrder> todaylistCwbOrders = new ArrayList<CwbOrder>();
		// for (int i = 0; (i < Page.DETAIL_PAGE_NUMBER) && (i < cList.size());
		// i++) {
		// todaylistCwbOrders.add(cList.get(i));
		// }
		// List<CwbDetailView> todayweilingViewlist =
		// this.getcwbDetail(todaylistCwbOrders, customerlist,
		// showCustomerjSONArray, branchList, 2);
		// return todayweilingViewlist;

		return new ArrayList<CwbDetailView>();
	}

	/**
	 * 获取今日已揽收
	 *
	 * @param deliverid
	 * @param page
	 * @return
	 */
	@RequestMapping("/getMoreTodayTakedList")
	public @ResponseBody List<CwbDetailView> getMoreTodayTakedList(@RequestParam(value = "deliverid", defaultValue = "0") long deliverid, @RequestParam(value = "page", defaultValue = "1") long page) {
		// List<Branch> branchList = this.branchDAO.getAllBranches();
		// // 今日到货订单数
		// // List<String> todaydaohuocwbs =
		// //
		// orderFlowDAO.getOrderFlowLingHuoList(getSessionUser().getBranchid(),
		// //
		// FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()+","+FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(),
		// // DateTimeUtil.getCurrentDayZeroTime(), "");
		// // List<String> todaydaohuocwbs =
		// //
		// operationTimeDAO.getOrderFlowLingHuoList(getSessionUser().getBranchid(),
		// //
		// FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()+","+FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(),
		// // DateTimeUtil.getCurrentDayZeroTime());
		// // 今日滞留订单数
		// // List<String> todayzhiliucwbs =
		// //
		// orderFlowDAO.getOrderFlowLingHuoList(getSessionUser().getBranchid(),
		// // FlowOrderTypeEnum.YiShenHe.getValue()+"",
		// // DateTimeUtil.getCurrentDayZeroTime(), "");
		// // List<String> todayzhiliucwbs =
		// // operationTimeDAO.getjinrizhiliu(getSessionUser().getBranchid(),
		// //
		// DeliveryStateEnum.FenZhanZhiLiu.getValue(),FlowOrderTypeEnum.YiShenHe.getValue(),
		// // DateTimeUtil.getCurrentDayZeroTime());
		//
		// List<CwbOrder> cList = new ArrayList<CwbOrder>();
		// List<CwbOrder> historydaohuolist = new ArrayList<CwbOrder>();
		// // 历史到货订单
		// // List<CwbOrder> historydaohuolist =
		// //
		// cwbDAO.getHistoryyWeiLingDaohuobyBranchid(getSessionUser().getBranchid(),getStrings(todaydaohuocwbs));
		// List<String> historycwbs =
		// this.operationTimeDAO.getlishidaohuo(this.getSessionUser().getBranchid(),
		// FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue() + ","
		// + FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(),
		// DateTimeUtil.getCurrentDayZeroTime());
		//
		// if (historycwbs.size() > 0) {
		//
		// historydaohuolist =
		// this.cwbDAO.getCwbOrderByFlowordertypeAndCwbs(this.getSessionUser().getBranchid(),
		// FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue() + ","
		// + FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(),
		// this.getStrings(historycwbs));
		// }
		//
		// List<CwbOrder> historyzhiliulist = new ArrayList<CwbOrder>();//
		// 历史待领货list
		// // 历史滞留订单
		// // List<CwbOrder> historyzhiliulist =
		// //
		// cwbDAO.getHistoryWeiLingZhiliuByWhereList(DeliveryStateEnum.FenZhanZhiLiu.getValue(),getSessionUser().getBranchid(),getStrings(todayzhiliucwbs));
		// List<String> historyzhiliucwbs =
		// this.operationTimeDAO.getlishizhiliu(this.getSessionUser().getBranchid(),
		// DeliveryStateEnum.FenZhanZhiLiu.getValue(),
		// FlowOrderTypeEnum.YiShenHe.getValue(),
		// DateTimeUtil.getCurrentDayZeroTime());
		//
		// if (historyzhiliucwbs.size() > 0) {
		// historyzhiliulist =
		// this.cwbDAO.getCwbOrderByDeliverystateAndCwbs(DeliveryStateEnum.FenZhanZhiLiu.getValue(),
		// this.getStrings(historyzhiliucwbs));
		// }
		//
		// cList.addAll(historydaohuolist);
		// cList.addAll(historyzhiliulist);
		// List<CwbOrder> historylistCwbOrders = new ArrayList<CwbOrder>();
		// for (int i = 0; (i < Page.DETAIL_PAGE_NUMBER) && (i < cList.size());
		// i++) {
		// historylistCwbOrders.add(cList.get(i));
		// }
		//
		// // 系统设置是否显示订单备注
		// String showCustomer =
		// this.systemInstallDAO.getSystemInstall("showCustomer").getValue();
		// JSONArray showCustomerjSONArray = JSONArray.fromObject("[" +
		// showCustomer + "]");
		// // 已入库明细
		// List<CwbDetailView> weidaohuoViewlist =
		// this.getcwbDetail(historylistCwbOrders,
		// this.customerDAO.getAllCustomers(), showCustomerjSONArray,
		// branchList, 2);
		// return weidaohuoViewlist;
		return new ArrayList<CwbDetailView>();
	}

}
