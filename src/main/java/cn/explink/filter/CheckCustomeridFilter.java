package cn.explink.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.vipshop.VipShop;
import cn.explink.dao.CustomerDAO;
import cn.explink.domain.Customer;
import cn.explink.util.ApplicationContextUtil;
import cn.explink.util.StringUtil;

@Component
public class CheckCustomeridFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		WebApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
		CustomerDAO customerDAO = (CustomerDAO) ac.getBean("customerDAO"); 
		JiontDAO jiontDAO = (JiontDAO) ac.getBean("jiontDAO"); 
		// TODO Auto-generated method stub
		String customerids = request.getParameter("customerids");
		
		String[] uri = request.getRequestURI().split("/");  
		int joint_num = Integer.parseInt(uri[uri.length-1]);
		boolean pass = this.checkCustomer(customerids,joint_num,customerDAO,jiontDAO); //  继续执行 action
        if(pass){
        	filterChain.doFilter(request, response);
        }else{
        	//request.setAttribute("error", "该客户已在别的接口对接中设置");
        	response.getWriter().write("{\"errorCode\":1,\"error\":\"" + "该客户已在别的接口对接中设置" + "\"}");
        }
	}	

	
	public boolean checkCustomer(String customerids,int joint_num,CustomerDAO customerDAO,JiontDAO jiontDAO){
		boolean pass = true;
		String[] custormeridstr = null;//修改之后的客户id
		List<Long> customeridArray = new ArrayList<Long>();
		if(StringUtils.isNotBlank(customerids)){
			custormeridstr = customerids.split(",|，");
			for(String custStr : custormeridstr){
				try{
					customeridArray.add(Long.valueOf(custStr));
				}catch(Exception e){
					pass = false;
				}
			}
		}
		/**
		 * 如果新设置的客户id 设置过其他的对接，不允许设置当前的对接
		 */
		if(!customeridArray.isEmpty()){
			List<Customer> oldCustList = customerDAO.getCustomerBoundB2cEnumByIds(StringUtil.getStringsByLongList(customeridArray));
			if(oldCustList != null){
				for(Customer cust : oldCustList){
					if(!cust.getB2cEnum().equals(joint_num + "") && jiontDAO.getJointEntity(Integer.valueOf(cust.getB2cEnum())) != null){
						pass = false;
						//throw new RuntimeException("客户" + cust.getCustomerid() + "已在【" + B2cEnum.getEnumByKey(Integer.valueOf(cust.getB2cEnum()).intValue()).getText() + "】设置对接");
					}
					
				}
			}
		}
		return pass;
	}
	
}
