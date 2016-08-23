package cn.explink.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.service.taskshow.TaskShowService;

import com.pjbest.osp.cfg.system.service.VersionInfoRespone;
import com.pjbest.osp.cfg.system.service.ViewRecordRespone;


@RequestMapping("/taskShow")
@Controller
public class TaskShowController {

	private static Logger logger = LoggerFactory.getLogger(TaskShowController.class);
	@Autowired
	TaskShowService taskShowService;
	
	@RequestMapping("/historyList/{page}")
	private String list(@PathVariable("page") long page, Model model) {
		return "taskHistory/taskHistoryList";
	}
	@RequestMapping("/getLatestVersion")
	private @ResponseBody Map<String,Object> getLatestVersion(Model model, HttpServletRequest req)throws Exception{
		VersionInfoRespone latestVersion = this.taskShowService.getLatestVersion();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("latestVersion",latestVersion);
		return  map;
	}
	
	//用户浏览记录上报
	@RequestMapping("/sendReadRecord")
	private @ResponseBody String sendReadRecord(Model model, 
			@RequestParam(value = "versionNo", required = true) String versionNo,
			@RequestParam(value = "showTime", required = true) long showTime){
		ViewRecordRespone viewRecordRespone;
		try {
			viewRecordRespone = this.taskShowService.getAddVersionViewRecord(versionNo,showTime);
			if(viewRecordRespone!=null&&viewRecordRespone.getIsSuccess()!=false){
				logger.info("用户浏览记录上报成功,版本号为：{}",versionNo);
				return "{\"success\":0,\"successdata\":\"用户浏览记录上报成功\"}";
			}else{
				logger.info("用户浏览记录上报失败，失败原因：{},版本号为：{}",viewRecordRespone.getErrorMsg(),versionNo);
				return "{\"success\":1,\"successdata\":\""+viewRecordRespone.getErrorMsg()+"\"}";
			}
		} catch (Exception e) {
			logger.info("用户浏览记录上报失败，失败原因：{},版本号为：{}",e.getMessage(),versionNo);
			return "{\"success\":1,\"successdata\":\""+e.getMessage()+"\"}";
		}
		
		
	}
	
}
