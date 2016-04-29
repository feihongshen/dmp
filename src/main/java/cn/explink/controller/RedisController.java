package cn.explink.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.explink.util.ResourceBundleUtil;
import cn.explink.util.impl.RedisMapImpl;

/**
 * redis 处理缓存
 * @author jeef.fang
 *
 */
@Controller
@RequestMapping("/redis")
public class RedisController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	CacheManager cacheManager;
	
	@SuppressWarnings("rawtypes")
	@Autowired
	RedisTemplate redisTemplate;

	//redis 工程前缀
	private static final String RedisProjectPrefix = ResourceBundleUtil.RedisPrefix;
	
	//redis 启动参数前缀
    private static final String RedisCompanyPrefix = System.getProperty("company");
    
    //redis 查询统配字符
    private static final String RedisSeparator = ":";
    
    //redis 查询统配字符
    private static final String RedisQueryConstant = "*";
    
    //mac地址正则
    private static String PatternMac = "[A-F0-9]{2}(-[A-F0-9]{2}){5}"; 
    
	/**
	 * 缓存辅助类
	 * @author jeef.fang
	 *
	 */
	public class RedisModel{
		private String key;
		private String value;
		
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		
	}
	
	/**
	 * 截取有效字符串
	 * @param input
	 * @return
	 */
	private String trimKey(String input){
	    String output = "";
	    int lastIndex = input.lastIndexOf(":");
	    output  = input.substring(lastIndex + 1);
	    return output;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/list")
	public String list(Model model, HttpServletRequest request, 
			@RequestParam(value = "cacheName", required = false, defaultValue = "") String cacheName,
			@RequestParam(value = "key", required = false, defaultValue = "") String key) throws Exception {
		
		logger.info("查询redis缓存，cacheName={},key={},redisController->list", cacheName, key);
		
		cacheName = cacheName.replace("'", "");
		key = key.replace("'", "");

		List<RedisModel> siList = new ArrayList<RedisModel>();
		if(!"".equals(cacheName)){
			Cache cache = this.cacheManager.getCache(cacheName);
			if(null != cache){
				Set<String> keys = null;
				String prefix = cacheName + RedisSeparator + RedisProjectPrefix + RedisSeparator + RedisCompanyPrefix + RedisSeparator;
				if(!"".equals(key)){
					keys = new HashSet<String>();
					keys.add(prefix + key);
				}else{
					keys = this.redisTemplate.keys(prefix + RedisQueryConstant);
				}
				//查询缓存值
				if(null != keys && keys.size() > 0){
					RedisMapImpl redisMapImpl = new RedisMapImpl(cacheName);
					for(String tmpKey : keys){
						String simpleKey = this.trimKey(tmpKey);
						Cache.ValueWrapper valueObj = cache.get(simpleKey);
						if(null != valueObj){
							RedisModel valueModel = new RedisModel();
							valueModel.setKey(simpleKey);
							if(null != valueObj.get()){
								if(Pattern.compile(PatternMac).matcher(valueObj.get().toString()).find()){  
									valueModel.setValue(redisMapImpl.getValue(valueObj.get().toString()).toString());
								}else{
									String valueStr = "";
									try{
										valueStr = JSON.toJSONString(valueObj.get());
									}catch(Exception e){
										this.logger.error("redis查看 JSON.toJSONString 转换错误", e);
										valueStr = valueObj.get().toString();
									}
									valueModel.setValue(valueStr);
								}
							}else{
								valueModel.setValue("");
							}
							
							siList.add(valueModel);
						}
					}
				}
			}
		}
		
		model.addAttribute("siList", siList);
		model.addAttribute("cacheName", cacheName);
		model.addAttribute("key", key);
		return "/redis/list";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/del")
	public @ResponseBody String del(@RequestParam(value = "cacheName", required = false, defaultValue = "") String cacheName,
			@RequestParam(value = "key", required = false, defaultValue = "") String key) throws Exception {
		cacheName = cacheName.replace("'", "");
		key = key.replace("'", "");
		
		if(!"".equals(cacheName)){
			Cache cache = this.cacheManager.getCache(cacheName);
			if(null != cache){
				Set<String> keys = null;
				String prefix = cacheName + RedisSeparator + RedisProjectPrefix + RedisSeparator + RedisCompanyPrefix + RedisSeparator;
				if(!"".equals(key)){
					keys = new HashSet<String>();
					keys.add(prefix + key);
				}else{
					keys = this.redisTemplate.keys(prefix + RedisQueryConstant);
				}
				//查询缓存值
				if(null != keys && keys.size() > 0){
					for(String tmpKey : keys){
						String simpleKey = this.trimKey(tmpKey);
						cache.evict(simpleKey);
					}
				}
			}else{
				logger.info("清除redis缓存,根据cacheName找不到对应的cache,redisController->del");
			}
		}else{
			logger.info("清除redis缓存,cacheName为空,redisController->del");
		}
		
		logger.info("清除redis缓存,cacheName={},key={},redisController->del", cacheName, key);
		return "{\"errorCode\":0,\"error\":\"清除redis缓存成功\"}";
	}
}