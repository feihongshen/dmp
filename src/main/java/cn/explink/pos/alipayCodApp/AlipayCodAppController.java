package cn.explink.pos.alipayCodApp;

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

import cn.explink.b2c.tools.JiontDAO;
import cn.explink.pos.tools.PosEnum;

@Controller
@RequestMapping("/alipaycodapp")
public class AlipayCodAppController {
	private Logger logger = LoggerFactory.getLogger(AlipayCodAppController.class);
	@Autowired
	AlipayCodAppService alipayCodAppService;
	@Autowired
	JiontDAO jointDAO;

	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable("id") int key, Model model) {
		model.addAttribute("alipaycodapplist", alipayCodAppService.getAlipaySettingMethod(key));
		model.addAttribute("joint_num", key);
		return "jointmanage/alipaycodapp/edit";

	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(@PathVariable("id") int joint_num, HttpServletRequest request) {

		String password = request.getParameter("password");
		if (password != null && "explink".equals(password)) {
			alipayCodAppService.edit(request, joint_num);

			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":0,\"error\":\"密码错误！\"}";
		}
	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		alipayCodAppService.update(key, state);

		// 保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";

	}

	/**
	 * 支付宝请求入口 app
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/")
	public @ResponseBody String requestByAliPayCodApp(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/xml;charset=UTF-8");
			AliPayCodApp alipay = alipayCodAppService.getAlipaySettingMethod(PosEnum.AliPayCodApp.getKey()); // 获取配置信息
			int isOpenFlag = jointDAO.getStateByJointKey(PosEnum.AliPayCodApp.getKey());
			if (isOpenFlag == 0) {
				logger.warn("未开启支付宝CODAPP对接");
				return "未开启支付宝CODAPP对接";
			}

			String service = request.getParameter("service");
			String partner = request.getParameter("partner");
			String charset = request.getParameter("charset");
			String sign_type = request.getParameter("sign_type");
			String sign = request.getParameter("sign");
			String timestamp = request.getParameter("timestamp");
			String logistics_bill_no = request.getParameter("logistics_bill_no");
			String logistics_code = request.getParameter("logistics_code");
			String ord_pmt_time = request.getParameter("ord_pmt_time");
			String ord_pmt_amt = request.getParameter("ord_pmt_amt");

			logger.info("alipaycodapp请求参数:service=" + service + ",partner=" + partner + ",charset=" + charset + ",sign_type=" + sign_type + ",sign=" + sign + ",timestamp=" + timestamp
					+ ",logistics_bill_no=" + logistics_bill_no + ",logistics_code=" + logistics_code + ",ord_pmt_time=" + ord_pmt_time + ",ord_pmt_amt=" + ord_pmt_amt);

			return alipayCodAppService.AnalyzXMLByAlipayCodApp(alipay, service, partner, charset, sign_type, sign, timestamp, logistics_bill_no, logistics_code, ord_pmt_time, ord_pmt_amt);

		} catch (Exception e) {
			logger.error("AlipayControllerCODAPP遇见不可预知的错误！", e);

			return "系统遇到不可预知的错误!";
		}

	}

}
