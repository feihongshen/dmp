package cn.explink.b2c.tools;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.suning.responsedto.ResponseData;
import cn.explink.b2c.suning.responsedto.Result;
import cn.explink.b2c.tmall.CwbColumnSet;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CommonDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CwbErrorDAO;
import cn.explink.dao.EmailDateDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.SetExcelColumnDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.EmailDate;
import cn.explink.domain.ExcelColumnSet;
import cn.explink.domain.ImportValidationManager;
import cn.explink.domain.User;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.enumutil.VipExchangeFlagEnum;
import cn.explink.service.CwbOrderValidator;
import cn.explink.service.DataImportService;
import cn.explink.service.ResultCollector;
import cn.explink.util.DateTimeUtil;

@Service
public class DataImportService_B2c {
	private Logger logger = LoggerFactory.getLogger(DataImportService_B2c.class);

	@Autowired
	ImportValidationManager importValidationManager;
	@Autowired
	DataImportService dataImportService;
	@Autowired
	SetExcelColumnDAO setExcelColumnDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	CommonDAO commonDAO;
	@Autowired
	CwbColumnSet cwbColumnSet;
	@Autowired
	CwbErrorDAO cwbErrorDAO;
	@Autowired
	EmailDateDAO emaildateDAO;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	CustomWareHouseDAO customWarehouseDAO;
	
	/**
	 * 提供数据导入接口-对接用到
	 * 对接管理中的托运模式关闭的情况下：
	 * 1.导入批次表(express_ops_emaildate)emaildatetime使用当天第一条进入DMP系统订单的创建时间
	 * 2.临时表(express_ops_cwb_detail_b2ctemp)和正式表(express_ops_cwb_detail)的emaildate(发货时间)取TMS的出仓时间
	 * @author leo01.liao
	 * @param customerid
	 * @param b2cFlag
	 * @param xmlList
	 * @param warehouse_id
	 * @param SaveTempTableFlag
	 * @param emaildate
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public List<CwbOrderDTO> Analizy_DataDealByB2cNonTuoYun(long customerid, String b2cFlag, List<Map<String, String>> xmlList, 
															long warehouse_id, boolean SaveTempTableFlag, String emaildate) throws Exception {
		//获取虚拟库房Id,所有的B2C对接如果未设置都会导入默认的虚拟库房里面，方便能够统计到。
		long warehouseid = warehouse_id == 0 ? getTempWarehouseIdForB2c() : warehouse_id;
		
		//导入批次表(express_ops_emaildate)emaildatetime使用当天第一条进入DMP系统订单的创建时间
		EmailDate ed = dataImportService.getOrCreateEmailDate(customerid, 0, warehouseid);
		
		ExcelColumnSet excelColumnSet = cwbColumnSet.getEexcelColumnSetByB2cJoint(b2cFlag);
		List<CwbOrderDTO> cwbOrders = getCwbOrders(customerid, excelColumnSet, warehouseid, xmlList);
		User user = new User();
		user.setUserid(1);
		user.setBranchid(warehouseid);
		
		//临时表(express_ops_cwb_detail_b2ctemp)和正式表(express_ops_cwb_detail)的emaildate(发货时间)取TMS的出仓时间
		ed.setEmaildatetime(emaildate);
		
		if (!SaveTempTableFlag) { // 调用正常导入的方法
			importData(cwbOrders, customerid, user, warehouseid, ed);
		} else { // 导入临时表，然后定时器处理
			for (CwbOrderDTO cwbOrder : cwbOrders) {
					String remark1 = (cwbOrder.getRemark1()==null?"":cwbOrder.getRemark1().trim());
					if(remark1.length() > 100){
						remark1 = remark1.substring(0, 100);
					}
					cwbOrder.setRemark1(remark1);
					
					if (!b2cFlag.equals(B2cEnum.YiXun.getMethod()) && !b2cFlag.equals(B2cEnum.Yihaodian.getMethod()) && !b2cFlag.equals(B2cEnum.Tmall.getMethod())
							&& !b2cFlag.equals(B2cEnum.YangGuang.getMethod()) && !b2cFlag.equals(B2cEnum.GuangZhouABC.getMethod()) && !b2cFlag.equals(B2cEnum.HangZhouABC.getMethod())
							&& !b2cFlag.equals(B2cEnum.Rufengda.getMethod()) && !b2cFlag.equals(B2cEnum.HaoXiangGou.getMethod())) {

						List<CwbOrderValidator> vailidators = importValidationManager.getVailidators(excelColumnSet);
						for (CwbOrderValidator cwbOrderValidator : vailidators) {
							cwbOrderValidator.validate(cwbOrder);
						}
					}

					dataImportDAO_B2c.insertCwbOrder_toTempTable(cwbOrder, customerid, warehouseid, user, ed);
			}

		}
		return cwbOrders;
	}
	
	/**
	 * 提供数据导入接口-对接用到
	 * 
	 * @param customerid
	 * @param branchid
	 * @param ed
	 * @param b2cFlag
	 * @param xmlList
	 * @return
	 */
	@Transactional
	public List<CwbOrderDTO> Analizy_DataDealByB2c(long customerid, String b2cFlag, List<Map<String, String>> xmlList, long warehouse_id, // 对接设置中传过来的ID
			boolean SaveTempTableFlag) throws Exception {

		long ruleEmaildateHours = 0;
		if (xmlList != null && xmlList.size() > 0) {
			ruleEmaildateHours = Long.valueOf(xmlList.get(0).get("ruleEmaildateHours") == null ? "0" : xmlList.get(0).get("ruleEmaildateHours").toString()); // 是否按照时间来卡批次
		}
		long warehouseid = warehouse_id == 0 ? getTempWarehouseIdForB2c() : warehouse_id; // 获取虚拟库房
																							// Id,所有的B2C对接如果未设置都会导入默认的虚拟库房里面，方便能够统计到。

		EmailDate ed = dataImportService.getOrCreateEmailDate_B2C(customerid, 0, warehouseid, ruleEmaildateHours);

		ExcelColumnSet excelColumnSet = cwbColumnSet.getEexcelColumnSetByB2cJoint(b2cFlag);
		List<CwbOrderDTO> cwbOrders = getCwbOrders(customerid, excelColumnSet, warehouseid, xmlList);
		User user = new User();
		user.setUserid(1);
		user.setBranchid(warehouseid);
		List<CwbOrderDTO> resultCwbOrders = new ArrayList<CwbOrderDTO>();
		if (!SaveTempTableFlag) { // 调用正常导入的方法
			importData(cwbOrders, customerid, user, warehouseid, ed);
			resultCwbOrders.addAll(cwbOrders);
		} else { // 导入临时表，然后定时器处理
			for (CwbOrderDTO cwbOrder : cwbOrders) {
				try {
					String remark1 = (cwbOrder.getRemark1()==null?"":cwbOrder.getRemark1().trim());
					if(remark1.length() > 100){
						remark1 = remark1.substring(0, 100);
					}
					cwbOrder.setRemark1(remark1);
					
					if (!b2cFlag.equals(B2cEnum.YiXun.getMethod()) && !b2cFlag.equals(B2cEnum.Yihaodian.getMethod()) && !b2cFlag.equals(B2cEnum.Tmall.getMethod())
							&& !b2cFlag.equals(B2cEnum.YangGuang.getMethod()) && !b2cFlag.equals(B2cEnum.GuangZhouABC.getMethod()) && !b2cFlag.equals(B2cEnum.HangZhouABC.getMethod())
							&& !b2cFlag.equals(B2cEnum.Rufengda.getMethod()) && !b2cFlag.equals(B2cEnum.HaoXiangGou.getMethod())) {

						List<CwbOrderValidator> vailidators = importValidationManager.getVailidators(excelColumnSet);
						for (CwbOrderValidator cwbOrderValidator : vailidators) {
							cwbOrderValidator.validate(cwbOrder);
						}
					}

					dataImportDAO_B2c.insertCwbOrder_toTempTable(cwbOrder, customerid, warehouseid, user, ed);
					resultCwbOrders.add(cwbOrder);
				} catch (Exception e) {
					logger.error(b2cFlag + "数据插入临时表发生未知异常cwb=" + cwbOrder.getCwb(), e);
					// Mail.LoadingAndSendMessage(b2cFlag+"数据临时表插入主表发生未知异常cwb="+cwbOrder.getCwb()+",请及时查看并修复.");
					// delete by jian_xie 这里只要第一单有问题，后面所以的单都会处理不了。
//					 e.printStackTrace();
//					return null;
				}
			}

		}

		return resultCwbOrders;
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public List<CwbOrderDTO> Analizy_DataDealByB2cByEmaildate(long customerid, String b2cFlag, List<Map<String, String>> xmlList, long warehouse_id, // 对接设置中传过来的ID
			boolean SaveTempTableFlag, String emaildate, long cWareHouseId) throws Exception {

		long warehouseid = warehouse_id == 0 ? getTempWarehouseIdForB2c() : warehouse_id; // 获取虚拟库房
																							// Id,所有的B2C对接如果未设置都会导入默认的虚拟库房里面，方便能够统计到。
		EmailDate ed = dataImportService.getEmailDate_B2CByEmaildate(customerid, cWareHouseId, warehouseid, emaildate);

		ExcelColumnSet excelColumnSet = cwbColumnSet.getEexcelColumnSetByB2cJoint(b2cFlag);
		List<CwbOrderDTO> cwbOrders = getCwbOrders(customerid, excelColumnSet, warehouseid, xmlList);
		User user = new User();
		user.setUserid(1);
		user.setBranchid(warehouseid);

		if (!SaveTempTableFlag) { // 调用正常导入的方法
			importData(cwbOrders, customerid, user, warehouseid, ed);
		} else { // 导入临时表，然后定时器处理
			for (CwbOrderDTO cwbOrder : cwbOrders) {
				if (!b2cFlag.equals(B2cEnum.YiXun.getMethod()) && !b2cFlag.equals(B2cEnum.Yihaodian.getMethod()) && !b2cFlag.equals(B2cEnum.Tmall.getMethod())
						&& !b2cFlag.equals(B2cEnum.YangGuang.getMethod()) && !b2cFlag.equals(B2cEnum.GuangZhouABC.getMethod()) && !b2cFlag.equals(B2cEnum.HangZhouABC.getMethod())
						&& !b2cFlag.equals(B2cEnum.Rufengda.getMethod()) && !b2cFlag.equals(B2cEnum.HaoXiangGou.getMethod())) {
					List<CwbOrderValidator> vailidators = importValidationManager.getVailidators(excelColumnSet);
					for (CwbOrderValidator cwbOrderValidator : vailidators) {

						cwbOrderValidator.validate(cwbOrder);
					}
				}
				dataImportDAO_B2c.insertCwbOrder_toTempTable(cwbOrder, customerid, warehouseid, user, ed);
			}
		}

		return cwbOrders;
	}

	// =======================新增快乐购方法============================
	@Transactional
	public List<CwbOrderDTO> Analizy_DataDealByB2cHappyGoByEmaildate(long customerid, String b2cFlag, List<Map<String, String>> xmlList, long warehouse_id, // 对接设置中传过来的ID
			boolean SaveTempTableFlag, String emaildate, long cWareHouseId) throws Exception {

		long warehouseid = warehouse_id == 0 ? getTempWarehouseIdForB2c() : warehouse_id; // 获取虚拟库房
																							// Id,所有的B2C对接如果未设置都会导入默认的虚拟库房里面，方便能够统计到。
		EmailDate ed = dataImportService.getEmailDate_B2CByEmaildate(customerid, cWareHouseId, warehouseid, emaildate);

		ExcelColumnSet excelColumnSet = cwbColumnSet.getEexcelColumnSetByB2cJoint(b2cFlag);
		List<CwbOrderDTO> cwbOrders = getCwbOrders(customerid, excelColumnSet, warehouseid, xmlList);
		User user = new User();
		user.setUserid(1);
		user.setBranchid(warehouseid);

		if (!SaveTempTableFlag) { // 调用正常导入的方法
			importData(cwbOrders, customerid, user, warehouseid, ed);
		} else { // 导入临时表，然后定时器处理
			for (CwbOrderDTO cwbOrder : cwbOrders) {
				if (!b2cFlag.equals(B2cEnum.YiXun.getMethod()) && !b2cFlag.equals(B2cEnum.Yihaodian.getMethod()) && !b2cFlag.equals(B2cEnum.Tmall.getMethod())
						&& !b2cFlag.equals(B2cEnum.YangGuang.getMethod()) && !b2cFlag.equals(B2cEnum.GuangZhouABC.getMethod()) && !b2cFlag.equals(B2cEnum.HangZhouABC.getMethod())
						&& !b2cFlag.equals(B2cEnum.Rufengda.getMethod()) && !b2cFlag.equals(B2cEnum.HaoXiangGou.getMethod())) {
					List<CwbOrderValidator> vailidators = importValidationManager.getVailidators(excelColumnSet);
					for (CwbOrderValidator cwbOrderValidator : vailidators) {

						cwbOrderValidator.validate(cwbOrder);
					}
				}
				try {
					dataImportDAO_B2c.inserHappyGo(cwbOrder, customerid, warehouseid, user, ed);

				} catch (Exception e) {
					logger.error(b2cFlag + "数据插入快乐购临时表发生未知异常cwb=" + cwbOrder.getCwb(), e);
					// Mail.LoadingAndSendMessage(b2cFlag+"数据临时表插入主表发生未知异常cwb="+cwbOrder.getCwb()+",请及时查看并修复.");
					// e.printStackTrace();
				}
			}
		}

		return cwbOrders;
	}

	// =================快乐购方法结束==============================
	public long getTempWarehouseIdForB2c() {
		long warehouseid = 0;
		List<Branch> branchlist = branchDAO.getBranchByBranchnameCheck("虚拟库房");
		warehouseid = branchlist.get(0).getBranchid();
		if (warehouseid == 0) {
			logger.error("B2C对接导入系统中检测到没有虚拟库房，请设置后再导入！");
		}
		return warehouseid;
	}

	/**
	 * 验证好，各个参数，返回一个正常的List.
	 * 
	 * @param f
	 * @param customerid
	 * @param errorCollector
	 * @param excelColumnSet
	 * @param branchid
	 * @param session
	 * @param ed
	 * @return
	 */
	protected List<CwbOrderDTO> getCwbOrders(long customerid, ExcelColumnSet excelColumnSet, long branchid, List<Map<String, String>> xmlList) {
		List<Branch> branchList = branchDAO.getAllBranches();// 获取所有branch记录，用于匹配下一站id

		List<CwbOrderDTO> cwbOrders = new ArrayList<CwbOrderDTO>();
		for (Map<String, String> map : xmlList) {
			CwbOrderDTO cwbOrder = null;
			try {
				// 把参数加载到类里面 待会改这里
				cwbOrder = getCwbOrderAccordingtoConf(excelColumnSet, map, customerid, branchList, branchid);
				cwbOrders.add(cwbOrder);
			} catch (Exception e) {
				logger.error("对接保存订单出错", e);
			}
		}
		return cwbOrders;
	}

	// 插入数据的方法
	public void importData(final List<CwbOrderDTO> cwbOrderDTOs, final long customerid, // 供货商
			User user, // 操作员
			final long warhouseid, EmailDate ed) {
		logger.info("开始导入,customerid:{},operatoruserid:{}", new Object[] { customerid, user.getUserid() });
		dataImportService.importData(cwbOrderDTOs, user, new ResultCollector(), ed, false);
		logger.info("对接导入结束,customerid:{},operatoruserid:{}", new Object[] { customerid, user.getUserid() });
	}

	public CwbOrderDTO getCwbOrderAccordingtoConf(ExcelColumnSet excelColumnSet, Map<String, String> row, long customerid, List<Branch> branchList, long branchid) throws Exception {
		CwbOrderDTO cwbOrder = new CwbOrderDTO();
		cwbOrder.setCwb(row.get("cwb"));// 订单号
		if (excelColumnSet.getTranscwbindex() != 0) {
			cwbOrder.setTranscwb(row.get("transcwb"));
			;// 运单号
		}
		if (excelColumnSet.getConsigneenameindex() != 0) {
			cwbOrder.setConsigneename(row.get("consigneename"));// 收件人名称
		}
		if (excelColumnSet.getConsigneeaddressindex() != 0) {
			cwbOrder.setConsigneeaddress(row.get("consigneeaddress"));// 收件人地址
		}
		if (excelColumnSet.getConsigneepostcodeindex() != 0) {
			cwbOrder.setConsigneepostcode(row.get("consigneepostcode"));// 收件人邮编
		}
		if (excelColumnSet.getConsigneephoneindex() != 0) {
			cwbOrder.setConsigneephone(row.get("consigneephone"));// 收件人电话
		}
		if (excelColumnSet.getSendcargonameindex() != 0) {
			cwbOrder.setSendcargoname(row.get("sendcarname"));// 发出商品名称
		}
		if (excelColumnSet.getBackcargonameindex() != 0) {
			cwbOrder.setBackcargoname(row.get("backcargoname"));
		}
		if (excelColumnSet.getReceivablefeeindex() != 0) {
			cwbOrder.setReceivablefee(row.get("receivablefee"));// 代收货款应收金额
		}
		if (excelColumnSet.getPaybackfeeindex() != 0) {
			cwbOrder.setPaybackfee(row.get("paybackfee"));// 上门退货应退金额
		}
		if (excelColumnSet.getCargorealweightindex() != 0) {
			cwbOrder.setCargorealweight(row.get("cargorealweight"));// 货物重量kg
		}
		if (excelColumnSet.getExcelbranchindex() != 0) {
			cwbOrder.setExcelbranch(row.get("excelbranch") == null ? "" : row.get("excelbranch"));// 站点
		}
		if (excelColumnSet.getCwbremarkindex() != 0) {
			cwbOrder.setCwbremark(row.get("cwbremark"));// 订单备注
		}

		if (excelColumnSet.getEmaildateindex() != 0) {
			cwbOrder.setEmaildate(row.get("emaildate"));// 发货时间Id
		} else {
			cwbOrder.setEmaildate(DateTimeUtil.getNowTime());
		}

		if (excelColumnSet.getConsigneenoindex() != 0) {
			cwbOrder.setConsigneeno(row.get("consigneeno"));// 收件人编号
		}

		if (excelColumnSet.getCargoamountindex() != 0) {
			cwbOrder.setCargoamount(row.get("caramount"));// 货物金额
		}
		if (excelColumnSet.getCustomercommandindex() != 0) {
			cwbOrder.setCustomercommand(row.get("customercommand"));// 客户要求
		}
		if (excelColumnSet.getCargotypeindex() != 0) {

			cwbOrder.setCargotype(row.get("cargotype"));// 货物类别
		}
		if (excelColumnSet.getCargosizeindex() != 0) {
			cwbOrder.setCargosize(row.get("cargosize"));// 商品尺寸
		}
		if (excelColumnSet.getBackcargoamountindex() != 0) {
			cwbOrder.setBackcargoamount(row.get("cargoamount"));
		}
		if (excelColumnSet.getDestinationindex() != 0) {
			cwbOrder.setDestination(row.get("destination"));// 目的地
		}
		if (excelColumnSet.getTranswayindex() != 0) {
			cwbOrder.setTransway(row.get("transway"));// 运输方式
		}

		if (excelColumnSet.getSendcargonumindex() != 0) {
			cwbOrder.setSendcargonum(row.get("sendcarnum"));// 发货数量
		}
		if (excelColumnSet.getCommoncwb() != 0) {
			cwbOrder.setCommoncwb(row.get("commoncwb"));// commoncwb快乐购
		}
		if (excelColumnSet.getBackcargonumindex() != 0) {
			cwbOrder.setBackcargonum(row.get("backcargonum"));// 取货数量
		}
		if (excelColumnSet.getCwbordertypeindex() != 0) {
			cwbOrder.setCwbordertypeid(Integer.parseInt(row.get("cwbordertypeid").toString()));// 订单类型（1配送
																								// 2上门退
																								// 3上门换）
		} else {
			cwbOrder.guessCwbordertypeid();
		}

		if (excelColumnSet.getPaywayindex() != 0) {
			cwbOrder.setPaywayid(Integer.parseInt(row.get("paywayid").toString()));
			cwbOrder.setNewpaywayid(Integer.parseInt(row.get("paywayid").toString()) + "");
		} else {
			cwbOrder.guessPaywayid();
			cwbOrder.setNewpaywayid(PaytypeEnum.Xianjin.getValue() + "");
		}
		if (excelColumnSet.getCwbdelivertypeindex() != 0) {
			cwbOrder.setCwbdelivertypeid(Integer.parseInt(row.get("cwbdelivertypeid").toString()));
		}
		if (excelColumnSet.getCwbprovinceindex() != 0) {
			cwbOrder.setCwbprovince(row.get("cwbprovince"));
		}
		if (excelColumnSet.getCwbcityindex() != 0) {
			cwbOrder.setCwbcity(row.get("cwbcity"));
		}
		if (excelColumnSet.getCwbcountyindex() != 0) {
			cwbOrder.setCwbcounty(row.get("cwbcounty"));
		}
		if (excelColumnSet.getConsigneemobileindex() != 0) {
			cwbOrder.setConsigneemobile(row.get("consigneemobile"));
		}

		if (excelColumnSet.getShipcwbindex() != 0) {
			cwbOrder.setShipcwb(row.get("shipcwb"));
		}
		if (excelColumnSet.getWarehousenameindex() != 0) {
			cwbOrder.setCustomerwarehouseid(Integer.parseInt(row.get("customerwarehouseid")));

		}
		if (excelColumnSet.getIsaudit() != 0) {
			cwbOrder.setIsaudit(row.get("isaudit") == null ? 0 : Integer.parseInt(row.get("isaudit")));
		}
		cwbOrder.setStartbranchid(branchid);

		if (excelColumnSet.getModelnameindex() != 0) {
			cwbOrder.setModelname(row.get("modelname"));
		}

		if (excelColumnSet.getCargovolumeindex() != 0) {
			cwbOrder.setCargovolume(BigDecimal.valueOf(Double.parseDouble(row.get("cargovolume").toString())));
		}
		if (excelColumnSet.getConsignoraddressindex() != 0) {
			cwbOrder.setConsignoraddress(row.get("consignoraddress"));
		}
		if (excelColumnSet.getTmall_notifyidindex() != 0) {
			cwbOrder.setTmall_notifyid(row.get("tmall_notify_id"));
		}

		if (excelColumnSet.getMulti_shipcwbindex() != 0) {
			cwbOrder.setMulti_shipcwb(row.get("multi_shipcwb"));
		}

		if (excelColumnSet.getRemark1index() != 0) {
			cwbOrder.setRemark1(row.get("remark1") == null ? "" : row.get("remark1"));
		}
		if (excelColumnSet.getRemark2index() != 0) {
			cwbOrder.setRemark2(row.get("remark2") == null ? "" : row.get("remark2"));
		}
		if (excelColumnSet.getRemark3index() != 0) {
			cwbOrder.setRemark3(row.get("remark3") == null ? "" : row.get("remark3"));
		}
		if (excelColumnSet.getRemark4index() != 0) {
			cwbOrder.setRemark4(row.get("remark4") == null ? "" : row.get("remark4"));
		}
		if (excelColumnSet.getRemark5index() != 0) {
			cwbOrder.setRemark5(row.get("remark5") == null ? "" : row.get("remark5"));
		}

		if (excelColumnSet.getCwbordertypeidindex() != 0) {
			cwbOrder.setCwbordertypeid(Long.valueOf(row.get("cwbordertypeid") == null ? "1" : row.get("cwbordertypeid")));
		}

		if (excelColumnSet.getShouldfareindex() != 0) {
			cwbOrder.setShouldfare(row.get("shouldfare") == null ? "0" : row.get("shouldfare"));
		}
		
		if (excelColumnSet.getMpsallarrivedflagindex() != 0) {
			cwbOrder.setMpsallarrivedflag(Integer.valueOf(row.get("mpsallarrivedflag") == null ? "0" : row.get("mpsallarrivedflag")));
		}
		if (excelColumnSet.getIsmpsflagindex() != 0) {
			cwbOrder.setIsmpsflag(Integer.valueOf(row.get("ismpsflag") == null ? "0" : row.get("ismpsflag")));
		}
		//团购标识
		if (excelColumnSet.getVipclubindex() != 0) {
			cwbOrder.setVipclub(Integer.parseInt(row.get("vipclub")));
		}
		cwbOrder.setDefaultCargoName();
		
		if (excelColumnSet.getSaletypeindex() != 0) {
			cwbOrder.setExchangeflag(row.get("exchange_flag") == null ? VipExchangeFlagEnum.NO.getValue() : Integer.parseInt(row.get("exchange_flag")));
			cwbOrder.setExchangecwb(row.get("exchange_cwb") == null||row.get("exchange_cwb").length()<1 ? null : row.get("exchange_cwb"));
		}
		
		return cwbOrder;
	}

	/**
	 * 根据仓库编码查询warehouseid
	 * 
	 * @param warehouse_no
	 * @param customer_id
	 * @return
	 */
	public String getCustomerWarehouseNo(String warehouse_no, String customer_id) {
		String warehouseid = "0";
		CustomWareHouse cw = customWarehouseDAO.getCustomWareHouseByNo(warehouse_no, customer_id);
		if (cw != null) {
			warehouseid = cw.getWarehouseid() + "";
		} else {
			List<CustomWareHouse> warelist = customWarehouseDAO.getCustomWareHouseByCustomerid(Long.parseLong(customer_id));
			if (warelist != null && warelist.size() > 0) {
				warehouseid = warelist.get(0).getWarehouseid() + "";
			}
		}
		return warehouseid;
	}

	/**
	 * 根据仓库编码查询warehouseid
	 * 
	 * @param warehouse_no
	 * @param customer_id
	 * @return
	 */
	public CustomWareHouse getCustomerWarehouseByName(String warehouse_name) {
		CustomWareHouse warehouse = null;
		try {
			List<CustomWareHouse> cwlist = customWarehouseDAO.getWareHouseByHousename(warehouse_name);
			if (cwlist != null && cwlist.size() > 0) {
				warehouse = cwlist.get(0);
			} else {
				logger.warn("发货仓库[" + warehouse_name + "]不存在");
			}
		} catch (Exception e) {
			logger.warn("发货仓库[" + warehouse_name + "]不存在");
		}
		return warehouse;
	}

	/**
	 * 根据仓库名称查询warehouseid
	 * 
	 * @param warehouse_no
	 * @param customer_id
	 * @return
	 */
	public String getCustomerWarehouseIdByName(String warehouseName, String customer_id) {
		String warehouseid = "0";
		List<CustomWareHouse> warelist = null;
		try {
			warelist = customWarehouseDAO.getCustomWareHouseByCustomerid(Long.valueOf(customer_id));
		} catch (Exception e) {
			logger.error("获取仓库异常", e);
			warelist = null;
		}
		if (warelist != null && warelist.size() > 0) {
			for (CustomWareHouse cw : warelist) {
				if (cw.getCustomerwarehouse().equals(warehouseName.trim())) {
					return cw.getWarehouseid() + "";
				}
			}
			warelist = customWarehouseDAO.getCustomWareHouseByCustomerid(Long.parseLong(customer_id));
			warehouseid = warelist.get(0).getWarehouseid() + "";
		}
		return warehouseid;
	}

	/**
	 * 提供数据导入接口-对接用到
	 * (只适用于【苏宁易购】的对接)
	 * @param customerid
	 * @param branchid
	 * @param ed
	 * @param b2cFlag
	 * @param xmlList
	 * @return
	 */
	@Transactional
	public List<Result> suning_DataDealByB2c(long customerid, String b2cFlag, List<Map<String, String>> xmlList, long warehouse_id, // 对接设置中传过来的ID
			boolean SaveTempTableFlag,ResponseData respda,List<String> realcwbList) throws Exception {
		List<Result> results = new ArrayList<Result>();
		long ruleEmaildateHours = 0;
		if (xmlList != null && xmlList.size() > 0) {
			ruleEmaildateHours = Long.valueOf(xmlList.get(0).get("ruleEmaildateHours") == null ? "0" : xmlList.get(0).get("ruleEmaildateHours").toString()); // 是否按照时间来卡批次
		}
		long warehouseid = warehouse_id == 0 ? getTempWarehouseIdForB2c() : warehouse_id; // 获取虚拟库房
																							// Id,所有的B2C对接如果未设置都会导入默认的虚拟库房里面，方便能够统计到。
		EmailDate ed = dataImportService.getOrCreateEmailDate_B2C(customerid, 0, warehouseid, ruleEmaildateHours);
		ExcelColumnSet excelColumnSet = cwbColumnSet.getEexcelColumnSetByB2cJoint(b2cFlag);
		List<CwbOrderDTO> cwbOrders = getCwbOrders(customerid, excelColumnSet, warehouseid, xmlList);
		User user = new User();
		user.setUserid(1);
		user.setBranchid(warehouseid);

		if (!SaveTempTableFlag) { // 调用正常导入的方法
			importData(cwbOrders, customerid, user, warehouseid, ed);
		} else { // 导入临时表，然后定时器处理
			for (CwbOrderDTO cwbOrder : cwbOrders) {
				for(String realcwb : realcwbList){
					if(realcwb.contains(cwbOrder.getCwb())){
						Result result = new Result();
						try {
							if (!b2cFlag.equals(B2cEnum.YiXun.getMethod()) && !b2cFlag.equals(B2cEnum.Yihaodian.getMethod()) && !b2cFlag.equals(B2cEnum.Tmall.getMethod())
									&& !b2cFlag.equals(B2cEnum.YangGuang.getMethod()) && !b2cFlag.equals(B2cEnum.GuangZhouABC.getMethod()) && !b2cFlag.equals(B2cEnum.HangZhouABC.getMethod())
									&& !b2cFlag.equals(B2cEnum.Rufengda.getMethod()) && !b2cFlag.equals(B2cEnum.HaoXiangGou.getMethod())) {
		
								List<CwbOrderValidator> vailidators = importValidationManager.getVailidators(excelColumnSet);
								for (CwbOrderValidator cwbOrderValidator : vailidators) {
									cwbOrderValidator.validate(cwbOrder);
								}
							}
							dataImportDAO_B2c.insertCwbOrder_toTempTable(cwbOrder, customerid, warehouseid, user, ed);
							result.setReturn_code("S");//正常插入临时表时
							result.setReturn_message("存储成功");
						} catch (Exception e) {
							result.setReturn_code("E");//插入临时表异常
							result.setReturn_message("存储失败");
							respda.setSuccess("false");
							respda.setFailedReason("存在插入异常数据");
							logger.error(b2cFlag + "数据插入临时表异常,订单号:cwb=" + cwbOrder.getCwb(), e);
						}
						result.setWork_id(realcwb);//回传真正的cwb
						results.add(result);
					}
				}
			}
		}
		return results;
	}
	
	/**
	 * 提供数据导入接口-对接用到
	 * 
	 * 针对【玫琳凯】进行修改
	 * 
	 * @param customerid
	 * @param branchid
	 * @param ed
	 * @param b2cFlag
	 * @param xmlList
	 * @return
	 */
	@Transactional
	public List<CwbOrderDTO> analizy_DataDealByMLK(long customerid, String b2cFlag, List<Map<String, String>> xmlList, long warehouse_id, // 对接设置中传过来的ID
			boolean SaveTempTableFlag) throws Exception {

		long ruleEmaildateHours = 0;
		if (xmlList != null && xmlList.size() > 0) {
			ruleEmaildateHours = Long.valueOf(xmlList.get(0).get("ruleEmaildateHours") == null ? "0" : xmlList.get(0).get("ruleEmaildateHours").toString()); // 是否按照时间来卡批次
		}
		long warehouseid = warehouse_id == 0 ? getTempWarehouseIdForB2c() : warehouse_id; // 获取虚拟库房
																							// Id,所有的B2C对接如果未设置都会导入默认的虚拟库房里面，方便能够统计到。

		EmailDate ed = dataImportService.getOrCreateEmailDate_B2C(customerid, 0, warehouseid, ruleEmaildateHours);

		ExcelColumnSet excelColumnSet = cwbColumnSet.getEexcelColumnSetByB2cJoint(b2cFlag);
		List<CwbOrderDTO> cwbOrders = getCwbOrders(customerid, excelColumnSet, warehouseid, xmlList);
		User user = new User();
		user.setUserid(1);
		user.setBranchid(warehouseid);

		if (!SaveTempTableFlag) { // 调用正常导入的方法
			importData(cwbOrders, customerid, user, warehouseid, ed);
		} else { // 导入临时表，然后定时器处理
			for (CwbOrderDTO cwbOrder : cwbOrders) {

				try {

					if (!b2cFlag.equals(B2cEnum.YiXun.getMethod()) && !b2cFlag.equals(B2cEnum.Yihaodian.getMethod()) && !b2cFlag.equals(B2cEnum.Tmall.getMethod())
							&& !b2cFlag.equals(B2cEnum.YangGuang.getMethod()) && !b2cFlag.equals(B2cEnum.GuangZhouABC.getMethod()) && !b2cFlag.equals(B2cEnum.HangZhouABC.getMethod())
							&& !b2cFlag.equals(B2cEnum.Rufengda.getMethod()) && !b2cFlag.equals(B2cEnum.HaoXiangGou.getMethod())) {

						List<CwbOrderValidator> vailidators = importValidationManager.getVailidators(excelColumnSet);
						for (CwbOrderValidator cwbOrderValidator : vailidators) {
							cwbOrderValidator.validate(cwbOrder);
						}
					}

					dataImportDAO_B2c.insertCwbOrder_toTempTable(cwbOrder, customerid, warehouseid, user, ed);
				} catch (Exception e) {
					logger.error(b2cFlag + "数据插入临时表发生未知异常cwb=" + cwbOrder.getCwb(), e);
					// Mail.LoadingAndSendMessage(b2cFlag+"数据临时表插入主表发生未知异常cwb="+cwbOrder.getCwb()+",请及时查看并修复.");
					// e.printStackTrace();
					return null;
				}
			}

		}

		return cwbOrders;
	}
	
}
