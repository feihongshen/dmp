package cn.explink.b2c.jiuxian;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.tools.B2cEnum;

@Controller
@RequestMapping("/jiuxianwang")
public class JiuxianController {
	@Autowired
	JiuxianService jiuxianService;
	private Logger logger = LoggerFactory.getLogger(JiuxianWang.class);

	@RequestMapping("/ReceiveExpress")
	public @ResponseBody String ReceiveExpress() {
		long calcCount = jiuxianService.feedback_status(B2cEnum.Jiuxian.getKey());
		logger.info("酒仙网]日志反馈接口——执行记录数={}", calcCount);
		return "执行酒仙网记录数" + calcCount;
	}

}
