package cn.explink.b2c.explink;

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

@Controller
@RequestMapping("/explinkInterface")
public class ExplinkController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	ExplinkService explinkService;

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("explinkObject", explinkService.getExplink(key));
		model.addAttribute("joint_num", key);
		return "b2cdj/explinkinterface";

	}

	@RequestMapping("/saveExplink/{id}")
	public @ResponseBody String explinkInterfaceSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {

		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {

			explinkService.edit(request, key);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
		// 保存

	}

	@RequestMapping("/saveDangdang/{id}")
	public @ResponseBody String dangdangSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {

		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {

			explinkService.edit(request, key);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
		// 保存

	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		explinkService.update(key, state);
		// 保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";

	}

	@RequestMapping("/")
	public @ResponseBody String explinkinterface(HttpServletResponse response, HttpServletRequest request) {
		String cwbs = "";
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			cwbs = request.getParameter("cwb");
			String companyname = request.getParameter("companyname");
			String sign = request.getParameter("sign");
			return explinkService.getCwbStatusInterface(cwbs, companyname, sign);
		} catch (Exception e) {
			logger.error("查询explink接口发生不可预知的异常,当前cwb=" + cwbs, e);
			return explinkService.respExptMsg("查询发生不可预知的异常");
		}

	}

	@RequestMapping("/view_test")
	public String explinkinterface_view(HttpServletResponse response, HttpServletRequest request, Model model) {
		return "b2cdj/explink_test/explinkinterface_test";
	}

	@RequestMapping("/test")
	public String explinkinterface_test(HttpServletResponse response, HttpServletRequest request, Model model) {
		String cwbs = "";
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			cwbs = request.getParameter("cwb");
			String companyname = request.getParameter("companyname");
			String sign = request.getParameter("sign");
			String values = explinkService.getCwbStatusInterface(cwbs, companyname, sign);
			model.addAttribute("values", values);

		} catch (Exception e) {
			logger.error("查询explink接口发生不可预知的异常,当前cwb=" + cwbs, e);
		}
		return "b2cdj/explink_test/explinkinterface_test";
	}

}
