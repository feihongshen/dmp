package cn.explink.pos.etong;

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
import cn.explink.pos.tools.PosEnum;

/**
 * 物流E通手机端应用
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/etong")
public class EtongController {
	private Logger logger = LoggerFactory.getLogger(EtongController.class);
	@Autowired
	EtongService etongService;
	@Autowired
	JiontDAO jointDAO;

	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable("id") int key, Model model) {
		model.addAttribute("mobilecodapplist", etongService.getEtong(key));
		model.addAttribute("joint_num", key);
		return "jointmanage/etong/edit";
	}

	@RequestMapping("/test")
	public String test() {
		return "jointmanage/etong/etong_test";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(@PathVariable("id") int joint_num, HttpServletRequest request) {

		String password = request.getParameter("password");
		if (password != null && "explink".equals(password)) {
			etongService.edit(request, joint_num);

			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":0,\"error\":\"密码错误！\"}";
		}
	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		etongService.update(key, state);

		// 保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	/**
	 * 接收物流E通的反馈信息
	 */
	@RequestMapping("/feedback")
	public @ResponseBody String feedback(HttpServletRequest request) {
		try {
			int isOpenFlag = jointDAO.getStateByJointKey(PosEnum.MobileEtong.getKey());
			if (isOpenFlag == 0) {
				logger.warn("未开启物流E通终端接口");
				return "未开启物流E通终端接口";
			}

			String cwb = request.getParameter("cwb");
			String delivermobile = request.getParameter("delivermobile");
			String username = request.getParameter("username");
			String podresultid = request.getParameter("podresultid") == null || request.getParameter("podresultid").isEmpty() ? "0" : request.getParameter("podresultid");
			String backreasonid = request.getParameter("backreasonid") == null ? "0" : request.getParameter("backreasonid");
			String leavedreasonid = request.getParameter("leavedreasonid") == null ? "0" : request.getParameter("leavedreasonid");
			String receivedfeecash = request.getParameter("receivedfeecash") == null || request.getParameter("receivedfeecash").isEmpty() ? "0" : request.getParameter("receivedfeecash");
			String receivedfeepos = request.getParameter("receivedfeepos") == null || request.getParameter("receivedfeepos").isEmpty() ? "0" : request.getParameter("receivedfeepos");
			String receivedfeecheque = request.getParameter("receivedfeecheque") == null || request.getParameter("receivedfeecheque").isEmpty() ? "0" : request.getParameter("receivedfeecheque");
			String remark = request.getParameter("remark") == null ? "" : request.getParameter("remark");
			String photofile = request.getParameter("photofile");// 签收图片文件
			String operatedate = request.getParameter("operatedate");// 反馈时间

			String parms = "cwb=" + cwb + ",delivermobile=" + delivermobile + ",username=" + username + ",podresultid=" + podresultid + ",backreasonid=" + backreasonid + ",leavedreasonid="
					+ leavedreasonid + ",receivedfeecash=" + receivedfeecash + ",receivedfeepos=" + receivedfeepos + ",operatedate=" + operatedate;

			logger.info("物流E通手机端反馈请求参数={}", parms);

			DeliveryEtong detong = etongService.loadingDeliveryEtongParms(cwb, username, delivermobile, podresultid, backreasonid, leavedreasonid, receivedfeecash, receivedfeepos, receivedfeecheque,
					remark, photofile, operatedate);
			String responseinfo = etongService.dealWithDeliveryInfoByEtong(detong);

			logger.info("物流E通手机端返回信息cwb={},info={}", cwb, responseinfo);

			return responseinfo;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("未知异常", e);
			return "未知异常" + e.getMessage();

		}

	}

}
