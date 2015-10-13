package cn.explink.b2c.maisike;

import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.maisike.feedback_json.RespStatus;
import cn.explink.b2c.tools.JacksonMapper;
import cn.explink.dao.BranchDAO;

/**
 * 迈思可
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/maisike")
public class MaisikeController {
	private Logger logger = LoggerFactory.getLogger(MaisikeController.class);

	@Autowired
	BranchDAO branchDAO;
	@Autowired
	MaisikeService_Feedback maisikeService_Feedback;
	@Autowired
	MaisikeService_Send2LvBranch maisikeService_Send2LvBranch;

	@RequestMapping("/send")
	public @ResponseBody String sendTwoLeavelBranch(HttpServletRequest request) throws Exception {
		maisikeService_Send2LvBranch.sendTwoLeavelBranch();
		return "手动调用承运商出库成功";
	}

	@RequestMapping("/feedback")
	public @ResponseBody String searchMaisike(HttpServletRequest request) throws Exception {
		RespStatus respStatus = new RespStatus();
		String fn = request.getParameter("fn");
		String appname = request.getParameter("appname");
		String apptime = request.getParameter("apptime");
		String appkey = request.getParameter("appkey");
		String appdata = request.getParameter("appdata");

		String repsonsedata = null;
		try {

			appdata = URLDecoder.decode(appdata, "UTF-8");
			logger.info("状态反馈-迈思可请求信息fn={},appname={},apptime={},appkey={],appdata={}", new Object[] { fn, appname, apptime, appkey, appdata });

			repsonsedata = maisikeService_Feedback.receivedOrderFeedback(fn, appname, apptime, appkey, appdata);
			logger.info("状态反馈-返回迈思可信息data={}", repsonsedata);
			return repsonsedata;

		} catch (Exception e) {
			respStatus.setCode(MaisikeExpEmum.CommitFailed.getErrCode());
			respStatus.setMsg(MaisikeExpEmum.CommitFailed.getErrMsg() + e.getMessage());
			String respdatas = JacksonMapper.getInstance().writeValueAsString(respStatus);
			logger.info("状态反馈-返回迈思可信息data={}", respdatas);
			return respdatas;
		}

	}

}
