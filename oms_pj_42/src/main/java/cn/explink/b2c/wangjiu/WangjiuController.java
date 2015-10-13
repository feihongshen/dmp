package cn.explink.b2c.wangjiu;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.cwbsearch.B2cDatasearchDAO;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.B2cTools;

@Controller
@RequestMapping("/wangjiu")
public class WangjiuController {
	private Logger logger = LoggerFactory.getLogger(WangjiuController.class);

	@Autowired
	B2cDatasearchDAO b2cDatasearchDAO;
	@Autowired
	private B2cTools b2ctools;
	@Autowired
	WangjiuService wangjiuService;

	/**
	 * 提供口查询
	 */
	@RequestMapping("/search")
	public @ResponseBody String requestCwbSearch(HttpServletRequest request, HttpServletResponse response) {
		try {

			response.setCharacterEncoding("UTF-8");

			if (!b2ctools.isB2cOpen(B2cEnum.Wangjiu.getKey())) {
				return "未开启[网酒网]查询接口";
			}
			Wangjiu wj = wangjiuService.getWangjiu(B2cEnum.Wangjiu.getKey());

			String logistics_interface = request.getParameter("logistics_interface"); // xml字符串

			String data_digest = request.getParameter("data_digest"); // 签名字符串

			logger.info("网酒网跟踪查询请求信息logistics_interface={},data_digest={}", logistics_interface, data_digest);

			String responseXML = wangjiuService.requestCwbSearchInterface(logistics_interface, data_digest, wj);
			logger.info("返回网酒网信息={}", responseXML);

			return responseXML;
		} catch (Exception e) {
			logger.error("网酒网处理业务逻辑异常！", e);
			return "处理业务逻辑异常";
		}
	}

	@RequestMapping("/feedback")
	public @ResponseBody String feedback(HttpServletRequest request, HttpServletResponse response) {
		wangjiuService.feedback_status();
		return "SUCEESS";
	}

}
