package cn.explink.b2c.benlaishenghuo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.explink.b2c.tools.B2cEnum;

@Controller
@RequestMapping("/Ben_lai")
public class BenlaishenghuoController {
	@Autowired
	BenlaishenghuoService benlaishenghuoService;

	private Logger logger = LoggerFactory.getLogger(Benlaishenghuo.class);

	@RequestMapping("/hander")
	public @ResponseBody String Trace_Log() {
		benlaishenghuoService.feedback_status(B2cEnum.Benlaishenghuo.getKey());
		logger.info("[本来网]日志反馈接口——返回的xml={}");
		return "[本来网]日志反馈接口手动执行完毕";
	}

}
