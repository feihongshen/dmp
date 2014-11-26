package cn.explink.b2c.tools.poscodeMapp;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.JiontDAO;
import cn.explink.dao.CustomerDAO;

@Service
public class PoscodeMappService {
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;

	public PoscodeMapp LoadingExptEntity(HttpServletRequest request, long posenum) {
		PoscodeMapp pc = new PoscodeMapp();
		pc.setPosenum(Integer.valueOf(request.getParameter("posenum") == null ? "0" : request.getParameter("posenum")));
		pc.setCustomerid(Integer.valueOf(request.getParameter("customerid") == null ? "0" : request.getParameter("customerid")));
		pc.setCustomercode(request.getParameter("customercode"));
		pc.setRemark(request.getParameter("remark"));

		return pc;
	}

}
