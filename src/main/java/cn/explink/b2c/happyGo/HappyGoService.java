package cn.explink.b2c.happyGo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.jumei.AnalyzXMLJuMeiHandler;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.JointService;
import cn.explink.b2c.tools.b2cmonntor.B2cAutoDownloadMonitorDAO;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.EmailDateDAO;
import cn.explink.dao.MqExceptionDAO;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.EmailDate;
import cn.explink.domain.MqExceptionBuilder;
import cn.explink.domain.User;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.service.CustomerService;
import cn.explink.service.CwbOrderService;
import cn.explink.service.DataImportService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.StringUtil;
import cn.explink.util.MD5.MD5Util;

@Service
public class HappyGoService {
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	EmailDateDAO emaildateDAO;
	@Autowired
	HappyGoUsedDao happyGoUsedDao;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	@Autowired
	DataImportService_B2c dataImportservice;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	CustomWareHouseDAO customWarehouseDAO;
	@Autowired
	DataImportService dataImportService;
	@Produce(uri = "jms:topic:addressmatch")
	ProducerTemplate addressmatch;
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	DataImportService_B2c dataImportService_B2c;
	@Autowired
	JointService jointService;
	@Autowired
	B2cAutoDownloadMonitorDAO b2cAutoDownloadMonitorDAO;
	@Autowired
	CustomerService customerService;
	private static Logger logger = LoggerFactory.getLogger(HappyGoService.class);

	@Autowired
	private MqExceptionDAO mqExceptionDAO;
	
	/**
	 * 出库批次导入
	 * 
	 * @param happy
	 */

	public long excute_getHappyGoTask() {

		long count1 = 0;
		long count2 = 0;
		String customerids = "";
		String remark = "";
		String cretime = DateTimeUtil.getNowTime();
		try {
			int isOpenFlag = jointService.getStateForJoint(B2cEnum.happyGo.getKey());
			if (isOpenFlag == 0) {
				logger.info("未开启 快乐购对接！");
				return -1;
			}
			HappyGo happy = getHappyGo(B2cEnum.happyGo.getKey());
			judgmentLanded(happy);
			callbackLanded(happy);
			count1 = insertDelivery(happy, FunctionForHappy.ChuKu.getText());
			count2 = insertCallback(happy, FunctionForHappy.HuiShou.getText());

			timerHappy();

			customerids = happy.getCustomerid();

		} catch (Exception e) {
			remark = "下载遇未知异常" + e.getMessage();
			logger.error(remark, e);
		}
		remark = !remark.isEmpty() && (count1 + count2) == 0 ? "未下载到数据" : remark;
		if ((count1 + count2) > 0) {
			remark = "下载完成";
		}
		b2cAutoDownloadMonitorDAO.saveB2cDownloadRecord(customerids.indexOf(",") > 0 ? customerids.substring(0, customerids.length() - 1) : customerids, cretime, count1 + count2, remark);

		return count1 + count2;
	}

	public static void main(String[] args) {
		String customerids = "134";
		logger.error("", String.valueOf(customerids.indexOf(",") > 0));
	}

	public void judgmentLanded(HappyGo happy) {
		int isOpenFlag = jointService.getStateForJoint(B2cEnum.happyGo.getKey());
		if (isOpenFlag == 0) {
			logger.error("未开快乐购的对接!");
			return;
		}
		// 应用参数
		String xml = requestXMl(happy.getPagesize(), happy);
		logger.info("请求快乐购HOPE000001的xml{}", xml);
		// 系统参数
		String strMethod = FunctionForHappy.HOPE000001.getText();
		StringBuffer sbSystemArgs = new StringBuffer(); // 系统参数
		String sign = getMD5GetherForSign(happy, xml, strMethod, sbSystemArgs);
		logger.info("快乐购HOPE000001sign={},url={}", sign, happy.getPostUrl() + "?" + sbSystemArgs.toString() + "&sign=" + sign);
		String retrun_xml = doRequest(xml, happy.getPostUrl() + "?" + sbSystemArgs.toString() + "&sign=" + sign, happy);
		logger.info("获得返回快乐购HOPE000001的xml{}", retrun_xml);
		if ("".equals(retrun_xml)) {
			logger.info("获得返回快乐购HOPE000001的xml为空，不必解析：{}" + DateTimeUtil.getNowTime());
			return;
		}
		Map<String, Object> map;
		try {
			map = AnalyzXMLJuMeiHandler.parserXmlToJSONObjectByArray(retrun_xml);

			if (map.get("error_type").equals("0")) {// 成功
				List<Map<String, Object>> listmap = (List<Map<String, Object>>) map.get("orderlist");
				if (listmap == null || listmap.size() < 1) {
					return;
				}
				for (Map<String, Object> l : listmap) {
					try {
						long count = happyGoUsedDao.getbatchBybatchname((String) l.get("filename"));
						if (count > 0) {
							logger.info("已经存在批次：HOPE000001{}", (String) l.get("filename"));
							continue;
						}
						logger.info("获得快乐购批次有：{}", l.get("filename"));
						if (listmap.size() > 0) {
							happyGoUsedDao.getInsertBatch(l, FunctionForHappy.ChuKu.getKey());// 导入批次表
						}
					} catch (Exception e) {
						logger.error("快乐购插入新表异常", e);
					}
				}

			}
		} catch (Exception e) {
			logger.error("快乐购异常", e);
		}

	}

	/**
	 * 回收批次导入
	 * 
	 * @param happy
	 */
	public void callbackLanded(HappyGo happy) {
		int isOpenFlag = jointService.getStateForJoint(B2cEnum.happyGo.getKey());
		if (isOpenFlag == 0) {
			logger.error("未开快乐购的对接!");
			return;
		}
		// 应用参数
		String xml = requestXMl(happy.getPagesize(), happy);
		logger.info("请求快乐购HOPE000006的xml{}", xml);
		// 系统参数
		String strMethod = FunctionForHappy.HOPE000006.getText();
		StringBuffer sbSystemArgs = new StringBuffer(); // 系统参数
		String sign = getMD5GetherForSign(happy, xml, strMethod, sbSystemArgs);
		logger.info("sign={},url={}", sign, happy.getPostUrl() + "?" + sbSystemArgs.toString() + "&sign=" + sign);
		try {
			String retrun_xml = doRequest(xml, happy.getPostUrl() + "?" + sbSystemArgs.toString() + "&sign=" + sign, happy);
			logger.info("获得返回快乐购HOPE000006的xml{}", retrun_xml);
			if ("".equals(retrun_xml)) {
				logger.info("获得返回快乐购HOPE000006的xml为空，不必解析：{}" + DateTimeUtil.getNowTime());
				return;
			}
			Map<String, Object> map = AnalyzXMLJuMeiHandler.parserXmlToJSONObjectByArray(retrun_xml);
			if (map.get("error_type").equals("0")) {// 成功
				List<Map<String, Object>> listmap = (List<Map<String, Object>>) map.get("orderlist");
				for (Map<String, Object> l : listmap) {
					try {
						long count = happyGoUsedDao.getbatchBybatchname((String) l.get("filename"));

						if (count > 0) {
							logger.info("已经存在批次：HOPE000006{}", (String) l.get("filename"));
							continue;
						}
						logger.info("获得快乐购批次有：{}", (String) l.get("filename"));

						if (listmap.size() > 0) {
							happyGoUsedDao.getInsertBatch(l, FunctionForHappy.HuiShou.getKey());// 导入批次表

						}
					} catch (Exception e) {
						logger.error("快乐购插入新表异常", e);
					}
				}

			}
		} catch (Exception e) {
			logger.error("快乐购获取批次报错{}", e);
		}
	}

	/**
	 * 出库订单导入
	 * 
	 * @param happy
	 * @param flag
	 */
	public long insertDelivery(HappyGo happy, String flag) {
		long calcCount = 0;

		int isOpenFlag = jointService.getStateForJoint(B2cEnum.happyGo.getKey());
		if (isOpenFlag == 0) {
			logger.error("未开快乐购的对接!--出库");
			return -1;
		}
		logger.error("进入快乐购的对接!--出库");
		List<HappyMethod> happyGoList = happyGoUsedDao.getbatchByTypeAndState(FunctionForHappy.ChuKu.getKey());
		if (happyGoList != null && happyGoList.size() > 0) {
			for (HappyMethod list : happyGoList) {
				// 请求明细
				int a = list.getAmount();
				int pagesize = happy.getPagesize();
				int i = 0;
				if (a % pagesize == 0) {
					i = a / pagesize;
				} else {
					i = a / pagesize + 1;
				}
				for (int m = 1; m <= i; m++) {
					int n = 0;
					calcCount += dealwithHappygoDelivery(happy, flag, list, m, n);
				}
			}
		}
		return calcCount;

	}

	/**
	 * 出库订单导入---处理
	 * 
	 * @param happy
	 * @param flag
	 */
	private long dealwithHappygoDelivery(HappyGo happy, String flag, HappyMethod list, int m, int n) {
		String xml = inserGetxml(list, happy, m);
		logger.info("请求快乐购HOPE000002的xml{}", xml);
		String strMethod = FunctionForHappy.HOPE000002.getText();
		StringBuffer sbSystemArgs = new StringBuffer(); // 系统参数
		String sign = getMD5GetherForSign(happy, xml, strMethod, sbSystemArgs);
		logger.info("sign={},url={}", sign, happy.getPostUrl() + "?" + sbSystemArgs.toString() + "&sign=" + sign);
		Map<String, Object> map;
		try {
			String retrun_xml = doRequest(xml, happy.getPostUrl() + "?" + sbSystemArgs.toString() + "&sign=" + sign, happy);
			/*
			 * String retrun_xml=
			 * "<Program><request_id>201859566537200030</request_id><error_type>0</error_type><parameters><log_session_id>APP0320140120185909759_135</log_session_id></parameters><page><page_count>-1</page_count><page_size>1</page_size><page_no>201</page_no><end_flag>1</end_flag></page><se99_d_list><row1><piece>1</piece><town>30040901</town><send_degree>1</send_degree><return_wb_no/><filename>SE9908420140120180416</filename><wh_code>101</wh_code><notice_amt>308</notice_amt><wb_no>"
			 * +
			 * "201455654647</wb_no><item_name>快乐购商品</item_name><dely_gb>84</dely_gb><receiver_name>王泰嵘</receiver_name><order_no>"
			 * +
			 * "20144565345365674</order_no><msg/><order_batch_seq>11010</order_batch_seq><receiver_addr>湖北省 荆州市 沙市区 沙市区 沙市区 中央大道便河国美店</receiver_addr><notice_type>01</notice_type><weight>0.00</weight><out_dely_gb>084</out_dely_gb><order_gb>41</order_gb><big_yn>0</big_yn><nowdate>20140120</nowdate></row1></se99_d_list></Program>"
			 * ;
			 */logger.info("获得返回快乐购HOPE000002的xml{},批次号是：{}", retrun_xml, list.getBatchname());

			if ("".equals(retrun_xml)) {
				n++;
				logger.info("获得返回快乐购HOPE000002的xml为空，不必解析：{}再次发送n={}" + DateTimeUtil.getNowTime(), n);
				if (n > 6) {
					return 0;
				}
				dealwithHappygoDelivery(happy, flag, list, m, n);
				return 0;
			}

			map = AnalyzXMLJuMeiHandler.parserXmlToJSONObjectByArray(retrun_xml);

			if ("0".equals(map.get("error_type"))) {
				long cWareHouseId = 0;
				String wareHouseNo = list.getWarehousecode();
				if (!customWarehouseDAO.isExistsWarehouFlag(wareHouseNo, happy.getCustomerid())) {
					cWareHouseId = customWarehouseDAO.creCustomerGetId(Long.valueOf(happy.getCustomerid()), wareHouseNo, wareHouseNo);
				} else {
					CustomWareHouse cWareHouse = customWarehouseDAO.getCustomWareHouseByHousecode(wareHouseNo, Long.valueOf(happy.getCustomerid()));
					if (cWareHouse != null) {
						cWareHouseId = cWareHouse.getWarehouseid();
					}
				}

				List<Map<String, Object>> orderList = (List<Map<String, Object>>) map.get("orderlist");
				List<Map<String, String>> happymap = BuildChukuList(orderList, happy, flag);
				dataImportservice.Analizy_DataDealByB2cHappyGoByEmaildate(Long.parseLong(happy.getCustomerid()), B2cEnum.happyGo.getMethod(), happymap, happy.getWarehouseid(), true,
						list.getSendtime(), cWareHouseId);
				happyGoUsedDao.getupdatebatchBybatchname(list.getBatchname());
				logger.info("快乐购下载订单信息调用数据导入接口-插入数据库成功!");

				return happymap.size();
			} else {
				happyGoUsedDao.getupdateStateBybatchname(list.getBatchname());
				return 0;
			}
		} catch (Exception e) {
			logger.error("快乐购下载出库明细订单信息异常," + e);
			return 0;
		}
	}

	/**
	 * 回收的订单导入
	 * 
	 * @param happy
	 * @param flag
	 */

	public long insertCallback(HappyGo happy, String flag) {
		long calcCount = 0;
		int isOpenFlag = jointService.getStateForJoint(B2cEnum.happyGo.getKey());
		if (isOpenFlag == 0) {
			logger.error("未开快乐购的对接!--回收");
			return -1;
		}
		logger.error("进入快乐购的对接!--回收");
		List<HappyMethod> happyGoList = happyGoUsedDao.getbatchByTypeAndState(FunctionForHappy.HuiShou.getKey());
		if (happyGoList != null && happyGoList.size() > 0) {
			for (HappyMethod list : happyGoList) {
				// 请求明细
				int a = list.getAmount();
				int pagesize = happy.getPagesize();
				int i = 0;
				if (a % pagesize == 0) {
					i = a / pagesize;
				} else {
					i = a / pagesize + 1;
				}
				for (int m = 1; m <= i; m++) {
					int n = 0;
					calcCount += dealwithHappygoCallBack(happy, flag, list, m, n);
				}
			}
		}
		return calcCount;
	}

	/**
	 * 回收的订单导入--处理
	 * 
	 * @param happy
	 * @param flag
	 */
	private long dealwithHappygoCallBack(HappyGo happy, String flag, HappyMethod list, int m, int n) {
		String xml = inserGetxml(list, happy, m);
		logger.info("请求快乐购HOPE000007的xml{}", xml);
		String strMethod = FunctionForHappy.HOPE000007.getText();
		StringBuffer sbSystemArgs = new StringBuffer(); // 系统参数
		String sign = getMD5GetherForSign(happy, xml, strMethod, sbSystemArgs);
		Map<String, Object> map;
		try {
			String retrun_xml = doRequest(xml, happy.getPostUrl() + "?" + sbSystemArgs.toString() + "&sign=" + sign, happy);
			/*
			 * String retrun_xml=
			 * "<Program><request_id>201859566537200030</request_id><error_type>0</error_type><parameters><log_session_id>APP0320140120185909759_135</log_session_id></parameters><page><page_count>-1</page_count><page_size>1</page_size><page_no>201</page_no><end_flag>1</end_flag></page><se99_d_list><row1><piece>1</piece><town>30040901</town><send_degree>1</send_degree><return_wb_no/><filename>SE9908420140120180416</filename><wh_code>101</wh_code><notice_amt>308</notice_amt><wb_no>"
			 * +
			 * "201455654648</wb_no><item_name>快乐购商品</item_name><dely_gb>84</dely_gb><receiver_name>王泰嵘</receiver_name><order_no>"
			 * +
			 * "20144565345365674</order_no><msg/><order_batch_seq>11010</order_batch_seq><receiver_addr>湖北省 荆州市 沙市区 沙市区 沙市区 中央大道便河国美店</receiver_addr><notice_type>01</notice_type><weight>0.00</weight><out_dely_gb>084</out_dely_gb><order_gb>42</order_gb><big_yn>0</big_yn><nowdate>20140120</nowdate></row1></se99_d_list></Program>"
			 * ;
			 */logger.info("获得返回快乐购HOPE000007的xml{},批次号是：{}", retrun_xml, list.getBatchname());
			if ("".equals(retrun_xml)) {
				logger.info("获得返回快乐购HOPE000007的xml为空，不必解析：{}" + DateTimeUtil.getNowTime());
				n++;
				if (n > 6) {
					return 0;
				}
				dealwithHappygoCallBack(happy, flag, list, m, n);
				return 0;
			}
			map = AnalyzXMLJuMeiHandler.parserXmlToJSONObjectByArray(retrun_xml);
			if ("0".equals(map.get("error_type"))) {
				long cWareHouseId = 0;
				String wareHouseNo = list.getWarehousecode();
				if (!customWarehouseDAO.isExistsWarehouFlag(wareHouseNo, happy.getCustomerid())) {
					cWareHouseId = customWarehouseDAO.creCustomerGetId(Long.valueOf(happy.getCustomerid()), wareHouseNo, wareHouseNo);
				} else {
					CustomWareHouse cWareHouse = customWarehouseDAO.getCustomWareHouseByHousecode(wareHouseNo, Long.valueOf(happy.getCustomerid()));
					if (cWareHouse != null) {
						cWareHouseId = cWareHouse.getWarehouseid();
					}
				}

				List<Map<String, Object>> orderList = (List<Map<String, Object>>) map.get("orderlist");
				List<Map<String, String>> happymap = BuildHuishouList(orderList, happy, flag);
				dataImportservice.Analizy_DataDealByB2cHappyGoByEmaildate(Long.parseLong(happy.getCustomerid()), B2cEnum.happyGo.getMethod(), happymap, happy.getWarehouseid(), true,
						list.getSendtime(), cWareHouseId);
				happyGoUsedDao.getupdatebatchBybatchname(list.getBatchname());
				logger.info("快乐购回收下载订单信息调用数据导入接口-插入数据库成功!");

				return happymap.size();

			} else {
				happyGoUsedDao.getupdateStateBybatchname(list.getBatchname());
				return 0;
			}
		} catch (Exception e) {
			logger.error("快乐购回收下载出库明细订单信息异常", e);
			return 0;
		}
	}

	/*
	 * 回收订单 1，退货的回收30 2.换货的回收42
	 */

	private List<Map<String, String>> BuildHuishouList(List<Map<String, Object>> packageOne, HappyGo happy, String dgle) {

		List<Map<String, String>> packageList = new ArrayList<Map<String, String>>();

		for (Map<String, Object> p1 : packageOne) {
			Map<String, String> map = new HashMap<String, String>();
			CwbOrderDTO cwbOrder = dataImportDAO_B2c.getCwbByHappyCwbB2ctemp((String) p1.get("wb_no"));
			if (cwbOrder != null) {
				logger.warn("获取快乐购临时表--回收订单中含有重复数据cwb={}", p1.get("wb_no"));
				continue;
			}
			String warhouseid = "0";
			String wareid = p1.get("wh_code").toString();
			if (wareid != null && wareid.length() > 0) {

				if (!customWarehouseDAO.isExistsWarehouFlag(wareid, happy.getCustomerid())) {
					CustomWareHouse custwarehouse = new CustomWareHouse();
					custwarehouse.setCustomerid(Long.valueOf(happy.getCustomerid()));
					custwarehouse.setCustomerwarehouse(wareid);
					custwarehouse.setWarehouse_no(wareid);
					custwarehouse.setWarehouseremark("");
					custwarehouse.setIfeffectflag(1);
					customWarehouseDAO.creCustomer(custwarehouse);
					logger.info("【快乐购】没有库房【新建】{}={}", custwarehouse);
				}
				warhouseid = dataImportService_B2c.getCustomerWarehouseNo(wareid, happy.getCustomerid());
			}
			String noticetype = (String) p1.get("notice_type");
			String trle = "";
			String flow = "";
			String mobile = p1.get("receiver_tel") == null ? "无" : (String) p1.get("receiver_tel");
			map.put("consigneename", (String) p1.get("receiver_name"));//
			map.put("transcwb", (String) p1.get("order_no"));//
			map.put("consigneeaddress", (String) p1.get("receiver_addr"));// 地址
			map.put("consigneemobile", mobile);//
			map.put("sendcarname", (String) p1.get("item_name"));//
			map.put("customerwarehouseid", warhouseid);
			map.put("cwb", (String) p1.get("wb_no"));//
			map.put("remark3", "出库时间：" + p1.get("nowdate") + "&" + "处理时间：" + p1.get("send_date"));
			map.put("sendcarnum", (String) p1.get("piece"));// 默认为1,防止订单扫描时定义为一票多件
			map.put("cwbremark", (String) p1.get("msg"));
			map.put("consigneephone", mobile);
			map.put("remark2", dgle + "仓库：" + (String) p1.get("wh_code"));
			String Weight = p1.get("weight") == null ? "0" : String.valueOf(p1.get("weight"));
			map.put("cargorealweight", Weight);//
			if (!"".equals((String) p1.get("order_gb"))) {
				int aTrle = Integer.parseInt((String) p1.get("order_gb"));
				if (aTrle == FunctionForHappy.ee.getKey()) {// 退货的回收
					trle = "退货的回收";
					map.put("cwbordertypeid", String.valueOf(CwbOrderTypeIdEnum.Shangmentui.getValue()));
					map.put("isaudit", "2");
				} else {// 换货的回收
					trle = "换货的回收";
					try {// 修改
						CwbOrderDTO branchid = dataImportDAO_B2c.getbranchByCwbandtranscwb(Long.valueOf(map.get("cwb")) - 1, (String) p1.get("order_no"));
						if (branchid != null) {
							String transcwb = map.get("cwb");
							dataImportDAO_B2c.updateTransCwbByCwbs(transcwb, (String) p1.get("order_no"));
							continue;
						} else {
							map.put("cwbordertypeid", String.valueOf(CwbOrderTypeIdEnum.Shangmenhuan.getValue()));
							map.put("paywayid", "1");
							map.put("remark1", "");
							map.put("isaudit", "1");
							map.put("commoncwb", (String) p1.get("order_no"));
							logger.info("没有换货的出库订单---换货的回收订单号为：{}", map.get("cwb"));
							packageList.add(map);
							continue;
						}
					} catch (Exception e) {
						logger.error("异常", e);
					}
					map.put("cwbordertypeid", String.valueOf(CwbOrderTypeIdEnum.Shangmenhuan.getValue()));
				}
			}
			if (!"".equals(noticetype)) {
				for (FunctionForHappy f : FunctionForHappy.values()) {
					if (String.valueOf(f.getKey()).equals("1" + noticetype)) {
						flow = f.getText();
					}

				}
			}
			// 回收的订单
			map.put("paywayid", "1");
			map.put("remark1", "订单分类：" + trle + "--退货取件方式：" + flow);// 代收货款类型
			packageList.add(map);

		}
		return packageList;
	}

	/*
	 * 正常出库 1，正常配送10 2.换货的出库41
	 */
	public List<Map<String, String>> BuildChukuList(List<Map<String, Object>> packageOne, HappyGo happy, String dgle) {

		List<Map<String, String>> packageList = new ArrayList<Map<String, String>>();

		for (Map<String, Object> p1 : packageOne) {
			Map<String, String> map = new HashMap<String, String>();
			CwbOrderDTO cwbOrder = dataImportDAO_B2c.getCwbByHappyCwbB2ctemp((String) p1.get("wb_no"));// 运单号
			if (cwbOrder != null) {
				logger.warn("获取快乐购--出库临时表订单中含有重复数据cwb={}", p1.get("wb_no"));
				continue;
			}
			String warhouseid = "0";
			String wareid = p1.get("wh_code").toString();
			if (wareid != null && wareid.length() > 0) {

				if (!customWarehouseDAO.isExistsWarehouFlag(wareid, happy.getCustomerid())) {
					CustomWareHouse custwarehouse = new CustomWareHouse();
					custwarehouse.setCustomerid(Long.valueOf(happy.getCustomerid()));
					custwarehouse.setCustomerwarehouse(wareid);
					custwarehouse.setWarehouse_no(wareid);
					custwarehouse.setWarehouseremark("");
					custwarehouse.setIfeffectflag(1);
					customWarehouseDAO.creCustomer(custwarehouse);
					logger.info("【快乐购】没有库房【新建】{}={}", custwarehouse);
				}
				warhouseid = dataImportService_B2c.getCustomerWarehouseNo(wareid, happy.getCustomerid());
			}
			String noticetype = (String) p1.get("notice_type");
			int s = Integer.parseInt(noticetype);
			String mobile = p1.get("receiver_tel") == null ? "无" : (String) p1.get("receiver_tel");
			map.put("consigneename", (String) p1.get("receiver_name"));//
			map.put("transcwb", (String) p1.get("order_no"));//
			map.put("consigneeaddress", (String) p1.get("receiver_addr"));// 地址
			map.put("consigneemobile", mobile);//
			map.put("sendcarname", (String) p1.get("item_name"));//
			map.put("customerwarehouseid", warhouseid);
			map.put("cwb", (String) p1.get("wb_no"));//
			map.put("remark3", "出库时间：" + p1.get("nowdate") + "&" + "处理时间：" + p1.get("send_date"));
			map.put("sendcarnum", (String) p1.get("piece"));// 默认为1,防止订单扫描时定义为一票多件
			map.put("cwbremark", (String) p1.get("msg"));
			map.put("consigneephone", mobile);
			map.put("remark2", dgle + "仓库：" + (String) p1.get("wh_code"));
			String Weight = p1.get("weight") == null ? "0" : String.valueOf(p1.get("weight"));
			map.put("cargorealweight", Weight);//

			String trle = "";
			if (!"".equals((String) p1.get("order_gb"))) {
				int aTrle = Integer.parseInt((String) p1.get("order_gb"));
				if (aTrle == FunctionForHappy.ww.getKey()) {// 正常的出库
					trle = "正常的出库";
					map.put("cwbordertypeid", String.valueOf(CwbOrderTypeIdEnum.Peisong.getValue()));
					map.put("isaudit", "2");
				} else {// 上门换
					trle = "换货的出库";
					map.put("cwbordertypeid", String.valueOf(CwbOrderTypeIdEnum.Shangmenhuan.getValue()));
					map.put("isaudit", "1");
					map.put("commoncwb", (String) p1.get("order_no"));
				}
			}
			if (s < 3) {
				map.put("paywayid", noticetype);
				map.put("receivablefee", (String) p1.get("notice_amt"));//
			} else {
				map.put("paywayid", String.valueOf(PaytypeEnum.Xianjin.getValue()));
				map.put("receivablefee", "0");//
			}

			map.put("remark1", "订单分类：" + trle);// 代收货款类型
			packageList.add(map);

		}
		return packageList;
	}

	private String getMD5GetherForSign(HappyGo happy, String xml, String strMethod, StringBuffer sbSystemArgs) {
		long ate = System.currentTimeMillis();
		String a = String.valueOf(ate);
		String b = a.substring(0, 10);

		sbSystemArgs.append("app_id=" + happy.getCode());
		sbSystemArgs.append("&charset=utf-8");
		sbSystemArgs.append("&data_type=xml");
		sbSystemArgs.append("&function_id=" + strMethod);

		sbSystemArgs.append("&sign_type=md5");
		sbSystemArgs.append("&time=" + b);
		sbSystemArgs.append("&version=1.0");
		String md5Date = sbSystemArgs.toString() + xml + happy.getKey();
		String sign = MD5Util.md5(md5Date).toUpperCase();
		return sign;
	}

	public HappyGo getHappyGo(int key) {
		HappyGo happy = new HappyGo();
		if (!"".equals(getObjectMethod(key))) {
			JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
			happy = (HappyGo) JSONObject.toBean(jsonObj, HappyGo.class);
		} else {
			happy = new HappyGo();
		}
		return happy == null ? new HappyGo() : happy;
	}

	private String getObjectMethod(int key) {
		JointEntity obj = null;
		String posValue = "";
		try {
			obj = jiontDAO.getJointEntity(key);
			posValue = obj.getJoint_property();
		} catch (Exception e) {
		}
		return posValue;
	}

	public void update(int joint_num, int state) {
		jiontDAO.UpdateState(joint_num, state);
	}

	@Transactional
	public void edit(HttpServletRequest request, int joint_num) {
		HappyGo happy = new HappyGo();
		String key = StringUtil.nullConvertToEmptyString(request.getParameter("key"));
		String postUrl = StringUtil.nullConvertToEmptyString(request.getParameter("postUrl"));
		String customerid = StringUtil.nullConvertToEmptyString(request.getParameter("customerid"));
		String code = StringUtil.nullConvertToEmptyString(request.getParameter("code_happ"));
		String postkey = StringUtil.nullConvertToEmptyString(request.getParameter("postkey"));
		String location = StringUtil.nullConvertToEmptyString(request.getParameter("locationpost"));
		String time = StringUtil.nullConvertToEmptyString(request.getParameter("time"));
		String pagesize = request.getParameter("pagesize");

		String warehouseid = request.getParameter("warehouseid");
		int count = Integer.parseInt(pagesize);
		int warehouse = Integer.parseInt(warehouseid);
		happy.setCustomerid(customerid);
		happy.setKey(key);
		happy.setPostUrl(postUrl);
		happy.setWarehouseid(warehouse);
		happy.setPagesize(count);
		happy.setCode(code);
		happy.setLocation(location);
		happy.setPostkey(postkey);
		happy.setTime(time);
		JSONObject jsonObj = JSONObject.fromObject(happy);
		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		String oldCustomerids = "";
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerids = getHappyGo(joint_num).getCustomerid();
			} catch (Exception e) {
			}
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Update(jointEntity);
		}
		// 保存 枚举到供货商表中
		customerDAO.updateB2cEnumByJoint_num(happy.getCustomerid(), oldCustomerids, joint_num);
		this.customerService.initCustomerList();
	}

	private String requestXMl(int pagesize, HappyGo happy) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 当前时间
		Date date = new Date();
		// 格式化时间
		String endtime = sf.format(date);
		// 获得昨天的时间
		String time = happy.getTime();
		if ("".equals(time) || time == null) {
			time = "2";
		}
		int data3 = Integer.parseInt(time);
		Date date2 = new Date(date.getTime() - data3 * 6 * 30 * 30 * 1000);
		String starttime = sf.format(date2);
		String xml = "<Program>" + "<parameters>" + "<from_date>" + starttime + "</from_date>" + "<to_date>" + endtime + "</to_date>" + "</parameters>" + "<page>" + "<page_size>" + pagesize
				+ "</page_size>" + "<page_no>0</page_no >" + "</page>" + "</Program>";

		return xml;
	}

	private String inserGetxml(HappyMethod happy, HappyGo hapGo, int i) {
		String xml = "<Program>" + "<parameters>" + "<filename>" + happy.getBatchname() + "</filename>" + "<send_degree>" + happy.getBatchno() + "</send_degree>" + "</parameters>" + "<page>"
				+ "<page_no>" + i + "</page_no>" + "<page_size>" + hapGo.getPagesize() + "</page_size>" + "</page>" + "</Program>";
		return xml;
	}

	static public String doRequest(String sSendXml, String strUrl, HappyGo happy) {
		System.setProperty("javax.net.ssl.trustStore", happy.getLocation());
		System.setProperty("javax.net.ssl.trustStorePassword", happy.getPostkey());
		String value = "";
		OutputStream outputstream = null;
		InputStream ins = null;
		InputStreamReader inreder = null;
		BufferedReader in = null;
		try {
			URL url = new URL(strUrl);
			HttpURLConnection hcon = null;
			hcon = (HttpURLConnection) url.openConnection();
			hcon.setRequestProperty("Content-type", "text/html");
			hcon.setRequestProperty("Content-length", String.valueOf(-1));
			hcon.setRequestProperty("HTTP-Version", "HTTP/1.0");
			hcon.setConnectTimeout(10000);
			hcon.setReadTimeout(6000);
			hcon.setUseCaches(false);
			hcon.setDefaultUseCaches(false);
			hcon.setDoOutput(true);

			hcon.setRequestMethod("POST");
			hcon.addRequestProperty("POST", "/  HTTP/1.1");
			outputstream = hcon.getOutputStream();
			outputstream.write(sSendXml.getBytes("UTF-8"));
			outputstream.flush();
			outputstream.close();

			ins = hcon.getInputStream();
			inreder = new InputStreamReader(ins, "UTF-8");

			in = new BufferedReader(inreder);
			StringBuffer buffer = new StringBuffer();

			String line = "";
			while ((line = in.readLine()) != null)
				buffer.append(line);

			inreder.close();
			ins.close();
			in.close();
			String text = buffer.toString();
			buffer = null;

			int code = hcon.getResponseCode();
			if (code == 200) {
				value = text;
			} else {
				logger.info("错误代码" + code);
			}
		} catch (Exception e) {
			try {
				throw new Exception("dealRequest,调用远程接口失败，接口不可达或中途出现异常，详细错误信息：" + e.getMessage(), e);
			} catch (Exception e1) {
				logger.error("", e1);
			}
		} finally {
			try {
				if (outputstream != null) {
					outputstream.close();
				}
				if (inreder != null) {
					inreder.close();
				}
				if (ins != null) {
					ins.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
				try {
					throw new Exception("dealRequest,调用远程接口失败，资源释放异常，详细错误信息：" + e.getMessage(), e);
				} catch (Exception e1) {
					logger.error("", e1);
				}
			}
		}

		return value;
	}

	public void manageShappygo(String number, Map<String, Object> map) {
		if (map.get("error_type").equals("0")) {// 成功
			List<Map<String, Object>> listmap = (List<Map<String, Object>>) map.get("orderlist");
			if (listmap == null || listmap.size() < 1) {
				return;
			}
			for (Map<String, Object> l : listmap) {
				try {
					long count = happyGoUsedDao.getbatchBybatchname((String) l.get("filename"));
					if (count > 0) {
						continue;
					}
					logger.info("获得快乐购" + number + "批次有：{}", listmap.size());
					if (listmap.size() > 0) {
						if (FunctionForHappy.HOPE000001.getText().equals(number)) {
							happyGoUsedDao.getInsertBatch(l, FunctionForHappy.ChuKu.getKey());// 导入批次表
						}
						if (FunctionForHappy.HOPE000006.getText().equals(number)) {
							happyGoUsedDao.getInsertBatch(l, FunctionForHappy.HuiShou.getKey());// 导入批次表
						}
					}
				} catch (Exception e) {
					logger.error("快乐购插入新表异常", e);
				}
			}

		}
	}

	public void timerHappy() {
		try {
			HappyGo happy = getHappyGo(B2cEnum.happyGo.getKey());
			int isOpenFlag = jointService.getStateForJoint(B2cEnum.happyGo.getKey());

			if (isOpenFlag == 0) {
				logger.info("未开启[快乐购]对接！---当前获取订单插入临时表-----");
				return;
			}
			List<CwbOrderDTO> cwbOrderList = dataImportDAO_B2c.getCwbOrderTempByKeys(happy.getCustomerid());
			if (cwbOrderList != null && cwbOrderList.size() > 0) {
				for (CwbOrderDTO cwbOrder : cwbOrderList) {
					try {
						CwbOrder order = cwbDAO.getCwbByCwb(cwbOrder.getCwb());
						if (order != null) { // 要合单子
							logger.warn("[快乐购]查询临时表-检测到有重复数据,已过滤!订单号：{},运单号:{}", cwbOrder.getCwb(), cwbOrder.getTranscwb());
						} else {
							User user = new User();
							user.setUserid(1);
							long warehouse_id = happy.getWarehouseid();
							long warehouseid = warehouse_id != 0 ? warehouse_id : dataImportService_B2c.getTempWarehouseIdForB2c(); // 获取虚拟库房
																																	// Id,所有的B2C对接都会导入默认的虚拟库房里面，方便能够统计到。
							user.setBranchid(warehouseid);
							EmailDate ed = dataImportService.getEmailDate_B2CByEmaildate(cwbOrder.getCustomerid(), cwbOrder.getCustomerwarehouseid(), warehouseid, cwbOrder.getEmaildate());

							emaildateDAO.editEditEmaildateForCwbcountAdd(ed.getEmaildateid());
							cwbOrderService.insertCwbOrder(cwbOrder, cwbOrder.getCustomerid(), ed.getWarehouseid(), user, ed);
							logger.info("[快乐购]定时器临时表插入detail表成功!cwb={},trancwb={}", cwbOrder.getCwb(), cwbOrder.getTranscwb());

							if (cwbOrder.getExcelbranch() == null || cwbOrder.getExcelbranch().length() == 0) {
								HashMap<String, Object> map = new HashMap<String, Object>();
								map.put("cwb", cwbOrder.getCwb());
								map.put("userid", "1");
								try{
									addressmatch.sendBodyAndHeaders(null, map);
								}catch(Exception e){
									logger.error("", e);
									//写MQ异常表
									this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode("timerHappy")
											.buildExceptionInfo(e.toString()).buildTopic(this.addressmatch.getDefaultEndpoint().getEndpointUri())
											.buildMessageHeaderObject(map).getMqException());
								}
							}
						}
						dataImportDAO_B2c.update_CwbDetailTempByCwb(cwbOrder.getOpscwbid());
					} catch (Exception e) {
						logger.error("[快乐购]定时器临时表插入执行异常!", e);
					}
				}

			}
		} catch (Exception e) {
			logger.error("[快乐购]定时器临时表插入或修改方法执行异常!", e);
		}
	}

	public void happyGoForDetail() {
		try {
			HappyGo happy = getHappyGo(B2cEnum.happyGo.getKey());
			int isOpenFlag = jointService.getStateForJoint(B2cEnum.happyGo.getKey());

			if (isOpenFlag == 0) {
				logger.info("未开启[快乐购]对接！---当前获取订单插入临时表-----");
				return;
			}
			List<CwbOrderDTO> cwbOrderList = dataImportDAO_B2c.getHappyGoByKeys(happy.getCustomerid());
			if (cwbOrderList != null && cwbOrderList.size() > 0) {
				for (CwbOrderDTO cwbOrder : cwbOrderList) {
					try {

						User user = new User();
						user.setUserid(1);
						long warehouse_id = happy.getWarehouseid();
						long warehouseid = warehouse_id != 0 ? warehouse_id : dataImportService_B2c.getTempWarehouseIdForB2c(); // 获取虚拟库房
																																// Id,所有的B2C对接都会导入默认的虚拟库房里面，方便能够统计到。
						user.setBranchid(warehouseid);
						EmailDate ed = dataImportService.getEmailDate_B2CByEmaildate(cwbOrder.getCustomerid(), cwbOrder.getCustomerwarehouseid(), warehouseid, cwbOrder.getEmaildate());

						emaildateDAO.editEditEmaildateForCwbcountAdd(ed.getEmaildateid());
						dataImportDAO_B2c.insertCwbOrder_toTempTable(cwbOrder, cwbOrder.getCustomerid(), warehouseid, user, ed);
						logger.info("[快乐购]定时器临时表插入detail表成功!cwb={},trancwb={}", cwbOrder.getCwb(), cwbOrder.getTranscwb());

						if (cwbOrder.getExcelbranch() == null || cwbOrder.getExcelbranch().length() == 0) {
							HashMap<String, Object> map = new HashMap<String, Object>();
							map.put("cwb", cwbOrder.getCwb());
							map.put("userid", "1");
							try{
								addressmatch.sendBodyAndHeaders(null, map);
							}catch(Exception e){
								logger.error("", e);
								//写MQ异常表
								this.mqExceptionDAO.save(MqExceptionBuilder.getInstance().buildExceptionCode("happyGoForDetail")
										.buildExceptionInfo(e.toString()).buildTopic(this.addressmatch.getDefaultEndpoint().getEndpointUri())
										.buildMessageHeaderObject(map).getMqException());
							}

						}
						dataImportDAO_B2c.updateHappyGoByCwb(cwbOrder.getOpscwbid());
					} catch (Exception e) {
						logger.error("定时器异常", e);
					}
				}
			}
		} catch (Exception e) {
			logger.error("[快乐购]定时器临时表插入或修改方法执行异常!", e);
		}
	}

	public String ggHappy(String retrun_xml, String code, HappyGo happy, String PICI, String time) {
		Map<String, Object> map;
		if ("HOPE000007".equals(code)) {
			try {
				logger.info("获得返回快乐购HOPE000007的xml{},批次号是：{}", retrun_xml);
				if ("".equals(retrun_xml)) {
					logger.info("获得返回快乐购HOPE000007的xml为空，不必解析：{}" + DateTimeUtil.getNowTime());
				}
				map = AnalyzXMLJuMeiHandler.parserXmlToJSONObjectByArray(retrun_xml);
				if ("0".equals(map.get("error_type"))) {
					long cWareHouseId = 0;
					List<Map<String, Object>> orderList = (List<Map<String, Object>>) map.get("orderlist");
					String wareHouseNo = (String) orderList.get(0).get("wh_code");
					if (!customWarehouseDAO.isExistsWarehouFlag(wareHouseNo, happy.getCustomerid())) {
						cWareHouseId = customWarehouseDAO.creCustomerGetId(Long.valueOf(happy.getCustomerid()), wareHouseNo, wareHouseNo);
					} else {
						CustomWareHouse cWareHouse = customWarehouseDAO.getCustomWareHouseByHousecode(wareHouseNo, Long.valueOf(happy.getCustomerid()));
						if (cWareHouse != null) {
							cWareHouseId = cWareHouse.getWarehouseid();
						}
					}

					List<Map<String, String>> happymap = BuildHuishouList(orderList, happy, "pull");
					dataImportservice.Analizy_DataDealByB2cHappyGoByEmaildate(Long.parseLong(happy.getCustomerid()), B2cEnum.happyGo.getMethod(), happymap, happy.getWarehouseid(), true, time,
							cWareHouseId);
					happyGoUsedDao.getupdatebatchBybatchname(PICI);
					logger.info("快乐购回收下载订单信息调用数据导入接口-插入数据库成功!");

				} else {
					happyGoUsedDao.getupdateStateBybatchname(PICI);
				}
			} catch (Exception e) {
				logger.error("快乐购回收下载出库明细订单信息异常", e);
				return "7回收异常";
			}
		} else {
			try {
				logger.info("获得返回快乐购HOPE000002的xml{},批次号是：{}", retrun_xml, PICI);

				if ("".equals(retrun_xml)) {
					logger.info("获得返回快乐购HOPE000002的xml为空，不必解析：{}" + DateTimeUtil.getNowTime());
				}

				map = AnalyzXMLJuMeiHandler.parserXmlToJSONObjectByArray(retrun_xml);

				if ("0".equals(map.get("error_type"))) {
					long cWareHouseId = 0;
					List<Map<String, Object>> orderList = (List<Map<String, Object>>) map.get("orderlist");
					String wareHouseNo = (String) orderList.get(0).get("wh_code");
					if (!customWarehouseDAO.isExistsWarehouFlag(wareHouseNo, happy.getCustomerid())) {
						cWareHouseId = customWarehouseDAO.creCustomerGetId(Long.valueOf(happy.getCustomerid()), wareHouseNo, wareHouseNo);
					} else {
						CustomWareHouse cWareHouse = customWarehouseDAO.getCustomWareHouseByHousecode(wareHouseNo, Long.valueOf(happy.getCustomerid()));
						if (cWareHouse != null) {
							cWareHouseId = cWareHouse.getWarehouseid();
						}
					}

					List<Map<String, String>> happymap = BuildChukuList(orderList, happy, "push");
					dataImportservice.Analizy_DataDealByB2cHappyGoByEmaildate(Long.parseLong(happy.getCustomerid()), B2cEnum.happyGo.getMethod(), happymap, happy.getWarehouseid(), true, time,
							cWareHouseId);
					happyGoUsedDao.getupdatebatchBybatchname(PICI);
					logger.info("快乐购下载订单信息调用数据导入接口-插入数据库成功!");

				} else {
					happyGoUsedDao.getupdateStateBybatchname(PICI);
				}
			} catch (Exception e) {
				logger.error("快乐购下载出库明细订单信息异常," + e);
				return "2出库异常";
			}

		}
		return "完毕";
	}

	public void gethappygoInfoTimmer() {
		try {
			HappyGo happy = getHappyGo(B2cEnum.happyGo.getKey());
			int isOpenFlag = jointService.getStateForJoint(B2cEnum.happyGo.getKey());

			if (isOpenFlag == 0) {
				logger.info("未开启[快乐购]对接！---当前获取需要关联订单-----");
				return;
			}
			List<CwbOrderDTO> cwbOrderList = dataImportDAO_B2c.getHappyGoInfoByIsaudit(happy.getCustomerid(), "1");
			if (cwbOrderList != null && cwbOrderList.size() > 0) {
				for (CwbOrderDTO cwbOrder : cwbOrderList) {
					try {
						List<CwbOrderDTO> listinfo = dataImportDAO_B2c.getHappyGoDetailByIsaudit(cwbOrder.getCommoncwb());
						if (listinfo.size() == 2) {
							String first = listinfo.get(0).getCwb();
							String end = listinfo.get(1).getCwb();
							if (Long.valueOf(first) - Long.valueOf(end) < 0) {// first是出库单子end是回收back单子
								logger.info("准备合并两个订单号,出库的为{}，回收的为{}", first, end);
								dataImportDAO_B2c.updateHappyGoDetailByIsaudit(listinfo.get(1).getCwb(), listinfo.get(0).getCwb());
								dataImportDAO_B2c.updateIsaudit(listinfo.get(1).getCwb());
							}
							if (Long.valueOf(first) - Long.valueOf(end) == 1) {// first是back回收单子end是出库单子
								logger.info("准备合并两个订单号,出库的为{}，回收的为{}", end, first);
								dataImportDAO_B2c.updateHappyGoDetailByIsaudit(listinfo.get(0).getCwb(), listinfo.get(1).getCwb());
								dataImportDAO_B2c.updateIsaudit(listinfo.get(0).getCwb());
							}

						}
					} catch (Exception e) {
						logger.error("定时器异常", e);
					}
				}
			}
		} catch (Exception e) {
			logger.error("[快乐购]定时器临时表插入或修改方法执行异常!", e);
		}

	}

}
