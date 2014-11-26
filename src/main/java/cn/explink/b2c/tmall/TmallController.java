package cn.explink.b2c.tmall;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.BranchDAO;
import cn.explink.enumutil.BranchEnum;

@Controller
@RequestMapping("/tmall")
public class TmallController {
	private Logger logger = LoggerFactory.getLogger(TmallController.class);
	@Autowired
	TmallService tmallService;
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	JointService jointService;
	@Autowired
	TmallInsertCwbDetailTimmer tmallInsertCwbDetailTimmer;
	@Autowired
	BranchDAO branchDAO;

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		model.addAttribute("tmallObject", tmallService.getTmall(key));
		model.addAttribute("warehouselist", branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		model.addAttribute("joint_num", key);
		return "b2cdj/tmall";

	}

	@RequestMapping("/saveTmall/{id}")
	public @ResponseBody String tmallSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {

		if (request.getParameter("password") != null && "explink".equals(request.getParameter("password"))) {

			tmallService.edit(request, key);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
		// 保存

	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		tmallService.update(key, state);
		// 保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";

	}

	/**
	 * tmall请求接口 2012-10-25
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/interface")
	public @ResponseBody String requestByTmall(HttpServletRequest request, HttpServletResponse response) {

		try {
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/xml;charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			String partner = request.getParameter("partner");
			String signtype = request.getParameter("sign_type");
			String sign = request.getParameter("sign");
			String notify_id = request.getParameter("notify_id");
			String notify_type = request.getParameter("notify_type");
			String notify_time = request.getParameter("notify_time");
			String content = request.getParameter("content");

			String baseParams = "partner=" + partner + ",sign_type=" + signtype + ",sign=" + sign + ",notify_id=" + notify_id + ",notify_type=" + notify_type + ",notify_time=" + notify_time;
			logger.info("tmall请求参数：{},content={}", baseParams, content);

			Tmall tmall = getTmallEntityByPartner(partner);
			if (tmall == null) {

				return tmallService.response_XML("fail", "指定partner=" + partner + "不存在");
			}

			int isOpenFlag = jointService.getStateForJoint(tmall.getB2c_enum());
			if (isOpenFlag == 0) {
				return tmallService.response_XML("fail", "未开启tmall对接,type=" + tmall.getB2c_enum());
			}

			return tmallService.requestMethod(tmall, partner, signtype, sign, notify_id, notify_type, notify_time, content);

		} catch (Exception e) {
			logger.error("tmall接收订单数据发生未知异常！", e);
			return tmallService.response_XML("fail", "tmall接收订单数据发生未知异常");
		}
	}

	/**
	 * 根据partner 来判断 属于哪个配置
	 * 
	 * @param partner
	 * @return
	 */
	private Tmall getTmallEntityByPartner(String partner) {
		Tmall tmall = null;
		for (B2cEnum enums : B2cEnum.values()) { // 遍历唯品会enum，可能有多个枚举
			if (enums.getMethod().contains("tmall")) {
				tmall = tmallService.getTmall(enums.getKey());
				if (tmall != null && tmall.getPartner().equals(partner)) {
					return tmall;
				}
			}
		}
		return null;
	}

	/**
	 * tmall请求接口 2012-10-25
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/tmall_timmer")
	public @ResponseBody void ExcuteTimmerMethod_tmall(HttpServletRequest request, HttpServletResponse response) {
		tmallInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();
		logger.info("执行了tmall查询临时表的定时器!");
	}

}
