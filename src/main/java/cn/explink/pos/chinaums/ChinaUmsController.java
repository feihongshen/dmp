package cn.explink.pos.chinaums;

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
import cn.explink.pos.chinaums.xml.Transaction;
import cn.explink.pos.chinaums.xml.Transaction_Header;
import cn.explink.pos.tools.PosEnum;
import cn.explink.service.ExplinkUserDetail;
import cn.explink.util.MD5.MD5Util;

@Controller
@RequestMapping("/chinaums")
public class ChinaUmsController {
	private Logger logger = LoggerFactory.getLogger(ChinaUmsController.class);
	@Autowired
	ChinaUmsService chinaUmsService;
	@Autowired
	JiontDAO jointDAO;
	@Autowired
	SecurityContextHolderStrategy securityContextHolderStrategy;
	@Autowired
	ChinaUmsService_public chinaUmsService_public;

	private User getSessionUser() {
		ExplinkUserDetail userDetail = (ExplinkUserDetail) securityContextHolderStrategy.getContext().getAuthentication().getPrincipal();
		return userDetail.getUser();
	}

	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable("id") int key, Model model) {
		model.addAttribute("chinaums", chinaUmsService.getChinaUmsSettingMethod(key));
		model.addAttribute("joint_num", key);
		return "jointmanage/chinaums/edit";

	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(@PathVariable("id") int joint_num, HttpServletRequest request) {

		String password = request.getParameter("password");
		if (password != null && "explink".equals(password)) {
			chinaUmsService.edit(request, joint_num);
			logger.info("operatorUser={},chinaums->save", getSessionUser().getUsername());
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":0,\"error\":\"密码错误！\"}";
		}
	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		chinaUmsService.update(key, state);
		logger.info("operatorUser={},chinaums->del", getSessionUser().getUsername());
		// 保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";

	}

	/**
	 * 北京银联商务请求入口
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/")
	public @ResponseBody String requestBychinaums(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/xml;charset=UTF-8");
			String XMLDOC = request.getParameter("context");
			logger.info("北京银联商务对接:有信息请求------{}", XMLDOC);
			ChinaUms chinaUms = chinaUmsService.getChinaUmsSettingMethod(PosEnum.ChinaUms.getKey()); // 获取配置信息
			int isOpenFlag = jointDAO.getStateByJointKey(PosEnum.ChinaUms.getKey());
			if (isOpenFlag == 0) {
				logger.error("未开启北京银联商务对接");
				Transaction rootnote = new Transaction();
				Transaction_Header header=new Transaction_Header();
				header.setResponse_code("50");
				header.setResponse_msg("未开启pos对接！");
				rootnote.setTransaction_Header(header);
				return chinaUmsService_public.createXML_toExptFeedBack(chinaUms, rootnote);
			}
			logger.info("北京银联商务对接:请 的消息:{}", XMLDOC);
			return chinaUmsService.AnalyzXMLByChinaUms(chinaUms, XMLDOC);
		} catch (Exception e) {
			logger.error("ChinaUmsController遇见不可预知的错误！" + e);
			ChinaUms chinaUms = chinaUmsService.getChinaUmsSettingMethod(PosEnum.ChinaUms.getKey()); // 获取配置信息
			Transaction rootnote = new Transaction();
			rootnote.getTransaction_Header().setResponse_code("40");
			rootnote.getTransaction_Header().setResponse_msg("系统异常！");
			return chinaUmsService_public.createXML_toExptFeedBack(chinaUms, rootnote);
		}

	}

	public static void main(String[] args) {
		String str = "newlandsexplink";
		String mac = MD5Util.md5(str);
		System.out.println(mac);
	}
}
