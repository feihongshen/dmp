package cn.explink.b2c.dongfangcj;

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
import cn.explink.b2c.tools.DataImportDAO_B2c;
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
public class DongFangCJService_getOrder extends DongFangCJService {
	private Logger logger = LoggerFactory.getLogger(DongFangCJService_getOrder.class);

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
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;

	/**
	 * 从东方购物下载数据
	 */
	@Transactional
	public void downloadOrdersToDongFangCJFTPServer() {
		// 这里需要一个基础设置
		int isOpenFlag = jointService.getStateForJoint(B2cEnum.DongFangCJ.getKey());
		if (isOpenFlag == 0) {
			logger.warn("未开启东方CJ订单下载接口");
			return;
		}
		DongFangCJ cj = super.getDongFangCJ(B2cEnum.DongFangCJ.getKey());

		DongFangCJFTPUtils ftp = new DongFangCJFTPUtils(cj.getFtp_host(), cj.getFtp_username(), cj.getFtp_password(), cj.getFtp_port(), cj.getCharencode(), cj.isIsdelDirFlag());

		try {

			ftp.downloadFileforFTP(cj.getPut_remotePath(), cj.getDownloadPath());
		} catch (Exception e) {
			logger.error("从FTP下载文件发生未知异常,return", e);
			return;
		}

	}

	/**
	 * 解析下载东方CJ的数据，存入b2c_temp表中
	 */
	@Transactional
	public long AnalyzTxtFileAndSaveB2cTemp() {
		long calcCount = 0;
		String customerids = "";
		String remark = "";

		int isOpenFlag = jointService.getStateForJoint(B2cEnum.DongFangCJ.getKey());
		if (isOpenFlag == 0) {
			logger.warn("未开启东方CJ解析txt插入临时表方法");
			return -1;
		}
		DongFangCJ cj = super.getDongFangCJ(B2cEnum.DongFangCJ.getKey());
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

				calcCount += AnzlizyTxTFileToB2cTempMethod(downloadPath, fi, cj, cj.getWarehouseid(), cj.getCustomerids()); // 解析txt文件
																															// 并导入系统b2ctemp

			}

			customerids += cj.getCustomerids() + ",";
			remark = "下载完成";

		} catch (Exception e) {
			logger.error("东方CJ购物读取服务器路径=" + downloadPath + "解析发生未知异常", e);
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
	private long AnzlizyTxTFileToB2cTempMethod(String downloadPath, String fi, DongFangCJ cj, long warehouseid, String customerid) throws Exception {
		File file = new File(downloadPath + "/" + fi);

		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis, "GBK");
		BufferedReader buf = new BufferedReader(isr);
		String line = "";

		List<Map<String, String>> datalist = new ArrayList<Map<String, String>>();
		while ((line = buf.readLine()) != null) {
			if (line != null && line.trim().length() > 100) {
				Map<String, String> cwbMap = BuildOrderDetailMapByCwbOrdertypeRoute(cj, line, fi, customerid);
				if (cwbMap == null) {
					continue;
				}
				datalist.add(cwbMap);
			}
		}

		try {

			List<CwbOrderDTO> extractss = dataImportService_B2c.Analizy_DataDealByB2c(Long.parseLong(customerid), B2cEnum.DongFangCJ.getMethod(), datalist, warehouseid, true);

			logger.debug("处理0东方CJ0后的订单信息=" + extractss.toString());
			return datalist.size();
		} catch (Exception e) {
			logger.error("0东方CJ0调用数据导入接口异常!,Json=", e);
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
	private Map<String, String> BuildOrderDetailMapByCwbOrdertypeRoute(DongFangCJ cj, String line, String fi, String customerid) {

		if (fi.startsWith("CJF1")) { // 配送 CJF1标示
			return BuildOrderDetailMap_peisong(cj, line, customerid);
		} else if (fi.startsWith("CJF2")) { // 上门退换
			return BuildOrderDetailMap_HuiShou(cj, line, customerid);
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
		int isOpenFlag = jointService.getStateForJoint(B2cEnum.DongFangCJ.getKey());
		if (isOpenFlag == 0) {
			logger.warn("未开启东方CJ解析txt插入临时表方法");
			return;
		}
		DongFangCJ cj = super.getDongFangCJ(B2cEnum.DongFangCJ.getKey());

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
						logger.info("东方CJ移动文件从{}移动到{}", downloadPath + "/" + fi, downloadPath_bak + "/" + fi + ",移动次数=" + (i));
					}
				}
				Thread.sleep(10000);// 停留10秒钟再执行.

			}
		} catch (Exception e) {
			logger.error("东方CJ移动文件发生未知异常", e);
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
	private Map<String, String> BuildOrderDetailMap_peisong(DongFangCJ cj, String line, String customerid) {
		// 首尾 行数据不解析
		Map<String, String> cwbMap = new HashMap<String, String>();

		String cwb = StringUtil.subStrByByte(line, CJF1LineEnum.Cwb.getbIx(), CJF1LineEnum.Cwb.geteIx(), "GBK"); // 订单号
		String transcwb = StringUtil.subStrByByte(line, CJF1LineEnum.Shipcwb.getbIx(), CJF1LineEnum.Shipcwb.geteIx(), "GBK");// 订购编号
		String cargotype = getCargoTypeInfo(line); // 货物类型
		String sendcarname = getSendCaroname(line); // 发出商品
		String Cargoamount = StringUtil.subStrByByte(line, CJF1LineEnum.Cargoamount.getbIx(), CJF1LineEnum.Cargoamount.geteIx(), "GBK"); // 货物金额
		String Receivablefee = StringUtil.subStrByByte(line, CJF1LineEnum.Receivablefee.getbIx(), CJF1LineEnum.Receivablefee.geteIx(), "GBK"); // 代收金额
		String Cargorealweight = StringUtil.subStrByByte(line, CJF1LineEnum.Cargorealweight.getbIx(), CJF1LineEnum.Cargorealweight.geteIx(), "GBK"); // 货物重量
		String Consigneename = StringUtil.subStrByByte(line, CJF1LineEnum.Consigneename.getbIx(), CJF1LineEnum.Consigneename.geteIx(), "GBK"); // 收件人姓名
		String Consigneephone = StringUtil.subStrByByte(line, CJF1LineEnum.Consigneephone.getbIx(), CJF1LineEnum.Consigneephone.geteIx(), "GBK"); // 收件人电话

		String ConsigneeMobile = StringUtil.subStrByByte(line, CJF1LineEnum.ConsigneeMobile.getbIx(), CJF1LineEnum.ConsigneeMobile.geteIx(), "GBK"); // 手机
		String Consigneepostcode = StringUtil.subStrByByte(line, CJF1LineEnum.Consigneepostcode.getbIx(), CJF1LineEnum.Consigneepostcode.geteIx(), "GBK"); // 邮编
		String Consigneeaddress = StringUtil.subStrByByte(line, CJF1LineEnum.Consigneeaddress.getbIx(), CJF1LineEnum.Consigneeaddress.geteIx(), "GBK"); // 收件地址
		String cwbremark = getCwbRemarks(line); // 订单备注
		String CustomerCommand = getCustomerCommand(line); // 客户要求

		String ShangPingBianHao = StringUtil.subStrByByte(line, CJF1LineEnum.ShangPingBianHao.getbIx(), CJF1LineEnum.ShangPingBianHao.geteIx(), "GBK"); // 商品编号
		String PayWayId = getPayWayId(line); // 支付方式

		CwbOrderDTO cwbOrder = dataImportDAO_B2c.getCwbByCwbB2ctemp(cwb);
		if (cwbOrder != null) {
			logger.warn("获取0东方CJ0订单中含有重复数据cwb={}", cwb);
			return null;
		}

		cwbMap.put("cwb", cwb);
		cwbMap.put("transcwb", transcwb);
		cwbMap.put("consigneename", Consigneename);
		cwbMap.put("consigneephone", Consigneephone);
		cwbMap.put("consigneemobile", ConsigneeMobile);
		cwbMap.put("consigneepostcode", Consigneepostcode);
		cwbMap.put("consigneeaddress", Consigneeaddress);
		cwbMap.put("sendcarname", sendcarname);
		cwbMap.put("sendcarnum", 1 + "");
		cwbMap.put("receivablefee", Receivablefee);
		cwbMap.put("cargorealweight", Cargorealweight);// 货物重量
		cwbMap.put("cargoamount", Cargoamount); // 货物金额
		cwbMap.put("cwbremark", cwbremark + "," + ShangPingBianHao);
		cwbMap.put("customercommand", CustomerCommand);
		cwbMap.put("paywayid", PayWayId);
		cwbMap.put("cargotype", cargotype); // 货物类别

		cwbMap.put("cwbordertypeid", "1"); // 订单类型
		cwbMap.put("customerid", customerid); // 供货商Id

		return cwbMap;
	}

	/**
	 * 货物类型
	 * 
	 * @param line
	 * @return
	 */
	private String getCargoTypeInfo(String line) {
		String ZhiPeiSongYuFou = getZhiPeiSongYuFou(line); // 直配送与否
		String BuTieYuFou = getBuTieYuFou(line);
		String ZengPinYuFou = getZengPingYuFou(line);
		String cargotype = ZhiPeiSongYuFou + "," + BuTieYuFou + " " + ZengPinYuFou;
		return cargotype;
	}

	/**
	 * 直配送与否
	 * 
	 * @param line
	 * @return
	 */
	private String getZhiPeiSongYuFou(String line) {
		String ZhiPeiSongYuFou = StringUtil.subStrByByte(line, CJF1LineEnum.ZhiPeiSongYuFou.getbIx(), CJF1LineEnum.ZhiPeiSongYuFou.geteIx(), "GBK");
		if ("0".equals(ZhiPeiSongYuFou)) {
			return "一般配送";
		} else if ("1".equals(ZhiPeiSongYuFou)) {
			return "直配送";
		} else if ("2".equals(ZhiPeiSongYuFou)) {
			return "家电下乡";
		} else if ("3".equals(ZhiPeiSongYuFou)) {
			return "国家以旧换新";
		}
		return "";
	}

	/**
	 * 补贴与否
	 * 
	 * @param line
	 * @return
	 */
	private String getBuTieYuFou(String line) {
		String BuTieYuFou = StringUtil.subStrByByte(line, CJF1LineEnum.BuTieYuFou.getbIx(), CJF1LineEnum.BuTieYuFou.geteIx(), "GBK");
		if ("0".equals(BuTieYuFou)) {
			return "";
		} else {
			return "节能补贴";
		}

	}

	/**
	 * 赠品与否
	 * 
	 * @param line
	 * @return
	 */
	private String getZengPingYuFou(String line) {
		String ZengPingYuFou = StringUtil.subStrByByte(line, CJF1LineEnum.ZengPingYuFou.getbIx(), CJF1LineEnum.ZengPingYuFou.geteIx(), "GBK");
		;
		if ("0".equals(ZengPingYuFou)) {
			return " 主品";
		} else {
			return " 赠品";
		}

	}

	private String getSendCaroname(String line) {
		String ShangPingMingCheng = StringUtil.subStrByByte(line, CJF1LineEnum.ShangPingMingCheng.getbIx(), CJF1LineEnum.ShangPingMingCheng.geteIx(), "GBK");
		String PeiSongShangPingQuFen = StringUtil.subStrByByte(line, CJF1LineEnum.PeiSongShangPingQuFen.getbIx(), CJF1LineEnum.PeiSongShangPingQuFen.geteIx(), "GBK");
		String SongHuoFeiYongQuFen = StringUtil.subStrByByte(line, CJF1LineEnum.SongHuoFeiYongQuFen.getbIx(), CJF1LineEnum.SongHuoFeiYongQuFen.geteIx(), "GBK"); // 送货费用分区
		SongHuoFeiYongQuFen = getSongHuoFeiYongByCode(SongHuoFeiYongQuFen);
		String sendcargoname = ShangPingMingCheng + SongHuoFeiYongQuFen + ",配送商品区分:" + PeiSongShangPingQuFen;
		return sendcargoname;
	}

	/**
	 * 根据编码获取送货费用
	 * 
	 * @return
	 */
	private String getSongHuoFeiYongByCode(String str) {
		if ("10".equals(str)) {
			return "一般";
		}
		if ("20".equals(str)) {
			return "小型";
		}
		if ("30".equals(str)) {
			return "大型";
		}
		if ("31".equals(str)) {
			return "特殊商品1";
		}
		if ("32".equals(str)) {
			return "特殊商品2";
		}
		if ("33".equals(str)) {
			return "特殊商品3";
		}
		if ("34".equals(str)) {
			return "特殊商品4";
		}
		if ("35".equals(str)) {
			return "特殊商品5";
		}
		return "一般";
	}

	private String getPayWayId(String line) {
		String ChuKuQuFen = StringUtil.subStrByByte(line, CJF1LineEnum.ChuKuQuFen.getbIx(), CJF1LineEnum.ChuKuQuFen.geteIx(), "GBK");
		if ("1".equals(ChuKuQuFen)) {
			return String.valueOf(PaytypeEnum.Xianjin.getValue());
		} else if ("2".equals(ChuKuQuFen)) {
			return String.valueOf(PaytypeEnum.Pos.getValue());
		}
		return String.valueOf(PaytypeEnum.Xianjin.getValue());

	}

	private String getCwbRemarks(String line) {
		String ChuKuQuFen = StringUtil.subStrByByte(line, CJF1LineEnum.ChuKuQuFen.getbIx(), CJF1LineEnum.ChuKuQuFen.geteIx(), "GBK");
		if ("1".equals(ChuKuQuFen)) {
			ChuKuQuFen = "COD[货到付款],";
		} else if ("2".equals(ChuKuQuFen)) {
			ChuKuQuFen = "MOB[刷卡],";
		} else if ("3".equals(ChuKuQuFen)) {
			ChuKuQuFen = "已收款,";
		} else {
			ChuKuQuFen = "";
		}
		String DingDanBeiZhu = StringUtil.subStrByByte(line, CJF1LineEnum.DingDanBeiZhu.getbIx(), CJF1LineEnum.DingDanBeiZhu.geteIx(), "GBK");
		String cwbremark = ChuKuQuFen + DingDanBeiZhu;
		return cwbremark;
	}

	private String getCustomerCommand(String line) {
		String XiuXiRiYuFou = StringUtil.subStrByByte(line, CJF1LineEnum.XiuXiRiYuFou.getbIx(), CJF1LineEnum.XiuXiRiYuFou.geteIx(), "GBK");
		XiuXiRiYuFou = (XiuXiRiYuFou.equals("1") ? "周末" : "平时");

		String GongZuoRiYuFou = StringUtil.subStrByByte(line, CJF1LineEnum.GongZuoRiYuFou.getbIx(), CJF1LineEnum.GongZuoRiYuFou.geteIx(), "GBK");
		;
		GongZuoRiYuFou = (GongZuoRiYuFou.equals("1") ? "上班" : "休息");

		String ZhiDingRiYuFou = StringUtil.subStrByByte(line, CJF1LineEnum.ZhiDingRiYuFou.getbIx(), CJF1LineEnum.ZhiDingRiYuFou.geteIx(), "GBK");
		;
		ZhiDingRiYuFou = (ZhiDingRiYuFou.equals("1") ? "指定日" : "非指定日");
		if (ZhiDingRiYuFou.equals("指定日")) {
			String ZhiDingRi = StringUtil.subStrByByte(line, CJF1LineEnum.ZhiDingRi.getbIx(), CJF1LineEnum.ZhiDingRi.geteIx(), "GBK");
			;
			String ZhiDingShiJian = getZhiDingShiJian(line);
			ZhiDingRiYuFou += ZhiDingRi + " " + ZhiDingShiJian;
		}
		String customercommand = XiuXiRiYuFou + "," + GongZuoRiYuFou + "," + ZhiDingRiYuFou;
		return customercommand;
	}

	/**
	 * 指定时间
	 * 
	 * @param line
	 * @return
	 */
	private String getZhiDingShiJian(String line) {
		String ZhiDingShiJian = StringUtil.subStrByByte(line, CJF1LineEnum.ZhiDingShiJian.getbIx(), CJF1LineEnum.ZhiDingShiJian.geteIx(), "GBK");
		if ("01".equals(ZhiDingShiJian)) {
			return "AM";
		} else if ("02".equals(ZhiDingShiJian)) {
			return "PM";
		} else if ("03".equals(ZhiDingShiJian)) {
			return "Night";
		} else if ("00".equals(ZhiDingShiJian)) {
			return "随时可送";
		}
		return "随时可送";
	}

	/***
	 * 订单类型为上门退换
	 * 
	 * @param cj
	 * @param line
	 * @return
	 */
	private Map<String, String> BuildOrderDetailMap_HuiShou(DongFangCJ cj, String line, String customerid) {
		// 首尾 行数据不解析
		Map<String, String> cwbMap = new HashMap<String, String>();

		String Cwb = StringUtil.subStrByByte(line, CJF2LineEnum.HuoYunDanHao.getbIx(), CJF2LineEnum.HuoYunDanHao.geteIx(), "GBK");
		String transcwb = StringUtil.subStrByByte(line, CJF2LineEnum.DingGouBianHao.getbIx(), CJF2LineEnum.DingGouBianHao.geteIx(), "GBK");
		int cwbOrdertypeId = getCwbOrdertypeName(line); // 订单类型
		String CargoType = getCargoTypeInfo2(line); // 货物类型
		String PeisongQuFen = getPeiSongDiQu(line); // 配送地区
		String backcargoname = getCargoTypeInfo2(line); // 取回商品

		String sendcarnum = StringUtil.subStrByByte(line, CJF2LineEnum.ShangPinShuLiang.getbIx(), CJF2LineEnum.ShangPinShuLiang.geteIx(), "GBK"); // 商品数量
		String cargorealweight = StringUtil.subStrByByte(line, CJF2LineEnum.ShangPinZhongLiang.getbIx(), CJF2LineEnum.ShangPinZhongLiang.geteIx(), "GBK"); // 重量
		String consigneename = StringUtil.subStrByByte(line, CJF2LineEnum.ShouJianRen.getbIx(), CJF2LineEnum.ShouJianRen.geteIx(), "GBK"); // 收件人
		String consigneephone = StringUtil.subStrByByte(line, CJF2LineEnum.DianHua.getbIx(), CJF2LineEnum.DianHua.geteIx(), "GBK"); // 电话
		String consigneemobile = StringUtil.subStrByByte(line, CJF2LineEnum.ShouJi.getbIx(), CJF2LineEnum.ShouJi.geteIx(), "GBK"); // 手机
		String consigneepostcode = StringUtil.subStrByByte(line, CJF2LineEnum.YouBian.getbIx(), CJF2LineEnum.YouBian.geteIx(), "GBK");// 邮编
		String consigneeaddress = StringUtil.subStrByByte(line, CJF2LineEnum.GuKeDiZhi.getbIx(), CJF2LineEnum.GuKeDiZhi.geteIx(), "GBK"); // 收件地址
		String TuiHuoCangKu = getTuiHuoCangKu(line); // 退回仓库
		String GongYingShang = StringUtil.subStrByByte(line, CJF2LineEnum.GongYingShang.getbIx(), CJF2LineEnum.GongYingShang.geteIx(), "GBK"); // 供应商

		String GYSFuZeRen = StringUtil.subStrByByte(line, CJF2LineEnum.GYSFuZeRen.getbIx(), CJF2LineEnum.GYSFuZeRen.geteIx(), "GBK"); // 供应商
																																		// 负责人
		String GYSDianHua = StringUtil.subStrByByte(line, CJF2LineEnum.GYSDianHua.getbIx(), CJF2LineEnum.GYSDianHua.geteIx(), "GBK"); // 供应商
																																		// 电话

		String GYSYouBian = StringUtil.subStrByByte(line, CJF2LineEnum.GYSYouBian.getbIx(), CJF2LineEnum.GYSYouBian.geteIx(), "GBK"); // 供应商
																																		// 邮编
		String GYSDiZhi = StringUtil.subStrByByte(line, CJF2LineEnum.GYSDiZhi.getbIx(), CJF2LineEnum.GYSDiZhi.geteIx(), "GBK"); // 供应商
																																// 地址
		String BeiZhu = StringUtil.subStrByByte(line, CJF2LineEnum.BeiZhu.getbIx(), CJF2LineEnum.BeiZhu.geteIx(), "GBK"); // 供应商
																															// 备注
		String ShangPinBianHao = StringUtil.subStrByByte(line, CJF2LineEnum.ShangPinBianHao.getbIx(), CJF2LineEnum.ShangPinBianHao.geteIx(), "GBK"); // 商品编号

		String customercommand = "";
		customercommand = "商品需退回供应商:" + GongYingShang + ",负责人:" + GYSFuZeRen + ",电话:" + GYSDianHua + ", 邮编:" + GYSYouBian + ",退回仓库:" + TuiHuoCangKu + ",退回地址:" + GYSDiZhi;

		cwbMap.put("cwb", Cwb);
		cwbMap.put("transcwb", transcwb);
		cwbMap.put("consigneename", consigneename);
		cwbMap.put("consigneephone", consigneephone);
		cwbMap.put("consigneemobile", consigneemobile);
		cwbMap.put("consigneepostcode", consigneepostcode);
		cwbMap.put("consigneeaddress", consigneeaddress);
		cwbMap.put("backcarname", backcargoname);
		cwbMap.put("backcarnum", 1 + "");
		cwbMap.put("cargorealweight", cargorealweight);// 货物重量
		cwbMap.put("cwbremark", ShangPinBianHao + "," + BeiZhu);
		cwbMap.put("cargoamount", "0");
		cwbMap.put("customercommand", customercommand);
		cwbMap.put("cargotype", CargoType); // 货物类别
		cwbMap.put("cwbordertypeid", cwbOrdertypeId + ""); // 订单类型
		cwbMap.put("customerid", customerid); // 供货商Id
		cwbMap.put("paywayid", PaytypeEnum.Xianjin.getValue() + ""); // 供货商Id

		return cwbMap;
	}

	/**
	 * 订单类型
	 * 
	 * @param line
	 * @return
	 */
	private int getCwbOrdertypeName(String line) {
		String BuTieYuFou = StringUtil.subStrByByte(line, CJF2LineEnum.HuiShouQuFen.getbIx(), CJF2LineEnum.HuiShouQuFen.geteIx(), "GBK");
		if ("1".equals(BuTieYuFou)) {
			return CwbOrderTypeIdEnum.Shangmentui.getValue();
		}
		if ("2".equals(BuTieYuFou)) {
			return CwbOrderTypeIdEnum.Shangmenhuan.getValue();
		}
		return CwbOrderTypeIdEnum.Shangmentui.getValue();
	}

	/**
	 * 拼接为货物类型
	 * 
	 * @param line
	 * @return
	 */
	private String getCargoTypeInfo2(String line) {

		String SongHuoFeiYongQuFen2 = getSongHuoFeiYongQuFen2(line);
		String BuTieYuFou2 = getBuTieYuFou2(line);
		String ZengPinYuFou2 = getZengPingYuFou2(line);
		String cargotype = SongHuoFeiYongQuFen2 + "," + BuTieYuFou2 + " " + ZengPinYuFou2;
		return cargotype;
	}

	/**
	 * 送货费用区分
	 * 
	 * @param line
	 * @return
	 */
	private String getSongHuoFeiYongQuFen2(String line) {
		String SongHuoFeiYongQuFen2 = StringUtil.subStrByByte(line, CJF2LineEnum.SongHuoFeiYongQufen.getbIx(), CJF2LineEnum.SongHuoFeiYongQufen.geteIx(), "GBK");
		if ("10".equals(SongHuoFeiYongQuFen2)) {
			return "一般";
		}
		if ("20".equals(SongHuoFeiYongQuFen2)) {
			return "小型";
		}
		if ("30".equals(SongHuoFeiYongQuFen2)) {
			return "大型";
		}
		if ("31".equals(SongHuoFeiYongQuFen2)) {
			return "特殊商品一";
		}
		if ("32".equals(SongHuoFeiYongQuFen2)) {
			return "特殊商品二";
		}
		if ("33".equals(SongHuoFeiYongQuFen2)) {
			return "特殊商品三";
		}
		if ("34".equals(SongHuoFeiYongQuFen2)) {
			return "特殊商品四";
		}
		if ("35".equals(SongHuoFeiYongQuFen2)) {
			return "特殊商品五";
		}
		return "一般商品";

	}

	/**
	 * 补贴与否
	 * 
	 * @param line
	 * @return
	 */
	private String getBuTieYuFou2(String line) {
		String BuTieYuFou = StringUtil.subStrByByte(line, CJF2LineEnum.JieNengBuTieYufou.getbIx(), CJF2LineEnum.JieNengBuTieYufou.geteIx(), "GBK");
		if ("0".equals(BuTieYuFou)) {
			return "";
		} else {
			return "节能补贴";
		}

	}

	/**
	 * 赠品与否
	 * 
	 * @param line
	 * @return
	 */
	private String getZengPingYuFou2(String line) {
		String ZengPingYuFou = StringUtil.subStrByByte(line, CJF2LineEnum.ZengPinYufou.getbIx(), CJF2LineEnum.ZengPinYufou.geteIx(), "GBK");
		;
		if ("0".equals(ZengPingYuFou)) {
			return " 主品";
		} else {
			return " 赠品";
		}

	}

	/**
	 * 配送地区
	 * 
	 * @param line
	 * @return
	 */
	private String getPeiSongDiQu(String line) {
		String PeiSongDiQu = StringUtil.subStrByByte(line, CJF2LineEnum.PeiSongDiQu.getbIx(), CJF2LineEnum.PeiSongDiQu.geteIx(), "GBK");
		if ("10".equals(PeiSongDiQu)) {
			return "上海";
		}
		if ("11".equals(PeiSongDiQu)) {
			return "崇明岛";
		}
		if ("21".equals(PeiSongDiQu)) {
			return "无锡";
		}
		if ("32".equals(PeiSongDiQu)) {
			return "嘉兴";
		}

		return "";

	}

	/**
	 * 拼接取回/换货商品列
	 * 
	 * @param line
	 * @return
	 */
	private String getBackCargoName2(String line) {
		String ShangPingMingCheng2 = StringUtil.subStrByByte(line, CJF2LineEnum.ShangPinMingCheng.getbIx(), CJF2LineEnum.ShangPinMingCheng.geteIx(), "GBK");
		String PeiSongShangPingQuFen2 = StringUtil.subStrByByte(line, CJF2LineEnum.PeiSongShangPinQufen.getbIx(), CJF2LineEnum.PeiSongShangPinQufen.geteIx(), "GBK");

		String sendcargoname = ShangPingMingCheng2 + ",配送商品区分:" + PeiSongShangPingQuFen2;
		return sendcargoname;
	}

	/**
	 * 退货仓库
	 * 
	 * @param line
	 * @return
	 */
	private String getTuiHuoCangKu(String line) {
		String tuihuocangku2 = StringUtil.subStrByByte(line, CJF2LineEnum.TuiHuoCangKu.getbIx(), CJF2LineEnum.TuiHuoCangKu.geteIx(), "GBK");
		if ("1".equals(tuihuocangku2)) {
			return "SMG-CJ";
		}
		if ("2".equals(tuihuocangku2)) {
			return "VENDOR";
		}
		return "SMG-CJ";
	}

}
