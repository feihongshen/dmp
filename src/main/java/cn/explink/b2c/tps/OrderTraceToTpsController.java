package cn.explink.b2c.tps;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.explink.b2c.tools.JointService;
import cn.explink.core.utils.StringUtils;
import cn.explink.dao.CustomerDAO;
import cn.explink.domain.Customer;
/**
 * 订单轨迹推tps Controller
 * @author huan.zhou
 *
 */
@Controller
@RequestMapping("/orderTraceToTPS")
public class OrderTraceToTpsController {
	@Autowired
	private JointService jointService;
	@Autowired
	private CustomerDAO customerDAO;
	@Autowired
	private OrderTraceToTPSService orderTraceToTPSService;
	
	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		OrderTraceToTPSCfg  orderTraceToTPS = this.orderTraceToTPSService.getOrderTraceToTPSCfg(key);
		String customerids = orderTraceToTPS == null ? null : orderTraceToTPS.getCustomerids();
		String[] customeridArray = StringUtils.isEmpty(customerids) ? new String[]{} : customerids.split(",|，");
		List<Long> customeridList = new ArrayList<Long>();
		for(String customerid : customeridArray){
			try{
				customeridList.add(Long.valueOf(customerid));
			}catch(Exception e){
				//DO nothing
			}
		}
		List<Customer> customerList  = customerDAO.getAllCustomers();
		model.addAttribute("orderTraceToTPS", orderTraceToTPS);
		model.addAttribute("customeridList", customeridList);
		model.addAttribute("customerList", customerList);
		model.addAttribute("joint_num", key);
		return "b2cdj/tps/OrderTraceToTPS";

	}
	
	@RequestMapping("/save/{id}")
	public @ResponseBody String save(Model model, @PathVariable("id") int key, HttpServletRequest request) {

		if(StringUtils.isBlank(request.getParameter("customerids"))){
			return "{\"errorCode\":1,\"error\":\"订单客户必填\"}";
		}		
		if(StringUtils.isBlank(request.getParameter("trackMaxTryTime"))){
			return "{\"errorCode\":1,\"error\":\"轨迹最大尝试次数必填\"}";
		}
		if(StringUtils.isBlank(request.getParameter("password")) || !"explink".equals(request.getParameter("password"))){
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
		
		this.orderTraceToTPSService.edit(request, key);
		return "{\"errorCode\":0,\"error\":\"修改成功\"}";
	}
	
	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		this.orderTraceToTPSService.updateState(key, state);
		// 保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}
}
