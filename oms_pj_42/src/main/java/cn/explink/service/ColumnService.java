package cn.explink.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Service;

import cn.explink.dao.SetExcelColumnDAO;
import cn.explink.domain.ExcelColumnSet;

@Service
public class ColumnService {

	@Autowired
	SetExcelColumnDAO setExcelColumnDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	public void addColumn(ExcelColumnSet excelColumnSet) {
		setExcelColumnDAO.creColumn(excelColumnSet);
	}

	public void editColumn(ExcelColumnSet excelColumnSer) {
		setExcelColumnDAO.saveColumn(excelColumnSer);
	}

	public ExcelColumnSet loadFormForColumn(HttpServletRequest request, long columnid) {

		ExcelColumnSet ecs = loadFormForColumn(request);
		ecs.setColumnid(columnid);
		return ecs;
	}

	public ExcelColumnSet loadFormForColumn(HttpServletRequest request) {
		ExcelColumnSet excelcolumnset = new ExcelColumnSet();
		excelcolumnset.setCustomerid(Integer.parseInt(request.getParameter("customerid") == null ? "0" : request.getParameter("customerid")));
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
		// excelcolumnset.setEmaildateindex(Integer.parseInt(request.getParameter("emaildateindex")));
		excelcolumnset.setExcelbranchindex(Integer.parseInt(request.getParameter("excelbranchindex")));
		excelcolumnset.setExceldeliverindex(Integer.parseInt(request.getParameter("exceldeliverindex")));
		excelcolumnset.setGetmobileflag(Integer.parseInt(request.getParameter("getmobileflag")));
		excelcolumnset.setShipcwbindex(Integer.parseInt(request.getParameter("shipcwbindex")));
		excelcolumnset.setWarehousenameindex(Integer.parseInt(request.getParameter("warehousenameindex")));
		excelcolumnset.setShippernameindex(Integer.parseInt(request.getParameter("shippernameindex")));
		excelcolumnset.setUpdatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		excelcolumnset.setUpdateuserid(userDetail.getUser().getUserid());
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
		 * 
		 * 
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
