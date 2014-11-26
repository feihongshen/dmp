package cn.explink.service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CommonDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbOrderTypeDAO;
import cn.explink.dao.PayWayDao;
import cn.explink.dao.ServiceAreaDAO;
import cn.explink.dao.SetExcelColumnDAO;
import cn.explink.dao.SwitchDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Common;
import cn.explink.domain.Customer;
import cn.explink.domain.EmailDate;
import cn.explink.domain.ExcelColumnSet;
import cn.explink.domain.ImportValidationManager;
import cn.explink.domain.ServiceArea;
import cn.explink.enumutil.CwbOrderAddressCodeEditTypeEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.enumutil.switchs.SwitchEnum;
import cn.explink.util.DateTimeUtil;

public abstract class ExcelExtractor {

	private static org.slf4j.Logger logger = LoggerFactory.getLogger(ExcelExtractor.class);
	@Autowired
	private SetExcelColumnDAO setExcelColumnDAO;

	@Autowired
	private ImportValidationManager importValidationManager;

	@Autowired
	private CwbOrderTypeDAO cwbOrderTypeDAO;

	@Autowired
	private CustomWareHouseDAO customWareHouseDAO;

	@Autowired
	private ServiceAreaDAO serviceAreaDAO;

	@Autowired
	private BranchDAO branchDAO;

	@Autowired
	private CommonDAO commonDAO;

	@Autowired
	private PayWayDao payWayDao;

	@Autowired
	private SwitchDAO switchDAO;

	@Autowired
	CustomerDAO customerDAO;

	@Autowired
	ImportCwbErrorService importCwbErrorService;

	@Autowired
	DataImportService dataImportService;

	public String strtovalid(String str) {
		// 2013-8-6 需求： 收件人地址让显示中文符号。最终是替换所有的中文符号为中文即可
		str = str.replaceAll("—", "-").replaceAll("，", ",");
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

	protected CwbOrderDTO getCwbOrderAccordingtoConf(ExcelColumnSet excelColumnSet, Object row, long customerid, EmailDate ed, List<Branch> branchList, List<Common> commonList, long branchid,
			Customer customer, Map<String, Long> orderTypeIdMap, Map<String, ServiceArea> serviceAreaMap, Map<String, Long> payWayIdMap, Map<String, Long> customWareHouseIdMap) throws Exception {
		CwbOrderDTO cwbOrder = new CwbOrderDTO();

		if (excelColumnSet.getCwbordertypeindex() != 0) {
			String orderTypeString = getXRowCellData(row, excelColumnSet.getCwbordertypeindex());
			if (orderTypeString != null && !"".equals(orderTypeString.trim())) {
				Long orderTypeIdLong = orderTypeIdMap.get(orderTypeString);
				long orderTypeId = 0l;
				if (orderTypeIdLong == null) {
					orderTypeId = CwbOrderTypeIdEnum.Peisong.getValue();
				} else {
					orderTypeId = orderTypeIdLong;
				}
				cwbOrder.setCwbordertypeid(orderTypeId);
			} else {
				cwbOrder.guessCwbordertypeid();
			}
		} else {
			cwbOrder.guessCwbordertypeid();
		}

		if (excelColumnSet.getSendcargonumindex() != 0) {
			cwbOrder.setSendcargonum(getXRowCellData(row, excelColumnSet.getSendcargonumindex()));
		}
		if (excelColumnSet.getBackcargonumindex() != 0) {
			cwbOrder.setBackcargonum(getXRowCellData(row, excelColumnSet.getBackcargonumindex()));
		}

		if (customer.getIsAutoProductcwb() == 1) {
			// 订单号
			if (excelColumnSet.getCwbindex() == 0 || (excelColumnSet.getCwbindex() != 0 && getXRowCellData(row, excelColumnSet.getCwbindex()).length() == 0)) {
				cwbOrder.setCwb(customer.getAutoProductcwbpre() + System.currentTimeMillis());
			} else {
				cwbOrder.setCwb(getXRowCellData(row, excelColumnSet.getCwbindex()));
			}
		} else {
			// 订单号
			if (excelColumnSet.getCwbindex() != 0) {
				cwbOrder.setCwb(getXRowCellData(row, excelColumnSet.getCwbindex()));
			}

		}

		if (customer.getIsAutoProductcwb() == 1 && customer.getIsUsetranscwb() == 0 && customer.getIsypdjusetranscwb() == 1) {
			// 运单号
			if (excelColumnSet.getTranscwbindex() == 0 || (excelColumnSet.getTranscwbindex() != 0 && getXRowCellData(row, excelColumnSet.getTranscwbindex()).length() == 0)) {
				String transcwb = "";
				if (cwbOrder.getCwbordertypeid() != CwbOrderTypeIdEnum.Shangmentui.getValue()) {
					for (int i = 1; i < cwbOrder.getSendcargonum() + 1; i++) {
						if (customer.getAutoProductcwbpre().indexOf("[index]") > 1) {
							// 01，10 [0][index](如果小于10，那么就是01，02，之后是10）；1,10
							// [index]；01,010 0[index]
							String index = i < 10 ? (customer.getAutoProductcwbpre().indexOf("[0]") > -1 ? "0" + i : i + "") : i + "";
							String produceTransCwb = customer.getAutoProductcwbpre().replace("[0]", "").replace("[sendcarnum]", cwbOrder.getSendcargonum() + "").replace("[cwb]", cwbOrder.getCwb())
									.replace("[index]", index);
							transcwb += produceTransCwb + ",";
						} else {
							transcwb += cwbOrder.getCwb() + "-" + i + ",";
						}
					}
				}
				if (transcwb.length() > 0) {
					transcwb = transcwb.substring(0, transcwb.length() - 1);
				}
				cwbOrder.setTranscwb(transcwb);
			} else {
				cwbOrder.setTranscwb(getXRowCellData(row, excelColumnSet.getTranscwbindex()));
			}
		} else {
			// 运单号
			if (excelColumnSet.getTranscwbindex() != 0) {
				cwbOrder.setTranscwb(getXRowCellData(row, excelColumnSet.getTranscwbindex()));
			}
		}

		// 结算区域
		if (excelColumnSet.getAccountareaindex() != 0) {
			String serviceAreaName = getXRowCellData(row, excelColumnSet.getAccountareaindex());
			ServiceArea serviceArea = serviceAreaMap.get(serviceAreaName);
			if (serviceArea == null) {
				ServiceArea newServiceArea = new ServiceArea();
				try {
					newServiceArea.setCustomerid(customerid);
					newServiceArea.setServiceareaname(serviceAreaName);
					long serviceAreaid = serviceAreaDAO.reateServiceArea(newServiceArea);
					newServiceArea.setServiceareaid(serviceAreaid);
					serviceAreaMap.put(serviceAreaName, serviceArea);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				serviceArea = serviceAreaMap.get(serviceAreaName);
			}
			cwbOrder.setServiceareaid(serviceArea.getServiceareaid());
		} else {
			cwbOrder.setServiceareaid(ed.getAreaid());
		}
		/**
		 * 派送区域 20120213
		 */

		if (excelColumnSet.getConsigneenameindex() != 0) {
			cwbOrder.setConsigneename(getXRowCellData(row, excelColumnSet.getConsigneenameindex()));
		}

		String provinceString = "", cityString = "", countyString = "";
		if (excelColumnSet.getCwbprovinceindex() != 0) {
			provinceString = getXRowCellData(row, excelColumnSet.getCwbprovinceindex(), true);
		}
		if (excelColumnSet.getCwbcityindex() != 0) {
			cityString = getXRowCellData(row, excelColumnSet.getCwbcityindex(), true);
		}
		if (excelColumnSet.getCwbcountyindex() != 0) {
			countyString = getXRowCellData(row, excelColumnSet.getCwbcountyindex(), true);
		}
		if (excelColumnSet.getConsigneeaddressindex() != 0) {
			String addressString = getXRowCellData(row, excelColumnSet.getConsigneeaddressindex(), true);
			StringBuilder addressPrefix = new StringBuilder();
			if (excelColumnSet.getCwbprovinceindex() != 0 && addressString.indexOf(provinceString) == -1) {
				addressPrefix.append(provinceString);
				if (excelColumnSet.getCwbcityindex() != 0 && addressString.indexOf(cityString) == -1) {
					addressPrefix.append(" ").append(cityString);
					if (excelColumnSet.getCwbcountyindex() != 0 && addressString.indexOf(countyString) == -1) {
						addressPrefix.append(" ").append(countyString);
					}
				}
			}
			cwbOrder.setConsigneeaddress(addressPrefix.append(addressString).toString());
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
			cwbOrder.setEmaildate(getXRowCellDateData(row, excelColumnSet.getEmaildateindex()));
		} else {
			cwbOrder.setEmaildate(ed.getEmaildatetime());
			// cwbOrder.setEmaildateid(ed.getEmaildateid());
		}

		if (excelColumnSet.getConsigneenoindex() != 0) {
			cwbOrder.setConsigneeno(getXRowCellData(row, excelColumnSet.getConsigneenoindex()));
		}
		if (excelColumnSet.getExcelbranchindex() != 0) {
			cwbOrder.setExcelbranch(getXRowCellData(row, excelColumnSet.getExcelbranchindex()));
			if (cwbOrder.getExcelbranch() != null && cwbOrder.getExcelbranch().length() > 0) {
				cwbOrder.setAddresscodeedittype(CwbOrderAddressCodeEditTypeEnum.RenGong.getValue());
			}

			for (Branch b : branchList) {
				if (b.getBranchname().equals(cwbOrder.getExcelbranch())) {
					cwbOrder.setDeliverybranchid(b.getBranchid());
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
		if (excelColumnSet.getCommonnumberindex() != 0) {
			for (Common c : commonList) {
				if (c.getCommonnumber().equalsIgnoreCase(getXRowCellData(row, excelColumnSet.getCommonnumberindex()))) {
					cwbOrder.setCommon(c);
					break;
				}
			}
		}

		if (cwbOrder.getCwbordertypeid() != CwbOrderTypeIdEnum.Shangmentui.getValue()) {
			cwbOrder.setPrinttime(DateTimeUtil.getNowTime());
		}

		if (excelColumnSet.getPaywayindex() != 0) {
			String payWayString = getXRowCellData(row, excelColumnSet.getPaywayindex());
			Long payWayIdLong = payWayIdMap.get(payWayString);
			long payWayId = 0L;
			if (payWayIdLong == null) {
				payWayId = PaytypeEnum.Xianjin.getValue();
			} else {
				payWayId = payWayIdLong;
			}
			cwbOrder.setPaywayid(payWayId);
			cwbOrder.setNewpaywayid(payWayId + "");
		} else {
			cwbOrder.guessPaywayid();
			cwbOrder.setNewpaywayid(PaytypeEnum.Xianjin.getValue() + "");
		}

		if (excelColumnSet.getCwbdelivertypeindex() != 0) {
			cwbOrder.setCwbdelivertypeid(getXRowCellData(row, excelColumnSet.getCwbdelivertypeindex()).equals("加急") ? 2 : 1);
		}
		if (excelColumnSet.getCwbprovinceindex() != 0) {
			cwbOrder.setCwbprovince(provinceString);
		}
		if (excelColumnSet.getCwbcityindex() != 0) {
			cwbOrder.setCwbcity(cityString);
		}
		if (excelColumnSet.getCwbcountyindex() != 0) {
			cwbOrder.setCwbcounty(countyString);
		}
		if (excelColumnSet.getConsigneemobileindex() != 0) {
			cwbOrder.setConsigneemobile(getXRowCellData(row, excelColumnSet.getConsigneemobileindex()));
		}

		if (excelColumnSet.getShipcwbindex() != 0) {
			cwbOrder.setShipcwb(getXRowCellData(row, excelColumnSet.getShipcwbindex()));
		}
		if (excelColumnSet.getWarehousenameindex() != 0) {
			String wareHouseNameString = getXRowCellData(row, excelColumnSet.getWarehousenameindex());
			Long warehouseId = customWareHouseIdMap.get(wareHouseNameString);
			if (warehouseId == null) {
				if (switchDAO.getSwitchBySwitchname(SwitchEnum.DaoRuShuJuChuangJianFaHuoCangKu.getText()).getState().equals(SwitchEnum.DaoRuShuJuChuangJianFaHuoCangKu.getInfo())) {
					warehouseId = customWareHouseDAO.creCustomerGetId(customerid, wareHouseNameString);
					customWareHouseIdMap.put(wareHouseNameString, warehouseId);
				} else {
					throw new RuntimeException("供货商仓库" + wareHouseNameString + "不存在，请创建此仓库");
				}
			}
			cwbOrder.setCustomerwarehouseid(warehouseId);
		} else {
			cwbOrder.setCustomerwarehouseid(ed.getWarehouseid());
		}

		cwbOrder.setStartbranchid(branchid);
		/*
		 * if (excelColumnSet.getOrdercwbindex() != 0) {
		 * cwbOrder.setOrdercwb(getXRowCellData(row,
		 * excelColumnSet.getOrdercwbindex())); }
		 * 
		 * if (excelColumnSet.getServiceareaindex() != 0) {
		 * cwbOrder.setPaisongArea( getXRowCellData(row,
		 * excelColumnSet.getServiceareaindex())); }
		 */
		if (excelColumnSet.getModelnameindex() != 0) {
			cwbOrder.setModelname(getXRowCellData(row, excelColumnSet.getModelnameindex()));
		}

		// 自定义字段赋值
		if (excelColumnSet.getRemark1index() != 0) {
			cwbOrder.setRemark1(getXRowCellData(row, excelColumnSet.getRemark1index()));
		} else {
			cwbOrder.setRemark1("");
		}
		if (excelColumnSet.getRemark2index() != 0) {
			cwbOrder.setRemark2(getXRowCellData(row, excelColumnSet.getRemark2index()));
		} else {
			cwbOrder.setRemark2("");
		}
		if (excelColumnSet.getRemark3index() != 0) {
			cwbOrder.setRemark3(getXRowCellData(row, excelColumnSet.getRemark3index()));
		} else {
			cwbOrder.setRemark3("");
		}
		if (excelColumnSet.getRemark4index() != 0) {
			cwbOrder.setRemark4(getXRowCellData(row, excelColumnSet.getRemark4index()));
		} else {
			cwbOrder.setRemark4("");
		}
		if (excelColumnSet.getRemark5index() != 0) {
			cwbOrder.setRemark5(getXRowCellData(row, excelColumnSet.getRemark5index()));
		} else {
			cwbOrder.setRemark5("");
		}

		if (excelColumnSet.getShouldfareindex() != 0) {
			cwbOrder.setShouldfare(getXRowCellData(row, excelColumnSet.getShouldfareindex()));
		} else {
			cwbOrder.setShouldfare(BigDecimal.ZERO);
		}

		cwbOrder.setDefaultCargoName();

		return cwbOrder;
	}

	public void extract(InputStream f, long customerId, ResultCollector errorCollector, long branchId, EmailDate ed, ExplinkUserDetail userDetail, boolean isRetry) {
		ExcelColumnSet excelColumnSet = setExcelColumnDAO.getExcelColumnSetByCustomerid(customerId);
		List<Branch> branchList = branchDAO.getAllEffectBranches();// 获取所有branch记录，用于匹配下一站id
		List<Common> commonList = commonDAO.getAllCommons();
		Customer customer = customerDAO.getCustomerById(customerId);
		List<CwbOrderDTO> cwbOrders = new ArrayList<CwbOrderDTO>();
		List<CwbOrderValidator> vailidators = importValidationManager.getVailidators(excelColumnSet);

		// prepare global values
		// 订单类型
		Map<String, Long> orderTypeIdMap = cwbOrderTypeDAO.getOrderTypeMappings();
		// 服务区域
		Map<String, ServiceArea> serviceAreaMap = serviceAreaDAO.getAllServiceAreaNames();
		// 支付方式
		Map<String, Long> payWayIdMap = payWayDao.getPayWayIdMapping();
		// 发货仓库
		Map<String, Long> customWareHouseIdMap = customWareHouseDAO.getCustomWareHouseIdMapByCustomerid(customerId);

		int count = 0;

		for (Object row : getRows(f)) {
			count++;
			if (errorCollector.isStoped()) {
				// errorCollector.setStoped(false);
				break;
			}
			try {
				CwbOrderDTO cwbOrder = getCwbOrderAccordingtoConf(excelColumnSet, row, customerId, ed, branchList, commonList, branchId, customer, orderTypeIdMap, serviceAreaMap, payWayIdMap,
						customWareHouseIdMap);
				for (CwbOrderValidator cwbOrderValidator : vailidators) {
					cwbOrderValidator.validate(cwbOrder);
				}
				cwbOrders.add(cwbOrder);
				if (count % 100 == 0) {
					logger.info("parsed {} orders", count);
					dataImportService.importData(cwbOrders, userDetail.getUser(), errorCollector, ed, isRetry);
					cwbOrders.clear();
				}
			} catch (Exception e) {
				e.printStackTrace();
				String cwb = "";
				if (excelColumnSet.getCwbindex() != 0) {
					cwb = getXRowCellData(row, excelColumnSet.getCwbindex());
				}
				errorCollector.addError(cwb, e.getMessage());

				// 失败订单数+1 前台显示
				errorCollector.setFailSavcNum(errorCollector.getFailSavcNum() + 1);

				// 存储报错订单，以便统计错误记录和处理错误订单
				JSONObject errorOrder = new JSONObject();
				errorOrder.put("cwbOrderDTO", "{\"cwb\":\"" + removeZero(cwb) + "\",\"emaildate\":\"" + ed.getEmaildatetime() + "\"}");
				errorOrder.put("emaildateid", ed.getEmaildateid());
				errorOrder.put("customerid", customerId);
				errorOrder.put("message", e.getMessage());

				importCwbErrorService.saveOrderError(errorOrder);
			}
		}

		if (cwbOrders.size() > 0) {
			dataImportService.importData(cwbOrders, userDetail.getUser(), errorCollector, ed, isRetry);
		}

	}

	private String removeZero(String cwb) {
		int end = cwb.indexOf(".0");
		if (end > -1) {
			cwb = cwb.substring(0, end);
		}
		return cwb;
	}

	protected abstract String getXRowCellData(Object row, int cwbindex);

	protected abstract String getXRowCellData(Object row, int cwbindex, boolean escapeAddress);

	protected abstract String getXRowCellDateData(Object row, int cwbindex);

	protected abstract List<Object> getRows(InputStream f);

}