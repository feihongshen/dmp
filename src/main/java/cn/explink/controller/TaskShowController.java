package cn.explink.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.service.taskshow.TaskShowService;
import cn.explink.util.AjaxResult;

import com.pjbest.osp.cfg.system.service.HandbookOnLine;
import com.pjbest.osp.cfg.system.service.HandbookRespone;
import com.pjbest.osp.cfg.system.service.NearestViewRecordRespone;
import com.pjbest.osp.cfg.system.service.NoticeRespone;
import com.pjbest.osp.cfg.system.service.SysVersionModel;
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
	
	
	@RequestMapping("/getNearestViewRecord")
	@ResponseBody
	public List<SysVersionModel> getNearestViewRecord(Model model, HttpServletRequest req)throws Exception{
		List<SysVersionModel> sysVersionModels = new ArrayList<SysVersionModel>();
		NearestViewRecordRespone nearestViewRecord = this.taskShowService.getNearestViewRecord();
		if(nearestViewRecord != null) {
			if (nearestViewRecord.getIsSuccess() == false) {
				logger.error(nearestViewRecord.getErrorMsg());
			} else if(nearestViewRecord.getData() != null) {
				sysVersionModels = nearestViewRecord.getData();
			}
		}
		
		return sysVersionModels;
	}
	
	@RequestMapping("/getLatestHandbook")
	@ResponseBody
	public HandbookOnLine getLatestHandbookUrl(Model model, HttpServletRequest req)throws Exception{
		HandbookOnLine handbookOnLine = null;
		HandbookRespone handbook = this.taskShowService.getLatestHandbook();
		if(handbook != null) {
			if (handbook.getIsSuccess() == false) {
				logger.error(handbook.getErrorMsg());
				//return new AjaxResult(false, handbook.getErrorMsg());
			} else {
				if(handbook.getData() != null) {
					handbookOnLine = handbook.getData();
				}
			}
		}
		
		return handbookOnLine;
	}
	
	@RequestMapping("/getLatestNoticeContent")
	@ResponseBody
	public AjaxResult getLatestNoticeContent(Model model, HttpServletRequest req)throws Exception{
		String noticeContent = "";
		NoticeRespone notice = this.taskShowService.getLatestNotice();
		if(notice != null) {
			if(notice != null) {
				if (notice.getIsSuccess() == false) {
					logger.error(notice.getErrorMsg());
					//return new AjaxResult(false, notice.getErrorMsg());
				} else {
					if(notice.getData() != null && notice.getData().getContent() != null) {
						noticeContent = notice.getData().getContent();
					}
				}
			}
		}
		
		return new AjaxResult(true, noticeContent);
	}
}
