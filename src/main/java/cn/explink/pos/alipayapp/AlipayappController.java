package cn.explink.pos.alipayapp;

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
@RequestMapping("/alipayapp")
public class AlipayappController {
	private Logger logger = LoggerFactory.getLogger(AlipayappController.class);
	@Autowired
	AlipayappService alipayappService;
	@Autowired
	JiontDAO jointDAO;

	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable("id") int key, Model model) {
		model.addAttribute("alipayapplist", alipayappService.getAlipayapp(key));
		model.addAttribute("joint_num", key);
		return "jointmanage/alipayapp/edit";

	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(@PathVariable("id") int joint_num, HttpServletRequest request) {

		String password = request.getParameter("password");
		if (password != null && "explink".equals(password)) {
			alipayappService.edit(request, joint_num);

			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":0,\"error\":\"密码错误！\"}";
		}
	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		alipayappService.update(key, state);

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
			Alipayapp alipay = alipayappService.getAlipayapp(PosEnum.AliPayApp2.getKey()); // 获取配置信息
			int isOpenFlag = jointDAO.getStateByJointKey(PosEnum.AliPayApp2.getKey());
			if (isOpenFlag == 0) {
				logger.warn("未开启支付宝APP对接");
				return "未开启支付宝APP对接";
			}
			// 基本参数

			AlipayParam param = buildRequestParams(request); // 封装请求的参数

			logger.info("alipay-app请求参数:service=" + param.getService() + ",partner=" + param.getPartner() + ",charset=" + param.getCharset() + ",sign_type=" + param.getSign_type() + ",sign="
					+ param.getSign() + ",bill_no=" + param.getBill_no() + ",logistics_code=" + param.getLogistics_code() + ",bill_id=" + param.getBill_id() + ",pay_amount=" + param.getPay_amount()
					+ ",pay_status=" + param.getPay_status() + ",query_amount=" + param.getQuery_amount() + ",trade_time=" + param.getTrade_time());

			return alipayappService.AnalyzParamsByAlipayapp(alipay, param);

		} catch (Exception e) {
			logger.error("alipay-app未知异常", e);
			return "未知异常";
		}

	}

	private AlipayParam buildRequestParams(HttpServletRequest request) {

		String service = request.getParameter("service");
		String charset = request.getParameter("charset");
		String sign_type = request.getParameter("sign_type");
		String sign = request.getParameter("sign");

		// 业务参数
		String bill_no = request.getParameter("bill_no"); // 运单号
		String logistics_code = request.getParameter("logistics_code"); // 物流公司编码
		String bill_id = request.getParameter("bill_id"); // 支付宝运单号
		String pay_amount = request.getParameter("pay_amount"); // 实付金额
		String pay_status = request.getParameter("pay_status"); // 支付状态
		String query_amount = request.getParameter("query_amount"); // 查询金额
		String trade_time = request.getParameter("trade_time"); // 支付完成时间

		AlipayParam param = new AlipayParam();
		param.setService(service);
		param.setCharset(charset);
		param.setSign_type(sign_type);
		param.setSign(sign);
		param.setBill_id(bill_id);
		param.setPay_amount(pay_amount);
		param.setPay_status(pay_status);
		param.setQuery_amount(query_amount);
		param.setTrade_time(trade_time);
		param.setBill_no(bill_no);
		param.setLogistics_code(logistics_code);
		return param;
	}

}
