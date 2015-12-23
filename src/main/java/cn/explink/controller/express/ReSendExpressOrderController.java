package cn.explink.controller.express;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.enumutil.express.SendTpsExpressOrderResultEnum;
import cn.explink.service.express.ReSendExpressOrderService;
import cn.explink.util.Page;

@Controller
@RequestMapping("/reSendExpressOrderController")
public class ReSendExpressOrderController extends ExpressCommonController {
	@Autowired
	private ReSendExpressOrderService reSendExpressOrderService;

	@RequestMapping("/list/{page}")
	public String list(Model model, String beginTime, String endTime, String transNo, String opeFlag, @PathVariable(value = "page") long page) {
		Map<String, Object> map = this.reSendExpressOrderService.getTpsInterfaceInfo(beginTime, endTime, transNo, opeFlag, page, Page.ONE_PAGE_NUMBER);
		int count = (Integer) map.get("count");
		model.addAttribute("page_obj", new Page((Integer) map.get("count"), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		model.addAttribute("beginTime", beginTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("transNo", transNo);
		model.addAttribute("opeFlag", opeFlag);
		model.addAttribute("list", map.get("list"));
		model.addAttribute("count", count);
		model.addAttribute("ResultEnum", SendTpsExpressOrderResultEnum.getAllStatus());
		return "express/stationOperation/reSendExpressOrder";
	}

	@RequestMapping("/reSend")
	@ResponseBody
	public JSONObject reSend(HttpServletRequest request, String[] ids) {
		JSONObject obj = new JSONObject();
		List<String> success = new ArrayList<String>();
		List<String> failure = new ArrayList<String>();
		this.reSendExpressOrderService.reSendTps(ids, success, failure);
		obj.put("success", success.size() > 0 ? StringUtils.join(success.toArray(), ",") : "0单");
		obj.put("failure", failure.size() > 0 ? StringUtils.join(failure.toArray(), ",") : "0单");
		return obj;
	}
}
