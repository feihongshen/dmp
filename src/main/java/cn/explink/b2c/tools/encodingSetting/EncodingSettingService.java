package cn.explink.b2c.tools.encodingSetting;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.JiontDAO;
import cn.explink.dao.CustomerDAO;

@Service
public class EncodingSettingService {
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;

	public EncodingSetting LoadingExptEntity(HttpServletRequest request, long customerid) {
		EncodingSetting pc = new EncodingSetting();
		
		pc.setCustomerid(Integer.valueOf(request.getParameter("customerid") == null ? "0" : request.getParameter("customerid")));
		pc.setCustomercode(request.getParameter("customercode"));
		pc.setRemark(request.getParameter("remark"));

		return pc;
	}

}
