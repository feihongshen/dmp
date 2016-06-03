package cn.explink.b2c.gome;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.gome.domain.AsnVO;
import cn.explink.b2c.gome.domain.Detail;
import cn.explink.b2c.gome.domain.GomeASNUnmarchal;
import cn.explink.b2c.gome.domain.GomeUnmarchal;
import cn.explink.b2c.gome.domain.ItemLine;
import cn.explink.b2c.gome.domain.OrderVO;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.CwbTempDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.CwbTemp;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.support.transcwb.TransCwbDao;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.WebServiceHandler;
import cn.explink.util.MD5.DES3Utils;

/**
 * 国美 获取订单数据的接口
 * 
 * @author Administrator
 *
 */
@Service
public class GomeService_GetOrders extends GomeService {

	@Autowired
	CwbTempDAO cwbTempDAO;
	@Autowired
	TransCwbDao transCwbDao;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	
	private static Logger logger = LoggerFactory.getLogger(GomeService_GetOrders.class);

	ObjectMapper objectMapper = JacksonMapper.getInstance();

	/***
	 * 获取正向订单号的接口
	 * ================================================================
	 * ==================
	 * 
	 * @throws Exception
	 */

	public long getSoAndAsnCwbs() {
		long calcCount = 0;

		Gome gome = getGome(B2cEnum.Gome.getKey());
		int isOpenFlag = jointService.getStateForJoint(B2cEnum.Gome.getKey());
		if (isOpenFlag == 0) {
			logger.warn("未开启[国美]获取订单接口");
			return -1;
		}
		// 获取正向订单
		try {
			String method = "getTransactionIdByAction";
			String pram = "SO";
			logger.info(gome.getFull_url() + "===" + gome.getUsername() + "====" + gome.getPassword());
			Object o = WebServiceHandler.invokeWsByNameAndPassWord(gome.getFull_url(), method, pram, gome.getUsername(), gome.getPassword());
			logger.info("请求国美result :{} ", o);
			if (o == null || "".equals(o)) {
				logger.info("请求国美result :{} ,没有SO订单数据", o);
				return 0;
			} else {
				List<String> returnValue = new ArrayList<String>();
				try {
					returnValue = (ArrayList<String>) o;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					returnValue.add(o.toString());
				}
				logger.info("请求国美result :{} ", returnValue);
				for (String cwb : returnValue) {
					long count = cwbTempDAO.checkCwb(cwb);
					if (count > 0) {
						logger.info("[国美]数据插入订单号表 订单号：{} 已重复", cwb);
						continue;
					}
					CwbTemp cwbtemp = new CwbTemp();
					cwbtemp.setCwb(cwb);
					cwbtemp.setCwbtype("SO");
					cwbtemp.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					cwbTempDAO.createCwbTemp(cwbtemp);
				}
				logger.info("[国美]数据插入订单号表成功returnValue:{}", returnValue.toString());
				calcCount = returnValue.size();
			}

		} catch (Exception e) {
			logger.error("获取[国美]订单出现未知异常", e);
		}

		// 获取订单运单号
		try {
			String method = "getTransactionIdByAction";
			String pram = "ASN";
			Object o = WebServiceHandler.invokeWsByNameAndPassWord(gome.getFull_url(), method, pram, gome.getUsername(), gome.getPassword());
			logger.info("请求国美result :{} ", o);
			if (o == null || "".equals(o)) {
				logger.info("请求国美result :{} ,没有ASN订单数据", o);
			} else {
				List<String> returnValue = new ArrayList<String>();
				try {
					returnValue = (ArrayList<String>) o;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					returnValue.add(o.toString());
				}
				logger.info("请求国美result :{} ", returnValue);
				for (String cwb : returnValue) {
					long count = cwbTempDAO.checkCwb(cwb);
					if (count > 0) {
						logger.info("[国美]数据插入订单号表 订单号：{} 已重复", cwb);
						continue;
					}
					CwbTemp cwbtemp = new CwbTemp();
					cwbtemp.setCwb(cwb);
					cwbtemp.setCwbtype("ASN");
					cwbtemp.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					cwbTempDAO.createCwbTemp(cwbtemp);
				}
				logger.info("[国美]数据插入订单号表成功returnValue:{}", returnValue.toString());
			}
		} catch (Exception e) {
			logger.error("获取[国美]订单出现未知异常", e);
		}
		return calcCount;
	}

	public void getOrders() {
		Gome gome = getGome(B2cEnum.Gome.getKey());
		int isOpenFlag = jointService.getStateForJoint(B2cEnum.Gome.getKey());
		if (isOpenFlag == 0) {
			logger.warn("未开启[国美]获取订单接口");
			return;
		}
		// 获取订单
		try {
			List<CwbTemp> cwpTempList = cwbTempDAO.getCwbTemp(0 + "," + 2, "SO");// 获取正向订单
			if (cwpTempList != null && cwpTempList.size() > 0) {
				for (CwbTemp cwbTemp : cwpTempList) {
					if (getOrder(gome, cwbTemp, "SO")) {// 处理无异常，获取订单正确，修改为已获取正确

						String method = "setTransactionStatusByXmlStr";
						String pram = returnOrderToXMl(cwbTemp, "Y", "获取订单成功");
						logger.info("返回给国美获取订单成功标识xml：" + pram);
						Object o = WebServiceHandler.invokeWsByNameAndPassWord(gome.getTnt_url(), method, pram, gome.getUsername(), gome.getPassword());
						// 通知国美也处理完成
						if (o == null || o.toString().equals("")) {
							cwbTempDAO.updateCwbtemp(cwbTemp.getCwb(), 1);// 修改为已获取正确
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("获取[国美]订单出现未知异常", e);
		}
		// 获取订单
		try {
			logger.info("获取[国美]ASN订单");
			List<CwbTemp> cwpTempList = cwbTempDAO.getCwbTemp(0 + "," + 2, "ASN");// 获取订单运单号
			if (cwpTempList != null && cwpTempList.size() > 0) {
				for (CwbTemp cwbTemp : cwpTempList) {
					if (getOrder(gome, cwbTemp, "ASN")) {// 处理无异常，获取订单正确，修改为已获取正确

						String method = "setTransactionStatusByXmlStr";
						String pram = returnOrderToXMl(cwbTemp, "Y", "获取订单成功");
						logger.info("返回给国美获取订单成功标识xml：" + pram);
						Object o = WebServiceHandler.invokeWsByNameAndPassWord(gome.getTnt_url(), method, pram, gome.getUsername(), gome.getPassword());
						logger.info("国美返回获取订单结果：" + o);
						// 通知国美也处理完成
						if (o == null || o.toString().equals("")) {
							cwbTempDAO.updateCwbtemp(cwbTemp.getCwb(), 1);// 修改为已获取正确
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("获取[国美]订单出现未知异常", e);
		}

	}

	public void inserttemp(String returnValue) {
		Gome gome = getGome(B2cEnum.Gome.getKey());
		int isOpenFlag = jointService.getStateForJoint(B2cEnum.Gome.getKey());
		if (isOpenFlag == 0) {
			logger.warn("未开启[国美]获取订单接口");
			return;
		}
		insertOrder(gome, returnValue, "SO", "6110370000");
	}

	public void getorder(String cwb) {
		Gome gome = getGome(B2cEnum.Gome.getKey());
		int isOpenFlag = jointService.getStateForJoint(B2cEnum.Gome.getKey());
		if (isOpenFlag == 0) {
			logger.warn("未开启[国美]获取订单接口");
			return;
		}
		String method = "getASNInformationByXmlStr";
		String pram = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<getTransactionStatusEvnt>" + "<conditions>" + "<transactionStatusVO>" + "<transaction_id>" + cwb + "</transaction_id>"
				+ "</transactionStatusVO>" + "</conditions>" + "</getTransactionStatusEvnt>";
		String remark4 = "";
		String remark5 = "";
		try {
			Object o = WebServiceHandler.invokeWsByNameAndPassWord(gome.getFull_url(), method, pram, gome.getUsername(), gome.getPassword());
			String returnValue = (String) o;
			returnValue = returnValue.replaceAll("<list>", "").replace("</list>", "");
			logger.info("[国美]获取订单详情returnValue:{}", returnValue.toString());
			OrderVO order = GomeUnmarchal.Unmarchal(returnValue);

			JSONObject json = new JSONObject();
			json.put("originalOrderNum", order.getOriginalOrderNum());
			json.put("orderDate", order.getOrderDate());
			json.put("exDate", order.getExDate());
			remark4 = json.toString();
			if (order.getDetails() != null && order.getDetails().getDetail() != null) {
				List<Detail> dList = order.getDetails().getDetail();
				if (dList != null && dList.size() > 0) {
					try {
						JSONArray remark = new JSONArray();
						remark = JSONArray.fromObject(dList);
						remark5 = remark.toString();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logger.error("国美对接，退货订单信息转换异常", e);
					}
				}
			}
		} catch (Exception e1) {
			logger.error("国美对接，退货订单信息订单时间转换异常", e1);
		}
		logger.info("[国美]获取订单详情json:{}", remark4 + " " + remark5);

	}

	private boolean getOrder(Gome gome, CwbTemp cwbTemp, String ordertype) {
		try {
			String orderNoList = setOrderToXMl(cwbTemp);
			if (orderNoList.equals("")) {
				return false;
			}
			String method = "getSalesOrderByXmlStr";
			if ("ASN".equals(ordertype)) {
				method = "getASNInformationByXmlStr";
			}
			String pram = orderNoList.replaceAll("'", "");
			Object o = WebServiceHandler.invokeWsByNameAndPassWord(gome.getFull_url(), method, pram, gome.getUsername(), gome.getPassword());
			String returnValue = (String) o;
			returnValue = returnValue.replaceAll("<list>", "").replace("</list>", "");
			logger.info("[国美]获取订单详情returnValue:{}", returnValue.toString());
			if (insertOrder(gome, returnValue, ordertype, cwbTemp.getCwb())) {// 插入表成功，返回给国美标识
				logger.info("[国美]获取订单插入成功");
				return true;
			} else {// 标识获取订单失败，下次再重新获取
				cwbTempDAO.updateCwbtemp(cwbTemp.getCwb(), 2);
				return false;
			}
		} catch (Exception e) {
			logger.error("", e);
			return false;
		}
	}

	private String setOrderToXMl(CwbTemp cwbTemp) {
		String orderXml = "";
		if (cwbTemp.getCwb() != null && !"".equals(cwbTemp.getCwb())) {
			orderXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<getTransactionStatusEvnt>" + "<conditions>" + "<transactionStatusVO>" + "<transaction_id>" + cwbTemp.getCwb()
					+ "</transaction_id>" + "</transactionStatusVO>" + "</conditions>" + "</getTransactionStatusEvnt>";
		}
		return orderXml;
	}

	private String returnOrderToXMl(CwbTemp cwbTemp, String state, String remark) {
		String orderXml = "";
		if (cwbTemp.getCwb() != null && !"".equals(cwbTemp.getCwb())) {
			orderXml = "<?xml version=\"1.0\" encoding=\"GBK\"?>" + "<transactionStatus>" + "<conditions>" + "<transactionStatusVO>" + "<transaction_id>" + cwbTemp.getCwb() + "</transaction_id>"
					+ "<is_successful>" + state + "</is_successful>" + "<remark>" + remark + "</remark>" + "</transactionStatusVO>" + "</conditions>" + "</transactionStatus>";
		}
		return orderXml;
	}

	@Transactional
	private boolean insertOrder(Gome gome, String returnValue, String ordertype, String orderID) {
		try {
			if ("SO".equals(ordertype)) {
				logger.info("处理[国美]so后的xml=" + returnValue);
				OrderVO orderVO = GomeUnmarchal.Unmarchal(returnValue);
				logger.info("处理[国美]so后的订单信息=" + orderVO);
				// 保存对应so订单号
				cwbTempDAO.updateSoCwbByCwb(orderVO.getOrderNumber(), orderID);
				List<Map<String, String>> list = buildOrderMap(gome, orderVO);
				long warehouseid = getGome(B2cEnum.Gome.getKey()).getWarehouseid();
				List<CwbOrderDTO> extractss = dataImportService_B2c.Analizy_DataDealByB2c(Long.parseLong(gome.getCustomerid()), B2cEnum.Gome.getMethod(), list, warehouseid, true);
				logger.info("处理[国美]so后的订单信息=" + extractss.toString());
				logger.info("[国美]so数据插入表成功");
				return true;
			} else {
				int check = 0;
				logger.info("处理[国美]asn后的xml=" + returnValue);
				AsnVO asnVO = GomeASNUnmarchal.Unmarchal(returnValue);
				logger.info("处理[国美]asn后的订单信息=" + asnVO);
				// 保存对应so订单号
				cwbTempDAO.updateAsnCwbByCwb(asnVO.getAsnNumber(), orderID);
				List<ItemLine> iList = asnVO.getItems().getItem().getItemLines().getItemLine();
				if (iList != null && iList.size() > 0) {
					for (ItemLine itemLine : iList) {
						if (cwbTempDAO.checkSOCwb(itemLine.getOrderNumber()) > 0) {// 如果没获取到so订单
																					// ，就不用操作运单的存储
							if (itemLine.getBoxId().toString().length() < 2) {
								String asnCwb = itemLine.getOrderNumber() + "0" + itemLine.getBoxId();// 获取到运单号
								String soCwb = itemLine.getOrderNumber();
								if (itemLine.getBoxId().equals("1")) {
									try {
										dataImportDAO_B2c.updateCarrealweightAndTranscwb(soCwb);
									} catch (Exception e) {
										logger.info("处理国美asn后的订单信息,初始化件数和运单号异常" + soCwb);
										logger.error("", e);
									}
								}
								float weight = Float.parseFloat(itemLine.getGrossWeight());
								logger.info("国美订单号对应重量：{}:{}", soCwb, weight);
								logger.info("国美运单号对应重量：{}:{}", asnCwb, weight);
								// 更新订单对应的重量和件数和运单号
								dataImportDAO_B2c.updateCarrealweightAndTranscwb(soCwb, weight, asnCwb);
								// 保存asnCwb到对应的soCwb
								cwbTempDAO.updateAsnCwbBySoCwb(soCwb, asnCwb);
								String transCwb = transCwbDao.getCwbByTransCwb(asnCwb);
								if (transCwb == null) {
									transCwbDao.saveTranscwb(asnCwb, soCwb);
								}
							} else {
								String asnCwb = itemLine.getOrderNumber() + "" + itemLine.getBoxId();// 获取到运单号
								String soCwb = itemLine.getOrderNumber();
								float weight = Float.parseFloat(itemLine.getGrossWeight());
								logger.info("国美订单号对应重量：{}:{}", soCwb, weight);
								logger.info("国美运单号对应重量：{}:{}", asnCwb, weight);
								// 更新订单对应的重量和件数和运单号
								dataImportDAO_B2c.updateCarrealweightAndTranscwb(soCwb, weight, asnCwb);
								// 保存asnCwb到对应的soCwb
								cwbTempDAO.updateAsnCwbBySoCwb(soCwb, asnCwb);
								String transCwb = transCwbDao.getCwbByTransCwb(asnCwb);
								if (transCwb == null) {
									transCwbDao.saveTranscwb(asnCwb, soCwb);
								}
							}
						} else {
							logger.info("处理国美asn后的订单信息,so订单没有保存，不能保存修改获取已成功，订单号：{},san号 :{}", itemLine.getOrderNumber(), asnVO.getAsnNumber());
							check++;
							continue;
						}

					}
				}
				logger.info("[国美]数据插入表成功");
				if (check > 0) {
					return false;
				}
				return true;
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("", e);
			return false;
		} catch (JAXBException e) {
			logger.error("", e);
			return false;
		} catch (Exception e) {
			logger.error("", e);
			return false;
		}
	}

	private List<Map<String, String>> buildOrderMap(Gome gome, OrderVO order) throws Exception {
		String nowtime = DateTimeUtil.getNowTime();
		List<Map<String, String>> paraList = new ArrayList<Map<String, String>>();
		String cwb = order.getOrderNumber(); // 订单号
		String cwbordertypeid = getCwbOrderType(order.getOrderType()); // 订单类型
		String cargotype = getCartype(order.getOrderType()); // 货物类型：配送的，退货的，换新取旧
		String contactName = order.getShippingAddress().getContactName(); // 收件人
		String zipcode = order.getShippingAddress().getZipcode(); // 收件人邮编
		String province = order.getShippingAddress().getProvince(); // 收件人所在省
		String city = order.getShippingAddress().getCityName(); // 收件人 收件市
		String area = order.getShippingAddress().getDivisionName(); // 收件人 区
		String weight = ""; // 重量
		String address = order.getShippingAddress().getLine1() + order.getShippingAddress().getLine2() + order.getShippingAddress().getLine3() + order.getShippingAddress().getLine4();
		String mobile = order.getShippingAddress().getMobileNumber1(); // 手机
		String phone = order.getShippingAddress().getTelephoneNumber1() + "/" + order.getShippingAddress().getTelephoneNumber2(); // 座机

		address = province + city + area + address;// 收件人详细地址 拼接省市区
		String waybillSourseRemark = "运单来源：国美   " + order.getOrderType() + " 订单";
		String paywayid = getPaywayid(order.getPaymentType()); // 支付方式
		String callBefore = order.getTelephoneFlag(); // 是否送前提示
		String receivablefee = order.getTotalDualAmount(); // 应收金额
		String paybackfee = "0"; // 应退金额
		String wareHouseNo = order.getFromMasLoc(); // 仓库名称
		// 国美的发货仓库，需提前在系统中设置
		String warhouseid = dataImportService_B2c.getCustomerWarehouseNo(wareHouseNo, gome.getCustomerid());
		// 商品信息
		List orderGoods = new ArrayList();
		String productName = "";
		// String orderAmount="1";
		String sellPrice = order.getSellingPrice();
		if (orderGoods != null && orderGoods.size() > 0) {
			Map goodsMap = (Map) orderGoods.get(0);
			productName = goodsMap.get("ProductName") != null ? goodsMap.get("ProductName").toString() : ""; // 商品名称
			// OrderAmount=goodsMap.get("OrderAmount")!=null?goodsMap.get("OrderAmount").toString():"1";
			// //商品数量
		}
		String sendcargoname = "发出商品-" + productName; // 商品信息 发出，取回
		String backcargoname = "";
		int backcarnum = 0;
		String remark3 = order.getComments();
		String remark4 = "";
		try {
			if (!order.getOrderType().equals("SO")) {
				JSONObject json = new JSONObject();
				json.put("originalOrderNum", order.getOriginalOrderNum());
				json.put("orderDate", order.getOrderDate());
				json.put("exDate", order.getExDate());
				remark4 = json.toString();
			}
		} catch (Exception e1) {
			logger.error("国美对接，退货订单信息订单时间转换异常", e1);
		}
		String remark5 = "";// 退货订单Detail
		if (order.getDetails() != null && order.getDetails().getDetail() != null) {
			List<Detail> dList = order.getDetails().getDetail();
			if (dList != null && dList.size() > 0) {
				for (Detail detail : dList) {
					backcargoname += detail.getPartDesc() + " ";
					backcarnum += Integer.parseInt("".equals(detail.getQty()) ? "0" : detail.getQty());
				}
				try {
					JSONArray remark = new JSONArray();
					remark = JSONArray.fromObject(dList);
					remark5 = remark.toString();
					if (remark5.length() > 1500) {
						logger.info("国美对接，退货订单号：{}，货订单信息{},长度超过1500字节", cwb, remark5);
						remark5 = "";
					}
				} catch (Exception e) {
					logger.error("国美对接，退货订单信息转换异常", e);
				}
			}
		}
		String emaildate = "";
		try {
			emaildate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new SimpleDateFormat("yyyy-MM-dd").parse(order.getExDate()));// 发货时间
		} catch (Exception e) {
			// TODO Auto-generated catch block
			emaildate = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
			logger.error("国美对接，发货时间转换异常", e);
		}
		// 加字段：

		CwbOrder cwbOrder = cwbDAO.getCwbByCwb(cwb);
		if (cwbOrder != null) {
			logger.warn("获取[国美]订单中含有重复数据cwb={}", cwb);
			return null;
		}
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("cwb", cwb);
		dataMap.put("emaildate", emaildate);
		dataMap.put("nowtime", nowtime);
		dataMap.put("consigneename", contactName);
		dataMap.put("consigneepostcode", zipcode);
		dataMap.put("consigneeaddress", address);
		dataMap.put("consignoraddress", waybillSourseRemark); // 发件地址
		dataMap.put("cwbprovince", province);
		dataMap.put("cwbcity", city);
		dataMap.put("cwbcounty", area);
		dataMap.put("consigneemobile", mobile);
		dataMap.put("consigneephone", phone);
		dataMap.put("cargorealweight", weight);
		dataMap.put("customercommand", callBefore.equals("Y") ? "送货前打电话" : "");
		dataMap.put("caramount", sellPrice + ""); // 货物金额
		dataMap.put("paywayid", paywayid); // 支付方式
		dataMap.put("receivablefee", receivablefee); // 代收款
		dataMap.put("paybackfee", paybackfee); // 应退金额
		dataMap.put("sendcarname", sendcargoname); // 发货商品
		dataMap.put("sendcarnum", "1".equals(cwbordertypeid) ? "1" : "0"); // 发货数量
		dataMap.put("backcargoname", backcargoname); // 取回商品
		dataMap.put("customerwarehouseid", warhouseid); // 发货仓库
		dataMap.put("cwbordertypeid", cwbordertypeid); // 订单类型
		dataMap.put("cwbdelivertypeid", "1"); // 配送方式
		dataMap.put("startbranchid", "0"); // 当前库房id
		dataMap.put("customerid", gome.getCustomerid()); // 供货商Id
		dataMap.put("cwbremark", waybillSourseRemark); // 备注
		// ---加导入字段
		dataMap.put("backcargonum", "1".equals(cwbordertypeid) ? "0" : backcarnum + ""); // 取货数量
		dataMap.put("remark4", remark4); // 退货订单信息
		dataMap.put("remark5", remark5); // 退货订单信息
		dataMap.put("remark3", remark3); // 商品描述
		dataMap.put("cargotype", cargotype); // 商品描述

		paraList.add(dataMap);
		return paraList;
	}

	// 获取订单类型
	public String getCwbOrderType(String cwbOrdertype) {
		if (cwbOrdertype.equals("SO") || cwbOrdertype.equals("PS") || cwbOrdertype.equals("SPS")) { // SO:正向订单；PS：换货送货单;SPS:换新取旧发货单
			return "1";
		}
		if (cwbOrdertype.equals("RO") || cwbOrdertype.equals("PR") || cwbOrdertype.equals("SPR")) {// RO:退货订单PR:换货退货单；SPR:换新取旧退货单
			return "2";
		}
		return "1";
	}

	public String getCartype(String cwbOrdertype) {
		if (cwbOrdertype.equals("SO")) { // SO:正向订单；PS：换货送货单;SPS:换新取旧发货单
			return "普通发货订单";
		}
		if (cwbOrdertype.equals("PS")) { // SO:正向订单；PS：换货送货单;SPS:换新取旧发货单
			return "换货送货单";
		}
		if (cwbOrdertype.equals("SPS")) { // SO:正向订单；PS：换货送货单;SPS:换新取旧发货单
			return "换新取旧发货单";
		}
		if (cwbOrdertype.equals("RO")) {// RO:退货订单PR:换货退货单；SPR:换新取旧退货单
			return "普通退货订单";
		}
		if (cwbOrdertype.equals("PR")) {// RO:退货订单PR:换货退货单；SPR:换新取旧退货单
			return "换货退货单";
		}
		if (cwbOrdertype.equals("SPR")) {// RO:退货订单PR:换货退货单；SPR:换新取旧退货单
			return "换新取旧退货单";
		}
		return "";
	}

	public String getPaywayid(String paymentType) {
		if (paymentType.equals("CASH")) { // cash，现金
			return "1";
		} else {
			return "2";
		}

	}

	public static void main(String[] args) throws Exception {
		String address = "PugG91vm04Mss310JInuOLtYbSd4ELoKWMw8wr/owgc=";
		// +PD4xvd8TwBinMMb6g6RtwenjbHxZgLrt9QB8Eh2SnHA5H90vvxN+Q==

		String key = "yBMiXHY34TwhuBv5gqllfZerg1qKXLSL" + "50" + "d7952104-dd28-44db-8fbd-94f78edcc0e6";

		// 解密后再加密

		String reAddress = "";
		try {
			reAddress = DES3Utils.decryptMode(address, key);
		} catch (NoSuchAlgorithmException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		}

		logger.info(reAddress);

	}

}
