package cn.explink.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.explink.core.common.model.json.DataGridReturn;
import cn.explink.domain.AjaxJson;
import cn.explink.util.Tools;


@Controller
@RequestMapping(value = "/dmpJob")
public class DmpJobController{

	@Autowired(required=false)
	@Qualifier("schedulerFactory") 
	private StdScheduler schedulerFactory;
	
	private static Logger logger = LoggerFactory.getLogger(DmpJobController.class);
	
	/**
	 * 定时任务管理列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "index")
	public ModelAndView timeTask(HttpServletRequest request) {
		return new ModelAndView("dmpJobIndex");
	}

	@RequestMapping(value = "/findJobList", method = RequestMethod.POST)
	@ResponseBody
	public void findJobList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DataGridReturn dgr  = new DataGridReturn();
		List<CronTriggerVo> jobList = new ArrayList<CronTriggerVo>();
		String[] triggerGroups;
		String[] triggersInGroup;
		int i;
		int j;
		int k=1;

		triggerGroups = schedulerFactory.getTriggerGroupNames();
		for (i = 0; i < triggerGroups.length; i++) {
//		   System.out.println("Group: " + triggerGroups[i] + " contains the following triggers");
		   triggersInGroup = schedulerFactory.getTriggerNames(triggerGroups[i]);
		   
		   for (j = 0; j < triggersInGroup.length; j++) {
		      CronTrigger trigger=(CronTrigger) schedulerFactory.getTrigger(triggersInGroup[j], triggerGroups[i]);

		      if(trigger == null){
		      	continue;
		      }
		      CronTriggerVo vo = new CronTriggerVo();
		      int state=schedulerFactory.getTriggerState(triggersInGroup[j], triggerGroups[i]);
//		      TriggerState statea = schedulerFactory.getTriggerState(triggerKey);
		      vo.setJobId(k);
		      k=k+1;
		      vo.setJobName(triggersInGroup[j]);
		      vo.setJobDesc(trigger.getDescription());
		      vo.setJobPlan(trigger.getCronExpression());
		      vo.setJobPlanDesc("");
		      vo.setPreviousFireTime(trigger.getPreviousFireTime());
		      vo.setNextFireTime(trigger.getNextFireTime());
		      
//		      Trigger.STATE_NORMAL, Trigger.STATE_PAUSED, 
//		      Trigger.STATE_COMPLETE, Trigger.STATE_ERROR, 
//		      Trigger.STATE_BLOCKED, Trigger.STATE_NONE;
		      switch (state) {
	            case Trigger.STATE_NORMAL:
	            	vo.setStatusCode(0);
	            	vo.setStatusName("正常");	       
	                break;
	            case Trigger.STATE_PAUSED:
	            	vo.setStatusCode(1);
	            	vo.setStatusName("暂停");
	                break;
	            case Trigger.STATE_COMPLETE:
	            	vo.setStatusCode(2);
	            	vo.setStatusName("完成");
	                break;
	            case Trigger.STATE_ERROR:
	            	vo.setStatusCode(3);
	            	vo.setStatusName("错误");
	                break;
	            case Trigger.STATE_BLOCKED:
	            	vo.setStatusCode(4);
	            	vo.setStatusName("阻塞");
	                break;
	            case Trigger.STATE_NONE:
	            	vo.setStatusCode(-1);// 如果不存在该trigger,获得的状态是NONE
	            	vo.setStatusName("不存在");	      
	                break;
			}
		      jobList.add(vo);
		   }
		}

		dgr.setRows(jobList);
		dgr.setTotal(jobList.size());
		Tools.outData2Page(Tools.obj2json(dgr), response);
	}

	private String getTriggerGroupName(String triggerName) throws SchedulerException{
		String[] triggerGroups=schedulerFactory.getTriggerGroupNames();;
		String[] triggersInGroup;
		int i,j;
		String groupName="";
		for (i = 0; i < triggerGroups.length; i++) {
//			   System.out.println("Group: " + triggerGroups[i] + " contains the following triggers");
			   triggersInGroup = schedulerFactory.getTriggerNames(triggerGroups[i]);

			   for (j = 0; j < triggersInGroup.length; j++) {
//			      System.out.println("- " + triggersInGroup[j]);
				  if(triggerName.equals(triggersInGroup[j])){
					  groupName=triggerGroups[i];
				  }
			   }
			}
		return groupName;		
	}
	
	
	/**
	 * 开启任务
	 * 
	 * @param jobName
	 * @since 1.0
	 */
	@RequestMapping(value = "/startJob", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson startJob(@RequestParam("jobNames[]") String[] jobNames) {
		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.setStatus(true);
		try {
			for(String jobName : jobNames){
				if(!Tools.isEmpty(jobName)){
					String groupName=getTriggerGroupName(jobName);
					
					logger.info("DMPJobController手动立刻执行定时任务jobName={},groupName={}", jobName, groupName);
					
					schedulerFactory.resumeTrigger(jobName, groupName);
//					schedulerFactory.resumeTrigger(TriggerKey.triggerKey(jobName));
				}
			}
		} catch (SchedulerException e) {
			ajaxJson.setStatus(false);
			ajaxJson.setMsg("系統异常");
			logger.error("", e);
		}
		return ajaxJson;
	}

	/**
	 * 暂停任务
	 * 
	 * @param jobName
	 * @since 1.0
	 */
	@RequestMapping(value = "/pauseJob", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson pauseJob(@RequestParam("jobNames[]") String[] jobNames) {
		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.setStatus(true);
		try {
			for(String jobName : jobNames){
				if(!Tools.isEmpty(jobName)){
					String groupName=getTriggerGroupName(jobName);
					
					logger.info("DMPJobController手动暂停执行定时任务jobName={},groupName={}", jobName, groupName);
					
					schedulerFactory.pauseTrigger(jobName, groupName);
//					schedulerFactory.pauseTrigger(TriggerKey.triggerKey(jobName));
				}
			}
		} catch (SchedulerException e) {
			ajaxJson.setStatus(false);
			ajaxJson.setMsg("系統异常");
			logger.error("", e);
		}
		return ajaxJson;
	}

	/**
	 * 立即执行指定定时器
	 * <p>
	 * 立即执行指定定时器
	 * </p>
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @since 1.0
	 */
	@RequestMapping(value = "/executeJobNow", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson executeJobNow(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		String jobName = request.getParameter("jobName");
		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.setStatus(true);
		try {
			String groupName=getTriggerGroupName(jobName);
			CronTrigger trigger = (CronTrigger)schedulerFactory.getTrigger(jobName, groupName);
//			CronTrigger trigger = (CronTrigger)schedulerFactory.getTrigger(TriggerKey.triggerKey(jobName));
			if (null != trigger) {// 存在才修改
				String taskJobName=trigger.getJobName();
				String taskGroupName=trigger.getJobGroup();
				
				logger.info("DMPJobController手动立刻执行定时任务taskJobName={},taskGroupName={}", taskJobName, taskGroupName);
				
				schedulerFactory.triggerJob(taskJobName, taskGroupName);
//				schedulerFactory.triggerJob(trigger.getJobKey());
			}
		} catch (SchedulerException e) {
			ajaxJson.setStatus(false);
			ajaxJson.setMsg("系統异常");
			logger.error("", e);
		}
		return ajaxJson;
	}
	
	/**
	 * 修改执行计划
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @since 1.0
	 */
	@RequestMapping(value = "/modifyCronExpression", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson modifyCronExpression  (HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		String jobName = request.getParameter("jobName");
		String cronExpression = request.getParameter("cronExpression");
		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.setStatus(true);
		try {
			schedulerFactory.getTriggerState("", "");
			if(!StringUtils.isEmpty(jobName)){
//				CronTrigger trigger = (CronTrigger)schedulerFactory.getTrigger(TriggerKey.triggerKey(jobName));
//				TriggerState state = schedulerFactory.getTriggerState(TriggerKey.triggerKey(jobName));
				String groupName=getTriggerGroupName(jobName);
				CronTrigger trigger = (CronTrigger)schedulerFactory.getTrigger(jobName, groupName);
				int state=schedulerFactory.getTriggerState(jobName, groupName);
				String taskJobName=trigger.getJobName();
				String taskGroupName=trigger.getJobGroup();
				JobDataMap jobdata=trigger.getJobDataMap();
		
				String oldCron = trigger.getCronExpression();
				if(!oldCron.equals(cronExpression) && !StringUtils.isBlank(cronExpression)){
//					 CronTrigger newCronTrigger = TriggerBuilder.newTrigger()
//							 	.withDescription(trigger.getDescription())
//							 	.withIdentity(TriggerKey.triggerKey(jobName))
//							 	.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).forJob(trigger.getJobKey()).build();
					CronTrigger newCronTriggera=new CronTrigger();
					newCronTriggera.setName(trigger.getName());
					newCronTriggera.setDescription(trigger.getDescription());
					newCronTriggera.setCronExpression(cronExpression);
//					CronExpression cronExpressionEP=new CronExpression(cronExpression);
					newCronTriggera.setJobGroup(taskGroupName);
					newCronTriggera.setJobName(taskJobName);
					newCronTriggera.setJobDataMap(jobdata);
			
					schedulerFactory.rescheduleJob(jobName, groupName, newCronTriggera);
//					schedulerFactory.rescheduleJob(TriggerKey.triggerKey(jobName), newCronTrigger);
//					if(state == TriggerState.PAUSED){
//						schedulerFactory.pauseTrigger(TriggerKey.triggerKey(jobName));
//					}
//					Trigger.STATE_PAUSED
					if(state == Trigger.STATE_PAUSED){
						schedulerFactory.pauseTrigger(jobName, groupName);
					}
				}
				
			}
		} catch (Exception e) {
			ajaxJson.setStatus(false);
			ajaxJson.setMsg("系統异常");
			logger.error("", e);
		}
		return ajaxJson;
	}
	
}

/**
 * CronTriggerVo
 * @author gordon.zhou
 *
 */
class CronTriggerVo{
	private int jobId;
	/**
	 * 任务名称
	 */
    private String jobName; 
    /**
     * 任务描述
     */
    private String jobDesc;
    /**
     * 执行计划
     */
    private String jobPlan;
    /**
     * 执行计划描述
     */
    private String jobPlanDesc;
    /**
     * 上次执行时间
     */
    private Date previousFireTime;
    /**
     * 下次执行时间
     */
    private Date nextFireTime;
    /**
     * 任务状态编码
     */
    private int statusCode;
    /**
     * 任务状态
     */
    private String statusName;
    
	public int getJobId() {
		return jobId;
	}
	public void setJobId(int jobId) {
		this.jobId = jobId;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getJobDesc() {
		return jobDesc;
	}
	public void setJobDesc(String jobDesc) {
		this.jobDesc = jobDesc;
	}
	public String getJobPlan() {
		return jobPlan;
	}
	public void setJobPlan(String jobPlan) {
		this.jobPlan = jobPlan;
	}
	public String getJobPlanDesc() {
		return jobPlanDesc;
	}
	public void setJobPlanDesc(String jobPlanDesc) {
		this.jobPlanDesc = jobPlanDesc;
	}
	public Date getPreviousFireTime() {
		return previousFireTime;
	}
	public void setPreviousFireTime(Date previousFireTime) {
		this.previousFireTime = previousFireTime;
	}
	public Date getNextFireTime() {
		return nextFireTime;
	}
	public void setNextFireTime(Date nextFireTime) {
		this.nextFireTime = nextFireTime;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	
    
}
