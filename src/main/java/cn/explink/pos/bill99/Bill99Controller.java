package cn.explink.pos.bill99;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

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
@RequestMapping("/bill99")
public class Bill99Controller {
	private Logger logger = LoggerFactory.getLogger(Bill99Controller.class);
	@Autowired
	Bill99Service bill99Service;
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
		model.addAttribute("bill99list", bill99Service.getBill99SettingMethod(key));
		model.addAttribute("joint_num", key);
		return "jointmanage/bill99/edit";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(@PathVariable("id") int joint_num, HttpServletRequest request) {
		String password = request.getParameter("password");
		if (password != null && "explink".equals(password)) {
			bill99Service.edit(request, joint_num);
			logger.info("operatorUser={},bill99->save", getSessionUser().getUsername());
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":0,\"error\":\"密码错误！\"}";
		}
	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		bill99Service.update(key, state);
		logger.info("operatorUser={},bill99->del", getSessionUser().getUsername());
		// 保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	@RequestMapping("/")
	public @ResponseBody String requestByBill99(HttpServletRequest request, HttpServletResponse response) {
		String returnCode = null;
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/xml;charset=UTF-8");
			Bill99 bill99 = bill99Service.getBill99SettingMethod(PosEnum.Bill99.getKey()); // 获取配置信息
			int isOpenFlag = jointDAO.getStateByJointKey(PosEnum.Bill99.getKey());
			if (isOpenFlag == 0) {
				returnCode = "未开启快钱对接";
			} else {
				InputStream input = new BufferedInputStream(request.getInputStream());
				String xmlstr = Dom4jParseUtil.getStringByInputStream(input); // 读取文件流，获得xml字符串
				returnCode = bill99Service.AnalyzXMLByBill99(xmlstr, returnCode, bill99);
			}
		} catch (Exception e) {
			logger.error("系统遇到不可预知的异常", e);
			e.printStackTrace();
		}
		return returnCode;
	}

}
