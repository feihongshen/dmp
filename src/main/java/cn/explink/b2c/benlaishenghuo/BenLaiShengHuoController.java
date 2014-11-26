package cn.explink.b2c.benlaishenghuo;

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
import cn.explink.dao.BranchDAO;
import cn.explink.enumutil.BranchEnum;

@Controller
@RequestMapping("/benlaishenghuo")
public class BenLaiShengHuoController {
	@Autowired
	BenLaiShengHuoService benLaiShengHuoService;
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	BenlaiInsertCwbDetailTimmer benlaiInsertCwbDetailTimmer;
	private Logger logger = LoggerFactory.getLogger(BenLaiShengHuo.class);

	@RequestMapping("/B_laipost")
	public @ResponseBody String Postxml(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/xml;charset=utf-8");
		String XMLDOC = request.getParameter("logicdata");
		String sign = request.getParameter("checkdata");
		logger.info("获得的[本来生活网]xml={}，sign={}", XMLDOC, sign);
		String res_xml = benLaiShengHuoService.PostXmlForBenlai(XMLDOC, sign, B2cEnum.benlaishenghuo.getKey());
		benlaiInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(B2cEnum.benlaishenghuo.getKey());
		return res_xml;
	}

	@RequestMapping("/hander")
	public @ResponseBody String hander(HttpServletRequest request, HttpServletResponse response) {
		benlaiInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(B2cEnum.benlaishenghuo.getKey());
		return "定时器执行完毕";
	}

	@RequestMapping("/test")
	public @ResponseBody String test(HttpServletRequest request, HttpServletResponse response) {
		String xml = "<?xml version='1.0' encoding='UTF-8'?><RequestOrders><RequestOrder><waybillCode>D6610003404565</waybillCode><order_no>D6610003404565</order_no><shippName>本来生活网</shippName><order_type>1</order_type><shippDate>2013-11-21</shippDate><deliveryName>本来生活网</deliveryName><deliveryPhone></deliveryPhone><deliveryhouseId></deliveryhouseId><deliveryhouseName></deliveryhouseName><deliveryAddress>4008-000-917</deliveryAddress><customerName>陆惠霞</customerName><customerPhone>15306213805,</customerPhone><customerAddress>江苏省 苏州市 吴中区 东吴南路173号盛丰苑别墅8栋</customerAddress><customerProvince>江苏省</customerProvince><customerCity>苏州市</customerCity><deliveryGoods>食品</deliveryGoods><deliveryAmount>0</deliveryAmount><weight>0</weight><shouldReceive>0</shouldReceive><remark>当日到达,请检查好质量</remark><goodsNum>1</goodsNum><subPackageNo></subPackageNo></RequestOrder><RequestOrder><waybillCode>D66100000</waybillCode><order_no>D66100000</order_no><shippName>本来生活网</shippName><order_type>1</order_type><shippDate>2013-11-21</shippDate><deliveryName>本来生活网</deliveryName><deliveryPhone></deliveryPhone><deliveryhouseId></deliveryhouseId><deliveryhouseName></deliveryhouseName><deliveryAddress>4008-000-917</deliveryAddress><customerName>陆惠霞</customerName><customerPhone>15306213805,</customerPhone><customerAddress>江苏省 苏州市 吴中区 东吴南路173号盛丰苑别墅8栋</customerAddress><customerProvince>江苏省</customerProvince><customerCity>苏州市</customerCity><deliveryGoods>食品</deliveryGoods><deliveryAmount>0</deliveryAmount><weight>0</weight><shouldReceive>0</shouldReceive><remark>当日到达,请检查好质量</remark><goodsNum>1</goodsNum><subPackageNo></subPackageNo></RequestOrder></RequestOrders>";
		String res_xml = benLaiShengHuoService.PostXmlForBenlai(xml, "", B2cEnum.benlaishenghuo.getKey());
		return res_xml;
	}

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("warehouselist", branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		model.addAttribute("BenLaiObject", benLaiShengHuoService.getBenlaiShenghuo(B2cEnum.benlaishenghuo.getKey()));
		model.addAttribute("joint_num", key);
		return "/b2cdj/benlaishenghuo";
	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		benLaiShengHuoService.update(key, state);
		// 保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String dangdangSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {

		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {

			benLaiShengHuoService.edit(request, key);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
		// 保存
	}

}
