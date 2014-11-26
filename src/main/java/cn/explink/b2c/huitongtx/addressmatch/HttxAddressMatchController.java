package cn.explink.b2c.huitongtx.addressmatch;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.huitongtx.Huitongtx;
import cn.explink.b2c.huitongtx.HuitongtxService;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;

@Controller
@RequestMapping("/httxAddress")
public class HttxAddressMatchController {
	private Logger logger = LoggerFactory.getLogger(HttxAddressMatchController.class);
	@Autowired
	HttxAddressMatchService httxAddressMatchService;
	@Autowired
	HuitongtxService huitongtxService;
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;

	@Autowired
	BranchDAO branchDAO;
	@Autowired
	CustomerDAO customerDAO;

	/**
	 * 汇通天下 运单站点匹配(落地配)(G7外部系统)
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/match")
	public @ResponseBody String requestByTmall(HttpServletRequest request, HttpServletResponse response) {

		try {

			String method = request.getParameter("method");
			String timestamp = request.getParameter("timestamp");
			String app_key = request.getParameter("app_key");
			String sign = request.getParameter("sign");
			String data = request.getParameter("data");

			String baseParams = "app_key=" + app_key + ",sign=" + sign + ",sign=" + sign + ",timestamp=" + timestamp + ",method=" + method;
			logger.info("httxaddress请求参数：{},data={}", baseParams, data);

			Huitongtx httx = huitongtxService.getHuitongtx(B2cEnum.Huitongtx.getKey());

			if (httx.getIsopenaddress() == 0) {
				return httxAddressMatchService.responseMessage("1", "未开启httxadderss对接", null);
			}

			if (app_key == null || app_key.isEmpty() || sign == null || sign.isEmpty() || timestamp == null || timestamp.isEmpty() || method == null || method.isEmpty() || data == null
					|| data.isEmpty()) {
				return httxAddressMatchService.responseMessage("2", "基本参数不完整" + baseParams, null);
			}

			return httxAddressMatchService.invokeAddressmatch(httx, app_key, sign, timestamp, method, data);

		} catch (Exception e) {
			logger.error("汇通天下-接收站点匹配数据未知异常", e);
			return httxAddressMatchService.responseMessage("-1", "未知异常" + e.getMessage(), null);
		}
	}

}
