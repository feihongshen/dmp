package cn.explink.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import cn.explink.controller.CwbOrderDTO;
import cn.explink.controller.CwbOrderImportDTO;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CommonDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbOrderTypeDAO;
import cn.explink.dao.SetExcelColumnDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Common;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.ExcelColumnSet;
import cn.explink.domain.ImportValidationManager;

public abstract class ExcelExtractor {

	@Autowired
	SetExcelColumnDAO setExcelColumnDAO;

	@Autowired
	ImportValidationManager importValidationManager;

	@Autowired
	CwbOrderTypeDAO cwbOrderTypeDAO;

	@Autowired
	ResultCollectorManager resultCollectorManager;

	@Autowired
	BranchDAO branchDAO;

	@Autowired
	CommonDAO commonDAO;

	@Autowired
	CustomerDAO customerDAO;

	@Autowired
	ImportCwbErrorService importCwbErrorService;
	@Autowired
	CustomWareHouseDAO customWareHouseDAO;

	public List<CwbOrderDTO> extract(InputStream f, long customerid, ResultCollector errorCollector, String defaultEmailDate) {
		ExcelColumnSet excelColumnSet = setExcelColumnDAO.getExcelColumnSetByCustomerid(customerid);
		List<CwbOrderDTO> cwbOrders = getCwbOrders(f, customerid, errorCollector, excelColumnSet, defaultEmailDate);
		// validate(errorCollector, cwbOrders, excelColumnSet);
		return cwbOrders;
	}

	public List<CwbOrderImportDTO> extractImport(MultipartFile f) throws IOException {
		Map map = getTitle(f.getInputStream());
		List<CwbOrderImportDTO> cwbOrders = getImportCwbOrders(f.getInputStream(), map);
		return cwbOrders;
	}

	public Map getTitle(InputStream f) {
		return getRowsTitle(f);
	}

	// void validate(ResultCollector errorCollector, List<CwbOrderDTO>
	// cwbOrders, ExcelColumnSet excelColumnSet) {
	//
	// for(CwbOrderDTO cwbOrderDTO:cwbOrders){
	// try {
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// errorCollector.addError(cwbOrderDTO.getCwb(), e.getMessage());
	// }
	// }
	// }

	protected String strtovalid(String str) {
		String cwb = "";
		for (int i = 0; i < str.length(); i++) {
			int asc = (int) str.charAt(i);
			if (asc >= 32 && asc <= 127 || // 英文字符，标点符号，数字
					(str.charAt(i) + "").matches("[\u4e00-\u9fa5]+")) { // //判断字符是否为中文
				cwb += str.charAt(i);
			}
		}
		return cwb;
	}

	protected CwbOrderDTO getCwbOrderAccordingtoConf(ExcelColumnSet excelColumnSet, Object row, long customerid, String defaultEmailDate, List<Branch> branchList, List<Common> commonList)
			throws Exception {
		CwbOrderDTO cwbOrder = new CwbOrderDTO();

		cwbOrder.setCwb(getXRowCellData(row, excelColumnSet.getCwbindex()));

		if (excelColumnSet.getTranscwbindex() != 0) {
			cwbOrder.setTranscwb(getXRowCellData(row, excelColumnSet.getTranscwbindex()));
		}

		/**
		 * 派送区域 20120213
		 */

		if (excelColumnSet.getConsigneenameindex() != 0) {
			cwbOrder.setConsigneename(getXRowCellData(row, excelColumnSet.getConsigneenameindex()));
		}
		if (excelColumnSet.getConsigneeaddressindex() != 0) {
			cwbOrder.setConsigneeaddress(getXRowCellData(row, excelColumnSet.getConsigneeaddressindex()));
		}
		if (excelColumnSet.getConsigneepostcodeindex() != 0) {
			cwbOrder.setConsigneepostcode(getXRowCellData(row, excelColumnSet.getConsigneepostcodeindex()));
		}
		if (excelColumnSet.getConsigneephoneindex() != 0) {
			cwbOrder.setConsigneephone(getXRowCellData(row, excelColumnSet.getConsigneephoneindex()), excelColumnSet.getGetmobileflag() == 1);
		}
		if (excelColumnSet.getSendcargonameindex() != 0) {
			cwbOrder.setSendcargoname(getXRowCellData(row, excelColumnSet.getSendcargonameindex()));
		}
		if (excelColumnSet.getBackcargonameindex() != 0) {
			cwbOrder.setBackcargoname(getXRowCellData(row, excelColumnSet.getBackcargonameindex()));
		}
		if (excelColumnSet.getReceivablefeeindex() != 0) {
			cwbOrder.setReceivablefee(getXRowCellData(row, excelColumnSet.getReceivablefeeindex()));
			// 存储代收金额的同时，将代收金额备份到原始金额中一份
			cwbOrder.setPrimitivemoney(getXRowCellData(row, excelColumnSet.getReceivablefeeindex()));
		}
		if (excelColumnSet.getPaybackfeeindex() != 0) {
			cwbOrder.setPaybackfee(getXRowCellData(row, excelColumnSet.getPaybackfeeindex()));
		}
		if (excelColumnSet.getCargorealweightindex() != 0) {
			cwbOrder.setCargorealweight(getXRowCellData(row, excelColumnSet.getCargorealweightindex()));
		}
		if (excelColumnSet.getCwbremarkindex() != 0) {
			cwbOrder.setCwbremark(getXRowCellData(row, excelColumnSet.getCwbremarkindex()));
		}

		if (excelColumnSet.getEmaildateindex() != 0) {
			cwbOrder.setEmaildate(getXRowCellData(row, excelColumnSet.getEmaildateindex()));
		} else {
			cwbOrder.setEmaildate(defaultEmailDate);
		}

		if (excelColumnSet.getConsigneenoindex() != 0) {
			cwbOrder.setConsigneeno(getXRowCellData(row, excelColumnSet.getConsigneenoindex()));
		}
		if (excelColumnSet.getExcelbranchindex() != 0) {
			cwbOrder.setExcelbranch(getXRowCellData(row, excelColumnSet.getExcelbranchindex()));
			for (Branch b : branchList) {
				if (b.getBranchname().equals(cwbOrder.getExcelbranch())) {
					cwbOrder.setNextbranchid(b.getBranchid());
					break;
				}
			}
		}
		if (excelColumnSet.getExceldeliverindex() != 0) {
			cwbOrder.setExceldeliver(getXRowCellData(row, excelColumnSet.getExceldeliverindex()));
		}
		if (excelColumnSet.getCargoamountindex() != 0) {
			cwbOrder.setCargoamount(getXRowCellData(row, excelColumnSet.getCargoamountindex()));
		}
		if (excelColumnSet.getCustomercommandindex() != 0) {
			cwbOrder.setCustomercommand(getXRowCellData(row, excelColumnSet.getCustomercommandindex()));
		}
		if (excelColumnSet.getCargotypeindex() != 0) {
			cwbOrder.setCargotype(getXRowCellData(row, excelColumnSet.getCargotypeindex()));
		}
		if (excelColumnSet.getCargosizeindex() != 0) {
			cwbOrder.setCargosize(getXRowCellData(row, excelColumnSet.getCargosizeindex()));
		}
		if (excelColumnSet.getBackcargoamountindex() != 0) {
			cwbOrder.setBackcargoamount(getXRowCellData(row, excelColumnSet.getBackcargoamountindex()));
		}
		if (excelColumnSet.getDestinationindex() != 0) {
			cwbOrder.setDestination(getXRowCellData(row, excelColumnSet.getDestinationindex()));
		}
		if (excelColumnSet.getTranswayindex() != 0) {
			cwbOrder.setTransway(getXRowCellData(row, excelColumnSet.getTranswayindex()));
		}
		if (excelColumnSet.getSendcargonumindex() != 0) {
			cwbOrder.setSendcargonum(getXRowCellData(row, excelColumnSet.getSendcargonumindex()));
		}
		if (excelColumnSet.getBackcargonumindex() != 0) {
			cwbOrder.setBackcargonum(getXRowCellData(row, excelColumnSet.getBackcargonumindex()));
		}

		if (excelColumnSet.getCwbdelivertypeindex() != 0) {
			cwbOrder.setCwbdelivertypeid(getXRowCellData(row, excelColumnSet.getCwbdelivertypeindex()).equals("加急") ? 2 : 1);
		}
		if (excelColumnSet.getCwbprovinceindex() != 0) {
			cwbOrder.setCwbprovince(getXRowCellData(row, excelColumnSet.getCwbprovinceindex()));
		}
		if (excelColumnSet.getCwbcityindex() != 0) {
			cwbOrder.setCwbcity(getXRowCellData(row, excelColumnSet.getCwbcityindex()));
		}
		if (excelColumnSet.getCwbcountyindex() != 0) {
			cwbOrder.setCwbcounty(getXRowCellData(row, excelColumnSet.getCwbcountyindex()));
		}
		if (excelColumnSet.getWarehousenameindex() != 0) {
			CustomWareHouse cw = customWareHouseDAO.getCustomWareHouseByHousename(getXRowCellData(row, excelColumnSet.getWarehousenameindex()), customerid);
			if (cw != null) {
				cwbOrder.setCustomerwarehouseid(cw.getWarehouseid());
			}

		}
		if (excelColumnSet.getConsigneemobileindex() != 0) {
			cwbOrder.setConsigneemobile(getXRowCellData(row, excelColumnSet.getConsigneemobileindex()));
		}

		if (excelColumnSet.getShipcwbindex() != 0) {
			cwbOrder.setShipcwb(getXRowCellData(row, excelColumnSet.getShipcwbindex()));
		}
		if (excelColumnSet.getCwbordertypeindex() != 0) {
			cwbOrder.setCwbordertypeid(cwbOrderTypeDAO.getImporttypeidByImportType(getXRowCellData(row, excelColumnSet.getCwbordertypeindex())));
		} else {
			cwbOrder.guessCwbordertypeid();
		}
		if (excelColumnSet.getCommonnumberindex() != 0) {
			cwbOrder.setCommonnumber(getXRowCellData(row, excelColumnSet.getCommonnumberindex()).toUpperCase());
			for (Common c : commonList) {
				if (c.getCommonnumber().equals(cwbOrder.getCommonnumber())) {
					cwbOrder.setCommonname(c.getCommonname());
					cwbOrder.setCommonid(c.getId());
					cwbOrder.setOrderprefix(c.getOrderprefix());
					cwbOrder.setCommonstate(c.getCommonstate());
					break;
				}
			}
		}

		/*
		 * if (excelColumnSet.getOrdercwbindex() != 0) {
		 * cwbOrder.setOrdercwb(getXRowCellData(row,
		 * excelColumnSet.getOrdercwbindex())); }
		 * 
		 * if (excelColumnSet.getServiceareaindex() != 0) {
		 * cwbOrder.setPaisongArea( getXRowCellData(row,
		 * excelColumnSet.getServiceareaindex())); }
		 */

		cwbOrder.setDefaultCargoName();

		return cwbOrder;
	}

	protected CwbOrderImportDTO getImportCwbOrderAccordingtoConf(Object row, Map map) throws Exception {
		CwbOrderImportDTO cwbOrder = new CwbOrderImportDTO();
		Map map1 = map;
		if (map1.get("订单号") != null) {
			cwbOrder.setCwb(getXRowCellData(row, Integer.parseInt(map1.get("订单号").toString())));
		}
		if (map1.get("签收人") != null) {
			cwbOrder.setSigninman(getXRowCellData(row, Integer.parseInt(map1.get("签收人").toString())));
		}
		if (map1.get("签收时间") != null) {
			cwbOrder.setSignintime(getXRowCellData(row, Integer.parseInt(map1.get("签收时间").toString())));
		}
		if (map1.get("备注") != null) {
			cwbOrder.setCwbremark(getXRowCellData(row, Integer.parseInt(map1.get("备注").toString())));
		}
		return cwbOrder;
	}

	protected List<CwbOrderDTO> getCwbOrders(InputStream f, long customerid, ResultCollector errorCollector, ExcelColumnSet excelColumnSet, String defaultEmailDate) {
		List<Branch> branchList = branchDAO.getAllBranches();// 获取所有branch记录，用于匹配下一站id
		List<Common> commonList = commonDAO.getAllCommon();

		List<CwbOrderDTO> cwbOrders = new ArrayList<CwbOrderDTO>();
		List<CwbOrderValidator> vailidators = importValidationManager.getVailidators(excelColumnSet);
		for (Object row : getRows(f)) {
			try {
				CwbOrderDTO cwbOrder = getCwbOrderAccordingtoConf(excelColumnSet, row, customerid, defaultEmailDate, branchList, commonList);
				for (CwbOrderValidator cwbOrderValidator : vailidators) {
					cwbOrderValidator.validate(cwbOrder);
				}
				cwbOrders.add(cwbOrder);
			} catch (Exception e) {
				e.printStackTrace();
				errorCollector.addError(getXRowCellData(row, excelColumnSet.getCwbindex()), e.getMessage());

				// 失败订单数+1 前台显示
				resultCollectorManager.setFailureImportCount(defaultEmailDate, (resultCollectorManager.getFailureImportCount(defaultEmailDate) + 1));

				// 存储报错订单，以便统计错误记录和处理错误订单
				JSONObject errorOrder = new JSONObject();
				errorOrder.put("cwbOrderDTO", "{\"cwb\":\"" + getXRowCellData(row, excelColumnSet.getCwbindex()) + "\",\"emaildate\":\"" + defaultEmailDate + "\"}");
				errorOrder.put("customerid", customerid);
				errorOrder.put("message", e.getMessage());
				try {
					// importCwbErrorProducer_mgr.sendBodyAndHeader(null,"errorOrder",errorOrder.toString());
				} catch (Exception ex) {
					importCwbErrorService.saveError(errorOrder.toString());
				}
			}
		}
		return cwbOrders;
	}

	protected List<CwbOrderImportDTO> getImportCwbOrders(InputStream f, Map map) {

		List<CwbOrderImportDTO> cwbOrders = new ArrayList<CwbOrderImportDTO>();
		for (Object row : getRows(f)) {
			try {
				CwbOrderImportDTO cwbOrder = getImportCwbOrderAccordingtoConf(row, map);
				cwbOrders.add(cwbOrder);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return cwbOrders;
	}

	protected abstract String getXRowCellData(Object row, int cwbindex);

	protected abstract List<Object> getRows(InputStream f);

	protected abstract Map getRowsTitle(InputStream f);// 获取表头

}