package cn.explink.b2c.liebo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/libo")
public class LiBoController {

	@Autowired
	LieBoService explinkCwbSearchService;

	@RequestMapping("/")
	public @ResponseBody String explinkinterface(HttpServletResponse response, HttpServletRequest request) {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String cwbs = request.getParameter("cwb");
		String companyname = request.getParameter("companyname");
		String sign = request.getParameter("sign");
		return explinkCwbSearchService.getCwbStatusInterface(cwbs, companyname, sign);

	}

}