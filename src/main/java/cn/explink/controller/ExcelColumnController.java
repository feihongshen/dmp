package cn.explink.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.CustomerDAO;
import cn.explink.dao.SetExcelColumnDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.ExcelColumnSet;
import cn.explink.domain.User;
import cn.explink.service.ColumnService;
import cn.explink.service.ExplinkUserDetail;

@RequestMapping("/excelcolumn")
@Controller
public class ExcelColumnController {

	@Autowired
	SetExcelColumnDAO setExcelColumnDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	ColumnService columnService;
	@Autowired
	UserDAO userDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/list")
	public String list(Model model, HttpServletRequest request) {
		model.addAttribute("columns", setExcelColumnDAO.getExcelColumnAll());
		model.addAttribute("customers", customerDAO.getAllCustomersNew());

		return "/excelcolumn/list";
	}

	@RequestMapping("/view/{customerid}")
	public String view(Model model, @PathVariable("customerid") long customerid) {
		model.addAttribute("name", customerDAO.getCustomerById(customerid));
		model.addAttribute("customerid", setExcelColumnDAO.getExcelColumnSetByCustomerid(customerid));
		return "/excelcolumn/view";
	}

	@RequestMapping("/add")
	public String add(Model model, HttpServletRequest request) {
		model.addAttribute("customers", customerDAO.getAllCustomers());
		model.addAttribute("customerName", setExcelColumnDAO.getExcelColumnAll());
		return "/excelcolumn/add";
	}

	@RequestMapping("/create")
	public @ResponseBody String create(Model model, HttpServletRequest request) throws Exception {
		long customerid = Long.parseLong(request.getParameter("customerid").split("-")[0]);
		long num = setExcelColumnDAO.getSetExcelColumnSetByCustomerid(customerid);
		if (num > 0) {
			return "{\"errorCode\":1,\"error\":\"该供货商的导入模板已存在\"}";
		} else {
			ExcelColumnSet excelColumnSet = loadFormForColumn(request);
			columnService.addColumn(excelColumnSet);
			logger.info("operatorUser={},导入模版设置->create", getSessionUser().getUsername());
			return "{\"errorCode\":0,\"error\":\"新建成功\"}";
		}

	}

	@RequestMapping("/edit/{columnid}")
	public String edit(Model model, @PathVariable("columnid") long columnid) {
		model.addAttribute("e", setExcelColumnDAO.getColumnid(columnid));
		model.addAttribute("name", customerDAO.getCustomerById(setExcelColumnDAO.getColumnid(columnid).getCustomerid()));
		return "/excelcolumn/edit";
	}

	@RequestMapping("/save/{columnid}")
	public @ResponseBody String save(Model model, @PathVariable("columnid") long columnid, HttpServletRequest request) {
		ExcelColumnSet ecs = loadFormForColumn(request);
		ecs.setColumnid(columnid);
		columnService.editColumn(ecs);
		logger.info("operatorUser={},导入模版设置->save", getSessionUser().getUsername());
		return "{\"errorCode\":0,\"error\":\"修改成功\"}";
	}

	@RequestMapping("/del/{columnid}")
	public @ResponseBody String del(@PathVariable("columnid") long columnid) {
		setExcelColumnDAO.getDeleColumn(columnid);
		logger.info("operatorUser={},导入模版设置->del", getSessionUser().getUsername());
		return "{\"errorCode\":0}";
	}

	private ExcelColumnSet loadFormForColumn(HttpServletRequest request) {
		ExcelColumnSet excelcolumnset = new ExcelColumnSet();
		excelcolumnset.setCustomerid(Integer.parseInt(request.getParameter("customerid") == null ? "0" : request.getParameter("customerid").split("-")[0]));
		excelcolumnset.setCwbindex(Integer.parseInt(request.getParameter("cwbindex")));
		excelcolumnset.setConsigneenameindex(Integer.parseInt(request.getParameter("consigneenameindex")));
		excelcolumnset.setConsigneeaddressindex(Integer.parseInt(request.getParameter("consigneeaddressindex")));
		excelcolumnset.setConsigneepostcodeindex(Integer.parseInt(request.getParameter("consigneepostcodeindex")));
		excelcolumnset.setConsigneephoneindex(Integer.parseInt(request.getParameter("consigneephoneindex")));
		excelcolumnset.setConsigneemobileindex(Integer.parseInt(request.getParameter("consigneemobileindex")));
		excelcolumnset.setCwbremarkindex(Integer.parseInt(request.getParameter("cwbremarkindex")));
		excelcolumnset.setSendcargonameindex(Integer.parseInt(request.getParameter("sendcargonameindex")));
		excelcolumnset.setBackcargonameindex(Integer.parseInt(request.getParameter("backcargonameindex")));
		excelcolumnset.setCargorealweightindex(Integer.parseInt(request.getParameter("cargorealweightindex")));
		excelcolumnset.setReceivablefeeindex(Integer.parseInt(request.getParameter("receivablefeeindex")));
		excelcolumnset.setPaybackfeeindex(Integer.parseInt(request.getParameter("paybackfeeindex")));
		excelcolumnset.setConsigneenoindex(Integer.parseInt(request.getParameter("consigneenoindex")));
		excelcolumnset.setCargoamountindex(Integer.parseInt(request.getParameter("cargoamountindex")));
		excelcolumnset.setCustomercommandindex(Integer.parseInt(request.getParameter("customercommandindex")));
		excelcolumnset.setCargotypeindex(Integer.parseInt(request.getParameter("cargotypeindex")));
		// excelcolumnset.setCargowarehouseindex(Integer.parseInt(request.getParameter("cargowarehouseindex")));
		excelcolumnset.setCargosizeindex(Integer.parseInt(request.getParameter("cargosizeindex")));
		excelcolumnset.setBackcargoamountindex(Integer.parseInt(request.getParameter("backcargoamountindex")));
		excelcolumnset.setDestinationindex(Integer.parseInt(request.getParameter("destinationindex")));
		excelcolumnset.setTranswayindex(Integer.parseInt(request.getParameter("transwayindex")));
		excelcolumnset.setSendcargonumindex(Integer.parseInt(request.getParameter("sendcargonumindex")));
		excelcolumnset.setBackcargonumindex(Integer.parseInt(request.getParameter("backcargonumindex")));
		excelcolumnset.setCwbprovinceindex(Integer.parseInt(request.getParameter("cwbprovinceindex")));
		excelcolumnset.setCwbcityindex(Integer.parseInt(request.getParameter("cwbcityindex")));
		excelcolumnset.setCwbcountyindex(Integer.parseInt(request.getParameter("cwbcountyindex")));
		excelcolumnset.setCwbordertypeindex(Integer.parseInt(request.getParameter("cwbordertypeindex")));
		excelcolumnset.setCwbdelivertypeindex(Integer.parseInt(request.getParameter("cwbdelivertypeindex")));
		excelcolumnset.setTranscwbindex(Integer.parseInt(request.getParameter("transcwbindex")));

		excelcolumnset.setAccountareaindex(Integer.parseInt(request.getParameter("accountareaindex")));
		excelcolumnset.setEmaildateindex(Integer.parseInt(request.getParameter("emaildateindex")));
		excelcolumnset.setExcelbranchindex(Integer.parseInt(request.getParameter("excelbranchindex")));
		excelcolumnset.setExceldeliverindex(Integer.parseInt(request.getParameter("exceldeliverindex")));
		excelcolumnset.setGetmobileflag(Integer.parseInt(request.getParameter("getmobileflag")));
		excelcolumnset.setShipcwbindex(Integer.parseInt(request.getParameter("shipcwbindex")));
		excelcolumnset.setWarehousenameindex(Integer.parseInt(request.getParameter("warehousenameindex")));
		excelcolumnset.setCommonnumberindex(Integer.parseInt(request.getParameter("commonnumberindex")));
		excelcolumnset.setUpdatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		excelcolumnset.setUpdateuserid(userDetail.getUser().getUserid());
		excelcolumnset.setModelnameindex(Integer.parseInt(request.getParameter("modelnameindex")));

		excelcolumnset.setRemark1index(Integer.parseInt(request.getParameter("remark1index")));
		excelcolumnset.setRemark2index(Integer.parseInt(request.getParameter("remark2index")));
		excelcolumnset.setRemark3index(Integer.parseInt(request.getParameter("remark3index")));
		excelcolumnset.setRemark4index(Integer.parseInt(request.getParameter("remark4index")));
		excelcolumnset.setRemark5index(Integer.parseInt(request.getParameter("remark5index")));
		excelcolumnset.setPaywayindex(Integer.parseInt(request.getParameter("paywayindex")));
		excelcolumnset.setShouldfareindex(Integer.parseInt(request.getParameter("shouldfareindex")));
		
		/**订单导入模板新增退货地址，及商家退货号 add by chunlei05.li 2016/8/22*/
		// 为防止报错，采用兼容模式
		String returnnoindex = request.getParameter("returnnoindex");
		if (StringUtils.isNotBlank(returnnoindex)) {
			excelcolumnset.setReturnnoindex(Integer.parseInt(returnnoindex));
		}
		String returnaddressindex = request.getParameter("returnaddressindex");
		if (StringUtils.isNotBlank(returnaddressindex)) {
			excelcolumnset.setReturnaddressindex(Integer.parseInt(returnaddressindex));
		}
		/**************end*****************/
		
		/*
		 * excelcolumnset.setUpdateuserid(Integer.parseInt(request.getParameter(
		 * "updateuserid")));
		 * excelcolumnset.setPodsigninfoindex(Integer.parseInt
		 * (request.getParameter("podsigninfoindex")));
		 * excelcolumnset.setUpdatetime
		 * (StringUtil.nullConvertToEmptyString(request
		 * .getParameter("updatetime")));
		 * excelcolumnset.setOrdercwbindex(Integer
		 * .parseInt(request.getParameter("ordercwbindex")));
		 * 
		 * excelcolumnset.setServiceareaindex(Integer.parseInt(request.getParameter
		 * ("serviceareaindex")));
		 * excelcolumnset.setPlusbonusindex(Integer.parseInt
		 * (request.getParameter("plusbonusindex")));
		 * excelcolumnset.setPlustransfeeindex
		 * (Integer.parseInt(request.getParameter("plustransfeeindex")));
		 */

		return excelcolumnset;
	}

}
