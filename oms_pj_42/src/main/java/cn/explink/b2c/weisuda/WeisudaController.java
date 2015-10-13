package cn.explink.b2c.weisuda;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.cwbsearch.B2cDatasearchDAO;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.enumutil.pos.PosEnum;

@Controller
@RequestMapping("/weisuda")
public class WeisudaController {
	private Logger logger = LoggerFactory.getLogger(WeisudaController.class);

	@Autowired
	B2cDatasearchDAO b2cDatasearchDAO;
	@Autowired
	private B2cTools b2ctools;
	@Autowired
	WeisudaService weisudaService;

	/**
	 * 提供口查询
	 */
	@RequestMapping("/select")
	public @ResponseBody
	String requestCwbSearch(HttpServletRequest request, HttpServletResponse response) {

		response.setCharacterEncoding("UTF-8");

		if (!this.b2ctools.isB2cOpen(PosEnum.Weisuda.getKey())) {
			this.logger.info("未开启[唯速达]查询接口");
			return "未开启[唯速达]查询接口";
		}
		this.logger.info("进入唯速达_01数据对接Controller");
		this.weisudaService.selectWeisudaCwb();

		return "";

	}

	@RequestMapping("/update")
	public @ResponseBody
	String getUnVerifyOrders(HttpServletRequest request, HttpServletResponse response) {

		response.setCharacterEncoding("UTF-8");

		if (!this.b2ctools.isB2cOpen(PosEnum.Weisuda.getKey())) {
			this.logger.info("未开启[唯速达]查询接口");
			return "未开启[唯速达]查询接口";
		}
		this.weisudaService.getUnVerifyOrders();

		return "";

	}

}
