package cn.explink.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.domain.VO.express.EmbracedImportResult;
import cn.explink.service.ResultCollector;
import cn.explink.service.ResultCollectorManager;

@Controller
@RequestMapping("/result")
public class ResultCollectorController {

	@Autowired
	ResultCollectorManager resultCollectorManager;

	@RequestMapping("/{id}")
	public @ResponseBody ResultCollector getResultCollector(@PathVariable("id") String id) {
		return this.resultCollectorManager.getResultCollector(id);
	}

	/**
	 *
	 * @Title: getEmbracedResultCollector
	 * @description 通过id获取补录时产生的导入结果类
	 * @author 刘武强
	 * @date  2015年10月13日下午3:33:54
	 * @param  @param id
	 * @param  @return
	 * @return  EmbracedImportResult
	 * @throws
	 */
	@RequestMapping("EmbracedResult/{id}")
	public @ResponseBody EmbracedImportResult getEmbracedResultCollector(@PathVariable("id") String id) {
		return this.resultCollectorManager.getEmbracedResultCollector(id);
	}

	@RequestMapping("/stop/{id}")
	public @ResponseBody ResultCollector stopResultCollector(@PathVariable("id") String id) {
		ResultCollector resultCollector = this.resultCollectorManager.getResultCollector(id);
		resultCollector.setStoped(true);
		return resultCollector;
	}
}
