package cn.explink.b2c.liantongordercenter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JointService;
import cn.explink.dao.BranchDAO;
import cn.explink.enumutil.BranchEnum;

/**
 * 联通商城controller
 *
 * @author zhaoshb
 * @since AR1.0
 */
@Controller
@RequestMapping("/liantongOrderCenter")
public class LiantongOrderCenterController {
	// 日志
	private Logger logger = LoggerFactory.getLogger(LiantongOrderCenterController.class);
	// 联通商城service
	@Autowired
	LianTongOrderCenterService lianTongOrderCenterService;
	// 站点Servie
	@Autowired
	BranchDAO branchDAO;
	// 对接设置Servie
	@Autowired
	JointService jointService;
	@Autowired
	LianTongOrderCenterInsertCwbDetailTimmer liantongSCInsertCwbDetailTimmer;

	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {

		// 获取联通商城对象
		model.addAttribute("liantongObject", this.lianTongOrderCenterService.getLianTong(key));
		// 站点类型是库房的集合
		model.addAttribute("warehouselist", this.branchDAO.getBranchBySiteType(BranchEnum.KuFang.getValue()));
		// 枚举key
		model.addAttribute("joint_num", key);
		return "b2cdj/liantongordercenter";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String dangdangSave(Model model, @PathVariable("id") int key, HttpServletRequest request) {
		// 密码不能为空，并且密码为explin
		if ((request.getParameter("password") != null) && "explink".equals(request.getParameter("password"))) {
			// 保存
			this.lianTongOrderCenterService.edit(request, key);
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		} else {
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
		// 保存

	}

	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key,
			@PathVariable("state") int state) {
		this.lianTongOrderCenterService.update(key, state);
		// 保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	/**
	 * 接收 联通商城推送数据
	 *
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/received")
	public @ResponseBody String receivedLiantongOrders(HttpServletRequest request) throws IOException {
		// String content = new
		// String(request.getParameter("xml").getBytes("ISO-8859-1"),
		// "UTF-8");// xml参数
		String content = request.getParameter("xml");
		String verifyCode = request.getParameter("verifyCode");// sign
		this.logger.info("联通商城推送订单数据={}", content);
		if (!StringUtils.hasText(content) || !StringUtils.hasText(verifyCode)) {
			String response = "<Response service=\"OrderService\"><Head>ERR</Head><Error>" + "系统参数为空"
					+ "</Error></Response>";
			return response;
		}
		// 判断是否开启了电商对接
		int isOpenFlag = this.jointService.getStateForJoint(B2cEnum.LianTongOrderCenter.getKey());
		if (isOpenFlag == 0) {
			return "未开启联通数据同步接口，请联系易普联科";
		}
		// 订单处理并返回电商信息
		String repsonse = this.lianTongOrderCenterService.invokeDealWithCwbOrderImport(content, verifyCode);
		this.logger.info("当前返回联通：" + repsonse);
		return repsonse;
	}

	/**
	 *
	 * 临时表传到主表
	 *
	 * @return
	 */
	@RequestMapping("/timmer")
	public @ResponseBody String excuteTimmer() {
		this.liantongSCInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail();
		return "SUCCESS";
	}

	/**
	 * 接收 取消订单信息（未入库前 可取消）
	 *
	 * @return
	 * @throws IOException
	 * @throws IOException
	 */
	@RequestMapping(value = "/cancel")
	public @ResponseBody String cancelLiantongOrders(HttpServletRequest request) throws IOException {
		String content = new String(request.getParameter("xml").getBytes("ISO-8859-1"), "UTF-8");// 参数
		String verifyCode = request.getParameter("verifyCode");// 签名
		this.logger.info("联通商城订单取消参数，content={}", content);
		if (!StringUtils.hasText(content)) {
			String response = "<Response service=\"OrderConfirmService\"><Head>ERR</Head><Error>参数错误</Error></Response>";
			return response;
		}
		int isOpenFlag = this.jointService.getStateForJoint(B2cEnum.LianTongOrderCenter.getKey());
		if (isOpenFlag == 0) {
			return "未开启联通订单取消接口，请联系易普联科";
		}

		String response = this.lianTongOrderCenterService.cancelCwbOrder(content, verifyCode);

		this.logger.info("当前返回联通商城信息：" + response);

		return response;
	}
}
