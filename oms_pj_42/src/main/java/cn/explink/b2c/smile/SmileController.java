package cn.explink.b2c.smile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.explink.dao.BranchDAO;

@Controller
@RequestMapping("/smile")
public class SmileController {

	@Autowired
	SmileService smileService;
	@Autowired
	BranchDAO branchDAO;

}
