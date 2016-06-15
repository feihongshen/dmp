package cn.explink.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.core.common.model.json.DataGridReturn;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.IEmsOrderDao;
import cn.explink.domain.Customer;
import cn.explink.domain.EmsOrderPO;
import cn.explink.domain.QueryCondition;
import cn.explink.service.IEmsOrderManagerService;

@Service("emsOrderManagerService")
public class EmsOrderManagerService implements IEmsOrderManagerService{
	@Autowired
	private CustomerDAO customerDao;
	
	@Qualifier("emsOrderDao")
	@Autowired
	private IEmsOrderDao emsOrderDao ;
	
	public List<Customer> queryAllCustomers(){
		return customerDao.getAllCustomers();
	}
	
	/**
	 * 获得查询符合条件的列表
	 */
	public DataGridReturn queryEmsOrderList(QueryCondition qc){
		DataGridReturn dg = new DataGridReturn() ;
		int count = this.emsOrderDao.getEmsUnpushOrderCount(qc) ;
		List<EmsOrderPO> list = this.emsOrderDao.queryEmsUnpushOrder(qc) ;
		dg.setRows(list);
		dg.setTotal(count);
		return dg ;
	}
}
