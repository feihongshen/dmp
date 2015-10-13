package cn.explink.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.TruckDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Truck;
import cn.explink.service.TruckService;
import cn.explink.util.Page;
import cn.explink.util.StringUtil;

@RequestMapping("/truck")
@Controller
public class TruckController {

	@Autowired
	TruckDAO truckDao;
	@Autowired
	TruckService truckService;
	@Autowired
	UserDAO userDao;

	@RequestMapping("/list/{page}")
	public String list(Model model, @PathVariable("page") long page, @RequestParam(value = "truckno", required = false, defaultValue = "") String truckno,
			@RequestParam(value = "trucktype", required = false, defaultValue = "") String trucktype, @RequestParam(value = "showMessage", required = false, defaultValue = "") String showMessage) {
		model.addAttribute("truckList", truckDao.getTruckByPage(page, truckno, trucktype));
		model.addAttribute("page_obj", new Page(truckDao.getTruckCount(truckno, trucktype), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "/truck/list";
	}

	@RequestMapping("/trucknoCheck")
	public @ResponseBody boolean customernameCheck(@RequestParam("tno") String truckno) throws Exception {
		truckno = new String(truckno.getBytes("ISO8859-1"), "utf-8");
		List<Truck> truckList = truckDao.getTruckByTrucknoCheck(truckno);
		return truckList.size() == 0;
	}

	@RequestMapping("/add")
	public String add(Model model) {
		model.addAttribute("userList", userDao.getUserByRole(3));
		return "truck/add";
	}

	@RequestMapping("/create")
	public @ResponseBody String create(Model model, HttpServletRequest request) {
		String truckno = StringUtil.nullConvertToEmptyString(request.getParameter("truckno"));
		List<Truck> list = truckDao.getTruckByTruckname(truckno);
		if (list.size() > 0) {
			return "{\"errorCode\":1,\"error\":\"对应文字已存在\"}";
		} else {
			Truck truck = truckService.loadFormForTruck(request);
			truckDao.creTruck(truck);
			model.addAttribute("userList", userDao.getUserByRole(3));
			return "{\"errorCode\":0,\"error\":\"新建成功\"}";
		}

	}

	@RequestMapping("/edit/{id}")
	public String edit(@PathVariable("id") long truckid, Model model, HttpServletRequest request) {
		model.addAttribute("truck", truckDao.getTruckByTruckid(truckid));
		model.addAttribute("userList", userDao.getUserByRole(3));
		return "truck/edit";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(Model model, HttpServletRequest request, @PathVariable("id") long truckid) {
		String truckno = StringUtil.nullConvertToEmptyString(request.getParameter("truckno"));
		List<Truck> list = truckDao.getTruckByTruckname(truckno);
		if (list.size() > 0 && list.get(0).getTruckid() != truckid) {
			return "{\"errorCode\":1,\"error\":\"对应文字已存在\"}";
		} else {
			Truck truck = truckService.loadFormForTruck(request, truckid);
			truckDao.saveTruck(truck);
			model.addAttribute("truck", truckDao.getTruckByTruckid(truckid));
			model.addAttribute("userList", userDao.getUserByRole(3));
			return "{\"errorCode\":0,\"error\":\"修改成功\"}";
		}

	}

	@RequestMapping("/del/{id}")
	public @ResponseBody String del(@PathVariable("id") long truckid) {
		truckDao.delTruck(truckid);
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

}
