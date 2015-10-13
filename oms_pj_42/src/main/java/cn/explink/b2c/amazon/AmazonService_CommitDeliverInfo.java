package cn.explink.b2c.amazon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.explink.b2c.amazon.domain.Amazon;
import cn.explink.b2c.amazon.domain.AmazonInfo;
import cn.explink.b2c.tools.B2CCodDataDAO;
import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.BuildB2cDataMaster;
import cn.explink.b2c.tools.JacksonMapper;
import cn.explink.dao.CwbDAO;
import cn.explink.domain.B2CCodData;
import cn.explink.domain.B2CCodFile;
import cn.explink.domain.B2CData;
import cn.explink.domain.CwbOrder;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.jms.dto.DmpDeliveryState;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.DateTimeUtil;

@Service
public class AmazonService_CommitDeliverInfo extends AmazonaService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private B2cTools b2cTools;
	@Autowired
	private B2CDataDAO b2CDataDAO;
	@Autowired
	private B2CCodDataDAO b2CCodDataDAO;
	@Autowired
	BuildB2cDataMaster buildB2cDataMaster;
	@Autowired
	private CwbDAO cwbDAO;
	@Autowired
	private AmazonZitiDAO amazonZitiDAO;

	private ObjectMapper objectMapper = JacksonMapper.getInstance();

	/***
	 * 开始接口
	 * ======================================================================
	 * ============
	 */

	public void commitDeliverInfo_interface() {
		Amazon amazon = super.getAmazonSettingMethod(B2cEnum.Amazon.getKey());
		if (!b2cTools.isB2cOpen(B2cEnum.Amazon.getKey())) {
			logger.info("未开[亚马逊]状态反馈对接!");
			return;
		}
		commitDeliverInfo(amazon); // 所有的

	}

	/**
	 * 推送cod文件
	 */
	public void commitDeliver_Cod() {
		commitDeliverCod();
		commimtZiti();// 站点到站延迟订单
	}

	public void commitDeliverCod() {
		Amazon amazon = super.getAmazonSettingMethod(B2cEnum.Amazon.getKey());
		if (!b2cTools.isB2cOpen(B2cEnum.Amazon.getKey())) {
			logger.info("未开[亚马逊]状态反馈对接!");
			return;
		}
		while (true) {
			List<B2CCodData> datalist = b2CCodDataDAO.selectB2cMonitorDataList(amazon.getCustomerid(), 1000);
			if (datalist == null || datalist.size() == 0) {
				logger.info("当前没有推送给[亚马逊]的cod订单数据");
				return;
			} else {
				try {
					String fileXml = setTxtStr(datalist);
					SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
					String fileName = "dss_payment_" + amazon.getDssFile() + "_" + ((int) (Math.random() * 9000) + 1000) + "_" + df.format(new Date()) + ".txt"; // 文件名
					logger.info("COD写入路径：{},文件名：{}", amazon.getTnt_url(), fileName);
					int result = writerFile(amazon.getTnt_url(), fileName, fileXml);
					writerFile(amazon.getTnt_url_bak(), fileName, fileXml);

					if (result == 1) {
						dealCodByCwbs(datalist, 1, ""); // 修改配送结果
						logger.info("cod推送给亚马逊成功");
					} else {
						dealCodByCwbs(datalist, 2, "写入文件异常 ，推送失败"); // 修改配送结果
						logger.info("cod推送给亚马逊失败");
					}
				} catch (Exception e) {
					logger.error("[亚马逊]状态反馈发生未知异", e);
					return;
				}
			}
		}
	}

	public void commitDeliverInfo(Amazon amazon) {
		int i = 0;
		boolean tuotou = true;
		boolean notuotou = true;
		while (true) {
			i++;
			List<B2CData> datalist = null;
			if (amazon.getIsHebingTuotou() == 0) {
				datalist = b2CDataDAO.getDataListByFlowStatus(amazon.getCustomerid(), amazon.getMaxCount());
			} else {
				if (i % 2 != 0 && notuotou || !tuotou) {
					datalist = b2CDataDAO.getDataListByFlowByIsTuotou(amazon.getCustomerid(), amazon.getMaxCount(), 0);
					if (datalist == null || datalist.size() == 0) {
						notuotou = false;
						datalist = b2CDataDAO.getDataListByFlowByIsTuotou(amazon.getCustomerid(), amazon.getMaxCount(), 1);
					}
				} else if (tuotou || !notuotou) {
					datalist = b2CDataDAO.getDataListByFlowByIsTuotou(amazon.getCustomerid(), amazon.getMaxCount(), 1);
					if (datalist == null || datalist.size() == 0) {
						tuotou = false;
						datalist = b2CDataDAO.getDataListByFlowByIsTuotou(amazon.getCustomerid(), amazon.getMaxCount(), 0);
					}
				}
			}
			if (datalist == null || datalist.size() == 0) {
				logger.info("当前没有推送给[亚马逊]的订单数据,所有state为0的已经没有数据");
				return;
			} else {
				try {
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
					String fileName = "Shipment_Status_" + ((int) (Math.random() * 9000) + 1000) + "_" + df.format(new Date()) + ".xml"; // 文件名
					StringBuffer fileXml = new StringBuffer();
					fileXml.append(setHeadXml(amazon));
					for (B2CData b2cData : datalist) {
						AmazonInfo deliveryInfo = new ObjectMapper().readValue(b2cData.getJsoncontent(), AmazonInfo.class); // 构建DeliveryInfoSyn对象
						String orderxml = "";
						if (amazon.getIsHebingTuotou() == 0) {
							orderxml += setOrderToXml(deliveryInfo, amazon);
						} else {
							orderxml += setOrderToXmlToTuotou(deliveryInfo, amazon);
						}
						fileXml.append(orderxml);
						logger.info("订单号：" + b2cData.getCwb() + ",posttime:" + b2cData.getPosttime() + ",flowordertype:" + b2cData.getFlowordertype() + ",写入路径：{},文件名：{}", amazon.getTnt_url(),
								fileName);
					}
					fileXml.append(setEndXml());
					int result = writerFile(amazon.getTnt_url(), fileName, fileXml.toString());
					writerFile(amazon.getTnt_url_bak(), fileName, fileXml.toString());
					if (result == 1) {
						dealWithSendFlagUpdateByHand(datalist, 1, ""); // 修改配送结果
						logger.info("推送给亚马逊成功");
					} else {
						dealWithSendFlagUpdateByHand(datalist, 2, "写入文件异常 ，推送失败"); // 修改配送结果
						logger.info("推送给亚马逊失败");
					}
				} catch (Exception e) {
					logger.error("[亚马逊]状态反馈发生未知异", e);
					return;
				}
			}
		}

	}

	private String setHeadXml(Amazon amazon) {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<ShipmentInformation>\n" + "<TransactionInformation>\n" + "<SenderIdentifier>" + amazon.getSenderIdentifier()
				+ "</SenderIdentifier>\n" + "<RecipientIdentifier>" + amazon.getRecipientIdentifier() + "</RecipientIdentifier>\n" + "<DateOfPreparation>"
				+ new SimpleDateFormat("yyyyMMdd").format(new Date()) + "</DateOfPreparation>\n" + "<TimeOfPreparation>" + new SimpleDateFormat("HHmmss").format(new Date()) + "</TimeOfPreparation>\n"
				+ "</TransactionInformation>\n" + "<EdiDocumentInformation>\n" + "<EdiDocumentStandard>X12</EdiDocumentStandard>\n" + "<EdiDocumentName>EDI214</EdiDocumentName>\n"
				+ "<EdiDocumentVersion>1.1</EdiDocumentVersion>\n" + "</EdiDocumentInformation>\n";

		return xml;
	}

	private String setEndXml() {
		String xml = "</ShipmentInformation>";
		return xml;
	}

	/**
	 * @param amazonInfo
	 * @param amazon
	 * @return
	 */
	private String setOrderToXml(AmazonInfo amazonInfo, Amazon amazon) {
		String xml = "<ShipmentStatusSeq>\n" + "<ShipmentStatus>\n" + "<ShipmentIdentification>\n" + "<MessageReferenceNum>" + amazonInfo.getMessageReferenceNum() + "</MessageReferenceNum>\n"
				+ "<CarrierTrackingNumber>" + amazonInfo.getCarrierTrackingNum() + "</CarrierTrackingNumber>\n" + "</ShipmentIdentification>\n" + "<LocationOfFreight>\n" + "<Address>\n"
				+ "<Line1></Line1>\n" + "<Line2 />\n" + "<Line3 />\n" + "<district></district>\n";
		if (amazonInfo.getBranchAddress() != null && amazonInfo.getBranchAddress().indexOf(" ") > -1) {
			String[] adess = amazonInfo.getBranchAddress().split(" ");
			if (adess.length >= 2) {
				xml += "<City>" + adess[1] + "</City>\n" + "<StateProvinceCode>" + adess[0] + "</StateProvinceCode>\n";
			} else {
				xml += "<City></City>\n" + "<StateProvinceCode>" + (adess.length > 0 ? adess[0] : "") + "</StateProvinceCode>\n";
			}
		} else {
			xml += "<City></City>\n" + "<StateProvinceCode></StateProvinceCode>\n";
		}
		xml += "<PostalCode></PostalCode>\n" + "<CountryCode>CN</CountryCode>\n" + "</Address>\n" + "</LocationOfFreight>\n" + "<ShipmentStatusInformation>\n" + "<Status>"
				+ amazonInfo.getStatus()
				+ "</Status>\n"
				+ "<StatusReason>"
				+ amazonInfo.getStatusReason()
				+ "</StatusReason>\n"
				+ "</ShipmentStatusInformation>\n"
				+ (amazonInfo.getIsTuotou() == 1 ? ("<PaymentMethod>\n" + "<ActualPaymentMethod>" + amazonInfo.getPayType() + "</ActualPaymentMethod>\n" + "</PaymentMethod>\n")
						: "<PaymentMethod></PaymentMethod>\n")
				+ (amazonInfo.getPmcr_CODE() != null && !"".equals(amazonInfo.getPmcr_CODE()) ? ("<PaymentMethodChangeCode>\n" + "<PMCR_CODE>" + amazonInfo.getPmcr_CODE() + "</PMCR_CODE>\n" + "</PaymentMethodChangeCode>\n")
						: "<PaymentMethodChangeCode>\n" + "<PMCR_CODE></PMCR_CODE>\n</PaymentMethodChangeCode>\n")
				+ "<TransportInformation>\n"
				+ "<CarrierSCAC>"
				+ amazon.getCarrierSCAC()
				+ "</CarrierSCAC>\n"
				+ "<ContactInfo>\n"
				+ (amazon.getIsShow() == 1 ? "" + "<Name>"
						+ (amazonInfo.getDeliveryName() == null || "".equals(amazonInfo.getDeliveryName()) ? amazonInfo.getBranchName() : amazonInfo.getDeliveryName()) + "</Name>\n" + "<Phone>"
						+ (amazonInfo.getDeliveryPhone() == null || "".equals(amazonInfo.getDeliveryPhone()) ? amazonInfo.getBranchPhone() : amazonInfo.getDeliveryPhone()) + "</Phone>"
						: "<Name></Name>\n" + "<Phone></Phone>") + "</ContactInfo>\n" + "</TransportInformation>\n" + "<DateTimePeriodInformation>\n"
				+ "<DateTimePeriodCode>123</DateTimePeriodCode>\n" + "<DateTimePeriodFormat>203</DateTimePeriodFormat>\n" + "<DateTimePeriodValue>" + amazonInfo.getDateTimePeriodValue()
				+ "</DateTimePeriodValue>\n" + "</DateTimePeriodInformation>\n" + "</ShipmentStatus>\n" + "</ShipmentStatusSeq>\n";
		if (amazonInfo.getIsCach() == 1) {
			xml += "<ShipmentStatusSeq>\n" + "<ShipmentStatus>\n" + "<ShipmentIdentification>\n" + "<MessageReferenceNum>" + amazonInfo.getMessageReferenceNum() + "</MessageReferenceNum>\n"
					+ "<CarrierTrackingNumber>" + amazonInfo.getCarrierTrackingNum() + "</CarrierTrackingNumber>\n" + "</ShipmentIdentification>\n" + "<LocationOfFreight>\n" + "<Address>\n"
					+ "<Line1></Line1>\n" + "<Line2 />\n" + "<Line3 />\n" + "<district></district>\n";
			if (amazonInfo.getBranchAddress() != null && amazonInfo.getBranchAddress().indexOf(" ") > -1) {
				String[] adess = amazonInfo.getBranchAddress().split(" ");
				if (adess.length >= 2) {
					xml += "<City>" + adess[1] + "</City>\n" + "<StateProvinceCode>" + adess[0] + "</StateProvinceCode>\n";
				} else {
					xml += "<City></City>\n" + "<StateProvinceCode>" + (adess.length > 0 ? adess[0] : "") + "</StateProvinceCode>\n";
				}
			} else {
				xml += "<City></City>\n" + "<StateProvinceCode></StateProvinceCode>\n";
			}
			xml += "<PostalCode></PostalCode>\n"
					+ "<CountryCode></CountryCode>\n"
					+ "</Address>\n"
					+ "</LocationOfFreight>\n"
					+ "<ShipmentStatusInformation>\n"
					+ "<Status>D1</Status>\n"
					+ "<StatusReason>ZS</StatusReason>\n"
					+ "</ShipmentStatusInformation>\n"
					+ "<PaymentMethodChangeCode>\n"
					+ "<PMCR_CODE></PMCR_CODE>\n"
					+ "</PaymentMethodChangeCode>\n"
					+ "<TransportInformation>\n"
					+ "<CarrierSCAC>"
					+ amazon.getCarrierSCAC()
					+ "</CarrierSCAC>\n"
					+ "<ContactInfo>\n"
					+ (amazon.getIsShow() == 1 ? "" + "<Name>"
							+ (amazonInfo.getDeliveryName() == null || "".equals(amazonInfo.getDeliveryName()) ? amazonInfo.getBranchName() : amazonInfo.getDeliveryName()) + "</Name>\n" + "<Phone>"
							+ (amazonInfo.getDeliveryPhone() == null || "".equals(amazonInfo.getDeliveryPhone()) ? amazonInfo.getBranchPhone() : amazonInfo.getDeliveryPhone()) + "</Phone>"
							: "<Name></Name>\n" + "<Phone></Phone>") + "</ContactInfo>\n" + "</TransportInformation>\n" + "<DateTimePeriodInformation>\n"
					+ "<DateTimePeriodCode>123</DateTimePeriodCode>\n" + "<DateTimePeriodFormat>203</DateTimePeriodFormat>\n" + "<DateTimePeriodValue>" + amazonInfo.getDateTime5PeriodValue()
					+ "</DateTimePeriodValue>\n" + "</DateTimePeriodInformation>\n" + "</ShipmentStatus>\n" + "</ShipmentStatusSeq>\n";
		}
		return xml;
	}

	/**
	 * @param amazonInfo
	 * @param amazon
	 * @return
	 */
	private String setOrderToXmlToTuotou(AmazonInfo amazonInfo, Amazon amazon) {
		String xml = "<ShipmentStatusSeq>\n" + "<ShipmentStatus>\n" + "<ShipmentIdentification>\n" + "<MessageReferenceNum>" + amazonInfo.getMessageReferenceNum() + "</MessageReferenceNum>\n"
				+ "<CarrierTrackingNumber>" + amazonInfo.getCarrierTrackingNum() + "</CarrierTrackingNumber>\n" + "</ShipmentIdentification>\n" + "<LocationOfFreight>\n" + "<Address>\n"
				+ "<Line1></Line1>\n" + "<Line2 />\n" + "<Line3 />\n" + "<district></district>\n";
		if (amazonInfo.getBranchAddress() != null && amazonInfo.getBranchAddress().indexOf(" ") > -1) {
			String[] adess = amazonInfo.getBranchAddress().split(" ");
			if (adess.length >= 2) {
				xml += "<City>" + adess[1] + "</City>\n" + "<StateProvinceCode>" + adess[0] + "</StateProvinceCode>\n";
			} else {
				xml += "<City></City>\n" + "<StateProvinceCode>" + (adess.length > 0 ? adess[0] : "") + "</StateProvinceCode>\n";
			}
		} else {
			xml += "<City></City>\n" + "<StateProvinceCode></StateProvinceCode>\n";
		}
		xml += "<PostalCode></PostalCode>\n" + "<CountryCode>CN</CountryCode>\n" + "</Address>\n" + "</LocationOfFreight>\n" + "<ShipmentStatusInformation>\n" + "<Status>"
				+ amazonInfo.getStatus()
				+ "</Status>\n"
				+ "<StatusReason>"
				+ amazonInfo.getStatusReason()
				+ "</StatusReason>\n"
				+ "</ShipmentStatusInformation>\n"
				+ (amazonInfo.getIsTuotou() == 1 ? ("<PaymentMethod>\n" + "<ActualPaymentMethod>" + amazonInfo.getPayType() + "</ActualPaymentMethod>\n" + "</PaymentMethod>\n")
						: "<PaymentMethod></PaymentMethod>\n")
				+ (amazonInfo.getPmcr_CODE() != null && !"".equals(amazonInfo.getPmcr_CODE()) ? ("<PaymentMethodChangeCode>\n" + "<PMCR_CODE>" + amazonInfo.getPmcr_CODE() + "</PMCR_CODE>\n" + "</PaymentMethodChangeCode>\n")
						: "<PaymentMethodChangeCode>\n" + "<PMCR_CODE></PMCR_CODE>\n</PaymentMethodChangeCode>\n")
				+ "<TransportInformation>\n"
				+ "<CarrierSCAC>"
				+ amazon.getCarrierSCAC()
				+ "</CarrierSCAC>\n"
				+ "<ContactInfo>\n"
				+ (amazon.getIsShow() == 1 ? "" + "<Name>"
						+ (amazonInfo.getDeliveryName() == null || "".equals(amazonInfo.getDeliveryName()) ? amazonInfo.getBranchName() : amazonInfo.getDeliveryName()) + "</Name>\n" + "<Phone>"
						+ (amazonInfo.getDeliveryPhone() == null || "".equals(amazonInfo.getDeliveryPhone()) ? amazonInfo.getBranchPhone() : amazonInfo.getDeliveryPhone()) + "</Phone>"
						: "<Name></Name>\n" + "<Phone></Phone>") + "</ContactInfo>\n" + "</TransportInformation>\n" + "<DateTimePeriodInformation>\n"
				+ "<DateTimePeriodCode>123</DateTimePeriodCode>\n" + "<DateTimePeriodFormat>203</DateTimePeriodFormat>\n" + "<DateTimePeriodValue>" + amazonInfo.getDateTimePeriodValue()
				+ "</DateTimePeriodValue>\n" + "</DateTimePeriodInformation>\n" + "</ShipmentStatus>\n" + "</ShipmentStatusSeq>\n";
		return xml;
	}

	//

	private String setTxtStr(List<B2CCodData> datalist) {
		StringBuffer txtStr = new StringBuffer();
		B2CCodFile bfile = b2CCodDataDAO.getFileCount(1);
		long fileCount = 1;
		if (bfile == null) {
			b2CCodDataDAO.saveB2CFile(1);
		} else {
			fileCount = bfile.getFilecount();
			b2CCodDataDAO.updateFileCount(1, (bfile.getFilecount() + 1) > 999999 ? 1 : bfile.getFilecount() + 1);
		}
		String header = AmazonDataToTxtUtil.getHeader(fileCount);
		txtStr.append(header);
		long codMoney = 0;
		long susMoney = 0;
		long filMoney = 0;
		double codMoneyD = 0;
		double susMoneyD = 0;
		double filMoneyD = 0;
		for (B2CCodData b2cCodData : datalist) {
			String detailStr = AmazonDataToTxtUtil.getDetail(b2cCodData);
			txtStr.append(detailStr);
			JSONObject json = JSONObject.fromObject(b2cCodData.getDatajson());
			codMoneyD += json.getDouble("businessfee");
			if (json.getDouble("receivedfee") > 0 && json.getInt("deliverystate") == DeliveryStateEnum.PeiSongChengGong.getValue()) {// 妥投
				susMoneyD += json.getDouble("businessfee");
			} else {
				filMoneyD += json.getDouble("businessfee");
			}
		}
		codMoney = (long) (codMoneyD * 100);
		susMoney = (long) (susMoneyD * 100);
		filMoney = (long) (filMoneyD * 100);
		String endStr = AmazonDataToTxtUtil.getEnd(datalist.size() + "", codMoney + "", susMoney + "", filMoney + "");
		txtStr.append(endStr);

		return txtStr.toString();
	}

	// 修改处理结果的方法
	private void dealWithSendFlagUpdateByHand(List<B2CData> datalist, int state, String msg) throws Exception {
		for (B2CData b2cData : datalist) {
			b2CDataDAO.updateB2cIdSQLResponseStatus(b2cData.getB2cid(), state, msg);
		}

	}

	// 修改处理结果的方法
	private void dealCodByCwbs(List<B2CCodData> datalist, int state, String msg) throws Exception {
		String cwbs = "";
		for (B2CCodData b2cData : datalist) {
			cwbs += "'" + b2cData.getCwb() + "',";

		}
		cwbs = cwbs.length() > 0 ? cwbs.substring(0, cwbs.length() - 1) : "";
		if (cwbs.length() > 0) {
			b2CCodDataDAO.updateStateByCwbs(cwbs, state);
		}

	}

	/**
	 * 写文件的方法
	 * 
	 * @param url
	 * @param fileName
	 * @param fileXml
	 * @return
	 */
	private int writerFile(String url, String fileName, String fileXml) {
		OutputStreamWriter pw_dc = null;
		try {
			File file = new File(url + fileName); // 创建文件txt
			File f = new File(url); // 创建文件txt
			if (!f.exists()) {
				f.mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			pw_dc = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
			pw_dc.write(fileXml);
			pw_dc.flush();
			pw_dc.close();
			return 1;
		} catch (Exception e) {
			logger.error("写文件异常", e);
			e.printStackTrace();
			return 0;
		}
	}

	private static String readFileByLines(String url, String fileName) {
		File file = new File(url + fileName);
		BufferedReader reader = null;
		StringBuffer xmlStr = new StringBuffer();
		try {
			System.out.println("以行为单位读取文件内容，一次读一整行：");
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				xmlStr.append(tempString);
			}
			reader.close();
			return xmlStr.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
	}

	public static void main(String[] args) {
		// String a = readFileByLines("C:/", "add3.xml");
		// System.out.println(a);
		//
		File file = new File("C:/");
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (!files[i].isDirectory() && files[i].toString().indexOf(".xml") > -1) {
				System.out.println(files[i]);
			}
		}
	}

	public void commimtZiti() {

		Amazon amazon = super.getAmazonSettingMethod(B2cEnum.Amazon.getKey());
		if (!b2cTools.isB2cOpen(B2cEnum.Amazon.getKey())) {
			logger.info("未开[亚马逊]状态反馈对接!");
			return;
		}
		if (amazon != null && amazon.getIsSystemCommit() == 0) {// 不使用系统自动推送
			logger.info("[亚马逊]未开启自动推送延迟状态!");
			return;
		}
		while (true) {

			List<CwbOrder> datalist = cwbDAO.getAmazonZitiByPage(new Date().getTime() - (1000 * 60 * 24 * (amazon.getDelay() == 0 ? 1 : amazon.getDelay())), amazon.getMaxCount());
			if (datalist == null || datalist.size() == 0) {
				logger.info("当前没有推送给[亚马逊]的cod订单数据");
				return;
			} else {
				for (CwbOrder co : datalist) {
					DmpOrderFlow orderFlow = new DmpOrderFlow();
					orderFlow.setFlowordertype(FlowOrderTypeEnum.ZiTiYanWu.getValue());
					orderFlow.setCredate(new Timestamp(System.currentTimeMillis()));
					orderFlow.setCwb(co.getCwb());

					DmpCwbOrder cwbOrder = new DmpCwbOrder();
					cwbOrder.setCwb(co.getCwb());
					cwbOrder.setShipcwb(co.getShipcwb());
					cwbOrder.setRemark5(co.getRemark5());
					cwbOrder.setCwbcity(co.getCwbcity());
					cwbOrder.setConsigneeaddress(co.getConsigneeaddress());
					cwbOrder.setConsigneename(co.getConsigneename());
					cwbOrder.setConsigneeno(co.getConsigneeno());
					cwbOrder.setCwbprovince(co.getCwbprovince());
					cwbOrder.setCwbordertypeid(co.getCwbordertypeid());
					cwbOrder.setCartype(co.getCartype());
					cwbOrder.setCustomerid(co.getCustomerid());
					long flowOrdertype = FlowOrderTypeEnum.ZiTiYanWu.getValue();
					DmpDeliveryState deliveryState = null;
					long delivery_state = 0;
					try {
						String jsoncontent = buildB2cDataMaster.getBuildAmazonB2cData().BuildAmazonMethod(orderFlow, flowOrdertype, cwbOrder, deliveryState, delivery_state, objectMapper);
						B2CData b2cData = new B2CData();
						b2cData.setCwb(orderFlow.getCwb());
						b2cData.setCustomerid(cwbOrder.getCustomerid());
						b2cData.setFlowordertype(orderFlow.getFlowordertype());
						b2cData.setPosttime(DateTimeUtil.formatDate(orderFlow.getCredate()));
						b2cData.setSend_b2c_flag(0);
						b2cData.setJsoncontent(jsoncontent); // 封装的JSON格式.
						String multi_shipcwb = StringUtils.hasLength(cwbOrder.getMulti_shipcwb()) ? cwbOrder.getMulti_shipcwb() : null;
						b2CDataDAO.saveB2CData(b2cData, multi_shipcwb);
						amazonZitiDAO.updateAmazonZiti(co.getCwb());
					} catch (JsonGenerationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JsonMappingException e) {

						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}

	}

}
