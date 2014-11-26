package cn.explink.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.AccountAreaDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.domain.AccountArea;
import cn.explink.domain.CustomWareHouse;
import cn.explink.domain.Customer;
import cn.explink.domain.EmailDate;

@Service
public class EmailDateService {

	@Autowired
	AccountAreaDAO accountAreaDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	CustomWareHouseDAO customWareHouseDAO;

	public void makeEmFullName(List<EmailDate> eList) {
		List<AccountArea> aList = accountAreaDAO.getAllAccountArea();
		Map<Long, AccountArea> aMap = new HashMap<Long, AccountArea>();
		for (AccountArea a : aList) {
			aMap.put(a.getAreaid(), a);
		}
		Map<Long, Customer> cMap = customerDAO.getAllCustomersToMap();
		List<CustomWareHouse> cwhList = customWareHouseDAO.getAllCustomWareHouse();
		Map<Long, CustomWareHouse> cwhMap = new HashMap<Long, CustomWareHouse>();
		for (CustomWareHouse cwh : cwhList) {
			cwhMap.put(cwh.getWarehouseid(), cwh);
		}
		for (EmailDate e : eList) {
			e.setAreaname(aMap.get(e.getAreaid()) == null ? "" : aMap.get(e.getAreaid()).getAreaname());
			e.setCustomername(cMap.get(e.getCustomerid()) == null ? "" : cMap.get(e.getCustomerid()).getCustomername());
			e.setWarehousename(cwhMap.get(e.getWarehouseid()) == null ? "" : cwhMap.get(e.getWarehouseid()).getCustomerwarehouse());
		}
	}

}
