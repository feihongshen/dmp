package cn.explink.b2c.homegou;

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

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointService;
import cn.explink.b2c.tools.b2cmonntor.B2cAutoDownloadMonitorDAO;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.JMath;
import cn.explink.util.StringUtil;

@Service
public class HomegouService_getOrder extends HomegouService {
	private Logger logger = LoggerFactory.getLogger(HomegouService_getOrder.class);

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	DataImportService_B2c dataImportService_B2c;
	@Autowired
	JointService jointService;
	@Autowired
	CustomWareHouseDAO customWarehouseDAO;
	@Autowired
	B2cAutoDownloadMonitorDAO b2cAutoDownloadMonitorDAO;

	/**
	 * 从家有购物下载数据
	 */
	@Transactional
	public void downloadOrdersToHomeGouFTPServer() {
		// 这里需要一个基础设置
		int isOpenFlag = jointService.getStateForJoint(B2cEnum.HomeGou.getKey());
		if (isOpenFlag == 0) {
			logger.warn("未开启家有购物订单下载接口");
			return;
		}
		Homegou hg = super.getHomeGou(B2cEnum.HomeGou.getKey());

		HomegouFTPUtils ftp = new HomegouFTPUtils(hg.getFtp_host(), hg.getFtp_username(), hg.getFtp_password(), hg.getFtp_port(), hg.getCharencode(), hg.isIsdelDirFlag());

		try {

			ftp.downloadFileforFTP(hg.getPut_remotePath(), hg.getDownloadPath());
		} catch (Exception e) {
			logger.error("从FTP下载文件发生未知异常,return", e);
			return;
		}

	}

	/**
	 * 解析下载家有购物的数据，存入b2c_temp表中
	 */
	@Transactional
	public long AnalyzTxtFileAndSaveB2cTemp() {
		long calcCount = 0;
		String customerids = "";
		String remark = "下载完成";

		int isOpenFlag = jointService.getStateForJoint(B2cEnum.HomeGou.getKey());
		if (isOpenFlag == 0) {
			logger.warn("未开启家有购物解析txt插入临时表方法");
			return -1;
		}
		Homegou cj = super.getHomeGou(B2cEnum.HomeGou.getKey());
		String downloadPath = "";

		try {

			downloadPath = cj.getDownloadPath();
			String downloadPath_bak = cj.getDownloadPath_bak();

			String[] filelist = isExistFileDir(downloadPath, downloadPath_bak).list();
			if (filelist == null || filelist.length == 0) {
				logger.info("当前没有下载到txt文件！download={}", downloadPath);
				return 0;
			}

			for (String fi : filelist) {

				File file = new File(downloadPath + "/" + fi);
				if (!file.exists()) {
					logger.warn("不存在此文件无法解析");
					continue;
				}

				String updateDate = JMath.getFileLastUpdateTime(downloadPath + "/" + fi).substring(0, 10);
				if (!updateDate.equals(DateTimeUtil.getNowDate())) {
					logger.warn("该文件" + downloadPath + "/" + fi + "的文件不是今天要解析的文件,credate=" + updateDate);
					continue;
				}

				calcCount += AnzlizyTxTFileToB2cTempMethod(downloadPath, fi, cj, cj.getWarehouseid(), cj.getCustomerids(), cj.getCustomerid_tuihuo()); // 解析txt文件
																																						// 并导入系统b2ctemp

			}
			customerids = cj.getCustomerids() + ",";

		} catch (Exception e) {
			logger.error("家有购物读取服务器路径=" + downloadPath + "解析发生未知异常", e);
			remark = "下载遇未知异常" + e.getMessage();
		}

		remark = !remark.isEmpty() && calcCount == 0 ? "未下载到数据" : remark;
		if (calcCount > 0) {
			remark = "下载完成";
		}
		b2cAutoDownloadMonitorDAO.saveB2cDownloadRecord(customerids.indexOf(",") > 0 ? customerids.substring(0, customerids.length() - 1) : customerids, DateTimeUtil.getNowTime(), calcCount, remark);
		return calcCount;

	}

	/**
	 * 开始解析txt文件
	 * 
	 * @param downloadPath
	 * @param fi
	 * @param cj
	 * @param warehouseid
	 * @param express_id
	 * @param customerid
	 * @throws Exception
	 */
	private long AnzlizyTxTFileToB2cTempMethod(String downloadPath, String fi, Homegou cj, long warehouseid, String customerid, String customerid_tuihuo) throws Exception {
		File file = new File(downloadPath + "/" + fi);
		long calcCount = 0;

		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis, cj.getCharencode());
		BufferedReader buf = new BufferedReader(isr);
		String line = "";

		List<Map<String, String>> datalist = new ArrayList<Map<String, String>>(); // 配送类型订单

		List<Map<String, String>> datalist1 = new ArrayList<Map<String, String>>();// 上门退类型订单
		while ((line = buf.readLine()) != null) {
			if (line != null && line.trim().length() > 100) {
				Map<String, String> cwbMap = BuildOrderDetailMapByCwbOrdertypeRoute(cj, line, fi, customerid, customerid_tuihuo);

				if (fi.startsWith("DO")) { // 配送 DO标示
					datalist.add(cwbMap);
				} else if (fi.startsWith("RO")) { // 上门退换
					datalist1.add(cwbMap);
				} else {
					logger.error("无效的文件名称{}", fi);
					return 0;
				}
			}
		}

		try {
			if (datalist != null && datalist.size() > 0) {
				List<CwbOrderDTO> extractss = dataImportService_B2c.Analizy_DataDealByB2c(Long.parseLong(customerid), B2cEnum.HomeGou.getMethod(), datalist, warehouseid, true);
				calcCount += datalist.size();
			}
			if (datalist1 != null && datalist1.size() > 0) {
				List<CwbOrderDTO> extractss1 = dataImportService_B2c.Analizy_DataDealByB2c(Long.parseLong(customerid_tuihuo), B2cEnum.HomeGou.getMethod(), datalist1, warehouseid, true);
				calcCount += datalist1.size();
			}
			return calcCount;

		} catch (Exception e) {
			logger.error("0家有购物0调用数据导入接口异常!,Json=", e);
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
	private Map<String, String> BuildOrderDetailMapByCwbOrdertypeRoute(Homegou cj, String line, String fi, String customerid, String customerid_tuihuo) {

		if (fi.startsWith("DO")) { // 配送 DO标示
			return BuildOrderDetailMap_peisong(cj, line, customerid);
		} else if (fi.startsWith("RO")) { // 上门退换
			return BuildOrderDetailMap_HuiShou(cj, line, customerid_tuihuo);
		} else {
			logger.error("无效的文件名称{}", fi);
			return null;
		}

	}

	/**
	 * 不存在就创建
	 * 
	 * @param downloadPath
	 * @param downloadPath_bak
	 * @return
	 */
	private File isExistFileDir(String downloadPath, String downloadPath_bak) {
		File MyDir = new File(downloadPath);
		if (!MyDir.exists()) { // 如果不存在,创建一个子目录
			MyDir.mkdirs();
		}
		File MyDirBak = new File(downloadPath_bak);
		if (!MyDirBak.exists()) { // 如果不存在,创建一个子目录
			MyDirBak.mkdirs();
		}
		return MyDir;
	}

	/**
	 * 移动路径到bak里面，删除原路径文件
	 */
	public void MoveTxtToDownload_BakFile() {
		int isOpenFlag = jointService.getStateForJoint(B2cEnum.HomeGou.getKey());
		if (isOpenFlag == 0) {
			logger.warn("未开启家有购物解析txt插入临时表方法");
			return;
		}
		Homegou cj = super.getHomeGou(B2cEnum.HomeGou.getKey());

		String downloadPath = cj.getDownloadPath();
		String downloadPath_bak = cj.getDownloadPath_bak();
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
						JMath.moveFile(downloadPath + "/" + fi, downloadPath_bak + "/" + fi); // 移动文件到download_bak里面
						logger.info("家有购物移动文件从{}移动到{}", downloadPath + "/" + fi, downloadPath_bak + "/" + fi + ",移动次数=" + (i));
					}
				}
				Thread.sleep(10000);// 停留10秒钟再执行.

			}
		} catch (Exception e) {
			logger.error("家有购物移动文件发生未知异常", e);
			e.printStackTrace();
		}

	}

	/***
	 * 订单类型为配送
	 * 
	 * @param cj
	 * @param line
	 * @return
	 */
	private Map<String, String> BuildOrderDetailMap_peisong(Homegou cj, String line, String customerid) {
		// 首尾 行数据不解析
		Map<String, String> cwbMap = new HashMap<String, String>();

		String cwb = StringUtil.subStrByByte(line, DOLineEnum.Cwb.getbIx(), DOLineEnum.Cwb.geteIx(), cj.getReadtxtCharcode()); // 订单号
		String consigneeno = StringUtil.subStrByByte(line, DOLineEnum.DingGouBianHao.getbIx(), DOLineEnum.DingGouBianHao.geteIx(), cj.getReadtxtCharcode());// 订购编号

		String Shipptime = StringUtil.subStrByByte(line, DOLineEnum.Shipptime.getbIx(), DOLineEnum.Shipptime.geteIx(), cj.getReadtxtCharcode()); // 配送日期
		String express_id = StringUtil.subStrByByte(line, DOLineEnum.express_id.getbIx(), DOLineEnum.express_id.geteIx(), cj.getReadtxtCharcode()); // 配送公司代码

		String DingdanLeiXing = StringUtil.subStrByByte(line, DOLineEnum.DingdanLeiXing.getbIx(), DOLineEnum.DingdanLeiXing.geteIx(), cj.getReadtxtCharcode()); // 订单类型
																																								// 101:订购,
																																								// 104:交换
		int cwbordertypeid = DingdanLeiXing.contains("101") ? CwbOrderTypeIdEnum.Peisong.getValue() : CwbOrderTypeIdEnum.Shangmenhuan.getValue();

		String consignee_no = StringUtil.subStrByByte(line, DOLineEnum.Consignee_no.getbIx(), DOLineEnum.Consignee_no.geteIx(), cj.getReadtxtCharcode()); // 客户编号
		String Consigneename = StringUtil.subStrByByte(line, DOLineEnum.Consigneename.getbIx(), DOLineEnum.Consigneename.geteIx(), cj.getReadtxtCharcode()); // 收件人姓名
		String Consigneeaddress = StringUtil.subStrByByte(line, DOLineEnum.Consigneeaddress.getbIx(), DOLineEnum.Consigneeaddress.geteIx(), cj.getReadtxtCharcode()); // 收件地址
		String ConsigneepostCode = StringUtil.subStrByByte(line, DOLineEnum.ConsigneepostCode.getbIx(), DOLineEnum.ConsigneepostCode.geteIx(), cj.getReadtxtCharcode()); // 邮编

		String ConsigneeMobile = StringUtil.subStrByByte(line, DOLineEnum.ConsigneeMobile.getbIx(), DOLineEnum.ConsigneeMobile.geteIx(), cj.getReadtxtCharcode()); // 手机
		String Consigneephone = StringUtil.subStrByByte(line, DOLineEnum.Consigneephone.getbIx(), DOLineEnum.Consigneephone.geteIx(), cj.getReadtxtCharcode()); // 收件人电话

		String PayType = StringUtil.subStrByByte(line, DOLineEnum.PayType.getbIx(), DOLineEnum.PayType.geteIx(), cj.getReadtxtCharcode()); // 支付类型
																																			// 03:COD,
																																			// 04:MOB,
																																			// 00:其它
		int paytypeid = PayType.contains("04") ? PaytypeEnum.Pos.getValue() : PaytypeEnum.Xianjin.getValue();

		String ShangPingDaiMa = StringUtil.subStrByByte(line, DOLineEnum.ShangPingDaiMa.getbIx(), DOLineEnum.ShangPingDaiMa.geteIx(), cj.getReadtxtCharcode()); // 商品代码（6）+
																																								// 单件（3）

		String sendcarnumstr = StringUtil.subStrByByte(line, DOLineEnum.sendcarnum.getbIx(), DOLineEnum.sendcarnum.geteIx(), cj.getReadtxtCharcode()); // 商品数量
		int sendcarnum = sendcarnumstr.equals("") ? 1 : Integer.valueOf(sendcarnumstr);

		String receivablefee = StringUtil.subStrByByte(line, DOLineEnum.receivablefee.getbIx(), DOLineEnum.receivablefee.geteIx(), cj.getReadtxtCharcode()); // 代收款

		String Cargoremark = StringUtil.subStrByByte(line, DOLineEnum.Cargoremark.getbIx(), DOLineEnum.Cargoremark.geteIx(), cj.getReadtxtCharcode()); // 备注信息

		cwbMap.put("cwb", cwb);
		cwbMap.put("consigneeno", consigneeno);
		cwbMap.put("consigneename", Consigneename);
		cwbMap.put("consigneephone", Consigneephone);
		cwbMap.put("consigneemobile", ConsigneeMobile);
		cwbMap.put("consigneepostcode", ConsigneepostCode);
		cwbMap.put("consigneeaddress", Consigneeaddress);

		cwbMap.put("sendcarname", "");
		cwbMap.put("backcargoname", "");

		cwbMap.put("sendcarnum", sendcarnum + "");
		cwbMap.put("receivablefee", receivablefee);
		cwbMap.put("cargoamount", receivablefee); // 货物金额
		cwbMap.put("cwbremark", "");

		cwbMap.put("paywayid", paytypeid + "");

		cwbMap.put("cwbordertypeid", cwbordertypeid + ""); // 订单类型
		cwbMap.put("customerid", customerid); // 供货商Id

		cwbMap.put("backcargoname", "");
		cwbMap.put("backcargonum", "0");
		cwbMap.put("remark5", ShangPingDaiMa);

		cwbMap.put("remark1", consignee_no);
		cwbMap.put("remark4", Cargoremark.length() > 490 ? Cargoremark.substring(0, 490) : Cargoremark);

		return cwbMap;
	}

	/***
	 * 订单类型为上门退换
	 * 
	 * @param cj
	 * @param line
	 * @return
	 */
	private Map<String, String> BuildOrderDetailMap_HuiShou(Homegou cj, String line, String customerid) {
		// 首尾 行数据不解析
		Map<String, String> cwbMap = new HashMap<String, String>();

		String cwb = StringUtil.subStrByByte(line, ROLineEnum.Cwb.getbIx(), ROLineEnum.Cwb.geteIx(), cj.getReadtxtCharcode()); // 订单号
		String consigneeno = StringUtil.subStrByByte(line, ROLineEnum.DingGouBianHao.getbIx(), ROLineEnum.DingGouBianHao.geteIx(), cj.getReadtxtCharcode());// 订购编号

		String Shipptime = StringUtil.subStrByByte(line, ROLineEnum.Shiptime.getbIx(), ROLineEnum.Shiptime.geteIx(), cj.getReadtxtCharcode()); // 配送日期
		String express_id = StringUtil.subStrByByte(line, ROLineEnum.express_id.getbIx(), ROLineEnum.express_id.geteIx(), cj.getReadtxtCharcode()); // 配送公司代码

		String DingdanLeiXing = StringUtil.subStrByByte(line, ROLineEnum.DingdanLeiXing.getbIx(), ROLineEnum.DingdanLeiXing.geteIx(), cj.getReadtxtCharcode()); // 订单类型
																																								// 101:订购,
																																								// 104:交换
		int cwbordertypeid = DingdanLeiXing.contains("203") ? CwbOrderTypeIdEnum.Shangmenhuan.getValue() : CwbOrderTypeIdEnum.Shangmentui.getValue();

		String consignee_no = StringUtil.subStrByByte(line, ROLineEnum.Consignee_no.getbIx(), ROLineEnum.Consignee_no.geteIx(), cj.getReadtxtCharcode()); // 客户编号
		String Consigneename = StringUtil.subStrByByte(line, ROLineEnum.Consigneename.getbIx(), ROLineEnum.Consigneename.geteIx(), cj.getReadtxtCharcode()); // 收件人姓名
		String Consigneeaddress = StringUtil.subStrByByte(line, ROLineEnum.Consigneeaddress.getbIx(), ROLineEnum.Consigneeaddress.geteIx(), cj.getReadtxtCharcode()); // 收件地址
		String ConsigneepostCode = StringUtil.subStrByByte(line, ROLineEnum.ConsigneepostCode.getbIx(), ROLineEnum.ConsigneepostCode.geteIx(), cj.getReadtxtCharcode()); // 邮编

		String ConsigneeMobile = StringUtil.subStrByByte(line, ROLineEnum.ConsigneeMobile.getbIx(), ROLineEnum.ConsigneeMobile.geteIx(), cj.getReadtxtCharcode()); // 手机
		String Consigneephone = StringUtil.subStrByByte(line, ROLineEnum.Consigneephone.getbIx(), ROLineEnum.Consigneephone.geteIx(), cj.getReadtxtCharcode()); // 收件人电话

		int paytypeid = PaytypeEnum.Xianjin.getValue();

		String ShangPingDaiMa = StringUtil.subStrByByte(line, ROLineEnum.ShangPingDaiMa.getbIx(), ROLineEnum.ShangPingDaiMa.geteIx(), cj.getReadtxtCharcode()); // 商品代码（6）+
																																								// 单件（3）

		String backcarnumstr = StringUtil.subStrByByte(line, ROLineEnum.BackCarnum.getbIx(), ROLineEnum.BackCarnum.geteIx(), cj.getReadtxtCharcode()); // 商品数量
		int backcarnum = backcarnumstr.equals("") ? 1 : Integer.valueOf(backcarnumstr);

		String BackCarname = StringUtil.subStrByByte(line, ROLineEnum.BackCarname.getbIx(), ROLineEnum.BackCarname.geteIx(), cj.getReadtxtCharcode()); // 商品名称

		String Cargoremark = StringUtil.subStrByByte(line, ROLineEnum.Cargoremark.getbIx(), ROLineEnum.Cargoremark.geteIx(), cj.getReadtxtCharcode()); // 备注信息

		String BackCarname_str = BackCarname + "," + ShangPingDaiMa;

		cwbMap.put("cwb", cwb);
		cwbMap.put("consigneeno", consigneeno);
		cwbMap.put("consigneename", Consigneename);
		cwbMap.put("consigneephone", Consigneephone);
		cwbMap.put("consigneemobile", ConsigneeMobile);
		cwbMap.put("consigneepostcode", ConsigneepostCode);
		cwbMap.put("consigneeaddress", Consigneeaddress);
		// cwbMap.put("consigneeno",consignee_no);
		cwbMap.put("sendcarname", "");
		cwbMap.put("backcargoname", BackCarname_str.length() > 500 ? BackCarname_str.substring(0, 480) : BackCarname_str);
		cwbMap.put("sendcarnum", "0");
		cwbMap.put("receivablefee", "0");
		cwbMap.put("cargoamount", "0"); // 货物金额
		cwbMap.put("cwbremark", "");

		cwbMap.put("paywayid", paytypeid + "");

		cwbMap.put("cwbordertypeid", cwbordertypeid + ""); // 订单类型
		cwbMap.put("customerid", customerid); // 供货商Id

		cwbMap.put("backcargoname", cwbordertypeid == CwbOrderTypeIdEnum.Shangmentui.getValue() ? BackCarname : "");
		cwbMap.put("backcargonum", cwbordertypeid == CwbOrderTypeIdEnum.Shangmentui.getValue() ? String.valueOf(backcarnum) : "0");
		cwbMap.put("remark5", ShangPingDaiMa);
		cwbMap.put("remark1", consignee_no);

		cwbMap.put("remark4", Cargoremark.length() > 490 ? Cargoremark.substring(0, 490) : Cargoremark);

		return cwbMap;

	}

}
