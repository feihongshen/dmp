package cn.explink.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.CustomerDAO;
import cn.explink.dao.PaybusinessbenefitsDao;
import cn.explink.domain.Customer;
import cn.explink.domain.Paybusinessbenefits;

@Service
public class PaybusinessbenefitsService {
	@Autowired
	PaybusinessbenefitsDao paybusinessbenefitsDao;
	@Autowired
	CustomerDAO customerDAO;
	public String getCustomerids(List<Customer> customers){
		String joint="'";
		StringBuffer buffer=new StringBuffer();
		if (customers!=null&&customers.size()>0) {
			for (Customer customer : customers) {
				buffer.append(joint).append(customer.getCustomerid()).append(joint).append(",");
			}
		}
		return buffer.length()>1?buffer.substring(0,buffer.length()-1).toString():"''";
	}
	public List<Paybusinessbenefits> addCustomerNameCustomers(List<Customer> customers,List<Paybusinessbenefits> paybusinessbenefits){
		for (Paybusinessbenefits paybusinessbenefit : paybusinessbenefits) {
			String customername="";
			if (customers.size()>0) {
				for (Customer customer : customers) {
					if (paybusinessbenefit.getCustomerid() == customer.getCustomerid()) {
						customername=customer.getCustomername();
						break;
					}
				}
			}
			paybusinessbenefit.setCustomername(customername);
		}
		return paybusinessbenefits;
	}
	public boolean checkIsCreateByCustomerid(long customerid){
		boolean flag=false;
		int count=paybusinessbenefitsDao.getPaybusinessbenefitsByCustomerid(customerid);
		if (count>0) {
			flag=true;
		}
		return flag;
	}
}
