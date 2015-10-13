package cn.explink.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.explink.dao.BranchDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.DeliveryDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.Customer;
import cn.explink.enumutil.BranchEnum;
import cn.explink.service.DeliveryService;
import cn.explink.util.DateDayUtil;

@Controller
@RequestMapping("/delivery")
public class DeliveryController {

	@Autowired
	DeliveryDAO deliveryDao;
	@Autowired
	BranchDAO branchDao;
	@Autowired
	CustomerDAO customerDao;
	@Autowired
	GetDmpDAO getDmpDAO;
	@Autowired
	DeliveryService deliveryService;

	@RequestMapping("/select")
	public String list(Model model, @RequestParam(value = "datetype", required = false, defaultValue = "1") long dateType,
			@RequestParam(value = "stratedate", required = false, defaultValue = "") String stratedate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate,
			@RequestParam(value = "controlStr", required = false, defaultValue = "") String controlStr, @RequestParam(value = "branchid", required = false, defaultValue = "-1") long branchid,
			@RequestParam(value = "timeLong", required = false, defaultValue = "24") long timeLong, @RequestParam(value = "flowordertype", required = false, defaultValue = "1") long flowordertype,
			HttpServletResponse response, HttpServletRequest request) {

		// 查询当前选择的时间段内 所有的批次号 ""
		List<EmaildateTDO> emailList = deliveryService.getEmaildateAndBrandidList(stratedate, enddate, branchid);
		// 查询所有的站点
		List<Branch> branchnameList = getDmpDAO.getBranchByZhanDian();
		// 查询当前站点
		String dmpid = request.getSession().getAttribute("dmpid") == null ? "" : request.getSession().getAttribute("dmpid").toString();
		long branchidPram = branchid;
		long branchidSession = getDmpDAO.getNowBrancheId(dmpid);
		Branch nowbranch = getDmpDAO.getNowBranch(branchidSession);
		if (nowbranch != null) {
			if (nowbranch.getSitetype() == BranchEnum.ZhanDian.getValue()) {
				model.addAttribute("nowBranch", nowbranch);
				branchidPram = branchidSession;
			}
		}
		String dayStr = "";// 存储数据库查询条件
		String dayStr2 = "";// 显示到前台的时间段
		long dayCha = DateDayUtil.getDaycha(stratedate, enddate);// 获取两个时间的时间差（天数）
		if (stratedate.length() < 0 && enddate.length() > 0) {
			stratedate = enddate;
		} else if (stratedate.length() > 0 && enddate.length() < 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			enddate = sdf.format(new Date());
		}
		if (dayCha > -1) {
			for (int i = 0; i < dayCha + 1; i++) {
				dayStr += DateDayUtil.getDayCum(stratedate, i) + ",";
				dayStr2 += "'" + DateDayUtil.getDayCum(stratedate, i) + "',";
			}
			dayStr = dayStr.substring(0, dayStr.length() - 1);
			dayStr2 = dayStr2.substring(0, dayStr2.length() - 1);
		}
		if ("".equals(dayStr)) {
			for (int i = 0; i < 1; i++) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String nowdate = sdf.format(new Date());
				dayStr += DateDayUtil.getDayCum(nowdate, i) + ",";
				dayStr2 += "'" + DateDayUtil.getDayCum(nowdate, i) + "',";
			}
			dayStr = dayStr.substring(0, dayStr.length() - 1);
			dayStr2 = dayStr2.substring(0, dayStr2.length() - 1);
		}
		String cumstStr = "";// 保存前台传递过来的供货商
		String emaildateStr = "";// 保存前台传递过来的批次号
		String[] cumstr = null;
		if (controlStr.indexOf("#") > -1) {
			cumstStr = controlStr.split("#")[0];
			emaildateStr = controlStr.substring(controlStr.indexOf("#"), controlStr.length() - 1).replaceAll("#", "").replaceAll("-", "");
			if (cumstStr.length() > 0 && cumstStr.indexOf(",") > -1) {
				cumstr = cumstStr.split(",");
			}

		} else if (controlStr.length() > 0 && controlStr.indexOf("-") < 0) {
			cumstr = controlStr.split(",");

		} else if (controlStr.length() > 0 && controlStr.indexOf("-") > -1) {
			emaildateStr = controlStr.replaceAll("#-", "").replaceAll("-", "").substring(0, controlStr.length() - 1);
		}
		// 保存前台传递过来的供货商解析后存储到列表中
		List cumstrList1 = new ArrayList();
		if (cumstr == null) {
			List<DeliveryDTO> dDto = deliveryDao.getDeliveryCutomer();
			if (dDto != null) {
				cumstr = new String[dDto.size()];
				for (int i = 0; i < dDto.size(); i++) {
					cumstr[i] = String.valueOf(((DeliveryDTO) dDto.get(i)).getCustomerid());
				}
			}
		} else {
			for (int i = 0; i < cumstr.length; i++) {
				cumstrList1.add(cumstr[i]);
			}

		}
		// 传递到后台查询供货商的条件
		List<Customer> cumstrList = new ArrayList();
		// 保存到前台显示的所有供货商
		// List<Customer> cumstrListAll = customerDao.getAllCustomers();
		List<Customer> cumstrListAll = getDmpDAO.getAllCustomers();
		if (cumstr != null) {
			for (int i = 0; i < cumstr.length; i++) {

				Customer customer = getDmpDAO.getCustomer(new Long(cumstr[i]));
				if (customer != null && customer.getCustomerid() > 0) {
					cumstrList.add(customer);
				}
			}
		}
		Map map1 = new HashMap();
		Map map2 = new HashMap();

		map1 = deliveryService.getAllByDayAndTypeAndBranchId(dayStr, emaildateStr, cumstr, branchidPram, flowordertype, timeLong, dateType);
		map2 = deliveryService.getAllByDayAndBranchid(dayStr, emaildateStr, cumstr, branchidPram, flowordertype, timeLong, dateType);

		model.addAttribute("cumstrList1", cumstrList1);
		model.addAttribute("branchnameList", branchnameList);
		model.addAttribute("emailList", emailList);
		model.addAttribute("deliverySuccessMap", map1);
		model.addAttribute("deliveryAllMap", map2);
		model.addAttribute("cumstrList", cumstrList);
		model.addAttribute("cumstrListAll", cumstrListAll);
		model.addAttribute("dayStr", dayStr2);
		model.addAttribute("dayStrList", dayStr);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		stratedate = stratedate.equals("") ? sdf.format(new Date()) : stratedate;
		enddate = enddate.equals("") ? sdf.format(new Date()) : enddate;
		model.addAttribute("stratedate", stratedate);
		model.addAttribute("enddate", enddate);

		return "/delivery/view";
	}

	@RequestMapping("/selectAll")
	public String allList(Model model, @RequestParam(value = "datetype", required = false, defaultValue = "1") long datetype,
			@RequestParam(value = "stratedate", required = false, defaultValue = "") String stratedate, @RequestParam(value = "enddate", required = false, defaultValue = "") String enddate,
			@RequestParam(value = "controlStr", required = false, defaultValue = "") String controlStr, @RequestParam(value = "timeLong", required = false, defaultValue = "24") long timeLong,
			@RequestParam(value = "flowordertype", required = false, defaultValue = "1") int flowordertype) {
		// 查询当前选择的时间段内 所有的批次号
		List<EmaildateTDO> emailList = deliveryService.getEmaildateList(stratedate, enddate);

		String dayStr = "";// 存储数据库查询条件
		String dayStr2 = "";// 显示到前台的时间段
		long dayCha = DateDayUtil.getDaycha(stratedate, enddate);// 获取两个时间的时间差（天数）
		if (stratedate.length() < 0 && enddate.length() > 0) {
			stratedate = enddate;
		} else if (stratedate.length() > 0 && enddate.length() < 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			enddate = sdf.format(new Date());
		}
		if (dayCha > -1) {
			for (int i = 0; i < dayCha + 1; i++) {
				dayStr += DateDayUtil.getDayCum(stratedate, i) + ",";
				dayStr2 += "'" + DateDayUtil.getDayCum(stratedate, i) + "',";
			}
			dayStr = dayStr.substring(0, dayStr.length() - 1);
			dayStr2 = dayStr2.substring(0, dayStr2.length() - 1);
		}
		if ("".equals(dayStr)) {
			for (int i = 0; i < 1; i++) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String nowdate = sdf.format(new Date());
				dayStr += DateDayUtil.getDayCum(nowdate, i) + ",";
				dayStr2 += "'" + DateDayUtil.getDayCum(nowdate, i) + "',";
			}
			dayStr = dayStr.substring(0, dayStr.length() - 1);
			dayStr2 = dayStr2.substring(0, dayStr2.length() - 1);
		}
		String cumstStr = "";// 保存前台传递过来的供货商
		String emaildateStr = "";// 保存前台传递过来的批次号
		String[] cumstr = null;
		if (controlStr.indexOf("#") > -1) {
			cumstStr = controlStr.split("#")[0];
			emaildateStr = controlStr.substring(controlStr.indexOf("#"), controlStr.length() - 1).replaceAll("#", "").replaceAll("-", "");
			if (cumstStr.length() > 0 && cumstStr.indexOf(",") > -1) {
				cumstr = cumstStr.split(",");
			}

		} else if (controlStr.length() > 0 && controlStr.indexOf("-") < 0) {
			cumstr = controlStr.split(",");

		} else if (controlStr.length() > 0 && controlStr.indexOf("-") > -1) {
			emaildateStr = controlStr.replaceAll("#", "").replaceAll("-", "").substring(0, controlStr.length() - 1);
		}
		// 保存前台传递过来的供货商解析后存储到列表中
		List cumstrList1 = new ArrayList();
		if (cumstr == null) {
			List<DeliveryDTO> dDto = deliveryDao.getDeliveryCutomer();
			if (dDto != null) {
				cumstr = new String[dDto.size()];
				for (int i = 0; i < dDto.size(); i++) {
					cumstr[i] = String.valueOf(((DeliveryDTO) dDto.get(i)).getCustomerid());
				}
			}
		} else {
			for (int i = 0; i < cumstr.length; i++) {
				cumstrList1.add(cumstr[i]);
			}

		}
		// 传递到后台查询供货商的条件
		List<Customer> cumstrList = new ArrayList();
		// 保存到前台显示的所有供货商
		List<Customer> cumstrListAll = getDmpDAO.getAllCustomers();
		if (cumstr != null) {
			for (int i = 0; i < cumstr.length; i++) {
				Customer customer = getDmpDAO.getCustomer(new Long(cumstr[i]));
				if (customer != null && customer.getCustomerid() > 0) {
					cumstrList.add(customer);
				}
			}
		}
		Map map1 = deliveryService.getAllByDayAndType(dayStr, emaildateStr, cumstr, flowordertype, timeLong, datetype);
		Map map2 = deliveryService.getAllByDay(dayStr, emaildateStr, cumstr, timeLong, datetype);

		model.addAttribute("cumstrList1", cumstrList1);
		model.addAttribute("emailList", emailList);
		model.addAttribute("deliverySuccessMap", map1);
		model.addAttribute("deliveryAllMap", map2);
		model.addAttribute("cumstrList", cumstrList);
		model.addAttribute("cumstrListAll", cumstrListAll);
		model.addAttribute("dayStr", dayStr2);
		model.addAttribute("dayStrList", dayStr);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		stratedate = stratedate.equals("") ? sdf.format(new Date()) : stratedate;
		enddate = enddate.equals("") ? sdf.format(new Date()) : enddate;
		model.addAttribute("stratedate", stratedate);
		model.addAttribute("enddate", enddate);
		return "/delivery/viewall";
	}

}