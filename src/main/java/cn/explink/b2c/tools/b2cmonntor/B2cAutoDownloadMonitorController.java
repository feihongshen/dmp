package cn.explink.b2c.tools.b2cmonntor;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.explink.b2c.explink.core_down.EpaiApi;
import cn.explink.b2c.explink.core_down.EpaiApiDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.Page;

@Controller
@RequestMapping("/b2cdownloadmonitor")
public class B2cAutoDownloadMonitorController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	B2cAutoDownloadMonitorDAO b2cAutoDownloadMonitorDAO;
	@Autowired
	B2cJointMonitorService b2cJointMonitorService;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	EpaiApiDAO epaiApiDAO;

	@RequestMapping("/list/{page}")
	public String list(@PathVariable("page") long page, Model model, @RequestParam(value = "customerid", required = false, defaultValue = "") String customerid,
			@RequestParam(value = "starttime", required = false, defaultValue = "") String starttime, @RequestParam(value = "endtime", required = false, defaultValue = "") String endtime) {
		if (starttime.equals("")) {
			starttime = DateTimeUtil.getDateBefore(7);
		}
		if (endtime.equals("")) {
			endtime = DateTimeUtil.getNowTime();
		}
		model.addAttribute("customerlist", this.customerDAO.getAllCustomers());
		model.addAttribute("downloadlist", this.b2cAutoDownloadMonitorDAO.selectB2cMonitorDataList(customerid, starttime, endtime, page));

		model.addAttribute("page_obj", new Page(this.b2cAutoDownloadMonitorDAO.selectB2cMonitorDataCount(customerid, starttime, endtime), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);

		List<EpaiApi> epailist = this.epaiApiDAO.getEpaiApiList();
		model.addAttribute("epailist", epailist);

		this.saveModel(model, customerid, starttime, endtime);

		// 保存查询条件到request
		return "b2cdj/autodownloadmonitorlist";
	}

	private void saveModel(Model model, String customerid, String starttime, String endtime) {
		model.addAttribute("starttime", starttime);
		model.addAttribute("endtime", endtime);
		model.addAttribute("customerid", customerid);
	}

	@RequestMapping("/export")
	public void ExportB2cDataExptInfo(
			@RequestParam(value = "cwb", required = false, defaultValue = "") String cwb,
			@RequestParam(value = "customerid", required = false, defaultValue = "0") String customerid,
			@RequestParam(value = "flowordertype", required = false, defaultValue = "0") long flowordertype,// 状态
			@RequestParam(value = "starttime", required = false, defaultValue = "") String starttime, @RequestParam(value = "endtime", required = false, defaultValue = "") String endtime,
			@RequestParam(value = "send_b2c_flag", required = false, defaultValue = "3") long send_b2c_flag,
			@RequestParam(value = "hand_deal_flag", required = false, defaultValue = "0") long hand_deal_flag, HttpServletResponse response) {

		this.b2cJointMonitorService.exportB2cDataExptInfo(cwb, customerid, flowordertype, starttime, endtime, send_b2c_flag, hand_deal_flag, response);
	}
}
