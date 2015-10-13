package cn.explink.b2c.liantong;

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
@RequestMapping("/liantong")
public class LiantongController {
	private Logger logger = LoggerFactory.getLogger(LiantongController.class);
	@Autowired
	LiantongService liantongService;
	@Autowired
	B2cDatasearchDAO b2cDatasearchDAO;
	@Autowired
	private B2cTools b2ctools;

	/**
	 * 提供口查询
	 */
	@RequestMapping("/")
	public @ResponseBody String requestCwbSearch(HttpServletRequest request, HttpServletResponse response) {
		try {

			response.setCharacterEncoding("UTF-8");

			if (!b2ctools.isB2cOpen(B2cEnum.Liantong.getKey())) {
				return "未开启[联通]查询接口";
			}
			Liantong lt = liantongService.getLiantong(B2cEnum.Liantong.getKey());

			String QueryMode = request.getParameter("QueryMode"); // 查询类别
			String QueryNo = request.getParameter("QueryNo"); // 单号 (根据查询类别来订)
			String appkey = request.getParameter("appkey"); // 加密后信息

			logger.info("[联通商城]请求跟踪信息QueryNo={},QueryMode={},appkey=" + appkey, QueryNo, QueryMode);

			String responseXML = liantongService.requestCwbSearchInterface(lt, QueryMode, QueryNo, appkey);
			logger.info("返回联通信息={}", responseXML);

			return responseXML;
		} catch (Exception e) {
			logger.error("联通处理业务逻辑异常！", e);
			return "处理业务逻辑异常";
		}
	}

}
