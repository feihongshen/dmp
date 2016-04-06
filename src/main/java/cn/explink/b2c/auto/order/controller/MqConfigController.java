package cn.explink.b2c.auto.order.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.auto.order.mq.ConsumerStarter;
import cn.explink.b2c.auto.order.mq.MqConfigService;
import cn.explink.b2c.auto.order.vo.MqConfigVo;



@RequestMapping("/mqconfig")
@Controller
public class MqConfigController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	ConsumerStarter consumerStarter;

	@Autowired
	MqConfigService mqConfigService;
	
	@RequestMapping("/list")
	public  String list(Model model) {
		List<MqConfigVo> cfgList =mqConfigService.querAll();
		Map<String,String> queueMap=consumerStarter.getMqSubscribers();
		
		if(cfgList!=null){
			for(MqConfigVo vo:cfgList){
				if(queueMap.get(vo.getName())!=null){
					vo.setConnected(1);
				}
			}
		}else{
			cfgList=new ArrayList<MqConfigVo>();
		}
		
		model.addAttribute("cfgList", cfgList);
		return "auto/mqconfig";
	}
	
	@RequestMapping("/start")
	public @ResponseBody String start(Model model) {
		try {
			consumerStarter.stop();
			consumerStarter.start();
		} catch (Exception e) {
			logger.error("mq start error",e);
			return "{\"errorCode\":1,\"error\":\"启动MQ失败，"+e.getMessage()+"\"}";
		}
		return "{\"errorCode\":0,\"error\":\"启动MQ成功\"}";
	}
	
	@RequestMapping("/stop")
	public @ResponseBody String stop(Model model) {
		try {
			consumerStarter.stop();
		} catch (Exception e) {
			logger.error("mq stop error",e);
			return "{\"errorCode\":1,\"error\":\"停止MQ失败，"+e.getMessage()+"\"}";
		}
		return "{\"errorCode\":0,\"error\":\"停止MQ成功\"}";
	}

	@RequestMapping("/save")
	public @ResponseBody String save(Model model,MqConfigVo vo) {
		try{
		mqConfigService.saveOrUpdate(vo);
		} catch (Exception e) {
			logger.error("mq config save error",e);
			return "{\"errorCode\":1,\"error\":\"保存MQ配置失败，"+e.getMessage()+"\"}";
		}
		return "{\"errorCode\":0,\"error\":\"保存MQ配置成功\"}";
	}
	
	@RequestMapping("/del")
	public @ResponseBody String del(Model model,MqConfigVo vo) {
		try{
		mqConfigService.delete(vo);
		} catch (Exception e) {
			logger.error("mq config save error",e);
			return "{\"errorCode\":1,\"error\":\"删除MQ配置失败，"+e.getMessage()+"\"}";
		}
		return "{\"errorCode\":0,\"error\":\"删除MQ配置成功\"}";
	}

}
