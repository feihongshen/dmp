package cn.explink.b2c.zhts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.tools.JointService;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.OrderTrackDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.orderflow.OrderNote;
import cn.explink.service.CwbTranslator;

@Controller
@RequestMapping("/zhts")
public class ZhtsController {
	private Logger logger = LoggerFactory.getLogger(ZhtsController.class);
	@Autowired
	ZhtsService zhtsService;

	@Autowired
	BranchDAO branchDAO;
	@Autowired
	JointService jointService;
	@Autowired
	ZhtsAutoOptService  zhtsAutoOptService;
	@Autowired
	List<CwbTranslator> cwbTranslators;
	@Autowired
	OrderTrackDAO orderTrackDAO;
	@Autowired
	CwbDAO cwbDAO;
	

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {

		model.addAttribute("zhtsObject", zhtsService.getZhts(key));
		model.addAttribute("joint_num", key);
		return "b2cdj/zhts";

	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String dangdangSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {

		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {

			zhtsService.edit(request, key);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
		// 保存

	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		zhtsService.update(key, state);
		// 保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	@RequestMapping("/orderTrack")
	public @ResponseBody String receiver(HttpServletRequest request) {
		String userCode = request.getParameter("userCode");
		String sign = request.getParameter("sign");
		String requestTime = request.getParameter("requestTime");
		String content = request.getParameter("content");
		
		logger.info("中浩接收订单轨迹参数userCode={},sign={},requestTime={},content={}",new Object[]{userCode,sign,requestTime,content});

		String responseMsg = zhtsService.receivedOrderTrack(userCode, sign, requestTime, content);
		
		logger.info("中浩途胜返回接口信息={}",responseMsg);
		
		return responseMsg;
	}
	
	@RequestMapping("/auto")
	public @ResponseBody String auto() {
		zhtsAutoOptService.autoOperatorInputOut();
		zhtsAutoOptService.autoOperatorCarrierConfirm();
		// 保存
		return "{\"errorCode\":0,\"error\":\"执行手机入库、出库、承运商确认出库 成功\"}";
	}

	
	
	
	
	@RequestMapping("/queryOrderTrack")
	public String queryOrderTrack(Model model, @RequestParam(value = "cwb", required = false, defaultValue = "") String cwbs,
			@RequestParam(value = "validateCode", required = false, defaultValue = "") String validateCode, HttpServletResponse response, HttpServletRequest request) {

		if (validateCode.length() > 0) {
			if (!this.checkYValidateCode(validateCode, request)) {
				String remand = "验证码错误！";
				model.addAttribute("remand", remand);
				
			} else {
				List<JSONObject> viewList = new ArrayList<JSONObject>();
				List<Map<String, List<OrderNote>>> orderflowviewlist = new ArrayList<Map<String, List<OrderNote>>>();
				for (String cwb : cwbs.split("\r\n")) {
					
					if (cwb.trim().length() == 0) {
						continue;
					}
					if (cwb.replace(" ", "").isEmpty()) {
						continue;
					}
					cwb = this.translateCwb(cwb);
					CwbOrder cwbOrder = cwbDAO.getCwborder(cwb);
					if(cwbOrder==null){
						continue;
					}
					
					
					List<OrderNote> orerTrackList= orderTrackDAO.getTranscwbOrderFlowByScanCwb(null, cwb);
					Map<String, List<OrderNote>> mapParams=new HashMap<String, List<OrderNote>>();
					mapParams.put(cwb, orerTrackList);
					orderflowviewlist.add(mapParams);
					
				}
				model.addAttribute("viewList", viewList);
				model.addAttribute("orderflowviewlist", orderflowviewlist);
				
			}
		}

		return "/orderflow/orderTrackQueryforZhts";
	}
	
	private boolean checkYValidateCode(String validateCode, HttpServletRequest request) {
		if (request.getSession().getAttribute("validateYCode") != null) {
			if (request.getSession().getAttribute("validateYCode").equals(validateCode)) {
				return true;
			}
		}
		return false;
	}
	
	public String translateCwb(String cwb) {
		for (CwbTranslator cwbTranslator : this.cwbTranslators) {
			String translateCwb = cwbTranslator.translate(cwb);
			if (StringUtils.hasLength(translateCwb)) {
				cwb = translateCwb;
			}
		}
		return cwb;
	}
}
