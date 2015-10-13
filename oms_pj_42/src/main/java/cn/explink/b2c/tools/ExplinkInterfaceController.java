package cn.explink.b2c.tools;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.dangdang.DangDangService;
import cn.explink.b2c.tmall.TmallService;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.vipshop.VipShopCwbFeedBackService;
import cn.explink.b2c.yixun.YiXunService;

@Controller
@RequestMapping("/explinkinterface")
public class ExplinkInterfaceController {
	@Autowired
	DangDangService dangdangService;
	@Autowired
	TmallService tmallService;
	@Autowired
	YiXunService yixunService;

	@Autowired
	ExplinkCwbSearchService explinkCwbSearchService;
	@Autowired
	VipShopCwbFeedBackService vipShopCwbFeedBackService;

	@RequestMapping("/")
	public @ResponseBody String liebo(HttpServletResponse response, HttpServletRequest request) {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String cwbs = request.getParameter("cwb");
		String companyname = request.getParameter("companyname");
		String sign = request.getParameter("sign");
		return explinkCwbSearchService.getLiebo(cwbs, companyname, sign);

	}

	@RequestMapping("/dangdang")
	public @ResponseBody String dangdang(HttpServletResponse response, HttpServletRequest request) {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		return explinkCwbSearchService.getDangdang(request);

	}

	@RequestMapping("/dangdang_test")
	public @ResponseBody String dangdangtest(HttpServletResponse response, HttpServletRequest request) {
		dangdangService.feedback_status();
		return "";

	}

	@RequestMapping("/tmall_test")
	public @ResponseBody String tmalltest(HttpServletResponse response, HttpServletRequest request) {
		for (B2cEnum enums : B2cEnum.values()) {
			if (enums.getMethod().contains("tmall")) {
				tmallService.feedback_status(enums.getKey());
			}
		}
		return "success";

	}

	@RequestMapping("/vipshop_test")
	public @ResponseBody String vipshoptest(HttpServletResponse response, HttpServletRequest request) {
		for (B2cEnum enums : B2cEnum.values()) {
			if (enums.getMethod().contains("vipshop")) {
				vipShopCwbFeedBackService.feedback_status(enums.getKey());
			}
		}
		return "";

	}

	@RequestMapping("/yixun_test")
	public @ResponseBody String yixuntest(HttpServletResponse response, HttpServletRequest request) {
		yixunService.feedback_status();
		return "执行了推送易迅记录失败信息的方法";

	}

}