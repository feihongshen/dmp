package cn.explink.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.CwbDAO;
import cn.explink.dao.WorkOrderDAO;
import cn.explink.domain.CsConsigneeInfo;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.CwbOrderAndCustomname;

@Service
public class WorkOrderService {
	@Autowired
	private WorkOrderDAO workorderdao;
	@Autowired
	private CwbDAO cwbdao;
	
	public void addcsconsigneeInfo(CsConsigneeInfo cci) {

				workorderdao.save(cci);
		
	}

	public CsConsigneeInfo querycciByPhoneNum(String phoneonOne) throws Exception {
		// TODO Auto-generated method stub
		return workorderdao.queryByPhoneNum(phoneonOne);
	}

	public List<CwbOrder> SelectCwbdetalForm(String phone) {
		return cwbdao.SelectDetalForm(phone);		
	}

}
