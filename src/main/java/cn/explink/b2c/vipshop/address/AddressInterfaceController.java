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
import cn.explink.enumutil.BranchEnum;

@Controller
@RequestMapping("/address")
public class AddressInterfaceController {
	@Autowired
	AddressInterfaceService addressInterfaceService;
	@Autowired
	JointService jointService;

	private Logger logger = LoggerFactory.getLogger(AddressInterfaceController.class);

	@RequestMapping("/")
	public @ResponseBody String requestBychinaums(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/xml;charset=UTF-8");
			int isOpenFlag = jointService.getStateForJoint(B2cEnum.VipShopAddress.getKey());
			if (isOpenFlag == 0) {
				logger.info("未开启唯品会请求地址库对接！");
				return addressInterfaceService.returnExp("未开启唯品会请求地址库");
			}
			String xml = request.getParameter("data");
			logger.info("唯品会匹配站点: 地址：请求xml------{}", xml);
			VipShopAddress vip = addressInterfaceService.getVipShopAdrress(B2cEnum.VipShopAddress.getKey());
			return addressInterfaceService.getAddress(xml, vip);
		} catch (Exception e) {
			logger.error("唯品会请求地址库 遇见不可预知的错误！" + e);
			return addressInterfaceService.returnExp("系统内部错误");
		}

	}

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("vipshopObject", addressInterfaceService.getVipShopAdrress(key));
		model.addAttribute("joint_num", key);
		return "b2cdj/vipshopAddress";

	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String saveVipShop(Model model, @PathVariable("id") int key, HttpServletRequest request) {

		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {

			addressInterfaceService.edit(request, key);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
		// 保存

	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		addressInterfaceService.update(key, state);
		// 保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	@RequestMapping("/test")
	public String test(HttpServletRequest request) {
		return "b2cdj/vipshop/test";
	}

	public static void main(String[] args) {
		String xml = "<request>" + "<head>" + "<usercode>VIPSHOP</usercode>" + "<batchno>2013072312341034</batchno>" + "<key>2341asd9fasdfadf23</key>" + "</head>" + "<items>" + "<item>"
				+ "<itemno>1</itemno>" + "<province><![CDATA[广东省]]></province>" + "<city><![CDATA[广州市]]></city>" + "<area><![CDATA[天河区]]></area>" + "<address><![CDATA[广东省广州市天河区天河东路118号]]></address>"
				+ "</item>" + "<item>" + "<itemno>2</itemno>" + "<province><![CDATA[广东省]]></province>" + "<city><![CDATA[广州市]]></city>" + "<area><![CDATA[天河区]]></area>"
				+ "<address><![CDATA[广东省广州市天河区天河东路119号]]></address>" + "</item>" + "</items>" + "</request>";
		System.out.println("xml:" + xml);
	}
}
