package cn.explink.b2c.yangguang;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.explink.ExplinkService;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointService;
import cn.explink.b2c.tools.b2cmonntor.B2cAutoDownloadMonitorDAO;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.domain.CustomWareHouse;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.JMath;
import cn.explink.util.StringUtil;

/**
 * 央广购物订单下载部分
 * 
 * @author Administrator
 *
 */
@Service
public class YangGuangService_download extends YangGuangService {
	private Logger logger = LoggerFactory.getLogger(YangGuangService_download.class);

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	ExplinkService explinkService;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	JointService jointService;
	@Autowired
	CustomWareHouseDAO customWarehouseDAO;
	@Autowired
	DataImportService_B2c dataImportService_B2c;
	@Autowired
	B2cAutoDownloadMonitorDAO b2cAutoDownloadMonitorDAO;

	/**
	 * 请求央广FTP服务器 下载订单
	 */
	@Transactional
	public synchronized void getOrderDetailToYangGuangFTPServer() {

		int isOpenFlag = jointService.getStateForJoint(B2cEnum.YangGuang.getKey());
		if (isOpenFlag == 0) {
			logger.warn("未开启央广购物对接->总开关");
			return;
		}
		YangGuang yg = super.getYangGuang(B2cEnum.YangGuang.getKey());

		List<YangGuangdiff> difflist = super.filterYangGuangDiffs(super.getYangGuangDiffs(B2cEnum.YangGuang.getKey()));
		for (YangGuangdiff diff : difflist) {

			YangGuangFTPUtils ftputils = new YangGuangFTPUtils(yg.getHost(), yg.getPort(), diff.getUsername(), diff.getPwd(), yg.getCharencode(), 60000, false, yg.getServer_sys());

			try {

				ftputils.downloadFileforFTP(yg.getDownload_remotePath(), yg.getDownloadPath(), yg.getDownloadPath_bak());

			} catch (Exception e) {
				logger.error("央广购物FTP下载发生未知异常", e);
				e.printStackTrace();
			}

		}

	}

	/**
	 * testFTP
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		YangGuang yg = new YangGuang();

		// yg.setHost("112.64.144.37");
		// String username="ymjlink";
		// String pwd="ymjlink0520";
		yg.setPort(21);
		yg.setHost("1.202.141.149");
		String username = "txd";
		String pwd = "rt5yu6";

		yg.setCharencode("UTF-8");
		yg.setServer_sys("UNIX");
		yg.setDownload_remotePath("/from_txd/");
		yg.setIsdelDirFlag(false);
		yg.setDownloadPath("D:/databak/download");
		YangGuangFTPUtils ftputils = new YangGuangFTPUtils(yg.getHost(), yg.getPort(), username, pwd, yg.getCharencode(), yg.getTimeout(), false, yg.getServer_sys());
		ftputils.downloadFileforFTP(yg.getDownload_remotePath(), yg.getDownloadPath(), yg.getDownloadPath_bak());

	}

	/**
	 * 解析下载央广的数据，存入b2c_temp表中
	 */
	@Transactional
	public long AnalyzTxtFileSaveB2cTemp() {
		String customerids = "";
		long calcCount = 0;
		String remark = "下载完成";

		YangGuang yg = super.getYangGuang(B2cEnum.YangGuang.getKey());
		int isOpenFlag = jointService.getStateForJoint(B2cEnum.YangGuang.getKey());
		if (isOpenFlag == 0) {
			logger.warn("未开启央广购物对接->总开关");
			return -1;
		}

		List<YangGuangdiff> difflist = filterYangGuangDiffs(super.getYangGuangDiffs(B2cEnum.YangGuang.getKey()));
		for (YangGuangdiff diff : difflist) {
			customerids += diff.getCustomerids() + ",";
		}

		String downloadPath = yg.getDownloadPath();
		String downloadPath_bak = yg.getDownloadPath_bak();
		try {
			File MyDir = new File(downloadPath);
			if (!MyDir.exists()) { // 如果不存在,创建一个子目录
				MyDir.mkdirs();
			}
			File MyDirBak = new File(yg.getDownloadPath_bak());
			if (!MyDirBak.exists()) { // 如果不存在,创建一个子目录
				MyDirBak.mkdirs();
			}
			String[] filelist = MyDir.list();
			if (filelist == null || filelist.length == 0) {
				logger.info("downloadPath没有下载到txt文件！");
				return 0;
			}

			for (String fi : filelist) {

				File file = new File(downloadPath + "/" + fi);
				if (!file.exists()) {
					logger.warn("不存在此文件无法解析");
					return 0;
				}

				String updateDate = JMath.getFileLastUpdateTime(downloadPath + "/" + fi).substring(0, 10);
				if (!updateDate.equals(DateTimeUtil.getNowDate())) {
					logger.warn("该文件" + downloadPath + "/" + fi + "的文件不是今天要解析的文件,credate=" + updateDate);
					continue;
				}

				for (YangGuangdiff diff : difflist) {

					if (fi.startsWith("SE" + diff.getExpress_id() + "1")) { // 配送
																			// SE+"配送公司标识"+1
																			// 上门退换
																			// SE+"配送公司标识"+2
						calcCount += AnzlizyTxTFileToB2cTempMethod(downloadPath, fi, yg, diff.getWarehouseid(), diff.getExpress_id(), diff.getCustomerids()); // 解析txt文件
																																								// 并导入系统b2ctemp
					}

					if (fi.startsWith("SE" + diff.getExpress_id() + "2")) { // 配送
																			// SE+"配送公司标识"+1
																			// 上门退换
																			// SE+"配送公司标识"+2
						logger.warn("央广上门退类型订单暂时不参与导入数据" + fi);
						return 0;
					}

				}

			}

		} catch (Exception e) {
			logger.error("央广购物读取服务器路径=" + downloadPath + "解析发生未知异常", e);
			remark = "央广购物读取服务器路径=" + downloadPath + "解析发生未知异常" + e.getMessage();
		} finally {
			remark = !remark.isEmpty() && calcCount == 0 ? "未下载到数据" : remark;
			if (calcCount > 0) {
				remark = "下载完成";
			}
			b2cAutoDownloadMonitorDAO.saveB2cDownloadRecord(customerids.indexOf(",") > 0 ? customerids.substring(0, customerids.length() - 1) : customerids, DateTimeUtil.getNowTime(), calcCount,
					remark);
		}

		return calcCount;

	}

	/**
	 * 移动路径到bak里面，删除原路径文件
	 */
	public void MoveTxtFileToBakFile() {
		YangGuang yg = super.getYangGuang(B2cEnum.YangGuang.getKey());
		int isOpenFlag = jointService.getStateForJoint(B2cEnum.YangGuang.getKey());
		if (isOpenFlag == 0) {
			logger.warn("未开启央广购物对接");
			return;
		}

		String downloadPath = yg.getDownloadPath();
		String downloadPath_bak = yg.getDownloadPath_bak();
		try {

			for (int i = 0; i < 50; i++) {
				File MyDir1 = new File(downloadPath);
				String[] filelist = MyDir1.list();
				if (filelist == null || filelist.length == 0) {
					logger.info("downloadPath没有可移动的文件！");
					return;
				}

				if (MyDir1.list() != null && MyDir1.list().length > 0) {
					for (String fi : filelist) {
						// String updatetime =
						// JMath.getFileLastUpdateTime(downloadPath + "/" + fi);
						// if (DateTimeUtil.getDaysFromToTime(updatetime,
						// DateTimeUtil.getNowTime())
						// >=(yg.getKeepDays()==0?7:yg.getKeepDays())) { //最近n天的
						JMath.moveFile(downloadPath + "/" + fi, downloadPath_bak + "/" + fi); // 移动文件到download_bak里面
						logger.info("央广移动文件从{}移动到{}", downloadPath + "/" + fi, downloadPath_bak + "/" + fi + ",移动次数=" + (i));
					}
				}
				Thread.sleep(10000);// 停留10秒钟再执行.

			}
		} catch (Exception e) {
			logger.error("央广购物移动文件发生未知异常", e);
			e.printStackTrace();
		}

	}

	private long AnzlizyTxTFileToB2cTempMethod(String downloadPath, String fi, YangGuang yg, long warehouseid, String express_id, String customerid) throws Exception {
		File file = new File(downloadPath + "/" + fi);

		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis, "GBK");
		BufferedReader buf = new BufferedReader(isr);
		String line = "";

		List<Map<String, String>> datalist = new ArrayList<Map<String, String>>();
		while ((line = buf.readLine()) != null) {
			if (line != null && line.trim().length() > 100) {
				Map<String, String> cwbMap = BuildOrderDetailMapByCwbOrdertypeRoute(yg, line, fi, express_id, customerid);
				datalist.add(cwbMap);
			}
		}

		try {

			List<CwbOrderDTO> extractss = dataImportService_B2c.Analizy_DataDealByB2c(Long.parseLong(customerid), B2cEnum.YangGuang.getMethod(), datalist, warehouseid, true);

			logger.debug("处理0央广0后的订单信息=" + extractss.toString());
			return datalist.size();
		} catch (Exception e) {
			logger.error("0央广0调用数据导入接口异常!,Json=", e);
			return 0;
		} finally {
			fis.close();
			isr.close();
			buf.close();
		}

	}

	/**
	 * 根据文件类型来判断 订单类型
	 * 
	 * @return
	 */
	private Map<String, String> BuildOrderDetailMapByCwbOrdertypeRoute(YangGuang yg, String line, String fi, String express_id, String customerid) {

		if (fi.startsWith("SE" + express_id + "1")) { // 配送 SE+"配送公司标识"+1
			return BuildOrderDetailMap_peisong(yg, line, customerid);
		} else { // 上门退换 上门退换 SE+"配送公司标识"+2
			logger.warn("央广上门退类型订单暂时不参与导入数据" + fi);
			// return BuildOrderDetailMap_HuiShou(yg, line);
			return null;
		}

	}

	/***
	 * 订单类型为配送
	 * 
	 * @param yg
	 * @param line
	 * @return
	 */
	private Map<String, String> BuildOrderDetailMap_peisong(YangGuang yg, String line, String customerid) {
		// 首尾 行数据不解析
		Map<String, String> cwbMap = new HashMap<String, String>();
		String SerialNo = StringUtil.subStrByByte(line, YgSE1Enum.SerialNo.getbIx(), YgSE1Enum.SerialNo.geteIx(), "GBK");
		String WarehouseCode = StringUtil.subStrByByte(line, YgSE1Enum.WarehouseCode.getbIx(), YgSE1Enum.WarehouseCode.geteIx(), "GBK");
		String ShipperNo = StringUtil.subStrByByte(line, YgSE1Enum.ShipperNo.getbIx(), YgSE1Enum.ShipperNo.geteIx(), "GBK"); // 1
		String KuChuQuFen = StringUtil.subStrByByte(line, YgSE1Enum.KuChuQuFen.getbIx(), YgSE1Enum.KuChuQuFen.geteIx(), "GBK");
		String CustName = StringUtil.subStrByByte(line, YgSE1Enum.CustName.getbIx(), YgSE1Enum.CustName.geteIx(), "GBK");
		String TelephoneNo = StringUtil.subStrByByte(line, YgSE1Enum.TelephoneNo.getbIx(), YgSE1Enum.TelephoneNo.geteIx(), "GBK");
		String MobilephoneNo = StringUtil.subStrByByte(line, YgSE1Enum.MobilephoneNo.getbIx(), YgSE1Enum.MobilephoneNo.geteIx(), "GBK");
		String ZipCode = StringUtil.subStrByByte(line, YgSE1Enum.ZipCode.getbIx(), YgSE1Enum.ZipCode.geteIx(), "GBK");

		String OrderNo = StringUtil.subStrByByte(line, YgSE1Enum.OrderNo.getbIx(), YgSE1Enum.OrderNo.geteIx(), "GBK"); // 存入transcwb

		String Address = StringUtil.subStrByByte(line, YgSE1Enum.Address.getbIx(), YgSE1Enum.Address.geteIx(), "GBK");
		String ProductCode = StringUtil.subStrByByte(line, YgSE1Enum.ProductCode.getbIx(), YgSE1Enum.ProductCode.geteIx(), "GBK");
		String ProductName = StringUtil.subStrByByte(line, YgSE1Enum.ProductName.getbIx(), YgSE1Enum.ProductName.geteIx(), "GBK");
		String UnitCode = StringUtil.subStrByByte(line, YgSE1Enum.UnitCode.getbIx(), YgSE1Enum.UnitCode.geteIx(), "GBK");
		String UnitName = StringUtil.subStrByByte(line, YgSE1Enum.UnitName.getbIx(), YgSE1Enum.UnitName.geteIx(), "GBK");
		String ProductQty = StringUtil.subStrByByte(line, YgSE1Enum.ProductQty.getbIx(), YgSE1Enum.ProductQty.geteIx(), "GBK");
		String ProductAmount = StringUtil.subStrByByte(line, YgSE1Enum.ProductAmount.getbIx(), YgSE1Enum.ProductAmount.geteIx(), "GBK");
		String Comments = StringUtil.subStrByByte(line, YgSE1Enum.Comments.getbIx(), YgSE1Enum.Comments.geteIx(), "GBK");
		String DateofDelivery = StringUtil.subStrByByte(line, YgSE1Enum.DateofDelivery.getbIx(), YgSE1Enum.DateofDelivery.geteIx(), "GBK");
		String Wb_I_No = StringUtil.subStrByByte(line, YgSE1Enum.Wb_I_No.getbIx(), YgSE1Enum.Wb_I_No.geteIx(), "GBK");
		String Weight = StringUtil.subStrByByte(line, YgSE1Enum.Weight.getbIx(), YgSE1Enum.Weight.geteIx(), "GBK");

		if (!customWarehouseDAO.isExistsWarehouFlag(WarehouseCode, customerid)) {
			CustomWareHouse custwarehouse = new CustomWareHouse();
			custwarehouse.setCustomerid(Long.valueOf(customerid));
			custwarehouse.setCustomerwarehouse(YgWareHoueEnum.YgWarehouse(Integer.valueOf(WarehouseCode)).getWarehouseName());
			custwarehouse.setWarehouse_no(WarehouseCode);
			custwarehouse.setWarehouseremark("");
			custwarehouse.setIfeffectflag(1);
			customWarehouseDAO.creCustomer(custwarehouse);
		}

		String warhouseid = dataImportService_B2c.getCustomerWarehouseNo(WarehouseCode, customerid);
		String cwbordertypeid = CwbOrderTypeIdEnum.Peisong.getValue() + "";
		if (KuChuQuFen.equals("4")) {
			cwbordertypeid = CwbOrderTypeIdEnum.Shangmenhuan.getValue() + "";
		}

		cwbMap.put("cwb", ShipperNo); // 运单号 ->cwb
		cwbMap.put("transcwb", OrderNo); // yg订单号->transcwb 回传时用到
		cwbMap.put("shipcwb", Wb_I_No); // Wb_I_No -> shipcwb 回传时候用到
		cwbMap.put("consigneename", CustName);
		cwbMap.put("consigneephone", TelephoneNo);
		cwbMap.put("consigneemobile", MobilephoneNo);
		cwbMap.put("consigneepostcode", ZipCode);
		cwbMap.put("consigneeaddress", Address);
		cwbMap.put("sendcarname", ProductName);
		cwbMap.put("sendcarnum", ProductQty);
		cwbMap.put("receivablefee", ProductAmount);
		cwbMap.put("cargorealweight", 0 + "");
		cwbMap.put("cwbremark", Comments);
		cwbMap.put("customercommand", YgChuKuQuFenEnum.getYgStatus(Integer.valueOf(KuChuQuFen)).getText() + ","
				+ (DateofDelivery != null && !"".equals(DateofDelivery) ? "送货邀请日：" + DateofDelivery : ""));

		cwbMap.put("cwbordertypeid", cwbordertypeid); // 订单类型
		cwbMap.put("customerid", customerid); // 供货商Id
		cwbMap.put("paywayid", YgChuKuQuFenEnum.getYgStatus(Integer.valueOf(KuChuQuFen)).getPaytype() + ""); // 支付方式
		cwbMap.put("customerwarehouseid", warhouseid); // 发货仓库
		return cwbMap;
	}

	/***
	 * 订单类型为上门退换
	 * 
	 * @param yg
	 * @param line
	 * @return
	 */
	private Map<String, String> BuildOrderDetailMap_HuiShou(YangGuang yg, String line, String customerid) {
		// 首尾 行数据不解析
		Map<String, String> cwbMap = new HashMap<String, String>();
		String SerialNo = StringUtil.subStrByByte(line, YgSE2Enum.SerialNo.getbIx(), YgSE2Enum.SerialNo.geteIx(), "GBK");
		String WarehouseCode = StringUtil.subStrByByte(line, YgSE2Enum.WarehouseCode.getbIx(), YgSE2Enum.WarehouseCode.geteIx(), "GBK");
		int cwbordertypeid = StringUtil.subStrByByte(line, YgSE2Enum.pick_type.getbIx(), YgSE2Enum.pick_type.geteIx(), "GBK").equals("1") ? CwbOrderTypeIdEnum.Shangmentui.getValue()
				: CwbOrderTypeIdEnum.Shangmenhuan.getValue(); // 订单类型控制 1退货 2换货
		String OrderNo = StringUtil.subStrByByte(line, YgSE2Enum.OrderNo.getbIx(), YgSE2Enum.OrderNo.geteIx(), "GBK");

		String CustName = StringUtil.subStrByByte(line, YgSE2Enum.CustName.getbIx(), YgSE2Enum.CustName.geteIx(), "GBK");
		String TelephoneNo = StringUtil.subStrByByte(line, YgSE2Enum.TelephoneNo.getbIx(), YgSE2Enum.TelephoneNo.geteIx(), "GBK");
		String MobilephoneNo = StringUtil.subStrByByte(line, YgSE2Enum.MobilephoneNo.getbIx(), YgSE2Enum.MobilephoneNo.geteIx(), "GBK");
		String ZipCode = StringUtil.subStrByByte(line, YgSE2Enum.ZipCode.getbIx(), YgSE2Enum.ZipCode.geteIx(), "GBK");

		String Address = StringUtil.subStrByByte(line, YgSE2Enum.Address.getbIx(), YgSE2Enum.Address.geteIx(), "GBK");
		String ProductCode = StringUtil.subStrByByte(line, YgSE2Enum.ProductCode.getbIx(), YgSE2Enum.ProductCode.geteIx(), "GBK");
		String ProductName = StringUtil.subStrByByte(line, YgSE2Enum.ProductName.getbIx(), YgSE2Enum.ProductName.geteIx(), "GBK");
		String UnitCode = StringUtil.subStrByByte(line, YgSE2Enum.UnitCode.getbIx(), YgSE2Enum.UnitCode.geteIx(), "GBK");
		String UnitName = StringUtil.subStrByByte(line, YgSE2Enum.UnitName.getbIx(), YgSE2Enum.UnitName.geteIx(), "GBK");
		String ProductQty = StringUtil.subStrByByte(line, YgSE2Enum.ProductQty.getbIx(), YgSE2Enum.ProductQty.geteIx(), "GBK");

		String Memo = StringUtil.subStrByByte(line, YgSE2Enum.Memo.getbIx(), YgSE2Enum.Memo.geteIx(), "GBK");

		String RETURN_NO = StringUtil.subStrByByte(line, YgSE2Enum.RETURN_NO.getbIx(), YgSE2Enum.RETURN_NO.geteIx(), "GBK");
		String Weight = StringUtil.subStrByByte(line, YgSE2Enum.Weight.getbIx(), YgSE2Enum.Weight.geteIx(), "GBK");

		if (!customWarehouseDAO.isExistsWarehouFlag(WarehouseCode, customerid)) {
			CustomWareHouse custwarehouse = new CustomWareHouse();
			custwarehouse.setCustomerid(Long.valueOf(customerid));
			custwarehouse.setCustomerwarehouse(YgWareHoueEnum.YgWarehouse(Integer.valueOf(WarehouseCode)).getWarehouseName());
			custwarehouse.setWarehouse_no(WarehouseCode);
			custwarehouse.setWarehouseremark("");
			custwarehouse.setIfeffectflag(1);
			customWarehouseDAO.creCustomer(custwarehouse);
		}

		String warhouseid = dataImportService_B2c.getCustomerWarehouseNo(WarehouseCode, customerid);

		String sendcarname = "";
		if (cwbordertypeid == CwbOrderTypeIdEnum.Shangmenhuan.getValue()) {
			sendcarname = ProductName;
		}

		cwbMap.put("cwb", ""); // 运单号 ->cwb
		cwbMap.put("transcwb", OrderNo); // yg订单号->transcwb 回传时用到
		cwbMap.put("shipcwb", RETURN_NO); // Wb_I_No -> shipcwb 回传时候用到
		cwbMap.put("consigneename", CustName);
		cwbMap.put("consigneephone", TelephoneNo);
		cwbMap.put("consigneemobile", MobilephoneNo);
		cwbMap.put("consigneepostcode", ZipCode);
		cwbMap.put("consigneeaddress", Address);
		cwbMap.put("sendcarname", sendcarname);
		cwbMap.put("sendcarnum", ProductQty);

		cwbMap.put("cargorealweight", Weight);
		cwbMap.put("cwbremark", Memo);

		cwbMap.put("cwbordertypeid", String.valueOf(cwbordertypeid)); // 订单类型
		cwbMap.put("customerid", customerid); // 供货商Id

		cwbMap.put("customerwarehouseid", warhouseid); // 发货仓库
		return cwbMap;
	}

}
