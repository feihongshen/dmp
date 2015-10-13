package cn.explink.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.ProxyConfDAO;
import cn.explink.domain.ProxyConf;
import cn.explink.util.Page;

@Controller
@RequestMapping("/proxy")
public class ProxyConfController {
	@Autowired
	ProxyConfDAO proxyConfDAO;

	@RequestMapping("/select/{page}")
	public String toNextStopPage(Model model, @PathVariable("page") long page, @RequestParam(value = "state", required = false, defaultValue = "-1") int state) {
		model.addAttribute("proxyConfList", proxyConfDAO.getProxyAll(page, state));
		model.addAttribute("page_obj", new Page(proxyConfDAO.getProxyAllConut(state), page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "proxy/proxylist";
	}

	@RequestMapping("/add")
	public String add() {
		return "proxy/add";
	}

	@RequestMapping("/create")
	public @ResponseBody String create(Model model, @RequestParam("ip") String ip, @RequestParam("port") int port, @RequestParam("with") int with, @RequestParam("pass") String pass,
			@RequestParam("state") int state, @RequestParam("isnoDefault") int isnoDefault) {

		if ("explink".equals(pass)) {
			ProxyConf proxy = proxyConfDAO.getProxyByIp(ip);
			if (proxy != null) {
				return "{\"errorCode\":1,\"error\":\"该代理ip已存在\"}";
			}
			ProxyConf proxyConf = new ProxyConf();
			proxyConf.setIp(ip);
			proxyConf.setPort(port);
			proxyConf.setWith(with);
			proxyConf.setState(state);
			proxyConf.setIsnoDefault(isnoDefault);
			proxyConfDAO.creProxyConf(proxyConf);
		} else {
			return "{\"errorCode\":1,\"error\":\"密码有误\"}";
		}
		return "{\"errorCode\":0,\"error\":\"新建成功\"}";
	}

	@RequestMapping("/edit/{id}")
	public String edit(Model model, @PathVariable("id") int id) {
		model.addAttribute("proxyConf", proxyConfDAO.getProxyById(id));
		return "proxy/edit";
	}

	@RequestMapping("/save/{id}")
	public @ResponseBody String save(Model model, @PathVariable("id") int id, @RequestParam("ip") String ip, @RequestParam("port") int port, @RequestParam("with") int with,
			@RequestParam("pass") String pass, @RequestParam("state") int state, @RequestParam("isnoDefault") int isnoDefault) {

		if ("explink".equals(pass)) {
			ProxyConf proxy = proxyConfDAO.getProxyByIp(ip);
			if (proxy != null && proxy.getId() != id) {
				return "{\"errorCode\":1,\"error\":\"该代理ip已存在\"}";
			}
			proxyConfDAO.editProxyType(ip, port, with, id, state, isnoDefault);
		} else {
			return "{\"errorCode\":1,\"error\":\"密码有误\"}";
		}
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

	@RequestMapping("/del/{id}")
	public String del(Model model, @PathVariable("id") int id) {
		model.addAttribute("proxyConf", proxyConfDAO.getProxyById(id));
		return "proxy/del";
	}

	@RequestMapping("/delproxy/{id}")
	public @ResponseBody String del(Model model, @PathVariable("id") int id, @RequestParam("pass") String pass) {

		if ("explink".equals(pass)) {
			proxyConfDAO.delProxyState(id);
		} else {
			return "{\"errorCode\":1,\"error\":\"密码有误\"}";
		}
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}

}
