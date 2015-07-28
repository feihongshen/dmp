package cn.explink.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.setViewName("salary/paybusinessbenefits/list");
		return modelAndView;
	}
	
	@RequestMapping("/cutData/{ids}")
	@ResponseBody
	public String cutData(@PathVariable("ids")String customerids){
		int num=paybusinessbenefitsDao.cutDatabyCustomerids(customerids);
		return "{\"errorcode\":0,\"errormsg\":\"成功删除"+num+"单数据\"}";
	}
	
	@RequestMapping("/savedata")
	@ResponseBody
	public String saveData(Paybusinessbenefits paybusinessbenefits
			){
			int count=0;
			String eString="";
			List<Paybusinessbenefits> paybusinessbenefitsList=paybusinessbenefitsService.updateAndAddPaybusinessbenefitsValue(paybusinessbenefits);
			if (paybusinessbenefits.getRemark().equals("1")) {
				//修改的时候先删除customerid的相关的设置
				paybusinessbenefitsService.deleteUpdateinstall(paybusinessbenefits.getCustomerid());
				for (Paybusinessbenefits paybusinessbenefits2 : paybusinessbenefitsList) {
					try {
						paybusinessbenefitsDao.insertIntoPaybusinessbenefits(paybusinessbenefits2);
						count++;
					} catch (Exception e) {
					}
				}
/*				int count=paybusinessbenefitsDao.updatePaybusinessbenfits(paybusinessbenefits);
*/				if (count>0) {
					return "{\"errorCode\":0,\"error\":\"修改成功\"}";
				}else {
					return "{\"errorCode\":1,\"error\":\"修改出现异常\"}";
				}
			}else {
				
					if (paybusinessbenefitsService.checkIsCreateByCustomerid(paybusinessbenefits.getCustomerid())) {
						return "{\"errorCode\":2,\"error\":\"该供货商已经创建过工资业务补助设置，不能重复创建\"}";
					}
					for (Paybusinessbenefits paybusinessbenefits2 : paybusinessbenefitsList) {
						try {
							paybusinessbenefitsDao.insertIntoPaybusinessbenefits(paybusinessbenefits2);
							count++;
						} catch (Exception e) {
							 eString=e.getMessage();
						}
					}
/*					paybusinessbenefitsDao.insertIntoPaybusinessbenefits(paybusinessbenefits);
*/					
					if (count>0) {
						return "{\"errorCode\":0,\"error\":\"成功保存\"}";
					}else {
						return "{\"errorCode\":1,\"error\":\"保存失败,失败原因为"+eString+"\"}";
					}
			}
		
	}
	/**
	 * easyui数据查询
	 * @param rows
	 * @param customername
	 * @param page
	 * @param model
	 * @return
	 */
	@RequestMapping("/listfind")
	public @ResponseBody Map<String, Object> queryList1(@RequestParam(defaultValue="10",required=false,value="rows")long rows,
			@RequestParam(defaultValue="",required=false,value="customername")String customername,
			@RequestParam(defaultValue="",required=false,value="page")long page,
			Model model){
		List<Paybusinessbenefits> paybusinessbenefits=new ArrayList<Paybusinessbenefits>();
		long count=0;
		List<Customer> customers=customerDAO.getCustomerByCustomerName(customername);
		String customerids=paybusinessbenefitsService.getCustomerids(customers);
		paybusinessbenefits=paybusinessbenefitsDao.getPaybusinessbenefitsbyCustomerName(page,customerids,rows);
		count=paybusinessbenefitsDao.getPaybusinessbenefitsbyCustomerName(-9,customerids,rows).size();
		List<Customer> customersList=customerDAO.getAllCustomers();
		paybusinessbenefits=paybusinessbenefitsService.addCustomerNameCustomers(customersList,paybusinessbenefits);
		HashMap<String, Object> jsonMap=new HashMap<String, Object>();
		jsonMap.put("total", count);
		jsonMap.put("rows", paybusinessbenefits);
		JSONObject jsonObject=new JSONObject();
		jsonObject=JSONObject.fromObject(jsonMap);
		model.addAttribute("jsonObject", jsonObject);
		return jsonMap;
	}
	/**
	 * 编辑设置需要的数据
	 * @param id
	 * @return
	 */
	@RequestMapping("/findeditinstall/{id}")
	public @ResponseBody Map<String, Object> editinstall(@PathVariable("id")long id){
		Paybusinessbenefits paybusinessbenefits=paybusinessbenefitsDao.getPaybusinessbenefitsByid(id);
		return paybusinessbenefitsService.changeDataDetail(paybusinessbenefits);
	}
	@RequestMapping("/getCustomers")
	public @ResponseBody List<Customer> getCustomers(){
		List<Customer> customersList=customerDAO.getAllCustomers();
		return customersList;
	}
	
	
}
