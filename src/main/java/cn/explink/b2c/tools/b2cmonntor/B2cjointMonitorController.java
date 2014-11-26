package cn.explink.b2c.tools.b2cmonntor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.explink.dao.CustomerDAO;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.Page;

@Controller
@RequestMapping("/b2cjointmonitor")
public class B2cjointMonitorController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	B2cJointMonitorDAO b2cJointMonitorDAO;
	@Autowired
	B2cJointMonitorService b2cJointMonitorService;
	@Autowired
	CustomerDAO customerDAO;

	@RequestMapping("/list/{page}")
	public String list(@PathVariable("page") long page,
			Model model,
			@RequestParam(value = "cwb", required = false, defaultValue = "") String cwb,
			@RequestParam(value = "customerid", required = false, defaultValue = "0") String customerid,
			@RequestParam(value = "flowordertype", required = false, defaultValue = "0") long flowordertype,// 状态
			@RequestParam(value = "starttime", required = false, defaultValue = "") String starttime, @RequestParam(value = "endtime", required = false, defaultValue = "") String endtime,
			@RequestParam(value = "send_b2c_flag", required = false, defaultValue = "3") long send_b2c_flag,
			@RequestParam(value = "hand_deal_flag", required = false, defaultValue = "0") long hand_deal_flag) {
		if (starttime.equals("")) {
			starttime = DateTimeUtil.getDateBefore(7);
		}
		if (endtime.equals("")) {
			endtime = DateTimeUtil.getNowTime();
		}
		model.addAttribute("customerlist", customerDAO.getAllCustomers());
		model.addAttribute("monitorlist", b2cJointMonitorDAO.selectB2cMonitorDataList(cwb, customerid, flowordertype, send_b2c_flag, hand_deal_flag, starttime, endtime, page));
		model.addAttribute("page_obj", new Page(b2cJointMonitorDAO.selectB2cMonitorDataCount(cwb, customerid, flowordertype, send_b2c_flag, hand_deal_flag, starttime, endtime), page,
				Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		saveModel(model, cwb, customerid, flowordertype, starttime, endtime, send_b2c_flag, hand_deal_flag);
		// 保存查询条件到request
		return "b2cdj/b2cmonitorlist";
	}

	private void saveModel(Model model, String cwb, String customerid, long flowordertype, String starttime, String endtime, long send_b2c_flag, long hand_deal_flag) {
		model.addAttribute("starttime", starttime);
		model.addAttribute("endtime", endtime);
		model.addAttribute("cwb", cwb);
		model.addAttribute("customerid", customerid);
		model.addAttribute("flowordertype", flowordertype);
		model.addAttribute("starttime", starttime);
		model.addAttribute("endtime", endtime);
		model.addAttribute("send_b2c_flag", send_b2c_flag);
		model.addAttribute("hand_deal_flag", hand_deal_flag);
	}

	@RequestMapping("/deal/{page}")
	public String deal(@PathVariable("page") long page,
			Model model,
			@RequestParam(value = "cwb", required = false, defaultValue = "") String cwb,
			@RequestParam(value = "customerid", required = false, defaultValue = "0") String customerid,
			@RequestParam(value = "flowordertype", required = false, defaultValue = "0") long flowordertype,// 状态
			@RequestParam(value = "starttime", required = false, defaultValue = "") String starttime, @RequestParam(value = "endtime", required = false, defaultValue = "") String endtime,
			@RequestParam(value = "send_b2c_flag", required = false, defaultValue = "3") long send_b2c_flag,
			@RequestParam(value = "hand_deal_flag", required = false, defaultValue = "0") long hand_deal_flag) {

		List<B2CMonitorData> b2cdatalist = b2cJointMonitorDAO.selectB2cIdsByB2cMonitorDataList(cwb, customerid, flowordertype, send_b2c_flag, hand_deal_flag, starttime, endtime);

		if (b2cdatalist == null || b2cdatalist.size() == 0) {
			model.addAttribute("msg", "没有要处理的数据!");
		} else {
			for (B2CMonitorData b2cMonitorData : b2cdatalist) {
				b2cJointMonitorDAO.updateB2cDataMonitorDeal(b2cMonitorData.getB2cid());
			}
			model.addAttribute("msg", "处理成功!");
		}
		return list(page, model, cwb, customerid, flowordertype, starttime, endtime, send_b2c_flag, hand_deal_flag);
	}

	@RequestMapping("/export")
	public void ExportB2cDataExptInfo(
			@RequestParam(value = "cwb", required = false, defaultValue = "") String cwb,
			@RequestParam(value = "customerid", required = false, defaultValue = "0") String customerid,
			@RequestParam(value = "flowordertype", required = false, defaultValue = "0") long flowordertype,// 状态
			@RequestParam(value = "starttime", required = false, defaultValue = "") String starttime, @RequestParam(value = "endtime", required = false, defaultValue = "") String endtime,
			@RequestParam(value = "send_b2c_flag", required = false, defaultValue = "3") long send_b2c_flag,
			@RequestParam(value = "hand_deal_flag", required = false, defaultValue = "0") long hand_deal_flag, HttpServletResponse response) {

		b2cJointMonitorService.exportB2cDataExptInfo(cwb, customerid, flowordertype, starttime, endtime, send_b2c_flag, hand_deal_flag, response);
	}
}
