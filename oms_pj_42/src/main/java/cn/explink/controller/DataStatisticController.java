package cn.explink.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.constant.Constants;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.CwbOrderTailDao;
import cn.explink.dao.CwbTiHuoDAO;
import cn.explink.dao.DeliveryChukuDAO;
import cn.explink.dao.DeliveryDaohuoDAO;
import cn.explink.dao.DeliveryJuShouDAO;
import cn.explink.dao.DeliverySuccessfulDAO;
import cn.explink.dao.DeliveryZhiLiuDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.KDKDeliveryChukuDAO;
import cn.explink.dao.KuFangRuKuDao;
import cn.explink.dao.KuFangZaiTuDao;
import cn.explink.dao.TuiHuoChuZhanDao;
import cn.explink.dao.TuiHuoZhanRuKuDao;
import cn.explink.dao.UpdateMessageDAO;
import cn.explink.dao.ZhongZhuanDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.CwbOrderTail;
import cn.explink.domain.CwbTiHuo;
import cn.explink.domain.DeliveryChuku;
import cn.explink.domain.DeliveryDaohuo;
import cn.explink.domain.DeliveryJuShou;
import cn.explink.domain.DeliveryRateCondition;
import cn.explink.domain.DeliverySuccessful;
import cn.explink.domain.DeliveryZhiLiu;
import cn.explink.domain.DownloadManager;
import cn.explink.domain.Exportmould;
import cn.explink.domain.KDKDeliveryChuku;
import cn.explink.domain.KuFangRuKuOrder;
import cn.explink.domain.KuFangZaiTuOrder;
import cn.explink.domain.Reason;
import cn.explink.domain.Remark;
import cn.explink.domain.TuiHuoChuZhanOrder;
import cn.explink.domain.TuiHuoZhanRuKuOrder;
import cn.explink.domain.User;
import cn.explink.domain.ZhongZhuan;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderColumnMap;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.ModelEnum;
import cn.explink.enumutil.UpdateMessageMenuNameEnum;
import cn.explink.service.CwbOrderTailService;
import cn.explink.service.DataStatisticService;
import cn.explink.service.DeliveryRateService;
import cn.explink.service.DownloadManagerService;
import cn.explink.util.DateDayUtil;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.JsonUtil;
import cn.explink.util.Page;
import cn.explink.vo.delivery.CustomizedDeliveryDateType;
import cn.explink.vo.delivery.DeliveryRateAggregation;
import cn.explink.vo.delivery.DeliveryRateBranchOrCustomerAggregation;
import cn.explink.vo.delivery.DeliveryRateComputeType;
import cn.explink.vo.delivery.DeliveryRateConditionWrapper;
import cn.explink.vo.delivery.DeliveryRateDateAggregation;
import cn.explink.vo.delivery.DeliveryRateQueryType;
import cn.explink.vo.delivery.DeliveryRateRequest;
import cn.explink.vo.delivery.DeliveryRateTimeType;
import cn.explink.vo.delivery.DownloadManagerWrapper;

@RequestMapping("/datastatistics")
@Controller
public class DataStatisticController {

	@Autowired
	GetDmpDAO getDmpDAO;

	@Autowired
	DataStatisticService dataStatisticService;

	@Autowired
	DeliverySuccessfulDAO deliverySuccessfulDAO;

	@Autowired
	DeliveryChukuDAO deliveryChukuDAO;

	@Autowired
	DeliveryDaohuoDAO deliveryDaohuoDAO;

	@Autowired
	CwbDAO cwbDAO;

	@Autowired
	DownloadManagerService downloadManagerService;

	@Autowired
	DeliveryZhiLiuDAO deliveryZhiLiuDAO;

	@Autowired
	TuiHuoChuZhanDao tuiHuoChuZhanDao;

	@Autowired
	UpdateMessageDAO updateMessageDAO;

	@Autowired
	ZhongZhuanDAO zhongzhuanDAO;

	@Autowired
	DeliveryJuShouDAO deliveryJuShouDAO;

	@Autowired
	CwbTiHuoDAO cwbTiHuoDAO;

	@Autowired
	KDKDeliveryChukuDAO kdkDeliveryChukuDAO;

	@Autowired
	TuiHuoZhanRuKuDao tuiHuoZhanRuKuDao;

	@Autowired
	KuFangZaiTuDao kufangZaiTuDao;

	@Autowired
	KuFangRuKuDao kuFangRuKuDao;

	@Autowired
	CwbOrderTailService cwbOrderTailService;

	@Autowired
	DeliveryRateService deliveryRateService;

	@Autowired
	private CwbOrderTailDao cwbOrderTaildao;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 妥投订单汇总
	 *
	 * @param model
	 * @param begindate
	 * @param enddate
	 * @param isaudit
	 * @param isauditTime
	 * @param customerids
	 * @param cwbordertypeids
	 * @param paywayid
	 * @param dispatchbranchids
	 * @param deliverid
	 * @param operationOrderResultTypes
	 * @param isshow
	 * @param page
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/tuotousearch/{page}")
	public String tuotousearch(Model model, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate,
			@RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "isaudit", required = false, defaultValue = "-1") long isaudit,
			@RequestParam(value = "isauditTime", required = false, defaultValue = "0") long isauditTime, @RequestParam(value = "customerid", required = false, defaultValue = "") String[] customerids,
			@RequestParam(value = "cwbordertypeid", required = false, defaultValue = "") String[] cwbordertypeids,
			@RequestParam(value = "paytype", required = false, defaultValue = "-1") long paywayid,
			@RequestParam(value = "dispatchbranchid", required = false, defaultValue = "") String[] dispatchbranchids,
			@RequestParam(value = "deliverid", required = false, defaultValue = "-1") long deliverid,
			@RequestParam(value = "operationOrderResultType", required = false, defaultValue = "") String[] operationOrderResultTypes,
			@RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow, @PathVariable(value = "page") long page,
			@RequestParam(value = "paybackfeeIsZero", required = false, defaultValue = "-1") Integer paybackfeeIsZero, HttpServletResponse response, HttpServletRequest request) {
		long count = 0;
		Page pageparm = new Page();
		DeliverySuccessful sum = new DeliverySuccessful();
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = this.getDmpDAO.getLogUser(dmpid);
		// 加载供货商
		List<Customer> customerlist = this.getDmpDAO.getAllCustomers();
		// 需要返回页面的前10条订单List
		List<CwbOrder> orderlist = new ArrayList<CwbOrder>();
		// 加载站点区域权限
		List<Branch> branchnameList = new ArrayList<Branch>();
		if ((user != null) && (user.getUserid() > 0)) {
			Branch branch = this.getDmpDAO.getNowBranch(user.getBranchid());
			branchnameList = this.getDmpDAO.getQueryBranchByBranchsiteAndUserid(user.getUserid(),
					BranchEnum.ZhanDian.getValue() + "," + BranchEnum.TuiHuo.getValue() + "," + BranchEnum.ZhongZhuan.getValue());
			if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
				if (branchnameList.size() == 0) {
					branchnameList.add(branch);
				} else {
					if (!this.dataStatisticService.checkBranchRepeat(branchnameList, branch)) {
						branchnameList.add(branch);
					}
				}
			}
		}
		// 加载导出模板
		List<Exportmould> exportmouldlist = this.getDmpDAO.getExportmoulds(user, dmpid);
		// 保存订单配送结果的选择
		if (operationOrderResultTypes.length == 0) {
			operationOrderResultTypes = new String[] { DeliveryStateEnum.PeiSongChengGong.getValue() + "", DeliveryStateEnum.ShangMenHuanChengGong.getValue() + "",
					DeliveryStateEnum.ShangMenTuiChengGong.getValue() + "" };
		}
		List<String> operationOrderResultTypeslist = this.dataStatisticService.getList(operationOrderResultTypes);
		// 保存供货商的选择
		List<String> customeridList = new ArrayList<String>();
		// 保存站点的选择
		List<String> dispatchbranchidList = new ArrayList<String>();
		// 保存订单类型的选择
		List<String> cwbordertypeidList = new ArrayList<String>();
		// 保存小件员的选择
		List<User> deliverlist = new ArrayList<User>();

		if ((user == null) || (user.getUserid() == 0)) {// 如果登录失效，提示登录失败
			model.addAttribute("nouser", "nouser");
		} else if (isshow == 1) {// 如果是点击查询按钮，封装查询数据
			this.logger.info("妥投订单汇总(云)，操作人{}，选择条件 begindate:" + begindate + ",enddate:" + enddate + ",isaudit:" + isaudit + ",customerid:" + this.dataStatisticService.getStrings(customerids)
					+ ",cwbordertypeid:" + this.dataStatisticService.getStrings(cwbordertypeids) + ",paywayid:" + paywayid + ",isauditTime:" + isauditTime + ",deliverid:" + deliverid
					+ ",dispatchbranchids:" + this.dataStatisticService.getStrings(dispatchbranchids) + ",operationOrderResultTypes:" + this.dataStatisticService.getStrings(operationOrderResultTypes)
					+ ",isshow:" + isshow + ",page:" + page, user.getRealname());
			// 保存供货商的选择
			customeridList = this.dataStatisticService.getList(customerids);
			// 保存站点的选择
			if ((dispatchbranchids.length == 0) & (branchnameList.size() > 0)) {
				dispatchbranchids = new String[branchnameList.size()];
				for (Branch bc : branchnameList) {
					dispatchbranchids[branchnameList.indexOf(bc)] = bc.getBranchid() + "";

				}
			}
			dispatchbranchidList = this.dataStatisticService.getList(dispatchbranchids);
			// 保存订单类型的选择
			cwbordertypeidList = this.dataStatisticService.getList(cwbordertypeids);
			// 保存小件员的选择
			deliverlist = this.getDmpDAO.getAllUserByBranchIds(this.dataStatisticService.getStrings(dispatchbranchids));
			String customeridStr = this.dataStatisticService.getStrings(customerids);
			String cwbordertypeidStr = this.dataStatisticService.getStrings(cwbordertypeids);
			String dispatchbranchidStr = this.dataStatisticService.getStrings(dispatchbranchids);
			String operationOrderResultTypeStr = this.dataStatisticService.getStrings(operationOrderResultTypes);

			List<DeliverySuccessful> delList = this.deliverySuccessfulDAO.getDeliverySuccessfulList(begindate, enddate, isaudit, isauditTime, customeridStr, cwbordertypeidStr, paywayid,
					dispatchbranchidStr, deliverid, operationOrderResultTypeStr, page, paybackfeeIsZero);

			sum = this.deliverySuccessfulDAO.getDeliverySuccessfulSum(begindate, enddate, isaudit, isauditTime, customeridStr, cwbordertypeidStr, paywayid, dispatchbranchidStr, deliverid,
					operationOrderResultTypeStr, page, paybackfeeIsZero);
			count = sum.getId();
			pageparm = new Page(count, page, Page.ONE_PAGE_NUMBER);
			if ((delList != null) && (delList.size() > 0)) {
				String cwbs = "";
				for (DeliverySuccessful deliverySuccessful : delList) {
					cwbs += "'" + deliverySuccessful.getCwb() + "',";
				}
				cwbs = cwbs.length() > 0 ? cwbs.substring(0, cwbs.length() - 1) : "";
				if (cwbs.length() > 0) {
					orderlist = this.cwbDAO.getCwbOrderByCwbs(cwbs);
					List<Branch> branchList = this.getDmpDAO.getAllBranchs();
					List<User> userList = this.getDmpDAO.getUserForALL();
					orderlist = this.dataStatisticService.getCwbOrderViewCount10(orderlist, delList, customerlist, branchList, userList);
				}
			}
			String dmpURl = this.getDmpDAO.getDmpurl().substring(this.getDmpDAO.getDmpurl().lastIndexOf("/"), this.getDmpDAO.getDmpurl().length());
			model.addAttribute("dmpUrl", dmpURl);
			this.logger.info("妥投订单汇总(云)，当前操作人{},条数{}", user.getRealname(), count);

		}
		model.addAttribute("deliverlist", deliverlist);
		model.addAttribute("count", count);
		model.addAttribute("sum", sum.getReceivablefee());
		model.addAttribute("paybackfeesum", sum.getPaybackfee());
		model.addAttribute("orderlist", orderlist);
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);
		model.addAttribute("deliverid", deliverid);
		model.addAttribute("check", 1);
		model.addAttribute("enddate", enddate);

		model.addAttribute("branchList", branchnameList);
		model.addAttribute("customerlist", customerlist);
		model.addAttribute("exportmouldlist", exportmouldlist);
		model.addAttribute("operationOrderResultTypeStr", operationOrderResultTypeslist);
		model.addAttribute("dispatchbranchidStr", dispatchbranchidList);
		model.addAttribute("cwbordertypeidStr", cwbordertypeidList);
		model.addAttribute("customeridStr", customeridList);
		model.addAttribute("lastupdatetime", this.updateMessageDAO.getUpdateMessageByMenunvalue(UpdateMessageMenuNameEnum.TuoTOuDingDanHuiZong.getValue()).getLastupdatetime());
		return "datastatistics/tuotoulist";

	}

	/**
	 * 库房出库统计(云)
	 *
	 * @param model
	 * @param begindate
	 * @param enddate
	 * @param kufangid
	 * @param customerid
	 * @param nextbranchid
	 * @param cwbordertypeid
	 * @param isshow
	 * @param page
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/outwarehousedata/{page}")
	public String outwarehousedata(Model model, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate,
			@RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "kufangid", required = false, defaultValue = "-1") String[] kufangid,
			@RequestParam(value = "customerid", required = false, defaultValue = "") String[] customerid,
			@RequestParam(value = "nextbranchid", required = false, defaultValue = "") String[] nextbranchid,
			@RequestParam(value = "cwbordertypeid", required = false, defaultValue = "") String[] cwbordertypeid, @RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow,
			@PathVariable(value = "page") long page, HttpServletResponse response, HttpServletRequest request) {

		long count = 0;
		Page pageparm = new Page();
		DeliveryChuku deliveryChukusum = new DeliveryChuku();
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = this.getDmpDAO.getLogUser(dmpid);
		// 加载供货商
		List<Customer> customerlist = this.getDmpDAO.getAllCustomers();
		// 需要返回页面的前10条订单List
		List<CwbOrder> orderlist = new ArrayList<CwbOrder>();
		// 加载站点区域权限
		// 管退货的中转站站点
		List<Branch> branchAllList = new ArrayList<Branch>();
		// 管退货的中转站库房
		List<Branch> kufangList = new ArrayList<Branch>();
		if ((user != null) && (user.getUserid() > 0)) {

			// 按beanchid 查询站点
			Branch branch = this.getDmpDAO.getNowBranch(user.getBranchid());
			// 按用户ID和站点(2)+退货(3)+中转(4) 按照站点类型和用户ID查找 2,3,4:代表 处理退货的中转站
			branchAllList = this.getDmpDAO.getQueryBranchByBranchsiteAndUserid(user.getUserid(),
					BranchEnum.ZhanDian.getValue() + "," + BranchEnum.TuiHuo.getValue() + "," + BranchEnum.ZhongZhuan.getValue());
			// 按用户ID和库房(1)+退货(3)+中转(4) 按照站点类型和用户ID查找 2,3,4:代表 处理退货的中转站
			kufangList = this.getDmpDAO.getQueryBranchByBranchsiteAndUserid(user.getUserid(), BranchEnum.KuFang.getValue() + "");
			// 如果说站点的类型是库房或退货或中转站的时候进入判断
			if ((branch.getSitetype() == BranchEnum.KuFang.getValue()) || (branch.getSitetype() == BranchEnum.TuiHuo.getValue()) || (branch.getSitetype() == BranchEnum.ZhongZhuan.getValue())) {
				// 库房的集合为0的时候,添加符合以上条件的站点到库房集合中(这..........,因为退货业务中退步到合适的库房,所以由站点直接退?)
				if (kufangList.size() == 0) {
					kufangList.add(branch);
				} else {
					// 如果集合不为0,判断集合中是否存在这个,如果不存在就添加
					if (!this.dataStatisticService.checkBranchRepeat(kufangList, branch)) {
						kufangList.add(branch);
					}
				}
				// 如果说站点的类型是站点进入判断
			} else if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
				// 同上子判断
				if (branchAllList.size() == 0) {
					branchAllList.add(branch);
				} else {
					if (!this.dataStatisticService.checkBranchRepeat(branchAllList, branch)) {
						branchAllList.add(branch);
					}
				}
			}
		}
		// 加载导出模板
		List<Exportmould> exportmouldlist = this.getDmpDAO.getExportmoulds(user, dmpid);

		// 保存供货商的选择
		List<String> customeridList = new ArrayList<String>();
		// 保存站点的选择
		List<String> nextbranchidList = new ArrayList<String>();
		// 保存订单类型的选择
		List<String> cwbordertypeidList = new ArrayList<String>();
		// 保存发货仓库
		List<String> kufangidList = new ArrayList<String>();
		if ((user == null) || (user.getUserid() == 0)) {// 如果登录失效，提示登录失败
			model.addAttribute("nouser", "nouser");
		} else if (isshow == 1) {// 如果是点击查询按钮，封装查询数据
			this.logger.info(
					"库房出库统计(云)，操作人{}，选择条件 begindate:" + begindate + ",enddate:" + enddate + ",kufangid:" + this.dataStatisticService.getStrings(kufangid) + ",customerid:"
							+ this.dataStatisticService.getStrings(customerid) + ",cwbordertypeid:" + this.dataStatisticService.getStrings(cwbordertypeid) + ",nextbranchid:"
							+ this.dataStatisticService.getStrings(nextbranchid) + ",isshow:" + isshow + ",page:" + page, user.getRealname());
			begindate = begindate.length() == 0 ? DateTimeUtil.getNowTime() : begindate;
			enddate = enddate.length() == 0 ? DateTimeUtil.getNowTime() : enddate;
			// 保存供货商的选择 数组转List
			customeridList = this.dataStatisticService.getList(customerid);
			// 保存发货仓库 数组转List
			kufangidList = this.dataStatisticService.getList(kufangid);

			// 保存站点的选择 数组转List
			nextbranchidList = this.dataStatisticService.getList(nextbranchid);
			// 保存订单类型的选择 数组转List
			cwbordertypeidList = this.dataStatisticService.getList(cwbordertypeid);
			// 数组转逗号分割字符串,最后把多个供应商，订单类型ID，下一站ID，库房ID的集用逗号分开，使用这种数据方便做数据库操作
			String customerids = this.dataStatisticService.getStrings(customerid);
			String cwbordertypeids = this.dataStatisticService.getStrings(cwbordertypeid);
			String nextbranchids = this.dataStatisticService.getStrings(nextbranchid);
			String kufangids = this.dataStatisticService.getStrings(kufangid);

			// 查询出库表 ops_delivery_chuku,按条件查询数据
			// 除汇总数据
			List<DeliveryChuku> delList = this.deliveryChukuDAO.getDeliveryChukuList(page, begindate, enddate, customerids, kufangids, nextbranchids, cwbordertypeids);

			// 查询出库表 ops_delivery_chuku,整合汇总数据
			// private BigDecimal receivablefee;//代收货款应收金额
			// private BigDecimal paybackfee;//上门退货应退金额
			deliveryChukusum = this.deliveryChukuDAO.getDeliveryChukuSum(begindate, enddate, customerids, kufangids, nextbranchids, cwbordertypeids);

			count = deliveryChukusum.getId();
			pageparm = new Page(count, page, Page.ONE_PAGE_NUMBER);
			if ((delList != null) && (delList.size() > 0)) {
				String cwbs = "";
				for (DeliveryChuku deliveryChuku : delList) {
					cwbs += "'" + deliveryChuku.getCwb() + "',";
				}
				cwbs = cwbs.length() > 0 ? cwbs.substring(0, cwbs.length() - 1) : "";
				if (cwbs.length() > 0) {
					orderlist = this.cwbDAO.getCwbOrderByCwbs(cwbs);
					List<Branch> branchList = this.getDmpDAO.getAllBranchs();
					List<User> userList = this.getDmpDAO.getUserForALL();
					orderlist = this.dataStatisticService.getChukuCwbOrderViewCount10(orderlist, delList, customerlist, branchList, userList);
				}
			}
			String dmpURl = this.getDmpDAO.getDmpurl().substring(this.getDmpDAO.getDmpurl().lastIndexOf("/"), this.getDmpDAO.getDmpurl().length());
			model.addAttribute("dmpUrl", dmpURl);
			this.logger.info("库房出库统计(云)，当前操作人{},条数{}", user.getRealname(), count);
		}

		model.addAttribute("count", count);
		model.addAttribute("sum", deliveryChukusum.getReceivablefee());
		model.addAttribute("paybackfeesum", deliveryChukusum.getPaybackfee());
		model.addAttribute("orderlist", orderlist);
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);

		model.addAttribute("branchList", branchAllList);
		model.addAttribute("customerlist", customerlist);
		model.addAttribute("exportmouldlist", exportmouldlist);
		model.addAttribute("kufangList", kufangList);

		model.addAttribute("cwbordertypeidStr", cwbordertypeidList);
		model.addAttribute("customeridStr", customeridList);
		model.addAttribute("kufangidStr", kufangidList);
		model.addAttribute("kufangid", kufangid);
		model.addAttribute("nextbranchidStr", nextbranchidList);
		model.addAttribute("lastupdatetime", this.updateMessageDAO.getUpdateMessageByMenunvalue(UpdateMessageMenuNameEnum.KuFangChuKuTongJi.getValue()).getLastupdatetime());
		return "datastatistics/outwarehousedatalist";
	}

	/**
	 * 库对库出库统计功能
	 *
	 * @param model
	 * @param begindate
	 * @param enddate
	 * @param kufangid
	 * @param customerid
	 * @param nextbranchid
	 * @param cwbordertypeid
	 * @param isshow
	 * @param page
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/kdkoutwarehousedata/{page}")
	public String kdkoutwarehousedata(Model model, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate,
			@RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "kufangid", required = false, defaultValue = "-1") String[] kufangid,
			@RequestParam(value = "customerid", required = false, defaultValue = "") String[] customerid,
			@RequestParam(value = "nextbranchid", required = false, defaultValue = "") String[] nextbranchid,
			@RequestParam(value = "cwbordertypeid", required = false, defaultValue = "") String[] cwbordertypeid, @RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow,
			@PathVariable(value = "page") long page, HttpServletResponse response, HttpServletRequest request) {

		long count = 0;
		Page pageparm = new Page();
		KDKDeliveryChuku kdkdeliveryChukusum = new KDKDeliveryChuku();
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = this.getDmpDAO.getLogUser(dmpid);
		// 加载供货商
		List<Customer> customerlist = this.getDmpDAO.getAllCustomers();
		// 需要返回页面的前10条订单List
		List<CwbOrder> orderlist = new ArrayList<CwbOrder>();
		// 加载站点区域权限
		List<Branch> kufangList = new ArrayList<Branch>();
		if ((user != null) && (user.getUserid() > 0)) {
			Branch branch = this.getDmpDAO.getNowBranch(user.getBranchid());
			kufangList = this.getDmpDAO.getQueryBranchByBranchsiteAndUserid(user.getUserid(), BranchEnum.KuFang.getValue() + "");
			if (branch.getSitetype() == BranchEnum.KuFang.getValue()) {
				if (kufangList.size() == 0) {
					kufangList.add(branch);
				} else {
					if (!this.dataStatisticService.checkBranchRepeat(kufangList, branch)) {
						kufangList.add(branch);
					}
				}
			}
		}
		// 加载导出模板
		List<Exportmould> exportmouldlist = this.getDmpDAO.getExportmoulds(user, dmpid);

		// 保存供货商的选择
		List<String> customeridList = new ArrayList<String>();
		// 保存站点的选择
		List<String> nextbranchidList = new ArrayList<String>();
		// 保存订单类型的选择
		List<String> cwbordertypeidList = new ArrayList<String>();
		// 保存发货库房
		List<String> kufangidList = new ArrayList<String>();

		if ((user == null) || (user.getUserid() == 0)) {// 如果登录失效，提示登录失败
			model.addAttribute("nouser", "nouser");
		} else if (isshow == 1) {// 如果是点击查询按钮，封装查询数据
			this.logger.info(
					"库对库出库统计(云)，操作人{}，选择条件 begindate:" + begindate + ",enddate:" + enddate + ",kufangid:" + this.dataStatisticService.getStrings(kufangid) + ",customerid:"
							+ this.dataStatisticService.getStrings(customerid) + ",cwbordertypeid:" + this.dataStatisticService.getStrings(cwbordertypeid) + ",nextbranchid:"
							+ this.dataStatisticService.getStrings(nextbranchid) + ",isshow:" + isshow + ",page:" + page, user.getRealname());
			begindate = begindate.length() == 0 ? DateTimeUtil.getNowTime() : begindate;
			enddate = enddate.length() == 0 ? DateTimeUtil.getNowTime() : enddate;
			// 保存供货商的选择
			customeridList = this.dataStatisticService.getList(customerid);
			// 保存站点的选择
			nextbranchidList = this.dataStatisticService.getList(nextbranchid);
			// 保存订单类型的选择
			cwbordertypeidList = this.dataStatisticService.getList(cwbordertypeid);
			// 保存发货库房
			kufangidList = this.dataStatisticService.getList(kufangid);

			String customerids = this.dataStatisticService.getStrings(customerid);
			String cwbordertypeids = this.dataStatisticService.getStrings(cwbordertypeid);
			String nextbranchids = this.dataStatisticService.getStrings(nextbranchid);
			String kufangids = this.dataStatisticService.getStrings(kufangid);

			List<KDKDeliveryChuku> delList = this.kdkDeliveryChukuDAO.getKDKDeliveryChukuList(page, begindate, enddate, customerids, kufangids, nextbranchids, cwbordertypeids);

			kdkdeliveryChukusum = this.kdkDeliveryChukuDAO.getKDKDeliveryChukuSum(begindate, enddate, customerids, kufangids, nextbranchids, cwbordertypeids);
			count = kdkdeliveryChukusum.getId();
			pageparm = new Page(count, page, Page.ONE_PAGE_NUMBER);
			if ((delList != null) && (delList.size() > 0)) {
				String cwbs = "";
				for (KDKDeliveryChuku kdkdeliveryChuku : delList) {
					cwbs += "'" + kdkdeliveryChuku.getCwb() + "',";
				}
				cwbs = cwbs.length() > 0 ? cwbs.substring(0, cwbs.length() - 1) : "";
				if (cwbs.length() > 0) {
					orderlist = this.cwbDAO.getCwbOrderByCwbs(cwbs);
					List<Branch> branchList = this.getDmpDAO.getAllBranchs();
					List<User> userList = this.getDmpDAO.getUserForALL();
					orderlist = this.dataStatisticService.getKDKChukuCwbOrderViewCount10(orderlist, delList, customerlist, branchList, userList);
				}
			}
			String dmpURl = this.getDmpDAO.getDmpurl().substring(this.getDmpDAO.getDmpurl().lastIndexOf("/"), this.getDmpDAO.getDmpurl().length());
			model.addAttribute("dmpUrl", dmpURl);
			this.logger.info("库对库出库统计(云)，当前操作人{},条数{}", user.getRealname(), count);
		}

		model.addAttribute("count", count);
		model.addAttribute("sum", kdkdeliveryChukusum.getReceivablefee());
		model.addAttribute("paybackfeesum", kdkdeliveryChukusum.getPaybackfee());
		model.addAttribute("orderlist", orderlist);
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);

		model.addAttribute("customerlist", customerlist);
		model.addAttribute("exportmouldlist", exportmouldlist);
		model.addAttribute("kufangList", kufangList);

		model.addAttribute("cwbordertypeidStr", cwbordertypeidList);
		model.addAttribute("customeridStr", customeridList);
		model.addAttribute("kufangidStr", kufangidList);
		model.addAttribute("kufangid", kufangid);
		model.addAttribute("nextbranchidStr", nextbranchidList);
		model.addAttribute("lastupdatetime", this.updateMessageDAO.getUpdateMessageByMenunvalue(UpdateMessageMenuNameEnum.KDKKuFangChuKuTongJi.getValue()).getLastupdatetime());
		return "datastatistics/kdkoutwarehousedatalist";
	}

	/**
	 * 退货出站统计 （云）
	 *
	 * @param model
	 * @param page
	 * @param begindate
	 * @param enddate
	 * @param branchids
	 * @param customerids
	 * @param istuihuozhanruku
	 * @param isshow
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/tuihuochuzhanlist/{page}")
	public String tuihuochuzhanlist(Model model, @PathVariable(value = "page") long page, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate,
			@RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "branchid", required = false, defaultValue = "") String[] branchids,
			@RequestParam(value = "customerid", required = false, defaultValue = "") String[] customerids,
			@RequestParam(value = "istuihuozhanruku", required = false, defaultValue = "0") long istuihuozhanruku, @RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow,
			HttpServletResponse response, HttpServletRequest request) {
		try {
			long count = 0;
			Page pageparm = new Page();
			String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
			User user = this.getDmpDAO.getLogUser(dmpid);
			// 加载供货商
			List<Customer> customerlist = this.getDmpDAO.getAllCustomers();
			// 加载导出模板
			List<Exportmould> exportmouldlist = this.getDmpDAO.getExportmoulds(user, dmpid);
			// 需要返回页面的前10条订单List
			List<CwbOrder> orderlist = new ArrayList<CwbOrder>();

			// 加载站点区域权限
			List<Branch> branchnameList = new ArrayList<Branch>();
			if ((user != null) && (user.getUserid() > 0)) {
				Branch branch = this.getDmpDAO.getNowBranch(user.getBranchid());
				branchnameList = this.getDmpDAO.getQueryBranchByBranchsiteAndUserid(user.getUserid(), BranchEnum.ZhanDian.getValue() + "");
				if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
					if (branchnameList.size() == 0) {
						branchnameList.add(branch);
					} else {
						if (!this.dataStatisticService.checkBranchRepeat(branchnameList, branch)) {
							branchnameList.add(branch);
						}
					}
				}
			}
			if ((user == null) || (user.getUserid() == 0)) {// 如果登录失效，提示登录失败
				model.addAttribute("nouser", "nouser");
			} else if (isshow == 1) {
				this.logger.info("退货出站统计(云)，操作人{}，选择条件 begindate:" + begindate + ",enddate:" + enddate + ",branchids:" + this.dataStatisticService.getStrings(branchids) + ",istuihuozhanruku:"
						+ istuihuozhanruku + ",customerids:" + this.dataStatisticService.getStrings(customerids) + ",isshow:" + isshow + ",page:" + page, user.getRealname());
				begindate = begindate.length() == 0 ? DateTimeUtil.getNowTime() : begindate;
				enddate = enddate.length() == 0 ? DateTimeUtil.getNowTime() : enddate;
				String branchstr = this.dataStatisticService.getStrings(branchids);
				String customerstr = this.dataStatisticService.getStrings(customerids);
				List<TuiHuoChuZhanOrder> tlist = this.tuiHuoChuZhanDao.getTuiHuoChuZhanList(begindate, enddate, branchstr, customerstr, istuihuozhanruku, page);
				count = this.tuiHuoChuZhanDao.getCountTuihuoChuZhan(begindate, enddate, branchstr, customerstr, istuihuozhanruku, 0);
				pageparm = new Page(count, page, Page.ONE_PAGE_NUMBER);
				if ((tlist != null) && (tlist.size() > 0)) {
					String cwbs = "";
					for (TuiHuoChuZhanOrder to : tlist) {
						cwbs += "'" + to.getCwb() + "',";
					}
					cwbs = cwbs.length() > 0 ? cwbs.substring(0, cwbs.length() - 1) : "";
					if (cwbs.length() > 0) {
						List<Branch> branchList = this.getDmpDAO.getAllBranchs();
						orderlist = this.cwbDAO.getCwbOrderByCwbs(cwbs);
						orderlist = this.dataStatisticService.getTuiHuoChuZhanCwbOrderViewCount10(orderlist, tlist, customerlist, branchList);
					}
				}
				this.logger.info("退货出站统计(云)，当前操作人{},条数{}", user.getRealname(), count);
				String dmpURl = this.getDmpDAO.getDmpurl().substring(this.getDmpDAO.getDmpurl().lastIndexOf("/"), this.getDmpDAO.getDmpurl().length());
				model.addAttribute("dmpUrl", dmpURl);
			}

			List<String> branchidlist = this.dataStatisticService.getList(branchids);
			List<String> customeridList = this.dataStatisticService.getList(customerids);
			model.addAttribute("branchnameList", branchnameList);
			model.addAttribute("customerList", customerlist);
			model.addAttribute("exportmouldlist", exportmouldlist);
			model.addAttribute("branchidStr", branchidlist);
			model.addAttribute("customeridStr", customeridList);
			model.addAttribute("count", count);
			model.addAttribute("orderlist", orderlist);
			model.addAttribute("page_obj", pageparm);
			model.addAttribute("page", page);

		} catch (Exception e) {
			this.logger.error("退货出站统计出错(云)", e);
		}
		return "datastatistics/tuihuochuzhanlist";

	}

	// 退货站入库统计 云
	@RequestMapping("/tuihuozhanrukulist/{page}")
	public String tuihuozhanrukulist(Model model, @PathVariable(value = "page") long page, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate,
			@RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "branchid", required = false, defaultValue = "") String[] branchids,
			@RequestParam(value = "customerid", required = false, defaultValue = "") String[] customerids,
			@RequestParam(value = "cwbordertypeid", required = false, defaultValue = "") String[] cwbordertypeids, @RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow,
			HttpServletResponse response, HttpServletRequest request) {
		try {
			long count = 0;
			Page pageparm = new Page();
			String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
			User user = this.getDmpDAO.getLogUser(dmpid);
			List<CwbOrder> orderlist = new ArrayList<CwbOrder>();
			// 加载站点区域权限
			List<Branch> branchnameList = new ArrayList<Branch>();
			if ((user != null) && (user.getUserid() > 0)) {
				Branch branch = this.getDmpDAO.getNowBranch(user.getBranchid());
				branchnameList = this.getDmpDAO.getQueryBranchByBranchsiteAndUserid(user.getUserid(), BranchEnum.ZhanDian.getValue() + "");
				if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
					if (branchnameList.size() == 0) {
						branchnameList.add(branch);
					} else {
						if (!this.dataStatisticService.checkBranchRepeat(branchnameList, branch)) {
							branchnameList.add(branch);
						}
					}
				}
			}
			model.addAttribute("branchnameList", branchnameList);
			// 加载供货商
			List<Customer> customerList = this.getDmpDAO.getAllCustomers();
			if ((user == null) || (user.getUserid() == 0)) {// 如果登录失效，提示登录失败
				model.addAttribute("nouser", "nouser");
			} else if (isshow != 0) {
				this.logger.info("退货站入库统计（云），操作人{}，选择条件 begindate:" + begindate + ",enddate:" + enddate + ",branchids:" + this.dataStatisticService.getStrings(branchids) + ",cwbordertypeids:"
						+ this.dataStatisticService.getStrings(cwbordertypeids) + ",customerids:" + this.dataStatisticService.getStrings(customerids) + ",isshow:" + isshow + ",page:" + page,
						user.getRealname());

				begindate = begindate.length() == 0 ? DateTimeUtil.getNowTime() : begindate;
				enddate = enddate.length() == 0 ? DateTimeUtil.getNowTime() : enddate;
				// 定义参数
				List<TuiHuoZhanRuKuOrder> tuihuoRecordList = this.tuiHuoZhanRuKuDao.getTuiHuoRecordByTuihuozhanruku(begindate, enddate, this.dataStatisticService.getStrings(branchids),
						this.dataStatisticService.getStrings(customerids), this.dataStatisticService.getStrings(cwbordertypeids), page);
				count = this.tuiHuoZhanRuKuDao.getTuiHuoRecordByTuihuozhanrukuCount(begindate, enddate, this.dataStatisticService.getStrings(branchids),
						this.dataStatisticService.getStrings(customerids), this.dataStatisticService.getStrings(cwbordertypeids));
				pageparm = new Page(count, page, Page.ONE_PAGE_NUMBER);
				if ((tuihuoRecordList != null) && (tuihuoRecordList.size() > 0)) {
					String cwbs = "";
					for (TuiHuoZhanRuKuOrder to : tuihuoRecordList) {
						cwbs += "'" + to.getCwb() + "',";
					}
					cwbs = cwbs.length() > 0 ? cwbs.substring(0, cwbs.length() - 1) : "";
					if (cwbs.length() > 0) {
						List<Branch> branchList = this.getDmpDAO.getAllBranchs();
						orderlist = this.cwbDAO.getCwbOrderByCwbs(cwbs);
						orderlist = this.dataStatisticService.getTuiHuoZhanRuKuIndex(orderlist, tuihuoRecordList, customerList, branchList);
					}
				}
			}

			List<String> branchidlist = this.dataStatisticService.getList(branchids);
			List<String> customeridList = this.dataStatisticService.getList(customerids);
			List<String> cwbordertypeidList = this.dataStatisticService.getList(cwbordertypeids);
			model.addAttribute("customerList", customerList);
			model.addAttribute("exportmouldlist", this.getDmpDAO.getExportmoulds(user, dmpid));
			model.addAttribute("branchidStr", branchidlist);
			model.addAttribute("customeridStr", customeridList);
			model.addAttribute("cwbordertypeidStr", cwbordertypeidList);
			model.addAttribute("count", count);
			model.addAttribute("orderlist", orderlist);
			model.addAttribute("page_obj", pageparm);
			model.addAttribute("page", page);
			String dmpURl = this.getDmpDAO.getDmpurl().substring(this.getDmpDAO.getDmpurl().lastIndexOf("/"), this.getDmpDAO.getDmpurl().length());
			model.addAttribute("dmpUrl", dmpURl);
			model.addAttribute("lastupdatetime", this.updateMessageDAO.getUpdateMessageByMenunvalue(UpdateMessageMenuNameEnum.TuiHuoZhanRuKuTongJi.getValue()).getLastupdatetime());
			this.logger.info("退货站入库统计(云)，当前操作人{},条数{}", user.getRealname(), count);
		} catch (Exception e) {
			this.logger.error("退货站入库统计出错", e);
		}
		return "datastatistics/tuihuozhanrukulist";
	}

	@RequestMapping("/zaitusearch/{page}")
	public String zaitusearch(Model model, @RequestParam(value = "datetype", required = false, defaultValue = "1") long datetype,
			@RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate,
			@RequestParam(value = "kufangid", required = false, defaultValue = "") String[] kufangid, @RequestParam(value = "nextbranchid", required = false, defaultValue = "") String[] nextbranchid,
			@RequestParam(value = "cwbordertypeid", required = false, defaultValue = "") String[] cwbordertypeid,
			@RequestParam(value = "orderbyName", required = false, defaultValue = "emaildate") String orderbyName,
			@RequestParam(value = "orderbyType", required = false, defaultValue = "DESC") String orderbyId, @RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow,
			@PathVariable(value = "page") long page, HttpServletResponse response, HttpServletRequest request) {
		try {
			long count = 0;
			Page pageparm = new Page();
			String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
			User user = this.getDmpDAO.getLogUser(dmpid);
			List<Branch> branchList = this.getDmpDAO.getAllBranchs();
			List<CwbOrder> orderlist = new ArrayList<CwbOrder>();
			KuFangZaiTuOrder sum = new KuFangZaiTuOrder();

			List<String> nextbranchidlist = this.dataStatisticService.getList(nextbranchid);
			List<String> kufanglist = this.dataStatisticService.getList(kufangid);
			List<String> cwbordertypelist = this.dataStatisticService.getList(cwbordertypeid);
			List<Branch> kufangList = this.getDmpDAO.getQueryBranchByBranchsiteAndUserid(user.getUserid(), BranchEnum.KuFang.getValue() + "," + BranchEnum.TuiHuo.getValue() + ","
					+ BranchEnum.ZhongZhuan.getValue());

			model.addAttribute("exportmouldlist", this.getDmpDAO.getExportmoulds(user, dmpid));
			model.addAttribute("nextbranchidStr", nextbranchidlist);
			model.addAttribute("kufangidStr", kufanglist);
			model.addAttribute("cwbordertypeidStr", cwbordertypelist);
			model.addAttribute("kufangList", kufangList);
			if ((user == null) || (user.getUserid() == 0)) {// 如果登录失效，提示登录失败
				model.addAttribute("nouser", "nouser");
			} else if ((isshow != 0) && (kufangid.length > 0) && (nextbranchid.length > 0)) {
				this.logger.info("库房在途订单汇总(云)，操作人{}，选择条件datetype:" + datetype + "begindate:" + begindate + ",enddate:" + enddate + ",kufangid:" + this.dataStatisticService.getStrings(kufangid)
						+ ",nextbranchid:" + this.dataStatisticService.getStrings(nextbranchid) + ",cwbordertypeid:" + this.dataStatisticService.getStrings(cwbordertypeid) + ",orderbyName:"
						+ orderbyName + ",orderbyId:" + orderbyId + ",isshow:" + isshow + ",page:" + page, user.getRealname());
				begindate = begindate.length() == 0 ? DateTimeUtil.getNowTime() : begindate;
				enddate = enddate.length() == 0 ? DateTimeUtil.getNowTime() : enddate;
				// 定义参数
				String orderName = " " + orderbyName + " " + orderbyId;
				String cwbordertypeids = this.dataStatisticService.getStrings(cwbordertypeid);
				String nextbranchids = this.dataStatisticService.getStrings(nextbranchid);
				String kufangids = this.dataStatisticService.getStrings(kufangid);

				// 获取值
				List<KuFangZaiTuOrder> zaiTuOrders = this.kufangZaiTuDao.getKuFangZaiTu(page, begindate, enddate, kufangids, nextbranchids, cwbordertypeids, datetype, orderName);
				sum = this.kufangZaiTuDao.getKuFangZaiTuCount(begindate, enddate, kufangids, nextbranchids, cwbordertypeids, datetype);
				count = sum.getId();
				List<Customer> customerList = this.getDmpDAO.getAllCustomers();
				List<CustomWareHouse> customerWareHouseList = this.getDmpDAO.getCustomWareHouse();
				List<User> userList = this.getDmpDAO.getUserForALL();
				List<Reason> reasonList = this.getDmpDAO.getAllReason();
				List<Remark> remarkList = this.getDmpDAO.getAllRemark();

				pageparm = new Page(count, page, Page.ONE_PAGE_NUMBER);
				if ((zaiTuOrders != null) && (zaiTuOrders.size() > 0)) {
					String cwbs = "";
					for (KuFangZaiTuOrder to : zaiTuOrders) {
						cwbs += "'" + to.getCwb() + "',";
					}
					cwbs = cwbs.length() > 0 ? cwbs.substring(0, cwbs.length() - 1) : "";
					if (cwbs.length() > 0) {
						orderlist = this.cwbDAO.getCwbOrderByCwbs(cwbs);
						orderlist = this.dataStatisticService.getKuFangZaiTuHuiZongViewIndex(zaiTuOrders, orderlist, customerList, customerWareHouseList, branchList, userList, reasonList, remarkList);
					}
				}
			}
			model.addAttribute("branchAllList", this.getDmpDAO.getBranchListByUser(user.getUserid()));
			model.addAttribute("count", count);
			model.addAttribute("sum", sum.getReceivablefee());
			model.addAttribute("paybackfeesum", sum.getPaybackfee());
			model.addAttribute("orderlist", orderlist);
			model.addAttribute("page_obj", pageparm);
			model.addAttribute("page", page);
			String dmpURl = this.getDmpDAO.getDmpurl().substring(this.getDmpDAO.getDmpurl().lastIndexOf("/"), this.getDmpDAO.getDmpurl().length());
			model.addAttribute("dmpUrl", dmpURl);
			model.addAttribute("lastupdatetime", this.updateMessageDAO.getUpdateMessageByMenunvalue(UpdateMessageMenuNameEnum.KuFangZaiTuTongJi.getValue()).getLastupdatetime());
			this.logger.info("库房在途订单汇总(云)，当前操作人{},条数{}", user.getRealname(), count);
		} catch (Exception e) {
			this.logger.error("库房在途订单汇总(云)", e);
		}

		return "datastatistics/zaitulist";
	}

	/**
	 * 库房入库统计(云)
	 *
	 * @param model
	 * @param page
	 * @param emaildateids
	 * @param begindate
	 * @param enddate
	 * @param kufangid
	 * @param customerids
	 * @param cwbordertypeid
	 * @param isruku
	 * @param isshow
	 * @param response
	 * @param request
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@RequestMapping("/intowarehousedata/{page}")
	public String intowarehousedata(Model model, @PathVariable(value = "page") long page, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate,
			@RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "emaildatebegin", required = false, defaultValue = "") String emaildatebegin,
			@RequestParam(value = "emaildateend", required = false, defaultValue = "") String emaildateend, @RequestParam(value = "kufangid", required = false, defaultValue = "0") long kufangid,
			@RequestParam(value = "customerid", required = false, defaultValue = "") String[] customerids,
			@RequestParam(value = "cwbordertypeid", required = false, defaultValue = "-2") long cwbordertypeid,
			@RequestParam(value = "isruku", required = false, defaultValue = "false") String isruku, @RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow,
			HttpServletResponse response, HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException {
		try {
			long count = 0;
			Page pageparm = new Page();
			String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
			User user = this.getDmpDAO.getLogUser(dmpid);
			/*
			 * String starttime=""; String endtime="";
			 */
			/* long rukuflag=1;//已入库为1 */
			if ("false".equals(isruku)) {
				emaildatebegin = emaildatebegin.length() == 0 ? DateTimeUtil.getNowDate() + " 00:00:00" : emaildatebegin;
				emaildateend = emaildateend.length() == 0 ? DateTimeUtil.getNowTime() : emaildateend;

			} else {
				begindate = begindate.length() == 0 ? DateTimeUtil.getNowDate() + " 00:00:00" : begindate;
				enddate = enddate.length() == 0 ? DateTimeUtil.getNowTime() : enddate;
			}
			List<CwbOrder> orderlist = new ArrayList<CwbOrder>();
			Branch branch = this.getDmpDAO.getNowBranch(user.getBranchid());// 注意
			List<Branch> kufangList = this.getDmpDAO.getQueryBranchByBranchsiteAndUserid(user.getUserid(), BranchEnum.KuFang.getValue() + "," + BranchEnum.TuiHuo.getValue() + ","
					+ BranchEnum.ZhongZhuan.getValue());
			if ((branch.getSitetype() == BranchEnum.KuFang.getValue()) || (branch.getSitetype() == BranchEnum.TuiHuo.getValue()) || (branch.getSitetype() == BranchEnum.ZhongZhuan.getValue())) {
				if (kufangList.size() == 0) {
					kufangList.add(branch);
				} else {
					if (!this.dataStatisticService.checkBranchRepeat(kufangList, branch)) {
						kufangList.add(branch);
					}
				}
			}
			List<User> userList = this.getDmpDAO.getAllUsers();
			List<Customer> customerList = this.getDmpDAO.getAllCustomers();
			model.addAttribute("customerlist", customerList);
			model.addAttribute("exportmouldlist", this.getDmpDAO.getExportmoulds(user, dmpid));
			model.addAttribute("kufangList", kufangList);
			List<String> customeridList = this.dataStatisticService.getList(customerids);
			model.addAttribute("customeridStr", customeridList);
			if ((user == null) || (user.getUserid() == 0)) {// 如果登录失效，提示登录失败
				model.addAttribute("nouser", "nouser");
			} else if (isshow != 0) {
				this.logger.info("库房入库统计(云)，操作人{}，选择条件 begindate:" + begindate + ",enddate:" + enddate + ",kufangid:" + kufangid + ",cwbordertypeid:" + cwbordertypeid + ",isruku:" + isruku
						+ ",customerids:" + this.dataStatisticService.getStrings(customerids) + ",isshow:" + isshow + ",page:" + page, user.getRealname());

				String customers = "";
				if (customerids.length > 0) {
					customers = this.dataStatisticService.getStrings(customerids);
				}

				List<KuFangRuKuOrder> kuFangRuKuOrders = this.kuFangRuKuDao.getKuKangRuKuOrders(page, begindate, enddate, emaildatebegin, emaildateend, kufangid, customers, cwbordertypeid, isruku);
				count = this.kuFangRuKuDao.getKuKangRuKuOrdersCount(begindate, enddate, emaildatebegin, emaildateend, kufangid, customers, cwbordertypeid, isruku);
				// 查询数据
				pageparm = new Page(count, page, Page.ONE_PAGE_NUMBER);

				if ((kuFangRuKuOrders != null) && (kuFangRuKuOrders.size() > 0)) {
					String cwbs = "";
					for (KuFangRuKuOrder kf : kuFangRuKuOrders) {
						cwbs += "'" + kf.getCwb() + "',";
					}
					cwbs = cwbs.length() > 0 ? cwbs.substring(0, cwbs.length() - 1) : "";
					if (cwbs.length() > 0) {
						orderlist = this.cwbDAO.getCwbOrderByCwbs(cwbs);
						List<Branch> branchs = this.getDmpDAO.getAllBranchs();
						orderlist = this.dataStatisticService.getKuFangRuKuHuiZongViewIndex(kuFangRuKuOrders, orderlist, customerList, branchs, userList);
					}
				}

			}
			List<Customer> customerMarkList = new ArrayList<Customer>();
			if (customerids.length > 0) {
				customerMarkList = this.getDmpDAO.getCustomerByIds(this.dataStatisticService.getStrings(customerids));
			}
			model.addAttribute("customerMarkList", customerMarkList);
			model.addAttribute("count", count);
			model.addAttribute("orderlist", orderlist);
			model.addAttribute("page_obj", pageparm);
			model.addAttribute("page", page);
			String dmpURl = this.getDmpDAO.getDmpurl().substring(this.getDmpDAO.getDmpurl().lastIndexOf("/"), this.getDmpDAO.getDmpurl().length());
			model.addAttribute("dmpUrl", dmpURl);
			model.addAttribute("lastupdatetime", this.updateMessageDAO.getUpdateMessageByMenunvalue(UpdateMessageMenuNameEnum.KuFangRuKuTongJi.getValue()).getLastupdatetime());
			this.logger.info("库房入库统计(云)，当前操作人{},条数{}", user.getRealname(), count);
		} catch (Exception e) {
			this.logger.error("库房入库统计(云)出错", e);
		}
		return "datastatistics/intowarehousedatalist";
	}

	/**
	 * 库房出库汇总（云）
	 *
	 * @param model
	 * @param begindate
	 * @param enddate
	 * @param kufangid
	 * @param isshow
	 * @param response
	 * @param request
	 * @return
	 */
	/*
	 * @RequestMapping("/outwarehousecollectdata") public String
	 * outwarehousecollectdata(Model model,
	 *
	 * @RequestParam(value = "begindate", required = false, defaultValue = "")
	 * String begindate,
	 *
	 * @RequestParam(value = "enddate", required = false, defaultValue = "")
	 * String enddate,
	 *
	 * @RequestParam(value = "kufangid", required = false, defaultValue = "0")
	 * long kufangid,
	 *
	 * @RequestParam(value = "isshow", required = false, defaultValue = "0")
	 * long isshow, HttpServletResponse response, HttpServletRequest request) {
	 *
	 * String dmpid = request.getSession().getAttribute("dmpid") == null ? "" :
	 * request.getSession().getAttribute("dmpid").toString(); User user =
	 * getDmpDAO.getLogUser(dmpid); //加载供货商 List<Customer> customerlist =
	 * getDmpDAO.getAllCustomers(); //得到供货商对应的站点的map Map<String, Long>
	 * customerMap = new HashMap<String, Long>(); //得到供货商所有的站点的map Map<Long,
	 * Long> customerAllMap = new HashMap<Long, Long>(); //得到对应的站点所有供货商的map
	 * Map<Long, Long> branchAllMap = new HashMap<Long, Long>(); //加载站点区域权限
	 * List<Branch> branchAllList = new ArrayList<Branch>(); List<Branch>
	 * kufangList = new ArrayList<Branch>(); String nextbranchids = ""; if(user
	 * != null && user.getUserid()>0){ Branch branch =
	 * getDmpDAO.getNowBranch(user.getBranchid()); branchAllList =
	 * getDmpDAO.getQueryBranchByBranchsiteAndUserid
	 * (user.getUserid(),BranchEnum.ZhanDian.getValue()+"");
	 *
	 * for(Branch b : branchAllList){ nextbranchids += b.getBranchid()+","; }
	 *
	 * kufangList =
	 * getDmpDAO.getQueryBranchByBranchsiteAndUserid(user.getUserid(
	 * ),BranchEnum.KuFang.getValue()+"");
	 * if(branch.getSitetype()==BranchEnum.KuFang.getValue()){
	 * if(kufangList.size()==0){ kufangList.add(branch); }else{
	 * if(!dataStatisticService.checkBranchRepeat(kufangList, branch)){
	 * kufangList.add(branch); } } } } //加载导出模板 List<Exportmould>
	 * exportmouldlist = getDmpDAO.getExportmoulds(user,dmpid);
	 *
	 * if(user == null || user.getUserid()==0 ){//如果登录失效，提示登录失败
	 * model.addAttribute("nouser", "nouser"); }else
	 * if(isshow==1){//如果是点击查询按钮，封装查询数据
	 * logger.info("库房出库汇总(云)，操作人{}，选择条件 begindate:"
	 * +begindate+",enddate:"+enddate+ ",kufangid:"+kufangid+",isshow:"+isshow,
	 * user.getRealname()); begindate =
	 * begindate.length()==0?DateTimeUtil.getNowTime():begindate; enddate =
	 * enddate.length()==0?DateTimeUtil.getNowTime():enddate; nextbranchids =
	 * nextbranchids
	 * .length()>0?nextbranchids.substring(0,nextbranchids.length()-1):"";
	 * customerMap = dataStatisticService.getCustomerMap(customerlist,
	 * branchAllList, kufangid, begindate, enddate); customerAllMap =
	 * dataStatisticService.getcustomerAllMap(customerlist, kufangid, begindate,
	 * enddate,nextbranchids); branchAllMap =
	 * dataStatisticService.getbranchAllMap(branchAllList, kufangid, begindate,
	 * enddate); }
	 *
	 * model.addAttribute("customerMap", customerMap);
	 * model.addAttribute("customerAllMap", customerAllMap);
	 * model.addAttribute("branchAllMap", branchAllMap);
	 *
	 * model.addAttribute("branchList", branchAllList);
	 * model.addAttribute("customerlist", customerlist);
	 * model.addAttribute("exportmouldlist", exportmouldlist);
	 * model.addAttribute("kufangList", kufangList);
	 *
	 * model.addAttribute("lastupdatetime",
	 * updateMessageDAO.getUpdateMessageByMenunvalue
	 * (UpdateMessageMenuNameEnum.KuFangChuKuTongJi
	 * .getValue()).getLastupdatetime()); return
	 * "datastatistics/outwarehousecollectdata"; }
	 */

	/**
	 * 库房出库汇总详情功能
	 *
	 * @param model
	 * @param page
	 * @param customerid
	 * @param begindate
	 * @param enddate
	 * @param kufangid
	 * @param nextbranchid
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/outwarehousecollectdatashow/{page}")
	public String outwarehousecollectdatashow(Model model, @PathVariable("page") long page, @RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid,
			@RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate,
			@RequestParam(value = "kufangid", required = false, defaultValue = "0") long kufangid, @RequestParam(value = "nextbranchid", required = false, defaultValue = "0") long nextbranchid,
			HttpServletResponse response, HttpServletRequest request) {
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = this.getDmpDAO.getLogUser(dmpid);
		// 需要返回页面的前10条订单List
		List<CwbOrder> orderlist = new ArrayList<CwbOrder>();
		// 加载导出模板
		List<Exportmould> exportmouldlist = this.getDmpDAO.getExportmoulds(user, dmpid);
		List<Customer> customerList = this.getDmpDAO.getAllCustomers();
		List<Branch> branchList = this.getDmpDAO.getAllBranchs();
		List<CustomWareHouse> customerWareHouseList = this.getDmpDAO.getCustomWareHouse();

		List<DeliveryChuku> delList = this.deliveryChukuDAO.getDeliveryChukuCollectList(page, customerid, kufangid, nextbranchid, begindate, enddate);

		DeliveryChuku deliveryChukusum = this.deliveryChukuDAO.getDeliveryChukuCollectSum(customerid, kufangid, nextbranchid, begindate, enddate);
		long count = deliveryChukusum.getId();
		Page pageparm = new Page(count, page, Page.ONE_PAGE_NUMBER);
		if ((delList != null) && (delList.size() > 0)) {
			String cwbs = "";
			for (DeliveryChuku deliveryChuku : delList) {
				cwbs += "'" + deliveryChuku.getCwb() + "',";
			}

			cwbs = cwbs.length() > 0 ? cwbs.substring(0, cwbs.length() - 1) : "";
			if (cwbs.length() > 0) {
				orderlist = this.cwbDAO.getCwbOrderByCwbs(cwbs);
				orderlist = this.dataStatisticService.getChukuCollectDataCwbOrderViewCount10(orderlist, delList, customerList, branchList, customerWareHouseList);
			}
			String dmpURl = this.getDmpDAO.getDmpurl().substring(this.getDmpDAO.getDmpurl().lastIndexOf("/"), this.getDmpDAO.getDmpurl().length());
			model.addAttribute("dmpUrl", dmpURl);
		}

		model.addAttribute("orderlist", orderlist);
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);
		model.addAttribute("count", count);
		model.addAttribute("deliveryChukusum", deliveryChukusum);
		model.addAttribute("exportmouldlist", exportmouldlist);
		return "datastatistics/outwarehousecollectdatashow";
	}

	@RequestMapping("/zhiliusearch/{page}")
	public String zhiliusearch(Model model, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate,
			@RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "isaudit", required = false, defaultValue = "-1") long isaudit,
			@RequestParam(value = "isauditTime", required = false, defaultValue = "0") long isauditTime, @RequestParam(value = "customerid", required = false, defaultValue = "") String[] customerid,
			@RequestParam(value = "dispatchbranchid", required = false, defaultValue = "") String[] dispatchbranchid,
			@RequestParam(value = "deliverid", required = false, defaultValue = "-1") long deliverid,
			@RequestParam(value = "cwbordertypeid", required = false, defaultValue = "") String[] cwbordertypeid, @RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow,
			@PathVariable(value = "page") long page, HttpServletResponse response, HttpServletRequest request) {
		long count = 0;
		Page pageparm = new Page();
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = this.getDmpDAO.getLogUser(dmpid);
		// 应收费 滞留
		DeliveryZhiLiu sum = new DeliveryZhiLiu();
		// 加载供货商
		List<Customer> customerlist = this.getDmpDAO.getAllCustomers();
		// 加载导出模板
		List<Exportmould> exportmouldlist = this.getDmpDAO.getExportmoulds(user, dmpid);
		// 需要返回页面的前10条订单List
		List<CwbOrder> orderlist = new ArrayList<CwbOrder>();
		// 加载站点区域权限
		List<Branch> branchnameList = new ArrayList<Branch>();
		// 保存订单配送结果的选择
		// String[] operationOrderResultTypes =
		// {DeliveryStateEnum.FenZhanZhiLiu.getValue()+""};
		// List<String> operationOrderResultTypeslist =
		// dataStatisticService.getList(operationOrderResultTypes);
		// 保存供货商的选择
		List<String> customeridList = new ArrayList<String>();
		// 保存站点的选择
		List<String> dispatchbranchidList = new ArrayList<String>();
		// 保存订单类型的选择
		List<String> cwbordertypeidList = new ArrayList<String>();
		// 保存小件员
		List<User> deliverlist = new ArrayList<User>();

		if ((user != null) && (user.getUserid() > 0)) {
			Branch branch = this.getDmpDAO.getNowBranch(user.getBranchid());
			branchnameList = this.getDmpDAO.getQueryBranchByBranchsiteAndUserid(user.getUserid(),
					BranchEnum.ZhanDian.getValue() + "," + BranchEnum.TuiHuo.getValue() + "," + BranchEnum.ZhongZhuan.getValue());
			if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
				if (branchnameList.size() == 0) {
					branchnameList.add(branch);
				} else {
					if (!this.dataStatisticService.checkBranchRepeat(branchnameList, branch)) {
						branchnameList.add(branch);
					}
				}
			}
		}
		if ((user == null) || (user.getUserid() == 0)) {// 如果登录失效，提示登录失败
			model.addAttribute("nouser", "nouser");
		} else if (isshow == 1) {
			this.logger.info("滞留订单汇总(云)，操作人{}，选择条件 begindate:" + begindate + ",enddate:" + enddate + ",customerid:" + this.dataStatisticService.getStrings(customerid) + ",cwbordertypeid:"
					+ this.dataStatisticService.getStrings(cwbordertypeid) + ",isaudit" + isaudit + ",isauditTime" + isauditTime + ",dispatchbranchid" + dispatchbranchid + ",deliverid" + deliverid
					+ ",isshow:" + isshow + ",page:" + page, user.getRealname());
			// 保存供货商的选择
			customeridList = this.dataStatisticService.getList(customerid);
			// 保存站点的选择
			if ((dispatchbranchid.length == 0) & (branchnameList.size() > 0)) {
				dispatchbranchid = new String[branchnameList.size()];
				for (Branch bc : branchnameList) {
					dispatchbranchid[branchnameList.indexOf(bc)] = bc.getBranchid() + "";
				}
			}
			dispatchbranchidList = this.dataStatisticService.getList(dispatchbranchid);
			// 保存订单类型的选择
			cwbordertypeidList = this.dataStatisticService.getList(cwbordertypeid);
			// 保存小件员的选择
			deliverlist = this.getDmpDAO.getAllUserByBranchIds(this.dataStatisticService.getStrings(dispatchbranchid));
			String customeridStr = this.dataStatisticService.getStrings(customerid);
			String cwbordertypeidStr = this.dataStatisticService.getStrings(cwbordertypeid);
			String dispatchbranchidStr = this.dataStatisticService.getStrings(dispatchbranchid);
			List<DeliveryZhiLiu> dzlList = this.deliveryZhiLiuDAO.getZhiliuList(begindate, enddate, isaudit, isauditTime, customeridStr, cwbordertypeidStr, dispatchbranchidStr, deliverid, page);

			sum = this.deliveryZhiLiuDAO.getDeliveryZhiLiuSum(begindate, enddate, isaudit, isauditTime, customeridStr, cwbordertypeidStr, dispatchbranchidStr, deliverid);
			count = sum.getId();
			pageparm = new Page(count, page, Page.ONE_PAGE_NUMBER);
			if ((dzlList != null) && (dzlList.size() > 0)) {
				String cwbs = "";
				for (DeliveryZhiLiu deliveryZhiLiu : dzlList) {
					cwbs += "'" + deliveryZhiLiu.getCwb() + "',";
				}
				cwbs = cwbs.length() > 0 ? cwbs.substring(0, cwbs.length() - 1) : "";
				if (cwbs.length() > 0) {
					List<Branch> branchList = this.getDmpDAO.getAllBranchs();
					List<Reason> reasonList = this.getDmpDAO.getAllReason();
					orderlist = this.cwbDAO.getCwbOrderByCwbs(cwbs);
					orderlist = this.dataStatisticService.getZhiLiuCwbOrderViewCount10(orderlist, dzlList, customerlist, branchList, reasonList);
				}
			}
			String dmpURl = this.getDmpDAO.getDmpurl().substring(this.getDmpDAO.getDmpurl().lastIndexOf("/"), this.getDmpDAO.getDmpurl().length());
			model.addAttribute("dmpUrl", dmpURl);
			this.logger.info("滞留订单汇总(云)，当前操作人{},条数{}", user.getRealname(), count);
		}
		model.addAttribute("branchList", branchnameList);
		model.addAttribute("customerlist", customerlist);
		model.addAttribute("exportmouldlist", exportmouldlist);
		model.addAttribute("dispatchbranchidStr", dispatchbranchidList);
		model.addAttribute("cwbordertypeidStr", cwbordertypeidList);
		model.addAttribute("customeridStr", customeridList);
		model.addAttribute("deliverlist", deliverlist);
		model.addAttribute("count", count);
		model.addAttribute("sum", sum.getReceivablefee());
		model.addAttribute("paybackfeesum", sum.getPaybackfee());
		model.addAttribute("orderlist", orderlist);
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);
		model.addAttribute("check", 1);
		model.addAttribute("deliverid", deliverid);
		model.addAttribute("lastupdatetime", this.updateMessageDAO.getUpdateMessageByMenunvalue(UpdateMessageMenuNameEnum.ZhiLiuDingDanHuiZong.getValue()).getLastupdatetime());

		return "datastatistics/zhiliulist";
	}

	/**
	 * 拒收订单汇总
	 */
	@RequestMapping("/jushousearch/{page}")
	public String jushousearch(Model model, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate,
			@RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "isaudit", required = false, defaultValue = "-1") long isaudit,
			@RequestParam(value = "isauditTime", required = false, defaultValue = "0") long isauditTime, @RequestParam(value = "customerid", required = false, defaultValue = "") String[] customerid,
			@RequestParam(value = "dispatchbranchid", required = false, defaultValue = "") String[] dispatchbranchid,
			@RequestParam(value = "deliverid", required = false, defaultValue = "-1") long deliverid,
			@RequestParam(value = "cwbordertypeid", required = false, defaultValue = "") String[] cwbordertypeid, @RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow,
			@RequestParam(value = "operationOrderResultType", required = false, defaultValue = "") String[] operationOrderResultTypes, @PathVariable(value = "page") long page,
			HttpServletResponse response, HttpServletRequest request) {
		long count = 0;
		Page pageparm = new Page();
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = this.getDmpDAO.getLogUser(dmpid);
		// 应收费 deliveryjushou 拒收
		DeliveryJuShou sum = new DeliveryJuShou();
		// 加载供货商
		List<Customer> customerlist = this.getDmpDAO.getAllCustomers();
		// 加载导出模板
		List<Exportmould> exportmouldlist = this.getDmpDAO.getExportmoulds(user, dmpid);
		// 需要返回页面的前10条订单List
		List<CwbOrder> orderlist = new ArrayList<CwbOrder>();
		// 加载站点区域权限
		List<Branch> branchnameList = new ArrayList<Branch>();
		// 保存操作结果
		List<String> operationOrderResultTypeslist = this.dataStatisticService.getList(operationOrderResultTypes);
		// 保存供货商的选择
		List<String> customeridList = new ArrayList<String>();
		// 保存站点的选择
		List<String> dispatchbranchidList = new ArrayList<String>();
		// 保存订单类型的选择
		List<String> cwbordertypeidList = new ArrayList<String>();
		// 保存小件员
		List<User> deliverlist = new ArrayList<User>();

		if ((user != null) && (user.getUserid() > 0)) {
			Branch branch = this.getDmpDAO.getNowBranch(user.getBranchid());
			branchnameList = this.getDmpDAO.getQueryBranchByBranchsiteAndUserid(user.getUserid(), BranchEnum.ZhanDian.getValue() + "");
			if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
				if (branchnameList.size() == 0) {
					branchnameList.add(branch);
				} else {
					if (!this.dataStatisticService.checkBranchRepeat(branchnameList, branch)) {
						branchnameList.add(branch);
					}
				}
			}
		}
		if ((user == null) || (user.getUserid() == 0)) {// 如果登录失效，提示登录失败
			model.addAttribute("nouser", "nouser");
		} else if (isshow == 1) {
			this.logger.info("拒收订单汇总(云)，操作人{}，选择条件 begindate:" + begindate + ",enddate:" + enddate + ",customerid:" + this.dataStatisticService.getStrings(customerid) + ",cwbordertypeid:"
					+ this.dataStatisticService.getStrings(cwbordertypeid) + ",isaudit" + isaudit + ",isauditTime" + isauditTime + ",dispatchbranchid" + dispatchbranchid + ",deliverid" + deliverid
					+ ",operationOrderResultTypes" + operationOrderResultTypes + ",isshow:" + isshow + ",page:" + page, user.getRealname());
			// 保存供货商的选择
			customeridList = this.dataStatisticService.getList(customerid);
			// 保存站点的选择
			if ((dispatchbranchid.length == 0) & (branchnameList.size() > 0)) {
				dispatchbranchid = new String[branchnameList.size()];
				for (Branch bc : branchnameList) {
					dispatchbranchid[branchnameList.indexOf(bc)] = bc.getBranchid() + "";
				}
			}
			if (operationOrderResultTypes.length == 0) {
				operationOrderResultTypes = new String[4];
				operationOrderResultTypes[0] = DeliveryStateEnum.QuanBuTuiHuo.getValue() + "";
				operationOrderResultTypes[1] = DeliveryStateEnum.ShangMenJuTui.getValue() + "";
				operationOrderResultTypes[2] = DeliveryStateEnum.ShangMenHuanChengGong.getValue() + "";
				operationOrderResultTypes[3] = DeliveryStateEnum.ShangMenTuiChengGong.getValue() + "";
			}

			operationOrderResultTypeslist = this.dataStatisticService.getList(operationOrderResultTypes);
			dispatchbranchidList = this.dataStatisticService.getList(dispatchbranchid);
			// 保存订单类型的选择
			cwbordertypeidList = this.dataStatisticService.getList(cwbordertypeid);
			// 保存小件员的选择
			deliverlist = this.getDmpDAO.getAllUserByBranchIds(this.dataStatisticService.getStrings(dispatchbranchid));
			String customeridStr = this.dataStatisticService.getStrings(customerid);
			String cwbordertypeidStr = this.dataStatisticService.getStrings(cwbordertypeid);
			String dispatchbranchidStr = this.dataStatisticService.getStrings(dispatchbranchid);

			String operationOrderResultTypeStr = this.dataStatisticService.getStrings(operationOrderResultTypes);
			List<DeliveryJuShou> djList = this.deliveryJuShouDAO.getJuShouList(begindate, enddate, isaudit, isauditTime, customeridStr, cwbordertypeidStr, dispatchbranchidStr, deliverid,
					operationOrderResultTypeStr, page);
			sum = this.deliveryJuShouDAO.getDeliveryJuShouSum(begindate, enddate, isaudit, isauditTime, customeridStr, cwbordertypeidStr, dispatchbranchidStr, deliverid, operationOrderResultTypeStr);
			count = sum.getId();
			pageparm = new Page(count, page, Page.ONE_PAGE_NUMBER);
			if ((djList != null) && (djList.size() > 0)) {
				String cwbs = "";
				for (DeliveryJuShou deliveryJuShou : djList) {
					cwbs += "'" + deliveryJuShou.getCwb() + "',";
				}
				cwbs = cwbs.length() > 0 ? cwbs.substring(0, cwbs.length() - 1) : "";
				if (cwbs.length() > 0) {
					List<Branch> branchList = this.getDmpDAO.getAllBranchs();
					List<Reason> reasonList = this.getDmpDAO.getAllReason();
					orderlist = this.cwbDAO.getCwbOrderByCwbs(cwbs);
					orderlist = this.dataStatisticService.getJuShouCwbOrderViewCount10(orderlist, djList, customerlist, branchList, reasonList);
				}
			}
			String dmpURl = this.getDmpDAO.getDmpurl().substring(this.getDmpDAO.getDmpurl().lastIndexOf("/"), this.getDmpDAO.getDmpurl().length());
			model.addAttribute("dmpUrl", dmpURl);
			this.logger.info("拒收订单汇总(云)，当前操作人{},条数{}", user.getRealname(), count);
		}
		model.addAttribute("branchList", branchnameList);
		model.addAttribute("customerlist", customerlist);
		model.addAttribute("exportmouldlist", exportmouldlist);
		model.addAttribute("operationOrderResultTypeStr", operationOrderResultTypeslist);
		model.addAttribute("dispatchbranchidStr", dispatchbranchidList);
		model.addAttribute("cwbordertypeidStr", cwbordertypeidList);
		model.addAttribute("customeridStr", customeridList);
		model.addAttribute("deliverlist", deliverlist);
		model.addAttribute("count", count);
		model.addAttribute("sum", sum.getReceivablefee());
		model.addAttribute("orderlist", orderlist);
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);
		model.addAttribute("check", 1);
		model.addAttribute("deliverid", deliverid);
		return "datastatistics/jushoulist";
	}

	/**
	 * 分站到货统计
	 *
	 * @param model
	 * @param begindate
	 * @param enddate
	 * @param kufangid
	 * @param customerid
	 * @param cwbordertypeid
	 * @param currentBranchid
	 * @param isshow
	 * @param isnowdata
	 * @param page
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/daohuodata/{page}")
	public String daohuodata(Model model, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate,
			@RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "kufangid", required = false, defaultValue = "") String[] kufangid,
			@RequestParam(value = "customerid", required = false, defaultValue = "0") long customerid,
			@RequestParam(value = "cwbordertypeid", required = false, defaultValue = "") String[] cwbordertypeid,
			@RequestParam(value = "currentBranchid", required = false, defaultValue = "") String[] currentBranchid, @RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow,
			@RequestParam(value = "isnowdata", required = false, defaultValue = "0") long isnowdata, @PathVariable(value = "page") long page, HttpServletResponse response, HttpServletRequest request) {
		long count = 0;
		Page pageparm = new Page();
		DeliveryDaohuo deliveryDaohuosum = new DeliveryDaohuo();
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = this.getDmpDAO.getLogUser(dmpid);
		// 加载供货商
		List<Customer> customerlist = this.getDmpDAO.getAllCustomers();
		// 需要返回页面的前10条订单List
		List<CwbOrder> orderlist = new ArrayList<CwbOrder>();
		// 加载站点区域权限
		List<Branch> branchList = new ArrayList<Branch>();
		List<Branch> kufangList = new ArrayList<Branch>();
		if ((user != null) && (user.getUserid() > 0)) {
			Branch branch = this.getDmpDAO.getNowBranch(user.getBranchid());
			branchList = this.getDmpDAO.getQueryBranchByBranchsiteAndUserid(user.getUserid(), BranchEnum.ZhanDian.getValue() + "");
			kufangList = this.getDmpDAO.getQueryBranchByBranchsiteAndUserid(user.getUserid(),
					BranchEnum.KuFang.getValue() + "," + BranchEnum.TuiHuo.getValue() + "," + BranchEnum.ZhongZhuan.getValue());
			if ((branch.getSitetype() == BranchEnum.KuFang.getValue()) || (branch.getSitetype() == BranchEnum.TuiHuo.getValue()) || (branch.getSitetype() == BranchEnum.ZhongZhuan.getValue())) {
				if (kufangList.size() == 0) {
					kufangList.add(branch);
				} else {
					if (!this.dataStatisticService.checkBranchRepeat(kufangList, branch)) {
						kufangList.add(branch);
					}
				}
			} else if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
				if (branchList.size() == 0) {
					branchList.add(branch);
				} else {
					if (!this.dataStatisticService.checkBranchRepeat(branchList, branch)) {
						branchList.add(branch);
					}
				}
			}
		}
		// 加载导出模板
		List<Exportmould> exportmouldlist = this.getDmpDAO.getExportmoulds(user, dmpid);

		// 保存库房的选择
		List<String> kufangidStr = new ArrayList<String>();
		// 保存到货站点的选择
		List<String> currentBranchidStr = new ArrayList<String>();
		// 保存订单类型的选择
		List<String> cwbordertypeidStr = new ArrayList<String>();

		if ((user == null) || (user.getUserid() == 0)) {// 如果登录失效，提示登录失败
			model.addAttribute("nouser", "nouser");
		} else if (isshow == 1) {// 如果是点击查询按钮，封装查询数据
			this.logger.info("分站到货统计(云)，操作人{}，选择条件 begindate:" + begindate + ",enddate:" + enddate + ",kufangid:" + this.dataStatisticService.getStrings(kufangid) + ",cwbordertypeid:"
					+ cwbordertypeid + ",customerid:" + customerid + ",currentBranchid:" + this.dataStatisticService.getStrings(currentBranchid) + ",isshow:" + isshow + ",page:" + page,
					user.getRealname());

			begindate = begindate.length() == 0 ? DateTimeUtil.getNowTime() : begindate;
			enddate = enddate.length() == 0 ? DateTimeUtil.getNowTime() : enddate;

			// 保存供货商的选择
			kufangidStr = this.dataStatisticService.getList(kufangid);
			// 保存站点的选择
			currentBranchidStr = this.dataStatisticService.getList(currentBranchid);
			// 保存订单类型的选择
			cwbordertypeidStr = this.dataStatisticService.getList(cwbordertypeid);

			String cwbordertypeids = this.dataStatisticService.getStrings(cwbordertypeid);
			String kufangids = this.dataStatisticService.getStrings(kufangid);
			String currentBranchids = this.dataStatisticService.getStrings(currentBranchid);

			List<DeliveryDaohuo> delList = this.deliveryDaohuoDAO.getDeliveryDaohuoList(begindate, enddate, customerid, kufangids, cwbordertypeids, currentBranchids, isnowdata, page);

			deliveryDaohuosum = this.deliveryDaohuoDAO.getDeliveryDaohuoSum(begindate, enddate, customerid, kufangids, cwbordertypeids, currentBranchids, isnowdata);
			count = deliveryDaohuosum.getId();
			pageparm = new Page(count, page, Page.ONE_PAGE_NUMBER);
			if ((delList != null) && (delList.size() > 0)) {
				String cwbs = "";
				for (DeliveryDaohuo deliveryDaohuo : delList) {
					cwbs += "'" + deliveryDaohuo.getCwb() + "',";
				}
				cwbs = cwbs.length() > 0 ? cwbs.substring(0, cwbs.length() - 1) : "";
				if (cwbs.length() > 0) {
					orderlist = this.cwbDAO.getCwbOrderByCwbs(cwbs);
					List<Branch> branchAllList = this.getDmpDAO.getAllBranchs();
					List<User> userList = this.getDmpDAO.getUserForALL();
					orderlist = this.dataStatisticService.getDaohuoCwbOrderViewCount10(orderlist, delList, customerlist, branchAllList, userList);
				}
			}
			String dmpURl = this.getDmpDAO.getDmpurl().substring(this.getDmpDAO.getDmpurl().lastIndexOf("/"), this.getDmpDAO.getDmpurl().length());
			model.addAttribute("dmpUrl", dmpURl);
			this.logger.info("分站到货统计(云)，当前操作人{},条数{}", user.getRealname(), count);
		}
		model.addAttribute("customerlist", customerlist);
		model.addAttribute("exportmouldlist", exportmouldlist);
		model.addAttribute("branchList", branchList);
		model.addAttribute("kufangList", kufangList);
		model.addAttribute("cwbordertypeidStr", cwbordertypeidStr);
		model.addAttribute("kufangidStr", kufangidStr);
		model.addAttribute("currentBranchidStr", currentBranchidStr);

		model.addAttribute("count", count);
		model.addAttribute("sum", deliveryDaohuosum.getReceivablefee());
		model.addAttribute("paybackfeesum", deliveryDaohuosum.getPaybackfee());
		model.addAttribute("orderlist", orderlist);
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);
		model.addAttribute("lastupdatetime", this.updateMessageDAO.getUpdateMessageByMenunvalue(UpdateMessageMenuNameEnum.FenZhanDaoHuoTongJi.getValue()).getLastupdatetime());
		return "datastatistics/daohuodata";
	}

	@RequestMapping("/zhongzhuandata/{page}")
	public String zhongzhuandata(Model model, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate,
			@RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "branchid", required = false, defaultValue = "-1") long branchid,
			@RequestParam(value = "type", required = false, defaultValue = "startbranchid") String type, @RequestParam(value = "branchid2", required = false, defaultValue = "") String[] branchid2,
			@RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow, @PathVariable(value = "page") long page, HttpServletResponse response, HttpServletRequest request) {

		long count = 0;
		Page pageparm = new Page();
		ZhongZhuan zhongZhuansum = new ZhongZhuan();
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = this.getDmpDAO.getLogUser(dmpid);
		// 加载供货商
		List<Customer> customerlist = this.getDmpDAO.getAllCustomers();
		// 需要返回页面的前10条订单List
		List<CwbOrder> orderlist = new ArrayList<CwbOrder>();
		// 加载站点区域权限
		List<Branch> notuihuobranchList = new ArrayList<Branch>();
		List<Branch> zhongzhuanbranchList = new ArrayList<Branch>();
		long iskufang = 0;
		if ((user != null) && (user.getUserid() > 0)) {
			Branch branch = this.getDmpDAO.getNowBranch(user.getBranchid());
			if (branch.getSitetype() == BranchEnum.KuFang.getValue()) {
				iskufang = 1;
			}

			notuihuobranchList = this.getDmpDAO.getQueryBranchByBranchsiteAndUserid(user.getUserid(), BranchEnum.KuFang.getValue() + "," + BranchEnum.ZhanDian.getValue());
			zhongzhuanbranchList = this.getDmpDAO.getQueryBranchByBranchsiteAndUserid(user.getUserid(), BranchEnum.ZhongZhuan.getValue() + "");
		}
		// 加载导出模板
		List<Exportmould> exportmouldlist = this.getDmpDAO.getExportmoulds(user, dmpid);

		// 保存站点的选择
		List<String> branchid2list = new ArrayList<String>();
		String nextbranchids = "";
		String startbranchids = "";

		if ((user == null) || (user.getUserid() == 0)) {// 如果登录失效，提示登录失败
			model.addAttribute("nouser", "nouser");
		} else if (isshow == 1) {// 如果是点击查询按钮，封装查询数据
			this.logger.info(
					"中转订单统计(云)，操作人{}，选择条件 begindate:" + begindate + ",enddate:" + enddate + ",branchid:" + branchid + ",type:" + type + ",branchid2:" + this.dataStatisticService.getStrings(branchid2)
							+ ",isshow:" + isshow + ",page:" + page, user.getRealname());
			begindate = begindate.length() == 0 ? DateTimeUtil.getNowTime() : begindate;
			enddate = enddate.length() == 0 ? DateTimeUtil.getNowTime() : enddate;
			// 保存站点的选择
			branchid2list = this.dataStatisticService.getList(branchid2);

			if (type.equals("startbranchid")) {
				nextbranchids = this.dataStatisticService.getStrings(branchid2);
				startbranchids = branchid + "";
			} else if (type.equals("nextbranchid")) {
				nextbranchids = branchid + "";
				startbranchids = this.dataStatisticService.getStrings(branchid2);
			}

			List<ZhongZhuan> zhongzhuanList = this.zhongzhuanDAO.getZhongZhuanList(begindate, enddate, nextbranchids, startbranchids, page);

			zhongZhuansum = this.zhongzhuanDAO.getZhongZhuanSum(begindate, enddate, nextbranchids, startbranchids);
			count = zhongZhuansum.getId();
			pageparm = new Page(count, page, Page.ONE_PAGE_NUMBER);
			if ((zhongzhuanList != null) && (zhongzhuanList.size() > 0)) {
				String cwbs = "";
				for (ZhongZhuan zhongZhuan : zhongzhuanList) {
					cwbs += "'" + zhongZhuan.getCwb() + "',";
				}
				cwbs = cwbs.length() > 0 ? cwbs.substring(0, cwbs.length() - 1) : "";
				if (cwbs.length() > 0) {
					orderlist = this.cwbDAO.getCwbOrderByCwbs(cwbs);
					List<Branch> branchList = this.getDmpDAO.getAllBranchs();
					List<User> userList = this.getDmpDAO.getUserForALL();
					orderlist = this.dataStatisticService.getZhongzhuanCwbOrderViewCount10(orderlist, zhongzhuanList, customerlist, branchList, userList);
				}
			}
			String dmpURl = this.getDmpDAO.getDmpurl().substring(this.getDmpDAO.getDmpurl().lastIndexOf("/"), this.getDmpDAO.getDmpurl().length());
			model.addAttribute("dmpUrl", dmpURl);
			this.logger.info("中转订单统计(云)，当前操作人{},条数{}", user.getRealname(), count);
		}

		model.addAttribute("iskufang", iskufang);
		model.addAttribute("branchid2Str", branchid2list);
		model.addAttribute("notuihuobranchList", notuihuobranchList);
		model.addAttribute("zhongzhuanbranchList", zhongzhuanbranchList);
		model.addAttribute("count", count);
		model.addAttribute("orderlist", orderlist);
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);

		model.addAttribute("exportmouldlist", exportmouldlist);
		model.addAttribute("lastupdatetime", this.updateMessageDAO.getUpdateMessageByMenunvalue(UpdateMessageMenuNameEnum.KuFangChuKuTongJi.getValue()).getLastupdatetime());
		this.logger.info("中转订单统计(云)，当前操作人{},条数{}", user.getRealname(), count);
		return "datastatistics/zhongzhuandata";
	}

	/**
	 * 站点到货汇总
	 *
	 * @param model
	 * @param begindate
	 * @param enddate
	 * @param isshow
	 * @return
	 */
	@RequestMapping("/zhandiandaohuodata")
	public String zhandiandaohuodata(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate,
			@RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow) {

		List<Branch> zhandianlist = this.getDmpDAO.getBranchByAllEffectZhanDian();
		Map<Long, Long> branchMap = new HashMap<Long, Long>();

		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = this.getDmpDAO.getLogUser(dmpid);
		if ((user == null) || (user.getUserid() == 0)) {// 如果登录失效，提示登录失败
			model.addAttribute("nouser", "nouser");
		} else if (isshow == 1) {
			this.logger.info("站点到货汇总(云)，操作人{}，选择条件 begindate:" + begindate + ",enddate:" + enddate + ",isshow:" + isshow, user.getRealname());
			if (zhandianlist.size() > 0) {
				for (Branch b : zhandianlist) {
					List<String> ordercwblist = this.deliveryDaohuoDAO.getDaohuoDataByCurrentbranchidAndInSitetime(begindate, enddate, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + ","
							+ FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), b.getBranchid());
					branchMap.put(b.getBranchid(), (long) ordercwblist.size());
				}
			}
		}
		model.addAttribute("branchMap", branchMap);
		model.addAttribute("zhandianlist", zhandianlist);
		model.addAttribute("lastupdatetime", this.updateMessageDAO.getUpdateMessageByMenunvalue(UpdateMessageMenuNameEnum.FenZhanDaoHuoTongJi.getValue()).getLastupdatetime());
		this.logger.info("站点到货汇总(云)，当前操作人{},参数{}", user.getRealname(), begindate + "--" + enddate);

		return "datastatistics/zhandiandaohuodatalist";
	}

	/**
	 * 站点到货汇总详情
	 *
	 * @param model
	 * @param page
	 * @param customerid
	 * @param begindate
	 * @param enddate
	 * @param kufangid
	 * @return
	 */
	@RequestMapping("/zhandiandaohuodatashow/{page}")
	public String zhandiandaohuodatashow(Model model, HttpServletResponse response, HttpServletRequest request, @PathVariable("page") long page,
			@RequestParam(value = "branchid", required = false, defaultValue = "0") long branchid, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate,
			@RequestParam(value = "enddate", required = false, defaultValue = "") String enddate) {
		// 需要返回页面的前10条订单List
		List<CwbOrder> orderlist = new ArrayList<CwbOrder>();

		List<Customer> customerList = this.getDmpDAO.getAllCustomers();
		Page pageparm = new Page();
		long count = 0;

		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = this.getDmpDAO.getLogUser(dmpid);
		// 加载供货商
		List<Customer> customerlist = this.getDmpDAO.getAllCustomers();

		// 加载导出模板
		List<Exportmould> exportmouldlist = this.getDmpDAO.getExportmoulds(user, dmpid);

		if ((user == null) || (user.getUserid() == 0)) {// 如果登录失效，提示登录失败
			model.addAttribute("nouser", "nouser");
		} else {

			List<DeliveryDaohuo> delList = this.deliveryDaohuoDAO.getDaohuoDataByCurrentbranchidAndInSitetimePage(page, begindate, enddate, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + ","
					+ FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), branchid);
			count = this.deliveryDaohuoDAO.getDaohuoDataByCurrentbranchidAndInSitetimeCount(begindate, enddate, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() + ","
					+ FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), branchid);

			pageparm = new Page(count, page, Page.ONE_PAGE_NUMBER);
			if ((delList != null) && (delList.size() > 0)) {
				String cwbs = "";
				for (DeliveryDaohuo delidaohuo : delList) {
					cwbs += "'" + delidaohuo.getCwb() + "',";
				}
				cwbs = cwbs.length() > 0 ? cwbs.substring(0, cwbs.length() - 1) : "";
				if (cwbs.length() > 0) {
					orderlist = this.cwbDAO.getCwbOrderByCwbs(cwbs);
					List<Branch> branchAllList = this.getDmpDAO.getAllBranchs();
					List<User> userList = this.getDmpDAO.getUserForALL();
					orderlist = this.dataStatisticService.getDaohuoCwbOrderViewCount10(orderlist, delList, customerlist, branchAllList, userList);
				}
			}
			String dmpURl = this.getDmpDAO.getDmpurl().substring(this.getDmpDAO.getDmpurl().lastIndexOf("/"), this.getDmpDAO.getDmpurl().length());
			model.addAttribute("dmpUrl", dmpURl);
		}
		model.addAttribute("orderlist", orderlist);
		model.addAttribute("customerList", customerList);
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);
		model.addAttribute("exportmouldlist", exportmouldlist);
		return "datastatistics/zhandiandaohuodatashow";
	}

	/**
	 * 提货订单统计(云)
	 */
	@RequestMapping("/tihuodata/{page}")
	public String tihuodata(Model model, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate,
			@RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, @RequestParam(value = "customerid", required = false, defaultValue = "") String[] customerids,
			@RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow, @PathVariable(value = "page") long page, HttpServletResponse response, HttpServletRequest request) {
		long count = 0;
		Page pageparm = new Page();
		CwbTiHuo cwbTiHuoSum = new CwbTiHuo();
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = this.getDmpDAO.getLogUser(dmpid);
		// 加载供货商
		List<Customer> customerlist = this.getDmpDAO.getAllCustomers();
		// 需要返回页面的前10条订单List
		List<CwbOrder> orderlist = new ArrayList<CwbOrder>();
		// 加载导出模板
		// List<Exportmould> exportmouldlist =
		// getDmpDAO.getExportmoulds(user,dmpid);

		// 保存供货商的选择
		List<String> customeridList = new ArrayList<String>();

		if ((user == null) || (user.getUserid() == 0)) {// 如果登录失效，提示登录失败
			model.addAttribute("nouser", "nouser");
		} else if (isshow == 1) {// 如果是点击查询按钮，封装查询数据
			// 保存供货商的选择
			customeridList = this.dataStatisticService.getList(customerids);

			this.logger.info(" 提货订单统计(云)，操作人{}，选择条件 begindate:" + begindate + ",enddate:" + enddate + ",customerid:" + this.dataStatisticService.getStrings(customerids) + ",isshow:" + isshow
					+ ",page:" + page, user.getRealname());
			begindate = begindate.length() == 0 ? DateTimeUtil.getNowTime() : begindate;
			enddate = enddate.length() == 0 ? DateTimeUtil.getNowTime() : enddate;

			String customeridStr = this.dataStatisticService.getStrings(customerids);
			List<CwbTiHuo> tihuoList = this.cwbTiHuoDAO.getCwbTiHuoList(page, begindate, enddate, customeridStr);
			cwbTiHuoSum = this.cwbTiHuoDAO.getCwbTiHuoSum(begindate, enddate, customeridStr);
			count = cwbTiHuoSum.getId();

			pageparm = new Page(count, page, Page.ONE_PAGE_NUMBER);
			if ((tihuoList != null) && (tihuoList.size() > 0)) {
				String cwbs = "";
				for (CwbTiHuo cwbTiHuo : tihuoList) {
					cwbs += "'" + cwbTiHuo.getCwb() + "',";
				}
				cwbs = cwbs.length() > 0 ? cwbs.substring(0, cwbs.length() - 1) : "";
				if (cwbs.length() > 0) {
					orderlist = this.cwbDAO.getCwbOrderByCwbs(cwbs);
					orderlist = this.dataStatisticService.getTiHuoCwbOrderViewCount10(orderlist, tihuoList, customerlist);
				}
			}
			String dmpURl = this.getDmpDAO.getDmpurl().substring(this.getDmpDAO.getDmpurl().lastIndexOf("/"), this.getDmpDAO.getDmpurl().length());
			model.addAttribute("dmpUrl", dmpURl);
			this.logger.info("提货订单统计(云)，当前操作人{},条数{}", user.getRealname(), count);
		}

		model.addAttribute("customerlist", customerlist);
		model.addAttribute("customeridStr", customeridList);
		model.addAttribute("count", count);
		model.addAttribute("orderlist", orderlist);
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);

		// model.addAttribute("exportmouldlist", exportmouldlist);
		model.addAttribute("lastupdatetime", this.updateMessageDAO.getUpdateMessageByMenunvalue(UpdateMessageMenuNameEnum.TiHuoDingDanTongJi.getValue()).getLastupdatetime());
		this.logger.info("提货订单统计，当前操作人{},条数{}", user.getRealname(), count);
		return "datastatistics/tihuolist";
	}

	/**
	 * 客户发货统计
	 *
	 * @param model
	 * @param begindate
	 * @param enddate
	 * @param kufangid
	 * @param customerid
	 * @param cwbordertypeid
	 * @param orderbyName
	 * @param orderbyId
	 * @param isshow
	 * @param page
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/fahuodata/{page}")
	public String fahuodata(Model model, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate,
			@RequestParam(value = "enddate", required = false, defaultValue = "") String enddate,
			@RequestParam(value = "curdispatchbranchid", required = false, defaultValue = "") String curdispatchbranchid,
			@RequestParam(value = "dispatchbranchid", required = false, defaultValue = "") String dispatchbranchid,
			@RequestParam(value = "nextdispatchbranchid", required = false, defaultValue = "") String nextdispatchbranchid,
			@RequestParam(value = "kufangid", required = false, defaultValue = "") String[] kufangid, @RequestParam(value = "customerid", required = false, defaultValue = "") String[] customerid,
			@RequestParam(value = "cwbordertypeid", required = false, defaultValue = "") String[] cwbordertypeid,
			@RequestParam(value = "orderbyName", required = false, defaultValue = "emaildate") String orderbyName,
			@RequestParam(value = "orderbyType", required = false, defaultValue = "DESC") String orderbyId,
			@RequestParam(value = "flowordertype", required = false, defaultValue = "0") long flowordertype,
			@RequestParam(value = "servicetype", required = false, defaultValue = "全部") String servicetype, @RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow,
			@PathVariable(value = "page") long page, HttpServletResponse response, HttpServletRequest request) {
		long count = 0;
		Page pageparm = new Page();
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = this.getDmpDAO.getLogUser(dmpid);
		CwbOrder sum = new CwbOrder();
		List<Customer> customerList = this.getDmpDAO.getAllCustomers();

		List<CwbOrder> cwbOrderView = new ArrayList<CwbOrder>();
		if ((user == null) || (user.getUserid() == 0)) {// 如果登录失效，提示登录失败
			model.addAttribute("nouser", "nouser");
		}
		Branch branch = this.getDmpDAO.getNowBranch(user.getBranchid());
		List<Branch> kufangList = this.getDmpDAO.getQueryBranchByBranchsiteAndUserid(user.getUserid(),
				BranchEnum.KuFang.getValue() + "," + BranchEnum.TuiHuo.getValue() + "," + BranchEnum.ZhongZhuan.getValue());

		if ((branch.getSitetype() == BranchEnum.KuFang.getValue()) || (branch.getSitetype() == BranchEnum.TuiHuo.getValue()) || (branch.getSitetype() == BranchEnum.ZhongZhuan.getValue())) {
			if (kufangList.size() == 0) {
				kufangList.add(branch);
			} else {
				if (!this.dataStatisticService.checkBranchRepeat(kufangList, branch)) {
					kufangList.add(branch);
				}
			}
		}
		if (isshow != 0) {
			this.logger.info("客户发货统计(云)，操作人{}，选择条件 begindate:" + begindate + ",enddate:" + enddate + ",kufangid:" + this.dataStatisticService.getStrings(kufangid) + ",cwbordertypeid:"
					+ cwbordertypeid + ",customerid:" + this.dataStatisticService.getStrings(customerid) + ",orderbyName:" + orderbyName + ",orderbyId:" + orderbyId + ",isshow:" + isshow + ",page:"
					+ page, user.getRealname());

			begindate = begindate.length() == 0 ? DateTimeUtil.getNowTime() : begindate;
			enddate = enddate.length() == 0 ? DateTimeUtil.getNowTime() : enddate;
			// 定义参数
			List<CwbOrder> clist = new ArrayList<CwbOrder>();
			String customerids = this.dataStatisticService.getStrings(customerid);
			String cwbordertypeids = this.dataStatisticService.getStrings(cwbordertypeid);
			String kufangids = this.dataStatisticService.getStrings(kufangid);
			if((kufangid.length==0)&&(kufangids.length()==0)){
				for(Branch kf:kufangList)
				{
					kufangids+=kf.getBranchid()+",";
				}
				if ((kufangids.length() > 0)&&kufangids.contains(",")) {
					kufangids = kufangids.substring(0, kufangids.length() - 1);
				}
			}
			// 获取值
			count = this.cwbDAO.getKeHuFaHuoTongJiCount(begindate, enddate, customerids, cwbordertypeids, kufangids, flowordertype, servicetype);

			sum = this.cwbDAO.getKeHuFaHuoTongJiSum(begindate, enddate, customerids, cwbordertypeids, kufangids, flowordertype, servicetype);

			clist = this.cwbDAO.getKeHuFaHuoTongJi(page, begindate, enddate, customerids, cwbordertypeids, kufangids, flowordertype, servicetype);

			pageparm = new Page(count, page, Page.ONE_PAGE_NUMBER);

			// 赋值显示对象
			List<Branch> branchs = this.getDmpDAO.getAllBranchs();
			cwbOrderView = this.dataStatisticService.getKeHuFaHuoTongJiCwbOrderView(clist, customerList, branchs);
			String dmpURl = this.getDmpDAO.getDmpurl().substring(this.getDmpDAO.getDmpurl().lastIndexOf("/"), this.getDmpDAO.getDmpurl().length());
			model.addAttribute("dmpUrl", dmpURl);
			this.logger.info("客户发货统计(云)，当前操作人{},条数{}", user.getRealname(), count);
		}

		List<String> kufangidlist = this.dataStatisticService.getList(kufangid);
		List<String> cwbordertypeidlist = this.dataStatisticService.getList(cwbordertypeid);
		List<String> customeridList = this.dataStatisticService.getList(customerid);
		model.addAttribute("customerlist", customerList);
		model.addAttribute("kufangList", kufangList);
		model.addAttribute("exportmouldlist", this.getDmpDAO.getExportmoulds(user, dmpid));
		model.addAttribute("customeridStr", customeridList);
		model.addAttribute("cwbordertypeidStr", cwbordertypeidlist);
		model.addAttribute("kufangidStr", kufangidlist);
		model.addAttribute("count", count);
		model.addAttribute("sum", sum.getReceivablefee());
		model.addAttribute("paybackfeesum", sum.getPaybackfee());
		model.addAttribute("orderlist", cwbOrderView);
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);
		model.addAttribute("servicetype", servicetype);
		this.logger.info("客户发货统计(云)，当前操作人{},条数{}", user.getRealname(), count);
		return "datastatistics/fahuolist";
	}

	/**
	 * 综合查询
	 *
	 * @param model
	 * @param begindate
	 * @param enddate
	 * @param isaudit
	 * @param isauditTime
	 * @param customerids
	 * @param cwbordertypeids
	 * @param paywayid
	 * @param dispatchbranchids
	 * @param deliverid
	 * @param operationOrderResultTypes
	 * @param isshow
	 * @param page
	 * @param response
	 * @param request
	 * @return
	 */

	@RequestMapping("/zonghechaxun/{page}")
	public String zonghechaxun(Model model, @RequestParam(value = "cwdname", required = false, defaultValue = "") String cwdname,
			@RequestParam(value = "begindate", required = false, defaultValue = "") String begindate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate,
			@RequestParam(value = "curquerytimetype", required = false, defaultValue = "-1") String curquerytimetype,
			@RequestParam(value = "isaudit", required = false, defaultValue = "-1") String isaudit, @RequestParam(value = "customerid", required = false, defaultValue = "") String[] customerids,
			@RequestParam(value = "cwbordertypeid", required = false, defaultValue = "") String[] cwbordertypeids,
			@RequestParam(value = "paytype", required = false, defaultValue = "-1") String paywayid, @RequestParam(value = "curpaytype", required = false, defaultValue = "-1") String curpaytypeid,
			@RequestParam(value = "cwbstate", required = false, defaultValue = "-1") long cwbstate,
			@RequestParam(value = "dispatchbranchid", required = false, defaultValue = "") String[] dispatchbranchids,
			@RequestParam(value = "curdispatchbranchid", required = false, defaultValue = "") String[] curdispatchbranchid,
			@RequestParam(value = "nextdispatchbranchid", required = false, defaultValue = "") String[] nextdispatchbranchid,
			@RequestParam(value = "operationOrderResultType", required = false, defaultValue = "") String[] operationOrderResultTypes,
			@RequestParam(value = "financeAuditStatus", required = false, defaultValue = "-1") int financeAuditStatus,
			@RequestParam(value = "goodsType", required = false, defaultValue = "-1") int goodsType, HttpServletResponse response,
			@RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow, @PathVariable(value = "page") long page, HttpServletRequest request) {

		long count = 0;
		Page pageparm = new Page();
		CwbOrderTail sum = new CwbOrderTail();
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = this.getDmpDAO.getLogUser(dmpid);
		// 加载供货商
		List<Customer> customerlist = this.getDmpDAO.getAllCustomers();
		// 查询列表
		List<CwbOrderTail> orderlist = null;
		// 加载站点区域权限
		List<Branch> branchnameList = new ArrayList<Branch>();
		if ((user != null) && (user.getUserid() > 0)) {
			Branch branch = this.getDmpDAO.getNowBranch(user.getBranchid());
			branchnameList = this.getDmpDAO.getQueryBranchByBranchsiteAndUserid(user.getUserid(),
					BranchEnum.ZhanDian.getValue() + "," + BranchEnum.TuiHuo.getValue() + "," + BranchEnum.ZhongZhuan.getValue());
			if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
				if (branchnameList.size() == 0) {
					branchnameList.add(branch);
				} else {
					if (!this.dataStatisticService.checkBranchRepeat(branchnameList, branch)) {
						branchnameList.add(branch);
					}
				}
			}
		}

		List<String> operationOrderResultTypeslist = this.dataStatisticService.getList(operationOrderResultTypes);
		// 加载导出模板
		List<Exportmould> exportmouldlist = this.getDmpDAO.getExportmoulds(user, dmpid);

		Map<Long, String> brachmap = new HashMap<Long, String>();
		Map<Long, String> customermap = new HashMap<Long, String>();

		if ((user == null) || (user.getUserid() == 0)) {// 如果登录失效，提示登录失败
			model.addAttribute("nouser", "nouser");
		} else if (isshow == 1) {// 如果是点击查询按钮，封装查询数据
			List<Branch> branchs = this.getDmpDAO.getAllBranchs();
			for (Branch branch : branchs) {
				brachmap.put(branch.getBranchid(), branch.getBranchname());
			}
			for (Customer customer : customerlist) {
				customermap.put(customer.getCustomerid(), customer.getCustomername());
			}
			this.logger.info(
					"综合查询，操作人{}，选择条件 begindate:" + begindate + ",enddate:" + enddate + ",isaudit:" + isaudit + ",customerid:" + this.dataStatisticService.getStrings(customerids) + ",cwbordertypeid:"
							+ this.dataStatisticService.getStrings(cwbordertypeids) + ",paywayid:" + paywayid + ",dispatchbranchids:" + this.dataStatisticService.getStrings(dispatchbranchids)
							+ ",curdispatchbranchid:" + this.dataStatisticService.getStrings(curdispatchbranchid) + ",nextdispatchbranchid:"
							+ this.dataStatisticService.getStrings(nextdispatchbranchid) + ",isshow:" + isshow + ",page:" + page, user.getRealname());

			CwbOrderTail tail = new CwbOrderTail();

			tail.setBegintime(begindate);

			tail.setEndtime(enddate);

			tail.setCurquerytimetype(curquerytimetype);

			tail.setDispatchbranchids(dispatchbranchids);

			tail.setCurdispatchbranchids(curdispatchbranchid);

			tail.setNextdispatchbranchids(nextdispatchbranchid);

			tail.setOperationOrderResultTypes(operationOrderResultTypes);

			tail.setFlowordertype(cwbstate);

			tail.setPaywayid(paywayid);

			tail.setNewpaywayid(curpaytypeid);

			tail.setCustomerids(customerids);

			tail.setCwbordertypeids(cwbordertypeids);

			tail.setGobackstate(isaudit);

			tail.setCurquerytimecolumn(FlowOrderColumnMap.ORDER_FLOW_TAIL_MAP.get(curquerytimetype).replaceAll("credate_", ""));

			tail.setFinanceAuditStatus(financeAuditStatus);
			tail.setGoodsType(goodsType);

			orderlist = this.cwbOrderTaildao.getTailList(tail, page);
			sum = this.cwbOrderTaildao.getTailSum(tail);
			count = sum.getId();
			pageparm = new Page(count, page, Page.ONE_PAGE_NUMBER);

			String dmpURl = this.getDmpDAO.getDmpurl().substring(this.getDmpDAO.getDmpurl().lastIndexOf("/"), this.getDmpDAO.getDmpurl().length());
			model.addAttribute("dmpUrl", dmpURl);
			this.logger.info("综合查询，当前操作人{},条数{}", user.getRealname(), count);

		}
		model.addAttribute("sum", sum.getReceivablefee());
		model.addAttribute("paybackfeesum", sum.getPaybackfee());
		model.addAttribute("operationOrderResultTypeStr", operationOrderResultTypeslist);
		model.addAttribute("dispatchbranchidArray", dispatchbranchids);
		model.addAttribute("curdispatchbranchidArray", curdispatchbranchid);
		model.addAttribute("nextdispatchbranchidArray", nextdispatchbranchid);
		model.addAttribute("curquerytimetype", curquerytimetype);
		model.addAttribute("begindate", begindate);
		model.addAttribute("enddate", enddate);
		model.addAttribute("count", count);
		model.addAttribute("orderlist", orderlist);
		model.addAttribute("cwbordertypeidStr", cwbordertypeids);
		model.addAttribute("customeridStr", customerids);
		model.addAttribute("branchList", branchnameList);
		model.addAttribute("customerlist", customerlist);
		model.addAttribute("paytype", paywayid);
		model.addAttribute("curpaytypeid", curpaytypeid);
		model.addAttribute("cwbstate", cwbstate);
		model.addAttribute("page_obj", pageparm);
		model.addAttribute("page", page);
		model.addAttribute("isaudit", isaudit);
		model.addAttribute("brachmap", brachmap);
		model.addAttribute("customermap", customermap);
		model.addAttribute("check", 1);
		model.addAttribute("exportmouldlist", exportmouldlist);
		model.addAttribute("goodsType", goodsType);
		model.addAttribute("financeAuditStatus", financeAuditStatus);
		model.addAttribute("lastupdatetime", this.updateMessageDAO.getUpdateMessageByMenunvalue(UpdateMessageMenuNameEnum.ZongHeChaXun.getValue()).getLastupdatetime());
		return "datastatistics/zonghelist";
	}

	/**
	 * 验证离线导出状况
	 *
	 * @param model
	 * @param response
	 * @param request
	 * @param page
	 * @param sign
	 *            模块id
	 * @return
	 */
	@RequestMapping("/exportExcle")
	public @ResponseBody String exportExcle(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "begin", required = false, defaultValue = "0") long page,
			@RequestParam(value = "sign", required = false, defaultValue = "0") long sign) {
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = this.getDmpDAO.getLogUser(dmpid);
		JSONObject json = new JSONObject();
		if ((user == null) || (user.getUserid() == 0)) {// 如果登录失效，提示登录失败
			json.put("errorCode", 1);
			json.put("remark", "登录失效");
			return json.toString();
		}
		return this.dataStatisticService.DataStatisticsExportExcelCheck(response, request, page, sign, user.getUserid());

	}

	/**
	 * 执行离线导出
	 *
	 * @param model
	 * @param response
	 * @param request
	 * @param page
	 * @param sign
	 */
	@RequestMapping("/commitExportExcle")
	public void commitExportExcle(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "begin", required = false, defaultValue = "0") long page,
			@RequestParam(value = "sign", required = false, defaultValue = "0") long sign) {
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = this.getDmpDAO.getLogUser(dmpid);
		if ((user == null) || (user.getUserid() == 0)) {// 如果登录失效，提示登录失败
			return;
		}
		this.downloadManagerService.down_task();
	}

	/**
	 * 按站点查询小件员
	 *
	 * @param model
	 * @param branchids
	 * @return
	 */
	@RequestMapping("/updateDeliverByBranchids")
	public @ResponseBody String updateDeliverByBranchids(Model model, @RequestParam("branchid") String branchids) {
		if (branchids.length() > 0) {
			branchids = branchids.substring(0, branchids.length() - 1);
			List<User> list = this.getDmpDAO.getAllUserByBranchIds(branchids);
			return JSONArray.fromObject(list).toString();
		} else {
			return "[]";
		}
	}

	/**
	 * 按输入站点名称查询多选下拉框站点
	 *
	 * @param branchname
	 * @param response
	 * @param request
	 * @return
	 */

	@RequestMapping("/selectallnexusbranch")
	public @ResponseBody String selectallnexusbranch(@RequestParam(value = "branchname", required = false, defaultValue = "") String branchname, HttpServletResponse response,
			HttpServletRequest request) {

		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = this.getDmpDAO.getLogUser(dmpid);
		List<Branch> branchnameList = new ArrayList<Branch>();
		if (branchname.length() > 0) {
			if ((user != null) && (user.getUserid() > 0)) {
				Branch branch = this.getDmpDAO.getNowBranch(user.getBranchid());
				branchnameList = this.getDmpDAO.getQueryBranchByBranchsiteAndUserid(user.getUserid(),
						BranchEnum.ZhanDian.getValue() + "," + BranchEnum.TuiHuo.getValue() + "," + BranchEnum.ZhongZhuan.getValue());
				if (branch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
					if (branchnameList.size() == 0) {
						branchnameList.add(branch);
					} else {
						if (!this.dataStatisticService.checkBranchRepeat(branchnameList, branch)) {
							branchnameList.add(branch);
						}
					}
				}
			}
			List<Branch> dispatchbranchidStr = this.getDmpDAO.getBranchByName(branchname);
			List<Branch> dispatchbranchidnewStr = new ArrayList<Branch>();
			if ((dispatchbranchidStr.size() > 0) && (branchnameList.size() > 0)) {
				for (Branch b : branchnameList) {
					for (Branch branchStr : dispatchbranchidStr) {
						if (branchStr.getBranchid() == b.getBranchid()) {
							dispatchbranchidnewStr.add(branchStr);
						}
					}
				}
			}
			return JSONArray.fromObject(dispatchbranchidnewStr).toString();
		} else {
			return "[]";
		}
	}

	/**
	 * 根据站点名称精确查询到匹配的站点名称
	 *
	 * @param branchname
	 * @return
	 */
	@RequestMapping("/selectnexusbranchbybranchname")
	public @ResponseBody String selectnexusbranchbybranchname(@RequestParam(value = "branchname", required = false, defaultValue = "") String branchname, HttpServletResponse response,
			HttpServletRequest request) {
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = this.getDmpDAO.getLogUser(dmpid);
		if (branchname.length() > 0) {
			Branch branch = this.getDmpDAO.getNowBranch(user.getBranchid());
			List<Branch> branchList = this.getDmpDAO.getQueryBranchByBranchsiteAndUserid(user.getUserid(), BranchEnum.KuFang.getValue() + "," + BranchEnum.ZhanDian.getValue() + ","
					+ BranchEnum.TuiHuo.getValue() + "," + BranchEnum.ZhongZhuan.getValue());

			if (branchList.size() == 0) {
				branchList.add(branch);
			} else {
				if (!this.dataStatisticService.checkBranchRepeat(branchList, branch)) {
					branchList.add(branch);
				}
			}

			Branch deliverybranch = this.getDmpDAO.getBranchByBranchName(branchname);
			Branch newdispatchbranch = new Branch();
			for (Branch b : branchList) {
				if (deliverybranch.getBranchid() == b.getBranchid()) {
					newdispatchbranch = deliverybranch;
				}
			}
			return JSONObject.fromObject(newdispatchbranch).toString();
		} else {
			return "{}";
		}
	}

	@RequestMapping("/dianshangdanliang")
	public String DianshangSearch(Model model, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate,// 开始时间
			@RequestParam(value = "enddate", required = false, defaultValue = "") String enddate, // 结束时间
			@RequestParam(value = "kufangid", required = false, defaultValue = "0") long kufangid,// 库房id
			@RequestParam(value = "select", required = false, defaultValue = "0") long type,// 查询的时间模式：1.客户发货时间2.库房入库时间3.库房出库时间
			HttpServletRequest request, @RequestParam(value = "isshow", required = false, defaultValue = "0") long isshow) {
		List<Customer> customerList = this.getDmpDAO.getAllCustomers();// 查询所有供货商

		List<Branch> kufanglist = this.getDmpDAO.getBranchByKufang();// 查询所有库房
		this.logger.info("查询时间范围{}-{}", begindate, enddate + "库房" + kufangid + "type" + type);
		// 得到查询的当前客户
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = this.getDmpDAO.getLogUser(dmpid);
		// 定义一个map用来存储
		Map<Long, Map<String, Long>> customMap = new HashMap<Long, Map<String, Long>>();
		if (isshow != 0) {
			Map<Long, Map<String, Long>> KuFangRuKuMap = new HashMap<Long, Map<String, Long>>();
			Map<Long, Map<String, Long>> KuFangChukuMap = new HashMap<Long, Map<String, Long>>();

			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String endTimmer = enddate;
			boolean flag = true;

			if (type == 1) {
				customMap = this.getfahuoListForDianshang(df, begindate, flag, endTimmer, kufangid, customerList, customMap);
				model.addAttribute("customMap", customMap);
				model.addAttribute("type", type);
				model.addAttribute("customerList", customerList);
				model.addAttribute("kufanglist", kufanglist);
				List<String> dateList = this.getDateLsit("".equals(begindate) ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00" : begindate + " 00:00:00",
						"".equals(enddate) ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 23:59:59" : enddate + " 23:59:59");
				model.addAttribute("startdate", "".equals(begindate) ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) : begindate);
				model.addAttribute("enddate", "".equals(enddate) ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) : enddate);
				model.addAttribute("dateList", dateList);
				this.logger.info("电商单量查询，当前操作人{},参数{}", user.getRealname(), kufangid + "--" + begindate + "--" + enddate);
				return "datastatistics/dianshangdanliang";
			}
			if (type == 2) {
				KuFangRuKuMap = this.getrukuListForDianshang(df, begindate, flag, endTimmer, kufangid, customerList, KuFangRuKuMap);
				model.addAttribute("customMap", KuFangRuKuMap);
				model.addAttribute("type", type);
				model.addAttribute("customerList", customerList);
				model.addAttribute("kufanglist", kufanglist);
				List<String> dateList = this.getDateLsit("".equals(begindate) ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00" : begindate + " 00:00:00",
						"".equals(enddate) ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 23:59:59" : enddate + " 23:59:59");
				model.addAttribute("startdate", "".equals(begindate) ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) : begindate);
				model.addAttribute("enddate", "".equals(enddate) ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) : enddate);
				model.addAttribute("dateList", dateList);
				this.logger.info("电商单量查询，当前操作人{},参数{}", user.getRealname(), kufangid + "--" + begindate + "--" + enddate);
				return "datastatistics/dianshangRuku";
			}
			if (type == 3) {
				KuFangChukuMap = this.getchukuListForDianshang(df, begindate, flag, endTimmer, kufangid, customerList, KuFangChukuMap);
				model.addAttribute("customMap", KuFangChukuMap);
				model.addAttribute("type", type);
				model.addAttribute("customerList", customerList);
				model.addAttribute("kufanglist", kufanglist);
				List<String> dateList = this.getDateLsit("".equals(begindate) ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00" : begindate + " 00:00:00",
						"".equals(enddate) ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 23:59:59" : enddate + " 23:59:59");
				model.addAttribute("startdate", "".equals(begindate) ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) : begindate);
				model.addAttribute("enddate", "".equals(enddate) ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) : enddate);
				model.addAttribute("dateList", dateList);
				this.logger.info("电商单量查询，当前操作人{},参数{}", user.getRealname(), kufangid + "--" + begindate + "--" + enddate);
				return "datastatistics/dianshangChuku";
			}

		}
		model.addAttribute("customMap", customMap);
		model.addAttribute("type", type);
		model.addAttribute("customerList", customerList);
		model.addAttribute("kufanglist", kufanglist);
		List<String> dateList = this.getDateLsit("".equals(begindate) ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00" : begindate + " 00:00:00",
				"".equals(enddate) ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 23:59:59" : enddate + " 23:59:59");
		model.addAttribute("startdate", "".equals(begindate) ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) : begindate);
		model.addAttribute("enddate", "".equals(enddate) ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) : enddate);
		model.addAttribute("dateList", dateList);
		this.logger.info("电商单量查询，当前操作人{},参数{}", user.getRealname(), kufangid + "--" + begindate + "--" + enddate);
		return "datastatistics/dianshangdanliang";

	}

	@RequestMapping("/searchDataFor/{page}")
	// 分页
	public String searchDataFor(Model model, HttpServletRequest request, @PathVariable(value = "page") long page,
			@RequestParam(value = "searchbranchid", required = false, defaultValue = "0") long kufangid,// 库房id
			@RequestParam(value = "select", required = false, defaultValue = "0") long type,// 查询的时间模式：1.客户发货时间2.库房入库时间3.库房出库时间
			@RequestParam(value = "searchtime", required = false, defaultValue = "0") String timer,// 查询的时间
			@RequestParam(value = "searchcustid", required = false, defaultValue = "0") long searchcustid// 查询的供货商id
	) {
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = this.getDmpDAO.getLogUser(dmpid);
		List<Customer> customerList = this.getDmpDAO.getAllCustomers();// 查询所有供货商
		List<Branch> kufanglist = this.getDmpDAO.getBranchByKufang();// 查询所有库房
		List<Exportmould> exportmouldlist = this.getDmpDAO.getExportmoulds(user, dmpid);
		model.addAttribute("exportmouldlist", exportmouldlist);
		if (type == 1) {
			List<CwbOrder> clist = this.cwbDAO.getEditByCustomeridAndEmaildate(timer, kufangid, searchcustid, page);
			model.addAttribute("page", page);
			model.addAttribute("page_obj", new Page(this.cwbDAO.getCountByCustomeridAndEmaildate(timer, kufangid, searchcustid), page, Page.ONE_PAGE_NUMBER));
			model.addAttribute("type", type);
			model.addAttribute("showlist", clist);
			model.addAttribute("timer", timer);
			model.addAttribute("customerid", searchcustid);
			model.addAttribute("kufangid", kufangid);
			model.addAttribute("customerList", customerList);
			model.addAttribute("kufanglist", kufanglist);
			return "datastatistics/showfahuo";
		}
		if (type == 2) {
			List<KuFangRuKuOrder> rukulist = this.kuFangRuKuDao.getEditByCustomeridAndEmaildate(timer, kufangid, searchcustid, page);
			model.addAttribute("type", type);
			model.addAttribute("showlist", rukulist);
			model.addAttribute("page", page);
			model.addAttribute("page_obj", new Page(this.kuFangRuKuDao.getCountByCustomeridAndEmaildate(timer, kufangid, searchcustid), page, Page.ONE_PAGE_NUMBER));
			model.addAttribute("kufanglist", kufanglist);
			model.addAttribute("customerList", customerList);
			model.addAttribute("timer", timer);
			model.addAttribute("customerid", searchcustid);
			model.addAttribute("kufangid", kufangid);
			return "datastatistics/showfahuo";
		}
		if (type == 3) {
			List<DeliveryChuku> chukulist = this.deliveryChukuDAO.getEditByCustomeridAndEmaildate(timer, kufangid, searchcustid, page);
			model.addAttribute("type", type);
			model.addAttribute("showlist", chukulist);
			model.addAttribute("page", page);
			model.addAttribute("page_obj", new Page(this.deliveryChukuDAO.getCountByCustomeridAndEmaildate(timer, kufangid, searchcustid), page, Page.ONE_PAGE_NUMBER));
			model.addAttribute("kufanglist", kufanglist);
			model.addAttribute("timer", timer);
			model.addAttribute("customerid", searchcustid);
			model.addAttribute("kufangid", kufangid);
			model.addAttribute("customerList", customerList);
			return "datastatistics/showfahuo";
		}
		return "";
	}

	private Map<Long, Map<String, Long>> getfahuoListForDianshang(DateFormat df, String begindate, boolean flag, String endTimmer, long kufangid, List<Customer> customerList,
			Map<Long, Map<String, Long>> customMap) {

		List<CwbOrder> todayLogList = this.cwbDAO.getCwbByCustomeridAndEmaildate("".equals(begindate) ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00" : begindate + " 00:00:00",
				"".equals(endTimmer) ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 23:59:59" : endTimmer + " 23:59:59", kufangid);
		if ((customerList != null) && (customerList.size() > 0)) {

			for (Customer cus : customerList) {
				long i = 0;
				Map<String, Long> logMap = new HashMap<String, Long>();
				if ((todayLogList != null) && (todayLogList.size() > 0)) {
					for (CwbOrder branchTodayLog : todayLogList) {
						if (branchTodayLog.getCustomerid() == cus.getCustomerid()) {
							i = i + 1;
							if (!logMap.containsKey(branchTodayLog.getEmaildate().substring(0, 10))) {
								i = 1;
							}
							logMap.put(branchTodayLog.getEmaildate().substring(0, 10), i);
						}
					}
				}
				customMap.put(cus.getCustomerid(), logMap);
			}

		}

		return customMap;

	}

	private Map<Long, Map<String, Long>> getrukuListForDianshang(DateFormat df, String begindate, boolean flag, String endTimmer, long kufangid, List<Customer> customerList,
			Map<Long, Map<String, Long>> kuFangRuKuMap) {

		List<KuFangRuKuOrder> todayLogList = this.kuFangRuKuDao.getCwbByCustomeridAndEmaildate("".equals(begindate) ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00" : begindate
				+ " 00:00:00", "".equals(endTimmer) ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 23:59:59" : endTimmer + " 23:59:59", kufangid);
		if ((customerList != null) && (customerList.size() > 0)) {

			for (Customer cus : customerList) {
				long i = 0;
				Map<String, Long> logMap = new HashMap<String, Long>();
				if ((todayLogList != null) && (todayLogList.size() > 0)) {
					for (KuFangRuKuOrder branchTodayLog : todayLogList) {
						if (branchTodayLog.getCustomerid() == cus.getCustomerid()) {
							i = i + 1;
							if (!logMap.containsKey(branchTodayLog.getIntowarehousetime().substring(0, 10))) {
								i = 1;
							}
							logMap.put(branchTodayLog.getIntowarehousetime().substring(0, 10), i);
						}
					}
				}
				kuFangRuKuMap.put(cus.getCustomerid(), logMap);
			}

		}

		return kuFangRuKuMap;
	}

	private Map<Long, Map<String, Long>> getchukuListForDianshang(DateFormat df, String begindate, boolean flag, String endTimmer, long kufangid, List<Customer> customerList,
			Map<Long, Map<String, Long>> kuFangChukuMap) {

		List<DeliveryChuku> todayLogList = this.deliveryChukuDAO.getCwbByCustomeridAndEmaildate("".equals(begindate) ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00" : begindate
				+ " 00:00:00", "".equals(endTimmer) ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 23:59:59" : endTimmer + " 23:59:59", kufangid);
		if ((customerList != null) && (customerList.size() > 0)) {

			for (Customer cus : customerList) {
				long i = 0;
				Map<String, Long> logMap = new HashMap<String, Long>();
				if ((todayLogList != null) && (todayLogList.size() > 0)) {
					for (DeliveryChuku branchTodayLog : todayLogList) {
						if (branchTodayLog.getCustomerid() == cus.getCustomerid()) {
							i++;
							if (!logMap.containsKey(branchTodayLog.getOutstoreroomtime().substring(0, 10))) {
								i = 1;
							}
							logMap.put(branchTodayLog.getOutstoreroomtime().substring(0, 10), i);
						}
					}
				}
				kuFangChukuMap.put(cus.getCustomerid(), logMap);
			}

		}

		return kuFangChukuMap;
	}

	/**
	 * 获取两个时间之间的日期list
	 *
	 * @param startdate
	 * @param enddate
	 * @return
	 */
	private List<String> getDateLsit(String startdate, String enddate) {
		if (DateDayUtil.getDaycha(new SimpleDateFormat("yyyy-MM-dd").format(new Date()), enddate) > 0) {
			enddate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		}
		List<String> list = new ArrayList<String>();
		long dayCha = DateDayUtil.getDaycha(startdate, enddate);// 获取两个时间的时间差（天数）
		if (dayCha > -1) {
			for (int i = 0; i < (dayCha + 1); i++) {
				list.add(DateDayUtil.getDayCum(startdate, i));
			}
		}

		return list;
	}

	/**
	 * 投递率查询 主页面
	 *
	 * @param model
	 * @param cdDateType
	 * @param startTime
	 * @param time
	 * @return
	 */
	@RequestMapping("/deliveryRateList")
	public String deliveryRateList(Model model, HttpServletRequest request, HttpServletResponse response) {

		// user
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = this.getDmpDAO.getLogUser(dmpid);
		if ((user == null) || (user.getUserid() == 0)) {
			String dmpUrl = this.getDmpDAO.getDmpurl();
			model.addAttribute("dmpUrl", dmpUrl.substring(dmpUrl.lastIndexOf("/"), dmpUrl.length()));
			return "/common/login";
		}

		// 所有站点
		List<Branch> branchList = this.getDmpDAO.getBranchByAllZhanDian();
		branchList = this.getDmpDAO.getAccessableBranch(user.getUserid());
		model.addAttribute("branchList", branchList);

		// 所有供应商
		List<Customer> customerList = this.getDmpDAO.getAllCustomers();
		model.addAttribute("customerList", customerList);

		DeliveryRateTimeType[] allTimeTypes = DeliveryRateTimeType.values;
		model.addAttribute("allTimeTypes", allTimeTypes);

		model.addAttribute("allCdDateTypes", CustomizedDeliveryDateType.values());

		return "/deliveryRate/search";
	}

	/**
	 * 投递率查询 主页面
	 *
	 * @param model
	 * @param cdDateType
	 * @param startTime
	 * @param time
	 * @return
	 */
	@RequestMapping("/listDeliveryRateRequests")
	public String listDeliveryRateRequests(Model model, HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "tabId", required = false) Integer tabId,
			@RequestParam(value = "branchId", required = false) List<Long> branchIds, @RequestParam(value = "customerId", required = false) List<Long> customerIds,
			@RequestParam(value = "branchId3", required = false) List<Long> branchIds3, @RequestParam(value = "customerId3", required = false) List<Long> customerIds3,
			@RequestParam(value = "queryType", required = false) String queryType, @RequestParam(value = "computeType", required = false) String computeType,
			@RequestParam(value = "startDate", required = false) String startDate, @RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "timeType", required = false) List<String> timeTypes, @RequestParam(value = "timeType2", required = false) List<String> timeTypes2,
			@RequestParam(value = "timeType3", required = false) List<String> timeTypes3, @RequestParam(value = "formId", required = false) Integer formId,
			@RequestParam(value = "action", required = false) String action, @RequestParam(value = "customization", required = false) Boolean customization,
			@RequestParam(value = "cdTime", required = false) String cdTime, @RequestParam(value = "cdDateType", required = false) String cdDateType,
			@RequestParam(value = "startTime", required = false) String startTime, @RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam(value = "downloadRequestId", required = false) Long downloadRequestId) {

		// user
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = this.getDmpDAO.getLogUser(dmpid);
		if ((user == null) || (user.getUserid() == 0)) {
			String dmpUrl = this.getDmpDAO.getDmpurl();
			model.addAttribute("dmpUrl", dmpUrl.substring(dmpUrl.lastIndexOf("/"), dmpUrl.length()));
			return "/common/login";
		}

		// 用户站点权限
		if ("delete".equals(action)) {
			this.downloadManagerService.deleteDownloadManager(user.getUserid(), downloadRequestId);
		}

		if (tabId == null) {
			tabId = 1;
		}
		model.addAttribute("tabId", tabId);
		// 所有站点
		List<Branch> branchList = this.getDmpDAO.getBranchByAllZhanDian();
		branchList = this.getDmpDAO.getAccessableBranch(user.getUserid());
		model.addAttribute("branchList", branchList);

		// 所有供应商
		List<Customer> customerList = this.getDmpDAO.getAllCustomers();
		model.addAttribute("customerList", customerList);

		DeliveryRateTimeType[] allTimeTypes = DeliveryRateTimeType.values;
		model.addAttribute("allTimeTypes", allTimeTypes);

		model.addAttribute("allCdDateTypes", CustomizedDeliveryDateType.values());

		if (formId != null) {
			DeliveryRateRequest drRequest = new DeliveryRateRequest();
			if (formId == 3) {
				drRequest.setBranchIds(branchIds3);
				drRequest.setCustomerIds(customerIds3);
			} else {
				drRequest.setBranchIds(branchIds);
				drRequest.setCustomerIds(customerIds);
			}

			drRequest.setQueryType(DeliveryRateQueryType.valueOf(queryType));
			drRequest.setComputeType(DeliveryRateComputeType.valueOf(computeType));
			List<DeliveryRateTimeType> drTimeTypes = new ArrayList<DeliveryRateTimeType>();

			drRequest.setTimeTypes(drTimeTypes);

			// 是否自行指定时效
			drRequest.setCustomization(customization);
			if ((customization != null) && customization) {
				DeliveryRateTimeType cdTimeType = new DeliveryRateTimeType();
				cdTimeType.setCustomization(true);
				cdTimeType.setCdTime(cdTime);
				cdTimeType.setCdDateType(CustomizedDeliveryDateType.valueOf(cdDateType));
				cdTimeType.setDesc(cdTimeType.getCdDateType().getDesc() + cdTime);
				drTimeTypes.add(cdTimeType);

				drRequest.setStartTime(startTime);
				drRequest.setEndTime(endTime);
			} else {
				if (formId == 2) {
					timeTypes = timeTypes2;
				}
				if (formId == 3) {
					timeTypes = timeTypes3;
				}
				for (String timeType : timeTypes) {
					drTimeTypes.add(DeliveryRateTimeType.valueOf(timeType));
				}
			}
			try {
				drRequest.setStartDate(DateTimeUtil.parse(startDate, "yyyy-MM-dd"));
				drRequest.setEndDate(DateTimeUtil.nextDate(DateTimeUtil.parse(endDate, "yyyy-MM-dd")));
				this.downloadManagerService.createDeliveryRateRequest(drRequest, user.getUserid());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		// 列表已存在的查询
		List<DownloadManager> downloadRequestList = this.downloadManagerService.listDownloadRequest(user.getUserid(), ModelEnum.deliveryRate);
		List<DownloadManagerWrapper> downloadRequestWrapperList = new ArrayList<DownloadManagerWrapper>();
		for (DownloadManager download : downloadRequestList) {
			downloadRequestWrapperList.add(new DownloadManagerWrapper(download, branchList, customerList));
		}
		model.addAttribute("downloadRequestList", downloadRequestWrapperList);

		return "/deliveryRate/list";
	}

	/**
	 * 投递率查询结果页
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/deliveryRateResult")
	public String deliveryRateResult(Model model, HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "downloadRequestId", required = true) Integer downloadRequestId,
			@RequestParam(value = "tabID", required = true) int tabID) {

		// user
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = this.getDmpDAO.getLogUser(dmpid);
		if ((user == null) || (user.getUserid() == 0)) {
			String dmpUrl = this.getDmpDAO.getDmpurl();
			model.addAttribute("dmpUrl", dmpUrl.substring(dmpUrl.lastIndexOf("/"), dmpUrl.length()));
			return "/common/login";
		}

		DeliveryRateAggregation drAgg = this.downloadManagerService.getDeliveryRateResult(downloadRequestId);
		model.addAttribute("drAgg", drAgg);
		System.out.println(drAgg);

		Map<Integer, String> bocNameMap = new HashMap<Integer, String>();
		if ((tabID == 1) || (tabID == 2)) {
			if ((DeliveryRateQueryType.byBranch == drAgg.getQueryType()) || (DeliveryRateQueryType.byUser == drAgg.getQueryType())) {
				// 所有站点
				List<Branch> branchList = this.getDmpDAO.getBranchByAllZhanDian();
				for (Branch branch : branchList) {
					bocNameMap.put((int) branch.getBranchid(), branch.getBranchname());
				}
			} else {
				// 所有供应商
				List<Customer> customerList = this.getDmpDAO.getAllCustomers();
				for (Customer customer : customerList) {
					bocNameMap.put((int) customer.getCustomerid(), customer.getCustomername());
				}
			}
		}
		if (tabID == 3) {
			List<Branch> branchList = this.getDmpDAO.getBranchByAllZhanDian();
			for (Branch branch : branchList) {
				bocNameMap.put((int) branch.getBranchid(), branch.getBranchname());
			}
			// 所有供应商
			List<Customer> customerList = this.getDmpDAO.getAllCustomers();
			for (Customer customer : customerList) {
				bocNameMap.put((int) customer.getCustomerid(), customer.getCustomername());
			}
		}
		model.addAttribute("bocNameMap", bocNameMap);
		model.addAttribute("tabID", tabID);

		model.addAttribute("downloadRequestId", downloadRequestId);

		return "/deliveryRate/result";
	}

	/**
	 * 下载投递率查询 结果
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/downloadDeliveryRateResult")
	public String downloadDeliveryRateResult(Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "downloadRequestId", required = true) Integer downloadRequestId) {
		// user
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = this.getDmpDAO.getLogUser(dmpid);
		if ((user == null) || (user.getUserid() == 0)) {
			String dmpUrl = this.getDmpDAO.getDmpurl();
			model.addAttribute("dmpUrl", dmpUrl.substring(dmpUrl.lastIndexOf("/"), dmpUrl.length()));
			return "/common/login";
		}
		Integer tabId = 0;
		SXSSFWorkbook wb = this.deliveryRateService.download(user.getUserid(), downloadRequestId, tabId);
		response.setContentType("application/x-msdownload");
		try {
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("deliveryRate.xlsx", "UTF-8"));
			ServletOutputStream out = response.getOutputStream();
			wb.write(out);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping("/deliveryRateChart")
	public String deliveryRateChart(Model model, HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "bocId", required = false) String bocId,
			@RequestParam(value = "dateSeq", required = false) Integer dateSeq, @RequestParam(value = "customerid", required = false) Integer customerid,
			@RequestParam(value = "type", required = true) String type, @RequestParam(value = "downloadRequestId", required = true) Integer downloadRequestId) {
		model.addAttribute("bocId", bocId);
		model.addAttribute("type", type);
		model.addAttribute("downloadRequestId", downloadRequestId);
		DeliveryRateAggregation drAgg = this.deliveryRateService.getDeliveryRateResult(downloadRequestId);
		DeliveryRateQueryType queryType = drAgg.getQueryType();

		Map<Integer, String> bocNameMap = new HashMap<Integer, String>();
		if ((DeliveryRateQueryType.byBranch == drAgg.getQueryType()) || (DeliveryRateQueryType.byUser == drAgg.getQueryType())) {
			// 所有站点
			List<Branch> branchList = this.getDmpDAO.getBranchByAllZhanDian();
			for (Branch branch : branchList) {
				bocNameMap.put((int) branch.getBranchid(), branch.getBranchname());
			}
		} else {
			// 所有供应商
			List<Customer> customerList = this.getDmpDAO.getAllCustomers();
			for (Customer customer : customerList) {
				bocNameMap.put((int) customer.getCustomerid(), customer.getCustomername());
			}
		}
		model.addAttribute("bocNameMap", bocNameMap);
		model.addAttribute("drAgg", drAgg);
		model.addAttribute("customerid", customerid);

		Map<Integer, DeliveryRateBranchOrCustomerAggregation> bocAggMap = null;
		Map<Integer, DeliveryRateBranchOrCustomerAggregation> bocAgg = null;
		StringBuilder title = new StringBuilder();
		if (bocId != null) {
			if ("total".equals(bocId)) {
				bocAggMap = drAgg.getTotal();
				title.append("整体");
			} else {
				bocAggMap = drAgg.getBranchOrCustomerAggMap().get(Integer.parseInt(bocId));
				title.append(bocNameMap.get(Integer.valueOf(bocId)));
			}
			model.addAttribute("bocAggMap", bocAggMap);
			title.append(queryType.getChartName());
			title.append("各时效妥投率");
		} else {
			Map<String, DeliveryRateDateAggregation> dateAggMap = new HashMap<String, DeliveryRateDateAggregation>();
			if (drAgg.getAllDate().size() == dateSeq) {
				if (drAgg.getTotal().get(customerid) != null) {
					dateAggMap.put("整体", drAgg.getTotal().get(customerid).getTotal());
					for (Integer bocIdInt : drAgg.getBranchOrCustomerAggMap().keySet()) {
						bocAgg = drAgg.getBranchOrCustomerAggMap().get(bocIdInt);
						DeliveryRateDateAggregation dateAgg = bocAgg.get(customerid).getTotal();
						dateAggMap.put(bocNameMap.get(bocIdInt), dateAgg);
					}
				}
				model.addAttribute("dateAggMap", dateAggMap);
				title.append("整体各时效各站点妥投率");
			} else {
				String date = drAgg.getAllDate().get(dateSeq);
				model.addAttribute("date", date);
				title.append(date);
				if (drAgg.getTotal().get(customerid) != null) {
					dateAggMap.put("整体", drAgg.getTotal().get(customerid).getDateAggMap().get(date));
					for (Integer bocIdInt : drAgg.getBranchOrCustomerAggMap().keySet()) {
						bocAgg = drAgg.getBranchOrCustomerAggMap().get(bocIdInt);
						DeliveryRateDateAggregation dateAgg = bocAgg.get(customerid).getDateAggMap().get(date);
						dateAggMap.put(bocNameMap.get(bocIdInt), dateAgg);
					}
				}
				model.addAttribute("dateAggMap", dateAggMap);
				title.append("各时效各站点妥投率");
			}
		}

		model.addAttribute("title", title);

		return "/deliveryRate/chart";
	}

	/**
	 * 系统设置 > 信息维护 > 微信平台设置 妥投率条件设置
	 *
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/mobileDeliveryRateSetup")
	public String mobileDeliveryRateSetup(Model model, HttpServletRequest request, HttpServletResponse response) {
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = this.getDmpDAO.getLogUser(dmpid);
		if ((user == null) || (user.getUserid() == 0)) {
			String dmpUrl = this.getDmpDAO.getDmpurl();
			model.addAttribute("dmpUrl", dmpUrl.substring(dmpUrl.lastIndexOf("/"), dmpUrl.length()));
			return "/common/login";
		}

		return "/deliveryRate/mobileDeliveryRateSetup";
	}

	@RequestMapping("/listDeliveryRateCondition")
	public String listDeliveryRateCondition(Model model, HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "selectType", required = false) Integer selectType) {
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = this.getDmpDAO.getLogUser(dmpid);
		if ((user == null) || (user.getUserid() == 0)) {
			String dmpUrl = this.getDmpDAO.getDmpurl();
			model.addAttribute("dmpUrl", dmpUrl.substring(dmpUrl.lastIndexOf("/"), dmpUrl.length()));
			return "/common/login";
		}
		if (selectType == 0) {
			selectType = null;
		}

		List<Branch> branchList = this.getDmpDAO.getAccessableBranch(user.getUserid());
		model.addAttribute("branchList", branchList);

		// 所有供应商
		List<Customer> customerList = this.getDmpDAO.getAllCustomers();
		model.addAttribute("customerList", customerList);

		List<DeliveryRateCondition> deliveryRateConditionList = this.deliveryRateService.listMobileDeliveryRate(user.getUserid(), name, selectType);
		List<DeliveryRateConditionWrapper> deliveryRateConditionWrapperList = new ArrayList<DeliveryRateConditionWrapper>();
		for (DeliveryRateCondition deliveryRateCondition : deliveryRateConditionList) {
			deliveryRateConditionWrapperList.add(new DeliveryRateConditionWrapper(deliveryRateCondition, branchList, customerList));
		}
		model.addAttribute("deliveryRateConditionList", deliveryRateConditionWrapperList);

		return "/deliveryRate/mobileDeliveryRateList";
	}

	@RequestMapping("/preSaveDeliveryRateCondition")
	public String preSaveDeliveryRateCondition(Model model, HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "id", required = false) Long id) {
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = this.getDmpDAO.getLogUser(dmpid);
		if ((user == null) || (user.getUserid() == 0)) {
			String dmpUrl = this.getDmpDAO.getDmpurl();
			model.addAttribute("dmpUrl", dmpUrl.substring(dmpUrl.lastIndexOf("/"), dmpUrl.length()));
			return "/common/login";
		}

		// 用户站点权限
		// 所有站点
		List<Branch> branchList = this.getDmpDAO.getBranchByAllZhanDian();
		branchList = this.getDmpDAO.getAccessableBranch(user.getUserid());
		model.addAttribute("branchList", branchList);

		// 所有供应商
		List<Customer> customerList = this.getDmpDAO.getAllCustomers();
		model.addAttribute("customerList", customerList);

		DeliveryRateTimeType[] allTimeTypes = DeliveryRateTimeType.values;
		model.addAttribute("allTimeTypes", allTimeTypes);

		model.addAttribute("allCdDateTypes", CustomizedDeliveryDateType.values());

		DeliveryRateComputeType.values();
		if (id != null) {
			// 修改
			DeliveryRateCondition deliveryRateCondition = this.deliveryRateService.getDeliveryRateCondition(id);
			model.addAttribute("deliveryRateCondition", deliveryRateCondition);

			try {
				DeliveryRateRequest deliveryRateRequest = JsonUtil.readValue(deliveryRateCondition.getDeliveryRateRequest(), DeliveryRateRequest.class);
				model.addAttribute("deliveryRateRequest", deliveryRateRequest);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "/deliveryRate/saveMobileDeliveryRate";
	}

	@RequestMapping("/saveDeliveryRateCondition")
	public String saveDeliveryRateCondition(Model model, HttpServletRequest request, DeliveryRateCondition deliveryRateCondition, DeliveryRateRequest deliveryRateRequest,
			@RequestParam(value = "branchId", required = false) List<Long> branchIds, @RequestParam(value = "customerId", required = false) List<Long> customerIds,
			@RequestParam(value = "queryType", required = false) String queryType, @RequestParam(value = "computeType", required = false) String computeType,
			@RequestParam(value = "timeType", required = false) List<String> timeTypes, @RequestParam(value = "formId", required = false) Integer formId,
			@RequestParam(value = "action", required = false) String action, @RequestParam(value = "customization", required = false) Boolean customization,
			@RequestParam(value = "cdTime", required = false) String cdTime, @RequestParam(value = "cdDateType", required = false) String cdDateType,
			@RequestParam(value = "startTime", required = false) String startTime, @RequestParam(value = "endTime", required = false) String endTime) {
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		User user = this.getDmpDAO.getLogUser(dmpid);
		if ((user == null) || (user.getUserid() == 0)) {
			String dmpUrl = this.getDmpDAO.getDmpurl();
			model.addAttribute("dmpUrl", dmpUrl.substring(dmpUrl.lastIndexOf("/"), dmpUrl.length()));
			return "/common/login";
		}
		deliveryRateCondition.setUserId(user.getUserid());
		deliveryRateCondition.setStatus(Constants.DELIVERY_RATE_CONDITION_STATUS_VALID);

		deliveryRateRequest.setBranchIds(branchIds);
		deliveryRateRequest.setCustomerIds(customerIds);

		deliveryRateRequest.setQueryType(DeliveryRateQueryType.valueOf(queryType));
		deliveryRateRequest.setComputeType(DeliveryRateComputeType.valueOf(computeType));
		List<DeliveryRateTimeType> drTimeTypes = new ArrayList<DeliveryRateTimeType>();

		deliveryRateRequest.setTimeTypes(drTimeTypes);

		// 是否自行指定时效
		deliveryRateRequest.setCustomization(customization);
		if ((customization != null) && customization) {
			DeliveryRateTimeType cdTimeType = new DeliveryRateTimeType();
			cdTimeType.setCustomization(true);
			cdTimeType.setCdTime(cdTime);
			cdTimeType.setCdDateType(CustomizedDeliveryDateType.valueOf(cdDateType));
			cdTimeType.setDesc(cdTimeType.getCdDateType().getDesc() + cdTime);
			drTimeTypes.add(cdTimeType);

			deliveryRateRequest.setStartTime(startTime);
			deliveryRateRequest.setEndTime(endTime);
		} else {
			for (String timeType : timeTypes) {
				drTimeTypes.add(DeliveryRateTimeType.valueOf(timeType));
			}
		}
		try {
			deliveryRateCondition.setDeliveryRateRequest(JsonUtil.translateToJson(deliveryRateRequest));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.deliveryRateService.saveDeliveryRateCondition(deliveryRateCondition);

		return "/deliveryRate/saveMobileDeliveryRate";
	}

	@RequestMapping("/deactiveDeliveryRateCondition")
	public String deactiveDeliveryRateCondition(Model model, HttpServletRequest request, @RequestParam(value = "id", required = true) Long id) {
		this.deliveryRateService.deactiveDeliveryRateCondition(id);
		return "/common/httpCommunicationResult";
	}

	@RequestMapping("/activeDeliveryRateCondition")
	public String activeDeliveryRateCondition(Model model, HttpServletRequest request, @RequestParam(value = "id", required = true) Long id) {
		this.deliveryRateService.activeDeliveryRateCondition(id);
		return "/common/httpCommunicationResult";
	}

	/**
	 * 微信平台接口 根据用户id查询
	 *
	 * @param model
	 * @param request
	 * @param response
	 * @param userId
	 * @return
	 */
	@RequestMapping("/listMobileDeliveryRateCondition")
	public @ResponseBody String listMobileDeliveryRateCondition(Model model, HttpServletRequest request, HttpServletResponse response) {
		List<DeliveryRateCondition> deliveryRateConditionList = this.deliveryRateService.listMobileDeliveryRate(null, null, null);
		String json = null;
		try {
			json = JsonUtil.translateToJson(deliveryRateConditionList);
		} catch (Exception e) {
			this.logger.error("translate to json failed for {}", deliveryRateConditionList, e);
		}
		return json;
	}

	/**
	 * 微信平台接口，查询妥投率查询执行结果
	 *
	 * @param model
	 * @param request
	 * @param response
	 * @param ruleId
	 *            规则id
	 * @param date
	 *            执行日期
	 * @return
	 */
	@RequestMapping("/queryMobileDeliveryRate")
	public @ResponseBody String queryMobileDeliveryRate(Model model, HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "ruleId", required = true) Long ruleId) {
		String deliveryRateAgg = null;
		try {
			deliveryRateAgg = this.deliveryRateService.getMobileDeliveryRate(ruleId);
		} catch (Exception e) {
			this.logger.error("queryMobileDeliveryRate failed ", e);
		}
		return deliveryRateAgg;
	}

}