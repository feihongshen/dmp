package cn.explink.b2c.vipshop.address;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JointService;

@Controller
@RequestMapping("/address")
public class AddressInterfaceController {
	@Autowired
	AddressInterfaceService addressInterfaceService;
	@Autowired
	JointService jointService;

	private Logger logger = LoggerFactory.getLogger(AddressInterfaceController.class);

	@RequestMapping("/")
	public @ResponseBody
	String requestBychinaums(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/xml;charset=UTF-8");
			int isOpenFlag = this.jointService.getStateForJoint(B2cEnum.VipShopAddress.getKey());
			if (isOpenFlag == 0) {
				this.logger.info("未开启唯品会请求地址库对接！");
				return this.addressInterfaceService.returnExp("未开启唯品会请求地址库");
			}
			String xml = request.getParameter("data");
			this.logger.info("唯品会匹配站点: 地址：请求xml------{}", xml);
			VipShopAddress vip = this.addressInterfaceService.getVipShopAdrress(B2cEnum.VipShopAddress.getKey());
			return this.addressInterfaceService.getAddress(xml, vip);
		} catch (Exception e) {
			this.logger.error("唯品会请求地址库 遇见不可预知的错误！" + e);
			return this.addressInterfaceService.returnExp("系统内部错误");
		}

	}

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("vipshopObject", this.addressInterfaceService.getVipShopAdrress(key));
		model.addAttribute("joint_num", key);
		return "b2cdj/vipshopAddress";

	}

	@RequestMapping("/save/{id}")
	public @ResponseBody
	String saveVipShop(Model model, @PathVariable("id") int key, HttpServletRequest request) {

		if ((request.getParameter("password") != null) && "explink".equals(request.getParameter("password"))) {

			this.addressInterfaceService.edit(request, key);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
		// 保存

	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody
	String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		this.addressInterfaceService.update(key, state);
		// 保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	@RequestMapping("/test")
	public String test(HttpServletRequest request) {
		return "b2cdj/vipshop/test";
	}

	@RequestMapping("/match_test")
	public String match_test(HttpServletRequest request, HttpServletResponse response) {
		String xml = "<request>" + "<head>" + "<usercode>VIPSHOP</usercode>" + "<batchno>2013072312341034</batchno>" + "<key>514000ac484f7631d22284388b69868b</key>" + "</head>" + "<items>" + "<item>"
				+ "<itemno>1</itemno>" + "<province></province>" + "<city></city>" + "<area><![CDATA[天河区]]></area>" + "<address><![CDATA[广西壮族自治区桂林市象山区象山街道铁西兴进曦镇12-2栋2单元304]]></address>" + "</item>"
				+ "<item>" + "<itemno>2</itemno>" + "<province><![CDATA[广西]]></province>" + "<city><![CDATA[广西]]></city>" + "<area></area>" + "<address><![CDATA[广西壮族自治区桂林市象山区象山街道西门大酒店旁]]></address>"
				+ "</item>" + "</items>" + "</request>";

		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/xml;charset=UTF-8");
			int isOpenFlag = this.jointService.getStateForJoint(B2cEnum.VipShopAddress.getKey());
			if (isOpenFlag == 0) {
				this.logger.info("未开启唯品会请求地址库对接！");
				return this.addressInterfaceService.returnExp("未开启唯品会请求地址库");
			}

			this.logger.info("唯品会匹配站点: 地址：请求xml------{}", xml);
			VipShopAddress vip = this.addressInterfaceService.getVipShopAdrress(B2cEnum.VipShopAddress.getKey());
			return this.addressInterfaceService.getAddress(xml, vip);
		} catch (Exception e) {
			this.logger.error("唯品会请求地址库 遇见不可预知的错误！" + e);
			return this.addressInterfaceService.returnExp("系统内部错误");
		}
	}

	public static void main(String[] args) {
		String xml = "<request>" + "<head>" + "<usercode>VIPSHOP</usercode>" + "<batchno>2013072312341034</batchno>" + "<key>2341asd9fasdfadf23</key>" + "</head>" + "<items>" + "<item>"
				+ "<itemno>1</itemno>" + "<province><![CDATA[广东省]]></province>" + "<city><![CDATA[广州市]]></city>" + "<area><![CDATA[天河区]]></area>" + "<address><![CDATA[广东省广州市天河区天河东路118号]]></address>"
				+ "</item>" + "<item>" + "<itemno>2</itemno>" + "<province><![CDATA[广东省]]></province>" + "<city><![CDATA[广州市]]></city>" + "<area><![CDATA[天河区]]></area>"
				+ "<address><![CDATA[广东省广州市天河区天河东路119号]]></address>" + "</item>" + "</items>" + "</request>";
		System.out.println("xml:" + xml);
	}

}
