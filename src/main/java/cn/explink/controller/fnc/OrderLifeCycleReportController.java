package cn.explink.controller.fnc;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.core.common.model.json.DataGridReturn;
import cn.explink.dao.CustomerDAO;
import cn.explink.service.fnc.OrderLifeCycleReportService;
import cn.explink.util.Tools;

@Controller
@RequestMapping("/orderlifecycle")
public class OrderLifeCycleReportController {
	
	@Autowired
	private CustomerDAO customerDao;
	
	@Autowired
	private OrderLifeCycleReportService orderLifeCycleReportService;
	
	@RequestMapping("/index")
	public String index(Model model){
		
		model.addAttribute("customerList", customerDao.getAllCustomers());
		
		return "/orderlifecycle/index";
	}
	
	/**
	 * 签收站点余额报表数据
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(params = "list")
	@ResponseBody
	public void list(Model model,
			@RequestParam(value = "customers", required = false) Integer[] customers,
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		String selectedCustomers = "";
		if(ArrayUtils.isNotEmpty(customers)){
			selectedCustomers = StringUtils.join(Arrays.asList(customers), ',');
		}
		
		DataGridReturn dg = this.orderLifeCycleReportService.getDataGridReturn(selectedCustomers);
		Tools.outData2Page(Tools.obj2json(dg), response);
		
	}
}
