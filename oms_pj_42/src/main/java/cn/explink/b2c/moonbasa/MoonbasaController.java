package cn.explink.b2c.moonbasa;

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
@RequestMapping("/moonbasa")
public class MoonbasaController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	B2cDatasearchDAO b2cDatasearchDAO;
	@Autowired
	private B2cTools b2ctools;
	@Autowired
	MoonbasaService moonbasaService;

	/**
	 * 提供口查询
	 */
	@RequestMapping("/search")
	public @ResponseBody String requestCwbSearch(HttpServletRequest request, HttpServletResponse response) {
		try {

			response.setCharacterEncoding("UTF-8");

			if (!b2ctools.isB2cOpen(B2cEnum.Moonbasa.getKey())) {
				logger.info("未开启0梦芭莎0查询接口");
				return "-1";
			}

			Moonbasa mbs = moonbasaService.getMoonbasa(B2cEnum.Moonbasa.getKey());

			String custcode = request.getParameter("CustCode");
			String pwd = request.getParameter("Pwd");
			String from = request.getParameter("From");
			String to = request.getParameter("To");

			logger.info("梦芭莎请求信息CustCode={},Pwd={},from=" + from + ",to=" + to, custcode, pwd);
			if (!mbs.getCustcode().equals(custcode) || !mbs.getPwd().equals(pwd)) {
				return "-1";
			}

			String responseXML = moonbasaService.requestCwbSearchInterface(mbs, from, to);
			logger.info("返回梦芭莎信息={}", responseXML);

			return responseXML;
		} catch (Exception e) {
			logger.error("梦芭莎处理业务逻辑异常！", e);
			return "处理业务逻辑异常";
		}
	}

}
