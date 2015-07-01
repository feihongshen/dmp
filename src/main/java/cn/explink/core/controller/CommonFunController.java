package cn.explink.core.controller;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.core.utils.NameUtils;
import cn.explink.service.BranchService;
import cn.explink.service.CustomerService;
import cn.explink.service.UserService;

/**
 * 前台公共Controller
 * @author gaoll
 *
 */
@Controller
@RequestMapping("/commonFun")
public class CommonFunController {

	@Autowired
	private UserService userService;
	@Autowired
	private BranchService branchService;
	@Autowired
	private CustomerService customerservice;
	
	/**
	 * 渲染前台枚举数据
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getEmumByName")
	@ResponseBody
	public List<Object> getEmumByName(HttpServletRequest request) {
	
		List<Object> resultList = new ArrayList<Object>();
		String fullClassName = request.getParameter("fullClassName");
		String viewField = request.getParameter("viewField");
		String saveField = request.getParameter("saveField");
		Class<?> classT;
		try {
			classT = Class.forName(fullClassName);
			if(classT.isEnum()){
				Map<String,Object> tempMap = null;
	        	for(Object obj : classT.getEnumConstants()){
	        		tempMap = new HashMap<String, Object>();
	        		Method getView = obj.getClass().getDeclaredMethod("get" + NameUtils.getFirstUpperName(viewField));
	        		Method getSave = obj.getClass().getDeclaredMethod("get" + NameUtils.getFirstUpperName(saveField));
	        		Object saveObj = getSave.invoke(((Enum<?>) obj));
	        		Object viewObj = getView.invoke(((Enum<?>) obj));
	        		tempMap.put(saveField, saveObj);
    				tempMap.put(viewField, viewObj);
    				resultList.add(tempMap);
	        	}
	        }
		} catch (Exception e) {
			// TODO: 枚举 类不存在,枚举方法不存在
			e.printStackTrace();
		}
		return resultList;
	}
	
	/**
	 * 渲染前台外键关联数据
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getJoinByName")
	@ResponseBody
	public List<? extends Object> getFnameById(HttpServletRequest request) {
	
		//根据entity名获取对应Service
		List<? extends Object> resultList = null;
		String entityName = request.getParameter("entityName");
		//用户表express_set_user
		if( entityName.equals("User")){
			resultList =  this.userService.getPageCash();
		}
		//机构表express_set_branch
		else if( entityName.equals("Branch")){
			resultList = this.branchService.getPageCash();
		}
		else if( entityName.equals("Customer")){
			resultList = this.customerservice.getPageCash();
		}
		
		return resultList;
	}
	
}
