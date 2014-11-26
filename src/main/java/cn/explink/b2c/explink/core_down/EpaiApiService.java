package cn.explink.b2c.explink.core_down;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;

/**
 * 系统之间的对接（下游）
 * 
 * @author Administrator
 *
 */
@Service
public class EpaiApiService {
	private Logger logger = LoggerFactory.getLogger(EpaiApiService.class);

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	EpaiApiDAO epaiApiDAO;
	@Autowired
	DataImportService_B2c dataImportService_B2c;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	@Autowired
	CustomWareHouseDAO customWareHouseDAO;

	@Autowired
	CwbDAO cwbDAO;

	public EpaiApi loadingEpaiApiEntity(HttpServletRequest request, String usercode) {
		EpaiApi pc = new EpaiApi();
		pc.setCustomerid(Long.valueOf(request.getParameter("customerid")));
		pc.setFeedback_url(request.getParameter("feedback_url"));
		pc.setGetOrder_url(request.getParameter("getOrder_url"));
		pc.setPageSize(Integer.valueOf(request.getParameter("pageSize")));
		pc.setPrivate_key(request.getParameter("private_key"));

		pc.setUserCode(request.getParameter("userCode"));
		pc.setWarehouseid(Long.valueOf(request.getParameter("warehouseid")));
		pc.setCallBack_url(request.getParameter("callBack_url"));
		pc.setIsopenflag(Integer.valueOf(request.getParameter("isopenflag")));
		pc.setIsfeedbackflag(Integer.valueOf(request.getParameter("isfeedbackflag")));
		pc.setIspostflag(Integer.valueOf(request.getParameter("ispostflag")));

		return pc;
	}

}
