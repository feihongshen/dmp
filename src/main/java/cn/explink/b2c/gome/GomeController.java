package cn.explink.b2c.gome;

import java.util.List;

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
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CwbTempDAO;
import cn.explink.domain.CwbTemp;
import cn.explink.enumutil.BranchEnum;
import cn.explink.util.WebServiceHandler;

@Controller
@RequestMapping("/gome")
public class GomeController {
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;
	@Autowired
	GomeService gomeService;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	GomeService_GetOrders gomeService_GetOrders;
	@Autowired
	CwbTempDAO cwbTempDAO;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("gomeObject", gomeService.getGome(key));
		model.addAttribute("warehouselist", branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		model.addAttribute("joint_num", key);
		return "b2cdj/gome";
	}

	@RequestMapping("/saveGome/{id}")
	public @ResponseBody String gomeSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {
		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {
			try{
				gomeService.edit(request, key);
				return "{\"errorCode\":0,\"error\":\"修改成功\"}";
			}catch(Exception e){
				return "{\"errorCode\":1,\"error\":\""+ e.getMessage() +"\"}";
			}
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		gomeService.update(key, state);
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	@RequestMapping("/testxml")
	public String testxml(Model model, @RequestParam(value = "xml", required = false, defaultValue = "") String xml) {

		xml = xml.replaceAll("<list>", "").replace("</list>", "");
		logger.info("[国美]获取订单详情returnValue:{}", xml.toString());
		if (!"".equals(xml)) {
			gomeService_GetOrders.inserttemp(xml);
		}

		return "/b2cdj/gome/testgome";
	}

	@RequestMapping("/getorder")
	public String getorder(Model model, @RequestParam(value = "cwb", required = false, defaultValue = "") String xml) {

		xml = xml.replaceAll("<list>", "").replace("</list>", "");
		logger.info("[国美]获取订单详情returnValue:{}", xml.toString());
		if (!"".equals(xml)) {
			gomeService_GetOrders.getorder(xml);
		}

		return "/b2cdj/gome/getgome";
	}

	@RequestMapping("/insert")
	public void insert() {
		gomeService.selectTempAndInsertToCwbDetail();

	}

	@RequestMapping("/excute")
	public void getOrder() {
		gomeService.gomeInterfaceInvoke();

	}

	@RequestMapping("/successOrder")
	public void successOrder() {
		gomeService.gomeInterfaceSuccessOrderInvoke();

	}

	@RequestMapping("/reason")
	public void reason() {
		gomeService.Reason();

	}

	@RequestMapping("/test")
	public void test() {
		gomeService.gomeInterfaceInvoke();

	}

	@RequestMapping("/codxiufu")
	public String back(Model model, @RequestParam(value = "cwb", required = false, defaultValue = "") String cwbs) {
		if (!cwbs.equals("")) {
			String[] cwbstrs = cwbs.split("\r\n");
			String cwbstr = "";
			for (int i = 0; i < cwbstrs.length; i++) {
				cwbstr += "'" + cwbstrs[i] + "',";
			}
			cwbstr = cwbstr.substring(0, cwbstr.length() - 1);
			List<CwbTemp> cwpTempList = cwbTempDAO.getCwbTempByCwbs(cwbstr);
			if (cwpTempList != null && cwpTempList.size() > 0) {
				Gome gome = gomeService.getGome(B2cEnum.Gome.getKey());
				for (CwbTemp cwbTemp : cwpTempList) {

					String method = "setTransactionStatusByXmlStr";
					String pram = returnOrderToXMl(cwbTemp, "Y", "获取订单成功");
					logger.info("返回给国美获取订单成功标识xml：" + pram);
					try {
						Object o = WebServiceHandler.invokeWsByNameAndPassWord(gome.getTnt_url(), method, pram, gome.getUsername(), gome.getPassword());
						// 通知国美也处理完成
						if (o == null || o.toString().equals("")) {
							cwbTempDAO.updateCwbtemp(cwbTemp.getCwb(), 1);// 修改为已获取正确
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		model.addAttribute("result", "处理成功");
		return "/codxiufu";
	}

	private String returnOrderToXMl(CwbTemp cwbTemp, String state, String remark) {
		String orderXml = "";
		if (cwbTemp.getCwb() != null && !"".equals(cwbTemp.getCwb())) {
			orderXml = "<?xml version=\"1.0\" encoding=\"GBK\"?>" + "<transactionStatus>" + "<conditions>" + "<transactionStatusVO>" + "<transaction_id>" + cwbTemp.getCwb() + "</transaction_id>"
					+ "<is_successful>" + state + "</is_successful>" + "<remark>" + remark + "</remark>" + "</transactionStatusVO>" + "</conditions>" + "</transactionStatus>";
		}
		return orderXml;
	}

	public static void main(String[] args) {
		String order = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<orderVo>" + "<buid>8270</buid>" + "<businessCode>SO</businessCode>" + "<orderNumber>100108****</orderNumber>"
				+ "<lspAbbr>合肥汇文货物配送服务有限公司</lspAbbr>" + "<lspCode>21000047</lspCode>" + "<orderDate class=\"sql-timestamp\">2011-10-26 09:07:20.0</orderDate>"
				+ "<originalOrderNum></originalOrderNum>" + "<shippingType>G</shippingType>" + "<category></category>" + "<fromMasLoc>D002</fromMasLoc>"
				+ "<exDate class=\"sql-timestamp\">2011-10-28 08:54:28.0</exDate>" + "<shipperCode></shipperCode>" + "<shipCity>马鞍山市</shipCity>" + "<receiptReturnFlag></receiptReturnFlag>"
				+ "<receiptReturnType></receiptReturnType>" + "<insuranceFlag></insuranceFlag>" + "<insuranceFee>52.02</insuranceFee>" + "<sellingPrice>52.02</sellingPrice>"
				+ "<orderType>SO</orderType>" + "<inspectionProcedure></inspectionProcedure>" + "<bestDelievryTime></bestDelievryTime>" + "<paymentTerm>COD</paymentTerm>"
				+ "<paymentType>CASH</paymentType>" + "<totalDualAmount>60.0</totalDualAmount>" + "<freightCollectFlag></freightCollectFlag>" + "<identifyingCode>6588</identifyingCode>"
				+ "<telephoneFlag>Y</telephoneFlag>" + "<airShipFlag>Y</airShipFlag>" + "<attachment1></attachment1>" + "<attachment2></attachment2>" + "<comments></comments>" + "<shippingAddress>"
				+ "<zipcode></zipcode>" + "<line1>安徽省马鞍山市花山区解放路******</line1>" + "<line2></line2>" + "<line3></line3>" + "<line4></line4>" + "<email>jl0062@suhu.com</email>"
				+ "<contactName>许成斌</contactName>" + "<province>安徽省</province>" + "<cityName>马鞍山市</cityName>" + "<divisionName>花山区</divisionName>" + "<villageName></villageName>"
				+ "<companyName></companyName>" + "<mobileNumber1>133******25</mobileNumber1>" + "<mobileNumber2></mobileNumber2>" + "<telephoneNumber1></telephoneNumber1>"
				+ "<telephoneNumber2></telephoneNumber2>" + "<faxNumber1></faxNumber1>" + "<faxNumber2></faxNumber2>" + "<comments></comments>" + "</shippingAddress>" + "<fdd>"
				+ "<deliveryWeekendFlag>Z</deliveryWeekendFlag>" + "<deliveryTimeslot></deliveryTimeslot>" + "<fddDate class=\"sql-timestamp\">2011-10-28 09:26:10.0</fddDate>"
				+ "<fddFlag>N</fddFlag>" + "<comments></comments>" + "</fdd>" + "</orderVo>";
		System.out.println("订单xml:" + order);

		String asnVO = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<asnVo>" + "<businessCode>ASN</businessCode>" + "<buid>8270</buid>" + "<asnNumber>0000323049</asnNumber>"
				+ "<lspCode>21000047</lspCode>" + "<subLine>21000047</subLine>" + "<lspAbbr>合肥汇文货物配送服务有限公司</lspAbbr>" + "<shipDate class=\"sql-timestamp\">2011-11-14 08:47:56.0</shipDate>"
				+ "<fromMasLoc>D002</fromMasLoc>" + "<comments></comments>" + "<shippingAddress>" + "<zipcode></zipcode>" + "<line1></line1>" + "<line2></line2>" + "<line3></line3>"
				+ "<line4></line4>" + "<email></email>" + "<contactName></contactName>" + "<province></province>" + "<cityName></cityName>" + "<divisionName></divisionName>"
				+ "<villageName></villageName>" + "<companyName></companyName>" + "<mobileNumber1></mobileNumber1>" + "<mobileNumber2></mobileNumber2>" + "<telephoneNumber1></telephoneNumber1>"
				+ "<telephoneNumber2></telephoneNumber2>" + "<faxNumber1></faxNumber1>" + "<faxNumber2></faxNumber2>" + "<comments></comments>" + "</shippingAddress>" + "<truckInfos>" + "<truckInfo>"
				+ "<truckNumber>皖ND2353</truckNumber>" + "<truckDesc></truckDesc>" + "<driverName>杜成春</driverName>" + "<driverPhoneNumber>13817258679</driverPhoneNumber>" + "<comments></comments>"
				+ "</truckInfo>" + "</truckInfos>" + "<items>" + "<item>" + "<boxId></boxId>" + "<boxCode></boxCode>" + "<boxBarcode></boxBarcode>" + "<grossWeight>0.0</grossWeight>"
				+ "<volumeWeight>0.0</volumeWeight>" + "<length>0.0</length>" + "<width>0.0</width>" + "<height>0.0</height>" + "<comments></comments>" + "<itemLines>" + "<itemLine>"
				+ "<boxId>1</boxId>" + "<boxCode>4000000016</boxCode>" + "<boxBarcode></boxBarcode>" + "<grossWeight>0.676</grossWeight>" + "<volumeWeight>0.0</volumeWeight>"
				+ "<length>110.0</length>" + "<width>200.0</width>" + "<height>230.0</height>" + "<orderNumber>100112****</orderNumber>" + "<comments></comments>" + "</itemLine>" + "<itemLine>"
				+ "<boxId>1</boxId>" + "<boxCode>4000000010</boxCode>" + "<boxBarcode></boxBarcode>" + "<grossWeight>6.082</grossWeight>" + "<volumeWeight>0.0</volumeWeight>"
				+ "<length>520.0</length>" + "<width>385.0</width>" + "<height>330.0</height>" + "<orderNumber>100112****</orderNumber>" + "<comments></comments>" + "</itemLine>" + "</itemLines>"
				+ "</item>" + "</items>" + "</asnVo>";
		System.out.println("退货订单Xml:" + asnVO);

		String returnXml = "<?xml version=\"1.0\" encoding=\"GBK\"?>" + "<transactionStatus>" + "<conditions>" + "<transactionStatusVO>" + "<transaction_id>1000000000013</transaction_id>"
				+ "<is_successful>Y</is_successful>" + "<remark>状态</remark>" + "</transactionStatusVO>" + "</conditions>" + "</transactionStatus>";

		System.out.println("返回给国美的xml:" + returnXml);
	}
}
