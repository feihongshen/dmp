package cn.explink.b2c.moonbasa;

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

import cn.explink.b2c.jingdong.JingDong;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.BranchDAO;

@Controller
@RequestMapping("/moonbasa")
public class MoonbasaController {
	private Logger logger = LoggerFactory.getLogger(MoonbasaController.class);
	@Autowired
	MoonbasaService moonbasaService;

	@Autowired
	BranchDAO branchDAO;
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
		model.addAttribute("moonbasaObject", moonbasaService.getMoonBasa(key));
		model.addAttribute("joint_num", key);
		return "b2cdj/" + editJsp;

	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String dangdangSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {

		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {

			moonbasaService.edit(request, key);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
		// 保存

	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		moonbasaService.update(key, state);
		// 保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	/**
	 * 提供口查询
	 */
	@RequestMapping("/search")
	public @ResponseBody String requestCwbSearch(HttpServletRequest request, HttpServletResponse response) {
		try {

			response.setCharacterEncoding("UTF-8");

			int isOpenFlag = jointService.getStateForJoint(B2cEnum.Moonbasa.getKey());
			if (isOpenFlag == 0) {
				return "未开启[梦芭莎]查询接口";
			}
			Moonbasa mbs = moonbasaService.getMoonBasa(B2cEnum.Moonbasa.getKey());

			String custcode = request.getParameter("CustCode");
			String pwd = request.getParameter("Pwd");
			String from = request.getParameter("From");
			String to = request.getParameter("To");

			logger.info("梦芭莎请求信息CustCode={},Pwd={},from=" + from + ",to=" + to, custcode, pwd);

			String responseXML = moonbasaService.requestCwbSearchInterface(mbs, custcode, pwd, from, to);
			logger.info("返回梦芭莎信息={}", responseXML);

			return responseXML;
		} catch (Exception e) {
			logger.error("梦芭莎处理业务逻辑异常！", e);
			return "处理业务逻辑异常";
		}
	}

}
