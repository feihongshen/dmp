package cn.explink.pos.mobileapp_dcb;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.tools.JiontDAO;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.pos.tools.PosEnum;

/**
 * 新疆大晨报App手机端应用
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/mobiledcb")
public class MobiledcbController {
	private Logger logger = LoggerFactory.getLogger(MobiledcbController.class);
	@Autowired
	MobiledcbService mobiledcbService;
	@Autowired
	JiontDAO jointDAO;
	@Autowired
	MobiledcbService_SynUser mobiledcbService_SynUser;

	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable("id") int key, Model model) {
		model.addAttribute("mobilecodapplist", mobiledcbService.getMobiledcb(key));
		model.addAttribute("joint_num", key);
		return "jointmanage/mobileapp_dcb/edit";
	}

	@RequestMapping("/test")
	public String test() {
		return "jointmanage/mobileapp_dcb/mobiledcb_test";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(@PathVariable("id") int joint_num, HttpServletRequest request) {

		String password = request.getParameter("password");
		if (password != null && "explink".equals(password)) {
			mobiledcbService.edit(request, joint_num);

			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":0,\"error\":\"密码错误！\"}";
		}
	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		mobiledcbService.update(key, state);

		// 保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	/**
	 * 接收大晨报的反馈信息
	 */
	@RequestMapping("/feedback")
	public @ResponseBody String feedback(HttpServletRequest request) {
		try {
			int isOpenFlag = jointDAO.getStateByJointKey(PosEnum.MobileApp_dcb.getKey());
			if (isOpenFlag == 0) {
				return "未开启大晨报终端接口";
			}
			DeliveryDcb deparms = mobiledcbService.loadingDeliveryEtongParms(request);// 把请求参数转化为对象存储

			String parmsLog = "cwb=" + deparms.getCwb() + ",delivermobile=" + deparms.getDelivermobile() + ",username=" + deparms.getUsername() + ",podresultid=" + deparms.getPodresultid()
					+ ",backreasonid=" + deparms.getBackreasonid() + ",leavedreasonid=" + deparms.getLeavedreasonid() + ",receivedfeecash=" + deparms.getReceivedfeecash() + ",receivedfeepos="
					+ deparms.getReceivedfeepos() + ",operatedate=" + deparms.getOperatedate();

			logger.info("大晨报手机端反馈请求参数={}", parmsLog);

			ResponseJson respJson = mobiledcbService.dealwithmobileApp_feedback(deparms);

			String responseinfo = JacksonMapper.getInstance().writeValueAsString(respJson);

			logger.info("大晨报手机端返回信息cwb={},info={}", deparms.getCwb(), responseinfo);

			return responseinfo;

		} catch (Exception e) {
			logger.error("未知异常", e);
			return "未知异常" + e.getMessage();

		}

	}

	/**
	 * 接收新疆大晟报查询接口
	 */
	@RequestMapping("/cwbsearch")
	public @ResponseBody String cwbSearch(HttpServletRequest request) {
		try {
			int isOpenFlag = jointDAO.getStateByJointKey(PosEnum.MobileApp_dcb.getKey());
			if (isOpenFlag == 0) {
				return "未开启大晨报终端接口";
			}
			String cwb = request.getParameter("cwb");
			String username = request.getParameter("username");
			String delivermobile = request.getParameter("delivermobile");

			logger.info("大晨报手机【查询】请求参数cwb={},username={},delivermobile=" + delivermobile, cwb, username);

			ResponseJsonSearch respJson = mobiledcbService.dealwithmobileApp_cwbSearch(cwb, username, delivermobile);

			String responseinfo = JacksonMapper.getInstance().writeValueAsString(respJson);

			logger.info("大晨报手机【查询】返回信息cwb={},info={}", cwb, responseinfo);

			return responseinfo;

		} catch (Exception e) {
			logger.error("未知异常", e);
			return "未知异常" + e.getMessage();

		}

	}

	/**
	 * 接收大晨报的反馈信息
	 */
	@RequestMapping("/feedback_test")
	public String feedback_test(HttpServletRequest request) {
		try {
			String response = feedback(request);

			request.setAttribute("values", response);
			request.setAttribute("username", request.getParameter("username"));
			request.setAttribute("delivermobile", request.getParameter("delivermobile"));

		} catch (Exception e) {
			logger.error("未知异常", e);

		}
		return "jointmanage/mobileapp_dcb/mobiledcb_test";

	}

	@RequestMapping("/syn_user")
	public @ResponseBody String syn_user() {

		mobiledcbService_SynUser.UserInfo_synchronzed();
		return "配送员信息已同步至新疆大晨报手机App";
	}

}
