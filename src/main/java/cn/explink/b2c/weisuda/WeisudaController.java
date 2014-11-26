package cn.explink.b2c.weisuda;

import javax.servlet.http.HttpServletRequest;

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

@Controller
@RequestMapping("/weisuda")
public class WeisudaController {
	private Logger logger = LoggerFactory.getLogger(WeisudaController.class);
	@Autowired
	WeisudaService weisudaService;
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
		model.addAttribute("weisudalist", weisudaService.getWeisudaSettingMethod(PosEnum.Weisuda.getKey()));
		model.addAttribute("joint_num", key);
		return "jointmanage/weisuda/edit";

	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(@PathVariable("id") int joint_num, HttpServletRequest request) {

		String password = request.getParameter("password");
		if (password != null && "explink".equals(password)) {
			weisudaService.edit(request, joint_num);
			logger.info("operatorUser={},alipay->save", getSessionUser().getUsername());
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":0,\"error\":\"密码错误！\"}";
		}
	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		weisudaService.update(key, state);
		logger.info("operatorUser={},alipay->del", getSessionUser().getUsername());
		// 保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";

	}

}
