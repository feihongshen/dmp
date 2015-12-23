package cn.explink.controller.express;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.explink.domain.User;
import cn.explink.service.express.ExpressQuickQueryService;
import cn.explink.util.Tools;

@Controller
@RequestMapping("/preOrderSelect")
public class ExpressPreOrderSelectController extends ExpressCommonController{
	@Autowired
	private ExpressQuickQueryService expressQuickQueryService;
	
	//快速查询预订单信息
	@RequestMapping("/queckSelectPreOrder/{preOrder}")
	public String queckSelectPreOrder(Model model, @PathVariable(value = "preOrder") String preOrder) {
		model.addAttribute("preOrder", preOrder);
		if (!Tools.isEmpty(preOrder)) {
			return "/express/quickQuery/queckSelectPreOrder";
		}else{
			return "{\"errorCode\":0,\"error\":\"查询失败\"}";
		}
	}
	/**
	 * 查询预订单信息
	 * @param model
	 * @param preOrderNo
	 * @return
	 */
	@RequestMapping("/queckSelectPreOrderLeft/{preOrderNo}")
	public String queckSelectOrderleft(Model model, @PathVariable(value = "preOrderNo") String preOrderNo) {
		User user = getSessionUser();
		Map<String, Object> viewMap = expressQuickQueryService.transferPreOrder2View(preOrderNo,user);
		model.addAttribute("view", viewMap.get("view"));
		return "/express/quickQuery/queckSelectPreOrderLeft";
	}
}
