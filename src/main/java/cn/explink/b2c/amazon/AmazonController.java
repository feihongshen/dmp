package cn.explink.b2c.amazon;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.BranchDAO;
import cn.explink.enumutil.BranchEnum;

@Controller
@RequestMapping("/amazon")
public class AmazonController {
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;
	@Autowired
	AmazonService amazonService;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	AmazonService_GetOrders amazonService_GetOrders;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("amazonObject", amazonService.getAmazon(key));
		model.addAttribute("warehouselist", branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		model.addAttribute("joint_num", key);
		return "b2cdj/amazon";
	}

	@RequestMapping("/saveamazon/{id}")
	public @ResponseBody String amazonSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {
		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {
			amazonService.edit(request, key);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		amazonService.update(key, state);
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	@RequestMapping("/testxml")
	public String testxml(Model model, @RequestParam(value = "xml", required = false, defaultValue = "") String xml) {
		if (!xml.equals("") && xml.indexOf(".xml") < 0) {
			xml = xml.replaceAll("\n", "").replaceAll("\t", "").replaceAll("\r\n", "");
			logger.info("[亚马逊]获取订单详情returnValue:{}", xml.toString());
			amazonService_GetOrders.getOrdersByxml(xml);
		} else if (xml.indexOf(".xml") > -1) {
			logger.info("[亚马逊]获取订单文件名:{}", xml.toString());
			amazonService_GetOrders.getOrdersByFileName(xml);
		}
		return "/b2cdj/amazon/testamazon";
	}

	@RequestMapping("/test")
	public @ResponseBody String test() {
		amazonService_GetOrders.getOrders();
		amazonService.selectTempAndInsertToCwbDetail();
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	@RequestMapping("/getorder")
	public String getorder(Model model, @RequestParam(value = "cwb", required = false, defaultValue = "") String xml) {
		xml = xml.replaceAll("<list>", "").replace("</list>", "");
		logger.info("[亚马逊]获取订单详情returnValue:{}", xml.toString());
		return "/b2cdj/amazon/getamazon";
	}

	@RequestMapping("/insert")
	public void insert() {
		amazonService.selectTempAndInsertToCwbDetail();

	}

	@RequestMapping("/reason")
	public void reason() {
		amazonService.Reason();

	}

	public static void main(String[] args) {
		String order = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<transmission><!-- 消息信息 -->" + "<message messageType=\"AMAZON_MFT\">" + "<!-- 亚马逊发货单 -->" + "<amazonManifest>"
				+ "<!-- 发货单头信息 -->" + "<manifestHeader>" + "<!-- 发货库房 -->" + "<warehouseLocationID>CAN1</warehouseLocationID>" + "<!-- 发货单创建时间 -->" + "<manifestCreateDateTime>"
				+ "2012-10-15T01:10:25" + "</manifestCreateDateTime>" + "<!-- 配送公司id -->" + "<carrierInternalID>CARRIERID</carrierInternalID>" + "<!-- 发货方法 -->" + "<shipmentMethod>"
				+ "<amazonTechnicalName>" + "CARRIERSCAC_SPK_COD" + "</amazonTechnicalName>" + "</shipmentMethod>" + "<!-- 发货单号 -->" + "<manifestNumber>183906060</manifestNumber>" + "<!-- 配送公司ID-->"
				+ "<carrierAccountID>CARRIERID</carrierAccountID>" + "<!-- 配送方发货单号 -->" + "<carrierManifestID />" + "<!-- 预计发货时间 -->" + "<shipmentDate>2012-10-15T01:10:25</shipmentDate>"
				+ "<!-- 货币代码 -->" + "<currencyCode>CNY</currencyCode>" + "<!-- 出货地信息-->" + "<shipFromAddress AddressType=\"SHIP_FROM\">" + "<name>JoyoAmazon</name>" + "<addressLine1>"
				+ "B1-B2, No.1 Pu Bei Rd, North Kai C" + "</addressLine1>" + "<city>Huangpu</city>" + "<stateChoice>" + "<stateProvince>Guangzhou</stateProvince>" + "</stateChoice>"
				+ "<zip>510530</zip>" + "<countryCode>CN</countryCode>" + "<countryName>China</countryName>" + "</shipFromAddress>" + "<!-- 出货人信息 -->" + "<shipperInformation>"
				+ "<amazonTaxID>AmazonTaxID</amazonTaxID>" + "</shipperInformation>" + "<!-- 亚马逊付款方式, 亚马逊如何支付配送公司 -->" + "<prepaidCollect>PREPAID</prepaidCollect>" + "</manifestHeader>"
				+ "<!-- 发货单明细 -->" + "<manifestDetail>" + "<!-- 发货条目明细(每个货品循环shipmentDetail) -->" + "<shipmentDetail>" + "<!-- 委托方订单号 -->" + "<customerOrderNumber>" + "FC1-3593040-4702630"
				+ "</customerOrderNumber>" + "<!-- 收货人地址 -->" + "<consigneeAddress AddressType=\"CONSIGNEE\">" + "<name>蒋光华</name>" + "<addressLine1>" + "天河区 广州天河工业园建工路11号528" + "</addressLine1>"
				+ "<addressLine2>艾云尼</addressLine2>" + "<district>天河区</district>" + "<city>广州市</city>" + "<stateChoice>" + "<stateProvince>广东</stateProvince>" + "</stateChoice>" + "<zip>510630</zip>"
				+ "<countryCode>CN</countryCode>" + "<countryName>China</countryName>" + "<contactPhone>13751897518</contactPhone>" + "<contactEmail>wuchonming@126.com</contactEmail>"
				+ "<!--地址类型, R表示居民-->" + "<amzShipAddressUsage>R</amzShipAddressUsage>" + "<!-- 自提点id -->" + "<storeId>458383</storeId>" + "</consigneeAddress>" + "<!-- 货品金额信息 -->"
				+ "<shipmentCostInfo>" + "<!-- 支付方式, Non-COD/非货到付款, Cash/货到付款 -->" + "<paymentMethod>Non-COD</paymentMethod>" + "<!-- 销售方式DDU/FCA, 参考specification -->"
				+ "<termsOfSale>DDU</termsOfSale>" + "<!-- 运费信息(仅供参考) -->" + "<amazonFreightCost>" + "<chargeOrAllowance>" + "CHARGE" + "</chargeOrAllowance>"
				+ "<monetaryAmount currencyISOCode=\"CNY\">" + "4.30" + "</monetaryAmount>" + "</amazonFreightCost>" + "<!-- 货品金额信息 -->" + "<valueOfGoods>" + "<chargeOrAllowance>" + "CHARGE"
				+ "</chargeOrAllowance>" + "<monetaryAmount currencyISOCode=\"CNY\">" + "152.99" + "</monetaryAmount>" + "</valueOfGoods>" + "<!-- 暂不使用 -->" + "<CIF>" + "<chargeOrAllowance>"
				+ "CHARGE" + "</chargeOrAllowance>" + "<monetaryAmount currencyISOCode=\"CNY\">" + "157.26" + "</monetaryAmount>" + "</CIF>" + "<!-- 收取收货人运费(仅供参考) -->" + "<consigneeFreightCharge>"
				+ "<chargeOrAllowance>" + "CHARGE" + "</chargeOrAllowance>" + "<monetaryAmount currencyISOCode=\"CNY\">" + "5.00" + "</monetaryAmount>" + "</consigneeFreightCharge>"
				+ "<!-- 应收金额,货到付款金额 -->" + "<CashOnDeliveryCharge>" + "<chargeOrAllowance>" + "CHARGE" + "</chargeOrAllowance>" + "<monetaryAmount currencyISOCode=\"CNY\">" + "0.00"
				+ "</monetaryAmount>" + "</CashOnDeliveryCharge>" + "<!-- 暂不使用 -->" + "<ConsumptionCharge>" + "<chargeOrAllowance>" + "CHARGE" + "</chargeOrAllowance>"
				+ "<monetaryAmount currencyISOCode=\"CNY\">" + "0.00" + "</monetaryAmount>" + "</ConsumptionCharge>" + "</shipmentCostInfo>" + "<!-- 包裹信息 -->" + "<shipmentPackageInfo>"
				+ "<!-- 包裹盒信息 -->" + "<cartonID>" + "<!-- 加密发货单号-->" + "<encryptedShipmentID>" + "DWn2WvlvR" + "</encryptedShipmentID>" + "<packageID>1</packageID>" + "<!-- 条码号 -->"
				+ "<trackingID>10311813527384</trackingID>" + "</cartonID>" + "<!-- 包裹发货方法 -->" + "<packageShipmentMethod>" + "<amazonTechnicalName>" + "CARRIERSCAC_SPK_COD"
				+ "</amazonTechnicalName>" + "</packageShipmentMethod>" + "<!-- zone代码 -->" + "<shipZone>z01_CAN1</shipZone>" + "<!-- sort代码 -->" + "<shipSort>0301</shipSort>" + "<!-- 参考store id -->"
				+ "<pointID>458383</pointID>" + "<!-- 商业发票创建时间 -->" + "<commercialInvoiceDate>" + "2012-10-14" + "</commercialInvoiceDate>" + "<!-- 预计送达时间  -->" + "<scheduledDeliveryDate>"
				+ "2012-10-15" + "</scheduledDeliveryDate>" + "<!-- 货品声报重量信息  -->" + "<shipmentPackageDeclaredGrossWeight>" + "<weightValue unitOfMeasure=\"KG\">" + "999.9988" + "</weightValue>"
				+ "</shipmentPackageDeclaredGrossWeight>" + "<shipmentPackageDimWtCalcMethod>" + "WEIGHT" + "</shipmentPackageDimWtCalcMethod>" + "<!-- 货品实际重量信息  -->"
				+ "<shipmentPackageActualGrossWeight>" + "<weightValue unitOfMeasure=\"KG\">" + "4.56" + "</weightValue>" + "</shipmentPackageActualGrossWeight>" + "<!-- 包裹尺寸 -->"
				+ "<shipmentPackageDimensions>" + "<lengthValue unitOfMeasure=\"CM\">" + "49.4" + "</lengthValue>" + "<heightValue unitOfMeasure=\"CM\">" + "25.8" + "</heightValue>"
				+ "<widthValue unitOfMeasure=\"CM\">" + "32.6" + "</widthValue>" + "</shipmentPackageDimensions>" + "<!-- 包裹条目信息 -->" + "<shipmentPackageItemDetail>" + "<!-- 条目订单号 -->"
				+ "<OrderingOrderId>" + "C02-1133833-2453637" + "</OrderingOrderId>" + "<!-- 条目代码 -->" + "<itemID type=\"AMAZON_ASIN\" isFreeRep=\"N\">" + "B006CEWFME" + "</itemID>" + "<!-- 条目名称 -->"
				+ "<itemTitle>" + "Huggies好奇干爽舒适纸尿裤箱装XL104片（适合12-16公斤）" + "</itemTitle>" + "<!-- 数量 -->" + "<itemQuantity>" + "<quantity unitOfMeasure=\"EA\">" + "</quantity>" + "</itemQuantity>"
				+ "<!-- 条目价格信息 -->" + "<itemPriceInfo>" + "<!-- 条目单位价格 -->" + "<itemUnitPrice>" + "<chargeOrAllowance>" + "CHARGE" + "</chargeOrAllowance>" + "<monetaryAmount"
				+ " currencyISOCode=\"CNY\">" + "152.99" + "</monetaryAmount>" + "</itemUnitPrice>" + "<!-- 条目价格, 单价*数量 -->" + "<itemExtendedPrice>" + "<chargeOrAllowance>" + "CHARGE"
				+ "</chargeOrAllowance>" + "<monetaryAmount currencyISOCode=\"CNY\">" + "152.99" + "</monetaryAmount>" + "</itemExtendedPrice>" + "</itemPriceInfo>" + "<!-- 条目重量 -->" + "<itemWeight>"
				+ "<weightValue unitOfMeasure=\"KG\">" + "4.639953" + "</weightValue>" + "</itemWeight>" + "<!-- 管制品代号-->" + "<harmonizedTariffNumber>" + "0000.00.0000" + "</harmonizedTariffNumber>"
				+ "<!-- 管制分类-->" + "<harmonizedTariffDescription>" + "Baby Product Item" + "</harmonizedTariffDescription>" + "<!-- 国家 -->" + "<countryOfOrigin>CN</countryOfOrigin>"
				+ "</shipmentPackageItemDetail>" + "<!-- 总金额 -->" + "<totalDeclaredValue currencyISOCode=\"CNY\">" + "157.26" + "</totalDeclaredValue>" + "<!-- 包裹描述, 出关用-->"
				+ "<pkgHarmonizedTariffDescription>" + "Baby Product Item - New (count 1)" + "</pkgHarmonizedTariffDescription>" + "<!-- 包裹数量 -->" + "<shipmentPackageItemQuantity>"
				+ "<quantity unitOfMeasure=\"EA\">1</quantity>" + "</shipmentPackageItemQuantity>" + "<!-- 出口原因 -->" + "</shipmentPackageInfo>" + "<reasonForExport>PERMANENT</reasonForExport>"
				+ "<!-- 顾客送货时间倾向 -->" + "<preferredDeliveryTime>" + "工作日、双休日或假日均可送货" + "</preferredDeliveryTime>" + "<!-- 是否为替换货 -->" + "<isFreeReplacementExchangeFlag>" + "N"
				+ "</isFreeReplacementExchangeFlag>" + "</shipmentDetail>" + "</manifestDetail>" + "<!-- 发货单汇总 -->" + "<manifestSummary>" + "<!-- 总数量 -->" + "<totalShipmentQuantity>"
				+ "<quantity unitOfMeasure=\"EA\">211</quantity>" + "</totalShipmentQuantity>" + "<!-- 包裹总金额 -->" + "<totalShipmentValue>" + "<chargeOrAllowance>CHARGE</chargeOrAllowance>"
				+ "<monetaryAmount currencyISOCode=\"CNY\">\39600.37" + "</monetaryAmount>" + "</totalShipmentValue>" + "<!-- 包裹声报重量信息 -->" + "<totalDeclaredGrossWeight>"
				+ "<weightValue unitOfMeasure=\"KG\">" + "210999.75" + "</weightValue>" + "</totalDeclaredGrossWeight>" + "<!-- 包裹实际重量信息  -->" + "<totalActualGrossWeight>"
				+ "<weightValue unitOfMeasure=\"KG\">967.32</weightValue>" + "</totalActualGrossWeight>" + "</manifestSummary>" + "</amazonManifest>" + "</message>" + "</transmission>";
		System.out.println("订单Xml:" + order);

	}
}
