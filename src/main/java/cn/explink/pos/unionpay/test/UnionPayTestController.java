package cn.explink.pos.unionpay.test;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.explink.b2c.tools.ExptReason;
import cn.explink.b2c.tools.ExptReasonDAO;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.pos.tools.PosEnum;

@Controller
@RequestMapping("/unionpay_test")
public class UnionPayTestController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	UnionPayTestService unionPayTestService;
	@Autowired
	JiontDAO jointDAO;
	@Autowired
	ExptReasonDAO exptReasonDAO;

	@RequestMapping("/")
	public String enterPage(HttpServletRequest request, HttpServletResponse response, Model model) {
		List<ExptReason> exptlist = exptReasonDAO.getExptReasonListByPosExpt(PosEnum.UnionPay.getKey());
		model.addAttribute("exptlist", exptlist);
		return "jointmanage/unionpay/unionpay_test";
	}

	@RequestMapping("/request")
	public String requestInterface(HttpServletRequest request, HttpServletResponse response, Model model, @RequestParam(value = "command", required = false, defaultValue = "") String command,
			@RequestParam(value = "username", required = false, defaultValue = "") String username, @RequestParam(value = "password", required = false, defaultValue = "") String password,
			@RequestParam(value = "cwb", required = false, defaultValue = "") String cwb, @RequestParam(value = "payamount", required = false, defaultValue = "0") String payamount,
			@RequestParam(value = "sign_type", required = false, defaultValue = "0") String sign_type, @RequestParam(value = "signname", required = false, defaultValue = "") String signname,
			@RequestParam(value = "pay_type", required = false, defaultValue = "") String pay_type, @RequestParam(value = "expt_code", required = false, defaultValue = "") String expt_code) {

		String values = "";
		try {
			values = unionPayTestService.requestUnionPayInterface_test(command, username, password, cwb, payamount, sign_type, signname, pay_type, expt_code, model);
		} catch (Exception e) {
			logger.error("", e);
		}

		model.addAttribute("values", values);
		List<ExptReason> exptlist = exptReasonDAO.getExptReasonListByPosExpt(PosEnum.UnionPay.getKey());
		model.addAttribute("exptlist", exptlist);
		return "jointmanage/unionpay/unionpay_test";
	}
}
