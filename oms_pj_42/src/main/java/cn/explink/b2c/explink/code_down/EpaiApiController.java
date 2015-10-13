package cn.explink.b2c.explink.code_down;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.explink.b2c.tools.B2CDataDAO;

/**
 * 接收订单状态通知url
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/epainotify")
public class EpaiApiController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	B2CDataDAO b2CDataDAO;

	@RequestMapping("/")
	public void explinkinterface(HttpServletRequest request) {
		String custid = "";
		try {
			custid = request.getParameter("custid");
			int success = request.getParameter("success").equals("T") ? 1 : 2;
			String msg = request.getParameter("msg");

			logger.info("上游订单状态异步通知请求参数custid={},success={},msg=" + msg, custid, success);

			b2CDataDAO.updateB2cIdSQLResponseStatus(Long.valueOf(custid), success, msg);
		} catch (Exception e) {
			logger.error("上游订单状态异步通知发生未知异常custid=" + custid, e);
		}

	}

}
