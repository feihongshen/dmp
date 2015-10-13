package cn.explink.b2c.Wholeline;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.tools.B2cEnum;

@Controller
@RequestMapping("/wholeLine")
public class WholeLineController {
	@Autowired
	WholeLineService wholeLineService;
	@Autowired
	WholeLineService_search wholeLineService_search;

	/**
	 * 轮偱order表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/getInfo")
	// 轮寻
	public @ResponseBody String getInfoWholeline(HttpServletRequest request) {
		wholeLineService.getInfoCall();
		return "查询已出库数据插入到轮询表完成";
	}

	/**
	 * webservice请求
	 * 
	 * @param request
	 *            方法名：queryWaybillRoute， 参数：comeCode[XA0300],waybillNos[订单号]
	 * @return
	 */
	@RequestMapping("/getOrder")
	public @ResponseBody String getOrderForWholeline(HttpServletRequest request) {
		int key = B2cEnum.wholeLine.getKey();
		WholeLine whole = wholeLineService.getWholeline(key);
		wholeLineService_search.searchQuanxianRoute(key, whole);
		return "手动执行请求全线快递完成";
	}

}
