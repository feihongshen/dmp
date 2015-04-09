package cn.explink.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.codehaus.jackson.map.ObjectMapper;
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

import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.dao.AccountCwbFareDetailDAO;
import cn.explink.dao.AppearWindowDao;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.EditCwbDAO;
import cn.explink.dao.EmailDateDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.UserDAO;
import cn.explink.dao.searchEditCwbInfoDao;
import cn.explink.domain.AccountCwbFareDetail;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.EdtiCwb_DeliveryStateDetail;
import cn.explink.domain.EmailDate;
import cn.explink.domain.SearcheditInfo;
import cn.explink.domain.User;
import cn.explink.domain.WindowShow;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.EditCwbTypeEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.exception.ExplinkException;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.service.AdjustmentRecordService;
import cn.explink.service.CwbOrderService;
import cn.explink.service.CwbOrderWithDeliveryState;
import cn.explink.service.DataImportService;
import cn.explink.service.EditCwbService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.service.OrgBillAdjustmentRecordService;
import cn.explink.service.addressmatch.AddressMatchService;
import cn.explink.support.transcwb.TransCwbDao;
import cn.explink.util.ExcelUtils;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@RequestMapping("/editcwb")
@Controller
public class EditCwbController {
	@Autowired
	AddressMatchService addressMatchService;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	ExportService exportService;
	@Autowired
	AppearWindowDao appearWindowDao;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	TransCwbDao transCwbDao;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	CwbOrderService cwborderService;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	DataImportService dataImportService;
	@Autowired
	EditCwbService editCwbService;
	@Produce(uri = "jms:topic:addressmatch")
	ProducerTemplate addressmatch;
	@Autowired
	EditCwbDAO editCwbDAO;
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	EmailDateDAO emaildateDAO;

	@Autowired
	searchEditCwbInfoDao cwbInfoDao;
	@Autowired
	AccountCwbFareDetailDAO accountCwbFareDetailDAO;
	
	@Autowired
	AdjustmentRecordService adjustmentRecordService;
	@Autowired
	OrgBillAdjustmentRecordService orgBillAdjustmentRecordService;
	

	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	private Logger logger = LoggerFactory.getLogger(EditCwbController.class);

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	protected static ObjectMapper jacksonmapper = JacksonMapper.getInstance();

	@RequestMapping("/start")
	public String start(Model model, @RequestParam(value = "cwbs", required = false, defaultValue = "") String cwbs, @RequestParam(value = "type", required = false, defaultValue = "0") int type) {
		logger.info("淇敼璁㈠崟鍔熻兘 [" + type + "][{}] cwb: {}", getSessionUser().getRealname(), cwbs);
		// 鏁寸悊sql瑕佽鍙栫殑cwb start
		String[] cwbArray = cwbs.trim().split("\r\n");
		String s = "'";
		String s1 = "',";
		StringBuffer cwbsSqlBuffer = new StringBuffer();
		for (String cwb : cwbArray) {
			if (cwb.trim().length() == 0) {
				continue;
			}
			if (cwb.length() > 0) {
				cwbsSqlBuffer = cwbsSqlBuffer.append(s).append(cwb).append(s1);
			}
		}
		if (cwbsSqlBuffer.length() == 0) {
			return "editcwb/start";
		}
		// 鏁寸悊sql瑕佽鍙栫殑cwb end
		model.addAttribute("cwbArray", cwbArray);
		String cwbsSql = cwbsSqlBuffer.substring(0, cwbsSqlBuffer.length() - 1);
		List<CwbOrder> cwbList = cwbDAO.getCwbByCwbs(cwbsSql);
		Map<String, AccountCwbFareDetail> accountCwbFareDetailMap = accountCwbFareDetailDAO.getAccountCwbFareDetailMapByCwbs(cwbsSql);

		// 鍋氶噸缃鏍哥姸鎬佹洿鏀圭殑鎿嶄綔 start
		if (type == EditCwbTypeEnum.ChongZhiShenHeZhuangTai.getValue()) {
			List<CwbOrder> allowCwb = new ArrayList<CwbOrder>();// 鍏佽鏇存敼璁㈠崟
			List<CwbOrder> prohibitedCwb = new ArrayList<CwbOrder>(); // 绂佹鏇存敼鐨勮鍗�
			for (CwbOrder co : cwbList) {

				// 鍒ゆ柇璁㈠崟褰撳墠鐘舵�涓�6 宸插鏍哥姸鎬佺殑璁㈠崟鎵嶈兘閲嶇疆瀹℃牳鐘舵�
				if (co.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) {
					// 鍒ゆ柇璁㈠崟鍙锋槸鍚︿负POS鍒峰崱 posremark=POS鍒峰崱 POS鍒峰崱鐨勮鍗曚笉鍏佽閲嶇疆瀹℃牳鐘舵�
					DeliveryState ds = deliveryStateDAO.getDeliveryStateByCwb(co.getCwb()).get(0);
					if (co.getInfactfare().compareTo(BigDecimal.ZERO) > 0 && (accountCwbFareDetailMap.get(co.getCwb()) == null ? 0 : accountCwbFareDetailMap.get(co.getCwb()).getFareid()) > 0) {
						// 鏆傚�瀵硅薄涓殑澶囨敞1瀛楁杈撳嚭涓�簺鎻愮ず璇�
						co.setRemark1("褰撳墠璁㈠崟杩愯垂宸蹭氦娆撅紝涓嶅彲閲嶇疆瀹℃牳鐘舵�");
						prohibitedCwb.add(co);
					} else if (ds.getPosremark().indexOf("POS鍒峰崱") == -1) {
						allowCwb.add(co);
					} else {
						// 鏆傚�瀵硅薄涓殑澶囨敞1瀛楁杈撳嚭涓�簺鎻愮ず璇�
						co.setRemark1("POS鍒峰崱绛炬敹鐨勮鍗曞鏍稿悗涓嶅厑璁搁噸缃鏍哥姸鎬�);
						prohibitedCwb.add(co);
					}
				} else {
					// 鏆傚�瀵硅薄涓殑澶囨敞1瀛楁杈撳嚭涓�簺鎻愮ず璇�
					co.setRemark1("褰撳墠璁㈠崟鐘舵�涓篬" + FlowOrderTypeEnum.getText(co.getFlowordertype()).getText() + "],涓嶅厑璁搁噸缃鏍哥姸鎬�);
					prohibitedCwb.add(co);
				}

			}
			model.addAttribute("allowCwb", allowCwb);
			model.addAttribute("prohibitedCwb", prohibitedCwb);
			return "editcwb/ChongZhiShenHe";
			// 鍋氶噸缃鏍哥姸鎬佹洿鏀圭殑鎿嶄綔 end
		} else if (type == EditCwbTypeEnum.XiuGaiJinE.getValue()) {// 淇敼璁㈠崟閲戦鏇存敼鎿嶄綔
																	// Start
			List<CwbOrderWithDeliveryState> allowCods = new ArrayList<CwbOrderWithDeliveryState>();
			for (CwbOrder co : cwbList) {
				CwbOrderWithDeliveryState cods = new CwbOrderWithDeliveryState();
				cods.setCwbOrder(co);
				cods.setDeliveryState(deliveryStateDAO.getDeliveryByCwbAndDeliverystate(co.getCwb()));
				// 瀛樺偍璁㈠崟琛ㄨ褰曞拰鍙嶉琛ㄨ褰曪紝鐢ㄤ簬鍓嶇鍒ゆ柇
				allowCods.add(cods);
			}
			model.addAttribute("allowCods", allowCods);
			return "editcwb/XiuGaiJinE";
			// 淇敼璁㈠崟閲戦鏇存敼鎿嶄綔 end
		} else if (type == EditCwbTypeEnum.XiuGaiZhiFuFangShi.getValue()) {// 淇敼璁㈠崟鏀粯鏂瑰紡鏇存敼鎿嶄綔
																			// Start
			List<CwbOrderWithDeliveryState> allowCods = new ArrayList<CwbOrderWithDeliveryState>();
			List<CwbOrderWithDeliveryState> prohibitedCods = new ArrayList<CwbOrderWithDeliveryState>();
			for (CwbOrder co : cwbList) {
				CwbOrderWithDeliveryState cods = new CwbOrderWithDeliveryState();
				cods.setCwbOrder(co);
				cods.setDeliveryState(deliveryStateDAO.getDeliveryByCwbAndDeliverystate(co.getCwb()));
				// 瀛樺偍璁㈠崟琛ㄨ褰曞拰鍙嶉琛ㄨ褰曪紝鐢ㄤ簬鍓嶇鍒ゆ柇 濡傛灉浠ｆ敹閲戦
				if (co.getReceivablefee().compareTo(BigDecimal.ZERO) <= 0) {
					prohibitedCods.add(cods);
				} else {
					allowCods.add(cods);
				}
			}
			model.addAttribute("allowCods", allowCods);
			model.addAttribute("prohibitedCods", prohibitedCods);
			return "editcwb/XiuGaiZhiFuFangShi";
			// 淇敼璁㈠崟鏀粯鏂瑰紡鏇存敼鎿嶄綔 end
		} else if (type == EditCwbTypeEnum.XiuGaiDingDanLeiXing.getValue()) {// 淇敼璁㈠崟绫诲瀷鏇存敼鎿嶄綔
																				// Start
			List<CwbOrderWithDeliveryState> allowCods = new ArrayList<CwbOrderWithDeliveryState>();
			List<CwbOrderWithDeliveryState> prohibitedCods = new ArrayList<CwbOrderWithDeliveryState>();
			for (CwbOrder co : cwbList) {
				CwbOrderWithDeliveryState cods = new CwbOrderWithDeliveryState();
				cods.setCwbOrder(co);
				cods.setDeliveryState(deliveryStateDAO.getDeliveryByCwbAndDeliverystate(co.getCwb()));
				// 宸茬粡褰掔彮鐨勮鍗曚笉鑳戒慨鏀硅鍗曠被鍨嬶紝蹇呴』鍏呭�瀹℃牳鐘舵�鎵嶈兘淇敼
				if (cods.getDeliveryState() != null && cods.getDeliveryState().getGcaid() > 0) {
					cods.setError("宸插鏍哥殑璁㈠崟涓嶅厑璁镐慨鏀硅鍗曠被鍨嬶紝鑻ヨ淇敼锛岃閲嶇疆瀹℃牳鐘舵�鍚庡啀璇�);
					prohibitedCods.add(cods);
				} else if (cods.getDeliveryState() != null && co.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()
						&& cods.getDeliveryState().getInfactfare().compareTo(BigDecimal.ZERO) > 0) {
					cods.setError("涓婇棬閫�湁搴旀敹杩愯垂鐨勮鍗曚笉鍏佽淇敼璁㈠崟绫诲瀷");
					prohibitedCods.add(cods);
				} else {
					allowCods.add(cods);
				}
			}
			model.addAttribute("allowCods", allowCods);
			model.addAttribute("prohibitedCods", prohibitedCods);
			return "editcwb/XiuGaiDingDanLeiXing";
			// 淇敼璁㈠崟璁㈠崟绫诲瀷鏇存敼鎿嶄綔 end
		}

		// 濡傛灉涓嶅睘浜庝换浣曟搷浣�鍒欏洖鍒板紑濮嬮〉闈�
		return "editcwb/start";
	}

	@RequestMapping("/getUserForAutoComplete")
	public @ResponseBody List<JSONObject> getUserForAutoComplete(@RequestParam(value = "username", required = false, defaultValue = "") String username) {
		List<User> userList = new ArrayList<User>();
		List<JSONObject> jsonlist = new ArrayList<JSONObject>();
		if (username.length() > 0) {
			userList = userDAO.getUserForAutoComplete(username);

		}
		if (userList.size() > 0) {
			for (User user : userList) {
				JSONObject obj = new JSONObject();
				obj.put("userid", user.getUserid());
				obj.put("username", user.getRealname());
				jsonlist.add(obj);
			}
		}
		return jsonlist;
	}

	@RequestMapping("/editChongZhiShenHeZhuangTai")
	public String editChongZhiShenHeZhuangTai(Model model, @RequestParam(value = "requestUser", required = false, defaultValue = "0") Long requestUser,
			@RequestParam(value = "cwbs", required = false, defaultValue = "") String[] cwbs) {
		List<User> userList = userDAO.getUserByid(requestUser);
		if (userList != null) {
			logger.info("閲嶇疆璁㈠崟瀹℃牳鐘舵�鍔熻兘 [{}] cwb: {}", getSessionUser().getRealname(), StringUtil.getStringsToString(cwbs));
			List<EdtiCwb_DeliveryStateDetail> ecList = new ArrayList<EdtiCwb_DeliveryStateDetail>();
			List<String> errorList = new ArrayList<String>();
			for (String cwb : cwbs) {
				try {
					EdtiCwb_DeliveryStateDetail ec_dsd = editCwbService.analysisAndSaveByChongZhiShenHe(cwb, requestUser, getSessionUser().getUserid());
					ecList.add(ec_dsd);
				} catch (ExplinkException ee) {
					errorList.add(cwb + "_" + ee.getMessage());
				} catch (Exception e) {
					errorList.add(cwb + "_" + FlowOrderTypeEnum.YiShenHe.getValue() + "_绯荤粺鍐呴儴鎶ラ敊锛�);
					e.printStackTrace();
				}
			}
			model.addAttribute("ecList", ecList);
			model.addAttribute("errorList", errorList);

		}
		return "editcwb/ChongZhiShenHeResult";
	}

	@RequestMapping("/ChongZhiShenHeZhuangTai")
	public String ChongZhiShenHeZhuangTai(Model model, @RequestParam(value = "cwbs", required = false, defaultValue = "") String[] cwbs) {
		logger.info("閲嶇疆璁㈠崟瀹℃牳鐘舵�鍔熻兘 [{}] cwb: {}", getSessionUser().getRealname(), StringUtil.getStringsToString(cwbs));
		List<EdtiCwb_DeliveryStateDetail> ecList = new ArrayList<EdtiCwb_DeliveryStateDetail>();
		List<String> errorList = new ArrayList<String>();
		for (String cwb : cwbs) {
			try {
				EdtiCwb_DeliveryStateDetail ec_dsd = editCwbService.analysisAndSaveByChongZhiShenHe(cwb, (long) 0, getSessionUser().getUserid());
				ecList.add(ec_dsd);
			} catch (ExplinkException ee) {
				errorList.add(cwb + "_" + FlowOrderTypeEnum.YiShenHe.getValue() + "_" + ee.getMessage());
			} catch (Exception e) {
				errorList.add(cwb + "_" + FlowOrderTypeEnum.YiShenHe.getValue() + "_绯荤粺鍐呴儴鎶ラ敊锛�);
				e.printStackTrace();
			}
		}
		model.addAttribute("ecList", ecList);
		model.addAttribute("errorList", errorList);

		return "editcwb/chongzhiResult";
	}

	@RequestMapping("/editXiuGaiJinE")
	public String editXiuGaiJinE(Model model, HttpServletRequest request, @RequestParam(value = "requestUser", required = true, defaultValue = "0") Long requestUser,
			@RequestParam(value = "cwbs", required = false, defaultValue = "") String[] cwbs) {
		List<User> userList = userDAO.getUserByid(requestUser);
		if (userList.size() > 0) {
			logger.info("淇敼璁㈠崟閲戦鍔熻兘 [{}] cwb: {}", getSessionUser().getRealname(), StringUtil.getStringsToString(cwbs));
			List<EdtiCwb_DeliveryStateDetail> ecList = new ArrayList<EdtiCwb_DeliveryStateDetail>();
			List<String> errorList = new ArrayList<String>();
			for (String cwb : cwbs) {
				String isDeliveryState = request.getParameter("isDeliveryState_" + cwb);
				BigDecimal Receivablefee = request.getParameter("Receivablefee_" + cwb) == null ? BigDecimal.ZERO : new BigDecimal(request.getParameter("Receivablefee_" + cwb));
				BigDecimal cash = request.getParameter("Receivablefee_cash_" + cwb) == null ? BigDecimal.ZERO : new BigDecimal(request.getParameter("Receivablefee_cash_" + cwb));
				BigDecimal pos = request.getParameter("Receivablefee_pos_" + cwb) == null ? BigDecimal.ZERO : new BigDecimal(request.getParameter("Receivablefee_pos_" + cwb));
				BigDecimal checkfee = request.getParameter("Receivablefee_checkfee_" + cwb) == null ? BigDecimal.ZERO : new BigDecimal(request.getParameter("Receivablefee_checkfee_" + cwb));
				BigDecimal otherfee = request.getParameter("Receivablefee_otherfee_" + cwb) == null ? BigDecimal.ZERO : new BigDecimal(request.getParameter("Receivablefee_otherfee_" + cwb));
				BigDecimal Paybackfee = request.getParameter("Paybackfee_" + cwb) == null ? BigDecimal.ZERO : new BigDecimal(request.getParameter("Paybackfee_" + cwb));
				//added by jiangyu  begin
				CwbOrder cwbOrder = new CwbOrder();
				cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
				User user = new User();
				if (userList.size() > 0) {
					user = userList.get(0);
				}

				// 先判断是有账单 获取到修改订单金额的值,进行判断插入到数据库中
				if ((receivablefee != null) && !receivablefee.equals(cwbOrder.getReceivablefee())) {
//					this.adjustmentRecordService.createAdjustmentRecode(cwb, cwbOrder.getCustomerid(), cwbOrder.getReceivablefee(), paybackfee, receivablefee, "", user.getUsername(),cwbOrder.getCwbordertypeid());
					//客户调整单逻辑入口
					this.adjustmentRecordService.processAdjusRecordByMoney(cwbOrder, paybackfee, receivablefee, "", user.getUsername());
					//站内调整单逻辑入口
					this.orgBillAdjustmentRecordService.createOrgBillAdjustRecord(cwbOrder,user,receivablefee,paybackfee);
				}
				//added by jiangyu  end
				
				try {
					EdtiCwb_DeliveryStateDetail ec_dsd = editCwbService.analysisAndSaveByXiuGaiJinE(cwb, isDeliveryState, Receivablefee, cash, pos, checkfee, otherfee, Paybackfee, requestUser,
							getSessionUser().getUserid());
					ecList.add(ec_dsd);
				} catch (ExplinkException ee) {
					errorList.add(cwb + "_" + ee.getMessage());
				} catch (Exception e) {
					errorList.add(cwb + "_" + FlowOrderTypeEnum.YiShenHe.getValue() + "_绯荤粺鍐呴儴鎶ラ敊锛�);
					e.printStackTrace();
				}
			}
			model.addAttribute("ecList", ecList);
			model.addAttribute("errorList", errorList);

		}
		return "editcwb/XiuGaiJinEResult";
	}

	@RequestMapping("/editXiuGaiZhiFuFangShi")
	public String editXiuGaiZhiFuFangShi(Model model, HttpServletRequest request, @RequestParam(value = "requestUser", required = false, defaultValue = "0") Long requestUser,
			@RequestParam(value = "cwbs", required = false, defaultValue = "") String[] cwbs) {
		List<User> userList = userDAO.getUserByid(requestUser);
		if (userList.size() > 0) {
			logger.info("淇敼璁㈠崟鏀粯鏂瑰紡鍔熻兘 [{}] cwb: {}", getSessionUser().getRealname(), StringUtil.getStringsToString(cwbs));
			List<EdtiCwb_DeliveryStateDetail> ecList = new ArrayList<EdtiCwb_DeliveryStateDetail>();
			List<String> errorList = new ArrayList<String>();
			for (String cwb : cwbs) {
				int paywayid = request.getParameter("paywayid_" + cwb) == null ? 0 : Integer.valueOf(request.getParameter("paywayid_" + cwb));
				int newpaywayid = request.getParameter("Newpaywayid_" + cwb) == null ? 0 : Integer.valueOf(request.getParameter("Newpaywayid_" + cwb));
				try {
					EdtiCwb_DeliveryStateDetail ec_dsd = editCwbService.analysisAndSaveByXiuGaiZhiFuFangShi(cwb, paywayid, newpaywayid, requestUser, getSessionUser().getUserid());
					//added by jiangyu begin
					adjustmentRecordService.createAdjustmentRecordByPayType(cwb, paywayid, newpaywayid);
					orgBillAdjustmentRecordService.createAdjustmentRecordByPayType(cwb,paywayid,newpaywayid);
					//修改支付方式,判断是否生成调整单
					//added by jiangyu end
					ecList.add(ec_dsd);
				} catch (ExplinkException ee) {
					errorList.add(cwb + "_" + ee.getMessage());
				} catch (Exception e) {
					errorList.add(cwb + "_涓嶇‘瀹歘绯荤粺鍐呴儴鎶ラ敊锛�);
					e.printStackTrace();
				}
			}
			model.addAttribute("ecList", ecList);
			model.addAttribute("errorList", errorList);

		}
		return "editcwb/XiuGaiZhiFuFangShiResult";
	}

	@RequestMapping("/editXiuGaiDingDanLeiXing")
	public String editXiuGaiDingDanLeiXing(Model model, HttpServletRequest request, @RequestParam(value = "requestUser", required = false, defaultValue = "0") Long requestUser,
			@RequestParam(value = "cwbs", required = false, defaultValue = "") String[] cwbs) {
		List<User> userList = userDAO.getUserByid(requestUser);
		if (userList.size() > 0) {
			logger.info("淇敼璁㈠崟绫诲瀷鍔熻兘 [{}] cwb: {}", getSessionUser().getRealname(), StringUtil.getStringsToString(cwbs));
			List<EdtiCwb_DeliveryStateDetail> ecList = new ArrayList<EdtiCwb_DeliveryStateDetail>();
			List<String> errorList = new ArrayList<String>();
			for (String cwb : cwbs) {
				int cwbordertypeid = request.getParameter("cwbordertypeid_" + cwb) == null ? 0 : Integer.valueOf(request.getParameter("cwbordertypeid_" + cwb));
				int Newcwbordertypeid = request.getParameter("Newcwbordertypeid_" + cwb) == null ? 0 : Integer.valueOf(request.getParameter("Newcwbordertypeid_" + cwb));
				try {
					EdtiCwb_DeliveryStateDetail ec_dsd = editCwbService.analysisAndSaveByXiuGaiDingDanLeiXing(cwb, cwbordertypeid, Newcwbordertypeid, requestUser, getSessionUser().getUserid());
					ecList.add(ec_dsd);
				} catch (ExplinkException ee) {
					errorList.add(cwb + "_" + ee.getMessage());
				} catch (Exception e) {
					errorList.add(cwb + "_涓嶇‘瀹歘绯荤粺鍐呴儴鎶ラ敊锛�);
					e.printStackTrace();
				}
			}
			model.addAttribute("ecList", ecList);
			model.addAttribute("errorList", errorList);

		}
		return "editcwb/XiuGaiDingDanLeiXingResult";
	}

	/**
	 * 杩斿洖淇敼璁㈠崟鏄庣粏鍒楄〃
	 * 
	 * @param model
	 * @param fd_payup_detail_id
	 *            灏忎欢鍛樹氦娆惧鏍歌〃finance_deliver_pay_up_detail鐨�id
	 * @param f_payup_audit_id
	 *            绔欑偣浜ゆ瀹℃牳琛╢inance_pay_up_audit 鐨刬d
	 * @param finance_audit_id
	 *            涓庝緵璐у晢缁撶畻瀹℃牳琛╢inance_audit 鐨刬d
	 * @param payupid
	 *            绔欑偣浜ゆ琛�express_ops_pay_up id
	 * @param gcaid
	 *            褰掔彮琛�express_ops_goto_class_auditing id
	 * @return
	 */
	@RequestMapping("/getList")
	public String getList(Model model, @RequestParam(value = "fd_payup_detail_id", required = false, defaultValue = "0") Long fd_payup_detail_id,
			@RequestParam(value = "f_payup_audit_id", required = false, defaultValue = "0") Long f_payup_audit_id,
			@RequestParam(value = "finance_audit_id", required = false, defaultValue = "0") Long finance_audit_id,
			@RequestParam(value = "payupid", required = false, defaultValue = "") String payupids, @RequestParam(value = "gcaid", required = false, defaultValue = "0") Long gcaid) {
		model.addAttribute("userList", userDAO.getAllUserByuserDeleteFlag());
		if (fd_payup_detail_id > 0) {
			model.addAttribute("ecList", editCwbDAO.getEditCwbListByFdPayupDetailId(fd_payup_detail_id));
		} else if (finance_audit_id > 0) {
			model.addAttribute("ecList", editCwbDAO.getEditCwbListByFinanceAuditId(finance_audit_id));
		} else if (f_payup_audit_id > 0) {
			model.addAttribute("ecList", editCwbDAO.getEditCwbListByFPayupAuditId(f_payup_audit_id));
		} else if (gcaid > 0) {
			model.addAttribute("ecList", editCwbDAO.getEditCwbListByGcaid(gcaid));
		} else if (payupids.length() > 0) {
			List<EdtiCwb_DeliveryStateDetail> ecList = new ArrayList<EdtiCwb_DeliveryStateDetail>();
			for (String payupid : payupids.split(",")) {
				ecList.addAll(editCwbDAO.getEditCwbListByPayupid(payupid));
			}

			model.addAttribute("ecList", ecList);
		}

		return "editcwb/list";
	}

	/**
	 * 璁㈠崟淇℃伅淇敼
	 */
	@RequestMapping("/editCwbInfo")
	public String editCwbInfo(Model model, @RequestParam(value = "cwb", required = false, defaultValue = "") String cwb,
			@RequestParam(value = "isshow", defaultValue = "0", required = false) long isshow // 鏄惁鏄剧ず,
	) {

		if (isshow > 0) {// 鏌ヨ
			List<CwbOrder> cwborderlist = new ArrayList<CwbOrder>();

			for (String cwbStr : cwb.split("\r\n")) {
				if (cwbStr.trim().length() == 0) {
					continue;
				}
				CwbOrder co = cwbDAO.getCwbByCwb(cwbStr);
				if (co != null) {
					cwborderlist.add(co);
				}
			}
			model.addAttribute("cwbList", cwborderlist);
		}
		return "editcwb/editInfo";

	}

	@RequestMapping("/searchCwbInfo/{cwb}")
	public @ResponseBody String searchCwbInfo(Model model, @PathVariable("cwb") String cwb, @RequestParam(value = "editname", required = false, defaultValue = "") String editname,
			@RequestParam(value = "editmobile", required = false, defaultValue = "0") Long editmobile, @RequestParam(value = "editcommand", defaultValue = "", required = false) String editcommand, // 鏄惁鏄剧ず,
			@RequestParam(value = "editshow", defaultValue = "0", required = false) long editshow, // 鏄惁鏄剧ず,
			@RequestParam(value = "editaddress", required = false, defaultValue = "") String editaddress) {
		cwb = cwb.trim();
		CwbOrderDTO co = dataImportDAO_B2c.getCwbFromCwborder(cwb);// 杩愬崟鍙�
		// 鍒犻櫎璁㈠崟锛岀劧鍚巌nsert杩涙潵
		if (co == null) {
			return "{\"errorCode\":1,\"error\":\"璁㈠崟鍙蜂笉瀛樺湪\"}";
		}
		if (orderFlowDAO.getOrderFlowByCwbAndFlowordertype(FlowOrderTypeEnum.FenZhanLingHuo.getValue(), cwb).size() > 0) {
			return "{\"errorCode\":1,\"error\":\"棰嗚揣鐨勪笉璁镐慨鏀瑰湴鍧�"}";
		}
		return "{\"errorCode\":0,\"error\":\"淇敼\"}";
	}

	@RequestMapping("/updateCwbInfo/{cwb}")
	public @ResponseBody String updateCwbInfo(Model model, @PathVariable("cwb") String cwb, @RequestParam(value = "editname", required = false, defaultValue = "") String editname,// 淇敼鐨勫鍚�
			@RequestParam(value = "editmobile", required = false, defaultValue = "") String editmobile,// 淇敼鐨勭數璇�
			@RequestParam(value = "editcommand", defaultValue = "", required = false) String editcommand, // 闇�眰
			@RequestParam(value = "editshow", defaultValue = "0", required = false) long editshow, // 鏄惁鏄剧ず,
			@RequestParam(value = "remark", defaultValue = "", required = false) String remark, // 璁㈠崟澶囨敞
			@RequestParam(value = "begindate", defaultValue = "", required = false) String begindate, @RequestParam(value = "editaddress", required = false, defaultValue = "") String editaddress) {// 鍦板潃
		// 1.淇敼鍚庣殑淇℃伅璧嬪�
		final ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		cwb = cwb.trim();
		logger.info("淇敼璁㈠崟鍙凤細{}寮�,editname" + editname + "editmobile" + editmobile + "editcommand" + editcommand + "editaddress" + editaddress, cwb);
		CwbOrder old = cwbDAO.getCwbByCwb(cwb);
		// 鍒犻櫎鍚庢柊澧烇紝鎻掑叆鏂板鏌ヨ琛ㄤ腑
		cwbInfoDao.deleteEditInfo(cwb);
		cwbInfoDao.createEditInfo(old, editname, editmobile, editcommand, editaddress, begindate, userDetail.getUser().getUserid(), remark);
		// 鏋勫缓鏂扮殑璁㈠崟淇℃伅
		CwbOrderDTO co = dataImportDAO_B2c.getCwbFromCwborder(cwb);// 杩愬崟鍙�
		co.setConsigneename(editname);
		co.setCustomercommand(editcommand);
		co.setConsigneemobile(editmobile);
		co.setConsigneeaddress(editaddress);
		co.setCwbremark(remark);
		// 2.鏇存柊鍒颁富琛�
		EmailDate ed = dataImportService.getEmailDate_B2CByEmaildate(co.getCustomerid(), co.getCustomerwarehouseid(), co.getCustomerwarehouseid(), co.getEmaildate());
		userDetail.getUser().setBranchid(Long.valueOf(ed.getWarehouseid()));
		emaildateDAO.editEditEmaildateForCwbcountAdd(ed.getEmaildateid());
		cwbOrderService.updateExcelCwb(co, co.getCustomerid(), ed.getWarehouseid(), userDetail.getUser(), ed, true);
		// 3.鍖归厤鍦板潃搴�
		try {
			co.setExcelbranch(null);
			if (co.getExcelbranch() == null || co.getExcelbranch().length() == 0 || co.getDeliverybranchid() == 0) {
				logger.info("鍦板潃搴�------");
				if (!old.getConsigneeaddress().equals(co.getConsigneeaddress())) {
					addressMatchService.matchAddress(getSessionUser().getUserid(), co.getCwb());
				}
			}
			// 4.鎴愬姛鍚庢彃鍏ユ秷鎭〃express_ops_window

			List<User> userlist = userDAO.getAllUserbybranchid(old.getDeliverybranchid() == 0 ? old.getNextbranchid() : old.getDeliverybranchid());
			if (userlist != null && userlist.size() > 0) {

				String jsonInfo = co.getCwb();
				WindowShow a = appearWindowDao.getObjectWindowByState(userlist.get(0).getUserid());
				if (a != null) {// 瀛樺湪,update
					logger.info("鏇存柊瀹氭椂鍣ㄨ〃 --璁㈠崟鍙穥}锛岀被鍨嬩负2锛岀敤鎴蜂负{}", co.getCwb(), userlist.get(0).getUserid());
					appearWindowDao.updateByStateAndUserid(a.getJsoninfo() + "," + jsonInfo, userlist.get(0).getUserid());
				} else {
					logger.info("鏂板瀹氭椂鍣ㄨ〃 --璁㈠崟鍙穥}锛岀被鍨嬩负2锛岀敤鎴蜂负{}", co.getCwb(), userlist.get(0).getUserid());
					appearWindowDao.creWindowTime(jsonInfo, "2", userlist.get(0).getUserid(), "1");
				}
			}
			return "{\"errorCode\":0,\"error\":\"淇敼鎴愬姛\"}";
		} catch (Exception e) {
			logger.error("璋冪敤鍦板潃搴撳紓甯�, e);
			e.printStackTrace();
			return "{\"errorCode\":1,\"error\":\"澶辫触锛氳皟鐢ㄥ湴鍧�簱寮傚父\"}";
		}
	}

	@RequestMapping("/toSearchCwb/{page}")
	public String toSerchCwb(Model model, @PathVariable("page") long page, @RequestParam(value = "begindate", required = false, defaultValue = "") String begindate,
			@RequestParam(value = "enddate", defaultValue = "", required = false) String endtime, // 鏄惁鏄剧ず,
			@RequestParam(value = "isshow", defaultValue = "0", required = false) long isshow // 鏄惁鏄剧ず,
	) {
		Page pageobj = new Page();
		if (isshow > 0) {
			List<SearcheditInfo> slist = cwbInfoDao.getInfoByCretime(page, begindate, endtime);
			List<User> ulist = userDAO.getAllUser();
			pageobj = new Page(cwbInfoDao.countEditInfo(begindate, endtime), page, Page.ONE_PAGE_NUMBER);
			model.addAttribute("slist", slist);
			model.addAttribute("ulist", ulist);
		}
		model.addAttribute("page", page);
		model.addAttribute("page_obj", pageobj);
		return "editcwb/searchInfo";
	}

	/**
	 * 璁㈠崟淇℃伅淇敼瀵煎嚭
	 */
	@RequestMapping("/exportExcle")
	public void exportExcle(Model model, HttpServletResponse response, HttpServletRequest request) {

		String[] cloumnName1 = new String[16]; // 瀵煎嚭鐨勫垪鍚�
		String[] cloumnName2 = new String[16]; // 瀵煎嚭鐨勮嫳鏂囧垪鍚�

		exportService.SetEditOrderFields(cloumnName1, cloumnName2);
		final String[] cloumnName = cloumnName1;
		final String[] cloumnName3 = cloumnName2;
		String sheetName = "璁㈠崟淇敼淇℃伅"; // sheet鐨勫悕绉�
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "editcwbOrder_" + df.format(new Date()) + ".xlsx"; // 鏂囦欢鍚�
		try {
			// 鏌ヨ鍑烘暟鎹�
			String begindate = request.getParameter("begindate") == null ? "" : request.getParameter("begindate");
			String enddate = request.getParameter("enddate") == null ? "" : request.getParameter("enddate");

			final List<SearcheditInfo> views = cwbInfoDao.getAllInfoByCretime(begindate, enddate);
			final List<User> userlist = userDAO.getAllUser();
			ExcelUtils excelUtil = new ExcelUtils() { // 鐢熸垚宸ュ叿绫诲疄渚嬶紝骞跺疄鐜板～鍏呮暟鎹殑鎶借薄鏂规硶
				@Override
				public void fillData(Sheet sheet, CellStyle style) {
					for (int k = 0; k < views.size(); k++) {
						Row row = sheet.createRow(k + 1);
						row.setHeightInPoints((float) 15);
						for (int i = 0; i < cloumnName.length; i++) {
							Cell cell = row.createCell((short) i);
							cell.setCellStyle(style);
							Object a = null;
							// 缁欏鍑篹xcel璧嬪�
							a = exportService.setEditOrderObject(cloumnName3, views, userlist, a, i, k);
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
}
