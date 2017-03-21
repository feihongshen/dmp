package cn.explink.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.stringtemplate.v4.ST;

import cn.explink.controller.pda.BatchDeliverBody;
import cn.explink.controller.pda.BatchDeliverBodyPdaResponse;
import cn.explink.controller.pda.BranchListBodyPdaResponse;
import cn.explink.controller.pda.CustomerListBodyPdaResponse;
import cn.explink.controller.pda.CwbTrackBody;
import cn.explink.controller.pda.CwbTrackBodyPdaResponse;
import cn.explink.controller.pda.CwborderViewBodyPdaResponse;
import cn.explink.controller.pda.LoginResponse;
import cn.explink.controller.pda.LoginResponseBody;
import cn.explink.controller.pda.PDAResponse;
import cn.explink.controller.pda.PDAXMLBody;
import cn.explink.controller.pda.PdaCwbOrderView;
import cn.explink.controller.pda.ReasonListBodyPdaResponse;
import cn.explink.controller.pda.StringBodyPdaResponse;
import cn.explink.controller.pda.StringBodyXMLPdaResponse;
import cn.explink.controller.pda.TruckListBodyPdaResponse;
import cn.explink.controller.pda.UserView;
import cn.explink.controller.pda.UserViewBodyPdaResponse;
import cn.explink.dao.BaleCwbDao;
import cn.explink.dao.BaleDao;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CommonDAO;
import cn.explink.dao.ComplaintDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.ExceedFeeDAO;
import cn.explink.dao.ExceptionCwbDAO;
import cn.explink.dao.ExportmouldDAO;
import cn.explink.dao.GroupDetailDao;
import cn.explink.dao.MenuDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.OutWarehouseGroupDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.dao.RemarkDAO;
import cn.explink.dao.RoleDAO;
import cn.explink.dao.StockResultDAO;
import cn.explink.dao.SwitchDAO;
import cn.explink.dao.SystemInstallDAO;
import cn.explink.dao.TranscwbOrderFlowDAO;
import cn.explink.dao.TruckDAO;
import cn.explink.dao.TuihuoRecordDAO;
import cn.explink.dao.UserDAO;
import cn.explink.dao.express.CityDAO;
import cn.explink.dao.express.CountyDAO;
import cn.explink.dao.express.ProvinceDAO;
import cn.explink.domain.Bale;
import cn.explink.domain.BaleView;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.ExceptionCwb;
import cn.explink.domain.Exportmould;
import cn.explink.domain.Menu;
import cn.explink.domain.OutWarehouseGroup;
import cn.explink.domain.Reason;
import cn.explink.domain.Role;
import cn.explink.domain.Truck;
import cn.explink.domain.User;
import cn.explink.domain.VO.express.EmbracedOrderVO;
import cn.explink.domain.VO.express.ExpressIntoStationVO;
import cn.explink.domain.VO.express.ExpressOpeAjaxResult;
import cn.explink.domain.VO.express.ExpressOutStationParamsVO;
import cn.explink.domain.VO.express.ExpressParamsVO;
import cn.explink.domain.dto.ExpressIntoDto;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.domain.orderflow.TranscwbOrderFlow;
import cn.explink.enumutil.BalePDAEnum;
import cn.explink.enumutil.BaleStateEnum;
import cn.explink.enumutil.BranchEnum;
import cn.explink.enumutil.CwbOrderPDAEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;
import cn.explink.enumutil.ExceptionCwbIsHanlderEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.OutWarehouseGroupEnum;
import cn.explink.enumutil.OutwarehousegroupOperateEnum;
import cn.explink.enumutil.ReasonTypeEnum;
import cn.explink.enumutil.VipExchangeFlagEnum;
import cn.explink.enumutil.express.AddressMatchEnum;
import cn.explink.enumutil.express.ExpressOutStationFlagEnum;
import cn.explink.exception.CwbException;
import cn.explink.exception.ExplinkException;
import cn.explink.pos.tools.SignTypeEnum;
import cn.explink.service.BaleService;
import cn.explink.service.CwbOrderPdaService;
import cn.explink.service.CwbOrderService;
import cn.explink.service.CwbRouteService;
import cn.explink.service.DataStatisticsService;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.ExportService;
import cn.explink.service.SystemInstallService;
import cn.explink.service.express.EmbracedOrderInputService;
import cn.explink.service.express.ExpressOutStationService;
import cn.explink.service.express.TpsInterfaceExecutor;
import cn.explink.support.transcwb.TransCwbDao;
import cn.explink.util.B2cUtil;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.ExcelUtils;
import cn.explink.util.Page;
import cn.explink.util.ResourceBundleUtil;
import cn.explink.util.ServiceUtil;
import cn.explink.util.StringUtil;
import cn.explink.util.Tools;

@RequestMapping("/cwborderPDA")
@Controller
public class CwbOrderPDAController {
	String serverversion = "3.0";
	private Logger logger = LoggerFactory.getLogger(CwbOrderPDAController.class);

	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	BaleDao baleDao;
	@Autowired
	BaleCwbDao baleCwbDao;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	ReasonDao reasonDAO;
	@Autowired
	CwbOrderService cwborderService;
	@Autowired
	BaleService baleService;
	@Autowired
	OutWarehouseGroupDAO outWarehouseGroupDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	StockResultDAO stockResultDAO;
	@Autowired
	TruckDAO truckDAO;
	@Autowired
	GroupDetailDao groupdetailDAO;
	@Autowired
	MenuDAO menuDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	ExceptionCwbDAO exceptionCwbDAO;
	@Autowired
	ExceedFeeDAO exceedFeeDAO;
	@Autowired
	GroupDetailDao groupDetailDAO;
	@Autowired
	SwitchDAO switchDAO;
	@Autowired
	SystemInstallService systemInstallService;
	@Autowired
	CwbRouteService cwbRouteService;
	@Autowired
	SystemInstallDAO systemInstallDAO;
	@Autowired
	TranscwbOrderFlowDAO transcwbOrderFlowDAO;
	@Autowired
	TransCwbDao transCwbDao;
	@Autowired
	CommonDAO commonDAO;
	@Autowired
	CustomWareHouseDAO customWareHouseDAO;
	@Autowired
	ExportService exportService;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	DataStatisticsService dataStatisticsService;
	@Autowired
	TuihuoRecordDAO tuihuoRecordDAO;
	@Autowired
	ComplaintDAO complaintDAO;
	@Autowired
	RemarkDAO remarkDAO;
	@Autowired
	B2cUtil bcUtil;

	PropertyPlaceholderHelper placeholderHelper = new PropertyPlaceholderHelper("${", "}");
	@Autowired
	RoleDAO roleDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	ExportmouldDAO exportmouldDAO;
	@Autowired
	EmbracedOrderInputService embracedOrderInputService;
	@Autowired
	CwbOrderPdaService cwbOrderPdaService;
	@Autowired
	ProvinceDAO provinceDAO ;
	@Autowired
	CityDAO cityDAO;
	@Autowired
	CountyDAO  countyDAO;
	@Autowired
	ExpressOutStationService expressOutStationService;
	@Autowired
	private TpsInterfaceExecutor tpsInterfaceExecutor;

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail)

		this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/getSystime")
	public @ResponseBody PDAResponse getSystime(HttpServletResponse response, String error) {
		PDAResponse PDAResponse = new StringBodyPdaResponse(CwbOrderPDAEnum.OK.getCode(), "", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		return PDAResponse;
	}

	@RequestMapping("/loginError")
	public @ResponseBody PDAResponse loginError(HttpServletRequest request, HttpServletResponse response, String error) {
		logger.info(error);
		/***** 开始 ******/
		// APP返回值修改 vic.liang@pjbest.com 2016-07-19
		if (StringUtil.isEmpty(error)) {
			return new StringBodyPdaResponse(CwbOrderPDAEnum.LOGIN_ERROR.getCode(), CwbOrderPDAEnum.LOGIN_ERROR.getError(), "");
		}
		return new StringBodyPdaResponse(CwbOrderPDAEnum.LOGIN_TIMEOUT.getCode(), CwbOrderPDAEnum.LOGIN_TIMEOUT.getError(), "");
		/****** 结束 ******/
	}

	@RequestMapping("/login")
	public @ResponseBody LoginResponse login(HttpServletResponse response, HttpServletRequest request) {
		LoginResponse PDAResponse = new LoginResponse(CwbOrderPDAEnum.OK.getCode(), CwbOrderPDAEnum.OK.getError());
		PDAResponse.setBody(this.getUser(request));
		return PDAResponse;
	}

	private LoginResponseBody getUser(HttpServletRequest request) {
		LoginResponseBody loginResponseBody = new LoginResponseBody();
		loginResponseBody.setUsername(this.getSessionUser().getUsername());
		loginResponseBody.setAuthtoken(request.getSession().getId());
		loginResponseBody.setRealname(this.getSessionUser().getRealname());
		loginResponseBody.setBranchname(this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid()).getBranchname());
		List<Menu> mList = this.menuDAO.getMenusByUserRoleidToPDA(this.getSessionUser().getRoleid());
		String functionids = this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid()).getFunctionids();
		StringBuffer pdaMenu = new StringBuffer();
		for (Menu m : mList) {
			if (functionids.indexOf(m.getMenuno()) != -1) {
				pdaMenu.append(",").append(m.getMenuno());
			}
		}
		loginResponseBody.setMenuids(pdaMenu.toString());
		return loginResponseBody;
	}

	@RequestMapping("/loadWavFile")
	public void loadWavFile(HttpServletRequest request, HttpServletResponse response, @RequestParam("filename") String filename) {
		// 获得文件下载的路径
		String path = ResourceBundleUtil.WAVPATH;
		InputStream is = null;
		byte[] data = new byte[1024 * 4];
		int len = 0;
		OutputStream os = null;
		try {
			File file = new File(path + filename);
			if (!file.exists()) {
				file.createNewFile();
			}
			is = new FileInputStream(file);
			os = response.getOutputStream();
			while ((len = is.read(data)) != -1) {
				os.write(data, 0, len);
			}
			os.flush();
			os.close();
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	@RequestMapping("/newPDA")
	public @ResponseBody PDAResponse PDA_NEW(HttpServletResponse response, HttpServletRequest request, @RequestParam("deviceid") String deviceid, @RequestParam("authtoken") String authtoken,
			@RequestParam("clientversion") String clientversion, @RequestParam("requestparam") String requestparam, @RequestParam(value = "cwb", required = false, defaultValue = "") String cwb,
			@RequestParam(value = "requestbatchno", required = false, defaultValue = "0") long requestbatchno) throws Exception {

		this.logger.info("PDA--开始进行扫描,cwb={}", cwb);
		String scancwb = cwb;
		try {
			User u = this.getSessionUser();
		} catch (Exception e) {
			return this.loginError(request, response, CwbOrderPDAEnum.LOGIN_TIMEOUT.getError());
		}
		try {
			if (requestparam.equals("transf_warehouse_submit")) { // 中转入库
				long customerid = Long.parseLong(request.getParameter("customerid") == "" ? "0" : request.getParameter("customerid"));
				String baleno = "";
				if ((request.getParameter("baleno") != null) && (request.getParameter("baleno").length() > 0)) {
					baleno = request.getParameter("baleno");
				}
				return this.transfintohouse(cwb, customerid, 0, requestbatchno, baleno, request, "");
			} else if (requestparam.equals("back_warehouse_submit")) { // 退货入库
				cwb = this.cwborderService.translateCwb(cwb);
				long driverid = request.getParameter("driverid").length() == 0 ? 0 : Long.parseLong(request.getParameter("driverid"));
				return this.backIntoWarehous(cwb, driverid, requestbatchno, request, "");

			} else if (requestparam.equals("transf_outputhouse_submit")) {// 中转出库

			} else if (requestparam.equals("back_again_post_submit")) {// 退货再投

			} else if (requestparam.equals("back_supplier_outputhouse_submit")) {// 退供应商出库

			} else if (requestparam.equals("transf_output_station_submit")) {// 中转出站

			} else if (requestparam.equals("back_output_station_submit")) {// 退货出站

			}
		} catch (CwbException e) {
			CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
			this.exceptionCwbDAO.createExceptionCwbScan(cwb, e.getFlowordertye(), e.getMessage(), this.getSessionUser().getBranchid(), this.getSessionUser().getUserid(),
					cwbOrder == null ? 0 : cwbOrder.getCustomerid(), 0, 0, 0, "", scancwb);
			PDAResponse PDAResponse = new PDAResponse(CwbOrderPDAEnum.SYS_ERROR.getCode(), e.getMessage());
			PDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.SYS_ERROR.getVediourl());
			return PDAResponse;
		}
		return null;
	}

	/**
	 * 退货入库
	 *
	 * @param cwb
	 * @param driverid
	 * @param requestbatchno
	 * @param request
	 * @param comment
	 * @return
	 */
	private PDAResponse backIntoWarehous(String cwb, long driverid, long requestbatchno, HttpServletRequest request, String comment) {
		String scancwb = cwb;
		cwb = this.cwborderService.translateCwb(cwb);
		CwbOrder cwbOrder = new CwbOrder();
		try {
			cwbOrder = this.cwborderService.backIntoWarehous(this.getSessionUser(), cwb, scancwb, driverid, 0, "", false, 0, 0);
			String errorinfo = this.getErrorInfoForIntoWarehouse(cwbOrder);
			errorinfo = errorinfo.substring(0, errorinfo.indexOf("#")) + "定入成功！";
			PDAResponse pDAResponse = new PDAResponse(CwbOrderPDAEnum.OK.getCode(), errorinfo);
			pDAResponse.setScannum(cwbOrder.getScannum());
			pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());

			String RUKUPCandPDAaboutYJDPWAV = this.systemInstallDAO.getSystemInstall("RUKUPCandPDAaboutYJDPWAV") == null ? "yes"
					: this.systemInstallDAO.getSystemInstall("RUKUPCandPDAaboutYJDPWAV").getValue();

			if (RUKUPCandPDAaboutYJDPWAV.equals("yes") && ((cwbOrder.getSendcarnum() > 1) || (cwbOrder.getBackcarnum() > 1))) {
				pDAResponse.setShouldShock(true);
				pDAResponse.setPrintinfo(pDAResponse.getPrintinfo() + " " + CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getError());
				pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getVediourl());
			}
			if (cwbOrder.getDeliverid() != 0) {
				User user = this.userDAO.getUserByUserid(cwbOrder.getDeliverid());
				if ((user.getUserwavfile() != null) && (user.getUserwavfile().length() > 0)) {
					pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.wavPath + user.getUserwavfile());
				}
				pDAResponse.setPrintinfo(pDAResponse.getPrintinfo() + " " + user.getRealname());
			}
			if (cwbOrder.getNextbranchid() != 0) {
				Branch branch = this.branchDAO.getBranchByBranchid(cwbOrder.getNextbranchid());
				if ((branch != null) && (branch.getBranchwavfile() != null) && (branch.getBranchwavfile().length() > 0)) {
					pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.wavPath + branch.getBranchwavfile());
				} else {
					pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());
				}
				pDAResponse.setPrintinfo(pDAResponse.getPrintinfo() + " " + branch.getBranchname());
			}
			if ((cwbOrder.getReceivablefee() != null) && (cwbOrder.getReceivablefee().compareTo(this.exceedFeeDAO.getExceedFee().getExceedfee()) > 0)) {
				pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.GAO_JIA.getVediourl());
				pDAResponse.setPrintinfo(pDAResponse.getPrintinfo() + " " + CwbOrderPDAEnum.GAO_JIA.getError());
				pDAResponse.setShouldShock(true);
			}
			return pDAResponse;
		} catch (CwbException e) {
			cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
			this.exceptionCwbDAO.createExceptionCwbScan(cwb, e.getFlowordertye(), e.getMessage(), this.getSessionUser().getBranchid(), this.getSessionUser().getUserid(),
					cwbOrder == null ? 0 : cwbOrder.getCustomerid(), 0, 0, 0, "", scancwb);
			if (e.getError().getValue() == ExceptionCwbErrorTypeEnum.CHONG_FU_RU_KU.getValue()) {
				Branch branch = this.branchDAO.getBranchByBranchid(cwbOrder.getNextbranchid());
				PDAResponse pDAResponse = new PDAResponse(CwbOrderPDAEnum.CHONG_FU_RU_KU.getCode(),
						cwb + CwbOrderPDAEnum.CHONG_FU_RU_KU.getError() + " " + StringUtil.nullConvertToEmptyString(branch.getBranchname()));
				pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.CHONG_FU_RU_KU.getVediourl());
				pDAResponse.setPrintinfo(pDAResponse.getPrintinfo() + " " + CwbOrderPDAEnum.CHONG_FU_RU_KU.getError());
				return pDAResponse;
			} else {
				throw e;
			}
		}
	}

	private PDAResponse transfintohouse(String cwb, long customerid, long deviceid, long requestbatchno, String baleno, HttpServletRequest request, String comments) {
		String scancwb = cwb;
		cwb = this.cwborderService.translateCwb(cwb);
		CwbOrder cwbOrder = new CwbOrder();
		try {
			cwbOrder = this.cwborderService.changeintoWarehous(this.getSessionUser(), cwb, scancwb, customerid, deviceid, requestbatchno, comments, baleno, false, 0, 0);
			// cwbOrder=cwborderService.changeintoWarehous(getSessionUser(),cwb,scancwb,
			// customerid, 0l, requestbatchno,comments,"",false);
			String errorinfo = this.getErrorInfoForIntoWarehouse(cwbOrder);
			errorinfo = errorinfo.substring(0, errorinfo.indexOf("#")) + "定入成功！";
			PDAResponse pDAResponse = new PDAResponse(CwbOrderPDAEnum.OK.getCode(), errorinfo);
			pDAResponse.setScannum(cwbOrder.getScannum());
			pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());

			String RUKUPCandPDAaboutYJDPWAV = this.systemInstallDAO.getSystemInstall("RUKUPCandPDAaboutYJDPWAV") == null ? "yes"
					: this.systemInstallDAO.getSystemInstall("RUKUPCandPDAaboutYJDPWAV").getValue();

			if (RUKUPCandPDAaboutYJDPWAV.equals("yes") && ((cwbOrder.getSendcarnum() > 1) || (cwbOrder.getBackcarnum() > 1))) {
				pDAResponse.setShouldShock(true);
				pDAResponse.setPrintinfo(pDAResponse.getPrintinfo() + " " + CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getError());
				pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getVediourl());
			}
			if (cwbOrder.getDeliverid() != 0) {
				User user = this.userDAO.getUserByUserid(cwbOrder.getDeliverid());
				if ((user.getUserwavfile() != null) && (user.getUserwavfile().length() > 0)) {
					pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.wavPath + user.getUserwavfile());
				}
				pDAResponse.setPrintinfo(pDAResponse.getPrintinfo() + " " + user.getRealname());
			}
			if (cwbOrder.getNextbranchid() != 0) {
				Branch branch = this.branchDAO.getBranchByBranchid(cwbOrder.getNextbranchid());
				if ((branch != null) && (branch.getBranchwavfile() != null) && (branch.getBranchwavfile().length() > 0)) {
					pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.wavPath + branch.getBranchwavfile());
				} else {
					pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());
				}
				pDAResponse.setPrintinfo(pDAResponse.getPrintinfo() + " " + branch.getBranchname());
			}
			if ((cwbOrder.getReceivablefee() != null) && (cwbOrder.getReceivablefee().compareTo(this.exceedFeeDAO.getExceedFee().getExceedfee()) > 0)) {
				pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.GAO_JIA.getVediourl());
				pDAResponse.setPrintinfo(pDAResponse.getPrintinfo() + " " + CwbOrderPDAEnum.GAO_JIA.getError());
				pDAResponse.setShouldShock(true);
			}

			return pDAResponse;
		} catch (CwbException e) {
			cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
			this.exceptionCwbDAO.createExceptionCwbScan(cwb, e.getFlowordertye(), e.getMessage(), this.getSessionUser().getBranchid(), this.getSessionUser().getUserid(),
					cwbOrder == null ? 0 : cwbOrder.getCustomerid(), 0, 0, 0, "", scancwb);
			if (e.getError().getValue() == ExceptionCwbErrorTypeEnum.CHONG_FU_RU_KU.getValue()) {
				Branch branch = this.branchDAO.getBranchByBranchid(cwbOrder.getNextbranchid());
				PDAResponse pDAResponse = new PDAResponse(CwbOrderPDAEnum.CHONG_FU_RU_KU.getCode(),
						cwb + CwbOrderPDAEnum.CHONG_FU_RU_KU.getError() + " " + StringUtil.nullConvertToEmptyString(branch.getBranchname()));
				pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.CHONG_FU_RU_KU.getVediourl());
				pDAResponse.setPrintinfo(pDAResponse.getPrintinfo() + " " + CwbOrderPDAEnum.CHONG_FU_RU_KU.getError());
				return pDAResponse;
			} else {
				throw e;
			}
		}
	}

	@RequestMapping("/")
	public @ResponseBody PDAResponse PDA(HttpServletResponse response, HttpServletRequest request, @RequestParam("deviceid") String deviceid, @RequestParam("authtoken") String authtoken,
			@RequestParam("clientversion") String clientversion, @RequestParam("requestparam") String requestparam, @RequestParam(value = "cwb", required = false, defaultValue = "") String cwb,
			@RequestParam(value = "requestbatchno", required = false, defaultValue = "0") long requestbatchno) throws Exception {
		this.logger.info("PDA--开始进行扫描");
		String scancwb = cwb;
		try {
			User u = this.getSessionUser();
		} catch (Exception e) {
			return this.loginError(request, response, CwbOrderPDAEnum.LOGIN_TIMEOUT.getError());
		}

		try {
			if (requestparam.equals("selectcustomerlist")) {// 查询供货商列表
				List<Customer> cList = this.customerDAO.getAllCustomers();
				return this.getCustomers(cList, requestbatchno, new StringBuffer(), CwbOrderPDAEnum.OK.getCode(), CwbOrderPDAEnum.OK.getError(), CwbOrderPDAEnum.OK.getVediourl());

			} else if (requestparam.equals("selectdriverlist")) {// 查询驾驶员列表,3为驾驶员角色的ID
				List<User> uList = this.userDAO.getUserByRole(3);
				return this.getUsers(uList, requestbatchno, new StringBuffer(), CwbOrderPDAEnum.OK.getCode(), CwbOrderPDAEnum.OK.getError(), CwbOrderPDAEnum.OK.getVediourl());

			} else if (requestparam.equals("warehouse_catchcargo_submit")) {// 提货扫描提交
				long customerid = Long.parseLong(request.getParameter("customerid") == "" ? "0" : request.getParameter("customerid"));
				long driverid = Long.parseLong(request.getParameter("driverid") == "" ? "0" : request.getParameter("driverid"));
				return this.getGoods(cwb, customerid, driverid, requestbatchno, new StringBuffer(), CwbOrderPDAEnum.OK.getCode(), CwbOrderPDAEnum.OK.getError(),
						request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl(), request);

			} else if (requestparam.equals("warehouseimport_submit")) {// 入库扫描提交
				String customerid = request.getParameter("customerid").length() == 0 ? "-1" : request.getParameter("customerid");
				long driverid = request.getParameter("driverid").length() == 0 ? 0 : Long.parseLong(request.getParameter("driverid"));

				String baleno = "";
				if ((request.getParameter("baleno") != null) && (request.getParameter("baleno").length() > 0)) {
					baleno = request.getParameter("baleno");
				}
				return this.intoWarehous(cwb, customerid, driverid, requestbatchno, baleno, request, "");
			} else if (requestparam.equals("warehouseimport_submit_app")) {// 入库扫描提交
				String customerid = request.getParameter("customerid").length() == 0 ? "-1" : request.getParameter("customerid");
				long driverid = request.getParameter("driverid").length() == 0 ? 0 : Long.parseLong(request.getParameter("driverid"));

				String baleno = "";
				if ((request.getParameter("baleno") != null) && (request.getParameter("baleno").length() > 0)) {
					baleno = request.getParameter("baleno");
				}
				return this.intoWarehousAPP(cwb, customerid, driverid, requestbatchno, baleno, request, "");

			} else if (requestparam.equals("warehouseimport_submit_forremark")) {// 备注
				long csremarkid = Long.parseLong(request.getParameter("csremarkid"));
				long multicwbnum = Long.parseLong(request.getParameter("multicwbnum"));
				String comment = request.getParameter("comment");

				if (csremarkid == 1) {
					comment = "破损";
				} else if (csremarkid == 2) {
					comment = "超大";
				} else if (csremarkid == 3) {
					comment = "超重";
				} else if (csremarkid == 4) {
					comment = "一票多物";
				}

				return this.forremark(comment, multicwbnum, cwb, requestbatchno, new StringBuffer(), CwbOrderPDAEnum.OK.getCode(), CwbOrderPDAEnum.OK.getError(), CwbOrderPDAEnum.OK.getVediourl(),
						request);

			} else if (requestparam.equals("selectbranchlist_forexport")) {// 查询站点列表（出库、中转出库）
				List<Long> nextPossibleBackBranch = this.cwbRouteService.getNextPossibleBranch(this.getSessionUser().getBranchid());
				List<Branch> bList = new ArrayList<Branch>();
				for (long branchid : nextPossibleBackBranch) {
					Branch branch = this.branchDAO.getBranchByBranchid(branchid);
					if (branch.getBranchid() != 0) {
						bList.add(branch);
					}
				}
				return this.getBranch(bList, requestbatchno, new StringBuffer(), CwbOrderPDAEnum.OK.getCode(), CwbOrderPDAEnum.OK.getError(), CwbOrderPDAEnum.OK.getVediourl());

			} else if (requestparam.equals("selectbranchlist_forbackexport")) {// 查询站点列表（出库、中转出库）
				List<Long> nextPossibleBackBranch = this.cwbRouteService.getNextPossibleBackBranch(this.getSessionUser().getBranchid());
				List<Branch> bList = new ArrayList<Branch>();
				for (long branchid : nextPossibleBackBranch) {
					bList.add(this.branchDAO.getBranchByBranchid(branchid));
				}
				return this.getBranch(bList, requestbatchno, new StringBuffer(), CwbOrderPDAEnum.OK.getCode(), CwbOrderPDAEnum.OK.getError(), CwbOrderPDAEnum.OK.getVediourl());

			} else if (requestparam.equals("selecttrucklist")) {// 查询车辆列表
				List<Truck> tlist = this.truckDAO.getAllTruck();
				return this.getTruck(tlist, requestbatchno, new StringBuffer(), CwbOrderPDAEnum.OK.getCode(), CwbOrderPDAEnum.OK.getError(), CwbOrderPDAEnum.OK.getVediourl());

			} else if (requestparam.equals("warehouseexport_submit")) {// 出库扫描提交
				/*
				 * 首先判断是否传入批次的id，如果没有，创建一个批次（id,编号,司机,车辆,状态[扫入中、已封包、已到货]）
				 * 然后向这个订单中扫描对应的货单，产生监控记录，当相关人员操作了对此批次进行打印后，此批次进行封包，批次状态变为已封包
				 */

				long branchid = Long.parseLong(request.getParameter("branchid"));
				long driverid = request.getParameter("driverid") == "" ? -1 : Long.parseLong(request.getParameter("driverid"));
				long truckid = request.getParameter("truckid").length() == 0 ? 0 : Long.parseLong(request.getParameter("truckid"));
				long confirmflag = request.getParameter("confirmflag").length() == 0 ? 0 : Long.parseLong(request.getParameter("confirmflag"));
				long reasonid = request.getParameter("reasonid") == null ? 0 : Long.parseLong(request.getParameter("reasonid"));
				String baleno = "";
				if ((request.getParameter("baleno") != null) && (request.getParameter("baleno").length() > 0)) {
					baleno = request.getParameter("baleno");
				}
				// 这个可以注释掉，因为如果页面上没有选下一站，出库方法里面会根据路由来确定下一站，这里这样处理还会造成
				// 问题（跨入库直接出库，会自己出给自己）----刘武强20161104
				/*
				 * if (branchid <= 0) { CwbOrder co =
				 * this.cwbDAO.getCwbByCwb(scancwb); if (co != null) { branchid
				 * = co.getNextbranchid(); } }
				 */
				return this.outWarehous(cwb, driverid, truckid, branchid, confirmflag, reasonid, requestbatchno, baleno, new StringBuffer(), CwbOrderPDAEnum.OK.getCode(),
						request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl(), request);

			} else if (requestparam.equals("warehouseexport_submit_app")) {// 出库扫描提交
				/*
				 * 首先判断是否传入批次的id，如果没有，创建一个批次（id,编号,司机,车辆,状态[扫入中、已封包、已到货]）
				 * 然后向这个订单中扫描对应的货单，产生监控记录，当相关人员操作了对此批次进行打印后，此批次进行封包，批次状态变为已封包
				 */

				long branchid = Long.parseLong(request.getParameter("branchid"));
				long driverid = request.getParameter("driverid") == "" ? -1 : Long.parseLong(request.getParameter("driverid"));
				long truckid = request.getParameter("truckid").length() == 0 ? 0 : Long.parseLong(request.getParameter("truckid"));
				long confirmflag = request.getParameter("confirmflag").length() == 0 ? 0 : Long.parseLong(request.getParameter("confirmflag"));
				long reasonid = request.getParameter("reasonid") == null ? 0 : Long.parseLong(request.getParameter("reasonid"));
				String baleno = "";
				if ((request.getParameter("baleno") != null) && (request.getParameter("baleno").length() > 0)) {
					baleno = request.getParameter("baleno");
				}
				return this.outWarehousAPP(cwb, driverid, truckid, branchid, confirmflag, reasonid, requestbatchno, baleno, new StringBuffer(), CwbOrderPDAEnum.OK.getCode(),
						request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl(), request);

			} else if (requestparam.equals("warehouse_finishexport_submit")) {// 出库扫描批次封包确认
				long driverid = request.getParameter("driverid") == "" ? -1 : Long.parseLong(request.getParameter("driverid"));
				return this.finishExport(driverid, OutwarehousegroupOperateEnum.ChuKu.getValue(), requestbatchno, new StringBuffer(), CwbOrderPDAEnum.OK.getCode(), CwbOrderPDAEnum.OK.getError(),
						CwbOrderPDAEnum.OK.getVediourl());

			} else if (requestparam.equals("branchimport_submit")) {// 分站到货扫描
				long driverid = request.getParameter("driverid") == null ? 0 : Long.parseLong(request.getParameter("driverid"));
				String baleno = "";
				if ((request.getParameter("baleno") != null) && (request.getParameter("baleno").length() > 0)) {
					baleno = request.getParameter("baleno");
				}
				return this.substationGoods(cwb, baleno, driverid, requestbatchno, request);

			} else if (requestparam.equals("selectdeliverlist_forbranchdeliver")) {// 查询本站点小件员列表
				List<User> uList = new ArrayList<User>();
				if (this.getSessionUser().getRoleid() == 2) {// 小件员只能看见小件员的名称
					uList = this.userDAO.getUserByid(this.getSessionUser().getUserid());
				} else {
					String roleids = "2,4";
					uList = this.userDAO.getDeliveryUserByRolesAndBranchid(roleids, this.getSessionUser().getBranchid());
				}
				return this.getUser(uList, requestbatchno, new StringBuffer(), CwbOrderPDAEnum.OK.getCode(), CwbOrderPDAEnum.OK.getError(), CwbOrderPDAEnum.OK.getVediourl());

			} else if (requestparam.equals("branchdeliver_submit")) {// 领货扫描提交
				long deliverid = Long.parseLong(request.getParameter("deliverid"));
				return this.ReceiveGoods(cwb, deliverid, requestbatchno, new StringBuffer(), request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl(), request);

			} else if (requestparam.equals("selecttransreasonlist_fortransexport")) {// 查询中转原因列表
				List<Reason> rlist = this.reasonDAO.getAllReasonByReasonType(ReasonTypeEnum.ChangeTrains.getValue());
				return this.getReason(rlist, requestbatchno, new StringBuffer(), CwbOrderPDAEnum.OK.getCode(), CwbOrderPDAEnum.OK.getError(), CwbOrderPDAEnum.OK.getVediourl());

			} else if (requestparam.equals("branch_finishbackexport_submit")) {// 退货出站扫描批次封包确认
				long driverid = request.getParameter("driverid") == "" ? 0 : Long.parseLong(request.getParameter("driverid"));
				long truckid = request.getParameter("truckid").length() == 0 ? 0 : Long.parseLong(request.getParameter("truckid"));
				return this.BranchFinishBackExport(driverid, truckid, OutwarehousegroupOperateEnum.TuiHuoChuZhan.getValue(), requestbatchno, new StringBuffer(), CwbOrderPDAEnum.OK.getCode(),
						CwbOrderPDAEnum.OK.getError(), CwbOrderPDAEnum.OK.getVediourl(), request);

			} else if (requestparam.equals("warehouse_printbar_submit")) {// 标签打印
				return this.printBar(cwb, requestbatchno, new StringBuffer(), CwbOrderPDAEnum.OK.getCode(), CwbOrderPDAEnum.OK.getError(), CwbOrderPDAEnum.OK.getVediourl(), request);

			} else if (requestparam.equals("warehouse_packageprintbar_submit")) {// 包号打印
				return this.packagePrintBar(cwb, requestbatchno, new StringBuffer(), BalePDAEnum.OK.getCode(), BalePDAEnum.OK.getError(), BalePDAEnum.OK.getVediourl(), request);
			} else if (requestparam.equals("warehouse_scancwbbranch_submit")) {// 理货扫描
				return this.scanCwbBranch(cwb, requestbatchno, new StringBuffer(), CwbOrderPDAEnum.OK.getCode(), CwbOrderPDAEnum.OK.getError(),
						request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl(), request);

			} else if (requestparam.equals("selectbranchlist_forscanstock")) {// 盘点站点列表
				String sitetypes = BranchEnum.KuFang.getValue() + "," + BranchEnum.ZhanDian.getValue() + "," + BranchEnum.ZhongZhuan.getValue() + "," + BranchEnum.TuiHuo.getValue();
				List<Branch> bList = this.branchDAO.getBanchByBranchidForStock(sitetypes);
				return this.getBranch(bList, requestbatchno, new StringBuffer(), CwbOrderPDAEnum.OK.getCode(), CwbOrderPDAEnum.OK.getError(), CwbOrderPDAEnum.OK.getVediourl());

			} else if (requestparam.equals("deliverpod_forquery")) {// 订单扫描查询
				return this.ScanCwb(cwb, requestbatchno, new StringBuffer(), CwbOrderPDAEnum.OK.getCode(), CwbOrderPDAEnum.OK.getError(), CwbOrderPDAEnum.OK.getVediourl());

			} else if (requestparam.equals("selectpodresultlist_forpod")) {// 配送结果列表查询
				return this.SelectResult(DeliveryStateEnum.values(), requestbatchno, new StringBuffer(), CwbOrderPDAEnum.OK.getCode(), CwbOrderPDAEnum.OK.getError(), CwbOrderPDAEnum.OK.getVediourl());

			} else if (requestparam.equals("selectbackreasonlist_forpod")) {// 退货原因列表查询
				List<Reason> rlist = this.reasonDAO.getAllReasonByReasonType(ReasonTypeEnum.ReturnGoods.getValue());
				return this.SelectReturnResult(rlist, requestbatchno, new StringBuffer(), CwbOrderPDAEnum.OK.getCode(), CwbOrderPDAEnum.OK.getError(), CwbOrderPDAEnum.OK.getVediourl());

			} else if (requestparam.equals("selectleavedreasonlist_forpod")) {// 滞留原因列表查询
				List<Reason> rlist = this.reasonDAO.getAllReasonByReasonType(ReasonTypeEnum.BeHelpUp.getValue());
				return this.getLeaveReason(rlist, requestbatchno, new StringBuffer(), CwbOrderPDAEnum.OK.getCode(), CwbOrderPDAEnum.OK.getError(), CwbOrderPDAEnum.OK.getVediourl());

			} else if (requestparam.equals("selectpodremarklist_forpod")) {// 配送结果备注列表查询
				List<Reason> rlist = this.reasonDAO.getAllReasonByReasonType(ReasonTypeEnum.GiveResult.getValue());
				return this.SelectRemarkResult(rlist, requestbatchno, new StringBuffer(), CwbOrderPDAEnum.OK.getCode(), CwbOrderPDAEnum.OK.getError(), CwbOrderPDAEnum.OK.getVediourl());

			} else if (requestparam.equals("deliverpod_submit")) {// 单票结果反馈提交
				// 派送员登录名
				String username = new String(request.getParameter("username").getBytes("ISO8859-1"), "UTF-8"); // 登录派送员id
				long podresultid = request.getParameter("podresultid") == "" ? 0 : Long.parseLong(request.getParameter("podresultid")); // 配送结果id
				long backreasonid = request.getParameter("backreasonid") == "" ? 0 : Long.parseLong(request.getParameter("backreasonid")); // 退货原因id
				long leavedreasonid = request.getParameter("leavedreasonid") == "" ? 0 : Long.parseLong(request.getParameter("leavedreasonid")); // 滞留原因id
				long podremarkid = request.getParameter("podremarkid") == "" ? 0 : Long.parseLong(request.getParameter("podremarkid")); // 配送结果备注
				BigDecimal receivedfeecash = BigDecimal.valueOf(Double.parseDouble(request.getParameter("receivedfeecash") == "" ? "0" : request.getParameter("receivedfeecash"))); // 实收现金
				BigDecimal receivedfeepos = BigDecimal.valueOf(Double.parseDouble(request.getParameter("receivedfeepos") == "" ? "0" : request.getParameter("receivedfeepos"))); // Pos刷卡实收
				BigDecimal receivedfeecheque = BigDecimal.valueOf(Double.parseDouble(request.getParameter("receivedfeecheque") == "" ? "0" : request.getParameter("receivedfeecheque"))); // 支票实收
				BigDecimal receivedfeeother = BigDecimal.valueOf(Double.parseDouble(request.getParameter("receivedfeeother") == "" ? "0" : request.getParameter("receivedfeeother"))); // 其他实收（优惠券折扣卷等）
				BigDecimal paybackedfee = BigDecimal.valueOf(Double.parseDouble(request.getParameter("paybackedfee") == "" ? "0" : request.getParameter("paybackedfee"))); // 上门实退现金

				long deliverid = this.userDAO.getSingelUsersByRealname(username).getUserid();
				return this.deliverPod(cwb, deliverid, podresultid, backreasonid, leavedreasonid, receivedfeecash, receivedfeepos, receivedfeecheque, receivedfeeother, paybackedfee, podremarkid,
						requestbatchno, new StringBuffer(), CwbOrderPDAEnum.OK.getCode(), CwbOrderPDAEnum.OK.getError(),
						request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl(), request);

			} else if (requestparam.equals("deliverpod_submit_batch")) {// 批量订单反馈扫描
				// 派送员登录名
				// String username = new
				// String(request.getParameter("username").getBytes("ISO8859-1"),
				// "UTF-8");

				// username=getSessionUser().getRealname();

				// 配送结果id
				long podresultid = StringUtils.isBlank(request.getParameter("podresultid")) ? 0 : Long.parseLong(request.getParameter("podresultid"));
				// 支付方式 （1.现金 2.POS 3.支票 4.其他）
				long paywayid = StringUtils.isBlank(request.getParameter("paywayid")) ? 1 : Long.parseLong(request.getParameter("paywayid"));
				// 退货原因id
				long backreasonid = StringUtils.isBlank(request.getParameter("backreasonid")) ? 0 : Long.parseLong(request.getParameter("backreasonid"));
				// 滞留原因id
				long leavedreasonid = StringUtils.isBlank(request.getParameter("leavedreasonid")) ? 0 : Long.parseLong(request.getParameter("leavedreasonid"));
				// 批量订单号 
				String cwbs = request.getParameter("cwbs");

				// long deliverid =
				// userDAO.getSingelUsersByRealname(username).getUserid();
				long deliverid = this.getSessionUser().getUserid();
				String deliverstateremark = request.getParameter("deliverstateremark");
				return this.deliverPodBatch(deliverid, podresultid, backreasonid, leavedreasonid, paywayid, deliverstateremark, cwbs, requestbatchno, new StringBuffer(), CwbOrderPDAEnum.OK.getError(),
						CwbOrderPDAEnum.OK.getVediourl());

			} else if (requestparam.equals("deliverpod_statistics")) {// 派送员归班汇总
				String username = new String(request.getParameter("username").getBytes("iso8859_1"), "UTF-8");
				if (username.length() == 0) {
					username = this.getSessionUser().getRealname();
				}
				return this.deliverpodStatistics(this.userDAO.getSingelUsersByRealname(username).getUserid(), requestbatchno, new StringBuffer(), CwbOrderPDAEnum.OK.getCode(),
						CwbOrderPDAEnum.OK.getError(), CwbOrderPDAEnum.OK.getVediourl());

			} else if (requestparam.equals("cwbtrack_submit")) {// 订单状态查询（查询单号提交）
				return this.cwbtrack(cwb, requestbatchno, new StringBuffer(), CwbOrderPDAEnum.OK.getCode(), CwbOrderPDAEnum.OK.getError(),
						request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl(), request);

			} else if (requestparam.equals("packagetrack_submit")) {// 包号查询
				return this.packagetrack(cwb, requestbatchno, new StringBuffer(), BalePDAEnum.OK.getCode(), BalePDAEnum.OK.getError(),
						request.getContextPath() + ServiceUtil.waverrorPath + BalePDAEnum.OK.getVediourl(), request);
			} else if (requestparam.equals("backtocustomerexport_submit")) {// 退供货商出库
				return this.backtocustom(cwb, requestbatchno, request);

			} else if (requestparam.equals("cwbtrack_submit_new")) {// 订单状态查询（查询单号提交）
																	// （添加）
				return this.cwbtrack_new(cwb, requestbatchno, new StringBuffer(), CwbOrderPDAEnum.OK.getCode(), CwbOrderPDAEnum.OK.getError(),
						request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl(), request);

			} else if (requestparam.equals("customerbackimport_submit")) {// 供货商拒收返库
				return this.customrefuseback(cwb, requestbatchno, new StringBuffer(), CwbOrderPDAEnum.OK.getCode(), CwbOrderPDAEnum.OK.getError(),
						request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl(), request);

			} else if (requestparam.equals("selectwavfiles_submit")) {// 声音文件下载
				return this.selectwavfiles(requestbatchno, new StringBuffer(), CwbOrderPDAEnum.OK.getCode(), CwbOrderPDAEnum.OK.getError(), CwbOrderPDAEnum.OK.getVediourl());

			} else if (requestparam.equals("branchimportbatch_submit")) {// 分站到货批量
				String cwbs = request.getParameter("cwbs");
				long driverid = request.getParameter("driverid") == "" ? 0 : Long.parseLong(request.getParameter("driverid"));
				return this.substationGoodsBatch(cwbs, driverid, requestbatchno, request);

			} else if (requestparam.equals("branchdeliverbatch_submit")) {// 领货批量
				String cwbs = request.getParameter("cwbs");

				long deliverid = StringUtils.isBlank(request.getParameter("deliverid")) ? this.getSessionUser().getUserid() : Long.parseLong(request.getParameter("deliverid"));

				return this.ReceiveGoodsBatch(cwbs, deliverid, requestbatchno, new StringBuffer(), CwbOrderPDAEnum.OK.getCode(), CwbOrderPDAEnum.OK.getError(), CwbOrderPDAEnum.OK.getVediourl(),
						request);
			} else if (requestparam.equals("branchdeliverbatch_submit")) {
				long branchid = Long.parseLong(request.getParameter("branchid"));
				long driverid = request.getParameter("driverid") == "" ? -1 : Long.parseLong(request.getParameter("driverid"));
				long truckid = request.getParameter("truckid").length() == 0 ? 0 : Long.parseLong(request.getParameter("truckid"));
				long confirmflag = request.getParameter("confirmflag").length() == 0 ? 0 : Long.parseLong(request.getParameter("confirmflag"));
				String baleno = "";
				if ((request.getParameter("baleno") != null) && (request.getParameter("baleno").length() > 0)) {
					baleno = request.getParameter("baleno");
				}
				return this.outWarehous(cwb, driverid, truckid, branchid, confirmflag, 0, requestbatchno, baleno, new StringBuffer(), CwbOrderPDAEnum.OK.getCode(), CwbOrderPDAEnum.OK.getVediourl(),
						request);
			} else if (requestparam.equals("branchbackexport_submit")) {// 退货出站
				long branchid = ((request.getParameter("branchid") == null) || (request.getParameter("branchid").length() == 0)) ? 0 : Long.parseLong(request.getParameter("branchid"));
				long driverid = request.getParameter("driverid") == "" ? -1 : Long.parseLong(request.getParameter("driverid"));
				long truckid = request.getParameter("truckid").length() == 0 ? 0 : Long.parseLong(request.getParameter("truckid"));
				long confirmflag = ((request.getParameter("confirmflag") == null) || (request.getParameter("confirmflag").length() == 0)) ? 0 : Long.parseLong(request.getParameter("confirmflag"));
				String baleno = "";
				if ((request.getParameter("baleno") != null) && (request.getParameter("baleno").length() > 0)) {
					baleno = request.getParameter("baleno");
				}
				return this.outUntreadWarehous(cwb, driverid, truckid, branchid, confirmflag, requestbatchno, baleno, new StringBuffer(), CwbOrderPDAEnum.OK.getCode(), CwbOrderPDAEnum.OK.getError(),
						request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl(), request);
			} else if (requestparam.equals("backbranchimport_submit")) {// 退货站入库扫描提交
				long driverid = request.getParameter("driverid").length() == 0 ? 0 : Long.parseLong(request.getParameter("driverid"));
				return this.backintoWarehous(cwb, driverid, requestbatchno, request, "");
			} else if (requestparam.equals("selectkufangbranchlist_forkdkexport")) {// 查询站点列表（库对库出库）
				List<Branch> bList = this.cwborderService.getNextPossibleKuFangBranches(this.getSessionUser());
				return this.getBranch(bList, requestbatchno, new StringBuffer(), CwbOrderPDAEnum.OK.getCode(), CwbOrderPDAEnum.OK.getError(), CwbOrderPDAEnum.OK.getVediourl());

			} else if (requestparam.equals("selecttuihuobranchlist_forthexport")) {// 查询站点列表（退货站退货出站）
				List<Branch> bList = this.cwborderService.getNextPossibleBranches(this.getSessionUser());
				List<Branch> lastList = new ArrayList<Branch>();
				for (Branch b : bList) {// 去掉中转站
					if (b.getSitetype() == BranchEnum.TuiHuo.getValue()) {
						lastList.add(b);
					}
				}
				return this.getBranch(lastList, requestbatchno, new StringBuffer(), CwbOrderPDAEnum.OK.getCode(), CwbOrderPDAEnum.OK.getError(), CwbOrderPDAEnum.OK.getVediourl());

			} else if (requestparam.equals("packageexport_checkpackageillegal")) {// 校验包号合法
				return this.checkPackageIllegal(cwb, requestbatchno, new StringBuffer(), BalePDAEnum.OK.getCode(), BalePDAEnum.OK.getError(), BalePDAEnum.OK.getVediourl(), request);

			} else if (requestparam.equals("packageexport_generatepackageno")) {// 生成包号
				return this.generatePackageNO(cwb, requestbatchno, new StringBuffer(), BalePDAEnum.OK.getCode(), BalePDAEnum.OK.getError(), BalePDAEnum.OK.getVediourl(), request);
			} else if (requestparam.equals("packageexport_checkorder")) {// 校验单号站点
				return this.checkOrderForPackage(cwb, requestbatchno, new StringBuffer(), CwbOrderPDAEnum.OK.getCode(), CwbOrderPDAEnum.OK.getError(), CwbOrderPDAEnum.OK.getVediourl(), request);
			} else if (requestparam.equals("packageexport_package")) {// 合包
				return this.packageProcess(cwb, requestbatchno, new StringBuffer(), BalePDAEnum.OK.getCode(), BalePDAEnum.OK.getError(), BalePDAEnum.OK.getVediourl(), request);
			} else if (requestparam.equals("packageexport_outstore")) {// 出库
				return this.outStore(cwb, requestbatchno, new StringBuffer(), BalePDAEnum.OK.getCode(), BalePDAEnum.OK.getError(), BalePDAEnum.OK.getVediourl(), request);

			}
			throw new CwbException(cwb, ExceptionCwbErrorTypeEnum.Invalid_Operation);
		} catch (CwbException e) {
			CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
			this.exceptionCwbDAO.createExceptionCwbScan(cwb, e.getFlowordertye(), e.getMessage(), this.getSessionUser().getBranchid(), this.getSessionUser().getUserid(),
					cwbOrder == null ? 0 : cwbOrder.getCustomerid(), 0, 0, 0, "", scancwb);
			PDAResponse PDAResponse = new PDAResponse(CwbOrderPDAEnum.SYS_ERROR.getCode(), e.getMessage());
			PDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.SYS_ERROR.getVediourl());
			return PDAResponse;
		}

	}

	/**
	 * 出库
	 *
	 * @param strPara
	 * @param requestbatchno
	 * @param body
	 * @param statuscode
	 * @param errorinfo
	 * @param errorinfovediurl
	 * @param request
	 * @return
	 */
	private PDAResponse outStore(String strPara, long requestbatchno, StringBuffer body, String statuscode, String errorinfo, String errorinfovediurl, HttpServletRequest request) {

		String baleNO;
		String nextbranch;
		long nextBranchid;
		Bale bale = null;
		try {
			Document document = DocumentHelper.parseText(strPara);
			Element rootElt = document.getRootElement();
			baleNO = rootElt.elementTextTrim("baleNO");
			nextbranch = rootElt.elementTextTrim("nextBranch");
			// nextBranchid=branchDAO.getBranchByBranchname(nextbranch).getBranchid();
			nextBranchid = Long.parseLong(nextbranch);
		} catch (DocumentException e) {
			statuscode = "100001";
			errorinfo = e.getMessage();
			errorinfovediurl = request.getContextPath() + ServiceUtil.waverrorPath + BalePDAEnum.YI_CHANG_BAO_HAO.getVediourl();
			PDAResponse PDAResponse = new StringBodyPdaResponse(statuscode, errorinfo, requestbatchno, errorinfovediurl, body);
			return PDAResponse;
		}
		try {
			bale = this.baleService.fengbao(this.getSessionUser(), baleNO.trim(), nextBranchid);
		} catch (CwbException e) {
			statuscode = CwbOrderPDAEnum.Feng_Bao.getCode();
			errorinfo = e.getMessage();
			errorinfovediurl = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.Feng_Bao.getVediourl();
			PDAResponse PDAResponse = new StringBodyPdaResponse(statuscode, errorinfo, requestbatchno, errorinfovediurl, body);
			return PDAResponse;
		}

		// 根据包号查找订单信息
		List<CwbOrder> cwbOrderList = this.cwbDAO.getListByPackagecodeExcel(baleNO.trim());
		List<CwbOrder> errorList = new ArrayList<CwbOrder>();
		List<BaleView> errorListView = new ArrayList<BaleView>();
		if ((cwbOrderList != null) && !cwbOrderList.isEmpty()) {
			long successCount = 0;
			long errorCount = 0;
			for (CwbOrder co : cwbOrderList) {
				try {
					// 订单出库
					CwbOrder cwbOrder = this.cwborderService.outWarehous(this.getSessionUser(), co.getCwb(), co.getCwb(), 0, 0, nextBranchid, 0, false, "", baleNO, 0, false, true);
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
				} catch (Exception e) {
					logger.error("", e);
				}
			}
			if (successCount > 0) {// 成功
				// 更改包的状态
				logger.info("outStore，出库，更改包状态，包号：" + bale.getBaleno());
				this.baleDao.updateBalesate(bale.getId(), BaleStateEnum.YiFengBaoChuKu.getValue());
				statuscode = "000000";
				errorinfo = "成功" + String.valueOf(successCount) + "件,失败" + String.valueOf(errorCount) + "件";
				errorinfovediurl = request.getContextPath() + ServiceUtil.waverrorPath + BalePDAEnum.YI_CHANG_BAO_HAO.getVediourl();
				PDAResponse PDAResponse = new StringBodyPdaResponse(statuscode, errorinfo, requestbatchno, errorinfovediurl, body);
				return PDAResponse;

			} else {// 失败
				statuscode = "100001";
				errorinfo = "成功" + String.valueOf(successCount) + "件,失败" + String.valueOf(errorCount) + "件";
				PDAResponse PDAResponse = new StringBodyPdaResponse(statuscode, errorinfo, requestbatchno, errorinfovediurl, body);
				return PDAResponse;
			}

		}
		statuscode = "100001";
		errorinfo = "出库失败";
		errorinfovediurl = request.getContextPath() + ServiceUtil.waverrorPath + BalePDAEnum.YI_CHANG_BAO_HAO.getVediourl();
		PDAResponse PDAResponse = new StringBodyPdaResponse(statuscode, errorinfo, requestbatchno, errorinfovediurl, body);
		return PDAResponse;

	}

	/**
	 * 合包
	 *
	 * @param strPara
	 * @param requestbatchno
	 * @param body
	 * @param statuscode
	 * @param errorinfo
	 * @param errorinfovediurl
	 * @param request
	 * @return
	 */
	private PDAResponse packageProcess(String strPara, long requestbatchno, StringBuffer body, String statuscode, String errorinfo, String errorinfovediurl, HttpServletRequest request) {

		try {
			Document document = DocumentHelper.parseText(strPara);
			Element rootElt = document.getRootElement();
			String baleNO = rootElt.elementTextTrim("baleNO");
			String nextbranch = rootElt.elementTextTrim("nextBranch");
			long nextBranchid = Long.parseLong(nextbranch);// branchDAO.getBranchByBranchname(nextbranch).getBranchid();
			this.baleService.fengbao(this.getSessionUser(), baleNO.trim(), nextBranchid);
			statuscode = "000000";
			PDAResponse PDAResponse = new StringBodyPdaResponse(statuscode, errorinfo, requestbatchno, errorinfovediurl, body);
			return PDAResponse;
		} catch (DocumentException e) {
			statuscode = "1000001";
			errorinfo = e.getMessage();
			errorinfovediurl = request.getContextPath() + ServiceUtil.waverrorPath + BalePDAEnum.YI_CHANG_BAO_HAO.getVediourl();
			PDAResponse PDAResponse = new StringBodyPdaResponse(statuscode, errorinfo, requestbatchno, errorinfovediurl, body);
			return PDAResponse;
		}

	}

	/**
	 * 扫描单号
	 *
	 * @param strPara
	 * @param requestbatchno
	 * @param body
	 * @param statuscode
	 * @param errorinfo
	 * @param errorinfovediurl
	 * @param request
	 * @return
	 */
	private PDAResponse checkOrderForPackage(String strPara, long requestbatchno, StringBuffer body, String statuscode, String errorinfo, String errorinfovediurl, HttpServletRequest request) {

		try {
			boolean isForceOutstore;
			Document document = DocumentHelper.parseText(strPara);
			Element rootElt = document.getRootElement();
			String baleNO = rootElt.elementTextTrim("baleNO");
			String orderNO = rootElt.elementTextTrim("orderNO");
			String nextbranch = rootElt.elementTextTrim("nextBranch");
			long nextBranchid = Long.parseLong(nextbranch);// branchDAO.getBranchByBranchname(nextbranch).getBranchid();
			String flag = rootElt.elementTextTrim("isForceOutstore");
			// 强制出库标识
			if ("1".equals(flag)) {
				isForceOutstore = true;
			} else {
				isForceOutstore = false;
			}
			// 校验包号
			this.baleService.validateBaleCheck(this.getSessionUser(), baleNO, orderNO, nextBranchid, FlowOrderTypeEnum.ChuKuSaoMiao.getValue());

			CwbOrderPDAEnum cwbOrderPDAEnum = this.verficationCwb(orderNO);
			statuscode = cwbOrderPDAEnum.getCode();
			if (CwbOrderPDAEnum.OK.getCode().equals(cwbOrderPDAEnum.getCode())) {
				logger.info("扫描单号,checkOrderForPackage,包号：" + baleNO + " , 订单号：" + orderNO);
				int iType = this.cwbDAO.getCwbByCwb(orderNO).getCwbordertypeid();
				if (iType == 1) {// 库房出库
					this.baleService.baleaddcwbChukuCheck(this.getSessionUser(), baleNO.trim(), orderNO.trim(), isForceOutstore, this.getSessionUser().getBranchid(), nextBranchid);
				} else if (iType == 2) {// 退货出站
					this.baleService.baleaddcwbTuiHuoCheck(this.getSessionUser(), baleNO, orderNO, isForceOutstore, this.getSessionUser().getBranchid(), nextBranchid);
				} else if (iType == 3) {// 中转出站
					this.baleService.baleaddcwbzhongzhuanchuzhanCheck(this.getSessionUser(), baleNO.trim(), orderNO.trim(), isForceOutstore, this.getSessionUser().getBranchid(), nextBranchid);
				} else if (iType == 4) {// 退供货商出库
					this.baleService.baleaddcwbToCustomerCheck(this.getSessionUser(), baleNO, orderNO, this.getSessionUser().getBranchid(), nextBranchid);
				} else {
					statuscode = "100001";
					PDAResponse PDAResponse = new StringBodyPdaResponse(statuscode, errorinfo, requestbatchno, errorinfovediurl, body);
					return PDAResponse;
				}

				this.baleService.baleaddcwb(this.getSessionUser(), baleNO.trim(), orderNO.trim(), nextBranchid);
				statuscode = "000000";

			} else {
				errorinfo = cwbOrderPDAEnum.getError();
				errorinfovediurl = request.getContextPath() + ServiceUtil.waverrorPath + cwbOrderPDAEnum.getVediourl();
			}
			PDAResponse PDAResponse = new StringBodyPdaResponse(statuscode, errorinfo, requestbatchno, errorinfovediurl, body);
			return PDAResponse;
		} catch (DocumentException e) {
			statuscode = "100001";
			errorinfo = e.getMessage();
			errorinfovediurl = request.getContextPath() + ServiceUtil.waverrorPath + BalePDAEnum.YI_CHANG_BAO_HAO.getVediourl();
			PDAResponse PDAResponse = new StringBodyPdaResponse(statuscode, errorinfo, requestbatchno, errorinfovediurl, body);
			return PDAResponse;
		} catch (CwbException cwbException) {
			statuscode = "100001";
			errorinfo = cwbException.getMessage();
			errorinfovediurl = request.getContextPath() + ServiceUtil.waverrorPath + BalePDAEnum.YI_CHANG_BAO_HAO.getVediourl();
			PDAResponse PDAResponse = new StringBodyPdaResponse(statuscode, errorinfo, requestbatchno, errorinfovediurl, body);
			return PDAResponse;
		} catch (Exception e) {
			statuscode = "100001";
			errorinfo = e.getMessage();
			errorinfovediurl = request.getContextPath() + ServiceUtil.waverrorPath + BalePDAEnum.YI_CHANG_BAO_HAO.getVediourl();
			PDAResponse PDAResponse = new StringBodyPdaResponse(statuscode, errorinfo, requestbatchno, errorinfovediurl, body);
			return PDAResponse;
		}

	}

	/**
	 * 生成包号
	 *
	 * @param bale
	 * @param requestbatchno
	 * @param body
	 * @param statuscode
	 * @param errorinfo
	 * @param errorinfovediurl
	 * @param request
	 * @return
	 */
	private PDAResponse generatePackageNO(String bale, long requestbatchno, StringBuffer body, String statuscode, String errorinfo, String errorinfovediurl, HttpServletRequest request) {
		body.append("<baleNO>").append(new Date().getTime()).append("</baleNO>");
		PDAResponse PDAResponse = new StringBodyPdaResponse(statuscode, errorinfo, requestbatchno, errorinfovediurl, body);
		return PDAResponse;
	}

	/**
	 * 校验包号合法性
	 *
	 * @param cwb
	 * @param requestbatchno
	 * @param body
	 * @param statuscode
	 * @param errorinfo
	 * @param errorinfovediurl
	 * @param request
	 * @return
	 */
	private PDAResponse checkPackageIllegal(String bale, long requestbatchno, StringBuffer body, String statuscode, String errorinfo, String errorinfovediurl, HttpServletRequest request) {

		String reg = "^[a-zA-Z0-9-_*]+$";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(bale);
		BalePDAEnum balePDAEnum;
		if ((bale.length() != 0) && matcher.matches()) {
			balePDAEnum = BalePDAEnum.OK;
		} else {
			balePDAEnum = BalePDAEnum.YI_CHANG_BAO_HAO;
		}

		statuscode = balePDAEnum.getCode();
		if (!statuscode.equals(BalePDAEnum.OK.getCode())) {
			errorinfo = balePDAEnum.getError();
			errorinfovediurl = request.getContextPath() + ServiceUtil.waverrorPath + balePDAEnum.getVediourl();
		}

		PDAResponse PDAResponse = new StringBodyPdaResponse(statuscode, errorinfo, requestbatchno, errorinfovediurl, body);
		return PDAResponse;
	}

	/**
	 *
	 * @param cwb
	 * @param requestbatchno
	 * @param stringBuffer
	 * @param code
	 * @param error
	 * @param string
	 * @param request
	 * @return
	 */
	private PDAResponse packagetrack(String bale, long requestbatchno, StringBuffer body, String statuscode, String errorinfo, String errorinfovediurl, HttpServletRequest request) {

		BalePDAEnum balePDAEnum = this.verficationBaleNO(bale);
		statuscode = balePDAEnum.getCode();
		if (statuscode.equals(BalePDAEnum.OK.getCode())) {
			Bale co = this.baleDao.getBaleOnway(bale);
			body.append("<bale>").append(co.getBaleno()).append("</bale>");
			long nextBranchid = co.getNextbranchid();
			Branch branch = this.branchDAO.getBranchByBranchid(nextBranchid);
			if (null != branch.getBranchname()) {
				body.append("<branch>").append(branch.getBranchname()).append("</branch>");
			} else {
				body.append("<branch>").append("</branch>");
			}
			Map<String, Object> map = this.cwbDAO.getCwbIDsByBale(bale);
			if (null != map) {
				body.append("<cwbnum>").append(map.get("cwbnum")).append("</cwbnum>");
				body.append("<transcwbnum>").append(map.get("transcwbnum")).append("</transcwbnum>");
			} else {
				body.append("<cwbnum>").append("</cwbnum>");
				body.append("<transcwbnum>").append("</transcwbnum>");
			}

		} else {
			errorinfo = balePDAEnum.getError();
			errorinfovediurl = request.getContextPath() + ServiceUtil.waverrorPath + balePDAEnum.getVediourl();
		}
		PDAResponse PDAResponse = new StringBodyPdaResponse(statuscode, errorinfo, requestbatchno, errorinfovediurl, body);
		return PDAResponse;

	}

	/**
	 * 包号打印
	 *
	 * @param request
	 * @param string3
	 * @param string2
	 * @param string
	 * @param stringBuffer
	 * @param requestbatchno
	 * @param cwb
	 * @return
	 */
	private PDAResponse packagePrintBar(String baleNO, long requestbatchno, StringBuffer body, String statuscode, String errorinfo, String errorinfovediurl, HttpServletRequest request) {
		// TODO Auto-generated method stub
		BalePDAEnum balePDAEnum = this.verficationBaleNO(baleNO);
		statuscode = balePDAEnum.getCode();
		if (statuscode.equals(BalePDAEnum.OK.getCode())) {
			Bale co = this.baleDao.getBaleOnway(baleNO);
			body.append("<bale>").append(co.getBaleno()).append("</bale>");
		} else {
			errorinfo = balePDAEnum.getError();
			errorinfovediurl = request.getContextPath() + ServiceUtil.waverrorPath + balePDAEnum.getVediourl();
		}
		PDAResponse PDAResponse = new StringBodyPdaResponse(statuscode, errorinfo, requestbatchno, errorinfovediurl, body);
		return PDAResponse;
	}

	/**
	 * 分站到货批量（PDA）
	 *
	 * @param cwbs
	 * @param driverid
	 * @param requestbatchno
	 * @param request
	 * @return
	 */
	public PDAResponse substationGoodsBatch(String cwbs, long driverid, long requestbatchno, HttpServletRequest request) {
		String[] cwbArr = cwbs.split(",");
		StringBuffer statuscode = new StringBuffer();
		StringBuffer errorMsg = new StringBuffer();
		for (String cwb : cwbArr) {
			String scancwb = cwb;
			try {
				if (cwb.trim().length() == 0) {
					continue;
				}
				cwb = this.cwborderService.translateCwb(cwb);
				this.cwborderService.substationGoods(this.getSessionUser(), cwb, scancwb, driverid, requestbatchno, "", "", false);
				statuscode = statuscode.append(cwb).append("_").append(CwbOrderPDAEnum.OK.getValue()).append(",");
				errorMsg = errorMsg.append(cwb).append("_").append(CwbOrderPDAEnum.OK.getError()).append(",");
			} catch (CwbException e) {
				CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
				this.exceptionCwbDAO.createExceptionCwbScan(cwb, e.getFlowordertye(), e.getMessage(), this.getSessionUser().getBranchid(), this.getSessionUser().getUserid(),
						cwbOrder == null ? 0 : cwbOrder.getCustomerid(), 0, 0, 0, "", scancwb);
				statuscode = statuscode.append(cwb).append("_").append(e.getError().getValue()).append(",");
				errorMsg = errorMsg.append(cwb).append("_").append(e.getMessage()).append(",");
				this.logger.error("分站到货批量异常" + cwb, e);
			} catch (Exception e) {
				statuscode = statuscode.append(cwb).append("_").append(ExceptionCwbErrorTypeEnum.SYS_ERROR.getValue()).append(",");
				errorMsg = errorMsg.append(cwb).append("_").append(e.getMessage()).append(",");
				this.logger.error("分站到货批量异常" + cwb, e);
			}
		}
		BatchDeliverBody batchDeliverBody = new BatchDeliverBody();
		batchDeliverBody.setFailcwbinfo(errorMsg.toString());

		BatchDeliverBodyPdaResponse PDAResponse = new BatchDeliverBodyPdaResponse(statuscode.toString(), errorMsg.toString());
		PDAResponse.setBody(batchDeliverBody);
		return PDAResponse;

	}

	/********************************************************* PDA异常订单监控BIGIN ***************************************************************/
	/**
	 * 分站到货异常单监控
	 *
	 * @param parm
	 *            (page,cwb,scantype,errortype,branchid)
	 * @return
	 */
	@RequestMapping("/controlForBranchImport/{page}")
	public String controlForBranchImport(Model model, @PathVariable("page") long page, @RequestParam(value = "cwb", required = false, defaultValue = "") String cwb,
			@RequestParam(value = "userid", required = false, defaultValue = "") String[] userid,
			@RequestParam(value = "flowOrderTypeEnumid", required = false, defaultValue = "0") long flowOrderTypeEnumid,
			@RequestParam(value = "beginemaildate", required = false, defaultValue = "") String beginemaildate,
			@RequestParam(value = "endemaildate", required = false, defaultValue = "") String endemaildate, @RequestParam(value = "scopeValue", required = false, defaultValue = "-1") long scope,
			@RequestParam(value = "branchid", required = false, defaultValue = "") String[] branchid, @RequestParam(value = "username", required = false, defaultValue = "") String username) {
		model.addAttribute("flowOrderTypeEnumid", flowOrderTypeEnumid);
		model.addAttribute("branchid", branchid);
		model.addAttribute("userid", userid);

		String userids = this.cwborderService.getToString(userid);
		String branchids = this.cwborderService.getToString(branchid);

		List<ExceptionCwb> eclist = new ArrayList<ExceptionCwb>();
		// List<ExceptionCwb> eclistall = new ArrayList<ExceptionCwb>();
		long count = 0;

		if ((beginemaildate.length() == 0) && (endemaildate.length() == 0)) {

		} else {
			if ((username != null) && !username.equals("") && (this.userDAO.getUserByJobnum(username) != null)) {
				String usernameforid = this.userDAO.getUserByJobnum(username).getUserid() + "";
				String usernameforbranchid = this.userDAO.getUserByJobnum(username).getBranchid() + "";
				eclist = this.cwborderService.controlForBranchImport(this.getSessionUser(), page, cwb, flowOrderTypeEnumid, "", usernameforid, beginemaildate, endemaildate, usernameforbranchid,
						scope);
				count = this.exceptionCwbDAO.getAllECCount(cwb, flowOrderTypeEnumid, "", usernameforbranchid, usernameforid, 0, beginemaildate, endemaildate, scope);

			} else {

				eclist = this.cwborderService.controlForBranchImport(this.getSessionUser(), page, cwb, flowOrderTypeEnumid, "", userids, beginemaildate, endemaildate, branchids, scope);
				// eclistall =
				// cwborderService.controlForBranchImport(getSessionUser(),-1,cwb,flowOrderTypeEnumid,"",userid,
				// beginemaildate, endemaildate,branchid);
				count = this.exceptionCwbDAO.getAllECCount(cwb, flowOrderTypeEnumid, "", branchids, userids, 0, beginemaildate, endemaildate, scope);
			}
		}

		List<Exportmould> exportmouldlist = this.exportmouldDAO.getAllExportmouldByUser(this.getSessionUser().getRoleid());
		model.addAttribute("exportmouldlist", exportmouldlist);
		List<String> strArrList = this.cwborderService.getBranchList(branchid);
		model.addAttribute("strArrList", strArrList);
		model.addAttribute("branchlist", this.branchDAO.getAllEffectBranches());
		model.addAttribute("eclist", eclist);
		// model.addAttribute("eclistall", eclistall);
		model.addAttribute("page", page);
		model.addAttribute("scopeValue", scope);
		model.addAttribute("page_obj", new Page(count, page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("beginemaildate", beginemaildate);
		model.addAttribute("endemaildate", endemaildate);
		model.addAttribute("username", username);
		model.addAttribute("ulist", this.userDAO.getAllUser());
		return "control/list";
	}

	@RequestMapping("/controlCwbForHanlder/{id}")
	public ModelAndView controlCwbForHanlder(Model model, @PathVariable("id") long id) {
		this.exceptionCwbDAO.saveExceptionCwbByCwb(id, ExceptionCwbIsHanlderEnum.YiChuLi.getValue());
		model.addAttribute("page_obj", new Page());
		return new ModelAndView("redirect:/cwborderPDA/controlForBranchImport/1");
	}

	/********************************************************* PDA异常订单监控END ***************************************************************/

	@RequestMapping("/cwbresetbranchpage")
	public String cwbresetbranchpage(Model model) {
		List<Branch> bList = this.branchDAO.getBanchByBranchidForCwbtobranchid(this.getSessionUser().getBranchid());

		model.addAttribute("branchlist", bList);
		return "cwborder/cwbresetbranchpage";
	}

	/**
	 * 单票结果反馈提交
	 *
	 * @param cwb
	 * @param deliverid
	 * @param podresultid
	 * @param backreasonid
	 * @param leavedreasonid
	 * @param receivedfeecash
	 * @param receivedfeepos
	 * @param receivedfeecheque
	 * @param receivedfeeother
	 * @param paybackedfee
	 * @param podremarkid
	 * @param requestbatchno
	 * @param outWarehouseGroupId
	 * @param
	 * @param body
	 * @param statuscode
	 * @param errorinfo
	 * @return
	 */
	private PDAResponse deliverPod(String cwb, long deliverid, long podresultid, long backreasonid, long leavedreasonid, BigDecimal receivedfeecash, BigDecimal receivedfeepos,
			BigDecimal receivedfeecheque, BigDecimal receivedfeeother, BigDecimal paybackedfee, long podremarkid, long requestbatchno, StringBuffer body, String statuscode, String errorinfo,
			String errorinfovediurl, HttpServletRequest request) {
		String scancwb = cwb;
		cwb = this.cwborderService.translateCwb(cwb);
		this.logger.info("PDA--进入单票反馈cwb:" + cwb + "--deliverid:" + deliverid + "--podresultid:" + podresultid + "--receivedfeecash:" + receivedfeecash + "--receivedfeepos:" + receivedfeepos
				+ "--receivedfeecheque:" + receivedfeecheque + "--receivedfeeother:" + receivedfeeother);

		try {
			checkVipSmh(cwb, FlowOrderTypeEnum.YiFanKui);

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("deliverid", deliverid);
			parameters.put("podresultid", podresultid);
			parameters.put("backreasonid", backreasonid);
			parameters.put("leavedreasonid", leavedreasonid);
			parameters.put("receivedfeecash", receivedfeecash);
			parameters.put("receivedfeepos", receivedfeepos);
			parameters.put("receivedfeepos", receivedfeepos);
			parameters.put("receivedfeeother", receivedfeeother);
			parameters.put("paybackedfee", paybackedfee);
			parameters.put("podremarkid", podremarkid);
			parameters.put("posremark", "");
			parameters.put("checkremark", "");
			parameters.put("deliverstateremark", "");
			parameters.put("owgid", requestbatchno);
			parameters.put("sessionbranchid", this.getSessionUser().getBranchid());
			parameters.put("sessionuserid", this.getSessionUser().getUserid());
			parameters.put("sign_typeid", SignTypeEnum.BenRenQianShou.getValue());
			parameters.put("sign_man", "");
			parameters.put("sign_time", DateTimeUtil.getNowTime());
			this.cwborderService.deliverStatePod(this.getSessionUser(), cwb, scancwb, parameters);
		} catch (Exception e) {
			this.logger.error("单票反馈出错。", e);
			statuscode = CwbOrderPDAEnum.SYS_ERROR.getCode();
			errorinfo = e.getMessage();
			errorinfovediurl = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.SYS_ERROR.getVediourl();
		}
		PDAResponse PDAResponse = new StringBodyPdaResponse(statuscode, errorinfo, requestbatchno, errorinfovediurl, body);
		return PDAResponse;
	}

	/**
	 * 派送员归班汇总
	 *
	 * @param deliverid
	 * @param requestbatchno
	 * @param outWarehouseGroupId
	 * @param
	 * @param body
	 * @param statuscode
	 * @param errorinfo
	 * @return
	 * @throws JAXBException
	 */
	private PDAResponse deliverpodStatistics(long deliverid, long requestbatchno, StringBuffer body, String statuscode, String errorinfo, String errorinfovediurl) throws JAXBException {
		// String[]
		// allParm=cwborderService.deliverpodStatistics(deliverid).split(",");
		DeliveryStateDTO dsDTO = new DeliveryStateDTO();
		List<DeliveryState> dlist = this.deliveryStateDAO.getDeliveryStateByDeliver(deliverid);
		dsDTO.analysisDeliveryStateList(this.getDeliveryStateView(dlist), bcUtil, customerDAO);

		PDAXMLBody pxb = new PDAXMLBody();
		pxb.setDelivercwbnum(dsDTO.getNowNumber());// 当日领货票数
		pxb.setDelivercwbamount(
				dsDTO.getAmount(dsDTO.getFankui_peisong_chenggongList()) + dsDTO.getAmount(dsDTO.getFankui_shangmentui_jutuiList()) + dsDTO.getAmount(dsDTO.getFankui_shangmentui_chenggongList())
						+ dsDTO.getAmount(dsDTO.getFankui_shangmenhuan_chenggongList()) + dsDTO.getAmount(dsDTO.getFankui_tuihuoList()) + dsDTO.getAmount(dsDTO.getFankui_bufentuihuoList())
						+ dsDTO.getAmount(dsDTO.getFankui_zhiliuList()) + dsDTO.getAmount(dsDTO.getFankui_diushiList()) + dsDTO.getAmount(dsDTO.getWeifankuiList()));// 当日领货金额
		pxb.setSuccesscwbnum(dsDTO.getFankui_peisong_chenggong());// 成功合计票数
		pxb.setSuccesscwbamount(dsDTO.getAmount(dsDTO.getFankui_peisong_chenggongList()));// 成功实收金额
		pxb.setBackcwbnum(dsDTO.getTuihuoAllNumber());// 退货票数（含半退 上门拒退）
		pxb.setBackcwbamount(dsDTO.getAmount(dsDTO.getFankui_shangmentui_jutuiList()) + dsDTO.getAmount(dsDTO.getFankui_bufentuihuoList()));// 退货应收金额
		pxb.setLeavedcwbnum(dsDTO.getFankui_zhiliu() - dsDTO.getFankui_zhiliu_zanbuchuli());// 分站滞留票数
		pxb.setLeavedcwbamount(dsDTO.getAmount(dsDTO.getFankui_zhiliuList()));// 滞留应收金额
		pxb.setNopodcwbnum(dsDTO.getWeifankuiNumber());// 未反馈票数
		pxb.setNopodcwbamount(dsDTO.getWeiFanKuiAmount(dsDTO.getWeifankuiList()));// 未反馈金额
		pxb.setPodnoconfirmcwb(dsDTO.getLishi_weishenhe());// 未归班票数
		pxb.setPodnoconfirmcwbamount(dsDTO.getAmount(dsDTO.getLishi_weishenheList()));// 未归班金额
		pxb.setLeavednopodcwbnum(dsDTO.getYiliu());// 遗留订单票数
		pxb.setLeavednopodcwbamount(dsDTO.getAmount(dsDTO.getYiliuList()));// 遗留订单金额

		PDAResponse pDAResponse = new StringBodyXMLPdaResponse(statuscode, errorinfo, requestbatchno, errorinfovediurl, pxb);

		return pDAResponse;
	}

	public List<DeliveryStateView> getDeliveryStateView(List<DeliveryState> dsList) {
		List<DeliveryStateView> deliveryStateViewList = new ArrayList<DeliveryStateView>();
		if (dsList.size() > 0) {
			for (DeliveryState ds : dsList) {
				DeliveryStateView sdv = new DeliveryStateView();
				sdv.setId(ds.getId());
				sdv.setCwb(ds.getCwb());
				sdv.setDeliveryid(ds.getDeliveryid());
				sdv.setReceivedfee(ds.getReceivedfee());
				sdv.setReturnedfee(ds.getReturnedfee());
				sdv.setBusinessfee(ds.getBusinessfee());
				sdv.setDeliverystate(ds.getDeliverystate());
				sdv.setCash(ds.getCash());
				sdv.setPos(ds.getPos());
				sdv.setPosremark(ds.getPosremark());
				sdv.setMobilepodtime(ds.getMobilepodtime());
				sdv.setCheckfee(ds.getCheckfee());
				sdv.setCheckremark(ds.getCheckremark());
				sdv.setReceivedfeeuser(ds.getReceivedfeeuser());
				sdv.setCreatetime(ds.getCreatetime());
				sdv.setOtherfee(ds.getOtherfee());
				sdv.setPodremarkid(ds.getPodremarkid());
				sdv.setDeliverstateremark(ds.getDeliverstateremark());
				sdv.setIsout(ds.getIsout());
				sdv.setPos_feedback_flag(ds.getPos_feedback_flag());
				sdv.setUserid(ds.getUserid());
				sdv.setGcaid(ds.getGcaid());
				sdv.setSign_typeid(ds.getSign_typeid());
				sdv.setSign_man(ds.getSign_man());
				sdv.setSign_time(ds.getSign_time());
				sdv.setBackreason(ds.getBackreason());
				sdv.setLeavedreason(ds.getLeavedreason());
				CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(ds.getCwb());
				sdv.setCustomername(this.customerDAO.getCustomerById(cwbOrder.getCustomerid()).getCustomername());
				sdv.setEmaildate(cwbOrder.getEmaildate());
				sdv.setConsigneename(cwbOrder.getConsigneename());
				sdv.setConsigneemobile(cwbOrder.getConsigneemobile());
				sdv.setConsigneephone(cwbOrder.getConsigneephone());
				sdv.setConsigneeaddress(cwbOrder.getConsigneeaddress());
				sdv.setBackcarname(cwbOrder.getBackcarname());
				sdv.setSendcarname(cwbOrder.getSendcarname());
				sdv.setDeliverealname(this.userDAO.getUserByUserid(ds.getDeliveryid()).getRealname());
				sdv.setFlowordertype(cwbOrder.getFlowordertype());
				sdv.setCwbordertypeid(cwbOrder.getCwbordertypeid());

				deliveryStateViewList.add(sdv);
			}

		}

		return deliveryStateViewList;
	}

	/**
	 * 订单状态查询（查询单号提交）
	 *
	 * @param cwb
	 * @param requestbatchno
	 * @param outWarehouseGroupId
	 * @param
	 * @param body
	 * @param statuscode
	 * @param errorinfo
	 * @return
	 */
	private PDAResponse cwbtrack(String cwb, long requestbatchno, StringBuffer body, String statuscode, String errorinfo, String errorinfovediurl, HttpServletRequest request) {
		String scancwb = cwb;
		cwb = this.cwborderService.translateCwb(cwb);
		CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);

		if (co == null) {
			throw new CwbException(cwb, ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}

		String isHWHQPDASelect = this.systemInstallDAO.getSystemInstallByName("isHWHQPDASelect") == null ? "no" : this.systemInstallDAO.getSystemInstallByName("isHWHQPDASelect").getValue();

		OrderFlow of = this.orderFlowDAO.getOrderFlowByCwbAndState(cwb, 1);
		String trackevent = "";
		String branchname = "";
		String realname = "";
		for (FlowOrderTypeEnum fot : FlowOrderTypeEnum.values()) {
			if (of.getFlowordertype() == fot.getValue()) {
				trackevent = fot.getText();
			}
		}
		branchname = this.branchDAO.getBranchByBranchid(of.getBranchid()).getBranchname();
		realname = this.userDAO.getUserByUserid(of.getUserid()).getRealname();
		StringBuilder cwbTrackBody = new StringBuilder();
		cwbTrackBody.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
		cwbTrackBody.append("  ");
		cwbTrackBody.append(branchname);
		cwbTrackBody.append("  ");
		cwbTrackBody.append(trackevent + "（" + realname + "）");
		cwbTrackBody.append("  ");

		errorinfo = errorinfo + " " + co.getCwb() + "（订单号）";
		if (this.transCwbDao.getCwbByTransCwb(scancwb) != null) {
			errorinfo = errorinfo + " /" + scancwb + "（运单号）";
		}

		CwbTrackBodyPdaResponse PDAResponse = new CwbTrackBodyPdaResponse(statuscode, errorinfo, requestbatchno, errorinfovediurl, new CwbTrackBody());
		List<String> strings = new ArrayList<String>();
		if (isHWHQPDASelect.equals("xhm")) {
			strings.add(cwbTrackBody.toString());
		} else {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long isypdjusetranscwb = this.customerDAO.getCustomerById(co.getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(co.getCustomerid()).getIsypdjusetranscwb();
			if (((co.getSendcarnum() > 1) || (co.getBackcarnum() > 1)) && ((co.getTranscwb().split(",").length > 1) || (co.getTranscwb().split(":").length > 1)) && (isypdjusetranscwb == 1)) {
				List<TranscwbOrderFlow> datalist = this.transcwbOrderFlowDAO.getTranscwbOrderFlowByScanCwb(scancwb, this.cwborderService.translateCwb(scancwb));

				for (TranscwbOrderFlow transcwborderFlowAll : datalist) {
					StringBuilder stringBuilder = new StringBuilder();
					long sitetype = this.branchDAO.getBranchByBranchid(transcwborderFlowAll.getBranchid()).getBranchid() == 0 ? 0
							: this.branchDAO.getBranchByBranchid(transcwborderFlowAll.getBranchid()).getSitetype();
					if (isHWHQPDASelect.equals("yes")) {
						if ((sitetype == BranchEnum.KuFang.getValue()) && ((transcwborderFlowAll.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue())
								|| (transcwborderFlowAll.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()))) {
							long deliverornextbranchid = transcwborderFlowAll.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue()
									? JSONObject.fromObject(JSONObject.fromObject(transcwborderFlowAll.getFloworderdetail()).getString("cwbOrder")).getLong("deliverybranchid")
									: JSONObject.fromObject(JSONObject.fromObject(transcwborderFlowAll.getFloworderdetail()).getString("cwbOrder")).getLong("nextbranchid");
							String bname = this.branchDAO.getBranchByBranchid(deliverornextbranchid).getBranchid() == 0 ? ""
									: this.branchDAO.getBranchByBranchid(deliverornextbranchid).getBranchname();

							stringBuilder.append(simpleDateFormat.format(transcwborderFlowAll.getCredate()));
							stringBuilder.append("  ");
							stringBuilder.append(this.userDAO.getUserByUserid(transcwborderFlowAll.getUserid()).getRealname());
							stringBuilder.append(" ");
							stringBuilder.append(transcwborderFlowAll.getFlowordertypeText());
							stringBuilder.append("\n");
							stringBuilder.append(bname);
							stringBuilder.append(" ");
							strings.add(stringBuilder.toString());
						}
					} else {
						long deliverornextbranchid = ((transcwborderFlowAll.getFlowordertype() == FlowOrderTypeEnum.DaoRuShuJu.getValue())
								|| (transcwborderFlowAll.getFlowordertype() == FlowOrderTypeEnum.UpdateDeliveryBranch.getValue())
								|| (transcwborderFlowAll.getFlowordertype() == FlowOrderTypeEnum.ShouGongXiuGai.getValue()))
										? JSONObject.fromObject(JSONObject.fromObject(transcwborderFlowAll.getFloworderdetail()).getString("cwbOrder")).getLong("deliverybranchid")
										: JSONObject.fromObject(JSONObject.fromObject(transcwborderFlowAll.getFloworderdetail()).getString("cwbOrder")).getLong("nextbranchid");
						String bname = this.branchDAO.getBranchByBranchid(deliverornextbranchid).getBranchid() == 0 ? "" : this.branchDAO.getBranchByBranchid(deliverornextbranchid).getBranchname();
						stringBuilder.append(simpleDateFormat.format(transcwborderFlowAll.getCredate()));
						stringBuilder.append("  ");
						stringBuilder.append(this.userDAO.getUserByUserid(transcwborderFlowAll.getUserid()).getRealname());
						stringBuilder.append("\n");
						stringBuilder.append(this.branchDAO.getBranchByBranchid(transcwborderFlowAll.getBranchid()).getBranchname());
						stringBuilder.append(" ");
						stringBuilder.append(transcwborderFlowAll.getFlowordertypeText());
						if ((transcwborderFlowAll.getFlowordertype() == FlowOrderTypeEnum.DaoRuShuJu.getValue())
								|| (transcwborderFlowAll.getFlowordertype() == FlowOrderTypeEnum.UpdateDeliveryBranch.getValue())
								|| (transcwborderFlowAll.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue())
								|| (transcwborderFlowAll.getFlowordertype() == FlowOrderTypeEnum.TuiHuoChuZhan.getValue())
								|| (transcwborderFlowAll.getFlowordertype() == FlowOrderTypeEnum.ShouGongXiuGai.getValue())) {
							stringBuilder.append("\n");
							stringBuilder.append(bname);
							stringBuilder.append(" ");
						}
						strings.add(stringBuilder.toString());
					}
				}
			} else {
				List<OrderFlow> orderFlows = this.orderFlowDAO.getOrderFlowByCwb(cwb);

				for (OrderFlow orderFlow : orderFlows) {
					StringBuilder stringBuilder = new StringBuilder();
					Branch branch = this.branchDAO.getBranchByBranchid(orderFlow.getBranchid());
					long sitetype = branch.getBranchid() == 0 ? 0 : branch.getSitetype();
					if (isHWHQPDASelect.equals("yes")) {
						if ((sitetype == BranchEnum.KuFang.getValue())
								&& ((orderFlow.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue()) || (orderFlow.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()))) {
							long deliverornextbranchid = orderFlow.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue()
									? JSONObject.fromObject(JSONObject.fromObject(orderFlow.getFloworderdetail()).getString("cwbOrder")).getLong("deliverybranchid")
									: JSONObject.fromObject(JSONObject.fromObject(orderFlow.getFloworderdetail()).getString("cwbOrder")).getLong("nextbranchid");
							String bname = this.branchDAO.getBranchByBranchid(deliverornextbranchid).getBranchid() == 0 ? ""
									: this.branchDAO.getBranchByBranchid(deliverornextbranchid).getBranchname();

							stringBuilder.append(simpleDateFormat.format(orderFlow.getCredate()));
							stringBuilder.append("  ");
							stringBuilder.append(this.userDAO.getUserByUserid(orderFlow.getUserid()).getRealname());
							stringBuilder.append(" ");
							stringBuilder.append(orderFlow.getFlowordertypeText());
							stringBuilder.append("\n");
							stringBuilder.append(bname);
							stringBuilder.append(" ");
							strings.add(stringBuilder.toString());
						}
					} else {
						long deliverornextbranchid = ((orderFlow.getFlowordertype() == FlowOrderTypeEnum.DaoRuShuJu.getValue())
								|| (orderFlow.getFlowordertype() == FlowOrderTypeEnum.UpdateDeliveryBranch.getValue()) || (orderFlow.getFlowordertype() == FlowOrderTypeEnum.ShouGongXiuGai.getValue()))
										? JSONObject.fromObject(JSONObject.fromObject(orderFlow.getFloworderdetail()).getString("cwbOrder")).getLong("deliverybranchid")
										: JSONObject.fromObject(JSONObject.fromObject(orderFlow.getFloworderdetail()).getString("cwbOrder")).getLong("nextbranchid");
						String bname = this.branchDAO.getBranchByBranchid(deliverornextbranchid).getBranchid() == 0 ? "" : this.branchDAO.getBranchByBranchid(deliverornextbranchid).getBranchname();
						stringBuilder.append(simpleDateFormat.format(orderFlow.getCredate()));
						stringBuilder.append("  ");
						stringBuilder.append(this.userDAO.getUserByUserid(orderFlow.getUserid()).getRealname());
						stringBuilder.append("\n");
						stringBuilder.append(this.branchDAO.getBranchByBranchid(orderFlow.getBranchid()).getBranchname());
						stringBuilder.append(" ");
						stringBuilder.append(orderFlow.getFlowordertypeText());
						if ((orderFlow.getFlowordertype() == FlowOrderTypeEnum.DaoRuShuJu.getValue()) || (orderFlow.getFlowordertype() == FlowOrderTypeEnum.UpdateDeliveryBranch.getValue())
								|| (orderFlow.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) || (orderFlow.getFlowordertype() == FlowOrderTypeEnum.TuiHuoChuZhan.getValue())
								|| (orderFlow.getFlowordertype() == FlowOrderTypeEnum.ShouGongXiuGai.getValue())) {
							stringBuilder.append("\n");
							stringBuilder.append(bname);
							stringBuilder.append(" ");
						}
						strings.add(stringBuilder.toString());
					}
				}
			}

		}
		PDAResponse.setOrderFlows(strings);

		return PDAResponse;
	}

	/**
	 * 声音文件下载
	 *
	 * @param requestbatchno
	 * @param outWarehouseGroupId
	 * @param
	 * @param body
	 * @param statuscode
	 * @param errorinfo
	 * @return
	 */
	private PDAResponse selectwavfiles(long requestbatchno, StringBuffer body, String statuscode, String errorinfo, String errorinfovediurl) {
		List<Branch> blist = this.branchDAO.getAllBranches();
		String wavfilenames = "";
		PDAXMLBody pxb = new PDAXMLBody();
		for (Branch b : blist) {
			if ((b.getBranchwavfile() != null) && (b.getBranchwavfile().length() > 0)) {
				wavfilenames += b.getBranchwavfile() + ",";
			}
		}
		pxb.setWavfilenames(wavfilenames);

		PDAResponse pDAResponse = new StringBodyXMLPdaResponse(statuscode, errorinfo, requestbatchno, errorinfovediurl, pxb);
		return pDAResponse;
	}

	/**
	 * 标签打印
	 *
	 * @param cwb
	 * @param requestbatchno
	 * @param outWarehouseGroupId
	 * @param
	 * @param body
	 * @param statuscode
	 * @param errorinfo
	 * @param request
	 * @return
	 */
	private PDAResponse printBar(String cwb, long requestbatchno, StringBuffer body, String statuscode, String errorinfo, String errorinfovediurl, HttpServletRequest request) {

		CwbOrderPDAEnum PDAErrorEnum = this.verficationCwb(cwb);
		statuscode = PDAErrorEnum.getCode();
		if (statuscode.equals(CwbOrderPDAEnum.OK.getCode())) {
			CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);
			body.append("<cwb>").append(co.getCwb()).append("</cwb>");
			if (co != null) {
				// 分拣到站的信息输出 如果当前站点和下一站id不一致 则认为是分拣到站，如果小件员字段没有值也将提示站信息
				if ((co.getStartbranchid() != co.getNextbranchid()) || (co.getExceldeliver() == null) || (co.getExceldeliver().length() == 0)) {
					List<Branch> bl = this.branchDAO.getBranchByBranchnameCheck(co.getExcelbranch());
					if ((co.getExcelbranch() != null) && (co.getExcelbranch().length() > 0) && (bl.size() > 0)) {
						Branch branch = bl.get(0);
						body.append("<cwbbranchname>").append(co.getExcelbranch() + "(" + branch.getBranchcode() + ")").append("</cwbbranchname>");

						if ((branch.getBranchwavfile() == null) || (branch.getBranchwavfile().length() < 1)) {
							body.append("<cwbbranchnamewav></cwbbranchnamewav>");
						} else {
							body.append("<cwbbranchnamewav>").append(request.getContextPath() + ServiceUtil.wavPath + branch.getBranchwavfile()).append("</cwbbranchnamewav>");
						}

						body.append("<cwbdelivername></cwbdelivername>");
						body.append("<cwbdelivernamewav></cwbdelivernamewav>");
						body.append("<cwbreceivablefee>").append(co.getReceivablefee()).append("</cwbreceivablefee>");
					} else {
						body.append("<cwbbranchname></cwbbranchname>");
						body.append("<cwbbranchnamewav></cwbbranchnamewav>");
						body.append("<cwbdelivername></cwbdelivername>");
						body.append("<cwbdelivernamewav></cwbdelivernamewav>");
						body.append("<cwbreceivablefee></cwbreceivablefee>");
					}
				} else {
					body.append("<cwbbranchname></cwbbranchname>");
					body.append("<cwbbranchnamewav></cwbbranchnamewav>");
					body.append("<cwbdelivername>").append(co.getExceldeliver()).append("</cwbdelivername>");
					String roleids = "2,4";
					User user = this.userDAO.getUsersByRealnameAndRole(co.getExceldeliver(), roleids);
					if ((user.getUserwavfile() != null) && (user.getUserwavfile().length() > 0)) {
						body.append("<cwbdelivernamewav>").append(request.getContextPath() + ServiceUtil.wavPath + user.getUserwavfile()).append("</cwbdelivernamewav>");
					} else {
						body.append("<cwbdelivernamewav></cwbdelivernamewav>");
					}
					body.append("<cwbreceivablefee>").append(co.getReceivablefee()).append("</cwbreceivablefee>");
				}
			}
		} else {
			errorinfo = PDAErrorEnum.getError();
			errorinfovediurl = request.getContextPath() + ServiceUtil.waverrorPath + PDAErrorEnum.getVediourl();
		}
		PDAResponse PDAResponse = new StringBodyPdaResponse(statuscode, errorinfo, requestbatchno, errorinfovediurl, body);
		return PDAResponse;
	}

	/**
	 * 订单扫描查询
	 *
	 * @param cwb
	 * @param requestbatchno
	 * @param outWarehouseGroupId
	 * @param
	 * @param body
	 * @param statuscode
	 * @param errorinfo
	 * @return
	 */
	private PDAResponse ScanCwb(String cwb, long requestbatchno, StringBuffer body, String statuscode, String errorinfo, String errorinfovediurl) {
		CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);
		PdaCwbOrderView view = this.toCwbOrderView(co);
		return new CwborderViewBodyPdaResponse(statuscode, errorinfo, requestbatchno, errorinfovediurl, view);
	}

	private PdaCwbOrderView toCwbOrderView(CwbOrder co) {
		PdaCwbOrderView cwbOrderView = new PdaCwbOrderView();
		cwbOrderView.setConsigneename(co.getConsigneename());
		cwbOrderView.setCwb(co.getCwb());
		cwbOrderView.setPaybackfee(co.getPaybackfee().toString());
		cwbOrderView.setReceivablefee(co.getReceivablefee().toString());
		cwbOrderView.setCwbordertype(CwbOrderTypeIdEnum.getByValue(co.getCwbordertypeid()).getText());
		return cwbOrderView;
	}

	/**
	 * 配送结果列表查询
	 *
	 * @param dlist
	 * @param requestbatchno
	 * @param outWarehouseGroupId
	 * @param
	 * @param body
	 * @param statuscode
	 * @param errorinfo
	 * @return
	 */
	private PDAResponse SelectResult(DeliveryStateEnum[] dlist, long requestbatchno, StringBuffer body, String statuscode, String errorinfo, String errorinfovediurl) {
		for (DeliveryStateEnum d : dlist) {
			body.append("<podresultid>").append(d.getValue()).append("</podresultid>");
			body.append("<podresultname>").append(d.getText()).append("</podresultname>");
		}
		PDAResponse PDAResponse = new StringBodyPdaResponse(statuscode, errorinfo, requestbatchno, errorinfovediurl, body);
		return PDAResponse;
	}

	/**
	 * 退货原因列表查询
	 *
	 * @param rlist
	 * @param requestbatchno
	 * @param outWarehouseGroupId
	 * @param
	 * @param body
	 * @param statuscode
	 * @param errorinfo
	 * @return
	 */
	private PDAResponse SelectReturnResult(List<Reason> rList, long requestbatchno, StringBuffer body, String statuscode, String errorinfo, String errorinfovediurl) {
		PDAResponse PDAResponse = new ReasonListBodyPdaResponse(statuscode, errorinfo, requestbatchno, errorinfovediurl, rList);
		return PDAResponse;
	}

	/**
	 * 配送结果备注列表查询
	 *
	 * @param rlist
	 * @param requestbatchno
	 * @param outWarehouseGroupId
	 * @param
	 * @param body
	 * @param statuscode
	 * @param errorinfo
	 * @return
	 */
	private PDAResponse SelectRemarkResult(List<Reason> rList, long requestbatchno, StringBuffer body, String statuscode, String errorinfo, String errorinfovediurl) {

		PDAResponse PDAResponse = new ReasonListBodyPdaResponse(statuscode, errorinfo, requestbatchno, errorinfovediurl, rList);
		return PDAResponse;
	}

	private CustomerListBodyPdaResponse getCustomers(List<Customer> cList, long requestbatchno, StringBuffer body, String statuscode, String errorinfo, String errorinfovediurl) {
		CustomerListBodyPdaResponse PDAResponse = new CustomerListBodyPdaResponse(statuscode, errorinfo);
		PDAResponse.setBody(cList);
		return PDAResponse;
	}

	private PDAResponse getUsers(List<User> uList, long requestbatchno, StringBuffer body, String statuscode, String errorinfo, String errorinfovediurl) {
		List<UserView> drivers = this.getUserViewList(uList);
		UserViewBodyPdaResponse PDAResponse = new UserViewBodyPdaResponse(statuscode, errorinfo);
		PDAResponse.setBody(drivers);
		return PDAResponse;
	}

	private List<UserView> getUserViewList(List<User> uList) {
		List<UserView> drivers = new ArrayList<UserView>();
		for (User u : uList) {
			UserView driver = new UserView();
			driver.setUserid(u.getUserid());
			driver.setUsername(u.getRealname());
			drivers.add(driver);
		}
		return drivers;
	}

	private PDAResponse getBranch(List<Branch> bList, long requestbatchno, StringBuffer body, String statuscode, String errorinfo, String errorinfovediurl) {
		BranchListBodyPdaResponse PDAResponse = new BranchListBodyPdaResponse(statuscode, errorinfo);
		PDAResponse.setBody(bList);
		return PDAResponse;
	}

	private PDAResponse getTruck(List<Truck> tList, long requestbatchno, StringBuffer body, String statuscode, String errorinfo, String errorinfovediurl) {
		TruckListBodyPdaResponse PDAResponse = new TruckListBodyPdaResponse(statuscode, errorinfo);
		PDAResponse.setBody(tList);
		return PDAResponse;
	}

	private PDAResponse getUser(List<User> uList, long requestbatchno, StringBuffer body, String statuscode, String errorinfo, String errorinfovediurl) {
		List<UserView> drivers = this.getUserViewList(uList);
		UserViewBodyPdaResponse PDAResponse = new UserViewBodyPdaResponse(statuscode, errorinfo);
		PDAResponse.setBody(drivers);
		return PDAResponse;
	}

	private PDAResponse getReason(List<Reason> rList, long requestbatchno, StringBuffer body, String statuscode, String errorinfo, String errorinfovediurl) {
		PDAResponse PDAResponse = new ReasonListBodyPdaResponse(statuscode, errorinfo, requestbatchno, errorinfovediurl, rList);
		return PDAResponse;
	}

	private PDAResponse getLeaveReason(List<Reason> rList, long requestbatchno, StringBuffer body, String statuscode, String errorinfo, String errorinfovediurl) {
		PDAResponse PDAResponse = new ReasonListBodyPdaResponse(statuscode, errorinfo, requestbatchno, errorinfovediurl, rList);
		return PDAResponse;
	}

	// 提货
	private PDAResponse getGoods(String cwb, long customerid, long driverid, long requestbatchno, StringBuffer body, String statuscode, String errorinfo, String errorinfovediurl,
			HttpServletRequest request) {
		CwbOrder co = this.cwborderService.intoWarehousForGetGoods(this.getSessionUser(), cwb, requestbatchno, customerid);

		PDAXMLBody pxb = new PDAXMLBody();

		pxb.setCwb(cwb);

		if (co != null) {

			if ((co.getSendcarnum() > 1) || (co.getBackcarnum() > 1)) {
				errorinfo += " " + CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getError();
				errorinfovediurl = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getVediourl();
			}
			// 分拣到站的信息输出 如果当前站点和下一站id不一致 则认为是分拣到站，如果小件员字段没有值也将提示站信息
			if ((co.getStartbranchid() != co.getNextbranchid()) || (co.getExceldeliver() == null) || (co.getExceldeliver().length() == 0)) {
				List<Branch> bl = this.branchDAO.getBranchByBranchnameCheck(co.getExcelbranch());
				if ((co.getExcelbranch() != null) && (co.getExcelbranch().length() > 0) && (bl.size() > 0)) {
					Branch branch = bl.get(0);
					pxb.setCwbbranchname(co.getExcelbranch() + "(" + branch.getBranchcode() + ")");

					if ((branch.getBranchwavfile() == null) || (branch.getBranchwavfile().length() < 1)) {
						pxb.setCwbbranchnamewav("");
					} else {
						pxb.setCwbbranchnamewav(request.getContextPath() + ServiceUtil.wavPath + branch.getBranchwavfile());
						errorinfovediurl = request.getContextPath() + ServiceUtil.wavPath + branch.getBranchwavfile();
					}
					pxb.setCwbdelivername("");
					pxb.setCwbdelivernamewav("");
				} else {
					pxb.setCwbbranchname("");
					pxb.setCwbbranchnamewav("");
					pxb.setCwbdelivername("");
					pxb.setCwbdelivernamewav("");
				}
			} else {
				pxb.setCwbbranchname("");
				pxb.setCwbbranchnamewav("");
				pxb.setCwbdelivername(co.getExceldeliver());
				String roleids = "2,4";
				User user = this.userDAO.getUsersByRealnameAndRole(co.getExceldeliver(), roleids);
				if ((user.getUserwavfile() != null) && (user.getUserwavfile().length() > 0)) {
					pxb.setCwbdelivernamewav(request.getContextPath() + ServiceUtil.wavPath + user.getUserwavfile());
				}
			}
			pxb.setCwbreceivablefee(co.getReceivablefee());

			if ((co.getReceivablefee() != null) && (co.getReceivablefee().compareTo(this.exceedFeeDAO.getExceedFee().getExceedfee()) > 0)) {
				errorinfo += " " + CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getError();
				errorinfovediurl = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.GAO_JIA.getVediourl();
			}

		}

		if (statuscode.equals(CwbOrderPDAEnum.YOU_HUO_WU_DAN.getCode())) {
			errorinfo = CwbOrderPDAEnum.YOU_HUO_WU_DAN.getError();
			errorinfovediurl = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.YOU_HUO_WU_DAN.getVediourl();
		}

		PDAResponse pDAResponse = new StringBodyXMLPdaResponse(statuscode, errorinfo, requestbatchno, errorinfovediurl, pxb);
		return pDAResponse;
	}

	// 入库扫描
	private PDAResponse intoWarehous(String cwb, String customerid, long driverid, long requestbatchno, String baleno, HttpServletRequest request, String comments) {
		CwbOrder co;
		String scancwb = cwb;
		try {
			cwb = this.cwborderService.translateCwb(cwb);
			co = this.cwbDAO.getCwbByCwb(cwb);

			Branch userbranch = this.branchDAO.getBranchById(this.getSessionUser().getBranchid());
			if ((userbranch.getBranchid() != 0) && (userbranch.getSitetype() == BranchEnum.ZhanDian.getValue())) {
				co = this.cwborderService.substationGoods(this.getSessionUser(), cwb, scancwb, driverid, requestbatchno, "", baleno, false);
			} else {
				co = this.cwborderService.intoWarehous(this.getSessionUser(), cwb, scancwb, Long.parseLong(customerid), driverid, requestbatchno, comments, baleno, false);
			}

			String errorinfo = this.getErrorInfoForIntoWarehouse(co);
			PDAResponse pDAResponse = new PDAResponse(CwbOrderPDAEnum.OK.getCode(), errorinfo);
			pDAResponse.setScannum(co.getScannum());
			pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());

			String RUKUPCandPDAaboutYJDPWAV = this.systemInstallDAO.getSystemInstall("RUKUPCandPDAaboutYJDPWAV") == null ? "yes"
					: this.systemInstallDAO.getSystemInstall("RUKUPCandPDAaboutYJDPWAV").getValue();

			if (RUKUPCandPDAaboutYJDPWAV.equals("yes") && ((co.getSendcarnum() > 1) || (co.getBackcarnum() > 1))) {
				pDAResponse.setShouldShock(true);
				pDAResponse.setPrintinfo(pDAResponse.getPrintinfo() + " " + CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getError());
				pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getVediourl());
			}
			if (co.getDeliverid() != 0) {
				User user = this.userDAO.getUserByUserid(co.getDeliverid());
				if ((user.getUserwavfile() != null) && (user.getUserwavfile().length() > 0)) {
					pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.wavPath + user.getUserwavfile());
				}
				pDAResponse.setPrintinfo(pDAResponse.getPrintinfo() + " " + user.getRealname());
			}
			if (co.getNextbranchid() != 0) {
				Branch branch = this.branchDAO.getBranchByBranchid(co.getNextbranchid());
				if ((branch != null) && (branch.getBranchwavfile() != null) && (branch.getBranchwavfile().length() > 0)) {
					pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.wavPath + branch.getBranchwavfile());
				} else {
					pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());
				}
				pDAResponse.setPrintinfo(pDAResponse.getPrintinfo() + " " + branch.getBranchname());
			}
			if ((co.getReceivablefee() != null) && (co.getReceivablefee().compareTo(this.exceedFeeDAO.getExceedFee().getExceedfee()) > 0)) {
				pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.GAO_JIA.getVediourl());
				pDAResponse.setPrintinfo(pDAResponse.getPrintinfo() + " " + CwbOrderPDAEnum.GAO_JIA.getError());
				pDAResponse.setShouldShock(true);
			}
			this.logger.info("入库完成，订单号：{},当前站：{},下一站：" + co.getNextbranchid() + "", co.getCwb(), co.getCurrentbranchid());
			return pDAResponse;
		} catch (CwbException e) {
			co = this.cwbDAO.getCwbByCwb(cwb);
			this.exceptionCwbDAO.createExceptionCwbScan(cwb, e.getFlowordertye(), e.getMessage(), this.getSessionUser().getBranchid(), this.getSessionUser().getUserid(),
					co == null ? 0 : co.getCustomerid(), 0, 0, 0, "", scancwb);
			if (e.getError().getValue() == ExceptionCwbErrorTypeEnum.CHONG_FU_RU_KU.getValue()) {
				Branch branch = this.branchDAO.getBranchByBranchid(co.getNextbranchid());
				PDAResponse pDAResponse = new PDAResponse(CwbOrderPDAEnum.CHONG_FU_RU_KU.getCode(),
						cwb + CwbOrderPDAEnum.CHONG_FU_RU_KU.getError() + " " + StringUtil.nullConvertToEmptyString(branch.getBranchname()));
				pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.CHONG_FU_RU_KU.getVediourl());
				pDAResponse.setPrintinfo(pDAResponse.getPrintinfo() + " " + CwbOrderPDAEnum.CHONG_FU_RU_KU.getError());
				this.logger.info("重复入库，订单号：{}", co.getCwb());
				return pDAResponse;
			} else {
				throw e;
			}
		}

	}

	private String getErrorInfoForIntoWarehouse(CwbOrder co) {
		// 修改APP提示声音 vic.liang@pjbest.com 2016-07-19
		String successPattern = this.systemInstallService.getParameter("prompt.intoWarehouse.successPattern", "<co.cwb><fenge><yipiaoduojian><branch.branchname><gaojia>");
		ST errorInfoST = new ST(successPattern);
		errorInfoST.add("co", co);
		errorInfoST.add("fenge", "#");
		if ((co.getSendcarnum() > 1) || (co.getBackcarnum() > 1)) {
			errorInfoST.add("yipiaoduojian", CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getError());
		}
		/*
		 * if(co.getDeliverid()!=0){ User user =
		 * userDAO.getUserByUserid(co.getDeliverid());
		 * errorInfoST.add("deliver", user); }
		 */
		if (co.getNextbranchid() != 0) {
			Branch branch = this.branchDAO.getBranchByBranchid(co.getNextbranchid());
			errorInfoST.add("branch", branch);
		}
		if ((co.getReceivablefee() != null) && (co.getReceivablefee().compareTo(this.exceedFeeDAO.getExceedFee().getExceedfee()) > 0)) {
			errorInfoST.add("gaojia", CwbOrderPDAEnum.GAO_JIA.getError());
		}

		logger.info("入库/出库 返回app的errorinfo:  " + errorInfoST.render());
		return errorInfoST.render();
	}

	// 退货站入库扫描
	private PDAResponse backintoWarehous(String cwb, long driverid, long requestbatchno, HttpServletRequest request, String comments) {
		String scancwb = cwb;
		try {
			cwb = this.cwborderService.translateCwb(cwb);
			CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);
			co = this.cwborderService.backIntoWarehous(this.getSessionUser(), cwb, scancwb, driverid, requestbatchno, comments, false, 0, 0);
			PDAResponse pDAResponse = new PDAResponse(CwbOrderPDAEnum.OK.getCode(), co.getCwb() + "成功扫描");
			pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());
			if ((co.getSendcarnum() > 1) || (co.getBackcarnum() > 1)) {
				pDAResponse.setShouldShock(true);
				pDAResponse.setErrorinfo(pDAResponse.getErrorinfo() + " " + CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getError());
				pDAResponse.setPrintinfo(pDAResponse.getPrintinfo() + " " + CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getError());
				pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getVediourl());
			}
			if (co.getDeliverid() != 0) {
				User user = this.userDAO.getUserByUserid(co.getDeliverid());
				if ((user.getUserwavfile() != null) && (user.getUserwavfile().length() > 0)) {
					pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.wavPath + user.getUserwavfile());
				}
				pDAResponse.setErrorinfo(cwb + " " + CwbOrderPDAEnum.OK.getError());
				pDAResponse.setPrintinfo(cwb + " " + CwbOrderPDAEnum.OK.getError());
			}
			if (co.getNextbranchid() != 0) {
				Branch branch = this.branchDAO.getBranchByBranchid(co.getNextbranchid());
				pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.wavPath + (branch.getBranchwavfile() == null ? "" : branch.getBranchwavfile()));
				pDAResponse.setErrorinfo(cwb + " " + CwbOrderPDAEnum.OK.getError());
				pDAResponse.setPrintinfo(cwb + " " + CwbOrderPDAEnum.OK.getError());
			}
			if ((co.getReceivablefee() != null) && (co.getReceivablefee().compareTo(this.exceedFeeDAO.getExceedFee().getExceedfee()) > 0)) {
				pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.GAO_JIA.getVediourl());
				pDAResponse.setErrorinfo(pDAResponse.getErrorinfo() + " " + CwbOrderPDAEnum.GAO_JIA.getError());
				pDAResponse.setPrintinfo(pDAResponse.getPrintinfo() + " " + CwbOrderPDAEnum.GAO_JIA.getError());
				pDAResponse.setShouldShock(true);
			}

			return pDAResponse;
		} catch (CwbException e) {
			CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
			this.exceptionCwbDAO.createExceptionCwbScan(cwb, e.getFlowordertye(), e.getMessage(), this.getSessionUser().getBranchid(), this.getSessionUser().getUserid(),
					cwbOrder == null ? 0 : cwbOrder.getCustomerid(), 0, 0, 0, "", scancwb);
			if (e.getError().getValue() == ExceptionCwbErrorTypeEnum.CHONG_FU_RU_KU.getValue()) {
				PDAResponse pDAResponse = new PDAResponse(CwbOrderPDAEnum.CHONG_FU_RU_KU.getCode(), cwb + CwbOrderPDAEnum.CHONG_FU_RU_KU.getError());
				pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.CHONG_FU_RU_KU.getVediourl());
				return pDAResponse;
			} else {
				throw e;
			}
		}

	}

	// 入库备注提交
	private PDAResponse forremark(String comment, long multicwbnum, String cwb, long requestbatchno, StringBuffer body, String statuscode, String errorinfo, String errorinfovediurl,
			HttpServletRequest request) {
		try {
			this.cwborderService.forremark(this.getSessionUser(), comment, multicwbnum, cwb);
		} catch (CwbException e) {
			CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
			this.exceptionCwbDAO.createExceptionCwbScan(cwb, e.getFlowordertye(), e.getMessage(), this.getSessionUser().getBranchid(), this.getSessionUser().getUserid(),
					cwbOrder == null ? 0 : cwbOrder.getCustomerid(), 0, 0, 0, "", cwb);
			errorinfo = CwbOrderPDAEnum.SYS_ERROR.getError();
			errorinfovediurl = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.SYS_ERROR.getVediourl();
		}
		PDAResponse PDAResponse = new StringBodyPdaResponse(statuscode, errorinfo, requestbatchno, errorinfovediurl, body);
		return PDAResponse;

	}

	// 出库扫描
	private PDAResponse outWarehous(String cwb, long driverid, long truckid, long branchid, long confirmflag, long reasonid, long requestbatchno, String baleno, StringBuffer body, String statuscode,
			String errorinfovediurl, HttpServletRequest request) {

		CwbOrder co;
		String scancwb = cwb;
		try {

			cwb = this.cwborderService.translateCwb(cwb);
			co = this.cwborderService.outWarehous(this.getSessionUser(), cwb, scancwb, driverid, truckid, branchid, requestbatchno, confirmflag == 1, "", baleno, reasonid, false, false);

			String errorinfo = this.getErrorInfoForIntoWarehouse(co);
			PDAResponse pDAResponse = new StringBodyPdaResponse(statuscode, errorinfo);
			pDAResponse.setScannum(co.getScannum());
			pDAResponse.setWavPath(errorinfovediurl);
			if ((co.getSendcarnum() > 1) || (co.getBackcarnum() > 1)) {
				pDAResponse.setShouldShock(true);
				pDAResponse.setErrorinfo(pDAResponse.getErrorinfo() + " " + CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getError());
				pDAResponse.setPrintinfo(pDAResponse.getPrintinfo() + " " + CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getError());
				pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getVediourl());
			}
			if (co.getDeliverid() != 0) {
				User user = this.userDAO.getUserByUserid(co.getDeliverid());
				if ((user.getUserwavfile() != null) && (user.getUserwavfile().length() > 0)) {
					pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.wavPath + user.getUserwavfile());
				}
				pDAResponse.setErrorinfo(pDAResponse.getErrorinfo() + " " + user.getRealname());
				pDAResponse.setPrintinfo(pDAResponse.getPrintinfo() + " " + user.getRealname());
			}
			if (co.getNextbranchid() != 0) {
				Branch branch = this.branchDAO.getBranchByBranchid(co.getNextbranchid());
				pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.wavPath + (branch.getBranchwavfile() == null ? "" : branch.getBranchwavfile()));
				pDAResponse.setErrorinfo(pDAResponse.getErrorinfo() + " " + branch.getBranchname());
				pDAResponse.setPrintinfo(branch.getBranchid() + "");
			}
			if ((co.getReceivablefee() != null) && (co.getReceivablefee().compareTo(this.exceedFeeDAO.getExceedFee().getExceedfee()) > 0)) {
				pDAResponse.setShouldShock(true);
				pDAResponse.setErrorinfo(pDAResponse.getErrorinfo() + " " + CwbOrderPDAEnum.GAO_JIA.getError());
				pDAResponse.setPrintinfo(pDAResponse.getPrintinfo() + " " + CwbOrderPDAEnum.GAO_JIA.getError());
				pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.GAO_JIA.getVediourl());
			}
			return pDAResponse;

		} catch (CwbException e) {
			co = this.cwbDAO.getCwbByCwb(cwb);
			this.exceptionCwbDAO.createExceptionCwbScan(cwb, e.getFlowordertye(), e.getMessage(), this.getSessionUser().getBranchid(), this.getSessionUser().getUserid(),
					co == null ? 0 : co.getCustomerid(), 0, 0, 0, "", scancwb);
			if (e.getError().getValue() == ExceptionCwbErrorTypeEnum.BU_SHI_ZHE_GE_MU_DI_DI.getValue()) {
				PopupConfig popupConfig = new PopupConfig();
				popupConfig.setText("强制出站?");
				Map map = new HashMap();
				map.putAll(request.getParameterMap());
				map.remove("confirmflag");
				popupConfig.setOkUrl(
						request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getRequestURI() + "?" + this.mapToURL(map, request) + "&confirmflag=1");
				PDAResponse pDAResponse = new PDAResponse(CwbOrderPDAEnum.BU_SHI_ZHE_GE_MU_DI_DI.getCode(), CwbOrderPDAEnum.BU_SHI_ZHE_GE_MU_DI_DI.getError());
				pDAResponse.setPopupConfig(popupConfig);
				pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.BU_SHI_ZHE_GE_MU_DI_DI.getVediourl());
				return pDAResponse;

			} else if (e.getError().getValue() == ExceptionCwbErrorTypeEnum.CHONG_FU_CHU_KU.getValue()) {

				Branch branch = this.branchDAO.getBranchByBranchid(co.getNextbranchid());
				PDAResponse pDAResponse = new PDAResponse(CwbOrderPDAEnum.CHONG_FU_CHU_KU.getCode(), cwb + CwbOrderPDAEnum.CHONG_FU_CHU_KU.getError() + " " + branch.getBranchname());
				pDAResponse.setPrintinfo(pDAResponse.getPrintinfo() + " " + CwbOrderPDAEnum.CHONG_FU_CHU_KU.getError());
				pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.CHONG_FU_CHU_KU.getVediourl());
				String popupOnException = this.systemInstallService.getParameter("prompt.outwarehouse.popupOnException", "false");
				if (popupOnException.equals("true")) {
					PopupConfig popupConfig = new PopupConfig();
					popupConfig.setText(cwb + CwbOrderPDAEnum.CHONG_FU_CHU_KU.getError());
					pDAResponse.setPopupConfig(popupConfig);
				}
				return pDAResponse;
			} else {
				throw e;
			}
		}

	}

	// 退货出站
	private PDAResponse outUntreadWarehous(String cwb, long driverid, long truckid, long branchid, long confirmflag, long requestbatchno, String baleno, StringBuffer body, String statuscode,
			String errorinfo, String errorinfovediurl, HttpServletRequest request) {
		CwbOrder co;
		String scancwb = cwb;
		try {
			PDAResponse pDAResponse = new StringBodyPdaResponse(statuscode, errorinfo);
			cwb = this.cwborderService.translateCwb(cwb);
			co = this.cwborderService.outUntreadWarehous(this.getSessionUser(), cwb, scancwb, driverid, truckid, branchid, requestbatchno, confirmflag == 1, "", "", false);// 为包号修改
			pDAResponse.setWavPath(errorinfovediurl);
			if ((co.getSendcarnum() > 1) || (co.getBackcarnum() > 1)) {
				pDAResponse.setShouldShock(true);
				pDAResponse.setErrorinfo(pDAResponse.getErrorinfo() + " " + CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getError());
				pDAResponse.setPrintinfo(pDAResponse.getPrintinfo() + " " + CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getError());
				pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getVediourl());
			}
			if (co.getDeliverid() != 0) {
				User user = this.userDAO.getUserByUserid(co.getDeliverid());
				if ((user.getUserwavfile() != null) && (user.getUserwavfile().length() > 0)) {
					pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.wavPath + user.getUserwavfile());
				} else {
					pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());
				}

				pDAResponse.setErrorinfo(cwb + " " + CwbOrderPDAEnum.OK.getError());
				pDAResponse.setPrintinfo(cwb + " " + CwbOrderPDAEnum.OK.getError());
			}
			if (co.getNextbranchid() != 0) {
				Branch branch = this.branchDAO.getBranchByBranchid(co.getNextbranchid());
				if ((branch.getBranchwavfile() != null) && (branch.getBranchwavfile().length() > 0)) {
					pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.wavPath + branch.getBranchwavfile());
				} else {
					pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());
				}
				pDAResponse.setErrorinfo(cwb + " " + CwbOrderPDAEnum.OK.getError());
				pDAResponse.setPrintinfo(cwb + " " + CwbOrderPDAEnum.OK.getError());
			}
			if ((co.getReceivablefee() != null) && (co.getReceivablefee().compareTo(this.exceedFeeDAO.getExceedFee().getExceedfee()) > 0)) {
				pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.GAO_JIA.getVediourl());
				pDAResponse.setErrorinfo(pDAResponse.getErrorinfo() + " " + CwbOrderPDAEnum.GAO_JIA.getError());
				pDAResponse.setPrintinfo(pDAResponse.getPrintinfo() + " " + CwbOrderPDAEnum.GAO_JIA.getError());
				pDAResponse.setShouldShock(true);
			}
			return pDAResponse;

		} catch (CwbException e) {
			co = this.cwbDAO.getCwbByCwb(cwb);
			this.exceptionCwbDAO.createExceptionCwbScan(cwb, e.getFlowordertye(), e.getMessage(), this.getSessionUser().getBranchid(), this.getSessionUser().getUserid(),
					co == null ? 0 : co.getCustomerid(), 0, 0, 0, "", scancwb);
			if (e.getError().getValue() == ExceptionCwbErrorTypeEnum.BU_SHI_ZHE_GE_MU_DI_DI.getValue()) {
				PopupConfig popupConfig = new PopupConfig();
				popupConfig.setText("强制出站?");
				Map map = new HashMap();
				map.putAll(request.getParameterMap());
				map.remove("confirmflag");
				popupConfig.setOkUrl(
						request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getRequestURI() + "?" + this.mapToURL(map, request) + "&confirmflag=1");
				PDAResponse pDAResponse = new PDAResponse(CwbOrderPDAEnum.BU_SHI_ZHE_GE_MU_DI_DI.getCode(), CwbOrderPDAEnum.BU_SHI_ZHE_GE_MU_DI_DI.getError());
				pDAResponse.setPopupConfig(popupConfig);
				pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.BU_SHI_ZHE_GE_MU_DI_DI.getVediourl());
				return pDAResponse;

			} else if (e.getError().getValue() == ExceptionCwbErrorTypeEnum.CHONG_FU_CHU_KU.getValue()) {
				Branch branch = this.branchDAO.getBranchByBranchid(co.getNextbranchid());
				PDAResponse pDAResponse = new PDAResponse(CwbOrderPDAEnum.CHONG_FU_CHU_KU.getCode(), cwb + CwbOrderPDAEnum.CHONG_FU_CHU_KU.getError() + " " + branch.getBranchname());
				pDAResponse.setPrintinfo(pDAResponse.getPrintinfo() + " " + CwbOrderPDAEnum.CHONG_FU_CHU_KU.getError());
				pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.CHONG_FU_CHU_KU.getVediourl());
				String popupOnException = this.systemInstallService.getParameter("prompt.outwarehouse.popupOnException", "false");
				if (popupOnException.equals("true")) {
					PopupConfig popupConfig = new PopupConfig();
					popupConfig.setText(cwb + CwbOrderPDAEnum.CHONG_FU_CHU_KU.getError());
					pDAResponse.setPopupConfig(popupConfig);
				}
				return pDAResponse;
			} else {
				throw e;
			}
		}
	}

	// 出库扫描确认
	private PDAResponse finishExport(long driverid, long operatetype, long requestbatchno, StringBuffer body, String statuscode, String errorinfo, String errorinfovediurl) {
		if (requestbatchno == 0) {
			OutWarehouseGroup og = this.outWarehouseGroupDAO.getAllOutWarehouseGroupByWhere(requestbatchno, 0, driverid, 0, OutWarehouseGroupEnum.SaoMiaoZhong.getValue(), operatetype, 0);
			if (og != null) {
				requestbatchno = og.getId();
			}
		}
		long successcwbnum = this.groupDetailDAO.getGroupDetailCount(this.getSessionUser().getUserid(), 0, 0);
		this.groupDetailDAO.saveGroupDetailUserid(this.getSessionUser().getUserid());
		body.append("<successcwbnum>").append(successcwbnum).append("</successcwbnum>");

		PDAResponse PDAResponse = new StringBodyPdaResponse(statuscode, errorinfo, requestbatchno, errorinfovediurl, body);
		return PDAResponse;
	}

	/**
	 * 分站到货扫描
	 *
	 * @param cwb
	 * @param baleno
	 * @param driverid
	 * @param requestbatchno
	 * @param body
	 * @param statuscode
	 * @param errorinfovediurl
	 * @param request
	 * @return
	 */
	private PDAResponse substationGoods(String cwb, String baleno, long driverid, long requestbatchno, HttpServletRequest request) {
		String scancwb = cwb;
		try {
			cwb = this.cwborderService.translateCwb(cwb);
			CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);
			co = this.cwborderService.substationGoods(this.getSessionUser(), cwb, scancwb, driverid, requestbatchno, "", baleno, false);

			String errorinfo = this.getErrorInfoForIntoWarehouse(co);
			PDAResponse pDAResponse = new PDAResponse(CwbOrderPDAEnum.OK.getCode(), errorinfo);
			pDAResponse.setScannum(co.getScannum());
			pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());

			if ((co.getSendcarnum() > 1) || (co.getBackcarnum() > 1)) {
				pDAResponse.setShouldShock(true);
				pDAResponse.setPrintinfo(pDAResponse.getPrintinfo() + " " + CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getError());
				pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getVediourl());
			}
			if (co.getDeliverid() != 0) {
				User user = this.userDAO.getUserByUserid(co.getDeliverid());
				if ((user.getUserwavfile() != null) && (user.getUserwavfile().length() > 0)) {
					pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.wavPath + user.getUserwavfile());
				}
				pDAResponse.setPrintinfo(pDAResponse.getPrintinfo() + " " + user.getRealname());
			}
			if (co.getNextbranchid() != 0) {
				Branch branch = this.branchDAO.getBranchByBranchid(co.getNextbranchid());
				if ((branch != null) && (branch.getBranchwavfile() != null) && (branch.getBranchwavfile().length() > 0)) {
					pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.wavPath + branch.getBranchwavfile());
				} else {
					pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());
				}
				pDAResponse.setPrintinfo(pDAResponse.getPrintinfo() + " " + branch.getBranchname());
			}
			if ((co.getReceivablefee() != null) && (co.getReceivablefee().compareTo(this.exceedFeeDAO.getExceedFee().getExceedfee()) > 0)) {
				pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.GAO_JIA.getVediourl());
				pDAResponse.setPrintinfo(pDAResponse.getPrintinfo() + " " + CwbOrderPDAEnum.GAO_JIA.getError());
				pDAResponse.setShouldShock(true);
			}

			return pDAResponse;
		} catch (CwbException e) {
			CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
			this.exceptionCwbDAO.createExceptionCwbScan(cwb, e.getFlowordertye(), e.getMessage(), this.getSessionUser().getBranchid(), this.getSessionUser().getUserid(),
					cwbOrder == null ? 0 : cwbOrder.getCustomerid(), 0, 0, 0, "", scancwb);
			if (e.getError().getValue() == ExceptionCwbErrorTypeEnum.CHONG_FU_RU_KU.getValue()) {
				PDAResponse pDAResponse = new PDAResponse(CwbOrderPDAEnum.CHONG_FU_RU_KU.getCode(), cwb + CwbOrderPDAEnum.CHONG_FU_RU_KU.getError());
				pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.CHONG_FU_RU_KU.getVediourl());
				pDAResponse.setPrintinfo(pDAResponse.getPrintinfo() + " " + CwbOrderPDAEnum.CHONG_FU_RU_KU.getError());
				return pDAResponse;
			} else {
				throw e;
			}
		}
	}

	// 领货扫描提交
	private PDAResponse ReceiveGoods(String cwb, long deliverid, long requestbatchno, StringBuffer body, String errorinfovediurl, HttpServletRequest request) {
		String scancwb = cwb;
		cwb = this.cwborderService.translateCwb(cwb);

		checkVipSmh(cwb, FlowOrderTypeEnum.FenZhanLingHuo);
		User deliveryUser = this.userDAO.getUserByUserid(deliverid);
		CwbOrder co = this.cwborderService.receiveGoods(this.getSessionUser(), deliveryUser, cwb, scancwb);
		PDAResponse pDAResponse = new PDAResponse(CwbOrderPDAEnum.OK.getCode(), co.getCwb() + "成功扫描");
		pDAResponse.setWavPath(errorinfovediurl);

		if ((co.getSendcarnum() > 1) || (co.getBackcarnum() > 1)) {
			pDAResponse.setShouldShock(true);
			pDAResponse.setErrorinfo(pDAResponse.getErrorinfo() + " " + CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getError());
			pDAResponse.setPrintinfo(pDAResponse.getPrintinfo() + " " + CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getError());
			pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getVediourl());
		}
		if ((co.getReceivablefee() != null) && (co.getReceivablefee().compareTo(this.exceedFeeDAO.getExceedFee().getExceedfee()) > 0)) {
			pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.GAO_JIA.getVediourl());
			pDAResponse.setErrorinfo(pDAResponse.getErrorinfo() + " " + CwbOrderPDAEnum.GAO_JIA.getError());
			pDAResponse.setPrintinfo(pDAResponse.getPrintinfo() + " " + CwbOrderPDAEnum.GAO_JIA.getError());
			pDAResponse.setShouldShock(true);
		}

		return pDAResponse;

	}

	// 退货出站扫描批次封包确认
	private PDAResponse BranchFinishBackExport(long driverid, long truckid, long operatetype, long requestbatchno, StringBuffer body, String statuscode, String errorinfo, String errorinfovediurl,
			HttpServletRequest request) {

		long branchid = this.branchDAO.getBranchBySiteType(BranchEnum.TuiHuo.getValue()).get(0).getBranchid();
		if (requestbatchno == 0) {
			OutWarehouseGroup og = this.outWarehouseGroupDAO.getAllOutWarehouseGroupByWhere(requestbatchno, 0, driverid, 0, OutWarehouseGroupEnum.SaoMiaoZhong.getValue(), operatetype, 0);
			if (og != null) {
				requestbatchno = og.getId();
			}
		}
		this.outWarehouseGroupDAO.saveByBranchidAndOperateType(driverid, truckid, OutWarehouseGroupEnum.SaoMiaoZhong.getValue(), branchid, operatetype);
		long successcwbnum = this.groupdetailDAO.getGroupDetailCount(this.getSessionUser().getBranchid(), 0, 0);

		this.groupDetailDAO.saveGroupDetailUserid(this.getSessionUser().getUserid());
		body.append("<successcwbnum>").append(successcwbnum).append("</successcwbnum>");

		PDAResponse PDAResponse = new StringBodyPdaResponse(statuscode, errorinfo, requestbatchno, errorinfovediurl, body);
		return PDAResponse;
	}

	// 理货扫描
	private PDAResponse scanCwbBranch(String cwb, long requestbatchno, StringBuffer body, String statuscode, String errorinfo, String errorinfovediurl, HttpServletRequest request) {
		CwbOrder co = this.cwborderService.intoWarehousForGetGoods(this.getSessionUser(), cwb, requestbatchno, 0);
		PDAResponse pDAResponse = new PDAResponse(CwbOrderPDAEnum.OK.getCode(), co.getCwb() + "成功扫描");
		pDAResponse.setWavPath(errorinfovediurl);

		if ((co.getSendcarnum() > 1) || (co.getBackcarnum() > 1)) {
			pDAResponse.setShouldShock(true);
			pDAResponse.setErrorinfo(pDAResponse.getErrorinfo() + " " + CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getError());
			pDAResponse.setPrintinfo(pDAResponse.getPrintinfo() + " " + CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getError());
			pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getVediourl());
		}
		if ((co.getReceivablefee() != null) && (co.getReceivablefee().compareTo(this.exceedFeeDAO.getExceedFee().getExceedfee()) > 0)) {
			pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.GAO_JIA.getVediourl());
			pDAResponse.setErrorinfo(pDAResponse.getErrorinfo() + " " + CwbOrderPDAEnum.GAO_JIA.getError());
			pDAResponse.setPrintinfo(pDAResponse.getPrintinfo() + " " + CwbOrderPDAEnum.GAO_JIA.getError());
			pDAResponse.setShouldShock(true);
		}

		return pDAResponse;
	}

	// 批量订单反馈扫描
	private PDAResponse deliverPodBatch(long deliverid, long podresultid, long backreasonid, long leavedreasonid, long paywayid, String deliverstateremark, String cwbs, long requestbatchno,
			StringBuffer body, String errorinfo, String errorinfovediurl) {
		this.logger.info("进入批量反馈处理方法中cwbs:" + cwbs + "--deliverid:" + deliverid + "--podresultid:" + podresultid + "--paywayid:" + paywayid);

		String[] cwbArray = cwbs.split(",");
		StringBuilder errorMsg = new StringBuilder();
		int backMes = 0;
		String statuscode = CwbOrderPDAEnum.OK.getCode();
		for (String cwb : cwbArray) {
			String scancwb = cwb;
			try {
				if (cwb.trim().length() == 0) {
					continue;
				}
				cwb = this.cwborderService.translateCwb(cwb);

				CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
				if (cwbOrder == null) {
					throw new CwbException(cwb, FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.YI_CHANG_DAN_HAO);
				}
				checkVipSmh(cwbOrder, FlowOrderTypeEnum.YiFanKui);
				if (cwbOrder.getCwbordertypeid() != CwbOrderTypeIdEnum.Peisong.getValue()&&cwbOrder.getCwbordertypeid()!=CwbOrderTypeIdEnum.Express.getValue()) {
					throw new CwbException(cwbOrder.getCwb(), FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.FEI_PEI_SONG_DING_DAN);
				}
				DeliveryState deliveryState = this.deliveryStateDAO.getActiveDeliveryStateByCwb(cwb);
				if (deliveryState != null) {
					if (deliveryState.getDeliverystate() != DeliveryStateEnum.WeiFanKui.getValue()) {
						throw new CwbException(cwb, FlowOrderTypeEnum.YiFanKui.getValue(), ExceptionCwbErrorTypeEnum.YI_FAN_KUI_BU_NENG_PI_LIANG_ZAI_FAN_KUI);
					}
				}
				BigDecimal cash = new BigDecimal(0);
				BigDecimal pos = new BigDecimal(0);
				BigDecimal checkfee = new BigDecimal(0);
				BigDecimal other = new BigDecimal(0);
				if (paywayid == 1) {
					// 现金支付
					cash = cwbOrder.getBusinessFee();
				} else if (paywayid == 2) {
					// pos支付
					pos = cwbOrder.getBusinessFee();
				} else if (paywayid == 3) {
					// 支票支付
					checkfee = cwbOrder.getBusinessFee();
				} else if (paywayid == 4) {
					// 其他支付方式
					other = cwbOrder.getBusinessFee();
				}
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("deliverid", deliveryState.getDeliveryid());
				parameters.put("podresultid", podresultid);
				parameters.put("backreasonid", backreasonid);
				parameters.put("leavedreasonid", leavedreasonid);
				parameters.put("receivedfeecash", cash);
				parameters.put("receivedfeepos", pos);
				parameters.put("receivedfeepos", pos);
				parameters.put("receivedfeeother", other);
				parameters.put("paybackedfee", cwbOrder.getPaybackfee());
				parameters.put("podremarkid", 0l);
				parameters.put("posremark", "");
				parameters.put("checkremark", "");
				parameters.put("deliverstateremark", deliverstateremark);
				parameters.put("owgid", requestbatchno);
				parameters.put("sessionbranchid", this.getSessionUser().getBranchid());
				parameters.put("sessionuserid", this.getSessionUser().getUserid());
				parameters.put("sign_typeid", SignTypeEnum.BenRenQianShou.getValue());
				parameters.put("sign_man", cwbOrder.getConsigneename());
				parameters.put("sign_time", DateTimeUtil.getNowTime());
				this.cwborderService.deliverStatePod(this.getSessionUser(), cwb, scancwb, parameters);
				backMes++;
			} catch (CwbException e) {
				CwbOrder cwbOrder = this.cwbDAO.getCwbByCwb(cwb);
				this.exceptionCwbDAO.createExceptionCwbScan(cwb, e.getFlowordertye(), e.getMessage(), this.getSessionUser().getBranchid(), this.getSessionUser().getUserid(),
						cwbOrder == null ? 0 : cwbOrder.getCustomerid(), 0, 0, 0, "", scancwb);

				statuscode = CwbOrderPDAEnum.SYS_ERROR.getCode();
				errorMsg = errorMsg.append(cwb).append(" ").append("签收失败");
				
				this.logger.error("归班反馈异常" + cwb, e);
				BatchDeliverBody batchDeliverBody = new BatchDeliverBody();
				batchDeliverBody.setFailcwbinfo(errorMsg.toString());
				batchDeliverBody.setSuccesscwbnum(Integer.toString(backMes));

				BatchDeliverBodyPdaResponse PDAResponse = new BatchDeliverBodyPdaResponse(statuscode, errorMsg.toString());
				PDAResponse.setBody(batchDeliverBody);
				return PDAResponse;
			} catch (Exception e) {
				
				statuscode = CwbOrderPDAEnum.SYS_ERROR.getCode();
				errorMsg = errorMsg.append(cwb).append(" ").append("系统异常");
				this.logger.error("归班反馈异常" + cwb, e);
				BatchDeliverBody batchDeliverBody = new BatchDeliverBody();
				batchDeliverBody.setFailcwbinfo(errorMsg.toString());
				batchDeliverBody.setSuccesscwbnum(Integer.toString(backMes));

				BatchDeliverBodyPdaResponse PDAResponse = new BatchDeliverBodyPdaResponse(statuscode, errorMsg.toString());
				PDAResponse.setBody(batchDeliverBody);
				return PDAResponse;
			}
		}

		this.logger.info("进入批量反馈,结束后得到跳出处理cwbs:" + cwbArray + "--deliverid:" + deliverid + "--podresultid:" + podresultid + "--paywayid:" + paywayid);
		backMes = cwbArray.length;
		BatchDeliverBody batchDeliverBody = new BatchDeliverBody();
		batchDeliverBody.setFailcwbinfo(errorinfo);
		batchDeliverBody.setSuccesscwbnum(Integer.toString(backMes));

		BatchDeliverBodyPdaResponse PDAResponse = new BatchDeliverBodyPdaResponse(statuscode, errorinfo);
		PDAResponse.setBody(batchDeliverBody);
		return PDAResponse;
	}

	// 退供货商出库
	private PDAResponse backtocustom(String cwb, long requestbatchno, HttpServletRequest request) {
		String scancwb = cwb;
		cwb = this.cwborderService.translateCwb(cwb);
		CwbOrder co = this.cwborderService.backtocustom(this.getSessionUser(), cwb, scancwb, requestbatchno, "", false);// 为包号修改
		String customername = this.customerDAO.getCustomerById(co.getCustomerid()).getCustomername();
		PDAResponse PDAResponse = new PDAResponse(CwbOrderPDAEnum.OK.getCode(), customername);
		PDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());
		return PDAResponse;
	}

	// 供货商拒收退货
	private PDAResponse customrefuseback(String cwb, long requestbatchno, StringBuffer body, String statuscode, String errorinfo, String errorinfovediurl, HttpServletRequest request) {
		String scancwb = cwb;
		cwb = this.cwborderService.translateCwb(cwb);
		this.cwborderService.customrefuseback(this.getSessionUser(), cwb, scancwb, requestbatchno, "");
		PDAResponse PDAResponse = new StringBodyPdaResponse(CwbOrderPDAEnum.OK.getCode(), CwbOrderPDAEnum.OK.getError());
		PDAResponse.setWavPath(errorinfovediurl);
		return PDAResponse;

	}

	// 领货扫描批量提交
	private PDAResponse ReceiveGoodsBatch(String cwbs, long deliverid, long requestbatchno, StringBuffer body, String statuscode, String errorinfo, String errorinfovediurl,
			HttpServletRequest request) {
		String statuscodeSTR = "";
		String errorinfoSTR = "";
		for (String cwb : cwbs.split(",")) {
			try {
				if (cwb.trim().length() == 0) {
					continue;
				}
				String scancwb = cwb;
				cwb = this.cwborderService.translateCwb(cwb);

				checkVipSmh(cwb, FlowOrderTypeEnum.FenZhanLingHuo);
				this.cwborderService.receiveGoods(this.getSessionUser(), this.userDAO.getUserByUserid(deliverid), cwb, scancwb);
				statuscodeSTR += cwb + "_" + statuscode + ",";
				errorinfoSTR += cwb + "_成功,";
			} catch (Exception e) {
				statuscodeSTR += cwb + "_" + CwbOrderPDAEnum.SYS_ERROR.getCode() + ",";
				errorinfovediurl = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.SYS_ERROR.getVediourl();
				errorinfoSTR += cwb + "_" + e.getMessage() + ",";
			}

		}
		PDAResponse PDAResponse = new StringBodyPdaResponse(statuscodeSTR, errorinfoSTR, requestbatchno, errorinfovediurl, body);
		return PDAResponse;
	}

	// ==============================验证==============================================
	private CwbOrderPDAEnum verficationCwb(String cwb) {
		String reg = "^[a-zA-Z0-9-_*]+$";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(cwb);
		CwbOrder co = null;
		if ((cwb.length() != 0) && matcher.matches()) {
			co = this.cwbDAO.getCwbByCwb(cwb);
		}

		if (!matcher.matches()) {
			return CwbOrderPDAEnum.YI_CHANG_DAN_HAO;
		} else if (co == null) {
			return CwbOrderPDAEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI;
		}
		return CwbOrderPDAEnum.OK;
	}

	private BalePDAEnum verficationBaleNO(String baleNO) {
		String reg = "^[a-zA-Z0-9-_*]+$";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(baleNO);
		Bale co = null;
		if ((baleNO.length() != 0) && matcher.matches()) {
			co = this.baleDao.getBaleOnway(baleNO);
		}

		if (!matcher.matches()) {
			return BalePDAEnum.YI_CHANG_BAO_HAO;
		} else if (co == null) {
			return BalePDAEnum.CHA_XUN_YI_CHANG_BAO_HAO_BU_CUN_ZAI;
		}
		return BalePDAEnum.OK;
	}

	private StringBuffer mapToURL(Map<String, Object> paramMap, HttpServletRequest request) {
		StringBuffer sb = new StringBuffer();
		List<String> keySet = new ArrayList<String>(paramMap.keySet());
		Collections.sort(keySet);
		for (int i = 0; i < paramMap.size(); i++) {
			sb.append("&").append(keySet.get(i)).append("=").append(request.getParameter(keySet.get(i)));
		}
		sb.delete(0, 1);
		return sb;
	}

	@ExceptionHandler(CwbException.class)
	public PDAResponse handleCwbException(CwbException ex, HttpServletRequest request) {
		this.logger.error("系统异常", ex);
		CwbOrder co = this.cwbDAO.getCwbByCwb(ex.getCwb());
		this.exceptionCwbDAO.createExceptionCwbScan(ex.getCwb(), ex.getFlowordertye(), ex.getMessage(), this.getSessionUser().getBranchid(), this.getSessionUser().getUserid(),
				co == null ? 0 : co.getCustomerid(), 0, 0, 0, "", ex.getCwb());
		PDAResponse explinkResponse = new StringBodyPdaResponse(ex.getError().getValue() + "", ex.getMessage(), "");
		if (explinkResponse.getStatuscode().equals(CwbOrderPDAEnum.OK.getCode())) {
			explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());
		} else {
			explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.SYS_ERROR.getVediourl());
		}
		return explinkResponse;
	}

	@ExceptionHandler(Exception.class)
	public PDAResponse handleException(Exception ex, HttpServletRequest request) {
		this.logger.error("系统异常", ex);

		PDAResponse explinkResponse = new StringBodyPdaResponse("000001", ex.getMessage(), "");
		if (explinkResponse.getStatuscode().equals(CwbOrderPDAEnum.OK.getCode())) {
			explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());
		} else {
			explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.SYS_ERROR.getVediourl());
		}
		return explinkResponse;
	}

	@ExceptionHandler(ExplinkException.class)
	public PDAResponse explinkException(Exception ex, HttpServletRequest request) {
		this.logger.error("系统异常", ex);

		PDAResponse explinkResponse = new StringBodyPdaResponse("000001", ex.getMessage(), "");
		if (explinkResponse.getStatuscode().equals(CwbOrderPDAEnum.OK.getCode())) {
			explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());
		} else {
			explinkResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.SYS_ERROR.getVediourl());
		}
		return explinkResponse;
	}

	@RequestMapping("/exportExcle")
	public void exportExcle(HttpServletResponse response, @RequestParam(value = "cwb", required = false, defaultValue = "") String cwb,
			@RequestParam(value = "userid", required = false, defaultValue = "0") long userid,
			@RequestParam(value = "flowOrderTypeEnumid", required = false, defaultValue = "0") long flowOrderTypeEnumid,
			@RequestParam(value = "beginemaildate", required = false, defaultValue = "") String beginemaildate,
			@RequestParam(value = "endemaildate", required = false, defaultValue = "") String endemaildate, @RequestParam(value = "scopeValue", required = false, defaultValue = "-1") long scope,
			@RequestParam(value = "branchid", required = false, defaultValue = "0") String[] branchid) {
		String[] cloumnName1 = new String[8]; // 导出的列名
		String[] cloumnName2 = new String[8]; // 导出的英文列名

		String branchids = this.cwborderService.getToString(branchid);
		this.setExcelstyle(cloumnName1, cloumnName2);
		final String[] cloumnName3 = cloumnName1;
		final String[] cloumnName4 = cloumnName2;
		String sheetName = "扫描监控"; // sheet的名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String fileName = "Order_" + df.format(new Date()) + ".xlsx"; // 文件名
		try {
			// 查询出数据

			final List<Branch> branchs = this.branchDAO.getAllEffectBranches();
			final List<User> users = this.userDAO.getAllUser();
			final List<ExceptionCwb> eclist = this.exceptionCwbDAO.getAllECByExcel(1, cwb, flowOrderTypeEnumid, "", branchids, userid, 0, beginemaildate, endemaildate, scope);
			final Map<Long, String> branchMap = this.getBranchMap(branchs, eclist);
			final Map<Long, String> flowOrderTypeEnumMap = this.getFlowOrderTypeEnumMap(eclist);
			final Map<String, String> errortypeMap = this.getExceptionCwbErrorTypeEnum(eclist);
			final Map<Long, String> userMap = this.getUserMap(users, eclist);
			ExcelUtils excelUtil = new ExcelUtils() { // 生成工具类实例，并实现填充数据的抽象方法
				@Override
				public void fillData(final Sheet sheet, final CellStyle style) {
					style.setAlignment(CellStyle.ALIGN_CENTER);
					for (int k = 0; k < eclist.size(); k++) {
						Row row = sheet.createRow(k + 1);
						row.setHeightInPoints(15);
						for (int i = 0; i < cloumnName4.length; i++) {
							sheet.setColumnWidth(i, 15 * 256);
							Cell cell = row.createCell((short) i);
							cell.setCellStyle(style);
							Object a = null;
							// 给导出excel赋值
							a = this.setExcelObject(cloumnName4, a, i, k);

							cell.setCellValue(a == null ? "" : a.toString());
						}
					}
				}

				private Object setExcelObject(String[] cloumnName, Object a, int i, int k) {
					if (cloumnName[i].equals("cwb")) {
						a = eclist.get(k).getCwb();
					}
					if (cloumnName[i].equals("branchname")) {
						a = branchMap.get(eclist.get(k).getBranchid());
					}
					if (cloumnName[i].equals("flowOrderTypeEnumid")) {
						a = flowOrderTypeEnumMap.get(eclist.get(k).getScantype());
					}
					if (cloumnName[i].equals("errortype")) {
						a = errortypeMap.get(eclist.get(k).getErrortype());
					}
					if (cloumnName[i].equals("createtime")) {
						a = eclist.get(k).getCreatetime();
					}
					if (cloumnName[i].equals("username")) {
						a = userMap.get(eclist.get(k).getUserid());
					}
					if (cloumnName[i].equals("remark")) {
						a = eclist.get(k).getRemark();
					}
					if (cloumnName[i].equals("scancwb")) {
						a = eclist.get(k).getScancwb();
					}
					return a;
				}
			};
			excelUtil.excel(response, cloumnName3, sheetName, fileName);

		} catch (Exception e) {
			logger.error("", e);
		}
	}

	private Map<Long, String> getBranchMap(final List<Branch> branchs, final List<ExceptionCwb> eclist) {
		Map<Long, String> map = new HashMap<Long, String>();
		for (ExceptionCwb ex : eclist) {
			for (Branch b : branchs) {
				if (ex.getBranchid() == b.getBranchid()) {
					map.put(b.getBranchid(), b.getBranchname());
				}
			}
		}
		return map;
	}

	private Map<Long, String> getFlowOrderTypeEnumMap(final List<ExceptionCwb> eclist) {
		Map<Long, String> map = new HashMap<Long, String>();
		for (ExceptionCwb ex : eclist) {
			for (FlowOrderTypeEnum f : FlowOrderTypeEnum.values()) {
				if (ex.getScantype() == f.getValue()) {
					map.put(ex.getScantype(), f.getText());
				}
			}
		}
		return map;
	}

	private Map<String, String> getExceptionCwbErrorTypeEnum(final List<ExceptionCwb> eclist) {
		Map<String, String> map = new HashMap<String, String>();
		for (ExceptionCwb ex : eclist) {
			String errortype = ex.getErrortype();
			if ((errortype.length() > 0) && errortype.matches("^[0-9]*$")) {
				for (ExceptionCwbErrorTypeEnum er : ExceptionCwbErrorTypeEnum.values()) {
					if (Long.parseLong(ex.getErrortype()) == er.getValue()) {
						map.put(ex.getErrortype(), er.getText());
					}
				}
			} else {
				map.put(ex.getErrortype(), ex.getErrortype());
			}
		}
		return map;
	}

	private Map<Long, String> getUserMap(final List<User> users, final List<ExceptionCwb> eclist) {
		Map<Long, String> map = new HashMap<Long, String>();
		for (ExceptionCwb ex : eclist) {
			for (User u : users) {
				if (ex.getUserid() == u.getUserid()) {
					map.put(ex.getUserid(), u.getRealname());
				}
			}
		}
		return map;
	}

	public void setExcelstyle(String[] cloumnName1, String[] cloumnName2) {
		cloumnName1[0] = "订单号";
		cloumnName2[0] = "cwb";
		cloumnName1[1] = "扫描号";
		cloumnName2[1] = "scancwb";
		cloumnName1[2] = "扫描类型";
		cloumnName2[2] = "flowOrderTypeEnumid";
		cloumnName1[3] = "错误类型";
		cloumnName2[3] = "errortype";
		cloumnName1[4] = "操作时间";
		cloumnName2[4] = "createtime";
		cloumnName1[5] = "操作人";
		cloumnName2[5] = "username";
		cloumnName1[6] = "备注";
		cloumnName2[6] = "remark";
		cloumnName1[7] = "操作机构";
		cloumnName2[7] = "branchname";

	}

	/**
	 * 入库扫描 （APP）
	 *
	 * @param cwb
	 * @param customerid
	 * @param driverid
	 * @param requestbatchno
	 * @param baleno
	 * @param request
	 * @param comments
	 * @return
	 */
	private PDAResponse intoWarehousAPP(String cwb, String customerid, long driverid, long requestbatchno, String baleno, HttpServletRequest request, String comments) {
		CwbOrder co;
		String scancwb = cwb;
		try {
			cwb = this.cwborderService.translateCwb(cwb);
			co = this.cwbDAO.getCwbByCwb(cwb);

			Branch userbranch = this.branchDAO.getBranchById(this.getSessionUser().getBranchid());
			if ((userbranch.getBranchid() != 0) && (userbranch.getSitetype() == BranchEnum.ZhanDian.getValue())) {
				co = this.cwborderService.substationGoods(this.getSessionUser(), cwb, scancwb, driverid, requestbatchno, "", baleno, false);
			} else {
				co = this.cwborderService.intoWarehous(this.getSessionUser(), cwb, scancwb, Long.parseLong(customerid), driverid, requestbatchno, comments, baleno, false);
			}

			// String errorinfo = getErrorInfoForIntoWarehouse(co);
			String errorinfo = this.getErrorInfoForIntoWarehouseAPP(co);
			PDAResponse pDAResponse = new PDAResponse(CwbOrderPDAEnum.OK.getCode(), errorinfo);
			pDAResponse.setScannum(co.getScannum());
			pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());
			// 中浩乐宠@上海江苏 2016-6-27
			pDAResponse.setBranchid("" + co.getDeliverybranchid());
			Branch tempBr = branchDAO.getBranchById(co.getDeliverybranchid());
			pDAResponse.setDeliverybranchname(tempBr == null ? null : tempBr.getBranchname());

			String RUKUPCandPDAaboutYJDPWAV = this.systemInstallDAO.getSystemInstall("RUKUPCandPDAaboutYJDPWAV") == null ? "yes"
					: this.systemInstallDAO.getSystemInstall("RUKUPCandPDAaboutYJDPWAV").getValue();

			if (RUKUPCandPDAaboutYJDPWAV.equals("yes") && ((co.getSendcarnum() > 1) || (co.getBackcarnum() > 1))) {
				pDAResponse.setShouldShock(true);
				pDAResponse.setPrintinfo(pDAResponse.getPrintinfo() + " " + CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getError());
				pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getVediourl());
			}
			if (co.getDeliverid() != 0) {
				User user = this.userDAO.getUserByUserid(co.getDeliverid());
				if ((user.getUserwavfile() != null) && (user.getUserwavfile().length() > 0)) {
					pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.wavPath + user.getUserwavfile());
				}
				pDAResponse.setPrintinfo(pDAResponse.getPrintinfo() + " " + user.getRealname());
			}
			if (co.getNextbranchid() != 0) {
				Branch branch = this.branchDAO.getBranchByBranchid(co.getNextbranchid());
				if ((branch != null) && (branch.getBranchwavfile() != null) && (branch.getBranchwavfile().length() > 0)) {
					pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.wavPath + branch.getBranchwavfile());
				} else {
					pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl());
				}
				pDAResponse.setPrintinfo(pDAResponse.getPrintinfo() + " 站点：" + branch.getBranchname() + " 编号：" + branch.getBranchcode());
				// pDAResponse.setErrorinfo(pDAResponse.getErrorinfo().substring(0,
				// errorinfo.indexOf("#")) + "##站点：" + branch.getBranchname() +
				// "#编号：" + branch.getBranchcode() + "#");
			}
			if ((co.getReceivablefee() != null) && (co.getReceivablefee().compareTo(this.exceedFeeDAO.getExceedFee().getExceedfee()) > 0)) {
				pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.GAO_JIA.getVediourl());
				pDAResponse.setPrintinfo(pDAResponse.getPrintinfo() + " " + CwbOrderPDAEnum.GAO_JIA.getError());
				pDAResponse.setShouldShock(true);
			}
			logger.info("返回" + pDAResponse.getErrorinfo());
			return pDAResponse;
		} catch (CwbException e) {
			co = this.cwbDAO.getCwbByCwb(cwb);
			this.exceptionCwbDAO.createExceptionCwbScan(cwb, e.getFlowordertye(), e.getMessage(), this.getSessionUser().getBranchid(), this.getSessionUser().getUserid(),
					co == null ? 0 : co.getCustomerid(), 0, 0, 0, "", scancwb);
			if (e.getError().getValue() == ExceptionCwbErrorTypeEnum.CHONG_FU_RU_KU.getValue()) {
				Branch branch = this.branchDAO.getBranchByBranchid(co.getNextbranchid());
				PDAResponse pDAResponse = new PDAResponse(CwbOrderPDAEnum.CHONG_FU_RU_KU.getCode(),
						cwb + CwbOrderPDAEnum.CHONG_FU_RU_KU.getError() + " " + StringUtil.nullConvertToEmptyString(branch.getBranchname()));
				pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.CHONG_FU_RU_KU.getVediourl());
				pDAResponse.setPrintinfo(pDAResponse.getPrintinfo() + " " + CwbOrderPDAEnum.CHONG_FU_RU_KU.getError());
				return pDAResponse;
			} else {
				throw e;
			}
		}
	}

	/* 新增站点编号字段 */
	private String getErrorInfoForIntoWarehouseAPP(CwbOrder co) {
		// APP入库出库声音修改 vic.liang@pjbest.com 2016-07-19
		String successPattern = this.systemInstallService.getParameter("prompt.intoWarehouse.successPattern", "<co.cwb><fenge><yipiaoduojian><fenge><branch.branchname><branch.branchcode><gaojia>");
		ST errorInfoST = new ST(successPattern);
		errorInfoST.add("co", co);
		errorInfoST.add("fenge", " ");
		if ((co.getSendcarnum() > 1) || (co.getBackcarnum() > 1)) { // 发货数量 ||
																	// 取货数量;
			errorInfoST.add("yipiaoduojian", CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getError());
		}
		/*
		 * if(co.getDeliverid()!=0){ User user =
		 * userDAO.getUserByUserid(co.getDeliverid());
		 * errorInfoST.add("deliver", user); }
		 */
		if (co.getNextbranchid() != 0) { // 下一站目的机构;
			Branch branch = this.branchDAO.getBranchByBranchid(co.getNextbranchid());
			errorInfoST.add("branch", branch);
		}
		// 待收货款 应收金额
		if ((co.getReceivablefee() != null) && (co.getReceivablefee().compareTo(this.exceedFeeDAO.getExceedFee().getExceedfee()) > 0)) {
			errorInfoST.add("gaojia", CwbOrderPDAEnum.GAO_JIA.getError());
		}

		logger.info("入库出库 返回errorinfo: " + errorInfoST.render());
		return errorInfoST.render();
	}

	/**
	 * 出库扫描
	 */
	private PDAResponse outWarehousAPP(String cwb, long driverid, long truckid, long branchid, long confirmflag, long reasonid, long requestbatchno, String baleno, StringBuffer body,
			String statuscode, String errorinfovediurl, HttpServletRequest request) {

		CwbOrder co;
		String scancwb = cwb;
		try {

			cwb = this.cwborderService.translateCwb(cwb);
			co = this.cwborderService.outWarehous(this.getSessionUser(), cwb, scancwb, driverid, truckid, branchid, requestbatchno, confirmflag == 1, "", baleno, reasonid, false, false);

			// String errorinfo = getErrorInfoForIntoWarehouse(co);
			String errorinfo = this.getErrorInfoForIntoWarehouseAPP(co);
			PDAResponse pDAResponse = new StringBodyPdaResponse(statuscode, errorinfo);
			pDAResponse.setScannum(co.getScannum());
			pDAResponse.setWavPath(errorinfovediurl);
			if ((co.getSendcarnum() > 1) || (co.getBackcarnum() > 1)) {
				pDAResponse.setShouldShock(true);
				// pDAResponse.setErrorinfo(pDAResponse.getErrorinfo() + " " +
				// CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getError());
				pDAResponse.setPrintinfo(pDAResponse.getPrintinfo() + " " + CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getError());
				pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.YI_PIAO_DUO_JIAN.getVediourl());
			}
			if (co.getDeliverid() != 0) {
				User user = this.userDAO.getUserByUserid(co.getDeliverid());
				if ((user.getUserwavfile() != null) && (user.getUserwavfile().length() > 0)) {
					pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.wavPath + user.getUserwavfile());
				}
				// pDAResponse.setErrorinfo(pDAResponse.getErrorinfo() + " " +
				// user.getRealname());
				pDAResponse.setPrintinfo(pDAResponse.getPrintinfo() + " " + user.getRealname());
			}
			if (co.getNextbranchid() != 0) {
				Branch branch = this.branchDAO.getBranchByBranchid(co.getNextbranchid());
				pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.wavPath + (branch.getBranchwavfile() == null ? "" : branch.getBranchwavfile()));
				// pDAResponse.setErrorinfo(pDAResponse.getErrorinfo().substring(0,
				// errorinfo.indexOf("#")) + "##站点：" + branch.getBranchname() +
				// "#编号：" + branch.getBranchcode() + "#");
				// pDAResponse.setPrintinfo(branch.getBranchid()+"");
				pDAResponse.setPrintinfo(branch.getBranchname());
			}
			if ((co.getReceivablefee() != null) && (co.getReceivablefee().compareTo(this.exceedFeeDAO.getExceedFee().getExceedfee()) > 0)) {
				pDAResponse.setShouldShock(true);
				// pDAResponse.setErrorinfo(pDAResponse.getErrorinfo() + " " +
				// CwbOrderPDAEnum.GAO_JIA.getError());
				pDAResponse.setPrintinfo(pDAResponse.getPrintinfo() + " " + CwbOrderPDAEnum.GAO_JIA.getError());
				pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.GAO_JIA.getVediourl());
			}
			logger.info("返回" + pDAResponse.getErrorinfo());
			return pDAResponse;

		} catch (CwbException e) {
			co = this.cwbDAO.getCwbByCwb(cwb);
			this.exceptionCwbDAO.createExceptionCwbScan(cwb, e.getFlowordertye(), e.getMessage(), this.getSessionUser().getBranchid(), this.getSessionUser().getUserid(),
					co == null ? 0 : co.getCustomerid(), 0, 0, 0, "", scancwb);
			if (e.getError().getValue() == ExceptionCwbErrorTypeEnum.BU_SHI_ZHE_GE_MU_DI_DI.getValue()) {
				PopupConfig popupConfig = new PopupConfig();
				popupConfig.setText("强制出站?");
				Map map = new HashMap();
				map.putAll(request.getParameterMap());
				map.remove("confirmflag");
				popupConfig.setOkUrl(
						request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getRequestURI() + "?" + this.mapToURL(map, request) + "&confirmflag=1");
				PDAResponse pDAResponse = new PDAResponse(CwbOrderPDAEnum.BU_SHI_ZHE_GE_MU_DI_DI.getCode(), CwbOrderPDAEnum.BU_SHI_ZHE_GE_MU_DI_DI.getError());
				pDAResponse.setPopupConfig(popupConfig);
				pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.BU_SHI_ZHE_GE_MU_DI_DI.getVediourl());
				return pDAResponse;

			} else if (e.getError().getValue() == ExceptionCwbErrorTypeEnum.CHONG_FU_CHU_KU.getValue()) {

				Branch branch = this.branchDAO.getBranchByBranchid(co.getNextbranchid());
				PDAResponse pDAResponse = new PDAResponse(CwbOrderPDAEnum.CHONG_FU_CHU_KU.getCode(), cwb + CwbOrderPDAEnum.CHONG_FU_CHU_KU.getError() + " " + branch.getBranchname());
				pDAResponse.setPrintinfo(pDAResponse.getPrintinfo() + " " + CwbOrderPDAEnum.CHONG_FU_CHU_KU.getError());
				pDAResponse.setWavPath(request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.CHONG_FU_CHU_KU.getVediourl());
				String popupOnException = this.systemInstallService.getParameter("prompt.outwarehouse.popupOnException", "false");
				if (popupOnException.equals("true")) {
					PopupConfig popupConfig = new PopupConfig();
					popupConfig.setText(cwb + CwbOrderPDAEnum.CHONG_FU_CHU_KU.getError());
					pDAResponse.setPopupConfig(popupConfig);
				}
				return pDAResponse;
			} else {
				throw e;
			}
		}

	}

	/**
	 * 订单状态查询（查询单号提交） (APP)
	 *
	 * @param cwb
	 * @param requestbatchno
	 * @param body
	 * @param statuscode
	 * @param errorinfo
	 * @param errorinfovediurl
	 * @param request
	 * @return
	 */
	private PDAResponse cwbtrack_new(String cwb, long requestbatchno, StringBuffer body, String statuscode, String errorinfo, String errorinfovediurl, HttpServletRequest request) {
		String scancwb = cwb;
		cwb = this.cwborderService.translateCwb(cwb);
		CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);

		if (co == null) {
			throw new CwbException(cwb, ExceptionCwbErrorTypeEnum.CHA_XUN_YI_CHANG_DAN_HAO_BU_CUN_ZAI);
		}
		// 就否是海外环球 no;
		String isHWHQPDASelect = this.systemInstallDAO.getSystemInstallByName("isHWHQPDASelect") == null ? "no" : this.systemInstallDAO.getSystemInstallByName("isHWHQPDASelect").getValue();
		// 订单流向表; order_flow
		OrderFlow of = this.orderFlowDAO.getOrderFlowByCwbAndState(cwb, 1);
		String trackevent = "";
		String branchname = "";
		String realname = "";
		String usermobile = ""; // 用户手机号
		String backreason = "";
		for (FlowOrderTypeEnum fot : FlowOrderTypeEnum.values()) {
			if (of.getFlowordertype() == fot.getValue()) {
				trackevent = fot.getText(); // 操作步骤信息
			}
		}
		// 站点信息 (根据订单号查询站点信息并获得站点的名字;)
		branchname = this.branchDAO.getBranchByBranchid(of.getBranchid()).getBranchname();
		backreason = co.getBackreason();
		// 根据登录的id号查找 操作人的名字;
		User user = this.userDAO.getUserByUserid(of.getUserid());
		realname = user.getRealname();
		usermobile = user.getUsermobile();
		StringBuilder cwbTrackBody = new StringBuilder();

		// 操作详情信息;
		cwbTrackBody.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(of.getCredate()));
		cwbTrackBody.append("  ");
		cwbTrackBody.append(branchname);
		cwbTrackBody.append("  ");
		cwbTrackBody.append(trackevent + "（" + realname + "）"); // 该句说明了谁进行了怎样的操作;
		cwbTrackBody.append("  ");
		cwbTrackBody.append(usermobile);
		cwbTrackBody.append("  ");
		cwbTrackBody.append("[" + backreason + "]");
		cwbTrackBody.append("  ");

		errorinfo = errorinfo + " " + co.getCwb() + "（订单号）";
		if (this.transCwbDao.getCwbByTransCwb(scancwb) != null) { // 查找
																	// 订单号对应的运单号;
			errorinfo = errorinfo + " /" + scancwb + "（运单号）";
		}

		CwbTrackBodyPdaResponse PDAResponse = new CwbTrackBodyPdaResponse(statuscode, errorinfo, requestbatchno, errorinfovediurl, new CwbTrackBody());
		List<String> strings = new ArrayList<String>();
		if (isHWHQPDASelect.equals("xhm")) {
			strings.add(cwbTrackBody.toString());
		} else {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// isypdjusetranscwb; 标识是否一票多件
			long isypdjusetranscwb = this.customerDAO.getCustomerById(co.getCustomerid()).getCustomerid() == 0 ? 0 : this.customerDAO.getCustomerById(co.getCustomerid()).getIsypdjusetranscwb();
			if (((co.getSendcarnum() > 1) || (co.getBackcarnum() > 1)) && ((co.getTranscwb().split(",").length > 1) || (co.getTranscwb().split(":").length > 1)) && (isypdjusetranscwb == 1)) {
				List<TranscwbOrderFlow> datalist = this.transcwbOrderFlowDAO.getTranscwbOrderFlowByScanCwb(scancwb, this.cwborderService.translateCwb(scancwb));

				for (TranscwbOrderFlow transcwborderFlowAll : datalist) {
					StringBuilder stringBuilder = new StringBuilder();
					long sitetype = this.branchDAO.getBranchByBranchid(transcwborderFlowAll.getBranchid()).getBranchid() == 0 ? 0
							: this.branchDAO.getBranchByBranchid(transcwborderFlowAll.getBranchid()).getSitetype();
					if (isHWHQPDASelect.equals("yes")) {
						if ((sitetype == BranchEnum.KuFang.getValue()) && ((transcwborderFlowAll.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue())
								|| (transcwborderFlowAll.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()))) {
							long deliverornextbranchid = transcwborderFlowAll.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue()
									? JSONObject.fromObject(JSONObject.fromObject(transcwborderFlowAll.getFloworderdetail()).getString("cwbOrder")).getLong("deliverybranchid")
									: JSONObject.fromObject(JSONObject.fromObject(transcwborderFlowAll.getFloworderdetail()).getString("cwbOrder")).getLong("nextbranchid");
							String bname = this.branchDAO.getBranchByBranchid(deliverornextbranchid).getBranchid() == 0 ? ""
									: this.branchDAO.getBranchByBranchid(deliverornextbranchid).getBranchname();

							stringBuilder.append(simpleDateFormat.format(transcwborderFlowAll.getCredate()));
							stringBuilder.append("  ");
							stringBuilder.append(this.userDAO.getUserByUserid(transcwborderFlowAll.getUserid()).getRealname() + "（" + usermobile + "）");
							stringBuilder.append(" ");
							stringBuilder.append(transcwborderFlowAll.getFlowordertypeText());
							stringBuilder.append("\n");
							stringBuilder.append(bname);
							stringBuilder.append(" ");
							// stringBuilder.append(usermobile);
							// stringBuilder.append(" ");
							stringBuilder.append("[" + this.getBackReason(transcwborderFlowAll) + "]");
							strings.add(stringBuilder.toString());
							stringBuilder.append(" ");
						}
					} else {
						long deliverornextbranchid = ((transcwborderFlowAll.getFlowordertype() == FlowOrderTypeEnum.DaoRuShuJu.getValue())
								|| (transcwborderFlowAll.getFlowordertype() == FlowOrderTypeEnum.UpdateDeliveryBranch.getValue())
								|| (transcwborderFlowAll.getFlowordertype() == FlowOrderTypeEnum.ShouGongXiuGai.getValue()))
										? JSONObject.fromObject(JSONObject.fromObject(transcwborderFlowAll.getFloworderdetail()).getString("cwbOrder")).getLong("deliverybranchid")
										: JSONObject.fromObject(JSONObject.fromObject(transcwborderFlowAll.getFloworderdetail()).getString("cwbOrder")).getLong("nextbranchid");
						String bname = this.branchDAO.getBranchByBranchid(deliverornextbranchid).getBranchid() == 0 ? "" : this.branchDAO.getBranchByBranchid(deliverornextbranchid).getBranchname();
						stringBuilder.append(simpleDateFormat.format(transcwborderFlowAll.getCredate()));
						stringBuilder.append("  ");
						stringBuilder.append(this.userDAO.getUserByUserid(transcwborderFlowAll.getUserid()).getRealname() + "（" + usermobile + "）");
						stringBuilder.append("\n");
						stringBuilder.append(this.branchDAO.getBranchByBranchid(transcwborderFlowAll.getBranchid()).getBranchname());
						stringBuilder.append(" ");
						stringBuilder.append(transcwborderFlowAll.getFlowordertypeText());
						if ((transcwborderFlowAll.getFlowordertype() == FlowOrderTypeEnum.DaoRuShuJu.getValue())
								|| (transcwborderFlowAll.getFlowordertype() == FlowOrderTypeEnum.UpdateDeliveryBranch.getValue())
								|| (transcwborderFlowAll.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue())
								|| (transcwborderFlowAll.getFlowordertype() == FlowOrderTypeEnum.TuiHuoChuZhan.getValue())
								|| (transcwborderFlowAll.getFlowordertype() == FlowOrderTypeEnum.ShouGongXiuGai.getValue())) {
							stringBuilder.append("\n");
							stringBuilder.append(bname);
							// stringBuilder.append(" ");
							// stringBuilder.append(usermobile);
							stringBuilder.append(" ");
							stringBuilder.append("[" + this.getBackReason(transcwborderFlowAll) + "]");
							stringBuilder.append(" ");
						}
						strings.add(stringBuilder.toString());
					}
				}
			} else {
				List<OrderFlow> orderFlows = this.orderFlowDAO.getOrderFlowByCwb(cwb);

				for (OrderFlow orderFlow : orderFlows) {
					StringBuilder stringBuilder = new StringBuilder();
					Branch branch = this.branchDAO.getBranchByBranchid(orderFlow.getBranchid());
					long sitetype = branch.getBranchid() == 0 ? 0 : branch.getSitetype();
					if (isHWHQPDASelect.equals("yes")) {
						if ((sitetype == BranchEnum.KuFang.getValue())
								&& ((orderFlow.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue()) || (orderFlow.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()))) {
							long deliverornextbranchid = orderFlow.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue()
									? JSONObject.fromObject(JSONObject.fromObject(orderFlow.getFloworderdetail()).getString("cwbOrder")).getLong("deliverybranchid")
									: JSONObject.fromObject(JSONObject.fromObject(orderFlow.getFloworderdetail()).getString("cwbOrder")).getLong("nextbranchid");
							String bname = this.branchDAO.getBranchByBranchid(deliverornextbranchid).getBranchid() == 0 ? ""
									: this.branchDAO.getBranchByBranchid(deliverornextbranchid).getBranchname();

							stringBuilder.append(simpleDateFormat.format(orderFlow.getCredate()));
							stringBuilder.append("  ");
							stringBuilder.append(this.userDAO.getUserByUserid(orderFlow.getUserid()).getRealname() + "（" + usermobile + "）");
							stringBuilder.append(" ");
							stringBuilder.append(orderFlow.getFlowordertypeText());
							stringBuilder.append("\n");
							stringBuilder.append(bname);
							// stringBuilder.append(" ");
							// stringBuilder.append(usermobile);
							stringBuilder.append(" ");
							stringBuilder.append("[" + JSONObject.fromObject(JSONObject.fromObject(orderFlow.getFloworderdetail()).getString("cwbOrder")).getString("backreason") + "]");
							stringBuilder.append(" ");
							strings.add(stringBuilder.toString());
						}
					} else {
						long deliverornextbranchid = ((orderFlow.getFlowordertype() == FlowOrderTypeEnum.DaoRuShuJu.getValue())
								|| (orderFlow.getFlowordertype() == FlowOrderTypeEnum.UpdateDeliveryBranch.getValue()) || (orderFlow.getFlowordertype() == FlowOrderTypeEnum.ShouGongXiuGai.getValue()))
										? JSONObject.fromObject(JSONObject.fromObject(orderFlow.getFloworderdetail()).getString("cwbOrder")).getLong("deliverybranchid")
										: JSONObject.fromObject(JSONObject.fromObject(orderFlow.getFloworderdetail()).getString("cwbOrder")).getLong("nextbranchid");
						String bname = this.branchDAO.getBranchByBranchid(deliverornextbranchid).getBranchid() == 0 ? "" : this.branchDAO.getBranchByBranchid(deliverornextbranchid).getBranchname();
						stringBuilder.append(simpleDateFormat.format(orderFlow.getCredate()));
						stringBuilder.append("  ");
						stringBuilder.append(this.userDAO.getUserByUserid(orderFlow.getUserid()).getRealname() + "（" + usermobile + "）");
						stringBuilder.append("\n");
						stringBuilder.append(this.branchDAO.getBranchByBranchid(orderFlow.getBranchid()).getBranchname());
						stringBuilder.append(" ");
						stringBuilder.append(orderFlow.getFlowordertypeText());
						if ((orderFlow.getFlowordertype() == FlowOrderTypeEnum.DaoRuShuJu.getValue()) || (orderFlow.getFlowordertype() == FlowOrderTypeEnum.UpdateDeliveryBranch.getValue())
								|| (orderFlow.getFlowordertype() == FlowOrderTypeEnum.ChuKuSaoMiao.getValue()) || (orderFlow.getFlowordertype() == FlowOrderTypeEnum.TuiHuoChuZhan.getValue())
								|| (orderFlow.getFlowordertype() == FlowOrderTypeEnum.ShouGongXiuGai.getValue())) {
							stringBuilder.append("\n");
							stringBuilder.append(bname);
							// stringBuilder.append(" ");
							// stringBuilder.append(usermobile);
							stringBuilder.append(" ");
							stringBuilder.append("[" + JSONObject.fromObject(JSONObject.fromObject(orderFlow.getFloworderdetail()).getString("cwbOrder")).getString("backreason") + "]");
							stringBuilder.append(" ");
						}
						strings.add(stringBuilder.toString());
					}
				}
			}

		}
		PDAResponse.setOrderFlows(strings);

		return PDAResponse;
	}

	/* 获得每一个单据的反馈信息 */
	public String getBackReason(TranscwbOrderFlow tf) {
		return JSONObject.fromObject(JSONObject.fromObject(tf.getFloworderdetail()).getString("cwbOrder")).getString("backreason");
	}

	@RequestMapping("/getMoHuBranch")
	@ResponseBody
	public List<Branch> getMoHuBranch(@RequestParam(value = "branchName") String branchName) {
		List<Branch> branchlist = this.branchDAO.getMoHuBranch(branchName);
		return branchlist;
	}

	@RequestMapping("/getMoHuUser")
	@ResponseBody
	public List<User> getMoHuUser(@RequestParam(value = "userName") String userName) {
		return this.userDAO.getMoHuUser(userName);
	}

	// 暂时不支持使用PDA进行唯品会上门换业务操作
	private void checkVipSmh(String cwb, FlowOrderTypeEnum flowOrderTypeEnum) {
		CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);
		if (co != null && co.getExchangeflag() == VipExchangeFlagEnum.YES.getValue()) {
			throw new CwbException(cwb, flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.VipShangmenhuanOperateNot);
		}
	}

	// 暂时不支持使用PDA进行唯品会上门换业务操作
	private void checkVipSmh(CwbOrder co, FlowOrderTypeEnum flowOrderTypeEnum) {
		if (co != null && co.getExchangeflag() == VipExchangeFlagEnum.YES.getValue()) {
			throw new CwbException(co.getCwb(), flowOrderTypeEnum.getValue(), ExceptionCwbErrorTypeEnum.VipShangmenhuanOperateNot);
		}
	}

	/**
	 * pda 确认交货接口
	 * 
	 * @param pickExpressTime
	 * @param cwb
	 * @return
	 */
	@RequestMapping("/executeIntoStationPda")
	@ResponseBody
	public String executeIntoStationPda(@RequestParam("pickExpressTime") String pickExpressTime, @RequestParam("cwb") String cwb,@RequestParam("username")String username) {
		User user=this.userDAO.getUserByUsername(username);
		this.logger.info("当前确认交货的用户为 "+user.getUsername());
		String data = this.cwborderService.executeIntoStationPda(cwb, pickExpressTime, user);
		return data;
	}

	/**
	 * pda 站长查询快递接口
	 */
	@RequestMapping("/expressQueryList")
	@ResponseBody
	public String expressQueryList(@RequestParam("username")String username,@RequestParam(value="processState",required=false,defaultValue= "1" )String processState) {
		String deliveryIds="";
		// 获取当前毫秒
		long startTime = System.currentTimeMillis();
		User user = this.userDAO.getUserByUserIdAndWareHouse(username);
		if(null!=user){
			deliveryIds=String.valueOf(user.getUserid());
			List<User> uList = this.getRoleUsers(user.getBranchid());
			if (user.getUserid()>0) {
				if (uList.size() != 0) {
					for (User u : uList) {
						deliveryIds+=","+u.getUserid();
					}
				}
			}
		}else{
			user=this.userDAO.getUserByUsername(username);
			deliveryIds=String.valueOf(user.getUserid());
		}
		List<ExpressIntoDto> queryExpressList = this.cwborderService.queryExpressListByPage(deliveryIds,new Integer(processState), user.getBranchid());
		logger.info("pda进入快递查询的时间共：" + (System.currentTimeMillis() - startTime) + "毫秒");

		return JSONArray.fromObject(queryExpressList).toString(); 
	}
	/**
	 * 获取当前站点的所有用戶
	 * @return
	 */
	private List<User> getRoleUsers(long userid) {
		String roleids = "2,4";
		List<Role> roles = this.roleDAO.getRolesByIsdelivery();
		if ((roles != null) && (roles.size() > 0)) {
			for (Role r : roles) {
				roleids += "," + r.getRoleid();
			}
		}
		List<User> uList = this.userDAO.getUserByRolesAndBranchid(roleids, userid);
		return uList;
	}
	@RequestMapping("/inputSave")
	@ResponseBody
	public String inputSaveAPP(HttpServletRequest request,HttpServletResponse response){
		//快递单导入
		EmbracedOrderVO embracedOrderVO=new EmbracedOrderVO();
		embracedOrderVO = this.conventEmbracedOrderVo(embracedOrderVO,request);
		String flag = this.embracedOrderInputService.savaEmbracedOrderVO(embracedOrderVO, 2);
		JSONObject jsonObject=new JSONObject();
		if("true".equals(flag)){
			jsonObject.element("code", "00");
			jsonObject.element("msg", "录入成功");
		}else if("false".equals(flag)){
			jsonObject.element("code", "11");
			jsonObject.element("msg", "录入失败");
		}else{
			jsonObject.element("code", "22");
			jsonObject.element("msg", "订单已经存在");
		}
		
		return jsonObject.toString();
	}

	private EmbracedOrderVO conventEmbracedOrderVo(EmbracedOrderVO embracedOrderVO, HttpServletRequest request) {
		embracedOrderVO.setOrderNo(null==request.getParameter("orderNo")?"":request.getParameter("orderNo"));
		embracedOrderVO.setDelivermanId(String.valueOf(this.getSessionUser().getUserid()));
		embracedOrderVO.setDelivermanName(String.valueOf(this.getSessionUser().getUsername()));
		embracedOrderVO.setDeliverid(null==request.getParameter("deliverid")?"":request.getParameter("deliverid"));
		embracedOrderVO.setDelivername(null==request.getParameter("delivername")?"":request.getParameter("delivername"));
		embracedOrderVO.setSender_provinceid(String.valueOf(this.provinceDAO.getProvinceByCode(null==request.getParameter("sender_provinceid")?"":request.getParameter("sender_provinceid")).getId()));
		embracedOrderVO.setSender_provinceName(null==request.getParameter("sender_provinceName")?"":request.getParameter("sender_provinceName"));
		embracedOrderVO.setSender_cityid(String.valueOf(this.cityDAO.getCityByCode(null==request.getParameter("sender_cityid")?"":request.getParameter("sender_cityid")).getId()));//
		embracedOrderVO.setSender_cityName(null==request.getParameter("sender_cityName")?"":request.getParameter("sender_cityName"));
		embracedOrderVO.setSender_cellphone(null==request.getParameter("sender_cellphone")?"":request.getParameter("sender_cellphone"));
		embracedOrderVO.setSender_telephone(null==request.getParameter("sender_telephone")?"":request.getParameter("sender_telephone"));
		embracedOrderVO.setSender_adress(null==request.getParameter("sender_adress")?"":request.getParameter("sender_adress"));
		embracedOrderVO.setConsignee_provinceid(String.valueOf(this.provinceDAO.getProvinceByCode(null==request.getParameter("consignee_provinceid")?"":request.getParameter("consignee_provinceid")).getId()));
		embracedOrderVO.setConsignee_provinceName(null==request.getParameter("consignee_provinceName")?"":request.getParameter("consignee_provinceName"));
		embracedOrderVO.setConsignee_cityid(String.valueOf(this.cityDAO.getCityByCode(null==request.getParameter("consignee_cityid")?"":request.getParameter("consignee_cityid")).getId()));
		embracedOrderVO.setConsignee_cityName(null==request.getParameter("consignee_cityName")?"":request.getParameter("consignee_cityName"));
		embracedOrderVO.setConsignee_cellphone(null==request.getParameter("consignee_cellphone")?"":request.getParameter("consignee_cellphone"));
		embracedOrderVO.setConsignee_telephone(null==request.getParameter("consignee_telephone")?"":request.getParameter("consignee_telephone"));
		embracedOrderVO.setConsignee_adress(null==request.getParameter("consignee_adress")?"":request.getParameter("consignee_adress"));
		embracedOrderVO.setNumber(null==request.getParameter("number")?"":request.getParameter("number"));
		embracedOrderVO.setFreight(null==request.getParameter("freight")?"":request.getParameter("freight"));
		embracedOrderVO.setIsadditionflag(null==request.getParameter("isadditionflag")?"":request.getParameter("isadditionflag"));
		embracedOrderVO.setInputhandlerid(String.valueOf(this.getSessionUser().getUserid()));
		embracedOrderVO.setInputhandlername(this.getSessionUser().getUsername());
		embracedOrderVO.setInputdatetime(null==request.getParameter("inputdatetime")?"":request.getParameter("inputdatetime"));
		embracedOrderVO.setSender_No(null==request.getParameter("sender_No")?"":request.getParameter("sender_No"));
		embracedOrderVO.setSender_companyName(null==request.getParameter("sender_companyName")?"":request.getParameter("sender_companyName"));
		//embracedOrderVO.setSender_customerid(Long.parseLong(null==request.getParameter("sender_customerid")?"":request.getParameter("sender_customerid")));
		embracedOrderVO.setSender_name(null==request.getParameter("sender_name")?"":request.getParameter("sender_name"));
		embracedOrderVO.setSender_countyid(String.valueOf(this.countyDAO.getCountyByCode(null==request.getParameter("sender_countyid")?"":request.getParameter("sender_countyid")).getId()));
		embracedOrderVO.setSender_countyName(null==request.getParameter("sender_countyName")?"":request.getParameter("sender_countyName"));
		embracedOrderVO.setSender_townid(null==request.getParameter("sender_townid")?"":request.getParameter("sender_townid"));
		embracedOrderVO.setSender_townName(null==request.getParameter("sender_townName")?"":request.getParameter("sender_townName"));
		embracedOrderVO.setSender_certificateNo(null==request.getParameter("sender_certificateNo")?"":request.getParameter("sender_certificateNo"));
		embracedOrderVO.setConsignee_No(null==request.getParameter("consignee_No")?"":request.getParameter("consignee_No"));
		embracedOrderVO.setConsignee_companyName(null==request.getParameter("consignee_companyName")?"":request.getParameter("consignee_companyName"));
		//embracedOrderVO.setConsignee_customerid(Long.parseLong(null==request.getParameter("consignee_customerid")?"":request.getParameter("consignee_customerid")));
		embracedOrderVO.setConsignee_name(null==request.getParameter("consignee_name")?"":request.getParameter("consignee_name"));
		embracedOrderVO.setConsignee_countyid(String.valueOf(this.countyDAO.getCountyByCode(null==request.getParameter("consignee_countyid")?"":request.getParameter("consignee_countyid")).getId()));
		embracedOrderVO.setConsignee_countyName(null==request.getParameter("consignee_countyName")?"":request.getParameter("consignee_countyName"));
		embracedOrderVO.setConsignee_townid(null==request.getParameter("consignee_townid")?"":request.getParameter("consignee_townid"));
		embracedOrderVO.setConsignee_townName(null==request.getParameter("consignee_townName")?"":request.getParameter("consignee_townName"));
		embracedOrderVO.setConsignee_certificateNo(null==request.getParameter("consignee_certificateNo")?"":request.getParameter("consignee_certificateNo"));
		embracedOrderVO.setGoods_name(null==request.getParameter("goods_name")?"":request.getParameter("goods_name"));
		embracedOrderVO.setGoods_number(null==request.getParameter("goods_number")?"":request.getParameter("goods_number"));
		embracedOrderVO.setGoods_weight(null==request.getParameter("goods_weight")?"":request.getParameter("goods_weight"));
		embracedOrderVO.setGoods_longth(null==request.getParameter("goods_longth")?"":request.getParameter("goods_longth"));
		embracedOrderVO.setGoods_width(null==request.getParameter("goods_width")?"":request.getParameter("goods_width"));
		embracedOrderVO.setGoods_height(null==request.getParameter("goods_height")?"":request.getParameter("goods_height"));
		embracedOrderVO.setGoods_kgs(null==request.getParameter("goods_kgs")?"":request.getParameter("goods_kgs"));
		embracedOrderVO.setGoods_other(null==request.getParameter("goods_other")?"":request.getParameter("goods_other"));
		embracedOrderVO.setCollection(null==request.getParameter("collection")?"":request.getParameter("collection"));
		embracedOrderVO.setCollection_amount(null==request.getParameter("collection_amount")?"":request.getParameter("collection_amount"));
		embracedOrderVO.setPacking_amount(null==request.getParameter("packing_amount")?"":request.getParameter("packing_amount"));
		embracedOrderVO.setInsured(null==request.getParameter("insured")?"":request.getParameter("insured"));
		embracedOrderVO.setInsured_amount(null==request.getParameter("insured_amount")?"":request.getParameter("insured_amount"));
		embracedOrderVO.setInsured_cost(null==request.getParameter("insured_cost")?"":request.getParameter("insured_cost"));
		embracedOrderVO.setCharge_weight(null==request.getParameter("charge_weight")?"":request.getParameter("charge_weight"));
		embracedOrderVO.setActual_weight(null==request.getParameter("actual_weight")?"":request.getParameter("actual_weight"));
		embracedOrderVO.setFreight_total(null==request.getParameter("freight_total")?"":request.getParameter("freight_total"));
		embracedOrderVO.setOrigin_adress(null==request.getParameter("origin_adress")?"":request.getParameter("origin_adress"));
		embracedOrderVO.setDestination(null==request.getParameter("destination")?"":request.getParameter("destination"));
		embracedOrderVO.setPayment_method(null==request.getParameter("payment_method")?"":request.getParameter("payment_method"));
		embracedOrderVO.setRemarks(null==request.getParameter("remarks")?"":request.getParameter("remarks"));
		embracedOrderVO.setInstationhandlerid(String.valueOf(this.getSessionUser().getUserid()));
		embracedOrderVO.setInstationhandlername(String.valueOf(this.getSessionUser().getUsername()));
		embracedOrderVO.setInstationid((int)this.getSessionUser().getBranchid());
		embracedOrderVO.setInstationname(null==request.getParameter("instationname")?"":request.getParameter("instationname"));
		embracedOrderVO.setInstationdatetime(null==request.getParameter("instationdatetime")?"":request.getParameter("instationdatetime"));
		embracedOrderVO.setFlowordertype(null==request.getParameter("flowordertype")?"":request.getParameter("flowordertype"));
		embracedOrderVO.setCarsize(null==request.getParameter("carsize")?"":request.getParameter("carsize"));
		embracedOrderVO.setExpress_product_type(Integer.valueOf(null==request.getParameter("express_product_type")?"":request.getParameter("express_product_type")));
		embracedOrderVO.setPaywayid(Integer.valueOf(null==request.getParameter("paywayid")?"":request.getParameter("paywayid")));
		//embracedOrderVO.setRecordVersion(Long.valueOf(null==request.getParameter("recordVersion")?"":request.getParameter("recordVersion")));
		return embracedOrderVO;
	}
	@RequestMapping("/getAddress")
	@ResponseBody
	public String getAddress(){
		return cwbOrderPdaService.getAddress();
	}
	
	
	@RequestMapping("/executeOutStation")
	@ResponseBody
	public ExpressOpeAjaxResult expressOutStationExecute(Model model, HttpServletRequest request, HttpServletResponse response) {
		ExpressOutStationParamsVO params = this.getRequestParams(request);
		//当前请求时间
		long startTime = System.currentTimeMillis();
		//返回数据
		ExpressOpeAjaxResult res = new ExpressOpeAjaxResult();
		//获取请求路径
		String contextPath = request.getContextPath();
		Map<String, Object> checkResMap = new HashMap<String, Object>();
		try {
			//校验订单号是否为空
			if (!Tools.isEmpty(params.getScanNo())) {
				//赋值当前路径
				params.setContextPath(contextPath);
				//快递单号
				String scanNo = params.getScanNo();
				scanNo = this.cwborderService.translateCwb(scanNo);// 订单号和运单号的转换
				checkResMap = this.expressOutStationService.checkIsOrderOrBaleOperation(scanNo, params);
				//声音文件路径
				String wavPath = null;
				if (ExpressOutStationFlagEnum.OrderNo.getValue().equals(checkResMap.get("opeFlag"))) {
					// 订单号操作
					res = this.expressOutStationService.executeOutStationOpeOrderNo(this.getSessionUser(), scanNo, params);
					if (res.getStatus()) {// 成功
						// 匹配地址库
						CwbOrder cwbOrder = this.cwborderService.getCwbByCwb(scanNo);
						this.matchStation(scanNo, this.getSessionUser().getUserid(), cwbOrder.getConsigneeaddress());
						wavPath = this.getErrorWavFullPath(request, CwbOrderPDAEnum.OK.getVediourl());
					} else {
						wavPath = this.getErrorWavFullPath(request, CwbOrderPDAEnum.SYS_ERROR.getVediourl());
					}

				} else if (ExpressOutStationFlagEnum.BaleNo.getValue().equals(checkResMap.get("opeFlag"))) {
					// 包号操作
					res = this.expressOutStationService.executeOutStationOpeBaleNo(this.getSessionUser(), scanNo, params);
					if (res.getStatus()) {
						// 匹配地址库
						Bale bale=(Bale) checkResMap.get("bale");
						List<String> cwbList = this.cwborderService.getCwbsByBale(bale.getId());
						for (String cwb : cwbList) {
							CwbOrder cwbOrder = this.cwborderService.getCwbByCwb(cwb);
							this.matchStation(cwb, this.getSessionUser().getUserid(), cwbOrder.getConsigneeaddress());
						}
						wavPath = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.OK.getVediourl();
					} else {
						wavPath = request.getContextPath() + ServiceUtil.waverrorPath + CwbOrderPDAEnum.Feng_Bao.getVediourl();
					}
				}
				res.addLongWav(wavPath);
			}
		} catch (Exception e) {
			checkResMap.put("opeFlag", ExpressOutStationFlagEnum.OrderNo.getValue());
			res.setAttributes(checkResMap);
			res.setStatus(false);
			res.setMsg(e.getMessage());
			String wavPath = this.getErrorWavFullPath(request, CwbOrderPDAEnum.SYS_ERROR.getVediourl());
			res.addLongWav(wavPath);
		}
		this.logger.info("进入揽件出站页面的时间共：" + (System.currentTimeMillis() - startTime) + "毫秒");
		return res;
	}
	
	private void matchStation(String cwb, long userid, String address) {
		Boolean matchFlag = this.tpsInterfaceExecutor.autoMatch(cwb, userid, address, AddressMatchEnum.ExpressOutStation.getValue());
		if (matchFlag) {
			this.logger.info("揽件出站调用地址库jms消息发送成功");

		} else {
			this.logger.info("揽件出站调用地址库jms消息发送失败");
		}
	}

	private String getErrorWavFullPath(HttpServletRequest request, String fillName) {
		return request.getContextPath() + ServiceUtil.waverrorPath + fillName;
	}

	private String getWavFullPath(HttpServletRequest request, String fillName) {
		return request.getContextPath() + ServiceUtil.wavPath + fillName;
	}
	private ExpressOutStationParamsVO getRequestParams(HttpServletRequest request){
		ExpressOutStationParamsVO expressOutStationParamsVO=new ExpressOutStationParamsVO();
		expressOutStationParamsVO.setScanNo(null==request.getParameter("scanNo")?"":request.getParameter("scanNo"));
		expressOutStationParamsVO.setNextBranch(Long.parseLong(request.getParameter("nextBranch")));
		return expressOutStationParamsVO;
	}

}
