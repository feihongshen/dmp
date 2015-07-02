package cn.explink.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.CwbDAO;
import cn.explink.dao.ExportwarhousesummaryDAO;
import cn.explink.domain.CwbOrder;

@Service
public class CustomerBillContractService {
	
	@Autowired
	private CwbDAO cwbdao;
	@Autowired
	private ExportwarhousesummaryDAO edao;
	
	
	public String listToString(List<CwbOrder> cwborderlist){
		StringBuilder sb = new StringBuilder();
		String cwbs="";	
			for(CwbOrder str:cwborderlist){
				sb=sb.append("'"+str.getCwb()+"',");
			}
			cwbs=sb.substring(0, sb.length()-1);	
		return cwbs;	
	}

	public List<CwbOrder> findAllByCwbDate(String cwbs,String startdate,String enddate) {
		List<CwbOrder> col=cwbdao.findcwbByCwbsAndDate(cwbs,startdate,enddate);
		return col;
	}

	public List<CwbOrder> findAllByCwbDateType(String cwbs, String startdate,
			String enddate, String cwbOrderType) {
		List<CwbOrder> col=cwbdao.findcwbByCwbsAndDateAndtype(cwbs,startdate,enddate,cwbOrderType);
		return col;
	}

	public List<CwbOrder> findcwbByCwbsAndDateAndtypeedao(String cwbs,
			String string, String string2, String cwbOrderType) {		
		return edao.findcwbByCwbsAndDateAndtype(cwbs, string, string2, cwbOrderType);
	}

}
