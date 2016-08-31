package cn.explink.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.service.TpsTranscwbPrintService;
import cn.explink.util.Page;
import cn.explink.vo.TPStranscwb;

/**
 * TPS 运单打印
 * @author yurong.liang 2016-06-17
 */
@RequestMapping("/tpsTranscwbPrint")
@Controller
public class TpsTranscwbPrintController {
	@Autowired
	private TpsTranscwbPrintService transcwbPrintService;
	
	/**
	 * 从tps获取运单号
	 */
	@RequestMapping("/getTpsTranscwbFromTps")
	public String getTpsTranscwb(HttpServletResponse response,Model model,
			@RequestParam(value = "num", required = false,defaultValue = "0") int num) {
		this.transcwbPrintService.getTranscwbFromTPS(num);
		return "redirect:/tpsTranscwbPrint/printList/1";
	}
	
	/**
	 * 查询本地运单号列表
	 */
	@RequestMapping("/printList/{page}")
	public String queryList(HttpServletResponse response,Model model,
			@PathVariable("page") int page,
			@RequestParam(value = "tpstranscwb", required = false, defaultValue = "") String tpstranscwb,
			@RequestParam(value = "printStatus", required = false,defaultValue = "") Integer printStatus,
			@RequestParam(value = "rows", required = false, defaultValue = "10") int rows
			) throws IOException{
		
		List<TPStranscwb> transcwbList = this.transcwbPrintService.getList(printStatus,tpstranscwb,page,rows);
		int total=transcwbPrintService.getCount(printStatus, tpstranscwb);
		model.addAttribute("transcwbList", transcwbList);
		model.addAttribute("printStatus", printStatus);
		model.addAttribute("tpstranscwb", tpstranscwb);
		model.addAttribute("page_obj", new Page(total, page, Page.ONE_PAGE_NUMBER));
		model.addAttribute("page", page);
		return "transcwbprint/transcwbPrintList";
	} 

	/**
	 * 打印运单号页面
	 */
	@RequestMapping("/printTpstranscwbPage")
	public String printExcle(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "transcwbs", required = false, defaultValue = "") final String transcwbs) {
		List<String> tpstranscwbList = new ArrayList<String>();
		String[] transcwbArray = transcwbs.split(",");
		for (String tpstranscwb : transcwbArray) {
			tpstranscwbList.add(tpstranscwb);
		}
		//更新打印状态
		//this.transcwbPrintService.updatePrintStatus(tpstranscwbList);
		model.addAttribute("cwbs", tpstranscwbList);
		return "transcwbprint/printTranscwb";
	}
	
	/**
	 * 打印运单号
	 */
	@RequestMapping("/printTranscwb")
	@ResponseBody
	public void  printTranscwb(Model model, HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "transcwbs", required = false, defaultValue = "") final String transcwbs) {
		List<String> tpstranscwbList = new ArrayList<String>();
		String[] transcwbArray = transcwbs.split(",");
		for (String tpstranscwb : transcwbArray) {
			tpstranscwbList.add(tpstranscwb);
		}
		//更新打印状态
		this.transcwbPrintService.updatePrintStatus(tpstranscwbList);
	}
	
}
