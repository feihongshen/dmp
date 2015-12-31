package cn.explink.pos.unionpay;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.tools.JiontDAO;
import cn.explink.domain.User;
import cn.explink.pos.tools.PosEnum;
import cn.explink.service.ExplinkUserDetail;

@Controller
@RequestMapping("/unionpay")
public class UnionPayController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	UnionPayService unionPayService;
	@Autowired
	JiontDAO jointDAO;
	@Autowired
	UnionPayService_SearchPos unionPayService_SearchPos;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable("id") int key, Model model) {
		UnionPay unionpay = unionPayService.getUnionPaySettingMethod(PosEnum.UnionPay.getKey());
		model.addAttribute("unionpay", unionpay);
		model.addAttribute("joint_num", key);
		return "jointmanage/unionpay/edit";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(@PathVariable("id") int joint_num, HttpServletRequest request) {
		String password = request.getParameter("password");
		if (password != null && "explink".equals(password)) {
			unionPayService.edit(request, joint_num);
			logger.info("operatorUser={},unionpay->save", getSessionUser().getUsername());
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":0,\"error\":\"密码错误！\"}";
		}
	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		unionPayService.update(key, state);
		logger.info("operatorUser={},unionpay->del", getSessionUser().getUsername());
		// 保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	@RequestMapping("/")
	public @ResponseBody String requestByUnion(HttpServletRequest request, HttpServletResponse response) {
		String returnCode = null;
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/xml;charset=UTF-8");
			UnionPay unionpay = unionPayService.getUnionPaySettingMethod(PosEnum.UnionPay.getKey());
			int isOpenFlag = jointDAO.getStateByJointKey(PosEnum.UnionPay.getKey());
			if (isOpenFlag == 0) {
				returnCode = "未开启UnionPay对接";
			} else {
				String Command = request.getParameter("Command"); // 请求类型
				String Data = request.getParameter("Data"); // 请求内容
				String Md5str = request.getParameter("Md5"); // 请求加密验证
				returnCode = unionPayService.AnalyzXMLByUnionPay(Command, Data, Md5str, unionpay);
			}

		} catch (Exception e) {
			logger.error("unionPay请求接口发生不可预知的异常", e);
			returnCode = "未知异常";
			e.printStackTrace();
		}

		return returnCode;
	}

	@RequestMapping("/searchpos")
	public @ResponseBody String searchpos(HttpServletRequest request, HttpServletResponse response) {
		String cwb = request.getParameter("cwb");
		unionPayService_SearchPos.cwbSearchtoPos(cwb);

		return "SUCCESS";
	}

}
