package cn.explink.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.CwbStateControlDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.CwbStateControl;
import cn.explink.domain.User;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.UserService;

@Controller
@RequestMapping("/cwbStateControl")
public class CwbStateControlController {

	@Autowired
	UserService userService;
	@Autowired
	UserDAO userDAO;
	@Autowired
	CwbStateControlDAO cwbStateControlDAO;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/list")
	public String list(Model model) {

		Map<String, List<CwbStateControl>> mapList = new HashMap<String, List<CwbStateControl>>();

		for (FlowOrderTypeEnum ft : FlowOrderTypeEnum.values()) {
			mapList.put(ft.getValue() + "List", cwbStateControlDAO.getCwbStateControlByWhere(ft.getValue()));
		}
		model.addAttribute("mapList", mapList);
		return "cwbstatecontrol/list";
	}

	@RequestMapping("/edit/{fromstate}")
	public String edit(Model model, @PathVariable("fromstate") int fromstate) {
		model.addAttribute("cscList", cwbStateControlDAO.getCwbStateControlByWhere(fromstate));
		model.addAttribute("fromstate", fromstate);
		return "cwbstatecontrol/edit";
	}

	@RequestMapping("/save/{fromstate}")
	public @ResponseBody String save(@PathVariable("fromstate") int fromstate, @RequestParam(value = "tostate", required = false, defaultValue = "") String[] tostates) throws Exception {

		cwbStateControlDAO.delCwbStateControl(fromstate);

		if (tostates.length > 0) {
			StringBuffer buffer = new StringBuffer();
			for (String string : tostates) {
				buffer.append(string).append(",");
			}
			buffer.deleteCharAt(buffer.length() - 1);

			for (String tostate : tostates) {
				cwbStateControlDAO.creCwbStateControl(fromstate, Integer.parseInt(tostate));
			}
		}

		logger.info("operatorUser={},订单流程设置->save", getSessionUser().getUsername());
		return "{\"errorCode\":0,\"error\":\"保存成功\"}";
	}

}