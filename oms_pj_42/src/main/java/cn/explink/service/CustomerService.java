package cn.explink.service;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import cn.explink.domain.Customer;
import cn.explink.util.StringUtil;

@Service
public class CustomerService {

	public Customer loadFormForCustomer(HttpServletRequest request, long customerid) {
		Customer customer = this.loadFormForCustomer(request);
		customer.setCustomerid(customerid);
		return customer;
	}

	public Customer loadFormForCustomer(HttpServletRequest request) {
		Customer customer = new Customer();
		customer.setCustomername(StringUtil.nullConvertToEmptyString(request.getParameter("customername")));
		customer.setCustomeraddress(StringUtil.nullConvertToEmptyString(request.getParameter("customeraddress")));
		customer.setCustomercontactman(StringUtil.nullConvertToEmptyString(request.getParameter("customercontactman")));
		customer.setCustomerphone(StringUtil.nullConvertToEmptyString(request.getParameter("customerphone")));
		return customer;
	}

}
