package cn.explink.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.CwbDAO;
import cn.explink.dao.WorkOrderDAO;
import cn.explink.domain.CsConsigneeInfo;
import cn.explink.domain.CwbOrder;

@Service
public class WorkOrderService {
	@Autowired
	private WorkOrderDAO workorderdao;
	@Autowired
	private CwbDAO cwbdao;
	
	public void addcsconsigneeInfo(CsConsigneeInfo cci) {
		if(cci!=null)
				workorderdao.save(cci);
		
	}

	public CsConsigneeInfo querycciByPhoneNum(String phoneonOne){
		
		return workorderdao.queryByPhoneNum(phoneonOne);
	}

	public List<CwbOrder> SelectCwbdetalForm(String phone) {
		List<CwbOrder> lc=cwbdao.SelectDetalForm(phone)==null?null:cwbdao.SelectDetalForm(phone);
		return lc;		
	}

}
