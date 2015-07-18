package cn.explink.service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.AbnormalOrderDAO;
import cn.explink.dao.AbnormalTypeDAO;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CommonDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.CwbOrderTypeDAO;
import cn.explink.dao.PayWayDao;
import cn.explink.dao.PenalizeOutDAO;
import cn.explink.dao.PenalizeOutImportErrorRecordDAO;
import cn.explink.dao.PenalizeOutImportRecordDAO;
import cn.explink.dao.PenalizeTypeDAO;
import cn.explink.dao.PunishDAO;
import cn.explink.dao.PunishInsideDao;
import cn.explink.dao.PunishTypeDAO;
import cn.explink.dao.SalaryErrorDAO;
import cn.explink.dao.SalaryFixedDAO;
import cn.explink.dao.SalaryGatherDao;
import cn.explink.dao.SalaryImportDao;
import cn.explink.dao.SalaryImportRecordDAO;
import cn.explink.dao.ServiceAreaDAO;
import cn.explink.dao.SetExcelColumnDAO;
import cn.explink.dao.SwitchDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.AbnormalImportView;
import cn.explink.domain.AbnormalOrder;
import cn.explink.domain.AbnormalType;
import cn.explink.domain.Branch;
import cn.explink.domain.Common;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.EmailDate;
import cn.explink.domain.ExcelColumnSet;
import cn.explink.domain.ImportValidationManager;
import cn.explink.domain.PenalizeInside;
import cn.explink.domain.PenalizeOut;
import cn.explink.domain.PenalizeOutImportRecord;
import cn.explink.domain.PenalizeType;
import cn.explink.domain.Punish;
import cn.explink.domain.PunishType;
import cn.explink.domain.SalaryFixed;
import cn.explink.domain.SalaryGather;
import cn.explink.domain.SalaryImport;
import cn.explink.domain.SalaryImportRecord;
import cn.explink.domain.ServiceArea;
import cn.explink.domain.User;
import cn.explink.enumutil.AbnormalOrderHandleEnum;
import cn.explink.enumutil.AbnormalWriteBackEnum;
import cn.explink.enumutil.CwbOrderAddressCodeEditTypeEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.enumutil.PenalizeSateEnum;
import cn.explink.enumutil.PunishInsideStateEnum;
import cn.explink.enumutil.PunishlevelEnum;
import cn.explink.enumutil.PunishtimeEnum;
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
	UserDAO userDAO;
	@Autowired
	PunishTypeDAO punishTypeDAO;
	@Autowired
	PunishDAO punishDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	PenalizeTypeDAO penalizeTypeDAO;
	@Autowired
	PenalizeOutDAO penalizeOutDAO;
	@Autowired
	PunishInsideDao punishInsideDao;
	@Autowired
	PenalizeOutImportErrorRecordDAO penalizeOutImportErrorRecordDAO;
	@Autowired
	PenalizeOutImportRecordDAO penalizeOutImportRecordDAO;
	@Autowired
	SalaryFixedDAO salaryFixedDAO;
	@Autowired
	SalaryErrorDAO salaryErrorDAO;
	@Autowired
	ImportCwbErrorService importCwbErrorService;
	@Autowired
	SalaryImportDao salaryImportDao;

	@Autowired
	DataImportService dataImportService;

	@Autowired
	SalaryImportRecordDAO salaryImportRecordDAO;

	@Autowired
	AbnormalTypeDAO abnormalTypeDAO;
	@Autowired
	AbnormalOrderDAO abnormalOrderDAO;
	@Autowired
	AbnormalService abnormalService;
	@Autowired
	SalaryGatherDao salaryGatherDao;


	public String strtovalid(String str) {
		// 2013-8-6 需求： 收件人地址让显示中文符号。最终是替换所有的中文符号为中文即可
		str = str.replaceAll("—", "-").replaceAll("，", ",");
		String cwb = "";
		for (int i = 0; i < str.length(); i++) {
			int asc = str.charAt(i);
			if (((asc >= 32) && (asc <= 127)) || // 英文字符，标点符号，数字
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
			String orderTypeString = this.getXRowCellData(row, excelColumnSet.getCwbordertypeindex());
			if ((orderTypeString != null) && !"".equals(orderTypeString.trim())) {
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
			cwbOrder.setSendcargonum(this.getXRowCellData(row, excelColumnSet.getSendcargonumindex()));
		}
		if (excelColumnSet.getBackcargonumindex() != 0) {
			cwbOrder.setBackcargonum(this.getXRowCellData(row, excelColumnSet.getBackcargonumindex()));
		}

		if (customer.getIsAutoProductcwb() == 1) {
			// 订单号
			if ((excelColumnSet.getCwbindex() == 0) || ((excelColumnSet.getCwbindex() != 0) && (this.getXRowCellData(row, excelColumnSet.getCwbindex()).length() == 0))) {
				cwbOrder.setCwb(customer.getAutoProductcwbpre() + System.currentTimeMillis());
			} else {
				cwbOrder.setCwb(this.getXRowCellData(row, excelColumnSet.getCwbindex()));
			}
		} else {
			// 订单号
			if (excelColumnSet.getCwbindex() != 0) {
				cwbOrder.setCwb(this.getXRowCellData(row, excelColumnSet.getCwbindex()));
			}

		}

		if ((customer.getIsAutoProductcwb() == 1) && (customer.getIsUsetranscwb() == 0) && (customer.getIsypdjusetranscwb() == 1)) {
			// 运单号
			if ((excelColumnSet.getTranscwbindex() == 0) || ((excelColumnSet.getTranscwbindex() != 0) && (this.getXRowCellData(row, excelColumnSet.getTranscwbindex()).length() == 0))) {
				String transcwb = "";
				if (cwbOrder.getCwbordertypeid() != CwbOrderTypeIdEnum.Shangmentui.getValue()) {
					for (int i = 1; i < (cwbOrder.getSendcargonum() + 1); i++) {
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
				cwbOrder.setTranscwb(this.getXRowCellData(row, excelColumnSet.getTranscwbindex()));
			}
		} else {
			// 运单号
			if (excelColumnSet.getTranscwbindex() != 0) {
				cwbOrder.setTranscwb(this.getXRowCellData(row, excelColumnSet.getTranscwbindex()));
			}
		}

		// 结算区域
		if (excelColumnSet.getAccountareaindex() != 0) {
			String serviceAreaName = this.getXRowCellData(row, excelColumnSet.getAccountareaindex());
			ServiceArea serviceArea = serviceAreaMap.get(serviceAreaName);
			if (serviceArea == null) {
				ServiceArea newServiceArea = new ServiceArea();
				try {
					newServiceArea.setCustomerid(customerid);
					newServiceArea.setServiceareaname(serviceAreaName);
					long serviceAreaid = this.serviceAreaDAO.reateServiceArea(newServiceArea);
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
			cwbOrder.setConsigneename(this.getXRowCellData(row, excelColumnSet.getConsigneenameindex()));
		}

		String provinceString = "", cityString = "", countyString = "";
		if (excelColumnSet.getCwbprovinceindex() != 0) {
			provinceString = this.getXRowCellData(row, excelColumnSet.getCwbprovinceindex(), true);
		}
		if (excelColumnSet.getCwbcityindex() != 0) {
			cityString = this.getXRowCellData(row, excelColumnSet.getCwbcityindex(), true);
		}
		if (excelColumnSet.getCwbcountyindex() != 0) {
			countyString = this.getXRowCellData(row, excelColumnSet.getCwbcountyindex(), true);
		}
		if (excelColumnSet.getConsigneeaddressindex() != 0) {
			String addressString = this.getXRowCellData(row, excelColumnSet.getConsigneeaddressindex(), true);
			StringBuilder addressPrefix = new StringBuilder();
			if ((excelColumnSet.getCwbprovinceindex() != 0) && (addressString.indexOf(provinceString) == -1)) {
				addressPrefix.append(provinceString);
				if ((excelColumnSet.getCwbcityindex() != 0) && (addressString.indexOf(cityString) == -1)) {
					addressPrefix.append(" ").append(cityString);
					if ((excelColumnSet.getCwbcountyindex() != 0) && (addressString.indexOf(countyString) == -1)) {
						addressPrefix.append(" ").append(countyString);
					}
				}
			}
			cwbOrder.setConsigneeaddress(addressPrefix.append(addressString).toString());
		}
		if (excelColumnSet.getConsigneepostcodeindex() != 0) {
			cwbOrder.setConsigneepostcode(this.getXRowCellData(row, excelColumnSet.getConsigneepostcodeindex()));
		}
		if (excelColumnSet.getConsigneephoneindex() != 0) {
			cwbOrder.setConsigneephone(this.getXRowCellData(row, excelColumnSet.getConsigneephoneindex()), excelColumnSet.getGetmobileflag() == 1);
		}
		if (excelColumnSet.getSendcargonameindex() != 0) {
			cwbOrder.setSendcargoname(this.getXRowCellData(row, excelColumnSet.getSendcargonameindex()));
		}
		if (excelColumnSet.getBackcargonameindex() != 0) {
			cwbOrder.setBackcargoname(this.getXRowCellData(row, excelColumnSet.getBackcargonameindex()));
		}
		if (excelColumnSet.getReceivablefeeindex() != 0) {
			cwbOrder.setReceivablefee(this.getXRowCellData(row, excelColumnSet.getReceivablefeeindex()));
		}
		if (excelColumnSet.getPaybackfeeindex() != 0) {
			cwbOrder.setPaybackfee(this.getXRowCellData(row, excelColumnSet.getPaybackfeeindex()));
		}
		if (excelColumnSet.getCargorealweightindex() != 0) {
			cwbOrder.setCargorealweight(this.getXRowCellData(row, excelColumnSet.getCargorealweightindex()));
		}
		if (excelColumnSet.getCwbremarkindex() != 0) {
			cwbOrder.setCwbremark(this.getXRowCellData(row, excelColumnSet.getCwbremarkindex()));
		}

		if (excelColumnSet.getEmaildateindex() != 0) {
			cwbOrder.setEmaildate(this.getXRowCellDateData(row, excelColumnSet.getEmaildateindex()));
		} else {
			cwbOrder.setEmaildate(ed.getEmaildatetime());
			// cwbOrder.setEmaildateid(ed.getEmaildateid());
		}

		if (excelColumnSet.getConsigneenoindex() != 0) {
			cwbOrder.setConsigneeno(this.getXRowCellData(row, excelColumnSet.getConsigneenoindex()));
		}
		if (excelColumnSet.getExcelbranchindex() != 0) {
			cwbOrder.setExcelbranch(this.getXRowCellData(row, excelColumnSet.getExcelbranchindex()));
			if ((cwbOrder.getExcelbranch() != null) && (cwbOrder.getExcelbranch().length() > 0)) {
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
			cwbOrder.setExceldeliver(this.getXRowCellData(row, excelColumnSet.getExceldeliverindex()));
		}
		if (excelColumnSet.getCargoamountindex() != 0) {
			cwbOrder.setCargoamount(this.getXRowCellData(row, excelColumnSet.getCargoamountindex()));
		}
		if (excelColumnSet.getCustomercommandindex() != 0) {
			cwbOrder.setCustomercommand(this.getXRowCellData(row, excelColumnSet.getCustomercommandindex()));
		}
		if (excelColumnSet.getCargotypeindex() != 0) {
			cwbOrder.setCargotype(this.getXRowCellData(row, excelColumnSet.getCargotypeindex()));
		}
		if (excelColumnSet.getCargosizeindex() != 0) {
			cwbOrder.setCargosize(this.getXRowCellData(row, excelColumnSet.getCargosizeindex()));
		}
		if (excelColumnSet.getBackcargoamountindex() != 0) {
			cwbOrder.setBackcargoamount(this.getXRowCellData(row, excelColumnSet.getBackcargoamountindex()));
		}
		if (excelColumnSet.getDestinationindex() != 0) {
			cwbOrder.setDestination(this.getXRowCellData(row, excelColumnSet.getDestinationindex()));
		}
		if (excelColumnSet.getTranswayindex() != 0) {
			cwbOrder.setTransway(this.getXRowCellData(row, excelColumnSet.getTranswayindex()));
		}
		if (excelColumnSet.getCommonnumberindex() != 0) {
			for (Common c : commonList) {
				if (c.getCommonnumber().equalsIgnoreCase(this.getXRowCellData(row, excelColumnSet.getCommonnumberindex()))) {
					cwbOrder.setCommon(c);
					break;
				}
			}
		}

		if (cwbOrder.getCwbordertypeid() != CwbOrderTypeIdEnum.Shangmentui.getValue()) {
			cwbOrder.setPrinttime(DateTimeUtil.getNowTime());
		}

		if (excelColumnSet.getPaywayindex() != 0) {
			String payWayString = this.getXRowCellData(row, excelColumnSet.getPaywayindex());
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
			cwbOrder.setCwbdelivertypeid(this.getXRowCellData(row, excelColumnSet.getCwbdelivertypeindex()).equals("加急") ? 2 : 1);
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
			cwbOrder.setConsigneemobile(this.getXRowCellData(row, excelColumnSet.getConsigneemobileindex()));
		}

		if (excelColumnSet.getShipcwbindex() != 0) {
			cwbOrder.setShipcwb(this.getXRowCellData(row, excelColumnSet.getShipcwbindex()));
		}
		if (excelColumnSet.getWarehousenameindex() != 0) {
			String wareHouseNameString = this.getXRowCellData(row, excelColumnSet.getWarehousenameindex());
			Long warehouseId = customWareHouseIdMap.get(wareHouseNameString);
			if (warehouseId == null) {
				if (this.switchDAO.getSwitchBySwitchname(SwitchEnum.DaoRuShuJuChuangJianFaHuoCangKu.getText()).getState().equals(SwitchEnum.DaoRuShuJuChuangJianFaHuoCangKu.getInfo())) {
					warehouseId = this.customWareHouseDAO.creCustomerGetId(customerid, wareHouseNameString);
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
			cwbOrder.setModelname(this.getXRowCellData(row, excelColumnSet.getModelnameindex()));
		}

		// 自定义字段赋值
		if (excelColumnSet.getRemark1index() != 0) {
			cwbOrder.setRemark1(this.getXRowCellData(row, excelColumnSet.getRemark1index()));
		} else {
			cwbOrder.setRemark1("");
		}
		if (excelColumnSet.getRemark2index() != 0) {
			cwbOrder.setRemark2(this.getXRowCellData(row, excelColumnSet.getRemark2index()));
		} else {
			cwbOrder.setRemark2("");
		}
		if (excelColumnSet.getRemark3index() != 0) {
			cwbOrder.setRemark3(this.getXRowCellData(row, excelColumnSet.getRemark3index()));
		} else {
			cwbOrder.setRemark3("");
		}
		if (excelColumnSet.getRemark4index() != 0) {
			cwbOrder.setRemark4(this.getXRowCellData(row, excelColumnSet.getRemark4index()));
		} else {
			cwbOrder.setRemark4("");
		}
		if (excelColumnSet.getRemark5index() != 0) {
			cwbOrder.setRemark5(this.getXRowCellData(row, excelColumnSet.getRemark5index()));
		} else {
			cwbOrder.setRemark5("");
		}

		if (excelColumnSet.getShouldfareindex() != 0) {
			cwbOrder.setShouldfare(this.getXRowCellData(row, excelColumnSet.getShouldfareindex()));
		} else {
			cwbOrder.setShouldfare(BigDecimal.ZERO);
		}

		cwbOrder.setDefaultCargoName();

		return cwbOrder;
	}

	public void extract(InputStream f, long customerId, ResultCollector errorCollector, long branchId, EmailDate ed, ExplinkUserDetail userDetail, boolean isRetry) {
		ExcelColumnSet excelColumnSet = this.setExcelColumnDAO.getExcelColumnSetByCustomerid(customerId);
		List<Branch> branchList = this.branchDAO.getAllEffectBranches();// 获取所有branch记录，用于匹配下一站id
		List<Common> commonList = this.commonDAO.getAllCommons();
		Customer customer = this.customerDAO.getCustomerById(customerId);
		List<CwbOrderDTO> cwbOrders = new ArrayList<CwbOrderDTO>();
		List<CwbOrderValidator> vailidators = this.importValidationManager.getVailidators(excelColumnSet);

		// prepare global values
		// 订单类型
		Map<String, Long> orderTypeIdMap = this.cwbOrderTypeDAO.getOrderTypeMappings();
		// 服务区域
		Map<String, ServiceArea> serviceAreaMap = this.serviceAreaDAO.getAllServiceAreaNames();
		// 支付方式
		Map<String, Long> payWayIdMap = this.payWayDao.getPayWayIdMapping();
		// 发货仓库
		Map<String, Long> customWareHouseIdMap = this.customWareHouseDAO.getCustomWareHouseIdMapByCustomerid(customerId);

		int count = 0;

		for (Object row : this.getRows(f)) {
			count++;
			if (errorCollector.isStoped()) {
				// errorCollector.setStoped(false);
				break;
			}
			try {
				CwbOrderDTO cwbOrder = this.getCwbOrderAccordingtoConf(excelColumnSet, row, customerId, ed, branchList, commonList, branchId, customer, orderTypeIdMap, serviceAreaMap, payWayIdMap,
						customWareHouseIdMap);
				for (CwbOrderValidator cwbOrderValidator : vailidators) {
					cwbOrderValidator.validate(cwbOrder);
				}
				cwbOrders.add(cwbOrder);
				if ((count % 100) == 0) {
					ExcelExtractor.logger.info("parsed {} orders", count);
					this.dataImportService.importData(cwbOrders, userDetail.getUser(), errorCollector, ed, isRetry);
					cwbOrders.clear();
				}
			} catch (Exception e) {
				e.printStackTrace();
				String cwb = "";
				if (excelColumnSet.getCwbindex() != 0) {
					cwb = this.getXRowCellData(row, excelColumnSet.getCwbindex());
				}
				errorCollector.addError(cwb, e.getMessage());

				// 失败订单数+1 前台显示
				errorCollector.setFailSavcNum(errorCollector.getFailSavcNum() + 1);

				// 存储报错订单，以便统计错误记录和处理错误订单
				JSONObject errorOrder = new JSONObject();
				errorOrder.put("cwbOrderDTO", "{\"cwb\":\"" + this.removeZero(cwb) + "\",\"emaildate\":\"" + ed.getEmaildatetime() + "\"}");
				errorOrder.put("emaildateid", ed.getEmaildateid());
				errorOrder.put("customerid", customerId);
				errorOrder.put("message", e.getMessage());

				this.importCwbErrorService.saveOrderError(errorOrder);
			}
		}

		if (cwbOrders.size() > 0) {
			this.dataImportService.importData(cwbOrders, userDetail.getUser(), errorCollector, ed, isRetry);
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

	public int extract(InputStream f) {
		List<Branch> branchList = this.branchDAO.getAllEffectBranches();// 获取所有branch记录，用于匹配下一站id
		List<User> userList = this.userDAO.getAllUser();
		List<PunishType> punishTypeList = this.punishTypeDAO.getAllPunishTypeByName();
		List<Punish> punisList = new ArrayList<Punish>();
		List<Customer> customers = this.customerDAO.getAllCustomers();

		Map<String, PunishType> punishTypeMap = this.SetMapPunishType(punishTypeList);
		Map<String, Branch> branchMap = this.SetMapBranch(branchList);
		Map<String, User> userMap = this.SetMapUser(userList);
		Map<String, Customer> customerMap = this.SetMapCustomer(customers);

		int count = 0;
		for (Object row : this.getRows(f)) {
			try {
				Punish punish = this.getPunishAccordingtoConf(row, userMap, branchMap, punishTypeMap, customerMap);

				punisList.add(punish);
			} catch (Exception e) {
				e.printStackTrace();

				// 失败订单数+1 前台显示

				// 存储报错订单，以便统计错误记录和处理错误订单

			}
		}
		for (Punish p : punisList) {
			try {
				count += this.punishDAO.importPunish(p);
			} catch (Exception e) {
				ExcelExtractor.logger.error("扣罚信息导入异常:" + e);
			}

		}
		return count;
	}

	private Map<String, PunishType> SetMapPunishType(List<PunishType> punishTypeList) {
		Map<String, PunishType> map = new HashMap<String, PunishType>();
		for (PunishType p : punishTypeList) {
			map.put(p.getName(), p);
		}
		return map;
	}

	private Map<String, Branch> SetMapBranch(List<Branch> branchList) {
		Map<String, Branch> map = new HashMap<String, Branch>();
		for (Branch b : branchList) {
			map.put(b.getBranchname(), b);
		}
		return map;
	}

	private Map<String, User> SetMapUser(List<User> userList) {
		Map<String, User> map = new HashMap<String, User>();
		for (User u : userList) {
			map.put(u.getRealname(), u);
		}
		return map;
	}

	private Map<String, Customer> SetMapCustomer(List<Customer> customers) {
		Map<String, Customer> map = new HashMap<String, Customer>();
		for (Customer cu : customers) {
			map.put(cu.getCustomername(), cu);
		}
		return map;
	}

	private Punish getPunishAccordingtoConf(Object row, Map<String, User> userMap, Map<String, Branch> branchMap, Map<String, PunishType> punishTypeMap, Map<String, Customer> customerMap) {

		Punish punish = new Punish();
		String cwb = this.getXRowCellData(row, 1);
		CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);
		if (co == null) {
			return null;
		}
		/*
		 * Punish p = this.punishDAO.getPunishByCwb(cwb); if (p != null) {
		 * return null; }
		 */
		punish.setCwb(cwb);
		Customer customer = customerMap.get(this.getXRowCellData(row, 2));
		customer = customer == null ? new Customer() : customer;
		punish.setCustomerid(customer.getCustomerid());

		PunishType pType = punishTypeMap.get(this.getXRowCellData(row, 3));
		pType = pType == null ? new PunishType() : pType;
		punish.setPunishid(pType.getId());

		Branch branch = branchMap.get(this.getXRowCellData(row, 4));
		branch = branch == null ? new Branch() : branch;
		punish.setBranchid(branch.getBranchid());

		User user = userMap.get(this.getXRowCellData(row, 5));
		user = user == null ? new User() : user;
		punish.setUserid(user.getUserid());

		try {
			String punishtime = this.getXRowCellData(row, 6);
			if (punishtime.contains(".")) {
				punish.setPunishtime((int) (Double.parseDouble(punishtime)));
			} else {
				punish.setPunishtime(PunishtimeEnum.getText(punishtime).getValue());
			}
		} catch (Exception e) {
			punish.setPunishlevel(0);
		}
		try {
			punish.setPunishlevel(PunishlevelEnum.getText(this.getXRowCellData(row, 7)).getValue());
		} catch (Exception e) {
			punish.setPunishlevel(0);
		}
		try {
			punish.setPunishfee(new BigDecimal(this.getXRowCellData(row, 8)));
		} catch (Exception e) {
			punish.setPunishfee(new BigDecimal("0.00"));
		}
		try {
			punish.setRealfee(new BigDecimal(this.getXRowCellData(row, 9)));
		} catch (Exception e) {
			punish.setRealfee(new BigDecimal("0.00"));
		}
		punish.setPunishcontent(this.getXRowCellData(row, 10));
		User createuser = userMap.get(this.getXRowCellData(row, 11));
		createuser = createuser == null ? new User() : createuser;
		punish.setCreateuser(createuser.getUserid());
		try {
			punish.setCreatetime(this.getXRowCellDateData(row, 12));
		} catch (Exception e) {
			punish.setCreatetime(this.getXRowCellData(row, 12));
		}
		if (punish.getCreatetime().equals("")) {
			punish.setCreatetime(this.getXRowCellData(row, 12));
		}
		ExcelExtractor.logger.info("扣罚登记_导入数据cwb={}--创建日期={}", cwb, punish.getCreatetime());

		punish.setState(this.getXRowCellData(row, 13).equals("已审核") ? 1 : 0);
		return punish;
	}

	public void extractPenalizeOut(InputStream f, User user, Long systemTime) {
		List<PenalizeType> penalizeTypelList = this.penalizeTypeDAO.getPenalizeTypeByType(2);

		Map<String, Integer> penalizeTypeMap = this.SetMapPenalizeTypeMap(penalizeTypelList);

		List<PenalizeOut> penalizeOuts = new ArrayList<PenalizeOut>();
		int successCounts = 0;
		int failCounts = 0;
		int totalCounts = 0;// this.getRows(f).size();
		PenalizeOutImportRecord record = new PenalizeOutImportRecord();
		for (Object row : this.getRows(f)) {
			totalCounts++;
			try {
				PenalizeOut out = this.getPenalizeOutAccordingtoConf(row, penalizeTypeMap, user, systemTime);
				if (out != null) {
					penalizeOuts.add(out);
				} else {
					failCounts++;
				}
			} catch (Exception e) {
				// e.printStackTrace();
				failCounts++;
				ExcelExtractor.logger.info("对外扣罚导入异常：cwb={},message={}", this.getXRowCellData(row, 1), e.toString());
				this.penalizeOutImportErrorRecordDAO.crePenalizeOutImportErrorRecord(this.getXRowCellData(row, 1), systemTime, "未知异常");
				continue;
				// 失败订单数+1 前台显示

				// 存储报错订单，以便统计错误记录和处理错误订单

			}
		}
		for (PenalizeOut out : penalizeOuts) {
			try {
				int nums = this.penalizeOutDAO.crePenalizeOut(out);
				successCounts += nums;
			} catch (Exception e) {
				failCounts++;
				ExcelExtractor.logger.info("对外扣罚导入异常：cwb={},message={}", out.getCwb(), e.toString());
				this.penalizeOutImportErrorRecordDAO.crePenalizeOutImportErrorRecord(out.getCwb(), systemTime, "未知异常");
				continue;
			}

		}

		record.setImportFlag(systemTime);
		record.setUserid(user.getUserid());
		record.setTotalCounts(totalCounts);
		record.setSuccessCounts(successCounts);
		record.setFailCounts(failCounts);
		this.penalizeOutImportRecordDAO.crePenalizeOutImportRecord(record);
	}

	/**
	 * @param row
	 * @param userMap
	 * @param penalizeTypeMap
	 * @param customerMap
	 * @return
	 */
	private PenalizeOut getPenalizeOutAccordingtoConf(Object row, Map<String, Integer> penalizeTypeMap, User user, long systemTime) throws Exception {
		PenalizeOut out = new PenalizeOut();
		String cwb = this.getXRowCellData(row, 1);
		CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);
		if (co == null) {
			this.penalizeOutImportErrorRecordDAO.crePenalizeOutImportErrorRecord(cwb, systemTime, "订单号不存在！");
			return null;
		} else {
			out.setCwb(cwb);
		}
		BigDecimal penalizeOutfee = null;
		BigDecimal penalizeOutGoodsfee = null;
		BigDecimal penalizeOutOtherfee = null;
		try {

			penalizeOutGoodsfee = new BigDecimal(this.getXRowCellData(row, 2));
			if (penalizeOutGoodsfee.compareTo(new BigDecimal(0)) == -1) {
				this.penalizeOutImportErrorRecordDAO.crePenalizeOutImportErrorRecord(cwb, systemTime, "货物赔付金额必须大于0.00！");
				return null;
			}
			if (penalizeOutGoodsfee.compareTo(new BigDecimal(1000000000000000l)) == 1) {
				this.penalizeOutImportErrorRecordDAO.crePenalizeOutImportErrorRecord(cwb, systemTime, "货物赔付金额有误");
				return null;
			}
			/*
			 * if(penalizeOutfee.compareTo(co.getReceivablefee())>0){
			 * this.penalizeOutImportErrorRecordDAO
			 * .crePenalizeOutImportErrorRecord(cwb, systemTime,
			 * "赔付金额不能大于订单金额有误！"); return null; }
			 */
		} catch (Exception e) {
			this.penalizeOutImportErrorRecordDAO.crePenalizeOutImportErrorRecord(cwb, systemTime, "货物赔付金额有误！");
			return null;
		}
		try {

			penalizeOutOtherfee = new BigDecimal(this.getXRowCellData(row, 3));
			if (penalizeOutOtherfee.compareTo(new BigDecimal(0)) == -1) {
				this.penalizeOutImportErrorRecordDAO.crePenalizeOutImportErrorRecord(cwb, systemTime, "其它赔付金额必须大于0.00！");
				return null;
			}
			if (penalizeOutOtherfee.compareTo(new BigDecimal(1000000000000000l)) == 1) {
				this.penalizeOutImportErrorRecordDAO.crePenalizeOutImportErrorRecord(cwb, systemTime, "货物赔付金额有误");
				return null;
			}
			/*
			 * if(penalizeOutfee.compareTo(co.getReceivablefee())>0){
			 * this.penalizeOutImportErrorRecordDAO
			 * .crePenalizeOutImportErrorRecord(cwb, systemTime,
			 * "赔付金额不能大于订单金额有误！"); return null; }
			 */
		} catch (Exception e) {
			this.penalizeOutImportErrorRecordDAO.crePenalizeOutImportErrorRecord(cwb, systemTime, "其它赔付金额有误！");
			return null;
		}
		out.setPenalizeOutNO(System.currentTimeMillis());
		out.setPenalizeOutGoodsfee(penalizeOutGoodsfee);
		out.setPenalizeOutOtherfee(penalizeOutOtherfee);
		penalizeOutfee = penalizeOutOtherfee.add(penalizeOutGoodsfee);
		String penalizeOutsmallText = this.getXRowCellData(row, 4);
		int penalizeOutsmall = 0;
		try {
			penalizeOutsmall = penalizeTypeMap.get(penalizeOutsmallText);
		} catch (Exception e) {
			this.penalizeOutImportErrorRecordDAO.crePenalizeOutImportErrorRecord(cwb, systemTime, "赔付类型不存在！");
			return null;
		}

		PenalizeOut penalizeOut = this.penalizeOutDAO.getPenalizeOutByIsNull(cwb, penalizeOutsmall, penalizeOutGoodsfee, penalizeOutOtherfee);
		if (penalizeOut != null) {
			this.penalizeOutImportErrorRecordDAO.crePenalizeOutImportErrorRecord(cwb, systemTime, "该记录已经存在！");
			return null;
		}
		out.setCustomerid(co.getCustomerid());
		out.setFlowordertype(co.getFlowordertype());
		out.setCaramount(co.getCaramount());
		out.setPenalizeOutfee(penalizeOutfee);
		out.setCreateruser(user.getUserid());
		out.setPenalizeOutsmall(penalizeOutsmall);
		out.setPenalizeOutstate(PenalizeSateEnum.Successful.getValue());
		PenalizeType type = this.penalizeTypeDAO.getPenalizeTypeById(penalizeOutsmall);
		out.setPenalizeOutbig(type == null ? 0 : type.getParent());

		return out;
	}

	/**
	 * @param penalizeTypelList
	 * @return
	 */
	private Map<String, Integer> SetMapPenalizeTypeMap(List<PenalizeType> penalizeTypelList) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		if ((penalizeTypelList != null) && (penalizeTypelList.size() > 0)) {
			for (PenalizeType type : penalizeTypelList) {
				map.put(type.getText(), type.getId());
			}
		}
		return map;
	}

	/**
	 * 对内扣罚
	 *
	 * @param row
	 * @param userMap
	 * @param penalizeTypeMap
	 * @param customerMap
	 * @return
	 */
	private PenalizeInside getPenalizeInAccordingtoConf(Object row, Map<String, Integer> penalizeTypeMap, User user, long systemTime) throws Exception {
		PenalizeInside out = new PenalizeInside();
		String cwb = this.getXRowCellData(row, 1);
		CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);
		if (co == null) {
			this.penalizeOutImportErrorRecordDAO.crePenalizeOutImportErrorRecord(cwb, systemTime, "订单号不存在！");
			return null;
		} else {
			out.setCwb(cwb);
		}
		BigDecimal penalizeOutfee = null;
		BigDecimal penalizeOutGoodsfee = null;
		BigDecimal penalizeOutOtherfee = null;
		try {

			penalizeOutGoodsfee = new BigDecimal(this.getXRowCellData(row, 2));
			/*if (penalizeOutGoodsfee.compareTo(new BigDecimal(0)) == -1) {
				this.penalizeOutImportErrorRecordDAO.crePenalizeOutImportErrorRecord(cwb, systemTime, "货物扣罚金额必须大于0.00！");
				return null;
			}*/
			if ((penalizeOutGoodsfee.compareTo(new BigDecimal(1000000000000000l)) == 1)||(String.valueOf(penalizeOutGoodsfee).length()>14)) {
				this.penalizeOutImportErrorRecordDAO.crePenalizeOutImportErrorRecord(cwb, systemTime, "货物扣罚金额（超过14位长度）有误");
				return null;
			}
			/*
			 * if(penalizeOutfee.compareTo(co.getReceivablefee())>0){
			 * this.penalizeOutImportErrorRecordDAO
			 * .crePenalizeOutImportErrorRecord(cwb, systemTime,
			 * "赔付金额不能大于订单金额有误！"); return null; }
			 */
		} catch (Exception e) {
			this.penalizeOutImportErrorRecordDAO.crePenalizeOutImportErrorRecord(cwb, systemTime, "货物扣罚金额有误！");
			return null;
		}
		try {

			penalizeOutOtherfee = new BigDecimal(this.getXRowCellData(row, 3));
			/*if (penalizeOutOtherfee.compareTo(new BigDecimal(0)) == -1) {
				this.penalizeOutImportErrorRecordDAO.crePenalizeOutImportErrorRecord(cwb, systemTime, "其它扣罚金额必须大于0.00！");
				return null;
			}*/
			if ((penalizeOutOtherfee.compareTo(new BigDecimal(1000000000000000l)) == 1)||(String.valueOf(penalizeOutOtherfee).length()>14)) {
				this.penalizeOutImportErrorRecordDAO.crePenalizeOutImportErrorRecord(cwb, systemTime, "其它扣罚金额（超过14位长度）有误");
				return null;
			}
			/*
			 * if(penalizeOutfee.compareTo(co.getReceivablefee())>0){
			 * this.penalizeOutImportErrorRecordDAO
			 * .crePenalizeOutImportErrorRecord(cwb, systemTime,
			 * "赔付金额不能大于订单金额有误！"); return null; }
			 */
		} catch (Exception e) {
			this.penalizeOutImportErrorRecordDAO.crePenalizeOutImportErrorRecord(cwb, systemTime, "其它扣罚金额有误！");
			return null;
		}
		out.setCreategoodpunishprice(penalizeOutGoodsfee);
		out.setCreateqitapunishprice(penalizeOutOtherfee);
		penalizeOutfee = penalizeOutOtherfee.add(penalizeOutGoodsfee);
		String penalizeOutsmallText = this.getXRowCellData(row, 4);
		int penalizeOutsmall = 0;
		try {
			penalizeOutsmall = penalizeTypeMap.get(penalizeOutsmallText);
		} catch (Exception e) {
			this.penalizeOutImportErrorRecordDAO.crePenalizeOutImportErrorRecord(cwb, systemTime, "扣罚类型不存在！");
			return null;
		}
		String dutybranchname = this.getXRowCellData(row, 5);
		long branchid = 0;
		try {
			List<Branch> branchs = this.branchDAO.getBranchByBranchnameCheck(dutybranchname);
			if ((branchs == null) || (branchs.size() == 0)) {
				this.penalizeOutImportErrorRecordDAO.crePenalizeOutImportErrorRecord(cwb, systemTime, "该记录责任机构不存在！");
				return null;
			} else {
				branchid = branchs.get(0).getBranchid();
			}
		} catch (Exception e) {
			this.penalizeOutImportErrorRecordDAO.crePenalizeOutImportErrorRecord(cwb, systemTime, "该记录责任机构不存在！");
			return null;
		}
		String dutypersonname = this.getXRowCellData(row, 6);
		long dutypersonid = 0;
		if (dutypersonname.equals("")) {

		} else {
			try {
				User user2 = this.userDAO.getUserByRealname(dutypersonname);
				if (user2 == null) {
					// this.penalizeOutImportErrorRecordDAO.crePenalizeOutImportErrorRecord(cwb,
					// systemTime, "该记录责任机构人不存在！");
				} else {
					dutypersonid = user2.getUserid();
				}
			} catch (Exception e) {
				// this.penalizeOutImportErrorRecordDAO.crePenalizeOutImportErrorRecord(cwb,
				// systemTime, "该记录责任机构人不存在！");
				// return null;
			}
		}
		//记录已经存在的限制
	/*	PenalizeInside penalizeInside = this.punishInsideDao.getPenalizeInsideIsNullCheck(cwb, branchid, dutypersonid, penalizeOutsmall, penalizeOutGoodsfee, penalizeOutOtherfee);
		if (penalizeInside != null) {
			this.penalizeOutImportErrorRecordDAO.crePenalizeOutImportErrorRecord(cwb, systemTime, "该记录已经存在！");
			return null;
		}*/
		out.setPunishNo("P" + System.currentTimeMillis() + "");
		out.setSourceNo(co.getCwb());
		out.setCwb(co.getCwb());
		out.setCreateBySource(1);
		out.setDutybranchid(branchid);
		out.setDutypersonid(dutypersonid);
		out.setCreDate(DateTimeUtil.getNowTime());
		out.setPunishdescribe("");
		out.setFileposition("");
		out.setCwbstate(co.getFlowordertype());
		out.setCwbPrice(co.getReceivablefee());
		out.setPunishInsideprice(penalizeOutfee);
		out.setCreateuserid(user.getUserid());
		out.setPunishsmallsort(penalizeOutsmall);
		out.setPunishcwbstate(PunishInsideStateEnum.daiqueren.getValue());
		PenalizeType type = this.penalizeTypeDAO.getPenalizeTypeById(penalizeOutsmall);
		out.setPunishbigsort(type == null ? 0 : type.getParent());
		return out;
	}

	public String extractPenalizeIn(InputStream f, User user, Long systemTime) {
		List<PenalizeType> penalizeTypelList = this.penalizeTypeDAO.getPenalizeTypeByType(2);

		Map<String, Integer> penalizeTypeMap = this.SetMapPenalizeTypeMap(penalizeTypelList);

		List<PenalizeInside> penalizeIns = new ArrayList<PenalizeInside>();
		int successCounts = 0;
		int failCounts = 0;
		int totalCounts = 0;// this.getRows(f).size();
		PenalizeOutImportRecord record = new PenalizeOutImportRecord();
		for (Object row : this.getRows(f)) {
			totalCounts++;
			try {
				PenalizeInside out = this.getPenalizeInAccordingtoConf(row, penalizeTypeMap, user, systemTime);
				if (out != null) {
					penalizeIns.add(out);
				} else {
					failCounts++;
				}
			} catch (Exception e) {
				// e.printStackTrace();
				failCounts++;
				ExcelExtractor.logger.info("对内扣罚导入异常：cwb={},message={}", this.getXRowCellData(row, 1), e.toString());
				this.penalizeOutImportErrorRecordDAO.crePenalizeOutImportErrorRecord(this.getXRowCellData(row, 1), systemTime, "未知异常");

				// 失败订单数+1 前台显示

				// 存储报错订单，以便统计错误记录和处理错误订单

			}
		}
		for (PenalizeInside out : penalizeIns) {
			try {
				int nums = this.punishInsideDao.createPunishInside(out, systemTime);
				successCounts += nums;
			} catch (Exception e) {
				failCounts++;
				ExcelExtractor.logger.info("对内扣罚导入异常：cwb={},message={}", out.getCwb(), e.toString());
				this.penalizeOutImportErrorRecordDAO.crePenalizeOutImportErrorRecord(out.getCwb(), systemTime, "未知异常");

			}

		}

		record.setImportFlag(systemTime);
		record.setUserid(user.getUserid());
		record.setTotalCounts(totalCounts);
		record.setSuccessCounts(successCounts);
		record.setFailCounts(failCounts);
		this.penalizeOutImportRecordDAO.crePenalizeOutImportRecord(record);
		return failCounts + "," + successCounts;
	}

	/**
	 * @param user
	 * @param importflag
	 * @param inputStream
	 */
	@Transactional
	public void extractSalary(InputStream f, long importflag, User user) {
		List<SalaryFixed> salaryList = new ArrayList<SalaryFixed>();
		List<User> userList = this.userDAO.getAllUser();
		List<SalaryImport> salaryImports=this.salaryImportDao.getAllSalaryImports();
		Map<String,Integer> improtMap=this.SetMapImport(salaryImports);
		Map<String, User> userMap = this.SetMapUser(userList);
		int successCounts = 0;
		int failCounts = 0;
		int totalCounts = 0;
		SalaryImportRecord record=new SalaryImportRecord();
		for (Object row : this.getRows(f)) {
			totalCounts++;
			try {
				SalaryFixed salary = this.getSalaryAccordingtoConf(row, userMap, importflag,improtMap);
				if(salary!=null){
					salaryList.add(salary);
				}
				else {
					failCounts++;
				}
			} catch (Exception e) {
				failCounts++;
				this.salaryErrorDAO.creSalaryError(this.getXRowCellData(row, 1), this.getXRowCellData(row, 1), "未知异常", importflag);
				continue;
			}
		}
		for (SalaryFixed salary : salaryList) {
			try {
				SalaryFixed salary1 =this.salaryFixedDAO.getSalaryByIdcard(salary.getIdcard());
				if(salary1!=null){
					this.salaryFixedDAO.deleteSalaryByids(salary1.getId()+"");
				}
				salary.setCreuserid(user.getUserid());
				salary.setImportflag(importflag);
				int nums=this.salaryFixedDAO.creSalaryByRealname(salary);
				successCounts += nums;
			} catch (Exception e) {
				failCounts++;
				this.salaryErrorDAO.creSalaryError(salary.getRealname(), salary.getIdcard(), "未知异常", importflag);
				continue;
			}

		}
		record.setImportFlag(importflag);
		record.setUserid(user.getUserid());
		record.setTotalCounts(totalCounts);
		record.setSuccessCounts(successCounts);
		record.setFailCounts(failCounts);
		this.salaryImportRecordDAO.creSalaryImportRecord(record);
	}

	/**
	 * @param salaryImports
	 * @return
	 */
	private Map<String, Integer> SetMapImport(List<SalaryImport> salaryImports) {
		Map<String, Integer> map=new HashMap<String, Integer>();
		for(SalaryImport sip:salaryImports)
		{
			map.put(sip.getFilename(), sip.getWhichvalue());
		}
		return map;
	}

	/**
	 * @param row
	 * @param userMap
	 * @param importflag
	 * @return
	 */
	private SalaryFixed getSalaryAccordingtoConf(Object row, Map<String, User> userMap, long importflag,Map<String,Integer> map) {
		SalaryFixed salary = new SalaryFixed();
		String realname = this.getXRowCellData(row, 1);
		String idcard = this.getXRowCellData(row, 2);
		User user = userMap.get(realname);
		if (user == null) {
			this.salaryErrorDAO.creSalaryError(realname, idcard, "配送员不存在！", importflag);
			return null;
		}
		else {
			if(!idcard.equals(user.getIdcardno()))
			{
				this.salaryErrorDAO.creSalaryError(realname, idcard, "身份证号码有误！", importflag);
				return null;
			}
		}
		salary.setBranchid(user.getBranchid());
		salary.setRealname(realname);
		salary.setIdcard(idcard);
		try{
		if((null!=map.get("salarybasic"))&&(map.get("salarybasic")==0)) {
			salary.setSalarybasic(new BigDecimal(this.getXRowCellData(row, 3)));
		}
		if((null!=map.get("salaryjob"))&&(map.get("salaryjob")==0)) {
		salary.setSalaryjob(new BigDecimal(this.getXRowCellData(row, 4)));
		}
		if((null!=map.get("pushcash"))&&(map.get("pushcash")==0)) {
		salary.setPushcash(new BigDecimal(this.getXRowCellData(row, 5)));
		}
		if((null!=map.get("jobpush"))&&(map.get("jobpush")==0)) {
			salary.setJobpush(new BigDecimal(this.getXRowCellData(row, 6)));
		}
		if((null!=map.get("agejob"))&&(map.get("agejob")==0)) {
			salary.setAgejob(new BigDecimal(this.getXRowCellData(row, 7)));
		}
		if((null!=map.get("bonusroom"))&&(map.get("bonusroom")==0)) {
		salary.setBonusroom(new BigDecimal(this.getXRowCellData(row, 8)));
		}
		if((null!=map.get("bonusallday"))&&(map.get("bonusallday")==0)) {
		salary.setBonusallday(new BigDecimal(this.getXRowCellData(row, 9)));
		}
		if((null!=map.get("bonusfood"))&&(map.get("bonusfood")==0)) {
		salary.setBonusfood(new BigDecimal(this.getXRowCellData(row, 10)));
		}
		if((null!=map.get("bonustraffic"))&&(map.get("bonustraffic")==0)) {
		salary.setBonustraffic(new BigDecimal(this.getXRowCellData(row, 11)));
		}
		if((null!=map.get("bonusphone"))&&(map.get("bonusphone")==0)) {
		salary.setBonusphone(new BigDecimal(this.getXRowCellData(row, 12)));
		}
		if((null!=map.get("bonusweather"))&&(map.get("bonusweather")==0)) {
			salary.setBonusweather(new BigDecimal(this.getXRowCellData(row, 13)));
		}
		if((null!=map.get("penalizecancel_import"))&&(map.get("penalizecancel_import")==0)) {
			salary.setPenalizecancel_import(new BigDecimal(this.getXRowCellData(row, 14)));
		}
		if((null!=map.get("bonusother1"))&&(map.get("bonusother1")==0)) {
			salary.setBonusother1(new BigDecimal(this.getXRowCellData(row, 15)));
		}
		if((null!=map.get("bonusother2"))&&(map.get("bonusother2")==0)) {
		salary.setBonusother2(new BigDecimal(this.getXRowCellData(row, 16)));
		}
		if((null!=map.get("bonusother3"))&&(map.get("bonusother3")==0)) {
		salary.setBonusother3(new BigDecimal(this.getXRowCellData(row, 17)));
		}
		if((null!=map.get("bonusother4"))&&(map.get("bonusother4")==0)) {
		salary.setBonusother4(new BigDecimal(this.getXRowCellData(row, 18)));
		}
		if((null!=map.get("bonusother5"))&&(map.get("bonusother5")==0)) {
		salary.setBonusother5(new BigDecimal(this.getXRowCellData(row, 19)));
		}
		if((null!=map.get("bonusother6"))&&(map.get("bonusother6")==0)) {
		salary.setBonusother6(new BigDecimal(this.getXRowCellData(row, 20)));
		}
		if((null!=map.get("overtimework"))&&(map.get("overtimework")==0)) {
		salary.setOvertimework(new BigDecimal(this.getXRowCellData(row, 21)));
		}
		if((null!=map.get("attendance"))&&(map.get("attendance")==0)) {
		salary.setAttendance(new BigDecimal(this.getXRowCellData(row, 22)));
		}
		if((null!=map.get("security"))&&(map.get("security")==0)) {
		salary.setSecurity(new BigDecimal(this.getXRowCellData(row, 23)));
		}
		if((null!=map.get("gongjijin"))&&(map.get("gongjijin")==0)) {
		salary.setGongjijin(new BigDecimal(this.getXRowCellData(row, 24)));
		}
		if((null!=map.get("foul_import"))&&(map.get("foul_import")==0)) {
		salary.setFoul_import(new BigDecimal(this.getXRowCellData(row, 25)));
		}
		if((null!=map.get("dorm"))&&(map.get("dorm")==0)) {
			salary.setDorm(new BigDecimal(this.getXRowCellData(row, 26)));
		}
		if((null!=map.get("penalizeother1"))&&(map.get("penalizeother1")==0)) {
		salary.setPenalizeother1(new BigDecimal(this.getXRowCellData(row, 27)));
		}
		if((null!=map.get("penalizeother2"))&&(map.get("penalizeother2")==0)) {
		salary.setPenalizeother2(new BigDecimal(this.getXRowCellData(row, 28)));
		}
		if((null!=map.get("penalizeother3"))&&(map.get("penalizeother3")==0)) {
		salary.setPenalizeother3(new BigDecimal(this.getXRowCellData(row, 29)));
		}
		if((null!=map.get("penalizeother4"))&&(map.get("penalizeother4")==0)) {
		salary.setPenalizeother4(new BigDecimal(this.getXRowCellData(row, 30)));
		}
		if((null!=map.get("penalizeother5"))&&(map.get("penalizeother5")==0)) {
		salary.setPenalizeother5(new BigDecimal(this.getXRowCellData(row, 31)));
		}
		if((null!=map.get("penalizeother6"))&&(map.get("penalizeother6")==0)) {
		salary.setPenalizeother6(new BigDecimal(this.getXRowCellData(row, 32)));
		}
		if((null!=map.get("imprestother1"))&&(map.get("imprestother1")==0)) {
		salary.setImprestother1(new BigDecimal(this.getXRowCellData(row, 33)));
		}
		if((null!=map.get("imprestother2"))&&(map.get("imprestother2")==0)) {
		salary.setImprestother2(new BigDecimal(this.getXRowCellData(row, 34)));
		}
		if((null!=map.get("imprestother3"))&&(map.get("imprestother3")==0)) {
		salary.setImprestother3(new BigDecimal(this.getXRowCellData(row, 35)));
		}
		if((null!=map.get("imprestother4"))&&(map.get("imprestother4")==0)) {
		salary.setImprestother4(new BigDecimal(this.getXRowCellData(row, 36)));
		}
		if((null!=map.get("imprestother5"))&&(map.get("imprestother5")==0)) {
		salary.setImprestother5(new BigDecimal(this.getXRowCellData(row, 37)));
		}
		if((null!=map.get("imprestother6"))&&(map.get("imprestother6")==0)) {
		salary.setImprestother6(new BigDecimal(this.getXRowCellData(row, 38)));
		}
		if((null!=map.get("carrent"))&&(map.get("carrent")==0)) {
			salary.setCarrent(new BigDecimal(this.getXRowCellData(row, 39)));
		}
		if((null!=map.get("carmaintain"))&&(map.get("carmaintain")==0)) {
				salary.setCarmaintain(new BigDecimal(this.getXRowCellData(row, 40)));
		}
		if((null!=map.get("carfuel"))&&(map.get("carfuel")==0)) {
				salary.setCarfuel(new BigDecimal(this.getXRowCellData(row, 41)));
		}
	}
		catch(Exception e)
		{
			this.salaryErrorDAO.creSalaryError(realname, idcard, "数据格式有误", importflag);
			ExcelExtractor.logger.error("配送员工资导入设置异常",e);
			return null;
		}
		return salary;
	}


	public String extractAbnormal(InputStream f, User user, Long systemTime) {
		List<AbnormalImportView> abnormalImportViews=new ArrayList<AbnormalImportView>();
		List<AbnormalType> abnormalTypes=this.abnormalTypeDAO.getAllAbnormalTypeByName();
		Map<String, Long> abnormalMaps=this.getAbnormalTypeMaps(abnormalTypes);
		int successCounts = 0;
		int failCounts = 0;
		int totalCounts = 0;// this.getRows(f).size();
		PenalizeOutImportRecord record = new PenalizeOutImportRecord();
		for (Object row : this.getRows(f)) {
			totalCounts++;
			try {
				AbnormalImportView abnormalImportView = this.getAbnormalAccordingtoConf(row, abnormalMaps, user, systemTime);
				if (abnormalImportView != null) {
					abnormalImportViews.add(abnormalImportView);
				} else {
					failCounts++;
				}
			} catch (Exception e) {
				// e.printStackTrace();
				failCounts++;
				ExcelExtractor.logger.info("问题件导入异常：cwb={},message={}", this.getXRowCellData(row, 1), e.toString());
				this.penalizeOutImportErrorRecordDAO.crePenalizeOutImportErrorRecord(this.getXRowCellData(row, 1), systemTime, "未知异常");

				// 失败订单数+1 前台显示

				// 存储报错订单，以便统计错误记录和处理错误订单

			}
		}
		for (AbnormalImportView abnormalImportView : abnormalImportViews) {
			try {
				this.abnormalService.creAbnormalOrderExcel(abnormalImportView);
				successCounts ++;
			} catch (Exception e) {
				failCounts++;
				ExcelExtractor.logger.info("对内扣罚导入异常：cwb={},message={}", abnormalImportView.getCwbOrder().getCwb(), e.toString());
				this.penalizeOutImportErrorRecordDAO.crePenalizeOutImportErrorRecord(abnormalImportView.getCwbOrder().getCwb(), systemTime, "未知异常");

			}

		}

		record.setImportFlag(systemTime);
		record.setUserid(user.getUserid());
		record.setTotalCounts(totalCounts);
		record.setSuccessCounts(successCounts);
		record.setFailCounts(failCounts);
		this.penalizeOutImportRecordDAO.crePenalizeOutImportRecord(record);
		return failCounts + "," + successCounts;
	}
	private AbnormalImportView getAbnormalAccordingtoConf(Object row, Map<String, Long> abnormalTypes, User user, long systemTime) throws Exception {
		AbnormalImportView abnormalImportView=new AbnormalImportView();
		String cwb = this.getXRowCellData(row, 1);
		CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);
		if (co == null) {
			this.penalizeOutImportErrorRecordDAO.crePenalizeOutImportErrorRecord(cwb, systemTime, "订单号不存在！");
			return null;
		} else {
			abnormalImportView.setCwbOrder(co);
		}
		long abnormaltypeid=0;
		String abnormalTypeString= this.getXRowCellData(row, 2);
		try {
			abnormaltypeid=abnormalTypes.get(abnormalTypeString);
		} catch (Exception e) {
			this.penalizeOutImportErrorRecordDAO.crePenalizeOutImportErrorRecord(cwb, systemTime, "问题件类型不存在！");
			return null;
		}
		String abnormalInfo= this.getXRowCellData(row, 3);
		try {
			if (abnormalInfo.length()>100) {
				this.penalizeOutImportErrorRecordDAO.crePenalizeOutImportErrorRecord(cwb, systemTime, "问题件说明字数大于100！");
				return null;
			}
		} catch (Exception e) {
			this.penalizeOutImportErrorRecordDAO.crePenalizeOutImportErrorRecord(cwb, systemTime, "问题件说明字数大于100！");
			return null;
		}
		/*//判断问题件记录是否存在
		List<AbnormalOrder> abnormalOrders=this.abnormalOrderDAO.checkexcelIsExist(cwb,abnormaltypeid,abnormalInfo);
		if ((abnormalOrders != null)&&(abnormalOrders.size()>0)) {
			this.penalizeOutImportErrorRecordDAO.crePenalizeOutImportErrorRecord(cwb, systemTime, "该记录已经存在！");
			return null;
		}*/
		long action = AbnormalWriteBackEnum.ChuangJian.getValue();
		//abnormalOrder的状态为未处理
		long ishandle=AbnormalOrderHandleEnum.weichuli.getValue();
		long isfind=0;
		Branch branch=this.branchDAO.getBranchByBranchid(user.getBranchid());
		long handleBranch=0;//0为没有操作机构
		if (branch!=null) {
			handleBranch=branch.getSitetype();
		}
		abnormalImportView.setAbnormalinfo(abnormalInfo);
		abnormalImportView.setAbnormaltypeid(abnormaltypeid);
		abnormalImportView.setAction(action);
		abnormalImportView.setFilepath("");
		abnormalImportView.setHandleBranch(handleBranch);
		abnormalImportView.setIsfind(isfind);
		abnormalImportView.setNowtime(DateTimeUtil.getNowTime());
		abnormalImportView.setQuestionNo("Q"+System.currentTimeMillis());
		abnormalImportView.setUser(user);
		abnormalImportView.setIshandle(ishandle);
		abnormalImportView.setSystemtime(systemTime);
		return abnormalImportView;
	}
	public Map<String, Long> getAbnormalTypeMaps(List<AbnormalType> abnormalTypes){
		Map<String, Long> abnormalMaps=new HashMap<String, Long>();
		for (Iterator<AbnormalType> iterator = abnormalTypes.iterator(); iterator.hasNext();) {
			AbnormalType abnormalType = iterator.next();
			abnormalMaps.put(abnormalType.getName(), abnormalType.getId());
		}
		return abnormalMaps;
	}

	/*
	 * importflag
	 * 
	 */
	@Transactional
	public void extractSalaryGather(InputStream input, long importflag, User user) {
		List<SalaryGather> salaryList = new ArrayList<SalaryGather>();
		List<User> userList = this.userDAO.getAllUser();
		List<SalaryFixed> fixedList = this.salaryFixedDAO.getAllFixed();
		List<SalaryImport> salaryImports=this.salaryImportDao.getAllSalaryImports();//固定值设置描述
		Map<String,Integer> improtMap=this.SetMapImport(salaryImports);
		Map<String, User> userMap = this.SetMapUser(userList);
		Map<String,SalaryFixed> fixedMap = this.setMapFixed(fixedList);
		int successCounts = 0;
		int failCounts = 0;
		int totalCounts = 0;
		SalaryImportRecord record=new SalaryImportRecord();
		for (Object row : this.getRows(input)) {
			totalCounts++;
			try {
				SalaryGather salary = this.getSalaryGatherAccordingtoConf(row, userMap, importflag,improtMap,fixedMap);
				if(salary!=null){
					salaryList.add(salary);
				}
				else {
					failCounts++;
				}
			} catch (Exception e) {
				failCounts++;
				this.salaryErrorDAO.creSalaryError(this.getXRowCellData(row, 1), this.getXRowCellData(row, 1), "未知异常", importflag);
				continue;
			}
		}
		record.setImportFlag(importflag);
		record.setUserid(user.getUserid());
		record.setTotalCounts(totalCounts);
		record.setSuccessCounts(successCounts);
		record.setFailCounts(failCounts);
		this.salaryImportRecordDAO.creSalaryImportRecord(record);
	}

	private Map<String, SalaryFixed> setMapFixed(List<SalaryFixed> fixedList){
		Map<String, SalaryFixed> fixedMap = new HashMap<String, SalaryFixed>();
		for(SalaryFixed sf : fixedList){
			fixedMap.put(sf.getIdcard(), sf);
		}
		return fixedMap;
	}
	
	/**
	 * @param row
	 * @param userMap
	 * @param importflag
	 * @return
	 */
	private SalaryGather getSalaryGatherAccordingtoConf(Object row, Map<String, User> userMap, long importflag,Map<String,Integer> map,Map<String,SalaryFixed> fixedMap) {
		SalaryGather salary = new SalaryGather();
		String realname = this.getXRowCellData(row, 1);
		String idcard = this.getXRowCellData(row, 2);
		User user = userMap.get(realname);
		if (user == null) {
			this.salaryErrorDAO.creSalaryError(realname, idcard, "配送员不存在！", importflag);
			return null;
		}
		else {
			if(!idcard.equals(user.getIdcardno()))
			{
				this.salaryErrorDAO.creSalaryError(realname, idcard, "身份证号码有误！", importflag);
				return null;
			}
		}
		salary.setBranchid(user.getBranchid());
		salary.setRealname(realname);
		salary.setIdcard(idcard);
		SalaryFixed sf = fixedMap.get(idcard);
		try{
			
			//结算单量
			salary.setAccountSingle(0);
			
			if((null!=map.get("salarybasic"))&&(map.get("salarybasic")==1)) {//基本工资
				salary.setSalarybasic(new BigDecimal(this.getXRowCellData(row, 3)));
			}else{
				salary.setSalarybasic(sf.getSalarybasic());
			}
			if((null!=map.get("salaryjob"))&&(map.get("salaryjob")==1)) {//岗位工资
				salary.setSalaryjob(new BigDecimal(this.getXRowCellData(row, 4)));
			}else{
				salary.setSalaryjob(sf.getSalaryjob());
			}
			if((null!=map.get("pushcash"))&&(map.get("pushcash")==1)) {//绩效奖金pushcash
				salary.setPushcash(new BigDecimal(this.getXRowCellData(row, 5)));
			}else{
				salary.setPushcash(sf.getPushcash());
			}
			if((null!=map.get("jobpush"))&&(map.get("jobpush")==1)) {//岗位津贴
				salary.setJobpush(new BigDecimal(this.getXRowCellData(row, 6)));
			}else{
				salary.setJobpush(sf.getJobpush());
			}

			//提成
			salary.setSalarypush(BigDecimal.ZERO);
			
			
			if((null!=map.get("agejob"))&&(map.get("agejob")==1)) {//工龄
				salary.setAgejob(new BigDecimal(this.getXRowCellData(row, 7)));
			}else{
				salary.setAgejob(sf.getAgejob());
			}
			
			if((null!=map.get("bonusroom"))&&(map.get("bonusroom")==1)) {//住房补贴
				salary.setBonusroom(new BigDecimal(this.getXRowCellData(row, 8)));
			}else{
				salary.setBonusroom(sf.getBonusroom());
			}
			if((null!=map.get("bonusallday"))&&(map.get("bonusallday")==1)) {//全勤补贴
				salary.setBonusallday(new BigDecimal(this.getXRowCellData(row, 9)));
			}else{
				salary.setBonusallday(sf.getBonusallday());
			}
			if((null!=map.get("bonusfood"))&&(map.get("bonusfood")==1)) {//餐费补贴
				salary.setBonusfood(new BigDecimal(this.getXRowCellData(row, 10)));
			}else{
				salary.setBonusfood(sf.getBonusfood());
			}
			if((null!=map.get("bonustraffic"))&&(map.get("bonustraffic")==1)) {//交通补贴
				salary.setBonustraffic(new BigDecimal(this.getXRowCellData(row, 11)));
			}else{
				salary.setBonustraffic(sf.getBonustraffic());
			}
			if((null!=map.get("bonusphone"))&&(map.get("bonusphone")==1)) {//通讯补贴
				salary.setBonusphone(new BigDecimal(this.getXRowCellData(row, 12)));
			}else{
				salary.setBonusphone(salary.getBonusphone());
			}
			if((null!=map.get("bonusweather"))&&(map.get("bonusweather")==1)) {//高温寒冷补贴
				salary.setBonusweather(new BigDecimal(this.getXRowCellData(row, 13)));
			}else{
				salary.setBonusweather(sf.getBonusweather());
			}
			
			//扣款撤销
			salary.setPenalizecancel(BigDecimal.ZERO);
			
			if((null!=map.get("penalizecancel_import"))&&(map.get("penalizecancel_import")==1)) {//扣款撤销(导入)
				salary.setPenalizecancel_import(new BigDecimal(this.getXRowCellData(row, 14)));
			}else{
				salary.setPenalizecancel_import(sf.getPenalizecancel_import());
			}
			if((null!=map.get("bonusother1"))&&(map.get("bonusother1")==1)) {//其他补贴
				salary.setBonusother1(new BigDecimal(this.getXRowCellData(row, 15)));
			}else{
				salary.setBonusother1(sf.getBonusother1());
			}
			if((null!=map.get("bonusother2"))&&(map.get("bonusother2")==1)) {//其他补贴2
				salary.setBonusother2(new BigDecimal(this.getXRowCellData(row, 16)));
			}else{
				salary.setBonusother2(sf.getBonusother2());
			}
			if((null!=map.get("bonusother3"))&&(map.get("bonusother3")==1)) {//其他补贴3
				salary.setBonusother3(new BigDecimal(this.getXRowCellData(row, 17)));
			}else{
				salary.setBonusother3(sf.getBonusother3());
			}
			if((null!=map.get("bonusother4"))&&(map.get("bonusother4")==1)) {//其他补贴4
				salary.setBonusother4(new BigDecimal(this.getXRowCellData(row, 18)));
			}else{
				salary.setBonusother4(sf.getBonusother4());
			}
			if((null!=map.get("bonusother5"))&&(map.get("bonusother5")==1)) {//其他补贴5
				salary.setBonusother5(new BigDecimal(this.getXRowCellData(row, 19)));
			}else{
				salary.setBonusother5(sf.getBonusother5());
			}
			if((null!=map.get("bonusother6"))&&(map.get("bonusother6")==1)) {//其他补贴6
				salary.setBonusother6(new BigDecimal(this.getXRowCellData(row, 20)));
			}else{
				salary.setBonusother6(sf.getBonusother6());
			}
			if((null!=map.get("overtimework"))&&(map.get("overtimework")==1)) {//加班费
				salary.setOvertimework(new BigDecimal(this.getXRowCellData(row, 21)));
			}else{
				salary.setOvertimework(sf.getOvertimework());
			}
			if((null!=map.get("attendance"))&&(map.get("attendance")==1)) {//考勤扣款
				salary.setAttendance(new BigDecimal(this.getXRowCellData(row, 22)));
			}else{
				salary.setAttendance(sf.getAttendance());
			}
			if((null!=map.get("security"))&&(map.get("security")==1)) {//个人社保扣款
				salary.setSecurity(new BigDecimal(this.getXRowCellData(row, 23)));
			}else{
				salary.setSecurity(sf.getSecurity());
			}
			if((null!=map.get("gongjijin"))&&(map.get("gongjijin")==1)) {//个人公积金扣款
				salary.setGongjijin(new BigDecimal(this.getXRowCellData(row, 24)));
			}else{
				salary.setGongjijin(sf.getGongjijin());
			}
			
			//违纪扣款扣罚
			salary.setFoul(BigDecimal.ZERO);
			
			
			if((null!=map.get("foul_import"))&&(map.get("foul_import")==1)) {//违纪扣款扣罚(导入)
				salary.setFoul_import(new BigDecimal(this.getXRowCellData(row, 25)));
			}else{
				salary.setFoul_import(sf.getFoul_import());
			}
			
			//货损赔偿
			salary.setGoods(BigDecimal.ZERO);
			
			if((null!=map.get("dorm"))&&(map.get("dorm")==1)) {//宿舍费用
				salary.setDorm(new BigDecimal(this.getXRowCellData(row, 26)));
			}else{
				salary.setDorm(sf.getDorm());
			}
			if((null!=map.get("penalizeother1"))&&(map.get("penalizeother1")==1)) {//其他扣罚
				salary.setPenalizeother1(new BigDecimal(this.getXRowCellData(row, 27)));
			}else{
				salary.setPenalizeother1(sf.getPenalizeother1());
			}
			if((null!=map.get("penalizeother2"))&&(map.get("penalizeother2")==1)) {//其他扣罚2
				salary.setPenalizeother2(new BigDecimal(this.getXRowCellData(row, 28)));
			}else{
				salary.setPenalizeother2(sf.getPenalizeother2());
			}
			if((null!=map.get("penalizeother3"))&&(map.get("penalizeother3")==1)) {//其他扣罚3
				salary.setPenalizeother3(new BigDecimal(this.getXRowCellData(row, 29)));
			}else{
				salary.setPenalizeother3(sf.getPenalizeother3());
			}
			if((null!=map.get("penalizeother4"))&&(map.get("penalizeother4")==1)) {//其他扣罚4
				salary.setPenalizeother4(new BigDecimal(this.getXRowCellData(row, 30)));
			}else{
				salary.setPenalizeother4(sf.getPenalizeother4());
			}
			if((null!=map.get("penalizeother5"))&&(map.get("penalizeother5")==1)) {//其他扣罚5
				salary.setPenalizeother5(new BigDecimal(this.getXRowCellData(row, 31)));
			}else{
				salary.setPenalizeother5(sf.getPenalizeother5());
			}
			if((null!=map.get("penalizeother6"))&&(map.get("penalizeother6")==1)) {//其他扣罚6
				salary.setPenalizeother6(new BigDecimal(this.getXRowCellData(row, 32)));
			}else{
				salary.setPenalizeother6(sf.getPenalizeother6());
			}
			
			//货物预付款
			salary.setImprestgoods(BigDecimal.ZERO);
			
			if((null!=map.get("imprestother1"))&&(map.get("imprestother1")==1)) {//其他预付款
				salary.setImprestother1(new BigDecimal(this.getXRowCellData(row, 33)));
			}else{
				salary.setImprestother1(sf.getImprestother1());
			}
			if((null!=map.get("imprestother2"))&&(map.get("imprestother2")==1)) {//其他预付款2
				salary.setImprestother2(new BigDecimal(this.getXRowCellData(row, 34)));
			}else{
				salary.setImprestother2(sf.getImprestother2());
			}
			if((null!=map.get("imprestother3"))&&(map.get("imprestother3")==1)) {//其他预付款3
				salary.setImprestother3(new BigDecimal(this.getXRowCellData(row, 35)));
			}else{
				salary.setImprestother3(sf.getImprestother3());
			}
			if((null!=map.get("imprestother4"))&&(map.get("imprestother4")==1)) {//其他预付款4
				salary.setImprestother4(new BigDecimal(this.getXRowCellData(row, 36)));
			}else{
				salary.setImprestother4(sf.getImprestother5());
			}
			if((null!=map.get("imprestother5"))&&(map.get("imprestother5")==1)) {//其他预付款5
				salary.setImprestother5(new BigDecimal(this.getXRowCellData(row, 37)));
			}else{
				salary.setImprestother5(sf.getImprestother5());
			}
			if((null!=map.get("imprestother6"))&&(map.get("imprestother6")==1)) {//其他预付款6
				salary.setImprestother6(new BigDecimal(this.getXRowCellData(row, 38)));
			}else{
				salary.setImprestother6(sf.getImprestother6());
			}
			
			if((null!=map.get("carrent"))&&(map.get("carrent")==1)) {//租用车辆费用
				salary.setCarrent(new BigDecimal(this.getXRowCellData(row, 39)));
			}else{
				salary.setCarrent(sf.getCarrent());
			}
			if((null!=map.get("carmaintain"))&&(map.get("carmaintain")==1)) {//车子维修给用
				salary.setCarmaintain(new BigDecimal(this.getXRowCellData(row, 40)));
			}else{
				salary.setCarmaintain(sf.getCarmaintain());
			}
			if((null!=map.get("carfuel"))&&(map.get("carfuel")==1)) {//油/电费用
				salary.setCarfuel(new BigDecimal(this.getXRowCellData(row, 41)));
			}else{
				salary.setCarfuel(sf.getCarfuel());
			}
			
			//应发金额
			salary.setSalaryaccrual(BigDecimal.ZERO);
			//个税
			salary.setTax(BigDecimal.ZERO);
			//实发金额
			salary.setSalary(BigDecimal.ZERO);
			
		}catch(Exception e){
			this.salaryErrorDAO.creSalaryError(realname, idcard, "数据格式有误", importflag);
			ExcelExtractor.logger.error("人事数据导入异常",e);
			return null;
		}
		return salary;
	}






}