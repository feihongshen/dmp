package cn.explink.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.domain.Customer;
import cn.explink.schedule.Constants;
import cn.explink.service.taskshow.TaskShowService;
import cn.explink.util.StringUtil;

import com.pjbest.osp.cfg.system.service.SysVersionModel;
import com.pjbest.osp.cfg.system.service.VersionInfoRespone;


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
		SysVersionModel sysVersionModel= new SysVersionModel();
		sysVersionModel.setId(123);
		sysVersionModel.setAdded("232");
		latestVersion.setData(sysVersionModel);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("latestVersion",latestVersion);
		return  map;
	}
	
}
