package cn.explink.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.explink.dao.CwbDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.support.transcwb.TransCwbDao;

@Service
public class OneToMoreService {
	@Autowired
	TransCwbDao transCwbDao;
	@Autowired
	CwbDAO cwbDao;
	
	/*public String getMoreTranscwb(String cwb){
		List<TranscwbView> list = new ArrayList<TranscwbView>();
		list = transCwbDao.getTransCwbByCwb(cwb);
		String transcwbs = "";
		for(TranscwbView tcd : list){
			String transcwb = tcd.getTranscwb();
			transcwbs += transcwb+",";
		}
//		String transcwbss = transcwbs.substring(0, transcwbs.length()-1);
		return transcwbs;
	}*/
	
	public String compareWithDetail(String cwb){
		CwbOrder order = cwbDao.getCwbByCwb(cwb);
		String transcwbs = order.getTranscwb();
		return transcwbs;
	}
	public String replaceTranscwb(String cwb,String transcwb){
		String transcwbs = compareWithDetail(cwb);//detail 表中获取transcwb
//		String transcwbss = getMoreTranscwb(cwb);//transcwb 表中获取transcwb
		for(String str:transcwb.split("\r\n")){
			if(transcwbs.contains(str)){
				continue;
			}
			if(transcwbs.endsWith(",")){
				transcwbs += str+",";
			}else{
				transcwbs += ","+str+",";
			}
		}
		transcwbs=transcwbs.substring(0,transcwbs.length()-1);
		
		return transcwbs;
	}
	
}
