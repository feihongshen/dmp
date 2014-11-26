package cn.explink.pos.alipay;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.tools.JiontDAO;
import cn.explink.domain.User;
import cn.explink.pos.tools.PosEnum;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.util.Dom4jParseUtil;

@Controller
@RequestMapping("/alipay")
public class AlipayController {
	private Logger logger = LoggerFactory.getLogger(AlipayController.class);
	@Autowired
	AlipayService alipayService;
	@Autowired
	JiontDAO jointDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable("id") int key, Model model) {
		model.addAttribute("alipaylist", alipayService.getAlipaySettingMethod(key));
		model.addAttribute("joint_num", key);
		return "jointmanage/alipay/edit";

	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(@PathVariable("id") int joint_num, HttpServletRequest request) {

		String password = request.getParameter("password");
		if (password != null && "explink".equals(password)) {
			alipayService.edit(request, joint_num);
			logger.info("operatorUser={},alipay->save", getSessionUser().getUsername());
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":0,\"error\":\"密码错误！\"}";
		}
	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		alipayService.update(key, state);
		logger.info("operatorUser={},alipay->del", getSessionUser().getUsername());
		// 保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";

	}

	/**
	 * 支付宝请求入口
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/")
	public @ResponseBody String requestByAliPay(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/xml;charset=UTF-8");
			AliPay alipay = alipayService.getAlipaySettingMethod(PosEnum.AliPay.getKey()); // 获取配置信息
			int isOpenFlag = jointDAO.getStateByJointKey(PosEnum.AliPay.getKey());
			if (isOpenFlag == 0) {
				return "未开启支付宝对接";
			}
			InputStream input = new BufferedInputStream(request.getInputStream());
			String XMLDOC = Dom4jParseUtil.getStringByInputStream(input); // 读取文件流，获得xml字符串

			return alipayService.AnalyzXMLByAlipay(alipay, XMLDOC);
		} catch (Exception e) {
			logger.error("AlipayController遇见不可预知的错误！" + e);
			e.printStackTrace();
			return "系统遇到不可预知的错误!";
		}

	}

}
