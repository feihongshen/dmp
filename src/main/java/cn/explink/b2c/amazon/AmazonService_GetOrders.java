package cn.explink.b2c.amazon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import net.sf.json.JSONObject;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.amazon.domain.AmazonUnmarchal;
import cn.explink.b2c.amazon.domain.Transmission;
import cn.explink.b2c.amazon.domain.header.ManifestHeader;
import cn.explink.b2c.amazon.domain.manifestDetail.ShipmentDetail;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CwbTempDAO;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.CwbOrder;
import cn.explink.support.transcwb.TransCwbDao;
import cn.explink.util.DateTimeUtil;

/**
 * 亚马逊 获取订单数据的接口
 * 
 * @author Administrator
 *
 */
@Service
public class AmazonService_GetOrders extends AmazonService {

	@Autowired
	CwbTempDAO cwbTempDAO;
	@Autowired
	TransCwbDao transCwbDao;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	@Autowired
	AmazonDAO amazonDAO;
	@Autowired
	CustomWareHouseDAO customWarehouseDAO;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	ObjectMapper objectMapper = new ObjectMapper();

	// 定时器获取订单
	public long getOrders() {
		long calcCount = 0;
		Amazon amazon = getAmazon(B2cEnum.Amazon.getKey());
		int isOpenFlag = jointService.getStateForJoint(B2cEnum.Amazon.getKey());
		if (isOpenFlag == 0) {
			logger.warn("未开启[亚马逊]获取订单接口");
			return -1;
		}

		try {
			logger.info("文件路径：{}", amazon.getFull_url());
			File file = new File(amazon.getFull_url());
			logger.info("文件路径是否存在：{}", file.exists());
			if (!file.exists()) {
				return 0;
			}
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {

				if (!files[i].isDirectory()) {
					long count = amazonDAO.checkFile(files[i].getName().toString());
					if (count > 0) {// 文件已获取
						continue;
					} else {
						logger.info(files[i].getName().toString());
						try {
							amazonDAO.saveFile(files[i].getName().toString(), 0, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							logger.error("亚马逊对接，执行插入异常", e1);
						}
						String xmlstr = readFileByLines(amazon.getFull_url(), files[i].getName().toString());
						Transmission tr = AmazonUnmarchal.Unmarchal(xmlstr);
						List<Map<String, String>> list = buildOrderMap(amazon, tr);
						if (list == null) {
							logger.warn("[亚马逊]获取map为空");
							continue;
						}
						long warehouseid = amazon.getWarehouseid();

						String emaildate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

						try {
							emaildate = tr.getMessage().getAmazonManifest().getManifestHeader().getManifestCreateDateTime().replaceAll("\n", "").replaceAll("T", " ");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							emaildate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
						}
						long cWareHouseId = 0;
						try {
							String wareHouseNo = tr.getMessage().getAmazonManifest().getManifestHeader().getWarehouseLocationID();
							if (!customWarehouseDAO.isExistsWarehouFlag(wareHouseNo, amazon.getCustomerid())) {
								cWareHouseId = customWarehouseDAO.creCustomerGetId(Long.valueOf(amazon.getCustomerid()), wareHouseNo, wareHouseNo);
							} else {
								CustomWareHouse cWareHouse = customWarehouseDAO.getCustomWareHouseByHousename(wareHouseNo, Long.valueOf(amazon.getCustomerid()));
								if (cWareHouse != null) {
									cWareHouseId = cWareHouse.getWarehouseid();
								}
							}

						} catch (Exception e) {
							// TODO Auto-generated catch block
							emaildate = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
							logger.error("亚马逊对接，发货时间转换异常", e);
						}

						try {
							List<CwbOrderDTO> extractss = dataImportService_B2c.Analizy_DataDealByB2cByEmaildate(Long.parseLong(amazon.getCustomerid()), B2cEnum.Amazon.getMethod(), list, warehouseid,
									true, emaildate, cWareHouseId);
							logger.info("处理[亚马逊]的订单信息=" + extractss.toString());
							logger.info("[亚马逊]订单数据插入表成功");
							amazonDAO.updateFile(files[i].getName().toString(), 1);
							calcCount += list.size();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							logger.error("亚马逊对接，执行插入异常", e);
							return 0;
						}
					}
				}

			}

			return calcCount;
		} catch (UnsupportedEncodingException e) {
			logger.error("", e);
		} catch (JAXBException e) {
			logger.error("", e);
		} catch (Exception e) {
			logger.error("", e);
		}
		return 0;

	}

	public void getOrdersByFileName(String filename) {
		Amazon amazon = getAmazon(B2cEnum.Amazon.getKey());
		int isOpenFlag = jointService.getStateForJoint(B2cEnum.Amazon.getKey());
		if (isOpenFlag == 0) {
			logger.warn("未开启[亚马逊]获取订单接口");
			return;
		}

		try {
			String xmlstr = readFileByLines(amazon.getFull_url(), filename);
			Transmission tr = AmazonUnmarchal.Unmarchal(xmlstr);
			List<Map<String, String>> list = buildOrderMap(amazon, tr);
			if (list == null) {
				logger.warn("[亚马逊]获取map为空");
				return;
			}
			long warehouseid = amazon.getWarehouseid();
			List<CwbOrderDTO> extractss = dataImportService_B2c.Analizy_DataDealByB2c(Long.parseLong(amazon.getCustomerid()), B2cEnum.Amazon.getMethod(), list, warehouseid, true);
			logger.info("处理[亚马逊]的订单信息=" + extractss.toString());
			logger.info("[亚马逊]订单数据插入表成功");
			amazonDAO.saveFile(filename, 1, new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()));
		} catch (UnsupportedEncodingException e) {
			logger.error("", e);
		} catch (JAXBException e) {
			logger.error("", e);
		} catch (Exception e) {
			logger.error("", e);
		}

	}

	// 定时器获取订单
	public void getOrdersByxml(String xml) {
		Amazon amazon = getAmazon(B2cEnum.Amazon.getKey());
		int isOpenFlag = jointService.getStateForJoint(B2cEnum.Amazon.getKey());
		if (isOpenFlag == 0) {
			logger.warn("未开启[亚马逊]获取订单接口");
			return;
		}

		try {
			Transmission tr = AmazonUnmarchal.Unmarchal(xml);
			List<Map<String, String>> list = buildOrderMap(amazon, tr);
			if (list == null) {
				logger.warn("[亚马逊]获取map为空");
				return;
			}
			long warehouseid = amazon.getWarehouseid();
			List<CwbOrderDTO> extractss = dataImportService_B2c.Analizy_DataDealByB2c(Long.parseLong(amazon.getCustomerid()), B2cEnum.Amazon.getMethod(), list, warehouseid, true);
			logger.info("处理[亚马逊]的订单信息=" + extractss.toString());
			logger.info("[亚马逊]订单数据插入表成功");
		} catch (UnsupportedEncodingException e) {
			logger.error("", e);
		} catch (JAXBException e) {
			logger.error("", e);
		} catch (Exception e) {
			logger.error("", e);
		}

	}

	/**
	 * 构建订单对应map
	 * 
	 * @param amazon
	 * @param transmission
	 * @return
	 * @throws Exception
	 */
	private List<Map<String, String>> buildOrderMap(Amazon amazon, Transmission transmission) throws Exception {
		String nowtime = DateTimeUtil.getNowTime();
		List<Map<String, String>> paraList = new ArrayList<Map<String, String>>();
		List<ShipmentDetail> orderList = transmission.getMessage().getAmazonManifest().getManifestDetail().getShipmentDetail();
		ManifestHeader manifestHeader = transmission.getMessage().getAmazonManifest().getManifestHeader();
		for (ShipmentDetail shipmentDetail : orderList) {
			try {
				String cwb = "";
				String cwbordertypeid = "";
				String remark = "";
				String remark1 = "";
				String transcwb = "";
				String remark2 = "";
				String remark3 = "";
				String contactName = "";
				String zipcode = "";
				String province = "";
				String city = "";
				String area = "";
				String weight = "";
				String address = "";
				String mobile = "";
				String phone = "";
				String waybillSourseRemark = "";
				String paywayid = "";
				String callBefore = "";
				String receivablefee = "";
				String paybackfee = "";
				String wareHouseNo = "";
				// 亚马逊的发货仓库，需提前在系统中设置
				String orderAmount = "1";
				String remark4 = "";
				String sellPrice = "";
				String carsize = "";
				// OrderAmount=goodsMap.get("OrderAmount")!=null?goodsMap.get("OrderAmount").toString():"1";
				// //商品数量
				String sendcargoname = "";
				// 加字段：
				String shipcwb = "";
				// 是否是自提
				String packageShipOption = "";
				try {
					cwb = shipmentDetail.getShipmentPackageInfo().getCartonID().getTrackingID();

					cwbordertypeid = "1";
					callBefore = "0";
					receivablefee = shipmentDetail.getShipmentCostInfo().getCashOnDeliveryCharge().getMonetaryAmount();
					paybackfee = "0";
					remark4 = "商品数量：" + shipmentDetail.getShipmentPackageInfo().getShipmentPackageItemQuantity().getQuantity();
					sellPrice = shipmentDetail.getShipmentCostInfo().getValueOfGoods().getMonetaryAmount();
					remark = "来源亚马逊:" + (shipmentDetail.getPreferredDeliveryTime() == null ? "" : shipmentDetail.getPreferredDeliveryTime());
					remark1 = "";
					mobile = shipmentDetail.getConsigneeAddress().getContactPhone();
					if (shipmentDetail.getShipmentPackageInfo().getShipmentPackageItemDetail() != null && shipmentDetail.getShipmentPackageInfo().getShipmentPackageItemDetail().get(0) != null
							&& shipmentDetail.getShipmentPackageInfo().getShipmentPackageItemDetail().get(0).getReplacementType() != null
							&& !shipmentDetail.getShipmentPackageInfo().getShipmentPackageItemDetail().get(0).getReplacementType().equals("")) {
						// cwbordertypeid = "3"; //订单类型
						remark1 = getCwbOrderTypeRemark(shipmentDetail.getShipmentPackageInfo().getShipmentPackageItemDetail().get(0).getReplacementType().replaceAll("\n", ""));
					}
					transcwb = shipmentDetail.getCustomerOrderNumber();
					remark2 = shipmentDetail.getShipmentPackageInfo().getScheduledDeliveryDate() == null ? "" : "预计送达：" + shipmentDetail.getShipmentPackageInfo().getScheduledDeliveryDate();
					remark3 = shipmentDetail.getPreferredDeliveryTime() == null ? "" : shipmentDetail.getPreferredDeliveryTime();
					contactName = shipmentDetail.getConsigneeAddress().getName();
					province = shipmentDetail.getConsigneeAddress().getStateChoice().getStateProvince();
					city = shipmentDetail.getConsigneeAddress().getCity();
					area = shipmentDetail.getConsigneeAddress().getDistrict();
					weight = shipmentDetail.getShipmentPackageInfo().getShipmentPackageActualGrossWeight().getWeightValue();
					address = shipmentDetail.getConsigneeAddress().getAddressLine1()
							+ (shipmentDetail.getConsigneeAddress().getAddressLine2() == null ? "" : shipmentDetail.getConsigneeAddress().getAddressLine2())
							+ (shipmentDetail.getConsigneeAddress().getAddressLine3() == null ? "" : shipmentDetail.getConsigneeAddress().getAddressLine3());

					address = province + city + area + address;// 收件人详细地址 拼接省市区
					waybillSourseRemark = manifestHeader.getShipFromAddress().getAddressLine1();
					paywayid = getPaywayid(shipmentDetail.getShipmentCostInfo().getPaymentMethod().replaceAll("\n", ""));

					String productName = shipmentDetail.getShipmentPackageInfo().getShipmentPackageItemDetail().get(0).getItemTitle(); // 商品名称
					sendcargoname = "发出商品-" + productName;
					shipcwb = shipmentDetail.getShipmentPackageInfo().getCartonID().getEncryptedShipmentID();
					zipcode = shipmentDetail.getConsigneeAddress().getZip();
					carsize = shipmentDetail.getShipmentPackageInfo().getShipmentPackageDimensions().getLengthValue() + "*"
							+ shipmentDetail.getShipmentPackageInfo().getShipmentPackageDimensions().getWidthValue() + "*"
							+ shipmentDetail.getShipmentPackageInfo().getShipmentPackageDimensions().getHeightValue();
					packageShipOption = shipmentDetail.getShipmentPackageInfo().getPackageShipmentMethod().getPackageShipOption();
				} catch (Exception e1) {
					logger.error("", e1);
				}
				String backcargoname = "";
				int backcarnum = 0;

				String emaildate = "";
				long wareHouseId = 0;
				try {
					String eamildatestr = manifestHeader.getManifestCreateDateTime().replaceAll("\n", "").replaceAll("T", " ");
					emaildate = eamildatestr;// 发货时间
					wareHouseNo = manifestHeader.getWarehouseLocationID();
					if (!customWarehouseDAO.isExistsWarehouFlag(wareHouseNo, amazon.getCustomerid())) {
						wareHouseId = customWarehouseDAO.creCustomerGetId(Long.valueOf(amazon.getCustomerid()), wareHouseNo, wareHouseNo);
					} else {
						CustomWareHouse cWareHouse = customWarehouseDAO.getCustomWareHouseByHousename(wareHouseNo, Long.valueOf(amazon.getCustomerid()));
						if (cWareHouse != null) {
							wareHouseId = cWareHouse.getWarehouseid();
						}
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					emaildate = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
					logger.error("亚马逊对接，发货时间转换异常", e);
				}

				String remark5 = "";

				try {
					remark5 = JSONObject.fromObject(manifestHeader.getShipFromAddress()).toString();
				} catch (Exception e) {
					logger.error("", e);
				}
				CwbOrder cwbOrder = cwbDAO.getCwbByCwb(cwb);
				if (cwbOrder != null) {
					logger.warn("获取[亚马逊]订单中含有重复数据cwb={}", cwb);
					continue;
				}
				Map<String, String> dataMap = new HashMap<String, String>();
				try {
					dataMap.put("cwb", cwb.replaceAll("\n", ""));
					dataMap.put("emaildate", emaildate);
					dataMap.put("nowtime", nowtime == null ? "" : nowtime.replaceAll("\n", ""));
					dataMap.put("consigneename", contactName == null ? "" : contactName.replaceAll("\n", ""));
					dataMap.put("consigneepostcode", zipcode == null ? "" : zipcode.replaceAll("\n", ""));
					dataMap.put("consigneeaddress", address == null ? "" : address.replaceAll("\n", ""));
					dataMap.put("consignoraddress", waybillSourseRemark == null ? "" : waybillSourseRemark.replaceAll("\n", "")); // 发件地址
					dataMap.put("cwbprovince", province == null ? "" : province.replaceAll("\n", ""));
					dataMap.put("cwbcity", city == null ? "" : city.replaceAll("\n", ""));
					dataMap.put("cwbcounty", area == null ? "" : area.replaceAll("\n", ""));
					dataMap.put("consigneemobile", mobile == null ? "" : mobile.replaceAll("\n", ""));
					dataMap.put("consigneephone", phone == null ? "" : phone.replaceAll("\n", ""));
					dataMap.put("cargorealweight", weight == null ? "" : weight.replaceAll("\n", ""));
					dataMap.put("customercommand", callBefore == null ? "" : (callBefore.equals("Y") ? "送货前打电话," + remark3 : remark3));
					dataMap.put("caramount", sellPrice == null ? "" : sellPrice.replaceAll("\n", "")); // 货物金额
					dataMap.put("paywayid", paywayid); // 支付方式
					dataMap.put("receivablefee", receivablefee == null ? "" : receivablefee.replaceAll("\n", "")); // 代收款
					dataMap.put("paybackfee", paybackfee == null ? "" : paybackfee.replaceAll("\n", "")); // 应退金额
					dataMap.put("sendcarname", sendcargoname == null ? "" : nowtime.replaceAll("\n", "")); // 发货商品
					dataMap.put("sendcarnum", orderAmount == null ? "" : orderAmount.replaceAll("\n", "")); // 发货数量
					dataMap.put("backcargoname", backcargoname == null ? "" : backcargoname.replaceAll("\n", "")); // 取回商品
					dataMap.put("customerwarehouseid", wareHouseId + ""); // 发货仓库
					dataMap.put("cwbordertypeid", cwbordertypeid); // 订单类型
					dataMap.put("cwbdelivertypeid", "1"); // 配送方式
					dataMap.put("startbranchid", "0"); // 当前库房id
					dataMap.put("customerid", amazon.getCustomerid()); // 供货商Id
					dataMap.put("cwbremark", remark); // 备注
					// ---加导入字段
					dataMap.put("backcarnum", backcarnum + ""); // 取货数量
					dataMap.put("cargosize", carsize == null ? "" : carsize.replaceAll("\n", ""));// 尺寸

					dataMap.put("shipcwb", shipcwb);// 加密单号
					dataMap.put("remark5", remark5);// 发货人信息

					dataMap.put("remark1", remark1);// 换货标识
					dataMap.put("remark2", remark2);// 备用
					dataMap.put("remark3", remark3);// 备用
					dataMap.put("remark4", remark4);// 备用
					dataMap.put("transcwb", transcwb);// 发货人信息

					if (packageShipOption.indexOf("pickup-cn") > -1) {// 自提的货
						dataMap.put("cargotype", "亚马逊自提"); // 商品描述
					} else {
						dataMap.put("cargotype", ""); // 商品描述
					}

				} catch (Exception e) {

					logger.info("亚马逊EDI封装map异常订单号：{}", cwb);
					logger.error("亚马逊EDI封装map异常", e);
					continue;
				}
				paraList.add(dataMap);
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		return paraList;
	}

	// 获取订单类型
	public String getCwbOrderType(String cwbOrdertype) {
		if (cwbOrdertype.indexOf("Replacement") > -1 || cwbOrdertype.indexOf("replacement") > -1) {
			return "3";
		} else {
			return "1";
		}
	}

	public String getCwbOrderTypeRemark(String cwbOrdertype) {
		if (cwbOrdertype.indexOf("Replacement") > -1 || cwbOrdertype.indexOf("replacement") > -1) {
			return "换相同件";
		} else {
			return "换不同件";
		}
	}

	public String getPaywayid(String paymentType) {
		if (paymentType.equalsIgnoreCase("cash") || paymentType.equalsIgnoreCase("Non-cod")) { // cash，现金
			return "1";
		} else {
			return "2";
		}

	}

	private String readFileByLines(String url, String fileName) {
		File file = new File(url + fileName);
		BufferedReader reader = null;
		StringBuffer xmlStr = new StringBuffer();
		try {
			logger.info("以行为单位读取文件内容，一次读一整行：");
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				xmlStr.append(tempString);
			}
			reader.close();
			logger.info("以行为单位读取文件内容，一次读一整行：读取文件成功");
			return xmlStr.toString();
		} catch (IOException e) {
			logger.error("以行为单位读取文件内容，一次读一整行：读取文件失败", e);
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

}
