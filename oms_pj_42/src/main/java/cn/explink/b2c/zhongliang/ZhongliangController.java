package cn.explink.b2c.zhongliang;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/zhongliang")
public class ZhongliangController {
	@Autowired
	ZhongliangService zhongliangService;

	@RequestMapping("/orderFlow")
	public @ResponseBody String timmer() {
		zhongliangService.feedback_status();
		return "状态回转";
	}

}
