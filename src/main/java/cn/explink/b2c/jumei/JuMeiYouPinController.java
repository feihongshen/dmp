package cn.explink.b2c.jumei;

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
@RequestMapping("/jumeiyoupin")
public class JuMeiYouPinController {
	private Logger logger = LoggerFactory.getLogger(JuMeiYouPinController.class);
	@Autowired
	JuMeiYouPinService jumeiyoupinService;
	@Autowired
	JointService jointService;

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		String editJsp = "";
		for (B2cEnum fote : B2cEnum.values()) {
			if (fote.getKey() == key) {
				editJsp = fote.getMethod();
				break;
			}
		}
		model.addAttribute("jumeiyoupinObject", jumeiyoupinService.getJuMeiYouPin(key));
		model.addAttribute("joint_num", key);
		return "b2cdj/" + editJsp;

	}

	@RequestMapping("/saveJumeiyoupin/{id}")
	public @ResponseBody String jumeiyoupinSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {

		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {
			try{
				jumeiyoupinService.edit(request, key);
				return "{\"errorCode\":0,\"error\":\"修改成功\"}";
			}catch(Exception e){
				return "{\"errorCode\":1,\"error\":\""+ e.getMessage() +"\"}";
			}
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
		// 保存

	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		jumeiyoupinService.update(key, state);
		// 保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";

	}

	/**
	 * 聚美请求接口地址 客服处理请求
	 */
	@RequestMapping("/interface")
	public @ResponseBody String requestByJuMei(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("GBK");
			response.setCharacterEncoding("GBK");
			JuMeiYouPin jumei = jumeiyoupinService.getJuMeiYouPin(B2cEnum.JuMeiYouPin.getKey());
			String exceptionReply = request.getParameter("exceptionReply");
			String xmlstr = new String(exceptionReply.getBytes("iso-8859-1"), "GBK");
			String cwb = request.getParameter("cwb");
			String unixstamp = request.getParameter("unixstamp");
			String sign = request.getParameter("sign");

			return jumeiyoupinService.requestMethod(jumei, xmlstr, cwb, unixstamp, sign);
		} catch (Exception e) {
			logger.error("聚美处理业务逻辑异常！" + e);
			return "处理业务逻辑异常";
		}
	}

	/**
	 * 跟踪接口查询
	 */
	@RequestMapping("/search")
	public @ResponseBody String requestCwbSearch(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			JuMeiYouPin jumei = jumeiyoupinService.getJuMeiYouPin(B2cEnum.JuMeiYouPin.getKey());
			String cwb = request.getParameter("cwb");

			int isOpenFlag = jointService.getStateForJoint(B2cEnum.JuMeiYouPin.getKey());
			if (isOpenFlag == 0) {
				return "未开启[聚美]查询接口";
			}
			return jumeiyoupinService.requestMehodCwbSearch(jumei, cwb);
		} catch (Exception e) {
			logger.error("聚美处理业务逻辑异常！", e);
			return "处理业务逻辑异常";
		}
	}

}
