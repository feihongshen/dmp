package cn.explink.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.explink.dao.CustomerDAO;
import cn.explink.dao.PaybusinessbenefitsDao;
import cn.explink.domain.Customer;
import cn.explink.domain.Paybusinessbenefits;
import cn.explink.service.PaybusinessbenefitsService;
import cn.explink.util.Page;

@Controller
@RequestMapping("/paybusinessbenefits")
public class PaybusinessbenefitsController {
	@Autowired
	PaybusinessbenefitsDao paybusinessbenefitsDao;
	@Autowired
	PaybusinessbenefitsService paybusinessbenefitsService;
	@Autowired
	CustomerDAO customerDAO;
	/**
	 * 工资业务补助设置主查询接口
	 * @param page
	 * @param customerid
	 * @return
	 */
	@RequestMapping("/list/{page}")
	public ModelAndView queryList(@PathVariable("page")long page,@RequestParam(defaultValue="",required=false,value="customername")String customername){
		List<Paybusinessbenefits> paybusinessbenefits=new ArrayList<Paybusinessbenefits>();
		long count=0;
		List<Customer> customers=customerDAO.getCustomerByCustomerName(customername);
		String customerids=paybusinessbenefitsService.getCustomerids(customers);
		paybusinessbenefits=paybusinessbenefitsDao.getPaybusinessbenefitsbyCustomerName(page,customerids);
		count=paybusinessbenefitsDao.getPaybusinessbenefitsbyCustomerName(-9,customerids).size();
		List<Customer> customersList=customerDAO.getAllCustomers();
		paybusinessbenefits=paybusinessbenefitsService.addCustomerNameCustomers(customersList,paybusinessbenefits);
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.addObject("customersList", customersList);
		modelAndView.addObject("paybusinessbenefits", paybusinessbenefits);
		modelAndView.addObject("Page", new Page(count, page, Page.ONE_PAGE_NUMBER));
		modelAndView.setViewName("salary/paybusinessbenefits/list");
		return modelAndView;
	}
	@RequestMapping("/cutData/{ids}")
	@ResponseBody
	public String cutData(@PathVariable("ids")String ids){
		int num=paybusinessbenefitsDao.cutData(ids);
		return "{\"errorcode\":0,\"errormsg\":\"成功删除"+num+"单数据\"}";
	}
	@RequestMapping("/savedata")
	@ResponseBody
	public String saveData(Paybusinessbenefits paybusinessbenefits
			){
			try {
				paybusinessbenefitsDao.insertIntoPaybusinessbenefits(paybusinessbenefits);
				return "{\"errorCode\":0,\"error\":\"成功保存\"}";
			} catch (Exception e) {
				return "{\"errorCode\":1,\"error\":\"保存失败,失败原因为"+e+"\"}";
			}
	}
	
}
