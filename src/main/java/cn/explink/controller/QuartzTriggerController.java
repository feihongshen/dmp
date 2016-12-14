package cn.explink.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.jfree.util.Log;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.StdScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.explink.pos.tools.JacksonMapper;

@Controller
@RequestMapping(value = "/quartz")
public class QuartzTriggerController{
	private Logger logger = LoggerFactory.getLogger(this.getClass());
//	xml中配置schedulerFactory实际指向的是org.quartz.impl.StdScheduler
//	所以这里进行注入的是StdScheduler,而不是在xml中的SchedulerFactoryBean类。
	@Resource(name="schedulerFactory")
	private StdScheduler stdScheduler;
//	@Resource(name="schedulerFactory")
//	private SchedulerFactoryBean schedulerFactorywh;
//	@Resource(name="schedulerFactory")
//	private Scheduler scheduler;
		
	/**
	 * 定时任务管理列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "index")
	public ModelAndView timeTask(HttpServletRequest request) {
		return new ModelAndView("timetask/quartzTriggerConsole");
	}

	@RequestMapping(value = "/findTriggerList", method = RequestMethod.POST)
	@ResponseBody
	public void findTriggerList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		DataGridReturn dgr  = new DataGridReturn();
		List<TriggerVo> jobList = new ArrayList<TriggerVo>();
//		String[] triggersInGroup;
//		String[] triggerGroups;
		int k=1;
		
//		   triggerGroups= stdScheduler.getTriggerGroupNames();
//		   triggersInGroup = stdScheduler.getTriggerNames(StdScheduler.DEFAULT_GROUP);
		//stdScheduler.getTriggerGroupNames()取得所有的jobDetail组
		   for(String triggerGroup: stdScheduler.getTriggerGroupNames()){
			   //取得某个group下的所有的jobDetail
			   for(String triggerName: stdScheduler.getTriggerNames(triggerGroup)){
				   //取得指定的Trigger
				  Trigger trigger=stdScheduler.getTrigger(triggerName, triggerGroup);//StdScheduler.DEFAULT_GROUP
				  if(trigger == null){
					  logger.info("stdScheduler.getTrigger get Null!!! ");
				      	continue;
				  }
				  logger.info(trigger.getName()+"  trigger is instanceof CronTrigger :"+(	trigger instanceof CronTrigger));
//				  CronTrigger cronTrigger = (CronTrigger) trigger;
				  TriggerVo vo = new TriggerVo();
				  if(trigger instanceof CronTrigger){
						CronTrigger cronTrigger = (CronTrigger)trigger;
					    vo.setTriggerId(k);
					    k=k+1;
					    //任务名称
					    vo.setTriggerName(triggerName);//cronTrigger.getJobName();
					    
					    vo.setTriggerType("CRON");
					    //任务描述
					    vo.setTriggerDesc(cronTrigger.getDescription());
					    //执行计划
					    vo.setTriggerPlan(cronTrigger.getCronExpression());
					    //分组
					    vo.setTriggerGroup(triggerGroup);
					    //上次执行时间
					    vo.setPreviousFireTime(cronTrigger.getPreviousFireTime());
					    //下次执行时间
					    vo.setNextFireTime(cronTrigger.getNextFireTime());
					}
					if(trigger instanceof SimpleTrigger){
						SimpleTrigger simpleTrigger = (SimpleTrigger)trigger;
						vo.setTriggerId(k);
						k=k+1;
						vo.setTriggerName(triggerName);
						vo.setTriggerType("SIMPLE");
					    vo.setTriggerDesc(simpleTrigger.getDescription());
					    vo.setTriggerPlan("SIMPLE Trigger does't have CronExpression");
					    vo.setTriggerGroup(triggerGroup);
					    vo.setPreviousFireTime(simpleTrigger.getPreviousFireTime());
					    vo.setNextFireTime(simpleTrigger.getNextFireTime());
					}
				  
//				  -------test---------
//				  CronTrigger is superclass of CronTriggerBean,so it should be able to
//				  cast to the CronTriggerBean
//				  CronTriggerFactoryBean
//				  CronTriggerBean cronTriggerBean=(CronTriggerBean)trigger;
//				  logger.info("Trigger cast to CronTriggerBean succesful");
//				  CronTriggerBean cronTriggerBean2=(CronTriggerBean) cronTrigger;
//				  logger.info("CronTrigger cast to CronTriggerBean succesful");
//				  --------end---------
				  //获取当前定时器的状态
			      int state=stdScheduler.getTriggerState(triggerName, triggerGroup);
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
		String[] triggerGroups=stdScheduler.getTriggerGroupNames();;
		String[] triggersInGroup;
		int i,j;
		String groupName="";
		for (i = 0; i < triggerGroups.length; i++) {
			   triggersInGroup = stdScheduler.getTriggerNames(triggerGroups[i]);

			   for (j = 0; j < triggersInGroup.length; j++) {
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
	 * @param triggerNames
	 * @since 1.0
	 */
	@RequestMapping(value = "/startTrigger", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson startTrigger(@RequestParam("triggerNames[]") String[] triggerNames) {
		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.setStatus(true);
		try {
			for(String triggerName : triggerNames){
				if(!Tools.isEmpty(triggerName)){
					String groupName=getTriggerGroupName(triggerName);
					
					this.logger.info("QuartzTriggerController手动启动定时任务triggerName={},groupName={}", triggerName, groupName);
					
					stdScheduler.resumeTrigger(triggerName, groupName);
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
	 * @param triggerNames
	 * @since 1.0
	 */
	@RequestMapping(value = "/pauseTrigger", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson pauseTrigger(@RequestParam("triggerNames[]") String[] triggerNames) {
		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.setStatus(true);
		try {
			for(String triggerName : triggerNames){
				if(!Tools.isEmpty(triggerName)){
					String groupName=getTriggerGroupName(triggerName);
					
					this.logger.info("QuartzTriggerController手动暂停定时任务triggerName={},groupName={}", triggerName, groupName);
					
					stdScheduler.pauseTrigger(triggerName, groupName);
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
	@RequestMapping(value = "/executeTriggerNow", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson executeTriggerNow(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		String triggerName = request.getParameter("triggerName");
		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.setStatus(true);
		try {
			String groupName=getTriggerGroupName(triggerName);
			Trigger trigger = stdScheduler.getTrigger(triggerName, groupName);
			if (null != trigger) {// 存在才修改
				String taskJobName=trigger.getJobName();
				String taskGroupName=trigger.getJobGroup();
				
				this.logger.info("QuartzTriggerController手动立刻执行定时任务taskJobName={},taskGroupName={}", taskJobName, taskGroupName);
				
				stdScheduler.triggerJob(taskJobName, taskGroupName);
			}
		} catch (SchedulerException e) {
			ajaxJson.setStatus(false);
			ajaxJson.setMsg("系統异常");
			Log.info("executeTriggerNow:定时器执行异常");
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

		String triggerName = request.getParameter("triggerName");
		String cronExpression = request.getParameter("cronExpression");
		AjaxJson ajaxJson = new AjaxJson();
		ajaxJson.setStatus(true);
		try {
			if(!StringUtils.isEmpty(triggerName)){
				String groupName=getTriggerGroupName(triggerName);
				CronTrigger trigger = (CronTrigger)stdScheduler.getTrigger(triggerName, groupName);
				int state=stdScheduler.getTriggerState(triggerName, groupName);
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
					newCronTriggera.setDescription("此cron触发器的cron表达式已被修改!  "+trigger.getDescription());
					newCronTriggera.setCronExpression(cronExpression);
//					CronExpression cronExpressionEP=new CronExpression(cronExpression);
					newCronTriggera.setJobGroup(taskGroupName);
					newCronTriggera.setJobName(taskJobName);
					newCronTriggera.setJobDataMap(jobdata);
					stdScheduler.rescheduleJob(triggerName, groupName, newCronTriggera);
					if(state == Trigger.STATE_PAUSED){
						stdScheduler.pauseTrigger(triggerName, groupName);
					}
				}
				
			}
		} catch (Exception e) {
			ajaxJson.setStatus(false);
			ajaxJson.setMsg("系統异常");
			logger.error("modifyCronExpression  error");
			logger.error("", e);
		}
		return ajaxJson;
	}
	
}

class TriggerVo{
	private int triggerId;
	/**
	 * 任务名称
	 */
    private String triggerName; 
    
    private String triggerGroup;
    private String triggerType;
    /**
     * 任务描述
     */
    private String triggerDesc;
    /**
     * 执行计划
     * CronTrigger    里面是getCronExpression
     * SimpleTrigger  无
     */
    private String triggerPlan;

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
	public int getTriggerId() {
		return triggerId;
	}
	public void setTriggerId(int triggerId) {
		this.triggerId = triggerId;
	}
	public String getTriggerName() {
		return triggerName;
	}
	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}
	public String getTriggerGroup() {
		return triggerGroup;
	}
	public void setTriggerGroup(String triggerGroup) {
		this.triggerGroup = triggerGroup;
	}
	public String getTriggerType() {
		return triggerType;
	}
	public void setTriggerType(String triggerType) {
		this.triggerType = triggerType;
	}
	public String getTriggerDesc() {
		return triggerDesc;
	}
	public void setTriggerDesc(String triggerDesc) {
		this.triggerDesc = triggerDesc;
	}
	public String getTriggerPlan() {
		return triggerPlan;
	}
	public void setTriggerPlan(String triggerPlan) {
		this.triggerPlan = triggerPlan;
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
 class AjaxJson {
	/**
	 * 操作成功标识
	 */
	private boolean status = true;
	/**
	 * 提示信息
	 */
	private String msg = "";
	/**
	 * 动态对象
	 */
	private Object obj = null;
	/**
	 * 拓展属性
	 */
	private Map map = new HashMap();
	
	private List<Object> objList=new ArrayList<Object>();
	
	public List<Object> getObjList() {
		return objList;
	}
	public void setObjList(List<Object> objList) {
		this.objList = objList;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	public Map getMap() {
		return map;
	}
	public void setMap(Map map) {
		this.map = map;
	}
	
}
 
 /**封装easyUI datagrid所需的格式
 * @author pjbest
 *
 */
class DataGridReturn {

		public DataGridReturn(Integer total, List rows) {
			this.total = total;
			this.rows = rows;
		}

		private Integer total;// 总记录数
		private List rows;// 每行记录
		private List footer;

		public Integer getTotal() {
			return total;
		}

		public void setTotal(Integer total) {
			this.total = total;
		}

		public List getRows() {
			return rows;
		}

		public void setRows(List rows) {
			this.rows = rows;
		}

		public List getFooter() {
			return footer;
		}

		public void setFooter(List footer) {
			this.footer = footer;
		}
		public DataGridReturn() {
			// TODO Auto-generated constructor stub
		}
	}
 
  class Tools {
	  private static Logger logger = LoggerFactory.getLogger(Tools.class);
	  static ObjectMapper mapper = JacksonMapper.getInstance();
		public static String obj2json(Object obj) {
			try {
				if (obj == null) {
					return "{}";
				}
				//mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
				mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);

				return mapper.writeValueAsString(obj);
			} catch (Exception e) {
				logger.error("==>> Object to JSON occer error: ", e);
				logger.error("obj2json:Object to JSON occer error");
			}
			return "{}";
		}
	  
	  
	  public static void outData2Page(String s, HttpServletResponse response) throws IOException {
			outData2Page(s, response, "text/html; charset=UTF-8");
		}

		public static void outData2Page(String s, HttpServletResponse response, String contentType)
				throws IOException {
			response.setContentType(contentType);
			PrintWriter printwriter = response.getWriter();
			printwriter.print(s);
			printwriter.close();
		}
		
		public static boolean isEmpty(String source) {
			return (source == null || source.trim().equals(""));
		} 
 }
 
 
