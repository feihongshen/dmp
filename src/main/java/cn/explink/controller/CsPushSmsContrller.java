package cn.explink.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.core.interceptor.PageControl;
import cn.explink.core.pager.Pager;
import cn.explink.core.pager.PropertyFilter;
import cn.explink.core.utils.StringUtils;
import cn.explink.dao.SmsManageDao;
import cn.explink.service.CsPushSmsService;

@Controller
@RequestMapping("/csPushSms")
public class CsPushSmsContrller {
	
	@Autowired
	private CsPushSmsService csPushSmsService;
	@Autowired
	private SmsManageDao smsManageDao;
	
	@RequestMapping("/rushSmsList")
	public String sendList(){
		return "/workorder/rushSmsList";
	}
	
	/**
	 * 渲染table数据
	 * @param request
	 * @return
	 */
	@RequestMapping(value="json",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> dataList(HttpServletRequest request) {
		
		//分页参数
		Pager pager = this.generatePager(request);
		//查询条件
		List<PropertyFilter> filters = PropertyFilter.buildFromHttpRequest(request);
		//执行查询
		//service.queryData(pager,filters);
		this.csPushSmsService.queryData(pager,filters);
		return this.getPageData(PageControl.getPager());
	}
	
	/**
	 * 构建页面数据对象
	 * @param pager
	 * @return
	 */
	private Map<String, Object> getPageData(Pager pager) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rows", pager.getList());
		map.put("total", pager.getItemsTotal());
		return map;
	}

	/**
	 * 构建查询分页参数
	 * @param request
	 * @return
	 */
	private Pager generatePager(HttpServletRequest request) {
		
		int pageNo=1;	//当前页码
		int pageSize=20;	//每页行数
		//TODO 排序待实现
		String orderBy="id";	//排序字段
		String order="asc";	//排序顺序
		if(StringUtils.isNotEmpty(request.getParameter("page")))
			pageNo=Integer.valueOf(request.getParameter("page"));
		if(StringUtils.isNotEmpty(request.getParameter("rows")))
			pageSize=Integer.valueOf(request.getParameter("rows"));
		if(StringUtils.isNotEmpty(request.getParameter("sort")))
			orderBy=request.getParameter("sort").toString();
		if(StringUtils.isNotEmpty(request.getParameter("order")))
			order=request.getParameter("order").toString();
		Pager pager = new Pager();
		pager.setCurPage(pageNo);
		pager.setItemsPerPage(pageSize);
		return pager;
	}

	// 验证手机号码的合法性
	private static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^1\\d{10}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
	
	@ModelAttribute
	public void getCsPushSms(@RequestParam(value = "id", defaultValue = "-1") Integer id,Model model) {
		if (id != -1) {
			model.addAttribute("csPushSms", csPushSmsService.getCsPushSmsDao().find(Long.valueOf(id.intValue())));
		}
	}

}
