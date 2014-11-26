package cn.explink.b2c.saohuobang;

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
@RequestMapping("/S_huobang")
public class SaohuobangController {
	@Autowired
	BranchDAO branchDAO;
	@Autowired
	SaohuobangService saohuobangService;
	@Autowired
	SaohuobangInsertCwbDetailTimmer saohuobangInsertCwbDetailTimmer;
	private Logger logger = LoggerFactory.getLogger(Saohuobang.class);

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("warehouselist", branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		model.addAttribute("SaohuobangObject", saohuobangService.getSaohuobang(key));
		model.addAttribute("joint_num", key);
		return "/b2cdj/saohuobang";

	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		saohuobangService.update(key, state);
		// 保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String dangdangSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {

		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {

			saohuobangService.edit(request, key);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
		// 保存

	}

	@RequestMapping("/Insert")
	public @ResponseBody String getInsert(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/xml;charset=utf-8");
		String retrun_xml = request.getParameter("logistics_interface");
		String sign = request.getParameter("data_digest");
		logger.info("[Insert]Controller接收到的信息sign={},retrun={}", sign, retrun_xml);
		String xml = saohuobangService.getPushXMLImport(retrun_xml, sign);

		return xml;
	}

	@RequestMapping("/Cancel")
	public @ResponseBody String getCancel(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/xml;charset=utf-8");
		String retrun_xml = request.getParameter("logistics_interface");
		String sign = request.getParameter("data_digest");
		logger.info("[Cancel]Controller接收到的信息sign={},retrun={}", sign, retrun_xml);
		String xml = saohuobangService.cancelImportXMl(retrun_xml, sign);

		return xml;

	}

	@RequestMapping("/hander")
	public @ResponseBody String hander(HttpServletRequest request, HttpServletResponse response) {
		/*
		 * response.setContentType("text/xml;charset=utf-8"); String retrun_xml=
		 * "<RequestOrder><clientID>JYHT_MJ</clientID><logisticProviderID>HWHQ</logisticProviderID><txLogisticID>D0000090</txLogisticID><customerId>HWHQ</customerId><tradeNo>seq</tradeNo><mailNo>M0000090</mailNo><type>1</type><flag>1</flag><orderType>1</orderType><storeID>1000</storeID><storeName>nike 西单大悦城店</storeName><storeAddress>长安街西单大悦城5层</storeAddress><storeTel>01012345678</storeTel><supplierID>9999</supplierID><supplierName>nike北京总代理</supplierName><orderCreateTime>2013-12-05 16:53:57</orderCreateTime><itemsAllPrice>200</itemsAllPrice><itemsTakePrice>220</itemsTakePrice><itemsSendDate>工作日</itemsSendDate><serviceType>0</serviceType><sender><name>张三</name><postCode>510890</postCode><phone>66666555</phone><mobile>13800010000</mobile><prov>广州</prov><city>广州</city><address>广州</address></sender><receiver><name>李四</name><postCode>510890</postCode><phone>66666555</phone><mobile>13800010000</mobile><prov>北京</prov><city>北京</city><address>北京</address></receiver><sendStartTime></sendStartTime><sendEndTime></sendEndTime><itemsValue>20</itemsValue><goodsValue>1900</goodsValue><itemsWeight>10</itemsWeight><items><item><itemID>201312050008</itemID><itemName>克罗心</itemName><itemBrandName>MMJ</itemBrandName><number>3</number><remark>轻拿轻放</remark></item></items><insuranceValue>0.0</insuranceValue><packageOrNot>false</packageOrNot><remark>tes</remark><special>0</special></RequestOrder>"
		 * ; String sign=request.getParameter("data_digest"); String
		 * xml=saohuobangService.getPushXMLImport(retrun_xml, sign);
		 */for (B2cEnum enums : B2cEnum.values()) { // 遍历唯品会enum，可能有多个枚举
			if (enums.getMethod().contains("saohuobang")) {
				saohuobangInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(enums.getKey());
			}
		}
		return "定时器执行完毕";
	}
}
