package cn.explink.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.dao.FindLocationDao;
import cn.explink.domain.DeliveryStations;
import cn.explink.pos.tools.JacksonMapper;

@Controller
@RequestMapping("/location")
public class FindLocationController {
	@Autowired
	FindLocationDao findLocationDao;

	private Logger logger = LoggerFactory.getLogger(this.getClass());


	@RequestMapping("/findaddress")
	
	public @ResponseBody String findaddress(HttpServletRequest request){
			
		List<DeliveryStations> list =findLocationDao.findALLDeliveryStations();
		
		String responseJson = null;
		String callback = request.getParameter("callback");

		try {
			responseJson = JacksonMapper.getInstance().writeValueAsString(list);
			logger.info("返回信息:{}",responseJson);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return callback + "(" + responseJson + ");";
		
		}
	/*@RequestMapping("/findKuFangaddress")
	public String findKuFangAddress(){
		
		List<DeliveryStations> list=findLocationDao.findKuFangALLDeliveryStations();
		String responseJson=null;
		try {
			responseJson = JacksonMapper.getInstance().writeValueAsString(list);
			logger.info("返回信息:{}",responseJson);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return responseJson;		
	}*/
	
	}

