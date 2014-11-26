package cn.explink.pos.yeepay;

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

/**
 * 易宝支付对接
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/yeepay")
public class YeePayController {
	private Logger logger = LoggerFactory.getLogger(YeePayController.class);
	@Autowired
	YeePayService yeePayService;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@Autowired
	JiontDAO jiontDAO;

	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable("id") int key, Model model) {
		model.addAttribute("yeepaylist", yeePayService.getYeePaySettingMethod(key));
		model.addAttribute("joint_num", key);
		return "jointmanage/yeepay/edit";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(@PathVariable("id") int joint_num, HttpServletRequest request) {

		String password = request.getParameter("password");
		if (password != null && "explink".equals(password)) {
			yeePayService.edit(request, joint_num);
			logger.info("operatorUser={},yeepay->save", getSessionUser().getUsername());
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":0,\"error\":\"密码错误！\"}";
		}

	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		yeePayService.update(key, state);
		logger.info("operatorUser={},yeepay->del", getSessionUser().getUsername());
		// 保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";

	}

	/**
	 * yeepay对接请求入口
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/")
	public @ResponseBody String requestByYeePay(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/xml;charset=UTF-8");
			YeePay yeepay = yeePayService.getYeePaySettingMethod(PosEnum.YeePay.getKey()); // 获取配置信息
			int isOpenFlag = jiontDAO.getStateByJointKey(PosEnum.YeePay.getKey());
			if (isOpenFlag == 0) {
				return "未开启[易宝支付]对接!";
			}
			InputStream input = new BufferedInputStream(request.getInputStream());
			String xmlstr = Dom4jParseUtil.getStringByInputStream(input); // 读取文件流，获得xml字符串
			return yeePayService.AnalyzXMLByYeePay(xmlstr, yeepay);
		} catch (Exception e) {
			logger.error("yeepayControll部分异常，原因=" + e);
			e.printStackTrace();
		}
		return null;
	}

}
