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
import cn.explink.dao.CwbALLStateControlDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.CwbALLStateControl;
import cn.explink.domain.User;
import cn.explink.enumutil.CwbStateEnum;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.service.UserService;

@Controller
@RequestMapping("/cwbAllStateControl")
public class CwbALLStateControlController {

	@Autowired
	UserService userService;
	@Autowired
	UserDAO userDAO;
	@Autowired
	CwbALLStateControlDAO cwbALLStateControlDAO;
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
		Map<String, List<CwbALLStateControl>> mapList = new HashMap<String, List<CwbALLStateControl>>();

		for (CwbStateEnum ce : CwbStateEnum.values()) {
			mapList.put(ce.getValue() + "List", cwbALLStateControlDAO.getCwbAllStateControlByWhere(ce.getValue()));
		}
		model.addAttribute("mapList", mapList);
		return "/cwballstatecontrol/list";
	}

	@RequestMapping("/edit/{cwbstate}")
	public String edit(Model model, @PathVariable("cwbstate") int cwbstate) {
		model.addAttribute("csList", cwbALLStateControlDAO.getCwbAllStateControlByWhere(cwbstate));
		model.addAttribute("cwbstate", cwbstate);
		return "/cwballstatecontrol/edit";
	}

	@RequestMapping("/save/{cwbstate}")
	public @ResponseBody String save(@PathVariable("cwbstate") int cwbstate, @RequestParam(value = "toflowtype", required = false, defaultValue = "") String[] toflowtypes) throws Exception {
		cwbALLStateControlDAO.delCwbAllStateControl(cwbstate);

		if (toflowtypes.length > 0) {
			StringBuffer buffer = new StringBuffer();
			for (String string : toflowtypes) {
				buffer.append(string).append(",");
			}
			buffer.deleteCharAt(buffer.length() - 1);

			for (String toflowtype : toflowtypes) {
				cwbALLStateControlDAO.creCwbAllStateControl(cwbstate, Integer.parseInt(toflowtype));
			}
		}
		logger.info("operatorUser={},订单状态流程设置->save", getSessionUser().getUsername());
		return "{\"errorCode\":0,\"error\":\"保存成功\"}";
	}

}