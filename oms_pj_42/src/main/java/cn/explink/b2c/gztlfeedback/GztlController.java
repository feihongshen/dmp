package cn.explink.b2c.gztlfeedback;

import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.maisike.MaisikeController;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.util.MD5.MD5Util;

@Controller
@RequestMapping("/gztlfeedback")
public class GztlController {
	@Autowired
	GztlServiceFeedback gztlServiceFeedback;
	@Autowired
	B2cTools b2cTools;
	private Logger logger = LoggerFactory.getLogger(MaisikeController.class);

	// @ResponseBody
	@RequestMapping("/send")
	public @ResponseBody String sendTwoLeavelBranch(HttpServletRequest request) throws Exception {
		this.gztlServiceFeedback.sendTwoLeavelBranch();
		return "手动调用承运商出库成功";
	}

	/**
	 * 接收广州通路外发单信息
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/feedback")
	public @ResponseBody String searchGztl(HttpServletRequest request) throws Exception {
		String xml = null;
		try {
			int key = B2cEnum.GuangzhoutongluWaifadan.getKey();
			boolean isOpenFlag = this.b2cTools.isB2cOpen(key);
			if (!isOpenFlag) {
				return "未开启0广州通路外发0对接接口";
				// this.errorReturnData("F", "未开启0广州通路外发0对接接口")
			}
			GztlFeedback gztl = this.gztlServiceFeedback.getGztlFeedback(key);
			xml = request.getParameter("XML");
//			System.out.println(xml);
			String MD5 = request.getParameter("MD5");
			xml = URLDecoder.decode(xml, "UTF-8");
			System.out.println(xml);
			this.logger.info("外发单订单反馈推送接口推送参数xml={},MD5={}", xml, MD5);
			String localSignString = MD5Util.md5(xml + gztl.getPrivate_key());
			System.out.println(localSignString);

			if (!MD5.equalsIgnoreCase(localSignString)) {
				this.logger.info("签名验证失败,xml={},MD5={}", xml, MD5);
				return this.gztlServiceFeedback.errorReturnData("F", "签名验证失败" );

			}

			String responseString = this.gztlServiceFeedback.receivedOrderFeedback(xml, gztl);
			return responseString;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			this.logger.error("0广州通路外发单处理业务逻辑异常（可能xml为空）！" + xml, e);
			return this.gztlServiceFeedback.errorReturnData("F", "业务处理异常" );

		}
	}

	@RequestMapping("/testinsert")
	public String testFeedbackInsert() {
		return "test11111111111111111";
	}
}
