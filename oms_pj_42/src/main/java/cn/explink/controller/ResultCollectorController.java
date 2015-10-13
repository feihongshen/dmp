package cn.explink.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.service.ResultCollector;
import cn.explink.service.ResultCollectorManager;

@Controller
@RequestMapping("/result")
public class ResultCollectorController {

	@Autowired
	ResultCollectorManager resultCollectorManager;

	@RequestMapping("/{id}")
	public @ResponseBody ResultCollector getResultCollector(@PathVariable("id") String id) {
		return resultCollectorManager.getResultCollector(id);
	}
}
