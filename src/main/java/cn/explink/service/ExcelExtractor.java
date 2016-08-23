package cn.explink.service;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
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
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.PayWayDao;
import cn.explink.dao.PaybusinessbenefitsDao;
import cn.explink.dao.PenalizeOutDAO;
import cn.explink.dao.PenalizeOutImportErrorRecordDAO;
import cn.explink.dao.PenalizeOutImportRecordDAO;
import cn.explink.dao.PenalizeTypeDAO;
import cn.explink.dao.PunishDAO;
import cn.explink.dao.PunishInsideDao;
import cn.explink.dao.PunishTypeDAO;
import cn.explink.dao.RoleDAO;
import cn.explink.dao.SalaryCountDAO;
import cn.explink.dao.SalaryErrorDAO;
import cn.explink.dao.SalaryFixedDAO;
import cn.explink.dao.SalaryGatherDao;
import cn.explink.dao.SalaryImportDao;
import cn.explink.dao.SalaryImportRecordDAO;
import cn.explink.dao.ServiceAreaDAO;
import cn.explink.dao.SetExcelColumnDAO;
import cn.explink.dao.SwitchDAO;
import cn.explink.dao.UserDAO;
import cn.explink.dao.express.CityDAO;
import cn.explink.dao.express.CountyDAO;
import cn.explink.dao.express.ExpressOrderDao;
import cn.explink.dao.express.ProvinceDAO;
import cn.explink.dao.express.TownDAO;
import cn.explink.domain.AbnormalImportView;
import cn.explink.domain.AbnormalType;
import cn.explink.domain.Branch;
import cn.explink.domain.Common;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.EmailDate;
import cn.explink.domain.ExcelColumnSet;
import cn.explink.domain.ImportValidationManager;
import cn.explink.domain.PenalizeInside;
import cn.explink.domain.PenalizeOut;
import cn.explink.domain.PenalizeOutImportRecord;
import cn.explink.domain.PenalizeType;
import cn.explink.domain.Punish;
import cn.explink.domain.PunishType;
import cn.explink.domain.Role;
import cn.explink.domain.SalaryCount;
import cn.explink.domain.SalaryFixed;
import cn.explink.domain.SalaryGather;
import cn.explink.domain.SalaryImport;
import cn.explink.domain.SalaryImportRecord;
import cn.explink.domain.ServiceArea;
import cn.explink.domain.User;
import cn.explink.domain.VO.express.AdressVO;
import cn.explink.domain.VO.express.EmbracedImportErrOrder;
import cn.explink.domain.VO.express.EmbracedImportOrderVO;
import cn.explink.domain.VO.express.EmbracedImportResult;
import cn.explink.domain.VO.express.EmbracedOrderVO;
import cn.explink.domain.express.ExpressOperationInfo;
import cn.explink.enumutil.AbnormalOrderHandleEnum;
import cn.explink.enumutil.AbnormalWriteBackEnum;
import cn.explink.enumutil.CwbOrderAddressCodeEditTypeEnum;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.IsmpsflagEnum;
import cn.explink.enumutil.JiesuanstateEnum;
import cn.explink.enumutil.MPSAllArrivedFlagEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.enumutil.PenalizeSateEnum;
import cn.explink.enumutil.PunishInsideStateEnum;
import cn.explink.enumutil.PunishlevelEnum;
import cn.explink.enumutil.PunishtimeEnum;
import cn.explink.enumutil.express.ExpressOperationEnum;
import cn.explink.enumutil.switchs.SwitchEnum;
import cn.explink.service.express.EmbracedOrderInputService;
import cn.explink.service.express.ExpressCommonService;
import cn.explink.service.express.TpsInterfaceExecutor;
import cn.explink.service.express.tps.enums.FeedbackOperateTypeEnum;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.StringUtil;

import com.pjbest.deliveryorder.service.PjTransportFeedbackRequest;

public abstract class ExcelExtractor extends ExpressCommonService {

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
	@Autowired
	SalaryGatherService salaryGatherService;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	PaybusinessbenefitsDao paybusinessbenefitsDao;
	@Autowired
	SalaryCountDAO salaryCountDAO;
	@Autowired
	ProvinceDAO provinceDAO;
	@Autowired
	CityDAO cityDAO;
	@Autowired
	CountyDAO countyDAO;
	@Autowired
	TownDAO townDAO;
	@Autowired
	RoleDAO roleDAO;
	@Autowired
	ExpressOrderDao expressOrderDao;
	@Autowired
	EmbracedOrderInputService embracedOrderInputService;
	@Autowired
	TpsInterfaceExecutor tpsInterfaceExecutor;
	@Autowired
	CwbOrderService cwbOrderService;

	Map<String, Integer> EmbracedColumnIndexMap = new HashMap<String, Integer>() {
		{
			this.put("OrderNo", 1);
			this.put("Sender_name", 2);
			this.put("Sender_companyName", 3);
			this.put("Monthly_account_number", 4);
			this.put("Sender_provinceName", 5);
			this.put("Sender_cityName", 6);
			this.put("Sender_countyName", 7);
			this.put("Sender_townName", 8);
			this.put("Sender_adress", 9);
			this.put("Sender_cellphone", 10);
			this.put("Sender_telephone", 11);
			this.put("Goods_name", 12);
			this.put("Goods_number", 13);
			this.put("Charge_weight", 14);
			this.put("Goods_length", 15);
			this.put("Goods_width", 16);
			this.put("Goods_high", 17);
			this.put("Goods_other", 18);
			this.put("Actual_weight", 19);
			this.put("Consignee_name", 20);
			this.put("Consignee_provinceName", 21);
			this.put("Consignee_cityName", 22);
			this.put("Consignee_countyName", 23);
			this.put("Consignee_townName", 24);
			this.put("Consignee_adress", 25);
			this.put("Consignee_cellphone", 26);
			this.put("Consignee_telephone", 27);
			this.put("DelivermanName", 28);
			this.put("Xianfu", 29);
			this.put("Daofu", 30);
			this.put("Yuejie", 31);
			this.put("Collection_amount", 32);
			this.put("Insured_value", 33);
			this.put("Insured_cost", 34);
			this.put("Packing_amount", 35);
		}
	};

	Map<String, Integer> checkmMap = new HashMap<String, Integer>() {
		{
			this.put("OrderNo", 1);
			this.put("Sender_name", 2);
			this.put("Sender_companyName", 3);
			this.put("Monthly_account_number", 4);
			this.put("Sender_provinceName", 5);
			this.put("Sender_cityName", 6);
			this.put("Sender_countyName", 7);
			this.put("Sender_townName", 8);
			this.put("Sender_adress", 9);
			this.put("Sender_cellphone", 10);
			this.put("Sender_telephone", 11);
			this.put("Goods_name", 12);
			this.put("Goods_number", 13);
			this.put("Charge_weight", 14);
			this.put("Goods_length", 15);
			this.put("Goods_width", 16);
			this.put("Goods_high", 17);
			this.put("Goods_other", 18);
			this.put("Actual_weight", 19);
			this.put("Consignee_name", 20);
			this.put("Consignee_provinceName", 21);
			this.put("Consignee_cityName", 22);
			this.put("Consignee_countyName", 23);
			this.put("Consignee_townName", 24);
			this.put("Consignee_adress", 25);
			this.put("Consignee_cellphone", 26);
			this.put("Consignee_telephone", 27);
			this.put("DelivermanName", 28);
			this.put("Xianfu", 29);
			this.put("Daofu", 30);
			this.put("Yuejie", 31);
			this.put("Collection_amount", 32);
			this.put("Insured_amount", 33);
			this.put("Insured_cost", 34);
			this.put("Packing_amount", 35);
		}
	};

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

	protected CwbOrderDTO getCwbOrderAccordingtoConf(ExcelColumnSet excelColumnSet, Object row, long customerid, EmailDate ed, List<Branch> branchList, List<Common> commonList, long branchid, Customer customer, Map<String, Long> orderTypeIdMap, Map<String, ServiceArea> serviceAreaMap, Map<String, Long> payWayIdMap, Map<String, Long> customWareHouseIdMap) throws Exception {
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
					logger.error("", e);
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
		
		extendsImportColumn(customer, cwbOrder);
		

		cwbOrder.setDefaultCargoName();

		return cwbOrder;
	}

	protected EmbracedImportOrderVO getEmbracedOrderAccordingtoConf(Map<String, Integer> map, Object row) throws Exception {
		EmbracedImportOrderVO embracedImportOrder = new EmbracedImportOrderVO();
		// 反射机制调用方法
		String methodName = "";
		Method m;
		Set<String> keys = map.keySet();
		for (String key : keys) {
			methodName = "set" + key;
			// 获取方法
			m = embracedImportOrder.getClass().getDeclaredMethod(methodName, String.class);
			// 调用方法
			m.invoke(embracedImportOrder, this.getXRowCellData(row, this.EmbracedColumnIndexMap.get(key)).trim());
		}
		return embracedImportOrder;
	}

	/**
	 * 扩展导入列方法,主要处理一票多件集包模式
	 * @param customer
	 * @param cwbOrder
	 */
	private void extendsImportColumn(Customer customer, CwbOrderDTO cwbOrder) {
		if(customer.getMpsswitch()!=0){ //开启集单模式
			if (cwbOrder.getTranscwb() != null) { //add by neo01.huang，2016-6-1，加入非空判断，不然会有报空指针异常的风险
				if(cwbOrder.getTranscwb().split(cwbOrderService.getSplitstring(cwbOrder.getTranscwb())).length>1){
					cwbOrder.setIsmpsflag((int)IsmpsflagEnum.yes.getValue()); //默认是一票多件模式
					cwbOrder.setMpsallarrivedflag(MPSAllArrivedFlagEnum.YES.getValue()); //excel导入默认到齐
				}
			}
		}
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
				CwbOrderDTO cwbOrder = this
						.getCwbOrderAccordingtoConf(excelColumnSet, row, customerId, ed, branchList, commonList, branchId, customer, orderTypeIdMap, serviceAreaMap, payWayIdMap, customWareHouseIdMap);
				for (CwbOrderValidator cwbOrderValidator : vailidators) {
					cwbOrderValidator.validate(cwbOrder);
				}
				//zhili01.liang V4.2.18 货物类型修改：默认为普件=====Begin===
				if(StringUtils.isEmpty(cwbOrder.getCargotype())){
					cwbOrder.setCargotype("普件");
				}
				//zhili01.liang V4.2.18货物类型修改：默认为普件=====End===
				cwbOrders.add(cwbOrder);
				if ((count % 100) == 0) {
					ExcelExtractor.logger.info("parsed {} orders", count);
					this.dataImportService.importData(cwbOrders, userDetail.getUser(), errorCollector, ed, isRetry);
					cwbOrders.clear();
				}
			} catch (Exception e) {
				logger.error("", e);
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
				//errorOrder.put("message", e.getMessage());
				//update by neo01.huang，如果Exception刚好是空指针异常，此时e.getMessage()就是null，加上"",
				//可以避免后续errorOrder.getString("message")报异常
				errorOrder.put("message", e.getMessage() + "");

				
				this.importCwbErrorService.saveOrderError(errorOrder);
			}
		}

		if (cwbOrders.size() > 0) {
			this.dataImportService.importData(cwbOrders, userDetail.getUser(), errorCollector, ed, isRetry);
		}

	}

	/**
	 *
	 * @Title: extractExbraced
	 * @description 解析的数据进行保存，保存前校验
	 * @author 刘武强
	 * @date 2015年10月13日上午9:10:31
	 * @param @param cwbImportOrders
	 * @param @param user
	 * @return void
	 * @throws
	 */
	public void saveExbracedImport(List<EmbracedImportOrderVO> cwbImportOrders, User user) {
		//Added by leoliao at 2016-07-26 输出日志
		logger.info("批量导入方式揽件入站");
		
		List<EmbracedOrderVO> EmbracedOrders = new ArrayList<EmbracedOrderVO>();
		EmbracedImportResult resultCollector = new EmbracedImportResult();
		List<EmbracedOrderVO> EmbracedOrdersTps = new ArrayList<EmbracedOrderVO>();
		// 保存前转化为可以保存的数据形式并校验
		Map<String, List<EmbracedOrderVO>> map = this.changeAndCheckDate(cwbImportOrders, user, EmbracedOrders, resultCollector);
		// 将isadditionflag=1的运单调用tps创建运单接口
		List<String> flowCwbList = new ArrayList<String>();
		for (EmbracedOrderVO embracedOrderVO : map.get("insertOrders")) {
			if ((embracedOrderVO.getIsadditionflag() != null) && "1".equals(embracedOrderVO.getIsadditionflag())) {
				EmbracedOrdersTps.add(embracedOrderVO);
			}
			// 新建的运单，要调用状态反馈接口-刘武强11.06
			this.executeTpsTransInterface(embracedOrderVO, user);
			flowCwbList.add(embracedOrderVO.getOrderNo());
		}
		for (EmbracedOrderVO embracedOrderVO : map.get("updateOrders")) {
			if ((embracedOrderVO.getIsadditionflag() != null) && "1".equals(embracedOrderVO.getIsadditionflag())) {
				EmbracedOrdersTps.add(embracedOrderVO);
			}
			// 更新的运单中，如果运单的状态为揽件录入，那么需要把状态改为揽件入站，并且发送tps状态反馈接口；否则还是运单原来的状态，不需要改变
			if ((embracedOrderVO.getFlowordertype() != null) && (FlowOrderTypeEnum.YunDanLuRu.getValue() + "").equals(embracedOrderVO.getFlowordertype())) {
				this.executeTpsTransInterface(embracedOrderVO, user);
				embracedOrderVO.setFlowordertype(FlowOrderTypeEnum.LanJianRuZhan.getValue() + "");
				flowCwbList.add(embracedOrderVO.getOrderNo());
			}
		}

		Branch branch = this.branchDAO.getBranchByBranchid(this.getSessionUser().getBranchid());
		// 保存校验通过的数据
		this.expressOrderDao.importEmbracedData(map.get("insertOrders"), user, branch);
		// 更新已经存在的并且可以补录的订单
		this.expressOrderDao.updateImportEmbracedData(map.get("updateOrders"), user, branch);
		String[] flowArray = flowCwbList.toArray(new String[flowCwbList.size()]);
		String cwbs = StringUtils.join(flowArray, "','");
		cwbs = "'" + cwbs + "'";
		List<CwbOrder> orders = this.cwbDAO.getcwborderList(cwbs);

		for (EmbracedOrderVO embracedOrderVO : EmbracedOrdersTps) {
			this.embracedOrderInputService.tpsSender(embracedOrderVO, "inbrace");
		}
		for (CwbOrder order : orders) {
			this.cwbOrderService.createFloworder(user, branch.getBranchid(), order, FlowOrderTypeEnum.LanJianRuZhan, "", System.currentTimeMillis());
		}

	}

	/**
	 *
	 * @Title: changeAndCheckDate
	 * @description 转化为可以保存的数据形式并校验
	 * @author 刘武强
	 * @date 2015年10月13日上午9:14:39
	 * @param @param cwbImportOrders
	 * @param @param user
	 * @param @param EmbracedOrders
	 * @param @param resultCollector
	 * @return void
	 * @throws
	 */
	private Map<String, List<EmbracedOrderVO>> changeAndCheckDate(List<EmbracedImportOrderVO> cwbImportOrders, User user, List<EmbracedOrderVO> EmbracedOrders, EmbracedImportResult resultCollector) {
		Map<String, List<EmbracedOrderVO>> map = new HashMap<String, List<EmbracedOrderVO>>();
		if (cwbImportOrders.size() > 0) {
			// 根据解析的数据获取查询条件
			StringBuffer cwbordersArr = new StringBuffer();// 订单号集合字符串
			StringBuffer senderProvincesArr = new StringBuffer();// 寄件人省的集合字符串
			StringBuffer senderCitysArr = new StringBuffer();// 寄件人市的集合字符串
			StringBuffer senderCountysArr = new StringBuffer();// 寄件人区的集合字符串
			StringBuffer sendertownsArr = new StringBuffer();// 寄件人街道的集合字符串
			StringBuffer consigneeProvincesArr = new StringBuffer();// 收件人省的集合字符串
			StringBuffer consigneeCitysArr = new StringBuffer();// 收件人市的集合字符串
			StringBuffer consigneeCountysArr = new StringBuffer();// 收件人区的集合字符串
			StringBuffer senderCompanyNamesArr = new StringBuffer();// 寄件人公司名称的集合字符串
			StringBuffer delivermanNamesArr = new StringBuffer();// 揽件员的集合字符串
			StringBuffer consigneeTownsArr = new StringBuffer();// 收件人街道的集合字符串

			// 存放查询数据所用的list
			List<EmbracedOrderVO> cwbordersList = new ArrayList<EmbracedOrderVO>();// 订单号集合
			List<String> transorderList = new ArrayList<String>();// 运单号集合
			List<AdressVO> senderProvincesList = new ArrayList<AdressVO>();// 寄件人省的集合
			List<AdressVO> senderCitysList = new ArrayList<AdressVO>();// 寄件人市的集合
			List<AdressVO> senderCountysList = new ArrayList<AdressVO>();// 寄件人区的集合
			List<AdressVO> sendertownsList = new ArrayList<AdressVO>();// 寄件人街道的集合
			List<AdressVO> consigneeProvincesList = new ArrayList<AdressVO>();// 收件人省的集合
			List<AdressVO> consigneeCitysList = new ArrayList<AdressVO>();// 收件人市的集合
			List<AdressVO> consigneeCountysList = new ArrayList<AdressVO>();// 收件人区的集合
			List<AdressVO> consigneeTownsList = new ArrayList<AdressVO>();// 收件人街道的集合
			List<Customer> senderCompanyNamesList = new ArrayList<Customer>();// 寄件人公司名称的集合
			List<User> delivermanNamesList = new ArrayList<User>();// 属于本站点的揽件员的集合
			Set<String> repeatOrdersSet = new HashSet<String>();// 重复订单号集合

			// 初始化StringBuffer
			this.initArr(cwbImportOrders, cwbordersArr, senderProvincesArr, senderCitysArr, senderCountysArr, sendertownsArr, consigneeProvincesArr, consigneeCitysArr, consigneeCountysArr, senderCompanyNamesArr, delivermanNamesArr, consigneeTownsArr);

			// 根据查询条件，获取查询数据
			// this.getData(cwbordersArr, senderProvincesArr, senderCitysArr,
			// senderCountysArr, sendertownsArr, consigneeProvincesArr,
			// consigneeCitysArr, consigneeCountysArr, senderCompanyNamesArr,
			// delivermanNamesArr, cwbordersList, senderProvincesList,
			// senderCitysList, senderCountysList, sendertownsList,
			// consigneeProvincesList, consigneeCitysList, consigneeCountysList,
			// senderCompanyNamesList, delivermanNamesList, user);
			cwbordersList = this.expressOrderDao.getOrderBycwbs(cwbordersArr.toString());
			transorderList = this.expressOrderDao.getTranscwbByCwbs(cwbordersArr.toString());
			senderProvincesList = this.provinceDAO.getProvincesByProviceNames(senderProvincesArr.toString());
			senderCitysList = this.cityDAO.getCityByCityNames(senderCitysArr.toString());
			senderCountysList = this.countyDAO.getCountysByCountyNames(senderCountysArr.toString());
			sendertownsList = this.townDAO.getTownByTownNames(sendertownsArr.toString());
			consigneeProvincesList = this.provinceDAO.getProvincesByProviceNames(consigneeProvincesArr.toString());
			consigneeCitysList = this.cityDAO.getCityByCityNames(consigneeCitysArr.toString());
			consigneeCountysList = this.countyDAO.getCountysByCountyNames(consigneeCountysArr.toString());
			consigneeTownsList = this.townDAO.getTownByTownNames(consigneeTownsArr.toString());
			senderCompanyNamesList = this.customerDAO.getCustomerByCustomernames(senderCompanyNamesArr.toString());
			delivermanNamesList = this.getDeliveryManByDeliveryManNames(delivermanNamesArr.toString(), user);
			repeatOrdersSet = this.getRepeatOrderNo(cwbImportOrders);

			// 将解析出来的数据转变为可运单类型的实体,同时校验数据，将错误数据放到resultCollector中
			map = this
					.changeImportOrder(cwbImportOrders, resultCollector, cwbordersList, transorderList, senderProvincesList, senderCitysList, senderCountysList, sendertownsList, consigneeProvincesList, consigneeCitysList, consigneeCountysList, consigneeTownsList, senderCompanyNamesList, delivermanNamesList, repeatOrdersSet, user);
		}
		return map;
	}

	/**
	 *
	 * @Title: extractExbraced
	 * @description 导入文件(分为解析、转化、校验、保存)
	 * @author 刘武强
	 * @date 2015年10月10日下午2:43:34
	 * @param @param f
	 * @param @param resultCollector
	 * @return void
	 * @throws
	 */
	public void extractExbraced(InputStream f, EmbracedImportResult resultCollector, User user) {
		List<EmbracedImportOrderVO> cwbImportOrders = new ArrayList<EmbracedImportOrderVO>();
		List<EmbracedOrderVO> EmbracedOrders = new ArrayList<EmbracedOrderVO>();

		// 解析文件，将解析出来的记录放进cwbImportOrders，正常情况下，所有的记录都会被解析出来，知道结束（有空行或第一个单元格空了）
		for (Object row : this.getRows(f, 2)) {
			if (resultCollector.isStoped()) {
				break;
			}

			try {
				/*
				 * if (!this.getXRowCellType(row,
				 * this.EmbracedColumnIndexMap.size())) {
				 * resultCollector.setResultErrMsg("文件格式不对，请将excel设置为文本格式");
				 * return; }
				 */
				this.changeXRowCellTypeToString(row, this.EmbracedColumnIndexMap.size());
				EmbracedImportOrderVO cwbImportOrder = this.getEmbracedOrderAccordingtoConf(this.EmbracedColumnIndexMap, row);
				cwbImportOrders.add(cwbImportOrder);
			} catch (Exception e) {
				logger.error("", e);
				// 失败订单数+1 前台显示
				// resultCollector.setFailSavcNum(resultCollector.getFailSavcNum()
				// + 1);
			}
		}
		resultCollector.setAnalysisList(cwbImportOrders);
		// 转化为可以保存的数据形式并校验
		Map<String, List<EmbracedOrderVO>> map = this.changeAndCheckDate(cwbImportOrders, user, EmbracedOrders, resultCollector);
		// 将需要插入新增的订单添加到成功的list当中
		if (map.size() > 0) {
			for (int a = 0; a < map.get("insertOrders").size(); a++) {
				EmbracedOrders.add(map.get("insertOrders").get(a));
			}
			for (int a = 0; a < map.get("updateOrders").size(); a++) {
				EmbracedOrders.add(map.get("updateOrders").get(a));
			}
		}
		resultCollector.setSuccessList(EmbracedOrders);

	}

	/**
	 *
	 * @Title: initArr
	 * @description 根据查询条件，获取保存时需要用的数据
	 * @author 刘武强
	 * @date 2015年10月12日下午3:22:29
	 * @param @param cwbImportOrders
	 * @param @param cwbordersArr
	 * @param @param senderProvincesArr
	 * @param @param senderCitysArr
	 * @param @param senderCountysArr
	 * @param @param sendertownsArr
	 * @param @param consigneeProvincesArr
	 * @param @param consigneeCitysArr
	 * @param @param consigneeCountysArr
	 * @param @param senderCompanyNamesArr
	 * @param @param delivermanNamesArr
	 * @return void
	 * @throws
	 */

	private void initArr(List<EmbracedImportOrderVO> cwbImportOrders, StringBuffer cwbordersArr, StringBuffer senderProvincesArr, StringBuffer senderCitysArr, StringBuffer senderCountysArr, StringBuffer sendertownsArr, StringBuffer consigneeProvincesArr, StringBuffer consigneeCitysArr, StringBuffer consigneeCountysArr, StringBuffer senderCompanyNamesArr, StringBuffer delivermanNamesArr, StringBuffer consigneeTownsArr) {
		cwbordersArr.append("('',");
		senderProvincesArr.append("('',");
		senderCitysArr.append("('',");
		senderCountysArr.append("('',");
		sendertownsArr.append("('',");
		consigneeProvincesArr.append("('',");
		consigneeCitysArr.append("('',");
		consigneeCountysArr.append("('',");
		consigneeTownsArr.append("('',");
		senderCompanyNamesArr.append("('',");
		delivermanNamesArr.append("('',");

		for (EmbracedImportOrderVO temp : cwbImportOrders) {
			cwbordersArr.append("'").append(temp.getOrderNo()).append("',");
			senderProvincesArr.append("'").append(temp.getSender_provinceName()).append("',");
			senderCitysArr.append("'").append(temp.getSender_cityName()).append("',");
			senderCountysArr.append("'").append(temp.getSender_countyName()).append("',");
			sendertownsArr.append("'").append(temp.getSender_townName()).append("',");
			consigneeProvincesArr.append("'").append(temp.getConsignee_provinceName()).append("',");
			consigneeCitysArr.append("'").append(temp.getConsignee_cityName()).append("',");
			consigneeCountysArr.append("'").append(temp.getConsignee_countyName()).append("',");
			consigneeTownsArr.append("'").append(temp.getConsignee_townName()).append("',");
			senderCompanyNamesArr.append("'").append(temp.getSender_companyName()).append("',");
			delivermanNamesArr.append("'").append(temp.getDelivermanName()).append("',");
		}
		cwbordersArr.delete(cwbordersArr.length() - 1, cwbordersArr.length()).append(")");
		senderProvincesArr.delete(senderProvincesArr.length() - 1, senderProvincesArr.length()).append(")");
		senderCitysArr.delete(senderCitysArr.length() - 1, senderCitysArr.length()).append(")");
		senderCountysArr.delete(senderCountysArr.length() - 1, senderCountysArr.length()).append(")");
		sendertownsArr.delete(sendertownsArr.length() - 1, sendertownsArr.length()).append(")");
		consigneeProvincesArr.delete(consigneeProvincesArr.length() - 1, consigneeProvincesArr.length()).append(")");
		consigneeCitysArr.delete(consigneeCitysArr.length() - 1, consigneeCitysArr.length()).append(")");
		consigneeCountysArr.delete(consigneeCountysArr.length() - 1, consigneeCountysArr.length()).append(")");
		consigneeTownsArr.delete(consigneeTownsArr.length() - 1, consigneeTownsArr.length()).append(")");
		senderCompanyNamesArr.delete(senderCompanyNamesArr.length() - 1, senderCompanyNamesArr.length()).append(")");
		delivermanNamesArr.delete(delivermanNamesArr.length() - 1, delivermanNamesArr.length()).append(")");
	}

	/**
	 *
	 * @Title: getData
	 * @description 通过解析出来的数据，获取需要保存的数据信息
	 * @author 刘武强
	 * @date 2015年10月12日下午3:38:25
	 * @param @param senderProvincesArr
	 * @param @param cwbordersArr
	 * @param @param senderCitysArr
	 * @param @param senderCountysArr
	 * @param @param sendertownsArr
	 * @param @param consigneeProvincesArr
	 * @param @param consigneeCitysArr
	 * @param @param consigneeCountysArr
	 * @param @param senderCompanyNamesArr
	 * @param @param delivermanNamesArr
	 * @param @param cwbordersList
	 * @param @param senderProvincesList
	 * @param @param senderCitysList
	 * @param @param senderCountysList
	 * @param @param sendertownsList
	 * @param @param consigneeProvincesList
	 * @param @param consigneeCitysList
	 * @param @param consigneeCountysList
	 * @param @param senderCompanyNamesList
	 * @param @param delivermanNamesList
	 * @return void
	 * @throws
	 */
	private void getData(StringBuffer cwbordersArr, StringBuffer senderProvincesArr, StringBuffer senderCitysArr, StringBuffer senderCountysArr, StringBuffer sendertownsArr, StringBuffer consigneeProvincesArr, StringBuffer consigneeCitysArr, StringBuffer consigneeCountysArr, StringBuffer senderCompanyNamesArr, StringBuffer delivermanNamesArr, List<EmbracedOrderVO> cwbordersList, List<AdressVO> senderProvincesList, List<AdressVO> senderCitysList, List<AdressVO> senderCountysList, List<AdressVO> sendertownsList, List<AdressVO> consigneeProvincesList, List<AdressVO> consigneeCitysList, List<AdressVO> consigneeCountysList, List<Customer> senderCompanyNamesList, List<User> delivermanNamesList, User user) {
		cwbordersList = this.expressOrderDao.getOrderBycwbs(cwbordersArr.toString());
		senderProvincesList = this.provinceDAO.getProvincesByProviceNames(senderProvincesArr.toString());
		senderCitysList = this.cityDAO.getCityByCityNames(senderCitysArr.toString());
		senderCountysList = this.countyDAO.getCountysByCountyNames(senderCountysArr.toString());
		sendertownsList = this.townDAO.getTownByTownNames(sendertownsArr.toString());
		consigneeProvincesList = this.provinceDAO.getProvincesByProviceNames(consigneeProvincesArr.toString());
		consigneeCitysList = this.cityDAO.getCityByCityNames(consigneeCitysArr.toString());
		consigneeCountysList = this.countyDAO.getCountysByCountyNames(consigneeCountysArr.toString());
		senderCompanyNamesList = this.customerDAO.getCustomerByCustomernames(senderCompanyNamesArr.toString());
		delivermanNamesList = this.getDeliveryManByDeliveryManNames(delivermanNamesArr.toString(), user);
	}

	/**
	 *
	 * @Title: changeImportOrder
	 * @description 将解析出来的数据转变为补录保存的实体
	 * @author 刘武强
	 * @date 2015年10月12日下午1:42:53
	 * @param @param cwbImportOrders
	 * @param @param resultCollector
	 * @param @param senderProvincesList
	 * @param @param senderCitysList
	 * @param @param senderCountysList
	 * @param @param sendertownsList
	 * @param @param consigneeProvincesList
	 * @param @param consigneeCitysList
	 * @param @param consigneeCountysList
	 * @param @param senderCompanyNamesList
	 * @param @param delivermanNamesList
	 * @param @return
	 * @return List<EmbracedOrderVO>
	 * @throws
	 */
	private synchronized Map<String, List<EmbracedOrderVO>> changeImportOrder(List<EmbracedImportOrderVO> cwbImportOrders, EmbracedImportResult resultCollector, List<EmbracedOrderVO> cwbordersList, List<String> transorderList, List<AdressVO> senderProvincesList, List<AdressVO> senderCitysList, List<AdressVO> senderCountysList, List<AdressVO> sendertownsList, List<AdressVO> consigneeProvincesList, List<AdressVO> consigneeCitysList, List<AdressVO> consigneeCountysList, List<AdressVO> consigneeTownsList, List<Customer> senderCompanyNamesList, List<User> delivermanNamesList, Set<String> repeatOrdersSet, User user) {
		Map<String, List<EmbracedOrderVO>> map = new HashMap<String, List<EmbracedOrderVO>>();
		List<EmbracedOrderVO> cwbCheckedOrders = new ArrayList<EmbracedOrderVO>();
		List<EmbracedImportOrderVO> cwbOrders = new ArrayList<EmbracedImportOrderVO>();
		cwbOrders.addAll(cwbImportOrders);
		List<EmbracedImportErrOrder> failList = new ArrayList<EmbracedImportErrOrder>();
		List<EmbracedOrderVO> cwbUpdateOrders = new ArrayList<EmbracedOrderVO>();
		for (EmbracedImportOrderVO temp : cwbImportOrders) {
			boolean flag = false; // 出错的标志
			EmbracedOrderVO embracedOrdervo = new EmbracedOrderVO();
			EmbracedOrderVO embracedUpdateOrderVO = new EmbracedOrderVO();
			String addressCode = "";
			// 校验运单号是否为空

			if (((temp.getOrderNo() == null) || "".equals(temp.getOrderNo().trim()))) {
				this.createErrNote(temp.getOrderNo(), "运单号为空", failList);
				cwbOrders.remove(temp);
				continue;
			} else if (!ExcelExtractor.isNumOrLetter(temp.getOrderNo().trim())) {
				this.createErrNote(temp.getOrderNo(), "运单号输入不合法：不是由数字和字母组成", failList);
				cwbOrders.remove(temp);
				continue;
			}
			for (String str : repeatOrdersSet) {
				if ((str != null) && str.equals(temp.getOrderNo())) {
					this.createErrNote(temp.getOrderNo(), "订单号重复", failList);
					cwbOrders.remove(temp);
					flag = true; // 已经出错，下面不用在执行
					break;
				}
			}
			if (flag) {
				continue;
			}
			embracedOrdervo.setOrderNo(temp.getOrderNo());
			embracedUpdateOrderVO.setOrderNo(temp.getOrderNo());
			// 校验寄件人是否填写
			if ((temp.getSender_name() == null) || "".equals(temp.getSender_name().trim())) {
				/*
				 * this.createErrNote(temp.getOrderNo(), "寄件人未填写", failList);
				 * cwbOrders.remove(temp); continue;
				 */
			}
			embracedOrdervo.setSender_name(temp.getSender_name());
			embracedUpdateOrderVO.setSender_name(temp.getSender_name());
			// 如果是月结账户，那么单位名称和月结账户必填，且单位名称必须存在于数据库
			//数据库会默认付款方式为月结0元，所有运费必须要填

			if (this.getBoolean(temp.getXianfu(), temp.getDaofu(), temp.getYuejie())) {
				if (ExcelExtractor.isPositiveNumber(temp.getXianfu().trim())) {
					temp.setPayment_method("1");
					embracedOrdervo.setPayment_method("1");
					embracedUpdateOrderVO.setPayment_method("1");
					temp.setFreight(temp.getXianfu().trim());
					embracedOrdervo.setFreight(temp.getXianfu().trim());
					embracedUpdateOrderVO.setFreight(temp.getXianfu().trim());
				} else {
					this.createErrNote(temp.getOrderNo(), "运费（现付）填写不符合要求", failList);
					cwbOrders.remove(temp);
					continue;
				}
			} else if (this.getBoolean(temp.getDaofu(), temp.getXianfu(), temp.getYuejie())) {
				//导入模板不允许有到付类型  modify by vic.liang@pjbest.com	2016-08-08
				this.createErrNote(temp.getOrderNo(), "系统目前不支持快到付类型快递订单", failList);
				cwbOrders.remove(temp);
				continue;
				/*if (ExcelExtractor.isPositiveNumber(temp.getDaofu().trim())) {
					temp.setPayment_method("2");
					embracedOrdervo.setPayment_method("2");
					embracedUpdateOrderVO.setPayment_method("2");
					temp.setFreight(temp.getDaofu().trim());
					embracedOrdervo.setFreight(temp.getDaofu().trim());
					embracedUpdateOrderVO.setFreight(temp.getDaofu().trim());
				} else {
					this.createErrNote(temp.getOrderNo(), "运费（到付）填写不符合要求", failList);
					cwbOrders.remove(temp);
					continue;
				}*/
				//end modify by vic.liang@pjbest.com	2016-08-08
			} else if (this.getBoolean(temp.getYuejie(), temp.getDaofu(), temp.getXianfu())) {
				if (ExcelExtractor.isPositiveNumber(temp.getYuejie().trim())) {
					temp.setPayment_method("0");
					embracedOrdervo.setPayment_method("0");
					embracedUpdateOrderVO.setPayment_method("0");
					temp.setFreight(temp.getYuejie().trim());
					embracedOrdervo.setFreight(temp.getYuejie().trim());
					embracedUpdateOrderVO.setFreight(temp.getYuejie().trim());
				} else {
					this.createErrNote(temp.getOrderNo(), "运费（月结）填写不符合要求", failList);
					cwbOrders.remove(temp);
					continue;
				}
			} else {
				this.createErrNote(temp.getOrderNo(), "运费没有填写或者填写不符合要求", failList);
				cwbOrders.remove(temp);
				continue;
			}
			if ((temp.getPayment_method() == null) || "".equals(temp.getPayment_method().trim())) {
				/*
				 * this.createErrNote(temp.getOrderNo(), "付款方式未填写或填写不符合要求",
				 * failList); cwbOrders.remove(temp); continue;
				 */
			} else if ("0".equals(temp.getPayment_method().trim()) && ((temp.getSender_companyName() == null) || "".equals(temp.getSender_companyName().trim()))) {
				/*
				 * this.createErrNote(temp.getOrderNo(), "月结账户单位名称未填写",
				 * failList); cwbOrders.remove(temp); continue;
				 */
			} else if ("0".equals(temp.getPayment_method().trim()) && ((temp.getMonthly_account_number() == null) || "".equals(temp.getMonthly_account_number().trim()))) {
				/*
				 * this.createErrNote(temp.getOrderNo(), "月结账户客户账号未填写",
				 * failList); cwbOrders.remove(temp); continue;
				 */
			} else if ("0".equals(temp.getPayment_method().trim())) {
				for (Customer senderCompanyName : senderCompanyNamesList) {
					if (temp.getSender_companyName().equals(senderCompanyName.getCompanyname())) {
						embracedOrdervo.setSender_customerid(senderCompanyName.getCustomerid());// 从map中对应得到
																								// 单位名称对应的id
						embracedOrdervo.setSender_No(senderCompanyName.getCustomercode());// 从map中对应得到
																							// 单位名称对应的编码
						embracedUpdateOrderVO.setSender_customerid(senderCompanyName.getCustomerid());// 从map中对应得到
																										// 单位名称对应的id
						embracedUpdateOrderVO.setSender_No(senderCompanyName.getCustomercode());// 从map中对应得到
																								// 单位名称对应的编码
						break;
					}
				}
				if ((embracedOrdervo.getSender_customerid() == null) || "".equals(embracedOrdervo.getSender_customerid())) {
					this.createErrNote(temp.getOrderNo(), "月结账户单位名称在系统中没数据", failList);
					cwbOrders.remove(temp);
					continue;
				}
				embracedOrdervo.setSender_companyName(temp.getSender_companyName());
				embracedUpdateOrderVO.setSender_companyName(temp.getSender_companyName());
				if ("0".equals(temp.getPayment_method().trim())) {
					embracedOrdervo.setMonthly_account_number(temp.getMonthly_account_number());
					embracedUpdateOrderVO.setMonthly_account_number(temp.getMonthly_account_number());
				}
			} else if (temp.getSender_companyName() != null) {// 如果不是月结账号，那么也校验客户，如果校验不通过，则不保存这个字段
																// -----如果非月结，则不校验，不保存
				/*
				 * String companyCode = ""; Long customerid = 0L; for (Customer
				 * senderCompanyName : senderCompanyNamesList) { if
				 * (temp.getSender_companyName
				 * ().equals(senderCompanyName.getCompanyname())) { customerid =
				 * senderCompanyName.getCustomerid();//从map中对应得到 单位名称对应的id
				 * companyCode = senderCompanyName.getCustomercode();//从map中对应得到
				 * 单位名称对应的编码 break; } } if (!"".equals(companyCode)) {
				 * embracedOrdervo.setSender_customerid(customerid);//从map中对应得到
				 * 单位名称对应的id
				 * embracedOrdervo.setSender_No(companyCode);//从map中对应得到
				 * 单位名称对应的编码
				 * embracedOrdervo.setSender_companyName(temp.getSender_companyName
				 * ()); }
				 */
			}

			// 寄件人省是否填写，是否存在与数据库
			if ((temp.getSender_provinceName() == null) || "".equals(temp.getSender_provinceName().trim())) {
				/*
				 * this.createErrNote(temp.getOrderNo(), "寄件人省未填写", failList);
				 * cwbOrders.remove(temp); continue;
				 */
			} else {
				for (AdressVO para : senderProvincesList) {
					if (temp.getSender_provinceName().equals(para.getName())) {
						embracedOrdervo.setSender_provinceid(para.getId() + "");
						embracedOrdervo.setOrigin_adress(para.getId() + "");
						embracedUpdateOrderVO.setSender_provinceid(para.getId() + "");
						embracedUpdateOrderVO.setOrigin_adress(para.getId() + "");
						addressCode = para.getCode();// 用于市的父子关系判断
						break;
					}
				}
				if ((embracedOrdervo.getSender_provinceid() == null) || "".equals(embracedOrdervo.getSender_provinceid().trim())) {
					this.createErrNote(temp.getOrderNo(), "寄件人省在系统中没找到", failList);
					cwbOrders.remove(temp);
					continue;
				}
				embracedOrdervo.setSender_provinceName(temp.getSender_provinceName());
				embracedUpdateOrderVO.setSender_provinceName(temp.getSender_provinceName());
			}
			// 寄件人市是否填写，是否存在与数据库，父子关系是否正确
			if ((temp.getSender_cityName() == null) || "".equals(temp.getSender_cityName().trim())) {
				/*
				 * this.createErrNote(temp.getOrderNo(), "寄件人市未填写", failList);
				 * cwbOrders.remove(temp); continue;
				 */
			} else {
				for (AdressVO para : senderCitysList) {
					if (temp.getSender_cityName().equals(para.getName()) && (para.getParentCode() != null) && para.getParentCode().equals(addressCode)) {
						embracedOrdervo.setSender_cityid(para.getId() + "");
						embracedUpdateOrderVO.setSender_cityid(para.getId() + "");
						addressCode = para.getCode(); // 用于区的父子关系判断
						break;
					}
				}
				if ((embracedOrdervo.getSender_cityid() == null) || "".equals(embracedOrdervo.getSender_cityid().trim())) {
					this.createErrNote(temp.getOrderNo(), "寄件人市在系统中没找到", failList);
					cwbOrders.remove(temp);
					continue;
				}
				embracedOrdervo.setSender_cityName(temp.getSender_cityName());
				embracedUpdateOrderVO.setSender_cityName(temp.getSender_cityName());
			}
			// 寄件人区是否填写，是否存在与数据库，父子关系是否正确
			if ((temp.getSender_countyName() == null) || "".equals(temp.getSender_countyName().trim())) {
				/*
				 * this.createErrNote(temp.getOrderNo(), "寄件人区/县未填写", failList);
				 * cwbOrders.remove(temp); continue;
				 */
			} else {
				for (AdressVO para : senderCountysList) {
					if (temp.getSender_countyName().equals(para.getName()) && (para.getParentCode() != null) && para.getParentCode().equals(addressCode)) {
						embracedOrdervo.setSender_countyid(para.getId() + "");
						embracedUpdateOrderVO.setSender_countyid(para.getId() + "");
						addressCode = para.getCode();// 用于街道的父子关系判断
						break;
					}
				}
				if ((embracedOrdervo.getSender_countyid() == null) || "".equals(embracedOrdervo.getSender_countyid().trim())) {
					this.createErrNote(temp.getOrderNo(), "寄件人区/县在系统中没找到", failList);
					cwbOrders.remove(temp);
					continue;
				}
				embracedOrdervo.setSender_countyName(temp.getSender_countyName());
				embracedUpdateOrderVO.setSender_countyName(temp.getSender_countyName());
			}

			// 寄件人街道是否填写，是否存在与数据库，父子关系是否正确
			if ((temp.getSender_townName() == null) || "".equals(temp.getSender_townName().trim())) {
				/*
				 * this.createErrNote(temp.getOrderNo(), "寄件人街道未填写", failList);
				 * cwbOrders.remove(temp); continue;
				 */
			} else {
				for (AdressVO para : sendertownsList) {
					if (temp.getSender_townName().equals(para.getName()) && (para.getParentCode() != null) && para.getParentCode().equals(addressCode)) {
						embracedOrdervo.setSender_townid(para.getId() + "");
						embracedUpdateOrderVO.setSender_townid(para.getId() + "");
						break;
					}
				}
				if ((embracedOrdervo.getSender_townid() == null) || "".equals(embracedOrdervo.getSender_townid().trim())) {
					this.createErrNote(temp.getOrderNo(), "寄件人街道在系统中没找到", failList);
					cwbOrders.remove(temp);
					continue;
				}
				embracedOrdervo.setSender_townName(temp.getSender_townName());
				embracedUpdateOrderVO.setSender_townName(temp.getSender_townName());
			}
			embracedOrdervo.setSender_adress(temp.getSender_adress());
			embracedUpdateOrderVO.setSender_adress(temp.getSender_adress());
			// 判断寄件人手机号和固话是否至少有一个，且格式是否正确
			if (((temp.getSender_cellphone() == null) || "".equals(temp.getSender_cellphone().trim())) && ((temp.getSender_telephone() == null) || "".equals(temp.getSender_telephone().trim()))) {
				/*
				 * this.createErrNote(temp.getOrderNo(), "寄件人手机和固话至少填写一个",
				 * failList); cwbOrders.remove(temp); continue;
				 */
			}
			if (!((temp.getSender_cellphone() == null) || "".equals(temp.getSender_cellphone().trim())) && !ExcelExtractor.isMobiPhoneNum(temp.getSender_cellphone().trim())) {
				this.createErrNote(temp.getOrderNo(), "寄件人手机号不合法", failList);
				cwbOrders.remove(temp);
				continue;
			}
			embracedOrdervo.setSender_cellphone(temp.getSender_cellphone());
			embracedUpdateOrderVO.setSender_cellphone(temp.getSender_cellphone());

			if (!((temp.getSender_telephone() == null) || "".equals(temp.getSender_telephone().trim())) && !ExcelExtractor.isTelePhoneNum(temp.getSender_telephone().trim())) {
				this.createErrNote(temp.getOrderNo(), "寄件人固话不合法", failList);
				cwbOrders.remove(temp);
				continue;
			}
			embracedOrdervo.setSender_telephone(temp.getSender_telephone());
			embracedOrdervo.setGoods_name(temp.getGoods_name());
			embracedUpdateOrderVO.setSender_telephone(temp.getSender_telephone());
			embracedUpdateOrderVO.setGoods_name(temp.getGoods_name());

			// 数量必须为非负数
			if ((temp.getGoods_number() == null) || "".equals(temp.getGoods_number().trim())) {
				/*
				 * this.createErrNote(temp.getOrderNo(), "寄件人区/县未填写", failList);
				 * cwbOrders.remove(temp); continue;
				 */
			} else {
				if ((temp.getGoods_number() != null) && !"".equals(temp.getGoods_number().trim()) && !ExcelExtractor.isPositiveNumber(temp.getGoods_number().trim())) {
					this.createErrNote(temp.getOrderNo(), "商品数量不为大于0的数", failList);
					cwbOrders.remove(temp);
					continue;
				}
				embracedOrdervo.setGoods_number(temp.getGoods_number());
				embracedUpdateOrderVO.setGoods_number(temp.getGoods_number());
			}
			// 重量必须为非负数
			if ((temp.getCharge_weight() == null) || "".equals(temp.getCharge_weight().trim())) {
				/*
				 * this.createErrNote(temp.getOrderNo(), "寄件人区/县未填写", failList);
				 * cwbOrders.remove(temp); continue;
				 */
			} else {
				if ((temp.getCharge_weight() != null) && !"".equals(temp.getCharge_weight().trim()) && !ExcelExtractor.isPositiveNumber(temp.getCharge_weight().trim())) {
					this.createErrNote(temp.getOrderNo(), "计费重量不为大于0的数", failList);
					cwbOrders.remove(temp);
					continue;
				}
				embracedOrdervo.setCharge_weight(temp.getCharge_weight());
				embracedUpdateOrderVO.setCharge_weight(temp.getCharge_weight());
			}
			embracedOrdervo.setConsignee_name(temp.getConsignee_name());
			embracedUpdateOrderVO.setConsignee_name(temp.getConsignee_name());
			// 收件人省是否在数据库存在
			if ((temp.getConsignee_provinceName() == null) || "".equals(temp.getConsignee_provinceName().trim())) {
				/*
				 * this.createErrNote(temp.getOrderNo(), "寄件人省未填写", failList);
				 * cwbOrders.remove(temp); continue;
				 */
			} else if ((temp.getConsignee_provinceName() != null) && !"".equals(temp.getConsignee_provinceName().trim())) {
				for (AdressVO para : consigneeProvincesList) {
					if (temp.getConsignee_provinceName().equals(para.getName())) {
						embracedOrdervo.setConsignee_provinceid(para.getId() + "");
						embracedOrdervo.setDestination(para.getId() + "");
						embracedUpdateOrderVO.setConsignee_provinceid(para.getId() + "");
						embracedUpdateOrderVO.setDestination(para.getId() + "");

						addressCode = para.getCode();// 用于市的父子关系判断
						break;
					}
				}
				if ((embracedOrdervo.getConsignee_provinceid() == null) || "".equals(embracedOrdervo.getConsignee_provinceid().trim())) {
					this.createErrNote(temp.getOrderNo(), "收件人省在系统中没找到", failList);
					cwbOrders.remove(temp);
					continue;
				}
				embracedOrdervo.setConsignee_provinceName(temp.getConsignee_provinceName());
				embracedUpdateOrderVO.setConsignee_provinceName(temp.getConsignee_provinceName());
			}
			// 收件人市是否填写，是否存在与数据库，父子关系是否正确
			if ((temp.getConsignee_cityName() == null) || "".equals(temp.getConsignee_cityName().trim())) {
				/*
				 * this.createErrNote(temp.getOrderNo(), "收件人市未填写", failList);
				 * cwbOrders.remove(temp); continue;
				 */
			} else {
				for (AdressVO para : consigneeCitysList) {
					if (temp.getConsignee_cityName().equals(para.getName()) && (para.getParentCode() != null) && para.getParentCode().equals(addressCode)) {
						embracedOrdervo.setConsignee_cityid(para.getId() + "");
						embracedUpdateOrderVO.setConsignee_cityid(para.getId() + "");
						addressCode = para.getCode(); // 用于区的父子关系判断
						break;
					}
				}
				if ((embracedOrdervo.getConsignee_cityid() == null) || "".equals(embracedOrdervo.getConsignee_cityid().trim())) {
					this.createErrNote(temp.getOrderNo(), "收件人市在系统中没找到", failList);
					cwbOrders.remove(temp);
					continue;
				}
				embracedOrdervo.setConsignee_cityName(temp.getConsignee_cityName());
				embracedUpdateOrderVO.setConsignee_cityName(temp.getConsignee_cityName());
			}
			// 收件人区是否填写，是否存在与数据库，父子关系是否正确
			if ((temp.getConsignee_countyName() == null) || "".equals(temp.getConsignee_countyName().trim())) {
				/*
				 * this.createErrNote(temp.getOrderNo(), "收件人区/县未填写", failList);
				 * cwbOrders.remove(temp); continue;
				 */
			} else {
				for (AdressVO para : consigneeCountysList) {
					if (temp.getConsignee_countyName().equals(para.getName()) && (para.getParentCode() != null) && para.getParentCode().equals(addressCode)) {
						embracedOrdervo.setConsignee_countyid(para.getId() + "");
						embracedUpdateOrderVO.setConsignee_countyid(para.getId() + "");
						addressCode = para.getCode();// 用于街道的父子关系判断
						break;
					}
				}
				if ((embracedOrdervo.getConsignee_countyid() == null) || "".equals(embracedOrdervo.getConsignee_countyid().trim())) {
					this.createErrNote(temp.getOrderNo(), "收件人区/县在系统中没找到", failList);
					cwbOrders.remove(temp);
					continue;
				}
				embracedOrdervo.setConsignee_countyName(temp.getConsignee_countyName());
				embracedUpdateOrderVO.setConsignee_countyName(temp.getConsignee_countyName());
			}
			// 寄件人街道是否填写，是否存在与数据库，父子关系是否正确
			if ((temp.getConsignee_townName() == null) || "".equals(temp.getConsignee_townName().trim())) {
				/*
				 * this.createErrNote(temp.getOrderNo(), "寄件人街道未填写", failList);
				 * cwbOrders.remove(temp); continue;
				 */
			} else {
				for (AdressVO para : consigneeTownsList) {
					if (temp.getConsignee_townName().equals(para.getName()) && (para.getParentCode() != null) && para.getParentCode().equals(addressCode)) {
						embracedOrdervo.setConsignee_townid(para.getId() + "");
						embracedUpdateOrderVO.setConsignee_townid(para.getId() + "");
						break;
					}
				}
				if ((embracedOrdervo.getConsignee_townid() == null) || "".equals(embracedOrdervo.getConsignee_townid().trim())) {
					this.createErrNote(temp.getOrderNo(), "收件人街道在系统中没找到", failList);
					cwbOrders.remove(temp);
					continue;
				}
				embracedOrdervo.setConsignee_townName(temp.getConsignee_townName());
				embracedUpdateOrderVO.setConsignee_townName(temp.getConsignee_townName());
			}

			if ((temp.getConsignee_adress() == null) || "".equals(temp.getConsignee_adress().trim())) {
				/*
				 * this.createErrNote(temp.getOrderNo(), "收件人详细地址未填写",
				 * failList); cwbOrders.remove(temp); continue;
				 */
			}
			embracedOrdervo.setConsignee_adress(temp.getConsignee_adress());
			embracedUpdateOrderVO.setConsignee_adress(temp.getConsignee_adress());
			// 判断收件人手机号和固话是否至少有一个，且格式是否正确
			if (((temp.getConsignee_cellphone() == null) || "".equals(temp.getConsignee_cellphone().trim())) && ((temp.getConsignee_telephone() == null) || "".equals(temp.getConsignee_telephone()
					.trim()))) {
				/*
				 * this.createErrNote(temp.getOrderNo(), "收件人手机和固话至少填写一个",
				 * failList); cwbOrders.remove(temp); continue;
				 */
			}
			if (!((temp.getConsignee_cellphone() == null) || "".equals(temp.getConsignee_cellphone().trim())) && !ExcelExtractor.isMobiPhoneNum(temp.getConsignee_cellphone().trim())) {
				this.createErrNote(temp.getOrderNo(), "收件人手机号不合法", failList);
				cwbOrders.remove(temp);
				continue;
			}
			embracedOrdervo.setConsignee_cellphone(temp.getConsignee_cellphone());
			embracedUpdateOrderVO.setConsignee_cellphone(temp.getConsignee_cellphone());

			if (!((temp.getConsignee_telephone() == null) || "".equals(temp.getConsignee_telephone().trim())) && !ExcelExtractor.isTelePhoneNum(temp.getConsignee_telephone().trim())) {
				this.createErrNote(temp.getOrderNo(), "收件人固话不合法", failList);
				cwbOrders.remove(temp);
				continue;
			}
			embracedOrdervo.setConsignee_telephone(temp.getConsignee_telephone());
			embracedUpdateOrderVO.setConsignee_telephone(temp.getConsignee_telephone());
			// 揽件员是否存在于本站点
			if ((temp.getDelivermanName() != null) && !"".equals(temp.getDelivermanName().trim())) {
				for (User para : delivermanNamesList) {
					if (temp.getDelivermanName().equals(para.getRealname())) {
						embracedOrdervo.setDelivermanId(para.getUserid() + "");
						embracedUpdateOrderVO.setDelivermanId(para.getUserid() + "");
						break;
					}
				}
				if ((embracedOrdervo.getDelivermanId() == null) || "".equals(embracedOrdervo.getDelivermanId().trim())) {
					this.createErrNote(temp.getOrderNo(), "揽件人在本站所属人员在中没找到", failList);
					cwbOrders.remove(temp);
					continue;
				}
				embracedOrdervo.setDelivermanName(temp.getDelivermanName());
				embracedUpdateOrderVO.setDelivermanName(temp.getDelivermanName());
			} else {
				this.createErrNote(temp.getOrderNo(), "揽件人未填写", failList);
				cwbOrders.remove(temp);
				continue;
			}

			// 代收货款必须为非负数
			if ((temp.getCollection_amount() == null) || "".equals(temp.getCollection_amount().trim())) {
				/*
				 * this.createErrNote(temp.getOrderNo(), "收件人市未填写", failList);
				 * cwbOrders.remove(temp); continue;
				 */
			} else {
				if ((temp.getCollection_amount() != null) && !"".equals(temp.getCollection_amount().trim()) && !ExcelExtractor.isPositiveNumber(temp.getCollection_amount().trim())) {
					this.createErrNote(temp.getOrderNo(), "代收货款不为大于0的数", failList);
					cwbOrders.remove(temp);
					continue;
				}
			}
			if ((temp.getCollection_amount() != null) && !"".equals(temp.getCollection_amount())) {
				embracedOrdervo.setCollection_amount(temp.getCollection_amount());
				embracedOrdervo.setCollection("1");
				embracedUpdateOrderVO.setCollection_amount(temp.getCollection_amount());
				embracedUpdateOrderVO.setCollection("1");

			} else {
				embracedOrdervo.setCollection_amount(0 + "");
				embracedOrdervo.setCollection("0");
				embracedUpdateOrderVO.setCollection_amount(0 + "");
				embracedUpdateOrderVO.setCollection("0");

			}
			// 保价费用必须为非负数
			if ((temp.getInsured_cost() == null) || "".equals(temp.getInsured_cost().trim())) {
				/*
				 * this.createErrNote(temp.getOrderNo(), "收件人市未填写", failList);
				 * cwbOrders.remove(temp); continue;
				 */
			} else {
				if ((temp.getInsured_cost() != null) && !"".equals(temp.getInsured_cost().trim()) && !ExcelExtractor.isPositiveNumber(temp.getInsured_cost().trim())) {
					this.createErrNote(temp.getOrderNo(), "保价费用不为大于0的数", failList);
					cwbOrders.remove(temp);
					continue;
				}
			}
			if ((temp.getInsured_cost() != null) && !"".equals(temp.getInsured_cost().trim())) {
				embracedOrdervo.setInsured_cost(temp.getInsured_cost());
				embracedUpdateOrderVO.setInsured_cost(temp.getInsured_cost());
			} else {
				embracedOrdervo.setInsured_cost(0 + "");
				embracedUpdateOrderVO.setInsured_cost(0 + "");
			}

			// 长宽高 非负数校验
			if ((temp.getGoods_length() != null) && !"".equals(temp.getGoods_length().trim()) && !ExcelExtractor.isPositiveNumber(temp.getGoods_length().trim())) {
				this.createErrNote(temp.getOrderNo(), "商品长度不为大于0的数", failList);
				cwbOrders.remove(temp);
				continue;
			}
			// 如果没有填写，那么给一个0，便于后面kgs计算
			if ((temp.getGoods_length() != null) && !"".equals(temp.getGoods_length().trim())) {
				embracedOrdervo.setGoods_longth(this.toFixTwo(temp.getGoods_length())); // 10.28--刘武强
				embracedUpdateOrderVO.setGoods_longth(this.toFixTwo(temp.getGoods_length())); // 10.28--刘武强
			} else {
				embracedOrdervo.setGoods_longth(0 + ""); // 10.28--刘武强
				embracedUpdateOrderVO.setGoods_longth(0 + ""); // 10.28--刘武强
			}

			if ((temp.getGoods_width() != null) && !"".equals(temp.getGoods_width().trim()) && !ExcelExtractor.isPositiveNumber(temp.getGoods_width().trim())) {
				this.createErrNote(temp.getOrderNo(), "商品宽度不为大于0的数", failList);
				cwbOrders.remove(temp);
				continue;
			}

			// 如果没有填写，那么给一个0，便于后面kgs计算
			if ((temp.getGoods_width() != null) && !"".equals(temp.getGoods_width().trim())) {
				embracedOrdervo.setGoods_width(this.toFixTwo(temp.getGoods_width())); // 10.28--刘武强
				embracedUpdateOrderVO.setGoods_width(this.toFixTwo(temp.getGoods_width())); // 10.28--刘武强
			} else {
				embracedOrdervo.setGoods_width(0 + ""); // 10.28--刘武强
				embracedUpdateOrderVO.setGoods_width(0 + ""); // 10.28--刘武强
			}

			if ((temp.getGoods_high() != null) && !"".equals(temp.getGoods_high().trim()) && !ExcelExtractor.isPositiveNumber(temp.getGoods_high().trim())) {
				this.createErrNote(temp.getOrderNo(), "商品高度不为大于0的数", failList);
				cwbOrders.remove(temp);
				continue;
			}

			// 如果没有填写，那么给一个0，便于后面kgs计算
			if ((temp.getGoods_high() != null) && !"".equals(temp.getGoods_high().trim())) {
				embracedOrdervo.setGoods_height(this.toFixTwo(temp.getGoods_high())); // 10.28--刘武强
				embracedUpdateOrderVO.setGoods_height(this.toFixTwo(temp.getGoods_high())); // 10.28--刘武强
			} else {
				embracedOrdervo.setGoods_height(0 + ""); // 10.28--刘武强
				embracedUpdateOrderVO.setGoods_height(0 + ""); // 10.28--刘武强
			}

			// 保价声明价值 非负数校验
			if ((temp.getInsured_value() != null) && !"".equals(temp.getInsured_value().trim()) && !ExcelExtractor.isPositiveNumber(temp.getInsured_value().trim())) {
				this.createErrNote(temp.getOrderNo(), "保价声明价值不为大于0的数", failList);
				cwbOrders.remove(temp);
				continue;
			}

			// 如果没有填写，那么给一个0，便于后面费用合计计算
			if ((temp.getInsured_value() != null) && !"".equals(temp.getInsured_value().trim())) {
				embracedOrdervo.setInsured_amount(temp.getInsured_value()); // 10.28--刘武强
				embracedUpdateOrderVO.setInsured_amount(temp.getInsured_value()); // 10.28--刘武强
			} else {
				embracedOrdervo.setInsured_amount(0 + ""); // 10.28--刘武强
				embracedUpdateOrderVO.setInsured_amount(0 + ""); // 10.28--刘武强
			}

			// 包装费用校验 非负数
			if ((temp.getPacking_amount() != null) && !"".equals(temp.getPacking_amount().trim()) && !ExcelExtractor.isPositiveNumber(temp.getPacking_amount().trim())) {
				this.createErrNote(temp.getOrderNo(), "包装费用不为大于0的数", failList);
				cwbOrders.remove(temp);
				continue;
			}

			// 如果没有填写，那么给一个0，便于后面费用合计计算
			if ((temp.getPacking_amount() != null) && !"".equals(temp.getPacking_amount().trim())) {
				embracedOrdervo.setPacking_amount(temp.getPacking_amount()); // 10.28--刘武强
				embracedUpdateOrderVO.setPacking_amount(temp.getPacking_amount()); // 10.28--刘武强
			} else {
				embracedOrdervo.setPacking_amount(0 + ""); // 10.28--刘武强
				embracedUpdateOrderVO.setPacking_amount(0 + ""); // 10.28--刘武强
			}

			// 实际重量校验 非负数
			if ((temp.getActual_weight() == null) || "".equals(temp.getActual_weight().trim())) {
				/*
				 * this.createErrNote(temp.getOrderNo(), "实际重量未填写", failList);
				 * cwbOrders.remove(temp); continue;
				 */
			} else {
				if ((temp.getActual_weight() != null) && !"".equals(temp.getActual_weight().trim()) && !ExcelExtractor.isPositiveNumber(temp.getActual_weight().trim())) {
					this.createErrNote(temp.getOrderNo(), "实际重量不为大于0的数", failList);
					cwbOrders.remove(temp);
					continue;
				}
				embracedOrdervo.setActual_weight(temp.getActual_weight()); // 10.28--刘武强
				embracedUpdateOrderVO.setActual_weight(temp.getActual_weight()); // 10.28--刘武强
			}

			// embracedOrdervo.setConsignee_townName(temp.getConsignee_townName());
			// embracedOrdervo.setConsignee_townid("0");
			embracedOrdervo.setGoods_other(temp.getGoods_other());
			embracedOrdervo.setNumber(1 + "");
			embracedUpdateOrderVO.setGoods_other(temp.getGoods_other());
			embracedUpdateOrderVO.setNumber(1 + "");

			// 计算运费合计
			embracedOrdervo.setFreight_total((embracedOrdervo.getFreight() == null ? 0 : Double.parseDouble(embracedOrdervo.getFreight())) + (embracedOrdervo.getInsured_cost() == null ? 0 : Double
					.parseDouble(embracedOrdervo.getInsured_cost())) + (embracedOrdervo.getPacking_amount() == null ? 0 : Double.parseDouble(embracedOrdervo.getPacking_amount())) + "");
			embracedUpdateOrderVO
					.setFreight_total((embracedOrdervo.getFreight() == null ? 0 : Double.parseDouble(embracedOrdervo.getFreight())) + (embracedOrdervo.getInsured_cost() == null ? 0 : Double
							.parseDouble(embracedOrdervo.getInsured_cost())) + (embracedOrdervo.getPacking_amount() == null ? 0 : Double.parseDouble(embracedOrdervo.getPacking_amount())) + "");
			// 计算kgs
			Double kgs = ((embracedOrdervo.getGoods_longth() == null ? 0 : Double.parseDouble(embracedOrdervo.getGoods_longth())) * (embracedOrdervo.getGoods_width() == null ? 0 : Double
					.parseDouble(embracedOrdervo.getGoods_width())) * (embracedOrdervo.getGoods_height() == null ? 0 : Double.parseDouble(embracedOrdervo.getGoods_height()))) / 6000;
			if (kgs != 0) {
				embracedOrdervo.setGoods_kgs(kgs + "");
				embracedUpdateOrderVO.setGoods_kgs(kgs + "");
			}

			//校验运单号是否重复    add by vic.liang@pjbest.com 2016-08-05 
			for (String trans : transorderList) {
				if (trans.equals(temp.getOrderNo())) {//运单号重复
					this.createErrNote(temp.getOrderNo(), "运单与系统订单/运单重复", failList);
					cwbOrders.remove(temp);
					flag = true; // 已经出错，下面不用在执行
					break;
				}
			}
			//this.embracedOrderInputService.checkTranscwb(temp.getOrderNo());//校验录入运单号是否与系统订单号/运单号重复
			//end add by vic.liang@pjbest.com 2016-08-05
			
			
			for (EmbracedOrderVO cwborder : cwbordersList) {
				if ((cwborder.getOrderNo() != null) && cwborder.getOrderNo().equals(temp.getOrderNo())) {
					if ("1".equals(cwborder.getIsadditionflag())) {
						this.createErrNote(temp.getOrderNo(), "运单已经保存并且不可补录", failList);
						cwbOrders.remove(temp);
						flag = true; // 已经出错，下面不用在执行
						break;
					} else {
						if (this.embracedOrderInputService.checkEmbracedVO(embracedUpdateOrderVO, this.checkmMap)) {
							embracedUpdateOrderVO.setIsadditionflag("1");
						} else {
							embracedUpdateOrderVO.setIsadditionflag("0");
						}
						embracedUpdateOrderVO.setInstationhandlerid(cwborder.getInstationhandlerid());
						embracedUpdateOrderVO.setInstationhandlername(cwborder.getInstationhandlername());
						embracedUpdateOrderVO.setInstationdatetime(cwborder.getInstationdatetime());
						embracedUpdateOrderVO.setInstationid(cwborder.getInstationid());
						embracedUpdateOrderVO.setFlowordertype(cwborder.getFlowordertype());

						embracedOrdervo.setInstationhandlerid(cwborder.getInstationhandlerid());
						embracedOrdervo.setInstationhandlername(cwborder.getInstationhandlername());
						embracedOrdervo.setInstationdatetime(cwborder.getInstationdatetime());
						embracedOrdervo.setInstationid(cwborder.getInstationid());
						embracedOrdervo.setFlowordertype(cwborder.getFlowordertype());

						cwbUpdateOrders.add(embracedUpdateOrderVO);
						flag = true; // 已经出错，下面不用在执行
						break;
					}
				} 
			}
			this.setNullToZero(embracedOrdervo, user);
			this.setNullToZero(embracedUpdateOrderVO, user);
			if (flag) {
				continue;
			}

			if (this.embracedOrderInputService.checkEmbracedVO(embracedOrdervo, this.checkmMap)) {
				embracedOrdervo.setIsadditionflag("1");
			} else {
				embracedOrdervo.setIsadditionflag("0");
			}

			cwbCheckedOrders.add(embracedOrdervo);
		}
		resultCollector.setFailList(failList);
		// 订单表中没有的订单需要执行insert语句的订单
		map.put("insertOrders", cwbCheckedOrders);
		// 订单表中已有订单需要执行update语句的订单
		map.put("updateOrders", cwbUpdateOrders);
		return map;
		/*
		 * this.put("OrderNo", 1); /this.put("Sender_name", 2);
		 * /this.put("Sender_companyName", 3);客户名、客户编号、客户id
		 * /this.put("Monthly_account_number", 4);
		 * /this.put("Sender_provinceName", 5);province的id和name
		 * /this.put("Sender_cityName", 6);city的id和name
		 * /this.put("Sender_countyName", 7);county的id和name
		 * /this.put("Sender_townName", 8);town的id和name
		 * /this.put("Sender_adress", 9); /this.put("Sender_cellphone", 10);
		 * /this.put("Sender_telephone", 11); /this.put("Goods_name", 12);
		 * /this.put("Goods_number", 13); /this.put("Goods_weight", 14);
		 * /this.put("Consignee_name", 15); /this.put("Consignee_provinceName",
		 * 16);province的id和name /this.put("Consignee_cityName", 17);city的id和name
		 * /this.put("Consignee_countyName", 18);county的id和name
		 * /this.put("Consignee_adress", 19); /this.put("Consignee_cellphone",
		 * 20); /this.put("Consignee_telephone", 21);
		 * /this.put("DelivermanName", 22);delivermanName和delivermanid
		 * /this.put("Xianfu", 23); /this.put("Daofu", 24); 这三个决定付款方式
		 * /this.put("Yuejie", 25); /this.put("Collection_amount", 26);
		 * /this.put("Insured_cost", 27);
		 */
	}

	/**
	 *
	 * @Title: createErrNote
	 * @description
	 * @author 刘武强
	 * @date 2015年10月12日下午5:52:01
	 * @param @param orderNo
	 * @param @param msg
	 * @param @param failList
	 * @return void
	 * @throws
	 */
	private void createErrNote(String orderNo, String msg, List<EmbracedImportErrOrder> failList) {
		ExcelExtractor.logger.info("cwb:" + orderNo + "   " + msg + "!");
		EmbracedImportErrOrder errOrder = new EmbracedImportErrOrder();
		errOrder.setOrderNo(orderNo);
		errOrder.setErrMsg(msg);
		failList.add(errOrder);
	}

	/**
	 *
	 * @Title: getDeliveryManByDeliveryManNames
	 * @description 通过揽件员的名字的集合，查询其中所有揽件员的信息
	 * @author 刘武强
	 * @date 2015年10月12日下午1:13:36
	 * @param @param DeliveryManNames
	 * @param @return
	 * @return List<User>
	 * @throws
	 */
	public List<User> getDeliveryManByDeliveryManNames(String DeliveryManNames, User user) {
		String roleids = "2,4";
		List<Role> roles = this.roleDAO.getRolesByIsdelivery();
		if ((roles != null) && (roles.size() > 0)) {
			for (Role r : roles) {
				roleids += "," + r.getRoleid();
			}
		}
		List<User> uList = this.userDAO.getUserByRolesAndBranchidAndDeliveryManNames(roleids, user.getBranchid(), DeliveryManNames);
		return uList;
	}

	private String removeZero(String cwb) {
		int end = cwb.indexOf(".0");
		if (end > -1) {
			cwb = cwb.substring(0, end);
		}
		return cwb;
	}

	public abstract String getXRowCellData(Object row, int cwbindex);

	public abstract boolean getXRowCellType(Object row, int num);

	public abstract void changeXRowCellTypeToString(Object row, int num);

	protected abstract String getXRowCellData(Object row, int cwbindex, boolean escapeAddress);

	protected abstract String getXRowCellDateData(Object row, int cwbindex);

	public abstract List<Object> getRows(InputStream f);

	public abstract List<Object> getRows(InputStream f, int rowindex);

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
				logger.error("", e);

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
				// logger.error("", e);
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
			/*
			 * if (penalizeOutGoodsfee.compareTo(new BigDecimal(0)) == -1) {
			 * this
			 * .penalizeOutImportErrorRecordDAO.crePenalizeOutImportErrorRecord
			 * (cwb, systemTime, "货物扣罚金额必须大于0.00！"); return null; }
			 */
			if ((penalizeOutGoodsfee.compareTo(new BigDecimal(1000000000000000l)) == 1) || (String.valueOf(penalizeOutGoodsfee).length() > 14)) {
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
			/*
			 * if (penalizeOutOtherfee.compareTo(new BigDecimal(0)) == -1) {
			 * this
			 * .penalizeOutImportErrorRecordDAO.crePenalizeOutImportErrorRecord
			 * (cwb, systemTime, "其它扣罚金额必须大于0.00！"); return null; }
			 */
			if ((penalizeOutOtherfee.compareTo(new BigDecimal(1000000000000000l)) == 1) || (String.valueOf(penalizeOutOtherfee).length() > 14)) {
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
		// 记录已经存在的限制
		/*
		 * PenalizeInside penalizeInside =
		 * this.punishInsideDao.getPenalizeInsideIsNullCheck(cwb, branchid,
		 * dutypersonid, penalizeOutsmall, penalizeOutGoodsfee,
		 * penalizeOutOtherfee); if (penalizeInside != null) {
		 * this.penalizeOutImportErrorRecordDAO
		 * .crePenalizeOutImportErrorRecord(cwb, systemTime, "该记录已经存在！"); return
		 * null; }
		 */
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
				// logger.error("", e);
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
		List<SalaryImport> salaryImports = this.salaryImportDao.getAllSalaryImports();
		Map<String, Integer> improtMap = this.SetMapImport(salaryImports);
		Map<String, User> userMap = this.SetMapUser(userList);
		int successCounts = 0;
		int failCounts = 0;
		int totalCounts = 0;
		SalaryImportRecord record = new SalaryImportRecord();
		for (Object row : this.getRows(f)) {
			totalCounts++;
			try {
				SalaryFixed salary = this.getSalaryAccordingtoConf(row, userMap, importflag, improtMap);
				if (salary != null) {
					salaryList.add(salary);
				} else {
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
				SalaryFixed salary1 = this.salaryFixedDAO.getSalaryByIdcard(salary.getIdcard());
				if (salary1 != null) {
					this.salaryFixedDAO.deleteSalaryByids(salary1.getId() + "");
				}
				salary.setCreuserid(user.getUserid());
				salary.setImportflag(importflag);
				int nums = this.salaryFixedDAO.creSalaryByRealname(salary);
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
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (SalaryImport sip : salaryImports) {
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
	private SalaryFixed getSalaryAccordingtoConf(Object row, Map<String, User> userMap, long importflag, Map<String, Integer> map) {
		SalaryFixed salary = new SalaryFixed();
		String realname = this.getXRowCellData(row, 1);
		String idcard = this.getXRowCellData(row, 2);
		User user = userMap.get(realname);
		if (user == null) {
			this.salaryErrorDAO.creSalaryError(realname, idcard, "配送员不存在！", importflag);
			return null;
		} else {
			if (!idcard.trim().equals(user.getIdcardno())) {
				this.salaryErrorDAO.creSalaryError(realname, idcard, "身份证号码有误！", importflag);
				return null;
			}
		}
		salary.setBranchid(user.getBranchid());
		salary.setRealname(realname);
		salary.setIdcard(idcard);
		try {
			if ((null != map.get("salarybasic")) && (map.get("salarybasic") == 0)) {
				salary.setSalarybasic(StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 3)));
			}
			if ((null != map.get("salaryjob")) && (map.get("salaryjob") == 0)) {
				salary.setSalaryjob(StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 4)));
			}
			if ((null != map.get("pushcash")) && (map.get("pushcash") == 0)) {
				salary.setPushcash(StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 5)));
			}
			if ((null != map.get("jobpush")) && (map.get("jobpush") == 0)) {
				salary.setJobpush(StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 6)));
			}
			if ((null != map.get("agejob")) && (map.get("agejob") == 0)) {
				salary.setAgejob(StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 7)));
			}
			if ((null != map.get("bonusroom")) && (map.get("bonusroom") == 0)) {
				salary.setBonusroom(StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 8)));
			}
			if ((null != map.get("bonusallday")) && (map.get("bonusallday") == 0)) {
				salary.setBonusallday(StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 9)));
			}
			if ((null != map.get("bonusfood")) && (map.get("bonusfood") == 0)) {
				salary.setBonusfood(StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 10)));
			}
			if ((null != map.get("bonustraffic")) && (map.get("bonustraffic") == 0)) {
				salary.setBonustraffic(StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 11)));
			}
			if ((null != map.get("bonusphone")) && (map.get("bonusphone") == 0)) {
				salary.setBonusphone(StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 12)));
			}
			if ((null != map.get("bonusweather")) && (map.get("bonusweather") == 0)) {
				salary.setBonusweather(StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 13)));
			}
			if ((null != map.get("penalizecancel_import")) && (map.get("penalizecancel_import") == 0)) {
				salary.setPenalizecancel_import(StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 14)));
			}
			if ((null != map.get("bonusother1")) && (map.get("bonusother1") == 0)) {
				salary.setBonusother1(StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 15)));
			}
			if ((null != map.get("bonusother2")) && (map.get("bonusother2") == 0)) {
				salary.setBonusother2(StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 16)));
			}
			if ((null != map.get("bonusother3")) && (map.get("bonusother3") == 0)) {
				salary.setBonusother3(StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 17)));
			}
			if ((null != map.get("bonusother4")) && (map.get("bonusother4") == 0)) {
				salary.setBonusother4(StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 18)));
			}
			if ((null != map.get("bonusother5")) && (map.get("bonusother5") == 0)) {
				salary.setBonusother5(StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 19)));
			}
			if ((null != map.get("bonusother6")) && (map.get("bonusother6") == 0)) {
				salary.setBonusother6(StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 20)));
			}
			if ((null != map.get("overtimework")) && (map.get("overtimework") == 0)) {
				salary.setOvertimework(StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 21)));
			}
			if ((null != map.get("attendance")) && (map.get("attendance") == 0)) {
				salary.setAttendance(StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 22)));
			}
			if ((null != map.get("security")) && (map.get("security") == 0)) {
				salary.setSecurity(StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 23)));
			}
			if ((null != map.get("gongjijin")) && (map.get("gongjijin") == 0)) {
				salary.setGongjijin(StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 24)));
			}
			if ((null != map.get("foul_import")) && (map.get("foul_import") == 0)) {
				salary.setFoul_import(StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 25)));
			}
			if ((null != map.get("dorm")) && (map.get("dorm") == 0)) {
				salary.setDorm(StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 26)));
			}
			if ((null != map.get("penalizeother1")) && (map.get("penalizeother1") == 0)) {
				salary.setPenalizeother1(StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 27)));
			}
			if ((null != map.get("penalizeother2")) && (map.get("penalizeother2") == 0)) {
				salary.setPenalizeother2(StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 28)));
			}
			if ((null != map.get("penalizeother3")) && (map.get("penalizeother3") == 0)) {
				salary.setPenalizeother3(StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 29)));
			}
			if ((null != map.get("penalizeother4")) && (map.get("penalizeother4") == 0)) {
				salary.setPenalizeother4(StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 30)));
			}
			if ((null != map.get("penalizeother5")) && (map.get("penalizeother5") == 0)) {
				salary.setPenalizeother5(StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 31)));
			}
			if ((null != map.get("penalizeother6")) && (map.get("penalizeother6") == 0)) {
				salary.setPenalizeother6(StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 32)));
			}
			if ((null != map.get("imprestother1")) && (map.get("imprestother1") == 0)) {
				salary.setImprestother1(StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 33)));
			}
			if ((null != map.get("imprestother2")) && (map.get("imprestother2") == 0)) {
				salary.setImprestother2(StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 34)));
			}
			if ((null != map.get("imprestother3")) && (map.get("imprestother3") == 0)) {
				salary.setImprestother3(StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 35)));
			}
			if ((null != map.get("imprestother4")) && (map.get("imprestother4") == 0)) {
				salary.setImprestother4(StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 36)));
			}
			if ((null != map.get("imprestother5")) && (map.get("imprestother5") == 0)) {
				salary.setImprestother5(StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 37)));
			}
			if ((null != map.get("imprestother6")) && (map.get("imprestother6") == 0)) {
				salary.setImprestother6(StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 38)));
			}
			if ((null != map.get("carrent")) && (map.get("carrent") == 0)) {
				salary.setCarrent(StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 39)));
			}
			if ((null != map.get("carmaintain")) && (map.get("carmaintain") == 0)) {
				salary.setCarmaintain(StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 40)));
			}
			if ((null != map.get("carfuel")) && (map.get("carfuel") == 0)) {
				salary.setCarfuel(StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 41)));
			}
		} catch (Exception e) {
			this.salaryErrorDAO.creSalaryError(realname, idcard, "数据格式有误", importflag);
			ExcelExtractor.logger.error("配送员工资导入设置异常", e);
			return null;
		}
		return salary;
	}

	public String extractAbnormal(InputStream f, User user, Long systemTime) {
		List<AbnormalImportView> abnormalImportViews = new ArrayList<AbnormalImportView>();
		List<AbnormalType> abnormalTypes = this.abnormalTypeDAO.getAllAbnormalTypeByName();
		Map<String, Long> abnormalMaps = this.getAbnormalTypeMaps(abnormalTypes);
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
				// logger.error("", e);
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
				successCounts++;
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
		AbnormalImportView abnormalImportView = new AbnormalImportView();
		String cwb = this.getXRowCellData(row, 1);
		CwbOrder co = this.cwbDAO.getCwbByCwb(cwb);
		if (co == null) {
			this.penalizeOutImportErrorRecordDAO.crePenalizeOutImportErrorRecord(cwb, systemTime, "订单号不存在！");
			return null;
		} else {
			abnormalImportView.setCwbOrder(co);
		}
		long abnormaltypeid = 0;
		String abnormalTypeString = this.getXRowCellData(row, 2);
		try {
			abnormaltypeid = abnormalTypes.get(abnormalTypeString);
		} catch (Exception e) {
			this.penalizeOutImportErrorRecordDAO.crePenalizeOutImportErrorRecord(cwb, systemTime, "问题件类型不存在！");
			return null;
		}
		String abnormalInfo = this.getXRowCellData(row, 3);
		try {
			if (abnormalInfo.length() > 100) {
				this.penalizeOutImportErrorRecordDAO.crePenalizeOutImportErrorRecord(cwb, systemTime, "问题件说明字数大于100！");
				return null;
			}
		} catch (Exception e) {
			this.penalizeOutImportErrorRecordDAO.crePenalizeOutImportErrorRecord(cwb, systemTime, "问题件说明字数大于100！");
			return null;
		}
		/*
		 * //判断问题件记录是否存在 List<AbnormalOrder>
		 * abnormalOrders=this.abnormalOrderDAO
		 * .checkexcelIsExist(cwb,abnormaltypeid,abnormalInfo); if
		 * ((abnormalOrders != null)&&(abnormalOrders.size()>0)) {
		 * this.penalizeOutImportErrorRecordDAO
		 * .crePenalizeOutImportErrorRecord(cwb, systemTime, "该记录已经存在！"); return
		 * null; }
		 */
		long action = AbnormalWriteBackEnum.ChuangJian.getValue();
		// abnormalOrder的状态为未处理
		long ishandle = AbnormalOrderHandleEnum.weichuli.getValue();
		long isfind = 0;
		Branch branch = this.branchDAO.getBranchByBranchid(user.getBranchid());
		long handleBranch = 0;// 0为没有操作机构
		if (branch != null) {
			handleBranch = branch.getSitetype();
		}
		abnormalImportView.setAbnormalinfo(abnormalInfo);
		abnormalImportView.setAbnormaltypeid(abnormaltypeid);
		abnormalImportView.setAction(action);
		abnormalImportView.setFilepath("");
		abnormalImportView.setHandleBranch(handleBranch);
		abnormalImportView.setIsfind(isfind);
		abnormalImportView.setNowtime(DateTimeUtil.getNowTime());
		abnormalImportView.setQuestionNo("Q" + System.currentTimeMillis());
		abnormalImportView.setUser(user);
		abnormalImportView.setIshandle(ishandle);
		abnormalImportView.setSystemtime(systemTime);
		return abnormalImportView;
	}

	public Map<String, Long> getAbnormalTypeMaps(List<AbnormalType> abnormalTypes) {
		Map<String, Long> abnormalMaps = new HashMap<String, Long>();
		for (Iterator<AbnormalType> iterator = abnormalTypes.iterator(); iterator.hasNext();) {
			AbnormalType abnormalType = iterator.next();
			abnormalMaps.put(abnormalType.getName(), abnormalType.getId());
		}
		return abnormalMaps;
	}

	/*
	 * importflag
	 */
	@Transactional
	public void extractSalaryGather(InputStream input, long importflag, User user, SalaryCount sc) {
		List<SalaryGather> salaryList = new ArrayList<SalaryGather>();
		List<User> userList = this.userDAO.getAllUsers();
		List<SalaryFixed> fixedList = this.salaryFixedDAO.getAllFixed();
		List<SalaryImport> salaryImports = this.salaryImportDao.getAllSalaryImports();// 固定值设置描述
		Map<String, Integer> improtMap = this.SetMapImport(salaryImports);
		Map<String, User> userMap = this.SetMapUser(userList);
		Map<String, SalaryFixed> fixedMap = this.setMapFixed(fixedList);
		int successCounts = 0;
		int failCounts = 0;
		int totalCounts = 0;
		SalaryImportRecord record = new SalaryImportRecord();
		for (Object row : this.getRows(input)) {
			totalCounts++;
			try {
				// 工资计算方法
				SalaryGather salary = this.getSalaryGatherAccordingtoConf(row, userMap, importflag, improtMap, fixedMap, sc);
				if (salary != null) {
					salaryList.add(salary);
				} else {
					failCounts++;
				}
			} catch (Exception e) {
				failCounts++;
				this.salaryErrorDAO.creSalaryError(this.getXRowCellData(row, 1), this.getXRowCellData(row, 1), "未知异常", importflag);
				continue;
			}
		}
		if ((salaryList != null) && !salaryList.isEmpty()) {
			// Set<SalaryGather> sgSet = new HashSet<SalaryGather>();
			Map<Long, SalaryGather> lsMap = new HashMap<Long, SalaryGather>();
			for (SalaryGather sg : salaryList) {
				lsMap.put(sg.getUserid(), sg);
			}
			if ((lsMap != null) && (lsMap.size() > 0)) {
				Collection<SalaryGather> sgColl = lsMap.values();
				for (SalaryGather sg : sgColl) {
					this.salaryGatherDao.cresalaryGather(sg);
				}
			}
		}
		record.setImportFlag(importflag);
		record.setUserid(user.getUserid());
		record.setTotalCounts(totalCounts);
		record.setSuccessCounts(successCounts);
		record.setFailCounts(failCounts);
		this.salaryImportRecordDAO.creSalaryImportRecord(record);
	}

	private Map<String, SalaryFixed> setMapFixed(List<SalaryFixed> fixedList) {
		Map<String, SalaryFixed> fixedMap = new HashMap<String, SalaryFixed>();
		for (SalaryFixed sf : fixedList) {
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
	@SuppressWarnings("unused")
	private SalaryGather getSalaryGatherAccordingtoConf(Object row, Map<String, User> userMap, long importflag, Map<String, Integer> map, Map<String, SalaryFixed> fixedMap, SalaryCount sc) throws Exception {
		SalaryGather salary = new SalaryGather();
		String realname = this.getXRowCellData(row, 1);
		String idcard = this.getXRowCellData(row, 2);
		User user = userMap.get(realname);
		if (sc.getBatchstate() == 1) {
			return null;
		}
		if (user == null) {
			this.salaryErrorDAO.creSalaryError(realname, idcard, "配送员:" + user.getRealname() + "不存在！", importflag);
			return null;
		} else {
			if (sc.getBranchid() != user.getBranchid()) {
				this.salaryErrorDAO.creSalaryError(realname, idcard, "配送员:" + user.getRealname() + "不属于该批次指定站点！", importflag);
				return null;
			}
			if (!idcard.trim().equals(user.getIdcardno())) {
				this.salaryErrorDAO.creSalaryError(realname, idcard, "身份证号码:" + user.getIdcardno() + "有误！", importflag);
				return null;
			}
			if (user.getJiesuanstate() == JiesuanstateEnum.TingzhiJiesuan.getValue()) {
				this.salaryErrorDAO.creSalaryError(realname, idcard, "配送员:" + user.getRealname() + "为离职状态,停止结算！", importflag);
				return null;
			}
		}
		long count = this.salaryGatherDao.getSalarygatherByuidandbid(user.getUserid(), sc.getBatchid());
		if (count > 0) {
			this.salaryErrorDAO.creSalaryError(realname, idcard, "配送员:" + user.getRealname() + "存在已导入工资条信息!", importflag);
			return null;
		}
		salary.setUserid(user.getUserid());// 配送员id
		salary.setBatchid(sc.getBatchid());// 批次编号
		salary.setBranchid(sc.getBranchid());// 直营站点
		salary.setRealname(realname);// 真实姓名
		salary.setIdcard(idcard);// 身份证号
		SalaryFixed sf = fixedMap.get(idcard);
		List<BigDecimal> bdList = new ArrayList<BigDecimal>();
		// 结算单量
		// 小件员在期间内的成功单量(配送，上门退，上门换)
		long accountSingle = this.deliveryStateDAO.getDeliverysCountBytime(sc.getStarttime(), sc.getEndtime(), user.getUserid());
		salary.setAccountSingle(accountSingle);

		BigDecimal salarybasic = BigDecimal.ZERO;
		if ((null != map.get("salarybasic")) && (map.get("salarybasic") == 1)) {// 基本工资
			salarybasic = StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 3));
		} else {
			salarybasic = sf.getSalarybasic();
		}
		salary.setSalarybasic(salarybasic);
		bdList.add(salarybasic);

		BigDecimal salaryjob = BigDecimal.ZERO;
		if ((null != map.get("salaryjob")) && (map.get("salaryjob") == 1)) {// 岗位工资
			salaryjob = StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 4));
		} else {
			salaryjob = sf.getSalaryjob();
		}
		salary.setSalaryjob(salaryjob);
		bdList.add(salaryjob);

		BigDecimal pushcash = BigDecimal.ZERO;
		if ((null != map.get("pushcash")) && (map.get("pushcash") == 1)) {// 绩效奖金pushcash
			pushcash = StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 5));
		} else {
			pushcash = sf.getPushcash();
		}
		salary.setPushcash(pushcash);
		bdList.add(pushcash);

		BigDecimal jobpush = BigDecimal.ZERO;
		if ((null != map.get("jobpush")) && (map.get("jobpush") == 1)) {// 岗位津贴
			jobpush = StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 6));
		} else {
			jobpush = sf.getJobpush();
		}
		salary.setJobpush(jobpush);
		bdList.add(jobpush);

		List<DeliveryState> dsList = this.deliveryStateDAO.getDeliverysBytime(sc.getStarttime(), sc.getEndtime(), user.getUserid());

		String cwbsStr = "";
		if ((dsList != null) && !dsList.isEmpty()) {
			for (DeliveryState ds : dsList) {
				cwbsStr += "'" + ds.getCwb() + "',";
			}
		}
		if (cwbsStr.length() > 0) {
			cwbsStr = cwbsStr.substring(0, cwbsStr.length() - 1);
		}

		// 妥投率
		/*
		 * List<Map> list = new ArrayList<Map>(); List<Map> intemapList =
		 * this.salaryGatherService.getCount(user.getUserid(),
		 * sc.getStarttime(), sc.getEndtime());
		 * if(intemapList!=null&&!intemapList
		 * .isEmpty()&&dsList!=null&&!dsList.isEmpty()){ for(Map
		 * lists:intemapList){ Map maps = new HashMap(); int customerint = 0;
		 * double dbl = 0; BigDecimal bdc = BigDecimal.ZERO; long customerid =
		 * (Long)lists.get(1); double bili = (Double)lists.get(2);
		 * for(DeliveryState ds : dsList){ if(customerid == ds.getCustomerid()){
		 * //获取格式化对象 NumberFormat nt = NumberFormat.getPercentInstance();
		 * //设置百分数精确度2即保留两位小数 nt.setMinimumFractionDigits(2); String biliStr =
		 * nt.format(bili); String[] biliArray = biliStr.split("%"); double dble
		 * = Double.parseDouble(biliArray[0]); Paybusinessbenefits pbbf =
		 * this.paybusinessbenefitsDao.getpbbfBycustomerid(customerid); bdc =
		 * pbbf.getOthersubsidies(); String pbbfStr =
		 * pbbf.getPaybusinessbenefits(); String[] strArray =
		 * pbbfStr.split("\\|"); for(String str : strArray){ String[] strarr =
		 * str.substring(0,str.indexOf("=")).split("~"); String starr =
		 * str.substring(str.indexOf("=")+1, str.length()).replace(" ", "");
		 * if(dble>=Double.parseDouble(strarr[0].replace(" ",
		 * ""))&&dble<=Double.parseDouble(strarr[1].replace(" ", ""))){ dbl =
		 * Double.parseDouble(starr); }else{ continue; } } customerint++; }else{
		 * continue; } } maps.put(1, customerint); maps.put(2, dbl); maps.put(3,
		 * bdc); list.add(maps); customerint = 0; dbl = 0; bdc =
		 * BigDecimal.ZERO; } } //kpi业务补助(总和) BigDecimal bd1 = BigDecimal.ZERO;
		 * //其他补助(总和) BigDecimal bd2 = BigDecimal.ZERO;
		 * if(list!=null&&!list.isEmpty()){ for(Map m : list){ if(m.size()>0){
		 * int mLong = (Integer)(m.get(1)); double mDouble = (Double)(m.get(2));
		 * bd1 = bd1.add(new BigDecimal(mDouble).multiply(new
		 * BigDecimal(mLong))); BigDecimal dbbdc = (BigDecimal)(m.get(3)); bd2 =
		 * bd2.add(dbbdc.multiply(new BigDecimal(mLong))); } } }
		 */

		List<Map> list = new ArrayList<Map>();
		List<BigDecimal> bdecList = this.salaryGatherService.getKpiOthers(user.getUserid(), sc.getStarttime(), sc.getEndtime());
		// kpi业务补助(总和)
		BigDecimal bd1 = BigDecimal.ZERO;
		// 其他补助(总和)
		BigDecimal bd2 = BigDecimal.ZERO;
		if ((bdecList != null) && !bdecList.isEmpty()) {
			bd1 = bdecList.get(0);
			bd2 = bdecList.get(1);
		}

		// cwbsStr
		if ("".equals(cwbsStr)) {
			cwbsStr = "''";
		}

		List<CwbOrder> cwbList = this.cwbDAO.getCwbsBycwbs(cwbsStr);

		BigDecimal bds = BigDecimal.ZERO;
		// 减去kpi+其他派费总和之外的总和
		bds = this.salaryGatherService.getSalarypush(user.getPfruleid(), cwbList);
		// 最终计件配送费
		// bds+业务kpi+其他派费
		BigDecimal bdss = bds.add(bd1).add(bd2);

		// 调整额度
		BigDecimal tiaozheng = (user.getBasicfee().add(user.getAreafee())).multiply(new BigDecimal(user.getFallbacknum())).subtract(salarybasic);// 单件标准费用*保底单量-基本工资

		// 提成
		BigDecimal salarypush = BigDecimal.ZERO;
		salarypush = bdss.subtract(salarybasic.add(tiaozheng));// 提成=寄件配送费-（基本工资+调整额度）
		int inte = salarypush.compareTo(BigDecimal.ZERO);
		if (inte == -1) {
			salarypush = new BigDecimal("0.00");
		}
		salary.setSalarypush(salarypush);
		bdList.add(salarypush);

		BigDecimal agejob = BigDecimal.ZERO;
		if ((null != map.get("agejob")) && (map.get("agejob") == 1)) {// 工龄
			agejob = StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 7));
		} else {
			agejob = sf.getAgejob();
		}
		salary.setAgejob(agejob);
		bdList.add(agejob);

		BigDecimal bonusroom = BigDecimal.ZERO;
		if ((null != map.get("bonusroom")) && (map.get("bonusroom") == 1)) {// 住房补贴
			bonusroom = StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 8));
		} else {
			bonusroom = sf.getBonusroom();
		}
		salary.setBonusroom(bonusroom);
		bdList.add(bonusroom);

		BigDecimal bonusallday = BigDecimal.ZERO;
		if ((null != map.get("bonusallday")) && (map.get("bonusallday") == 1)) {// 全勤补贴
			bonusallday = StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 9));
		} else {
			bonusallday = sf.getBonusallday();
		}
		salary.setBonusallday(bonusallday);
		bdList.add(bonusallday);

		BigDecimal bonusfood = BigDecimal.ZERO;
		if ((null != map.get("bonusfood")) && (map.get("bonusfood") == 1)) {// 餐费补贴
			bonusfood = StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 10));
		} else {
			bonusfood = sf.getBonusfood();
		}
		salary.setBonusfood(bonusfood);
		bdList.add(bonusfood);

		BigDecimal bonustraffic = BigDecimal.ZERO;
		if ((null != map.get("bonustraffic")) && (map.get("bonustraffic") == 1)) {// 交通补贴
			bonustraffic = StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 11));
		} else {
			bonustraffic = sf.getBonustraffic();
		}
		salary.setBonustraffic(bonustraffic);
		bdList.add(bonustraffic);

		BigDecimal bonusphone = BigDecimal.ZERO;
		if ((null != map.get("bonusphone")) && (map.get("bonusphone") == 1)) {// 通讯补贴
			bonusphone = StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 12));
		} else {
			bonusphone = sf.getBonusphone();
		}
		salary.setBonusphone(bonusphone);
		bdList.add(bonusphone);

		BigDecimal bonusweather = BigDecimal.ZERO;
		if ((null != map.get("bonusweather")) && (map.get("bonusweather") == 1)) {// 高温寒冷补贴
			bonusweather = StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 13));
		} else {
			bonusweather = sf.getBonusweather();
		}
		salary.setBonusweather(bonusweather);
		bdList.add(bonusweather);

		// 扣款撤销，违纪扣款扣罚，货损赔偿部分取值
		List<DeliveryState> deliList = this.deliveryStateDAO.getLinghuoDeliveryStates(sc.getStarttime(), sc.getEndtime(), user.getUserid());
		String cwbsStrs = "";
		if ((deliList != null) && !deliList.isEmpty()) {
			for (DeliveryState ds : deliList) {
				cwbsStrs += "'" + ds.getCwb() + "',";
			}
		}
		if (cwbsStrs.length() > 0) {
			cwbsStrs = cwbsStrs.substring(0, cwbsStrs.length() - 1);
		} else {
			cwbsStrs = "''";
		}
		// 扣款撤销
		BigDecimal penalizecancel = BigDecimal.ZERO;
		penalizecancel = this.punishInsideDao.getKouFaPrice(sc.getStarttime(), sc.getEndtime(), user.getUserid(), 1);
		salary.setPenalizecancel(penalizecancel);
		bdList.add(penalizecancel);
		// 违纪扣款扣罚
		BigDecimal foul = BigDecimal.ZERO;
		foul = this.punishInsideDao.getKouFaPrice(sc.getStarttime(), sc.getEndtime(), user.getUserid(), 3);
		salary.setFoul(foul);
		bdList.add(foul.multiply(new BigDecimal(-1)));
		// 货损赔偿
		BigDecimal goods = BigDecimal.ZERO;
		goods = this.punishInsideDao.getKouFaPrice(sc.getStarttime(), sc.getEndtime(), user.getUserid(), 2);
		salary.setGoods(goods);
		bdList.add(goods.multiply(new BigDecimal(-1)));

		BigDecimal penalizecancel_import = BigDecimal.ZERO;
		if ((null != map.get("penalizecancel_import")) && (map.get("penalizecancel_import") == 1)) {// 扣款撤销(导入)
			penalizecancel_import = StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 14));
		} else {
			penalizecancel_import = sf.getPenalizecancel_import();
		}
		salary.setPenalizecancel_import(penalizecancel_import);
		bdList.add(penalizecancel_import);

		BigDecimal bonusother1 = BigDecimal.ZERO;
		if ((null != map.get("bonusother1")) && (map.get("bonusother1") == 1)) {// 其他补贴
			bonusother1 = StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 15));
		} else {
			bonusother1 = sf.getBonusother1();
		}
		salary.setBonusother1(bonusother1);
		bdList.add(bonusother1);

		BigDecimal bonusother2 = BigDecimal.ZERO;
		if ((null != map.get("bonusother2")) && (map.get("bonusother2") == 1)) {// 其他补贴2
			bonusother2 = StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 16));
		} else {
			bonusother2 = sf.getBonusother2();
		}
		salary.setBonusother2(bonusother2);
		bdList.add(bonusother2);

		BigDecimal bonusother3 = BigDecimal.ZERO;
		if ((null != map.get("bonusother3")) && (map.get("bonusother3") == 1)) {// 其他补贴3
			bonusother3 = StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 17));
		} else {
			bonusother3 = sf.getBonusother3();
		}
		salary.setBonusother3(bonusother3);
		bdList.add(bonusother3);

		BigDecimal bonusother4 = BigDecimal.ZERO;
		if ((null != map.get("bonusother4")) && (map.get("bonusother4") == 1)) {// 其他补贴4
			bonusother4 = StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 18));
		} else {
			bonusother4 = sf.getBonusother4();
		}
		salary.setBonusother4(bonusother4);
		bdList.add(bonusother4);

		BigDecimal bonusother5 = BigDecimal.ZERO;
		if ((null != map.get("bonusother5")) && (map.get("bonusother5") == 1)) {// 其他补贴5
			bonusother5 = StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 19));
		} else {
			bonusother5 = sf.getBonusother5();
		}
		salary.setBonusother5(bonusother5);
		bdList.add(bonusother5);

		BigDecimal bonusother6 = BigDecimal.ZERO;
		if ((null != map.get("bonusother6")) && (map.get("bonusother6") == 1)) {// 其他补贴6
			bonusother6 = StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 20));
		} else {
			bonusother6 = sf.getBonusother6();
		}
		salary.setBonusother6(bonusother6);
		bdList.add(bonusother6);

		BigDecimal overtimework = BigDecimal.ZERO;
		if ((null != map.get("overtimework")) && (map.get("overtimework") == 1)) {// 加班费
			overtimework = StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 21));
		} else {
			overtimework = sf.getOvertimework();
		}
		salary.setOvertimework(overtimework);
		bdList.add(overtimework);

		BigDecimal attendance = BigDecimal.ZERO;
		if ((null != map.get("attendance")) && (map.get("attendance") == 1)) {// 考勤扣款
			attendance = StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 22));
		} else {
			attendance = sf.getAttendance();
		}
		salary.setAttendance(attendance);
		bdList.add(attendance.multiply(new BigDecimal(-1)));

		BigDecimal security = BigDecimal.ZERO;
		if ((null != map.get("security")) && (map.get("security") == 1)) {// 个人社保扣款
			security = StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 23));
		} else {
			security = sf.getSecurity();
		}
		salary.setSecurity(security);
		bdList.add(security.multiply(new BigDecimal(-1)));

		BigDecimal gongjijin = BigDecimal.ZERO;
		if ((null != map.get("gongjijin")) && (map.get("gongjijin") == 1)) {// 个人公积金扣款
			gongjijin = StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 24));
		} else {
			gongjijin = sf.getGongjijin();
		}
		salary.setGongjijin(gongjijin);
		bdList.add(gongjijin.multiply(new BigDecimal(-1)));

		BigDecimal foul_import = BigDecimal.ZERO;
		if ((null != map.get("foul_import")) && (map.get("foul_import") == 1)) {// 违纪扣款扣罚(导入)
			foul_import = StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 25));
		} else {
			foul_import = sf.getFoul_import();
		}
		salary.setFoul_import(foul_import);
		bdList.add(foul_import.multiply(new BigDecimal(-1)));

		BigDecimal dorm = BigDecimal.ZERO;
		if ((null != map.get("dorm")) && (map.get("dorm") == 1)) {// 宿舍费用
			dorm = StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 26));
		} else {
			dorm = sf.getDorm();
		}
		salary.setDorm(dorm);
		bdList.add(dorm.multiply(new BigDecimal(-1)));

		BigDecimal penalizeother1 = BigDecimal.ZERO;
		if ((null != map.get("penalizeother1")) && (map.get("penalizeother1") == 1)) {// 其他扣罚
			penalizeother1 = StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 27));
		} else {
			penalizeother1 = sf.getPenalizeother1();
		}
		salary.setPenalizeother1(penalizeother1);
		bdList.add(penalizeother1.multiply(new BigDecimal(-1)));

		BigDecimal penalizeother2 = BigDecimal.ZERO;
		if ((null != map.get("penalizeother2")) && (map.get("penalizeother2") == 1)) {// 其他扣罚2
			penalizeother2 = StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 28));
		} else {
			penalizeother2 = sf.getPenalizeother2();
		}
		salary.setPenalizeother2(penalizeother2);
		bdList.add(penalizeother2.multiply(new BigDecimal(-1)));

		BigDecimal penalizeother3 = BigDecimal.ZERO;
		if ((null != map.get("penalizeother3")) && (map.get("penalizeother3") == 1)) {// 其他扣罚3
			penalizeother3 = StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 29));
		} else {
			penalizeother3 = sf.getPenalizeother3();
		}
		salary.setPenalizeother3(penalizeother3);
		bdList.add(penalizeother3.multiply(new BigDecimal(-1)));

		BigDecimal penalizeother4 = BigDecimal.ZERO;
		if ((null != map.get("penalizeother4")) && (map.get("penalizeother4") == 1)) {// 其他扣罚4
			penalizeother4 = StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 30));
		} else {
			penalizeother4 = sf.getPenalizeother4();
		}
		salary.setPenalizeother4(penalizeother4);
		bdList.add(penalizeother4.multiply(new BigDecimal(-1)));

		BigDecimal penalizeother5 = BigDecimal.ZERO;
		if ((null != map.get("penalizeother5")) && (map.get("penalizeother5") == 1)) {// 其他扣罚5
			penalizeother5 = StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 31));
		} else {
			penalizeother5 = sf.getPenalizeother5();
		}
		salary.setPenalizeother5(penalizeother5);
		bdList.add(penalizeother5.multiply(new BigDecimal(-1)));

		BigDecimal penalizeother6 = BigDecimal.ZERO;
		if ((null != map.get("penalizeother6")) && (map.get("penalizeother6") == 1)) {// 其他扣罚6
			penalizeother6 = StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 32));
		} else {
			penalizeother6 = sf.getPenalizeother6();
		}
		salary.setPenalizeother6(penalizeother6);
		bdList.add(penalizeother6.multiply(new BigDecimal(-1)));

		BigDecimal imprestother1 = BigDecimal.ZERO;
		if ((null != map.get("imprestother1")) && (map.get("imprestother1") == 1)) {// 其他预付款
			imprestother1 = StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 33));
		} else {
			imprestother1 = sf.getImprestother1();
		}
		salary.setImprestother1(imprestother1);
		bdList.add(imprestother1.multiply(new BigDecimal(-1)));

		BigDecimal imprestother2 = BigDecimal.ZERO;
		if ((null != map.get("imprestother2")) && (map.get("imprestother2") == 1)) {// 其他预付款2
			imprestother2 = StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 34));
		} else {
			imprestother2 = sf.getImprestother2();
		}
		salary.setImprestother2(imprestother2);
		bdList.add(imprestother2.multiply(new BigDecimal(-1)));

		BigDecimal imprestother3 = BigDecimal.ZERO;
		if ((null != map.get("imprestother3")) && (map.get("imprestother3") == 1)) {// 其他预付款3
			imprestother3 = StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 35));
		} else {
			imprestother3 = sf.getImprestother3();
		}
		salary.setImprestother3(imprestother3);
		bdList.add(imprestother3.multiply(new BigDecimal(-1)));

		BigDecimal imprestother4 = BigDecimal.ZERO;
		if ((null != map.get("imprestother4")) && (map.get("imprestother4") == 1)) {// 其他预付款4
			imprestother4 = StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 36));
		} else {
			imprestother4 = sf.getImprestother5();
		}
		salary.setImprestother4(imprestother4);
		bdList.add(imprestother4.multiply(new BigDecimal(-1)));

		BigDecimal imprestother5 = BigDecimal.ZERO;
		if ((null != map.get("imprestother5")) && (map.get("imprestother5") == 1)) {// 其他预付款5
			imprestother5 = StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 37));
		} else {
			imprestother5 = sf.getImprestother5();
		}
		salary.setImprestother5(imprestother5);
		bdList.add(imprestother5.multiply(new BigDecimal(-1)));

		BigDecimal imprestother6 = BigDecimal.ZERO;
		if ((null != map.get("imprestother6")) && (map.get("imprestother6") == 1)) {// 其他预付款6
			imprestother6 = StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 38));
		} else {
			imprestother6 = sf.getImprestother6();
		}
		salary.setImprestother6(imprestother6);
		bdList.add(imprestother6.multiply(new BigDecimal(-1)));

		BigDecimal carrent = BigDecimal.ZERO;
		if ((null != map.get("carrent")) && (map.get("carrent") == 1)) {// 租用车辆费用
			carrent = StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 39));
		} else {
			carrent = sf.getCarrent();
		}
		salary.setCarrent(carrent);
		bdList.add(carrent);

		BigDecimal carmaintain = BigDecimal.ZERO;
		if ((null != map.get("carmaintain")) && (map.get("carmaintain") == 1)) {// 车子维修给用
			carmaintain = StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 40));
		} else {
			carmaintain = sf.getCarmaintain();
		}
		salary.setCarmaintain(carmaintain);
		bdList.add(carmaintain);

		BigDecimal carfuel = BigDecimal.ZERO;
		if ((null != map.get("carfuel")) && (map.get("carfuel") == 1)) {// 油/电费用
			carfuel = StringUtil.nullOrEmptyConvertToBigDecimal(this.getXRowCellData(row, 41));
		} else {
			carfuel = sf.getCarfuel();
		}
		salary.setCarfuel(carfuel);
		bdList.add(carfuel);

		// 应发金额
		BigDecimal salaryaccrual = BigDecimal.ZERO;
		if ((bdList != null) && !bdList.isEmpty()) {
			for (BigDecimal bd : bdList) {
				salaryaccrual = salaryaccrual.add(bd);
			}
		}

		// 货物预付款
		BigDecimal imprestgoods = BigDecimal.ZERO;
		// bdList.add(imprestgoods.multiply(new BigDecimal(-1)));
		int value = salaryaccrual.compareTo(new BigDecimal(1000));
		if (value != -1) {
			BigDecimal bmb = user.getMaxcutpayment().subtract(user.getBasicadvance().add(user.getLateradvance()));
			double dbmb = bmb.doubleValue();
			double fixed = user.getFixedadvance().doubleValue();
			if (dbmb >= fixed) {
				BigDecimal bddown = new BigDecimal(fixed);
				salary.setImprestgoods(bddown);
				salaryaccrual = salaryaccrual.subtract(bddown);
				salary.setSalaryaccrual(salaryaccrual);
			} else if (dbmb <= 0) {
				salary.setImprestgoods(imprestgoods);
				salary.setSalaryaccrual(salaryaccrual);
			} else {
				salary.setImprestgoods(bmb);
				salaryaccrual = salaryaccrual.subtract(bmb);
				salary.setSalaryaccrual(salaryaccrual);
			}
		}
		// 当工资金额小于1000时
		else {
			salary.setImprestgoods(imprestgoods);// 货物预付款
			salary.setSalaryaccrual(salaryaccrual);// 应发工资
		}

		// 个税
		BigDecimal tax = BigDecimal.ZERO;
		tax = this.salaryGatherService.getTax(salaryaccrual);
		salary.setTax(tax);

		// 实发金额
		BigDecimal salarys = BigDecimal.ZERO;
		salarys = salaryaccrual.subtract(tax);
		salary.setSalary(salarys);

		return salary;
	}

	/**
	 *
	 * @Title: getSessionUser
	 * @description 获取登录人的信息
	 * @author 刘武强
	 * @date 2015年10月12日下午2:09:58
	 * @param @return
	 * @return User
	 * @throws
	 */
	/*
	 * protected User getSessionUser() { return
	 * this.smbracedOrderInputService.getSessionUser(); }
	 */

	/**
	 *
	 * @Title: isMobiPhoneNum
	 * @description 判断手机号是否合法（与js一致）
	 * @author 刘武强
	 * @date 2015年10月12日下午9:36:57
	 * @param @param telNum
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public static boolean isMobiPhoneNum(String telNum) {
		String regex = "^([+][8][6])?1\\d{10}$";
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(telNum);
		return m.matches();
	}

	/**
	 *
	 * @Title: isOnlyNum
	 * @description 判断是否全为数字
	 * @author 刘武强
	 * @date  2016年1月20日下午2:16:18
	 * @param  @param telNum
	 * @param  @return
	 * @return  boolean
	 * @throws
	 */
	public static boolean isOnlyNum(String telNum) {
		String regex = "^[0-9]*$";
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(telNum);
		return m.matches();
	}
	
	/**
	 *
	 * @Title: isNumOrLetter
	 * @description 判断是否为数字和字母组合
	 * @param  @return
	 * @return  boolean
	 * @throws
	 */
	public static boolean isNumOrLetter(String telNum) {
		String regex = "^[0-9A-Za-z]*$";
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(telNum);
		return m.matches();
	}
	
	/**
	 *
	 * @Title: isTelePhoneNum
	 * @description 判断固话是否合法（与js一致）
	 * @author 刘武强
	 * @date 2015年10月12日下午9:40:01
	 * @param @param telNum
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public static boolean isTelePhoneNum(String telNum) {
		String regex = "^\\d{3,4}[-]\\d{7,8}$";
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(telNum);
		return m.matches();
	}

	/**
	 *
	 * @Title: isPositiveNumber
	 * @description 校验输入内容为非负数
	 * @author 刘武强
	 * @date 2015年10月12日下午9:48:27
	 * @param @param value
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public static boolean isPositiveNumber(String value) {
		String regex = "^(0|[1-9]\\d*|\\d+\\.\\d+)$";
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(value);
		return m.matches();
	}

	/**
	 *
	 * @Title: is
	 * @description 校验输入内容位0或正整数的方法
	 * @author 刘武强
	 * @date 2015年11月3日下午5:03:15
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public static boolean isPositive(String value) {
		String regex = "^(0|[1-9]\\d*)$";
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(value);
		return m.matches();
	}

	/**
	 *
	 * @Title: getRepeatOrderNo
	 * @description 从list找出有重复（订单号重复）的订单号
	 * @author 刘武强
	 * @date 2015年10月13日上午8:23:00
	 * @param @param list
	 * @param @return
	 * @return List<String>
	 * @throws
	 */
	public Set<String> getRepeatOrderNo(List<EmbracedImportOrderVO> list) {
		Set<String> repeatOrdersSet = new HashSet<String>();
		String temp = "";
		for (int i = 0; i < (list.size() - 1); i++) {
			temp = list.get(i).getOrderNo();
			for (int j = i + 1; j < list.size(); j++) {
				if (temp.equals(list.get(j).getOrderNo())) {
					repeatOrdersSet.add(temp);
				}
			}
		}
		return repeatOrdersSet;
	}

	public void setNullToZero(EmbracedOrderVO vo, User user) {
		if (vo.getSender_provinceid() == null) {
			vo.setSender_provinceid("0");
		}
		if (vo.getSender_cityid() == null) {
			vo.setSender_cityid("0");
		}
		if (vo.getSender_countyid() == null) {
			vo.setSender_countyid("0");
		}
		if (vo.getSender_townid() == null) {
			vo.setSender_townid("0");
		}
		if (vo.getSender_customerid() == null) {
			vo.setSender_customerid(1000L);
		}
		if (vo.getConsignee_provinceid() == null) {
			vo.setConsignee_provinceid("0");
		}
		if (vo.getConsignee_cityid() == null) {
			vo.setConsignee_cityid("0");
		}
		if (vo.getConsignee_countyid() == null) {
			vo.setConsignee_countyid("0");
		}
		if (vo.getConsignee_townid() == null) {
			vo.setConsignee_townid("0");
		}
		if (vo.getConsignee_customerid() == null) {
			vo.setConsignee_customerid(1000L);
		}
		if (vo.getGoods_longth() == null) {
			vo.setGoods_longth("0");
		}
		if (vo.getGoods_height() == null) {
			vo.setGoods_height("0");
		}

		if (vo.getGoods_width() == null) {
			vo.setGoods_width("0");
		}

		if (vo.getGoods_kgs() == null) {
			vo.setGoods_kgs("0");
		}

		if (vo.getCollection() == null) {
			vo.setCollection("0");
		}

		if (vo.getPacking_amount() == null) {
			vo.setPacking_amount("0.00");
		}

		if (vo.getInsured() == null) {
			vo.setInsured("0");
		}

		if (vo.getInsured_amount() == null) {
			vo.setInsured_amount("0.00");
		}

		if (vo.getInsured_cost() == null) {
			vo.setInsured_cost("0.00");
		}

		if (vo.getCharge_weight() == null) {
			vo.setCharge_weight("0.00");
		}

		if (vo.getActual_weight() == null) {
			vo.setActual_weight("0.00");
		}

		if (vo.getFreight_total() == null) {
			vo.setFreight_total("0.00");
		}

		if (vo.getFreight() == null) {
			vo.setFreight("0.00");
		}

		if (vo.getPayment_method() == null) {
			vo.setPayment_method("1");
		}

		if (vo.getDelivermanId() == null) {
			vo.setDelivermanId("0");
		}

		if (vo.getFlowordertype() == null) {
			vo.setFlowordertype(FlowOrderTypeEnum.LanJianRuZhan.getValue() + "");
		}
		//
		Branch branch = this.branchDAO.getBranchByBranchid(user.getBranchid());
		branch = branch == null ? new Branch() : branch;
		if (vo.getInstationhandlerid() == null) {
			vo.setInstationhandlerid(user.getUserid() + "");
		}
		if (vo.getInstationhandlername() == null) {
			vo.setInstationhandlername(user.getUsername());
		}
		if (vo.getInstationid() == null) {
			vo.setInstationid((int) (branch.getBranchid()));
		}
		if (vo.getInstationname() == null) {
			vo.setInstationname(branch.getBranchname());
		}
		if (vo.getInstationdatetime() == null) {
			Date date = new Date();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dd = df.format(date);
			vo.setInstationdatetime(dd);
		}

	}

	/**
	 * 调用tps运单反馈接口 ----揽件入站
	 *
	 * @param orders
	 */
	private void executeTpsTransInterface(EmbracedOrderVO order, User user) {
		if (order != null) {
			ExpressOperationInfo paramObj = new ExpressOperationInfo(ExpressOperationEnum.TransNOFeedBack);
			PjTransportFeedbackRequest transNoFeedBack = new PjTransportFeedbackRequest();
			Branch branch = this.branchDAO.getBranchByBranchid(user.getBranchid());
			transNoFeedBack.setTransportNo(order.getOrderNo());
			transNoFeedBack.setOperateOrg(branch.getTpsbranchcode());
			transNoFeedBack.setOperater(user.getRealname());
			transNoFeedBack.setOperateTime(System.currentTimeMillis());
			transNoFeedBack.setOperateType(FeedbackOperateTypeEnum.InboundScan.getValue());
			transNoFeedBack.setReason("");

			/*
			 * //拼接描述 JoinMessageVO contextVar = new JoinMessageVO();
			 * contextVar.
			 * setOperationType(TpsOperationEnum.ArrivalScan.getValue(
			 * ));//揽件入站对应入站扫描
			 * contextVar.setStation(branch.getBranchname());//站点名称
			 * contextVar.setOperator(user.getRealname());
			 * contextVar.connectMessage();
			 * transNoFeedBack.setTransportDetail(contextVar.getTrackMessage());
			 */
			paramObj.setTransNoFeedBack(transNoFeedBack);
			// 发送JMS消息
			this.tpsInterfaceExecutor.executTpsInterface(paramObj);
		}
	}

	public String toFixTwo(String number) {
		return String.format("%.0f", Float.valueOf(number));
	}

	public boolean getBoolean(String a, String b, String c) {
		boolean flag = (!"0".equals(a.trim()) && !"".equals(a.trim()) && ("0".equals(b.trim()) || "".equals(b.trim())) && ("0".equals(c.trim()) || "".equals(c.trim()))) || ("0".equals(a.trim()) && ""
				.equals(b.trim()) && "".equals(c.trim()));
		return flag;
	}
}