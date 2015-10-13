package cn.explink.b2c.explink;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/explinkInterface")
public class ExplinkController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	ExplinkService explinkService;

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
		return "xiufu/explinkinterface_test";
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
		return "xiufu/explinkinterface_test";
	}

}
