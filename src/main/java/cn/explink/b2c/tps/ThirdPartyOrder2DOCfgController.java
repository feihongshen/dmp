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
 * 外单推送DO Controller
 * @author gordon.zhou
 *
 */
@Controller
@RequestMapping("/thirdPartyOrder2DO")
public class ThirdPartyOrder2DOCfgController {
	@Autowired
	private JointService jointService;
	@Autowired
	private CustomerDAO customerDAO;
	@Autowired
	private ThirdPartyOrder2DOCfgService thirdPartyOrder2DOService;
	
	@RequestMapping("/show/{id}")
	public String jointShow(@PathVariable("id") int key, Model model) {
		ThirdPartyOrder2DOCfg  ThirdPartyOrder2DO = this.thirdPartyOrder2DOService.getThirdPartyOrder2DOCfg(key);
		String customerids = ThirdPartyOrder2DO == null ? null : ThirdPartyOrder2DO.getCustomerids();
		String[] customeridArray = StringUtils.isEmpty(customerids) ? new String[]{} : customerids.split(",|，");
		List<Long> customeridList = new ArrayList<Long>();
		for(String customerid : customeridArray){
			customeridList.add(Long.valueOf(customerid));
		}
		List<Customer> customerList  = customerDAO.getAllCustomers();
		model.addAttribute("thirdPartyOrder2DO", ThirdPartyOrder2DO);
		model.addAttribute("customeridList", customeridList);
		model.addAttribute("customerList", customerList);
		model.addAttribute("joint_num", key);
		return "b2cdj/tps/thirdPartyOrder2DOCfg";

	}
	
	@RequestMapping("/save/{id}")
	public @ResponseBody String save(Model model, @PathVariable("id") int key, HttpServletRequest request) {

		if(StringUtils.isBlank(request.getParameter("carrierCode"))){
			return "{\"errorCode\":1,\"error\":\"承运商编码必填\"}";
		}
		
		if(StringUtils.isBlank(request.getParameter("maxTryTime"))){
			return "{\"errorCode\":1,\"error\":\"最大尝试次数必填\"}";
		}
		
		if(StringUtils.isBlank(request.getParameter("customerids"))){
			return "{\"errorCode\":1,\"error\":\"外单客户必填\"}";
		}		
		if(StringUtils.isBlank(request.getParameter("password")) || !"explink".equals(request.getParameter("password"))){
			return "{\"errorCode\":1,\"error\":\"密码不正确\"}";
		}
		
		this.thirdPartyOrder2DOService.edit(request, key);
		return "{\"errorCode\":0,\"error\":\"修改成功\"}";
	}
	
	@RequestMapping("/del/{state}/{id}")
	public @ResponseBody String updateState(Model model, @PathVariable("id") int key, @PathVariable("state") int state) {
		this.thirdPartyOrder2DOService.updateState(key, state);
		// 保存
		return "{\"errorCode\":0,\"error\":\"操作成功\"}";
	}
}
