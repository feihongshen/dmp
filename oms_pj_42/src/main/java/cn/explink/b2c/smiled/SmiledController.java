package cn.explink.b2c.smiled;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.util.Dom4jParseUtil;

@Controller
@RequestMapping("/smiled")
public class SmiledController {
	private Logger logger = LoggerFactory.getLogger(SmiledController.class);

	@Autowired
	SmiledService_Feedback smiledService_Feedback;

	@RequestMapping("/feedback")
	public @ResponseBody String feedback(HttpServletRequest request) throws IOException {
		InputStream input = new BufferedInputStream(request.getInputStream());
		String requestXml = Dom4jParseUtil.getStringByInputStream(input); // 读取文件流，获得xml字符串

		smiledService_Feedback.receivedOrderFeedback(requestXml);

		return null;
	}

}
