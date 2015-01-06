package cn.explink.pos.tonglianpos;

import java.io.BufferedInputStream;
import java.io.IOException;
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
import cn.explink.pos.tonglianpos.delivery.DeliveryService;
import cn.explink.pos.tools.PosEnum;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.util.Dom4jParseUtil;

@Controller
@RequestMapping("/tlmpos")
public class TlmposController {
	private Logger logger = LoggerFactory.getLogger(TlmposController.class);
	@Autowired
	TlmposService tlmposService;
	@Autowired
	JiontDAO jointDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	DeliveryService deliveryService;

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) this.securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable("id") int key, Model model) {
		model.addAttribute("tlmposlist", this.tlmposService.gettlmposSettingMethod(PosEnum.TongLianPos.getKey()));
		model.addAttribute("joint_num", key);
		return "jointmanage/tlmpos/edit";

	}

	@RequestMapping("/save/{id}")
	public @ResponseBody
	String save(@PathVariable("id") int joint_num, HttpServletRequest request) {

		String password = request.getParameter("password");
		if ((password != null) && "explink".equals(password)) {
			this.tlmposService.edit(request, joint_num);
			this.logger.info("operatorUser={},alipay->save", this.getSessionUser().getUsername());
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":0,\"error\":\"密码错误！\"}";
		}
	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody
	String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		this.tlmposService.update(key, state);
		this.logger.info("operatorUser={},alipay->del", this.getSessionUser().getUsername());
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
	public @ResponseBody
	String requestByAliPay(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/xml;charset=UTF-8");
			Tlmpos tlmpos = this.tlmposService.gettlmposSettingMethod(PosEnum.TongLianPos.getKey()); // 获取配置信息
			int isOpenFlag = this.jointDAO.getStateByJointKey(PosEnum.TongLianPos.getKey());
			if (isOpenFlag == 0) {
				return "未开启通联POS对接";
			}
			InputStream input = new BufferedInputStream(request.getInputStream());
			String XMLDOC = Dom4jParseUtil.getStringByInputStream(input); // 读取文件流，获得xml字符串

			this.logger.info("通联pos请求XML={}", XMLDOC);

			return this.tlmposService.AnalyzXMLBytlmpos(tlmpos, XMLDOC);
		} catch (Exception e) {
			this.logger.error("AlipayController遇见不可预知的错误！", e);

			return "未知异常!";
		}

	}

	@RequestMapping("/delivery")
	public @ResponseBody
	String test(HttpServletRequest request) throws IOException {

		InputStream input = new BufferedInputStream(request.getInputStream());
		String XMLDOC = Dom4jParseUtil.getStringByInputStream(input); // 读取文件流，获得xml字符串

		this.logger.info("读取POS机领货流文件：xml={}", XMLDOC);

		return this.deliveryService.delivery(request, XMLDOC);
	}

}
